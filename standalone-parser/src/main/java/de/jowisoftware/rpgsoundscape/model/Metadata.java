package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.SMetadataStatement;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record Metadata(
        String description,
        Map<String, Set<String>> categories) {
    public static Metadata from(Context context, List<SMetadataStatement> metadataStatements) {
        String description = null;
        Map<String, Set<String>> categories = new HashMap<>();

        Set<String> sourceSet = new HashSet<>();
        sourceSet.add("file:" + context.fileName());
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

        return new Metadata(description, Collections.unmodifiableMap(categories));
    }
}