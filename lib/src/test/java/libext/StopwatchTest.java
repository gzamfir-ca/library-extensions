package libext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;

class StopwatchTest {

  @Nested
  class StartTest {

    @SuppressWarnings("StatementWithEmptyBody")
    @Test
    void shouldInitializeWithCurrentTime() {
      Stopwatch stopwatch = Stopwatch.start();
      long timeStart = System.nanoTime();
      while (System.nanoTime() - timeStart < 1000) {
        // Busy wait
      }
      long timeStop = System.nanoTime();
      long elapsed = stopwatch.getElapsedTime(TimeUnit.NANOSECONDS);
      assertNotNull(stopwatch);
      assertTrue(elapsed >= 0);
      assertTrue(elapsed >= (timeStop - timeStart));
    }
  }

  @Nested
  class GetElapsedTimeTest {

    @Test
    void shouldReturnElapsedTimeInRequestedUnit() throws InterruptedException {
      Stopwatch stopwatch = Stopwatch.start();
      TimeUnit.MILLISECONDS.sleep(50);
      long elapsedNanos = stopwatch.getElapsedTime(TimeUnit.NANOSECONDS);
      long elapsedMillis = stopwatch.getElapsedTime(TimeUnit.MILLISECONDS);
      assertEquals(elapsedMillis,
          TimeUnit.MILLISECONDS.convert(elapsedNanos, TimeUnit.NANOSECONDS));
      assertTrue(elapsedMillis >= 50);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Test
    void shouldHandleSubMillisecondAccuracy() throws InterruptedException {
      Stopwatch stopwatch = Stopwatch.start();
      long startNanos = System.nanoTime();
      while (System.nanoTime() - startNanos < 1000) {
        // Busy wait
      }
      long elapsedNanos = stopwatch.getElapsedTime(TimeUnit.NANOSECONDS);
      assertTrue(elapsedNanos > 0);
    }
  }

  @Nested
  class GetElapsedMillisTest {

    @Test
    void shouldReturnElapsedTimeInMillis() throws InterruptedException {
      Stopwatch stopwatch = Stopwatch.start();
      TimeUnit.MILLISECONDS.sleep(30);
      double elapsedMillisDirect = stopwatch.getElapsedMillis();
      long elapsedMillisFromUnit = stopwatch.getElapsedTime(TimeUnit.MILLISECONDS);
      assertTrue(elapsedMillisDirect >= 30.0);

      double difference = elapsedMillisDirect - elapsedMillisFromUnit;
      assertTrue(difference >= 0.0 && difference < 1.0);
    }
  }

  @Nested
  class NullHandlingTest {

    @Test
    void shouldThrowExceptionOnNullUnit() {
      Stopwatch stopwatch = Stopwatch.start();
      NullPointerException exception = assertThrows(NullPointerException.class,
          () -> stopwatch.getElapsedTime(null));
      assertEquals("no valid unit provided", exception.getMessage());
    }
  }
}
