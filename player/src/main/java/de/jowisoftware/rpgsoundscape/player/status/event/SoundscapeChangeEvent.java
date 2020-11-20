package de.jowisoftware.rpgsoundscape.player.status.event;

import java.util.Set;

public record SoundscapeChangeEvent(String soundscape, boolean forceReload, Set<String> runningTracks) {
}
