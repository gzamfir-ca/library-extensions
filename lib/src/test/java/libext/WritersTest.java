package libext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class WritersTest {

  @BeforeEach
  void setUp() {
    Writers.CHARSET = StandardCharsets.UTF_8;
    Writers.AUTO_FLUSH = true;
  }

  @Test
  void shouldWriteCommonTokensCorrectly() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintWriter writer = Writers.newPrintWriter(outputStream);
    assertNotNull(writer);
    writer.print("hello world");
    writer.flush();
    assertEquals("hello world", outputStream.toString(StandardCharsets.UTF_8));
  }

  @Test
  void shouldWriteCommonTokensToFileCorrectly() throws Exception {
    Path tempFile = Files.createTempFile("writersTest", ".txt");
    try {
      PrintWriter writer = Writers.newPrintWriter(tempFile);
      assertNotNull(writer);
      writer.print("file content");
      writer.close();
      String fileContents = Files.readString(tempFile, StandardCharsets.UTF_8);
      assertEquals("file content", fileContents);
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  @Test
  void shouldObserveCustomConfigurationChanges() {
    Writers.CHARSET = StandardCharsets.ISO_8859_1;
    Writers.AUTO_FLUSH = true;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintWriter writer = Writers.newPrintWriter(outputStream);
    writer.println("testing auto-flush");
    String result = outputStream.toString(StandardCharsets.ISO_8859_1);
    assertTrue(result.contains("testing auto-flush"));
  }

  @Test
  void shouldPreserveStateUnderConcurrentAccess() throws InterruptedException {
    int threadCount = 10;
    AtomicBoolean safetyFailureOccurred;
    try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
      CountDownLatch startLatch = new CountDownLatch(1);
      CountDownLatch finishLatch = new CountDownLatch(threadCount);
      safetyFailureOccurred = new AtomicBoolean(false);
      for (int i = 0; i < threadCount; i++) {
        executor.submit(() -> {
          try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            startLatch.await();
            final java.nio.charset.Charset threadEntryCharset = Writers.CHARSET;
            PrintWriter writer = Writers.newPrintWriter(outputStream);
            assertNotNull(writer);
            writer.print("check");
            writer.flush();
            if (!outputStream.toString(threadEntryCharset).equals("check")) {
              safetyFailureOccurred.set(true);
            }
          } catch (Exception e) {
            safetyFailureOccurred.set(true);
          } finally {
            finishLatch.countDown();
          }
        });
      }
      startLatch.countDown();
      for (int i = 0; i < 50; i++) {
        Writers.CHARSET = (i % 2 == 0) ? StandardCharsets.US_ASCII : StandardCharsets.UTF_16;
        Writers.AUTO_FLUSH = (i % 2 == 0);
      }
      finishLatch.await();
      executor.shutdown();
    }
    assertFalse(safetyFailureOccurred.get(),
        "Configuration race condition detected! State snapshot was corrupted.");
  }
}
