package de.jowisoftware.rpgsoundscape.player.interpreter;

import de.jowisoftware.rpgsoundscape.model.Block;
import de.jowisoftware.rpgsoundscape.model.NoOp;
import de.jowisoftware.rpgsoundscape.model.Parallelly;
import de.jowisoftware.rpgsoundscape.model.Pause;
import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.model.Randomly;
import de.jowisoftware.rpgsoundscape.model.Randomly.RandomChoice;
import de.jowisoftware.rpgsoundscape.model.Range;
import de.jowisoftware.rpgsoundscape.model.Repeat;
import de.jowisoftware.rpgsoundscape.model.Resume;
import de.jowisoftware.rpgsoundscape.model.Sleep;
import de.jowisoftware.rpgsoundscape.model.Statement;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.AudioFrontend;
import de.jowisoftware.rpgsoundscape.player.player.SoundscapePlayer;
import de.jowisoftware.rpgsoundscape.player.threading.StackResult;
import de.jowisoftware.rpgsoundscape.player.threading.ThreadStep;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class StatementInterpreterService {
    private static final Logger LOG = LoggerFactory.getLogger(StatementInterpreterService.class);

    private final AudioFrontend audioFrontend;

    public StatementInterpreterService(AudioFrontend audioFrontend) {
        this.audioFrontend = audioFrontend;
    }

    public ThreadStep createStep(Statement statement) {
        if (statement instanceof Block b) {
            return block(b);
        } else if (statement instanceof Play p) {
            return play(p);
        } else if (statement instanceof Sleep s) {
            return sleep(s);
        } else if (statement instanceof Pause p) {
            return pause(p);
        } else if (statement instanceof Resume r) {
            return resume(r);
        } else if (statement instanceof Parallelly p) {
            return parallelly(p);
        } else if (statement instanceof Repeat r) {
            return repeat(r);
        } else if (statement instanceof Randomly r) {
            return randomly(r);
        } else if (statement instanceof NoOp n) {
            return noop();
        } else {
            throw new IllegalArgumentException("Unknown statement: " + statement);
        }
    }

    private ThreadStep block(Block statement) {
        AtomicInteger nextStatement = new AtomicInteger(0);

        return (context) -> {
            int i = nextStatement.get();
            ThreadStep step = createStep(statement.statements().get(i));
            StackResult result;

            if (i == statement.statements().size() - 1) {
                result = StackResult.replace(step);
            } else {
                result = StackResult.enter(step);
                nextStatement.incrementAndGet();
            }

            return result;
        };
    }

    private ThreadStep play(Play statement) {
        return context -> {
            trace(context, "Now playing: {}", statement.sample().name());
            audioFrontend.play(context, statement);
            trace(context, "Playing finished: {}", statement.sample().name());
            return StackResult.finish();
        };
    }

    private ThreadStep repeat(Repeat statement) {
        Range<Long> range = statement.range();
        long count = range.max()
                .map(max -> randomlyBetween(range.min(), max))
                .orElse(range.min());
        var remaining = new AtomicLong(count);

        return context -> {
            long i = remaining.decrementAndGet();
            trace(context, "Track: {}: next iteration (i = {} of {})",
                    context.getName(), count - i, count);
            if (i == -1) {
                return StackResult.finish();
            } else {
                return StackResult.enter(createStep(statement.statement()));
            }
        };
    }

    private ThreadStep sleep(Sleep statement) {
        return context -> {
            Range<Duration> range = statement.range();
            long millis = Math.max(25, range.max()
                    .map(max -> randomlyBetween(range.min().toMillis(), max.toMillis()))
                    .orElse(range.min().toMillis()));

            trace(context, "Track: {}: sleeping {}ms", context.getName(), millis);
            context.runInterruptible(new SleepStatement(millis));
            trace(context, "Track: {}: sleep finished");
            return StackResult.finish();
        };
    }

    private ThreadStep randomly(Randomly statement) {
        return context -> {
            long totalWeight = statement.summedWeights();
            long selectedWeight = randomlyBetween(0, totalWeight - 1);

            long summed = 0;
            int i = 0;
            for (int choicesSize = statement.choices().size(); i < choicesSize; i++) {
                summed += statement.choices().get(i).weight();
                if (selectedWeight < summed) {
                    break;
                }
            }

            trace(context, "Track {}: Selected {}. option (summed weight={} of total weight={})",
                    context.getName(), i, selectedWeight, totalWeight);
            RandomChoice selectedChoice = statement.choices().get(i);

            return StackResult.replace(createStep(selectedChoice.statement()));
        };
    }

    private ThreadStep pause(Pause statement) {
        return context -> {
            SoundscapePlayer player = context.getSoundscapePlayer();
            switch (statement.pauseMode()) {
                case THIS -> context.pause();
                case SPECIFIC -> player.getTrackContext(statement.track())
                        .orElseThrow(() -> new IllegalStateException("Track '" + statement.track() + "' does not exist"))
                        .pause();
                case ALL -> player.getTrackContexts()
                        .forEach(BlockExecutionContext::pause);
                case OTHER -> player.getTrackContexts()
                        .stream()
                        .filter(t -> t.getName().equals(context.getName()))
                        .forEach(BlockExecutionContext::pause);
            }

            return StackResult.finish();
        };
    }

    private ThreadStep resume(Resume statement) {
        return context -> {
            SoundscapePlayer player = context.getSoundscapePlayer();

            switch (statement.resumeMode()) {
                case LOOPING -> context.getSoundscapePlayer().getTrackContexts()
                        .stream()
                        .filter(BlockExecutionContext::isLooping)
                        .forEach(BlockExecutionContext::pause);
                case SPECIFIC -> player.getTrackContext(statement.track())
                        .orElseThrow(() -> new IllegalStateException("Track '" + statement.track() + "' does not exist"))
                        .resume();
            }
            return StackResult.finish();
        };
    }

    private ThreadStep noop() {
        return context -> StackResult.finish();
    }

    private ThreadStep parallelly(Parallelly statement) {
        return context -> {
            ParallellyStatement runner = new ParallellyStatement(context, statement);
            runner.run();
            return StackResult.finish();
        };
    }

    private long randomlyBetween(long minInclusive, long maxInclusive) {
        long range = maxInclusive - minInclusive;
        return minInclusive + (long) (Math.random() * (range + 1));
    }

    private void trace(BlockExecutionContext context, String message, Object... args) {
        LOG.trace("Thread " + context.getName() + ": " + message, args);
    }
}
