package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.SResumeStatement;

public record Resume(
        ResumeMode resumeMode,
        String track) implements Statement {
    public enum ResumeMode {
        SPECIFIC,
        LOOPING
    }

    static Resume from(SResumeStatement statement, Context context) {
        String track = null;
        ResumeMode resumeMode = ResumeMode.LOOPING;

        if (statement.getTrackRef() != null) {
            resumeMode = ResumeMode.SPECIFIC;
            track = statement.getTrackRef().getText();

            if (!context.knowsTrack(track)) {
                throw new SemanticException(statement, "Track does not exist: " + track);
            }
        }

        return new Resume(resumeMode, track);
    }
}
