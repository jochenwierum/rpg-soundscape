package de.jowisoftware.rpgsoundscape.player.audio;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.threading.TrackExecutionContext;

public interface AudioPlayer {
    void play(TrackExecutionContext context, Play play);
}
