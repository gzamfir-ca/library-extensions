package libext;

import java.io.BufferedReader;
import java.util.AbstractMap;
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
      Objects.requireNonNull(entry, "invalid entry provided");
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

  public static <K, V> Entry<K, V> entry(K key, V value) {
    return new AbstractMap.SimpleImmutableEntry<>(key, value);
  }

  @SafeVarargs
  public static <K, V> Map<K, V> newHashMap(Entry<K, V>... entries) {
    Objects.requireNonNull(entries, "no valid entries provided");
    Map<K, V> map = HashMap.newHashMap(entries.length);
    addAll(map, entries);
    return map;
  }

  @SafeVarargs
  public static <K, V> Map<K, V> newHashMap(Function<K, V> function, K... keys) {
    Objects.requireNonNull(function, "no valid function provided");
    Objects.requireNonNull(keys, "no valid keys provided");
    Map<K, V> map = HashMap.newHashMap(keys.length);
    addAll(map, function, keys);
    return map;
  }

  public static Map<String, String> newHashMap(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    Map<String, String> map = HashMap.newHashMap(64);
    Readers.addAll(map, reader);
    return map;
  }

  @SafeVarargs
  public static <K, V> Map<K, V> newLinkedHashMap(Entry<K, V>... entries) {
    Objects.requireNonNull(entries, "no valid entries provided");
    Map<K, V> map = LinkedHashMap.newLinkedHashMap(entries.length);
    addAll(map, entries);
    return map;
  }

  @SafeVarargs
  public static <K, V> Map<K, V> newLinkedHashMap(Function<K, V> function, K... keys) {
    Objects.requireNonNull(function, "no valid function provided");
    Objects.requireNonNull(keys, "no valid keys provided");
    Map<K, V> map = LinkedHashMap.newLinkedHashMap(keys.length);
    addAll(map, function, keys);
    return map;
  }

  public static Map<String, String> newLinkedHashMap(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    Map<String, String> map = LinkedHashMap.newLinkedHashMap(64);
    Readers.addAll(map, reader);
    return map;
  }
}
