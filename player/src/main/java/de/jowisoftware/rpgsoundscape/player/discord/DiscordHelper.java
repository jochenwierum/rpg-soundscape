package de.jowisoftware.rpgsoundscape.player.discord;

import de.jowisoftware.rpgsoundscape.player.audio.backend.discord.DiscordMixer;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.config.DiscordComponent;
import de.jowisoftware.rpgsoundscape.player.threading.DebounceService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildJoinedEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@DiscordComponent
public class DiscordHelper {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordHelper.class);
    private final Runnable refreshGuilds;
    private final JDA jda;

    private final Set<Long> guildsById = ConcurrentHashMap.newKeySet();
    private final Map<String, Long> guildsByName = new ConcurrentHashMap<>();
    private final List<Entry> guildsByIndex = new CopyOnWriteArrayList<>();
    private final Set<String> allowedUsers;
    private final DiscordMixer discordMixer;
    private final String inviteUrl;

    private AudioManager currentAudioManager;

    public DiscordHelper(JDA jda, DebounceService debounceService,
            ApplicationSettings applicationSettings,
            DiscordMixer discordMixer) {
        this.refreshGuilds = debounceService.createDebouncer("discord-guild-refresh",
                this::refreshGuilds, 500, 3000);
        this.jda = jda;
        this.allowedUsers = applicationSettings.getDiscord().getAllowedUsers();
        this.discordMixer = discordMixer;

        inviteUrl = jda.getInviteUrl(
                Permission.VIEW_CHANNEL,

                Permission.MESSAGE_READ,
                Permission.MESSAGE_WRITE,

                Permission.VOICE_CONNECT,
                Permission.VOICE_SPEAK,

                Permission.MESSAGE_ADD_REACTION
        );

        jda.addEventListener(new GuildEventListener());
        refreshGuilds.run();
    }

    public synchronized Optional<String> currentJoinedChannel() {
        return Optional.ofNullable(currentAudioManager)
                .map(AudioManager::getConnectedChannel)
                .map(ISnowflake::getId);
    }

    public synchronized Optional<String> currentJoinedGuild() {
        return Optional.ofNullable(currentAudioManager)
                .map(AudioManager::getGuild)
                .map(ISnowflake::getId);
    }

    private void refreshGuilds() {
        guildsByIndex.clear();
        guildsById.clear();
        guildsByName.clear();

        jda.getGuilds().stream()
                .sorted(Comparator.comparing(Guild::getName))
                .forEach(guild -> {
                    guildsByIndex.add(new Entry(guild.getIdLong(), guild.getName()));
                    guildsByName.put(guild.getName().toLowerCase(Locale.ROOT), guild.getIdLong());
                    guildsById.add(guild.getIdLong());
                });
    }

    public List<Entry> getGuilds() {
        return new ArrayList<>(guildsByIndex);
    }

    public Guild getGuild(String argument) throws BotCommandException {
        Long guildId = null;
        argument = argument.toLowerCase(Locale.ROOT);

        if (argument.matches("^\\d+$")) {
            int numericId = Integer.parseInt(argument);
            if (numericId > 0 && numericId <= guildsByIndex.size()) {
                guildId = guildsByIndex.get(numericId - 1).id();
            }
        }

        if (guildId == null) {
            guildId = guildsByName.get(argument);
        }

        if (guildId == null && argument.matches("^#\\d+$")) {
            guildId = Long.parseLong(argument.substring(1));
        }

        if (guildId == null) {
            throw new BotCommandException("I don't know which guild you mean");
        }

        Guild guild = jda.getGuildById(guildId);
        if (guild == null) {
            throw new BotCommandException("I am not a member of this guild or the guild does not exist");
        }
        return guild;
    }

    public void validateMessage(MessageWrapper message) throws BotCommandException {
        if (allowedUsers.isEmpty()) {
            return;
        }

        String author = message.author().getId();
        if (!allowedUsers.contains(author)) {
            throw new BotCommandException(
                    """
                    You are not allowed to execute any commands.
                    Please add your user id (`%s`) to the `discord.allowedUsers` in the soundscape application first.
                    """.formatted(author));
        }
    }

    public boolean joinVoiceChat(String guildId, String channelId) {
        Guild guild = jda.getGuildById(guildId);
        if (guild == null) {
            return false;
        }

        VoiceChannel channel = guild.getVoiceChannelById(channelId);
        if (channel == null) {
            return false;
        }

        joinVoiceChat(channel);
        return true;
    }

    public synchronized void joinVoiceChat(VoiceChannel channel) {
        leaveVoiceChannel();

        LOG.info("Joining '%s' (%s) on '%s' (%s)".formatted(channel.getName(), channel.getId(),
                channel.getGuild().getName(), channel.getGuild().getId()));

        currentAudioManager = channel.getGuild().getAudioManager();

        currentAudioManager.setSelfDeafened(true);
        currentAudioManager.setSendingHandler(new DiscordAudioHandler(discordMixer));
        currentAudioManager.openAudioConnection(channel);

        discordMixer.play();
    }

    public synchronized void leaveVoiceChannel() {
        LOG.info("Leaving current channel...");
        discordMixer.pause();
        if (currentAudioManager != null) {
            currentAudioManager.closeAudioConnection();
            currentAudioManager = null;
        }
    }

    public String createInviteUrl() {
        return inviteUrl;
    }

    public Optional<List<Entry>> listChannels(String guildId) {
        Guild guild = jda.getGuildById(guildId);

        if (guild == null) {
            return Optional.empty();
        }

        return Optional.of(guild.getVoiceChannels().stream()
                .sorted(Comparator.comparing(GuildChannel::getName))
                .map(vc -> new Entry(vc.getIdLong(), vc.getName()))
                .collect(Collectors.toList()));
    }

    private class GuildEventListener extends ListenerAdapter {
        @Override
        public void onGuildJoin(@NotNull GuildJoinEvent event) {
            refreshGuilds.run();
        }

        @Override
        public void onGuildLeave(@NotNull GuildLeaveEvent event) {
            refreshGuilds.run();
        }

        @Override
        public void onUnavailableGuildJoined(@NotNull UnavailableGuildJoinedEvent event) {
            refreshGuilds.run();
        }

        @Override
        public void onUnavailableGuildLeave(@NotNull UnavailableGuildLeaveEvent event) {
            refreshGuilds.run();
        }
    }

    public static record Entry(
            long id,
            String name) {
    }
}
