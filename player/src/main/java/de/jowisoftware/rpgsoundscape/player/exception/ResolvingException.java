package de.jowisoftware.rpgsoundscape.player.exception;

import de.jowisoftware.rpgsoundscape.exceptions.ErrorPosition;
import de.jowisoftware.rpgsoundscape.exceptions.HasErrorPosition;

public class ResolvingException extends RuntimeException implements HasErrorPosition {
    private final ErrorPosition errorPosition;

    public ResolvingException(String message, Throwable cause, ErrorPosition errorPosition) {
        super(message, cause);
        this.errorPosition = errorPosition;
    }

    @Override
    public ErrorPosition getErrorPosition() {
        return errorPosition;
    }
}
