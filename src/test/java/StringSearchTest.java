import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class StringSearchTest {
    StringSearch[] methods = {new NaiveSearch(), new KMPSearch()};

    @Test
    void lpsReferenceValues() {
        KMPSearch kmp = new KMPSearch();
        assertArrayEquals(new int[]{0, 1, 2, 0, 1, 2, 3, 3}, kmp.lps("AAACAAAA"));
        assertArrayEquals(new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4}, kmp.lps("ABABCABAB"));
    }

    @Test
    void searchReferenceValues() {
        for (StringSearch method : methods) {
            assertEquals(List.of(10), method.search("ABABDABACDABABCABAB", "ABABCABAB"));
            assertEquals(List.of(0, 1, 2, 3), method.search("AAAAA", "AA"));
            assertEquals(List.of(0, 2, 4), method.search("ABABABA", "ABA"));
            assertEquals(List.of(), method.search("ABCDE", "ZZ"));
            assertEquals(List.of(), method.search("AB", "ABC"));
            assertEquals(List.of(0), method.search("ABC", "ABC"));
            assertEquals(List.of(), method.search("", "A"));
        }
    }

    @Test
    void emptyPatternThrows() {
        for (StringSearch method : methods) {
            assertThrows(IllegalArgumentException.class, () -> method.search("abc", ""));
        }
    }

    @Test
    void hundredRandomTextsAgree() {
        Random random = new Random(123);
        for (int k = 0; k < 100; k++) {
            String text = InputGenerator.randomString(random, random.nextInt(26));
            String pattern = InputGenerator.randomString(random, 1 + random.nextInt(7));
            assertEquals(methods[0].search(text, pattern), methods[1].search(text, pattern),
                    text + " / " + pattern);
        }
    }

    @Test
    void comparisonsAreCollected() {
        for (StringSearch method : methods) {
            method.search("ABABAB", "ABA");
            assertTrue(method.getComparisons() > 0);
        }
    }
}
