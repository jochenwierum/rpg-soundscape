package de.jowisoftware.rpgsoundscape.player.rest;

import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings;
import de.jowisoftware.rpgsoundscape.player.config.ApplicationSettings.ExitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.ProcessBuilder.Redirect;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
public class SystemController {
    private static final Logger LOG = LoggerFactory.getLogger(SystemController.class);

    private final ConfigurableApplicationContext applicationContext;
    private final ApplicationSettings applicationSettings;

    public SystemController(ConfigurableApplicationContext applicationContext, ApplicationSettings applicationSettings) {
        this.applicationContext = applicationContext;
        this.applicationSettings = applicationSettings;
    }

    @PostMapping("/exit")
    public void exit() {
        switch (applicationSettings.getUi().getExitType()) {
            case DISABLED -> LOG.info("Exit command is disabled");
            case QUIT -> applicationContext.close();
            case EXIT_COMMAND -> {
                String command = applicationSettings.getUi().getExitCommand();
                try {
                    new ProcessBuilder(command)
                            .redirectErrorStream(true)
                            .redirectOutput(Redirect.INHERIT)
                            .start()
                            .waitFor();
                } catch (Exception e) {
                    LOG.error("Could not execute exit command", e);
                }
                applicationContext.close();
            }
        }
    }

    @GetMapping("/config")
    public Map<String, Boolean> config() {
        return Map.of(
                "fullscreen", applicationSettings.getUi().isAllowFullscreen(),
                "exit", applicationSettings.getUi().getExitType() != ExitType.DISABLED,
                "logs", applicationSettings.getUi().getLogs().isEnabled()
        );
    }

}
