// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SRootItem extends PsiElement {

  @Nullable
  SEffectDefinition getEffectDefinition();

  @Nullable
  SIncludableTrackDefinition getIncludableTrackDefinition();

  @Nullable
  SIncludeDefinition getIncludeDefinition();

  @Nullable
  SLoadDefinition getLoadDefinition();

  @Nullable
  SMusicDefinition getMusicDefinition();

  @Nullable
  SSoundscapeDefinition getSoundscapeDefinition();

}
