package libext;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class Lists {

  private Lists() {
    throw new AssertionError("no instances");
  }

  @SafeVarargs
  private static <T> void addAll(Collection<T> col, T... elements) {
    Objects.requireNonNull(col, "no valid collection provided");
    col.addAll(Arrays.asList(elements));
  }

  private static <T> void addAll(Collection<T> col, int size, Supplier<T> supplier) {
    Objects.requireNonNull(col, "no valid collection provided");
    for (int i = 0; i < size; i++) {
      col.add(supplier.get());
    }
  }

  private static int validateSize(int size) {
    if (size < 0) {
      throw new IllegalArgumentException("size must be >= 0");
    }
    return size;
  }

  @SafeVarargs
  public static <T> ArrayList<T> newArrayList(T... elements) {
    Objects.requireNonNull(elements, "no valid elements provided");
    ArrayList<T> list = new ArrayList<>(validateSize(elements.length));
    addAll(list, elements);
    return list;
  }

  public static <T> ArrayList<T> newArrayList(int size, Supplier<T> supplier) {
    Objects.requireNonNull(supplier, "no valid supplier provided");
    ArrayList<T> list = new ArrayList<>(validateSize(size));
    addAll(list, size, supplier);
    return list;
  }

  public static ArrayList<String> newArrayList(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    ArrayList<String> list = new ArrayList<>(128);
    Readers.addAll(list, reader);
    return list;
  }

  @SafeVarargs
  public static <T> LinkedList<T> newLinkedList(T... elements) {
    Objects.requireNonNull(elements, "no valid elements provided");
    LinkedList<T> list = new LinkedList<>();
    addAll(list, elements);
    return list;
  }

  public static <T> LinkedList<T> newLinkedList(int size, Supplier<T> supplier) {
    Objects.requireNonNull(supplier, "no valid supplier provided");
    LinkedList<T> list = new LinkedList<>();
    validateSize(size);
    addAll(list, size, supplier);
    return list;
  }

  public static LinkedList<String> newLinkedList(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    LinkedList<String> list = new LinkedList<>();
    Readers.addAll(list, reader);
    return list;
  }

  @SafeVarargs
  public static <T> LinkedHashSet<T> newLinkedHashSet(T... elements) {
    Objects.requireNonNull(elements, "no valid elements provided");
    int capacity = Math.multiplyExact(validateSize(elements.length), 134) / 100;
    LinkedHashSet<T> list = new LinkedHashSet<>(capacity);
    addAll(list, elements);
    return list;
  }

  public static <T> LinkedHashSet<T> newLinkedHashSet(int size, Supplier<T> supplier) {
    Objects.requireNonNull(supplier, "no valid supplier provided");
    int capacity = Math.multiplyExact(validateSize(size), 134) / 100;
    LinkedHashSet<T> list = new LinkedHashSet<>(capacity);
    addAll(list, size, supplier);
    return list;
  }

  public static LinkedHashSet<String> newLinkedHashSet(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    LinkedHashSet<String> list = new LinkedHashSet<>(128);
    Readers.addAll(list, reader);
    return list;
  }

  public static long[] toLongArray(List<? extends Number> list) {
    Objects.requireNonNull(list, "no valid list provided");
    int size = list.size();
    if (size > 0) {
      long[] array = new long[size];
      for (int i = 0; i < size; i++) {
        array[i] = list.get(i).longValue();
      }
      return array;
    }
    return new long[0];
  }

  public static double[] toDoubleArray(List<? extends Number> list) {
    Objects.requireNonNull(list, "no valid list provided");
    int size = list.size();
    if (size > 0) {
      double[] array = new double[size];
      for (int i = 0; i < size; i++) {
        array[i] = list.get(i).doubleValue();
      }
      return array;
    }
    return new double[0];
  }
}
