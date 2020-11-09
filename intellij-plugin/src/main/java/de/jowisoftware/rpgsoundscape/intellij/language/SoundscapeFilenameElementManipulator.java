package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.util.IncorrectOperationException;
import de.jowisoftware.rpgsoundscape.language.psi.SFilename;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeElementFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoundscapeFilenameElementManipulator implements ElementManipulator<SFilename> {

    @Override
    public @Nullable SFilename handleContentChange(@NotNull SFilename element, @NotNull TextRange range, String newContent) throws IncorrectOperationException {
        final String originalContent = element.getText();
        final TextRange withoutQuotes = getRangeInElement(element);

        final String replacement = originalContent.substring(withoutQuotes.getStartOffset(), range.getStartOffset()) +
                newContent +
                originalContent.substring(range.getEndOffset(), withoutQuotes.getEndOffset());

        return (SFilename) element.replace(SoundscapeElementFactory.createFilename(element.getProject(), replacement));
    }

    @Override
    public SFilename handleContentChange(@NotNull final SFilename element, final String newContent) throws IncorrectOperationException {
        return handleContentChange(element, getRangeInElement(element), newContent);
    }

    @Override
    @NotNull
    public TextRange getRangeInElement(@NotNull final SFilename element) {
        return new TextRange(1, element.getTextLength() - 1);
    }
}
