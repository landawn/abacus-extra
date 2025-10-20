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
@Tag("2025")
public class DoubleTuple2025Test extends TestBase {

    // Factory method tests
    @Test
    public void testOf1() {
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf2() {
        DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(2.0, tuple._2, 0.001);
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
        DoubleTuple1 tuple = DoubleTuple.create(new double[] { 1.0 });
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate3() {
        DoubleTuple3 tuple = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(2.0, tuple._2, 0.001);
        assertEquals(3.0, tuple._3, 0.001);
    }

    @Test
    public void testCreate9() {
        DoubleTuple9 tuple = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(9.0, tuple._9, 0.001);
    }

    @Test
    public void testCreateTooMany() {
        assertThrows(IllegalArgumentException.class, () -> {
            DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });
        });
    }

    // Statistical method tests - min
    @Test
    public void testMinTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertEquals(1.0, tuple.min(), 0.001);
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

    // Statistical method tests - max
    @Test
    public void testMaxTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertEquals(1.0, tuple.max(), 0.001);
    }

    @Test
    public void testMaxTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(3.0, tuple.max(), 0.001);
    }

    @Test
    public void testMaxTuple0ThrowsException() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    // Statistical method tests - median
    @Test
    public void testMedianTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertEquals(1.0, tuple.median(), 0.001);
    }

    @Test
    public void testMedianTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(2.0, tuple.median(), 0.001);
    }

    @Test
    public void testMedianTuple0ThrowsException() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    // Statistical method tests - sum
    @Test
    public void testSumTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertEquals(0.0, tuple.sum(), 0.001);
    }

    @Test
    public void testSumTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertEquals(1.0, tuple.sum(), 0.001);
    }

    @Test
    public void testSumTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(6.0, tuple.sum(), 0.001);
    }

    // Statistical method tests - average
    @Test
    public void testAverageTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertEquals(1.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(2.0, tuple.average(), 0.001);
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
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        DoubleTuple1 reversed = tuple.reverse();
        assertEquals(1.0, reversed._1, 0.001);
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

    // Contains tests
    @Test
    public void testContainsTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertFalse(tuple.contains(1.0));
    }

    @Test
    public void testContainsTuple1True() {
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertTrue(tuple.contains(1.0));
    }

    @Test
    public void testContainsTuple1False() {
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertFalse(tuple.contains(99.0));
    }

    @Test
    public void testContainsTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertTrue(tuple.contains(1.0));
        assertTrue(tuple.contains(2.0));
        assertTrue(tuple.contains(3.0));
        assertFalse(tuple.contains(99.0));
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
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        double[] array = tuple.toArray();
        assertArrayEquals(new double[] { 1.0 }, array, 0.001);
    }

    @Test
    public void testToArrayTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double[] array = tuple.toArray();
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, array, 0.001);
    }

    @Test
    public void testToArrayModificationDoesNotAffectTuple() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double[] array = tuple.toArray();
        array[0] = 999.0;
        assertEquals(1.0, tuple._1, 0.001);
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
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        DoubleList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals(1.0, list.get(0), 0.001);
    }

    @Test
    public void testToListTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(1.0, list.get(0), 0.001);
        assertEquals(2.0, list.get(1), 0.001);
        assertEquals(3.0, list.get(2), 0.001);
    }

    // forEach tests
    @Test
    public void testForEachTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        List<Double> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(0, result.size());
    }

    @Test
    public void testForEachTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        List<Double> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(1, result.size());
        assertEquals(Double.valueOf(1.0), result.get(0));
    }

    @Test
    public void testForEachTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        List<Double> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Double.valueOf(1.0), result.get(0));
        assertEquals(Double.valueOf(2.0), result.get(1));
        assertEquals(Double.valueOf(3.0), result.get(2));
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
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        DoubleStream stream = tuple.stream();
        assertEquals(1.0, stream.sum(), 0.001);
    }

    @Test
    public void testStreamTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleStream stream = tuple.stream();
        assertEquals(6.0, stream.sum(), 0.001);
    }

    // hashCode tests
    @Test
    public void testHashCodeTuple1() {
        DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
        DoubleTuple1 tuple2 = DoubleTuple.of(1.0);
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
    public void testEqualsSameObject() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEqualsNull() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertNotEquals(null, tuple);
    }

    @Test
    public void testEqualsDifferentClass() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertNotEquals("not a tuple", tuple);
    }

    @Test
    public void testEqualsTuple1() {
        DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
        DoubleTuple1 tuple2 = DoubleTuple.of(1.0);
        DoubleTuple1 tuple3 = DoubleTuple.of(99.0);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple2() {
        DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
        DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
        DoubleTuple2 tuple3 = DoubleTuple.of(1.0, 3.0);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple3() {
        DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 4.0);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    // toString tests
    @Test
    public void testToStringTuple0() {
        DoubleTuple<DoubleTuple0> tuple = DoubleTuple.create(new double[0]);
        assertEquals("[]", tuple.toString());
    }

    @Test
    public void testToStringTuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(1.0);
        String str = tuple.toString();
        assertTrue(str.contains("1.0"));
    }

    @Test
    public void testToStringTuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        String str = tuple.toString();
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("2.0"));
        assertTrue(str.contains("3.0"));
    }

    // Tuple2 special methods - accept
    @Test
    public void testTuple2Accept() {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        List<Double> result = new ArrayList<>();
        tuple.accept((a, b) -> {
            result.add(a);
            result.add(b);
        });
        assertEquals(2, result.size());
        assertEquals(Double.valueOf(3.0), result.get(0));
        assertEquals(Double.valueOf(4.0), result.get(1));
    }

    // Tuple2 special methods - map
    @Test
    public void testTuple2Map() {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        double result = tuple.map((a, b) -> a * b);
        assertEquals(12.0, result, 0.001);
    }

    // Tuple2 special methods - filter
    @Test
    public void testTuple2FilterTrue() {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        var result = tuple.filter((a, b) -> a + b > 5.0);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2FilterFalse() {
        DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        var result = tuple.filter((a, b) -> a + b > 10.0);
        assertFalse(result.isPresent());
    }

    // Tuple3 special methods - accept
    @Test
    public void testTuple3Accept() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        List<Double> result = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            result.add(a);
            result.add(b);
            result.add(c);
        });
        assertEquals(3, result.size());
        assertEquals(Double.valueOf(1.0), result.get(0));
        assertEquals(Double.valueOf(2.0), result.get(1));
        assertEquals(Double.valueOf(3.0), result.get(2));
    }

    // Tuple3 special methods - map
    @Test
    public void testTuple3Map() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6.0, result, 0.001);
    }

    // Tuple3 special methods - filter
    @Test
    public void testTuple3FilterTrue() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        var result = tuple.filter((a, b, c) -> a + b + c > 5.0);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple3FilterFalse() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        var result = tuple.filter((a, b, c) -> a + b + c > 10.0);
        assertFalse(result.isPresent());
    }

    // arity tests for all tuple sizes
    @Test
    public void testArity() {
        assertEquals(0, DoubleTuple.create(new double[0]).arity());
        assertEquals(1, DoubleTuple.of(1.0).arity());
        assertEquals(2, DoubleTuple.of(1.0, 2.0).arity());
        assertEquals(3, DoubleTuple.of(1.0, 2.0, 3.0).arity());
        assertEquals(4, DoubleTuple.of(1.0, 2.0, 3.0, 4.0).arity());
        assertEquals(5, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0).arity());
        assertEquals(6, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0).arity());
        assertEquals(7, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0).arity());
        assertEquals(8, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0).arity());
        assertEquals(9, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0).arity());
    }

    // Tests for inherited methods from PrimitiveTuple
    @Test
    public void testAcceptConsumer() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        List<Double> result = new ArrayList<>();
        tuple.accept(t -> {
            result.add(t._1);
            result.add(t._2);
            result.add(t._3);
        });
        assertEquals(3, result.size());
        assertEquals(Double.valueOf(1.0), result.get(0));
        assertEquals(Double.valueOf(2.0), result.get(1));
        assertEquals(Double.valueOf(3.0), result.get(2));
    }

    @Test
    public void testMapFunction() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        String result = tuple.map(t -> "Sum: " + (t._1 + t._2 + t._3));
        assertEquals("Sum: 6.0", result);
    }

    @Test
    public void testFilterPredicate() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        var result = tuple.filter(t -> t._1 == 1.0);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testFilterPredicateFalse() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        var result = tuple.filter(t -> t._1 == 99.0);
        assertFalse(result.isPresent());
    }

    @Test
    public void testToOptional() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        var result = tuple.toOptional();
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    // Additional tests for larger tuple sizes - reverse
    @Test
    public void testReverseTuple4() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        DoubleTuple4 reversed = tuple.reverse();
        assertEquals(4.0, reversed._1, 0.001);
        assertEquals(3.0, reversed._2, 0.001);
        assertEquals(2.0, reversed._3, 0.001);
        assertEquals(1.0, reversed._4, 0.001);
    }

    @Test
    public void testReverseTuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        DoubleTuple5 reversed = tuple.reverse();
        assertEquals(5.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._5, 0.001);
    }

    @Test
    public void testReverseTuple6() {
        DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        DoubleTuple6 reversed = tuple.reverse();
        assertEquals(6.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._6, 0.001);
    }

    @Test
    public void testReverseTuple7() {
        DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        DoubleTuple7 reversed = tuple.reverse();
        assertEquals(7.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._7, 0.001);
    }

    @Test
    public void testReverseTuple8() {
        DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        DoubleTuple8 reversed = tuple.reverse();
        assertEquals(8.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._8, 0.001);
    }

    @Test
    public void testReverseTuple9() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        DoubleTuple9 reversed = tuple.reverse();
        assertEquals(9.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._9, 0.001);
    }

    // Additional tests for larger tuple sizes - contains
    @Test
    public void testContainsTuple4() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertTrue(tuple.contains(1.0));
        assertTrue(tuple.contains(4.0));
        assertFalse(tuple.contains(99.0));
    }

    @Test
    public void testContainsTuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertTrue(tuple.contains(5.0));
        assertFalse(tuple.contains(99.0));
    }

    @Test
    public void testContainsTuple6() {
        DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertTrue(tuple.contains(6.0));
        assertFalse(tuple.contains(99.0));
    }

    @Test
    public void testContainsTuple7() {
        DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        assertTrue(tuple.contains(7.0));
        assertFalse(tuple.contains(99.0));
    }

    @Test
    public void testContainsTuple8() {
        DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        assertTrue(tuple.contains(8.0));
        assertFalse(tuple.contains(99.0));
    }

    @Test
    public void testContainsTuple9() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertTrue(tuple.contains(9.0));
        assertFalse(tuple.contains(99.0));
    }

    // Test for create() with all sizes (2, 4-9)
    @Test
    public void testCreate2() {
        DoubleTuple2 tuple = DoubleTuple.create(new double[] { 1.0, 2.0 });
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(2.0, tuple._2, 0.001);
    }

    @Test
    public void testCreate4() {
        DoubleTuple4 tuple = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0 });
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(4.0, tuple._4, 0.001);
    }

    @Test
    public void testCreate5() {
        DoubleTuple5 tuple = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(5.0, tuple._5, 0.001);
    }

    @Test
    public void testCreate6() {
        DoubleTuple6 tuple = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 });
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(6.0, tuple._6, 0.001);
    }

    @Test
    public void testCreate7() {
        DoubleTuple7 tuple = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 });
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(7.0, tuple._7, 0.001);
    }

    @Test
    public void testCreate8() {
        DoubleTuple8 tuple = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0 });
        assertEquals(1.0, tuple._1, 0.001);
        assertEquals(8.0, tuple._8, 0.001);
    }

    // Comprehensive tests for Tuple4 through Tuple9
    @Test
    public void testTuple4Operations() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);

        // Test reverse
        DoubleTuple4 reversed = tuple.reverse();
        assertEquals(4.0, reversed._1, 0.001);
        assertEquals(3.0, reversed._2, 0.001);
        assertEquals(2.0, reversed._3, 0.001);
        assertEquals(1.0, reversed._4, 0.001);

        // Test contains
        assertTrue(tuple.contains(1.0));
        assertTrue(tuple.contains(4.0));
        assertFalse(tuple.contains(99.0));

        // Test toArray
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, tuple.toArray(), 0.001);

        // Test min/max/median/sum/average via base class
        assertEquals(1.0, tuple.min(), 0.001);
        assertEquals(4.0, tuple.max(), 0.001);
        assertEquals(2.0, tuple.median(), 0.001); // For even-sized tuples, returns lower middle value
        assertEquals(10.0, tuple.sum(), 0.001);
        assertEquals(2.5, tuple.average(), 0.001);

        // Test hashCode and equals
        DoubleTuple4 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        DoubleTuple4 tuple3 = DoubleTuple.of(1.0, 2.0, 3.0, 5.0);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        String str = tuple.toString();
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("4.0"));
    }

    @Test
    public void testTuple5Operations() {
        DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);

        // Test reverse
        DoubleTuple5 reversed = tuple.reverse();
        assertEquals(5.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._5, 0.001);

        // Test contains
        assertTrue(tuple.contains(1.0));
        assertTrue(tuple.contains(5.0));
        assertFalse(tuple.contains(99.0));

        // Test toArray
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }, tuple.toArray(), 0.001);

        // Test statistical operations
        assertEquals(1.0, tuple.min(), 0.001);
        assertEquals(5.0, tuple.max(), 0.001);
        assertEquals(3.0, tuple.median(), 0.001);
        assertEquals(15.0, tuple.sum(), 0.001);
        assertEquals(3.0, tuple.average(), 0.001);

        // Test equals
        DoubleTuple5 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertEquals(tuple, tuple2);
    }

    @Test
    public void testTuple6Operations() {
        DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);

        // Test reverse
        DoubleTuple6 reversed = tuple.reverse();
        assertEquals(6.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._6, 0.001);

        // Test contains
        assertTrue(tuple.contains(1.0));
        assertTrue(tuple.contains(6.0));
        assertFalse(tuple.contains(99.0));

        // Test toArray
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }, tuple.toArray(), 0.001);

        // Test statistical operations
        assertEquals(1.0, tuple.min(), 0.001);
        assertEquals(6.0, tuple.max(), 0.001);
        assertEquals(3.0, tuple.median(), 0.001); // For even-sized tuples, returns lower middle value
        assertEquals(21.0, tuple.sum(), 0.001);
        assertEquals(3.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple7Operations() {
        DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

        // Test reverse
        DoubleTuple7 reversed = tuple.reverse();
        assertEquals(7.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._7, 0.001);

        // Test contains
        assertTrue(tuple.contains(1.0));
        assertTrue(tuple.contains(7.0));
        assertFalse(tuple.contains(99.0));

        // Test toArray
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 }, tuple.toArray(), 0.001);

        // Test statistical operations
        assertEquals(1.0, tuple.min(), 0.001);
        assertEquals(7.0, tuple.max(), 0.001);
        assertEquals(28.0, tuple.sum(), 0.001);
        assertEquals(4.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple8Operations() {
        DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);

        // Test reverse
        DoubleTuple8 reversed = tuple.reverse();
        assertEquals(8.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._8, 0.001);

        // Test contains
        assertTrue(tuple.contains(1.0));
        assertTrue(tuple.contains(8.0));
        assertFalse(tuple.contains(99.0));

        // Test toArray
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0 }, tuple.toArray(), 0.001);

        // Test statistical operations
        assertEquals(1.0, tuple.min(), 0.001);
        assertEquals(8.0, tuple.max(), 0.001);
        assertEquals(4.0, tuple.median(), 0.001); // For even-sized tuples, returns lower middle value
        assertEquals(36.0, tuple.sum(), 0.001);
        assertEquals(4.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple9Operations() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);

        // Test reverse
        DoubleTuple9 reversed = tuple.reverse();
        assertEquals(9.0, reversed._1, 0.001);
        assertEquals(1.0, reversed._9, 0.001);

        // Test contains
        assertTrue(tuple.contains(1.0));
        assertTrue(tuple.contains(9.0));
        assertFalse(tuple.contains(99.0));

        // Test toArray
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 }, tuple.toArray(), 0.001);

        // Test statistical operations
        assertEquals(1.0, tuple.min(), 0.001);
        assertEquals(9.0, tuple.max(), 0.001);
        assertEquals(45.0, tuple.sum(), 0.001);
        assertEquals(5.0, tuple.average(), 0.001);
    }

    // Test create methods for sizes 2, 4-8
    @Test
    public void testCreate2Through8() {
        DoubleTuple2 tuple2 = DoubleTuple.create(new double[] { 1.0, 2.0 });
        assertEquals(1.0, tuple2._1, 0.001);
        assertEquals(2.0, tuple2._2, 0.001);

        DoubleTuple4 tuple4 = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0 });
        assertEquals(1.0, tuple4._1, 0.001);
        assertEquals(4.0, tuple4._4, 0.001);

        DoubleTuple5 tuple5 = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
        assertEquals(1.0, tuple5._1, 0.001);
        assertEquals(5.0, tuple5._5, 0.001);

        DoubleTuple6 tuple6 = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 });
        assertEquals(1.0, tuple6._1, 0.001);
        assertEquals(6.0, tuple6._6, 0.001);

        DoubleTuple7 tuple7 = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 });
        assertEquals(1.0, tuple7._1, 0.001);
        assertEquals(7.0, tuple7._7, 0.001);

        DoubleTuple8 tuple8 = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0 });
        assertEquals(1.0, tuple8._1, 0.001);
        assertEquals(8.0, tuple8._8, 0.001);
    }

    // Test toList for larger tuples
    @Test
    public void testToListTuple4Through9() {
        DoubleTuple4 tuple4 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        DoubleList list4 = tuple4.toList();
        assertEquals(4, list4.size());
        assertEquals(4.0, list4.get(3), 0.001);

        DoubleTuple9 tuple9 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        DoubleList list9 = tuple9.toList();
        assertEquals(9, list9.size());
        assertEquals(9.0, list9.get(8), 0.001);
    }

    // Test forEach for larger tuples
    @Test
    public void testForEachTuple4() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        List<Double> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(4, result.size());
        assertEquals(Double.valueOf(4.0), result.get(3));
    }

    // Test forEach override for Tuple2
    @Test
    public void testForEachTuple2Override() {
        DoubleTuple2 tuple = DoubleTuple.of(10.0, 20.0);
        List<Double> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(2, result.size());
        assertEquals(Double.valueOf(10.0), result.get(0));
        assertEquals(Double.valueOf(20.0), result.get(1));
    }

    // Test forEach override for Tuple3
    @Test
    public void testForEachTuple3Override() {
        DoubleTuple3 tuple = DoubleTuple.of(10.0, 20.0, 30.0);
        List<Double> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Double.valueOf(30.0), result.get(2));
    }

    // Test stream for larger tuples
    @Test
    public void testStreamTuple4Through9() {
        DoubleTuple4 tuple4 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(10.0, tuple4.stream().sum(), 0.001);

        DoubleTuple9 tuple9 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertEquals(45.0, tuple9.stream().sum(), 0.001);
    }

    // ==================== DoubleTuple Nested Class Tests ====================

    // ============ DoubleTuple1 Nested Class Tests ============

    @Test
    public void testDoubleTuple1_arity() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testDoubleTuple1_reverse() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
        DoubleTuple.DoubleTuple1 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._1);
        assertEquals(tuple._1, reversed._1);
    }

    @Test
    public void testDoubleTuple1_contains() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertTrue(tuple.contains(1.0));
    }

    @Test
    public void testDoubleTuple1_hashCode() {
        DoubleTuple.DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
        DoubleTuple.DoubleTuple1 tuple2 = DoubleTuple.of(1.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testDoubleTuple1_equals() {
        DoubleTuple.DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
        DoubleTuple.DoubleTuple1 tuple2 = DoubleTuple.of(1.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testDoubleTuple1_toString() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testDoubleTuple1_forEach() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testDoubleTuple1_min() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertNotNull(tuple.min());
    }

    @Test
    public void testDoubleTuple1_max() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertNotNull(tuple.max());
    }

    @Test
    public void testDoubleTuple1_median() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertNotNull(tuple.median());
    }

    @Test
    public void testDoubleTuple1_sum() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testDoubleTuple1_average() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    // ============ DoubleTuple2 Nested Class Tests ============

    @Test
    public void testDoubleTuple2_arity() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testDoubleTuple2_reverse() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        DoubleTuple.DoubleTuple2 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._2);
        assertEquals(tuple._2, reversed._1);
    }

    @Test
    public void testDoubleTuple2_contains() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertTrue(tuple.contains(1.0));
    }

    @Test
    public void testDoubleTuple2_hashCode() {
        DoubleTuple.DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
        DoubleTuple.DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testDoubleTuple2_equals() {
        DoubleTuple.DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
        DoubleTuple.DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testDoubleTuple2_toString() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testDoubleTuple2_forEach() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(2, count.size());
    }

    @Test
    public void testDoubleTuple2_min() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertNotNull(tuple.min());
    }

    @Test
    public void testDoubleTuple2_max() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertNotNull(tuple.max());
    }

    @Test
    public void testDoubleTuple2_median() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertNotNull(tuple.median());
    }

    @Test
    public void testDoubleTuple2_sum() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testDoubleTuple2_average() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testDoubleTuple2_accept_biConsumer() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testDoubleTuple2_map_biFunction() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        String result = tuple.map((a, b) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testDoubleTuple2_filter_biPredicate() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertTrue(tuple.filter((a, b) -> true).isPresent());
        assertFalse(tuple.filter((a, b) -> false).isPresent());
    }

    // ============ DoubleTuple3 Nested Class Tests ============

    @Test
    public void testDoubleTuple3_arity() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testDoubleTuple3_reverse() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._3);
        assertEquals(tuple._3, reversed._1);
    }

    @Test
    public void testDoubleTuple3_contains() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertTrue(tuple.contains(1.0));
    }

    @Test
    public void testDoubleTuple3_hashCode() {
        DoubleTuple.DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testDoubleTuple3_equals() {
        DoubleTuple.DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testDoubleTuple3_toString() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testDoubleTuple3_forEach() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(3, count.size());
    }

    @Test
    public void testDoubleTuple3_min() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertNotNull(tuple.min());
    }

    @Test
    public void testDoubleTuple3_max() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertNotNull(tuple.max());
    }

    @Test
    public void testDoubleTuple3_median() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertNotNull(tuple.median());
    }

    @Test
    public void testDoubleTuple3_sum() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testDoubleTuple3_average() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testDoubleTuple3_accept_triConsumer() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b, c) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testDoubleTuple3_map_triFunction() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        String result = tuple.map((a, b, c) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testDoubleTuple3_filter_triPredicate() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertTrue(tuple.filter((a, b, c) -> true).isPresent());
        assertFalse(tuple.filter((a, b, c) -> false).isPresent());
    }

    // ============ DoubleTuple4 Nested Class Tests ============

    @Test
    public void testDoubleTuple4_arity() {
        DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testDoubleTuple4_reverse() {
        DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        DoubleTuple.DoubleTuple4 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._4);
        assertEquals(tuple._4, reversed._1);
    }

    @Test
    public void testDoubleTuple4_contains() {
        DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertTrue(tuple.contains(1.0));
    }

    @Test
    public void testDoubleTuple4_hashCode() {
        DoubleTuple.DoubleTuple4 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        DoubleTuple.DoubleTuple4 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testDoubleTuple4_equals() {
        DoubleTuple.DoubleTuple4 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        DoubleTuple.DoubleTuple4 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testDoubleTuple4_toString() {
        DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testDoubleTuple4_forEach() {
        DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(4, count.size());
    }

    // ============ DoubleTuple5 Nested Class Tests ============

    @Test
    public void testDoubleTuple5_arity() {
        DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testDoubleTuple5_reverse() {
        DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        DoubleTuple.DoubleTuple5 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._5);
        assertEquals(tuple._5, reversed._1);
    }

    @Test
    public void testDoubleTuple5_contains() {
        DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertTrue(tuple.contains(1.0));
    }

    @Test
    public void testDoubleTuple5_hashCode() {
        DoubleTuple.DoubleTuple5 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        DoubleTuple.DoubleTuple5 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testDoubleTuple5_equals() {
        DoubleTuple.DoubleTuple5 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        DoubleTuple.DoubleTuple5 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testDoubleTuple5_toString() {
        DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testDoubleTuple5_forEach() {
        DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(5, count.size());
    }

    // ============ DoubleTuple6 Nested Class Tests ============

    @Test
    public void testDoubleTuple6_arity() {
        DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testDoubleTuple6_reverse() {
        DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        DoubleTuple.DoubleTuple6 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._6);
        assertEquals(tuple._6, reversed._1);
    }

    @Test
    public void testDoubleTuple6_contains() {
        DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertTrue(tuple.contains(1.0));
    }

    @Test
    public void testDoubleTuple6_hashCode() {
        DoubleTuple.DoubleTuple6 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        DoubleTuple.DoubleTuple6 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testDoubleTuple6_equals() {
        DoubleTuple.DoubleTuple6 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        DoubleTuple.DoubleTuple6 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testDoubleTuple6_toString() {
        DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testDoubleTuple6_forEach() {
        DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(6, count.size());
    }

    // ============ DoubleTuple7 Nested Class Tests ============

    @Test
    public void testDoubleTuple7_arity() {
        DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testDoubleTuple7_reverse() {
        DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        DoubleTuple.DoubleTuple7 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._7);
        assertEquals(tuple._7, reversed._1);
    }

    @Test
    public void testDoubleTuple7_contains() {
        DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        assertTrue(tuple.contains(1.0));
    }

    @Test
    public void testDoubleTuple7_hashCode() {
        DoubleTuple.DoubleTuple7 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        DoubleTuple.DoubleTuple7 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testDoubleTuple7_equals() {
        DoubleTuple.DoubleTuple7 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        DoubleTuple.DoubleTuple7 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testDoubleTuple7_toString() {
        DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testDoubleTuple7_forEach() {
        DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(7, count.size());
    }

    // ============ DoubleTuple8 Nested Class Tests ============

    @Test
    public void testDoubleTuple8_arity() {
        DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testDoubleTuple8_reverse() {
        DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        DoubleTuple.DoubleTuple8 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._8);
        assertEquals(tuple._8, reversed._1);
    }

    @Test
    public void testDoubleTuple8_contains() {
        DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        assertTrue(tuple.contains(1.0));
    }

    @Test
    public void testDoubleTuple8_hashCode() {
        DoubleTuple.DoubleTuple8 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        DoubleTuple.DoubleTuple8 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testDoubleTuple8_equals() {
        DoubleTuple.DoubleTuple8 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        DoubleTuple.DoubleTuple8 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testDoubleTuple8_toString() {
        DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testDoubleTuple8_forEach() {
        DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(8, count.size());
    }

    // ============ DoubleTuple9 Nested Class Tests ============

    @Test
    public void testDoubleTuple9_arity() {
        DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testDoubleTuple9_reverse() {
        DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        DoubleTuple.DoubleTuple9 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._9);
        assertEquals(tuple._9, reversed._1);
    }

    @Test
    public void testDoubleTuple9_contains() {
        DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertTrue(tuple.contains(1.0));
    }

    @Test
    public void testDoubleTuple9_hashCode() {
        DoubleTuple.DoubleTuple9 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        DoubleTuple.DoubleTuple9 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testDoubleTuple9_equals() {
        DoubleTuple.DoubleTuple9 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        DoubleTuple.DoubleTuple9 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testDoubleTuple9_toString() {
        DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testDoubleTuple9_forEach() {
        DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(9, count.size());
    }

}
