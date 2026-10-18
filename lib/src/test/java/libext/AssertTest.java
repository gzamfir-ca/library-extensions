package libext;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssertTest {

  private static final Supplier<String> MSG = () -> "custom error message";

  @Nested
  class IsTrueTests {

    @Test
    void shouldPassOnTrueCondition() {
      assertDoesNotThrow(() -> Assert.isTrue(true, MSG));
    }

    @Test
    void shouldThrowOnFalseCondition() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isTrue(false, MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class NotNullTests {

    @Test
    void shouldPassOnValidObject() {
      assertDoesNotThrow(() -> Assert.notNull(new Object(), MSG));
    }

    @Test
    void shouldThrowOnNullObject() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.notNull(null, MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class HasContentTests {

    @Test
    void shouldPassOnValidText() {
      assertDoesNotThrow(() -> Assert.hasContent("valid text", MSG));
    }

    @Test
    void shouldThrowOnNullText() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.hasContent(null, MSG));
      assertEquals("custom error message", ex.getMessage());
    }

    @Test
    void shouldThrowOnEmptyText() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.hasContent("", MSG));
      assertEquals("custom error message", ex.getMessage());
    }

    @Test
    void shouldThrowOnWhitespaceText() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.hasContent("   ", MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class IsPositiveTests {

    @Test
    void shouldPassOnPositiveNumber() {
      assertDoesNotThrow(() -> Assert.isPositive(10, MSG));
      assertDoesNotThrow(() -> Assert.isPositive(0.1, MSG));
      assertDoesNotThrow(() -> Assert.isPositive(0, MSG));
    }

    @Test
    void shouldThrowOnNegativeNumber() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isPositive(-1, MSG));
      assertEquals("custom error message", ex.getMessage());
    }

    @Test
    void shouldThrowOnNullNumber() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isPositive(null, MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class IsNegativeTests {

    @Test
    void shouldPassOnNegativeNumber() {
      assertDoesNotThrow(() -> Assert.isNegative(-5, MSG));
    }

    @Test
    void shouldThrowOnZeroOrPositiveNumber() {
      IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
          () -> Assert.isNegative(0, MSG));
      assertEquals("custom error message", ex1.getMessage());
      IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
          () -> Assert.isNegative(5, MSG));
      assertEquals("custom error message", ex2.getMessage());
    }

    @Test
    void shouldThrowOnNullNumber() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isNegative(null, MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class IsPastTests {

    @Test
    void shouldPassOnPastInstant() {
      Instant past = Instant.now().minus(1, ChronoUnit.DAYS);
      assertDoesNotThrow(() -> Assert.isPast(past, MSG));
    }

    @Test
    void shouldThrowOnFutureInstant() {
      Instant future = Instant.now().plus(1, ChronoUnit.DAYS);
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isPast(future, MSG));
      assertEquals("custom error message", ex.getMessage());
    }

    @Test
    void shouldThrowOnNullInstant() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isPast(null, MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class IsFutureTests {

    @Test
    void shouldPassOnFutureInstant() {
      Instant future = Instant.now().plus(1, ChronoUnit.DAYS);
      assertDoesNotThrow(() -> Assert.isFuture(future, MSG));
    }

    @Test
    void shouldThrowOnPastInstant() {
      Instant past = Instant.now().minus(1, ChronoUnit.DAYS);
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isFuture(past, MSG));
      assertEquals("custom error message", ex.getMessage());
    }

    @Test
    void shouldThrowOnNullInstant() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isFuture(null, MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class CollectionNotEmptyTests {

    @Test
    void shouldPassOnNonEmptyCollection() {
      assertDoesNotThrow(() -> Assert.notEmpty(List.of("item"), MSG));
    }

    @Test
    void shouldThrowOnEmptyCollection() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.notEmpty(Collections.emptyList(), MSG));
      assertEquals("custom error message", ex.getMessage());
    }

    @Test
    void shouldThrowOnNullCollection() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.notEmpty((List<?>) null, MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class MultisetNotEmptyTests {

    @Test
    void shouldPassOnNonEmptyMultiset() {
      Multiset<String> multiset = Multiset.newMultiset();
      multiset.addKey("item");
      assertDoesNotThrow(() -> Assert.notEmpty(multiset, MSG));
    }

    @Test
    void shouldThrowOnEmptyMultiset() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.notEmpty(Multiset.newMultiset(), MSG));
      assertEquals("custom error message", ex.getMessage());
    }

    @Test
    void shouldThrowOnNullMultiset() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.notEmpty((Multiset<?>) null, MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class MapNotEmptyTests {

    @Test
    void shouldPassOnNonEmptyMap() {
      assertDoesNotThrow(() -> Assert.notEmpty(Map.of("key", "value"), MSG));
    }

    @Test
    void shouldThrowOnEmptyMap() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.notEmpty(Collections.emptyMap(), MSG));
      assertEquals("custom error message", ex.getMessage());
    }

    @Test
    void shouldThrowOnNullMap() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.notEmpty((Map<?, ?>) null, MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class MultimapNotEmptyTests {

    @Test
    void shouldPassOnNonEmptyMultimap() {
      Multimap<String, String> multimap = Multimap.newMultimap();
      multimap.addValue("key", "value");
      assertDoesNotThrow(() -> Assert.notEmpty(multimap, MSG));
    }

    @Test
    void shouldThrowOnEmptyMultimap() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.notEmpty(Multimap.newMultimap(), MSG));
      assertEquals("custom error message", ex.getMessage());
    }

    @Test
    void shouldThrowOnNullMultimap() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.notEmpty((Multimap<?, ?>) null, MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class IsInstanceOfTests {

    @Test
    void shouldPassOnCorrectInstance() {
      assertDoesNotThrow(() -> Assert.isInstanceOf(String.class, "hello", MSG));
    }

    @Test
    void shouldThrowOnWrongInstance() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isInstanceOf(String.class, 123, MSG));
      assertEquals("custom error message", ex.getMessage());
    }

    @Test
    void shouldThrowOnNullObject() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isInstanceOf(String.class, null, MSG));
      assertEquals("custom error message", ex.getMessage());
    }

    @Test
    void shouldThrowOnNullClassType() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isInstanceOf(null, "hello", MSG));
      assertEquals("custom error message", ex.getMessage());
    }
  }

  @Nested
  class EdgeCaseTests {

    @Test
    void shouldUseDefaultMessageOnNullSupplier() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Assert.isTrue(false, null));
      assertEquals("assertion failed", ex.getMessage());
    }
  }
}
