package de.jowisoftware.rpgsoundscape.player.audio.javabackend;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleStatus;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.InterruptibleTask;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.Pause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;

import static de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaAudioUtils.bytesToPlay;
import static de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaAudioUtils.modifyAmplification;
import static de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaAudioUtils.skipStart;

class PausableLine implements InterruptibleTask {
    private static final Logger LOG = LoggerFactory.getLogger(PausableLine.class);

    private final Play play;
    private final SourceDataLine line;
    private final AudioInputStream inputStream;
    private final LookupResult lookupResult;
    private final Pause pause = new Pause(false);

    public PausableLine(LookupResult lookupResult, Play play, SourceDataLine line, AudioInputStream is) {
        this.play = play;
        this.line = line;
        this.inputStream = is;
        this.lookupResult = lookupResult;
    }

    @Override
    public void pause() {
        pause.pause();
        line.stop();
    }

    @Override
    public void startOrResume() {
        line.start();
        pause.resume();
    }

    @Override
    public void abort() {
        line.stop();
        line.close();
        pause.resume();
    }

    @Override
    public void run(boolean resume) {
        long bytesToPlay = Long.MAX_VALUE;
        if (lookupResult.sampleStatus() != SampleStatus.ERROR) {
            skipStart(play, inputStream);
            bytesToPlay = bytesToPlay(play, inputStream.getFormat()).orElse(bytesToPlay);
        }

        modifyAmplification(play, line);

        try {
            byte[] buffer = new byte[line.getBufferSize()];

            if (resume) {
                startOrResume();
            }

            int read = inputStream.read(buffer);
            while (read != -1
                    && bytesToPlay > 0
                    && line.isOpen()
            ) {
                int written = line.write(buffer, 0, read);
                while (written < read && line.isOpen()) {
                    pause.awaitToPass(); // prevent 100% cpu usage
                    written += line.write(buffer, written, read - written);
                }

                bytesToPlay -= read;
                read = inputStream.read(buffer);
            }

            LOG.trace("Input finished or line closed - draining and closing track");
            line.drain();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        abort();
    }
}
