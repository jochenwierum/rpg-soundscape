package de.jowisoftware.rpgsoundscape.player.exception;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class ExceptionUtils {
    private ExceptionUtils() {}

    public static <T> Optional<T> firstCauseOfType(Exception e, Class<T> clazz) {
        return streamCauses(e)
                .filter(clazz::isInstance)
                .findFirst()
                .map(clazz::cast);
    }

    public static Stream<Throwable> streamCauses(Throwable e) {
        return Stream.iterate(e, Objects::nonNull, Throwable::getCause);
    }
}
