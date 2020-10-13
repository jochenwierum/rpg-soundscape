package de.jowisoftware.rpgsoundscape.player.rest;

import de.jowisoftware.rpgsoundscape.player.player.EffectPlayer;
import de.jowisoftware.rpgsoundscape.player.player.MusicPlayer;
import de.jowisoftware.rpgsoundscape.player.player.SoundscapePlayer;
import de.jowisoftware.rpgsoundscape.player.library.EffectLibrary;
import de.jowisoftware.rpgsoundscape.player.library.MusicLibrary;
import de.jowisoftware.rpgsoundscape.player.library.SoundscapeLibrary;
import de.jowisoftware.rpgsoundscape.model.Effect;
import de.jowisoftware.rpgsoundscape.model.Soundscape;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    private final SoundscapeLibrary soundscapeRepository;
    private final MusicLibrary musicRepository;
    private final EffectLibrary effectLibrary;

    private final SoundscapePlayer soundscapePlayer;
    private final MusicPlayer musicPlayer;
    private final EffectPlayer effectPlayer;

    public PlayerController(
            SoundscapeLibrary soundscapeRepository,
            MusicLibrary musicRepository,
            EffectLibrary effectLibrary,
            SoundscapePlayer soundscapePlayer,
            MusicPlayer musicPlayer,
            EffectPlayer effectPlayer) {
        this.soundscapeRepository = soundscapeRepository;
        this.musicRepository = musicRepository;
        this.effectLibrary = effectLibrary;
        this.soundscapePlayer = soundscapePlayer;
        this.musicPlayer = musicPlayer;
        this.effectPlayer = effectPlayer;
    }

    @PostMapping("/soundscape")
    public ResponseEntity<?> play(@RequestBody NameDto nameDto) {
        Soundscape soundscape = soundscapeRepository.get(nameDto.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        soundscapePlayer.switchSoundscape(soundscape);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/soundscape/pausetrack")
    public ResponseEntity<?> pauseTrack(@RequestBody NameDto nameDto) {
        soundscapePlayer.pauseTrack(nameDto.name());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/soundscape/resumetrack")
    public ResponseEntity<?> resume(@RequestBody NameDto nameDto) {
        soundscapePlayer.resumeTrack(nameDto.name());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/soundscape/reset")
    public ResponseEntity<?> reset() {
        soundscapePlayer.resetAllTracks();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/soundscape/pauseAll")
    public ResponseEntity<?> pauseAll() {
        soundscapePlayer.pauseAll();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/music")
    public ResponseEntity<?> playMusic(@RequestBody NameDto nameDto) {
        Effect music = musicRepository.get(nameDto.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        musicPlayer.switchMusic(music);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/music/pause")
    public ResponseEntity<?> pauseMusic() {
        musicPlayer.pause();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/music/resume")
    public ResponseEntity<?> resumeMusic() {
        musicPlayer.resume();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/music/restart")
    public ResponseEntity<?> resetMusic() {
        musicPlayer.restart();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/effect")
    public ResponseEntity<?> playEffect(@RequestBody NameDto nameDto) {
        Effect music = effectLibrary.get(nameDto.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        effectPlayer.playEffect(music);

        return ResponseEntity.noContent().build();
    }
}
