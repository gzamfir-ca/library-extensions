package libext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CountMap<K> implements Map<K, Integer> {

  private final Map<K, Integer> map;

  public CountMap() {
    map = new HashMap<>();
  }

  public CountMap(Map<K, Integer> countMap) {
    this.map = countMap;
  }

  public Map<K, Integer> getCountMap() {
    return map;
  }

  public Integer incrementCount(K key) {
    return map.put(key, map.getOrDefault(key, 0) + 1);
  }

  public Integer decrementCount(K key) {
    int count = map.getOrDefault(key, 0) - 1;
    if (count >= 1) {
      return map.put(key, count);
    }
    if (count == 0) {
      map.remove(key);
    }
    return count;
  }

  public Integer keyCount(K key) {
    return map.getOrDefault(key, 0);
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
  public Integer put(K key, Integer value) {
    return map.put(key, value);
  }

  @Override
  public Integer remove(Object key) {
    return map.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends Integer> m) {
    map.putAll(m);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<K> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<Integer> values() {
    return map.values();
  }

  @Override
  public Set<Entry<K, Integer>> entrySet() {
    return map.entrySet();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CountMap<?> countMap = (CountMap<?>) o;
    return Objects.equals(map, countMap.map);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(map);
  }

  @Override
  public String toString() {
    return "CountMap{" +
        "map=" + map +
        '}';
  }
}
