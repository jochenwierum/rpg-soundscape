package de.jowisoftware.rpgsoundscape.player.rest;

import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.status.logs.CachingAppender;
import de.jowisoftware.rpgsoundscape.player.status.logs.CachingAppender.LogEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.function.Consumer;

@RestController
public class LoggingController {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(LoggingController.class);

    private final CachingAppender appender;

    public LoggingController(ApplicationSettings settings) {
        this.appender = ((Logger) LogManager.getRootLogger()).getAppenders()
                .values().stream()
                .filter(appender -> appender instanceof CachingAppender)
                .map(CachingAppender.class::cast)
                .findAny()
                .orElseThrow();

        if (!settings.getUi().getLogs().isEnabled()) {
            appender.disable();
        } else {
            appender.setCacheSize(settings.getUi().getLogs().getSize());
        }
    }

    @GetMapping("/api/logs")
    public SseEmitter test() {
        final SseEmitter emitter = new SseEmitter(0L);

        Consumer<LogEntry> listener = new LogListener(emitter);

        emitter.onCompletion(() -> appender.deregisterListener(listener));

        emitter.onTimeout(() -> {
            LOG.warn("Timeout in logging SSE emitter");
            appender.deregisterListener(listener);
        });

        emitter.onError((e) -> {
            if (!e.getMessage().contains("broken pipe")) {
                LOG.warn("Error in logging SSE emitter", e);
            }
            appender.deregisterListener(listener);
        });

        appender.registerListener(listener);

        return emitter;
    }

    private class LogListener implements Consumer<LogEntry> {
        private final SseEmitter emitter;

        public LogListener(SseEmitter emitter) {
            this.emitter = emitter;
        }

        @Override
        public void accept(LogEntry logEntry) {
            try {
                emitter.send(SseEmitter.event().name("log").data(logEntry));
            } catch (Exception e) {
                LOG.info("Status client disconnected or broken");
                appender.deregisterListener(this);
            }
        }
    }
}
