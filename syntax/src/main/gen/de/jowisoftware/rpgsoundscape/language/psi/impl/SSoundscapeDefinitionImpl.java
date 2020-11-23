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
import com.intellij.navigation.ItemPresentation;

public class SSoundscapeDefinitionImpl extends ASTWrapperPsiElement implements SSoundscapeDefinition {

  public SSoundscapeDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitSoundscapeDefinition(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SIncludableTrackDefinition> getIncludableTrackDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SIncludableTrackDefinition.class);
  }

  @Override
  @NotNull
  public List<SIncludeSoundscapeDefinition> getIncludeSoundscapeDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SIncludeSoundscapeDefinition.class);
  }

  @Override
  @NotNull
  public List<SLoadDefinition> getLoadDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SLoadDefinition.class);
  }

  @Override
  @NotNull
  public List<SMetadataStatement> getMetadataStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SMetadataStatement.class);
  }

  @Override
  @Nullable
  public SString getString() {
    return findChildByClass(SString.class);
  }

  @Override
  @NotNull
  public List<STrackDefinition> getTrackDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, STrackDefinition.class);
  }

  @Override
  public String getName() {
    return PsiImplUtil.getName(this);
  }

  @Override
  public ItemPresentation getPresentation() {
    return PsiImplUtil.getPresentation(this);
  }

}
