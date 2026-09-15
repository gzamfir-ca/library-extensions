package libext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

class WritersTest {

  @BeforeEach
  void setUp() {
    Writers.updateConfig(StandardCharsets.UTF_8, true);
  }

  @AfterEach
  void tearDown() {
    Writers.updateConfig(StandardCharsets.UTF_8, true);
  }

  @Test
  void shouldWriteTokens() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try (PrintWriter writer = Writers.newPrintWriter(outputStream)) {
      assertNotNull(writer);
      writer.print("hello world");
      writer.flush();
    }
    assertEquals("hello world", outputStream.toString(StandardCharsets.UTF_8));
  }

  @Test
  void shouldWriteTokensToFile() {
    try {
      Path tempFile = Files.createTempFile("writersTest", ".txt");
      try {
        try (PrintWriter writer = Writers.newPrintWriter(tempFile)) {
          assertNotNull(writer);
          writer.print("file content");
        }
        String fileContents = Files.readString(tempFile, StandardCharsets.UTF_8);
        assertEquals("file content", fileContents);
      } finally {
        Files.deleteIfExists(tempFile);
      }
    } catch (IOException e) {
      fail("Test failed due to an IOException: " + e.getMessage());
    }
  }

  @Test
  void shouldAppendTokensToFile() {
    try {
      Path tempFile = Files.createTempFile("writersAppendTest", ".txt");
      try {
        Files.writeString(tempFile, "initial content", StandardCharsets.UTF_8);
        try (PrintWriter writer = Writers.newPrintWriter(tempFile, StandardOpenOption.APPEND)) {
          assertNotNull(writer);
          writer.print(" + appended content");
        }
        String fileContents = Files.readString(tempFile, StandardCharsets.UTF_8);
        assertEquals("initial content + appended content", fileContents);
      } finally {
        Files.deleteIfExists(tempFile);
      }
    } catch (IOException e) {
      fail("Test failed due to an IOException: " + e.getMessage());
    }
  }

  @Test
  void shouldCreateAndAppendTokensToFile() {
    try {
      Path tempDir = Files.createTempDirectory("writersCreateAppendTest");
      Path nonExistentFile = tempDir.resolve("new_append_file.txt");
      try {
        try (PrintWriter writer = Writers.newPrintWriter(nonExistentFile, StandardOpenOption.CREATE,
            StandardOpenOption.APPEND)) {
          assertNotNull(writer);
          writer.print("first line");
        }
        try (PrintWriter writer = Writers.newPrintWriter(nonExistentFile, StandardOpenOption.CREATE,
            StandardOpenOption.APPEND)) {
          writer.print(" second line");
        }
        String fileContents = Files.readString(nonExistentFile, StandardCharsets.UTF_8);
        assertEquals("first line second line", fileContents);
      } finally {
        Files.deleteIfExists(nonExistentFile);
        Files.deleteIfExists(tempDir);
      }
    } catch (IOException e) {
      fail("Test failed due to an IOException: " + e.getMessage());
    }
  }

  @Test
  void shouldFollowConfigurationChanges() {
    Writers.updateConfig(StandardCharsets.ISO_8859_1, true);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try (PrintWriter writer = Writers.newPrintWriter(outputStream)) {
      writer.println("testing auto-flush");
    }
    String result = outputStream.toString(StandardCharsets.ISO_8859_1);
    assertTrue(result.contains("testing auto-flush"));
  }

  @Test
  void shouldThrowExceptionOnNullArguments() {
    Path validPath = Path.of("dummy.txt");
    NullPointerException pathException = assertThrows(NullPointerException.class,
        () -> {
          try (PrintWriter writer = Writers.newPrintWriter(null, StandardOpenOption.WRITE)) {
            writer.flush();
          }
        });
    assertEquals("no valid path provided", pathException.getMessage());

    NullPointerException optionsException = assertThrows(NullPointerException.class,
        () -> {
          try (PrintWriter writer = Writers.newPrintWriter(validPath, (OpenOption[]) null)) {
            writer.flush();
          }
        });
    assertEquals("no valid options provided", optionsException.getMessage());
  }
}
