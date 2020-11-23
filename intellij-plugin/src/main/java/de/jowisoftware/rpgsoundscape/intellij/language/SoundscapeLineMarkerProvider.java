package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiElement;
import de.jowisoftware.rpgsoundscape.language.SoundscapeIcons;
import de.jowisoftware.rpgsoundscape.language.psi.SEffectDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableSoundscapeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableSoundscapeId;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableSoundscapeRef;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackId;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackRef;
import de.jowisoftware.rpgsoundscape.language.psi.SMusicDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleId;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleRef;
import de.jowisoftware.rpgsoundscape.language.psi.SSoundscapeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.STrackId;
import de.jowisoftware.rpgsoundscape.language.psi.STrackRef;
import de.jowisoftware.rpgsoundscape.language.references.ReferenceUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;

public class SoundscapeLineMarkerProvider extends RelatedItemLineMarkerProvider {
    @Override
    protected void collectNavigationMarkers(
            @NotNull PsiElement element,
            @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        NavigationGutterIconBuilder<PsiElement> marker = createMarker(element);
        if (marker != null) {
            result.add(marker.createLineMarkerInfo(element.getFirstChild()));
        }
    }

    private NavigationGutterIconBuilder<PsiElement> createMarker(PsiElement element) {
        if (element instanceof SIncludableSoundscapeId) {
            return createNewIncludableSoundscapeMarker();
        } else if (element instanceof SSoundscapeDefinition && !(element instanceof SIncludableSoundscapeDefinition)) {
            return createNewSoundscapeMarker();
        } else if (element instanceof SSampleId) {
            return createNewSampleMarker();
        } else if (element instanceof STrackId) {
            return createNewTrackMarker();
        } else if (element instanceof SIncludableTrackId) {
            return createNewIncludableTrackMarker();
        } else if (element instanceof SSampleRef) {
            return createMarkers(((SSampleRef) element));
        } else if (element instanceof STrackRef) {
            return createMarkers(((STrackRef) element));
        } else if (element instanceof SIncludableTrackRef) {
            return createMarker(((SIncludableTrackRef) element));
        } else if (element instanceof SIncludableSoundscapeRef) {
            return createMarker((SIncludableSoundscapeRef) element);
        } else if (element instanceof SMusicDefinition) {
            return createNewMusicMarker();
        } else if (element instanceof SEffectDefinition) {
            return createNewEffectMarker();
        }

        return null;
    }

    private NavigationGutterIconBuilder<PsiElement> createNewIncludableSoundscapeMarker() {
        return createIdMarker(SoundscapeIcons.NEW_INCLUDABLE_SOUNDSCAPE);
    }

    private NavigationGutterIconBuilder<PsiElement> createNewSampleMarker() {
        return createIdMarker(SoundscapeIcons.NEW_SAMPLE);
    }

    private NavigationGutterIconBuilder<PsiElement> createMarkers(SSampleRef element) {
        return createMarker(element, ReferenceUtil::findSample,
                SoundscapeIcons.SAMPLE, "Navigate to sample definition");
    }

    private NavigationGutterIconBuilder<PsiElement> createNewTrackMarker() {
        return createIdMarker(SoundscapeIcons.NEW_TRACK);
    }

    private NavigationGutterIconBuilder<PsiElement> createMarkers(STrackRef element) {
        return createMarker(element, ReferenceUtil::findTrack,
                SoundscapeIcons.TRACK, "Navigate to track definition");
    }

    private NavigationGutterIconBuilder<PsiElement> createNewIncludableTrackMarker() {
        return createIdMarker(SoundscapeIcons.NEW_INCLUDABLE_TRACK);
    }

    private NavigationGutterIconBuilder<PsiElement> createMarker(SIncludableTrackRef element) {
        return createMarker(element, ReferenceUtil::findIncludableTrack,
                SoundscapeIcons.INCLUDABLE_TRACK, "Navigate to includable track definition");
    }

    private NavigationGutterIconBuilder<PsiElement> createMarker(SIncludableSoundscapeRef element) {
        return createMarker(element, ReferenceUtil::findIncludableSoundscape,
                SoundscapeIcons.INCLUDABLE_TRACK, "Navigate to includable soundscape definition");
    }

    private NavigationGutterIconBuilder<PsiElement> createNewMusicMarker() {
        return createIdMarker(SoundscapeIcons.NEW_MUSIC);
    }

    private NavigationGutterIconBuilder<PsiElement> createNewEffectMarker() {
        return createIdMarker(SoundscapeIcons.NEW_EFFECT);
    }

    private NavigationGutterIconBuilder<PsiElement> createNewSoundscapeMarker() {
        return createIdMarker(SoundscapeIcons.NEW_SOUNDSCAPE);
    }

    private <T extends PsiElement> NavigationGutterIconBuilder<PsiElement> createMarker(
            T element, BiFunction<T, String, Optional<? extends PsiElement>> finder,
            Icon icon, String tooltipText) {

        return finder.apply(element, element.getText())
                .map(definition ->
                        NavigationGutterIconBuilder
                                .create(icon)
                                .setTarget(definition.getParent())
                                .setTooltipText(tooltipText)
                )
                .orElse(null);
    }

    private NavigationGutterIconBuilder<PsiElement> createIdMarker(Icon icon) {
        return NavigationGutterIconBuilder.create(icon).setTargets();
    }
}
