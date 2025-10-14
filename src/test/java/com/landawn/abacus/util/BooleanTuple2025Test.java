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
}
