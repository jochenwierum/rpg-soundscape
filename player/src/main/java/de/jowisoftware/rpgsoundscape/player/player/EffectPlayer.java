package de.jowisoftware.rpgsoundscape.player.player;

import de.jowisoftware.rpgsoundscape.model.Effect;
import de.jowisoftware.rpgsoundscape.player.interpreter.StatementInterpreterService;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;
import de.jowisoftware.rpgsoundscape.player.threading.ExecutionThreadPool;
import org.springframework.stereotype.Component;

@Component
public class EffectPlayer {
    private final BlockExecutionContext context;

    public EffectPlayer(ExecutionThreadPool executionThreadPool, StatementInterpreterService statementInterpreterService) {
        this.context = new BlockExecutionContext("effects", BlockExecutionContext.NO_CALLBACK,
                statementInterpreterService, executionThreadPool);
        context.resume();

        executionThreadPool.onShutdown(context::abort);
    }

    public void playEffect(Effect effect) {
        context.startBlockExecutor(effect.play(), false);
    }
}
