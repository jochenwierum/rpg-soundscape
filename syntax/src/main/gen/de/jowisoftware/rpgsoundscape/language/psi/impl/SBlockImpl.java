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

public class SBlockImpl extends ASTWrapperPsiElement implements SBlock {

  public SBlockImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitBlock(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SStatement> getStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SStatement.class);
  }

  @Override
  public String getName() {
    return PsiImplUtil.getName(this);
  }

  @Override
  public ItemPresentation getPresentation() {
    return PsiImplUtil.getPresentation(this);
  }

  @Override
  public boolean skipInStructureView() {
    return PsiImplUtil.skipInStructureView(this);
  }

}
