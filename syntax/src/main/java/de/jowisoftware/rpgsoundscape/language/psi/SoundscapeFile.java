package de.jowisoftware.rpgsoundscape.language.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import de.jowisoftware.rpgsoundscape.language.SoundscapeFileTypeStub;
import de.jowisoftware.rpgsoundscape.language.SoundscapeLanguage;
import org.jetbrains.annotations.NotNull;

public class SoundscapeFile extends PsiFileBase implements SoundscapeStructureViewPsiElement {
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
