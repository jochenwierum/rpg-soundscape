// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SMusicEffectDefinition extends PsiElement {

  @NotNull
  List<SMetadataStatement> getMetadataStatementList();

  @Nullable
  SPlayModifications getPlayModifications();

  @NotNull
  SSampleRef getSampleRef();

  @NotNull
  SString getString();

}
