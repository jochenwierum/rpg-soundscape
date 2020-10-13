package de.jowisoftware.rpgsoundscape.player.rest;

import de.jowisoftware.rpgsoundscape.player.status.ApplicationStatusCollector;
import de.jowisoftware.rpgsoundscape.player.status.ApplicationStatusListener;
import de.jowisoftware.rpgsoundscape.player.status.event.MusicChangedEvent;
import de.jowisoftware.rpgsoundscape.player.status.event.Problem;
import de.jowisoftware.rpgsoundscape.player.status.event.SoundscapeChangeEvent;
import de.jowisoftware.rpgsoundscape.player.status.event.UpdateLibraryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api")
public class StatusController {
    private static final Logger LOG = LoggerFactory.getLogger(StatusController.class);

    private final ApplicationStatusCollector applicationStatusCollector;

    public StatusController(ApplicationStatusCollector applicationStatusCollector) {
        this.applicationStatusCollector = applicationStatusCollector;
    }

    @GetMapping("/status")
    public ResponseBodyEmitter handleRequest() {
        final SseEmitter emitter = new SseEmitter(0L);

        ApplicationStatusListener listener = new SseForwardingListener(emitter);

        emitter.onCompletion(() -> applicationStatusCollector.deregisterListener(listener));
        emitter.onTimeout(() -> {
            LOG.warn("Timeout in SSE emitter");
            applicationStatusCollector.deregisterListener(listener);
        });
        emitter.onError((e) -> {
            if (!e.getMessage().contains("broken pipe")) {
                LOG.warn("Error in SSE emitter", e);
            }
            applicationStatusCollector.deregisterListener(listener);
        });
        applicationStatusCollector.registerListener(listener);

        return emitter;
    }

    @GetMapping("/status/problems")
    public List<ProblemDto> getProblems() {
        return applicationStatusCollector.getProblems()
                .stream()
                .map(ProblemDto::new)
                .collect(Collectors.toList());
    }

    private static class SseForwardingListener implements ApplicationStatusListener {
        private final SseEmitter emitter;

        public SseForwardingListener(SseEmitter emitter) {
            this.emitter = emitter;
        }

        @Override
        public void ping() {
            send("ping", "");
        }

        @Override
        public void updateLibrary(UpdateLibraryEvent e) {
            send("updateLibrary", "");
        }

        @Override
        public void reportProblemCount(int count) {
            send("problems", count);
        }

        @Override
        public void reportSoundscapeChanged(SoundscapeChangeEvent e) {
            send("soundscapeChanged", e);
        }

        @Override
        public void reportMusicChanged(MusicChangedEvent e) {
            send("musicChanged", e);
        }

        private void send(String name, Object data) {
            try {
                emitter.send(SseEmitter.event().name(name).data(data));
            } catch (Exception e) {
                LOG.info("Status client disconnected or broken");
                emitter.completeWithError(e);
            }
        }
    }

    private static record ProblemDto(
            long id,
            String message,
            List<String> errors,
            String source,
            String link) {

        public ProblemDto(Problem problem) {
            this(problem.id(), problem.message(), problem.errors(), problem.source(), problem.link());
        }
    }
}
