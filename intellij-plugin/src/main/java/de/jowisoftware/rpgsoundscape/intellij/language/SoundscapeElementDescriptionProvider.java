package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageViewTypeLocation;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableSoundscapeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableSoundscapeId;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackId;
import de.jowisoftware.rpgsoundscape.language.psi.SLoadDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleId;
import de.jowisoftware.rpgsoundscape.language.psi.STrackDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.STrackId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoundscapeElementDescriptionProvider implements com.intellij.psi.ElementDescriptionProvider {
    @Nullable
    @Override
    public String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location) {
        if (location instanceof UsageViewTypeLocation) {
            if (element instanceof SLoadDefinition || element instanceof SSampleId) {
                return "sample";
            } else if (element instanceof SIncludableTrackDefinition || element instanceof SIncludableTrackId) {
                return "includable track";
            } else if (element instanceof STrackDefinition || element instanceof STrackId) {
                return "track";
            } else if (element instanceof SIncludableSoundscapeDefinition || element instanceof SIncludableSoundscapeId) {
                return "includable soundscape";
            }
        }
        return null;
    }
}
