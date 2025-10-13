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
import com.landawn.abacus.TestBase;

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
}
