package libext;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ValueListMapTest {

  Map<String, List<Integer>> makeListMap() {
    Map<String, List<Integer>> listMap = new HashMap<>();
    listMap.put("1", new ArrayList<>(List.of(1, 2, 3)));
    listMap.put("2", new ArrayList<>(List.of(4, 5, 6, 7, 8)));
    listMap.put("3", new ArrayList<>(List.of(9, 10)));
    return listMap;
  }

  ValueListMap<String, Integer> makeValueListMap() {
    return new ValueListMap<>(makeListMap());
  }

  @Test
  void putValue() {
    var valueListMap = makeValueListMap();
    List<Integer> valueList1 = valueListMap.putValue("1", 11);
    var expected1 = Arrays.asList(1, 2, 3, 11);
    assertIterableEquals(expected1, valueList1);
    List<Integer> valueList2 = valueListMap.putValue("5", 12);
    var expected2 = Collections.singletonList(12);
    assertIterableEquals(expected2, valueList2);
    var expected3 = Arrays.asList(1, 2, 3, 11, 4, 5, 6, 7, 8, 9, 10, 12);
    assertIterableEquals(expected3, valueListMap.valueList());
  }

  @Test
  void removeValue() {
    var valueListMap = makeValueListMap();
    List<Integer> valueList1 = valueListMap.removeValue("2", 6);
    var expected1 = Arrays.asList(4, 5, 7, 8);
    assertIterableEquals(expected1, valueList1);
    List<Integer> valueList2 = valueListMap.removeValue("1", 11);
    var expected2 = Arrays.asList(1, 2, 3);
    assertIterableEquals(expected2, valueList2);
    var expected3 = Arrays.asList(1, 2, 3, 4, 5, 7, 8, 9, 10);
    assertIterableEquals(expected3, valueListMap.valueList());
  }

  @Test
  void valueList() {
    var valueListMap = makeValueListMap();
    var valueList = valueListMap.valueList();
    var expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    assertIterableEquals(expected, valueList);
  }

  @Test
  void valueCount() {
    var valueListMap = makeValueListMap();
    assertEquals(10, valueListMap.valueCount());
  }

  @Test
  void size() {
    var valueListMap = makeValueListMap();
    assertEquals(3, valueListMap.size());
  }

  @Test
  void isEmpty() {
    var valueListMap = makeValueListMap();
    assertFalse(valueListMap.isEmpty());
  }

  @Test
  void containsKey() {
    var valueListMap = makeValueListMap();
    assertTrue(valueListMap.containsKey("1"));
    assertFalse(valueListMap.containsKey("5"));
  }

  @Test
  @SuppressWarnings("SuspiciousMethodCalls")
  void containsValue() {
    Object value1 = 9;
    Object value2 = 11;
    var valueListMap = makeValueListMap();
    assertTrue(valueListMap.containsValue(value1));
    assertFalse(valueListMap.containsValue(value2));
  }

  @Test
  void get() {
    var valueListMap = makeValueListMap();
    List<Integer> valueList1 = valueListMap.get("1");
    var expected1 = Arrays.asList(1, 2, 3);
    assertIterableEquals(expected1, valueList1);
    List<Integer> valueList2 = valueListMap.get("5");
    assertNull(valueList2);
    var expected2 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    assertIterableEquals(expected2, valueListMap.valueList());
  }

  @Test
  void put() {
    var valueListMap = makeValueListMap();
    List<Integer> valueList1 = valueListMap.put("1", List.of(11, 13, 14));
    var expected1 = Arrays.asList(1, 2, 3, 11, 13, 14);
    assertIterableEquals(expected1, valueList1);
    List<Integer> valueList2 = valueListMap.put("5", Collections.singletonList(12));
    var expected2 = Collections.singletonList(12);
    assertIterableEquals(expected2, valueList2);
    var expected3 = Arrays.asList(1, 2, 3, 11, 13, 14, 4, 5, 6, 7, 8, 9, 10, 12);
    assertIterableEquals(expected3, valueListMap.valueList());
  }

  @Test
  void remove() {
    var valueListMap = makeValueListMap();
    List<Integer> valueList1 = valueListMap.remove("2");
    var expected1 = Arrays.asList(4, 5, 6, 7, 8);
    assertIterableEquals(expected1, valueList1);
    Collection<Integer> valueList2 = valueListMap.valueList();
    var expected2 = Arrays.asList(1, 2, 3, 9, 10);
    assertIterableEquals(expected2, valueList2);
  }

  @Test
  void putAll() {
    var valueListMap1 = makeValueListMap();
    Map<String, List<Integer>> listMap = makeListMap();
    ValueListMap<String, Integer> valueListMap2 = new ValueListMap<>();
    valueListMap2.putAll(listMap);
    assertEquals(3, valueListMap2.size());
    assertEquals(10, valueListMap2.valueCount());
    assertEquals(valueListMap1, valueListMap2);
  }

  @Test
  void clear() {
    var valueListMap = makeValueListMap();
    valueListMap.clear();
    assertTrue(valueListMap.getListMap().isEmpty());
    assertEquals(0, valueListMap.size());
  }

  @Test
  void keySet() {
    var valueListMap = makeValueListMap();
    assertEquals(valueListMap.getListMap().keySet(), valueListMap.keySet());
  }

  @Test
  void values() {
    var valueListMap = makeValueListMap();
    assertEquals(valueListMap.getListMap().values(), valueListMap.values());
  }

  @Test
  void entrySet() {
    var valueListMap = makeValueListMap();
    assertEquals(valueListMap.getListMap().entrySet(), valueListMap.entrySet());
  }

  @Test
  void testHashCode() {
    var valueListMap = makeValueListMap();
    assertEquals(valueListMap.getListMap().hashCode(), valueListMap.hashCode());
  }

  @Test
  void testToString() {
    var valueListMap = makeValueListMap();
    var string = valueListMap.toString();
    var expected = "ValueListMap{map={1=[1, 2, 3], 2=[4, 5, 6, 7, 8], 3=[9, 10]}}";
    assertEquals(expected, string);
  }
}
