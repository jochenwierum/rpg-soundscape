package de.jowisoftware.rpgsoundscape.intellij;

import com.intellij.lexer.FlexAdapter;

public class SoundscapeLexerAdapter extends FlexAdapter {
    public SoundscapeLexerAdapter() {
        super(new SoundscapeLexer(null));
    }
}
