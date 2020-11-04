package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.exceptions.ErrorPosition;
import de.jowisoftware.rpgsoundscape.exceptions.SyntaxException;
import de.jowisoftware.rpgsoundscape.language.psi.SLoadDefinition;
import de.jowisoftware.rpgsoundscape.model.Modification.AttributionModification;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

public record Sample(
        String name,
        URI uri,
        List<Modification> modifications,
        ErrorPosition position) {

    static Sample from(SLoadDefinition sampleDefinition) {
        String sampleName = sampleDefinition.getSampleId().getText();

        String uriString = sampleDefinition.getString().parsed();
        URI uri;
        try {
            uri = new URI(uriString).normalize();
        } catch (URISyntaxException e) {
            throw new SyntaxException(sampleDefinition, "Invalid URI: '" + uriString + "'");
        }

        List<Modification> modifications = Modification.from(sampleDefinition.getPlayModifications(), true);
        return new Sample(sampleName, uri, modifications, new ErrorPosition(sampleDefinition));
    }

    public Optional<String> getAttribution() {
        return modifications.stream()
                .filter(AttributionModification.class::isInstance)
                .findAny()
                .map(AttributionModification.class::cast)
                .map(AttributionModification::attribution);
    }
}
