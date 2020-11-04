package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.SPauseStatement;

public record Pause(
        PauseMode pauseMode,
        String track) implements Statement {
    public enum PauseMode {
        SPECIFIC,
        ALL,
        THIS,
        OTHER
    }

    public static Pause from(SPauseStatement statement, Context context) {
        PauseMode pauseMode;
        String track = null;

        if (statement.getPauseAllTracks() != null) {
            pauseMode = PauseMode.ALL;
        } else if (statement.getPauseAllOtherTracks() != null) {
            pauseMode = PauseMode.OTHER;
        } else if (statement.getPauseThisTrack() != null) {
            pauseMode = PauseMode.THIS;
        } else if (statement.getTrackRef() != null) {
            pauseMode = PauseMode.SPECIFIC;
            track = statement.getTrackRef().getText();

            if (!context.knowsTrack(track)) {
                throw new SemanticException(statement, "Track does not exist: " + track);
            }
        } else {
            throw new IllegalArgumentException("Unknown pause mode: " + statement);
        }

        return new Pause(pauseMode, track);
    }
}
