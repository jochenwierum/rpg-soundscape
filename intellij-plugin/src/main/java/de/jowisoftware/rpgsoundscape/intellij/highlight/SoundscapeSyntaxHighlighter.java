package de.jowisoftware.rpgsoundscape.intellij.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import de.jowisoftware.rpgsoundscape.intellij.SoundscapeParserDefinition;
import de.jowisoftware.rpgsoundscape.language.lexer.SoundscapeLexerAdapter;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SoundscapeSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey BRACES =
            createTextAttributesKey("SOUNDSCAPE_BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey IDENTIFIER =
            createTextAttributesKey("SOUNDSCAPE_IDENTIFIER_KEY", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    public static final TextAttributesKey SEMICOLON =
            createTextAttributesKey("SOUNDSCAPE_SEMICOLON_KEY", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey STRING =
            createTextAttributesKey("SOUNDSCAPE_STRING_KEY", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER =
            createTextAttributesKey("SOUNDSCAPE_NUMBER_KEY", DefaultLanguageHighlighterColors.CONSTANT);
    public static final TextAttributesKey NUMBER_WITH_UNIT =
            createTextAttributesKey("SOUNDSCAPE_NUMBER_WITH_UNIT_KEY", DefaultLanguageHighlighterColors.CONSTANT);
    public static final TextAttributesKey COMMA =
            createTextAttributesKey("SOUNDSCAPE_COMMA_KEY", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("SOUNDSCAPE_KEYWORD_VALUE", DefaultLanguageHighlighterColors.FUNCTION_CALL);
    public static final TextAttributesKey SECTION =
            createTextAttributesKey("SOUNDSCAPE_SECTION_VALUE", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey DETAIL =
            createTextAttributesKey("SOUNDSCAPE_DETAIL_VALUE", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);
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
            entries(IDENTIFIER, SoundscapeTypes.IDENTIFIER),
            entries(STRING, SoundscapeTypes.STRING, SoundscapeTypes.TEXT),
            entries(NUMBER, SoundscapeTypes.INT, SoundscapeTypes.NUM_INTEGER),
            entries(NUMBER_WITH_UNIT, SoundscapeTypes.DURATION, SoundscapeTypes.PERCENTAGE),
            entries(BRACES, SoundscapeTypes.CURLY_L, SoundscapeTypes.CURLY_R),
            entries(SECTION,
                    SoundscapeTypes.CATEGORIZED,
                    SoundscapeTypes.DESCRIBED,
                    SoundscapeTypes.INCLUDE,
                    SoundscapeTypes.EFFECT,
                    SoundscapeTypes.MUSIC,
                    SoundscapeTypes.SOUNDSCAPE,
                    SoundscapeTypes.TRACK,
                    SoundscapeTypes.LOAD
            ),
            entries(KEYWORD,
                    SoundscapeTypes.PLAY,
                    SoundscapeTypes.SLEEP,
                    SoundscapeTypes.PAUSE,
                    SoundscapeTypes.RESUME,
                    SoundscapeTypes.REPEAT,
                    SoundscapeTypes.RANDOMLY,
                    SoundscapeTypes.PARALLELLY,
                    SoundscapeTypes.WEIGHTED
            ),
            entries(COMMA,
                    SoundscapeTypes.AND,
                    SoundscapeTypes.LIST_DELIMITER
            ),
            entries(SEMICOLON,
                    SoundscapeTypes.SEPARATOR
            ),
            entries(DETAIL,
                    SoundscapeTypes.ALL,
                    SoundscapeTypes.AMPLIFICATION,
                    SoundscapeTypes.AS,
                    SoundscapeTypes.ATTRIBUTION,
                    SoundscapeTypes.AUTOSTARTING,
                    SoundscapeTypes.BETWEEN,
                    SoundscapeTypes.BY,
                    SoundscapeTypes.DO,
                    SoundscapeTypes.FIRST,
                    SoundscapeTypes.FROM,
                    SoundscapeTypes.IN,
                    SoundscapeTypes.INCLUDABLE,
                    SoundscapeTypes.INCLUDES,
                    SoundscapeTypes.LIMIT,
                    SoundscapeTypes.LOOPING,
                    SoundscapeTypes.MANUAL,
                    SoundscapeTypes.NOTHING,
                    SoundscapeTypes.OF,
                    SoundscapeTypes.OMISSION,
                    SoundscapeTypes.OTHER,
                    SoundscapeTypes.PAUSED,
                    SoundscapeTypes.SAMPLE,
                    SoundscapeTypes.THIS,
                    SoundscapeTypes.TIMES,
                    SoundscapeTypes.TITLE,
                    SoundscapeTypes.TO,
                    SoundscapeTypes.TRACKS,
                    SoundscapeTypes.WITH
            )
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
