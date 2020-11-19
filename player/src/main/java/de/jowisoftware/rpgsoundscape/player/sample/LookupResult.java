package de.jowisoftware.rpgsoundscape.player.sample;

import de.jowisoftware.rpgsoundscape.model.Modification.AttributionModification;
import de.jowisoftware.rpgsoundscape.model.Sample;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository.UriLookupResult;

import java.nio.file.Path;

public class LookupResult {
    private final UriLookupResult uriLookupResult;
    private final Sample sample;

    LookupResult(UriLookupResult uriLookupResult, Sample sample) {
        this.uriLookupResult = uriLookupResult;
        this.sample = sample;
    }

    public Path file() {
        return uriLookupResult.file();
    }

    public Sample sample() {
        return sample;
    }

    public SampleStatus sampleStatus() {
        return uriLookupResult.sampleStatus();
    }

    public String attribution() {
        return sample.getModification(AttributionModification.class)
                .map(AttributionModification::attribution)
                .orElse(uriLookupResult.attribution());
    }
}
