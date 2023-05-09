package libext;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

import static libext.IOStream.newPrintWriter;
import static libext.IOStream.readAllIntegers;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class IOStreamTest {

  @Test
  public void testInput() {
    final String input = """
                    1   2 3
        4             5 6 7



                 8 9         \s""";
    final List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

    ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
    List<Integer> output = readAllIntegers(stream);

    assertIterableEquals(expected, output);
  }
}
