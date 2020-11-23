package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.STrackDefinition;

import java.util.Comparator;
import java.util.Set;

public record Track(
        String name,
        String title,
        boolean autoStart,
        boolean looping,
        boolean hidden,
        Statement statement) implements Comparable<Track> {

    @Override
    public int compareTo(Track o) {
        if (looping && !o.looping) {
            return 1;
        } else if (!looping && o.looping) {
            return -1;
        } else {
            return Comparator.comparing(Track::title).thenComparing(Track::name).compare(this, o);
        }
    }

    public static Track from(STrackDefinition definition, Context context) {
        String id = definition.getTrackId().getText();
        return new Track(
                id,
                definition.getString() == null ? id : definition.getString().parsed(),
                isAutoStart(definition),
                isLooping(definition),
                isHidden(definition),
                findBlock(definition, context)
        );
    }

    private static boolean isLooping(STrackDefinition definition) {
        return definition.getLoopingTrackModifier() != null;
    }

    private static boolean isHidden(STrackDefinition definition) {
        return definition.getLoopingTrackModifier() != null
                && definition.getLoopingTrackModifier().getHiddenModifier() != null;
    }

    private static boolean isAutoStart(STrackDefinition definition) {
        return (definition.getManualTrackModifier() != null
                && definition.getManualTrackModifier().getAutostartingModifier() != null)
                || (definition.getLoopingTrackModifier() != null
                && definition.getLoopingTrackModifier().getPausedModifier() == null);
    }

    private static Statement findBlock(STrackDefinition definition, Context context) {
        Statement statement;

        statement = Block.from(definition.getBlock(), context);
        if (!statement.isValid()) {
            throw new SemanticException(definition.getBlock(),
                    "Every path in the track must contain either a sleep or a play command");
        }

        return statement;
    }

    void collectSamples(Set<Sample> samples) {
        statement.collectSamples(samples);
    }
}
