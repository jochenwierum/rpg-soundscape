package de.jowisoftware.rpgsoundscape.model;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;
import de.jowisoftware.rpgsoundscape.language.psi.impl.SIncludeTrackStatementImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Util {
    private Util() {
    }

    public static <S extends PsiElement, T> Consumer<S> collectChecked(
            Map<String, T> targetmap,
            Function<S, String> nameTransformation, Function<S, T> transformation) {
        return source -> collectChecked(source, targetmap, nameTransformation.apply(source), transformation.apply(source));
    }

    public static <T> void collectChecked(PsiElement source, Map<String, T> targetmap, String key, T value) {
        if (targetmap.put(key, value) != null) {
            throw new SemanticException(source, "%s with the name '%s' already exists".formatted(
                    source.getClass().getSimpleName(), key));
        }
    }

    public static Context collectIncludableTracks(Stream<SIncludableTrackDefinition> definitions, Context context) {
        Map<String, Statement> includableTracks = new HashMap<>();
        return dagResolve(definitions,
                context.withAdditionalIncludableTracks(includableTracks),
                includableTracks,
                d -> d.getIncludableTrackId().getText(),
                SIncludableTrackDefinition::getBlock,
                Util::collectDependencies,
                Block::from);
    }

    public static <I extends PsiElement, C extends PsiElement, M> Context dagResolve(
            Stream<I> definitions,
            Context context,
            Map<String, M> contextMap,
            Function<I, String> nameIdentifier,
            Function<I, C> contentExtractor,
            Function<I, List<String>> dependencyCollector,
            BiFunction<C, Context, M> processor
    ) {

        Map<String, DagInfo<C>> unprocessed = new HashMap<>();
        definitions.forEach(definition -> Util.collectChecked(
                definition,
                unprocessed,
                nameIdentifier.apply(definition),
                new DagInfo<>(contentExtractor.apply(definition), dependencyCollector.apply(definition))));


        while (!unprocessed.isEmpty()) {
            List<String> front = unprocessed.entrySet().stream()
                    .filter(kv -> kv.getValue().dependencies().isEmpty())
                    .map(Entry::getKey)
                    .collect(Collectors.toList());

            if (front.isEmpty()) {
                throw new SemanticException(unprocessed.values().iterator().next().content(),
                        "Found cyclic dependency or unresolved reference in includable track(s): " + unprocessed.keySet());
            } else {
                front.forEach(name -> {
                    DagInfo<C> dagInfo = unprocessed.remove(name);
                    contextMap.put(name, processor.apply(dagInfo.content, context.withFile(file(dagInfo.content))));
                    unprocessed.forEach((__, info) -> info.dependencies().remove(name));
                });
            }
        }

        return context;
    }

    private static List<String> collectDependencies(SIncludableTrackDefinition definition) {
        return PsiTreeUtil.findChildrenOfType(definition, SIncludeTrackStatementImpl.class)
                .stream()
                .map(definition2 -> definition2.getIncludableTrackRef().getText())
                .collect(Collectors.toList());
    }

    public static <T> Function<Object, Stream<T>> filterCast(Class<T> targetType) {
        return o -> {
            if (targetType.isInstance(o)) {
                @SuppressWarnings("unchecked") T casted = (T) o;
                return Stream.of(casted);
            } else {
                return null;
            }
        };
    }

    public static SoundscapeFile file(PsiElement element) {
        return (SoundscapeFile) element.getContainingFile();
    }

    public static record DagInfo<T extends PsiElement>(
            T content,
            List<String> dependencies) {
    }
}
