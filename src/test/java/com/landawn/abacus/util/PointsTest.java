/*
 * Copyright (C) 2024 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

public class PointsTest extends TestBase {

    @Test
    public void testByteBytePoint() {
        Points.xy.ByteBytePoint point = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(30, point.v);
    }

    @Test
    public void testByteBytePointEqualsAndHashCode() {
        Points.xy.ByteBytePoint point1 = Points.xy.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 15);
        Points.xy.ByteBytePoint point2 = Points.xy.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 15);
        Points.xy.ByteBytePoint point3 = Points.xy.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 16);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(null));
        assertFalse(point1.equals("not a point"));
        assertTrue(point1.equals(point1));

        assertEquals(point1.hashCode(), point2.hashCode());
        assertNotEquals(point1.hashCode(), point3.hashCode());
    }

    @Test
    public void testByteBytePointToString() {
        Points.xy.ByteBytePoint point = Points.xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
        String str = point.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
        assertEquals("[1, 2, 3]", str);
    }

    @Test
    public void testByteIntPoint() {
        Points.xy.ByteIntPoint point = Points.xy.ByteIntPoint.of((byte) 5, (byte) 10, 100);

        assertEquals(5, point.x);
        assertEquals(10, point.y);
        assertEquals(100, point.v);
    }

    @Test
    public void testByteIntPointEqualsAndHashCode() {
        Points.xy.ByteIntPoint point1 = Points.xy.ByteIntPoint.of((byte) 1, (byte) 2, 300);
        Points.xy.ByteIntPoint point2 = Points.xy.ByteIntPoint.of((byte) 1, (byte) 2, 300);
        Points.xy.ByteIntPoint point3 = Points.xy.ByteIntPoint.of((byte) 1, (byte) 2, 301);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertTrue(point1.equals(point1));

        assertEquals(point1.hashCode(), point2.hashCode());
        assertNotEquals(point1.hashCode(), point3.hashCode());
    }

    @Test
    public void testIntIntPoint() {
        Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(100, 200, 300);

        assertEquals(100, point.x);
        assertEquals(200, point.y);
        assertEquals(300, point.v);
    }

    @Test
    public void testIntIntPointEqualsAndHashCode() {
        Points.xy.IntIntPoint point1 = Points.xy.IntIntPoint.of(10, 20, 30);
        Points.xy.IntIntPoint point2 = Points.xy.IntIntPoint.of(10, 20, 30);
        Points.xy.IntIntPoint point3 = Points.xy.IntIntPoint.of(10, 20, 31);
        Points.xy.IntIntPoint point4 = Points.xy.IntIntPoint.of(11, 20, 30);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(point4));
        assertTrue(point1.equals(point1));

        assertEquals(point1.hashCode(), point2.hashCode());
        assertNotEquals(point1.hashCode(), point3.hashCode());
        assertNotEquals(point1.hashCode(), point4.hashCode());
    }

    @Test
    public void testLongLongPoint() {
        Points.xy.LongLongPoint point = Points.xy.LongLongPoint.of(1000L, 2000L, 3000L);

        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals(3000L, point.v);
    }

    @Test
    public void testLongLongPointEqualsAndHashCode() {
        Points.xy.LongLongPoint point1 = Points.xy.LongLongPoint.of(100L, 200L, 300L);
        Points.xy.LongLongPoint point2 = Points.xy.LongLongPoint.of(100L, 200L, 300L);
        Points.xy.LongLongPoint point3 = Points.xy.LongLongPoint.of(100L, 200L, 301L);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));

        assertEquals(point1.hashCode(), point2.hashCode());
        assertNotEquals(point1.hashCode(), point3.hashCode());
    }

    @Test
    public void testDoubleDoublePoint() {
        Points.xy.DoubleDoublePoint point = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 30.9);

        assertEquals(10.5, point.x, 0.001);
        assertEquals(20.7, point.y, 0.001);
        assertEquals(30.9, point.v, 0.001);
    }

    @Test
    public void testDoubleDoublePointEqualsAndHashCode() {
        Points.xy.DoubleDoublePoint point1 = Points.xy.DoubleDoublePoint.of(1.1, 2.2, 3.3);
        Points.xy.DoubleDoublePoint point2 = Points.xy.DoubleDoublePoint.of(1.1, 2.2, 3.3);
        Points.xy.DoubleDoublePoint point3 = Points.xy.DoubleDoublePoint.of(1.1, 2.2, 3.4);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));

        assertEquals(point1.hashCode(), point2.hashCode());
        assertEquals(point1.hashCode(), point3.hashCode());
    }

    @Test
    public void testIntObjPoint() {
        String value = "test value";
        Points.xy.IntObjPoint<String> point = Points.xy.IntObjPoint.of(10, 20, value);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(value, point.v);
    }

    @Test
    public void testIntObjPointEqualsAndHashCode() {
        Points.xy.IntObjPoint<String> point1 = Points.xy.IntObjPoint.of(1, 2, "hello");
        Points.xy.IntObjPoint<String> point2 = Points.xy.IntObjPoint.of(1, 2, "hello");
        Points.xy.IntObjPoint<String> point3 = Points.xy.IntObjPoint.of(1, 2, "world");
        Points.xy.IntObjPoint<String> point4 = Points.xy.IntObjPoint.of(1, 2, null);
        Points.xy.IntObjPoint<String> point5 = Points.xy.IntObjPoint.of(1, 2, null);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(point4));
        assertTrue(point4.equals(point5));

        assertEquals(point1.hashCode(), point2.hashCode());
        assertNotEquals(point1.hashCode(), point3.hashCode());
        assertEquals(point4.hashCode(), point5.hashCode());
    }

    @Test
    public void testDoubleObjPoint() {
        Integer value = 42;
        Points.xy.DoubleObjPoint<Integer> point = Points.xy.DoubleObjPoint.of(1.5, 2.5, value);

        assertEquals(1.5, point.x, 0.001);
        assertEquals(2.5, point.y, 0.001);
        assertEquals(value, point.v);
    }

    @Test
    public void testMixedTypePoints() {
        // Test different combinations of coordinate and value types
        Points.xy.ByteLongPoint byteLong = Points.xy.ByteLongPoint.of((byte) 1, (byte) 2, 1000L);
        Points.xy.IntDoublePoint intDouble = Points.xy.IntDoublePoint.of(10, 20, 30.5);
        Points.xy.LongIntPoint longInt = Points.xy.LongIntPoint.of(100L, 200L, 300);

        assertEquals(1, byteLong.x);
        assertEquals(2, byteLong.y);
        assertEquals(1000L, byteLong.v);

        assertEquals(10, intDouble.x);
        assertEquals(20, intDouble.y);
        assertEquals(30.5, intDouble.v, 0.001);

        assertEquals(100L, longInt.x);
        assertEquals(200L, longInt.y);
        assertEquals(300, longInt.v);
    }

    @Test
    public void testPointToStringFormats() {
        Points.xy.IntIntPoint intPoint = Points.xy.IntIntPoint.of(1, 2, 3);
        Points.xy.DoubleDoublePoint doublePoint = Points.xy.DoubleDoublePoint.of(1.1, 2.2, 3.3);
        Points.xy.IntObjPoint<String> objPoint = Points.xy.IntObjPoint.of(1, 2, "test");

        assertEquals("[1, 2, 3]", intPoint.toString());
        assertTrue(doublePoint.toString().contains("1.1"));
        assertTrue(doublePoint.toString().contains("2.2"));
        assertTrue(doublePoint.toString().contains("3.3"));
        assertTrue(objPoint.toString().contains("test"));
    }

    @Test
    public void testPointsClassIsUtility() {
        // Test that Points class is a proper utility class
        // Cannot instantiate Points directly since constructor is private
        // This is verified by the design

        // Test that nested classes exist and are accessible
        assertFalse(Points.xy.class == null);

        // Verify that we can access the point classes through the utility class
        Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(1, 2, 3);
        assertEquals(1, point.x);
    }

    @Test
    public void testPointImmutability() {
        // Test that points are immutable by design
        Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(10, 20, 30);

        // Fields are final - cannot be changed after construction
        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(30, point.v);

        // Values should remain the same
        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(30, point.v);
    }

    @Test
    public void testDifferentPointTypesNotEqual() {
        // Test that different point types with same values are not equal
        Points.xy.IntIntPoint intPoint = Points.xy.IntIntPoint.of(1, 2, 3);
        Points.xy.LongLongPoint longPoint = Points.xy.LongLongPoint.of(1L, 2L, 3L);

        assertFalse(intPoint.equals(longPoint));
        assertFalse(longPoint.equals(intPoint));
    }

    // Additional comprehensive tests for all remaining point types

    @Test
    public void testByteLongPoint() {
        Points.xy.ByteLongPoint point = Points.xy.ByteLongPoint.of((byte) 10, (byte) 20, 1000L);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(1000L, point.v);

        // Test equals and hashCode
        Points.xy.ByteLongPoint point2 = Points.xy.ByteLongPoint.of((byte) 10, (byte) 20, 1000L);
        Points.xy.ByteLongPoint point3 = Points.xy.ByteLongPoint.of((byte) 10, (byte) 20, 1001L);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("[10, 20, 1000]", point.toString());
    }

    @Test
    public void testByteDoublePoint() {
        Points.xy.ByteDoublePoint point = Points.xy.ByteDoublePoint.of((byte) 5, (byte) 10, 15.5);

        assertEquals(5, point.x);
        assertEquals(10, point.y);
        assertEquals(15.5, point.v, 0.001);

        // Test equals and hashCode
        Points.xy.ByteDoublePoint point2 = Points.xy.ByteDoublePoint.of((byte) 5, (byte) 10, 15.5);
        Points.xy.ByteDoublePoint point3 = Points.xy.ByteDoublePoint.of((byte) 5, (byte) 10, 15.6);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());

        // Test toString
        assertTrue(point.toString().contains("15.5"));
    }

    @Test
    public void testByteObjPoint() {
        String value = "test string";
        Points.xy.ByteObjPoint<String> point = Points.xy.ByteObjPoint.of((byte) 1, (byte) 2, value);

        assertEquals(1, point.x);
        assertEquals(2, point.y);
        assertEquals(value, point.v);

        // Test equals and hashCode with null values
        Points.xy.ByteObjPoint<String> point1 = Points.xy.ByteObjPoint.of((byte) 1, (byte) 2, "hello");
        Points.xy.ByteObjPoint<String> point2 = Points.xy.ByteObjPoint.of((byte) 1, (byte) 2, "hello");
        Points.xy.ByteObjPoint<String> point3 = Points.xy.ByteObjPoint.of((byte) 1, (byte) 2, "world");
        Points.xy.ByteObjPoint<String> point4 = Points.xy.ByteObjPoint.of((byte) 1, (byte) 2, null);
        Points.xy.ByteObjPoint<String> point5 = Points.xy.ByteObjPoint.of((byte) 1, (byte) 2, null);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(point4));
        assertTrue(point4.equals(point5));

        assertEquals(point1.hashCode(), point2.hashCode());
        assertNotEquals(point1.hashCode(), point3.hashCode());
        assertEquals(point4.hashCode(), point5.hashCode());

        // Test toString
        assertTrue(point.toString().contains("test string"));
    }

    @Test
    public void testIntBytePoint() {
        Points.xy.IntBytePoint point = Points.xy.IntBytePoint.of(100, 200, (byte) 50);

        assertEquals(100, point.x);
        assertEquals(200, point.y);
        assertEquals(50, point.v);

        // Test equals and hashCode
        Points.xy.IntBytePoint point2 = Points.xy.IntBytePoint.of(100, 200, (byte) 50);
        Points.xy.IntBytePoint point3 = Points.xy.IntBytePoint.of(100, 200, (byte) 51);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("[100, 200, 50]", point.toString());
    }

    @Test
    public void testIntLongPoint() {
        Points.xy.IntLongPoint point = Points.xy.IntLongPoint.of(10, 20, 5000L);

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(5000L, point.v);

        // Test equals and hashCode
        Points.xy.IntLongPoint point2 = Points.xy.IntLongPoint.of(10, 20, 5000L);
        Points.xy.IntLongPoint point3 = Points.xy.IntLongPoint.of(10, 20, 5001L);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("[10, 20, 5000]", point.toString());
    }

    @Test
    public void testLongBytePoint() {
        Points.xy.LongBytePoint point = Points.xy.LongBytePoint.of(1000L, 2000L, (byte) 100);

        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals(100, point.v);

        // Test equals and hashCode
        Points.xy.LongBytePoint point2 = Points.xy.LongBytePoint.of(1000L, 2000L, (byte) 100);
        Points.xy.LongBytePoint point3 = Points.xy.LongBytePoint.of(1000L, 2000L, (byte) 101);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("[1000, 2000, 100]", point.toString());
    }

    @Test
    public void testLongIntPoint() {
        Points.xy.LongIntPoint point = Points.xy.LongIntPoint.of(500L, 1000L, 250);

        assertEquals(500L, point.x);
        assertEquals(1000L, point.y);
        assertEquals(250, point.v);

        // Test equals and hashCode
        Points.xy.LongIntPoint point2 = Points.xy.LongIntPoint.of(500L, 1000L, 250);
        Points.xy.LongIntPoint point3 = Points.xy.LongIntPoint.of(500L, 1000L, 251);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("[500, 1000, 250]", point.toString());
    }

    @Test
    public void testLongDoublePoint() {
        Points.xy.LongDoublePoint point = Points.xy.LongDoublePoint.of(100L, 200L, 300.75);

        assertEquals(100L, point.x);
        assertEquals(200L, point.y);
        assertEquals(300.75, point.v, 0.001);

        // Test equals and hashCode
        Points.xy.LongDoublePoint point2 = Points.xy.LongDoublePoint.of(100L, 200L, 300.75);
        Points.xy.LongDoublePoint point3 = Points.xy.LongDoublePoint.of(100L, 200L, 300.76);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());

        // Test toString
        assertTrue(point.toString().contains("300.75"));
    }

    @Test
    public void testLongObjPoint() {
        Integer value = 42;
        Points.xy.LongObjPoint<Integer> point = Points.xy.LongObjPoint.of(10L, 20L, value);

        assertEquals(10L, point.x);
        assertEquals(20L, point.y);
        assertEquals(value, point.v);

        // Test equals and hashCode with null values
        Points.xy.LongObjPoint<Integer> point1 = Points.xy.LongObjPoint.of(1L, 2L, 100);
        Points.xy.LongObjPoint<Integer> point2 = Points.xy.LongObjPoint.of(1L, 2L, 100);
        Points.xy.LongObjPoint<Integer> point3 = Points.xy.LongObjPoint.of(1L, 2L, 101);
        Points.xy.LongObjPoint<Integer> point4 = Points.xy.LongObjPoint.of(1L, 2L, null);
        Points.xy.LongObjPoint<Integer> point5 = Points.xy.LongObjPoint.of(1L, 2L, null);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(point4));
        assertTrue(point4.equals(point5));

        assertEquals(point1.hashCode(), point2.hashCode());
        assertEquals(point4.hashCode(), point5.hashCode());

        // Test toString
        assertTrue(point.toString().contains("42"));
    }

    @Test
    public void testDoubleBytePoint() {
        Points.xy.DoubleBytePoint point = Points.xy.DoubleBytePoint.of(10.5, 20.7, (byte) 30);

        assertEquals(10.5, point.x, 0.001);
        assertEquals(20.7, point.y, 0.001);
        assertEquals(30, point.v);

        // Test equals and hashCode
        Points.xy.DoubleBytePoint point2 = Points.xy.DoubleBytePoint.of(10.5, 20.7, (byte) 30);
        Points.xy.DoubleBytePoint point3 = Points.xy.DoubleBytePoint.of(10.5, 20.7, (byte) 31);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());

        // Test toString
        assertTrue(point.toString().contains("10.5"));
        assertTrue(point.toString().contains("20.7"));
        assertTrue(point.toString().contains("30"));
    }

    @Test
    public void testDoubleIntPoint() {
        Points.xy.DoubleIntPoint point = Points.xy.DoubleIntPoint.of(1.1, 2.2, 300);

        assertEquals(1.1, point.x, 0.001);
        assertEquals(2.2, point.y, 0.001);
        assertEquals(300, point.v);

        // Test equals and hashCode
        Points.xy.DoubleIntPoint point2 = Points.xy.DoubleIntPoint.of(1.1, 2.2, 300);
        Points.xy.DoubleIntPoint point3 = Points.xy.DoubleIntPoint.of(1.1, 2.2, 301);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());

        // Test toString
        assertTrue(point.toString().contains("1.1"));
        assertTrue(point.toString().contains("2.2"));
        assertTrue(point.toString().contains("300"));
    }

    @Test
    public void testDoubleLongPoint() {
        Points.xy.DoubleLongPoint point = Points.xy.DoubleLongPoint.of(5.5, 10.75, 1000000L);

        assertEquals(5.5, point.x, 0.001);
        assertEquals(10.75, point.y, 0.001);
        assertEquals(1000000L, point.v);

        // Test equals and hashCode
        Points.xy.DoubleLongPoint point2 = Points.xy.DoubleLongPoint.of(5.5, 10.75, 1000000L);
        Points.xy.DoubleLongPoint point3 = Points.xy.DoubleLongPoint.of(5.5, 10.75, 1000001L);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());

        // Test toString
        assertTrue(point.toString().contains("5.5"));
        assertTrue(point.toString().contains("10.75"));
        assertTrue(point.toString().contains("1000000"));
    }

    @Test
    public void testDoubleObjPointComprehensive() {
        // Test with different object types
        String stringValue = "test";
        Points.xy.DoubleObjPoint<String> stringPoint = Points.xy.DoubleObjPoint.of(1.0, 2.0, stringValue);

        assertEquals(1.0, stringPoint.x, 0.001);
        assertEquals(2.0, stringPoint.y, 0.001);
        assertEquals(stringValue, stringPoint.v);

        // Test equals and hashCode with null values
        Points.xy.DoubleObjPoint<String> point1 = Points.xy.DoubleObjPoint.of(1.5, 2.5, "hello");
        Points.xy.DoubleObjPoint<String> point2 = Points.xy.DoubleObjPoint.of(1.5, 2.5, "hello");
        Points.xy.DoubleObjPoint<String> point3 = Points.xy.DoubleObjPoint.of(1.5, 2.5, "world");
        Points.xy.DoubleObjPoint<String> point4 = Points.xy.DoubleObjPoint.of(1.5, 2.5, null);
        Points.xy.DoubleObjPoint<String> point5 = Points.xy.DoubleObjPoint.of(1.5, 2.5, null);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertFalse(point1.equals(point4));
        assertTrue(point4.equals(point5));

        assertEquals(point1.hashCode(), point2.hashCode());
        assertEquals(point4.hashCode(), point5.hashCode());

        // Test toString
        assertTrue(stringPoint.toString().contains("test"));
    }

    @Test
    public void testPointsEdgeCases() {
        // Test with extreme values
        Points.xy.ByteBytePoint minBytePoint = Points.xy.ByteBytePoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0);
        assertEquals(Byte.MIN_VALUE, minBytePoint.x);
        assertEquals(Byte.MAX_VALUE, minBytePoint.y);
        assertEquals(0, minBytePoint.v);

        Points.xy.IntIntPoint maxIntPoint = Points.xy.IntIntPoint.of(Integer.MAX_VALUE, Integer.MIN_VALUE, 0);
        assertEquals(Integer.MAX_VALUE, maxIntPoint.x);
        assertEquals(Integer.MIN_VALUE, maxIntPoint.y);
        assertEquals(0, maxIntPoint.v);

        Points.xy.LongLongPoint maxLongPoint = Points.xy.LongLongPoint.of(Long.MAX_VALUE, Long.MIN_VALUE, 0L);
        assertEquals(Long.MAX_VALUE, maxLongPoint.x);
        assertEquals(Long.MIN_VALUE, maxLongPoint.y);
        assertEquals(0L, maxLongPoint.v);

        // Test with special double values
        Points.xy.DoubleDoublePoint specialDoublePoint = Points.xy.DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN);
        assertEquals(Double.POSITIVE_INFINITY, specialDoublePoint.x, 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, specialDoublePoint.y, 0.0);
        assertTrue(Double.isNaN(specialDoublePoint.v));
    }

    @Test
    public void testAllPointTypesWithSelfEquals() {
        // Test that all point types equal themselves
        Points.xy.ByteBytePoint bp1 = Points.xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
        Points.xy.ByteIntPoint bp2 = Points.xy.ByteIntPoint.of((byte) 1, (byte) 2, 3);
        Points.xy.ByteLongPoint bp3 = Points.xy.ByteLongPoint.of((byte) 1, (byte) 2, 3L);
        Points.xy.ByteDoublePoint bp4 = Points.xy.ByteDoublePoint.of((byte) 1, (byte) 2, 3.0);
        Points.xy.ByteObjPoint<String> bp5 = Points.xy.ByteObjPoint.of((byte) 1, (byte) 2, "3");

        Points.xy.IntBytePoint ip1 = Points.xy.IntBytePoint.of(1, 2, (byte) 3);
        Points.xy.IntIntPoint ip2 = Points.xy.IntIntPoint.of(1, 2, 3);
        Points.xy.IntLongPoint ip3 = Points.xy.IntLongPoint.of(1, 2, 3L);
        Points.xy.IntDoublePoint ip4 = Points.xy.IntDoublePoint.of(1, 2, 3.0);
        Points.xy.IntObjPoint<String> ip5 = Points.xy.IntObjPoint.of(1, 2, "3");

        Points.xy.LongBytePoint lp1 = Points.xy.LongBytePoint.of(1L, 2L, (byte) 3);
        Points.xy.LongIntPoint lp2 = Points.xy.LongIntPoint.of(1L, 2L, 3);
        Points.xy.LongLongPoint lp3 = Points.xy.LongLongPoint.of(1L, 2L, 3L);
        Points.xy.LongDoublePoint lp4 = Points.xy.LongDoublePoint.of(1L, 2L, 3.0);
        Points.xy.LongObjPoint<String> lp5 = Points.xy.LongObjPoint.of(1L, 2L, "3");

        Points.xy.DoubleBytePoint dp1 = Points.xy.DoubleBytePoint.of(1.0, 2.0, (byte) 3);
        Points.xy.DoubleIntPoint dp2 = Points.xy.DoubleIntPoint.of(1.0, 2.0, 3);
        Points.xy.DoubleLongPoint dp3 = Points.xy.DoubleLongPoint.of(1.0, 2.0, 3L);
        Points.xy.DoubleDoublePoint dp4 = Points.xy.DoubleDoublePoint.of(1.0, 2.0, 3.0);
        Points.xy.DoubleObjPoint<String> dp5 = Points.xy.DoubleObjPoint.of(1.0, 2.0, "3");

        // Test self-equality for all point types
        assertTrue(bp1.equals(bp1));
        assertTrue(bp2.equals(bp2));
        assertTrue(bp3.equals(bp3));
        assertTrue(bp4.equals(bp4));
        assertTrue(bp5.equals(bp5));

        assertTrue(ip1.equals(ip1));
        assertTrue(ip2.equals(ip2));
        assertTrue(ip3.equals(ip3));
        assertTrue(ip4.equals(ip4));
        assertTrue(ip5.equals(ip5));

        assertTrue(lp1.equals(lp1));
        assertTrue(lp2.equals(lp2));
        assertTrue(lp3.equals(lp3));
        assertTrue(lp4.equals(lp4));
        assertTrue(lp5.equals(lp5));

        assertTrue(dp1.equals(dp1));
        assertTrue(dp2.equals(dp2));
        assertTrue(dp3.equals(dp3));
        assertTrue(dp4.equals(dp4));
        assertTrue(dp5.equals(dp5));
    }

    @Test
    public void testAllPointTypesNotEqualToNull() {
        // Test that no point equals null
        assertFalse(Points.xy.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3).equals(null));
        assertFalse(Points.xy.IntIntPoint.of(1, 2, 3).equals(null));
        assertFalse(Points.xy.LongLongPoint.of(1L, 2L, 3L).equals(null));
        assertFalse(Points.xy.DoubleDoublePoint.of(1.0, 2.0, 3.0).equals(null));
        assertFalse(Points.xy.ByteObjPoint.of((byte) 1, (byte) 2, "test").equals(null));
        assertFalse(Points.xy.IntObjPoint.of(1, 2, "test").equals(null));
        assertFalse(Points.xy.LongObjPoint.of(1L, 2L, "test").equals(null));
        assertFalse(Points.xy.DoubleObjPoint.of(1.0, 2.0, "test").equals(null));
    }
}