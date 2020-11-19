package de.jowisoftware.rpgsoundscape.player.audio.javabackend;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public class CacheFileAudioFileReader extends AudioFileReader {
    private static final Type AUDIO_FILE_TYPE = new Type("CacheAudioFile", ".converted");
    private static final int HEADER_SIZE = Integer.SIZE / 8 * 2 + JavaAudioConverter.MAGIC_BYTES.length;

    @Override
    public AudioFileFormat getAudioFileFormat(InputStream stream) throws UnsupportedAudioFileException, IOException {
        stream.mark(HEADER_SIZE);
        try {
            return readAudioFileFormat(stream);
        } finally {
            stream.reset();
        }
    }

    @Override
    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        try (InputStream stream = url.openStream()) {
            return readAudioFileFormat(stream);
        }
    }

    @Override
    public AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException {
        try (InputStream stream = new FileInputStream(file)) {
            return readAudioFileFormat(stream);
        }
    }

    @Override
    public AudioInputStream getAudioInputStream(InputStream stream) throws UnsupportedAudioFileException, IOException {
        stream.mark(HEADER_SIZE);
        try {
            return open(stream);
        } catch (UnsupportedAudioFileException e) {
            stream.reset();
            throw e;
        }
    }

    @Override
    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        return open(new BufferedInputStream(url.openStream()));
    }

    @Override
    public AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        return open(new BufferedInputStream(new FileInputStream(file)));
    }

    private AudioFileFormat readAudioFileFormat(InputStream inputStream) throws IOException, UnsupportedAudioFileException {
        return new AudioFileFormat(AUDIO_FILE_TYPE, readAudioFormat(inputStream), AudioSystem.NOT_SPECIFIED);
    }

    private AudioFormat readAudioFormat(InputStream inputStream) throws IOException, UnsupportedAudioFileException {
        byte[] magic = new byte[JavaAudioConverter.MAGIC_BYTES.length];

        if (inputStream.read(magic) != magic.length) {
            throw new UnsupportedAudioFileException();
        }

        if (!Arrays.equals(magic, JavaAudioConverter.MAGIC_BYTES)) {
            throw new UnsupportedAudioFileException();
        }

        float sampleRate = Float.intBitsToFloat(readInt(inputStream));
        int channels = readInt(inputStream);

        return new AudioFormat(Encoding.PCM_SIGNED,
                sampleRate, 16, channels, channels * 2, sampleRate, false);
    }

    private int readInt(InputStream is) throws IOException {
        return (is.read() << 24 | is.read() << 16 | is.read() << 8 | is.read());
    }

    private AudioInputStream open(InputStream stream) throws IOException, UnsupportedAudioFileException {
        AudioFileFormat format = readAudioFileFormat(stream);
        return new AudioInputStream(stream, format.getFormat(), format.getFrameLength());
    }
}
