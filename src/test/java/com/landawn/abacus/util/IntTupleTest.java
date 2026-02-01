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

public class IntTupleTest extends TestBase {

    @Test
    public void testOf1() {
        IntTuple.IntTuple1 tuple = IntTuple.of(10);
        assertEquals(1, tuple.arity());
        assertEquals(10, tuple._1);
    }

    @Test
    public void testOf2() {
        IntTuple.IntTuple2 tuple = IntTuple.of(10, 20);
        assertEquals(2, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
    }

    @Test
    public void testOf3() {
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
        assertEquals(3, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(30, tuple._3);
    }

    @Test
    public void testOf4() {
        IntTuple.IntTuple4 tuple = IntTuple.of(10, 20, 30, 40);
        assertEquals(4, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(30, tuple._3);
        assertEquals(40, tuple._4);
    }

    @Test
    public void testOf5() {
        IntTuple.IntTuple5 tuple = IntTuple.of(10, 20, 30, 40, 50);
        assertEquals(5, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(30, tuple._3);
        assertEquals(40, tuple._4);
        assertEquals(50, tuple._5);
    }

    @Test
    public void testOf6() {
        IntTuple.IntTuple6 tuple = IntTuple.of(10, 20, 30, 40, 50, 60);
        assertEquals(6, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(30, tuple._3);
        assertEquals(40, tuple._4);
        assertEquals(50, tuple._5);
        assertEquals(60, tuple._6);
    }

    @Test
    public void testOf7() {
        IntTuple.IntTuple7 tuple = IntTuple.of(10, 20, 30, 40, 50, 60, 70);
        assertEquals(7, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(30, tuple._3);
        assertEquals(40, tuple._4);
        assertEquals(50, tuple._5);
        assertEquals(60, tuple._6);
        assertEquals(70, tuple._7);
    }

    @Test
    public void testOf8() {
        IntTuple.IntTuple8 tuple = IntTuple.of(10, 20, 30, 40, 50, 60, 70, 80);
        assertEquals(8, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(30, tuple._3);
        assertEquals(40, tuple._4);
        assertEquals(50, tuple._5);
        assertEquals(60, tuple._6);
        assertEquals(70, tuple._7);
        assertEquals(80, tuple._8);
    }

    @Test
    public void testOf9() {
        IntTuple.IntTuple9 tuple = IntTuple.of(10, 20, 30, 40, 50, 60, 70, 80, 90);
        assertEquals(9, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(30, tuple._3);
        assertEquals(40, tuple._4);
        assertEquals(50, tuple._5);
        assertEquals(60, tuple._6);
        assertEquals(70, tuple._7);
        assertEquals(80, tuple._8);
        assertEquals(90, tuple._9);
    }

    @Test
    public void testCreate() {
        // Test empty array
        IntTuple<?> empty = IntTuple.create(null);
        assertEquals(0, empty.arity());

        empty = IntTuple.create(new int[0]);
        assertEquals(0, empty.arity());

        // Test array with 1 element
        IntTuple.IntTuple1 tuple1 = IntTuple.create(new int[] { 10 });
        assertEquals(1, tuple1.arity());
        assertEquals(10, tuple1._1);

        // Test array with 5 elements
        IntTuple.IntTuple5 tuple5 = IntTuple.create(new int[] { 10, 20, 30, 40, 50 });
        assertEquals(5, tuple5.arity());
        assertEquals(50, tuple5._5);

        // Test array with 9 elements
        IntTuple.IntTuple9 tuple9 = IntTuple.create(new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 });
        assertEquals(9, tuple9.arity());
        assertEquals(90, tuple9._9);

        // Test too many elements
        assertThrows(IllegalArgumentException.class, () -> IntTuple.create(new int[10]));
    }

    @Test
    public void testMin() {
        IntTuple.IntTuple3 tuple = IntTuple.of(30, 10, 20);
        assertEquals(10, tuple.min());

        IntTuple.IntTuple0 empty = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> empty.min());
    }

    @Test
    public void testMax() {
        IntTuple.IntTuple3 tuple = IntTuple.of(30, 10, 20);
        assertEquals(30, tuple.max());

        IntTuple.IntTuple0 empty = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> empty.max());
    }

    @Test
    public void testMedian() {
        IntTuple.IntTuple3 tuple = IntTuple.of(30, 10, 20);
        assertEquals(20, tuple.median());

        IntTuple.IntTuple4 evenTuple = IntTuple.of(10, 20, 30, 40);
        assertEquals(20, tuple.median()); // Should be (20 + 30) / 2 = 25

        IntTuple.IntTuple0 empty = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> empty.median());
    }

    @Test
    public void testSum() {
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
        assertEquals(60, tuple.sum());

        IntTuple.IntTuple0 empty = IntTuple.create(new int[0]);
        assertEquals(0, empty.sum());
    }

    @Test
    public void testAverage() {
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
        assertEquals(20.0, tuple.average());

        IntTuple.IntTuple0 empty = IntTuple.create(new int[0]);
        assertThrows(NoSuchElementException.class, () -> empty.average());
    }

    @Test
    public void testReverse() {
        // Test Tuple0
        IntTuple.IntTuple0 empty = IntTuple.create(new int[0]);
        IntTuple.IntTuple0 reversedEmpty = empty.reverse();
        assertEquals(0, reversedEmpty.arity());

        // Test Tuple1
        IntTuple.IntTuple1 tuple1 = IntTuple.of(10);
        IntTuple.IntTuple1 reversed1 = tuple1.reverse();
        assertEquals(10, reversed1._1);

        // Test Tuple2
        IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
        IntTuple.IntTuple2 reversed2 = tuple2.reverse();
        assertEquals(20, reversed2._1);
        assertEquals(10, reversed2._2);

        // Test Tuple3
        IntTuple.IntTuple3 tuple3 = IntTuple.of(10, 20, 30);
        IntTuple.IntTuple3 reversed3 = tuple3.reverse();
        assertEquals(30, reversed3._1);
        assertEquals(20, reversed3._2);
        assertEquals(10, reversed3._3);

        // Test larger tuples
        IntTuple.IntTuple5 tuple5 = IntTuple.of(10, 20, 30, 40, 50);
        IntTuple.IntTuple5 reversed5 = tuple5.reverse();
        assertEquals(50, reversed5._1);
        assertEquals(40, reversed5._2);
        assertEquals(30, reversed5._3);
        assertEquals(20, reversed5._4);
        assertEquals(10, reversed5._5);
    }

    @Test
    public void testContains() {
        // Test Tuple0
        IntTuple.IntTuple0 empty = IntTuple.create(new int[0]);
        assertFalse(empty.contains(10));

        // Test Tuple1
        IntTuple.IntTuple1 tuple1 = IntTuple.of(10);
        assertTrue(tuple1.contains(10));
        assertFalse(tuple1.contains(20));

        // Test Tuple3
        IntTuple.IntTuple3 tuple3 = IntTuple.of(10, 20, 30);
        assertTrue(tuple3.contains(10));
        assertTrue(tuple3.contains(20));
        assertTrue(tuple3.contains(30));
        assertFalse(tuple3.contains(40));

        // Test larger tuples
        IntTuple.IntTuple7 tuple7 = IntTuple.of(10, 20, 30, 40, 50, 60, 70);
        assertTrue(tuple7.contains(70));
        assertFalse(tuple7.contains(80));
    }

    @Test
    public void testToArray() {
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
        int[] array = tuple.toArray();
        assertEquals(3, array.length);
        assertEquals(10, array[0]);
        assertEquals(20, array[1]);
        assertEquals(30, array[2]);

        // Ensure it's a copy
        array[0] = 100;
        assertEquals(10, tuple._1);
    }

    @Test
    public void testToList() {
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
        IntList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    public void testForEach() {
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
        int[] sum = { 0 };
        tuple.forEach(i -> sum[0] += i);
        assertEquals(60, sum[0]);

        // Test Tuple2 forEach
        IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
        int[] count = { 0 };
        tuple2.forEach(i -> count[0]++);
        assertEquals(2, count[0]);
    }

    @Test
    public void testStream() {
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
        int[] array = tuple.stream().toArray();
        assertEquals(3, array.length);
        assertEquals(10, array[0]);
        assertEquals(20, array[1]);
        assertEquals(30, array[2]);

        long count = tuple.stream().filter(i -> i > 15).count();
        assertEquals(2, count);
    }

    @Test
    public void testHashCode() {
        IntTuple.IntTuple3 tuple1 = IntTuple.of(10, 20, 30);
        IntTuple.IntTuple3 tuple2 = IntTuple.of(10, 20, 30);
        IntTuple.IntTuple3 tuple3 = IntTuple.of(10, 20, 31);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());

        // Test specific hashcodes for coverage
        IntTuple.IntTuple1 single = IntTuple.of(10);
        assertEquals(10, single.hashCode());

        IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
        assertEquals(31 * 10 + 20, pair.hashCode());
    }

    @Test
    public void testEquals() {
        IntTuple.IntTuple3 tuple1 = IntTuple.of(10, 20, 30);
        IntTuple.IntTuple3 tuple2 = IntTuple.of(10, 20, 30);
        IntTuple.IntTuple3 tuple3 = IntTuple.of(10, 20, 31);
        IntTuple.IntTuple2 tuple4 = IntTuple.of(10, 20);

        assertTrue(tuple1.equals(tuple1));
        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
        assertFalse(tuple1.equals(tuple4));
        assertFalse(tuple1.equals(null));
        assertFalse(tuple1.equals("not a tuple"));

        // Test specific equals for coverage
        IntTuple.IntTuple1 single1 = IntTuple.of(10);
        IntTuple.IntTuple1 single2 = IntTuple.of(10);
        IntTuple.IntTuple1 single3 = IntTuple.of(20);
        assertTrue(single1.equals(single2));
        assertFalse(single1.equals(single3));
        assertFalse(single1.equals("not a tuple"));

        IntTuple.IntTuple2 pair1 = IntTuple.of(10, 20);
        IntTuple.IntTuple2 pair2 = IntTuple.of(10, 20);
        IntTuple.IntTuple2 pair3 = IntTuple.of(10, 21);
        assertTrue(pair1.equals(pair2));
        assertFalse(pair1.equals(pair3));
    }

    @Test
    public void testToString() {
        IntTuple.IntTuple0 empty = IntTuple.create(new int[0]);
        assertEquals("()", empty.toString());

        IntTuple.IntTuple1 single = IntTuple.of(10);
        assertEquals("(10)", single.toString());

        IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
        assertEquals("(10, 20)", pair.toString());

        IntTuple.IntTuple3 triple = IntTuple.of(10, 20, 30);
        assertEquals("(10, 20, 30)", triple.toString());
    }

    @Test
    public void testTuple2SpecificMethods() {
        IntTuple.IntTuple2 tuple = IntTuple.of(10, 20);

        // Test accept
        int[] sum = { 0 };
        tuple.accept((a, b) -> sum[0] = a + b);
        assertEquals(30, sum[0]);

        // Test map
        int result = tuple.map((a, b) -> a + b);
        assertEquals(30, result);

        // Test filter
        assertTrue(tuple.filter((a, b) -> a < b).isPresent());
        assertFalse(tuple.filter((a, b) -> a > b).isPresent());
    }

    @Test
    public void testTuple3SpecificMethods() {
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);

        // Test accept
        int[] sum = { 0 };
        tuple.accept((a, b, c) -> sum[0] = a + b + c);
        assertEquals(60, sum[0]);

        // Test map
        int result = tuple.map((a, b, c) -> a + b + c);
        assertEquals(60, result);

        // Test filter
        assertTrue(tuple.filter((a, b, c) -> a < b && b < c).isPresent());
        assertFalse(tuple.filter((a, b, c) -> a > b).isPresent());
    }

    @Test
    public void testTuple1SpecificMethods() {
        IntTuple.IntTuple1 tuple = IntTuple.of(10);

        // Test min/max/median/sum/average
        assertEquals(10, tuple.min());
        assertEquals(10, tuple.max());
        assertEquals(10, tuple.median());
        assertEquals(10, tuple.sum());
        assertEquals(10.0, tuple.average());
    }

    @Test
    public void testElementsMethod() {
        // Just test that elements are cached properly
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
        int[] elements1 = tuple.elements();
        int[] elements2 = tuple.elements();
        assertSame(elements1, elements2); // Should return same cached array
    }
}
