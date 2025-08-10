package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.u.Optional;

public class BooleanTupleTest extends TestBase {

    @Test
    public void testOf1() {
        BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
        assertEquals(1, tuple.arity());
        assertTrue(tuple._1);
    }

    @Test
    public void testOf2() {
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        assertEquals(2, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
    }

    @Test
    public void testOf3() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        assertEquals(3, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
        assertTrue(tuple._3);
    }

    @Test
    public void testOf4() {
        BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        assertEquals(4, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
        assertTrue(tuple._3);
        assertFalse(tuple._4);
    }

    @Test
    public void testOf5() {
        BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        assertEquals(5, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
        assertTrue(tuple._3);
        assertFalse(tuple._4);
        assertTrue(tuple._5);
    }

    @Test
    public void testOf6() {
        BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
        assertEquals(6, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
        assertTrue(tuple._3);
        assertFalse(tuple._4);
        assertTrue(tuple._5);
        assertFalse(tuple._6);
    }

    @Test
    public void testOf7() {
        BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
        assertEquals(7, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
        assertTrue(tuple._3);
        assertFalse(tuple._4);
        assertTrue(tuple._5);
        assertFalse(tuple._6);
        assertTrue(tuple._7);
    }

    @Test
    public void testOf8() {
        BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertEquals(8, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
        assertTrue(tuple._3);
        assertFalse(tuple._4);
        assertTrue(tuple._5);
        assertFalse(tuple._6);
        assertTrue(tuple._7);
        assertFalse(tuple._8);
    }

    @Test
    public void testOf9() {
        BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertEquals(9, tuple.arity());
        assertTrue(tuple._1);
        assertFalse(tuple._2);
        assertTrue(tuple._3);
        assertFalse(tuple._4);
        assertTrue(tuple._5);
        assertFalse(tuple._6);
        assertTrue(tuple._7);
        assertFalse(tuple._8);
        assertTrue(tuple._9);
    }

    @Test
    public void testEquals() {
        BooleanTuple.BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple.BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        BooleanTuple.BooleanTuple3 tuple3 = BooleanTuple.of(false, true, false);

        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertNotEquals(tuple1, null);
    }

    @Test
    public void testHashCode() {
        BooleanTuple.BooleanTuple3 tuple1 = BooleanTuple.of(true, false, true);
        BooleanTuple.BooleanTuple3 tuple2 = BooleanTuple.of(true, false, true);
        BooleanTuple.BooleanTuple3 tuple3 = BooleanTuple.of(false, true, false);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
    }

    @Test
    public void testToString() {
        BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        String result = tuple.toString();
        assertTrue(result.contains("true"));
        assertTrue(result.contains("false"));
    }

    @Test
    public void testReverse() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, false);
        BooleanTuple.BooleanTuple3 reversed = tuple.reverse();

        assertFalse(reversed._1);
        assertFalse(reversed._2);
        assertTrue(reversed._3);
    }

    @Test
    public void testContains() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);

        assertTrue(tuple.contains(true));
        assertTrue(tuple.contains(false));
    }

    @Test
    public void testToArray() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        boolean[] array = tuple.toArray();

        assertEquals(3, array.length);
        assertTrue(array[0]);
        assertFalse(array[1]);
        assertTrue(array[2]);
    }

    @Test
    public void testStream() {
        BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        long trueCount = tuple.stream().filter(b -> b).count();

        assertEquals(2, trueCount);
    }

    // Additional comprehensive tests for missing functionality

    @Test
    public void testHighArityTupleEqualsAndHashCode() {
        // Test BooleanTuple4
        BooleanTuple.BooleanTuple4 tuple4a = BooleanTuple.of(true, false, true, false);
        BooleanTuple.BooleanTuple4 tuple4b = BooleanTuple.of(true, false, true, false);
        BooleanTuple.BooleanTuple4 tuple4c = BooleanTuple.of(true, false, true, true);

        assertEquals(tuple4a, tuple4b);
        assertNotEquals(tuple4a, tuple4c);
        assertEquals(tuple4a.hashCode(), tuple4b.hashCode());

        // Test BooleanTuple5
        BooleanTuple.BooleanTuple5 tuple5a = BooleanTuple.of(true, false, true, false, true);
        BooleanTuple.BooleanTuple5 tuple5b = BooleanTuple.of(true, false, true, false, true);
        BooleanTuple.BooleanTuple5 tuple5c = BooleanTuple.of(true, false, true, false, false);

        assertEquals(tuple5a, tuple5b);
        assertNotEquals(tuple5a, tuple5c);
        assertEquals(tuple5a.hashCode(), tuple5b.hashCode());
    }

    @Test
    public void testHighArityTupleToString() {
        BooleanTuple.BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
        String str4 = tuple4.toString();
        assertTrue(str4.contains("true"));
        assertTrue(str4.contains("false"));

        BooleanTuple.BooleanTuple6 tuple6 = BooleanTuple.of(true, false, true, false, true, false);
        String str6 = tuple6.toString();
        assertTrue(str6.contains("true"));
        assertTrue(str6.contains("false"));
    }

    @Test
    public void testHighArityTupleReverse() {
        BooleanTuple.BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
        BooleanTuple.BooleanTuple4 reversed4 = tuple4.reverse();

        assertEquals(false, reversed4._1);
        assertEquals(true, reversed4._2);
        assertEquals(false, reversed4._3);
        assertEquals(true, reversed4._4);

        BooleanTuple.BooleanTuple5 tuple5 = BooleanTuple.of(true, false, true, false, true);
        BooleanTuple.BooleanTuple5 reversed5 = tuple5.reverse();

        assertEquals(true, reversed5._1);
        assertEquals(false, reversed5._2);
        assertEquals(true, reversed5._3);
        assertEquals(false, reversed5._4);
        assertEquals(true, reversed5._5);
    }

    @Test
    public void testHighArityTupleContains() {
        BooleanTuple.BooleanTuple6 tuple6 = BooleanTuple.of(true, false, true, false, true, false);

        assertTrue(tuple6.contains(true));
        assertTrue(tuple6.contains(false));

        BooleanTuple.BooleanTuple7 allTrue = BooleanTuple.of(true, true, true, true, true, true, true);
        assertTrue(allTrue.contains(true));
        assertFalse(allTrue.contains(false));
    }

    @Test
    public void testCompleteHighArityTuples() {
        // Test all tuple arities 4-9 for basic functionality
        BooleanTuple.BooleanTuple4 tuple4 = BooleanTuple.of(true, false, true, false);
        assertEquals(4, tuple4.arity());
        assertEquals(4, tuple4.toArray().length);

        BooleanTuple.BooleanTuple5 tuple5 = BooleanTuple.of(true, false, true, false, true);
        assertEquals(5, tuple5.arity());
        assertEquals(5, tuple5.toArray().length);

        BooleanTuple.BooleanTuple6 tuple6 = BooleanTuple.of(true, false, true, false, true, false);
        assertEquals(6, tuple6.arity());
        assertEquals(6, tuple6.toArray().length);

        BooleanTuple.BooleanTuple7 tuple7 = BooleanTuple.of(true, false, true, false, true, false, true);
        assertEquals(7, tuple7.arity());
        assertEquals(7, tuple7.toArray().length);

        BooleanTuple.BooleanTuple8 tuple8 = BooleanTuple.of(true, false, true, false, true, false, true, false);
        assertEquals(8, tuple8.arity());
        assertEquals(8, tuple8.toArray().length);

        BooleanTuple.BooleanTuple9 tuple9 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertEquals(9, tuple9.arity());
        assertEquals(9, tuple9.toArray().length);
    }

    @Test
    public void testEqualsDifferentTupleTypes() {
        BooleanTuple.BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        BooleanTuple.BooleanTuple3 tuple3 = BooleanTuple.of(true, false, true);

        assertNotEquals(tuple2, tuple3);
        assertNotEquals(tuple2, null);
        assertNotEquals(tuple2, "not a tuple");
    }

    @Test
    public void testSelfEquality() {
        // Test that all tuple types equal themselves
        BooleanTuple.BooleanTuple1 tuple1 = BooleanTuple.of(true);
        BooleanTuple.BooleanTuple2 tuple2 = BooleanTuple.of(true, false);
        BooleanTuple.BooleanTuple3 tuple3 = BooleanTuple.of(true, false, true);

        assertEquals(tuple1, tuple1);
        assertEquals(tuple2, tuple2);
        assertEquals(tuple3, tuple3);
    }

    @Test
    public void testStreamOperationsOnHighArityTuples() {
        BooleanTuple.BooleanTuple5 tuple5 = BooleanTuple.of(true, false, true, true, false);

        long trueCount = tuple5.stream().filter(b -> b).count();
        assertEquals(3, trueCount);

        long falseCount = tuple5.stream().filter(b -> !b).count();
        assertEquals(2, falseCount);
    }

}