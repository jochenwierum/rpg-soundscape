package de.jowisoftware.rpgsoundscape.player.sample.resolvers;

import de.jowisoftware.rpgsoundscape.player.sample.ResolvedSample;

import java.net.URI;

public interface SampleResolver {
    boolean supportsScheme(String scheme);

    void resolve(URI uri, ResolverCallback resolverCallback);

    default void abortAll() {};

    String getAttributionPreamble(String type);

    default String formatAttributionUri(URI uri) {
        return uri.toString();
    }

    interface ResolverCallback {
        void resolve(ResolvedSample present);
        void reject(Exception e);
        void planTask(Runnable r);
    }
}
