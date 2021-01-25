package de.jowisoftware.rpgsoundscape.player.audio.frontend.java;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioBackend;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioStream;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;

public class StreamingAudioFrontend extends AbstractJavaAudioFrontend {
    private static final Logger LOG = LoggerFactory.getLogger(StreamingAudioFrontend.class);

    public StreamingAudioFrontend(SampleRepository sampleRepository, AudioBackend audioBackend) {
        super(sampleRepository, audioBackend);
    }

    @Override
    protected void playStream(AudioInputStream is, BlockExecutionContext context, Play play, LookupResult resolvedSample)
            throws Exception {
        try (AudioStream audioStream = audioBackend.openStream(is.getFormat())) {
            LOG.trace("Starting to play {}", play);
            context.runInterruptible(new PausableStream(resolvedSample, play, audioStream, is));
            LOG.trace("Completed play of {}", play);
        }
    }
}
