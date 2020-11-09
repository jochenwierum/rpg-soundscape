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
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitCategorizedAs(@NotNull SCategorizedAs o) {
    visitPsiElement(o);
  }

  public void visitCategorizedIn(@NotNull SCategorizedIn o) {
    visitPsiElement(o);
  }

  public void visitCategoryStatement(@NotNull SCategoryStatement o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitDescribedStatement(@NotNull SDescribedStatement o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitDoNothingStatement(@NotNull SDoNothingStatement o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitEffectDefinition(@NotNull SEffectDefinition o) {
    visitMusicEffectDefinition(o);
  }

  public void visitFilename(@NotNull SFilename o) {
    visitString(o);
  }

  public void visitId(@NotNull SId o) {
    visitPsiElement(o);
  }

  public void visitIncludableTrackDefinition(@NotNull SIncludableTrackDefinition o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitIncludableTrackId(@NotNull SIncludableTrackId o) {
    visitId(o);
    // visitoundscapeNamedElement(o);
  }

  public void visitIncludableTrackRef(@NotNull SIncludableTrackRef o) {
    visitReference(o);
  }

  public void visitIncludeDefinition(@NotNull SIncludeDefinition o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitInt(@NotNull SInt o) {
    visitPsiElement(o);
  }

  public void visitLoadDefinition(@NotNull SLoadDefinition o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitLoopingTrackModifier(@NotNull SLoopingTrackModifier o) {
    visitPsiElement(o);
  }

  public void visitManualTrackModifier(@NotNull SManualTrackModifier o) {
    visitPsiElement(o);
  }

  public void visitMetadataStatement(@NotNull SMetadataStatement o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitMusicDefinition(@NotNull SMusicDefinition o) {
    visitMusicEffectDefinition(o);
  }

  public void visitMusicEffectDefinition(@NotNull SMusicEffectDefinition o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitParallellyStatement(@NotNull SParallellyStatement o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitPauseAllOtherTracks(@NotNull SPauseAllOtherTracks o) {
    visitPsiElement(o);
  }

  public void visitPauseAllTracks(@NotNull SPauseAllTracks o) {
    visitPsiElement(o);
  }

  public void visitPauseStatement(@NotNull SPauseStatement o) {
    visitoundscapeStructureViewPsiElement(o);
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
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitRandomlyStatement(@NotNull SRandomlyStatement o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitRandomlyWeight(@NotNull SRandomlyWeight o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitReference(@NotNull SReference o) {
    visitPsiElement(o);
  }

  public void visitRepeatStatement(@NotNull SRepeatStatement o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitResumeLoopingTracksStatement(@NotNull SResumeLoopingTracksStatement o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitResumeStatement(@NotNull SResumeStatement o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitRootContent(@NotNull SRootContent o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitSampleId(@NotNull SSampleId o) {
    visitId(o);
    // visitoundscapeNamedElement(o);
  }

  public void visitSampleRef(@NotNull SSampleRef o) {
    visitReference(o);
  }

  public void visitSleepStatement(@NotNull SSleepStatement o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitSoundscapeDefinition(@NotNull SSoundscapeDefinition o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitStatement(@NotNull SStatement o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitString(@NotNull SString o) {
    visitPsiElement(o);
  }

  public void visitTimespan(@NotNull STimespan o) {
    visitPsiElement(o);
  }

  public void visitTrackContent(@NotNull STrackContent o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitTrackDefinition(@NotNull STrackDefinition o) {
    visitoundscapeStructureViewPsiElement(o);
  }

  public void visitTrackId(@NotNull STrackId o) {
    visitId(o);
    // visitoundscapeNamedElement(o);
  }

  public void visitTrackRef(@NotNull STrackRef o) {
    visitReference(o);
  }

  public void visitoundscapeStructureViewPsiElement(@NotNull SoundscapeStructureViewPsiElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
