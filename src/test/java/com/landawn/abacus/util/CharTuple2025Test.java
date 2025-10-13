/*
 * Copyright (C) 2025 HaiYang Li
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
import com.landawn.abacus.util.stream.CharStream;

/**
 * Comprehensive test suite for CharTuple and its nested classes.
 * Tests all public methods including factory methods, statistical operations,
 * collection conversions, and special methods in Tuple2 and Tuple3.
 */
@Tag("2025")
public class CharTuple2025Test extends TestBase {

    // Factory method tests
    @Test
    public void testOf1() {
        CharTuple1 tuple = CharTuple.of('a');
        assertEquals('a', tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf2() {
        CharTuple2 tuple = CharTuple.of('a', 'b');
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf3() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf4() {
        CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf5() {
        CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
        assertEquals('a', tuple._1);
        assertEquals('e', tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf6() {
        CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        assertEquals('a', tuple._1);
        assertEquals('f', tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf7() {
        CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        assertEquals('a', tuple._1);
        assertEquals('g', tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf8() {
        CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        assertEquals('a', tuple._1);
        assertEquals('h', tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf9() {
        CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        assertEquals('a', tuple._1);
        assertEquals('i', tuple._9);
        assertEquals(9, tuple.arity());
    }

    // Create method tests
    @Test
    public void testCreateEmpty() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreateNull() {
        CharTuple<CharTuple0> tuple = CharTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate1() {
        CharTuple1 tuple = CharTuple.create(new char[] { 'a' });
        assertEquals('a', tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate3() {
        CharTuple3 tuple = CharTuple.create(new char[] { 'a', 'b', 'c' });
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
    }

    @Test
    public void testCreate9() {
        CharTuple9 tuple = CharTuple.create(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i' });
        assertEquals('a', tuple._1);
        assertEquals('i', tuple._9);
    }

    @Test
    public void testCreateTooMany() {
        assertThrows(IllegalArgumentException.class, () -> {
            CharTuple.create(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j' });
        });
    }

    // Statistical method tests - min
    @Test
    public void testMinTuple1() {
        CharTuple1 tuple = CharTuple.of('a');
        assertEquals('a', tuple.min());
    }

    @Test
    public void testMinTuple3() {
        CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
        assertEquals('a', tuple.min());
    }

    @Test
    public void testMinTuple0ThrowsException() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    // Statistical method tests - max
    @Test
    public void testMaxTuple1() {
        CharTuple1 tuple = CharTuple.of('a');
        assertEquals('a', tuple.max());
    }

    @Test
    public void testMaxTuple3() {
        CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
        assertEquals('c', tuple.max());
    }

    @Test
    public void testMaxTuple0ThrowsException() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    // Statistical method tests - median
    @Test
    public void testMedianTuple1() {
        CharTuple1 tuple = CharTuple.of('a');
        assertEquals('a', tuple.median());
    }

    @Test
    public void testMedianTuple3() {
        CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
        assertEquals('b', tuple.median());
    }

    @Test
    public void testMedianTuple0ThrowsException() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    // Statistical method tests - sum
    @Test
    public void testSumTuple0() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void testSumTuple1() {
        CharTuple1 tuple = CharTuple.of('a');
        assertEquals(97, tuple.sum());
    }

    @Test
    public void testSumTuple3() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertEquals(294, tuple.sum());
    }

    // Statistical method tests - average
    @Test
    public void testAverageTuple1() {
        CharTuple1 tuple = CharTuple.of('a');
        assertEquals(97.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple3() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertEquals(98.0, tuple.average(), 0.001);
    }

    @Test
    public void testAverageTuple0ThrowsException() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    // Reverse tests
    @Test
    public void testReverseTuple0() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        CharTuple<CharTuple0> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testReverseTuple1() {
        CharTuple1 tuple = CharTuple.of('a');
        CharTuple1 reversed = tuple.reverse();
        assertEquals('a', reversed._1);
    }

    @Test
    public void testReverseTuple2() {
        CharTuple2 tuple = CharTuple.of('a', 'b');
        CharTuple2 reversed = tuple.reverse();
        assertEquals('b', reversed._1);
        assertEquals('a', reversed._2);
    }

    @Test
    public void testReverseTuple3() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        CharTuple3 reversed = tuple.reverse();
        assertEquals('c', reversed._1);
        assertEquals('b', reversed._2);
        assertEquals('a', reversed._3);
    }

    // Contains tests
    @Test
    public void testContainsTuple0() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        assertFalse(tuple.contains('a'));
    }

    @Test
    public void testContainsTuple1True() {
        CharTuple1 tuple = CharTuple.of('a');
        assertTrue(tuple.contains('a'));
    }

    @Test
    public void testContainsTuple1False() {
        CharTuple1 tuple = CharTuple.of('a');
        assertFalse(tuple.contains('z'));
    }

    @Test
    public void testContainsTuple3() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertTrue(tuple.contains('a'));
        assertTrue(tuple.contains('b'));
        assertTrue(tuple.contains('c'));
        assertFalse(tuple.contains('z'));
    }

    // toArray tests
    @Test
    public void testToArrayTuple0() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        char[] array = tuple.toArray();
        assertEquals(0, array.length);
    }

    @Test
    public void testToArrayTuple1() {
        CharTuple1 tuple = CharTuple.of('a');
        char[] array = tuple.toArray();
        assertArrayEquals(new char[] { 'a' }, array);
    }

    @Test
    public void testToArrayTuple3() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        char[] array = tuple.toArray();
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, array);
    }

    @Test
    public void testToArrayModificationDoesNotAffectTuple() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        char[] array = tuple.toArray();
        array[0] = 'x';
        assertEquals('a', tuple._1);
    }

    // toList tests
    @Test
    public void testToListTuple0() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        CharList list = tuple.toList();
        assertEquals(0, list.size());
    }

    @Test
    public void testToListTuple1() {
        CharTuple1 tuple = CharTuple.of('a');
        CharList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals('a', list.get(0));
    }

    @Test
    public void testToListTuple3() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        CharList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals('a', list.get(0));
        assertEquals('b', list.get(1));
        assertEquals('c', list.get(2));
    }

    // forEach tests
    @Test
    public void testForEachTuple0() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        List<Character> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(0, result.size());
    }

    @Test
    public void testForEachTuple1() {
        CharTuple1 tuple = CharTuple.of('a');
        List<Character> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(1, result.size());
        assertEquals(Character.valueOf('a'), result.get(0));
    }

    @Test
    public void testForEachTuple3() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        List<Character> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Character.valueOf('a'), result.get(0));
        assertEquals(Character.valueOf('b'), result.get(1));
        assertEquals(Character.valueOf('c'), result.get(2));
    }

    // stream tests
    @Test
    public void testStreamTuple0() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        CharStream stream = tuple.stream();
        assertEquals(0, stream.count());
    }

    @Test
    public void testStreamTuple1() {
        CharTuple1 tuple = CharTuple.of('a');
        CharStream stream = tuple.stream();
        assertEquals(97, stream.sum());
    }

    @Test
    public void testStreamTuple3() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        CharStream stream = tuple.stream();
        assertEquals(294, stream.sum());
    }

    // hashCode tests
    @Test
    public void testHashCodeTuple1() {
        CharTuple1 tuple1 = CharTuple.of('a');
        CharTuple1 tuple2 = CharTuple.of('a');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple2() {
        CharTuple2 tuple1 = CharTuple.of('a', 'b');
        CharTuple2 tuple2 = CharTuple.of('a', 'b');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple3() {
        CharTuple3 tuple1 = CharTuple.of('a', 'b', 'c');
        CharTuple3 tuple2 = CharTuple.of('a', 'b', 'c');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // equals tests
    @Test
    public void testEqualsSameObject() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEqualsNull() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertNotEquals(null, tuple);
    }

    @Test
    public void testEqualsDifferentClass() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertNotEquals("not a tuple", tuple);
    }

    @Test
    public void testEqualsTuple1() {
        CharTuple1 tuple1 = CharTuple.of('a');
        CharTuple1 tuple2 = CharTuple.of('a');
        CharTuple1 tuple3 = CharTuple.of('z');
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple2() {
        CharTuple2 tuple1 = CharTuple.of('a', 'b');
        CharTuple2 tuple2 = CharTuple.of('a', 'b');
        CharTuple2 tuple3 = CharTuple.of('a', 'c');
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple3() {
        CharTuple3 tuple1 = CharTuple.of('a', 'b', 'c');
        CharTuple3 tuple2 = CharTuple.of('a', 'b', 'c');
        CharTuple3 tuple3 = CharTuple.of('a', 'b', 'd');
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    // toString tests
    @Test
    public void testToStringTuple0() {
        CharTuple<CharTuple0> tuple = CharTuple.create(new char[0]);
        assertEquals("[]", tuple.toString());
    }

    @Test
    public void testToStringTuple1() {
        CharTuple1 tuple = CharTuple.of('a');
        String str = tuple.toString();
        assertTrue(str.contains("a"));
    }

    @Test
    public void testToStringTuple3() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        String str = tuple.toString();
        assertTrue(str.contains("a"));
        assertTrue(str.contains("b"));
        assertTrue(str.contains("c"));
    }

    // Tuple2 special methods - accept
    @Test
    public void testTuple2Accept() {
        CharTuple2 tuple = CharTuple.of('c', 'd');
        List<Character> result = new ArrayList<>();
        tuple.accept((a, b) -> {
            result.add(a);
            result.add(b);
        });
        assertEquals(2, result.size());
        assertEquals(Character.valueOf('c'), result.get(0));
        assertEquals(Character.valueOf('d'), result.get(1));
    }

    // Tuple2 special methods - map
    @Test
    public void testTuple2Map() {
        CharTuple2 tuple = CharTuple.of('c', 'd');
        String result = tuple.map((a, b) -> String.valueOf(a) + String.valueOf(b));
        assertEquals("cd", result);
    }

    // Tuple2 special methods - filter
    @Test
    public void testTuple2FilterTrue() {
        CharTuple2 tuple = CharTuple.of('c', 'd');
        var result = tuple.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2FilterFalse() {
        CharTuple2 tuple = CharTuple.of('c', 'd');
        var result = tuple.filter((a, b) -> a > b);
        assertFalse(result.isPresent());
    }

    // Tuple3 special methods - accept
    @Test
    public void testTuple3Accept() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        List<Character> result = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            result.add(a);
            result.add(b);
            result.add(c);
        });
        assertEquals(3, result.size());
        assertEquals(Character.valueOf('a'), result.get(0));
        assertEquals(Character.valueOf('b'), result.get(1));
        assertEquals(Character.valueOf('c'), result.get(2));
    }

    // Tuple3 special methods - map
    @Test
    public void testTuple3Map() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        String result = tuple.map((a, b, c) -> String.valueOf(a) + String.valueOf(b) + String.valueOf(c));
        assertEquals("abc", result);
    }

    // Tuple3 special methods - filter
    @Test
    public void testTuple3FilterTrue() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        var result = tuple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple3FilterFalse() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        var result = tuple.filter((a, b, c) -> a > c);
        assertFalse(result.isPresent());
    }

    // arity tests for all tuple sizes
    @Test
    public void testArity() {
        assertEquals(0, CharTuple.create(new char[0]).arity());
        assertEquals(1, CharTuple.of('a').arity());
        assertEquals(2, CharTuple.of('a', 'b').arity());
        assertEquals(3, CharTuple.of('a', 'b', 'c').arity());
        assertEquals(4, CharTuple.of('a', 'b', 'c', 'd').arity());
        assertEquals(5, CharTuple.of('a', 'b', 'c', 'd', 'e').arity());
        assertEquals(6, CharTuple.of('a', 'b', 'c', 'd', 'e', 'f').arity());
        assertEquals(7, CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g').arity());
        assertEquals(8, CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h').arity());
        assertEquals(9, CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i').arity());
    }
}
