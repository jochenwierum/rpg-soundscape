package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.searches.ReferencesSearch;
import de.jowisoftware.rpgsoundscape.language.psi.SFilename;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackId;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackRef;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleId;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleRef;
import de.jowisoftware.rpgsoundscape.language.psi.STrackRef;
import de.jowisoftware.rpgsoundscape.language.references.ReferenceUtil;
import org.jetbrains.annotations.NotNull;

import static com.intellij.lang.annotation.HighlightSeverity.ERROR;
import static com.intellij.lang.annotation.HighlightSeverity.WEAK_WARNING;

public class SoundscapeAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof SFilename) {
            annotateFilename((SFilename) element, holder);
        } else if (element instanceof SSampleId) {
            annotateUnused(element, holder, "sample");
        } else if (element instanceof SIncludableTrackId) {
            annotateUnused(element, holder, "includable track");
        } else if (element instanceof SSampleRef) {
            annotatePlay(((SSampleRef) element), holder);
        } else if (element instanceof STrackRef) {
            annotateTrack(((STrackRef) element), holder);
        } else if (element instanceof SIncludableTrackRef) {
            annotateIncludableTrack(((SIncludableTrackRef) element), holder);
        }
    }

    private void annotateUnused(PsiElement element, AnnotationHolder holder, String type) {
        if (ReferencesSearch.search(element).findFirst() == null) {
            holder.newAnnotation(WEAK_WARNING, type + " is unused")
                    .range(element.getTextRange())
                    .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                    .withFix(new SoundscapeDeleteElementQuickFix(element))
                    .create();
        }
    }

    private void annotateTrack(STrackRef element, AnnotationHolder holder) {
        if (ReferenceUtil.findTrack(element, element.getText()).isEmpty()) {
            holder.newAnnotation(ERROR, "Unknown track")
                    .range(element.getTextRange())
                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                    .create();
        }
    }

    private void annotateIncludableTrack(SIncludableTrackRef element, AnnotationHolder holder) {
        if (ReferenceUtil.findIncludableTrack(element, element.getText()).isEmpty()) {
            holder.newAnnotation(ERROR, "Unknown includable track")
                    .range(element.getTextRange())
                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                    .create();
        }
    }

    private void annotatePlay(SSampleRef element, AnnotationHolder holder) {
        if (ReferenceUtil.findSample(element, element.getText()).isEmpty()) {
            holder.newAnnotation(ERROR, "Unknown sample")
                    .range(element.getTextRange())
                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                    .create();
        }
    }

    private void annotateFilename(SFilename element, AnnotationHolder holder) {
        if (element.getReference().resolve() == null) {
            holder.newAnnotation(ERROR, "Unknown file")
                    .range(element.getTextRange())
                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                    .create();
        }
    }
}
