package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.language.psi.SEffectDefinition;
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
import java.util.stream.Stream;

public final class SoundscapeReader {
    private SoundscapeReader() {
    }

    public static Set<String> readIncludes(SoundscapeFile psiFile) {
        Set<String> result = new HashSet<>();
        collect(List.of(psiFile), SRootContent::getIncludeDefinitionList)
                .forEach(item -> result.add(item.content().getString().parsed()));
        return result;
    }

    public static SoundscapeFileContent read(List<SoundscapeFile> files) {
        Context context = Context.create();
        context = readSamples(files, context);
        context = readIncludableTracks(files, context);

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

        collect(files, SRootContent::getSoundscapeDefinitionList).forEach(tuple ->
                Util.collectChecked(
                        tuple.content(), soundscapes,
                        tuple.content().getString().parsed(),
                        Soundscape.from(tuple.content(), context.withFile(tuple.file()))));

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
                .filter(x -> selector.isInstance(x.content()))
                .forEach(tuple ->
                        Util.collectChecked(tuple.content(), effects,
                                tuple.content().getString().parsed(),
                                Effect.from(tuple.content(), context.withFile(tuple.file()))));

        return effects;
    }

    private static Context readIncludableTracks(List<SoundscapeFile> files, Context context) {
        Map<String, Statement> includableTracks = new HashMap<>();

        collect(files, SRootContent::getIncludableTrackDefinitionList).forEach(tuple ->
                Util.collectChecked(
                        tuple.content(), includableTracks,
                        tuple.content().getIncludableTrackId().getText(),
                        Block.from(tuple.content().getBlock(), context.withFile(tuple.file()))));

        return context.withAdditionalIncludableTracks(includableTracks);
    }

    private static Context readSamples(
            List<SoundscapeFile> files, Context context) {
        Map<String, Sample> samples = new HashMap<>();

        collect(files, SRootContent::getLoadDefinitionList).forEach(tuple ->
                Util.collectChecked(tuple.content(), samples,
                        tuple.content().getSampleId().getText(),
                        Sample.from(tuple.content())));

        return context.withAdditionalSamples(samples);
    }

    private static record Tuple<T>(
            SoundscapeFile file,
            T content) {
    }

    private static <T> Stream<Tuple<T>> collect(List<SoundscapeFile> files,
            Function<SRootContent, List<T>> contentSelector) {
        return files.stream()
                .flatMap(file -> Arrays.stream(file.getChildren())
                        .filter(SRootContent.class::isInstance)
                        .flatMap(sr -> contentSelector.apply((SRootContent) sr).stream())
                        .map(x -> new Tuple<>(file, x)));
    }

}
