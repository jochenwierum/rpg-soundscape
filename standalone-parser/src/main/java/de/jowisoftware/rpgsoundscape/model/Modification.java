package de.jowisoftware.rpgsoundscape.model;

import com.intellij.psi.PsiElement;
import de.jowisoftware.rpgsoundscape.exceptions.SemanticException;
import de.jowisoftware.rpgsoundscape.language.psi.SAmplificationPlayModification;
import de.jowisoftware.rpgsoundscape.language.psi.SAttributionLoadModification;
import de.jowisoftware.rpgsoundscape.language.psi.SLimitPlayModification;
import de.jowisoftware.rpgsoundscape.language.psi.SNoConversionLoadModification;
import de.jowisoftware.rpgsoundscape.language.psi.SOmissionPlayModification;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleModification;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public sealed interface Modification {

    record AmplificationModification(Percentage percentage) implements Modification {
        static AmplificationModification from(SAmplificationPlayModification amplification) {
            return new AmplificationModification(Percentage.from(amplification.getPercentage()));
        }

        public AmplificationModification merge(AmplificationModification other) {
            return new AmplificationModification(percentage.add(other.percentage));
        }
    }

    record AttributionModification(String attribution) implements Modification {
        static AttributionModification from(SAttributionLoadModification attribution) {
            return new AttributionModification(attribution.getString().parsed());
        }
    }

    record StartOmissionModification(Duration duration) implements Modification {
        static StartOmissionModification from(SOmissionPlayModification omission) {
            return new StartOmissionModification(omission.getTimespan().parsed());
        }

        public StartOmissionModification merge(StartOmissionModification other) {
            return new StartOmissionModification(Duration.ofMillis(duration.toMillis() + other.duration().toMillis()));
        }
    }

    record LimitModification(Duration duration) implements Modification {
        static LimitModification from(SLimitPlayModification limit) {
            return new LimitModification(limit.getTimespan().parsed());
        }

        public LimitModification merge(LimitModification other) {
            return new LimitModification(Duration.ofMillis(duration.toMillis() + other.duration().toMillis()));
        }
    }

    record NoConversionLoadModification() implements Modification {
    }

    static List<Modification> from(List<SSampleModification> list) {
        if (list == null) {
            return Collections.emptyList();
        }

        List<Modification> result = new ArrayList<>();

        singleOrEmpty(list, SAmplificationPlayModification.class, "amplification")
                .ifPresent(e -> result.add(AmplificationModification.from(e)));

        singleOrEmpty(list, SOmissionPlayModification.class, "omission")
                .ifPresent(e -> result.add(StartOmissionModification.from(e)));

        singleOrEmpty(list, SLimitPlayModification.class, "limit")
                .ifPresent(e -> result.add(LimitModification.from(e)));

        singleOrEmpty(list, SAttributionLoadModification.class, "attribution")
                .ifPresent(e -> result.add(AttributionModification.from(e)));

        singleOrEmpty(list, SNoConversionLoadModification.class, "conversion cache")
                .ifPresent(e -> result.add(new NoConversionLoadModification()));

        return Collections.unmodifiableList(result);
    }

    private static <T extends PsiElement, E extends PsiElement> Optional<E> singleOrEmpty(
            List<T> list, Class<E> targetType, String type) {
        List<E> matching = list.stream()
                .flatMap(Util.filterCast(targetType))
                .collect(Collectors.toList());

        if (matching.size() == 0) {
            return Optional.empty();
        } else if (matching.size() == 1) {
            return Optional.of(matching.get(0));
        } else {
            throw new SemanticException(matching.get(1), "Only one " + type + " can be specified");
        }
    }

}
