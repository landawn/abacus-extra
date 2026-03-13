package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.FloatTuple.FloatTuple0;
import com.landawn.abacus.util.FloatTuple.FloatTuple1;
import com.landawn.abacus.util.FloatTuple.FloatTuple2;
import com.landawn.abacus.util.FloatTuple.FloatTuple3;
import com.landawn.abacus.util.FloatTuple.FloatTuple4;
import com.landawn.abacus.util.FloatTuple.FloatTuple5;
import com.landawn.abacus.util.FloatTuple.FloatTuple6;
import com.landawn.abacus.util.FloatTuple.FloatTuple7;
import com.landawn.abacus.util.FloatTuple.FloatTuple8;
import com.landawn.abacus.util.FloatTuple.FloatTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.FloatStream;

class FloatTupleTest extends TestBase {

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
        FloatTuple<?> empty = FloatTuple.copyOf(null);
        assertEquals(0, empty.arity());

        empty = FloatTuple.copyOf(new float[0]);
        assertEquals(0, empty.arity());

        // Test array with 1 element
        FloatTuple.FloatTuple1 tuple1 = FloatTuple.copyOf(new float[] { 1.5f });
        assertEquals(1, tuple1.arity());
        assertEquals(1.5f, tuple1._1, DELTA);

        // Test array with 5 elements
        FloatTuple.FloatTuple5 tuple5 = FloatTuple.copyOf(new float[] { 1.5f, 2.5f, 3.5f, 4.5f, 5.5f });
        assertEquals(5, tuple5.arity());
        assertEquals(5.5f, tuple5._5, DELTA);

        // Test array with 9 elements
        FloatTuple.FloatTuple9 tuple9 = FloatTuple.copyOf(new float[] { 1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f, 9.5f });
        assertEquals(9, tuple9.arity());
        assertEquals(9.5f, tuple9._9, DELTA);

        // Test too many elements
        assertThrows(IllegalArgumentException.class, () -> FloatTuple.copyOf(new float[10]));
    }

    @Test
    public void testMin() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.5f, 1.5f, 2.5f);
        assertEquals(1.5f, tuple.min(), DELTA);

        FloatTuple.FloatTuple0 empty = FloatTuple.copyOf(new float[0]);
        assertThrows(NoSuchElementException.class, () -> empty.min());
    }

    @Test
    public void testMax() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.5f, 1.5f, 2.5f);
        assertEquals(3.5f, tuple.max(), DELTA);

        FloatTuple.FloatTuple0 empty = FloatTuple.copyOf(new float[0]);
        assertThrows(NoSuchElementException.class, () -> empty.max());
    }

    @Test
    public void testMedian() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.5f, 1.5f, 2.5f);
        assertEquals(2.5f, tuple.median(), DELTA);

        FloatTuple.FloatTuple4 evenTuple = FloatTuple.of(1.5f, 2.5f, 3.5f, 4.5f);
        assertEquals(2.5f, evenTuple.median(), DELTA);

        FloatTuple.FloatTuple0 empty = FloatTuple.copyOf(new float[0]);
        assertThrows(NoSuchElementException.class, () -> empty.median());
    }

    @Test
    public void testSum() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.5f, 2.5f, 3.0f);
        assertEquals(7.0f, tuple.sum(), DELTA);

        FloatTuple.FloatTuple0 empty = FloatTuple.copyOf(new float[0]);
        assertEquals(0.0f, empty.sum(), DELTA);
    }

    @Test
    public void testAverage() {
        FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
        assertEquals(2.0f, tuple.average(), DELTA);

        FloatTuple.FloatTuple0 empty = FloatTuple.copyOf(new float[0]);
        assertThrows(NoSuchElementException.class, () -> empty.average());
    }

    @Test
    public void testReverse() {
        // Test Tuple0
        FloatTuple.FloatTuple0 empty = FloatTuple.copyOf(new float[0]);
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
        FloatTuple.FloatTuple0 empty = FloatTuple.copyOf(new float[0]);
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
        FloatTuple.FloatTuple0 empty = FloatTuple.copyOf(new float[0]);
        assertEquals("()", empty.toString());

        FloatTuple.FloatTuple1 single = FloatTuple.of(1.5f);
        assertEquals("(1.5)", single.toString());

        FloatTuple.FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
        assertEquals("(1.5, 2.5)", pair.toString());

        FloatTuple.FloatTuple3 triple = FloatTuple.of(1.5f, 2.5f, 3.5f);
        assertEquals("(1.5, 2.5, 3.5)", triple.toString());
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

    @Nested
    /**
     * Comprehensive test suite for FloatTuple and its nested classes.
     * Tests all public methods including factory methods, statistical operations,
     * collection conversions, and special methods in Tuple2 and Tuple3.
     */
    @Tag("2025")
    class FloatTuple2025Test extends TestBase {

        // Factory method tests
        @Test
        public void testOf1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf2() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(2.0f, tuple._2, 0.001f);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(2.0f, tuple._2, 0.001f);
            assertEquals(3.0f, tuple._3, 0.001f);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf4() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(2.0f, tuple._2, 0.001f);
            assertEquals(3.0f, tuple._3, 0.001f);
            assertEquals(4.0f, tuple._4, 0.001f);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf5() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(5.0f, tuple._5, 0.001f);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf6() {
            FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(6.0f, tuple._6, 0.001f);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf7() {
            FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(7.0f, tuple._7, 0.001f);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf8() {
            FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(8.0f, tuple._8, 0.001f);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf9() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(9.0f, tuple._9, 0.001f);
            assertEquals(9, tuple.arity());
        }

        // Create method tests
        @Test
        public void testCreateEmpty() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreateNull() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate1() {
            FloatTuple1 tuple = FloatTuple.copyOf(new float[] { 1.0f });
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate3() {
            FloatTuple3 tuple = FloatTuple.copyOf(new float[] { 1.0f, 2.0f, 3.0f });
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(2.0f, tuple._2, 0.001f);
            assertEquals(3.0f, tuple._3, 0.001f);
        }

        @Test
        public void testCreate9() {
            FloatTuple9 tuple = FloatTuple.copyOf(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f });
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(9.0f, tuple._9, 0.001f);
        }

        @Test
        public void testCreateTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                FloatTuple.copyOf(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f });
            });
        }

        // Statistical method tests - min
        @Test
        public void testMinTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1.0f, tuple.min(), 0.001f);
        }

        @Test
        public void testMinTuple3() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(1.0f, tuple.min(), 0.001f);
        }

        @Test
        public void testMinTuple0ThrowsException() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        // Statistical method tests - max
        @Test
        public void testMaxTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1.0f, tuple.max(), 0.001f);
        }

        @Test
        public void testMaxTuple3() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(3.0f, tuple.max(), 0.001f);
        }

        @Test
        public void testMaxTuple0ThrowsException() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        // Statistical method tests - median
        @Test
        public void testMedianTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1.0f, tuple.median(), 0.001f);
        }

        @Test
        public void testMedianTuple3() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(2.0f, tuple.median(), 0.001f);
        }

        @Test
        public void testMedianTuple0ThrowsException() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        // Statistical method tests - sum
        @Test
        public void testSumTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            assertEquals(0.0f, tuple.sum(), 0.001f);
        }

        @Test
        public void testSumTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1.0f, tuple.sum(), 0.001f);
        }

        @Test
        public void testSumTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(6.0f, tuple.sum(), 0.001f);
        }

        // Statistical method tests - average
        @Test
        public void testAverageTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(2.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple0ThrowsException() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        // Reverse tests
        @Test
        public void testReverseTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            FloatTuple<FloatTuple0> reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        public void testReverseTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            FloatTuple1 reversed = tuple.reverse();
            assertEquals(1.0f, reversed._1, 0.001f);
        }

        @Test
        public void testReverseTuple2() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 reversed = tuple.reverse();
            assertEquals(2.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._2, 0.001f);
        }

        @Test
        public void testReverseTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 reversed = tuple.reverse();
            assertEquals(3.0f, reversed._1, 0.001f);
            assertEquals(2.0f, reversed._2, 0.001f);
            assertEquals(1.0f, reversed._3, 0.001f);
        }

        // Contains tests
        @Test
        public void testContainsTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            assertFalse(tuple.contains(1.0f));
        }

        @Test
        public void testContainsTuple1True() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        public void testContainsTuple1False() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertFalse(tuple.contains(99.0f));
        }

        @Test
        public void testContainsTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(2.0f));
            assertTrue(tuple.contains(3.0f));
            assertFalse(tuple.contains(99.0f));
        }

        // toArray tests
        @Test
        public void testToArrayTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            float[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        public void testToArrayTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            float[] array = tuple.toArray();
            assertArrayEquals(new float[] { 1.0f }, array, 0.001f);
        }

        @Test
        public void testToArrayTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array = tuple.toArray();
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, array, 0.001f);
        }

        @Test
        public void testToArrayModificationDoesNotAffectTuple() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array = tuple.toArray();
            array[0] = 999.0f;
            assertEquals(1.0f, tuple._1, 0.001f);
        }

        // toList tests
        @Test
        public void testToListTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            FloatList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        public void testToListTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            FloatList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(1.0f, list.get(0), 0.001f);
        }

        @Test
        public void testToListTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1.0f, list.get(0), 0.001f);
            assertEquals(2.0f, list.get(1), 0.001f);
            assertEquals(3.0f, list.get(2), 0.001f);
        }

        // forEach tests
        @Test
        public void testForEachTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        public void testForEachTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Float.valueOf(1.0f), result.get(0));
        }

        @Test
        public void testForEachTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Float.valueOf(1.0f), result.get(0));
            assertEquals(Float.valueOf(2.0f), result.get(1));
            assertEquals(Float.valueOf(3.0f), result.get(2));
        }

        // stream tests
        @Test
        public void testStreamTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            FloatStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        public void testStreamTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            FloatStream stream = tuple.stream();
            assertEquals(1.0f, stream.sum(), 0.001f);
        }

        @Test
        public void testStreamTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatStream stream = tuple.stream();
            assertEquals(6.0f, stream.sum(), 0.001f);
        }

        // hashCode tests
        @Test
        public void testHashCodeTuple1() {
            FloatTuple1 tuple1 = FloatTuple.of(1.0f);
            FloatTuple1 tuple2 = FloatTuple.of(1.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple3() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // equals tests
        @Test
        public void testEqualsSameObject() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEqualsNull() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotEquals(null, tuple);
        }

        @Test
        public void testEqualsDifferentClass() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        public void testEqualsTuple1() {
            FloatTuple1 tuple1 = FloatTuple.of(1.0f);
            FloatTuple1 tuple2 = FloatTuple.of(1.0f);
            FloatTuple1 tuple3 = FloatTuple.of(99.0f);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple2() {
            FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 tuple3 = FloatTuple.of(1.0f, 3.0f);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple3() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple3 = FloatTuple.of(1.0f, 2.0f, 4.0f);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        // toString tests
        @Test
        public void testToStringTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testToStringTuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            String str = tuple.toString();
            assertTrue(str.contains("1.0"));
        }

        @Test
        public void testToStringTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            String str = tuple.toString();
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("2.0"));
            assertTrue(str.contains("3.0"));
        }

        // Tuple2 special methods - accept
        @Test
        public void testTuple2Accept() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            List<Float> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Float.valueOf(3.0f), result.get(0));
            assertEquals(Float.valueOf(4.0f), result.get(1));
        }

        // Tuple2 special methods - map
        @Test
        public void testTuple2Map() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            float result = tuple.map((a, b) -> a * b);
            assertEquals(12.0f, result, 0.001f);
        }

        // Tuple2 special methods - filter
        @Test
        public void testTuple2FilterTrue() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            var result = tuple.filter((a, b) -> a + b > 5.0f);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2FilterFalse() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            var result = tuple.filter((a, b) -> a + b > 10.0f);
            assertFalse(result.isPresent());
        }

        // Tuple3 special methods - accept
        @Test
        public void testTuple3Accept() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Float.valueOf(1.0f), result.get(0));
            assertEquals(Float.valueOf(2.0f), result.get(1));
            assertEquals(Float.valueOf(3.0f), result.get(2));
        }

        // Tuple3 special methods - map
        @Test
        public void testTuple3Map() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0f, result, 0.001f);
        }

        // Tuple3 special methods - filter
        @Test
        public void testTuple3FilterTrue() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            var result = tuple.filter((a, b, c) -> a + b + c > 5.0f);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple3FilterFalse() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            var result = tuple.filter((a, b, c) -> a + b + c > 10.0f);
            assertFalse(result.isPresent());
        }

        // arity tests for all tuple sizes
        @Test
        public void testArity() {
            assertEquals(0, FloatTuple.copyOf(new float[0]).arity());
            assertEquals(1, FloatTuple.of(1.0f).arity());
            assertEquals(2, FloatTuple.of(1.0f, 2.0f).arity());
            assertEquals(3, FloatTuple.of(1.0f, 2.0f, 3.0f).arity());
            assertEquals(4, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f).arity());
            assertEquals(5, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f).arity());
            assertEquals(6, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f).arity());
            assertEquals(7, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f).arity());
            assertEquals(8, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f).arity());
            assertEquals(9, FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f).arity());
        }

        // Tests for inherited methods from PrimitiveTuple
        @Test
        public void testAcceptConsumer() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals(1.0f, result.get(0), 0.001f);
            assertEquals(2.0f, result.get(1), 0.001f);
            assertEquals(3.0f, result.get(2), 0.001f);
        }

        @Test
        public void testMapFunction() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float result = tuple.map(t -> t._1 + t._2 + t._3);
            assertEquals(6.0f, result, 0.001f);
        }

        @Test
        public void testFilterPredicate() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            var result = tuple.filter(t -> t._1 == 1.0f);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testFilterPredicateFalse() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            var result = tuple.filter(t -> t._1 == 99.0f);
            assertFalse(result.isPresent());
        }

        // Comprehensive tests for Tuple4 through Tuple9
        @Test
        public void testTuple4Operations() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);

            // Test reverse
            FloatTuple4 reversed = tuple.reverse();
            assertEquals(4.0f, reversed._1, 0.001f);
            assertEquals(3.0f, reversed._2, 0.001f);
            assertEquals(2.0f, reversed._3, 0.001f);
            assertEquals(1.0f, reversed._4, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(4.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, tuple.toArray(), 0.001f);

            // Test min/max/median/sum/average via base class
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(4.0f, tuple.max(), 0.001f);
            assertEquals(2.0f, tuple.median(), 0.001f);
            assertEquals(10.0f, tuple.sum(), 0.001f);
            assertEquals(2.5, tuple.average(), 0.001);

            // Test hashCode and equals
            FloatTuple4 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatTuple4 tuple3 = FloatTuple.of(1.0f, 2.0f, 3.0f, 5.0f);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            String str = tuple.toString();
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("4.0"));
        }

        @Test
        public void testTuple5Operations() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);

            // Test reverse
            FloatTuple5 reversed = tuple.reverse();
            assertEquals(5.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._5, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(5.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f }, tuple.toArray(), 0.001f);

            // Test statistical operations
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(5.0f, tuple.max(), 0.001f);
            assertEquals(3.0f, tuple.median(), 0.001f);
            assertEquals(15.0f, tuple.sum(), 0.001f);
            assertEquals(3.0, tuple.average(), 0.001);

            // Test equals
            FloatTuple5 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(tuple, tuple2);
        }

        @Test
        public void testTuple6Operations() {
            FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);

            // Test reverse
            FloatTuple6 reversed = tuple.reverse();
            assertEquals(6.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._6, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(6.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f }, tuple.toArray(), 0.001f);

            // Test statistical operations
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(6.0f, tuple.max(), 0.001f);
            assertEquals(21.0f, tuple.sum(), 0.001f);
            assertEquals(3.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple7Operations() {
            FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);

            // Test reverse
            FloatTuple7 reversed = tuple.reverse();
            assertEquals(7.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._7, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(7.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f }, tuple.toArray(), 0.001f);

            // Test statistical operations
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(7.0f, tuple.max(), 0.001f);
            assertEquals(28.0f, tuple.sum(), 0.001f);
            assertEquals(4.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple8Operations() {
            FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);

            // Test reverse
            FloatTuple8 reversed = tuple.reverse();
            assertEquals(8.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._8, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(8.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f }, tuple.toArray(), 0.001f);

            // Test statistical operations
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(8.0f, tuple.max(), 0.001f);
            assertEquals(36.0f, tuple.sum(), 0.001f);
            assertEquals(4.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple9Operations() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);

            // Test reverse
            FloatTuple9 reversed = tuple.reverse();
            assertEquals(9.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._9, 0.001f);

            // Test contains
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(9.0f));
            assertFalse(tuple.contains(99.0f));

            // Test toArray
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f }, tuple.toArray(), 0.001f);

            // Test statistical operations
            assertEquals(1.0f, tuple.min(), 0.001f);
            assertEquals(9.0f, tuple.max(), 0.001f);
            assertEquals(45.0f, tuple.sum(), 0.001f);
            assertEquals(5.0, tuple.average(), 0.001);
        }

        // Test create methods for sizes 2, 4-8
        @Test
        public void testCreate2Through8() {
            FloatTuple2 tuple2 = FloatTuple.copyOf(new float[] { 1.0f, 2.0f });
            assertEquals(1.0f, tuple2._1, 0.001f);
            assertEquals(2.0f, tuple2._2, 0.001f);

            FloatTuple4 tuple4 = FloatTuple.copyOf(new float[] { 1.0f, 2.0f, 3.0f, 4.0f });
            assertEquals(1.0f, tuple4._1, 0.001f);
            assertEquals(4.0f, tuple4._4, 0.001f);

            FloatTuple5 tuple5 = FloatTuple.copyOf(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f });
            assertEquals(1.0f, tuple5._1, 0.001f);
            assertEquals(5.0f, tuple5._5, 0.001f);

            FloatTuple6 tuple6 = FloatTuple.copyOf(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f });
            assertEquals(1.0f, tuple6._1, 0.001f);
            assertEquals(6.0f, tuple6._6, 0.001f);

            FloatTuple7 tuple7 = FloatTuple.copyOf(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f });
            assertEquals(1.0f, tuple7._1, 0.001f);
            assertEquals(7.0f, tuple7._7, 0.001f);

            FloatTuple8 tuple8 = FloatTuple.copyOf(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f });
            assertEquals(1.0f, tuple8._1, 0.001f);
            assertEquals(8.0f, tuple8._8, 0.001f);
        }

        // Test toList for larger tuples
        @Test
        public void testToListTuple4Through9() {
            FloatTuple4 tuple4 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals(4.0f, list4.get(3), 0.001f);

            FloatTuple9 tuple9 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            FloatList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals(9.0f, list9.get(8), 0.001f);
        }

        // Test forEach for larger tuples
        @Test
        public void testForEachTuple4() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Float.valueOf(4.0f), result.get(3));
        }

        // Test forEach override for Tuple2
        @Test
        public void testForEachTuple2Override() {
            FloatTuple2 tuple = FloatTuple.of(10.0f, 20.0f);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Float.valueOf(10.0f), result.get(0));
            assertEquals(Float.valueOf(20.0f), result.get(1));
        }

        // Test forEach override for Tuple3
        @Test
        public void testForEachTuple3Override() {
            FloatTuple3 tuple = FloatTuple.of(10.0f, 20.0f, 30.0f);
            List<Float> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Float.valueOf(30.0f), result.get(2));
        }

        // Test stream for larger tuples
        @Test
        public void testStreamTuple4Through9() {
            FloatTuple4 tuple4 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(10.0f, tuple4.stream().sum(), 0.001f);

            FloatTuple9 tuple9 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertEquals(45.0f, tuple9.stream().sum(), 0.001f);
        }

        // ==================== FloatTuple Nested Class Tests ====================

        // ============ FloatTuple1 Nested Class Tests ============

        @Test
        public void testFloatTuple1_arity() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testFloatTuple1_reverse() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            FloatTuple.FloatTuple1 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        public void testFloatTuple1_contains() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        public void testFloatTuple1_hashCode() {
            FloatTuple.FloatTuple1 tuple1 = FloatTuple.of(1.0f);
            FloatTuple.FloatTuple1 tuple2 = FloatTuple.of(1.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testFloatTuple1_equals() {
            FloatTuple.FloatTuple1 tuple1 = FloatTuple.of(1.0f);
            FloatTuple.FloatTuple1 tuple2 = FloatTuple.of(1.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testFloatTuple1_toString() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testFloatTuple1_forEach() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testFloatTuple1_min() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertNotNull(tuple.min());
        }

        @Test
        public void testFloatTuple1_max() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertNotNull(tuple.max());
        }

        @Test
        public void testFloatTuple1_median() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertNotNull(tuple.median());
        }

        @Test
        public void testFloatTuple1_sum() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testFloatTuple1_average() {
            FloatTuple.FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        // ============ FloatTuple2 Nested Class Tests ============

        @Test
        public void testFloatTuple2_arity() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testFloatTuple2_reverse() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            FloatTuple.FloatTuple2 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        public void testFloatTuple2_contains() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        public void testFloatTuple2_hashCode() {
            FloatTuple.FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple.FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testFloatTuple2_equals() {
            FloatTuple.FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple.FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testFloatTuple2_toString() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testFloatTuple2_forEach() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        public void testFloatTuple2_min() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotNull(tuple.min());
        }

        @Test
        public void testFloatTuple2_max() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotNull(tuple.max());
        }

        @Test
        public void testFloatTuple2_median() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotNull(tuple.median());
        }

        @Test
        public void testFloatTuple2_sum() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testFloatTuple2_average() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testFloatTuple2_accept_biConsumer() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testFloatTuple2_map_biFunction() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testFloatTuple2_filter_biPredicate() {
            FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        // ============ FloatTuple3 Nested Class Tests ============

        @Test
        public void testFloatTuple3_arity() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testFloatTuple3_reverse() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple.FloatTuple3 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        public void testFloatTuple3_contains() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        public void testFloatTuple3_hashCode() {
            FloatTuple.FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple.FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testFloatTuple3_equals() {
            FloatTuple.FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple.FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testFloatTuple3_toString() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testFloatTuple3_forEach() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        public void testFloatTuple3_min() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple.min());
        }

        @Test
        public void testFloatTuple3_max() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple.max());
        }

        @Test
        public void testFloatTuple3_median() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple.median());
        }

        @Test
        public void testFloatTuple3_sum() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testFloatTuple3_average() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testFloatTuple3_accept_triConsumer() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testFloatTuple3_map_triFunction() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testFloatTuple3_filter_triPredicate() {
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        // ============ FloatTuple4 Nested Class Tests ============

        @Test
        public void testFloatTuple4_arity() {
            FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testFloatTuple4_reverse() {
            FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatTuple.FloatTuple4 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        public void testFloatTuple4_contains() {
            FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        public void testFloatTuple4_hashCode() {
            FloatTuple.FloatTuple4 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatTuple.FloatTuple4 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testFloatTuple4_equals() {
            FloatTuple.FloatTuple4 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatTuple.FloatTuple4 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testFloatTuple4_toString() {
            FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testFloatTuple4_forEach() {
            FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        // ============ FloatTuple5 Nested Class Tests ============

        @Test
        public void testFloatTuple5_arity() {
            FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testFloatTuple5_reverse() {
            FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            FloatTuple.FloatTuple5 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        public void testFloatTuple5_contains() {
            FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        public void testFloatTuple5_hashCode() {
            FloatTuple.FloatTuple5 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            FloatTuple.FloatTuple5 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testFloatTuple5_equals() {
            FloatTuple.FloatTuple5 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            FloatTuple.FloatTuple5 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testFloatTuple5_toString() {
            FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testFloatTuple5_forEach() {
            FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        // ============ FloatTuple6 Nested Class Tests ============

        @Test
        public void testFloatTuple6_arity() {
            FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testFloatTuple6_reverse() {
            FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            FloatTuple.FloatTuple6 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        public void testFloatTuple6_contains() {
            FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        public void testFloatTuple6_hashCode() {
            FloatTuple.FloatTuple6 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            FloatTuple.FloatTuple6 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testFloatTuple6_equals() {
            FloatTuple.FloatTuple6 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            FloatTuple.FloatTuple6 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testFloatTuple6_toString() {
            FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testFloatTuple6_forEach() {
            FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        // ============ FloatTuple7 Nested Class Tests ============

        @Test
        public void testFloatTuple7_arity() {
            FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testFloatTuple7_reverse() {
            FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            FloatTuple.FloatTuple7 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        public void testFloatTuple7_contains() {
            FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        public void testFloatTuple7_hashCode() {
            FloatTuple.FloatTuple7 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            FloatTuple.FloatTuple7 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testFloatTuple7_equals() {
            FloatTuple.FloatTuple7 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            FloatTuple.FloatTuple7 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testFloatTuple7_toString() {
            FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testFloatTuple7_forEach() {
            FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        // ============ FloatTuple8 Nested Class Tests ============

        @Test
        public void testFloatTuple8_arity() {
            FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testFloatTuple8_reverse() {
            FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            FloatTuple.FloatTuple8 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        public void testFloatTuple8_contains() {
            FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        public void testFloatTuple8_hashCode() {
            FloatTuple.FloatTuple8 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            FloatTuple.FloatTuple8 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testFloatTuple8_equals() {
            FloatTuple.FloatTuple8 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            FloatTuple.FloatTuple8 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testFloatTuple8_toString() {
            FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testFloatTuple8_forEach() {
            FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        // ============ FloatTuple9 Nested Class Tests ============

        @Test
        public void testFloatTuple9_arity() {
            FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testFloatTuple9_reverse() {
            FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            FloatTuple.FloatTuple9 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._9);
            assertEquals(tuple._9, reversed._1);
        }

        @Test
        public void testFloatTuple9_contains() {
            FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertTrue(tuple.contains(1.0f));
        }

        @Test
        public void testFloatTuple9_hashCode() {
            FloatTuple.FloatTuple9 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            FloatTuple.FloatTuple9 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testFloatTuple9_equals() {
            FloatTuple.FloatTuple9 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            FloatTuple.FloatTuple9 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testFloatTuple9_toString() {
            FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testFloatTuple9_forEach() {
            FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

    }

    @Nested
    /**
     * Comprehensive test suite for FloatTuple and its nested classes.
     * Tests all public methods including factory methods, statistical operations,
     * collection conversions, and special methods in Tuple2 and Tuple3.
     */
    @Tag("2510")
    class FloatTuple2510Test extends TestBase {

        // Factory method tests
        @Test
        public void testOf1() {
            FloatTuple1 tuple = FloatTuple.of(3.14f);
            assertEquals(3.14f, tuple._1, 0.001f);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf2() {
            FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
            assertEquals(1.5f, tuple._1, 0.001f);
            assertEquals(2.5f, tuple._2, 0.001f);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testCreate1() {
            FloatTuple1 tuple = FloatTuple.copyOf(new float[] { 42.0f });
            assertEquals(42.0f, tuple._1, 0.001f);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate2() {
            FloatTuple2 tuple = FloatTuple.copyOf(new float[] { 1.0f, 2.0f });
            assertEquals(1.0f, tuple._1, 0.001f);
            assertEquals(2.0f, tuple._2, 0.001f);
        }

        // Min tests
        @Test
        public void testMinTuple1() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals(42.0f, tuple.min(), 0.001f);
        }

        @Test
        public void testMinWithNegativeValues() {
            FloatTuple3 tuple = FloatTuple.of(-5.0f, -1.0f, -10.0f);
            assertEquals(-10.0f, tuple.min(), 0.001f);
        }

        // Max tests
        @Test
        public void testMaxTuple1() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals(42.0f, tuple.max(), 0.001f);
        }

        @Test
        public void testMaxTuple7() {
            FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertEquals(7.0f, tuple.max(), 0.001f);
        }

        // Median tests
        @Test
        public void testMedianTuple1() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals(42.0f, tuple.median(), 0.001f);
        }

        @Test
        public void testMedianTuple3() {
            FloatTuple3 tuple = FloatTuple.of(30.0f, 10.0f, 20.0f);
            assertEquals(20.0f, tuple.median(), 0.001f);
        }

        @Test
        public void testMedianTuple4() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(2.0f, tuple.median(), 0.001f);
        }

        @Test
        public void testMedianTuple5() {
            FloatTuple5 tuple = FloatTuple.of(5.0f, 1.0f, 3.0f, 2.0f, 4.0f);
            assertEquals(3.0f, tuple.median(), 0.001f);
        }

        @Test
        public void testSumTuple1() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals(42.0f, tuple.sum(), 0.001f);
        }

        @Test
        public void testSumTuple4() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(10.0f, tuple.sum(), 0.001f);
        }

        @Test
        public void testSumTuple9() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertEquals(45.0f, tuple.sum(), 0.001f);
        }

        // Average tests
        @Test
        public void testAverageTuple1() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals(42.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple2() {
            FloatTuple2 tuple = FloatTuple.of(10.0f, 20.0f);
            assertEquals(15.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple6() {
            FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertEquals(3.5, tuple.average(), 0.001);
        }

        @Test
        public void testReverseTuple1() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            FloatTuple1 reversed = tuple.reverse();
            assertEquals(42.0f, reversed._1, 0.001f);
        }

        @Test
        public void testReverseTuple9() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            FloatTuple9 reversed = tuple.reverse();
            assertEquals(9.0f, reversed._1, 0.001f);
            assertEquals(1.0f, reversed._9, 0.001f);
        }

        @Test
        public void testContainsTuple1True() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertTrue(tuple.contains(42.0f));
        }

        @Test
        public void testContainsTuple1False() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertFalse(tuple.contains(41.0f));
        }

        @Test
        public void testContainsTuple9() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertTrue(tuple.contains(5.0f));
            assertFalse(tuple.contains(10.0f));
        }

        @Test
        public void testToArrayTuple1() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            float[] array = tuple.toArray();
            assertArrayEquals(new float[] { 42.0f }, array, 0.001f);
        }

        @Test
        public void testToArrayTuple9() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            float[] array = tuple.toArray();
            assertEquals(9, array.length);
            assertEquals(1.0f, array[0], 0.001f);
            assertEquals(9.0f, array[8], 0.001f);
        }

        @Test
        public void testToArrayImmutable() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array1 = tuple.toArray();
            float[] array2 = tuple.toArray();
            array1[0] = 999.0f;
            assertEquals(1.0f, array2[0], 0.001f);
        }

        @Test
        public void testToListTuple1() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            FloatList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(42.0f, list.get(0), 0.001f);
        }

        @Test
        public void testToListTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1.0f, list.get(0), 0.001f);
            assertEquals(3.0f, list.get(2), 0.001f);
        }

        // forEach tests
        @Test
        public void testForEachTuple0() {
            FloatTuple<FloatTuple0> tuple = FloatTuple.copyOf(new float[0]);
            final List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(0, collected.size());
        }

        @Test
        public void testForEachTuple1() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            final List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals(42.0f, collected.get(0), 0.001f);
        }

        @Test
        public void testForEachTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            final List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(3, collected.size());
            assertEquals(1.0f, collected.get(0), 0.001f);
            assertEquals(2.0f, collected.get(1), 0.001f);
            assertEquals(3.0f, collected.get(2), 0.001f);
        }

        @Test
        public void testForEachTuple2() {
            FloatTuple2 tuple = FloatTuple.of(10.0f, 20.0f);
            final List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(2, collected.size());
            assertEquals(10.0f, collected.get(0), 0.001f);
            assertEquals(20.0f, collected.get(1), 0.001f);
        }

        @Test
        public void testStreamTuple1() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            FloatStream stream = tuple.stream();
            assertEquals(1, stream.count());
        }

        @Test
        public void testStreamTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float sum = (float) tuple.stream().sum();
            assertEquals(6.0f, sum, 0.001f);
        }

        @Test
        public void testStreamTuple5() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            float max = tuple.stream().max().getAsFloat();
            assertEquals(5.0f, max, 0.001f);
        }

        // hashCode tests
        @Test
        public void testHashCodeTuple0() {
            FloatTuple<FloatTuple0> tuple1 = FloatTuple.copyOf(new float[0]);
            FloatTuple<FloatTuple0> tuple2 = FloatTuple.copyOf(new float[0]);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple1() {
            FloatTuple1 tuple1 = FloatTuple.of(42.0f);
            FloatTuple1 tuple2 = FloatTuple.of(42.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // equals tests
        @Test
        public void testEqualsTuple0() {
            FloatTuple<FloatTuple0> tuple1 = FloatTuple.copyOf(new float[0]);
            FloatTuple<FloatTuple0> tuple2 = FloatTuple.copyOf(new float[0]);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEqualsTuple1() {
            FloatTuple1 tuple1 = FloatTuple.of(42.0f);
            FloatTuple1 tuple2 = FloatTuple.of(42.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testNotEqualsDifferentValues() {
            FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 tuple2 = FloatTuple.of(2.0f, 3.0f);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testNotEqualsDifferentTypes() {
            FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple3 tuple3 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotEquals(tuple2, tuple3);
        }

        @Test
        public void testEqualsNull() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertNotEquals(tuple, null);
        }

        @Test
        public void testEqualsSelf() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testToStringTuple1() {
            FloatTuple1 tuple = FloatTuple.of(42.0f);
            assertEquals("(42.0)", tuple.toString());
        }

        @Test
        public void testToStringTuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals("(1.0, 2.0, 3.0)", tuple.toString());
        }

        // FloatTuple2 special methods
        @Test
        public void testTuple2Accept() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            final List<Float> values = new ArrayList<>();
            tuple.accept((a, b) -> {
                values.add(a);
                values.add(b);
            });
            assertEquals(2, values.size());
            assertEquals(3.0f, values.get(0), 0.001f);
            assertEquals(4.0f, values.get(1), 0.001f);
        }

        @Test
        public void testTuple2Filter() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            assertTrue(tuple.filter((a, b) -> a + b > 5).isPresent());
            assertFalse(tuple.filter((a, b) -> a + b > 10).isPresent());
        }

        // FloatTuple3 special methods
        @Test
        public void testTuple3Accept() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            final List<Float> values = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                values.add(a);
                values.add(b);
                values.add(c);
            });
            assertEquals(3, values.size());
            assertEquals(1.0f, values.get(0), 0.001f);
            assertEquals(2.0f, values.get(1), 0.001f);
            assertEquals(3.0f, values.get(2), 0.001f);
        }

        @Test
        public void testTuple3Filter() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.filter((a, b, c) -> a + b + c > 5).isPresent());
            assertFalse(tuple.filter((a, b, c) -> a + b + c > 10).isPresent());
        }

        // Edge cases and special values
        @Test
        public void testZeroValues() {
            FloatTuple3 tuple = FloatTuple.of(0.0f, 0.0f, 0.0f);
            assertEquals(0.0f, tuple.min(), 0.001f);
            assertEquals(0.0f, tuple.max(), 0.001f);
            assertEquals(0.0f, tuple.sum(), 0.001f);
        }

        @Test
        public void testNegativeValues() {
            FloatTuple3 tuple = FloatTuple.of(-1.0f, -2.0f, -3.0f);
            assertEquals(-3.0f, tuple.min(), 0.001f);
            assertEquals(-1.0f, tuple.max(), 0.001f);
            assertEquals(-6.0f, tuple.sum(), 0.001f);
        }

        @Test
        public void testMixedValues() {
            FloatTuple5 tuple = FloatTuple.of(-2.0f, 0.0f, 5.0f, -1.0f, 3.0f);
            assertEquals(-2.0f, tuple.min(), 0.001f);
            assertEquals(5.0f, tuple.max(), 0.001f);
            assertEquals(5.0f, tuple.sum(), 0.001f);
        }
    }

    @Nested
    @Tag("2511")
    class FloatTuple2511Test extends TestBase {

        // ============ Factory Method Tests - FloatTuple.of() ============

        @Test
        public void testOf_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(3.14f);
            assertNotNull(tuple);
            assertEquals(3.14f, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf_tuple2() {
            FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
            assertNotNull(tuple);
            assertEquals(1.5f, tuple._1);
            assertEquals(2.5f, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple);
            assertEquals(1.0f, tuple._1);
            assertEquals(2.0f, tuple._2);
            assertEquals(3.0f, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf_tuple4() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertNotNull(tuple);
            assertEquals(1.0f, tuple._1);
            assertEquals(2.0f, tuple._2);
            assertEquals(3.0f, tuple._3);
            assertEquals(4.0f, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertNotNull(tuple);
            assertEquals(1.0f, tuple._1);
            assertEquals(2.0f, tuple._2);
            assertEquals(3.0f, tuple._3);
            assertEquals(4.0f, tuple._4);
            assertEquals(5.0f, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf_tuple6() {
            FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertNotNull(tuple);
            assertEquals(1.0f, tuple._1);
            assertEquals(2.0f, tuple._2);
            assertEquals(3.0f, tuple._3);
            assertEquals(4.0f, tuple._4);
            assertEquals(5.0f, tuple._5);
            assertEquals(6.0f, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf_tuple7() {
            FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertNotNull(tuple);
            assertEquals(1.0f, tuple._1);
            assertEquals(7.0f, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf_tuple8() {
            FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertNotNull(tuple);
            assertEquals(1.0f, tuple._1);
            assertEquals(8.0f, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf_tuple9() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertNotNull(tuple);
            assertEquals(1.0f, tuple._1);
            assertEquals(9.0f, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // ============ Factory Method Tests - FloatTuple.copyOf() ============

        @Test
        public void testCreate_nullArray() {
            FloatTuple<?> tuple = FloatTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_emptyArray() {
            FloatTuple<?> tuple = FloatTuple.copyOf(new float[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_array1() {
            FloatTuple1 tuple = FloatTuple.copyOf(new float[] { 5.5f });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(5.5f, tuple._1);
        }

        @Test
        public void testCreate_array2() {
            FloatTuple2 tuple = FloatTuple.copyOf(new float[] { 1.1f, 2.2f });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals(1.1f, tuple._1);
            assertEquals(2.2f, tuple._2);
        }

        @Test
        public void testCreate_array3() {
            FloatTuple3 tuple = FloatTuple.copyOf(new float[] { 1.0f, 2.0f, 3.0f });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(1.0f, tuple._1);
            assertEquals(3.0f, tuple._3);
        }

        @Test
        public void testCreate_array9() {
            FloatTuple9 tuple = FloatTuple.copyOf(new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testCreate_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> FloatTuple.copyOf(new float[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        }

        // ============ Min/Max/Median Tests ============

        @Test
        public void testMin_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertEquals(5.5f, tuple.min());
        }

        @Test
        public void testMin_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(1.0f, tuple.min());
        }

        @Test
        public void testMin_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(10.5f, -3.2f, 0.0f, 7.1f, -10.0f);
            assertEquals(-10.0f, tuple.min());
        }

        @Test
        public void testMin_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, tuple::min);
        }

        @Test
        public void testMax_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertEquals(5.5f, tuple.max());
        }

        @Test
        public void testMax_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(3.0f, tuple.max());
        }

        @Test
        public void testMax_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(10.5f, -3.2f, 0.0f, 7.1f, -10.0f);
            assertEquals(10.5f, tuple.max());
        }

        @Test
        public void testMax_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, tuple::max);
        }

        @Test
        public void testMedian_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertEquals(5.5f, tuple.median());
        }

        @Test
        public void testMedian_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(30.0f, 10.0f, 20.0f);
            assertEquals(20.0f, tuple.median());
        }

        @Test
        public void testMedian_tuple4_even() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(2.0f, tuple.median());
        }

        @Test
        public void testMedian_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, tuple::median);
        }

        // ============ Sum/Average Tests ============

        @Test
        public void testSum_tuple0() {
            FloatTuple<?> tuple = FloatTuple.copyOf(new float[0]);
            assertEquals(0.0f, tuple.sum());
        }

        @Test
        public void testSum_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(3.14f);
            assertEquals(3.14f, tuple.sum(), 0.001f);
        }

        @Test
        public void testSum_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(6.0f, tuple.sum());
        }

        @Test
        public void testSum_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(15.0f, tuple.sum());
        }

        @Test
        public void testAverage_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0, tuple.average());
        }

        @Test
        public void testAverage_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(2.0, tuple.average());
        }

        @Test
        public void testAverage_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(2.0f, 4.0f, 6.0f, 8.0f, 10.0f);
            assertEquals(6.0, tuple.average());
        }

        @Test
        public void testAverage_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, tuple::average);
        }

        // ============ Reverse Tests ============

        @Test
        public void testReverse_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            FloatTuple1 reversed = tuple.reverse();
            assertEquals(5.5f, reversed._1);
        }

        @Test
        public void testReverse_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 reversed = tuple.reverse();
            assertEquals(3.0f, reversed._1);
            assertEquals(2.0f, reversed._2);
            assertEquals(1.0f, reversed._3);
        }

        @Test
        public void testReverse_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            FloatTuple5 reversed = tuple.reverse();
            assertEquals(5.0f, reversed._1);
            assertEquals(1.0f, reversed._5);
        }

        @Test
        public void testReverse_tuple0() {
            FloatTuple<?> tuple = FloatTuple.copyOf(new float[0]);
            FloatTuple<?> reversed = tuple.reverse();
            assertEquals(0, reversed.arity());
        }

        // ============ Contains Tests ============

        @Test
        public void testContains_tuple1_found() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertTrue(tuple.contains(5.5f));
        }

        @Test
        public void testContains_tuple1_notFound() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertFalse(tuple.contains(3.3f));
        }

        @Test
        public void testContains_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(2.0f));
            assertTrue(tuple.contains(3.0f));
            assertFalse(tuple.contains(4.0f));
        }

        @Test
        public void testContains_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.copyOf(new float[0]);
            assertFalse(tuple.contains(1.0f));
        }

        // ============ Array/List Conversion Tests ============

        @Test
        public void testToArray_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array = tuple.toArray();
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, array);
        }

        @Test
        public void testToArray_independence() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array1 = tuple.toArray();
            float[] array2 = tuple.toArray();
            assertNotSame(array1, array2);
            assertArrayEquals(array1, array2);
        }

        @Test
        public void testToList_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals(1.0f, list.get(0));
            assertEquals(3.0f, list.get(2));
        }

        @Test
        public void testToList_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            FloatList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals(5.5f, list.get(0));
        }

        // ============ Functional Methods Tests (ForEach, Stream) ============

        @Test
        public void testForEach_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(3, collected.size());
            assertEquals(1.0f, collected.get(0));
            assertEquals(3.0f, collected.get(2));
        }

        @Test
        public void testForEach_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            List<Float> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals(5.5f, collected.get(0));
        }

        @Test
        public void testStream_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            double sum = tuple.stream().sum();
            assertEquals(6.0, sum);
        }

        @Test
        public void testStream_tuple5() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            long count = tuple.stream().count();
            assertEquals(5, count);
        }

        // ============ Tuple2 Specific Functional Methods ============

        @Test
        public void testTuple2_accept() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            List<Float> results = new ArrayList<>();
            tuple.accept((a, b) -> {
                results.add(a);
                results.add(b);
            });
            assertEquals(2, results.size());
            assertEquals(3.0f, results.get(0));
            assertEquals(4.0f, results.get(1));
        }

        @Test
        public void testTuple2_map() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            float product = tuple.map((a, b) -> a * b);
            assertEquals(12.0f, product);
        }

        @Test
        public void testTuple2_filter() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            Optional<FloatTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());

            Optional<FloatTuple2> result2 = tuple.filter((a, b) -> a + b < 5);
            assertFalse(result2.isPresent());
        }

        // ============ Tuple3 Specific Functional Methods ============

        @Test
        public void testTuple3_accept() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> results = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                results.add(a);
                results.add(b);
                results.add(c);
            });
            assertEquals(3, results.size());
            assertEquals(1.0f, results.get(0));
            assertEquals(3.0f, results.get(2));
        }

        @Test
        public void testTuple3_map() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float product = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0f, product);
        }

        @Test
        public void testTuple3_filter() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            Optional<FloatTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());

            Optional<FloatTuple3> result2 = tuple.filter((a, b, c) -> a + b + c < 5);
            assertFalse(result2.isPresent());
        }

        // ============ Equality Tests ============

        @Test
        public void testEquals_sameTuple1() {
            FloatTuple1 tuple1 = FloatTuple.of(5.5f);
            FloatTuple1 tuple2 = FloatTuple.of(5.5f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_sameTuple3() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_differentTuple3() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 4.0f);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_differentTypes() {
            FloatTuple1 tuple1 = FloatTuple.of(5.5f);
            FloatTuple2 tuple2 = FloatTuple.of(5.5f, 1.0f);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_null() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            assertNotEquals(tuple, null);
        }
        // ============ HashCode Tests ============

        @Test
        public void testHashCode_consistency() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        public void testHashCode_different() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 4.0f);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // ============ ToString Tests ============

        @Test
        public void testToString_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(5.5f);
            String str = tuple.toString();
            assertNotNull(str);
            assertTrue(str.contains("5.5"));
        }

        @Test
        public void testToString_tuple3() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            String str = tuple.toString();
            assertNotNull(str);
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("2.0"));
            assertTrue(str.contains("3.0"));
        }

        @Test
        public void testToString_emptyTuple() {
            FloatTuple<?> tuple = FloatTuple.copyOf(new float[0]);
            String str = tuple.toString();
            assertEquals("()", str);
        }

        // ============ PrimitiveTuple Base Class Methods Tests ============

        @Test
        public void testAccept() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<FloatTuple3> results = new ArrayList<>();
            tuple.accept(results::add);
            assertEquals(1, results.size());
            assertEquals(tuple, results.get(0));
        }

        @Test
        public void testMap() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            String result = tuple.map(t -> "Sum: " + (t._1 + t._2 + t._3));
            assertEquals("Sum: 6.0", result);
        }

        @Test
        public void testFilter() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            Optional<FloatTuple3> result = tuple.filter(t -> t._1 > 0);
            assertTrue(result.isPresent());

            Optional<FloatTuple3> result2 = tuple.filter(t -> t._1 < 0);
            assertFalse(result2.isPresent());
        }

        // ============ Arity Tests ============

        @Test
        public void testArity_tuple0() {
            FloatTuple<?> tuple = FloatTuple.copyOf(new float[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testArity_tuple1() {
            FloatTuple1 tuple = FloatTuple.of(1.0f);
            assertEquals(1, tuple.arity());
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for FloatTuple and its nested tuple classes (FloatTuple0 through FloatTuple9).
     * Tests cover all public methods including factory methods, statistical operations, conversions, and edge cases.
     */
    @Tag("2512")
    class FloatTuple2512Test extends TestBase {

        // ===== Factory Method Tests =====

        @Test
        public void test_of_singleElement() {
            FloatTuple1 tuple = FloatTuple.of(3.14f);
            assertNotNull(tuple);
            assertEquals(3.14f, tuple._1, 0.0001f);
        }

        @Test
        public void test_of_twoElements() {
            FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
            assertNotNull(tuple);
            assertEquals(1.5f, tuple._1, 0.0001f);
            assertEquals(2.5f, tuple._2, 0.0001f);
        }

        @Test
        public void test_of_threeElements() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertNotNull(tuple);
            assertEquals(1.0f, tuple._1, 0.0001f);
            assertEquals(2.0f, tuple._2, 0.0001f);
            assertEquals(3.0f, tuple._3, 0.0001f);
        }

        @Test
        public void test_of_fourElements() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertNotNull(tuple);
            assertEquals(1.0f, tuple._1, 0.0001f);
            assertEquals(4.0f, tuple._4, 0.0001f);
        }

        @Test
        public void test_of_fiveElements() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertNotNull(tuple);
            assertEquals(5.0f, tuple._5, 0.0001f);
        }

        @Test
        public void test_of_sixElements() {
            FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertNotNull(tuple);
            assertEquals(6.0f, tuple._6, 0.0001f);
        }

        @Test
        public void test_of_sevenElements() {
            FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertNotNull(tuple);
            assertEquals(7.0f, tuple._7, 0.0001f);
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_of_eightElements() {
            FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertNotNull(tuple);
            assertEquals(8.0f, tuple._8, 0.0001f);
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_of_nineElements() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertNotNull(tuple);
            assertEquals(9.0f, tuple._9, 0.0001f);
        }

        @Test
        public void test_create_nullArray() {
            FloatTuple0 tuple = FloatTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_emptyArray() {
            FloatTuple0 tuple = FloatTuple.copyOf(new float[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_singleElementArray() {
            FloatTuple1 tuple = FloatTuple.copyOf(new float[] { 42.0f });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(42.0f, tuple._1, 0.0001f);
        }

        @Test
        public void test_create_multipleElementsArray() {
            FloatTuple3 tuple = FloatTuple.copyOf(new float[] { 1.0f, 2.0f, 3.0f });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(1.0f, tuple._1, 0.0001f);
            assertEquals(2.0f, tuple._2, 0.0001f);
            assertEquals(3.0f, tuple._3, 0.0001f);
        }

        @Test
        public void test_create_tooManyElements() {
            float[] array = new float[10];
            assertThrows(IllegalArgumentException.class, () -> FloatTuple.copyOf(array));
        }

        // ===== FloatTuple0 Tests =====

        @Test
        public void test_FloatTuple0_arity() {
            FloatTuple0 tuple = FloatTuple.copyOf(new float[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_FloatTuple0_min_throwsException() {
            FloatTuple0 tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void test_FloatTuple0_max_throwsException() {
            FloatTuple0 tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void test_FloatTuple0_median_throwsException() {
            FloatTuple0 tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void test_FloatTuple0_sum() {
            FloatTuple0 tuple = FloatTuple.copyOf(new float[0]);
            assertEquals(0.0f, tuple.sum(), 0.0001f);
        }

        @Test
        public void test_FloatTuple0_average_throwsException() {
            FloatTuple0 tuple = FloatTuple.copyOf(new float[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void test_FloatTuple0_reverse() {
            FloatTuple0 tuple = FloatTuple.copyOf(new float[0]);
            FloatTuple0 reversed = tuple.reverse();
            assertSame(tuple, reversed);
        }

        @Test
        public void test_FloatTuple0_contains() {
            FloatTuple0 tuple = FloatTuple.copyOf(new float[0]);
            assertFalse(tuple.contains(1.0f));
        }

        @Test
        public void test_FloatTuple0_toString() {
            FloatTuple0 tuple = FloatTuple.copyOf(new float[0]);
            assertEquals("()", tuple.toString());
        }

        // ===== FloatTuple1 Tests =====

        @Test
        public void test_FloatTuple1_arity() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void test_FloatTuple1_min() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0f, tuple.min(), 0.0001f);
        }

        @Test
        public void test_FloatTuple1_max() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0f, tuple.max(), 0.0001f);
        }

        @Test
        public void test_FloatTuple1_median() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0f, tuple.median(), 0.0001f);
        }

        @Test
        public void test_FloatTuple1_sum() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0f, tuple.sum(), 0.0001f);
        }

        @Test
        public void test_FloatTuple1_average() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(5.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_FloatTuple1_reverse() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            FloatTuple1 reversed = tuple.reverse();
            assertEquals(5.0f, reversed._1, 0.0001f);
        }

        @Test
        public void test_FloatTuple1_contains_found() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertTrue(tuple.contains(5.0f));
        }

        @Test
        public void test_FloatTuple1_contains_notFound() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertFalse(tuple.contains(10.0f));
        }

        @Test
        public void test_FloatTuple1_hashCode() {
            FloatTuple1 tuple1 = FloatTuple.of(5.0f);
            FloatTuple1 tuple2 = FloatTuple.of(5.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_FloatTuple1_equals_same() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals(tuple, tuple);
        }

        @Test
        public void test_FloatTuple1_equals_equal() {
            FloatTuple1 tuple1 = FloatTuple.of(5.0f);
            FloatTuple1 tuple2 = FloatTuple.of(5.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void test_FloatTuple1_equals_notEqual() {
            FloatTuple1 tuple1 = FloatTuple.of(5.0f);
            FloatTuple1 tuple2 = FloatTuple.of(10.0f);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void test_FloatTuple1_equals_null() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertNotEquals(null, tuple);
        }

        @Test
        public void test_FloatTuple1_equals_differentType() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertNotEquals("string", tuple);
        }

        @Test
        public void test_FloatTuple1_toString() {
            FloatTuple1 tuple = FloatTuple.of(5.0f);
            assertEquals("(5.0)", tuple.toString());
        }

        // ===== FloatTuple2 Tests =====

        @Test
        public void test_FloatTuple2_min() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 1.0f);
            assertEquals(1.0f, tuple.min(), 0.0001f);
        }

        @Test
        public void test_FloatTuple2_max() {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 1.0f);
            assertEquals(3.0f, tuple.max(), 0.0001f);
        }

        @Test
        public void test_FloatTuple2_median() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            float median = tuple.median();
            assertTrue(median == 1.0f || median == 2.0f);
        }

        @Test
        public void test_FloatTuple2_sum() {
            FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
            assertEquals(4.0f, tuple.sum(), 0.0001f);
        }

        @Test
        public void test_FloatTuple2_average() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 3.0f);
            assertEquals(2.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_FloatTuple2_reverse() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 reversed = tuple.reverse();
            assertEquals(2.0f, reversed._1, 0.0001f);
            assertEquals(1.0f, reversed._2, 0.0001f);
        }

        @Test
        public void test_FloatTuple2_contains_found() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertTrue(tuple.contains(1.0f));
            assertTrue(tuple.contains(2.0f));
        }

        @Test
        public void test_FloatTuple2_contains_notFound() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertFalse(tuple.contains(3.0f));
        }

        @Test
        public void test_FloatTuple2_forEach() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            List<Float> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals(1.0f, values.get(0), 0.0001f);
            assertEquals(2.0f, values.get(1), 0.0001f);
        }

        @Test
        public void test_FloatTuple2_accept() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            final float[] result = new float[1];
            tuple.accept((a, b) -> result[0] = a + b);
            assertEquals(7.0f, result[0], 0.0001f);
        }

        @Test
        public void test_FloatTuple2_map() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            Float result = tuple.map((a, b) -> a * b);
            assertEquals(12.0f, result, 0.0001f);
        }

        @Test
        public void test_FloatTuple2_filter_satisfied() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
            Optional<FloatTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void test_FloatTuple2_filter_notSatisfied() throws Exception {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 1.0f);
            Optional<FloatTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_FloatTuple2_hashCode() {
            FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_FloatTuple2_equals() {
            FloatTuple2 tuple1 = FloatTuple.of(1.0f, 2.0f);
            FloatTuple2 tuple2 = FloatTuple.of(1.0f, 2.0f);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void test_FloatTuple2_toString() {
            FloatTuple2 tuple = FloatTuple.of(1.0f, 2.0f);
            assertEquals("(1.0, 2.0)", tuple.toString());
        }

        // ===== FloatTuple3 Tests =====

        @Test
        public void test_FloatTuple3_min() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(1.0f, tuple.min(), 0.0001f);
        }

        @Test
        public void test_FloatTuple3_max() {
            FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(3.0f, tuple.max(), 0.0001f);
        }

        @Test
        public void test_FloatTuple3_median() {
            FloatTuple3 tuple = FloatTuple.of(30.0f, 10.0f, 20.0f);
            assertEquals(20.0f, tuple.median(), 0.0001f);
        }

        @Test
        public void test_FloatTuple3_sum() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(6.0f, tuple.sum(), 0.0001f);
        }

        @Test
        public void test_FloatTuple3_average() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(2.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_FloatTuple3_reverse() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 reversed = tuple.reverse();
            assertEquals(3.0f, reversed._1, 0.0001f);
            assertEquals(2.0f, reversed._2, 0.0001f);
            assertEquals(1.0f, reversed._3, 0.0001f);
        }

        @Test
        public void test_FloatTuple3_contains() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertTrue(tuple.contains(2.0f));
            assertFalse(tuple.contains(5.0f));
        }

        @Test
        public void test_FloatTuple3_forEach() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            List<Float> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        public void test_FloatTuple3_accept() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            final float[] result = new float[1];
            tuple.accept((a, b, c) -> result[0] = a + b + c);
            assertEquals(6.0f, result[0], 0.0001f);
        }

        @Test
        public void test_FloatTuple3_map() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            Float result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0f, result, 0.0001f);
        }

        @Test
        public void test_FloatTuple3_filter() throws Exception {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            Optional<FloatTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
        }

        // ===== FloatTuple4+ Tests =====

        @Test
        public void test_FloatTuple4_arity() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void test_FloatTuple4_statisticalOperations() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(1.0f, tuple.min(), 0.0001f);
            assertEquals(4.0f, tuple.max(), 0.0001f);
            assertEquals(10.0f, tuple.sum(), 0.0001f);
            assertEquals(2.5, tuple.average(), 0.0001);
        }

        @Test
        public void test_FloatTuple5_arity() {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void test_FloatTuple6_arity() {
            FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void test_FloatTuple7_arity() {
            FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
            assertEquals(7, tuple.arity());
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_FloatTuple8_arity() {
            FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
            assertEquals(8, tuple.arity());
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_FloatTuple9_arity() {
            FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
            assertEquals(9, tuple.arity());
        }

        // ===== Common Method Tests =====

        @Test
        public void test_toArray() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals(1.0f, array[0], 0.0001f);
            assertEquals(2.0f, array[1], 0.0001f);
            assertEquals(3.0f, array[2], 0.0001f);
        }

        @Test
        public void test_toArray_modification() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            float[] array = tuple.toArray();
            array[0] = 99.0f;
            assertEquals(1.0f, tuple._1, 0.0001f);
        }

        @Test
        public void test_toList() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1.0f, list.get(0), 0.0001f);
        }

        @Test
        public void test_forEach_multipleElements() throws Exception {
            FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            List<Float> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        public void test_stream() {
            FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            double sum = tuple.stream().sum();
            assertEquals(6.0, sum, 0.0001);
        }

        @Test
        public void test_equals_symmetric() {
            FloatTuple3 tuple1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            FloatTuple3 tuple2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }
        // ===== Edge Cases =====

        @Test
        public void test_negativeValues() {
            FloatTuple3 tuple = FloatTuple.of(-1.0f, -2.0f, -3.0f);
            assertEquals(-3.0f, tuple.min(), 0.0001f);
            assertEquals(-1.0f, tuple.max(), 0.0001f);
            assertEquals(-6.0f, tuple.sum(), 0.0001f);
        }

        @Test
        public void test_zeroValues() {
            FloatTuple3 tuple = FloatTuple.of(0.0f, 0.0f, 0.0f);
            assertEquals(0.0f, tuple.min(), 0.0001f);
            assertEquals(0.0f, tuple.max(), 0.0001f);
            assertEquals(0.0f, tuple.sum(), 0.0001f);
        }

        @Test
        public void test_largeValues() {
            FloatTuple2 tuple = FloatTuple.of(Float.MAX_VALUE, Float.MAX_VALUE / 2);
            assertEquals(Float.MAX_VALUE / 2, tuple.min(), 0.0001f);
            assertEquals(Float.MAX_VALUE, tuple.max(), 0.0001f);
        }

        @Test
        public void test_NaN_handling() {
            FloatTuple2 tuple = FloatTuple.of(Float.NaN, 1.0f);
            assertTrue(Float.isNaN(tuple._1));
            assertEquals(1.0f, tuple._2, 0.0001f);
        }

        @Test
        public void test_infinity_handling() {
            FloatTuple2 tuple = FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
            assertEquals(Float.POSITIVE_INFINITY, tuple._1, 0.0001f);
            assertEquals(Float.NEGATIVE_INFINITY, tuple._2, 0.0001f);
        }

        @Test
        public void test_reverse_largerTuples() {
            FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            FloatTuple4 reversed = tuple.reverse();
            assertEquals(4.0f, reversed._1, 0.0001f);
            assertEquals(3.0f, reversed._2, 0.0001f);
            assertEquals(2.0f, reversed._3, 0.0001f);
            assertEquals(1.0f, reversed._4, 0.0001f);
        }

        @Test
        public void test_contains_withNaN() {
            FloatTuple2 tuple = FloatTuple.of(Float.NaN, 1.0f);
            assertTrue(tuple.contains(Float.NaN));
        }
    }

    @Nested
    @SuppressWarnings("deprecation")
    @Tag("2512")
    class JavadocExampleTupleTest_FloatTuple extends TestBase {
        // ===== FloatTuple Javadoc examples =====

        @Test
        public void testFloatTupleOf1() {
            // FloatTuple.FloatTuple1 single = FloatTuple.of(3.14f);
            // float value = single._1;  // 3.14f
            FloatTuple.FloatTuple1 single = FloatTuple.of(3.14f);
            assertEquals(3.14f, single._1, 0.001f);
        }

        @Test
        public void testFloatTupleOf2() {
            // FloatTuple.FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
            // float first = pair._1;  // 1.5f
            // float second = pair._2;  // 2.5f
            FloatTuple.FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
            assertEquals(1.5f, pair._1, 0.001f);
            assertEquals(2.5f, pair._2, 0.001f);
        }

        @Test
        public void testFloatTupleOf3() {
            // FloatTuple.FloatTuple3 triple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            // float third = triple._3;  // 3.0f
            FloatTuple.FloatTuple3 triple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(3.0f, triple._3, 0.001f);
        }

        @Test
        public void testFloatTupleOf4() {
            // FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            // float fourth = tuple._4;  // 4.0f
            FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
            assertEquals(4.0f, tuple._4, 0.001f);
        }

        @Test
        public void testFloatTupleClassLevelExamples() {
            // float min = triple.min();   // 1.0f
            // float max = triple.max();   // 3.0f
            // double avg = triple.average();   // 2.0
            FloatTuple.FloatTuple3 triple = FloatTuple.of(1.0f, 2.0f, 3.0f);
            assertEquals(1.0f, triple.min(), 0.001f);
            assertEquals(3.0f, triple.max(), 0.001f);
            assertEquals(2.0, triple.average(), 0.001);
        }

        @Test
        public void testFloatTupleOf5Median() {
            // FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            // float median = tuple.median();   // 3.0f
            FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
            assertEquals(3.0f, tuple.median(), 0.001f);
        }

        @Test
        public void testFloatTupleOf6Sum() {
            // FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            // float sum = tuple.sum();   // 21.0f
            FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
            assertEquals(21.0f, tuple.sum(), 0.001f);
        }

        @Test
        public void testFloatTupleMin() {
            // FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            // float min = tuple.min();   // 1.0f
            FloatTuple.FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
            assertEquals(1.0f, tuple.min(), 0.001f);

            // FloatTuple.FloatTuple2 pair = FloatTuple.of(2.5f, 1.5f);
            // float minPair = pair.min();   // 1.5f
            FloatTuple.FloatTuple2 pair = FloatTuple.of(2.5f, 1.5f);
            assertEquals(1.5f, pair.min(), 0.001f);
        }
    }

}
