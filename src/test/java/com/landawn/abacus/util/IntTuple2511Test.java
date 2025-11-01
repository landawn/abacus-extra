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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

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
import com.landawn.abacus.util.u.Optional;

@Tag("2511")
public class IntTuple2511Test extends TestBase {

    // ============ Factory Method Tests - IntTuple.of() ============

    @Test
    public void testOf_tuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertNotNull(tuple);
        assertEquals(42, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf_tuple2() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        assertNotNull(tuple);
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertNotNull(tuple);
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(3, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf_tuple4() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertNotNull(tuple);
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(3, tuple._3);
        assertEquals(4, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf_tuple5() {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        assertNotNull(tuple);
        assertEquals(1, tuple._1);
        assertEquals(5, tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf_tuple6() {
        IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertNotNull(tuple);
        assertEquals(1, tuple._1);
        assertEquals(6, tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf_tuple7() {
        IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertNotNull(tuple);
        assertEquals(1, tuple._1);
        assertEquals(7, tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf_tuple8() {
        IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertNotNull(tuple);
        assertEquals(1, tuple._1);
        assertEquals(8, tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf_tuple9() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertNotNull(tuple);
        assertEquals(1, tuple._1);
        assertEquals(9, tuple._9);
        assertEquals(9, tuple.arity());
    }

    // ============ Create from Array Tests ============

    @Test
    public void testCreate_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_null() {
        IntTuple0 tuple = IntTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_single() {
        IntTuple1 tuple = IntTuple.create(new int[] { 100 });
        assertNotNull(tuple);
        assertEquals(100, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate_two() {
        IntTuple2 tuple = IntTuple.create(new int[] { 10, 20 });
        assertNotNull(tuple);
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
    }

    @Test
    public void testCreate_nine() {
        IntTuple9 tuple = IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        assertNotNull(tuple);
        assertEquals(1, tuple._1);
        assertEquals(9, tuple._9);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testCreate_tooMany() {
        assertThrows(IllegalArgumentException.class, () -> {
            IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        });
    }

    // ============ Statistics Tests - Min/Max/Median/Sum/Average ============

    @Test
    public void testMin_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void testMin_single() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.min());
    }

    @Test
    public void testMin_multiple() {
        IntTuple3 tuple = IntTuple.of(100, 5, 50);
        assertEquals(5, tuple.min());
    }

    @Test
    public void testMin_negatives() {
        IntTuple3 tuple = IntTuple.of(-10, -50, -20);
        assertEquals(-50, tuple.min());
    }

    @Test
    public void testMax_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void testMax_single() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.max());
    }

    @Test
    public void testMax_multiple() {
        IntTuple3 tuple = IntTuple.of(100, 5, 50);
        assertEquals(100, tuple.max());
    }

    @Test
    public void testMax_negatives() {
        IntTuple3 tuple = IntTuple.of(-10, -50, -20);
        assertEquals(-10, tuple.max());
    }

    @Test
    public void testMedian_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void testMedian_single() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.median());
    }

    @Test
    public void testMedian_odd() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(2, tuple.median());
    }

    @Test
    public void testMedian_even() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertEquals(2, tuple.median()); // Lower middle for even length
    }

    @Test
    public void testSum_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void testSum_single() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.sum());
    }

    @Test
    public void testSum_multiple() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        assertEquals(60, tuple.sum());
    }

    @Test
    public void testSum_negatives() {
        IntTuple3 tuple = IntTuple.of(10, -5, 15);
        assertEquals(20, tuple.sum());
    }

    @Test
    public void testAverage_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void testAverage_single() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42.0, tuple.average());
    }

    @Test
    public void testAverage_multiple() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        assertEquals(20.0, tuple.average());
    }

    @Test
    public void testAverage_odd() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(2.0, tuple.average());
    }

    // ============ Reverse Tests ============

    @Test
    public void testReverse_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        IntTuple0 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testReverse_single() {
        IntTuple1 tuple = IntTuple.of(42);
        IntTuple1 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(42, reversed._1);
        assertNotSame(tuple, reversed); // Should be new instance
    }

    @Test
    public void testReverse_multiple() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        IntTuple3 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(3, reversed._1);
        assertEquals(2, reversed._2);
        assertEquals(1, reversed._3);
    }

    @Test
    public void testReverse_twoElements() {
        IntTuple2 tuple = IntTuple.of(100, 200);
        IntTuple2 reversed = tuple.reverse();
        assertEquals(200, reversed._1);
        assertEquals(100, reversed._2);
    }

    @Test
    public void testReverse_largeValues() {
        IntTuple4 tuple = IntTuple.of(Integer.MAX_VALUE, 0, -1, Integer.MIN_VALUE);
        IntTuple4 reversed = tuple.reverse();
        assertEquals(Integer.MIN_VALUE, reversed._1);
        assertEquals(-1, reversed._2);
        assertEquals(0, reversed._3);
        assertEquals(Integer.MAX_VALUE, reversed._4);
    }

    // ============ Contains Tests ============

    @Test
    public void testContains_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertFalse(tuple.contains(42));
    }

    @Test
    public void testContains_single_found() {
        IntTuple1 tuple = IntTuple.of(42);
        assertTrue(tuple.contains(42));
    }

    @Test
    public void testContains_single_notfound() {
        IntTuple1 tuple = IntTuple.of(42);
        assertFalse(tuple.contains(99));
    }

    @Test
    public void testContains_multiple_found() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        assertTrue(tuple.contains(10));
        assertTrue(tuple.contains(20));
        assertTrue(tuple.contains(30));
    }

    @Test
    public void testContains_multiple_notfound() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        assertFalse(tuple.contains(99));
    }

    @Test
    public void testContains_negatives() {
        IntTuple3 tuple = IntTuple.of(-10, 0, 10);
        assertTrue(tuple.contains(-10));
        assertTrue(tuple.contains(0));
        assertTrue(tuple.contains(10));
        assertFalse(tuple.contains(5));
    }

    // ============ Functional Methods - IntTuple2 ============

    @Test
    public void testAccept_tuple2() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        List<Integer> captured = new ArrayList<>();
        tuple.accept((a, b) -> {
            captured.add(a);
            captured.add(b);
        });
        assertEquals(2, captured.size());
        assertEquals(3, captured.get(0));
        assertEquals(4, captured.get(1));
    }

    @Test
    public void testMap_tuple2() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        int result = tuple.map((a, b) -> a * b);
        assertEquals(12, result);
    }

    @Test
    public void testMap_tuple2_addition() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        int result = tuple.map((a, b) -> a + b);
        assertEquals(30, result);
    }

    @Test
    public void testFilter_tuple2_passes() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testFilter_tuple2_fails() {
        IntTuple2 tuple = IntTuple.of(1, 1);
        Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 10);
        assertFalse(result.isPresent());
    }

    // ============ Functional Methods - IntTuple3 ============

    @Test
    public void testAccept_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        List<Integer> captured = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            captured.add(a);
            captured.add(b);
            captured.add(c);
        });
        assertEquals(3, captured.size());
        assertEquals(1, captured.get(0));
        assertEquals(2, captured.get(1));
        assertEquals(3, captured.get(2));
    }

    @Test
    public void testMap_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6, result);
    }

    @Test
    public void testMap_tuple3_sum() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        int result = tuple.map((a, b, c) -> a + b + c);
        assertEquals(60, result);
    }

    @Test
    public void testFilter_tuple3_passes() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        Optional<IntTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testFilter_tuple3_fails() {
        IntTuple3 tuple = IntTuple.of(1, 1, 1);
        Optional<IntTuple3> result = tuple.filter((a, b, c) -> a + b + c > 10);
        assertFalse(result.isPresent());
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(i -> count.incrementAndGet());
        assertEquals(0, count.get());
    }

    @Test
    public void testForEach_tuple2() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        List<Integer> visited = new ArrayList<>();
        tuple.forEach(visited::add);
        assertEquals(2, visited.size());
        assertEquals(10, visited.get(0));
        assertEquals(20, visited.get(1));
    }

    @Test
    public void testForEach_tuple3() {
        IntTuple3 tuple = IntTuple.of(100, 200, 300);
        List<Integer> visited = new ArrayList<>();
        tuple.forEach(visited::add);
        assertEquals(3, visited.size());
        assertEquals(100, visited.get(0));
        assertEquals(200, visited.get(1));
        assertEquals(300, visited.get(2));
    }

    @Test
    public void testForEach_sum() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        AtomicInteger sum = new AtomicInteger(0);
        tuple.forEach(sum::addAndGet);
        assertEquals(10, sum.get());
    }

    // ============ ToArray Tests ============

    @Test
    public void testToArray_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        int[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void testToArray_single() {
        IntTuple1 tuple = IntTuple.of(42);
        int[] array = tuple.toArray();
        assertArrayEquals(new int[] { 42 }, array);
    }

    @Test
    public void testToArray_multiple() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        int[] array = tuple.toArray();
        assertArrayEquals(new int[] { 10, 20, 30 }, array);
    }

    @Test
    public void testToArray_independence() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        int[] array1 = tuple.toArray();
        int[] array2 = tuple.toArray();
        assertNotSame(array1, array2); // Should be independent copies
        assertArrayEquals(array1, array2);
    }

    // ============ ToList Tests ============

    @Test
    public void testToList_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        IntList list = tuple.toList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testToList_single() {
        IntTuple1 tuple = IntTuple.of(42);
        IntList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals(42, list.get(0));
    }

    @Test
    public void testToList_multiple() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        IntList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    // ============ Stream Tests ============

    @Test
    public void testStream_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        int sum = tuple.stream().sum();
        assertEquals(0, sum);
    }

    @Test
    public void testStream_single() {
        IntTuple1 tuple = IntTuple.of(42);
        int sum = tuple.stream().sum();
        assertEquals(42, sum);
    }

    @Test
    public void testStream_multiple() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        int sum = tuple.stream().sum();
        assertEquals(60, sum);
    }

    @Test
    public void testStream_filter() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        long count = tuple.stream().filter(i -> i > 2).count();
        assertEquals(2, count);
    }

    // ============ Equality and HashCode Tests ============

    @Test
    public void testEquals_sameInstance() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEquals_sameValues() {
        IntTuple2 tuple1 = IntTuple.of(10, 20);
        IntTuple2 tuple2 = IntTuple.of(10, 20);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_differentValues() {
        IntTuple2 tuple1 = IntTuple.of(10, 20);
        IntTuple2 tuple2 = IntTuple.of(30, 40);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_differentTypes() {
        IntTuple1 tuple1 = IntTuple.of(10);
        IntTuple2 tuple2 = IntTuple.of(10, 20);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_null() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        assertNotEquals(tuple, null);
    }

    @Test
    public void testEquals_otherObject() {
        IntTuple2 tuple = IntTuple.of(10, 20);
        assertNotEquals(tuple, "10,20");
    }

    @Test
    public void testHashCode_sameValues() {
        IntTuple2 tuple1 = IntTuple.of(10, 20);
        IntTuple2 tuple2 = IntTuple.of(10, 20);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_differentValues() {
        IntTuple2 tuple1 = IntTuple.of(10, 20);
        IntTuple2 tuple2 = IntTuple.of(30, 40);
        assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertNotNull(tuple.hashCode());
    }

    // ============ ToString Tests ============

    @Test
    public void testToString_empty() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertEquals("()", tuple.toString());
    }

    @Test
    public void testToString_single() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals("(42)", tuple.toString());
    }

    @Test
    public void testToString_multiple() {
        IntTuple3 tuple = IntTuple.of(10, 20, 30);
        assertEquals("(10, 20, 30)", tuple.toString());
    }

    // ============ Arity Tests ============

    @Test
    public void testArity_0() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testArity_1() {
        IntTuple1 tuple = IntTuple.of(1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testArity_2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testArity_3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testArity_4() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testArity_5() {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testArity_6() {
        IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testArity_7() {
        IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testArity_8() {
        IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testArity_9() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(9, tuple.arity());
    }

    // ============ Field Accessors Tests ============

    @Test
    public void testFieldAccessors_tuple5() {
        IntTuple5 tuple = IntTuple.of(10, 20, 30, 40, 50);
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(30, tuple._3);
        assertEquals(40, tuple._4);
        assertEquals(50, tuple._5);
    }

    @Test
    public void testFieldAccessors_tuple9() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(3, tuple._3);
        assertEquals(4, tuple._4);
        assertEquals(5, tuple._5);
        assertEquals(6, tuple._6);
        assertEquals(7, tuple._7);
        assertEquals(8, tuple._8);
        assertEquals(9, tuple._9);
    }

    // ============ Complex Statistics Tests ============

    @Test
    public void testStatistics_allOperations() {
        IntTuple5 tuple = IntTuple.of(100, 500, 300, 200, 400);
        assertEquals(100, tuple.min());
        assertEquals(500, tuple.max());
        assertEquals(300, tuple.median());
        assertEquals(1500, tuple.sum());
        assertEquals(300.0, tuple.average());
    }

    @Test
    public void testContains_allElements() {
        IntTuple5 tuple = IntTuple.of(10, 20, 30, 40, 50);
        assertTrue(tuple.contains(10));
        assertTrue(tuple.contains(20));
        assertTrue(tuple.contains(30));
        assertTrue(tuple.contains(40));
        assertTrue(tuple.contains(50));
        assertFalse(tuple.contains(99));
    }

    // ============ Edge Cases Tests ============

    @Test
    public void testSingleElement_allOperations() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.min());
        assertEquals(42, tuple.max());
        assertEquals(42, tuple.median());
        assertEquals(42.0, tuple.average());
        assertEquals(42, tuple.sum());
        assertTrue(tuple.contains(42));
        assertFalse(tuple.contains(99));
        IntTuple1 reversed = tuple.reverse();
        assertEquals(42, reversed._1);
    }

    @Test
    public void testExtremeValues() {
        IntTuple3 tuple = IntTuple.of(Integer.MIN_VALUE, 0, Integer.MAX_VALUE);
        assertEquals(Integer.MIN_VALUE, tuple.min());
        assertEquals(Integer.MAX_VALUE, tuple.max());
        assertTrue(tuple.contains(0));
    }

    @Test
    public void testZeroes() {
        IntTuple4 tuple = IntTuple.of(0, 0, 0, 0);
        assertEquals(0, tuple.min());
        assertEquals(0, tuple.max());
        assertEquals(0, tuple.sum());
        assertEquals(0.0, tuple.average());
        assertTrue(tuple.contains(0));
    }

    @Test
    public void testMixedSignes() {
        IntTuple5 tuple = IntTuple.of(-100, -50, 0, 50, 100);
        assertEquals(-100, tuple.min());
        assertEquals(100, tuple.max());
        assertEquals(0, tuple.sum());
        assertEquals(0.0, tuple.average());
        assertTrue(tuple.contains(-100));
        assertTrue(tuple.contains(100));
    }

    @Test
    public void testLargeSum() {
        IntTuple3 tuple = IntTuple.of(1000000, 2000000, 3000000);
        assertEquals(6000000, tuple.sum());
        assertEquals(2000000.0, tuple.average());
    }
}
