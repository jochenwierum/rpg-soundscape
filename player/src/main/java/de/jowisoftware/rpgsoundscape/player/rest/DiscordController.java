package de.jowisoftware.rpgsoundscape.player.rest;

import de.jowisoftware.rpgsoundscape.player.config.ConditionalOnDiscord;
import de.jowisoftware.rpgsoundscape.player.discord.DiscordHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/discord")
@ConditionalOnDiscord
public class DiscordController {
    private final DiscordHelper discordHelper;

    public DiscordController(DiscordHelper discordHelper) {
        this.discordHelper = discordHelper;
    }

    @GetMapping("/status")
    public Map<String, Object> state() {
        var map = new HashMap<String, Object>();
        map.put("guildId", discordHelper.currentJoinedGuild().orElse(null));
        map.put("channelId", discordHelper.currentJoinedChannel().orElse(null));
        map.put("inviteUrl", discordHelper.createInviteUrl());
        return map;
    }

    @GetMapping("/guilds")
    public List<Map<String, String>> guilds() {
        return discordHelper.getGuilds().stream()
                .map(e -> Map.of(
                        "id", Long.toString(e.id()),
                        "name", e.name()))
                .collect(Collectors.toList());
    }

    @GetMapping("/guild/{guildId}/channels")
    public ResponseEntity<List<Map<String, String>>> channels(@PathVariable("guildId") String guildId) {
        return discordHelper.listChannels(guildId)
                .map(c -> c
                        .stream()
                        .map(e -> Map.of(
                                "id", Long.toString(e.id()),
                                "name", e.name()))
                        .collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/guild/{guildId}/channel/{channelId}/join")
    public ResponseEntity<?> join(@PathVariable("guildId") String guildId, @PathVariable("channelId") String channelId) {
        if (discordHelper.joinVoiceChat(guildId, channelId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/leave")
    public ResponseEntity<?> leave() {
        discordHelper.leaveVoiceChannel();
        return ResponseEntity.noContent().build();
    }

}
