package de.jowisoftware.rpgsoundscape.player.discord;

import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Configuration
@ConditionalOnProperty(value = "application.audio.backend", havingValue = "DISCORD")
public class DiscordBotConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordBotConfiguration.class);

    public static final EnumSet<GatewayIntent> INTENTS = EnumSet.of(
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_VOICE_STATES
    );

    @Bean(destroyMethod = "shutdownNow")
    public JDA jda(ApplicationSettings settings) throws Exception {
        String token = settings.getDiscord().getToken();

        LOG.info("Connecting to Discord bot...");
        JDA jda = JDABuilder.createLight(token, INTENTS)
                .setStatus(OnlineStatus.ONLINE)
                .setMemberCachePolicy(MemberCachePolicy.VOICE)
                .enableCache(CacheFlag.VOICE_STATE)
                .build();

        LOG.info("Waiting for successful connection...");
        jda.awaitReady();

        LOG.info("Bot is ready!");

        return jda;
    }
}
