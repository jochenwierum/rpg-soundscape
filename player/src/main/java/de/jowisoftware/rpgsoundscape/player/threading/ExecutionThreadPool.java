package de.jowisoftware.rpgsoundscape.player.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class ExecutionThreadPool implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(ExecutionThreadPool.class);
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final List<Runnable> shutdownHooks = Collections.synchronizedList(new ArrayList<>());

    public Future<Void> submitThread(Runnable r) {
        return threadPool.submit(() -> {
            try {
                r.run();
            } catch (RuntimeException e) {
                LOG.error("Unhandled exception in worker thread", e);
            }
        }, null);
    }

    public void onShutdown(Runnable runnable) {
        shutdownHooks.add(runnable);
    }

    @Override
    public void destroy() {
        shutdownHooks.forEach(r -> {
            try {
                r.run();
            } catch (RuntimeException e) {
                LOG.error("Unhandled exception in cleanup", e);
            }
        });
        threadPool.shutdownNow();
    }
}
