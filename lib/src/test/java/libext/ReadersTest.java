package libext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ReadersTest {

  @BeforeEach
  void setUp() {
    Readers.CHARSET = StandardCharsets.UTF_8;
    Readers.DELIM = ' ';
  }

  private BufferedReader createReader(String content) {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(Readers.CHARSET));
    return Readers.newBufferedReader(inputStream);
  }

  @Test
  void shouldReadCommonTokensCorrectly() {
    BufferedReader reader = createReader("hello world java");
    assertNotNull(reader);

    List<String> result = new ArrayList<>();
    Readers.addAll(result, reader);
    assertEquals(3, result.size());
    assertEquals("hello", result.get(0));
    assertEquals("world", result.get(1));
    assertEquals("java", result.get(2));
  }

  @Test
  void shouldIgnoreMultipleConsecutiveDelimitersAndTrailingSpaces() {
    BufferedReader reader = createReader("  leading   middle  trailing  ");
    assertNotNull(reader);

    List<String> result = new ArrayList<>();
    Readers.addAll(result, reader);
    assertEquals(3, result.size());
    assertEquals("leading", result.get(0));
    assertEquals("middle", result.get(1));
    assertEquals("trailing", result.get(2));
  }

  @Test
  void shouldReadCommonTokensAcrossMultipleLinesCorrectly() {
    BufferedReader reader = createReader("line1 word1\nline2 word2 word3\nline3");
    assertNotNull(reader);

    List<String> result = new ArrayList<>();
    Readers.addAll(result, reader);
    assertEquals(6, result.size());
    assertEquals("line1", result.get(0));
    assertEquals("word3", result.get(4));
    assertEquals("line3", result.get(5));
  }

  @Test
  void shouldObserveCustomConfigurationChanges() {
    Readers.DELIM = ',';
    BufferedReader reader = createReader("comma,separated,values,,next");
    assertNotNull(reader);

    List<String> result = new ArrayList<>();
    Readers.addAll(result, reader);
    assertEquals(4, result.size());
    assertEquals("comma", result.get(0));
    assertEquals("separated", result.get(1));
    assertEquals("values", result.get(2));
    assertEquals("next", result.get(3));
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
            BufferedReader reader = createReader("concurrent processing token test");
            List<String> result = new ArrayList<>();
            startLatch.await();
            Readers.addAll(result, reader);
            if (result.size() != 4 || !result.get(0).equals("concurrent") ||
                !result.get(3).equals("test")) {
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
      finishLatch.await();
      executor.shutdown();
    }
    assertFalse(safetyFailureOccurred.get(),
        "Thread safety race condition detected! State corruption occurred.");
  }
}
