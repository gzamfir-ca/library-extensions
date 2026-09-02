package libext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class MultimapTest {

  private Multimap<String, String> map;

  @BeforeEach
  void setUp() {
    map = Multimap.newMultimap();
  }

  @Test
  void shouldAddValueCorrectlyAndReturnUnmodifiableList() {
    List<String> list1 = map.addValue("k1", "v1");
    List<String> list2 = map.addValue("k1", "v2");
    assertEquals(2, list2.size());
    assertIterableEquals(List.of("v1", "v2"), list2);
    assertThrows(UnsupportedOperationException.class, () -> list1.add("v3"));
  }

  @Test
  void shouldRemoveValueCorrectlyAndReturnUnmodifiableList() {
    map.addValue("k1", "v1");
    map.addValue("k1", "v2");
    List<String> remaining = map.removeValue("k1", "v1");
    assertIterableEquals(List.of("v2"), remaining);
    assertThrows(UnsupportedOperationException.class, () -> remaining.add("v3"));

    List<String> lastRemove = map.removeValue("k1", "v2");
    assertTrue(lastRemove.isEmpty());
    assertFalse(map.containsKey("k1"));
  }

  @Test
  void shouldRecoverValueCorrectlyForMissingKeys() {
    assertNull(map.removeValue("missing", "v1"));

    List<String> emptyList = map.valueList("missing");
    assertTrue(emptyList.isEmpty());
    assertThrows(UnsupportedOperationException.class, () -> emptyList.add("v1"));
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
  @SuppressWarnings("SuspiciousMethodCalls")
  void shouldFindCorrectlyIfValuesExists() {
    map.addValue("k1", "v1");
    List<String> matchingArrayList = new ArrayList<>(List.of("v1"));
    assertTrue(map.containsValue(matchingArrayList));
    assertFalse(map.containsValue((Object) "string-element"));
  }

  @Test
  void shouldPreventAddWholesaleValues() {
    assertThrows(UnsupportedOperationException.class, () -> map.put("k1", List.of("v1")));

    Map<String, List<String>> source = Map.of("k1", List.of("v1"), "k2", List.of("v2"));
    assertThrows(UnsupportedOperationException.class, () -> map.putAll(source));
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
    assertThrows(UnsupportedOperationException.class, () -> valuesView.remove(null));
  }

  @Test
  void shouldProtectEntrySetAndIndividualEntriesFromMutation() {
    map.addValue("k1", "v1");
    Set<Map.Entry<String, List<String>>> entrySet = map.entrySet();
    assertThrows(UnsupportedOperationException.class, entrySet::clear);
  }

  @Test
  void shouldProtectKeySetAgainstExternalModifications() {
    map.addValue("k1", "v1");
    java.util.Collection<?> view = map.keySet();
    assertThrows(UnsupportedOperationException.class, () -> view.remove("k1"));
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
    assertThrows(NullPointerException.class, () -> new Multimap<>(null));
  }

  @Test
  void shouldConstructMapAccuratelyWhenExplicitSupplierProvided() {
    Multimap<String, String> customSupplierMap = new Multimap<>(TreeMap::new);
    customSupplierMap.addValue("z", "val");
    customSupplierMap.addValue("a", "val");
    assertEquals("a", customSupplierMap.keySet().iterator().next());
  }
}
