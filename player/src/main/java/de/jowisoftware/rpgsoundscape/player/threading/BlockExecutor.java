package de.jowisoftware.rpgsoundscape.player.threading;

import de.jowisoftware.rpgsoundscape.model.Statement;
import de.jowisoftware.rpgsoundscape.player.player.SoundscapePlayer;
import de.jowisoftware.rpgsoundscape.player.interpreter.StatementInterpreterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class BlockExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(BlockExecutor.class);

    private final String name;
    private final Statement statement;
    private final boolean looping;
    protected final SoundscapePlayer soundscapePlayer;
    protected final StatementInterpreterService statementInterpreterService;
    protected final ExecutionThreadPool executionThreadPool;

    private boolean quit = false;
    private final Pause pause = new Pause(true);

    private final CallStack stack = new CallStack();
    protected final Consumer<Boolean> onStatusChange;
    private final Runnable finished;

    protected TrackExecutionContext context;

    public BlockExecutor(
            String name,
            Statement statement,
            boolean looping,
            SoundscapePlayer soundscapePlayer,
            Consumer<Boolean> onStatusChange,
            Runnable finished,
            StatementInterpreterService statementInterpreterService,
            ExecutionThreadPool executionThreadPool) {

        this.name = name;
        this.statement = statement;
        this.looping = looping;
        this.soundscapePlayer = soundscapePlayer;
        this.onStatusChange = onStatusChange;
        this.finished = finished;
        this.statementInterpreterService = statementInterpreterService;
        this.executionThreadPool = executionThreadPool;

        resetStack();
        executionThreadPool.submitThread(this::runAsync);
    }

    protected void setContext(TrackExecutionContext context) {
        this.context = context;
    }

    private void resetStack() {
        stack.reset(statementInterpreterService.createStep(statement));
    }

    private void runAsync() {
        try {
            while (!quit) {
                pause.awaitToPass();
                if (quit) {
                    return;
                }

                boolean hasMore = stack.next(context);

                if (!hasMore) {
                    resetStack();
                    if (!looping) {
                        pause.pause();
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Unhandled exception in block execution", e);
        }

        LOG.trace("Thread {}: executed all statements - run finished block", name);
        finished.run();
    }

    public synchronized void pause() {
        if (pause.pause()) {
            context.pauseTasks();
            LOG.trace("Thread " + name + " is now paused");
            onStatusChange.accept(false);
        } else {
            LOG.trace("Thread " + name + " already is paused");
        }
    }

    public synchronized void resume() {
        if (pause.resume()) {
            LOG.trace("Thread " + name + " is now resumed");
            context.resumeTasks();
            onStatusChange.accept(true);
        } else {
            LOG.trace("Thread " + name + " already is resumed");
        }
    }

    public void abort() {
        quit = true;
        pause.resume();
        context.abortTasks();
    }

    public String getName() {
        return name;
    }

    public boolean isLooping() {
        return looping;
    }
}
