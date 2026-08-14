package libext;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TypesTest {

  @Nested
  class ToStringTest {

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
  class ToNumberTest {

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
