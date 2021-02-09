package de.jowisoftware.rpgsoundscape.player.discord;

import de.jowisoftware.rpgsoundscape.player.config.DiscordComponent;
import de.jowisoftware.rpgsoundscape.player.config.PersistableState;
import de.jowisoftware.rpgsoundscape.player.discord.DiscordHelper.Entry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

@DiscordComponent
public class PrivateMessageProcessor extends ListenerAdapter {
    public static final String PERSISTENT_ID_KEY = "discord.selectedGuild";
    private final CommonMessageProcessor commonMessageProcessor;
    private final DiscordHelper discordHelper;
    private String selectedGuildId;
    private final PersistableState persistableState;

    public PrivateMessageProcessor(
            CommonMessageProcessor commonMessageProcessor,
            DiscordHelper discordHelper,
            JDA jda,
            PersistableState persistableState) {
        this.commonMessageProcessor = commonMessageProcessor;
        this.discordHelper = discordHelper;
        this.persistableState = persistableState;
        this.selectedGuildId = persistableState.get(PERSISTENT_ID_KEY);

        jda.addEventListener(this);
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        MessageWrapper message = new PrivateMessageWrapper(event);
        try {
            discordHelper.validateMessage(message);
            handleMessage(message);
        } catch (BotCommandException e) {
            e.sendReply(message);
        }
    }

    private void handleMessage(MessageWrapper message) throws BotCommandException {
        String token = message.consumeNextToken();
        switch (token.toLowerCase(Locale.ROOT)) {
            case "guilds" -> sendGuilds(message);
            case "guild" -> selectGuild(message);
            case "help" -> sendHelp(false, message);
            default -> {
                if (!commonMessageProcessor.processMessage(token, message)) {
                    sendHelp(true, message);
                }
            }
        }
    }

    private void selectGuild(MessageWrapper message) throws BotCommandException {
        if (!message.previewNextToken().startsWith("#")) {
            throw new BotCommandException("A guild must be provided.");
        }

        this.selectedGuildId = message.consumeGuild().getId();
        persistableState.set(PERSISTENT_ID_KEY, selectedGuildId);
        message.acknowledge();
    }

    private void sendGuilds(MessageWrapper message) {
        StringBuilder b = new StringBuilder("I know the following guilds:\n");
        List<Entry> guilds = discordHelper.getGuilds();

        for (int i = 0; i < guilds.size(); i++) {
            Entry guild = guilds.get(i);
            b.append(" - `#")
                    .append(i + 1)
                    .append("`, `#")
                    .append(guild.name())
                    .append("` or `##")
                    .append(guild.id())
                    .append("`\n");
        }

        message.reply(b);
    }

    private void sendHelp(boolean warn, MessageWrapper message) {
        message.reply(
                """
                %sI know the following commands:
                `guilds` - I show all guilds I know.
                `guild #guild` - I select a default guild.

                `join [#SERVER] [channel]` - I join a voice channel. I will join your current channel, if you don't provide one.
                %s
                """.formatted(
                        (warn ? "I did not understand you...\n\n" : ""),
                        CommonMessageProcessor.HELP_STRING));
    }

    private class PrivateMessageWrapper extends MessageWrapper {
        private final PrivateMessageReceivedEvent event;

        public PrivateMessageWrapper(PrivateMessageReceivedEvent event) {
            super(null, event.getAuthor(), event.getMessage(), event.getMessage().getContentRaw());
            this.event = event;
        }

        @Override
        public Guild consumeGuild() throws BotCommandException {
            String guildId;
            if (!components.isEmpty() && this.components.peekFirst().startsWith("#")) {
                guildId = components.removeFirst().substring(1);
            } else if (PrivateMessageProcessor.this.selectedGuildId != null) {
                guildId = "#" + selectedGuildId;
            } else {
                throw new BotCommandException("No guild provided (use either `#guild` or send me `guild #guild` first)!");
            }

            return discordHelper.getGuild(guildId);
        }

        @Override
        public void reply(CharSequence text) {
            event.getMessage().reply(text).queue();
        }
    }
}
