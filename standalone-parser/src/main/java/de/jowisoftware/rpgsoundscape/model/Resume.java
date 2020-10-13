package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.intellij.psi.SResumeStatement;

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

        if (statement.getResumeTrackStatement() != null) {
            resumeMode = ResumeMode.SPECIFIC;
            track = statement.getResumeTrackStatement().getId().getText();

            if (!context.knowsTrack(track)) {
                throw new SemanticException(statement, "Track does not exist: " + track);
            }
        }

        return new Resume(resumeMode, track);
    }
}
