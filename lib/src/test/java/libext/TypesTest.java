package libext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Optional;

class TypesTest {

  @Nested
  class ToStringTest {

    @Test
    void shouldConvertBooleanLiteralToStringOptional() {
      assertEquals(Optional.of("true"), Types.toString(true));
      assertEquals(Optional.of("false"), Types.toString(Boolean.FALSE));
    }

    @Test
    void shouldConvertLongLiteralToStringOptional() {
      assertEquals(Optional.of("123456789"), Types.toString(123456789L));
      assertEquals(Optional.of("-987654321"), Types.toString(-987654321L));
    }

    @Test
    void shouldConvertInstantToStringOptional() {
      Instant now = Instant.now();
      assertEquals(Optional.of(now.toString()), Types.toString(now));

      Instant instant = Instant.parse("2026-08-17T10:00:00Z");
      assertEquals(Optional.of("2026-08-17T10:00:00Z"), Types.toString(instant));
    }

    @Test
    void shouldConvertNullReferenceToNullStringOptional() {
      Optional<String> result = Types.toString(null);
      assertTrue(result.isPresent());
      assertEquals("null", result.get());
    }

    @Test
    void shouldConvertNullStringToEmptyOptional() {
      assertTrue(Types.toString("null").isEmpty());
      assertTrue(Types.toString("NULL").isEmpty());
      assertTrue(Types.toString("Null").isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {" null ", "null\n"})
    void shouldConvertRandomStringToSameStringOptional(String input) {
      Optional<String> result = Types.toString(input);
      assertTrue(result.isPresent());
      assertEquals(input, result.get());
    }

    @Test
    void shouldConvertLiteralToStringOptional() {
      assertEquals(Optional.of("123"), Types.toString(123));
      assertEquals(Optional.of("12.34"), Types.toString(12.34));
      assertEquals(Optional.of("true"), Types.toString(true));
    }

    @Test
    void shouldConvertObjectToDefaultStringOptional() {
      Optional<String> result = Types.toString(new Object());
      assertTrue(result.isPresent());
      assertTrue(result.get().startsWith("java.lang.Object@"));
    }
  }

  @Nested
  class ToBooleanTest {

    @Test
    void shouldConvertInstantToBooleanOptional() {
      assertEquals(Optional.of(false), Types.toBoolean(java.time.Instant.EPOCH));
      assertEquals(Optional.of(true), Types.toBoolean(java.time.Instant.now()));
      assertEquals(Optional.of(true), Types.toBoolean(java.time.Instant.ofEpochMilli(123456L)));
    }

    @Test
    void shouldConvertLiteralToBooleanOptional() {
      assertEquals(Optional.of(true), Types.toBoolean(Boolean.TRUE));
      assertEquals(Optional.of(false), Types.toBoolean(Boolean.FALSE));
      assertEquals(Optional.of(true), Types.toBoolean(1));
      assertEquals(Optional.of(true), Types.toBoolean(-5));
      assertEquals(Optional.of(false), Types.toBoolean(0));
    }

    @Test
    void shouldConvertNullToEmptyOptional() {
      assertTrue(Types.toBoolean(null).isEmpty());
      assertTrue(Types.toBoolean("null").isEmpty());
      assertTrue(Types.toBoolean("NULL").isEmpty());
      assertTrue(Types.toBoolean("Null").isEmpty());
    }

    @Test
    void shouldConvertStringToBooleanOptional() {
      assertEquals(Optional.of(true), Types.toBoolean("true"));
      assertEquals(Optional.of(true), Types.toBoolean("TRUE"));
      assertEquals(Optional.of(true), Types.toBoolean("TrUe"));
      assertEquals(Optional.of(false), Types.toBoolean("false"));
      assertEquals(Optional.of(false), Types.toBoolean(" true "));
      assertEquals(Optional.of(false), Types.toBoolean("not-a-boolean"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\n"})
    void shouldThrowExceptionOnBlankString(String blankInput) {
      RuntimeException exception = assertThrows(RuntimeException.class,
          () -> Types.toBoolean(blankInput));
      assertTrue(exception.getMessage().startsWith("failed to parse boolean:"));
    }

    @Test
    void shouldConvertObjectToEmptyOptional() {
      assertTrue(Types.toBoolean(new Object()).isEmpty());
    }
  }

  @Nested
  class ToInstantTest {

    @Test
    void shouldConvertTrueToCurrentInstant() {
      Instant before = Instant.now();
      Optional<Instant> result = Types.toInstant(true);
      Instant after = Instant.now();
      assertTrue(result.isPresent());

      Instant actual = result.get();
      assertTrue(!actual.isBefore(before) && !actual.isAfter(after));
    }

    @Test
    void shouldConvertFalseToEpochInstant() {
      Optional<Instant> result = Types.toInstant(false);
      assertTrue(result.isPresent());
      assertEquals(Instant.EPOCH, result.get());
    }

    @Test
    void shouldConvertInstantToItself() {
      Instant expected = Instant.parse("2026-08-17T12:00:00Z");
      Optional<Instant> result = Types.toInstant(expected);
      assertTrue(result.isPresent());
      assertEquals(expected, result.get());
    }

    @Test
    void shouldConvertLongToEpochSecondInstant() {
      long epochSecond = 1692273600L;
      Optional<Instant> result = Types.toInstant(epochSecond);
      assertTrue(result.isPresent());
      assertEquals(Instant.ofEpochSecond(epochSecond), result.get());
    }

    @Test
    void shouldConvertIntegerToEpochSecondInstant() {
      int epochSecond = 1692273600;
      Optional<Instant> result = Types.toInstant(epochSecond);
      assertTrue(result.isPresent());
      assertEquals(Instant.ofEpochSecond(epochSecond), result.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "NULL", "Null"})
    void shouldConvertNullStringToEmptyOptional(String nullStr) {
      Optional<Instant> result = Types.toInstant(nullStr);
      assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertValidIsoStringToInstant() {
      String isoString = "2026-08-17T12:00:00Z";
      Optional<Instant> result = Types.toInstant(isoString);
      assertTrue(result.isPresent());
      assertEquals(Instant.parse(isoString), result.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldThrowExceptionForBlankStrings(String blankStr) {
      RuntimeException exception = assertThrows(RuntimeException.class,
          () -> {
            Types.toInstant(blankStr);
          });
      assertTrue(exception.getMessage().startsWith("failed to parse instant:"));
    }

    @Test
    void shouldThrowExceptionForInvalidFormatString() {
      RuntimeException exception = assertThrows(RuntimeException.class,
          () -> {
            Types.toInstant("invalid-date-format");
          });
      assertTrue(exception.getMessage().startsWith("failed to parse instant:"));
    }

    @Test
    void shouldConvertNullToEmptyOptional() {
      Optional<Instant> result = Types.toInstant(null);
      assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertObjectToEmptyOptional() {
      Object unsupported = new Object();
      Optional<Instant> result = Types.toInstant(unsupported);
      assertTrue(result.isEmpty());
    }

  }

  @Nested
  class ToNumberTest {

    @Test
    void shouldConvertBooleanToLongOptional() {
      Optional<Number> trueResult = Types.toNumber(true);
      assertTrue(trueResult.isPresent());
      assertInstanceOf(Long.class, trueResult.get());
      assertEquals(1L, trueResult.get());

      Optional<Number> falseResult = Types.toNumber(false);
      assertTrue(falseResult.isPresent());
      assertInstanceOf(Long.class, falseResult.get());
      assertEquals(0L, falseResult.get());
    }

    @Test
    void shouldConvertInstantToEpochSecondOptional() {
      Instant now = Instant.now();
      Optional<Number> result = Types.toNumber(now);
      assertTrue(result.isPresent());
      assertInstanceOf(Long.class, result.get());
      assertEquals(now.getEpochSecond(), result.get());
    }


    @Test
    void shouldConvertStringToLongOptional() {
      Optional<Number> result = Types.toNumber("123");
      assertTrue(result.isPresent());
      assertInstanceOf(Long.class, result.get());
      assertEquals(123L, result.get());
      assertEquals(Optional.of(-50L), Types.toNumber("-50"));
      assertEquals(Optional.of(100L), Types.toNumber("+100"));
    }

    @Test
    void shouldConvertStringToDoubleOptional() {
      Optional<Number> decimalResult = Types.toNumber("12.34");
      assertTrue(decimalResult.isPresent());
      assertInstanceOf(Double.class, decimalResult.get());
      assertEquals(12.34, decimalResult.get());

      Optional<Number> scientificResult = Types.toNumber("1e3");
      assertTrue(scientificResult.isPresent());
      assertInstanceOf(Double.class, scientificResult.get());
      assertEquals(1000.0, scientificResult.get());
    }

    @Test
    void shouldConvertNullToEmptyOptional() {
      assertTrue(Types.toNumber("null").isEmpty());
      assertTrue(Types.toNumber("NULL").isEmpty());
      assertTrue(Types.toNumber("Null").isEmpty());
      assertTrue(Types.toNumber(null).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "+", "-", "12.3.4", "abc"})
    void shouldThrowExceptionOnInvalidString(String invalidInput) {
      RuntimeException exception = assertThrows(RuntimeException.class,
          () -> Types.toNumber(invalidInput));
      assertTrue(exception.getMessage().startsWith("failed to parse number:"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.05d", "0.05D"})
    void shouldConvertLiteralToDoubleOptional(String input) {
      Optional<Number> result = Types.toNumber(input);
      assertTrue(result.isPresent());
      assertInstanceOf(Double.class, result.get());
      assertEquals(0.05, result.get().doubleValue(), 0.000001);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.05M", "0M005"})
    void shouldThrowExceptionOnInvalidLiteral(String invalidInput) {
      RuntimeException exception = assertThrows(RuntimeException.class,
          () -> Types.toNumber(invalidInput));
      assertTrue(exception.getMessage().startsWith("failed to parse number:"));
    }

    @Test
    void shouldConvertObjectToEmptyOptional() {
      assertTrue(Types.toNumber(new Object()).isEmpty());
    }
  }

  @Nested
  class NarrowingTypeTest {

    @Test
    void shouldConvertBooleanToSpecificNumberOptional() {
      assertEquals(Optional.of((byte) 1), Types.toByte(true));
      assertEquals(Optional.of((short) 1), Types.toShort(true));
      assertEquals(Optional.of(1), Types.toInteger(true));
      assertEquals(Optional.of(1L), Types.toLong(true));
      assertEquals(Optional.of(1.0f), Types.toFloat(true));
      assertEquals(Optional.of(1.0), Types.toDouble(true));
      assertEquals(Optional.of((byte) 0), Types.toByte(false));
      assertEquals(Optional.of((short) 0), Types.toShort(false));
      assertEquals(Optional.of(0), Types.toInteger(false));
      assertEquals(Optional.of(0L), Types.toLong(false));
      assertEquals(Optional.of(0.0f), Types.toFloat(false));
      assertEquals(Optional.of(0.0), Types.toDouble(false));
    }

    @Test
    void shouldConvertLongToSpecificNumberOptional() {
      Long value = 42L;
      assertEquals(Optional.of((byte) 42), Types.toByte(value));
      assertEquals(Optional.of((short) 42), Types.toShort(value));
      assertEquals(Optional.of(42), Types.toInteger(value));
      assertEquals(Optional.of(42L), Types.toLong(value));
      assertEquals(Optional.of(42.0f), Types.toFloat(value));
      assertEquals(Optional.of(42.0), Types.toDouble(value));
    }

    @Test
    void shouldConvertInstantToSpecificNumberOptional() {
      long epochSecond = 1700000000L;
      Instant instant = Instant.ofEpochSecond(epochSecond);
      assertEquals(Optional.of((byte) epochSecond), Types.toByte(instant));
      assertEquals(Optional.of((short) epochSecond), Types.toShort(instant));
      assertEquals(Optional.of((int) epochSecond), Types.toInteger(instant));
      assertEquals(Optional.of(epochSecond), Types.toLong(instant));
      assertEquals(Optional.of((float) epochSecond), Types.toFloat(instant));
      assertEquals(Optional.of((double) epochSecond), Types.toDouble(instant));
    }

    @Test
    void shouldConvertStringToSpecificNumberOptional() {
      assertEquals(Optional.of((byte) 12), Types.toByte("12"));
      assertEquals(Optional.of((short) 500), Types.toShort("500"));
      assertEquals(Optional.of(100_000), Types.toInteger("100000"));
      assertEquals(Optional.of(9_000_000_000L), Types.toLong("9000000000"));
      assertEquals(Optional.of(12.5f), Types.toFloat("12.5"));
      assertEquals(Optional.of(123.456), Types.toDouble("123.456"));
    }

    @Test
    void shouldConvertNullToEmptyOptional() {
      Object nullStr = "null";
      assertTrue(Types.toByte(nullStr).isEmpty());
      assertTrue(Types.toShort(nullStr).isEmpty());
      assertTrue(Types.toInteger(nullStr).isEmpty());
      assertTrue(Types.toLong(nullStr).isEmpty());
      assertTrue(Types.toFloat(nullStr).isEmpty());
      assertTrue(Types.toDouble(nullStr).isEmpty());
      assertTrue(Types.toByte(null).isEmpty());
      assertTrue(Types.toShort(null).isEmpty());
      assertTrue(Types.toInteger(null).isEmpty());
      assertTrue(Types.toLong(null).isEmpty());
      assertTrue(Types.toFloat(null).isEmpty());
      assertTrue(Types.toDouble(null).isEmpty());
    }

    @Test
    void shouldConvertObjectToEmptyOptional() {
      Object defaultObj = new Object();
      assertTrue(Types.toByte(defaultObj).isEmpty());
      assertTrue(Types.toShort(defaultObj).isEmpty());
      assertTrue(Types.toInteger(defaultObj).isEmpty());
      assertTrue(Types.toLong(defaultObj).isEmpty());
      assertTrue(Types.toFloat(defaultObj).isEmpty());
      assertTrue(Types.toDouble(defaultObj).isEmpty());
    }
  }
}
