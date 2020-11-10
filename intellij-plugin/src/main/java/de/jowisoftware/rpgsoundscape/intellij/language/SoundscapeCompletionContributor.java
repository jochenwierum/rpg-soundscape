package de.jowisoftware.rpgsoundscape.intellij.language;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.util.ProcessingContext;
import de.jowisoftware.rpgsoundscape.language.SoundscapeIcons;
import de.jowisoftware.rpgsoundscape.language.psi.SRandomlyStatement;
import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeTypes;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;

public class SoundscapeCompletionContributor extends CompletionContributor {
    public SoundscapeCompletionContributor() {
        addCompletion(psiElement()
                        .withParent(customError("unexpected")
                                .withParent(psiElement(SoundscapeTypes.ROOT_CONTENT))),
                List.of(
                        item("Include \"", null, true),
                        item("Load sample ", SoundscapeIcons.NEW_SAMPLE, false),
                        item("Includable track ", SoundscapeIcons.NEW_INCLUDABLE_TRACK, false),
                        item("Soundscape ", SoundscapeIcons.NEW_SOUNDSCAPE, false),
                        item("Music ", SoundscapeIcons.NEW_MUSIC, false),
                        item("Effect ", SoundscapeIcons.NEW_EFFECT, false)
                ));

        addCompletion(psiElement(SoundscapeTypes.IDENTIFIER)
                        .withParent(customError("<metadata statement>")
                                .withParent(psiElement(SoundscapeTypes.SOUNDSCAPE_DEFINITION))),
                List.of(
                        item("Looping ", SoundscapeIcons.NEW_TRACK, true),
                        item("Manual ", SoundscapeIcons.NEW_TRACK, true),
                        item("Includable track ", SoundscapeIcons.NEW_INCLUDABLE_TRACK, false),
                        item("Load sample ", SoundscapeIcons.NEW_SAMPLE, false),
                        item("Described as \""),
                        item("Categorized ", null, true)
                ));

        addCompletion(psiElement()
                        .withParent(psiElement(PsiErrorElement.class)
                                .withParent(or(
                                        psiElement(SoundscapeTypes.STATEMENT),
                                        psiElement(SoundscapeTypes.PARALLELLY_STATEMENT),
                                        psiElement(SoundscapeTypes.RANDOMLY_WEIGHT),
                                        psiElement(SoundscapeTypes.REPEAT_STATEMENT)
                                ))),
                List.of(
                        item("Play sample ", SoundscapeIcons.SAMPLE, true),
                        item("Sleep ", null, true),
                        item("Parallelly "),
                        item("Pause ", null, true),
                        item("Randomly "),
                        item("Repeat ", SoundscapeIcons.TRACK, true),
                        item("Resume ", SoundscapeIcons.TRACK, true)
                ));

        addCompletion(psiElement().withSuperParent(2, SRandomlyStatement.class),
                List.of(
                        item("Weighted with "),
                        item("Weighted with 1 ")
                ));

        addCompletion(psiElement().afterLeaf(psiElement(SoundscapeTypes.PAUSE)),
                List.of(
                        item("looping tracks;", SoundscapeIcons.TRACK, false),
                        item("track ", SoundscapeIcons.TRACK, true)
                ));

        addCompletion(psiElement().afterLeaf(psiElement(SoundscapeTypes.PAUSE)),
                List.of(
                        item("track ", SoundscapeIcons.TRACK, true),
                        item("this track;", SoundscapeIcons.TRACK, false),
                        item("all other tracks;", SoundscapeIcons.TRACK, false),
                        item("all tracks;", SoundscapeIcons.TRACK, false)
                ));

        addCompletion(psiElement().afterLeaf(psiElement(SoundscapeTypes.LOOPING)),
                List.of(
                        item("track ", SoundscapeIcons.NEW_TRACK, false),
                        item("paused track ", SoundscapeIcons.NEW_TRACK, false)
                ));

        addCompletion(psiElement().afterLeaf(psiElement(SoundscapeTypes.MANUAL)),
                List.of(
                        item("track ", SoundscapeIcons.NEW_TRACK, false),
                        item("autostarting track ", SoundscapeIcons.NEW_TRACK, false)
                ));

        addCompletion(
                psiElement().andOr(
                        // after identifier
                        psiElement().afterLeaf(
                                psiElement(SoundscapeTypes.IDENTIFIER)
                                        .withParent(psiElement(SoundscapeTypes.SAMPLE_REF))),

                        // after "from STRING"
                        psiElement()
                                .afterLeaf(psiElement(SoundscapeTypes.TEXT)
                                        .afterLeaf(psiElement(SoundscapeTypes.FROM))
                                )),

                // TODO: after comma, after and (but inside PLAY/LOAD/MUSIC/EFFECT!)
                List.of(item("with "), item("without ")));

        addCompletion(psiElement().afterLeaf(
                psiElement(SoundscapeTypes.IDENTIFIER)
                        .afterLeafSkipping(
                                or(psiElement().whitespaceCommentEmptyOrError(), psiElement(SoundscapeTypes.SAMPLE)),
                                psiElement(SoundscapeTypes.LOAD))),
                List.of(item("from \"")));

        addCompletion(psiElement().afterLeaf(or(psiElement(SoundscapeTypes.SLEEP), psiElement(SoundscapeTypes.REPEAT))),
                List.of(item("between ")));

        addCompletion(psiElement()
                        .afterLeaf(or(
                                psiElement(SoundscapeTypes.NUM_INTEGER)
                                        .withTextLengthLongerThan(0)
                                        .afterLeaf(psiElement(SoundscapeTypes.BETWEEN)),
                                psiElement(SoundscapeTypes.DURATION)
                                        .withTextLengthLongerThan(1)
                                        .afterLeaf(psiElement(SoundscapeTypes.BETWEEN))
                        )),
                List.of(item(" and ")));

        addCompletion(psiElement()
                        .afterLeaf(or(
                                psiElement(SoundscapeTypes.NUM_INTEGER)
                                        .afterLeaf(psiElement(SoundscapeTypes.AND)
                                                .afterLeaf(psiElement(SoundscapeTypes.NUM_INTEGER)
                                                        .afterLeaf(psiElement(SoundscapeTypes.BETWEEN)
                                                                .afterLeaf(psiElement(SoundscapeTypes.REPEAT))))),
                                psiElement(SoundscapeTypes.NUM_INTEGER)
                                        .afterLeaf(psiElement(SoundscapeTypes.REPEAT))
                        )),
                List.of(item(" times ")));

        addCompletion(psiElement().afterLeaf(
                or(
                        psiElement(SoundscapeTypes.TEXT)
                                .afterLeaf(psiElement(SoundscapeTypes.IN)
                                        .afterLeaf(psiElement(SoundscapeTypes.CATEGORIZED))
                                ),
                        psiElement(SoundscapeTypes.CATEGORIZED)
                )),
                List.of(item("as \"")));

        addCompletion(psiElement().afterLeaf(
                or(
                        psiElement(SoundscapeTypes.TEXT)
                                .afterLeaf(psiElement(SoundscapeTypes.AS)
                                        .afterLeaf(psiElement(SoundscapeTypes.CATEGORIZED))
                                ),
                        psiElement(SoundscapeTypes.CATEGORIZED)
                )),
                List.of(item("in \"")));
    }

    private Capture<PsiErrorElement> customError(final String text) {
        return psiElement(PsiErrorElement.class)
                .with(new PatternCondition<PsiErrorElement>("custom error: " + text) {
                    @Override
                    public boolean accepts(@NotNull PsiErrorElement psiErrorElement, ProcessingContext context) {
                        return psiErrorElement.getErrorDescription().contains(text);
                    }
                });
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        super.fillCompletionVariants(parameters, result);
    }

    private void addCompletion(Capture<PsiElement> selector, List<LookupElementBuilder> completions) {
        extend(CompletionType.BASIC, selector,
                new CompletionProvider<>() {
                    public void addCompletions(
                            @NotNull CompletionParameters parameters,
                            @NotNull ProcessingContext context,
                            @NotNull CompletionResultSet resultSet) {
                        resultSet.addAllElements(completions);
                    }
                }
        );
    }

    private LookupElementBuilder item(String text) {
        return item(text, null, false);
    }

    private LookupElementBuilder item(String text, Icon icon, boolean triggerAgain) {
        return LookupElementBuilder.create(text)
                .withIcon(icon)
                .withInsertHandler(!triggerAgain
                        ? null
                        : (context, item) -> AutoPopupController.getInstance(context.getProject())
                        .scheduleAutoPopup(context.getEditor()))
                .withCaseSensitivity(false);
    }
}
