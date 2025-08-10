package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

public class CharTupleTest extends TestBase {

    @Test
    public void testOf1() {
        CharTuple.CharTuple1 tuple = CharTuple.of('a');
        assertEquals(1, tuple.arity());
        assertEquals('a', tuple._1);
    }

    @Test
    public void testOf2() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        assertEquals(2, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
    }

    @Test
    public void testOf3() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        assertEquals(3, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
    }

    @Test
    public void testOf4() {
        CharTuple.CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
        assertEquals(4, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
    }

    @Test
    public void testOf5() {
        CharTuple.CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
        assertEquals(5, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
        assertEquals('e', tuple._5);
    }

    @Test
    public void testOf6() {
        CharTuple.CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
        assertEquals(6, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
        assertEquals('e', tuple._5);
        assertEquals('f', tuple._6);
    }

    @Test
    public void testOf7() {
        CharTuple.CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        assertEquals(7, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
        assertEquals('e', tuple._5);
        assertEquals('f', tuple._6);
        assertEquals('g', tuple._7);
    }

    @Test
    public void testOf8() {
        CharTuple.CharTuple8 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        assertEquals(8, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
        assertEquals('e', tuple._5);
        assertEquals('f', tuple._6);
        assertEquals('g', tuple._7);
        assertEquals('h', tuple._8);
    }

    @Test
    public void testOf9() {
        CharTuple.CharTuple9 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
        assertEquals(9, tuple.arity());
        assertEquals('a', tuple._1);
        assertEquals('b', tuple._2);
        assertEquals('c', tuple._3);
        assertEquals('d', tuple._4);
        assertEquals('e', tuple._5);
        assertEquals('f', tuple._6);
        assertEquals('g', tuple._7);
        assertEquals('h', tuple._8);
        assertEquals('i', tuple._9);
    }

    @Test
    public void testEquals() {
        CharTuple.CharTuple3 tuple1 = CharTuple.of('x', 'y', 'z');
        CharTuple.CharTuple3 tuple2 = CharTuple.of('x', 'y', 'z');
        CharTuple.CharTuple3 tuple3 = CharTuple.of('a', 'b', 'c');

        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertNotEquals(tuple1, null);
    }

    @Test
    public void testHashCode() {
        CharTuple.CharTuple3 tuple1 = CharTuple.of('x', 'y', 'z');
        CharTuple.CharTuple3 tuple2 = CharTuple.of('x', 'y', 'z');
        CharTuple.CharTuple3 tuple3 = CharTuple.of('a', 'b', 'c');

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
    }

    @Test
    public void testToString() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'b');
        String result = tuple.toString();
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
    }

    @Test
    public void testReverse() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        CharTuple.CharTuple3 reversed = tuple.reverse();

        assertEquals('c', reversed._1);
        assertEquals('b', reversed._2);
        assertEquals('a', reversed._3);
    }

    @Test
    public void testContains() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');

        assertTrue(tuple.contains('a'));
        assertTrue(tuple.contains('b'));
        assertTrue(tuple.contains('c'));
        assertFalse(tuple.contains('x'));
    }

    @Test
    public void testToArray() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        char[] array = tuple.toArray();

        assertEquals(3, array.length);
        assertEquals('a', array[0]);
        assertEquals('b', array[1]);
        assertEquals('c', array[2]);
    }

    @Test
    public void testStream() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        long count = tuple.stream().count();

        assertEquals(3, count);
    }

    @Test
    public void testMin() {
        CharTuple.CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
        assertEquals('a', tuple.min());
    }

    @Test
    public void testMax() {
        CharTuple.CharTuple3 tuple = CharTuple.of('c', 'a', 'b');
        assertEquals('c', tuple.max());
    }

    @Test
    public void testSum() {
        CharTuple.CharTuple3 tuple = CharTuple.of('a', 'b', 'c');
        int sum = tuple.sum();
        assertEquals('a' + 'b' + 'c', sum);
    }

    @Test
    public void testAverage() {
        CharTuple.CharTuple2 tuple = CharTuple.of('a', 'c');
        double average = tuple.average();
        assertEquals(('a' + 'c') / 2.0, average, 0.001);
    }
}