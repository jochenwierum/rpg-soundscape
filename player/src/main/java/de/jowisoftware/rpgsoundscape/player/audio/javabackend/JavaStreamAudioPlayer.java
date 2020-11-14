package de.jowisoftware.rpgsoundscape.player.audio.javabackend;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.AudioConverter;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.threading.TrackExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class JavaStreamAudioPlayer extends AbstractJavaAudioPlayer {
    private static final Logger LOG = LoggerFactory.getLogger(JavaStreamAudioPlayer.class);

    public JavaStreamAudioPlayer(SampleRepository sampleRepository, AudioConverter audioConverter, ApplicationSettings applicationSettings) {
        super(sampleRepository, audioConverter, applicationSettings);
    }

    @Override
    protected void playStream(AudioInputStream is, TrackExecutionContext context, Play play, LookupResult resolvedSample)
            throws Exception {
        SourceDataLine line = createLine(is.getFormat());
        try {
            line.open(is.getFormat());
            LOG.trace("Starting to play {}", play);
            context.runInterruptible(new PausableLine(resolvedSample, play, line, is));
            LOG.trace("Completed play of {}", play);
        } catch (Exception e) {
            line.close();
            throw e;
        }
    }

    private SourceDataLine createLine(AudioFormat format) throws LineUnavailableException {
        if (mixer == null) {
            return (SourceDataLine) AudioSystem.getLine(new Info(SourceDataLine.class, format));
        } else {
            return (SourceDataLine) mixer.getLine(new Info(SourceDataLine.class, format));
        }
    }
}
