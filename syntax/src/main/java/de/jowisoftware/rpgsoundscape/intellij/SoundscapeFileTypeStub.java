package de.jowisoftware.rpgsoundscape.intellij;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SoundscapeFileTypeStub extends LanguageFileType {
    public SoundscapeFileTypeStub() {
        super(SoundscapeLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Soundscape";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Soundscape";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "soundscape";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getCharset(@NotNull VirtualFile file, @NotNull byte[] content) {
        return "UTF-8";
    }
}
