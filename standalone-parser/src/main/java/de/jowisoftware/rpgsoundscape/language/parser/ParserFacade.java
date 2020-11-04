package de.jowisoftware.rpgsoundscape.language.parser;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.util.PsiTreeUtil;
import de.jowisoftware.rpgsoundscape.exceptions.SyntaxException;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;
import de.jowisoftware.rpgsoundscape.model.SoundscapeFileContent;
import de.jowisoftware.rpgsoundscape.model.SoundscapeReader;
import org.intellij.grammar.LightPsi;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParserFacade {
    public static SoundscapeFileContent parse(Path file) throws IOException {
        List<SoundscapeFile> includes = new ArrayList<>();
        findIncludes(file, new HashSet<>(), includes);
        return SoundscapeReader.read(includes);
    }

    private static void findIncludes(Path file,
            Set<String> seenFiles, List<SoundscapeFile> result) throws IOException {

        String normalizedName = file.toAbsolutePath().normalize().toString();
        if (seenFiles.contains(normalizedName)) {
            return;
        }
        seenFiles.add(normalizedName);

        SoundscapeFile psiFile = readChecked(file);
        result.add(psiFile);

        for (String includeFile : SoundscapeReader.readIncludes(psiFile)) {
            findIncludes(file.getParent().resolve(includeFile), seenFiles, result);
        }
    }

    private static SoundscapeFile readChecked(Path file) throws IOException {
        SoundscapeFile psiFile = (SoundscapeFile) LightPsi.parseFile(file.toFile(), new SoundscapeParserDefinitionStub());
        checkForErrors(psiFile);
        return psiFile;
    }

    private static void checkForErrors(SoundscapeFile psiFile) {
        PsiElement root = psiFile.getNode().getPsi();
        PsiErrorElement errorElement = PsiTreeUtil.findChildOfType(root, PsiErrorElement.class);
        if (errorElement != null) {
            throw new SyntaxException(errorElement);
        }
    }
}
