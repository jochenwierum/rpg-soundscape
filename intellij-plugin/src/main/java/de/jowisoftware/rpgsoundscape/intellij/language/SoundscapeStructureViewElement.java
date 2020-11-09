package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import de.jowisoftware.rpgsoundscape.language.psi.SDoNothingStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeStructureViewPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public class SoundscapeStructureViewElement
        implements StructureViewTreeElement, SortableTreeElement {
    private final SoundscapeStructureViewPsiElement myElement;

    public SoundscapeStructureViewElement(SoundscapeStructureViewPsiElement element) {
        this.myElement = element;
    }

    @Override
    public Object getValue() {
        return myElement;
    }

    @Override
    public void navigate(boolean requestFocus) {
        myElement.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return myElement.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return myElement.canNavigateToSource();
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        return Objects.requireNonNullElse(myElement.getName(), "");
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        ItemPresentation presentation = myElement.getPresentation();
        return presentation != null ? presentation :
                new PresentationData("Unknown element: " + myElement.getClass().getSimpleName(),null,null,null);
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        return myElement.getStructureViewChildren()
                .filter(Predicate.not(SDoNothingStatement.class::isInstance))
                .map(SoundscapeStructureViewElement::new)
                .toArray(TreeElement[]::new);
    }
}
