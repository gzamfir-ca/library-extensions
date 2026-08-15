package libext;

import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
  private Bag<String> bag;

  @BeforeEach
  void setUp() {
    bag = Bag.newBag();
  }

  @Test
  void shouldIncrementCountCorrectly() {
    assertEquals(1, bag.add("Apple"));
    assertEquals(2, bag.add("Apple"));
    assertEquals(1, bag.add("Banana"));
    assertEquals(2, bag.count("Apple"));
    assertEquals(1, bag.count("Banana"));
    assertEquals(0, bag.count("Cherry"));
  }

  @Test
  void shouldDecrementCountAndRemoveElementAtZero() {
    bag.add("Apple");
    bag.add("Apple");
    assertEquals(2, bag.count("Apple"));
    assertEquals(2, bag.remove("Apple"));
    assertEquals(1, bag.count("Apple"));
    assertTrue(bag.containsKey("Apple"));
    assertEquals(1, bag.remove("Apple"));
    assertEquals(0, bag.count("Apple"));
    assertFalse(bag.containsKey("Apple"));
  }

  @Test
  void shouldReturnNullWhenDecrementingMissingKey() {
    assertNull(bag.remove("Missing"));
  }

  @Test
  void shouldPreserveInsertionOrderInOrderedBag() {
    Bag<String> orderedBag = Bag.newOrderedBag();
    orderedBag.add("Zebra");
    orderedBag.add("Apple");
    orderedBag.add("Mango");
    Iterator<String> iterator = orderedBag.keySet().iterator();
    assertEquals("Zebra", iterator.next());
    assertEquals("Apple", iterator.next());
    assertEquals("Mango", iterator.next());
  }

  @Test
  void shouldSortElementsAlphabeticallyInSortedBag() {
    Bag<String> sortedBag = Bag.newSortedBag();
    sortedBag.add("Zebra");
    sortedBag.add("Apple");
    sortedBag.add("Mango");
    Iterator<String> iterator = sortedBag.keySet().iterator();
    assertEquals("Apple", iterator.next());
    assertEquals("Mango", iterator.next());
    assertEquals("Zebra", iterator.next());
  }

  @Test
  void shouldIterateOverUniqueElementsAndPreventModification() {
    bag.add("Apple");
    bag.add("Apple");
    bag.add("Banana");
    assertEquals(2, bag.size());
    assertEquals(2, bag.count("Apple"));
    java.util.Iterator<String> iterator = bag.keySet().iterator();
    assertTrue(iterator.hasNext());
    assertEquals("Apple", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("Banana", iterator.next());
    assertFalse(iterator.hasNext());
    java.util.Iterator<String> secureIterator = bag.keySet().iterator();
    secureIterator.next(); // Move to the first element
    assertThrows(UnsupportedOperationException.class, secureIterator::remove,
        "The iterator view should be unmodifiable and reject structural changes via remove().");
  }

  @Test
  void shouldThrowUnsupportedOperationExceptionOnPut() {
    assertThrows(UnsupportedOperationException.class, () -> bag.put("Apple", 5));
  }

  @Test
  void shouldThrowUnsupportedOperationExceptionOnPutAll() {
    Map<String, Integer> externalMap = Map.of("Apple", 1, "Banana", 2);
    assertThrows(UnsupportedOperationException.class, () -> bag.putAll(externalMap));
  }

  private <T> Set<T> maskView(Set<T> set) {
    return set;
  }

  @Test
  void shouldProtectKeySetAgainstExternalModifications() {
    bag.add("Apple");
    Set<String> keysToTest = maskView(bag.keySet());
    assertThrows(UnsupportedOperationException.class, keysToTest::clear);
    assertThrows(UnsupportedOperationException.class, () -> keysToTest.remove("Apple"));
  }

  private <T> Collection<T> maskView(Collection<T> collection) {
    return collection;
  }

  @Test
  void shouldProtectValuesViewAgainstExternalModifications() {
    bag.add("Apple");
    Collection<Integer> valuesToTest = maskView(bag.values());
    assertThrows(UnsupportedOperationException.class, valuesToTest::clear);
  }

  private <K, V> Set<Map.Entry<K, V>> maskEntries(Set<Map.Entry<K, V>> set) {
    return set;
  }

  @Test
  void shouldProtectEntrySetAndIndividualEntriesFromMutation() {
    bag.add("Apple");
    Set<Map.Entry<String, Integer>> entriesToTest = maskEntries(bag.entrySet());
    assertThrows(UnsupportedOperationException.class, entriesToTest::clear);
    Map.Entry<String, Integer> entry = entriesToTest.iterator().next();
    assertThrows(UnsupportedOperationException.class, () -> entry.setValue(999));
  }

  @Test
  void shouldObeyEqualsAndHashCodeContracts() {
    Bag<String> bag1 = Bag.newBag();
    Bag<String> bag2 = Bag.newBag();
    bag1.add("Apple");
    bag2.add("Apple");
    assertEquals(bag1, bag2);
    assertEquals(bag1.hashCode(), bag2.hashCode());
    bag2.add("Apple");
    assertNotEquals(bag1, bag2);
  }
}
