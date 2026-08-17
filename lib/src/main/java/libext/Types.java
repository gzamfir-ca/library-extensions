package libext;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public final class Types {

  private Types() {
    throw new AssertionError("no instances");
  }

  private static boolean integerLike(String str) {
    if (str.isBlank()) {
      return false;
    }
    int pos = 0;
    while (pos < str.length()) {
      char ch = str.charAt(pos);
      if (ch == '+' || ch == '-') {
        if (pos != 0 || str.length() == 1) {
          return false;
        }
      } else if (ch == '.' || ch == 'e' || ch == 'E') {
        return false;
      } else if (!Character.isDigit(ch)) {
        return false;
      }
      pos++;
    }
    return true;
  }

  static Optional<Number> toNumber(Object obj) {
    return Optional.ofNullable(
        switch (obj) {
          case Boolean bool -> bool ? 1L : 0L;
          case Instant instant -> instant.getEpochSecond();
          case Number number -> number;
          case String str when "null".equalsIgnoreCase(str) -> null;
          case String str -> {
            if (str.isBlank()) {
              throw new RuntimeException("failed to parse number: " + str);
            }
            str = str.trim();
            if (integerLike(str)) {
              try {
                yield Long.parseLong(str);
              } catch (NumberFormatException e) {
                throw new RuntimeException("failed to parse number: " + str);
              }
            }
            try {
              yield Double.parseDouble(str);
            } catch (NumberFormatException e) {
              throw new RuntimeException("failed to parse number: " + str);
            }
          }
          case null, default -> null;
        }
    );
  }

  public static Optional<Boolean> toBoolean(Object obj) {
    return Optional.ofNullable(
        switch (obj) {
          case Boolean bool -> bool;
          case Instant instant -> !instant.equals(Instant.EPOCH);
          case Number number -> number.longValue() != 0L;
          case String str when "null".equalsIgnoreCase(str) -> null;
          case String str -> {
            if (str.isBlank()) {
              throw new RuntimeException("failed to parse boolean: " + str);
            }
            yield Boolean.parseBoolean(str);
          }
          case null, default -> null;
        }
    );
  }

  public static Optional<Instant> toInstant(Object obj) {
    return Optional.ofNullable(
        switch (obj) {
          case Boolean bool -> bool ? Instant.now() : Instant.EPOCH;
          case Instant instant -> instant;
          case Number number -> Instant.ofEpochSecond(number.longValue());
          case String str when "null".equalsIgnoreCase(str) -> null;
          case String str -> {
            if (str.isBlank()) {
              throw new RuntimeException("failed to parse instant: " + str);
            }
            try {
              yield Instant.parse(str);
            } catch (DateTimeParseException e) {
              throw new RuntimeException("failed to parse instant: " + str);
            }
          }
          case null, default -> null;
        }
    );
  }

  public static Optional<Byte> toByte(Object obj) {
    return toNumber(obj).map(Number::byteValue);
  }

  public static Optional<Short> toShort(Object obj) {
    return toNumber(obj).map(Number::shortValue);
  }

  public static Optional<Integer> toInteger(Object obj) {
    return toNumber(obj).map(Number::intValue);
  }

  public static Optional<Long> toLong(Object obj) {
    return toNumber(obj).map(Number::longValue);

  }

  public static Optional<Float> toFloat(Object obj) {
    return toNumber(obj).map(Number::floatValue);
  }

  public static Optional<Double> toDouble(Object obj) {
    return toNumber(obj).map(Number::doubleValue);
  }

  public static Optional<String> toString(Object obj) {
    if (obj instanceof String str && "null".equalsIgnoreCase(str)) {
      return Optional.empty();
    }
    return Optional.of(String.valueOf(obj));
  }
}
