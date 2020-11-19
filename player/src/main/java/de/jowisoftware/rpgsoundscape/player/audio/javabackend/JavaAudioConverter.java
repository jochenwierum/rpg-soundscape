package de.jowisoftware.rpgsoundscape.player.audio.javabackend;

import de.jowisoftware.rpgsoundscape.player.audio.AudioConverter;
import org.springframework.util.FileCopyUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaAudioConverter implements AudioConverter {
    public static final byte[] MAGIC_BYTES = {'r', 'a', 'w', 'C', 'A', 'C', 'H', 'E'};

    @Override
    public boolean requiresConversion(Path path) throws IOException, UnsupportedAudioFileException {
        return requiresConversion(AudioSystem.getAudioFileFormat(path.toFile()).getFormat());
    }

    @Override
    public void convert(InputStream is, OutputStream os, float maxSampleRate) {
        try (AudioInputStream converted = open(is, maxSampleRate)) {
            os.write(MAGIC_BYTES);
            writeInt(os, Float.floatToIntBits(converted.getFormat().getSampleRate()));
            writeInt(os, converted.getFormat().getChannels());

            FileCopyUtils.copy(converted, os);
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException("Cannot convert audio file", e);
        }
    }

    private void writeInt(OutputStream os, int value) throws IOException {
        os.write((value & 0xFF000000) >> 24);
        os.write((value & 0xFF0000) >> 16);
        os.write((value & 0xFF00) >> 8);
        os.write((value & 0xFF));
    }

    AudioInputStream open(Path path) throws Exception {
        InputStream is = new BufferedInputStream(Files.newInputStream(path));
        return open(is, 0);
    }

    private AudioInputStream open(InputStream inputStream, float maxSampleRate)
            throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
        if (!requiresConversion(audioInputStream.getFormat())) {
            return audioInputStream;
        }

        AudioFormat targetFormat = decodedFormat(audioInputStream.getFormat(), maxSampleRate);
        return AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
    }

    private boolean requiresConversion(AudioFormat format) {
        return format.getEncoding() != Encoding.PCM_SIGNED
                || format.getSampleSizeInBits() != 16
                || format.isBigEndian();
    }

    private AudioFormat decodedFormat(AudioFormat format, float maxSampleRate) {
        float sampleRate = format.getSampleRate();
        sampleRate = maxSampleRate == 0 ? sampleRate : Math.min(sampleRate, maxSampleRate);

        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                16,
                format.getChannels(),
                format.getChannels() * 2,
                sampleRate,
                false);
    }
}
