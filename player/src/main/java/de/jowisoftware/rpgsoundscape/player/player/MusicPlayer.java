package de.jowisoftware.rpgsoundscape.player.player;

import de.jowisoftware.rpgsoundscape.model.Effect;
import de.jowisoftware.rpgsoundscape.player.interpreter.StatementInterpreterService;
import de.jowisoftware.rpgsoundscape.player.library.LibraryUpdatedEvent;
import de.jowisoftware.rpgsoundscape.player.library.MusicLibrary;
import de.jowisoftware.rpgsoundscape.player.status.StatusReporter;
import de.jowisoftware.rpgsoundscape.player.status.event.MusicChangedEvent;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;
import de.jowisoftware.rpgsoundscape.player.threading.ExecutionThreadPool;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class MusicPlayer {
    private final StatusReporter statusReporter;
    private final ExecutionThreadPool executionThreadPool;
    private final StatementInterpreterService statementInterpreterService;
    private final MusicLibrary library;

    private volatile BlockExecutionContext context;
    private volatile Effect currentMusic = null;

    public MusicPlayer(
            StatusReporter statusReporter,
            ExecutionThreadPool executionThreadPool, StatementInterpreterService statementInterpreterService, MusicLibrary musicLibrary) {
        this.statusReporter = statusReporter;
        this.executionThreadPool = executionThreadPool;
        this.statementInterpreterService = statementInterpreterService;
        this.library = musicLibrary;

        executionThreadPool.onShutdown(this::quit);
    }

    @EventListener(LibraryUpdatedEvent.class)
    public void libraryReloaded() {
        if (currentMusic == null) {
            return;
        }

        library.get(currentMusic.name()).ifPresentOrElse(
                this::switchMusic,
                () -> {
                    if (context == null) {
                        return;
                    }

                    context.abort();
                    this.currentMusic = null;
                    reportStatus(false);
                }
        );
    }

    private void reportStatus(boolean playing) {
        statusReporter.reportMusicChanged(new MusicChangedEvent(currentMusic == null ? "" : currentMusic.name(), playing));
    }

    private void quit() {
        if (context != null) {
            context.abort();
        }
    }

    public synchronized void switchMusic(Effect music) {
        if (music.equals(currentMusic)) {
            return;
        }

        if (context != null) {
            context.abort();
        }

        currentMusic = music;
        recreateContext();
    }

    private void recreateContext() {
        context = new BlockExecutionContext("music", this::reportStatus,
                statementInterpreterService, executionThreadPool);

        context.resume();
        context.startBlockExecutor(currentMusic.play(), true);
    }

    public void pause() {
        ifContext(BlockExecutionContext::pause);
    }

    public void resume() {
        ifContext(BlockExecutionContext::resume);
    }

    public void restart() {
        ifContext(c -> {
            c.abort();
            recreateContext();
        });
    }

    private void ifContext(Consumer<BlockExecutionContext> action) {
        if (context == null) {
            return;
        }

        action.accept(context);
    }
}
