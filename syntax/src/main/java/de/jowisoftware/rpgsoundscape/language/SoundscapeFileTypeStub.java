package de.jowisoftware.rpgsoundscape.language;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SoundscapeFileTypeStub extends LanguageFileType {
    public static SoundscapeFileTypeStub INSTANCE;

    static {
        SoundscapeFileTypeStub instance;
        try {
            instance = (SoundscapeFileTypeStub) SoundscapeFileTypeStub.class.getClassLoader()
                    .loadClass("de.jowisoftware.rpgsoundscape.intellij.SoundscapeFileType")
                    .getConstructor()
                    .newInstance();
        } catch (Exception e) {
            instance = new SoundscapeFileTypeStub();
        }
        INSTANCE = instance;
    }

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
