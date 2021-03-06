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

public class SMetadataStatementImpl extends ASTWrapperPsiElement implements SMetadataStatement {

  public SMetadataStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitMetadataStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SCategoryStatement getCategoryStatement() {
    return findChildByClass(SCategoryStatement.class);
  }

  @Override
  @Nullable
  public SDescribedStatement getDescribedStatement() {
    return findChildByClass(SDescribedStatement.class);
  }

  @Override
  public boolean skipInStructureView() {
    return PsiImplUtil.skipInStructureView(this);
  }

}
