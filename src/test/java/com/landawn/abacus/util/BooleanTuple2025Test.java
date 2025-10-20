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
import com.landawn.abacus.util.stream.Stream;

/**
 * Comprehensive test suite for BooleanTuple and its nested classes.
 * Tests all public methods including factory methods, statistical operations,
 * collection conversions, and special methods in Tuple2 and Tuple3.
 */
@Tag("2025")
public class BooleanTuple2025Test extends TestBase {

    // Factory method tests
    @Test
    public void testOf1() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals(true, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf2() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf3() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf4() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(false, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf5() {
        BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        assertEquals(true, tuple._1);
        assertEquals(true, tuple._5);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf6() {
        BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._6);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf7() {
        BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        assertEquals(true, tuple._1);
        assertEquals(true, tuple._7);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf8() {
        BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._8);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf9() {
        BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertEquals(true, tuple._1);
        assertEquals(true, tuple._9);
        assertEquals(9, tuple.arity());
    }

    // Create method tests
    @Test
    public void testCreateEmpty() {
        BooleanTuple<BooleanTuple0> tuple = BooleanTuple.create(new boolean[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreateNull() {
        BooleanTuple<BooleanTuple0> tuple = BooleanTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate1() {
        BooleanTuple1 tuple = BooleanTuple.create(new boolean[] { true });
        assertEquals(true, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testCreate3() {
        BooleanTuple3 tuple = BooleanTuple.create(new boolean[] { true, false, true });
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
    }

    @Test
    public void testCreate9() {
        BooleanTuple9 tuple = BooleanTuple.create(new boolean[] { true, false, true, false, true, false, true, false, true });
        assertEquals(true, tuple._1);
        assertEquals(true, tuple._9);
    }

    @Test
    public void testCreateTooMany() {
        assertThrows(IllegalArgumentException.class, () -> {
            BooleanTuple.create(new boolean[] { true, false, true, false, true, false, true, false, true, false });
        });
    }

    // Reverse tests
    @Test
    public void testReverseTuple0() {
        BooleanTuple<BooleanTuple0> tuple = BooleanTuple.create(new boolean[0]);
        BooleanTuple<BooleanTuple0> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testReverseTuple1() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        BooleanTuple1 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
    }

    @Test
    public void testReverseTuple2() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        BooleanTuple2 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
        assertEquals(true, reversed._2);
    }

    @Test
    public void testReverseTuple3() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        BooleanTuple3 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
        assertEquals(false, reversed._2);
        assertEquals(true, reversed._3);
    }

    // Contains tests
    @Test
    public void testContainsTuple0() {
        BooleanTuple<BooleanTuple0> tuple = BooleanTuple.create(new boolean[0]);
        assertFalse(tuple.contains(true));
    }

    @Test
    public void testContainsTuple1True() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testContainsTuple1False() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertFalse(tuple.contains(false));
    }

    @Test
    public void testContainsTuple3() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertTrue(tuple._1);
        assertFalse(tuple._2);
        assertTrue(tuple._3);
    }

    // toArray tests
    @Test
    public void testToArrayTuple0() {
        BooleanTuple<BooleanTuple0> tuple = BooleanTuple.create(new boolean[0]);
        boolean[] array = tuple.toArray();
        assertEquals(0, array.length);
    }

    @Test
    public void testToArrayTuple1() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        boolean[] array = tuple.toArray();
        assertArrayEquals(new boolean[] { true }, array);
    }

    @Test
    public void testToArrayTuple3() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        boolean[] array = tuple.toArray();
        assertArrayEquals(new boolean[] { true, false, true }, array);
    }

    @Test
    public void testToArrayModificationDoesNotAffectTuple() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        boolean[] array = tuple.toArray();
        array[0] = true;
        assertEquals(true, tuple._1);
    }

    // toList tests
    @Test
    public void testToListTuple0() {
        BooleanTuple<BooleanTuple0> tuple = BooleanTuple.create(new boolean[0]);
        BooleanList list = tuple.toList();
        assertEquals(0, list.size());
    }

    @Test
    public void testToListTuple1() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        BooleanList list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals(true, list.get(0));
    }

    @Test
    public void testToListTuple3() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        BooleanList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(true, list.get(0));
        assertEquals(false, list.get(1));
        assertEquals(true, list.get(2));
    }

    // forEach tests
    @Test
    public void testForEachTuple0() {
        BooleanTuple<BooleanTuple0> tuple = BooleanTuple.create(new boolean[0]);
        List<Boolean> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(0, result.size());
    }

    @Test
    public void testForEachTuple1() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        List<Boolean> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(1, result.size());
        assertEquals(Boolean.valueOf(true), result.get(0));
    }

    @Test
    public void testForEachTuple3() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        List<Boolean> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Boolean.valueOf(true), result.get(0));
        assertEquals(Boolean.valueOf(false), result.get(1));
        assertEquals(Boolean.valueOf(true), result.get(2));
    }

    // stream tests
    @Test
    public void testStreamTuple0() {
        BooleanTuple<BooleanTuple0> tuple = BooleanTuple.create(new boolean[0]);
        Stream<Boolean> stream = tuple.stream();
        assertEquals(0, stream.count());
    }

    @Test
    public void testStreamTuple1() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        Stream<Boolean> stream = tuple.stream();
        assertEquals(1, stream.count());
    }

    @Test
    public void testStreamTuple3() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        Stream<Boolean> stream = tuple.stream();
        assertEquals(3, stream.count());
    }

    // hashCode tests
    @Test
    public void testHashCodeTuple1() {
        BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple1 tuple2 = BooleanTuple.of(true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple2() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testHashCodeTuple3() {
        BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    // equals tests
    @Test
    public void testEqualsSameObject() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals(tuple, tuple);
    }

    @Test
    public void testEqualsNull() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertNotEquals(null, tuple);
    }

    @Test
    public void testEqualsDifferentClass() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertNotEquals("not a tuple", tuple);
    }

    @Test
    public void testEqualsTuple1() {
        BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple1 tuple2 = BooleanTuple.of(true);
        BooleanTuple1 tuple3 = BooleanTuple.of(false);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple2() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple3 = BooleanTuple.of(true, true);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testEqualsTuple3() {
        BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        BooleanTuple3 tuple3 = BooleanTuple.of(true, false, false);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    // toString tests
    @Test
    public void testToStringTuple0() {
        BooleanTuple<BooleanTuple0> tuple = BooleanTuple.create(new boolean[0]);
        assertEquals("[]", tuple.toString());
    }

    @Test
    public void testToStringTuple1() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        String str = tuple.toString();
        assertTrue(str.contains("true"));
    }

    @Test
    public void testToStringTuple3() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        String str = tuple.toString();
        assertTrue(str.contains("true"));
        assertTrue(str.contains("false"));
        assertTrue(str.contains("true"));
    }

    // Tuple2 special methods - accept
    @Test
    public void testTuple2Accept() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        List<Boolean> result = new ArrayList<>();
        tuple.accept((a, b) -> {
            result.add(a);
            result.add(b);
        });
        assertEquals(2, result.size());
        assertEquals(Boolean.valueOf(true), result.get(0));
        assertEquals(Boolean.valueOf(false), result.get(1));
    }

    // Tuple2 special methods - map
    @Test
    public void testTuple2Map() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        boolean result = tuple.map((a, b) -> a && b);
        assertEquals(false, result);
    }

    // Tuple2 special methods - filter
    @Test
    public void testTuple2FilterTrue() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        var result = tuple.filter((a, b) -> a || b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2FilterFalse() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        var result = tuple.filter((a, b) -> a && b);
        assertFalse(result.isPresent());
    }

    // Tuple3 special methods - accept
    @Test
    public void testTuple3Accept() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        List<Boolean> result = new ArrayList<>();
        tuple.accept((a, b, c) -> {
            result.add(a);
            result.add(b);
            result.add(c);
        });
        assertEquals(3, result.size());
        assertEquals(Boolean.valueOf(true), result.get(0));
        assertEquals(Boolean.valueOf(false), result.get(1));
        assertEquals(Boolean.valueOf(true), result.get(2));
    }

    // Tuple3 special methods - map
    @Test
    public void testTuple3Map() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        boolean result = tuple.map((a, b, c) -> a || b || c);
        assertEquals(true, result);
    }

    // Tuple3 special methods - filter
    @Test
    public void testTuple3FilterTrue() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        var result = tuple.filter((a, b, c) -> a || b || c);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple3FilterFalse() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        var result = tuple.filter((a, b, c) -> a && b && c);
        assertFalse(result.isPresent());
    }

    // arity tests for all tuple sizes
    @Test
    public void testArity() {
        assertEquals(0, BooleanTuple.create(new boolean[0]).arity());
        assertEquals(1, BooleanTuple.of(true).arity());
        assertEquals(2, BooleanTuple.of(true, false).arity());
        assertEquals(3, BooleanTuple.of(true, false, true).arity());
        assertEquals(4, BooleanTuple.of(true, false, true, false).arity());
        assertEquals(5, BooleanTuple.of(true, false, true, false, true).arity());
        assertEquals(6, BooleanTuple.of(true, false, true, false, true, false).arity());
        assertEquals(7, BooleanTuple.of(true, false, true, false, true, false, true).arity());
        assertEquals(8, BooleanTuple.of(true, false, true, false, true, false, true, false).arity());
        assertEquals(9, BooleanTuple.of(true, false, true, false, true, false, true, false, true).arity());
    }

    // Comprehensive tests for Tuple4 through Tuple9
    @Test
    public void testTuple4Operations() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);

        // Test reverse
        BooleanTuple4 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
        assertEquals(true, reversed._2);
        assertEquals(false, reversed._3);
        assertEquals(true, reversed._4);

        // Test contains
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));

        // Test toArray
        assertArrayEquals(new boolean[] { true, false, true, false }, tuple.toArray());

        // Test hashCode and equals
        BooleanTuple4 tuple2 = BooleanTuple.of(true, false, true, false);
        BooleanTuple4 tuple3 = BooleanTuple.of(true, false, true, true);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        String str = tuple.toString();
        assertTrue(str.contains("true"));
        assertTrue(str.contains("false"));
    }

    @Test
    public void testTuple5Operations() {
        BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);

        // Test reverse
        BooleanTuple5 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
        assertEquals(false, reversed._2);
        assertEquals(true, reversed._3);
        assertEquals(false, reversed._4);
        assertEquals(true, reversed._5);

        // Test contains
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));

        // Test toArray
        assertArrayEquals(new boolean[] { true, false, true, false, true }, tuple.toArray());

        // Test equals
        BooleanTuple5 tuple2 = BooleanTuple.of(true, false, true, false, true);
        BooleanTuple5 tuple3 = BooleanTuple.of(true, false, true, false, false);
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);

        // Test toString
        String str = tuple.toString();
        assertTrue(str.contains("true"));
        assertTrue(str.contains("false"));
    }

    @Test
    public void testTuple6Operations() {
        BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);

        // Test reverse
        BooleanTuple6 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
        assertEquals(true, reversed._2);
        assertEquals(false, reversed._3);
        assertEquals(true, reversed._4);
        assertEquals(false, reversed._5);
        assertEquals(true, reversed._6);

        // Test contains
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));

        // Test toArray
        assertArrayEquals(new boolean[] { true, false, true, false, true, false }, tuple.toArray());

        // Test hashCode and equals
        BooleanTuple6 tuple2 = BooleanTuple.of(true, false, true, false, true, false);
        BooleanTuple6 tuple3 = BooleanTuple.of(true, false, true, false, true, true);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);
    }

    @Test
    public void testTuple7Operations() {
        BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);

        // Test reverse
        BooleanTuple7 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
        assertEquals(false, reversed._2);
        assertEquals(true, reversed._3);
        assertEquals(false, reversed._4);
        assertEquals(true, reversed._5);
        assertEquals(false, reversed._6);
        assertEquals(true, reversed._7);

        // Test contains
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));

        // Test toArray
        assertArrayEquals(new boolean[] { true, false, true, false, true, false, true }, tuple.toArray());

        // Test hashCode and equals
        BooleanTuple7 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true);
        BooleanTuple7 tuple3 = BooleanTuple.of(true, false, true, false, true, false, false);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);
    }

    @Test
    public void testTuple8Operations() {
        BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);

        // Test reverse
        BooleanTuple8 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
        assertEquals(true, reversed._2);
        assertEquals(false, reversed._3);
        assertEquals(true, reversed._4);
        assertEquals(false, reversed._5);
        assertEquals(true, reversed._6);
        assertEquals(false, reversed._7);
        assertEquals(true, reversed._8);

        // Test contains
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));

        // Test toArray
        assertArrayEquals(new boolean[] { true, false, true, false, true, false, true, false }, tuple.toArray());

        // Test hashCode and equals
        BooleanTuple8 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false);
        BooleanTuple8 tuple3 = BooleanTuple.of(true, false, true, false, true, false, true, true);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);
    }

    @Test
    public void testTuple9Operations() {
        BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);

        // Test reverse
        BooleanTuple9 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
        assertEquals(false, reversed._2);
        assertEquals(true, reversed._3);
        assertEquals(false, reversed._4);
        assertEquals(true, reversed._5);
        assertEquals(false, reversed._6);
        assertEquals(true, reversed._7);
        assertEquals(false, reversed._8);
        assertEquals(true, reversed._9);

        // Test contains
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));

        // Test toArray
        assertArrayEquals(new boolean[] { true, false, true, false, true, false, true, false, true }, tuple.toArray());

        // Test hashCode and equals
        BooleanTuple9 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        BooleanTuple9 tuple3 = BooleanTuple.of(true, false, true, false, true, false, true, false, false);
        assertEquals(tuple.hashCode(), tuple2.hashCode());
        assertEquals(tuple, tuple2);
        assertNotEquals(tuple, tuple3);
    }

    // Test create methods for sizes 2, 4-8
    @Test
    public void testCreate2Through8() {
        BooleanTuple2 tuple2 = BooleanTuple.create(new boolean[] { true, false });
        assertEquals(true, tuple2._1);
        assertEquals(false, tuple2._2);

        BooleanTuple4 tuple4 = BooleanTuple.create(new boolean[] { true, false, true, false });
        assertEquals(true, tuple4._1);
        assertEquals(false, tuple4._4);

        BooleanTuple5 tuple5 = BooleanTuple.create(new boolean[] { true, false, true, false, true });
        assertEquals(true, tuple5._1);
        assertEquals(true, tuple5._5);

        BooleanTuple6 tuple6 = BooleanTuple.create(new boolean[] { true, false, true, false, true, false });
        assertEquals(true, tuple6._1);
        assertEquals(false, tuple6._6);

        BooleanTuple7 tuple7 = BooleanTuple.create(new boolean[] { true, false, true, false, true, false, true });
        assertEquals(true, tuple7._1);
        assertEquals(true, tuple7._7);

        BooleanTuple8 tuple8 = BooleanTuple.create(new boolean[] { true, false, true, false, true, false, true, false });
        assertEquals(true, tuple8._1);
        assertEquals(false, tuple8._8);
    }

    // Test toList for larger tuples
    @Test
    public void testToListTuple4Through9() {
        BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
        BooleanList list4 = tuple4.toList();
        assertEquals(4, list4.size());
        assertEquals(false, list4.get(3));

        BooleanTuple9 tuple9 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        BooleanList list9 = tuple9.toList();
        assertEquals(9, list9.size());
        assertEquals(true, list9.get(8));
    }

    // Test forEach for larger tuples
    @Test
    public void testForEachTuple4() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        List<Boolean> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(4, result.size());
        assertEquals(Boolean.valueOf(false), result.get(3));
    }

    // Test forEach override for Tuple2
    @Test
    public void testForEachTuple2Override() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        List<Boolean> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(2, result.size());
        assertEquals(Boolean.valueOf(true), result.get(0));
        assertEquals(Boolean.valueOf(false), result.get(1));
    }

    // Test forEach override for Tuple3
    @Test
    public void testForEachTuple3Override() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        List<Boolean> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals(Boolean.valueOf(true), result.get(2));
    }

    // Test stream for larger tuples
    @Test
    public void testStreamTuple4Through9() {
        BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
        assertEquals(4, tuple4.stream().count());

        BooleanTuple9 tuple9 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertEquals(9, tuple9.stream().count());
    }

    // ==================== BooleanTuple Nested Class Tests ====================

    // ============ BooleanTuple1 Nested Class Tests ============

    @Test
    public void testBooleanTuple1_arity() {
        BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testBooleanTuple1_reverse() {
        BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
        BooleanTuple.BooleanTuple1 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._1);
        assertEquals(tuple._1, reversed._1);
    }

    @Test
    public void testBooleanTuple1_contains() {
        BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testBooleanTuple1_hashCode() {
        BooleanTuple.BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple.BooleanTuple1 tuple2 = BooleanTuple.of(true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testBooleanTuple1_equals() {
        BooleanTuple.BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple.BooleanTuple1 tuple2 = BooleanTuple.of(true);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testBooleanTuple1_toString() {
        BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testBooleanTuple1_forEach() {
        BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(1, count.size());
    }

    // ============ BooleanTuple2 Nested Class Tests ============

    @Test
    public void testBooleanTuple2_arity() {
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testBooleanTuple2_reverse() {
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        BooleanTuple.BooleanTuple2 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._2);
        assertEquals(tuple._2, reversed._1);
    }

    @Test
    public void testBooleanTuple2_contains() {
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testBooleanTuple2_hashCode() {
        BooleanTuple.BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple.BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testBooleanTuple2_equals() {
        BooleanTuple.BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple.BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testBooleanTuple2_toString() {
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testBooleanTuple2_forEach() {
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(2, count.size());
    }

    @Test
    public void testBooleanTuple2_accept_biConsumer() {{
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b) -> count.add(1));
        assertEquals(1, count.size());
    }}

    @Test
    public void testBooleanTuple2_map_biFunction() {{
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        String result = tuple.map((a, b) -> "test");
        assertNotNull(result);
    }}

    @Test
    public void testBooleanTuple2_filter_biPredicate() {{
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertTrue(tuple.filter((a, b) -> true).isPresent());
        assertFalse(tuple.filter((a, b) -> false).isPresent());
    }}

    // ============ BooleanTuple3 Nested Class Tests ============

    @Test
    public void testBooleanTuple3_arity() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testBooleanTuple3_reverse() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        BooleanTuple.BooleanTuple3 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._3);
        assertEquals(tuple._3, reversed._1);
    }

    @Test
    public void testBooleanTuple3_contains() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testBooleanTuple3_hashCode() {
        BooleanTuple.BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple.BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testBooleanTuple3_equals() {
        BooleanTuple.BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple.BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testBooleanTuple3_toString() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testBooleanTuple3_forEach() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(3, count.size());
    }

    @Test
    public void testBooleanTuple3_accept_triConsumer() {{
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        List<Integer> count = new ArrayList<>();
        tuple.accept((a, b, c) -> count.add(1));
        assertEquals(1, count.size());
    }}

    @Test
    public void testBooleanTuple3_map_triFunction() {{
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        String result = tuple.map((a, b, c) -> "test");
        assertNotNull(result);
    }}

    @Test
    public void testBooleanTuple3_filter_triPredicate() {{
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertTrue(tuple.filter((a, b, c) -> true).isPresent());
        assertFalse(tuple.filter((a, b, c) -> false).isPresent());
    }}

    // ============ BooleanTuple4 Nested Class Tests ============

    @Test
    public void testBooleanTuple4_arity() {
        BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testBooleanTuple4_reverse() {
        BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        BooleanTuple.BooleanTuple4 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._4);
        assertEquals(tuple._4, reversed._1);
    }

    @Test
    public void testBooleanTuple4_contains() {
        BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testBooleanTuple4_hashCode() {
        BooleanTuple.BooleanTuple4 tuple1 = BooleanTuple.of(true, false, true, false);
        BooleanTuple.BooleanTuple4 tuple2 = BooleanTuple.of(true, false, true, false);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testBooleanTuple4_equals() {
        BooleanTuple.BooleanTuple4 tuple1 = BooleanTuple.of(true, false, true, false);
        BooleanTuple.BooleanTuple4 tuple2 = BooleanTuple.of(true, false, true, false);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testBooleanTuple4_toString() {
        BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testBooleanTuple4_forEach() {
        BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(4, count.size());
    }

    // ============ BooleanTuple5 Nested Class Tests ============

    @Test
    public void testBooleanTuple5_arity() {
        BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testBooleanTuple5_reverse() {
        BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        BooleanTuple.BooleanTuple5 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._5);
        assertEquals(tuple._5, reversed._1);
    }

    @Test
    public void testBooleanTuple5_contains() {
        BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testBooleanTuple5_hashCode() {
        BooleanTuple.BooleanTuple5 tuple1 = BooleanTuple.of(true, false, true, false, true);
        BooleanTuple.BooleanTuple5 tuple2 = BooleanTuple.of(true, false, true, false, true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testBooleanTuple5_equals() {
        BooleanTuple.BooleanTuple5 tuple1 = BooleanTuple.of(true, false, true, false, true);
        BooleanTuple.BooleanTuple5 tuple2 = BooleanTuple.of(true, false, true, false, true);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testBooleanTuple5_toString() {
        BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testBooleanTuple5_forEach() {
        BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(5, count.size());
    }

    // ============ BooleanTuple6 Nested Class Tests ============

    @Test
    public void testBooleanTuple6_arity() {
        BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testBooleanTuple6_reverse() {
        BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        BooleanTuple.BooleanTuple6 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._6);
        assertEquals(tuple._6, reversed._1);
    }

    @Test
    public void testBooleanTuple6_contains() {
        BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testBooleanTuple6_hashCode() {
        BooleanTuple.BooleanTuple6 tuple1 = BooleanTuple.of(true, false, true, false, true, false);
        BooleanTuple.BooleanTuple6 tuple2 = BooleanTuple.of(true, false, true, false, true, false);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testBooleanTuple6_equals() {
        BooleanTuple.BooleanTuple6 tuple1 = BooleanTuple.of(true, false, true, false, true, false);
        BooleanTuple.BooleanTuple6 tuple2 = BooleanTuple.of(true, false, true, false, true, false);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testBooleanTuple6_toString() {
        BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testBooleanTuple6_forEach() {
        BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(6, count.size());
    }

    // ============ BooleanTuple7 Nested Class Tests ============

    @Test
    public void testBooleanTuple7_arity() {
        BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testBooleanTuple7_reverse() {
        BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        BooleanTuple.BooleanTuple7 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._7);
        assertEquals(tuple._7, reversed._1);
    }

    @Test
    public void testBooleanTuple7_contains() {
        BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testBooleanTuple7_hashCode() {
        BooleanTuple.BooleanTuple7 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true);
        BooleanTuple.BooleanTuple7 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testBooleanTuple7_equals() {
        BooleanTuple.BooleanTuple7 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true);
        BooleanTuple.BooleanTuple7 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testBooleanTuple7_toString() {
        BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testBooleanTuple7_forEach() {
        BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(7, count.size());
    }

    // ============ BooleanTuple8 Nested Class Tests ============

    @Test
    public void testBooleanTuple8_arity() {
        BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testBooleanTuple8_reverse() {
        BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        BooleanTuple.BooleanTuple8 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._8);
        assertEquals(tuple._8, reversed._1);
    }

    @Test
    public void testBooleanTuple8_contains() {
        BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testBooleanTuple8_hashCode() {
        BooleanTuple.BooleanTuple8 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true, false);
        BooleanTuple.BooleanTuple8 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testBooleanTuple8_equals() {
        BooleanTuple.BooleanTuple8 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true, false);
        BooleanTuple.BooleanTuple8 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testBooleanTuple8_toString() {
        BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testBooleanTuple8_forEach() {
        BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(8, count.size());
    }

    // ============ BooleanTuple9 Nested Class Tests ============

    @Test
    public void testBooleanTuple9_arity() {
        BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testBooleanTuple9_reverse() {
        BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        BooleanTuple.BooleanTuple9 reversed = tuple.reverse();
        assertEquals(tuple._1, reversed._9);
        assertEquals(tuple._9, reversed._1);
    }

    @Test
    public void testBooleanTuple9_contains() {
        BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testBooleanTuple9_hashCode() {
        BooleanTuple.BooleanTuple9 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        BooleanTuple.BooleanTuple9 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testBooleanTuple9_equals() {
        BooleanTuple.BooleanTuple9 tuple1 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        BooleanTuple.BooleanTuple9 tuple2 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void testBooleanTuple9_toString() {
        BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertNotNull(tuple.toString());
    }

    @Test
    public void testBooleanTuple9_forEach() {
        BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        List<Integer> count = new ArrayList<>();
        tuple.forEach(v -> count.add(1));
        assertEquals(9, count.size());
    }

}
