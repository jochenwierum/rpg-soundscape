package de.jowisoftware.rpgsoundscape.player.audio.frontend.java;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioBackend;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.AudioFrontend;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;

import javax.sound.sampled.AudioInputStream;

public abstract class AbstractJavaAudioFrontend extends AudioFrontend {
    private final JavaAudioConverter audioConverter;

    public AbstractJavaAudioFrontend(SampleRepository sampleRepository, AudioBackend audioBackend) {
        super(sampleRepository, audioBackend);
        audioConverter = new JavaAudioConverter(audioBackend);
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
