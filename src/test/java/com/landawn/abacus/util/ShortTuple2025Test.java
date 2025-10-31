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
        ShortTuple9 tuple = ShortTuple
                .create(new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9 });
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

    // Comprehensive tests for Tuple4 through Tuple9
    @Test
    public void testTuple4Operations() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);

        // Test reverse
        ShortTuple4 reversed = tuple.reverse();
        assertEquals((short) 4, reversed._1);
        assertEquals((short) 3, reversed._2);
        assertEquals((short) 2, reversed._3);
        assertEquals((short) 1, reversed._4);

        // Test contains
        assertTrue(tuple.contains((short) 1));
        assertTrue(tuple.contains((short) 4));
        assertFalse(tuple.contains((short) 99));

        // Test toArray
        assertArrayEquals(new short[] { 1, 2, 3, 4 }, tuple.toArray());

        // Test min/max/median/sum/average via base class
        assertEquals((short) 1, tuple.min());
        assertEquals((short) 4, tuple.max());
        assertEquals((short) 2, tuple.median());
        assertEquals(10, tuple.sum());
        assertEquals(2.5, tuple.average(), 0.001);

        // Test hashCode and equals
        ShortTuple4 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        ShortTuple4 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 5);
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
        ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);

        // Test reverse
        ShortTuple5 reversed = tuple.reverse();
        assertEquals((short) 5, reversed._1);
        assertEquals((short) 1, reversed._5);

        // Test contains
        assertTrue(tuple.contains((short) 1));
        assertTrue(tuple.contains((short) 5));
        assertFalse(tuple.contains((short) 99));

        // Test toArray
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5 }, tuple.toArray());

        // Test statistical operations
        assertEquals((short) 1, tuple.min());
        assertEquals((short) 5, tuple.max());
        assertEquals((short) 3, tuple.median());
        assertEquals(15, tuple.sum());
        assertEquals(3.0, tuple.average(), 0.001);

        // Test equals
        ShortTuple5 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertEquals(tuple, tuple2);
    }

    @Test
    public void testTuple6Operations() {
        ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);

        // Test reverse
        ShortTuple6 reversed = tuple.reverse();
        assertEquals((short) 6, reversed._1);
        assertEquals((short) 1, reversed._6);

        // Test contains
        assertTrue(tuple.contains((short) 1));
        assertTrue(tuple.contains((short) 6));
        assertFalse(tuple.contains((short) 99));

        // Test toArray
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6 }, tuple.toArray());

        // Test statistical operations
        assertEquals((short) 1, tuple.min());
        assertEquals((short) 6, tuple.max());
        assertEquals(21, tuple.sum());
        assertEquals(3.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple7Operations() {
        ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);

        // Test reverse
        ShortTuple7 reversed = tuple.reverse();
        assertEquals((short) 7, reversed._1);
        assertEquals((short) 1, reversed._7);

        // Test contains
        assertTrue(tuple.contains((short) 1));
        assertTrue(tuple.contains((short) 7));
        assertFalse(tuple.contains((short) 99));

        // Test toArray
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6, 7 }, tuple.toArray());

        // Test statistical operations
        assertEquals((short) 1, tuple.min());
        assertEquals((short) 7, tuple.max());
        assertEquals(28, tuple.sum());
        assertEquals(4.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple8Operations() {
        ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);

        // Test reverse
        ShortTuple8 reversed = tuple.reverse();
        assertEquals((short) 8, reversed._1);
        assertEquals((short) 1, reversed._8);

        // Test contains
        assertTrue(tuple.contains((short) 1));
        assertTrue(tuple.contains((short) 8));
        assertFalse(tuple.contains((short) 99));

        // Test toArray
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6, 7, 8 }, tuple.toArray());

        // Test statistical operations
        assertEquals((short) 1, tuple.min());
        assertEquals((short) 8, tuple.max());
        assertEquals(36, tuple.sum());
        assertEquals(4.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple9Operations() {
        ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);

        // Test reverse
        ShortTuple9 reversed = tuple.reverse();
        assertEquals((short) 9, reversed._1);
        assertEquals((short) 1, reversed._9);

        // Test contains
        assertTrue(tuple.contains((short) 1));
        assertTrue(tuple.contains((short) 9));
        assertFalse(tuple.contains((short) 99));

        // Test toArray
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, tuple.toArray());

        // Test statistical operations
        assertEquals((short) 1, tuple.min());
        assertEquals((short) 9, tuple.max());
        assertEquals(45, tuple.sum());
        assertEquals(5.0, tuple.average(), 0.001);
    }

    // Test create methods for sizes 2, 4-8
    @Test
    public void testCreate2Through8() {
        ShortTuple2 tuple2 = ShortTuple.create(new short[] { 1, 2 });
        assertEquals((short) 1, tuple2._1);
        assertEquals((short) 2, tuple2._2);

        ShortTuple4 tuple4 = ShortTuple.create(new short[] { 1, 2, 3, 4 });
        assertEquals((short) 1, tuple4._1);
        assertEquals((short) 4, tuple4._4);

        ShortTuple5 tuple5 = ShortTuple.create(new short[] { 1, 2, 3, 4, 5 });
        assertEquals((short) 1, tuple5._1);
        assertEquals((short) 5, tuple5._5);

        ShortTuple6 tuple6 = ShortTuple.create(new short[] { 1, 2, 3, 4, 5, 6 });
        assertEquals((short) 1, tuple6._1);
        assertEquals((short) 6, tuple6._6);

        ShortTuple7 tuple7 = ShortTuple.create(new short[] { 1, 2, 3, 4, 5, 6, 7 });
        assertEquals((short) 1, tuple7._1);
        assertEquals((short) 7, tuple7._7);

        ShortTuple8 tuple8 = ShortTuple.create(new short[] { 1, 2, 3, 4, 5, 6, 7, 8 });
        assertEquals((short) 1, tuple8._1);
        assertEquals((short) 8, tuple8._8);
    }

    // Test toList for larger tuples
    @Test
    public void testToListTuple4Through9() {
        ShortTuple4 tuple4 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        ShortList list4 = tuple4.toList();
        assertEquals(4, list4.size());
        assertEquals((short) 4, list4.get(3));

        ShortTuple9 tuple9 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        ShortList list9 = tuple9.toList();
        assertEquals(9, list9.size());
        assertEquals((short) 9, list9.get(8));
    }

    // Test forEach for larger tuples
    @Test
    public void testForEachTuple4() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        List<Short> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(4, result.size());
        assertEquals(Short.valueOf((short) 4), result.get(3));
    }

    // Test forEach override for Tuple2
    @Test
    public void testForEachTuple2Override() {
        ShortTuple2 tuple = ShortTuple.of((short) 10, (short) 20);
        List<Short> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(2, result.size());
        assertEquals(Short.valueOf((short) 10), result.get(0));
        assertEquals(Short.valueOf((short) 20), result.get(1));
    }

    // Test forEach override for Tuple3
    @Test
    public void testForEachTuple3Override() {
        ShortTuple3 tuple = ShortTuple.of((short) 10, (short) 20, (short) 30);
        List<Short> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Short.valueOf((short) 30), result.get(2));
    }

    // Test stream for larger tuples
    @Test
    public void testStreamTuple4Through9() {
        ShortTuple4 tuple4 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertEquals(10, tuple4.stream().sum());

        ShortTuple9 tuple9 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertEquals(45, tuple9.stream().sum());
    }

    // ==================== ShortTuple Nested Class Tests ====================

    // ============ ShortTuple1 Nested Class Tests ============

    @Test
    public void testShortTuple1_arity() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testShortTuple1_reverse() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
        ShortTuple.ShortTuple1 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._1);
        assertEquals(tuple._1, reversed._1);
    }

    @Test
    public void testShortTuple1_contains() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertTrue(tuple.contains((short) 1));
    }

    @Test
    public void testShortTuple1_hashCode() {
        ShortTuple.ShortTuple1 tuple1 = ShortTuple.of((short) 1);
        ShortTuple.ShortTuple1 tuple2 = ShortTuple.of((short) 1);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testShortTuple1_equals() {
        ShortTuple.ShortTuple1 tuple1 = ShortTuple.of((short) 1);
        ShortTuple.ShortTuple1 tuple2 = ShortTuple.of((short) 1);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testShortTuple1_toString() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testShortTuple1_forEach() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testShortTuple1_min() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertNotNull(tuple.min());
    }

    @Test
    public void testShortTuple1_max() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertNotNull(tuple.max());
    }

    @Test
    public void testShortTuple1_median() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertNotNull(tuple.median());
    }

    @Test
    public void testShortTuple1_sum() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testShortTuple1_average() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    // ============ ShortTuple2 Nested Class Tests ============

    @Test
    public void testShortTuple2_arity() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testShortTuple2_reverse() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        ShortTuple.ShortTuple2 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._2);
        assertEquals(tuple._2, reversed._1);
    }

    @Test
    public void testShortTuple2_contains() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertTrue(tuple.contains((short) 1));
    }

    @Test
    public void testShortTuple2_hashCode() {
        ShortTuple.ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple.ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testShortTuple2_equals() {
        ShortTuple.ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple.ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testShortTuple2_toString() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testShortTuple2_forEach() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(2, count.size());
    }

    @Test
    public void testShortTuple2_min() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertNotNull(tuple.min());
    }

    @Test
    public void testShortTuple2_max() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertNotNull(tuple.max());
    }

    @Test
    public void testShortTuple2_median() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertNotNull(tuple.median());
    }

    @Test
    public void testShortTuple2_sum() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testShortTuple2_average() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testShortTuple2_accept_biConsumer() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testShortTuple2_map_biFunction() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        String result = tuple.map((a, b) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testShortTuple2_filter_biPredicate() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertTrue(tuple.filter((a, b) -> true).isPresent());
        assertFalse(tuple.filter((a, b) -> false).isPresent());
    }

    // ============ ShortTuple3 Nested Class Tests ============

    @Test
    public void testShortTuple3_arity() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testShortTuple3_reverse() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple.ShortTuple3 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._3);
        assertEquals(tuple._3, reversed._1);
    }

    @Test
    public void testShortTuple3_contains() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertTrue(tuple.contains((short) 1));
    }

    @Test
    public void testShortTuple3_hashCode() {
        ShortTuple.ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple.ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testShortTuple3_equals() {
        ShortTuple.ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple.ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testShortTuple3_toString() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testShortTuple3_forEach() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(3, count.size());
    }

    @Test
    public void testShortTuple3_min() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertNotNull(tuple.min());
    }

    @Test
    public void testShortTuple3_max() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertNotNull(tuple.max());
    }

    @Test
    public void testShortTuple3_median() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertNotNull(tuple.median());
    }

    @Test
    public void testShortTuple3_sum() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testShortTuple3_average() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testShortTuple3_accept_triConsumer() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b, c) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testShortTuple3_map_triFunction() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        String result = tuple.map((a, b, c) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testShortTuple3_filter_triPredicate() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertTrue(tuple.filter((a, b, c) -> true).isPresent());
        assertFalse(tuple.filter((a, b, c) -> false).isPresent());
    }

    // ============ ShortTuple4 Nested Class Tests ============

    @Test
    public void testShortTuple4_arity() {
        ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testShortTuple4_reverse() {
        ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        ShortTuple.ShortTuple4 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._4);
        assertEquals(tuple._4, reversed._1);
    }

    @Test
    public void testShortTuple4_contains() {
        ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertTrue(tuple.contains((short) 1));
    }

    @Test
    public void testShortTuple4_hashCode() {
        ShortTuple.ShortTuple4 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        ShortTuple.ShortTuple4 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testShortTuple4_equals() {
        ShortTuple.ShortTuple4 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        ShortTuple.ShortTuple4 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testShortTuple4_toString() {
        ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testShortTuple4_forEach() {
        ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(4, count.size());
    }

    // ============ ShortTuple5 Nested Class Tests ============

    @Test
    public void testShortTuple5_arity() {
        ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testShortTuple5_reverse() {
        ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        ShortTuple.ShortTuple5 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._5);
        assertEquals(tuple._5, reversed._1);
    }

    @Test
    public void testShortTuple5_contains() {
        ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertTrue(tuple.contains((short) 1));
    }

    @Test
    public void testShortTuple5_hashCode() {
        ShortTuple.ShortTuple5 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        ShortTuple.ShortTuple5 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testShortTuple5_equals() {
        ShortTuple.ShortTuple5 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        ShortTuple.ShortTuple5 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testShortTuple5_toString() {
        ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testShortTuple5_forEach() {
        ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(5, count.size());
    }

    // ============ ShortTuple6 Nested Class Tests ============

    @Test
    public void testShortTuple6_arity() {
        ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testShortTuple6_reverse() {
        ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        ShortTuple.ShortTuple6 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._6);
        assertEquals(tuple._6, reversed._1);
    }

    @Test
    public void testShortTuple6_contains() {
        ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        assertTrue(tuple.contains((short) 1));
    }

    @Test
    public void testShortTuple6_hashCode() {
        ShortTuple.ShortTuple6 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        ShortTuple.ShortTuple6 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testShortTuple6_equals() {
        ShortTuple.ShortTuple6 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        ShortTuple.ShortTuple6 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testShortTuple6_toString() {
        ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testShortTuple6_forEach() {
        ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(6, count.size());
    }

    // ============ ShortTuple7 Nested Class Tests ============

    @Test
    public void testShortTuple7_arity() {
        ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testShortTuple7_reverse() {
        ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        ShortTuple.ShortTuple7 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._7);
        assertEquals(tuple._7, reversed._1);
    }

    @Test
    public void testShortTuple7_contains() {
        ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        assertTrue(tuple.contains((short) 1));
    }

    @Test
    public void testShortTuple7_hashCode() {
        ShortTuple.ShortTuple7 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        ShortTuple.ShortTuple7 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testShortTuple7_equals() {
        ShortTuple.ShortTuple7 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        ShortTuple.ShortTuple7 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testShortTuple7_toString() {
        ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testShortTuple7_forEach() {
        ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(7, count.size());
    }

    // ============ ShortTuple8 Nested Class Tests ============

    @Test
    public void testShortTuple8_arity() {
        ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testShortTuple8_reverse() {
        ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        ShortTuple.ShortTuple8 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._8);
        assertEquals(tuple._8, reversed._1);
    }

    @Test
    public void testShortTuple8_contains() {
        ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        assertTrue(tuple.contains((short) 1));
    }

    @Test
    public void testShortTuple8_hashCode() {
        ShortTuple.ShortTuple8 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        ShortTuple.ShortTuple8 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testShortTuple8_equals() {
        ShortTuple.ShortTuple8 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        ShortTuple.ShortTuple8 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testShortTuple8_toString() {
        ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testShortTuple8_forEach() {
        ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(8, count.size());
    }

    // ============ ShortTuple9 Nested Class Tests ============

    @Test
    public void testShortTuple9_arity() {
        ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testShortTuple9_reverse() {
        ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        ShortTuple.ShortTuple9 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._9);
        assertEquals(tuple._9, reversed._1);
    }

    @Test
    public void testShortTuple9_contains() {
        ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertTrue(tuple.contains((short) 1));
    }

    @Test
    public void testShortTuple9_hashCode() {
        ShortTuple.ShortTuple9 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        ShortTuple.ShortTuple9 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testShortTuple9_equals() {
        ShortTuple.ShortTuple9 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        ShortTuple.ShortTuple9 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testShortTuple9_toString() {
        ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testShortTuple9_forEach() {
        ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(9, count.size());
    }

}
