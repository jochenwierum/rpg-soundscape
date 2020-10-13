package de.jowisoftware.rpgsoundscape.intellij;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import de.jowisoftware.rpgsoundscape.intellij.psi.SoundscapeTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SoundscapeSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey SEPARATOR =
            createTextAttributesKey("SOUNDSCAPE_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey BRACES =
            createTextAttributesKey("SOUNDSCAPE_BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey IDENTIFIER =
            createTextAttributesKey("SOUNDSCAPE_IDENTIFIER_KEY", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey STRING =
            createTextAttributesKey("SOUNDSCAPE_STRING_KEY", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("SOUNDSCAPE_KEYWORD_VALUE", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey DETAIL =
            createTextAttributesKey("SOUNDSCAPE_DETAIL_VALUE", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("SOUNDSCAPE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("SOUNDSCAPE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] EMPTY = new TextAttributesKey[0];

    private static final Map<IElementType, TextAttributesKey[]> mapping = Stream.of(
            entries(BAD_CHARACTER, TokenType.BAD_CHARACTER),
            entries(COMMENT,
                    SoundscapeParserDefinition.COMMENT_CONTENT, SoundscapeParserDefinition.COMMENT_START,
                    SoundscapeParserDefinition.COMMENT_END, SoundscapeParserDefinition.LINE_COMMENT),
            entries(SEPARATOR, SoundscapeTypes.SEPARATOR),
            entries(IDENTIFIER, SoundscapeTypes.IDENTIFIER),
            entries(STRING, SoundscapeTypes.STRING),
            entries(BRACES, SoundscapeTypes.CURLY_L, SoundscapeTypes.CURLY_R),
            entries(DETAIL, SoundscapeTypes.LOOPING, SoundscapeTypes.MANUAL, SoundscapeTypes.AUTOSTARTING,
                    SoundscapeTypes.PAUSED, SoundscapeTypes.WITH, SoundscapeTypes.TITLE),
            entries(KEYWORD,
                    SoundscapeTypes.PLAY, SoundscapeTypes.SLEEP, SoundscapeTypes.PAUSE, SoundscapeTypes.RESUME,
                    SoundscapeTypes.REPEAT, SoundscapeTypes.RANDOMLY, SoundscapeTypes.PARALLELLY, SoundscapeTypes.WEIGHTED)
    )
            .flatMap(Function.identity())
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

    private static Stream<Entry<IElementType, TextAttributesKey[]>> entries(TextAttributesKey value, IElementType... keys) {
        return Stream.of(keys).map(key -> Map.entry(key, new TextAttributesKey[]{value}));
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new SoundscapeLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return mapping.getOrDefault(tokenType, EMPTY);
    }
}
