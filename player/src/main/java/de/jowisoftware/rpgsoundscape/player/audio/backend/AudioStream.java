package de.jowisoftware.rpgsoundscape.player.audio.backend;

import de.jowisoftware.rpgsoundscape.model.Play;

import java.nio.ByteBuffer;

public interface AudioStream extends AutoCloseable {
    void resume();

    void pause();

    boolean isOpen();

    void applyAmplification(Play play);

    void write(ByteBuffer bb);

    int getBufferSize();

    void close(boolean drain);

    @Override
    default void close() {
        close(true);
    }
}
