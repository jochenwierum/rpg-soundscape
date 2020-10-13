package de.jowisoftware.rpgsoundscape.player.library;

import de.jowisoftware.rpgsoundscape.model.Sample;
import de.jowisoftware.rpgsoundscape.model.Soundscape;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SoundscapeLibrary extends AbstractAssetLibrary<Soundscape> {
    @Override
    public Set<Sample> add(String name, Soundscape soundscape) {
        addEntry(name, soundscape);
        return soundscape.collectSamples();
    }
}
