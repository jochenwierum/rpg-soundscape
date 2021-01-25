package de.jowisoftware.rpgsoundscape.player.audio;

import de.jowisoftware.rpgsoundscape.model.Modification.AmplificationModification;
import de.jowisoftware.rpgsoundscape.model.Modification.LimitModification;
import de.jowisoftware.rpgsoundscape.model.Modification.StartOmissionModification;
import de.jowisoftware.rpgsoundscape.model.Play;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.OptionalDouble;

public final class JavaAudioUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JavaAudioUtils.class);

    private JavaAudioUtils() {
    }

    public static OptionalDouble calculateAmplification(Play play) {
        return play.collectModifications(AmplificationModification.class, AmplificationModification::merge)
                .stream()
                .mapToDouble(amplification -> 20f * (float) Math.log10(1 + amplification.percentage().toDouble()))
                .findFirst();
    }

    public static long bytesPerMilliseconds(AudioFormat format, long millis) {
        return (long) (millis * format.getFrameSize() * format.getFrameRate() / 1000);
    }

    public static int bytesPerSample(AudioFormat format) {
        return format.getChannels() * format.getSampleSizeInBits() / 8;
    }

    public static Optional<Long> bytesToSkip(Play play, AudioFormat format) {
        return play.collectModifications(StartOmissionModification.class, StartOmissionModification::merge)
                .map(omission -> omission.duration().toMillis())
                .map(milliseconds -> {
                    long bytes = bytesPerMilliseconds(format, milliseconds);
                    LOG.trace("Skip first {} milliseconds (= {} bytes)", milliseconds, bytes);
                    return bytes;
                });
    }

    public static Optional<Long> bytesToPlay(Play play, AudioFormat format) {
        return play.collectModifications(LimitModification.class, LimitModification::merge)
                .map(omission -> omission.duration().toMillis())
                .map(milliseconds -> {
                    long bytes = bytesPerMilliseconds(format, milliseconds);
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
