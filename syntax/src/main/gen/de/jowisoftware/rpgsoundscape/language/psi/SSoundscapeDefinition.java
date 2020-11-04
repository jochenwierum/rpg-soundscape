// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SSoundscapeDefinition extends PsiElement {

  @NotNull
  List<SIncludableTrackDefinition> getIncludableTrackDefinitionList();

  @NotNull
  List<SLoadDefinition> getLoadDefinitionList();

  @NotNull
  List<SMetadataStatement> getMetadataStatementList();

  @Nullable
  SString getString();

  @NotNull
  List<STrackDefinition> getTrackDefinitionList();

}
