package de.jowisoftware.rpgsoundscape.player.sample;

import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.status.StatusReporter;
import de.jowisoftware.rpgsoundscape.player.status.event.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class SampleCache {
    private static final Logger LOG = LoggerFactory.getLogger(SampleCache.class);

    private final StatusReporter statusReporter;

    private final Path cachePath;
    private final Set<String> seen = ConcurrentHashMap.newKeySet();
    private long unusedWarningId;

    public SampleCache(StatusReporter statusReporter, ApplicationSettings applicationSettings) throws Exception {
        this.statusReporter = statusReporter;
        this.cachePath = applicationSettings.getCache().calculatePath();

        if (!Files.exists(cachePath)) {
            Files.createDirectories(cachePath);
        }
    }

    public Optional<ResolvedSample> tryResolve(URI uri) {
        Filenames files = filenames(uri);

        LOG.info("Resolving '{}' (cached as '{}')", uri, files.cacheFile());
        if (Files.exists(files.cacheFile())) {
            LOG.debug("Cached entry for '{}' (cached as '{}') exists!", uri, files.cacheFile());
            return Optional.of(new ResolvedSample(files.cacheFile(), readFile(files.attributionFile())));
        }

        return Optional.empty();
    }

    public ResolvedSample addToCache(URI uri, InputStream is, String attribution) {
        Filenames files = filenames(uri);

        LOG.info("Saving cache entry for '{}' (as '{}')", uri, files.cacheFile());
        try (OutputStream os = Files.newOutputStream(files.cacheFile())) {
            FileCopyUtils.copy(is, os);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write cache file", deleteAfterError(e, files.cacheFile()));
        }

        if (attribution != null) {
            try {
                Files.writeString(files.attributionFile(), attribution);
            } catch (IOException e) {
                throw new RuntimeException("Unable to write attribution file", deleteAfterError(e, files.attributionFile()));
            }
        }

        return new ResolvedSample(files.cacheFile(), Optional.ofNullable(attribution));
    }

    private Exception deleteAfterError(Exception e, Path file) {
        try {
            Files.deleteIfExists(file);
            return e;
        } catch (IOException e2) {
            e2.addSuppressed(e);
            return e2;
        }
    }

    private Optional<String> readFile(Path path) {
        if (!Files.exists(path)) {
            return Optional.empty();
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            return Optional.of(FileCopyUtils.copyToString(br));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static String hash(URI uri) {
        return uri.toString().replaceAll("[^a-zA-Z0-9-_]+", "-");
    }

    public Path resolveConverted(Path file, URI uri, Converter converter) {
        Filenames files = filenames(uri);

        String hash = fileContentHash(file);
        LOG.info("Resolving converted '{}' (cached as '{}' for hash '{}')",
                uri, files.convertedFile(), hash);

        Optional<Path> convertedFile = getConvertedEntry(uri, hash, files);
        if (convertedFile.isPresent()) {
            return convertedFile.orElseThrow();
        }

        LOG.info("Converted entry not in cache or hash does not match, resolving file directly");
        try (InputStream is = new BufferedInputStream(Files.newInputStream(file));
             OutputStream os = new BufferedOutputStream(Files.newOutputStream(files.convertedFile()))) {
            converter.convert(is, os);
            Files.writeString(files.hashFile(), hash);
        } catch (Exception e) {
            e = deleteAfterError(e, files.convertedFile());
            e = deleteAfterError(e, files.hashFile);
            throw new RuntimeException("unable to write converted cache file", e);
        }

        return files.convertedFile();
    }

    public Optional<Path> getConvertedEntry(Path file, URI uri) {
        return getConvertedEntry(uri, fileContentHash(file), filenames(uri));
    }

    private Optional<Path> getConvertedEntry(URI uri, String hash, Filenames files) {
        Path file = files.convertedFile();

        if (Files.exists(file)) {
            boolean hashMatches = readFile(files.hashFile())
                    .map(h -> h.equals(hash))
                    .orElse(false);

            if (hashMatches) {
                LOG.debug("Converted entry '{}' (cached as '{}') found", uri, file);
                return Optional.of(file);
            }
        }

        return Optional.empty();
    }

    private static String fileContentHash(Path file) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        try (InputStream is = Files.newInputStream(file)) {
            byte[] buffer = new byte[1024];
            int numRead;
            do {
                if ((numRead = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : digest.digest()) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }

    private Filenames filenames(URI uri) {
        String filename = hash(uri);
        Path cacheFile = cachePath.resolve(filename + ".cache");
        Path attributionFile = cachePath.resolve(filename + ".attribution");
        Path convertedFile = cachePath.resolve(filename + ".converted");
        Path hashFile = cachePath.resolve(filename + ".convertedhash");
        seen.add(filename);

        return new Filenames(cacheFile, attributionFile, convertedFile, hashFile);
    }

    public void reportUnused() {
        List<Path> unused = collectUnused();
        if (unused.isEmpty()) {
            if (unusedWarningId != 0) {
                statusReporter.removeProblem(unusedWarningId);
                unusedWarningId = 0;
            }

            LOG.info("No unused files found in cache!");
            return;
        }

        String listString = unused.stream()
                .map(path -> "    * " + path)
                .collect(Collectors.joining("\n", "Found unused but cached files:\n", ""));

        unusedWarningId = statusReporter.logProblem(Problem.create(new UnusedCacheItemsException(listString)));
    }

    public List<String> deleteUnused() {
        List<Path> unused = collectUnused();
        if (unused.isEmpty()) {
            LOG.info("No unused files found in cache!");
            return Collections.emptyList();
        }

        List<String> deleted = unused.stream()
                .map(path -> {
                    LOG.info("deleting unused cache file: " + path);
                    try {
                        Files.delete(path);
                        return path.toString();
                    } catch (IOException e) {
                        LOG.warn("Could not delete cache file " + path, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        reportUnused();
        return deleted;
    }

    private List<Path> collectUnused() {
        try {
            return Files.list(cachePath)
                    .map(Path::toAbsolutePath)
                    .filter(path -> !path.getFileName().toString().startsWith("state"))
                    .filter(path -> {
                        String s = path.getFileName().toString();
                        s = s.substring(0, s.lastIndexOf('.'));
                        return !seen.contains(s);
                    })
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public void markAsSeen(URI uri) {
        seen.add(hash(uri));
    }

    public void resetSeen() {
        seen.clear();
        unusedWarningId = 0;
    }

    @FunctionalInterface
    public interface Converter {
        void convert(InputStream source, OutputStream target) throws Exception;
    }

    private static record Filenames(
            Path cacheFile,
            Path attributionFile,
            Path convertedFile,
            Path hashFile) {
    }
}

