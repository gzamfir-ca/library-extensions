package libext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public class Algorithms {

  private Algorithms() {
    throw new AssertionError("no instances");
  }

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
