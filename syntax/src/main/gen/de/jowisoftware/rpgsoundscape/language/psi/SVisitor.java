// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class SVisitor extends PsiElementVisitor {

  public void visitAutostartingModifier(@NotNull SAutostartingModifier o) {
    visitPsiElement(o);
  }

  public void visitBlock(@NotNull SBlock o) {
    visitPsiElement(o);
  }

  public void visitCategorizedAs(@NotNull SCategorizedAs o) {
    visitPsiElement(o);
  }

  public void visitCategorizedIn(@NotNull SCategorizedIn o) {
    visitPsiElement(o);
  }

  public void visitCategoryStatement(@NotNull SCategoryStatement o) {
    visitPsiElement(o);
  }

  public void visitDescribedStatement(@NotNull SDescribedStatement o) {
    visitPsiElement(o);
  }

  public void visitDoNothingStatement(@NotNull SDoNothingStatement o) {
    visitPsiElement(o);
  }

  public void visitEffectDefinition(@NotNull SEffectDefinition o) {
    visitPsiElement(o);
  }

  public void visitId(@NotNull SId o) {
    visitPsiElement(o);
  }

  public void visitIncludableTrackDefinition(@NotNull SIncludableTrackDefinition o) {
    visitPsiElement(o);
  }

  public void visitIncludableTrackId(@NotNull SIncludableTrackId o) {
    visitIncludableTrackRef(o);
    // visitoundscapeNamedElement(o);
  }

  public void visitIncludableTrackRef(@NotNull SIncludableTrackRef o) {
    visitId(o);
  }

  public void visitIncludeDefinition(@NotNull SIncludeDefinition o) {
    visitPsiElement(o);
  }

  public void visitInt(@NotNull SInt o) {
    visitPsiElement(o);
  }

  public void visitLoadDefinition(@NotNull SLoadDefinition o) {
    visitPsiElement(o);
  }

  public void visitLoopingTrackModifier(@NotNull SLoopingTrackModifier o) {
    visitPsiElement(o);
  }

  public void visitManualTrackModifier(@NotNull SManualTrackModifier o) {
    visitPsiElement(o);
  }

  public void visitMetadataStatement(@NotNull SMetadataStatement o) {
    visitPsiElement(o);
  }

  public void visitMusicDefinition(@NotNull SMusicDefinition o) {
    visitPsiElement(o);
  }

  public void visitMusicEffectDefinition(@NotNull SMusicEffectDefinition o) {
    visitPsiElement(o);
  }

  public void visitParallellyStatement(@NotNull SParallellyStatement o) {
    visitPsiElement(o);
  }

  public void visitPauseAllOtherTracks(@NotNull SPauseAllOtherTracks o) {
    visitPsiElement(o);
  }

  public void visitPauseAllTracks(@NotNull SPauseAllTracks o) {
    visitPsiElement(o);
  }

  public void visitPauseStatement(@NotNull SPauseStatement o) {
    visitPsiElement(o);
  }

  public void visitPauseThisTrack(@NotNull SPauseThisTrack o) {
    visitPsiElement(o);
  }

  public void visitPausedModifier(@NotNull SPausedModifier o) {
    visitPsiElement(o);
  }

  public void visitPercentage(@NotNull SPercentage o) {
    visitPsiElement(o);
  }

  public void visitPlayModificationAmplification(@NotNull SPlayModificationAmplification o) {
    visitPsiElement(o);
  }

  public void visitPlayModificationAttribution(@NotNull SPlayModificationAttribution o) {
    visitPsiElement(o);
  }

  public void visitPlayModificationItem(@NotNull SPlayModificationItem o) {
    visitPsiElement(o);
  }

  public void visitPlayModificationLimit(@NotNull SPlayModificationLimit o) {
    visitPsiElement(o);
  }

  public void visitPlayModificationOmission(@NotNull SPlayModificationOmission o) {
    visitPsiElement(o);
  }

  public void visitPlayModifications(@NotNull SPlayModifications o) {
    visitPsiElement(o);
  }

  public void visitPlayStatement(@NotNull SPlayStatement o) {
    visitPsiElement(o);
  }

  public void visitRandomlyStatement(@NotNull SRandomlyStatement o) {
    visitPsiElement(o);
  }

  public void visitRandomlyWeight(@NotNull SRandomlyWeight o) {
    visitPsiElement(o);
  }

  public void visitRepeatStatement(@NotNull SRepeatStatement o) {
    visitPsiElement(o);
  }

  public void visitResumeLoopingTracksStatement(@NotNull SResumeLoopingTracksStatement o) {
    visitPsiElement(o);
  }

  public void visitResumeStatement(@NotNull SResumeStatement o) {
    visitPsiElement(o);
  }

  public void visitRootItem(@NotNull SRootItem o) {
    visitPsiElement(o);
  }

  public void visitSampleId(@NotNull SSampleId o) {
    visitSampleRef(o);
    // visitoundscapeNamedElement(o);
  }

  public void visitSampleRef(@NotNull SSampleRef o) {
    visitId(o);
  }

  public void visitSleepStatement(@NotNull SSleepStatement o) {
    visitPsiElement(o);
  }

  public void visitSoundscapeDefinition(@NotNull SSoundscapeDefinition o) {
    visitPsiElement(o);
  }

  public void visitStatement(@NotNull SStatement o) {
    visitPsiElement(o);
  }

  public void visitString(@NotNull SString o) {
    visitPsiElement(o);
  }

  public void visitTimespan(@NotNull STimespan o) {
    visitPsiElement(o);
  }

  public void visitTrackContent(@NotNull STrackContent o) {
    visitPsiElement(o);
  }

  public void visitTrackDefinition(@NotNull STrackDefinition o) {
    visitPsiElement(o);
  }

  public void visitTrackId(@NotNull STrackId o) {
    visitTrackRef(o);
    // visitoundscapeNamedElement(o);
  }

  public void visitTrackRef(@NotNull STrackRef o) {
    visitId(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
