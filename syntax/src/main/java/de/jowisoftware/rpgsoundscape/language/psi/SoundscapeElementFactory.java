package de.jowisoftware.rpgsoundscape.language.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import de.jowisoftware.rpgsoundscape.language.SoundscapeFileTypeStub;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class SoundscapeElementFactory {
    private static final String SOUNDSCAPE = "SOUNDSCAPE \"x\" { %s }";
    private static final String STATEMENT = String.format(SOUNDSCAPE, "LOOPING TRACK y { %s; }");

    private static SoundscapeFile createFile(Project project, String text) {
        String name = "dummy.simple";
        return (SoundscapeFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, SoundscapeFileTypeStub.INSTANCE, text);
    }

    private static <T extends PsiElement> T createAndFind(Class<T> childType, Project project, String text) {
        return findChildOfType(createFile(project, text), childType);
    }

    public static SSampleId createSampleId(Project project, String newName) {
        return createAndFind(SSampleId.class, project, "LOAD SAMPLE " + newName + " FROM \"file:///dummy\";");
    }

    public static SSampleRef createSampleRef(Project project, String newName) {
        return createAndFind(SSampleRef.class, project, String.format(STATEMENT, "PLAY SAMPLE " + newName));
    }

    public static SIncludableTrackId createIncludableTrackId(Project project, String newName) {
        return createAndFind(SIncludableTrackId.class, project, "INCLUDABLE TRACK " + newName + " { SLEEP 1s; }");
    }

    public static SIncludableTrackRef createIncludableTrackRef(Project project, String newName) {
        return createAndFind(SIncludableTrackRef.class, project, String.format(SOUNDSCAPE, "LOOPING TRACK y INCLUDES " + newName + ";"));
    }

    public static STrackId createTrackId(Project project, String newName) {
        return createAndFind(STrackId.class, project, String.format(SOUNDSCAPE, "LOOPING TRACK " + newName + " { SLEEP 1s; } }"));
    }

    public static PsiElement createIncludableSoundscapeRef(Project project, String newName) {
        return createAndFind(SIncludableSoundscapeRef.class, project, String.format(SOUNDSCAPE, "INCLUDE SOUNDSCAPE " + newName + ";"));
    }

    public static PsiElement createIncludableSoundscapeId(Project project, String newName) {
        return createAndFind(SIncludableSoundscapeId.class, project, "INCLUDABLE SOUNDSCAPE " + newName + " {}");
    }

    public static STrackRef createTrackRef(Project project, String newName) {
        PsiElement file = createFile(project, String.format(STATEMENT, "PAUSE TRACK " + newName));
        return findChildOfType(findChildOfType(file, SPauseStatement.class), STrackRef.class);
    }

    public static SFilename createFilename(Project project, String content) {
        return createAndFind(SFilename.class, project, "include \"" + content + "\";");
    }
}
