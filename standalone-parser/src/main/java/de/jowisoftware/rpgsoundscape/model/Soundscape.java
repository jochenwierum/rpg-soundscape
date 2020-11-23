package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludeSoundscapeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SSoundscapeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SString;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static de.jowisoftware.rpgsoundscape.model.Util.collectChecked;
import static de.jowisoftware.rpgsoundscape.model.Util.collectIncludableTracks;

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
        Metadata metadata = Metadata.from(soundscape, soundscape.getMetadataStatementList());

        //GO ON HERE: PROCESS INCLUDES

        SString name = soundscape.getString();

        Soundscape result = new Soundscape(name != null ? name.parsed() : "",
                Util.file(soundscape).getName(),
                Collections.unmodifiableMap(tracks),
                metadata);

        List<SIncludeSoundscapeDefinition> includeSoundscapeDefinitionList = soundscape.getIncludeSoundscapeDefinitionList();
        for (int i = includeSoundscapeDefinitionList.size() - 1; i >= 0; i--) {
            SIncludeSoundscapeDefinition include = includeSoundscapeDefinitionList.get(i);
            Soundscape includedSoundscape = context.includableSoundscape(include.getIncludableSoundscapeRef().getText());
            if (includedSoundscape == null) {
                throw new SemanticException(include, "Referenced includable soundscape '%s' does not exist"
                        .formatted(include.getName()));
            }
            result = result.merge(includedSoundscape);
        }

        return result;
    }

    private Soundscape merge(Soundscape include) {
        String mergedName = this.name != null ? this.name : include.name();
        var mergedTracks = new HashMap<>(tracks);
        include.tracks().forEach(mergedTracks::putIfAbsent);
        Metadata mergedMetadata = metadata.merge(include.metadata());

        return new Soundscape(mergedName, source, Collections.unmodifiableMap(mergedTracks), mergedMetadata);
    }

    private static Map<String, Track> processTracks(SSoundscapeDefinition soundscape, Context context) {
        Map<String, Track> tracks = new HashMap<>();

        soundscape.getTrackDefinitionList()
                .forEach(collectChecked(tracks,
                        td -> td.getTrackId().getText(),
                        td -> Track.from(td, context)));
        return tracks;
    }

    private static Context processIncludableTracks(SSoundscapeDefinition soundscape, Context context) {
        return collectIncludableTracks(soundscape.getIncludableTrackDefinitionList().stream(), context);
    }

    private static Context processSamples(SSoundscapeDefinition soundscape, Context context) {
        Map<String, Sample> samples = new HashMap<>();
        soundscape.getLoadDefinitionList()
                .forEach(collectChecked(samples, sd -> sd.getSampleId().getText(), Sample::from));
        return context.withAdditionalSamples(samples);
    }

    private static Context processTracksNames(SSoundscapeDefinition soundscape, Context context) {
        Set<String> names = soundscape.getTrackDefinitionList().stream()
                .map(td -> td.getTrackId().getText())
                .collect(Collectors.toSet());

        return context.withTrackNames(names);
    }

    public Set<Sample> collectSamples() {
        Set<Sample> sources = new HashSet<>();
        tracks.values().forEach(t -> t.collectSamples(sources));
        return Collections.unmodifiableSet(sources);
    }
}
