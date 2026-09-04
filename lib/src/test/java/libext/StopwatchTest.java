package libext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
      assertTrue(elapsed >= 0, "Elapsed time should be positive");
      assertTrue(elapsed >= (timeStop - timeStart), "Elapsed time should match execution window");
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
      assertTrue(elapsedMillis >= 50, "Elapsed time should reflect the sleep duration");
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
      assertTrue(elapsedNanos > 0, "Nano-second tracking should catch sub-millisecond gaps");
    }
  }

  @Nested
  class GetElapsedMillisTest {

    @Test
    void shouldReturnConvenienceMillisDirectly() throws InterruptedException {
      Stopwatch stopwatch = Stopwatch.start();
      TimeUnit.MILLISECONDS.sleep(30);
      long elapsedMillisDirect = stopwatch.getElapsedMillis();
      long elapsedMillisFromUnit = stopwatch.getElapsedTime(TimeUnit.MILLISECONDS);
      assertTrue(elapsedMillisDirect >= 30, "Convenience method should reflect elapsed time");
      assertTrue(Math.abs(elapsedMillisDirect - elapsedMillisFromUnit) <= 1,
          "Methods should be synchronized");
    }
  }
}
