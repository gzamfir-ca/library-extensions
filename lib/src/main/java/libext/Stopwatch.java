package libext;

import java.util.concurrent.TimeUnit;

public final class Stopwatch {

  private final long startTime;

  private Stopwatch() {
    this.startTime = System.nanoTime();
  }

  public static Stopwatch start() {
    return new Stopwatch();
  }

  public long getElapsedTime(TimeUnit unit) {
    long elapsedTime = System.nanoTime() - startTime;
    return unit.convert(elapsedTime, TimeUnit.NANOSECONDS);
  }

  public double getElapsedMillis() {
    long elapsedTime = System.nanoTime() - startTime;
    return elapsedTime / 1_000_000.0;
  }
}
