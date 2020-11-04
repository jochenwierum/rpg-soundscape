package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import de.jowisoftware.rpgsoundscape.language.psi.SPlayStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleId;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleRef;
import de.jowisoftware.rpgsoundscape.language.psi.STrackDefinition;
import de.jowisoftware.rpgsoundscape.language.references.ReferenceUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.intellij.lang.annotation.HighlightSeverity.ERROR;

public class SoundscapeAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof SPlayStatement) {
            annotatePlay(((SPlayStatement) element), holder);
        } else if (element instanceof STrackDefinition) {
            annotateTrack(element, holder);
        }

    }

    private void annotateTrack(PsiElement element, AnnotationHolder holder) {

    }

    private void annotatePlay(SPlayStatement element, AnnotationHolder holder) {
        SSampleRef child = PsiTreeUtil.getChildOfType(element, SSampleRef.class);
      if (child == null || child.getText().isEmpty()) {
        return;
      }
        SSampleRef value = element.getSampleRef();

        Optional<SSampleId> matches = ReferenceUtil.findSample(value, value.getText());
        if (matches.isEmpty()) {
            holder.newAnnotation(ERROR, "Unknown sample").range(value.getTextRange())
                    .highlightType(ProblemHighlightType.ERROR)
                    .create();
        }
    }
}
