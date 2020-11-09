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

public class STrackDefinitionImpl extends ASTWrapperPsiElement implements STrackDefinition {

  public STrackDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitTrackDefinition(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SLoopingTrackModifier getLoopingTrackModifier() {
    return findChildByClass(SLoopingTrackModifier.class);
  }

  @Override
  @Nullable
  public SManualTrackModifier getManualTrackModifier() {
    return findChildByClass(SManualTrackModifier.class);
  }

  @Override
  @Nullable
  public SString getString() {
    return findChildByClass(SString.class);
  }

  @Override
  @Nullable
  public STrackContent getTrackContent() {
    return findChildByClass(STrackContent.class);
  }

  @Override
  @Nullable
  public STrackId getTrackId() {
    return findChildByClass(STrackId.class);
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
