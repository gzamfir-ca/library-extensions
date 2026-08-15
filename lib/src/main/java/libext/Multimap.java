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
import java.util.function.Supplier;

public class Multimap<K, V> implements Map<K, List<V>> {

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

  public static <K, V> Multimap<K, V> newMultimap() {
    return new Multimap<>();
  }

  public static <K extends Comparable<? super K>, V> Multimap<K, V> newSortedMultimap() {
    return new Multimap<>(TreeMap::new);
  }

  public static <K, V> Multimap<K, V> newOrderedMultimap() {
    return new Multimap<>(LinkedHashMap::new);
  }

  public List<V> putValue(K key, V element) {
    List<V> list = map.computeIfAbsent(key, k -> new ArrayList<>());
    list.add(element);
    return unmodifiableList(list);
  }

  public List<V> removeValue(K key, V element) {
    List<V> list = map.get(key);
    if (list == null) {
      return null;
    }
    list.remove(element);
    if (list.isEmpty()) {
      map.remove(key);
      return Collections.emptyList();
    }
    return unmodifiableList(list);
  }

  public Collection<V> valueList() {
    Collection<V> valueList = new ArrayList<>();
    for (List<V> list : map.values()) {
      valueList.addAll(list);
    }
    return valueList;
  }

  public List<V> valueList(K key) {
    return unmodifiableList(map.getOrDefault(key, new ArrayList<>()));
  }

  public int valueCount() {
    int count = 0;
    for (List<V> list : map.values()) {
      count += list.size();
    }
    return count;
  }

  public int valueCount(K key) {
    List<V> list = map.get(key);
    return list == null ? 0 : list.size();
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
    if (value instanceof List) {
      return map.containsValue(value);
    }
    return false;
  }

  @Override
  public List<V> get(Object key) {
    return unmodifiableList(map.get(key));
  }

  @Override
  public List<V> put(K key, List<V> value) {
    if (value == null) {
      throw new NullPointerException("value cannot be null");
    }
    List<V> previous = map.put(key, new ArrayList<>(value));
    return unmodifiableList(previous);
  }

  @Override
  public List<V> remove(Object key) {
    return unmodifiableList(map.remove(key));
  }

  @Override
  public void putAll(Map<? extends K, ? extends List<V>> m) {
    for (Map.Entry<? extends K, ? extends List<V>> entry : m.entrySet()) {
      this.put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<K> keySet() {
    return Collections.unmodifiableMap(map).keySet();
  }

  @Override
  public Collection<List<V>> values() {
    return Collections.unmodifiableMap(map).values();
  }

  @Override
  public Set<Entry<K, List<V>>> entrySet() {
    return Collections.unmodifiableMap(map).entrySet();
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
