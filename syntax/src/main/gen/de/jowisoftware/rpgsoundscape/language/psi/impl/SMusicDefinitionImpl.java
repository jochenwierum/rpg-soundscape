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

public class SMusicDefinitionImpl extends SMusicEffectDefinitionImpl implements SMusicDefinition {

  public SMusicDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitMusicDefinition(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SSampleRef getSampleRef() {
    return findChildByClass(SSampleRef.class);
  }

  @Override
  @Nullable
  public SString getString() {
    return findChildByClass(SString.class);
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
