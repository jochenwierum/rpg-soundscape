package de.jowisoftware.rpgsoundscape.language;

import com.intellij.lang.Language;

public class SoundscapeLanguage extends Language {
    public static final SoundscapeLanguage INSTANCE = new SoundscapeLanguage();

    private SoundscapeLanguage() {
        super("Soundscape");
    }
}
