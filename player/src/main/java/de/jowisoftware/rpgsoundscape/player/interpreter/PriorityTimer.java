package de.jowisoftware.rpgsoundscape.player.interpreter;

import de.jowisoftware.rpgsoundscape.player.threading.InterruptibleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class PriorityTimer implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(PriorityTimer.class);

    private final Thread timerThread = new Thread(this::run, "priorityTimerThread");
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final DelayQueue<Task> queue = new DelayQueue<>();

    public PriorityTimer() {
        timerThread.start();
    }

    private void run() {
        while (running.get()) {
            try {
                Task element = queue.poll(500, TimeUnit.MILLISECONDS);
                if (element != null) {
                    element.runnable.run();
                }
            } catch (Exception e) {
                LOG.warn("Unexpected exception in priority timer", e);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        running.set(false);
        timerThread.join();
    }

    public InterruptibleTask createTask(long millis) {
        return new InterruptibleTask() {
            private volatile Task task;
            private volatile long remaining = millis;

            @Override
            public void pause() {
                if (queue.remove(task)) {
                    remaining = task.getDelay(TimeUnit.MILLISECONDS);
                }
            }

            @Override
            public void startOrResume() {
                if (remaining >= 0) {
                    task = new Task(this::finish, remaining);
                    queue.add(task);
                    remaining = -1;
                }
            }
        };
    }

    private static class Task implements Delayed {
        private final Runnable runnable;
        private final long waitUntil;

        public Task(Runnable runnable, long delayMillis) {
            this.runnable = runnable;
            this.waitUntil = System.currentTimeMillis() + delayMillis;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(waitUntil - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(waitUntil, ((Task) o).waitUntil);
        }
    }
}

