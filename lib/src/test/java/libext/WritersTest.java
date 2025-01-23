package libext;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class WritersTest {

  @Test
  void newPrintWriterToStream() {
    OutputStream outputStream = new ByteArrayOutputStream();
    try(PrintWriter writer = Writers.newPrintWriter(outputStream)) {
      assertNotNull(writer);
      final var expected = Arrays.asList("1", "2", "3", "4", "5", "6", "7",
          "8", "9");
      expected.forEach(writer::println);
      final var output = Arrays.asList(outputStream.toString().split("\n"));
      assertIterableEquals(expected, output);
    }
  }

  @Test
  void newPrintWriterToPath() throws IOException {
    Path path = Files.createTempFile(
          Path.of("src/test/resources"), "writersTest", ".txt");
    assertNotNull(path);
    path.toFile().deleteOnExit();
    try(PrintWriter writer = Writers.newPrintWriter(path)) {
      assertNotNull(writer);
      final var expected = Arrays.asList("1", "2", "3", "4", "5", "6", "7",
          "8", "9");
      expected.forEach(writer::println);
      final var output = Arrays.asList(Files.readString(path).split("\n"));
      assertIterableEquals(expected, output);
    }
  }
}