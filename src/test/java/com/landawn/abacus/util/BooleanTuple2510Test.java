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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
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

@Tag("2510")
public class BooleanTuple2510Test extends TestBase {

    // ============ Factory Method Tests - BooleanTuple.of() ============

    @Test
    public void testOf_tuple1() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testOf_tuple2() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testOf_tuple3() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testOf_tuple4() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertNotNull(tuple);
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
        assertEquals(false, tuple._4);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testOf_tuple5() {
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
    public void testOf_tuple6() {
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
    public void testOf_tuple7() {
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
    public void testOf_tuple8() {
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
    public void testOf_tuple9() {
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
    public void testCreate_nullArray() {
        BooleanTuple<?> tuple = BooleanTuple.create(null);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_emptyArray() {
        BooleanTuple<?> tuple = BooleanTuple.create(new boolean[0]);
        assertNotNull(tuple);
        assertEquals(0, tuple.arity());
    }

    @Test
    public void testCreate_array1() {
        BooleanTuple1 tuple = BooleanTuple.create(new boolean[] { true });
        assertNotNull(tuple);
        assertEquals(1, tuple.arity());
        assertEquals(true, tuple._1);
    }

    @Test
    public void testCreate_array2() {
        BooleanTuple2 tuple = BooleanTuple.create(new boolean[] { true, false });
        assertNotNull(tuple);
        assertEquals(2, tuple.arity());
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
    }

    @Test
    public void testCreate_array3() {
        BooleanTuple3 tuple = BooleanTuple.create(new boolean[] { true, false, true });
        assertNotNull(tuple);
        assertEquals(3, tuple.arity());
        assertEquals(true, tuple._1);
        assertEquals(false, tuple._2);
        assertEquals(true, tuple._3);
    }

    @Test
    public void testCreate_array4() {
        BooleanTuple4 tuple = BooleanTuple.create(new boolean[] { true, false, true, false });
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testCreate_array5() {
        BooleanTuple5 tuple = BooleanTuple.create(new boolean[] { true, false, true, false, true });
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testCreate_array6() {
        BooleanTuple6 tuple = BooleanTuple.create(new boolean[] { true, false, true, false, true, false });
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testCreate_array7() {
        BooleanTuple7 tuple = BooleanTuple.create(new boolean[] { true, false, true, false, true, false, true });
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testCreate_array8() {
        BooleanTuple8 tuple = BooleanTuple.create(new boolean[] { true, false, true, false, true, false, true, false });
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testCreate_array9() {
        BooleanTuple9 tuple = BooleanTuple.create(new boolean[] { true, false, true, false, true, false, true, false, true });
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testCreate_arrayTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> {
            BooleanTuple.create(new boolean[10]);
        });
    }

    // ============ BooleanTuple1 Tests ============

    @Test
    public void testTuple1_arity() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals(1, tuple.arity());
    }

    @Test
    public void testTuple1_reverse() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        BooleanTuple1 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
        assertNotSame(tuple, reversed);
    }

    @Test
    public void testTuple1_contains_true() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testTuple1_contains_false() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertFalse(tuple.contains(false));
    }

    @Test
    public void testTuple1_hashCode() {
        BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple1 tuple2 = BooleanTuple.of(true);
        BooleanTuple1 tuple3 = BooleanTuple.of(false);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertEquals(1231, tuple1.hashCode());
        assertEquals(1237, tuple3.hashCode());
    }

    @Test
    public void testTuple1_equals() {
        BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple1 tuple2 = BooleanTuple.of(true);
        BooleanTuple1 tuple3 = BooleanTuple.of(false);

        assertEquals(tuple1, tuple1);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertNotEquals(tuple1, null);
        assertNotEquals(tuple1, "not a tuple");
    }

    @Test
    public void testTuple1_toString() {
        BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals("[true]", tuple.toString());
    }

    // ============ BooleanTuple2 Tests ============

    @Test
    public void testTuple2_arity() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertEquals(2, tuple.arity());
    }

    @Test
    public void testTuple2_reverse() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        BooleanTuple2 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
        assertEquals(true, reversed._2);
    }

    @Test
    public void testTuple2_contains_firstElement() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertTrue(tuple.contains(true));
    }

    @Test
    public void testTuple2_contains_secondElement() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testTuple2_contains_notFound() {
        BooleanTuple2 tuple = BooleanTuple.of(true, true);
        assertTrue(tuple.contains(true));
        assertFalse(tuple.contains(false));
    }

    @Test
    public void testTuple2_forEach() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        List<Boolean> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(2, values.size());
        assertEquals(true, values.get(0));
        assertEquals(false, values.get(1));
    }

    @Test
    public void testTuple2_accept() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        List<String> results = new ArrayList<>();
        tuple.accept((a, b) -> results.add(a + "," + b));
        assertEquals(1, results.size());
        assertEquals("true,false", results.get(0));
    }

    @Test
    public void testTuple2_map() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        Boolean result = tuple.map((a, b) -> a && b);
        assertFalse(result);
    }

    @Test
    public void testTuple2_filter_matches() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        Optional<BooleanTuple2> result = tuple.filter((a, b) -> a || b);
        assertTrue(result.isPresent());
        assertEquals(tuple, result.get());
    }

    @Test
    public void testTuple2_filter_noMatch() {
        BooleanTuple2 tuple = BooleanTuple.of(false, false);
        Optional<BooleanTuple2> result = tuple.filter((a, b) -> a && b);
        assertFalse(result.isPresent());
    }

    @Test
    public void testTuple2_hashCode() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple3 = BooleanTuple.of(false, true);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
    }

    @Test
    public void testTuple2_equals() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple3 = BooleanTuple.of(false, true);

        assertEquals(tuple1, tuple1);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertNotEquals(tuple1, null);
    }

    @Test
    public void testTuple2_toString() {
        BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertEquals("[true, false]", tuple.toString());
    }

    // ============ BooleanTuple3 Tests ============

    @Test
    public void testTuple3_arity() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals(3, tuple.arity());
    }

    @Test
    public void testTuple3_reverse() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, false);
        BooleanTuple3 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
        assertEquals(false, reversed._2);
        assertEquals(true, reversed._3);
    }

    @Test
    public void testTuple3_contains() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testTuple3_forEach() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        List<Boolean> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(3, values.size());
    }

    @Test
    public void testTuple3_accept() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        List<String> results = new ArrayList<>();
        tuple.accept((a, b, c) -> results.add(a + "," + b + "," + c));
        assertEquals(1, results.size());
        assertEquals("true,false,true", results.get(0));
    }

    @Test
    public void testTuple3_map() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        Boolean result = tuple.map((a, b, c) -> a || b || c);
        assertTrue(result);
    }

    @Test
    public void testTuple3_filter_matches() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a && c);
        assertTrue(result.isPresent());
    }

    @Test
    public void testTuple3_filter_noMatch() {
        BooleanTuple3 tuple = BooleanTuple.of(false, false, false);
        Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a || b || c);
        assertFalse(result.isPresent());
    }

    @Test
    public void testTuple3_hashCode() {
        BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testTuple3_equals() {
        BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        BooleanTuple3 tuple3 = BooleanTuple.of(false, true, false);

        assertEquals(tuple1, tuple1);
        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
    }

    @Test
    public void testTuple3_toString() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals("[true, false, true]", tuple.toString());
    }

    // ============ BooleanTuple4 Tests ============

    @Test
    public void testTuple4_arity() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertEquals(4, tuple.arity());
    }

    @Test
    public void testTuple4_reverse() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        BooleanTuple4 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
        assertEquals(true, reversed._2);
        assertEquals(false, reversed._3);
        assertEquals(true, reversed._4);
    }

    @Test
    public void testTuple4_contains() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testTuple4_forEach() {
        BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        List<Boolean> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(4, values.size());
    }

    // ============ BooleanTuple5 Tests ============

    @Test
    public void testTuple5_arity() {
        BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        assertEquals(5, tuple.arity());
    }

    @Test
    public void testTuple5_reverse() {
        BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        BooleanTuple5 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
        assertEquals(false, reversed._2);
        assertEquals(true, reversed._3);
        assertEquals(false, reversed._4);
        assertEquals(true, reversed._5);
    }

    @Test
    public void testTuple5_contains() {
        BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testTuple5_forEach() {
        BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        List<Boolean> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(5, values.size());
    }

    // ============ BooleanTuple6 Tests ============

    @Test
    public void testTuple6_arity() {
        BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        assertEquals(6, tuple.arity());
    }

    @Test
    public void testTuple6_reverse() {
        BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        BooleanTuple6 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
        assertEquals(true, reversed._2);
    }

    @Test
    public void testTuple6_contains() {
        BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testTuple6_forEach() {
        BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        List<Boolean> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(6, values.size());
    }

    // ============ BooleanTuple7 Tests ============

    @Test
    public void testTuple7_arity() {
        BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        assertEquals(7, tuple.arity());
    }

    @Test
    public void testTuple7_reverse() {
        BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        BooleanTuple7 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
        assertEquals(false, reversed._2);
    }

    @Test
    public void testTuple7_contains() {
        BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testTuple7_forEach() {
        BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        List<Boolean> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(7, values.size());
    }

    // ============ BooleanTuple8 Tests ============

    @Test
    public void testTuple8_arity() {
        BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertEquals(8, tuple.arity());
    }

    @Test
    public void testTuple8_reverse() {
        BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        BooleanTuple8 reversed = tuple.reverse();
        assertEquals(false, reversed._1);
        assertEquals(true, reversed._2);
    }

    @Test
    public void testTuple8_contains() {
        BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testTuple8_forEach() {
        BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        List<Boolean> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(8, values.size());
    }

    // ============ BooleanTuple9 Tests ============

    @Test
    public void testTuple9_arity() {
        BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertEquals(9, tuple.arity());
    }

    @Test
    public void testTuple9_reverse() {
        BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        BooleanTuple9 reversed = tuple.reverse();
        assertEquals(true, reversed._1);
        assertEquals(false, reversed._2);
    }

    @Test
    public void testTuple9_contains() {
        BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testTuple9_forEach() {
        BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        List<Boolean> values = new ArrayList<>();
        tuple.forEach(values::add);
        assertEquals(9, values.size());
    }

    // ============ Common Method Tests (inherited from BooleanTuple) ============

    @Test
    public void testToArray() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        boolean[] array = tuple.toArray();
        assertArrayEquals(new boolean[] { true, false, true }, array);

        // Verify it's a copy
        array[0] = false;
        assertEquals(true, tuple._1);
    }

    @Test
    public void testToList() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        BooleanList list = tuple.toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(true, list.get(0));
        assertEquals(false, list.get(1));
        assertEquals(true, list.get(2));
    }

    @Test
    public void testStream() {
        BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        long count = tuple.stream().filter(b -> b).count();
        assertEquals(2, count);
    }

    @Test
    public void testHashCode_consistency() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
    }

    @Test
    public void testEquals_symmetry() {
        BooleanTuple2 tuple1 = BooleanTuple.of(true, false);
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        assertEquals(tuple1, tuple2);
        assertEquals(tuple2, tuple1);
    }

    @Test
    public void testEquals_differentTypes() {
        BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        BooleanTuple3 tuple3 = BooleanTuple.of(true, false, true);
        assertNotEquals(tuple2, tuple3);
    }
}
