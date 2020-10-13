package de.jowisoftware.rpgsoundscape.player.library;

import org.springframework.context.ApplicationEvent;

public class LibraryUpdatedEvent extends ApplicationEvent {
    public LibraryUpdatedEvent(Object source) {
        super(source);
    }
}
