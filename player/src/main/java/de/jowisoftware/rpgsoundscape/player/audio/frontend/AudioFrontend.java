package de.jowisoftware.rpgsoundscape.player.audio.frontend;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioBackend;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioStream;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.java.AbstractJavaAudioFrontend;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.sample.SampleStatus;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.InputStream;
import java.nio.ByteBuffer;

public abstract class AudioFrontend {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJavaAudioFrontend.class);

    protected final SampleRepository sampleRepository;
    protected final AudioBackend audioBackend;

    private byte[] clipBytes;
    private AudioFormat clipFormat;

    public AudioFrontend(SampleRepository sampleRepository, AudioBackend audioBackend) {
        this.audioBackend = audioBackend;
        this.sampleRepository = sampleRepository;
    }


    public void play(BlockExecutionContext context, Play play) {
        LookupResult resolvedSample = sampleRepository.lookup(play.sample());

        if (resolvedSample.sampleStatus() == SampleStatus.RESOLVED) {
            try {
                play(resolvedSample, context, play);
            } catch (Exception e) {
                LOG.warn("Unable to play resource '{}', marking resource as broken", play.sample().uri(), e);
                sampleRepository.markAsBroken(play, e);
            }
        } else {
            playErrorSound();
        }
    }

    abstract protected void play(LookupResult resolvedSample, BlockExecutionContext context, Play play)
            throws Exception;

    private void playErrorSound() {
        if (clipBytes == null) {
            cacheErrorSound();
        }

        try (AudioStream audioStream = audioBackend.openStream(clipFormat)) {
            audioStream.resume();
            audioStream.write(ByteBuffer.wrap(clipBytes));
            audioStream.close(true);
        } catch (Exception e) {
            LOG.error("Could even play error sound.", e);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private synchronized void cacheErrorSound() {
        if (clipBytes == null) {
            try (
                    InputStream errorStream = getClass().getResourceAsStream("/error.wav");
                    AudioInputStream ais = AudioSystem.getAudioInputStream(errorStream);
                    AudioInputStream converted = AudioSystem.getAudioInputStream(audioBackend.deriveTargetFormat(ais.getFormat()), ais)
            ) {
                clipFormat = ais.getFormat();
                clipBytes = converted.readAllBytes();
            } catch (Exception e) {
                LOG.error("Could not even load error sound.", e);
            }
        }
    }
}
