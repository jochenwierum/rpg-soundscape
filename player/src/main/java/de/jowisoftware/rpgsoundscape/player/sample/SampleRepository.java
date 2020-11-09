package de.jowisoftware.rpgsoundscape.player.sample;

import de.jowisoftware.rpgsoundscape.exceptions.ErrorPosition;
import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.model.Sample;
import de.jowisoftware.rpgsoundscape.player.audio.AudioConverter;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.exception.ResolvingException;
import de.jowisoftware.rpgsoundscape.player.sample.resolvers.SampleResolver;
import de.jowisoftware.rpgsoundscape.player.sample.resolvers.SampleResolver.ResolverCallback;
import de.jowisoftware.rpgsoundscape.player.status.StatusReporter;
import de.jowisoftware.rpgsoundscape.player.status.event.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class SampleRepository implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(SampleRepository.class);

    private static final Supplier<InputStream> ERROR_SUPPLIER =
            () -> SampleRepository.class.getResourceAsStream("/error.wav");

    private static final UriLookupResult ERROR_ENTRY =
            new UriLookupResult(SampleStatus.ERROR, false, null, ERROR_SUPPLIER);

    private final StatusReporter statusReporter;
    private final List<SampleResolver> resolvers;
    private final AudioConverter audioConverter;
    private final SampleCache sampleCache;
    private final ApplicationSettings applicationSettings;

    private final ExecutorService executorService;

    private final Map<URI, UriLookupResult> sources = new ConcurrentHashMap<>();
    private volatile boolean completed;

    public SampleRepository(
            StatusReporter statusReporter,
            List<SampleResolver> resolvers,
            SampleCache sampleCache,
            AudioConverter audioConverter,
            ApplicationSettings applicationSettings) {
        this.statusReporter = statusReporter;
        this.resolvers = resolvers;
        this.audioConverter = audioConverter;
        this.sampleCache = sampleCache;
        this.applicationSettings = applicationSettings;

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

        Map<URI, ErrorPosition> allUris = samples.stream()
                .map(s -> Map.entry(s.uri(), s.position()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (first, second) -> first));

        Set<URI> removeSamples = new HashSet<>(sources.keySet());
        removeSamples.removeAll(allUris.keySet());
        sources.keySet().removeAll(removeSamples);

        this.completed = false;
        allUris.forEach(this::resolve);
    }

    private void resolve(URI uri, ErrorPosition errorPosition) {
        if (sources.containsKey(uri)) {
            sampleCache.markAsSeen(uri);
            return;
        }

        sources.put(uri, new UriLookupResult(SampleStatus.RESOLVING, false, null, ERROR_SUPPLIER));

        LOG.info("Resolving audio file '{}'...", uri);

        ResolverCallback resolverCallback = new Callback(uri, errorPosition);
        try {
            resolvers.stream()
                    .filter(r -> r.supportsScheme(uri.getScheme()))
                    .findFirst()
                    .orElseThrow(() -> new ResolvingException("Source type '%s' is not supported", null, errorPosition))
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
            boolean isPreconverted,
            String attribution,
            Supplier<InputStream> inputStreamSupplier) {

        public UriLookupResult asError() {
            return ERROR_ENTRY;
        }

        public UriLookupResult asResolved(Path path, boolean isPreConverted, String attribution) {
            return new UriLookupResult(SampleStatus.RESOLVED, isPreConverted, attribution, () -> {
                try {
                    return new BufferedInputStream(Files.newInputStream(path));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    private void updateStatus(URI uri, Function<UriLookupResult, UriLookupResult> update) {
        sources.compute(uri, (k, v) -> v == null ? null : update.apply(v));

        if (!completed) {
            boolean allResolved = sources.values().stream().allMatch(r -> r.sampleStatus() != SampleStatus.RESOLVING);

            if (allResolved) {
                LOG.info("All files are resolved now.");
                completed = true;
                sampleCache.reportUnused();
            }
        }
    }

    private class Callback implements SampleResolver.ResolverCallback {
        private final URI uri;
        private final ErrorPosition errorPosition;

        public Callback(URI uri, ErrorPosition errorPosition) {
            this.uri = uri;
            this.errorPosition = errorPosition;
        }

        @Override
        public void planTask(Runnable r) {
            executorService.submit(r);
        }

        @Override
        public void reject(Exception e) {
            updateStatus(uri, UriLookupResult::asError);
            statusReporter.logProblem(Problem.create(new ResolvingException(
                    "Unable to resolve sample - marking file as broken", e, errorPosition)));
        }

        @Override
        public void resolve(ResolvedSample present) {
            Path file = present.path();
            String attribution = present.attribution().orElse(null);

            LOG.debug("Resolved audio file '{}' as file '{}'", uri, file);

            try {
                if (!audioConverter.requiresConversion(file)) {
                    LOG.debug("Resolved audio file '{}' does not require conversion", uri);
                    updateStatus(uri, v -> v.asResolved(file, false, attribution));
                } else if (applicationSettings.getCache().isPreCacheConversion()) {
                    planTask(() -> preCacheConversion(file, attribution));
                } else {
                    sampleCache.getConvertedEntry(file, uri).ifPresentOrElse(
                            oldConverted -> updateStatus(uri, v ->
                                    v.asResolved(oldConverted, true, attribution)),
                            () -> updateStatus(uri,
                                    v -> v.asResolved(file, false, attribution))
                    );
                }
            } catch (Exception e) {
                reject(e);
            }
        }

        private void preCacheConversion(Path file, String attribution) {
            try {
                Path result = sampleCache.resolveConverted(file, uri,
                        (i, o) -> audioConverter.convert(i, o, applicationSettings.getCache().getCacheMaxSampleRate()));
                updateStatus(uri, v -> v.asResolved(result, true, attribution));
            } catch (Exception e) {
                reject(e);
            }
        }
    }
}