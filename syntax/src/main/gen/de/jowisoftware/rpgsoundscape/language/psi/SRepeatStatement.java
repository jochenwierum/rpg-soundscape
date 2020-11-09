// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SRepeatStatement extends SoundscapeStructureViewPsiElement {

  @NotNull
  List<SInt> getIntList();

  @Nullable
  SStatement getStatement();

  //WARNING: getName(...) is skipped
  //matching getName(SRepeatStatement, ...)
  //methods are not found in PsiImplUtil

  //WARNING: getPresentation(...) is skipped
  //matching getPresentation(SRepeatStatement, ...)
  //methods are not found in PsiImplUtil

  boolean skipInStructureView();

}
