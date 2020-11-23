package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import de.jowisoftware.rpgsoundscape.language.psi.SCategoryStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SDescribedStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludeTrackStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SLoadDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SPauseStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SPlayStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SResumeStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SSleepStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SSoundscapeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;
import org.jetbrains.annotations.NotNull;

public class SoundscapeStructureViewModel
        extends StructureViewModelBase
        implements StructureViewModel.ElementInfoProvider {
    public SoundscapeStructureViewModel(@NotNull SoundscapeFile psiFile) {
        super(psiFile, new SoundscapeStructureViewElement(psiFile));
    }

    @Override
    public @NotNull Sorter[] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }


    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        Object value = element.getValue();
        return value instanceof SoundscapeFile
                || value instanceof SSoundscapeDefinition;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        Object value = element.getValue();
        return value instanceof SLoadDefinition
                || value instanceof SPlayStatement
                || value instanceof SPauseStatement
                || value instanceof SResumeStatement
                || value instanceof SSleepStatement
                || value instanceof SIncludeTrackStatement
                || value instanceof SIncludeDefinition
                || value instanceof SCategoryStatement
                || value instanceof SDescribedStatement
                ;
    }
}
