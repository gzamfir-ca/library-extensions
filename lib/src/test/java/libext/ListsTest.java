package libext;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;
import org.junit.jupiter.api.Test;

class ListsTest {

  static class CopyableRecord implements Copyable<CopyableRecord> {

    int id;
    String name;

    public CopyableRecord(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public CopyableRecord(CopyableRecord other) {
      this.id = other.id;
      this.name = other.name;
    }

    @Override
    public CopyableRecord copy() {
      return new CopyableRecord(this);
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      CopyableRecord that = (CopyableRecord) o;
      return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, name);
    }

    @Override
    public String toString() {
      return "CopyableRecord [id=" + id + ", name=" + name + "]";
    }
  }

  @Test
  void newArrayListFromStream() {
    String s = """
        one   two three
                four             five six seven
                                 eight nine              ten
                                                \s""";
    InputStream inputStream = new ByteArrayInputStream(s.getBytes());
    try (BufferedReader bufferedReader = Readers.newBufferedReader(inputStream)) {
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
    try (BufferedReader bufferedReader = Readers.newBufferedReader(path)) {
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
  void newArrayListFromString() {
    final String line = "one, two;three  four,,five;;six seven eight,, nine;; ten";
    final String regex = "[,;\\s]+";
    var expected = Arrays.asList("one", "two", "three", "four",
        "five", "six", "seven", "eight", "nine", "ten");
    var tokens = Lists.newArrayList(line, regex);
    assertIterableEquals(expected, tokens);
  }

  @Test
  void newArrayListFromIterable() {
    var original = new ArrayList<>(Arrays.asList(
        new CopyableRecord(1, "one"),
        new CopyableRecord(2, "two")
    ));
    var expected = new ArrayList<>(Arrays.asList(
        new CopyableRecord(1, "one"),
        new CopyableRecord(2, "two")
    ));
    var copy = Lists.newArrayList(original);
    original.getFirst().id = 10;
    original.getLast().name = "ten";
    assertIterableEquals(expected, copy);
  }

  @Test
  void newArrayListFromSupplier() {
    Random random = new Random(1234567890L);
    int N = 10;
    var initialized = Lists.newArrayList(N, () -> random.nextInt(10 * N));
    var expected = Arrays.asList(77, 42, 21, 22, 65, 16, 99, 32, 79, 71);
    assertIterableEquals(expected, initialized);
  }

  @Test
  void newLinkedListFromStream() {
    String s = """
        one   two three
                four             five six seven
                                 eight nine              ten
                                                \s""";
    InputStream inputStream = new ByteArrayInputStream(s.getBytes());
    try (BufferedReader bufferedReader = Readers.newBufferedReader(inputStream)) {
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
    try (BufferedReader bufferedReader = Readers.newBufferedReader(path)) {
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
  void newLinkedListFromString() {
    final String line = "one, two;three  four,,five;;six seven eight,, nine;; ten";
    final String regex = "[,;\\s]+";
    var expected = Arrays.asList("one", "two", "three", "four",
        "five", "six", "seven", "eight", "nine", "ten");
    var tokens = Lists.newLinkedList(line, regex);
    assertIterableEquals(expected, tokens);
  }

  @Test
  void newLinkedListFromIterable() {
    var original = new LinkedList<>(Arrays.asList(
        new CopyableRecord(1, "one"),
        new CopyableRecord(2, "two")
    ));
    var expected = new LinkedList<>(Arrays.asList(
        new CopyableRecord(1, "one"),
        new CopyableRecord(2, "two")
    ));
    var copy = Lists.newLinkedList(original);
    original.getFirst().id = 10;
    original.getLast().name = "ten";
    assertIterableEquals(expected, copy);
  }

  @Test
  void newLinkedListFromSupplier() {
    Random random = new Random(1234567890L);
    int N = 10;
    var initialized = Lists.newLinkedList(N, () -> random.nextInt(10 * N));
    var expected = Arrays.asList(77, 42, 21, 22, 65, 16, 99, 32, 79, 71);
    assertIterableEquals(expected, initialized);
  }

  @Test
  void newLinkedHashSetFromStream() {
    String s = """
        one   two three
                four             five six seven
                                 eight nine              ten
                                                \s""";
    InputStream inputStream = new ByteArrayInputStream(s.getBytes());
    try (BufferedReader bufferedReader = Readers.newBufferedReader(inputStream)) {
      assertNotNull(bufferedReader);
      var input = Lists.newLinkedHashSet(bufferedReader);
      var expected = Arrays.asList("one", "two", "three", "four",
          "five", "six", "seven", "eight", "nine", "ten");
      assertIterableEquals(expected, input);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void newLinkedHashSetFromPath() {
    Path path = Path.of("src/test/resources/readersTest.txt");
    assertNotNull(path);
    try (BufferedReader bufferedReader = Readers.newBufferedReader(path)) {
      assertNotNull(bufferedReader);
      var input = Lists.newLinkedHashSet(bufferedReader);
      var expected = Arrays.asList("one", "two", "three", "four",
          "five", "six", "seven", "eight", "nine", "ten");
      assertIterableEquals(expected, input);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void newLinkedHashSetFromString() {
    final String line = "one, two;three  four,,five;;six seven eight,, nine;; ten";
    final String regex = "[,;\\s]+";
    var expected = Arrays.asList("one", "two", "three", "four",
        "five", "six", "seven", "eight", "nine", "ten");
    var tokens = Lists.newLinkedHashSet(line, regex);
    assertIterableEquals(expected, tokens);
  }

  @Test
  void newLinkedHashSetFromIterable() {
    var original = new LinkedHashSet<>(Arrays.asList(
        new CopyableRecord(1, "one"),
        new CopyableRecord(2, "two")
    ));
    var expected = new LinkedHashSet<>(Arrays.asList(
        new CopyableRecord(1, "one"),
        new CopyableRecord(2, "two")
    ));
    var copy = Lists.newLinkedHashSet(original);
    original.getFirst().id = 10;
    original.getLast().name = "ten";
    assertIterableEquals(expected, copy);
  }

  @Test
  void newLinkedHashSetFromSupplier() {
    Random random = new Random(1234567890L);
    int N = 10;
    var initialized = Lists.newLinkedHashSet(N, () -> random.nextInt(10 * N));
    var expected = Arrays.asList(77, 42, 21, 22, 65, 16, 99, 32, 79, 71);
    assertIterableEquals(expected, initialized);
  }
}
