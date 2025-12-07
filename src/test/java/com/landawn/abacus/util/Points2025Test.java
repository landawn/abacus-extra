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
import com.landawn.abacus.util.Points.xyz;

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

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(100, point.v());
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

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(100, point.v());
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

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(1000L, point.v());
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

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(100.5, point.v(), 0.0001);
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

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals("Test", point.v());
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

        assertEquals(100, point.x());
        assertEquals(200, point.y());
        assertEquals(50, point.v());
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

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(100, point.v());
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

        assertEquals(-10, point.x());
        assertEquals(-20, point.y());
        assertEquals(-30, point.v());
    }

    // ============================================
    // Tests for IntLongPoint
    // ============================================

    @Test
    public void testIntLongPoint_Of() {
        xy.IntLongPoint point = xy.IntLongPoint.of(10, 20, 1000L);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(1000L, point.v());
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

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(100.5, point.v(), 0.0001);
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

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals("TestValue", point.v());
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

        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(50, point.v());
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

        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(100, point.v());
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

        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(3000L, point.v());
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

        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(100.5, point.v(), 0.0001);
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

        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals("TestValue", point.v());
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

        assertEquals(10.5, point.x(), 0.0001);
        assertEquals(20.5, point.y(), 0.0001);
        assertEquals(50, point.v());
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

        assertEquals(10.5, point.x(), 0.0001);
        assertEquals(20.5, point.y(), 0.0001);
        assertEquals(100, point.v());
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

        assertEquals(10.5, point.x(), 0.0001);
        assertEquals(20.5, point.y(), 0.0001);
        assertEquals(1000L, point.v());
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

        assertEquals(10.5, point.x(), 0.0001);
        assertEquals(20.5, point.y(), 0.0001);
        assertEquals(100.5, point.v(), 0.0001);
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

        assertEquals(10.5, point.x(), 0.0001);
        assertEquals(20.5, point.y(), 0.0001);
        assertEquals("TestValue", point.v());
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

        assertEquals(0, point.x());
        assertEquals(0, point.y());
        assertEquals(0, point.v());
    }

    @Test
    public void testDoubleDoublePoint_SpecialValues() {
        xy.DoubleDoublePoint point1 = xy.DoubleDoublePoint.of(Double.MAX_VALUE, Double.MIN_VALUE, 0.0);

        assertEquals(Double.MAX_VALUE, point1.x(), 0.0001);
        assertEquals(Double.MIN_VALUE, point1.y(), 0.0001);
        assertEquals(0.0, point1.v(), 0.0001);
    }

    @Test
    public void testObjPoint_NullValue() {
        xy.IntObjPoint<String> point = xy.IntObjPoint.of(10, 20, null);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(null, point.v());
    }

    @Test
    public void testObjPoint_ComplexObject() {
        java.util.List<Integer> list = java.util.Arrays.asList(1, 2, 3);
        xy.IntObjPoint<java.util.List<Integer>> point = xy.IntObjPoint.of(10, 20, list);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(list, point.v());
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

    // ============================================
    // Tests for xyz.ByteBytePoint (3D)
    // ============================================

    @Test
    public void testXyzByteBytePoint_Of() {
        xyz.ByteBytePoint point = xyz.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);

        assertEquals(1, point.x());
        assertEquals(2, point.y());
        assertEquals(3, point.z());
        assertEquals(4, point.v());
    }

    @Test
    public void testXyzByteBytePoint_HashCode() {
        xyz.ByteBytePoint point1 = xyz.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        xyz.ByteBytePoint point2 = xyz.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzByteBytePoint_Equals() {
        xyz.ByteBytePoint point1 = xyz.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        xyz.ByteBytePoint point2 = xyz.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        xyz.ByteBytePoint point3 = xyz.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 5);
        xyz.ByteBytePoint point4 = xyz.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 4, (byte) 4);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(point4));
        assertFalse(point1.equals(null));
        assertTrue(point1.equals(point1));
    }

    @Test
    public void testXyzByteBytePoint_ToString() {
        xyz.ByteBytePoint point = xyz.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        String str = point.toString();

        assertEquals("ByteBytePoint[x=1, y=2, z=3, v=4]", str);
    }

    // ============================================
    // Tests for xyz.ByteIntPoint (3D)
    // ============================================

    @Test
    public void testXyzByteIntPoint_Of() {
        xyz.ByteIntPoint point = xyz.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);

        assertEquals(1, point.x());
        assertEquals(2, point.y());
        assertEquals(3, point.z());
        assertEquals(100, point.v());
    }

    @Test
    public void testXyzByteIntPoint_HashCode() {
        xyz.ByteIntPoint point1 = xyz.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);
        xyz.ByteIntPoint point2 = xyz.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzByteIntPoint_Equals() {
        xyz.ByteIntPoint point1 = xyz.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);
        xyz.ByteIntPoint point2 = xyz.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);
        xyz.ByteIntPoint point3 = xyz.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 101);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzByteIntPoint_ToString() {
        xyz.ByteIntPoint point = xyz.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);
        assertEquals("ByteIntPoint[x=1, y=2, z=3, v=100]", point.toString());
    }

    // ============================================
    // Tests for xyz.ByteLongPoint (3D)
    // ============================================

    @Test
    public void testXyzByteLongPoint_Of() {
        xyz.ByteLongPoint point = xyz.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);

        assertEquals(1, point.x());
        assertEquals(2, point.y());
        assertEquals(3, point.z());
        assertEquals(1000L, point.v());
    }

    @Test
    public void testXyzByteLongPoint_HashCode() {
        xyz.ByteLongPoint point1 = xyz.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);
        xyz.ByteLongPoint point2 = xyz.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzByteLongPoint_Equals() {
        xyz.ByteLongPoint point1 = xyz.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);
        xyz.ByteLongPoint point2 = xyz.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);
        xyz.ByteLongPoint point3 = xyz.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1001L);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzByteLongPoint_ToString() {
        xyz.ByteLongPoint point = xyz.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);
        assertEquals("ByteLongPoint[x=1, y=2, z=3, v=1000]", point.toString());
    }

    // ============================================
    // Tests for xyz.ByteDoublePoint (3D)
    // ============================================

    @Test
    public void testXyzByteDoublePoint_Of() {
        xyz.ByteDoublePoint point = xyz.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);

        assertEquals(1, point.x());
        assertEquals(2, point.y());
        assertEquals(3, point.z());
        assertEquals(10.5, point.v(), 0.001);
    }

    @Test
    public void testXyzByteDoublePoint_HashCode() {
        xyz.ByteDoublePoint point1 = xyz.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);
        xyz.ByteDoublePoint point2 = xyz.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzByteDoublePoint_Equals() {
        xyz.ByteDoublePoint point1 = xyz.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);
        xyz.ByteDoublePoint point2 = xyz.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);
        xyz.ByteDoublePoint point3 = xyz.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.6);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzByteDoublePoint_ToString() {
        xyz.ByteDoublePoint point = xyz.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);
        assertTrue(point.toString().contains("10.5"));
    }

    // ============================================
    // Tests for xyz.ByteObjPoint (3D)
    // ============================================

    @Test
    public void testXyzByteObjPoint_Of() {
        xyz.ByteObjPoint<String> point = xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "test");

        assertEquals(1, point.x());
        assertEquals(2, point.y());
        assertEquals(3, point.z());
        assertEquals("test", point.v());
    }

    @Test
    public void testXyzByteObjPoint_HashCode() {
        xyz.ByteObjPoint<String> point1 = xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "hello");
        xyz.ByteObjPoint<String> point2 = xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "hello");

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzByteObjPoint_Equals() {
        xyz.ByteObjPoint<String> point1 = xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "hello");
        xyz.ByteObjPoint<String> point2 = xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "hello");
        xyz.ByteObjPoint<String> point3 = xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "world");
        xyz.ByteObjPoint<String> point4 = xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, null);
        xyz.ByteObjPoint<String> point5 = xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, null);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(point4));
        assertTrue(point4.equals(point5));
    }

    @Test
    public void testXyzByteObjPoint_ToString() {
        xyz.ByteObjPoint<String> point = xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "test");
        assertTrue(point.toString().contains("test"));
    }

    // ============================================
    // Tests for xyz.IntBytePoint (3D)
    // ============================================

    @Test
    public void testXyzIntBytePoint_Of() {
        xyz.IntBytePoint point = xyz.IntBytePoint.of(100, 200, 300, (byte) 50);

        assertEquals(100, point.x());
        assertEquals(200, point.y());
        assertEquals(300, point.z());
        assertEquals(50, point.v());
    }

    @Test
    public void testXyzIntBytePoint_HashCode() {
        xyz.IntBytePoint point1 = xyz.IntBytePoint.of(100, 200, 300, (byte) 50);
        xyz.IntBytePoint point2 = xyz.IntBytePoint.of(100, 200, 300, (byte) 50);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzIntBytePoint_Equals() {
        xyz.IntBytePoint point1 = xyz.IntBytePoint.of(100, 200, 300, (byte) 50);
        xyz.IntBytePoint point2 = xyz.IntBytePoint.of(100, 200, 300, (byte) 50);
        xyz.IntBytePoint point3 = xyz.IntBytePoint.of(100, 200, 300, (byte) 51);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzIntBytePoint_ToString() {
        xyz.IntBytePoint point = xyz.IntBytePoint.of(100, 200, 300, (byte) 50);
        assertEquals("IntBytePoint[x=100, y=200, z=300, v=50]", point.toString());
    }

    // ============================================
    // Tests for xyz.IntIntPoint (3D)
    // ============================================

    @Test
    public void testXyzIntIntPoint_Of() {
        xyz.IntIntPoint point = xyz.IntIntPoint.of(10, 20, 30, 40);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(30, point.z());
        assertEquals(40, point.v());
    }

    @Test
    public void testXyzIntIntPoint_HashCode() {
        xyz.IntIntPoint point1 = xyz.IntIntPoint.of(10, 20, 30, 40);
        xyz.IntIntPoint point2 = xyz.IntIntPoint.of(10, 20, 30, 40);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzIntIntPoint_Equals() {
        xyz.IntIntPoint point1 = xyz.IntIntPoint.of(10, 20, 30, 40);
        xyz.IntIntPoint point2 = xyz.IntIntPoint.of(10, 20, 30, 40);
        xyz.IntIntPoint point3 = xyz.IntIntPoint.of(10, 20, 30, 41);
        xyz.IntIntPoint point4 = xyz.IntIntPoint.of(10, 20, 31, 40);
        xyz.IntIntPoint point5 = xyz.IntIntPoint.of(11, 20, 30, 40);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(point4));
        assertFalse(point1.equals(point5));
    }

    @Test
    public void testXyzIntIntPoint_ToString() {
        xyz.IntIntPoint point = xyz.IntIntPoint.of(10, 20, 30, 40);
        assertEquals("IntIntPoint[x=10, y=20, z=30, v=40]", point.toString());
    }

    // ============================================
    // Tests for xyz.IntLongPoint (3D)
    // ============================================

    @Test
    public void testXyzIntLongPoint_Of() {
        xyz.IntLongPoint point = xyz.IntLongPoint.of(10, 20, 30, 5000L);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(30, point.z());
        assertEquals(5000L, point.v());
    }

    @Test
    public void testXyzIntLongPoint_HashCode() {
        xyz.IntLongPoint point1 = xyz.IntLongPoint.of(10, 20, 30, 5000L);
        xyz.IntLongPoint point2 = xyz.IntLongPoint.of(10, 20, 30, 5000L);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzIntLongPoint_Equals() {
        xyz.IntLongPoint point1 = xyz.IntLongPoint.of(10, 20, 30, 5000L);
        xyz.IntLongPoint point2 = xyz.IntLongPoint.of(10, 20, 30, 5000L);
        xyz.IntLongPoint point3 = xyz.IntLongPoint.of(10, 20, 30, 5001L);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzIntLongPoint_ToString() {
        xyz.IntLongPoint point = xyz.IntLongPoint.of(10, 20, 30, 5000L);
        assertEquals("IntLongPoint[x=10, y=20, z=30, v=5000]", point.toString());
    }

    // ============================================
    // Tests for xyz.IntDoublePoint (3D)
    // ============================================

    @Test
    public void testXyzIntDoublePoint_Of() {
        xyz.IntDoublePoint point = xyz.IntDoublePoint.of(10, 20, 30, 40.5);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(30, point.z());
        assertEquals(40.5, point.v(), 0.001);
    }

    @Test
    public void testXyzIntDoublePoint_HashCode() {
        xyz.IntDoublePoint point1 = xyz.IntDoublePoint.of(10, 20, 30, 40.5);
        xyz.IntDoublePoint point2 = xyz.IntDoublePoint.of(10, 20, 30, 40.5);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzIntDoublePoint_Equals() {
        xyz.IntDoublePoint point1 = xyz.IntDoublePoint.of(10, 20, 30, 40.5);
        xyz.IntDoublePoint point2 = xyz.IntDoublePoint.of(10, 20, 30, 40.5);
        xyz.IntDoublePoint point3 = xyz.IntDoublePoint.of(10, 20, 30, 40.6);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzIntDoublePoint_ToString() {
        xyz.IntDoublePoint point = xyz.IntDoublePoint.of(10, 20, 30, 40.5);
        assertTrue(point.toString().contains("40.5"));
    }

    // ============================================
    // Tests for xyz.IntObjPoint (3D)
    // ============================================

    @Test
    public void testXyzIntObjPoint_Of() {
        xyz.IntObjPoint<String> point = xyz.IntObjPoint.of(10, 20, 30, "test");

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(30, point.z());
        assertEquals("test", point.v());
    }

    @Test
    public void testXyzIntObjPoint_HashCode() {
        xyz.IntObjPoint<String> point1 = xyz.IntObjPoint.of(10, 20, 30, "hello");
        xyz.IntObjPoint<String> point2 = xyz.IntObjPoint.of(10, 20, 30, "hello");

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzIntObjPoint_Equals() {
        xyz.IntObjPoint<String> point1 = xyz.IntObjPoint.of(10, 20, 30, "hello");
        xyz.IntObjPoint<String> point2 = xyz.IntObjPoint.of(10, 20, 30, "hello");
        xyz.IntObjPoint<String> point3 = xyz.IntObjPoint.of(10, 20, 30, "world");
        xyz.IntObjPoint<String> point4 = xyz.IntObjPoint.of(10, 20, 30, null);
        xyz.IntObjPoint<String> point5 = xyz.IntObjPoint.of(10, 20, 30, null);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(point4));
        assertTrue(point4.equals(point5));
    }

    @Test
    public void testXyzIntObjPoint_ToString() {
        xyz.IntObjPoint<String> point = xyz.IntObjPoint.of(10, 20, 30, "test");
        assertTrue(point.toString().contains("test"));
    }

    // ============================================
    // Tests for xyz.LongBytePoint (3D)
    // ============================================

    @Test
    public void testXyzLongBytePoint_Of() {
        xyz.LongBytePoint point = xyz.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);

        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(3000L, point.z());
        assertEquals(100, point.v());
    }

    @Test
    public void testXyzLongBytePoint_HashCode() {
        xyz.LongBytePoint point1 = xyz.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);
        xyz.LongBytePoint point2 = xyz.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzLongBytePoint_Equals() {
        xyz.LongBytePoint point1 = xyz.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);
        xyz.LongBytePoint point2 = xyz.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);
        xyz.LongBytePoint point3 = xyz.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 101);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzLongBytePoint_ToString() {
        xyz.LongBytePoint point = xyz.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);
        assertEquals("LongBytePoint[x=1000, y=2000, z=3000, v=100]", point.toString());
    }

    // ============================================
    // Tests for xyz.LongIntPoint (3D)
    // ============================================

    @Test
    public void testXyzLongIntPoint_Of() {
        xyz.LongIntPoint point = xyz.LongIntPoint.of(500L, 1000L, 1500L, 250);

        assertEquals(500L, point.x());
        assertEquals(1000L, point.y());
        assertEquals(1500L, point.z());
        assertEquals(250, point.v());
    }

    @Test
    public void testXyzLongIntPoint_HashCode() {
        xyz.LongIntPoint point1 = xyz.LongIntPoint.of(500L, 1000L, 1500L, 250);
        xyz.LongIntPoint point2 = xyz.LongIntPoint.of(500L, 1000L, 1500L, 250);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzLongIntPoint_Equals() {
        xyz.LongIntPoint point1 = xyz.LongIntPoint.of(500L, 1000L, 1500L, 250);
        xyz.LongIntPoint point2 = xyz.LongIntPoint.of(500L, 1000L, 1500L, 250);
        xyz.LongIntPoint point3 = xyz.LongIntPoint.of(500L, 1000L, 1500L, 251);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzLongIntPoint_ToString() {
        xyz.LongIntPoint point = xyz.LongIntPoint.of(500L, 1000L, 1500L, 250);
        assertEquals("LongIntPoint[x=500, y=1000, z=1500, v=250]", point.toString());
    }

    // ============================================
    // Tests for xyz.LongLongPoint (3D)
    // ============================================

    @Test
    public void testXyzLongLongPoint_Of() {
        xyz.LongLongPoint point = xyz.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);

        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(3000L, point.z());
        assertEquals(4000L, point.v());
    }

    @Test
    public void testXyzLongLongPoint_HashCode() {
        xyz.LongLongPoint point1 = xyz.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);
        xyz.LongLongPoint point2 = xyz.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzLongLongPoint_Equals() {
        xyz.LongLongPoint point1 = xyz.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);
        xyz.LongLongPoint point2 = xyz.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);
        xyz.LongLongPoint point3 = xyz.LongLongPoint.of(1000L, 2000L, 3000L, 4001L);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzLongLongPoint_ToString() {
        xyz.LongLongPoint point = xyz.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);
        assertEquals("LongLongPoint[x=1000, y=2000, z=3000, v=4000]", point.toString());
    }

    // ============================================
    // Tests for xyz.LongDoublePoint (3D)
    // ============================================

    @Test
    public void testXyzLongDoublePoint_Of() {
        xyz.LongDoublePoint point = xyz.LongDoublePoint.of(100L, 200L, 300L, 400.75);

        assertEquals(100L, point.x());
        assertEquals(200L, point.y());
        assertEquals(300L, point.z());
        assertEquals(400.75, point.v(), 0.001);
    }

    @Test
    public void testXyzLongDoublePoint_HashCode() {
        xyz.LongDoublePoint point1 = xyz.LongDoublePoint.of(100L, 200L, 300L, 400.75);
        xyz.LongDoublePoint point2 = xyz.LongDoublePoint.of(100L, 200L, 300L, 400.75);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzLongDoublePoint_Equals() {
        xyz.LongDoublePoint point1 = xyz.LongDoublePoint.of(100L, 200L, 300L, 400.75);
        xyz.LongDoublePoint point2 = xyz.LongDoublePoint.of(100L, 200L, 300L, 400.75);
        xyz.LongDoublePoint point3 = xyz.LongDoublePoint.of(100L, 200L, 300L, 400.76);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzLongDoublePoint_ToString() {
        xyz.LongDoublePoint point = xyz.LongDoublePoint.of(100L, 200L, 300L, 400.75);
        assertTrue(point.toString().contains("400.75"));
    }

    // ============================================
    // Tests for xyz.LongObjPoint (3D)
    // ============================================

    @Test
    public void testXyzLongObjPoint_Of() {
        xyz.LongObjPoint<Integer> point = xyz.LongObjPoint.of(10L, 20L, 30L, 42);

        assertEquals(10L, point.x());
        assertEquals(20L, point.y());
        assertEquals(30L, point.z());
        assertEquals(Integer.valueOf(42), point.v());
    }

    @Test
    public void testXyzLongObjPoint_HashCode() {
        xyz.LongObjPoint<Integer> point1 = xyz.LongObjPoint.of(10L, 20L, 30L, 100);
        xyz.LongObjPoint<Integer> point2 = xyz.LongObjPoint.of(10L, 20L, 30L, 100);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzLongObjPoint_Equals() {
        xyz.LongObjPoint<Integer> point1 = xyz.LongObjPoint.of(10L, 20L, 30L, 100);
        xyz.LongObjPoint<Integer> point2 = xyz.LongObjPoint.of(10L, 20L, 30L, 100);
        xyz.LongObjPoint<Integer> point3 = xyz.LongObjPoint.of(10L, 20L, 30L, 101);
        xyz.LongObjPoint<Integer> point4 = xyz.LongObjPoint.of(10L, 20L, 30L, null);
        xyz.LongObjPoint<Integer> point5 = xyz.LongObjPoint.of(10L, 20L, 30L, null);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(point4));
        assertTrue(point4.equals(point5));
    }

    @Test
    public void testXyzLongObjPoint_ToString() {
        xyz.LongObjPoint<Integer> point = xyz.LongObjPoint.of(10L, 20L, 30L, 42);
        assertTrue(point.toString().contains("42"));
    }

    // ============================================
    // Tests for xyz.DoubleBytePoint (3D)
    // ============================================

    @Test
    public void testXyzDoubleBytePoint_Of() {
        xyz.DoubleBytePoint point = xyz.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);

        assertEquals(10.5, point.x(), 0.001);
        assertEquals(20.7, point.y(), 0.001);
        assertEquals(30.9, point.z(), 0.001);
        assertEquals(40, point.v());
    }

    @Test
    public void testXyzDoubleBytePoint_HashCode() {
        xyz.DoubleBytePoint point1 = xyz.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);
        xyz.DoubleBytePoint point2 = xyz.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzDoubleBytePoint_Equals() {
        xyz.DoubleBytePoint point1 = xyz.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);
        xyz.DoubleBytePoint point2 = xyz.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);
        xyz.DoubleBytePoint point3 = xyz.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 41);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzDoubleBytePoint_ToString() {
        xyz.DoubleBytePoint point = xyz.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);
        assertTrue(point.toString().contains("10.5"));
        assertTrue(point.toString().contains("20.7"));
        assertTrue(point.toString().contains("30.9"));
    }

    // ============================================
    // Tests for xyz.DoubleIntPoint (3D)
    // ============================================

    @Test
    public void testXyzDoubleIntPoint_Of() {
        xyz.DoubleIntPoint point = xyz.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);

        assertEquals(1.1, point.x(), 0.001);
        assertEquals(2.2, point.y(), 0.001);
        assertEquals(3.3, point.z(), 0.001);
        assertEquals(400, point.v());
    }

    @Test
    public void testXyzDoubleIntPoint_HashCode() {
        xyz.DoubleIntPoint point1 = xyz.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);
        xyz.DoubleIntPoint point2 = xyz.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzDoubleIntPoint_Equals() {
        xyz.DoubleIntPoint point1 = xyz.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);
        xyz.DoubleIntPoint point2 = xyz.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);
        xyz.DoubleIntPoint point3 = xyz.DoubleIntPoint.of(1.1, 2.2, 3.3, 401);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzDoubleIntPoint_ToString() {
        xyz.DoubleIntPoint point = xyz.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);
        assertTrue(point.toString().contains("1.1"));
        assertTrue(point.toString().contains("2.2"));
        assertTrue(point.toString().contains("3.3"));
    }

    // ============================================
    // Tests for xyz.DoubleLongPoint (3D)
    // ============================================

    @Test
    public void testXyzDoubleLongPoint_Of() {
        xyz.DoubleLongPoint point = xyz.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);

        assertEquals(5.5, point.x(), 0.001);
        assertEquals(10.75, point.y(), 0.001);
        assertEquals(15.25, point.z(), 0.001);
        assertEquals(1000000L, point.v());
    }

    @Test
    public void testXyzDoubleLongPoint_HashCode() {
        xyz.DoubleLongPoint point1 = xyz.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);
        xyz.DoubleLongPoint point2 = xyz.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzDoubleLongPoint_Equals() {
        xyz.DoubleLongPoint point1 = xyz.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);
        xyz.DoubleLongPoint point2 = xyz.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);
        xyz.DoubleLongPoint point3 = xyz.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000001L);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzDoubleLongPoint_ToString() {
        xyz.DoubleLongPoint point = xyz.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);
        assertTrue(point.toString().contains("5.5"));
        assertTrue(point.toString().contains("10.75"));
        assertTrue(point.toString().contains("15.25"));
    }

    // ============================================
    // Tests for xyz.DoubleDoublePoint (3D)
    // ============================================

    @Test
    public void testXyzDoubleDoublePoint_Of() {
        xyz.DoubleDoublePoint point = xyz.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.1);

        assertEquals(10.5, point.x(), 0.001);
        assertEquals(20.7, point.y(), 0.001);
        assertEquals(30.9, point.z(), 0.001);
        assertEquals(40.1, point.v(), 0.001);
    }

    @Test
    public void testXyzDoubleDoublePoint_HashCode() {
        xyz.DoubleDoublePoint point1 = xyz.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.1);
        xyz.DoubleDoublePoint point2 = xyz.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.1);

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzDoubleDoublePoint_Equals() {
        xyz.DoubleDoublePoint point1 = xyz.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.1);
        xyz.DoubleDoublePoint point2 = xyz.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.1);
        xyz.DoubleDoublePoint point3 = xyz.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.2);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
    }

    @Test
    public void testXyzDoubleDoublePoint_ToString() {
        xyz.DoubleDoublePoint point = xyz.DoubleDoublePoint.of(1.1, 2.2, 3.3, 4.4);
        assertTrue(point.toString().contains("1.1"));
        assertTrue(point.toString().contains("2.2"));
        assertTrue(point.toString().contains("3.3"));
        assertTrue(point.toString().contains("4.4"));
    }

    // ============================================
    // Tests for xyz.DoubleObjPoint (3D)
    // ============================================

    @Test
    public void testXyzDoubleObjPoint_Of() {
        xyz.DoubleObjPoint<String> point = xyz.DoubleObjPoint.of(1.5, 2.5, 3.5, "test");

        assertEquals(1.5, point.x(), 0.001);
        assertEquals(2.5, point.y(), 0.001);
        assertEquals(3.5, point.z(), 0.001);
        assertEquals("test", point.v());
    }

    @Test
    public void testXyzDoubleObjPoint_HashCode() {
        xyz.DoubleObjPoint<String> point1 = xyz.DoubleObjPoint.of(1.5, 2.5, 3.5, "hello");
        xyz.DoubleObjPoint<String> point2 = xyz.DoubleObjPoint.of(1.5, 2.5, 3.5, "hello");

        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    public void testXyzDoubleObjPoint_Equals() {
        xyz.DoubleObjPoint<String> point1 = xyz.DoubleObjPoint.of(1.5, 2.5, 3.5, "hello");
        xyz.DoubleObjPoint<String> point2 = xyz.DoubleObjPoint.of(1.5, 2.5, 3.5, "hello");
        xyz.DoubleObjPoint<String> point3 = xyz.DoubleObjPoint.of(1.5, 2.5, 3.5, "world");
        xyz.DoubleObjPoint<String> point4 = xyz.DoubleObjPoint.of(1.5, 2.5, 3.5, null);
        xyz.DoubleObjPoint<String> point5 = xyz.DoubleObjPoint.of(1.5, 2.5, 3.5, null);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(point4));
        assertTrue(point4.equals(point5));
    }

    @Test
    public void testXyzDoubleObjPoint_ToString() {
        xyz.DoubleObjPoint<String> point = xyz.DoubleObjPoint.of(1.5, 2.5, 3.5, "test");
        assertTrue(point.toString().contains("test"));
    }

    // ============================================
    // xyz Edge cases and comprehensive tests
    // ============================================

    @Test
    public void testXyz_MixedTypePoints() {
        xyz.ByteLongPoint byteLong = xyz.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);
        xyz.IntDoublePoint intDouble = xyz.IntDoublePoint.of(10, 20, 30, 40.5);
        xyz.LongIntPoint longInt = xyz.LongIntPoint.of(100L, 200L, 300L, 400);

        assertEquals(1, byteLong.x());
        assertEquals(2, byteLong.y());
        assertEquals(3, byteLong.z());
        assertEquals(1000L, byteLong.v());

        assertEquals(10, intDouble.x());
        assertEquals(20, intDouble.y());
        assertEquals(30, intDouble.z());
        assertEquals(40.5, intDouble.v(), 0.001);

        assertEquals(100L, longInt.x());
        assertEquals(200L, longInt.y());
        assertEquals(300L, longInt.z());
        assertEquals(400, longInt.v());
    }

    @Test
    public void testXyz_EdgeCases() {
        xyz.ByteBytePoint minBytePoint = xyz.ByteBytePoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0, (byte) 0);
        assertEquals(Byte.MIN_VALUE, minBytePoint.x());
        assertEquals(Byte.MAX_VALUE, minBytePoint.y());
        assertEquals(0, minBytePoint.z());
        assertEquals(0, minBytePoint.v());

        xyz.IntIntPoint maxIntPoint = xyz.IntIntPoint.of(Integer.MAX_VALUE, Integer.MIN_VALUE, 0, 0);
        assertEquals(Integer.MAX_VALUE, maxIntPoint.x());
        assertEquals(Integer.MIN_VALUE, maxIntPoint.y());
        assertEquals(0, maxIntPoint.z());
        assertEquals(0, maxIntPoint.v());

        xyz.LongLongPoint maxLongPoint = xyz.LongLongPoint.of(Long.MAX_VALUE, Long.MIN_VALUE, 0L, 0L);
        assertEquals(Long.MAX_VALUE, maxLongPoint.x());
        assertEquals(Long.MIN_VALUE, maxLongPoint.y());
        assertEquals(0L, maxLongPoint.z());
        assertEquals(0L, maxLongPoint.v());

        xyz.DoubleDoublePoint specialDoublePoint = xyz.DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0, Double.NaN);
        assertEquals(Double.POSITIVE_INFINITY, specialDoublePoint.x(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, specialDoublePoint.y(), 0.0);
        assertEquals(0.0, specialDoublePoint.z(), 0.0);
        assertTrue(Double.isNaN(specialDoublePoint.v()));
    }

    @Test
    public void testXyz_AllPointTypesSelfEquals() {
        assertTrue(xyz.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4).equals(xyz.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4)));
        assertTrue(xyz.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 4).equals(xyz.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 4)));
        assertTrue(xyz.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 4L).equals(xyz.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 4L)));
        assertTrue(xyz.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 4.0).equals(xyz.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 4.0)));
        assertTrue(xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "4").equals(xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "4")));

        assertTrue(xyz.IntBytePoint.of(1, 2, 3, (byte) 4).equals(xyz.IntBytePoint.of(1, 2, 3, (byte) 4)));
        assertTrue(xyz.IntIntPoint.of(1, 2, 3, 4).equals(xyz.IntIntPoint.of(1, 2, 3, 4)));
        assertTrue(xyz.IntLongPoint.of(1, 2, 3, 4L).equals(xyz.IntLongPoint.of(1, 2, 3, 4L)));
        assertTrue(xyz.IntDoublePoint.of(1, 2, 3, 4.0).equals(xyz.IntDoublePoint.of(1, 2, 3, 4.0)));
        assertTrue(xyz.IntObjPoint.of(1, 2, 3, "4").equals(xyz.IntObjPoint.of(1, 2, 3, "4")));

        assertTrue(xyz.LongBytePoint.of(1L, 2L, 3L, (byte) 4).equals(xyz.LongBytePoint.of(1L, 2L, 3L, (byte) 4)));
        assertTrue(xyz.LongIntPoint.of(1L, 2L, 3L, 4).equals(xyz.LongIntPoint.of(1L, 2L, 3L, 4)));
        assertTrue(xyz.LongLongPoint.of(1L, 2L, 3L, 4L).equals(xyz.LongLongPoint.of(1L, 2L, 3L, 4L)));
        assertTrue(xyz.LongDoublePoint.of(1L, 2L, 3L, 4.0).equals(xyz.LongDoublePoint.of(1L, 2L, 3L, 4.0)));
        assertTrue(xyz.LongObjPoint.of(1L, 2L, 3L, "4").equals(xyz.LongObjPoint.of(1L, 2L, 3L, "4")));

        assertTrue(xyz.DoubleBytePoint.of(1.0, 2.0, 3.0, (byte) 4).equals(xyz.DoubleBytePoint.of(1.0, 2.0, 3.0, (byte) 4)));
        assertTrue(xyz.DoubleIntPoint.of(1.0, 2.0, 3.0, 4).equals(xyz.DoubleIntPoint.of(1.0, 2.0, 3.0, 4)));
        assertTrue(xyz.DoubleLongPoint.of(1.0, 2.0, 3.0, 4L).equals(xyz.DoubleLongPoint.of(1.0, 2.0, 3.0, 4L)));
        assertTrue(xyz.DoubleDoublePoint.of(1.0, 2.0, 3.0, 4.0).equals(xyz.DoubleDoublePoint.of(1.0, 2.0, 3.0, 4.0)));
        assertTrue(xyz.DoubleObjPoint.of(1.0, 2.0, 3.0, "4").equals(xyz.DoubleObjPoint.of(1.0, 2.0, 3.0, "4")));
    }

    @Test
    public void testXyz_AllPointTypesNotEqualToNull() {
        assertFalse(xyz.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4).equals(null));
        assertFalse(xyz.IntIntPoint.of(1, 2, 3, 4).equals(null));
        assertFalse(xyz.LongLongPoint.of(1L, 2L, 3L, 4L).equals(null));
        assertFalse(xyz.DoubleDoublePoint.of(1.0, 2.0, 3.0, 4.0).equals(null));
        assertFalse(xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "test").equals(null));
        assertFalse(xyz.IntObjPoint.of(1, 2, 3, "test").equals(null));
        assertFalse(xyz.LongObjPoint.of(1L, 2L, 3L, "test").equals(null));
        assertFalse(xyz.DoubleObjPoint.of(1.0, 2.0, 3.0, "test").equals(null));
    }

    @Test
    public void testXyz_DifferentPointTypesNotEqual() {
        xyz.IntIntPoint intPoint = xyz.IntIntPoint.of(1, 2, 3, 4);
        xyz.LongLongPoint longPoint = xyz.LongLongPoint.of(1L, 2L, 3L, 4L);

        assertFalse(intPoint.equals(longPoint));
        assertFalse(longPoint.equals(intPoint));
    }

    @Test
    public void testXyz_Immutability() {
        xyz.IntIntPoint point = xyz.IntIntPoint.of(10, 20, 30, 40);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(30, point.z());
        assertEquals(40, point.v());

        // Values should remain the same after multiple accesses
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(30, point.z());
        assertEquals(40, point.v());
    }

    @Test
    public void testXyz_ClassAccessibility() {
        assertFalse(xyz.class == null);

        xyz.IntIntPoint point = xyz.IntIntPoint.of(1, 2, 3, 4);
        assertEquals(1, point.x());
        assertEquals(2, point.y());
        assertEquals(3, point.z());
        assertEquals(4, point.v());
    }
}
