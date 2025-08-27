package libext;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

public class Lists {

  private Lists() {
  }

  @SafeVarargs
  public static <T> ArrayList<T> newArrayList(T... elements) {
    Objects.requireNonNull(elements, "no valid elements provided");
    ArrayList<T> list = new ArrayList<>();
    boolean success = false;
    for (T element : elements) {
      success |= list.add(element);
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static ArrayList<String> newArrayList(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    ArrayList<String> list = new ArrayList<>();
    boolean success = Readers.addAll(list, reader);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static ArrayList<String> newArrayList(String line, String regex) {
    Objects.requireNonNull(line, "no valid line provided");
    Objects.requireNonNull(regex, "no valid regex provided");
    ArrayList<String> list = new ArrayList<>();
    boolean success = false;
    for (String token : line.split(regex)) {
      success |= list.add(token);
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T extends Copyable<T>> ArrayList<T> newArrayList(Iterable<T> iterable) {
    Objects.requireNonNull(iterable, "no valid iterable provided");
    ArrayList<T> list = new ArrayList<>();
    boolean success = false;
    for (T element : iterable) {
      success |= list.add(element.copy());
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T> ArrayList<T> newArrayList(int size, Supplier<T> supplier) {
    Objects.requireNonNull(supplier, "no valid supplier provided");
    ArrayList<T> list = new ArrayList<>();
    boolean success = false;
    for (int i = 0; i < size; i++) {
      success |= list.add(supplier.get());
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  @SafeVarargs
  public static <T> LinkedList<T> newLinkedList(T... elements) {
    Objects.requireNonNull(elements, "no valid elements provided");
    LinkedList<T> list = new LinkedList<>();
    boolean success = false;
    for (T element : elements) {
      success |= list.add(element);
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static LinkedList<String> newLinkedList(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    LinkedList<String> list = new LinkedList<>();
    boolean success = Readers.addAll(list, reader);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static LinkedList<String> newLinkedList(String line, String regex) {
    Objects.requireNonNull(line, "no valid line provided");
    Objects.requireNonNull(regex, "no valid regex provided");
    LinkedList<String> list = new LinkedList<>();
    boolean success = false;
    for (String token : line.split(regex)) {
      success |= list.add(token);
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T extends Copyable<T>> LinkedList<T> newLinkedList(Iterable<T> iterable) {
    Objects.requireNonNull(iterable, "no valid iterable provided");
    LinkedList<T> list = new LinkedList<>();
    boolean success = false;
    for (T element : iterable) {
      success |= list.add(element.copy());
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T> LinkedList<T> newLinkedList(int size, Supplier<T> supplier) {
    Objects.requireNonNull(supplier, "no valid supplier provided");
    LinkedList<T> list = new LinkedList<>();
    boolean success = false;
    for (int i = 0; i < size; i++) {
      success |= list.add(supplier.get());
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static LinkedHashSet<String> newLinkedHashSet(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    LinkedHashSet<String> list = new LinkedHashSet<>();
    boolean success = Readers.addAll(list, reader);
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  @SafeVarargs
  public static <T> LinkedHashSet<T> newLinkedHashSet(T... elements) {
    Objects.requireNonNull(elements, "no valid elements provided");
    LinkedHashSet<T> list = new LinkedHashSet<>();
    boolean success = false;
    for (T element : elements) {
      success |= list.add(element);
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static LinkedHashSet<String> newLinkedHashSet(String line, String regex) {
    Objects.requireNonNull(line, "no valid line provided");
    Objects.requireNonNull(regex, "no valid regex provided");
    LinkedHashSet<String> list = new LinkedHashSet<>();
    boolean success = false;
    for (String token : line.split(regex)) {
      success |= list.add(token);
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T extends Copyable<T>> LinkedHashSet<T> newLinkedHashSet(Iterable<T> iterable) {
    Objects.requireNonNull(iterable, "no valid iterable provided");
    LinkedHashSet<T> list = new LinkedHashSet<>();
    boolean success = false;
    for (T element : iterable) {
      success |= list.add(element.copy());
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }

  public static <T> LinkedHashSet<T> newLinkedHashSet(int size, Supplier<T> supplier) {
    Objects.requireNonNull(supplier, "no valid supplier provided");
    LinkedHashSet<T> list = new LinkedHashSet<>();
    boolean success = false;
    for (int i = 0; i < size; i++) {
      success |= list.add(supplier.get());
    }
    if (!success) {
      throw new RuntimeException("failed to add some element");
    }
    return list;
  }
}
