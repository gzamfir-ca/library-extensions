package libext;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ReadersTest {

  @Test
  void newBufferedReader() {
    String s = """
                            one   two three
                                    four             five six seven
                                                     eight nine              ten
                                                                    \s""";
    InputStream inputStream = new ByteArrayInputStream(s.getBytes());
    BufferedReader bufferedReader = Readers.newBufferedReader(inputStream);
    var input = new ArrayList<String>();
    boolean b = Readers.addAll(input, bufferedReader);
    var expected = Arrays.asList("one", "two", "three", "four",
        "five", "six", "seven", "eight", "nine", "ten");
    assertTrue(b);
    assertIterableEquals(expected, input);
  }
}
