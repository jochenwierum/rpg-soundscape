package de.jowisoftware.rpgsoundscape.player.audio.backend.discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static de.jowisoftware.rpgsoundscape.player.audio.backend.discord.DiscordBackend.BUFFER_LENGTH;
import static de.jowisoftware.rpgsoundscape.player.audio.backend.discord.DiscordBackend.CHUNK_SIZE;

@Component
@ConditionalOnProperty(value = "application.audio.backend", havingValue = "DISCORD")
public class DiscordMixer implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordMixer.class);

    private final DiscordBackend discordBackend;
    private final BlockingBuffer<ByteBuffer> outQueue;
    private volatile boolean quit = false;
    private volatile boolean burnInput = true;

    private final float[] buffer = new float[BUFFER_LENGTH];

    public DiscordMixer(DiscordBackend discordBackend) {
        this.discordBackend = discordBackend;
        this.outQueue = new BlockingBuffer<>(2, () -> ByteBuffer.allocate(CHUNK_SIZE));

        var t = new Thread(this::run, "DiscordMixer");
        t.start();
    }

    private void run() {
        while (!quit) {
            try {
                processQueues();
            } catch (Exception e) {
                LOG.warn("Unhandled exception in discord mixer thread", e);
            }
        }

        // Burn data to not block any producers
        while (!discordBackend.getStreams().isEmpty()) {
            discordBackend.getStreams().forEach(BlockingBuffer::nextRead);
        }
    }

    private void processQueues() throws InterruptedException {
        boolean found = false;

        for (BlockingBuffer<float[]> inQueue : discordBackend.getStreams()) {
            float[] in = inQueue.nextRead();
            if (in == null) {
                continue;
            }

            found = true;

            for (int i = 0; i < buffer.length; i++) {
                buffer[i] += in[i];
            }
        }

        if (!found) {
            Thread.sleep(15);
        } else if (!burnInput) {
            ByteBuffer out = outQueue.nextWrite();
            out.rewind();
            for (float v : buffer) {
                out.put((byte) ((int) v >> 8));
                out.put((byte) v);
            }
            out.flip();
        }

        Arrays.fill(buffer, 0);
    }

    @Override
    public void destroy() throws Exception {
        quit = true;
    }

    public void play() {
        burnInput = false;
    }

    public void pause() {
        burnInput = true;

        // make sure that there is no race condition that blocks the last invocation of outQueue.nextWrite()
        // because nobody is reading...
        outQueue.clear();
    }

    public BlockingBuffer<ByteBuffer> getOutQueue() {
        return outQueue;
    }
}
