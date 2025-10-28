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
import com.landawn.abacus.util.DoubleTuple.DoubleTuple0;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple1;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple3;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple4;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple5;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple6;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple7;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple8;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple9;
import com.landawn.abacus.util.stream.DoubleStream;

/**
 * Comprehensive test suite for DoubleTuple and its nested classes.
 * Tests all public methods including factory methods, statistical operations,
 * collection conversions, and special methods in Tuple2 and Tuple3.
 */
@Tag("2510")
public class DoubleTuple2510Test extends TestBase {

    // Factory method tests
    @Test
    public void testOf1() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertEquals(3.14, tuple._1, 0.001);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf2() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertEquals(1.5, tuple._1, 0.001);
        assertEquals(2.5, tuple._2, 0.001);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(2.0, tuple._2, 0.001);
        assertEquals(3.0, tuple._3, 0.001);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf4() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(2.0, tuple._2, 0.001);
        assertEquals(3.0, tuple._3, 0.001);
        assertEquals(4.0, tuple._4, 0.001);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf5() {
        DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(5.0, tuple._5, 0.001);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf6() {
        DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(6.0, tuple._6, 0.001);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf7() {
        DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(7.0, tuple._7, 0.001);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf8() {
        DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(8.0, tuple._8, 0.001);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf9() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(9.0, tuple._9, 0.001);
        assertEquals(9, tuple.arity());
    }

    // Create method tests
    @Test
    public void testCreateEmpty() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreateNull() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate1() {
        DoubleTuple1 tuple = DoubleTuple.create(new double[] { 42.0 });
        assertEquals(42.0, tuple._1, 0.001);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate2() {
        DoubleTuple2 tuple = DoubleTuple.create(new double[] { 1.0, 2.0 });
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(2.0, tuple._2, 0.001);
    }

    @Test
    public void testCreate9() {
        DoubleTuple9 tuple = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(9.0, tuple._9, 0.001);
    }

    @Test
    public void testCreateTooManyElements() {
        assertThrows(IllegalArgumentException.class, () -> DoubleTuple.create(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
    }

    // Min tests
    @Test
    public void testMinTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        assertEquals(42.0, tuple.min(), 0.001);
    }

    @Test
    public void testMinTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(1.0, tuple.min(), 0.001);
    }

    @Test
    public void testMinTuple0ThrowsException() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void testMinWithNegativeValues() {
        DoubleTuple3 tuple = DoubleTuple.of(-5.0, -1.0, -10.0);
        assertEquals(-10.0, tuple.min(), 0.001);
    }

    // Max tests
    @Test
    public void testMaxTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        assertEquals(42.0, tuple.max(), 0.001);
    }

    @Test
    public void testMaxTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(3.0, tuple.max(), 0.001);
    }

    @Test
    public void testMaxTuple7() {
        DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        assertEquals(7.0, tuple.max(), 0.001);
    }

    @Test
    public void testMaxTuple0ThrowsException() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    // Median tests
    @Test
    public void testMedianTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        assertEquals(42.0, tuple.median(), 0.001);
    }

    @Test
    public void testMedianTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(30.0, 10.0, 20.0);
        assertEquals(20.0, tuple.median(), 0.001);
    }

    @Test
    public void testMedianTuple4() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(2.0, tuple.median(), 0.001);
    }

    @Test
    public void testMedianTuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(5.0, 1.0, 3.0, 2.0, 4.0);
        assertEquals(3.0, tuple.median(), 0.001);
    }

    @Test
    public void testMedianTuple0ThrowsException() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    // Sum tests
    @Test
    public void testSumTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertEquals(0.0, tuple.sum(), 0.001);
    }

    @Test
    public void testSumTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        assertEquals(42.0, tuple.sum(), 0.001);
    }

    @Test
    public void testSumTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(6.0, tuple.sum(), 0.001);
    }

    @Test
    public void testSumTuple4() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(10.0, tuple.sum(), 0.001);
    }

    @Test
    public void testSumTuple9() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertEquals(45.0, tuple.sum(), 0.001);
    }

    // Average tests
    @Test
    public void testAverageTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        assertEquals(42.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(2.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple2() {
        DoubleTuple2 tuple = DoubleTuple.of(10.0, 20.0);
        assertEquals(15.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple6() {
        DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertEquals(3.5, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple0ThrowsException() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    // Reverse tests
    @Test
    public void testReverseTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        DoubleTuple<DoubleTuple0> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testReverseTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        DoubleTuple1 reversed = tuple.reverse();
        assertEquals(42.0, reversed._1, 0.001);
    }

    @Test
    public void testReverseTuple2() {
        DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        DoubleTuple2 reversed = tuple.reverse();
        assertEquals(2.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._2, 0.001);
    }

    @Test
    public void testReverseTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 reversed = tuple.reverse();
        assertEquals(3.0, reversed._1, 0.001);
        assertEquals(2.0, reversed._2, 0.001);
        assertEquals(1.0, reversed._3, 0.001);
    }

    @Test
    public void testReverseTuple9() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        DoubleTuple9 reversed = tuple.reverse();
        assertEquals(9.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._9, 0.001);
    }

    // Contains tests
    @Test
    public void testContainsTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertFalse(tuple.contains(1.0));
    }

    @Test
    public void testContainsTuple1True() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        assertTrue(tuple.contains(42.0));
    }

    @Test
    public void testContainsTuple1False() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        assertFalse(tuple.contains(41.0));
    }

    @Test
    public void testContainsTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertTrue(tuple.contains(1.0));
        assertTrue(tuple.contains(2.0));
        assertTrue(tuple.contains(3.0));
        assertFalse(tuple.contains(4.0));
    }

    @Test
    public void testContainsTuple9() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertTrue(tuple.contains(5.0));
        assertFalse(tuple.contains(10.0));
    }

    // toArray tests
    @Test
    public void testToArrayTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        double[] array = tuple.toArray();
        assertEquals(0, array.length);
    }

    @Test
    public void testToArrayTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        double[] array = tuple.toArray();
        assertArrayEquals(new double[] { 42.0 }, array, 0.001);
    }

    @Test
    public void testToArrayTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double[] array = tuple.toArray();
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, array, 0.001);
    }

    @Test
    public void testToArrayTuple9() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        double[] array = tuple.toArray();
        assertEquals(9, array.length);
        assertEquals(1.0, array[0], 0.001);
        assertEquals(9.0, array[8], 0.001);
    }

    @Test
    public void testToArrayImmutable() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double[] array1 = tuple.toArray();
        double[] array2 = tuple.toArray();
        array1[0] = 999.0;
        assertEquals(1.0, array2[0], 0.001);
    }

    // toList tests
    @Test
    public void testToListTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        DoubleList list = tuple.toList();
        assertEquals(0, list.size());
    }

    @Test
    public void testToListTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        DoubleList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals(42.0, list.get(0), 0.001);
    }

    @Test
    public void testToListTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(1.0, list.get(0), 0.001);
        assertEquals(3.0, list.get(2), 0.001);
    }

    // forEach tests
    @Test
    public void testForEachTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        final List<Double> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(0, collected.size());
    }

    @Test
    public void testForEachTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        final List<Double> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(1, collected.size());
        assertEquals(42.0, collected.get(0), 0.001);
    }

    @Test
    public void testForEachTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        final List<Double> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(3, collected.size());
        assertEquals(1.0, collected.get(0), 0.001);
        assertEquals(2.0, collected.get(1), 0.001);
        assertEquals(3.0, collected.get(2), 0.001);
    }

    @Test
    public void testForEachTuple2() {
        DoubleTuple2 tuple = DoubleTuple.of(10.0, 20.0);
        final List<Double> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(2, collected.size());
        assertEquals(10.0, collected.get(0), 0.001);
        assertEquals(20.0, collected.get(1), 0.001);
    }

    // stream tests
    @Test
    public void testStreamTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        DoubleStream stream = tuple.stream();
        assertEquals(0, stream.count());
    }

    @Test
    public void testStreamTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        DoubleStream stream = tuple.stream();
        assertEquals(1, stream.count());
    }

    @Test
    public void testStreamTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double sum = tuple.stream().sum();
        assertEquals(6.0, sum, 0.001);
    }

    @Test
    public void testStreamTuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        double max = tuple.stream().max().getAsDouble();
        assertEquals(5.0, max, 0.001);
    }

    // hashCode tests
    @Test
    public void testHashCodeTuple0() {
        DoubleTuple<DoubleTuple0> tuple1 = DoubleTuple.create(new double[0]);
        DoubleTuple<DoubleTuple0> tuple2 = DoubleTuple.create(new double[0]);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple1() {
        DoubleTuple1 tuple1 = DoubleTuple.of(42.0);
        DoubleTuple1 tuple2 = DoubleTuple.of(42.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple2() {
        DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
        DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple3() {
        DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // equals tests
    @Test
    public void testEqualsTuple0() {
        DoubleTuple<DoubleTuple0> tuple1 = DoubleTuple.create(new double[0]);
        DoubleTuple<DoubleTuple0> tuple2 = DoubleTuple.create(new double[0]);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEqualsTuple1() {
        DoubleTuple1 tuple1 = DoubleTuple.of(42.0);
        DoubleTuple1 tuple2 = DoubleTuple.of(42.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEqualsTuple2() {
        DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
        DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEqualsTuple3() {
        DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testNotEqualsDifferentValues() {
        DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
        DoubleTuple2 tuple2 = DoubleTuple.of(2.0, 3.0);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testNotEqualsDifferentTypes() {
        DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
        DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 3.0);
        assertNotEquals(tuple2, tuple3);
    }

    @Test
    public void testEqualsNull() {
        DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertNotEquals(tuple, null);
    }

    @Test
    public void testEqualsSelf() {
        DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertEquals(tuple, tuple);
    }

    // toString tests
    @Test
    public void testToStringTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertEquals("[]", tuple.toString());
    }

    @Test
    public void testToStringTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(42.0);
        assertEquals("[42.0]", tuple.toString());
    }

    @Test
    public void testToStringTuple2() {
        DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertEquals("[1.0, 2.0]", tuple.toString());
    }

    @Test
    public void testToStringTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals("[1.0, 2.0, 3.0]", tuple.toString());
    }

    // DoubleTuple2 special methods
    @Test
    public void testTuple2Accept() {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        final List<Double> values = new ArrayList<>();
        tuple.accept((a, b) -> {
            values.add(a);
            values.add(b);
        });
        assertEquals(2, values.size());
        assertEquals(3.0, values.get(0), 0.001);
        assertEquals(4.0, values.get(1), 0.001);
    }

    @Test
    public void testTuple2Map() {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        double result = tuple.map((a, b) -> a * b);
        assertEquals(12.0, result, 0.001);
    }

    @Test
    public void testTuple2Filter() {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        assertTrue(tuple.filter((a, b) -> a + b > 5).isPresent());
        assertFalse(tuple.filter((a, b) -> a + b > 10).isPresent());
    }

    // DoubleTuple3 special methods
    @Test
    public void testTuple3Accept() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        final List<Double> values = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            values.add(a);
            values.add(b);
            values.add(c);
        });
        assertEquals(3, values.size());
        assertEquals(1.0, values.get(0), 0.001);
        assertEquals(2.0, values.get(1), 0.001);
        assertEquals(3.0, values.get(2), 0.001);
    }

    @Test
    public void testTuple3Map() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6.0, result, 0.001);
    }

    @Test
    public void testTuple3Filter() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertTrue(tuple.filter((a, b, c) -> a + b + c > 5).isPresent());
        assertFalse(tuple.filter((a, b, c) -> a + b + c > 10).isPresent());
    }

    // Edge cases and special values
    @Test
    public void testZeroValues() {
        DoubleTuple3 tuple = DoubleTuple.of(0.0, 0.0, 0.0);
        assertEquals(0.0, tuple.min(), 0.001);
        assertEquals(0.0, tuple.max(), 0.001);
        assertEquals(0.0, tuple.sum(), 0.001);
    }

    @Test
    public void testNegativeValues() {
        DoubleTuple3 tuple = DoubleTuple.of(-1.0, -2.0, -3.0);
        assertEquals(-3.0, tuple.min(), 0.001);
        assertEquals(-1.0, tuple.max(), 0.001);
        assertEquals(-6.0, tuple.sum(), 0.001);
    }

    @Test
    public void testMixedValues() {
        DoubleTuple5 tuple = DoubleTuple.of(-2.0, 0.0, 5.0, -1.0, 3.0);
        assertEquals(-2.0, tuple.min(), 0.001);
        assertEquals(5.0, tuple.max(), 0.001);
        assertEquals(5.0, tuple.sum(), 0.001);
    }
}
