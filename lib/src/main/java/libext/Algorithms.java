package libext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class Algorithms {

  private Algorithms() {
    throw new AssertionError("no instances");
  }

  private static final int LIST_THRESHOLD = 10;

  public static <T> boolean allMatch(Collection<T> col, Predicate<T> pred) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(pred, "no valid predicate provided");
    for (T t : col) {
      if (!pred.test(t)) {
        return false;
      }
    }
    return true;
  }

  public static <T> boolean anyMatch(Collection<T> col, Predicate<T> pred) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(pred, "no valid predicate provided");
    for (T t : col) {
      if (pred.test(t)) {
        return true;
      }
    }
    return false;
  }

  public static <T> long count(Collection<T> col, Object o) {
    Objects.requireNonNull(col, "no valid collection provided");
    long count = 0;
    for (T t : col) {
      if (Objects.equals(o, t)) {
        count++;
      }
    }
    return count;
  }

  public static <T> long countIf(Collection<T> col, Predicate<T> pred) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(pred, "no valid predicate provided");
    long count = 0;
    for (T t : col) {
      if (pred.test(t)) {
        count++;
      }
    }
    return count;
  }

  public static <T> void filter(List<T> dest, List<T> src, Predicate<T> pred) {
    Objects.requireNonNull(dest, "no valid destination provided");
    Objects.requireNonNull(src, "no valid source provided");
    Objects.requireNonNull(pred, "no valid predicate provided");
    int srcSize = src.size();
    int destSize = dest.size();
    if (srcSize > destSize) {
      throw new IndexOutOfBoundsException("src size is greater than dest size");
    }
    if (srcSize < LIST_THRESHOLD ||
        (src instanceof RandomAccess && dest instanceof RandomAccess)) {
      for (int i = 0; i < srcSize; i++) {
        T t = src.get(i);
        if (pred.test(t)) {
          dest.set(i, t);
        }
      }
    } else {
      ListIterator<T> srcIter = src.listIterator();
      ListIterator<T> destIter = dest.listIterator();
      for (int i = 0; i < srcSize; i++) {
        destIter.next();
        T t = srcIter.next();
        if (pred.test(t)) {
          destIter.set(t);
        }
      }
    }
  }

  public static <T> List<T> findAll(Collection<T> col, Object o) {
    Objects.requireNonNull(col, "no valid collection provided");
    ArrayList<T> list = new ArrayList<>();
    for (T t : col) {
      if (Objects.equals(o, t)) {
        list.add(t);
      }
    }
    return list;
  }

  public static <T> T findFirst(Collection<T> col, Object o) {
    Objects.requireNonNull(col, "no valid collection provided");
    for (T t : col) {
      if (Objects.equals(o, t)) {
        return t;
      }
    }
    return null;
  }

  public static <T> List<T> findIf(Collection<T> col, Predicate<T> pred) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(pred, "no valid predicate provided");
    ArrayList<T> list = new ArrayList<>();
    for (T t : col) {
      if (pred.test(t)) {
        list.add(t);
      }
    }
    return list;
  }

  public static <T, R> void map(List<R> dest, List<T> src, Function<T, R> mapper) {
    Objects.requireNonNull(dest, "no valid destination provided");
    Objects.requireNonNull(src, "no valid source provided");
    Objects.requireNonNull(mapper, "no valid mapper provided");
    int srcSize = src.size();
    int destSize = dest.size();
    if (srcSize > destSize) {
      throw new IndexOutOfBoundsException("src size is greater than dest size");
    }
    if (srcSize < LIST_THRESHOLD ||
        (src instanceof RandomAccess && dest instanceof RandomAccess)) {
      for (int i = 0; i < srcSize; i++) {
        dest.set(i, mapper.apply(src.get(i)));
      }
    } else {
      ListIterator<T> srcIter = src.listIterator();
      ListIterator<R> destIter = dest.listIterator();
      for (int i = 0; i < srcSize; i++) {
        destIter.next();
        destIter.set(mapper.apply(srcIter.next()));
      }
    }
  }

  public static <T> boolean noneMatch(Collection<T> col, Predicate<T> pred) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(pred, "no valid predicate provided");
    for (T t : col) {
      if (pred.test(t)) {
        return false;
      }
    }
    return true;
  }

  public static <T> T reduce(Collection<T> col, T initial, BinaryOperator<T> op) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(op, "no valid operator provided");
    T result = initial;
    for (T t : col) {
      result = op.apply(result, t);
    }
    return result;
  }
}
