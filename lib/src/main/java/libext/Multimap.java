package libext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Multimap<K, V> {

  private final Map<K, List<V>> map;

  private List<V> unmodifiableList(List<V> list) {
    return list == null ? null : Collections.unmodifiableList(list);
  }

  Multimap() {
    this(HashMap::new);
  }

  Multimap(Supplier<Map<K, List<V>>> mapSupplier) {
    this.map = Objects.requireNonNull(mapSupplier).get();
  }

  Multimap(int expectedSize) {
    this(expectedSize, HashMap::newHashMap);
  }

  Multimap(int expectedSize, Function<Integer, Map<K, List<V>>> mapFunction) {
    if (expectedSize < 0) {
      throw new IllegalArgumentException("expected size must be >= 0");
    }
    this.map = Objects.requireNonNull(mapFunction).apply(expectedSize);
  }

  public static <K, V> Multimap<K, V> newMultimap() {
    return new Multimap<>();
  }

  public static <K extends Comparable<? super K>, V> Multimap<K, V> newSortedMultimap() {
    return new Multimap<>(TreeMap::new);
  }

  public static <K, V> Multimap<K, V> newOrderedMultimap() {
    return new Multimap<>(LinkedHashMap::new);
  }

  public static <K, V> Multimap<K, V> newMultimap(int expectedSize) {
    return new Multimap<>(expectedSize);
  }

  public static <K, V> Multimap<K, V> newOrderedMultimap(int expectedSize) {
    return new Multimap<>(expectedSize, LinkedHashMap::newLinkedHashMap);
  }

  public List<V> addValue(K key, V value) {
    List<V> list = map.computeIfAbsent(key, k -> new ArrayList<>());
    list.add(value);
    return unmodifiableList(list);
  }

  public List<V> removeValue(K key, V value) {
    List<V> list = map.get(key);
    if (list == null) {
      return null;
    }
    list.remove(value);
    if (list.isEmpty()) {
      map.remove(key);
      return Collections.emptyList();
    }
    return unmodifiableList(list);
  }

  @SuppressWarnings("SuspiciousMethodCalls")
  public List<V> valueList(Object key) {
    return unmodifiableList(map.getOrDefault(key, Collections.emptyList()));
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

  public Collection<List<V>> values() {
    return Collections.unmodifiableCollection(map.values());
  }

  public Set<Map.Entry<K, List<V>>> entrySet() {
    return Collections.unmodifiableMap(map).entrySet();
  }

  public Collection<V> flattenedValues() {
    int size = 0;
    for (List<V> list : map.values()) {
      size += list.size();
    }
    Collection<V> values = new ArrayList<>(size);
    for (List<V> list : map.values()) {
      values.addAll(list);
    }
    return Collections.unmodifiableCollection(values);
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
    Multimap<?, ?> that = (Multimap<?, ?>) o;
    return Objects.equals(map, that.map);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(map);
  }

  @Override
  public String toString() {
    return "Multimap{" +
        "map=" + map +
        '}';
  }
}
