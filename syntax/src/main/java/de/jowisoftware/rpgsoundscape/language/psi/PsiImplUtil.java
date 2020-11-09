package de.jowisoftware.rpgsoundscape.language.psi;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import de.jowisoftware.rpgsoundscape.language.SoundscapeIcons;
import de.jowisoftware.rpgsoundscape.language.references.SoundscapeIncludableTrackReference;
import de.jowisoftware.rpgsoundscape.language.references.SoundscapeSampleReference;
import de.jowisoftware.rpgsoundscape.language.references.SoundscapeTrackReference;

import java.time.Duration;
import java.util.List;

public class PsiImplUtil {
    public static PsiReference getReference(SSampleRef element) {
        return PsiImplUtilHelper.getReference(element, SoundscapeSampleReference::new);
    }

    public static PsiReference getReference(STrackRef element) {
        return PsiImplUtilHelper.getReference(element, SoundscapeTrackReference::new);
    }

    public static PsiReference getReference(SIncludableTrackRef element) {
        return PsiImplUtilHelper.getReference(element, SoundscapeIncludableTrackReference::new);
    }

    public static PsiReference getReference(SFilename element) {
        if (element.getTextLength() <= 2) {
            return null;
        }

        return new FileReferenceSet(element.parsed(), element, 1, null, true, true)
                .getLastReference();
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

    public static String getName(SSoundscapeDefinition element) {
        SString string = element.getString();
        return string == null ? "(unknown)" : string.parsed();
    }

    public static String getName(SMusicEffectDefinition element) {
        SString string = element.getString();
        return string.parsed();
    }

    public static String getName(SLoadDefinition element) {
        SSampleId id = element.getSampleId();
        return id == null ? "(unknown)" : id.getText();
    }

    public static String getName(SIncludableTrackDefinition element) {
        SIncludableTrackId id = element.getIncludableTrackId();
        return id == null ? "(unknown)" : id.getText();
    }

    public static String getName(STrackDefinition element) {
        STrackId id = element.getTrackId();
        return id == null ? "(unknown)" : id.getText();
    }

    public static String getName(SIncludeDefinition element) {
        SFilename filename = element.getFilename();
        return filename == null ? "(unknown)" : filename.parsed();
    }

    public static String getName(SPlayStatement element) {
        SSampleRef sample = element.getSampleRef();
        return sample == null ? "(unknown)" : sample.getText();
    }

    public static String getName(SSleepStatement element) {
        List<STimespan> timespanList = element.getTimespanList();

        String text = "(unknown)";
        if (timespanList.size() >= 1) {
            text = element.getTimespanList().get(0).getText();
        }
        if (timespanList.size() >= 2) {
            text += "-" + element.getTimespanList().get(1).getText();
        }

        return "Sleep " + text;
    }

    public static String getName(SDescribedStatement element) {
        SString string = element.getString();
        String text = string == null ? "(unknown)" : string.parsed();

        return "Description: " + (text.length() > 20
                ? text.substring(0, 20) + "…"
                : text);
    }

    public static String getName(SCategoryStatement element) {
        SCategorizedIn in = element.getCategorizedIn();
        SCategorizedAs as = element.getCategorizedAs();

        String inString = in == null || in.getString() == null
                ? "(unknown)" : in.getString().parsed();
        String asString = as == null || as.getString() == null
                ? "(unknown)" : as.getString().parsed();

        return "Category: " + inString + "=" + asString;
    }

    public static String getName(SRandomlyWeight element) {
        long weight = element.getInt() != null ? element.getInt().parsed() : 1;
        return "Weight: " + weight;
    }

    public static String getName(SPauseStatement element) {
        if (element.getPauseAllOtherTracks() != null) {
            return "Pause other tracks";
        } else if (element.getPauseThisTrack() != null) {
            return "Pause this track";
        } else if (element.getPauseAllTracks() != null) {
            return "Pause all tracks";
        } else if (element.getTrackRef() != null) {
            return "Pause '" + element.getTrackRef().getText() + "'";
        } else {
            return "Pause (unknown)";
        }
    }

    public static String getName(SResumeStatement element) {
        if (element.getResumeLoopingTracksStatement() != null) {
            return "Resume looping tracks";
        } else if (element.getTrackRef() != null) {
            return "Resume '" + element.getTrackRef().getText() + "'";
        } else {
            return "Resume (unknown)";
        }
    }

    @SuppressWarnings("unused")
    public static String getName(SBlock element) {
        return "(block)";
    }


    @SuppressWarnings("unused")
    public static String getName(SParallellyStatement element) {
        return "(parallelly)";
    }

    @SuppressWarnings("unused")
    public static boolean skipInStructureView(PsiElement e) {
        return true;
    }

    public static ItemPresentation getPresentation(SSoundscapeDefinition element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), SoundscapeIcons.NEW_SOUNDSCAPE, null);
    }

    public static ItemPresentation getPresentation(SDescribedStatement element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), null, null);
    }

    public static ItemPresentation getPresentation(SMusicDefinition element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), SoundscapeIcons.NEW_MUSIC, null);
    }

    public static ItemPresentation getPresentation(SEffectDefinition element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), SoundscapeIcons.NEW_EFFECT, null);
    }

    public static ItemPresentation getPresentation(SLoadDefinition element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), SoundscapeIcons.NEW_SAMPLE, null);
    }

    public static ItemPresentation getPresentation(SIncludableTrackDefinition element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), SoundscapeIcons.NEW_INCLUDABLE_TRACK, null);
    }

    public static ItemPresentation getPresentation(STrackDefinition element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), SoundscapeIcons.NEW_TRACK, null);
    }

    public static ItemPresentation getPresentation(SIncludeDefinition element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), SoundscapeIcons.FILE, null);
    }

    public static ItemPresentation getPresentation(SPlayStatement element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), SoundscapeIcons.SAMPLE, null);
    }

    public static ItemPresentation getPresentation(SBlock element) {
        return new PresentationData("(block)", element.getContainingFile().getName(), null, null);
    }

    public static ItemPresentation getPresentation(SRandomlyStatement element) {
        return new PresentationData("Randomly", element.getContainingFile().getName(), null, null);
    }

    public static ItemPresentation getPresentation(SCategoryStatement element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), null, null);
    }

    public static ItemPresentation getPresentation(SSleepStatement element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), null, null);
    }

    public static ItemPresentation getPresentation(SRandomlyWeight element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), null, null);
    }

    public static ItemPresentation getPresentation(SPauseStatement element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), null, null);
    }

    public static ItemPresentation getPresentation(SResumeStatement element) {
        return new PresentationData(getName(element), element.getContainingFile().getName(), null, null);
    }

    public static ItemPresentation getPresentation(SParallellyStatement element) {
        return new PresentationData("Parallelly", element.getContainingFile().getName(), null, null);
    }

}
