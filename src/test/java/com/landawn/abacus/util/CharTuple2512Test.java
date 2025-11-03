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
import com.landawn.abacus.util.CharTuple.CharTuple0;
import com.landawn.abacus.util.CharTuple.CharTuple1;
import com.landawn.abacus.util.CharTuple.CharTuple2;
import com.landawn.abacus.util.CharTuple.CharTuple3;
import com.landawn.abacus.util.CharTuple.CharTuple4;
import com.landawn.abacus.util.CharTuple.CharTuple5;
import com.landawn.abacus.util.CharTuple.CharTuple6;
import com.landawn.abacus.util.CharTuple.CharTuple7;
import com.landawn.abacus.util.CharTuple.CharTuple8;
import com.landawn.abacus.util.CharTuple.CharTuple9;
import com.landawn.abacus.util.u.Optional;

/**
 * Comprehensive unit tests for CharTuple and its inner classes (CharTuple0-9).
 * Tests cover all public methods including factory methods, statistical methods,
 * utility methods, functional methods, equality/hashCode, and stream operations.
 */
@Tag("2512")
public class CharTuple2512Test extends TestBase {

    // ============ Factory Method Tests - CharTuple.of() ============

    @Test
    public void test_of_tuple1() {
        CharTuple1 tuple = CharTuple.of('A');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_of_tuple2() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('B', tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_of_tuple3() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('B', tuple._2);
        assertEquals('C', tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_of_tuple4() {
        CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('D', tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void test_of_tuple5() {
        CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('E', tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void test_of_tuple6() {
        CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('F', tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void test_of_tuple7() {
        CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('G', tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void test_of_tuple8() {
        CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('H', tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void test_of_tuple9() {
        CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('I', tuple._9);
        assertEquals(9, tuple.arity());
    }

    // ============ Factory Method Tests - CharTuple.create() ============

    @Test
    public void test_create_nullArray() {
        CharTuple0 tuple = CharTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_emptyArray() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_create_arraySize1() {
        CharTuple1 tuple = CharTuple.create(new char[] { 'A' });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals('A', tuple._1);
    }

    @Test
    public void test_create_arraySize2() {
        CharTuple2 tuple = CharTuple.create(new char[] { 'A', 'B' });
        assertNotNull(tuple);
        assertEquals(2, tuple.arity());
        assertEquals('A', tuple._1);
        assertEquals('B', tuple._2);
    }

    @Test
    public void test_create_arraySize9() {
        CharTuple9 tuple = CharTuple.create(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' });
        assertNotNull(tuple);
        assertEquals(9, tuple.arity());
        assertEquals('A', tuple._1);
        assertEquals('I', tuple._9);
    }

    @Test
    public void test_create_arrayTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> {
            CharTuple.create(new char[10]);
        });
    }

    // ============ Tuple0 Tests ============

    @Test
    public void test_tuple0_arity() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void test_tuple0_min_throwsException() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void test_tuple0_max_throwsException() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void test_tuple0_median_throwsException() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void test_tuple0_sum() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void test_tuple0_average_throwsException() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void test_tuple0_reverse() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        CharTuple0 reversed = tuple.reverse();
        assertSame(tuple, reversed);
    }

    @Test
    public void test_tuple0_contains() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertFalse(tuple.contains('A'));
    }

    @Test
    public void test_tuple0_toArray() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        char[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void test_tuple0_toList() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        CharList list = tuple.toList();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void test_tuple0_forEach() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        List<Character> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertTrue(collected.isEmpty());
    }

    @Test
    public void test_tuple0_stream() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        long count = tuple.stream().count();
        assertEquals(0, count);
    }

    @Test
    public void test_tuple0_toString() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertEquals("()", tuple.toString());
    }

    // ============ Tuple1 Tests ============

    @Test
    public void test_tuple1_arity() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals(1, tuple.arity());
    }

    @Test
    public void test_tuple1_min() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals('A', tuple.min());
    }

    @Test
    public void test_tuple1_max() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals('A', tuple.max());
    }

    @Test
    public void test_tuple1_median() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals('A', tuple.median());
    }

    @Test
    public void test_tuple1_sum() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals((int) 'A', tuple.sum());
    }

    @Test
    public void test_tuple1_average() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals((double) 'A', tuple.average(), 0.0001);
    }

    @Test
    public void test_tuple1_reverse() {
        CharTuple1 tuple = CharTuple.of('A');
        CharTuple1 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals('A', reversed._1);
        assertNotSame(tuple, reversed);
    }

    @Test
    public void test_tuple1_contains_found() {
        CharTuple1 tuple = CharTuple.of('A');
        assertTrue(tuple.contains('A'));
    }

    @Test
    public void test_tuple1_contains_notFound() {
        CharTuple1 tuple = CharTuple.of('A');
        assertFalse(tuple.contains('B'));
    }

    @Test
    public void test_tuple1_toArray() {
        CharTuple1 tuple = CharTuple.of('A');
        char[] array = tuple.toArray();
        assertArrayEquals(new char[] { 'A' }, array);
    }

    @Test
    public void test_tuple1_toList() {
        CharTuple1 tuple = CharTuple.of('A');
        CharList list = tuple.toList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals('A', list.get(0));
    }

    @Test
    public void test_tuple1_forEach() {
        CharTuple1 tuple = CharTuple.of('A');
        List<Character> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(1, collected.size());
        assertEquals('A', collected.get(0));
    }

    @Test
    public void test_tuple1_stream() {
        CharTuple1 tuple = CharTuple.of('A');
        int sum = tuple.stream().sum();
        assertEquals((int) 'A', sum);
    }

    @Test
    public void test_tuple1_hashCode() {
        CharTuple1 tuple1 = CharTuple.of('A');
        CharTuple1 tuple2 = CharTuple.of('A');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple1_equals_same() {
        CharTuple1 tuple = CharTuple.of('A');
        assertTrue(tuple.equals(tuple));
    }

    @Test
    public void test_tuple1_equals_equal() {
        CharTuple1 tuple1 = CharTuple.of('A');
        CharTuple1 tuple2 = CharTuple.of('A');
        assertTrue(tuple1.equals(tuple2));
        assertTrue(tuple2.equals(tuple1));
    }

    @Test
    public void test_tuple1_equals_notEqual() {
        CharTuple1 tuple1 = CharTuple.of('A');
        CharTuple1 tuple2 = CharTuple.of('B');
        assertFalse(tuple1.equals(tuple2));
    }

    @Test
    public void test_tuple1_equals_null() {
        CharTuple1 tuple = CharTuple.of('A');
        assertFalse(tuple.equals(null));
    }

    @Test
    public void test_tuple1_equals_differentClass() {
        CharTuple1 tuple = CharTuple.of('A');
        assertFalse(tuple.equals("not a tuple"));
    }

    @Test
    public void test_tuple1_toString() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals("(A)", tuple.toString());
    }

    // ============ Tuple2 Tests ============

    @Test
    public void test_tuple2_arity() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals(2, tuple.arity());
    }

    @Test
    public void test_tuple2_min() {
        CharTuple2 tuple = CharTuple.of('B', 'A');
        assertEquals('A', tuple.min());
    }

    @Test
    public void test_tuple2_max() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals('B', tuple.max());
    }

    @Test
    public void test_tuple2_median() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals('A', tuple.median());
    }

    @Test
    public void test_tuple2_sum() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals((int) 'A' + (int) 'B', tuple.sum());
    }

    @Test
    public void test_tuple2_average() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals(((int) 'A' + (int) 'B') / 2.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_tuple2_reverse() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        CharTuple2 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals('B', reversed._1);
        assertEquals('A', reversed._2);
    }

    @Test
    public void test_tuple2_contains_found() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertTrue(tuple.contains('A'));
        assertTrue(tuple.contains('B'));
    }

    @Test
    public void test_tuple2_contains_notFound() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertFalse(tuple.contains('C'));
    }

    @Test
    public void test_tuple2_toArray() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        char[] array = tuple.toArray();
        assertArrayEquals(new char[] { 'A', 'B' }, array);
    }

    @Test
    public void test_tuple2_toList() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        CharList list = tuple.toList();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void test_tuple2_forEach() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        List<Character> collected = new ArrayList<>();
        tuple.forEach(collected::add);
        assertEquals(2, collected.size());
        assertEquals('A', collected.get(0));
        assertEquals('B', collected.get(1));
    }

    @Test
    public void test_tuple2_stream() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        long count = tuple.stream().count();
        assertEquals(2, count);
    }

    @Test
    public void test_tuple2_accept() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        List<Character> collected = new ArrayList<>();
        tuple.accept((a, b) -> {
            collected.add(a);
            collected.add(b);
        });
        assertEquals(2, collected.size());
        assertEquals('A', collected.get(0));
        assertEquals('B', collected.get(1));
    }

    @Test
    public void test_tuple2_map() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        String result = tuple.map((a, b) -> "" + a + b);
        assertEquals("AB", result);
    }

    @Test
    public void test_tuple2_filter_match() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void test_tuple2_filter_noMatch() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        Optional<CharTuple2> result = tuple.filter((a, b) -> a > b);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_tuple2_hashCode() {
        CharTuple2 tuple1 = CharTuple.of('A', 'B');
        CharTuple2 tuple2 = CharTuple.of('A', 'B');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple2_equals() {
        CharTuple2 tuple1 = CharTuple.of('A', 'B');
        CharTuple2 tuple2 = CharTuple.of('A', 'B');
        CharTuple2 tuple3 = CharTuple.of('B', 'A');

        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
        assertFalse(tuple1.equals(null));
    }

    @Test
    public void test_tuple2_toString() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals("(A, B)", tuple.toString());
    }

    // ============ Tuple3 Tests ============

    @Test
    public void test_tuple3_arity() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertEquals(3, tuple.arity());
    }

    @Test
    public void test_tuple3_min() {
        CharTuple3 tuple = CharTuple.of('C', 'A', 'B');
        assertEquals('A', tuple.min());
    }

    @Test
    public void test_tuple3_max() {
        CharTuple3 tuple = CharTuple.of('C', 'A', 'B');
        assertEquals('C', tuple.max());
    }

    @Test
    public void test_tuple3_median() {
        CharTuple3 tuple = CharTuple.of('C', 'A', 'B');
        assertEquals('B', tuple.median());
    }

    @Test
    public void test_tuple3_sum() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertEquals((int) 'A' + (int) 'B' + (int) 'C', tuple.sum());
    }

    @Test
    public void test_tuple3_average() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertEquals(((int) 'A' + (int) 'B' + (int) 'C') / 3.0, tuple.average(), 0.0001);
    }

    @Test
    public void test_tuple3_reverse() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        CharTuple3 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals('C', reversed._1);
        assertEquals('B', reversed._2);
        assertEquals('A', reversed._3);
    }

    @Test
    public void test_tuple3_contains() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertTrue(tuple.contains('A'));
        assertTrue(tuple.contains('B'));
        assertTrue(tuple.contains('C'));
        assertFalse(tuple.contains('D'));
    }

    @Test
    public void test_tuple3_toArray() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        char[] array = tuple.toArray();
        assertArrayEquals(new char[] { 'A', 'B', 'C' }, array);
    }

    @Test
    public void test_tuple3_accept() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        List<Character> collected = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            collected.add(a);
            collected.add(b);
            collected.add(c);
        });
        assertEquals(3, collected.size());
    }

    @Test
    public void test_tuple3_map() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        String result = tuple.map((a, b, c) -> "" + a + b + c);
        assertEquals("ABC", result);
    }

    @Test
    public void test_tuple3_filter_match() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
    }

    @Test
    public void test_tuple3_filter_noMatch() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        Optional<CharTuple3> result = tuple.filter((a, b, c) -> a > c);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_tuple3_hashCode() {
        CharTuple3 tuple1 = CharTuple.of('A', 'B', 'C');
        CharTuple3 tuple2 = CharTuple.of('A', 'B', 'C');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void test_tuple3_equals() {
        CharTuple3 tuple1 = CharTuple.of('A', 'B', 'C');
        CharTuple3 tuple2 = CharTuple.of('A', 'B', 'C');
        CharTuple3 tuple3 = CharTuple.of('A', 'B', 'D');

        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
    }

    @Test
    public void test_tuple3_toString() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertEquals("(A, B, C)", tuple.toString());
    }

    // ============ Tuple4-9 Basic Tests ============

    @Test
    public void test_tuple4_basic() {
        CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
        assertEquals(4, tuple.arity());
        assertEquals('A', tuple.min());
        assertEquals('D', tuple.max());
        assertTrue(tuple.contains('C'));
        CharTuple4 reversed = tuple.reverse();
        assertEquals('D', reversed._1);
        assertEquals('A', reversed._4);
    }

    @Test
    public void test_tuple5_basic() {
        CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
        assertEquals(5, tuple.arity());
        assertEquals('A', tuple.min());
        assertEquals('E', tuple.max());
        assertEquals('C', tuple.median());
        assertTrue(tuple.contains('C'));
    }

    @Test
    public void test_tuple6_basic() {
        CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
        assertEquals(6, tuple.arity());
        assertEquals('A', tuple.min());
        assertEquals('F', tuple.max());
    }

    @Test
    public void test_tuple7_basic() {
        CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
        assertEquals(7, tuple.arity());
        assertEquals('D', tuple.median());
        assertEquals(7, tuple.toArray().length);
    }

    @Test
    public void test_tuple8_basic() {
        CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
        assertEquals(8, tuple.arity());
        assertEquals(8, tuple.toArray().length);
    }

    @Test
    public void test_tuple9_basic() {
        CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
        assertEquals(9, tuple.arity());
        assertEquals(9, tuple.toArray().length);
    }

    // ============ Edge Cases and Additional Coverage ============

    @Test
    public void test_numericChars() {
        CharTuple3 tuple = CharTuple.of('0', '1', '2');
        assertEquals('0', tuple.min());
        assertEquals('2', tuple.max());
        assertEquals('1', tuple.median());
    }

    @Test
    public void test_specialChars() {
        CharTuple3 tuple = CharTuple.of('!', '@', '#');
        assertEquals('!', tuple.min());
        assertEquals('@', tuple.max());
    }

    @Test
    public void test_mixedCase() {
        CharTuple2 tuple = CharTuple.of('a', 'A');
        assertEquals('A', tuple.min()); // uppercase has lower ASCII value
        assertEquals('a', tuple.max());
    }

    @Test
    public void test_toArray_independence() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        char[] array = tuple.toArray();
        array[0] = 'Z';
        assertEquals('A', tuple._1); // Tuple should be unaffected
    }

    @Test
    public void test_toList_independence() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        CharList list = tuple.toList();
        list.set(0, 'Z');
        assertEquals('A', tuple._1); // Tuple should be unaffected
    }

    @Test
    public void test_stream_operations() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        int sum = tuple.stream().sum();
        assertEquals((int) 'A' + (int) 'B' + (int) 'C', sum);
    }

    @Test
    public void test_forEach_withException() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertThrows(RuntimeException.class, () -> {
            tuple.forEach(c -> {
                if (c == 'B') throw new RuntimeException("test exception");
            });
        });
    }

    @Test
    public void test_equals_differentArity() {
        CharTuple1 tuple1 = CharTuple.of('A');
        CharTuple2 tuple2 = CharTuple.of('A', 'B');
        assertFalse(tuple1.equals(tuple2));
    }

    @Test
    public void test_hashCode_consistency() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        int hash1 = tuple.hashCode();
        int hash2 = tuple.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void test_create_allSizes() {
        for (int i = 0; i <= 9; i++) {
            char[] array = new char[i];
            CharTuple<?> tuple = CharTuple.create(array);
            assertNotNull(tuple);
            assertEquals(i, tuple.arity());
        }
    }

    @Test
    public void test_median_evenCount() {
        CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
        assertEquals('B', tuple.median()); // Lower middle value
    }

    @Test
    public void test_whitespaceChars() {
        CharTuple2 tuple = CharTuple.of(' ', '\t');
        assertTrue(tuple.contains(' '));
        assertTrue(tuple.contains('\t'));
        assertNotNull(tuple.toString());
    }
}
