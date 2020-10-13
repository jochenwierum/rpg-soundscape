package de.jowisoftware.rpgsoundscape.player.status.event;

import de.jowisoftware.rpgsoundscape.exceptions.ErrorPosition;
import de.jowisoftware.rpgsoundscape.exceptions.HasErrorPosition;
import de.jowisoftware.rpgsoundscape.player.exception.HasLink;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static de.jowisoftware.rpgsoundscape.player.exception.ExceptionUtils.streamCauses;
import static de.jowisoftware.rpgsoundscape.player.exception.ExceptionUtils.firstCauseOfType;

public record Problem(
        long id,
        String message,
        List<String> errors,
        String source,
        String link,
        Exception exception) {

    private static final AtomicLong lastId = new AtomicLong();

    public static Problem create(Exception e) {
        return create(e, getSource(e));
    }

    public static Problem create(Exception e, ErrorPosition errorPosition) {
        return new Problem(lastId.incrementAndGet(),
                getMessage(e), getExceptionMessages(e),
                errorPosition == null ? null : errorPosition.extract(),
                getLink(e),
                e);
    }

    private static String getMessage(Exception e) {
        return e.getClass().getSimpleName() + streamCauses(e)
                .filter(ex -> ex.getMessage() != null)
                .findFirst()
                .map(t -> ": " + t.getMessage())
                .orElse("");
    }

    private static List<String> getExceptionMessages(Exception e) {
        return streamCauses(e)
                .filter(ex -> ex.getMessage() != null)
                .skip(1)
                .map(Throwable::getMessage)
                .collect(Collectors.toUnmodifiableList());
    }

    private static ErrorPosition getSource(Exception e) {
        return firstCauseOfType(e, HasErrorPosition.class)
                .map(HasErrorPosition::getErrorPosition)
                .orElse(null);
    }

    private static String getLink(Exception e) {
        return firstCauseOfType(e, HasLink.class)
                .map(HasLink::getSolveLink)
                .orElse(null);
    }
}
