package libext;

public class Types {

  private Types() {
  }

  private static boolean integerLike(String str) {
    int pos = 0;
    while (pos < str.length()) {
      char ch = str.charAt(pos);
      if (ch == '+' || ch == '-') {
        if (pos != 0) {
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

  public static Number toNumber(Object obj) {
    if (obj != null) {
      switch (obj) {
        case Number number -> {
          return number;
        }
        case String str -> {
          if (str.isBlank()) {
            throw new RuntimeException("failed to parse number: " + str);
          }
          str = str.trim();
          if (integerLike(str)) {
            try {
              return Long.parseLong(str);
            } catch (NumberFormatException e) {
              throw new RuntimeException("failed to parse number: " + str);
            }
          }
          try {
            return Double.parseDouble(str);
          } catch (NumberFormatException e) {
            throw new RuntimeException("failed to parse number: " + str);
          }
        }
        default -> {
        }
      }
    }
    return null;
  }

  public static Boolean toBoolean(Object obj) {
    if (obj != null) {
      switch (obj) {
        case Boolean bool -> {
          return bool;
        }
        case Number number -> {
          return number.intValue() != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        case String str -> {
          return Boolean.parseBoolean(str);
        }
        default -> {
        }
      }
    }
    return null;
  }

  public static Byte toByte(Object obj) {
    Number number = toNumber(obj);
    if (number != null) {
      if (number instanceof Byte byteNum) {
        return byteNum;
      }
      return number.byteValue();
    }
    return null;
  }

  public static Short toShort(Object obj) {
    Number number = toNumber(obj);
    if (number != null) {
      if (number instanceof Short shortNum) {
        return shortNum;
      }
      return number.shortValue();
    }
    return null;
  }

  public static Integer toInteger(Object obj) {
    Number number = toNumber(obj);
    if (number != null) {
      if (number instanceof Integer intNum) {
        return intNum;
      }
      return number.intValue();
    }
    return null;
  }

  public static Long toLong(Object obj) {
    Number number = toNumber(obj);
    if (number != null) {
      if (number instanceof Long longNum) {
        return longNum;
      }
      return number.longValue();
    }
    return null;
  }

  public static Float toFloat(Object obj) {
    Number number = toNumber(obj);
    if (number != null) {
      if (number instanceof Float floatNum) {
        return floatNum;
      }
      return number.floatValue();
    }
    return null;
  }

  public static Double toDouble(Object obj) {
    Number number = toNumber(obj);
    if (number != null) {
      if (number instanceof Double doubleNum) {
        return doubleNum;
      }
      return number.doubleValue();
    }
    return null;
  }

  public static String toString(Object obj) {
    if (obj != null) {
      return obj.toString();
    }
    return null;
  }
}
