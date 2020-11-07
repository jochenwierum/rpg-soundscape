package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import de.jowisoftware.rpgsoundscape.language.psi.SId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoundscapeRefactoringSupportProvider extends RefactoringSupportProvider {
  @Override
  public boolean isMemberInplaceRenameAvailable(@NotNull PsiElement elementToRename, @Nullable PsiElement context) {
    return elementToRename instanceof SId;
  }
}
