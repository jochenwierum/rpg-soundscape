package de.jowisoftware.rpgsoundscape.player;

import de.jowisoftware.rpgsoundscape.player.audio.AudioPlayer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        var argList = new ArrayList<>(Arrays.asList(args));

        if (argList.stream().anyMatch(e -> e.equalsIgnoreCase("--print-mixers"))) {
            AudioPlayer.printInfo();
            return;
        }

        SpringApplication application = new SpringApplication(Main.class);
        //application.setApplicationStartup(new BufferingApplicationStartup(1500));
        application.run(argList.toArray(String[]::new));
    }
}
