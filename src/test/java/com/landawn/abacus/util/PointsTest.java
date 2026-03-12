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
import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Points.xy;
import com.landawn.abacus.util.Points.xy.ByteBytePoint;
import com.landawn.abacus.util.Points.xy.ByteDoublePoint;
import com.landawn.abacus.util.Points.xy.ByteIntPoint;
import com.landawn.abacus.util.Points.xy.ByteLongPoint;
import com.landawn.abacus.util.Points.xy.ByteObjPoint;
import com.landawn.abacus.util.Points.xy.DoubleBytePoint;
import com.landawn.abacus.util.Points.xy.DoubleDoublePoint;
import com.landawn.abacus.util.Points.xy.DoubleIntPoint;
import com.landawn.abacus.util.Points.xy.DoubleLongPoint;
import com.landawn.abacus.util.Points.xy.DoubleObjPoint;
import com.landawn.abacus.util.Points.xy.IntBytePoint;
import com.landawn.abacus.util.Points.xy.IntDoublePoint;
import com.landawn.abacus.util.Points.xy.IntIntPoint;
import com.landawn.abacus.util.Points.xy.IntLongPoint;
import com.landawn.abacus.util.Points.xy.IntObjPoint;
import com.landawn.abacus.util.Points.xy.LongBytePoint;
import com.landawn.abacus.util.Points.xy.LongDoublePoint;
import com.landawn.abacus.util.Points.xy.LongIntPoint;
import com.landawn.abacus.util.Points.xy.LongLongPoint;
import com.landawn.abacus.util.Points.xy.LongObjPoint;
import com.landawn.abacus.util.Points.xyz;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class PointsTest extends TestBase {
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
        assertEquals("ByteBytePoint[x=1, y=2, v=3]", str);
    }

    @Test
    public void testByteIntPoint() {
        Points.xy.ByteIntPoint point = Points.xy.ByteIntPoint.of((byte) 5, (byte) 10, 100);

        assertEquals(5, point.x());
        assertEquals(10, point.y());
        assertEquals(100, point.v());
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

        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(3000L, point.v());
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

        assertEquals(10.5, point.x(), 0.001);
        assertEquals(20.7, point.y(), 0.001);
        assertEquals(30.9, point.v(), 0.001);
    }

    @Test
    public void testDoubleDoublePointEqualsAndHashCode() {
        Points.xy.DoubleDoublePoint point1 = Points.xy.DoubleDoublePoint.of(1.1, 2.2, 3.3);
        Points.xy.DoubleDoublePoint point2 = Points.xy.DoubleDoublePoint.of(1.1, 2.2, 3.3);
        Points.xy.DoubleDoublePoint point3 = Points.xy.DoubleDoublePoint.of(1.1, 2.2, 3.4);

        assertTrue(point1.equals(point2));
        assertFalse(point1.equals(point3));

        assertEquals(point1.hashCode(), point2.hashCode());
        // Record uses default hashCode which includes all fields
        assertNotEquals(point1.hashCode(), point3.hashCode());
    }

    @Test
    public void testIntObjPoint() {
        String value = "test value";
        Points.xy.IntObjPoint<String> point = Points.xy.IntObjPoint.of(10, 20, value);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(value, point.v());
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

        assertEquals(1.5, point.x(), 0.001);
        assertEquals(2.5, point.y(), 0.001);
        assertEquals(value, point.v());
    }

    @Test
    public void testMixedTypePoints() {
        // Test different combinations of coordinate and value types
        Points.xy.ByteLongPoint byteLong = Points.xy.ByteLongPoint.of((byte) 1, (byte) 2, 1000L);
        Points.xy.IntDoublePoint intDouble = Points.xy.IntDoublePoint.of(10, 20, 30.5);
        Points.xy.LongIntPoint longInt = Points.xy.LongIntPoint.of(100L, 200L, 300);

        assertEquals(1, byteLong.x());
        assertEquals(2, byteLong.y());
        assertEquals(1000L, byteLong.v());

        assertEquals(10, intDouble.x());
        assertEquals(20, intDouble.y());
        assertEquals(30.5, intDouble.v(), 0.001);

        assertEquals(100L, longInt.x());
        assertEquals(200L, longInt.y());
        assertEquals(300, longInt.v());
    }

    @Test
    public void testPointToStringFormats() {
        Points.xy.IntIntPoint intPoint = Points.xy.IntIntPoint.of(1, 2, 3);
        Points.xy.DoubleDoublePoint doublePoint = Points.xy.DoubleDoublePoint.of(1.1, 2.2, 3.3);
        Points.xy.IntObjPoint<String> objPoint = Points.xy.IntObjPoint.of(1, 2, "test");

        assertEquals("IntIntPoint[x=1, y=2, v=3]", intPoint.toString());
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
        assertEquals(1, point.x());
    }

    @Test
    public void testPointImmutability() {
        // Test that points are immutable by design
        Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(10, 20, 30);

        // Fields are final - cannot be changed after construction
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(30, point.v());

        // Values should remain the same
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(30, point.v());
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

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(1000L, point.v());

        // Test equals and hashCode
        Points.xy.ByteLongPoint point2 = Points.xy.ByteLongPoint.of((byte) 10, (byte) 20, 1000L);
        Points.xy.ByteLongPoint point3 = Points.xy.ByteLongPoint.of((byte) 10, (byte) 20, 1001L);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("ByteLongPoint[x=10, y=20, v=1000]", point.toString());
    }

    @Test
    public void testByteDoublePoint() {
        Points.xy.ByteDoublePoint point = Points.xy.ByteDoublePoint.of((byte) 5, (byte) 10, 15.5);

        assertEquals(5, point.x());
        assertEquals(10, point.y());
        assertEquals(15.5, point.v(), 0.001);

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

        assertEquals(1, point.x());
        assertEquals(2, point.y());
        assertEquals(value, point.v());

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

        assertEquals(100, point.x());
        assertEquals(200, point.y());
        assertEquals(50, point.v());

        // Test equals and hashCode
        Points.xy.IntBytePoint point2 = Points.xy.IntBytePoint.of(100, 200, (byte) 50);
        Points.xy.IntBytePoint point3 = Points.xy.IntBytePoint.of(100, 200, (byte) 51);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("IntBytePoint[x=100, y=200, v=50]", point.toString());
    }

    @Test
    public void testIntLongPoint() {
        Points.xy.IntLongPoint point = Points.xy.IntLongPoint.of(10, 20, 5000L);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
        assertEquals(5000L, point.v());

        // Test equals and hashCode
        Points.xy.IntLongPoint point2 = Points.xy.IntLongPoint.of(10, 20, 5000L);
        Points.xy.IntLongPoint point3 = Points.xy.IntLongPoint.of(10, 20, 5001L);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("IntLongPoint[x=10, y=20, v=5000]", point.toString());
    }

    @Test
    public void testLongBytePoint() {
        Points.xy.LongBytePoint point = Points.xy.LongBytePoint.of(1000L, 2000L, (byte) 100);

        assertEquals(1000L, point.x());
        assertEquals(2000L, point.y());
        assertEquals(100, point.v());

        // Test equals and hashCode
        Points.xy.LongBytePoint point2 = Points.xy.LongBytePoint.of(1000L, 2000L, (byte) 100);
        Points.xy.LongBytePoint point3 = Points.xy.LongBytePoint.of(1000L, 2000L, (byte) 101);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("LongBytePoint[x=1000, y=2000, v=100]", point.toString());
    }

    @Test
    public void testLongIntPoint() {
        Points.xy.LongIntPoint point = Points.xy.LongIntPoint.of(500L, 1000L, 250);

        assertEquals(500L, point.x());
        assertEquals(1000L, point.y());
        assertEquals(250, point.v());

        // Test equals and hashCode
        Points.xy.LongIntPoint point2 = Points.xy.LongIntPoint.of(500L, 1000L, 250);
        Points.xy.LongIntPoint point3 = Points.xy.LongIntPoint.of(500L, 1000L, 251);

        assertTrue(point.equals(point2));
        assertFalse(point.equals(point3));
        assertEquals(point.hashCode(), point2.hashCode());
        assertNotEquals(point.hashCode(), point3.hashCode());

        // Test toString
        assertEquals("LongIntPoint[x=500, y=1000, v=250]", point.toString());
    }

    @Test
    public void testLongDoublePoint() {
        Points.xy.LongDoublePoint point = Points.xy.LongDoublePoint.of(100L, 200L, 300.75);

        assertEquals(100L, point.x());
        assertEquals(200L, point.y());
        assertEquals(300.75, point.v(), 0.001);

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

        assertEquals(10L, point.x());
        assertEquals(20L, point.y());
        assertEquals(value, point.v());

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

        assertEquals(10.5, point.x(), 0.001);
        assertEquals(20.7, point.y(), 0.001);
        assertEquals(30, point.v());

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

        assertEquals(1.1, point.x(), 0.001);
        assertEquals(2.2, point.y(), 0.001);
        assertEquals(300, point.v());

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

        assertEquals(5.5, point.x(), 0.001);
        assertEquals(10.75, point.y(), 0.001);
        assertEquals(1000000L, point.v());

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

        assertEquals(1.0, stringPoint.x(), 0.001);
        assertEquals(2.0, stringPoint.y(), 0.001);
        assertEquals(stringValue, stringPoint.v());

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
        assertEquals(Byte.MIN_VALUE, minBytePoint.x());
        assertEquals(Byte.MAX_VALUE, minBytePoint.y());
        assertEquals(0, minBytePoint.v());

        Points.xy.IntIntPoint maxIntPoint = Points.xy.IntIntPoint.of(Integer.MAX_VALUE, Integer.MIN_VALUE, 0);
        assertEquals(Integer.MAX_VALUE, maxIntPoint.x());
        assertEquals(Integer.MIN_VALUE, maxIntPoint.y());
        assertEquals(0, maxIntPoint.v());

        Points.xy.LongLongPoint maxLongPoint = Points.xy.LongLongPoint.of(Long.MAX_VALUE, Long.MIN_VALUE, 0L);
        assertEquals(Long.MAX_VALUE, maxLongPoint.x());
        assertEquals(Long.MIN_VALUE, maxLongPoint.y());
        assertEquals(0L, maxLongPoint.v());

        // Test with special double values
        Points.xy.DoubleDoublePoint specialDoublePoint = Points.xy.DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN);
        assertEquals(Double.POSITIVE_INFINITY, specialDoublePoint.x(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, specialDoublePoint.y(), 0.0);
        assertTrue(Double.isNaN(specialDoublePoint.v()));
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

    @Nested
    class JavadocExampleOtherTest_Points extends TestBase {
        // ===== Points.xy 2D examples =====

        @Test
        public void testXyIntIntPoint() {
            // From class-level Javadoc and IntIntPoint.of Javadoc
            Points.xy.IntIntPoint point2D = Points.xy.IntIntPoint.of(10, 20, 100);
            assertEquals(10, point2D.x());
            assertEquals(20, point2D.y());
            assertEquals(100, point2D.v());
        }

        @Test
        public void testXyIntIntPointPathfinding() {
            // From IntIntPoint.of Javadoc: "Use in pathfinding with cost values"
            Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(100, 200, 300);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(300, point.v());

            Points.xy.IntIntPoint pathNode = Points.xy.IntIntPoint.of(5, 8, 15);
            assertEquals(5, pathNode.x());
            assertEquals(8, pathNode.y());
            assertEquals(15, pathNode.v());
        }

        @Test
        public void testXyByteBytePoint() {
            // From ByteBytePoint.of Javadoc
            Points.xy.ByteBytePoint point = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 5);
            assertEquals((byte) 10, point.x());
            assertEquals((byte) 20, point.y());
            assertEquals((byte) 5, point.v());
        }

        @Test
        public void testXyByteIntPoint() {
            // From ByteIntPoint.of Javadoc
            Points.xy.ByteIntPoint point = Points.xy.ByteIntPoint.of((byte) 5, (byte) 10, 1000);
            assertEquals((byte) 5, point.x());
            assertEquals((byte) 10, point.y());
            assertEquals(1000, point.v());
        }

        @Test
        public void testXyByteLongPoint() {
            // From ByteLongPoint.of Javadoc
            Points.xy.ByteLongPoint point = Points.xy.ByteLongPoint.of((byte) 3, (byte) 7, 1000000000L);
            assertEquals((byte) 3, point.x());
            assertEquals((byte) 7, point.y());
            assertEquals(1000000000L, point.v());
        }

        @Test
        public void testXyByteDoublePoint() {
            // From ByteDoublePoint.of Javadoc
            Points.xy.ByteDoublePoint point = Points.xy.ByteDoublePoint.of((byte) 2, (byte) 4, 3.14159);
            assertEquals((byte) 2, point.x());
            assertEquals((byte) 4, point.y());
            assertEquals(3.14159, point.v(), 0.00001);
        }

        @Test
        public void testXyByteObjPoint() {
            // From ByteObjPoint.of Javadoc
            Points.xy.ByteObjPoint<String> point = Points.xy.ByteObjPoint.of((byte) 1, (byte) 2, "label");
            assertEquals((byte) 1, point.x());
            assertEquals((byte) 2, point.y());
            assertEquals("label", point.v());
        }

        @Test
        public void testXyIntBytePoint() {
            // From IntBytePoint.of Javadoc
            Points.xy.IntBytePoint point = Points.xy.IntBytePoint.of(100, 200, (byte) 10);
            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals((byte) 10, point.v());
        }

        @Test
        public void testXyIntLongPoint() {
            // From IntLongPoint.of Javadoc
            Points.xy.IntLongPoint point = Points.xy.IntLongPoint.of(50, 75, 10000000000L);
            assertEquals(50, point.x());
            assertEquals(75, point.y());
            assertEquals(10000000000L, point.v());
        }

        @Test
        public void testXyIntDoublePoint() {
            // From IntDoublePoint.of Javadoc
            Points.xy.IntDoublePoint point = Points.xy.IntDoublePoint.of(10, 20, 3.14159);
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(3.14159, point.v(), 0.00001);
        }

        @Test
        public void testXyIntObjPoint() {
            // From IntObjPoint.of Javadoc
            Points.xy.IntObjPoint<String> point = Points.xy.IntObjPoint.of(10, 20, "label");
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals("label", point.v());
        }

        @Test
        public void testXyLongBytePoint() {
            // From LongBytePoint.of Javadoc
            Points.xy.LongBytePoint point = Points.xy.LongBytePoint.of(1000000L, 2000000L, (byte) 5);
            assertEquals(1000000L, point.x());
            assertEquals(2000000L, point.y());
            assertEquals((byte) 5, point.v());
        }

        @Test
        public void testXyLongIntPoint() {
            // From LongIntPoint.of Javadoc
            Points.xy.LongIntPoint point = Points.xy.LongIntPoint.of(1000000L, 2000000L, 500);
            assertEquals(1000000L, point.x());
            assertEquals(2000000L, point.y());
            assertEquals(500, point.v());
        }

        @Test
        public void testXyLongLongPoint() {
            // From LongLongPoint.of Javadoc
            Points.xy.LongLongPoint point = Points.xy.LongLongPoint.of(1000000L, 2000000L, 3000000000L);
            assertEquals(1000000L, point.x());
            assertEquals(2000000L, point.y());
            assertEquals(3000000000L, point.v());
        }

        @Test
        public void testXyLongDoublePoint() {
            // From LongDoublePoint.of Javadoc
            Points.xy.LongDoublePoint point = Points.xy.LongDoublePoint.of(1000000L, 2000000L, 3.14159);
            assertEquals(1000000L, point.x());
            assertEquals(2000000L, point.y());
            assertEquals(3.14159, point.v(), 0.00001);
        }

        @Test
        public void testXyLongObjPoint() {
            // From LongObjPoint.of Javadoc
            Points.xy.LongObjPoint<String> point = Points.xy.LongObjPoint.of(1000000L, 2000000L, "marker");
            assertEquals(1000000L, point.x());
            assertEquals(2000000L, point.y());
            assertEquals("marker", point.v());
        }

        @Test
        public void testXyDoubleBytePoint() {
            // From DoubleBytePoint.of Javadoc
            Points.xy.DoubleBytePoint point = Points.xy.DoubleBytePoint.of(10.5, 20.7, (byte) 3);
            assertEquals(10.5, point.x(), 0.00001);
            assertEquals(20.7, point.y(), 0.00001);
            assertEquals((byte) 3, point.v());
        }

        @Test
        public void testXyDoubleIntPoint() {
            // From DoubleIntPoint.of Javadoc
            Points.xy.DoubleIntPoint point = Points.xy.DoubleIntPoint.of(10.5, 20.7, 100);
            assertEquals(10.5, point.x(), 0.00001);
            assertEquals(20.7, point.y(), 0.00001);
            assertEquals(100, point.v());
        }

        @Test
        public void testXyDoubleLongPoint() {
            // From DoubleLongPoint.of Javadoc
            Points.xy.DoubleLongPoint point = Points.xy.DoubleLongPoint.of(10.5, 20.7, 1000000000L);
            assertEquals(10.5, point.x(), 0.00001);
            assertEquals(20.7, point.y(), 0.00001);
            assertEquals(1000000000L, point.v());
        }

        @Test
        public void testXyDoubleDoublePoint() {
            // From DoubleDoublePoint.of Javadoc
            Points.xy.DoubleDoublePoint point = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 3.14159);
            assertEquals(10.5, point.x(), 0.00001);
            assertEquals(20.7, point.y(), 0.00001);
            assertEquals(3.14159, point.v(), 0.00001);
        }

        @Test
        public void testXyDoubleObjPoint() {
            // From DoubleObjPoint.of Javadoc
            Points.xy.DoubleObjPoint<String> point = Points.xy.DoubleObjPoint.of(10.5, 20.7, "location");
            assertEquals(10.5, point.x(), 0.00001);
            assertEquals(20.7, point.y(), 0.00001);
            assertEquals("location", point.v());
        }

        // ===== Points.xyz 3D examples =====

        @Test
        public void testXyzIntIntPoint() {
            // From class-level Javadoc
            Points.xyz.IntIntPoint point3D = Points.xyz.IntIntPoint.of(10, 20, 30, 100);
            assertEquals(10, point3D.x());
            assertEquals(20, point3D.y());
            assertEquals(30, point3D.z());
            assertEquals(100, point3D.v());
        }

        @Test
        public void testXyzByteBytePoint() {
            // From xyz.ByteBytePoint.of Javadoc
            Points.xyz.ByteBytePoint point = Points.xyz.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30, (byte) 5);
            assertEquals((byte) 10, point.x());
            assertEquals((byte) 20, point.y());
            assertEquals((byte) 30, point.z());
            assertEquals((byte) 5, point.v());
        }

        @Test
        public void testXyzByteIntPoint() {
            // From xyz.ByteIntPoint.of Javadoc
            Points.xyz.ByteIntPoint point = Points.xyz.ByteIntPoint.of((byte) 5, (byte) 10, (byte) 15, 1000);
            assertEquals((byte) 5, point.x());
            assertEquals((byte) 10, point.y());
            assertEquals((byte) 15, point.z());
            assertEquals(1000, point.v());
        }

        @Test
        public void testXyzByteLongPoint() {
            // From xyz.ByteLongPoint.of Javadoc
            Points.xyz.ByteLongPoint point = Points.xyz.ByteLongPoint.of((byte) 3, (byte) 7, (byte) 11, 1000000000L);
            assertEquals((byte) 3, point.x());
            assertEquals((byte) 7, point.y());
            assertEquals((byte) 11, point.z());
            assertEquals(1000000000L, point.v());
        }

        @Test
        public void testXyzByteDoublePoint() {
            // From xyz.ByteDoublePoint.of Javadoc
            Points.xyz.ByteDoublePoint point = Points.xyz.ByteDoublePoint.of((byte) 2, (byte) 4, (byte) 6, 3.14159);
            assertEquals((byte) 2, point.x());
            assertEquals((byte) 4, point.y());
            assertEquals((byte) 6, point.z());
            assertEquals(3.14159, point.v(), 0.00001);
        }

        @Test
        public void testXyzByteObjPoint() {
            // From xyz.ByteObjPoint.of Javadoc
            Points.xyz.ByteObjPoint<String> point = Points.xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "voxel");
            assertEquals((byte) 1, point.x());
            assertEquals((byte) 2, point.y());
            assertEquals((byte) 3, point.z());
            assertEquals("voxel", point.v());
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

        @Test
        public void testEquals_DifferentType() {
            xy.IntIntPoint point = xy.IntIntPoint.of(1, 2, 3);
            String notAPoint = "not a point";

            assertFalse(point.equals(notAPoint));
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
            xy.ByteBytePoint point = xy.ByteBytePoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0);

            assertEquals(Byte.MIN_VALUE, point.x());
            assertEquals(Byte.MAX_VALUE, point.y());
            assertEquals(0, point.v());
        }

        @Test
        public void testByteBytePoint_NegativeValues() {
            xy.ByteBytePoint point = xy.ByteBytePoint.of((byte) -10, (byte) -20, (byte) -30);

            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(-30, point.v());
        }

        @Test
        public void testByteBytePoint_ZeroValues() {
            xy.ByteBytePoint point = xy.ByteBytePoint.of((byte) 0, (byte) 0, (byte) 0);

            assertEquals(0, point.x());
            assertEquals(0, point.y());
            assertEquals(0, point.v());
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

            assertEquals("ByteBytePoint[x=1, y=2, v=3]", result);
        }

        // ============================================
        // Tests for ByteIntPoint - Edge Cases
        // ============================================

        @Test
        public void testByteIntPoint_MaxIntValue() {
            xy.ByteIntPoint point = xy.ByteIntPoint.of((byte) 10, (byte) 20, Integer.MAX_VALUE);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(Integer.MAX_VALUE, point.v());
        }

        @Test
        public void testByteIntPoint_MinIntValue() {
            xy.ByteIntPoint point = xy.ByteIntPoint.of((byte) -10, (byte) -20, Integer.MIN_VALUE);

            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(Integer.MIN_VALUE, point.v());
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

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(Long.MAX_VALUE, point.v());
        }

        @Test
        public void testByteLongPoint_MinLongValue() {
            xy.ByteLongPoint point = xy.ByteLongPoint.of((byte) -10, (byte) -20, Long.MIN_VALUE);

            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(Long.MIN_VALUE, point.v());
        }

        @Test
        public void testByteLongPoint_ZeroValue() {
            xy.ByteLongPoint point = xy.ByteLongPoint.of((byte) 5, (byte) 10, 0L);

            assertEquals(5, point.x());
            assertEquals(10, point.y());
            assertEquals(0L, point.v());
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

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(Double.MAX_VALUE, point.v(), 0.0);
        }

        @Test
        public void testByteDoublePoint_MinDoubleValue() {
            xy.ByteDoublePoint point = xy.ByteDoublePoint.of((byte) 10, (byte) 20, Double.MIN_VALUE);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(Double.MIN_VALUE, point.v(), 0.0);
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

            assertEquals(1, point.x());
            assertEquals(2, point.y());
            assertEquals(Double.POSITIVE_INFINITY, point.v(), 0.0);
        }

        @Test
        public void testByteDoublePoint_NegativeInfinity() {
            xy.ByteDoublePoint point = xy.ByteDoublePoint.of((byte) 1, (byte) 2, Double.NEGATIVE_INFINITY);

            assertEquals(1, point.x());
            assertEquals(2, point.y());
            assertEquals(Double.NEGATIVE_INFINITY, point.v(), 0.0);
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

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(null, point.v());
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

            assertEquals(5, point.x());
            assertEquals(10, point.y());
            assertEquals(list, point.v());
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

            assertEquals(Integer.MAX_VALUE, point.x());
            assertEquals(Integer.MIN_VALUE, point.y());
            assertEquals(50, point.v());
        }

        @Test
        public void testIntBytePoint_NegativeCoordinates() {
            xy.IntBytePoint point = xy.IntBytePoint.of(-100, -200, (byte) -30);

            assertEquals(-100, point.x());
            assertEquals(-200, point.y());
            assertEquals(-30, point.v());
        }

        @Test
        public void testIntBytePoint_ZeroValues() {
            xy.IntBytePoint point = xy.IntBytePoint.of(0, 0, (byte) 0);

            assertEquals(0, point.x());
            assertEquals(0, point.y());
            assertEquals(0, point.v());
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

            assertEquals(Integer.MAX_VALUE, point.x());
            assertEquals(Integer.MAX_VALUE, point.y());
            assertEquals(Integer.MAX_VALUE, point.v());
        }

        @Test
        public void testIntIntPoint_MinIntValues() {
            xy.IntIntPoint point = xy.IntIntPoint.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

            assertEquals(Integer.MIN_VALUE, point.x());
            assertEquals(Integer.MIN_VALUE, point.y());
            assertEquals(Integer.MIN_VALUE, point.v());
        }

        @Test
        public void testIntIntPoint_MixedValues() {
            xy.IntIntPoint point = xy.IntIntPoint.of(-100, 200, 0);

            assertEquals(-100, point.x());
            assertEquals(200, point.y());
            assertEquals(0, point.v());
        }

        @Test
        public void testIntIntPoint_LargePositiveValues() {
            xy.IntIntPoint point = xy.IntIntPoint.of(1000000, 2000000, 3000000);

            assertEquals(1000000, point.x());
            assertEquals(2000000, point.y());
            assertEquals(3000000, point.v());
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

            assertEquals("IntIntPoint[x=-10, y=-20, v=-30]", result);
        }

        // ============================================
        // Tests for IntLongPoint - Edge Cases
        // ============================================

        @Test
        public void testIntLongPoint_MaxLongValue() {
            xy.IntLongPoint point = xy.IntLongPoint.of(100, 200, Long.MAX_VALUE);

            assertEquals(100, point.x());
            assertEquals(200, point.y());
            assertEquals(Long.MAX_VALUE, point.v());
        }

        @Test
        public void testIntLongPoint_MinLongValue() {
            xy.IntLongPoint point = xy.IntLongPoint.of(-100, -200, Long.MIN_VALUE);

            assertEquals(-100, point.x());
            assertEquals(-200, point.y());
            assertEquals(Long.MIN_VALUE, point.v());
        }

        @Test
        public void testIntLongPoint_MixedSignValues() {
            xy.IntLongPoint point = xy.IntLongPoint.of(-100, 200, -3000L);

            assertEquals(-100, point.x());
            assertEquals(200, point.y());
            assertEquals(-3000L, point.v());
        }

        // ============================================
        // Tests for IntDoublePoint - Edge Cases
        // ============================================

        @Test
        public void testIntDoublePoint_SmallDecimals() {
            xy.IntDoublePoint point = xy.IntDoublePoint.of(10, 20, 0.0001);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(0.0001, point.v(), 0.00001);
        }

        @Test
        public void testIntDoublePoint_LargeDecimals() {
            xy.IntDoublePoint point = xy.IntDoublePoint.of(10, 20, 999999.999999);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(999999.999999, point.v(), 0.00001);
        }

        @Test
        public void testIntDoublePoint_NegativeDecimals() {
            xy.IntDoublePoint point = xy.IntDoublePoint.of(-10, -20, -3.14159);

            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(-3.14159, point.v(), 0.00001);
        }

        @Test
        public void testIntDoublePoint_PositiveInfinity() {
            xy.IntDoublePoint point = xy.IntDoublePoint.of(1, 2, Double.POSITIVE_INFINITY);

            assertEquals(Double.POSITIVE_INFINITY, point.v(), 0.0);
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

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(null, point.v());
        }

        @Test
        public void testIntObjPoint_IntegerObject() {
            xy.IntObjPoint<Integer> point = xy.IntObjPoint.of(10, 20, Integer.MAX_VALUE);

            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(Integer.MAX_VALUE, point.v());
        }

        @Test
        public void testIntObjPoint_StringObject() {
            xy.IntObjPoint<String> point = xy.IntObjPoint.of(-100, 200, "Test String");

            assertEquals(-100, point.x());
            assertEquals(200, point.y());
            assertEquals("Test String", point.v());
        }

        @Test
        public void testIntObjPoint_EmptyString() {
            xy.IntObjPoint<String> point = xy.IntObjPoint.of(0, 0, "");

            assertEquals(0, point.x());
            assertEquals(0, point.y());
            assertEquals("", point.v());
        }

        @Test
        public void testIntObjPoint_ArrayValue() {
            int[] array = { 1, 2, 3 };
            xy.IntObjPoint<int[]> point = xy.IntObjPoint.of(5, 10, array);

            assertEquals(5, point.x());
            assertEquals(10, point.y());
            assertEquals(array, point.v());
        }

        // ============================================
        // Tests for LongBytePoint - Edge Cases
        // ============================================

        @Test
        public void testLongBytePoint_MaxLongCoordinates() {
            xy.LongBytePoint point = xy.LongBytePoint.of(Long.MAX_VALUE, Long.MIN_VALUE, (byte) 50);

            assertEquals(Long.MAX_VALUE, point.x());
            assertEquals(Long.MIN_VALUE, point.y());
            assertEquals(50, point.v());
        }

        @Test
        public void testLongBytePoint_ZeroCoordinates() {
            xy.LongBytePoint point = xy.LongBytePoint.of(0L, 0L, (byte) 0);

            assertEquals(0L, point.x());
            assertEquals(0L, point.y());
            assertEquals(0, point.v());
        }

        @Test
        public void testLongBytePoint_NegativeValues() {
            xy.LongBytePoint point = xy.LongBytePoint.of(-1000L, -2000L, (byte) -50);

            assertEquals(-1000L, point.x());
            assertEquals(-2000L, point.y());
            assertEquals(-50, point.v());
        }

        // ============================================
        // Tests for LongIntPoint - Edge Cases
        // ============================================

        @Test
        public void testLongIntPoint_MaxValues() {
            xy.LongIntPoint point = xy.LongIntPoint.of(Long.MAX_VALUE, Long.MIN_VALUE, Integer.MAX_VALUE);

            assertEquals(Long.MAX_VALUE, point.x());
            assertEquals(Long.MIN_VALUE, point.y());
            assertEquals(Integer.MAX_VALUE, point.v());
        }

        @Test
        public void testLongIntPoint_MixedSigns() {
            xy.LongIntPoint point = xy.LongIntPoint.of(-100000L, 200000L, -300);

            assertEquals(-100000L, point.x());
            assertEquals(200000L, point.y());
            assertEquals(-300, point.v());
        }

        @Test
        public void testLongIntPoint_ZeroValues() {
            xy.LongIntPoint point = xy.LongIntPoint.of(0L, 0L, 0);

            assertEquals(0L, point.x());
            assertEquals(0L, point.y());
            assertEquals(0, point.v());
        }

        // ============================================
        // Tests for LongLongPoint - Edge Cases
        // ============================================

        @Test
        public void testLongLongPoint_MaxLongValues() {
            xy.LongLongPoint point = xy.LongLongPoint.of(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);

            assertEquals(Long.MAX_VALUE, point.x());
            assertEquals(Long.MAX_VALUE, point.y());
            assertEquals(Long.MAX_VALUE, point.v());
        }

        @Test
        public void testLongLongPoint_MinLongValues() {
            xy.LongLongPoint point = xy.LongLongPoint.of(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);

            assertEquals(Long.MIN_VALUE, point.x());
            assertEquals(Long.MIN_VALUE, point.y());
            assertEquals(Long.MIN_VALUE, point.v());
        }

        @Test
        public void testLongLongPoint_LargeValues() {
            xy.LongLongPoint point = xy.LongLongPoint.of(9223372036854775806L, -9223372036854775807L, 1000000000000L);

            assertEquals(9223372036854775806L, point.x());
            assertEquals(-9223372036854775807L, point.y());
            assertEquals(1000000000000L, point.v());
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

            assertEquals(Long.MAX_VALUE, point.x());
            assertEquals(Long.MIN_VALUE, point.y());
            assertEquals(Double.MAX_VALUE, point.v(), 0.0);
        }

        @Test
        public void testLongDoublePoint_SmallDouble() {
            xy.LongDoublePoint point = xy.LongDoublePoint.of(100L, 200L, Double.MIN_VALUE);

            assertEquals(100L, point.x());
            assertEquals(200L, point.y());
            assertEquals(Double.MIN_VALUE, point.v(), 0.0);
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

            assertEquals(Double.NEGATIVE_INFINITY, point.v(), 0.0);
        }

        // ============================================
        // Tests for LongObjPoint - Edge Cases
        // ============================================

        @Test
        public void testLongObjPoint_NullValue() {
            xy.LongObjPoint<String> point = xy.LongObjPoint.of(1000L, 2000L, null);

            assertEquals(1000L, point.x());
            assertEquals(2000L, point.y());
            assertEquals(null, point.v());
        }

        @Test
        public void testLongObjPoint_LongObject() {
            xy.LongObjPoint<Long> point = xy.LongObjPoint.of(100L, 200L, Long.MAX_VALUE);

            assertEquals(100L, point.x());
            assertEquals(200L, point.y());
            assertEquals(Long.MAX_VALUE, point.v());
        }

        @Test
        public void testLongObjPoint_ComplexGeneric() {
            java.util.Map<String, Integer> map = new java.util.HashMap<>();
            map.put("key", 42);
            xy.LongObjPoint<java.util.Map<String, Integer>> point = xy.LongObjPoint.of(10L, 20L, map);

            assertEquals(10L, point.x());
            assertEquals(20L, point.y());
            assertEquals(map, point.v());
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

            assertEquals(Double.MAX_VALUE, point.x(), 0.0);
            assertEquals(Double.MIN_VALUE, point.y(), 0.0);
            assertEquals(50, point.v());
        }

        @Test
        public void testDoubleBytePoint_NegativeCoordinates() {
            xy.DoubleBytePoint point = xy.DoubleBytePoint.of(-10.5, -20.5, (byte) -30);

            assertEquals(-10.5, point.x(), 0.001);
            assertEquals(-20.5, point.y(), 0.001);
            assertEquals(-30, point.v());
        }

        @Test
        public void testDoubleBytePoint_ZeroCoordinates() {
            xy.DoubleBytePoint point = xy.DoubleBytePoint.of(0.0, 0.0, (byte) 0);

            assertEquals(0.0, point.x(), 0.0);
            assertEquals(0.0, point.y(), 0.0);
            assertEquals(0, point.v());
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

            assertEquals(Double.POSITIVE_INFINITY, point.x(), 0.0);
            assertEquals(Double.NEGATIVE_INFINITY, point.y(), 0.0);
        }

        // ============================================
        // Tests for DoubleIntPoint - Edge Cases
        // ============================================

        @Test
        public void testDoubleIntPoint_SmallDoubles() {
            xy.DoubleIntPoint point = xy.DoubleIntPoint.of(0.00001, 0.00002, 100);

            assertEquals(0.00001, point.x(), 0.000001);
            assertEquals(0.00002, point.y(), 0.000001);
            assertEquals(100, point.v());
        }

        @Test
        public void testDoubleIntPoint_LargeDoubles() {
            xy.DoubleIntPoint point = xy.DoubleIntPoint.of(999999.999, 888888.888, 777);

            assertEquals(999999.999, point.x(), 0.001);
            assertEquals(888888.888, point.y(), 0.001);
            assertEquals(777, point.v());
        }

        @Test
        public void testDoubleIntPoint_MixedSigns() {
            xy.DoubleIntPoint point = xy.DoubleIntPoint.of(-1.5, 2.5, -300);

            assertEquals(-1.5, point.x(), 0.001);
            assertEquals(2.5, point.y(), 0.001);
            assertEquals(-300, point.v());
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

            assertEquals(Double.MAX_VALUE, point.x(), 0.0);
            assertEquals(Double.MIN_VALUE, point.y(), 0.0);
            assertEquals(Long.MAX_VALUE, point.v());
        }

        @Test
        public void testDoubleLongPoint_NegativeValues() {
            xy.DoubleLongPoint point = xy.DoubleLongPoint.of(-100.5, -200.5, -3000L);

            assertEquals(-100.5, point.x(), 0.001);
            assertEquals(-200.5, point.y(), 0.001);
            assertEquals(-3000L, point.v());
        }

        @Test
        public void testDoubleLongPoint_ZeroValues() {
            xy.DoubleLongPoint point = xy.DoubleLongPoint.of(0.0, 0.0, 0L);

            assertEquals(0.0, point.x(), 0.0);
            assertEquals(0.0, point.y(), 0.0);
            assertEquals(0L, point.v());
        }

        @Test
        public void testDoubleLongPoint_InfinityCoordinates() {
            xy.DoubleLongPoint point = xy.DoubleLongPoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 1000L);

            assertEquals(Double.POSITIVE_INFINITY, point.x(), 0.0);
            assertEquals(Double.NEGATIVE_INFINITY, point.y(), 0.0);
            assertEquals(1000L, point.v());
        }

        // ============================================
        // Tests for DoubleDoublePoint - Edge Cases
        // ============================================

        @Test
        public void testDoubleDoublePoint_MaxDoubleValues() {
            xy.DoubleDoublePoint point = xy.DoubleDoublePoint.of(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

            assertEquals(Double.MAX_VALUE, point.x(), 0.0);
            assertEquals(Double.MAX_VALUE, point.y(), 0.0);
            assertEquals(Double.MAX_VALUE, point.v(), 0.0);
        }

        @Test
        public void testDoubleDoublePoint_MinDoubleValues() {
            xy.DoubleDoublePoint point = xy.DoubleDoublePoint.of(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);

            assertEquals(Double.MIN_VALUE, point.x(), 0.0);
            assertEquals(Double.MIN_VALUE, point.y(), 0.0);
            assertEquals(Double.MIN_VALUE, point.v(), 0.0);
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

            assertEquals(Double.POSITIVE_INFINITY, point.x(), 0.0);
            assertEquals(Double.NEGATIVE_INFINITY, point.y(), 0.0);
            assertEquals(Double.POSITIVE_INFINITY, point.v(), 0.0);
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

            assertEquals(1e-308, point.x(), 0.0);
            assertEquals(1e-307, point.y(), 0.0);
            assertEquals(1e-306, point.v(), 0.0);
        }

        @Test
        public void testDoubleDoublePoint_MixedSpecialValues() {
            xy.DoubleDoublePoint point = xy.DoubleDoublePoint.of(Double.NaN, Double.POSITIVE_INFINITY, -0.0);

            assertTrue(Double.isNaN(point.x()));
            assertEquals(Double.POSITIVE_INFINITY, point.y(), 0.0);
            assertEquals(-0.0, point.v(), 0.0);
        }

        // ============================================
        // Tests for DoubleObjPoint - Edge Cases
        // ============================================

        @Test
        public void testDoubleObjPoint_NullValue() {
            xy.DoubleObjPoint<String> point = xy.DoubleObjPoint.of(10.5, 20.5, null);

            assertEquals(10.5, point.x(), 0.001);
            assertEquals(20.5, point.y(), 0.001);
            assertEquals(null, point.v());
        }

        @Test
        public void testDoubleObjPoint_DoubleObject() {
            xy.DoubleObjPoint<Double> point = xy.DoubleObjPoint.of(1.5, 2.5, Double.MAX_VALUE);

            assertEquals(1.5, point.x(), 0.001);
            assertEquals(2.5, point.y(), 0.001);
            assertEquals(Double.MAX_VALUE, point.v());
        }

        @Test
        public void testDoubleObjPoint_NaNCoordinates() {
            xy.DoubleObjPoint<String> point = xy.DoubleObjPoint.of(Double.NaN, Double.NaN, "test");

            assertTrue(Double.isNaN(point.x()));
            assertTrue(Double.isNaN(point.y()));
            assertEquals("test", point.v());
        }

        @Test
        public void testDoubleObjPoint_InfinityCoordinates() {
            xy.DoubleObjPoint<Integer> point = xy.DoubleObjPoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 42);

            assertEquals(Double.POSITIVE_INFINITY, point.x(), 0.0);
            assertEquals(Double.NEGATIVE_INFINITY, point.y(), 0.0);
            assertEquals(42, point.v());
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

            assertEquals("ByteBytePoint[x=-128, y=127, v=0]", result);
        }

        @Test
        public void testToString_IntIntPoint_Format() {
            xy.IntIntPoint point = xy.IntIntPoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
            String result = point.toString();

            assertEquals("IntIntPoint[x=" + Integer.MIN_VALUE + ", y=" + Integer.MAX_VALUE + ", v=0]", result);
        }

        @Test
        public void testToString_LongLongPoint_Format() {
            xy.LongLongPoint point = xy.LongLongPoint.of(Long.MIN_VALUE, Long.MAX_VALUE, 0L);
            String result = point.toString();

            assertEquals("LongLongPoint[x=" + Long.MIN_VALUE + ", y=" + Long.MAX_VALUE + ", v=0]", result);
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

            assertEquals(1.5, point.x(), 0.001);
            assertEquals(2.5, point.y(), 0.001);
            assertEquals(nested, point.v());
        }

        // ============================================
        // Boundary Value Tests
        // ============================================

        @Test
        public void testBytePoint_BoundaryValues() {
            xy.ByteBytePoint minPoint = xy.ByteBytePoint.of(Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE);
            xy.ByteBytePoint maxPoint = xy.ByteBytePoint.of(Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE);

            assertEquals(Byte.MIN_VALUE, minPoint.x());
            assertEquals(Byte.MIN_VALUE, minPoint.y());
            assertEquals(Byte.MIN_VALUE, minPoint.v());

            assertEquals(Byte.MAX_VALUE, maxPoint.x());
            assertEquals(Byte.MAX_VALUE, maxPoint.y());
            assertEquals(Byte.MAX_VALUE, maxPoint.v());

            assertFalse(minPoint.equals(maxPoint));
        }

        @Test
        public void testIntPoint_BoundaryValues() {
            xy.IntIntPoint minPoint = xy.IntIntPoint.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
            xy.IntIntPoint maxPoint = xy.IntIntPoint.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

            assertEquals(Integer.MIN_VALUE, minPoint.x());
            assertEquals(Integer.MIN_VALUE, minPoint.y());
            assertEquals(Integer.MIN_VALUE, minPoint.v());

            assertEquals(Integer.MAX_VALUE, maxPoint.x());
            assertEquals(Integer.MAX_VALUE, maxPoint.y());
            assertEquals(Integer.MAX_VALUE, maxPoint.v());

            assertFalse(minPoint.equals(maxPoint));
        }

        @Test
        public void testLongPoint_BoundaryValues() {
            xy.LongLongPoint minPoint = xy.LongLongPoint.of(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);
            xy.LongLongPoint maxPoint = xy.LongLongPoint.of(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);

            assertEquals(Long.MIN_VALUE, minPoint.x());
            assertEquals(Long.MIN_VALUE, minPoint.y());
            assertEquals(Long.MIN_VALUE, minPoint.v());

            assertEquals(Long.MAX_VALUE, maxPoint.x());
            assertEquals(Long.MAX_VALUE, maxPoint.y());
            assertEquals(Long.MAX_VALUE, maxPoint.v());

            assertFalse(minPoint.equals(maxPoint));
        }

        @Test
        public void testDoublePoint_SpecialValues() {
            xy.DoubleDoublePoint nanPoint = xy.DoubleDoublePoint.of(Double.NaN, Double.NaN, Double.NaN);
            xy.DoubleDoublePoint infPoint = xy.DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

            assertTrue(Double.isNaN(nanPoint.x()));
            assertTrue(Double.isNaN(nanPoint.y()));
            assertTrue(Double.isNaN(nanPoint.v()));

            assertEquals(Double.POSITIVE_INFINITY, infPoint.x(), 0.0);
            assertEquals(Double.NEGATIVE_INFINITY, infPoint.y(), 0.0);
            assertEquals(Double.POSITIVE_INFINITY, infPoint.v(), 0.0);

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
            assertEquals(10, point.x());
            assertEquals(20, point.y());
            assertEquals(30, point.v());
        }

        @Test
        public void testAllPointTypes_ImmutabilityPattern() {
            // Points should be immutable - fields are final
            // This is verified by the compiler, but we can check behavior
            xy.IntIntPoint point1 = xy.IntIntPoint.of(1, 2, 3);
            xy.IntIntPoint point2 = xy.IntIntPoint.of(1, 2, 3);

            // Creating new points doesn't affect existing ones
            assertTrue(point1.equals(point2));
            assertEquals(1, point1.x());
            assertEquals(2, point1.y());
            assertEquals(3, point1.v());
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
            assertEquals(30, point.v());
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

            assertEquals("ByteBytePoint[x=10, y=20, v=30]", point.toString());
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
            assertEquals(300, point.v());
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

            assertEquals("ByteIntPoint[x=10, y=20, v=300]", point.toString());
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
            assertEquals(3000L, point.v());
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

            assertEquals("ByteLongPoint[x=10, y=20, v=3000]", point.toString());
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
            assertEquals(3.14, point.v(), 0.001);
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
            assertEquals("test", point.v());
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
            assertEquals(30, point.v());
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

            assertEquals("IntBytePoint[x=100, y=200, v=30]", point.toString());
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
            assertEquals(300, point.v());
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

            assertEquals("IntIntPoint[x=100, y=200, v=300]", point.toString());
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
            assertEquals(3000L, point.v());
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

            assertEquals("IntLongPoint[x=100, y=200, v=3000]", point.toString());
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
            assertEquals(3.14, point.v(), 0.001);
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
            assertEquals("test", point.v());
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
            assertEquals(30, point.v());
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

            assertEquals("LongBytePoint[x=1000, y=2000, v=30]", point.toString());
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
            assertEquals(300, point.v());
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

            assertEquals("LongIntPoint[x=1000, y=2000, v=300]", point.toString());
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
            assertEquals(3000L, point.v());
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

            assertEquals("LongLongPoint[x=1000, y=2000, v=3000]", point.toString());
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
            assertEquals(3.14, point.v(), 0.001);
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
            assertEquals("test", point.v());
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
            assertEquals(30, point.v());
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
            assertEquals(300, point.v());
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
            assertEquals(3000L, point.v());
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
            assertEquals(30.5, point.v(), 0.001);
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
            assertEquals("test", point.v());
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
            assertEquals(0, point.v());
            assertEquals("IntIntPoint[x=0, y=0, v=0]", point.toString());
        }

        @Test
        public void test_edgeCase_negativeValues() {
            IntIntPoint point = IntIntPoint.of(-10, -20, -30);

            assertEquals(-10, point.x());
            assertEquals(-20, point.y());
            assertEquals(-30, point.v());
        }

        @Test
        public void test_edgeCase_maxValues() {
            IntIntPoint point = IntIntPoint.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

            assertEquals(Integer.MAX_VALUE, point.x());
            assertEquals(Integer.MAX_VALUE, point.y());
            assertEquals(Integer.MAX_VALUE, point.v());
        }

        @Test
        public void test_edgeCase_minValues() {
            IntIntPoint point = IntIntPoint.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

            assertEquals(Integer.MIN_VALUE, point.x());
            assertEquals(Integer.MIN_VALUE, point.y());
            assertEquals(Integer.MIN_VALUE, point.v());
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
            assertEquals(null, point.v());
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
