package de.jowisoftware.rpgsoundscape.player.discord;

import de.jowisoftware.rpgsoundscape.player.audio.backend.discord.BlockingBuffer;
import de.jowisoftware.rpgsoundscape.player.audio.backend.discord.DiscordMixer;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class DiscordAudioHandler implements AudioSendHandler {
    private final BlockingBuffer<ByteBuffer> queue;

    public DiscordAudioHandler(DiscordMixer discordMixer) {
        this.queue = discordMixer.getOutQueue();
    }

    @Override
    public boolean canProvide() {
        return queue.isAvailable();
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return queue.nextRead();
    }

    @Override
    public boolean isOpus() {
        return false;
    }
}
