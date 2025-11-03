/*
 * Copyright (C) 2024 HaiYang Li
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.IntTuple.*;
import com.landawn.abacus.util.u.Optional;

/**
 * Comprehensive unit tests for IntTuple and its nested tuple classes (IntTuple0 through IntTuple9).
 * Tests cover all public methods including factory methods, statistical operations, conversions, and edge cases.
 */
@Tag("2512")
public class IntTuple2512Test extends TestBase {

    // ===== Factory Method Tests =====

    @Test
    public void test_of_singleElement() {
        IntTuple1 tuple = IntTuple.of(42);
        assertNotNull(tuple);
        assertEquals(42, tuple._1);
    }

    @Test
    public void test_of_twoElements() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertNotNull(tuple);
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
    }

    @Test
    public void test_of_threeElements() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertNotNull(tuple);
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(3, tuple._3);
    }

    @Test
    public void test_of_fourElements() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertNotNull(tuple);
        assertEquals(1, tuple._1);
        assertEquals(4, tuple._4);
    }

    @Test
    public void test_of_fiveElements() {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        assertNotNull(tuple);
        assertEquals(5, tuple._5);
    }

    @Test
    public void test_of_sixElements() {
        IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertNotNull(tuple);
        assertEquals(6, tuple._6);
    }

    @Test
    public void test_of_sevenElements() {
        IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertNotNull(tuple);
        assertEquals(7, tuple._7);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_of_eightElements() {
        IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertNotNull(tuple);
        assertEquals(8, tuple._8);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_of_nineElements() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertNotNull(tuple);
        assertEquals(9, tuple._9);
    }

    @Test
    public void test_create_nullArray() {
        IntTuple0 tuple = IntTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_emptyArray() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_singleElementArray() {
        IntTuple1 tuple = IntTuple.create(new int[] { 42 });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals(42, tuple._1);
    }

    @Test
    public void test_create_multipleElementsArray() {
        IntTuple3 tuple = IntTuple.create(new int[] { 1, 2, 3 });
        assertNotNull(tuple);
        assertEquals(3, tuple.arity());
        assertEquals(1, tuple._1);
        assertEquals(2, tuple._2);
        assertEquals(3, tuple._3);
    }

    @Test
    public void test_create_tooManyElements() {
        int[] array = new int[10];
        assertThrows(IllegalArgumentException.class, () -> IntTuple.create(array));
    }

    // ===== IntTuple0 Tests =====

    @Test
    public void test_IntTuple0_arity() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_IntTuple0_min_throwsException() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void test_IntTuple0_max_throwsException() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void test_IntTuple0_median_throwsException() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void test_IntTuple0_sum() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void test_IntTuple0_average_throwsException() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void test_IntTuple0_reverse() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        IntTuple0 reversed = tuple.reverse();
        assertSame(tuple, reversed);
    }

    @Test
    public void test_IntTuple0_contains() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertFalse(tuple.contains(1));
    }

    @Test
    public void test_IntTuple0_toString() {
        IntTuple0 tuple = IntTuple.create(new int[0]);
        assertEquals("()", tuple.toString());
    }

    // ===== IntTuple1 Tests =====

    @Test
    public void test_IntTuple1_arity() {
        IntTuple1 tuple = IntTuple.of(5);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_IntTuple1_min() {
        IntTuple1 tuple = IntTuple.of(5);
        assertEquals(5, tuple.min());
    }

    @Test
    public void test_IntTuple1_max() {
        IntTuple1 tuple = IntTuple.of(5);
        assertEquals(5, tuple.max());
    }

    @Test
    public void test_IntTuple1_median() {
        IntTuple1 tuple = IntTuple.of(5);
        assertEquals(5, tuple.median());
    }

    @Test
    public void test_IntTuple1_sum() {
        IntTuple1 tuple = IntTuple.of(5);
        assertEquals(5, tuple.sum());
    }

    @Test
    public void test_IntTuple1_average() {
        IntTuple1 tuple = IntTuple.of(5);
        assertEquals(5.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_IntTuple1_reverse() {
        IntTuple1 tuple = IntTuple.of(5);
        IntTuple1 reversed = tuple.reverse();
        assertEquals(5, reversed._1);
    }

    @Test
    public void test_IntTuple1_contains_found() {
        IntTuple1 tuple = IntTuple.of(5);
        assertTrue(tuple.contains(5));
    }

    @Test
    public void test_IntTuple1_contains_notFound() {
        IntTuple1 tuple = IntTuple.of(5);
        assertFalse(tuple.contains(10));
    }

    @Test
    public void test_IntTuple1_hashCode() {
        IntTuple1 tuple1 = IntTuple.of(5);
        IntTuple1 tuple2 = IntTuple.of(5);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_IntTuple1_equals_same() {
        IntTuple1 tuple = IntTuple.of(5);
        assertEquals(tuple, tuple);
    }

    @Test
    public void test_IntTuple1_equals_equal() {
        IntTuple1 tuple1 = IntTuple.of(5);
        IntTuple1 tuple2 = IntTuple.of(5);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void test_IntTuple1_equals_notEqual() {
        IntTuple1 tuple1 = IntTuple.of(5);
        IntTuple1 tuple2 = IntTuple.of(10);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void test_IntTuple1_equals_null() {
        IntTuple1 tuple = IntTuple.of(5);
        assertNotEquals(null, tuple);
    }

    @Test
    public void test_IntTuple1_equals_differentType() {
        IntTuple1 tuple = IntTuple.of(5);
        assertNotEquals("string", tuple);
    }

    @Test
    public void test_IntTuple1_toString() {
        IntTuple1 tuple = IntTuple.of(5);
        assertEquals("(5)", tuple.toString());
    }

    // ===== IntTuple2 Tests =====

    @Test
    public void test_IntTuple2_arity() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_IntTuple2_min() {
        IntTuple2 tuple = IntTuple.of(3, 1);
        assertEquals(1, tuple.min());
    }

    @Test
    public void test_IntTuple2_max() {
        IntTuple2 tuple = IntTuple.of(3, 1);
        assertEquals(3, tuple.max());
    }

    @Test
    public void test_IntTuple2_median() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        int median = tuple.median();
        assertTrue(median == 1 || median == 2);
    }

    @Test
    public void test_IntTuple2_sum() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertEquals(3, tuple.sum());
    }

    @Test
    public void test_IntTuple2_average() {
        IntTuple2 tuple = IntTuple.of(1, 3);
        assertEquals(2.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_IntTuple2_reverse() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        IntTuple2 reversed = tuple.reverse();
        assertEquals(2, reversed._1);
        assertEquals(1, reversed._2);
    }

    @Test
    public void test_IntTuple2_contains_found() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertTrue(tuple.contains(1));
        assertTrue(tuple.contains(2));
    }

    @Test
    public void test_IntTuple2_contains_notFound() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertFalse(tuple.contains(3));
    }

    @Test
    public void test_IntTuple2_forEach() throws Exception {
        IntTuple2 tuple = IntTuple.of(1, 2);
        List<Integer> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(2, values.size());
        assertEquals(1, values.get(0).intValue());
        assertEquals(2, values.get(1).intValue());
    }

    @Test
    public void test_IntTuple2_accept() throws Exception {
        IntTuple2 tuple = IntTuple.of(3, 4);
        final int[] result = new int[1];
        tuple.accept((a, b) -> result[0] = a + b);
        assertEquals(7, result[0]);
    }

    @Test
    public void test_IntTuple2_map() throws Exception {
        IntTuple2 tuple = IntTuple.of(3, 4);
        Integer result = tuple.map((a, b) -> a * b);
        assertEquals(12, result.intValue());
    }

    @Test
    public void test_IntTuple2_filter_satisfied() throws Exception {
        IntTuple2 tuple = IntTuple.of(3, 4);
        Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_IntTuple2_filter_notSatisfied() throws Exception {
        IntTuple2 tuple = IntTuple.of(1, 1);
        Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_IntTuple2_hashCode() {
        IntTuple2 tuple1 = IntTuple.of(1, 2);
        IntTuple2 tuple2 = IntTuple.of(1, 2);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_IntTuple2_equals() {
        IntTuple2 tuple1 = IntTuple.of(1, 2);
        IntTuple2 tuple2 = IntTuple.of(1, 2);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void test_IntTuple2_toString() {
        IntTuple2 tuple = IntTuple.of(1, 2);
        assertEquals("(1, 2)", tuple.toString());
    }

    // ===== IntTuple3 Tests =====

    @Test
    public void test_IntTuple3_arity() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_IntTuple3_min() {
        IntTuple3 tuple = IntTuple.of(3, 1, 2);
        assertEquals(1, tuple.min());
    }

    @Test
    public void test_IntTuple3_max() {
        IntTuple3 tuple = IntTuple.of(3, 1, 2);
        assertEquals(3, tuple.max());
    }

    @Test
    public void test_IntTuple3_median() {
        IntTuple3 tuple = IntTuple.of(30, 10, 20);
        assertEquals(20, tuple.median());
    }

    @Test
    public void test_IntTuple3_sum() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(6, tuple.sum());
    }

    @Test
    public void test_IntTuple3_average() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(2.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_IntTuple3_reverse() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        IntTuple3 reversed = tuple.reverse();
        assertEquals(3, reversed._1);
        assertEquals(2, reversed._2);
        assertEquals(1, reversed._3);
    }

    @Test
    public void test_IntTuple3_contains() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertTrue(tuple.contains(2));
        assertFalse(tuple.contains(5));
    }

    @Test
    public void test_IntTuple3_forEach() throws Exception {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        List<Integer> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(3, values.size());
    }

    @Test
    public void test_IntTuple3_accept() throws Exception {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        final int[] result = new int[1];
        tuple.accept((a, b, c) -> result[0] = a + b + c);
        assertEquals(6, result[0]);
    }

    @Test
    public void test_IntTuple3_map() throws Exception {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        Integer result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6, result.intValue());
    }

    @Test
    public void test_IntTuple3_filter() throws Exception {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        Optional<IntTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());
    }

    // ===== IntTuple4+ Tests =====

    @Test
    public void test_IntTuple4_arity() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void test_IntTuple4_statisticalOperations() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        assertEquals(1, tuple.min());
        assertEquals(4, tuple.max());
        assertEquals(10, tuple.sum());
        assertEquals(2.5, tuple.average(), 0.0001);
    }

    @Test
    public void test_IntTuple5_arity() {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void test_IntTuple6_arity() {
        IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void test_IntTuple7_arity() {
        IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
        assertEquals(7, tuple.arity());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_IntTuple8_arity() {
        IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(8, tuple.arity());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_IntTuple9_arity() {
        IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(9, tuple.arity());
    }

    // ===== Common Method Tests =====

    @Test
    public void test_toArray() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int[] array = tuple.toArray();
        assertEquals(3, array.length);
        assertEquals(1, array[0]);
        assertEquals(2, array[1]);
        assertEquals(3, array[2]);
    }

    @Test
    public void test_toArray_modification() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int[] array = tuple.toArray();
        array[0] = 99;
        assertEquals(1, tuple._1);
    }

    @Test
    public void test_toList() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        IntList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    public void test_forEach_multipleElements() throws Exception {
        IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
        List<Integer> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(5, values.size());
    }

    @Test
    public void test_stream() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int sum = tuple.stream().sum();
        assertEquals(6, sum);
    }

    @Test
    public void test_hashCode_consistency() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void test_equals_reflexive() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        assertEquals(tuple, tuple);
    }

    @Test
    public void test_equals_symmetric() {
        IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
        IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
        assertEquals(tuple1, tuple2);
        assertEquals(tuple2, tuple1);
    }

    @Test
    public void test_equals_differentClass() {
        IntTuple2 tuple2 = IntTuple.of(1, 2);
        IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
        assertNotEquals(tuple2, tuple3);
    }

    @Test
    public void test_toString_format() {
        IntTuple3 tuple = IntTuple.of(1, 2, 3);
        String str = tuple.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
    }

    // ===== Edge Cases =====

    @Test
    public void test_negativeValues() {
        IntTuple3 tuple = IntTuple.of(-1, -2, -3);
        assertEquals(-3, tuple.min());
        assertEquals(-1, tuple.max());
        assertEquals(-6, tuple.sum());
    }

    @Test
    public void test_zeroValues() {
        IntTuple3 tuple = IntTuple.of(0, 0, 0);
        assertEquals(0, tuple.min());
        assertEquals(0, tuple.max());
        assertEquals(0, tuple.sum());
    }

    @Test
    public void test_largeValues() {
        IntTuple2 tuple = IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE / 2);
        assertEquals(Integer.MAX_VALUE / 2, tuple.min());
        assertEquals(Integer.MAX_VALUE, tuple.max());
    }

    @Test
    public void test_mixedValues() {
        IntTuple3 tuple = IntTuple.of(-100, 0, 100);
        assertEquals(-100, tuple.min());
        assertEquals(100, tuple.max());
        assertEquals(0, tuple.sum());
    }

    @Test
    public void test_reverse_largerTuples() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        IntTuple4 reversed = tuple.reverse();
        assertEquals(4, reversed._1);
        assertEquals(3, reversed._2);
        assertEquals(2, reversed._3);
        assertEquals(1, reversed._4);
    }

    @Test
    public void test_median_evenSize() {
        IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
        int median = tuple.median();
        assertTrue(median >= 1 && median <= 4);
    }

    @Test
    public void test_median_oddSize() {
        IntTuple5 tuple = IntTuple.of(50, 10, 30, 20, 40);
        int median = tuple.median();
        assertEquals(30, median);
    }
}
