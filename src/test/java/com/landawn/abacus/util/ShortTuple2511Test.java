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
import com.landawn.abacus.util.ShortTuple.ShortTuple0;
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

@Tag("2511")
public class ShortTuple2511Test extends TestBase {

    // ====== Factory Methods Tests ======

    @Test
    public void testOf1() {
        ShortTuple1 tuple = ShortTuple.of((short) 42);
        assertEquals(1, tuple.arity());
        assertEquals((short) 42, tuple._1);
    }

    @Test
    public void testOf2() {
        ShortTuple2 tuple = ShortTuple.of((short) 10, (short) 20);
        assertEquals(2, tuple.arity());
        assertEquals((short) 10, tuple._1);
        assertEquals((short) 20, tuple._2);
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
    public void testCreateFromArray() {
        // Empty array
        ShortTuple<?> empty = ShortTuple.create(null);
        assertEquals(0, empty.arity());

        empty = ShortTuple.create(new short[0]);
        assertEquals(0, empty.arity());

        // Single element
        ShortTuple1 tuple1 = ShortTuple.of((short) 42);
        assertEquals(1, tuple1.arity());
        assertEquals((short) 42, tuple1._1);

        // Multiple elements
        ShortTuple<?> tuple3 = ShortTuple.create(new short[] { 10, 20, 30 });
        assertEquals(3, tuple3.arity());

        // Max size
        ShortTuple<?> tuple9 = ShortTuple.create(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        assertEquals(9, tuple9.arity());

        // Too many elements
        assertThrows(IllegalArgumentException.class, () -> ShortTuple.create(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
    }

    // ====== Arity Tests ======

    @Test
    public void testArity0() {
        ShortTuple<?> empty = ShortTuple.create(null);
        assertEquals(0, empty.arity());
    }

    @Test
    public void testArity1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testArity2() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testArity3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(3, tuple.arity());
    }

    // ====== Statistics Tests (min, max, median, sum, average) ======

    @Test
    public void testMin() {
        assertEquals((short) 2, ShortTuple.of((short) 5, (short) 2, (short) 8).min());
        assertEquals((short) 1, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).min());
        assertEquals((short) -10, ShortTuple.of((short) -5, (short) -10, (short) 0).min());
    }

    @Test
    public void testMax() {
        assertEquals((short) 8, ShortTuple.of((short) 5, (short) 2, (short) 8).max());
        assertEquals((short) 5, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).max());
        assertEquals((short) 10, ShortTuple.of((short) -5, (short) 0, (short) 10).max());
    }

    @Test
    public void testMedian() {
        // Odd number of elements - returns middle value
        assertEquals((short) 2, ShortTuple.of((short) 3, (short) 1, (short) 2).median());
        assertEquals((short) 3, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).median());

        // Even number of elements - returns lower middle value
        assertEquals((short) 2, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4).median());
    }

    @Test
    public void testSum() {
        assertEquals(60, ShortTuple.of((short) 10, (short) 20, (short) 30).sum());
        assertEquals(15, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).sum());
        assertEquals(0, ShortTuple.create(null).sum());
    }

    @Test
    public void testAverage() {
        assertEquals(20.0, ShortTuple.of((short) 10, (short) 20, (short) 30).average(), 0.0001);
        assertEquals(3.0, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).average(), 0.0001);
    }

    @Test
    public void testMin_Empty() {
        assertThrows(NoSuchElementException.class, () -> ShortTuple.create(null).min());
    }

    @Test
    public void testMax_Empty() {
        assertThrows(NoSuchElementException.class, () -> ShortTuple.create(null).max());
    }

    @Test
    public void testMedian_Empty() {
        assertThrows(NoSuchElementException.class, () -> ShortTuple.create(null).median());
    }

    @Test
    public void testAverage_Empty() {
        assertThrows(NoSuchElementException.class, () -> ShortTuple.create(null).average());
    }

    // ====== Reverse Tests ======

    @Test
    public void testReverse1() {
        ShortTuple1 reversed = ShortTuple.of((short) 42).reverse();
        assertEquals((short) 42, reversed._1);
    }

    @Test
    public void testReverse2() {
        ShortTuple2 reversed = ShortTuple.of((short) 10, (short) 20).reverse();
        assertEquals((short) 20, reversed._1);
        assertEquals((short) 10, reversed._2);
    }

    @Test
    public void testReverse3() {
        ShortTuple3 reversed = ShortTuple.of((short) 1, (short) 2, (short) 3).reverse();
        assertEquals((short) 3, reversed._1);
        assertEquals((short) 2, reversed._2);
        assertEquals((short) 1, reversed._3);
    }

    @Test
    public void testReverse0() {
        ShortTuple<?> reversed = ShortTuple.create(null).reverse();
        assertEquals(0, reversed.arity());
    }

    // ====== Contains Tests ======

    @Test
    public void testContains() {
        ShortTuple3 tuple = ShortTuple.of((short) 10, (short) 20, (short) 30);
        assertTrue(tuple.contains((short) 20));
        assertFalse(tuple.contains((short) 40));
        assertTrue(tuple.contains((short) 10));
        assertTrue(tuple.contains((short) 30));
    }

    @Test
    public void testContains_Empty() {
        assertFalse(ShortTuple.create(null).contains((short) 10));
    }

    @Test
    public void testContains_Single() {
        ShortTuple1 tuple = ShortTuple.of((short) 42);
        assertTrue(tuple.contains((short) 42));
        assertFalse(tuple.contains((short) 43));
    }

    // ====== Array Conversion Tests ======

    @Test
    public void testToArray() {
        short[] array = ShortTuple.of((short) 1, (short) 2, (short) 3).toArray();
        assertArrayEquals(new short[] { 1, 2, 3 }, array);

        // Test that modifications to returned array don't affect tuple
        array[0] = 999;
        assertEquals((short) 1, ShortTuple.of((short) 1, (short) 2, (short) 3).toArray()[0]);
    }

    @Test
    public void testToArray_Empty() {
        short[] array = ShortTuple.create(null).toArray();
        assertEquals(0, array.length);
    }

    // ====== List Conversion Tests ======

    @Test
    public void testToList() {
        ShortList list = ShortTuple.of((short) 1, (short) 2, (short) 3).toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals((short) 1, list.get(0));
        assertEquals((short) 2, list.get(1));
        assertEquals((short) 3, list.get(2));
    }

    @Test
    public void testToList_Empty() {
        ShortList list = ShortTuple.create(null).toList();
        assertEquals(0, list.size());
    }

    // ====== forEach Tests ======

    @Test
    public void testForEach() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        AtomicInteger sum = new AtomicInteger(0);
        tuple.forEach(v -> sum.addAndGet(v));
        assertEquals(6, sum.get());
    }

    @Test
    public void testForEach_Empty() {
        AtomicInteger sum = new AtomicInteger(0);
        ShortTuple.create(null).forEach(v -> sum.addAndGet(v));
        assertEquals(0, sum.get());
    }

    // ====== Stream Tests ======

    @Test
    public void testStream() {
        int sum = ShortTuple.of((short) 1, (short) 2, (short) 3).stream().sum();
        assertEquals(6, sum);
    }

    @Test
    public void testStream_Empty() {
        int sum = ShortTuple.create(null).stream().sum();
        assertEquals(0, sum);
    }

    // ====== Equality Tests ======

    @Test
    public void testEquals() {
        ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 4);

        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertEquals(tuple1, tuple1);
        assertNotEquals(tuple1, null);
    }

    @Test
    public void testEquals_DifferentTypes() {
        ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple3 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertNotEquals(tuple2, tuple3);
    }

    @Test
    public void testEquals_Empty() {
        ShortTuple<?> empty1 = ShortTuple.create(null);
        ShortTuple<?> empty2 = ShortTuple.create(new short[0]);
        assertEquals(empty1, empty2);
    }

    // ====== HashCode Tests ======

    @Test
    public void testHashCode() {
        ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);

        // Equal objects must have equal hash codes
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_Consistency() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    // ====== String Representation Tests ======

    @Test
    public void testToString() {
        assertEquals("(42)", ShortTuple.of((short) 42).toString());
        assertEquals("(10, 20)", ShortTuple.of((short) 10, (short) 20).toString());
        assertEquals("(1, 2, 3)", ShortTuple.of((short) 1, (short) 2, (short) 3).toString());
        assertEquals("()", ShortTuple.create(null).toString());
    }

    // ====== ShortTuple2 Specific Tests ======

    @Test
    public void testAccept2() {
        ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
        AtomicInteger sum = new AtomicInteger(0);
        pair.accept((a, b) -> sum.set(a + b));
        assertEquals(7, sum.get());
    }

    @Test
    public void testMap2() {
        ShortTuple2 pair = ShortTuple.of((short) 10, (short) 3);
        int remainder = pair.map((a, b) -> a % b);
        assertEquals(1, remainder);
    }

    @Test
    public void testFilter2_True() {
        ShortTuple2 pair = ShortTuple.of((short) 10, (short) 20);
        Optional<ShortTuple2> result = pair.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(pair, result.get());
    }

    @Test
    public void testFilter2_False() {
        ShortTuple2 pair = ShortTuple.of((short) 20, (short) 10);
        Optional<ShortTuple2> result = pair.filter((a, b) -> a < b);
        assertFalse(result.isPresent());
    }

    // ====== ShortTuple3 Specific Tests ======

    @Test
    public void testAccept3() {
        ShortTuple3 triple = ShortTuple.of((short) 3, (short) 4, (short) 5);
        AtomicInteger sum = new AtomicInteger(0);
        triple.accept((a, b, c) -> sum.set(a + b + c));
        assertEquals(12, sum.get());
    }

    @Test
    public void testMap3() {
        ShortTuple3 triple = ShortTuple.of((short) 2, (short) 3, (short) 4);
        int volume = triple.map((l, w, h) -> l * w * h);
        assertEquals(24, volume);
    }

    @Test
    public void testFilter3_True() {
        ShortTuple3 triple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        Optional<ShortTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
        assertEquals(triple, result.get());
    }

    @Test
    public void testFilter3_False() {
        ShortTuple3 triple = ShortTuple.of((short) 3, (short) 2, (short) 1);
        Optional<ShortTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
        assertFalse(result.isPresent());
    }

    // ====== Edge Cases and Special Values ======

    @Test
    public void testWithZero() {
        ShortTuple3 tuple = ShortTuple.of((short) 0, (short) 1, (short) 2);
        assertEquals((short) 0, tuple.min());
        assertEquals((short) 2, tuple.max());
        assertEquals(3, tuple.sum());
    }

    @Test
    public void testWithNegatives() {
        ShortTuple3 tuple = ShortTuple.of((short) -5, (short) -10, (short) -1);
        assertEquals((short) -10, tuple.min());
        assertEquals((short) -1, tuple.max());
        assertEquals(-16, tuple.sum());
    }

    @Test
    public void testWithMaxValues() {
        short max = Short.MAX_VALUE / 2;
        ShortTuple2 tuple = ShortTuple.of(max, max);
        assertEquals(max, tuple.min());
        assertEquals(max, tuple.max());
    }

    @Test
    public void testWithSingleElement() {
        ShortTuple1 tuple = ShortTuple.of((short) 42);
        assertEquals((short) 42, tuple.min());
        assertEquals((short) 42, tuple.max());
        assertEquals((short) 42, tuple.median());
        assertEquals(42, tuple.sum());
        assertEquals(42.0, tuple.average(), 0.0001);
        assertTrue(tuple.contains((short) 42));
    }

    // ====== All Tuple Sizes Combined ======

    @Test
    public void testAllTupleSizes() {
        ShortTuple0 t0 = ShortTuple.create(null);
        ShortTuple1 t1 = ShortTuple.of((short) 1);
        ShortTuple2 t2 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple3 t3 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple4 t4 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        ShortTuple5 t5 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        ShortTuple6 t6 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        ShortTuple7 t7 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        ShortTuple8 t8 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        ShortTuple9 t9 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);

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
        ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
        assertThrows(IllegalArgumentException.class, () -> pair.accept((a, b) -> {
            throw new IllegalArgumentException("test");
        }));
    }

    @Test
    public void testMap2_Exception() {
        ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
        assertThrows(IllegalArgumentException.class, () -> pair.map((a, b) -> {
            throw new IllegalArgumentException("test");
        }));
    }

    @Test
    public void testFilter2_Exception() {
        ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
        assertThrows(IllegalArgumentException.class, () -> pair.filter((a, b) -> {
            throw new IllegalArgumentException("test");
        }));
    }

    // ====== Immutability Tests ======

    @Test
    public void testImmutability() {
        short[] arr = new short[] { 1, 2, 3 };
        ShortTuple3 tuple = ShortTuple.create(arr);
        arr[0] = 999;
        assertEquals((short) 1, tuple._1);
    }

    @Test
    public void testToArray_Independence() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        short[] arr1 = tuple.toArray();
        short[] arr2 = tuple.toArray();
        arr1[0] = 999;
        assertEquals((short) 1, arr2[0]);
    }
}
