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
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.IntTuple.IntTuple0;
import com.landawn.abacus.util.IntTuple.IntTuple1;
import com.landawn.abacus.util.IntTuple.IntTuple2;
import com.landawn.abacus.util.IntTuple.IntTuple3;
import com.landawn.abacus.util.IntTuple.IntTuple4;
import com.landawn.abacus.util.IntTuple.IntTuple5;
import com.landawn.abacus.util.IntTuple.IntTuple6;
import com.landawn.abacus.util.IntTuple.IntTuple7;
import com.landawn.abacus.util.IntTuple.IntTuple8;
import com.landawn.abacus.util.IntTuple.IntTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.IntStream;

class IntTupleTest extends TestBase {

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
        IntTuple<?> empty = IntTuple.copyOf(null);
        assertEquals(0, empty.arity());

        empty = IntTuple.copyOf(new int[0]);
        assertEquals(0, empty.arity());

        // Test array with 1 element
        IntTuple.IntTuple1 tuple1 = IntTuple.copyOf(new int[] { 10 });
        assertEquals(1, tuple1.arity());
        assertEquals(10, tuple1._1);

        // Test array with 5 elements
        IntTuple.IntTuple5 tuple5 = IntTuple.copyOf(new int[] { 10, 20, 30, 40, 50 });
        assertEquals(5, tuple5.arity());
        assertEquals(50, tuple5._5);

        // Test array with 9 elements
        IntTuple.IntTuple9 tuple9 = IntTuple.copyOf(new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 });
        assertEquals(9, tuple9.arity());
        assertEquals(90, tuple9._9);

        // Test too many elements
        assertThrows(IllegalArgumentException.class, () -> IntTuple.copyOf(new int[10]));
    }

    @Test
    public void testMin() {
        IntTuple.IntTuple3 tuple = IntTuple.of(30, 10, 20);
        assertEquals(10, tuple.min());

        IntTuple.IntTuple0 empty = IntTuple.copyOf(new int[0]);
        assertThrows(NoSuchElementException.class, () -> empty.min());
    }

    @Test
    public void testMax() {
        IntTuple.IntTuple3 tuple = IntTuple.of(30, 10, 20);
        assertEquals(30, tuple.max());

        IntTuple.IntTuple0 empty = IntTuple.copyOf(new int[0]);
        assertThrows(NoSuchElementException.class, () -> empty.max());
    }

    @Test
    public void testMedian() {
        IntTuple.IntTuple3 tuple = IntTuple.of(30, 10, 20);
        assertEquals(20, tuple.median());

        IntTuple.IntTuple4 evenTuple = IntTuple.of(10, 20, 30, 40);
        assertEquals(20, tuple.median()); // Should be (20 + 30) / 2 = 25

        IntTuple.IntTuple0 empty = IntTuple.copyOf(new int[0]);
        assertThrows(NoSuchElementException.class, () -> empty.median());
    }

    @Test
    public void testSum() {
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
        assertEquals(60, tuple.sum());

        IntTuple.IntTuple0 empty = IntTuple.copyOf(new int[0]);
        assertEquals(0, empty.sum());
    }

    @Test
    public void testAverage() {
        IntTuple.IntTuple3 tuple = IntTuple.of(10, 20, 30);
        assertEquals(20.0, tuple.average());

        IntTuple.IntTuple0 empty = IntTuple.copyOf(new int[0]);
        assertThrows(NoSuchElementException.class, () -> empty.average());
    }

    @Test
    public void testReverse() {
        // Test Tuple0
        IntTuple.IntTuple0 empty = IntTuple.copyOf(new int[0]);
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
        IntTuple.IntTuple0 empty = IntTuple.copyOf(new int[0]);
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
        IntTuple.IntTuple0 empty = IntTuple.copyOf(new int[0]);
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

    @Nested
    /**
     * Comprehensive test suite for IntTuple and its nested classes.
     * Tests all public methods including factory methods, statistical operations,
     * collection conversions, and special methods in Tuple2 and Tuple3.
     */
    @Tag("2025")
    class IntTuple2025Test extends TestBase {

        // Factory method tests
        @Test
        public void testOf1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
            assertEquals(4, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf5() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
            assertEquals(4, tuple._4);
            assertEquals(5, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf6() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(1, tuple._1);
            assertEquals(6, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf7() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(1, tuple._1);
            assertEquals(7, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf8() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(1, tuple._1);
            assertEquals(8, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf9() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(1, tuple._1);
            assertEquals(9, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // Create method tests
        @Test
        public void testCreateEmpty() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreateNull() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate1() {
            IntTuple1 tuple = IntTuple.copyOf(new int[] { 42 });
            assertEquals(42, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate3() {
            IntTuple3 tuple = IntTuple.copyOf(new int[] { 1, 2, 3 });
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
        }

        @Test
        public void testCreate9() {
            IntTuple9 tuple = IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertEquals(1, tuple._1);
            assertEquals(9, tuple._9);
        }

        // Statistical method tests - min
        @Test
        public void testMinTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42, tuple.min());
        }

        @Test
        public void testMinWithNegatives() {
            IntTuple3 tuple = IntTuple.of(5, -10, 3);
            assertEquals(-10, tuple.min());
        }

        @Test
        public void testMinWithMaxValue() {
            IntTuple3 tuple = IntTuple.of(Integer.MAX_VALUE, 0, Integer.MIN_VALUE);
            assertEquals(Integer.MIN_VALUE, tuple.min());
        }

        @Test
        public void testMinTuple0ThrowsException() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        // Statistical method tests - max
        @Test
        public void testMaxTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42, tuple.max());
        }

        @Test
        public void testMaxWithNegatives() {
            IntTuple3 tuple = IntTuple.of(-5, -10, -3);
            assertEquals(-3, tuple.max());
        }

        @Test
        public void testMaxWithMaxValue() {
            IntTuple3 tuple = IntTuple.of(Integer.MAX_VALUE, 0, Integer.MIN_VALUE);
            assertEquals(Integer.MAX_VALUE, tuple.max());
        }

        @Test
        public void testMaxTuple0ThrowsException() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        // Statistical method tests - median
        @Test
        public void testMedianTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42, tuple.median());
        }

        @Test
        public void testMedianTuple2() {
            IntTuple2 tuple = IntTuple.of(5, 10);
            assertEquals(5, tuple.median());
        }

        @Test
        public void testMedianTuple0ThrowsException() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        // Statistical method tests - sum
        @Test
        public void testSumTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void testSumWithNegatives() {
            IntTuple3 tuple = IntTuple.of(10, -5, 3);
            assertEquals(8, tuple.sum());
        }

        // Statistical method tests - average
        @Test
        public void testAverageTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple2() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            assertEquals(15.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple0ThrowsException() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        // Reverse tests
        @Test
        public void testReverseTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            IntTuple<IntTuple0> reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        public void testReverseTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            IntTuple1 reversed = tuple.reverse();
            assertEquals(42, reversed._1);
        }

        // Contains tests
        @Test
        public void testContainsTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            assertFalse(tuple.contains(1));
        }

        @Test
        public void testContainsTuple1True() {
            IntTuple1 tuple = IntTuple.of(42);
            assertTrue(tuple.contains(42));
        }

        @Test
        public void testContainsTuple1False() {
            IntTuple1 tuple = IntTuple.of(42);
            assertFalse(tuple.contains(41));
        }

        @Test
        public void testContainsTuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(2));
            assertTrue(tuple.contains(3));
            assertFalse(tuple.contains(4));
        }

        @Test
        public void testContainsTuple9() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertTrue(tuple.contains(5));
            assertFalse(tuple.contains(10));
        }

        // toArray tests
        @Test
        public void testToArrayTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            int[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        public void testToArrayModificationDoesNotAffectTuple() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int[] array = tuple.toArray();
            array[0] = 999;
            assertEquals(1, tuple._1);
        }

        // toList tests
        @Test
        public void testToListTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            IntList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        public void testToListTuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1, list.get(0));
            assertEquals(2, list.get(1));
            assertEquals(3, list.get(2));
        }

        // forEach tests
        @Test
        public void testForEachTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        public void testForEachTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(42, result.get(0).intValue());
        }

        @Test
        public void testForEachTuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Integer.valueOf(1), result.get(0));
            assertEquals(Integer.valueOf(2), result.get(1));
            assertEquals(Integer.valueOf(3), result.get(2));
        }

        // stream tests
        @Test
        public void testStreamTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            IntStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        public void testStreamTuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            IntStream stream = tuple.stream();
            assertEquals(42, stream.sum());
        }

        @Test
        public void testStreamTuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntStream stream = tuple.stream();
            assertEquals(6, stream.sum());
        }

        // hashCode tests
        @Test
        public void testHashCodeTuple1() {
            IntTuple1 tuple1 = IntTuple.of(42);
            IntTuple1 tuple2 = IntTuple.of(42);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // equals tests
        @Test
        public void testEqualsSameObject() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEqualsNull() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotEquals(null, tuple);
        }

        @Test
        public void testEqualsDifferentClass() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        public void testEqualsTuple1() {
            IntTuple1 tuple1 = IntTuple.of(42);
            IntTuple1 tuple2 = IntTuple.of(42);
            IntTuple1 tuple3 = IntTuple.of(41);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple2() {
            IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple2 tuple2 = IntTuple.of(1, 2);
            IntTuple2 tuple3 = IntTuple.of(1, 3);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple3() {
            IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple3 = IntTuple.of(1, 2, 4);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        // toString tests
        @Test
        public void testToStringTuple0() {
            IntTuple<IntTuple0> tuple = IntTuple.copyOf(new int[0]);
            assertEquals("()", tuple.toString());
        }

        // Tuple2 special methods - accept
        @Test
        public void testTuple2Accept() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            List<Integer> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Integer.valueOf(3), result.get(0));
            assertEquals(Integer.valueOf(4), result.get(1));
        }

        // Tuple2 special methods - map
        @Test
        public void testTuple2Map() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            int result = tuple.map((a, b) -> a * b);
            assertEquals(12, result);
        }

        @Test
        public void testTuple2MapToString() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            String result = tuple.map((a, b) -> a + "+" + b + "=" + (a + b));
            assertEquals("3+4=7", result);
        }

        // Tuple2 special methods - filter
        @Test
        public void testTuple2FilterTrue() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            var result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2FilterFalse() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            var result = tuple.filter((a, b) -> a + b > 10);
            assertFalse(result.isPresent());
        }

        // Tuple3 special methods - accept
        @Test
        public void testTuple3Accept() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Integer.valueOf(1), result.get(0));
            assertEquals(Integer.valueOf(2), result.get(1));
            assertEquals(Integer.valueOf(3), result.get(2));
        }

        // Tuple3 special methods - map
        @Test
        public void testTuple3Map() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6, result);
        }

        @Test
        public void testTuple3MapToString() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            String result = tuple.map((a, b, c) -> a + "+" + b + "+" + c + "=" + (a + b + c));
            assertEquals("1+2+3=6", result);
        }

        // Tuple3 special methods - filter
        @Test
        public void testTuple3FilterTrue() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple3FilterFalse() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 10);
            assertFalse(result.isPresent());
        }

        // Edge cases with duplicates
        @Test
        public void testDuplicates() {
            IntTuple3 tuple = IntTuple.of(5, 5, 5);
            assertEquals(5, tuple.min());
            assertEquals(5, tuple.max());
            assertEquals(5, tuple.median());
            assertEquals(15, tuple.sum());
            assertEquals(5.0, tuple.average(), 0.001);
        }

        // Edge cases with extreme values
        @Test
        public void testExtremeValues() {
            IntTuple3 tuple = IntTuple.of(Integer.MAX_VALUE, Integer.MIN_VALUE, 0);
            assertEquals(Integer.MIN_VALUE, tuple.min());
            assertEquals(Integer.MAX_VALUE, tuple.max());
            assertEquals(0, tuple.median());
        }

        // arity tests for all tuple sizes
        @Test
        public void testArity() {
            assertEquals(0, IntTuple.copyOf(new int[0]).arity());
            assertEquals(1, IntTuple.of(1).arity());
            assertEquals(2, IntTuple.of(1, 2).arity());
            assertEquals(3, IntTuple.of(1, 2, 3).arity());
            assertEquals(4, IntTuple.of(1, 2, 3, 4).arity());
            assertEquals(5, IntTuple.of(1, 2, 3, 4, 5).arity());
            assertEquals(6, IntTuple.of(1, 2, 3, 4, 5, 6).arity());
            assertEquals(7, IntTuple.of(1, 2, 3, 4, 5, 6, 7).arity());
            assertEquals(8, IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8).arity());
            assertEquals(9, IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9).arity());
        }

        // Tests for inherited methods from PrimitiveTuple
        @Test
        public void testAcceptConsumer() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals(1, result.get(0));
            assertEquals(2, result.get(1));
            assertEquals(3, result.get(2));
        }

        @Test
        public void testFilterPredicate() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            var result = tuple.filter(t -> t._1 == 1);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testFilterPredicateFalse() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            var result = tuple.filter(t -> t._1 == 99);
            assertFalse(result.isPresent());
        }

        @Test
        public void testToOptional() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        // Comprehensive tests for Tuple4 through Tuple9
        @Test
        public void testTuple4Operations() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);

            // Test reverse
            IntTuple4 reversed = tuple.reverse();
            assertEquals(4, reversed._1);
            assertEquals(3, reversed._2);
            assertEquals(2, reversed._3);
            assertEquals(1, reversed._4);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(4));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4 }, tuple.toArray());

            // Test min/max/median/sum/average
            assertEquals(1, tuple.min());
            assertEquals(4, tuple.max());
            assertEquals(2, tuple.median());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average(), 0.001);

            // Test hashCode and equals
            IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
            IntTuple4 tuple3 = IntTuple.of(1, 2, 3, 5);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4)", tuple.toString());
        }

        @Test
        public void testTuple5Operations() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);

            // Test reverse
            IntTuple5 reversed = tuple.reverse();
            assertEquals(5, reversed._1);
            assertEquals(4, reversed._2);
            assertEquals(3, reversed._3);
            assertEquals(2, reversed._4);
            assertEquals(1, reversed._5);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(5));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, tuple.toArray());

            // Test statistical operations
            assertEquals(1, tuple.min());
            assertEquals(5, tuple.max());
            assertEquals(3, tuple.median());
            assertEquals(15, tuple.sum());
            assertEquals(3.0, tuple.average(), 0.001);

            // Test hashCode and equals
            IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple5 tuple3 = IntTuple.of(1, 2, 3, 4, 6);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4, 5)", tuple.toString());
        }

        @Test
        public void testTuple6Operations() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);

            // Test reverse
            IntTuple6 reversed = tuple.reverse();
            assertEquals(6, reversed._1);
            assertEquals(5, reversed._2);
            assertEquals(4, reversed._3);
            assertEquals(3, reversed._4);
            assertEquals(2, reversed._5);
            assertEquals(1, reversed._6);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(6));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, tuple.toArray());

            // Test statistical operations
            assertEquals(1, tuple.min());
            assertEquals(6, tuple.max());
            assertEquals(3, tuple.median());
            assertEquals(21, tuple.sum());
            assertEquals(3.5, tuple.average(), 0.001);

            // Test hashCode and equals
            IntTuple6 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6);
            IntTuple6 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 7);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4, 5, 6)", tuple.toString());
        }

        @Test
        public void testTuple7Operations() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);

            // Test reverse
            IntTuple7 reversed = tuple.reverse();
            assertEquals(7, reversed._1);
            assertEquals(6, reversed._2);
            assertEquals(5, reversed._3);
            assertEquals(4, reversed._4);
            assertEquals(3, reversed._5);
            assertEquals(2, reversed._6);
            assertEquals(1, reversed._7);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(7));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7 }, tuple.toArray());

            // Test statistical operations
            assertEquals(1, tuple.min());
            assertEquals(7, tuple.max());
            assertEquals(4, tuple.median());
            assertEquals(28, tuple.sum());
            assertEquals(4.0, tuple.average(), 0.001);

            // Test hashCode and equals
            IntTuple7 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            IntTuple7 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 6, 8);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4, 5, 6, 7)", tuple.toString());
        }

        @Test
        public void testTuple8Operations() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);

            // Test reverse
            IntTuple8 reversed = tuple.reverse();
            assertEquals(8, reversed._1);
            assertEquals(7, reversed._2);
            assertEquals(6, reversed._3);
            assertEquals(5, reversed._4);
            assertEquals(4, reversed._5);
            assertEquals(3, reversed._6);
            assertEquals(2, reversed._7);
            assertEquals(1, reversed._8);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(8));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, tuple.toArray());

            // Test statistical operations
            assertEquals(1, tuple.min());
            assertEquals(8, tuple.max());
            assertEquals(4, tuple.median());
            assertEquals(36, tuple.sum());
            assertEquals(4.5, tuple.average(), 0.001);

            // Test hashCode and equals
            IntTuple8 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            IntTuple8 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 9);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4, 5, 6, 7, 8)", tuple.toString());
        }

        @Test
        public void testTuple9Operations() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

            // Test reverse
            IntTuple9 reversed = tuple.reverse();
            assertEquals(9, reversed._1);
            assertEquals(8, reversed._2);
            assertEquals(7, reversed._3);
            assertEquals(6, reversed._4);
            assertEquals(5, reversed._5);
            assertEquals(4, reversed._6);
            assertEquals(3, reversed._7);
            assertEquals(2, reversed._8);
            assertEquals(1, reversed._9);

            // Test contains
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(9));
            assertFalse(tuple.contains(99));

            // Test toArray
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, tuple.toArray());

            // Test statistical operations
            assertEquals(1, tuple.min());
            assertEquals(9, tuple.max());
            assertEquals(5, tuple.median());
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average(), 0.001);

            // Test hashCode and equals
            IntTuple9 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntTuple9 tuple3 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 10);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            assertEquals("(1, 2, 3, 4, 5, 6, 7, 8, 9)", tuple.toString());
        }

        // Test create methods for sizes 2, 4-8
        @Test
        public void testCreate2Through8() {
            IntTuple2 tuple2 = IntTuple.copyOf(new int[] { 1, 2 });
            assertEquals(1, tuple2._1);
            assertEquals(2, tuple2._2);

            IntTuple4 tuple4 = IntTuple.copyOf(new int[] { 1, 2, 3, 4 });
            assertEquals(1, tuple4._1);
            assertEquals(4, tuple4._4);

            IntTuple5 tuple5 = IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5 });
            assertEquals(1, tuple5._1);
            assertEquals(5, tuple5._5);

            IntTuple6 tuple6 = IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5, 6 });
            assertEquals(1, tuple6._1);
            assertEquals(6, tuple6._6);

            IntTuple7 tuple7 = IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals(1, tuple7._1);
            assertEquals(7, tuple7._7);

            IntTuple8 tuple8 = IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals(1, tuple8._1);
            assertEquals(8, tuple8._8);
        }

        // Test toList for larger tuples
        @Test
        public void testToListTuple4Through9() {
            IntTuple4 tuple4 = IntTuple.of(1, 2, 3, 4);
            IntList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals(1, list4.get(0));
            assertEquals(4, list4.get(3));

            IntTuple5 tuple5 = IntTuple.of(1, 2, 3, 4, 5);
            IntList list5 = tuple5.toList();
            assertEquals(5, list5.size());
            assertEquals(5, list5.get(4));

            IntTuple6 tuple6 = IntTuple.of(1, 2, 3, 4, 5, 6);
            IntList list6 = tuple6.toList();
            assertEquals(6, list6.size());
            assertEquals(6, list6.get(5));

            IntTuple7 tuple7 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            IntList list7 = tuple7.toList();
            assertEquals(7, list7.size());
            assertEquals(7, list7.get(6));

            IntTuple8 tuple8 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            IntList list8 = tuple8.toList();
            assertEquals(8, list8.size());
            assertEquals(8, list8.get(7));

            IntTuple9 tuple9 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals(9, list9.get(8));
        }

        // Test forEach for larger tuples
        @Test
        public void testForEachTuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Integer.valueOf(1), result.get(0));
            assertEquals(Integer.valueOf(2), result.get(1));
            assertEquals(Integer.valueOf(3), result.get(2));
            assertEquals(Integer.valueOf(4), result.get(3));
        }

        // Test forEach override for Tuple2
        @Test
        public void testForEachTuple2Override() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Integer.valueOf(10), result.get(0));
            assertEquals(Integer.valueOf(20), result.get(1));
        }

        // Test forEach override for Tuple3
        @Test
        public void testForEachTuple3Override() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            List<Integer> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Integer.valueOf(10), result.get(0));
            assertEquals(Integer.valueOf(20), result.get(1));
            assertEquals(Integer.valueOf(30), result.get(2));
        }

        // Test stream for larger tuples
        @Test
        public void testStreamTuple4Through9() {
            IntTuple4 tuple4 = IntTuple.of(1, 2, 3, 4);
            assertEquals(10, tuple4.stream().sum());

            IntTuple5 tuple5 = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(15, tuple5.stream().sum());

            IntTuple6 tuple6 = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(21, tuple6.stream().sum());

            IntTuple7 tuple7 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(28, tuple7.stream().sum());

            IntTuple8 tuple8 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(36, tuple8.stream().sum());

            IntTuple9 tuple9 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(45, tuple9.stream().sum());
        }

        // ==================== IntTuple Nested Class Tests ====================

        // ============ IntTuple1 Nested Class Tests ============

        @Test
        public void testIntTuple1_arity() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testIntTuple1_reverse() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            IntTuple.IntTuple1 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        public void testIntTuple1_contains() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertTrue(tuple.contains(1));
        }

        @Test
        public void testIntTuple1_hashCode() {
            IntTuple.IntTuple1 tuple1 = IntTuple.of(1);
            IntTuple.IntTuple1 tuple2 = IntTuple.of(1);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testIntTuple1_equals() {
            IntTuple.IntTuple1 tuple1 = IntTuple.of(1);
            IntTuple.IntTuple1 tuple2 = IntTuple.of(1);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testIntTuple1_toString() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testIntTuple1_forEach() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testIntTuple1_min() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertNotNull(tuple.min());
        }

        @Test
        public void testIntTuple1_max() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertNotNull(tuple.max());
        }

        @Test
        public void testIntTuple1_median() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertNotNull(tuple.median());
        }

        @Test
        public void testIntTuple1_sum() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testIntTuple1_average() {
            IntTuple.IntTuple1 tuple = IntTuple.of(1);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        // ============ IntTuple2 Nested Class Tests ============

        @Test
        public void testIntTuple2_arity() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testIntTuple2_reverse() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            IntTuple.IntTuple2 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        public void testIntTuple2_contains() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertTrue(tuple.contains(1));
        }

        @Test
        public void testIntTuple2_hashCode() {
            IntTuple.IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple.IntTuple2 tuple2 = IntTuple.of(1, 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testIntTuple2_equals() {
            IntTuple.IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple.IntTuple2 tuple2 = IntTuple.of(1, 2);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testIntTuple2_toString() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testIntTuple2_forEach() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        public void testIntTuple2_min() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertNotNull(tuple.min());
        }

        @Test
        public void testIntTuple2_max() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertNotNull(tuple.max());
        }

        @Test
        public void testIntTuple2_median() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertNotNull(tuple.median());
        }

        @Test
        public void testIntTuple2_sum() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testIntTuple2_average() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testIntTuple2_accept_biConsumer() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testIntTuple2_map_biFunction() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testIntTuple2_filter_biPredicate() {
            IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        // ============ IntTuple3 Nested Class Tests ============

        @Test
        public void testIntTuple3_arity() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testIntTuple3_reverse() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntTuple.IntTuple3 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        public void testIntTuple3_contains() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.contains(1));
        }

        @Test
        public void testIntTuple3_hashCode() {
            IntTuple.IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple.IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testIntTuple3_equals() {
            IntTuple.IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple.IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testIntTuple3_toString() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testIntTuple3_forEach() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        public void testIntTuple3_min() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple.min());
        }

        @Test
        public void testIntTuple3_max() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple.max());
        }

        @Test
        public void testIntTuple3_median() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple.median());
        }

        @Test
        public void testIntTuple3_sum() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testIntTuple3_average() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testIntTuple3_accept_triConsumer() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testIntTuple3_map_triFunction() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testIntTuple3_filter_triPredicate() {
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        // ============ IntTuple4 Nested Class Tests ============

        @Test
        public void testIntTuple4_arity() {
            IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testIntTuple4_reverse() {
            IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            IntTuple.IntTuple4 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        public void testIntTuple4_contains() {
            IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertTrue(tuple.contains(1));
        }

        @Test
        public void testIntTuple4_hashCode() {
            IntTuple.IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
            IntTuple.IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testIntTuple4_equals() {
            IntTuple.IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
            IntTuple.IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testIntTuple4_toString() {
            IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testIntTuple4_forEach() {
            IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        // ============ IntTuple5 Nested Class Tests ============

        @Test
        public void testIntTuple5_arity() {
            IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testIntTuple5_reverse() {
            IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple.IntTuple5 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        public void testIntTuple5_contains() {
            IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertTrue(tuple.contains(1));
        }

        @Test
        public void testIntTuple5_hashCode() {
            IntTuple.IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple.IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testIntTuple5_equals() {
            IntTuple.IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple.IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testIntTuple5_toString() {
            IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testIntTuple5_forEach() {
            IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        // ============ IntTuple6 Nested Class Tests ============

        @Test
        public void testIntTuple6_arity() {
            IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testIntTuple6_reverse() {
            IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            IntTuple.IntTuple6 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        public void testIntTuple6_contains() {
            IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertTrue(tuple.contains(1));
        }

        @Test
        public void testIntTuple6_hashCode() {
            IntTuple.IntTuple6 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6);
            IntTuple.IntTuple6 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testIntTuple6_equals() {
            IntTuple.IntTuple6 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6);
            IntTuple.IntTuple6 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testIntTuple6_toString() {
            IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testIntTuple6_forEach() {
            IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        // ============ IntTuple7 Nested Class Tests ============

        @Test
        public void testIntTuple7_arity() {
            IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testIntTuple7_reverse() {
            IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            IntTuple.IntTuple7 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        public void testIntTuple7_contains() {
            IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertTrue(tuple.contains(1));
        }

        @Test
        public void testIntTuple7_hashCode() {
            IntTuple.IntTuple7 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            IntTuple.IntTuple7 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testIntTuple7_equals() {
            IntTuple.IntTuple7 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            IntTuple.IntTuple7 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testIntTuple7_toString() {
            IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testIntTuple7_forEach() {
            IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        // ============ IntTuple8 Nested Class Tests ============

        @Test
        public void testIntTuple8_arity() {
            IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testIntTuple8_reverse() {
            IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            IntTuple.IntTuple8 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        public void testIntTuple8_contains() {
            IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertTrue(tuple.contains(1));
        }

        @Test
        public void testIntTuple8_hashCode() {
            IntTuple.IntTuple8 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            IntTuple.IntTuple8 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testIntTuple8_equals() {
            IntTuple.IntTuple8 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            IntTuple.IntTuple8 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testIntTuple8_toString() {
            IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testIntTuple8_forEach() {
            IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        // ============ IntTuple9 Nested Class Tests ============

        @Test
        public void testIntTuple9_arity() {
            IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testIntTuple9_reverse() {
            IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntTuple.IntTuple9 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._9);
            assertEquals(tuple._9, reversed._1);
        }

        @Test
        public void testIntTuple9_contains() {
            IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertTrue(tuple.contains(1));
        }

        @Test
        public void testIntTuple9_hashCode() {
            IntTuple.IntTuple9 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntTuple.IntTuple9 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testIntTuple9_equals() {
            IntTuple.IntTuple9 tuple1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntTuple.IntTuple9 tuple2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testIntTuple9_toString() {
            IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testIntTuple9_forEach() {
            IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

    }

    @Nested
    @Tag("2510")
    class IntTuple2510Test extends TestBase {

        @Test
        public void testOf1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(1, tuple.arity());
            assertEquals(42, tuple._1);
        }

        @Test
        public void testOf2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertEquals(2, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
        }

        @Test
        public void testOf3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(3, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
        }

        @Test
        public void testOf4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(4, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
            assertEquals(4, tuple._4);
        }

        @Test
        public void testOf5() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(5, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(5, tuple._5);
        }

        @Test
        public void testOf6() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(6, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(6, tuple._6);
        }

        @Test
        public void testOf7() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(7, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(7, tuple._7);
        }

        @Test
        public void testOf8() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(8, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(8, tuple._8);
        }

        @Test
        public void testOf9() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(9, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(9, tuple._9);
        }

        @Test
        public void testCreate_nullArray() {
            IntTuple<?> tuple = IntTuple.copyOf(null);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_emptyArray() {
            IntTuple<?> tuple = IntTuple.copyOf(new int[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_sizeOne() {
            IntTuple1 tuple = IntTuple.copyOf(new int[] { 42 });
            assertEquals(1, tuple.arity());
            assertEquals(42, tuple._1);
        }

        @Test
        public void testCreate_sizeTwo() {
            IntTuple2 tuple = IntTuple.copyOf(new int[] { 1, 2 });
            assertEquals(2, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
        }

        @Test
        public void testCreate_sizeThree() {
            IntTuple3 tuple = IntTuple.copyOf(new int[] { 1, 2, 3 });
            assertEquals(3, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(3, tuple._3);
        }

        @Test
        public void testCreate_sizeFour() {
            IntTuple4 tuple = IntTuple.copyOf(new int[] { 1, 2, 3, 4 });
            assertEquals(4, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(4, tuple._4);
        }

        @Test
        public void testCreate_sizeFive() {
            IntTuple5 tuple = IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5 });
            assertEquals(5, tuple.arity());
            assertEquals(5, tuple._5);
        }

        @Test
        public void testCreate_sizeSix() {
            IntTuple6 tuple = IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5, 6 });
            assertEquals(6, tuple.arity());
            assertEquals(6, tuple._6);
        }

        @Test
        public void testCreate_sizeSeven() {
            IntTuple7 tuple = IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals(7, tuple.arity());
            assertEquals(7, tuple._7);
        }

        @Test
        public void testCreate_sizeEight() {
            IntTuple8 tuple = IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple._8);
        }

        @Test
        public void testCreate_sizeNine() {
            IntTuple9 tuple = IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertEquals(9, tuple.arity());
            assertEquals(9, tuple._9);
        }

        @Test
        public void testCreate_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        }

        @Test
        public void testMin_tuple2() {
            IntTuple2 tuple = IntTuple.of(5, 2);
            assertEquals(2, tuple.min());
        }

        @Test
        public void testMin_tuple3() {
            IntTuple3 tuple = IntTuple.of(5, 2, 8);
            assertEquals(2, tuple.min());
        }

        @Test
        public void testMin_tuple4() {
            IntTuple4 tuple = IntTuple.of(5, 2, 8, 1);
            assertEquals(1, tuple.min());
        }

        @Test
        public void testMax_tuple2() {
            IntTuple2 tuple = IntTuple.of(5, 2);
            assertEquals(5, tuple.max());
        }

        @Test
        public void testMax_tuple3() {
            IntTuple3 tuple = IntTuple.of(5, 2, 8);
            assertEquals(8, tuple.max());
        }

        @Test
        public void testMedian_tuple2() {
            IntTuple2 tuple = IntTuple.of(5, 2);
            assertEquals(2, tuple.median());
        }

        @Test
        public void testMedian_tuple3() {
            IntTuple3 tuple = IntTuple.of(3, 1, 2);
            assertEquals(2, tuple.median());
        }

        @Test
        public void testMedian_tuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(2, tuple.median());
        }

        @Test
        public void testSum_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42, tuple.sum());
        }

        @Test
        public void testSum_tuple2() {
            IntTuple2 tuple = IntTuple.of(5, 2);
            assertEquals(7, tuple.sum());
        }

        @Test
        public void testAverage_tuple2() {
            IntTuple2 tuple = IntTuple.of(4, 6);
            assertEquals(5.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverage_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(2.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverage_tuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(2.5, tuple.average(), 0.001);
        }

        @Test
        public void testReverse_tuple1() {
            IntTuple1 tuple = IntTuple.of(1);
            IntTuple1 reversed = tuple.reverse();
            assertEquals(1, reversed._1);
        }

        @Test
        public void testReverse_tuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            IntTuple4 reversed = tuple.reverse();
            assertEquals(4, reversed._1);
            assertEquals(1, reversed._4);
        }

        @Test
        public void testContains_tuple1_notFound() {
            IntTuple1 tuple = IntTuple.of(42);
            assertFalse(tuple.contains(100));
        }

        @Test
        public void testContains_tuple2_found() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertTrue(tuple.contains(2));
        }

        @Test
        public void testContains_tuple2_notFound() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertFalse(tuple.contains(5));
        }

        @Test
        public void testContains_tuple3_found() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.contains(2));
        }

        @Test
        public void testContains_tuple3_notFound() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertFalse(tuple.contains(5));
        }

        @Test
        public void testToArray_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            int[] array = tuple.toArray();
            assertArrayEquals(new int[] { 42 }, array);
        }

        @Test
        public void testToArray_tuple2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            int[] array = tuple.toArray();
            assertArrayEquals(new int[] { 1, 2 }, array);
        }

        @Test
        public void testToArray_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int[] array = tuple.toArray();
            assertArrayEquals(new int[] { 1, 2, 3 }, array);
        }

        @Test
        public void testToList_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            IntList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(42, list.get(0));
        }

        @Test
        public void testToList_tuple2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            IntList list = tuple.toList();
            assertEquals(2, list.size());
            assertEquals(1, list.get(0));
            assertEquals(2, list.get(1));
        }

        @Test
        public void testToList_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(3, list.get(2));
        }

        @Test
        public void testForEach_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(42, sum.get());
        }

        @Test
        public void testForEach_tuple2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(3, sum.get());
        }

        @Test
        public void testForEach_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(6, sum.get());
        }

        @Test
        public void testForEach_tuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(10, sum.get());
        }

        @Test
        public void testStream_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            int sum = tuple.stream().sum();
            assertEquals(42, sum);
        }

        @Test
        public void testStream_tuple2() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            int sum = tuple.stream().sum();
            assertEquals(3, sum);
        }

        @Test
        public void testStream_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int sum = tuple.stream().sum();
            assertEquals(6, sum);
        }

        @Test
        public void testHashCode_tuple1_different() {
            IntTuple1 tuple1 = IntTuple.of(42);
            IntTuple1 tuple2 = IntTuple.of(100);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCode_tuple3() {
            IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testEquals_tuple1_same() {
            IntTuple1 tuple1 = IntTuple.of(42);
            IntTuple1 tuple2 = IntTuple.of(42);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple1_sameObject() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEquals_tuple1_different() {
            IntTuple1 tuple1 = IntTuple.of(42);
            IntTuple1 tuple2 = IntTuple.of(100);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple1_null() {
            IntTuple1 tuple = IntTuple.of(42);
            assertNotEquals(null, tuple);
        }

        @Test
        public void testEquals_tuple2_different() {
            IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple2 tuple2 = IntTuple.of(1, 3);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple3_same() {
            IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple3_different() {
            IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple2 = IntTuple.of(1, 2, 4);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple4() {
            IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
            IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple5() {
            IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testToString_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals("(42)", tuple.toString());
        }

        @Test
        public void testToString_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals("(1, 2, 3)", tuple.toString());
        }

        @Test
        public void testAccept_tuple2() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            AtomicInteger result = new AtomicInteger(0);
            tuple.accept((a, b) -> result.set(a * b));
            assertEquals(12, result.get());
        }

        @Test
        public void testFilter_tuple2_noMatch() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            Optional<IntTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        public void testAccept_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            AtomicInteger result = new AtomicInteger(0);
            tuple.accept((a, b, c) -> result.set(a + b + c));
            assertEquals(6, result.get());
        }

        @Test
        public void testFilter_tuple3_noMatch() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> result = tuple.filter((a, b, c) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        public void testTuple0_min() {
            IntTuple<?> tuple = IntTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void testTuple0_max() {
            IntTuple<?> tuple = IntTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void testTuple0_median() {
            IntTuple<?> tuple = IntTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void testTuple0_sum() {
            IntTuple<?> tuple = IntTuple.copyOf(null);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void testTuple0_average() {
            IntTuple<?> tuple = IntTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void testTuple0_reverse() {
            IntTuple<?> tuple = IntTuple.copyOf(null);
            assertNotNull(tuple.reverse());
        }

        @Test
        public void testTuple0_contains() {
            IntTuple<?> tuple = IntTuple.copyOf(null);
            assertFalse(tuple.contains(1));
        }

        @Test
        public void testTuple0_toString() {
            IntTuple<?> tuple = IntTuple.copyOf(null);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testTuple5_operations() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(5, tuple.arity());
            assertEquals(1, tuple.min());
            assertEquals(5, tuple.max());
            assertEquals(3, tuple.median());
            assertEquals(15, tuple.sum());
            assertEquals(3.0, tuple.average(), 0.001);
            assertTrue(tuple.contains(3));
            assertFalse(tuple.contains(10));
        }

        @Test
        public void testTuple5_reverse() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple5 reversed = tuple.reverse();
            assertEquals(5, reversed._1);
            assertEquals(4, reversed._2);
            assertEquals(3, reversed._3);
            assertEquals(2, reversed._4);
            assertEquals(1, reversed._5);
        }

        @Test
        public void testTuple6_operations() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(6, tuple.arity());
            assertEquals(1, tuple.min());
            assertEquals(6, tuple.max());
            assertEquals(21, tuple.sum());
        }

        @Test
        public void testTuple7_operations() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(7, tuple.arity());
            assertEquals(4, tuple.median());
            assertEquals(28, tuple.sum());
        }

        @Test
        public void testTuple8_operations() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple.max());
            assertEquals(36, tuple.sum());
        }

        @Test
        public void testTuple9_operations() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(9, tuple.arity());
            assertEquals(5, tuple.median());
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple9_reverse() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            IntTuple9 reversed = tuple.reverse();
            assertEquals(9, reversed._1);
            assertEquals(1, reversed._9);
        }

        @Test
        public void testForEach_tuple5() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(5, count.get());
        }

        @Test
        public void testForEach_tuple6() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(6, count.get());
        }

        @Test
        public void testForEach_tuple7() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(7, count.get());
        }

        @Test
        public void testForEach_tuple8() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(8, count.get());
        }

        @Test
        public void testForEach_tuple9() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(9, count.get());
        }

        @Test
        public void testContains_tuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(4));
            assertFalse(tuple.contains(5));
        }

        @Test
        public void testContains_tuple5() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertTrue(tuple.contains(5));
            assertFalse(tuple.contains(10));
        }

        @Test
        public void testContains_tuple6() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertTrue(tuple.contains(6));
            assertFalse(tuple.contains(7));
        }

        @Test
        public void testContains_tuple7() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertTrue(tuple.contains(7));
            assertFalse(tuple.contains(8));
        }

        @Test
        public void testContains_tuple8() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertTrue(tuple.contains(8));
            assertFalse(tuple.contains(9));
        }

        @Test
        public void testContains_tuple9() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertTrue(tuple.contains(9));
            assertFalse(tuple.contains(10));
        }

        @Test
        public void testHashCode_tuple4() {
            IntTuple4 tuple1 = IntTuple.of(1, 2, 3, 4);
            IntTuple4 tuple2 = IntTuple.of(1, 2, 3, 4);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCode_tuple5() {
            IntTuple5 tuple1 = IntTuple.of(1, 2, 3, 4, 5);
            IntTuple5 tuple2 = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }
    }

    @Nested
    @Tag("2511")
    class IntTuple2511Test extends TestBase {

        // ============ Factory Method Tests - IntTuple.of() ============

        @Test
        public void testOf_tuple1() {
            IntTuple1 tuple = IntTuple.of(42);
            assertNotNull(tuple);
            assertEquals(42, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf_tuple2() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            assertNotNull(tuple);
            assertEquals(10, tuple._1);
            assertEquals(20, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf_tuple4() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
            assertEquals(4, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf_tuple5() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(5, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf_tuple6() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(6, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf_tuple7() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(7, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf_tuple8() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(8, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf_tuple9() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(9, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // ============ Create from Array Tests ============

        @Test
        public void testCreate_empty() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_single() {
            IntTuple1 tuple = IntTuple.copyOf(new int[] { 100 });
            assertNotNull(tuple);
            assertEquals(100, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate_two() {
            IntTuple2 tuple = IntTuple.copyOf(new int[] { 10, 20 });
            assertNotNull(tuple);
            assertEquals(10, tuple._1);
            assertEquals(20, tuple._2);
        }

        @Test
        public void testCreate_nine() {
            IntTuple9 tuple = IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(9, tuple._9);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testCreate_tooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                IntTuple.copyOf(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
            });
        }

        @Test
        public void testMin_multiple() {
            IntTuple3 tuple = IntTuple.of(100, 5, 50);
            assertEquals(5, tuple.min());
        }

        @Test
        public void testMin_negatives() {
            IntTuple3 tuple = IntTuple.of(-10, -50, -20);
            assertEquals(-50, tuple.min());
        }

        @Test
        public void testMax_multiple() {
            IntTuple3 tuple = IntTuple.of(100, 5, 50);
            assertEquals(100, tuple.max());
        }

        @Test
        public void testMax_negatives() {
            IntTuple3 tuple = IntTuple.of(-10, -50, -20);
            assertEquals(-10, tuple.max());
        }

        @Test
        public void testMedian_odd() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(2, tuple.median());
        }

        @Test
        public void testSum_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertEquals(60, tuple.sum());
        }

        @Test
        public void testSum_negatives() {
            IntTuple3 tuple = IntTuple.of(10, -5, 15);
            assertEquals(20, tuple.sum());
        }

        @Test
        public void testAverage_single() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42.0, tuple.average());
        }

        @Test
        public void testAverage_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertEquals(20.0, tuple.average());
        }

        @Test
        public void testAverage_odd() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(2.0, tuple.average());
        }

        // ============ Reverse Tests ============

        @Test
        public void testReverse_empty() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            IntTuple0 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        public void testReverse_single() {
            IntTuple1 tuple = IntTuple.of(42);
            IntTuple1 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(42, reversed._1);
            assertNotSame(tuple, reversed); // Should be new instance
        }

        @Test
        public void testReverse_multiple() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntTuple3 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(3, reversed._1);
            assertEquals(2, reversed._2);
            assertEquals(1, reversed._3);
        }

        @Test
        public void testReverse_twoElements() {
            IntTuple2 tuple = IntTuple.of(100, 200);
            IntTuple2 reversed = tuple.reverse();
            assertEquals(200, reversed._1);
            assertEquals(100, reversed._2);
        }

        @Test
        public void testReverse_largeValues() {
            IntTuple4 tuple = IntTuple.of(Integer.MAX_VALUE, 0, -1, Integer.MIN_VALUE);
            IntTuple4 reversed = tuple.reverse();
            assertEquals(Integer.MIN_VALUE, reversed._1);
            assertEquals(-1, reversed._2);
            assertEquals(0, reversed._3);
            assertEquals(Integer.MAX_VALUE, reversed._4);
        }

        // ============ Contains Tests ============

        @Test
        public void testContains_empty() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            assertFalse(tuple.contains(42));
        }

        @Test
        public void testContains_single_notfound() {
            IntTuple1 tuple = IntTuple.of(42);
            assertFalse(tuple.contains(99));
        }

        @Test
        public void testContains_multiple_found() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertTrue(tuple.contains(10));
            assertTrue(tuple.contains(20));
            assertTrue(tuple.contains(30));
        }

        @Test
        public void testContains_multiple_notfound() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertFalse(tuple.contains(99));
        }

        @Test
        public void testContains_negatives() {
            IntTuple3 tuple = IntTuple.of(-10, 0, 10);
            assertTrue(tuple.contains(-10));
            assertTrue(tuple.contains(0));
            assertTrue(tuple.contains(10));
            assertFalse(tuple.contains(5));
        }

        // ============ Functional Methods - IntTuple2 ============

        @Test
        public void testAccept_tuple2() {
            IntTuple2 tuple = IntTuple.of(3, 4);
            List<Integer> captured = new ArrayList<>();
            tuple.accept((a, b) -> {
                captured.add(a);
                captured.add(b);
            });
            assertEquals(2, captured.size());
            assertEquals(3, captured.get(0));
            assertEquals(4, captured.get(1));
        }

        @Test
        public void testMap_tuple2_addition() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            int result = tuple.map((a, b) -> a + b);
            assertEquals(30, result);
        }

        @Test
        public void testFilter_tuple2_fails() {
            IntTuple2 tuple = IntTuple.of(1, 1);
            Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 10);
            assertFalse(result.isPresent());
        }

        // ============ Functional Methods - IntTuple3 ============

        @Test
        public void testAccept_tuple3() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> captured = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                captured.add(a);
                captured.add(b);
                captured.add(c);
            });
            assertEquals(3, captured.size());
            assertEquals(1, captured.get(0));
            assertEquals(2, captured.get(1));
            assertEquals(3, captured.get(2));
        }

        @Test
        public void testMap_tuple3_sum() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int result = tuple.map((a, b, c) -> a + b + c);
            assertEquals(60, result);
        }

        @Test
        public void testFilter_tuple3_passes() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testFilter_tuple3_fails() {
            IntTuple3 tuple = IntTuple.of(1, 1, 1);
            Optional<IntTuple3> result = tuple.filter((a, b, c) -> a + b + c > 10);
            assertFalse(result.isPresent());
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach_empty() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(i -> count.incrementAndGet());
            assertEquals(0, count.get());
        }

        @Test
        public void testForEach_tuple2() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            List<Integer> visited = new ArrayList<>();
            tuple.forEach(visited::add);
            assertEquals(2, visited.size());
            assertEquals(10, visited.get(0));
            assertEquals(20, visited.get(1));
        }

        @Test
        public void testForEach_tuple3() {
            IntTuple3 tuple = IntTuple.of(100, 200, 300);
            List<Integer> visited = new ArrayList<>();
            tuple.forEach(visited::add);
            assertEquals(3, visited.size());
            assertEquals(100, visited.get(0));
            assertEquals(200, visited.get(1));
            assertEquals(300, visited.get(2));
        }

        @Test
        public void testForEach_sum() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(sum::addAndGet);
            assertEquals(10, sum.get());
        }

        // ============ ToArray Tests ============

        @Test
        public void testToArray_empty() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            int[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        public void testToArray_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int[] array = tuple.toArray();
            assertArrayEquals(new int[] { 10, 20, 30 }, array);
        }

        @Test
        public void testToArray_independence() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int[] array1 = tuple.toArray();
            int[] array2 = tuple.toArray();
            assertNotSame(array1, array2); // Should be independent copies
            assertArrayEquals(array1, array2);
        }

        // ============ ToList Tests ============

        @Test
        public void testToList_empty() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            IntList list = tuple.toList();
            assertNotNull(list);
            assertEquals(0, list.size());
        }

        @Test
        public void testToList_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            IntList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(10, list.get(0));
            assertEquals(20, list.get(1));
            assertEquals(30, list.get(2));
        }

        // ============ Stream Tests ============

        @Test
        public void testStream_empty() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            int sum = tuple.stream().sum();
            assertEquals(0, sum);
        }

        @Test
        public void testStream_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            int sum = tuple.stream().sum();
            assertEquals(60, sum);
        }

        @Test
        public void testStream_filter() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            long count = tuple.stream().filter(i -> i > 2).count();
            assertEquals(2, count);
        }

        // ============ Equality and HashCode Tests ============

        @Test
        public void testEquals_sameInstance() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEquals_sameValues() {
            IntTuple2 tuple1 = IntTuple.of(10, 20);
            IntTuple2 tuple2 = IntTuple.of(10, 20);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_differentValues() {
            IntTuple2 tuple1 = IntTuple.of(10, 20);
            IntTuple2 tuple2 = IntTuple.of(30, 40);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_differentTypes() {
            IntTuple1 tuple1 = IntTuple.of(10);
            IntTuple2 tuple2 = IntTuple.of(10, 20);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_null() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            assertNotEquals(tuple, null);
        }

        @Test
        public void testEquals_otherObject() {
            IntTuple2 tuple = IntTuple.of(10, 20);
            assertNotEquals(tuple, "10,20");
        }

        @Test
        public void testHashCode_sameValues() {
            IntTuple2 tuple1 = IntTuple.of(10, 20);
            IntTuple2 tuple2 = IntTuple.of(10, 20);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCode_differentValues() {
            IntTuple2 tuple1 = IntTuple.of(10, 20);
            IntTuple2 tuple2 = IntTuple.of(30, 40);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCode_empty() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            assertNotNull(tuple.hashCode());
        }

        @Test
        public void testToString_multiple() {
            IntTuple3 tuple = IntTuple.of(10, 20, 30);
            assertEquals("(10, 20, 30)", tuple.toString());
        }

        @Test
        public void testArity_1() {
            IntTuple1 tuple = IntTuple.of(1);
            assertEquals(1, tuple.arity());
        }
        // ============ Field Accessors Tests ============

        @Test
        public void testFieldAccessors_tuple5() {
            IntTuple5 tuple = IntTuple.of(10, 20, 30, 40, 50);
            assertEquals(10, tuple._1);
            assertEquals(20, tuple._2);
            assertEquals(30, tuple._3);
            assertEquals(40, tuple._4);
            assertEquals(50, tuple._5);
        }

        @Test
        public void testFieldAccessors_tuple9() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
            assertEquals(4, tuple._4);
            assertEquals(5, tuple._5);
            assertEquals(6, tuple._6);
            assertEquals(7, tuple._7);
            assertEquals(8, tuple._8);
            assertEquals(9, tuple._9);
        }

        // ============ Complex Statistics Tests ============

        @Test
        public void testStatistics_allOperations() {
            IntTuple5 tuple = IntTuple.of(100, 500, 300, 200, 400);
            assertEquals(100, tuple.min());
            assertEquals(500, tuple.max());
            assertEquals(300, tuple.median());
            assertEquals(1500, tuple.sum());
            assertEquals(300.0, tuple.average());
        }

        @Test
        public void testContains_allElements() {
            IntTuple5 tuple = IntTuple.of(10, 20, 30, 40, 50);
            assertTrue(tuple.contains(10));
            assertTrue(tuple.contains(20));
            assertTrue(tuple.contains(30));
            assertTrue(tuple.contains(40));
            assertTrue(tuple.contains(50));
            assertFalse(tuple.contains(99));
        }

        // ============ Edge Cases Tests ============

        @Test
        public void testSingleElement_allOperations() {
            IntTuple1 tuple = IntTuple.of(42);
            assertEquals(42, tuple.min());
            assertEquals(42, tuple.max());
            assertEquals(42, tuple.median());
            assertEquals(42.0, tuple.average());
            assertEquals(42, tuple.sum());
            assertTrue(tuple.contains(42));
            assertFalse(tuple.contains(99));
            IntTuple1 reversed = tuple.reverse();
            assertEquals(42, reversed._1);
        }

        @Test
        public void testExtremeValues() {
            IntTuple3 tuple = IntTuple.of(Integer.MIN_VALUE, 0, Integer.MAX_VALUE);
            assertEquals(Integer.MIN_VALUE, tuple.min());
            assertEquals(Integer.MAX_VALUE, tuple.max());
            assertTrue(tuple.contains(0));
        }

        @Test
        public void testZeroes() {
            IntTuple4 tuple = IntTuple.of(0, 0, 0, 0);
            assertEquals(0, tuple.min());
            assertEquals(0, tuple.max());
            assertEquals(0, tuple.sum());
            assertEquals(0.0, tuple.average());
            assertTrue(tuple.contains(0));
        }

        @Test
        public void testMixedSignes() {
            IntTuple5 tuple = IntTuple.of(-100, -50, 0, 50, 100);
            assertEquals(-100, tuple.min());
            assertEquals(100, tuple.max());
            assertEquals(0, tuple.sum());
            assertEquals(0.0, tuple.average());
            assertTrue(tuple.contains(-100));
            assertTrue(tuple.contains(100));
        }

        @Test
        public void testLargeSum() {
            IntTuple3 tuple = IntTuple.of(1000000, 2000000, 3000000);
            assertEquals(6000000, tuple.sum());
            assertEquals(2000000.0, tuple.average());
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for IntTuple and its nested tuple classes (IntTuple0 through IntTuple9).
     * Tests cover all public methods including factory methods, statistical operations, conversions, and edge cases.
     */
    @Tag("2512")
    class IntTuple2512Test extends TestBase {

        // ===== Factory Method Tests =====

        @Test
        public void test_of_singleElement() {
            IntTuple1 tuple = IntTuple.of(42);
            assertNotNull(tuple);
            assertEquals(42, tuple._1);
        }

        @Test
        public void test_of_twoElements() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
        }

        @Test
        public void test_of_threeElements() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
        }

        @Test
        public void test_of_fourElements() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertNotNull(tuple);
            assertEquals(1, tuple._1);
            assertEquals(4, tuple._4);
        }

        @Test
        public void test_of_fiveElements() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertNotNull(tuple);
            assertEquals(5, tuple._5);
        }

        @Test
        public void test_of_sixElements() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertNotNull(tuple);
            assertEquals(6, tuple._6);
        }

        @Test
        public void test_of_sevenElements() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertNotNull(tuple);
            assertEquals(7, tuple._7);
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_of_eightElements() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertNotNull(tuple);
            assertEquals(8, tuple._8);
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_of_nineElements() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertNotNull(tuple);
            assertEquals(9, tuple._9);
        }

        @Test
        public void test_create_nullArray() {
            IntTuple0 tuple = IntTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_singleElementArray() {
            IntTuple1 tuple = IntTuple.copyOf(new int[] { 42 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(42, tuple._1);
        }

        @Test
        public void test_create_multipleElementsArray() {
            IntTuple3 tuple = IntTuple.copyOf(new int[] { 1, 2, 3 });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(1, tuple._1);
            assertEquals(2, tuple._2);
            assertEquals(3, tuple._3);
        }

        @Test
        public void test_create_tooManyElements() {
            int[] array = new int[10];
            assertThrows(IllegalArgumentException.class, () -> IntTuple.copyOf(array));
        }

        // ===== IntTuple0 Tests =====

        @Test
        public void test_IntTuple0_arity() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_IntTuple0_min_throwsException() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void test_IntTuple0_max_throwsException() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void test_IntTuple0_median_throwsException() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void test_IntTuple0_sum() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void test_IntTuple0_average_throwsException() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void test_IntTuple0_reverse() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            IntTuple0 reversed = tuple.reverse();
            assertSame(tuple, reversed);
        }

        @Test
        public void test_IntTuple0_contains() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            assertFalse(tuple.contains(1));
        }

        @Test
        public void test_IntTuple0_toString() {
            IntTuple0 tuple = IntTuple.copyOf(new int[0]);
            assertEquals("()", tuple.toString());
        }

        // ===== IntTuple1 Tests =====

        @Test
        public void test_IntTuple1_arity() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void test_IntTuple1_min() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(5, tuple.min());
        }

        @Test
        public void test_IntTuple1_max() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(5, tuple.max());
        }

        @Test
        public void test_IntTuple1_median() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(5, tuple.median());
        }

        @Test
        public void test_IntTuple1_sum() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(5, tuple.sum());
        }

        @Test
        public void test_IntTuple1_average() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(5.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_IntTuple1_reverse() {
            IntTuple1 tuple = IntTuple.of(5);
            IntTuple1 reversed = tuple.reverse();
            assertEquals(5, reversed._1);
        }

        @Test
        public void test_IntTuple1_contains_found() {
            IntTuple1 tuple = IntTuple.of(5);
            assertTrue(tuple.contains(5));
        }

        @Test
        public void test_IntTuple1_contains_notFound() {
            IntTuple1 tuple = IntTuple.of(5);
            assertFalse(tuple.contains(10));
        }

        @Test
        public void test_IntTuple1_hashCode() {
            IntTuple1 tuple1 = IntTuple.of(5);
            IntTuple1 tuple2 = IntTuple.of(5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_IntTuple1_equals_same() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals(tuple, tuple);
        }

        @Test
        public void test_IntTuple1_equals_equal() {
            IntTuple1 tuple1 = IntTuple.of(5);
            IntTuple1 tuple2 = IntTuple.of(5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void test_IntTuple1_equals_notEqual() {
            IntTuple1 tuple1 = IntTuple.of(5);
            IntTuple1 tuple2 = IntTuple.of(10);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void test_IntTuple1_equals_null() {
            IntTuple1 tuple = IntTuple.of(5);
            assertNotEquals(null, tuple);
        }

        @Test
        public void test_IntTuple1_equals_differentType() {
            IntTuple1 tuple = IntTuple.of(5);
            assertNotEquals("string", tuple);
        }

        @Test
        public void test_IntTuple1_toString() {
            IntTuple1 tuple = IntTuple.of(5);
            assertEquals("(5)", tuple.toString());
        }

        // ===== IntTuple2 Tests =====

        @Test
        public void test_IntTuple2_min() {
            IntTuple2 tuple = IntTuple.of(3, 1);
            assertEquals(1, tuple.min());
        }

        @Test
        public void test_IntTuple2_max() {
            IntTuple2 tuple = IntTuple.of(3, 1);
            assertEquals(3, tuple.max());
        }

        @Test
        public void test_IntTuple2_median() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            int median = tuple.median();
            assertTrue(median == 1 || median == 2);
        }

        @Test
        public void test_IntTuple2_sum() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertEquals(3, tuple.sum());
        }

        @Test
        public void test_IntTuple2_average() {
            IntTuple2 tuple = IntTuple.of(1, 3);
            assertEquals(2.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_IntTuple2_reverse() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            IntTuple2 reversed = tuple.reverse();
            assertEquals(2, reversed._1);
            assertEquals(1, reversed._2);
        }

        @Test
        public void test_IntTuple2_contains_found() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertTrue(tuple.contains(1));
            assertTrue(tuple.contains(2));
        }

        @Test
        public void test_IntTuple2_contains_notFound() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertFalse(tuple.contains(3));
        }

        @Test
        public void test_IntTuple2_forEach() throws Exception {
            IntTuple2 tuple = IntTuple.of(1, 2);
            List<Integer> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals(1, values.get(0).intValue());
            assertEquals(2, values.get(1).intValue());
        }

        @Test
        public void test_IntTuple2_accept() throws Exception {
            IntTuple2 tuple = IntTuple.of(3, 4);
            final int[] result = new int[1];
            tuple.accept((a, b) -> result[0] = a + b);
            assertEquals(7, result[0]);
        }

        @Test
        public void test_IntTuple2_map() throws Exception {
            IntTuple2 tuple = IntTuple.of(3, 4);
            Integer result = tuple.map((a, b) -> a * b);
            assertEquals(12, result.intValue());
        }

        @Test
        public void test_IntTuple2_filter_satisfied() throws Exception {
            IntTuple2 tuple = IntTuple.of(3, 4);
            Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void test_IntTuple2_filter_notSatisfied() throws Exception {
            IntTuple2 tuple = IntTuple.of(1, 1);
            Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_IntTuple2_hashCode() {
            IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple2 tuple2 = IntTuple.of(1, 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_IntTuple2_equals() {
            IntTuple2 tuple1 = IntTuple.of(1, 2);
            IntTuple2 tuple2 = IntTuple.of(1, 2);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void test_IntTuple2_toString() {
            IntTuple2 tuple = IntTuple.of(1, 2);
            assertEquals("(1, 2)", tuple.toString());
        }

        // ===== IntTuple3 Tests =====

        @Test
        public void test_IntTuple3_min() {
            IntTuple3 tuple = IntTuple.of(3, 1, 2);
            assertEquals(1, tuple.min());
        }

        @Test
        public void test_IntTuple3_max() {
            IntTuple3 tuple = IntTuple.of(3, 1, 2);
            assertEquals(3, tuple.max());
        }

        @Test
        public void test_IntTuple3_median() {
            IntTuple3 tuple = IntTuple.of(30, 10, 20);
            assertEquals(20, tuple.median());
        }

        @Test
        public void test_IntTuple3_sum() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(6, tuple.sum());
        }

        @Test
        public void test_IntTuple3_average() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(2.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_IntTuple3_reverse() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntTuple3 reversed = tuple.reverse();
            assertEquals(3, reversed._1);
            assertEquals(2, reversed._2);
            assertEquals(1, reversed._3);
        }

        @Test
        public void test_IntTuple3_contains() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.contains(2));
            assertFalse(tuple.contains(5));
        }

        @Test
        public void test_IntTuple3_forEach() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            List<Integer> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        public void test_IntTuple3_accept() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            final int[] result = new int[1];
            tuple.accept((a, b, c) -> result[0] = a + b + c);
            assertEquals(6, result[0]);
        }

        @Test
        public void test_IntTuple3_map() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Integer result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6, result.intValue());
        }

        @Test
        public void test_IntTuple3_filter() throws Exception {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            Optional<IntTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
        }

        // ===== IntTuple4+ Tests =====

        @Test
        public void test_IntTuple4_arity() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void test_IntTuple4_statisticalOperations() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(1, tuple.min());
            assertEquals(4, tuple.max());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average(), 0.0001);
        }

        @Test
        public void test_IntTuple5_arity() {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void test_IntTuple6_arity() {
            IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void test_IntTuple7_arity() {
            IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(7, tuple.arity());
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_IntTuple8_arity() {
            IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(8, tuple.arity());
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_IntTuple9_arity() {
            IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(9, tuple.arity());
        }

        // ===== Common Method Tests =====

        @Test
        public void test_toArray() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals(1, array[0]);
            assertEquals(2, array[1]);
            assertEquals(3, array[2]);
        }

        @Test
        public void test_toArray_modification() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int[] array = tuple.toArray();
            array[0] = 99;
            assertEquals(1, tuple._1);
        }

        @Test
        public void test_toList() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            IntList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1, list.get(0));
        }

        @Test
        public void test_forEach_multipleElements() throws Exception {
            IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
            List<Integer> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        public void test_hashCode_consistency() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        public void test_equals_symmetric() {
            IntTuple3 tuple1 = IntTuple.of(1, 2, 3);
            IntTuple3 tuple2 = IntTuple.of(1, 2, 3);
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }

        @Test
        public void test_equals_differentClass() {
            IntTuple2 tuple2 = IntTuple.of(1, 2);
            IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
            assertNotEquals(tuple2, tuple3);
        }

        @Test
        public void test_toString_format() {
            IntTuple3 tuple = IntTuple.of(1, 2, 3);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("2"));
            assertTrue(str.contains("3"));
        }

        // ===== Edge Cases =====

        @Test
        public void test_negativeValues() {
            IntTuple3 tuple = IntTuple.of(-1, -2, -3);
            assertEquals(-3, tuple.min());
            assertEquals(-1, tuple.max());
            assertEquals(-6, tuple.sum());
        }

        @Test
        public void test_zeroValues() {
            IntTuple3 tuple = IntTuple.of(0, 0, 0);
            assertEquals(0, tuple.min());
            assertEquals(0, tuple.max());
            assertEquals(0, tuple.sum());
        }

        @Test
        public void test_largeValues() {
            IntTuple2 tuple = IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE / 2);
            assertEquals(Integer.MAX_VALUE / 2, tuple.min());
            assertEquals(Integer.MAX_VALUE, tuple.max());
        }

        @Test
        public void test_mixedValues() {
            IntTuple3 tuple = IntTuple.of(-100, 0, 100);
            assertEquals(-100, tuple.min());
            assertEquals(100, tuple.max());
            assertEquals(0, tuple.sum());
        }

        @Test
        public void test_reverse_largerTuples() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            IntTuple4 reversed = tuple.reverse();
            assertEquals(4, reversed._1);
            assertEquals(3, reversed._2);
            assertEquals(2, reversed._3);
            assertEquals(1, reversed._4);
        }

        @Test
        public void test_median_evenSize() {
            IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            int median = tuple.median();
            assertTrue(median >= 1 && median <= 4);
        }

        @Test
        public void test_median_oddSize() {
            IntTuple5 tuple = IntTuple.of(50, 10, 30, 20, 40);
            int median = tuple.median();
            assertEquals(30, median);
        }
    }

    @Nested
    @SuppressWarnings("deprecation")
    @Tag("2512")
    class JavadocExampleTupleTest_IntTuple extends TestBase {
        // ===== IntTuple Javadoc examples =====

        @Test
        public void testIntTupleOf1() {
            // IntTuple.IntTuple1 single = IntTuple.of(42);
            // int value = single._1;  // 42
            IntTuple.IntTuple1 single = IntTuple.of(42);
            assertEquals(42, single._1);
        }

        @Test
        public void testIntTupleOf2() {
            // IntTuple.IntTuple2 pair = IntTuple.of(1, 2);
            // int sum = pair._1 + pair._2;  // 3
            IntTuple.IntTuple2 pair = IntTuple.of(1, 2);
            assertEquals(3, pair._1 + pair._2);
        }

        @Test
        public void testIntTupleOf3Average() {
            // IntTuple.IntTuple3 triple = IntTuple.of(1, 2, 3);
            // double average = triple.average();   // 2.0
            IntTuple.IntTuple3 triple = IntTuple.of(1, 2, 3);
            assertEquals(2.0, triple.average(), 0.001);
        }

        @Test
        public void testIntTupleOf4Fields() {
            // IntTuple.IntTuple4 quad = IntTuple.of(1, 2, 3, 4);
            // quad._1 == 1, quad._2 == 2, quad._3 == 3, quad._4 == 4
            IntTuple.IntTuple4 quad = IntTuple.of(1, 2, 3, 4);
            assertEquals(1, quad._1);
            assertEquals(2, quad._2);
            assertEquals(3, quad._3);
            assertEquals(4, quad._4);
        }

        @Test
        public void testIntTupleOf5Field() {
            // IntTuple.IntTuple5 quint = IntTuple.of(1, 2, 3, 4, 5);
            // quint._5 == 5
            IntTuple.IntTuple5 quint = IntTuple.of(1, 2, 3, 4, 5);
            assertEquals(5, quint._5);
        }

        @Test
        public void testIntTupleOf6Field() {
            // IntTuple.IntTuple6 sext = IntTuple.of(1, 2, 3, 4, 5, 6);
            // sext._6 == 6
            IntTuple.IntTuple6 sext = IntTuple.of(1, 2, 3, 4, 5, 6);
            assertEquals(6, sext._6);
        }

        @Test
        public void testIntTupleOf7Field() {
            // IntTuple.IntTuple7 sept = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            // sept._7 == 7
            IntTuple.IntTuple7 sept = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
            assertEquals(7, sept._7);
        }

        @Test
        public void testIntTupleOf8Field() {
            // IntTuple.IntTuple8 oct = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            // oct._8 == 8
            IntTuple.IntTuple8 oct = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
            assertEquals(8, oct._8);
        }

        @Test
        public void testIntTupleOf9Field() {
            // IntTuple.IntTuple9 non = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            // non._9 == 9
            IntTuple.IntTuple9 non = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertEquals(9, non._9);
        }

        @Test
        public void testIntTupleClassLevelExamples() {
            // int min = triple.min();         // 1
            // int max = triple.max();         // 3
            // double avg = triple.average();  // 2.0
            IntTuple.IntTuple3 triple = IntTuple.of(1, 2, 3);
            assertEquals(1, triple.min());
            assertEquals(3, triple.max());
            assertEquals(2.0, triple.average(), 0.001);
        }

        @Test
        public void testIntTupleMin() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
            // int min = tuple.min();   // 1
            IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
            assertEquals(1, tuple.min());

            // IntTuple.IntTuple1 single = IntTuple.of(42);
            // int singleMin = single.min();   // 42
            IntTuple.IntTuple1 single = IntTuple.of(42);
            assertEquals(42, single.min());
        }

        @Test
        public void testIntTupleMax() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
            // int max = tuple.max();   // 3
            IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
            assertEquals(3, tuple.max());

            // IntTuple.IntTuple1 single = IntTuple.of(42);
            // int singleMax = single.max();   // 42
            IntTuple.IntTuple1 single = IntTuple.of(42);
            assertEquals(42, single.max());
        }

        @Test
        public void testIntTupleMedian() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 3, 2);
            // int median = tuple.median();   // 2
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 3, 2);
            assertEquals(2, tuple.median());

            // IntTuple.IntTuple4 evenTuple = IntTuple.of(1, 2, 3, 4);
            // int evenMedian = evenTuple.median();   // 2
            IntTuple.IntTuple4 evenTuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(2, evenTuple.median());
        }

        @Test
        public void testIntTupleSum() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            // int sum = tuple.sum();   // 6
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(6, tuple.sum());

            // IntTuple.IntTuple2 pair = IntTuple.of(100, 200);
            // int total = pair.sum();  // 300
            IntTuple.IntTuple2 pair = IntTuple.of(100, 200);
            assertEquals(300, pair.sum());
        }

        @Test
        public void testIntTupleAverage() {
            // IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            // double avg = tuple.average();   // 2.5
            IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
            assertEquals(2.5, tuple.average(), 0.001);
        }

        @Test
        public void testIntTupleContains() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            // boolean has2 = tuple.contains(2);   // true
            // boolean has5 = tuple.contains(5);   // false
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertTrue(tuple.contains(2));
            assertFalse(tuple.contains(5));

            // IntTuple.IntTuple2 pair = IntTuple.of(10, 10);
            // boolean hasTen = pair.contains(10);   // true
            // boolean hasOne = pair.contains(1);    // false
            IntTuple.IntTuple2 pair = IntTuple.of(10, 10);
            assertTrue(pair.contains(10));
            assertFalse(pair.contains(1));
        }

        @Test
        public void testIntTupleToArray() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            // int[] array = tuple.toArray();   // [1, 2, 3]
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertArrayEquals(new int[] { 1, 2, 3 }, tuple.toArray());

            // IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
            // int[] pairArray = pair.toArray();   // [10, 20]
            IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
            assertArrayEquals(new int[] { 10, 20 }, pair.toArray());
        }

        @Test
        public void testIntTupleReverse() {
            // IntTuple.IntTuple2 pair = IntTuple.of(1, 2);
            // IntTuple.IntTuple2 reversedPair = pair.reverse();   // (2, 1)
            IntTuple.IntTuple2 pair = IntTuple.of(1, 2);
            IntTuple.IntTuple2 reversed2 = pair.reverse();
            assertEquals(2, reversed2._1);
            assertEquals(1, reversed2._2);

            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            // IntTuple.IntTuple3 reversed = tuple.reverse();   // (3, 2, 1)
            IntTuple.IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
            IntTuple.IntTuple3 reversed3 = tuple3.reverse();
            assertEquals(3, reversed3._1);
            assertEquals(2, reversed3._2);
            assertEquals(1, reversed3._3);
        }

        @Test
        public void testIntTupleStream() {
            // IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            // int sum = tuple.stream().sum();   // 6
            IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
            assertEquals(6, tuple.stream().sum());
        }
    }

}
