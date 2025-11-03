/*
 * Copyright (C) 2020 HaiYang Li
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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Points.xy.ByteBytePoint;
import com.landawn.abacus.util.Points.xy.ByteIntPoint;
import com.landawn.abacus.util.Points.xy.ByteLongPoint;
import com.landawn.abacus.util.Points.xy.ByteDoublePoint;
import com.landawn.abacus.util.Points.xy.ByteObjPoint;
import com.landawn.abacus.util.Points.xy.IntBytePoint;
import com.landawn.abacus.util.Points.xy.IntIntPoint;
import com.landawn.abacus.util.Points.xy.IntLongPoint;
import com.landawn.abacus.util.Points.xy.IntDoublePoint;
import com.landawn.abacus.util.Points.xy.IntObjPoint;
import com.landawn.abacus.util.Points.xy.LongBytePoint;
import com.landawn.abacus.util.Points.xy.LongIntPoint;
import com.landawn.abacus.util.Points.xy.LongLongPoint;
import com.landawn.abacus.util.Points.xy.LongDoublePoint;
import com.landawn.abacus.util.Points.xy.LongObjPoint;
import com.landawn.abacus.util.Points.xy.DoubleBytePoint;
import com.landawn.abacus.util.Points.xy.DoubleIntPoint;
import com.landawn.abacus.util.Points.xy.DoubleLongPoint;
import com.landawn.abacus.util.Points.xy.DoubleDoublePoint;
import com.landawn.abacus.util.Points.xy.DoubleObjPoint;

/**
 * Comprehensive unit tests for Points utility class and its nested point classes.
 * Tests all point types with different coordinate and value type combinations.
 * Covers: of(), hashCode(), equals(), toString() for all point variants.
 */
@Tag("2512")
public class Points2512Test extends TestBase {

    // ============================================
    // Tests for ByteBytePoint
    // ============================================

    @Test
    public void test_ByteBytePoint_of() {
        ByteBytePoint point = ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30);

        assertNotNull(point);
        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(30, point.v);
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

        assertEquals("[10, 20, 30]", point.toString());
    }

    // ============================================
    // Tests for ByteIntPoint
    // ============================================

    @Test
    public void test_ByteIntPoint_of() {
        ByteIntPoint point = ByteIntPoint.of((byte) 10, (byte) 20, 300);

        assertNotNull(point);
        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(300, point.v);
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

        assertEquals("[10, 20, 300]", point.toString());
    }

    // ============================================
    // Tests for ByteLongPoint
    // ============================================

    @Test
    public void test_ByteLongPoint_of() {
        ByteLongPoint point = ByteLongPoint.of((byte) 10, (byte) 20, 3000L);

        assertNotNull(point);
        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(3000L, point.v);
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

        assertEquals("[10, 20, 3000]", point.toString());
    }

    // ============================================
    // Tests for ByteDoublePoint
    // ============================================

    @Test
    public void test_ByteDoublePoint_of() {
        ByteDoublePoint point = ByteDoublePoint.of((byte) 10, (byte) 20, 3.14);

        assertNotNull(point);
        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(3.14, point.v, 0.001);
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
        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals("test", point.v);
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
        assertEquals(100, point.x);
        assertEquals(200, point.y);
        assertEquals(30, point.v);
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

        assertEquals("[100, 200, 30]", point.toString());
    }

    // ============================================
    // Tests for IntIntPoint
    // ============================================

    @Test
    public void test_IntIntPoint_of() {
        IntIntPoint point = IntIntPoint.of(100, 200, 300);

        assertNotNull(point);
        assertEquals(100, point.x);
        assertEquals(200, point.y);
        assertEquals(300, point.v);
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

        assertEquals("[100, 200, 300]", point.toString());
    }

    // ============================================
    // Tests for IntLongPoint
    // ============================================

    @Test
    public void test_IntLongPoint_of() {
        IntLongPoint point = IntLongPoint.of(100, 200, 3000L);

        assertNotNull(point);
        assertEquals(100, point.x);
        assertEquals(200, point.y);
        assertEquals(3000L, point.v);
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

        assertEquals("[100, 200, 3000]", point.toString());
    }

    // ============================================
    // Tests for IntDoublePoint
    // ============================================

    @Test
    public void test_IntDoublePoint_of() {
        IntDoublePoint point = IntDoublePoint.of(100, 200, 3.14);

        assertNotNull(point);
        assertEquals(100, point.x);
        assertEquals(200, point.y);
        assertEquals(3.14, point.v, 0.001);
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
        assertEquals(100, point.x);
        assertEquals(200, point.y);
        assertEquals("test", point.v);
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
        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals(30, point.v);
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

        assertEquals("[1000, 2000, 30]", point.toString());
    }

    // ============================================
    // Tests for LongIntPoint
    // ============================================

    @Test
    public void test_LongIntPoint_of() {
        LongIntPoint point = LongIntPoint.of(1000L, 2000L, 300);

        assertNotNull(point);
        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals(300, point.v);
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

        assertEquals("[1000, 2000, 300]", point.toString());
    }

    // ============================================
    // Tests for LongLongPoint
    // ============================================

    @Test
    public void test_LongLongPoint_of() {
        LongLongPoint point = LongLongPoint.of(1000L, 2000L, 3000L);

        assertNotNull(point);
        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals(3000L, point.v);
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

        assertEquals("[1000, 2000, 3000]", point.toString());
    }

    // ============================================
    // Tests for LongDoublePoint
    // ============================================

    @Test
    public void test_LongDoublePoint_of() {
        LongDoublePoint point = LongDoublePoint.of(1000L, 2000L, 3.14);

        assertNotNull(point);
        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals(3.14, point.v, 0.001);
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
        assertEquals(1000L, point.x);
        assertEquals(2000L, point.y);
        assertEquals("test", point.v);
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
        assertEquals(10.5, point.x, 0.001);
        assertEquals(20.5, point.y, 0.001);
        assertEquals(30, point.v);
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
        assertEquals(10.5, point.x, 0.001);
        assertEquals(20.5, point.y, 0.001);
        assertEquals(300, point.v);
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
        assertEquals(10.5, point.x, 0.001);
        assertEquals(20.5, point.y, 0.001);
        assertEquals(3000L, point.v);
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
        assertEquals(10.5, point.x, 0.001);
        assertEquals(20.5, point.y, 0.001);
        assertEquals(30.5, point.v, 0.001);
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
        assertEquals(10.5, point.x, 0.001);
        assertEquals(20.5, point.y, 0.001);
        assertEquals("test", point.v);
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

        assertEquals(0, point.x);
        assertEquals(0, point.y);
        assertEquals(0, point.v);
        assertEquals("[0, 0, 0]", point.toString());
    }

    @Test
    public void test_edgeCase_negativeValues() {
        IntIntPoint point = IntIntPoint.of(-10, -20, -30);

        assertEquals(-10, point.x);
        assertEquals(-20, point.y);
        assertEquals(-30, point.v);
    }

    @Test
    public void test_edgeCase_maxValues() {
        IntIntPoint point = IntIntPoint.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, point.x);
        assertEquals(Integer.MAX_VALUE, point.y);
        assertEquals(Integer.MAX_VALUE, point.v);
    }

    @Test
    public void test_edgeCase_minValues() {
        IntIntPoint point = IntIntPoint.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

        assertEquals(Integer.MIN_VALUE, point.x);
        assertEquals(Integer.MIN_VALUE, point.y);
        assertEquals(Integer.MIN_VALUE, point.v);
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

        assertEquals(10, point.x);
        assertEquals(20, point.y);
        assertEquals(null, point.v);
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
