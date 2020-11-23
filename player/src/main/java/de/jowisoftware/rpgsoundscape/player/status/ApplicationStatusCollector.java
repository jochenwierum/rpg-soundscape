package de.jowisoftware.rpgsoundscape.player.status;

import de.jowisoftware.rpgsoundscape.exceptions.SyntaxException;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.status.event.MusicChangedEvent;
import de.jowisoftware.rpgsoundscape.player.status.event.Problem;
import de.jowisoftware.rpgsoundscape.player.status.event.ResolvedStatus;
import de.jowisoftware.rpgsoundscape.player.status.event.SoundscapeChangeEvent;
import de.jowisoftware.rpgsoundscape.player.status.event.UpdateLibraryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static de.jowisoftware.rpgsoundscape.player.exception.ExceptionUtils.firstCauseOfType;

@Component
public class ApplicationStatusCollector implements StatusReporter, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStatusCollector.class);

    private final Thread readingThread = new Thread(this::run, "applicationStatusControllerThread");
    private final BlockingQueue<Consumer<ApplicationStatusListener>> queue = new LinkedBlockingQueue<>();
    private static final Consumer<ApplicationStatusListener> quitThreadSignal = e -> {
    };

    private final boolean debugParser;

    private final List<ApplicationStatusListener> listeners = new CopyOnWriteArrayList<>();
    private final List<Problem> problems = new CopyOnWriteArrayList<>();

    private final Deduplicator<SoundscapeChangeEvent> soundscapeChangeEventDeduplicator =
            new Deduplicator<>(queue, ApplicationStatusListener::reportSoundscapeChanged);
    private final Deduplicator<MusicChangedEvent> musicChangeEventDeduplicator =
            new Deduplicator<>(queue, ApplicationStatusListener::reportMusicChanged);
    private final Deduplicator<UpdateLibraryEvent> updateLibraryDeduplicator =
            new Deduplicator<>(queue, ApplicationStatusListener::updateLibrary);
    private final Deduplicator<ResolvedStatus> resolvedStatusDeduplicator =
            new Deduplicator<>(queue, ApplicationStatusListener::reportResolvedStatus);

    public ApplicationStatusCollector(ApplicationSettings applicationSettings) {
        this.debugParser = applicationSettings.isDebugParser();
        readingThread.setDaemon(true);
        readingThread.start();
    }

    @Override
    public void destroy() throws Exception {
        queue.add(quitThreadSignal);
        readingThread.join();
    }

    private void run() {
        while (true) {
            try {
                Consumer<ApplicationStatusListener> toProcess = queue.poll(5, TimeUnit.SECONDS);
                if (toProcess == quitThreadSignal) {
                    break;
                }

                if (toProcess == null) {
                    toProcess = ApplicationStatusListener::ping;
                }

                listeners.forEach(toProcess);
            } catch (InterruptedException ignore) {
            } catch (Exception e) {
                LOG.warn("Unhandled exception in collector thread", e);
            }
        }
    }

    public void registerListener(ApplicationStatusListener listener) {
        listeners.add(listener);

        listener.reportProblemCount(problems.size());
        updateLibraryDeduplicator.getLast().ifPresent(listener::updateLibrary);

        listener.reportSoundscapeChanged(
                soundscapeChangeEventDeduplicator.getLast()
                        .orElseGet(() -> new SoundscapeChangeEvent("", false, Set.of())));

        listener.reportMusicChanged(
                musicChangeEventDeduplicator.getLast()
                        .orElseGet(() -> new MusicChangedEvent("", false)));

        listener.reportResolvedStatus(
                resolvedStatusDeduplicator.getLast()
                        .orElseGet(() -> new ResolvedStatus(0, 0, 0)));
    }

    public void deregisterListener(ApplicationStatusListener listener) {
        listeners.remove(listener);
    }

    public List<Problem> getProblems() {
        return Collections.unmodifiableList(problems);
    }

    @Override
    public long logProblem(Problem problem) {
        String line = "=".repeat(80) + "\n";
        String line2 = "-".repeat(80) + "\n";

        StringBuilder msg = new StringBuilder(line)
                .append(problem.message()).append("\n");

        if (!problem.errors().isEmpty()) {
            msg.append(line2);
            for (String m : problem.errors()) {
                msg.append("* ").append(m).append("\n");
            }
        }

        if (problem.source() != null) {
            msg.append(line2)
                    .append(problem.source()).append("\n");
        }
        msg.append(line);
        System.err.println(msg);

        if (problem.exception() != null) {
            LOG.error("Original exception:", problem.exception());

            if (debugParser) {
                firstCauseOfType(problem.exception(), SyntaxException.class)
                        .map(SyntaxException::formatTree)
                        .ifPresent(System.err::println);
            }
        }

        problems.add(problem);
        queue.add(listener -> listener.reportProblemCount(problems.size()));

        return problem.id();
    }

    @Override
    public void removeProblem(long problemId) {
        problems.removeIf(p -> p.id() == problemId);
        queue.add(listener -> listener.reportProblemCount(problems.size()));
    }

    @Override
    public void clearProblems() {
        problems.clear();
        queue.add(listener -> listener.reportProblemCount(0));
    }

    @Override
    public void updateLibrary(UpdateLibraryEvent event) {
        updateLibraryDeduplicator.accept(event);
    }

    @Override
    public void reportSoundscapeChanged(SoundscapeChangeEvent event) {
        LOG.debug("Soundscape changed: " + event.toString());
        soundscapeChangeEventDeduplicator.accept(event);
    }

    @Override
    public void reportMusicChanged(MusicChangedEvent event) {
        LOG.debug("Music changed: " + event.toString());
        musicChangeEventDeduplicator.accept(event);
    }

    @Override
    public void reportResolved(ResolvedStatus event) {
        LOG.debug("Resolved status changed: " + event.toString());
        resolvedStatusDeduplicator.accept(event);
    }
}
