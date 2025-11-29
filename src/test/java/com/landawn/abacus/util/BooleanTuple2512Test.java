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
 * Tests cover all public methods including factory methods, accessor methods, utility methods,
 * functional methods, equality/hashCode, and stream operations.
 */
@Tag("2512")
public class BooleanTuple2512Test extends TestBase {

    // ============ Factory Method Tests - BooleanTuple.of() ============

    @Test
    public void test_of_tuple1() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_of_tuple2() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_of_tuple3() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_of_tuple4() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(false, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void test_of_tuple5() {
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
    public void test_of_tuple6() {
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
    public void test_of_tuple7() {
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
    public void test_of_tuple8() {
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
    public void test_of_tuple9() {
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
    public void test_create_nullArray() {
        BooleanTuple0 tuple = BooleanTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_emptyArray() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_arraySize1() {
        BooleanTuple1 tuple = BooleanTuple.create(new boolean[] { true });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals(true, tuple._1);
    }

    @Test
    public void test_create_arraySize2() {
        BooleanTuple2 tuple = BooleanTuple.create(new boolean[] { true, false });
        assertNotNull(tuple);
        assertEquals(2, tuple.arity());
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
    }

    @Test
    public void test_create_arraySize9() {
        BooleanTuple9 tuple = BooleanTuple.create(new boolean[] { true, false, true, false, true, false, true, false, true });
        assertNotNull(tuple);
        assertEquals(9, tuple.arity());
        assertEquals(true, tuple._1);
        assertEquals(true, tuple._9);
    }

    @Test
    public void test_create_arrayTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> {
            BooleanTuple.create(new boolean[10]);
        });
    }

    // ============ Tuple0 Tests ============

    @Test
    public void test_tuple0_arity() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_tuple0_reverse() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        BooleanTuple0 reversed = tuple.reverse();
        assertSame(tuple, reversed);
    }

    @Test
    public void test_tuple0_contains() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        assertFalse(tuple.contains(true));
        assertFalse(tuple.contains(false));
    }

    @Test
    public void test_tuple0_toArray() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        boolean[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void test_tuple0_toList() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        BooleanList list = tuple.toList();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void test_tuple0_forEach() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        List<Boolean> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertTrue(collected.isEmpty());
    }

    @Test
    public void test_tuple0_stream() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        long count = tuple.stream().count();
        assertEquals(0, count);
    }

    @Test
    public void test_tuple0_toString() {
        BooleanTuple0 tuple = BooleanTuple.create(new boolean[0]);
        assertEquals("()", tuple.toString());
    }

    // ============ Tuple1 Tests ============

    @Test
    public void test_tuple1_arity() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_tuple1_reverse() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        BooleanTuple1 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(true, reversed._1);
        assertNotSame(tuple, reversed);
    }

    @Test
    public void test_tuple1_contains_found() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void test_tuple1_contains_notFound() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertFalse(tuple.contains(false));
    }

    @Test
    public void test_tuple1_toArray() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        boolean[] array = tuple.toArray();
        assertArrayEquals(new boolean[] { true }, array);
    }

    @Test
    public void test_tuple1_toList() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        BooleanList list = tuple.toList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(true, list.get(0));
    }

    @Test
    public void test_tuple1_forEach() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        List<Boolean> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(1, collected.size());
        assertEquals(true, collected.get(0));
    }

    @Test
    public void test_tuple1_stream() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        long trueCount = tuple.stream().filter(b -> b).count();
        assertEquals(1, trueCount);
    }

    @Test
    public void test_tuple1_hashCode() {
        BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple1 tuple2 = BooleanTuple.of(true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple1_equals_same() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertTrue(tuple.equals(tuple));
    }

    @Test
    public void test_tuple1_equals_equal() {
        BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple1 tuple2 = BooleanTuple.of(true);
        assertTrue(tuple1.equals(tuple2));
        assertTrue(tuple2.equals(tuple1));
    }

    @Test
    public void test_tuple1_equals_notEqual() {
        BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple1 tuple2 = BooleanTuple.of(false);
        assertFalse(tuple1.equals(tuple2));
    }

    @Test
    public void test_tuple1_equals_null() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertFalse(tuple.equals(null));
    }

    @Test
    public void test_tuple1_equals_differentClass() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertFalse(tuple.equals("not a tuple"));
    }

    @Test
    public void test_tuple1_toString() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals("(true)", tuple.toString());
    }

    // ============ Tuple2 Tests ============

    @Test
    public void test_tuple2_arity() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_tuple2_reverse() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        BooleanTuple2 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(false, reversed._1);
        assertEquals(true, reversed._2);
    }

    @Test
    public void test_tuple2_contains_found() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void test_tuple2_contains_notFound() {
        BooleanTuple2 tuple = BooleanTuple.of(true, true);
        assertFalse(tuple.contains(false));
    }

    @Test
    public void test_tuple2_toArray() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        boolean[] array = tuple.toArray();
        assertArrayEquals(new boolean[] { true, false }, array);
    }

    @Test
    public void test_tuple2_toList() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        BooleanList list = tuple.toList();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void test_tuple2_forEach() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        List<Boolean> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(2, collected.size());
        assertEquals(true, collected.get(0));
        assertEquals(false, collected.get(1));
    }

    @Test
    public void test_tuple2_stream() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        long count = tuple.stream().count();
        assertEquals(2, count);
    }

    @Test
    public void test_tuple2_accept() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        List<Boolean> collected = new ArrayList<>();
        tuple.accept((a, b) -> {
            collected.add(a);
            collected.add(b);
        });
        assertEquals(2, collected.size());
        assertEquals(true, collected.get(0));
        assertEquals(false, collected.get(1));
    }

    @Test
    public void test_tuple2_map() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        String result = tuple.map((a, b) -> a + "," + b);
        assertEquals("true,false", result);
    }

    @Test
    public void test_tuple2_filter_match() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        Optional<BooleanTuple2> result = tuple.filter((a, b) -> a != b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_tuple2_filter_noMatch() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        Optional<BooleanTuple2> result = tuple.filter((a, b) -> a == b);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_tuple2_hashCode() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple2_equals() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple3 = BooleanTuple.of(false, true);

        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
        assertFalse(tuple1.equals(null));
    }

    @Test
    public void test_tuple2_toString() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertEquals("(true, false)", tuple.toString());
    }

    // ============ Tuple3 Tests ============

    @Test
    public void test_tuple3_arity() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_tuple3_reverse() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        BooleanTuple3 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(true, reversed._1);
        assertEquals(false, reversed._2);
        assertEquals(true, reversed._3);
    }

    @Test
    public void test_tuple3_contains() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void test_tuple3_toArray() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        boolean[] array = tuple.toArray();
        assertArrayEquals(new boolean[] { true, false, true }, array);
    }

    @Test
    public void test_tuple3_accept() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        List<Boolean> collected = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            collected.add(a);
            collected.add(b);
            collected.add(c);
        });
        assertEquals(3, collected.size());
    }

    @Test
    public void test_tuple3_map() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        String result = tuple.map((a, b, c) -> a + "," + b + "," + c);
        assertEquals("true,false,true", result);
    }

    @Test
    public void test_tuple3_filter_match() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a == c);
        assertTrue(result.isPresent());
    }

    @Test
    public void test_tuple3_filter_noMatch() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a == b);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_tuple3_hashCode() {
        BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple3_equals() {
        BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        BooleanTuple3 tuple3 = BooleanTuple.of(false, false, true);

        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
    }

    @Test
    public void test_tuple3_toString() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals("(true, false, true)", tuple.toString());
    }

    // ============ Tuple4-9 Basic Tests ============

    @Test
    public void test_tuple4_basic() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertEquals(4, tuple.arity());
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
        BooleanTuple4 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
        assertEquals(true, reversed._2);
        assertEquals(false, reversed._3);
        assertEquals(true, reversed._4);
    }

    @Test
    public void test_tuple5_basic() {
        BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        assertEquals(5, tuple.arity());
        assertTrue(tuple.contains(true));
        BooleanTuple5 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
        assertEquals(false, reversed._2);
    }

    @Test
    public void test_tuple6_basic() {
        BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        assertEquals(6, tuple.arity());
        assertTrue(tuple.contains(false));
        BooleanTuple6 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
    }

    @Test
    public void test_tuple7_basic() {
        BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        assertEquals(7, tuple.arity());
        assertEquals(7, tuple.toArray().length);
        BooleanTuple7 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
    }

    @Test
    public void test_tuple8_basic() {
        BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertEquals(8, tuple.arity());
        assertEquals(8, tuple.toArray().length);
        BooleanTuple8 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
    }

    @Test
    public void test_tuple9_basic() {
        BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertEquals(9, tuple.arity());
        assertEquals(9, tuple.toArray().length);
        BooleanTuple9 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
        assertEquals(true, reversed._9);
    }

    // ============ Edge Cases and Additional Coverage ============

    @Test
    public void test_toArray_independence() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        boolean[] array = tuple.toArray();
        array[0] = false;
        assertEquals(true, tuple._1);   // Tuple should be unaffected
    }

    @Test
    public void test_toList_independence() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        BooleanList list = tuple.toList();
        list.set(0, false);
        assertEquals(true, tuple._1);   // Tuple should be unaffected
    }

    @Test
    public void test_stream_operations() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        long trueCount = tuple.stream().filter(b -> b).count();
        assertEquals(2, trueCount);
    }

    @Test
    public void test_forEach_withException() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertThrows(RuntimeException.class, () -> {
            tuple.forEach(b -> {
                if (!b)
                    throw new RuntimeException("test exception");
            });
        });
    }

    @Test
    public void test_equals_differentArity() {
        BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        assertFalse(tuple1.equals(tuple2));
    }

    @Test
    public void test_hashCode_consistency() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void test_create_allSizes() {
        for (int i = 0; i <= 9; i++) {
            boolean[] array = new boolean[i];
            BooleanTuple<?> tuple = BooleanTuple.create(array);
            assertNotNull(tuple);
            assertEquals(i, tuple.arity());
        }
    }
}
