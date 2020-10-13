package de.jowisoftware.rpgsoundscape.exceptions;

import com.intellij.psi.PsiElement;

public class SemanticException extends RuntimeException implements HasErrorPosition {
    private final ErrorPosition errorPosition;
    private final String message;

    public SemanticException(PsiElement element, String message) {
        this.errorPosition = new ErrorPosition(element);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message + " at " + errorPosition.positionString();
    }

    @Override
    public ErrorPosition getErrorPosition() {
        return errorPosition;
    }
}
