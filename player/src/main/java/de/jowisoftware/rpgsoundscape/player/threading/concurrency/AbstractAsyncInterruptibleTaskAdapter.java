package de.jowisoftware.rpgsoundscape.player.threading.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractAsyncInterruptibleTaskAdapter implements InterruptibleTask {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractAsyncInterruptibleTaskAdapter.class);

    private Runnable finished;

    @Override
    public void run(boolean resume) {
        var latch = new CountDownLatch(1);
        onFinish(latch::countDown);

        if (resume) {
            LOG.trace("Context starts interruptible task");
            startOrResume();
        } else {
            LOG.trace("Context is paused - don't start interruptible task");
        }

        try {
            LOG.trace("Context enters sleep");
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOG.trace("Sleep finished");
    }

    protected final void finish() {
        finished.run();
    }

    public final void onFinish(Runnable finished) {
        this.finished = finished;
    }

    public void abort() {
        pause();
        finish();
    }
}
