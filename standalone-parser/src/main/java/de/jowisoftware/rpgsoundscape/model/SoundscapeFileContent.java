package de.jowisoftware.rpgsoundscape.model;

import java.util.Map;

public record SoundscapeFileContent(
        Map<String, Soundscape> soundscapes,
        Map<String, Effect> effects,
        Map<String, Effect> music
) {
}
