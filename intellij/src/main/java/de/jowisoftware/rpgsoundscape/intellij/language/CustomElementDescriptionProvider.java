package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageViewTypeLocation;
import de.jowisoftware.rpgsoundscape.language.psi.SLoadDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomElementDescriptionProvider implements com.intellij.psi.ElementDescriptionProvider {
    @Nullable
    @Override
    public String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location) {
        if (element instanceof SLoadDefinition && location instanceof UsageViewTypeLocation) {
            return "sample";
        }
        return null;
    }
}
