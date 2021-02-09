package de.jowisoftware.rpgsoundscape.player.discord;

public class BotCommandException extends Exception {
    public BotCommandException(String message) {
        super(message);
    }

    public static BotCommandException missingArgument() {
        return new BotCommandException("An argument is missing");
    }

    public static BotCommandException notFound(String name) {
        return new BotCommandException("I don't know something like `%s`".formatted(name));
    }

    public static BotCommandException unknownArgument(String kind) {
        return new BotCommandException("I did not understand the argument `%s`".formatted(kind));
    }

    public void sendReply(MessageWrapper message) {
        message.reply(getMessage() + " (try `help`)");
    }
}
