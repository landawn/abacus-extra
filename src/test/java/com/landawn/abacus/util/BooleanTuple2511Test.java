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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple0;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple1;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple2;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple3;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple4;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple5;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple6;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple7;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple8;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple9;
import com.landawn.abacus.util.u.Optional;

/**
 * Comprehensive unit tests for BooleanTuple and its inner classes (BooleanTuple0-9).
 * Tests cover factory methods, accessor methods, utility methods, functional methods,
 * equality/hashCode, and stream operations.
 */
@Tag("2511")
public class BooleanTuple2511Test extends TestBase {

    // ============ Factory Method Tests - BooleanTuple.of() ============

    @Test
    public void testOf_tuple1() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf_tuple2() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf_tuple3() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf_tuple4() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(false, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf_tuple5() {
        BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(false, tuple._4);
        assertEquals(true, tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf_tuple6() {
        BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(false, tuple._4);
        assertEquals(true, tuple._5);
        assertEquals(false, tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf_tuple7() {
        BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(false, tuple._4);
        assertEquals(true, tuple._5);
        assertEquals(false, tuple._6);
        assertEquals(true, tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf_tuple8() {
        BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(false, tuple._4);
        assertEquals(true, tuple._5);
        assertEquals(false, tuple._6);
        assertEquals(true, tuple._7);
        assertEquals(false, tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf_tuple9() {
        BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(false, tuple._4);
        assertEquals(true, tuple._5);
        assertEquals(false, tuple._6);
        assertEquals(true, tuple._7);
        assertEquals(false, tuple._8);
        assertEquals(true, tuple._9);
        assertEquals(9, tuple.arity());
    }

    // ============ Factory Method Tests - BooleanTuple.create() ============

    @Test
    public void testCreate_nullArray() {
        BooleanTuple<?> tuple = BooleanTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_emptyArray() {
        BooleanTuple<?> tuple = BooleanTuple.create(new boolean[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_singleElement() {
        BooleanTuple1 tuple = BooleanTuple.create(new boolean[] { true });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals(true, tuple._1);
    }

    @Test
    public void testCreate_twoElements() {
        BooleanTuple2 tuple = BooleanTuple.create(new boolean[] { true, false });
        assertNotNull(tuple);
        assertEquals(2, tuple.arity());
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
    }

    @Test
    public void testCreate_threeElements() {
        BooleanTuple3 tuple = BooleanTuple.create(new boolean[] { true, false, true });
        assertNotNull(tuple);
        assertEquals(3, tuple.arity());
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
    }

    @Test
    public void testCreate_nineElements() {
        BooleanTuple9 tuple = BooleanTuple.create(new boolean[] { true, false, true, false, true, false, true, false, true });
        assertNotNull(tuple);
        assertEquals(9, tuple.arity());
        assertEquals(true, tuple._1);
        assertEquals(true, tuple._9);
    }

    @Test
    public void testCreate_tooManyElements() {
        assertThrows(IllegalArgumentException.class, () -> BooleanTuple.create(new boolean[10]));
    }

    // ============ Accessor Tests - Direct Field Access ============

    @Test
    public void testTuple1_directFieldAccess() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals(true, tuple._1);
    }

    @Test
    public void testTuple2_directFieldAccess() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
    }

    @Test
    public void testTuple3_directFieldAccess() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
    }

    // ============ Reverse Tests ============

    @Test
    public void testTuple0_reverse() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        BooleanTuple0 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(tuple, reversed);
    }

    @Test
    public void testTuple1_reverse() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        BooleanTuple1 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertNotSame(tuple, reversed);
        assertEquals(true, reversed._1);
    }

    @Test
    public void testTuple2_reverse() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        BooleanTuple2 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertNotSame(tuple, reversed);
        assertEquals(false, reversed._1);
        assertEquals(true, reversed._2);
    }

    @Test
    public void testTuple3_reverse() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        BooleanTuple3 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertNotSame(tuple, reversed);
        assertEquals(true, reversed._1);
        assertEquals(false, reversed._2);
        assertEquals(true, reversed._3);
    }

    @Test
    public void testTuple5_reverse() {
        BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        BooleanTuple5 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(true, reversed._1);
        assertEquals(false, reversed._2);
        assertEquals(true, reversed._3);
        assertEquals(false, reversed._4);
        assertEquals(true, reversed._5);
    }

    // ============ Contains Tests ============

    @Test
    public void testTuple0_contains() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        assertFalse(tuple.contains(true));
        assertFalse(tuple.contains(false));
    }

    @Test
    public void testTuple1_contains_true() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertTrue(tuple.contains(true));
        assertFalse(tuple.contains(false));
    }

    @Test
    public void testTuple1_contains_false() {
        BooleanTuple1 tuple = BooleanTuple.of(false);
        assertFalse(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testTuple2_contains_true() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testTuple3_contains_mixed() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testTuple5_contains_all_true() {
        BooleanTuple5 tuple = BooleanTuple.of(true, true, true, true, true);
        assertTrue(tuple.contains(true));
        assertFalse(tuple.contains(false));
    }

    // ============ toArray Tests ============

    @Test
    public void testTuple0_toArray() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        boolean[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void testTuple1_toArray() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        boolean[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(1, array.length);
        assertEquals(true, array[0]);
    }

    @Test
    public void testTuple2_toArray() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        boolean[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(2, array.length);
        assertArrayEquals(new boolean[] { true, false }, array);
    }

    @Test
    public void testTuple3_toArray() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        boolean[] array = tuple.toArray();
        assertNotNull(array);
        assertArrayEquals(new boolean[] { true, false, true }, array);
    }

    @Test
    public void testToArray_immutability() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        boolean[] array1 = tuple.toArray();
        boolean[] array2 = tuple.toArray();
        assertNotSame(array1, array2);
    }

    // ============ toList Tests ============

    @Test
    public void testTuple0_toList() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        BooleanList list = tuple.toList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testTuple1_toList() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        BooleanList list = tuple.toList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(true, list.get(0));
    }

    @Test
    public void testTuple2_toList() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        BooleanList list = tuple.toList();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(true, list.get(0));
        assertEquals(false, list.get(1));
    }

    @Test
    public void testTuple3_toList() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        BooleanList list = tuple.toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(true, list.get(0));
        assertEquals(false, list.get(1));
        assertEquals(true, list.get(2));
    }

    // ============ forEach Tests ============

    @Test
    public void testTuple0_forEach() throws Exception {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        List<Boolean> results = new ArrayList<>();
        tuple.forEach(b -> results.add(b));
        assertEquals(0, results.size());
    }

    @Test
    public void testTuple1_forEach() throws Exception {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        List<Boolean> results = new ArrayList<>();
        tuple.forEach(b -> results.add(b));
        assertEquals(1, results.size());
        assertEquals(true, results.get(0));
    }

    @Test
    public void testTuple2_forEach() throws Exception {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        List<Boolean> results = new ArrayList<>();
        tuple.forEach(b -> results.add(b));
        assertEquals(2, results.size());
        assertEquals(true, results.get(0));
        assertEquals(false, results.get(1));
    }

    @Test
    public void testTuple3_forEach() throws Exception {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        List<Boolean> results = new ArrayList<>();
        tuple.forEach(b -> results.add(b));
        assertEquals(3, results.size());
        assertEquals(true, results.get(0));
        assertEquals(false, results.get(1));
        assertEquals(true, results.get(2));
    }

    // ============ Stream Tests ============

    @Test
    public void testTuple1_stream() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        long count = tuple.stream().count();
        assertEquals(1, count);
    }

    @Test
    public void testTuple2_stream_filter() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        long trueCount = tuple.stream().filter(b -> b).count();
        assertEquals(1, trueCount);
    }

    @Test
    public void testTuple3_stream_allMatch() {
        BooleanTuple3 tuple = BooleanTuple.of(true, true, true);
        boolean allTrue = tuple.stream().allMatch(b -> b);
        assertTrue(allTrue);
    }

    // ============ BooleanTuple2.accept Tests ============

    @Test
    public void testTuple2_accept() throws Exception {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        List<Boolean> results = new ArrayList<>();
        tuple.accept((a, b) -> {
            results.add(a);
            results.add(b);
        });
        assertEquals(2, results.size());
        assertEquals(true, results.get(0));
        assertEquals(false, results.get(1));
    }

    // ============ BooleanTuple2.map Tests ============

    @Test
    public void testTuple2_map() throws Exception {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        boolean result = tuple.map((a, b) -> a && b);
        assertEquals(false, result);
    }

    @Test
    public void testTuple2_map_or() throws Exception {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        boolean result = tuple.map((a, b) -> a || b);
        assertEquals(true, result);
    }

    // ============ BooleanTuple2.filter Tests ============

    @Test
    public void testTuple2_filter_pass() throws Exception {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        Optional<BooleanTuple2> result = tuple.filter((a, b) -> a || b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2_filter_fail() throws Exception {
        BooleanTuple2 tuple = BooleanTuple.of(false, false);
        Optional<BooleanTuple2> result = tuple.filter((a, b) -> a || b);
        assertFalse(result.isPresent());
    }

    // ============ BooleanTuple3.accept Tests ============

    @Test
    public void testTuple3_accept() throws Exception {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        List<Boolean> results = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            results.add(a);
            results.add(b);
            results.add(c);
        });
        assertEquals(3, results.size());
        assertEquals(true, results.get(0));
        assertEquals(false, results.get(1));
        assertEquals(true, results.get(2));
    }

    // ============ BooleanTuple3.map Tests ============

    @Test
    public void testTuple3_map() throws Exception {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        boolean result = tuple.map((a, b, c) -> a && b && c);
        assertEquals(false, result);
    }

    @Test
    public void testTuple3_map_or() throws Exception {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        boolean result = tuple.map((a, b, c) -> a || b || c);
        assertEquals(true, result);
    }

    // ============ BooleanTuple3.filter Tests ============

    @Test
    public void testTuple3_filter_pass() throws Exception {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a || c);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple3_filter_fail() throws Exception {
        BooleanTuple3 tuple = BooleanTuple.of(false, false, false);
        Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a || b || c);
        assertFalse(result.isPresent());
    }

    // ============ hashCode Tests ============

    @Test
    public void testTuple0_hashCode() {
        BooleanTuple0 tuple1 = BooleanTuple.create(new boolean[0]);
        BooleanTuple0 tuple2 = BooleanTuple.create(new boolean[0]);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testTuple1_hashCode_true() {
        BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple1 tuple2 = BooleanTuple.of(true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertEquals(1231, tuple1.hashCode());
    }

    @Test
    public void testTuple1_hashCode_false() {
        BooleanTuple1 tuple = BooleanTuple.of(false);
        assertEquals(1237, tuple.hashCode());
    }

    @Test
    public void testTuple2_hashCode() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testTuple3_hashCode() {
        BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testTuple2_hashCode_different() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(false, true);
        assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // ============ equals Tests ============

    @Test
    public void testTuple0_equals() {
        BooleanTuple0 tuple1 = BooleanTuple.create(new boolean[0]);
        BooleanTuple0 tuple2 = BooleanTuple.create(new boolean[0]);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple1_equals_same() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testTuple1_equals_true() {
        BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple1 tuple2 = BooleanTuple.of(true);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple1_equals_false() {
        BooleanTuple1 tuple1 = BooleanTuple.of(false);
        BooleanTuple1 tuple2 = BooleanTuple.of(false);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple1_notEquals() {
        BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple1 tuple2 = BooleanTuple.of(false);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple2_equals() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple2_notEquals_different_values() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(false, true);
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple3_equals() {
        BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testTuple_notEquals_null() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertNotEquals(tuple, null);
        assertFalse(tuple.equals(null));
    }

    @Test
    public void testTuple_notEquals_differentType() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertNotEquals(tuple, "true");
        assertNotEquals(tuple, 1);
    }

    @Test
    public void testTuple2_notEquals_differentTuple() {
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        BooleanTuple3 tuple3 = BooleanTuple.of(true, false, true);
        assertNotEquals(tuple2, tuple3);
    }

    // ============ toString Tests ============

    @Test
    public void testTuple0_toString() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        assertEquals("()", tuple.toString());
    }

    @Test
    public void testTuple1_toString() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals("(true)", tuple.toString());
    }

    @Test
    public void testTuple1_toString_false() {
        BooleanTuple1 tuple = BooleanTuple.of(false);
        assertEquals("(false)", tuple.toString());
    }

    @Test
    public void testTuple2_toString() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertEquals("(true, false)", tuple.toString());
    }

    @Test
    public void testTuple3_toString() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals("(true, false, true)", tuple.toString());
    }

    // ============ Arity Tests ============

    @Test
    public void testTuple0_arity() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testTuple1_arity() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testTuple2_arity() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testTuple3_arity() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testTuple4_arity() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testTuple5_arity() {
        BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testTuple6_arity() {
        BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testTuple7_arity() {
        BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testTuple8_arity() {
        BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testTuple9_arity() {
        BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertEquals(9, tuple.arity());
    }

    // ============ Edge Cases and Special Tests ============

    @Test
    public void testTuple_immutability() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        // The tuple is immutable, so we can't change _1, but we verify it stays the same
        assertEquals(true, tuple._1);
        assertEquals(true, tuple._1);
    }

    @Test
    public void testMultipleTuples_independence() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(false, true);
        assertNotEquals(tuple1, tuple2);
        assertEquals(true, tuple1._1);
        assertEquals(false, tuple2._1);
    }

    @Test
    public void testTuple_allTrueValues() {
        BooleanTuple3 tuple = BooleanTuple.of(true, true, true);
        assertTrue(tuple.contains(true));
        assertFalse(tuple.contains(false));
        assertTrue(tuple.stream().allMatch(b -> b));
    }

    @Test
    public void testTuple_allFalseValues() {
        BooleanTuple3 tuple = BooleanTuple.of(false, false, false);
        assertFalse(tuple.contains(true));
        assertTrue(tuple.contains(false));
        assertFalse(tuple.stream().anyMatch(b -> b));
    }

}
