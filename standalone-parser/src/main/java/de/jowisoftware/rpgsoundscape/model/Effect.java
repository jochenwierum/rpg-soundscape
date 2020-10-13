package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.exceptions.ErrorPosition;
import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.intellij.psi.SMusicEffectDefinition;

public record Effect(
        String name,
        Play play,
        Metadata metadata)
        implements MetadataAware {

    static Effect from(SMusicEffectDefinition definition, Context context) {
        String name = Util.parse(definition.getString());
        String id = definition.getId().getText();

        Sample sample = context.sample(id);
        if (sample == null) {
            throw new SemanticException(definition, "Sample '%s' is referenced but does not exist".formatted(id));
        }

        Metadata metadata = Metadata.from(context, definition.getMetadataStatementList());
        Play play = new Play(sample, Modification.from(definition.getPlayModifications(), false), new ErrorPosition(definition));
        return new Effect(name, play, metadata);
    }
}
