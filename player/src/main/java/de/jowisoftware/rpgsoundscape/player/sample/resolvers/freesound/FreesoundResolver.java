package de.jowisoftware.rpgsoundscape.player.sample.resolvers.freesound;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jowisoftware.rpgsoundscape.player.sample.ResolvedSample;
import de.jowisoftware.rpgsoundscape.player.sample.resolvers.AbstractCachingResolver;
import de.jowisoftware.rpgsoundscape.player.status.StatusReporter;
import de.jowisoftware.rpgsoundscape.player.status.event.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class FreesoundResolver extends AbstractCachingResolver {
    private static final int ATTRIBUTION_URI_PREFIX_LENGTH = "freesound:///".length();

    private record AgendaItem(
            URI uri,
            String id,
            ResolverCallback resolverCallback) {
    }

    private static final Logger LOG = LoggerFactory.getLogger(FreesoundResolver.class);

    private static final Pattern CC_LICENSE_PATTERN = Pattern.compile("https?://creativecommons\\.org/licenses/([^/]+)/([\\d.]+)/?");
    private static final Pattern PD_LICENSE_PATTERN = Pattern.compile("https?://creativecommons\\.org/publicdomain/zero/([^/]+)/([\\d.]+)/?");

    private final StatusReporter statusReporter;
    private final ObjectMapper objectMapper;
    private final FreesoundSettings settings;

    private final FreesoundDownloader downloader;

    private final List<AgendaItem> agenda = Collections.synchronizedList(new ArrayList<>());
    private volatile long problemId;

    public FreesoundResolver(FreesoundSettings settings,
            StatusReporter statusReporter, FreesoundDownloader freeSoundsDownloader,
            ObjectMapper objectMapper) {
        this.settings = settings;
        this.statusReporter = statusReporter;
        this.downloader = freeSoundsDownloader;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsScheme(String scheme) {
        return scheme.equals("freesound");
    }

    @Override
    protected void receiveSample(URI uri, ResolverCallback resolverCallback) {
        if (uri.getPath().length() <= 1 || !uri.getPath().startsWith("/")) {
            resolverCallback.reject(new IllegalArgumentException("Invalid URI: " + uri));
            return;
        }

        String id = uri.getPath().substring(1);
        if (settings.getClientId() == null || settings.getSecret() == null) {
            LOG.error("A freesound api key is required. application.freesound.client-id or application.freesound.secret is not set!");
            statusReporter.logProblem(Problem.create(new IllegalStateException(
                    "application.freesound.client-id or application.freesound.secret is not set")));
            return;
        }

        synchronized (downloader) {
            if (downloader.needsLogin()) {
                if (problemId == 0) {
                    String loginUri = "https://freesound.org/apiv2/oauth2/authorize/?response_type=code&client_id=%s"
                            .formatted(URLEncoder.encode(settings.getClientId(), StandardCharsets.UTF_8));
                    this.problemId = statusReporter.logProblem(Problem.create(
                            new LoginException("A login for freesound is required", loginUri)));
                }
                LOG.debug("Login is not yet available, queuing download of '{}'", uri);
                agenda.add(new AgendaItem(uri, id, resolverCallback));
            } else {
                LOG.debug("Login is available, starting download of '{}' immediately", uri);
                download(uri, id, resolverCallback);
            }
        }
    }

    private void download(URI uri, String id, ResolverCallback resolverCallback) {
        LOG.info("Planing download of {}", uri);
        resolverCallback.planTask(() -> {
            if (!downloader.refresh()) {
                LOG.info("Not ready to download {}, adding url to queue", uri);
                agenda.add(new AgendaItem(uri, id, resolverCallback));
                return;
            }

            LOG.info("Downloading metadata for {}", uri);
            downloadInfo(id, resolverCallback).ifPresent(data -> {
                LOG.info("Downloading sound file for {}", uri);
                downloadFile(id, uri, resolverCallback, data);
            });
        });
    }

    private Optional<Map<String, String>> downloadInfo(String id, ResolverCallback resolverCallback) {
        URI uri = URI.create("https://freesound.org/apiv2/sounds/%s/?fields=name,license,download,username".formatted(id));
        try {
            Map<String, String> data = objectMapper.readValue(
                    downloader.download(HttpRequest.newBuilder(uri), BodyHandlers.ofString()),
                    new TypeReference<>() {
                    });
            return Optional.of(data);
        } catch (Exception e) {
            LOG.error("Unable to download sample '{}'", id, e);
            resolverCallback.reject(new RuntimeException("Unable to download sample '%s' info from '%s'".formatted(id, uri), e));
            return Optional.empty();
        }
    }

    private void downloadFile(String id, URI uri, ResolverCallback resolverCallback, Map<String, String> data) {
        String name = data.getOrDefault("name", "(unknown)");
        String username = data.getOrDefault("username", "(unknown)");
        String license = mapLicensese(data.get("license"));

        String attribution = "%s by %s (http://freesound.org/people/%s/), licensed as %s"
                .formatted(name, username, username, license);

        URI downloadUri = URI.create(data.get("download"));
        try (InputStream is = downloader.download(HttpRequest.newBuilder(downloadUri), BodyHandlers.ofInputStream())) {
            ResolvedSample cached = sampleCache.addToCache(uri, is, attribution);
            LOG.info("Download of sample '{}' from '{}' finished", uri, downloadUri);
            resolverCallback.resolve(cached);
        } catch (Exception e) {
            resolverCallback.reject(new RuntimeException("Unable to download sample '%s' audio from '%s'".formatted(id, downloadUri), e));
        }
    }

    private String mapLicensese(String licenseUrl) {
        if (licenseUrl == null) {
            return "";
        }

        Matcher matcher = CC_LICENSE_PATTERN.matcher(licenseUrl);
        if (matcher.matches()) {
            return "licensed as Creative Commons: CC %s(v %s)".formatted(
                    matcher.group(1).toUpperCase(), matcher.group(2));
        }

        if (PD_LICENSE_PATTERN.matcher(licenseUrl).matches()) {
            return "licensed as Public Domain";
        }

        return licenseUrl;
    }

    public void retryDownloads() {
        statusReporter.removeProblem(problemId);
        problemId = 0;

        var todo = new ArrayList<>(agenda);
        agenda.clear();
        todo.forEach(a -> download(a.uri(), a.id(), a.resolverCallback()));
    }

    @Override
    public void abortAll() {
        agenda.clear();
    }

    @Override
    public String getAttributionPreamble(String type) {
        return "This " + type + " uses these sounds from freesound:";
    }

    @Override
    public String formatAttributionUri(URI uri) {
        return "freesound sample #" + uri.toString().substring(ATTRIBUTION_URI_PREFIX_LENGTH);
    }
}
