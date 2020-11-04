package de.jowisoftware.rpgsoundscape.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeNamedElement;

public abstract class SoundscapeNamedElementImpl extends ASTWrapperPsiElement implements SoundscapeNamedElement {
  public SoundscapeNamedElementImpl(ASTNode node) {
    super(node);
  }
}
