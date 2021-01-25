package de.jowisoftware.rpgsoundscape.player.audio.frontend.java;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.backend.java.JavaAudioBackend;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

import static de.jowisoftware.rpgsoundscape.player.audio.JavaAudioUtils.bytesToPlay;
import static de.jowisoftware.rpgsoundscape.player.audio.JavaAudioUtils.skipStart;

public class JavaClipAudioFrontend extends AbstractJavaAudioFrontend {
    private static final Logger LOG = LoggerFactory.getLogger(JavaClipAudioFrontend.class);

    public JavaClipAudioFrontend(SampleRepository sampleRepository, JavaAudioBackend audioBackend) {
        super(sampleRepository, audioBackend);
    }

    @Override
    protected void playStream(AudioInputStream inputStream, BlockExecutionContext context, Play play, LookupResult resolvedSample)
            throws Exception {

        try (Clip clip = ((JavaAudioBackend) audioBackend).createClip(inputStream.getFormat())) {
            try (inputStream) {
                byte[] bytes = getBytesToPlay(inputStream, play);
                clip.open(inputStream.getFormat(), bytes, 0, bytes.length);
            }

            LOG.trace("Starting to play {}", play);
            context.runInterruptible(new PausableClip(play, clip));
            LOG.trace("Completed play of {}", play);
        }
    }

    private byte[] getBytesToPlay(AudioInputStream inputStream, Play play) {
        skipStart(play, inputStream);
        Optional<byte[]> bytesToPlay = bytesToPlay(play, inputStream.getFormat())
                .map(byteCount -> {
                    try {
                        return inputStream.readNBytes(byteCount.intValue());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });

        return bytesToPlay.orElseGet(() -> {
            try {
                return inputStream.readAllBytes();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
