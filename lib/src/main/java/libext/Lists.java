package libext;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;
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

  private static <T> void addAll(Collection<T> col, int size, IntFunction<T> function) {
    Objects.requireNonNull(col, "no valid collection provided");
    for (int i = 0; i < size; i++) {
      col.add(function.apply(i));
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

  public static <T> ArrayList<T> newArrayList(int size, IntFunction<T> function) {
    Objects.requireNonNull(function, "no valid function provided");
    ArrayList<T> list = new ArrayList<>(validateSize(size));
    addAll(list, size, function);
    return list;
  }

  public static ArrayList<String> newArrayList(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    ArrayList<String> list = new ArrayList<>(64);
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

  public static <T> LinkedList<T> newLinkedList(int size, IntFunction<T> function) {
    Objects.requireNonNull(function, "no valid function provided");
    LinkedList<T> list = new LinkedList<>();
    validateSize(size);
    addAll(list, size, function);
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
    LinkedHashSet<T> list = LinkedHashSet.newLinkedHashSet(validateSize(elements.length));
    addAll(list, elements);
    return list;
  }

  public static <T> LinkedHashSet<T> newLinkedHashSet(int size, Supplier<T> supplier) {
    Objects.requireNonNull(supplier, "no valid supplier provided");
    LinkedHashSet<T> list = LinkedHashSet.newLinkedHashSet(validateSize(size));
    addAll(list, size, supplier);
    return list;
  }

  public static <T> LinkedHashSet<T> newLinkedHashSet(int size, IntFunction<T> function) {
    Objects.requireNonNull(function, "no valid function provided");
    LinkedHashSet<T> list = LinkedHashSet.newLinkedHashSet(validateSize(size));
    addAll(list, size, function);
    return list;
  }

  public static LinkedHashSet<String> newLinkedHashSet(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    LinkedHashSet<String> list = LinkedHashSet.newLinkedHashSet(64);
    Readers.addAll(list, reader);
    return list;
  }

  public static long[] toLongArray(List<String> list) {
    Objects.requireNonNull(list, "no valid list provided");
    int size = list.size();
    long[] array = new long[size];
    int i = 0;
    for (String s : list) {
      array[i++] = Long.parseLong(s);
    }
    return array;
  }

  public static List<Long> toLongList(List<String> list) {
    Objects.requireNonNull(list, "no valid list provided");
    List<Long> result = new ArrayList<>(list.size());
    for (String s : list) {
      result.add(Long.valueOf(s));
    }
    return result;
  }

  public static double[] toDoubleArray(List<String> list) {
    Objects.requireNonNull(list, "no valid list provided");
    int size = list.size();
    double[] array = new double[size];
    int i = 0;
    for (String s : list) {
      array[i++] = Double.parseDouble(s);
    }
    return array;
  }

  public static List<Double> toDoubleList(List<String> list) {
    Objects.requireNonNull(list, "no valid list provided");
    List<Double> result = new ArrayList<>(list.size());
    for (String s : list) {
      result.add(Double.valueOf(s));
    }
    return result;
  }
}
