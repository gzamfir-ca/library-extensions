package libext;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class Lists {

  private Lists() {
  }

  @SafeVarargs
  private static <T> boolean addAll(Collection<T> col, T... elements) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(elements, "no valid elements provided");
    boolean success = false;
    for (T element : elements) {
      success |= col.add(element);
    }
    return success;
  }

  private static boolean addAll(Collection<String> col, String line, String regex) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(line, "no valid line provided");
    Objects.requireNonNull(regex, "no valid regex provided");
    boolean success = false;
    for (String token : line.split(regex)) {
      success |= col.add(token);
    }
    return success;
  }

  private static <T extends Copyable<T>> boolean addAll(Collection<T> col, Iterable<T> iterable) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(iterable, "no valid iterable provided");
    boolean success = false;
    for (T element : iterable) {
      success |= col.add(element.copy());
    }
    return success;
  }

  private static <T> boolean addAll(Collection<T> col, int size, Supplier<T> supplier) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(supplier, "no valid supplier provided");
    boolean success = false;
    for (int i = 0; i < size; i++) {
      success |= col.add(supplier.get());
    }
    return success;
  }

  private static <K, V> boolean addAll(Collection<Map.Entry<K, V>> col, Map<K, V> map) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(map, "no valid map provided");
    boolean success = false;
    for (Map.Entry<K, V> entry : map.entrySet()) {
      success |= col.add(Map.Entry.copyOf(entry));
    }
    return success;
  }

  @SafeVarargs
  public static <T> ArrayList<T> newArrayList(T... elements) {
    ArrayList<T> list = new ArrayList<>();
    boolean success = addAll(list, elements);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static ArrayList<String> newArrayList(BufferedReader reader) {
    ArrayList<String> list = new ArrayList<>();
    boolean success = Readers.addAll(list, reader);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static ArrayList<String> newArrayList(String line, String regex) {
    ArrayList<String> list = new ArrayList<>();
    boolean success = addAll(list, line, regex);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T extends Copyable<T>> ArrayList<T> newArrayList(Iterable<T> iterable) {
    ArrayList<T> list = new ArrayList<>();
    boolean success = addAll(list, iterable);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T> ArrayList<T> newArrayList(int size, Supplier<T> supplier) {
    ArrayList<T> list = new ArrayList<>();
    boolean success = addAll(list, size, supplier);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <K, V> ArrayList<Map.Entry<K, V>> newArrayList(Map<K, V> map) {
    ArrayList<Map.Entry<K, V>> list = new ArrayList<>();
    boolean success = addAll(list, map);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  @SafeVarargs
  public static <T> LinkedList<T> newLinkedList(T... elements) {
    LinkedList<T> list = new LinkedList<>();
    boolean success = addAll(list, elements);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static LinkedList<String> newLinkedList(BufferedReader reader) {
    LinkedList<String> list = new LinkedList<>();
    boolean success = Readers.addAll(list, reader);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static LinkedList<String> newLinkedList(String line, String regex) {
    LinkedList<String> list = new LinkedList<>();
    boolean success = addAll(list, line, regex);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T extends Copyable<T>> LinkedList<T> newLinkedList(Iterable<T> iterable) {
    LinkedList<T> list = new LinkedList<>();
    boolean success = addAll(list, iterable);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T> LinkedList<T> newLinkedList(int size, Supplier<T> supplier) {
    LinkedList<T> list = new LinkedList<>();
    boolean success = addAll(list, size, supplier);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <K, V> LinkedList<Map.Entry<K, V>> newLinkedList(Map<K, V> map) {
    LinkedList<Map.Entry<K, V>> list = new LinkedList<>();
    boolean success = addAll(list, map);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  @SafeVarargs
  public static <T> LinkedHashSet<T> newLinkedHashSet(T... elements) {
    LinkedHashSet<T> list = new LinkedHashSet<>();
    boolean success = addAll(list, elements);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static LinkedHashSet<String> newLinkedHashSet(BufferedReader reader) {
    LinkedHashSet<String> list = new LinkedHashSet<>();
    boolean success = Readers.addAll(list, reader);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static LinkedHashSet<String> newLinkedHashSet(String line, String regex) {
    LinkedHashSet<String> list = new LinkedHashSet<>();
    boolean success = addAll(list, line, regex);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T extends Copyable<T>> LinkedHashSet<T> newLinkedHashSet(Iterable<T> iterable) {
    LinkedHashSet<T> list = new LinkedHashSet<>();
    boolean success = addAll(list, iterable);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T> LinkedHashSet<T> newLinkedHashSet(int size, Supplier<T> supplier) {
    LinkedHashSet<T> list = new LinkedHashSet<>();
    boolean success = addAll(list, size, supplier);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <K, V> LinkedHashSet<Map.Entry<K, V>> newLinkedHashSet(Map<K, V> map) {
    LinkedHashSet<Map.Entry<K, V>> list = new LinkedHashSet<>();
    boolean success = addAll(list, map);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }
}
