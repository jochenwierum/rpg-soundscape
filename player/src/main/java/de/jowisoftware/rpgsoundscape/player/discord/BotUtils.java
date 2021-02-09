package de.jowisoftware.rpgsoundscape.player.discord;

import java.util.LinkedList;

public final class BotUtils {
    private BotUtils() {
    }

    public static LinkedList<String> parseString(String s) {
        var result = new LinkedList<String>();
        var b = new StringBuilder();

        char quote = 0;
        boolean nextRaw = false;

        for (char c : s.toCharArray()) {
            if (nextRaw) {
                b.append(c);
                nextRaw = false;
            } else if ((c == ' ' || c == '\t') && b.isEmpty()) {
                // skip spaces
            } else if (c == '\\') {
                nextRaw = true;
            } else if (c == '"') {
                if (quote == 0) {
                    quote = '"';
                } else if (quote == '"') {
                    quote = 0;
                } else {
                    b.append(c);
                }
            } else if (c == '\'') {
                if (quote == 0) {
                    quote = '\'';
                } else if (quote == '\'') {
                    quote = 0;
                } else {
                    b.append(c);
                }
            } else if (c == ' ' && quote == 0) {
                result.add(b.toString().trim());
                b.delete(0, b.length());
            } else {
                b.append(c);
            }
        }

        if (b.length() > 0) {
            result.add(b.toString().trim());
        }

        return result;
    }
}
