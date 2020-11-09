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

public class SRootContentImpl extends ASTWrapperPsiElement implements SRootContent {

  public SRootContentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitRootContent(this);
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
  public List<SIncludeDefinition> getIncludeDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SIncludeDefinition.class);
  }

  @Override
  @NotNull
  public List<SLoadDefinition> getLoadDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SLoadDefinition.class);
  }

  @Override
  @NotNull
  public List<SMusicEffectDefinition> getMusicEffectDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SMusicEffectDefinition.class);
  }

  @Override
  @NotNull
  public List<SSoundscapeDefinition> getSoundscapeDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SSoundscapeDefinition.class);
  }

  @Override
  public boolean skipInStructureView() {
    return PsiImplUtil.skipInStructureView(this);
  }

}
