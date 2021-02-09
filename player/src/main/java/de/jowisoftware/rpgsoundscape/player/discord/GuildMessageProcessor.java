package de.jowisoftware.rpgsoundscape.player.discord;

import de.jowisoftware.rpgsoundscape.player.config.DiscordComponent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@DiscordComponent
public class GuildMessageProcessor extends ListenerAdapter {
    private final CommonMessageProcessor commonMessageProcessor;
    private final DiscordHelper discordHelper;

    public GuildMessageProcessor(CommonMessageProcessor commonMessageProcessor, JDA jda, DiscordHelper discordHelper) {
        this.commonMessageProcessor = commonMessageProcessor;
        this.discordHelper = discordHelper;
        jda.addEventListener(this);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getMessage().getAuthor().isBot()) {
            return;
        }

        if (event.getMessage().isMentioned(event.getJDA().getSelfUser())) {
            return;
        }

        MessageWrapper message = new GuildMessageWrapper(event);
        try {
            discordHelper.validateMessage(message);
            String token = message.consumeNextToken();
            if (token.toLowerCase(Locale.ROOT).equals("help")) {
                sendHelp(false, message);
            } else if (!commonMessageProcessor.processMessage(token, message)) {
                sendHelp(true, message);
            }
        } catch (BotCommandException e) {
            e.sendReply(message);
        }
    }


    private void sendHelp(boolean warn, MessageWrapper message) {
        message.reply(
                """
                %sI know the following commands:
                `join [channel]` - I join a voice channel. I will join your current channel, if you don't provide one.
                %s
                """.formatted(
                        (warn ? "I did not understand you...\n\n" : ""),
                        CommonMessageProcessor.HELP_STRING));
    }

    public static class GuildMessageWrapper extends MessageWrapper {
        private final GuildMessageReceivedEvent event;

        public GuildMessageWrapper(@NotNull GuildMessageReceivedEvent event) {
            super(event.getMember(), event.getAuthor(), event.getMessage(), event.getMessage().getContentRaw().replaceAll("\\B@[-a-z0-9_]+\\b", "").trim());
            this.event = event;
        }

        @Override
        public Guild consumeGuild() {
            return event.getGuild();
        }

        @Override
        public void reply(CharSequence text) {
            event.getChannel().sendMessage(text).queue();
        }
    }
}
