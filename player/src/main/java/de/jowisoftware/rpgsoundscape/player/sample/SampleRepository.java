package de.jowisoftware.rpgsoundscape.player.sample;

import de.jowisoftware.rpgsoundscape.exceptions.ErrorPosition;
import de.jowisoftware.rpgsoundscape.model.Modification.NoConversionLoadModification;
import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.model.Sample;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.AudioConverter;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.exception.ResolvingException;
import de.jowisoftware.rpgsoundscape.player.sample.resolvers.SampleResolver;
import de.jowisoftware.rpgsoundscape.player.sample.resolvers.SampleResolver.ResolverCallback;
import de.jowisoftware.rpgsoundscape.player.status.StatusReporter;
import de.jowisoftware.rpgsoundscape.player.status.event.Problem;
import de.jowisoftware.rpgsoundscape.player.status.event.ResolvedStatus;
import de.jowisoftware.rpgsoundscape.player.threading.DebounceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SampleRepository implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(SampleRepository.class);

    private static final UriLookupResult ERROR_ENTRY =
            new UriLookupResult(SampleStatus.ERROR, null, null);

    private final StatusReporter statusReporter;
    private final List<SampleResolver> resolvers;
    private final AudioConverter audioConverter;
    private final SampleCache sampleCache;
    private final ApplicationSettings applicationSettings;

    private final ExecutorService executorService;

    private final Map<URI, UriLookupResult> sources = new ConcurrentHashMap<>();
    private final Runnable sendStatus;
    private volatile boolean completed;
    private volatile boolean importing;

    public SampleRepository(
            StatusReporter statusReporter,
            DebounceService debounceService,
            List<SampleResolver> resolvers,
            SampleCache sampleCache,
            ApplicationSettings applicationSettings,
            AudioConverter audioConverter) {
        this.statusReporter = statusReporter;
        this.resolvers = resolvers;
        this.audioConverter = audioConverter;
        this.sampleCache = sampleCache;
        this.applicationSettings = applicationSettings;
        this.sendStatus = debounceService.createDebouncer("send-status",
                this::sendStatus, 250, 500);

        executorService = new ThreadPoolExecutor(0, 8, 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }

    @Override
    public void destroy() {
        resolvers.forEach(SampleResolver::abortAll);
        executorService.shutdownNow();
    }

    public LookupResult lookup(Sample sample) {
        return new LookupResult(sources.computeIfAbsent(sample.uri(), s -> {
            LOG.error("Lookup tried to load unresolved sample " + sample);
            return ERROR_ENTRY;
        }), sample);
    }

    public synchronized void updateSamples(Set<Sample> samples) {
        sampleCache.resetSeen();
        resolvers.forEach(SampleResolver::abortAll);

        record Tuple(
                ErrorPosition errorPosition,
                boolean allowCaching) {
            Tuple(Sample s) {
                this(s.position(), s.getModification(NoConversionLoadModification.class).isEmpty());
            }
        }

        Map<URI, Tuple> allUris = samples.stream()
                .map(s -> Map.entry(s.uri(), new Tuple(s)))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (first, second) ->
                        new Tuple(first.errorPosition, first.allowCaching || second.allowCaching)));

        Set<URI> removeSamples = new HashSet<>(sources.keySet());
        allUris.keySet().stream()
                .filter(uri -> {
                    UriLookupResult existing = sources.get(uri);
                    return existing != null && existing.sampleStatus() == SampleStatus.RESOLVED;
                })
                .forEach(removeSamples::remove);
        sources.keySet().removeAll(removeSamples);

        this.completed = false;
        this.importing = true;

        allUris.forEach((uri, tuple) -> this.resolve(uri, tuple.errorPosition(), tuple.allowCaching()));

        this.importing = false;
        maybeReportUnused();
        sendStatus.run();
    }

    private void resolve(URI uri, ErrorPosition errorPosition, boolean allowCaching) {
        if (sources.containsKey(uri)) {
            sampleCache.markAsSeen(uri);
            return;
        }

        sources.put(uri, new UriLookupResult(SampleStatus.RESOLVING, null, null));

        LOG.info("Resolving audio file '{}'...", uri);

        ResolverCallback resolverCallback = new Callback(uri, errorPosition, allowCaching);
        try {
            resolvers.stream()
                    .filter(r -> r.supportsScheme(uri.getScheme()))
                    .findFirst()
                    .orElseThrow(() -> new ResolvingException(
                            "Source type '%s' is not supported".formatted(uri.getScheme()), null, errorPosition))
                    .resolve(uri, resolverCallback);
        } catch (Exception e) {
            resolverCallback.reject(e);
        }
    }

    public void markAsBroken(Play play, Exception e) {
        LOG.error("Sample cannot be played - marking file as broken.\nPlay statement:\n{}\nSource declaration:\n{}",
                play.position().extract(), play.sample().position().extract(), e);
        updateStatus(play.sample().uri(), UriLookupResult::asError);
        statusReporter.logProblem(Problem.create(e, play.position()));
    }

    static record UriLookupResult(
            SampleStatus sampleStatus,
            String attribution,
            Path file) {
        public UriLookupResult asError() {
            return ERROR_ENTRY;
        }

        public UriLookupResult asResolved(Path path, String attribution) {
            return new UriLookupResult(SampleStatus.RESOLVED, attribution, path);
        }
    }

    private void updateStatus(URI uri, Function<UriLookupResult, UriLookupResult> update) {
        sources.compute(uri, (k, v) -> v == null ? null : update.apply(v));

        maybeReportUnused();
        sendStatus.run();
    }

    private void maybeReportUnused() {
        if (completed || importing) {
            return;
        }

        boolean allResolved = sources.values().stream().allMatch(r -> r.sampleStatus() != SampleStatus.RESOLVING);

        if (allResolved) {
            LOG.info("All files are resolved now.");
            completed = true;
            sampleCache.reportUnused();
        }
    }

    private void sendStatus() {
        Map<SampleStatus, Long> statsCount = sources.values().stream()
                .collect(Collectors.groupingBy(
                        UriLookupResult::sampleStatus,
                        Collectors.counting()));

        statusReporter.reportResolved(new ResolvedStatus(
                statsCount.getOrDefault(SampleStatus.RESOLVED, 0L),
                statsCount.getOrDefault(SampleStatus.RESOLVING, 0L),
                statsCount.getOrDefault(SampleStatus.ERROR, 0L)
        ));
    }

    private class Callback implements SampleResolver.ResolverCallback {
        private final URI uri;
        private final ErrorPosition errorPosition;
        private final boolean allowCaching;

        public Callback(URI uri, ErrorPosition errorPosition, boolean allowCaching) {
            this.uri = uri;
            this.errorPosition = errorPosition;
            this.allowCaching = allowCaching;
        }

        @Override
        public void planTask(Runnable r) {
            executorService.submit(r);
        }

        @Override
        public long reject(Exception e) {
            updateStatus(uri, UriLookupResult::asError);
            return statusReporter.logProblem(Problem.create(new ResolvingException(
                    "Unable to resolve sample - marking file as broken", e, errorPosition)));
        }

        @Override
        public void resolve(ResolvedSample resolvedSample) {
            Path file = resolvedSample.path();
            String attribution = resolvedSample.attribution().orElse(null);

            LOG.debug("Resolved audio file '{}' as file '{}'", uri, file);
            try {
                if (!allowCaching) {
                    LOG.debug("Caching is disabled for audio file '{}'", uri);
                    updateStatus(uri, v -> v.asResolved(file, attribution));
                } else {
                    offerToCache(file, attribution);
                }
            } catch (Exception e) {
                reject(e);
            }
        }

        private void offerToCache(Path file, String attribution) throws Exception {
            if (!audioConverter.requiresConversion(file)) {
                LOG.debug("Resolved audio file '{}' does not require conversion", uri);
                updateStatus(uri, v -> v.asResolved(file, attribution));
            } else if (shouldCache(file)) {
                planTask(() -> {
                    try {
                        Path result = sampleCache.resolveConverted(file, uri, audioConverter::convert);
                        updateStatus(uri, v -> v.asResolved(result, attribution));
                    } catch (Exception e) {
                        reject(e);
                    }
                });
            } else {
                sampleCache.getConvertedEntry(file, uri).ifPresentOrElse(
                        oldConverted -> updateStatus(uri, v ->
                                v.asResolved(oldConverted, attribution)),
                        () -> updateStatus(uri,
                                v -> v.asResolved(file, attribution))
                );
            }
        }

        private boolean shouldCache(Path file) {
            if (!applicationSettings.getCache().isPreCacheConversion()) {
                return false;
            }

            if (applicationSettings.getCache().getMaxFileSize() <= 0) {
                return true;
            }

            try {
                return applicationSettings.getCache().getMaxFileSize() >= Files.size(file);
            } catch (IOException e) {
                return false;
            }
        }
    }
}
