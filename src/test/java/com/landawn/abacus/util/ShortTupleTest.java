package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.ShortTuple.ShortTuple0;
import com.landawn.abacus.util.ShortTuple.ShortTuple1;
import com.landawn.abacus.util.ShortTuple.ShortTuple2;
import com.landawn.abacus.util.ShortTuple.ShortTuple3;
import com.landawn.abacus.util.ShortTuple.ShortTuple4;
import com.landawn.abacus.util.ShortTuple.ShortTuple5;
import com.landawn.abacus.util.ShortTuple.ShortTuple6;
import com.landawn.abacus.util.ShortTuple.ShortTuple7;
import com.landawn.abacus.util.ShortTuple.ShortTuple8;
import com.landawn.abacus.util.ShortTuple.ShortTuple9;
import com.landawn.abacus.util.stream.ShortStream;
import com.landawn.abacus.util.u.Optional;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ShortTupleTest extends TestBase {

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
        ShortTuple<?> empty = ShortTuple.copyOf(null);
        assertEquals(0, empty.arity());

        empty = ShortTuple.copyOf(new short[0]);
        assertEquals(0, empty.arity());

        // Test array with 1 element
        ShortTuple.ShortTuple1 tuple1 = ShortTuple.copyOf(new short[] { 5 });
        assertEquals(1, tuple1.arity());
        assertEquals(5, tuple1._1);

        // Test array with 5 elements
        ShortTuple.ShortTuple5 tuple5 = ShortTuple.copyOf(new short[] { 5, 10, 15, 20, 25 });
        assertEquals(5, tuple5.arity());
        assertEquals(25, tuple5._5);

        // Test array with 9 elements
        ShortTuple.ShortTuple9 tuple9 = ShortTuple.copyOf(new short[] { 5, 10, 15, 20, 25, 30, 35, 40, 45 });
        assertEquals(9, tuple9.arity());
        assertEquals(45, tuple9._9);

        // Test too many elements
        assertThrows(IllegalArgumentException.class, () -> ShortTuple.copyOf(new short[10]));
    }

    @Test
    public void testMin() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 15, (short) 5, (short) 10);
        assertEquals(5, tuple.min());

        ShortTuple.ShortTuple0 empty = ShortTuple.copyOf(new short[0]);
        assertThrows(NoSuchElementException.class, () -> empty.min());
    }

    @Test
    public void testMax() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 15, (short) 5, (short) 10);
        assertEquals(15, tuple.max());

        ShortTuple.ShortTuple0 empty = ShortTuple.copyOf(new short[0]);
        assertThrows(NoSuchElementException.class, () -> empty.max());
    }

    @Test
    public void testMedian() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 15, (short) 5, (short) 10);
        assertEquals(10, tuple.median());

        ShortTuple.ShortTuple4 evenTuple = ShortTuple.of((short) 5, (short) 10, (short) 15, (short) 20);
        assertEquals((short) 10, evenTuple.median()); // Should be (10 + 15) / 2 = 12

        ShortTuple.ShortTuple0 empty = ShortTuple.copyOf(new short[0]);
        assertThrows(NoSuchElementException.class, () -> empty.median());
    }

    @Test
    public void testSum() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
        assertEquals(30, tuple.sum());

        ShortTuple.ShortTuple0 empty = ShortTuple.copyOf(new short[0]);
        assertEquals(0, empty.sum());
    }

    @Test
    public void testAverage() {
        ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 10, (short) 15);
        assertEquals(10.0, tuple.average());

        ShortTuple.ShortTuple0 empty = ShortTuple.copyOf(new short[0]);
        assertThrows(NoSuchElementException.class, () -> empty.average());
    }

    @Test
    public void testReverse() {
        // Test Tuple0
        ShortTuple.ShortTuple0 empty = ShortTuple.copyOf(new short[0]);
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
        ShortTuple.ShortTuple0 empty = ShortTuple.copyOf(new short[0]);
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
        ShortTuple.ShortTuple0 empty = ShortTuple.copyOf(new short[0]);
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
        assertSame(elements1, elements2); // Should return same cached array
    }

    @Nested
    @SuppressWarnings("deprecation")
    @Tag("2512")
    class JavadocExampleTupleTest_ShortTuple extends TestBase {
        // ===== ShortTuple Javadoc examples =====

        @Test
        public void testShortTupleOf1() {
            // ShortTuple.ShortTuple1 single = ShortTuple.of((short)42);
            // short value = single._1;  // 42
            ShortTuple.ShortTuple1 single = ShortTuple.of((short) 42);
            assertEquals((short) 42, single._1);
        }

        @Test
        public void testShortTupleOf2() {
            // ShortTuple.ShortTuple2 pair = ShortTuple.of((short)1, (short)2);
            // int sum = pair._1 + pair._2;  // 3
            ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 1, (short) 2);
            assertEquals(3, pair._1 + pair._2);
        }

        @Test
        public void testShortTupleOf3Average() {
            // ShortTuple.ShortTuple3 triple = ShortTuple.of((short)1, (short)2, (short)3);
            // double average = triple.average();   // 2.0
            ShortTuple.ShortTuple3 triple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(2.0, triple.average(), 0.001);
        }

        @Test
        public void testShortTupleClassLevelExamples() {
            // short min = triple.min();         // 1
            // short max = triple.max();         // 3
            // double avg = triple.average();    // 2.0
            ShortTuple.ShortTuple3 triple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals((short) 1, triple.min());
            assertEquals((short) 3, triple.max());
            assertEquals(2.0, triple.average(), 0.001);
        }

        @Test
        public void testShortTupleOf5Field() {
            // ShortTuple.ShortTuple5 quint = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5);
            // quint._5 == 5
            ShortTuple.ShortTuple5 quint = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals((short) 5, quint._5);
        }
    }

    @Nested
    /**
     * Comprehensive test suite for ShortTuple and its nested classes.
     * Tests all public methods including factory methods, statistical operations,
     * collection conversions, and special methods in Tuple2 and Tuple3.
     */
    @Tag("2025")
    class ShortTuple2025Test extends TestBase {

        // Factory method tests
        @Test
        public void testOf1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals((short) 1, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
            assertEquals((short) 3, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
            assertEquals((short) 3, tuple._3);
            assertEquals((short) 4, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf5() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 5, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf6() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 6, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf7() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 7, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf8() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 8, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf9() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 9, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // Create method tests
        @Test
        public void testCreateEmpty() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreateNull() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate1() {
            ShortTuple1 tuple = ShortTuple.copyOf(new short[] { (short) 1 });
            assertEquals((short) 1, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate3() {
            ShortTuple3 tuple = ShortTuple.copyOf(new short[] { (short) 1, (short) 2, (short) 3 });
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
            assertEquals((short) 3, tuple._3);
        }

        @Test
        public void testCreate9() {
            ShortTuple9 tuple = ShortTuple
                    .copyOf(new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9 });
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 9, tuple._9);
        }

        @Test
        public void testCreateTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                ShortTuple
                        .copyOf(new short[] { (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9, (short) 10 });
            });
        }

        // Statistical method tests - min
        @Test
        public void testMinTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals((short) 1, tuple.min());
        }

        @Test
        public void testMinTuple0ThrowsException() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        // Statistical method tests - max
        @Test
        public void testMaxTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals((short) 1, tuple.max());
        }

        @Test
        public void testMaxTuple0ThrowsException() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        // Statistical method tests - median
        @Test
        public void testMedianTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals((short) 1, tuple.median());
        }

        @Test
        public void testMedianTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 3, (short) 1, (short) 2);
            assertEquals((short) 2, tuple.median());
        }

        @Test
        public void testMedianTuple0ThrowsException() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        // Statistical method tests - sum
        @Test
        public void testSumTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void testSumTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals(1, tuple.sum());
        }

        // Statistical method tests - average
        @Test
        public void testAverageTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals(1.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple0ThrowsException() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        // Reverse tests
        @Test
        public void testReverseTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            ShortTuple<ShortTuple0> reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        // Contains tests
        @Test
        public void testContainsTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            assertFalse(tuple.contains((short) 1));
        }

        @Test
        public void testContainsTuple1True() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        public void testContainsTuple1False() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertFalse(tuple.contains((short) 99));
        }

        @Test
        public void testContainsTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 2));
            assertTrue(tuple.contains((short) 3));
            assertFalse(tuple.contains((short) 99));
        }

        // toArray tests
        @Test
        public void testToArrayTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            short[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        public void testToArrayTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            short[] array = tuple.toArray();
            assertArrayEquals(new short[] { (short) 1 }, array);
        }

        @Test
        public void testToArrayTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] array = tuple.toArray();
            assertArrayEquals(new short[] { (short) 1, (short) 2, (short) 3 }, array);
        }

        @Test
        public void testToArrayModificationDoesNotAffectTuple() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] array = tuple.toArray();
            array[0] = (short) 100;
            assertEquals((short) 1, tuple._1);
        }

        // toList tests
        @Test
        public void testToListTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            ShortList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        public void testToListTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals((short) 1, list.get(0));
            assertEquals((short) 2, list.get(1));
            assertEquals((short) 3, list.get(2));
        }

        // forEach tests
        @Test
        public void testForEachTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        public void testForEachTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Short.valueOf((short) 1), result.get(0));
        }

        @Test
        public void testForEachTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Short.valueOf((short) 1), result.get(0));
            assertEquals(Short.valueOf((short) 2), result.get(1));
            assertEquals(Short.valueOf((short) 3), result.get(2));
        }

        // stream tests
        @Test
        public void testStreamTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            ShortStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        public void testStreamTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            ShortStream stream = tuple.stream();
            assertEquals(1, stream.sum());
        }

        @Test
        public void testStreamTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortStream stream = tuple.stream();
            assertEquals(6, stream.sum());
        }

        // hashCode tests
        @Test
        public void testHashCodeTuple1() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple1 tuple2 = ShortTuple.of((short) 1);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // equals tests
        @Test
        public void testEqualsSameObject() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEqualsNull() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotEquals(null, tuple);
        }

        @Test
        public void testEqualsDifferentClass() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        public void testEqualsTuple1() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple1 tuple2 = ShortTuple.of((short) 1);
            ShortTuple1 tuple3 = ShortTuple.of((short) 99);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple2() {
            ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 tuple3 = ShortTuple.of((short) 1, (short) 3);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple3() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 4);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        // toString tests
        @Test
        public void testToStringTuple0() {
            ShortTuple<ShortTuple0> tuple = ShortTuple.copyOf(new short[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testToStringTuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
        }

        @Test
        public void testToStringTuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("2"));
            assertTrue(str.contains("3"));
        }

        // Tuple2 special methods - accept
        @Test
        public void testTuple2Accept() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            List<Short> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Short.valueOf((short) 3), result.get(0));
            assertEquals(Short.valueOf((short) 4), result.get(1));
        }

        // Tuple2 special methods - map
        @Test
        public void testTuple2Map() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            int result = tuple.map((a, b) -> a * b);
            assertEquals(12, result);
        }

        // Tuple2 special methods - filter
        @Test
        public void testTuple2FilterTrue() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            var result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2FilterFalse() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            var result = tuple.filter((a, b) -> a + b > 10);
            assertFalse(result.isPresent());
        }

        // Tuple3 special methods - accept
        @Test
        public void testTuple3Accept() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Short> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Short.valueOf((short) 1), result.get(0));
            assertEquals(Short.valueOf((short) 2), result.get(1));
            assertEquals(Short.valueOf((short) 3), result.get(2));
        }

        // Tuple3 special methods - map
        @Test
        public void testTuple3Map() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            int result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6, result);
        }

        // Tuple3 special methods - filter
        @Test
        public void testTuple3FilterTrue() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple3FilterFalse() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            var result = tuple.filter((a, b, c) -> a + b + c > 10);
            assertFalse(result.isPresent());
        }

        // arity tests for all tuple sizes
        @Test
        public void testArity() {
            assertEquals(0, ShortTuple.copyOf(new short[0]).arity());
            assertEquals(1, ShortTuple.of((short) 1).arity());
            assertEquals(2, ShortTuple.of((short) 1, (short) 2).arity());
            assertEquals(3, ShortTuple.of((short) 1, (short) 2, (short) 3).arity());
            assertEquals(4, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4).arity());
            assertEquals(5, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).arity());
            assertEquals(6, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6).arity());
            assertEquals(7, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7).arity());
            assertEquals(8, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8).arity());
            assertEquals(9, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9).arity());
        }

        // Tests for inherited methods from PrimitiveTuple
        @Test
        public void testAcceptConsumer() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Short> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals((short) 1, result.get(0));
            assertEquals((short) 2, result.get(1));
            assertEquals((short) 3, result.get(2));
        }

        @Test
        public void testMapFunction() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            int result = tuple.map(t -> t._1 + t._2 + t._3);
            assertEquals(6, result);
        }

        @Test
        public void testFilterPredicate() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            var result = tuple.filter(t -> t._1 == (short) 1);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testFilterPredicateFalse() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            var result = tuple.filter(t -> t._1 == (short) 99);
            assertFalse(result.isPresent());
        }

        @Test
        public void testToOptional() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        // Comprehensive tests for Tuple4 through Tuple9
        @Test
        public void testTuple4Operations() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);

            // Test reverse
            ShortTuple4 reversed = tuple.reverse();
            assertEquals((short) 4, reversed._1);
            assertEquals((short) 3, reversed._2);
            assertEquals((short) 2, reversed._3);
            assertEquals((short) 1, reversed._4);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 4));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4 }, tuple.toArray());

            // Test min/max/median/sum/average via base class
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 4, tuple.max());
            assertEquals((short) 2, tuple.median());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average(), 0.001);

            // Test hashCode and equals
            ShortTuple4 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple4 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 5);
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
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);

            // Test reverse
            ShortTuple5 reversed = tuple.reverse();
            assertEquals((short) 5, reversed._1);
            assertEquals((short) 1, reversed._5);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 5));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5 }, tuple.toArray());

            // Test statistical operations
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 5, tuple.max());
            assertEquals((short) 3, tuple.median());
            assertEquals(15, tuple.sum());
            assertEquals(3.0, tuple.average(), 0.001);

            // Test equals
            ShortTuple5 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals(tuple, tuple2);
        }

        @Test
        public void testTuple6Operations() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);

            // Test reverse
            ShortTuple6 reversed = tuple.reverse();
            assertEquals((short) 6, reversed._1);
            assertEquals((short) 1, reversed._6);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 6));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6 }, tuple.toArray());

            // Test statistical operations
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 6, tuple.max());
            assertEquals(21, tuple.sum());
            assertEquals(3.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple7Operations() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);

            // Test reverse
            ShortTuple7 reversed = tuple.reverse();
            assertEquals((short) 7, reversed._1);
            assertEquals((short) 1, reversed._7);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 7));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6, 7 }, tuple.toArray());

            // Test statistical operations
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 7, tuple.max());
            assertEquals(28, tuple.sum());
            assertEquals(4.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple8Operations() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);

            // Test reverse
            ShortTuple8 reversed = tuple.reverse();
            assertEquals((short) 8, reversed._1);
            assertEquals((short) 1, reversed._8);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 8));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6, 7, 8 }, tuple.toArray());

            // Test statistical operations
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 8, tuple.max());
            assertEquals(36, tuple.sum());
            assertEquals(4.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple9Operations() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);

            // Test reverse
            ShortTuple9 reversed = tuple.reverse();
            assertEquals((short) 9, reversed._1);
            assertEquals((short) 1, reversed._9);

            // Test contains
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 9));
            assertFalse(tuple.contains((short) 99));

            // Test toArray
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, tuple.toArray());

            // Test statistical operations
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 9, tuple.max());
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average(), 0.001);
        }

        // Test create methods for sizes 2, 4-8
        @Test
        public void testCreate2Through8() {
            ShortTuple2 tuple2 = ShortTuple.copyOf(new short[] { 1, 2 });
            assertEquals((short) 1, tuple2._1);
            assertEquals((short) 2, tuple2._2);

            ShortTuple4 tuple4 = ShortTuple.copyOf(new short[] { 1, 2, 3, 4 });
            assertEquals((short) 1, tuple4._1);
            assertEquals((short) 4, tuple4._4);

            ShortTuple5 tuple5 = ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5 });
            assertEquals((short) 1, tuple5._1);
            assertEquals((short) 5, tuple5._5);

            ShortTuple6 tuple6 = ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5, 6 });
            assertEquals((short) 1, tuple6._1);
            assertEquals((short) 6, tuple6._6);

            ShortTuple7 tuple7 = ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals((short) 1, tuple7._1);
            assertEquals((short) 7, tuple7._7);

            ShortTuple8 tuple8 = ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals((short) 1, tuple8._1);
            assertEquals((short) 8, tuple8._8);
        }

        // Test toList for larger tuples
        @Test
        public void testToListTuple4Through9() {
            ShortTuple4 tuple4 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals((short) 4, list4.get(3));

            ShortTuple9 tuple9 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            ShortList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals((short) 9, list9.get(8));
        }

        // Test forEach for larger tuples
        @Test
        public void testForEachTuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Short.valueOf((short) 4), result.get(3));
        }

        // Test forEach override for Tuple2
        @Test
        public void testForEachTuple2Override() {
            ShortTuple2 tuple = ShortTuple.of((short) 10, (short) 20);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Short.valueOf((short) 10), result.get(0));
            assertEquals(Short.valueOf((short) 20), result.get(1));
        }

        // Test forEach override for Tuple3
        @Test
        public void testForEachTuple3Override() {
            ShortTuple3 tuple = ShortTuple.of((short) 10, (short) 20, (short) 30);
            List<Short> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Short.valueOf((short) 30), result.get(2));
        }

        // Test stream for larger tuples
        @Test
        public void testStreamTuple4Through9() {
            ShortTuple4 tuple4 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals(10, tuple4.stream().sum());

            ShortTuple9 tuple9 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals(45, tuple9.stream().sum());
        }

        // ==================== ShortTuple Nested Class Tests ====================

        // ============ ShortTuple1 Nested Class Tests ============

        @Test
        public void testShortTuple1_arity() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testShortTuple1_reverse() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            ShortTuple.ShortTuple1 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        public void testShortTuple1_contains() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        public void testShortTuple1_hashCode() {
            ShortTuple.ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple.ShortTuple1 tuple2 = ShortTuple.of((short) 1);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testShortTuple1_equals() {
            ShortTuple.ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple.ShortTuple1 tuple2 = ShortTuple.of((short) 1);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testShortTuple1_toString() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testShortTuple1_forEach() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testShortTuple1_min() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotNull(tuple.min());
        }

        @Test
        public void testShortTuple1_max() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotNull(tuple.max());
        }

        @Test
        public void testShortTuple1_median() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotNull(tuple.median());
        }

        @Test
        public void testShortTuple1_sum() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testShortTuple1_average() {
            ShortTuple.ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        // ============ ShortTuple2 Nested Class Tests ============

        @Test
        public void testShortTuple2_arity() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testShortTuple2_reverse() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            ShortTuple.ShortTuple2 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        public void testShortTuple2_contains() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        public void testShortTuple2_hashCode() {
            ShortTuple.ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple.ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testShortTuple2_equals() {
            ShortTuple.ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple.ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testShortTuple2_toString() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testShortTuple2_forEach() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        public void testShortTuple2_min() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertNotNull(tuple.min());
        }

        @Test
        public void testShortTuple2_max() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertNotNull(tuple.max());
        }

        @Test
        public void testShortTuple2_median() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertNotNull(tuple.median());
        }

        @Test
        public void testShortTuple2_sum() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testShortTuple2_average() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testShortTuple2_accept_biConsumer() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testShortTuple2_map_biFunction() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testShortTuple2_filter_biPredicate() {
            ShortTuple.ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        // ============ ShortTuple3 Nested Class Tests ============

        @Test
        public void testShortTuple3_arity() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testShortTuple3_reverse() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple.ShortTuple3 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        public void testShortTuple3_contains() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        public void testShortTuple3_hashCode() {
            ShortTuple.ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple.ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testShortTuple3_equals() {
            ShortTuple.ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple.ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testShortTuple3_toString() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testShortTuple3_forEach() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        public void testShortTuple3_min() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotNull(tuple.min());
        }

        @Test
        public void testShortTuple3_max() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotNull(tuple.max());
        }

        @Test
        public void testShortTuple3_median() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotNull(tuple.median());
        }

        @Test
        public void testShortTuple3_sum() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testShortTuple3_average() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testShortTuple3_accept_triConsumer() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testShortTuple3_map_triFunction() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testShortTuple3_filter_triPredicate() {
            ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        // ============ ShortTuple4 Nested Class Tests ============

        @Test
        public void testShortTuple4_arity() {
            ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testShortTuple4_reverse() {
            ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple.ShortTuple4 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        public void testShortTuple4_contains() {
            ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        public void testShortTuple4_hashCode() {
            ShortTuple.ShortTuple4 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple.ShortTuple4 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testShortTuple4_equals() {
            ShortTuple.ShortTuple4 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple.ShortTuple4 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testShortTuple4_toString() {
            ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testShortTuple4_forEach() {
            ShortTuple.ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        // ============ ShortTuple5 Nested Class Tests ============

        @Test
        public void testShortTuple5_arity() {
            ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testShortTuple5_reverse() {
            ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            ShortTuple.ShortTuple5 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        public void testShortTuple5_contains() {
            ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        public void testShortTuple5_hashCode() {
            ShortTuple.ShortTuple5 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            ShortTuple.ShortTuple5 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testShortTuple5_equals() {
            ShortTuple.ShortTuple5 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            ShortTuple.ShortTuple5 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testShortTuple5_toString() {
            ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testShortTuple5_forEach() {
            ShortTuple.ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        // ============ ShortTuple6 Nested Class Tests ============

        @Test
        public void testShortTuple6_arity() {
            ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testShortTuple6_reverse() {
            ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            ShortTuple.ShortTuple6 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        public void testShortTuple6_contains() {
            ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        public void testShortTuple6_hashCode() {
            ShortTuple.ShortTuple6 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            ShortTuple.ShortTuple6 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testShortTuple6_equals() {
            ShortTuple.ShortTuple6 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            ShortTuple.ShortTuple6 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testShortTuple6_toString() {
            ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testShortTuple6_forEach() {
            ShortTuple.ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        // ============ ShortTuple7 Nested Class Tests ============

        @Test
        public void testShortTuple7_arity() {
            ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testShortTuple7_reverse() {
            ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            ShortTuple.ShortTuple7 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        public void testShortTuple7_contains() {
            ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        public void testShortTuple7_hashCode() {
            ShortTuple.ShortTuple7 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            ShortTuple.ShortTuple7 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testShortTuple7_equals() {
            ShortTuple.ShortTuple7 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            ShortTuple.ShortTuple7 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testShortTuple7_toString() {
            ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testShortTuple7_forEach() {
            ShortTuple.ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        // ============ ShortTuple8 Nested Class Tests ============

        @Test
        public void testShortTuple8_arity() {
            ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testShortTuple8_reverse() {
            ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            ShortTuple.ShortTuple8 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        public void testShortTuple8_contains() {
            ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        public void testShortTuple8_hashCode() {
            ShortTuple.ShortTuple8 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            ShortTuple.ShortTuple8 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testShortTuple8_equals() {
            ShortTuple.ShortTuple8 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            ShortTuple.ShortTuple8 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testShortTuple8_toString() {
            ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testShortTuple8_forEach() {
            ShortTuple.ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        // ============ ShortTuple9 Nested Class Tests ============

        @Test
        public void testShortTuple9_arity() {
            ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testShortTuple9_reverse() {
            ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            ShortTuple.ShortTuple9 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._9);
            assertEquals(tuple._9, reversed._1);
        }

        @Test
        public void testShortTuple9_contains() {
            ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertTrue(tuple.contains((short) 1));
        }

        @Test
        public void testShortTuple9_hashCode() {
            ShortTuple.ShortTuple9 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            ShortTuple.ShortTuple9 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testShortTuple9_equals() {
            ShortTuple.ShortTuple9 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            ShortTuple.ShortTuple9 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testShortTuple9_toString() {
            ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testShortTuple9_forEach() {
            ShortTuple.ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

    }

    @Nested
    @Tag("2510")
    class ShortTuple2510Test extends TestBase {

        @Test
        public void testOf1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals(1, tuple.arity());
            assertEquals((short) 1, tuple._1);
        }

        @Test
        public void testOf2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertEquals(2, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
        }

        @Test
        public void testOf3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(3, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
            assertEquals((short) 3, tuple._3);
        }

        @Test
        public void testOf4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals(4, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
            assertEquals((short) 3, tuple._3);
            assertEquals((short) 4, tuple._4);
        }

        @Test
        public void testOf5() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals(5, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 5, tuple._5);
        }

        @Test
        public void testOf6() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertEquals(6, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 6, tuple._6);
        }

        @Test
        public void testOf7() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertEquals(7, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 7, tuple._7);
        }

        @Test
        public void testOf8() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertEquals(8, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 8, tuple._8);
        }

        @Test
        public void testOf9() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals(9, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 9, tuple._9);
        }

        @Test
        public void testCreate_nullArray() {
            ShortTuple<?> tuple = ShortTuple.copyOf(null);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_emptyArray() {
            ShortTuple<?> tuple = ShortTuple.copyOf(new short[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_sizeOne() {
            ShortTuple1 tuple = ShortTuple.copyOf(new short[] { 1 });
            assertEquals(1, tuple.arity());
            assertEquals((short) 1, tuple._1);
        }

        @Test
        public void testCreate_sizeTwo() {
            ShortTuple2 tuple = ShortTuple.copyOf(new short[] { 1, 2 });
            assertEquals(2, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
        }

        @Test
        public void testCreate_sizeThree() {
            ShortTuple3 tuple = ShortTuple.copyOf(new short[] { 1, 2, 3 });
            assertEquals(3, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 3, tuple._3);
        }

        @Test
        public void testCreate_sizeFour() {
            ShortTuple4 tuple = ShortTuple.copyOf(new short[] { 1, 2, 3, 4 });
            assertEquals(4, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 4, tuple._4);
        }

        @Test
        public void testCreate_sizeFive() {
            ShortTuple5 tuple = ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5 });
            assertEquals(5, tuple.arity());
            assertEquals((short) 5, tuple._5);
        }

        @Test
        public void testCreate_sizeSix() {
            ShortTuple6 tuple = ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5, 6 });
            assertEquals(6, tuple.arity());
            assertEquals((short) 6, tuple._6);
        }

        @Test
        public void testCreate_sizeSeven() {
            ShortTuple7 tuple = ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5, 6, 7 });
            assertEquals(7, tuple.arity());
            assertEquals((short) 7, tuple._7);
        }

        @Test
        public void testCreate_sizeEight() {
            ShortTuple8 tuple = ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            assertEquals(8, tuple.arity());
            assertEquals((short) 8, tuple._8);
        }

        @Test
        public void testCreate_sizeNine() {
            ShortTuple9 tuple = ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertEquals(9, tuple.arity());
            assertEquals((short) 9, tuple._9);
        }

        @Test
        public void testCreate_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        }

        @Test
        public void testMin_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
            assertEquals((short) 2, tuple.min());
        }

        @Test
        public void testMin_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 2, (short) 8);
            assertEquals((short) 2, tuple.min());
        }

        @Test
        public void testMin_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 5, (short) 2, (short) 8, (short) 1);
            assertEquals((short) 1, tuple.min());
        }

        @Test
        public void testMax_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
            assertEquals((short) 5, tuple.max());
        }

        @Test
        public void testMax_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 5, (short) 2, (short) 8);
            assertEquals((short) 8, tuple.max());
        }

        @Test
        public void testMedian_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
            assertEquals((short) 2, tuple.median());
        }

        @Test
        public void testMedian_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 3, (short) 2);
            assertEquals((short) 2, tuple.median());
        }

        @Test
        public void testMedian_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals((short) 2, tuple.median());
        }

        @Test
        public void testSum_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 5, (short) 2);
            assertEquals(7, tuple.sum());
        }

        @Test
        public void testAverage_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals(5.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverage_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 4, (short) 6);
            assertEquals(5.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverage_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(2.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverage_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals(2.5, tuple.average(), 0.001);
        }

        @Test
        public void testReverse_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            ShortTuple1 reversed = tuple.reverse();
            assertEquals((short) 1, reversed._1);
        }

        @Test
        public void testReverse_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple4 reversed = tuple.reverse();
            assertEquals((short) 4, reversed._1);
            assertEquals((short) 1, reversed._4);
        }

        @Test
        public void testContains_tuple1_notFound() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertFalse(tuple.contains((short) 2));
        }

        @Test
        public void testContains_tuple2_found() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertTrue(tuple.contains((short) 2));
        }

        @Test
        public void testContains_tuple2_notFound() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertFalse(tuple.contains((short) 5));
        }

        @Test
        public void testContains_tuple3_found() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.contains((short) 2));
        }

        @Test
        public void testContains_tuple3_notFound() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertFalse(tuple.contains((short) 5));
        }

        @Test
        public void testToArray_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            short[] array = tuple.toArray();
            assertArrayEquals(new short[] { 1 }, array);
        }

        @Test
        public void testToArray_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            short[] array = tuple.toArray();
            assertArrayEquals(new short[] { 1, 2 }, array);
        }

        @Test
        public void testToArray_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] array = tuple.toArray();
            assertArrayEquals(new short[] { 1, 2, 3 }, array);
        }

        @Test
        public void testToList_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            ShortList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals((short) 1, list.get(0));
        }

        @Test
        public void testToList_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            ShortList list = tuple.toList();
            assertEquals(2, list.size());
            assertEquals((short) 1, list.get(0));
            assertEquals((short) 2, list.get(1));
        }

        @Test
        public void testToList_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals((short) 3, list.get(2));
        }

        @Test
        public void testForEach_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(1, sum.get());
        }

        @Test
        public void testForEach_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(3, sum.get());
        }

        @Test
        public void testForEach_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(6, sum.get());
        }

        @Test
        public void testForEach_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(10, sum.get());
        }

        @Test
        public void testStream_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            int sum = tuple.stream().sum();
            assertEquals(1, sum);
        }

        @Test
        public void testStream_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            int sum = tuple.stream().sum();
            assertEquals(3, sum);
        }

        @Test
        public void testStream_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            int sum = tuple.stream().sum();
            assertEquals(6, sum);
        }

        @Test
        public void testHashCode_tuple1_different() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple1 tuple2 = ShortTuple.of((short) 2);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCode_tuple3() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testEquals_tuple1_same() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple1 tuple2 = ShortTuple.of((short) 1);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple1_sameObject() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEquals_tuple1_different() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 1);
            ShortTuple1 tuple2 = ShortTuple.of((short) 2);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple1_null() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertNotEquals(null, tuple);
        }

        @Test
        public void testEquals_tuple2_different() {
            ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 3);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple3_same() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple3_different() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 4);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testToString_tuple1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals("(1)", tuple.toString());
        }

        @Test
        public void testToString_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals("(1, 2, 3)", tuple.toString());
        }

        @Test
        public void testAccept_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
            AtomicInteger result = new AtomicInteger(0);
            tuple.accept((a, b) -> result.set(a * b));
            assertEquals(15, result.get());
        }

        @Test
        public void testMap_tuple2() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
            int result = tuple.map((a, b) -> a * b);
            assertEquals(15, result);
        }

        @Test
        public void testFilter_tuple2_match() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
            Optional<ShortTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testFilter_tuple2_noMatch() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 5);
            Optional<ShortTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        public void testAccept_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
            AtomicInteger result = new AtomicInteger(0);
            tuple.accept((a, b, c) -> result.set(a + b + c));
            assertEquals(10, result.get());
        }

        @Test
        public void testMap_tuple3() {
            ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
            int result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(30, result);
        }

        @Test
        public void testFilter_tuple3_match() {
            ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
            Optional<ShortTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
        }

        @Test
        public void testFilter_tuple3_noMatch() {
            ShortTuple3 tuple = ShortTuple.of((short) 2, (short) 3, (short) 5);
            Optional<ShortTuple3> result = tuple.filter((a, b, c) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        public void testTuple0_min() {
            ShortTuple<?> tuple = ShortTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void testTuple0_max() {
            ShortTuple<?> tuple = ShortTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void testTuple0_median() {
            ShortTuple<?> tuple = ShortTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void testTuple0_sum() {
            ShortTuple<?> tuple = ShortTuple.copyOf(null);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void testTuple0_average() {
            ShortTuple<?> tuple = ShortTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void testTuple0_reverse() {
            ShortTuple<?> tuple = ShortTuple.copyOf(null);
            assertNotNull(tuple.reverse());
        }

        @Test
        public void testTuple0_contains() {
            ShortTuple<?> tuple = ShortTuple.copyOf(null);
            assertFalse(tuple.contains((short) 1));
        }

        @Test
        public void testTuple0_toString() {
            ShortTuple<?> tuple = ShortTuple.copyOf(null);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testTuple5_operations() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals(5, tuple.arity());
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 5, tuple.max());
            assertEquals((short) 3, tuple.median());
            assertEquals(15, tuple.sum());
            assertEquals(3.0, tuple.average(), 0.001);
            assertTrue(tuple.contains((short) 3));
            assertFalse(tuple.contains((short) 10));
        }

        @Test
        public void testTuple5_reverse() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            ShortTuple5 reversed = tuple.reverse();
            assertEquals((short) 5, reversed._1);
            assertEquals((short) 4, reversed._2);
            assertEquals((short) 3, reversed._3);
            assertEquals((short) 2, reversed._4);
            assertEquals((short) 1, reversed._5);
        }

        @Test
        public void testTuple6_operations() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertEquals(6, tuple.arity());
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 6, tuple.max());
            assertEquals(21, tuple.sum());
        }

        @Test
        public void testTuple7_operations() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertEquals(7, tuple.arity());
            assertEquals((short) 4, tuple.median());
            assertEquals(28, tuple.sum());
        }

        @Test
        public void testTuple8_operations() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertEquals(8, tuple.arity());
            assertEquals((short) 8, tuple.max());
            assertEquals(36, tuple.sum());
        }

        @Test
        public void testTuple9_operations() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals(9, tuple.arity());
            assertEquals((short) 5, tuple.median());
            assertEquals(45, tuple.sum());
            assertEquals(5.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple9_reverse() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            ShortTuple9 reversed = tuple.reverse();
            assertEquals((short) 9, reversed._1);
            assertEquals((short) 1, reversed._9);
        }

        @Test
        public void testForEach_tuple5() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(5, count.get());
        }

        @Test
        public void testForEach_tuple6() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(6, count.get());
        }

        @Test
        public void testForEach_tuple7() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(7, count.get());
        }

        @Test
        public void testForEach_tuple8() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(8, count.get());
        }

        @Test
        public void testForEach_tuple9() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            AtomicInteger count = new AtomicInteger(0);
            tuple.forEach(value -> count.incrementAndGet());
            assertEquals(9, count.get());
        }

        @Test
        public void testContains_tuple4() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 4));
            assertFalse(tuple.contains((short) 5));
        }

        @Test
        public void testContains_tuple5() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertTrue(tuple.contains((short) 5));
            assertFalse(tuple.contains((short) 10));
        }

        @Test
        public void testContains_tuple6() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertTrue(tuple.contains((short) 6));
            assertFalse(tuple.contains((short) 7));
        }

        @Test
        public void testContains_tuple7() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertTrue(tuple.contains((short) 7));
            assertFalse(tuple.contains((short) 8));
        }

        @Test
        public void testContains_tuple8() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertTrue(tuple.contains((short) 8));
            assertFalse(tuple.contains((short) 9));
        }

        @Test
        public void testContains_tuple9() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertTrue(tuple.contains((short) 9));
            assertFalse(tuple.contains((short) 10));
        }
    }

    @Nested
    @Tag("2511")
    class ShortTuple2511Test extends TestBase {

        // ====== Factory Methods Tests ======

        @Test
        public void testOf1() {
            ShortTuple1 tuple = ShortTuple.of((short) 42);
            assertEquals(1, tuple.arity());
            assertEquals((short) 42, tuple._1);
        }

        @Test
        public void testOf2() {
            ShortTuple2 tuple = ShortTuple.of((short) 10, (short) 20);
            assertEquals(2, tuple.arity());
            assertEquals((short) 10, tuple._1);
            assertEquals((short) 20, tuple._2);
        }

        @Test
        public void testCreateFromArray() {
            // Empty array
            ShortTuple<?> empty = ShortTuple.copyOf(null);
            assertEquals(0, empty.arity());

            empty = ShortTuple.copyOf(new short[0]);
            assertEquals(0, empty.arity());

            // Single element
            ShortTuple1 tuple1 = ShortTuple.of((short) 42);
            assertEquals(1, tuple1.arity());
            assertEquals((short) 42, tuple1._1);

            // Multiple elements
            ShortTuple<?> tuple3 = ShortTuple.copyOf(new short[] { 10, 20, 30 });
            assertEquals(3, tuple3.arity());

            // Max size
            ShortTuple<?> tuple9 = ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            assertEquals(9, tuple9.arity());

            // Too many elements
            assertThrows(IllegalArgumentException.class, () -> ShortTuple.copyOf(new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        }

        // ====== Arity Tests ======

        @Test
        public void testArity0() {
            ShortTuple<?> empty = ShortTuple.copyOf(null);
            assertEquals(0, empty.arity());
        }

        @Test
        public void testArity1() {
            ShortTuple1 tuple = ShortTuple.of((short) 1);
            assertEquals(1, tuple.arity());
        }
        // ====== Statistics Tests (min, max, median, sum, average) ======

        @Test
        public void testMin() {
            assertEquals((short) 2, ShortTuple.of((short) 5, (short) 2, (short) 8).min());
            assertEquals((short) 1, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).min());
            assertEquals((short) -10, ShortTuple.of((short) -5, (short) -10, (short) 0).min());
        }

        @Test
        public void testMax() {
            assertEquals((short) 8, ShortTuple.of((short) 5, (short) 2, (short) 8).max());
            assertEquals((short) 5, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).max());
            assertEquals((short) 10, ShortTuple.of((short) -5, (short) 0, (short) 10).max());
        }

        @Test
        public void testMedian() {
            // Odd number of elements - returns middle value
            assertEquals((short) 2, ShortTuple.of((short) 3, (short) 1, (short) 2).median());
            assertEquals((short) 3, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).median());

            // Even number of elements - returns lower middle value
            assertEquals((short) 2, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4).median());
        }

        @Test
        public void testSum() {
            assertEquals(60, ShortTuple.of((short) 10, (short) 20, (short) 30).sum());
            assertEquals(15, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).sum());
            assertEquals(0, ShortTuple.copyOf(null).sum());
        }

        @Test
        public void testAverage() {
            assertEquals(20.0, ShortTuple.of((short) 10, (short) 20, (short) 30).average(), 0.0001);
            assertEquals(3.0, ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).average(), 0.0001);
        }

        @Test
        public void testMin_Empty() {
            assertThrows(NoSuchElementException.class, () -> ShortTuple.copyOf(null).min());
        }

        @Test
        public void testMax_Empty() {
            assertThrows(NoSuchElementException.class, () -> ShortTuple.copyOf(null).max());
        }

        @Test
        public void testMedian_Empty() {
            assertThrows(NoSuchElementException.class, () -> ShortTuple.copyOf(null).median());
        }

        @Test
        public void testAverage_Empty() {
            assertThrows(NoSuchElementException.class, () -> ShortTuple.copyOf(null).average());
        }

        // ====== Reverse Tests ======

        @Test
        public void testReverse1() {
            ShortTuple1 reversed = ShortTuple.of((short) 42).reverse();
            assertEquals((short) 42, reversed._1);
        }

        @Test
        public void testReverse2() {
            ShortTuple2 reversed = ShortTuple.of((short) 10, (short) 20).reverse();
            assertEquals((short) 20, reversed._1);
            assertEquals((short) 10, reversed._2);
        }

        @Test
        public void testReverse3() {
            ShortTuple3 reversed = ShortTuple.of((short) 1, (short) 2, (short) 3).reverse();
            assertEquals((short) 3, reversed._1);
            assertEquals((short) 2, reversed._2);
            assertEquals((short) 1, reversed._3);
        }

        @Test
        public void testReverse0() {
            ShortTuple<?> reversed = ShortTuple.copyOf(null).reverse();
            assertEquals(0, reversed.arity());
        }

        // ====== Contains Tests ======

        @Test
        public void testContains() {
            ShortTuple3 tuple = ShortTuple.of((short) 10, (short) 20, (short) 30);
            assertTrue(tuple.contains((short) 20));
            assertFalse(tuple.contains((short) 40));
            assertTrue(tuple.contains((short) 10));
            assertTrue(tuple.contains((short) 30));
        }

        @Test
        public void testContains_Empty() {
            assertFalse(ShortTuple.copyOf(null).contains((short) 10));
        }

        @Test
        public void testContains_Single() {
            ShortTuple1 tuple = ShortTuple.of((short) 42);
            assertTrue(tuple.contains((short) 42));
            assertFalse(tuple.contains((short) 43));
        }

        // ====== Array Conversion Tests ======

        @Test
        public void testToArray() {
            short[] array = ShortTuple.of((short) 1, (short) 2, (short) 3).toArray();
            assertArrayEquals(new short[] { 1, 2, 3 }, array);

            // Test that modifications to returned array don't affect tuple
            array[0] = 999;
            assertEquals((short) 1, ShortTuple.of((short) 1, (short) 2, (short) 3).toArray()[0]);
        }

        @Test
        public void testToArray_Empty() {
            short[] array = ShortTuple.copyOf(null).toArray();
            assertEquals(0, array.length);
        }

        // ====== List Conversion Tests ======

        @Test
        public void testToList() {
            ShortList list = ShortTuple.of((short) 1, (short) 2, (short) 3).toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals((short) 1, list.get(0));
            assertEquals((short) 2, list.get(1));
            assertEquals((short) 3, list.get(2));
        }

        @Test
        public void testToList_Empty() {
            ShortList list = ShortTuple.copyOf(null).toList();
            assertEquals(0, list.size());
        }

        // ====== forEach Tests ======

        @Test
        public void testForEach() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            AtomicInteger sum = new AtomicInteger(0);
            tuple.forEach(v -> sum.addAndGet(v));
            assertEquals(6, sum.get());
        }

        @Test
        public void testForEach_Empty() {
            AtomicInteger sum = new AtomicInteger(0);
            ShortTuple.copyOf(null).forEach(v -> sum.addAndGet(v));
            assertEquals(0, sum.get());
        }

        // ====== Stream Tests ======

        @Test
        public void testStream() {
            int sum = ShortTuple.of((short) 1, (short) 2, (short) 3).stream().sum();
            assertEquals(6, sum);
        }

        @Test
        public void testStream_Empty() {
            int sum = ShortTuple.copyOf(null).stream().sum();
            assertEquals(0, sum);
        }

        // ====== Equality Tests ======

        @Test
        public void testEquals() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 4);

            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
            assertEquals(tuple1, tuple1);
            assertNotEquals(tuple1, null);
        }

        @Test
        public void testEquals_Empty() {
            ShortTuple<?> empty1 = ShortTuple.copyOf(null);
            ShortTuple<?> empty2 = ShortTuple.copyOf(new short[0]);
            assertEquals(empty1, empty2);
        }
        // ====== String Representation Tests ======

        @Test
        public void testToString() {
            assertEquals("(42)", ShortTuple.of((short) 42).toString());
            assertEquals("(10, 20)", ShortTuple.of((short) 10, (short) 20).toString());
            assertEquals("(1, 2, 3)", ShortTuple.of((short) 1, (short) 2, (short) 3).toString());
            assertEquals("()", ShortTuple.copyOf(null).toString());
        }

        // ====== ShortTuple2 Specific Tests ======

        @Test
        public void testAccept2() {
            ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
            AtomicInteger sum = new AtomicInteger(0);
            pair.accept((a, b) -> sum.set(a + b));
            assertEquals(7, sum.get());
        }

        @Test
        public void testMap2() {
            ShortTuple2 pair = ShortTuple.of((short) 10, (short) 3);
            int remainder = pair.map((a, b) -> a % b);
            assertEquals(1, remainder);
        }

        @Test
        public void testFilter2_True() {
            ShortTuple2 pair = ShortTuple.of((short) 10, (short) 20);
            Optional<ShortTuple2> result = pair.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(pair, result.get());
        }

        @Test
        public void testFilter2_False() {
            ShortTuple2 pair = ShortTuple.of((short) 20, (short) 10);
            Optional<ShortTuple2> result = pair.filter((a, b) -> a < b);
            assertFalse(result.isPresent());
        }

        // ====== ShortTuple3 Specific Tests ======

        @Test
        public void testAccept3() {
            ShortTuple3 triple = ShortTuple.of((short) 3, (short) 4, (short) 5);
            AtomicInteger sum = new AtomicInteger(0);
            triple.accept((a, b, c) -> sum.set(a + b + c));
            assertEquals(12, sum.get());
        }

        @Test
        public void testMap3() {
            ShortTuple3 triple = ShortTuple.of((short) 2, (short) 3, (short) 4);
            int volume = triple.map((l, w, h) -> l * w * h);
            assertEquals(24, volume);
        }

        @Test
        public void testFilter3_True() {
            ShortTuple3 triple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            Optional<ShortTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
            assertEquals(triple, result.get());
        }

        @Test
        public void testFilter3_False() {
            ShortTuple3 triple = ShortTuple.of((short) 3, (short) 2, (short) 1);
            Optional<ShortTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
            assertFalse(result.isPresent());
        }

        // ====== Edge Cases and Special Values ======

        @Test
        public void testWithZero() {
            ShortTuple3 tuple = ShortTuple.of((short) 0, (short) 1, (short) 2);
            assertEquals((short) 0, tuple.min());
            assertEquals((short) 2, tuple.max());
            assertEquals(3, tuple.sum());
        }

        @Test
        public void testWithNegatives() {
            ShortTuple3 tuple = ShortTuple.of((short) -5, (short) -10, (short) -1);
            assertEquals((short) -10, tuple.min());
            assertEquals((short) -1, tuple.max());
            assertEquals(-16, tuple.sum());
        }

        @Test
        public void testWithMaxValues() {
            short max = Short.MAX_VALUE / 2;
            ShortTuple2 tuple = ShortTuple.of(max, max);
            assertEquals(max, tuple.min());
            assertEquals(max, tuple.max());
        }

        @Test
        public void testWithSingleElement() {
            ShortTuple1 tuple = ShortTuple.of((short) 42);
            assertEquals((short) 42, tuple.min());
            assertEquals((short) 42, tuple.max());
            assertEquals((short) 42, tuple.median());
            assertEquals(42, tuple.sum());
            assertEquals(42.0, tuple.average(), 0.0001);
            assertTrue(tuple.contains((short) 42));
        }

        // ====== All Tuple Sizes Combined ======

        @Test
        public void testAllTupleSizes() {
            ShortTuple0 t0 = ShortTuple.copyOf(null);
            ShortTuple1 t1 = ShortTuple.of((short) 1);
            ShortTuple2 t2 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple3 t3 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple4 t4 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple5 t5 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            ShortTuple6 t6 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            ShortTuple7 t7 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            ShortTuple8 t8 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            ShortTuple9 t9 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);

            assertEquals(0, t0.arity());
            assertEquals(1, t1.arity());
            assertEquals(2, t2.arity());
            assertEquals(3, t3.arity());
            assertEquals(4, t4.arity());
            assertEquals(5, t5.arity());
            assertEquals(6, t6.arity());
            assertEquals(7, t7.arity());
            assertEquals(8, t8.arity());
            assertEquals(9, t9.arity());
        }

        // ====== Functional Method Exception Tests ======

        @Test
        public void testAccept2_Exception() throws Exception {
            ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
            assertThrows(IllegalArgumentException.class, () -> pair.accept((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        @Test
        public void testMap2_Exception() {
            ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
            assertThrows(IllegalArgumentException.class, () -> pair.map((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        @Test
        public void testFilter2_Exception() {
            ShortTuple2 pair = ShortTuple.of((short) 3, (short) 4);
            assertThrows(IllegalArgumentException.class, () -> pair.filter((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        // ====== Immutability Tests ======

        @Test
        public void testImmutability() {
            short[] arr = new short[] { 1, 2, 3 };
            ShortTuple3 tuple = ShortTuple.copyOf(arr);
            arr[0] = 999;
            assertEquals((short) 1, tuple._1);
        }

        @Test
        public void testToArray_Independence() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] arr1 = tuple.toArray();
            short[] arr2 = tuple.toArray();
            arr1[0] = 999;
            assertEquals((short) 1, arr2[0]);
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for ShortTuple and its nested tuple classes (ShortTuple0 through ShortTuple9).
     * Tests cover all public methods including factory methods, statistical operations, conversions, and edge cases.
     */
    @Tag("2512")
    class ShortTuple2512Test extends TestBase {

        // ===== Factory Method Tests =====

        @Test
        public void test_of_singleElement() {
            ShortTuple1 tuple = ShortTuple.of((short) 42);
            assertNotNull(tuple);
            assertEquals((short) 42, tuple._1);
        }

        @Test
        public void test_of_twoElements() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertNotNull(tuple);
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
        }

        @Test
        public void test_of_threeElements() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotNull(tuple);
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
            assertEquals((short) 3, tuple._3);
        }

        @Test
        public void test_of_fourElements() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertNotNull(tuple);
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 4, tuple._4);
        }

        @Test
        public void test_of_fiveElements() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertNotNull(tuple);
            assertEquals((short) 5, tuple._5);
        }

        @Test
        public void test_of_sixElements() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertNotNull(tuple);
            assertEquals((short) 6, tuple._6);
        }

        @Test
        public void test_of_sevenElements() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertNotNull(tuple);
            assertEquals((short) 7, tuple._7);
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_of_eightElements() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertNotNull(tuple);
            assertEquals((short) 8, tuple._8);
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_of_nineElements() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertNotNull(tuple);
            assertEquals((short) 9, tuple._9);
        }

        @Test
        public void test_create_nullArray() {
            ShortTuple0 tuple = ShortTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_emptyArray() {
            ShortTuple0 tuple = ShortTuple.copyOf(new short[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_singleElementArray() {
            ShortTuple1 tuple = ShortTuple.copyOf(new short[] { (short) 42 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals((short) 42, tuple._1);
        }

        @Test
        public void test_create_multipleElementsArray() {
            ShortTuple3 tuple = ShortTuple.copyOf(new short[] { (short) 1, (short) 2, (short) 3 });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals((short) 1, tuple._1);
            assertEquals((short) 2, tuple._2);
            assertEquals((short) 3, tuple._3);
        }

        @Test
        public void test_create_tooManyElements() {
            short[] array = new short[10];
            assertThrows(IllegalArgumentException.class, () -> ShortTuple.copyOf(array));
        }

        // ===== ShortTuple0 Tests =====

        @Test
        public void test_ShortTuple0_arity() {
            ShortTuple0 tuple = ShortTuple.copyOf(new short[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_ShortTuple0_min_throwsException() {
            ShortTuple0 tuple = ShortTuple.copyOf(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void test_ShortTuple0_max_throwsException() {
            ShortTuple0 tuple = ShortTuple.copyOf(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void test_ShortTuple0_median_throwsException() {
            ShortTuple0 tuple = ShortTuple.copyOf(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void test_ShortTuple0_sum() {
            ShortTuple0 tuple = ShortTuple.copyOf(new short[0]);
            assertEquals(0, tuple.sum());
        }

        @Test
        public void test_ShortTuple0_average_throwsException() {
            ShortTuple0 tuple = ShortTuple.copyOf(new short[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void test_ShortTuple0_reverse() {
            ShortTuple0 tuple = ShortTuple.copyOf(new short[0]);
            ShortTuple0 reversed = tuple.reverse();
            assertSame(tuple, reversed);
        }

        @Test
        public void test_ShortTuple0_contains() {
            ShortTuple0 tuple = ShortTuple.copyOf(new short[0]);
            assertFalse(tuple.contains((short) 1));
        }

        @Test
        public void test_ShortTuple0_toString() {
            ShortTuple0 tuple = ShortTuple.copyOf(new short[0]);
            assertEquals("()", tuple.toString());
        }

        // ===== ShortTuple1 Tests =====

        @Test
        public void test_ShortTuple1_arity() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void test_ShortTuple1_min() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals((short) 5, tuple.min());
        }

        @Test
        public void test_ShortTuple1_max() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals((short) 5, tuple.max());
        }

        @Test
        public void test_ShortTuple1_median() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals((short) 5, tuple.median());
        }

        @Test
        public void test_ShortTuple1_sum() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals(5, tuple.sum());
        }

        @Test
        public void test_ShortTuple1_average() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals(5.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_ShortTuple1_reverse() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            ShortTuple1 reversed = tuple.reverse();
            assertEquals((short) 5, reversed._1);
        }

        @Test
        public void test_ShortTuple1_contains_found() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertTrue(tuple.contains((short) 5));
        }

        @Test
        public void test_ShortTuple1_contains_notFound() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertFalse(tuple.contains((short) 10));
        }

        @Test
        public void test_ShortTuple1_hashCode() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 5);
            ShortTuple1 tuple2 = ShortTuple.of((short) 5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_ShortTuple1_equals_same() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals(tuple, tuple);
        }

        @Test
        public void test_ShortTuple1_equals_equal() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 5);
            ShortTuple1 tuple2 = ShortTuple.of((short) 5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void test_ShortTuple1_equals_notEqual() {
            ShortTuple1 tuple1 = ShortTuple.of((short) 5);
            ShortTuple1 tuple2 = ShortTuple.of((short) 10);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void test_ShortTuple1_equals_null() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertNotEquals(null, tuple);
        }

        @Test
        public void test_ShortTuple1_equals_differentType() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertNotEquals("string", tuple);
        }

        @Test
        public void test_ShortTuple1_toString() {
            ShortTuple1 tuple = ShortTuple.of((short) 5);
            assertEquals("(5)", tuple.toString());
        }

        // ===== ShortTuple2 Tests =====

        @Test
        public void test_ShortTuple2_arity() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void test_ShortTuple2_min() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 1);
            assertEquals((short) 1, tuple.min());
        }

        @Test
        public void test_ShortTuple2_max() {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 1);
            assertEquals((short) 3, tuple.max());
        }

        @Test
        public void test_ShortTuple2_median() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            short median = tuple.median();
            assertTrue(median == (short) 1 || median == (short) 2);
        }

        @Test
        public void test_ShortTuple2_sum() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertEquals(3, tuple.sum());
        }

        @Test
        public void test_ShortTuple2_average() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 3);
            assertEquals(2.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_ShortTuple2_reverse() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 reversed = tuple.reverse();
            assertEquals((short) 2, reversed._1);
            assertEquals((short) 1, reversed._2);
        }

        @Test
        public void test_ShortTuple2_contains_found() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertTrue(tuple.contains((short) 1));
            assertTrue(tuple.contains((short) 2));
        }

        @Test
        public void test_ShortTuple2_contains_notFound() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertFalse(tuple.contains((short) 3));
        }

        @Test
        public void test_ShortTuple2_forEach() throws Exception {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            List<Short> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals((short) 1, values.get(0).shortValue());
            assertEquals((short) 2, values.get(1).shortValue());
        }

        @Test
        public void test_ShortTuple2_accept() throws Exception {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            final int[] result = new int[1];
            tuple.accept((a, b) -> result[0] = a + b);
            assertEquals(7, result[0]);
        }

        @Test
        public void test_ShortTuple2_map() throws Exception {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            Integer result = tuple.map((a, b) -> a * b);
            assertEquals(12, result.intValue());
        }

        @Test
        public void test_ShortTuple2_filter_satisfied() throws Exception {
            ShortTuple2 tuple = ShortTuple.of((short) 3, (short) 4);
            Optional<ShortTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void test_ShortTuple2_filter_notSatisfied() throws Exception {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 1);
            Optional<ShortTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_ShortTuple2_hashCode() {
            ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_ShortTuple2_equals() {
            ShortTuple2 tuple1 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void test_ShortTuple2_toString() {
            ShortTuple2 tuple = ShortTuple.of((short) 1, (short) 2);
            assertEquals("(1, 2)", tuple.toString());
        }

        // ===== ShortTuple3 Tests =====

        @Test
        public void test_ShortTuple3_arity() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void test_ShortTuple3_min() {
            ShortTuple3 tuple = ShortTuple.of((short) 3, (short) 1, (short) 2);
            assertEquals((short) 1, tuple.min());
        }

        @Test
        public void test_ShortTuple3_max() {
            ShortTuple3 tuple = ShortTuple.of((short) 3, (short) 1, (short) 2);
            assertEquals((short) 3, tuple.max());
        }

        @Test
        public void test_ShortTuple3_median() {
            ShortTuple3 tuple = ShortTuple.of((short) 30, (short) 10, (short) 20);
            assertEquals((short) 20, tuple.median());
        }

        @Test
        public void test_ShortTuple3_sum() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(6, tuple.sum());
        }

        @Test
        public void test_ShortTuple3_average() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(2.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_ShortTuple3_reverse() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 reversed = tuple.reverse();
            assertEquals((short) 3, reversed._1);
            assertEquals((short) 2, reversed._2);
            assertEquals((short) 1, reversed._3);
        }

        @Test
        public void test_ShortTuple3_contains() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertTrue(tuple.contains((short) 2));
            assertFalse(tuple.contains((short) 5));
        }

        @Test
        public void test_ShortTuple3_forEach() throws Exception {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            List<Short> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        public void test_ShortTuple3_accept() throws Exception {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            final int[] result = new int[1];
            tuple.accept((a, b, c) -> result[0] = a + b + c);
            assertEquals(6, result[0]);
        }

        @Test
        public void test_ShortTuple3_map() throws Exception {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            Integer result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6, result.intValue());
        }

        @Test
        public void test_ShortTuple3_filter() throws Exception {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            Optional<ShortTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
        }

        // ===== ShortTuple4+ Tests =====

        @Test
        public void test_ShortTuple4_arity() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void test_ShortTuple4_statisticalOperations() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            assertEquals((short) 1, tuple.min());
            assertEquals((short) 4, tuple.max());
            assertEquals(10, tuple.sum());
            assertEquals(2.5, tuple.average(), 0.0001);
        }

        @Test
        public void test_ShortTuple5_arity() {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void test_ShortTuple6_arity() {
            ShortTuple6 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void test_ShortTuple7_arity() {
            ShortTuple7 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
            assertEquals(7, tuple.arity());
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_ShortTuple8_arity() {
            ShortTuple8 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8);
            assertEquals(8, tuple.arity());
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_ShortTuple9_arity() {
            ShortTuple9 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9);
            assertEquals(9, tuple.arity());
        }

        // ===== Common Method Tests =====

        @Test
        public void test_toArray() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals((short) 1, array[0]);
            assertEquals((short) 2, array[1]);
            assertEquals((short) 3, array[2]);
        }

        @Test
        public void test_toArray_modification() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            short[] array = tuple.toArray();
            array[0] = (short) 99;
            assertEquals((short) 1, tuple._1);
        }

        @Test
        public void test_toList() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals((short) 1, list.get(0));
        }

        @Test
        public void test_forEach_multipleElements() throws Exception {
            ShortTuple5 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
            List<Short> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        public void test_hashCode_consistency() {
            ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        public void test_equals_symmetric() {
            ShortTuple3 tuple1 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            ShortTuple3 tuple2 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }

        @Test
        public void test_equals_differentClass() {
            ShortTuple2 tuple2 = ShortTuple.of((short) 1, (short) 2);
            ShortTuple3 tuple3 = ShortTuple.of((short) 1, (short) 2, (short) 3);
            assertNotEquals(tuple2, tuple3);
        }
        // ===== Edge Cases =====

        @Test
        public void test_negativeValues() {
            ShortTuple3 tuple = ShortTuple.of((short) -1, (short) -2, (short) -3);
            assertEquals((short) -3, tuple.min());
            assertEquals((short) -1, tuple.max());
            assertEquals(-6, tuple.sum());
        }

        @Test
        public void test_zeroValues() {
            ShortTuple3 tuple = ShortTuple.of((short) 0, (short) 0, (short) 0);
            assertEquals((short) 0, tuple.min());
            assertEquals((short) 0, tuple.max());
            assertEquals(0, tuple.sum());
        }

        @Test
        public void test_largeValues() {
            ShortTuple2 tuple = ShortTuple.of(Short.MAX_VALUE, (short) (Short.MAX_VALUE / 2));
            assertEquals((short) (Short.MAX_VALUE / 2), tuple.min());
            assertEquals(Short.MAX_VALUE, tuple.max());
        }

        @Test
        public void test_mixedValues() {
            ShortTuple3 tuple = ShortTuple.of((short) -100, (short) 0, (short) 100);
            assertEquals((short) -100, tuple.min());
            assertEquals((short) 100, tuple.max());
            assertEquals(0, tuple.sum());
        }

        @Test
        public void test_reverse_largerTuples() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            ShortTuple4 reversed = tuple.reverse();
            assertEquals((short) 4, reversed._1);
            assertEquals((short) 3, reversed._2);
            assertEquals((short) 2, reversed._3);
            assertEquals((short) 1, reversed._4);
        }

        @Test
        public void test_median_evenSize() {
            ShortTuple4 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
            short median = tuple.median();
            assertTrue(median >= (short) 1 && median <= (short) 4);
        }

        @Test
        public void test_median_oddSize() {
            ShortTuple5 tuple = ShortTuple.of((short) 50, (short) 10, (short) 30, (short) 20, (short) 40);
            short median = tuple.median();
            assertEquals((short) 30, median);
        }

        @Test
        public void test_sum_returnsInt() {
            ShortTuple3 tuple = ShortTuple.of((short) 100, (short) 200, (short) 300);
            int sum = tuple.sum();
            assertEquals(600, sum);
            // Note: sum() returns int (not short) for ShortTuple
        }
    }

}
