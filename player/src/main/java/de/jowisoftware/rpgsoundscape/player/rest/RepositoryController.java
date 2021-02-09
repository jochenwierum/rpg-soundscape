package de.jowisoftware.rpgsoundscape.player.rest;

import de.jowisoftware.rpgsoundscape.player.library.AbstractAssetLibrary;
import de.jowisoftware.rpgsoundscape.player.library.EffectLibrary;
import de.jowisoftware.rpgsoundscape.player.library.MusicLibrary;
import de.jowisoftware.rpgsoundscape.player.library.SoundscapeLibrary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/{repository:soundscapes|music|effects}")
public class RepositoryController {
    private final Map<String, AbstractAssetLibrary<?>> repositories;
    static final int ITEMS_PER_PAGE = 50;

    public RepositoryController(SoundscapeLibrary soundscapeLibrary, MusicLibrary musicLibrary,
            EffectLibrary effectLibrary) {
        this.repositories = Map.of(
                "soundscapes", soundscapeLibrary,
                "music", musicLibrary,
                "effects", effectLibrary
        );
    }

    @GetMapping
    public Page listAll(@PathVariable("repository") String repository,
            @RequestParam(value = "q") Optional<String> filter,
            @RequestParam(value = "p") Optional<Integer> page
    ) {

        return toPage(repositories.get(repository).listAll(), filter, page);
    }

    @GetMapping("/categories")
    public SortedSet<String> categories(@PathVariable("repository") String repository) {
        return repositories.get(repository).categories();
    }

    @GetMapping("/category/{name}")
    public SortedSet<String> category(@PathVariable("repository") String repository, @PathVariable("name") String name) {
        return repositories.get(repository).categoryValues(name);
    }

    @GetMapping("/category/{name}/{value}")
    public Page categoryEntries(@PathVariable("repository") String repository,
            @PathVariable("name") String name, @PathVariable("value") String value,
            @RequestParam(value = "q") Optional<String> filter,
            @RequestParam(value = "p") Optional<Integer> page) {
        return toPage(repositories.get(repository).categoryEntries(name, value), filter, page);
    }

    private Page toPage(SortedSet<String> content, Optional<String> filter, Optional<Integer> page) {
        int skipItems = page.filter(p -> p >= 0).orElse(0) * ITEMS_PER_PAGE;
        Optional<String> lcFilter = filter.map(s -> s.toLowerCase(Locale.ROOT));

        Predicate<String> filterFunction = item ->
                lcFilter.map(filterValue -> item.toLowerCase().contains(filterValue))
                        .orElse(true);

        List<String> filtered = content.stream()
                .filter(filterFunction)
                .skip(skipItems)
                .limit(ITEMS_PER_PAGE)
                .collect(Collectors.toList());

        return new Page(filtered, (int) Math.ceil(1.0 * content.size() / ITEMS_PER_PAGE));
    }

    public static record Page(
            List<String> data,
            int pages) {
    }
}
