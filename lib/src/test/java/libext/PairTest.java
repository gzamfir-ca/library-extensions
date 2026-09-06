package libext;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PairTest {

  @Nested
  class OfTest {

    @Test
    void shouldCreatePairWithCorrectValues() {
      Pair<String, Integer> pair = Pair.of("apple", 42);
      assertEquals("apple", pair.first());
      assertEquals(42, pair.second());
    }
  }

  @Nested
  class MapTest {

    @Test
    void shouldTransformSecondValueAndKeepFirst() {
      Pair<String, Integer> initial = Pair.of("id-100", 5);
      Pair<String, String> result = initial.map(val -> "Count: " + val);
      assertEquals("id-100", result.first());
      assertEquals("Count: 5", result.second());
    }
  }

  @Nested
  class FlatMapTest {

    @Test
    void shouldFlattenAndReturnNewPair() {
      Pair<String, Integer> initial = Pair.of("session-xyz", 10);
      Pair<String, String> result = initial.flatMap(
          val -> Pair.of("session-xyz", "Value is " + val));
      assertEquals("session-xyz", result.first());
      assertEquals("Value is 10", result.second());
    }
  }
}
