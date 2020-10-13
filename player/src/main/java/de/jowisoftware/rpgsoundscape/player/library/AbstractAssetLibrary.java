package de.jowisoftware.rpgsoundscape.player.library;

import de.jowisoftware.rpgsoundscape.model.MetadataAware;
import de.jowisoftware.rpgsoundscape.model.Sample;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractAssetLibrary<M extends MetadataAware> {
    protected final Map<String, M> content = new ConcurrentHashMap<>();

    public Optional<M> get(String name) {
        M result = content.get(name);
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    protected void addEntry(String name, M entry) {
        String key = name;
        int i = 0;
        while (content.containsKey(key)) {
            key = name + " (" + ++i + ")";
        }

        content.put(key, entry);
    }

    public SortedSet<String> listAll() {
        return new TreeSet<>(content.keySet());
    }

    public SortedSet<String> categories() {
        return content.values().stream()
                .flatMap(element -> element.metadata().categories().keySet().stream())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public SortedSet<String> categoryValues(String category) {
        return content.values().stream()
                .map(element -> element.metadata().categories().get(category))
                .flatMap(values -> values == null ? null : values.stream())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public SortedSet<String> categoryEntries(String category, String value) {
        return content.entrySet().stream()
                .filter(kv -> {
                    Set<String> values = kv.getValue().metadata().categories().get(category);
                    return values != null && values.contains(value);
                })
                .map(Entry::getKey)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public abstract Set<Sample> add(String name, M content);
}
