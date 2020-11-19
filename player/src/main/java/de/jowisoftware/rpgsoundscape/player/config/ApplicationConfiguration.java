package de.jowisoftware.rpgsoundscape.player.config;

import de.jowisoftware.rpgsoundscape.player.audio.AudioConverter;
import de.jowisoftware.rpgsoundscape.player.audio.AudioPlayer;
import de.jowisoftware.rpgsoundscape.player.audio.ffmpeg.FfmpegPlayer;
import de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaAudioConverter;
import de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaClipAudioPlayer;
import de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaStreamAudioPlayer;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfiguration.class);

    @Bean
    public AudioPlayer audioPlayer(ApplicationSettings applicationSettings, SampleRepository sampleRepository) {
        LOG.info("Using backend: " + applicationSettings.getAudio().getBackend());
        return switch (applicationSettings.getAudio().getBackend()) {
            case JAVA_CLIP -> new JavaClipAudioPlayer(sampleRepository, applicationSettings);
            case JAVA_STREAM -> new JavaStreamAudioPlayer(sampleRepository, applicationSettings);
            case FFMPEG_STREAM -> new FfmpegPlayer(sampleRepository, applicationSettings);
        };
    }

    @Bean
    public AudioConverter audioConverter(ApplicationSettings applicationSettings) {
        return switch (applicationSettings.getAudio().getBackend()) {
            case JAVA_CLIP, JAVA_STREAM -> new JavaAudioConverter();
            default -> AudioConverter.NULL_CONVERTER;
        };
    }
}
