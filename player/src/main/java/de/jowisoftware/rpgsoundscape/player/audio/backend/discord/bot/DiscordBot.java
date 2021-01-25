package de.jowisoftware.rpgsoundscape.player.audio.backend.discord.bot;

import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
@ConditionalOnProperty(value = "application.audio.backend", havingValue = "DISCORD")
public class DiscordBot implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordBot.class);

    private final JDA jda;

    public static final EnumSet<GatewayIntent> INTENTS = EnumSet.of(
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_VOICE_STATES
    );

    public DiscordBot(DiscordEventListener discordEventListener, ApplicationSettings settings) throws Exception {
        String token = settings.getDiscord().getToken();

        LOG.info("Connecting to Discord bot...");
        jda = JDABuilder.createLight(token, INTENTS)
                .addEventListeners(discordEventListener)
                .setStatus(OnlineStatus.ONLINE)
                .setMemberCachePolicy(MemberCachePolicy.VOICE)
                .enableCache(CacheFlag.VOICE_STATE)
                .build();

        LOG.info("Waiting for successful connection...");
        jda.awaitReady();

        LOG.info("Bot is ready!");
    }

    @Override
    public void destroy() throws Exception {
        LOG.info("Shutting down discord connection");
        jda.shutdownNow();
        LOG.info("Connection closed");
    }
}
