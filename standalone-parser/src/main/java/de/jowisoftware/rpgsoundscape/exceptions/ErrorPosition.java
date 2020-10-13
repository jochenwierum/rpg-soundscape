package de.jowisoftware.rpgsoundscape.exceptions;

import com.intellij.psi.PsiElement;

import java.util.Objects;
import java.util.StringJoiner;

public class ErrorPosition {
    private final String fileName;
    private int line;
    private int column;

    private String lineContent;
    private String previousLineContent;
    private String nextLineContent;

    public ErrorPosition(PsiElement element) {
        this.fileName = element.getContainingFile().getName();

        int offset = element.getNode().getStartOffset();
        String[] lines = element.getContainingFile().getText().split("(?<=\\n)");

        int lineStartOffset = 0;
        for (int i = 0; i < lines.length; i++) {
            int length = lines[i].length();

            if (lineStartOffset <= offset && lineStartOffset + length > offset) {
                line = i;
                column = offset - lineStartOffset;

                lineContent = lines[i];
                if (i > 0) {
                    previousLineContent = lines[i - 1];
                }
                if (i < lines.length - 1) {
                    nextLineContent = lines[i + 1];
                }

                break;
            }

            lineStartOffset += length;
        }
    }

    public String positionString() {
        return "%s ad %d:%d".formatted(fileName, line, column);
    }

    public String extract() {
        StringBuilder builder = new StringBuilder("line ");
        builder.append(line + 1).append(':').append(column);
        builder.append(" in file ").append(fileName).append("\n");

        int decimals = (int) Math.ceil(Math.log10(line + 2));

        if (previousLineContent != null) {
            builder.append(formatLine(line - 1, decimals, previousLineContent));
        }

        builder.append(formatLine(line, decimals, lineContent));
        builder.append("-".repeat(column + decimals + 2)).append("^ (here)").append("\n");

        if (nextLineContent != null) {
            builder.append(formatLine(line + 1, decimals, nextLineContent));
        }

        return builder.toString();
    }

    private String formatLine(int line, int decimals, String content) {
        return ("%" + decimals + "d: %s").formatted(line, content);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ErrorPosition.class.getSimpleName() + "[", "]")
                .add("fileName='" + fileName + "'")
                .add("line=" + line)
                .add("column=" + column)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ErrorPosition that)) {
            return false;
        }

        return line == that.line &&
                column == that.column &&
                Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, line, column);
    }
}
