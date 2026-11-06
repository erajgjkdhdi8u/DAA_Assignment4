import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;

public class Experiment {
    private static final int RUNS = 5;

    public static void main(String[] args) throws IOException {
        Files.createDirectories(Path.of("results"));
        Random random = new Random(42);

        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(
                Path.of("results/results.csv"), StandardCharsets.UTF_8))) {
            out.println("algorithm,input,n,other_length,run,time_ms,operations,result");
            runEditDistance(out, random);
            runSearch(out, random);
        }
        System.out.println("Finished. Open results/results.csv");
    }

    private static void runEditDistance(PrintWriter out, Random random) {
        EditDistance[] methods = {
                new RecursiveEditDistance(),
                new TableEditDistance(),
                new TwoRowsEditDistance()
        };
        String[] names = {"recursive", "table", "two_rows"};
        String[] inputTypes = {"random", "different"};

        for (String type : inputTypes) {
            int[][] groups = {{2, 4, 6, 8}, {100, 200, 400, 800}};
            for (int group = 0; group < groups.length; group++) {
                for (int n : groups[group]) {
                    String a;
                    String b;
                    if (type.equals("random")) {
                        a = InputGenerator.randomString(random, n);
                        b = InputGenerator.randomString(random, n);
                    } else {
                        a = InputGenerator.repeated('A', n);
                        b = InputGenerator.repeated('B', n);
                    }

                    int first = group == 0 ? 0 : 1;
                    int last = group == 0 ? 1 : 3;
                    for (int k = first; k < last; k++) {
                        methods[k].distance(a, b); // warm-up, not saved
                        for (int run = 1; run <= RUNS; run++) {
                            long start = System.nanoTime();
                            int answer = methods[k].distance(a, b);
                            long elapsed = System.nanoTime() - start;
                            save(out, names[k], type, n, n, run,
                                    elapsed, methods[k].getCount(), answer);
                        }
                    }
                }
            }
        }
    }

    private static void runSearch(PrintWriter out, Random random) {
        StringSearch[] methods = {new NaiveSearch(), new KMPSearch()};
        String[] names = {"naive", "kmp"};
        String[] inputTypes = {"random", "difficult"};
        int[] sizes = {20000, 40000, 80000, 160000};
        int patternLength = 128;

        for (String type : inputTypes) {
            for (int n : sizes) {
                String text;
                String pattern;
                if (type.equals("random")) {
                    text = InputGenerator.randomString(random, n);
                    pattern = InputGenerator.randomString(random, patternLength);
                } else {
                    text = InputGenerator.repeated('A', n);
                    pattern = InputGenerator.repeated('A', patternLength - 1) + "B";
                }

                for (int k = 0; k < methods.length; k++) {
                    methods[k].search(text, pattern); // warm-up, not saved
                    for (int run = 1; run <= RUNS; run++) {
                        long start = System.nanoTime();
                        int found = methods[k].search(text, pattern).size();
                        long elapsed = System.nanoTime() - start;
                        save(out, names[k], type, n, patternLength, run,
                                elapsed, methods[k].getComparisons(), found);
                    }
                }
            }
        }
    }

    private static void save(PrintWriter out, String algorithm, String input,
                             int n, int otherLength, int run, long elapsedNs,
                             long operations, int answer) {
        double timeMs = elapsedNs / 1_000_000.0;
        out.printf(Locale.US, "%s,%s,%d,%d,%d,%.6f,%d,%d%n",
                algorithm, input, n, otherLength, run, timeMs, operations, answer);
    }
}
