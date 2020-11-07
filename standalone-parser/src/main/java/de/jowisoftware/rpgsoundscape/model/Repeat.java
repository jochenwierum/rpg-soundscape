package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.language.psi.SInt;
import de.jowisoftware.rpgsoundscape.language.psi.SRepeatStatement;

import java.util.Set;

public record Repeat(
        Statement statement,
        Range<Long> range) implements Statement {

    static Statement from(SRepeatStatement statement, Context context) {
        Statement body = Statement.from(statement.getStatement(), context);
        Range<Long> range = Range.of(statement.getIntList(), SInt::parsed);

        if (range.min() == 0 && range.max().isEmpty()) {
            return new NoOp();
        } else if (range.min() == 1 && range.max().isEmpty()) {
            return body;
        } else {
            return new Repeat(body, range);
        }
    }

    @Override
    public void collectSamples(Set<Sample> samples) {
        statement.collectSamples(samples);
    }

    @Override
    public boolean isValid() {
        return range.min() > 0 && statement.isValid();
    }
}
