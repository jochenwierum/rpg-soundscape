package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioBackend;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioStream;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.AudioFrontend;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;

import static de.jowisoftware.rpgsoundscape.player.audio.JavaAudioUtils.bytesToPlay;
import static de.jowisoftware.rpgsoundscape.player.audio.JavaAudioUtils.bytesToSkip;

public class FfmpegFrontend extends AudioFrontend {
    private static final Logger LOG = LoggerFactory.getLogger(FfmpegFrontend.class);

    public FfmpegFrontend(SampleRepository sampleRepository, AudioBackend audioBackend) {
        super(sampleRepository, audioBackend);
    }

    @Override
    protected void play(LookupResult resolvedSample, BlockExecutionContext context, Play play) {
        String file = resolvedSample.file().toAbsolutePath().normalize().toString();
        LOG.trace("Starting to play {}", play);

        AudioFormat format = audioBackend.deriveTargetFormat(null);
        try (AudioStream stream = audioBackend.openStream(format)) {
            long skipBytes = bytesToSkip(play, format).orElse(0L);
            long playBytes = bytesToPlay(play, format).orElse(-1L);

            context.runInterruptible(new FfmpegClip(play, file, skipBytes, playBytes, stream, format));
            LOG.trace("Completed play of {}", play);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
