package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test for the {@link LimitedFileInputStream}
 * Created by jrbuckeridge on 7/5/16.
 */
public class LimitedFileInputStreamTest {

    private static final byte[] FILE_CONTENT = "0123456789abcdefghij54321qwerty".getBytes();

    private static class SkipMaxCombination {
        final int skip;
        final int max;

        public SkipMaxCombination(int skip, int max) {
            this.skip = skip;
            this.max = max;
        }
    }

    @Test
    public void testRead() {

        List<SkipMaxCombination> combinations = new ArrayList<SkipMaxCombination>();
        combinations.add(new SkipMaxCombination(10, 5));
        combinations.add(new SkipMaxCombination(20, 5));
        combinations.add(new SkipMaxCombination(20, 10));
        combinations.add(new SkipMaxCombination(0, 15));
        combinations.add(new SkipMaxCombination(3, 12));

        for (SkipMaxCombination combination : combinations) {
            try {
                LimitedFileInputStream inputStream =
                        new LimitedFileInputStream(getClass().getResource("/limited-input.test").getFile(),
                                combination.skip,
                                combination.max);

                byte[] bytes = new byte[combination.max];
                // could improve the test by relying on read bytes, but there's no need because the numbers are really small
                int read = inputStream.read(bytes);
                assertArrayEquals(getArray(combination.skip, combination.max), bytes);
                System.out.println("Read: " + new String(bytes));

            } catch (IOException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }
    }

    private byte[] getArray(int skip, int max) {
        return Arrays.copyOfRange(FILE_CONTENT, skip, skip + max);
    }
}
