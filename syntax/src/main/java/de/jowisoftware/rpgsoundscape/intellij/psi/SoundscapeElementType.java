package de.jowisoftware.rpgsoundscape.intellij.psi;

import com.intellij.psi.tree.IElementType;
import de.jowisoftware.rpgsoundscape.intellij.SoundscapeLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class SoundscapeElementType extends IElementType {
  public SoundscapeElementType(@NotNull @NonNls String debugName) {
    super(debugName, SoundscapeLanguage.INSTANCE);
  }
}
