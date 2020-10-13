package de.jowisoftware.rpgsoundscape.player.threading;

public abstract class InterruptibleTask {
    private Runnable finished;

    abstract public void pause();

    abstract public void startOrResume();

    public void abort() {
        pause();
        finish();
    }

    protected final void finish() {
        finished.run();
    }

    public void onFinish(Runnable finished) {
        this.finished = finished;
    }
}
