package de.jowisoftware.rpgsoundscape.player.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.LinkedList;
import java.util.Optional;

public abstract class MessageWrapper {
    private final Member member;
    private final User author;
    private final Message message;
    protected final LinkedList<String> components;

    public MessageWrapper(Member member, User author, Message message, String input) {
        this.member = member;
        this.author = author;
        this.message = message;
        this.components = BotUtils.parseString(input);
    }

    public String consumeNextToken() throws BotCommandException {
        return consumeNextTokenIfAvailable()
                .orElseThrow(BotCommandException::missingArgument);
    }

    public Optional<String> consumeNextTokenIfAvailable() {
        if (components.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(components.removeFirst());
        }
    }

    public String previewNextToken() throws BotCommandException {
        if (components.isEmpty()) {
            throw BotCommandException.missingArgument();
        } else {
            return components.peekFirst();
        }
    }

    public void acknowledge() {
        message.addReaction("\uD83D\uDC4D").queue();
    }

    public Optional<Member> member() {
        return Optional.ofNullable(member);
    }

    public User author() {
        return author;
    }

    public abstract Guild consumeGuild() throws BotCommandException;

    public abstract void reply(CharSequence text);
}
