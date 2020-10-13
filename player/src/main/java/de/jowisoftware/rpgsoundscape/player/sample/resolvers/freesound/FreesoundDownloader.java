package de.jowisoftware.rpgsoundscape.player.sample.resolvers.freesound;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jowisoftware.rpgsoundscape.player.config.PersistableState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FreesoundDownloader {
    private static final Logger LOG = LoggerFactory.getLogger(FreesoundDownloader.class);

    private final PersistableState state;
    private final FreesoundSettings settings;
    private final ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private volatile String accessToken;
    private volatile String refreshToken;
    private volatile LocalDateTime expires;

    public FreesoundDownloader(PersistableState state, FreesoundSettings settings, ObjectMapper objectMapper) {
        this.state = state;
        this.settings = settings;
        this.objectMapper = objectMapper;

        readCredentials();
    }

    private void readCredentials() {
        accessToken = state.get("freesound.accessToken");
        refreshToken = state.get("freesound.refreshToken");
        String expires = state.get("freesound.expiresIn");

        if (expires != null) {
            this.expires = LocalDateTime.ofEpochSecond(Long.parseLong(expires), 0, ZoneOffset.UTC);
        }
    }

    private void updateCredentials(AuthResponse authResponse) {
        this.accessToken = authResponse.accessToken();
        this.refreshToken = authResponse.refreshToken();
        this.expires = LocalDateTime.now().plusSeconds(authResponse.expires_in() - 30);

        state.set("freesound.accessToken", accessToken);
        state.set("freesound.refreshToken", refreshToken);
        state.set("freesound.expiresIn", Long.toString(expires.toEpochSecond(ZoneOffset.UTC)));

        LOG.info("Updated credentials");
    }

    private void resetCredentials() {
        this.accessToken = null;
        this.refreshToken = null;
        this.expires = LocalDateTime.now().minusSeconds(1);

        state.set("freesound.accessToken", accessToken);
        state.set("freesound.refreshToken", refreshToken);
        state.set("freesound.expiresIn", Long.toString(expires.toEpochSecond(ZoneOffset.UTC)));

        LOG.info("Deleted credentials");
    }

    public <T> T download(Builder request, BodyHandler<T> bodyHandler) throws Exception {
        if (!refresh()) {
            throw new RuntimeException("Could not refresh the token");
        }

        HttpRequest finishedRequest = request.header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        LOG.info("Starting download for {}", finishedRequest.uri());
        return httpClient.send(finishedRequest, bodyHandler)
                .body();
    }

    public boolean needsLogin() {
        return accessToken == null
                || refreshToken == null
                || this.expires == null
                || LocalDateTime.now().isBefore(this.expires);
    }

    public synchronized boolean login(String code) {
        try {
            var request = HttpRequest.newBuilder(URI.create("https://freesound.org/apiv2/oauth2/access_token/"))
                    .POST(asFormBody(Map.of(
                            "client_id", settings.getClientId(),
                            "client_secret", settings.getSecret(),
                            "grant_type", "authorization_code",
                            "code", code
                    )))
                    .header("content-type", "application/x-www-form-urlencoded")
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                LOG.error("Unexpected response: {}\n{}", response.statusCode(), response.body());
                return false;
            }

            AuthResponse authResponse = objectMapper.readValue(response.body(), AuthResponse.class);
            updateCredentials(authResponse);
            return true;
        } catch (InterruptedException | IOException e) {
            LOG.error("Unable to receive login token", e);
            resetCredentials();
            return false;
        }
    }

    public synchronized boolean refresh() {
        if (refreshToken == null || this.expires == null) {
            return false;
        }

        if (LocalDateTime.now().isBefore(this.expires)) {
            return true;
        }

        var request = HttpRequest.newBuilder(URI.create("https://freesound.org/apiv2/oauth2/access_token/"))
                .POST(asFormBody(Map.of(
                        "client_id", settings.getClientId(),
                        "client_secret", settings.getSecret(),
                        "grant_type", "refresh_token",
                        "refresh_token", refreshToken
                )))
                .header("content-type", "application/x-www-form-urlencoded")
                .build();

        try {
            var response = httpClient.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                LOG.error("Unexpected response: {}\n{}", response.statusCode(), response.body());
                resetCredentials();
                return false;
            }

            AuthResponse authResponse = objectMapper.readValue(response.body(), AuthResponse.class);
            updateCredentials(authResponse);
            return true;
        } catch (IOException | InterruptedException e) {
            LOG.error("Unable to refresh token", e);
            resetCredentials();
            return false;
        }
    }

    private BodyPublisher asFormBody(Map<String, String> postBody) {
        return BodyPublishers.ofString(
                postBody.entrySet().stream()
                        .map(kv ->
                                URLEncoder.encode(kv.getKey(), StandardCharsets.UTF_8)
                                        + "="
                                        + URLEncoder.encode(kv.getValue(), StandardCharsets.UTF_8))
                        .collect(Collectors.joining("&")));
    }

    public static record AuthResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("refresh_token") String refreshToken,
            @JsonProperty("expires_in") int expires_in) {
    }
}
