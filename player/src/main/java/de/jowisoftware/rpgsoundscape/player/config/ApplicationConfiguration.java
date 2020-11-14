package de.jowisoftware.rpgsoundscape.player.config;

import de.jowisoftware.rpgsoundscape.player.audio.AudioConverter;
import de.jowisoftware.rpgsoundscape.player.audio.AudioConverter.NullAudioConverter;
import de.jowisoftware.rpgsoundscape.player.audio.AudioPlayer;
import de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaAudioConverter;
import de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaClipAudioPlayer;
import de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaStreamAudioPlayer;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public AudioPlayer audioPlayer(ApplicationSettings applicationSettings, SampleRepository sampleRepository, AudioConverter audioConverter) {
        return switch (applicationSettings.getAudio().getBackend()) {
            case JAVA_SOUND_CLIP -> new JavaClipAudioPlayer(sampleRepository, audioConverter, applicationSettings);
            case JAVA_SOUND_STREAM -> new JavaStreamAudioPlayer(sampleRepository, audioConverter, applicationSettings);
        };
    }

    @Bean
    public AudioConverter audioConverter(ApplicationSettings applicationSettings) {
        return switch (applicationSettings.getAudio().getBackend()) {
            case JAVA_SOUND_CLIP, JAVA_SOUND_STREAM -> new JavaAudioConverter();
            default -> new NullAudioConverter();
        };
    }
}
