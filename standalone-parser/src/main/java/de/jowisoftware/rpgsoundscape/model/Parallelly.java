package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.intellij.psi.SParallellyStatement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Parallelly(List<Statement> statements) implements Statement {
    static Statement from(SParallellyStatement statement, Context context) {
        List<Statement> statements = statement.getStatementList().stream()
                .map(s -> Statement.from(s, context))
                .flatMap(s -> {
                    if (s instanceof Parallelly p) {
                        return p.statements().stream();
                    } else {
                        return Stream.of(s);
                    }
                })
                .collect(Collectors.toUnmodifiableList());

        return new Parallelly(statements);
    }

    @Override
    public void collectSamples(Set<Sample> samples) {
        statements.forEach(s -> s.collectSamples(samples));
    }

    @Override
    public boolean isValid() {
        return statements.stream().anyMatch(Statement::isValid);
    }
}
