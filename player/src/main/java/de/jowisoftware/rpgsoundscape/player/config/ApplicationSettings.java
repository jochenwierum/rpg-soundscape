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

    public static class Audio {
        private String mixer;

        public String getMixer() {
            return mixer;
        }

        public void setMixer(String mixer) {
            this.mixer = mixer;
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
}
