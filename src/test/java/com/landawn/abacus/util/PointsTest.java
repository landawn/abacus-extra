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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Points.D2;
import com.landawn.abacus.util.Points.D2.ByteBytePoint;
import com.landawn.abacus.util.Points.D2.ByteDoublePoint;
import com.landawn.abacus.util.Points.D2.ByteIntPoint;
import com.landawn.abacus.util.Points.D2.ByteLongPoint;
import com.landawn.abacus.util.Points.D2.ByteObjPoint;
import com.landawn.abacus.util.Points.D2.DoubleBytePoint;
import com.landawn.abacus.util.Points.D2.DoubleDoublePoint;
import com.landawn.abacus.util.Points.D2.DoubleIntPoint;
import com.landawn.abacus.util.Points.D2.DoubleLongPoint;
import com.landawn.abacus.util.Points.D2.DoubleObjPoint;
import com.landawn.abacus.util.Points.D2.IntBytePoint;
import com.landawn.abacus.util.Points.D2.IntDoublePoint;
import com.landawn.abacus.util.Points.D2.IntIntPoint;
import com.landawn.abacus.util.Points.D2.IntLongPoint;
import com.landawn.abacus.util.Points.D2.IntObjPoint;
import com.landawn.abacus.util.Points.D2.LongBytePoint;
import com.landawn.abacus.util.Points.D2.LongDoublePoint;
import com.landawn.abacus.util.Points.D2.LongIntPoint;
import com.landawn.abacus.util.Points.D2.LongLongPoint;
import com.landawn.abacus.util.Points.D2.LongObjPoint;
import com.landawn.abacus.util.Points.D3;

class PointsTest extends TestBase {

    @Test
    public void testByteBytePointEqualsAndHashCode() {
        Points.D2.ByteBytePoint point1 = Points.D2.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 15);
        Points.D2.ByteBytePoint point2 = Points.D2.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 15);
        Points.D2.ByteBytePoint point3 = Points.D2.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 16);

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
        Points.D2.ByteBytePoint point = Points.D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
        String str = point.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
        assertEquals("ByteBytePoint[x=1, y=2, value=3]", str);
    }

    @Test
    public void testByteIntPoint() {
        Points.D2.ByteIntPoint point = Points.D2.ByteIntPoint.of((byte) 5, (byte) 10, 100);

        assertEquals(5, point.x());
        assertEquals(10, point.y());
        assertEquals(100, point.value());
    }

    @Test
    public void testByteIntPointEqualsAndHashCode() {
        Points.D2.ByteIntPoint point1 = Points.D2.ByteIntPoint.of((byte) 1, (byte) 2, 300);
        Points.D2.ByteIntPoint point2 = Points.D2.ByteIntPoint.of((byte) 1, (byte) 2, 300);
        Points.D2.ByteIntPoint point3 = Points.D2.ByteIntPoint.of((byte) 1, (byte) 2, 301);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));
        assertTrue(point1.equals(point1));

        assertEquals(point1.hashCode(), point2.hashCode());
        assertNotEquals(point1.hashCode(), point3.hashCode());
    }

    @Test
    public void testIntIntPointEqualsAndHashCode() {
        Points.D2.IntIntPoint point1 = Points.D2.IntIntPoint.of(10, 20, 30);
        Points.D2.IntIntPoint point2 = Points.D2.IntIntPoint.of(10, 20, 30);
        Points.D2.IntIntPoint point3 = Points.D2.IntIntPoint.of(10, 20, 31);
        Points.D2.IntIntPoint point4 = Points.D2.IntIntPoint.of(11, 20, 30);

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
        Points.D2.LongLongPoint point = Points.D2.LongLongPoint.of(1000L, 2000L, 3000L);

        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(3000L, point.value());
    }

    @Test
    public void testLongLongPointEqualsAndHashCode() {
        Points.D2.LongLongPoint point1 = Points.D2.LongLongPoint.of(100L, 200L, 300L);
        Points.D2.LongLongPoint point2 = Points.D2.LongLongPoint.of(100L, 200L, 300L);
        Points.D2.LongLongPoint point3 = Points.D2.LongLongPoint.of(100L, 200L, 301L);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));

        assertEquals(point1.hashCode(), point2.hashCode());
        assertNotEquals(point1.hashCode(), point3.hashCode());
    }

    @Test
    public void testDoubleDoublePoint() {
        Points.D2.DoubleDoublePoint point = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 30.9);

        assertEquals(10.5, point.x(), 0.001);
        assertEquals(20.7, point.y(), 0.001);
        assertEquals(30.9, point.value(), 0.001);
    }

    @Test
    public void testDoubleDoublePointEqualsAndHashCode() {
        Points.D2.DoubleDoublePoint point1 = Points.D2.DoubleDoublePoint.of(1.1, 2.2, 3.3);
        Points.D2.DoubleDoublePoint point2 = Points.D2.DoubleDoublePoint.of(1.1, 2.2, 3.3);
        Points.D2.DoubleDoublePoint point3 = Points.D2.DoubleDoublePoint.of(1.1, 2.2, 3.4);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));

        assertEquals(point1.hashCode(), point2.hashCode());
        // Record uses default hashCode which includes all fields
        assertNotEquals(point1.hashCode(), point3.hashCode());
    }

    @Test
    public void testIntObjPoint() {
        String value = "test value";
        Points.D2.IntObjPoint<String> point = Points.D2.IntObjPoint.of(10, 20, value);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(value, point.value());
    }

    @Test
    public void testIntObjPointEqualsAndHashCode() {
        Points.D2.IntObjPoint<String> point1 = Points.D2.IntObjPoint.of(1, 2, "hello");
        Points.D2.IntObjPoint<String> point2 = Points.D2.IntObjPoint.of(1, 2, "hello");
        Points.D2.IntObjPoint<String> point3 = Points.D2.IntObjPoint.of(1, 2, "world");
        Points.D2.IntObjPoint<String> point4 = Points.D2.IntObjPoint.of(1, 2, null);
        Points.D2.IntObjPoint<String> point5 = Points.D2.IntObjPoint.of(1, 2, null);

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
        Points.D2.DoubleObjPoint<Integer> point = Points.D2.DoubleObjPoint.of(1.5, 2.5, value);

        assertEquals(1.5, point.x(), 0.001);
        assertEquals(2.5, point.y(), 0.001);
        assertEquals(value, point.value());
    }

    @Test
    public void testMixedTypePoints() {
        // Test different combinations of coordinate and value types
        Points.D2.ByteLongPoint byteLong = Points.D2.ByteLongPoint.of((byte) 1, (byte) 2, 1000L);
        Points.D2.IntDoublePoint intDouble = Points.D2.IntDoublePoint.of(10, 20, 30.5);
        Points.D2.LongIntPoint longInt = Points.D2.LongIntPoint.of(100L, 200L, 300);

        assertEquals(1, byteLong.x());
        assertEquals(2, byteLong.y());
        assertEquals(1000L, byteLong.value());

        assertEquals(10, intDouble.x());
        assertEquals(20, intDouble.y());
        assertEquals(30.5, intDouble.value(), 0.001);

        assertEquals(100L, longInt.x());
        assertEquals(200L, longInt.y());
        assertEquals(300, longInt.value());
    }

    @Test
    public void testPointToStringFormats() {
        Points.D2.IntIntPoint intPoint = Points.D2.IntIntPoint.of(1, 2, 3);
        Points.D2.DoubleDoublePoint doublePoint = Points.D2.DoubleDoublePoint.of(1.1, 2.2, 3.3);
        Points.D2.IntObjPoint<String> objPoint = Points.D2.IntObjPoint.of(1, 2, "test");

        assertEquals("IntIntPoint[x=1, y=2, value=3]", intPoint.toString());
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
        assertFalse(Points.D2.class == null);

        // Verify that we can access the point classes through the utility class
        Points.D2.IntIntPoint point = Points.D2.IntIntPoint.of(1, 2, 3);
        assertEquals(1, point.x());
    }

    @Test
    public void testPointImmutability() {
        // Test that points are immutable by design
        Points.D2.IntIntPoint point = Points.D2.IntIntPoint.of(10, 20, 30);

        // Fields are final - cannot be changed after construction
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(30, point.value());

        // Values should remain the same
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(30, point.value());
    }

    @Test
    public void testDifferentPointTypesNotEqual() {
        // Test that different point types with same values are not equal
        Points.D2.IntIntPoint intPoint = Points.D2.IntIntPoint.of(1, 2, 3);
        Points.D2.LongLongPoint longPoint = Points.D2.LongLongPoint.of(1L, 2L, 3L);

        assertFalse(intPoint.equals(longPoint));
        assertFalse(longPoint.equals(intPoint));
    }

    // Additional comprehensive tests for all remaining point types

    @Test
    public void testByteLongPoint() {
        Points.D2.ByteLongPoint point = Points.D2.ByteLongPoint.of((byte) 10, (byte) 20, 1000L);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(1000L, point.value());

        // Test equals and hashCode
        Points.D2.ByteLongPoint point2 = Points.D2.ByteLongPoint.of((byte) 10, (byte) 20, 1000L);
        Points.D2.ByteLongPoint point3 = Points.D2.ByteLongPoint.of((byte) 10, (byte) 20, 1001L);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("ByteLongPoint[x=10, y=20, value=1000]", point.toString());
    }

    @Test
    public void testByteDoublePoint() {
        Points.D2.ByteDoublePoint point = Points.D2.ByteDoublePoint.of((byte) 5, (byte) 10, 15.5);

        assertEquals(5, point.x());
        assertEquals(10, point.y());
        assertEquals(15.5, point.value(), 0.001);

        // Test equals and hashCode
        Points.D2.ByteDoublePoint point2 = Points.D2.ByteDoublePoint.of((byte) 5, (byte) 10, 15.5);
        Points.D2.ByteDoublePoint point3 = Points.D2.ByteDoublePoint.of((byte) 5, (byte) 10, 15.6);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());

        // Test toString
        assertTrue(point.toString().contains("15.5"));
    }

    @Test
    public void testByteObjPoint() {
        String value = "test string";
        Points.D2.ByteObjPoint<String> point = Points.D2.ByteObjPoint.of((byte) 1, (byte) 2, value);

        assertEquals(1, point.x());
        assertEquals(2, point.y());
        assertEquals(value, point.value());

        // Test equals and hashCode with null values
        Points.D2.ByteObjPoint<String> point1 = Points.D2.ByteObjPoint.of((byte) 1, (byte) 2, "hello");
        Points.D2.ByteObjPoint<String> point2 = Points.D2.ByteObjPoint.of((byte) 1, (byte) 2, "hello");
        Points.D2.ByteObjPoint<String> point3 = Points.D2.ByteObjPoint.of((byte) 1, (byte) 2, "world");
        Points.D2.ByteObjPoint<String> point4 = Points.D2.ByteObjPoint.of((byte) 1, (byte) 2, null);
        Points.D2.ByteObjPoint<String> point5 = Points.D2.ByteObjPoint.of((byte) 1, (byte) 2, null);

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
        Points.D2.IntBytePoint point = Points.D2.IntBytePoint.of(100, 200, (byte) 50);

        assertEquals(100, point.x());
        assertEquals(200, point.y());
        assertEquals(50, point.value());

        // Test equals and hashCode
        Points.D2.IntBytePoint point2 = Points.D2.IntBytePoint.of(100, 200, (byte) 50);
        Points.D2.IntBytePoint point3 = Points.D2.IntBytePoint.of(100, 200, (byte) 51);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("IntBytePoint[x=100, y=200, value=50]", point.toString());
    }

    @Test
    public void testIntLongPoint() {
        Points.D2.IntLongPoint point = Points.D2.IntLongPoint.of(10, 20, 5000L);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(5000L, point.value());

        // Test equals and hashCode
        Points.D2.IntLongPoint point2 = Points.D2.IntLongPoint.of(10, 20, 5000L);
        Points.D2.IntLongPoint point3 = Points.D2.IntLongPoint.of(10, 20, 5001L);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("IntLongPoint[x=10, y=20, value=5000]", point.toString());
    }

    @Test
    public void testLongBytePoint() {
        Points.D2.LongBytePoint point = Points.D2.LongBytePoint.of(1000L, 2000L, (byte) 100);

        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(100, point.value());

        // Test equals and hashCode
        Points.D2.LongBytePoint point2 = Points.D2.LongBytePoint.of(1000L, 2000L, (byte) 100);
        Points.D2.LongBytePoint point3 = Points.D2.LongBytePoint.of(1000L, 2000L, (byte) 101);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("LongBytePoint[x=1000, y=2000, value=100]", point.toString());
    }

    @Test
    public void testLongIntPoint() {
        Points.D2.LongIntPoint point = Points.D2.LongIntPoint.of(500L, 1000L, 250);

        assertEquals(500L, point.x());
        assertEquals(1000L, point.y());
        assertEquals(250, point.value());

        // Test equals and hashCode
        Points.D2.LongIntPoint point2 = Points.D2.LongIntPoint.of(500L, 1000L, 250);
        Points.D2.LongIntPoint point3 = Points.D2.LongIntPoint.of(500L, 1000L, 251);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("LongIntPoint[x=500, y=1000, value=250]", point.toString());
    }

    @Test
    public void testLongDoublePoint() {
        Points.D2.LongDoublePoint point = Points.D2.LongDoublePoint.of(100L, 200L, 300.75);

        assertEquals(100L, point.x());
        assertEquals(200L, point.y());
        assertEquals(300.75, point.value(), 0.001);

        // Test equals and hashCode
        Points.D2.LongDoublePoint point2 = Points.D2.LongDoublePoint.of(100L, 200L, 300.75);
        Points.D2.LongDoublePoint point3 = Points.D2.LongDoublePoint.of(100L, 200L, 300.76);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());

        // Test toString
        assertTrue(point.toString().contains("300.75"));
    }

    @Test
    public void testLongObjPoint() {
        Integer value = 42;
        Points.D2.LongObjPoint<Integer> point = Points.D2.LongObjPoint.of(10L, 20L, value);

        assertEquals(10L, point.x());
        assertEquals(20L, point.y());
        assertEquals(value, point.value());

        // Test equals and hashCode with null values
        Points.D2.LongObjPoint<Integer> point1 = Points.D2.LongObjPoint.of(1L, 2L, 100);
        Points.D2.LongObjPoint<Integer> point2 = Points.D2.LongObjPoint.of(1L, 2L, 100);
        Points.D2.LongObjPoint<Integer> point3 = Points.D2.LongObjPoint.of(1L, 2L, 101);
        Points.D2.LongObjPoint<Integer> point4 = Points.D2.LongObjPoint.of(1L, 2L, null);
        Points.D2.LongObjPoint<Integer> point5 = Points.D2.LongObjPoint.of(1L, 2L, null);

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
        Points.D2.DoubleBytePoint point = Points.D2.DoubleBytePoint.of(10.5, 20.7, (byte) 30);

        assertEquals(10.5, point.x(), 0.001);
        assertEquals(20.7, point.y(), 0.001);
        assertEquals(30, point.value());

        // Test equals and hashCode
        Points.D2.DoubleBytePoint point2 = Points.D2.DoubleBytePoint.of(10.5, 20.7, (byte) 30);
        Points.D2.DoubleBytePoint point3 = Points.D2.DoubleBytePoint.of(10.5, 20.7, (byte) 31);

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
        Points.D2.DoubleIntPoint point = Points.D2.DoubleIntPoint.of(1.1, 2.2, 300);

        assertEquals(1.1, point.x(), 0.001);
        assertEquals(2.2, point.y(), 0.001);
        assertEquals(300, point.value());

        // Test equals and hashCode
        Points.D2.DoubleIntPoint point2 = Points.D2.DoubleIntPoint.of(1.1, 2.2, 300);
        Points.D2.DoubleIntPoint point3 = Points.D2.DoubleIntPoint.of(1.1, 2.2, 301);

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
        Points.D2.DoubleLongPoint point = Points.D2.DoubleLongPoint.of(5.5, 10.75, 1000000L);

        assertEquals(5.5, point.x(), 0.001);
        assertEquals(10.75, point.y(), 0.001);
        assertEquals(1000000L, point.value());

        // Test equals and hashCode
        Points.D2.DoubleLongPoint point2 = Points.D2.DoubleLongPoint.of(5.5, 10.75, 1000000L);
        Points.D2.DoubleLongPoint point3 = Points.D2.DoubleLongPoint.of(5.5, 10.75, 1000001L);

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
        Points.D2.DoubleObjPoint<String> stringPoint = Points.D2.DoubleObjPoint.of(1.0, 2.0, stringValue);

        assertEquals(1.0, stringPoint.x(), 0.001);
        assertEquals(2.0, stringPoint.y(), 0.001);
        assertEquals(stringValue, stringPoint.value());

        // Test equals and hashCode with null values
        Points.D2.DoubleObjPoint<String> point1 = Points.D2.DoubleObjPoint.of(1.5, 2.5, "hello");
        Points.D2.DoubleObjPoint<String> point2 = Points.D2.DoubleObjPoint.of(1.5, 2.5, "hello");
        Points.D2.DoubleObjPoint<String> point3 = Points.D2.DoubleObjPoint.of(1.5, 2.5, "world");
        Points.D2.DoubleObjPoint<String> point4 = Points.D2.DoubleObjPoint.of(1.5, 2.5, null);
        Points.D2.DoubleObjPoint<String> point5 = Points.D2.DoubleObjPoint.of(1.5, 2.5, null);

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
        Points.D2.ByteBytePoint minBytePoint = Points.D2.ByteBytePoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0);
        assertEquals(Byte.MIN_VALUE, minBytePoint.x());
        assertEquals(Byte.MAX_VALUE, minBytePoint.y());
        assertEquals(0, minBytePoint.value());

        Points.D2.IntIntPoint maxIntPoint = Points.D2.IntIntPoint.of(Integer.MAX_VALUE, Integer.MIN_VALUE, 0);
        assertEquals(Integer.MAX_VALUE, maxIntPoint.x());
        assertEquals(Integer.MIN_VALUE, maxIntPoint.y());
        assertEquals(0, maxIntPoint.value());

        Points.D2.LongLongPoint maxLongPoint = Points.D2.LongLongPoint.of(Long.MAX_VALUE, Long.MIN_VALUE, 0L);
        assertEquals(Long.MAX_VALUE, maxLongPoint.x());
        assertEquals(Long.MIN_VALUE, maxLongPoint.y());
        assertEquals(0L, maxLongPoint.value());

        // Test with special double values
        Points.D2.DoubleDoublePoint specialDoublePoint = Points.D2.DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN);
        assertEquals(Double.POSITIVE_INFINITY, specialDoublePoint.x(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, specialDoublePoint.y(), 0.0);
        assertTrue(Double.isNaN(specialDoublePoint.value()));
    }

    @Test
    public void testAllPointTypesWithSelfEquals() {
        // Test that all point types equal themselves
        Points.D2.ByteBytePoint bp1 = Points.D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
        Points.D2.ByteIntPoint bp2 = Points.D2.ByteIntPoint.of((byte) 1, (byte) 2, 3);
        Points.D2.ByteLongPoint bp3 = Points.D2.ByteLongPoint.of((byte) 1, (byte) 2, 3L);
        Points.D2.ByteDoublePoint bp4 = Points.D2.ByteDoublePoint.of((byte) 1, (byte) 2, 3.0);
        Points.D2.ByteObjPoint<String> bp5 = Points.D2.ByteObjPoint.of((byte) 1, (byte) 2, "3");

        Points.D2.IntBytePoint ip1 = Points.D2.IntBytePoint.of(1, 2, (byte) 3);
        Points.D2.IntIntPoint ip2 = Points.D2.IntIntPoint.of(1, 2, 3);
        Points.D2.IntLongPoint ip3 = Points.D2.IntLongPoint.of(1, 2, 3L);
        Points.D2.IntDoublePoint ip4 = Points.D2.IntDoublePoint.of(1, 2, 3.0);
        Points.D2.IntObjPoint<String> ip5 = Points.D2.IntObjPoint.of(1, 2, "3");

        Points.D2.LongBytePoint lp1 = Points.D2.LongBytePoint.of(1L, 2L, (byte) 3);
        Points.D2.LongIntPoint lp2 = Points.D2.LongIntPoint.of(1L, 2L, 3);
        Points.D2.LongLongPoint lp3 = Points.D2.LongLongPoint.of(1L, 2L, 3L);
        Points.D2.LongDoublePoint lp4 = Points.D2.LongDoublePoint.of(1L, 2L, 3.0);
        Points.D2.LongObjPoint<String> lp5 = Points.D2.LongObjPoint.of(1L, 2L, "3");

        Points.D2.DoubleBytePoint dp1 = Points.D2.DoubleBytePoint.of(1.0, 2.0, (byte) 3);
        Points.D2.DoubleIntPoint dp2 = Points.D2.DoubleIntPoint.of(1.0, 2.0, 3);
        Points.D2.DoubleLongPoint dp3 = Points.D2.DoubleLongPoint.of(1.0, 2.0, 3L);
        Points.D2.DoubleDoublePoint dp4 = Points.D2.DoubleDoublePoint.of(1.0, 2.0, 3.0);
        Points.D2.DoubleObjPoint<String> dp5 = Points.D2.DoubleObjPoint.of(1.0, 2.0, "3");

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
        assertFalse(Points.D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3).equals(null));
        assertFalse(Points.D2.IntIntPoint.of(1, 2, 3).equals(null));
        assertFalse(Points.D2.LongLongPoint.of(1L, 2L, 3L).equals(null));
        assertFalse(Points.D2.DoubleDoublePoint.of(1.0, 2.0, 3.0).equals(null));
        assertFalse(Points.D2.ByteObjPoint.of((byte) 1, (byte) 2, "test").equals(null));
        assertFalse(Points.D2.IntObjPoint.of(1, 2, "test").equals(null));
        assertFalse(Points.D2.LongObjPoint.of(1L, 2L, "test").equals(null));
        assertFalse(Points.D2.DoubleObjPoint.of(1.0, 2.0, "test").equals(null));
    }

    @Nested
    class JavadocExampleOtherTest_Points extends TestBase {
        // ===== Points.D2 2D examples =====

        @Test
        public void testXyIntIntPoint() {
            // From class-level Javadoc and IntIntPoint.of Javadoc
            Points.D2.IntIntPoint point2D = Points.D2.IntIntPoint.of(10, 20, 100);
            assertEquals(10, point2D.x());
            assertEquals(20, point2D.y());
            assertEquals(100, point2D.value());
        }

        @Test
        public void testXyIntIntPointPathfinding() {
            // From IntIntPoint.of Javadoc: "Use in pathfinding with cost values"
            Points.D2.IntIntPoint point = Points.D2.IntIntPoint.of(100, 200, 300);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(300, point.value());

            Points.D2.IntIntPoint pathNode = Points.D2.IntIntPoint.of(5, 8, 15);
            assertEquals(5, pathNode.x());
            assertEquals(8, pathNode.y());
            assertEquals(15, pathNode.value());
        }

        @Test
        public void testXyByteBytePoint() {
            // From ByteBytePoint.of Javadoc
            Points.D2.ByteBytePoint point = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 5);
            assertEquals((byte) 10, point.x());
            assertEquals((byte) 20, point.y());
            assertEquals((byte) 5, point.value());
        }

        @Test
        public void testXyByteIntPoint() {
            // From ByteIntPoint.of Javadoc
            Points.D2.ByteIntPoint point = Points.D2.ByteIntPoint.of((byte) 5, (byte) 10, 1000);
            assertEquals((byte) 5, point.x());
            assertEquals((byte) 10, point.y());
            assertEquals(1000, point.value());
        }

        @Test
        public void testXyByteLongPoint() {
            // From ByteLongPoint.of Javadoc
            Points.D2.ByteLongPoint point = Points.D2.ByteLongPoint.of((byte) 3, (byte) 7, 1000000000L);
            assertEquals((byte) 3, point.x());
            assertEquals((byte) 7, point.y());
            assertEquals(1000000000L, point.value());
        }

        @Test
        public void testXyByteDoublePoint() {
            // From ByteDoublePoint.of Javadoc
            Points.D2.ByteDoublePoint point = Points.D2.ByteDoublePoint.of((byte) 2, (byte) 4, 3.14159);
            assertEquals((byte) 2, point.x());
            assertEquals((byte) 4, point.y());
            assertEquals(3.14159, point.value(), 0.00001);
        }

        @Test
        public void testXyByteObjPoint() {
            // From ByteObjPoint.of Javadoc
            Points.D2.ByteObjPoint<String> point = Points.D2.ByteObjPoint.of((byte) 1, (byte) 2, "label");
            assertEquals((byte) 1, point.x());
            assertEquals((byte) 2, point.y());
            assertEquals("label", point.value());
        }

        @Test
        public void testXyIntBytePoint() {
            // From IntBytePoint.of Javadoc
            Points.D2.IntBytePoint point = Points.D2.IntBytePoint.of(100, 200, (byte) 10);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals((byte) 10, point.value());
        }

        @Test
        public void testXyIntLongPoint() {
            // From IntLongPoint.of Javadoc
            Points.D2.IntLongPoint point = Points.D2.IntLongPoint.of(50, 75, 10000000000L);
            assertEquals(50, point.x());
            assertEquals(75, point.y());
            assertEquals(10000000000L, point.value());
        }

        @Test
        public void testXyIntDoublePoint() {
            // From IntDoublePoint.of Javadoc
            Points.D2.IntDoublePoint point = Points.D2.IntDoublePoint.of(10, 20, 3.14159);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(3.14159, point.value(), 0.00001);
        }

        @Test
        public void testXyIntObjPoint() {
            // From IntObjPoint.of Javadoc
            Points.D2.IntObjPoint<String> point = Points.D2.IntObjPoint.of(10, 20, "label");
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals("label", point.value());
        }

        @Test
        public void testXyLongBytePoint() {
            // From LongBytePoint.of Javadoc
            Points.D2.LongBytePoint point = Points.D2.LongBytePoint.of(1000000L, 2000000L, (byte) 5);
            assertEquals(1000000L, point.x());
            assertEquals(2000000L, point.y());
            assertEquals((byte) 5, point.value());
        }

        @Test
        public void testXyLongIntPoint() {
            // From LongIntPoint.of Javadoc
            Points.D2.LongIntPoint point = Points.D2.LongIntPoint.of(1000000L, 2000000L, 500);
            assertEquals(1000000L, point.x());
            assertEquals(2000000L, point.y());
            assertEquals(500, point.value());
        }

        @Test
        public void testXyLongLongPoint() {
            // From LongLongPoint.of Javadoc
            Points.D2.LongLongPoint point = Points.D2.LongLongPoint.of(1000000L, 2000000L, 3000000000L);
            assertEquals(1000000L, point.x());
            assertEquals(2000000L, point.y());
            assertEquals(3000000000L, point.value());
        }

        @Test
        public void testXyLongDoublePoint() {
            // From LongDoublePoint.of Javadoc
            Points.D2.LongDoublePoint point = Points.D2.LongDoublePoint.of(1000000L, 2000000L, 3.14159);
            assertEquals(1000000L, point.x());
            assertEquals(2000000L, point.y());
            assertEquals(3.14159, point.value(), 0.00001);
        }

        @Test
        public void testXyLongObjPoint() {
            // From LongObjPoint.of Javadoc
            Points.D2.LongObjPoint<String> point = Points.D2.LongObjPoint.of(1000000L, 2000000L, "marker");
            assertEquals(1000000L, point.x());
            assertEquals(2000000L, point.y());
            assertEquals("marker", point.value());
        }

        @Test
        public void testXyDoubleBytePoint() {
            // From DoubleBytePoint.of Javadoc
            Points.D2.DoubleBytePoint point = Points.D2.DoubleBytePoint.of(10.5, 20.7, (byte) 3);
            assertEquals(10.5, point.x(), 0.00001);
            assertEquals(20.7, point.y(), 0.00001);
            assertEquals((byte) 3, point.value());
        }

        @Test
        public void testXyDoubleIntPoint() {
            // From DoubleIntPoint.of Javadoc
            Points.D2.DoubleIntPoint point = Points.D2.DoubleIntPoint.of(10.5, 20.7, 100);
            assertEquals(10.5, point.x(), 0.00001);
            assertEquals(20.7, point.y(), 0.00001);
            assertEquals(100, point.value());
        }

        @Test
        public void testXyDoubleLongPoint() {
            // From DoubleLongPoint.of Javadoc
            Points.D2.DoubleLongPoint point = Points.D2.DoubleLongPoint.of(10.5, 20.7, 1000000000L);
            assertEquals(10.5, point.x(), 0.00001);
            assertEquals(20.7, point.y(), 0.00001);
            assertEquals(1000000000L, point.value());
        }

        @Test
        public void testXyDoubleDoublePoint() {
            // From DoubleDoublePoint.of Javadoc
            Points.D2.DoubleDoublePoint point = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 3.14159);
            assertEquals(10.5, point.x(), 0.00001);
            assertEquals(20.7, point.y(), 0.00001);
            assertEquals(3.14159, point.value(), 0.00001);
        }

        @Test
        public void testXyDoubleObjPoint() {
            // From DoubleObjPoint.of Javadoc
            Points.D2.DoubleObjPoint<String> point = Points.D2.DoubleObjPoint.of(10.5, 20.7, "location");
            assertEquals(10.5, point.x(), 0.00001);
            assertEquals(20.7, point.y(), 0.00001);
            assertEquals("location", point.value());
        }

        // ===== Points.D3 3D examples =====

        @Test
        public void testXyzIntIntPoint() {
            // From class-level Javadoc
            Points.D3.IntIntPoint point3D = Points.D3.IntIntPoint.of(10, 20, 30, 100);
            assertEquals(10, point3D.x());
            assertEquals(20, point3D.y());
            assertEquals(30, point3D.z());
            assertEquals(100, point3D.value());
        }

        @Test
        public void testXyzByteBytePoint() {
            // From D3.ByteBytePoint.of Javadoc
            Points.D3.ByteBytePoint point = Points.D3.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30, (byte) 5);
            assertEquals((byte) 10, point.x());
            assertEquals((byte) 20, point.y());
            assertEquals((byte) 30, point.z());
            assertEquals((byte) 5, point.value());
        }

        @Test
        public void testXyzByteIntPoint() {
            // From D3.ByteIntPoint.of Javadoc
            Points.D3.ByteIntPoint point = Points.D3.ByteIntPoint.of((byte) 5, (byte) 10, (byte) 15, 1000);
            assertEquals((byte) 5, point.x());
            assertEquals((byte) 10, point.y());
            assertEquals((byte) 15, point.z());
            assertEquals(1000, point.value());
        }

        @Test
        public void testXyzByteLongPoint() {
            // From D3.ByteLongPoint.of Javadoc
            Points.D3.ByteLongPoint point = Points.D3.ByteLongPoint.of((byte) 3, (byte) 7, (byte) 11, 1000000000L);
            assertEquals((byte) 3, point.x());
            assertEquals((byte) 7, point.y());
            assertEquals((byte) 11, point.z());
            assertEquals(1000000000L, point.value());
        }

        @Test
        public void testXyzByteDoublePoint() {
            // From D3.ByteDoublePoint.of Javadoc
            Points.D3.ByteDoublePoint point = Points.D3.ByteDoublePoint.of((byte) 2, (byte) 4, (byte) 6, 3.14159);
            assertEquals((byte) 2, point.x());
            assertEquals((byte) 4, point.y());
            assertEquals((byte) 6, point.z());
            assertEquals(3.14159, point.value(), 0.00001);
        }

        @Test
        public void testXyzByteObjPoint() {
            // From D3.ByteObjPoint.of Javadoc
            Points.D3.ByteObjPoint<String> point = Points.D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "voxel");
            assertEquals((byte) 1, point.x());
            assertEquals((byte) 2, point.y());
            assertEquals((byte) 3, point.z());
            assertEquals("voxel", point.value());
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for the Points utility class and all its nested point classes.
     * Tests cover all point types with different coordinate and value combinations,
     * verifying factory methods, field access, hashCode, equals, and toString methods.
     */
    @Tag("2025")
    class Points2025Test extends TestBase {

        // ============================================
        // Tests for ByteBytePoint
        // ============================================

        @Test
        public void testByteBytePoint_Of() {
            D2.ByteBytePoint point = D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 100);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(100, point.value());
        }

        @Test
        public void testByteBytePoint_HashCode() {
            D2.ByteBytePoint point1 = D2.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 15);
            D2.ByteBytePoint point2 = D2.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 15);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testByteBytePoint_Equals() {
            D2.ByteBytePoint point1 = D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
            D2.ByteBytePoint point2 = D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
            D2.ByteBytePoint point3 = D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 4);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
            assertTrue(point1.equals(point1));
        }

        @Test
        public void testByteBytePoint_ToString() {
            D2.ByteBytePoint point = D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
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
            D2.ByteIntPoint point = D2.ByteIntPoint.of((byte) 10, (byte) 20, 100);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(100, point.value());
        }

        @Test
        public void testByteIntPoint_HashCode() {
            D2.ByteIntPoint point1 = D2.ByteIntPoint.of((byte) 5, (byte) 10, 500);
            D2.ByteIntPoint point2 = D2.ByteIntPoint.of((byte) 5, (byte) 10, 500);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testByteIntPoint_Equals() {
            D2.ByteIntPoint point1 = D2.ByteIntPoint.of((byte) 1, (byte) 2, 300);
            D2.ByteIntPoint point2 = D2.ByteIntPoint.of((byte) 1, (byte) 2, 300);
            D2.ByteIntPoint point3 = D2.ByteIntPoint.of((byte) 1, (byte) 2, 400);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testByteIntPoint_ToString() {
            D2.ByteIntPoint point = D2.ByteIntPoint.of((byte) 10, (byte) 20, 999);
            String result = point.toString();

            assertNotNull(result);
            assertTrue(result.contains("999"));
        }

        // ============================================
        // Tests for ByteLongPoint
        // ============================================

        @Test
        public void testByteLongPoint_Of() {
            D2.ByteLongPoint point = D2.ByteLongPoint.of((byte) 10, (byte) 20, 1000L);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(1000L, point.value());
        }

        @Test
        public void testByteLongPoint_HashCode() {
            D2.ByteLongPoint point1 = D2.ByteLongPoint.of((byte) 5, (byte) 10, 5000L);
            D2.ByteLongPoint point2 = D2.ByteLongPoint.of((byte) 5, (byte) 10, 5000L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testByteLongPoint_Equals() {
            D2.ByteLongPoint point1 = D2.ByteLongPoint.of((byte) 1, (byte) 2, 3000L);
            D2.ByteLongPoint point2 = D2.ByteLongPoint.of((byte) 1, (byte) 2, 3000L);
            D2.ByteLongPoint point3 = D2.ByteLongPoint.of((byte) 1, (byte) 2, 4000L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testByteLongPoint_ToString() {
            D2.ByteLongPoint point = D2.ByteLongPoint.of((byte) 10, (byte) 20, 99999L);
            String result = point.toString();

            assertNotNull(result);
            assertTrue(result.contains("99999"));
        }

        // ============================================
        // Tests for ByteDoublePoint
        // ============================================

        @Test
        public void testByteDoublePoint_Of() {
            D2.ByteDoublePoint point = D2.ByteDoublePoint.of((byte) 10, (byte) 20, 100.5);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(100.5, point.value(), 0.0001);
        }

        @Test
        public void testByteDoublePoint_HashCode() {
            D2.ByteDoublePoint point1 = D2.ByteDoublePoint.of((byte) 5, (byte) 10, 50.5);
            D2.ByteDoublePoint point2 = D2.ByteDoublePoint.of((byte) 5, (byte) 10, 50.5);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testByteDoublePoint_Equals() {
            D2.ByteDoublePoint point1 = D2.ByteDoublePoint.of((byte) 1, (byte) 2, 3.14);
            D2.ByteDoublePoint point2 = D2.ByteDoublePoint.of((byte) 1, (byte) 2, 3.14);
            D2.ByteDoublePoint point3 = D2.ByteDoublePoint.of((byte) 1, (byte) 2, 2.71);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testByteDoublePoint_ToString() {
            D2.ByteDoublePoint point = D2.ByteDoublePoint.of((byte) 10, (byte) 20, 99.99);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for ByteObjPoint
        // ============================================

        @Test
        public void testByteObjPoint_Of() {
            D2.ByteObjPoint<String> point = D2.ByteObjPoint.of((byte) 10, (byte) 20, "Test");

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals("Test", point.value());
        }

        @Test
        public void testByteObjPoint_HashCode() {
            D2.ByteObjPoint<String> point1 = D2.ByteObjPoint.of((byte) 5, (byte) 10, "Value");
            D2.ByteObjPoint<String> point2 = D2.ByteObjPoint.of((byte) 5, (byte) 10, "Value");

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testByteObjPoint_Equals() {
            D2.ByteObjPoint<String> point1 = D2.ByteObjPoint.of((byte) 1, (byte) 2, "ABC");
            D2.ByteObjPoint<String> point2 = D2.ByteObjPoint.of((byte) 1, (byte) 2, "ABC");
            D2.ByteObjPoint<String> point3 = D2.ByteObjPoint.of((byte) 1, (byte) 2, "XYZ");

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testByteObjPoint_ToString() {
            D2.ByteObjPoint<Integer> point = D2.ByteObjPoint.of((byte) 10, (byte) 20, 999);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for IntBytePoint
        // ============================================

        @Test
        public void testIntBytePoint_Of() {
            D2.IntBytePoint point = D2.IntBytePoint.of(100, 200, (byte) 50);

            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(50, point.value());
        }

        @Test
        public void testIntBytePoint_HashCode() {
            D2.IntBytePoint point1 = D2.IntBytePoint.of(10, 20, (byte) 30);
            D2.IntBytePoint point2 = D2.IntBytePoint.of(10, 20, (byte) 30);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testIntBytePoint_Equals() {
            D2.IntBytePoint point1 = D2.IntBytePoint.of(1, 2, (byte) 3);
            D2.IntBytePoint point2 = D2.IntBytePoint.of(1, 2, (byte) 3);
            D2.IntBytePoint point3 = D2.IntBytePoint.of(1, 2, (byte) 4);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testIntBytePoint_ToString() {
            D2.IntBytePoint point = D2.IntBytePoint.of(100, 200, (byte) 99);
            String result = point.toString();

            assertNotNull(result);
            assertTrue(result.contains("100"));
        }

        // ============================================
        // Tests for IntIntPoint
        // ============================================

        @Test
        public void testIntIntPoint_Of() {
            D2.IntIntPoint point = D2.IntIntPoint.of(10, 20, 100);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(100, point.value());
        }

        @Test
        public void testIntIntPoint_HashCode() {
            D2.IntIntPoint point1 = D2.IntIntPoint.of(5, 10, 15);
            D2.IntIntPoint point2 = D2.IntIntPoint.of(5, 10, 15);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testIntIntPoint_Equals() {
            D2.IntIntPoint point1 = D2.IntIntPoint.of(1, 2, 3);
            D2.IntIntPoint point2 = D2.IntIntPoint.of(1, 2, 3);
            D2.IntIntPoint point3 = D2.IntIntPoint.of(1, 2, 4);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testIntIntPoint_ToString() {
            D2.IntIntPoint point = D2.IntIntPoint.of(100, 200, 300);
            String result = point.toString();

            assertNotNull(result);
            assertTrue(result.contains("100"));
            assertTrue(result.contains("200"));
            assertTrue(result.contains("300"));
        }

        @Test
        public void testIntIntPoint_NegativeValues() {
            D2.IntIntPoint point = D2.IntIntPoint.of(-10, -20, -30);

            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(-30, point.value());
        }

        // ============================================
        // Tests for IntLongPoint
        // ============================================

        @Test
        public void testIntLongPoint_Of() {
            D2.IntLongPoint point = D2.IntLongPoint.of(10, 20, 1000L);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(1000L, point.value());
        }

        @Test
        public void testIntLongPoint_HashCode() {
            D2.IntLongPoint point1 = D2.IntLongPoint.of(5, 10, 5000L);
            D2.IntLongPoint point2 = D2.IntLongPoint.of(5, 10, 5000L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testIntLongPoint_Equals() {
            D2.IntLongPoint point1 = D2.IntLongPoint.of(1, 2, 3000L);
            D2.IntLongPoint point2 = D2.IntLongPoint.of(1, 2, 3000L);
            D2.IntLongPoint point3 = D2.IntLongPoint.of(1, 2, 4000L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testIntLongPoint_ToString() {
            D2.IntLongPoint point = D2.IntLongPoint.of(100, 200, 999999L);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for IntDoublePoint
        // ============================================

        @Test
        public void testIntDoublePoint_Of() {
            D2.IntDoublePoint point = D2.IntDoublePoint.of(10, 20, 100.5);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(100.5, point.value(), 0.0001);
        }

        @Test
        public void testIntDoublePoint_HashCode() {
            D2.IntDoublePoint point1 = D2.IntDoublePoint.of(5, 10, 50.5);
            D2.IntDoublePoint point2 = D2.IntDoublePoint.of(5, 10, 50.5);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testIntDoublePoint_Equals() {
            D2.IntDoublePoint point1 = D2.IntDoublePoint.of(1, 2, 3.14);
            D2.IntDoublePoint point2 = D2.IntDoublePoint.of(1, 2, 3.14);
            D2.IntDoublePoint point3 = D2.IntDoublePoint.of(1, 2, 2.71);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testIntDoublePoint_ToString() {
            D2.IntDoublePoint point = D2.IntDoublePoint.of(100, 200, 99.99);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for IntObjPoint
        // ============================================

        @Test
        public void testIntObjPoint_Of() {
            D2.IntObjPoint<String> point = D2.IntObjPoint.of(10, 20, "TestValue");

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals("TestValue", point.value());
        }

        @Test
        public void testIntObjPoint_HashCode() {
            D2.IntObjPoint<String> point1 = D2.IntObjPoint.of(5, 10, "ABC");
            D2.IntObjPoint<String> point2 = D2.IntObjPoint.of(5, 10, "ABC");

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testIntObjPoint_Equals() {
            D2.IntObjPoint<String> point1 = D2.IntObjPoint.of(1, 2, "Value");
            D2.IntObjPoint<String> point2 = D2.IntObjPoint.of(1, 2, "Value");
            D2.IntObjPoint<String> point3 = D2.IntObjPoint.of(1, 2, "Other");

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testIntObjPoint_ToString() {
            D2.IntObjPoint<Integer> point = D2.IntObjPoint.of(100, 200, 999);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for LongBytePoint
        // ============================================

        @Test
        public void testLongBytePoint_Of() {
            D2.LongBytePoint point = D2.LongBytePoint.of(1000L, 2000L, (byte) 50);

            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(50, point.value());
        }

        @Test
        public void testLongBytePoint_HashCode() {
            D2.LongBytePoint point1 = D2.LongBytePoint.of(100L, 200L, (byte) 30);
            D2.LongBytePoint point2 = D2.LongBytePoint.of(100L, 200L, (byte) 30);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testLongBytePoint_Equals() {
            D2.LongBytePoint point1 = D2.LongBytePoint.of(10L, 20L, (byte) 3);
            D2.LongBytePoint point2 = D2.LongBytePoint.of(10L, 20L, (byte) 3);
            D2.LongBytePoint point3 = D2.LongBytePoint.of(10L, 20L, (byte) 4);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testLongBytePoint_ToString() {
            D2.LongBytePoint point = D2.LongBytePoint.of(1000L, 2000L, (byte) 99);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for LongIntPoint
        // ============================================

        @Test
        public void testLongIntPoint_Of() {
            D2.LongIntPoint point = D2.LongIntPoint.of(1000L, 2000L, 100);

            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(100, point.value());
        }

        @Test
        public void testLongIntPoint_HashCode() {
            D2.LongIntPoint point1 = D2.LongIntPoint.of(100L, 200L, 300);
            D2.LongIntPoint point2 = D2.LongIntPoint.of(100L, 200L, 300);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testLongIntPoint_Equals() {
            D2.LongIntPoint point1 = D2.LongIntPoint.of(10L, 20L, 30);
            D2.LongIntPoint point2 = D2.LongIntPoint.of(10L, 20L, 30);
            D2.LongIntPoint point3 = D2.LongIntPoint.of(10L, 20L, 40);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testLongIntPoint_ToString() {
            D2.LongIntPoint point = D2.LongIntPoint.of(1000L, 2000L, 999);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for LongLongPoint
        // ============================================

        @Test
        public void testLongLongPoint_Of() {
            D2.LongLongPoint point = D2.LongLongPoint.of(1000L, 2000L, 3000L);

            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(3000L, point.value());
        }

        @Test
        public void testLongLongPoint_HashCode() {
            D2.LongLongPoint point1 = D2.LongLongPoint.of(100L, 200L, 300L);
            D2.LongLongPoint point2 = D2.LongLongPoint.of(100L, 200L, 300L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testLongLongPoint_Equals() {
            D2.LongLongPoint point1 = D2.LongLongPoint.of(10L, 20L, 30L);
            D2.LongLongPoint point2 = D2.LongLongPoint.of(10L, 20L, 30L);
            D2.LongLongPoint point3 = D2.LongLongPoint.of(10L, 20L, 40L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testLongLongPoint_ToString() {
            D2.LongLongPoint point = D2.LongLongPoint.of(1000L, 2000L, 3000L);
            String result = point.toString();

            assertNotNull(result);
            assertTrue(result.contains("1000"));
        }

        // ============================================
        // Tests for LongDoublePoint
        // ============================================

        @Test
        public void testLongDoublePoint_Of() {
            D2.LongDoublePoint point = D2.LongDoublePoint.of(1000L, 2000L, 100.5);

            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(100.5, point.value(), 0.0001);
        }

        @Test
        public void testLongDoublePoint_HashCode() {
            D2.LongDoublePoint point1 = D2.LongDoublePoint.of(100L, 200L, 50.5);
            D2.LongDoublePoint point2 = D2.LongDoublePoint.of(100L, 200L, 50.5);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testLongDoublePoint_Equals() {
            D2.LongDoublePoint point1 = D2.LongDoublePoint.of(10L, 20L, 3.14);
            D2.LongDoublePoint point2 = D2.LongDoublePoint.of(10L, 20L, 3.14);
            D2.LongDoublePoint point3 = D2.LongDoublePoint.of(10L, 20L, 2.71);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testLongDoublePoint_ToString() {
            D2.LongDoublePoint point = D2.LongDoublePoint.of(1000L, 2000L, 99.99);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for LongObjPoint
        // ============================================

        @Test
        public void testLongObjPoint_Of() {
            D2.LongObjPoint<String> point = D2.LongObjPoint.of(1000L, 2000L, "TestValue");

            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals("TestValue", point.value());
        }

        @Test
        public void testLongObjPoint_HashCode() {
            D2.LongObjPoint<String> point1 = D2.LongObjPoint.of(100L, 200L, "ABC");
            D2.LongObjPoint<String> point2 = D2.LongObjPoint.of(100L, 200L, "ABC");

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testLongObjPoint_Equals() {
            D2.LongObjPoint<String> point1 = D2.LongObjPoint.of(10L, 20L, "Value");
            D2.LongObjPoint<String> point2 = D2.LongObjPoint.of(10L, 20L, "Value");
            D2.LongObjPoint<String> point3 = D2.LongObjPoint.of(10L, 20L, "Other");

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testLongObjPoint_ToString() {
            D2.LongObjPoint<Integer> point = D2.LongObjPoint.of(1000L, 2000L, 999);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for DoubleBytePoint
        // ============================================

        @Test
        public void testDoubleBytePoint_Of() {
            D2.DoubleBytePoint point = D2.DoubleBytePoint.of(10.5, 20.5, (byte) 50);

            assertEquals(10.5, point.x(), 0.0001);
            assertEquals(20.5, point.y(), 0.0001);
            assertEquals(50, point.value());
        }

        @Test
        public void testDoubleBytePoint_HashCode() {
            D2.DoubleBytePoint point1 = D2.DoubleBytePoint.of(10.5, 20.5, (byte) 30);
            D2.DoubleBytePoint point2 = D2.DoubleBytePoint.of(10.5, 20.5, (byte) 30);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testDoubleBytePoint_Equals() {
            D2.DoubleBytePoint point1 = D2.DoubleBytePoint.of(1.5, 2.5, (byte) 3);
            D2.DoubleBytePoint point2 = D2.DoubleBytePoint.of(1.5, 2.5, (byte) 3);
            D2.DoubleBytePoint point3 = D2.DoubleBytePoint.of(1.5, 2.5, (byte) 4);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testDoubleBytePoint_ToString() {
            D2.DoubleBytePoint point = D2.DoubleBytePoint.of(10.5, 20.5, (byte) 99);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for DoubleIntPoint
        // ============================================

        @Test
        public void testDoubleIntPoint_Of() {
            D2.DoubleIntPoint point = D2.DoubleIntPoint.of(10.5, 20.5, 100);

            assertEquals(10.5, point.x(), 0.0001);
            assertEquals(20.5, point.y(), 0.0001);
            assertEquals(100, point.value());
        }

        @Test
        public void testDoubleIntPoint_HashCode() {
            D2.DoubleIntPoint point1 = D2.DoubleIntPoint.of(10.5, 20.5, 300);
            D2.DoubleIntPoint point2 = D2.DoubleIntPoint.of(10.5, 20.5, 300);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testDoubleIntPoint_Equals() {
            D2.DoubleIntPoint point1 = D2.DoubleIntPoint.of(1.5, 2.5, 30);
            D2.DoubleIntPoint point2 = D2.DoubleIntPoint.of(1.5, 2.5, 30);
            D2.DoubleIntPoint point3 = D2.DoubleIntPoint.of(1.5, 2.5, 40);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testDoubleIntPoint_ToString() {
            D2.DoubleIntPoint point = D2.DoubleIntPoint.of(10.5, 20.5, 999);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for DoubleLongPoint
        // ============================================

        @Test
        public void testDoubleLongPoint_Of() {
            D2.DoubleLongPoint point = D2.DoubleLongPoint.of(10.5, 20.5, 1000L);

            assertEquals(10.5, point.x(), 0.0001);
            assertEquals(20.5, point.y(), 0.0001);
            assertEquals(1000L, point.value());
        }

        @Test
        public void testDoubleLongPoint_HashCode() {
            D2.DoubleLongPoint point1 = D2.DoubleLongPoint.of(10.5, 20.5, 3000L);
            D2.DoubleLongPoint point2 = D2.DoubleLongPoint.of(10.5, 20.5, 3000L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testDoubleLongPoint_Equals() {
            D2.DoubleLongPoint point1 = D2.DoubleLongPoint.of(1.5, 2.5, 300L);
            D2.DoubleLongPoint point2 = D2.DoubleLongPoint.of(1.5, 2.5, 300L);
            D2.DoubleLongPoint point3 = D2.DoubleLongPoint.of(1.5, 2.5, 400L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testDoubleLongPoint_ToString() {
            D2.DoubleLongPoint point = D2.DoubleLongPoint.of(10.5, 20.5, 999999L);
            String result = point.toString();

            assertNotNull(result);
        }

        // ============================================
        // Tests for DoubleDoublePoint
        // ============================================

        @Test
        public void testDoubleDoublePoint_Of() {
            D2.DoubleDoublePoint point = D2.DoubleDoublePoint.of(10.5, 20.5, 100.5);

            assertEquals(10.5, point.x(), 0.0001);
            assertEquals(20.5, point.y(), 0.0001);
            assertEquals(100.5, point.value(), 0.0001);
        }

        @Test
        public void testDoubleDoublePoint_HashCode() {
            D2.DoubleDoublePoint point1 = D2.DoubleDoublePoint.of(10.5, 20.5, 50.5);
            D2.DoubleDoublePoint point2 = D2.DoubleDoublePoint.of(10.5, 20.5, 50.5);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testDoubleDoublePoint_Equals() {
            D2.DoubleDoublePoint point1 = D2.DoubleDoublePoint.of(1.5, 2.5, 3.14);
            D2.DoubleDoublePoint point2 = D2.DoubleDoublePoint.of(1.5, 2.5, 3.14);
            D2.DoubleDoublePoint point3 = D2.DoubleDoublePoint.of(1.5, 2.5, 2.71);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testDoubleDoublePoint_ToString() {
            D2.DoubleDoublePoint point = D2.DoubleDoublePoint.of(10.5, 20.5, 99.99);
            String result = point.toString();

            assertNotNull(result);
            assertTrue(result.contains("10.5"));
        }

        // ============================================
        // Tests for DoubleObjPoint
        // ============================================

        @Test
        public void testDoubleObjPoint_Of() {
            D2.DoubleObjPoint<String> point = D2.DoubleObjPoint.of(10.5, 20.5, "TestValue");

            assertEquals(10.5, point.x(), 0.0001);
            assertEquals(20.5, point.y(), 0.0001);
            assertEquals("TestValue", point.value());
        }

        @Test
        public void testDoubleObjPoint_HashCode() {
            D2.DoubleObjPoint<String> point1 = D2.DoubleObjPoint.of(10.5, 20.5, "ABC");
            D2.DoubleObjPoint<String> point2 = D2.DoubleObjPoint.of(10.5, 20.5, "ABC");

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testDoubleObjPoint_Equals() {
            D2.DoubleObjPoint<String> point1 = D2.DoubleObjPoint.of(1.5, 2.5, "Value");
            D2.DoubleObjPoint<String> point2 = D2.DoubleObjPoint.of(1.5, 2.5, "Value");
            D2.DoubleObjPoint<String> point3 = D2.DoubleObjPoint.of(1.5, 2.5, "Other");

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testDoubleObjPoint_ToString() {
            D2.DoubleObjPoint<Integer> point = D2.DoubleObjPoint.of(10.5, 20.5, 999);
            String result = point.toString();

            assertNotNull(result);
        }

        @Test
        public void testEquals_DifferentType() {
            D2.IntIntPoint point = D2.IntIntPoint.of(1, 2, 3);
            String notAPoint = "not a point";

            assertFalse(point.equals(notAPoint));
        }

        @Test
        public void testHashCode_DifferentPoints() {
            D2.IntIntPoint point1 = D2.IntIntPoint.of(1, 2, 3);
            D2.IntIntPoint point2 = D2.IntIntPoint.of(4, 5, 6);

            assertNotEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testIntIntPoint_ZeroValues() {
            D2.IntIntPoint point = D2.IntIntPoint.of(0, 0, 0);

            assertEquals(0, point.x());
            assertEquals(0, point.y());
            assertEquals(0, point.value());
        }

        @Test
        public void testDoubleDoublePoint_SpecialValues() {
            D2.DoubleDoublePoint point1 = D2.DoubleDoublePoint.of(Double.MAX_VALUE, Double.MIN_VALUE, 0.0);

            assertEquals(Double.MAX_VALUE, point1.x(), 0.0001);
            assertEquals(Double.MIN_VALUE, point1.y(), 0.0001);
            assertEquals(0.0, point1.value(), 0.0001);
        }

        @Test
        public void testObjPoint_ComplexObject() {
            java.util.List<Integer> list = java.util.Arrays.asList(1, 2, 3);
            D2.IntObjPoint<java.util.List<Integer>> point = D2.IntObjPoint.of(10, 20, list);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(list, point.value());
        }

        @Test
        public void testMultiplePointTypes_SameCoordinates() {
            // Create points with same coordinates but different types
            D2.IntIntPoint intPoint = D2.IntIntPoint.of(10, 20, 100);
            D2.LongLongPoint longPoint = D2.LongLongPoint.of(10L, 20L, 100L);
            D2.DoubleDoublePoint doublePoint = D2.DoubleDoublePoint.of(10.0, 20.0, 100.0);

            // Verify they don't equal each other (different types)
            assertFalse(intPoint.equals(longPoint));
            assertFalse(intPoint.equals(doublePoint));
            assertFalse(longPoint.equals(doublePoint));
        }

        // ============================================
        // Tests for D3.ByteBytePoint (3D)
        // ============================================

        @Test
        public void testXyzByteBytePoint_Of() {
            D3.ByteBytePoint point = D3.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);

            assertEquals(1, point.x());
            assertEquals(2, point.y());
            assertEquals(3, point.z());
            assertEquals(4, point.value());
        }

        @Test
        public void testXyzByteBytePoint_HashCode() {
            D3.ByteBytePoint point1 = D3.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            D3.ByteBytePoint point2 = D3.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzByteBytePoint_Equals() {
            D3.ByteBytePoint point1 = D3.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            D3.ByteBytePoint point2 = D3.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            D3.ByteBytePoint point3 = D3.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 5);
            D3.ByteBytePoint point4 = D3.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 4, (byte) 4);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
            assertFalse(point1.equals(point4));
            assertFalse(point1.equals(null));
            assertTrue(point1.equals(point1));
        }

        @Test
        public void testXyzByteBytePoint_ToString() {
            D3.ByteBytePoint point = D3.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            String str = point.toString();

            assertEquals("ByteBytePoint[x=1, y=2, z=3, value=4]", str);
        }

        // ============================================
        // Tests for D3.ByteIntPoint (3D)
        // ============================================

        @Test
        public void testXyzByteIntPoint_Of() {
            D3.ByteIntPoint point = D3.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);

            assertEquals(1, point.x());
            assertEquals(2, point.y());
            assertEquals(3, point.z());
            assertEquals(100, point.value());
        }

        @Test
        public void testXyzByteIntPoint_HashCode() {
            D3.ByteIntPoint point1 = D3.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);
            D3.ByteIntPoint point2 = D3.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzByteIntPoint_Equals() {
            D3.ByteIntPoint point1 = D3.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);
            D3.ByteIntPoint point2 = D3.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);
            D3.ByteIntPoint point3 = D3.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 101);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzByteIntPoint_ToString() {
            D3.ByteIntPoint point = D3.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 100);
            assertEquals("ByteIntPoint[x=1, y=2, z=3, value=100]", point.toString());
        }

        // ============================================
        // Tests for D3.ByteLongPoint (3D)
        // ============================================

        @Test
        public void testXyzByteLongPoint_Of() {
            D3.ByteLongPoint point = D3.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);

            assertEquals(1, point.x());
            assertEquals(2, point.y());
            assertEquals(3, point.z());
            assertEquals(1000L, point.value());
        }

        @Test
        public void testXyzByteLongPoint_HashCode() {
            D3.ByteLongPoint point1 = D3.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);
            D3.ByteLongPoint point2 = D3.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzByteLongPoint_Equals() {
            D3.ByteLongPoint point1 = D3.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);
            D3.ByteLongPoint point2 = D3.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);
            D3.ByteLongPoint point3 = D3.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1001L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzByteLongPoint_ToString() {
            D3.ByteLongPoint point = D3.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);
            assertEquals("ByteLongPoint[x=1, y=2, z=3, value=1000]", point.toString());
        }

        // ============================================
        // Tests for D3.ByteDoublePoint (3D)
        // ============================================

        @Test
        public void testXyzByteDoublePoint_Of() {
            D3.ByteDoublePoint point = D3.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);

            assertEquals(1, point.x());
            assertEquals(2, point.y());
            assertEquals(3, point.z());
            assertEquals(10.5, point.value(), 0.001);
        }

        @Test
        public void testXyzByteDoublePoint_HashCode() {
            D3.ByteDoublePoint point1 = D3.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);
            D3.ByteDoublePoint point2 = D3.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzByteDoublePoint_Equals() {
            D3.ByteDoublePoint point1 = D3.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);
            D3.ByteDoublePoint point2 = D3.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);
            D3.ByteDoublePoint point3 = D3.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.6);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzByteDoublePoint_ToString() {
            D3.ByteDoublePoint point = D3.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 10.5);
            assertTrue(point.toString().contains("10.5"));
        }

        // ============================================
        // Tests for D3.ByteObjPoint (3D)
        // ============================================

        @Test
        public void testXyzByteObjPoint_Of() {
            D3.ByteObjPoint<String> point = D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "test");

            assertEquals(1, point.x());
            assertEquals(2, point.y());
            assertEquals(3, point.z());
            assertEquals("test", point.value());
        }

        @Test
        public void testXyzByteObjPoint_HashCode() {
            D3.ByteObjPoint<String> point1 = D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "hello");
            D3.ByteObjPoint<String> point2 = D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "hello");

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzByteObjPoint_Equals() {
            D3.ByteObjPoint<String> point1 = D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "hello");
            D3.ByteObjPoint<String> point2 = D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "hello");
            D3.ByteObjPoint<String> point3 = D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "world");
            D3.ByteObjPoint<String> point4 = D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, null);
            D3.ByteObjPoint<String> point5 = D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, null);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
            assertFalse(point1.equals(point4));
            assertTrue(point4.equals(point5));
        }

        @Test
        public void testXyzByteObjPoint_ToString() {
            D3.ByteObjPoint<String> point = D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "test");
            assertTrue(point.toString().contains("test"));
        }

        // ============================================
        // Tests for D3.IntBytePoint (3D)
        // ============================================

        @Test
        public void testXyzIntBytePoint_Of() {
            D3.IntBytePoint point = D3.IntBytePoint.of(100, 200, 300, (byte) 50);

            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(300, point.z());
            assertEquals(50, point.value());
        }

        @Test
        public void testXyzIntBytePoint_HashCode() {
            D3.IntBytePoint point1 = D3.IntBytePoint.of(100, 200, 300, (byte) 50);
            D3.IntBytePoint point2 = D3.IntBytePoint.of(100, 200, 300, (byte) 50);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzIntBytePoint_Equals() {
            D3.IntBytePoint point1 = D3.IntBytePoint.of(100, 200, 300, (byte) 50);
            D3.IntBytePoint point2 = D3.IntBytePoint.of(100, 200, 300, (byte) 50);
            D3.IntBytePoint point3 = D3.IntBytePoint.of(100, 200, 300, (byte) 51);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzIntBytePoint_ToString() {
            D3.IntBytePoint point = D3.IntBytePoint.of(100, 200, 300, (byte) 50);
            assertEquals("IntBytePoint[x=100, y=200, z=300, value=50]", point.toString());
        }

        // ============================================
        // Tests for D3.IntIntPoint (3D)
        // ============================================

        @Test
        public void testXyzIntIntPoint_Of() {
            D3.IntIntPoint point = D3.IntIntPoint.of(10, 20, 30, 40);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(30, point.z());
            assertEquals(40, point.value());
        }

        @Test
        public void testXyzIntIntPoint_HashCode() {
            D3.IntIntPoint point1 = D3.IntIntPoint.of(10, 20, 30, 40);
            D3.IntIntPoint point2 = D3.IntIntPoint.of(10, 20, 30, 40);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzIntIntPoint_Equals() {
            D3.IntIntPoint point1 = D3.IntIntPoint.of(10, 20, 30, 40);
            D3.IntIntPoint point2 = D3.IntIntPoint.of(10, 20, 30, 40);
            D3.IntIntPoint point3 = D3.IntIntPoint.of(10, 20, 30, 41);
            D3.IntIntPoint point4 = D3.IntIntPoint.of(10, 20, 31, 40);
            D3.IntIntPoint point5 = D3.IntIntPoint.of(11, 20, 30, 40);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
            assertFalse(point1.equals(point4));
            assertFalse(point1.equals(point5));
        }

        @Test
        public void testXyzIntIntPoint_ToString() {
            D3.IntIntPoint point = D3.IntIntPoint.of(10, 20, 30, 40);
            assertEquals("IntIntPoint[x=10, y=20, z=30, value=40]", point.toString());
        }

        // ============================================
        // Tests for D3.IntLongPoint (3D)
        // ============================================

        @Test
        public void testXyzIntLongPoint_Of() {
            D3.IntLongPoint point = D3.IntLongPoint.of(10, 20, 30, 5000L);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(30, point.z());
            assertEquals(5000L, point.value());
        }

        @Test
        public void testXyzIntLongPoint_HashCode() {
            D3.IntLongPoint point1 = D3.IntLongPoint.of(10, 20, 30, 5000L);
            D3.IntLongPoint point2 = D3.IntLongPoint.of(10, 20, 30, 5000L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzIntLongPoint_Equals() {
            D3.IntLongPoint point1 = D3.IntLongPoint.of(10, 20, 30, 5000L);
            D3.IntLongPoint point2 = D3.IntLongPoint.of(10, 20, 30, 5000L);
            D3.IntLongPoint point3 = D3.IntLongPoint.of(10, 20, 30, 5001L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzIntLongPoint_ToString() {
            D3.IntLongPoint point = D3.IntLongPoint.of(10, 20, 30, 5000L);
            assertEquals("IntLongPoint[x=10, y=20, z=30, value=5000]", point.toString());
        }

        // ============================================
        // Tests for D3.IntDoublePoint (3D)
        // ============================================

        @Test
        public void testXyzIntDoublePoint_Of() {
            D3.IntDoublePoint point = D3.IntDoublePoint.of(10, 20, 30, 40.5);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(30, point.z());
            assertEquals(40.5, point.value(), 0.001);
        }

        @Test
        public void testXyzIntDoublePoint_HashCode() {
            D3.IntDoublePoint point1 = D3.IntDoublePoint.of(10, 20, 30, 40.5);
            D3.IntDoublePoint point2 = D3.IntDoublePoint.of(10, 20, 30, 40.5);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzIntDoublePoint_Equals() {
            D3.IntDoublePoint point1 = D3.IntDoublePoint.of(10, 20, 30, 40.5);
            D3.IntDoublePoint point2 = D3.IntDoublePoint.of(10, 20, 30, 40.5);
            D3.IntDoublePoint point3 = D3.IntDoublePoint.of(10, 20, 30, 40.6);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzIntDoublePoint_ToString() {
            D3.IntDoublePoint point = D3.IntDoublePoint.of(10, 20, 30, 40.5);
            assertTrue(point.toString().contains("40.5"));
        }

        // ============================================
        // Tests for D3.IntObjPoint (3D)
        // ============================================

        @Test
        public void testXyzIntObjPoint_Of() {
            D3.IntObjPoint<String> point = D3.IntObjPoint.of(10, 20, 30, "test");

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(30, point.z());
            assertEquals("test", point.value());
        }

        @Test
        public void testXyzIntObjPoint_HashCode() {
            D3.IntObjPoint<String> point1 = D3.IntObjPoint.of(10, 20, 30, "hello");
            D3.IntObjPoint<String> point2 = D3.IntObjPoint.of(10, 20, 30, "hello");

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzIntObjPoint_Equals() {
            D3.IntObjPoint<String> point1 = D3.IntObjPoint.of(10, 20, 30, "hello");
            D3.IntObjPoint<String> point2 = D3.IntObjPoint.of(10, 20, 30, "hello");
            D3.IntObjPoint<String> point3 = D3.IntObjPoint.of(10, 20, 30, "world");
            D3.IntObjPoint<String> point4 = D3.IntObjPoint.of(10, 20, 30, null);
            D3.IntObjPoint<String> point5 = D3.IntObjPoint.of(10, 20, 30, null);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
            assertFalse(point1.equals(point4));
            assertTrue(point4.equals(point5));
        }

        @Test
        public void testXyzIntObjPoint_ToString() {
            D3.IntObjPoint<String> point = D3.IntObjPoint.of(10, 20, 30, "test");
            assertTrue(point.toString().contains("test"));
        }

        // ============================================
        // Tests for D3.LongBytePoint (3D)
        // ============================================

        @Test
        public void testXyzLongBytePoint_Of() {
            D3.LongBytePoint point = D3.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);

            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(3000L, point.z());
            assertEquals(100, point.value());
        }

        @Test
        public void testXyzLongBytePoint_HashCode() {
            D3.LongBytePoint point1 = D3.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);
            D3.LongBytePoint point2 = D3.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzLongBytePoint_Equals() {
            D3.LongBytePoint point1 = D3.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);
            D3.LongBytePoint point2 = D3.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);
            D3.LongBytePoint point3 = D3.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 101);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzLongBytePoint_ToString() {
            D3.LongBytePoint point = D3.LongBytePoint.of(1000L, 2000L, 3000L, (byte) 100);
            assertEquals("LongBytePoint[x=1000, y=2000, z=3000, value=100]", point.toString());
        }

        // ============================================
        // Tests for D3.LongIntPoint (3D)
        // ============================================

        @Test
        public void testXyzLongIntPoint_Of() {
            D3.LongIntPoint point = D3.LongIntPoint.of(500L, 1000L, 1500L, 250);

            assertEquals(500L, point.x());
            assertEquals(1000L, point.y());
            assertEquals(1500L, point.z());
            assertEquals(250, point.value());
        }

        @Test
        public void testXyzLongIntPoint_HashCode() {
            D3.LongIntPoint point1 = D3.LongIntPoint.of(500L, 1000L, 1500L, 250);
            D3.LongIntPoint point2 = D3.LongIntPoint.of(500L, 1000L, 1500L, 250);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzLongIntPoint_Equals() {
            D3.LongIntPoint point1 = D3.LongIntPoint.of(500L, 1000L, 1500L, 250);
            D3.LongIntPoint point2 = D3.LongIntPoint.of(500L, 1000L, 1500L, 250);
            D3.LongIntPoint point3 = D3.LongIntPoint.of(500L, 1000L, 1500L, 251);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzLongIntPoint_ToString() {
            D3.LongIntPoint point = D3.LongIntPoint.of(500L, 1000L, 1500L, 250);
            assertEquals("LongIntPoint[x=500, y=1000, z=1500, value=250]", point.toString());
        }

        // ============================================
        // Tests for D3.LongLongPoint (3D)
        // ============================================

        @Test
        public void testXyzLongLongPoint_Of() {
            D3.LongLongPoint point = D3.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);

            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(3000L, point.z());
            assertEquals(4000L, point.value());
        }

        @Test
        public void testXyzLongLongPoint_HashCode() {
            D3.LongLongPoint point1 = D3.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);
            D3.LongLongPoint point2 = D3.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzLongLongPoint_Equals() {
            D3.LongLongPoint point1 = D3.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);
            D3.LongLongPoint point2 = D3.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);
            D3.LongLongPoint point3 = D3.LongLongPoint.of(1000L, 2000L, 3000L, 4001L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzLongLongPoint_ToString() {
            D3.LongLongPoint point = D3.LongLongPoint.of(1000L, 2000L, 3000L, 4000L);
            assertEquals("LongLongPoint[x=1000, y=2000, z=3000, value=4000]", point.toString());
        }

        // ============================================
        // Tests for D3.LongDoublePoint (3D)
        // ============================================

        @Test
        public void testXyzLongDoublePoint_Of() {
            D3.LongDoublePoint point = D3.LongDoublePoint.of(100L, 200L, 300L, 400.75);

            assertEquals(100L, point.x());
            assertEquals(200L, point.y());
            assertEquals(300L, point.z());
            assertEquals(400.75, point.value(), 0.001);
        }

        @Test
        public void testXyzLongDoublePoint_HashCode() {
            D3.LongDoublePoint point1 = D3.LongDoublePoint.of(100L, 200L, 300L, 400.75);
            D3.LongDoublePoint point2 = D3.LongDoublePoint.of(100L, 200L, 300L, 400.75);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzLongDoublePoint_Equals() {
            D3.LongDoublePoint point1 = D3.LongDoublePoint.of(100L, 200L, 300L, 400.75);
            D3.LongDoublePoint point2 = D3.LongDoublePoint.of(100L, 200L, 300L, 400.75);
            D3.LongDoublePoint point3 = D3.LongDoublePoint.of(100L, 200L, 300L, 400.76);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzLongDoublePoint_ToString() {
            D3.LongDoublePoint point = D3.LongDoublePoint.of(100L, 200L, 300L, 400.75);
            assertTrue(point.toString().contains("400.75"));
        }

        // ============================================
        // Tests for D3.LongObjPoint (3D)
        // ============================================

        @Test
        public void testXyzLongObjPoint_Of() {
            D3.LongObjPoint<Integer> point = D3.LongObjPoint.of(10L, 20L, 30L, 42);

            assertEquals(10L, point.x());
            assertEquals(20L, point.y());
            assertEquals(30L, point.z());
            assertEquals(Integer.valueOf(42), point.value());
        }

        @Test
        public void testXyzLongObjPoint_HashCode() {
            D3.LongObjPoint<Integer> point1 = D3.LongObjPoint.of(10L, 20L, 30L, 100);
            D3.LongObjPoint<Integer> point2 = D3.LongObjPoint.of(10L, 20L, 30L, 100);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzLongObjPoint_Equals() {
            D3.LongObjPoint<Integer> point1 = D3.LongObjPoint.of(10L, 20L, 30L, 100);
            D3.LongObjPoint<Integer> point2 = D3.LongObjPoint.of(10L, 20L, 30L, 100);
            D3.LongObjPoint<Integer> point3 = D3.LongObjPoint.of(10L, 20L, 30L, 101);
            D3.LongObjPoint<Integer> point4 = D3.LongObjPoint.of(10L, 20L, 30L, null);
            D3.LongObjPoint<Integer> point5 = D3.LongObjPoint.of(10L, 20L, 30L, null);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
            assertFalse(point1.equals(point4));
            assertTrue(point4.equals(point5));
        }

        @Test
        public void testXyzLongObjPoint_ToString() {
            D3.LongObjPoint<Integer> point = D3.LongObjPoint.of(10L, 20L, 30L, 42);
            assertTrue(point.toString().contains("42"));
        }

        // ============================================
        // Tests for D3.DoubleBytePoint (3D)
        // ============================================

        @Test
        public void testXyzDoubleBytePoint_Of() {
            D3.DoubleBytePoint point = D3.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);

            assertEquals(10.5, point.x(), 0.001);
            assertEquals(20.7, point.y(), 0.001);
            assertEquals(30.9, point.z(), 0.001);
            assertEquals(40, point.value());
        }

        @Test
        public void testXyzDoubleBytePoint_HashCode() {
            D3.DoubleBytePoint point1 = D3.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);
            D3.DoubleBytePoint point2 = D3.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzDoubleBytePoint_Equals() {
            D3.DoubleBytePoint point1 = D3.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);
            D3.DoubleBytePoint point2 = D3.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);
            D3.DoubleBytePoint point3 = D3.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 41);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzDoubleBytePoint_ToString() {
            D3.DoubleBytePoint point = D3.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 40);
            assertTrue(point.toString().contains("10.5"));
            assertTrue(point.toString().contains("20.7"));
            assertTrue(point.toString().contains("30.9"));
        }

        // ============================================
        // Tests for D3.DoubleIntPoint (3D)
        // ============================================

        @Test
        public void testXyzDoubleIntPoint_Of() {
            D3.DoubleIntPoint point = D3.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);

            assertEquals(1.1, point.x(), 0.001);
            assertEquals(2.2, point.y(), 0.001);
            assertEquals(3.3, point.z(), 0.001);
            assertEquals(400, point.value());
        }

        @Test
        public void testXyzDoubleIntPoint_HashCode() {
            D3.DoubleIntPoint point1 = D3.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);
            D3.DoubleIntPoint point2 = D3.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzDoubleIntPoint_Equals() {
            D3.DoubleIntPoint point1 = D3.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);
            D3.DoubleIntPoint point2 = D3.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);
            D3.DoubleIntPoint point3 = D3.DoubleIntPoint.of(1.1, 2.2, 3.3, 401);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzDoubleIntPoint_ToString() {
            D3.DoubleIntPoint point = D3.DoubleIntPoint.of(1.1, 2.2, 3.3, 400);
            assertTrue(point.toString().contains("1.1"));
            assertTrue(point.toString().contains("2.2"));
            assertTrue(point.toString().contains("3.3"));
        }

        // ============================================
        // Tests for D3.DoubleLongPoint (3D)
        // ============================================

        @Test
        public void testXyzDoubleLongPoint_Of() {
            D3.DoubleLongPoint point = D3.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);

            assertEquals(5.5, point.x(), 0.001);
            assertEquals(10.75, point.y(), 0.001);
            assertEquals(15.25, point.z(), 0.001);
            assertEquals(1000000L, point.value());
        }

        @Test
        public void testXyzDoubleLongPoint_HashCode() {
            D3.DoubleLongPoint point1 = D3.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);
            D3.DoubleLongPoint point2 = D3.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzDoubleLongPoint_Equals() {
            D3.DoubleLongPoint point1 = D3.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);
            D3.DoubleLongPoint point2 = D3.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);
            D3.DoubleLongPoint point3 = D3.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000001L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzDoubleLongPoint_ToString() {
            D3.DoubleLongPoint point = D3.DoubleLongPoint.of(5.5, 10.75, 15.25, 1000000L);
            assertTrue(point.toString().contains("5.5"));
            assertTrue(point.toString().contains("10.75"));
            assertTrue(point.toString().contains("15.25"));
        }

        // ============================================
        // Tests for D3.DoubleDoublePoint (3D)
        // ============================================

        @Test
        public void testXyzDoubleDoublePoint_Of() {
            D3.DoubleDoublePoint point = D3.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.1);

            assertEquals(10.5, point.x(), 0.001);
            assertEquals(20.7, point.y(), 0.001);
            assertEquals(30.9, point.z(), 0.001);
            assertEquals(40.1, point.value(), 0.001);
        }

        @Test
        public void testXyzDoubleDoublePoint_HashCode() {
            D3.DoubleDoublePoint point1 = D3.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.1);
            D3.DoubleDoublePoint point2 = D3.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.1);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzDoubleDoublePoint_Equals() {
            D3.DoubleDoublePoint point1 = D3.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.1);
            D3.DoubleDoublePoint point2 = D3.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.1);
            D3.DoubleDoublePoint point3 = D3.DoubleDoublePoint.of(10.5, 20.7, 30.9, 40.2);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void testXyzDoubleDoublePoint_ToString() {
            D3.DoubleDoublePoint point = D3.DoubleDoublePoint.of(1.1, 2.2, 3.3, 4.4);
            assertTrue(point.toString().contains("1.1"));
            assertTrue(point.toString().contains("2.2"));
            assertTrue(point.toString().contains("3.3"));
            assertTrue(point.toString().contains("4.4"));
        }

        // ============================================
        // Tests for D3.DoubleObjPoint (3D)
        // ============================================

        @Test
        public void testXyzDoubleObjPoint_Of() {
            D3.DoubleObjPoint<String> point = D3.DoubleObjPoint.of(1.5, 2.5, 3.5, "test");

            assertEquals(1.5, point.x(), 0.001);
            assertEquals(2.5, point.y(), 0.001);
            assertEquals(3.5, point.z(), 0.001);
            assertEquals("test", point.value());
        }

        @Test
        public void testXyzDoubleObjPoint_HashCode() {
            D3.DoubleObjPoint<String> point1 = D3.DoubleObjPoint.of(1.5, 2.5, 3.5, "hello");
            D3.DoubleObjPoint<String> point2 = D3.DoubleObjPoint.of(1.5, 2.5, 3.5, "hello");

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testXyzDoubleObjPoint_Equals() {
            D3.DoubleObjPoint<String> point1 = D3.DoubleObjPoint.of(1.5, 2.5, 3.5, "hello");
            D3.DoubleObjPoint<String> point2 = D3.DoubleObjPoint.of(1.5, 2.5, 3.5, "hello");
            D3.DoubleObjPoint<String> point3 = D3.DoubleObjPoint.of(1.5, 2.5, 3.5, "world");
            D3.DoubleObjPoint<String> point4 = D3.DoubleObjPoint.of(1.5, 2.5, 3.5, null);
            D3.DoubleObjPoint<String> point5 = D3.DoubleObjPoint.of(1.5, 2.5, 3.5, null);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
            assertFalse(point1.equals(point4));
            assertTrue(point4.equals(point5));
        }

        @Test
        public void testXyzDoubleObjPoint_ToString() {
            D3.DoubleObjPoint<String> point = D3.DoubleObjPoint.of(1.5, 2.5, 3.5, "test");
            assertTrue(point.toString().contains("test"));
        }

        // ============================================
        // D3 Edge cases and comprehensive tests
        // ============================================

        @Test
        public void testXyz_MixedTypePoints() {
            D3.ByteLongPoint byteLong = D3.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 1000L);
            D3.IntDoublePoint intDouble = D3.IntDoublePoint.of(10, 20, 30, 40.5);
            D3.LongIntPoint longInt = D3.LongIntPoint.of(100L, 200L, 300L, 400);

            assertEquals(1, byteLong.x());
            assertEquals(2, byteLong.y());
            assertEquals(3, byteLong.z());
            assertEquals(1000L, byteLong.value());

            assertEquals(10, intDouble.x());
            assertEquals(20, intDouble.y());
            assertEquals(30, intDouble.z());
            assertEquals(40.5, intDouble.value(), 0.001);

            assertEquals(100L, longInt.x());
            assertEquals(200L, longInt.y());
            assertEquals(300L, longInt.z());
            assertEquals(400, longInt.value());
        }

        @Test
        public void testXyz_EdgeCases() {
            D3.ByteBytePoint minBytePoint = D3.ByteBytePoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0, (byte) 0);
            assertEquals(Byte.MIN_VALUE, minBytePoint.x());
            assertEquals(Byte.MAX_VALUE, minBytePoint.y());
            assertEquals(0, minBytePoint.z());
            assertEquals(0, minBytePoint.value());

            D3.IntIntPoint maxIntPoint = D3.IntIntPoint.of(Integer.MAX_VALUE, Integer.MIN_VALUE, 0, 0);
            assertEquals(Integer.MAX_VALUE, maxIntPoint.x());
            assertEquals(Integer.MIN_VALUE, maxIntPoint.y());
            assertEquals(0, maxIntPoint.z());
            assertEquals(0, maxIntPoint.value());

            D3.LongLongPoint maxLongPoint = D3.LongLongPoint.of(Long.MAX_VALUE, Long.MIN_VALUE, 0L, 0L);
            assertEquals(Long.MAX_VALUE, maxLongPoint.x());
            assertEquals(Long.MIN_VALUE, maxLongPoint.y());
            assertEquals(0L, maxLongPoint.z());
            assertEquals(0L, maxLongPoint.value());

            D3.DoubleDoublePoint specialDoublePoint = D3.DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0, Double.NaN);
            assertEquals(Double.POSITIVE_INFINITY, specialDoublePoint.x(), 0.0);
            assertEquals(Double.NEGATIVE_INFINITY, specialDoublePoint.y(), 0.0);
            assertEquals(0.0, specialDoublePoint.z(), 0.0);
            assertTrue(Double.isNaN(specialDoublePoint.value()));
        }

        @Test
        public void testXyz_AllPointTypesSelfEquals() {
            assertTrue(D3.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4).equals(D3.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4)));
            assertTrue(D3.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 4).equals(D3.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, 4)));
            assertTrue(D3.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 4L).equals(D3.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, 4L)));
            assertTrue(D3.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 4.0).equals(D3.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, 4.0)));
            assertTrue(D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "4").equals(D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "4")));

            assertTrue(D3.IntBytePoint.of(1, 2, 3, (byte) 4).equals(D3.IntBytePoint.of(1, 2, 3, (byte) 4)));
            assertTrue(D3.IntIntPoint.of(1, 2, 3, 4).equals(D3.IntIntPoint.of(1, 2, 3, 4)));
            assertTrue(D3.IntLongPoint.of(1, 2, 3, 4L).equals(D3.IntLongPoint.of(1, 2, 3, 4L)));
            assertTrue(D3.IntDoublePoint.of(1, 2, 3, 4.0).equals(D3.IntDoublePoint.of(1, 2, 3, 4.0)));
            assertTrue(D3.IntObjPoint.of(1, 2, 3, "4").equals(D3.IntObjPoint.of(1, 2, 3, "4")));

            assertTrue(D3.LongBytePoint.of(1L, 2L, 3L, (byte) 4).equals(D3.LongBytePoint.of(1L, 2L, 3L, (byte) 4)));
            assertTrue(D3.LongIntPoint.of(1L, 2L, 3L, 4).equals(D3.LongIntPoint.of(1L, 2L, 3L, 4)));
            assertTrue(D3.LongLongPoint.of(1L, 2L, 3L, 4L).equals(D3.LongLongPoint.of(1L, 2L, 3L, 4L)));
            assertTrue(D3.LongDoublePoint.of(1L, 2L, 3L, 4.0).equals(D3.LongDoublePoint.of(1L, 2L, 3L, 4.0)));
            assertTrue(D3.LongObjPoint.of(1L, 2L, 3L, "4").equals(D3.LongObjPoint.of(1L, 2L, 3L, "4")));

            assertTrue(D3.DoubleBytePoint.of(1.0, 2.0, 3.0, (byte) 4).equals(D3.DoubleBytePoint.of(1.0, 2.0, 3.0, (byte) 4)));
            assertTrue(D3.DoubleIntPoint.of(1.0, 2.0, 3.0, 4).equals(D3.DoubleIntPoint.of(1.0, 2.0, 3.0, 4)));
            assertTrue(D3.DoubleLongPoint.of(1.0, 2.0, 3.0, 4L).equals(D3.DoubleLongPoint.of(1.0, 2.0, 3.0, 4L)));
            assertTrue(D3.DoubleDoublePoint.of(1.0, 2.0, 3.0, 4.0).equals(D3.DoubleDoublePoint.of(1.0, 2.0, 3.0, 4.0)));
            assertTrue(D3.DoubleObjPoint.of(1.0, 2.0, 3.0, "4").equals(D3.DoubleObjPoint.of(1.0, 2.0, 3.0, "4")));
        }

        @Test
        public void testXyz_AllPointTypesNotEqualToNull() {
            assertFalse(D3.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3, (byte) 4).equals(null));
            assertFalse(D3.IntIntPoint.of(1, 2, 3, 4).equals(null));
            assertFalse(D3.LongLongPoint.of(1L, 2L, 3L, 4L).equals(null));
            assertFalse(D3.DoubleDoublePoint.of(1.0, 2.0, 3.0, 4.0).equals(null));
            assertFalse(D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "test").equals(null));
            assertFalse(D3.IntObjPoint.of(1, 2, 3, "test").equals(null));
            assertFalse(D3.LongObjPoint.of(1L, 2L, 3L, "test").equals(null));
            assertFalse(D3.DoubleObjPoint.of(1.0, 2.0, 3.0, "test").equals(null));
        }

        @Test
        public void testXyz_DifferentPointTypesNotEqual() {
            D3.IntIntPoint intPoint = D3.IntIntPoint.of(1, 2, 3, 4);
            D3.LongLongPoint longPoint = D3.LongLongPoint.of(1L, 2L, 3L, 4L);

            assertFalse(intPoint.equals(longPoint));
            assertFalse(longPoint.equals(intPoint));
        }

        @Test
        public void testXyz_Immutability() {
            D3.IntIntPoint point = D3.IntIntPoint.of(10, 20, 30, 40);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(30, point.z());
            assertEquals(40, point.value());

            // Values should remain the same after multiple accesses
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(30, point.z());
            assertEquals(40, point.value());
        }

        @Test
        public void testXyz_ClassAccessibility() {
            assertFalse(D3.class == null);

            D3.IntIntPoint point = D3.IntIntPoint.of(1, 2, 3, 4);
            assertEquals(1, point.x());
            assertEquals(2, point.y());
            assertEquals(3, point.z());
            assertEquals(4, point.value());
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for the Points utility class and all its nested point classes.
     * Tests cover all 20 point types with different coordinate and value combinations,
     * verifying factory methods, field access, hashCode, equals, and toString methods.
     * This test class focuses on edge cases, boundary values, and thorough code path coverage.
     */
    @Tag("2510")
    class Points2510Test extends TestBase {

        // ============================================
        // Tests for ByteBytePoint - Edge Cases
        // ============================================

        @Test
        public void testByteBytePoint_MinMaxValues() {
            D2.ByteBytePoint point = D2.ByteBytePoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0);

            assertEquals(Byte.MIN_VALUE, point.x());
            assertEquals(Byte.MAX_VALUE, point.y());
            assertEquals(0, point.value());
        }

        @Test
        public void testByteBytePoint_NegativeValues() {
            D2.ByteBytePoint point = D2.ByteBytePoint.of((byte) -10, (byte) -20, (byte) -30);

            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(-30, point.value());
        }

        @Test
        public void testByteBytePoint_ZeroValues() {
            D2.ByteBytePoint point = D2.ByteBytePoint.of((byte) 0, (byte) 0, (byte) 0);

            assertEquals(0, point.x());
            assertEquals(0, point.y());
            assertEquals(0, point.value());
        }

        @Test
        public void testByteBytePoint_Equals_DifferentX() {
            D2.ByteBytePoint point1 = D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
            D2.ByteBytePoint point2 = D2.ByteBytePoint.of((byte) 10, (byte) 2, (byte) 3);

            assertFalse(point1.equals(point2));
        }

        @Test
        public void testByteBytePoint_Equals_DifferentY() {
            D2.ByteBytePoint point1 = D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
            D2.ByteBytePoint point2 = D2.ByteBytePoint.of((byte) 1, (byte) 20, (byte) 3);

            assertFalse(point1.equals(point2));
        }

        @Test
        public void testByteBytePoint_HashCode_Consistency() {
            D2.ByteBytePoint point = D2.ByteBytePoint.of((byte) 5, (byte) 10, (byte) 15);
            int hash1 = point.hashCode();
            int hash2 = point.hashCode();

            assertEquals(hash1, hash2);
        }

        @Test
        public void testByteBytePoint_ToString_Format() {
            D2.ByteBytePoint point = D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
            String result = point.toString();

            assertEquals("ByteBytePoint[x=1, y=2, value=3]", result);
        }

        // ============================================
        // Tests for ByteIntPoint - Edge Cases
        // ============================================

        @Test
        public void testByteIntPoint_MaxIntValue() {
            D2.ByteIntPoint point = D2.ByteIntPoint.of((byte) 10, (byte) 20, Integer.MAX_VALUE);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(Integer.MAX_VALUE, point.value());
        }

        @Test
        public void testByteIntPoint_MinIntValue() {
            D2.ByteIntPoint point = D2.ByteIntPoint.of((byte) -10, (byte) -20, Integer.MIN_VALUE);

            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(Integer.MIN_VALUE, point.value());
        }

        @Test
        public void testByteIntPoint_Equals_Null() {
            D2.ByteIntPoint point = D2.ByteIntPoint.of((byte) 1, (byte) 2, 3);

            assertFalse(point.equals(null));
        }

        @Test
        public void testByteIntPoint_Equals_DifferentClass() {
            D2.ByteIntPoint point = D2.ByteIntPoint.of((byte) 1, (byte) 2, 3);
            D2.ByteBytePoint other = D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);

            assertFalse(point.equals(other));
        }

        @Test
        public void testByteIntPoint_HashCode_DifferentValues() {
            D2.ByteIntPoint point1 = D2.ByteIntPoint.of((byte) 1, (byte) 2, 100);
            D2.ByteIntPoint point2 = D2.ByteIntPoint.of((byte) 1, (byte) 2, 200);

            assertNotEquals(point1.hashCode(), point2.hashCode());
        }

        // ============================================
        // Tests for ByteLongPoint - Edge Cases
        // ============================================

        @Test
        public void testByteLongPoint_MaxLongValue() {
            D2.ByteLongPoint point = D2.ByteLongPoint.of((byte) 10, (byte) 20, Long.MAX_VALUE);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(Long.MAX_VALUE, point.value());
        }

        @Test
        public void testByteLongPoint_MinLongValue() {
            D2.ByteLongPoint point = D2.ByteLongPoint.of((byte) -10, (byte) -20, Long.MIN_VALUE);

            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(Long.MIN_VALUE, point.value());
        }

        @Test
        public void testByteLongPoint_ZeroValue() {
            D2.ByteLongPoint point = D2.ByteLongPoint.of((byte) 5, (byte) 10, 0L);

            assertEquals(5, point.x());
            assertEquals(10, point.y());
            assertEquals(0L, point.value());
        }

        @Test
        public void testByteLongPoint_ToString_NegativeValues() {
            D2.ByteLongPoint point = D2.ByteLongPoint.of((byte) -1, (byte) -2, -3L);
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
            D2.ByteDoublePoint point = D2.ByteDoublePoint.of((byte) 10, (byte) 20, Double.MAX_VALUE);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(Double.MAX_VALUE, point.value(), 0.0);
        }

        @Test
        public void testByteDoublePoint_MinDoubleValue() {
            D2.ByteDoublePoint point = D2.ByteDoublePoint.of((byte) 10, (byte) 20, Double.MIN_VALUE);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(Double.MIN_VALUE, point.value(), 0.0);
        }

        @Test
        public void testByteDoublePoint_NaN() {
            D2.ByteDoublePoint point1 = D2.ByteDoublePoint.of((byte) 1, (byte) 2, Double.NaN);
            D2.ByteDoublePoint point2 = D2.ByteDoublePoint.of((byte) 1, (byte) 2, Double.NaN);

            assertTrue(point1.equals(point2));
        }

        @Test
        public void testByteDoublePoint_PositiveInfinity() {
            D2.ByteDoublePoint point = D2.ByteDoublePoint.of((byte) 1, (byte) 2, Double.POSITIVE_INFINITY);

            assertEquals(1, point.x());
            assertEquals(2, point.y());
            assertEquals(Double.POSITIVE_INFINITY, point.value(), 0.0);
        }

        @Test
        public void testByteDoublePoint_NegativeInfinity() {
            D2.ByteDoublePoint point = D2.ByteDoublePoint.of((byte) 1, (byte) 2, Double.NEGATIVE_INFINITY);

            assertEquals(1, point.x());
            assertEquals(2, point.y());
            assertEquals(Double.NEGATIVE_INFINITY, point.value(), 0.0);
        }

        @Test
        public void testByteDoublePoint_NegativeZero() {
            D2.ByteDoublePoint point1 = D2.ByteDoublePoint.of((byte) 1, (byte) 2, 0.0);
            D2.ByteDoublePoint point2 = D2.ByteDoublePoint.of((byte) 1, (byte) 2, -0.0);

            assertFalse(point1.equals(point2));
        }

        // ============================================
        // Tests for ByteObjPoint - Edge Cases
        // ============================================

        @Test
        public void testByteObjPoint_NullValue() {
            D2.ByteObjPoint<String> point = D2.ByteObjPoint.of((byte) 10, (byte) 20, null);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(null, point.value());
        }

        @Test
        public void testByteObjPoint_Equals_WithNull() {
            D2.ByteObjPoint<String> point1 = D2.ByteObjPoint.of((byte) 1, (byte) 2, null);
            D2.ByteObjPoint<String> point2 = D2.ByteObjPoint.of((byte) 1, (byte) 2, null);

            assertTrue(point1.equals(point2));
        }

        @Test
        public void testByteObjPoint_Equals_OneNull() {
            D2.ByteObjPoint<String> point1 = D2.ByteObjPoint.of((byte) 1, (byte) 2, "test");
            D2.ByteObjPoint<String> point2 = D2.ByteObjPoint.of((byte) 1, (byte) 2, null);

            assertFalse(point1.equals(point2));
        }

        @Test
        public void testByteObjPoint_HashCode_WithNull() {
            D2.ByteObjPoint<String> point1 = D2.ByteObjPoint.of((byte) 1, (byte) 2, null);
            D2.ByteObjPoint<String> point2 = D2.ByteObjPoint.of((byte) 1, (byte) 2, null);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testByteObjPoint_ComplexObject() {
            java.util.List<String> list = java.util.Arrays.asList("a", "b", "c");
            D2.ByteObjPoint<java.util.List<String>> point = D2.ByteObjPoint.of((byte) 5, (byte) 10, list);

            assertEquals(5, point.x());
            assertEquals(10, point.y());
            assertEquals(list, point.value());
        }

        @Test
        public void testByteObjPoint_ToString_WithNull() {
            D2.ByteObjPoint<String> point = D2.ByteObjPoint.of((byte) 1, (byte) 2, null);
            String result = point.toString();

            assertNotNull(result);
            assertTrue(result.contains("null"));
        }

        // ============================================
        // Tests for IntBytePoint - Edge Cases
        // ============================================

        @Test
        public void testIntBytePoint_MaxIntCoordinates() {
            D2.IntBytePoint point = D2.IntBytePoint.of(Integer.MAX_VALUE, Integer.MIN_VALUE, (byte) 50);

            assertEquals(Integer.MAX_VALUE, point.x());
            assertEquals(Integer.MIN_VALUE, point.y());
            assertEquals(50, point.value());
        }

        @Test
        public void testIntBytePoint_NegativeCoordinates() {
            D2.IntBytePoint point = D2.IntBytePoint.of(-100, -200, (byte) -30);

            assertEquals(-100, point.x());
            assertEquals(-200, point.y());
            assertEquals(-30, point.value());
        }

        @Test
        public void testIntBytePoint_ZeroValues() {
            D2.IntBytePoint point = D2.IntBytePoint.of(0, 0, (byte) 0);

            assertEquals(0, point.x());
            assertEquals(0, point.y());
            assertEquals(0, point.value());
        }

        @Test
        public void testIntBytePoint_Equals_SameInstance() {
            D2.IntBytePoint point = D2.IntBytePoint.of(10, 20, (byte) 30);

            assertTrue(point.equals(point));
        }

        // ============================================
        // Tests for IntIntPoint - Edge Cases
        // ============================================

        @Test
        public void testIntIntPoint_MaxIntValues() {
            D2.IntIntPoint point = D2.IntIntPoint.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

            assertEquals(Integer.MAX_VALUE, point.x());
            assertEquals(Integer.MAX_VALUE, point.y());
            assertEquals(Integer.MAX_VALUE, point.value());
        }

        @Test
        public void testIntIntPoint_MinIntValues() {
            D2.IntIntPoint point = D2.IntIntPoint.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

            assertEquals(Integer.MIN_VALUE, point.x());
            assertEquals(Integer.MIN_VALUE, point.y());
            assertEquals(Integer.MIN_VALUE, point.value());
        }

        @Test
        public void testIntIntPoint_MixedValues() {
            D2.IntIntPoint point = D2.IntIntPoint.of(-100, 200, 0);

            assertEquals(-100, point.x());
            assertEquals(200, point.y());
            assertEquals(0, point.value());
        }

        @Test
        public void testIntIntPoint_LargePositiveValues() {
            D2.IntIntPoint point = D2.IntIntPoint.of(1000000, 2000000, 3000000);

            assertEquals(1000000, point.x());
            assertEquals(2000000, point.y());
            assertEquals(3000000, point.value());
        }

        @Test
        public void testIntIntPoint_HashCode_Different() {
            D2.IntIntPoint point1 = D2.IntIntPoint.of(1, 2, 3);
            D2.IntIntPoint point2 = D2.IntIntPoint.of(3, 2, 1);

            assertNotEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testIntIntPoint_ToString_NegativeValues() {
            D2.IntIntPoint point = D2.IntIntPoint.of(-10, -20, -30);
            String result = point.toString();

            assertEquals("IntIntPoint[x=-10, y=-20, value=-30]", result);
        }

        // ============================================
        // Tests for IntLongPoint - Edge Cases
        // ============================================

        @Test
        public void testIntLongPoint_MaxLongValue() {
            D2.IntLongPoint point = D2.IntLongPoint.of(100, 200, Long.MAX_VALUE);

            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(Long.MAX_VALUE, point.value());
        }

        @Test
        public void testIntLongPoint_MinLongValue() {
            D2.IntLongPoint point = D2.IntLongPoint.of(-100, -200, Long.MIN_VALUE);

            assertEquals(-100, point.x());
            assertEquals(-200, point.y());
            assertEquals(Long.MIN_VALUE, point.value());
        }

        @Test
        public void testIntLongPoint_MixedSignValues() {
            D2.IntLongPoint point = D2.IntLongPoint.of(-100, 200, -3000L);

            assertEquals(-100, point.x());
            assertEquals(200, point.y());
            assertEquals(-3000L, point.value());
        }

        // ============================================
        // Tests for IntDoublePoint - Edge Cases
        // ============================================

        @Test
        public void testIntDoublePoint_SmallDecimals() {
            D2.IntDoublePoint point = D2.IntDoublePoint.of(10, 20, 0.0001);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(0.0001, point.value(), 0.00001);
        }

        @Test
        public void testIntDoublePoint_LargeDecimals() {
            D2.IntDoublePoint point = D2.IntDoublePoint.of(10, 20, 999999.999999);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(999999.999999, point.value(), 0.00001);
        }

        @Test
        public void testIntDoublePoint_NegativeDecimals() {
            D2.IntDoublePoint point = D2.IntDoublePoint.of(-10, -20, -3.14159);

            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(-3.14159, point.value(), 0.00001);
        }

        @Test
        public void testIntDoublePoint_PositiveInfinity() {
            D2.IntDoublePoint point = D2.IntDoublePoint.of(1, 2, Double.POSITIVE_INFINITY);

            assertEquals(Double.POSITIVE_INFINITY, point.value(), 0.0);
        }

        @Test
        public void testIntDoublePoint_NaN() {
            D2.IntDoublePoint point1 = D2.IntDoublePoint.of(1, 2, Double.NaN);
            D2.IntDoublePoint point2 = D2.IntDoublePoint.of(1, 2, Double.NaN);

            assertTrue(point1.equals(point2));
        }

        // ============================================
        // Tests for IntObjPoint - Edge Cases
        // ============================================

        @Test
        public void testIntObjPoint_NullValue() {
            D2.IntObjPoint<String> point = D2.IntObjPoint.of(10, 20, null);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(null, point.value());
        }

        @Test
        public void testIntObjPoint_IntegerObject() {
            D2.IntObjPoint<Integer> point = D2.IntObjPoint.of(10, 20, Integer.MAX_VALUE);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(Integer.MAX_VALUE, point.value());
        }

        @Test
        public void testIntObjPoint_StringObject() {
            D2.IntObjPoint<String> point = D2.IntObjPoint.of(-100, 200, "Test String");

            assertEquals(-100, point.x());
            assertEquals(200, point.y());
            assertEquals("Test String", point.value());
        }

        @Test
        public void testIntObjPoint_EmptyString() {
            D2.IntObjPoint<String> point = D2.IntObjPoint.of(0, 0, "");

            assertEquals(0, point.x());
            assertEquals(0, point.y());
            assertEquals("", point.value());
        }

        @Test
        public void testIntObjPoint_ArrayValue() {
            int[] array = { 1, 2, 3 };
            D2.IntObjPoint<int[]> point = D2.IntObjPoint.of(5, 10, array);

            assertEquals(5, point.x());
            assertEquals(10, point.y());
            assertEquals(array, point.value());
        }

        // ============================================
        // Tests for LongBytePoint - Edge Cases
        // ============================================

        @Test
        public void testLongBytePoint_MaxLongCoordinates() {
            D2.LongBytePoint point = D2.LongBytePoint.of(Long.MAX_VALUE, Long.MIN_VALUE, (byte) 50);

            assertEquals(Long.MAX_VALUE, point.x());
            assertEquals(Long.MIN_VALUE, point.y());
            assertEquals(50, point.value());
        }

        @Test
        public void testLongBytePoint_ZeroCoordinates() {
            D2.LongBytePoint point = D2.LongBytePoint.of(0L, 0L, (byte) 0);

            assertEquals(0L, point.x());
            assertEquals(0L, point.y());
            assertEquals(0, point.value());
        }

        @Test
        public void testLongBytePoint_NegativeValues() {
            D2.LongBytePoint point = D2.LongBytePoint.of(-1000L, -2000L, (byte) -50);

            assertEquals(-1000L, point.x());
            assertEquals(-2000L, point.y());
            assertEquals(-50, point.value());
        }

        // ============================================
        // Tests for LongIntPoint - Edge Cases
        // ============================================

        @Test
        public void testLongIntPoint_MaxValues() {
            D2.LongIntPoint point = D2.LongIntPoint.of(Long.MAX_VALUE, Long.MIN_VALUE, Integer.MAX_VALUE);

            assertEquals(Long.MAX_VALUE, point.x());
            assertEquals(Long.MIN_VALUE, point.y());
            assertEquals(Integer.MAX_VALUE, point.value());
        }

        @Test
        public void testLongIntPoint_MixedSigns() {
            D2.LongIntPoint point = D2.LongIntPoint.of(-100000L, 200000L, -300);

            assertEquals(-100000L, point.x());
            assertEquals(200000L, point.y());
            assertEquals(-300, point.value());
        }

        @Test
        public void testLongIntPoint_ZeroValues() {
            D2.LongIntPoint point = D2.LongIntPoint.of(0L, 0L, 0);

            assertEquals(0L, point.x());
            assertEquals(0L, point.y());
            assertEquals(0, point.value());
        }

        // ============================================
        // Tests for LongLongPoint - Edge Cases
        // ============================================

        @Test
        public void testLongLongPoint_MaxLongValues() {
            D2.LongLongPoint point = D2.LongLongPoint.of(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);

            assertEquals(Long.MAX_VALUE, point.x());
            assertEquals(Long.MAX_VALUE, point.y());
            assertEquals(Long.MAX_VALUE, point.value());
        }

        @Test
        public void testLongLongPoint_MinLongValues() {
            D2.LongLongPoint point = D2.LongLongPoint.of(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);

            assertEquals(Long.MIN_VALUE, point.x());
            assertEquals(Long.MIN_VALUE, point.y());
            assertEquals(Long.MIN_VALUE, point.value());
        }

        @Test
        public void testLongLongPoint_LargeValues() {
            D2.LongLongPoint point = D2.LongLongPoint.of(9223372036854775806L, -9223372036854775807L, 1000000000000L);

            assertEquals(9223372036854775806L, point.x());
            assertEquals(-9223372036854775807L, point.y());
            assertEquals(1000000000000L, point.value());
        }

        @Test
        public void testLongLongPoint_HashCode_Consistency() {
            D2.LongLongPoint point = D2.LongLongPoint.of(12345L, 67890L, 11111L);
            int hash1 = point.hashCode();
            int hash2 = point.hashCode();

            assertEquals(hash1, hash2);
        }

        // ============================================
        // Tests for LongDoublePoint - Edge Cases
        // ============================================

        @Test
        public void testLongDoublePoint_MaxValues() {
            D2.LongDoublePoint point = D2.LongDoublePoint.of(Long.MAX_VALUE, Long.MIN_VALUE, Double.MAX_VALUE);

            assertEquals(Long.MAX_VALUE, point.x());
            assertEquals(Long.MIN_VALUE, point.y());
            assertEquals(Double.MAX_VALUE, point.value(), 0.0);
        }

        @Test
        public void testLongDoublePoint_SmallDouble() {
            D2.LongDoublePoint point = D2.LongDoublePoint.of(100L, 200L, Double.MIN_VALUE);

            assertEquals(100L, point.x());
            assertEquals(200L, point.y());
            assertEquals(Double.MIN_VALUE, point.value(), 0.0);
        }

        @Test
        public void testLongDoublePoint_NaN() {
            D2.LongDoublePoint point1 = D2.LongDoublePoint.of(1L, 2L, Double.NaN);
            D2.LongDoublePoint point2 = D2.LongDoublePoint.of(1L, 2L, Double.NaN);

            assertTrue(point1.equals(point2));
        }

        @Test
        public void testLongDoublePoint_Infinity() {
            D2.LongDoublePoint point = D2.LongDoublePoint.of(100L, 200L, Double.NEGATIVE_INFINITY);

            assertEquals(Double.NEGATIVE_INFINITY, point.value(), 0.0);
        }

        // ============================================
        // Tests for LongObjPoint - Edge Cases
        // ============================================

        @Test
        public void testLongObjPoint_NullValue() {
            D2.LongObjPoint<String> point = D2.LongObjPoint.of(1000L, 2000L, null);

            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(null, point.value());
        }

        @Test
        public void testLongObjPoint_LongObject() {
            D2.LongObjPoint<Long> point = D2.LongObjPoint.of(100L, 200L, Long.MAX_VALUE);

            assertEquals(100L, point.x());
            assertEquals(200L, point.y());
            assertEquals(Long.MAX_VALUE, point.value());
        }

        @Test
        public void testLongObjPoint_ComplexGeneric() {
            java.util.Map<String, Integer> map = new java.util.HashMap<>();
            map.put("key", 42);
            D2.LongObjPoint<java.util.Map<String, Integer>> point = D2.LongObjPoint.of(10L, 20L, map);

            assertEquals(10L, point.x());
            assertEquals(20L, point.y());
            assertEquals(map, point.value());
        }

        @Test
        public void testLongObjPoint_Equals_NullValues() {
            D2.LongObjPoint<String> point1 = D2.LongObjPoint.of(1L, 2L, null);
            D2.LongObjPoint<String> point2 = D2.LongObjPoint.of(1L, 2L, null);

            assertTrue(point1.equals(point2));
        }

        // ============================================
        // Tests for DoubleBytePoint - Edge Cases
        // ============================================

        @Test
        public void testDoubleBytePoint_MaxDoubleCoordinates() {
            D2.DoubleBytePoint point = D2.DoubleBytePoint.of(Double.MAX_VALUE, Double.MIN_VALUE, (byte) 50);

            assertEquals(Double.MAX_VALUE, point.x(), 0.0);
            assertEquals(Double.MIN_VALUE, point.y(), 0.0);
            assertEquals(50, point.value());
        }

        @Test
        public void testDoubleBytePoint_NegativeCoordinates() {
            D2.DoubleBytePoint point = D2.DoubleBytePoint.of(-10.5, -20.5, (byte) -30);

            assertEquals(-10.5, point.x(), 0.001);
            assertEquals(-20.5, point.y(), 0.001);
            assertEquals(-30, point.value());
        }

        @Test
        public void testDoubleBytePoint_ZeroCoordinates() {
            D2.DoubleBytePoint point = D2.DoubleBytePoint.of(0.0, 0.0, (byte) 0);

            assertEquals(0.0, point.x(), 0.0);
            assertEquals(0.0, point.y(), 0.0);
            assertEquals(0, point.value());
        }

        @Test
        public void testDoubleBytePoint_NaNCoordinates() {
            D2.DoubleBytePoint point1 = D2.DoubleBytePoint.of(Double.NaN, 2.5, (byte) 3);
            D2.DoubleBytePoint point2 = D2.DoubleBytePoint.of(Double.NaN, 2.5, (byte) 3);

            assertTrue(point1.equals(point2));
        }

        @Test
        public void testDoubleBytePoint_InfinityCoordinates() {
            D2.DoubleBytePoint point = D2.DoubleBytePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, (byte) 0);

            assertEquals(Double.POSITIVE_INFINITY, point.x(), 0.0);
            assertEquals(Double.NEGATIVE_INFINITY, point.y(), 0.0);
        }

        // ============================================
        // Tests for DoubleIntPoint - Edge Cases
        // ============================================

        @Test
        public void testDoubleIntPoint_SmallDoubles() {
            D2.DoubleIntPoint point = D2.DoubleIntPoint.of(0.00001, 0.00002, 100);

            assertEquals(0.00001, point.x(), 0.000001);
            assertEquals(0.00002, point.y(), 0.000001);
            assertEquals(100, point.value());
        }

        @Test
        public void testDoubleIntPoint_LargeDoubles() {
            D2.DoubleIntPoint point = D2.DoubleIntPoint.of(999999.999, 888888.888, 777);

            assertEquals(999999.999, point.x(), 0.001);
            assertEquals(888888.888, point.y(), 0.001);
            assertEquals(777, point.value());
        }

        @Test
        public void testDoubleIntPoint_MixedSigns() {
            D2.DoubleIntPoint point = D2.DoubleIntPoint.of(-1.5, 2.5, -300);

            assertEquals(-1.5, point.x(), 0.001);
            assertEquals(2.5, point.y(), 0.001);
            assertEquals(-300, point.value());
        }

        @Test
        public void testDoubleIntPoint_NaN() {
            D2.DoubleIntPoint point1 = D2.DoubleIntPoint.of(1.5, Double.NaN, 100);
            D2.DoubleIntPoint point2 = D2.DoubleIntPoint.of(1.5, Double.NaN, 100);

            assertTrue(point1.equals(point2));
        }

        // ============================================
        // Tests for DoubleLongPoint - Edge Cases
        // ============================================

        @Test
        public void testDoubleLongPoint_MaxValues() {
            D2.DoubleLongPoint point = D2.DoubleLongPoint.of(Double.MAX_VALUE, Double.MIN_VALUE, Long.MAX_VALUE);

            assertEquals(Double.MAX_VALUE, point.x(), 0.0);
            assertEquals(Double.MIN_VALUE, point.y(), 0.0);
            assertEquals(Long.MAX_VALUE, point.value());
        }

        @Test
        public void testDoubleLongPoint_NegativeValues() {
            D2.DoubleLongPoint point = D2.DoubleLongPoint.of(-100.5, -200.5, -3000L);

            assertEquals(-100.5, point.x(), 0.001);
            assertEquals(-200.5, point.y(), 0.001);
            assertEquals(-3000L, point.value());
        }

        @Test
        public void testDoubleLongPoint_ZeroValues() {
            D2.DoubleLongPoint point = D2.DoubleLongPoint.of(0.0, 0.0, 0L);

            assertEquals(0.0, point.x(), 0.0);
            assertEquals(0.0, point.y(), 0.0);
            assertEquals(0L, point.value());
        }

        @Test
        public void testDoubleLongPoint_InfinityCoordinates() {
            D2.DoubleLongPoint point = D2.DoubleLongPoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 1000L);

            assertEquals(Double.POSITIVE_INFINITY, point.x(), 0.0);
            assertEquals(Double.NEGATIVE_INFINITY, point.y(), 0.0);
            assertEquals(1000L, point.value());
        }

        // ============================================
        // Tests for DoubleDoublePoint - Edge Cases
        // ============================================

        @Test
        public void testDoubleDoublePoint_MaxDoubleValues() {
            D2.DoubleDoublePoint point = D2.DoubleDoublePoint.of(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

            assertEquals(Double.MAX_VALUE, point.x(), 0.0);
            assertEquals(Double.MAX_VALUE, point.y(), 0.0);
            assertEquals(Double.MAX_VALUE, point.value(), 0.0);
        }

        @Test
        public void testDoubleDoublePoint_MinDoubleValues() {
            D2.DoubleDoublePoint point = D2.DoubleDoublePoint.of(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);

            assertEquals(Double.MIN_VALUE, point.x(), 0.0);
            assertEquals(Double.MIN_VALUE, point.y(), 0.0);
            assertEquals(Double.MIN_VALUE, point.value(), 0.0);
        }

        @Test
        public void testDoubleDoublePoint_AllNaN() {
            D2.DoubleDoublePoint point1 = D2.DoubleDoublePoint.of(Double.NaN, Double.NaN, Double.NaN);
            D2.DoubleDoublePoint point2 = D2.DoubleDoublePoint.of(Double.NaN, Double.NaN, Double.NaN);

            assertTrue(point1.equals(point2));
        }

        @Test
        public void testDoubleDoublePoint_AllInfinity() {
            D2.DoubleDoublePoint point = D2.DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

            assertEquals(Double.POSITIVE_INFINITY, point.x(), 0.0);
            assertEquals(Double.NEGATIVE_INFINITY, point.y(), 0.0);
            assertEquals(Double.POSITIVE_INFINITY, point.value(), 0.0);
        }

        @Test
        public void testDoubleDoublePoint_NegativeZero() {
            D2.DoubleDoublePoint point1 = D2.DoubleDoublePoint.of(-0.0, 0.0, -0.0);
            D2.DoubleDoublePoint point2 = D2.DoubleDoublePoint.of(0.0, -0.0, 0.0);

            assertFalse(point1.equals(point2));
        }

        @Test
        public void testDoubleDoublePoint_VerySmallValues() {
            D2.DoubleDoublePoint point = D2.DoubleDoublePoint.of(1e-308, 1e-307, 1e-306);

            assertEquals(1e-308, point.x(), 0.0);
            assertEquals(1e-307, point.y(), 0.0);
            assertEquals(1e-306, point.value(), 0.0);
        }

        @Test
        public void testDoubleDoublePoint_MixedSpecialValues() {
            D2.DoubleDoublePoint point = D2.DoubleDoublePoint.of(Double.NaN, Double.POSITIVE_INFINITY, -0.0);

            assertTrue(Double.isNaN(point.x()));
            assertEquals(Double.POSITIVE_INFINITY, point.y(), 0.0);
            assertEquals(-0.0, point.value(), 0.0);
        }

        // ============================================
        // Tests for DoubleObjPoint - Edge Cases
        // ============================================

        @Test
        public void testDoubleObjPoint_NullValue() {
            D2.DoubleObjPoint<String> point = D2.DoubleObjPoint.of(10.5, 20.5, null);

            assertEquals(10.5, point.x(), 0.001);
            assertEquals(20.5, point.y(), 0.001);
            assertEquals(null, point.value());
        }

        @Test
        public void testDoubleObjPoint_DoubleObject() {
            D2.DoubleObjPoint<Double> point = D2.DoubleObjPoint.of(1.5, 2.5, Double.MAX_VALUE);

            assertEquals(1.5, point.x(), 0.001);
            assertEquals(2.5, point.y(), 0.001);
            assertEquals(Double.MAX_VALUE, point.value());
        }

        @Test
        public void testDoubleObjPoint_NaNCoordinates() {
            D2.DoubleObjPoint<String> point = D2.DoubleObjPoint.of(Double.NaN, Double.NaN, "test");

            assertTrue(Double.isNaN(point.x()));
            assertTrue(Double.isNaN(point.y()));
            assertEquals("test", point.value());
        }

        @Test
        public void testDoubleObjPoint_InfinityCoordinates() {
            D2.DoubleObjPoint<Integer> point = D2.DoubleObjPoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 42);

            assertEquals(Double.POSITIVE_INFINITY, point.x(), 0.0);
            assertEquals(Double.NEGATIVE_INFINITY, point.y(), 0.0);
            assertEquals(42, point.value());
        }

        @Test
        public void testDoubleObjPoint_Equals_DifferentNullValues() {
            D2.DoubleObjPoint<String> point1 = D2.DoubleObjPoint.of(1.5, 2.5, "test");
            D2.DoubleObjPoint<String> point2 = D2.DoubleObjPoint.of(1.5, 2.5, null);

            assertFalse(point1.equals(point2));
        }

        // ============================================
        // Cross-Type Equality Tests
        // ============================================

        @Test
        public void testCrossType_IntIntPoint_vs_LongLongPoint() {
            D2.IntIntPoint intPoint = D2.IntIntPoint.of(10, 20, 30);
            D2.LongLongPoint longPoint = D2.LongLongPoint.of(10L, 20L, 30L);

            assertFalse(intPoint.equals(longPoint));
            assertFalse(longPoint.equals(intPoint));
        }

        @Test
        public void testCrossType_DoubleDoublePoint_vs_IntIntPoint() {
            D2.DoubleDoublePoint doublePoint = D2.DoubleDoublePoint.of(10.0, 20.0, 30.0);
            D2.IntIntPoint intPoint = D2.IntIntPoint.of(10, 20, 30);

            assertFalse(doublePoint.equals(intPoint));
            assertFalse(intPoint.equals(doublePoint));
        }

        @Test
        public void testCrossType_ByteObjPoint_vs_IntObjPoint() {
            D2.ByteObjPoint<String> bytePoint = D2.ByteObjPoint.of((byte) 10, (byte) 20, "test");
            D2.IntObjPoint<String> intPoint = D2.IntObjPoint.of(10, 20, "test");

            assertFalse(bytePoint.equals(intPoint));
            assertFalse(intPoint.equals(bytePoint));
        }

        // ============================================
        // HashCode Distribution Tests
        // ============================================

        @Test
        public void testHashCode_IntIntPoint_Distribution() {
            D2.IntIntPoint point1 = D2.IntIntPoint.of(1, 2, 3);
            D2.IntIntPoint point2 = D2.IntIntPoint.of(2, 1, 3);
            D2.IntIntPoint point3 = D2.IntIntPoint.of(3, 2, 1);

            // All should have different hash codes due to different coordinates
            int hash1 = point1.hashCode();
            int hash2 = point2.hashCode();
            int hash3 = point3.hashCode();

            assertTrue(hash1 != hash2 || hash1 != hash3 || hash2 != hash3);
        }

        @Test
        public void testHashCode_DoubleDoublePoint_NaN() {
            D2.DoubleDoublePoint point1 = D2.DoubleDoublePoint.of(Double.NaN, 2.0, 3.0);
            D2.DoubleDoublePoint point2 = D2.DoubleDoublePoint.of(Double.NaN, 2.0, 3.0);

            // Hash codes should be equal for NaN values
            assertEquals(point1.hashCode(), point2.hashCode());
        }

        // ============================================
        // ToString Format Tests
        // ============================================

        @Test
        public void testToString_ByteBytePoint_Format() {
            D2.ByteBytePoint point = D2.ByteBytePoint.of((byte) -128, (byte) 127, (byte) 0);
            String result = point.toString();

            assertEquals("ByteBytePoint[x=-128, y=127, value=0]", result);
        }

        @Test
        public void testToString_IntIntPoint_Format() {
            D2.IntIntPoint point = D2.IntIntPoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
            String result = point.toString();

            assertEquals("IntIntPoint[x=" + Integer.MIN_VALUE + ", y=" + Integer.MAX_VALUE + ", value=0]", result);
        }

        @Test
        public void testToString_LongLongPoint_Format() {
            D2.LongLongPoint point = D2.LongLongPoint.of(Long.MIN_VALUE, Long.MAX_VALUE, 0L);
            String result = point.toString();

            assertEquals("LongLongPoint[x=" + Long.MIN_VALUE + ", y=" + Long.MAX_VALUE + ", value=0]", result);
        }

        @Test
        public void testToString_DoubleDoublePoint_Format() {
            D2.DoubleDoublePoint point = D2.DoubleDoublePoint.of(1.5, 2.5, 3.5);
            String result = point.toString();

            assertTrue(result.contains("1.5"));
            assertTrue(result.contains("2.5"));
            assertTrue(result.contains("3.5"));
        }

        @Test
        public void testToString_ObjPoint_NullValue() {
            D2.IntObjPoint<String> point = D2.IntObjPoint.of(10, 20, null);
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
            D2.IntIntPoint point = D2.IntIntPoint.of(1, 2, 3);

            assertFalse(point.equals(null));
        }

        @Test
        public void testEquals_IntIntPoint_String() {
            D2.IntIntPoint point = D2.IntIntPoint.of(1, 2, 3);

            assertFalse(point.equals("not a point"));
        }

        @Test
        public void testEquals_IntIntPoint_SameReference() {
            D2.IntIntPoint point = D2.IntIntPoint.of(10, 20, 30);

            assertTrue(point.equals(point));
        }

        @Test
        public void testEquals_LongLongPoint_DifferentX() {
            D2.LongLongPoint point1 = D2.LongLongPoint.of(100L, 200L, 300L);
            D2.LongLongPoint point2 = D2.LongLongPoint.of(999L, 200L, 300L);

            assertFalse(point1.equals(point2));
        }

        @Test
        public void testEquals_LongLongPoint_DifferentY() {
            D2.LongLongPoint point1 = D2.LongLongPoint.of(100L, 200L, 300L);
            D2.LongLongPoint point2 = D2.LongLongPoint.of(100L, 999L, 300L);

            assertFalse(point1.equals(point2));
        }

        @Test
        public void testEquals_LongLongPoint_DifferentV() {
            D2.LongLongPoint point1 = D2.LongLongPoint.of(100L, 200L, 300L);
            D2.LongLongPoint point2 = D2.LongLongPoint.of(100L, 200L, 999L);

            assertFalse(point1.equals(point2));
        }

        @Test
        public void testEquals_DoubleDoublePoint_DifferentX() {
            D2.DoubleDoublePoint point1 = D2.DoubleDoublePoint.of(1.5, 2.5, 3.5);
            D2.DoubleDoublePoint point2 = D2.DoubleDoublePoint.of(9.9, 2.5, 3.5);

            assertFalse(point1.equals(point2));
        }

        @Test
        public void testEquals_DoubleDoublePoint_DifferentY() {
            D2.DoubleDoublePoint point1 = D2.DoubleDoublePoint.of(1.5, 2.5, 3.5);
            D2.DoubleDoublePoint point2 = D2.DoubleDoublePoint.of(1.5, 9.9, 3.5);

            assertFalse(point1.equals(point2));
        }

        @Test
        public void testEquals_DoubleDoublePoint_DifferentV() {
            D2.DoubleDoublePoint point1 = D2.DoubleDoublePoint.of(1.5, 2.5, 3.5);
            D2.DoubleDoublePoint point2 = D2.DoubleDoublePoint.of(1.5, 2.5, 9.9);

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

            D2.IntObjPoint<CustomObject> point1 = D2.IntObjPoint.of(10, 20, obj1);
            D2.IntObjPoint<CustomObject> point2 = D2.IntObjPoint.of(10, 20, obj2);

            assertTrue(point1.equals(point2));
            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void testObjPoint_WithNestedCollection() {
            java.util.List<java.util.List<Integer>> nested = new java.util.ArrayList<>();
            nested.add(java.util.Arrays.asList(1, 2, 3));
            nested.add(java.util.Arrays.asList(4, 5, 6));

            D2.DoubleObjPoint<java.util.List<java.util.List<Integer>>> point = D2.DoubleObjPoint.of(1.5, 2.5, nested);

            assertEquals(1.5, point.x(), 0.001);
            assertEquals(2.5, point.y(), 0.001);
            assertEquals(nested, point.value());
        }

        // ============================================
        // Boundary Value Tests
        // ============================================

        @Test
        public void testBytePoint_BoundaryValues() {
            D2.ByteBytePoint minPoint = D2.ByteBytePoint.of(Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE);
            D2.ByteBytePoint maxPoint = D2.ByteBytePoint.of(Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE);

            assertEquals(Byte.MIN_VALUE, minPoint.x());
            assertEquals(Byte.MIN_VALUE, minPoint.y());
            assertEquals(Byte.MIN_VALUE, minPoint.value());

            assertEquals(Byte.MAX_VALUE, maxPoint.x());
            assertEquals(Byte.MAX_VALUE, maxPoint.y());
            assertEquals(Byte.MAX_VALUE, maxPoint.value());

            assertFalse(minPoint.equals(maxPoint));
        }

        @Test
        public void testIntPoint_BoundaryValues() {
            D2.IntIntPoint minPoint = D2.IntIntPoint.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
            D2.IntIntPoint maxPoint = D2.IntIntPoint.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

            assertEquals(Integer.MIN_VALUE, minPoint.x());
            assertEquals(Integer.MIN_VALUE, minPoint.y());
            assertEquals(Integer.MIN_VALUE, minPoint.value());

            assertEquals(Integer.MAX_VALUE, maxPoint.x());
            assertEquals(Integer.MAX_VALUE, maxPoint.y());
            assertEquals(Integer.MAX_VALUE, maxPoint.value());

            assertFalse(minPoint.equals(maxPoint));
        }

        @Test
        public void testLongPoint_BoundaryValues() {
            D2.LongLongPoint minPoint = D2.LongLongPoint.of(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);
            D2.LongLongPoint maxPoint = D2.LongLongPoint.of(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);

            assertEquals(Long.MIN_VALUE, minPoint.x());
            assertEquals(Long.MIN_VALUE, minPoint.y());
            assertEquals(Long.MIN_VALUE, minPoint.value());

            assertEquals(Long.MAX_VALUE, maxPoint.x());
            assertEquals(Long.MAX_VALUE, maxPoint.y());
            assertEquals(Long.MAX_VALUE, maxPoint.value());

            assertFalse(minPoint.equals(maxPoint));
        }

        @Test
        public void testDoublePoint_SpecialValues() {
            D2.DoubleDoublePoint nanPoint = D2.DoubleDoublePoint.of(Double.NaN, Double.NaN, Double.NaN);
            D2.DoubleDoublePoint infPoint = D2.DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

            assertTrue(Double.isNaN(nanPoint.x()));
            assertTrue(Double.isNaN(nanPoint.y()));
            assertTrue(Double.isNaN(nanPoint.value()));

            assertEquals(Double.POSITIVE_INFINITY, infPoint.x(), 0.0);
            assertEquals(Double.NEGATIVE_INFINITY, infPoint.y(), 0.0);
            assertEquals(Double.POSITIVE_INFINITY, infPoint.value(), 0.0);

            assertFalse(nanPoint.equals(infPoint));
        }

        // ============================================
        // Comprehensive Coverage Tests
        // ============================================

        @Test
        public void testAllPointTypes_FactoryMethod() {
            // Verify factory methods work for all point types
            assertNotNull(D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3));
            assertNotNull(D2.ByteIntPoint.of((byte) 1, (byte) 2, 3));
            assertNotNull(D2.ByteLongPoint.of((byte) 1, (byte) 2, 3L));
            assertNotNull(D2.ByteDoublePoint.of((byte) 1, (byte) 2, 3.0));
            assertNotNull(D2.ByteObjPoint.of((byte) 1, (byte) 2, "test"));

            assertNotNull(D2.IntBytePoint.of(1, 2, (byte) 3));
            assertNotNull(D2.IntIntPoint.of(1, 2, 3));
            assertNotNull(D2.IntLongPoint.of(1, 2, 3L));
            assertNotNull(D2.IntDoublePoint.of(1, 2, 3.0));
            assertNotNull(D2.IntObjPoint.of(1, 2, "test"));

            assertNotNull(D2.LongBytePoint.of(1L, 2L, (byte) 3));
            assertNotNull(D2.LongIntPoint.of(1L, 2L, 3));
            assertNotNull(D2.LongLongPoint.of(1L, 2L, 3L));
            assertNotNull(D2.LongDoublePoint.of(1L, 2L, 3.0));
            assertNotNull(D2.LongObjPoint.of(1L, 2L, "test"));

            assertNotNull(D2.DoubleBytePoint.of(1.0, 2.0, (byte) 3));
            assertNotNull(D2.DoubleIntPoint.of(1.0, 2.0, 3));
            assertNotNull(D2.DoubleLongPoint.of(1.0, 2.0, 3L));
            assertNotNull(D2.DoubleDoublePoint.of(1.0, 2.0, 3.0));
            assertNotNull(D2.DoubleObjPoint.of(1.0, 2.0, "test"));
        }

        @Test
        public void testAllPointTypes_FieldAccess() {
            // Verify all fields are accessible and correct
            D2.IntIntPoint point = D2.IntIntPoint.of(10, 20, 30);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(30, point.value());
        }

        @Test
        public void testAllPointTypes_ImmutabilityPattern() {
            // Points should be immutable - fields are final
            // This is verified by the compiler, but we can check behavior
            D2.IntIntPoint point1 = D2.IntIntPoint.of(1, 2, 3);
            D2.IntIntPoint point2 = D2.IntIntPoint.of(1, 2, 3);

            // Creating new points doesn't affect existing ones
            assertTrue(point1.equals(point2));
            assertEquals(1, point1.x());
            assertEquals(2, point1.y());
            assertEquals(3, point1.value());
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for the Points utility class and all inner Point classes.
     * Tests cover factory methods, field access, equality, hashing, and string representation
     * for all point types: ByteBytePoint, ByteIntPoint, ByteLongPoint, ByteDoublePoint, ByteObjPoint,
     * IntBytePoint, IntIntPoint, IntLongPoint, IntDoublePoint, IntObjPoint,
     * LongBytePoint, LongIntPoint, LongLongPoint, LongDoublePoint, LongObjPoint,
     * DoubleBytePoint, DoubleIntPoint, DoubleLongPoint, DoubleDoublePoint, DoubleObjPoint.
     */
    @Tag("2511")
    class Points2511Test extends TestBase {

        // ============ ByteBytePoint Tests ============

        @Test
        public void testByteBytePoint_of() {
            Points.D2.ByteBytePoint point = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 100);
            assertNotNull(point);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(100, point.value());
        }

        @Test
        public void testByteBytePoint_negativeValues() {
            Points.D2.ByteBytePoint point = Points.D2.ByteBytePoint.of((byte) -10, (byte) -20, (byte) -100);
            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(-100, point.value());
        }

        @Test
        public void testByteBytePoint_zeroValues() {
            Points.D2.ByteBytePoint point = Points.D2.ByteBytePoint.of((byte) 0, (byte) 0, (byte) 0);
            assertEquals(0, point.x());
            assertEquals(0, point.y());
            assertEquals(0, point.value());
        }

        @Test
        public void testByteBytePoint_maxValues() {
            Points.D2.ByteBytePoint point = Points.D2.ByteBytePoint.of(Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE);
            assertEquals(Byte.MAX_VALUE, point.x());
            assertEquals(Byte.MAX_VALUE, point.y());
            assertEquals(Byte.MAX_VALUE, point.value());
        }

        @Test
        public void testByteBytePoint_equals_same() {
            Points.D2.ByteBytePoint p1 = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
            Points.D2.ByteBytePoint p2 = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(p1, p2);
            assertTrue(p1.equals(p2));
        }

        @Test
        public void testByteBytePoint_equals_different() {
            Points.D2.ByteBytePoint p1 = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
            Points.D2.ByteBytePoint p2 = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 31);
            assertFalse(p1.equals(p2));
        }

        @Test
        public void testByteBytePoint_equals_self() {
            Points.D2.ByteBytePoint p = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(p, p);
        }

        @Test
        public void testByteBytePoint_equals_null() {
            Points.D2.ByteBytePoint p = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
            assertFalse(p.equals(null));
        }

        @Test
        public void testByteBytePoint_equals_differentType() {
            Points.D2.ByteBytePoint p = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
            assertFalse(p.equals("not a point"));
        }

        @Test
        public void testByteBytePoint_hashCode_same() {
            Points.D2.ByteBytePoint p1 = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
            Points.D2.ByteBytePoint p2 = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        public void testByteBytePoint_hashCode_different() {
            Points.D2.ByteBytePoint p1 = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
            Points.D2.ByteBytePoint p2 = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 31);
            assertFalse(p1.hashCode() == p2.hashCode());
        }

        @Test
        public void testByteBytePoint_toString() {
            Points.D2.ByteBytePoint point = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
            String str = point.toString();
            assertNotNull(str);
            assertTrue(str.contains("10"));
            assertTrue(str.contains("20"));
            assertTrue(str.contains("30"));
        }

        // ============ ByteIntPoint Tests ============

        @Test
        public void testByteIntPoint_of() {
            Points.D2.ByteIntPoint point = Points.D2.ByteIntPoint.of((byte) 10, (byte) 20, 1000);
            assertNotNull(point);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(1000, point.value());
        }

        @Test
        public void testByteIntPoint_largeValue() {
            Points.D2.ByteIntPoint point = Points.D2.ByteIntPoint.of((byte) 10, (byte) 20, Integer.MAX_VALUE);
            assertEquals(Integer.MAX_VALUE, point.value());
        }

        @Test
        public void testByteIntPoint_equals() {
            Points.D2.ByteIntPoint p1 = Points.D2.ByteIntPoint.of((byte) 10, (byte) 20, 100);
            Points.D2.ByteIntPoint p2 = Points.D2.ByteIntPoint.of((byte) 10, (byte) 20, 100);
            assertEquals(p1, p2);
        }

        @Test
        public void testByteIntPoint_hashCode() {
            Points.D2.ByteIntPoint p1 = Points.D2.ByteIntPoint.of((byte) 10, (byte) 20, 100);
            Points.D2.ByteIntPoint p2 = Points.D2.ByteIntPoint.of((byte) 10, (byte) 20, 100);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        public void testByteIntPoint_toString() {
            Points.D2.ByteIntPoint point = Points.D2.ByteIntPoint.of((byte) 5, (byte) 10, 50);
            String str = point.toString();
            assertTrue(str.contains("5"));
            assertTrue(str.contains("10"));
            assertTrue(str.contains("50"));
        }

        // ============ ByteLongPoint Tests ============

        @Test
        public void testByteLongPoint_of() {
            Points.D2.ByteLongPoint point = Points.D2.ByteLongPoint.of((byte) 10, (byte) 20, 1000000L);
            assertNotNull(point);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(1000000L, point.value());
        }

        @Test
        public void testByteLongPoint_largeValue() {
            Points.D2.ByteLongPoint point = Points.D2.ByteLongPoint.of((byte) 10, (byte) 20, Long.MAX_VALUE);
            assertEquals(Long.MAX_VALUE, point.value());
        }

        @Test
        public void testByteLongPoint_equals() {
            Points.D2.ByteLongPoint p1 = Points.D2.ByteLongPoint.of((byte) 10, (byte) 20, 1000L);
            Points.D2.ByteLongPoint p2 = Points.D2.ByteLongPoint.of((byte) 10, (byte) 20, 1000L);
            assertEquals(p1, p2);
        }

        @Test
        public void testByteLongPoint_toString() {
            Points.D2.ByteLongPoint point = Points.D2.ByteLongPoint.of((byte) 5, (byte) 10, 500L);
            String str = point.toString();
            assertTrue(str.contains("5"));
            assertTrue(str.contains("10"));
        }

        // ============ ByteDoublePoint Tests ============

        @Test
        public void testByteDoublePoint_of() {
            Points.D2.ByteDoublePoint point = Points.D2.ByteDoublePoint.of((byte) 10, (byte) 20, 99.99);
            assertNotNull(point);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(99.99, point.value(), 0.01);
        }

        @Test
        public void testByteDoublePoint_zeroValue() {
            Points.D2.ByteDoublePoint point = Points.D2.ByteDoublePoint.of((byte) 0, (byte) 0, 0.0);
            assertEquals(0.0, point.value(), 0.01);
        }

        @Test
        public void testByteDoublePoint_negativeValue() {
            Points.D2.ByteDoublePoint point = Points.D2.ByteDoublePoint.of((byte) 10, (byte) 20, -99.99);
            assertEquals(-99.99, point.value(), 0.01);
        }

        @Test
        public void testByteDoublePoint_equals() {
            Points.D2.ByteDoublePoint p1 = Points.D2.ByteDoublePoint.of((byte) 10, (byte) 20, 99.99);
            Points.D2.ByteDoublePoint p2 = Points.D2.ByteDoublePoint.of((byte) 10, (byte) 20, 99.99);
            assertEquals(p1, p2);
        }

        @Test
        public void testByteDoublePoint_toString() {
            Points.D2.ByteDoublePoint point = Points.D2.ByteDoublePoint.of((byte) 5, (byte) 10, 50.5);
            String str = point.toString();
            assertTrue(str.contains("5"));
            assertTrue(str.contains("10"));
        }

        // ============ ByteObjPoint Tests ============

        @Test
        public void testByteObjPoint_of_string() {
            Points.D2.ByteObjPoint<String> point = Points.D2.ByteObjPoint.of((byte) 5, (byte) 10, "marker");
            assertNotNull(point);
            assertEquals(5, point.x());
            assertEquals(10, point.y());
            assertEquals("marker", point.value());
        }

        @Test
        public void testByteObjPoint_of_integer() {
            Points.D2.ByteObjPoint<Integer> point = Points.D2.ByteObjPoint.of((byte) 5, (byte) 10, 42);
            assertEquals(42, point.value());
        }

        @Test
        public void testByteObjPoint_of_null() {
            Points.D2.ByteObjPoint<String> point = Points.D2.ByteObjPoint.of((byte) 5, (byte) 10, null);
            assertNotNull(point);
            assertEquals(null, point.value());
        }

        @Test
        public void testByteObjPoint_equals() {
            Points.D2.ByteObjPoint<String> p1 = Points.D2.ByteObjPoint.of((byte) 5, (byte) 10, "test");
            Points.D2.ByteObjPoint<String> p2 = Points.D2.ByteObjPoint.of((byte) 5, (byte) 10, "test");
            assertEquals(p1, p2);
        }

        @Test
        public void testByteObjPoint_equals_differentValue() {
            Points.D2.ByteObjPoint<String> p1 = Points.D2.ByteObjPoint.of((byte) 5, (byte) 10, "test1");
            Points.D2.ByteObjPoint<String> p2 = Points.D2.ByteObjPoint.of((byte) 5, (byte) 10, "test2");
            assertFalse(p1.equals(p2));
        }

        @Test
        public void testByteObjPoint_toString() {
            Points.D2.ByteObjPoint<String> point = Points.D2.ByteObjPoint.of((byte) 5, (byte) 10, "test");
            String str = point.toString();
            assertTrue(str.contains("5"));
            assertTrue(str.contains("10"));
        }

        // ============ IntBytePoint Tests ============

        @Test
        public void testIntBytePoint_of() {
            Points.D2.IntBytePoint point = Points.D2.IntBytePoint.of(100, 200, (byte) 50);
            assertNotNull(point);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(50, point.value());
        }

        @Test
        public void testIntBytePoint_largeCoordinates() {
            Points.D2.IntBytePoint point = Points.D2.IntBytePoint.of(Integer.MAX_VALUE, Integer.MIN_VALUE, (byte) 0);
            assertEquals(Integer.MAX_VALUE, point.x());
            assertEquals(Integer.MIN_VALUE, point.y());
        }

        @Test
        public void testIntBytePoint_equals() {
            Points.D2.IntBytePoint p1 = Points.D2.IntBytePoint.of(100, 200, (byte) 50);
            Points.D2.IntBytePoint p2 = Points.D2.IntBytePoint.of(100, 200, (byte) 50);
            assertEquals(p1, p2);
        }

        @Test
        public void testIntBytePoint_toString() {
            Points.D2.IntBytePoint point = Points.D2.IntBytePoint.of(100, 200, (byte) 50);
            String str = point.toString();
            assertTrue(str.contains("100"));
            assertTrue(str.contains("200"));
        }

        // ============ IntIntPoint Tests ============

        @Test
        public void testIntIntPoint_of() {
            Points.D2.IntIntPoint point = Points.D2.IntIntPoint.of(100, 200, 300);
            assertNotNull(point);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(300, point.value());
        }

        @Test
        public void testIntIntPoint_equals() {
            Points.D2.IntIntPoint p1 = Points.D2.IntIntPoint.of(100, 200, 300);
            Points.D2.IntIntPoint p2 = Points.D2.IntIntPoint.of(100, 200, 300);
            assertEquals(p1, p2);
        }

        @Test
        public void testIntIntPoint_equals_differentValue() {
            Points.D2.IntIntPoint p1 = Points.D2.IntIntPoint.of(100, 200, 300);
            Points.D2.IntIntPoint p2 = Points.D2.IntIntPoint.of(100, 200, 301);
            assertFalse(p1.equals(p2));
        }

        @Test
        public void testIntIntPoint_hashCode() {
            Points.D2.IntIntPoint p1 = Points.D2.IntIntPoint.of(100, 200, 300);
            Points.D2.IntIntPoint p2 = Points.D2.IntIntPoint.of(100, 200, 300);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        public void testIntIntPoint_toString() {
            Points.D2.IntIntPoint point = Points.D2.IntIntPoint.of(100, 200, 300);
            String str = point.toString();
            assertTrue(str.contains("100"));
            assertTrue(str.contains("200"));
            assertTrue(str.contains("300"));
        }

        // ============ IntLongPoint Tests ============

        @Test
        public void testIntLongPoint_of() {
            Points.D2.IntLongPoint point = Points.D2.IntLongPoint.of(100, 200, 1000000L);
            assertNotNull(point);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(1000000L, point.value());
        }

        @Test
        public void testIntLongPoint_equals() {
            Points.D2.IntLongPoint p1 = Points.D2.IntLongPoint.of(100, 200, 1000000L);
            Points.D2.IntLongPoint p2 = Points.D2.IntLongPoint.of(100, 200, 1000000L);
            assertEquals(p1, p2);
        }

        @Test
        public void testIntLongPoint_toString() {
            Points.D2.IntLongPoint point = Points.D2.IntLongPoint.of(100, 200, 1000000L);
            String str = point.toString();
            assertTrue(str.contains("100"));
            assertTrue(str.contains("200"));
        }

        // ============ IntDoublePoint Tests ============

        @Test
        public void testIntDoublePoint_of() {
            Points.D2.IntDoublePoint point = Points.D2.IntDoublePoint.of(100, 200, 3.14159);
            assertNotNull(point);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(3.14159, point.value(), 0.00001);
        }

        @Test
        public void testIntDoublePoint_equals() {
            Points.D2.IntDoublePoint p1 = Points.D2.IntDoublePoint.of(100, 200, 3.14159);
            Points.D2.IntDoublePoint p2 = Points.D2.IntDoublePoint.of(100, 200, 3.14159);
            assertEquals(p1, p2);
        }

        @Test
        public void testIntDoublePoint_toString() {
            Points.D2.IntDoublePoint point = Points.D2.IntDoublePoint.of(100, 200, 3.14);
            String str = point.toString();
            assertTrue(str.contains("100"));
            assertTrue(str.contains("200"));
        }

        // ============ IntObjPoint Tests ============

        @Test
        public void testIntObjPoint_of() {
            Points.D2.IntObjPoint<String> point = Points.D2.IntObjPoint.of(10, 20, "marker");
            assertNotNull(point);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals("marker", point.value());
        }

        @Test
        public void testIntObjPoint_of_list() {
            Points.D2.IntObjPoint<java.util.List<String>> point = Points.D2.IntObjPoint.of(10, 20, java.util.Arrays.asList("a", "b", "c"));
            assertEquals(3, point.value().size());
        }

        @Test
        public void testIntObjPoint_equals() {
            Points.D2.IntObjPoint<String> p1 = Points.D2.IntObjPoint.of(10, 20, "test");
            Points.D2.IntObjPoint<String> p2 = Points.D2.IntObjPoint.of(10, 20, "test");
            assertEquals(p1, p2);
        }

        @Test
        public void testIntObjPoint_toString() {
            Points.D2.IntObjPoint<String> point = Points.D2.IntObjPoint.of(10, 20, "test");
            String str = point.toString();
            assertTrue(str.contains("10"));
            assertTrue(str.contains("20"));
        }

        // ============ LongBytePoint Tests ============

        @Test
        public void testLongBytePoint_of() {
            Points.D2.LongBytePoint point = Points.D2.LongBytePoint.of(1000L, 2000L, (byte) 50);
            assertNotNull(point);
            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(50, point.value());
        }

        @Test
        public void testLongBytePoint_largeCoordinates() {
            Points.D2.LongBytePoint point = Points.D2.LongBytePoint.of(Long.MAX_VALUE, Long.MIN_VALUE, (byte) 0);
            assertEquals(Long.MAX_VALUE, point.x());
            assertEquals(Long.MIN_VALUE, point.y());
        }

        @Test
        public void testLongBytePoint_equals() {
            Points.D2.LongBytePoint p1 = Points.D2.LongBytePoint.of(1000L, 2000L, (byte) 50);
            Points.D2.LongBytePoint p2 = Points.D2.LongBytePoint.of(1000L, 2000L, (byte) 50);
            assertEquals(p1, p2);
        }

        @Test
        public void testLongBytePoint_toString() {
            Points.D2.LongBytePoint point = Points.D2.LongBytePoint.of(1000L, 2000L, (byte) 50);
            String str = point.toString();
            assertTrue(str.contains("1000"));
            assertTrue(str.contains("2000"));
        }

        // ============ LongIntPoint Tests ============

        @Test
        public void testLongIntPoint_of() {
            Points.D2.LongIntPoint point = Points.D2.LongIntPoint.of(1000L, 2000L, 500);
            assertNotNull(point);
            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(500, point.value());
        }

        @Test
        public void testLongIntPoint_equals() {
            Points.D2.LongIntPoint p1 = Points.D2.LongIntPoint.of(1000L, 2000L, 500);
            Points.D2.LongIntPoint p2 = Points.D2.LongIntPoint.of(1000L, 2000L, 500);
            assertEquals(p1, p2);
        }

        @Test
        public void testLongIntPoint_toString() {
            Points.D2.LongIntPoint point = Points.D2.LongIntPoint.of(1000L, 2000L, 500);
            String str = point.toString();
            assertTrue(str.contains("1000"));
            assertTrue(str.contains("2000"));
        }

        // ============ LongLongPoint Tests ============

        @Test
        public void testLongLongPoint_of() {
            Points.D2.LongLongPoint point = Points.D2.LongLongPoint.of(1000L, 2000L, 3000L);
            assertNotNull(point);
            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(3000L, point.value());
        }

        @Test
        public void testLongLongPoint_equals() {
            Points.D2.LongLongPoint p1 = Points.D2.LongLongPoint.of(1000L, 2000L, 3000L);
            Points.D2.LongLongPoint p2 = Points.D2.LongLongPoint.of(1000L, 2000L, 3000L);
            assertEquals(p1, p2);
        }

        @Test
        public void testLongLongPoint_equals_differentValue() {
            Points.D2.LongLongPoint p1 = Points.D2.LongLongPoint.of(1000L, 2000L, 3000L);
            Points.D2.LongLongPoint p2 = Points.D2.LongLongPoint.of(1000L, 2000L, 3001L);
            assertFalse(p1.equals(p2));
        }

        @Test
        public void testLongLongPoint_hashCode() {
            Points.D2.LongLongPoint p1 = Points.D2.LongLongPoint.of(1000L, 2000L, 3000L);
            Points.D2.LongLongPoint p2 = Points.D2.LongLongPoint.of(1000L, 2000L, 3000L);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        public void testLongLongPoint_toString() {
            Points.D2.LongLongPoint point = Points.D2.LongLongPoint.of(1000L, 2000L, 3000L);
            String str = point.toString();
            assertTrue(str.contains("1000"));
            assertTrue(str.contains("2000"));
            assertTrue(str.contains("3000"));
        }

        // ============ LongDoublePoint Tests ============

        @Test
        public void testLongDoublePoint_of() {
            Points.D2.LongDoublePoint point = Points.D2.LongDoublePoint.of(1000L, 2000L, 99.99);
            assertNotNull(point);
            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(99.99, point.value(), 0.01);
        }

        @Test
        public void testLongDoublePoint_equals() {
            Points.D2.LongDoublePoint p1 = Points.D2.LongDoublePoint.of(1000L, 2000L, 99.99);
            Points.D2.LongDoublePoint p2 = Points.D2.LongDoublePoint.of(1000L, 2000L, 99.99);
            assertEquals(p1, p2);
        }

        @Test
        public void testLongDoublePoint_toString() {
            Points.D2.LongDoublePoint point = Points.D2.LongDoublePoint.of(1000L, 2000L, 99.99);
            String str = point.toString();
            assertTrue(str.contains("1000"));
            assertTrue(str.contains("2000"));
        }

        // ============ LongObjPoint Tests ============

        @Test
        public void testLongObjPoint_of() {
            Points.D2.LongObjPoint<String> point = Points.D2.LongObjPoint.of(1000L, 2000L, "marker");
            assertNotNull(point);
            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals("marker", point.value());
        }

        @Test
        public void testLongObjPoint_equals() {
            Points.D2.LongObjPoint<String> p1 = Points.D2.LongObjPoint.of(1000L, 2000L, "test");
            Points.D2.LongObjPoint<String> p2 = Points.D2.LongObjPoint.of(1000L, 2000L, "test");
            assertEquals(p1, p2);
        }

        @Test
        public void testLongObjPoint_toString() {
            Points.D2.LongObjPoint<String> point = Points.D2.LongObjPoint.of(1000L, 2000L, "test");
            String str = point.toString();
            assertTrue(str.contains("1000"));
            assertTrue(str.contains("2000"));
        }

        // ============ DoubleBytePoint Tests ============

        @Test
        public void testDoubleBytePoint_of() {
            Points.D2.DoubleBytePoint point = Points.D2.DoubleBytePoint.of(10.5, 20.7, (byte) 50);
            assertNotNull(point);
            assertEquals(10.5, point.x(), 0.01);
            assertEquals(20.7, point.y(), 0.01);
            assertEquals(50, point.value());
        }

        @Test
        public void testDoubleBytePoint_negativeCoordinates() {
            Points.D2.DoubleBytePoint point = Points.D2.DoubleBytePoint.of(-10.5, -20.7, (byte) 50);
            assertEquals(-10.5, point.x(), 0.01);
            assertEquals(-20.7, point.y(), 0.01);
        }

        @Test
        public void testDoubleBytePoint_equals() {
            Points.D2.DoubleBytePoint p1 = Points.D2.DoubleBytePoint.of(10.5, 20.7, (byte) 50);
            Points.D2.DoubleBytePoint p2 = Points.D2.DoubleBytePoint.of(10.5, 20.7, (byte) 50);
            assertEquals(p1, p2);
        }

        @Test
        public void testDoubleBytePoint_toString() {
            Points.D2.DoubleBytePoint point = Points.D2.DoubleBytePoint.of(10.5, 20.7, (byte) 50);
            String str = point.toString();
            assertNotNull(str);
        }

        // ============ DoubleIntPoint Tests ============

        @Test
        public void testDoubleIntPoint_of() {
            Points.D2.DoubleIntPoint point = Points.D2.DoubleIntPoint.of(10.5, 20.7, 100);
            assertNotNull(point);
            assertEquals(10.5, point.x(), 0.01);
            assertEquals(20.7, point.y(), 0.01);
            assertEquals(100, point.value());
        }

        @Test
        public void testDoubleIntPoint_equals() {
            Points.D2.DoubleIntPoint p1 = Points.D2.DoubleIntPoint.of(10.5, 20.7, 100);
            Points.D2.DoubleIntPoint p2 = Points.D2.DoubleIntPoint.of(10.5, 20.7, 100);
            assertEquals(p1, p2);
        }

        @Test
        public void testDoubleIntPoint_toString() {
            Points.D2.DoubleIntPoint point = Points.D2.DoubleIntPoint.of(10.5, 20.7, 100);
            String str = point.toString();
            assertNotNull(str);
        }

        // ============ DoubleLongPoint Tests ============

        @Test
        public void testDoubleLongPoint_of() {
            Points.D2.DoubleLongPoint point = Points.D2.DoubleLongPoint.of(10.5, 20.7, 1000000L);
            assertNotNull(point);
            assertEquals(10.5, point.x(), 0.01);
            assertEquals(20.7, point.y(), 0.01);
            assertEquals(1000000L, point.value());
        }

        @Test
        public void testDoubleLongPoint_equals() {
            Points.D2.DoubleLongPoint p1 = Points.D2.DoubleLongPoint.of(10.5, 20.7, 1000000L);
            Points.D2.DoubleLongPoint p2 = Points.D2.DoubleLongPoint.of(10.5, 20.7, 1000000L);
            assertEquals(p1, p2);
        }

        @Test
        public void testDoubleLongPoint_toString() {
            Points.D2.DoubleLongPoint point = Points.D2.DoubleLongPoint.of(10.5, 20.7, 1000000L);
            String str = point.toString();
            assertNotNull(str);
        }

        // ============ DoubleDoublePoint Tests ============

        @Test
        public void testDoubleDoublePoint_of() {
            Points.D2.DoubleDoublePoint point = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 99.99);
            assertNotNull(point);
            assertEquals(10.5, point.x(), 0.01);
            assertEquals(20.7, point.y(), 0.01);
            assertEquals(99.99, point.value(), 0.01);
        }

        @Test
        public void testDoubleDoublePoint_equals() {
            Points.D2.DoubleDoublePoint p1 = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 99.99);
            Points.D2.DoubleDoublePoint p2 = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 99.99);
            assertEquals(p1, p2);
        }

        @Test
        public void testDoubleDoublePoint_equals_differentValue() {
            Points.D2.DoubleDoublePoint p1 = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 99.99);
            Points.D2.DoubleDoublePoint p2 = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 99.98);
            assertFalse(p1.equals(p2));
        }

        @Test
        public void testDoubleDoublePoint_hashCode() {
            Points.D2.DoubleDoublePoint p1 = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 99.99);
            Points.D2.DoubleDoublePoint p2 = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 99.99);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        public void testDoubleDoublePoint_toString() {
            Points.D2.DoubleDoublePoint point = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 99.99);
            String str = point.toString();
            assertTrue(str.contains("10.5") || str.contains("10."));
            assertTrue(str.contains("20.7") || str.contains("20."));
        }

        // ============ DoubleObjPoint Tests ============

        @Test
        public void testDoubleObjPoint_of() {
            Points.D2.DoubleObjPoint<String> point = Points.D2.DoubleObjPoint.of(10.5, 20.7, "marker");
            assertNotNull(point);
            assertEquals(10.5, point.x(), 0.01);
            assertEquals(20.7, point.y(), 0.01);
            assertEquals("marker", point.value());
        }

        @Test
        public void testDoubleObjPoint_of_null() {
            Points.D2.DoubleObjPoint<String> point = Points.D2.DoubleObjPoint.of(10.5, 20.7, null);
            assertNotNull(point);
            assertEquals(null, point.value());
        }

        @Test
        public void testDoubleObjPoint_equals() {
            Points.D2.DoubleObjPoint<String> p1 = Points.D2.DoubleObjPoint.of(10.5, 20.7, "test");
            Points.D2.DoubleObjPoint<String> p2 = Points.D2.DoubleObjPoint.of(10.5, 20.7, "test");
            assertEquals(p1, p2);
        }

        @Test
        public void testDoubleObjPoint_equals_differentValue() {
            Points.D2.DoubleObjPoint<String> p1 = Points.D2.DoubleObjPoint.of(10.5, 20.7, "test1");
            Points.D2.DoubleObjPoint<String> p2 = Points.D2.DoubleObjPoint.of(10.5, 20.7, "test2");
            assertFalse(p1.equals(p2));
        }

        @Test
        public void testDoubleObjPoint_toString() {
            Points.D2.DoubleObjPoint<String> point = Points.D2.DoubleObjPoint.of(10.5, 20.7, "test");
            String str = point.toString();
            assertTrue(str.contains("10.5") || str.contains("10."));
            assertTrue(str.contains("20.7") || str.contains("20."));
        }

        // ============ Cross-Type Comparison Tests ============

        @Test
        public void testDifferentPointTypes_notEqual() {
            Points.D2.IntIntPoint intPoint = Points.D2.IntIntPoint.of(10, 20, 30);
            Points.D2.ByteBytePoint bytePoint = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);

            assertFalse(intPoint.equals(bytePoint));
            assertFalse(bytePoint.equals(intPoint));
        }

        // ============ Immutability Tests ============

        @Test
        public void testImmutability_byteBytePoint() {
            Points.D2.ByteBytePoint point = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);
            // Fields are public final, so they cannot be modified
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(30, point.value());
        }

        @Test
        public void testImmutability_intIntPoint() {
            Points.D2.IntIntPoint point = Points.D2.IntIntPoint.of(100, 200, 300);
            // Fields are public final, so they cannot be modified
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(300, point.value());
        }

        // ============ Factory Method Tests ============

        @Test
        public void testFactoryMethod_returnsNonNull() {
            assertNotNull(Points.D2.ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3));
            assertNotNull(Points.D2.IntIntPoint.of(1, 2, 3));
            assertNotNull(Points.D2.LongLongPoint.of(1L, 2L, 3L));
            assertNotNull(Points.D2.DoubleDoublePoint.of(1.0, 2.0, 3.0));
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for Points utility class and its nested point classes.
     * Tests all point types with different coordinate and value type combinations.
     * Covers: of(), hashCode(), equals(), toString() for all point variants.
     */
    @Tag("2512")
    class Points2512Test extends TestBase {

        // ============================================
        // Tests for ByteBytePoint
        // ============================================

        @Test
        public void test_ByteBytePoint_of() {
            ByteBytePoint point = ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);

            assertNotNull(point);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(30, point.value());
        }

        @Test
        public void test_ByteBytePoint_hashCode() {
            ByteBytePoint point1 = ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
            ByteBytePoint point2 = ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_ByteBytePoint_equals() {
            ByteBytePoint point1 = ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
            ByteBytePoint point2 = ByteBytePoint.of((byte) 1, (byte) 2, (byte) 3);
            ByteBytePoint point3 = ByteBytePoint.of((byte) 1, (byte) 2, (byte) 4);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
            assertTrue(point1.equals(point1));
            assertFalse(point1.equals(null));
            assertFalse(point1.equals("string"));
        }

        @Test
        public void test_ByteBytePoint_toString() {
            ByteBytePoint point = ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);

            assertEquals("ByteBytePoint[x=10, y=20, value=30]", point.toString());
        }

        // ============================================
        // Tests for ByteIntPoint
        // ============================================

        @Test
        public void test_ByteIntPoint_of() {
            ByteIntPoint point = ByteIntPoint.of((byte) 10, (byte) 20, 300);

            assertNotNull(point);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(300, point.value());
        }

        @Test
        public void test_ByteIntPoint_hashCode() {
            ByteIntPoint point1 = ByteIntPoint.of((byte) 1, (byte) 2, 100);
            ByteIntPoint point2 = ByteIntPoint.of((byte) 1, (byte) 2, 100);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_ByteIntPoint_equals() {
            ByteIntPoint point1 = ByteIntPoint.of((byte) 1, (byte) 2, 100);
            ByteIntPoint point2 = ByteIntPoint.of((byte) 1, (byte) 2, 100);
            ByteIntPoint point3 = ByteIntPoint.of((byte) 1, (byte) 2, 200);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
            assertTrue(point1.equals(point1));
            assertFalse(point1.equals(null));
        }

        @Test
        public void test_ByteIntPoint_toString() {
            ByteIntPoint point = ByteIntPoint.of((byte) 10, (byte) 20, 300);

            assertEquals("ByteIntPoint[x=10, y=20, value=300]", point.toString());
        }

        // ============================================
        // Tests for ByteLongPoint
        // ============================================

        @Test
        public void test_ByteLongPoint_of() {
            ByteLongPoint point = ByteLongPoint.of((byte) 10, (byte) 20, 3000L);

            assertNotNull(point);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(3000L, point.value());
        }

        @Test
        public void test_ByteLongPoint_hashCode() {
            ByteLongPoint point1 = ByteLongPoint.of((byte) 1, (byte) 2, 1000L);
            ByteLongPoint point2 = ByteLongPoint.of((byte) 1, (byte) 2, 1000L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_ByteLongPoint_equals() {
            ByteLongPoint point1 = ByteLongPoint.of((byte) 1, (byte) 2, 1000L);
            ByteLongPoint point2 = ByteLongPoint.of((byte) 1, (byte) 2, 1000L);
            ByteLongPoint point3 = ByteLongPoint.of((byte) 1, (byte) 2, 2000L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_ByteLongPoint_toString() {
            ByteLongPoint point = ByteLongPoint.of((byte) 10, (byte) 20, 3000L);

            assertEquals("ByteLongPoint[x=10, y=20, value=3000]", point.toString());
        }

        // ============================================
        // Tests for ByteDoublePoint
        // ============================================

        @Test
        public void test_ByteDoublePoint_of() {
            ByteDoublePoint point = ByteDoublePoint.of((byte) 10, (byte) 20, 3.14);

            assertNotNull(point);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(3.14, point.value(), 0.001);
        }

        @Test
        public void test_ByteDoublePoint_hashCode() {
            ByteDoublePoint point1 = ByteDoublePoint.of((byte) 1, (byte) 2, 3.14);
            ByteDoublePoint point2 = ByteDoublePoint.of((byte) 1, (byte) 2, 3.14);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_ByteDoublePoint_equals() {
            ByteDoublePoint point1 = ByteDoublePoint.of((byte) 1, (byte) 2, 3.14);
            ByteDoublePoint point2 = ByteDoublePoint.of((byte) 1, (byte) 2, 3.14);
            ByteDoublePoint point3 = ByteDoublePoint.of((byte) 1, (byte) 2, 2.71);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_ByteDoublePoint_toString() {
            ByteDoublePoint point = ByteDoublePoint.of((byte) 10, (byte) 20, 3.14);

            String str = point.toString();
            assertTrue(str.contains("10"));
            assertTrue(str.contains("20"));
            assertTrue(str.contains("3.14"));
        }

        // ============================================
        // Tests for ByteObjPoint
        // ============================================

        @Test
        public void test_ByteObjPoint_of() {
            ByteObjPoint<String> point = ByteObjPoint.of((byte) 10, (byte) 20, "test");

            assertNotNull(point);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals("test", point.value());
        }

        @Test
        public void test_ByteObjPoint_hashCode() {
            ByteObjPoint<String> point1 = ByteObjPoint.of((byte) 1, (byte) 2, "test");
            ByteObjPoint<String> point2 = ByteObjPoint.of((byte) 1, (byte) 2, "test");

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_ByteObjPoint_equals() {
            ByteObjPoint<String> point1 = ByteObjPoint.of((byte) 1, (byte) 2, "test");
            ByteObjPoint<String> point2 = ByteObjPoint.of((byte) 1, (byte) 2, "test");
            ByteObjPoint<String> point3 = ByteObjPoint.of((byte) 1, (byte) 2, "other");

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_ByteObjPoint_toString() {
            ByteObjPoint<String> point = ByteObjPoint.of((byte) 10, (byte) 20, "test");

            String str = point.toString();
            assertTrue(str.contains("10"));
            assertTrue(str.contains("20"));
            assertTrue(str.contains("test"));
        }

        // ============================================
        // Tests for IntBytePoint
        // ============================================

        @Test
        public void test_IntBytePoint_of() {
            IntBytePoint point = IntBytePoint.of(100, 200, (byte) 30);

            assertNotNull(point);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(30, point.value());
        }

        @Test
        public void test_IntBytePoint_hashCode() {
            IntBytePoint point1 = IntBytePoint.of(10, 20, (byte) 3);
            IntBytePoint point2 = IntBytePoint.of(10, 20, (byte) 3);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_IntBytePoint_equals() {
            IntBytePoint point1 = IntBytePoint.of(10, 20, (byte) 3);
            IntBytePoint point2 = IntBytePoint.of(10, 20, (byte) 3);
            IntBytePoint point3 = IntBytePoint.of(10, 20, (byte) 4);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_IntBytePoint_toString() {
            IntBytePoint point = IntBytePoint.of(100, 200, (byte) 30);

            assertEquals("IntBytePoint[x=100, y=200, value=30]", point.toString());
        }

        // ============================================
        // Tests for IntIntPoint
        // ============================================

        @Test
        public void test_IntIntPoint_of() {
            IntIntPoint point = IntIntPoint.of(100, 200, 300);

            assertNotNull(point);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(300, point.value());
        }

        @Test
        public void test_IntIntPoint_hashCode() {
            IntIntPoint point1 = IntIntPoint.of(10, 20, 30);
            IntIntPoint point2 = IntIntPoint.of(10, 20, 30);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_IntIntPoint_equals() {
            IntIntPoint point1 = IntIntPoint.of(10, 20, 30);
            IntIntPoint point2 = IntIntPoint.of(10, 20, 30);
            IntIntPoint point3 = IntIntPoint.of(10, 20, 40);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_IntIntPoint_toString() {
            IntIntPoint point = IntIntPoint.of(100, 200, 300);

            assertEquals("IntIntPoint[x=100, y=200, value=300]", point.toString());
        }

        // ============================================
        // Tests for IntLongPoint
        // ============================================

        @Test
        public void test_IntLongPoint_of() {
            IntLongPoint point = IntLongPoint.of(100, 200, 3000L);

            assertNotNull(point);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(3000L, point.value());
        }

        @Test
        public void test_IntLongPoint_hashCode() {
            IntLongPoint point1 = IntLongPoint.of(10, 20, 300L);
            IntLongPoint point2 = IntLongPoint.of(10, 20, 300L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_IntLongPoint_equals() {
            IntLongPoint point1 = IntLongPoint.of(10, 20, 300L);
            IntLongPoint point2 = IntLongPoint.of(10, 20, 300L);
            IntLongPoint point3 = IntLongPoint.of(10, 20, 400L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_IntLongPoint_toString() {
            IntLongPoint point = IntLongPoint.of(100, 200, 3000L);

            assertEquals("IntLongPoint[x=100, y=200, value=3000]", point.toString());
        }

        // ============================================
        // Tests for IntDoublePoint
        // ============================================

        @Test
        public void test_IntDoublePoint_of() {
            IntDoublePoint point = IntDoublePoint.of(100, 200, 3.14);

            assertNotNull(point);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(3.14, point.value(), 0.001);
        }

        @Test
        public void test_IntDoublePoint_hashCode() {
            IntDoublePoint point1 = IntDoublePoint.of(10, 20, 3.14);
            IntDoublePoint point2 = IntDoublePoint.of(10, 20, 3.14);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_IntDoublePoint_equals() {
            IntDoublePoint point1 = IntDoublePoint.of(10, 20, 3.14);
            IntDoublePoint point2 = IntDoublePoint.of(10, 20, 3.14);
            IntDoublePoint point3 = IntDoublePoint.of(10, 20, 2.71);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_IntDoublePoint_toString() {
            IntDoublePoint point = IntDoublePoint.of(100, 200, 3.14);

            String str = point.toString();
            assertTrue(str.contains("100"));
            assertTrue(str.contains("200"));
            assertTrue(str.contains("3.14"));
        }

        // ============================================
        // Tests for IntObjPoint
        // ============================================

        @Test
        public void test_IntObjPoint_of() {
            IntObjPoint<String> point = IntObjPoint.of(100, 200, "test");

            assertNotNull(point);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals("test", point.value());
        }

        @Test
        public void test_IntObjPoint_hashCode() {
            IntObjPoint<String> point1 = IntObjPoint.of(10, 20, "test");
            IntObjPoint<String> point2 = IntObjPoint.of(10, 20, "test");

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_IntObjPoint_equals() {
            IntObjPoint<String> point1 = IntObjPoint.of(10, 20, "test");
            IntObjPoint<String> point2 = IntObjPoint.of(10, 20, "test");
            IntObjPoint<String> point3 = IntObjPoint.of(10, 20, "other");

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_IntObjPoint_toString() {
            IntObjPoint<String> point = IntObjPoint.of(100, 200, "test");

            String str = point.toString();
            assertTrue(str.contains("100"));
            assertTrue(str.contains("200"));
            assertTrue(str.contains("test"));
        }

        // ============================================
        // Tests for LongBytePoint
        // ============================================

        @Test
        public void test_LongBytePoint_of() {
            LongBytePoint point = LongBytePoint.of(1000L, 2000L, (byte) 30);

            assertNotNull(point);
            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(30, point.value());
        }

        @Test
        public void test_LongBytePoint_hashCode() {
            LongBytePoint point1 = LongBytePoint.of(100L, 200L, (byte) 3);
            LongBytePoint point2 = LongBytePoint.of(100L, 200L, (byte) 3);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_LongBytePoint_equals() {
            LongBytePoint point1 = LongBytePoint.of(100L, 200L, (byte) 3);
            LongBytePoint point2 = LongBytePoint.of(100L, 200L, (byte) 3);
            LongBytePoint point3 = LongBytePoint.of(100L, 200L, (byte) 4);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_LongBytePoint_toString() {
            LongBytePoint point = LongBytePoint.of(1000L, 2000L, (byte) 30);

            assertEquals("LongBytePoint[x=1000, y=2000, value=30]", point.toString());
        }

        // ============================================
        // Tests for LongIntPoint
        // ============================================

        @Test
        public void test_LongIntPoint_of() {
            LongIntPoint point = LongIntPoint.of(1000L, 2000L, 300);

            assertNotNull(point);
            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(300, point.value());
        }

        @Test
        public void test_LongIntPoint_hashCode() {
            LongIntPoint point1 = LongIntPoint.of(100L, 200L, 30);
            LongIntPoint point2 = LongIntPoint.of(100L, 200L, 30);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_LongIntPoint_equals() {
            LongIntPoint point1 = LongIntPoint.of(100L, 200L, 30);
            LongIntPoint point2 = LongIntPoint.of(100L, 200L, 30);
            LongIntPoint point3 = LongIntPoint.of(100L, 200L, 40);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_LongIntPoint_toString() {
            LongIntPoint point = LongIntPoint.of(1000L, 2000L, 300);

            assertEquals("LongIntPoint[x=1000, y=2000, value=300]", point.toString());
        }

        // ============================================
        // Tests for LongLongPoint
        // ============================================

        @Test
        public void test_LongLongPoint_of() {
            LongLongPoint point = LongLongPoint.of(1000L, 2000L, 3000L);

            assertNotNull(point);
            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(3000L, point.value());
        }

        @Test
        public void test_LongLongPoint_hashCode() {
            LongLongPoint point1 = LongLongPoint.of(100L, 200L, 300L);
            LongLongPoint point2 = LongLongPoint.of(100L, 200L, 300L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_LongLongPoint_equals() {
            LongLongPoint point1 = LongLongPoint.of(100L, 200L, 300L);
            LongLongPoint point2 = LongLongPoint.of(100L, 200L, 300L);
            LongLongPoint point3 = LongLongPoint.of(100L, 200L, 400L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_LongLongPoint_toString() {
            LongLongPoint point = LongLongPoint.of(1000L, 2000L, 3000L);

            assertEquals("LongLongPoint[x=1000, y=2000, value=3000]", point.toString());
        }

        // ============================================
        // Tests for LongDoublePoint
        // ============================================

        @Test
        public void test_LongDoublePoint_of() {
            LongDoublePoint point = LongDoublePoint.of(1000L, 2000L, 3.14);

            assertNotNull(point);
            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(3.14, point.value(), 0.001);
        }

        @Test
        public void test_LongDoublePoint_hashCode() {
            LongDoublePoint point1 = LongDoublePoint.of(100L, 200L, 3.14);
            LongDoublePoint point2 = LongDoublePoint.of(100L, 200L, 3.14);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_LongDoublePoint_equals() {
            LongDoublePoint point1 = LongDoublePoint.of(100L, 200L, 3.14);
            LongDoublePoint point2 = LongDoublePoint.of(100L, 200L, 3.14);
            LongDoublePoint point3 = LongDoublePoint.of(100L, 200L, 2.71);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_LongDoublePoint_toString() {
            LongDoublePoint point = LongDoublePoint.of(1000L, 2000L, 3.14);

            String str = point.toString();
            assertTrue(str.contains("1000"));
            assertTrue(str.contains("2000"));
            assertTrue(str.contains("3.14"));
        }

        // ============================================
        // Tests for LongObjPoint
        // ============================================

        @Test
        public void test_LongObjPoint_of() {
            LongObjPoint<String> point = LongObjPoint.of(1000L, 2000L, "test");

            assertNotNull(point);
            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals("test", point.value());
        }

        @Test
        public void test_LongObjPoint_hashCode() {
            LongObjPoint<String> point1 = LongObjPoint.of(100L, 200L, "test");
            LongObjPoint<String> point2 = LongObjPoint.of(100L, 200L, "test");

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_LongObjPoint_equals() {
            LongObjPoint<String> point1 = LongObjPoint.of(100L, 200L, "test");
            LongObjPoint<String> point2 = LongObjPoint.of(100L, 200L, "test");
            LongObjPoint<String> point3 = LongObjPoint.of(100L, 200L, "other");

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_LongObjPoint_toString() {
            LongObjPoint<String> point = LongObjPoint.of(1000L, 2000L, "test");

            String str = point.toString();
            assertTrue(str.contains("1000"));
            assertTrue(str.contains("2000"));
            assertTrue(str.contains("test"));
        }

        // ============================================
        // Tests for DoubleBytePoint
        // ============================================

        @Test
        public void test_DoubleBytePoint_of() {
            DoubleBytePoint point = DoubleBytePoint.of(10.5, 20.5, (byte) 30);

            assertNotNull(point);
            assertEquals(10.5, point.x(), 0.001);
            assertEquals(20.5, point.y(), 0.001);
            assertEquals(30, point.value());
        }

        @Test
        public void test_DoubleBytePoint_hashCode() {
            DoubleBytePoint point1 = DoubleBytePoint.of(10.5, 20.5, (byte) 3);
            DoubleBytePoint point2 = DoubleBytePoint.of(10.5, 20.5, (byte) 3);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_DoubleBytePoint_equals() {
            DoubleBytePoint point1 = DoubleBytePoint.of(10.5, 20.5, (byte) 3);
            DoubleBytePoint point2 = DoubleBytePoint.of(10.5, 20.5, (byte) 3);
            DoubleBytePoint point3 = DoubleBytePoint.of(10.5, 20.5, (byte) 4);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_DoubleBytePoint_toString() {
            DoubleBytePoint point = DoubleBytePoint.of(10.5, 20.5, (byte) 30);

            String str = point.toString();
            assertTrue(str.contains("10.5"));
            assertTrue(str.contains("20.5"));
            assertTrue(str.contains("30"));
        }

        // ============================================
        // Tests for DoubleIntPoint
        // ============================================

        @Test
        public void test_DoubleIntPoint_of() {
            DoubleIntPoint point = DoubleIntPoint.of(10.5, 20.5, 300);

            assertNotNull(point);
            assertEquals(10.5, point.x(), 0.001);
            assertEquals(20.5, point.y(), 0.001);
            assertEquals(300, point.value());
        }

        @Test
        public void test_DoubleIntPoint_hashCode() {
            DoubleIntPoint point1 = DoubleIntPoint.of(10.5, 20.5, 30);
            DoubleIntPoint point2 = DoubleIntPoint.of(10.5, 20.5, 30);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_DoubleIntPoint_equals() {
            DoubleIntPoint point1 = DoubleIntPoint.of(10.5, 20.5, 30);
            DoubleIntPoint point2 = DoubleIntPoint.of(10.5, 20.5, 30);
            DoubleIntPoint point3 = DoubleIntPoint.of(10.5, 20.5, 40);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_DoubleIntPoint_toString() {
            DoubleIntPoint point = DoubleIntPoint.of(10.5, 20.5, 300);

            String str = point.toString();
            assertTrue(str.contains("10.5"));
            assertTrue(str.contains("20.5"));
            assertTrue(str.contains("300"));
        }

        // ============================================
        // Tests for DoubleLongPoint
        // ============================================

        @Test
        public void test_DoubleLongPoint_of() {
            DoubleLongPoint point = DoubleLongPoint.of(10.5, 20.5, 3000L);

            assertNotNull(point);
            assertEquals(10.5, point.x(), 0.001);
            assertEquals(20.5, point.y(), 0.001);
            assertEquals(3000L, point.value());
        }

        @Test
        public void test_DoubleLongPoint_hashCode() {
            DoubleLongPoint point1 = DoubleLongPoint.of(10.5, 20.5, 300L);
            DoubleLongPoint point2 = DoubleLongPoint.of(10.5, 20.5, 300L);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_DoubleLongPoint_equals() {
            DoubleLongPoint point1 = DoubleLongPoint.of(10.5, 20.5, 300L);
            DoubleLongPoint point2 = DoubleLongPoint.of(10.5, 20.5, 300L);
            DoubleLongPoint point3 = DoubleLongPoint.of(10.5, 20.5, 400L);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_DoubleLongPoint_toString() {
            DoubleLongPoint point = DoubleLongPoint.of(10.5, 20.5, 3000L);

            String str = point.toString();
            assertTrue(str.contains("10.5"));
            assertTrue(str.contains("20.5"));
            assertTrue(str.contains("3000"));
        }

        // ============================================
        // Tests for DoubleDoublePoint
        // ============================================

        @Test
        public void test_DoubleDoublePoint_of() {
            DoubleDoublePoint point = DoubleDoublePoint.of(10.5, 20.5, 30.5);

            assertNotNull(point);
            assertEquals(10.5, point.x(), 0.001);
            assertEquals(20.5, point.y(), 0.001);
            assertEquals(30.5, point.value(), 0.001);
        }

        @Test
        public void test_DoubleDoublePoint_hashCode() {
            DoubleDoublePoint point1 = DoubleDoublePoint.of(10.5, 20.5, 30.5);
            DoubleDoublePoint point2 = DoubleDoublePoint.of(10.5, 20.5, 30.5);

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_DoubleDoublePoint_equals() {
            DoubleDoublePoint point1 = DoubleDoublePoint.of(10.5, 20.5, 30.5);
            DoubleDoublePoint point2 = DoubleDoublePoint.of(10.5, 20.5, 30.5);
            DoubleDoublePoint point3 = DoubleDoublePoint.of(10.5, 20.5, 40.5);

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_DoubleDoublePoint_toString() {
            DoubleDoublePoint point = DoubleDoublePoint.of(10.5, 20.5, 30.5);

            String str = point.toString();
            assertTrue(str.contains("10.5"));
            assertTrue(str.contains("20.5"));
            assertTrue(str.contains("30.5"));
        }

        // ============================================
        // Tests for DoubleObjPoint
        // ============================================

        @Test
        public void test_DoubleObjPoint_of() {
            DoubleObjPoint<String> point = DoubleObjPoint.of(10.5, 20.5, "test");

            assertNotNull(point);
            assertEquals(10.5, point.x(), 0.001);
            assertEquals(20.5, point.y(), 0.001);
            assertEquals("test", point.value());
        }

        @Test
        public void test_DoubleObjPoint_hashCode() {
            DoubleObjPoint<String> point1 = DoubleObjPoint.of(10.5, 20.5, "test");
            DoubleObjPoint<String> point2 = DoubleObjPoint.of(10.5, 20.5, "test");

            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_DoubleObjPoint_equals() {
            DoubleObjPoint<String> point1 = DoubleObjPoint.of(10.5, 20.5, "test");
            DoubleObjPoint<String> point2 = DoubleObjPoint.of(10.5, 20.5, "test");
            DoubleObjPoint<String> point3 = DoubleObjPoint.of(10.5, 20.5, "other");

            assertTrue(point1.equals(point2));
            assertFalse(point1.equals(point3));
        }

        @Test
        public void test_DoubleObjPoint_toString() {
            DoubleObjPoint<String> point = DoubleObjPoint.of(10.5, 20.5, "test");

            String str = point.toString();
            assertTrue(str.contains("10.5"));
            assertTrue(str.contains("20.5"));
            assertTrue(str.contains("test"));
        }

        // ============================================
        // Edge case and integration tests
        // ============================================

        @Test
        public void test_edgeCase_zeroValues() {
            IntIntPoint point = IntIntPoint.of(0, 0, 0);

            assertEquals(0, point.x());
            assertEquals(0, point.y());
            assertEquals(0, point.value());
            assertEquals("IntIntPoint[x=0, y=0, value=0]", point.toString());
        }

        @Test
        public void test_edgeCase_negativeValues() {
            IntIntPoint point = IntIntPoint.of(-10, -20, -30);

            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(-30, point.value());
        }

        @Test
        public void test_edgeCase_maxValues() {
            IntIntPoint point = IntIntPoint.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

            assertEquals(Integer.MAX_VALUE, point.x());
            assertEquals(Integer.MAX_VALUE, point.y());
            assertEquals(Integer.MAX_VALUE, point.value());
        }

        @Test
        public void test_edgeCase_minValues() {
            IntIntPoint point = IntIntPoint.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

            assertEquals(Integer.MIN_VALUE, point.x());
            assertEquals(Integer.MIN_VALUE, point.y());
            assertEquals(Integer.MIN_VALUE, point.value());
        }

        @Test
        public void test_edgeCase_specialDoubleValues() {
            DoubleDoublePoint point1 = DoubleDoublePoint.of(Double.MAX_VALUE, Double.MIN_VALUE, 0.0);
            DoubleDoublePoint point2 = DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN);

            assertNotNull(point1);
            assertNotNull(point2);
        }

        @Test
        public void test_integration_differentTypesNotEqual() {
            IntIntPoint intPoint = IntIntPoint.of(10, 20, 30);
            LongLongPoint longPoint = LongLongPoint.of(10L, 20L, 30L);

            assertFalse(intPoint.equals(longPoint));
        }

        @Test
        public void test_integration_equalsAndHashCodeContract() {
            IntIntPoint point1 = IntIntPoint.of(10, 20, 30);
            IntIntPoint point2 = IntIntPoint.of(10, 20, 30);

            assertTrue(point1.equals(point2));
            assertEquals(point1.hashCode(), point2.hashCode());
        }

        @Test
        public void test_integration_genericTypeWithNull() {
            IntObjPoint<String> point = IntObjPoint.of(10, 20, null);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(null, point.value());
        }

        @Test
        public void test_integration_equalsReflexive() {
            IntIntPoint point = IntIntPoint.of(10, 20, 30);

            assertTrue(point.equals(point));
        }

        @Test
        public void test_integration_equalsSymmetric() {
            IntIntPoint point1 = IntIntPoint.of(10, 20, 30);
            IntIntPoint point2 = IntIntPoint.of(10, 20, 30);

            assertTrue(point1.equals(point2));
            assertTrue(point2.equals(point1));
        }

        @Test
        public void test_integration_equalsTransitive() {
            IntIntPoint point1 = IntIntPoint.of(10, 20, 30);
            IntIntPoint point2 = IntIntPoint.of(10, 20, 30);
            IntIntPoint point3 = IntIntPoint.of(10, 20, 30);

            assertTrue(point1.equals(point2));
            assertTrue(point2.equals(point3));
            assertTrue(point1.equals(point3));
        }

        @Test
        public void test_integration_equalsConsistent() {
            IntIntPoint point1 = IntIntPoint.of(10, 20, 30);
            IntIntPoint point2 = IntIntPoint.of(10, 20, 30);

            boolean result1 = point1.equals(point2);
            boolean result2 = point1.equals(point2);

            assertEquals(result1, result2);
        }

        @Test
        public void test_integration_hashCodeDifferentForDifferentPoints() {
            IntIntPoint point1 = IntIntPoint.of(10, 20, 30);
            IntIntPoint point2 = IntIntPoint.of(11, 20, 30);
            IntIntPoint point3 = IntIntPoint.of(10, 21, 30);
            IntIntPoint point4 = IntIntPoint.of(10, 20, 31);

            assertNotEquals(point1.hashCode(), point2.hashCode());
            assertNotEquals(point1.hashCode(), point3.hashCode());
            assertNotEquals(point1.hashCode(), point4.hashCode());
        }
    }

}
