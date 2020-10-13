package de.jowisoftware.rpgsoundscape.player.audio;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.model.Sample;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.interpreter.PriorityTimer;
import de.jowisoftware.rpgsoundscape.player.threading.TrackExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import java.io.IOException;
import java.util.Arrays;

@Component
public class AudioPlayer implements InitializingBean, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(AudioPlayer.class);

    private final AudioConverter audioConverter;
    private final ApplicationSettings.Audio configuration;
    private final SampleRepository sampleRepository;
    private Mixer mixer = null;
    private final PriorityTimer timer = new PriorityTimer();

    public AudioPlayer(SampleRepository sampleRepository, AudioConverter audioConverter,
            ApplicationSettings configuration) {
        this.audioConverter = audioConverter;
        this.configuration = configuration.getAudio();
        this.sampleRepository = sampleRepository;
    }

    @Override
    public void afterPropertiesSet() {
        String mixerName = configuration.getMixer();
        if (mixerName != null) {
            Mixer.Info mixerInfo = Arrays.stream(AudioSystem.getMixerInfo())
                    .filter(info -> info.getName().equals(mixerName))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Mixer " + mixerName + " does not exist"));

            mixer = AudioSystem.getMixer(mixerInfo);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (mixer != null) {
            mixer.close();
        }
        timer.destroy();
    }

    public void play(TrackExecutionContext context, Play play) {
        Sample sample = play.sample();

        LookupResult resolvedSample = sampleRepository.lookup(sample);
        try (AudioInputStream is = audioConverter.openForPlaying(resolvedSample)) {
            Clip clip = createClip(mixer, is.getFormat());
            try {
                clip.open(is);
                LOG.trace("Starting to play {}", play);
                context.runInterruptible(new PausableClip(resolvedSample, timer, play, clip));
                LOG.trace("Completed play of {}", play);
            } catch (IOException | LineUnavailableException e) {
                clip.close();
                throw e;
            }
        } catch (Exception e) {
            LOG.warn("Unable to play resource '{}', marking resource as broken", play.sample().uri(), e);
            sampleRepository.markAsBroken(play, e);
        }
    }

    private Clip createClip(Mixer mixer, AudioFormat format) throws LineUnavailableException {
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
