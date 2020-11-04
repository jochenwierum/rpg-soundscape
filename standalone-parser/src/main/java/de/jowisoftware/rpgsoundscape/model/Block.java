package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.language.psi.SBlock;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Block(List<Statement> statements) implements Statement {
    static Statement from(SBlock block, Context context) {
        var statements = block.getStatementList().stream()
                .map(s -> Statement.from(s, context))
                .flatMap(s -> {
                    if (s instanceof Block b) {
                        return b.statements().stream();
                    } else {
                        return Stream.of(s);
                    }
                })
                .collect(Collectors.toUnmodifiableList());

        if (statements.size() == 0) {
            return new NoOp();
        } else if (statements.size() == 1) {
            return statements.get(0);
        } else {
            return new Block(statements);
        }
    }

    @Override
    public boolean isValid() {
        return statements().stream().anyMatch(Statement::isValid);
    }

    @Override
    public void collectSamples(Set<Sample> samples) {
        statements.forEach(s -> s.collectSamples(samples));
    }
}
