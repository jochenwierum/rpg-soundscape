package de.jowisoftware.rpgsoundscape.player.status.logs;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Plugin(name = "CachingAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class CachingAppender extends AbstractAppender {
    public static final LogEntry STOP_ENTRY = new LogEntry(0, null, null, null, null, null);
    private final Thread readingThread = new Thread(this::run, "applicationStatusControllerThread");

    private final Set<Consumer<LogEntry>> listeners = ConcurrentHashMap.newKeySet();
    private final BlockingQueue<LogEntry> queue = new LinkedBlockingQueue<>();
    private final List<LogEntry> events = Collections.synchronizedList(new LinkedList<>());

    private volatile boolean enabled = true;
    private long index = 0;
    private volatile int limit = 2000;

    protected CachingAppender(String name, Filter filter) {
        super(name, filter, null, true, new Property[0]);
    }

    @PluginFactory
    public static CachingAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter) {
        return new CachingAppender(name, filter);
    }

    @Override
    public void start() {
        super.start();
        readingThread.setDaemon(true);
        readingThread.start();
    }

    @Override
    public void stop() {
        disable();
        super.stop();
    }

    @Override
    public void append(LogEvent event) {
        if (!enabled || !event.getLevel().isLessSpecificThan(Level.INFO)) {
            return;
        }

        LogEntry entry = new LogEntry(++index,
                ZonedDateTime.ofInstant(Instant.ofEpochMilli(event.getTimeMillis()), ZoneId.systemDefault()),
                event.getLoggerName(),
                event.getLevel().toString(),
                event.getThreadName(),
                event.getMessage().getFormattedMessage());

        events.add(entry);
        shrink();

        queue.add(entry);
    }

    public void setCacheSize(int limit) {
        this.limit = limit;
        shrink();
    }

    public void disable() {
        this.enabled = false;
        queue.add(STOP_ENTRY);
        try {
            readingThread.join();
        } catch (InterruptedException ignored) {
        }
        events.clear();
    }

    private void shrink() {
        while (events.size() > limit) {
            events.remove(0);
        }
    }

    public List<LogEntry> lines() {
        return new ArrayList<>(events);
    }

    public void registerListener(Consumer<LogEntry> listener) {
        events.forEach(listener);
        listeners.add(listener);
    }

    public void deregisterListener(Consumer<LogEntry> listener) {
        listeners.remove(listener);
    }

    public static record LogEntry(
            long id,
            ZonedDateTime timestamp,
            String logger,
            String priority,
            String thread,
            String message) {
    }

    private void run() {
        while (true) {
            try {
                LogEntry toProcess = queue.take();
                if (toProcess == STOP_ENTRY) {
                    break;
                }

                listeners.forEach(c -> c.accept(toProcess));
            } catch (InterruptedException ignore) {
            }
        }
    }
}
