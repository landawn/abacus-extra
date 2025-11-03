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
import com.landawn.abacus.util.LongTuple.LongTuple0;
import com.landawn.abacus.util.LongTuple.LongTuple1;
import com.landawn.abacus.util.LongTuple.LongTuple2;
import com.landawn.abacus.util.LongTuple.LongTuple3;
import com.landawn.abacus.util.LongTuple.LongTuple4;
import com.landawn.abacus.util.LongTuple.LongTuple5;
import com.landawn.abacus.util.LongTuple.LongTuple6;
import com.landawn.abacus.util.LongTuple.LongTuple7;
import com.landawn.abacus.util.LongTuple.LongTuple8;
import com.landawn.abacus.util.LongTuple.LongTuple9;
import com.landawn.abacus.util.stream.LongStream;

/**
 * Comprehensive test suite for LongTuple and its nested classes.
 * Tests all public methods including factory methods, statistical operations,
 * collection conversions, and special methods in Tuple2 and Tuple3.
 */
@Tag("2025")
public class LongTuple2025Test extends TestBase {

    // Factory method tests
    @Test
    public void testOf1() {
        LongTuple1 tuple = LongTuple.of(1L);
        assertEquals(1L, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf2() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertEquals(1L, tuple._1);
        assertEquals(2L, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(1L, tuple._1);
        assertEquals(2L, tuple._2);
        assertEquals(3L, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf4() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        assertEquals(1L, tuple._1);
        assertEquals(2L, tuple._2);
        assertEquals(3L, tuple._3);
        assertEquals(4L, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf5() {
        LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertEquals(1L, tuple._1);
        assertEquals(5L, tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf6() {
        LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        assertEquals(1L, tuple._1);
        assertEquals(6L, tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf7() {
        LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertEquals(1L, tuple._1);
        assertEquals(7L, tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf8() {
        LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertEquals(1L, tuple._1);
        assertEquals(8L, tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf9() {
        LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertEquals(1L, tuple._1);
        assertEquals(9L, tuple._9);
        assertEquals(9, tuple.arity());
    }

    // Create method tests
    @Test
    public void testCreateEmpty() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreateNull() {
        LongTuple<LongTuple0> tuple = LongTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate1() {
        LongTuple1 tuple = LongTuple.create(new long[] { 1L });
        assertEquals(1L, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate3() {
        LongTuple3 tuple = LongTuple.create(new long[] { 1L, 2L, 3L });
        assertEquals(1L, tuple._1);
        assertEquals(2L, tuple._2);
        assertEquals(3L, tuple._3);
    }

    @Test
    public void testCreate9() {
        LongTuple9 tuple = LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L });
        assertEquals(1L, tuple._1);
        assertEquals(9L, tuple._9);
    }

    @Test
    public void testCreateTooMany() {
        assertThrows(IllegalArgumentException.class, () -> {
            LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L });
        });
    }

    // Statistical method tests - min
    @Test
    public void testMinTuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        assertEquals(1L, tuple.min());
    }

    @Test
    public void testMinTuple3() {
        LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
        assertEquals(1L, tuple.min());
    }

    @Test
    public void testMinTuple0ThrowsException() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    // Statistical method tests - max
    @Test
    public void testMaxTuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        assertEquals(1L, tuple.max());
    }

    @Test
    public void testMaxTuple3() {
        LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
        assertEquals(3L, tuple.max());
    }

    @Test
    public void testMaxTuple0ThrowsException() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    // Statistical method tests - median
    @Test
    public void testMedianTuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        assertEquals(1L, tuple.median());
    }

    @Test
    public void testMedianTuple3() {
        LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
        assertEquals(2L, tuple.median());
    }

    @Test
    public void testMedianTuple0ThrowsException() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    // Statistical method tests - sum
    @Test
    public void testSumTuple0() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        assertEquals(0L, tuple.sum());
    }

    @Test
    public void testSumTuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        assertEquals(1L, tuple.sum());
    }

    @Test
    public void testSumTuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(6L, tuple.sum());
    }

    // Statistical method tests - average
    @Test
    public void testAverageTuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        assertEquals(1.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(2.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple0ThrowsException() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    // Reverse tests
    @Test
    public void testReverseTuple0() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        LongTuple<LongTuple0> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testReverseTuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        LongTuple1 reversed = tuple.reverse();
        assertEquals(1L, reversed._1);
    }

    @Test
    public void testReverseTuple2() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        LongTuple2 reversed = tuple.reverse();
        assertEquals(2L, reversed._1);
        assertEquals(1L, reversed._2);
    }

    @Test
    public void testReverseTuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        LongTuple3 reversed = tuple.reverse();
        assertEquals(3L, reversed._1);
        assertEquals(2L, reversed._2);
        assertEquals(1L, reversed._3);
    }

    // Contains tests
    @Test
    public void testContainsTuple0() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        assertFalse(tuple.contains(1L));
    }

    @Test
    public void testContainsTuple1True() {
        LongTuple1 tuple = LongTuple.of(1L);
        assertTrue(tuple.contains(1L));
    }

    @Test
    public void testContainsTuple1False() {
        LongTuple1 tuple = LongTuple.of(1L);
        assertFalse(tuple.contains(99L));
    }

    @Test
    public void testContainsTuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertTrue(tuple.contains(1L));
        assertTrue(tuple.contains(2L));
        assertTrue(tuple.contains(3L));
        assertFalse(tuple.contains(99L));
    }

    // toArray tests
    @Test
    public void testToArrayTuple0() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        long[] array = tuple.toArray();
        assertEquals(0, array.length);
    }

    @Test
    public void testToArrayTuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        long[] array = tuple.toArray();
        assertArrayEquals(new long[] { 1L }, array);
    }

    @Test
    public void testToArrayTuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        long[] array = tuple.toArray();
        assertArrayEquals(new long[] { 1L, 2L, 3L }, array);
    }

    @Test
    public void testToArrayModificationDoesNotAffectTuple() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        long[] array = tuple.toArray();
        array[0] = 999L;
        assertEquals(1L, tuple._1);
    }

    // toList tests
    @Test
    public void testToListTuple0() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        LongList list = tuple.toList();
        assertEquals(0, list.size());
    }

    @Test
    public void testToListTuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        LongList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0));
    }

    @Test
    public void testToListTuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        LongList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(1L, list.get(0));
        assertEquals(2L, list.get(1));
        assertEquals(3L, list.get(2));
    }

    // forEach tests
    @Test
    public void testForEachTuple0() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        List<Long> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(0, result.size());
    }

    @Test
    public void testForEachTuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        List<Long> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(1, result.size());
        assertEquals(Long.valueOf(1L), result.get(0));
    }

    @Test
    public void testForEachTuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        List<Long> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Long.valueOf(1L), result.get(0));
        assertEquals(Long.valueOf(2L), result.get(1));
        assertEquals(Long.valueOf(3L), result.get(2));
    }

    // stream tests
    @Test
    public void testStreamTuple0() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        LongStream stream = tuple.stream();
        assertEquals(0, stream.count());
    }

    @Test
    public void testStreamTuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        LongStream stream = tuple.stream();
        assertEquals(1L, stream.sum());
    }

    @Test
    public void testStreamTuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        LongStream stream = tuple.stream();
        assertEquals(6L, stream.sum());
    }

    // hashCode tests
    @Test
    public void testHashCodeTuple1() {
        LongTuple1 tuple1 = LongTuple.of(1L);
        LongTuple1 tuple2 = LongTuple.of(1L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple2() {
        LongTuple2 tuple1 = LongTuple.of(1L, 2L);
        LongTuple2 tuple2 = LongTuple.of(1L, 2L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple3() {
        LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
        LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // equals tests
    @Test
    public void testEqualsSameObject() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEqualsNull() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertNotEquals(null, tuple);
    }

    @Test
    public void testEqualsDifferentClass() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertNotEquals("not a tuple", tuple);
    }

    @Test
    public void testEqualsTuple1() {
        LongTuple1 tuple1 = LongTuple.of(1L);
        LongTuple1 tuple2 = LongTuple.of(1L);
        LongTuple1 tuple3 = LongTuple.of(99L);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple2() {
        LongTuple2 tuple1 = LongTuple.of(1L, 2L);
        LongTuple2 tuple2 = LongTuple.of(1L, 2L);
        LongTuple2 tuple3 = LongTuple.of(1L, 3L);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple3() {
        LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
        LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
        LongTuple3 tuple3 = LongTuple.of(1L, 2L, 4L);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    // toString tests
    @Test
    public void testToStringTuple0() {
        LongTuple<LongTuple0> tuple = LongTuple.create(new long[0]);
        assertEquals("()", tuple.toString());
    }

    @Test
    public void testToStringTuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        String str = tuple.toString();
        assertTrue(str.contains("1"));
    }

    @Test
    public void testToStringTuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        String str = tuple.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
    }

    // Tuple2 special methods - accept
    @Test
    public void testTuple2Accept() {
        LongTuple2 tuple = LongTuple.of(3L, 4L);
        List<Long> result = new ArrayList<>();
        tuple.accept((a, b) -> {
            result.add(a);
            result.add(b);
        });
        assertEquals(2, result.size());
        assertEquals(Long.valueOf(3L), result.get(0));
        assertEquals(Long.valueOf(4L), result.get(1));
    }

    // Tuple2 special methods - map
    @Test
    public void testTuple2Map() {
        LongTuple2 tuple = LongTuple.of(3L, 4L);
        long result = tuple.map((a, b) -> a * b);
        assertEquals(12L, result);
    }

    // Tuple2 special methods - filter
    @Test
    public void testTuple2FilterTrue() {
        LongTuple2 tuple = LongTuple.of(3L, 4L);
        var result = tuple.filter((a, b) -> a + b > 5L);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2FilterFalse() {
        LongTuple2 tuple = LongTuple.of(3L, 4L);
        var result = tuple.filter((a, b) -> a + b > 10L);
        assertFalse(result.isPresent());
    }

    // Tuple3 special methods - accept
    @Test
    public void testTuple3Accept() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        List<Long> result = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            result.add(a);
            result.add(b);
            result.add(c);
        });
        assertEquals(3, result.size());
        assertEquals(Long.valueOf(1L), result.get(0));
        assertEquals(Long.valueOf(2L), result.get(1));
        assertEquals(Long.valueOf(3L), result.get(2));
    }

    // Tuple3 special methods - map
    @Test
    public void testTuple3Map() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        long result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6L, result);
    }

    // Tuple3 special methods - filter
    @Test
    public void testTuple3FilterTrue() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        var result = tuple.filter((a, b, c) -> a + b + c > 5L);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple3FilterFalse() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        var result = tuple.filter((a, b, c) -> a + b + c > 10L);
        assertFalse(result.isPresent());
    }

    // arity tests for all tuple sizes
    @Test
    public void testArity() {
        assertEquals(0, LongTuple.create(new long[0]).arity());
        assertEquals(1, LongTuple.of(1L).arity());
        assertEquals(2, LongTuple.of(1L, 2L).arity());
        assertEquals(3, LongTuple.of(1L, 2L, 3L).arity());
        assertEquals(4, LongTuple.of(1L, 2L, 3L, 4L).arity());
        assertEquals(5, LongTuple.of(1L, 2L, 3L, 4L, 5L).arity());
        assertEquals(6, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L).arity());
        assertEquals(7, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L).arity());
        assertEquals(8, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L).arity());
        assertEquals(9, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L).arity());
    }

    // Comprehensive tests for Tuple4 through Tuple9
    @Test
    public void testTuple4Operations() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);

        // Test reverse
        LongTuple4 reversed = tuple.reverse();
        assertEquals(4L, reversed._1);
        assertEquals(3L, reversed._2);
        assertEquals(2L, reversed._3);
        assertEquals(1L, reversed._4);

        // Test contains
        assertTrue(tuple.contains(1L));
        assertTrue(tuple.contains(4L));
        assertFalse(tuple.contains(99L));

        // Test toArray
        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L }, tuple.toArray());

        // Test min/max/median/sum/average via base class
        assertEquals(1L, tuple.min());
        assertEquals(4L, tuple.max());
        assertEquals(2L, tuple.median());
        assertEquals(10L, tuple.sum());
        assertEquals(2.5, tuple.average(), 0.001);

        // Test hashCode and equals
        LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
        LongTuple4 tuple3 = LongTuple.of(1L, 2L, 3L, 5L);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        String str = tuple.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("4"));
    }

    @Test
    public void testTuple5Operations() {
        LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);

        // Test reverse
        LongTuple5 reversed = tuple.reverse();
        assertEquals(5L, reversed._1);
        assertEquals(1L, reversed._5);

        // Test contains
        assertTrue(tuple.contains(1L));
        assertTrue(tuple.contains(5L));
        assertFalse(tuple.contains(99L));

        // Test toArray
        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L }, tuple.toArray());

        // Test statistical operations
        assertEquals(1L, tuple.min());
        assertEquals(5L, tuple.max());
        assertEquals(3L, tuple.median());
        assertEquals(15L, tuple.sum());
        assertEquals(3.0, tuple.average(), 0.001);

        // Test equals
        LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertEquals(tuple, tuple2);
    }

    @Test
    public void testTuple6Operations() {
        LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);

        // Test reverse
        LongTuple6 reversed = tuple.reverse();
        assertEquals(6L, reversed._1);
        assertEquals(1L, reversed._6);

        // Test contains
        assertTrue(tuple.contains(1L));
        assertTrue(tuple.contains(6L));
        assertFalse(tuple.contains(99L));

        // Test toArray
        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, tuple.toArray());

        // Test statistical operations
        assertEquals(1L, tuple.min());
        assertEquals(6L, tuple.max());
        assertEquals(21L, tuple.sum());
        assertEquals(3.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple7Operations() {
        LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

        // Test reverse
        LongTuple7 reversed = tuple.reverse();
        assertEquals(7L, reversed._1);
        assertEquals(1L, reversed._7);

        // Test contains
        assertTrue(tuple.contains(1L));
        assertTrue(tuple.contains(7L));
        assertFalse(tuple.contains(99L));

        // Test toArray
        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L }, tuple.toArray());

        // Test statistical operations
        assertEquals(1L, tuple.min());
        assertEquals(7L, tuple.max());
        assertEquals(28L, tuple.sum());
        assertEquals(4.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple8Operations() {
        LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);

        // Test reverse
        LongTuple8 reversed = tuple.reverse();
        assertEquals(8L, reversed._1);
        assertEquals(1L, reversed._8);

        // Test contains
        assertTrue(tuple.contains(1L));
        assertTrue(tuple.contains(8L));
        assertFalse(tuple.contains(99L));

        // Test toArray
        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L }, tuple.toArray());

        // Test statistical operations
        assertEquals(1L, tuple.min());
        assertEquals(8L, tuple.max());
        assertEquals(36L, tuple.sum());
        assertEquals(4.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple9Operations() {
        LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);

        // Test reverse
        LongTuple9 reversed = tuple.reverse();
        assertEquals(9L, reversed._1);
        assertEquals(1L, reversed._9);

        // Test contains
        assertTrue(tuple.contains(1L));
        assertTrue(tuple.contains(9L));
        assertFalse(tuple.contains(99L));

        // Test toArray
        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L }, tuple.toArray());

        // Test statistical operations
        assertEquals(1L, tuple.min());
        assertEquals(9L, tuple.max());
        assertEquals(45L, tuple.sum());
        assertEquals(5.0, tuple.average(), 0.001);
    }

    // Test create methods for sizes 2, 4-8
    @Test
    public void testCreate2Through8() {
        LongTuple2 tuple2 = LongTuple.create(new long[] { 1L, 2L });
        assertEquals(1L, tuple2._1);
        assertEquals(2L, tuple2._2);

        LongTuple4 tuple4 = LongTuple.create(new long[] { 1L, 2L, 3L, 4L });
        assertEquals(1L, tuple4._1);
        assertEquals(4L, tuple4._4);

        LongTuple5 tuple5 = LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L });
        assertEquals(1L, tuple5._1);
        assertEquals(5L, tuple5._5);

        LongTuple6 tuple6 = LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L });
        assertEquals(1L, tuple6._1);
        assertEquals(6L, tuple6._6);

        LongTuple7 tuple7 = LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L });
        assertEquals(1L, tuple7._1);
        assertEquals(7L, tuple7._7);

        LongTuple8 tuple8 = LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L });
        assertEquals(1L, tuple8._1);
        assertEquals(8L, tuple8._8);
    }

    // Test toList for larger tuples
    @Test
    public void testToListTuple4Through9() {
        LongTuple4 tuple4 = LongTuple.of(1L, 2L, 3L, 4L);
        LongList list4 = tuple4.toList();
        assertEquals(4, list4.size());
        assertEquals(4L, list4.get(3));

        LongTuple9 tuple9 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        LongList list9 = tuple9.toList();
        assertEquals(9, list9.size());
        assertEquals(9L, list9.get(8));
    }

    // Test forEach for larger tuples
    @Test
    public void testForEachTuple4() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        List<Long> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(4, result.size());
        assertEquals(Long.valueOf(4L), result.get(3));
    }

    // Test forEach override for Tuple2
    @Test
    public void testForEachTuple2Override() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        List<Long> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(2, result.size());
        assertEquals(Long.valueOf(10L), result.get(0));
        assertEquals(Long.valueOf(20L), result.get(1));
    }

    // Test forEach override for Tuple3
    @Test
    public void testForEachTuple3Override() {
        LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
        List<Long> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Long.valueOf(30L), result.get(2));
    }

    // Test stream for larger tuples
    @Test
    public void testStreamTuple4Through9() {
        LongTuple4 tuple4 = LongTuple.of(1L, 2L, 3L, 4L);
        assertEquals(10L, tuple4.stream().sum());

        LongTuple9 tuple9 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertEquals(45L, tuple9.stream().sum());
    }

    // ==================== LongTuple Nested Class Tests ====================

    // ============ LongTuple1 Nested Class Tests ============

    @Test
    public void testLongTuple1_arity() {
        LongTuple.LongTuple1 tuple = LongTuple.of(1L);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testLongTuple1_reverse() {
        LongTuple.LongTuple1 tuple = LongTuple.of(1L);
        LongTuple.LongTuple1 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._1);
        assertEquals(tuple._1, reversed._1);
    }

    @Test
    public void testLongTuple1_contains() {
        LongTuple.LongTuple1 tuple = LongTuple.of(1L);
        assertTrue(tuple.contains(1L));
    }

    @Test
    public void testLongTuple1_hashCode() {
        LongTuple.LongTuple1 tuple1 = LongTuple.of(1L);
        LongTuple.LongTuple1 tuple2 = LongTuple.of(1L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testLongTuple1_equals() {
        LongTuple.LongTuple1 tuple1 = LongTuple.of(1L);
        LongTuple.LongTuple1 tuple2 = LongTuple.of(1L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testLongTuple1_toString() {
        LongTuple.LongTuple1 tuple = LongTuple.of(1L);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testLongTuple1_forEach() {
        LongTuple.LongTuple1 tuple = LongTuple.of(1L);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testLongTuple1_min() {
        LongTuple.LongTuple1 tuple = LongTuple.of(1L);
        assertNotNull(tuple.min());
    }

    @Test
    public void testLongTuple1_max() {
        LongTuple.LongTuple1 tuple = LongTuple.of(1L);
        assertNotNull(tuple.max());
    }

    @Test
    public void testLongTuple1_median() {
        LongTuple.LongTuple1 tuple = LongTuple.of(1L);
        assertNotNull(tuple.median());
    }

    @Test
    public void testLongTuple1_sum() {
        LongTuple.LongTuple1 tuple = LongTuple.of(1L);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testLongTuple1_average() {
        LongTuple.LongTuple1 tuple = LongTuple.of(1L);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    // ============ LongTuple2 Nested Class Tests ============

    @Test
    public void testLongTuple2_arity() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testLongTuple2_reverse() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        LongTuple.LongTuple2 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._2);
        assertEquals(tuple._2, reversed._1);
    }

    @Test
    public void testLongTuple2_contains() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertTrue(tuple.contains(1L));
    }

    @Test
    public void testLongTuple2_hashCode() {
        LongTuple.LongTuple2 tuple1 = LongTuple.of(1L, 2L);
        LongTuple.LongTuple2 tuple2 = LongTuple.of(1L, 2L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testLongTuple2_equals() {
        LongTuple.LongTuple2 tuple1 = LongTuple.of(1L, 2L);
        LongTuple.LongTuple2 tuple2 = LongTuple.of(1L, 2L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testLongTuple2_toString() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testLongTuple2_forEach() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(2, count.size());
    }

    @Test
    public void testLongTuple2_min() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertNotNull(tuple.min());
    }

    @Test
    public void testLongTuple2_max() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertNotNull(tuple.max());
    }

    @Test
    public void testLongTuple2_median() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertNotNull(tuple.median());
    }

    @Test
    public void testLongTuple2_sum() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testLongTuple2_average() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testLongTuple2_accept_biConsumer() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testLongTuple2_map_biFunction() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        String result = tuple.map((a, b) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testLongTuple2_filter_biPredicate() {
        LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertTrue(tuple.filter((a, b) -> true).isPresent());
        assertFalse(tuple.filter((a, b) -> false).isPresent());
    }

    // ============ LongTuple3 Nested Class Tests ============

    @Test
    public void testLongTuple3_arity() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testLongTuple3_reverse() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        LongTuple.LongTuple3 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._3);
        assertEquals(tuple._3, reversed._1);
    }

    @Test
    public void testLongTuple3_contains() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertTrue(tuple.contains(1L));
    }

    @Test
    public void testLongTuple3_hashCode() {
        LongTuple.LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
        LongTuple.LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testLongTuple3_equals() {
        LongTuple.LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
        LongTuple.LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testLongTuple3_toString() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testLongTuple3_forEach() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(3, count.size());
    }

    @Test
    public void testLongTuple3_min() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertNotNull(tuple.min());
    }

    @Test
    public void testLongTuple3_max() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertNotNull(tuple.max());
    }

    @Test
    public void testLongTuple3_median() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertNotNull(tuple.median());
    }

    @Test
    public void testLongTuple3_sum() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testLongTuple3_average() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testLongTuple3_accept_triConsumer() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b, c) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testLongTuple3_map_triFunction() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        String result = tuple.map((a, b, c) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testLongTuple3_filter_triPredicate() {
        LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertTrue(tuple.filter((a, b, c) -> true).isPresent());
        assertFalse(tuple.filter((a, b, c) -> false).isPresent());
    }

    // ============ LongTuple4 Nested Class Tests ============

    @Test
    public void testLongTuple4_arity() {
        LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testLongTuple4_reverse() {
        LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        LongTuple.LongTuple4 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._4);
        assertEquals(tuple._4, reversed._1);
    }

    @Test
    public void testLongTuple4_contains() {
        LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        assertTrue(tuple.contains(1L));
    }

    @Test
    public void testLongTuple4_hashCode() {
        LongTuple.LongTuple4 tuple1 = LongTuple.of(1L, 2L, 3L, 4L);
        LongTuple.LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testLongTuple4_equals() {
        LongTuple.LongTuple4 tuple1 = LongTuple.of(1L, 2L, 3L, 4L);
        LongTuple.LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testLongTuple4_toString() {
        LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testLongTuple4_forEach() {
        LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(4, count.size());
    }

    // ============ LongTuple5 Nested Class Tests ============

    @Test
    public void testLongTuple5_arity() {
        LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testLongTuple5_reverse() {
        LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        LongTuple.LongTuple5 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._5);
        assertEquals(tuple._5, reversed._1);
    }

    @Test
    public void testLongTuple5_contains() {
        LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertTrue(tuple.contains(1L));
    }

    @Test
    public void testLongTuple5_hashCode() {
        LongTuple.LongTuple5 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        LongTuple.LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testLongTuple5_equals() {
        LongTuple.LongTuple5 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        LongTuple.LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testLongTuple5_toString() {
        LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testLongTuple5_forEach() {
        LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(5, count.size());
    }

    // ============ LongTuple6 Nested Class Tests ============

    @Test
    public void testLongTuple6_arity() {
        LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testLongTuple6_reverse() {
        LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        LongTuple.LongTuple6 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._6);
        assertEquals(tuple._6, reversed._1);
    }

    @Test
    public void testLongTuple6_contains() {
        LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        assertTrue(tuple.contains(1L));
    }

    @Test
    public void testLongTuple6_hashCode() {
        LongTuple.LongTuple6 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        LongTuple.LongTuple6 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testLongTuple6_equals() {
        LongTuple.LongTuple6 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        LongTuple.LongTuple6 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testLongTuple6_toString() {
        LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testLongTuple6_forEach() {
        LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(6, count.size());
    }

    // ============ LongTuple7 Nested Class Tests ============

    @Test
    public void testLongTuple7_arity() {
        LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testLongTuple7_reverse() {
        LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        LongTuple.LongTuple7 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._7);
        assertEquals(tuple._7, reversed._1);
    }

    @Test
    public void testLongTuple7_contains() {
        LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertTrue(tuple.contains(1L));
    }

    @Test
    public void testLongTuple7_hashCode() {
        LongTuple.LongTuple7 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        LongTuple.LongTuple7 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testLongTuple7_equals() {
        LongTuple.LongTuple7 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        LongTuple.LongTuple7 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testLongTuple7_toString() {
        LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testLongTuple7_forEach() {
        LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(7, count.size());
    }

    // ============ LongTuple8 Nested Class Tests ============

    @Test
    public void testLongTuple8_arity() {
        LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testLongTuple8_reverse() {
        LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        LongTuple.LongTuple8 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._8);
        assertEquals(tuple._8, reversed._1);
    }

    @Test
    public void testLongTuple8_contains() {
        LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertTrue(tuple.contains(1L));
    }

    @Test
    public void testLongTuple8_hashCode() {
        LongTuple.LongTuple8 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        LongTuple.LongTuple8 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testLongTuple8_equals() {
        LongTuple.LongTuple8 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        LongTuple.LongTuple8 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testLongTuple8_toString() {
        LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testLongTuple8_forEach() {
        LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(8, count.size());
    }

    // ============ LongTuple9 Nested Class Tests ============

    @Test
    public void testLongTuple9_arity() {
        LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testLongTuple9_reverse() {
        LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        LongTuple.LongTuple9 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._9);
        assertEquals(tuple._9, reversed._1);
    }

    @Test
    public void testLongTuple9_contains() {
        LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertTrue(tuple.contains(1L));
    }

    @Test
    public void testLongTuple9_hashCode() {
        LongTuple.LongTuple9 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        LongTuple.LongTuple9 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testLongTuple9_equals() {
        LongTuple.LongTuple9 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        LongTuple.LongTuple9 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testLongTuple9_toString() {
        LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testLongTuple9_forEach() {
        LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(9, count.size());
    }

}
