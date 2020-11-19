package de.jowisoftware.rpgsoundscape.player.interpreter;

import de.jowisoftware.rpgsoundscape.model.Parallelly;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;

import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ParallellyStatement {
    private final BlockExecutionContext context;
    private final Parallelly parallelly;

    public ParallellyStatement(BlockExecutionContext context, Parallelly parallelly) {
        this.context = context;
        this.parallelly = parallelly;
    }

    public void run() {
        parallelly.statements().stream()
                .map(s -> context.startBlockExecutor(s, false))
                .collect(Collectors.toList())
                .forEach(f -> {
                    try {
                        f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
