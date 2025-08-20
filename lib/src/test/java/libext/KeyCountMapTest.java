package libext;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class KeyCountMapTest {

  Map<String, Integer> makeCountMap() {
    Map<String, Integer> countMap = new HashMap<>();
    countMap.put("1", 1);
    countMap.put("2", 2);
    countMap.put("3", 3);
    return countMap;
  }

  KeyCountMap<String> makeKeyCountMap() {
    return new KeyCountMap<>(makeCountMap());
  }

  @Test
  void incrementCount() {
    var keyCountMap = makeKeyCountMap();
    assertEquals(2, keyCountMap.incrementCount("1"));
    assertEquals(3, keyCountMap.incrementCount("2"));
    assertEquals(4, keyCountMap.incrementCount("3"));
    assertEquals(1, keyCountMap.incrementCount("4"));
    assertEquals(2, keyCountMap.keyCount("1"));
    assertEquals(3, keyCountMap.keyCount("2"));
    assertEquals(4, keyCountMap.keyCount("3"));
    assertEquals(1, keyCountMap.keyCount("4"));
  }

  @Test
  void decrementCount() {
    var keyCountMap = makeKeyCountMap();
    assertEquals(0, keyCountMap.decrementCount("1"));
    assertEquals(1, keyCountMap.decrementCount("2"));
    assertEquals(2, keyCountMap.decrementCount("3"));
    assertEquals(0, keyCountMap.decrementCount("4"));
    assertEquals(0, keyCountMap.keyCount("1"));
    assertEquals(1, keyCountMap.keyCount("2"));
    assertEquals(2, keyCountMap.keyCount("3"));
    assertEquals(0, keyCountMap.keyCount("4"));
  }

  @Test
  void keyCount() {
    var keyCountMap = makeKeyCountMap();
    assertEquals(1, keyCountMap.keyCount("1"));
    assertEquals(2, keyCountMap.keyCount("2"));
    assertEquals(3, keyCountMap.keyCount("3"));
    assertEquals(0, keyCountMap.keyCount("4"));
  }

  @Test
  void size() {
    var keyCountMap = makeKeyCountMap();
    assertEquals(3, keyCountMap.size());
  }

  @Test
  void isEmpty() {
    var keyCountMap = makeKeyCountMap();
    assertFalse(keyCountMap.isEmpty());
  }

  @Test
  void containsKey() {
    var keyCountMap = makeKeyCountMap();
    assertTrue(keyCountMap.containsKey("1"));
    assertFalse(keyCountMap.containsKey("4"));
  }

  @Test
  void containsValue() {
    var keyCountMap = makeKeyCountMap();
    assertTrue(keyCountMap.containsValue(1));
    assertFalse(keyCountMap.containsValue(4));
  }

  @Test
  void get() {
    var keyCountMap = makeKeyCountMap();
    assertEquals(1, keyCountMap.get("1"));
    assertNull(keyCountMap.get("4"));
  }

  @Test
  void put() {
    var keyCountMap = makeKeyCountMap();
    Integer count1 = keyCountMap.put("1", 11);
    Integer count2 = keyCountMap.put("5", 12);
    Map<String, Integer> countMap = new HashMap<>();
    countMap.put("1", 11);
    countMap.put("2", 2);
    countMap.put("3", 3);
    countMap.put("5", 12);
    var expected = new KeyCountMap<>(countMap);
    assertEquals(expected, keyCountMap);
    assertEquals(1, count1);
    assertNull(count2);
  }

  @Test
  void remove() {
    var keyCountMap = makeKeyCountMap();
    Integer count1 = keyCountMap.remove("2");
    Map<String, Integer> countMap = new HashMap<>();
    countMap.put("1", 1);
    countMap.put("3", 3);
    var expected = new KeyCountMap<>(countMap);
    assertEquals(expected, keyCountMap);
    assertEquals(2, count1);
  }

  @Test
  void putAll() {
    var keyCountMap1 = makeKeyCountMap();
    Map<String, Integer> countMap = makeCountMap();
    KeyCountMap<String> keyCountMap2 = new KeyCountMap<>();
    keyCountMap2.putAll(countMap);
    assertEquals(3, keyCountMap2.size());
    assertEquals(keyCountMap1, keyCountMap2);
  }

  @Test
  void clear() {
    var keyCountMap = makeKeyCountMap();
    keyCountMap.clear();
    assertTrue(keyCountMap.getCountMap().isEmpty());
    assertEquals(0, keyCountMap.size());
  }

  @Test
  void keySet() {
    var keyCountMap = makeKeyCountMap();
    assertEquals(keyCountMap.getCountMap().keySet(), keyCountMap.keySet());
  }

  @Test
  void values() {
    var keyCountMap = makeKeyCountMap();
    assertEquals(keyCountMap.getCountMap().values(), keyCountMap.values());
  }

  @Test
  void entrySet() {
    var keyCountMap = makeKeyCountMap();
    assertEquals(keyCountMap.getCountMap().entrySet(), keyCountMap.entrySet());
  }

  @Test
  void testHashCode() {
    var keyCountMap = makeKeyCountMap();
    assertEquals(keyCountMap.getCountMap().hashCode(), keyCountMap.hashCode());
  }

  @Test
  void testToString() {
    var keyCountMap = makeKeyCountMap();
    var string = keyCountMap.toString();
    var expected = "KeyCountMap{map={1=1, 2=2, 3=3}}";
    System.out.println(string);
    assertEquals(expected, string);
  }
}
