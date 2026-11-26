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

  public static boolean isTrue(boolean condition, Supplier<String> supplier) {
    if (!condition) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return true;
  }

  public static <T> T notNull(T object, Supplier<String> supplier) {
    if (object == null) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return object;
  }

  public static String hasContent(String text, Supplier<String> supplier) {
    if (text == null || text.isBlank()) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return text;
  }

  public static <T extends Number> T isPositive(T number, Supplier<String> supplier) {
    if (number == null || number.doubleValue() < 0) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return number;
  }

  public static <T extends Number> T isNegative(T number, Supplier<String> supplier) {
    if (number == null || number.doubleValue() >= 0) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return number;
  }

  public static Instant isPast(Instant instant, Supplier<String> supplier) {
    if (instant == null || instant.isAfter(Instant.now())) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return instant;
  }

  public static Instant isFuture(Instant instant, Supplier<String> supplier) {
    if (instant == null || instant.isBefore(Instant.now())) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return instant;
  }

  public static <T extends Collection<?>> T notEmpty(T collection, Supplier<String> supplier) {
    if (collection == null || collection.isEmpty()) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return collection;
  }

  public static <T extends Multiset<?>> T notEmpty(T multiset, Supplier<String> supplier) {
    if (multiset == null || multiset.isEmpty()) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return multiset;
  }

  public static <T extends Map<?, ?>> T notEmpty(T map, Supplier<String> supplier) {
    if (map == null || map.isEmpty()) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return map;
  }

  public static <T extends Multimap<?, ?>> T notEmpty(T multimap, Supplier<String> supplier) {
    if (multimap == null || multimap.isEmpty()) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return multimap;
  }

  public static <T> T isInstanceOf(Class<T> type, Object object, Supplier<String> supplier) {
    if (type == null || !type.isInstance(object)) {
      throw new IllegalArgumentException(resolveMessage(supplier));
    }
    return type.cast(object);
  }
}
