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
import com.landawn.abacus.util.ShortTuple.ShortTuple1;
import com.landawn.abacus.util.ShortTuple.ShortTuple2;
import com.landawn.abacus.util.ShortTuple.ShortTuple3;
import com.landawn.abacus.util.ShortTuple.ShortTuple4;
import com.landawn.abacus.util.ShortTuple.ShortTuple5;
import com.landawn.abacus.util.ShortTuple.ShortTuple6;
import com.landawn.abacus.util.ShortTuple.ShortTuple7;
import com.landawn.abacus.util.ShortTuple.ShortTuple8;
import com.landawn.abacus.util.ShortTuple.ShortTuple9;
import com.landawn.abacus.util.u.Optional;

@Tag("2510")
public class ShortTuple2510Test extends TestBase {

    @Test
    public void testOf1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertEquals(1, tuple.arity());
        assertEquals((short) 1, tuple._1);
    }

    @Test
    public void testOf2() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertEquals(2, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 2, tuple._2);
    }

    @Test
    public void testOf3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(3, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 2, tuple._2);
        assertEquals((short) 3, tuple._3);
    }

    @Test
    public void testOf4() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertEquals(4, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 2, tuple._2);
        assertEquals((short) 3, tuple._3);
        assertEquals((short) 4, tuple._4);
    }

    @Test
    public void testOf5() {
        ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertEquals(5, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 5, tuple._5);
    }

    @Test
    public void testOf6() {
        ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        assertEquals(6, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 6, tuple._6);
    }

    @Test
    public void testOf7() {
        ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        assertEquals(7, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 7, tuple._7);
    }

    @Test
    public void testOf8() {
        ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        assertEquals(8, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 8, tuple._8);
    }

    @Test
    public void testOf9() {
        ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertEquals(9, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 9, tuple._9);
    }

    @Test
    public void testCreate_nullArray() {
        ShortTuple<?> tuple = ShortTuple.create(null);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_emptyArray() {
        ShortTuple<?> tuple = ShortTuple.create(new short[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_sizeOne() {
        ShortTuple1 tuple = ShortTuple.create(new short[] { 1 });
        assertEquals(1, tuple.arity());
        assertEquals((short) 1, tuple._1);
    }

    @Test
    public void testCreate_sizeTwo() {
        ShortTuple2 tuple = ShortTuple.create(new short[] { 1, 2 });
        assertEquals(2, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 2, tuple._2);
    }

    @Test
    public void testCreate_sizeThree() {
        ShortTuple3 tuple = ShortTuple.create(new short[] { 1, 2, 3 });
        assertEquals(3, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 3, tuple._3);
    }

    @Test
    public void testCreate_sizeFour() {
        ShortTuple4 tuple = ShortTuple.create(new short[] { 1, 2, 3, 4 });
        assertEquals(4, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 4, tuple._4);
    }

    @Test
    public void testCreate_sizeFive() {
        ShortTuple5 tuple = ShortTuple.create(new short[] { 1, 2, 3, 4, 5 });
        assertEquals(5, tuple.arity());
        assertEquals((short) 5, tuple._5);
    }

    @Test
    public void testCreate_sizeSix() {
        ShortTuple6 tuple = ShortTuple.create(new short[] { 1, 2, 3, 4, 5, 6 });
        assertEquals(6, tuple.arity());
        assertEquals((short) 6, tuple._6);
    }

    @Test
    public void testCreate_sizeSeven() {
        ShortTuple7 tuple = ShortTuple.create(new short[] { 1, 2, 3, 4, 5, 6, 7 });
        assertEquals(7, tuple.arity());
        assertEquals((short) 7, tuple._7);
    }

    @Test
    public void testCreate_sizeEight() {
        ShortTuple8 tuple = ShortTuple.create(new short[] { 1, 2, 3, 4, 5, 6, 7, 8 });
        assertEquals(8, tuple.arity());
        assertEquals((short) 8, tuple._8);
    }

    @Test
    public void testCreate_sizeNine() {
        ShortTuple9 tuple = ShortTuple.create(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        assertEquals(9, tuple.arity());
        assertEquals((short) 9, tuple._9);
    }

    @Test
    public void testCreate_tooManyElements() {
        assertThrows(IllegalArgumentException.class, () -> ShortTuple.create(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
    }

    @Test
    public void testMin_tuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals((short) 5, tuple.min());
    }

    @Test
    public void testMin_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
        assertEquals((short) 2, tuple.min());
    }

    @Test
    public void testMin_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 2, (short) 8);
        assertEquals((short) 2, tuple.min());
    }

    @Test
    public void testMin_tuple4() {
        ShortTuple4 tuple = ShortTuple.of((short) 5, (short) 2, (short) 8, (short) 1);
        assertEquals((short) 1, tuple.min());
    }

    @Test
    public void testMax_tuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals((short) 5, tuple.max());
    }

    @Test
    public void testMax_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
        assertEquals((short) 5, tuple.max());
    }

    @Test
    public void testMax_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 2, (short) 8);
        assertEquals((short) 8, tuple.max());
    }

    @Test
    public void testMedian_tuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals((short) 5, tuple.median());
    }

    @Test
    public void testMedian_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
        assertEquals((short) 2, tuple.median());
    }

    @Test
    public void testMedian_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 3, (short) 2);
        assertEquals((short) 2, tuple.median());
    }

    @Test
    public void testMedian_tuple4() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertEquals((short) 2, tuple.median());
    }

    @Test
    public void testSum_tuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals(5, tuple.sum());
    }

    @Test
    public void testSum_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
        assertEquals(7, tuple.sum());
    }

    @Test
    public void testSum_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(6, tuple.sum());
    }

    @Test
    public void testAverage_tuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals(5.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverage_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 4, (short) 6);
        assertEquals(5.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverage_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(2.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverage_tuple4() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertEquals(2.5, tuple.average(), 0.001);
    }

    @Test
    public void testReverse_tuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        ShortTuple1 reversed = tuple.reverse();
        assertEquals((short) 1, reversed._1);
    }

    @Test
    public void testReverse_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        ShortTuple2 reversed = tuple.reverse();
        assertEquals((short) 2, reversed._1);
        assertEquals((short) 1, reversed._2);
    }

    @Test
    public void testReverse_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 reversed = tuple.reverse();
        assertEquals((short) 3, reversed._1);
        assertEquals((short) 2, reversed._2);
        assertEquals((short) 1, reversed._3);
    }

    @Test
    public void testReverse_tuple4() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        ShortTuple4 reversed = tuple.reverse();
        assertEquals((short) 4, reversed._1);
        assertEquals((short) 1, reversed._4);
    }

    @Test
    public void testContains_tuple1_found() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertTrue(tuple.contains((short) 1));
    }

    @Test
    public void testContains_tuple1_notFound() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertFalse(tuple.contains((short) 2));
    }

    @Test
    public void testContains_tuple2_found() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertTrue(tuple.contains((short) 2));
    }

    @Test
    public void testContains_tuple2_notFound() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertFalse(tuple.contains((short) 5));
    }

    @Test
    public void testContains_tuple3_found() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertTrue(tuple.contains((short) 2));
    }

    @Test
    public void testContains_tuple3_notFound() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertFalse(tuple.contains((short) 5));
    }

    @Test
    public void testToArray_tuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        short[] array = tuple.toArray();
        assertArrayEquals(new short[] { 1 }, array);
    }

    @Test
    public void testToArray_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        short[] array = tuple.toArray();
        assertArrayEquals(new short[] { 1, 2 }, array);
    }

    @Test
    public void testToArray_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        short[] array = tuple.toArray();
        assertArrayEquals(new short[] { 1, 2, 3 }, array);
    }

    @Test
    public void testToList_tuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        ShortList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals((short) 1, list.get(0));
    }

    @Test
    public void testToList_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        ShortList list = tuple.toList();
        assertEquals(2, list.size());
        assertEquals((short) 1, list.get(0));
        assertEquals((short) 2, list.get(1));
    }

    @Test
    public void testToList_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals((short) 3, list.get(2));
    }

    @Test
    public void testForEach_tuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        AtomicInteger sum = new AtomicInteger(0);
        tuple.forEach(value -> sum.addAndGet(value));
        assertEquals(1, sum.get());
    }

    @Test
    public void testForEach_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        AtomicInteger sum = new AtomicInteger(0);
        tuple.forEach(value -> sum.addAndGet(value));
        assertEquals(3, sum.get());
    }

    @Test
    public void testForEach_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        AtomicInteger sum = new AtomicInteger(0);
        tuple.forEach(value -> sum.addAndGet(value));
        assertEquals(6, sum.get());
    }

    @Test
    public void testForEach_tuple4() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        AtomicInteger sum = new AtomicInteger(0);
        tuple.forEach(value -> sum.addAndGet(value));
        assertEquals(10, sum.get());
    }

    @Test
    public void testStream_tuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        int sum = tuple.stream().sum();
        assertEquals(1, sum);
    }

    @Test
    public void testStream_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        int sum = tuple.stream().sum();
        assertEquals(3, sum);
    }

    @Test
    public void testStream_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        int sum = tuple.stream().sum();
        assertEquals(6, sum);
    }

    @Test
    public void testHashCode_tuple1() {
        ShortTuple1 tuple1 = ShortTuple.of((short) 1);
        ShortTuple1 tuple2 = ShortTuple.of((short) 1);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_tuple1_different() {
        ShortTuple1 tuple1 = ShortTuple.of((short) 1);
        ShortTuple1 tuple2 = ShortTuple.of((short) 2);
        assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_tuple2() {
        ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_tuple3() {
        ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testEquals_tuple1_same() {
        ShortTuple1 tuple1 = ShortTuple.of((short) 1);
        ShortTuple1 tuple2 = ShortTuple.of((short) 1);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple1_sameObject() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEquals_tuple1_different() {
        ShortTuple1 tuple1 = ShortTuple.of((short) 1);
        ShortTuple1 tuple2 = ShortTuple.of((short) 2);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple1_null() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertNotEquals(null, tuple);
    }

    @Test
    public void testEquals_tuple2_same() {
        ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple2_different() {
        ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 3);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple3_same() {
        ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_tuple3_different() {
        ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 4);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testToString_tuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertEquals("(1)", tuple.toString());
    }

    @Test
    public void testToString_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertEquals("(1, 2)", tuple.toString());
    }

    @Test
    public void testToString_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals("(1, 2, 3)", tuple.toString());
    }

    @Test
    public void testAccept_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
        AtomicInteger result = new AtomicInteger(0);
        tuple.accept((a, b) -> result.set(a * b));
        assertEquals(15, result.get());
    }

    @Test
    public void testMap_tuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
        int result = tuple.map((a, b) -> a * b);
        assertEquals(15, result);
    }

    @Test
    public void testFilter_tuple2_match() {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
        Optional<ShortTuple2> result = tuple.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testFilter_tuple2_noMatch() {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
        Optional<ShortTuple2> result = tuple.filter((a, b) -> a > b);
        assertFalse(result.isPresent());
    }

    @Test
    public void testAccept_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
        AtomicInteger result = new AtomicInteger(0);
        tuple.accept((a, b, c) -> result.set(a + b + c));
        assertEquals(10, result.get());
    }

    @Test
    public void testMap_tuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
        int result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(30, result);
    }

    @Test
    public void testFilter_tuple3_match() {
        ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
        Optional<ShortTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFilter_tuple3_noMatch() {
        ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
        Optional<ShortTuple3> result = tuple.filter((a, b, c) -> a > b);
        assertFalse(result.isPresent());
    }

    @Test
    public void testTuple0_arity() {
        ShortTuple<?> tuple = ShortTuple.create(null);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testTuple0_min() {
        ShortTuple<?> tuple = ShortTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void testTuple0_max() {
        ShortTuple<?> tuple = ShortTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void testTuple0_median() {
        ShortTuple<?> tuple = ShortTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void testTuple0_sum() {
        ShortTuple<?> tuple = ShortTuple.create(null);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void testTuple0_average() {
        ShortTuple<?> tuple = ShortTuple.create(null);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void testTuple0_reverse() {
        ShortTuple<?> tuple = ShortTuple.create(null);
        assertNotNull(tuple.reverse());
    }

    @Test
    public void testTuple0_contains() {
        ShortTuple<?> tuple = ShortTuple.create(null);
        assertFalse(tuple.contains((short) 1));
    }

    @Test
    public void testTuple0_toString() {
        ShortTuple<?> tuple = ShortTuple.create(null);
        assertEquals("()", tuple.toString());
    }

    @Test
    public void testTuple5_operations() {
        ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertEquals(5, tuple.arity());
        assertEquals((short) 1, tuple.min());
        assertEquals((short) 5, tuple.max());
        assertEquals((short) 3, tuple.median());
        assertEquals(15, tuple.sum());
        assertEquals(3.0, tuple.average(), 0.001);
        assertTrue(tuple.contains((short) 3));
        assertFalse(tuple.contains((short) 10));
    }

    @Test
    public void testTuple5_reverse() {
        ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        ShortTuple5 reversed = tuple.reverse();
        assertEquals((short) 5, reversed._1);
        assertEquals((short) 4, reversed._2);
        assertEquals((short) 3, reversed._3);
        assertEquals((short) 2, reversed._4);
        assertEquals((short) 1, reversed._5);
    }

    @Test
    public void testTuple6_operations() {
        ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        assertEquals(6, tuple.arity());
        assertEquals((short) 1, tuple.min());
        assertEquals((short) 6, tuple.max());
        assertEquals(21, tuple.sum());
    }

    @Test
    public void testTuple7_operations() {
        ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        assertEquals(7, tuple.arity());
        assertEquals((short) 4, tuple.median());
        assertEquals(28, tuple.sum());
    }

    @Test
    public void testTuple8_operations() {
        ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        assertEquals(8, tuple.arity());
        assertEquals((short) 8, tuple.max());
        assertEquals(36, tuple.sum());
    }

    @Test
    public void testTuple9_operations() {
        ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertEquals(9, tuple.arity());
        assertEquals((short) 5, tuple.median());
        assertEquals(45, tuple.sum());
        assertEquals(5.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple9_reverse() {
        ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        ShortTuple9 reversed = tuple.reverse();
        assertEquals((short) 9, reversed._1);
        assertEquals((short) 1, reversed._9);
    }

    @Test
    public void testForEach_tuple5() {
        ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(value -> count.incrementAndGet());
        assertEquals(5, count.get());
    }

    @Test
    public void testForEach_tuple6() {
        ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(value -> count.incrementAndGet());
        assertEquals(6, count.get());
    }

    @Test
    public void testForEach_tuple7() {
        ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(value -> count.incrementAndGet());
        assertEquals(7, count.get());
    }

    @Test
    public void testForEach_tuple8() {
        ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(value -> count.incrementAndGet());
        assertEquals(8, count.get());
    }

    @Test
    public void testForEach_tuple9() {
        ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(value -> count.incrementAndGet());
        assertEquals(9, count.get());
    }

    @Test
    public void testContains_tuple4() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertTrue(tuple.contains((short) 1));
        assertTrue(tuple.contains((short) 4));
        assertFalse(tuple.contains((short) 5));
    }

    @Test
    public void testContains_tuple5() {
        ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertTrue(tuple.contains((short) 5));
        assertFalse(tuple.contains((short) 10));
    }

    @Test
    public void testContains_tuple6() {
        ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        assertTrue(tuple.contains((short) 6));
        assertFalse(tuple.contains((short) 7));
    }

    @Test
    public void testContains_tuple7() {
        ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        assertTrue(tuple.contains((short) 7));
        assertFalse(tuple.contains((short) 8));
    }

    @Test
    public void testContains_tuple8() {
        ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        assertTrue(tuple.contains((short) 8));
        assertFalse(tuple.contains((short) 9));
    }

    @Test
    public void testContains_tuple9() {
        ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertTrue(tuple.contains((short) 9));
        assertFalse(tuple.contains((short) 10));
    }
}
