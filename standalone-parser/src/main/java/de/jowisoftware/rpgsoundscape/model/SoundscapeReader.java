package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.language.psi.SEffectDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableSoundscapeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SMusicDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SMusicEffectDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SRootContent;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SoundscapeReader {
    private SoundscapeReader() {
    }

    public static Set<String> readIncludes(SoundscapeFile psiFile) {
        Set<String> result = new HashSet<>();
        collect(List.of(psiFile), SRootContent::getIncludeDefinitionList)
                .forEach(definition -> result.add(definition.getFilename().parsed()));
        return result;
    }

    public static SoundscapeFileContent read(List<SoundscapeFile> files) {
        Context context = Context.create();
        context = readSamples(files, context);
        context = collectIncludableTracks(files, context);
        context = collectIncludableSoundscapes(files, context);

        Map<String, Soundscape> soundscapes = readSoundscapes(files, context);
        Map<String, Effect> music = readMusic(files, context);
        Map<String, Effect> effects = readEffects(files, context);

        return new SoundscapeFileContent(
                Collections.unmodifiableMap(soundscapes),
                Collections.unmodifiableMap(effects),
                Collections.unmodifiableMap(music));
    }

    private static Map<String, Soundscape> readSoundscapes(
            List<SoundscapeFile> files, Context context) {
        Map<String, Soundscape> soundscapes = new HashMap<>();

        collect(files, SRootContent::getSoundscapeDefinitionList)
                .filter(Predicate.not(SIncludableSoundscapeDefinition.class::isInstance))
                .forEach(definition -> Util.collectChecked(
                        definition, soundscapes,
                        definition.getString().parsed(),
                        Soundscape.from(definition, context)));

        return soundscapes;
    }

    private static Map<String, Effect> readMusic(List<SoundscapeFile> files, Context context) {
        return readMusicOrEffect(files, context, SMusicDefinition.class);
    }

    private static Map<String, Effect> readEffects(List<SoundscapeFile> files, Context context) {
        return readMusicOrEffect(files, context, SEffectDefinition.class);
    }

    private static <T extends SMusicEffectDefinition> Map<String, Effect> readMusicOrEffect(
            List<SoundscapeFile> files, Context context, Class<T> selector) {
        Map<String, Effect> effects = new HashMap<>();

        collect(files, SRootContent::getMusicEffectDefinitionList)
                .filter(selector::isInstance)
                .forEach(definition -> Util.collectChecked(definition, effects,
                        definition.getString().parsed(),
                        Effect.from(definition, context)));

        return effects;
    }

    private static Context collectIncludableTracks(List<SoundscapeFile> files, Context context) {
        return Util.collectIncludableTracks(collect(files, SRootContent::getIncludableTrackDefinitionList), context);
    }

    private static Context collectIncludableSoundscapes(List<SoundscapeFile> files, Context context) {
        Stream<SIncludableSoundscapeDefinition> includableSoundScapeChildren = collect(files, SRootContent::getSoundscapeDefinitionList)
                .flatMap(Util.filterCast(SIncludableSoundscapeDefinition.class));

        Map<String, Soundscape> includableSoundscapes = new HashMap<>();
        return Util.dagResolve(includableSoundScapeChildren, context.withIncludableSoundscapes(includableSoundscapes), includableSoundscapes,
                def -> def.getIncludableSoundscapeId().getText(),
                def -> def, SoundscapeReader::collectDependencies, Soundscape::from);
    }

    private static List<String> collectDependencies(SIncludableSoundscapeDefinition definition) {
        return definition.getIncludeSoundscapeDefinitionList().stream()
                .map(d -> d.getIncludableSoundscapeRef().getText())
                .collect(Collectors.toList());
    }

    private static Context readSamples(
            List<SoundscapeFile> files, Context context) {
        Map<String, Sample> samples = new HashMap<>();

        collect(files, SRootContent::getLoadDefinitionList).forEach(definition ->
                Util.collectChecked(definition, samples,
                        definition.getSampleId().getText(),
                        Sample.from(definition)));

        return context.withAdditionalSamples(samples);
    }

    private static <T> Stream<T> collect(List<SoundscapeFile> files,
            Function<SRootContent, List<T>> contentSelector) {
        return files.stream()
                .flatMap(file -> Arrays.stream(file.getChildren())
                        .filter(SRootContent.class::isInstance)
                        .flatMap(sr -> contentSelector.apply((SRootContent) sr).stream())
                );
    }

}
