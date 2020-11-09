// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface STrackDefinition extends SoundscapeStructureViewPsiElement {

  @Nullable
  SLoopingTrackModifier getLoopingTrackModifier();

  @Nullable
  SManualTrackModifier getManualTrackModifier();

  @Nullable
  SString getString();

  @Nullable
  STrackContent getTrackContent();

  @Nullable
  STrackId getTrackId();

  String getName();

  ItemPresentation getPresentation();

}
