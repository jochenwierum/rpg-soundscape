package de.jowisoftware.rpgsoundscape.player.player;

import de.jowisoftware.rpgsoundscape.model.Effect;
import de.jowisoftware.rpgsoundscape.player.library.LibraryUpdatedEvent;
import de.jowisoftware.rpgsoundscape.player.library.MusicLibrary;
import de.jowisoftware.rpgsoundscape.player.interpreter.StatementInterpreterService;
import de.jowisoftware.rpgsoundscape.player.status.event.MusicChangedEvent;
import de.jowisoftware.rpgsoundscape.player.status.StatusReporter;
import de.jowisoftware.rpgsoundscape.player.threading.ExecutionThreadPool;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.Pause;
import de.jowisoftware.rpgsoundscape.player.threading.ThreadStep;
import de.jowisoftware.rpgsoundscape.player.threading.TrackExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MusicPlayer {
    private static final Logger LOG = LoggerFactory.getLogger(MusicPlayer.class);

    private final StatusReporter statusReporter;
    private final StatementInterpreterService statementInterpreterService;
    private final MusicLibrary library;

    private final Pause pause = new Pause(true);
    private volatile ThreadStep currentStep;
    private volatile boolean quit;

    private final TrackExecutionContext context = new TrackExecutionContext("music", null, null);
    private volatile Effect currentMusic = null;

    public MusicPlayer(
            StatusReporter statusReporter,
            ExecutionThreadPool executionThreadPool, StatementInterpreterService statementInterpreterService, MusicLibrary musicLibrary) {
        this.statusReporter = statusReporter;
        this.statementInterpreterService = statementInterpreterService;
        this.library = musicLibrary;

        executionThreadPool.onShutdown(this::quit);
        executionThreadPool.submitThread(this::runAsync);
    }


    @EventListener(LibraryUpdatedEvent.class)
    public void libraryReloaded() {
        if (currentMusic == null) {
            return;
        }

        library.get(currentMusic.name()).ifPresentOrElse(
                this::switchMusic,
                () -> {
                    if (currentStep == null) {
                        return;
                    }

                    pause.pause();
                    context.abortTasks();
                    this.currentMusic = null;
                    this.currentStep = null;
                    reportStatus(false);
                }
        );
    }

    private void runAsync() {
        while (!quit) {
            pause.awaitToPass();
            if (quit) {
                return;
            }

            if (currentStep != null) {
                reportStatus(true);
                LOG.trace("Starting to play music " + currentMusic);
                currentStep.apply(context);
                LOG.trace("Music finished: " + currentMusic);
            } else {
                pause.pause(); // should never happen
            }
        }
    }

    private void reportStatus(boolean playing) {
        statusReporter.reportMusicChanged(new MusicChangedEvent(currentMusic == null ? "" : currentMusic.name(), playing));
    }

    private void quit() {
        this.quit = true;
        context.abortTasks();
        pause.resume();
    }

    public synchronized void switchMusic(Effect music) {
        if (music.equals(currentMusic)) {
            return;
        }

        if (currentStep != null) {
            context.abortTasks();
        }

        currentMusic = music;
        currentStep = statementInterpreterService.createStep(music.play());
        pause.resume();
    }

    public void pause() {
        if (currentStep == null) {
            return;
        }

        pause.pause();
        context.pauseTasks();
        reportStatus(false);
    }

    public void resume() {
        if (currentStep == null) {
            return;
        }

        pause.resume();
        context.resumeTasks();
        reportStatus(true);
    }

    public void restart() {
        if (currentStep == null) {
            return;
        }

        pause.resume();
        context.abortTasks();
        reportStatus(true);
    }
}
