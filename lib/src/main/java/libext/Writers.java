package libext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Writers {

  private Writers() {
  }

  public static Charset CHARSET = StandardCharsets.UTF_8;
  public static boolean AUTO_FLUSH = true;

  public static PrintWriter newPrintWriter(OutputStream output) {
    Objects.requireNonNull(output, "no valid output provided");
    CharsetEncoder encoder = CHARSET.newEncoder();
    Writer writer = new OutputStreamWriter(output, encoder);
    BufferedWriter bufferedWriter = new BufferedWriter(writer);
    return new PrintWriter(bufferedWriter, AUTO_FLUSH);
  }

  public static PrintWriter newPrintWriter(Path path) {
    Objects.requireNonNull(path, "no valid path provided");
    OutputStream outputStream = null;
    try {
      outputStream = Files.newOutputStream(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return newPrintWriter(outputStream);
  }
}