package de.jowisoftware.rpgsoundscape.language.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import de.jowisoftware.rpgsoundscape.language.references.SoundscapeIncludableTrackReference;
import de.jowisoftware.rpgsoundscape.language.references.SoundscapeSampleReference;
import de.jowisoftware.rpgsoundscape.language.references.SoundscapeTrackReference;

import java.time.Duration;

public class PsiImplUtil {
    public static PsiReference getReference(SSampleId element) {
        return PsiImplUtilHelper.getReference(element, SoundscapeSampleReference::new);
    }

    public static PsiReference getReference(SSampleRef element) {
        return PsiImplUtilHelper.getReference(element, SoundscapeSampleReference::new);
    }

    public static PsiReference getReference(STrackId element) {
        return PsiImplUtilHelper.getReference(element, SoundscapeTrackReference::new);
    }

    public static PsiReference getReference(STrackRef element) {
        return PsiImplUtilHelper.getReference(element, SoundscapeTrackReference::new);
    }

    public static PsiReference getReference(SIncludableTrackId element) {
        return PsiImplUtilHelper.getReference(element, SoundscapeIncludableTrackReference::new);
    }

    public static PsiReference getReference(SIncludableTrackRef element) {
        return PsiImplUtilHelper.getReference(element, SoundscapeIncludableTrackReference::new);
    }

    public static int parsed(SPercentage percentage) {
        String value = percentage.getText();
        return Integer.parseInt(value.substring(0, value.length() - 1));
    }

    public static long parsed(SInt ssInt) {
        return Long.parseLong(ssInt.getText());
    }

    public static String parsed(SString string) {
        String s = string.getText();
        return s.substring(1, s.length() - 1);
    }

    public static Duration parsed(STimespan ssTimespan) {
        String value = ssTimespan.getText();

        if (value.endsWith("ms")) {
            return Duration.ofMillis(Long.parseLong(value.substring(0, value.length() - 2)));
        } else if (value.endsWith("s") || value.endsWith("m")) {
            long amount = Long.parseLong(value.substring(0, value.length() - 1));
            return value.endsWith("m") ? Duration.ofMinutes(amount) : Duration.ofSeconds(amount);
        } else {
            throw new IllegalArgumentException("Unknown duration: " + value);
        }
    }

    public static String getName(SId element) {
        return element.getText();
    }

    public static PsiElement getNameIdentifier(SId element) {
        return element;
    }

    public static PsiElement setName(SSampleRef element, String newName) {
        PsiElement newElement = SoundscapeElementFactory.createSampleRef(element.getProject(), newName);
        element.getParent().getNode().replaceChild(element.getNode(), newElement.getNode());
        return newElement;
    }
    public static PsiElement setName(SSampleId element, String newName) {
        PsiElement newElement = SoundscapeElementFactory.createSampleId(element.getProject(), newName);
        element.getParent().getNode().replaceChild(element.getNode(), newElement.getNode());
        return newElement;
    }

    public static PsiElement setName(STrackId element, String newName) {
        PsiElement newElement = SoundscapeElementFactory.createTrackId(element.getProject(), newName);
        element.getParent().getNode().replaceChild(element.getNode(), newElement.getNode());
        return newElement;
    }

    public static PsiElement setName(STrackRef element, String newName) {
        PsiElement newElement = SoundscapeElementFactory.createTrackRef(element.getProject(), newName);
        element.getParent().getNode().replaceChild(element.getNode(), newElement.getNode());
        return newElement;
    }

    public static PsiElement setName(SIncludableTrackId element, String newName) {
        PsiElement newElement = SoundscapeElementFactory.createIncludableTrackId(element.getProject(), newName);
        element.getParent().getNode().replaceChild(element.getNode(), newElement.getNode());
        return newElement;
    }

    public static PsiElement setName(SIncludableTrackRef element, String newName) {
        PsiElement newElement = SoundscapeElementFactory.createIncludableTrackRef(element.getProject(), newName);
        element.getParent().getNode().replaceChild(element.getNode(), newElement.getNode());
        return newElement;
    }
}
