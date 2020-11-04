// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SPauseStatement extends PsiElement {

  @Nullable
  SPauseAllOtherTracks getPauseAllOtherTracks();

  @Nullable
  SPauseAllTracks getPauseAllTracks();

  @Nullable
  SPauseThisTrack getPauseThisTrack();

  @Nullable
  STrackRef getTrackRef();

}
