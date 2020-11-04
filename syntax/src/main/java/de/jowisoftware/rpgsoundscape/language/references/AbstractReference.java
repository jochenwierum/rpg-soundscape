package de.jowisoftware.rpgsoundscape.language.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractReference<T extends PsiElement> extends PsiReferenceBase<T> {
    protected final String name;

    public AbstractReference(@NotNull T element, TextRange textRange) {
        super(element, textRange);
        name = element.getText().substring(textRange.getStartOffset(), textRange.getEndOffset());
    }
}
