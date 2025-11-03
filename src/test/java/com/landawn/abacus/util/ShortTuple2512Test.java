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
import com.landawn.abacus.util.ShortTuple.*;
import com.landawn.abacus.util.u.Optional;

/**
 * Comprehensive unit tests for ShortTuple and its nested tuple classes (ShortTuple0 through ShortTuple9).
 * Tests cover all public methods including factory methods, statistical operations, conversions, and edge cases.
 */
@Tag("2512")
public class ShortTuple2512Test extends TestBase {

    // ===== Factory Method Tests =====

    @Test
    public void test_of_singleElement() {
        ShortTuple1 tuple = ShortTuple.of((short) 42);
        assertNotNull(tuple);
        assertEquals((short) 42, tuple._1);
    }

    @Test
    public void test_of_twoElements() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertNotNull(tuple);
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 2, tuple._2);
    }

    @Test
    public void test_of_threeElements() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertNotNull(tuple);
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 2, tuple._2);
        assertEquals((short) 3, tuple._3);
    }

    @Test
    public void test_of_fourElements() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertNotNull(tuple);
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 4, tuple._4);
    }

    @Test
    public void test_of_fiveElements() {
        ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertNotNull(tuple);
        assertEquals((short) 5, tuple._5);
    }

    @Test
    public void test_of_sixElements() {
        ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        assertNotNull(tuple);
        assertEquals((short) 6, tuple._6);
    }

    @Test
    public void test_of_sevenElements() {
        ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        assertNotNull(tuple);
        assertEquals((short) 7, tuple._7);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_of_eightElements() {
        ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        assertNotNull(tuple);
        assertEquals((short) 8, tuple._8);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_of_nineElements() {
        ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertNotNull(tuple);
        assertEquals((short) 9, tuple._9);
    }

    @Test
    public void test_create_nullArray() {
        ShortTuple0 tuple = ShortTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_emptyArray() {
        ShortTuple0 tuple = ShortTuple.create(new short[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_singleElementArray() {
        ShortTuple1 tuple = ShortTuple.create(new short[] { (short) 42 });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals((short) 42, tuple._1);
    }

    @Test
    public void test_create_multipleElementsArray() {
        ShortTuple3 tuple = ShortTuple.create(new short[] { (short) 1, (short) 2, (short) 3 });
        assertNotNull(tuple);
        assertEquals(3, tuple.arity());
        assertEquals((short) 1, tuple._1);
        assertEquals((short) 2, tuple._2);
        assertEquals((short) 3, tuple._3);
    }

    @Test
    public void test_create_tooManyElements() {
        short[] array = new short[10];
        assertThrows(IllegalArgumentException.class, () -> ShortTuple.create(array));
    }

    // ===== ShortTuple0 Tests =====

    @Test
    public void test_ShortTuple0_arity() {
        ShortTuple0 tuple = ShortTuple.create(new short[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_ShortTuple0_min_throwsException() {
        ShortTuple0 tuple = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void test_ShortTuple0_max_throwsException() {
        ShortTuple0 tuple = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void test_ShortTuple0_median_throwsException() {
        ShortTuple0 tuple = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void test_ShortTuple0_sum() {
        ShortTuple0 tuple = ShortTuple.create(new short[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void test_ShortTuple0_average_throwsException() {
        ShortTuple0 tuple = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void test_ShortTuple0_reverse() {
        ShortTuple0 tuple = ShortTuple.create(new short[0]);
        ShortTuple0 reversed = tuple.reverse();
        assertSame(tuple, reversed);
    }

    @Test
    public void test_ShortTuple0_contains() {
        ShortTuple0 tuple = ShortTuple.create(new short[0]);
        assertFalse(tuple.contains((short) 1));
    }

    @Test
    public void test_ShortTuple0_toString() {
        ShortTuple0 tuple = ShortTuple.create(new short[0]);
        assertEquals("()", tuple.toString());
    }

    // ===== ShortTuple1 Tests =====

    @Test
    public void test_ShortTuple1_arity() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_ShortTuple1_min() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals((short) 5, tuple.min());
    }

    @Test
    public void test_ShortTuple1_max() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals((short) 5, tuple.max());
    }

    @Test
    public void test_ShortTuple1_median() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals((short) 5, tuple.median());
    }

    @Test
    public void test_ShortTuple1_sum() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals(5, tuple.sum());
    }

    @Test
    public void test_ShortTuple1_average() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals(5.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_ShortTuple1_reverse() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        ShortTuple1 reversed = tuple.reverse();
        assertEquals((short) 5, reversed._1);
    }

    @Test
    public void test_ShortTuple1_contains_found() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertTrue(tuple.contains((short) 5));
    }

    @Test
    public void test_ShortTuple1_contains_notFound() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertFalse(tuple.contains((short) 10));
    }

    @Test
    public void test_ShortTuple1_hashCode() {
        ShortTuple1 tuple1 = ShortTuple.of((short) 5);
        ShortTuple1 tuple2 = ShortTuple.of((short) 5);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_ShortTuple1_equals_same() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals(tuple, tuple);
    }

    @Test
    public void test_ShortTuple1_equals_equal() {
        ShortTuple1 tuple1 = ShortTuple.of((short) 5);
        ShortTuple1 tuple2 = ShortTuple.of((short) 5);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void test_ShortTuple1_equals_notEqual() {
        ShortTuple1 tuple1 = ShortTuple.of((short) 5);
        ShortTuple1 tuple2 = ShortTuple.of((short) 10);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void test_ShortTuple1_equals_null() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertNotEquals(null, tuple);
    }

    @Test
    public void test_ShortTuple1_equals_differentType() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertNotEquals("string", tuple);
    }

    @Test
    public void test_ShortTuple1_toString() {
        ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals("(5)", tuple.toString());
    }

    // ===== ShortTuple2 Tests =====

    @Test
    public void test_ShortTuple2_arity() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_ShortTuple2_min() {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 1);
        assertEquals((short) 1, tuple.min());
    }

    @Test
    public void test_ShortTuple2_max() {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 1);
        assertEquals((short) 3, tuple.max());
    }

    @Test
    public void test_ShortTuple2_median() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        short median = tuple.median();
        assertTrue(median == (short) 1 || median == (short) 2);
    }

    @Test
    public void test_ShortTuple2_sum() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertEquals(3, tuple.sum());
    }

    @Test
    public void test_ShortTuple2_average() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 3);
        assertEquals(2.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_ShortTuple2_reverse() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        ShortTuple2 reversed = tuple.reverse();
        assertEquals((short) 2, reversed._1);
        assertEquals((short) 1, reversed._2);
    }

    @Test
    public void test_ShortTuple2_contains_found() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertTrue(tuple.contains((short) 1));
        assertTrue(tuple.contains((short) 2));
    }

    @Test
    public void test_ShortTuple2_contains_notFound() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertFalse(tuple.contains((short) 3));
    }

    @Test
    public void test_ShortTuple2_forEach() throws Exception {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        List<Short> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(2, values.size());
        assertEquals((short) 1, values.get(0).shortValue());
        assertEquals((short) 2, values.get(1).shortValue());
    }

    @Test
    public void test_ShortTuple2_accept() throws Exception {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
        final int[] result = new int[1];
        tuple.accept((a, b) -> result[0] = a + b);
        assertEquals(7, result[0]);
    }

    @Test
    public void test_ShortTuple2_map() throws Exception {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
        Integer result = tuple.map((a, b) -> a * b);
        assertEquals(12, result.intValue());
    }

    @Test
    public void test_ShortTuple2_filter_satisfied() throws Exception {
        ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
        Optional<ShortTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_ShortTuple2_filter_notSatisfied() throws Exception {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 1);
        Optional<ShortTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_ShortTuple2_hashCode() {
        ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_ShortTuple2_equals() {
        ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void test_ShortTuple2_toString() {
        ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
        assertEquals("(1, 2)", tuple.toString());
    }

    // ===== ShortTuple3 Tests =====

    @Test
    public void test_ShortTuple3_arity() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_ShortTuple3_min() {
        ShortTuple3 tuple = ShortTuple.of((short) 3, (short) 1, (short) 2);
        assertEquals((short) 1, tuple.min());
    }

    @Test
    public void test_ShortTuple3_max() {
        ShortTuple3 tuple = ShortTuple.of((short) 3, (short) 1, (short) 2);
        assertEquals((short) 3, tuple.max());
    }

    @Test
    public void test_ShortTuple3_median() {
        ShortTuple3 tuple = ShortTuple.of((short) 30, (short) 10, (short) 20);
        assertEquals((short) 20, tuple.median());
    }

    @Test
    public void test_ShortTuple3_sum() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(6, tuple.sum());
    }

    @Test
    public void test_ShortTuple3_average() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(2.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_ShortTuple3_reverse() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 reversed = tuple.reverse();
        assertEquals((short) 3, reversed._1);
        assertEquals((short) 2, reversed._2);
        assertEquals((short) 1, reversed._3);
    }

    @Test
    public void test_ShortTuple3_contains() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertTrue(tuple.contains((short) 2));
        assertFalse(tuple.contains((short) 5));
    }

    @Test
    public void test_ShortTuple3_forEach() throws Exception {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        List<Short> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(3, values.size());
    }

    @Test
    public void test_ShortTuple3_accept() throws Exception {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        final int[] result = new int[1];
        tuple.accept((a, b, c) -> result[0] = a + b + c);
        assertEquals(6, result[0]);
    }

    @Test
    public void test_ShortTuple3_map() throws Exception {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        Integer result = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6, result.intValue());
    }

    @Test
    public void test_ShortTuple3_filter() throws Exception {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        Optional<ShortTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());
    }

    // ===== ShortTuple4+ Tests =====

    @Test
    public void test_ShortTuple4_arity() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void test_ShortTuple4_statisticalOperations() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        assertEquals((short) 1, tuple.min());
        assertEquals((short) 4, tuple.max());
        assertEquals(10, tuple.sum());
        assertEquals(2.5, tuple.average(), 0.0001);
    }

    @Test
    public void test_ShortTuple5_arity() {
        ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void test_ShortTuple6_arity() {
        ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void test_ShortTuple7_arity() {
        ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
        assertEquals(7, tuple.arity());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_ShortTuple8_arity() {
        ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
        assertEquals(8, tuple.arity());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_ShortTuple9_arity() {
        ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
        assertEquals(9, tuple.arity());
    }

    // ===== Common Method Tests =====

    @Test
    public void test_toArray() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        short[] array = tuple.toArray();
        assertEquals(3, array.length);
        assertEquals((short) 1, array[0]);
        assertEquals((short) 2, array[1]);
        assertEquals((short) 3, array[2]);
    }

    @Test
    public void test_toArray_modification() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        short[] array = tuple.toArray();
        array[0] = (short) 99;
        assertEquals((short) 1, tuple._1);
    }

    @Test
    public void test_toList() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals((short) 1, list.get(0));
    }

    @Test
    public void test_forEach_multipleElements() throws Exception {
        ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        List<Short> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(5, values.size());
    }

    @Test
    public void test_stream() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        int sum = tuple.stream().sum();
        assertEquals(6, sum);
    }

    @Test
    public void test_hashCode_consistency() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void test_equals_reflexive() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(tuple, tuple);
    }

    @Test
    public void test_equals_symmetric() {
        ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertEquals(tuple1, tuple2);
        assertEquals(tuple2, tuple1);
    }

    @Test
    public void test_equals_differentClass() {
        ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
        ShortTuple3 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 3);
        assertNotEquals(tuple2, tuple3);
    }

    @Test
    public void test_toString_format() {
        ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
        String str = tuple.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
    }

    // ===== Edge Cases =====

    @Test
    public void test_negativeValues() {
        ShortTuple3 tuple = ShortTuple.of((short) -1, (short) -2, (short) -3);
        assertEquals((short) -3, tuple.min());
        assertEquals((short) -1, tuple.max());
        assertEquals(-6, tuple.sum());
    }

    @Test
    public void test_zeroValues() {
        ShortTuple3 tuple = ShortTuple.of((short) 0, (short) 0, (short) 0);
        assertEquals((short) 0, tuple.min());
        assertEquals((short) 0, tuple.max());
        assertEquals(0, tuple.sum());
    }

    @Test
    public void test_largeValues() {
        ShortTuple2 tuple = ShortTuple.of(Short.MAX_VALUE, (short) (Short.MAX_VALUE / 2));
        assertEquals((short) (Short.MAX_VALUE / 2), tuple.min());
        assertEquals(Short.MAX_VALUE, tuple.max());
    }

    @Test
    public void test_mixedValues() {
        ShortTuple3 tuple = ShortTuple.of((short) -100, (short) 0, (short) 100);
        assertEquals((short) -100, tuple.min());
        assertEquals((short) 100, tuple.max());
        assertEquals(0, tuple.sum());
    }

    @Test
    public void test_reverse_largerTuples() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        ShortTuple4 reversed = tuple.reverse();
        assertEquals((short) 4, reversed._1);
        assertEquals((short) 3, reversed._2);
        assertEquals((short) 2, reversed._3);
        assertEquals((short) 1, reversed._4);
    }

    @Test
    public void test_median_evenSize() {
        ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
        short median = tuple.median();
        assertTrue(median >= (short) 1 && median <= (short) 4);
    }

    @Test
    public void test_median_oddSize() {
        ShortTuple5 tuple = ShortTuple.of((short) 50, (short) 10, (short) 30, (short) 20, (short) 40);
        short median = tuple.median();
        assertEquals((short) 30, median);
    }

    @Test
    public void test_sum_returnsInt() {
        ShortTuple3 tuple = ShortTuple.of((short) 100, (short) 200, (short) 300);
        int sum = tuple.sum();
        assertEquals(600, sum);
        // Note: sum() returns int (not short) for ShortTuple
    }
}
