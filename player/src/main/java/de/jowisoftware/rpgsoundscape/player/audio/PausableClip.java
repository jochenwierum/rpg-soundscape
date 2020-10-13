package de.jowisoftware.rpgsoundscape.player.audio;

import de.jowisoftware.rpgsoundscape.model.Modification.AmplificationModification;
import de.jowisoftware.rpgsoundscape.model.Modification.LimitModification;
import de.jowisoftware.rpgsoundscape.model.Modification.StartOmissionModification;
import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleStatus;
import de.jowisoftware.rpgsoundscape.player.interpreter.PriorityTimer;
import de.jowisoftware.rpgsoundscape.player.threading.InterruptibleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import java.util.Optional;

public class PausableClip extends InterruptibleTask {
    private static final Logger LOG = LoggerFactory.getLogger(PausableClip.class);

    private final PriorityTimer timer;
    private final Play play;
    private final Clip clip;
    private volatile boolean paused = false;

    public PausableClip(
            LookupResult lookupResult,
            PriorityTimer priorityTimer,
            Play play, Clip clip) {
        this.timer = priorityTimer;
        this.play = play;
        this.clip = clip;

        Optional<Long> millisToPlay = Optional.empty();
        if (!(lookupResult.sampleStatus() == SampleStatus.ERROR)) {
            modifyAmplification();
            modifyStart();
            millisToPlay = millisToPlay();
        }

        setupEndOfStreamStopLogic(millisToPlay);
    }

    public void setupEndOfStreamStopLogic(Optional<Long> millisToPlay) {
        millisToPlay.ifPresentOrElse(
                millis -> clip.addLineListener(closeOnTimerLineListener(millis)),
                () -> clip.addLineListener(closeOnCompletionLineListener()));
    }

    private LineListener closeOnTimerLineListener(long millisToPlay) {
        var stopTimer = timer.createTask(millisToPlay);
        stopTimer.onFinish(() -> {
            LOG.trace("Timer elapsed, closing track");
            abort();
        });

        LOG.trace("Installing timer line listener");
        return event -> {
            if (event.getType() == Type.START) {
                LOG.trace("Sample was started - starting timer");
                stopTimer.startOrResume();
            } else if (event.getType() == Type.STOP) {
                LOG.trace("Sample was stopped - stopping timer");
                stopTimer.pause();
            }
        };
    }

    private LineListener closeOnCompletionLineListener() {
        LOG.trace("Installing on-finished listener");
        return event -> {
            if (event.getType() == Type.STOP && !paused) {
                LOG.trace("Clip finished - closing track");
                abort();
            }
        };
    }

    private void modifyAmplification() {
        play.collectModifications(AmplificationModification.class, AmplificationModification::merge)
                .ifPresent(amplification -> {
                    double volume = Math.max(Math.min(1.0 + amplification.percentage().toDouble(), 2.0), 0.0);
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    float value = 20f * (float) Math.log10(volume);
                    LOG.trace("Modifying volume to factor {} ({} dB)", volume, value);
                    gainControl.setValue(value);
                });
    }

    private Optional<Long> millisToPlay() {
        return play.collectModifications(LimitModification.class, LimitModification::merge)
                .map(omission -> omission.duration().toMillis());
    }

    private void modifyStart() {
        play.collectModifications(StartOmissionModification.class, StartOmissionModification::merge)
                .map(omission -> (long) (omission.duration().toMillis() * 1000.0))
                .ifPresent(microseconds -> {
                    LOG.trace("Skip first {} microseconds", microseconds);
                    clip.setMicrosecondPosition(microseconds);
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
