package de.jowisoftware.rpgsoundscape.language.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackId;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackRef;
import de.jowisoftware.rpgsoundscape.language.psi.SLoadDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SRootItem;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleId;
import de.jowisoftware.rpgsoundscape.language.psi.SSampleRef;
import de.jowisoftware.rpgsoundscape.language.psi.SSoundscapeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.STrackDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.STrackId;
import de.jowisoftware.rpgsoundscape.language.psi.STrackRef;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;

import java.util.Optional;
import java.util.stream.Stream;

public class ReferenceUtil {
    public static Optional<SSampleId> findSample(SSampleRef element, String name) {
        return Stream.concat(
                findInSoundscape(element, SLoadDefinition.class, SSampleId.class),
                findInRoot(element, SLoadDefinition.class, SSampleId.class)
        )
                .filter(id -> id.getText().equals(name))
                .findFirst();

        // CAREFUL! Handle recursion!

/*
        return FileTypeIndex.getFiles(SoundscapeFileTypeStub.INSTANCE, GlobalSearchScope.allScope(project)).stream()
                .map(virtualFile -> (SoundscapeFile) PsiManager.getInstance(project).findFile(virtualFile))
                .filter(Objects::nonNull)
                .flatMap(file -> …
 */
    }

    public static Optional<SIncludableTrackId> findIncludableTrack(SIncludableTrackRef element, String name) {
        return Stream.concat(
                findInSoundscape(element, SIncludableTrackDefinition.class, SIncludableTrackId.class),
                findInRoot(element, SIncludableTrackDefinition.class, SIncludableTrackId.class)
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
                findInRoot(element, siblingType, idClass));
        // TODO: scan included files (project)
    }

    private static <T extends PsiElement> Stream<T> findInRoot(
            PsiElement element, Class<? extends PsiElement> parentClass, Class<T> idClass) {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(element, SoundscapeFile.class))
                .stream()
                .flatMap(e -> PsiTreeUtil.getChildrenOfTypeAsList(e, SRootItem.class).stream())
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
}
