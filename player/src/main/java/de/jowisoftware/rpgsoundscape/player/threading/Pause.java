package de.jowisoftware.rpgsoundscape.player.threading;

public class Pause {
    private volatile boolean paused;

    public Pause(boolean paused) {
        this.paused = paused;
    }

    public synchronized boolean pause() {
        var old = paused;
        paused = true;
        return !old;
    }

    public synchronized boolean resume() {
        var old = paused;
        paused = false;
        this.notify();
        return old;
    }

    public synchronized void awaitToPass() {
        if (!paused) {
            return;
        }

        while (paused) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public boolean isPaused() {
        return paused;
    }
}
