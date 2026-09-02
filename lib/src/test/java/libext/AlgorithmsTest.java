package libext;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
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
  class FilterTests {

    @Test
    void shouldFilterElementsCorrectlyWhenSizesMatch() {
      List<Integer> src = Arrays.asList(1, 2, 3, 4);
      List<Integer> dest = Arrays.asList(0, 0, 0, 0);
      Predicate<Integer> isEven = x -> x % 2 == 0;
      Algorithms.filter(dest, src, isEven);
      assertEquals(Arrays.asList(0, 2, 0, 4), dest);
    }

    @Test
    void shouldFilterElementsCorrectlyWhenDestIsLargerThanSrc() {
      List<Integer> src = Arrays.asList(1, 2, 3);
      List<Integer> dest = Arrays.asList(9, 9, 9, 9, 9);
      Predicate<Integer> isOdd = x -> x % 2 != 0;
      Algorithms.filter(dest, src, isOdd);
      assertEquals(Arrays.asList(1, 9, 3, 9, 9), dest);
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenSrcIsLargerThanDest() {
      List<Integer> src = Arrays.asList(1, 2, 3);
      List<Integer> dest = List.of(0);
      Predicate<Integer> pred = x -> true;
      Exception exception = assertThrows(IndexOutOfBoundsException.class, () ->
          Algorithms.filter(dest, src, pred)
      );
      assertEquals("src size is greater than dest size", exception.getMessage());
    }

    @Test
    void shouldHandleSequentialAccessListsAboveThresholdCorrectly() {
      int LARGE_SIZE = 50;
      List<Integer> src = new LinkedList<>(Collections.nCopies(LARGE_SIZE, 10));
      List<Integer> dest = new LinkedList<>(Collections.nCopies(LARGE_SIZE, 0));
      Predicate<Integer> matchAll = x -> true;
      Algorithms.filter(dest, src, matchAll);
      assertAll(
          dest.stream().map(element -> () -> assertEquals(10, element))
      );
    }

    @Test
    void shouldHandleRandomAccessListsAboveThresholdCorrectly() {
      int LARGE_SIZE = 50;
      List<Integer> src = new ArrayList<>(Collections.nCopies(LARGE_SIZE, 10));
      List<Integer> dest = new ArrayList<>(Collections.nCopies(LARGE_SIZE, 0));
      Predicate<Integer> matchAll = x -> true;
      Algorithms.filter(dest, src, matchAll);
      assertAll(
          dest.stream().map(element -> () -> assertEquals(10, element))
      );
    }

    @Test
    void shouldHandleSequentialAccessListsBelowThresholdCorrectly() {
      List<Integer> src = new LinkedList<>(Arrays.asList(5, 10));
      List<Integer> dest = new LinkedList<>(Arrays.asList(1, 2, 3));
      Predicate<Integer> isGreaterWithThreshold = x -> x > 7;
      Algorithms.filter(dest, src, isGreaterWithThreshold);
      assertEquals(Arrays.asList(1, 10, 3), dest);
    }

    @Test
    void shouldThrowNullPointerExceptionWhenDestIsNull() {
      List<Integer> src = Arrays.asList(1, 2);
      Predicate<Integer> pred = x -> true;
      Exception exception = assertThrows(NullPointerException.class, () ->
          Algorithms.filter(null, src, pred)
      );
      assertEquals("no valid destination provided", exception.getMessage());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenSrcIsNull() {
      List<Integer> dest = Arrays.asList(1, 2);
      Predicate<Integer> pred = x -> true;
      Exception exception = assertThrows(NullPointerException.class, () ->
          Algorithms.filter(dest, null, pred)
      );
      assertEquals("no valid source provided", exception.getMessage());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenPredicateIsNull() {
      List<Integer> src = Arrays.asList(1, 2);
      List<Integer> dest = Arrays.asList(0, 0);
      Exception exception = assertThrows(NullPointerException.class, () ->
          Algorithms.filter(dest, src, null)
      );
      assertEquals("no valid predicate provided", exception.getMessage());
    }
  }

  @Nested
  class FindAllTests {

    @Test
    void shouldReturnArrayListContainingAllMatchingObjects() {
      Collection<String> col = Arrays.asList("test", "hello", "test");
      List<String> result = Algorithms.findAll(col, "test");
      assertEquals(2, result.size());
      assertEquals(Arrays.asList("test", "test"), result);
    }

    @Test
    void shouldReturnEmptyArrayListWhenFindAllFindsNoMatches() {
      Collection<String> col = Arrays.asList("hello", "world");
      List<String> result = Algorithms.findAll(col, "test");
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
      List<Integer> result = Algorithms.findIf(col, isGreaterWithTen);
      assertEquals(2, result.size());
      assertEquals(Arrays.asList(10, 20), result);
    }
  }

  @Nested
  class MapTests {

    @Test
    void shouldMapElementsCorrectlyWhenSizesMatch() {
      List<Integer> src = Arrays.asList(1, 2, 3);
      List<String> dest = Arrays.asList("", "", "");
      Function<Integer, String> mapper = String::valueOf;
      Algorithms.map(dest, src, mapper);
      assertEquals(Arrays.asList("1", "2", "3"), dest);
    }

    @Test
    void shouldMapElementsCorrectlyWhenDestIsLargerThanSrc() {
      List<Integer> src = Arrays.asList(1, 2);
      List<String> dest = Arrays.asList("A", "B", "C", "D");
      Function<Integer, String> mapper = String::valueOf;
      Algorithms.map(dest, src, mapper);
      assertEquals(Arrays.asList("1", "2", "C", "D"), dest);
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenSrcIsLargerThanDest() {
      List<Integer> src = Arrays.asList(1, 2, 3);
      List<String> dest = List.of("");
      Function<Integer, String> mapper = String::valueOf;
      Exception exception = assertThrows(IndexOutOfBoundsException.class, () ->
          Algorithms.map(dest, src, mapper)
      );
      assertEquals("src size is greater than dest size", exception.getMessage());
    }

    @Test
    void shouldHandleSequentialAccessListsAboveThresholdCorrectly() {
      int LARGE_SIZE = 50;
      List<Integer> src = new LinkedList<>(Collections.nCopies(LARGE_SIZE, 10));
      List<Integer> dest = new LinkedList<>(Collections.nCopies(LARGE_SIZE, 0));
      Function<Integer, Integer> identity = x -> x;
      Algorithms.map(dest, src, identity);
      assertAll(
          dest.stream().map(element -> () -> assertEquals(10, element))
      );
    }

    @Test
    void shouldHandleRandomAccessListsAboveThresholdCorrectly() {
      int LARGE_SIZE = 50;
      List<Integer> src = new ArrayList<>(Collections.nCopies(LARGE_SIZE, 10));
      List<Integer> dest = new ArrayList<>(Collections.nCopies(LARGE_SIZE, 0));
      Function<Integer, Integer> identity = x -> x;
      Algorithms.map(dest, src, identity);
      assertAll(
          dest.stream().map(element -> () -> assertEquals(10, element))
      );
    }

    @Test
    void shouldHandleSequentialAccessListsBelowThresholdCorrectly() {
      List<Integer> src = new LinkedList<>(Arrays.asList(10, 20));
      List<Integer> dest = new LinkedList<>(Arrays.asList(0, 0, 0));
      Function<Integer, Integer> mapper = x -> x * 2;
      Algorithms.map(dest, src, mapper);
      assertEquals(Arrays.asList(20, 40, 0), dest);
    }

    @Test
    void shouldThrowNullPointerExceptionWhenDestIsNull() {
      List<Integer> src = Arrays.asList(1, 2);
      Function<Integer, Integer> mapper = x -> x;

      Exception exception = assertThrows(NullPointerException.class, () ->
          Algorithms.map(null, src, mapper)
      );
      assertEquals("no valid destination provided", exception.getMessage());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenSrcIsNull() {
      List<Integer> dest = Arrays.asList(1, 2);
      Function<Integer, Integer> mapper = x -> x;

      Exception exception = assertThrows(NullPointerException.class, () ->
          Algorithms.map(dest, null, mapper)
      );
      assertEquals("no valid source provided", exception.getMessage());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenMapperIsNull() {
      List<Integer> src = Arrays.asList(1, 2);
      List<Integer> dest = Arrays.asList(0, 0);

      Exception exception = assertThrows(NullPointerException.class, () ->
          Algorithms.map(dest, src, null)
      );
      assertEquals("no valid mapper provided", exception.getMessage());
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
