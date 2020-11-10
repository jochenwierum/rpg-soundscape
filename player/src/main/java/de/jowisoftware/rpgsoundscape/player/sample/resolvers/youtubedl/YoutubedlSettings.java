package de.jowisoftware.rpgsoundscape.player.sample.resolvers.youtubedl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "application.youtubedl")
public class YoutubedlSettings {
    private String path = "youtube-dl";
    private List<String> arguments = new ArrayList<>(List.of(
            "-q",
            "--no-playlist", "--no-progress",
            "-x",
            "--audio-format", "mp3",
            "-o", "$file",
            "$url"));

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }
}
