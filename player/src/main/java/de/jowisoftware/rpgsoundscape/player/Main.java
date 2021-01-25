package de.jowisoftware.rpgsoundscape.player;

import de.jowisoftware.rpgsoundscape.player.audio.backend.java.JavaAudioBackend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
@PropertySource(value = "file:config.properties", ignoreResourceNotFound = true, encoding = "UTF-8")
public class Main {
    public static void main(String[] args) {
        var argList = new ArrayList<>(Arrays.asList(args));

        if (argList.stream().anyMatch(e -> e.equalsIgnoreCase("--print-mixers"))) {
            JavaAudioBackend.printInfo();
            return;
        }

        SpringApplication application = new SpringApplication(Main.class);
        //application.setApplicationStartup(new BufferingApplicationStartup(1500));
        application.run(argList.toArray(String[]::new));
    }
}
