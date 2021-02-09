package de.jowisoftware.rpgsoundscape.player.library;

import de.jowisoftware.rpgsoundscape.model.Effect;
import de.jowisoftware.rpgsoundscape.model.Sample;
import de.jowisoftware.rpgsoundscape.model.Soundscape;
import de.jowisoftware.rpgsoundscape.player.sample.LookupResult;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository;
import de.jowisoftware.rpgsoundscape.player.sample.resolvers.SampleResolver;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AttributionService {
    private final SampleRepository sampleRepository;
    private final List<SampleResolver> resolvers;

    public AttributionService(SampleRepository sampleRepository, List<SampleResolver> resolvers) {
        this.sampleRepository = sampleRepository;
        this.resolvers = resolvers;
    }

    public Description describeSoundscape(Soundscape soundscape) {
        Set<Sample> samples = soundscape.collectSamples();

        String name = soundscape.name();
        String description = soundscape.metadata().description();
        Map<String, Set<String>> categories = soundscape.metadata().categories();
        int sampleCount = samples.size();
        int trackCount = soundscape.tracks().size();

        var attributions = samples.stream()
                .map(sampleRepository::lookup)
                .collect(Collectors.groupingBy(s -> findResolver(s.sample().uri()), Collectors.groupingBy(s -> s.sample().uri())))
                .entrySet().stream()
                .map(entry -> formatSamplesForResolver(entry.getValue(), entry.getKey()))
                .collect(Collectors.joining("\n\n"));

        return new Description(name, description, categories, sampleCount, trackCount, attributions);
    }

    private String formatSamplesForResolver(Map<URI, List<LookupResult>> samples, SampleResolver resolver) {
        String preamble = resolver.getAttributionPreamble("soundscape");
        String values = samples.entrySet().stream()
                .sorted(Comparator.comparing(kv -> kv.getKey().toString()))
                .map(innerEntry -> resolver.formatAttributionUri(innerEntry.getKey())
                        + collectAttributions(innerEntry.getValue()).map(s -> ": " + s).orElse(""))
                .collect(Collectors.joining("\n"));

        return preamble + "\n" + values;
    }

    private Optional<String> collectAttributions(List<LookupResult> lookupResults) {
        String result = lookupResults.stream()
                .map(LookupResult::attribution)
                .filter(Objects::nonNull)
                .sorted()
                .distinct()
                .collect(Collectors.joining(", "));

        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    public Description describeMusic(Effect effect) {
        return describeMusicOrEffect(effect, "music");
    }

    public Description describeEffect(Effect effect) {
        return describeMusicOrEffect(effect, "effect");
    }

    private Description describeMusicOrEffect(Effect effect, String type) {
        LookupResult lookup = sampleRepository.lookup(effect.play().sample());
        SampleResolver resolver = findResolver(lookup.sample().uri());
        String lookupAttribution = lookup.attribution();

        String attribution = "%s\n%s%s".formatted(
                resolver.getAttributionPreamble(type),
                resolver.formatAttributionUri(lookup.sample().uri()),
                lookupAttribution != null ? ": " + lookupAttribution : "");

        return new Description(effect.name(), effect.metadata().description(), effect.metadata().categories(),
                null, null, attribution);
    }

    private SampleResolver findResolver(URI uri) {
        return resolvers.stream()
                .filter(resolver -> resolver.supportsScheme(uri.getScheme()))
                .findAny()
                .orElseThrow();
    }

    public static record Description(
            String name,
            String description,
            /* sorted, but read only! **/
            Map<String, Set<String>> categories,
            Integer sampleCount,
            Integer trackCount,
            String attributions) {
    }
}
