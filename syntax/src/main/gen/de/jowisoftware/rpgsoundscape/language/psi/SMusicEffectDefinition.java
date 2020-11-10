// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SMusicEffectDefinition extends SoundscapeStructureViewPsiElement {

  @NotNull
  List<SMetadataStatement> getMetadataStatementList();

  @NotNull
  List<SSampleModification> getSampleModificationList();

  @NotNull
  SSampleRef getSampleRef();

  @NotNull
  SString getString();

  String getName();

  //WARNING: getPresentation(...) is skipped
  //matching getPresentation(SMusicEffectDefinition, ...)
  //methods are not found in PsiImplUtil

  boolean skipInStructureView();

}
