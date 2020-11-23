package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.exceptions.ErrorPosition;
import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.SPlayStatement;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public record Play(
        Sample sample,
        List<Modification> modifications,
        ErrorPosition position) implements Statement {
    static Statement from(SPlayStatement statement, Context context) {
        String name = statement.getSampleRef().getText();

        Sample sample = context.sample(name);
        if (sample == null) {
            throw new SemanticException(statement, "Sample '%s' is referenced but does not exist".formatted(name));
        }

        List<Modification> modifications = Modification.from(statement.getSampleModificationList());

        return new Play(sample, modifications, new ErrorPosition(statement));
    }

    @Override
    public void collectSamples(Set<Sample> sources) {
        sources.add(sample());
    }

    public <T extends Modification> Optional<T> collectModifications(Class<T> type, BinaryOperator<T> merger) {
        return Stream.concat(modifications.stream(), sample.modifications().stream())
                .flatMap(Util.filterCast(type))
                .reduce(merger);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
