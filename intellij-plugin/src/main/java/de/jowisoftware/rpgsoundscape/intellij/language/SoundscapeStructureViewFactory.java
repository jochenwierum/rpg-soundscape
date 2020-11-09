package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;
import org.jetbrains.annotations.NotNull;

public class SoundscapeStructureViewFactory implements PsiStructureViewFactory {

    @Override
    public StructureViewBuilder getStructureViewBuilder(@NotNull final PsiFile psiFile) {
        if (!(psiFile instanceof SoundscapeFile)) {
            return null;
        }

        return new TreeBasedStructureViewBuilder() {
            @NotNull
            @Override
            public StructureViewModel createStructureViewModel(Editor editor) {
                return new SoundscapeStructureViewModel((SoundscapeFile) psiFile);
            }
        };
    }
}
