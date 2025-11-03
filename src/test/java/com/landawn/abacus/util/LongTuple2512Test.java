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
import com.landawn.abacus.util.LongTuple.*;
import com.landawn.abacus.util.u.Optional;

/**
 * Comprehensive unit tests for LongTuple and its nested tuple classes (LongTuple0 through LongTuple9).
 * Tests cover all public methods including factory methods, statistical operations, conversions, and edge cases.
 */
@Tag("2512")
public class LongTuple2512Test extends TestBase {

    // ===== Factory Method Tests =====

    @Test
    public void test_of_singleElement() {
        LongTuple1 tuple = LongTuple.of(42L);
        assertNotNull(tuple);
        assertEquals(42L, tuple._1);
    }

    @Test
    public void test_of_twoElements() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertNotNull(tuple);
        assertEquals(1L, tuple._1);
        assertEquals(2L, tuple._2);
    }

    @Test
    public void test_of_threeElements() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertNotNull(tuple);
        assertEquals(1L, tuple._1);
        assertEquals(2L, tuple._2);
        assertEquals(3L, tuple._3);
    }

    @Test
    public void test_of_fourElements() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        assertNotNull(tuple);
        assertEquals(1L, tuple._1);
        assertEquals(4L, tuple._4);
    }

    @Test
    public void test_of_fiveElements() {
        LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertNotNull(tuple);
        assertEquals(5L, tuple._5);
    }

    @Test
    public void test_of_sixElements() {
        LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        assertNotNull(tuple);
        assertEquals(6L, tuple._6);
    }

    @Test
    public void test_of_sevenElements() {
        LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertNotNull(tuple);
        assertEquals(7L, tuple._7);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_of_eightElements() {
        LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertNotNull(tuple);
        assertEquals(8L, tuple._8);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_of_nineElements() {
        LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertNotNull(tuple);
        assertEquals(9L, tuple._9);
    }

    @Test
    public void test_create_nullArray() {
        LongTuple0 tuple = LongTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_emptyArray() {
        LongTuple0 tuple = LongTuple.create(new long[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_singleElementArray() {
        LongTuple1 tuple = LongTuple.create(new long[] { 42L });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals(42L, tuple._1);
    }

    @Test
    public void test_create_multipleElementsArray() {
        LongTuple3 tuple = LongTuple.create(new long[] { 1L, 2L, 3L });
        assertNotNull(tuple);
        assertEquals(3, tuple.arity());
        assertEquals(1L, tuple._1);
        assertEquals(2L, tuple._2);
        assertEquals(3L, tuple._3);
    }

    @Test
    public void test_create_tooManyElements() {
        long[] array = new long[10];
        assertThrows(IllegalArgumentException.class, () -> LongTuple.create(array));
    }

    // ===== LongTuple0 Tests =====

    @Test
    public void test_LongTuple0_arity() {
        LongTuple0 tuple = LongTuple.create(new long[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_LongTuple0_min_throwsException() {
        LongTuple0 tuple = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void test_LongTuple0_max_throwsException() {
        LongTuple0 tuple = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void test_LongTuple0_median_throwsException() {
        LongTuple0 tuple = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void test_LongTuple0_sum() {
        LongTuple0 tuple = LongTuple.create(new long[0]);
        assertEquals(0L, tuple.sum());
    }

    @Test
    public void test_LongTuple0_average_throwsException() {
        LongTuple0 tuple = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void test_LongTuple0_reverse() {
        LongTuple0 tuple = LongTuple.create(new long[0]);
        LongTuple0 reversed = tuple.reverse();
        assertSame(tuple, reversed);
    }

    @Test
    public void test_LongTuple0_contains() {
        LongTuple0 tuple = LongTuple.create(new long[0]);
        assertFalse(tuple.contains(1L));
    }

    @Test
    public void test_LongTuple0_toString() {
        LongTuple0 tuple = LongTuple.create(new long[0]);
        assertEquals("()", tuple.toString());
    }

    // ===== LongTuple1 Tests =====

    @Test
    public void test_LongTuple1_arity() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_LongTuple1_min() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertEquals(5L, tuple.min());
    }

    @Test
    public void test_LongTuple1_max() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertEquals(5L, tuple.max());
    }

    @Test
    public void test_LongTuple1_median() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertEquals(5L, tuple.median());
    }

    @Test
    public void test_LongTuple1_sum() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertEquals(5L, tuple.sum());
    }

    @Test
    public void test_LongTuple1_average() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertEquals(5.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_LongTuple1_reverse() {
        LongTuple1 tuple = LongTuple.of(5L);
        LongTuple1 reversed = tuple.reverse();
        assertEquals(5L, reversed._1);
    }

    @Test
    public void test_LongTuple1_contains_found() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertTrue(tuple.contains(5L));
    }

    @Test
    public void test_LongTuple1_contains_notFound() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertFalse(tuple.contains(10L));
    }

    @Test
    public void test_LongTuple1_hashCode() {
        LongTuple1 tuple1 = LongTuple.of(5L);
        LongTuple1 tuple2 = LongTuple.of(5L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_LongTuple1_equals_same() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertEquals(tuple, tuple);
    }

    @Test
    public void test_LongTuple1_equals_equal() {
        LongTuple1 tuple1 = LongTuple.of(5L);
        LongTuple1 tuple2 = LongTuple.of(5L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void test_LongTuple1_equals_notEqual() {
        LongTuple1 tuple1 = LongTuple.of(5L);
        LongTuple1 tuple2 = LongTuple.of(10L);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void test_LongTuple1_equals_null() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertNotEquals(null, tuple);
    }

    @Test
    public void test_LongTuple1_equals_differentType() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertNotEquals("string", tuple);
    }

    @Test
    public void test_LongTuple1_toString() {
        LongTuple1 tuple = LongTuple.of(5L);
        assertEquals("(5)", tuple.toString());
    }

    // ===== LongTuple2 Tests =====

    @Test
    public void test_LongTuple2_arity() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_LongTuple2_min() {
        LongTuple2 tuple = LongTuple.of(3L, 1L);
        assertEquals(1L, tuple.min());
    }

    @Test
    public void test_LongTuple2_max() {
        LongTuple2 tuple = LongTuple.of(3L, 1L);
        assertEquals(3L, tuple.max());
    }

    @Test
    public void test_LongTuple2_median() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        long median = tuple.median();
        assertTrue(median == 1L || median == 2L);
    }

    @Test
    public void test_LongTuple2_sum() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertEquals(3L, tuple.sum());
    }

    @Test
    public void test_LongTuple2_average() {
        LongTuple2 tuple = LongTuple.of(1L, 3L);
        assertEquals(2.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_LongTuple2_reverse() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        LongTuple2 reversed = tuple.reverse();
        assertEquals(2L, reversed._1);
        assertEquals(1L, reversed._2);
    }

    @Test
    public void test_LongTuple2_contains_found() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertTrue(tuple.contains(1L));
        assertTrue(tuple.contains(2L));
    }

    @Test
    public void test_LongTuple2_contains_notFound() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertFalse(tuple.contains(3L));
    }

    @Test
    public void test_LongTuple2_forEach() throws Exception {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        List<Long> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(2, values.size());
        assertEquals(1L, values.get(0).longValue());
        assertEquals(2L, values.get(1).longValue());
    }

    @Test
    public void test_LongTuple2_accept() throws Exception {
        LongTuple2 tuple = LongTuple.of(3L, 4L);
        final long[] result = new long[1];
        tuple.accept((a, b) -> result[0] = a + b);
        assertEquals(7L, result[0]);
    }

    @Test
    public void test_LongTuple2_map() throws Exception {
        LongTuple2 tuple = LongTuple.of(3L, 4L);
        Long result = tuple.map((a, b) -> a * b);
        assertEquals(12L, result.longValue());
    }

    @Test
    public void test_LongTuple2_filter_satisfied() throws Exception {
        LongTuple2 tuple = LongTuple.of(3L, 4L);
        Optional<LongTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_LongTuple2_filter_notSatisfied() throws Exception {
        LongTuple2 tuple = LongTuple.of(1L, 1L);
        Optional<LongTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_LongTuple2_hashCode() {
        LongTuple2 tuple1 = LongTuple.of(1L, 2L);
        LongTuple2 tuple2 = LongTuple.of(1L, 2L);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_LongTuple2_equals() {
        LongTuple2 tuple1 = LongTuple.of(1L, 2L);
        LongTuple2 tuple2 = LongTuple.of(1L, 2L);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void test_LongTuple2_toString() {
        LongTuple2 tuple = LongTuple.of(1L, 2L);
        assertEquals("(1, 2)", tuple.toString());
    }

    // ===== LongTuple3 Tests =====

    @Test
    public void test_LongTuple3_arity() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_LongTuple3_min() {
        LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
        assertEquals(1L, tuple.min());
    }

    @Test
    public void test_LongTuple3_max() {
        LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
        assertEquals(3L, tuple.max());
    }

    @Test
    public void test_LongTuple3_median() {
        LongTuple3 tuple = LongTuple.of(30L, 10L, 20L);
        assertEquals(20L, tuple.median());
    }

    @Test
    public void test_LongTuple3_sum() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(6L, tuple.sum());
    }

    @Test
    public void test_LongTuple3_average() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(2.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_LongTuple3_reverse() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        LongTuple3 reversed = tuple.reverse();
        assertEquals(3L, reversed._1);
        assertEquals(2L, reversed._2);
        assertEquals(1L, reversed._3);
    }

    @Test
    public void test_LongTuple3_contains() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertTrue(tuple.contains(2L));
        assertFalse(tuple.contains(5L));
    }

    @Test
    public void test_LongTuple3_forEach() throws Exception {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        List<Long> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(3, values.size());
    }

    @Test
    public void test_LongTuple3_accept() throws Exception {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        final long[] result = new long[1];
        tuple.accept((a, b, c) -> result[0] = a + b + c);
        assertEquals(6L, result[0]);
    }

    @Test
    public void test_LongTuple3_map() throws Exception {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        Long result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6L, result.longValue());
    }

    @Test
    public void test_LongTuple3_filter() throws Exception {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        Optional<LongTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());
    }

    // ===== LongTuple4+ Tests =====

    @Test
    public void test_LongTuple4_arity() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void test_LongTuple4_statisticalOperations() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        assertEquals(1L, tuple.min());
        assertEquals(4L, tuple.max());
        assertEquals(10L, tuple.sum());
        assertEquals(2.5, tuple.average(), 0.0001);
    }

    @Test
    public void test_LongTuple5_arity() {
        LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void test_LongTuple6_arity() {
        LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void test_LongTuple7_arity() {
        LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertEquals(7, tuple.arity());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_LongTuple8_arity() {
        LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertEquals(8, tuple.arity());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_LongTuple9_arity() {
        LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertEquals(9, tuple.arity());
    }

    // ===== Common Method Tests =====

    @Test
    public void test_toArray() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        long[] array = tuple.toArray();
        assertEquals(3, array.length);
        assertEquals(1L, array[0]);
        assertEquals(2L, array[1]);
        assertEquals(3L, array[2]);
    }

    @Test
    public void test_toArray_modification() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        long[] array = tuple.toArray();
        array[0] = 99L;
        assertEquals(1L, tuple._1);
    }

    @Test
    public void test_toList() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        LongList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(1L, list.get(0));
    }

    @Test
    public void test_forEach_multipleElements() throws Exception {
        LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
        List<Long> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(5, values.size());
    }

    @Test
    public void test_stream() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        long sum = tuple.stream().sum();
        assertEquals(6L, sum);
    }

    @Test
    public void test_hashCode_consistency() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void test_equals_reflexive() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        assertEquals(tuple, tuple);
    }

    @Test
    public void test_equals_symmetric() {
        LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
        LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
        assertEquals(tuple1, tuple2);
        assertEquals(tuple2, tuple1);
    }

    @Test
    public void test_equals_differentClass() {
        LongTuple2 tuple2 = LongTuple.of(1L, 2L);
        LongTuple3 tuple3 = LongTuple.of(1L, 2L, 3L);
        assertNotEquals(tuple2, tuple3);
    }

    @Test
    public void test_toString_format() {
        LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
        String str = tuple.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
    }

    // ===== Edge Cases =====

    @Test
    public void test_negativeValues() {
        LongTuple3 tuple = LongTuple.of(-1L, -2L, -3L);
        assertEquals(-3L, tuple.min());
        assertEquals(-1L, tuple.max());
        assertEquals(-6L, tuple.sum());
    }

    @Test
    public void test_zeroValues() {
        LongTuple3 tuple = LongTuple.of(0L, 0L, 0L);
        assertEquals(0L, tuple.min());
        assertEquals(0L, tuple.max());
        assertEquals(0L, tuple.sum());
    }

    @Test
    public void test_largeValues() {
        LongTuple2 tuple = LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE / 2);
        assertEquals(Long.MAX_VALUE / 2, tuple.min());
        assertEquals(Long.MAX_VALUE, tuple.max());
    }

    @Test
    public void test_mixedValues() {
        LongTuple3 tuple = LongTuple.of(-100L, 0L, 100L);
        assertEquals(-100L, tuple.min());
        assertEquals(100L, tuple.max());
        assertEquals(0L, tuple.sum());
    }

    @Test
    public void test_reverse_largerTuples() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        LongTuple4 reversed = tuple.reverse();
        assertEquals(4L, reversed._1);
        assertEquals(3L, reversed._2);
        assertEquals(2L, reversed._3);
        assertEquals(1L, reversed._4);
    }

    @Test
    public void test_median_evenSize() {
        LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
        long median = tuple.median();
        assertTrue(median >= 1L && median <= 4L);
    }

    @Test
    public void test_median_oddSize() {
        LongTuple5 tuple = LongTuple.of(50L, 10L, 30L, 20L, 40L);
        long median = tuple.median();
        assertEquals(30L, median);
    }
}
