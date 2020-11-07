package de.jowisoftware.rpgsoundscape.intellij.settings;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import de.jowisoftware.rpgsoundscape.intellij.highlight.SoundscapeSyntaxHighlighter;
import de.jowisoftware.rpgsoundscape.language.SoundscapeIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SoundscapeSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Comment", SoundscapeSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Identifier", SoundscapeSyntaxHighlighter.IDENTIFIER),
            new AttributesDescriptor("String", SoundscapeSyntaxHighlighter.STRING),
            new AttributesDescriptor("Number", SoundscapeSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("Number with Unit", SoundscapeSyntaxHighlighter.NUMBER_WITH_UNIT),
            new AttributesDescriptor("Braces", SoundscapeSyntaxHighlighter.BRACES),
            new AttributesDescriptor("Section", SoundscapeSyntaxHighlighter.SECTION),
            new AttributesDescriptor("Keyword", SoundscapeSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("Comma", SoundscapeSyntaxHighlighter.COMMA),
            new AttributesDescriptor("Semicolon", SoundscapeSyntaxHighlighter.SEMICOLON),
            new AttributesDescriptor("Detail", SoundscapeSyntaxHighlighter.DETAIL)
    };

    private static final String demoString;

    static {
        String s = "error";
        try {
            s = new String(
                    SoundscapeSettingsPage.class.getResourceAsStream("/demoFile.soundscape").readAllBytes(),
                    StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
        demoString = s;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return SoundscapeIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new SoundscapeSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return demoString;
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Soundscape";
    }
}
