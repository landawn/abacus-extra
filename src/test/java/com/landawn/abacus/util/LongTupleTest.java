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

public class LongTupleTest extends TestBase {

    @Test
    public void testOf1() {
        LongTuple.LongTuple1 tuple = LongTuple.of(100L);
        assertEquals(1, tuple.arity());
        assertEquals(100L, tuple._1);
    }

    @Test
    public void testOf2() {
        LongTuple.LongTuple2 tuple = LongTuple.of(100L, 200L);
        assertEquals(2, tuple.arity());
        assertEquals(100L, tuple._1);
        assertEquals(200L, tuple._2);
    }

    @Test
    public void testOf3() {
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
        assertEquals(3, tuple.arity());
        assertEquals(100L, tuple._1);
        assertEquals(200L, tuple._2);
        assertEquals(300L, tuple._3);
    }

    @Test
    public void testOf4() {
        LongTuple.LongTuple4 tuple = LongTuple.of(100L, 200L, 300L, 400L);
        assertEquals(4, tuple.arity());
        assertEquals(100L, tuple._1);
        assertEquals(200L, tuple._2);
        assertEquals(300L, tuple._3);
        assertEquals(400L, tuple._4);
    }

    @Test
    public void testOf5() {
        LongTuple.LongTuple5 tuple = LongTuple.of(100L, 200L, 300L, 400L, 500L);
        assertEquals(5, tuple.arity());
        assertEquals(100L, tuple._1);
        assertEquals(200L, tuple._2);
        assertEquals(300L, tuple._3);
        assertEquals(400L, tuple._4);
        assertEquals(500L, tuple._5);
    }

    @Test
    public void testOf6() {
        LongTuple.LongTuple6 tuple = LongTuple.of(100L, 200L, 300L, 400L, 500L, 600L);
        assertEquals(6, tuple.arity());
        assertEquals(100L, tuple._1);
        assertEquals(200L, tuple._2);
        assertEquals(300L, tuple._3);
        assertEquals(400L, tuple._4);
        assertEquals(500L, tuple._5);
        assertEquals(600L, tuple._6);
    }

    @Test
    public void testOf7() {
        LongTuple.LongTuple7 tuple = LongTuple.of(100L, 200L, 300L, 400L, 500L, 600L, 700L);
        assertEquals(7, tuple.arity());
        assertEquals(100L, tuple._1);
        assertEquals(200L, tuple._2);
        assertEquals(300L, tuple._3);
        assertEquals(400L, tuple._4);
        assertEquals(500L, tuple._5);
        assertEquals(600L, tuple._6);
        assertEquals(700L, tuple._7);
    }

    @Test
    public void testOf8() {
        LongTuple.LongTuple8 tuple = LongTuple.of(100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L);
        assertEquals(8, tuple.arity());
        assertEquals(100L, tuple._1);
        assertEquals(200L, tuple._2);
        assertEquals(300L, tuple._3);
        assertEquals(400L, tuple._4);
        assertEquals(500L, tuple._5);
        assertEquals(600L, tuple._6);
        assertEquals(700L, tuple._7);
        assertEquals(800L, tuple._8);
    }

    @Test
    public void testOf9() {
        LongTuple.LongTuple9 tuple = LongTuple.of(100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L);
        assertEquals(9, tuple.arity());
        assertEquals(100L, tuple._1);
        assertEquals(200L, tuple._2);
        assertEquals(300L, tuple._3);
        assertEquals(400L, tuple._4);
        assertEquals(500L, tuple._5);
        assertEquals(600L, tuple._6);
        assertEquals(700L, tuple._7);
        assertEquals(800L, tuple._8);
        assertEquals(900L, tuple._9);
    }

    @Test
    public void testCreate() {
        // Test empty array
        LongTuple<?> empty = LongTuple.create(null);
        assertEquals(0, empty.arity());

        empty = LongTuple.create(new long[0]);
        assertEquals(0, empty.arity());

        // Test array with 1 element
        LongTuple.LongTuple1 tuple1 = LongTuple.create(new long[] { 100L });
        assertEquals(1, tuple1.arity());
        assertEquals(100L, tuple1._1);

        // Test array with 5 elements
        LongTuple.LongTuple5 tuple5 = LongTuple.create(new long[] { 100L, 200L, 300L, 400L, 500L });
        assertEquals(5, tuple5.arity());
        assertEquals(500L, tuple5._5);

        // Test array with 9 elements
        LongTuple.LongTuple9 tuple9 = LongTuple.create(new long[] { 100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L });
        assertEquals(9, tuple9.arity());
        assertEquals(900L, tuple9._9);

        // Test too many elements
        assertThrows(IllegalArgumentException.class, () -> LongTuple.create(new long[10]));
    }

    @Test
    public void testMin() {
        LongTuple.LongTuple3 tuple = LongTuple.of(300L, 100L, 200L);
        assertEquals(100L, tuple.min());

        LongTuple.LongTuple0 empty = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> empty.min());
    }

    @Test
    public void testMax() {
        LongTuple.LongTuple3 tuple = LongTuple.of(300L, 100L, 200L);
        assertEquals(300L, tuple.max());

        LongTuple.LongTuple0 empty = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> empty.max());
    }

    @Test
    public void testMedian() {
        LongTuple.LongTuple3 tuple = LongTuple.of(300L, 100L, 200L);
        assertEquals(200L, tuple.median());

        LongTuple.LongTuple4 evenTuple = LongTuple.of(100L, 200L, 300L, 400L);
        assertEquals(200L, evenTuple.median());   // Should be (200L + 300L) / 2 = 250L

        LongTuple.LongTuple0 empty = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> empty.median());
    }

    @Test
    public void testSum() {
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
        assertEquals(600L, tuple.sum());

        LongTuple.LongTuple0 empty = LongTuple.create(new long[0]);
        assertEquals(0L, empty.sum());
    }

    @Test
    public void testAverage() {
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
        assertEquals(200.0, tuple.average());

        LongTuple.LongTuple0 empty = LongTuple.create(new long[0]);
        assertThrows(NoSuchElementException.class, () -> empty.average());
    }

    @Test
    public void testReverse() {
        // Test Tuple0
        LongTuple.LongTuple0 empty = LongTuple.create(new long[0]);
        LongTuple.LongTuple0 reversedEmpty = empty.reverse();
        assertEquals(0, reversedEmpty.arity());

        // Test Tuple1
        LongTuple.LongTuple1 tuple1 = LongTuple.of(100L);
        LongTuple.LongTuple1 reversed1 = tuple1.reverse();
        assertEquals(100L, reversed1._1);

        // Test Tuple2
        LongTuple.LongTuple2 tuple2 = LongTuple.of(100L, 200L);
        LongTuple.LongTuple2 reversed2 = tuple2.reverse();
        assertEquals(200L, reversed2._1);
        assertEquals(100L, reversed2._2);

        // Test Tuple3
        LongTuple.LongTuple3 tuple3 = LongTuple.of(100L, 200L, 300L);
        LongTuple.LongTuple3 reversed3 = tuple3.reverse();
        assertEquals(300L, reversed3._1);
        assertEquals(200L, reversed3._2);
        assertEquals(100L, reversed3._3);

        // Test larger tuples
        LongTuple.LongTuple5 tuple5 = LongTuple.of(100L, 200L, 300L, 400L, 500L);
        LongTuple.LongTuple5 reversed5 = tuple5.reverse();
        assertEquals(500L, reversed5._1);
        assertEquals(400L, reversed5._2);
        assertEquals(300L, reversed5._3);
        assertEquals(200L, reversed5._4);
        assertEquals(100L, reversed5._5);
    }

    @Test
    public void testContains() {
        // Test Tuple0
        LongTuple.LongTuple0 empty = LongTuple.create(new long[0]);
        assertFalse(empty.contains(100L));

        // Test Tuple1
        LongTuple.LongTuple1 tuple1 = LongTuple.of(100L);
        assertTrue(tuple1.contains(100L));
        assertFalse(tuple1.contains(200L));

        // Test Tuple3
        LongTuple.LongTuple3 tuple3 = LongTuple.of(100L, 200L, 300L);
        assertTrue(tuple3.contains(100L));
        assertTrue(tuple3.contains(200L));
        assertTrue(tuple3.contains(300L));
        assertFalse(tuple3.contains(400L));

        // Test larger tuples
        LongTuple.LongTuple7 tuple7 = LongTuple.of(100L, 200L, 300L, 400L, 500L, 600L, 700L);
        assertTrue(tuple7.contains(700L));
        assertFalse(tuple7.contains(800L));
    }

    @Test
    public void testToArray() {
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
        long[] array = tuple.toArray();
        assertEquals(3, array.length);
        assertEquals(100L, array[0]);
        assertEquals(200L, array[1]);
        assertEquals(300L, array[2]);

        // Ensure it's a copy
        array[0] = 1000L;
        assertEquals(100L, tuple._1);
    }

    @Test
    public void testToList() {
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
        LongList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(100L, list.get(0));
        assertEquals(200L, list.get(1));
        assertEquals(300L, list.get(2));
    }

    @Test
    public void testForEach() {
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
        long[] sum = { 0L };
        tuple.forEach(l -> sum[0] += l);
        assertEquals(600L, sum[0]);

        // Test Tuple2 forEach
        LongTuple.LongTuple2 tuple2 = LongTuple.of(100L, 200L);
        int[] count = { 0 };
        tuple2.forEach(l -> count[0]++);
        assertEquals(2, count[0]);
    }

    @Test
    public void testStream() {
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
        long[] array = tuple.stream().toArray();
        assertEquals(3, array.length);
        assertEquals(100L, array[0]);
        assertEquals(200L, array[1]);
        assertEquals(300L, array[2]);

        long count = tuple.stream().filter(l -> l > 150L).count();
        assertEquals(2, count);
    }

    @Test
    public void testHashCode() {
        LongTuple.LongTuple3 tuple1 = LongTuple.of(100L, 200L, 300L);
        LongTuple.LongTuple3 tuple2 = LongTuple.of(100L, 200L, 300L);
        LongTuple.LongTuple3 tuple3 = LongTuple.of(100L, 200L, 301L);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());

        // Test specific hashcodes for coverage
        LongTuple.LongTuple1 single = LongTuple.of(100L);
        assertEquals(Long.hashCode(100L), single.hashCode());

        LongTuple.LongTuple2 pair = LongTuple.of(100L, 200L);
        assertEquals(31 * Long.hashCode(100L) + Long.hashCode(200L), pair.hashCode());
    }

    @Test
    public void testEquals() {
        LongTuple.LongTuple3 tuple1 = LongTuple.of(100L, 200L, 300L);
        LongTuple.LongTuple3 tuple2 = LongTuple.of(100L, 200L, 300L);
        LongTuple.LongTuple3 tuple3 = LongTuple.of(100L, 200L, 301L);
        LongTuple.LongTuple2 tuple4 = LongTuple.of(100L, 200L);

        assertTrue(tuple1.equals(tuple1));
        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
        assertFalse(tuple1.equals(tuple4));
        assertFalse(tuple1.equals(null));
        assertFalse(tuple1.equals("not a tuple"));

        // Test specific equals for coverage
        LongTuple.LongTuple1 single1 = LongTuple.of(100L);
        LongTuple.LongTuple1 single2 = LongTuple.of(100L);
        LongTuple.LongTuple1 single3 = LongTuple.of(200L);
        assertTrue(single1.equals(single2));
        assertFalse(single1.equals(single3));
        assertFalse(single1.equals("not a tuple"));

        LongTuple.LongTuple2 pair1 = LongTuple.of(100L, 200L);
        LongTuple.LongTuple2 pair2 = LongTuple.of(100L, 200L);
        LongTuple.LongTuple2 pair3 = LongTuple.of(100L, 201L);
        assertTrue(pair1.equals(pair2));
        assertFalse(pair1.equals(pair3));
    }

    @Test
    public void testToString() {
        LongTuple.LongTuple0 empty = LongTuple.create(new long[0]);
        assertEquals("()", empty.toString());

        LongTuple.LongTuple1 single = LongTuple.of(100L);
        assertEquals("(100)", single.toString());

        LongTuple.LongTuple2 pair = LongTuple.of(100L, 200L);
        assertEquals("(100, 200)", pair.toString());

        LongTuple.LongTuple3 triple = LongTuple.of(100L, 200L, 300L);
        assertEquals("(100, 200, 300)", triple.toString());
    }

    @Test
    public void testTuple2SpecificMethods() {
        LongTuple.LongTuple2 tuple = LongTuple.of(100L, 200L);

        // Test accept
        long[] sum = { 0L };
        tuple.accept((a, b) -> sum[0] = a + b);
        assertEquals(300L, sum[0]);

        // Test map
        long result = tuple.map((a, b) -> a + b);
        assertEquals(300L, result);

        // Test filter
        assertTrue(tuple.filter((a, b) -> a < b).isPresent());
        assertFalse(tuple.filter((a, b) -> a > b).isPresent());
    }

    @Test
    public void testTuple3SpecificMethods() {
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);

        // Test accept
        long[] sum = { 0L };
        tuple.accept((a, b, c) -> sum[0] = a + b + c);
        assertEquals(600L, sum[0]);

        // Test map
        long result = tuple.map((a, b, c) -> a + b + c);
        assertEquals(600L, result);

        // Test filter
        assertTrue(tuple.filter((a, b, c) -> a < b && b < c).isPresent());
        assertFalse(tuple.filter((a, b, c) -> a > b).isPresent());
    }

    @Test
    public void testTuple1SpecificMethods() {
        LongTuple.LongTuple1 tuple = LongTuple.of(100L);

        // Test min/max/median/sum/average
        assertEquals(100L, tuple.min());
        assertEquals(100L, tuple.max());
        assertEquals(100L, tuple.median());
        assertEquals(100L, tuple.sum());
        assertEquals(100.0, tuple.average());
    }

    @Test
    public void testElementsMethod() {
        // Just test that elements are cached properly
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
        long[] elements1 = tuple.elements();
        long[] elements2 = tuple.elements();
        assertSame(elements1, elements2);   // Should return same cached array
    }
}