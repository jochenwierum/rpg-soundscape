package de.jowisoftware.rpgsoundscape.player.player;

import de.jowisoftware.rpgsoundscape.model.Soundscape;
import de.jowisoftware.rpgsoundscape.model.Track;
import de.jowisoftware.rpgsoundscape.player.library.LibraryUpdatedEvent;
import de.jowisoftware.rpgsoundscape.player.library.SoundscapeLibrary;
import de.jowisoftware.rpgsoundscape.player.interpreter.StatementInterpreterService;
import de.jowisoftware.rpgsoundscape.player.status.event.SoundscapeChangeEvent;
import de.jowisoftware.rpgsoundscape.player.status.StatusReporter;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutor;
import de.jowisoftware.rpgsoundscape.player.threading.ExecutionThreadPool;
import de.jowisoftware.rpgsoundscape.player.threading.TrackExecutor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class SoundscapePlayer implements DisposableBean {
    private final ExecutionThreadPool executionThreadPool;
    private final StatementInterpreterService statementInterpreterService;
    private final StatusReporter statusReporter;
    private final SoundscapeLibrary library;

    private final Set<TrackExecutor> tracks = ConcurrentHashMap.newKeySet();
    private final Set<String> running = ConcurrentHashMap.newKeySet();
    private Soundscape soundscape;

    public SoundscapePlayer(
            StatusReporter statusReporter,
            ExecutionThreadPool executionThreadPool,
            StatementInterpreterService statementInterpreterService, SoundscapeLibrary library) {
        this.statusReporter = statusReporter;
        this.executionThreadPool = executionThreadPool;
        this.statementInterpreterService = statementInterpreterService;
        this.library = library;
        executionThreadPool.onShutdown(this::clear);
    }

    @EventListener(LibraryUpdatedEvent.class)
    public synchronized void libraryReloaded() {
        if (this.soundscape == null) {
            return;
        }

        library.get(soundscape.name()).ifPresentOrElse(
                this::switchSoundscape,
                () -> {
                    clear();
                    this.soundscape = null;
                }
        );

        broadcastState();
    }

    private void addTrackExecution(String name, Track track) {
        tracks.add(new TrackExecutor(name, track, this, (nowRunning) -> {
            if (!nowRunning && this.running.remove(name)) {
                broadcastState();
            } else if (nowRunning && this.running.add(name)) {
                broadcastState();
            }
        }, statementInterpreterService, executionThreadPool));
    }

    private void clear() {
        tracks.forEach(BlockExecutor::abort);
        tracks.clear();
        running.clear();
    }

    public List<TrackExecutor> getTrackExecutors() {
        return new ArrayList<>(tracks);
    }

    public Optional<TrackExecutor> getTaskExecutor(String track) {
        return tracks.stream()
                .filter(t -> t.getName().equals(track))
                .findAny();
    }

    public void switchSoundscape(Soundscape soundscape) {
        if (this.soundscape != null && soundscape.name().equals(this.soundscape.name())) {
            reloadTracks(soundscape);
            return;
        }

        initTracks(soundscape);
    }

    private void initTracks(Soundscape soundscape) {
        this.soundscape = soundscape;

        clear();
        soundscape.tracks().forEach(this::addTrackExecution);
        resetAllTracks();
    }

    private void reloadTracks(Soundscape soundscape) {
        Map<String, Boolean> oldExisting = this.soundscape.tracks().keySet().stream()
                .map(k -> Map.entry(k, running.contains(k)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        initTracks(soundscape);

        for (TrackExecutor track : tracks) {
            Boolean oldValue = oldExisting.get(track.getName());
            if (oldValue != null && oldValue) {
                track.resume();
            }
            if (oldValue != null && !oldValue) {
                track.pause();
            }
        }
    }

    public synchronized void resetAllTracks() {
        // TODO: reduce number of events?
        // pauseAll = true;
        tracks.forEach(executor -> {
            Track track = soundscape.tracks().get(executor.getName());

            if (track != null && track.autoStart()) {
                executor.resume();
            } else if (track != null) {
                executor.pause();
            }
        });
        // pauseAll = false;
        // synchronizeState();
    }

    public synchronized void resumeTrack(String track) {
        Optional<TrackExecutor> thread = getTaskExecutor(track);
        if (thread.isEmpty()) {
            return;
        }

        thread.get().resume();
    }

    public synchronized void pauseTrack(String track) {
        Optional<TrackExecutor> thread = getTaskExecutor(track);
        if (thread.isEmpty()) {
            return;
        }

        thread.get().pause();
    }

    public synchronized void pauseAll() {
        tracks.forEach(BlockExecutor::pause);
    }

    private void broadcastState() {
        String name = this.soundscape != null ? this.soundscape.name() : "";
        statusReporter.reportSoundscapeChanged(
                new SoundscapeChangeEvent(name, new HashSet<>(running)));
    }

    @Override
    public void destroy() {
        clear();
    }
}
