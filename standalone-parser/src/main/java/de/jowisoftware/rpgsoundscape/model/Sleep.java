package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.intellij.psi.SSleepStatement;

import java.time.Duration;

public record Sleep(Range<Duration> range) implements Statement {
    static Statement from(SSleepStatement sleepStatement) {
        return new Sleep(Range.of(sleepStatement.getTimespanList(), Util::parse));
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
