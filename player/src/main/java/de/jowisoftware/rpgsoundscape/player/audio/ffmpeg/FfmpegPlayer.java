package de.jowisoftware.rpgsoundscape.player.audio.ffmpeg;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.AudioPlayer;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaAudioUtils.bytesToPlay;
import static de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaAudioUtils.bytesToSkip;

public class FfmpegPlayer extends AudioPlayer {
    private static final Logger LOG = LoggerFactory.getLogger(FfmpegPlayer.class);

    public FfmpegPlayer(SampleRepository sampleRepository, ApplicationSettings applicationSettings) {
        super(sampleRepository, applicationSettings);
    }

    @Override
    protected void play(LookupResult resolvedSample, BlockExecutionContext context, Play play) {
        String file = resolvedSample.file().toAbsolutePath().normalize().toString();
        LOG.trace("Starting to play {}", play);

        long skipBytes = bytesToSkip(play, FfmpegClip.TARGET_FORMAT).orElse(0L);
        long playBytes = bytesToPlay(play, FfmpegClip.TARGET_FORMAT).orElse(-1L);

        context.runInterruptible(new FfmpegClip(play, file, skipBytes, playBytes));
        LOG.trace("Completed play of {}", play);
    }
}
