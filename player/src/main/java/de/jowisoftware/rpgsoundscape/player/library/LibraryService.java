package de.jowisoftware.rpgsoundscape.player.library;

import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.exceptions.SyntaxException;
import de.jowisoftware.rpgsoundscape.language.parser.ParserFacade;
import de.jowisoftware.rpgsoundscape.model.Sample;
import de.jowisoftware.rpgsoundscape.model.SoundscapeFileContent;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.status.StatusReporter;
import de.jowisoftware.rpgsoundscape.player.status.event.Problem;
import de.jowisoftware.rpgsoundscape.player.status.event.UpdateLibraryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LibraryService {
    private static final Logger LOG = LoggerFactory.getLogger(LibraryService.class);

    private final SoundscapeLibrary soundscapeLibrary;
    private final EffectLibrary effectLibrary;
    private final MusicLibrary musicLibrary;

    private final SampleRepository sampleRepository;

    private final ApplicationContext applicationContext;
    private final ApplicationSettings applicationSettings;

    private final StatusReporter statusReporter;

    public LibraryService(
            SoundscapeLibrary soundscapeLibrary,
            EffectLibrary effectLibrary,
            MusicLibrary musicLibrary,
            SampleRepository sampleRepository,
            ApplicationContext applicationContext,
            ApplicationSettings applicationSettings,
            StatusReporter statusReporter) {
        this.soundscapeLibrary = soundscapeLibrary;
        this.effectLibrary = effectLibrary;
        this.musicLibrary = musicLibrary;
        this.sampleRepository = sampleRepository;
        this.applicationContext = applicationContext;
        this.applicationSettings = applicationSettings;
        this.statusReporter = statusReporter;
    }

    public void refreshLibrary() {
        statusReporter.clearProblems();

        try {
            Set<Sample> samples = importLibrary();
            sampleRepository.updateSamples(samples);

            statusReporter.updateLibrary(new UpdateLibraryEvent());
            applicationContext.publishEvent(new LibraryUpdatedEvent(this));
        } catch (Exception e) {
            LOG.warn("Unable to update library", e);
        }
    }

    private Set<Sample> importLibrary() throws IOException {
        soundscapeLibrary.reset();
        musicLibrary.reset();
        effectLibrary.reset();

        return Files.list(applicationSettings.getLibraryPath().toAbsolutePath())
                .filter(Files::isRegularFile)
                .filter(this::shouldLoad)
                .sorted()
                .flatMap(this::importFile)
                .collect(Collectors.toSet());
    }

    private boolean shouldLoad(Path f) {
        String filename = f.getFileName().toString();
        return !filename.startsWith("_") && filename.endsWith(".soundscape");
    }

    private Stream<Sample> importFile(Path file) {
        SoundscapeFileContent loaded = loadFile(file);
        if (loaded == null) {
            return null;
        }

        return importContent(loaded);
    }

    private SoundscapeFileContent loadFile(Path file) {
        try {
            LOG.info("Loading file '{}'", file);
            return ParserFacade.parse(file);
        } catch (IOException | SyntaxException | SemanticException e) {
            statusReporter.logProblem(Problem.create(
                    new RuntimeException("Cannot process file '%s'".formatted(file.toAbsolutePath().toString()), e)));
            return null;
        }
    }

    private Stream<Sample> importContent(SoundscapeFileContent loaded) {
        return Stream.of(
                importContent(loaded.soundscapes(), soundscapeLibrary::add),
                importContent(loaded.music(), musicLibrary::add),
                importContent(loaded.effects(), effectLibrary::add))
                .reduce(Stream.empty(), Stream::concat);
    }

    private <T> Stream<Sample> importContent(Map<String, T> content, BiFunction<String, T, Set<Sample>> importFunction) {
        return content.entrySet().stream()
                .flatMap(entry -> importFunction.apply(entry.getKey(), entry.getValue()).stream());
    }

}
