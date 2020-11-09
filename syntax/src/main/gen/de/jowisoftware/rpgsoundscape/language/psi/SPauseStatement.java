// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface SPauseStatement extends SoundscapeStructureViewPsiElement {

  @Nullable
  SPauseAllOtherTracks getPauseAllOtherTracks();

  @Nullable
  SPauseAllTracks getPauseAllTracks();

  @Nullable
  SPauseThisTrack getPauseThisTrack();

  @Nullable
  STrackRef getTrackRef();

  String getName();

  ItemPresentation getPresentation();

}
