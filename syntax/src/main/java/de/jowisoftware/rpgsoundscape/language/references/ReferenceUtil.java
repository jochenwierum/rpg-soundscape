package de.jowisoftware.rpgsoundscape.language.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackId;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackRef;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SLoadDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SRootContent;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleId;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleRef;
import de.jowisoftware.rpgsoundscape.language.psi.SSoundscapeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.STrackDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.STrackId;
import de.jowisoftware.rpgsoundscape.language.psi.STrackRef;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterators;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ReferenceUtil {
    public static Optional<SSampleId> findSample(SSampleRef element, String name) {
        return Stream.concat(
                findInSoundscape(element, SLoadDefinition.class, SSampleId.class),
                resolveIncludes(((SoundscapeFile) element.getContainingFile()))
                        .flatMap(file -> findInFile(file, SLoadDefinition.class, SSampleId.class))
        )
                .filter(id -> id.getText().equals(name))
                .findFirst();
    }


    public static Optional<SIncludableTrackId> findIncludableTrack(SIncludableTrackRef element, String name) {
        return Stream.concat(
                findInSoundscape(element, SIncludableTrackDefinition.class, SIncludableTrackId.class),
                resolveIncludes(((SoundscapeFile) element.getContainingFile()))
                        .flatMap(file -> findInFile(file, SIncludableTrackDefinition.class, SIncludableTrackId.class))
        )
                .filter(id -> id.getText().equals(name))
                .findFirst();
    }

    public static Optional<STrackId> findTrack(STrackRef element, String name) {
        return findInSoundscape(element, STrackDefinition.class, STrackId.class)
                .filter(id -> id.getText().equals(name))
                .findFirst();
    }

    public static Stream<SSampleId> findSamples(PsiElement element) {
        return collectUsableDefinitions(element, SLoadDefinition.class, SSampleId.class);
    }

    public static Stream<STrackId> findTracks(PsiElement element) {
        return collectUsableDefinitions(element, STrackDefinition.class, STrackId.class);
    }

    public static Stream<SIncludableTrackId> findIncludableTracks(PsiElement element) {
        return collectUsableDefinitions(element, SIncludableTrackDefinition.class, SIncludableTrackId.class);
    }

    private static <T extends PsiElement> Stream<T> collectUsableDefinitions(
            PsiElement element, Class<? extends PsiElement> siblingType, Class<T> idClass) {
        return Stream.concat(
                findInSoundscape(element, siblingType, idClass),
                resolveIncludes(((SoundscapeFile) element.getContainingFile()))
                        .flatMap(file -> findInFile(file, siblingType, idClass))
        );
    }

    private static <T extends PsiElement> Stream<T> findInFile(
            SoundscapeFile file, Class<? extends PsiElement> parentClass, Class<T> idClass) {
        return Optional.ofNullable(PsiTreeUtil.getChildOfType(file, SRootContent.class)).stream()
                .flatMap(e -> PsiTreeUtil.getChildrenOfTypeAsList(e, parentClass).stream())
                .flatMap(e -> PsiTreeUtil.getChildrenOfTypeAsList(e, idClass).stream());
    }

    private static <T extends PsiElement> Stream<T> findInSoundscape(
            PsiElement element, Class<? extends PsiElement> parentClass, Class<T> idClass) {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(element, SSoundscapeDefinition.class))
                .stream()
                .flatMap(e -> PsiTreeUtil.getChildrenOfTypeAsList(e, parentClass).stream())
                .flatMap(e -> PsiTreeUtil.getChildrenOfTypeAsList(e, idClass).stream());
    }

    private static Stream<SoundscapeFile> resolveIncludes(SoundscapeFile psiFile) {
        Set<String> seen = new HashSet<>();
        Stack<SoundscapeFile> agenda = new Stack<>();
        agenda.add(psiFile);

        //Collection<VirtualFile> availableFiles = FileTypeIndex.getFiles(SoundscapeFileTypeStub.INSTANCE, GlobalSearchScope.allScope(psiFile.getProject()));

        Iterator<SoundscapeFile> iterator = new Iterator<>() {
            @Override
            public boolean hasNext() {
                return !agenda.isEmpty();
            }

            @Override
            public SoundscapeFile next() {
                SoundscapeFile includedFile = agenda.pop();
                seen.add(includedFile.getVirtualFile().getCanonicalPath());

                Set<String> newFiles = searchIncludes(includedFile);
                newFiles.stream()
                        .map(newFile -> includedFile.getVirtualFile().findFileByRelativePath("../" + newFile))
                        .filter(Objects::nonNull)
                        .filter(vf -> !seen.contains(vf.getCanonicalPath()))
                        .map(virtualFile -> (SoundscapeFile) PsiManager.getInstance(psiFile.getProject()).findFile(virtualFile))
                        .filter(Objects::nonNull)
                        .forEach(agenda::add);

                return includedFile;
            }
        };

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    private static Set<String> searchIncludes(PsiFile psiFile) {
        return Optional.ofNullable(PsiTreeUtil.getChildOfType(psiFile, SRootContent.class)).stream()
                .flatMap(e -> PsiTreeUtil.getChildrenOfTypeAsList(e, SIncludeDefinition.class).stream())
                .filter(e -> e.getString() != null && e.getString().getTextLength() > 2)
                .map(e -> e.getString().parsed())
                .collect(Collectors.toCollection(HashSet::new));
    }
}
