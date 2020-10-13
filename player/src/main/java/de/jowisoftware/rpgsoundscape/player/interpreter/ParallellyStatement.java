package de.jowisoftware.rpgsoundscape.player.interpreter;

import de.jowisoftware.rpgsoundscape.player.threading.InterruptibleTask;
import de.jowisoftware.rpgsoundscape.player.threading.TrackExecutionContext;
import de.jowisoftware.rpgsoundscape.model.Parallelly;

import java.util.concurrent.atomic.AtomicInteger;

public class ParallellyStatement extends InterruptibleTask {
    public ParallellyStatement(TrackExecutionContext context, Parallelly parallelly) {
        var remaining = new AtomicInteger(parallelly.statements().size());

        parallelly.statements().forEach(statement ->
                context.getTrackExecutor().createBlockExecutor(statement, () -> {
                    if (remaining.decrementAndGet() == 0) {
                        finish();
                    }
                })
        );
    }

    @Override
    public void pause() {
        // blocking sub statement's pause() methods are called too, so no need to do anything here
    }

    @Override
    public void startOrResume() {
        // blocking sub statement's startOrResume() methods are called too, so no need to do anything here
    }

    @Override
    public void abort() {
        // blocking sub statement's abort() methods are called too, so no need to do anything here
    }
}
