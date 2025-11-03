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
import com.landawn.abacus.util.FloatTuple.*;
import com.landawn.abacus.util.u.Optional;

/**
 * Comprehensive unit tests for FloatTuple and its nested tuple classes (FloatTuple0 through FloatTuple9).
 * Tests cover all public methods including factory methods, statistical operations, conversions, and edge cases.
 */
@Tag("2512")
public class FloatTuple2512Test extends TestBase {

    // ===== Factory Method Tests =====

    @Test
    public void test_of_singleElement() {
        FloatTuple1 tuple = FloatTuple.of(3.14f);
        assertNotNull(tuple);
        assertEquals(3.14f, tuple._1, 0.0001f);
    }

    @Test
    public void test_of_twoElements() {
        FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
        assertNotNull(tuple);
        assertEquals(1.5f, tuple._1, 0.0001f);
        assertEquals(2.5f, tuple._2, 0.0001f);
    }

    @Test
    public void test_of_threeElements() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertNotNull(tuple);
        assertEquals(1.0f, tuple._1, 0.0001f);
        assertEquals(2.0f, tuple._2, 0.0001f);
        assertEquals(3.0f, tuple._3, 0.0001f);
    }

    @Test
    public void test_of_fourElements() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertNotNull(tuple);
        assertEquals(1.0f, tuple._1, 0.0001f);
        assertEquals(4.0f, tuple._4, 0.0001f);
    }

    @Test
    public void test_of_fiveElements() {
        FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertNotNull(tuple);
        assertEquals(5.0f, tuple._5, 0.0001f);
    }

    @Test
    public void test_of_sixElements() {
        FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        assertNotNull(tuple);
        assertEquals(6.0f, tuple._6, 0.0001f);
    }

    @Test
    public void test_of_sevenElements() {
        FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        assertNotNull(tuple);
        assertEquals(7.0f, tuple._7, 0.0001f);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_of_eightElements() {
        FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        assertNotNull(tuple);
        assertEquals(8.0f, tuple._8, 0.0001f);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_of_nineElements() {
        FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertNotNull(tuple);
        assertEquals(9.0f, tuple._9, 0.0001f);
    }

    @Test
    public void test_create_nullArray() {
        FloatTuple0 tuple = FloatTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_emptyArray() {
        FloatTuple0 tuple = FloatTuple.create(new float[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_singleElementArray() {
        FloatTuple1 tuple = FloatTuple.create(new float[] { 42.0f });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals(42.0f, tuple._1, 0.0001f);
    }

    @Test
    public void test_create_multipleElementsArray() {
        FloatTuple3 tuple = FloatTuple.create(new float[] { 1.0f, 2.0f, 3.0f });
        assertNotNull(tuple);
        assertEquals(3, tuple.arity());
        assertEquals(1.0f, tuple._1, 0.0001f);
        assertEquals(2.0f, tuple._2, 0.0001f);
        assertEquals(3.0f, tuple._3, 0.0001f);
    }

    @Test
    public void test_create_tooManyElements() {
        float[] array = new float[10];
        assertThrows(IllegalArgumentException.class, () -> FloatTuple.create(array));
    }

    // ===== FloatTuple0 Tests =====

    @Test
    public void test_FloatTuple0_arity() {
        FloatTuple0 tuple = FloatTuple.create(new float[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_FloatTuple0_min_throwsException() {
        FloatTuple0 tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void test_FloatTuple0_max_throwsException() {
        FloatTuple0 tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void test_FloatTuple0_median_throwsException() {
        FloatTuple0 tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void test_FloatTuple0_sum() {
        FloatTuple0 tuple = FloatTuple.create(new float[0]);
        assertEquals(0.0f, tuple.sum(), 0.0001f);
    }

    @Test
    public void test_FloatTuple0_average_throwsException() {
        FloatTuple0 tuple = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void test_FloatTuple0_reverse() {
        FloatTuple0 tuple = FloatTuple.create(new float[0]);
        FloatTuple0 reversed = tuple.reverse();
        assertSame(tuple, reversed);
    }

    @Test
    public void test_FloatTuple0_contains() {
        FloatTuple0 tuple = FloatTuple.create(new float[0]);
        assertFalse(tuple.contains(1.0f));
    }

    @Test
    public void test_FloatTuple0_toString() {
        FloatTuple0 tuple = FloatTuple.create(new float[0]);
        assertEquals("()", tuple.toString());
    }

    // ===== FloatTuple1 Tests =====

    @Test
    public void test_FloatTuple1_arity() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_FloatTuple1_min() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertEquals(5.0f, tuple.min(), 0.0001f);
    }

    @Test
    public void test_FloatTuple1_max() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertEquals(5.0f, tuple.max(), 0.0001f);
    }

    @Test
    public void test_FloatTuple1_median() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertEquals(5.0f, tuple.median(), 0.0001f);
    }

    @Test
    public void test_FloatTuple1_sum() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertEquals(5.0f, tuple.sum(), 0.0001f);
    }

    @Test
    public void test_FloatTuple1_average() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertEquals(5.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_FloatTuple1_reverse() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        FloatTuple1 reversed = tuple.reverse();
        assertEquals(5.0f, reversed._1, 0.0001f);
    }

    @Test
    public void test_FloatTuple1_contains_found() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertTrue(tuple.contains(5.0f));
    }

    @Test
    public void test_FloatTuple1_contains_notFound() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertFalse(tuple.contains(10.0f));
    }

    @Test
    public void test_FloatTuple1_hashCode() {
        FloatTuple1 tuple1 = FloatTuple.of(5.0f);
        FloatTuple1 tuple2 = FloatTuple.of(5.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_FloatTuple1_equals_same() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertEquals(tuple, tuple);
    }

    @Test
    public void test_FloatTuple1_equals_equal() {
        FloatTuple1 tuple1 = FloatTuple.of(5.0f);
        FloatTuple1 tuple2 = FloatTuple.of(5.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void test_FloatTuple1_equals_notEqual() {
        FloatTuple1 tuple1 = FloatTuple.of(5.0f);
        FloatTuple1 tuple2 = FloatTuple.of(10.0f);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void test_FloatTuple1_equals_null() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertNotEquals(null, tuple);
    }

    @Test
    public void test_FloatTuple1_equals_differentType() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertNotEquals("string", tuple);
    }

    @Test
    public void test_FloatTuple1_toString() {
        FloatTuple1 tuple = FloatTuple.of(5.0f);
        assertEquals("(5.0)", tuple.toString());
    }

    // ===== FloatTuple2 Tests =====

    @Test
    public void test_FloatTuple2_arity() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_FloatTuple2_min() {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 1.0f);
        assertEquals(1.0f, tuple.min(), 0.0001f);
    }

    @Test
    public void test_FloatTuple2_max() {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 1.0f);
        assertEquals(3.0f, tuple.max(), 0.0001f);
    }

    @Test
    public void test_FloatTuple2_median() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        float median = tuple.median();
        assertTrue(median == 1.0f || median == 2.0f);
    }

    @Test
    public void test_FloatTuple2_sum() {
        FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
        assertEquals(4.0f, tuple.sum(), 0.0001f);
    }

    @Test
    public void test_FloatTuple2_average() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 3.0f);
        assertEquals(2.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_FloatTuple2_reverse() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        FloatTuple2 reversed = tuple.reverse();
        assertEquals(2.0f, reversed._1, 0.0001f);
        assertEquals(1.0f, reversed._2, 0.0001f);
    }

    @Test
    public void test_FloatTuple2_contains_found() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertTrue(tuple.contains(1.0f));
        assertTrue(tuple.contains(2.0f));
    }

    @Test
    public void test_FloatTuple2_contains_notFound() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertFalse(tuple.contains(3.0f));
    }

    @Test
    public void test_FloatTuple2_forEach() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        List<Float> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(2, values.size());
        assertEquals(1.0f, values.get(0), 0.0001f);
        assertEquals(2.0f, values.get(1), 0.0001f);
    }

    @Test
    public void test_FloatTuple2_accept() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        final float[] result = new float[1];
        tuple.accept((a, b) -> result[0] = a + b);
        assertEquals(7.0f, result[0], 0.0001f);
    }

    @Test
    public void test_FloatTuple2_map() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        Float result = tuple.map((a, b) -> a * b);
        assertEquals(12.0f, result, 0.0001f);
    }

    @Test
    public void test_FloatTuple2_filter_satisfied() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
        Optional<FloatTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_FloatTuple2_filter_notSatisfied() throws Exception {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 1.0f);
        Optional<FloatTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_FloatTuple2_hashCode() {
        FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_FloatTuple2_equals() {
        FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void test_FloatTuple2_toString() {
        FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
        assertEquals("(1.0, 2.0)", tuple.toString());
    }

    // ===== FloatTuple3 Tests =====

    @Test
    public void test_FloatTuple3_arity() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_FloatTuple3_min() {
        FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
        assertEquals(1.0f, tuple.min(), 0.0001f);
    }

    @Test
    public void test_FloatTuple3_max() {
        FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
        assertEquals(3.0f, tuple.max(), 0.0001f);
    }

    @Test
    public void test_FloatTuple3_median() {
        FloatTuple3 tuple = FloatTuple.of(30.0f, 10.0f, 20.0f);
        assertEquals(20.0f, tuple.median(), 0.0001f);
    }

    @Test
    public void test_FloatTuple3_sum() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(6.0f, tuple.sum(), 0.0001f);
    }

    @Test
    public void test_FloatTuple3_average() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(2.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_FloatTuple3_reverse() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 reversed = tuple.reverse();
        assertEquals(3.0f, reversed._1, 0.0001f);
        assertEquals(2.0f, reversed._2, 0.0001f);
        assertEquals(1.0f, reversed._3, 0.0001f);
    }

    @Test
    public void test_FloatTuple3_contains() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertTrue(tuple.contains(2.0f));
        assertFalse(tuple.contains(5.0f));
    }

    @Test
    public void test_FloatTuple3_forEach() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        List<Float> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(3, values.size());
    }

    @Test
    public void test_FloatTuple3_accept() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        final float[] result = new float[1];
        tuple.accept((a, b, c) -> result[0] = a + b + c);
        assertEquals(6.0f, result[0], 0.0001f);
    }

    @Test
    public void test_FloatTuple3_map() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        Float result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6.0f, result, 0.0001f);
    }

    @Test
    public void test_FloatTuple3_filter() throws Exception {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        Optional<FloatTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());
    }

    // ===== FloatTuple4+ Tests =====

    @Test
    public void test_FloatTuple4_arity() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void test_FloatTuple4_statisticalOperations() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(1.0f, tuple.min(), 0.0001f);
        assertEquals(4.0f, tuple.max(), 0.0001f);
        assertEquals(10.0f, tuple.sum(), 0.0001f);
        assertEquals(2.5, tuple.average(), 0.0001);
    }

    @Test
    public void test_FloatTuple5_arity() {
        FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void test_FloatTuple6_arity() {
        FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void test_FloatTuple7_arity() {
        FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
        assertEquals(7, tuple.arity());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_FloatTuple8_arity() {
        FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
        assertEquals(8, tuple.arity());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_FloatTuple9_arity() {
        FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertEquals(9, tuple.arity());
    }

    // ===== Common Method Tests =====

    @Test
    public void test_toArray() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float[] array = tuple.toArray();
        assertEquals(3, array.length);
        assertEquals(1.0f, array[0], 0.0001f);
        assertEquals(2.0f, array[1], 0.0001f);
        assertEquals(3.0f, array[2], 0.0001f);
    }

    @Test
    public void test_toArray_modification() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        float[] array = tuple.toArray();
        array[0] = 99.0f;
        assertEquals(1.0f, tuple._1, 0.0001f);
    }

    @Test
    public void test_toList() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(1.0f, list.get(0), 0.0001f);
    }

    @Test
    public void test_forEach_multipleElements() throws Exception {
        FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        List<Float> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(5, values.size());
    }

    @Test
    public void test_stream() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        double sum = tuple.stream().sum();
        assertEquals(6.0, sum, 0.0001);
    }

    @Test
    public void test_hashCode_consistency() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void test_equals_reflexive() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(tuple, tuple);
    }

    @Test
    public void test_equals_symmetric() {
        FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(tuple1, tuple2);
        assertEquals(tuple2, tuple1);
    }

    @Test
    public void test_equals_differentClass() {
        FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
        FloatTuple3 tuple3 = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertNotEquals(tuple2, tuple3);
    }

    @Test
    public void test_toString_format() {
        FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        String str = tuple.toString();
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("2.0"));
        assertTrue(str.contains("3.0"));
    }

    // ===== Edge Cases =====

    @Test
    public void test_negativeValues() {
        FloatTuple3 tuple = FloatTuple.of(-1.0f, -2.0f, -3.0f);
        assertEquals(-3.0f, tuple.min(), 0.0001f);
        assertEquals(-1.0f, tuple.max(), 0.0001f);
        assertEquals(-6.0f, tuple.sum(), 0.0001f);
    }

    @Test
    public void test_zeroValues() {
        FloatTuple3 tuple = FloatTuple.of(0.0f, 0.0f, 0.0f);
        assertEquals(0.0f, tuple.min(), 0.0001f);
        assertEquals(0.0f, tuple.max(), 0.0001f);
        assertEquals(0.0f, tuple.sum(), 0.0001f);
    }

    @Test
    public void test_largeValues() {
        FloatTuple2 tuple = FloatTuple.of(Float.MAX_VALUE, Float.MAX_VALUE / 2);
        assertEquals(Float.MAX_VALUE / 2, tuple.min(), 0.0001f);
        assertEquals(Float.MAX_VALUE, tuple.max(), 0.0001f);
    }

    @Test
    public void test_NaN_handling() {
        FloatTuple2 tuple = FloatTuple.of(Float.NaN, 1.0f);
        assertTrue(Float.isNaN(tuple._1));
        assertEquals(1.0f, tuple._2, 0.0001f);
    }

    @Test
    public void test_infinity_handling() {
        FloatTuple2 tuple = FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
        assertEquals(Float.POSITIVE_INFINITY, tuple._1, 0.0001f);
        assertEquals(Float.NEGATIVE_INFINITY, tuple._2, 0.0001f);
    }

    @Test
    public void test_reverse_largerTuples() {
        FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
        FloatTuple4 reversed = tuple.reverse();
        assertEquals(4.0f, reversed._1, 0.0001f);
        assertEquals(3.0f, reversed._2, 0.0001f);
        assertEquals(2.0f, reversed._3, 0.0001f);
        assertEquals(1.0f, reversed._4, 0.0001f);
    }

    @Test
    public void test_contains_withNaN() {
        FloatTuple2 tuple = FloatTuple.of(Float.NaN, 1.0f);
        assertTrue(tuple.contains(Float.NaN));
    }
}
