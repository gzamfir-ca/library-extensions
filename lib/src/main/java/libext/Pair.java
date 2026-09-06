package libext;

import java.util.function.Function;

public record Pair<U, V>(U first, V second) {

  public static <U, V> Pair<U, V> of(U first, V second) {
    return new Pair<>(first, second);
  }

  public <R> Pair<U, R> map(Function<V, R> mapper) {
    return new Pair<>(first, mapper.apply(second));
  }

  public <R> Pair<U, R> flatMap(Function<V, Pair<U, R>> mapper) {
    return mapper.apply(second);
  }
}
