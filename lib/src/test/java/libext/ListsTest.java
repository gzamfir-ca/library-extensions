package libext;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ListsTest {

  @Test
  void newArrayList() {
    String s = """
                            one   two three
                                    four             five six seven
                                                     eight nine              ten
                                                                    \s""";
    InputStream inputStream = new ByteArrayInputStream(s.getBytes());
    BufferedReader bufferedReader = Readers.newBufferedReader(inputStream);
    var input = Lists.newArrayList(bufferedReader);
    var expected = Arrays.asList("one", "two", "three", "four",
        "five", "six", "seven", "eight", "nine", "ten");
    assertIterableEquals(expected, input);
  }

  @Test
  void newLinkedList() {
    String s = """
                            one   two three
                                    four             five six seven
                                                     eight nine              ten
                                                                    \s""";
    InputStream inputStream = new ByteArrayInputStream(s.getBytes());
    BufferedReader bufferedReader = Readers.newBufferedReader(inputStream);
    var input = Lists.newLinkedList(bufferedReader);
    var expected = Arrays.asList("one", "two", "three", "four",
        "five", "six", "seven", "eight", "nine", "ten");
    assertIterableEquals(expected, input);
  }
}
