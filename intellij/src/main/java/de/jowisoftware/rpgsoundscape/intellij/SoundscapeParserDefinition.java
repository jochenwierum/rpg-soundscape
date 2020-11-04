package de.jowisoftware.rpgsoundscape.intellij;

import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import de.jowisoftware.rpgsoundscape.language.parser.SoundscapeParserDefinitionStub;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;

public class SoundscapeParserDefinition extends SoundscapeParserDefinitionStub {
    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new SoundscapeFile(viewProvider, SoundscapeFileType.INSTANCE);
    }
}
