package de.jowisoftware.rpgsoundscape.intellij.psi;

import com.intellij.psi.tree.IElementType;
import de.jowisoftware.rpgsoundscape.intellij.SoundscapeLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class SoundscapeTokenType extends IElementType {
  public SoundscapeTokenType(@NotNull @NonNls String debugName) {
    super(debugName, SoundscapeLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "TokenType." + super.toString();
  }
}
