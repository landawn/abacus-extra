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
import com.landawn.abacus.util.u.Optional;

@Tag("2511")
public class LongTuple2511Test extends TestBase {

    // ====== Factory Methods Tests ======

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
    public void testCreateFromArray() {
        // Empty array
        LongTuple<?> empty = LongTuple.create(null);
        assertEquals(0, empty.arity());

        empty = LongTuple.create(new long[0]);
        assertEquals(0, empty.arity());

        // Single element
        LongTuple<?> tuple1 = LongTuple.create(new long[] { 42L });
        assertEquals(1, tuple1.arity());

        // Multiple elements
        LongTuple<?> tuple3 = LongTuple.create(new long[] { 10L, 20L, 30L });
        assertEquals(3, tuple3.arity());

        // Max size
        LongTuple<?> tuple9 = LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L });
        assertEquals(9, tuple9.arity());

        // Too many elements
        assertThrows(IllegalArgumentException.class, () -> LongTuple.create(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L }));
    }

    // ====== Arity Tests ======

    @Test
    public void testArity0() {
        LongTuple<?> empty = LongTuple.create(null);
        assertEquals(0, empty.arity());
    }

    @Test
    public void testArity1() {
        LongTuple1 tuple = LongTuple.of(1L);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testArity2() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testArity3() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(3, tuple.arity());
    }

    // ====== Statistics Tests (min, max, median, sum, average) ======

    @Test
    public void testMin() {
        assertEquals(2L, LongTuple.of(5L, 2L, 8L).min());
        assertEquals(1L, LongTuple.of(1L, 2L, 3L, 4L, 5L).min());
        assertEquals(-10L, LongTuple.of(-5L, -10L, 0L).min());
    }

    @Test
    public void testMax() {
        assertEquals(8L, LongTuple.of(5L, 2L, 8L).max());
        assertEquals(5L, LongTuple.of(1L, 2L, 3L, 4L, 5L).max());
        assertEquals(10L, LongTuple.of(-5L, 0L, 10L).max());
    }

    @Test
    public void testMedian() {
        // Odd number of elements - returns middle value
        assertEquals(2L, LongTuple.of(3L, 1L, 2L).median());
        assertEquals(3L, LongTuple.of(1L, 2L, 3L, 4L, 5L).median());

        // Even number of elements - returns lower middle value
        assertEquals(2L, LongTuple.of(1L, 2L, 3L, 4L).median());
    }

    @Test
    public void testSum() {
        assertEquals(60L, LongTuple.of(10L, 20L, 30L).sum());
        assertEquals(15L, LongTuple.of(1L, 2L, 3L, 4L, 5L).sum());
        assertEquals(0L, LongTuple.create(null).sum());
    }

    @Test
    public void testAverage() {
        assertEquals(20.0, LongTuple.of(10L, 20L, 30L).average(), 0.0001);
        assertEquals(3.0, LongTuple.of(1L, 2L, 3L, 4L, 5L).average(), 0.0001);
    }

    @Test
    public void testMin_Empty() {
        assertThrows(NoSuchElementException.class, () -> LongTuple.create(null).min());
    }

    @Test
    public void testMax_Empty() {
        assertThrows(NoSuchElementException.class, () -> LongTuple.create(null).max());
    }

    @Test
    public void testMedian_Empty() {
        assertThrows(NoSuchElementException.class, () -> LongTuple.create(null).median());
    }

    @Test
    public void testAverage_Empty() {
        assertThrows(NoSuchElementException.class, () -> LongTuple.create(null).average());
    }

    // ====== Reverse Tests ======

    @Test
    public void testReverse1() {
        LongTuple1 reversed = LongTuple.of(42L).reverse();
        assertEquals(42L, reversed._1);
    }

    @Test
    public void testReverse2() {
        LongTuple2 reversed = LongTuple.of(10L, 20L).reverse();
        assertEquals(20L, reversed._1);
        assertEquals(10L, reversed._2);
    }

    @Test
    public void testReverse3() {
        LongTuple3 reversed = LongTuple.of(1L, 2L, 3L).reverse();
        assertEquals(3L, reversed._1);
        assertEquals(2L, reversed._2);
        assertEquals(1L, reversed._3);
    }

    @Test
    public void testReverse0() {
        LongTuple<?> reversed = LongTuple.create(null).reverse();
        assertEquals(0, reversed.arity());
    }

    // ====== Contains Tests ======

    @Test
    public void testContains() {
        LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
        assertTrue(tuple.contains(20L));
        assertFalse(tuple.contains(40L));
        assertTrue(tuple.contains(10L));
        assertTrue(tuple.contains(30L));
    }

    @Test
    public void testContains_Empty() {
        assertFalse(LongTuple.create(null).contains(10L));
    }

    @Test
    public void testContains_Single() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertTrue(tuple.contains(42L));
        assertFalse(tuple.contains(43L));
    }

    // ====== Array Conversion Tests ======

    @Test
    public void testToArray() {
        long[] array = LongTuple.of(1L, 2L, 3L).toArray();
        assertArrayEquals(new long[] { 1L, 2L, 3L }, array);

        // Test that modifications to returned array don't affect tuple
        array[0] = 999L;
        assertEquals(1L, LongTuple.of(1L, 2L, 3L).toArray()[0]);
    }

    @Test
    public void testToArray_Empty() {
        long[] array = LongTuple.create(null).toArray();
        assertEquals(0, array.length);
    }

    // ====== List Conversion Tests ======

    @Test
    public void testToList() {
        LongList list = LongTuple.of(1L, 2L, 3L).toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(1L, list.get(0));
        assertEquals(2L, list.get(1));
        assertEquals(3L, list.get(2));
    }

    @Test
    public void testToList_Empty() {
        LongList list = LongTuple.create(null).toList();
        assertEquals(0, list.size());
    }

    // ====== forEach Tests ======

    @Test
    public void testForEach() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        AtomicLong sum = new AtomicLong(0);
        tuple.forEach(sum::addAndGet);
        assertEquals(6L, sum.get());
    }

    @Test
    public void testForEach_Empty() {
        AtomicLong sum = new AtomicLong(0);
        LongTuple.create(null).forEach(sum::addAndGet);
        assertEquals(0L, sum.get());
    }

    // ====== Stream Tests ======

    @Test
    public void testStream() {
        long sum = LongTuple.of(1L, 2L, 3L).stream().sum();
        assertEquals(6L, sum);
    }

    @Test
    public void testStream_Empty() {
        long sum = LongTuple.create(null).stream().sum();
        assertEquals(0L, sum);
    }

    // ====== Equality Tests ======

    @Test
    public void testEquals() {
        LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
        LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
        LongTuple3 tuple3 = LongTuple.of(1L, 2L, 4L);

        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertEquals(tuple1, tuple1);
        assertNotEquals(tuple1, null);
    }

    @Test
    public void testEquals_DifferentTypes() {
        LongTuple2 tuple2 = LongTuple.of(1L, 2L);
        LongTuple3 tuple3 = LongTuple.of(1L, 2L, 3L);
        assertNotEquals(tuple2, tuple3);
    }

    @Test
    public void testEquals_Empty() {
        LongTuple<?> empty1 = LongTuple.create(null);
        LongTuple<?> empty2 = LongTuple.create(new long[0]);
        assertEquals(empty1, empty2);
    }

    // ====== HashCode Tests ======

    @Test
    public void testHashCode() {
        LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
        LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);

        // Equal objects must have equal hash codes
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_Consistency() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    // ====== String Representation Tests ======

    @Test
    public void testToString() {
        assertEquals("[42]", LongTuple.of(42L).toString());
        assertEquals("[10, 20]", LongTuple.of(10L, 20L).toString());
        assertEquals("[1, 2, 3]", LongTuple.of(1L, 2L, 3L).toString());
        assertEquals("[]", LongTuple.create(null).toString());
    }

    // ====== LongTuple2 Specific Tests ======

    @Test
    public void testAccept2() {
        LongTuple2 pair = LongTuple.of(3L, 4L);
        AtomicLong sum = new AtomicLong(0);
        pair.accept((a, b) -> sum.set(a + b));
        assertEquals(7L, sum.get());
    }

    @Test
    public void testMap2() {
        LongTuple2 pair = LongTuple.of(10L, 3L);
        long remainder = pair.map((a, b) -> a % b);
        assertEquals(1L, remainder);
    }

    @Test
    public void testFilter2_True() {
        LongTuple2 pair = LongTuple.of(10L, 20L);
        Optional<LongTuple2> result = pair.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(pair, result.get());
    }

    @Test
    public void testFilter2_False() {
        LongTuple2 pair = LongTuple.of(20L, 10L);
        Optional<LongTuple2> result = pair.filter((a, b) -> a < b);
        assertFalse(result.isPresent());
    }

    // ====== LongTuple3 Specific Tests ======

    @Test
    public void testAccept3() {
        LongTuple3 triple = LongTuple.of(3L, 4L, 5L);
        AtomicLong sum = new AtomicLong(0);
        triple.accept((a, b, c) -> sum.set(a + b + c));
        assertEquals(12L, sum.get());
    }

    @Test
    public void testMap3() {
        LongTuple3 triple = LongTuple.of(2L, 3L, 4L);
        long volume = triple.map((l, w, h) -> l * w * h);
        assertEquals(24L, volume);
    }

    @Test
    public void testFilter3_True() {
        LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
        Optional<LongTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
        assertEquals(triple, result.get());
    }

    @Test
    public void testFilter3_False() {
        LongTuple3 triple = LongTuple.of(3L, 2L, 1L);
        Optional<LongTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
        assertFalse(result.isPresent());
    }

    // ====== Edge Cases and Special Values ======

    @Test
    public void testWithZero() {
        LongTuple3 tuple = LongTuple.of(0L, 1L, 2L);
        assertEquals(0L, tuple.min());
        assertEquals(2L, tuple.max());
        assertEquals(3L, tuple.sum());
    }

    @Test
    public void testWithNegatives() {
        LongTuple3 tuple = LongTuple.of(-5L, -10L, -1L);
        assertEquals(-10L, tuple.min());
        assertEquals(-1L, tuple.max());
        assertEquals(-16L, tuple.sum());
    }

    @Test
    public void testWithLargeValues() {
        long max = Long.MAX_VALUE / 2;
        LongTuple2 tuple = LongTuple.of(max, max);
        assertEquals(max, tuple.min());
        assertEquals(max, tuple.max());
    }

    @Test
    public void testWithSingleElement() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertEquals(42L, tuple.min());
        assertEquals(42L, tuple.max());
        assertEquals(42L, tuple.median());
        assertEquals(42L, tuple.sum());
        assertEquals(42.0, tuple.average(), 0.0001);
        assertTrue(tuple.contains(42L));
    }

    // ====== All Tuple Sizes Combined ======

    @Test
    public void testAllTupleSizes() {
        LongTuple0 t0 = LongTuple.create(null);
        LongTuple1 t1 = LongTuple.of(1L);
        LongTuple2 t2 = LongTuple.of(1L, 2L);
        LongTuple3 t3 = LongTuple.of(1L, 2L, 3L);
        LongTuple4 t4 = LongTuple.of(1L, 2L, 3L, 4L);
        LongTuple5 t5 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        LongTuple6 t6 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        LongTuple7 t7 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        LongTuple8 t8 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        LongTuple9 t9 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);

        assertEquals(0, t0.arity());
        assertEquals(1, t1.arity());
        assertEquals(2, t2.arity());
        assertEquals(3, t3.arity());
        assertEquals(4, t4.arity());
        assertEquals(5, t5.arity());
        assertEquals(6, t6.arity());
        assertEquals(7, t7.arity());
        assertEquals(8, t8.arity());
        assertEquals(9, t9.arity());
    }

    // ====== Functional Method Exception Tests ======

    @Test
    public void testAccept2_Exception() throws Exception {
        LongTuple2 pair = LongTuple.of(3L, 4L);
        assertThrows(IllegalArgumentException.class, () -> pair.accept((a, b) -> {
            throw new IllegalArgumentException("test");
        }));
    }

    @Test
    public void testMap2_Exception() {
        LongTuple2 pair = LongTuple.of(3L, 4L);
        assertThrows(IllegalArgumentException.class, () -> pair.map((a, b) -> {
            throw new IllegalArgumentException("test");
        }));
    }

    @Test
    public void testFilter2_Exception() {
        LongTuple2 pair = LongTuple.of(3L, 4L);
        assertThrows(IllegalArgumentException.class, () -> pair.filter((a, b) -> {
            throw new IllegalArgumentException("test");
        }));
    }

    // ====== Immutability Tests ======

    @Test
    public void testImmutability() {
        long[] arr = new long[] { 1L, 2L, 3L };
        LongTuple3 tuple = LongTuple.create(arr);
        arr[0] = 999L;
        assertEquals(1L, tuple._1);
    }

    @Test
    public void testToArray_Independence() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        long[] arr1 = tuple.toArray();
        long[] arr2 = tuple.toArray();
        arr1[0] = 999L;
        assertEquals(1L, arr2[0]);
    }
}
