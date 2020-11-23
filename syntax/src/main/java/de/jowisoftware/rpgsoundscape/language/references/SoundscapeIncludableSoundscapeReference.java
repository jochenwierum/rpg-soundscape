package de.jowisoftware.rpgsoundscape.language.references;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import de.jowisoftware.rpgsoundscape.language.SoundscapeIcons;
import de.jowisoftware.rpgsoundscape.language.psi.PsiImplUtil;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableSoundscapeRef;
import org.jetbrains.annotations.NotNull;

public class SoundscapeIncludableSoundscapeReference extends AbstractReference<SIncludableSoundscapeRef> {
    public SoundscapeIncludableSoundscapeReference(@NotNull SIncludableSoundscapeRef element, TextRange textRange) {
        super(element, textRange);
    }

    @Override
    public PsiElement resolve() {
        return ReferenceUtil.findIncludableSoundscape(myElement, name).orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return ReferenceUtil.findIncludableSoundscapes(myElement)
                .filter(id -> id.getTextLength() > 0)
                .map(definition -> LookupElementBuilder
                        .create(definition)
                        .withIcon(SoundscapeIcons.INCLUDABLE_SOUNDSCAPE)
                        .withTypeText(definition.getContainingFile().getName())
                )
                .toArray(Object[]::new);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return PsiImplUtil.setName(myElement, newElementName);
    }
}
