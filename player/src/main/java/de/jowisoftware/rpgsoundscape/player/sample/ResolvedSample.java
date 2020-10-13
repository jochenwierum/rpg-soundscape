package de.jowisoftware.rpgsoundscape.player.sample;

import java.nio.file.Path;
import java.util.Optional;

public record ResolvedSample(
        Path path,
        Optional<String> attribution) {
}
