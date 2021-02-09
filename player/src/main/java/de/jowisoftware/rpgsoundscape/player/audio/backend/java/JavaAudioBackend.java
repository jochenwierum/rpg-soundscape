package de.jowisoftware.rpgsoundscape.player.audio.backend.java;

import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioBackend;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioStream;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings.Audio.Backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import java.util.Arrays;

@Component
@ConditionalOnProperty(value = "application.audio.backend", havingValue = "JAVA_AUDIO", matchIfMissing = true)
public class JavaAudioBackend implements AudioBackend, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(JavaAudioBackend.class);

    private static final AudioFormat TARGET_FORMAT = new AudioFormat(
            Encoding.PCM_SIGNED,
            44100,
            16,
            2,
            4,
            44100,
            false);

    private final Mixer mixer;
    private final float maxSampleRate;

    public JavaAudioBackend(ApplicationSettings applicationSettings) {
        LOG.info("Using audio backend: " + Backend.JAVA_AUDIO);

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

        this.maxSampleRate = applicationSettings.getCache().getCacheMaxSampleRate();
    }

    @Override
    public AudioStream openStream(AudioFormat format) throws Exception {
        SourceDataLine line;
        Info lineInfo = new Info(SourceDataLine.class, format);
        if (mixer == null) {
            line = (SourceDataLine) AudioSystem.getLine(lineInfo);
        } else {
            line = (SourceDataLine) mixer.getLine(lineInfo);
        }
        line.open(format);

        return new JavaAudioStream(line);
    }

    @Override
    public AudioFormat deriveTargetFormat(AudioFormat sourceFormat) {
        if (sourceFormat == null) {
            return TARGET_FORMAT;
        }

        if (sourceFormat.getEncoding() == Encoding.PCM_SIGNED
                && sourceFormat.getSampleSizeInBits() == 16
                && !sourceFormat.isBigEndian()) {
            return sourceFormat;
        }

        float sampleRate = sourceFormat.getSampleRate();
        sampleRate = maxSampleRate == 0 ? sampleRate : Math.min(sampleRate, maxSampleRate);

        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                16,
                sourceFormat.getChannels(),
                sourceFormat.getChannels() * 2,
                sampleRate,
                false);
    }

    @Override
    public void destroy() {
        if (mixer != null) {
            mixer.close();
        }
    }

    public Clip createClip(AudioFormat format) throws LineUnavailableException {
        if (mixer == null) {
            return (Clip) AudioSystem.getLine(new Info(Clip.class, format));
        } else {
            return (Clip) mixer.getLine(new Info(Clip.class, format));
        }
    }

    public static void printInfo() {
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            System.out.println("Name: " + mixerInfo.getName());
            System.out.println("  Vendor: " + mixerInfo.getVendor());
            System.out.println("  Description: " + mixerInfo.getDescription());
        }
    }

}
