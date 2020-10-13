package de.jowisoftware.rpgsoundscape.model;

import com.intellij.psi.PsiElement;
import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.intellij.psi.SInt;
import de.jowisoftware.rpgsoundscape.intellij.psi.SPercentage;
import de.jowisoftware.rpgsoundscape.intellij.psi.SString;
import de.jowisoftware.rpgsoundscape.intellij.psi.STimespan;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Util {
    private Util() {
    }

    public static int parse(SPercentage percentage) {
        String value = percentage.getText();
        return Integer.parseInt(value.substring(0, value.length() - 1));
    }

    public static long parse(SInt ssInt) {
        return Long.parseLong(ssInt.getText());
    }

    public static String parse(SString string) {
        String s = string.getText();
        return s.substring(1, s.length() - 1);
    }

    public static Duration parse(STimespan ssTimespan) {
        String value = ssTimespan.getText();

        if (value.endsWith("ms")) {
            return Duration.ofMillis(Long.parseLong(value.substring(0, value.length() - 2)));
        } else if (value.endsWith("s")) {
            return Duration.ofSeconds(Long.parseLong(value.substring(0, value.length() - 1)));
        } else if (value.endsWith("m")) {
            return Duration.ofMinutes(Long.parseLong(value.substring(0, value.length() - 1)));
        } else {
            throw new IllegalArgumentException("Unknown duration: " + value);
        }
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
