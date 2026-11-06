public class TwoRowsEditDistance implements EditDistance {
    private long cells;

    // State: previous[j] and current[j] store values for two neighboring DP rows.
    // Transition: same min(top, left, diagonal) rule as the full DP table.
    // Base case: first row is 0..m, and each new row starts with its row number.
    public int distance(String a, String b) {
        cells = 0;
        int n = a.length();
        int m = b.length();
        int[] previous = new int[m + 1];
        int[] current = new int[m + 1];

        for (int j = 0; j <= m; j++) previous[j] = j;

        for (int i = 1; i <= n; i++) {
            current[0] = i;
            for (int j = 1; j <= m; j++) {
                cells++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    current[j] = previous[j - 1];
                } else {
                    int best = Math.min(previous[j], current[j - 1]);
                    current[j] = 1 + Math.min(best, previous[j - 1]);
                }
            }
            int[] temp = previous;
            previous = current;
            current = temp;
        }
        return previous[m];
    }

    public long getCount() {
        return cells;
    }
}
