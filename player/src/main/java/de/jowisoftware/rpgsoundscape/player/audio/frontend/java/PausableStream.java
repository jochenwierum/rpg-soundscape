package de.jowisoftware.rpgsoundscape.player.audio.frontend.java;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioStream;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleStatus;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.InterruptibleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static de.jowisoftware.rpgsoundscape.player.audio.JavaAudioUtils.bytesToPlay;
import static de.jowisoftware.rpgsoundscape.player.audio.JavaAudioUtils.skipStart;

class PausableStream implements InterruptibleTask {
    private static final Logger LOG = LoggerFactory.getLogger(PausableStream.class);

    private final Play play;
    private final AudioStream audioStream;
    private final AudioInputStream inputStream;
    private final LookupResult lookupResult;

    public PausableStream(LookupResult lookupResult, Play play, AudioStream audioStream, AudioInputStream is) {
        this.play = play;
        this.audioStream = audioStream;
        this.inputStream = is;
        this.lookupResult = lookupResult;
    }

    @Override
    public void pause() {
        audioStream.pause();
    }

    @Override
    public void startOrResume() {
        audioStream.resume();
    }

    @Override
    public void abort() {
        try {
            audioStream.close();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void run(boolean resume) {
        long bytesToPlay = Long.MAX_VALUE;
        if (lookupResult.sampleStatus() != SampleStatus.ERROR) {
            skipStart(play, inputStream);
            bytesToPlay = bytesToPlay(play, inputStream.getFormat()).orElse(bytesToPlay);
        }

        audioStream.applyAmplification(play);

        try {
            byte[] buffer = new byte[audioStream.getBufferSize()];

            if (resume) {
                startOrResume();
            }

            int read = inputStream.read(buffer);
            while (read != -1
                    && bytesToPlay > 0
                    && audioStream.isOpen()
            ) {
                audioStream.write(ByteBuffer.wrap(buffer, 0, read));
                bytesToPlay -= read;
                read = inputStream.read(buffer);
            }

            LOG.trace("Input finished or line closed - draining and closing track");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        abort();
    }
}
