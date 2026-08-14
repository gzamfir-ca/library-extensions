package libext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ListsTest {

  private final Supplier<String> stringSupplier = new Supplier<>() {
    private int count = 0;

    @Override
    public String get() {
      return "Item" + (++count);
    }
  };
  private final Supplier<String> duplicateSupplier = () -> "Duplicate";
  private static final String MULTI_LINE_TEXT = """
      one   two three
              four             five six seven
                                              eight
                                              \s""";
  private static final List<String> EXPECTED_TOKENS = Arrays.asList(
      "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
  );

  @Nested
  class ArrayListTests {

    @Test
    void shouldCreateResizableArrayListFromVarargs() {
      ArrayList<String> list = Lists.newArrayList("A", "B", "C");
      assertIterableEquals(Arrays.asList("A", "B", "C"), list);
      list.add("D");
      assertIterableEquals(Arrays.asList("A", "B", "C", "D"), list);
    }

    @Test
    void shouldCreateEmptyArrayListFromEmptyVarargs() {
      ArrayList<String> list = Lists.newArrayList();
      assertTrue(list.isEmpty());
    }

    @Test
    void shouldCreateResizableArrayListFromSupplier() {
      ArrayList<String> list = Lists.newArrayList(3, stringSupplier);
      assertIterableEquals(Arrays.asList("Item1", "Item2", "Item3"), list);
      list.add("Item4");
      assertIterableEquals(Arrays.asList("Item1", "Item2", "Item3", "Item4"), list);
    }

    @Test
    void shouldThrowExceptionWhenListSizeIsNegative() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Lists.newArrayList(-1, stringSupplier));
      assertEquals("size must be >= 0", ex.getMessage());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldThrowExceptionWhenVarargsArrayIsNull() {
      String[] nullArray = null;
      NullPointerException ex = assertThrows(NullPointerException.class,
          () -> Lists.newArrayList(nullArray));
      assertEquals("no valid elements provided", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSupplierIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class,
          () -> Lists.newArrayList(5, null));
      assertEquals("no valid supplier provided", ex.getMessage());
    }

    @Test
    void shouldCreateResizableArrayListFromReader() {
      InputStream inputStream = new ByteArrayInputStream(MULTI_LINE_TEXT.getBytes());
      BufferedReader reader = Readers.newBufferedReader(inputStream);
      assertNotNull(reader);
      ArrayList<String> list = Lists.newArrayList(reader);
      assertNotNull(list);
      list.add("nine");
      assertIterableEquals(EXPECTED_TOKENS, list);
    }

    @Test
    void shouldCreateResizableArrayListFromPath() throws IOException {
      Path path = Path.of("src/test/resources/readersTest.txt");
      assertNotNull(path);
      BufferedReader reader = Readers.newBufferedReader(path);
      assertNotNull(reader);
      ArrayList<String> list = Lists.newArrayList(reader);
      assertNotNull(list);
      list.add("nine");
      assertIterableEquals(EXPECTED_TOKENS, list);
    }

    @Test
    void shouldThrowExceptionWhenReaderIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class, () ->
          Lists.newArrayList((BufferedReader) null)
      );
      assertEquals("no valid reader provided", ex.getMessage());
    }
  }

  @Nested
  class LinkedListTests {

    @Test
    void shouldCreateResizableLinkedListFromVarargs() {
      LinkedList<Integer> list = Lists.newLinkedList(1, 2, 3);
      assertIterableEquals(Arrays.asList(1, 2, 3), list);
      list.add(4);
      assertIterableEquals(Arrays.asList(1, 2, 3, 4), list);
    }

    @Test
    void shouldCreateEmptyLinkedListFromEmptyVarargs() {
      LinkedList<Integer> list = Lists.newLinkedList();
      assertTrue(list.isEmpty());
    }

    @Test
    void shouldCreateResizableLinkedListFromSupplier() {
      LinkedList<String> list = Lists.newLinkedList(2, stringSupplier);
      assertIterableEquals(Arrays.asList("Item1", "Item2"), list);
      list.add("Item3");
      assertIterableEquals(Arrays.asList("Item1", "Item2", "Item3"), list);
    }

    @Test
    void shouldThrowExceptionWhenListSizeIsNegative() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Lists.newLinkedList(-1, stringSupplier));
      assertEquals("size must be >= 0", ex.getMessage());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldThrowExceptionWhenVarargsArrayIsNull() {
      String[] nullArray = null;
      NullPointerException ex = assertThrows(NullPointerException.class,
          () -> Lists.newLinkedList(nullArray));
      assertEquals("no valid elements provided", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSupplierIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class,
          () -> Lists.newLinkedList(5, null));
      assertEquals("no valid supplier provided", ex.getMessage());
    }

    @Test
    void shouldCreateResizableLinkedListFromReader() {
      InputStream inputStream = new ByteArrayInputStream(MULTI_LINE_TEXT.getBytes());
      BufferedReader reader = Readers.newBufferedReader(inputStream);
      assertNotNull(reader);
      LinkedList<String> list = Lists.newLinkedList(reader);
      assertNotNull(list);
      list.add("nine");
      assertIterableEquals(EXPECTED_TOKENS, list);
    }

    @Test
    void shouldCreateResizableLinkedListFromPath() throws IOException {
      Path path = Path.of("src/test/resources/readersTest.txt");
      assertNotNull(path);
      BufferedReader reader = Readers.newBufferedReader(path);
      assertNotNull(reader);
      LinkedList<String> list = Lists.newLinkedList(reader);
      assertNotNull(list);
      list.add("nine");
      assertIterableEquals(EXPECTED_TOKENS, list);
    }

    @Test
    void shouldThrowExceptionWhenReaderIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class, () ->
          Lists.newLinkedList((BufferedReader) null)
      );
      assertEquals("no valid reader provided", ex.getMessage());
    }
  }

  @Nested
  class LinkedHashSetTests {

    @Test
    void shouldCreateResizableLinkedHashSetAndPreserveOrder() {
      LinkedHashSet<String> set = Lists.newLinkedHashSet("Z", "A", "B");
      assertIterableEquals(Arrays.asList("Z", "A", "B"), set);
      set.add("C");
      assertIterableEquals(Arrays.asList("Z", "A", "B", "C"), set);
    }

    @Test
    void shouldCreateResizableLinkedHashSetNoDuplicatesFromVarargs() {
      LinkedHashSet<String> set = Lists.newLinkedHashSet("A", "A", "B");
      assertIterableEquals(Arrays.asList("A", "B"), set);
      set.add("A");
      set.add("B");
      set.add("C");
      assertIterableEquals(Arrays.asList("A", "B", "C"), set);
    }

    @Test
    void shouldCreateResizableLinkedHashSetNoDuplicatesFromSupplier() {
      LinkedHashSet<String> set = Lists.newLinkedHashSet(3, duplicateSupplier);
      assertEquals(1, set.size());
      assertTrue(set.contains("Duplicate"));
      set.add("Duplicate");
      set.add("Not A Duplicate");
      assertEquals(2, set.size());
      assertTrue(set.contains("Duplicate"));
      assertTrue(set.contains("Not A Duplicate"));
    }

    @Test
    void shouldCreateEmptyLinkedHashSetFromEmptyVarargs() {
      LinkedHashSet<String> set = Lists.newLinkedHashSet();
      assertTrue(set.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenSetSizeIsNegative() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Lists.newLinkedHashSet(-5, stringSupplier));
      assertEquals("size must be >= 0", ex.getMessage());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldThrowExceptionWhenVarargsArrayIsNull() {
      String[] nullArray = null;
      NullPointerException ex = assertThrows(NullPointerException.class,
          () -> Lists.newLinkedHashSet(nullArray));
      assertEquals("no valid elements provided", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSupplierIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class,
          () -> Lists.newLinkedHashSet(5, null));
      assertEquals("no valid supplier provided", ex.getMessage());
    }

    @Test
    void shouldCreateResizableLinkedHashSetFromReader() {
      InputStream inputStream = new ByteArrayInputStream(MULTI_LINE_TEXT.getBytes());
      BufferedReader reader = Readers.newBufferedReader(inputStream);
      assertNotNull(reader);
      LinkedHashSet<String> set = Lists.newLinkedHashSet(reader);
      assertNotNull(set);
      set.add("nine");
      assertIterableEquals(EXPECTED_TOKENS, set);
    }

    @Test
    void shouldCreateResizableLinkedHashSetFromPath() throws IOException {
      Path path = Path.of("src/test/resources/readersTest.txt");
      assertNotNull(path);
      BufferedReader reader = Readers.newBufferedReader(path);
      assertNotNull(reader);
      LinkedHashSet<String> set = Lists.newLinkedHashSet(reader);
      assertNotNull(set);
      set.add("nine");
      assertIterableEquals(EXPECTED_TOKENS, set);
    }

    @Test
    void shouldThrowExceptionWhenReaderIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class, () ->
          Lists.newLinkedHashSet((BufferedReader) null)
      );
      assertEquals("no valid reader provided", ex.getMessage());
    }
  }
}
