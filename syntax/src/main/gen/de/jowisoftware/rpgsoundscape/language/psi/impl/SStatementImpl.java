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

public class SStatementImpl extends ASTWrapperPsiElement implements SStatement {

  public SStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SBlock getBlock() {
    return findChildByClass(SBlock.class);
  }

  @Override
  @Nullable
  public SDoNothingStatement getDoNothingStatement() {
    return findChildByClass(SDoNothingStatement.class);
  }

  @Override
  @Nullable
  public SParallellyStatement getParallellyStatement() {
    return findChildByClass(SParallellyStatement.class);
  }

  @Override
  @Nullable
  public SPauseStatement getPauseStatement() {
    return findChildByClass(SPauseStatement.class);
  }

  @Override
  @Nullable
  public SPlayStatement getPlayStatement() {
    return findChildByClass(SPlayStatement.class);
  }

  @Override
  @Nullable
  public SRandomlyStatement getRandomlyStatement() {
    return findChildByClass(SRandomlyStatement.class);
  }

  @Override
  @Nullable
  public SRepeatStatement getRepeatStatement() {
    return findChildByClass(SRepeatStatement.class);
  }

  @Override
  @Nullable
  public SResumeStatement getResumeStatement() {
    return findChildByClass(SResumeStatement.class);
  }

  @Override
  @Nullable
  public SSleepStatement getSleepStatement() {
    return findChildByClass(SSleepStatement.class);
  }

}
