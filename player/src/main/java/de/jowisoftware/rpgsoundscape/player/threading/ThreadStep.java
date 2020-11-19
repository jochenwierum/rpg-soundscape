package de.jowisoftware.rpgsoundscape.player.threading;

import java.util.function.Function;

@FunctionalInterface
public interface ThreadStep extends Function<BlockExecutionContext, StackResult> {
}
