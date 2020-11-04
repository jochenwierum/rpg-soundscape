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

public class SRootItemImpl extends ASTWrapperPsiElement implements SRootItem {

  public SRootItemImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitRootItem(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SEffectDefinition getEffectDefinition() {
    return findChildByClass(SEffectDefinition.class);
  }

  @Override
  @Nullable
  public SIncludableTrackDefinition getIncludableTrackDefinition() {
    return findChildByClass(SIncludableTrackDefinition.class);
  }

  @Override
  @Nullable
  public SIncludeDefinition getIncludeDefinition() {
    return findChildByClass(SIncludeDefinition.class);
  }

  @Override
  @Nullable
  public SLoadDefinition getLoadDefinition() {
    return findChildByClass(SLoadDefinition.class);
  }

  @Override
  @Nullable
  public SMusicDefinition getMusicDefinition() {
    return findChildByClass(SMusicDefinition.class);
  }

  @Override
  @Nullable
  public SSoundscapeDefinition getSoundscapeDefinition() {
    return findChildByClass(SSoundscapeDefinition.class);
  }

}
