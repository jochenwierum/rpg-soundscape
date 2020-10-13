package de.jowisoftware.rpgsoundscape.player.sample.resolvers.freesound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class FreesoundController {
    private static final Logger LOG = LoggerFactory.getLogger(FreesoundController.class);

    private final FreesoundResolver resolver;
    private final FreesoundDownloader downloader;
    private final FreesoundSettings settings;

    public FreesoundController(FreesoundResolver resolver, FreesoundDownloader downloader, FreesoundSettings settings) {
        this.resolver = resolver;
        this.downloader = downloader;
        this.settings = settings;
    }

    @GetMapping("/import/freesound")
    public ModelAndView enterKey() {
        var mav = new ModelAndView("freesound1");
        mav.getModel().put("clientId", settings.getClientId());
        return mav;
    }

    @GetMapping("/import/freesound/success")
    public String enterKey2(@RequestParam("code") String code, @RequestParam("error") Optional<String> error) {
        if (error.isPresent()) {
            LOG.error("User denied the access!");
            return "redirect:/import/freesound";
        }

        synchronized (downloader) {
            if (downloader.login(code)) {
                LOG.info("Login was successful, downloading samples");
                resolver.retryDownloads();
                return "freesound2";
            } else {
                LOG.warn("Login failed");
                return "redirect:/import/freesound";
            }
        }
    }
}
