package libext;

import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MultisetTest {

  private Multiset<String> multiset;

  @BeforeEach
  void setUp() {
    multiset = Multiset.newMultiset();
  }

  @Test
  void shouldIncrementCountCorrectly() {
    assertEquals(1, multiset.addKey("Apple"));
    assertEquals(2, multiset.addKey("Apple"));
    assertEquals(1, multiset.addKey("Banana"));
    assertEquals(2, multiset.keyCount("Apple"));
    assertEquals(1, multiset.keyCount("Banana"));
    assertEquals(0, multiset.keyCount("Cherry"));
  }

  @Test
  void shouldDecrementCountAndRemoveElementAtZero() {
    multiset.addKey("Apple");
    multiset.addKey("Apple");
    assertEquals(2, multiset.keyCount("Apple"));
    assertEquals(2, multiset.removeKey("Apple"));
    assertEquals(1, multiset.keyCount("Apple"));
    assertTrue(multiset.containsKey("Apple"));
    assertEquals(1, multiset.removeKey("Apple"));
    assertEquals(0, multiset.keyCount("Apple"));
    assertFalse(multiset.containsKey("Apple"));
  }

  @Test
  void shouldReturnNullWhenDecrementingMissingKey() {
    assertNull(multiset.removeKey("Missing"));
  }

  @Test
  void shouldPreserveInsertionOrderInOrderedBag() {
    Multiset<String> orderedMultiset = Multiset.newOrderedMultiset();
    orderedMultiset.addKey("Zebra");
    orderedMultiset.addKey("Apple");
    orderedMultiset.addKey("Mango");
    Iterator<String> iterator = orderedMultiset.keySet().iterator();
    assertEquals("Zebra", iterator.next());
    assertEquals("Apple", iterator.next());
    assertEquals("Mango", iterator.next());
  }

  @Test
  void shouldSortElementsAlphabeticallyInSortedBag() {
    Multiset<String> sortedMultiset = Multiset.newSortedMultiset();
    sortedMultiset.addKey("Zebra");
    sortedMultiset.addKey("Apple");
    sortedMultiset.addKey("Mango");
    Iterator<String> iterator = sortedMultiset.keySet().iterator();
    assertEquals("Apple", iterator.next());
    assertEquals("Mango", iterator.next());
    assertEquals("Zebra", iterator.next());
  }

  @Test
  void shouldIterateOverUniqueElementsAndPreventModification() {
    multiset.addKey("Apple");
    multiset.addKey("Apple");
    multiset.addKey("Banana");
    assertEquals(2, multiset.size());
    assertEquals(2, multiset.keyCount("Apple"));

    java.util.Iterator<String> iterator = multiset.keySet().iterator();
    assertTrue(iterator.hasNext());
    assertEquals("Apple", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("Banana", iterator.next());
    assertFalse(iterator.hasNext());

    java.util.Iterator<String> secureIterator = multiset.keySet().iterator();
    secureIterator.next();
    assertThrows(UnsupportedOperationException.class, secureIterator::remove,
        "The iterator view should be unmodifiable and reject structural changes via remove().");
  }

  @Test
  void shouldThrowUnsupportedOperationExceptionOnPut() {
    assertThrows(UnsupportedOperationException.class, () -> multiset.put("Apple", 5));
  }

  @Test
  void shouldThrowUnsupportedOperationExceptionOnPutAll() {
    Map<String, Integer> externalMap = Map.of("Apple", 1, "Banana", 2);
    assertThrows(UnsupportedOperationException.class, () -> multiset.putAll(externalMap));
  }

  private <T> Set<T> maskView(Set<T> set) {
    return set;
  }

  @Test
  void shouldProtectKeySetAgainstExternalModifications() {
    multiset.addKey("Apple");
    Set<String> keysToTest = maskView(multiset.keySet());
    assertThrows(UnsupportedOperationException.class, keysToTest::clear);
    assertThrows(UnsupportedOperationException.class, () -> keysToTest.remove("Apple"));
  }

  private <T> Collection<T> maskView(Collection<T> collection) {
    return collection;
  }

  @Test
  void shouldProtectValuesViewAgainstExternalModifications() {
    multiset.addKey("Apple");
    Collection<Integer> valuesToTest = maskView(multiset.values());
    assertThrows(UnsupportedOperationException.class, valuesToTest::clear);
  }

  private <K, V> Set<Map.Entry<K, V>> maskEntries(Set<Map.Entry<K, V>> set) {
    return set;
  }

  @Test
  void shouldProtectEntrySetAndIndividualEntriesFromMutation() {
    multiset.addKey("Apple");
    Set<Map.Entry<String, Integer>> entriesToTest = maskEntries(multiset.entrySet());
    assertThrows(UnsupportedOperationException.class, entriesToTest::clear);

    Map.Entry<String, Integer> entry = entriesToTest.iterator().next();
    assertThrows(UnsupportedOperationException.class, () -> entry.setValue(999));
  }

  @Test
  void shouldObeyEqualsAndHashCodeContracts() {
    Multiset<String> multiset1 = Multiset.newMultiset();
    Multiset<String> multiset2 = Multiset.newMultiset();
    multiset1.addKey("Apple");
    multiset2.addKey("Apple");
    assertEquals(multiset1, multiset2);
    assertEquals(multiset1.hashCode(), multiset2.hashCode());

    multiset2.addKey("Apple");
    assertNotEquals(multiset1, multiset2);
  }
}
