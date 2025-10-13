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
import com.landawn.abacus.util.stream.ShortStream;
import com.landawn.abacus.TestBase;

/**
 * Comprehensive test suite for ShortTuple and its nested classes.
 * Tests all public methods including factory methods, statistical operations,
 * collection conversions, and special methods in Tuple2 and Tuple3.
 */
@Tag("2025")
public class ShortTuple2025Test extends TestBase {

    // Factory method tests
    @Test
    public void testOf1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertEquals((short) 1, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf2() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 2, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 2, tuple._2);
        assertEquals((short) 3, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf4() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 2, tuple._2);
        assertEquals((short) 3, tuple._3);
        assertEquals((short) 4, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf5() {
        ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 5, tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf6() {
        ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 6, tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf7() {
        ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 7, tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf8() {
        ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 8, tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf9() {
        ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 9, tuple._9);
        assertEquals(9, tuple.arity());
    }

    // Create method tests
    @Test
    public void testCreateEmpty() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreateNull() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate1() {
        ShortTuple1 tuple = ShortTuple.create(new short[] { (short) 1 });
        assertEquals((short) 1, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate3() {
        ShortTuple3 tuple = ShortTuple.create(new short[] { (short) 1, (short) 2, (short) 3 });
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 2, tuple._2);
        assertEquals((short) 3, tuple._3);
    }

    @Test
    public void testCreate9() {
        ShortTuple9 tuple = ShortTuple.create(new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9 });
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 9, tuple._9);
    }

    @Test
    public void testCreateTooMany() {
        assertThrows(IllegalArgumentException.class, () -> {
            ShortTuple.create(new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9, (short) 10 });
        });
    }

    // Statistical method tests - min
    @Test
    public void testMinTuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertEquals((short) 1, tuple.min());
    }

    @Test
    public void testMinTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 3, (short) 1, (short) 2);
        assertEquals((short) 1, tuple.min());
    }

    @Test
    public void testMinTuple0ThrowsException() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    // Statistical method tests - max
    @Test
    public void testMaxTuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertEquals((short) 1, tuple.max());
    }

    @Test
    public void testMaxTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 3, (short) 1, (short) 2);
        assertEquals((short) 3, tuple.max());
    }

    @Test
    public void testMaxTuple0ThrowsException() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    // Statistical method tests - median
    @Test
    public void testMedianTuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertEquals((short) 1, tuple.median());
    }

    @Test
    public void testMedianTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 3, (short) 1, (short) 2);
        assertEquals((short) 2, tuple.median());
    }

    @Test
    public void testMedianTuple0ThrowsException() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    // Statistical method tests - sum
    @Test
    public void testSumTuple0() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void testSumTuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertEquals(1, tuple.sum());
    }

    @Test
    public void testSumTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(6, tuple.sum());
    }

    // Statistical method tests - average
    @Test
    public void testAverageTuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertEquals(1.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(2.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple0ThrowsException() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    // Reverse tests
    @Test
    public void testReverseTuple0() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        ShortTuple<ShortTuple0> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testReverseTuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        ShortTuple1 reversed = tuple.reverse();
        assertEquals((short) 1, reversed._1);
    }

    @Test
    public void testReverseTuple2() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        ShortTuple2 reversed = tuple.reverse();
        assertEquals((short) 2, reversed._1);
        assertEquals((short) 1, reversed._2);
    }

    @Test
    public void testReverseTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 reversed = tuple.reverse();
        assertEquals((short) 3, reversed._1);
        assertEquals((short) 2, reversed._2);
        assertEquals((short) 1, reversed._3);
    }

    // Contains tests
    @Test
    public void testContainsTuple0() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        assertFalse(tuple.contains((short) 1));
    }

    @Test
    public void testContainsTuple1True() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertTrue(tuple.contains((short) 1));
    }

    @Test
    public void testContainsTuple1False() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertFalse(tuple.contains((short) 99));
    }

    @Test
    public void testContainsTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertTrue(tuple.contains((short) 1));
        assertTrue(tuple.contains((short) 2));
        assertTrue(tuple.contains((short) 3));
        assertFalse(tuple.contains((short) 99));
    }

    // toArray tests
    @Test
    public void testToArrayTuple0() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        short[] array = tuple.toArray();
        assertEquals(0, array.length);
    }

    @Test
    public void testToArrayTuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        short[] array = tuple.toArray();
        assertArrayEquals(new short[] { (short) 1 }, array);
    }

    @Test
    public void testToArrayTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        short[] array = tuple.toArray();
        assertArrayEquals(new short[] { (short) 1, (short) 2, (short) 3 }, array);
    }

    @Test
    public void testToArrayModificationDoesNotAffectTuple() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        short[] array = tuple.toArray();
        array[0] = (short) 100;
        assertEquals((short) 1, tuple._1);
    }

    // toList tests
    @Test
    public void testToListTuple0() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        ShortList list = tuple.toList();
        assertEquals(0, list.size());
    }

    @Test
    public void testToListTuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        ShortList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals((short) 1, list.get(0));
    }

    @Test
    public void testToListTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals((short) 1, list.get(0));
        assertEquals((short) 2, list.get(1));
        assertEquals((short) 3, list.get(2));
    }

    // forEach tests
    @Test
    public void testForEachTuple0() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        List<Short> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(0, result.size());
    }

    @Test
    public void testForEachTuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        List<Short> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(1, result.size());
        assertEquals(Short.valueOf((short) 1), result.get(0));
    }

    @Test
    public void testForEachTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        List<Short> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Short.valueOf((short) 1), result.get(0));
        assertEquals(Short.valueOf((short) 2), result.get(1));
        assertEquals(Short.valueOf((short) 3), result.get(2));
    }

    // stream tests
    @Test
    public void testStreamTuple0() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        ShortStream stream = tuple.stream();
        assertEquals(0, stream.count());
    }

    @Test
    public void testStreamTuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        ShortStream stream = tuple.stream();
        assertEquals(1, stream.sum());
    }

    @Test
    public void testStreamTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortStream stream = tuple.stream();
        assertEquals(6, stream.sum());
    }

    // hashCode tests
    @Test
    public void testHashCodeTuple1() {
        ShortTuple1 tuple1 = ShortTuple.of((short) 1);
        ShortTuple1 tuple2 = ShortTuple.of((short) 1);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple2() {
        ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple3() {
        ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // equals tests
    @Test
    public void testEqualsSameObject() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEqualsNull() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertNotEquals(null, tuple);
    }

    @Test
    public void testEqualsDifferentClass() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertNotEquals("not a tuple", tuple);
    }

    @Test
    public void testEqualsTuple1() {
        ShortTuple1 tuple1 = ShortTuple.of((short) 1);
        ShortTuple1 tuple2 = ShortTuple.of((short) 1);
        ShortTuple1 tuple3 = ShortTuple.of((short) 99);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple2() {
        ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple2 tuple3 = ShortTuple.of((short) 1, (short) 3);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple3() {
        ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 4);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    // toString tests
    @Test
    public void testToStringTuple0() {
        ShortTuple<ShortTuple0> tuple = ShortTuple.create(new short[0]);
        assertEquals("[]", tuple.toString());
    }

    @Test
    public void testToStringTuple1() {
        ShortTuple1 tuple = ShortTuple.of((short) 1);
        String str = tuple.toString();
        assertTrue(str.contains("1"));
    }

    @Test
    public void testToStringTuple3() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        String str = tuple.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
    }

    // Tuple2 special methods - accept
    @Test
    public void testTuple2Accept() {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
        List<Short> result = new ArrayList<>();
        tuple.accept((a, b) -> {
            result.add(a);
            result.add(b);
        });
        assertEquals(2, result.size());
        assertEquals(Short.valueOf((short) 3), result.get(0));
        assertEquals(Short.valueOf((short) 4), result.get(1));
    }

    // Tuple2 special methods - map
    @Test
    public void testTuple2Map() {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
        int result = tuple.map((a, b) -> a * b);
        assertEquals(12, result);
    }

    // Tuple2 special methods - filter
    @Test
    public void testTuple2FilterTrue() {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
        var result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2FilterFalse() {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
        var result = tuple.filter((a, b) -> a + b > 10);
        assertFalse(result.isPresent());
    }

    // Tuple3 special methods - accept
    @Test
    public void testTuple3Accept() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        List<Short> result = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            result.add(a);
            result.add(b);
            result.add(c);
        });
        assertEquals(3, result.size());
        assertEquals(Short.valueOf((short) 1), result.get(0));
        assertEquals(Short.valueOf((short) 2), result.get(1));
        assertEquals(Short.valueOf((short) 3), result.get(2));
    }

    // Tuple3 special methods - map
    @Test
    public void testTuple3Map() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        int result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6, result);
    }

    // Tuple3 special methods - filter
    @Test
    public void testTuple3FilterTrue() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        var result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple3FilterFalse() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        var result = tuple.filter((a, b, c) -> a + b + c > 10);
        assertFalse(result.isPresent());
    }

    // arity tests for all tuple sizes
    @Test
    public void testArity() {
        assertEquals(0, ShortTuple.create(new short[0]).arity());
        assertEquals(1, ShortTuple.of((short) 1).arity());
        assertEquals(2, ShortTuple.of((short) 1, (short) 2).arity());
        assertEquals(3, ShortTuple.of((short) 1, (short) 2, (short) 3).arity());
        assertEquals(4, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4).arity());
        assertEquals(5, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).arity());
        assertEquals(6, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6).arity());
        assertEquals(7, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7).arity());
        assertEquals(8, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8).arity());
        assertEquals(9, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9).arity());
    }
}
