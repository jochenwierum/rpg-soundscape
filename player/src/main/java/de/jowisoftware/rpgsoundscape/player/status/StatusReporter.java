package de.jowisoftware.rpgsoundscape.player.status;

import de.jowisoftware.rpgsoundscape.player.status.event.MusicChangedEvent;
import de.jowisoftware.rpgsoundscape.player.status.event.Problem;
import de.jowisoftware.rpgsoundscape.player.status.event.ResolvedStatus;
import de.jowisoftware.rpgsoundscape.player.status.event.SoundscapeChangeEvent;
import de.jowisoftware.rpgsoundscape.player.status.event.UpdateLibraryEvent;

public interface StatusReporter {

    long logProblem(Problem problem);

    void removeProblem(long problemId);

    void clearProblems();

    void reportSoundscapeChanged(SoundscapeChangeEvent event);

    void reportMusicChanged(MusicChangedEvent event);

    void updateLibrary(UpdateLibraryEvent event);

    void reportResolved(ResolvedStatus event);

}
