package de.jowisoftware.rpgsoundscape.player.interpreter;

import de.jowisoftware.rpgsoundscape.player.threading.concurrency.InterruptibleTask;

public class SleepStatement implements InterruptibleTask {
    private long sleepMillis;
    private volatile boolean paused;

    public SleepStatement(long millis) {
        this.sleepMillis = millis;
    }

    @Override
    public void run(boolean resume) {
        paused = !resume;

        synchronized (this) {
            try {
                while (sleepMillis > 0) {
                    while (paused) {
                        this.wait();
                    }

                    if (sleepMillis > 0) {
                        long start = System.currentTimeMillis();
                        this.wait(sleepMillis);
                        sleepMillis -= System.currentTimeMillis() - start;
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Override
    public void pause() {
        synchronized (this) {
            paused = true;
            this.notify();
        }
    }

    @Override
    public void startOrResume() {
        synchronized (this) {
            paused = false;
            this.notify();
        }
    }

    @Override
    public void abort() {
        sleepMillis = 0;
        startOrResume();
    }
}
