package de.jowisoftware.rpgsoundscape.player.player;

import de.jowisoftware.rpgsoundscape.player.interpreter.StatementInterpreterService;
import de.jowisoftware.rpgsoundscape.player.threading.ExecutionThreadPool;
import de.jowisoftware.rpgsoundscape.player.threading.ThreadStep;
import de.jowisoftware.rpgsoundscape.player.threading.TrackExecutionContext;
import de.jowisoftware.rpgsoundscape.model.Effect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EffectPlayer {
    private static final Logger LOG = LoggerFactory.getLogger(EffectPlayer.class);

    private final ExecutionThreadPool executionThreadPool;
    private final StatementInterpreterService statementInterpreterService;
    private final TrackExecutionContext context = new TrackExecutionContext("effects", null, null);

    public EffectPlayer(ExecutionThreadPool executionThreadPool, StatementInterpreterService statementInterpreterService) {
        this.executionThreadPool = executionThreadPool;
        this.statementInterpreterService = statementInterpreterService;

        executionThreadPool.onShutdown(this::quit);
    }

    private void quit() {
        context.abortTasks();
    }

    public void playEffect(Effect effect) {
        ThreadStep step = statementInterpreterService.createStep(effect.play());
        executionThreadPool.submitThread(() -> {
            try {
                step.apply(context);
            } catch (RuntimeException e) {
                LOG.error("Unhandled error while playing effect", e);
            }
        });
    }
}
