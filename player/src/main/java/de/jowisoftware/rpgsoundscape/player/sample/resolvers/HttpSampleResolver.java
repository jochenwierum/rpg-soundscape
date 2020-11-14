package de.jowisoftware.rpgsoundscape.player.sample.resolvers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

@Component
public class HttpSampleResolver extends AbstractCachingResolver {
    private static final Logger LOG = LoggerFactory.getLogger(HttpSampleResolver.class);

    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(2))
            .version(Version.HTTP_2)
            .build();

    @Override
    public boolean supportsScheme(String scheme) {
        return scheme.equals("http") || scheme.equals("https");
    }

    @Override
    protected void receiveSample(URI uri, ResolverCallback resolverCallback) {
        resolverCallback.planTask(() -> {
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .GET()
                    .timeout(Duration.ofSeconds(8))
                    .build();
            try {
                LOG.info("Downloading: {}", uri);
                try (InputStream responseBody = client.send(request, BodyHandlers.ofInputStream()).body()) {
                    resolverCallback.resolve(sampleCache.addToCache(uri, responseBody, null));
                }
            } catch (IOException | InterruptedException e) {
                resolverCallback.reject(new RuntimeException("Unable to download " + uri.toString(), e));
            }
        });
    }

    @Override
    public String getAttributionPreamble(String type) {
        return "This " + type + " uses the following resources from the web:";
    }
}
