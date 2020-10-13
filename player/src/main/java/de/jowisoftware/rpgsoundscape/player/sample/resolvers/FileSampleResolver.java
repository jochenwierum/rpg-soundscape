package de.jowisoftware.rpgsoundscape.player.sample.resolvers;

import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.sample.ResolvedSample;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
public class FileSampleResolver implements SampleResolver {
    public static final int ATTRIBUTION_URI_PREFIX_LENGTH = "file:///".length();
    private final Path basePath;

    public FileSampleResolver(ApplicationSettings applicationSettings) {
        this.basePath = applicationSettings.getLibraryPath().toAbsolutePath().normalize();
    }

    @Override
    public boolean supportsScheme(String scheme) {
        return scheme.equals("file");
    }

    @Override
    public void resolve(URI uri, ResolverCallback resolverCallback) {
        Path file = basePath.resolve(Path.of(uri.getPath().substring(1))).toAbsolutePath();

        if (!file.normalize().toString().startsWith(basePath.normalize().toString())) {
            resolverCallback.reject(new IllegalArgumentException(
                    "Referenced file '%s' (resolved as '%s') escapes library path at"
                            .formatted(uri, file)));
        }

        if (!Files.exists(file)) {
            resolverCallback.reject(new IllegalArgumentException(
                    "Referenced file '%s' (expected as '%s') could not be found"
                            .formatted(uri, file)));
        }

        resolverCallback.resolve(new ResolvedSample(file, Optional.empty()));
    }

    @Override
    public String getAttributionPreamble(String type) {
        return "This " + type + " uses the following local files:";
    }

    @Override
    public String formatAttributionUri(URI uri) {
        return "file " + uri.toString().substring(ATTRIBUTION_URI_PREFIX_LENGTH);
    }
}
