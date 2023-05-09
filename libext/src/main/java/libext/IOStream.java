package libext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IOStream {

  public static Charset charset = StandardCharsets.UTF_8;

  public static boolean autoFlush = false;

  public static int delim = ' ';
  public static int radix = 10;

  private static int pos = 0;

  private IOStream() {
  }

  public static BufferedReader newBufferedReader(InputStream input) {
    Objects.requireNonNull(input, "no valid input provided");
    CharsetDecoder decoder = charset.newDecoder();
    Reader reader = new InputStreamReader(input, decoder);
    return new BufferedReader(reader);
  }

  public static PrintWriter newPrintWriter(OutputStream output) {
    Objects.requireNonNull(output, "no valid output provided");
    CharsetEncoder encoder = charset.newEncoder();
    Writer writer = new OutputStreamWriter(output, encoder);
    BufferedWriter bufferedWriter = new BufferedWriter(writer);
    return new PrintWriter(bufferedWriter, autoFlush);
  }

  public static BufferedReader newBufferedReader(Path path) {
    Objects.requireNonNull(path, "no valid path provided");
    try (InputStream inputStream = Files.newInputStream(path)) {
      return newBufferedReader(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static PrintWriter newPrintWriter(Path path) {
    Objects.requireNonNull(path, "no valid path provided");
    try (OutputStream outputStream = Files.newOutputStream(path)) {
      return newPrintWriter(outputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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

  public static Double readDouble(String line) {
    Objects.requireNonNull(line, "no valid line provided");
    String token = readToken(line);
    if (token != null) {
      return Double.valueOf(token);
    }
    return null;
  }

  public static Integer readInteger(String line) {
    Objects.requireNonNull(line, "no valid line provided");
    String token = readToken(line);
    if (token != null) {
      return Integer.valueOf(token, radix);
    }
    return null;
  }

  public static Long readLong(String line) {
    Objects.requireNonNull(line, "no valid line provided");
    String token = readToken(line);
    if (token != null) {
      return Long.valueOf(token, radix);
    }
    return null;
  }

  public static List<String> readAllLines(InputStream input) {
    Objects.requireNonNull(input, "no valid input provided");
    String line;
    List<String> result = new ArrayList<>();
    BufferedReader bufferedReader = newBufferedReader(input);
    while ((line = readLine(bufferedReader)) != null) {
      result.add(line);
    }
    return List.copyOf(result);
  }

  public static List<String> readAllTokens(InputStream input) {
    Objects.requireNonNull(input, "no valid input provided");
    String line;
    String token;
    List<String> result = new ArrayList<>();
    BufferedReader bufferedReader = newBufferedReader(input);
    while ((line = readLine(bufferedReader)) != null) {
      while ((token = readToken(line)) != null) {
        result.add(token);
      }
    }
    return List.copyOf(result);
  }

  public static List<Double> readAllDoubles(InputStream input) {
    Objects.requireNonNull(input, "no valid input provided");
    String line;
    Double token;
    List<Double> result = new ArrayList<>();
    BufferedReader bufferedReader = newBufferedReader(input);
    while ((line = readLine(bufferedReader)) != null) {
      while ((token = readDouble(line)) != null) {
        result.add(token);
      }
    }
    return List.copyOf(result);
  }

  public static List<Integer> readAllIntegers(InputStream input) {
    Objects.requireNonNull(input, "no valid input provided");
    String line;
    Integer token;
    List<Integer> result = new ArrayList<>();
    BufferedReader bufferedReader = newBufferedReader(input);
    while ((line = readLine(bufferedReader)) != null) {
      while ((token = readInteger(line)) != null) {
        result.add(token);
      }
    }
    return List.copyOf(result);
  }

  public static List<Long> readAllLongs(InputStream input) {
    Objects.requireNonNull(input, "no valid input provided");
    String line;
    Long token;
    List<Long> result = new ArrayList<>();
    BufferedReader bufferedReader = newBufferedReader(input);
    while ((line = readLine(bufferedReader)) != null) {
      while ((token = readLong(line)) != null) {
        result.add(token);
      }
    }
    return List.copyOf(result);
  }

  public static List<String> readAllLines(Path path) {
    Objects.requireNonNull(path, "no valid path provided");
    try (InputStream inputStream = Files.newInputStream(path)) {
      return readAllLines(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<String> readAllTokens(Path path) {
    Objects.requireNonNull(path, "no valid path provided");
    try (InputStream inputStream = Files.newInputStream(path)) {
      return readAllTokens(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<Double> readAllDoubles(Path path) {
    Objects.requireNonNull(path, "no valid path provided");
    try (InputStream inputStream = Files.newInputStream(path)) {
      return readAllDoubles(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<Integer> readAllIntegers(Path path) {
    Objects.requireNonNull(path, "no valid path provided");
    try (InputStream inputStream = Files.newInputStream(path)) {
      return readAllIntegers(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<Long> readAllLongs(Path path) {
    Objects.requireNonNull(path, "no valid path provided");
    try (InputStream inputStream = Files.newInputStream(path)) {
      return readAllLongs(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
