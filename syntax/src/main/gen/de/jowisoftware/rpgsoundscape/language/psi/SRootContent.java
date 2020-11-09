// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SRootContent extends SoundscapeStructureViewPsiElement {

  @NotNull
  List<SIncludableTrackDefinition> getIncludableTrackDefinitionList();

  @NotNull
  List<SIncludeDefinition> getIncludeDefinitionList();

  @NotNull
  List<SLoadDefinition> getLoadDefinitionList();

  @NotNull
  List<SMusicEffectDefinition> getMusicEffectDefinitionList();

  @NotNull
  List<SSoundscapeDefinition> getSoundscapeDefinitionList();

  boolean skipInStructureView();

}
