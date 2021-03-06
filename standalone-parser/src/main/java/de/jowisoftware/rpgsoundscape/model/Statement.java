package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludeTrackStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SStatement;

import java.util.Set;

public sealed interface Statement
        permits Play, Randomly, Block, Parallelly, Sleep, Repeat, Pause, Resume, NoOp {

    static Statement from(SStatement statement, Context context) {
        if (statement.getBlock() != null) {
            return Block.from(statement.getBlock(), context);
        } else if (statement.getPlayStatement() != null) {
            return Play.from(statement.getPlayStatement(), context);
        } else if (statement.getSleepStatement() != null) {
            return Sleep.from(statement.getSleepStatement());
        } else if (statement.getPauseStatement() != null) {
            return Pause.from(statement.getPauseStatement(), context);
        } else if (statement.getResumeStatement() != null) {
            return Resume.from(statement.getResumeStatement(), context);
        } else if (statement.getRandomlyStatement() != null) {
            return Randomly.from(statement.getRandomlyStatement(), context);
        } else if (statement.getParallellyStatement() != null) {
            return Parallelly.from(statement.getParallellyStatement(), context);
        } else if (statement.getRepeatStatement() != null) {
            return Repeat.from(statement.getRepeatStatement(), context);
        } else if (statement.getIncludeTrackStatement() != null) {
            return importTrack(statement.getIncludeTrackStatement(), context);
        } else if (statement.getDoNothingStatement() != null) {
            return new NoOp();
        } else {
            throw new IllegalArgumentException("Statement not implemented: " + statement.getText());
        }
    }

    static Statement importTrack(SIncludeTrackStatement statement, Context context) {
        String name = statement.getIncludableTrackRef().getText();
        Statement includedStatement = context.includableTrack(name);

        if (includedStatement == null) {
            throw new SemanticException(statement, "Included track '%s' does not exist".formatted(name));
        }

        return includedStatement;
    }

    default void collectSamples(Set<Sample> samples) {};

    default boolean isValid() {
        return false;
    }
}
