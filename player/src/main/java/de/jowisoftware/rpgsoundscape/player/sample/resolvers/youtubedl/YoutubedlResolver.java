package de.jowisoftware.rpgsoundscape.player.sample.resolvers.youtubedl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jowisoftware.rpgsoundscape.player.sample.SampleCache;
import de.jowisoftware.rpgsoundscape.player.sample.resolvers.AbstractCachingResolver;
import de.jowisoftware.rpgsoundscape.player.sample.resolvers.SampleResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Component
public class YoutubedlResolver
        extends AbstractCachingResolver
        implements SampleResolver, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(YoutubedlResolver.class);

    public static final String URI_PREFIX = "youtubedl+";

    private final YoutubedlSettings youtubedlSettings;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService;
    private final SynchronousQueue<Runnable> workQueue;

    private volatile long problemId;
    private volatile Boolean executableFound;

    public YoutubedlResolver(YoutubedlSettings youtubedlSettings, ObjectMapper objectMapper) {
        this.youtubedlSettings = youtubedlSettings;
        this.objectMapper = objectMapper;

        this.workQueue = new SynchronousQueue<>();
        this.executorService = new ThreadPoolExecutor(0, 1, 30,
                TimeUnit.SECONDS, workQueue);
    }

    @Override
    public void destroy() {
        executorService.shutdownNow();
    }

    @Override
    public boolean supportsScheme(String scheme) {
        return scheme.toLowerCase().startsWith(URI_PREFIX);
    }

    @Override
    public String formatAttributionUri(URI uri) {
        return uri.toString().substring(URI_PREFIX.length());
    }

    @Override
    public void abortAll() {
        problemId = 0;
        executableFound = null;
        workQueue.clear();
    }

    @Override
    protected void receiveSample(URI uri, ResolverCallback resolverCallback) {
        if (!programExists()) {
            if (problemId == 0) {
                problemId = resolverCallback.reject(new IllegalStateException(
                        "youtube-dl is not found on the system. Please specify 'application.youtubedl.path' in the configuration"));
            }
            return;
        }

        executorService.submit(() -> download(uri, resolverCallback));
    }

    private void download(URI uri, ResolverCallback resolverCallback) {
        Path mp3File = null;
        Path infoFile = null;

        try {
            Path tmpDir = Path.of(System.getProperty("java.io.tmpdir"));
            String basename = SampleCache.hash(uri);
            mp3File = tmpDir.resolve(basename + ".mp3");
            infoFile = tmpDir.resolve(basename + ".info.json");

            String realUri = uri.toString().substring(URI_PREFIX.length());
            Path basePath = tmpDir.resolve(basename);
            List<String> command = createDownloadCommand(basePath, realUri);

            LOG.info("Starting to download using external youtube-dl: {}", String.join(" ", command));

            int result = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .redirectOutput(Redirect.INHERIT)
                    .start()
                    .waitFor();
            LOG.info("youtube-dl finished with status {} for '{}'", result, uri);

            if (result == 0) {
                try (InputStream is = Files.newInputStream(mp3File)) {
                    resolverCallback.resolve(sampleCache.addToCache(uri, is, createAttribution(infoFile)));
                }
            } else {
                resolverCallback.reject(new RuntimeException("Unable to download video, exit status: " + result));
            }
        } catch (Exception e) {
            resolverCallback.reject(new RuntimeException("Unable to download video " + uri.toString(), e));
        } finally {
            deleteAfterDownload(mp3File, infoFile);
        }
    }

    private String createAttribution(Path infoFile) {
        try {
            if (!Files.exists(infoFile)) {
                return null;
            }

            Map<String, Object> data = objectMapper.readValue(infoFile.toFile(), new TypeReference<>() {
            });

            StringBuilder b = new StringBuilder();

            Optional.ofNullable(data.get("license"))
                    .ifPresent(l -> b.append("licensed as ").append(l));

            Stream.of("creator", "uploader_id", "uploader")
                    .map(data::get)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .ifPresent(value -> {
                                if (!b.isEmpty()) {
                                    b.append(' ');
                                }
                                b.append("by ").append(value);
                            }
                    );

            if (b.length() > 0) {
                return b.toString();
            } else {
                return null;
            }
        } catch (IOException e) {
            LOG.warn("Could not read info file", e);
            return null;
        }
    }

    private void deleteAfterDownload(Path... files) {
        for (Path file : files) {
            try {
                if (file != null) {
                    Files.deleteIfExists(file);
                }
            } catch (IOException e) {
                LOG.warn("Could not delete temp file '{}'", file, e);
            }
        }
    }

    private List<String> createDownloadCommand(Path file, String realUri) {
        List<String> args = new ArrayList<>();
        args.add(youtubedlSettings.getPath());
        youtubedlSettings.getArguments().stream()
                .map(arg -> arg
                        .replaceAll("\\$url", realUri)
                        .replaceAll("\\$file", file.toAbsolutePath().toString())
                )
                .forEach(args::add);
        return args;
    }

    private boolean programExists() {
        try {
            int exitValue = new ProcessBuilder(youtubedlSettings.getPath(), "--version")
                    .redirectError(Redirect.DISCARD)
                    .redirectOutput(Redirect.DISCARD)
                    .start()
                    .waitFor();

            executableFound = exitValue == 0;
        } catch (IOException | InterruptedException e) {
            executableFound = false;
        }

        return executableFound;
    }

    @Override
    public String getAttributionPreamble(String type) {
        return "This " + type + " uses the video resources from the web:";
    }
}
