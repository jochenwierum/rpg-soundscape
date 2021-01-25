package de.jowisoftware.rpgsoundscape.player.audio.backend.discord;

import de.jowisoftware.rpgsoundscape.model.Modification.AmplificationModification;
import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioStream;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.Latch;

import java.nio.ByteBuffer;

import static de.jowisoftware.rpgsoundscape.player.audio.backend.discord.DiscordBackend.BUFFER_LENGTH;
import static de.jowisoftware.rpgsoundscape.player.audio.backend.discord.DiscordBackend.CHUNK_SIZE;

public class DiscordAudioStream implements AudioStream {
    private final Latch latch = new Latch(false);
    private final DiscordBackend backend;
    private final BlockingBuffer<float[]> queue;

    private float amplification;
    private volatile boolean closed;

    private float[] write;
    private int written;

    public DiscordAudioStream(DiscordBackend backend, BlockingBuffer<float[]> queue) {
        this.backend = backend;
        this.queue = queue;

        written = 0;
        write = queue.nextWrite();
    }

    @Override
    public void close(boolean drain) {
        if (!drain) {
            backend.closeStream(queue);
        }

        closed = true;
        latch.open();

        if (drain) {
            try {
                queue.waitUntilConsumed();
            } catch (InterruptedException ignored) {
            }
            backend.closeStream(queue);
        }
    }

    @Override
    public void resume() {
        latch.open();
    }

    @Override
    public void pause() {
        latch.close();
    }

    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    public void write(ByteBuffer bb) {
        while (bb.hasRemaining() && !closed) {
            latch.waitForOpen();
            if (closed) {
                return;
            }

            int maxRead = Math.min(bb.remaining() / 2, BUFFER_LENGTH - written);
            for (int i = 0; i < maxRead; ++i) {
                write[written + i] = ((bb.get() & 0xff | bb.get() << 8) * amplification);
            }
            written += maxRead;

            if (written == BUFFER_LENGTH) {
                written = 0;
                //System.out.println("w");
                write = queue.nextWrite();
                //System.out.println("W");
            }
        }
    }

    @Override
    public void applyAmplification(Play play) {
        this.amplification = play.collectModifications(AmplificationModification.class, AmplificationModification::merge)
                .map(amplification -> 1.0 + amplification.percentage().toDouble())
                .orElse(1.0)
                .floatValue();
    }

    @Override
    public int getBufferSize() {
        return CHUNK_SIZE;
    }
}
