package de.jowisoftware.rpgsoundscape.player.audio.backend.discord;

import java.util.function.Supplier;
import java.util.stream.IntStream;

public class BlockingBuffer<T> {
    private final Object lock = new Object();
    private final Object[] queue;

    private volatile int readingPosition = -1;
    private volatile int writingPosition = 0;
    private volatile boolean closed = false;

    public BlockingBuffer(int capacity, Supplier<T> entrySupplier) {
        queue = IntStream.range(0, capacity)
                .mapToObj(__ -> entrySupplier.get())
                .toArray(Object[]::new);
    }

    public T nextWrite() {
        synchronized (lock) {
            int nextPosition = (this.writingPosition + 1) % queue.length;
            waitForOther(() -> readingPosition == nextPosition);

            this.writingPosition = nextPosition;
            //logN("write");
            lock.notifyAll();
        }

        if (closed) {
            return null;
        }

        @SuppressWarnings("unchecked")
        T result = (T) queue[writingPosition];
        return result;
    }

    public T nextRead() {
        synchronized (lock) {
            readingPosition = (readingPosition + 1) % queue.length;
            lock.notifyAll();

            waitForOther(() -> writingPosition == readingPosition);
        }

        if (closed) {
            return null;
        }

        @SuppressWarnings("unchecked")
        T result = (T) queue[readingPosition];
        return result;
    }

    private void waitForOther(Supplier<Boolean> condition) {
        while (condition.get() && !closed) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public boolean isAvailable() {
        return !(writingPosition == readingPosition);
    }

    public void close() {
        closed = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void waitUntilConsumed() throws InterruptedException {
        while (readingPosition != writingPosition) {
            synchronized (lock) {
                if (readingPosition != writingPosition) {
                    lock.wait();
                }
            }
        }
    }

    public void clear() {
        synchronized (lock) {
            writingPosition = (readingPosition + 1) % queue.length;

            lock.notifyAll();
        }
    }
}
