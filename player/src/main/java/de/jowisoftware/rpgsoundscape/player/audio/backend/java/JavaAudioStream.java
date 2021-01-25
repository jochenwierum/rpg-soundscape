package de.jowisoftware.rpgsoundscape.player.audio.backend.java;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioStream;
import de.jowisoftware.rpgsoundscape.player.audio.JavaAudioUtils;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.Latch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.SourceDataLine;
import java.nio.ByteBuffer;

public class JavaAudioStream implements AudioStream {
    private static final Logger LOG = LoggerFactory.getLogger(JavaAudioStream.class);

    private final SourceDataLine line;
    private byte[] bufferBytes = new byte[0];

    private final Latch latch = new Latch(true);
    private volatile boolean quit = false;

    public JavaAudioStream(SourceDataLine line) {
        this.line = line;
        line.start();
    }

    @Override
    public void close(boolean drain) {
        quit = true;
        latch.open();
        if (drain) {
            line.drain();
        }
        line.close();
    }

    @Override
    public void pause() {
        line.stop();
        latch.close();
    }

    @Override
    public void resume() {
        latch.open();
        line.start();
    }

    @Override
    public boolean isOpen() {
        return line.isOpen();
    }

    @Override
    public void applyAmplification(Play play) {
        JavaAudioUtils.calculateAmplification(play)
                .ifPresent(volume -> {
                    FloatControl gainControl = (FloatControl) line.getControl(Type.MASTER_GAIN);
                    volume = Math.min(Math.max(gainControl.getMinimum(), volume), gainControl.getMaximum());

                    LOG.trace("Modifying volume to {} dB", volume);
                    gainControl.setValue((float) volume);
                });
    }

    @Override
    public void write(ByteBuffer bb) {
        int length = bb.limit();

        if (bufferBytes.length < length) {
            bufferBytes = new byte[length];
        }

        int written;
        bb.get(bufferBytes, 0, length);

        written = line.write(bufferBytes, 0, length);
        while ((written < length && !quit && isOpen()) || latch.isClosed()) {
            latch.waitForOpen(); // prevent 100% cpu usage
            if (written < length) {
                written += line.write(bufferBytes, written, length - written);
            }
        }
    }

    @Override
    public int getBufferSize() {
        return line.getBufferSize();
    }
}
