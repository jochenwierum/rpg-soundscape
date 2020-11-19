package de.jowisoftware.rpgsoundscape.player.audio.javabackend;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

import static de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaAudioUtils.bytesToPlay;
import static de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaAudioUtils.skipStart;

public class JavaClipAudioPlayer extends AbstractJavaAudioPlayer {
    private static final Logger LOG = LoggerFactory.getLogger(JavaClipAudioPlayer.class);

    public JavaClipAudioPlayer(SampleRepository sampleRepository, ApplicationSettings applicationSettings) {
        super(sampleRepository, applicationSettings);
    }

    @Override
    protected void playStream(AudioInputStream inputStream, BlockExecutionContext context, Play play, LookupResult resolvedSample)
            throws Exception {
        try (Clip clip = createClip(inputStream.getFormat())) {
            try (inputStream) {
                byte[] bytes = getBytesToPlay(inputStream, play, resolvedSample);
                clip.open(inputStream.getFormat(), bytes, 0, bytes.length);
            }

            LOG.trace("Starting to play {}", play);
            context.runInterruptible(new PausableClip(play, clip));
            LOG.trace("Completed play of {}", play);
        }
    }

    private byte[] getBytesToPlay(AudioInputStream inputStream, Play play, LookupResult resolvedSample) {
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

    private Clip createClip(AudioFormat format) throws LineUnavailableException {
        if (mixer == null) {
            return (Clip) AudioSystem.getLine(new Info(Clip.class, format));
        } else {
            return (Clip) mixer.getLine(new Info(Clip.class, format));
        }
    }
}
