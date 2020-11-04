package de.jowisoftware.rpgsoundscape.model;

import com.intellij.psi.PsiElement;
import de.jowisoftware.rpgsoundscape.language.psi.SEffectDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SMusicDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SMusicEffectDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SRootItem;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public final class SoundscapeReader {
    private SoundscapeReader() {
    }

    public static Set<String> readIncludes(SoundscapeFile psiFile) {
        Set<String> result = new HashSet<>();
        collect(List.of(psiFile), SRootItem::getIncludeDefinition).forEach(item -> result.add(item.content().getString().parsed())
        );
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

        collect(files, SRootItem::getSoundscapeDefinition).forEach(tuple -> Util.collectChecked(
                tuple.content(), soundscapes,
                tuple.content().getString().parsed(),
                Soundscape.from(tuple.content(), context.withFile(tuple.file()))));

        return soundscapes;
    }

    private static Map<String, Effect> readMusic(List<SoundscapeFile> files, Context context) {
        return readMusicOrEffect(files, context, SRootItem::getMusicDefinition, SMusicDefinition::getMusicEffectDefinition);
    }

    private static Map<String, Effect> readEffects(List<SoundscapeFile> files, Context context) {
        return readMusicOrEffect(files, context, SRootItem::getEffectDefinition, SEffectDefinition::getMusicEffectDefinition);
    }

    private static <T extends PsiElement> Map<String, Effect> readMusicOrEffect(List<SoundscapeFile> files, Context context,
            Function<SRootItem, T> selector, Function<T, SMusicEffectDefinition> getDefinition) {
        Map<String, Effect> effects = new HashMap<>();

        collect(files, selector).forEach(tuple -> Util.collectChecked(tuple.content(), effects,
                getDefinition.apply(tuple.content()).getString().parsed(),
                Effect.from(getDefinition.apply(tuple.content()), context.withFile(tuple.file()))));

        return effects;
    }

    private static Context readIncludableTracks(List<SoundscapeFile> files, Context context) {
        Map<String, Statement> includableTracks = new HashMap<>();

        collect(files, SRootItem::getIncludableTrackDefinition).forEach(tuple ->
                Util.collectChecked(
                        tuple.content(), includableTracks,
                        tuple.content().getIncludableTrackId().getText(),
                        Block.from(tuple.content().getBlock(), context.withFile(tuple.file()))));

        return context.withAdditionalIncludableTracks(includableTracks);
    }

    private static Context readSamples(
            List<SoundscapeFile> files, Context context) {
        Map<String, Sample> samples = new HashMap<>();

        collect(files, SRootItem::getLoadDefinition).forEach(tuple ->
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
            Function<SRootItem, T> filter) {
        return files.stream()
                .flatMap(file -> Arrays.stream(file.getChildren())
                        .filter(SRootItem.class::isInstance)
                        .map(SRootItem.class::cast)
                        .map(filter)
                        .filter(Objects::nonNull)
                        .map(x -> new Tuple<>(file, x)));
    }

}
