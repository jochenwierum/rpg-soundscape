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

        List<Modification> modifications = Modification.from(sampleDefinition.getSampleModificationList());
        return new Sample(sampleName, uri, modifications, new ErrorPosition(sampleDefinition));
    }

    public <T extends Modification> Optional<T> getModification(Class<T> modificationClass) {
        return modifications.stream()
                .flatMap(Util.filterCast(modificationClass))
                .findAny();
    }
}
