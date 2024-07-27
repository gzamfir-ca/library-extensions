package libext;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class WritersTest {

  @Test
  void newPrintWriter() {
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintWriter writer = Writers.newPrintWriter(outputStream);
    assertNotNull(writer);
    final var expected = Arrays.asList("1", "2", "3", "4", "5", "6", "7",
        "8", "9");
    expected.forEach(writer::println);
    final var output = Arrays.asList(outputStream.toString().split("\n"));
    assertIterableEquals(expected, output);
    writer.close();
  }
}
