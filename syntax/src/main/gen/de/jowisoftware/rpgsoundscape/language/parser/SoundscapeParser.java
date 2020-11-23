// This is a generated file. Not intended for manual editing.
package de.jowisoftware.rpgsoundscape.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static de.jowisoftware.rpgsoundscape.language.psi.SoundscapeTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SoundscapeParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return root(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(INCLUDABLE_SOUNDSCAPE_DEFINITION, SOUNDSCAPE_DEFINITION),
    create_token_set_(EFFECT_DEFINITION, MUSIC_DEFINITION),
    create_token_set_(FILENAME, STRING),
    create_token_set_(INCLUDABLE_SOUNDSCAPE_REF, INCLUDABLE_TRACK_REF, SAMPLE_REF, TRACK_REF),
    create_token_set_(INCLUDABLE_SOUNDSCAPE_ID, INCLUDABLE_TRACK_ID, SAMPLE_ID, TRACK_ID),
    create_token_set_(AMPLIFICATION_PLAY_MODIFICATION, ATTRIBUTION_LOAD_MODIFICATION, LIMIT_PLAY_MODIFICATION, NO_CONVERSION_LOAD_MODIFICATION,
      OMISSION_PLAY_MODIFICATION),
  };

  /* ********************************************************** */
  // WITH AMPLIFICATION OF percentage
  public static boolean amplificationPlayModification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "amplificationPlayModification")) return false;
    if (!nextTokenIs(b, WITH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, AMPLIFICATION_PLAY_MODIFICATION, null);
    r = consumeTokens(b, 2, WITH, AMPLIFICATION, OF);
    p = r; // pin = 2
    r = r && percentage(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // WITH ATTRIBUTION string
  public static boolean attributionLoadModification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attributionLoadModification")) return false;
    if (!nextTokenIs(b, WITH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTION_LOAD_MODIFICATION, null);
    r = consumeTokens(b, 2, WITH, ATTRIBUTION);
    p = r; // pin = 2
    r = r && string(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // AUTOSTARTING
  public static boolean autostartingModifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "autostartingModifier")) return false;
    if (!nextTokenIs(b, AUTOSTARTING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AUTOSTARTING);
    exit_section_(b, m, AUTOSTARTING_MODIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // CURLY_L statement+ CURLY_R
  public static boolean block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block")) return false;
    if (!nextTokenIs(b, CURLY_L)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BLOCK, null);
    r = consumeToken(b, CURLY_L);
    p = r; // pin = 1
    r = r && report_error_(b, block_1(b, l + 1));
    r = p && consumeToken(b, CURLY_R) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // statement+
  private static boolean block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "block_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // AS string
  public static boolean categorizedAs(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "categorizedAs")) return false;
    if (!nextTokenIs(b, AS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CATEGORIZED_AS, null);
    r = consumeToken(b, AS);
    p = r; // pin = 1
    r = r && string(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // IN string
  public static boolean categorizedIn(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "categorizedIn")) return false;
    if (!nextTokenIs(b, IN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CATEGORIZED_IN, null);
    r = consumeToken(b, IN);
    p = r; // pin = 1
    r = r && string(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // CATEGORIZED ((categorizedIn categorizedAs) | (categorizedAs categorizedIn))
  public static boolean categoryStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "categoryStatement")) return false;
    if (!nextTokenIs(b, CATEGORIZED)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CATEGORY_STATEMENT, null);
    r = consumeToken(b, CATEGORIZED);
    p = r; // pin = 1
    r = r && categoryStatement_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (categorizedIn categorizedAs) | (categorizedAs categorizedIn)
  private static boolean categoryStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "categoryStatement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = categoryStatement_1_0(b, l + 1);
    if (!r) r = categoryStatement_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // categorizedIn categorizedAs
  private static boolean categoryStatement_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "categoryStatement_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = categorizedIn(b, l + 1);
    r = r && categorizedAs(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // categorizedAs categorizedIn
  private static boolean categoryStatement_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "categoryStatement_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = categorizedAs(b, l + 1);
    r = r && categorizedIn(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // BETWEEN int AND int
  static boolean complexRepeatStatement_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "complexRepeatStatement_")) return false;
    if (!nextTokenIs(b, BETWEEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, BETWEEN);
    p = r; // pin = 1
    r = r && report_error_(b, int_$(b, l + 1));
    r = p && report_error_(b, consumeToken(b, AND)) && r;
    r = p && int_$(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // BETWEEN timespan AND timespan
  static boolean complexSleepStatement_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "complexSleepStatement_")) return false;
    if (!nextTokenIs(b, BETWEEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, BETWEEN);
    p = r; // pin = 1
    r = r && report_error_(b, timespan(b, l + 1));
    r = p && report_error_(b, consumeToken(b, AND)) && r;
    r = p && timespan(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // DESCRIBED (BY | AS) string
  public static boolean describedStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "describedStatement")) return false;
    if (!nextTokenIs(b, DESCRIBED)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DESCRIBED_STATEMENT, null);
    r = consumeToken(b, DESCRIBED);
    p = r; // pin = 1
    r = r && report_error_(b, describedStatement_1(b, l + 1));
    r = p && string(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // BY | AS
  private static boolean describedStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "describedStatement_1")) return false;
    boolean r;
    r = consumeToken(b, BY);
    if (!r) r = consumeToken(b, AS);
    return r;
  }

  /* ********************************************************** */
  // DO NOTHING
  public static boolean doNothingStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "doNothingStatement")) return false;
    if (!nextTokenIs(b, DO)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DO_NOTHING_STATEMENT, null);
    r = consumeTokens(b, 1, DO, NOTHING);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // EFFECT musicEffectDefinition_
  public static boolean effectDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "effectDefinition")) return false;
    if (!nextTokenIs(b, EFFECT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, EFFECT_DEFINITION, null);
    r = consumeToken(b, EFFECT);
    p = r; // pin = 1
    r = r && musicEffectDefinition_(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // TEXT
  public static boolean filename(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "filename")) return false;
    if (!nextTokenIs(b, TEXT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TEXT);
    exit_section_(b, m, FILENAME, r);
    return r;
  }

  /* ********************************************************** */
  // HIDDEN
  public static boolean hiddenModifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hiddenModifier")) return false;
    if (!nextTokenIs(b, HIDDEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HIDDEN);
    exit_section_(b, m, HIDDEN_MODIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // INCLUDABLE (includableTrackDefinition | includableSoundscapeDefinition)
  static boolean includableDefinitions_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includableDefinitions_")) return false;
    if (!nextTokenIs(b, INCLUDABLE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, INCLUDABLE);
    p = r; // pin = 1
    r = r && includableDefinitions__1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // includableTrackDefinition | includableSoundscapeDefinition
  private static boolean includableDefinitions__1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includableDefinitions__1")) return false;
    boolean r;
    r = includableTrackDefinition(b, l + 1);
    if (!r) r = includableSoundscapeDefinition(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // SOUNDSCAPE includableSoundscapeId CURLY_L soundscapeBlockStatement_* CURLY_R
  public static boolean includableSoundscapeDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includableSoundscapeDefinition")) return false;
    if (!nextTokenIs(b, SOUNDSCAPE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INCLUDABLE_SOUNDSCAPE_DEFINITION, null);
    r = consumeToken(b, SOUNDSCAPE);
    p = r; // pin = 1
    r = r && report_error_(b, includableSoundscapeId(b, l + 1));
    r = p && report_error_(b, consumeToken(b, CURLY_L)) && r;
    r = p && report_error_(b, includableSoundscapeDefinition_3(b, l + 1)) && r;
    r = p && consumeToken(b, CURLY_R) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // soundscapeBlockStatement_*
  private static boolean includableSoundscapeDefinition_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includableSoundscapeDefinition_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!soundscapeBlockStatement_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "includableSoundscapeDefinition_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean includableSoundscapeId(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includableSoundscapeId")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, INCLUDABLE_SOUNDSCAPE_ID, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean includableSoundscapeRef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includableSoundscapeRef")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, INCLUDABLE_SOUNDSCAPE_REF, r);
    return r;
  }

  /* ********************************************************** */
  // TRACK includableTrackId block
  public static boolean includableTrackDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includableTrackDefinition")) return false;
    if (!nextTokenIs(b, TRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INCLUDABLE_TRACK_DEFINITION, null);
    r = consumeToken(b, TRACK);
    p = r; // pin = 1
    r = r && report_error_(b, includableTrackId(b, l + 1));
    r = p && block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // INCLUDABLE includableTrackDefinition
  static boolean includableTrackDefinition_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includableTrackDefinition_")) return false;
    if (!nextTokenIs(b, INCLUDABLE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, INCLUDABLE);
    p = r; // pin = 1
    r = r && includableTrackDefinition(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean includableTrackId(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includableTrackId")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, INCLUDABLE_TRACK_ID, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean includableTrackRef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includableTrackRef")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, INCLUDABLE_TRACK_REF, r);
    return r;
  }

  /* ********************************************************** */
  // INCLUDE filename
  public static boolean includeDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includeDefinition")) return false;
    if (!nextTokenIs(b, INCLUDE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INCLUDE_DEFINITION, null);
    r = consumeToken(b, INCLUDE);
    p = r; // pin = 1
    r = r && filename(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // INCLUDE SOUNDSCAPE includableSoundscapeRef
  public static boolean includeSoundscapeDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includeSoundscapeDefinition")) return false;
    if (!nextTokenIs(b, INCLUDE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INCLUDE_SOUNDSCAPE_DEFINITION, null);
    r = consumeTokens(b, 1, INCLUDE, SOUNDSCAPE);
    p = r; // pin = 1
    r = r && includableSoundscapeRef(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // INCLUDE includableTrackRef
  public static boolean includeTrackStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "includeTrackStatement")) return false;
    if (!nextTokenIs(b, INCLUDE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INCLUDE_TRACK_STATEMENT, null);
    r = consumeToken(b, INCLUDE);
    p = r; // pin = 1
    r = r && includableTrackRef(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // NUM_INTEGER
  public static boolean int_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "int_$")) return false;
    if (!nextTokenIs(b, NUM_INTEGER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NUM_INTEGER);
    exit_section_(b, m, INT, r);
    return r;
  }

  /* ********************************************************** */
  // WITH LIMIT TO timespan
  public static boolean limitPlayModification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "limitPlayModification")) return false;
    if (!nextTokenIs(b, WITH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LIMIT_PLAY_MODIFICATION, null);
    r = consumeTokens(b, 2, WITH, LIMIT, TO);
    p = r; // pin = 2
    r = r && timespan(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // LIST_DELIMITER | AND
  static boolean listDelimiter_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "listDelimiter_")) return false;
    if (!nextTokenIs(b, "", AND, LIST_DELIMITER)) return false;
    boolean r;
    r = consumeToken(b, LIST_DELIMITER);
    if (!r) r = consumeToken(b, AND);
    return r;
  }

  /* ********************************************************** */
  // LOAD SAMPLE? sampleId FROM string loadModifications_?
  public static boolean loadDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loadDefinition")) return false;
    if (!nextTokenIs(b, LOAD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LOAD_DEFINITION, null);
    r = consumeToken(b, LOAD);
    p = r; // pin = 1
    r = r && report_error_(b, loadDefinition_1(b, l + 1));
    r = p && report_error_(b, sampleId(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, FROM)) && r;
    r = p && report_error_(b, string(b, l + 1)) && r;
    r = p && loadDefinition_5(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // SAMPLE?
  private static boolean loadDefinition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loadDefinition_1")) return false;
    consumeToken(b, SAMPLE);
    return true;
  }

  // loadModifications_?
  private static boolean loadDefinition_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loadDefinition_5")) return false;
    loadModifications_(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // playModificationItem_ |
  //     attributionLoadModification |
  //     noConversionLoadModification
  static boolean loadModificationItem_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loadModificationItem_")) return false;
    if (!nextTokenIs(b, "", WITH, WITHOUT)) return false;
    boolean r;
    r = playModificationItem_(b, l + 1);
    if (!r) r = attributionLoadModification(b, l + 1);
    if (!r) r = noConversionLoadModification(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // loadModificationItem_ (listDelimiter_ loadModificationItem_)*
  static boolean loadModifications_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loadModifications_")) return false;
    if (!nextTokenIs(b, "", WITH, WITHOUT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = loadModificationItem_(b, l + 1);
    r = r && loadModifications__1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (listDelimiter_ loadModificationItem_)*
  private static boolean loadModifications__1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loadModifications__1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!loadModifications__1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "loadModifications__1", c)) break;
    }
    return true;
  }

  // listDelimiter_ loadModificationItem_
  private static boolean loadModifications__1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loadModifications__1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = listDelimiter_(b, l + 1);
    r = r && loadModificationItem_(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LOOPING pausedModifier? hiddenModifier?
  public static boolean loopingTrackModifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loopingTrackModifier")) return false;
    if (!nextTokenIs(b, LOOPING)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LOOPING_TRACK_MODIFIER, null);
    r = consumeToken(b, LOOPING);
    p = r; // pin = 1
    r = r && report_error_(b, loopingTrackModifier_1(b, l + 1));
    r = p && loopingTrackModifier_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // pausedModifier?
  private static boolean loopingTrackModifier_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loopingTrackModifier_1")) return false;
    pausedModifier(b, l + 1);
    return true;
  }

  // hiddenModifier?
  private static boolean loopingTrackModifier_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loopingTrackModifier_2")) return false;
    hiddenModifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // MANUAL autostartingModifier?
  public static boolean manualTrackModifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "manualTrackModifier")) return false;
    if (!nextTokenIs(b, MANUAL)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MANUAL_TRACK_MODIFIER, null);
    r = consumeToken(b, MANUAL);
    p = r; // pin = 1
    r = r && manualTrackModifier_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // autostartingModifier?
  private static boolean manualTrackModifier_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "manualTrackModifier_1")) return false;
    autostartingModifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // describedStatement | categoryStatement
  public static boolean metadataStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadataStatement")) return false;
    if (!nextTokenIs(b, "<metadata statement>", CATEGORIZED, DESCRIBED)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, METADATA_STATEMENT, "<metadata statement>");
    r = describedStatement(b, l + 1);
    if (!r) r = categoryStatement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // MUSIC musicEffectDefinition_
  public static boolean musicDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "musicDefinition")) return false;
    if (!nextTokenIs(b, MUSIC)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MUSIC_DEFINITION, null);
    r = consumeToken(b, MUSIC);
    p = r; // pin = 1
    r = r && musicEffectDefinition_(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // (metadataStatement sep_ )+
  static boolean musicEffectDefinitionDetails_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "musicEffectDefinitionDetails_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = musicEffectDefinitionDetails__0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!musicEffectDefinitionDetails__0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "musicEffectDefinitionDetails_", c)) break;
    }
    exit_section_(b, l, m, r, false, musicEffectDefinitionDetails_recover__parser_);
    return r;
  }

  // metadataStatement sep_
  private static boolean musicEffectDefinitionDetails__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "musicEffectDefinitionDetails__0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = metadataStatement(b, l + 1);
    r = r && sep_(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(CURLY_R | DESCRIBED | CATEGORIZED)
  static boolean musicEffectDefinitionDetails_recover_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "musicEffectDefinitionDetails_recover_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !musicEffectDefinitionDetails_recover__0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CURLY_R | DESCRIBED | CATEGORIZED
  private static boolean musicEffectDefinitionDetails_recover__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "musicEffectDefinitionDetails_recover__0")) return false;
    boolean r;
    r = consumeToken(b, CURLY_R);
    if (!r) r = consumeToken(b, DESCRIBED);
    if (!r) r = consumeToken(b, CATEGORIZED);
    return r;
  }

  /* ********************************************************** */
  // string FROM sampleRef playModifications_? ((CURLY_L musicEffectDefinitionDetails_ CURLY_R) | sep_)
  static boolean musicEffectDefinition_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "musicEffectDefinition_")) return false;
    if (!nextTokenIs(b, TEXT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string(b, l + 1);
    r = r && consumeToken(b, FROM);
    r = r && sampleRef(b, l + 1);
    r = r && musicEffectDefinition__3(b, l + 1);
    r = r && musicEffectDefinition__4(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // playModifications_?
  private static boolean musicEffectDefinition__3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "musicEffectDefinition__3")) return false;
    playModifications_(b, l + 1);
    return true;
  }

  // (CURLY_L musicEffectDefinitionDetails_ CURLY_R) | sep_
  private static boolean musicEffectDefinition__4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "musicEffectDefinition__4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = musicEffectDefinition__4_0(b, l + 1);
    if (!r) r = sep_(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // CURLY_L musicEffectDefinitionDetails_ CURLY_R
  private static boolean musicEffectDefinition__4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "musicEffectDefinition__4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CURLY_L);
    r = r && musicEffectDefinitionDetails_(b, l + 1);
    r = r && consumeToken(b, CURLY_R);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // WITHOUT CONVERSION CACHE
  public static boolean noConversionLoadModification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "noConversionLoadModification")) return false;
    if (!nextTokenIs(b, WITHOUT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NO_CONVERSION_LOAD_MODIFICATION, null);
    r = consumeTokens(b, 2, WITHOUT, CONVERSION, CACHE);
    p = r; // pin = 2
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // WITH OMISSION OF FIRST timespan
  public static boolean omissionPlayModification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "omissionPlayModification")) return false;
    if (!nextTokenIs(b, WITH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, OMISSION_PLAY_MODIFICATION, null);
    r = consumeTokens(b, 2, WITH, OMISSION, OF, FIRST);
    p = r; // pin = 2
    r = r && timespan(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // PARALLELLY CURLY_L statement statement+ CURLY_R
  public static boolean parallellyStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parallellyStatement")) return false;
    if (!nextTokenIs(b, PARALLELLY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARALLELLY_STATEMENT, null);
    r = consumeTokens(b, 1, PARALLELLY, CURLY_L);
    p = r; // pin = 1
    r = r && report_error_(b, statement(b, l + 1));
    r = p && report_error_(b, parallellyStatement_3(b, l + 1)) && r;
    r = p && consumeToken(b, CURLY_R) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // statement+
  private static boolean parallellyStatement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parallellyStatement_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "parallellyStatement_3", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ALL OTHER TRACKS
  public static boolean pauseAllOtherTracks(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pauseAllOtherTracks")) return false;
    if (!nextTokenIs(b, ALL)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PAUSE_ALL_OTHER_TRACKS, null);
    r = consumeTokens(b, 2, ALL, OTHER, TRACKS);
    p = r; // pin = 2
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ALL TRACKS
  public static boolean pauseAllTracks(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pauseAllTracks")) return false;
    if (!nextTokenIs(b, ALL)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PAUSE_ALL_TRACKS, null);
    r = consumeTokens(b, 1, ALL, TRACKS);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // PAUSE (pauseAllOtherTracks | pauseAllTracks | pauseThisTrack | trackNameReference_)
  public static boolean pauseStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pauseStatement")) return false;
    if (!nextTokenIs(b, PAUSE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PAUSE_STATEMENT, null);
    r = consumeToken(b, PAUSE);
    p = r; // pin = 1
    r = r && pauseStatement_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // pauseAllOtherTracks | pauseAllTracks | pauseThisTrack | trackNameReference_
  private static boolean pauseStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pauseStatement_1")) return false;
    boolean r;
    r = pauseAllOtherTracks(b, l + 1);
    if (!r) r = pauseAllTracks(b, l + 1);
    if (!r) r = pauseThisTrack(b, l + 1);
    if (!r) r = trackNameReference_(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // THIS TRACK
  public static boolean pauseThisTrack(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pauseThisTrack")) return false;
    if (!nextTokenIs(b, THIS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PAUSE_THIS_TRACK, null);
    r = consumeTokens(b, 1, THIS, TRACK);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // PAUSED
  public static boolean pausedModifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pausedModifier")) return false;
    if (!nextTokenIs(b, PAUSED)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PAUSED);
    exit_section_(b, m, PAUSED_MODIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // PERCENT
  public static boolean percentage(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "percentage")) return false;
    if (!nextTokenIs(b, PERCENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERCENT);
    exit_section_(b, m, PERCENTAGE, r);
    return r;
  }

  /* ********************************************************** */
  // amplificationPlayModification |
  //     omissionPlayModification |
  //     limitPlayModification
  static boolean playModificationItem_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "playModificationItem_")) return false;
    if (!nextTokenIs(b, WITH)) return false;
    boolean r;
    r = amplificationPlayModification(b, l + 1);
    if (!r) r = omissionPlayModification(b, l + 1);
    if (!r) r = limitPlayModification(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // playModificationItem_ (listDelimiter_ playModificationItem_)*
  static boolean playModifications_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "playModifications_")) return false;
    if (!nextTokenIs(b, WITH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = playModificationItem_(b, l + 1);
    r = r && playModifications__1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (listDelimiter_ playModificationItem_)*
  private static boolean playModifications__1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "playModifications__1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!playModifications__1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "playModifications__1", c)) break;
    }
    return true;
  }

  // listDelimiter_ playModificationItem_
  private static boolean playModifications__1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "playModifications__1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = listDelimiter_(b, l + 1);
    r = r && playModificationItem_(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PLAY SAMPLE? sampleRef playModifications_?
  public static boolean playStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "playStatement")) return false;
    if (!nextTokenIs(b, PLAY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PLAY_STATEMENT, null);
    r = consumeToken(b, PLAY);
    p = r; // pin = 1
    r = r && report_error_(b, playStatement_1(b, l + 1));
    r = p && report_error_(b, sampleRef(b, l + 1)) && r;
    r = p && playStatement_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // SAMPLE?
  private static boolean playStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "playStatement_1")) return false;
    consumeToken(b, SAMPLE);
    return true;
  }

  // playModifications_?
  private static boolean playStatement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "playStatement_3")) return false;
    playModifications_(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // RANDOMLY CURLY_L randomlyWeight randomlyWeight+ CURLY_R
  public static boolean randomlyStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "randomlyStatement")) return false;
    if (!nextTokenIs(b, RANDOMLY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RANDOMLY_STATEMENT, null);
    r = consumeTokens(b, 1, RANDOMLY, CURLY_L);
    p = r; // pin = 1
    r = r && report_error_(b, randomlyWeight(b, l + 1));
    r = p && report_error_(b, randomlyStatement_3(b, l + 1)) && r;
    r = p && consumeToken(b, CURLY_R) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // randomlyWeight+
  private static boolean randomlyStatement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "randomlyStatement_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = randomlyWeight(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!randomlyWeight(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "randomlyStatement_3", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // WEIGHTED WITH int statement
  public static boolean randomlyWeight(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "randomlyWeight")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RANDOMLY_WEIGHT, "<randomly weight>");
    r = consumeTokens(b, 1, WEIGHTED, WITH);
    p = r; // pin = 1
    r = r && report_error_(b, int_$(b, l + 1));
    r = p && statement(b, l + 1) && r;
    exit_section_(b, l, m, r, p, randomly_recover__parser_);
    return r || p;
  }

  /* ********************************************************** */
  // !(CURLY_R | WEIGHTED)
  static boolean randomly_recover_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "randomly_recover_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !randomly_recover__0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CURLY_R | WEIGHTED
  private static boolean randomly_recover__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "randomly_recover__0")) return false;
    boolean r;
    r = consumeToken(b, CURLY_R);
    if (!r) r = consumeToken(b, WEIGHTED);
    return r;
  }

  /* ********************************************************** */
  // REPEAT (simpleRepeatStatement_ | complexRepeatStatement_) TIMES statement
  public static boolean repeatStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "repeatStatement")) return false;
    if (!nextTokenIs(b, REPEAT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, REPEAT_STATEMENT, null);
    r = consumeToken(b, REPEAT);
    p = r; // pin = 1
    r = r && report_error_(b, repeatStatement_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, TIMES)) && r;
    r = p && statement(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // simpleRepeatStatement_ | complexRepeatStatement_
  private static boolean repeatStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "repeatStatement_1")) return false;
    boolean r;
    r = simpleRepeatStatement_(b, l + 1);
    if (!r) r = complexRepeatStatement_(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // LOOPING TRACKS
  public static boolean resumeLoopingTracksStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resumeLoopingTracksStatement")) return false;
    if (!nextTokenIs(b, LOOPING)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RESUME_LOOPING_TRACKS_STATEMENT, null);
    r = consumeTokens(b, 1, LOOPING, TRACKS);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // RESUME (trackNameReference_ | resumeLoopingTracksStatement)
  public static boolean resumeStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resumeStatement")) return false;
    if (!nextTokenIs(b, RESUME)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RESUME_STATEMENT, null);
    r = consumeToken(b, RESUME);
    p = r; // pin = 1
    r = r && resumeStatement_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // trackNameReference_ | resumeLoopingTracksStatement
  private static boolean resumeStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resumeStatement_1")) return false;
    boolean r;
    r = trackNameReference_(b, l + 1);
    if (!r) r = resumeLoopingTracksStatement(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // !<<eof>> rootContent
  static boolean root(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = root_0(b, l + 1);
    r = r && rootContent(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !<<eof>>
  private static boolean root_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !eof(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // rootItem_*
  public static boolean rootContent(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rootContent")) return false;
    Marker m = enter_section_(b, l, _NONE_, ROOT_CONTENT, "<root content>");
    while (true) {
      int c = current_position_(b);
      if (!rootItem_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "rootContent", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // sep_ |
  //     (includeDefinition sep_) |
  //     (loadDefinition sep_) |
  //     includableDefinitions_ |
  //     soundscapeDefinition |
  //     musicDefinition |
  //     effectDefinition
  static boolean rootItem_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rootItem_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = sep_(b, l + 1);
    if (!r) r = rootItem__1(b, l + 1);
    if (!r) r = rootItem__2(b, l + 1);
    if (!r) r = includableDefinitions_(b, l + 1);
    if (!r) r = soundscapeDefinition(b, l + 1);
    if (!r) r = musicDefinition(b, l + 1);
    if (!r) r = effectDefinition(b, l + 1);
    exit_section_(b, l, m, r, false, root_item_recover__parser_);
    return r;
  }

  // includeDefinition sep_
  private static boolean rootItem__1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rootItem__1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = includeDefinition(b, l + 1);
    r = r && sep_(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // loadDefinition sep_
  private static boolean rootItem__2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rootItem__2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = loadDefinition(b, l + 1);
    r = r && sep_(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(SOUNDSCAPE | INCLUDE | INCLUDABLE | LOAD | MUSIC | EFFECT)
  static boolean root_item_recover_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_item_recover_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !root_item_recover__0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SOUNDSCAPE | INCLUDE | INCLUDABLE | LOAD | MUSIC | EFFECT
  private static boolean root_item_recover__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_item_recover__0")) return false;
    boolean r;
    r = consumeToken(b, SOUNDSCAPE);
    if (!r) r = consumeToken(b, INCLUDE);
    if (!r) r = consumeToken(b, INCLUDABLE);
    if (!r) r = consumeToken(b, LOAD);
    if (!r) r = consumeToken(b, MUSIC);
    if (!r) r = consumeToken(b, EFFECT);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean sampleId(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sampleId")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, SAMPLE_ID, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean sampleRef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sampleRef")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, SAMPLE_REF, r);
    return r;
  }

  /* ********************************************************** */
  // SEPARATOR
  static boolean sep_(PsiBuilder b, int l) {
    return consumeToken(b, SEPARATOR);
  }

  /* ********************************************************** */
  // int
  static boolean simpleRepeatStatement_(PsiBuilder b, int l) {
    return int_$(b, l + 1);
  }

  /* ********************************************************** */
  // timespan
  static boolean simpleSleepStatement_(PsiBuilder b, int l) {
    return timespan(b, l + 1);
  }

  /* ********************************************************** */
  // SLEEP (simpleSleepStatement_ | complexSleepStatement_)
  public static boolean sleepStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sleepStatement")) return false;
    if (!nextTokenIs(b, SLEEP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SLEEP_STATEMENT, null);
    r = consumeToken(b, SLEEP);
    p = r; // pin = 1
    r = r && sleepStatement_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // simpleSleepStatement_ | complexSleepStatement_
  private static boolean sleepStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sleepStatement_1")) return false;
    boolean r;
    r = simpleSleepStatement_(b, l + 1);
    if (!r) r = complexSleepStatement_(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (
  //     sep_ |
  //     (includeSoundscapeDefinition sep_) |
  //     (loadDefinition sep_) |
  //     includableTrackDefinition_ |
  //     trackDefinition |
  //     (metadataStatement sep_)
  //     )+
  static boolean soundscapeBlockStatement_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "soundscapeBlockStatement_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = soundscapeBlockStatement__0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!soundscapeBlockStatement__0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "soundscapeBlockStatement_", c)) break;
    }
    exit_section_(b, l, m, r, false, soundscapeBlockStatement_recover__parser_);
    return r;
  }

  // sep_ |
  //     (includeSoundscapeDefinition sep_) |
  //     (loadDefinition sep_) |
  //     includableTrackDefinition_ |
  //     trackDefinition |
  //     (metadataStatement sep_)
  private static boolean soundscapeBlockStatement__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "soundscapeBlockStatement__0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sep_(b, l + 1);
    if (!r) r = soundscapeBlockStatement__0_1(b, l + 1);
    if (!r) r = soundscapeBlockStatement__0_2(b, l + 1);
    if (!r) r = includableTrackDefinition_(b, l + 1);
    if (!r) r = trackDefinition(b, l + 1);
    if (!r) r = soundscapeBlockStatement__0_5(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // includeSoundscapeDefinition sep_
  private static boolean soundscapeBlockStatement__0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "soundscapeBlockStatement__0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = includeSoundscapeDefinition(b, l + 1);
    r = r && sep_(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // loadDefinition sep_
  private static boolean soundscapeBlockStatement__0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "soundscapeBlockStatement__0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = loadDefinition(b, l + 1);
    r = r && sep_(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // metadataStatement sep_
  private static boolean soundscapeBlockStatement__0_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "soundscapeBlockStatement__0_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = metadataStatement(b, l + 1);
    r = r && sep_(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(CURLY_R | LOAD | LOOPING | MANUAL | INCLUDABLE | INCLUDE | DESCRIBED | CATEGORIZED)
  static boolean soundscapeBlockStatement_recover_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "soundscapeBlockStatement_recover_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !soundscapeBlockStatement_recover__0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CURLY_R | LOAD | LOOPING | MANUAL | INCLUDABLE | INCLUDE | DESCRIBED | CATEGORIZED
  private static boolean soundscapeBlockStatement_recover__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "soundscapeBlockStatement_recover__0")) return false;
    boolean r;
    r = consumeToken(b, CURLY_R);
    if (!r) r = consumeToken(b, LOAD);
    if (!r) r = consumeToken(b, LOOPING);
    if (!r) r = consumeToken(b, MANUAL);
    if (!r) r = consumeToken(b, INCLUDABLE);
    if (!r) r = consumeToken(b, INCLUDE);
    if (!r) r = consumeToken(b, DESCRIBED);
    if (!r) r = consumeToken(b, CATEGORIZED);
    return r;
  }

  /* ********************************************************** */
  // SOUNDSCAPE string CURLY_L soundscapeBlockStatement_* CURLY_R
  public static boolean soundscapeDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "soundscapeDefinition")) return false;
    if (!nextTokenIs(b, SOUNDSCAPE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SOUNDSCAPE_DEFINITION, null);
    r = consumeToken(b, SOUNDSCAPE);
    p = r; // pin = 1
    r = r && report_error_(b, string(b, l + 1));
    r = p && report_error_(b, consumeToken(b, CURLY_L)) && r;
    r = p && report_error_(b, soundscapeDefinition_3(b, l + 1)) && r;
    r = p && consumeToken(b, CURLY_R) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // soundscapeBlockStatement_*
  private static boolean soundscapeDefinition_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "soundscapeDefinition_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!soundscapeBlockStatement_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "soundscapeDefinition_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // block |
  //     sep_ |
  //     (playStatement sep_) |
  //     (sleepStatement sep_) |
  //     (pauseStatement sep_) |
  //     (resumeStatement sep_) |
  //     (doNothingStatement sep_) |
  //     (includeTrackStatement sep_) |
  //     repeatStatement |
  //     randomlyStatement |
  //     parallellyStatement
  public static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT, "<statement>");
    r = block(b, l + 1);
    if (!r) r = sep_(b, l + 1);
    if (!r) r = statement_2(b, l + 1);
    if (!r) r = statement_3(b, l + 1);
    if (!r) r = statement_4(b, l + 1);
    if (!r) r = statement_5(b, l + 1);
    if (!r) r = statement_6(b, l + 1);
    if (!r) r = statement_7(b, l + 1);
    if (!r) r = repeatStatement(b, l + 1);
    if (!r) r = randomlyStatement(b, l + 1);
    if (!r) r = parallellyStatement(b, l + 1);
    exit_section_(b, l, m, r, false, statement_recover__parser_);
    return r;
  }

  // playStatement sep_
  private static boolean statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_2")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = playStatement(b, l + 1);
    p = r; // pin = 1
    r = r && sep_(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // sleepStatement sep_
  private static boolean statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_3")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = sleepStatement(b, l + 1);
    p = r; // pin = 1
    r = r && sep_(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // pauseStatement sep_
  private static boolean statement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_4")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = pauseStatement(b, l + 1);
    p = r; // pin = 1
    r = r && sep_(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // resumeStatement sep_
  private static boolean statement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_5")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = resumeStatement(b, l + 1);
    p = r; // pin = 1
    r = r && sep_(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // doNothingStatement sep_
  private static boolean statement_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_6")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = doNothingStatement(b, l + 1);
    p = r; // pin = 1
    r = r && sep_(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // includeTrackStatement sep_
  private static boolean statement_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_7")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = includeTrackStatement(b, l + 1);
    p = r; // pin = 1
    r = r && sep_(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // !(CURLY_L | CURLY_R | sep_ | PLAY | SLEEP | REPEAT | PAUSE | RESUME | RANDOMLY | PARALLELLY | WEIGHTED | INCLUDE)
  static boolean statement_recover_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_recover_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !statement_recover__0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CURLY_L | CURLY_R | sep_ | PLAY | SLEEP | REPEAT | PAUSE | RESUME | RANDOMLY | PARALLELLY | WEIGHTED | INCLUDE
  private static boolean statement_recover__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_recover__0")) return false;
    boolean r;
    r = consumeToken(b, CURLY_L);
    if (!r) r = consumeToken(b, CURLY_R);
    if (!r) r = sep_(b, l + 1);
    if (!r) r = consumeToken(b, PLAY);
    if (!r) r = consumeToken(b, SLEEP);
    if (!r) r = consumeToken(b, REPEAT);
    if (!r) r = consumeToken(b, PAUSE);
    if (!r) r = consumeToken(b, RESUME);
    if (!r) r = consumeToken(b, RANDOMLY);
    if (!r) r = consumeToken(b, PARALLELLY);
    if (!r) r = consumeToken(b, WEIGHTED);
    if (!r) r = consumeToken(b, INCLUDE);
    return r;
  }

  /* ********************************************************** */
  // TEXT
  public static boolean string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string")) return false;
    if (!nextTokenIs(b, TEXT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TEXT);
    exit_section_(b, m, STRING, r);
    return r;
  }

  /* ********************************************************** */
  // DURATION
  public static boolean timespan(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "timespan")) return false;
    if (!nextTokenIs(b, DURATION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DURATION);
    exit_section_(b, m, TIMESPAN, r);
    return r;
  }

  /* ********************************************************** */
  // trackModifier_ TRACK trackId (WITH TITLE string)? block
  public static boolean trackDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trackDefinition")) return false;
    if (!nextTokenIs(b, "<track definition>", LOOPING, MANUAL)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TRACK_DEFINITION, "<track definition>");
    r = trackModifier_(b, l + 1);
    r = r && consumeToken(b, TRACK);
    p = r; // pin = 2
    r = r && report_error_(b, trackId(b, l + 1));
    r = p && report_error_(b, trackDefinition_3(b, l + 1)) && r;
    r = p && block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (WITH TITLE string)?
  private static boolean trackDefinition_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trackDefinition_3")) return false;
    trackDefinition_3_0(b, l + 1);
    return true;
  }

  // WITH TITLE string
  private static boolean trackDefinition_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trackDefinition_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, WITH, TITLE);
    r = r && string(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean trackId(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trackId")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, TRACK_ID, r);
    return r;
  }

  /* ********************************************************** */
  // loopingTrackModifier | manualTrackModifier
  static boolean trackModifier_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trackModifier_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = loopingTrackModifier(b, l + 1);
    if (!r) r = manualTrackModifier(b, l + 1);
    exit_section_(b, l, m, r, false, track_recover__parser_);
    return r;
  }

  /* ********************************************************** */
  // TRACK trackRef
  static boolean trackNameReference_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trackNameReference_")) return false;
    if (!nextTokenIs(b, TRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, TRACK);
    p = r; // pin = 1
    r = r && trackRef(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean trackRef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trackRef")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, TRACK_REF, r);
    return r;
  }

  /* ********************************************************** */
  // !TRACK
  static boolean track_recover_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "track_recover_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, TRACK);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  static final Parser musicEffectDefinitionDetails_recover__parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return musicEffectDefinitionDetails_recover_(b, l + 1);
    }
  };
  static final Parser randomly_recover__parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return randomly_recover_(b, l + 1);
    }
  };
  static final Parser root_item_recover__parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return root_item_recover_(b, l + 1);
    }
  };
  static final Parser soundscapeBlockStatement_recover__parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return soundscapeBlockStatement_recover_(b, l + 1);
    }
  };
  static final Parser statement_recover__parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return statement_recover_(b, l + 1);
    }
  };
  static final Parser track_recover__parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return track_recover_(b, l + 1);
    }
  };
}
