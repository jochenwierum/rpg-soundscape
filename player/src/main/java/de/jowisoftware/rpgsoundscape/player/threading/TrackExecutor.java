package de.jowisoftware.rpgsoundscape.player.threading;

import de.jowisoftware.rpgsoundscape.model.Statement;
import de.jowisoftware.rpgsoundscape.model.Track;
import de.jowisoftware.rpgsoundscape.player.player.SoundscapePlayer;
import de.jowisoftware.rpgsoundscape.player.interpreter.StatementInterpreterService;

import java.util.function.Consumer;

public class TrackExecutor extends BlockExecutor {
    public TrackExecutor(
            String name, Track track, SoundscapePlayer executor,
            Consumer<Boolean> onStatusChanged,
            StatementInterpreterService statementInterpreterService,
            ExecutionThreadPool executionThreadPool) {
        super(
                name,
                track.statement(),
                track.looping(),
                executor,
                onStatusChanged,
                () -> onStatusChanged.accept(false),
                statementInterpreterService,
                executionThreadPool);
        setContext(new TrackExecutionContext("track:" + this.getName(), this, executor));
    }

    public void createBlockExecutor(Statement statement, Runnable finished) {
        var executor = new BlockExecutor(
                context.getTrackExecutor().getName(),
                statement,
                false,
                soundscapePlayer,
                onStatusChange, finished,
                statementInterpreterService,
                executionThreadPool);
        executor.setContext(context);
    }
}
