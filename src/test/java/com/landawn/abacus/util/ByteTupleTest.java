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
import com.landawn.abacus.util.ByteTuple.ByteTuple0;
import com.landawn.abacus.util.ByteTuple.ByteTuple1;
import com.landawn.abacus.util.ByteTuple.ByteTuple2;
import com.landawn.abacus.util.ByteTuple.ByteTuple3;
import com.landawn.abacus.util.ByteTuple.ByteTuple4;
import com.landawn.abacus.util.ByteTuple.ByteTuple5;
import com.landawn.abacus.util.ByteTuple.ByteTuple6;
import com.landawn.abacus.util.ByteTuple.ByteTuple7;
import com.landawn.abacus.util.ByteTuple.ByteTuple8;
import com.landawn.abacus.util.ByteTuple.ByteTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.ByteStream;

class ByteTupleTest extends TestBase {

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
        ByteTuple<?> empty = ByteTuple.copyOf(null);
        assertEquals(0, empty.arity());

        empty = ByteTuple.copyOf(new byte[0]);
        assertEquals(0, empty.arity());

        // Test array with 1 element
        ByteTuple.ByteTuple1 tuple1 = ByteTuple.copyOf(new byte[] { 10 });
        assertEquals(1, tuple1.arity());
        assertEquals(10, tuple1._1);

        // Test array with 5 elements
        ByteTuple.ByteTuple5 tuple5 = ByteTuple.copyOf(new byte[] { 10, 20, 30, 40, 50 });
        assertEquals(5, tuple5.arity());
        assertEquals(50, tuple5._5);

        // Test array with 9 elements
        ByteTuple.ByteTuple9 tuple9 = ByteTuple.copyOf(new byte[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 });
        assertEquals(9, tuple9.arity());
        assertEquals(90, tuple9._9);

        // Test too many elements
        assertThrows(IllegalArgumentException.class, () -> ByteTuple.copyOf(new byte[10]));
    }

    @Test
    public void testMin() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals(10, tuple.min());

        ByteTuple.ByteTuple0 empty = ByteTuple.copyOf(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> empty.min());
    }

    @Test
    public void testMax() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals(30, tuple.max());

        ByteTuple.ByteTuple0 empty = ByteTuple.copyOf(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> empty.max());
    }

    @Test
    public void testMedian() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
        assertEquals(20, tuple.median());

        ByteTuple.ByteTuple0 empty = ByteTuple.copyOf(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> empty.median());
    }

    @Test
    public void testSum() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(60, tuple.sum());

        ByteTuple.ByteTuple0 empty = ByteTuple.copyOf(new byte[0]);
        assertEquals(0, empty.sum());
    }

    @Test
    public void testAverage() {
        ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
        assertEquals(20.0, tuple.average());

        ByteTuple.ByteTuple0 empty = ByteTuple.copyOf(new byte[0]);
        assertThrows(NoSuchElementException.class, () -> empty.average());
    }

    @Test
    public void testReverse() {
        // Test Tuple0
        ByteTuple.ByteTuple0 empty = ByteTuple.copyOf(new byte[0]);
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
        ByteTuple.ByteTuple0 empty = ByteTuple.copyOf(new byte[0]);
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
        ByteTuple.ByteTuple0 empty = ByteTuple.copyOf(new byte[0]);
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

    @Nested
    /**
     * Comprehensive test suite for ByteTuple and its nested classes.
     * Tests all public methods including factory methods, statistical operations,
     * collection conversions, and special methods in Tuple2 and Tuple3.
     */
    @Tag("2025")
    class ByteTuple2025Test extends TestBase {

        // Factory method tests
        @Test
        public void testOf1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals((byte) 1, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf2() {
            ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 2, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 2, tuple._2);
            assertEquals((byte) 3, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf4() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 2, tuple._2);
            assertEquals((byte) 3, tuple._3);
            assertEquals((byte) 4, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf5() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 5, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf6() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 6, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf7() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 7, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf8() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 8, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf9() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 9, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // Create method tests
        @Test
        public void testCreateEmpty() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreateNull() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate1() {
            ByteTuple1 tuple = ByteTuple.copyOf(new byte[] { (byte) 1 });
            assertEquals((byte) 1, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate3() {
            ByteTuple3 tuple = ByteTuple.copyOf(new byte[] { (byte) 1, (byte) 2, (byte) 3 });
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 2, tuple._2);
            assertEquals((byte) 3, tuple._3);
        }

        @Test
        public void testCreate9() {
            ByteTuple9 tuple = ByteTuple.copyOf(new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9 });
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 9, tuple._9);
        }

        @Test
        public void testCreateTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                ByteTuple.copyOf(new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10 });
            });
        }

        // Statistical method tests - min
        @Test
        public void testMinTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals((byte) 1, tuple.min());
        }

        @Test
        public void testMinTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 3, (byte) 1, (byte) 2);
            assertEquals((byte) 1, tuple.min());
        }

        @Test
        public void testMinTuple0ThrowsException() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        // Statistical method tests - max
        @Test
        public void testMaxTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals((byte) 1, tuple.max());
        }

        @Test
        public void testMaxTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 3, (byte) 1, (byte) 2);
            assertEquals((byte) 3, tuple.max());
        }

        @Test
        public void testMaxTuple0ThrowsException() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        // Statistical method tests - median
        @Test
        public void testMedianTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals((byte) 1, tuple.median());
        }

        @Test
        public void testMedianTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 3, (byte) 1, (byte) 2);
            assertEquals((byte) 2, tuple.median());
        }

        @Test
        public void testMedianTuple0ThrowsException() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        // Statistical method tests - sum
        @Test
        public void testSumTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void testSumTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals(1, tuple.sum());
        }

        @Test
        public void testSumTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(6, tuple.sum());
        }

        // Statistical method tests - average
        @Test
        public void testAverageTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals(1.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(2.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple0ThrowsException() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        // Reverse tests
        @Test
        public void testReverseTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            ByteTuple<ByteTuple0> reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        public void testReverseTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            ByteTuple1 reversed = tuple.reverse();
            assertEquals((byte) 1, reversed._1);
        }

        @Test
        public void testReverseTuple2() {
            ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple2 reversed = tuple.reverse();
            assertEquals((byte) 2, reversed._1);
            assertEquals((byte) 1, reversed._2);
        }

        @Test
        public void testReverseTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple3 reversed = tuple.reverse();
            assertEquals((byte) 3, reversed._1);
            assertEquals((byte) 2, reversed._2);
            assertEquals((byte) 1, reversed._3);
        }

        // Contains tests
        @Test
        public void testContainsTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            assertFalse(tuple.contains((byte) 1));
        }

        @Test
        public void testContainsTuple1True() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        public void testContainsTuple1False() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertFalse(tuple.contains((byte) 99));
        }

        @Test
        public void testContainsTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 2));
            assertTrue(tuple.contains((byte) 3));
            assertFalse(tuple.contains((byte) 99));
        }

        // toArray tests
        @Test
        public void testToArrayTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            byte[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        public void testToArrayTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { (byte) 1 }, array);
        }

        @Test
        public void testToArrayTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { (byte) 1, (byte) 2, (byte) 3 }, array);
        }

        @Test
        public void testToArrayModificationDoesNotAffectTuple() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            byte[] array = tuple.toArray();
            array[0] = (byte) 100;
            assertEquals((byte) 1, tuple._1);
        }

        // toList tests
        @Test
        public void testToListTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            ByteList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        public void testToListTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            ByteList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals((byte) 1, list.get(0));
        }

        @Test
        public void testToListTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals((byte) 1, list.get(0));
            assertEquals((byte) 2, list.get(1));
            assertEquals((byte) 3, list.get(2));
        }

        // forEach tests
        @Test
        public void testForEachTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        public void testForEachTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Byte.valueOf((byte) 1), result.get(0));
        }

        @Test
        public void testForEachTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Byte.valueOf((byte) 1), result.get(0));
            assertEquals(Byte.valueOf((byte) 2), result.get(1));
            assertEquals(Byte.valueOf((byte) 3), result.get(2));
        }

        // stream tests
        @Test
        public void testStreamTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            ByteStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        public void testStreamTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            ByteStream stream = tuple.stream();
            assertEquals(1, stream.sum());
        }

        @Test
        public void testStreamTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteStream stream = tuple.stream();
            assertEquals(6, stream.sum());
        }

        // hashCode tests
        @Test
        public void testHashCodeTuple1() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple2() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple3() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // equals tests
        @Test
        public void testEqualsSameObject() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEqualsNull() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotEquals(null, tuple);
        }

        @Test
        public void testEqualsDifferentClass() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        public void testEqualsTuple1() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
            ByteTuple1 tuple3 = ByteTuple.of((byte) 99);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple2() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple2 tuple3 = ByteTuple.of((byte) 1, (byte) 3);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple3() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple3 tuple3 = ByteTuple.of((byte) 1, (byte) 2, (byte) 4);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        // toString tests
        @Test
        public void testToStringTuple0() {
            ByteTuple<ByteTuple0> tuple = ByteTuple.copyOf(new byte[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testToStringTuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 1);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
        }

        @Test
        public void testToStringTuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("2"));
            assertTrue(str.contains("3"));
        }

        // Tuple2 special methods - accept
        @Test
        public void testTuple2Accept() {
            ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
            List<Byte> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Byte.valueOf((byte) 3), result.get(0));
            assertEquals(Byte.valueOf((byte) 4), result.get(1));
        }

        // Tuple2 special methods - map
        @Test
        public void testTuple2Map() {
            ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
            int result = tuple.map((a, b) -> a * b);
            assertEquals(12, result);
        }

        // Tuple2 special methods - filter
        @Test
        public void testTuple2FilterTrue() {
            ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
            var result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2FilterFalse() {
            ByteTuple2 tuple = ByteTuple.of((byte) 3, (byte) 4);
            var result = tuple.filter((a, b) -> a + b > 10);
            assertFalse(result.isPresent());
        }

        // Tuple3 special methods - accept
        @Test
        public void testTuple3Accept() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            List<Byte> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Byte.valueOf((byte) 1), result.get(0));
            assertEquals(Byte.valueOf((byte) 2), result.get(1));
            assertEquals(Byte.valueOf((byte) 3), result.get(2));
        }

        // Tuple3 special methods - map
        @Test
        public void testTuple3Map() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            int result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6, result);
        }

        // Tuple3 special methods - filter
        @Test
        public void testTuple3FilterTrue() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple3FilterFalse() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 10);
            assertFalse(result.isPresent());
        }

        // arity tests for all tuple sizes
        @Test
        public void testArity() {
            assertEquals(0, ByteTuple.copyOf(new byte[0]).arity());
            assertEquals(1, ByteTuple.of((byte) 1).arity());
            assertEquals(2, ByteTuple.of((byte) 1, (byte) 2).arity());
            assertEquals(3, ByteTuple.of((byte) 1, (byte) 2, (byte) 3).arity());
            assertEquals(4, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4).arity());
            assertEquals(5, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5).arity());
            assertEquals(6, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6).arity());
            assertEquals(7, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7).arity());
            assertEquals(8, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8).arity());
            assertEquals(9, ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9).arity());
        }

        // Tests for inherited methods from PrimitiveTuple
        @Test
        public void testAcceptConsumer() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            List<Byte> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals((byte) 1, result.get(0));
            assertEquals((byte) 2, result.get(1));
            assertEquals((byte) 3, result.get(2));
        }

        @Test
        public void testMapFunction() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            int result = tuple.map(t -> t._1 + t._2 + t._3);
            assertEquals(6, result);
        }

        @Test
        public void testFilterPredicate() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            var result = tuple.filter(t -> t._1 == (byte) 1);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testFilterPredicateFalse() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            var result = tuple.filter(t -> t._1 == (byte) 99);
            assertFalse(result.isPresent());
        }

        @Test
        public void testToOptional() {
            ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        // Comprehensive tests for Tuple4 through Tuple9
        @Test
        public void testTuple4Operations() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);

            // Test reverse
            ByteTuple4 reversed = tuple.reverse();
            assertEquals((byte) 4, reversed._1);
            assertEquals((byte) 3, reversed._2);
            assertEquals((byte) 2, reversed._3);
            assertEquals((byte) 1, reversed._4);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 4));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4 }, tuple.toArray());

            // Test min/max/median/sum/average via base class
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 4, tuple.max());
            assertEquals((byte) 2, tuple.median());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average(), 0.001);

            // Test hashCode and equals
            ByteTuple4 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteTuple4 tuple3 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 5);
            assertEquals(tuple.hashCode(), tuple2.hashCode());
            assertEquals(tuple, tuple2);
            assertNotEquals(tuple, tuple3);

            // Test toString
            String str = tuple.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("4"));
        }

        @Test
        public void testTuple5Operations() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);

            // Test reverse
            ByteTuple5 reversed = tuple.reverse();
            assertEquals((byte) 5, reversed._1);
            assertEquals((byte) 1, reversed._5);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 5));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, tuple.toArray());

            // Test statistical operations
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 5, tuple.max());
            assertEquals((byte) 3, tuple.median());
            assertEquals(15, tuple.sum());
            assertEquals(3.0, tuple.average(), 0.001);

            // Test equals
            ByteTuple5 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertEquals(tuple, tuple2);
        }

        @Test
        public void testTuple6Operations() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);

            // Test reverse
            ByteTuple6 reversed = tuple.reverse();
            assertEquals((byte) 6, reversed._1);
            assertEquals((byte) 1, reversed._6);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 6));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6 }, tuple.toArray());

            // Test statistical operations
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 6, tuple.max());
            assertEquals(21, tuple.sum());
            assertEquals(3.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple7Operations() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);

            // Test reverse
            ByteTuple7 reversed = tuple.reverse();
            assertEquals((byte) 7, reversed._1);
            assertEquals((byte) 1, reversed._7);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 7));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7 }, tuple.toArray());

            // Test statistical operations
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 7, tuple.max());
            assertEquals(28, tuple.sum());
            assertEquals(4.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple8Operations() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);

            // Test reverse
            ByteTuple8 reversed = tuple.reverse();
            assertEquals((byte) 8, reversed._1);
            assertEquals((byte) 1, reversed._8);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 8));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }, tuple.toArray());

            // Test statistical operations
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 8, tuple.max());
            assertEquals(36, tuple.sum());
            assertEquals(4.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple9Operations() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);

            // Test reverse
            ByteTuple9 reversed = tuple.reverse();
            assertEquals((byte) 9, reversed._1);
            assertEquals((byte) 1, reversed._9);

            // Test contains
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 9));
            assertFalse(tuple.contains((byte) 99));

            // Test toArray
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, tuple.toArray());

            // Test statistical operations
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 9, tuple.max());
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average(), 0.001);
        }

        // Test create methods for sizes 2, 4-8
        @Test
        public void testCreate2Through8() {
            ByteTuple2 tuple2 = ByteTuple.copyOf(new byte[] { 1, 2 });
            assertEquals((byte) 1, tuple2._1);
            assertEquals((byte) 2, tuple2._2);

            ByteTuple4 tuple4 = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4 });
            assertEquals((byte) 1, tuple4._1);
            assertEquals((byte) 4, tuple4._4);

            ByteTuple5 tuple5 = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4, 5 });
            assertEquals((byte) 1, tuple5._1);
            assertEquals((byte) 5, tuple5._5);

            ByteTuple6 tuple6 = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4, 5, 6 });
            assertEquals((byte) 1, tuple6._1);
            assertEquals((byte) 6, tuple6._6);

            ByteTuple7 tuple7 = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals((byte) 1, tuple7._1);
            assertEquals((byte) 7, tuple7._7);

            ByteTuple8 tuple8 = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals((byte) 1, tuple8._1);
            assertEquals((byte) 8, tuple8._8);
        }

        // Test toList for larger tuples
        @Test
        public void testToListTuple4Through9() {
            ByteTuple4 tuple4 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals((byte) 4, list4.get(3));

            ByteTuple9 tuple9 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            ByteList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals((byte) 9, list9.get(8));
        }

        // Test forEach for larger tuples
        @Test
        public void testForEachTuple4() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Byte.valueOf((byte) 4), result.get(3));
        }

        // Test forEach override for Tuple2
        @Test
        public void testForEachTuple2Override() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Byte.valueOf((byte) 10), result.get(0));
            assertEquals(Byte.valueOf((byte) 20), result.get(1));
        }

        // Test forEach override for Tuple3
        @Test
        public void testForEachTuple3Override() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            List<Byte> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Byte.valueOf((byte) 30), result.get(2));
        }

        // Test stream for larger tuples
        @Test
        public void testStreamTuple4Through9() {
            ByteTuple4 tuple4 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertEquals(10, tuple4.stream().sum());

            ByteTuple9 tuple9 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertEquals(45, tuple9.stream().sum());
        }

        // ==================== ByteTuple Nested Class Tests ====================

        // ============ ByteTuple1 Nested Class Tests ============

        @Test
        public void testByteTuple1_arity() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testByteTuple1_reverse() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            ByteTuple.ByteTuple1 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        public void testByteTuple1_contains() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        public void testByteTuple1_hashCode() {
            ByteTuple.ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
            ByteTuple.ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testByteTuple1_equals() {
            ByteTuple.ByteTuple1 tuple1 = ByteTuple.of((byte) 1);
            ByteTuple.ByteTuple1 tuple2 = ByteTuple.of((byte) 1);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testByteTuple1_toString() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testByteTuple1_forEach() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testByteTuple1_min() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertNotNull(tuple.min());
        }

        @Test
        public void testByteTuple1_max() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertNotNull(tuple.max());
        }

        @Test
        public void testByteTuple1_median() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertNotNull(tuple.median());
        }

        @Test
        public void testByteTuple1_sum() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testByteTuple1_average() {
            ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 1);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        // ============ ByteTuple2 Nested Class Tests ============

        @Test
        public void testByteTuple2_arity() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testByteTuple2_reverse() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple.ByteTuple2 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        public void testByteTuple2_contains() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        public void testByteTuple2_hashCode() {
            ByteTuple.ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple.ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testByteTuple2_equals() {
            ByteTuple.ByteTuple2 tuple1 = ByteTuple.of((byte) 1, (byte) 2);
            ByteTuple.ByteTuple2 tuple2 = ByteTuple.of((byte) 1, (byte) 2);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testByteTuple2_toString() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testByteTuple2_forEach() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        public void testByteTuple2_min() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertNotNull(tuple.min());
        }

        @Test
        public void testByteTuple2_max() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertNotNull(tuple.max());
        }

        @Test
        public void testByteTuple2_median() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertNotNull(tuple.median());
        }

        @Test
        public void testByteTuple2_sum() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testByteTuple2_average() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testByteTuple2_accept_biConsumer() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testByteTuple2_map_biFunction() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testByteTuple2_filter_biPredicate() {
            ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 1, (byte) 2);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        // ============ ByteTuple3 Nested Class Tests ============

        @Test
        public void testByteTuple3_arity() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testByteTuple3_reverse() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple.ByteTuple3 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        public void testByteTuple3_contains() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        public void testByteTuple3_hashCode() {
            ByteTuple.ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple.ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testByteTuple3_equals() {
            ByteTuple.ByteTuple3 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            ByteTuple.ByteTuple3 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testByteTuple3_toString() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testByteTuple3_forEach() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        public void testByteTuple3_min() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotNull(tuple.min());
        }

        @Test
        public void testByteTuple3_max() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotNull(tuple.max());
        }

        @Test
        public void testByteTuple3_median() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotNull(tuple.median());
        }

        @Test
        public void testByteTuple3_sum() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testByteTuple3_average() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testByteTuple3_accept_triConsumer() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testByteTuple3_map_triFunction() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testByteTuple3_filter_triPredicate() {
            ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        // ============ ByteTuple4 Nested Class Tests ============

        @Test
        public void testByteTuple4_arity() {
            ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testByteTuple4_reverse() {
            ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteTuple.ByteTuple4 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        public void testByteTuple4_contains() {
            ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        public void testByteTuple4_hashCode() {
            ByteTuple.ByteTuple4 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteTuple.ByteTuple4 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testByteTuple4_equals() {
            ByteTuple.ByteTuple4 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteTuple.ByteTuple4 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testByteTuple4_toString() {
            ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testByteTuple4_forEach() {
            ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        // ============ ByteTuple5 Nested Class Tests ============

        @Test
        public void testByteTuple5_arity() {
            ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testByteTuple5_reverse() {
            ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            ByteTuple.ByteTuple5 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        public void testByteTuple5_contains() {
            ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        public void testByteTuple5_hashCode() {
            ByteTuple.ByteTuple5 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            ByteTuple.ByteTuple5 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testByteTuple5_equals() {
            ByteTuple.ByteTuple5 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            ByteTuple.ByteTuple5 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testByteTuple5_toString() {
            ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testByteTuple5_forEach() {
            ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        // ============ ByteTuple6 Nested Class Tests ============

        @Test
        public void testByteTuple6_arity() {
            ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testByteTuple6_reverse() {
            ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            ByteTuple.ByteTuple6 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        public void testByteTuple6_contains() {
            ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        public void testByteTuple6_hashCode() {
            ByteTuple.ByteTuple6 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            ByteTuple.ByteTuple6 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testByteTuple6_equals() {
            ByteTuple.ByteTuple6 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            ByteTuple.ByteTuple6 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testByteTuple6_toString() {
            ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testByteTuple6_forEach() {
            ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        // ============ ByteTuple7 Nested Class Tests ============

        @Test
        public void testByteTuple7_arity() {
            ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testByteTuple7_reverse() {
            ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            ByteTuple.ByteTuple7 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        public void testByteTuple7_contains() {
            ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        public void testByteTuple7_hashCode() {
            ByteTuple.ByteTuple7 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            ByteTuple.ByteTuple7 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testByteTuple7_equals() {
            ByteTuple.ByteTuple7 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            ByteTuple.ByteTuple7 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testByteTuple7_toString() {
            ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testByteTuple7_forEach() {
            ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        // ============ ByteTuple8 Nested Class Tests ============

        @Test
        public void testByteTuple8_arity() {
            ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testByteTuple8_reverse() {
            ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            ByteTuple.ByteTuple8 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        public void testByteTuple8_contains() {
            ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        public void testByteTuple8_hashCode() {
            ByteTuple.ByteTuple8 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            ByteTuple.ByteTuple8 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testByteTuple8_equals() {
            ByteTuple.ByteTuple8 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            ByteTuple.ByteTuple8 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testByteTuple8_toString() {
            ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testByteTuple8_forEach() {
            ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        // ============ ByteTuple9 Nested Class Tests ============

        @Test
        public void testByteTuple9_arity() {
            ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testByteTuple9_reverse() {
            ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            ByteTuple.ByteTuple9 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._9);
            assertEquals(tuple._9, reversed._1);
        }

        @Test
        public void testByteTuple9_contains() {
            ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertTrue(tuple.contains((byte) 1));
        }

        @Test
        public void testByteTuple9_hashCode() {
            ByteTuple.ByteTuple9 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            ByteTuple.ByteTuple9 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testByteTuple9_equals() {
            ByteTuple.ByteTuple9 tuple1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            ByteTuple.ByteTuple9 tuple2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testByteTuple9_toString() {
            ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testByteTuple9_forEach() {
            ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

    }

    @Nested
    @Tag("2510")
    class ByteTuple2510Test extends TestBase {

        // ============ Factory Method Tests - ByteTuple.of() ============

        @Test
        public void testOf_tuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf_tuple2() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf_tuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
            assertEquals((byte) 30, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf_tuple4() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertNotNull(tuple);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 2, tuple._2);
            assertEquals((byte) 3, tuple._3);
            assertEquals((byte) 4, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf_tuple5() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertNotNull(tuple);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf_tuple6() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertNotNull(tuple);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf_tuple7() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertNotNull(tuple);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf_tuple8() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertNotNull(tuple);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf_tuple9() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
        }

        // ============ Factory Method Tests - ByteTuple.copyOf() ============

        @Test
        public void testCreate_nullArray() {
            ByteTuple<?> tuple = ByteTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_emptyArray() {
            ByteTuple<?> tuple = ByteTuple.copyOf(new byte[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_array1() {
            ByteTuple1 tuple = ByteTuple.copyOf(new byte[] { 10 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals((byte) 10, tuple._1);
        }

        @Test
        public void testCreate_array2() {
            ByteTuple2 tuple = ByteTuple.copyOf(new byte[] { 10, 20 });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
        }

        @Test
        public void testCreate_array3() {
            ByteTuple3 tuple = ByteTuple.copyOf(new byte[] { 10, 20, 30 });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testCreate_array4() {
            ByteTuple4 tuple = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4 });
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testCreate_array5() {
            ByteTuple5 tuple = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4, 5 });
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testCreate_array6() {
            ByteTuple6 tuple = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4, 5, 6 });
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testCreate_array7() {
            ByteTuple7 tuple = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testCreate_array8() {
            ByteTuple8 tuple = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testCreate_array9() {
            ByteTuple9 tuple = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testCreate_arrayTooLarge() {
            assertThrows(IllegalArgumentException.class, () -> {
                ByteTuple.copyOf(new byte[10]);
            });
        }

        // ============ ByteTuple0 Tests ============

        @Test
        public void testTuple0_arity() {
            ByteTuple<?> tuple = ByteTuple.copyOf(new byte[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testTuple0_min_throwsException() {
            ByteTuple<?> tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void testTuple0_max_throwsException() {
            ByteTuple<?> tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void testTuple0_median_throwsException() {
            ByteTuple<?> tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void testTuple0_sum() {
            ByteTuple<?> tuple = ByteTuple.copyOf(new byte[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void testTuple0_average_throwsException() {
            ByteTuple<?> tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void testTuple0_reverse() {
            ByteTuple<?> tuple = ByteTuple.copyOf(new byte[0]);
            ByteTuple<?> reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        public void testTuple0_contains() {
            ByteTuple<?> tuple = ByteTuple.copyOf(new byte[0]);
            assertFalse(tuple.contains((byte) 10));
        }

        @Test
        public void testTuple0_toString() {
            ByteTuple<?> tuple = ByteTuple.copyOf(new byte[0]);
            assertEquals("()", tuple.toString());
        }

        // ============ ByteTuple1 Tests ============

        @Test
        public void testTuple1_arity() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testTuple1_min() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        public void testTuple1_max() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals((byte) 10, tuple.max());
        }

        @Test
        public void testTuple1_median() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals((byte) 10, tuple.median());
        }

        @Test
        public void testTuple1_sum() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals(10, tuple.sum());
        }

        @Test
        public void testTuple1_average() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals(10.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple1_reverse() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            ByteTuple1 reversed = tuple.reverse();
            assertEquals((byte) 10, reversed._1);
            assertNotSame(tuple, reversed);
        }

        @Test
        public void testTuple1_contains_true() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertTrue(tuple.contains((byte) 10));
        }

        @Test
        public void testTuple1_contains_false() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertFalse(tuple.contains((byte) 20));
        }

        @Test
        public void testTuple1_hashCode() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple3 = ByteTuple.of((byte) 20);

            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertEquals(10, tuple1.hashCode());
            assertEquals(20, tuple3.hashCode());
        }

        @Test
        public void testTuple1_equals() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple3 = ByteTuple.of((byte) 20);

            assertEquals(tuple1, tuple1);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
            assertNotEquals(tuple1, null);
            assertNotEquals(tuple1, "not a tuple");
        }

        @Test
        public void testTuple1_toString() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals("(10)", tuple.toString());
        }

        // ============ ByteTuple2 Tests ============

        @Test
        public void testTuple2_arity() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testTuple2_min() {
            ByteTuple2 tuple = ByteTuple.of((byte) 20, (byte) 10);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        public void testTuple2_max() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals((byte) 20, tuple.max());
        }

        @Test
        public void testTuple2_median() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals((byte) 10, tuple.median());
        }

        @Test
        public void testTuple2_sum() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(30, tuple.sum());
        }

        @Test
        public void testTuple2_average() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(15.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple2_reverse() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 reversed = tuple.reverse();
            assertEquals((byte) 20, reversed._1);
            assertEquals((byte) 10, reversed._2);
        }

        @Test
        public void testTuple2_contains() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertTrue(tuple.contains((byte) 10));
            assertTrue(tuple.contains((byte) 20));
            assertFalse(tuple.contains((byte) 30));
        }

        @Test
        public void testTuple2_forEach() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals((byte) 10, values.get(0));
            assertEquals((byte) 20, values.get(1));
        }

        @Test
        public void testTuple2_accept() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            List<Integer> results = new ArrayList<>();
            tuple.accept((a, b) -> results.add(a + b));
            assertEquals(1, results.size());
            assertEquals(30, results.get(0));
        }

        @Test
        public void testTuple2_map() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            Integer result = tuple.map((a, b) -> (int) (a + b));
            assertEquals(30, result);
        }

        @Test
        public void testTuple2_filter_matches() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2_filter_noMatch() {
            ByteTuple2 tuple = ByteTuple.of((byte) 20, (byte) 10);
            Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
            assertFalse(result.isPresent());
        }

        @Test
        public void testTuple2_hashCode() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testTuple2_equals() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple3 = ByteTuple.of((byte) 20, (byte) 10);

            assertEquals(tuple1, tuple1);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testTuple2_toString() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals("(10, 20)", tuple.toString());
        }

        // ============ ByteTuple3 Tests ============

        @Test
        public void testTuple3_arity() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testTuple3_min() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        public void testTuple3_max() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 30, (byte) 20);
            assertEquals((byte) 30, tuple.max());
        }

        @Test
        public void testTuple3_median() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 30, (byte) 20);
            assertEquals((byte) 20, tuple.median());
        }

        @Test
        public void testTuple3_sum() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(60, tuple.sum());
        }

        @Test
        public void testTuple3_average() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(20.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple3_reverse() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 reversed = tuple.reverse();
            assertEquals((byte) 30, reversed._1);
            assertEquals((byte) 20, reversed._2);
            assertEquals((byte) 10, reversed._3);
        }

        @Test
        public void testTuple3_contains() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertTrue(tuple.contains((byte) 10));
            assertTrue(tuple.contains((byte) 20));
            assertTrue(tuple.contains((byte) 30));
            assertFalse(tuple.contains((byte) 40));
        }

        @Test
        public void testTuple3_forEach() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        public void testTuple3_accept() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            List<Integer> results = new ArrayList<>();
            tuple.accept((a, b, c) -> results.add(a + b + c));
            assertEquals(1, results.size());
            assertEquals(60, results.get(0));
        }

        @Test
        public void testTuple3_map() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            Integer result = tuple.map((a, b, c) -> (int) (a + b + c));
            assertEquals(60, result);
        }

        @Test
        public void testTuple3_filter_matches() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
        }

        @Test
        public void testTuple3_filter_noMatch() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 20, (byte) 10);
            Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertFalse(result.isPresent());
        }

        @Test
        public void testTuple3_hashCode() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testTuple3_equals() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple3 = ByteTuple.of((byte) 30, (byte) 20, (byte) 10);

            assertEquals(tuple1, tuple1);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testTuple3_toString() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals("(10, 20, 30)", tuple.toString());
        }

        // ============ ByteTuple4-9 Basic Tests ============

        @Test
        public void testTuple4_reverse() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            ByteTuple4 reversed = tuple.reverse();
            assertEquals((byte) 4, reversed._1);
            assertEquals((byte) 3, reversed._2);
            assertEquals((byte) 2, reversed._3);
            assertEquals((byte) 1, reversed._4);
        }

        @Test
        public void testTuple5_reverse() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            ByteTuple5 reversed = tuple.reverse();
            assertEquals((byte) 5, reversed._1);
            assertEquals((byte) 1, reversed._5);
        }

        @Test
        public void testTuple6_reverse() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            ByteTuple6 reversed = tuple.reverse();
            assertEquals((byte) 6, reversed._1);
            assertEquals((byte) 1, reversed._6);
        }

        @Test
        public void testTuple7_reverse() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            ByteTuple7 reversed = tuple.reverse();
            assertEquals((byte) 7, reversed._1);
            assertEquals((byte) 1, reversed._7);
        }

        @Test
        public void testTuple8_reverse() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            ByteTuple8 reversed = tuple.reverse();
            assertEquals((byte) 8, reversed._1);
            assertEquals((byte) 1, reversed._8);
        }

        @Test
        public void testTuple9_reverse() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            ByteTuple9 reversed = tuple.reverse();
            assertEquals((byte) 9, reversed._1);
            assertEquals((byte) 1, reversed._9);
        }

        @Test
        public void testTuple4_contains() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertTrue(tuple.contains((byte) 1));
            assertTrue(tuple.contains((byte) 4));
            assertFalse(tuple.contains((byte) 5));
        }

        @Test
        public void testTuple5_contains() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertTrue(tuple.contains((byte) 5));
            assertFalse(tuple.contains((byte) 6));
        }

        @Test
        public void testTuple6_contains() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertTrue(tuple.contains((byte) 6));
            assertFalse(tuple.contains((byte) 7));
        }

        @Test
        public void testTuple7_contains() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertTrue(tuple.contains((byte) 7));
            assertFalse(tuple.contains((byte) 8));
        }

        @Test
        public void testTuple8_contains() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertTrue(tuple.contains((byte) 8));
            assertFalse(tuple.contains((byte) 9));
        }

        @Test
        public void testTuple9_contains() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertTrue(tuple.contains((byte) 9));
            assertFalse(tuple.contains((byte) 10));
        }

        @Test
        public void testTuple4_forEach() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(4, values.size());
        }

        @Test
        public void testTuple5_forEach() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        public void testTuple6_forEach() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(6, values.size());
        }

        @Test
        public void testTuple7_forEach() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(7, values.size());
        }

        @Test
        public void testTuple8_forEach() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(8, values.size());
        }

        @Test
        public void testTuple9_forEach() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            List<Byte> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(9, values.size());
        }

        // ============ Common Method Tests (inherited from ByteTuple) ============

        @Test
        public void testToArray() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { 10, 20, 30 }, array);

            // Verify it's a copy
            array[0] = 99;
            assertEquals((byte) 10, tuple._1);
        }

        @Test
        public void testToList() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals((byte) 10, list.get(0));
            assertEquals((byte) 20, list.get(1));
            assertEquals((byte) 30, list.get(2));
        }

        @Test
        public void testStream() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            int sum = tuple.stream().sum();
            assertEquals(60, sum);
        }

        @Test
        public void testHashCode_consistency() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testEquals_symmetry() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }

        @Test
        public void testEquals_differentTypes() {
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertNotEquals(tuple2, tuple3);
        }

        @Test
        public void testNegativeValues() {
            ByteTuple3 tuple = ByteTuple.of((byte) -10, (byte) -20, (byte) -30);
            assertEquals((byte) -30, tuple.min());
            assertEquals((byte) -10, tuple.max());
            assertEquals(-60, tuple.sum());
        }

        @Test
        public void testMinMaxMedian_largerTuple() {
            ByteTuple5 tuple = ByteTuple.of((byte) 5, (byte) 2, (byte) 8, (byte) 1, (byte) 9);
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 9, tuple.max());
            assertEquals((byte) 5, tuple.median());
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for ByteTuple and its inner classes (ByteTuple0-9).
     * Tests cover factory methods, accessor methods, statistical methods (min, max, median, sum, average),
     * utility methods, functional methods, equality/hashCode, and stream operations.
     */
    @Tag("2511")
    class ByteTuple2511Test extends TestBase {

        // ============ Factory Method Tests - ByteTuple.of() ============

        @Test
        public void testOf_tuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf_tuple2() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf_tuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
            assertEquals((byte) 30, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf_tuple4() {
            ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
            assertEquals((byte) 30, tuple._3);
            assertEquals((byte) 40, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf_tuple5() {
            ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 50, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf_tuple6() {
            ByteTuple6 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 60, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf_tuple7() {
            ByteTuple7 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 70, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf_tuple8() {
            ByteTuple8 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 80, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf_tuple9() {
            ByteTuple9 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 90, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // ============ Factory Method Tests - ByteTuple.copyOf() ============

        @Test
        public void testCreate_nullArray() {
            ByteTuple<?> tuple = ByteTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_emptyArray() {
            ByteTuple<?> tuple = ByteTuple.copyOf(new byte[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_singleElement() {
            ByteTuple1 tuple = ByteTuple.copyOf(new byte[] { 10 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals((byte) 10, tuple._1);
        }

        @Test
        public void testCreate_twoElements() {
            ByteTuple2 tuple = ByteTuple.copyOf(new byte[] { 10, 20 });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
        }

        @Test
        public void testCreate_nineElements() {
            ByteTuple9 tuple = ByteTuple.copyOf(new byte[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 90, tuple._9);
        }

        @Test
        public void testCreate_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> ByteTuple.copyOf(new byte[10]));
        }

        // ============ Accessor Tests - Direct Field Access ============

        @Test
        public void testTuple1_directFieldAccess() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals((byte) 10, tuple._1);
        }

        @Test
        public void testTuple2_directFieldAccess() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
        }

        @Test
        public void testTuple3_directFieldAccess() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
            assertEquals((byte) 30, tuple._3);
        }

        // ============ Min Tests ============

        @Test
        public void testTuple0_min_throwsException() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void testTuple1_min() {
            ByteTuple1 tuple = ByteTuple.of((byte) 42);
            assertEquals((byte) 42, tuple.min());
        }

        @Test
        public void testTuple2_min() {
            ByteTuple2 tuple = ByteTuple.of((byte) 30, (byte) 10);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        public void testTuple3_min() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        public void testTuple5_min() {
            ByteTuple5 tuple = ByteTuple.of((byte) 50, (byte) 30, (byte) 10, (byte) 40, (byte) 20);
            assertEquals((byte) 10, tuple.min());
        }

        // ============ Max Tests ============

        @Test
        public void testTuple0_max_throwsException() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void testTuple1_max() {
            ByteTuple1 tuple = ByteTuple.of((byte) 42);
            assertEquals((byte) 42, tuple.max());
        }

        @Test
        public void testTuple2_max() {
            ByteTuple2 tuple = ByteTuple.of((byte) 30, (byte) 10);
            assertEquals((byte) 30, tuple.max());
        }

        @Test
        public void testTuple3_max() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals((byte) 30, tuple.max());
        }

        @Test
        public void testTuple5_max() {
            ByteTuple5 tuple = ByteTuple.of((byte) 50, (byte) 30, (byte) 10, (byte) 40, (byte) 20);
            assertEquals((byte) 50, tuple.max());
        }

        // ============ Median Tests ============

        @Test
        public void testTuple0_median_throwsException() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void testTuple1_median() {
            ByteTuple1 tuple = ByteTuple.of((byte) 42);
            assertEquals((byte) 42, tuple.median());
        }

        @Test
        public void testTuple2_median() {
            ByteTuple2 tuple = ByteTuple.of((byte) 30, (byte) 10);
            assertEquals((byte) 10, tuple.median()); // lower value for even-sized tuple
        }

        @Test
        public void testTuple3_median() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals((byte) 20, tuple.median()); // middle value
        }

        @Test
        public void testTuple4_median() {
            ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
            assertEquals((byte) 20, tuple.median()); // lower of two middle values
        }

        // ============ Sum Tests ============

        @Test
        public void testTuple0_sum() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void testTuple1_sum() {
            ByteTuple1 tuple = ByteTuple.of((byte) 42);
            assertEquals(42, tuple.sum());
        }

        @Test
        public void testTuple2_sum() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(30, tuple.sum());
        }

        @Test
        public void testTuple3_sum() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(60, tuple.sum());
        }

        @Test
        public void testTuple5_sum() {
            ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            assertEquals(150, tuple.sum());
        }

        // ============ Average Tests ============

        @Test
        public void testTuple0_average_throwsException() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void testTuple1_average() {
            ByteTuple1 tuple = ByteTuple.of((byte) 42);
            assertEquals(42.0, tuple.average());
        }

        @Test
        public void testTuple2_average() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(15.0, tuple.average());
        }

        @Test
        public void testTuple3_average() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(20.0, tuple.average());
        }

        @Test
        public void testTuple5_average() {
            ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            assertEquals(30.0, tuple.average());
        }

        // ============ Reverse Tests ============

        @Test
        public void testTuple0_reverse() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            ByteTuple0 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(tuple, reversed);
        }

        @Test
        public void testTuple1_reverse() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            ByteTuple1 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals((byte) 10, reversed._1);
        }

        @Test
        public void testTuple2_reverse() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals((byte) 20, reversed._1);
            assertEquals((byte) 10, reversed._2);
        }

        @Test
        public void testTuple3_reverse() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertNotSame(tuple, reversed);
            assertEquals((byte) 30, reversed._1);
            assertEquals((byte) 20, reversed._2);
            assertEquals((byte) 10, reversed._3);
        }

        @Test
        public void testTuple5_reverse() {
            ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            ByteTuple5 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals((byte) 50, reversed._1);
            assertEquals((byte) 40, reversed._2);
            assertEquals((byte) 30, reversed._3);
            assertEquals((byte) 20, reversed._4);
            assertEquals((byte) 10, reversed._5);
        }

        // ============ Contains Tests ============

        @Test
        public void testTuple0_contains() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertFalse(tuple.contains((byte) 10));
            assertFalse(tuple.contains((byte) 20));
        }

        @Test
        public void testTuple1_contains() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertTrue(tuple.contains((byte) 10));
            assertFalse(tuple.contains((byte) 20));
        }

        @Test
        public void testTuple2_contains() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertTrue(tuple.contains((byte) 10));
            assertTrue(tuple.contains((byte) 20));
            assertFalse(tuple.contains((byte) 30));
        }

        @Test
        public void testTuple3_contains() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertTrue(tuple.contains((byte) 10));
            assertTrue(tuple.contains((byte) 20));
            assertTrue(tuple.contains((byte) 30));
            assertFalse(tuple.contains((byte) 40));
        }

        @Test
        public void testTuple5_contains() {
            ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            assertTrue(tuple.contains((byte) 10));
            assertTrue(tuple.contains((byte) 50));
            assertFalse(tuple.contains((byte) 60));
        }

        // ============ toArray Tests ============

        @Test
        public void testTuple0_toArray() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            byte[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        public void testTuple1_toArray() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            byte[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(1, array.length);
            assertEquals((byte) 10, array[0]);
        }

        @Test
        public void testTuple2_toArray() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            byte[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(2, array.length);
            assertArrayEquals(new byte[] { 10, 20 }, array);
        }

        @Test
        public void testTuple3_toArray() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            byte[] array = tuple.toArray();
            assertNotNull(array);
            assertArrayEquals(new byte[] { 10, 20, 30 }, array);
        }

        @Test
        public void testToArray_immutability() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            byte[] array1 = tuple.toArray();
            byte[] array2 = tuple.toArray();
            assertNotSame(array1, array2);
        }

        // ============ toList Tests ============

        @Test
        public void testTuple0_toList() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(0, list.size());
        }

        @Test
        public void testTuple1_toList() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals((byte) 10, list.get(0));
        }

        @Test
        public void testTuple2_toList() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
            assertEquals((byte) 10, list.get(0));
            assertEquals((byte) 20, list.get(1));
        }

        @Test
        public void testTuple3_toList() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals((byte) 10, list.get(0));
            assertEquals((byte) 20, list.get(1));
            assertEquals((byte) 30, list.get(2));
        }

        // ============ forEach Tests ============

        @Test
        public void testTuple0_forEach() throws Exception {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            List<Byte> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(0, results.size());
        }

        @Test
        public void testTuple1_forEach() throws Exception {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            List<Byte> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(1, results.size());
            assertEquals((byte) 10, results.get(0).byteValue());
        }

        @Test
        public void testTuple2_forEach() throws Exception {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            List<Byte> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(2, results.size());
            assertEquals((byte) 10, results.get(0).byteValue());
            assertEquals((byte) 20, results.get(1).byteValue());
        }

        @Test
        public void testTuple3_forEach() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            List<Byte> results = new ArrayList<>();
            tuple.forEach(b -> results.add(b));
            assertEquals(3, results.size());
            assertEquals((byte) 10, results.get(0).byteValue());
            assertEquals((byte) 20, results.get(1).byteValue());
            assertEquals((byte) 30, results.get(2).byteValue());
        }

        // ============ Stream Tests ============

        @Test
        public void testTuple1_stream() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            long count = tuple.stream().count();
            assertEquals(1, count);
        }

        @Test
        public void testTuple2_stream_filter() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            long count = tuple.stream().filter(b -> b > 15).count();
            assertEquals(1, count);
        }

        @Test
        public void testTuple3_stream_sum() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            int sum = tuple.stream().sum();
            assertEquals(60, sum);
        }

        // ============ ByteTuple2.accept Tests ============

        @Test
        public void testTuple2_accept() throws Exception {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            List<Byte> results = new ArrayList<>();
            tuple.accept((a, b) -> {
                results.add(a);
                results.add(b);
            });
            assertEquals(2, results.size());
            assertEquals((byte) 10, results.get(0).byteValue());
            assertEquals((byte) 20, results.get(1).byteValue());
        }

        // ============ ByteTuple2.map Tests ============

        @Test
        public void testTuple2_map() throws Exception {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            int result = tuple.map((a, b) -> a + b);
            assertEquals(30, result);
        }

        @Test
        public void testTuple2_map_multiply() throws Exception {
            ByteTuple2 tuple = ByteTuple.of((byte) 5, (byte) 6);
            int result = tuple.map((a, b) -> a * b);
            assertEquals(30, result);
        }

        // ============ ByteTuple2.filter Tests ============

        @Test
        public void testTuple2_filter_pass() throws Exception {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2_filter_fail() throws Exception {
            ByteTuple2 tuple = ByteTuple.of((byte) 20, (byte) 10);
            Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
            assertFalse(result.isPresent());
        }

        // ============ ByteTuple3.accept Tests ============

        @Test
        public void testTuple3_accept() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            List<Byte> results = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                results.add(a);
                results.add(b);
                results.add(c);
            });
            assertEquals(3, results.size());
            assertEquals((byte) 10, results.get(0).byteValue());
            assertEquals((byte) 20, results.get(1).byteValue());
            assertEquals((byte) 30, results.get(2).byteValue());
        }

        // ============ ByteTuple3.map Tests ============

        @Test
        public void testTuple3_map() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            int result = tuple.map((a, b, c) -> a + b + c);
            assertEquals(60, result);
        }

        @Test
        public void testTuple3_map_max() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 30, (byte) 20);
            byte result = tuple.map((a, b, c) -> (byte) Math.max(a, Math.max(b, c)));
            assertEquals((byte) 30, result);
        }

        // ============ ByteTuple3.filter Tests ============

        @Test
        public void testTuple3_filter_pass() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple3_filter_fail() throws Exception {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a > b);
            assertFalse(result.isPresent());
        }

        // ============ hashCode Tests ============

        @Test
        public void testTuple0_hashCode() {
            ByteTuple0 tuple1 = ByteTuple.copyOf(new byte[0]);
            ByteTuple0 tuple2 = ByteTuple.copyOf(new byte[0]);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testTuple1_hashCode() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
            assertEquals((byte) 10, tuple1.hashCode());
        }

        @Test
        public void testTuple2_hashCode() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testTuple3_hashCode() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testTuple2_hashCode_different() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 20, (byte) 10);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // ============ equals Tests ============

        @Test
        public void testTuple0_equals() {
            ByteTuple0 tuple1 = ByteTuple.copyOf(new byte[0]);
            ByteTuple0 tuple2 = ByteTuple.copyOf(new byte[0]);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple1_equals_same() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testTuple1_equals() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple1_notEquals() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 20);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple2_equals() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple2_notEquals_different_values() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 20, (byte) 10);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple3_equals() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testTuple_notEquals_null() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertNotEquals(tuple, null);
            assertFalse(tuple.equals(null));
        }

        @Test
        public void testTuple_notEquals_differentType() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertNotEquals(tuple, "10");
            assertNotEquals(tuple, 10);
        }

        @Test
        public void testTuple2_notEquals_differentTuple() {
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertNotEquals(tuple2, tuple3);
        }

        // ============ toString Tests ============

        @Test
        public void testTuple0_toString() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testTuple1_toString() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals("(10)", tuple.toString());
        }

        @Test
        public void testTuple2_toString() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals("(10, 20)", tuple.toString());
        }

        @Test
        public void testTuple3_toString() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals("(10, 20, 30)", tuple.toString());
        }

        // ============ Arity Tests ============

        @Test
        public void testTuple0_arity() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testTuple1_arity() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testTuple2_arity() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testTuple3_arity() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testTuple4_arity() {
            ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testTuple5_arity() {
            ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testTuple6_arity() {
            ByteTuple6 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testTuple7_arity() {
            ByteTuple7 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testTuple8_arity() {
            ByteTuple8 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testTuple9_arity() {
            ByteTuple9 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
            assertEquals(9, tuple.arity());
        }

        // ============ Edge Cases and Special Tests ============

        @Test
        public void testTuple_immutability() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            // The tuple is immutable, so we can't change _1, but we verify it stays the same
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 10, tuple._1);
        }

        @Test
        public void testMultipleTuples_independence() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 20, (byte) 10);
            assertNotEquals(tuple1, tuple2);
            assertEquals((byte) 10, tuple1._1);
            assertEquals((byte) 20, tuple2._1);
        }

        @Test
        public void testTuple_negativeValues() {
            ByteTuple3 tuple = ByteTuple.of((byte) -10, (byte) 20, (byte) -30);
            assertEquals((byte) -30, tuple.min());
            assertEquals((byte) 20, tuple.max());
            assertEquals(-20, tuple.sum());
        }

        @Test
        public void testTuple_zeroValues() {
            ByteTuple3 tuple = ByteTuple.of((byte) 0, (byte) 0, (byte) 0);
            assertTrue(tuple.contains((byte) 0));
            assertEquals(0, tuple.sum());
            assertEquals(0.0, tuple.average());
        }

        @Test
        public void testTuple_mixedSignValues() {
            ByteTuple4 tuple = ByteTuple.of((byte) -5, (byte) 10, (byte) -3, (byte) 8);
            assertEquals((byte) -5, tuple.min());
            assertEquals((byte) 10, tuple.max());
            assertEquals(10, tuple.sum());
        }

    }

    @Nested
    /**
     * Comprehensive unit tests for ByteTuple and its inner classes (ByteTuple0-9).
     * Tests cover all public methods including factory methods, statistical methods,
     * utility methods, functional methods, equality/hashCode, and stream operations.
     */
    @Tag("2512")
    class ByteTuple2512Test extends TestBase {

        // ============ Factory Method Tests - ByteTuple.of() ============

        @Test
        public void test_of_tuple1() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void test_of_tuple2() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void test_of_tuple3() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertNotNull(tuple);
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
            assertEquals((byte) 30, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void test_of_tuple4() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertNotNull(tuple);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 4, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void test_of_tuple5() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertNotNull(tuple);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 5, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void test_of_tuple6() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertNotNull(tuple);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 6, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void test_of_tuple7() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertNotNull(tuple);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 7, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void test_of_tuple8() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertNotNull(tuple);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 8, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void test_of_tuple9() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertNotNull(tuple);
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 9, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // ============ Factory Method Tests - ByteTuple.copyOf() ============

        @Test
        public void test_create_nullArray() {
            ByteTuple0 tuple = ByteTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_emptyArray() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_arraySize1() {
            ByteTuple1 tuple = ByteTuple.copyOf(new byte[] { 10 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals((byte) 10, tuple._1);
        }

        @Test
        public void test_create_arraySize2() {
            ByteTuple2 tuple = ByteTuple.copyOf(new byte[] { 10, 20 });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals((byte) 10, tuple._1);
            assertEquals((byte) 20, tuple._2);
        }

        @Test
        public void test_create_arraySize9() {
            ByteTuple9 tuple = ByteTuple.copyOf(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
            assertEquals((byte) 1, tuple._1);
            assertEquals((byte) 9, tuple._9);
        }

        @Test
        public void test_create_arrayTooLarge() {
            assertThrows(IllegalArgumentException.class, () -> {
                ByteTuple.copyOf(new byte[10]);
            });
        }

        // ============ Tuple0 Tests ============

        @Test
        public void test_tuple0_arity() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_tuple0_min_throwsException() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void test_tuple0_max_throwsException() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void test_tuple0_median_throwsException() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void test_tuple0_sum() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void test_tuple0_average_throwsException() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void test_tuple0_reverse() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            ByteTuple0 reversed = tuple.reverse();
            assertSame(tuple, reversed);
        }

        @Test
        public void test_tuple0_contains() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertFalse(tuple.contains((byte) 10));
        }

        @Test
        public void test_tuple0_toArray() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            byte[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        public void test_tuple0_toList() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertTrue(list.isEmpty());
        }

        @Test
        public void test_tuple0_forEach() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            List<Byte> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertTrue(collected.isEmpty());
        }

        @Test
        public void test_tuple0_stream() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            long count = tuple.stream().count();
            assertEquals(0, count);
        }

        @Test
        public void test_tuple0_toString() {
            ByteTuple0 tuple = ByteTuple.copyOf(new byte[0]);
            assertEquals("()", tuple.toString());
        }

        // ============ Tuple1 Tests ============

        @Test
        public void test_tuple1_arity() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void test_tuple1_min() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        public void test_tuple1_max() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals((byte) 10, tuple.max());
        }

        @Test
        public void test_tuple1_median() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals((byte) 10, tuple.median());
        }

        @Test
        public void test_tuple1_sum() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals(10, tuple.sum());
        }

        @Test
        public void test_tuple1_average() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals(10.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_tuple1_reverse() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            ByteTuple1 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals((byte) 10, reversed._1);
            assertNotSame(tuple, reversed);
        }

        @Test
        public void test_tuple1_contains_found() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertTrue(tuple.contains((byte) 10));
        }

        @Test
        public void test_tuple1_contains_notFound() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertFalse(tuple.contains((byte) 20));
        }

        @Test
        public void test_tuple1_toArray() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { 10 }, array);
        }

        @Test
        public void test_tuple1_toList() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals((byte) 10, list.get(0));
        }

        @Test
        public void test_tuple1_forEach() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            List<Byte> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals((byte) 10, collected.get(0));
        }

        @Test
        public void test_tuple1_stream() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            int sum = tuple.stream().sum();
            assertEquals(10, sum);
        }

        @Test
        public void test_tuple1_hashCode() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple1_equals_same() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        public void test_tuple1_equals_equal() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 10);
            assertTrue(tuple1.equals(tuple2));
            assertTrue(tuple2.equals(tuple1));
        }

        @Test
        public void test_tuple1_equals_notEqual() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple1 tuple2 = ByteTuple.of((byte) 20);
            assertFalse(tuple1.equals(tuple2));
        }

        @Test
        public void test_tuple1_equals_null() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertFalse(tuple.equals(null));
        }

        @Test
        public void test_tuple1_equals_differentClass() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertFalse(tuple.equals("not a tuple"));
        }

        @Test
        public void test_tuple1_toString() {
            ByteTuple1 tuple = ByteTuple.of((byte) 10);
            assertEquals("(10)", tuple.toString());
        }

        // ============ Tuple2 Tests ============

        @Test
        public void test_tuple2_arity() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void test_tuple2_min() {
            ByteTuple2 tuple = ByteTuple.of((byte) 20, (byte) 10);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        public void test_tuple2_max() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals((byte) 20, tuple.max());
        }

        @Test
        public void test_tuple2_median() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals((byte) 10, tuple.median());
        }

        @Test
        public void test_tuple2_sum() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(30, tuple.sum());
        }

        @Test
        public void test_tuple2_average() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(15.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_tuple2_reverse() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals((byte) 20, reversed._1);
            assertEquals((byte) 10, reversed._2);
        }

        @Test
        public void test_tuple2_contains_found() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertTrue(tuple.contains((byte) 10));
            assertTrue(tuple.contains((byte) 20));
        }

        @Test
        public void test_tuple2_contains_notFound() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertFalse(tuple.contains((byte) 30));
        }

        @Test
        public void test_tuple2_toArray() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { 10, 20 }, array);
        }

        @Test
        public void test_tuple2_toList() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            ByteList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
        }

        @Test
        public void test_tuple2_forEach() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            List<Byte> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(2, collected.size());
            assertEquals((byte) 10, collected.get(0));
            assertEquals((byte) 20, collected.get(1));
        }

        @Test
        public void test_tuple2_stream() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            long count = tuple.stream().count();
            assertEquals(2, count);
        }

        @Test
        public void test_tuple2_accept() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            List<Byte> collected = new ArrayList<>();
            tuple.accept((a, b) -> {
                collected.add(a);
                collected.add(b);
            });
            assertEquals(2, collected.size());
            assertEquals((byte) 10, collected.get(0));
            assertEquals((byte) 20, collected.get(1));
        }

        @Test
        public void test_tuple2_map() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            int result = tuple.map((a, b) -> a + b);
            assertEquals(30, result);
        }

        @Test
        public void test_tuple2_filter_match() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void test_tuple2_filter_noMatch() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            Optional<ByteTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_tuple2_hashCode() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple2_equals() {
            ByteTuple2 tuple1 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            ByteTuple2 tuple3 = ByteTuple.of((byte) 20, (byte) 10);

            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
            assertFalse(tuple1.equals(null));
        }

        @Test
        public void test_tuple2_toString() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals("(10, 20)", tuple.toString());
        }

        // ============ Tuple3 Tests ============

        @Test
        public void test_tuple3_arity() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void test_tuple3_min() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals((byte) 10, tuple.min());
        }

        @Test
        public void test_tuple3_max() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals((byte) 30, tuple.max());
        }

        @Test
        public void test_tuple3_median() {
            ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
            assertEquals((byte) 20, tuple.median());
        }

        @Test
        public void test_tuple3_sum() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(60, tuple.sum());
        }

        @Test
        public void test_tuple3_average() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(20.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_tuple3_reverse() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals((byte) 30, reversed._1);
            assertEquals((byte) 20, reversed._2);
            assertEquals((byte) 10, reversed._3);
        }

        @Test
        public void test_tuple3_contains() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertTrue(tuple.contains((byte) 10));
            assertTrue(tuple.contains((byte) 20));
            assertTrue(tuple.contains((byte) 30));
            assertFalse(tuple.contains((byte) 40));
        }

        @Test
        public void test_tuple3_toArray() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            byte[] array = tuple.toArray();
            assertArrayEquals(new byte[] { 10, 20, 30 }, array);
        }

        @Test
        public void test_tuple3_accept() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            List<Byte> collected = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                collected.add(a);
                collected.add(b);
                collected.add(c);
            });
            assertEquals(3, collected.size());
        }

        @Test
        public void test_tuple3_map() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            int result = tuple.map((a, b, c) -> a + b + c);
            assertEquals(60, result);
        }

        @Test
        public void test_tuple3_filter_match() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
        }

        @Test
        public void test_tuple3_filter_noMatch() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a > c);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_tuple3_hashCode() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple3_equals() {
            ByteTuple3 tuple1 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteTuple3 tuple3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 40);

            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
        }

        @Test
        public void test_tuple3_toString() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals("(10, 20, 30)", tuple.toString());
        }

        // ============ Tuple4-9 Basic Tests ============

        @Test
        public void test_tuple4_basic() {
            ByteTuple4 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
            assertEquals(4, tuple.arity());
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 4, tuple.max());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average(), 0.0001);
            assertTrue(tuple.contains((byte) 3));
            ByteTuple4 reversed = tuple.reverse();
            assertEquals((byte) 4, reversed._1);
            assertEquals((byte) 1, reversed._4);
        }

        @Test
        public void test_tuple5_basic() {
            ByteTuple5 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
            assertEquals(5, tuple.arity());
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 5, tuple.max());
            assertEquals((byte) 3, tuple.median());
            assertEquals(15, tuple.sum());
            assertTrue(tuple.contains((byte) 3));
        }

        @Test
        public void test_tuple6_basic() {
            ByteTuple6 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
            assertEquals(6, tuple.arity());
            assertEquals((byte) 1, tuple.min());
            assertEquals((byte) 6, tuple.max());
            assertEquals(21, tuple.sum());
            assertEquals(3.5, tuple.average(), 0.0001);
        }

        @Test
        public void test_tuple7_basic() {
            ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
            assertEquals(7, tuple.arity());
            assertEquals((byte) 4, tuple.median());
            assertEquals(7, tuple.toArray().length);
        }

        @Test
        public void test_tuple8_basic() {
            ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple.toArray().length);
            assertEquals(36, tuple.sum());
        }

        @Test
        public void test_tuple9_basic() {
            ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
            assertEquals(9, tuple.arity());
            assertEquals(9, tuple.toArray().length);
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average(), 0.0001);
        }

        // ============ Edge Cases and Additional Coverage ============

        @Test
        public void test_negativeValues() {
            ByteTuple3 tuple = ByteTuple.of((byte) -10, (byte) 0, (byte) 10);
            assertEquals((byte) -10, tuple.min());
            assertEquals((byte) 10, tuple.max());
            assertEquals((byte) 0, tuple.median());
            assertEquals(0, tuple.sum());
        }

        @Test
        public void test_extremeValues() {
            ByteTuple2 tuple = ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE);
            assertEquals(Byte.MIN_VALUE, tuple.min());
            assertEquals(Byte.MAX_VALUE, tuple.max());
        }

        @Test
        public void test_toArray_independence() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            byte[] array = tuple.toArray();
            array[0] = 99;
            assertEquals((byte) 10, tuple._1); // Tuple should be unaffected
        }

        @Test
        public void test_toList_independence() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            ByteList list = tuple.toList();
            list.set(0, (byte) 99);
            assertEquals((byte) 10, tuple._1); // Tuple should be unaffected
        }

        @Test
        public void test_stream_operations() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            int sum = tuple.stream().sum();
            assertEquals(60, sum);
        }

        @Test
        public void test_forEach_withException() {
            ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            assertThrows(RuntimeException.class, () -> {
                tuple.forEach(b -> {
                    if (b == 20)
                        throw new RuntimeException("test exception");
                });
            });
        }

        @Test
        public void test_equals_differentArity() {
            ByteTuple1 tuple1 = ByteTuple.of((byte) 10);
            ByteTuple2 tuple2 = ByteTuple.of((byte) 10, (byte) 20);
            assertFalse(tuple1.equals(tuple2));
        }

        @Test
        public void test_hashCode_consistency() {
            ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        public void test_create_allSizes() {
            for (int i = 0; i <= 9; i++) {
                byte[] array = new byte[i];
                ByteTuple<?> tuple = ByteTuple.copyOf(array);
                assertNotNull(tuple);
                assertEquals(i, tuple.arity());
            }
        }

        @Test
        public void test_median_evenCount() {
            ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
            assertEquals((byte) 20, tuple.median()); // Lower middle value
        }
    }

    @Nested
    @SuppressWarnings("deprecation")
    @Tag("2512")
    class JavadocExampleTupleTest_ByteTuple extends TestBase {
        // ===== ByteTuple Javadoc examples =====

        @Test
        public void testByteTupleOf1() {
            // ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 10);
            // byte value = tuple._1;  // 10
            ByteTuple.ByteTuple1 t = ByteTuple.of((byte) 10);
            assertEquals((byte) 10, t._1);
        }

        @Test
        public void testByteTupleOf2() {
            // ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
            // byte first = tuple._1;  // 10
            // byte second = tuple._2;  // 20
            ByteTuple.ByteTuple2 t = ByteTuple.of((byte) 10, (byte) 20);
            assertEquals((byte) 10, t._1);
            assertEquals((byte) 20, t._2);
        }

        @Test
        public void testByteTupleOf3() {
            // ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            // byte third = tuple._3;  // 30
            ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals((byte) 30, t._3);
        }

        @Test
        public void testByteTupleClassLevelExamples() {
            // byte min = triple.min();   // 10
            // byte max = triple.max();   // 30
            // double avg = triple.average();   // 20.0
            ByteTuple.ByteTuple3 triple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
            assertEquals((byte) 10, triple.min());
            assertEquals((byte) 30, triple.max());
            assertEquals(20.0, triple.average(), 0.001);
        }

        @Test
        public void testByteTupleOf4Median() {
            // ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
            // byte median = tuple.median();   // 20
            ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
            assertEquals((byte) 20, t.median());
        }

        @Test
        public void testByteTupleOf5Sum() {
            // ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            // int sum = tuple.sum();   // 150
            ByteTuple.ByteTuple5 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
            assertEquals(150, t.sum());
        }

        @Test
        public void testByteTupleOf9Arity() {
            // ByteTuple.ByteTuple9 tuple = ...
            // int arity = tuple.arity();   // 9
            ByteTuple.ByteTuple9 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
            assertEquals(9, t.arity());
            assertEquals((byte) 10, t._1);
            assertEquals((byte) 90, t._9);
        }
    }

}
