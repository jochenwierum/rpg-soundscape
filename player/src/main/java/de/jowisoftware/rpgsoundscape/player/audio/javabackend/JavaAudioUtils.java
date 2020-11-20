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
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.Line;
import java.io.IOException;
import java.util.Optional;

public final class JavaAudioUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JavaAudioUtils.class);

    private JavaAudioUtils() {
    }

    public static void modifyAmplification(Play play, Line line) {
        play.collectModifications(AmplificationModification.class, AmplificationModification::merge)
                .ifPresent(amplification -> {
                    float volume = 20f * (float) Math.log10(1 + amplification.percentage().toDouble());

                    FloatControl gainControl = (FloatControl) line.getControl(Type.MASTER_GAIN);
                    volume = Math.min(Math.max(gainControl.getMinimum(), volume), gainControl.getMaximum());

                    LOG.trace("Modifying volume to {} dB", volume);
                    gainControl.setValue(volume);
                });
    }

    public static int bytesPerMillisecond(AudioFormat format) {
        return (int) (format.getFrameSize() * format.getFrameRate() / 1000);
    }

    public static Optional<Long> bytesToSkip(Play play, AudioFormat format) {
        return play.collectModifications(StartOmissionModification.class, StartOmissionModification::merge)
                .map(omission -> omission.duration().toMillis())
                .map(milliseconds -> {
                    long bytes = bytesPerMillisecond(format) * milliseconds;
                    LOG.trace("Skip first {} milliseconds (= {} bytes)", milliseconds, bytes);
                    return bytes;
                });
    }

    public static Optional<Long> bytesToPlay(Play play, AudioFormat format) {
        return play.collectModifications(LimitModification.class, LimitModification::merge)
                .map(omission -> omission.duration().toMillis())
                .map(milliseconds -> {
                    long bytes = bytesPerMillisecond(format) * milliseconds;
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
