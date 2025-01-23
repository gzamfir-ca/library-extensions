package libext;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ReadersTest {

  @Test
  void newBufferedReaderFromStream() {
    String s = """
                            one   two three
                                    four             five six seven
                                                     eight nine              ten
                                                                    \s""";
    InputStream inputStream = new ByteArrayInputStream(s.getBytes());
    try(BufferedReader bufferedReader = Readers.newBufferedReader(inputStream)) {
      assertNotNull(bufferedReader);
      var input = new ArrayList<String>();
      boolean b = Readers.addAll(input, bufferedReader);
      var expected = Arrays.asList("one", "two", "three", "four",
          "five", "six", "seven", "eight", "nine", "ten");
      assertTrue(b);
      assertIterableEquals(expected, input);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void newBufferedReaderFromPath() {
    Path path = Path.of("src/test/resources/readersTest.txt");
    assertNotNull(path);
    try(BufferedReader bufferedReader = Readers.newBufferedReader(path)) {
      assertNotNull(bufferedReader);
      var input = new ArrayList<String>();
      boolean b = Readers.addAll(input, bufferedReader);
      var expected = Arrays.asList("one", "two", "three", "four",
          "five", "six", "seven", "eight", "nine", "ten");
      assertTrue(b);
      assertIterableEquals(expected, input);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}