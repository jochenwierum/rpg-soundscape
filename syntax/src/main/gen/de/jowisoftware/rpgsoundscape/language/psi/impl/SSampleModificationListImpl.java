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

public class SSampleModificationListImpl extends ASTWrapperPsiElement implements SSampleModificationList {

  public SSampleModificationListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitSampleModificationList(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SSampleModification> getSampleModificationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SSampleModification.class);
  }

}