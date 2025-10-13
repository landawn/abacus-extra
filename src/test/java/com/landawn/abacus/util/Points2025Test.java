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
 * Tests cover all point types with different coordinate and value combinations,
 * verifying factory methods, field access, hashCode, equals, and toString methods.
 */
@Tag("2025")
public class Points2025Test extends TestBase {

    // ============================================
    // Tests for ByteBytePoint
    // ============================================

    @Test
    public void testByteBytePoint_Of() {
        xy.ByteBytePoint point = xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 100);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(100, point.v);
    }

    @Test
    public void testByteBytePoint_HashCode() {
        xy.ByteBytePoint point1 = xy.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 15);
        xy.ByteBytePoint point2 = xy.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 15);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testByteBytePoint_Equals() {
        xy.ByteBytePoint point1 = xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
        xy.ByteBytePoint point2 = xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
        xy.ByteBytePoint point3 = xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 4);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertTrue(point1.equals(point1));
    }

    @Test
    public void testByteBytePoint_ToString() {
        xy.ByteBytePoint point = xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        String result = point.toString();

        assertNotNull(result);
        assertTrue(result.contains("10"));
        assertTrue(result.contains("20"));
        assertTrue(result.contains("30"));
    }

    // ============================================
    // Tests for ByteIntPoint
    // ============================================

    @Test
    public void testByteIntPoint_Of() {
        xy.ByteIntPoint point = xy.ByteIntPoint.of((byte) 10, (byte) 20, 100);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(100, point.v);
    }

    @Test
    public void testByteIntPoint_HashCode() {
        xy.ByteIntPoint point1 = xy.ByteIntPoint.of((byte) 5, (byte) 10, 500);
        xy.ByteIntPoint point2 = xy.ByteIntPoint.of((byte) 5, (byte) 10, 500);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testByteIntPoint_Equals() {
        xy.ByteIntPoint point1 = xy.ByteIntPoint.of((byte) 1, (byte) 2, 300);
        xy.ByteIntPoint point2 = xy.ByteIntPoint.of((byte) 1, (byte) 2, 300);
        xy.ByteIntPoint point3 = xy.ByteIntPoint.of((byte) 1, (byte) 2, 400);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testByteIntPoint_ToString() {
        xy.ByteIntPoint point = xy.ByteIntPoint.of((byte) 10, (byte) 20, 999);
        String result = point.toString();

        assertNotNull(result);
        assertTrue(result.contains("999"));
    }

    // ============================================
    // Tests for ByteLongPoint
    // ============================================

    @Test
    public void testByteLongPoint_Of() {
        xy.ByteLongPoint point = xy.ByteLongPoint.of((byte) 10, (byte) 20, 1000L);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(1000L, point.v);
    }

    @Test
    public void testByteLongPoint_HashCode() {
        xy.ByteLongPoint point1 = xy.ByteLongPoint.of((byte) 5, (byte) 10, 5000L);
        xy.ByteLongPoint point2 = xy.ByteLongPoint.of((byte) 5, (byte) 10, 5000L);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testByteLongPoint_Equals() {
        xy.ByteLongPoint point1 = xy.ByteLongPoint.of((byte) 1, (byte) 2, 3000L);
        xy.ByteLongPoint point2 = xy.ByteLongPoint.of((byte) 1, (byte) 2, 3000L);
        xy.ByteLongPoint point3 = xy.ByteLongPoint.of((byte) 1, (byte) 2, 4000L);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testByteLongPoint_ToString() {
        xy.ByteLongPoint point = xy.ByteLongPoint.of((byte) 10, (byte) 20, 99999L);
        String result = point.toString();

        assertNotNull(result);
        assertTrue(result.contains("99999"));
    }

    // ============================================
    // Tests for ByteDoublePoint
    // ============================================

    @Test
    public void testByteDoublePoint_Of() {
        xy.ByteDoublePoint point = xy.ByteDoublePoint.of((byte) 10, (byte) 20, 100.5);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(100.5, point.v, 0.0001);
    }

    @Test
    public void testByteDoublePoint_HashCode() {
        xy.ByteDoublePoint point1 = xy.ByteDoublePoint.of((byte) 5, (byte) 10, 50.5);
        xy.ByteDoublePoint point2 = xy.ByteDoublePoint.of((byte) 5, (byte) 10, 50.5);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testByteDoublePoint_Equals() {
        xy.ByteDoublePoint point1 = xy.ByteDoublePoint.of((byte) 1, (byte) 2, 3.14);
        xy.ByteDoublePoint point2 = xy.ByteDoublePoint.of((byte) 1, (byte) 2, 3.14);
        xy.ByteDoublePoint point3 = xy.ByteDoublePoint.of((byte) 1, (byte) 2, 2.71);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testByteDoublePoint_ToString() {
        xy.ByteDoublePoint point = xy.ByteDoublePoint.of((byte) 10, (byte) 20, 99.99);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for ByteObjPoint
    // ============================================

    @Test
    public void testByteObjPoint_Of() {
        xy.ByteObjPoint<String> point = xy.ByteObjPoint.of((byte) 10, (byte) 20, "Test");

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals("Test", point.v);
    }

    @Test
    public void testByteObjPoint_HashCode() {
        xy.ByteObjPoint<String> point1 = xy.ByteObjPoint.of((byte) 5, (byte) 10, "Value");
        xy.ByteObjPoint<String> point2 = xy.ByteObjPoint.of((byte) 5, (byte) 10, "Value");

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testByteObjPoint_Equals() {
        xy.ByteObjPoint<String> point1 = xy.ByteObjPoint.of((byte) 1, (byte) 2, "ABC");
        xy.ByteObjPoint<String> point2 = xy.ByteObjPoint.of((byte) 1, (byte) 2, "ABC");
        xy.ByteObjPoint<String> point3 = xy.ByteObjPoint.of((byte) 1, (byte) 2, "XYZ");

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testByteObjPoint_ToString() {
        xy.ByteObjPoint<Integer> point = xy.ByteObjPoint.of((byte) 10, (byte) 20, 999);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for IntBytePoint
    // ============================================

    @Test
    public void testIntBytePoint_Of() {
        xy.IntBytePoint point = xy.IntBytePoint.of(100, 200, (byte) 50);

        assertEquals(100, point.x);
        assertEquals(200, point.y);
        assertEquals(50, point.v);
    }

    @Test
    public void testIntBytePoint_HashCode() {
        xy.IntBytePoint point1 = xy.IntBytePoint.of(10, 20, (byte) 30);
        xy.IntBytePoint point2 = xy.IntBytePoint.of(10, 20, (byte) 30);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testIntBytePoint_Equals() {
        xy.IntBytePoint point1 = xy.IntBytePoint.of(1, 2, (byte) 3);
        xy.IntBytePoint point2 = xy.IntBytePoint.of(1, 2, (byte) 3);
        xy.IntBytePoint point3 = xy.IntBytePoint.of(1, 2, (byte) 4);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testIntBytePoint_ToString() {
        xy.IntBytePoint point = xy.IntBytePoint.of(100, 200, (byte) 99);
        String result = point.toString();

        assertNotNull(result);
        assertTrue(result.contains("100"));
    }

    // ============================================
    // Tests for IntIntPoint
    // ============================================

    @Test
    public void testIntIntPoint_Of() {
        xy.IntIntPoint point = xy.IntIntPoint.of(10, 20, 100);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(100, point.v);
    }

    @Test
    public void testIntIntPoint_HashCode() {
        xy.IntIntPoint point1 = xy.IntIntPoint.of(5, 10, 15);
        xy.IntIntPoint point2 = xy.IntIntPoint.of(5, 10, 15);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testIntIntPoint_Equals() {
        xy.IntIntPoint point1 = xy.IntIntPoint.of(1, 2, 3);
        xy.IntIntPoint point2 = xy.IntIntPoint.of(1, 2, 3);
        xy.IntIntPoint point3 = xy.IntIntPoint.of(1, 2, 4);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testIntIntPoint_ToString() {
        xy.IntIntPoint point = xy.IntIntPoint.of(100, 200, 300);
        String result = point.toString();

        assertNotNull(result);
        assertTrue(result.contains("100"));
        assertTrue(result.contains("200"));
        assertTrue(result.contains("300"));
    }

    @Test
    public void testIntIntPoint_NegativeValues() {
        xy.IntIntPoint point = xy.IntIntPoint.of(-10, -20, -30);

        assertEquals(-10, point.x);
        assertEquals(-20, point.y);
        assertEquals(-30, point.v);
    }

    // ============================================
    // Tests for IntLongPoint
    // ============================================

    @Test
    public void testIntLongPoint_Of() {
        xy.IntLongPoint point = xy.IntLongPoint.of(10, 20, 1000L);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(1000L, point.v);
    }

    @Test
    public void testIntLongPoint_HashCode() {
        xy.IntLongPoint point1 = xy.IntLongPoint.of(5, 10, 5000L);
        xy.IntLongPoint point2 = xy.IntLongPoint.of(5, 10, 5000L);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testIntLongPoint_Equals() {
        xy.IntLongPoint point1 = xy.IntLongPoint.of(1, 2, 3000L);
        xy.IntLongPoint point2 = xy.IntLongPoint.of(1, 2, 3000L);
        xy.IntLongPoint point3 = xy.IntLongPoint.of(1, 2, 4000L);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testIntLongPoint_ToString() {
        xy.IntLongPoint point = xy.IntLongPoint.of(100, 200, 999999L);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for IntDoublePoint
    // ============================================

    @Test
    public void testIntDoublePoint_Of() {
        xy.IntDoublePoint point = xy.IntDoublePoint.of(10, 20, 100.5);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(100.5, point.v, 0.0001);
    }

    @Test
    public void testIntDoublePoint_HashCode() {
        xy.IntDoublePoint point1 = xy.IntDoublePoint.of(5, 10, 50.5);
        xy.IntDoublePoint point2 = xy.IntDoublePoint.of(5, 10, 50.5);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testIntDoublePoint_Equals() {
        xy.IntDoublePoint point1 = xy.IntDoublePoint.of(1, 2, 3.14);
        xy.IntDoublePoint point2 = xy.IntDoublePoint.of(1, 2, 3.14);
        xy.IntDoublePoint point3 = xy.IntDoublePoint.of(1, 2, 2.71);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testIntDoublePoint_ToString() {
        xy.IntDoublePoint point = xy.IntDoublePoint.of(100, 200, 99.99);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for IntObjPoint
    // ============================================

    @Test
    public void testIntObjPoint_Of() {
        xy.IntObjPoint<String> point = xy.IntObjPoint.of(10, 20, "TestValue");

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals("TestValue", point.v);
    }

    @Test
    public void testIntObjPoint_HashCode() {
        xy.IntObjPoint<String> point1 = xy.IntObjPoint.of(5, 10, "ABC");
        xy.IntObjPoint<String> point2 = xy.IntObjPoint.of(5, 10, "ABC");

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testIntObjPoint_Equals() {
        xy.IntObjPoint<String> point1 = xy.IntObjPoint.of(1, 2, "Value");
        xy.IntObjPoint<String> point2 = xy.IntObjPoint.of(1, 2, "Value");
        xy.IntObjPoint<String> point3 = xy.IntObjPoint.of(1, 2, "Other");

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testIntObjPoint_ToString() {
        xy.IntObjPoint<Integer> point = xy.IntObjPoint.of(100, 200, 999);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for LongBytePoint
    // ============================================

    @Test
    public void testLongBytePoint_Of() {
        xy.LongBytePoint point = xy.LongBytePoint.of(1000L, 2000L, (byte) 50);

        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals(50, point.v);
    }

    @Test
    public void testLongBytePoint_HashCode() {
        xy.LongBytePoint point1 = xy.LongBytePoint.of(100L, 200L, (byte) 30);
        xy.LongBytePoint point2 = xy.LongBytePoint.of(100L, 200L, (byte) 30);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testLongBytePoint_Equals() {
        xy.LongBytePoint point1 = xy.LongBytePoint.of(10L, 20L, (byte) 3);
        xy.LongBytePoint point2 = xy.LongBytePoint.of(10L, 20L, (byte) 3);
        xy.LongBytePoint point3 = xy.LongBytePoint.of(10L, 20L, (byte) 4);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testLongBytePoint_ToString() {
        xy.LongBytePoint point = xy.LongBytePoint.of(1000L, 2000L, (byte) 99);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for LongIntPoint
    // ============================================

    @Test
    public void testLongIntPoint_Of() {
        xy.LongIntPoint point = xy.LongIntPoint.of(1000L, 2000L, 100);

        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals(100, point.v);
    }

    @Test
    public void testLongIntPoint_HashCode() {
        xy.LongIntPoint point1 = xy.LongIntPoint.of(100L, 200L, 300);
        xy.LongIntPoint point2 = xy.LongIntPoint.of(100L, 200L, 300);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testLongIntPoint_Equals() {
        xy.LongIntPoint point1 = xy.LongIntPoint.of(10L, 20L, 30);
        xy.LongIntPoint point2 = xy.LongIntPoint.of(10L, 20L, 30);
        xy.LongIntPoint point3 = xy.LongIntPoint.of(10L, 20L, 40);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testLongIntPoint_ToString() {
        xy.LongIntPoint point = xy.LongIntPoint.of(1000L, 2000L, 999);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for LongLongPoint
    // ============================================

    @Test
    public void testLongLongPoint_Of() {
        xy.LongLongPoint point = xy.LongLongPoint.of(1000L, 2000L, 3000L);

        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals(3000L, point.v);
    }

    @Test
    public void testLongLongPoint_HashCode() {
        xy.LongLongPoint point1 = xy.LongLongPoint.of(100L, 200L, 300L);
        xy.LongLongPoint point2 = xy.LongLongPoint.of(100L, 200L, 300L);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testLongLongPoint_Equals() {
        xy.LongLongPoint point1 = xy.LongLongPoint.of(10L, 20L, 30L);
        xy.LongLongPoint point2 = xy.LongLongPoint.of(10L, 20L, 30L);
        xy.LongLongPoint point3 = xy.LongLongPoint.of(10L, 20L, 40L);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testLongLongPoint_ToString() {
        xy.LongLongPoint point = xy.LongLongPoint.of(1000L, 2000L, 3000L);
        String result = point.toString();

        assertNotNull(result);
        assertTrue(result.contains("1000"));
    }

    // ============================================
    // Tests for LongDoublePoint
    // ============================================

    @Test
    public void testLongDoublePoint_Of() {
        xy.LongDoublePoint point = xy.LongDoublePoint.of(1000L, 2000L, 100.5);

        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals(100.5, point.v, 0.0001);
    }

    @Test
    public void testLongDoublePoint_HashCode() {
        xy.LongDoublePoint point1 = xy.LongDoublePoint.of(100L, 200L, 50.5);
        xy.LongDoublePoint point2 = xy.LongDoublePoint.of(100L, 200L, 50.5);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testLongDoublePoint_Equals() {
        xy.LongDoublePoint point1 = xy.LongDoublePoint.of(10L, 20L, 3.14);
        xy.LongDoublePoint point2 = xy.LongDoublePoint.of(10L, 20L, 3.14);
        xy.LongDoublePoint point3 = xy.LongDoublePoint.of(10L, 20L, 2.71);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testLongDoublePoint_ToString() {
        xy.LongDoublePoint point = xy.LongDoublePoint.of(1000L, 2000L, 99.99);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for LongObjPoint
    // ============================================

    @Test
    public void testLongObjPoint_Of() {
        xy.LongObjPoint<String> point = xy.LongObjPoint.of(1000L, 2000L, "TestValue");

        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals("TestValue", point.v);
    }

    @Test
    public void testLongObjPoint_HashCode() {
        xy.LongObjPoint<String> point1 = xy.LongObjPoint.of(100L, 200L, "ABC");
        xy.LongObjPoint<String> point2 = xy.LongObjPoint.of(100L, 200L, "ABC");

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testLongObjPoint_Equals() {
        xy.LongObjPoint<String> point1 = xy.LongObjPoint.of(10L, 20L, "Value");
        xy.LongObjPoint<String> point2 = xy.LongObjPoint.of(10L, 20L, "Value");
        xy.LongObjPoint<String> point3 = xy.LongObjPoint.of(10L, 20L, "Other");

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testLongObjPoint_ToString() {
        xy.LongObjPoint<Integer> point = xy.LongObjPoint.of(1000L, 2000L, 999);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for DoubleBytePoint
    // ============================================

    @Test
    public void testDoubleBytePoint_Of() {
        xy.DoubleBytePoint point = xy.DoubleBytePoint.of(10.5, 20.5, (byte) 50);

        assertEquals(10.5, point.x, 0.0001);
        assertEquals(20.5, point.y, 0.0001);
        assertEquals(50, point.v);
    }

    @Test
    public void testDoubleBytePoint_HashCode() {
        xy.DoubleBytePoint point1 = xy.DoubleBytePoint.of(10.5, 20.5, (byte) 30);
        xy.DoubleBytePoint point2 = xy.DoubleBytePoint.of(10.5, 20.5, (byte) 30);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testDoubleBytePoint_Equals() {
        xy.DoubleBytePoint point1 = xy.DoubleBytePoint.of(1.5, 2.5, (byte) 3);
        xy.DoubleBytePoint point2 = xy.DoubleBytePoint.of(1.5, 2.5, (byte) 3);
        xy.DoubleBytePoint point3 = xy.DoubleBytePoint.of(1.5, 2.5, (byte) 4);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testDoubleBytePoint_ToString() {
        xy.DoubleBytePoint point = xy.DoubleBytePoint.of(10.5, 20.5, (byte) 99);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for DoubleIntPoint
    // ============================================

    @Test
    public void testDoubleIntPoint_Of() {
        xy.DoubleIntPoint point = xy.DoubleIntPoint.of(10.5, 20.5, 100);

        assertEquals(10.5, point.x, 0.0001);
        assertEquals(20.5, point.y, 0.0001);
        assertEquals(100, point.v);
    }

    @Test
    public void testDoubleIntPoint_HashCode() {
        xy.DoubleIntPoint point1 = xy.DoubleIntPoint.of(10.5, 20.5, 300);
        xy.DoubleIntPoint point2 = xy.DoubleIntPoint.of(10.5, 20.5, 300);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testDoubleIntPoint_Equals() {
        xy.DoubleIntPoint point1 = xy.DoubleIntPoint.of(1.5, 2.5, 30);
        xy.DoubleIntPoint point2 = xy.DoubleIntPoint.of(1.5, 2.5, 30);
        xy.DoubleIntPoint point3 = xy.DoubleIntPoint.of(1.5, 2.5, 40);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testDoubleIntPoint_ToString() {
        xy.DoubleIntPoint point = xy.DoubleIntPoint.of(10.5, 20.5, 999);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for DoubleLongPoint
    // ============================================

    @Test
    public void testDoubleLongPoint_Of() {
        xy.DoubleLongPoint point = xy.DoubleLongPoint.of(10.5, 20.5, 1000L);

        assertEquals(10.5, point.x, 0.0001);
        assertEquals(20.5, point.y, 0.0001);
        assertEquals(1000L, point.v);
    }

    @Test
    public void testDoubleLongPoint_HashCode() {
        xy.DoubleLongPoint point1 = xy.DoubleLongPoint.of(10.5, 20.5, 3000L);
        xy.DoubleLongPoint point2 = xy.DoubleLongPoint.of(10.5, 20.5, 3000L);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testDoubleLongPoint_Equals() {
        xy.DoubleLongPoint point1 = xy.DoubleLongPoint.of(1.5, 2.5, 300L);
        xy.DoubleLongPoint point2 = xy.DoubleLongPoint.of(1.5, 2.5, 300L);
        xy.DoubleLongPoint point3 = xy.DoubleLongPoint.of(1.5, 2.5, 400L);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testDoubleLongPoint_ToString() {
        xy.DoubleLongPoint point = xy.DoubleLongPoint.of(10.5, 20.5, 999999L);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Tests for DoubleDoublePoint
    // ============================================

    @Test
    public void testDoubleDoublePoint_Of() {
        xy.DoubleDoublePoint point = xy.DoubleDoublePoint.of(10.5, 20.5, 100.5);

        assertEquals(10.5, point.x, 0.0001);
        assertEquals(20.5, point.y, 0.0001);
        assertEquals(100.5, point.v, 0.0001);
    }

    @Test
    public void testDoubleDoublePoint_HashCode() {
        xy.DoubleDoublePoint point1 = xy.DoubleDoublePoint.of(10.5, 20.5, 50.5);
        xy.DoubleDoublePoint point2 = xy.DoubleDoublePoint.of(10.5, 20.5, 50.5);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testDoubleDoublePoint_Equals() {
        xy.DoubleDoublePoint point1 = xy.DoubleDoublePoint.of(1.5, 2.5, 3.14);
        xy.DoubleDoublePoint point2 = xy.DoubleDoublePoint.of(1.5, 2.5, 3.14);
        xy.DoubleDoublePoint point3 = xy.DoubleDoublePoint.of(1.5, 2.5, 2.71);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testDoubleDoublePoint_ToString() {
        xy.DoubleDoublePoint point = xy.DoubleDoublePoint.of(10.5, 20.5, 99.99);
        String result = point.toString();

        assertNotNull(result);
        assertTrue(result.contains("10.5"));
    }

    // ============================================
    // Tests for DoubleObjPoint
    // ============================================

    @Test
    public void testDoubleObjPoint_Of() {
        xy.DoubleObjPoint<String> point = xy.DoubleObjPoint.of(10.5, 20.5, "TestValue");

        assertEquals(10.5, point.x, 0.0001);
        assertEquals(20.5, point.y, 0.0001);
        assertEquals("TestValue", point.v);
    }

    @Test
    public void testDoubleObjPoint_HashCode() {
        xy.DoubleObjPoint<String> point1 = xy.DoubleObjPoint.of(10.5, 20.5, "ABC");
        xy.DoubleObjPoint<String> point2 = xy.DoubleObjPoint.of(10.5, 20.5, "ABC");

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testDoubleObjPoint_Equals() {
        xy.DoubleObjPoint<String> point1 = xy.DoubleObjPoint.of(1.5, 2.5, "Value");
        xy.DoubleObjPoint<String> point2 = xy.DoubleObjPoint.of(1.5, 2.5, "Value");
        xy.DoubleObjPoint<String> point3 = xy.DoubleObjPoint.of(1.5, 2.5, "Other");

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testDoubleObjPoint_ToString() {
        xy.DoubleObjPoint<Integer> point = xy.DoubleObjPoint.of(10.5, 20.5, 999);
        String result = point.toString();

        assertNotNull(result);
    }

    // ============================================
    // Edge case and integration tests
    // ============================================

    @Test
    public void testEquals_Null() {
        xy.IntIntPoint point = xy.IntIntPoint.of(1, 2, 3);

        assertFalse(point.equals(null));
    }

    @Test
    public void testEquals_DifferentType() {
        xy.IntIntPoint point = xy.IntIntPoint.of(1, 2, 3);
        String notAPoint = "not a point";

        assertFalse(point.equals(notAPoint));
    }

    @Test
    public void testEquals_SameInstance() {
        xy.IntIntPoint point = xy.IntIntPoint.of(10, 20, 30);

        assertTrue(point.equals(point));
    }

    @Test
    public void testHashCode_DifferentPoints() {
        xy.IntIntPoint point1 = xy.IntIntPoint.of(1, 2, 3);
        xy.IntIntPoint point2 = xy.IntIntPoint.of(4, 5, 6);

        assertNotEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testIntIntPoint_ZeroValues() {
        xy.IntIntPoint point = xy.IntIntPoint.of(0, 0, 0);

        assertEquals(0, point.x);
        assertEquals(0, point.y);
        assertEquals(0, point.v);
    }

    @Test
    public void testDoubleDoublePoint_SpecialValues() {
        xy.DoubleDoublePoint point1 = xy.DoubleDoublePoint.of(Double.MAX_VALUE, Double.MIN_VALUE, 0.0);

        assertEquals(Double.MAX_VALUE, point1.x, 0.0001);
        assertEquals(Double.MIN_VALUE, point1.y, 0.0001);
        assertEquals(0.0, point1.v, 0.0001);
    }

    @Test
    public void testObjPoint_NullValue() {
        xy.IntObjPoint<String> point = xy.IntObjPoint.of(10, 20, null);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(null, point.v);
    }

    @Test
    public void testObjPoint_ComplexObject() {
        java.util.List<Integer> list = java.util.Arrays.asList(1, 2, 3);
        xy.IntObjPoint<java.util.List<Integer>> point = xy.IntObjPoint.of(10, 20, list);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(list, point.v);
    }

    @Test
    public void testMultiplePointTypes_SameCoordinates() {
        // Create points with same coordinates but different types
        xy.IntIntPoint intPoint = xy.IntIntPoint.of(10, 20, 100);
        xy.LongLongPoint longPoint = xy.LongLongPoint.of(10L, 20L, 100L);
        xy.DoubleDoublePoint doublePoint = xy.DoubleDoublePoint.of(10.0, 20.0, 100.0);

        // Verify they don't equal each other (different types)
        assertFalse(intPoint.equals(longPoint));
        assertFalse(intPoint.equals(doublePoint));
        assertFalse(longPoint.equals(doublePoint));
    }
}
