// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface SResumeStatement extends SoundscapeStructureViewPsiElement {

  @Nullable
  SResumeLoopingTracksStatement getResumeLoopingTracksStatement();

  @Nullable
  STrackRef getTrackRef();

  String getName();

  ItemPresentation getPresentation();

}
