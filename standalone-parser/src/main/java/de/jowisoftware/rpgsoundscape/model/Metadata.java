package de.jowisoftware.rpgsoundscape.model;

import com.intellij.psi.PsiElement;
import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.SMetadataStatement;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public record Metadata(
        String description,
        Map<String, Set<String>> categories) {
    public static Metadata from(PsiElement parent, List<SMetadataStatement> metadataStatements) {
        String description = null;
        SortedMap<String, SortedSet<String>> categories = new TreeMap<>();

        SortedSet<String> sourceSet = new TreeSet<>();
        sourceSet.add("file:" + Util.file(parent).getName());
        categories.put("source", sourceSet);

        for (SMetadataStatement statement : metadataStatements) {
            if (statement.getDescribedStatement() != null) {
                if (description != null) {
                    throw new SemanticException(statement, "A description was already provided");
                }
                description = statement.getDescribedStatement().getString().parsed();
            } else if (statement.getCategoryStatement() != null) {
                categories.computeIfAbsent(
                        statement.getCategoryStatement().getCategorizedIn().getString().parsed(),
                        __ -> new TreeSet<>())
                        .add(statement.getCategoryStatement().getCategorizedAs().getString().parsed());
            }
        }

        return new Metadata(description, copyAsReadOnlyValues(categories));
    }

    private static Map<String, Set<String>> copyAsReadOnlyValues(Map<String, ? extends Set<String>> categories) {
        return Collections.unmodifiableMap(
                categories.entrySet().stream()
                        .map(e -> Map.entry(e.getKey(), Collections.unmodifiableSet(e.getValue())))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (a, b) -> {
                                    throw new IllegalStateException("duplicate key");
                                },
                                TreeMap::new)));
    }

    public Metadata merge(Metadata include) {
        String mergedDescription = description != null ? description : include.description();
        var mergedCategories = new TreeMap<>(categories.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), (Set<String>) new TreeSet<>(e.getValue())))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue)));

        include.categories().forEach(
                (k, v) -> mergedCategories.merge(k, v, (m1, m2) -> {
                    var newSet = new TreeSet<>(m1);
                    newSet.addAll(m2);
                    return newSet;
                }));

        return new Metadata(mergedDescription, Collections.unmodifiableMap(mergedCategories));
    }
}
