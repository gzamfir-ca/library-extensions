package libext;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ListsTest {

  @Test
  void newArrayListFromStream() {
    String s = """
                            one   two three
                                    four             five six seven
                                                     eight nine              ten
                                                                    \s""";
    InputStream inputStream = new ByteArrayInputStream(s.getBytes());
    try(BufferedReader bufferedReader = Readers.newBufferedReader(inputStream)) {
      assertNotNull(bufferedReader);
      var input = Lists.newArrayList(bufferedReader);
      var expected = Arrays.asList("one", "two", "three", "four",
          "five", "six", "seven", "eight", "nine", "ten");
      assertIterableEquals(expected, input);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void newArrayListFromPath() {
    Path path = Path.of("src/test/resources/readersTest.txt");
    assertNotNull(path);
    try(BufferedReader bufferedReader = Readers.newBufferedReader(path)) {
      assertNotNull(bufferedReader);
      var input = Lists.newArrayList(bufferedReader);
      var expected = Arrays.asList("one", "two", "three", "four",
          "five", "six", "seven", "eight", "nine", "ten");
      assertIterableEquals(expected, input);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void newLinkedListFromStream() {
    String s = """
                            one   two three
                                    four             five six seven
                                                     eight nine              ten
                                                                    \s""";
    InputStream inputStream = new ByteArrayInputStream(s.getBytes());
    try(BufferedReader bufferedReader = Readers.newBufferedReader(inputStream)) {
      assertNotNull(bufferedReader);
      var input = Lists.newLinkedList(bufferedReader);
      var expected = Arrays.asList("one", "two", "three", "four",
          "five", "six", "seven", "eight", "nine", "ten");
      assertIterableEquals(expected, input);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void newLinkedListFromPath() {
    Path path = Path.of("src/test/resources/readersTest.txt");
    assertNotNull(path);
    try(BufferedReader bufferedReader = Readers.newBufferedReader(path)) {
      assertNotNull(bufferedReader);
      var input = Lists.newLinkedList(bufferedReader);
      var expected = Arrays.asList("one", "two", "three", "four",
          "five", "six", "seven", "eight", "nine", "ten");
      assertIterableEquals(expected, input);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}