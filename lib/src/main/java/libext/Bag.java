package libext;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;

public class Bag<K> implements Map<K, Integer>, Iterable<K> {

  private final Map<K, Integer> map;

  Bag() {
    this(HashMap::new);
  }

  Bag(Supplier<Map<K, Integer>> mapSupplier) {
    this.map = Objects.requireNonNull(mapSupplier).get();
  }

  public static <K> Bag<K> newBag() {
    return new Bag<>();
  }

  public static <K extends Comparable<? super K>> Bag<K> newSortedBag() {
    return new Bag<>(TreeMap::new);
  }

  public static <K> Bag<K> newOrderedBag() {
    return new Bag<>(LinkedHashMap::new);
  }

  public Integer add(K key) {
    return map.merge(key, 1, Integer::sum);
  }

  @SuppressWarnings("SuspiciousMethodCalls")
  public Integer count(Object key) {
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
  public Iterator<K> iterator() {
    // KeySet provides a clean, unique stream of elements in the bag
    return keySet().iterator();
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
    return map.containsValue(value);
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
    throw new UnsupportedOperationException("Bag does not support put");
  }

  @Override
  public void putAll(Map<? extends K, ? extends Integer> m) {
    throw new UnsupportedOperationException("Bag does not support putAll");
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
    Bag<?> countMap = (Bag<?>) o;
    return Objects.equals(map, countMap.map);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(map);
  }

  @Override
  public String toString() {
    return "Bag{" +
        "map=" + map +
        '}';
  }
}
