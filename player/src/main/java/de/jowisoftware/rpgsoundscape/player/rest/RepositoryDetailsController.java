package de.jowisoftware.rpgsoundscape.player.rest;

import de.jowisoftware.rpgsoundscape.player.library.AttributionService;
import de.jowisoftware.rpgsoundscape.player.library.AttributionService.Description;
import de.jowisoftware.rpgsoundscape.player.library.EffectLibrary;
import de.jowisoftware.rpgsoundscape.player.library.MusicLibrary;
import de.jowisoftware.rpgsoundscape.player.library.SoundscapeLibrary;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RepositoryDetailsController {
    private final AttributionService attributionService;
    private final SoundscapeLibrary soundscapeLibrary;
    private final MusicLibrary musicLibrary;
    private final EffectLibrary effectLibrary;

    public RepositoryDetailsController(SoundscapeLibrary soundscapeLibrary, MusicLibrary musicLibrary,
            EffectLibrary effectLibrary, AttributionService attributionService) {
        this.soundscapeLibrary = soundscapeLibrary;
        this.musicLibrary = musicLibrary;
        this.effectLibrary = effectLibrary;
        this.attributionService = attributionService;
    }

    @GetMapping("/soundscape/{name}/info")
    public Description soundscapeInfo(@PathVariable("name") String name) {
        return soundscapeLibrary.get(name)
                .map(attributionService::describeSoundscape)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/music/{name}/info")
    public Description musicInfo(@PathVariable("name") String name) {
        return musicLibrary.get(name)
                .map(attributionService::describeMusic)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/effect/{name}/info")
    public Description effectInfo(@PathVariable("name") String name) {
        return effectLibrary.get(name)
                .map(attributionService::describeEffect)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/soundscape/{name}")
    public SoundscapeDto soundscape(@PathVariable("name") String name) {
        var tracks = soundscapeLibrary.get(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .tracks().values().stream()
                .sorted()
                .map(t -> new SoundscapeTrackDto(t.name(), t.title(), t.autoStart(), t.looping()))
                .collect(Collectors.toList());

        return new SoundscapeDto(tracks);
    }

    public static record SoundscapeDto(List<SoundscapeTrackDto> tracks) {
    }

    public static record SoundscapeTrackDto(
            String name,
            String title,
            boolean autostart,
            boolean looping) {
    }
}
