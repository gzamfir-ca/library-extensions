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
import java.util.Objects;

public final class Readers {

  private static int pos = 0;

  private Readers() {
    throw new AssertionError("no instances");
  }

  private static String readLine(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    String line = null;
    try {
      line = reader.readLine();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return line;
  }

  private static String readToken(int delim, String line) {
    Objects.requireNonNull(line, "no valid line provided");
    while (pos < line.length() && line.charAt(pos) == delim) {
      pos++;
    }
    int end = -1;
    if ((end = line.indexOf(delim, pos)) >= 0) {
      String token = line.substring(pos, end);
      pos = end + 1;
      while (pos < line.length() && line.charAt(pos) == delim) {
        pos++;
      }
      return token;
    }
    if (pos < line.length()) {
      String token = line.substring(pos);
      pos = line.length();
      return token;
    }
    pos = 0;
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

  public static synchronized void addAll(Collection<String> col, BufferedReader reader) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(reader, "no valid reader provided");
    pos = 0;
    String line, token = null;
    final int delim = DELIM;
    while ((line = readLine(reader)) != null) {
      while ((token = readToken(delim, line)) != null) {
        col.add(token);
      }
    }
  }
}
