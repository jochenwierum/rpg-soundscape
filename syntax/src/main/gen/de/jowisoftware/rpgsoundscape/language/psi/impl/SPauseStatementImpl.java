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

public class SPauseStatementImpl extends ASTWrapperPsiElement implements SPauseStatement {

  public SPauseStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SVisitor visitor) {
    visitor.visitPauseStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SVisitor) accept((SVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SPauseAllOtherTracks getPauseAllOtherTracks() {
    return findChildByClass(SPauseAllOtherTracks.class);
  }

  @Override
  @Nullable
  public SPauseAllTracks getPauseAllTracks() {
    return findChildByClass(SPauseAllTracks.class);
  }

  @Override
  @Nullable
  public SPauseThisTrack getPauseThisTrack() {
    return findChildByClass(SPauseThisTrack.class);
  }

  @Override
  @Nullable
  public STrackRef getTrackRef() {
    return findChildByClass(STrackRef.class);
  }

}
