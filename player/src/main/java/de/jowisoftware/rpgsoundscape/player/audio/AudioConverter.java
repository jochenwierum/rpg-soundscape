package de.jowisoftware.rpgsoundscape.player.audio;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public interface AudioConverter {
    boolean requiresConversion(Path path) throws Exception;

    void convert(InputStream is, OutputStream os, float maxSampleRate) throws Exception;

    AudioConverter NULL_CONVERTER = new AudioConverter() {
        @Override
        public boolean requiresConversion(Path path) {
            return false;
        }

        @Override
        public void convert(InputStream is, OutputStream os, float maxSampleRate) {
            throw new UnsupportedOperationException();
        }
    };
}
