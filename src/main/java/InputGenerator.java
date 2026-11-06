import java.util.Random;

public class InputGenerator {
    public static String randomString(Random random, int length) {
        StringBuilder result = new StringBuilder();
        String letters = "ABCD";
        for (int i = 0; i < length; i++) {
            result.append(letters.charAt(random.nextInt(letters.length())));
        }
        return result.toString();
    }

    public static String repeated(char letter, int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) result.append(letter);
        return result.toString();
    }
}
