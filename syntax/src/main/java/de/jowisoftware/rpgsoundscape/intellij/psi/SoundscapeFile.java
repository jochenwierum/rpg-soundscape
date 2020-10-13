package de.jowisoftware.rpgsoundscape.intellij.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import de.jowisoftware.rpgsoundscape.intellij.SoundscapeFileTypeStub;
import de.jowisoftware.rpgsoundscape.intellij.SoundscapeLanguage;
import org.jetbrains.annotations.NotNull;

public class SoundscapeFile extends PsiFileBase {
    private final SoundscapeFileTypeStub fileType;

    public SoundscapeFile(FileViewProvider viewProvider, SoundscapeFileTypeStub fileType) {
        super(viewProvider, SoundscapeLanguage.INSTANCE);
        this.fileType = fileType;
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return fileType;
    }
}
