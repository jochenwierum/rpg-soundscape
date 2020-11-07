package de.jowisoftware.rpgsoundscape.language.references;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import de.jowisoftware.rpgsoundscape.language.SoundscapeIcons;
import de.jowisoftware.rpgsoundscape.language.psi.PsiImplUtil;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleRef;
import org.jetbrains.annotations.NotNull;

public class SoundscapeSampleReference extends AbstractReference<SSampleRef> {
    public SoundscapeSampleReference(@NotNull SSampleRef element, TextRange textRange) {
        super(element, textRange);
    }

    @Override
    public PsiElement resolve() {
        return ReferenceUtil.findSample(myElement, name)
                .orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return ReferenceUtil.findSamples(myElement)
                .filter(id -> id.getTextLength() > 0)
                .map(definition -> LookupElementBuilder
                        .create(definition)
                        .withIcon(SoundscapeIcons.SAMPLE)
                        .withTypeText(definition.getContainingFile().getName())
                )
                .toArray(Object[]::new);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return PsiImplUtil.setName(myElement, newElementName);
    }
}
