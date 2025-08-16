package libext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public class Readers {

  private static int pos = 0;

  private Readers() {
  }

  public static Charset CHARSET = StandardCharsets.UTF_8;
  public static int DELIM = ' ';

  public static BufferedReader newBufferedReader(InputStream input) {
    Objects.requireNonNull(input, "no valid input provided");
    CharsetDecoder decoder = CHARSET.newDecoder();
    Reader reader = new InputStreamReader(input, decoder);
    return new BufferedReader(reader);
  }

  public static BufferedReader newBufferedReader(Path path) {
    Objects.requireNonNull(path, "no valid path provided");
    InputStream inputStream = null;
    try {
      inputStream = Files.newInputStream(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return newBufferedReader(inputStream);
  }

  public static String readLine(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    String line = null;
    try {
      line = reader.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return line;
  }

  public static String readToken(String line) {
    Objects.requireNonNull(line, "no valid line provided");
    while (pos < line.length() && line.charAt(pos) == DELIM) {
      pos++;
    }
    int end = -1;
    if ((end = line.indexOf(DELIM, pos)) >= 0) {
      String token = line.substring(pos, end);
      pos = end + 1;
      while (pos < line.length() && line.charAt(pos) == DELIM) {
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

  public static boolean addAll(Collection<String> col, BufferedReader reader) {
    Objects.requireNonNull(col, "no valid collection provided");
    Objects.requireNonNull(reader, "no valid reader provided");
    String line, token = null;
    boolean success = false;
    while ((line = readLine(reader)) != null) {
      while ((token = readToken(line)) != null) {
        success |= col.add(token);
      }
    }
    return success;
  }
}
