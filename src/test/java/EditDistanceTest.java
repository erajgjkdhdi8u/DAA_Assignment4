import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class EditDistanceTest {
    EditDistance[] methods = {
            new RecursiveEditDistance(),
            new TableEditDistance(),
            new TwoRowsEditDistance()
    };

    @Test
    void referenceValues() {
        for (EditDistance method : methods) {
            assertEquals(3, method.distance("kitten", "sitting"));
            assertEquals(2, method.distance("flaw", "lawn"));
            assertEquals(5, method.distance("intention", "execution"));
            assertEquals(3, method.distance("", "abc"));
            assertEquals(0, method.distance("abc", "abc"));
            assertEquals(0, method.distance("", ""));
            assertEquals(2, method.distance("ab", ""));
        }
    }

    @Test
    void hundredRandomPairsAgree() {
        Random random = new Random(123);
        for (int k = 0; k < 100; k++) {
            String a = InputGenerator.randomString(random, random.nextInt(6));
            String b = InputGenerator.randomString(random, random.nextInt(6));
            int expected = methods[0].distance(a, b);
            assertEquals(expected, methods[1].distance(a, b), a + " / " + b);
            assertEquals(expected, methods[2].distance(a, b), a + " / " + b);
        }
    }

    @Test
    void countIsCollected() {
        for (EditDistance method : methods) {
            method.distance("ab", "cd");
            assertTrue(method.getCount() > 0);
        }
    }
}
