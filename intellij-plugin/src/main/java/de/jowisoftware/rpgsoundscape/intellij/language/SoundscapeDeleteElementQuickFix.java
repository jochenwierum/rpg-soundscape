package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.codeInsight.intention.BaseElementAtCaretIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackId;
import de.jowisoftware.rpgsoundscape.language.psi.SRootContent;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleId;
import de.jowisoftware.rpgsoundscape.language.psi.SSoundscapeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.STrackId;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoundscapeDeleteElementQuickFix extends BaseElementAtCaretIntentionAction {
    public SoundscapeDeleteElementQuickFix(PsiElement element) {
        setText("Delete unused " + getType(element) + " '" + element.getText() + "'");
    }

    private String getType(PsiElement element) {
        if (element instanceof STrackId) {
            return "track";
        } else if (element instanceof SSampleId) {
            return "sample";
        } else if (element instanceof SIncludableTrackId) {
            return "includable track";
        } else {
            return "element";
        }
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "Soundscapes";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        //return element instanceof SId && ReferencesSearch.search(element).findFirst() == null;
        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        PsiElement owner = findOwner(element);
        if (owner != null) {
            PsiElement nextSibling = owner.getNextSibling();
            if (nextSibling.getNode().getElementType().equals(SoundscapeTypes.SEPARATOR)) {
                nextSibling.delete();
            }
            owner.delete();
        }
    }

    public static PsiElement findOwner(@Nullable PsiElement element) {
        while (element != null) {
            if (element instanceof PsiFile) {
                return null;
            }

            PsiElement parent = element.getParent();
            if (parent instanceof SSoundscapeDefinition || parent instanceof SRootContent) {
                return element;
            }

            element = parent;
        }

        return null;
    }
}
