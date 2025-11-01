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
 * Tests cover factory methods, accessor methods, statistical methods (min, max, median, sum, average),
 * utility methods, functional methods, equality/hashCode, and stream operations.
 */
@Tag("2511")
public class ByteTuple2511Test extends TestBase {

    // ============ Factory Method Tests - ByteTuple.of() ============

    @Test
    public void testOf_tuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf_tuple2() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf_tuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
        assertEquals((byte) 30, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf_tuple4() {
        ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
        assertEquals((byte) 30, tuple._3);
        assertEquals((byte) 40, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf_tuple5() {
        ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 50, tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf_tuple6() {
        ByteTuple6 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 60, tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf_tuple7() {
        ByteTuple7 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 70, tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf_tuple8() {
        ByteTuple8 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 80, tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf_tuple9() {
        ByteTuple9 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 90, tuple._9);
        assertEquals(9, tuple.arity());
    }

    // ============ Factory Method Tests - ByteTuple.create() ============

    @Test
    public void testCreate_nullArray() {
        ByteTuple<?> tuple = ByteTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_emptyArray() {
        ByteTuple<?> tuple = ByteTuple.create(new byte[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_singleElement() {
        ByteTuple1 tuple = ByteTuple.create(new byte[] { 10 });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals((byte) 10, tuple._1);
    }

    @Test
    public void testCreate_twoElements() {
        ByteTuple2 tuple = ByteTuple.create(new byte[] { 10, 20 });
        assertNotNull(tuple);
        assertEquals(2, tuple.arity());
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
    }

    @Test
    public void testCreate_nineElements() {
        ByteTuple9 tuple = ByteTuple.create(new byte[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 });
        assertNotNull(tuple);
        assertEquals(9, tuple.arity());
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 90, tuple._9);
    }

    @Test
    public void testCreate_tooManyElements() {
        assertThrows(IllegalArgumentException.class, () -> ByteTuple.create(new byte[10]));
    }

    // ============ Accessor Tests - Direct Field Access ============

    @Test
    public void testTuple1_directFieldAccess() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals((byte) 10, tuple._1);
    }

    @Test
    public void testTuple2_directFieldAccess() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
    }

    @Test
    public void testTuple3_directFieldAccess() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
        assertEquals((byte) 30, tuple._3);
    }

    // ============ Min Tests ============

    @Test
    public void testTuple0_min_throwsException() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void testTuple1_min() {
        ByteTuple1 tuple = ByteTuple.of((byte) 42);
        assertEquals((byte) 42, tuple.min());
    }

    @Test
    public void testTuple2_min() {
        ByteTuple2 tuple = ByteTuple.of((byte) 30, (byte) 10);
        assertEquals((byte) 10, tuple.min());
    }

    @Test
    public void testTuple3_min() {
        ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals((byte) 10, tuple.min());
    }

    @Test
    public void testTuple5_min() {
        ByteTuple5 tuple = ByteTuple.of((byte) 50, (byte) 30, (byte) 10, (byte) 40, (byte) 20);
        assertEquals((byte) 10, tuple.min());
    }

    // ============ Max Tests ============

    @Test
    public void testTuple0_max_throwsException() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void testTuple1_max() {
        ByteTuple1 tuple = ByteTuple.of((byte) 42);
        assertEquals((byte) 42, tuple.max());
    }

    @Test
    public void testTuple2_max() {
        ByteTuple2 tuple = ByteTuple.of((byte) 30, (byte) 10);
        assertEquals((byte) 30, tuple.max());
    }

    @Test
    public void testTuple3_max() {
        ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals((byte) 30, tuple.max());
    }

    @Test
    public void testTuple5_max() {
        ByteTuple5 tuple = ByteTuple.of((byte) 50, (byte) 30, (byte) 10, (byte) 40, (byte) 20);
        assertEquals((byte) 50, tuple.max());
    }

    // ============ Median Tests ============

    @Test
    public void testTuple0_median_throwsException() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void testTuple1_median() {
        ByteTuple1 tuple = ByteTuple.of((byte) 42);
        assertEquals((byte) 42, tuple.median());
    }

    @Test
    public void testTuple2_median() {
        ByteTuple2 tuple = ByteTuple.of((byte) 30, (byte) 10);
        assertEquals((byte) 10, tuple.median()); // lower value for even-sized tuple
    }

    @Test
    public void testTuple3_median() {
        ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals((byte) 20, tuple.median()); // middle value
    }

    @Test
    public void testTuple4_median() {
        ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
        assertEquals((byte) 20, tuple.median()); // lower of two middle values
    }

    // ============ Sum Tests ============

    @Test
    public void testTuple0_sum() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void testTuple1_sum() {
        ByteTuple1 tuple = ByteTuple.of((byte) 42);
        assertEquals(42, tuple.sum());
    }

    @Test
    public void testTuple2_sum() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(30, tuple.sum());
    }

    @Test
    public void testTuple3_sum() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(60, tuple.sum());
    }

    @Test
    public void testTuple5_sum() {
        ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
        assertEquals(150, tuple.sum());
    }

    // ============ Average Tests ============

    @Test
    public void testTuple0_average_throwsException() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void testTuple1_average() {
        ByteTuple1 tuple = ByteTuple.of((byte) 42);
        assertEquals(42.0, tuple.average());
    }

    @Test
    public void testTuple2_average() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(15.0, tuple.average());
    }

    @Test
    public void testTuple3_average() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(20.0, tuple.average());
    }

    @Test
    public void testTuple5_average() {
        ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
        assertEquals(30.0, tuple.average());
    }

    // ============ Reverse Tests ============

    @Test
    public void testTuple0_reverse() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        ByteTuple0 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(tuple, reversed);
    }

    @Test
    public void testTuple1_reverse() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        ByteTuple1 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertNotSame(tuple, reversed);
        assertEquals((byte) 10, reversed._1);
    }

    @Test
    public void testTuple2_reverse() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertNotSame(tuple, reversed);
        assertEquals((byte) 20, reversed._1);
        assertEquals((byte) 10, reversed._2);
    }

    @Test
    public void testTuple3_reverse() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple3 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertNotSame(tuple, reversed);
        assertEquals((byte) 30, reversed._1);
        assertEquals((byte) 20, reversed._2);
        assertEquals((byte) 10, reversed._3);
    }

    @Test
    public void testTuple5_reverse() {
        ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
        ByteTuple5 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals((byte) 50, reversed._1);
        assertEquals((byte) 40, reversed._2);
        assertEquals((byte) 30, reversed._3);
        assertEquals((byte) 20, reversed._4);
        assertEquals((byte) 10, reversed._5);
    }

    // ============ Contains Tests ============

    @Test
    public void testTuple0_contains() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertFalse(tuple.contains((byte) 10));
        assertFalse(tuple.contains((byte) 20));
    }

    @Test
    public void testTuple1_contains() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertTrue(tuple.contains((byte) 10));
        assertFalse(tuple.contains((byte) 20));
    }

    @Test
    public void testTuple2_contains() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertTrue(tuple.contains((byte) 10));
        assertTrue(tuple.contains((byte) 20));
        assertFalse(tuple.contains((byte) 30));
    }

    @Test
    public void testTuple3_contains() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertTrue(tuple.contains((byte) 10));
        assertTrue(tuple.contains((byte) 20));
        assertTrue(tuple.contains((byte) 30));
        assertFalse(tuple.contains((byte) 40));
    }

    @Test
    public void testTuple5_contains() {
        ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
        assertTrue(tuple.contains((byte) 10));
        assertTrue(tuple.contains((byte) 50));
        assertFalse(tuple.contains((byte) 60));
    }

    // ============ toArray Tests ============

    @Test
    public void testTuple0_toArray() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        byte[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void testTuple1_toArray() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        byte[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(1, array.length);
        assertEquals((byte) 10, array[0]);
    }

    @Test
    public void testTuple2_toArray() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        byte[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(2, array.length);
        assertArrayEquals(new byte[] { 10, 20 }, array);
    }

    @Test
    public void testTuple3_toArray() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        byte[] array = tuple.toArray();
        assertNotNull(array);
        assertArrayEquals(new byte[] { 10, 20, 30 }, array);
    }

    @Test
    public void testToArray_immutability() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        byte[] array1 = tuple.toArray();
        byte[] array2 = tuple.toArray();
        assertNotSame(array1, array2);
    }

    // ============ toList Tests ============

    @Test
    public void testTuple0_toList() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        ByteList list = tuple.toList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testTuple1_toList() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        ByteList list = tuple.toList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals((byte) 10, list.get(0));
    }

    @Test
    public void testTuple2_toList() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        ByteList list = tuple.toList();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals((byte) 10, list.get(0));
        assertEquals((byte) 20, list.get(1));
    }

    @Test
    public void testTuple3_toList() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteList list = tuple.toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals((byte) 10, list.get(0));
        assertEquals((byte) 20, list.get(1));
        assertEquals((byte) 30, list.get(2));
    }

    // ============ forEach Tests ============

    @Test
    public void testTuple0_forEach() throws Exception {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        List<Byte> results = new ArrayList<>();
        tuple.forEach(b -> results.add(b));
        assertEquals(0, results.size());
    }

    @Test
    public void testTuple1_forEach() throws Exception {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        List<Byte> results = new ArrayList<>();
        tuple.forEach(b -> results.add(b));
        assertEquals(1, results.size());
        assertEquals((byte) 10, results.get(0).byteValue());
    }

    @Test
    public void testTuple2_forEach() throws Exception {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        List<Byte> results = new ArrayList<>();
        tuple.forEach(b -> results.add(b));
        assertEquals(2, results.size());
        assertEquals((byte) 10, results.get(0).byteValue());
        assertEquals((byte) 20, results.get(1).byteValue());
    }

    @Test
    public void testTuple3_forEach() throws Exception {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        List<Byte> results = new ArrayList<>();
        tuple.forEach(b -> results.add(b));
        assertEquals(3, results.size());
        assertEquals((byte) 10, results.get(0).byteValue());
        assertEquals((byte) 20, results.get(1).byteValue());
        assertEquals((byte) 30, results.get(2).byteValue());
    }

    // ============ Stream Tests ============

    @Test
    public void testTuple1_stream() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        long count = tuple.stream().count();
        assertEquals(1, count);
    }

    @Test
    public void testTuple2_stream_filter() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        long count = tuple.stream().filter(b -> b > 15).count();
        assertEquals(1, count);
    }

    @Test
    public void testTuple3_stream_sum() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        int sum = tuple.stream().sum();
        assertEquals(60, sum);
    }

    // ============ ByteTuple2.accept Tests ============

    @Test
    public void testTuple2_accept() throws Exception {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        List<Byte> results = new ArrayList<>();
        tuple.accept((a, b) -> {
            results.add(a);
            results.add(b);
        });
        assertEquals(2, results.size());
        assertEquals((byte) 10, results.get(0).byteValue());
        assertEquals((byte) 20, results.get(1).byteValue());
    }

    // ============ ByteTuple2.map Tests ============

    @Test
    public void testTuple2_map() throws Exception {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        int result = tuple.map((a, b) -> a + b);
        assertEquals(30, result);
    }

    @Test
    public void testTuple2_map_multiply() throws Exception {
        ByteTuple2 tuple = ByteTuple.of((byte) 5, (byte) 6);
        int result = tuple.map((a, b) -> a * b);
        assertEquals(30, result);
    }

    // ============ ByteTuple2.filter Tests ============

    @Test
    public void testTuple2_filter_pass() throws Exception {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2_filter_fail() throws Exception {
        ByteTuple2 tuple = ByteTuple.of((byte) 20, (byte) 10);
        Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
        assertFalse(result.isPresent());
    }

    // ============ ByteTuple3.accept Tests ============

    @Test
    public void testTuple3_accept() throws Exception {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        List<Byte> results = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            results.add(a);
            results.add(b);
            results.add(c);
        });
        assertEquals(3, results.size());
        assertEquals((byte) 10, results.get(0).byteValue());
        assertEquals((byte) 20, results.get(1).byteValue());
        assertEquals((byte) 30, results.get(2).byteValue());
    }

    // ============ ByteTuple3.map Tests ============

    @Test
    public void testTuple3_map() throws Exception {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        int result = tuple.map((a, b, c) -> a + b + c);
        assertEquals(60, result);
    }

    @Test
    public void testTuple3_map_max() throws Exception {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 30, (byte) 20);
        byte result = tuple.map((a, b, c) -> (byte) Math.max(a, Math.max(b, c)));
        assertEquals((byte) 30, result);
    }

    // ============ ByteTuple3.filter Tests ============

    @Test
    public void testTuple3_filter_pass() throws Exception {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple3_filter_fail() throws Exception {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a > b);
        assertFalse(result.isPresent());
    }

    // ============ hashCode Tests ============

    @Test
    public void testTuple0_hashCode() {
        ByteTuple0 tuple1 = ByteTuple.create(new byte[0]);
        ByteTuple0 tuple2 = ByteTuple.create(new byte[0]);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testTuple1_hashCode() {
        ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
        ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertEquals((byte) 10, tuple1.hashCode());
    }

    @Test
    public void testTuple2_hashCode() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testTuple3_hashCode() {
        ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testTuple2_hashCode_different() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 20, (byte) 10);
        assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // ============ equals Tests ============

    @Test
    public void testTuple0_equals() {
        ByteTuple0 tuple1 = ByteTuple.create(new byte[0]);
        ByteTuple0 tuple2 = ByteTuple.create(new byte[0]);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple1_equals_same() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testTuple1_equals() {
        ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
        ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple1_notEquals() {
        ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
        ByteTuple1 tuple2 = ByteTuple.of((byte) 20);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple2_equals() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple2_notEquals_different_values() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 20, (byte) 10);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple3_equals() {
        ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple_notEquals_null() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertNotEquals(tuple, null);
        assertFalse(tuple.equals(null));
    }

    @Test
    public void testTuple_notEquals_differentType() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertNotEquals(tuple, "10");
        assertNotEquals(tuple, 10);
    }

    @Test
    public void testTuple2_notEquals_differentTuple() {
        ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertNotEquals(tuple2, tuple3);
    }

    // ============ toString Tests ============

    @Test
    public void testTuple0_toString() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertEquals("()", tuple.toString());
    }

    @Test
    public void testTuple1_toString() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals("(10)", tuple.toString());
    }

    @Test
    public void testTuple2_toString() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals("(10, 20)", tuple.toString());
    }

    @Test
    public void testTuple3_toString() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals("(10, 20, 30)", tuple.toString());
    }

    // ============ Arity Tests ============

    @Test
    public void testTuple0_arity() {
        ByteTuple0 tuple = ByteTuple.create(new byte[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testTuple1_arity() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testTuple2_arity() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testTuple3_arity() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testTuple4_arity() {
        ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testTuple5_arity() {
        ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testTuple6_arity() {
        ByteTuple6 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testTuple7_arity() {
        ByteTuple7 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testTuple8_arity() {
        ByteTuple8 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testTuple9_arity() {
        ByteTuple9 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
        assertEquals(9, tuple.arity());
    }

    // ============ Edge Cases and Special Tests ============

    @Test
    public void testTuple_immutability() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        // The tuple is immutable, so we can't change _1, but we verify it stays the same
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 10, tuple._1);
    }

    @Test
    public void testMultipleTuples_independence() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 20, (byte) 10);
        assertNotEquals(tuple1, tuple2);
        assertEquals((byte) 10, tuple1._1);
        assertEquals((byte) 20, tuple2._1);
    }

    @Test
    public void testTuple_negativeValues() {
        ByteTuple3 tuple = ByteTuple.of((byte) -10, (byte) 20, (byte) -30);
        assertEquals((byte) -30, tuple.min());
        assertEquals((byte) 20, tuple.max());
        assertEquals(-20, tuple.sum());
    }

    @Test
    public void testTuple_zeroValues() {
        ByteTuple3 tuple = ByteTuple.of((byte) 0, (byte) 0, (byte) 0);
        assertTrue(tuple.contains((byte) 0));
        assertEquals(0, tuple.sum());
        assertEquals(0.0, tuple.average());
    }

    @Test
    public void testTuple_mixedSignValues() {
        ByteTuple4 tuple = ByteTuple.of((byte) -5, (byte) 10, (byte) -3, (byte) 8);
        assertEquals((byte) -5, tuple.min());
        assertEquals((byte) 10, tuple.max());
        assertEquals(10, tuple.sum());
    }

}
