// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SStatement extends PsiElement {

  @Nullable
  SBlock getBlock();

  @Nullable
  SDoNothingStatement getDoNothingStatement();

  @Nullable
  SParallellyStatement getParallellyStatement();

  @Nullable
  SPauseStatement getPauseStatement();

  @Nullable
  SPlayStatement getPlayStatement();

  @Nullable
  SRandomlyStatement getRandomlyStatement();

  @Nullable
  SRepeatStatement getRepeatStatement();

  @Nullable
  SResumeStatement getResumeStatement();

  @Nullable
  SSleepStatement getSleepStatement();

}
