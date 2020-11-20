package de.jowisoftware.rpgsoundscape.player.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@ConfigurationProperties(prefix = "application")
@PropertySource(value = "file:config.properties", ignoreResourceNotFound = true, encoding = "UTF-8")
public class ApplicationSettings {
    private Audio audio = new Audio();
    private Cache cache = new Cache();
    private Ui ui = new Ui();

    private Path libraryPath = null;
    private boolean debugParser = false;

    public Path getLibraryPath() {
        if (libraryPath == null) {
            return Path.of("./library").normalize();
        }
        return libraryPath.toAbsolutePath().normalize();
    }

    public void setLibraryPath(Path libraryPath) {
        this.libraryPath = libraryPath;
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

    public static class Audio {
        private String mixer;
        private Backend backend = Backend.JAVA_CLIP;

        public String getMixer() {
            return mixer;
        }

        public void setMixer(String mixer) {
            this.mixer = mixer;
        }

        public Backend getBackend() {
            return backend;
        }

        public void setBackend(Backend backend) {
            this.backend = backend;
        }

        @SuppressWarnings("unused")
        public enum Backend {
            JAVA_CLIP,
            JAVA_STREAM,
            FFMPEG_STREAM
        }
    }

    public static class Cache {
        private Path path;
        private boolean preCacheConversion = true;
        private float cacheMaxSampleRate = 0;
        private long maxFileSize = 0;

        public Path getPath() {
            if (path == null) {
                return Path.of("./.cache").normalize();
            }
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
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

    public enum ExitType {
        DISABLED,
        QUIT,
        EXIT_COMMAND
    }
}
