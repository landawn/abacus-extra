#!/usr/bin/env python3
"""
Script to generate comprehensive *Tuple2025Test.java files based on IntTuple2025Test template.
"""

TEMPLATE = '''/*
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

import com.landawn.abacus.util.{TYPE}Tuple.{TYPE}Tuple0;
import com.landawn.abacus.util.{TYPE}Tuple.{TYPE}Tuple1;
import com.landawn.abacus.util.{TYPE}Tuple.{TYPE}Tuple2;
import com.landawn.abacus.util.{TYPE}Tuple.{TYPE}Tuple3;
import com.landawn.abacus.util.{TYPE}Tuple.{TYPE}Tuple4;
import com.landawn.abacus.util.{TYPE}Tuple.{TYPE}Tuple5;
import com.landawn.abacus.util.{TYPE}Tuple.{TYPE}Tuple6;
import com.landawn.abacus.util.{TYPE}Tuple.{TYPE}Tuple7;
import com.landawn.abacus.util.{TYPE}Tuple.{TYPE}Tuple8;
import com.landawn.abacus.util.{TYPE}Tuple.{TYPE}Tuple9;
import com.landawn.abacus.util.stream.{STREAM_TYPE}Stream;
import com.landawn.abacus.TestBase;

/**
 * Comprehensive test suite for {TYPE}Tuple and its nested classes.
 * Tests all public methods including factory methods, statistical operations,
 * collection conversions, and special methods in Tuple2 and Tuple3.
 */
@Tag("2025")
public class {TYPE}Tuple2025Test extends TestBase {{

    // Factory method tests
    @Test
    public void testOf1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals(1, tuple.arity());
    }}

    @Test
    public void testOf2() {{
        {TYPE}Tuple2 tuple = {TYPE}Tuple.of({VAL1}, {VAL2});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals({VAL2}, tuple._2{DELTA});
        assertEquals(2, tuple.arity());
    }}

    @Test
    public void testOf3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals({VAL2}, tuple._2{DELTA});
        assertEquals({VAL3}, tuple._3{DELTA});
        assertEquals(3, tuple.arity());
    }}

    @Test
    public void testOf4() {{
        {TYPE}Tuple4 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals({VAL2}, tuple._2{DELTA});
        assertEquals({VAL3}, tuple._3{DELTA});
        assertEquals({VAL4}, tuple._4{DELTA});
        assertEquals(4, tuple.arity());
    }}

    @Test
    public void testOf5() {{
        {TYPE}Tuple5 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals({VAL5}, tuple._5{DELTA});
        assertEquals(5, tuple.arity());
    }}

    @Test
    public void testOf6() {{
        {TYPE}Tuple6 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5}, {VAL6});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals({VAL6}, tuple._6{DELTA});
        assertEquals(6, tuple.arity());
    }}

    @Test
    public void testOf7() {{
        {TYPE}Tuple7 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5}, {VAL6}, {VAL7});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals({VAL7}, tuple._7{DELTA});
        assertEquals(7, tuple.arity());
    }}

    @Test
    public void testOf8() {{
        {TYPE}Tuple8 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5}, {VAL6}, {VAL7}, {VAL8});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals({VAL8}, tuple._8{DELTA});
        assertEquals(8, tuple.arity());
    }}

    @Test
    public void testOf9() {{
        {TYPE}Tuple9 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5}, {VAL6}, {VAL7}, {VAL8}, {VAL9});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals({VAL9}, tuple._9{DELTA});
        assertEquals(9, tuple.arity());
    }}

    // Create method tests
    @Test
    public void testCreateEmpty() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }}

    @Test
    public void testCreateNull() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }}

    @Test
    public void testCreate1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.create(new {PRIM}[] {{ {VAL1} }});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals(1, tuple.arity());
    }}

    @Test
    public void testCreate3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.create(new {PRIM}[] {{ {VAL1}, {VAL2}, {VAL3} }});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals({VAL2}, tuple._2{DELTA});
        assertEquals({VAL3}, tuple._3{DELTA});
    }}

    @Test
    public void testCreate9() {{
        {TYPE}Tuple9 tuple = {TYPE}Tuple.create(new {PRIM}[] {{ {VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5}, {VAL6}, {VAL7}, {VAL8}, {VAL9} }});
        assertEquals({VAL1}, tuple._1{DELTA});
        assertEquals({VAL9}, tuple._9{DELTA});
    }}

    @Test
    public void testCreateTooMany() {{
        assertThrows(IllegalArgumentException.class, () -> {{
            {TYPE}Tuple.create(new {PRIM}[] {{ {VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5}, {VAL6}, {VAL7}, {VAL8}, {VAL9}, {VAL10} }});
        }});
    }}

    // Statistical method tests - min
    @Test
    public void testMinTuple1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        assertEquals({VAL1}, tuple.min(){DELTA});
    }}

    @Test
    public void testMinTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL3}, {VAL1}, {VAL2});
        assertEquals({VAL1}, tuple.min(){DELTA});
    }}

    @Test
    public void testMinTuple0ThrowsException() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }}

    // Statistical method tests - max
    @Test
    public void testMaxTuple1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        assertEquals({VAL1}, tuple.max(){DELTA});
    }}

    @Test
    public void testMaxTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL3}, {VAL1}, {VAL2});
        assertEquals({VAL3}, tuple.max(){DELTA});
    }}

    @Test
    public void testMaxTuple0ThrowsException() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }}

    // Statistical method tests - median
    @Test
    public void testMedianTuple1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        assertEquals({VAL1}, tuple.median(){DELTA});
    }}

    @Test
    public void testMedianTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL3}, {VAL1}, {VAL2});
        assertEquals({VAL2}, tuple.median(){DELTA});
    }}

    @Test
    public void testMedianTuple0ThrowsException() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }}

    // Statistical method tests - sum
    @Test
    public void testSumTuple0() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        assertEquals({SUM_ZERO}, tuple.sum(){SUM_DELTA});
    }}

    @Test
    public void testSumTuple1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        assertEquals({SUM1}, tuple.sum(){SUM_DELTA});
    }}

    @Test
    public void testSumTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        assertEquals({SUM123}, tuple.sum(){SUM_DELTA});
    }}

    // Statistical method tests - average
    @Test
    public void testAverageTuple1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        assertEquals({AVG1}, tuple.average(), 0.001);
    }}

    @Test
    public void testAverageTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        assertEquals({AVG123}, tuple.average(), 0.001);
    }}

    @Test
    public void testAverageTuple0ThrowsException() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }}

    // Reverse tests
    @Test
    public void testReverseTuple0() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        {TYPE}Tuple<{TYPE}Tuple0> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }}

    @Test
    public void testReverseTuple1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        {TYPE}Tuple1 reversed = tuple.reverse();
        assertEquals({VAL1}, reversed._1{DELTA});
    }}

    @Test
    public void testReverseTuple2() {{
        {TYPE}Tuple2 tuple = {TYPE}Tuple.of({VAL1}, {VAL2});
        {TYPE}Tuple2 reversed = tuple.reverse();
        assertEquals({VAL2}, reversed._1{DELTA});
        assertEquals({VAL1}, reversed._2{DELTA});
    }}

    @Test
    public void testReverseTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        {TYPE}Tuple3 reversed = tuple.reverse();
        assertEquals({VAL3}, reversed._1{DELTA});
        assertEquals({VAL2}, reversed._2{DELTA});
        assertEquals({VAL1}, reversed._3{DELTA});
    }}

    // Contains tests
    @Test
    public void testContainsTuple0() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        assertFalse(tuple.contains({VAL1}));
    }}

    @Test
    public void testContainsTuple1True() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        assertTrue(tuple.contains({VAL1}));
    }}

    @Test
    public void testContainsTuple1False() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        assertFalse(tuple.contains({VAL_NOT}));
    }}

    @Test
    public void testContainsTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        assertTrue(tuple.contains({VAL1}));
        assertTrue(tuple.contains({VAL2}));
        assertTrue(tuple.contains({VAL3}));
        assertFalse(tuple.contains({VAL_NOT}));
    }}

    // toArray tests
    @Test
    public void testToArrayTuple0() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        {PRIM}[] array = tuple.toArray();
        assertEquals(0, array.length);
    }}

    @Test
    public void testToArrayTuple1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        {PRIM}[] array = tuple.toArray();
        assertArrayEquals(new {PRIM}[] {{ {VAL1} }}, array{ARRAY_DELTA});
    }}

    @Test
    public void testToArrayTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        {PRIM}[] array = tuple.toArray();
        assertArrayEquals(new {PRIM}[] {{ {VAL1}, {VAL2}, {VAL3} }}, array{ARRAY_DELTA});
    }}

    @Test
    public void testToArrayModificationDoesNotAffectTuple() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        {PRIM}[] array = tuple.toArray();
        array[0] = {VAL_MOD};
        assertEquals({VAL1}, tuple._1{DELTA});
    }}

    // toList tests
    @Test
    public void testToListTuple0() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        {TYPE}List list = tuple.toList();
        assertEquals(0, list.size());
    }}

    @Test
    public void testToListTuple1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        {TYPE}List list = tuple.toList();
        assertEquals(1, list.size());
        assertEquals({VAL1}, list.get(0){DELTA});
    }}

    @Test
    public void testToListTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        {TYPE}List list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals({VAL1}, list.get(0){DELTA});
        assertEquals({VAL2}, list.get(1){DELTA});
        assertEquals({VAL3}, list.get(2){DELTA});
    }}

    // forEach tests
    @Test
    public void testForEachTuple0() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        List<{WRAPPER}> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(0, result.size());
    }}

    @Test
    public void testForEachTuple1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        List<{WRAPPER}> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(1, result.size());
        assertEquals({WRAPPER}.valueOf({VAL1}), result.get(0));
    }}

    @Test
    public void testForEachTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        List<{WRAPPER}> result = new ArrayList<>();
        tuple.forEach(i -> result.add(i));
        assertEquals(3, result.size());
        assertEquals({WRAPPER}.valueOf({VAL1}), result.get(0));
        assertEquals({WRAPPER}.valueOf({VAL2}), result.get(1));
        assertEquals({WRAPPER}.valueOf({VAL3}), result.get(2));
    }}

    // stream tests
    @Test
    public void testStreamTuple0() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        {STREAM_TYPE}Stream stream = tuple.stream();
        assertEquals(0, stream.count());
    }}

    @Test
    public void testStreamTuple1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        {STREAM_TYPE}Stream stream = tuple.stream();
        assertEquals({SUM1}, stream.sum(){SUM_DELTA});
    }}

    @Test
    public void testStreamTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        {STREAM_TYPE}Stream stream = tuple.stream();
        assertEquals({SUM123}, stream.sum(){SUM_DELTA});
    }}

    // hashCode tests
    @Test
    public void testHashCodeTuple1() {{
        {TYPE}Tuple1 tuple1 = {TYPE}Tuple.of({VAL1});
        {TYPE}Tuple1 tuple2 = {TYPE}Tuple.of({VAL1});
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }}

    @Test
    public void testHashCodeTuple2() {{
        {TYPE}Tuple2 tuple1 = {TYPE}Tuple.of({VAL1}, {VAL2});
        {TYPE}Tuple2 tuple2 = {TYPE}Tuple.of({VAL1}, {VAL2});
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }}

    @Test
    public void testHashCodeTuple3() {{
        {TYPE}Tuple3 tuple1 = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        {TYPE}Tuple3 tuple2 = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }}

    // equals tests
    @Test
    public void testEqualsSameObject() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        assertEquals(tuple, tuple);
    }}

    @Test
    public void testEqualsNull() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        assertNotEquals(null, tuple);
    }}

    @Test
    public void testEqualsDifferentClass() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        assertNotEquals("not a tuple", tuple);
    }}

    @Test
    public void testEqualsTuple1() {{
        {TYPE}Tuple1 tuple1 = {TYPE}Tuple.of({VAL1});
        {TYPE}Tuple1 tuple2 = {TYPE}Tuple.of({VAL1});
        {TYPE}Tuple1 tuple3 = {TYPE}Tuple.of({VAL_NOT});
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }}

    @Test
    public void testEqualsTuple2() {{
        {TYPE}Tuple2 tuple1 = {TYPE}Tuple.of({VAL1}, {VAL2});
        {TYPE}Tuple2 tuple2 = {TYPE}Tuple.of({VAL1}, {VAL2});
        {TYPE}Tuple2 tuple3 = {TYPE}Tuple.of({VAL1}, {VAL3});
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }}

    @Test
    public void testEqualsTuple3() {{
        {TYPE}Tuple3 tuple1 = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        {TYPE}Tuple3 tuple2 = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        {TYPE}Tuple3 tuple3 = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL4});
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }}

    // toString tests
    @Test
    public void testToStringTuple0() {{
        {TYPE}Tuple<{TYPE}Tuple0> tuple = {TYPE}Tuple.create(new {PRIM}[0]);
        assertEquals("[]", tuple.toString());
    }}

    @Test
    public void testToStringTuple1() {{
        {TYPE}Tuple1 tuple = {TYPE}Tuple.of({VAL1});
        String str = tuple.toString();
        assertTrue(str.contains("{TOSTR1}"));
    }}

    @Test
    public void testToStringTuple3() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        String str = tuple.toString();
        assertTrue(str.contains("{TOSTR1}"));
        assertTrue(str.contains("{TOSTR2}"));
        assertTrue(str.contains("{TOSTR3}"));
    }}

    // Tuple2 special methods - accept
    @Test
    public void testTuple2Accept() {{
        {TYPE}Tuple2 tuple = {TYPE}Tuple.of({VAL3}, {VAL4});
        List<{WRAPPER}> result = new ArrayList<>();
        tuple.accept((a, b) -> {{
            result.add(a);
            result.add(b);
        }});
        assertEquals(2, result.size());
        assertEquals({WRAPPER}.valueOf({VAL3}), result.get(0));
        assertEquals({WRAPPER}.valueOf({VAL4}), result.get(1));
    }}

    // Tuple2 special methods - map
    @Test
    public void testTuple2Map() {{
        {TYPE}Tuple2 tuple = {TYPE}Tuple.of({VAL3}, {VAL4});
        {MAP_RET_TYPE} result = tuple.map((a, b) -> {MAP_OP});
        {MAP_ASSERT}
    }}

    // Tuple2 special methods - filter
    @Test
    public void testTuple2FilterTrue() {{
        {TYPE}Tuple2 tuple = {TYPE}Tuple.of({VAL3}, {VAL4});
        var result = tuple.filter((a, b) -> {FILTER_TRUE});
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }}

    @Test
    public void testTuple2FilterFalse() {{
        {TYPE}Tuple2 tuple = {TYPE}Tuple.of({VAL3}, {VAL4});
        var result = tuple.filter((a, b) -> {FILTER_FALSE});
        assertFalse(result.isPresent());
    }}

    // Tuple3 special methods - accept
    @Test
    public void testTuple3Accept() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        List<{WRAPPER}> result = new ArrayList<>();
        tuple.accept((a, b, c) -> {{
            result.add(a);
            result.add(b);
            result.add(c);
        }});
        assertEquals(3, result.size());
        assertEquals({WRAPPER}.valueOf({VAL1}), result.get(0));
        assertEquals({WRAPPER}.valueOf({VAL2}), result.get(1));
        assertEquals({WRAPPER}.valueOf({VAL3}), result.get(2));
    }}

    // Tuple3 special methods - map
    @Test
    public void testTuple3Map() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        {MAP3_RET_TYPE} result = tuple.map((a, b, c) -> {MAP3_OP});
        {MAP3_ASSERT}
    }}

    // Tuple3 special methods - filter
    @Test
    public void testTuple3FilterTrue() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        var result = tuple.filter((a, b, c) -> {FILTER3_TRUE});
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }}

    @Test
    public void testTuple3FilterFalse() {{
        {TYPE}Tuple3 tuple = {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3});
        var result = tuple.filter((a, b, c) -> {FILTER3_FALSE});
        assertFalse(result.isPresent());
    }}

    // arity tests for all tuple sizes
    @Test
    public void testArity() {{
        assertEquals(0, {TYPE}Tuple.create(new {PRIM}[0]).arity());
        assertEquals(1, {TYPE}Tuple.of({VAL1}).arity());
        assertEquals(2, {TYPE}Tuple.of({VAL1}, {VAL2}).arity());
        assertEquals(3, {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}).arity());
        assertEquals(4, {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4}).arity());
        assertEquals(5, {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5}).arity());
        assertEquals(6, {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5}, {VAL6}).arity());
        assertEquals(7, {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5}, {VAL6}, {VAL7}).arity());
        assertEquals(8, {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5}, {VAL6}, {VAL7}, {VAL8}).arity());
        assertEquals(9, {TYPE}Tuple.of({VAL1}, {VAL2}, {VAL3}, {VAL4}, {VAL5}, {VAL6}, {VAL7}, {VAL8}, {VAL9}).arity());
    }}
}}
'''

# Configuration for each tuple type
CONFIGS = {
    'Double': {
        'TYPE': 'Double',
        'PRIM': 'double',
        'WRAPPER': 'Double',
        'STREAM_TYPE': 'Double',
        'VAL1': '1.0', 'VAL2': '2.0', 'VAL3': '3.0', 'VAL4': '4.0', 'VAL5': '5.0',
        'VAL6': '6.0', 'VAL7': '7.0', 'VAL8': '8.0', 'VAL9': '9.0', 'VAL10': '10.0',
        'VAL_NOT': '99.0', 'VAL_MOD': '999.0',
        'SUM_ZERO': '0.0', 'SUM1': '1.0', 'SUM123': '6.0',
        'AVG1': '1.0', 'AVG123': '2.0',
        'DELTA': ', 0.001', 'SUM_DELTA': ', 0.001', 'ARRAY_DELTA': ', 0.001',
        'TOSTR1': '1.0', 'TOSTR2': '2.0', 'TOSTR3': '3.0',
        'MAP_RET_TYPE': 'double', 'MAP_OP': 'a * b', 'MAP_ASSERT': 'assertEquals(12.0, result, 0.001);',
        'MAP3_RET_TYPE': 'double', 'MAP3_OP': 'a * b * c', 'MAP3_ASSERT': 'assertEquals(6.0, result, 0.001);',
        'FILTER_TRUE': 'a + b > 5.0', 'FILTER_FALSE': 'a + b > 10.0',
        'FILTER3_TRUE': 'a + b + c > 5.0', 'FILTER3_FALSE': 'a + b + c > 10.0',
    },
    'Float': {
        'TYPE': 'Float',
        'PRIM': 'float',
        'WRAPPER': 'Float',
        'STREAM_TYPE': 'Float',
        'VAL1': '1.0f', 'VAL2': '2.0f', 'VAL3': '3.0f', 'VAL4': '4.0f', 'VAL5': '5.0f',
        'VAL6': '6.0f', 'VAL7': '7.0f', 'VAL8': '8.0f', 'VAL9': '9.0f', 'VAL10': '10.0f',
        'VAL_NOT': '99.0f', 'VAL_MOD': '999.0f',
        'SUM_ZERO': '0.0f', 'SUM1': '1.0f', 'SUM123': '6.0f',
        'AVG1': '1.0', 'AVG123': '2.0',
        'DELTA': ', 0.001f', 'SUM_DELTA': ', 0.001f', 'ARRAY_DELTA': ', 0.001f',
        'TOSTR1': '1.0', 'TOSTR2': '2.0', 'TOSTR3': '3.0',
        'MAP_RET_TYPE': 'float', 'MAP_OP': 'a * b', 'MAP_ASSERT': 'assertEquals(12.0f, result, 0.001f);',
        'MAP3_RET_TYPE': 'float', 'MAP3_OP': 'a * b * c', 'MAP3_ASSERT': 'assertEquals(6.0f, result, 0.001f);',
        'FILTER_TRUE': 'a + b > 5.0f', 'FILTER_FALSE': 'a + b > 10.0f',
        'FILTER3_TRUE': 'a + b + c > 5.0f', 'FILTER3_FALSE': 'a + b + c > 10.0f',
    },
    'Long': {
        'TYPE': 'Long',
        'PRIM': 'long',
        'WRAPPER': 'Long',
        'STREAM_TYPE': 'Long',
        'VAL1': '1L', 'VAL2': '2L', 'VAL3': '3L', 'VAL4': '4L', 'VAL5': '5L',
        'VAL6': '6L', 'VAL7': '7L', 'VAL8': '8L', 'VAL9': '9L', 'VAL10': '10L',
        'VAL_NOT': '99L', 'VAL_MOD': '999L',
        'SUM_ZERO': '0L', 'SUM1': '1L', 'SUM123': '6L',
        'AVG1': '1.0', 'AVG123': '2.0',
        'DELTA': '', 'SUM_DELTA': '', 'ARRAY_DELTA': '',
        'TOSTR1': '1', 'TOSTR2': '2', 'TOSTR3': '3',
        'MAP_RET_TYPE': 'long', 'MAP_OP': 'a * b', 'MAP_ASSERT': 'assertEquals(12L, result);',
        'MAP3_RET_TYPE': 'long', 'MAP3_OP': 'a * b * c', 'MAP3_ASSERT': 'assertEquals(6L, result);',
        'FILTER_TRUE': 'a + b > 5L', 'FILTER_FALSE': 'a + b > 10L',
        'FILTER3_TRUE': 'a + b + c > 5L', 'FILTER3_FALSE': 'a + b + c > 10L',
    },
    'Byte': {
        'TYPE': 'Byte',
        'PRIM': 'byte',
        'WRAPPER': 'Byte',
        'STREAM_TYPE': 'Byte',
        'VAL1': '(byte) 1', 'VAL2': '(byte) 2', 'VAL3': '(byte) 3', 'VAL4': '(byte) 4', 'VAL5': '(byte) 5',
        'VAL6': '(byte) 6', 'VAL7': '(byte) 7', 'VAL8': '(byte) 8', 'VAL9': '(byte) 9', 'VAL10': '(byte) 10',
        'VAL_NOT': '(byte) 99', 'VAL_MOD': '(byte) 100',
        'SUM_ZERO': '0', 'SUM1': '1', 'SUM123': '6',
        'AVG1': '1.0', 'AVG123': '2.0',
        'DELTA': '', 'SUM_DELTA': '', 'ARRAY_DELTA': '',
        'TOSTR1': '1', 'TOSTR2': '2', 'TOSTR3': '3',
        'MAP_RET_TYPE': 'int', 'MAP_OP': 'a * b', 'MAP_ASSERT': 'assertEquals(12, result);',
        'MAP3_RET_TYPE': 'int', 'MAP3_OP': 'a * b * c', 'MAP3_ASSERT': 'assertEquals(6, result);',
        'FILTER_TRUE': 'a + b > 5', 'FILTER_FALSE': 'a + b > 10',
        'FILTER3_TRUE': 'a + b + c > 5', 'FILTER3_FALSE': 'a + b + c > 10',
    },
    'Char': {
        'TYPE': 'Char',
        'PRIM': 'char',
        'WRAPPER': 'Character',
        'STREAM_TYPE': 'Char',
        'VAL1': "'a'", 'VAL2': "'b'", 'VAL3': "'c'", 'VAL4': "'d'", 'VAL5': "'e'",
        'VAL6': "'f'", 'VAL7': "'g'", 'VAL8': "'h'", 'VAL9': "'i'", 'VAL10': "'j'",
        'VAL_NOT': "'z'", 'VAL_MOD': "'x'",
        'SUM_ZERO': '0', 'SUM1': '97', 'SUM123': '294',
        'AVG1': '97.0', 'AVG123': '98.0',
        'DELTA': '', 'SUM_DELTA': '', 'ARRAY_DELTA': '',
        'TOSTR1': 'a', 'TOSTR2': 'b', 'TOSTR3': 'c',
        'MAP_RET_TYPE': 'String', 'MAP_OP': 'String.valueOf(a) + String.valueOf(b)', 'MAP_ASSERT': 'assertEquals("cd", result);',
        'MAP3_RET_TYPE': 'String', 'MAP3_OP': 'String.valueOf(a) + String.valueOf(b) + String.valueOf(c)', 'MAP3_ASSERT': 'assertEquals("abc", result);',
        'FILTER_TRUE': 'a < b', 'FILTER_FALSE': 'a > b',
        'FILTER3_TRUE': 'a < b && b < c', 'FILTER3_FALSE': 'a > c',
    },
    'Short': {
        'TYPE': 'Short',
        'PRIM': 'short',
        'WRAPPER': 'Short',
        'STREAM_TYPE': 'Short',
        'VAL1': '(short) 1', 'VAL2': '(short) 2', 'VAL3': '(short) 3', 'VAL4': '(short) 4', 'VAL5': '(short) 5',
        'VAL6': '(short) 6', 'VAL7': '(short) 7', 'VAL8': '(short) 8', 'VAL9': '(short) 9', 'VAL10': '(short) 10',
        'VAL_NOT': '(short) 99', 'VAL_MOD': '(short) 100',
        'SUM_ZERO': '0', 'SUM1': '1', 'SUM123': '6',
        'AVG1': '1.0', 'AVG123': '2.0',
        'DELTA': '', 'SUM_DELTA': '', 'ARRAY_DELTA': '',
        'TOSTR1': '1', 'TOSTR2': '2', 'TOSTR3': '3',
        'MAP_RET_TYPE': 'int', 'MAP_OP': 'a * b', 'MAP_ASSERT': 'assertEquals(12, result);',
        'MAP3_RET_TYPE': 'int', 'MAP3_OP': 'a * b * c', 'MAP3_ASSERT': 'assertEquals(6, result);',
        'FILTER_TRUE': 'a + b > 5', 'FILTER_FALSE': 'a + b > 10',
        'FILTER3_TRUE': 'a + b + c > 5', 'FILTER3_FALSE': 'a + b + c > 10',
    },
    'Boolean': {
        'TYPE': 'Boolean',
        'PRIM': 'boolean',
        'WRAPPER': 'Boolean',
        'STREAM_TYPE': 'Boolean',
        'VAL1': 'true', 'VAL2': 'false', 'VAL3': 'true', 'VAL4': 'false', 'VAL5': 'true',
        'VAL6': 'false', 'VAL7': 'true', 'VAL8': 'false', 'VAL9': 'true', 'VAL10': 'false',
        'VAL_NOT': 'false', 'VAL_MOD': 'true',
        'SUM_ZERO': '0', 'SUM1': '1', 'SUM123': '2',
        'AVG1': '1.0', 'AVG123': '0.666',
        'DELTA': '', 'SUM_DELTA': '', 'ARRAY_DELTA': '',
        'TOSTR1': 'true', 'TOSTR2': 'false', 'TOSTR3': 'true',
        'MAP_RET_TYPE': 'boolean', 'MAP_OP': 'a && b', 'MAP_ASSERT': 'assertEquals(false, result);',
        'MAP3_RET_TYPE': 'boolean', 'MAP3_OP': 'a || b || c', 'MAP3_ASSERT': 'assertEquals(true, result);',
        'FILTER_TRUE': 'a || b', 'FILTER_FALSE': 'a && b',
        'FILTER3_TRUE': 'a || b || c', 'FILTER3_FALSE': 'a && b && c',
    },
}

import os

def generate_test_file(config):
    content = TEMPLATE
    for key, value in config.items():
        content = content.replace('{' + key + '}', str(value))
    return content

# Generate all test files
output_dir = r'C:\Users\haiyangl\Landawn\abacus-extra\src\test\java\com\landawn\abacus\util'
for type_name, config in CONFIGS.items():
    content = generate_test_file(config)
    output_file = os.path.join(output_dir, f'{type_name}Tuple2025Test.java')
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f'Generated: {output_file}')

print('\nAll test files generated successfully!')
