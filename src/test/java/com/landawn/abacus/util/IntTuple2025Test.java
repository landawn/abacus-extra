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
import com.landawn.abacus.util.IntTuple.IntTuple0;
import com.landawn.abacus.util.IntTuple.IntTuple1;
import com.landawn.abacus.util.IntTuple.IntTuple2;
import com.landawn.abacus.util.IntTuple.IntTuple3;
import com.landawn.abacus.util.IntTuple.IntTuple4;
import com.landawn.abacus.util.IntTuple.IntTuple5;
import com.landawn.abacus.util.IntTuple.IntTuple6;
import com.landawn.abacus.util.IntTuple.IntTuple7;
import com.landawn.abacus.util.IntTuple.IntTuple8;
import com.landawn.abacus.util.IntTuple.IntTuple9;
import com.landawn.abacus.util.stream.IntStream;

/**
 * Comprehensive test suite for IntTuple and its nested classes.
 * Tests all public methods including factory methods, statistical operations,
 * collection conversions, and special methods in Tuple2 and Tuple3.
 */
@Tag("2025")
public class IntTuple2025Test extends TestBase {

    // Factory method tests
    @Test
    public void testOf1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(3, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf4() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(3, tuple._3);
        assertEquals(4, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf5() {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(3, tuple._3);
        assertEquals(4, tuple._4);
        assertEquals(5, tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf6() {
        IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertEquals(1, tuple._1);
        assertEquals(6, tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf7() {
        IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertEquals(1, tuple._1);
        assertEquals(7, tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf8() {
        IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(1, tuple._1);
        assertEquals(8, tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf9() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(1, tuple._1);
        assertEquals(9, tuple._9);
        assertEquals(9, tuple.arity());
    }

    // Create method tests
    @Test
    public void testCreateEmpty() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreateNull() {
        IntTuple<IntTuple0> tuple = IntTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate1() {
        IntTuple1 tuple = IntTuple.create(new int[] { 42 });
        assertEquals(42, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate3() {
        IntTuple3 tuple = IntTuple.create(new int[] { 1, 2, 3 });
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(3, tuple._3);
    }

    @Test
    public void testCreate9() {
        IntTuple9 tuple = IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        assertEquals(1, tuple._1);
        assertEquals(9, tuple._9);
    }

    @Test
    public void testCreateTooMany() {
        assertThrows(IllegalArgumentException.class, () -> {
            IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        });
    }

    // Statistical method tests - min
    @Test
    public void testMinTuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.min());
    }

    @Test
    public void testMinTuple3() {
        IntTuple3 tuple = IntTuple.of(3, 1, 2);
        assertEquals(1, tuple.min());
    }

    @Test
    public void testMinWithNegatives() {
        IntTuple3 tuple = IntTuple.of(5, -10, 3);
        assertEquals(-10, tuple.min());
    }

    @Test
    public void testMinWithMaxValue() {
        IntTuple3 tuple = IntTuple.of(Integer.MAX_VALUE, 0, Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, tuple.min());
    }

    @Test
    public void testMinTuple0ThrowsException() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    // Statistical method tests - max
    @Test
    public void testMaxTuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.max());
    }

    @Test
    public void testMaxTuple3() {
        IntTuple3 tuple = IntTuple.of(3, 1, 2);
        assertEquals(3, tuple.max());
    }

    @Test
    public void testMaxWithNegatives() {
        IntTuple3 tuple = IntTuple.of(-5, -10, -3);
        assertEquals(-3, tuple.max());
    }

    @Test
    public void testMaxWithMaxValue() {
        IntTuple3 tuple = IntTuple.of(Integer.MAX_VALUE, 0, Integer.MIN_VALUE);
        assertEquals(Integer.MAX_VALUE, tuple.max());
    }

    @Test
    public void testMaxTuple0ThrowsException() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    // Statistical method tests - median
    @Test
    public void testMedianTuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.median());
    }

    @Test
    public void testMedianTuple3() {
        IntTuple3 tuple = IntTuple.of(3, 1, 2);
        assertEquals(2, tuple.median());
    }

    @Test
    public void testMedianTuple2() {
        IntTuple2 tuple = IntTuple.of(5, 10);
        assertEquals(5, tuple.median());
    }

    @Test
    public void testMedianTuple4() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertEquals(2, tuple.median());
    }

    @Test
    public void testMedianTuple0ThrowsException() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    // Statistical method tests - sum
    @Test
    public void testSumTuple0() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void testSumTuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.sum());
    }

    @Test
    public void testSumTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(6, tuple.sum());
    }

    @Test
    public void testSumWithNegatives() {
        IntTuple3 tuple = IntTuple.of(10, -5, 3);
        assertEquals(8, tuple.sum());
    }

    // Statistical method tests - average
    @Test
    public void testAverageTuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(2.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple2() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        assertEquals(15.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple0ThrowsException() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    // Reverse tests
    @Test
    public void testReverseTuple0() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        IntTuple<IntTuple0> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testReverseTuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        IntTuple1 reversed = tuple.reverse();
        assertEquals(42, reversed._1);
    }

    @Test
    public void testReverseTuple2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        IntTuple2 reversed = tuple.reverse();
        assertEquals(2, reversed._1);
        assertEquals(1, reversed._2);
    }

    @Test
    public void testReverseTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        IntTuple3 reversed = tuple.reverse();
        assertEquals(3, reversed._1);
        assertEquals(2, reversed._2);
        assertEquals(1, reversed._3);
    }

    @Test
    public void testReverseTuple9() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        IntTuple9 reversed = tuple.reverse();
        assertEquals(9, reversed._1);
        assertEquals(1, reversed._9);
    }

    // Contains tests
    @Test
    public void testContainsTuple0() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        assertFalse(tuple.contains(1));
    }

    @Test
    public void testContainsTuple1True() {
        IntTuple1 tuple = IntTuple.of(42);
        assertTrue(tuple.contains(42));
    }

    @Test
    public void testContainsTuple1False() {
        IntTuple1 tuple = IntTuple.of(42);
        assertFalse(tuple.contains(41));
    }

    @Test
    public void testContainsTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertTrue(tuple.contains(1));
        assertTrue(tuple.contains(2));
        assertTrue(tuple.contains(3));
        assertFalse(tuple.contains(4));
    }

    @Test
    public void testContainsTuple9() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertTrue(tuple.contains(5));
        assertFalse(tuple.contains(10));
    }

    // toArray tests
    @Test
    public void testToArrayTuple0() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        int[] array = tuple.toArray();
        assertEquals(0, array.length);
    }

    @Test
    public void testToArrayTuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        int[] array = tuple.toArray();
        assertArrayEquals(new int[] { 42 }, array);
    }

    @Test
    public void testToArrayTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int[] array = tuple.toArray();
        assertArrayEquals(new int[] { 1, 2, 3 }, array);
    }

    @Test
    public void testToArrayModificationDoesNotAffectTuple() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int[] array = tuple.toArray();
        array[0] = 999;
        assertEquals(1, tuple._1);
    }

    // toList tests
    @Test
    public void testToListTuple0() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        IntList list = tuple.toList();
        assertEquals(0, list.size());
    }

    @Test
    public void testToListTuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        IntList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals(42, list.get(0));
    }

    @Test
    public void testToListTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        IntList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    // forEach tests
    @Test
    public void testForEachTuple0() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        List<Integer> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(0, result.size());
    }

    @Test
    public void testForEachTuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        List<Integer> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(1, result.size());
        assertEquals(42, result.get(0).intValue());
    }

    @Test
    public void testForEachTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        List<Integer> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(1), result.get(0));
        assertEquals(Integer.valueOf(2), result.get(1));
        assertEquals(Integer.valueOf(3), result.get(2));
    }

    // stream tests
    @Test
    public void testStreamTuple0() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        IntStream stream = tuple.stream();
        assertEquals(0, stream.count());
    }

    @Test
    public void testStreamTuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        IntStream stream = tuple.stream();
        assertEquals(42, stream.sum());
    }

    @Test
    public void testStreamTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        IntStream stream = tuple.stream();
        assertEquals(6, stream.sum());
    }

    // hashCode tests
    @Test
    public void testHashCodeTuple1() {
        IntTuple1 tuple1 = IntTuple.of(42);
        IntTuple1 tuple2 = IntTuple.of(42);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple2() {
        IntTuple2 tuple1 = IntTuple.of(1, 2);
        IntTuple2 tuple2 = IntTuple.of(1, 2);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple3() {
        IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
        IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // equals tests
    @Test
    public void testEqualsSameObject() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEqualsNull() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertNotEquals(null, tuple);
    }

    @Test
    public void testEqualsDifferentClass() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertNotEquals("not a tuple", tuple);
    }

    @Test
    public void testEqualsTuple1() {
        IntTuple1 tuple1 = IntTuple.of(42);
        IntTuple1 tuple2 = IntTuple.of(42);
        IntTuple1 tuple3 = IntTuple.of(41);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple2() {
        IntTuple2 tuple1 = IntTuple.of(1, 2);
        IntTuple2 tuple2 = IntTuple.of(1, 2);
        IntTuple2 tuple3 = IntTuple.of(1, 3);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple3() {
        IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
        IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
        IntTuple3 tuple3 = IntTuple.of(1, 2, 4);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsDifferentArity() {
        IntTuple2 tuple2 = IntTuple.of(1, 2);
        IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
        assertNotEquals(tuple2, tuple3);
    }

    // toString tests
    @Test
    public void testToStringTuple0() {
        IntTuple<IntTuple0> tuple = IntTuple.create(new int[0]);
        assertEquals("[]", tuple.toString());
    }

    @Test
    public void testToStringTuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals("[42]", tuple.toString());
    }

    @Test
    public void testToStringTuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals("[1, 2, 3]", tuple.toString());
    }

    // Tuple2 special methods - accept
    @Test
    public void testTuple2Accept() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        List<Integer> result = new ArrayList<>();
        tuple.accept((a, b) -> {
            result.add(a);
            result.add(b);
        });
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(3), result.get(0));
        assertEquals(Integer.valueOf(4), result.get(1));
    }

    // Tuple2 special methods - map
    @Test
    public void testTuple2Map() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        int result = tuple.map((a, b) -> a * b);
        assertEquals(12, result);
    }

    @Test
    public void testTuple2MapToString() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        String result = tuple.map((a, b) -> a + "+" + b + "=" + (a + b));
        assertEquals("3+4=7", result);
    }

    // Tuple2 special methods - filter
    @Test
    public void testTuple2FilterTrue() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        var result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2FilterFalse() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        var result = tuple.filter((a, b) -> a + b > 10);
        assertFalse(result.isPresent());
    }

    // Tuple3 special methods - accept
    @Test
    public void testTuple3Accept() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        List<Integer> result = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            result.add(a);
            result.add(b);
            result.add(c);
        });
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(1), result.get(0));
        assertEquals(Integer.valueOf(2), result.get(1));
        assertEquals(Integer.valueOf(3), result.get(2));
    }

    // Tuple3 special methods - map
    @Test
    public void testTuple3Map() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6, result);
    }

    @Test
    public void testTuple3MapToString() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        String result = tuple.map((a, b, c) -> a + "+" + b + "+" + c + "=" + (a + b + c));
        assertEquals("1+2+3=6", result);
    }

    // Tuple3 special methods - filter
    @Test
    public void testTuple3FilterTrue() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        var result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple3FilterFalse() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        var result = tuple.filter((a, b, c) -> a + b + c > 10);
        assertFalse(result.isPresent());
    }

    // Edge cases with duplicates
    @Test
    public void testDuplicates() {
        IntTuple3 tuple = IntTuple.of(5, 5, 5);
        assertEquals(5, tuple.min());
        assertEquals(5, tuple.max());
        assertEquals(5, tuple.median());
        assertEquals(15, tuple.sum());
        assertEquals(5.0, tuple.average(), 0.001);
    }

    // Edge cases with extreme values
    @Test
    public void testExtremeValues() {
        IntTuple3 tuple = IntTuple.of(Integer.MAX_VALUE, Integer.MIN_VALUE, 0);
        assertEquals(Integer.MIN_VALUE, tuple.min());
        assertEquals(Integer.MAX_VALUE, tuple.max());
        assertEquals(0, tuple.median());
    }

    // arity tests for all tuple sizes
    @Test
    public void testArity() {
        assertEquals(0, IntTuple.create(new int[0]).arity());
        assertEquals(1, IntTuple.of(1).arity());
        assertEquals(2, IntTuple.of(1, 2).arity());
        assertEquals(3, IntTuple.of(1, 2, 3).arity());
        assertEquals(4, IntTuple.of(1, 2, 3, 4).arity());
        assertEquals(5, IntTuple.of(1, 2, 3, 4, 5).arity());
        assertEquals(6, IntTuple.of(1, 2, 3, 4, 5, 6).arity());
        assertEquals(7, IntTuple.of(1, 2, 3, 4, 5, 6, 7).arity());
        assertEquals(8, IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8).arity());
        assertEquals(9, IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9).arity());
    }

    // Comprehensive tests for Tuple4 through Tuple9
    @Test
    public void testTuple4Operations() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);

        // Test reverse
        IntTuple4 reversed = tuple.reverse();
        assertEquals(4, reversed._1);
        assertEquals(3, reversed._2);
        assertEquals(2, reversed._3);
        assertEquals(1, reversed._4);

        // Test contains
        assertTrue(tuple.contains(1));
        assertTrue(tuple.contains(4));
        assertFalse(tuple.contains(99));

        // Test toArray
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, tuple.toArray());

        // Test min/max/median/sum/average
        assertEquals(1, tuple.min());
        assertEquals(4, tuple.max());
        assertEquals(2, tuple.median());
        assertEquals(10, tuple.sum());
        assertEquals(2.5, tuple.average(), 0.001);

        // Test hashCode and equals
        IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
        IntTuple4 tuple3 = IntTuple.of(1, 2, 3, 5);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        assertEquals("[1, 2, 3, 4]", tuple.toString());
    }

    @Test
    public void testTuple5Operations() {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);

        // Test reverse
        IntTuple5 reversed = tuple.reverse();
        assertEquals(5, reversed._1);
        assertEquals(4, reversed._2);
        assertEquals(3, reversed._3);
        assertEquals(2, reversed._4);
        assertEquals(1, reversed._5);

        // Test contains
        assertTrue(tuple.contains(1));
        assertTrue(tuple.contains(5));
        assertFalse(tuple.contains(99));

        // Test toArray
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, tuple.toArray());

        // Test statistical operations
        assertEquals(1, tuple.min());
        assertEquals(5, tuple.max());
        assertEquals(3, tuple.median());
        assertEquals(15, tuple.sum());
        assertEquals(3.0, tuple.average(), 0.001);

        // Test hashCode and equals
        IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
        IntTuple5 tuple3 = IntTuple.of(1, 2, 3, 4, 6);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        assertEquals("[1, 2, 3, 4, 5]", tuple.toString());
    }

    @Test
    public void testTuple6Operations() {
        IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);

        // Test reverse
        IntTuple6 reversed = tuple.reverse();
        assertEquals(6, reversed._1);
        assertEquals(5, reversed._2);
        assertEquals(4, reversed._3);
        assertEquals(3, reversed._4);
        assertEquals(2, reversed._5);
        assertEquals(1, reversed._6);

        // Test contains
        assertTrue(tuple.contains(1));
        assertTrue(tuple.contains(6));
        assertFalse(tuple.contains(99));

        // Test toArray
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, tuple.toArray());

        // Test statistical operations
        assertEquals(1, tuple.min());
        assertEquals(6, tuple.max());
        assertEquals(3, tuple.median());
        assertEquals(21, tuple.sum());
        assertEquals(3.5, tuple.average(), 0.001);

        // Test hashCode and equals
        IntTuple6 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6);
        IntTuple6 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 7);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        assertEquals("[1, 2, 3, 4, 5, 6]", tuple.toString());
    }

    @Test
    public void testTuple7Operations() {
        IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);

        // Test reverse
        IntTuple7 reversed = tuple.reverse();
        assertEquals(7, reversed._1);
        assertEquals(6, reversed._2);
        assertEquals(5, reversed._3);
        assertEquals(4, reversed._4);
        assertEquals(3, reversed._5);
        assertEquals(2, reversed._6);
        assertEquals(1, reversed._7);

        // Test contains
        assertTrue(tuple.contains(1));
        assertTrue(tuple.contains(7));
        assertFalse(tuple.contains(99));

        // Test toArray
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7 }, tuple.toArray());

        // Test statistical operations
        assertEquals(1, tuple.min());
        assertEquals(7, tuple.max());
        assertEquals(4, tuple.median());
        assertEquals(28, tuple.sum());
        assertEquals(4.0, tuple.average(), 0.001);

        // Test hashCode and equals
        IntTuple7 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        IntTuple7 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 6, 8);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        assertEquals("[1, 2, 3, 4, 5, 6, 7]", tuple.toString());
    }

    @Test
    public void testTuple8Operations() {
        IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);

        // Test reverse
        IntTuple8 reversed = tuple.reverse();
        assertEquals(8, reversed._1);
        assertEquals(7, reversed._2);
        assertEquals(6, reversed._3);
        assertEquals(5, reversed._4);
        assertEquals(4, reversed._5);
        assertEquals(3, reversed._6);
        assertEquals(2, reversed._7);
        assertEquals(1, reversed._8);

        // Test contains
        assertTrue(tuple.contains(1));
        assertTrue(tuple.contains(8));
        assertFalse(tuple.contains(99));

        // Test toArray
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, tuple.toArray());

        // Test statistical operations
        assertEquals(1, tuple.min());
        assertEquals(8, tuple.max());
        assertEquals(4, tuple.median());
        assertEquals(36, tuple.sum());
        assertEquals(4.5, tuple.average(), 0.001);

        // Test hashCode and equals
        IntTuple8 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        IntTuple8 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 9);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8]", tuple.toString());
    }

    @Test
    public void testTuple9Operations() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        // Test reverse
        IntTuple9 reversed = tuple.reverse();
        assertEquals(9, reversed._1);
        assertEquals(8, reversed._2);
        assertEquals(7, reversed._3);
        assertEquals(6, reversed._4);
        assertEquals(5, reversed._5);
        assertEquals(4, reversed._6);
        assertEquals(3, reversed._7);
        assertEquals(2, reversed._8);
        assertEquals(1, reversed._9);

        // Test contains
        assertTrue(tuple.contains(1));
        assertTrue(tuple.contains(9));
        assertFalse(tuple.contains(99));

        // Test toArray
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, tuple.toArray());

        // Test statistical operations
        assertEquals(1, tuple.min());
        assertEquals(9, tuple.max());
        assertEquals(5, tuple.median());
        assertEquals(45, tuple.sum());
        assertEquals(5.0, tuple.average(), 0.001);

        // Test hashCode and equals
        IntTuple9 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        IntTuple9 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 10);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", tuple.toString());
    }

    // Test create methods for sizes 2, 4-8
    @Test
    public void testCreate2Through8() {
        IntTuple2 tuple2 = IntTuple.create(new int[] { 1, 2 });
        assertEquals(1, tuple2._1);
        assertEquals(2, tuple2._2);

        IntTuple4 tuple4 = IntTuple.create(new int[] { 1, 2, 3, 4 });
        assertEquals(1, tuple4._1);
        assertEquals(4, tuple4._4);

        IntTuple5 tuple5 = IntTuple.create(new int[] { 1, 2, 3, 4, 5 });
        assertEquals(1, tuple5._1);
        assertEquals(5, tuple5._5);

        IntTuple6 tuple6 = IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6 });
        assertEquals(1, tuple6._1);
        assertEquals(6, tuple6._6);

        IntTuple7 tuple7 = IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6, 7 });
        assertEquals(1, tuple7._1);
        assertEquals(7, tuple7._7);

        IntTuple8 tuple8 = IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 });
        assertEquals(1, tuple8._1);
        assertEquals(8, tuple8._8);
    }

    // Test toList for larger tuples
    @Test
    public void testToListTuple4Through9() {
        IntTuple4 tuple4 = IntTuple.of(1, 2, 3, 4);
        IntList list4 = tuple4.toList();
        assertEquals(4, list4.size());
        assertEquals(1, list4.get(0));
        assertEquals(4, list4.get(3));

        IntTuple5 tuple5 = IntTuple.of(1, 2, 3, 4, 5);
        IntList list5 = tuple5.toList();
        assertEquals(5, list5.size());
        assertEquals(5, list5.get(4));

        IntTuple6 tuple6 = IntTuple.of(1, 2, 3, 4, 5, 6);
        IntList list6 = tuple6.toList();
        assertEquals(6, list6.size());
        assertEquals(6, list6.get(5));

        IntTuple7 tuple7 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        IntList list7 = tuple7.toList();
        assertEquals(7, list7.size());
        assertEquals(7, list7.get(6));

        IntTuple8 tuple8 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        IntList list8 = tuple8.toList();
        assertEquals(8, list8.size());
        assertEquals(8, list8.get(7));

        IntTuple9 tuple9 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        IntList list9 = tuple9.toList();
        assertEquals(9, list9.size());
        assertEquals(9, list9.get(8));
    }

    // Test forEach for larger tuples
    @Test
    public void testForEachTuple4() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        List<Integer> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(4, result.size());
        assertEquals(Integer.valueOf(1), result.get(0));
        assertEquals(Integer.valueOf(2), result.get(1));
        assertEquals(Integer.valueOf(3), result.get(2));
        assertEquals(Integer.valueOf(4), result.get(3));
    }

    // Test forEach override for Tuple2
    @Test
    public void testForEachTuple2Override() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        List<Integer> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(10), result.get(0));
        assertEquals(Integer.valueOf(20), result.get(1));
    }

    // Test forEach override for Tuple3
    @Test
    public void testForEachTuple3Override() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        List<Integer> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(10), result.get(0));
        assertEquals(Integer.valueOf(20), result.get(1));
        assertEquals(Integer.valueOf(30), result.get(2));
    }

    // Test stream for larger tuples
    @Test
    public void testStreamTuple4Through9() {
        IntTuple4 tuple4 = IntTuple.of(1, 2, 3, 4);
        assertEquals(10, tuple4.stream().sum());

        IntTuple5 tuple5 = IntTuple.of(1, 2, 3, 4, 5);
        assertEquals(15, tuple5.stream().sum());

        IntTuple6 tuple6 = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertEquals(21, tuple6.stream().sum());

        IntTuple7 tuple7 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertEquals(28, tuple7.stream().sum());

        IntTuple8 tuple8 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(36, tuple8.stream().sum());

        IntTuple9 tuple9 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(45, tuple9.stream().sum());
    }

    // ==================== IntTuple Nested Class Tests ====================

    // ============ IntTuple1 Nested Class Tests ============

    @Test
    public void testIntTuple1_arity() {
        IntTuple.IntTuple1 tuple = IntTuple.of(1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testIntTuple1_reverse() {
        IntTuple.IntTuple1 tuple = IntTuple.of(1);
        IntTuple.IntTuple1 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._1);
        assertEquals(tuple._1, reversed._1);
    }

    @Test
    public void testIntTuple1_contains() {
        IntTuple.IntTuple1 tuple = IntTuple.of(1);
        assertTrue(tuple.contains(1));
    }

    @Test
    public void testIntTuple1_hashCode() {
        IntTuple.IntTuple1 tuple1 = IntTuple.of(1);
        IntTuple.IntTuple1 tuple2 = IntTuple.of(1);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testIntTuple1_equals() {
        IntTuple.IntTuple1 tuple1 = IntTuple.of(1);
        IntTuple.IntTuple1 tuple2 = IntTuple.of(1);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testIntTuple1_toString() {
        IntTuple.IntTuple1 tuple = IntTuple.of(1);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testIntTuple1_forEach() {
        IntTuple.IntTuple1 tuple = IntTuple.of(1);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testIntTuple1_min() {
        IntTuple.IntTuple1 tuple = IntTuple.of(1);
        assertNotNull(tuple.min());
    }

    @Test
    public void testIntTuple1_max() {
        IntTuple.IntTuple1 tuple = IntTuple.of(1);
        assertNotNull(tuple.max());
    }

    @Test
    public void testIntTuple1_median() {
        IntTuple.IntTuple1 tuple = IntTuple.of(1);
        assertNotNull(tuple.median());
    }

    @Test
    public void testIntTuple1_sum() {
        IntTuple.IntTuple1 tuple = IntTuple.of(1);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testIntTuple1_average() {
        IntTuple.IntTuple1 tuple = IntTuple.of(1);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    // ============ IntTuple2 Nested Class Tests ============

    @Test
    public void testIntTuple2_arity() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testIntTuple2_reverse() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        IntTuple.IntTuple2 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._2);
        assertEquals(tuple._2, reversed._1);
    }

    @Test
    public void testIntTuple2_contains() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        assertTrue(tuple.contains(1));
    }

    @Test
    public void testIntTuple2_hashCode() {
        IntTuple.IntTuple2 tuple1 = IntTuple.of(1, 2);
        IntTuple.IntTuple2 tuple2 = IntTuple.of(1, 2);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testIntTuple2_equals() {
        IntTuple.IntTuple2 tuple1 = IntTuple.of(1, 2);
        IntTuple.IntTuple2 tuple2 = IntTuple.of(1, 2);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testIntTuple2_toString() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testIntTuple2_forEach() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(2, count.size());
    }

    @Test
    public void testIntTuple2_min() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        assertNotNull(tuple.min());
    }

    @Test
    public void testIntTuple2_max() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        assertNotNull(tuple.max());
    }

    @Test
    public void testIntTuple2_median() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        assertNotNull(tuple.median());
    }

    @Test
    public void testIntTuple2_sum() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testIntTuple2_average() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testIntTuple2_accept_biConsumer() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testIntTuple2_map_biFunction() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        String result = tuple.map((a, b) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testIntTuple2_filter_biPredicate() {
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        assertTrue(tuple.filter((a, b) -> true).isPresent());
        assertFalse(tuple.filter((a, b) -> false).isPresent());
    }

    // ============ IntTuple3 Nested Class Tests ============

    @Test
    public void testIntTuple3_arity() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testIntTuple3_reverse() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        IntTuple.IntTuple3 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._3);
        assertEquals(tuple._3, reversed._1);
    }

    @Test
    public void testIntTuple3_contains() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertTrue(tuple.contains(1));
    }

    @Test
    public void testIntTuple3_hashCode() {
        IntTuple.IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
        IntTuple.IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testIntTuple3_equals() {
        IntTuple.IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
        IntTuple.IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testIntTuple3_toString() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testIntTuple3_forEach() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(3, count.size());
    }

    @Test
    public void testIntTuple3_min() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertNotNull(tuple.min());
    }

    @Test
    public void testIntTuple3_max() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertNotNull(tuple.max());
    }

    @Test
    public void testIntTuple3_median() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertNotNull(tuple.median());
    }

    @Test
    public void testIntTuple3_sum() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testIntTuple3_average() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testIntTuple3_accept_triConsumer() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b, c) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testIntTuple3_map_triFunction() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        String result = tuple.map((a, b, c) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testIntTuple3_filter_triPredicate() {
        IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertTrue(tuple.filter((a, b, c) -> true).isPresent());
        assertFalse(tuple.filter((a, b, c) -> false).isPresent());
    }

    // ============ IntTuple4 Nested Class Tests ============

    @Test
    public void testIntTuple4_arity() {
        IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testIntTuple4_reverse() {
        IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        IntTuple.IntTuple4 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._4);
        assertEquals(tuple._4, reversed._1);
    }

    @Test
    public void testIntTuple4_contains() {
        IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertTrue(tuple.contains(1));
    }

    @Test
    public void testIntTuple4_hashCode() {
        IntTuple.IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
        IntTuple.IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testIntTuple4_equals() {
        IntTuple.IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
        IntTuple.IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testIntTuple4_toString() {
        IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testIntTuple4_forEach() {
        IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(4, count.size());
    }

    // ============ IntTuple5 Nested Class Tests ============

    @Test
    public void testIntTuple5_arity() {
        IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testIntTuple5_reverse() {
        IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        IntTuple.IntTuple5 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._5);
        assertEquals(tuple._5, reversed._1);
    }

    @Test
    public void testIntTuple5_contains() {
        IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        assertTrue(tuple.contains(1));
    }

    @Test
    public void testIntTuple5_hashCode() {
        IntTuple.IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
        IntTuple.IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testIntTuple5_equals() {
        IntTuple.IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
        IntTuple.IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testIntTuple5_toString() {
        IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testIntTuple5_forEach() {
        IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(5, count.size());
    }

    // ============ IntTuple6 Nested Class Tests ============

    @Test
    public void testIntTuple6_arity() {
        IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testIntTuple6_reverse() {
        IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        IntTuple.IntTuple6 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._6);
        assertEquals(tuple._6, reversed._1);
    }

    @Test
    public void testIntTuple6_contains() {
        IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertTrue(tuple.contains(1));
    }

    @Test
    public void testIntTuple6_hashCode() {
        IntTuple.IntTuple6 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6);
        IntTuple.IntTuple6 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testIntTuple6_equals() {
        IntTuple.IntTuple6 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6);
        IntTuple.IntTuple6 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testIntTuple6_toString() {
        IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testIntTuple6_forEach() {
        IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(6, count.size());
    }

    // ============ IntTuple7 Nested Class Tests ============

    @Test
    public void testIntTuple7_arity() {
        IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testIntTuple7_reverse() {
        IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        IntTuple.IntTuple7 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._7);
        assertEquals(tuple._7, reversed._1);
    }

    @Test
    public void testIntTuple7_contains() {
        IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertTrue(tuple.contains(1));
    }

    @Test
    public void testIntTuple7_hashCode() {
        IntTuple.IntTuple7 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        IntTuple.IntTuple7 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testIntTuple7_equals() {
        IntTuple.IntTuple7 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        IntTuple.IntTuple7 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testIntTuple7_toString() {
        IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testIntTuple7_forEach() {
        IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(7, count.size());
    }

    // ============ IntTuple8 Nested Class Tests ============

    @Test
    public void testIntTuple8_arity() {
        IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testIntTuple8_reverse() {
        IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        IntTuple.IntTuple8 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._8);
        assertEquals(tuple._8, reversed._1);
    }

    @Test
    public void testIntTuple8_contains() {
        IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertTrue(tuple.contains(1));
    }

    @Test
    public void testIntTuple8_hashCode() {
        IntTuple.IntTuple8 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        IntTuple.IntTuple8 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testIntTuple8_equals() {
        IntTuple.IntTuple8 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        IntTuple.IntTuple8 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testIntTuple8_toString() {
        IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testIntTuple8_forEach() {
        IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(8, count.size());
    }

    // ============ IntTuple9 Nested Class Tests ============

    @Test
    public void testIntTuple9_arity() {
        IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testIntTuple9_reverse() {
        IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        IntTuple.IntTuple9 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._9);
        assertEquals(tuple._9, reversed._1);
    }

    @Test
    public void testIntTuple9_contains() {
        IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertTrue(tuple.contains(1));
    }

    @Test
    public void testIntTuple9_hashCode() {
        IntTuple.IntTuple9 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        IntTuple.IntTuple9 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testIntTuple9_equals() {
        IntTuple.IntTuple9 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        IntTuple.IntTuple9 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testIntTuple9_toString() {
        IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testIntTuple9_forEach() {
        IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(9, count.size());
    }

}
