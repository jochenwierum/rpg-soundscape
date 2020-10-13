package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.intellij.psi.SRepeatStatement;

import java.util.Set;

public record Repeat(
        Statement statement,
        Range<Long> range) implements Statement {

    static Repeat from(SRepeatStatement statement, Context context) {
        Statement body = Statement.from(statement.getStatement(), context);
        return new Repeat(body, Range.of(statement.getIntList(), Util::parse));
    }

    @Override
    public void collectSamples(Set<Sample> samples) {
        statement.collectSamples(samples);
    }

    @Override
    public boolean isValid() {
        return statement.isValid();
    }
}
