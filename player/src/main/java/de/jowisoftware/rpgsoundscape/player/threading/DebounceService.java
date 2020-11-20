package de.jowisoftware.rpgsoundscape.player.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class DebounceService implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(DebounceService.class);
    private static final long MAX_AGE_MILLIS = 30_000;

    private volatile boolean running = true;

    @Override
    public void destroy() {
        running = false;
    }

    public Runnable createDebouncer(
            String name, Runnable target, int delayIntervalMillis, int maxIntervalMillis) {
        return new Debouncer(name, target, delayIntervalMillis, maxIntervalMillis);
    }

    private class Debouncer implements Runnable {
        private final String name;
        private final Runnable target;
        private final long delayIntervalMillis;
        private final long maxDelayMillis;

        private volatile int update = 0;
        private volatile Thread thread;
        private final Object threadLock = new Object();

        public Debouncer(String name, Runnable target, long delayIntervalMillis, long maxDelayMillis) {
            this.name = name;
            this.target = target;
            this.delayIntervalMillis = delayIntervalMillis;
            this.maxDelayMillis = maxDelayMillis;
        }

        @Override
        public void run() {
            synchronized (threadLock) {
                if (thread == null) {
                    update = 0;
                    thread = createThread();
                } else {
                    ++update;
                    threadLock.notify();
                }
            }
        }

        private Thread createThread() {
            LOG.trace("Creating new debouncer thread {}", name);
            var t = new Thread(() -> {
                int lastValue = update;
                int lastHandled = update - 1;

                long start = System.currentTimeMillis();

                while (running) {
                    synchronized (threadLock) {
                        try {
                            threadLock.wait(delayIntervalMillis);
                        } catch (InterruptedException e) {
                            return;
                        }

                        int updateValue = update;
                        boolean alreadyHandled = lastHandled == updateValue;
                        long statusAge = System.currentTimeMillis() - start;

                        if (start == -1) {
                            LOG.trace("Debouncer restarted (update: {}, age: {}ms)", updateValue, statusAge);
                            start = System.currentTimeMillis();
                        } else if (alreadyHandled && statusAge > MAX_AGE_MILLIS) {
                            LOG.trace("Debouncer now exits (update: {}, age: {}ms)", updateValue, statusAge);
                            thread = null;
                            return;
                        } else if (!alreadyHandled && (updateValue == lastValue || statusAge >= maxDelayMillis)) {
                            LOG.trace("Debouncer now triggers (update: {}, age: {}ms)", updateValue, statusAge);
                            lastHandled = updateValue;
                            start = -1;

                            try {
                                target.run();
                            } catch (RuntimeException e) {
                                LOG.error("Unhandled exception in debouncer", e);
                            }
                        } else {
                            LOG.trace("Debouncer is waiting (update: {}, age: {}ms)", updateValue, statusAge);
                        }

                        lastValue = updateValue;
                    }
                }
            });

            t.setName(name);
            t.setDaemon(true);
            t.start();
            return t;
        }
    }
}
