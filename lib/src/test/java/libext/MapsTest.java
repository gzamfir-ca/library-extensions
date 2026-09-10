package libext;

import static libext.Maps.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map.Entry;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Map;

class MapsTest {

  private static final String MULTI_LINE_TEXT = "key1 value1\nkey2 value2";

  @Nested
  class HashMapTests {

    @Test
    void shouldCreateResizableHashMapAndContainEntries() {
      Map<String, Integer> map = Maps.newHashMap(entry("A", 1), entry("B", 2));
      assertEquals(2, map.size());
      assertEquals(1, map.get("A"));
      assertEquals(2, map.get("B"));

      map.put("C", 3);
      assertEquals(3, map.size());
      assertEquals(3, map.get("C"));
    }

    @Test
    void shouldOverwriteDuplicateKeysUsingLastWinsRule() {
      Map<String, Integer> map = Maps.newHashMap(entry("A", 1), entry("A", 99));
      assertEquals(1, map.size());
      assertEquals(99, map.get("A"));
    }

    @Test
    void shouldCreateEmptyHashMapFromEmptyVarargs() {
      Map<String, Integer> map = Maps.newHashMap();
      assertTrue(map.isEmpty());

      map.put("A", 1);
      assertEquals(1, map.size());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldThrowExceptionWhenVarargsArrayIsNull() {
      Entry<String, Integer>[] nullArray = null;
      NullPointerException ex = assertThrows(NullPointerException.class, () ->
          Maps.newHashMap(nullArray)
      );
      assertEquals("no valid entries provided", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAnIndividualEntryIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class, () ->
          Maps.newHashMap(entry("A", 1), null, entry("B", 2))
      );
      assertNotNull(ex);
    }

    @Test
    void shouldCreateResizableHashMapFromReader() {
      InputStream inputStream = new ByteArrayInputStream(MULTI_LINE_TEXT.getBytes());
      BufferedReader reader = Readers.newBufferedReader(inputStream);
      assertNotNull(reader);

      Map<String, String> map = Maps.newHashMap(reader);
      assertNotNull(map);
      assertEquals("value1", map.get("key1"));
      assertEquals("value2", map.get("key2"));

      map.put("key3", "value3");
      assertEquals(3, map.size());
    }

    @Test
    void shouldThrowExceptionWhenHashMapReaderIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class, () ->
          Maps.newHashMap((BufferedReader) null)
      );
      assertEquals("no valid reader provided", ex.getMessage());
    }
  }

  @Nested
  class LinkedHashMapTests {

    @Test
    void shouldCreateResizableLinkedHashMapAndPreserveOrder() {
      Map<String, Integer> map = Maps.newLinkedHashMap(entry("Z", 26), entry("A", 1),
          entry("B", 2));
      var iterator = map.entrySet().iterator();
      assertTrue(iterator.hasNext());

      Entry<String, Integer> first = iterator.next();
      assertEquals("Z", first.getKey());
      assertEquals(26, first.getValue());
      assertTrue(iterator.hasNext());
      assertEquals("A", iterator.next().getKey());
      assertTrue(iterator.hasNext());
      assertEquals("B", iterator.next().getKey());

      map.put("C", 3);
      assertEquals(4, map.size());
    }

    @Test
    void shouldCreateEmptyLinkedHashMapFromEmptyVarargs() {
      Map<String, Integer> map = Maps.newLinkedHashMap();
      assertTrue(map.isEmpty());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldThrowExceptionWhenVarargsArrayIsNull() {
      Entry<String, Integer>[] nullArray = null;
      NullPointerException ex = assertThrows(NullPointerException.class, () ->
          Maps.newLinkedHashMap(nullArray)
      );
      assertEquals("no valid entries provided", ex.getMessage());
    }

    @Test
    void shouldCreateLinkedHashMapUsingFunctionAndPreserveOrder() {
      Map<String, Integer> map = Maps.newLinkedHashMap(String::length, "Apple", "Banana", "Zebra");
      assertEquals(3, map.size());

      var iterator = map.entrySet().iterator();
      assertTrue(iterator.hasNext());

      Entry<String, Integer> first = iterator.next();
      assertEquals("Apple", first.getKey());
      assertEquals(5, first.getValue());
      assertTrue(iterator.hasNext());

      Entry<String, Integer> second = iterator.next();
      assertEquals("Banana", second.getKey());
      assertEquals(6, second.getValue());
      assertTrue(iterator.hasNext());

      Entry<String, Integer> third = iterator.next();
      assertEquals("Zebra", third.getKey());
      assertEquals(5, third.getValue());

      map.put("Kiwi", 4);
      assertEquals(4, map.size());
    }

    @Test
    void shouldCreateEmptyLinkedHashMapFromFunctionAndEmptyVarargs() {
      Map<String, Integer> map = Maps.newLinkedHashMap(String::length);
      assertTrue(map.isEmpty());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldThrowExceptionWhenFunctionIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class,
          () -> Maps.newLinkedHashMap(null, "Key1", "Key2"));
      assertEquals("no valid function provided", ex.getMessage());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldThrowExceptionWhenFunctionVarargsArrayIsNull() {
      String[] nullKeys = null;
      NullPointerException ex = assertThrows(NullPointerException.class,
          () -> Maps.newLinkedHashMap(String::length, nullKeys));
      assertEquals("no valid keys provided", ex.getMessage());
    }

    @Test
    void shouldCreateResizableLinkedHashMapFromReaderAndPreserveOrder() {
      InputStream inputStream = new ByteArrayInputStream(MULTI_LINE_TEXT.getBytes());
      BufferedReader reader = Readers.newBufferedReader(inputStream);
      assertNotNull(reader);

      Map<String, String> map = Maps.newLinkedHashMap(reader);
      assertNotNull(map);

      var iterator = map.keySet().iterator();
      assertTrue(iterator.hasNext());
      assertEquals("key1", iterator.next());
      assertTrue(iterator.hasNext());
      assertEquals("key2", iterator.next());

      map.put("key3", "value3");
      assertEquals(3, map.size());
    }

    @Test
    void shouldThrowExceptionWhenLinkedHashMapReaderIsNull() {
      NullPointerException ex = assertThrows(NullPointerException.class, () ->
          Maps.newLinkedHashMap((BufferedReader) null)
      );
      assertEquals("no valid reader provided", ex.getMessage());
    }
  }
}
