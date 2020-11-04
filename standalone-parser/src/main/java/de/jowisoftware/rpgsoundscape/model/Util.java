package de.jowisoftware.rpgsoundscape.model;

import com.intellij.psi.PsiElement;
import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Util {
    private Util() {
    }

    public static <S extends PsiElement, T> Consumer<S> collectChecked(
            Map<String, T> targetmap,
            Function<S, String> nameTransformation, Function<S, T> transformation) {
        return source -> collectChecked(source, targetmap, nameTransformation.apply(source), transformation.apply(source));
    }

    public static <T> void collectChecked(PsiElement source, Map<String, T> targetmap, String key, T value) {
        if (targetmap.put(key, value) != null) {
            throw new SemanticException(source, "%s with the name '%s' already exists".formatted(source.getClass().getSimpleName(), key));
        }
    }
}
