package de.jowisoftware.rpgsoundscape.player.discord;

import de.jowisoftware.rpgsoundscape.model.Effect;
import de.jowisoftware.rpgsoundscape.model.MetadataAware;
import de.jowisoftware.rpgsoundscape.model.Soundscape;
import de.jowisoftware.rpgsoundscape.player.config.DiscordComponent;
import de.jowisoftware.rpgsoundscape.player.library.AbstractAssetLibrary;
import de.jowisoftware.rpgsoundscape.player.library.AttributionService;
import de.jowisoftware.rpgsoundscape.player.library.AttributionService.Description;
import de.jowisoftware.rpgsoundscape.player.library.EffectLibrary;
import de.jowisoftware.rpgsoundscape.player.library.MusicLibrary;
import de.jowisoftware.rpgsoundscape.player.library.SoundscapeLibrary;
import de.jowisoftware.rpgsoundscape.player.player.EffectPlayer;
import de.jowisoftware.rpgsoundscape.player.player.MusicPlayer;
import de.jowisoftware.rpgsoundscape.player.player.SoundscapePlayer;
import de.jowisoftware.rpgsoundscape.player.threading.BlockExecutionContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

@DiscordComponent
public class CommonMessageProcessor {
    public static final String HELP_STRING =
            """
            `leave` - I leave the current channel.

            `soundscapes` [filter] - I send you a list of soundscapes that I know.
            `soundscape name` - I play soundscape `name`.
            `play soundscape name` - Same as `soundscape name`.
            `stop soundscape` - I stop the current soundscape.
            `tracks [filter]` - I send you a list of all current tracks.
            `reset tracks` - I reset all tracks.
            `play track name` - I play the track `name` .
            `stop track name` - I stop the track `name`.
            `describe soundscape name` - I describe soundscape `name`.

            `effects` [filter] - I send you a list of effects that I know.
            `effect name` - I play the effect `name`.
            `play effect name` - Same as `effect name`.
            `describe effect_name` - I describe effect `name`.

            `music` [filter] - I send you a list of music that I know.
            `play music name` - I play the music `name`.
            `reset music` - I restart the music.
            `stop music` - I stop the current music.
            `describe music name` - I describe music `name`.

            `auth` - I send you the authorization URL to join new guilds. 
            `help` - I print this help.
             """;

    private final SoundscapeLibrary soundscapeLibrary;
    private final MusicLibrary musicLibrary;
    private final EffectLibrary effectLibrary;
    private final SoundscapePlayer soundscapePlayer;
    private final DiscordHelper discordHelper;
    private final EffectPlayer effectPlayer;
    private final MusicPlayer musicPlayer;
    private final AttributionService attributionService;

    public CommonMessageProcessor(
            SoundscapeLibrary soundscapeLibrary,
            MusicLibrary musicLibrary,
            EffectLibrary effectLibrary,
            SoundscapePlayer soundscapePlayer,
            DiscordHelper discordHelper,
            EffectPlayer effectPlayer,
            MusicPlayer musicPlayer,
            AttributionService attributionService) {
        this.soundscapeLibrary = soundscapeLibrary;
        this.musicLibrary = musicLibrary;
        this.effectLibrary = effectLibrary;
        this.soundscapePlayer = soundscapePlayer;
        this.discordHelper = discordHelper;
        this.effectPlayer = effectPlayer;
        this.musicPlayer = musicPlayer;
        this.attributionService = attributionService;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean processMessage(String token, MessageWrapper message) throws BotCommandException {
        switch (token.toLowerCase(Locale.ROOT)) {
            case "auth" -> message.reply(discordHelper.createInviteUrl());
            case "describe" -> describe(message);
            case "effect" -> playEffect(message);
            case "effects" -> list(message, "effects", effectLibrary);
            case "join" -> join(message);
            case "leave" -> discordHelper.leaveVoiceChannel();
            case "music" -> list(message, "music", musicLibrary);
            case "play" -> play(message);
            case "reset" -> reset(message);
            case "soundscape" -> playSoundscape(message);
            case "soundscapes" -> list(message, "soundscape", soundscapeLibrary);
            case "stop" -> stop(message);
            case "tracks" -> listTracks(message);
            default -> {
                return false;
            }
        }

        return true;
    }

    private void play(MessageWrapper message) throws BotCommandException {
        String kind = message.consumeNextToken().toLowerCase(Locale.ROOT);
        switch (kind) {
            case "soundscape" -> playSoundscape(message);
            case "music" -> playMusic(message);
            case "effect" -> playEffect(message);
            case "track" -> playTrack(message);
            default -> throw BotCommandException.unknownArgument(kind);
        }
    }

    private void stop(MessageWrapper message) throws BotCommandException {
        String kind = message.consumeNextToken().toLowerCase(Locale.ROOT);
        switch (kind) {
            case "soundscape" -> pauseSoundscape(message);
            case "music" -> pauseMusic(message);
            case "track" -> pauseTrack(message);
            default -> throw BotCommandException.unknownArgument(kind);
        }
    }

    private void reset(MessageWrapper message) throws BotCommandException {
        String kind = message.consumeNextToken().toLowerCase(Locale.ROOT);
        switch (kind) {
            case "tracks" -> resetTracks(message);
            case "music" -> resetMusic(message);
            default -> throw BotCommandException.unknownArgument(kind);
        }
    }

    private void describe(MessageWrapper message) throws BotCommandException {
        String kind = message.consumeNextToken().toLowerCase(Locale.ROOT);

        switch (kind) {
            case "soundscape" -> describe("soundscape", attributionService::describeSoundscape, soundscapeLibrary, message);
            case "music" -> describe("music", attributionService::describeMusic, musicLibrary, message);
            case "effect" -> describe("effect", attributionService::describeEffect, effectLibrary, message);
            default -> throw BotCommandException.unknownArgument(kind);
        }
    }

    private <S extends MetadataAware> void describe(String type, Function<S, Description> descriptionFunction,
            AbstractAssetLibrary<S> library, MessageWrapper message) throws BotCommandException {
        String name = message.consumeNextToken();

        S item = library.get(name).orElseThrow(() -> BotCommandException.notFound(name));
        Description description = descriptionFunction.apply(item);

        var b = new StringBuilder(description.name());
        if (description.description() != null) {
            b.append("\n\n").append(description.description());
        }

        if (!description.categories().isEmpty()) {
            b.append("\n\nCategories:");
            description.categories().forEach((key, set) -> {
                b.append("\n- ").append(key).append(":");
                set.forEach(s -> b.append("\n  * ").append(s));
            });
        }

        if (description.sampleCount() != null || description.trackCount() != null) {
            b.append("\n\nStatistics:");
            if (description.sampleCount() != null) {
                b.append("\nSamples: ").append(description.sampleCount());
            }
            if (description.trackCount() != null) {
                b.append("\nTracks: ").append(description.trackCount());
            }
        }

        if (description.attributions() != null) {
            b.append("\n\nAttributions:\n").append(description.attributions());
        }

        message.reply(b);
    }

    private void pauseSoundscape(MessageWrapper message) {
        soundscapePlayer.pauseAll();
        message.acknowledge();
    }

    private void pauseMusic(MessageWrapper message) {
        musicPlayer.pause();
        message.acknowledge();
    }

    public void join(MessageWrapper message) throws BotCommandException {
        Guild guild = message.consumeGuild();
        Optional<String> channelName = message.consumeNextTokenIfAvailable();

        VoiceChannel channel = channelName.isEmpty()
                ? guessChannel(guild, message)
                : findChannel(guild, channelName.get());

        discordHelper.joinVoiceChat(channel);
        message.acknowledge();
        message.reply("Connecting to `%s`".formatted(channel.getName()));
    }

    private VoiceChannel guessChannel(Guild guild, MessageWrapper message) throws BotCommandException {
        VoiceChannel channel = null;

        boolean hasMember = message.member().isPresent();
        if (hasMember) {
            Member member = message.member().orElseThrow();

            GuildVoiceState voiceState = member.getVoiceState();
            if (voiceState != null) {
                channel = voiceState.getChannel();
            }
        }

        if (channel == null) {
            List<VoiceChannel> channels = guild.getVoiceChannels();
            if (channels.size() == 1) {
                channel = channels.get(0);
            } else if (!hasMember) {
                channel = channels.stream()
                        .filter(voiceChannel -> voiceChannel.getMembers().stream()
                                .anyMatch(m -> m.getUser().getIdLong() == message.author().getIdLong())
                        )
                        .findAny()
                        .orElse(null);
            }
        }

        if (channel == null) {
            throw new BotCommandException(
                    """
                    I could not find you in any voice channel.
                    Please use `join name-of-the-channel` or join a channel first.
                    """);
        }

        return channel;
    }

    private VoiceChannel findChannel(Guild guild, String nameOrId) throws BotCommandException {
        VoiceChannel channel = null;
        if (nameOrId.matches("^\\d+$")) {
            channel = guild.getVoiceChannelById(nameOrId);
        }

        if (channel == null) {
            List<VoiceChannel> channels = guild.getVoiceChannelsByName(nameOrId, true);
            if (!channels.isEmpty()) {
                channel = channels.get(0);
            }
        }

        if (channel == null) {
            throw new BotCommandException("Unable to connect to `%s`, no such channel!".formatted(nameOrId));
        }
        return channel;
    }


    private void list(MessageWrapper message, final String type, AbstractAssetLibrary<?> library) {
        Optional<String> pattern = message.consumeNextTokenIfAvailable()
                .map(s -> s.toLowerCase(Locale.ROOT));

        var b = new StringBuilder("I know the following " + type);
        if (pattern.isPresent()) {
            b.append(" matching your pattern");
        }
        b.append(":");

        library.listAll().stream()
                .filter(name -> pattern.map(p -> name.toLowerCase(Locale.ROOT).contains(p)).orElse(true))
                .forEach(soundscape -> b.append("- ").append(soundscape).append("\n"));

        message.reply(b);
    }

    private void playSoundscape(MessageWrapper message) throws BotCommandException {
        String name = message.consumeNextToken();
        Soundscape soundscape = soundscapeLibrary.get(name)
                .orElseThrow(() -> BotCommandException.notFound(name));

        soundscapePlayer.switchSoundscape(soundscape);
        message.acknowledge();
    }

    private void playEffect(MessageWrapper message) throws BotCommandException {
        String name = message.consumeNextToken();
        Effect effect = effectLibrary.get(name)
                .orElseThrow(() -> BotCommandException.notFound(name));

        effectPlayer.playEffect(effect);
        message.acknowledge();
    }

    private void playMusic(MessageWrapper message) throws BotCommandException {
        String name = message.consumeNextToken();
        Effect music = musicLibrary.get(name)
                .orElseThrow(() -> BotCommandException.notFound(name));

        musicPlayer.switchMusic(music);
        message.acknowledge();
    }

    private void listTracks(MessageWrapper message) {
        Optional<String> pattern = message.consumeNextTokenIfAvailable()
                .map(s -> s.toLowerCase(Locale.ROOT));

        var b = new StringBuilder("I know the following tracks");
        if (pattern.isPresent()) {
            b.append(" matching your pattern");
        }
        b.append(":");

        soundscapePlayer.getTrackContexts().stream()
                .map(BlockExecutionContext::getName)
                .forEach(n -> b.append("- ").append(n).append("\n"));

        message.reply(b);
    }


    private void playTrack(MessageWrapper message) throws BotCommandException {
        String track = message.consumeNextToken();
        if (soundscapePlayer.resumeTrack(track)) {
            message.acknowledge();
        } else {
            throw BotCommandException.notFound(track);
        }
    }

    private void pauseTrack(MessageWrapper message) throws BotCommandException {
        String track = message.consumeNextToken();
        if (soundscapePlayer.pauseTrack(track)) {
            message.acknowledge();
        } else {
            throw BotCommandException.notFound(track);
        }
    }

    private void resetTracks(MessageWrapper message) {
        soundscapePlayer.resetAllTracks();
        message.acknowledge();
    }

    private void resetMusic(MessageWrapper message) {
        musicPlayer.restart();
        message.acknowledge();
    }
}
