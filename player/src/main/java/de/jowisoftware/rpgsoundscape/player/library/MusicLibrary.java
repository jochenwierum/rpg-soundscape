package de.jowisoftware.rpgsoundscape.player.library;

import org.springframework.stereotype.Component;

@Component
public class MusicLibrary extends AbstractEffectLibrary {
    @Override
    protected String getType() {
        return "music";
    }
}
