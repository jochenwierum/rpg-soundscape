package de.jowisoftware.rpgsoundscape.language.references;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import de.jowisoftware.rpgsoundscape.language.SoundscapeIcons;
import de.jowisoftware.rpgsoundscape.language.psi.PsiImplUtil;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackId;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackRef;
import org.jetbrains.annotations.NotNull;

public class SoundscapeIncludableTrackReference extends AbstractReference<SIncludableTrackRef> {
    public SoundscapeIncludableTrackReference(@NotNull SIncludableTrackRef element, TextRange textRange) {
        super(element, textRange);
    }

    @Override
    public PsiElement resolve() {
        return ReferenceUtil.findIncludableTrack(myElement, name).orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return ReferenceUtil.findIncludableTracks(myElement)
                .filter(id -> id.getTextLength() > 0)
                .map(definition -> LookupElementBuilder
                        .create(definition)
                        .withIcon(SoundscapeIcons.INCLUDABLE_TRACK)
                        .withTypeText(definition.getContainingFile().getName())
                )
                .toArray(Object[]::new);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return myElement instanceof SIncludableTrackId
                ? PsiImplUtil.setName(((SIncludableTrackId) myElement), newElementName)
                : PsiImplUtil.setName(myElement, newElementName);
    }
}
