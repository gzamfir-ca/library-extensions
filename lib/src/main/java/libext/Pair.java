package libext;

import java.util.Objects;
import java.util.function.Function;

public record Pair<U, V>(U first, V second) {

  public static <U, V> Pair<U, V> of(U first, V second) {
    return new Pair<>(first, second);
  }

  public <R> Pair<U, R> map(Function<V, R> mapper) {
    Objects.requireNonNull(mapper, "no valid mapper provided");
    return new Pair<>(first(), mapper.apply(second()));
  }

  public <R> Pair<U, R> flatMap(Function<V, Pair<U, R>> mapper) {
    Objects.requireNonNull(mapper, "no valid mapper provided");
    Pair<U, R> result = Objects.requireNonNull(mapper.apply(second), "invalid mapper result");
    return new Pair<>(first(), result.second());
  }
}
