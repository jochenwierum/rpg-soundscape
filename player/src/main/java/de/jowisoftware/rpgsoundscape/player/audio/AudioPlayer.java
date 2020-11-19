package de.jowisoftware.rpgsoundscape.player.audio;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.javabackend.AbstractJavaAudioPlayer;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.sample.SampleStatus;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import java.io.InputStream;
import java.util.Arrays;

public abstract class AudioPlayer implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJavaAudioPlayer.class);

    protected final SampleRepository sampleRepository;
    protected final Mixer mixer;

    private byte[] clipBytes;
    private AudioFormat clipFormat;

    public AudioPlayer(SampleRepository sampleRepository, ApplicationSettings applicationSettings) {
        this.sampleRepository = sampleRepository;

        String mixerName = applicationSettings.getAudio().getMixer();
        if (mixerName == null) {
            mixer = null;
        } else {
            Mixer.Info mixerInfo = Arrays.stream(AudioSystem.getMixerInfo())
                    .filter(info -> info.getName().equals(mixerName))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Mixer " + mixerName + " does not exist"));

            mixer = AudioSystem.getMixer(mixerInfo);
        }
    }

    public static void printInfo() {
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            System.out.println("Name: " + mixerInfo.getName());
            System.out.println("  Vendor: " + mixerInfo.getVendor());
            System.out.println("  Description: " + mixerInfo.getDescription());
        }
    }

    @Override
    public void destroy() {
        if (mixer != null) {
            mixer.close();
        }
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

        try (SourceDataLine clip = (SourceDataLine) (mixer == null
                ? AudioSystem.getLine(new Info(SourceDataLine.class, clipFormat))
                : mixer.getLine(new Info(SourceDataLine.class, clipFormat)))) {
            clip.open(clipFormat);
            clip.start();
            clip.write(clipBytes, 0, clipBytes.length);
            clip.drain();
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
                    AudioInputStream ais = AudioSystem.getAudioInputStream(errorStream)) {
                clipFormat = ais.getFormat();
                clipBytes = ais.readAllBytes();
            } catch (Exception e) {
                LOG.error("Could not even load error sound.", e);
            }
        }
    }
}
