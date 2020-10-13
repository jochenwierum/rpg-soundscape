package de.jowisoftware.rpgsoundscape.player.library;

import de.jowisoftware.rpgsoundscape.model.Effect;
import de.jowisoftware.rpgsoundscape.model.Sample;

import java.util.Set;

public abstract class AbstractEffectLibrary extends AbstractAssetLibrary<Effect> {
    @Override
    public Set<Sample> add(String name, Effect content) {
        addEntry(name, content);
        return Set.of(content.play().sample());
    }

    protected abstract String getType();
}
