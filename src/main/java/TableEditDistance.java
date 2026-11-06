public class TableEditDistance implements EditDistance {
    private long cells;

    // State: dp[i][j] is the distance between the first i and j characters.
    // Transition: copy diagonal on a match, otherwise 1 + min(top, left, diagonal).
    // Base case: dp[i][0] = i and dp[0][j] = j.
    public int distance(String a, String b) {
        cells = 0;
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                cells++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int best = Math.min(dp[i - 1][j], dp[i][j - 1]);
                    dp[i][j] = 1 + Math.min(best, dp[i - 1][j - 1]);
                }
            }
        }
        return dp[n][m];
    }

    public long getCount() {
        return cells;
    }
}
