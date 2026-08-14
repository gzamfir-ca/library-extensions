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
    void shouldMapNullReferenceToOptionalContainingStringNull() {
      Optional<String> result = Types.toString(null);
      assertTrue(result.isPresent());
      assertEquals("null", result.get());
    }

    @Test
    void shouldMapExactNullStringToOptionalEmpty() {
      assertTrue(Types.toString("null").isEmpty());
      assertTrue(Types.toString("NULL").isEmpty());
      assertTrue(Types.toString("Null").isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {" null ", "null\n"})
    void shouldNotMapVariationsWithSpacesToOptionalEmpty(String input) {
      Optional<String> result = Types.toString(input);
      assertTrue(result.isPresent());
      assertEquals(input, result.get());
    }

    @Test
    void shouldConvertStandardObjectsToString() {
      assertEquals(Optional.of("123"), Types.toString(123));
      assertEquals(Optional.of("true"), Types.toString(true));
    }

    @Test
    void shouldConvertArbitraryObjectToStandardClassNameString() {
      Optional<String> result = Types.toString(new Object());
      assertTrue(result.isPresent());
      assertTrue(result.get().startsWith("java.lang.Object@"));
    }
  }

  @Nested
  class ToBooleanTest {
    @Test
    void shouldHandleBooleanInputs() {
      assertEquals(Optional.of(true), Types.toBoolean(Boolean.TRUE));
      assertEquals(Optional.of(false), Types.toBoolean(Boolean.FALSE));
    }

    @Test
    void shouldHandleNumericInputs() {
      assertEquals(Optional.of(true), Types.toBoolean(1));
      assertEquals(Optional.of(true), Types.toBoolean(-5));
      assertEquals(Optional.of(false), Types.toBoolean(0));
    }

    @Test
    void shouldMapExactNullStringAndNullReferenceToOptionalEmpty() {
      assertTrue(Types.toBoolean("null").isEmpty());
      assertTrue(Types.toBoolean("NULL").isEmpty());
      assertTrue(Types.toBoolean("Null").isEmpty());
      assertTrue(Types.toBoolean(null).isEmpty());
    }

    @Test
    void shouldMatchPlatformDefaultBooleanParseBehavior() {
      assertEquals(Optional.of(true), Types.toBoolean("true"));
      assertEquals(Optional.of(true), Types.toBoolean("TRUE"));
      assertEquals(Optional.of(true), Types.toBoolean("TrUe"));
      assertEquals(Optional.of(false), Types.toBoolean(" true "));
      assertEquals(Optional.of(false), Types.toBoolean("not-a-boolean"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\n"})
    void shouldThrowExceptionOnBlankStrings(String blankInput) {
      RuntimeException exception = assertThrows(RuntimeException.class,
          () -> Types.toBoolean(blankInput));
      assertTrue(exception.getMessage().startsWith("failed to parse boolean:"));
    }

    @Test
    void shouldReturnEmptyOptionalForArbitraryObjectInstances() {
      assertTrue(Types.toBoolean(new Object()).isEmpty());
    }
  }

  @Nested
  class ToNumberTest {
    @Test
    void shouldParseValidIntegersAsLong() {
      Optional<Number> result = Types.toNumber("123");
      assertTrue(result.isPresent());
      assertInstanceOf(Long.class, result.get());
      assertEquals(123L, result.get());
    }

    @Test
    void shouldParseValidDecimalsAndScientificNotationAsDouble() {
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
    void shouldHandleLeadingSignsForIntegers() {
      assertEquals(Optional.of(-50L), Types.toNumber("-50"));
      assertEquals(Optional.of(100L), Types.toNumber("+100"));
    }

    @Test
    void shouldMapExactNullStringAndNullReferenceToOptionalEmpty() {
      assertTrue(Types.toNumber("null").isEmpty());
      assertTrue(Types.toNumber("NULL").isEmpty());
      assertTrue(Types.toNumber("Null").isEmpty());
      assertTrue(Types.toNumber(null).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "+", "-", "12.3.4", "abc"})
    void shouldThrowExceptionOnInvalidNumericInputs(String invalidInput) {
      RuntimeException exception = assertThrows(RuntimeException.class,
          () -> Types.toNumber(invalidInput));
      assertTrue(exception.getMessage().startsWith("failed to parse number:"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.05d", "0.05D"})
    void shouldParseDoubleLiteralSuffixes(String input) {
      Optional<Number> result = Types.toNumber(input);
      assertTrue(result.isPresent());
      assertEquals(0.05, result.get().doubleValue(), 0.000001);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.05M", "0M005"})
    void shouldThrowOnUnsupportedDecimalSuffixes(String input) {
      RuntimeException exception = assertThrows(RuntimeException.class,
          () -> Types.toNumber(input));
      assertTrue(exception.getMessage().startsWith("failed to parse number:"));
    }

    @Test
    void shouldReturnEmptyOptionalForArbitraryObjectInstances() {
      assertTrue(Types.toNumber(new Object()).isEmpty());
    }
  }

  @Nested
  class NarrowingTypeTest {
    @Test
    void shouldSafelyDowncastUsingFunctionalPipelines() {
      assertEquals(Optional.of((byte) 12), Types.toByte("12"));
      assertEquals(Optional.of((short) 500), Types.toShort("500"));
      assertEquals(Optional.of(100_000), Types.toInteger("100000"));
      assertEquals(Optional.of(9_000_000_000L), Types.toLong("9000000000"));
      assertEquals(Optional.of(12.5f), Types.toFloat("12.5"));
      assertEquals(Optional.of(123.456), Types.toDouble("123.456"));
    }

    @Test
    void shouldPropagateEmptyOptionalTriggersDownstream() {
      assertTrue(Types.toByte(null).isEmpty());
      assertTrue(Types.toShort(null).isEmpty());
      assertTrue(Types.toInteger(null).isEmpty());
      assertTrue(Types.toLong(null).isEmpty());
      assertTrue(Types.toFloat(null).isEmpty());
      assertTrue(Types.toDouble(null).isEmpty());

      Object nullStr = "null";
      assertTrue(Types.toByte(nullStr).isEmpty());
      assertTrue(Types.toShort(nullStr).isEmpty());
      assertTrue(Types.toInteger(nullStr).isEmpty());
      assertTrue(Types.toLong(nullStr).isEmpty());
      assertTrue(Types.toFloat(nullStr).isEmpty());
      assertTrue(Types.toDouble(nullStr).isEmpty());
    }

    @Test
    void shouldReturnEmptyOptionalForArbitraryObjectsDownstream() {
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
