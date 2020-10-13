package de.jowisoftware.rpgsoundscape.player.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NameDto(@JsonProperty("name") String name) {
}
