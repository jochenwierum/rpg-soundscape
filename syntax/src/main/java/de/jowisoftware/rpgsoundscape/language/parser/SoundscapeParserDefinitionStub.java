package de.jowisoftware.rpgsoundscape.language.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import de.jowisoftware.rpgsoundscape.language.SoundscapeFileTypeStub;
import de.jowisoftware.rpgsoundscape.language.SoundscapeLanguage;
import de.jowisoftware.rpgsoundscape.language.lexer.SoundscapeLexerAdapter;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeTokenType;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeTypes;
import org.jetbrains.annotations.NotNull;

public class SoundscapeParserDefinitionStub implements ParserDefinition {
    public static final SoundscapeTokenType LINE_COMMENT = new SoundscapeTokenType("LINE_COMMENT");
    public static final SoundscapeTokenType COMMENT_START = new SoundscapeTokenType("COMMENT_START");
    public static final SoundscapeTokenType COMMENT_CONTENT = new SoundscapeTokenType("COMMENT_CONTENT");
    public static final SoundscapeTokenType COMMENT_END = new SoundscapeTokenType("COMMENT_END");

    public static final IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
    public static final IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;

    public static final TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(LINE_COMMENT, COMMENT_START, COMMENT_CONTENT, COMMENT_END);

    public static final IFileElementType ROOT = new IFileElementType(SoundscapeLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new SoundscapeLexerAdapter();
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    public PsiParser createParser(final Project project) {
        return new SoundscapeParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        /*return SoundscapeElementTypes.FILE;*/
        return ROOT;
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new SoundscapeFile(viewProvider, new SoundscapeFileTypeStub());
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return SoundscapeTypes.Factory.createElement(node);
    }

}
