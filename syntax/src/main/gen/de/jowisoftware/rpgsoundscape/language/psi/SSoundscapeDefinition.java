// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface SSoundscapeDefinition extends SoundscapeStructureViewPsiElement {

  @NotNull
  List<SIncludableTrackDefinition> getIncludableTrackDefinitionList();

  @NotNull
  List<SIncludeSoundscapeDefinition> getIncludeSoundscapeDefinitionList();

  @NotNull
  List<SLoadDefinition> getLoadDefinitionList();

  @NotNull
  List<SMetadataStatement> getMetadataStatementList();

  @Nullable
  SString getString();

  @NotNull
  List<STrackDefinition> getTrackDefinitionList();

  String getName();

  ItemPresentation getPresentation();

}
