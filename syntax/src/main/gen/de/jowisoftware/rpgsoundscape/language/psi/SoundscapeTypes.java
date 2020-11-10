// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import de.jowisoftware.rpgsoundscape.language.psi.impl.*;

public interface SoundscapeTypes {

  IElementType AMPLIFICATION_PLAY_MODIFICATION = new SoundscapeElementType("AMPLIFICATION_PLAY_MODIFICATION");
  IElementType ATTRIBUTION_LOAD_MODIFICATION = new SoundscapeElementType("ATTRIBUTION_LOAD_MODIFICATION");
  IElementType AUTOSTARTING_MODIFIER = new SoundscapeElementType("AUTOSTARTING_MODIFIER");
  IElementType BLOCK = new SoundscapeElementType("BLOCK");
  IElementType CATEGORIZED_AS = new SoundscapeElementType("CATEGORIZED_AS");
  IElementType CATEGORIZED_IN = new SoundscapeElementType("CATEGORIZED_IN");
  IElementType CATEGORY_STATEMENT = new SoundscapeElementType("CATEGORY_STATEMENT");
  IElementType DESCRIBED_STATEMENT = new SoundscapeElementType("DESCRIBED_STATEMENT");
  IElementType DO_NOTHING_STATEMENT = new SoundscapeElementType("DO_NOTHING_STATEMENT");
  IElementType EFFECT_DEFINITION = new SoundscapeElementType("EFFECT_DEFINITION");
  IElementType FILENAME = new SoundscapeElementType("FILENAME");
  IElementType INCLUDABLE_TRACK_DEFINITION = new SoundscapeElementType("INCLUDABLE_TRACK_DEFINITION");
  IElementType INCLUDABLE_TRACK_ID = new SoundscapeElementType("INCLUDABLE_TRACK_ID");
  IElementType INCLUDABLE_TRACK_REF = new SoundscapeElementType("INCLUDABLE_TRACK_REF");
  IElementType INCLUDE_DEFINITION = new SoundscapeElementType("INCLUDE_DEFINITION");
  IElementType INT = new SoundscapeElementType("INT");
  IElementType LIMIT_PLAY_MODIFICATION = new SoundscapeElementType("LIMIT_PLAY_MODIFICATION");
  IElementType LOAD_DEFINITION = new SoundscapeElementType("LOAD_DEFINITION");
  IElementType LOOPING_TRACK_MODIFIER = new SoundscapeElementType("LOOPING_TRACK_MODIFIER");
  IElementType MANUAL_TRACK_MODIFIER = new SoundscapeElementType("MANUAL_TRACK_MODIFIER");
  IElementType METADATA_STATEMENT = new SoundscapeElementType("METADATA_STATEMENT");
  IElementType MUSIC_DEFINITION = new SoundscapeElementType("MUSIC_DEFINITION");
  IElementType NO_CONVERSION_LOAD_MODIFICATION = new SoundscapeElementType("NO_CONVERSION_LOAD_MODIFICATION");
  IElementType OMISSION_PLAY_MODIFICATION = new SoundscapeElementType("OMISSION_PLAY_MODIFICATION");
  IElementType PARALLELLY_STATEMENT = new SoundscapeElementType("PARALLELLY_STATEMENT");
  IElementType PAUSED_MODIFIER = new SoundscapeElementType("PAUSED_MODIFIER");
  IElementType PAUSE_ALL_OTHER_TRACKS = new SoundscapeElementType("PAUSE_ALL_OTHER_TRACKS");
  IElementType PAUSE_ALL_TRACKS = new SoundscapeElementType("PAUSE_ALL_TRACKS");
  IElementType PAUSE_STATEMENT = new SoundscapeElementType("PAUSE_STATEMENT");
  IElementType PAUSE_THIS_TRACK = new SoundscapeElementType("PAUSE_THIS_TRACK");
  IElementType PERCENTAGE = new SoundscapeElementType("PERCENTAGE");
  IElementType PLAY_STATEMENT = new SoundscapeElementType("PLAY_STATEMENT");
  IElementType RANDOMLY_STATEMENT = new SoundscapeElementType("RANDOMLY_STATEMENT");
  IElementType RANDOMLY_WEIGHT = new SoundscapeElementType("RANDOMLY_WEIGHT");
  IElementType REPEAT_STATEMENT = new SoundscapeElementType("REPEAT_STATEMENT");
  IElementType RESUME_LOOPING_TRACKS_STATEMENT = new SoundscapeElementType("RESUME_LOOPING_TRACKS_STATEMENT");
  IElementType RESUME_STATEMENT = new SoundscapeElementType("RESUME_STATEMENT");
  IElementType ROOT_CONTENT = new SoundscapeElementType("ROOT_CONTENT");
  IElementType SAMPLE_ID = new SoundscapeElementType("SAMPLE_ID");
  IElementType SAMPLE_REF = new SoundscapeElementType("SAMPLE_REF");
  IElementType SLEEP_STATEMENT = new SoundscapeElementType("SLEEP_STATEMENT");
  IElementType SOUNDSCAPE_DEFINITION = new SoundscapeElementType("SOUNDSCAPE_DEFINITION");
  IElementType STATEMENT = new SoundscapeElementType("STATEMENT");
  IElementType STRING = new SoundscapeElementType("STRING");
  IElementType TIMESPAN = new SoundscapeElementType("TIMESPAN");
  IElementType TRACK_CONTENT = new SoundscapeElementType("TRACK_CONTENT");
  IElementType TRACK_DEFINITION = new SoundscapeElementType("TRACK_DEFINITION");
  IElementType TRACK_ID = new SoundscapeElementType("TRACK_ID");
  IElementType TRACK_REF = new SoundscapeElementType("TRACK_REF");

  IElementType ALL = new SoundscapeTokenType("ALL");
  IElementType AMPLIFICATION = new SoundscapeTokenType("AMPLIFICATION");
  IElementType AND = new SoundscapeTokenType("AND");
  IElementType AS = new SoundscapeTokenType("AS");
  IElementType ATTRIBUTION = new SoundscapeTokenType("ATTRIBUTION");
  IElementType AUTOSTARTING = new SoundscapeTokenType("AUTOSTARTING");
  IElementType BETWEEN = new SoundscapeTokenType("BETWEEN");
  IElementType BY = new SoundscapeTokenType("BY");
  IElementType CACHE = new SoundscapeTokenType("CACHE");
  IElementType CATEGORIZED = new SoundscapeTokenType("CATEGORIZED");
  IElementType CONVERSION = new SoundscapeTokenType("CONVERSION");
  IElementType CURLY_L = new SoundscapeTokenType("CURLY_L");
  IElementType CURLY_R = new SoundscapeTokenType("CURLY_R");
  IElementType DESCRIBED = new SoundscapeTokenType("DESCRIBED");
  IElementType DO = new SoundscapeTokenType("DO");
  IElementType DURATION = new SoundscapeTokenType("DURATION");
  IElementType EFFECT = new SoundscapeTokenType("EFFECT");
  IElementType FIRST = new SoundscapeTokenType("FIRST");
  IElementType FROM = new SoundscapeTokenType("FROM");
  IElementType IDENTIFIER = new SoundscapeTokenType("IDENTIFIER");
  IElementType IN = new SoundscapeTokenType("IN");
  IElementType INCLUDABLE = new SoundscapeTokenType("INCLUDABLE");
  IElementType INCLUDE = new SoundscapeTokenType("INCLUDE");
  IElementType INCLUDES = new SoundscapeTokenType("INCLUDES");
  IElementType LIMIT = new SoundscapeTokenType("LIMIT");
  IElementType LIST_DELIMITER = new SoundscapeTokenType("LIST_DELIMITER");
  IElementType LOAD = new SoundscapeTokenType("LOAD");
  IElementType LOOPING = new SoundscapeTokenType("LOOPING");
  IElementType MANUAL = new SoundscapeTokenType("MANUAL");
  IElementType MUSIC = new SoundscapeTokenType("MUSIC");
  IElementType NOTHING = new SoundscapeTokenType("NOTHING");
  IElementType NUM_INTEGER = new SoundscapeTokenType("NUM_INTEGER");
  IElementType OF = new SoundscapeTokenType("OF");
  IElementType OMISSION = new SoundscapeTokenType("OMISSION");
  IElementType OTHER = new SoundscapeTokenType("OTHER");
  IElementType PARALLELLY = new SoundscapeTokenType("PARALLELLY");
  IElementType PAUSE = new SoundscapeTokenType("PAUSE");
  IElementType PAUSED = new SoundscapeTokenType("PAUSED");
  IElementType PERCENT = new SoundscapeTokenType("PERCENT");
  IElementType PLAY = new SoundscapeTokenType("PLAY");
  IElementType RANDOMLY = new SoundscapeTokenType("RANDOMLY");
  IElementType REPEAT = new SoundscapeTokenType("REPEAT");
  IElementType RESUME = new SoundscapeTokenType("RESUME");
  IElementType SAMPLE = new SoundscapeTokenType("SAMPLE");
  IElementType SEPARATOR = new SoundscapeTokenType("SEPARATOR");
  IElementType SLEEP = new SoundscapeTokenType("SLEEP");
  IElementType SOUNDSCAPE = new SoundscapeTokenType("SOUNDSCAPE");
  IElementType TEXT = new SoundscapeTokenType("TEXT");
  IElementType THIS = new SoundscapeTokenType("THIS");
  IElementType TIMES = new SoundscapeTokenType("TIMES");
  IElementType TITLE = new SoundscapeTokenType("TITLE");
  IElementType TO = new SoundscapeTokenType("TO");
  IElementType TRACK = new SoundscapeTokenType("TRACK");
  IElementType TRACKS = new SoundscapeTokenType("TRACKS");
  IElementType WEIGHTED = new SoundscapeTokenType("WEIGHTED");
  IElementType WITH = new SoundscapeTokenType("WITH");
  IElementType WITHOUT = new SoundscapeTokenType("WITHOUT");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == AMPLIFICATION_PLAY_MODIFICATION) {
        return new SAmplificationPlayModificationImpl(node);
      }
      else if (type == ATTRIBUTION_LOAD_MODIFICATION) {
        return new SAttributionLoadModificationImpl(node);
      }
      else if (type == AUTOSTARTING_MODIFIER) {
        return new SAutostartingModifierImpl(node);
      }
      else if (type == BLOCK) {
        return new SBlockImpl(node);
      }
      else if (type == CATEGORIZED_AS) {
        return new SCategorizedAsImpl(node);
      }
      else if (type == CATEGORIZED_IN) {
        return new SCategorizedInImpl(node);
      }
      else if (type == CATEGORY_STATEMENT) {
        return new SCategoryStatementImpl(node);
      }
      else if (type == DESCRIBED_STATEMENT) {
        return new SDescribedStatementImpl(node);
      }
      else if (type == DO_NOTHING_STATEMENT) {
        return new SDoNothingStatementImpl(node);
      }
      else if (type == EFFECT_DEFINITION) {
        return new SEffectDefinitionImpl(node);
      }
      else if (type == FILENAME) {
        return new SFilenameImpl(node);
      }
      else if (type == INCLUDABLE_TRACK_DEFINITION) {
        return new SIncludableTrackDefinitionImpl(node);
      }
      else if (type == INCLUDABLE_TRACK_ID) {
        return new SIncludableTrackIdImpl(node);
      }
      else if (type == INCLUDABLE_TRACK_REF) {
        return new SIncludableTrackRefImpl(node);
      }
      else if (type == INCLUDE_DEFINITION) {
        return new SIncludeDefinitionImpl(node);
      }
      else if (type == INT) {
        return new SIntImpl(node);
      }
      else if (type == LIMIT_PLAY_MODIFICATION) {
        return new SLimitPlayModificationImpl(node);
      }
      else if (type == LOAD_DEFINITION) {
        return new SLoadDefinitionImpl(node);
      }
      else if (type == LOOPING_TRACK_MODIFIER) {
        return new SLoopingTrackModifierImpl(node);
      }
      else if (type == MANUAL_TRACK_MODIFIER) {
        return new SManualTrackModifierImpl(node);
      }
      else if (type == METADATA_STATEMENT) {
        return new SMetadataStatementImpl(node);
      }
      else if (type == MUSIC_DEFINITION) {
        return new SMusicDefinitionImpl(node);
      }
      else if (type == NO_CONVERSION_LOAD_MODIFICATION) {
        return new SNoConversionLoadModificationImpl(node);
      }
      else if (type == OMISSION_PLAY_MODIFICATION) {
        return new SOmissionPlayModificationImpl(node);
      }
      else if (type == PARALLELLY_STATEMENT) {
        return new SParallellyStatementImpl(node);
      }
      else if (type == PAUSED_MODIFIER) {
        return new SPausedModifierImpl(node);
      }
      else if (type == PAUSE_ALL_OTHER_TRACKS) {
        return new SPauseAllOtherTracksImpl(node);
      }
      else if (type == PAUSE_ALL_TRACKS) {
        return new SPauseAllTracksImpl(node);
      }
      else if (type == PAUSE_STATEMENT) {
        return new SPauseStatementImpl(node);
      }
      else if (type == PAUSE_THIS_TRACK) {
        return new SPauseThisTrackImpl(node);
      }
      else if (type == PERCENTAGE) {
        return new SPercentageImpl(node);
      }
      else if (type == PLAY_STATEMENT) {
        return new SPlayStatementImpl(node);
      }
      else if (type == RANDOMLY_STATEMENT) {
        return new SRandomlyStatementImpl(node);
      }
      else if (type == RANDOMLY_WEIGHT) {
        return new SRandomlyWeightImpl(node);
      }
      else if (type == REPEAT_STATEMENT) {
        return new SRepeatStatementImpl(node);
      }
      else if (type == RESUME_LOOPING_TRACKS_STATEMENT) {
        return new SResumeLoopingTracksStatementImpl(node);
      }
      else if (type == RESUME_STATEMENT) {
        return new SResumeStatementImpl(node);
      }
      else if (type == ROOT_CONTENT) {
        return new SRootContentImpl(node);
      }
      else if (type == SAMPLE_ID) {
        return new SSampleIdImpl(node);
      }
      else if (type == SAMPLE_REF) {
        return new SSampleRefImpl(node);
      }
      else if (type == SLEEP_STATEMENT) {
        return new SSleepStatementImpl(node);
      }
      else if (type == SOUNDSCAPE_DEFINITION) {
        return new SSoundscapeDefinitionImpl(node);
      }
      else if (type == STATEMENT) {
        return new SStatementImpl(node);
      }
      else if (type == STRING) {
        return new SStringImpl(node);
      }
      else if (type == TIMESPAN) {
        return new STimespanImpl(node);
      }
      else if (type == TRACK_CONTENT) {
        return new STrackContentImpl(node);
      }
      else if (type == TRACK_DEFINITION) {
        return new STrackDefinitionImpl(node);
      }
      else if (type == TRACK_ID) {
        return new STrackIdImpl(node);
      }
      else if (type == TRACK_REF) {
        return new STrackRefImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
