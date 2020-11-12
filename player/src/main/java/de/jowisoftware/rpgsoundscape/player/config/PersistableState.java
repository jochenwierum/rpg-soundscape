package de.jowisoftware.rpgsoundscape.player.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PersistableState implements DisposableBean, InitializingBean {
    private final Path statefile;
    private final ObjectMapper objectMapper;

    private final Map<String, String> data = new ConcurrentHashMap<>();

    public PersistableState(ApplicationSettings applicationSettings, ObjectMapper objectMapper) {
        statefile = applicationSettings.getCache().getPath().resolve("state.json").toAbsolutePath();
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!Files.exists(statefile.getParent())) {
            Files.createDirectories(statefile.getParent());
        }
        if (!Files.isReadable(statefile)) {
            return;
        }

        this.data.putAll(objectMapper.readValue(statefile.toFile(), new TypeReference<Map<String, String>>() {
        }));
    }

    @Override
    public void destroy() throws Exception {
        objectMapper.writeValue(statefile.toFile(), data);
    }

    public void set(String name, String value) {
        data.put(name, value);
    }

    public String get(String name) {
        return data.get(name);
    }
}
