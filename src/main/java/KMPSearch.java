import java.util.ArrayList;
import java.util.List;

public class KMPSearch implements StringSearch {
    private long comparisons;

    public int[] lps(String pattern) {
        comparisons = 0;
        return buildLps(pattern);
    }

    private int[] buildLps(String pattern) {
        int[] result = new int[pattern.length()];
        int length = 0;
        int i = 1;

        while (i < pattern.length()) {
            comparisons++;
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                result[i] = length;
                i++;
            } else if (length > 0) {
                length = result[length - 1];
            } else {
                result[i] = 0;
                i++;
            }
        }
        return result;
    }

    public List<Integer> search(String text, String pattern) {
        if (pattern.isEmpty()) throw new IllegalArgumentException("Empty pattern");
        comparisons = 0;
        List<Integer> positions = new ArrayList<>();
        if (pattern.length() > text.length()) return positions;

        int[] prefix = buildLps(pattern);
        int i = 0;
        int j = 0;

        while (i < text.length()) {
            comparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern.length()) {
                    positions.add(i - j);
                    j = prefix[j - 1];
                }
            } else if (j > 0) {
                j = prefix[j - 1];
            } else {
                i++;
            }
        }
        return positions;
    }

    public long getComparisons() {
        return comparisons;
    }
}
