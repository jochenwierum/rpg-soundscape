package de.jowisoftware.rpgsoundscape.player.audio.backend.discord.bot;

import de.jowisoftware.rpgsoundscape.player.audio.backend.discord.DiscordMixer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(value = "application.audio.backend", havingValue = "DISCORD")
public class DiscordEventListener extends ListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordEventListener.class);

    private final DiscordMixer discordMixer;
    private AudioManager currentAudioManager;

    public DiscordEventListener(DiscordMixer discordMixer) {
        this.discordMixer = discordMixer;
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        event.getChannel().sendMessage(
                """
                At the moment, I do not support private messages. Please use a channel on a guild (server).
                If do don't know what to do, type `~help` in any channel I can read.
                """).queue();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        User author = message.getAuthor();

        // Ignore message if bot
        if (author.isBot()) {
            return;
        }

        String content = message.getContentRaw();
        LOG.trace("Got message: '" + content + "'");
        if (!content.startsWith("~")) {
            return;
        }

        LOG.info("Got bot message: '" + content.substring(1) + "'");
        handleChannelChat(event, content.substring(1).trim());
    }

    private void handleChannelChat(GuildMessageReceivedEvent event, String content) {
        int spaceIndex = content.indexOf(' ');
        String keyword = spaceIndex > 0 ? content.substring(0, spaceIndex) : content;
        String additionalCommand = spaceIndex > 0 ? content.substring(spaceIndex + 1) : null;

        switch (keyword) {
            case "help" -> sendHelp(event);
            case "join" -> joinChannel(event, additionalCommand);
            case "leave" -> leaveVoiceChannel();
            case "play" -> play(event);
            case "pause" -> pause(event);
        }
    }

    private void play(GuildMessageReceivedEvent event) {
        if (currentAudioManager == null) {
            reply(event, "Sorry, I have to join a audio channel first!");
            return;
        }

        discordMixer.play();

        reply(event, "Okay!");
    }


    private void pause(GuildMessageReceivedEvent event) {
        if (currentAudioManager == null) {
            reply(event, "Sorry, I have to join a audio channel first!");
            return;
        }

        discordMixer.pause();

        reply(event, "Okay!");
    }

    private void joinChannel(GuildMessageReceivedEvent event, String nameOrId) {
        if (nameOrId != null) {
            joinNamedChannel(event, nameOrId);
        } else {
            joinUsersChannel(event);
        }
    }

    private void sendHelp(GuildMessageReceivedEvent event) {
        reply(event,
                """
                I know the following commands:
                `~help` - print this help
                `~join` [channel] - to join a channel
                `~leave` - to leave the current channel
                `~play` - to start playing sounds (only required after `pause¸)
                `~pause` - to pause playing sounds)
                """);
    }

    private void joinUsersChannel(GuildMessageReceivedEvent event) {
        Optional.ofNullable(event.getMember())
                .map(Member::getVoiceState)
                .ifPresent(voiceState -> {
                    VoiceChannel channel = voiceState.getChannel();

                    if (channel != null) {
                        joinVoiceChat(channel);
                        reply(event, "Joining you in `" + channel.getName() + "`");
                    } else {
                        reply(event,
                                """
                                I could not find you in any voice channel.
                                Please use `~join name-of-the-channel` or join a channel first.
                                """);
                    }
                });
    }

    private void joinNamedChannel(GuildMessageReceivedEvent event, String nameOrId) {
        Guild guild = event.getGuild();

        VoiceChannel channel = null;
        if (nameOrId.matches("^\\d+$")) {
            channel = guild.getVoiceChannelById(nameOrId);
        }
        if (channel == null) {
            List<VoiceChannel> channels = guild.getVoiceChannelsByName(nameOrId, true);
            if (!channels.isEmpty()) {
                channel = channels.get(0);
            }
        }

        TextChannel textChannel = event.getChannel();
        if (channel == null) {
            reply(textChannel, "Unable to connect to ``" + nameOrId + "``, no such channel!");
            return;
        }
        joinVoiceChat(channel);
        reply(textChannel, "Connecting to `" + channel.getName() + "`");
    }

    private void reply(GuildMessageReceivedEvent event, String text) {
        reply(event.getChannel(), text);
    }

    private void reply(MessageChannel channel, String text) {
        channel.sendMessage(text).queue();
    }

    private void joinVoiceChat(VoiceChannel channel) {
        leaveVoiceChannel();

        LOG.info("Joining '%s' (%s) on '%s' (%s)".formatted(channel.getName(), channel.getId(),
                channel.getGuild().getName(), channel.getGuild().getId()));
        currentAudioManager = channel.getGuild().getAudioManager();

        currentAudioManager.setSelfDeafened(true);
        currentAudioManager.setSendingHandler(new DiscordAudioHandler(discordMixer));
        currentAudioManager.openAudioConnection(channel);

        discordMixer.play();
    }

    private void leaveVoiceChannel() {
        LOG.info("Leaving current channel...");
        discordMixer.pause();
        if (currentAudioManager != null) {
            currentAudioManager.closeAudioConnection();
        }
    }
}
