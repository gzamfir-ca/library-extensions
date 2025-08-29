package libext;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TypesTest {

  @Test
  void toNumber() {
    final Object obj1 = 1000;
    Number number1 = Types.toNumber(obj1);
    assertNotNull(number1);
    assertEquals(1000, number1.intValue());
    final Object obj2 = "1000";
    Number number2 = Types.toNumber(obj2);
    assertNotNull(number2);
    assertEquals(1000, number2.intValue());
    final Object obj3 = "+1000";
    Number number3 = Types.toNumber(obj3);
    assertNotNull(number3);
    assertEquals(+1000, number3.intValue());
    final Object obj4 = "-1000";
    Number number4 = Types.toNumber(obj4);
    assertNotNull(number4);
    assertEquals(-1000, number4.intValue());
    final Object obj5 = "0.05";
    Number number5 = Types.toNumber(obj5);
    assertNotNull(number5);
    assertEquals(0.05, number5.doubleValue(), 0.000001);
    final Object obj6 = "5.0e-2";
    Number number6 = Types.toNumber(obj6);
    assertNotNull(number6);
    assertEquals(0.05, number6.doubleValue(), 0.000001);
    final Object obj7 = "5.0E-2";
    Number number7 = Types.toNumber(obj7);
    assertNotNull(number7);
    assertEquals(0.05, number7.doubleValue(), 0.000001);
    final Object obj8 = "0.0005e+2";
    Number number8 = Types.toNumber(obj8);
    assertNotNull(number8);
    assertEquals(0.05, number8.doubleValue(), 0.000001);
    final Object obj9 = "0.0005E+2";
    Number number9 = Types.toNumber(obj9);
    assertNotNull(number9);
    assertEquals(0.05, number9.doubleValue(), 0.000001);
    final Object obj10 = "0.05d";
    Number number10 = Types.toNumber(obj10);
    assertNotNull(number10);
    assertEquals(0.05, number10.doubleValue(), 0.000001);
    final Object obj11 = "0.05D";
    Number number11 = Types.toNumber(obj11);
    assertNotNull(number11);
    assertEquals(0.05, number11.doubleValue(), 0.000001);
    final Object obj12 = new Object();
    Number number12 = Types.toNumber(obj12);
    assertNull(number12);
    assertNull(Types.toNumber(null));
    assertThrows(RuntimeException.class, () -> Types.toNumber("abc"));
    assertThrows(RuntimeException.class, () -> Types.toNumber("10+"));
    assertThrows(RuntimeException.class, () -> Types.toNumber("10-"));
    assertThrows(RuntimeException.class, () -> Types.toNumber("0.05M"));
    assertThrows(RuntimeException.class, () -> Types.toNumber("0M005"));
    assertThrows(RuntimeException.class, () -> Types.toNumber("e0.05"));
    assertThrows(RuntimeException.class, () -> Types.toNumber("E0.05"));
    assertThrows(RuntimeException.class, () -> Types.toNumber(".0.05"));
    assertThrows(RuntimeException.class, () -> Types.toNumber("9999999999999999999"));
    assertThrows(RuntimeException.class, () -> Types.toNumber(""));
    assertThrows(RuntimeException.class, () -> Types.toNumber("                   "));
  }

  @Test
  void toBoolean() {
    final Object obj1 = true;
    Boolean bool1 = Types.toBoolean(obj1);
    assertNotNull(bool1);
    assertTrue(bool1);
    final Object obj2 = false;
    Boolean bool2 = Types.toBoolean(obj2);
    assertNotNull(bool2);
    assertFalse(bool2);
    final Object obj3 = 1000;
    Boolean bool3 = Types.toBoolean(obj3);
    assertNotNull(bool3);
    assertTrue(bool3);
    final Object obj4 = 0;
    Boolean bool4 = Types.toBoolean(obj4);
    assertNotNull(bool4);
    assertFalse(bool4);
    final Object obj5 = "True";
    Boolean bool5 = Types.toBoolean(obj5);
    assertNotNull(bool5);
    assertTrue(bool5);
    final Object obj6 = "yes";
    Boolean bool6 = Types.toBoolean(obj6);
    assertNotNull(bool6);
    assertFalse(bool6);
    final Object obj7 = new Object();
    Boolean bool7 = Types.toBoolean(obj7);
    assertNull(bool7);
    assertNull(Types.toBoolean(null));
  }

  @Test
  void toByte() {
    final byte obj1 = 100;
    Byte byte1 = Types.toByte(obj1);
    assertNotNull(byte1);
    assertEquals(100, byte1.byteValue());
    final Object obj2 = "100";
    Byte byte2 = Types.toByte(obj2);
    assertNotNull(byte2);
    assertEquals(100, byte2.byteValue());
    final Object obj3 = new Object();
    Byte byte3 = Types.toByte(obj3);
    assertNull(byte3);
    assertNull(Types.toByte(null));
  }

  @Test
  void toShort() {
    final short obj1 = 30000;
    Short short1 = Types.toShort(obj1);
    assertNotNull(short1);
    assertEquals(30000, short1.shortValue());
    final Object obj2 = "30000";
    Short short2 = Types.toShort(obj2);
    assertNotNull(short2);
    assertEquals(30000, short2.shortValue());
    final Object obj3 = new Object();
    Short short3 = Types.toShort(obj3);
    assertNull(short3);
    assertNull(Types.toShort(null));
  }

  @Test
  void toInteger() {
    final int obj1 = 2000000000;
    Integer integer1 = Types.toInteger(obj1);
    assertNotNull(integer1);
    assertEquals(2000000000, integer1.intValue());
    final Object obj2 = "2000000000";
    Integer integer2 = Types.toInteger(obj2);
    assertNotNull(integer2);
    assertEquals(2000000000, integer2.intValue());
    final Object obj3 = new Object();
    Integer integer3 = Types.toInteger(obj3);
    assertNull(integer3);
    assertNull(Types.toInteger(null));
  }

  @Test
  void toLong() {
    final long obj1 = 9000000000000000000L;
    Long long1 = Types.toLong(obj1);
    assertNotNull(long1);
    assertEquals(9000000000000000000L, long1.longValue());
    final Object obj2 = "9000000000000000000";
    Long long2 = Types.toLong(obj2);
    assertNotNull(long2);
    assertEquals(9000000000000000000L, long2.longValue());
    final Object obj3 = new Object();
    Long long3 = Types.toLong(obj3);
    assertNull(long3);
    assertNull(Types.toLong(null));
  }

  @Test
  void toFloat() {
    final float obj1 = 1000000000.000001f;
    Float float1 = Types.toFloat(obj1);
    assertNotNull(float1);
    assertEquals(1000000000.000001f, float1, 0.000001);
    final Object obj2 = "1000000000.000001";
    Float float2 = Types.toFloat(obj2);
    assertNotNull(float2);
    assertEquals(1000000000.000001f, float2, 0.000001);
    final Object obj3 = new Object();
    Float float3 = Types.toFloat(obj3);
    assertNull(float3);
    assertNull(Types.toFloat(null));
  }

  @Test
  void toDouble() {
    final double obj1 = 10000000000000000000.000001;
    Double double1 = Types.toDouble(obj1);
    assertNotNull(double1);
    assertEquals(10000000000000000000.000001, double1, 0.000001);
    final Object obj2 = "10000000000000000000.000001";
    Double double2 = Types.toDouble(obj2);
    assertNotNull(double2);
    assertEquals(10000000000000000000.000001, double2, 0.000001);
    final Object obj3 = new Object();
    Double double3 = Types.toDouble(obj3);
    assertNull(double3);
    assertNull(Types.toDouble(null));
  }

  @Test
  void testToString() {
    final Object obj1 = "Some string";
    String string1 = Types.toString(obj1);
    assertNotNull(string1);
    assertEquals("Some string", string1);
    final Object obj2 = new Object();
    String string2 = Types.toString(obj2);
    assertNotNull(string2);
    assertNull(Types.toString(null));
  }
}
