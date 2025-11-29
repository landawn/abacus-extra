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
import com.landawn.abacus.util.DoubleTuple.DoubleTuple0;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple1;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple3;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple4;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple5;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple6;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple7;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple8;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple9;
import com.landawn.abacus.util.u.Optional;

/**
 * Comprehensive unit tests for DoubleTuple and its inner classes (DoubleTuple0-9).
 * Tests cover all public methods including factory methods, statistical methods,
 * utility methods, functional methods, equality/hashCode, and stream operations.
 */
@Tag("2512")
public class DoubleTuple2512Test extends TestBase {

    private static final double DELTA = 0.0001;

    // ============ Factory Method Tests - DoubleTuple.of() ============

    @Test
    public void test_of_tuple1() {
        DoubleTuple1 tuple = DoubleTuple.of(1.5);
        assertNotNull(tuple);
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_of_tuple2() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertNotNull(tuple);
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_of_tuple3() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1, DELTA);
        assertEquals(2.0, tuple._2, DELTA);
        assertEquals(3.0, tuple._3, DELTA);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_of_tuple4() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1, DELTA);
        assertEquals(4.0, tuple._4, DELTA);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void test_of_tuple5() {
        DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1, DELTA);
        assertEquals(5.0, tuple._5, DELTA);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void test_of_tuple6() {
        DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1, DELTA);
        assertEquals(6.0, tuple._6, DELTA);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void test_of_tuple7() {
        DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1, DELTA);
        assertEquals(7.0, tuple._7, DELTA);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void test_of_tuple8() {
        DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1, DELTA);
        assertEquals(8.0, tuple._8, DELTA);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void test_of_tuple9() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertNotNull(tuple);
        assertEquals(1.0, tuple._1, DELTA);
        assertEquals(9.0, tuple._9, DELTA);
        assertEquals(9, tuple.arity());
    }

    // ============ Factory Method Tests - DoubleTuple.create() ============

    @Test
    public void test_create_nullArray() {
        DoubleTuple0 tuple = DoubleTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_emptyArray() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_arraySize1() {
        DoubleTuple1 tuple = DoubleTuple.create(new double[] { 1.5 });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
    }

    @Test
    public void test_create_arraySize2() {
        DoubleTuple2 tuple = DoubleTuple.create(new double[] { 1.5, 2.5 });
        assertNotNull(tuple);
        assertEquals(2, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
    }

    @Test
    public void test_create_arraySize9() {
        DoubleTuple9 tuple = DoubleTuple.create(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
        assertNotNull(tuple);
        assertEquals(9, tuple.arity());
        assertEquals(1.0, tuple._1, DELTA);
        assertEquals(9.0, tuple._9, DELTA);
    }

    @Test
    public void test_create_arrayTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> {
            DoubleTuple.create(new double[10]);
        });
    }

    // ============ Tuple0 Tests ============

    @Test
    public void test_tuple0_arity() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_tuple0_min_throwsException() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void test_tuple0_max_throwsException() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void test_tuple0_median_throwsException() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void test_tuple0_sum() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        assertEquals(0.0, tuple.sum(), DELTA);
    }

    @Test
    public void test_tuple0_average_throwsException() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void test_tuple0_reverse() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        DoubleTuple0 reversed = tuple.reverse();
        assertSame(tuple, reversed);
    }

    @Test
    public void test_tuple0_contains() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        assertFalse(tuple.contains(1.0));
    }

    @Test
    public void test_tuple0_toArray() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        double[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void test_tuple0_toList() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        DoubleList list = tuple.toList();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void test_tuple0_forEach() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        List<Double> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertTrue(collected.isEmpty());
    }

    @Test
    public void test_tuple0_stream() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        long count = tuple.stream().count();
        assertEquals(0, count);
    }

    @Test
    public void test_tuple0_toString() {
        DoubleTuple0 tuple = DoubleTuple.create(new double[0]);
        assertEquals("()", tuple.toString());
    }

    // ============ Tuple1 Tests ============

    @Test
    public void test_tuple1_arity() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_tuple1_min() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertEquals(3.14, tuple.min(), DELTA);
    }

    @Test
    public void test_tuple1_max() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertEquals(3.14, tuple.max(), DELTA);
    }

    @Test
    public void test_tuple1_median() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertEquals(3.14, tuple.median(), DELTA);
    }

    @Test
    public void test_tuple1_sum() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertEquals(3.14, tuple.sum(), DELTA);
    }

    @Test
    public void test_tuple1_average() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertEquals(3.14, tuple.average(), DELTA);
    }

    @Test
    public void test_tuple1_reverse() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        DoubleTuple1 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(3.14, reversed._1, DELTA);
        assertNotSame(tuple, reversed);
    }

    @Test
    public void test_tuple1_contains_found() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertTrue(tuple.contains(3.14));
    }

    @Test
    public void test_tuple1_contains_notFound() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertFalse(tuple.contains(2.71));
    }

    @Test
    public void test_tuple1_toArray() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        double[] array = tuple.toArray();
        assertArrayEquals(new double[] { 3.14 }, array, DELTA);
    }

    @Test
    public void test_tuple1_toList() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        DoubleList list = tuple.toList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(3.14, list.get(0), DELTA);
    }

    @Test
    public void test_tuple1_forEach() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        List<Double> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(1, collected.size());
        assertEquals(3.14, collected.get(0), DELTA);
    }

    @Test
    public void test_tuple1_stream() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        double sum = tuple.stream().sum();
        assertEquals(3.14, sum, DELTA);
    }

    @Test
    public void test_tuple1_hashCode() {
        DoubleTuple1 tuple1 = DoubleTuple.of(3.14);
        DoubleTuple1 tuple2 = DoubleTuple.of(3.14);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple1_equals_same() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertTrue(tuple.equals(tuple));
    }

    @Test
    public void test_tuple1_equals_equal() {
        DoubleTuple1 tuple1 = DoubleTuple.of(3.14);
        DoubleTuple1 tuple2 = DoubleTuple.of(3.14);
        assertTrue(tuple1.equals(tuple2));
        assertTrue(tuple2.equals(tuple1));
    }

    @Test
    public void test_tuple1_equals_notEqual() {
        DoubleTuple1 tuple1 = DoubleTuple.of(3.14);
        DoubleTuple1 tuple2 = DoubleTuple.of(2.71);
        assertFalse(tuple1.equals(tuple2));
    }

    @Test
    public void test_tuple1_equals_null() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertFalse(tuple.equals(null));
    }

    @Test
    public void test_tuple1_equals_differentClass() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertFalse(tuple.equals("not a tuple"));
    }

    @Test
    public void test_tuple1_toString() {
        DoubleTuple1 tuple = DoubleTuple.of(3.14);
        assertEquals("(3.14)", tuple.toString());
    }

    // ============ Tuple2 Tests ============

    @Test
    public void test_tuple2_arity() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_tuple2_min() {
        DoubleTuple2 tuple = DoubleTuple.of(2.5, 1.5);
        assertEquals(1.5, tuple.min(), DELTA);
    }

    @Test
    public void test_tuple2_max() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertEquals(2.5, tuple.max(), DELTA);
    }

    @Test
    public void test_tuple2_median() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertEquals(1.5, tuple.median(), DELTA);
    }

    @Test
    public void test_tuple2_sum() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertEquals(4.0, tuple.sum(), DELTA);
    }

    @Test
    public void test_tuple2_average() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertEquals(2.0, tuple.average(), DELTA);
    }

    @Test
    public void test_tuple2_reverse() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        DoubleTuple2 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(2.5, reversed._1, DELTA);
        assertEquals(1.5, reversed._2, DELTA);
    }

    @Test
    public void test_tuple2_contains_found() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertTrue(tuple.contains(1.5));
        assertTrue(tuple.contains(2.5));
    }

    @Test
    public void test_tuple2_contains_notFound() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertFalse(tuple.contains(3.5));
    }

    @Test
    public void test_tuple2_toArray() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        double[] array = tuple.toArray();
        assertArrayEquals(new double[] { 1.5, 2.5 }, array, DELTA);
    }

    @Test
    public void test_tuple2_toList() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        DoubleList list = tuple.toList();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void test_tuple2_forEach() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        List<Double> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(2, collected.size());
        assertEquals(1.5, collected.get(0), DELTA);
        assertEquals(2.5, collected.get(1), DELTA);
    }

    @Test
    public void test_tuple2_stream() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        long count = tuple.stream().count();
        assertEquals(2, count);
    }

    @Test
    public void test_tuple2_accept() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        List<Double> collected = new ArrayList<>();
        tuple.accept((a, b) -> {
            collected.add(a);
            collected.add(b);
        });
        assertEquals(2, collected.size());
        assertEquals(1.5, collected.get(0), DELTA);
        assertEquals(2.5, collected.get(1), DELTA);
    }

    @Test
    public void test_tuple2_map() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        double result = tuple.map((a, b) -> a + b);
        assertEquals(4.0, result, DELTA);
    }

    @Test
    public void test_tuple2_filter_match() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        Optional<DoubleTuple2> result = tuple.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_tuple2_filter_noMatch() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        Optional<DoubleTuple2> result = tuple.filter((a, b) -> a > b);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_tuple2_hashCode() {
        DoubleTuple2 tuple1 = DoubleTuple.of(1.5, 2.5);
        DoubleTuple2 tuple2 = DoubleTuple.of(1.5, 2.5);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple2_equals() {
        DoubleTuple2 tuple1 = DoubleTuple.of(1.5, 2.5);
        DoubleTuple2 tuple2 = DoubleTuple.of(1.5, 2.5);
        DoubleTuple2 tuple3 = DoubleTuple.of(2.5, 1.5);

        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
        assertFalse(tuple1.equals(null));
    }

    @Test
    public void test_tuple2_toString() {
        DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertEquals("(1.5, 2.5)", tuple.toString());
    }

    // ============ Tuple3 Tests ============

    @Test
    public void test_tuple3_arity() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_tuple3_min() {
        DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(1.0, tuple.min(), DELTA);
    }

    @Test
    public void test_tuple3_max() {
        DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(3.0, tuple.max(), DELTA);
    }

    @Test
    public void test_tuple3_median() {
        DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(2.0, tuple.median(), DELTA);
    }

    @Test
    public void test_tuple3_sum() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(6.0, tuple.sum(), DELTA);
    }

    @Test
    public void test_tuple3_average() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(2.0, tuple.average(), DELTA);
    }

    @Test
    public void test_tuple3_reverse() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(3.0, reversed._1, DELTA);
        assertEquals(2.0, reversed._2, DELTA);
        assertEquals(1.0, reversed._3, DELTA);
    }

    @Test
    public void test_tuple3_contains() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertTrue(tuple.contains(1.0));
        assertTrue(tuple.contains(2.0));
        assertTrue(tuple.contains(3.0));
        assertFalse(tuple.contains(4.0));
    }

    @Test
    public void test_tuple3_toArray() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double[] array = tuple.toArray();
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, array, DELTA);
    }

    @Test
    public void test_tuple3_accept() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        List<Double> collected = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            collected.add(a);
            collected.add(b);
            collected.add(c);
        });
        assertEquals(3, collected.size());
    }

    @Test
    public void test_tuple3_map() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double result = tuple.map((a, b, c) -> a + b + c);
        assertEquals(6.0, result, DELTA);
    }

    @Test
    public void test_tuple3_filter_match() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        Optional<DoubleTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
    }

    @Test
    public void test_tuple3_filter_noMatch() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        Optional<DoubleTuple3> result = tuple.filter((a, b, c) -> a > c);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_tuple3_hashCode() {
        DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple3_equals() {
        DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 4.0);

        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
    }

    @Test
    public void test_tuple3_toString() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals("(1.0, 2.0, 3.0)", tuple.toString());
    }

    // ============ Tuple4-9 Basic Tests ============

    @Test
    public void test_tuple4_basic() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(4, tuple.arity());
        assertEquals(1.0, tuple.min(), DELTA);
        assertEquals(4.0, tuple.max(), DELTA);
        assertEquals(10.0, tuple.sum(), DELTA);
        assertEquals(2.5, tuple.average(), DELTA);
        assertTrue(tuple.contains(3.0));
        DoubleTuple4 reversed = tuple.reverse();
        assertEquals(4.0, reversed._1, DELTA);
        assertEquals(1.0, reversed._4, DELTA);
    }

    @Test
    public void test_tuple5_basic() {
        DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertEquals(5, tuple.arity());
        assertEquals(1.0, tuple.min(), DELTA);
        assertEquals(5.0, tuple.max(), DELTA);
        assertEquals(3.0, tuple.median(), DELTA);
        assertEquals(15.0, tuple.sum(), DELTA);
        assertTrue(tuple.contains(3.0));
    }

    @Test
    public void test_tuple6_basic() {
        DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertEquals(6, tuple.arity());
        assertEquals(1.0, tuple.min(), DELTA);
        assertEquals(6.0, tuple.max(), DELTA);
        assertEquals(21.0, tuple.sum(), DELTA);
        assertEquals(3.5, tuple.average(), DELTA);
    }

    @Test
    public void test_tuple7_basic() {
        DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        assertEquals(7, tuple.arity());
        assertEquals(4.0, tuple.median(), DELTA);
        assertEquals(7, tuple.toArray().length);
    }

    @Test
    public void test_tuple8_basic() {
        DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        assertEquals(8, tuple.arity());
        assertEquals(8, tuple.toArray().length);
        assertEquals(36.0, tuple.sum(), DELTA);
    }

    @Test
    public void test_tuple9_basic() {
        DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        assertEquals(9, tuple.arity());
        assertEquals(9, tuple.toArray().length);
        assertEquals(45.0, tuple.sum(), DELTA);
        assertEquals(5.0, tuple.average(), DELTA);
    }

    // ============ Edge Cases and Additional Coverage ============

    @Test
    public void test_negativeValues() {
        DoubleTuple3 tuple = DoubleTuple.of(-1.0, 0.0, 1.0);
        assertEquals(-1.0, tuple.min(), DELTA);
        assertEquals(1.0, tuple.max(), DELTA);
        assertEquals(0.0, tuple.median(), DELTA);
        assertEquals(0.0, tuple.sum(), DELTA);
    }

    @Test
    public void test_extremeValues() {
        DoubleTuple2 tuple = DoubleTuple.of(Double.MIN_VALUE, Double.MAX_VALUE);
        assertEquals(Double.MIN_VALUE, tuple.min(), DELTA);
        assertEquals(Double.MAX_VALUE, tuple.max(), DELTA);
    }

    @Test
    public void test_specialValues_infinity() {
        DoubleTuple2 tuple = DoubleTuple.of(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, tuple.min(), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, tuple.max(), DELTA);
    }

    @Test
    public void test_specialValues_NaN() {
        DoubleTuple1 tuple = DoubleTuple.of(Double.NaN);
        assertTrue(Double.isNaN(tuple._1));
    }

    @Test
    public void test_verySmallDifferences() {
        DoubleTuple2 tuple = DoubleTuple.of(1.0000001, 1.0000002);
        assertEquals(1.0000001, tuple.min(), 0.00000001);
        assertEquals(1.0000002, tuple.max(), 0.00000001);
    }

    @Test
    public void test_toArray_independence() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double[] array = tuple.toArray();
        array[0] = 99.9;
        assertEquals(1.0, tuple._1, DELTA);   // Tuple should be unaffected
    }

    @Test
    public void test_toList_independence() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleList list = tuple.toList();
        list.set(0, 99.9);
        assertEquals(1.0, tuple._1, DELTA);   // Tuple should be unaffected
    }

    @Test
    public void test_stream_operations() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double sum = tuple.stream().sum();
        assertEquals(6.0, sum, DELTA);
    }

    @Test
    public void test_forEach_withException() {
        DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
        assertThrows(RuntimeException.class, () -> {
            tuple.forEach(d -> {
                if (d == 2.0)
                    throw new RuntimeException("test exception");
            });
        });
    }

    @Test
    public void test_equals_differentArity() {
        DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
        DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
        assertFalse(tuple1.equals(tuple2));
    }

    @Test
    public void test_hashCode_consistency() {
        DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void test_create_allSizes() {
        for (int i = 0; i <= 9; i++) {
            double[] array = new double[i];
            DoubleTuple<?> tuple = DoubleTuple.create(array);
            assertNotNull(tuple);
            assertEquals(i, tuple.arity());
        }
    }

    @Test
    public void test_median_evenCount() {
        DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(2.0, tuple.median(), DELTA);   // Lower middle value
    }

    @Test
    public void test_fractionalValues() {
        DoubleTuple3 tuple = DoubleTuple.of(0.1, 0.2, 0.3);
        assertEquals(0.6, tuple.sum(), DELTA);
        assertEquals(0.2, tuple.average(), DELTA);
    }

    @Test
    public void test_zero() {
        DoubleTuple3 tuple = DoubleTuple.of(0.0, 0.0, 0.0);
        assertEquals(0.0, tuple.min(), DELTA);
        assertEquals(0.0, tuple.max(), DELTA);
        assertEquals(0.0, tuple.median(), DELTA);
        assertEquals(0.0, tuple.sum(), DELTA);
        assertEquals(0.0, tuple.average(), DELTA);
    }
}
