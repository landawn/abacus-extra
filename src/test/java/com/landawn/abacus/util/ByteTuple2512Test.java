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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
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
import com.landawn.abacus.util.u.Optional;

/**
 * Comprehensive unit tests for ByteTuple and its inner classes (ByteTuple0-9).
 * Tests cover all public methods including factory methods, statistical methods,
 * utility methods, functional methods, equality/hashCode, and stream operations.
 */
@Tag("2512")
public class ByteTuple2512Test extends TestBase {

    // ============ Factory Method Tests - ByteTuple.of() ============

    @Test
    public void test_of_tuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_of_tuple2() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_of_tuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
        assertEquals((byte) 30, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_of_tuple4() {
        ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        assertNotNull(tuple);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 4, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void test_of_tuple5() {
        ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        assertNotNull(tuple);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 5, tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void test_of_tuple6() {
        ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        assertNotNull(tuple);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 6, tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void test_of_tuple7() {
        ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        assertNotNull(tuple);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 7, tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void test_of_tuple8() {
        ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        assertNotNull(tuple);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 8, tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void test_of_tuple9() {
        ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        assertNotNull(tuple);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 9, tuple._9);
        assertEquals(9, tuple.arity());
    }

    // ============ Factory Method Tests - ByteTuple.create() ============

    @Test
    public void test_create_nullArray() {
        ByteTuple0 tuple = ByteTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_emptyArray() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_arraySize1() {
        ByteTuple1 tuple = ByteTuple.create(new byte[] { 10 });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals((byte) 10, tuple._1);
    }

    @Test
    public void test_create_arraySize2() {
        ByteTuple2 tuple = ByteTuple.create(new byte[] { 10, 20 });
        assertNotNull(tuple);
        assertEquals(2, tuple.arity());
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
    }

    @Test
    public void test_create_arraySize9() {
        ByteTuple9 tuple = ByteTuple.create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        assertNotNull(tuple);
        assertEquals(9, tuple.arity());
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 9, tuple._9);
    }

    @Test
    public void test_create_arrayTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> {
            ByteTuple.create(new byte[10]);
        });
    }

    // ============ Tuple0 Tests ============

    @Test
    public void test_tuple0_arity() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_tuple0_min_throwsException() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void test_tuple0_max_throwsException() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void test_tuple0_median_throwsException() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void test_tuple0_sum() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void test_tuple0_average_throwsException() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void test_tuple0_reverse() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        ByteTuple0 reversed = tuple.reverse();
        assertSame(tuple, reversed);
    }

    @Test
    public void test_tuple0_contains() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertFalse(tuple.contains((byte) 10));
    }

    @Test
    public void test_tuple0_toArray() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        byte[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void test_tuple0_toList() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        ByteList list = tuple.toList();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void test_tuple0_forEach() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        List<Byte> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertTrue(collected.isEmpty());
    }

    @Test
    public void test_tuple0_stream() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        long count = tuple.stream().count();
        assertEquals(0, count);
    }

    @Test
    public void test_tuple0_toString() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertEquals("()", tuple.toString());
    }

    // ============ Tuple1 Tests ============

    @Test
    public void test_tuple1_arity() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_tuple1_min() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals((byte) 10, tuple.min());
    }

    @Test
    public void test_tuple1_max() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals((byte) 10, tuple.max());
    }

    @Test
    public void test_tuple1_median() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals((byte) 10, tuple.median());
    }

    @Test
    public void test_tuple1_sum() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals(10, tuple.sum());
    }

    @Test
    public void test_tuple1_average() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals(10.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_tuple1_reverse() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        ByteTuple1 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals((byte) 10, reversed._1);
        assertNotSame(tuple, reversed);
    }

    @Test
    public void test_tuple1_contains_found() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertTrue(tuple.contains((byte) 10));
    }

    @Test
    public void test_tuple1_contains_notFound() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertFalse(tuple.contains((byte) 20));
    }

    @Test
    public void test_tuple1_toArray() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        byte[] array = tuple.toArray();
        assertArrayEquals(new byte[] { 10 }, array);
    }

    @Test
    public void test_tuple1_toList() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        ByteList list = tuple.toList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals((byte) 10, list.get(0));
    }

    @Test
    public void test_tuple1_forEach() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        List<Byte> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(1, collected.size());
        assertEquals((byte) 10, collected.get(0));
    }

    @Test
    public void test_tuple1_stream() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        int sum = tuple.stream().sum();
        assertEquals(10, sum);
    }

    @Test
    public void test_tuple1_hashCode() {
        ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
        ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple1_equals_same() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertTrue(tuple.equals(tuple));
    }

    @Test
    public void test_tuple1_equals_equal() {
        ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
        ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
        assertTrue(tuple1.equals(tuple2));
        assertTrue(tuple2.equals(tuple1));
    }

    @Test
    public void test_tuple1_equals_notEqual() {
        ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
        ByteTuple1 tuple2 = ByteTuple.of((byte) 20);
        assertFalse(tuple1.equals(tuple2));
    }

    @Test
    public void test_tuple1_equals_null() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertFalse(tuple.equals(null));
    }

    @Test
    public void test_tuple1_equals_differentClass() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertFalse(tuple.equals("not a tuple"));
    }

    @Test
    public void test_tuple1_toString() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals("(10)", tuple.toString());
    }

    // ============ Tuple2 Tests ============

    @Test
    public void test_tuple2_arity() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_tuple2_min() {
        ByteTuple2 tuple = ByteTuple.of((byte) 20, (byte) 10);
        assertEquals((byte) 10, tuple.min());
    }

    @Test
    public void test_tuple2_max() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals((byte) 20, tuple.max());
    }

    @Test
    public void test_tuple2_median() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals((byte) 10, tuple.median());
    }

    @Test
    public void test_tuple2_sum() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(30, tuple.sum());
    }

    @Test
    public void test_tuple2_average() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(15.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_tuple2_reverse() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals((byte) 20, reversed._1);
        assertEquals((byte) 10, reversed._2);
    }

    @Test
    public void test_tuple2_contains_found() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertTrue(tuple.contains((byte) 10));
        assertTrue(tuple.contains((byte) 20));
    }

    @Test
    public void test_tuple2_contains_notFound() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertFalse(tuple.contains((byte) 30));
    }

    @Test
    public void test_tuple2_toArray() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        byte[] array = tuple.toArray();
        assertArrayEquals(new byte[] { 10, 20 }, array);
    }

    @Test
    public void test_tuple2_toList() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        ByteList list = tuple.toList();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void test_tuple2_forEach() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        List<Byte> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(2, collected.size());
        assertEquals((byte) 10, collected.get(0));
        assertEquals((byte) 20, collected.get(1));
    }

    @Test
    public void test_tuple2_stream() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        long count = tuple.stream().count();
        assertEquals(2, count);
    }

    @Test
    public void test_tuple2_accept() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        List<Byte> collected = new ArrayList<>();
        tuple.accept((a, b) -> {
            collected.add(a);
            collected.add(b);
        });
        assertEquals(2, collected.size());
        assertEquals((byte) 10, collected.get(0));
        assertEquals((byte) 20, collected.get(1));
    }

    @Test
    public void test_tuple2_map() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        int result = tuple.map((a, b) -> a + b);
        assertEquals(30, result);
    }

    @Test
    public void test_tuple2_filter_match() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_tuple2_filter_noMatch() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        Optional<ByteTuple2> result = tuple.filter((a, b) -> a > b);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_tuple2_hashCode() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple2_equals() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple3 = ByteTuple.of((byte) 20, (byte) 10);

        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
        assertFalse(tuple1.equals(null));
    }

    @Test
    public void test_tuple2_toString() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals("(10, 20)", tuple.toString());
    }

    // ============ Tuple3 Tests ============

    @Test
    public void test_tuple3_arity() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_tuple3_min() {
        ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals((byte) 10, tuple.min());
    }

    @Test
    public void test_tuple3_max() {
        ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals((byte) 30, tuple.max());
    }

    @Test
    public void test_tuple3_median() {
        ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals((byte) 20, tuple.median());
    }

    @Test
    public void test_tuple3_sum() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(60, tuple.sum());
    }

    @Test
    public void test_tuple3_average() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(20.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_tuple3_reverse() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple3 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals((byte) 30, reversed._1);
        assertEquals((byte) 20, reversed._2);
        assertEquals((byte) 10, reversed._3);
    }

    @Test
    public void test_tuple3_contains() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertTrue(tuple.contains((byte) 10));
        assertTrue(tuple.contains((byte) 20));
        assertTrue(tuple.contains((byte) 30));
        assertFalse(tuple.contains((byte) 40));
    }

    @Test
    public void test_tuple3_toArray() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        byte[] array = tuple.toArray();
        assertArrayEquals(new byte[] { 10, 20, 30 }, array);
    }

    @Test
    public void test_tuple3_accept() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        List<Byte> collected = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            collected.add(a);
            collected.add(b);
            collected.add(c);
        });
        assertEquals(3, collected.size());
    }

    @Test
    public void test_tuple3_map() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        int result = tuple.map((a, b, c) -> a + b + c);
        assertEquals(60, result);
    }

    @Test
    public void test_tuple3_filter_match() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
    }

    @Test
    public void test_tuple3_filter_noMatch() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a > c);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_tuple3_hashCode() {
        ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple3_equals() {
        ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 40);

        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
    }

    @Test
    public void test_tuple3_toString() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals("(10, 20, 30)", tuple.toString());
    }

    // ============ Tuple4-9 Basic Tests ============

    @Test
    public void test_tuple4_basic() {
        ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        assertEquals(4, tuple.arity());
        assertEquals((byte) 1, tuple.min());
        assertEquals((byte) 4, tuple.max());
        assertEquals(10, tuple.sum());
        assertEquals(2.5, tuple.average(), 0.0001);
        assertTrue(tuple.contains((byte) 3));
        ByteTuple4 reversed = tuple.reverse();
        assertEquals((byte) 4, reversed._1);
        assertEquals((byte) 1, reversed._4);
    }

    @Test
    public void test_tuple5_basic() {
        ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        assertEquals(5, tuple.arity());
        assertEquals((byte) 1, tuple.min());
        assertEquals((byte) 5, tuple.max());
        assertEquals((byte) 3, tuple.median());
        assertEquals(15, tuple.sum());
        assertTrue(tuple.contains((byte) 3));
    }

    @Test
    public void test_tuple6_basic() {
        ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        assertEquals(6, tuple.arity());
        assertEquals((byte) 1, tuple.min());
        assertEquals((byte) 6, tuple.max());
        assertEquals(21, tuple.sum());
        assertEquals(3.5, tuple.average(), 0.0001);
    }

    @Test
    public void test_tuple7_basic() {
        ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        assertEquals(7, tuple.arity());
        assertEquals((byte) 4, tuple.median());
        assertEquals(7, tuple.toArray().length);
    }

    @Test
    public void test_tuple8_basic() {
        ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        assertEquals(8, tuple.arity());
        assertEquals(8, tuple.toArray().length);
        assertEquals(36, tuple.sum());
    }

    @Test
    public void test_tuple9_basic() {
        ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        assertEquals(9, tuple.arity());
        assertEquals(9, tuple.toArray().length);
        assertEquals(45, tuple.sum());
        assertEquals(5.0, tuple.average(), 0.0001);
    }

    // ============ Edge Cases and Additional Coverage ============

    @Test
    public void test_negativeValues() {
        ByteTuple3 tuple = ByteTuple.of((byte) -10, (byte) 0, (byte) 10);
        assertEquals((byte) -10, tuple.min());
        assertEquals((byte) 10, tuple.max());
        assertEquals((byte) 0, tuple.median());
        assertEquals(0, tuple.sum());
    }

    @Test
    public void test_extremeValues() {
        ByteTuple2 tuple = ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE);
        assertEquals(Byte.MIN_VALUE, tuple.min());
        assertEquals(Byte.MAX_VALUE, tuple.max());
    }

    @Test
    public void test_toArray_independence() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        byte[] array = tuple.toArray();
        array[0] = 99;
        assertEquals((byte) 10, tuple._1); // Tuple should be unaffected
    }

    @Test
    public void test_toList_independence() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteList list = tuple.toList();
        list.set(0, (byte) 99);
        assertEquals((byte) 10, tuple._1); // Tuple should be unaffected
    }

    @Test
    public void test_stream_operations() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        int sum = tuple.stream().sum();
        assertEquals(60, sum);
    }

    @Test
    public void test_forEach_withException() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertThrows(RuntimeException.class, () -> {
            tuple.forEach(b -> {
                if (b == 20) throw new RuntimeException("test exception");
            });
        });
    }

    @Test
    public void test_equals_differentArity() {
        ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        assertFalse(tuple1.equals(tuple2));
    }

    @Test
    public void test_hashCode_consistency() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void test_create_allSizes() {
        for (int i = 0; i <= 9; i++) {
            byte[] array = new byte[i];
            ByteTuple<?> tuple = ByteTuple.create(array);
            assertNotNull(tuple);
            assertEquals(i, tuple.arity());
        }
    }

    @Test
    public void test_median_evenCount() {
        ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
        assertEquals((byte) 20, tuple.median()); // Lower middle value
    }
}
