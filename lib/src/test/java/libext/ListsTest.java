package libext;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
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
  private final IntFunction<String> stringIntFunction = index -> "Item" + (index + 1);
  private final IntFunction<String> duplicateIntFunction = index -> "Duplicate";
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
      List<String> list = Lists.newArrayList("A", "B", "C");
      assertIterableEquals(Arrays.asList("A", "B", "C"), list);

      list.add("D");
      assertIterableEquals(Arrays.asList("A", "B", "C", "D"), list);
    }

    @Test
    void shouldCreateEmptyArrayListFromEmptyVarargs() {
      List<String> list = Lists.newArrayList();
      assertTrue(list.isEmpty());
    }

    @Test
    void shouldCreateResizableArrayListFromSupplier() {
      List<String> list = Lists.newArrayList(3, stringSupplier);
      assertIterableEquals(Arrays.asList("Item1", "Item2", "Item3"), list);

      list.add("Item4");
      assertIterableEquals(Arrays.asList("Item1", "Item2", "Item3", "Item4"), list);
    }

    @Test
    void shouldCreateResizableArrayListFromIntFunction() {
      List<String> list = Lists.newArrayList(3, stringIntFunction);
      assertIterableEquals(Arrays.asList("Item1", "Item2", "Item3"), list);

      list.add("Item4");
      assertIterableEquals(Arrays.asList("Item1", "Item2", "Item3", "Item4"), list);
    }

    @Test
    void shouldThrowExceptionWhenListSizeIsNegativeWithSupplier() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Lists.newArrayList(-1, stringSupplier));
      assertEquals("size must be >= 0", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenListSizeIsNegativeWithIntFunction() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Lists.newArrayList(-1, stringIntFunction));
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
          () -> Lists.newArrayList(5, (Supplier<String>) null));
      assertEquals("no valid supplier provided", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenIntFunctionIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class,
          () -> Lists.newArrayList(5, (IntFunction<String>) null));
      assertEquals("no valid function provided", ex.getMessage());
    }

    @Test
    void shouldCreateResizableArrayListFromReader() {
      InputStream inputStream = new ByteArrayInputStream(MULTI_LINE_TEXT.getBytes());
      BufferedReader reader = Readers.newBufferedReader(inputStream);
      assertNotNull(reader);

      List<String> list = Lists.newArrayList(reader);
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

      List<String> list = Lists.newArrayList(reader);
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
      List<Integer> list = Lists.newLinkedList(1, 2, 3);
      assertIterableEquals(Arrays.asList(1, 2, 3), list);

      list.add(4);
      assertIterableEquals(Arrays.asList(1, 2, 3, 4), list);
    }

    @Test
    void shouldCreateEmptyLinkedListFromEmptyVarargs() {
      List<Integer> list = Lists.newLinkedList();
      assertTrue(list.isEmpty());
    }

    @Test
    void shouldCreateResizableLinkedListFromSupplier() {
      List<String> list = Lists.newLinkedList(2, stringSupplier);
      assertIterableEquals(Arrays.asList("Item1", "Item2"), list);

      list.add("Item3");
      assertIterableEquals(Arrays.asList("Item1", "Item2", "Item3"), list);
    }

    @Test
    void shouldCreateResizableLinkedListFromIntFunction() {
      List<String> list = Lists.newLinkedList(2, stringIntFunction);
      assertIterableEquals(Arrays.asList("Item1", "Item2"), list);

      list.add("Item3");
      assertIterableEquals(Arrays.asList("Item1", "Item2", "Item3"), list);
    }

    @Test
    void shouldThrowExceptionWhenListSizeIsNegativeWithSupplier() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Lists.newLinkedList(-1, stringSupplier));
      assertEquals("size must be >= 0", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenListSizeIsNegativeWithIntFunction() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Lists.newLinkedList(-1, stringIntFunction));
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
          () -> Lists.newLinkedList(5, (Supplier<String>) null));
      assertEquals("no valid supplier provided", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenIntFunctionIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class,
          () -> Lists.newLinkedList(5, (IntFunction<String>) null));
      assertEquals("no valid function provided", ex.getMessage());
    }

    @Test
    void shouldCreateResizableLinkedListFromReader() {
      InputStream inputStream = new ByteArrayInputStream(MULTI_LINE_TEXT.getBytes());
      BufferedReader reader = Readers.newBufferedReader(inputStream);
      assertNotNull(reader);

      List<String> list = Lists.newLinkedList(reader);
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

      List<String> list = Lists.newLinkedList(reader);
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
      Set<String> set = Lists.newLinkedHashSet("Z", "A", "B");
      assertIterableEquals(Arrays.asList("Z", "A", "B"), set);

      set.add("C");
      assertIterableEquals(Arrays.asList("Z", "A", "B", "C"), set);
    }

    @Test
    void shouldCreateResizableLinkedHashSetNoDuplicatesFromVarargs() {
      Set<String> set = Lists.newLinkedHashSet("A", "A", "B");
      assertIterableEquals(Arrays.asList("A", "B"), set);

      set.add("A");
      set.add("B");
      set.add("C");
      assertIterableEquals(Arrays.asList("A", "B", "C"), set);
    }

    @Test
    void shouldCreateResizableLinkedHashSetNoDuplicatesFromSupplier() {
      Set<String> set = Lists.newLinkedHashSet(3, duplicateSupplier);
      assertEquals(1, set.size());
      assertTrue(set.contains("Duplicate"));

      set.add("Duplicate");
      set.add("Not A Duplicate");
      assertEquals(2, set.size());
      assertTrue(set.contains("Duplicate"));
      assertTrue(set.contains("Not A Duplicate"));
    }

    @Test
    void shouldCreateResizableLinkedHashSetNoDuplicatesFromIntFunction() {
      Set<String> set = Lists.newLinkedHashSet(3, duplicateIntFunction);
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
      Set<String> set = Lists.newLinkedHashSet();
      assertTrue(set.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenSetSizeIsNegativeWithSupplier() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Lists.newLinkedHashSet(-5, stringSupplier));
      assertEquals("size must be >= 0", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSetSizeIsNegativeWithIntFunction() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Lists.newLinkedHashSet(-5, stringIntFunction));
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
          () -> Lists.newLinkedHashSet(5, (Supplier<String>) null));
      assertEquals("no valid supplier provided", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenIntFunctionIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class,
          () -> Lists.newLinkedHashSet(5, (IntFunction<String>) null));
      assertEquals("no valid function provided", ex.getMessage());
    }

    @Test
    void shouldCreateResizableLinkedHashSetFromReader() {
      InputStream inputStream = new ByteArrayInputStream(MULTI_LINE_TEXT.getBytes());
      BufferedReader reader = Readers.newBufferedReader(inputStream);
      assertNotNull(reader);

      Set<String> set = Lists.newLinkedHashSet(reader);
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

      Set<String> set = Lists.newLinkedHashSet(reader);
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

  @Nested
  class ConversionTests {

    @Test
    void shouldConvertToLongArray() {
      List<String> input = Arrays.asList("10", "20", "30");
      long[] expected = {10L, 20L, 30L};
      assertArrayEquals(expected, Lists.toLongArray(input));
    }

    @Test
    void shouldReturnEmptyLongArrayForEmptyList() {
      long[] result = Lists.toLongArray(Collections.emptyList());
      assertNotNull(result);
      assertEquals(0, result.length);
    }

    @Test
    void shouldConvertToLongList() {
      List<String> input = Arrays.asList("100", "200");
      List<Long> expected = Arrays.asList(100L, 200L);
      assertIterableEquals(expected, Lists.toLongList(input));
    }

    @Test
    void shouldReturnEmptyLongListForEmptyList() {
      List<Long> result = Lists.toLongList(Collections.emptyList());
      assertNotNull(result);
      assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertToDoubleArray() {
      List<String> input = Arrays.asList("1.5", "2.75", "3.0");
      double[] expected = {1.5, 2.75, 3.0};
      assertArrayEquals(expected, Lists.toDoubleArray(input));
    }

    @Test
    void shouldReturnEmptyDoubleArrayForEmptyList() {
      double[] result = Lists.toDoubleArray(Collections.emptyList());
      assertNotNull(result);
      assertEquals(0, result.length);
    }

    @Test
    void shouldConvertToDoubleList() {
      List<String> input = Arrays.asList("0.1", "0.2");
      List<Double> expected = Arrays.asList(0.1, 0.2);
      assertIterableEquals(expected, Lists.toDoubleList(input));
    }

    @Test
    void shouldReturnEmptyDoubleListForEmptyList() {
      List<Double> result = Lists.toDoubleList(Collections.emptyList());
      assertNotNull(result);
      assertTrue(result.isEmpty());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldThrowNullPointerExceptionWhenListIsNull() {
      NullPointerException exLongArray = assertThrows(NullPointerException.class,
          () -> Lists.toLongArray(null));
      assertEquals("no valid list provided", exLongArray.getMessage());

      NullPointerException exLongList = assertThrows(NullPointerException.class,
          () -> Lists.toLongList(null));
      assertEquals("no valid list provided", exLongList.getMessage());

      NullPointerException exDoubleArray = assertThrows(NullPointerException.class,
          () -> Lists.toDoubleArray(null));
      assertEquals("no valid list provided", exDoubleArray.getMessage());

      NullPointerException exDoubleList = assertThrows(NullPointerException.class,
          () -> Lists.toDoubleList(null));
      assertEquals("no valid list provided", exDoubleList.getMessage());
    }

    @Test
    void shouldThrowNumberFormatExceptionForInvalidNumericStrings() {
      List<String> invalidInput = Arrays.asList("123", "abc");
      assertThrows(NumberFormatException.class,
          () -> Lists.toLongArray(invalidInput));
      assertThrows(NumberFormatException.class,
          () -> Lists.toLongList(invalidInput));
      assertThrows(NumberFormatException.class,
          () -> Lists.toDoubleArray(invalidInput));
      assertThrows(NumberFormatException.class,
          () -> Lists.toDoubleList(invalidInput));
    }
  }
}
