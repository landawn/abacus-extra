/*
 * Copyright (C) 2025 HaiYang Li
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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.FloatTuple.FloatTuple0;
import com.landawn.abacus.util.FloatTuple.FloatTuple1;
import com.landawn.abacus.util.FloatTuple.FloatTuple2;
import com.landawn.abacus.util.FloatTuple.FloatTuple3;
import com.landawn.abacus.util.FloatTuple.FloatTuple4;
import com.landawn.abacus.util.FloatTuple.FloatTuple5;
import com.landawn.abacus.util.FloatTuple.FloatTuple6;
import com.landawn.abacus.util.FloatTuple.FloatTuple7;
import com.landawn.abacus.util.FloatTuple.FloatTuple8;
import com.landawn.abacus.util.FloatTuple.FloatTuple9;
import com.landawn.abacus.util.stream.FloatStream;

/**
 * Comprehensive test suite for FloatTuple and its nested classes.
 * Tests all public methods including factory methods, statistical operations,
 * collection conversions, and special methods in Tuple2 and Tuple3.
 */
@Tag("2510")
public class FloatTuple2510Test extends TestBase {

    // Factory method tests
    @Test
    public void testOf1() {
        FloatTuple1 tuple = FloatTuple.of(3.14f);
        assertEquals(3.14f, tuple._1, 0.001f);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf2() {
        FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
        assertEquals(1.5f, tuple._1, 0.001f);
        assertEquals(2.5f, tuple._2, 0.001f);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(2.0f, tuple._2, 0.001f);
        assertEquals(3.0f, tuple._3, 0.001f);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf4() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(2.0f, tuple._2, 0.001f);
        assertEquals(3.0f, tuple._3, 0.001f);
        assertEquals(4.0f, tuple._4, 0.001f);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf5() {
        FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(5.0f, tuple._5, 0.001f);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf6() {
        FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(6.0f, tuple._6, 0.001f);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf7() {
        FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(7.0f, tuple._7, 0.001f);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf8() {
        FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(8.0f, tuple._8, 0.001f);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf9() {
        FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(9.0f, tuple._9, 0.001f);
        assertEquals(9, tuple.arity());
    }

    // Create method tests
    @Test
    public void testCreateEmpty() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreateNull() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate1() {
        FloatTuple1 tuple = FloatTuple.create(new float[] { 42.0f });
        assertEquals(42.0f, tuple._1, 0.001f);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate2() {
        FloatTuple2 tuple = FloatTuple.create(new float[] { 1.0f, 2.0f });
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(2.0f, tuple._2, 0.001f);
    }

    @Test
    public void testCreate9() {
        FloatTuple9 tuple = FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f });
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(9.0f, tuple._9, 0.001f);
    }

    @Test
    public void testCreateTooManyElements() {
        assertThrows(IllegalArgumentException.class, () -> FloatTuple.create(new float[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
    }

    // Min tests
    @Test
    public void testMinTuple1() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        assertEquals(42.0f, tuple.min(), 0.001f);
    }

    @Test
    public void testMinTuple3() {
        FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
        assertEquals(1.0f, tuple.min(), 0.001f);
    }

    @Test
    public void testMinTuple0ThrowsException() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void testMinWithNegativeValues() {
        FloatTuple3 tuple = FloatTuple.of(-5.0f, -1.0f, -10.0f);
        assertEquals(-10.0f, tuple.min(), 0.001f);
    }

    // Max tests
    @Test
    public void testMaxTuple1() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        assertEquals(42.0f, tuple.max(), 0.001f);
    }

    @Test
    public void testMaxTuple3() {
        FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
        assertEquals(3.0f, tuple.max(), 0.001f);
    }

    @Test
    public void testMaxTuple7() {
        FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        assertEquals(7.0f, tuple.max(), 0.001f);
    }

    @Test
    public void testMaxTuple0ThrowsException() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    // Median tests
    @Test
    public void testMedianTuple1() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        assertEquals(42.0f, tuple.median(), 0.001f);
    }

    @Test
    public void testMedianTuple3() {
        FloatTuple3 tuple = FloatTuple.of(30.0f, 10.0f, 20.0f);
        assertEquals(20.0f, tuple.median(), 0.001f);
    }

    @Test
    public void testMedianTuple4() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(2.0f, tuple.median(), 0.001f);
    }

    @Test
    public void testMedianTuple5() {
        FloatTuple5 tuple = FloatTuple.of(5.0f, 1.0f, 3.0f, 2.0f, 4.0f);
        assertEquals(3.0f, tuple.median(), 0.001f);
    }

    @Test
    public void testMedianTuple0ThrowsException() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    // Sum tests
    @Test
    public void testSumTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertEquals(0.0f, tuple.sum(), 0.001f);
    }

    @Test
    public void testSumTuple1() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        assertEquals(42.0f, tuple.sum(), 0.001f);
    }

    @Test
    public void testSumTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(6.0f, tuple.sum(), 0.001f);
    }

    @Test
    public void testSumTuple4() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(10.0f, tuple.sum(), 0.001f);
    }

    @Test
    public void testSumTuple9() {
        FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertEquals(45.0f, tuple.sum(), 0.001f);
    }

    // Average tests
    @Test
    public void testAverageTuple1() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        assertEquals(42.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(2.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple2() {
        FloatTuple2 tuple = FloatTuple.of(10.0f, 20.0f);
        assertEquals(15.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple6() {
        FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        assertEquals(3.5, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple0ThrowsException() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    // Reverse tests
    @Test
    public void testReverseTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        FloatTuple<FloatTuple0> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testReverseTuple1() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        FloatTuple1 reversed = tuple.reverse();
        assertEquals(42.0f, reversed._1, 0.001f);
    }

    @Test
    public void testReverseTuple2() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        FloatTuple2 reversed = tuple.reverse();
        assertEquals(2.0f, reversed._1, 0.001f);
        assertEquals(1.0f, reversed._2, 0.001f);
    }

    @Test
    public void testReverseTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 reversed = tuple.reverse();
        assertEquals(3.0f, reversed._1, 0.001f);
        assertEquals(2.0f, reversed._2, 0.001f);
        assertEquals(1.0f, reversed._3, 0.001f);
    }

    @Test
    public void testReverseTuple9() {
        FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        FloatTuple9 reversed = tuple.reverse();
        assertEquals(9.0f, reversed._1, 0.001f);
        assertEquals(1.0f, reversed._9, 0.001f);
    }

    // Contains tests
    @Test
    public void testContainsTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertFalse(tuple.contains(1.0f));
    }

    @Test
    public void testContainsTuple1True() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        assertTrue(tuple.contains(42.0f));
    }

    @Test
    public void testContainsTuple1False() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        assertFalse(tuple.contains(41.0f));
    }

    @Test
    public void testContainsTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertTrue(tuple.contains(1.0f));
        assertTrue(tuple.contains(2.0f));
        assertTrue(tuple.contains(3.0f));
        assertFalse(tuple.contains(4.0f));
    }

    @Test
    public void testContainsTuple9() {
        FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertTrue(tuple.contains(5.0f));
        assertFalse(tuple.contains(10.0f));
    }

    // toArray tests
    @Test
    public void testToArrayTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        float[] array = tuple.toArray();
        assertEquals(0, array.length);
    }

    @Test
    public void testToArrayTuple1() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        float[] array = tuple.toArray();
        assertArrayEquals(new float[] { 42.0f }, array, 0.001f);
    }

    @Test
    public void testToArrayTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float[] array = tuple.toArray();
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, array, 0.001f);
    }

    @Test
    public void testToArrayTuple9() {
        FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        float[] array = tuple.toArray();
        assertEquals(9, array.length);
        assertEquals(1.0f, array[0], 0.001f);
        assertEquals(9.0f, array[8], 0.001f);
    }

    @Test
    public void testToArrayImmutable() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float[] array1 = tuple.toArray();
        float[] array2 = tuple.toArray();
        array1[0] = 999.0f;
        assertEquals(1.0f, array2[0], 0.001f);
    }

    // toList tests
    @Test
    public void testToListTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        FloatList list = tuple.toList();
        assertEquals(0, list.size());
    }

    @Test
    public void testToListTuple1() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        FloatList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals(42.0f, list.get(0), 0.001f);
    }

    @Test
    public void testToListTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(1.0f, list.get(0), 0.001f);
        assertEquals(3.0f, list.get(2), 0.001f);
    }

    // forEach tests
    @Test
    public void testForEachTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        final List<Float> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(0, collected.size());
    }

    @Test
    public void testForEachTuple1() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        final List<Float> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(1, collected.size());
        assertEquals(42.0f, collected.get(0), 0.001f);
    }

    @Test
    public void testForEachTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        final List<Float> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(3, collected.size());
        assertEquals(1.0f, collected.get(0), 0.001f);
        assertEquals(2.0f, collected.get(1), 0.001f);
        assertEquals(3.0f, collected.get(2), 0.001f);
    }

    @Test
    public void testForEachTuple2() {
        FloatTuple2 tuple = FloatTuple.of(10.0f, 20.0f);
        final List<Float> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(2, collected.size());
        assertEquals(10.0f, collected.get(0), 0.001f);
        assertEquals(20.0f, collected.get(1), 0.001f);
    }

    // stream tests
    @Test
    public void testStreamTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        FloatStream stream = tuple.stream();
        assertEquals(0, stream.count());
    }

    @Test
    public void testStreamTuple1() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        FloatStream stream = tuple.stream();
        assertEquals(1, stream.count());
    }

    @Test
    public void testStreamTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float sum = (float) tuple.stream().sum();
        assertEquals(6.0f, sum, 0.001f);
    }

    @Test
    public void testStreamTuple5() {
        FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        float max = tuple.stream().max().getAsFloat();
        assertEquals(5.0f, max, 0.001f);
    }

    // hashCode tests
    @Test
    public void testHashCodeTuple0() {
        FloatTuple<FloatTuple0> tuple1 = FloatTuple.create(new float[0]);
        FloatTuple<FloatTuple0> tuple2 = FloatTuple.create(new float[0]);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple1() {
        FloatTuple1 tuple1 = FloatTuple.of(42.0f);
        FloatTuple1 tuple2 = FloatTuple.of(42.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple2() {
        FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple3() {
        FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // equals tests
    @Test
    public void testEqualsTuple0() {
        FloatTuple<FloatTuple0> tuple1 = FloatTuple.create(new float[0]);
        FloatTuple<FloatTuple0> tuple2 = FloatTuple.create(new float[0]);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEqualsTuple1() {
        FloatTuple1 tuple1 = FloatTuple.of(42.0f);
        FloatTuple1 tuple2 = FloatTuple.of(42.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEqualsTuple2() {
        FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEqualsTuple3() {
        FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testNotEqualsDifferentValues() {
        FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple2 tuple2 = FloatTuple.of(2.0f, 3.0f);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testNotEqualsDifferentTypes() {
        FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple3 tuple3 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertNotEquals(tuple2, tuple3);
    }

    @Test
    public void testEqualsNull() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertNotEquals(tuple, null);
    }

    @Test
    public void testEqualsSelf() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertEquals(tuple, tuple);
    }

    // toString tests
    @Test
    public void testToStringTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertEquals("()", tuple.toString());
    }

    @Test
    public void testToStringTuple1() {
        FloatTuple1 tuple = FloatTuple.of(42.0f);
        assertEquals("(42.0)", tuple.toString());
    }

    @Test
    public void testToStringTuple2() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertEquals("(1.0, 2.0)", tuple.toString());
    }

    @Test
    public void testToStringTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals("(1.0, 2.0, 3.0)", tuple.toString());
    }

    // FloatTuple2 special methods
    @Test
    public void testTuple2Accept() {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        final List<Float> values = new ArrayList<>();
        tuple.accept((a, b) -> {
            values.add(a);
            values.add(b);
        });
        assertEquals(2, values.size());
        assertEquals(3.0f, values.get(0), 0.001f);
        assertEquals(4.0f, values.get(1), 0.001f);
    }

    @Test
    public void testTuple2Map() {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        float result = tuple.map((a, b) -> a * b);
        assertEquals(12.0f, result, 0.001f);
    }

    @Test
    public void testTuple2Filter() {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        assertTrue(tuple.filter((a, b) -> a + b > 5).isPresent());
        assertFalse(tuple.filter((a, b) -> a + b > 10).isPresent());
    }

    // FloatTuple3 special methods
    @Test
    public void testTuple3Accept() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        final List<Float> values = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            values.add(a);
            values.add(b);
            values.add(c);
        });
        assertEquals(3, values.size());
        assertEquals(1.0f, values.get(0), 0.001f);
        assertEquals(2.0f, values.get(1), 0.001f);
        assertEquals(3.0f, values.get(2), 0.001f);
    }

    @Test
    public void testTuple3Map() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6.0f, result, 0.001f);
    }

    @Test
    public void testTuple3Filter() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertTrue(tuple.filter((a, b, c) -> a + b + c > 5).isPresent());
        assertFalse(tuple.filter((a, b, c) -> a + b + c > 10).isPresent());
    }

    // Edge cases and special values
    @Test
    public void testZeroValues() {
        FloatTuple3 tuple = FloatTuple.of(0.0f, 0.0f, 0.0f);
        assertEquals(0.0f, tuple.min(), 0.001f);
        assertEquals(0.0f, tuple.max(), 0.001f);
        assertEquals(0.0f, tuple.sum(), 0.001f);
    }

    @Test
    public void testNegativeValues() {
        FloatTuple3 tuple = FloatTuple.of(-1.0f, -2.0f, -3.0f);
        assertEquals(-3.0f, tuple.min(), 0.001f);
        assertEquals(-1.0f, tuple.max(), 0.001f);
        assertEquals(-6.0f, tuple.sum(), 0.001f);
    }

    @Test
    public void testMixedValues() {
        FloatTuple5 tuple = FloatTuple.of(-2.0f, 0.0f, 5.0f, -1.0f, 3.0f);
        assertEquals(-2.0f, tuple.min(), 0.001f);
        assertEquals(5.0f, tuple.max(), 0.001f);
        assertEquals(5.0f, tuple.sum(), 0.001f);
    }
}
