package de.jowisoftware.rpgsoundscape.player.audio.frontend.java;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.AbstractAsyncInterruptibleTaskAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent.Type;

import static de.jowisoftware.rpgsoundscape.player.audio.JavaAudioUtils.calculateAmplification;

class PausableClip extends AbstractAsyncInterruptibleTaskAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(PausableClip.class);

    private final Clip clip;
    private volatile boolean paused = false;

    public PausableClip(Play play, Clip clip) {
        this.clip = clip;

        calculateAmplification(play)
                .ifPresent(volume -> {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    volume = Math.min(Math.max(gainControl.getMinimum(), volume), gainControl.getMaximum());

                    LOG.trace("Modifying volume to {} dB", volume);
                    gainControl.setValue((float) volume);
                });

        clip.addLineListener(event -> {
            if (event.getType() == Type.STOP && !paused) {
                LOG.trace("Clip finished - closing track");
                abort();
            }
        });
    }

    @Override
    public void pause() {
        paused = true;
        clip.stop();
    }

    @Override
    public void startOrResume() {
        paused = false;
        clip.start();
    }

    @Override
    public void abort() {
        clip.stop();
        clip.close();
        finish();
    }
}
