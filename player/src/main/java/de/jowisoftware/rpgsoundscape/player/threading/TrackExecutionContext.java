package de.jowisoftware.rpgsoundscape.player.threading;

import de.jowisoftware.rpgsoundscape.player.player.SoundscapePlayer;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.InterruptibleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrackExecutionContext {
    private static final Logger LOG = LoggerFactory.getLogger(TrackExecutionContext.class);

    private final String name;
    private final TrackExecutor trackExecutor;
    private final SoundscapePlayer soundscapePlayer;
    private final List<InterruptibleTask> interruptibleTasks = new CopyOnWriteArrayList<>();

    private volatile TaskState state;

    public TrackExecutionContext(String name, TrackExecutor trackExecutor, SoundscapePlayer soundscapePlayer) {
        this.name = name;
        this.trackExecutor = trackExecutor;
        this.soundscapePlayer = soundscapePlayer;
    }

    public TrackExecutor getTrackExecutor() {
        return trackExecutor;
    }

    public SoundscapePlayer getSoundscapeExecutor() {
        return soundscapePlayer;
    }

    public void runInterruptible(InterruptibleTask task) {
        synchronized (this) {
            if (state == TaskState.ABORTED) {
                LOG.trace("Task is already aborted - do not start");
                return;
            }
            interruptibleTasks.add(task);
        }

        task.run(state == TaskState.RUNNING);

        interruptibleTasks.remove(task);
    }

    public synchronized void pauseTasks() {
        state = TaskState.PAUSED;
        interruptibleTasks.forEach(InterruptibleTask::pause);
    }

    public synchronized void resumeTasks() {
        state = TaskState.RUNNING;
        interruptibleTasks.forEach(InterruptibleTask::startOrResume);
    }

    public synchronized void abortTasks() {
        state = TaskState.ABORTED;
        interruptibleTasks.forEach(InterruptibleTask::abort);
    }

    public String getName() {
        return name;
    }

    private enum TaskState {
        RUNNING, PAUSED, ABORTED
    }
}
