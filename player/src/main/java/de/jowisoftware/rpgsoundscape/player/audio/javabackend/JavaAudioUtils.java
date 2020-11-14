package de.jowisoftware.rpgsoundscape.player.audio.javabackend;

import de.jowisoftware.rpgsoundscape.model.Modification.AmplificationModification;
import de.jowisoftware.rpgsoundscape.model.Modification.LimitModification;
import de.jowisoftware.rpgsoundscape.model.Modification.StartOmissionModification;
import de.jowisoftware.rpgsoundscape.model.Play;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import java.io.IOException;
import java.util.Optional;

public final class JavaAudioUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JavaAudioUtils.class);

    private JavaAudioUtils(){}

    public static void modifyAmplification(Play play, Line line) {
        play.collectModifications(AmplificationModification.class, AmplificationModification::merge)
                .ifPresent(amplification -> {
                    double volume = Math.max(Math.min(1.0 + amplification.percentage().toDouble(), 2.0), 0.0);
                    FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                    float value = 20f * (float) Math.log10(volume);
                    LOG.trace("Modifying volume to factor {} ({} dB)", volume, value);
                    gainControl.setValue(value);
                });
    }

    public static float bytesPerMillisecond(AudioFormat format) {
        return format.getFrameSize() * format.getFrameRate() / 1000;
    }

    public static Optional<Long> bytesToSkip(Play play, AudioFormat format) {
        return play.collectModifications(StartOmissionModification.class, StartOmissionModification::merge)
                .map(omission -> omission.duration().toMillis())
                .map(milliseconds -> {
                    long bytes = (long) (bytesPerMillisecond(format) * milliseconds);
                    LOG.trace("Skip first {} milliseconds (= {} bytes)", milliseconds, bytes);
                    return bytes;
                });
    }

    public static Optional<Long> bytesToPlay(Play play, AudioFormat format) {
        return play.collectModifications(LimitModification.class, LimitModification::merge)
                .map(omission -> omission.duration().toMillis())
                .map(milliseconds -> {
                    long bytes = (long) (bytesPerMillisecond(format) * milliseconds);
                    LOG.trace("play only {} milliseconds (= {} bytes)", milliseconds, bytes);
                    return bytes;
                });
    }

    public static void skipStart(Play play, AudioInputStream inputStream) {
        bytesToSkip(play, inputStream.getFormat()).ifPresent(skipBytes -> {
            try {
                //noinspection ResultOfMethodCallIgnored
                inputStream.skip(skipBytes);
            } catch (IOException e) {
                LOG.error("Unable to seek start position: %d".formatted(skipBytes), e);
            }
        });
    }
}
