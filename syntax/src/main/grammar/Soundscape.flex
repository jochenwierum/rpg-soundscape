package de.jowisoftware.rpgsoundscape.language.lexer;

import static de.jowisoftware.rpgsoundscape.language.parser.SoundscapeParserDefinitionStub.*;
import static de.jowisoftware.rpgsoundscape.language.psi.SoundscapeTypes.*;

%%

%class SoundscapeLexer
%implements com.intellij.lexer.FlexLexer
%unicode
%function advance
%type com.intellij.psi.tree.IElementType
%caseless
%eof{  return;
%eof}


%{
  int commentDepth = 0;
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = [ \t\f] | {LineTerminator}

/* comments */
CommentStart = \/\*+
CommentEnd = \*+\/
CommentContent = ( [^*/] | \*+ [^/*] | \/+ [^/*] )+

// Comment can be the last line of the file, without line terminator.
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

DIGIT=[:digit:]
HEX={DIGIT}+ | [aAbBcCdDeEfF]

ESC="\\" ( [^] | "u" {HEX}{HEX}{HEX}{HEX})
CHAR=[^\r\n\'\"\\]

IntegerNumber={DIGIT}+
Percentage=("-"|"+")? {DIGIT}+ "%"
Duration={DIGIT}+ ("ms" | "s" | "m")

STRING_BAD1=\" ({CHAR} | {ESC} | \')*
STRING_BAD2=\' ({CHAR} | {ESC} | \")*
String={STRING_BAD1} \" | {STRING_BAD2} \'

BAD_TOKENS={STRING_BAD1} | {STRING_BAD2}

Identifier=[:jletter:] [:jletterdigit:]*

%state COMMENT

%%

// ignore
<YYINITIAL> {
  {WhiteSpace} { return WHITE_SPACE; }

  {EndOfLineComment} { return LINE_COMMENT; }
  {CommentStart} {
            yybegin(COMMENT);
            commentDepth = 1;
            return COMMENT_START;
        }
}

<COMMENT> {
    // in case we are in comment, move step deeper and treat it as comment content
    {CommentStart} {
          commentDepth++;
          return COMMENT_CONTENT;
      }

    // either we move level up in nested comments, and treat this as content
    // or we have closed last nested comment
    {CommentEnd} {
          if(0 == --commentDepth) {
              yybegin(YYINITIAL);
              return COMMENT_END;
          }
          return COMMENT_CONTENT;
      }

    // while we are inside,  everything is just comment
    {CommentContent} { return COMMENT_CONTENT; }
}


<YYINITIAL> {
    ";" { return SEPARATOR; }

    "all" { return ALL; }
    "amplification" { return AMPLIFICATION; }
    "and" { return AND; }
    "as" { return AS; }
    "attribution" { return ATTRIBUTION; }
    "autostarting" { return AUTOSTARTING; }
    "between" { return BETWEEN; }
    "by" { return BY; }
    "cache" {return CACHE; }
    "categorized" { return CATEGORIZED; }
    "conversion" {return CONVERSION; }
    "described" { return DESCRIBED; }
    "do" { return DO; }
    "effect" { return EFFECT; }
    "first" { return FIRST; }
    "from" { return FROM; }
    "in" { return IN; }
    "includable" { return INCLUDABLE; }
    "include" { return INCLUDE; }
    "includes" { return INCLUDES; }
    "limit" { return LIMIT; }
    "load" { return LOAD; }
    "looping" { return LOOPING; }
    "manual" { return MANUAL; }
    "music" { return MUSIC; }
    "nothing" { return NOTHING; }
    "of" { return OF; }
    "omission" { return OMISSION; }
    "other" { return OTHER; }
    "parallelly" { return PARALLELLY; }
    "pause" { return PAUSE; }
    "paused" { return PAUSED; }
    "play" { return PLAY; }
    "randomly" { return RANDOMLY; }
    "repeat" { return REPEAT; }
    "resume" { return RESUME; }
    "sample" { return SAMPLE; }
    "sleep" { return SLEEP; }
    "soundscape" { return SOUNDSCAPE; }
    "this" { return THIS; }
    "times" { return TIMES; }
    "title" { return TITLE; }
    "to" { return TO; }
    "track" { return TRACK; }
    "tracks" { return TRACKS; }
    "weighted" { return WEIGHTED; }
    "with" { return WITH; }
    "without" { return WITHOUT; }

    "{" { return CURLY_L; }
    "}" { return CURLY_R; }

    "," { return LIST_DELIMITER; }
    {String} { return TEXT; }
    {Percentage} { return PERCENT; }
    {Duration} { return DURATION; }
    {IntegerNumber} { return NUM_INTEGER; }

    {Identifier} { return IDENTIFIER; }
}

{BAD_TOKENS} { /*yybegin(YYINITIAL);*/ return BAD_CHARACTER; }
[^] { /*yybegin(YYINITIAL);*/ return BAD_CHARACTER; }
