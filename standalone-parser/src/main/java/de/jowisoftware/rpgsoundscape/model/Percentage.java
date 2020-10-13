package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.intellij.psi.SPercentage;

public record Percentage(int value) {
    static Percentage from(SPercentage percentage) {
        return new Percentage(Util.parse(percentage));
    }

    public double toDouble() {
        return value / 100.0;
    }

    public Percentage add(Percentage other) {
        return new Percentage(value + other.value);
    }
}
