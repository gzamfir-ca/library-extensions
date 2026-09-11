package libext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public final class Readers {

  private static final class Position {

    int val = 0;
  }

  private Readers() {
    throw new AssertionError("no instances");
  }

  private static String readLine(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    try {
      return reader.readLine();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static String readToken(int delim, String line, Position pos) {
    Objects.requireNonNull(line, "no valid line provided");
    while (pos.val < line.length() && line.charAt(pos.val) == delim) {
      pos.val++;
    }
    int end = line.indexOf(delim, pos.val);
    if (end >= 0) {
      String token = line.substring(pos.val, end);
      pos.val = end + 1;
      while (pos.val < line.length() && line.charAt(pos.val) == delim) {
        pos.val++;
      }
      return token;
    }
    if (pos.val < line.length()) {
      String token = line.substring(pos.val);
      pos.val = line.length();
      return token;
    }
    pos.val = 0;
    return null;
  }

  public static volatile Charset CHARSET = StandardCharsets.UTF_8;
  public static volatile int DELIM = ' ';

  public static BufferedReader newBufferedReader(InputStream input) {
    Objects.requireNonNull(input, "no valid input provided");
    final Charset charset = CHARSET;
    InputStreamReader reader = new InputStreamReader(input, charset);
    return new BufferedReader(reader);
  }

  public static BufferedReader newBufferedReader(Path path) {
    Objects.requireNonNull(path, "no valid path provided");
    final Charset charset = CHARSET;
    try {
      return Files.newBufferedReader(path, charset);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void addAll(Collection<String> col, BufferedReader reader) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(reader, "no valid reader provided");
    Position pos = new Position();
    String line, token;
    final int delim = DELIM;
    while ((line = readLine(reader)) != null) {
      while ((token = readToken(delim, line, pos)) != null) {
        col.add(token);
      }
    }
  }

  public static synchronized void addAll(Map<String, String> map, BufferedReader reader) {
    Objects.requireNonNull(map, "no valid map provided");
    Objects.requireNonNull(reader, "no valid reader provided");
    Position pos = new Position();
    String line, token;
    final int delim = DELIM;
    String key = null;
    while ((line = readLine(reader)) != null) {
      while ((token = readToken(delim, line, pos)) != null) {
        if (key == null) {
          key = token;
        } else {
          map.put(key, token);
          key = null;
        }
      }
    }
    if (key != null) {
      throw new IllegalStateException("odd number of tokens");
    }
  }
}
