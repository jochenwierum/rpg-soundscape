package de.jowisoftware.rpgsoundscape.intellij;


import javax.swing.*;

public class SoundscapeFileType extends SoundscapeFileTypeStub {
  public static final SoundscapeFileType INSTANCE = new SoundscapeFileType();

  @Override
  public Icon getIcon() {
    return SoundscapeIcons.FILE;
  }
}
