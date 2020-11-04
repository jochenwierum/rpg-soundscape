package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import de.jowisoftware.rpgsoundscape.language.psi.SBlock;
import de.jowisoftware.rpgsoundscape.language.psi.SEffectDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SIncludableTrackDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SMusicDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SMusicEffectDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SParallellyStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SRandomlyStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SSoundscapeDefinition;
import de.jowisoftware.rpgsoundscape.language.psi.SString;
import de.jowisoftware.rpgsoundscape.language.psi.STrackDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SoundscapeFoldingBuilder extends FoldingBuilderEx implements DumbAware {

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        return PsiTreeUtil.findChildrenOfAnyType(root,
                SSoundscapeDefinition.class, STrackDefinition.class, SIncludableTrackDefinition.class,
                SBlock.class, SRandomlyStatement.class, SParallellyStatement.class,
                SMusicDefinition.class, SEffectDefinition.class)
                .stream()
                .map(e -> {
                    if (e instanceof SSoundscapeDefinition
                            || e instanceof STrackDefinition
                            || e instanceof SIncludableTrackDefinition) {
                        return markerIfNamed(e);
                    } else if (e instanceof SBlock
                            || e instanceof SRandomlyStatement
                            || e instanceof SParallellyStatement) {
                        return marker(e);
                    } else if (e instanceof SMusicDefinition
                            || e instanceof SEffectDefinition) {
                        SMusicEffectDefinition d = PsiTreeUtil.getChildOfType(e, SMusicEffectDefinition.class);
                        if (d != null && !d.getMetadataStatementList().isEmpty()) {
                            return marker(e);
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toArray(FoldingDescriptor[]::new);
    }

    private FoldingDescriptor markerIfNamed(PsiElement element) {
        SString title = PsiTreeUtil.getChildOfType(element, SString.class);
        if (title != null && !title.getText().isEmpty()) {
            return new FoldingDescriptor(element.getNode(), element.getTextRange());
        } else {
            return null;
        }
    }

    private FoldingDescriptor marker(PsiElement element) {
        return new FoldingDescriptor(element.getNode(), element.getTextRange());
    }

    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        PsiElement psi = node.getPsi();

        if (psi instanceof SSoundscapeDefinition) {
            return "Soundscape: " + name(psi);
        } else if (psi instanceof STrackDefinition) {
            return "Track: " + name(psi);
        } else if (psi instanceof SIncludableTrackDefinition) {
            return "Includable track: " + name(psi);
        } else if (psi instanceof SMusicDefinition) {
            return "Music: " + name(PsiTreeUtil.getChildOfType(psi, SMusicEffectDefinition.class));
        } else if (psi instanceof SEffectDefinition) {
            return "Effect: " + name(PsiTreeUtil.getChildOfType(psi, SMusicEffectDefinition.class));
        } else if (psi instanceof SBlock) {
            return "(block)";
        } else if (psi instanceof SRandomlyStatement) {
            return "(randomly)";
        } else if (psi instanceof SParallellyStatement) {
            return "(parallelly)";
        } else {
            return "";
        }
    }

    private String name(PsiElement parent) {
        if (parent == null)
            return "(unnamed)";

        SString title = PsiTreeUtil.getChildOfType(parent, SString.class);
        return title == null || title.getText().isEmpty()
                ? "(unnamed)"
                : title.parsed()
                .replaceAll("\n", "\\n")
                .replaceAll("\"", "\\\\\"");
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}
