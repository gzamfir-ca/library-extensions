package libext;

import static libext.Maps.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map.Entry;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Map;

class MapsTest {

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
  }
}
