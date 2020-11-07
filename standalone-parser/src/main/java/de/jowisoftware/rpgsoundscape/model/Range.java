package de.jowisoftware.rpgsoundscape.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record Range<T extends Comparable<T>>(
        T min,
        Optional<T> max) {

    static <T extends Comparable<T>, S> Range<T> of(List<S> list, Function<S, T> transform) {
        if (list.size() == 1 || (list.size() == 2 && list.get(0).equals(list.get(1)))) {
            return of(transform.apply(list.get(0)), null);
        } else if (list.size() == 2) {
            return of(transform.apply(list.get(0)),
                    transform.apply(list.get(1)));
        } else {
            throw new IllegalArgumentException("List has wrong size");
        }
    }

    static <T extends Comparable<T>> Range<T> of(T min, T max) {
        if (max == null) {
            return new Range<>(min, Optional.empty());
        }

        boolean swap = min.compareTo(max) > 0;
        T realMin = swap ? max : min;
        T realMax = swap ? min : max;
        return new Range<T>(realMin, Optional.of(realMax));
    }
}
