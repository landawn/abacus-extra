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

@Tag("2510")
public class CharTuple2510Test extends TestBase {

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
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testOf_tuple6() {
        CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
        assertNotNull(tuple);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testOf_tuple7() {
        CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
        assertNotNull(tuple);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testOf_tuple8() {
        CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
        assertNotNull(tuple);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testOf_tuple9() {
        CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
        assertNotNull(tuple);
        assertEquals(9, tuple.arity());
    }

    // ============ Factory Method Tests - CharTuple.create() ============

    @Test
    public void testCreate_nullArray() {
        CharTuple<?> tuple = CharTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_emptyArray() {
        CharTuple<?> tuple = CharTuple.create(new char[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_array1() {
        CharTuple1 tuple = CharTuple.create(new char[] { 'A' });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals('A', tuple._1);
    }

    @Test
    public void testCreate_array2() {
        CharTuple2 tuple = CharTuple.create(new char[] { 'A', 'B' });
        assertNotNull(tuple);
        assertEquals(2, tuple.arity());
        assertEquals('A', tuple._1);
        assertEquals('B', tuple._2);
    }

    @Test
    public void testCreate_array3() {
        CharTuple3 tuple = CharTuple.create(new char[] { 'A', 'B', 'C' });
        assertNotNull(tuple);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testCreate_array4() {
        CharTuple4 tuple = CharTuple.create(new char[] { 'A', 'B', 'C', 'D' });
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testCreate_array5() {
        CharTuple5 tuple = CharTuple.create(new char[] { 'A', 'B', 'C', 'D', 'E' });
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testCreate_array6() {
        CharTuple6 tuple = CharTuple.create(new char[] { 'A', 'B', 'C', 'D', 'E', 'F' });
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testCreate_array7() {
        CharTuple7 tuple = CharTuple.create(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G' });
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testCreate_array8() {
        CharTuple8 tuple = CharTuple.create(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' });
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testCreate_array9() {
        CharTuple9 tuple = CharTuple.create(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' });
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testCreate_arrayTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> {
            CharTuple.create(new char[10]);
        });
    }

    // ============ CharTuple0 Tests ============

    @Test
    public void testTuple0_arity() {
        CharTuple<?> tuple = CharTuple.create(new char[0]);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testTuple0_min_throwsException() {
        CharTuple<?> tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.min());
    }

    @Test
    public void testTuple0_max_throwsException() {
        CharTuple<?> tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.max());
    }

    @Test
    public void testTuple0_median_throwsException() {
        CharTuple<?> tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.median());
    }

    @Test
    public void testTuple0_sum() {
        CharTuple<?> tuple = CharTuple.create(new char[0]);
        assertEquals(0, tuple.sum());
    }

    @Test
    public void testTuple0_average_throwsException() {
        CharTuple<?> tuple = CharTuple.create(new char[0]);
        assertThrows(NoSuchElementException.class, () -> tuple.average());
    }

    @Test
    public void testTuple0_reverse() {
        CharTuple<?> tuple = CharTuple.create(new char[0]);
        CharTuple<?> reversed = tuple.reverse();
        assertNotNull(reversed);
        assertEquals(0, reversed.arity());
    }

    @Test
    public void testTuple0_contains() {
        CharTuple<?> tuple = CharTuple.create(new char[0]);
        assertFalse(tuple.contains('A'));
    }

    @Test
    public void testTuple0_toString() {
        CharTuple<?> tuple = CharTuple.create(new char[0]);
        assertEquals("()", tuple.toString());
    }

    // ============ CharTuple1 Tests ============

    @Test
    public void testTuple1_arity() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testTuple1_min() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals('A', tuple.min());
    }

    @Test
    public void testTuple1_max() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals('A', tuple.max());
    }

    @Test
    public void testTuple1_median() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals('A', tuple.median());
    }

    @Test
    public void testTuple1_sum() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals(65, tuple.sum()); // ASCII value of 'A' is 65
    }

    @Test
    public void testTuple1_average() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals(65.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple1_reverse() {
        CharTuple1 tuple = CharTuple.of('A');
        CharTuple1 reversed = tuple.reverse();
        assertEquals('A', reversed._1);
        assertNotSame(tuple, reversed);
    }

    @Test
    public void testTuple1_contains_true() {
        CharTuple1 tuple = CharTuple.of('A');
        assertTrue(tuple.contains('A'));
    }

    @Test
    public void testTuple1_contains_false() {
        CharTuple1 tuple = CharTuple.of('A');
        assertFalse(tuple.contains('B'));
    }

    @Test
    public void testTuple1_hashCode() {
        CharTuple1 tuple1 = CharTuple.of('A');
        CharTuple1 tuple2 = CharTuple.of('A');
        CharTuple1 tuple3 = CharTuple.of('B');

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertEquals(65, tuple1.hashCode()); // ASCII value of 'A'
        assertEquals(66, tuple3.hashCode()); // ASCII value of 'B'
    }

    @Test
    public void testTuple1_equals() {
        CharTuple1 tuple1 = CharTuple.of('A');
        CharTuple1 tuple2 = CharTuple.of('A');
        CharTuple1 tuple3 = CharTuple.of('B');

        assertEquals(tuple1, tuple1);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertNotEquals(tuple1, null);
        assertNotEquals(tuple1, "not a tuple");
    }

    @Test
    public void testTuple1_toString() {
        CharTuple1 tuple = CharTuple.of('A');
        assertEquals("(A)", tuple.toString());
    }

    // ============ CharTuple2 Tests ============

    @Test
    public void testTuple2_arity() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testTuple2_min() {
        CharTuple2 tuple = CharTuple.of('B', 'A');
        assertEquals('A', tuple.min());
    }

    @Test
    public void testTuple2_max() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals('B', tuple.max());
    }

    @Test
    public void testTuple2_median() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals('A', tuple.median());
    }

    @Test
    public void testTuple2_sum() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals(131, tuple.sum()); // 65 + 66
    }

    @Test
    public void testTuple2_average() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals(65.5, tuple.average(), 0.001);
    }

    @Test
    public void testTuple2_reverse() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        CharTuple2 reversed = tuple.reverse();
        assertEquals('B', reversed._1);
        assertEquals('A', reversed._2);
    }

    @Test
    public void testTuple2_contains() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertTrue(tuple.contains('A'));
        assertTrue(tuple.contains('B'));
        assertFalse(tuple.contains('C'));
    }

    @Test
    public void testTuple2_forEach() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        List<Character> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(2, values.size());
        assertEquals('A', values.get(0));
        assertEquals('B', values.get(1));
    }

    @Test
    public void testTuple2_accept() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        List<String> results = new ArrayList<>();
        tuple.accept((a, b) -> results.add("" + a + b));
        assertEquals(1, results.size());
        assertEquals("AB", results.get(0));
    }

    @Test
    public void testTuple2_map() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        String result = tuple.map((a, b) -> "" + a + b);
        assertEquals("AB", result);
    }

    @Test
    public void testTuple2_filter_matches() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2_filter_noMatch() {
        CharTuple2 tuple = CharTuple.of('B', 'A');
        Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);
        assertFalse(result.isPresent());
    }

    @Test
    public void testTuple2_hashCode() {
        CharTuple2 tuple1 = CharTuple.of('A', 'B');
        CharTuple2 tuple2 = CharTuple.of('A', 'B');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testTuple2_equals() {
        CharTuple2 tuple1 = CharTuple.of('A', 'B');
        CharTuple2 tuple2 = CharTuple.of('A', 'B');
        CharTuple2 tuple3 = CharTuple.of('B', 'A');

        assertEquals(tuple1, tuple1);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testTuple2_toString() {
        CharTuple2 tuple = CharTuple.of('A', 'B');
        assertEquals("(A, B)", tuple.toString());
    }

    // ============ CharTuple3 Tests ============

    @Test
    public void testTuple3_arity() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testTuple3_min() {
        CharTuple3 tuple = CharTuple.of('C', 'A', 'B');
        assertEquals('A', tuple.min());
    }

    @Test
    public void testTuple3_max() {
        CharTuple3 tuple = CharTuple.of('A', 'C', 'B');
        assertEquals('C', tuple.max());
    }

    @Test
    public void testTuple3_median() {
        CharTuple3 tuple = CharTuple.of('A', 'C', 'B');
        assertEquals('B', tuple.median());
    }

    @Test
    public void testTuple3_sum() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertEquals(198, tuple.sum()); // 65 + 66 + 67
    }

    @Test
    public void testTuple3_average() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertEquals(66.0, tuple.average(), 0.001);
    }

    @Test
    public void testTuple3_reverse() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        CharTuple3 reversed = tuple.reverse();
        assertEquals('C', reversed._1);
        assertEquals('B', reversed._2);
        assertEquals('A', reversed._3);
    }

    @Test
    public void testTuple3_contains() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertTrue(tuple.contains('A'));
        assertTrue(tuple.contains('B'));
        assertTrue(tuple.contains('C'));
        assertFalse(tuple.contains('D'));
    }

    @Test
    public void testTuple3_forEach() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        List<Character> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(3, values.size());
    }

    @Test
    public void testTuple3_accept() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        List<String> results = new ArrayList<>();
        tuple.accept((a, b, c) -> results.add("" + a + b + c));
        assertEquals(1, results.size());
        assertEquals("ABC", results.get(0));
    }

    @Test
    public void testTuple3_map() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        String result = tuple.map((a, b, c) -> "" + a + b + c);
        assertEquals("ABC", result);
    }

    @Test
    public void testTuple3_filter_matches() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertTrue(result.isPresent());
    }

    @Test
    public void testTuple3_filter_noMatch() {
        CharTuple3 tuple = CharTuple.of('C', 'B', 'A');
        Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
        assertFalse(result.isPresent());
    }

    @Test
    public void testTuple3_hashCode() {
        CharTuple3 tuple1 = CharTuple.of('A', 'B', 'C');
        CharTuple3 tuple2 = CharTuple.of('A', 'B', 'C');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testTuple3_equals() {
        CharTuple3 tuple1 = CharTuple.of('A', 'B', 'C');
        CharTuple3 tuple2 = CharTuple.of('A', 'B', 'C');
        CharTuple3 tuple3 = CharTuple.of('C', 'B', 'A');

        assertEquals(tuple1, tuple1);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testTuple3_toString() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        assertEquals("(A, B, C)", tuple.toString());
    }

    // ============ CharTuple4-9 Basic Tests ============

    @Test
    public void testTuple4_reverse() {
        CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
        CharTuple4 reversed = tuple.reverse();
        assertEquals('D', reversed._1);
        assertEquals('C', reversed._2);
        assertEquals('B', reversed._3);
        assertEquals('A', reversed._4);
    }

    @Test
    public void testTuple5_reverse() {
        CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
        CharTuple5 reversed = tuple.reverse();
        assertEquals('E', reversed._1);
        assertEquals('A', reversed._5);
    }

    @Test
    public void testTuple6_reverse() {
        CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
        CharTuple6 reversed = tuple.reverse();
        assertEquals('F', reversed._1);
        assertEquals('A', reversed._6);
    }

    @Test
    public void testTuple7_reverse() {
        CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
        CharTuple7 reversed = tuple.reverse();
        assertEquals('G', reversed._1);
        assertEquals('A', reversed._7);
    }

    @Test
    public void testTuple8_reverse() {
        CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
        CharTuple8 reversed = tuple.reverse();
        assertEquals('H', reversed._1);
        assertEquals('A', reversed._8);
    }

    @Test
    public void testTuple9_reverse() {
        CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
        CharTuple9 reversed = tuple.reverse();
        assertEquals('I', reversed._1);
        assertEquals('A', reversed._9);
    }

    @Test
    public void testTuple4_contains() {
        CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
        assertTrue(tuple.contains('A'));
        assertTrue(tuple.contains('D'));
        assertFalse(tuple.contains('E'));
    }

    @Test
    public void testTuple5_contains() {
        CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
        assertTrue(tuple.contains('E'));
        assertFalse(tuple.contains('F'));
    }

    @Test
    public void testTuple6_contains() {
        CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
        assertTrue(tuple.contains('F'));
        assertFalse(tuple.contains('G'));
    }

    @Test
    public void testTuple7_contains() {
        CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
        assertTrue(tuple.contains('G'));
        assertFalse(tuple.contains('H'));
    }

    @Test
    public void testTuple8_contains() {
        CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
        assertTrue(tuple.contains('H'));
        assertFalse(tuple.contains('I'));
    }

    @Test
    public void testTuple9_contains() {
        CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
        assertTrue(tuple.contains('I'));
        assertFalse(tuple.contains('J'));
    }

    @Test
    public void testTuple4_forEach() {
        CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
        List<Character> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(4, values.size());
    }

    @Test
    public void testTuple5_forEach() {
        CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
        List<Character> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(5, values.size());
    }

    @Test
    public void testTuple6_forEach() {
        CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
        List<Character> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(6, values.size());
    }

    @Test
    public void testTuple7_forEach() {
        CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
        List<Character> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(7, values.size());
    }

    @Test
    public void testTuple8_forEach() {
        CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
        List<Character> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(8, values.size());
    }

    @Test
    public void testTuple9_forEach() {
        CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
        List<Character> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(9, values.size());
    }

    // ============ Common Method Tests (inherited from CharTuple) ============

    @Test
    public void testToArray() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        char[] array = tuple.toArray();
        assertArrayEquals(new char[] { 'A', 'B', 'C' }, array);

        // Verify it's a copy
        array[0] = 'Z';
        assertEquals('A', tuple._1);
    }

    @Test
    public void testToList() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        CharList list = tuple.toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals('A', list.get(0));
        assertEquals('B', list.get(1));
        assertEquals('C', list.get(2));
    }

    @Test
    public void testStream() {
        CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        int sum = tuple.stream().sum();
        assertEquals(198, sum); // 65 + 66 + 67
    }

    @Test
    public void testHashCode_consistency() {
        CharTuple2 tuple1 = CharTuple.of('A', 'B');
        CharTuple2 tuple2 = CharTuple.of('A', 'B');
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testEquals_symmetry() {
        CharTuple2 tuple1 = CharTuple.of('A', 'B');
        CharTuple2 tuple2 = CharTuple.of('A', 'B');
        assertEquals(tuple1, tuple2);
        assertEquals(tuple2, tuple1);
    }

    @Test
    public void testEquals_differentTypes() {
        CharTuple2 tuple2 = CharTuple.of('A', 'B');
        CharTuple3 tuple3 = CharTuple.of('A', 'B', 'C');
        assertNotEquals(tuple2, tuple3);
    }

    @Test
    public void testDigitCharacters() {
        CharTuple3 tuple = CharTuple.of('1', '2', '3');
        assertEquals('1', tuple.min());
        assertEquals('3', tuple.max());
        assertEquals('2', tuple.median());
    }

    @Test
    public void testSpecialCharacters() {
        CharTuple3 tuple = CharTuple.of('!', '@', '#');
        assertTrue(tuple.contains('!'));
        assertTrue(tuple.contains('@'));
        assertTrue(tuple.contains('#'));
    }

    @Test
    public void testMinMaxMedian_largerTuple() {
        CharTuple5 tuple = CharTuple.of('E', 'B', 'D', 'A', 'C');
        assertEquals('A', tuple.min());
        assertEquals('E', tuple.max());
        assertEquals('C', tuple.median());
    }

    @Test
    public void testLowercaseCharacters() {
        CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertEquals('a', tuple.min());
        assertEquals('c', tuple.max());
        assertEquals(294, tuple.sum()); // 97 + 98 + 99
    }

    @Test
    public void testMixedCaseCharacters() {
        CharTuple2 tuple = CharTuple.of('A', 'a');
        assertEquals('A', tuple.min()); // 'A' = 65, 'a' = 97
        assertEquals('a', tuple.max());
    }
}
