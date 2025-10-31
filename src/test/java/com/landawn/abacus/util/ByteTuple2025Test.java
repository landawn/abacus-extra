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
import com.landawn.abacus.util.ByteTuple.ByteTuple0;
import com.landawn.abacus.util.ByteTuple.ByteTuple1;
import com.landawn.abacus.util.ByteTuple.ByteTuple2;
import com.landawn.abacus.util.ByteTuple.ByteTuple3;
import com.landawn.abacus.util.ByteTuple.ByteTuple4;
import com.landawn.abacus.util.ByteTuple.ByteTuple5;
import com.landawn.abacus.util.ByteTuple.ByteTuple6;
import com.landawn.abacus.util.ByteTuple.ByteTuple7;
import com.landawn.abacus.util.ByteTuple.ByteTuple8;
import com.landawn.abacus.util.ByteTuple.ByteTuple9;
import com.landawn.abacus.util.stream.ByteStream;

/**
 * Comprehensive test suite for ByteTuple and its nested classes.
 * Tests all public methods including factory methods, statistical operations,
 * collection conversions, and special methods in Tuple2 and Tuple3.
 */
@Tag("2025")
public class ByteTuple2025Test extends TestBase {

    // Factory method tests
    @Test
    public void testOf1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertEquals((byte) 1, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf2() {
        ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 2, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 2, tuple._2);
        assertEquals((byte) 3, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf4() {
        ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 2, tuple._2);
        assertEquals((byte) 3, tuple._3);
        assertEquals((byte) 4, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf5() {
        ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 5, tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf6() {
        ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 6, tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf7() {
        ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 7, tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf8() {
        ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 8, tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf9() {
        ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 9, tuple._9);
        assertEquals(9, tuple.arity());
    }

    // Create method tests
    @Test
    public void testCreateEmpty() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreateNull() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate1() {
        ByteTuple1 tuple = ByteTuple.create(new byte[] { (byte) 1 });
        assertEquals((byte) 1, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate3() {
        ByteTuple3 tuple = ByteTuple.create(new byte[] { (byte) 1, (byte) 2, (byte) 3 });
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 2, tuple._2);
        assertEquals((byte) 3, tuple._3);
    }

    @Test
    public void testCreate9() {
        ByteTuple9 tuple = ByteTuple.create(new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9 });
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 9, tuple._9);
    }

    @Test
    public void testCreateTooMany() {
        assertThrows(IllegalArgumentException.class, () -> {
            ByteTuple.create(new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10 });
        });
    }

    // Statistical method tests - min
    @Test
    public void testMinTuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertEquals((byte) 1, tuple.min());
    }

    @Test
    public void testMinTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 3, (byte) 1, (byte) 2);
        assertEquals((byte) 1, tuple.min());
    }

    @Test
    public void testMinTuple0ThrowsException() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    // Statistical method tests - max
    @Test
    public void testMaxTuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertEquals((byte) 1, tuple.max());
    }

    @Test
    public void testMaxTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 3, (byte) 1, (byte) 2);
        assertEquals((byte) 3, tuple.max());
    }

    @Test
    public void testMaxTuple0ThrowsException() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    // Statistical method tests - median
    @Test
    public void testMedianTuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertEquals((byte) 1, tuple.median());
    }

    @Test
    public void testMedianTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 3, (byte) 1, (byte) 2);
        assertEquals((byte) 2, tuple.median());
    }

    @Test
    public void testMedianTuple0ThrowsException() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    // Statistical method tests - sum
    @Test
    public void testSumTuple0() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void testSumTuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertEquals(1, tuple.sum());
    }

    @Test
    public void testSumTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertEquals(6, tuple.sum());
    }

    // Statistical method tests - average
    @Test
    public void testAverageTuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertEquals(1.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertEquals(2.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple0ThrowsException() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    // Reverse tests
    @Test
    public void testReverseTuple0() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        ByteTuple<ByteTuple0> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testReverseTuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        ByteTuple1 reversed = tuple.reverse();
        assertEquals((byte) 1, reversed._1);
    }

    @Test
    public void testReverseTuple2() {
        ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        ByteTuple2 reversed = tuple.reverse();
        assertEquals((byte) 2, reversed._1);
        assertEquals((byte) 1, reversed._2);
    }

    @Test
    public void testReverseTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        ByteTuple3 reversed = tuple.reverse();
        assertEquals((byte) 3, reversed._1);
        assertEquals((byte) 2, reversed._2);
        assertEquals((byte) 1, reversed._3);
    }

    // Contains tests
    @Test
    public void testContainsTuple0() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        assertFalse(tuple.contains((byte) 1));
    }

    @Test
    public void testContainsTuple1True() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertTrue(tuple.contains((byte) 1));
    }

    @Test
    public void testContainsTuple1False() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertFalse(tuple.contains((byte) 99));
    }

    @Test
    public void testContainsTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertTrue(tuple.contains((byte) 1));
        assertTrue(tuple.contains((byte) 2));
        assertTrue(tuple.contains((byte) 3));
        assertFalse(tuple.contains((byte) 99));
    }

    // toArray tests
    @Test
    public void testToArrayTuple0() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        byte[] array = tuple.toArray();
        assertEquals(0, array.length);
    }

    @Test
    public void testToArrayTuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        byte[] array = tuple.toArray();
        assertArrayEquals(new byte[] { (byte) 1 }, array);
    }

    @Test
    public void testToArrayTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        byte[] array = tuple.toArray();
        assertArrayEquals(new byte[] { (byte) 1, (byte) 2, (byte) 3 }, array);
    }

    @Test
    public void testToArrayModificationDoesNotAffectTuple() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        byte[] array = tuple.toArray();
        array[0] = (byte) 100;
        assertEquals((byte) 1, tuple._1);
    }

    // toList tests
    @Test
    public void testToListTuple0() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        ByteList list = tuple.toList();
        assertEquals(0, list.size());
    }

    @Test
    public void testToListTuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        ByteList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals((byte) 1, list.get(0));
    }

    @Test
    public void testToListTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        ByteList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    // forEach tests
    @Test
    public void testForEachTuple0() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        List<Byte> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(0, result.size());
    }

    @Test
    public void testForEachTuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        List<Byte> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(1, result.size());
        assertEquals(Byte.valueOf((byte) 1), result.get(0));
    }

    @Test
    public void testForEachTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        List<Byte> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Byte.valueOf((byte) 1), result.get(0));
        assertEquals(Byte.valueOf((byte) 2), result.get(1));
        assertEquals(Byte.valueOf((byte) 3), result.get(2));
    }

    // stream tests
    @Test
    public void testStreamTuple0() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        ByteStream stream = tuple.stream();
        assertEquals(0, stream.count());
    }

    @Test
    public void testStreamTuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        ByteStream stream = tuple.stream();
        assertEquals(1, stream.sum());
    }

    @Test
    public void testStreamTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        ByteStream stream = tuple.stream();
        assertEquals(6, stream.sum());
    }

    // hashCode tests
    @Test
    public void testHashCodeTuple1() {
        ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
        ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple2() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple3() {
        ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // equals tests
    @Test
    public void testEqualsSameObject() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEqualsNull() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertNotEquals(null, tuple);
    }

    @Test
    public void testEqualsDifferentClass() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertNotEquals("not a tuple", tuple);
    }

    @Test
    public void testEqualsTuple1() {
        ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
        ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
        ByteTuple1 tuple3 = ByteTuple.of((byte) 99);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple2() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
        ByteTuple2 tuple3 = ByteTuple.of((byte) 1, (byte) 3);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple3() {
        ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        ByteTuple3 tuple3 = ByteTuple.of((byte) 1, (byte) 2, (byte) 4);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    // toString tests
    @Test
    public void testToStringTuple0() {
        ByteTuple<ByteTuple0> tuple = ByteTuple.create(new byte[0]);
        assertEquals("[]", tuple.toString());
    }

    @Test
    public void testToStringTuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 1);
        String str = tuple.toString();
        assertTrue(str.contains("1"));
    }

    @Test
    public void testToStringTuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        String str = tuple.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
    }

    // Tuple2 special methods - accept
    @Test
    public void testTuple2Accept() {
        ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
        List<Byte> result = new ArrayList<>();
        tuple.accept((a, b) -> {
            result.add(a);
            result.add(b);
        });
        assertEquals(2, result.size());
        assertEquals(Byte.valueOf((byte) 3), result.get(0));
        assertEquals(Byte.valueOf((byte) 4), result.get(1));
    }

    // Tuple2 special methods - map
    @Test
    public void testTuple2Map() {
        ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
        int result = tuple.map((a, b) -> a * b);
        assertEquals(12, result);
    }

    // Tuple2 special methods - filter
    @Test
    public void testTuple2FilterTrue() {
        ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
        var result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2FilterFalse() {
        ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
        var result = tuple.filter((a, b) -> a + b > 10);
        assertFalse(result.isPresent());
    }

    // Tuple3 special methods - accept
    @Test
    public void testTuple3Accept() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        List<Byte> result = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            result.add(a);
            result.add(b);
            result.add(c);
        });
        assertEquals(3, result.size());
        assertEquals(Byte.valueOf((byte) 1), result.get(0));
        assertEquals(Byte.valueOf((byte) 2), result.get(1));
        assertEquals(Byte.valueOf((byte) 3), result.get(2));
    }

    // Tuple3 special methods - map
    @Test
    public void testTuple3Map() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        int result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6, result);
    }

    // Tuple3 special methods - filter
    @Test
    public void testTuple3FilterTrue() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        var result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple3FilterFalse() {
        ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        var result = tuple.filter((a, b, c) -> a + b + c > 10);
        assertFalse(result.isPresent());
    }

    // arity tests for all tuple sizes
    @Test
    public void testArity() {
        assertEquals(0, ByteTuple.create(new byte[0]).arity());
        assertEquals(1, ByteTuple.of((byte) 1).arity());
        assertEquals(2, ByteTuple.of((byte) 1, (byte) 2).arity());
        assertEquals(3, ByteTuple.of((byte) 1, (byte) 2, (byte) 3).arity());
        assertEquals(4, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4).arity());
        assertEquals(5, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5).arity());
        assertEquals(6, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6).arity());
        assertEquals(7, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7).arity());
        assertEquals(8, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8).arity());
        assertEquals(9, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9).arity());
    }

    // Comprehensive tests for Tuple4 through Tuple9
    @Test
    public void testTuple4Operations() {
        ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);

        // Test reverse
        ByteTuple4 reversed = tuple.reverse();
        assertEquals((byte) 4, reversed._1);
        assertEquals((byte) 3, reversed._2);
        assertEquals((byte) 2, reversed._3);
        assertEquals((byte) 1, reversed._4);

        // Test contains
        assertTrue(tuple.contains((byte) 1));
        assertTrue(tuple.contains((byte) 4));
        assertFalse(tuple.contains((byte) 99));

        // Test toArray
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, tuple.toArray());

        // Test min/max/median/sum/average via base class
        assertEquals((byte) 1, tuple.min());
        assertEquals((byte) 4, tuple.max());
        assertEquals((byte) 2, tuple.median());
        assertEquals(10, tuple.sum());
        assertEquals(2.5, tuple.average(), 0.001);

        // Test hashCode and equals
        ByteTuple4 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        ByteTuple4 tuple3 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 5);
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
        ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);

        // Test reverse
        ByteTuple5 reversed = tuple.reverse();
        assertEquals((byte) 5, reversed._1);
        assertEquals((byte) 1, reversed._5);

        // Test contains
        assertTrue(tuple.contains((byte) 1));
        assertTrue(tuple.contains((byte) 5));
        assertFalse(tuple.contains((byte) 99));

        // Test toArray
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, tuple.toArray());

        // Test statistical operations
        assertEquals((byte) 1, tuple.min());
        assertEquals((byte) 5, tuple.max());
        assertEquals((byte) 3, tuple.median());
        assertEquals(15, tuple.sum());
        assertEquals(3.0, tuple.average(), 0.001);

        // Test equals
        ByteTuple5 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        assertEquals(tuple, tuple2);
    }

    @Test
    public void testTuple6Operations() {
        ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);

        // Test reverse
        ByteTuple6 reversed = tuple.reverse();
        assertEquals((byte) 6, reversed._1);
        assertEquals((byte) 1, reversed._6);

        // Test contains
        assertTrue(tuple.contains((byte) 1));
        assertTrue(tuple.contains((byte) 6));
        assertFalse(tuple.contains((byte) 99));

        // Test toArray
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6 }, tuple.toArray());

        // Test statistical operations
        assertEquals((byte) 1, tuple.min());
        assertEquals((byte) 6, tuple.max());
        assertEquals(21, tuple.sum());
        assertEquals(3.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple7Operations() {
        ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);

        // Test reverse
        ByteTuple7 reversed = tuple.reverse();
        assertEquals((byte) 7, reversed._1);
        assertEquals((byte) 1, reversed._7);

        // Test contains
        assertTrue(tuple.contains((byte) 1));
        assertTrue(tuple.contains((byte) 7));
        assertFalse(tuple.contains((byte) 99));

        // Test toArray
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7 }, tuple.toArray());

        // Test statistical operations
        assertEquals((byte) 1, tuple.min());
        assertEquals((byte) 7, tuple.max());
        assertEquals(28, tuple.sum());
        assertEquals(4.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple8Operations() {
        ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);

        // Test reverse
        ByteTuple8 reversed = tuple.reverse();
        assertEquals((byte) 8, reversed._1);
        assertEquals((byte) 1, reversed._8);

        // Test contains
        assertTrue(tuple.contains((byte) 1));
        assertTrue(tuple.contains((byte) 8));
        assertFalse(tuple.contains((byte) 99));

        // Test toArray
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }, tuple.toArray());

        // Test statistical operations
        assertEquals((byte) 1, tuple.min());
        assertEquals((byte) 8, tuple.max());
        assertEquals(36, tuple.sum());
        assertEquals(4.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple9Operations() {
        ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);

        // Test reverse
        ByteTuple9 reversed = tuple.reverse();
        assertEquals((byte) 9, reversed._1);
        assertEquals((byte) 1, reversed._9);

        // Test contains
        assertTrue(tuple.contains((byte) 1));
        assertTrue(tuple.contains((byte) 9));
        assertFalse(tuple.contains((byte) 99));

        // Test toArray
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, tuple.toArray());

        // Test statistical operations
        assertEquals((byte) 1, tuple.min());
        assertEquals((byte) 9, tuple.max());
        assertEquals(45, tuple.sum());
        assertEquals(5.0, tuple.average(), 0.001);
    }

    // Test create methods for sizes 2, 4-8
    @Test
    public void testCreate2Through8() {
        ByteTuple2 tuple2 = ByteTuple.create(new byte[] { 1, 2 });
        assertEquals((byte) 1, tuple2._1);
        assertEquals((byte) 2, tuple2._2);

        ByteTuple4 tuple4 = ByteTuple.create(new byte[] { 1, 2, 3, 4 });
        assertEquals((byte) 1, tuple4._1);
        assertEquals((byte) 4, tuple4._4);

        ByteTuple5 tuple5 = ByteTuple.create(new byte[] { 1, 2, 3, 4, 5 });
        assertEquals((byte) 1, tuple5._1);
        assertEquals((byte) 5, tuple5._5);

        ByteTuple6 tuple6 = ByteTuple.create(new byte[] { 1, 2, 3, 4, 5, 6 });
        assertEquals((byte) 1, tuple6._1);
        assertEquals((byte) 6, tuple6._6);

        ByteTuple7 tuple7 = ByteTuple.create(new byte[] { 1, 2, 3, 4, 5, 6, 7 });
        assertEquals((byte) 1, tuple7._1);
        assertEquals((byte) 7, tuple7._7);

        ByteTuple8 tuple8 = ByteTuple.create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
        assertEquals((byte) 1, tuple8._1);
        assertEquals((byte) 8, tuple8._8);
    }

    // Test toList for larger tuples
    @Test
    public void testToListTuple4Through9() {
        ByteTuple4 tuple4 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        ByteList list4 = tuple4.toList();
        assertEquals(4, list4.size());
        assertEquals((byte) 4, list4.get(3));

        ByteTuple9 tuple9 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        ByteList list9 = tuple9.toList();
        assertEquals(9, list9.size());
        assertEquals((byte) 9, list9.get(8));
    }

    // Test forEach for larger tuples
    @Test
    public void testForEachTuple4() {
        ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        List<Byte> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(4, result.size());
        assertEquals(Byte.valueOf((byte) 4), result.get(3));
    }

    // Test forEach override for Tuple2
    @Test
    public void testForEachTuple2Override() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        List<Byte> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(2, result.size());
        assertEquals(Byte.valueOf((byte) 10), result.get(0));
        assertEquals(Byte.valueOf((byte) 20), result.get(1));
    }

    // Test forEach override for Tuple3
    @Test
    public void testForEachTuple3Override() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        List<Byte> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Byte.valueOf((byte) 30), result.get(2));
    }

    // Test stream for larger tuples
    @Test
    public void testStreamTuple4Through9() {
        ByteTuple4 tuple4 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        assertEquals(10, tuple4.stream().sum());

        ByteTuple9 tuple9 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        assertEquals(45, tuple9.stream().sum());
    }

    // ==================== ByteTuple Nested Class Tests ====================

    // ============ ByteTuple1 Nested Class Tests ============

    @Test
    public void testByteTuple1_arity() {
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testByteTuple1_reverse() {
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
        ByteTuple.ByteTuple1 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._1);
        assertEquals(tuple._1, reversed._1);
    }

    @Test
    public void testByteTuple1_contains() {
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertTrue(tuple.contains((byte) 1));
    }

    @Test
    public void testByteTuple1_hashCode() {
        ByteTuple.ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
        ByteTuple.ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testByteTuple1_equals() {
        ByteTuple.ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
        ByteTuple.ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testByteTuple1_toString() {
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testByteTuple1_forEach() {
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testByteTuple1_min() {
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertNotNull(tuple.min());
    }

    @Test
    public void testByteTuple1_max() {
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertNotNull(tuple.max());
    }

    @Test
    public void testByteTuple1_median() {
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertNotNull(tuple.median());
    }

    @Test
    public void testByteTuple1_sum() {
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testByteTuple1_average() {
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    // ============ ByteTuple2 Nested Class Tests ============

    @Test
    public void testByteTuple2_arity() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testByteTuple2_reverse() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        ByteTuple.ByteTuple2 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._2);
        assertEquals(tuple._2, reversed._1);
    }

    @Test
    public void testByteTuple2_contains() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        assertTrue(tuple.contains((byte) 1));
    }

    @Test
    public void testByteTuple2_hashCode() {
        ByteTuple.ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
        ByteTuple.ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testByteTuple2_equals() {
        ByteTuple.ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
        ByteTuple.ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testByteTuple2_toString() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testByteTuple2_forEach() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(2, count.size());
    }

    @Test
    public void testByteTuple2_min() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        assertNotNull(tuple.min());
    }

    @Test
    public void testByteTuple2_max() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        assertNotNull(tuple.max());
    }

    @Test
    public void testByteTuple2_median() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        assertNotNull(tuple.median());
    }

    @Test
    public void testByteTuple2_sum() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testByteTuple2_average() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testByteTuple2_accept_biConsumer() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testByteTuple2_map_biFunction() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        String result = tuple.map((a, b) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testByteTuple2_filter_biPredicate() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
        assertTrue(tuple.filter((a, b) -> true).isPresent());
        assertFalse(tuple.filter((a, b) -> false).isPresent());
    }

    // ============ ByteTuple3 Nested Class Tests ============

    @Test
    public void testByteTuple3_arity() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testByteTuple3_reverse() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        ByteTuple.ByteTuple3 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._3);
        assertEquals(tuple._3, reversed._1);
    }

    @Test
    public void testByteTuple3_contains() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertTrue(tuple.contains((byte) 1));
    }

    @Test
    public void testByteTuple3_hashCode() {
        ByteTuple.ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        ByteTuple.ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testByteTuple3_equals() {
        ByteTuple.ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        ByteTuple.ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testByteTuple3_toString() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testByteTuple3_forEach() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(3, count.size());
    }

    @Test
    public void testByteTuple3_min() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertNotNull(tuple.min());
    }

    @Test
    public void testByteTuple3_max() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertNotNull(tuple.max());
    }

    @Test
    public void testByteTuple3_median() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertNotNull(tuple.median());
    }

    @Test
    public void testByteTuple3_sum() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testByteTuple3_average() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testByteTuple3_accept_triConsumer() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b, c) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testByteTuple3_map_triFunction() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        String result = tuple.map((a, b, c) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testByteTuple3_filter_triPredicate() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
        assertTrue(tuple.filter((a, b, c) -> true).isPresent());
        assertFalse(tuple.filter((a, b, c) -> false).isPresent());
    }

    // ============ ByteTuple4 Nested Class Tests ============

    @Test
    public void testByteTuple4_arity() {
        ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testByteTuple4_reverse() {
        ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        ByteTuple.ByteTuple4 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._4);
        assertEquals(tuple._4, reversed._1);
    }

    @Test
    public void testByteTuple4_contains() {
        ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        assertTrue(tuple.contains((byte) 1));
    }

    @Test
    public void testByteTuple4_hashCode() {
        ByteTuple.ByteTuple4 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        ByteTuple.ByteTuple4 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testByteTuple4_equals() {
        ByteTuple.ByteTuple4 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        ByteTuple.ByteTuple4 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testByteTuple4_toString() {
        ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testByteTuple4_forEach() {
        ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(4, count.size());
    }

    // ============ ByteTuple5 Nested Class Tests ============

    @Test
    public void testByteTuple5_arity() {
        ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testByteTuple5_reverse() {
        ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        ByteTuple.ByteTuple5 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._5);
        assertEquals(tuple._5, reversed._1);
    }

    @Test
    public void testByteTuple5_contains() {
        ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        assertTrue(tuple.contains((byte) 1));
    }

    @Test
    public void testByteTuple5_hashCode() {
        ByteTuple.ByteTuple5 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        ByteTuple.ByteTuple5 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testByteTuple5_equals() {
        ByteTuple.ByteTuple5 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        ByteTuple.ByteTuple5 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testByteTuple5_toString() {
        ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testByteTuple5_forEach() {
        ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(5, count.size());
    }

    // ============ ByteTuple6 Nested Class Tests ============

    @Test
    public void testByteTuple6_arity() {
        ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testByteTuple6_reverse() {
        ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        ByteTuple.ByteTuple6 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._6);
        assertEquals(tuple._6, reversed._1);
    }

    @Test
    public void testByteTuple6_contains() {
        ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        assertTrue(tuple.contains((byte) 1));
    }

    @Test
    public void testByteTuple6_hashCode() {
        ByteTuple.ByteTuple6 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        ByteTuple.ByteTuple6 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testByteTuple6_equals() {
        ByteTuple.ByteTuple6 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        ByteTuple.ByteTuple6 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testByteTuple6_toString() {
        ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testByteTuple6_forEach() {
        ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(6, count.size());
    }

    // ============ ByteTuple7 Nested Class Tests ============

    @Test
    public void testByteTuple7_arity() {
        ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testByteTuple7_reverse() {
        ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        ByteTuple.ByteTuple7 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._7);
        assertEquals(tuple._7, reversed._1);
    }

    @Test
    public void testByteTuple7_contains() {
        ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        assertTrue(tuple.contains((byte) 1));
    }

    @Test
    public void testByteTuple7_hashCode() {
        ByteTuple.ByteTuple7 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        ByteTuple.ByteTuple7 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testByteTuple7_equals() {
        ByteTuple.ByteTuple7 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        ByteTuple.ByteTuple7 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testByteTuple7_toString() {
        ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testByteTuple7_forEach() {
        ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(7, count.size());
    }

    // ============ ByteTuple8 Nested Class Tests ============

    @Test
    public void testByteTuple8_arity() {
        ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testByteTuple8_reverse() {
        ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        ByteTuple.ByteTuple8 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._8);
        assertEquals(tuple._8, reversed._1);
    }

    @Test
    public void testByteTuple8_contains() {
        ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        assertTrue(tuple.contains((byte) 1));
    }

    @Test
    public void testByteTuple8_hashCode() {
        ByteTuple.ByteTuple8 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        ByteTuple.ByteTuple8 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testByteTuple8_equals() {
        ByteTuple.ByteTuple8 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        ByteTuple.ByteTuple8 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testByteTuple8_toString() {
        ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testByteTuple8_forEach() {
        ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(8, count.size());
    }

    // ============ ByteTuple9 Nested Class Tests ============

    @Test
    public void testByteTuple9_arity() {
        ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testByteTuple9_reverse() {
        ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        ByteTuple.ByteTuple9 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._9);
        assertEquals(tuple._9, reversed._1);
    }

    @Test
    public void testByteTuple9_contains() {
        ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        assertTrue(tuple.contains((byte) 1));
    }

    @Test
    public void testByteTuple9_hashCode() {
        ByteTuple.ByteTuple9 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        ByteTuple.ByteTuple9 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testByteTuple9_equals() {
        ByteTuple.ByteTuple9 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        ByteTuple.ByteTuple9 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testByteTuple9_toString() {
        ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testByteTuple9_forEach() {
        ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(9, count.size());
    }

}
