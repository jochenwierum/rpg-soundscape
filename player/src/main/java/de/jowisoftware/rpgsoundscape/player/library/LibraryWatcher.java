package de.jowisoftware.rpgsoundscape.player.library;

import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.threading.DebounceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class LibraryWatcher implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(LibraryWatcher.class);
    private final ApplicationSettings applicationSettings;

    private final WatchService watchService;
    private final Runnable debouncer;
    private volatile boolean running = true;

    private final LibraryService libraryService;

    private final Map<Path, WatchKey> watchedDirs = new ConcurrentHashMap<>();

    public LibraryWatcher(ApplicationSettings applicationSettings, LibraryService libraryService,
            DebounceService debounceService) throws IOException {
        this.applicationSettings = applicationSettings;
        this.libraryService = libraryService;

        this.watchService = FileSystems.getDefault().newWatchService();
        this.debouncer = debounceService.createDebouncer("library-update-debouncer",
                libraryService::refreshLibrary, 500, 2000);
    }

    private void collectDirs(Path dir, List<Path> result) {
        result.add(dir.toAbsolutePath().normalize());

        try {
            Files.list(dir)
                    .filter(Files::isDirectory)
                    .forEach(d -> collectDirs(d, result));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setupWatcher() {
        new Thread(() -> {
            updateWatchedDirs();
            libraryService.refreshLibrary();

            while (running) {
                try {
                    WatchKey poll = watchService.poll(500, TimeUnit.MILLISECONDS);
                    if (poll != null) {
                        poll.pollEvents() // must be called!
                                .forEach(e -> LOG.debug("Detected change: {} {}", e.kind().name(), e.context()));
                        poll.reset();

                        debouncer.run();
                        updateWatchedDirs();
                    }
                } catch (InterruptedException | ClosedWatchServiceException e) {
                    return;
                }
            }
        }, "library-update-watcher").start();
    }

    private void updateWatchedDirs() {
        var dirs = new ArrayList<Path>();
        collectDirs(applicationSettings.getLibraryPath(), dirs);

        Set<Path> toDelete = new HashSet<>(watchedDirs.keySet());

        dirs.forEach(dir -> {
            if (watchedDirs.containsKey(dir)) {
                toDelete.remove(dir);
            } else {
                try {
                    watchedDirs.put(dir, dir.register(watchService,
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_DELETE,
                            StandardWatchEventKinds.ENTRY_MODIFY));
                } catch (IOException e) {
                    LOG.warn("Unable to watch directory " + dir.toString(), e);
                }
            }
        });

        toDelete.stream()
                .map(watchedDirs::remove)
                .forEach(WatchKey::cancel);

        LOG.debug("Now watching: {}", watchedDirs.keySet());
    }

    @Override
    public void destroy() throws Exception {
        running = false;
        watchService.close();
    }
}
