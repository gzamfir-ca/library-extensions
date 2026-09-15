package libext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Objects;

public final class Writers {

  private Writers() {
    throw new AssertionError("no instances");
  }

  private static volatile Config config = new Config();

  public record Config(Charset charset, boolean autoFlush) {

    public Config() {
      this(StandardCharsets.UTF_8, true);
    }

    public Config(boolean autoFlush) {
      this(StandardCharsets.UTF_8, autoFlush);
    }
  }

  public static Config config() {
    return config;
  }

  public static void updateConfig(boolean autoFlush) {
    config = new Config(autoFlush);
  }

  public static void updateConfig(Charset charset, boolean autoFlush) {
    config = new Config(charset, autoFlush);
  }

  public static PrintWriter newPrintWriter(OutputStream output) {
    Objects.requireNonNull(output, "no valid output provided");
    final Config currentConfig = config;
    final Charset charset = currentConfig.charset();
    final boolean autoFlush = currentConfig.autoFlush();
    OutputStreamWriter writer = new OutputStreamWriter(output, charset);
    return new PrintWriter(new BufferedWriter(writer), autoFlush);
  }

  public static PrintWriter newPrintWriter(Path path, OpenOption... options) {
    Objects.requireNonNull(path, "no valid path provided");
    Objects.requireNonNull(options, "no valid options provided");
    final Config currentConfig = config;
    final Charset charset = currentConfig.charset();
    final boolean autoFlush = currentConfig.autoFlush();
    try {
      return new PrintWriter(Files.newBufferedWriter(path, charset, options), autoFlush);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
