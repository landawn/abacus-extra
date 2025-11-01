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
import com.landawn.abacus.util.FloatTuple.FloatTuple0;
import com.landawn.abacus.util.FloatTuple.FloatTuple1;
import com.landawn.abacus.util.FloatTuple.FloatTuple2;
import com.landawn.abacus.util.FloatTuple.FloatTuple3;
import com.landawn.abacus.util.FloatTuple.FloatTuple4;
import com.landawn.abacus.util.FloatTuple.FloatTuple5;
import com.landawn.abacus.util.FloatTuple.FloatTuple6;
import com.landawn.abacus.util.FloatTuple.FloatTuple7;
import com.landawn.abacus.util.FloatTuple.FloatTuple8;
import com.landawn.abacus.util.FloatTuple.FloatTuple9;
import com.landawn.abacus.util.stream.FloatStream;

/**
 * Comprehensive test suite for FloatTuple and its nested classes.
 * Tests all public methods including factory methods, statistical operations,
 * collection conversions, and special methods in Tuple2 and Tuple3.
 */
@Tag("2025")
public class FloatTuple2025Test extends TestBase {

    // Factory method tests
    @Test
    public void testOf1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf2() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(2.0f, tuple._2, 0.001f);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(2.0f, tuple._2, 0.001f);
        assertEquals(3.0f, tuple._3, 0.001f);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf4() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(2.0f, tuple._2, 0.001f);
        assertEquals(3.0f, tuple._3, 0.001f);
        assertEquals(4.0f, tuple._4, 0.001f);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf5() {
        FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(5.0f, tuple._5, 0.001f);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf6() {
        FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(6.0f, tuple._6, 0.001f);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf7() {
        FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(7.0f, tuple._7, 0.001f);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf8() {
        FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(8.0f, tuple._8, 0.001f);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf9() {
        FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(9.0f, tuple._9, 0.001f);
        assertEquals(9, tuple.arity());
    }

    // Create method tests
    @Test
    public void testCreateEmpty() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreateNull() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate1() {
        FloatTuple1 tuple = FloatTuple.create(new float[] { 1.0f });
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate3() {
        FloatTuple3 tuple = FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f });
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(2.0f, tuple._2, 0.001f);
        assertEquals(3.0f, tuple._3, 0.001f);
    }

    @Test
    public void testCreate9() {
        FloatTuple9 tuple = FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f });
        assertEquals(1.0f, tuple._1, 0.001f);
        assertEquals(9.0f, tuple._9, 0.001f);
    }

    @Test
    public void testCreateTooMany() {
        assertThrows(IllegalArgumentException.class, () -> {
            FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f });
        });
    }

    // Statistical method tests - min
    @Test
    public void testMinTuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertEquals(1.0f, tuple.min(), 0.001f);
    }

    @Test
    public void testMinTuple3() {
        FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
        assertEquals(1.0f, tuple.min(), 0.001f);
    }

    @Test
    public void testMinTuple0ThrowsException() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    // Statistical method tests - max
    @Test
    public void testMaxTuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertEquals(1.0f, tuple.max(), 0.001f);
    }

    @Test
    public void testMaxTuple3() {
        FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
        assertEquals(3.0f, tuple.max(), 0.001f);
    }

    @Test
    public void testMaxTuple0ThrowsException() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    // Statistical method tests - median
    @Test
    public void testMedianTuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertEquals(1.0f, tuple.median(), 0.001f);
    }

    @Test
    public void testMedianTuple3() {
        FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
        assertEquals(2.0f, tuple.median(), 0.001f);
    }

    @Test
    public void testMedianTuple0ThrowsException() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    // Statistical method tests - sum
    @Test
    public void testSumTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertEquals(0.0f, tuple.sum(), 0.001f);
    }

    @Test
    public void testSumTuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertEquals(1.0f, tuple.sum(), 0.001f);
    }

    @Test
    public void testSumTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(6.0f, tuple.sum(), 0.001f);
    }

    // Statistical method tests - average
    @Test
    public void testAverageTuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertEquals(1.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(2.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple0ThrowsException() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    // Reverse tests
    @Test
    public void testReverseTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        FloatTuple<FloatTuple0> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testReverseTuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        FloatTuple1 reversed = tuple.reverse();
        assertEquals(1.0f, reversed._1, 0.001f);
    }

    @Test
    public void testReverseTuple2() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        FloatTuple2 reversed = tuple.reverse();
        assertEquals(2.0f, reversed._1, 0.001f);
        assertEquals(1.0f, reversed._2, 0.001f);
    }

    @Test
    public void testReverseTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 reversed = tuple.reverse();
        assertEquals(3.0f, reversed._1, 0.001f);
        assertEquals(2.0f, reversed._2, 0.001f);
        assertEquals(1.0f, reversed._3, 0.001f);
    }

    // Contains tests
    @Test
    public void testContainsTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertFalse(tuple.contains(1.0f));
    }

    @Test
    public void testContainsTuple1True() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertTrue(tuple.contains(1.0f));
    }

    @Test
    public void testContainsTuple1False() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertFalse(tuple.contains(99.0f));
    }

    @Test
    public void testContainsTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertTrue(tuple.contains(1.0f));
        assertTrue(tuple.contains(2.0f));
        assertTrue(tuple.contains(3.0f));
        assertFalse(tuple.contains(99.0f));
    }

    // toArray tests
    @Test
    public void testToArrayTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        float[] array = tuple.toArray();
        assertEquals(0, array.length);
    }

    @Test
    public void testToArrayTuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        float[] array = tuple.toArray();
        assertArrayEquals(new float[] { 1.0f }, array, 0.001f);
    }

    @Test
    public void testToArrayTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float[] array = tuple.toArray();
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, array, 0.001f);
    }

    @Test
    public void testToArrayModificationDoesNotAffectTuple() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float[] array = tuple.toArray();
        array[0] = 999.0f;
        assertEquals(1.0f, tuple._1, 0.001f);
    }

    // toList tests
    @Test
    public void testToListTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        FloatList list = tuple.toList();
        assertEquals(0, list.size());
    }

    @Test
    public void testToListTuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        FloatList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals(1.0f, list.get(0), 0.001f);
    }

    @Test
    public void testToListTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(1.0f, list.get(0), 0.001f);
        assertEquals(2.0f, list.get(1), 0.001f);
        assertEquals(3.0f, list.get(2), 0.001f);
    }

    // forEach tests
    @Test
    public void testForEachTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        List<Float> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(0, result.size());
    }

    @Test
    public void testForEachTuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        List<Float> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(1, result.size());
        assertEquals(Float.valueOf(1.0f), result.get(0));
    }

    @Test
    public void testForEachTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        List<Float> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Float.valueOf(1.0f), result.get(0));
        assertEquals(Float.valueOf(2.0f), result.get(1));
        assertEquals(Float.valueOf(3.0f), result.get(2));
    }

    // stream tests
    @Test
    public void testStreamTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        FloatStream stream = tuple.stream();
        assertEquals(0, stream.count());
    }

    @Test
    public void testStreamTuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        FloatStream stream = tuple.stream();
        assertEquals(1.0f, stream.sum(), 0.001f);
    }

    @Test
    public void testStreamTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatStream stream = tuple.stream();
        assertEquals(6.0f, stream.sum(), 0.001f);
    }

    // hashCode tests
    @Test
    public void testHashCodeTuple1() {
        FloatTuple1 tuple1 = FloatTuple.of(1.0f);
        FloatTuple1 tuple2 = FloatTuple.of(1.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple2() {
        FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple3() {
        FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // equals tests
    @Test
    public void testEqualsSameObject() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEqualsNull() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertNotEquals(null, tuple);
    }

    @Test
    public void testEqualsDifferentClass() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertNotEquals("not a tuple", tuple);
    }

    @Test
    public void testEqualsTuple1() {
        FloatTuple1 tuple1 = FloatTuple.of(1.0f);
        FloatTuple1 tuple2 = FloatTuple.of(1.0f);
        FloatTuple1 tuple3 = FloatTuple.of(99.0f);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple2() {
        FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple2 tuple3 = FloatTuple.of(1.0f, 3.0f);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple3() {
        FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 tuple3 = FloatTuple.of(1.0f, 2.0f, 4.0f);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    // toString tests
    @Test
    public void testToStringTuple0() {
        FloatTuple<FloatTuple0> tuple = FloatTuple.create(new float[0]);
        assertEquals("()", tuple.toString());
    }

    @Test
    public void testToStringTuple1() {
        FloatTuple1 tuple = FloatTuple.of(1.0f);
        String str = tuple.toString();
        assertTrue(str.contains("1.0"));
    }

    @Test
    public void testToStringTuple3() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        String str = tuple.toString();
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("2.0"));
        assertTrue(str.contains("3.0"));
    }

    // Tuple2 special methods - accept
    @Test
    public void testTuple2Accept() {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        List<Float> result = new ArrayList<>();
        tuple.accept((a, b) -> {
            result.add(a);
            result.add(b);
        });
        assertEquals(2, result.size());
        assertEquals(Float.valueOf(3.0f), result.get(0));
        assertEquals(Float.valueOf(4.0f), result.get(1));
    }

    // Tuple2 special methods - map
    @Test
    public void testTuple2Map() {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        float result = tuple.map((a, b) -> a * b);
        assertEquals(12.0f, result, 0.001f);
    }

    // Tuple2 special methods - filter
    @Test
    public void testTuple2FilterTrue() {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        var result = tuple.filter((a, b) -> a + b > 5.0f);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2FilterFalse() {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        var result = tuple.filter((a, b) -> a + b > 10.0f);
        assertFalse(result.isPresent());
    }

    // Tuple3 special methods - accept
    @Test
    public void testTuple3Accept() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        List<Float> result = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            result.add(a);
            result.add(b);
            result.add(c);
        });
        assertEquals(3, result.size());
        assertEquals(Float.valueOf(1.0f), result.get(0));
        assertEquals(Float.valueOf(2.0f), result.get(1));
        assertEquals(Float.valueOf(3.0f), result.get(2));
    }

    // Tuple3 special methods - map
    @Test
    public void testTuple3Map() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6.0f, result, 0.001f);
    }

    // Tuple3 special methods - filter
    @Test
    public void testTuple3FilterTrue() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        var result = tuple.filter((a, b, c) -> a + b + c > 5.0f);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple3FilterFalse() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        var result = tuple.filter((a, b, c) -> a + b + c > 10.0f);
        assertFalse(result.isPresent());
    }

    // arity tests for all tuple sizes
    @Test
    public void testArity() {
        assertEquals(0, FloatTuple.create(new float[0]).arity());
        assertEquals(1, FloatTuple.of(1.0f).arity());
        assertEquals(2, FloatTuple.of(1.0f, 2.0f).arity());
        assertEquals(3, FloatTuple.of(1.0f, 2.0f, 3.0f).arity());
        assertEquals(4, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f).arity());
        assertEquals(5, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f).arity());
        assertEquals(6, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f).arity());
        assertEquals(7, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f).arity());
        assertEquals(8, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f).arity());
        assertEquals(9, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f).arity());
    }

    // Comprehensive tests for Tuple4 through Tuple9
    @Test
    public void testTuple4Operations() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);

        // Test reverse
        FloatTuple4 reversed = tuple.reverse();
        assertEquals(4.0f, reversed._1, 0.001f);
        assertEquals(3.0f, reversed._2, 0.001f);
        assertEquals(2.0f, reversed._3, 0.001f);
        assertEquals(1.0f, reversed._4, 0.001f);

        // Test contains
        assertTrue(tuple.contains(1.0f));
        assertTrue(tuple.contains(4.0f));
        assertFalse(tuple.contains(99.0f));

        // Test toArray
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, tuple.toArray(), 0.001f);

        // Test min/max/median/sum/average via base class
        assertEquals(1.0f, tuple.min(), 0.001f);
        assertEquals(4.0f, tuple.max(), 0.001f);
        assertEquals(2.0f, tuple.median(), 0.001f);
        assertEquals(10.0f, tuple.sum(), 0.001f);
        assertEquals(2.5, tuple.average(), 0.001);

        // Test hashCode and equals
        FloatTuple4 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        FloatTuple4 tuple3 = FloatTuple.of(1.0f, 2.0f, 3.0f, 5.0f);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        String str = tuple.toString();
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("4.0"));
    }

    @Test
    public void testTuple5Operations() {
        FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);

        // Test reverse
        FloatTuple5 reversed = tuple.reverse();
        assertEquals(5.0f, reversed._1, 0.001f);
        assertEquals(1.0f, reversed._5, 0.001f);

        // Test contains
        assertTrue(tuple.contains(1.0f));
        assertTrue(tuple.contains(5.0f));
        assertFalse(tuple.contains(99.0f));

        // Test toArray
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f }, tuple.toArray(), 0.001f);

        // Test statistical operations
        assertEquals(1.0f, tuple.min(), 0.001f);
        assertEquals(5.0f, tuple.max(), 0.001f);
        assertEquals(3.0f, tuple.median(), 0.001f);
        assertEquals(15.0f, tuple.sum(), 0.001f);
        assertEquals(3.0, tuple.average(), 0.001);

        // Test equals
        FloatTuple5 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertEquals(tuple, tuple2);
    }

    @Test
    public void testTuple6Operations() {
        FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);

        // Test reverse
        FloatTuple6 reversed = tuple.reverse();
        assertEquals(6.0f, reversed._1, 0.001f);
        assertEquals(1.0f, reversed._6, 0.001f);

        // Test contains
        assertTrue(tuple.contains(1.0f));
        assertTrue(tuple.contains(6.0f));
        assertFalse(tuple.contains(99.0f));

        // Test toArray
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f }, tuple.toArray(), 0.001f);

        // Test statistical operations
        assertEquals(1.0f, tuple.min(), 0.001f);
        assertEquals(6.0f, tuple.max(), 0.001f);
        assertEquals(21.0f, tuple.sum(), 0.001f);
        assertEquals(3.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple7Operations() {
        FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);

        // Test reverse
        FloatTuple7 reversed = tuple.reverse();
        assertEquals(7.0f, reversed._1, 0.001f);
        assertEquals(1.0f, reversed._7, 0.001f);

        // Test contains
        assertTrue(tuple.contains(1.0f));
        assertTrue(tuple.contains(7.0f));
        assertFalse(tuple.contains(99.0f));

        // Test toArray
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f }, tuple.toArray(), 0.001f);

        // Test statistical operations
        assertEquals(1.0f, tuple.min(), 0.001f);
        assertEquals(7.0f, tuple.max(), 0.001f);
        assertEquals(28.0f, tuple.sum(), 0.001f);
        assertEquals(4.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple8Operations() {
        FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);

        // Test reverse
        FloatTuple8 reversed = tuple.reverse();
        assertEquals(8.0f, reversed._1, 0.001f);
        assertEquals(1.0f, reversed._8, 0.001f);

        // Test contains
        assertTrue(tuple.contains(1.0f));
        assertTrue(tuple.contains(8.0f));
        assertFalse(tuple.contains(99.0f));

        // Test toArray
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f }, tuple.toArray(), 0.001f);

        // Test statistical operations
        assertEquals(1.0f, tuple.min(), 0.001f);
        assertEquals(8.0f, tuple.max(), 0.001f);
        assertEquals(36.0f, tuple.sum(), 0.001f);
        assertEquals(4.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple9Operations() {
        FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);

        // Test reverse
        FloatTuple9 reversed = tuple.reverse();
        assertEquals(9.0f, reversed._1, 0.001f);
        assertEquals(1.0f, reversed._9, 0.001f);

        // Test contains
        assertTrue(tuple.contains(1.0f));
        assertTrue(tuple.contains(9.0f));
        assertFalse(tuple.contains(99.0f));

        // Test toArray
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f }, tuple.toArray(), 0.001f);

        // Test statistical operations
        assertEquals(1.0f, tuple.min(), 0.001f);
        assertEquals(9.0f, tuple.max(), 0.001f);
        assertEquals(45.0f, tuple.sum(), 0.001f);
        assertEquals(5.0, tuple.average(), 0.001);
    }

    // Test create methods for sizes 2, 4-8
    @Test
    public void testCreate2Through8() {
        FloatTuple2 tuple2 = FloatTuple.create(new float[] { 1.0f, 2.0f });
        assertEquals(1.0f, tuple2._1, 0.001f);
        assertEquals(2.0f, tuple2._2, 0.001f);

        FloatTuple4 tuple4 = FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f, 4.0f });
        assertEquals(1.0f, tuple4._1, 0.001f);
        assertEquals(4.0f, tuple4._4, 0.001f);

        FloatTuple5 tuple5 = FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f });
        assertEquals(1.0f, tuple5._1, 0.001f);
        assertEquals(5.0f, tuple5._5, 0.001f);

        FloatTuple6 tuple6 = FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f });
        assertEquals(1.0f, tuple6._1, 0.001f);
        assertEquals(6.0f, tuple6._6, 0.001f);

        FloatTuple7 tuple7 = FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f });
        assertEquals(1.0f, tuple7._1, 0.001f);
        assertEquals(7.0f, tuple7._7, 0.001f);

        FloatTuple8 tuple8 = FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f });
        assertEquals(1.0f, tuple8._1, 0.001f);
        assertEquals(8.0f, tuple8._8, 0.001f);
    }

    // Test toList for larger tuples
    @Test
    public void testToListTuple4Through9() {
        FloatTuple4 tuple4 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        FloatList list4 = tuple4.toList();
        assertEquals(4, list4.size());
        assertEquals(4.0f, list4.get(3), 0.001f);

        FloatTuple9 tuple9 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        FloatList list9 = tuple9.toList();
        assertEquals(9, list9.size());
        assertEquals(9.0f, list9.get(8), 0.001f);
    }

    // Test forEach for larger tuples
    @Test
    public void testForEachTuple4() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        List<Float> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(4, result.size());
        assertEquals(Float.valueOf(4.0f), result.get(3));
    }

    // Test forEach override for Tuple2
    @Test
    public void testForEachTuple2Override() {
        FloatTuple2 tuple = FloatTuple.of(10.0f, 20.0f);
        List<Float> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(2, result.size());
        assertEquals(Float.valueOf(10.0f), result.get(0));
        assertEquals(Float.valueOf(20.0f), result.get(1));
    }

    // Test forEach override for Tuple3
    @Test
    public void testForEachTuple3Override() {
        FloatTuple3 tuple = FloatTuple.of(10.0f, 20.0f, 30.0f);
        List<Float> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Float.valueOf(30.0f), result.get(2));
    }

    // Test stream for larger tuples
    @Test
    public void testStreamTuple4Through9() {
        FloatTuple4 tuple4 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(10.0f, tuple4.stream().sum(), 0.001f);

        FloatTuple9 tuple9 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertEquals(45.0f, tuple9.stream().sum(), 0.001f);
    }

    // ==================== FloatTuple Nested Class Tests ====================

    // ============ FloatTuple1 Nested Class Tests ============

    @Test
    public void testFloatTuple1_arity() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testFloatTuple1_reverse() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
        FloatTuple.FloatTuple1 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._1);
        assertEquals(tuple._1, reversed._1);
    }

    @Test
    public void testFloatTuple1_contains() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertTrue(tuple.contains(1.0f));
    }

    @Test
    public void testFloatTuple1_hashCode() {
        FloatTuple.FloatTuple1 tuple1 = FloatTuple.of(1.0f);
        FloatTuple.FloatTuple1 tuple2 = FloatTuple.of(1.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testFloatTuple1_equals() {
        FloatTuple.FloatTuple1 tuple1 = FloatTuple.of(1.0f);
        FloatTuple.FloatTuple1 tuple2 = FloatTuple.of(1.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testFloatTuple1_toString() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testFloatTuple1_forEach() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testFloatTuple1_min() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertNotNull(tuple.min());
    }

    @Test
    public void testFloatTuple1_max() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertNotNull(tuple.max());
    }

    @Test
    public void testFloatTuple1_median() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertNotNull(tuple.median());
    }

    @Test
    public void testFloatTuple1_sum() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testFloatTuple1_average() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    // ============ FloatTuple2 Nested Class Tests ============

    @Test
    public void testFloatTuple2_arity() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testFloatTuple2_reverse() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        FloatTuple.FloatTuple2 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._2);
        assertEquals(tuple._2, reversed._1);
    }

    @Test
    public void testFloatTuple2_contains() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertTrue(tuple.contains(1.0f));
    }

    @Test
    public void testFloatTuple2_hashCode() {
        FloatTuple.FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple.FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testFloatTuple2_equals() {
        FloatTuple.FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple.FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testFloatTuple2_toString() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testFloatTuple2_forEach() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(2, count.size());
    }

    @Test
    public void testFloatTuple2_min() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertNotNull(tuple.min());
    }

    @Test
    public void testFloatTuple2_max() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertNotNull(tuple.max());
    }

    @Test
    public void testFloatTuple2_median() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertNotNull(tuple.median());
    }

    @Test
    public void testFloatTuple2_sum() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testFloatTuple2_average() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testFloatTuple2_accept_biConsumer() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testFloatTuple2_map_biFunction() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        String result = tuple.map((a, b) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testFloatTuple2_filter_biPredicate() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertTrue(tuple.filter((a, b) -> true).isPresent());
        assertFalse(tuple.filter((a, b) -> false).isPresent());
    }

    // ============ FloatTuple3 Nested Class Tests ============

    @Test
    public void testFloatTuple3_arity() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testFloatTuple3_reverse() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple.FloatTuple3 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._3);
        assertEquals(tuple._3, reversed._1);
    }

    @Test
    public void testFloatTuple3_contains() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertTrue(tuple.contains(1.0f));
    }

    @Test
    public void testFloatTuple3_hashCode() {
        FloatTuple.FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple.FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testFloatTuple3_equals() {
        FloatTuple.FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple.FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testFloatTuple3_toString() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testFloatTuple3_forEach() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(3, count.size());
    }

    @Test
    public void testFloatTuple3_min() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertNotNull(tuple.min());
    }

    @Test
    public void testFloatTuple3_max() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertNotNull(tuple.max());
    }

    @Test
    public void testFloatTuple3_median() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertNotNull(tuple.median());
    }

    @Test
    public void testFloatTuple3_sum() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertNotNull(tuple.sum());
    }

    @Test
    public void testFloatTuple3_average() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testFloatTuple3_accept_triConsumer() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b, c) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testFloatTuple3_map_triFunction() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        String result = tuple.map((a, b, c) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testFloatTuple3_filter_triPredicate() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertTrue(tuple.filter((a, b, c) -> true).isPresent());
        assertFalse(tuple.filter((a, b, c) -> false).isPresent());
    }

    // ============ FloatTuple4 Nested Class Tests ============

    @Test
    public void testFloatTuple4_arity() {
        FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testFloatTuple4_reverse() {
        FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        FloatTuple.FloatTuple4 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._4);
        assertEquals(tuple._4, reversed._1);
    }

    @Test
    public void testFloatTuple4_contains() {
        FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertTrue(tuple.contains(1.0f));
    }

    @Test
    public void testFloatTuple4_hashCode() {
        FloatTuple.FloatTuple4 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        FloatTuple.FloatTuple4 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testFloatTuple4_equals() {
        FloatTuple.FloatTuple4 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        FloatTuple.FloatTuple4 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testFloatTuple4_toString() {
        FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testFloatTuple4_forEach() {
        FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(4, count.size());
    }

    // ============ FloatTuple5 Nested Class Tests ============

    @Test
    public void testFloatTuple5_arity() {
        FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testFloatTuple5_reverse() {
        FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        FloatTuple.FloatTuple5 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._5);
        assertEquals(tuple._5, reversed._1);
    }

    @Test
    public void testFloatTuple5_contains() {
        FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertTrue(tuple.contains(1.0f));
    }

    @Test
    public void testFloatTuple5_hashCode() {
        FloatTuple.FloatTuple5 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        FloatTuple.FloatTuple5 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testFloatTuple5_equals() {
        FloatTuple.FloatTuple5 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        FloatTuple.FloatTuple5 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testFloatTuple5_toString() {
        FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testFloatTuple5_forEach() {
        FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(5, count.size());
    }

    // ============ FloatTuple6 Nested Class Tests ============

    @Test
    public void testFloatTuple6_arity() {
        FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testFloatTuple6_reverse() {
        FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        FloatTuple.FloatTuple6 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._6);
        assertEquals(tuple._6, reversed._1);
    }

    @Test
    public void testFloatTuple6_contains() {
        FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        assertTrue(tuple.contains(1.0f));
    }

    @Test
    public void testFloatTuple6_hashCode() {
        FloatTuple.FloatTuple6 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        FloatTuple.FloatTuple6 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testFloatTuple6_equals() {
        FloatTuple.FloatTuple6 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        FloatTuple.FloatTuple6 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testFloatTuple6_toString() {
        FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testFloatTuple6_forEach() {
        FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(6, count.size());
    }

    // ============ FloatTuple7 Nested Class Tests ============

    @Test
    public void testFloatTuple7_arity() {
        FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testFloatTuple7_reverse() {
        FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        FloatTuple.FloatTuple7 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._7);
        assertEquals(tuple._7, reversed._1);
    }

    @Test
    public void testFloatTuple7_contains() {
        FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        assertTrue(tuple.contains(1.0f));
    }

    @Test
    public void testFloatTuple7_hashCode() {
        FloatTuple.FloatTuple7 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        FloatTuple.FloatTuple7 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testFloatTuple7_equals() {
        FloatTuple.FloatTuple7 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        FloatTuple.FloatTuple7 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testFloatTuple7_toString() {
        FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testFloatTuple7_forEach() {
        FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(7, count.size());
    }

    // ============ FloatTuple8 Nested Class Tests ============

    @Test
    public void testFloatTuple8_arity() {
        FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testFloatTuple8_reverse() {
        FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        FloatTuple.FloatTuple8 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._8);
        assertEquals(tuple._8, reversed._1);
    }

    @Test
    public void testFloatTuple8_contains() {
        FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        assertTrue(tuple.contains(1.0f));
    }

    @Test
    public void testFloatTuple8_hashCode() {
        FloatTuple.FloatTuple8 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        FloatTuple.FloatTuple8 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testFloatTuple8_equals() {
        FloatTuple.FloatTuple8 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        FloatTuple.FloatTuple8 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testFloatTuple8_toString() {
        FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testFloatTuple8_forEach() {
        FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(8, count.size());
    }

    // ============ FloatTuple9 Nested Class Tests ============

    @Test
    public void testFloatTuple9_arity() {
        FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testFloatTuple9_reverse() {
        FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        FloatTuple.FloatTuple9 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._9);
        assertEquals(tuple._9, reversed._1);
    }

    @Test
    public void testFloatTuple9_contains() {
        FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertTrue(tuple.contains(1.0f));
    }

    @Test
    public void testFloatTuple9_hashCode() {
        FloatTuple.FloatTuple9 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        FloatTuple.FloatTuple9 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testFloatTuple9_equals() {
        FloatTuple.FloatTuple9 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        FloatTuple.FloatTuple9 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testFloatTuple9_toString() {
        FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testFloatTuple9_forEach() {
        FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(9, count.size());
    }

}
