package de.jowisoftware.rpgsoundscape.player.audio;

import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;

import javax.sound.sampled.AudioInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public interface AudioConverter {
    boolean requiresConversion(Path path) throws Exception;

    void convert(InputStream is, OutputStream os, float maxSampleRate) throws Exception;

    AudioInputStream openForPlaying(LookupResult resolvedSample) throws Exception;

    final class NullAudioConverter implements AudioConverter {
        @Override
        public boolean requiresConversion(Path path) {
            return false;
        }

        @Override
        public void convert(InputStream is, OutputStream os, float maxSampleRate) {
            throw new UnsupportedOperationException();
        }

        @Override
        public AudioInputStream openForPlaying(LookupResult resolvedSample) {
            throw new UnsupportedOperationException();
        }
    }
}
