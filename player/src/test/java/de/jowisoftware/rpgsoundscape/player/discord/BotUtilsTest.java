package de.jowisoftware.rpgsoundscape.player.discord;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class BotUtilsTest {

    @Test
    public void testSplit() {
        testSplit("""
                  ein test
                  """, "ein", "test");
        testSplit("""
                  ein\\ test
                  """, "ein test");
        testSplit("""
                  ein "etwas anderer" Test
                  """, "ein", "etwas anderer", "Test");
        testSplit("""
                  ein "etwas'anderer" Test
                  """, "ein", "etwas'anderer", "Test");
        testSplit("""
                  ein 'etwas"anderer' Test
                  """, "ein", "etwas\"anderer", "Test");
        testSplit("""
                  ein 'etwas\\'anderer' Test
                  """, "ein", "etwas'anderer", "Test");
    }

    private void testSplit(String input, String... expected) {
        List<String> actual = BotUtils.parseString(input.replaceAll("\\n", ""));

        @SuppressWarnings("unchecked") Matcher<? super String>[] matchers =
                Arrays.stream(expected)
                        .map(CoreMatchers::equalTo)
                        .toArray(Matcher[]::new);
        assertThat(actual, contains(matchers));
    }

}
