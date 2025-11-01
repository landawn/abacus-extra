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

public class ByteTupleTest extends TestBase {

    @Test
    public void testOf1() {
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 10);
        assertEquals(1, tuple.arity());
        assertEquals(10, tuple._1);
    }

    @Test
    public void testOf2() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(2, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
    }

    @Test
    public void testOf3() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(3, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(30, tuple._3);
    }

    @Test
    public void testOf4() {
        ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
        assertEquals(4, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(30, tuple._3);
        assertEquals(40, tuple._4);
    }

    @Test
    public void testOf5() {
        ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
        assertEquals(5, tuple.arity());
        assertEquals(10, tuple._1);
        assertEquals(20, tuple._2);
        assertEquals(30, tuple._3);
        assertEquals(40, tuple._4);
        assertEquals(50, tuple._5);
    }

    @Test
    public void testOf6() {
        ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
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
        ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
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
        ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
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
        ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
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
        ByteTuple<?> empty = ByteTuple.create(null);
        assertEquals(0, empty.arity());

        empty = ByteTuple.create(new byte[0]);
        assertEquals(0, empty.arity());

        // Test array with 1 element
        ByteTuple.ByteTuple1 tuple1 = ByteTuple.create(new byte[] { 10 });
        assertEquals(1, tuple1.arity());
        assertEquals(10, tuple1._1);

        // Test array with 5 elements
        ByteTuple.ByteTuple5 tuple5 = ByteTuple.create(new byte[] { 10, 20, 30, 40, 50 });
        assertEquals(5, tuple5.arity());
        assertEquals(50, tuple5._5);

        // Test array with 9 elements
        ByteTuple.ByteTuple9 tuple9 = ByteTuple.create(new byte[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 });
        assertEquals(9, tuple9.arity());
        assertEquals(90, tuple9._9);

        // Test too many elements
        assertThrows(IllegalArgumentException.class, () -> ByteTuple.create(new byte[10]));
    }

    @Test
    public void testMin() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals(10, tuple.min());

        ByteTuple.ByteTuple0 empty = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> empty.min());
    }

    @Test
    public void testMax() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals(30, tuple.max());

        ByteTuple.ByteTuple0 empty = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> empty.max());
    }

    @Test
    public void testMedian() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals(20, tuple.median());

        ByteTuple.ByteTuple0 empty = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> empty.median());
    }

    @Test
    public void testSum() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(60, tuple.sum());

        ByteTuple.ByteTuple0 empty = ByteTuple.create(new byte[0]);
        assertEquals(0, empty.sum());
    }

    @Test
    public void testAverage() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(20.0, tuple.average());

        ByteTuple.ByteTuple0 empty = ByteTuple.create(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> empty.average());
    }

    @Test
    public void testReverse() {
        // Test Tuple0
        ByteTuple.ByteTuple0 empty = ByteTuple.create(new byte[0]);
        ByteTuple.ByteTuple0 reversedEmpty = empty.reverse();
        assertEquals(0, reversedEmpty.arity());

        // Test Tuple1
        ByteTuple.ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
        ByteTuple.ByteTuple1 reversed1 = tuple1.reverse();
        assertEquals(10, reversed1._1);

        // Test Tuple2
        ByteTuple.ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple.ByteTuple2 reversed2 = tuple2.reverse();
        assertEquals(20, reversed2._1);
        assertEquals(10, reversed2._2);

        // Test Tuple3
        ByteTuple.ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple.ByteTuple3 reversed3 = tuple3.reverse();
        assertEquals(30, reversed3._1);
        assertEquals(20, reversed3._2);
        assertEquals(10, reversed3._3);

        // Test larger tuples
        ByteTuple.ByteTuple5 tuple5 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
        ByteTuple.ByteTuple5 reversed5 = tuple5.reverse();
        assertEquals(50, reversed5._1);
        assertEquals(40, reversed5._2);
        assertEquals(30, reversed5._3);
        assertEquals(20, reversed5._4);
        assertEquals(10, reversed5._5);
    }

    @Test
    public void testContains() {
        // Test Tuple0
        ByteTuple.ByteTuple0 empty = ByteTuple.create(new byte[0]);
        assertFalse(empty.contains((byte) 10));

        // Test Tuple1
        ByteTuple.ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
        assertTrue(tuple1.contains((byte) 10));
        assertFalse(tuple1.contains((byte) 20));

        // Test Tuple3
        ByteTuple.ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertTrue(tuple3.contains((byte) 10));
        assertTrue(tuple3.contains((byte) 20));
        assertTrue(tuple3.contains((byte) 30));
        assertFalse(tuple3.contains((byte) 40));

        // Test larger tuples
        ByteTuple.ByteTuple7 tuple7 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
        assertTrue(tuple7.contains((byte) 70));
        assertFalse(tuple7.contains((byte) 80));
    }

    @Test
    public void testToArray() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        byte[] array = tuple.toArray();
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
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    public void testForEach() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        int[] sum = { 0 };
        tuple.forEach(b -> sum[0] += b);
        assertEquals(60, sum[0]);

        // Test Tuple2 forEach
        ByteTuple.ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
        int[] count = { 0 };
        tuple2.forEach(b -> count[0]++);
        assertEquals(2, count[0]);
    }

    @Test
    public void testStream() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        byte[] array = tuple.stream().toArray();
        assertEquals(3, array.length);
        assertEquals(10, array[0]);
        assertEquals(20, array[1]);
        assertEquals(30, array[2]);

        long count = tuple.stream().filter(b -> b > 15).count();
        assertEquals(2, count);
    }

    @Test
    public void testHashCode() {
        ByteTuple.ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple.ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple.ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 31);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());

        // Test specific hashcodes for coverage
        ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 10);
        assertEquals(10, single.hashCode());

        ByteTuple.ByteTuple2 pair = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals(31 * 10 + 20, pair.hashCode());
    }

    @Test
    public void testEquals() {
        ByteTuple.ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple.ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        ByteTuple.ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 31);
        ByteTuple.ByteTuple2 tuple4 = ByteTuple.of((byte) 10, (byte) 20);

        assertTrue(tuple1.equals(tuple1));
        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
        assertFalse(tuple1.equals(tuple4));
        assertFalse(tuple1.equals(null));
        assertFalse(tuple1.equals("not a tuple"));

        // Test specific equals for coverage
        ByteTuple.ByteTuple1 single1 = ByteTuple.of((byte) 10);
        ByteTuple.ByteTuple1 single2 = ByteTuple.of((byte) 10);
        ByteTuple.ByteTuple1 single3 = ByteTuple.of((byte) 20);
        assertTrue(single1.equals(single2));
        assertFalse(single1.equals(single3));
        assertFalse(single1.equals("not a tuple"));

        ByteTuple.ByteTuple2 pair1 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple.ByteTuple2 pair2 = ByteTuple.of((byte) 10, (byte) 20);
        ByteTuple.ByteTuple2 pair3 = ByteTuple.of((byte) 10, (byte) 21);
        assertTrue(pair1.equals(pair2));
        assertFalse(pair1.equals(pair3));
    }

    @Test
    public void testToString() {
        ByteTuple.ByteTuple0 empty = ByteTuple.create(new byte[0]);
        assertEquals("()", empty.toString());

        ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 10);
        assertEquals("(10)", single.toString());

        ByteTuple.ByteTuple2 pair = ByteTuple.of((byte) 10, (byte) 20);
        assertEquals("(10, 20)", pair.toString());

        ByteTuple.ByteTuple3 triple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals("(10, 20, 30)", triple.toString());
    }

    @Test
    public void testTuple2SpecificMethods() {
        ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);

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
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);

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
        ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 10);

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
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        byte[] elements1 = tuple.elements();
        byte[] elements2 = tuple.elements();
        assertSame(elements1, elements2); // Should return same cached array
    }
}