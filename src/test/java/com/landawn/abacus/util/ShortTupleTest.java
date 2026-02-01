package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

public class ShortTupleTest extends TestBase {

    @Test
    public void testOf1() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 5);
        assertEquals(1, tuple.arity());
        assertEquals(5, tuple._1);
    }

    @Test
    public void testOf2() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 10);
        assertEquals(2, tuple.arity());
        assertEquals(5, tuple._1);
        assertEquals(10, tuple._2);
    }

    @Test
    public void testOf3() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
        assertEquals(3, tuple.arity());
        assertEquals(5, tuple._1);
        assertEquals(10, tuple._2);
        assertEquals(15, tuple._3);
    }

    @Test
    public void testOf4() {
        ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20);
        assertEquals(4, tuple.arity());
        assertEquals(5, tuple._1);
        assertEquals(10, tuple._2);
        assertEquals(15, tuple._3);
        assertEquals(20, tuple._4);
    }

    @Test
    public void testOf5() {
        ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25);
        assertEquals(5, tuple.arity());
        assertEquals(5, tuple._1);
        assertEquals(10, tuple._2);
        assertEquals(15, tuple._3);
        assertEquals(20, tuple._4);
        assertEquals(25, tuple._5);
    }

    @Test
    public void testOf6() {
        ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30);
        assertEquals(6, tuple.arity());
        assertEquals(5, tuple._1);
        assertEquals(10, tuple._2);
        assertEquals(15, tuple._3);
        assertEquals(20, tuple._4);
        assertEquals(25, tuple._5);
        assertEquals(30, tuple._6);
    }

    @Test
    public void testOf7() {
        ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35);
        assertEquals(7, tuple.arity());
        assertEquals(5, tuple._1);
        assertEquals(10, tuple._2);
        assertEquals(15, tuple._3);
        assertEquals(20, tuple._4);
        assertEquals(25, tuple._5);
        assertEquals(30, tuple._6);
        assertEquals(35, tuple._7);
    }

    @Test
    public void testOf8() {
        ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35, (short) 40);
        assertEquals(8, tuple.arity());
        assertEquals(5, tuple._1);
        assertEquals(10, tuple._2);
        assertEquals(15, tuple._3);
        assertEquals(20, tuple._4);
        assertEquals(25, tuple._5);
        assertEquals(30, tuple._6);
        assertEquals(35, tuple._7);
        assertEquals(40, tuple._8);
    }

    @Test
    public void testOf9() {
        ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35, (short) 40, (short) 45);
        assertEquals(9, tuple.arity());
        assertEquals(5, tuple._1);
        assertEquals(10, tuple._2);
        assertEquals(15, tuple._3);
        assertEquals(20, tuple._4);
        assertEquals(25, tuple._5);
        assertEquals(30, tuple._6);
        assertEquals(35, tuple._7);
        assertEquals(40, tuple._8);
        assertEquals(45, tuple._9);
    }

    @Test
    public void testCreate() {
        // Test empty array
        ShortTuple<?> empty = ShortTuple.create(null);
        assertEquals(0, empty.arity());

        empty = ShortTuple.create(new short[0]);
        assertEquals(0, empty.arity());

        // Test array with 1 element
        ShortTuple.ShortTuple1 tuple1 = ShortTuple.create(new short[] { 5 });
        assertEquals(1, tuple1.arity());
        assertEquals(5, tuple1._1);

        // Test array with 5 elements
        ShortTuple.ShortTuple5 tuple5 = ShortTuple.create(new short[] { 5, 10, 15, 20, 25 });
        assertEquals(5, tuple5.arity());
        assertEquals(25, tuple5._5);

        // Test array with 9 elements
        ShortTuple.ShortTuple9 tuple9 = ShortTuple.create(new short[] { 5, 10, 15, 20, 25, 30, 35, 40, 45 });
        assertEquals(9, tuple9.arity());
        assertEquals(45, tuple9._9);

        // Test too many elements
        assertThrows(IllegalArgumentException.class, () -> ShortTuple.create(new short[10]));
    }

    @Test
    public void testMin() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 15, (short) 5, (short) 10);
        assertEquals(5, tuple.min());

        ShortTuple.ShortTuple0 empty = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> empty.min());
    }

    @Test
    public void testMax() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 15, (short) 5, (short) 10);
        assertEquals(15, tuple.max());

        ShortTuple.ShortTuple0 empty = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> empty.max());
    }

    @Test
    public void testMedian() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 15, (short) 5, (short) 10);
        assertEquals(10, tuple.median());

        ShortTuple.ShortTuple4 evenTuple = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20);
        assertEquals((short) 10, evenTuple.median());   // Should be (10 + 15) / 2 = 12

        ShortTuple.ShortTuple0 empty = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> empty.median());
    }

    @Test
    public void testSum() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
        assertEquals(30, tuple.sum());

        ShortTuple.ShortTuple0 empty = ShortTuple.create(new short[0]);
        assertEquals(0, empty.sum());
    }

    @Test
    public void testAverage() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
        assertEquals(10.0, tuple.average());

        ShortTuple.ShortTuple0 empty = ShortTuple.create(new short[0]);
        assertThrows(NoSuchElementException.class, () -> empty.average());
    }

    @Test
    public void testReverse() {
        // Test Tuple0
        ShortTuple.ShortTuple0 empty = ShortTuple.create(new short[0]);
        ShortTuple.ShortTuple0 reversedEmpty = empty.reverse();
        assertEquals(0, reversedEmpty.arity());

        // Test Tuple1
        ShortTuple.ShortTuple1 tuple1 = ShortTuple.of((short) 5);
        ShortTuple.ShortTuple1 reversed1 = tuple1.reverse();
        assertEquals(5, reversed1._1);

        // Test Tuple2
        ShortTuple.ShortTuple2 tuple2 = ShortTuple.of((short) 5, (short) 10);
        ShortTuple.ShortTuple2 reversed2 = tuple2.reverse();
        assertEquals(10, reversed2._1);
        assertEquals(5, reversed2._2);

        // Test Tuple3
        ShortTuple.ShortTuple3 tuple3 = ShortTuple.of((short) 5, (short) 10, (short) 15);
        ShortTuple.ShortTuple3 reversed3 = tuple3.reverse();
        assertEquals(15, reversed3._1);
        assertEquals(10, reversed3._2);
        assertEquals(5, reversed3._3);

        // Test larger tuples
        ShortTuple.ShortTuple5 tuple5 = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25);
        ShortTuple.ShortTuple5 reversed5 = tuple5.reverse();
        assertEquals(25, reversed5._1);
        assertEquals(20, reversed5._2);
        assertEquals(15, reversed5._3);
        assertEquals(10, reversed5._4);
        assertEquals(5, reversed5._5);
    }

    @Test
    public void testContains() {
        // Test Tuple0
        ShortTuple.ShortTuple0 empty = ShortTuple.create(new short[0]);
        assertFalse(empty.contains((short) 5));

        // Test Tuple1
        ShortTuple.ShortTuple1 tuple1 = ShortTuple.of((short) 5);
        assertTrue(tuple1.contains((short) 5));
        assertFalse(tuple1.contains((short) 10));

        // Test Tuple3
        ShortTuple.ShortTuple3 tuple3 = ShortTuple.of((short) 5, (short) 10, (short) 15);
        assertTrue(tuple3.contains((short) 5));
        assertTrue(tuple3.contains((short) 10));
        assertTrue(tuple3.contains((short) 15));
        assertFalse(tuple3.contains((short) 20));

        // Test larger tuples
        ShortTuple.ShortTuple7 tuple7 = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20, (short) 25, (short) 30, (short) 35);
        assertTrue(tuple7.contains((short) 35));
        assertFalse(tuple7.contains((short) 40));
    }

    @Test
    public void testToArray() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
        short[] array = tuple.toArray();
        assertEquals(3, array.length);
        assertEquals(5, array[0]);
        assertEquals(10, array[1]);
        assertEquals(15, array[2]);

        // Ensure it's a copy
        array[0] = 50;
        assertEquals(5, tuple._1);
    }

    @Test
    public void testToList() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
        ShortList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(5, list.get(0));
        assertEquals(10, list.get(1));
        assertEquals(15, list.get(2));
    }

    @Test
    public void testForEach() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
        int[] sum = { 0 };
        tuple.forEach(s -> sum[0] += s);
        assertEquals(30, sum[0]);

        // Test Tuple2 forEach
        ShortTuple.ShortTuple2 tuple2 = ShortTuple.of((short) 5, (short) 10);
        int[] count = { 0 };
        tuple2.forEach(s -> count[0]++);
        assertEquals(2, count[0]);
    }

    @Test
    public void testStream() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
        short[] array = tuple.stream().toArray();
        assertEquals(3, array.length);
        assertEquals(5, array[0]);
        assertEquals(10, array[1]);
        assertEquals(15, array[2]);

        long count = tuple.stream().filter(s -> s > 7).count();
        assertEquals(2, count);
    }

    @Test
    public void testHashCode() {
        ShortTuple.ShortTuple3 tuple1 = ShortTuple.of((short) 5, (short) 10, (short) 15);
        ShortTuple.ShortTuple3 tuple2 = ShortTuple.of((short) 5, (short) 10, (short) 15);
        ShortTuple.ShortTuple3 tuple3 = ShortTuple.of((short) 5, (short) 10, (short) 16);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());

        // Test specific hashcodes for coverage
        ShortTuple.ShortTuple1 single = ShortTuple.of((short) 5);
        assertEquals(5, single.hashCode());

        ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 5, (short) 10);
        assertEquals(31 * 5 + 10, pair.hashCode());
    }

    @Test
    public void testEquals() {
        ShortTuple.ShortTuple3 tuple1 = ShortTuple.of((short) 5, (short) 10, (short) 15);
        ShortTuple.ShortTuple3 tuple2 = ShortTuple.of((short) 5, (short) 10, (short) 15);
        ShortTuple.ShortTuple3 tuple3 = ShortTuple.of((short) 5, (short) 10, (short) 16);
        ShortTuple.ShortTuple2 tuple4 = ShortTuple.of((short) 5, (short) 10);

        assertTrue(tuple1.equals(tuple1));
        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
        assertFalse(tuple1.equals(tuple4));
        assertFalse(tuple1.equals(null));
        assertFalse(tuple1.equals("not a tuple"));

        // Test specific equals for coverage
        ShortTuple.ShortTuple1 single1 = ShortTuple.of((short) 5);
        ShortTuple.ShortTuple1 single2 = ShortTuple.of((short) 5);
        ShortTuple.ShortTuple1 single3 = ShortTuple.of((short) 10);
        assertTrue(single1.equals(single2));
        assertFalse(single1.equals(single3));
        assertFalse(single1.equals("not a tuple"));

        ShortTuple.ShortTuple2 pair1 = ShortTuple.of((short) 5, (short) 10);
        ShortTuple.ShortTuple2 pair2 = ShortTuple.of((short) 5, (short) 10);
        ShortTuple.ShortTuple2 pair3 = ShortTuple.of((short) 5, (short) 11);
        assertTrue(pair1.equals(pair2));
        assertFalse(pair1.equals(pair3));
    }

    @Test
    public void testToString() {
        ShortTuple.ShortTuple0 empty = ShortTuple.create(new short[0]);
        assertEquals("()", empty.toString());

        ShortTuple.ShortTuple1 single = ShortTuple.of((short) 5);
        assertEquals("(5)", single.toString());

        ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 5, (short) 10);
        assertEquals("(5, 10)", pair.toString());

        ShortTuple.ShortTuple3 triple = ShortTuple.of((short) 5, (short) 10, (short) 15);
        assertEquals("(5, 10, 15)", triple.toString());
    }

    @Test
    public void testTuple2SpecificMethods() {
        ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 10);

        // Test accept
        int[] sum = { 0 };
        tuple.accept((a, b) -> sum[0] = a + b);
        assertEquals(15, sum[0]);

        // Test map
        int result = tuple.map((a, b) -> a + b);
        assertEquals(15, result);

        // Test filter
        assertTrue(tuple.filter((a, b) -> a < b).isPresent());
        assertFalse(tuple.filter((a, b) -> a > b).isPresent());
    }

    @Test
    public void testTuple3SpecificMethods() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);

        // Test accept
        int[] sum = { 0 };
        tuple.accept((a, b, c) -> sum[0] = a + b + c);
        assertEquals(30, sum[0]);

        // Test map
        int result = tuple.map((a, b, c) -> a + b + c);
        assertEquals(30, result);

        // Test filter
        assertTrue(tuple.filter((a, b, c) -> a < b && b < c).isPresent());
        assertFalse(tuple.filter((a, b, c) -> a > b).isPresent());
    }

    @Test
    public void testTuple1SpecificMethods() {
        ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 5);

        // Test min/max/median/sum/average
        assertEquals(5, tuple.min());
        assertEquals(5, tuple.max());
        assertEquals(5, tuple.median());
        assertEquals(5, tuple.sum());
        assertEquals(5.0, tuple.average());
    }

    @Test
    public void testElementsMethod() {
        // Just test that elements are cached properly
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
        short[] elements1 = tuple.elements();
        short[] elements2 = tuple.elements();
        assertSame(elements1, elements2);   // Should return same cached array
    }
}
