package de.jowisoftware.rpgsoundscape.player.audio.javabackend;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.model.Sample;
import de.jowisoftware.rpgsoundscape.player.audio.AudioConverter;
import de.jowisoftware.rpgsoundscape.player.audio.AudioPlayer;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.threading.TrackExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.util.Arrays;

public abstract class AbstractJavaAudioPlayer implements AudioPlayer, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJavaAudioPlayer.class);

    private final AudioConverter audioConverter;
    private final SampleRepository sampleRepository;
    protected Mixer mixer = null;

    public AbstractJavaAudioPlayer(SampleRepository sampleRepository, AudioConverter audioConverter,
            ApplicationSettings applicationSettings) {
        this.audioConverter = audioConverter;
        this.sampleRepository = sampleRepository;

        String mixerName = applicationSettings.getAudio().getMixer();
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
    }

    @Override
    public void play(TrackExecutionContext context, Play play) {
        Sample sample = play.sample();

        LookupResult resolvedSample = sampleRepository.lookup(sample);
        try (AudioInputStream is = audioConverter.openForPlaying(resolvedSample)) {
            playStream(is, context, play, resolvedSample);
        } catch (Exception e) {
            LOG.warn("Unable to play resource '{}', marking resource as broken", play.sample().uri(), e);
            sampleRepository.markAsBroken(play, e);
        }
    }

    protected abstract void playStream(AudioInputStream is, TrackExecutionContext context, Play play, LookupResult resolvedSample)
            throws Exception;

    public static void printInfo() {
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            System.out.println("Name: " + mixerInfo.getName());
            System.out.println("  Vendor: " + mixerInfo.getVendor());
            System.out.println("  Description: " + mixerInfo.getDescription());
        }
    }
}
