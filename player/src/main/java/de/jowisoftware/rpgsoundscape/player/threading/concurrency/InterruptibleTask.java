package de.jowisoftware.rpgsoundscape.player.threading.concurrency;

public interface InterruptibleTask {

    void run(boolean resume);

    void pause();

    void startOrResume();

    default void abort() {
        pause();
    }
}
