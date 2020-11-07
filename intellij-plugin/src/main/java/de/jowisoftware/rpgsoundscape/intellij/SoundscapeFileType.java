package de.jowisoftware.rpgsoundscape.intellij;


import de.jowisoftware.rpgsoundscape.language.SoundscapeFileTypeStub;
import de.jowisoftware.rpgsoundscape.language.SoundscapeIcons;

import javax.swing.*;

public class SoundscapeFileType extends SoundscapeFileTypeStub {
  public static final SoundscapeFileType INSTANCE = (SoundscapeFileType) SoundscapeFileTypeStub.INSTANCE;

  @Override
  public Icon getIcon() {
    return SoundscapeIcons.FILE;
  }
}
