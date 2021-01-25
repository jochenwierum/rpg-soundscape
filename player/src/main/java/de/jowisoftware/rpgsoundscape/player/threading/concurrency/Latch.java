package de.jowisoftware.rpgsoundscape.player.threading.concurrency;

public class Latch {
    private volatile boolean closed;

    public Latch(boolean closed) {
        this.closed = closed;
    }

    public synchronized boolean close() {
        var old = closed;
        closed = true;
        return !old;
    }

    public synchronized boolean open() {
        var old = closed;
        closed = false;
        this.notify();
        return old;
    }

    public synchronized void waitForOpen() {
        if (!closed) {
            return;
        }

        while (closed) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public boolean isClosed() {
        return closed;
    }
}
