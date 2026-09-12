package libext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Multiset<K> {

  private final Map<K, Integer> map;

  Multiset() {
    this(HashMap::new);
  }

  Multiset(Supplier<Map<K, Integer>> mapSupplier) {
    this.map = Objects.requireNonNull(mapSupplier).get();
  }

  Multiset(int expectedSize) {
    this(expectedSize, HashMap::newHashMap);
  }

  Multiset(int expectedSize, Function<Integer, Map<K, Integer>> mapFunction) {
    if (expectedSize < 0) {
      throw new IllegalArgumentException("expected size must be >= 0");
    }
    this.map = Objects.requireNonNull(mapFunction).apply(expectedSize);
  }

  public static <K> Multiset<K> newMultiset() {
    return new Multiset<>();
  }

  public static <K extends Comparable<? super K>> Multiset<K> newSortedMultiset() {
    return new Multiset<>(TreeMap::new);
  }

  public static <K> Multiset<K> newOrderedMultiset() {
    return new Multiset<>(LinkedHashMap::new);
  }

  public static <K> Multiset<K> newMultiset(int expectedSize) {
    return new Multiset<>(expectedSize);
  }

  public static <K> Multiset<K> newOrderedMultiset(int expectedSize) {
    return new Multiset<>(expectedSize, LinkedHashMap::newLinkedHashMap);
  }

  public Integer addKey(K key) {
    return map.merge(key, 1, Math::addExact);
  }

  public Integer removeKey(K key) {
    Integer currentCount = map.get(key);
    if (currentCount == null) {
      return null;
    }
    if (currentCount <= 1) {
      map.remove(key);
    } else {
      map.put(key, currentCount - 1);
    }
    return currentCount;
  }

  @SuppressWarnings("SuspiciousMethodCalls")
  public Integer keyCount(Object key) {
    return map.getOrDefault(key, 0);
  }

  @SuppressWarnings("SuspiciousMethodCalls")
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  public int size() {
    return map.size();
  }

  public boolean isEmpty() {
    return map.isEmpty();
  }

  public Set<K> keySet() {
    return Collections.unmodifiableSet(map.keySet());
  }

  public Collection<Integer> values() {
    return Collections.unmodifiableCollection(map.values());
  }

  public Set<Map.Entry<K, Integer>> entrySet() {
    return Collections.unmodifiableMap(map).entrySet();
  }

  public Collection<K> flattenedKeys() {
    int size = 0;
    for (Integer count : map.values()) {
      size += count;
    }
    Collection<K> keys = new ArrayList<>(size);
    for (Map.Entry<K, Integer> entry : map.entrySet()) {
      for (int i = 0; i < entry.getValue(); i++) {
        keys.add(entry.getKey());
      }
    }
    return Collections.unmodifiableCollection(keys);
  }

  public void clear() {
    map.clear();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Multiset<?> countMap = (Multiset<?>) o;
    return Objects.equals(map, countMap.map);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(map);
  }

  @Override
  public String toString() {
    return "Multiset{" +
        "map=" + map +
        '}';
  }
}
