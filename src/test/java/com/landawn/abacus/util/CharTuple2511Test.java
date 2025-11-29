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
import java.util.concurrent.atomic.AtomicInteger;

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

@Tag("2511")
public class CharTuple2511Test extends TestBase {

    // ============ Factory Method Tests - CharTuple.of() ============

    @Test
    public void testOf_tuple1() {
        CharTuple1 tuple = CharTuple.of('A');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf_tuple2() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('B', tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf_tuple3() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('B', tuple._2);
        assertEquals('C', tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf_tuple4() {
        CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('B', tuple._2);
        assertEquals('C', tuple._3);
        assertEquals('D', tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf_tuple5() {
        CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('E', tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf_tuple6() {
        CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('F', tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf_tuple7() {
        CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('G', tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf_tuple8() {
        CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('H', tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf_tuple9() {
        CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('I', tuple._9);
        assertEquals(9, tuple.arity());
    }

    // ============ Create from Array Tests ============

    @Test
    public void testCreate_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_null() {
        CharTuple0 tuple = CharTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_single() {
        CharTuple1 tuple = CharTuple.create(new char[] { 'X' });
        assertNotNull(tuple);
        assertEquals('X', tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate_two() {
        CharTuple2 tuple = CharTuple.create(new char[] { 'X', 'Y' });
        assertNotNull(tuple);
        assertEquals('X', tuple._1);
        assertEquals('Y', tuple._2);
    }

    @Test
    public void testCreate_nine() {
        CharTuple9 tuple = CharTuple.create(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' });
        assertNotNull(tuple);
        assertEquals('A', tuple._1);
        assertEquals('I', tuple._9);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testCreate_tooMany() {
        assertThrows(IllegalArgumentException.class, () -> {
            CharTuple.create(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' });
        });
    }

    // ============ Statistics Tests - Min/Max/Median/Sum/Average ============

    @Test
    public void testMin_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void testMin_single() {
        CharTuple1 tuple = CharTuple.of('M');
        assertEquals('M', tuple.min());
    }

    @Test
    public void testMin_multiple() {
        CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
        assertEquals('A', tuple.min());
    }

    @Test
    public void testMax_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void testMax_single() {
        CharTuple1 tuple = CharTuple.of('M');
        assertEquals('M', tuple.max());
    }

    @Test
    public void testMax_multiple() {
        CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
        assertEquals('Z', tuple.max());
    }

    @Test
    public void testMedian_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void testMedian_single() {
        CharTuple1 tuple = CharTuple.of('M');
        assertEquals('M', tuple.median());
    }

    @Test
    public void testMedian_odd() {
        CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
        assertEquals('M', tuple.median());
    }

    @Test
    public void testMedian_even() {
        CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
        assertEquals('B', tuple.median());   // Lower middle for even length
    }

    @Test
    public void testSum_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void testSum_single() {
        CharTuple1 tuple = CharTuple.of('A');   // 65
        assertEquals(65, tuple.sum());
    }

    @Test
    public void testSum_multiple() {
        CharTuple2 tuple = CharTuple.of('A', 'B');   // 65 + 66 = 131
        assertEquals(131, tuple.sum());
    }

    @Test
    public void testAverage_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void testAverage_single() {
        CharTuple1 tuple = CharTuple.of('A');   // 65
        assertEquals(65.0, tuple.average());
    }

    @Test
    public void testAverage_multiple() {
        CharTuple2 tuple = CharTuple.of('A', 'C');   // (65 + 67) / 2 = 66.0
        assertEquals(66.0, tuple.average());
    }

    // ============ Reverse Tests ============

    @Test
    public void testReverse_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        CharTuple0 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testReverse_single() {
        CharTuple1 tuple = CharTuple.of('A');
        CharTuple1 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals('A', reversed._1);
        assertNotSame(tuple, reversed);   // Should be new instance
    }

    @Test
    public void testReverse_multiple() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        CharTuple3 reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals('C', reversed._1);
        assertEquals('B', reversed._2);
        assertEquals('A', reversed._3);
    }

    @Test
    public void testReverse_twoElements() {
        CharTuple2 tuple = CharTuple.of('X', 'Y');
        CharTuple2 reversed = tuple.reverse();
        assertEquals('Y', reversed._1);
        assertEquals('X', reversed._2);
    }

    // ============ Contains Tests ============

    @Test
    public void testContains_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertFalse(tuple.contains('A'));
    }

    @Test
    public void testContains_single_found() {
        CharTuple1 tuple = CharTuple.of('A');
        assertTrue(tuple.contains('A'));
    }

    @Test
    public void testContains_single_notfound() {
        CharTuple1 tuple = CharTuple.of('A');
        assertFalse(tuple.contains('B'));
    }

    @Test
    public void testContains_multiple_found() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertTrue(tuple.contains('A'));
        assertTrue(tuple.contains('B'));
        assertTrue(tuple.contains('C'));
    }

    @Test
    public void testContains_multiple_notfound() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertFalse(tuple.contains('Z'));
    }

    // ============ Functional Methods - CharTuple2 ============

    @Test
    public void testAccept_tuple2() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        List<Character> captured = new ArrayList<>();
        tuple.accept((a, b) -> {
            captured.add(a);
            captured.add(b);
        });
        assertEquals(2, captured.size());
        assertEquals('A', captured.get(0));
        assertEquals('B', captured.get(1));
    }

    @Test
    public void testMap_tuple2() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        String result = tuple.map((a, b) -> "" + a + b);
        assertEquals("AB", result);
    }

    @Test
    public void testFilter_tuple2_passes() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testFilter_tuple2_fails() {
        CharTuple2 tuple = CharTuple.of('Z', 'A');
        Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);
        assertFalse(result.isPresent());
    }

    // ============ Functional Methods - CharTuple3 ============

    @Test
    public void testAccept_tuple3() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        List<Character> captured = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            captured.add(a);
            captured.add(b);
            captured.add(c);
        });
        assertEquals(3, captured.size());
        assertEquals('A', captured.get(0));
        assertEquals('B', captured.get(1));
        assertEquals('C', captured.get(2));
    }

    @Test
    public void testMap_tuple3() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        String result = tuple.map((a, b, c) -> "" + a + b + c);
        assertEquals("ABC", result);
    }

    @Test
    public void testFilter_tuple3_passes() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testFilter_tuple3_fails() {
        CharTuple3 tuple = CharTuple.of('C', 'B', 'A');
        Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertFalse(result.isPresent());
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        AtomicInteger count = new AtomicInteger(0);
        tuple.forEach(c -> count.incrementAndGet());
        assertEquals(0, count.get());
    }

    @Test
    public void testForEach_tuple2() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        List<Character> visited = new ArrayList<>();
        tuple.forEach(visited::add);
        assertEquals(2, visited.size());
        assertEquals('A', visited.get(0));
        assertEquals('B', visited.get(1));
    }

    @Test
    public void testForEach_tuple3() {
        CharTuple3 tuple = CharTuple.of('X', 'Y', 'Z');
        List<Character> visited = new ArrayList<>();
        tuple.forEach(visited::add);
        assertEquals(3, visited.size());
        assertEquals('X', visited.get(0));
        assertEquals('Y', visited.get(1));
        assertEquals('Z', visited.get(2));
    }

    // ============ ToArray Tests ============

    @Test
    public void testToArray_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        char[] array = tuple.toArray();
        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void testToArray_single() {
        CharTuple1 tuple = CharTuple.of('A');
        char[] array = tuple.toArray();
        assertArrayEquals(new char[] { 'A' }, array);
    }

    @Test
    public void testToArray_multiple() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        char[] array = tuple.toArray();
        assertArrayEquals(new char[] { 'A', 'B', 'C' }, array);
    }

    @Test
    public void testToArray_independence() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        char[] array1 = tuple.toArray();
        char[] array2 = tuple.toArray();
        assertNotSame(array1, array2);   // Should be independent copies
        assertArrayEquals(array1, array2);
    }

    // ============ ToList Tests ============

    @Test
    public void testToList_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        CharList list = tuple.toList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testToList_single() {
        CharTuple1 tuple = CharTuple.of('A');
        CharList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals('A', list.get(0));
    }

    @Test
    public void testToList_multiple() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        CharList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals('A', list.get(0));
        assertEquals('B', list.get(1));
        assertEquals('C', list.get(2));
    }

    // ============ Stream Tests ============

    @Test
    public void testStream_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        int sum = tuple.stream().sum();
        assertEquals(0, sum);
    }

    @Test
    public void testStream_single() {
        CharTuple1 tuple = CharTuple.of('A');   // 65
        int sum = tuple.stream().sum();
        assertEquals(65, sum);
    }

    @Test
    public void testStream_multiple() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');   // 65 + 66 + 67 = 198
        int sum = tuple.stream().sum();
        assertEquals(198, sum);
    }

    // ============ Equality and HashCode Tests ============

    @Test
    public void testEquals_sameInstance() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEquals_sameValues() {
        CharTuple2 tuple1 = CharTuple.of('A', 'B');
        CharTuple2 tuple2 = CharTuple.of('A', 'B');
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_differentValues() {
        CharTuple2 tuple1 = CharTuple.of('A', 'B');
        CharTuple2 tuple2 = CharTuple.of('X', 'Y');
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_differentTypes() {
        CharTuple1 tuple1 = CharTuple.of('A');
        CharTuple2 tuple2 = CharTuple.of('A', 'B');
        assertNotEquals(tuple1, tuple2);
    }

    @Test
    public void testEquals_null() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertNotEquals(tuple, null);
    }

    @Test
    public void testEquals_otherObject() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertNotEquals(tuple, "AB");
    }

    @Test
    public void testHashCode_sameValues() {
        CharTuple2 tuple1 = CharTuple.of('A', 'B');
        CharTuple2 tuple2 = CharTuple.of('A', 'B');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_differentValues() {
        CharTuple2 tuple1 = CharTuple.of('A', 'B');
        CharTuple2 tuple2 = CharTuple.of('X', 'Y');
        assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCode_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertNotNull(tuple.hashCode());
    }

    // ============ ToString Tests ============

    @Test
    public void testToString_empty() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertEquals("()", tuple.toString());
    }

    @Test
    public void testToString_single() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals("(A)", tuple.toString());
    }

    @Test
    public void testToString_multiple() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertEquals("(A, B, C)", tuple.toString());
    }

    // ============ Arity Tests ============

    @Test
    public void testArity_0() {
        CharTuple0 tuple = CharTuple.create(new char[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testArity_1() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testArity_2() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testArity_3() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testArity_4() {
        CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testArity_5() {
        CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testArity_6() {
        CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testArity_7() {
        CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testArity_8() {
        CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testArity_9() {
        CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
        assertEquals(9, tuple.arity());
    }

    // ============ Field Accessors Tests ============

    @Test
    public void testFieldAccessors_tuple5() {
        CharTuple5 tuple = CharTuple.of('1', '2', '3', '4', '5');
        assertEquals('1', tuple._1);
        assertEquals('2', tuple._2);
        assertEquals('3', tuple._3);
        assertEquals('4', tuple._4);
        assertEquals('5', tuple._5);
    }

    @Test
    public void testFieldAccessors_tuple9() {
        CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
        assertEquals('A', tuple._1);
        assertEquals('B', tuple._2);
        assertEquals('C', tuple._3);
        assertEquals('D', tuple._4);
        assertEquals('E', tuple._5);
        assertEquals('F', tuple._6);
        assertEquals('G', tuple._7);
        assertEquals('H', tuple._8);
        assertEquals('I', tuple._9);
    }

    // ============ Complex Statistics Tests ============

    @Test
    public void testStatistics_allOperations() {
        CharTuple5 tuple = CharTuple.of('A', 'E', 'C', 'B', 'D');
        assertEquals('A', tuple.min());
        assertEquals('E', tuple.max());
        assertEquals('C', tuple.median());
        assertEquals(65 + 69 + 67 + 66 + 68, tuple.sum());   // A(65)+E(69)+C(67)+B(66)+D(68)
        assertTrue(tuple.average() > 0);
    }

    @Test
    public void testContains_allElements() {
        CharTuple5 tuple = CharTuple.of('X', 'Y', 'Z', 'W', 'V');
        assertTrue(tuple.contains('X'));
        assertTrue(tuple.contains('Y'));
        assertTrue(tuple.contains('Z'));
        assertTrue(tuple.contains('W'));
        assertTrue(tuple.contains('V'));
        assertFalse(tuple.contains('A'));
    }

    // ============ Edge Cases Tests ============

    @Test
    public void testSingleElement_allOperations() {
        CharTuple1 tuple = CharTuple.of('X');
        assertEquals('X', tuple.min());
        assertEquals('X', tuple.max());
        assertEquals('X', tuple.median());
        assertEquals('X', tuple.average());   // 88.0
        assertEquals(88, tuple.sum());
        assertTrue(tuple.contains('X'));
        assertFalse(tuple.contains('Y'));
        CharTuple1 reversed = tuple.reverse();
        assertEquals('X', reversed._1);
    }

    @Test
    public void testLargeCharValues() {
        CharTuple3 tuple = CharTuple.of('\u00FF', '\u0080', '\u0100');
        assertNotNull(tuple);
        assertTrue(tuple.contains('\u0080'));
    }

    @Test
    public void testSpecialChars() {
        CharTuple3 tuple = CharTuple.of('\n', '\t', ' ');
        assertEquals(3, tuple.arity());
        assertTrue(tuple.contains('\n'));
        assertTrue(tuple.contains('\t'));
        assertTrue(tuple.contains(' '));
    }
}
