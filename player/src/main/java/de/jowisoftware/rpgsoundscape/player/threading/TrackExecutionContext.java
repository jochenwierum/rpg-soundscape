package de.jowisoftware.rpgsoundscape.player.threading;

import de.jowisoftware.rpgsoundscape.player.player.SoundscapePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

public class TrackExecutionContext {
    private static final Logger LOG = LoggerFactory.getLogger(TrackExecutionContext.class);

    private final String name;
    private final TrackExecutor trackExecutor;
    private final SoundscapePlayer soundscapePlayer;
    private final List<InterruptibleTask> interruptibleTasks = new CopyOnWriteArrayList<>();

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
        LOG.trace("Context prepares sleep on interruptible task");
        interruptibleTasks.add(task);

        var latch = new CountDownLatch(1);
        task.onFinish(latch::countDown);

        LOG.trace("Context starts interruptible task");
        task.startOrResume();

        try {
            LOG.trace("Context enters sleep");
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOG.trace("Sleep finished");
        interruptibleTasks.remove(task);
    }

    public void pauseTasks() {
        interruptibleTasks.forEach(InterruptibleTask::pause);
    }

    public void resumeTasks() {
        interruptibleTasks.forEach(InterruptibleTask::startOrResume);
    }

    public void abortTasks() {
        interruptibleTasks.forEach(InterruptibleTask::abort);
    }

    public String getName() {
        return name;
    }
}
