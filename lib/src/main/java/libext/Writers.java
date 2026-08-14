package libext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class Writers {

  private Writers() {
    throw new AssertionError("no instances");
  }

  public static volatile Charset CHARSET = StandardCharsets.UTF_8;
  public static volatile boolean AUTO_FLUSH = true;

  public static PrintWriter newPrintWriter(OutputStream output) {
    Objects.requireNonNull(output, "no valid output provided");
    final Charset charset = CHARSET;
    final boolean autoFlush = AUTO_FLUSH;
    OutputStreamWriter writer = new OutputStreamWriter(output, charset);
    return new PrintWriter(new BufferedWriter(writer), autoFlush);
  }

  public static PrintWriter newPrintWriter(Path path) {
    Objects.requireNonNull(path, "no valid path provided");
    final Charset charset = CHARSET;
    final boolean autoFlush = AUTO_FLUSH;
    try {
      return new PrintWriter(Files.newBufferedWriter(path, charset), autoFlush);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
