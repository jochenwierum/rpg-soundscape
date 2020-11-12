package de.jowisoftware.rpgsoundscape.player.status;

import java.util.Optional;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Deduplicator<T> implements Consumer<T> {
    private final Queue<Consumer<ApplicationStatusListener>> queue;
    private final BiConsumer<ApplicationStatusListener, T> operation;
    private volatile T last = null;

    public Deduplicator(Queue<Consumer<ApplicationStatusListener>> queue, BiConsumer<ApplicationStatusListener, T> operation) {
        this.queue = queue;
        this.operation = operation;
    }

    public void accept(T element) {
        synchronized (this) {
            if (last == null || !last.equals(element)) {
                last = element;
            } else {
                return;
            }
        }

        queue.add(listener -> operation.accept(listener, element));
    }

    public Optional<T> getLast() {
        return Optional.ofNullable(last);
    }
}
