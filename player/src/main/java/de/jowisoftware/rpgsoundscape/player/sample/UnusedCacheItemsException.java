package de.jowisoftware.rpgsoundscape.player.sample;

import de.jowisoftware.rpgsoundscape.player.exception.HasLink;

public class UnusedCacheItemsException extends RuntimeException implements HasLink {
    public UnusedCacheItemsException(String message) {
        super(message);
    }

    @Override
    public String getSolveLink() {
        return "/api/cache/delete-unused";
    }
}
