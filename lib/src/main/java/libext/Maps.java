package libext;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

public final class Maps {

  private Maps() {
    throw new AssertionError("no instances");
  }

  @SafeVarargs
  private static <K, V> void addAll(Map<K, V> map, Entry<K, V>... entries) {
    Objects.requireNonNull(map, "no valid map provided");
    for (Entry<K, V> entry : entries) {
      map.put(entry.getKey(), entry.getValue());
    }
  }

  @SafeVarargs
  private static <K, V> void addAll(Map<K, V> map, Function<K, V> function, K... keys) {
    Objects.requireNonNull(map, "no valid map provided");
    for (K key : keys) {
      map.put(key, function.apply(key));
    }
  }

  private static int validateSize(int size) {
    if (size < 0) {
      throw new IllegalArgumentException("size must be >= 0");
    }
    return size;
  }

  public static <K, V> Entry<K, V> entry(K key, V value) {
    return Map.entry(key, value);
  }

  @SafeVarargs
  public static <K, V> Map<K, V> newHashMap(Entry<K, V>... entries) {
    Objects.requireNonNull(entries, "no valid entries provided");
    HashMap<K, V> map = HashMap.newHashMap(validateSize(entries.length));
    addAll(map, entries);
    return map;
  }

  @SafeVarargs
  public static <K, V> Map<K, V> newLinkedHashMap(Entry<K, V>... entries) {
    Objects.requireNonNull(entries, "no valid entries provided");
    LinkedHashMap<K, V> map = LinkedHashMap.newLinkedHashMap(validateSize(entries.length));
    addAll(map, entries);
    return map;
  }

  @SafeVarargs
  public static <K, V> Map<K, V> newLinkedHashMap(Function<K, V> function, K... keys) {
    Objects.requireNonNull(function, "no valid function provided");
    Objects.requireNonNull(keys, "no valid keys provided");
    LinkedHashMap<K, V> map = LinkedHashMap.newLinkedHashMap(validateSize(keys.length));
    addAll(map, function, keys);
    return map;
  }
}
