package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.intellij.psi.SSoundscapeDefinition;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record Soundscape(
        String name,
        String source,
        Map<String, Track> tracks,
        Metadata metadata) implements MetadataAware {

    static Soundscape from(SSoundscapeDefinition soundscape, Context context) {
        context = processSamples(soundscape, context);
        context = processIncludableTracks(soundscape, context);
        context = processTracksNames(soundscape, context);
        Map<String, Track> tracks = processTracks(soundscape, context);
        Metadata metadata = Metadata.from(context, soundscape.getSoundscapeBlock().getMetadataStatementList());

        return new Soundscape(Util.parse(soundscape.getString()),
                context.fileName(),
                Collections.unmodifiableMap(tracks),
                metadata);
    }

    private static Map<String, Track> processTracks(SSoundscapeDefinition soundscape, Context context) {
        Map<String, Track> tracks = new HashMap<>();

        soundscape.getSoundscapeBlock().getTrackDefinitionList()
                .forEach(Util.collectChecked(tracks,
                        td -> td.getId().getText(),
                        td -> Track.from(td, context)));
        return tracks;
    }

    private static Context processIncludableTracks(SSoundscapeDefinition soundscape, Context context) {
        Map<String, Statement> includableTracks = new HashMap<>();
        soundscape.getSoundscapeBlock().getIncludableTrackDefinitionList()
                .forEach(Util.collectChecked(includableTracks,
                        td -> td.getId().getText(),
                        td -> Block.from(td.getBlock(), context)));
        return context.withAdditionalIncludableTracks(includableTracks);
    }

    private static Context processSamples(SSoundscapeDefinition soundscape, Context context) {
        Map<String, Sample> samples = new HashMap<>();
        soundscape.getSoundscapeBlock().getLoadDefinitionList()
                .forEach(Util.collectChecked(samples, sd -> sd.getId().getText(), Sample::from));
        return context.withAdditionalSamples(samples);
    }

    private static Context processTracksNames(SSoundscapeDefinition soundscape, Context context) {
        Set<String> names = soundscape.getSoundscapeBlock().getTrackDefinitionList().stream()
                .map(td -> td.getId().getText())
                .collect(Collectors.toSet());

        return context.withTrackNames(names);
    }

    public Set<Sample> collectSamples() {
        Set<Sample> sources = new HashSet<>();
        tracks.values().forEach(t -> t.collectSamples(sources));
        return Collections.unmodifiableSet(sources);
    }
}
