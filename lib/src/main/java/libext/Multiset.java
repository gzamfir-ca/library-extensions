package libext;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;

public class Multiset<K> implements Map<K, Integer> {

  private final Map<K, Integer> map;

  Multiset() {
    this(HashMap::new);
  }

  Multiset(Supplier<Map<K, Integer>> mapSupplier) {
    this.map = Objects.requireNonNull(mapSupplier).get();
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

  public Integer addKey(K key) {
    return map.merge(key, 1, Integer::sum);
  }

  public Integer removeKey(K key) {
    return this.remove(key);
  }

  @SuppressWarnings("SuspiciousMethodCalls")
  public Integer keyCount(Object key) {
    return map.getOrDefault(key, 0);
  }

  @Override
  @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
  public Integer remove(Object key) {
    Integer currentCount = map.get(key);
    if (currentCount == null) {
      return null;
    }
    map.compute((K) key, (k, v) -> (v <= 1) ? null : v - 1);
    return currentCount;
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    if (value instanceof Number) {
      return map.containsValue(((Number) value).intValue());
    }
    return false;
  }

  @Override
  public Integer get(Object key) {
    return map.get(key);
  }

  @Override
  public Set<K> keySet() {
    return Collections.unmodifiableMap(map).keySet();
  }

  @Override
  public Collection<Integer> values() {
    return Collections.unmodifiableMap(map).values();
  }

  @Override
  public Set<Entry<K, Integer>> entrySet() {
    return Collections.unmodifiableMap(map).entrySet();
  }

  @Override
  public Integer put(K key, Integer value) {
    throw new UnsupportedOperationException("Multiset does not support put");
  }

  @Override
  public void putAll(Map<? extends K, ? extends Integer> m) {
    throw new UnsupportedOperationException("Multiset does not support putAll");
  }

  @Override
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
