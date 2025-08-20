package libext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ValueListMap<K, V> implements Map<K, List<V>> {

  private final Map<K, List<V>> map;

  public ValueListMap() {
    map = new HashMap<>();
  }

  public ValueListMap(Map<K, List<V>> listMap) {
    map = listMap;
  }

  public Map<K, List<V>> getListMap() {
    return map;
  }

  public List<V> putValue(K key, V element) {
    map.computeIfAbsent(key, k -> new ArrayList<>()).add(element);
    return map.get(key);
  }

  public List<V> removeValue(K key, V element) {
    map.computeIfAbsent(key, k -> new ArrayList<>()).remove(element);
    return map.get(key);
  }

  public Collection<V> valueList() {
    Collection<V> valueList = new ArrayList<>();
    for (List<V> list : map.values()) {
      valueList.addAll(list);
    }
    return valueList;
  }

  public List<V> valueList(K key) {
    return map.getOrDefault(key, new ArrayList<>());
  }

  public int valueCount() {
    int count = 0;
    for (List<V> list : map.values()) {
      count += list.size();
    }
    return count;
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
    for (List<V> list : map.values()) {
      for (V v : list) {
        if (v.equals(value)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public List<V> get(Object key) {
    return map.get(key);
  }

  @Override
  public List<V> put(K key, List<V> value) {
    map.computeIfAbsent(key, k -> new ArrayList<>()).addAll(value);
    return map.get(key);
  }

  @Override
  public List<V> remove(Object key) {
    return map.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends List<V>> m) {
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
  public Collection<List<V>> values() {
    return map.values();
  }

  @Override
  public Set<Entry<K, List<V>>> entrySet() {
    return map.entrySet();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValueListMap<?, ?> that = (ValueListMap<?, ?>) o;
    return Objects.equals(map, that.map);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(map);
  }

  @Override
  public String toString() {
    return "ValueListMap{" +
        "map=" + map +
        '}';
  }
}
