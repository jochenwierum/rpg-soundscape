package de.jowisoftware.rpgsoundscape.player.rest;

import de.jowisoftware.rpgsoundscape.player.sample.SampleCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/cache/")
public class CacheController {

    private final SampleCache sampleCache;

    public CacheController(SampleCache sampleCache) {
        this.sampleCache = sampleCache;
    }

    @GetMapping("delete-unused")
    public ModelAndView deleteUnused() {
        var mav = new ModelAndView("cache-deleteunused");
        mav.getModel().put("deleted", sampleCache.deleteUnused());
        return mav;
    }
}
