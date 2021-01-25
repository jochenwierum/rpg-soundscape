package de.jowisoftware.rpgsoundscape.player.config;

import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioBackend;
import de.jowisoftware.rpgsoundscape.player.audio.backend.java.JavaAudioBackend;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.AudioConverter;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.AudioFrontend;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.FfmpegFrontend;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.java.JavaAudioConverter;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.java.JavaClipAudioFrontend;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.java.StreamingAudioFrontend;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfiguration.class);

    @Bean
    public AudioFrontend audioFrontend(ApplicationSettings applicationSettings, SampleRepository sampleRepository,
            AudioBackend audioBackend) {
        LOG.info("Using audio frontend: " + applicationSettings.getAudio().getFrontend());
        return switch (applicationSettings.getAudio().getFrontend()) {
            case JAVA_CLIP -> {
                if (audioBackend instanceof JavaAudioBackend jab) {
                    yield new JavaClipAudioFrontend(sampleRepository, jab);
                } else {
                    throw new IllegalStateException("Frontend JAVA_CLIP requires JAVA_AUDIO as backend");
                }
            }
            case JAVA_STREAM -> new StreamingAudioFrontend(sampleRepository, audioBackend);
            case FFMPEG_STREAM -> new FfmpegFrontend(sampleRepository, audioBackend);
        };
    }

    @Bean
    public AudioConverter audioConverter(ApplicationSettings applicationSettings, AudioBackend audioBackend) {
        return switch (applicationSettings.getAudio().getFrontend()) {
            case JAVA_CLIP, JAVA_STREAM -> new JavaAudioConverter(audioBackend);
            default -> AudioConverter.NULL_CONVERTER;
        };
    }
}
