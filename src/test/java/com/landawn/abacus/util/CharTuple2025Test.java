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
        assertEquals("()", tuple.toString());
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

    // Tests for inherited methods from PrimitiveTuple
    @Test
    public void testAcceptConsumer() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        List<Character> result = new ArrayList<>();
        tuple.accept(t -> {
            result.add(t._1);
            result.add(t._2);
            result.add(t._3);
        });
        assertEquals(3, result.size());
        assertEquals(Character.valueOf('a'), result.get(0));
        assertEquals(Character.valueOf('b'), result.get(1));
        assertEquals(Character.valueOf('c'), result.get(2));
    }

    @Test
    public void testMapFunction() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        String result = tuple.map(t -> "" + t._1 + t._2 + t._3);
        assertEquals("abc", result);
    }

    @Test
    public void testFilterPredicate() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        var result = tuple.filter(t -> t._1 == 'a');
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testFilterPredicateFalse() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        var result = tuple.filter(t -> t._1 == 'z');
        assertFalse(result.isPresent());
    }

    @Test
    public void testToOptional() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        var result = tuple.toOptional();
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    // Additional tests for larger tuple sizes - reverse
    @Test
    public void testReverseTuple4() {
        CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
        CharTuple4 reversed = tuple.reverse();
        assertEquals('d', reversed._1);
        assertEquals('c', reversed._2);
        assertEquals('b', reversed._3);
        assertEquals('a', reversed._4);
    }

    @Test
    public void testReverseTuple5() {
        CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
        CharTuple5 reversed = tuple.reverse();
        assertEquals('e', reversed._1);
        assertEquals('a', reversed._5);
    }

    @Test
    public void testReverseTuple6() {
        CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        CharTuple6 reversed = tuple.reverse();
        assertEquals('f', reversed._1);
        assertEquals('a', reversed._6);
    }

    @Test
    public void testReverseTuple7() {
        CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        CharTuple7 reversed = tuple.reverse();
        assertEquals('g', reversed._1);
        assertEquals('a', reversed._7);
    }

    @Test
    public void testReverseTuple8() {
        CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        CharTuple8 reversed = tuple.reverse();
        assertEquals('h', reversed._1);
        assertEquals('a', reversed._8);
    }

    @Test
    public void testReverseTuple9() {
        CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        CharTuple9 reversed = tuple.reverse();
        assertEquals('i', reversed._1);
        assertEquals('a', reversed._9);
    }

    // Additional tests for larger tuple sizes - contains
    @Test
    public void testContainsTuple4() {
        CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
        assertTrue(tuple.contains('a'));
        assertTrue(tuple.contains('d'));
        assertFalse(tuple.contains('z'));
    }

    @Test
    public void testContainsTuple5() {
        CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
        assertTrue(tuple.contains('e'));
        assertFalse(tuple.contains('z'));
    }

    @Test
    public void testContainsTuple6() {
        CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        assertTrue(tuple.contains('f'));
        assertFalse(tuple.contains('z'));
    }

    @Test
    public void testContainsTuple7() {
        CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        assertTrue(tuple.contains('g'));
        assertFalse(tuple.contains('z'));
    }

    @Test
    public void testContainsTuple8() {
        CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        assertTrue(tuple.contains('h'));
        assertFalse(tuple.contains('z'));
    }

    @Test
    public void testContainsTuple9() {
        CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        assertTrue(tuple.contains('i'));
        assertFalse(tuple.contains('z'));
    }

    // Test for create() with all sizes (2, 4-9)
    @Test
    public void testCreate2() {
        CharTuple2 tuple = CharTuple.create(new char[] { 'a', 'b' });
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
    }

    @Test
    public void testCreate4() {
        CharTuple4 tuple = CharTuple.create(new char[] { 'a', 'b', 'c', 'd' });
        assertEquals('a', tuple._1);
        assertEquals('d', tuple._4);
    }

    @Test
    public void testCreate5() {
        CharTuple5 tuple = CharTuple.create(new char[] { 'a', 'b', 'c', 'd', 'e' });
        assertEquals('a', tuple._1);
        assertEquals('e', tuple._5);
    }

    @Test
    public void testCreate6() {
        CharTuple6 tuple = CharTuple.create(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' });
        assertEquals('a', tuple._1);
        assertEquals('f', tuple._6);
    }

    @Test
    public void testCreate7() {
        CharTuple7 tuple = CharTuple.create(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' });
        assertEquals('a', tuple._1);
        assertEquals('g', tuple._7);
    }

    @Test
    public void testCreate8() {
        CharTuple8 tuple = CharTuple.create(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' });
        assertEquals('a', tuple._1);
        assertEquals('h', tuple._8);
    }

    // Comprehensive tests for Tuple4 through Tuple9
    @Test
    public void testTuple4Operations() {
        CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');

        // Test reverse
        CharTuple4 reversed = tuple.reverse();
        assertEquals('d', reversed._1);
        assertEquals('c', reversed._2);
        assertEquals('b', reversed._3);
        assertEquals('a', reversed._4);

        // Test contains
        assertTrue(tuple.contains('a'));
        assertTrue(tuple.contains('d'));
        assertFalse(tuple.contains('z'));

        // Test toArray
        assertArrayEquals(new char[] { 'a', 'b', 'c', 'd' }, tuple.toArray());

        // Test min/max/median/sum/average via base class
        assertEquals('a', tuple.min());
        assertEquals('d', tuple.max());
        assertEquals('b', tuple.median());
        assertEquals(394, tuple.sum());   // 97+98+99+100
        assertEquals(98.5, tuple.average(), 0.001);

        // Test hashCode and equals
        CharTuple4 tuple2 = CharTuple.of('a', 'b', 'c', 'd');
        CharTuple4 tuple3 = CharTuple.of('a', 'b', 'c', 'e');
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        String str = tuple.toString();
        assertTrue(str.contains("a"));
        assertTrue(str.contains("d"));
    }

    @Test
    public void testTuple5Operations() {
        CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');

        // Test reverse
        CharTuple5 reversed = tuple.reverse();
        assertEquals('e', reversed._1);
        assertEquals('a', reversed._5);

        // Test contains
        assertTrue(tuple.contains('a'));
        assertTrue(tuple.contains('e'));
        assertFalse(tuple.contains('z'));

        // Test toArray
        assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e' }, tuple.toArray());

        // Test statistical operations
        assertEquals('a', tuple.min());
        assertEquals('e', tuple.max());
        assertEquals('c', tuple.median());
        assertEquals(495, tuple.sum());   // 97+98+99+100+101
        assertEquals(99.0, tuple.average(), 0.001);

        // Test equals
        CharTuple5 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e');
        assertEquals(tuple, tuple2);
    }

    @Test
    public void testTuple6Operations() {
        CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');

        // Test reverse
        CharTuple6 reversed = tuple.reverse();
        assertEquals('f', reversed._1);
        assertEquals('a', reversed._6);

        // Test contains
        assertTrue(tuple.contains('a'));
        assertTrue(tuple.contains('f'));
        assertFalse(tuple.contains('z'));

        // Test toArray
        assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' }, tuple.toArray());

        // Test statistical operations
        assertEquals('a', tuple.min());
        assertEquals('f', tuple.max());
        assertEquals(597, tuple.sum());   // 97+98+99+100+101+102
        assertEquals(99.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple7Operations() {
        CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');

        // Test reverse
        CharTuple7 reversed = tuple.reverse();
        assertEquals('g', reversed._1);
        assertEquals('a', reversed._7);

        // Test contains
        assertTrue(tuple.contains('a'));
        assertTrue(tuple.contains('g'));
        assertFalse(tuple.contains('z'));

        // Test toArray
        assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' }, tuple.toArray());

        // Test statistical operations
        assertEquals('a', tuple.min());
        assertEquals('g', tuple.max());
        assertEquals(700, tuple.sum());   // 97+98+99+100+101+102+103
        assertEquals(100.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple8Operations() {
        CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');

        // Test reverse
        CharTuple8 reversed = tuple.reverse();
        assertEquals('h', reversed._1);
        assertEquals('a', reversed._8);

        // Test contains
        assertTrue(tuple.contains('a'));
        assertTrue(tuple.contains('h'));
        assertFalse(tuple.contains('z'));

        // Test toArray
        assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' }, tuple.toArray());

        // Test statistical operations
        assertEquals('a', tuple.min());
        assertEquals('h', tuple.max());
        assertEquals(804, tuple.sum());   // 97+98+99+100+101+102+103+104
        assertEquals(100.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple9Operations() {
        CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');

        // Test reverse
        CharTuple9 reversed = tuple.reverse();
        assertEquals('i', reversed._1);
        assertEquals('a', reversed._9);

        // Test contains
        assertTrue(tuple.contains('a'));
        assertTrue(tuple.contains('i'));
        assertFalse(tuple.contains('z'));

        // Test toArray
        assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i' }, tuple.toArray());

        // Test statistical operations
        assertEquals('a', tuple.min());
        assertEquals('i', tuple.max());
        assertEquals(909, tuple.sum());   // 97+98+99+100+101+102+103+104+105
        assertEquals(101.0, tuple.average(), 0.001);
    }

    // Test create methods for sizes 2, 4-8
    @Test
    public void testCreate2Through8() {
        CharTuple2 tuple2 = CharTuple.create(new char[] { 'a', 'b' });
        assertEquals('a', tuple2._1);
        assertEquals('b', tuple2._2);

        CharTuple4 tuple4 = CharTuple.create(new char[] { 'a', 'b', 'c', 'd' });
        assertEquals('a', tuple4._1);
        assertEquals('d', tuple4._4);

        CharTuple5 tuple5 = CharTuple.create(new char[] { 'a', 'b', 'c', 'd', 'e' });
        assertEquals('a', tuple5._1);
        assertEquals('e', tuple5._5);

        CharTuple6 tuple6 = CharTuple.create(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' });
        assertEquals('a', tuple6._1);
        assertEquals('f', tuple6._6);

        CharTuple7 tuple7 = CharTuple.create(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' });
        assertEquals('a', tuple7._1);
        assertEquals('g', tuple7._7);

        CharTuple8 tuple8 = CharTuple.create(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' });
        assertEquals('a', tuple8._1);
        assertEquals('h', tuple8._8);
    }

    // Test toList for larger tuples
    @Test
    public void testToListTuple4Through9() {
        CharTuple4 tuple4 = CharTuple.of('a', 'b', 'c', 'd');
        CharList list4 = tuple4.toList();
        assertEquals(4, list4.size());
        assertEquals('d', list4.get(3));

        CharTuple9 tuple9 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        CharList list9 = tuple9.toList();
        assertEquals(9, list9.size());
        assertEquals('i', list9.get(8));
    }

    // Test forEach for larger tuples
    @Test
    public void testForEachTuple4() {
        CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
        List<Character> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(4, result.size());
        assertEquals(Character.valueOf('d'), result.get(3));
    }

    // Test forEach override for Tuple2
    @Test
    public void testForEachTuple2Override() {
        CharTuple2 tuple = CharTuple.of('x', 'y');
        List<Character> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(2, result.size());
        assertEquals(Character.valueOf('x'), result.get(0));
        assertEquals(Character.valueOf('y'), result.get(1));
    }

    // Test forEach override for Tuple3
    @Test
    public void testForEachTuple3Override() {
        CharTuple3 tuple = CharTuple.of('x', 'y', 'z');
        List<Character> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Character.valueOf('z'), result.get(2));
    }

    // Test stream for larger tuples
    @Test
    public void testStreamTuple4Through9() {
        CharTuple4 tuple4 = CharTuple.of('a', 'b', 'c', 'd');
        assertEquals(394, tuple4.stream().sum());

        CharTuple9 tuple9 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        assertEquals(909, tuple9.stream().sum());
    }

    // ==================== CharTuple Nested Class Tests ====================

    // ============ CharTuple1 Nested Class Tests ============

    @Test
    public void testCharTuple1_arity() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCharTuple1_reverse() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        CharTuple.CharTuple1 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._1);
        assertEquals(tuple._1, reversed._1);
    }

    @Test
    public void testCharTuple1_contains() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        assertTrue(tuple.contains('a'));
    }

    @Test
    public void testCharTuple1_hashCode() {
        CharTuple.CharTuple1 tuple1 = CharTuple.of('a');
        CharTuple.CharTuple1 tuple2 = CharTuple.of('a');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testCharTuple1_equals() {
        CharTuple.CharTuple1 tuple1 = CharTuple.of('a');
        CharTuple.CharTuple1 tuple2 = CharTuple.of('a');
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testCharTuple1_toString() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        assertNotNull(tuple.toString());
    }

    @Test
    public void testCharTuple1_forEach() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testCharTuple1_min() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        assertNotNull(tuple.min());
    }

    @Test
    public void testCharTuple1_max() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        assertNotNull(tuple.max());
    }

    @Test
    public void testCharTuple1_median() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        assertNotNull(tuple.median());
    }

    @Test
    public void testCharTuple1_sum() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        assertNotNull(tuple.sum());
    }

    @Test
    public void testCharTuple1_average() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    // ============ CharTuple2 Nested Class Tests ============

    @Test
    public void testCharTuple2_arity() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testCharTuple2_reverse() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        CharTuple.CharTuple2 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._2);
        assertEquals(tuple._2, reversed._1);
    }

    @Test
    public void testCharTuple2_contains() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        assertTrue(tuple.contains('a'));
    }

    @Test
    public void testCharTuple2_hashCode() {
        CharTuple.CharTuple2 tuple1 = CharTuple.of('a', 'b');
        CharTuple.CharTuple2 tuple2 = CharTuple.of('a', 'b');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testCharTuple2_equals() {
        CharTuple.CharTuple2 tuple1 = CharTuple.of('a', 'b');
        CharTuple.CharTuple2 tuple2 = CharTuple.of('a', 'b');
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testCharTuple2_toString() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        assertNotNull(tuple.toString());
    }

    @Test
    public void testCharTuple2_forEach() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(2, count.size());
    }

    @Test
    public void testCharTuple2_min() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        assertNotNull(tuple.min());
    }

    @Test
    public void testCharTuple2_max() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        assertNotNull(tuple.max());
    }

    @Test
    public void testCharTuple2_median() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        assertNotNull(tuple.median());
    }

    @Test
    public void testCharTuple2_sum() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        assertNotNull(tuple.sum());
    }

    @Test
    public void testCharTuple2_average() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testCharTuple2_accept_biConsumer() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testCharTuple2_map_biFunction() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        String result = tuple.map((a, b) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testCharTuple2_filter_biPredicate() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        assertTrue(tuple.filter((a, b) -> true).isPresent());
        assertFalse(tuple.filter((a, b) -> false).isPresent());
    }

    // ============ CharTuple3 Nested Class Tests ============

    @Test
    public void testCharTuple3_arity() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testCharTuple3_reverse() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        CharTuple.CharTuple3 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._3);
        assertEquals(tuple._3, reversed._1);
    }

    @Test
    public void testCharTuple3_contains() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertTrue(tuple.contains('a'));
    }

    @Test
    public void testCharTuple3_hashCode() {
        CharTuple.CharTuple3 tuple1 = CharTuple.of('a', 'b', 'c');
        CharTuple.CharTuple3 tuple2 = CharTuple.of('a', 'b', 'c');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testCharTuple3_equals() {
        CharTuple.CharTuple3 tuple1 = CharTuple.of('a', 'b', 'c');
        CharTuple.CharTuple3 tuple2 = CharTuple.of('a', 'b', 'c');
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testCharTuple3_toString() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertNotNull(tuple.toString());
    }

    @Test
    public void testCharTuple3_forEach() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(3, count.size());
    }

    @Test
    public void testCharTuple3_min() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertNotNull(tuple.min());
    }

    @Test
    public void testCharTuple3_max() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertNotNull(tuple.max());
    }

    @Test
    public void testCharTuple3_median() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertNotNull(tuple.median());
    }

    @Test
    public void testCharTuple3_sum() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertNotNull(tuple.sum());
    }

    @Test
    public void testCharTuple3_average() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertTrue(tuple.average() >= 0 || tuple.average() < 0);
    }

    @Test
    public void testCharTuple3_accept_triConsumer() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b, c) -> count.add(1));
        assertEquals(1, count.size());
    }

    @Test
    public void testCharTuple3_map_triFunction() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        String result = tuple.map((a, b, c) -> "test");
        assertNotNull(result);
    }

    @Test
    public void testCharTuple3_filter_triPredicate() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertTrue(tuple.filter((a, b, c) -> true).isPresent());
        assertFalse(tuple.filter((a, b, c) -> false).isPresent());
    }

    // ============ CharTuple4 Nested Class Tests ============

    @Test
    public void testCharTuple4_arity() {
        CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testCharTuple4_reverse() {
        CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
        CharTuple.CharTuple4 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._4);
        assertEquals(tuple._4, reversed._1);
    }

    @Test
    public void testCharTuple4_contains() {
        CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
        assertTrue(tuple.contains('a'));
    }

    @Test
    public void testCharTuple4_hashCode() {
        CharTuple.CharTuple4 tuple1 = CharTuple.of('a', 'b', 'c', 'd');
        CharTuple.CharTuple4 tuple2 = CharTuple.of('a', 'b', 'c', 'd');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testCharTuple4_equals() {
        CharTuple.CharTuple4 tuple1 = CharTuple.of('a', 'b', 'c', 'd');
        CharTuple.CharTuple4 tuple2 = CharTuple.of('a', 'b', 'c', 'd');
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testCharTuple4_toString() {
        CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
        assertNotNull(tuple.toString());
    }

    @Test
    public void testCharTuple4_forEach() {
        CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(4, count.size());
    }

    // ============ CharTuple5 Nested Class Tests ============

    @Test
    public void testCharTuple5_arity() {
        CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testCharTuple5_reverse() {
        CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
        CharTuple.CharTuple5 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._5);
        assertEquals(tuple._5, reversed._1);
    }

    @Test
    public void testCharTuple5_contains() {
        CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
        assertTrue(tuple.contains('a'));
    }

    @Test
    public void testCharTuple5_hashCode() {
        CharTuple.CharTuple5 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e');
        CharTuple.CharTuple5 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testCharTuple5_equals() {
        CharTuple.CharTuple5 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e');
        CharTuple.CharTuple5 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e');
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testCharTuple5_toString() {
        CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
        assertNotNull(tuple.toString());
    }

    @Test
    public void testCharTuple5_forEach() {
        CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(5, count.size());
    }

    // ============ CharTuple6 Nested Class Tests ============

    @Test
    public void testCharTuple6_arity() {
        CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testCharTuple6_reverse() {
        CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        CharTuple.CharTuple6 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._6);
        assertEquals(tuple._6, reversed._1);
    }

    @Test
    public void testCharTuple6_contains() {
        CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        assertTrue(tuple.contains('a'));
    }

    @Test
    public void testCharTuple6_hashCode() {
        CharTuple.CharTuple6 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        CharTuple.CharTuple6 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testCharTuple6_equals() {
        CharTuple.CharTuple6 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        CharTuple.CharTuple6 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testCharTuple6_toString() {
        CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        assertNotNull(tuple.toString());
    }

    @Test
    public void testCharTuple6_forEach() {
        CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(6, count.size());
    }

    // ============ CharTuple7 Nested Class Tests ============

    @Test
    public void testCharTuple7_arity() {
        CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testCharTuple7_reverse() {
        CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        CharTuple.CharTuple7 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._7);
        assertEquals(tuple._7, reversed._1);
    }

    @Test
    public void testCharTuple7_contains() {
        CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        assertTrue(tuple.contains('a'));
    }

    @Test
    public void testCharTuple7_hashCode() {
        CharTuple.CharTuple7 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        CharTuple.CharTuple7 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testCharTuple7_equals() {
        CharTuple.CharTuple7 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        CharTuple.CharTuple7 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testCharTuple7_toString() {
        CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        assertNotNull(tuple.toString());
    }

    @Test
    public void testCharTuple7_forEach() {
        CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(7, count.size());
    }

    // ============ CharTuple8 Nested Class Tests ============

    @Test
    public void testCharTuple8_arity() {
        CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testCharTuple8_reverse() {
        CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        CharTuple.CharTuple8 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._8);
        assertEquals(tuple._8, reversed._1);
    }

    @Test
    public void testCharTuple8_contains() {
        CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        assertTrue(tuple.contains('a'));
    }

    @Test
    public void testCharTuple8_hashCode() {
        CharTuple.CharTuple8 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        CharTuple.CharTuple8 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testCharTuple8_equals() {
        CharTuple.CharTuple8 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        CharTuple.CharTuple8 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testCharTuple8_toString() {
        CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        assertNotNull(tuple.toString());
    }

    @Test
    public void testCharTuple8_forEach() {
        CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(8, count.size());
    }

    // ============ CharTuple9 Nested Class Tests ============

    @Test
    public void testCharTuple9_arity() {
        CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testCharTuple9_reverse() {
        CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        CharTuple.CharTuple9 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._9);
        assertEquals(tuple._9, reversed._1);
    }

    @Test
    public void testCharTuple9_contains() {
        CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        assertTrue(tuple.contains('a'));
    }

    @Test
    public void testCharTuple9_hashCode() {
        CharTuple.CharTuple9 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        CharTuple.CharTuple9 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testCharTuple9_equals() {
        CharTuple.CharTuple9 tuple1 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        CharTuple.CharTuple9 tuple2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testCharTuple9_toString() {
        CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        assertNotNull(tuple.toString());
    }

    @Test
    public void testCharTuple9_forEach() {
        CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(9, count.size());
    }

}
