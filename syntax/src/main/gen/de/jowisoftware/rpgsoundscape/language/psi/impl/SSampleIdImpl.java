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

public class SSampleIdImpl extends SIdImpl implements SSampleId {

  public SSampleIdImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitSampleId(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public String getName() {
    return PsiImplUtil.getName(this);
  }

  @Override
  public PsiElement getNameIdentifier() {
    return PsiImplUtil.getNameIdentifier(this);
  }

  @Override
  public PsiElement setName(String newName) {
    return PsiImplUtil.setName(this, newName);
  }

}
