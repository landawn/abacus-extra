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
}
