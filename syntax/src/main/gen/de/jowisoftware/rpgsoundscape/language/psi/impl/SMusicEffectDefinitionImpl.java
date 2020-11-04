// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static de.jowisoftware.rpgsoundscape.language.psi.SoundscapeTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import de.jowisoftware.rpgsoundscape.language.psi.*;

public class SMusicEffectDefinitionImpl extends ASTWrapperPsiElement implements SMusicEffectDefinition {

  public SMusicEffectDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitMusicEffectDefinition(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SMetadataStatement> getMetadataStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SMetadataStatement.class);
  }

  @Override
  @Nullable
  public SPlayModifications getPlayModifications() {
    return findChildByClass(SPlayModifications.class);
  }

  @Override
  @NotNull
  public SSampleRef getSampleRef() {
    return findNotNullChildByClass(SSampleRef.class);
  }

  @Override
  @NotNull
  public SString getString() {
    return findNotNullChildByClass(SString.class);
  }

}
