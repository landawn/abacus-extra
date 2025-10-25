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

public class FloatTupleTest extends TestBase {

    private static final float DELTA = 0.0001f;

    @Test
    public void testOf1() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.5f);
        assertEquals(1, tuple.arity());
        assertEquals(1.5f, tuple._1, DELTA);
    }

    @Test
    public void testOf2() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
        assertEquals(2, tuple.arity());
        assertEquals(1.5f, tuple._1, DELTA);
        assertEquals(2.5f, tuple._2, DELTA);
    }

    @Test
    public void testOf3() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f);
        assertEquals(3, tuple.arity());
        assertEquals(1.5f, tuple._1, DELTA);
        assertEquals(2.5f, tuple._2, DELTA);
        assertEquals(3.5f, tuple._3, DELTA);
    }

    @Test
    public void testOf4() {
        FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f);
        assertEquals(4, tuple.arity());
        assertEquals(1.5f, tuple._1, DELTA);
        assertEquals(2.5f, tuple._2, DELTA);
        assertEquals(3.5f, tuple._3, DELTA);
        assertEquals(4.5f, tuple._4, DELTA);
    }

    @Test
    public void testOf5() {
        FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f);
        assertEquals(5, tuple.arity());
        assertEquals(1.5f, tuple._1, DELTA);
        assertEquals(2.5f, tuple._2, DELTA);
        assertEquals(3.5f, tuple._3, DELTA);
        assertEquals(4.5f, tuple._4, DELTA);
        assertEquals(5.5f, tuple._5, DELTA);
    }

    @Test
    public void testOf6() {
        FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f);
        assertEquals(6, tuple.arity());
        assertEquals(1.5f, tuple._1, DELTA);
        assertEquals(2.5f, tuple._2, DELTA);
        assertEquals(3.5f, tuple._3, DELTA);
        assertEquals(4.5f, tuple._4, DELTA);
        assertEquals(5.5f, tuple._5, DELTA);
        assertEquals(6.5f, tuple._6, DELTA);
    }

    @Test
    public void testOf7() {
        FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f);
        assertEquals(7, tuple.arity());
        assertEquals(1.5f, tuple._1, DELTA);
        assertEquals(2.5f, tuple._2, DELTA);
        assertEquals(3.5f, tuple._3, DELTA);
        assertEquals(4.5f, tuple._4, DELTA);
        assertEquals(5.5f, tuple._5, DELTA);
        assertEquals(6.5f, tuple._6, DELTA);
        assertEquals(7.5f, tuple._7, DELTA);
    }

    @Test
    public void testOf8() {
        FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f);
        assertEquals(8, tuple.arity());
        assertEquals(1.5f, tuple._1, DELTA);
        assertEquals(2.5f, tuple._2, DELTA);
        assertEquals(3.5f, tuple._3, DELTA);
        assertEquals(4.5f, tuple._4, DELTA);
        assertEquals(5.5f, tuple._5, DELTA);
        assertEquals(6.5f, tuple._6, DELTA);
        assertEquals(7.5f, tuple._7, DELTA);
        assertEquals(8.5f, tuple._8, DELTA);
    }

    @Test
    public void testOf9() {
        FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f, 9.5f);
        assertEquals(9, tuple.arity());
        assertEquals(1.5f, tuple._1, DELTA);
        assertEquals(2.5f, tuple._2, DELTA);
        assertEquals(3.5f, tuple._3, DELTA);
        assertEquals(4.5f, tuple._4, DELTA);
        assertEquals(5.5f, tuple._5, DELTA);
        assertEquals(6.5f, tuple._6, DELTA);
        assertEquals(7.5f, tuple._7, DELTA);
        assertEquals(8.5f, tuple._8, DELTA);
        assertEquals(9.5f, tuple._9, DELTA);
    }

    @Test
    public void testCreate() {
        // Test empty array
        FloatTuple<?> empty = FloatTuple.create(null);
        assertEquals(0, empty.arity());

        empty = FloatTuple.create(new float[0]);
        assertEquals(0, empty.arity());

        // Test array with 1 element
        FloatTuple.FloatTuple1 tuple1 = FloatTuple.create(new float[] { 1.5f });
        assertEquals(1, tuple1.arity());
        assertEquals(1.5f, tuple1._1, DELTA);

        // Test array with 5 elements
        FloatTuple.FloatTuple5 tuple5 = FloatTuple.create(new float[] { 1.5f, 2.5f, 3.5f, 4.5f, 5.5f });
        assertEquals(5, tuple5.arity());
        assertEquals(5.5f, tuple5._5, DELTA);

        // Test array with 9 elements
        FloatTuple.FloatTuple9 tuple9 = FloatTuple.create(new float[] { 1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f, 9.5f });
        assertEquals(9, tuple9.arity());
        assertEquals(9.5f, tuple9._9, DELTA);

        // Test too many elements
        assertThrows(IllegalArgumentException.class, () -> FloatTuple.create(new float[10]));
    }

    @Test
    public void testMin() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.5f, 1.5f, 2.5f);
        assertEquals(1.5f, tuple.min(), DELTA);

        FloatTuple.FloatTuple0 empty = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> empty.min());
    }

    @Test
    public void testMax() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.5f, 1.5f, 2.5f);
        assertEquals(3.5f, tuple.max(), DELTA);

        FloatTuple.FloatTuple0 empty = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> empty.max());
    }

    @Test
    public void testMedian() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.5f, 1.5f, 2.5f);
        assertEquals(2.5f, tuple.median(), DELTA);

        FloatTuple.FloatTuple4 evenTuple = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f);
        assertEquals(2.5f, evenTuple.median(), DELTA);

        FloatTuple.FloatTuple0 empty = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> empty.median());
    }

    @Test
    public void testSum() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.0f);
        assertEquals(7.0f, tuple.sum(), DELTA);

        FloatTuple.FloatTuple0 empty = FloatTuple.create(new float[0]);
        assertEquals(0.0f, empty.sum(), DELTA);
    }

    @Test
    public void testAverage() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(2.0f, tuple.average(), DELTA);

        FloatTuple.FloatTuple0 empty = FloatTuple.create(new float[0]);
        assertThrows(NoSuchElementException.class, () -> empty.average());
    }

    @Test
    public void testReverse() {
        // Test Tuple0
        FloatTuple.FloatTuple0 empty = FloatTuple.create(new float[0]);
        FloatTuple.FloatTuple0 reversedEmpty = empty.reverse();
        assertEquals(0, reversedEmpty.arity());

        // Test Tuple1
        FloatTuple.FloatTuple1 tuple1 = FloatTuple.of(1.5f);
        FloatTuple.FloatTuple1 reversed1 = tuple1.reverse();
        assertEquals(1.5f, reversed1._1, DELTA);

        // Test Tuple2
        FloatTuple.FloatTuple2 tuple2 = FloatTuple.of(1.5f, 2.5f);
        FloatTuple.FloatTuple2 reversed2 = tuple2.reverse();
        assertEquals(2.5f, reversed2._1, DELTA);
        assertEquals(1.5f, reversed2._2, DELTA);

        // Test Tuple3
        FloatTuple.FloatTuple3 tuple3 = FloatTuple.of(1.5f, 2.5f, 3.5f);
        FloatTuple.FloatTuple3 reversed3 = tuple3.reverse();
        assertEquals(3.5f, reversed3._1, DELTA);
        assertEquals(2.5f, reversed3._2, DELTA);
        assertEquals(1.5f, reversed3._3, DELTA);

        // Test larger tuples
        FloatTuple.FloatTuple5 tuple5 = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f);
        FloatTuple.FloatTuple5 reversed5 = tuple5.reverse();
        assertEquals(5.5f, reversed5._1, DELTA);
        assertEquals(4.5f, reversed5._2, DELTA);
        assertEquals(3.5f, reversed5._3, DELTA);
        assertEquals(2.5f, reversed5._4, DELTA);
        assertEquals(1.5f, reversed5._5, DELTA);
    }

    @Test
    public void testContains() {
        // Test Tuple0
        FloatTuple.FloatTuple0 empty = FloatTuple.create(new float[0]);
        assertFalse(empty.contains(1.5f));

        // Test Tuple1
        FloatTuple.FloatTuple1 tuple1 = FloatTuple.of(1.5f);
        assertTrue(tuple1.contains(1.5f));
        assertFalse(tuple1.contains(2.5f));

        // Test Tuple3
        FloatTuple.FloatTuple3 tuple3 = FloatTuple.of(1.5f, 2.5f, 3.5f);
        assertTrue(tuple3.contains(1.5f));
        assertTrue(tuple3.contains(2.5f));
        assertTrue(tuple3.contains(3.5f));
        assertFalse(tuple3.contains(4.5f));

        // Test larger tuples
        FloatTuple.FloatTuple7 tuple7 = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f);
        assertTrue(tuple7.contains(7.5f));
        assertFalse(tuple7.contains(8.5f));
    }

    @Test
    public void testToArray() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f);
        float[] array = tuple.toArray();
        assertEquals(3, array.length);
        assertEquals(1.5f, array[0], DELTA);
        assertEquals(2.5f, array[1], DELTA);
        assertEquals(3.5f, array[2], DELTA);

        // Ensure it's a copy
        array[0] = 10.5f;
        assertEquals(1.5f, tuple._1, DELTA);
    }

    @Test
    public void testToList() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f);
        FloatList list = tuple.toList();
        assertEquals(3, list.size());
        assertEquals(1.5f, list.get(0), DELTA);
        assertEquals(2.5f, list.get(1), DELTA);
        assertEquals(3.5f, list.get(2), DELTA);
    }

    @Test
    public void testForEach() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.0f);
        float[] sum = { 0.0f };
        tuple.forEach(f -> sum[0] += f);
        assertEquals(7.0f, sum[0], DELTA);

        // Test Tuple2 forEach
        FloatTuple.FloatTuple2 tuple2 = FloatTuple.of(1.5f, 2.5f);
        int[] count = { 0 };
        tuple2.forEach(f -> count[0]++);
        assertEquals(2, count[0]);
    }

    @Test
    public void testStream() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f);
        float[] array = tuple.stream().toArray();
        assertEquals(3, array.length);
        assertEquals(1.5f, array[0], DELTA);
        assertEquals(2.5f, array[1], DELTA);
        assertEquals(3.5f, array[2], DELTA);

        long count = tuple.stream().filter(f -> f > 2.0f).count();
        assertEquals(2, count);
    }

    @Test
    public void testHashCode() {
        FloatTuple.FloatTuple3 tuple1 = FloatTuple.of(1.5f, 2.5f, 3.5f);
        FloatTuple.FloatTuple3 tuple2 = FloatTuple.of(1.5f, 2.5f, 3.5f);
        FloatTuple.FloatTuple3 tuple3 = FloatTuple.of(1.5f, 2.5f, 4.6f);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());

        // Test specific hashcodes for coverage
        FloatTuple.FloatTuple1 single = FloatTuple.of(1.5f);
        assertEquals(Float.floatToIntBits(1.5f), single.hashCode());

        FloatTuple.FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
        assertEquals(31 * Float.floatToIntBits(1.5f) + Float.floatToIntBits(2.5f), pair.hashCode());
    }

    @Test
    public void testEquals() {
        FloatTuple.FloatTuple3 tuple1 = FloatTuple.of(1.5f, 2.5f, 3.5f);
        FloatTuple.FloatTuple3 tuple2 = FloatTuple.of(1.5f, 2.5f, 3.5f);
        FloatTuple.FloatTuple3 tuple3 = FloatTuple.of(1.5f, 2.5f, 3.6f);
        FloatTuple.FloatTuple2 tuple4 = FloatTuple.of(1.5f, 2.5f);

        assertTrue(tuple1.equals(tuple1));
        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
        assertFalse(tuple1.equals(tuple4));
        assertFalse(tuple1.equals(null));
        assertFalse(tuple1.equals("not a tuple"));

        // Test specific equals for coverage
        FloatTuple.FloatTuple1 single1 = FloatTuple.of(1.5f);
        FloatTuple.FloatTuple1 single2 = FloatTuple.of(1.5f);
        FloatTuple.FloatTuple1 single3 = FloatTuple.of(2.5f);
        assertTrue(single1.equals(single2));
        assertFalse(single1.equals(single3));
        assertFalse(single1.equals("not a tuple"));

        FloatTuple.FloatTuple2 pair1 = FloatTuple.of(1.5f, 2.5f);
        FloatTuple.FloatTuple2 pair2 = FloatTuple.of(1.5f, 2.5f);
        FloatTuple.FloatTuple2 pair3 = FloatTuple.of(1.5f, 2.6f);
        assertTrue(pair1.equals(pair2));
        assertFalse(pair1.equals(pair3));
    }

    @Test
    public void testToString() {
        FloatTuple.FloatTuple0 empty = FloatTuple.create(new float[0]);
        assertEquals("[]", empty.toString());

        FloatTuple.FloatTuple1 single = FloatTuple.of(1.5f);
        assertEquals("[1.5]", single.toString());

        FloatTuple.FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
        assertEquals("[1.5, 2.5]", pair.toString());

        FloatTuple.FloatTuple3 triple = FloatTuple.of(1.5f, 2.5f, 3.5f);
        assertEquals("[1.5, 2.5, 3.5]", triple.toString());
    }

    @Test
    public void testTuple2SpecificMethods() {
        FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);

        // Test accept
        float[] sum = { 0.0f };
        tuple.accept((a, b) -> sum[0] = a + b);
        assertEquals(4.0f, sum[0], DELTA);

        // Test map
        float result = tuple.map((a, b) -> a + b);
        assertEquals(4.0f, result, DELTA);

        // Test filter
        assertTrue(tuple.filter((a, b) -> a < b).isPresent());
        assertFalse(tuple.filter((a, b) -> a > b).isPresent());
    }

    @Test
    public void testTuple3SpecificMethods() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f);

        // Test accept
        float[] sum = { 0.0f };
        tuple.accept((a, b, c) -> sum[0] = a + b + c);
        assertEquals(7.5f, sum[0], DELTA);

        // Test map
        float result = tuple.map((a, b, c) -> a + b + c);
        assertEquals(7.5f, result, DELTA);

        // Test filter
        assertTrue(tuple.filter((a, b, c) -> a < b && b < c).isPresent());
        assertFalse(tuple.filter((a, b, c) -> a > b).isPresent());
    }

    @Test
    public void testTuple1SpecificMethods() {
        FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.5f);

        // Test min/max/median/sum/average
        assertEquals(1.5f, tuple.min(), DELTA);
        assertEquals(1.5f, tuple.max(), DELTA);
        assertEquals(1.5f, tuple.median(), DELTA);
        assertEquals(1.5f, tuple.sum(), DELTA);
        assertEquals(1.5f, tuple.average(), DELTA);
    }

    @Test
    public void testElementsMethod() {
        // Just test that elements are cached properly
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.5f);
        float[] elements1 = tuple.elements();
        float[] elements2 = tuple.elements();
        assertSame(elements1, elements2); // Should return same cached array
    }
}