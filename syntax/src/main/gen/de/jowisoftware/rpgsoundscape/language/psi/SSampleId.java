// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

public interface SSampleId extends SSampleRef, SoundscapeNamedElement {

  PsiReference getReference();

  String getName();

  PsiElement getNameIdentifier();

  PsiElement setName(String newName);

}
