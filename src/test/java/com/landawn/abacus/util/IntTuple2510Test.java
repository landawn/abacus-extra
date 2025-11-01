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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
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

@Tag("2510")
public class IntTuple2510Test extends TestBase {

    @Test
    public void testOf1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(1, tuple.arity());
        assertEquals(42, tuple._1);
    }

    @Test
    public void testOf2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertEquals(2, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
    }

    @Test
    public void testOf3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(3, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(3, tuple._3);
    }

    @Test
    public void testOf4() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertEquals(4, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(3, tuple._3);
        assertEquals(4, tuple._4);
    }

    @Test
    public void testOf5() {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        assertEquals(5, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(5, tuple._5);
    }

    @Test
    public void testOf6() {
        IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertEquals(6, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(6, tuple._6);
    }

    @Test
    public void testOf7() {
        IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertEquals(7, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(7, tuple._7);
    }

    @Test
    public void testOf8() {
        IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(8, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(8, tuple._8);
    }

    @Test
    public void testOf9() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(9, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(9, tuple._9);
    }

    @Test
    public void testCreate_nullArray() {
        IntTuple<?> tuple = IntTuple.create(null);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_emptyArray() {
        IntTuple<?> tuple = IntTuple.create(new int[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_sizeOne() {
        IntTuple1 tuple = IntTuple.create(new int[] { 42 });
        assertEquals(1, tuple.arity());
        assertEquals(42, tuple._1);
    }

    @Test
    public void testCreate_sizeTwo() {
        IntTuple2 tuple = IntTuple.create(new int[] { 1, 2 });
        assertEquals(2, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
    }

    @Test
    public void testCreate_sizeThree() {
        IntTuple3 tuple = IntTuple.create(new int[] { 1, 2, 3 });
        assertEquals(3, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(3, tuple._3);
    }

    @Test
    public void testCreate_sizeFour() {
        IntTuple4 tuple = IntTuple.create(new int[] { 1, 2, 3, 4 });
        assertEquals(4, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(4, tuple._4);
    }

    @Test
    public void testCreate_sizeFive() {
        IntTuple5 tuple = IntTuple.create(new int[] { 1, 2, 3, 4, 5 });
        assertEquals(5, tuple.arity());
        assertEquals(5, tuple._5);
    }

    @Test
    public void testCreate_sizeSix() {
        IntTuple6 tuple = IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6 });
        assertEquals(6, tuple.arity());
        assertEquals(6, tuple._6);
    }

    @Test
    public void testCreate_sizeSeven() {
        IntTuple7 tuple = IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6, 7 });
        assertEquals(7, tuple.arity());
        assertEquals(7, tuple._7);
    }

    @Test
    public void testCreate_sizeEight() {
        IntTuple8 tuple = IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 });
        assertEquals(8, tuple.arity());
        assertEquals(8, tuple._8);
    }

    @Test
    public void testCreate_sizeNine() {
        IntTuple9 tuple = IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        assertEquals(9, tuple.arity());
        assertEquals(9, tuple._9);
    }

    @Test
    public void testCreate_tooManyElements() {
        assertThrows(IllegalArgumentException.class, () -> IntTuple.create(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
    }

    @Test
    public void testMin_tuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.min());
    }

    @Test
    public void testMin_tuple2() {
        IntTuple2 tuple = IntTuple.of(5, 2);
        assertEquals(2, tuple.min());
    }

    @Test
    public void testMin_tuple3() {
        IntTuple3 tuple = IntTuple.of(5, 2, 8);
        assertEquals(2, tuple.min());
    }

    @Test
    public void testMin_tuple4() {
        IntTuple4 tuple = IntTuple.of(5, 2, 8, 1);
        assertEquals(1, tuple.min());
    }

    @Test
    public void testMax_tuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.max());
    }

    @Test
    public void testMax_tuple2() {
        IntTuple2 tuple = IntTuple.of(5, 2);
        assertEquals(5, tuple.max());
    }

    @Test
    public void testMax_tuple3() {
        IntTuple3 tuple = IntTuple.of(5, 2, 8);
        assertEquals(8, tuple.max());
    }

    @Test
    public void testMedian_tuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.median());
    }

    @Test
    public void testMedian_tuple2() {
        IntTuple2 tuple = IntTuple.of(5, 2);
        assertEquals(2, tuple.median());
    }

    @Test
    public void testMedian_tuple3() {
        IntTuple3 tuple = IntTuple.of(3, 1, 2);
        assertEquals(2, tuple.median());
    }

    @Test
    public void testMedian_tuple4() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertEquals(2, tuple.median());
    }

    @Test
    public void testSum_tuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42, tuple.sum());
    }

    @Test
    public void testSum_tuple2() {
        IntTuple2 tuple = IntTuple.of(5, 2);
        assertEquals(7, tuple.sum());
    }

    @Test
    public void testSum_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(6, tuple.sum());
    }

    @Test
    public void testAverage_tuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(42.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverage_tuple2() {
        IntTuple2 tuple = IntTuple.of(4, 6);
        assertEquals(5.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverage_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(2.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverage_tuple4() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertEquals(2.5, tuple.average(), 0.001);
    }

    @Test
    public void testReverse_tuple1() {
        IntTuple1 tuple = IntTuple.of(1);
        IntTuple1 reversed = tuple.reverse();
        assertEquals(1, reversed._1);
    }

    @Test
    public void testReverse_tuple2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        IntTuple2 reversed = tuple.reverse();
        assertEquals(2, reversed._1);
        assertEquals(1, reversed._2);
    }

    @Test
    public void testReverse_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        IntTuple3 reversed = tuple.reverse();
        assertEquals(3, reversed._1);
        assertEquals(2, reversed._2);
        assertEquals(1, reversed._3);
    }

    @Test
    public void testReverse_tuple4() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        IntTuple4 reversed = tuple.reverse();
        assertEquals(4, reversed._1);
        assertEquals(1, reversed._4);
    }

    @Test
    public void testContains_tuple1_found() {
        IntTuple1 tuple = IntTuple.of(42);
        assertTrue(tuple.contains(42));
    }

    @Test
    public void testContains_tuple1_notFound() {
        IntTuple1 tuple = IntTuple.of(42);
        assertFalse(tuple.contains(100));
    }

    @Test
    public void testContains_tuple2_found() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertTrue(tuple.contains(2));
    }

    @Test
    public void testContains_tuple2_notFound() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertFalse(tuple.contains(5));
    }

    @Test
    public void testContains_tuple3_found() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertTrue(tuple.contains(2));
    }

    @Test
    public void testContains_tuple3_notFound() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertFalse(tuple.contains(5));
    }

    @Test
    public void testToArray_tuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        int[] array = tuple.toArray();
        assertArrayEquals(new int[] { 42 }, array);
    }

    @Test
    public void testToArray_tuple2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        int[] array = tuple.toArray();
        assertArrayEquals(new int[] { 1, 2 }, array);
    }

    @Test
    public void testToArray_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int[] array = tuple.toArray();
        assertArrayEquals(new int[] { 1, 2, 3 }, array);
    }

    @Test
    public void testToList_tuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        IntList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals(42, list.get(0));
    }

    @Test
    public void testToList_tuple2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        IntList list = tuple.toList();
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    public void testToList_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        IntList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(3, list.get(2));
    }

    @Test
    public void testForEach_tuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        AtomicInteger sum = new AtomicInteger(0);
        tuple.forEach(value -> sum.addAndGet(value));
        assertEquals(42, sum.get());
    }

    @Test
    public void testForEach_tuple2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        AtomicInteger sum = new AtomicInteger(0);
        tuple.forEach(value -> sum.addAndGet(value));
        assertEquals(3, sum.get());
    }

    @Test
    public void testForEach_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        AtomicInteger sum = new AtomicInteger(0);
        tuple.forEach(value -> sum.addAndGet(value));
        assertEquals(6, sum.get());
    }

    @Test
    public void testForEach_tuple4() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        AtomicInteger sum = new AtomicInteger(0);
        tuple.forEach(value -> sum.addAndGet(value));
        assertEquals(10, sum.get());
    }

    @Test
    public void testStream_tuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        int sum = tuple.stream().sum();
        assertEquals(42, sum);
    }

    @Test
    public void testStream_tuple2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        int sum = tuple.stream().sum();
        assertEquals(3, sum);
    }

    @Test
    public void testStream_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int sum = tuple.stream().sum();
        assertEquals(6, sum);
    }

    @Test
    public void testHashCode_tuple1() {
        IntTuple1 tuple1 = IntTuple.of(42);
        IntTuple1 tuple2 = IntTuple.of(42);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_tuple1_different() {
        IntTuple1 tuple1 = IntTuple.of(42);
        IntTuple1 tuple2 = IntTuple.of(100);
        assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_tuple2() {
        IntTuple2 tuple1 = IntTuple.of(1, 2);
        IntTuple2 tuple2 = IntTuple.of(1, 2);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_tuple3() {
        IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
        IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testEquals_tuple1_same() {
        IntTuple1 tuple1 = IntTuple.of(42);
        IntTuple1 tuple2 = IntTuple.of(42);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple1_sameObject() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEquals_tuple1_different() {
        IntTuple1 tuple1 = IntTuple.of(42);
        IntTuple1 tuple2 = IntTuple.of(100);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple1_null() {
        IntTuple1 tuple = IntTuple.of(42);
        assertNotEquals(null, tuple);
    }

    @Test
    public void testEquals_tuple2_same() {
        IntTuple2 tuple1 = IntTuple.of(1, 2);
        IntTuple2 tuple2 = IntTuple.of(1, 2);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple2_different() {
        IntTuple2 tuple1 = IntTuple.of(1, 2);
        IntTuple2 tuple2 = IntTuple.of(1, 3);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple3_same() {
        IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
        IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple3_different() {
        IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
        IntTuple3 tuple2 = IntTuple.of(1, 2, 4);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple4() {
        IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
        IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple5() {
        IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
        IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testToString_tuple1() {
        IntTuple1 tuple = IntTuple.of(42);
        assertEquals("(42)", tuple.toString());
    }

    @Test
    public void testToString_tuple2() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertEquals("(1, 2)", tuple.toString());
    }

    @Test
    public void testToString_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals("(1, 2, 3)", tuple.toString());
    }

    @Test
    public void testAccept_tuple2() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        AtomicInteger result = new AtomicInteger(0);
        tuple.accept((a, b) -> result.set(a * b));
        assertEquals(12, result.get());
    }

    @Test
    public void testMap_tuple2() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        int result = tuple.map((a, b) -> a * b);
        assertEquals(12, result);
    }

    @Test
    public void testFilter_tuple2_match() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testFilter_tuple2_noMatch() {
        IntTuple2 tuple = IntTuple.of(3, 4);
        Optional<IntTuple2> result = tuple.filter((a, b) -> a > b);
        assertFalse(result.isPresent());
    }

    @Test
    public void testAccept_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        AtomicInteger result = new AtomicInteger(0);
        tuple.accept((a, b, c) -> result.set(a + b + c));
        assertEquals(6, result.get());
    }

    @Test
    public void testMap_tuple3() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6, result);
    }

    @Test
    public void testFilter_tuple3_match() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        Optional<IntTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFilter_tuple3_noMatch() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        Optional<IntTuple3> result = tuple.filter((a, b, c) -> a > b);
        assertFalse(result.isPresent());
    }

    @Test
    public void testTuple0_arity() {
        IntTuple<?> tuple = IntTuple.create(null);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testTuple0_min() {
        IntTuple<?> tuple = IntTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void testTuple0_max() {
        IntTuple<?> tuple = IntTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void testTuple0_median() {
        IntTuple<?> tuple = IntTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void testTuple0_sum() {
        IntTuple<?> tuple = IntTuple.create(null);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void testTuple0_average() {
        IntTuple<?> tuple = IntTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void testTuple0_reverse() {
        IntTuple<?> tuple = IntTuple.create(null);
        assertNotNull(tuple.reverse());
    }

    @Test
    public void testTuple0_contains() {
        IntTuple<?> tuple = IntTuple.create(null);
        assertFalse(tuple.contains(1));
    }

    @Test
    public void testTuple0_toString() {
        IntTuple<?> tuple = IntTuple.create(null);
        assertEquals("()", tuple.toString());
    }

    @Test
    public void testTuple5_operations() {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        assertEquals(5, tuple.arity());
        assertEquals(1, tuple.min());
        assertEquals(5, tuple.max());
        assertEquals(3, tuple.median());
        assertEquals(15, tuple.sum());
        assertEquals(3.0, tuple.average(), 0.001);
        assertTrue(tuple.contains(3));
        assertFalse(tuple.contains(10));
    }

    @Test
    public void testTuple5_reverse() {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        IntTuple5 reversed = tuple.reverse();
        assertEquals(5, reversed._1);
        assertEquals(4, reversed._2);
        assertEquals(3, reversed._3);
        assertEquals(2, reversed._4);
        assertEquals(1, reversed._5);
    }

    @Test
    public void testTuple6_operations() {
        IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertEquals(6, tuple.arity());
        assertEquals(1, tuple.min());
        assertEquals(6, tuple.max());
        assertEquals(21, tuple.sum());
    }

    @Test
    public void testTuple7_operations() {
        IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertEquals(7, tuple.arity());
        assertEquals(4, tuple.median());
        assertEquals(28, tuple.sum());
    }

    @Test
    public void testTuple8_operations() {
        IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(8, tuple.arity());
        assertEquals(8, tuple.max());
        assertEquals(36, tuple.sum());
    }

    @Test
    public void testTuple9_operations() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(9, tuple.arity());
        assertEquals(5, tuple.median());
        assertEquals(45, tuple.sum());
        assertEquals(5.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple9_reverse() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        IntTuple9 reversed = tuple.reverse();
        assertEquals(9, reversed._1);
        assertEquals(1, reversed._9);
    }

    @Test
    public void testForEach_tuple5() {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(value -> count.incrementAndGet());
        assertEquals(5, count.get());
    }

    @Test
    public void testForEach_tuple6() {
        IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(value -> count.incrementAndGet());
        assertEquals(6, count.get());
    }

    @Test
    public void testForEach_tuple7() {
        IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(value -> count.incrementAndGet());
        assertEquals(7, count.get());
    }

    @Test
    public void testForEach_tuple8() {
        IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(value -> count.incrementAndGet());
        assertEquals(8, count.get());
    }

    @Test
    public void testForEach_tuple9() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(value -> count.incrementAndGet());
        assertEquals(9, count.get());
    }

    @Test
    public void testContains_tuple4() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertTrue(tuple.contains(1));
        assertTrue(tuple.contains(4));
        assertFalse(tuple.contains(5));
    }

    @Test
    public void testContains_tuple5() {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        assertTrue(tuple.contains(5));
        assertFalse(tuple.contains(10));
    }

    @Test
    public void testContains_tuple6() {
        IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertTrue(tuple.contains(6));
        assertFalse(tuple.contains(7));
    }

    @Test
    public void testContains_tuple7() {
        IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertTrue(tuple.contains(7));
        assertFalse(tuple.contains(8));
    }

    @Test
    public void testContains_tuple8() {
        IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertTrue(tuple.contains(8));
        assertFalse(tuple.contains(9));
    }

    @Test
    public void testContains_tuple9() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertTrue(tuple.contains(9));
        assertFalse(tuple.contains(10));
    }

    @Test
    public void testHashCode_tuple4() {
        IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
        IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_tuple5() {
        IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
        IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }
}
