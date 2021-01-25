package de.jowisoftware.rpgsoundscape.player.threading;

import de.jowisoftware.rpgsoundscape.model.Statement;
import de.jowisoftware.rpgsoundscape.model.Track;
import de.jowisoftware.rpgsoundscape.player.interpreter.StatementInterpreterService;
import de.jowisoftware.rpgsoundscape.player.player.SoundscapePlayer;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutor.OnFinishedAction;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.InterruptibleTask;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.Latch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class BlockExecutionContext {
    public static final Consumer<Boolean> NO_CALLBACK = ignored -> {
    };

    private static final Logger LOG = LoggerFactory.getLogger(BlockExecutionContext.class);

    private final SoundscapePlayer soundscapePlayer;
    private final StatementInterpreterService statementInterpreterService;
    private final ExecutionThreadPool executionThreadPool;
    private final String name;

    private final Latch latch = new Latch(true);
    private final Consumer<Boolean> onStatusChange;
    private final List<InterruptibleTask> interruptibleTasks = new CopyOnWriteArrayList<>();
    private final boolean looping;
    private volatile boolean quit;

    public BlockExecutionContext(
            Track track,
            Consumer<Boolean> onStatusChange,
            SoundscapePlayer soundscapePlayer,
            StatementInterpreterService statementInterpreterService,
            ExecutionThreadPool executionThreadPool) {

        this.name = track.name();
        this.looping = track.looping();
        this.onStatusChange = onStatusChange;
        this.soundscapePlayer = soundscapePlayer;
        this.statementInterpreterService = statementInterpreterService;
        this.executionThreadPool = executionThreadPool;

        OnFinishedAction onFinishedAction = track.looping()
                ? OnFinishedAction.LOOP
                : OnFinishedAction.PAUSE;

        startExecutor(track.statement(), onFinishedAction);
    }

    public BlockExecutionContext(
            String name,
            Consumer<Boolean> onStatusChange,
            StatementInterpreterService statementInterpreterService,
            ExecutionThreadPool executionThreadPool) {

        this.name = name;
        this.looping = false;
        this.onStatusChange = onStatusChange;
        this.soundscapePlayer = null;
        this.statementInterpreterService = statementInterpreterService;
        this.executionThreadPool = executionThreadPool;
    }

    public Future<Void> startBlockExecutor(Statement statement, boolean loop) {
        return startExecutor(statement, loop ? OnFinishedAction.LOOP : OnFinishedAction.EXIT);
    }

    private Future<Void> startExecutor(Statement statement, OnFinishedAction onFinishedAction) {
        return new BlockExecutor(this, statement, onFinishedAction,
                this.statementInterpreterService, this.executionThreadPool)
                .getFuture();
    }

    public SoundscapePlayer getSoundscapePlayer() {
        return soundscapePlayer;
    }

    public void runInterruptible(InterruptibleTask task) {
        synchronized (this) {
            if (quit) {
                LOG.trace("Task is already aborted - do not start");
                return;
            }
            interruptibleTasks.add(task);
        }

        try {
            task.run(!latch.isClosed());
        } finally {
            interruptibleTasks.remove(task);
        }
    }

    public synchronized void pause() {
        if (!latch.close()) {
            LOG.trace("Thread " + name + " is already paused");
            return;
        }

        LOG.trace("Thread " + name + " is now paused");
        interruptibleTasks.forEach(InterruptibleTask::pause);
        onStatusChange.accept(false);
    }

    public synchronized void resume() {
        if (!latch.open()) {
            LOG.trace("Thread " + name + " already is resumed");
            return;
        }

        LOG.trace("Thread " + name + " is now resumed");
        interruptibleTasks.forEach(InterruptibleTask::startOrResume);
        onStatusChange.accept(true);
    }

    void awaitPauseToPass() {
        latch.waitForOpen();
    }

    public synchronized void abort() {
        quit = true;
        interruptibleTasks.forEach(InterruptibleTask::abort);
        onStatusChange.accept(false);
        latch.open();
    }

    boolean isQuit() {
        return quit;
    }

    public String getName() {
        return name;
    }

    public boolean isLooping() {
        return looping;
    }

}
