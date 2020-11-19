package de.jowisoftware.rpgsoundscape.player.threading;

import de.jowisoftware.rpgsoundscape.model.Statement;
import de.jowisoftware.rpgsoundscape.player.interpreter.StatementInterpreterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

class BlockExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(BlockExecutor.class);

    private final Statement statement;
    private final StatementInterpreterService statementInterpreterService;

    private final CallStack stack = new CallStack();
    private final OnFinishedAction onFinishedAction;

    private final BlockExecutionContext context;
    private final Future<Void> future;

    BlockExecutor(
            BlockExecutionContext context, Statement statement,
            OnFinishedAction onFinishedAction, StatementInterpreterService statementInterpreterService,
            ExecutionThreadPool executionThreadPool) {
        this.statement = statement;
        this.statementInterpreterService = statementInterpreterService;
        this.onFinishedAction = onFinishedAction;
        this.context = context;

        resetStack();
        future = executionThreadPool.submitThread(this::runAsync);
    }

    private void resetStack() {
        stack.reset(statementInterpreterService.createStep(statement));
    }

    private void runAsync() {
        try {
            while (!context.isQuit()) {
                context.awaitPauseToPass();
                if (context.isQuit()) {
                    return;
                }

                boolean hasMore = stack.next(context);

                if (!hasMore) {
                    switch (onFinishedAction) {
                        case EXIT -> {
                            LOG.trace("Thread {}: executed all statements - exiting", context.getName());
                            return;
                        }
                        case PAUSE -> {
                            LOG.trace("Thread {}: executed all statements - pause thread", context.getName());
                            resetStack();
                            context.pause();
                        }
                        case LOOP -> {
                            LOG.trace("Thread {}: executed all statements - restarting", context.getName());
                            resetStack();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Unhandled exception in block execution", e);
        }
    }

    Future<Void> getFuture() {
        return future;
    }

    enum OnFinishedAction {
        LOOP, PAUSE, EXIT
    }
}
