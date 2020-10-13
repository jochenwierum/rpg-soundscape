package de.jowisoftware.rpgsoundscape.player.library;

import org.springframework.stereotype.Component;

@Component
public class EffectLibrary extends AbstractEffectLibrary {
    @Override
    protected String getType() {
        return "effect";
    }
}
