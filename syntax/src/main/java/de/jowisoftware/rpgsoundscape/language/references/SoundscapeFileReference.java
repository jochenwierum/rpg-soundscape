package de.jowisoftware.rpgsoundscape.language.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.IncorrectOperationException;
import de.jowisoftware.rpgsoundscape.language.psi.SFilename;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SoundscapeFileReference extends AbstractReference<SFilename> {
    public SoundscapeFileReference(@NotNull SFilename element, TextRange textRange) {
        super(element, textRange);
    }

    @Override
    public PsiElement resolve() {
        return Optional.ofNullable(myElement.getContainingFile())
                .map(PsiFile::getVirtualFile)
                .map(vf -> myElement.getContainingFile().getVirtualFile().findFileByRelativePath("../" + myElement.parsed()))
                .map(vf -> (SoundscapeFile) PsiManager.getInstance(myElement.getProject()).findFile(vf))
                .orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
//        return PsiImplUtil.setName(myElement, newElementName);
        return null;
    }
}
