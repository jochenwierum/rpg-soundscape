package de.jowisoftware.rpgsoundscape.language.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.stream.Stream;

public interface SoundscapeStructureViewPsiElement extends NavigatablePsiElement {

    default Stream<SoundscapeStructureViewPsiElement> getStructureViewChildren() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, SoundscapeStructureViewPsiElement.class).stream()
                .flatMap(child -> child.skipInStructureView()
                        ? child.getStructureViewChildren()
                        : Stream.of(child));
    }

    default boolean skipInStructureView() {
        return false;
    }

}
