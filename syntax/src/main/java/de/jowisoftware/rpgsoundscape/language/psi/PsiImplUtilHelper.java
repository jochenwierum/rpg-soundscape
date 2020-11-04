package de.jowisoftware.rpgsoundscape.language.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;

import java.util.function.BiFunction;

public class PsiImplUtilHelper {
    public static PsiElement setName(PsiElement element, IElementType elementType, String newName,
            BiFunction<Project, String, PsiElement> newObjectFactory) {
        ASTNode node = element.getNode().findChildByType(elementType);
        if (node != null) {
            ASTNode newNode = newObjectFactory.apply(element.getProject(), newName).getNode();
            element.getNode().replaceChild(node, newNode);
        }
        return element;
    }

    public static  <T extends PsiElement> PsiReference getReference(T element, BiFunction<T, TextRange, PsiReference> creator) {
        if (element.getText() == null) {
            return null;
        }

        return creator.apply(element, new TextRange(0, element.getTextLength()));
    }
}
