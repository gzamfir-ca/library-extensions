package libext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

class MultimapTest {

  private Multimap<String, String> map;

  @BeforeEach
  void setUp() {
    map = Multimap.newMultimap();
  }

  private void addValueToList(List<String> list) {
    list.add("v3");
  }

  @Test
  void shouldAddValueCorrectlyAndReturnUnmodifiableList() {
    List<String> list1 = map.addValue("k1", "v1");
    List<String> list2 = map.addValue("k1", "v2");
    assertEquals(2, list2.size());
    assertIterableEquals(List.of("v1", "v2"), list2);
    assertThrows(UnsupportedOperationException.class, () -> addValueToList(list1));
  }

  @Test
  void shouldRemoveValueCorrectlyAndReturnUnmodifiableList() {
    map.addValue("k1", "v1");
    map.addValue("k1", "v2");
    List<String> remaining = map.removeValue("k1", "v1");
    assertIterableEquals(List.of("v2"), remaining);
    assertThrows(UnsupportedOperationException.class, () -> {
      assertNotNull(remaining);
      remaining.add("v3");
    });
    List<String> lastRemove = map.removeValue("k1", "v2");
    assertNotNull(lastRemove);
    assertTrue(lastRemove.isEmpty());
    assertFalse(map.containsKey("k1"));
  }

  @Test
  void shouldRecoverValueCorrectlyForMissingKeys() {
    assertNull(map.removeValue("missing", "v1"));

    List<String> emptyList = map.valueList("missing");
    assertTrue(emptyList.isEmpty());
    assertThrows(UnsupportedOperationException.class,
        () -> addValueToList(emptyList));
  }

  @Test
  void shouldAggregateValueCorrectlyAcrossKeys() {
    map.addValue("k1", "v1");
    map.addValue("k2", "v2");
    map.addValue("k2", "v3");
    Collection<String> flattened = map.flattenedValues();
    assertEquals(3, flattened.size());
    assertTrue(flattened.containsAll(List.of("v1", "v2", "v3")));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void shouldClearAllElementsCorrectly() {
    map.addValue("k1", "v1");
    assertFalse(map.isEmpty());

    map.clear();
    assertTrue(map.isEmpty());
    assertEquals(0, map.size());
  }

  @Test
  void shouldProtectValuesViewAgainstExternalModifications() {
    map.addValue("k1", "v1");
    Collection<List<String>> valuesView = map.values();
    assertThrows(UnsupportedOperationException.class, valuesView::clear);
  }

  @Test
  void shouldProtectEntrySetAndIndividualEntriesFromMutation() {
    Multimap<String, String> multimap = Multimap.newMultimap();
    multimap.addValue("Apple", "Fuji");
    Set<Map.Entry<String, List<String>>> entriesToTest = multimap.entrySet();
    assertThrows(UnsupportedOperationException.class, entriesToTest::clear);

    Map.Entry<String, List<String>> entry = entriesToTest.iterator().next();
    assertThrows(UnsupportedOperationException.class, () -> entry.setValue(new ArrayList<>()));
  }

  @Test
  void shouldProtectKeySetAgainstExternalModifications() {
    map.addValue("k1", "v1");
    Set<String> keySetView = map.keySet();
    assertThrows(UnsupportedOperationException.class, keySetView::clear);
  }

  @Test
  void shouldObeyEqualsAndHashCodeAndToStringContracts() {
    map.addValue("k1", "v1");
    Multimap<String, String> matchingCustomMap = new Multimap<>();
    matchingCustomMap.addValue("k1", "v1");
    assertEquals(map, matchingCustomMap);
    assertEquals(matchingCustomMap.hashCode(), map.hashCode());
    assertEquals("Multimap{map={k1=[v1]}}", map.toString());
  }

  @Test
  void shouldSortElementsAlphabeticallyInSortedMultimap() {
    Multimap<String, String> sortedMap = Multimap.newSortedMultimap();
    sortedMap.addValue("banana", "v1");
    sortedMap.addValue("apple", "v2");
    sortedMap.addValue("cherry", "v3");
    Iterator<String> keyIterator = sortedMap.keySet().iterator();
    assertEquals("apple", keyIterator.next());
    assertEquals("banana", keyIterator.next());
    assertEquals("cherry", keyIterator.next());
  }

  @Test
  void shouldPreserveInsertionOrderInOrderedMultimap() {
    Multimap<String, String> orderedMap = Multimap.newOrderedMultimap();
    orderedMap.addValue("banana", "v1");
    orderedMap.addValue("apple", "v2");
    orderedMap.addValue("cherry", "v3");
    Iterator<String> keyIterator = orderedMap.keySet().iterator();
    assertEquals("banana", keyIterator.next());
    assertEquals("apple", keyIterator.next());
    assertEquals("cherry", keyIterator.next());
  }

  @Test
  void shouldThrowNullPointerExceptionWhenPassingNullSupplier() {
    assertThrows(NullPointerException.class,
        () -> new Multimap<>(null));
  }

  @Test
  void shouldConstructPreSizedMultimapAndBehaveNormally() {
    Multimap<String, String> preSizedMap = Multimap.newMultimap(16);
    assertNotNull(preSizedMap);
    assertTrue(preSizedMap.isEmpty());

    List<String> values = preSizedMap.addValue("k1", "v1");
    assertEquals(1, preSizedMap.size());
    assertIterableEquals(List.of("v1"), values);
    assertIterableEquals(List.of("v1"), preSizedMap.valueList("k1"));
  }

  @Test
  void shouldPreserveInsertionOrderInPreSizedOrderedMultimap() {
    Multimap<String, String> preSizedOrderedMap = Multimap.newOrderedMultimap(16);
    preSizedOrderedMap.addValue("banana", "v1");
    preSizedOrderedMap.addValue("apple", "v2");
    preSizedOrderedMap.addValue("cherry", "v3");
    Iterator<String> keyIterator = preSizedOrderedMap.keySet().iterator();
    assertEquals("banana", keyIterator.next());
    assertEquals("apple", keyIterator.next());
    assertEquals("cherry", keyIterator.next());
  }

  @Test
  void shouldThrowIllegalArgumentExceptionForNegativeInitialCapacity() {
    assertThrows(IllegalArgumentException.class, () -> Multimap.newMultimap(-1));
    assertThrows(IllegalArgumentException.class, () -> Multimap.newOrderedMultimap(-10));
  }

  @Test
  void shouldThrowNullPointerExceptionWhenPassingNullFunction() {
    assertThrows(NullPointerException.class, () -> new Multimap<>((short) 5, null));
  }
}
