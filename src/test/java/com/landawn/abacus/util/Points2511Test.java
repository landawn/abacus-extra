package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

/**
 * Comprehensive unit tests for the Points utility class and all inner Point classes.
 * Tests cover factory methods, field access, equality, hashing, and string representation
 * for all point types: ByteBytePoint, ByteIntPoint, ByteLongPoint, ByteDoublePoint, ByteObjPoint,
 * IntBytePoint, IntIntPoint, IntLongPoint, IntDoublePoint, IntObjPoint,
 * LongBytePoint, LongIntPoint, LongLongPoint, LongDoublePoint, LongObjPoint,
 * DoubleBytePoint, DoubleIntPoint, DoubleLongPoint, DoubleDoublePoint, DoubleObjPoint.
 */
@Tag("2511")
public class Points2511Test extends TestBase {

    // ============ ByteBytePoint Tests ============

    @Test
    public void testByteBytePoint_of() {
        Points.xy.ByteBytePoint point = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 100);
        assertNotNull(point);
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(100, point.v());
    }

    @Test
    public void testByteBytePoint_negativeValues() {
        Points.xy.ByteBytePoint point = Points.xy.ByteBytePoint.of((byte) -10, (byte) -20, (byte) -100);
        assertEquals(-10, point.x());
        assertEquals(-20, point.y());
        assertEquals(-100, point.v());
    }

    @Test
    public void testByteBytePoint_zeroValues() {
        Points.xy.ByteBytePoint point = Points.xy.ByteBytePoint.of((byte) 0, (byte) 0, (byte) 0);
        assertEquals(0, point.x());
        assertEquals(0, point.y());
        assertEquals(0, point.v());
    }

    @Test
    public void testByteBytePoint_maxValues() {
        Points.xy.ByteBytePoint point = Points.xy.ByteBytePoint.of(Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE);
        assertEquals(Byte.MAX_VALUE, point.x());
        assertEquals(Byte.MAX_VALUE, point.y());
        assertEquals(Byte.MAX_VALUE, point.v());
    }

    @Test
    public void testByteBytePoint_equals_same() {
        Points.xy.ByteBytePoint p1 = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        Points.xy.ByteBytePoint p2 = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(p1, p2);
        assertTrue(p1.equals(p2));
    }

    @Test
    public void testByteBytePoint_equals_different() {
        Points.xy.ByteBytePoint p1 = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        Points.xy.ByteBytePoint p2 = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 31);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testByteBytePoint_equals_self() {
        Points.xy.ByteBytePoint p = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(p, p);
    }

    @Test
    public void testByteBytePoint_equals_null() {
        Points.xy.ByteBytePoint p = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        assertFalse(p.equals(null));
    }

    @Test
    public void testByteBytePoint_equals_differentType() {
        Points.xy.ByteBytePoint p = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        assertFalse(p.equals("not a point"));
    }

    @Test
    public void testByteBytePoint_hashCode_same() {
        Points.xy.ByteBytePoint p1 = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        Points.xy.ByteBytePoint p2 = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testByteBytePoint_hashCode_different() {
        Points.xy.ByteBytePoint p1 = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        Points.xy.ByteBytePoint p2 = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 31);
        assertFalse(p1.hashCode() == p2.hashCode());
    }

    @Test
    public void testByteBytePoint_toString() {
        Points.xy.ByteBytePoint point = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        String str = point.toString();
        assertNotNull(str);
        assertTrue(str.contains("10"));
        assertTrue(str.contains("20"));
        assertTrue(str.contains("30"));
    }

    // ============ ByteIntPoint Tests ============

    @Test
    public void testByteIntPoint_of() {
        Points.xy.ByteIntPoint point = Points.xy.ByteIntPoint.of((byte) 10, (byte) 20, 1000);
        assertNotNull(point);
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(1000, point.v());
    }

    @Test
    public void testByteIntPoint_largeValue() {
        Points.xy.ByteIntPoint point = Points.xy.ByteIntPoint.of((byte) 10, (byte) 20, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, point.v());
    }

    @Test
    public void testByteIntPoint_equals() {
        Points.xy.ByteIntPoint p1 = Points.xy.ByteIntPoint.of((byte) 10, (byte) 20, 100);
        Points.xy.ByteIntPoint p2 = Points.xy.ByteIntPoint.of((byte) 10, (byte) 20, 100);
        assertEquals(p1, p2);
    }

    @Test
    public void testByteIntPoint_hashCode() {
        Points.xy.ByteIntPoint p1 = Points.xy.ByteIntPoint.of((byte) 10, (byte) 20, 100);
        Points.xy.ByteIntPoint p2 = Points.xy.ByteIntPoint.of((byte) 10, (byte) 20, 100);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testByteIntPoint_toString() {
        Points.xy.ByteIntPoint point = Points.xy.ByteIntPoint.of((byte) 5, (byte) 10, 50);
        String str = point.toString();
        assertTrue(str.contains("5"));
        assertTrue(str.contains("10"));
        assertTrue(str.contains("50"));
    }

    // ============ ByteLongPoint Tests ============

    @Test
    public void testByteLongPoint_of() {
        Points.xy.ByteLongPoint point = Points.xy.ByteLongPoint.of((byte) 10, (byte) 20, 1000000L);
        assertNotNull(point);
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(1000000L, point.v());
    }

    @Test
    public void testByteLongPoint_largeValue() {
        Points.xy.ByteLongPoint point = Points.xy.ByteLongPoint.of((byte) 10, (byte) 20, Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, point.v());
    }

    @Test
    public void testByteLongPoint_equals() {
        Points.xy.ByteLongPoint p1 = Points.xy.ByteLongPoint.of((byte) 10, (byte) 20, 1000L);
        Points.xy.ByteLongPoint p2 = Points.xy.ByteLongPoint.of((byte) 10, (byte) 20, 1000L);
        assertEquals(p1, p2);
    }

    @Test
    public void testByteLongPoint_toString() {
        Points.xy.ByteLongPoint point = Points.xy.ByteLongPoint.of((byte) 5, (byte) 10, 500L);
        String str = point.toString();
        assertTrue(str.contains("5"));
        assertTrue(str.contains("10"));
    }

    // ============ ByteDoublePoint Tests ============

    @Test
    public void testByteDoublePoint_of() {
        Points.xy.ByteDoublePoint point = Points.xy.ByteDoublePoint.of((byte) 10, (byte) 20, 99.99);
        assertNotNull(point);
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(99.99, point.v(), 0.01);
    }

    @Test
    public void testByteDoublePoint_zeroValue() {
        Points.xy.ByteDoublePoint point = Points.xy.ByteDoublePoint.of((byte) 0, (byte) 0, 0.0);
        assertEquals(0.0, point.v(), 0.01);
    }

    @Test
    public void testByteDoublePoint_negativeValue() {
        Points.xy.ByteDoublePoint point = Points.xy.ByteDoublePoint.of((byte) 10, (byte) 20, -99.99);
        assertEquals(-99.99, point.v(), 0.01);
    }

    @Test
    public void testByteDoublePoint_equals() {
        Points.xy.ByteDoublePoint p1 = Points.xy.ByteDoublePoint.of((byte) 10, (byte) 20, 99.99);
        Points.xy.ByteDoublePoint p2 = Points.xy.ByteDoublePoint.of((byte) 10, (byte) 20, 99.99);
        assertEquals(p1, p2);
    }

    @Test
    public void testByteDoublePoint_toString() {
        Points.xy.ByteDoublePoint point = Points.xy.ByteDoublePoint.of((byte) 5, (byte) 10, 50.5);
        String str = point.toString();
        assertTrue(str.contains("5"));
        assertTrue(str.contains("10"));
    }

    // ============ ByteObjPoint Tests ============

    @Test
    public void testByteObjPoint_of_string() {
        Points.xy.ByteObjPoint<String> point = Points.xy.ByteObjPoint.of((byte) 5, (byte) 10, "marker");
        assertNotNull(point);
        assertEquals(5, point.x());
        assertEquals(10, point.y());
        assertEquals("marker", point.v());
    }

    @Test
    public void testByteObjPoint_of_integer() {
        Points.xy.ByteObjPoint<Integer> point = Points.xy.ByteObjPoint.of((byte) 5, (byte) 10, 42);
        assertEquals(42, point.v());
    }

    @Test
    public void testByteObjPoint_of_null() {
        Points.xy.ByteObjPoint<String> point = Points.xy.ByteObjPoint.of((byte) 5, (byte) 10, null);
        assertNotNull(point);
        assertEquals(null, point.v());
    }

    @Test
    public void testByteObjPoint_equals() {
        Points.xy.ByteObjPoint<String> p1 = Points.xy.ByteObjPoint.of((byte) 5, (byte) 10, "test");
        Points.xy.ByteObjPoint<String> p2 = Points.xy.ByteObjPoint.of((byte) 5, (byte) 10, "test");
        assertEquals(p1, p2);
    }

    @Test
    public void testByteObjPoint_equals_differentValue() {
        Points.xy.ByteObjPoint<String> p1 = Points.xy.ByteObjPoint.of((byte) 5, (byte) 10, "test1");
        Points.xy.ByteObjPoint<String> p2 = Points.xy.ByteObjPoint.of((byte) 5, (byte) 10, "test2");
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testByteObjPoint_toString() {
        Points.xy.ByteObjPoint<String> point = Points.xy.ByteObjPoint.of((byte) 5, (byte) 10, "test");
        String str = point.toString();
        assertTrue(str.contains("5"));
        assertTrue(str.contains("10"));
    }

    // ============ IntBytePoint Tests ============

    @Test
    public void testIntBytePoint_of() {
        Points.xy.IntBytePoint point = Points.xy.IntBytePoint.of(100, 200, (byte) 50);
        assertNotNull(point);
        assertEquals(100, point.x());
        assertEquals(200, point.y());
        assertEquals(50, point.v());
    }

    @Test
    public void testIntBytePoint_largeCoordinates() {
        Points.xy.IntBytePoint point = Points.xy.IntBytePoint.of(Integer.MAX_VALUE, Integer.MIN_VALUE, (byte) 0);
        assertEquals(Integer.MAX_VALUE, point.x());
        assertEquals(Integer.MIN_VALUE, point.y());
    }

    @Test
    public void testIntBytePoint_equals() {
        Points.xy.IntBytePoint p1 = Points.xy.IntBytePoint.of(100, 200, (byte) 50);
        Points.xy.IntBytePoint p2 = Points.xy.IntBytePoint.of(100, 200, (byte) 50);
        assertEquals(p1, p2);
    }

    @Test
    public void testIntBytePoint_toString() {
        Points.xy.IntBytePoint point = Points.xy.IntBytePoint.of(100, 200, (byte) 50);
        String str = point.toString();
        assertTrue(str.contains("100"));
        assertTrue(str.contains("200"));
    }

    // ============ IntIntPoint Tests ============

    @Test
    public void testIntIntPoint_of() {
        Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(100, 200, 300);
        assertNotNull(point);
        assertEquals(100, point.x());
        assertEquals(200, point.y());
        assertEquals(300, point.v());
    }

    @Test
    public void testIntIntPoint_equals() {
        Points.xy.IntIntPoint p1 = Points.xy.IntIntPoint.of(100, 200, 300);
        Points.xy.IntIntPoint p2 = Points.xy.IntIntPoint.of(100, 200, 300);
        assertEquals(p1, p2);
    }

    @Test
    public void testIntIntPoint_equals_differentValue() {
        Points.xy.IntIntPoint p1 = Points.xy.IntIntPoint.of(100, 200, 300);
        Points.xy.IntIntPoint p2 = Points.xy.IntIntPoint.of(100, 200, 301);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testIntIntPoint_hashCode() {
        Points.xy.IntIntPoint p1 = Points.xy.IntIntPoint.of(100, 200, 300);
        Points.xy.IntIntPoint p2 = Points.xy.IntIntPoint.of(100, 200, 300);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testIntIntPoint_toString() {
        Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(100, 200, 300);
        String str = point.toString();
        assertTrue(str.contains("100"));
        assertTrue(str.contains("200"));
        assertTrue(str.contains("300"));
    }

    // ============ IntLongPoint Tests ============

    @Test
    public void testIntLongPoint_of() {
        Points.xy.IntLongPoint point = Points.xy.IntLongPoint.of(100, 200, 1000000L);
        assertNotNull(point);
        assertEquals(100, point.x());
        assertEquals(200, point.y());
        assertEquals(1000000L, point.v());
    }

    @Test
    public void testIntLongPoint_equals() {
        Points.xy.IntLongPoint p1 = Points.xy.IntLongPoint.of(100, 200, 1000000L);
        Points.xy.IntLongPoint p2 = Points.xy.IntLongPoint.of(100, 200, 1000000L);
        assertEquals(p1, p2);
    }

    @Test
    public void testIntLongPoint_toString() {
        Points.xy.IntLongPoint point = Points.xy.IntLongPoint.of(100, 200, 1000000L);
        String str = point.toString();
        assertTrue(str.contains("100"));
        assertTrue(str.contains("200"));
    }

    // ============ IntDoublePoint Tests ============

    @Test
    public void testIntDoublePoint_of() {
        Points.xy.IntDoublePoint point = Points.xy.IntDoublePoint.of(100, 200, 3.14159);
        assertNotNull(point);
        assertEquals(100, point.x());
        assertEquals(200, point.y());
        assertEquals(3.14159, point.v(), 0.00001);
    }

    @Test
    public void testIntDoublePoint_equals() {
        Points.xy.IntDoublePoint p1 = Points.xy.IntDoublePoint.of(100, 200, 3.14159);
        Points.xy.IntDoublePoint p2 = Points.xy.IntDoublePoint.of(100, 200, 3.14159);
        assertEquals(p1, p2);
    }

    @Test
    public void testIntDoublePoint_toString() {
        Points.xy.IntDoublePoint point = Points.xy.IntDoublePoint.of(100, 200, 3.14);
        String str = point.toString();
        assertTrue(str.contains("100"));
        assertTrue(str.contains("200"));
    }

    // ============ IntObjPoint Tests ============

    @Test
    public void testIntObjPoint_of() {
        Points.xy.IntObjPoint<String> point = Points.xy.IntObjPoint.of(10, 20, "marker");
        assertNotNull(point);
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals("marker", point.v());
    }

    @Test
    public void testIntObjPoint_of_list() {
        Points.xy.IntObjPoint<java.util.List<String>> point = Points.xy.IntObjPoint.of(10, 20, java.util.Arrays.asList("a", "b", "c"));
        assertEquals(3, point.v().size());
    }

    @Test
    public void testIntObjPoint_equals() {
        Points.xy.IntObjPoint<String> p1 = Points.xy.IntObjPoint.of(10, 20, "test");
        Points.xy.IntObjPoint<String> p2 = Points.xy.IntObjPoint.of(10, 20, "test");
        assertEquals(p1, p2);
    }

    @Test
    public void testIntObjPoint_toString() {
        Points.xy.IntObjPoint<String> point = Points.xy.IntObjPoint.of(10, 20, "test");
        String str = point.toString();
        assertTrue(str.contains("10"));
        assertTrue(str.contains("20"));
    }

    // ============ LongBytePoint Tests ============

    @Test
    public void testLongBytePoint_of() {
        Points.xy.LongBytePoint point = Points.xy.LongBytePoint.of(1000L, 2000L, (byte) 50);
        assertNotNull(point);
        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(50, point.v());
    }

    @Test
    public void testLongBytePoint_largeCoordinates() {
        Points.xy.LongBytePoint point = Points.xy.LongBytePoint.of(Long.MAX_VALUE, Long.MIN_VALUE, (byte) 0);
        assertEquals(Long.MAX_VALUE, point.x());
        assertEquals(Long.MIN_VALUE, point.y());
    }

    @Test
    public void testLongBytePoint_equals() {
        Points.xy.LongBytePoint p1 = Points.xy.LongBytePoint.of(1000L, 2000L, (byte) 50);
        Points.xy.LongBytePoint p2 = Points.xy.LongBytePoint.of(1000L, 2000L, (byte) 50);
        assertEquals(p1, p2);
    }

    @Test
    public void testLongBytePoint_toString() {
        Points.xy.LongBytePoint point = Points.xy.LongBytePoint.of(1000L, 2000L, (byte) 50);
        String str = point.toString();
        assertTrue(str.contains("1000"));
        assertTrue(str.contains("2000"));
    }

    // ============ LongIntPoint Tests ============

    @Test
    public void testLongIntPoint_of() {
        Points.xy.LongIntPoint point = Points.xy.LongIntPoint.of(1000L, 2000L, 500);
        assertNotNull(point);
        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(500, point.v());
    }

    @Test
    public void testLongIntPoint_equals() {
        Points.xy.LongIntPoint p1 = Points.xy.LongIntPoint.of(1000L, 2000L, 500);
        Points.xy.LongIntPoint p2 = Points.xy.LongIntPoint.of(1000L, 2000L, 500);
        assertEquals(p1, p2);
    }

    @Test
    public void testLongIntPoint_toString() {
        Points.xy.LongIntPoint point = Points.xy.LongIntPoint.of(1000L, 2000L, 500);
        String str = point.toString();
        assertTrue(str.contains("1000"));
        assertTrue(str.contains("2000"));
    }

    // ============ LongLongPoint Tests ============

    @Test
    public void testLongLongPoint_of() {
        Points.xy.LongLongPoint point = Points.xy.LongLongPoint.of(1000L, 2000L, 3000L);
        assertNotNull(point);
        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(3000L, point.v());
    }

    @Test
    public void testLongLongPoint_equals() {
        Points.xy.LongLongPoint p1 = Points.xy.LongLongPoint.of(1000L, 2000L, 3000L);
        Points.xy.LongLongPoint p2 = Points.xy.LongLongPoint.of(1000L, 2000L, 3000L);
        assertEquals(p1, p2);
    }

    @Test
    public void testLongLongPoint_equals_differentValue() {
        Points.xy.LongLongPoint p1 = Points.xy.LongLongPoint.of(1000L, 2000L, 3000L);
        Points.xy.LongLongPoint p2 = Points.xy.LongLongPoint.of(1000L, 2000L, 3001L);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testLongLongPoint_hashCode() {
        Points.xy.LongLongPoint p1 = Points.xy.LongLongPoint.of(1000L, 2000L, 3000L);
        Points.xy.LongLongPoint p2 = Points.xy.LongLongPoint.of(1000L, 2000L, 3000L);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testLongLongPoint_toString() {
        Points.xy.LongLongPoint point = Points.xy.LongLongPoint.of(1000L, 2000L, 3000L);
        String str = point.toString();
        assertTrue(str.contains("1000"));
        assertTrue(str.contains("2000"));
        assertTrue(str.contains("3000"));
    }

    // ============ LongDoublePoint Tests ============

    @Test
    public void testLongDoublePoint_of() {
        Points.xy.LongDoublePoint point = Points.xy.LongDoublePoint.of(1000L, 2000L, 99.99);
        assertNotNull(point);
        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(99.99, point.v(), 0.01);
    }

    @Test
    public void testLongDoublePoint_equals() {
        Points.xy.LongDoublePoint p1 = Points.xy.LongDoublePoint.of(1000L, 2000L, 99.99);
        Points.xy.LongDoublePoint p2 = Points.xy.LongDoublePoint.of(1000L, 2000L, 99.99);
        assertEquals(p1, p2);
    }

    @Test
    public void testLongDoublePoint_toString() {
        Points.xy.LongDoublePoint point = Points.xy.LongDoublePoint.of(1000L, 2000L, 99.99);
        String str = point.toString();
        assertTrue(str.contains("1000"));
        assertTrue(str.contains("2000"));
    }

    // ============ LongObjPoint Tests ============

    @Test
    public void testLongObjPoint_of() {
        Points.xy.LongObjPoint<String> point = Points.xy.LongObjPoint.of(1000L, 2000L, "marker");
        assertNotNull(point);
        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals("marker", point.v());
    }

    @Test
    public void testLongObjPoint_equals() {
        Points.xy.LongObjPoint<String> p1 = Points.xy.LongObjPoint.of(1000L, 2000L, "test");
        Points.xy.LongObjPoint<String> p2 = Points.xy.LongObjPoint.of(1000L, 2000L, "test");
        assertEquals(p1, p2);
    }

    @Test
    public void testLongObjPoint_toString() {
        Points.xy.LongObjPoint<String> point = Points.xy.LongObjPoint.of(1000L, 2000L, "test");
        String str = point.toString();
        assertTrue(str.contains("1000"));
        assertTrue(str.contains("2000"));
    }

    // ============ DoubleBytePoint Tests ============

    @Test
    public void testDoubleBytePoint_of() {
        Points.xy.DoubleBytePoint point = Points.xy.DoubleBytePoint.of(10.5, 20.7, (byte) 50);
        assertNotNull(point);
        assertEquals(10.5, point.x(), 0.01);
        assertEquals(20.7, point.y(), 0.01);
        assertEquals(50, point.v());
    }

    @Test
    public void testDoubleBytePoint_negativeCoordinates() {
        Points.xy.DoubleBytePoint point = Points.xy.DoubleBytePoint.of(-10.5, -20.7, (byte) 50);
        assertEquals(-10.5, point.x(), 0.01);
        assertEquals(-20.7, point.y(), 0.01);
    }

    @Test
    public void testDoubleBytePoint_equals() {
        Points.xy.DoubleBytePoint p1 = Points.xy.DoubleBytePoint.of(10.5, 20.7, (byte) 50);
        Points.xy.DoubleBytePoint p2 = Points.xy.DoubleBytePoint.of(10.5, 20.7, (byte) 50);
        assertEquals(p1, p2);
    }

    @Test
    public void testDoubleBytePoint_toString() {
        Points.xy.DoubleBytePoint point = Points.xy.DoubleBytePoint.of(10.5, 20.7, (byte) 50);
        String str = point.toString();
        assertNotNull(str);
    }

    // ============ DoubleIntPoint Tests ============

    @Test
    public void testDoubleIntPoint_of() {
        Points.xy.DoubleIntPoint point = Points.xy.DoubleIntPoint.of(10.5, 20.7, 100);
        assertNotNull(point);
        assertEquals(10.5, point.x(), 0.01);
        assertEquals(20.7, point.y(), 0.01);
        assertEquals(100, point.v());
    }

    @Test
    public void testDoubleIntPoint_equals() {
        Points.xy.DoubleIntPoint p1 = Points.xy.DoubleIntPoint.of(10.5, 20.7, 100);
        Points.xy.DoubleIntPoint p2 = Points.xy.DoubleIntPoint.of(10.5, 20.7, 100);
        assertEquals(p1, p2);
    }

    @Test
    public void testDoubleIntPoint_toString() {
        Points.xy.DoubleIntPoint point = Points.xy.DoubleIntPoint.of(10.5, 20.7, 100);
        String str = point.toString();
        assertNotNull(str);
    }

    // ============ DoubleLongPoint Tests ============

    @Test
    public void testDoubleLongPoint_of() {
        Points.xy.DoubleLongPoint point = Points.xy.DoubleLongPoint.of(10.5, 20.7, 1000000L);
        assertNotNull(point);
        assertEquals(10.5, point.x(), 0.01);
        assertEquals(20.7, point.y(), 0.01);
        assertEquals(1000000L, point.v());
    }

    @Test
    public void testDoubleLongPoint_equals() {
        Points.xy.DoubleLongPoint p1 = Points.xy.DoubleLongPoint.of(10.5, 20.7, 1000000L);
        Points.xy.DoubleLongPoint p2 = Points.xy.DoubleLongPoint.of(10.5, 20.7, 1000000L);
        assertEquals(p1, p2);
    }

    @Test
    public void testDoubleLongPoint_toString() {
        Points.xy.DoubleLongPoint point = Points.xy.DoubleLongPoint.of(10.5, 20.7, 1000000L);
        String str = point.toString();
        assertNotNull(str);
    }

    // ============ DoubleDoublePoint Tests ============

    @Test
    public void testDoubleDoublePoint_of() {
        Points.xy.DoubleDoublePoint point = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 99.99);
        assertNotNull(point);
        assertEquals(10.5, point.x(), 0.01);
        assertEquals(20.7, point.y(), 0.01);
        assertEquals(99.99, point.v(), 0.01);
    }

    @Test
    public void testDoubleDoublePoint_equals() {
        Points.xy.DoubleDoublePoint p1 = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 99.99);
        Points.xy.DoubleDoublePoint p2 = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 99.99);
        assertEquals(p1, p2);
    }

    @Test
    public void testDoubleDoublePoint_equals_differentValue() {
        Points.xy.DoubleDoublePoint p1 = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 99.99);
        Points.xy.DoubleDoublePoint p2 = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 99.98);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testDoubleDoublePoint_hashCode() {
        Points.xy.DoubleDoublePoint p1 = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 99.99);
        Points.xy.DoubleDoublePoint p2 = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 99.99);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testDoubleDoublePoint_toString() {
        Points.xy.DoubleDoublePoint point = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 99.99);
        String str = point.toString();
        assertTrue(str.contains("10.5") || str.contains("10."));
        assertTrue(str.contains("20.7") || str.contains("20."));
    }

    // ============ DoubleObjPoint Tests ============

    @Test
    public void testDoubleObjPoint_of() {
        Points.xy.DoubleObjPoint<String> point = Points.xy.DoubleObjPoint.of(10.5, 20.7, "marker");
        assertNotNull(point);
        assertEquals(10.5, point.x(), 0.01);
        assertEquals(20.7, point.y(), 0.01);
        assertEquals("marker", point.v());
    }

    @Test
    public void testDoubleObjPoint_of_null() {
        Points.xy.DoubleObjPoint<String> point = Points.xy.DoubleObjPoint.of(10.5, 20.7, null);
        assertNotNull(point);
        assertEquals(null, point.v());
    }

    @Test
    public void testDoubleObjPoint_equals() {
        Points.xy.DoubleObjPoint<String> p1 = Points.xy.DoubleObjPoint.of(10.5, 20.7, "test");
        Points.xy.DoubleObjPoint<String> p2 = Points.xy.DoubleObjPoint.of(10.5, 20.7, "test");
        assertEquals(p1, p2);
    }

    @Test
    public void testDoubleObjPoint_equals_differentValue() {
        Points.xy.DoubleObjPoint<String> p1 = Points.xy.DoubleObjPoint.of(10.5, 20.7, "test1");
        Points.xy.DoubleObjPoint<String> p2 = Points.xy.DoubleObjPoint.of(10.5, 20.7, "test2");
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testDoubleObjPoint_toString() {
        Points.xy.DoubleObjPoint<String> point = Points.xy.DoubleObjPoint.of(10.5, 20.7, "test");
        String str = point.toString();
        assertTrue(str.contains("10.5") || str.contains("10."));
        assertTrue(str.contains("20.7") || str.contains("20."));
    }

    // ============ Cross-Type Comparison Tests ============

    @Test
    public void testDifferentPointTypes_notEqual() {
        Points.xy.IntIntPoint intPoint = Points.xy.IntIntPoint.of(10, 20, 30);
        Points.xy.ByteBytePoint bytePoint = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);

        assertFalse(intPoint.equals(bytePoint));
        assertFalse(bytePoint.equals(intPoint));
    }

    // ============ Immutability Tests ============

    @Test
    public void testImmutability_byteBytePoint() {
        Points.xy.ByteBytePoint point = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
        // Fields are public final, so they cannot be modified
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(30, point.v());
    }

    @Test
    public void testImmutability_intIntPoint() {
        Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(100, 200, 300);
        // Fields are public final, so they cannot be modified
        assertEquals(100, point.x());
        assertEquals(200, point.y());
        assertEquals(300, point.v());
    }

    // ============ Factory Method Tests ============

    @Test
    public void testFactoryMethod_returnsNonNull() {
        assertNotNull(Points.xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3));
        assertNotNull(Points.xy.IntIntPoint.of(1, 2, 3));
        assertNotNull(Points.xy.LongLongPoint.of(1L, 2L, 3L));
        assertNotNull(Points.xy.DoubleDoublePoint.of(1.0, 2.0, 3.0));
    }
}
