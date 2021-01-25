package de.jowisoftware.rpgsoundscape.player.audio.backend;

import javax.sound.sampled.AudioFormat;

public interface AudioBackend {
    AudioStream openStream(AudioFormat format) throws Exception;

    AudioFormat deriveTargetFormat(AudioFormat sourceFormat);
}
