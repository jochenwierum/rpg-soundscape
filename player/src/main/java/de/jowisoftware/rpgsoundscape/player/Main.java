package de.jowisoftware.rpgsoundscape.player;

import de.jowisoftware.rpgsoundscape.player.audio.javabackend.JavaClipAudioPlayer;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        var argList = new ArrayList<>(Arrays.asList(args));

        if (argList.stream().anyMatch(e -> e.equalsIgnoreCase("--print-mixers"))) {
            JavaClipAudioPlayer.printInfo();
            return;
        }

        SpringApplication application = new SpringApplication(Main.class);
        application.setBannerMode(Mode.OFF);
        application.run(argList.toArray(String[]::new));
    }
}
