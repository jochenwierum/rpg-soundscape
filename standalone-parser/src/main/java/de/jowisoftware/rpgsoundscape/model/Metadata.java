package de.jowisoftware.rpgsoundscape.model;

import com.intellij.psi.PsiElement;
import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.SMetadataStatement;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record Metadata(
        String description,
        Map<String, Set<String>> categories) {
    public static Metadata from(PsiElement parent, List<SMetadataStatement> metadataStatements) {
        String description = null;
        Map<String, Set<String>> categories = new HashMap<>();

        Set<String> sourceSet = new HashSet<>();
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
                        __ -> new HashSet<>())
                        .add(statement.getCategoryStatement().getCategorizedAs().getString().parsed());
            }
        }

        return new Metadata(description, readOnlyValues(categories));
    }

    private static Map<String, Set<String>> readOnlyValues(Map<String, Set<String>> categories) {
        return categories.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), Collections.unmodifiableSet(e.getValue())))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Metadata merge(Metadata include) {
        String megedDescription = description != null ? description : include.description();
        var mergedCategories = new HashMap<>(categories.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), (Set<String>) new HashSet<>(e.getValue())))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue)));

        include.categories().forEach(
                (k, v) -> mergedCategories.merge(k, v, (m1, m2) -> {
                    var newSet = new HashSet<>(m1);
                    newSet.addAll(m2);
                    return newSet;
                }));

        return new Metadata(megedDescription, Collections.unmodifiableMap(mergedCategories));
    }
}
