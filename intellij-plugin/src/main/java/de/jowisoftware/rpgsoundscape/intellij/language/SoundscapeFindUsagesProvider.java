package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import de.jowisoftware.rpgsoundscape.language.lexer.SoundscapeLexerAdapter;
import de.jowisoftware.rpgsoundscape.language.parser.SoundscapeParserDefinitionStub;
import de.jowisoftware.rpgsoundscape.language.psi.PsiImplUtil;
import de.jowisoftware.rpgsoundscape.language.psi.SId;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackId;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleId;
import de.jowisoftware.rpgsoundscape.language.psi.STrackId;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeNamedElement;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SoundscapeFindUsagesProvider implements FindUsagesProvider {

  @Nullable
  @Override
  public WordsScanner getWordsScanner() {
    return new DefaultWordsScanner(new SoundscapeLexerAdapter(),
            TokenSet.create(SoundscapeTypes.IDENTIFIER),
            TokenSet.create(
                    SoundscapeParserDefinitionStub.COMMENT_CONTENT,
                    SoundscapeParserDefinitionStub.COMMENT_START,
                    SoundscapeParserDefinitionStub.COMMENT_END,
                    SoundscapeParserDefinitionStub.LINE_COMMENT
                    ),
            TokenSet.EMPTY);
  }

  @Override
  public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
    return psiElement instanceof SoundscapeNamedElement;
  }

  @Nullable
  @Override
  public String getHelpId(@NotNull PsiElement psiElement) {
    return null;
  }

  @NotNull
  @Override
  public String getType(@NotNull PsiElement element) {
    if (element instanceof STrackId) {
      return "track";
    }else if (element instanceof SIncludableTrackId) {
      return "includable track";
    }else if (element instanceof SSampleId) {
      return "sample";
    } else {
      return "";
    }
  }

  @NotNull
  @Override
  public String getDescriptiveName(@NotNull PsiElement element) {
    if (element instanceof SId) {
      return Objects.requireNonNullElse(PsiImplUtil.getName(((SId) element)),"");
    }
    return "";
  }

  @NotNull
  @Override
  public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
    if (element instanceof SId) {
      return element.getText();
    } else {
      return "";
    }
  }
}
