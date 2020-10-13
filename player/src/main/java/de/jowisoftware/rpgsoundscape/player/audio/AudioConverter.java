package de.jowisoftware.rpgsoundscape.player.audio;

import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import org.springframework.stereotype.Component;
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

@Component
public class AudioConverter {
    public boolean requiresConversion(Path path) throws IOException, UnsupportedAudioFileException {
        try (AudioInputStream is = AudioSystem.getAudioInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
            return requiresConversion(is.getFormat());
        }
    }

    public AudioInputStream openForPlaying(LookupResult lookupResult) throws IOException, UnsupportedAudioFileException {
        return lookupResult.isPreconverted()
                ? openPreConverted(lookupResult.open())
                : openAndConvert(lookupResult.open(), 0);
    }

    private boolean requiresConversion(AudioFormat format) {
        return format.getEncoding() != Encoding.PCM_SIGNED
                || format.getSampleSizeInBits() != 16
                || format.isBigEndian();
    }

    public void convert(InputStream is, OutputStream os, float maxSampleRate) {
        try (AudioInputStream converted = openAndConvert(is, maxSampleRate)) {
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

    private int readInt(InputStream is) throws IOException {
        return (is.read() << 24 | is.read() << 16 | is.read() << 8 | is.read());
    }

    private AudioInputStream openPreConverted(InputStream inputStream) throws IOException {
        float sampleRate = Float.intBitsToFloat(readInt(inputStream));
        int channels = readInt(inputStream);
        AudioFormat format = new AudioFormat(Encoding.PCM_SIGNED,
                sampleRate, 16, channels, channels * 2, sampleRate, false);

        return new AudioInputStream(inputStream, format, -1);
    }

    private AudioInputStream openAndConvert(InputStream inputStream, float maxSampleRate)
            throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
        if (requiresConversion(audioInputStream.getFormat())) {
            AudioFormat targetFormat = decodedFormat(audioInputStream.getFormat(), maxSampleRate);
            return AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
        } else {
            return audioInputStream;
        }
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
