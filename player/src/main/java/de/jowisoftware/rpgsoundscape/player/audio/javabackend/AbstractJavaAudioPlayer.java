package de.jowisoftware.rpgsoundscape.player.audio.javabackend;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.AudioPlayer;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;

import javax.sound.sampled.AudioInputStream;

public abstract class AbstractJavaAudioPlayer extends AudioPlayer {
    private final JavaAudioConverter audioConverter = new JavaAudioConverter();

    public AbstractJavaAudioPlayer(SampleRepository sampleRepository, ApplicationSettings applicationSettings) {
        super(sampleRepository, applicationSettings);
    }

    @Override
    protected void play(LookupResult resolvedSample, BlockExecutionContext context, Play play) throws Exception {
        try (AudioInputStream is = audioConverter.open(resolvedSample.file())) {
            playStream(is, context, play, resolvedSample);
        }
    }

    protected abstract void playStream(AudioInputStream is, BlockExecutionContext context, Play play, LookupResult resolvedSample)
            throws Exception;

}
