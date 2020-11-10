package de.jowisoftware.rpgsoundscape.player.sample;

import de.jowisoftware.rpgsoundscape.model.Modification.AttributionModification;
import de.jowisoftware.rpgsoundscape.model.Sample;
import de.jowisoftware.rpgsoundscape.player.sample.SampleRepository.UriLookupResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class LookupResult {
    private final UriLookupResult uriLookupResult;
    private final Sample sample;

    LookupResult(UriLookupResult uriLookupResult, Sample sample) {
        this.uriLookupResult = uriLookupResult;
        this.sample = sample;
    }

    public InputStream open() throws IOException {
        try {
            return uriLookupResult.inputStreamSupplier().get();
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    public Sample sample() {
        return sample;
    }

    public SampleStatus sampleStatus() {
        return uriLookupResult.sampleStatus();
    }

    public boolean isPreconverted() {
        return uriLookupResult.isPreconverted();
    }

    public String attribution() {
        return sample.getModification(AttributionModification.class)
                .map(AttributionModification::attribution)
                .orElse(uriLookupResult.attribution());
    }
}
