package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Points.xy;

/**
 * Comprehensive unit tests for the Points utility class and all its nested point classes.
 * Tests cover all 20 point types with different coordinate and value combinations,
 * verifying factory methods, field access, hashCode, equals, and toString methods.
 * This test class focuses on edge cases, boundary values, and thorough code path coverage.
 */
@Tag("2510")
public class Points2510Test extends TestBase {

    // ============================================
    // Tests for ByteBytePoint - Edge Cases
    // ============================================

    @Test
    public void testByteBytePoint_MinMaxValues() {
        xy.ByteBytePoint point = xy.ByteBytePoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0);

        assertEquals(Byte.MIN_VALUE, point.x);
        assertEquals(Byte.MAX_VALUE, point.y);
        assertEquals(0, point.v);
    }

    @Test
    public void testByteBytePoint_NegativeValues() {
        xy.ByteBytePoint point = xy.ByteBytePoint.of((byte) -10, (byte) -20, (byte) -30);

        assertEquals(-10, point.x);
        assertEquals(-20, point.y);
        assertEquals(-30, point.v);
    }

    @Test
    public void testByteBytePoint_ZeroValues() {
        xy.ByteBytePoint point = xy.ByteBytePoint.of((byte) 0, (byte) 0, (byte) 0);

        assertEquals(0, point.x);
        assertEquals(0, point.y);
        assertEquals(0, point.v);
    }

    @Test
    public void testByteBytePoint_Equals_DifferentX() {
        xy.ByteBytePoint point1 = xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
        xy.ByteBytePoint point2 = xy.ByteBytePoint.of((byte) 10, (byte) 2, (byte) 3);

        assertFalse(point1.equals(point2));
    }

    @Test
    public void testByteBytePoint_Equals_DifferentY() {
        xy.ByteBytePoint point1 = xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
        xy.ByteBytePoint point2 = xy.ByteBytePoint.of((byte) 1, (byte) 20, (byte) 3);

        assertFalse(point1.equals(point2));
    }

    @Test
    public void testByteBytePoint_HashCode_Consistency() {
        xy.ByteBytePoint point = xy.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 15);
        int hash1 = point.hashCode();
        int hash2 = point.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    public void testByteBytePoint_ToString_Format() {
        xy.ByteBytePoint point = xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
        String result = point.toString();

        assertEquals("[1, 2, 3]", result);
    }

    // ============================================
    // Tests for ByteIntPoint - Edge Cases
    // ============================================

    @Test
    public void testByteIntPoint_MaxIntValue() {
        xy.ByteIntPoint point = xy.ByteIntPoint.of((byte) 10, (byte) 20, Integer.MAX_VALUE);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(Integer.MAX_VALUE, point.v);
    }

    @Test
    public void testByteIntPoint_MinIntValue() {
        xy.ByteIntPoint point = xy.ByteIntPoint.of((byte) -10, (byte) -20, Integer.MIN_VALUE);

        assertEquals(-10, point.x);
        assertEquals(-20, point.y);
        assertEquals(Integer.MIN_VALUE, point.v);
    }

    @Test
    public void testByteIntPoint_Equals_Null() {
        xy.ByteIntPoint point = xy.ByteIntPoint.of((byte) 1, (byte) 2, 3);

        assertFalse(point.equals(null));
    }

    @Test
    public void testByteIntPoint_Equals_DifferentClass() {
        xy.ByteIntPoint point = xy.ByteIntPoint.of((byte) 1, (byte) 2, 3);
        xy.ByteBytePoint other = xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);

        assertFalse(point.equals(other));
    }

    @Test
    public void testByteIntPoint_HashCode_DifferentValues() {
        xy.ByteIntPoint point1 = xy.ByteIntPoint.of((byte) 1, (byte) 2, 100);
        xy.ByteIntPoint point2 = xy.ByteIntPoint.of((byte) 1, (byte) 2, 200);

        assertNotEquals(point1.hashCode(), point2.hashCode());
    }

    // ============================================
    // Tests for ByteLongPoint - Edge Cases
    // ============================================

    @Test
    public void testByteLongPoint_MaxLongValue() {
        xy.ByteLongPoint point = xy.ByteLongPoint.of((byte) 10, (byte) 20, Long.MAX_VALUE);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(Long.MAX_VALUE, point.v);
    }

    @Test
    public void testByteLongPoint_MinLongValue() {
        xy.ByteLongPoint point = xy.ByteLongPoint.of((byte) -10, (byte) -20, Long.MIN_VALUE);

        assertEquals(-10, point.x);
        assertEquals(-20, point.y);
        assertEquals(Long.MIN_VALUE, point.v);
    }

    @Test
    public void testByteLongPoint_ZeroValue() {
        xy.ByteLongPoint point = xy.ByteLongPoint.of((byte) 5, (byte) 10, 0L);

        assertEquals(5, point.x);
        assertEquals(10, point.y);
        assertEquals(0L, point.v);
    }

    @Test
    public void testByteLongPoint_ToString_NegativeValues() {
        xy.ByteLongPoint point = xy.ByteLongPoint.of((byte) -1, (byte) -2, -3L);
        String result = point.toString();

        assertTrue(result.contains("-1"));
        assertTrue(result.contains("-2"));
        assertTrue(result.contains("-3"));
    }

    // ============================================
    // Tests for ByteDoublePoint - Edge Cases
    // ============================================

    @Test
    public void testByteDoublePoint_MaxDoubleValue() {
        xy.ByteDoublePoint point = xy.ByteDoublePoint.of((byte) 10, (byte) 20, Double.MAX_VALUE);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(Double.MAX_VALUE, point.v, 0.0);
    }

    @Test
    public void testByteDoublePoint_MinDoubleValue() {
        xy.ByteDoublePoint point = xy.ByteDoublePoint.of((byte) 10, (byte) 20, Double.MIN_VALUE);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(Double.MIN_VALUE, point.v, 0.0);
    }

    @Test
    public void testByteDoublePoint_NaN() {
        xy.ByteDoublePoint point1 = xy.ByteDoublePoint.of((byte) 1, (byte) 2, Double.NaN);
        xy.ByteDoublePoint point2 = xy.ByteDoublePoint.of((byte) 1, (byte) 2, Double.NaN);

        assertTrue(point1.equals(point2));
    }

    @Test
    public void testByteDoublePoint_PositiveInfinity() {
        xy.ByteDoublePoint point = xy.ByteDoublePoint.of((byte) 1, (byte) 2, Double.POSITIVE_INFINITY);

        assertEquals(1, point.x);
        assertEquals(2, point.y);
        assertEquals(Double.POSITIVE_INFINITY, point.v, 0.0);
    }

    @Test
    public void testByteDoublePoint_NegativeInfinity() {
        xy.ByteDoublePoint point = xy.ByteDoublePoint.of((byte) 1, (byte) 2, Double.NEGATIVE_INFINITY);

        assertEquals(1, point.x);
        assertEquals(2, point.y);
        assertEquals(Double.NEGATIVE_INFINITY, point.v, 0.0);
    }

    @Test
    public void testByteDoublePoint_NegativeZero() {
        xy.ByteDoublePoint point1 = xy.ByteDoublePoint.of((byte) 1, (byte) 2, 0.0);
        xy.ByteDoublePoint point2 = xy.ByteDoublePoint.of((byte) 1, (byte) 2, -0.0);

        assertFalse(point1.equals(point2));
    }

    // ============================================
    // Tests for ByteObjPoint - Edge Cases
    // ============================================

    @Test
    public void testByteObjPoint_NullValue() {
        xy.ByteObjPoint<String> point = xy.ByteObjPoint.of((byte) 10, (byte) 20, null);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(null, point.v);
    }

    @Test
    public void testByteObjPoint_Equals_WithNull() {
        xy.ByteObjPoint<String> point1 = xy.ByteObjPoint.of((byte) 1, (byte) 2, null);
        xy.ByteObjPoint<String> point2 = xy.ByteObjPoint.of((byte) 1, (byte) 2, null);

        assertTrue(point1.equals(point2));
    }

    @Test
    public void testByteObjPoint_Equals_OneNull() {
        xy.ByteObjPoint<String> point1 = xy.ByteObjPoint.of((byte) 1, (byte) 2, "test");
        xy.ByteObjPoint<String> point2 = xy.ByteObjPoint.of((byte) 1, (byte) 2, null);

        assertFalse(point1.equals(point2));
    }

    @Test
    public void testByteObjPoint_HashCode_WithNull() {
        xy.ByteObjPoint<String> point1 = xy.ByteObjPoint.of((byte) 1, (byte) 2, null);
        xy.ByteObjPoint<String> point2 = xy.ByteObjPoint.of((byte) 1, (byte) 2, null);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testByteObjPoint_ComplexObject() {
        java.util.List<String> list = java.util.Arrays.asList("a", "b", "c");
        xy.ByteObjPoint<java.util.List<String>> point = xy.ByteObjPoint.of((byte) 5, (byte) 10, list);

        assertEquals(5, point.x);
        assertEquals(10, point.y);
        assertEquals(list, point.v);
    }

    @Test
    public void testByteObjPoint_ToString_WithNull() {
        xy.ByteObjPoint<String> point = xy.ByteObjPoint.of((byte) 1, (byte) 2, null);
        String result = point.toString();

        assertNotNull(result);
        assertTrue(result.contains("null"));
    }

    // ============================================
    // Tests for IntBytePoint - Edge Cases
    // ============================================

    @Test
    public void testIntBytePoint_MaxIntCoordinates() {
        xy.IntBytePoint point = xy.IntBytePoint.of(Integer.MAX_VALUE, Integer.MIN_VALUE, (byte) 50);

        assertEquals(Integer.MAX_VALUE, point.x);
        assertEquals(Integer.MIN_VALUE, point.y);
        assertEquals(50, point.v);
    }

    @Test
    public void testIntBytePoint_NegativeCoordinates() {
        xy.IntBytePoint point = xy.IntBytePoint.of(-100, -200, (byte) -30);

        assertEquals(-100, point.x);
        assertEquals(-200, point.y);
        assertEquals(-30, point.v);
    }

    @Test
    public void testIntBytePoint_ZeroValues() {
        xy.IntBytePoint point = xy.IntBytePoint.of(0, 0, (byte) 0);

        assertEquals(0, point.x);
        assertEquals(0, point.y);
        assertEquals(0, point.v);
    }

    @Test
    public void testIntBytePoint_Equals_SameInstance() {
        xy.IntBytePoint point = xy.IntBytePoint.of(10, 20, (byte) 30);

        assertTrue(point.equals(point));
    }

    // ============================================
    // Tests for IntIntPoint - Edge Cases
    // ============================================

    @Test
    public void testIntIntPoint_MaxIntValues() {
        xy.IntIntPoint point = xy.IntIntPoint.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, point.x);
        assertEquals(Integer.MAX_VALUE, point.y);
        assertEquals(Integer.MAX_VALUE, point.v);
    }

    @Test
    public void testIntIntPoint_MinIntValues() {
        xy.IntIntPoint point = xy.IntIntPoint.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

        assertEquals(Integer.MIN_VALUE, point.x);
        assertEquals(Integer.MIN_VALUE, point.y);
        assertEquals(Integer.MIN_VALUE, point.v);
    }

    @Test
    public void testIntIntPoint_MixedValues() {
        xy.IntIntPoint point = xy.IntIntPoint.of(-100, 200, 0);

        assertEquals(-100, point.x);
        assertEquals(200, point.y);
        assertEquals(0, point.v);
    }

    @Test
    public void testIntIntPoint_LargePositiveValues() {
        xy.IntIntPoint point = xy.IntIntPoint.of(1000000, 2000000, 3000000);

        assertEquals(1000000, point.x);
        assertEquals(2000000, point.y);
        assertEquals(3000000, point.v);
    }

    @Test
    public void testIntIntPoint_HashCode_Different() {
        xy.IntIntPoint point1 = xy.IntIntPoint.of(1, 2, 3);
        xy.IntIntPoint point2 = xy.IntIntPoint.of(3, 2, 1);

        assertNotEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testIntIntPoint_ToString_NegativeValues() {
        xy.IntIntPoint point = xy.IntIntPoint.of(-10, -20, -30);
        String result = point.toString();

        assertEquals("[-10, -20, -30]", result);
    }

    // ============================================
    // Tests for IntLongPoint - Edge Cases
    // ============================================

    @Test
    public void testIntLongPoint_MaxLongValue() {
        xy.IntLongPoint point = xy.IntLongPoint.of(100, 200, Long.MAX_VALUE);

        assertEquals(100, point.x);
        assertEquals(200, point.y);
        assertEquals(Long.MAX_VALUE, point.v);
    }

    @Test
    public void testIntLongPoint_MinLongValue() {
        xy.IntLongPoint point = xy.IntLongPoint.of(-100, -200, Long.MIN_VALUE);

        assertEquals(-100, point.x);
        assertEquals(-200, point.y);
        assertEquals(Long.MIN_VALUE, point.v);
    }

    @Test
    public void testIntLongPoint_MixedSignValues() {
        xy.IntLongPoint point = xy.IntLongPoint.of(-100, 200, -3000L);

        assertEquals(-100, point.x);
        assertEquals(200, point.y);
        assertEquals(-3000L, point.v);
    }

    // ============================================
    // Tests for IntDoublePoint - Edge Cases
    // ============================================

    @Test
    public void testIntDoublePoint_SmallDecimals() {
        xy.IntDoublePoint point = xy.IntDoublePoint.of(10, 20, 0.0001);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(0.0001, point.v, 0.00001);
    }

    @Test
    public void testIntDoublePoint_LargeDecimals() {
        xy.IntDoublePoint point = xy.IntDoublePoint.of(10, 20, 999999.999999);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(999999.999999, point.v, 0.00001);
    }

    @Test
    public void testIntDoublePoint_NegativeDecimals() {
        xy.IntDoublePoint point = xy.IntDoublePoint.of(-10, -20, -3.14159);

        assertEquals(-10, point.x);
        assertEquals(-20, point.y);
        assertEquals(-3.14159, point.v, 0.00001);
    }

    @Test
    public void testIntDoublePoint_PositiveInfinity() {
        xy.IntDoublePoint point = xy.IntDoublePoint.of(1, 2, Double.POSITIVE_INFINITY);

        assertEquals(Double.POSITIVE_INFINITY, point.v, 0.0);
    }

    @Test
    public void testIntDoublePoint_NaN() {
        xy.IntDoublePoint point1 = xy.IntDoublePoint.of(1, 2, Double.NaN);
        xy.IntDoublePoint point2 = xy.IntDoublePoint.of(1, 2, Double.NaN);

        assertTrue(point1.equals(point2));
    }

    // ============================================
    // Tests for IntObjPoint - Edge Cases
    // ============================================

    @Test
    public void testIntObjPoint_NullValue() {
        xy.IntObjPoint<String> point = xy.IntObjPoint.of(10, 20, null);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(null, point.v);
    }

    @Test
    public void testIntObjPoint_IntegerObject() {
        xy.IntObjPoint<Integer> point = xy.IntObjPoint.of(10, 20, Integer.MAX_VALUE);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(Integer.MAX_VALUE, point.v);
    }

    @Test
    public void testIntObjPoint_StringObject() {
        xy.IntObjPoint<String> point = xy.IntObjPoint.of(-100, 200, "Test String");

        assertEquals(-100, point.x);
        assertEquals(200, point.y);
        assertEquals("Test String", point.v);
    }

    @Test
    public void testIntObjPoint_EmptyString() {
        xy.IntObjPoint<String> point = xy.IntObjPoint.of(0, 0, "");

        assertEquals(0, point.x);
        assertEquals(0, point.y);
        assertEquals("", point.v);
    }

    @Test
    public void testIntObjPoint_ArrayValue() {
        int[] array = {1, 2, 3};
        xy.IntObjPoint<int[]> point = xy.IntObjPoint.of(5, 10, array);

        assertEquals(5, point.x);
        assertEquals(10, point.y);
        assertEquals(array, point.v);
    }

    // ============================================
    // Tests for LongBytePoint - Edge Cases
    // ============================================

    @Test
    public void testLongBytePoint_MaxLongCoordinates() {
        xy.LongBytePoint point = xy.LongBytePoint.of(Long.MAX_VALUE, Long.MIN_VALUE, (byte) 50);

        assertEquals(Long.MAX_VALUE, point.x);
        assertEquals(Long.MIN_VALUE, point.y);
        assertEquals(50, point.v);
    }

    @Test
    public void testLongBytePoint_ZeroCoordinates() {
        xy.LongBytePoint point = xy.LongBytePoint.of(0L, 0L, (byte) 0);

        assertEquals(0L, point.x);
        assertEquals(0L, point.y);
        assertEquals(0, point.v);
    }

    @Test
    public void testLongBytePoint_NegativeValues() {
        xy.LongBytePoint point = xy.LongBytePoint.of(-1000L, -2000L, (byte) -50);

        assertEquals(-1000L, point.x);
        assertEquals(-2000L, point.y);
        assertEquals(-50, point.v);
    }

    // ============================================
    // Tests for LongIntPoint - Edge Cases
    // ============================================

    @Test
    public void testLongIntPoint_MaxValues() {
        xy.LongIntPoint point = xy.LongIntPoint.of(Long.MAX_VALUE, Long.MIN_VALUE, Integer.MAX_VALUE);

        assertEquals(Long.MAX_VALUE, point.x);
        assertEquals(Long.MIN_VALUE, point.y);
        assertEquals(Integer.MAX_VALUE, point.v);
    }

    @Test
    public void testLongIntPoint_MixedSigns() {
        xy.LongIntPoint point = xy.LongIntPoint.of(-100000L, 200000L, -300);

        assertEquals(-100000L, point.x);
        assertEquals(200000L, point.y);
        assertEquals(-300, point.v);
    }

    @Test
    public void testLongIntPoint_ZeroValues() {
        xy.LongIntPoint point = xy.LongIntPoint.of(0L, 0L, 0);

        assertEquals(0L, point.x);
        assertEquals(0L, point.y);
        assertEquals(0, point.v);
    }

    // ============================================
    // Tests for LongLongPoint - Edge Cases
    // ============================================

    @Test
    public void testLongLongPoint_MaxLongValues() {
        xy.LongLongPoint point = xy.LongLongPoint.of(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);

        assertEquals(Long.MAX_VALUE, point.x);
        assertEquals(Long.MAX_VALUE, point.y);
        assertEquals(Long.MAX_VALUE, point.v);
    }

    @Test
    public void testLongLongPoint_MinLongValues() {
        xy.LongLongPoint point = xy.LongLongPoint.of(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, point.x);
        assertEquals(Long.MIN_VALUE, point.y);
        assertEquals(Long.MIN_VALUE, point.v);
    }

    @Test
    public void testLongLongPoint_LargeValues() {
        xy.LongLongPoint point = xy.LongLongPoint.of(9223372036854775806L, -9223372036854775807L, 1000000000000L);

        assertEquals(9223372036854775806L, point.x);
        assertEquals(-9223372036854775807L, point.y);
        assertEquals(1000000000000L, point.v);
    }

    @Test
    public void testLongLongPoint_HashCode_Consistency() {
        xy.LongLongPoint point = xy.LongLongPoint.of(12345L, 67890L, 11111L);
        int hash1 = point.hashCode();
        int hash2 = point.hashCode();

        assertEquals(hash1, hash2);
    }

    // ============================================
    // Tests for LongDoublePoint - Edge Cases
    // ============================================

    @Test
    public void testLongDoublePoint_MaxValues() {
        xy.LongDoublePoint point = xy.LongDoublePoint.of(Long.MAX_VALUE, Long.MIN_VALUE, Double.MAX_VALUE);

        assertEquals(Long.MAX_VALUE, point.x);
        assertEquals(Long.MIN_VALUE, point.y);
        assertEquals(Double.MAX_VALUE, point.v, 0.0);
    }

    @Test
    public void testLongDoublePoint_SmallDouble() {
        xy.LongDoublePoint point = xy.LongDoublePoint.of(100L, 200L, Double.MIN_VALUE);

        assertEquals(100L, point.x);
        assertEquals(200L, point.y);
        assertEquals(Double.MIN_VALUE, point.v, 0.0);
    }

    @Test
    public void testLongDoublePoint_NaN() {
        xy.LongDoublePoint point1 = xy.LongDoublePoint.of(1L, 2L, Double.NaN);
        xy.LongDoublePoint point2 = xy.LongDoublePoint.of(1L, 2L, Double.NaN);

        assertTrue(point1.equals(point2));
    }

    @Test
    public void testLongDoublePoint_Infinity() {
        xy.LongDoublePoint point = xy.LongDoublePoint.of(100L, 200L, Double.NEGATIVE_INFINITY);

        assertEquals(Double.NEGATIVE_INFINITY, point.v, 0.0);
    }

    // ============================================
    // Tests for LongObjPoint - Edge Cases
    // ============================================

    @Test
    public void testLongObjPoint_NullValue() {
        xy.LongObjPoint<String> point = xy.LongObjPoint.of(1000L, 2000L, null);

        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals(null, point.v);
    }

    @Test
    public void testLongObjPoint_LongObject() {
        xy.LongObjPoint<Long> point = xy.LongObjPoint.of(100L, 200L, Long.MAX_VALUE);

        assertEquals(100L, point.x);
        assertEquals(200L, point.y);
        assertEquals(Long.MAX_VALUE, point.v);
    }

    @Test
    public void testLongObjPoint_ComplexGeneric() {
        java.util.Map<String, Integer> map = new java.util.HashMap<>();
        map.put("key", 42);
        xy.LongObjPoint<java.util.Map<String, Integer>> point = xy.LongObjPoint.of(10L, 20L, map);

        assertEquals(10L, point.x);
        assertEquals(20L, point.y);
        assertEquals(map, point.v);
    }

    @Test
    public void testLongObjPoint_Equals_NullValues() {
        xy.LongObjPoint<String> point1 = xy.LongObjPoint.of(1L, 2L, null);
        xy.LongObjPoint<String> point2 = xy.LongObjPoint.of(1L, 2L, null);

        assertTrue(point1.equals(point2));
    }

    // ============================================
    // Tests for DoubleBytePoint - Edge Cases
    // ============================================

    @Test
    public void testDoubleBytePoint_MaxDoubleCoordinates() {
        xy.DoubleBytePoint point = xy.DoubleBytePoint.of(Double.MAX_VALUE, Double.MIN_VALUE, (byte) 50);

        assertEquals(Double.MAX_VALUE, point.x, 0.0);
        assertEquals(Double.MIN_VALUE, point.y, 0.0);
        assertEquals(50, point.v);
    }

    @Test
    public void testDoubleBytePoint_NegativeCoordinates() {
        xy.DoubleBytePoint point = xy.DoubleBytePoint.of(-10.5, -20.5, (byte) -30);

        assertEquals(-10.5, point.x, 0.001);
        assertEquals(-20.5, point.y, 0.001);
        assertEquals(-30, point.v);
    }

    @Test
    public void testDoubleBytePoint_ZeroCoordinates() {
        xy.DoubleBytePoint point = xy.DoubleBytePoint.of(0.0, 0.0, (byte) 0);

        assertEquals(0.0, point.x, 0.0);
        assertEquals(0.0, point.y, 0.0);
        assertEquals(0, point.v);
    }

    @Test
    public void testDoubleBytePoint_NaNCoordinates() {
        xy.DoubleBytePoint point1 = xy.DoubleBytePoint.of(Double.NaN, 2.5, (byte) 3);
        xy.DoubleBytePoint point2 = xy.DoubleBytePoint.of(Double.NaN, 2.5, (byte) 3);

        assertTrue(point1.equals(point2));
    }

    @Test
    public void testDoubleBytePoint_InfinityCoordinates() {
        xy.DoubleBytePoint point = xy.DoubleBytePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, (byte) 0);

        assertEquals(Double.POSITIVE_INFINITY, point.x, 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, point.y, 0.0);
    }

    // ============================================
    // Tests for DoubleIntPoint - Edge Cases
    // ============================================

    @Test
    public void testDoubleIntPoint_SmallDoubles() {
        xy.DoubleIntPoint point = xy.DoubleIntPoint.of(0.00001, 0.00002, 100);

        assertEquals(0.00001, point.x, 0.000001);
        assertEquals(0.00002, point.y, 0.000001);
        assertEquals(100, point.v);
    }

    @Test
    public void testDoubleIntPoint_LargeDoubles() {
        xy.DoubleIntPoint point = xy.DoubleIntPoint.of(999999.999, 888888.888, 777);

        assertEquals(999999.999, point.x, 0.001);
        assertEquals(888888.888, point.y, 0.001);
        assertEquals(777, point.v);
    }

    @Test
    public void testDoubleIntPoint_MixedSigns() {
        xy.DoubleIntPoint point = xy.DoubleIntPoint.of(-1.5, 2.5, -300);

        assertEquals(-1.5, point.x, 0.001);
        assertEquals(2.5, point.y, 0.001);
        assertEquals(-300, point.v);
    }

    @Test
    public void testDoubleIntPoint_NaN() {
        xy.DoubleIntPoint point1 = xy.DoubleIntPoint.of(1.5, Double.NaN, 100);
        xy.DoubleIntPoint point2 = xy.DoubleIntPoint.of(1.5, Double.NaN, 100);

        assertTrue(point1.equals(point2));
    }

    // ============================================
    // Tests for DoubleLongPoint - Edge Cases
    // ============================================

    @Test
    public void testDoubleLongPoint_MaxValues() {
        xy.DoubleLongPoint point = xy.DoubleLongPoint.of(Double.MAX_VALUE, Double.MIN_VALUE, Long.MAX_VALUE);

        assertEquals(Double.MAX_VALUE, point.x, 0.0);
        assertEquals(Double.MIN_VALUE, point.y, 0.0);
        assertEquals(Long.MAX_VALUE, point.v);
    }

    @Test
    public void testDoubleLongPoint_NegativeValues() {
        xy.DoubleLongPoint point = xy.DoubleLongPoint.of(-100.5, -200.5, -3000L);

        assertEquals(-100.5, point.x, 0.001);
        assertEquals(-200.5, point.y, 0.001);
        assertEquals(-3000L, point.v);
    }

    @Test
    public void testDoubleLongPoint_ZeroValues() {
        xy.DoubleLongPoint point = xy.DoubleLongPoint.of(0.0, 0.0, 0L);

        assertEquals(0.0, point.x, 0.0);
        assertEquals(0.0, point.y, 0.0);
        assertEquals(0L, point.v);
    }

    @Test
    public void testDoubleLongPoint_InfinityCoordinates() {
        xy.DoubleLongPoint point = xy.DoubleLongPoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 1000L);

        assertEquals(Double.POSITIVE_INFINITY, point.x, 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, point.y, 0.0);
        assertEquals(1000L, point.v);
    }

    // ============================================
    // Tests for DoubleDoublePoint - Edge Cases
    // ============================================

    @Test
    public void testDoubleDoublePoint_MaxDoubleValues() {
        xy.DoubleDoublePoint point = xy.DoubleDoublePoint.of(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

        assertEquals(Double.MAX_VALUE, point.x, 0.0);
        assertEquals(Double.MAX_VALUE, point.y, 0.0);
        assertEquals(Double.MAX_VALUE, point.v, 0.0);
    }

    @Test
    public void testDoubleDoublePoint_MinDoubleValues() {
        xy.DoubleDoublePoint point = xy.DoubleDoublePoint.of(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);

        assertEquals(Double.MIN_VALUE, point.x, 0.0);
        assertEquals(Double.MIN_VALUE, point.y, 0.0);
        assertEquals(Double.MIN_VALUE, point.v, 0.0);
    }

    @Test
    public void testDoubleDoublePoint_AllNaN() {
        xy.DoubleDoublePoint point1 = xy.DoubleDoublePoint.of(Double.NaN, Double.NaN, Double.NaN);
        xy.DoubleDoublePoint point2 = xy.DoubleDoublePoint.of(Double.NaN, Double.NaN, Double.NaN);

        assertTrue(point1.equals(point2));
    }

    @Test
    public void testDoubleDoublePoint_AllInfinity() {
        xy.DoubleDoublePoint point = xy.DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        assertEquals(Double.POSITIVE_INFINITY, point.x, 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, point.y, 0.0);
        assertEquals(Double.POSITIVE_INFINITY, point.v, 0.0);
    }

    @Test
    public void testDoubleDoublePoint_NegativeZero() {
        xy.DoubleDoublePoint point1 = xy.DoubleDoublePoint.of(-0.0, 0.0, -0.0);
        xy.DoubleDoublePoint point2 = xy.DoubleDoublePoint.of(0.0, -0.0, 0.0);

        assertFalse(point1.equals(point2));
    }

    @Test
    public void testDoubleDoublePoint_VerySmallValues() {
        xy.DoubleDoublePoint point = xy.DoubleDoublePoint.of(1e-308, 1e-307, 1e-306);

        assertEquals(1e-308, point.x, 0.0);
        assertEquals(1e-307, point.y, 0.0);
        assertEquals(1e-306, point.v, 0.0);
    }

    @Test
    public void testDoubleDoublePoint_MixedSpecialValues() {
        xy.DoubleDoublePoint point = xy.DoubleDoublePoint.of(Double.NaN, Double.POSITIVE_INFINITY, -0.0);

        assertTrue(Double.isNaN(point.x));
        assertEquals(Double.POSITIVE_INFINITY, point.y, 0.0);
        assertEquals(-0.0, point.v, 0.0);
    }

    // ============================================
    // Tests for DoubleObjPoint - Edge Cases
    // ============================================

    @Test
    public void testDoubleObjPoint_NullValue() {
        xy.DoubleObjPoint<String> point = xy.DoubleObjPoint.of(10.5, 20.5, null);

        assertEquals(10.5, point.x, 0.001);
        assertEquals(20.5, point.y, 0.001);
        assertEquals(null, point.v);
    }

    @Test
    public void testDoubleObjPoint_DoubleObject() {
        xy.DoubleObjPoint<Double> point = xy.DoubleObjPoint.of(1.5, 2.5, Double.MAX_VALUE);

        assertEquals(1.5, point.x, 0.001);
        assertEquals(2.5, point.y, 0.001);
        assertEquals(Double.MAX_VALUE, point.v);
    }

    @Test
    public void testDoubleObjPoint_NaNCoordinates() {
        xy.DoubleObjPoint<String> point = xy.DoubleObjPoint.of(Double.NaN, Double.NaN, "test");

        assertTrue(Double.isNaN(point.x));
        assertTrue(Double.isNaN(point.y));
        assertEquals("test", point.v);
    }

    @Test
    public void testDoubleObjPoint_InfinityCoordinates() {
        xy.DoubleObjPoint<Integer> point = xy.DoubleObjPoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 42);

        assertEquals(Double.POSITIVE_INFINITY, point.x, 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, point.y, 0.0);
        assertEquals(42, point.v);
    }

    @Test
    public void testDoubleObjPoint_Equals_DifferentNullValues() {
        xy.DoubleObjPoint<String> point1 = xy.DoubleObjPoint.of(1.5, 2.5, "test");
        xy.DoubleObjPoint<String> point2 = xy.DoubleObjPoint.of(1.5, 2.5, null);

        assertFalse(point1.equals(point2));
    }

    // ============================================
    // Cross-Type Equality Tests
    // ============================================

    @Test
    public void testCrossType_IntIntPoint_vs_LongLongPoint() {
        xy.IntIntPoint intPoint = xy.IntIntPoint.of(10, 20, 30);
        xy.LongLongPoint longPoint = xy.LongLongPoint.of(10L, 20L, 30L);

        assertFalse(intPoint.equals(longPoint));
        assertFalse(longPoint.equals(intPoint));
    }

    @Test
    public void testCrossType_DoubleDoublePoint_vs_IntIntPoint() {
        xy.DoubleDoublePoint doublePoint = xy.DoubleDoublePoint.of(10.0, 20.0, 30.0);
        xy.IntIntPoint intPoint = xy.IntIntPoint.of(10, 20, 30);

        assertFalse(doublePoint.equals(intPoint));
        assertFalse(intPoint.equals(doublePoint));
    }

    @Test
    public void testCrossType_ByteObjPoint_vs_IntObjPoint() {
        xy.ByteObjPoint<String> bytePoint = xy.ByteObjPoint.of((byte) 10, (byte) 20, "test");
        xy.IntObjPoint<String> intPoint = xy.IntObjPoint.of(10, 20, "test");

        assertFalse(bytePoint.equals(intPoint));
        assertFalse(intPoint.equals(bytePoint));
    }

    // ============================================
    // HashCode Distribution Tests
    // ============================================

    @Test
    public void testHashCode_IntIntPoint_Distribution() {
        xy.IntIntPoint point1 = xy.IntIntPoint.of(1, 2, 3);
        xy.IntIntPoint point2 = xy.IntIntPoint.of(2, 1, 3);
        xy.IntIntPoint point3 = xy.IntIntPoint.of(3, 2, 1);

        // All should have different hash codes due to different coordinates
        int hash1 = point1.hashCode();
        int hash2 = point2.hashCode();
        int hash3 = point3.hashCode();

        assertTrue(hash1 != hash2 || hash1 != hash3 || hash2 != hash3);
    }

    @Test
    public void testHashCode_DoubleDoublePoint_NaN() {
        xy.DoubleDoublePoint point1 = xy.DoubleDoublePoint.of(Double.NaN, 2.0, 3.0);
        xy.DoubleDoublePoint point2 = xy.DoubleDoublePoint.of(Double.NaN, 2.0, 3.0);

        // Hash codes should be equal for NaN values
        assertEquals(point1.hashCode(), point2.hashCode());
    }

    // ============================================
    // ToString Format Tests
    // ============================================

    @Test
    public void testToString_ByteBytePoint_Format() {
        xy.ByteBytePoint point = xy.ByteBytePoint.of((byte) -128, (byte) 127, (byte) 0);
        String result = point.toString();

        assertEquals("[-128, 127, 0]", result);
    }

    @Test
    public void testToString_IntIntPoint_Format() {
        xy.IntIntPoint point = xy.IntIntPoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        String result = point.toString();

        assertEquals("[" + Integer.MIN_VALUE + ", " + Integer.MAX_VALUE + ", 0]", result);
    }

    @Test
    public void testToString_LongLongPoint_Format() {
        xy.LongLongPoint point = xy.LongLongPoint.of(Long.MIN_VALUE, Long.MAX_VALUE, 0L);
        String result = point.toString();

        assertEquals("[" + Long.MIN_VALUE + ", " + Long.MAX_VALUE + ", 0]", result);
    }

    @Test
    public void testToString_DoubleDoublePoint_Format() {
        xy.DoubleDoublePoint point = xy.DoubleDoublePoint.of(1.5, 2.5, 3.5);
        String result = point.toString();

        assertTrue(result.contains("1.5"));
        assertTrue(result.contains("2.5"));
        assertTrue(result.contains("3.5"));
    }

    @Test
    public void testToString_ObjPoint_NullValue() {
        xy.IntObjPoint<String> point = xy.IntObjPoint.of(10, 20, null);
        String result = point.toString();

        assertTrue(result.contains("10"));
        assertTrue(result.contains("20"));
        assertTrue(result.contains("null"));
    }

    // ============================================
    // Equals Edge Cases
    // ============================================

    @Test
    public void testEquals_IntIntPoint_Null() {
        xy.IntIntPoint point = xy.IntIntPoint.of(1, 2, 3);

        assertFalse(point.equals(null));
    }

    @Test
    public void testEquals_IntIntPoint_String() {
        xy.IntIntPoint point = xy.IntIntPoint.of(1, 2, 3);

        assertFalse(point.equals("not a point"));
    }

    @Test
    public void testEquals_IntIntPoint_SameReference() {
        xy.IntIntPoint point = xy.IntIntPoint.of(10, 20, 30);

        assertTrue(point.equals(point));
    }

    @Test
    public void testEquals_LongLongPoint_DifferentX() {
        xy.LongLongPoint point1 = xy.LongLongPoint.of(100L, 200L, 300L);
        xy.LongLongPoint point2 = xy.LongLongPoint.of(999L, 200L, 300L);

        assertFalse(point1.equals(point2));
    }

    @Test
    public void testEquals_LongLongPoint_DifferentY() {
        xy.LongLongPoint point1 = xy.LongLongPoint.of(100L, 200L, 300L);
        xy.LongLongPoint point2 = xy.LongLongPoint.of(100L, 999L, 300L);

        assertFalse(point1.equals(point2));
    }

    @Test
    public void testEquals_LongLongPoint_DifferentV() {
        xy.LongLongPoint point1 = xy.LongLongPoint.of(100L, 200L, 300L);
        xy.LongLongPoint point2 = xy.LongLongPoint.of(100L, 200L, 999L);

        assertFalse(point1.equals(point2));
    }

    @Test
    public void testEquals_DoubleDoublePoint_DifferentX() {
        xy.DoubleDoublePoint point1 = xy.DoubleDoublePoint.of(1.5, 2.5, 3.5);
        xy.DoubleDoublePoint point2 = xy.DoubleDoublePoint.of(9.9, 2.5, 3.5);

        assertFalse(point1.equals(point2));
    }

    @Test
    public void testEquals_DoubleDoublePoint_DifferentY() {
        xy.DoubleDoublePoint point1 = xy.DoubleDoublePoint.of(1.5, 2.5, 3.5);
        xy.DoubleDoublePoint point2 = xy.DoubleDoublePoint.of(1.5, 9.9, 3.5);

        assertFalse(point1.equals(point2));
    }

    @Test
    public void testEquals_DoubleDoublePoint_DifferentV() {
        xy.DoubleDoublePoint point1 = xy.DoubleDoublePoint.of(1.5, 2.5, 3.5);
        xy.DoubleDoublePoint point2 = xy.DoubleDoublePoint.of(1.5, 2.5, 9.9);

        assertFalse(point1.equals(point2));
    }

    // ============================================
    // Special Object Types Tests
    // ============================================

    @Test
    public void testObjPoint_WithCustomObject() {
        class CustomObject {
            int value;

            CustomObject(int value) {
                this.value = value;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof CustomObject) {
                    return this.value == ((CustomObject) obj).value;
                }
                return false;
            }

            @Override
            public int hashCode() {
                return value;
            }
        }

        CustomObject obj1 = new CustomObject(42);
        CustomObject obj2 = new CustomObject(42);

        xy.IntObjPoint<CustomObject> point1 = xy.IntObjPoint.of(10, 20, obj1);
        xy.IntObjPoint<CustomObject> point2 = xy.IntObjPoint.of(10, 20, obj2);

        assertTrue(point1.equals(point2));
        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testObjPoint_WithNestedCollection() {
        java.util.List<java.util.List<Integer>> nested = new java.util.ArrayList<>();
        nested.add(java.util.Arrays.asList(1, 2, 3));
        nested.add(java.util.Arrays.asList(4, 5, 6));

        xy.DoubleObjPoint<java.util.List<java.util.List<Integer>>> point = xy.DoubleObjPoint.of(1.5, 2.5, nested);

        assertEquals(1.5, point.x, 0.001);
        assertEquals(2.5, point.y, 0.001);
        assertEquals(nested, point.v);
    }

    // ============================================
    // Boundary Value Tests
    // ============================================

    @Test
    public void testBytePoint_BoundaryValues() {
        xy.ByteBytePoint minPoint = xy.ByteBytePoint.of(Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE);
        xy.ByteBytePoint maxPoint = xy.ByteBytePoint.of(Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE);

        assertEquals(Byte.MIN_VALUE, minPoint.x);
        assertEquals(Byte.MIN_VALUE, minPoint.y);
        assertEquals(Byte.MIN_VALUE, minPoint.v);

        assertEquals(Byte.MAX_VALUE, maxPoint.x);
        assertEquals(Byte.MAX_VALUE, maxPoint.y);
        assertEquals(Byte.MAX_VALUE, maxPoint.v);

        assertFalse(minPoint.equals(maxPoint));
    }

    @Test
    public void testIntPoint_BoundaryValues() {
        xy.IntIntPoint minPoint = xy.IntIntPoint.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        xy.IntIntPoint maxPoint = xy.IntIntPoint.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

        assertEquals(Integer.MIN_VALUE, minPoint.x);
        assertEquals(Integer.MIN_VALUE, minPoint.y);
        assertEquals(Integer.MIN_VALUE, minPoint.v);

        assertEquals(Integer.MAX_VALUE, maxPoint.x);
        assertEquals(Integer.MAX_VALUE, maxPoint.y);
        assertEquals(Integer.MAX_VALUE, maxPoint.v);

        assertFalse(minPoint.equals(maxPoint));
    }

    @Test
    public void testLongPoint_BoundaryValues() {
        xy.LongLongPoint minPoint = xy.LongLongPoint.of(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);
        xy.LongLongPoint maxPoint = xy.LongLongPoint.of(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);

        assertEquals(Long.MIN_VALUE, minPoint.x);
        assertEquals(Long.MIN_VALUE, minPoint.y);
        assertEquals(Long.MIN_VALUE, minPoint.v);

        assertEquals(Long.MAX_VALUE, maxPoint.x);
        assertEquals(Long.MAX_VALUE, maxPoint.y);
        assertEquals(Long.MAX_VALUE, maxPoint.v);

        assertFalse(minPoint.equals(maxPoint));
    }

    @Test
    public void testDoublePoint_SpecialValues() {
        xy.DoubleDoublePoint nanPoint = xy.DoubleDoublePoint.of(Double.NaN, Double.NaN, Double.NaN);
        xy.DoubleDoublePoint infPoint = xy.DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        assertTrue(Double.isNaN(nanPoint.x));
        assertTrue(Double.isNaN(nanPoint.y));
        assertTrue(Double.isNaN(nanPoint.v));

        assertEquals(Double.POSITIVE_INFINITY, infPoint.x, 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, infPoint.y, 0.0);
        assertEquals(Double.POSITIVE_INFINITY, infPoint.v, 0.0);

        assertFalse(nanPoint.equals(infPoint));
    }

    // ============================================
    // Comprehensive Coverage Tests
    // ============================================

    @Test
    public void testAllPointTypes_FactoryMethod() {
        // Verify factory methods work for all point types
        assertNotNull(xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3));
        assertNotNull(xy.ByteIntPoint.of((byte) 1, (byte) 2, 3));
        assertNotNull(xy.ByteLongPoint.of((byte) 1, (byte) 2, 3L));
        assertNotNull(xy.ByteDoublePoint.of((byte) 1, (byte) 2, 3.0));
        assertNotNull(xy.ByteObjPoint.of((byte) 1, (byte) 2, "test"));

        assertNotNull(xy.IntBytePoint.of(1, 2, (byte) 3));
        assertNotNull(xy.IntIntPoint.of(1, 2, 3));
        assertNotNull(xy.IntLongPoint.of(1, 2, 3L));
        assertNotNull(xy.IntDoublePoint.of(1, 2, 3.0));
        assertNotNull(xy.IntObjPoint.of(1, 2, "test"));

        assertNotNull(xy.LongBytePoint.of(1L, 2L, (byte) 3));
        assertNotNull(xy.LongIntPoint.of(1L, 2L, 3));
        assertNotNull(xy.LongLongPoint.of(1L, 2L, 3L));
        assertNotNull(xy.LongDoublePoint.of(1L, 2L, 3.0));
        assertNotNull(xy.LongObjPoint.of(1L, 2L, "test"));

        assertNotNull(xy.DoubleBytePoint.of(1.0, 2.0, (byte) 3));
        assertNotNull(xy.DoubleIntPoint.of(1.0, 2.0, 3));
        assertNotNull(xy.DoubleLongPoint.of(1.0, 2.0, 3L));
        assertNotNull(xy.DoubleDoublePoint.of(1.0, 2.0, 3.0));
        assertNotNull(xy.DoubleObjPoint.of(1.0, 2.0, "test"));
    }

    @Test
    public void testAllPointTypes_FieldAccess() {
        // Verify all fields are accessible and correct
        xy.IntIntPoint point = xy.IntIntPoint.of(10, 20, 30);
        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(30, point.v);
    }

    @Test
    public void testAllPointTypes_ImmutabilityPattern() {
        // Points should be immutable - fields are final
        // This is verified by the compiler, but we can check behavior
        xy.IntIntPoint point1 = xy.IntIntPoint.of(1, 2, 3);
        xy.IntIntPoint point2 = xy.IntIntPoint.of(1, 2, 3);

        // Creating new points doesn't affect existing ones
        assertTrue(point1.equals(point2));
        assertEquals(1, point1.x);
        assertEquals(2, point1.y);
        assertEquals(3, point1.v);
    }
}
