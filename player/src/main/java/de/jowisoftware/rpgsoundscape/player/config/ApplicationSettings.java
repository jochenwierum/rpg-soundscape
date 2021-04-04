package de.jowisoftware.rpgsoundscape.player.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationSettings {
    private final Path basePath;
    private Audio audio = new Audio();
    private Cache cache;
    private Ui ui = new Ui();
    private Discord discord = new Discord();

    private String libraryPath = "library";
    private boolean debugParser = false;

    public ApplicationSettings() {
        basePath = Path.of(".").normalize();
        cache = new Cache(basePath);
    }

    public String getLibraryPath() {
        return libraryPath;
    }

    public void setLibraryPath(String libraryPath) {
        this.libraryPath = libraryPath;
    }

    public Path calculateLibraryPath() {
        return basePath.resolve(libraryPath).normalize().toAbsolutePath();
    }

    public boolean isDebugParser() {
        return debugParser;
    }

    public void setDebugParser(boolean debugParser) {
        this.debugParser = debugParser;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Ui getUi() {
        return ui;
    }

    public void setUi(Ui ui) {
        this.ui = ui;
    }

    public Discord getDiscord() {
        return discord;
    }

    public void setDiscord(Discord discord) {
        this.discord = discord;
    }

    public static class Audio {
        private String mixer;
        private Frontend frontend = Frontend.JAVA_CLIP;
        private Backend backend = Backend.JAVA_AUDIO;

        public String getMixer() {
            return mixer;
        }

        public void setMixer(String mixer) {
            this.mixer = mixer;
        }

        public Frontend getFrontend() {
            return frontend;
        }

        public void setFrontend(Frontend frontend) {
            this.frontend = frontend;
        }

        public Backend getBackend() {
            return backend;
        }

        public void setBackend(Backend backend) {
            this.backend = backend;
        }

        public enum Frontend {
            JAVA_CLIP,
            JAVA_STREAM,
            FFMPEG_STREAM
        }

        public enum Backend {
            JAVA_AUDIO,
            DISCORD
        }
    }

    public static class Cache {
        private final Path basePath;
        private String path = ".cache";
        private boolean preCacheConversion = true;
        private float cacheMaxSampleRate = 0;
        private long maxFileSize = 0;

        public Cache(Path basePath) {
            this.basePath = basePath.resolve(".cache");
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Path calculatePath() {
            return basePath.resolve(path).normalize().toAbsolutePath();
        }

        public boolean isPreCacheConversion() {
            return preCacheConversion;
        }

        public void setPreCacheConversion(boolean preCacheConversion) {
            this.preCacheConversion = preCacheConversion;
        }

        public float getCacheMaxSampleRate() {
            return cacheMaxSampleRate;
        }

        public void setCacheMaxSampleRate(float cacheMaxSampleRate) {
            this.cacheMaxSampleRate = cacheMaxSampleRate;
        }

        public long getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(long maxFileSize) {
            this.maxFileSize = maxFileSize;
        }
    }

    public static class Ui {
        private Logs logs = new Logs();

        private boolean allowFullscreen = true;
        private ExitType exitType = ExitType.QUIT;
        private String exitCommand = "";

        public boolean isAllowFullscreen() {
            return allowFullscreen;
        }

        public void setAllowFullscreen(boolean allowFullscreen) {
            this.allowFullscreen = allowFullscreen;
        }

        public ExitType getExitType() {
            return exitType;
        }

        public void setExitType(ExitType exitType) {
            this.exitType = exitType;
        }

        public String getExitCommand() {
            return exitCommand;
        }

        public void setExitCommand(String exitCommand) {
            this.exitCommand = exitCommand;
        }

        public Logs getLogs() {
            return logs;
        }

        public void setLogs(Logs logs) {
            this.logs = logs;
        }
    }

    public static class Logs {
        private boolean enabled = false;
        private int size = 2000;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    public static class Discord {
        private String token;
        private Set<String> allowedUsers = new HashSet<>();

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Set<String> getAllowedUsers() {
            return allowedUsers;
        }

        public void setAllowedUsers(Set<String> allowedUsers) {
            this.allowedUsers = allowedUsers;
        }
    }

    public enum ExitType {
        DISABLED,
        QUIT,
        EXIT_COMMAND
    }
}
