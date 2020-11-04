package de.jowisoftware.rpgsoundscape.model;

import com.intellij.psi.PsiElement;
import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.SPlayModificationAmplification;
import de.jowisoftware.rpgsoundscape.language.psi.SPlayModificationAttribution;
import de.jowisoftware.rpgsoundscape.language.psi.SPlayModificationItem;
import de.jowisoftware.rpgsoundscape.language.psi.SPlayModificationLimit;
import de.jowisoftware.rpgsoundscape.language.psi.SPlayModificationOmission;
import de.jowisoftware.rpgsoundscape.language.psi.SPlayModifications;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public sealed interface Modification {
    record AmplificationModification(Percentage percentage) implements Modification {
        static AmplificationModification from(SPlayModificationAmplification amplification) {
            return new AmplificationModification(Percentage.from(amplification.getPercentage()));
        }

        public AmplificationModification merge(AmplificationModification other) {
            return new AmplificationModification(percentage.add(other.percentage));
        }
    }

    record AttributionModification(String attribution) implements Modification {
        static AttributionModification from(SPlayModificationAttribution attribution) {
            return new AttributionModification(attribution.getString().parsed());
        }
    }

    record StartOmissionModification(Duration duration) implements Modification {
        static StartOmissionModification from(SPlayModificationOmission omission) {
            return new StartOmissionModification(omission.getTimespan().parsed());
        }

        public StartOmissionModification merge(StartOmissionModification other) {
            return new StartOmissionModification(Duration.ofMillis(duration.toMillis() + other.duration().toMillis()));
        }
    }

    record LimitModification(Duration duration) implements Modification {
        static LimitModification from(SPlayModificationLimit limit) {
            return new LimitModification(limit.getTimespan().parsed());
        }

        public LimitModification merge(LimitModification other) {
            return new LimitModification(Duration.ofMillis(duration.toMillis() + other.duration().toMillis()));
        }
    }

    static List<Modification> from(SPlayModifications sampleModifications, boolean allowAttribution) {
        if (sampleModifications == null) {
            return Collections.emptyList();
        }

        List<SPlayModificationItem> list = sampleModifications.getPlayModificationItemList();
        List<Modification> result = new ArrayList<>();

        singleOrEmpty(list,
                SPlayModificationItem::getPlayModificationAmplification, "amplification")
                .ifPresent(e -> result.add(AmplificationModification.from(e)));

        singleOrEmpty(list, SPlayModificationItem::getPlayModificationOmission, "omission")
                .ifPresent(e -> result.add(StartOmissionModification.from(e)));

        singleOrEmpty(list, SPlayModificationItem::getPlayModificationLimit, "limit")
                .ifPresent(e -> result.add(LimitModification.from(e)));

        singleOrEmpty(list, SPlayModificationItem::getPlayModificationAttribution, "attribution")
                .ifPresent(e -> {
                    if (allowAttribution) {
                        result.add(AttributionModification.from(e));
                    } else {
                        throw new SemanticException(sampleModifications, "Attribution is not allowed here");
                    }
                });

        return Collections.unmodifiableList(result);
    }

    private static <T extends PsiElement, E extends PsiElement> Optional<E> singleOrEmpty(
            List<T> list, Function<T, E> selection, String type) {
        return singleOrEmpty(list.stream()
                .map(selection)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()), type);
    }

    private static <E extends PsiElement> Optional<E> singleOrEmpty(List<E> filtered, String type) {
        if (filtered.size() == 0) {
            return Optional.empty();
        } else if (filtered.size() == 1) {
            return Optional.of(filtered.get(0));
        } else {
            throw new SemanticException(filtered.get(1), "Only one " + type + " can be specified");
        }
    }
}
