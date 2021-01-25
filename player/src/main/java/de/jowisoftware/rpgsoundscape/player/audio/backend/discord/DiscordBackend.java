package de.jowisoftware.rpgsoundscape.player.audio.backend.discord;

import de.jowisoftware.rpgsoundscape.player.audio.JavaAudioUtils;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioBackend;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioStream;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings.Audio.Backend;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static net.dv8tion.jda.api.audio.AudioSendHandler.INPUT_FORMAT;

@Component
@ConditionalOnProperty(value = "application.audio.backend", havingValue = "DISCORD")
public class DiscordBackend implements AudioBackend {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordBackend.class);

    public static final int CHUNK_SIZE = (int) JavaAudioUtils.bytesPerMilliseconds(INPUT_FORMAT, 20);
    public static final int BUFFER_LENGTH = CHUNK_SIZE / 2;

    private final Set<BlockingBuffer<float[]>> streams = new CopyOnWriteArraySet<>();

    public DiscordBackend() {
        LOG.info("Using audio backend: " + Backend.DISCORD);
    }

    @Override
    public AudioStream openStream(AudioFormat format) {
        if (!AudioSendHandler.INPUT_FORMAT.matches(format)) {
            throw new IllegalArgumentException("Unsupported format, required: " + AudioSendHandler.INPUT_FORMAT);
        }

        var queue = new BlockingBuffer<>(16, () -> new float[BUFFER_LENGTH]);
        streams.add(queue);
        return new DiscordAudioStream(this, queue);
    }

    void closeStream(BlockingBuffer<float[]> queue) {
        streams.remove(queue);
        queue.close();
    }

    @Override
    public AudioFormat deriveTargetFormat(AudioFormat sourceFormat) {
        return AudioSendHandler.INPUT_FORMAT;
    }

    public Set<BlockingBuffer<float[]>> getStreams() {
        return streams;
    }
}
