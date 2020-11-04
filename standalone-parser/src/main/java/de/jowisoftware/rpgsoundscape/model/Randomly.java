package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.language.psi.SRandomlyStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SRandomlyWeight;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record Randomly(List<RandomChoice> choices) implements Statement {
    public static record RandomChoice(
            long weight,
            Statement statement) {
    }

    public long summedWeights() {
        return choices.stream().mapToLong(RandomChoice::weight).sum();
    }

    static Statement from(SRandomlyStatement statement, Context context) {
        List<RandomChoice> choices = statement.getRandomlyWeightList().stream()
                .map(rw -> from(rw, context))
                .collect(Collectors.toUnmodifiableList());

        return new Randomly(choices);
    }

    private static RandomChoice from(SRandomlyWeight randomlyWeight, Context context) {
        Statement statement = Statement.from(randomlyWeight.getStatement(), context);
        long weight = randomlyWeight.getInt().parsed();
        return new RandomChoice(weight, statement);
    }

    @Override
    public void collectSamples(Set<Sample> samples) {
        choices.forEach(choice -> choice.statement().collectSamples(samples));
    }

    @Override
    public boolean isValid() {
        return choices.stream().allMatch(choice -> choice.statement.isValid());
    }
}
