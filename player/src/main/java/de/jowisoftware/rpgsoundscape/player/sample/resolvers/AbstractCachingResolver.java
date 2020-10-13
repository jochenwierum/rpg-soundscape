package de.jowisoftware.rpgsoundscape.player.sample.resolvers;

import de.jowisoftware.rpgsoundscape.player.sample.ResolvedSample;
import de.jowisoftware.rpgsoundscape.player.sample.SampleCache;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Optional;

public abstract class AbstractCachingResolver implements SampleResolver {
    protected SampleCache sampleCache;

    @Autowired
    public void setSampleCache(SampleCache sampleCache) {
        this.sampleCache = sampleCache;
    }

    @Override
    public void resolve(URI uri, ResolverCallback resolverCallback) {
        Optional<ResolvedSample> cached = sampleCache.tryResolve(uri);
        cached.ifPresentOrElse(
                resolverCallback::resolve,
                () -> this.receiveSample(uri, resolverCallback));
    }

    protected abstract void receiveSample(URI uri, ResolverCallback resolverCallback);
}
