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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.ByteTuple.ByteTuple1;
import com.landawn.abacus.util.ByteTuple.ByteTuple2;
import com.landawn.abacus.util.ByteTuple.ByteTuple3;
import com.landawn.abacus.util.ByteTuple.ByteTuple4;
import com.landawn.abacus.util.ByteTuple.ByteTuple5;
import com.landawn.abacus.util.ByteTuple.ByteTuple6;
import com.landawn.abacus.util.ByteTuple.ByteTuple7;
import com.landawn.abacus.util.ByteTuple.ByteTuple8;
import com.landawn.abacus.util.ByteTuple.ByteTuple9;
import com.landawn.abacus.util.u.Optional;

@Tag("2510")
public class ByteTuple2510Test extends TestBase {

    // ============ Factory Method Tests - ByteTuple.of() ============

    @Test
    public void testOf_tuple1() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf_tuple2() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf_tuple3() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertNotNull(tuple);
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
        assertEquals((byte) 30, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf_tuple4() {
        ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        assertNotNull(tuple);
        assertEquals((byte) 1, tuple._1);
        assertEquals((byte) 2, tuple._2);
        assertEquals((byte) 3, tuple._3);
        assertEquals((byte) 4, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf_tuple5() {
        ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        assertNotNull(tuple);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf_tuple6() {
        ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        assertNotNull(tuple);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf_tuple7() {
        ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        assertNotNull(tuple);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf_tuple8() {
        ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        assertNotNull(tuple);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf_tuple9() {
        ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        assertNotNull(tuple);
        assertEquals(9, tuple.arity());
    }

    // ============ Factory Method Tests - ByteTuple.create() ============

    @Test
    public void testCreate_nullArray() {
        ByteTuple<?> tuple = ByteTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_emptyArray() {
        ByteTuple<?> tuple = ByteTuple.create(new byte[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_array1() {
        ByteTuple1 tuple = ByteTuple.create(new byte[] { 10 });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals((byte) 10, tuple._1);
    }

    @Test
    public void testCreate_array2() {
        ByteTuple2 tuple = ByteTuple.create(new byte[] { 10, 20 });
        assertNotNull(tuple);
        assertEquals(2, tuple.arity());
        assertEquals((byte) 10, tuple._1);
        assertEquals((byte) 20, tuple._2);
    }

    @Test
    public void testCreate_array3() {
        ByteTuple3 tuple = ByteTuple.create(new byte[] { 10, 20, 30 });
        assertNotNull(tuple);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testCreate_array4() {
        ByteTuple4 tuple = ByteTuple.create(new byte[] { 1, 2, 3, 4 });
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testCreate_array5() {
        ByteTuple5 tuple = ByteTuple.create(new byte[] { 1, 2, 3, 4, 5 });
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testCreate_array6() {
        ByteTuple6 tuple = ByteTuple.create(new byte[] { 1, 2, 3, 4, 5, 6 });
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testCreate_array7() {
        ByteTuple7 tuple = ByteTuple.create(new byte[] { 1, 2, 3, 4, 5, 6, 7 });
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testCreate_array8() {
        ByteTuple8 tuple = ByteTuple.create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testCreate_array9() {
        ByteTuple9 tuple = ByteTuple.create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testCreate_arrayTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> {
            ByteTuple.create(new byte[10]);
        });
    }

    // ============ ByteTuple0 Tests ============

    @Test
    public void testTuple0_arity() {
        ByteTuple<?> tuple = ByteTuple.create(new byte[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testTuple0_min_throwsException() {
        ByteTuple<?> tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void testTuple0_max_throwsException() {
        ByteTuple<?> tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void testTuple0_median_throwsException() {
        ByteTuple<?> tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void testTuple0_sum() {
        ByteTuple<?> tuple = ByteTuple.create(new byte[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void testTuple0_average_throwsException() {
        ByteTuple<?> tuple = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void testTuple0_reverse() {
        ByteTuple<?> tuple = ByteTuple.create(new byte[0]);
        ByteTuple<?> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testTuple0_contains() {
        ByteTuple<?> tuple = ByteTuple.create(new byte[0]);
        assertFalse(tuple.contains((byte) 10));
    }

    @Test
    public void testTuple0_toString() {
        ByteTuple<?> tuple = ByteTuple.create(new byte[0]);
        assertEquals("[]", tuple.toString());
    }

    // ============ ByteTuple1 Tests ============

    @Test
    public void testTuple1_arity() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testTuple1_min() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals((byte) 10, tuple.min());
    }

    @Test
    public void testTuple1_max() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals((byte) 10, tuple.max());
    }

    @Test
    public void testTuple1_median() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals((byte) 10, tuple.median());
    }

    @Test
    public void testTuple1_sum() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals(10, tuple.sum());
    }

    @Test
    public void testTuple1_average() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals(10.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple1_reverse() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        ByteTuple1 reversed = tuple.reverse();
        assertEquals((byte) 10, reversed._1);
        assertNotSame(tuple, reversed);
    }

    @Test
    public void testTuple1_contains_true() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertTrue(tuple.contains((byte) 10));
    }

    @Test
    public void testTuple1_contains_false() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertFalse(tuple.contains((byte) 20));
    }

    @Test
    public void testTuple1_hashCode() {
        ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
        ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
        ByteTuple1 tuple3 = ByteTuple.of((byte) 20);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertEquals(10, tuple1.hashCode());
        assertEquals(20, tuple3.hashCode());
    }

    @Test
    public void testTuple1_equals() {
        ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
        ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
        ByteTuple1 tuple3 = ByteTuple.of((byte) 20);

        assertEquals(tuple1, tuple1);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertNotEquals(tuple1, null);
        assertNotEquals(tuple1, "not a tuple");
    }

    @Test
    public void testTuple1_toString() {
        ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals("[10]", tuple.toString());
    }

    // ============ ByteTuple2 Tests ============

    @Test
    public void testTuple2_arity() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testTuple2_min() {
        ByteTuple2 tuple = ByteTuple.of((byte) 20, (byte) 10);
        assertEquals((byte) 10, tuple.min());
    }

    @Test
    public void testTuple2_max() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals((byte) 20, tuple.max());
    }

    @Test
    public void testTuple2_median() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals((byte) 10, tuple.median());
    }

    @Test
    public void testTuple2_sum() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(30, tuple.sum());
    }

    @Test
    public void testTuple2_average() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(15.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple2_reverse() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 reversed = tuple.reverse();
        assertEquals((byte) 20, reversed._1);
        assertEquals((byte) 10, reversed._2);
    }

    @Test
    public void testTuple2_contains() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertTrue(tuple.contains((byte) 10));
        assertTrue(tuple.contains((byte) 20));
        assertFalse(tuple.contains((byte) 30));
    }

    @Test
    public void testTuple2_forEach() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        List<Byte> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(2, values.size());
        assertEquals((byte) 10, values.get(0));
        assertEquals((byte) 20, values.get(1));
    }

    @Test
    public void testTuple2_accept() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        List<Integer> results = new ArrayList<>();
        tuple.accept((a, b) -> results.add((int) (a + b)));
        assertEquals(1, results.size());
        assertEquals(30, results.get(0));
    }

    @Test
    public void testTuple2_map() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        Integer result = tuple.map((a, b) -> (int) (a + b));
        assertEquals(30, result);
    }

    @Test
    public void testTuple2_filter_matches() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2_filter_noMatch() {
        ByteTuple2 tuple = ByteTuple.of((byte) 20, (byte) 10);
        Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
        assertFalse(result.isPresent());
    }

    @Test
    public void testTuple2_hashCode() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testTuple2_equals() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple3 = ByteTuple.of((byte) 20, (byte) 10);

        assertEquals(tuple1, tuple1);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testTuple2_toString() {
        ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals("[10, 20]", tuple.toString());
    }

    // ============ ByteTuple3 Tests ============

    @Test
    public void testTuple3_arity() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testTuple3_min() {
        ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals((byte) 10, tuple.min());
    }

    @Test
    public void testTuple3_max() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 30, (byte) 20);
        assertEquals((byte) 30, tuple.max());
    }

    @Test
    public void testTuple3_median() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 30, (byte) 20);
        assertEquals((byte) 20, tuple.median());
    }

    @Test
    public void testTuple3_sum() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(60, tuple.sum());
    }

    @Test
    public void testTuple3_average() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(20.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple3_reverse() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple3 reversed = tuple.reverse();
        assertEquals((byte) 30, reversed._1);
        assertEquals((byte) 20, reversed._2);
        assertEquals((byte) 10, reversed._3);
    }

    @Test
    public void testTuple3_contains() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertTrue(tuple.contains((byte) 10));
        assertTrue(tuple.contains((byte) 20));
        assertTrue(tuple.contains((byte) 30));
        assertFalse(tuple.contains((byte) 40));
    }

    @Test
    public void testTuple3_forEach() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        List<Byte> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(3, values.size());
    }

    @Test
    public void testTuple3_accept() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        List<Integer> results = new ArrayList<>();
        tuple.accept((a, b, c) -> results.add((int) (a + b + c)));
        assertEquals(1, results.size());
        assertEquals(60, results.get(0));
    }

    @Test
    public void testTuple3_map() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        Integer result = tuple.map((a, b, c) -> (int) (a + b + c));
        assertEquals(60, result);
    }

    @Test
    public void testTuple3_filter_matches() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
    }

    @Test
    public void testTuple3_filter_noMatch() {
        ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 20, (byte) 10);
        Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertFalse(result.isPresent());
    }

    @Test
    public void testTuple3_hashCode() {
        ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testTuple3_equals() {
        ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple3 tuple3 = ByteTuple.of((byte) 30, (byte) 20, (byte) 10);

        assertEquals(tuple1, tuple1);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testTuple3_toString() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals("[10, 20, 30]", tuple.toString());
    }

    // ============ ByteTuple4-9 Basic Tests ============

    @Test
    public void testTuple4_reverse() {
        ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        ByteTuple4 reversed = tuple.reverse();
        assertEquals((byte) 4, reversed._1);
        assertEquals((byte) 3, reversed._2);
        assertEquals((byte) 2, reversed._3);
        assertEquals((byte) 1, reversed._4);
    }

    @Test
    public void testTuple5_reverse() {
        ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        ByteTuple5 reversed = tuple.reverse();
        assertEquals((byte) 5, reversed._1);
        assertEquals((byte) 1, reversed._5);
    }

    @Test
    public void testTuple6_reverse() {
        ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        ByteTuple6 reversed = tuple.reverse();
        assertEquals((byte) 6, reversed._1);
        assertEquals((byte) 1, reversed._6);
    }

    @Test
    public void testTuple7_reverse() {
        ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        ByteTuple7 reversed = tuple.reverse();
        assertEquals((byte) 7, reversed._1);
        assertEquals((byte) 1, reversed._7);
    }

    @Test
    public void testTuple8_reverse() {
        ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        ByteTuple8 reversed = tuple.reverse();
        assertEquals((byte) 8, reversed._1);
        assertEquals((byte) 1, reversed._8);
    }

    @Test
    public void testTuple9_reverse() {
        ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        ByteTuple9 reversed = tuple.reverse();
        assertEquals((byte) 9, reversed._1);
        assertEquals((byte) 1, reversed._9);
    }

    @Test
    public void testTuple4_contains() {
        ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        assertTrue(tuple.contains((byte) 1));
        assertTrue(tuple.contains((byte) 4));
        assertFalse(tuple.contains((byte) 5));
    }

    @Test
    public void testTuple5_contains() {
        ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        assertTrue(tuple.contains((byte) 5));
        assertFalse(tuple.contains((byte) 6));
    }

    @Test
    public void testTuple6_contains() {
        ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        assertTrue(tuple.contains((byte) 6));
        assertFalse(tuple.contains((byte) 7));
    }

    @Test
    public void testTuple7_contains() {
        ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        assertTrue(tuple.contains((byte) 7));
        assertFalse(tuple.contains((byte) 8));
    }

    @Test
    public void testTuple8_contains() {
        ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        assertTrue(tuple.contains((byte) 8));
        assertFalse(tuple.contains((byte) 9));
    }

    @Test
    public void testTuple9_contains() {
        ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        assertTrue(tuple.contains((byte) 9));
        assertFalse(tuple.contains((byte) 10));
    }

    @Test
    public void testTuple4_forEach() {
        ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        List<Byte> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(4, values.size());
    }

    @Test
    public void testTuple5_forEach() {
        ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
        List<Byte> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(5, values.size());
    }

    @Test
    public void testTuple6_forEach() {
        ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
        List<Byte> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(6, values.size());
    }

    @Test
    public void testTuple7_forEach() {
        ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        List<Byte> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(7, values.size());
    }

    @Test
    public void testTuple8_forEach() {
        ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
        List<Byte> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(8, values.size());
    }

    @Test
    public void testTuple9_forEach() {
        ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
        List<Byte> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(9, values.size());
    }

    // ============ Common Method Tests (inherited from ByteTuple) ============

    @Test
    public void testToArray() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        byte[] array = tuple.toArray();
        assertArrayEquals(new byte[] { 10, 20, 30 }, array);

        // Verify it's a copy
        array[0] = 99;
        assertEquals((byte) 10, tuple._1);
    }

    @Test
    public void testToList() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteList list = tuple.toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals((byte) 10, list.get(0));
        assertEquals((byte) 20, list.get(1));
        assertEquals((byte) 30, list.get(2));
    }

    @Test
    public void testStream() {
        ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        int sum = tuple.stream().sum();
        assertEquals(60, sum);
    }

    @Test
    public void testHashCode_consistency() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testEquals_symmetry() {
        ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(tuple1, tuple2);
        assertEquals(tuple2, tuple1);
    }

    @Test
    public void testEquals_differentTypes() {
        ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertNotEquals(tuple2, tuple3);
    }

    @Test
    public void testNegativeValues() {
        ByteTuple3 tuple = ByteTuple.of((byte) -10, (byte) -20, (byte) -30);
        assertEquals((byte) -30, tuple.min());
        assertEquals((byte) -10, tuple.max());
        assertEquals(-60, tuple.sum());
    }

    @Test
    public void testMinMaxMedian_largerTuple() {
        ByteTuple5 tuple = ByteTuple.of((byte) 5, (byte) 2, (byte) 8, (byte) 1, (byte) 9);
        assertEquals((byte) 1, tuple.min());
        assertEquals((byte) 9, tuple.max());
        assertEquals((byte) 5, tuple.median());
    }
}
