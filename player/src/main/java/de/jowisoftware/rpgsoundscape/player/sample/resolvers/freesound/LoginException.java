package de.jowisoftware.rpgsoundscape.player.sample.resolvers.freesound;

import de.jowisoftware.rpgsoundscape.player.exception.HasLink;

public class LoginException extends RuntimeException implements HasLink {
    private final String link;

    public LoginException(String message, String link) {
        super(message);
        this.link = link;
    }

    @Override
    public String getSolveLink() {
        return link;
    }
}
