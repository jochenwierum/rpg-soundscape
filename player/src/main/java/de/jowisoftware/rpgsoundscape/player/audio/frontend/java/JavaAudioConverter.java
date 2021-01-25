package de.jowisoftware.rpgsoundscape.player.audio.frontend.java;

import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioBackend;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.AudioConverter;
import org.springframework.util.FileCopyUtils;

import javax.sound.sampled.AudioFormat;
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
    private final AudioBackend audioBackend;

    public JavaAudioConverter(AudioBackend audioBackend) {
        this.audioBackend = audioBackend;
    }

    @Override
    public boolean requiresConversion(Path path) throws IOException, UnsupportedAudioFileException {
        AudioFormat format = AudioSystem.getAudioFileFormat(path.toFile()).getFormat();
        return !audioBackend.deriveTargetFormat(format).matches(format);
    }

    @Override
    public void convert(InputStream is, OutputStream os) {
        try (AudioInputStream converted = open(is)) {
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
        return open(new BufferedInputStream(Files.newInputStream(path)));
    }

    private AudioInputStream open(InputStream inputStream)
            throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);

        AudioFormat targetFormat = audioBackend.deriveTargetFormat(audioInputStream.getFormat());
        if (targetFormat.matches(audioInputStream.getFormat())) {
            return audioInputStream;
        }

        return AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
    }
}
