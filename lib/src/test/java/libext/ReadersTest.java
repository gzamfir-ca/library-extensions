package libext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class ReadersTest {

  @BeforeEach
  void setUp() {
    Readers.updateConfig(StandardCharsets.UTF_8, ' ');
  }

  @AfterEach
  void tearDown() {
    Readers.updateConfig(StandardCharsets.UTF_8, ' ');
  }

  private BufferedReader createReader(String content) {
    Charset currentCharset = Readers.config().charset();
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(content.getBytes(currentCharset));
    return Readers.newBufferedReader(inputStream);
  }

  @Nested
  class CollectionOverloadTests {

    @Test
    void shouldReadElements() {
      try (BufferedReader reader = createReader("hello world java")) {
        assertNotNull(reader);

        List<String> result = new ArrayList<>();
        Readers.addAll(result, reader);
        assertEquals(3, result.size());
        assertEquals("hello", result.get(0));
        assertEquals("world", result.get(1));
        assertEquals("java", result.get(2));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldIgnoreConsecutiveDelimitersAndTrailingSpaces() {
      try (BufferedReader reader = createReader(" leading middle trailing ")) {
        assertNotNull(reader);

        List<String> result = new ArrayList<>();
        Readers.addAll(result, reader);
        assertEquals(3, result.size());
        assertEquals("leading", result.get(0));
        assertEquals("middle", result.get(1));
        assertEquals("trailing", result.get(2));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldReadElementsAcrossMultipleLines() {
      try (BufferedReader reader = createReader("line1 word1\nline2 word2 word3\nline3")) {
        assertNotNull(reader);

        List<String> result = new ArrayList<>();
        Readers.addAll(result, reader);
        assertEquals(6, result.size());
        assertEquals("line1", result.get(0));
        assertEquals("word3", result.get(4));
        assertEquals("line3", result.get(5));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldFollowConfigurationChanges() {
      Readers.updateConfig(',');
      try (BufferedReader reader = createReader("comma,separated,values,,next")) {
        assertNotNull(reader);

        List<String> result = new ArrayList<>();
        Readers.addAll(result, reader);
        assertEquals(4, result.size());
        assertEquals("comma", result.get(0));
        assertEquals("separated", result.get(1));
        assertEquals("values", result.get(2));
        assertEquals("next", result.get(3));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldThrowExceptionOnNullArguments() {
      try (BufferedReader reader = createReader("token")) {
        assertThrows(NullPointerException.class,
            () -> Readers.addAll((Collection<String>) null, reader));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
      Map<String, String> map = new HashMap<>();
      assertThrows(NullPointerException.class,
          () -> Readers.addAll(map, null));
    }
  }

  @Nested
  class MapOverloadTests {

    @Test
    void shouldReadEntries() {
      try (BufferedReader reader = createReader("key1 value1 key2 value2")) {
        Map<String, String> map = new HashMap<>();
        Readers.addAll(map, reader);
        assertEquals(2, map.size());
        assertEquals("value1", map.get("key1"));
        assertEquals("value2", map.get("key2"));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldThrowExceptionOnOddNumberOfTokens() {
      try (BufferedReader reader = createReader("key1 value1 key2")) {
        Map<String, String> map = new HashMap<>();
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> Readers.addAll(map, reader));
        assertEquals("odd number of tokens", exception.getMessage());
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldThrowExceptionOnNullArguments() {
      try (BufferedReader reader = createReader("key value")) {
        assertThrows(NullPointerException.class,
            () -> Readers.addAll((Map<String, String>) null, reader));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
      Map<String, String> map = new HashMap<>();
      assertThrows(NullPointerException.class, () -> Readers.addAll(map, null));
    }
  }

  @Nested
  class MultisetOverloadTests {

    @Test
    void shouldReadKeys() {
      try (BufferedReader reader = createReader("hello world java hello")) {
        assertNotNull(reader);

        Multiset<String> result = Multiset.newMultiset();
        Readers.addAll(result, reader);
        assertEquals(4, result.flattenedKeys().size());
        assertEquals(2, result.keyCount("hello"));
        assertEquals(1, result.keyCount("world"));
        assertEquals(1, result.keyCount("java"));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldIgnoreConsecutiveDelimitersAndTrailingSpaces() {
      try (BufferedReader reader = createReader(" leading middle trailing leading ")) {
        assertNotNull(reader);

        Multiset<String> result = Multiset.newMultiset();
        Readers.addAll(result, reader);
        assertEquals(4, result.flattenedKeys().size());
        assertEquals(2, result.keyCount("leading"));
        assertEquals(1, result.keyCount("middle"));
        assertEquals(1, result.keyCount("trailing"));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldReadKeysAcrossMultipleLines() {
      try (BufferedReader reader = createReader("line1 word1\nline2 word1 word3\nline1")) {
        assertNotNull(reader);

        Multiset<String> result = Multiset.newMultiset();
        Readers.addAll(result, reader);
        assertEquals(6, result.flattenedKeys().size());
        assertEquals(2, result.keyCount("line1"));
        assertEquals(2, result.keyCount("word1"));
        assertEquals(1, result.keyCount("line2"));
        assertEquals(1, result.keyCount("word3"));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldFollowConfigurationChanges() {
      Readers.updateConfig(',');
      try (BufferedReader reader = createReader("comma,separated,values,,next,comma")) {
        assertNotNull(reader);

        Multiset<String> result = Multiset.newMultiset();
        Readers.addAll(result, reader);
        assertEquals(5, result.flattenedKeys().size());
        assertEquals(2, result.keyCount("comma"));
        assertEquals(1, result.keyCount("separated"));
        assertEquals(1, result.keyCount("values"));
        assertEquals(1, result.keyCount("next"));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldThrowExceptionOnNullArguments() {
      try (BufferedReader reader = createReader("token")) {
        assertThrows(NullPointerException.class,
            () -> Readers.addAll((Multiset<String>) null, reader));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }

      Multiset<String> set = Multiset.newMultiset();
      assertThrows(NullPointerException.class, () -> Readers.addAll(set, null));
    }
  }

  @Nested
  class MultimapOverloadTests {

    @Test
    void shouldReadEntries() {
      try (BufferedReader reader = createReader("key1 value1 key2 value2")) {
        Multimap<String, String> map = Multimap.newMultimap();
        Readers.addAll(map, reader);
        assertEquals(2, map.size());
        assertTrue(map.valueList("key1").contains("value1"));
        assertTrue(map.valueList("key2").contains("value2"));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldAggregateMultipleValuesForSameKey() {
      try (BufferedReader reader = createReader("key1 value1 key1 value2")) {
        Multimap<String, String> map = Multimap.newMultimap();
        Readers.addAll(map, reader);
        Collection<String> values = map.valueList("key1");
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldThrowExceptionOnOddNumberOfTokens() {
      try (BufferedReader reader = createReader("key1 value1 key2")) {
        Multimap<String, String> map = Multimap.newMultimap();
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> Readers.addAll(map, reader));
        assertEquals("odd number of tokens", exception.getMessage());
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
    }

    @Test
    void shouldThrowExceptionOnNullArguments() {
      try (BufferedReader reader = createReader("key value")) {
        assertThrows(NullPointerException.class,
            () -> Readers.addAll((Multimap<String, String>) null, reader));
      } catch (IOException e) {
        fail("An unexpected IOException occurred: " + e.getMessage());
      }
      Multimap<String, String> map = Multimap.newMultimap();
      assertThrows(NullPointerException.class, () -> Readers.addAll(map, null));
    }
  }
}
