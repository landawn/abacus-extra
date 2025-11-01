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
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.LongTuple.LongTuple1;
import com.landawn.abacus.util.LongTuple.LongTuple2;
import com.landawn.abacus.util.LongTuple.LongTuple3;
import com.landawn.abacus.util.LongTuple.LongTuple4;
import com.landawn.abacus.util.LongTuple.LongTuple5;
import com.landawn.abacus.util.LongTuple.LongTuple6;
import com.landawn.abacus.util.LongTuple.LongTuple7;
import com.landawn.abacus.util.LongTuple.LongTuple8;
import com.landawn.abacus.util.LongTuple.LongTuple9;
import com.landawn.abacus.util.u.Optional;

@Tag("2510")
public class LongTuple2510Test extends TestBase {

    @Test
    public void testOf1() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertEquals(1, tuple.arity());
        assertEquals(42L, tuple._1);
    }

    @Test
    public void testOf2() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        assertEquals(2, tuple.arity());
        assertEquals(10L, tuple._1);
        assertEquals(20L, tuple._2);
    }

    @Test
    public void testOf3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(3, tuple.arity());
        assertEquals(1L, tuple._1);
        assertEquals(2L, tuple._2);
        assertEquals(3L, tuple._3);
    }

    @Test
    public void testOf4() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        assertEquals(4, tuple.arity());
        assertEquals(1L, tuple._1);
        assertEquals(2L, tuple._2);
        assertEquals(3L, tuple._3);
        assertEquals(4L, tuple._4);
    }

    @Test
    public void testOf5() {
        LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertEquals(5, tuple.arity());
        assertEquals(1L, tuple._1);
        assertEquals(5L, tuple._5);
    }

    @Test
    public void testOf6() {
        LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        assertEquals(6, tuple.arity());
        assertEquals(1L, tuple._1);
        assertEquals(6L, tuple._6);
    }

    @Test
    public void testOf7() {
        LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertEquals(7, tuple.arity());
        assertEquals(1L, tuple._1);
        assertEquals(7L, tuple._7);
    }

    @Test
    public void testOf8() {
        LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertEquals(8, tuple.arity());
        assertEquals(1L, tuple._1);
        assertEquals(8L, tuple._8);
    }

    @Test
    public void testOf9() {
        LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertEquals(9, tuple.arity());
        assertEquals(1L, tuple._1);
        assertEquals(9L, tuple._9);
    }

    @Test
    public void testCreate_nullArray() {
        LongTuple<?> tuple = LongTuple.create(null);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_emptyArray() {
        LongTuple<?> tuple = LongTuple.create(new long[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_sizeOne() {
        LongTuple1 tuple = LongTuple.create(new long[] { 42L });
        assertEquals(1, tuple.arity());
        assertEquals(42L, tuple._1);
    }

    @Test
    public void testCreate_sizeTwo() {
        LongTuple2 tuple = LongTuple.create(new long[] { 10L, 20L });
        assertEquals(2, tuple.arity());
        assertEquals(10L, tuple._1);
        assertEquals(20L, tuple._2);
    }

    @Test
    public void testCreate_sizeThree() {
        LongTuple3 tuple = LongTuple.create(new long[] { 1L, 2L, 3L });
        assertEquals(3, tuple.arity());
        assertEquals(1L, tuple._1);
        assertEquals(3L, tuple._3);
    }

    @Test
    public void testCreate_sizeFour() {
        LongTuple4 tuple = LongTuple.create(new long[] { 1L, 2L, 3L, 4L });
        assertEquals(4, tuple.arity());
        assertEquals(1L, tuple._1);
        assertEquals(4L, tuple._4);
    }

    @Test
    public void testCreate_sizeFive() {
        LongTuple5 tuple = LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L });
        assertEquals(5, tuple.arity());
        assertEquals(5L, tuple._5);
    }

    @Test
    public void testCreate_sizeSix() {
        LongTuple6 tuple = LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L });
        assertEquals(6, tuple.arity());
        assertEquals(6L, tuple._6);
    }

    @Test
    public void testCreate_sizeSeven() {
        LongTuple7 tuple = LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L });
        assertEquals(7, tuple.arity());
        assertEquals(7L, tuple._7);
    }

    @Test
    public void testCreate_sizeEight() {
        LongTuple8 tuple = LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L });
        assertEquals(8, tuple.arity());
        assertEquals(8L, tuple._8);
    }

    @Test
    public void testCreate_sizeNine() {
        LongTuple9 tuple = LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L });
        assertEquals(9, tuple.arity());
        assertEquals(9L, tuple._9);
    }

    @Test
    public void testCreate_tooManyElements() {
        assertThrows(IllegalArgumentException.class, () -> LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L }));
    }

    @Test
    public void testMin_tuple1() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertEquals(42L, tuple.min());
    }

    @Test
    public void testMin_tuple2() {
        LongTuple2 tuple = LongTuple.of(5L, 2L);
        assertEquals(2L, tuple.min());
    }

    @Test
    public void testMin_tuple3() {
        LongTuple3 tuple = LongTuple.of(5L, 2L, 8L);
        assertEquals(2L, tuple.min());
    }

    @Test
    public void testMin_tuple4() {
        LongTuple4 tuple = LongTuple.of(5L, 2L, 8L, 1L);
        assertEquals(1L, tuple.min());
    }

    @Test
    public void testMax_tuple1() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertEquals(42L, tuple.max());
    }

    @Test
    public void testMax_tuple2() {
        LongTuple2 tuple = LongTuple.of(5L, 2L);
        assertEquals(5L, tuple.max());
    }

    @Test
    public void testMax_tuple3() {
        LongTuple3 tuple = LongTuple.of(5L, 2L, 8L);
        assertEquals(8L, tuple.max());
    }

    @Test
    public void testMedian_tuple1() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertEquals(42L, tuple.median());
    }

    @Test
    public void testMedian_tuple2() {
        LongTuple2 tuple = LongTuple.of(5L, 2L);
        assertEquals(2L, tuple.median());
    }

    @Test
    public void testMedian_tuple3() {
        LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
        assertEquals(2L, tuple.median());
    }

    @Test
    public void testMedian_tuple4() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        assertEquals(2L, tuple.median());
    }

    @Test
    public void testSum_tuple1() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertEquals(42L, tuple.sum());
    }

    @Test
    public void testSum_tuple2() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        assertEquals(30L, tuple.sum());
    }

    @Test
    public void testSum_tuple3() {
        LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
        assertEquals(60L, tuple.sum());
    }

    @Test
    public void testAverage_tuple1() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertEquals(42.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverage_tuple2() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        assertEquals(15.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverage_tuple3() {
        LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
        assertEquals(20.0, tuple.average(), 0.001);
    }

    @Test
    public void testReverse_tuple1() {
        LongTuple1 tuple = LongTuple.of(1L);
        LongTuple1 reversed = tuple.reverse();
        assertEquals(1L, reversed._1);
    }

    @Test
    public void testReverse_tuple2() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        LongTuple2 reversed = tuple.reverse();
        assertEquals(2L, reversed._1);
        assertEquals(1L, reversed._2);
    }

    @Test
    public void testReverse_tuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        LongTuple3 reversed = tuple.reverse();
        assertEquals(3L, reversed._1);
        assertEquals(2L, reversed._2);
        assertEquals(1L, reversed._3);
    }

    @Test
    public void testReverse_tuple4() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        LongTuple4 reversed = tuple.reverse();
        assertEquals(4L, reversed._1);
        assertEquals(1L, reversed._4);
    }

    @Test
    public void testContains_tuple1_found() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertTrue(tuple.contains(42L));
    }

    @Test
    public void testContains_tuple1_notFound() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertFalse(tuple.contains(100L));
    }

    @Test
    public void testContains_tuple2_found() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        assertTrue(tuple.contains(20L));
    }

    @Test
    public void testContains_tuple2_notFound() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        assertFalse(tuple.contains(40L));
    }

    @Test
    public void testContains_tuple3_found() {
        LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
        assertTrue(tuple.contains(20L));
    }

    @Test
    public void testContains_tuple3_notFound() {
        LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
        assertFalse(tuple.contains(40L));
    }

    @Test
    public void testToArray_tuple1() {
        LongTuple1 tuple = LongTuple.of(42L);
        long[] array = tuple.toArray();
        assertArrayEquals(new long[] { 42L }, array);
    }

    @Test
    public void testToArray_tuple2() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        long[] array = tuple.toArray();
        assertArrayEquals(new long[] { 10L, 20L }, array);
    }

    @Test
    public void testToArray_tuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        long[] array = tuple.toArray();
        assertArrayEquals(new long[] { 1L, 2L, 3L }, array);
    }

    @Test
    public void testToList_tuple1() {
        LongTuple1 tuple = LongTuple.of(42L);
        LongList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals(42L, list.get(0));
    }

    @Test
    public void testToList_tuple2() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        LongList list = tuple.toList();
        assertEquals(2, list.size());
        assertEquals(10L, list.get(0));
        assertEquals(20L, list.get(1));
    }

    @Test
    public void testToList_tuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        LongList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(3L, list.get(2));
    }

    @Test
    public void testForEach_tuple1() {
        LongTuple1 tuple = LongTuple.of(42L);
        AtomicLong sum = new AtomicLong(0);
        tuple.forEach(value -> sum.addAndGet(value));
        assertEquals(42L, sum.get());
    }

    @Test
    public void testForEach_tuple2() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        AtomicLong sum = new AtomicLong(0);
        tuple.forEach(value -> sum.addAndGet(value));
        assertEquals(30L, sum.get());
    }

    @Test
    public void testForEach_tuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        AtomicLong sum = new AtomicLong(0);
        tuple.forEach(value -> sum.addAndGet(value));
        assertEquals(6L, sum.get());
    }

    @Test
    public void testStream_tuple1() {
        LongTuple1 tuple = LongTuple.of(42L);
        long sum = tuple.stream().sum();
        assertEquals(42L, sum);
    }

    @Test
    public void testStream_tuple2() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        long sum = tuple.stream().sum();
        assertEquals(30L, sum);
    }

    @Test
    public void testStream_tuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        long sum = tuple.stream().sum();
        assertEquals(6L, sum);
    }

    @Test
    public void testHashCode_tuple1() {
        LongTuple1 tuple1 = LongTuple.of(42L);
        LongTuple1 tuple2 = LongTuple.of(42L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_tuple1_different() {
        LongTuple1 tuple1 = LongTuple.of(42L);
        LongTuple1 tuple2 = LongTuple.of(100L);
        assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_tuple2() {
        LongTuple2 tuple1 = LongTuple.of(10L, 20L);
        LongTuple2 tuple2 = LongTuple.of(10L, 20L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_tuple3() {
        LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
        LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testEquals_tuple1_same() {
        LongTuple1 tuple1 = LongTuple.of(42L);
        LongTuple1 tuple2 = LongTuple.of(42L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple1_sameObject() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEquals_tuple1_different() {
        LongTuple1 tuple1 = LongTuple.of(42L);
        LongTuple1 tuple2 = LongTuple.of(100L);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple1_null() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertNotEquals(null, tuple);
    }

    @Test
    public void testEquals_tuple2_same() {
        LongTuple2 tuple1 = LongTuple.of(10L, 20L);
        LongTuple2 tuple2 = LongTuple.of(10L, 20L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple2_different() {
        LongTuple2 tuple1 = LongTuple.of(10L, 20L);
        LongTuple2 tuple2 = LongTuple.of(10L, 30L);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple3_same() {
        LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
        LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple3_different() {
        LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
        LongTuple3 tuple2 = LongTuple.of(1L, 2L, 4L);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testToString_tuple1() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertEquals("(42)", tuple.toString());
    }

    @Test
    public void testToString_tuple2() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        assertEquals("(10, 20)", tuple.toString());
    }

    @Test
    public void testToString_tuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals("(1, 2, 3)", tuple.toString());
    }

    @Test
    public void testAccept_tuple2() {
        LongTuple2 tuple = LongTuple.of(3L, 4L);
        AtomicLong result = new AtomicLong(0);
        tuple.accept((a, b) -> result.set(a + b));
        assertEquals(7L, result.get());
    }

    @Test
    public void testMap_tuple2() {
        LongTuple2 tuple = LongTuple.of(10L, 3L);
        long result = tuple.map((a, b) -> a % b);
        assertEquals(1L, result);
    }

    @Test
    public void testFilter_tuple2_match() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        Optional<LongTuple2> result = tuple.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testFilter_tuple2_noMatch() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        Optional<LongTuple2> result = tuple.filter((a, b) -> a > b);
        assertFalse(result.isPresent());
    }

    @Test
    public void testAccept_tuple3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        AtomicLong result = new AtomicLong(0);
        tuple.accept((a, b, c) -> result.set(a + b + c));
        assertEquals(6L, result.get());
    }

    @Test
    public void testMap_tuple3() {
        LongTuple3 tuple = LongTuple.of(2L, 3L, 4L);
        long result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(24L, result);
    }

    @Test
    public void testFilter_tuple3_match() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        Optional<LongTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFilter_tuple3_noMatch() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        Optional<LongTuple3> result = tuple.filter((a, b, c) -> a > b);
        assertFalse(result.isPresent());
    }

    @Test
    public void testTuple0_arity() {
        LongTuple<?> tuple = LongTuple.create(null);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testTuple0_min() {
        LongTuple<?> tuple = LongTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void testTuple0_max() {
        LongTuple<?> tuple = LongTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void testTuple0_median() {
        LongTuple<?> tuple = LongTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void testTuple0_sum() {
        LongTuple<?> tuple = LongTuple.create(null);
        assertEquals(0L, tuple.sum());
    }

    @Test
    public void testTuple0_average() {
        LongTuple<?> tuple = LongTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void testTuple0_reverse() {
        LongTuple<?> tuple = LongTuple.create(null);
        assertNotNull(tuple.reverse());
    }

    @Test
    public void testTuple0_contains() {
        LongTuple<?> tuple = LongTuple.create(null);
        assertFalse(tuple.contains(1L));
    }

    @Test
    public void testTuple0_toString() {
        LongTuple<?> tuple = LongTuple.create(null);
        assertEquals("[]", tuple.toString());
    }

    @Test
    public void testTuple5_operations() {
        LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertEquals(5, tuple.arity());
        assertEquals(1L, tuple.min());
        assertEquals(5L, tuple.max());
        assertEquals(3L, tuple.median());
        assertEquals(15L, tuple.sum());
        assertEquals(3.0, tuple.average(), 0.001);
        assertTrue(tuple.contains(3L));
        assertFalse(tuple.contains(10L));
    }

    @Test
    public void testTuple5_reverse() {
        LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        LongTuple5 reversed = tuple.reverse();
        assertEquals(5L, reversed._1);
        assertEquals(4L, reversed._2);
        assertEquals(3L, reversed._3);
        assertEquals(2L, reversed._4);
        assertEquals(1L, reversed._5);
    }

    @Test
    public void testTuple6_operations() {
        LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        assertEquals(6, tuple.arity());
        assertEquals(1L, tuple.min());
        assertEquals(6L, tuple.max());
        assertEquals(21L, tuple.sum());
    }

    @Test
    public void testTuple6_reverse() {
        LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        LongTuple6 reversed = tuple.reverse();
        assertEquals(6L, reversed._1);
        assertEquals(1L, reversed._6);
    }

    @Test
    public void testTuple7_operations() {
        LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertEquals(7, tuple.arity());
        assertEquals(4L, tuple.median());
        assertEquals(28L, tuple.sum());
    }

    @Test
    public void testTuple7_reverse() {
        LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        LongTuple7 reversed = tuple.reverse();
        assertEquals(7L, reversed._1);
        assertEquals(1L, reversed._7);
    }

    @Test
    public void testTuple8_operations() {
        LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testTuple8_reverse() {
        LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        LongTuple8 reversed = tuple.reverse();
        assertEquals(8L, reversed._1);
        assertEquals(1L, reversed._8);
    }

    @Test
    public void testTuple9_operations() {
        LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testTuple9_reverse() {
        LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        LongTuple9 reversed = tuple.reverse();
        assertEquals(9L, reversed._1);
        assertEquals(1L, reversed._9);
    }

    @Test
    public void testContains_tuple4() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        assertTrue(tuple.contains(1L));
        assertTrue(tuple.contains(4L));
        assertFalse(tuple.contains(5L));
    }

    @Test
    public void testContains_tuple5() {
        LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertTrue(tuple.contains(5L));
        assertFalse(tuple.contains(10L));
    }

    @Test
    public void testContains_tuple6() {
        LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        assertTrue(tuple.contains(6L));
        assertFalse(tuple.contains(7L));
    }

    @Test
    public void testContains_tuple7() {
        LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertTrue(tuple.contains(7L));
        assertFalse(tuple.contains(8L));
    }

    @Test
    public void testContains_tuple8() {
        LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertTrue(tuple.contains(8L));
        assertFalse(tuple.contains(9L));
    }

    @Test
    public void testContains_tuple9() {
        LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertTrue(tuple.contains(9L));
        assertFalse(tuple.contains(10L));
    }

    @Test
    public void testEquals_tuple4() {
        LongTuple4 tuple1 = LongTuple.of(1L, 2L, 3L, 4L);
        LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple5() {
        LongTuple5 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testToString_tuple4() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        assertEquals("(1, 2, 3, 4)", tuple.toString());
    }

    @Test
    public void testToString_tuple5() {
        LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertEquals("(1, 2, 3, 4, 5)", tuple.toString());
    }

    @Test
    public void testMap_tuple2_returnString() {
        LongTuple2 tuple = LongTuple.of(10L, 20L);
        String result = tuple.map((a, b) -> "Sum: " + (a + b));
        assertEquals("Sum: 30", result);
    }

    @Test
    public void testMap_tuple3_returnString() {
        LongTuple3 tuple = LongTuple.of(3L, 4L, 5L);
        String result = tuple.map((a, b, c) -> {
            if (a * a + b * b == c * c) {
                return "Pythagorean triple!";
            }
            return "Not a Pythagorean triple";
        });
        assertEquals("Pythagorean triple!", result);
    }

    @Test
    public void testTuple4_minMaxMedian() {
        LongTuple4 tuple = LongTuple.of(10L, 5L, 15L, 20L);
        assertEquals(5L, tuple.min());
        assertEquals(20L, tuple.max());
        assertEquals(10L, tuple.median());
    }

    @Test
    public void testTuple5_sum() {
        LongTuple5 tuple = LongTuple.of(10L, 20L, 30L, 40L, 50L);
        assertEquals(150L, tuple.sum());
    }

    @Test
    public void testTuple6_average() {
        LongTuple6 tuple = LongTuple.of(10L, 20L, 30L, 40L, 50L, 60L);
        assertEquals(35.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple7_median() {
        LongTuple7 tuple = LongTuple.of(1L, 3L, 5L, 7L, 9L, 11L, 13L);
        assertEquals(7L, tuple.median());
    }
}
