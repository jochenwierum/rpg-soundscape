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

public class SRepeatStatementImpl extends ASTWrapperPsiElement implements SRepeatStatement {

  public SRepeatStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitRepeatStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SInt> getIntList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SInt.class);
  }

  @Override
  @Nullable
  public SStatement getStatement() {
    return findChildByClass(SStatement.class);
  }

  @Override
  public boolean skipInStructureView() {
    return PsiImplUtil.skipInStructureView(this);
  }

}
