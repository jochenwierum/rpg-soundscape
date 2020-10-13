package de.jowisoftware.rpgsoundscape.exceptions;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;

public class SyntaxException extends RuntimeException implements HasErrorPosition {
    private final String errorDescription;
    private final String text;

    private final ErrorPosition errorPosition;
    private final PsiFile psiFile;

    public SyntaxException(PsiErrorElement element) {
        this.errorDescription = element.getErrorDescription();
        this.text = element.getText();
        this.errorPosition = new ErrorPosition(element);
        this.psiFile = element.getContainingFile();
    }

    public SyntaxException(PsiElement element, String message) {
        this.errorDescription = message;
        this.text = element.getText();
        this.errorPosition = new ErrorPosition(element);
        this.psiFile = null;
    }

    @Override
    public String getMessage() {
        return "%s in \"%s\" at %s".formatted(errorDescription, text, errorPosition.positionString());
    }

    @Override
    public ErrorPosition getErrorPosition() {
        return errorPosition;
    }

    public String formatTree() {
        if (psiFile == null) {
            return null;
        }

        var sb = new StringBuilder();
        formatTree(sb, psiFile.getNode().getPsi(), "");
        return sb.toString();
    }

    private void formatTree(StringBuilder sb, PsiElement element, String indent) {
        if (element instanceof PsiWhiteSpaceImpl || element instanceof PsiCommentImpl) {
            return;
        }

        var cleanText = element.getText()
                .replaceAll("\\n", "\\\\n")
                .replaceAll(" +", " ");

        if (element instanceof PsiErrorElement e) {
            sb.append("%s- Error: %s [%s]%n".formatted(
                    indent, e.getErrorDescription(), cleanText));
        } else {
            sb.append("%s- %s: %s%n".formatted(
                    indent, element.getClass().getSimpleName(), cleanText));
        }

        for (PsiElement child : element.getChildren()) {
            formatTree(sb, child, indent + "  ");
        }
    }
}
