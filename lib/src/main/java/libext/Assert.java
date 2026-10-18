package libext;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

///
/// Provides pre-condition checks for method arguments.
public final class Assert {

  private Assert() {
    throw new AssertionError("no instances");
  }

  private static String resolveMessage(Supplier<String> supplier) {
    return supplier == null ? "assertion failed" : supplier.get();
  }

  public static void isTrue(boolean condition, Supplier<String> supplier) {
    if (!condition) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }

  public static void notNull(Object object, Supplier<String> supplier) {
    if (object == null) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }

  public static void hasContent(String text, Supplier<String> supplier) {
    if (text == null || text.trim().isEmpty()) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }

  public static <T extends Number> void isPositive(T number, Supplier<String> supplier) {
    if (number == null || number.doubleValue() < 0) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }

  public static <T extends Number> void isNegative(T number, Supplier<String> supplier) {
    if (number == null || number.doubleValue() >= 0) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }

  public static void isPast(Instant instant, Supplier<String> supplier) {
    if (instant == null || instant.isAfter(Instant.now())) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }

  public static void isFuture(Instant instant, Supplier<String> supplier) {
    if (instant == null || instant.isBefore(Instant.now())) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }

  public static void notEmpty(Collection<?> collection, Supplier<String> supplier) {
    if (collection == null || collection.isEmpty()) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }

  public static void notEmpty(Multiset<?> multiset, Supplier<String> supplier) {
    if (multiset == null || multiset.isEmpty()) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }

  public static void notEmpty(Map<?, ?> map, Supplier<String> supplier) {
    if (map == null || map.isEmpty()) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }

  public static void notEmpty(Multimap<?, ?> multimap, Supplier<String> supplier) {
    if (multimap == null || multimap.isEmpty()) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }

  public static <T> void isInstanceOf(Class<T> type, Object object, Supplier<String> supplier) {
    if (type == null || !type.isInstance(object)) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
  }
}
