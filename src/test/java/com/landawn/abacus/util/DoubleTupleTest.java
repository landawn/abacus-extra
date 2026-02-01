package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

public class DoubleTupleTest extends TestBase {

    private static final double DELTA = 0.0001;

    @Test
    public void testOf1() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.5);
        assertEquals(1, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
    }

    @Test
    public void testOf2() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertEquals(2, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
    }

    @Test
    public void testOf3() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.5, 2.5, 3.5);
        assertEquals(3, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
    }

    @Test
    public void testOf4() {
        DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5);
        assertEquals(4, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
    }

    @Test
    public void testOf5() {
        DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5);
        assertEquals(5, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
        assertEquals(5.5, tuple._5, DELTA);
    }

    @Test
    public void testOf6() {
        DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5);
        assertEquals(6, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
        assertEquals(5.5, tuple._5, DELTA);
        assertEquals(6.5, tuple._6, DELTA);
    }

    @Test
    public void testOf7() {
        DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5);
        assertEquals(7, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
        assertEquals(5.5, tuple._5, DELTA);
        assertEquals(6.5, tuple._6, DELTA);
        assertEquals(7.5, tuple._7, DELTA);
    }

    @Test
    public void testOf8() {
        DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5);
        assertEquals(8, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
        assertEquals(5.5, tuple._5, DELTA);
        assertEquals(6.5, tuple._6, DELTA);
        assertEquals(7.5, tuple._7, DELTA);
        assertEquals(8.5, tuple._8, DELTA);
    }

    @Test
    public void testOf9() {
        DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5);
        assertEquals(9, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
        assertEquals(5.5, tuple._5, DELTA);
        assertEquals(6.5, tuple._6, DELTA);
        assertEquals(7.5, tuple._7, DELTA);
        assertEquals(8.5, tuple._8, DELTA);
        assertEquals(9.5, tuple._9, DELTA);
    }

    @Test
    public void testEquals() {
        DoubleTuple.DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 4.0);

        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertNotEquals(tuple1, null);
    }

    @Test
    public void testHashCode() {
        DoubleTuple.DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 4.0);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
    }

    @Test
    public void testToString() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        String result = tuple.toString();
        assertTrue(result.contains("1.5"));
        assertTrue(result.contains("2.5"));
    }

    @Test
    public void testReverse() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 reversed = tuple.reverse();

        assertEquals(3.0, reversed._1, DELTA);
        assertEquals(2.0, reversed._2, DELTA);
        assertEquals(1.0, reversed._3, DELTA);
    }

    @Test
    public void testContains() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.5, 2.5, 3.5);

        assertTrue(tuple.contains(1.5));
        assertTrue(tuple.contains(2.5));
        assertTrue(tuple.contains(3.5));
        assertFalse(tuple.contains(4.5));
    }

    @Test
    public void testToArray() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.5, 2.5, 3.5);
        double[] array = tuple.toArray();

        assertEquals(3, array.length);
        assertEquals(1.5, array[0], DELTA);
        assertEquals(2.5, array[1], DELTA);
        assertEquals(3.5, array[2], DELTA);
    }

    @Test
    public void testStream() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double sum = tuple.stream().sum();

        assertEquals(6.0, sum, DELTA);
    }

    @Test
    public void testMin() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.5, 1.5, 2.5);
        assertEquals(1.5, tuple.min(), DELTA);
    }

    @Test
    public void testMax() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.5, 1.5, 2.5);
        assertEquals(3.5, tuple.max(), DELTA);
    }

    @Test
    public void testSum() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.5, 2.5, 3.0);
        assertEquals(7.0, tuple.sum(), DELTA);
    }

    @Test
    public void testAverage() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(2.0, tuple.average(), DELTA);
    }

    @Test
    public void testMedian() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(2.0, tuple.median(), DELTA);

        DoubleTuple.DoubleTuple4 evenTuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(2.0, evenTuple.median(), DELTA);
    }
}
