package de.jowisoftware.rpgsoundscape.player.status;

import de.jowisoftware.rpgsoundscape.player.status.event.MusicChangedEvent;
import de.jowisoftware.rpgsoundscape.player.status.event.SoundscapeChangeEvent;
import de.jowisoftware.rpgsoundscape.player.status.event.UpdateLibraryEvent;

public interface ApplicationStatusListener {
    void ping();

    void reportProblemCount(int count);

    void reportSoundscapeChanged(SoundscapeChangeEvent e);

    void reportMusicChanged(MusicChangedEvent e);

    void updateLibrary(UpdateLibraryEvent e);
}
