// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static de.jowisoftware.rpgsoundscape.language.psi.SoundscapeTypes.*;
import de.jowisoftware.rpgsoundscape.language.psi.*;
import com.intellij.navigation.ItemPresentation;

public class SIncludableSoundscapeDefinitionImpl extends SSoundscapeDefinitionImpl implements SIncludableSoundscapeDefinition {

  public SIncludableSoundscapeDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitIncludableSoundscapeDefinition(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SIncludableSoundscapeId getIncludableSoundscapeId() {
    return findChildByClass(SIncludableSoundscapeId.class);
  }

  @Override
  public SString getString() {
    return PsiImplUtil.getString(this);
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
