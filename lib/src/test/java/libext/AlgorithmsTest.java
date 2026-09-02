package libext;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AlgorithmsTest {

  @Nested
  class AllMatchTests {

    @Test
    void shouldReturnTrueWhenAllElementsMatchPredicate() {
      Collection<Integer> col = Arrays.asList(2, 4, 6);
      Predicate<Integer> isEven = n -> n % 2 == 0;
      assertTrue(Algorithms.allMatch(col, isEven));
    }

    @Test
    void shouldReturnFalseWhenOneElementDoesNotMatchPredicate() {
      Collection<Integer> col = Arrays.asList(2, 3, 4);
      Predicate<Integer> isEven = n -> n % 2 == 0;
      assertFalse(Algorithms.allMatch(col, isEven));
    }

    @Test
    void shouldReturnTrueWhenAllMatchIsCalledOnEmptyCollection() {
      Collection<Integer> col = Collections.emptyList();
      Predicate<Integer> isEven = n -> n % 2 == 0;
      assertTrue(Algorithms.allMatch(col, isEven));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenAllMatchReceivesNullCollection() {
      assertThrows(NullPointerException.class, () -> Algorithms.allMatch(null, n -> true));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenAllMatchReceivesNullPredicate() {
      Collection<Integer> col = Arrays.asList(1, 2);
      assertThrows(NullPointerException.class, () -> Algorithms.allMatch(col, null));
    }
  }

  @Nested
  class AnyMatchTests {

    @Test
    void shouldReturnTrueWhenAtLeastOneElementMatchesPredicate() {
      Collection<Integer> col = Arrays.asList(1, 3, 4);
      Predicate<Integer> isEven = n -> n % 2 == 0;
      assertTrue(Algorithms.anyMatch(col, isEven));
    }

    @Test
    void shouldReturnFalseWhenNoElementsMatchPredicate() {
      Collection<Integer> col = Arrays.asList(1, 3, 5);
      Predicate<Integer> isEven = n -> n % 2 == 0;
      assertFalse(Algorithms.anyMatch(col, isEven));
    }

    @Test
    void shouldReturnFalseWhenAnyMatchIsCalledOnEmptyCollection() {
      Collection<Integer> col = Collections.emptyList();
      Predicate<Integer> isEven = n -> n % 2 == 0;
      assertFalse(Algorithms.anyMatch(col, isEven));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenAnyMatchReceivesNullCollection() {
      assertThrows(NullPointerException.class, () -> Algorithms.anyMatch(null, n -> true));
    }
  }

  @Nested
  class CountTests {

    @Test
    void shouldReturnCorrectCountOfMatchingObjects() {
      Collection<String> col = Arrays.asList("apple", "banana", "apple", "orange");
      assertEquals(2, Algorithms.count(col, "apple"));
    }

    @Test
    void shouldReturnZeroCountWhenNoObjectsMatch() {
      Collection<String> col = Arrays.asList("apple", "banana");
      assertEquals(0, Algorithms.count(col, "orange"));
    }

    @Test
    void shouldReturnCorrectCountWhenSearchingForNullValues() {
      Collection<String> col = Arrays.asList("apple", null, "banana", null);
      assertEquals(2, Algorithms.count(col, null));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenCountReceivesNullCollection() {
      assertThrows(NullPointerException.class, () -> Algorithms.count(null, "target"));
    }
  }

  @Nested
  class CountIfTests {

    @Test
    void shouldReturnCorrectCountOfElementsMatchingPredicate() {
      Collection<Integer> col = Arrays.asList(1, 2, 3, 4, 5);
      Predicate<Integer> isEven = n -> n % 2 == 0;
      assertEquals(2, Algorithms.countIf(col, isEven));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenCountIfReceivesNullCollection() {
      assertThrows(NullPointerException.class, () -> Algorithms.countIf(null, n -> true));
    }
  }

  @Nested
  class FindAllTests {

    @Test
    void shouldReturnArrayListContainingAllMatchingObjects() {
      Collection<String> col = Arrays.asList("test", "hello", "test");
      ArrayList<String> result = Algorithms.findAll(col, "test");
      assertEquals(2, result.size());
      assertEquals(Arrays.asList("test", "test"), result);
    }

    @Test
    void shouldReturnEmptyArrayListWhenFindAllFindsNoMatches() {
      Collection<String> col = Arrays.asList("hello", "world");
      ArrayList<String> result = Algorithms.findAll(col, "test");
      assertTrue(result.isEmpty());
    }
  }

  @Nested
  class FindFirstTests {

    @Test
    void shouldReturnFirstMatchingObject() {
      Collection<String> col = Arrays.asList("first", "second", "first");
      String result = Algorithms.findFirst(col, "first");
      assertEquals("first", result);
    }

    @Test
    void shouldReturnNullWhenFindFirstFindsNoMatch() {
      Collection<String> col = Arrays.asList("first", "second");
      assertNull(Algorithms.findFirst(col, "third"));
    }
  }

  @Nested
  class FindIfTests {

    @Test
    void shouldReturnArrayListOfAllElementsMatchingPredicate() {
      Collection<Integer> col = Arrays.asList(1, 10, 2, 20);
      Predicate<Integer> isGreaterWithTen = n -> n >= 10;
      ArrayList<Integer> result = Algorithms.findIf(col, isGreaterWithTen);
      assertEquals(2, result.size());
      assertEquals(Arrays.asList(10, 20), result);
    }
  }

  @Nested
  class NoneMatchTests {

    @Test
    void shouldReturnTrueWhenNoElementsMatchPredicate() {
      Collection<Integer> col = Arrays.asList(1, 3, 5);
      Predicate<Integer> isEven = n -> n % 2 == 0;
      assertTrue(Algorithms.noneMatch(col, isEven));
    }

    @Test
    void shouldReturnFalseWhenAtLeastOneElementMatchesPredicate() {
      Collection<Integer> col = Arrays.asList(1, 2, 3);
      Predicate<Integer> isEven = n -> n % 2 == 0;
      assertFalse(Algorithms.noneMatch(col, isEven));
    }
  }

  @Nested
  class ReduceTests {

    @Test
    void shouldAccumulateValuesCorrectlyFromInitialValue() {
      Collection<Integer> col = Arrays.asList(1, 2, 3, 4);
      BinaryOperator<Integer> sum = Integer::sum;
      Integer result = Algorithms.reduce(col, 0, sum);
      assertEquals(10, result);
    }

    @Test
    void shouldReturnInitialValueWhenReduceIsCalledOnEmptyCollection() {
      Collection<Integer> col = Collections.emptyList();
      BinaryOperator<Integer> sum = Integer::sum;
      Integer result = Algorithms.reduce(col, 42, sum);
      assertEquals(42, result);
    }

    @Test
    void shouldThrowNullPointerExceptionWhenReduceReceivesNullOperator() {
      Collection<Integer> col = Arrays.asList(1, 2);
      assertThrows(NullPointerException.class, () -> Algorithms.reduce(col, 0, null));
    }
  }
}
