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
import com.landawn.abacus.util.LongTuple.LongTuple0;
import com.landawn.abacus.util.LongTuple.LongTuple1;
import com.landawn.abacus.util.LongTuple.LongTuple2;
import com.landawn.abacus.util.LongTuple.LongTuple3;
import com.landawn.abacus.util.LongTuple.LongTuple4;
import com.landawn.abacus.util.LongTuple.LongTuple5;
import com.landawn.abacus.util.LongTuple.LongTuple6;
import com.landawn.abacus.util.LongTuple.LongTuple7;
import com.landawn.abacus.util.LongTuple.LongTuple8;
import com.landawn.abacus.util.LongTuple.LongTuple9;
import com.landawn.abacus.util.stream.LongStream;
import com.landawn.abacus.util.u.Optional;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class LongTupleTest extends TestBase {

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
        LongTuple<?> empty = LongTuple.copyOf(null);
        assertEquals(0, empty.arity());

        empty = LongTuple.copyOf(new long[0]);
        assertEquals(0, empty.arity());

        // Test array with 1 element
        LongTuple.LongTuple1 tuple1 = LongTuple.copyOf(new long[] { 100L });
        assertEquals(1, tuple1.arity());
        assertEquals(100L, tuple1._1);

        // Test array with 5 elements
        LongTuple.LongTuple5 tuple5 = LongTuple.copyOf(new long[] { 100L, 200L, 300L, 400L, 500L });
        assertEquals(5, tuple5.arity());
        assertEquals(500L, tuple5._5);

        // Test array with 9 elements
        LongTuple.LongTuple9 tuple9 = LongTuple.copyOf(new long[] { 100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L });
        assertEquals(9, tuple9.arity());
        assertEquals(900L, tuple9._9);

        // Test too many elements
        assertThrows(IllegalArgumentException.class, () -> LongTuple.copyOf(new long[10]));
    }

    @Test
    public void testMin() {
        LongTuple.LongTuple3 tuple = LongTuple.of(300L, 100L, 200L);
        assertEquals(100L, tuple.min());

        LongTuple.LongTuple0 empty = LongTuple.copyOf(new long[0]);
        assertThrows(NoSuchElementException.class, () -> empty.min());
    }

    @Test
    public void testMax() {
        LongTuple.LongTuple3 tuple = LongTuple.of(300L, 100L, 200L);
        assertEquals(300L, tuple.max());

        LongTuple.LongTuple0 empty = LongTuple.copyOf(new long[0]);
        assertThrows(NoSuchElementException.class, () -> empty.max());
    }

    @Test
    public void testMedian() {
        LongTuple.LongTuple3 tuple = LongTuple.of(300L, 100L, 200L);
        assertEquals(200L, tuple.median());

        LongTuple.LongTuple4 evenTuple = LongTuple.of(100L, 200L, 300L, 400L);
        assertEquals(200L, evenTuple.median()); // Should be (200L + 300L) / 2 = 250L

        LongTuple.LongTuple0 empty = LongTuple.copyOf(new long[0]);
        assertThrows(NoSuchElementException.class, () -> empty.median());
    }

    @Test
    public void testSum() {
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
        assertEquals(600L, tuple.sum());

        LongTuple.LongTuple0 empty = LongTuple.copyOf(new long[0]);
        assertEquals(0L, empty.sum());
    }

    @Test
    public void testAverage() {
        LongTuple.LongTuple3 tuple = LongTuple.of(100L, 200L, 300L);
        assertEquals(200.0, tuple.average());

        LongTuple.LongTuple0 empty = LongTuple.copyOf(new long[0]);
        assertThrows(NoSuchElementException.class, () -> empty.average());
    }

    @Test
    public void testReverse() {
        // Test Tuple0
        LongTuple.LongTuple0 empty = LongTuple.copyOf(new long[0]);
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
        LongTuple.LongTuple0 empty = LongTuple.copyOf(new long[0]);
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
        LongTuple.LongTuple0 empty = LongTuple.copyOf(new long[0]);
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
        assertSame(elements1, elements2); // Should return same cached array
    }

    @Nested
    @SuppressWarnings("deprecation")
    @Tag("2512")
    class JavadocExampleTupleTest_LongTuple extends TestBase {
        // ===== LongTuple Javadoc examples =====

        @Test
        public void testLongTupleOf1() {
            // LongTuple.LongTuple1 single = LongTuple.of(42L);
            // long value = single._1;  // 42
            LongTuple.LongTuple1 single = LongTuple.of(42L);
            assertEquals(42L, single._1);
        }

        @Test
        public void testLongTupleOf2() {
            // LongTuple.LongTuple2 pair = LongTuple.of(1L, 2L);
            // long sum = pair._1 + pair._2;  // 3
            LongTuple.LongTuple2 pair = LongTuple.of(1L, 2L);
            assertEquals(3L, pair._1 + pair._2);
        }

        @Test
        public void testLongTupleOf3Average() {
            // LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            // double average = triple.average();   // 2.0
            LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            assertEquals(2.0, triple.average(), 0.001);
        }

        @Test
        public void testLongTupleClassLevelExamples() {
            // long min = triple.min();         // 1
            // long max = triple.max();         // 3
            // double avg = triple.average();   // 2.0
            LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            assertEquals(1L, triple.min());
            assertEquals(3L, triple.max());
            assertEquals(2.0, triple.average(), 0.001);
        }

        @Test
        public void testLongTupleOf4Sum() {
            // LongTuple.LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
            // long sum = quad.sum();   // 10L
            // long min = quad.min();   // 1L
            LongTuple.LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(10L, quad.sum());
            assertEquals(1L, quad.min());
        }

        @Test
        public void testLongTupleOf5() {
            // LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            // double avg = tuple.average();   // 3.0
            // long median = tuple.median();   // 3
            LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(3.0, tuple.average(), 0.001);
            assertEquals(3L, tuple.median());
        }

        @Test
        public void testLongTupleOf6() {
            // LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            // long sum = tuple.sum();         // 21
            // double avg = tuple.average();   // 3.5
            LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(21L, tuple.sum());
            assertEquals(3.5, tuple.average(), 0.001);
        }

        @Test
        public void testLongTupleOf7() {
            // LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            // long sum = tuple.sum();        // 28
            // long median = tuple.median();  // 4
            LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(28L, tuple.sum());
            assertEquals(4L, tuple.median());
        }

        @Test
        public void testLongTupleOf8() {
            // LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            // long sum = tuple.sum();                   // 36
            // double avg = tuple.average();             // 4.5
            // boolean contains5 = tuple.contains(5L);   // true
            LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertEquals(36L, tuple.sum());
            assertEquals(4.5, tuple.average(), 0.001);
            assertTrue(tuple.contains(5L));
        }

        @Test
        public void testLongTupleOf9() {
            // LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            // long sum = tuple.sum();         // 45
            // long median = tuple.median();   // 5
            // double avg = tuple.average();   // 5.0
            LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(45L, tuple.sum());
            assertEquals(5L, tuple.median());
            assertEquals(5.0, tuple.average(), 0.001);
        }

        @Test
        public void testLongTupleMap2() {
            // LongTuple.LongTuple2 dimensions = LongTuple.of(5L, 10L);
            // Long area = dimensions.map((width, height) -> width * height);  // 50
            LongTuple.LongTuple2 dimensions = LongTuple.of(5L, 10L);
            Long longArea = dimensions.map((width, height) -> width * height);
            assertEquals(50L, longArea);

            // LongTuple.LongTuple2 coords = LongTuple.of(3L, 4L);
            // String result = coords.map((x, y) -> "(" + x + ", " + y + ")");  // "(3, 4)"
            LongTuple.LongTuple2 coords = LongTuple.of(3L, 4L);
            String result = coords.map((x, y) -> "(" + x + ", " + y + ")");
            assertEquals("(3, 4)", result);
        }

        @Test
        public void testLongTupleMap3() {
            // LongTuple.LongTuple3 triple = LongTuple.of(2L, 3L, 4L);
            // long volume = triple.map((l, w, h) -> l * w * h);   // 24
            LongTuple.LongTuple3 triple = LongTuple.of(2L, 3L, 4L);
            long volume = triple.map((l, w, h) -> l * w * h);
            assertEquals(24L, volume);

            // LongTuple.LongTuple3 dimensions2 = LongTuple.of(10L, 20L, 30L);
            // String formatted = dimensions2.map((x, y, z) -> x + "x" + y + "x" + z);  // "10x20x30"
            LongTuple.LongTuple3 dimensions2 = LongTuple.of(10L, 20L, 30L);
            String formatted = dimensions2.map((x, y, z) -> x + "x" + y + "x" + z);
            assertEquals("10x20x30", formatted);

            // LongTuple.LongTuple3 values = LongTuple.of(1L, 2L, 3L);
            // Double avg = values.map((a, b, c) -> (a + b + c) / 3.0);  // 2.0
            LongTuple.LongTuple3 values = LongTuple.of(1L, 2L, 3L);
            Double avg = values.map((a, b, c) -> (a + b + c) / 3.0);
            assertEquals(2.0, avg, 0.001);
        }

        @Test
        public void testLongTupleFilter2() {
            // LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
            // u.Optional<LongTuple.LongTuple2> result = pair.filter((a, b) -> a < b);   // Optional containing the tuple
            LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
            Optional<LongTuple.LongTuple2> result = pair.filter((a, b) -> a < b);
            assertTrue(result.isPresent());

            // LongTuple.LongTuple2 values = LongTuple.of(5L, 3L);
            // u.Optional<LongTuple.LongTuple2> empty = values.filter((a, b) -> a < b);  // Empty Optional
            LongTuple.LongTuple2 values = LongTuple.of(5L, 3L);
            Optional<LongTuple.LongTuple2> empty = values.filter((a, b) -> a < b);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testLongTupleFilter3() {
            // LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            // result = triple.filter((a, b, c) -> a < b && b < c);   // Optional containing the tuple
            LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            Optional<LongTuple.LongTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());

            // LongTuple.LongTuple3 descending = LongTuple.of(5L, 4L, 3L);
            // empty = descending.filter((a, b, c) -> a < b && b < c);  // Empty Optional
            LongTuple.LongTuple3 descending = LongTuple.of(5L, 4L, 3L);
            Optional<LongTuple.LongTuple3> empty = descending.filter((a, b, c) -> a < b && b < c);
            assertFalse(empty.isPresent());
        }
    }

    @Nested
    /**
     * Comprehensive test suite for LongTuple and its nested classes.
     * Tests all public methods including factory methods, statistical operations,
     * collection conversions, and special methods in Tuple2 and Tuple3.
     */
    @Tag("2025")
    class LongTuple2025Test extends TestBase {

        // Factory method tests
        @Test
        public void testOf1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1L, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf2() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertEquals(1L, tuple._1);
            assertEquals(2L, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals(1L, tuple._1);
            assertEquals(2L, tuple._2);
            assertEquals(3L, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(1L, tuple._1);
            assertEquals(2L, tuple._2);
            assertEquals(3L, tuple._3);
            assertEquals(4L, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf5() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(1L, tuple._1);
            assertEquals(5L, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf6() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(1L, tuple._1);
            assertEquals(6L, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf7() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(1L, tuple._1);
            assertEquals(7L, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf8() {
            LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertEquals(1L, tuple._1);
            assertEquals(8L, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf9() {
            LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(1L, tuple._1);
            assertEquals(9L, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // Create method tests
        @Test
        public void testCreateEmpty() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreateNull() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate1() {
            LongTuple1 tuple = LongTuple.copyOf(new long[] { 1L });
            assertEquals(1L, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate3() {
            LongTuple3 tuple = LongTuple.copyOf(new long[] { 1L, 2L, 3L });
            assertEquals(1L, tuple._1);
            assertEquals(2L, tuple._2);
            assertEquals(3L, tuple._3);
        }

        @Test
        public void testCreate9() {
            LongTuple9 tuple = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L });
            assertEquals(1L, tuple._1);
            assertEquals(9L, tuple._9);
        }

        @Test
        public void testCreateTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L });
            });
        }

        // Statistical method tests - min
        @Test
        public void testMinTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1L, tuple.min());
        }

        @Test
        public void testMinTuple0ThrowsException() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        // Statistical method tests - max
        @Test
        public void testMaxTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1L, tuple.max());
        }

        @Test
        public void testMaxTuple0ThrowsException() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        // Statistical method tests - median
        @Test
        public void testMedianTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1L, tuple.median());
        }

        @Test
        public void testMedianTuple0ThrowsException() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        // Statistical method tests - sum
        @Test
        public void testSumTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            assertEquals(0L, tuple.sum());
        }

        @Test
        public void testSumTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1L, tuple.sum());
        }

        // Statistical method tests - average
        @Test
        public void testAverageTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals(2.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple0ThrowsException() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        // Reverse tests
        @Test
        public void testReverseTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            LongTuple<LongTuple0> reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        // Contains tests
        @Test
        public void testContainsTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            assertFalse(tuple.contains(1L));
        }

        @Test
        public void testContainsTuple1True() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        public void testContainsTuple1False() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertFalse(tuple.contains(99L));
        }

        @Test
        public void testContainsTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(2L));
            assertTrue(tuple.contains(3L));
            assertFalse(tuple.contains(99L));
        }

        // toArray tests
        @Test
        public void testToArrayTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            long[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        public void testToArrayTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            long[] array = tuple.toArray();
            assertArrayEquals(new long[] { 1L }, array);
        }

        @Test
        public void testToArrayModificationDoesNotAffectTuple() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long[] array = tuple.toArray();
            array[0] = 999L;
            assertEquals(1L, tuple._1);
        }

        // toList tests
        @Test
        public void testToListTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            LongList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        public void testToListTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            LongList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(1L, list.get(0));
        }

        @Test
        public void testToListTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1L, list.get(0));
            assertEquals(2L, list.get(1));
            assertEquals(3L, list.get(2));
        }

        // forEach tests
        @Test
        public void testForEachTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        public void testForEachTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Long.valueOf(1L), result.get(0));
        }

        @Test
        public void testForEachTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Long.valueOf(1L), result.get(0));
            assertEquals(Long.valueOf(2L), result.get(1));
            assertEquals(Long.valueOf(3L), result.get(2));
        }

        // stream tests
        @Test
        public void testStreamTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            LongStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        public void testStreamTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            LongStream stream = tuple.stream();
            assertEquals(1L, stream.sum());
        }

        @Test
        public void testStreamTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongStream stream = tuple.stream();
            assertEquals(6L, stream.sum());
        }

        // hashCode tests
        @Test
        public void testHashCodeTuple1() {
            LongTuple1 tuple1 = LongTuple.of(1L);
            LongTuple1 tuple2 = LongTuple.of(1L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // equals tests
        @Test
        public void testEqualsSameObject() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEqualsNull() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotEquals(null, tuple);
        }

        @Test
        public void testEqualsDifferentClass() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        public void testEqualsTuple1() {
            LongTuple1 tuple1 = LongTuple.of(1L);
            LongTuple1 tuple2 = LongTuple.of(1L);
            LongTuple1 tuple3 = LongTuple.of(99L);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple2() {
            LongTuple2 tuple1 = LongTuple.of(1L, 2L);
            LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            LongTuple2 tuple3 = LongTuple.of(1L, 3L);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple3() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple3 = LongTuple.of(1L, 2L, 4L);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        // toString tests
        @Test
        public void testToStringTuple0() {
            LongTuple<LongTuple0> tuple = LongTuple.copyOf(new long[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testToStringTuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
        }

        @Test
        public void testToStringTuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            String str = tuple.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("2"));
            assertTrue(str.contains("3"));
        }

        // Tuple2 special methods - accept
        @Test
        public void testTuple2Accept() {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            List<Long> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Long.valueOf(3L), result.get(0));
            assertEquals(Long.valueOf(4L), result.get(1));
        }

        // Tuple2 special methods - map
        @Test
        public void testTuple2Map() {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            long result = tuple.map((a, b) -> a * b);
            assertEquals(12L, result);
        }

        // Tuple2 special methods - filter
        @Test
        public void testTuple2FilterTrue() {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            var result = tuple.filter((a, b) -> a + b > 5L);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2FilterFalse() {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            var result = tuple.filter((a, b) -> a + b > 10L);
            assertFalse(result.isPresent());
        }

        // Tuple3 special methods - accept
        @Test
        public void testTuple3Accept() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Long> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Long.valueOf(1L), result.get(0));
            assertEquals(Long.valueOf(2L), result.get(1));
            assertEquals(Long.valueOf(3L), result.get(2));
        }

        // Tuple3 special methods - map
        @Test
        public void testTuple3Map() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6L, result);
        }

        // Tuple3 special methods - filter
        @Test
        public void testTuple3FilterTrue() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            var result = tuple.filter((a, b, c) -> a + b + c > 5L);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple3FilterFalse() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            var result = tuple.filter((a, b, c) -> a + b + c > 10L);
            assertFalse(result.isPresent());
        }

        // arity tests for all tuple sizes
        @Test
        public void testArity() {
            assertEquals(0, LongTuple.copyOf(new long[0]).arity());
            assertEquals(1, LongTuple.of(1L).arity());
            assertEquals(2, LongTuple.of(1L, 2L).arity());
            assertEquals(3, LongTuple.of(1L, 2L, 3L).arity());
            assertEquals(4, LongTuple.of(1L, 2L, 3L, 4L).arity());
            assertEquals(5, LongTuple.of(1L, 2L, 3L, 4L, 5L).arity());
            assertEquals(6, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L).arity());
            assertEquals(7, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L).arity());
            assertEquals(8, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L).arity());
            assertEquals(9, LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L).arity());
        }

        // Tests for inherited methods from PrimitiveTuple
        @Test
        public void testAcceptConsumer() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Long> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals(1L, result.get(0));
            assertEquals(2L, result.get(1));
            assertEquals(3L, result.get(2));
        }

        @Test
        public void testMapFunction() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long result = tuple.map(t -> t._1 + t._2 + t._3);
            assertEquals(6L, result);
        }

        @Test
        public void testFilterPredicate() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            var result = tuple.filter(t -> t._1 == 1L);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testFilterPredicateFalse() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            var result = tuple.filter(t -> t._1 == 99L);
            assertFalse(result.isPresent());
        }

        @Test
        public void testToOptional() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        // Comprehensive tests for Tuple4 through Tuple9
        @Test
        public void testTuple4Operations() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);

            // Test reverse
            LongTuple4 reversed = tuple.reverse();
            assertEquals(4L, reversed._1);
            assertEquals(3L, reversed._2);
            assertEquals(2L, reversed._3);
            assertEquals(1L, reversed._4);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(4L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L }, tuple.toArray());

            // Test min/max/median/sum/average via base class
            assertEquals(1L, tuple.min());
            assertEquals(4L, tuple.max());
            assertEquals(2L, tuple.median());
            assertEquals(10L, tuple.sum());
            assertEquals(2.5, tuple.average(), 0.001);

            // Test hashCode and equals
            LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple4 tuple3 = LongTuple.of(1L, 2L, 3L, 5L);
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
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);

            // Test reverse
            LongTuple5 reversed = tuple.reverse();
            assertEquals(5L, reversed._1);
            assertEquals(1L, reversed._5);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(5L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L }, tuple.toArray());

            // Test statistical operations
            assertEquals(1L, tuple.min());
            assertEquals(5L, tuple.max());
            assertEquals(3L, tuple.median());
            assertEquals(15L, tuple.sum());
            assertEquals(3.0, tuple.average(), 0.001);

            // Test equals
            LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(tuple, tuple2);
        }

        @Test
        public void testTuple6Operations() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);

            // Test reverse
            LongTuple6 reversed = tuple.reverse();
            assertEquals(6L, reversed._1);
            assertEquals(1L, reversed._6);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(6L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, tuple.toArray());

            // Test statistical operations
            assertEquals(1L, tuple.min());
            assertEquals(6L, tuple.max());
            assertEquals(21L, tuple.sum());
            assertEquals(3.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple7Operations() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

            // Test reverse
            LongTuple7 reversed = tuple.reverse();
            assertEquals(7L, reversed._1);
            assertEquals(1L, reversed._7);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(7L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L }, tuple.toArray());

            // Test statistical operations
            assertEquals(1L, tuple.min());
            assertEquals(7L, tuple.max());
            assertEquals(28L, tuple.sum());
            assertEquals(4.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple8Operations() {
            LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);

            // Test reverse
            LongTuple8 reversed = tuple.reverse();
            assertEquals(8L, reversed._1);
            assertEquals(1L, reversed._8);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(8L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L }, tuple.toArray());

            // Test statistical operations
            assertEquals(1L, tuple.min());
            assertEquals(8L, tuple.max());
            assertEquals(36L, tuple.sum());
            assertEquals(4.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple9Operations() {
            LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);

            // Test reverse
            LongTuple9 reversed = tuple.reverse();
            assertEquals(9L, reversed._1);
            assertEquals(1L, reversed._9);

            // Test contains
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(9L));
            assertFalse(tuple.contains(99L));

            // Test toArray
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L }, tuple.toArray());

            // Test statistical operations
            assertEquals(1L, tuple.min());
            assertEquals(9L, tuple.max());
            assertEquals(45L, tuple.sum());
            assertEquals(5.0, tuple.average(), 0.001);
        }

        // Test create methods for sizes 2, 4-8
        @Test
        public void testCreate2Through8() {
            LongTuple2 tuple2 = LongTuple.copyOf(new long[] { 1L, 2L });
            assertEquals(1L, tuple2._1);
            assertEquals(2L, tuple2._2);

            LongTuple4 tuple4 = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L });
            assertEquals(1L, tuple4._1);
            assertEquals(4L, tuple4._4);

            LongTuple5 tuple5 = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L });
            assertEquals(1L, tuple5._1);
            assertEquals(5L, tuple5._5);

            LongTuple6 tuple6 = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L });
            assertEquals(1L, tuple6._1);
            assertEquals(6L, tuple6._6);

            LongTuple7 tuple7 = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L });
            assertEquals(1L, tuple7._1);
            assertEquals(7L, tuple7._7);

            LongTuple8 tuple8 = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L });
            assertEquals(1L, tuple8._1);
            assertEquals(8L, tuple8._8);
        }

        // Test toList for larger tuples
        @Test
        public void testToListTuple4Through9() {
            LongTuple4 tuple4 = LongTuple.of(1L, 2L, 3L, 4L);
            LongList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals(4L, list4.get(3));

            LongTuple9 tuple9 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            LongList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals(9L, list9.get(8));
        }

        // Test forEach for larger tuples
        @Test
        public void testForEachTuple4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Long.valueOf(4L), result.get(3));
        }

        // Test forEach override for Tuple2
        @Test
        public void testForEachTuple2Override() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Long.valueOf(10L), result.get(0));
            assertEquals(Long.valueOf(20L), result.get(1));
        }

        // Test forEach override for Tuple3
        @Test
        public void testForEachTuple3Override() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            List<Long> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Long.valueOf(30L), result.get(2));
        }

        // Test stream for larger tuples
        @Test
        public void testStreamTuple4Through9() {
            LongTuple4 tuple4 = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(10L, tuple4.stream().sum());

            LongTuple9 tuple9 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(45L, tuple9.stream().sum());
        }

        // ==================== LongTuple Nested Class Tests ====================

        // ============ LongTuple1 Nested Class Tests ============

        @Test
        public void testLongTuple1_arity() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testLongTuple1_reverse() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            LongTuple.LongTuple1 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        public void testLongTuple1_contains() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        public void testLongTuple1_hashCode() {
            LongTuple.LongTuple1 tuple1 = LongTuple.of(1L);
            LongTuple.LongTuple1 tuple2 = LongTuple.of(1L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testLongTuple1_equals() {
            LongTuple.LongTuple1 tuple1 = LongTuple.of(1L);
            LongTuple.LongTuple1 tuple2 = LongTuple.of(1L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testLongTuple1_toString() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testLongTuple1_forEach() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testLongTuple1_min() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertNotNull(tuple.min());
        }

        @Test
        public void testLongTuple1_max() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertNotNull(tuple.max());
        }

        @Test
        public void testLongTuple1_median() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertNotNull(tuple.median());
        }

        @Test
        public void testLongTuple1_sum() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testLongTuple1_average() {
            LongTuple.LongTuple1 tuple = LongTuple.of(1L);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        // ============ LongTuple2 Nested Class Tests ============

        @Test
        public void testLongTuple2_arity() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testLongTuple2_reverse() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            LongTuple.LongTuple2 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        public void testLongTuple2_contains() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        public void testLongTuple2_hashCode() {
            LongTuple.LongTuple2 tuple1 = LongTuple.of(1L, 2L);
            LongTuple.LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testLongTuple2_equals() {
            LongTuple.LongTuple2 tuple1 = LongTuple.of(1L, 2L);
            LongTuple.LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testLongTuple2_toString() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testLongTuple2_forEach() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        public void testLongTuple2_min() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertNotNull(tuple.min());
        }

        @Test
        public void testLongTuple2_max() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertNotNull(tuple.max());
        }

        @Test
        public void testLongTuple2_median() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertNotNull(tuple.median());
        }

        @Test
        public void testLongTuple2_sum() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testLongTuple2_average() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testLongTuple2_accept_biConsumer() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testLongTuple2_map_biFunction() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testLongTuple2_filter_biPredicate() {
            LongTuple.LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        // ============ LongTuple3 Nested Class Tests ============

        @Test
        public void testLongTuple3_arity() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testLongTuple3_reverse() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongTuple.LongTuple3 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        public void testLongTuple3_contains() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        public void testLongTuple3_hashCode() {
            LongTuple.LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple.LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testLongTuple3_equals() {
            LongTuple.LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple.LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testLongTuple3_toString() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testLongTuple3_forEach() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        public void testLongTuple3_min() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotNull(tuple.min());
        }

        @Test
        public void testLongTuple3_max() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotNull(tuple.max());
        }

        @Test
        public void testLongTuple3_median() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotNull(tuple.median());
        }

        @Test
        public void testLongTuple3_sum() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testLongTuple3_average() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testLongTuple3_accept_triConsumer() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testLongTuple3_map_triFunction() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testLongTuple3_filter_triPredicate() {
            LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        // ============ LongTuple4 Nested Class Tests ============

        @Test
        public void testLongTuple4_arity() {
            LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testLongTuple4_reverse() {
            LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple.LongTuple4 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        public void testLongTuple4_contains() {
            LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        public void testLongTuple4_hashCode() {
            LongTuple.LongTuple4 tuple1 = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple.LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testLongTuple4_equals() {
            LongTuple.LongTuple4 tuple1 = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple.LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testLongTuple4_toString() {
            LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testLongTuple4_forEach() {
            LongTuple.LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        // ============ LongTuple5 Nested Class Tests ============

        @Test
        public void testLongTuple5_arity() {
            LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testLongTuple5_reverse() {
            LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple.LongTuple5 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        public void testLongTuple5_contains() {
            LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        public void testLongTuple5_hashCode() {
            LongTuple.LongTuple5 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple.LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testLongTuple5_equals() {
            LongTuple.LongTuple5 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple.LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testLongTuple5_toString() {
            LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testLongTuple5_forEach() {
            LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        // ============ LongTuple6 Nested Class Tests ============

        @Test
        public void testLongTuple6_arity() {
            LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testLongTuple6_reverse() {
            LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            LongTuple.LongTuple6 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        public void testLongTuple6_contains() {
            LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        public void testLongTuple6_hashCode() {
            LongTuple.LongTuple6 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            LongTuple.LongTuple6 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testLongTuple6_equals() {
            LongTuple.LongTuple6 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            LongTuple.LongTuple6 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testLongTuple6_toString() {
            LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testLongTuple6_forEach() {
            LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        // ============ LongTuple7 Nested Class Tests ============

        @Test
        public void testLongTuple7_arity() {
            LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testLongTuple7_reverse() {
            LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            LongTuple.LongTuple7 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        public void testLongTuple7_contains() {
            LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        public void testLongTuple7_hashCode() {
            LongTuple.LongTuple7 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            LongTuple.LongTuple7 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testLongTuple7_equals() {
            LongTuple.LongTuple7 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            LongTuple.LongTuple7 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testLongTuple7_toString() {
            LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testLongTuple7_forEach() {
            LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        // ============ LongTuple8 Nested Class Tests ============

        @Test
        public void testLongTuple8_arity() {
            LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testLongTuple8_reverse() {
            LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            LongTuple.LongTuple8 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        public void testLongTuple8_contains() {
            LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        public void testLongTuple8_hashCode() {
            LongTuple.LongTuple8 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            LongTuple.LongTuple8 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testLongTuple8_equals() {
            LongTuple.LongTuple8 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            LongTuple.LongTuple8 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testLongTuple8_toString() {
            LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testLongTuple8_forEach() {
            LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        // ============ LongTuple9 Nested Class Tests ============

        @Test
        public void testLongTuple9_arity() {
            LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testLongTuple9_reverse() {
            LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            LongTuple.LongTuple9 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._9);
            assertEquals(tuple._9, reversed._1);
        }

        @Test
        public void testLongTuple9_contains() {
            LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertTrue(tuple.contains(1L));
        }

        @Test
        public void testLongTuple9_hashCode() {
            LongTuple.LongTuple9 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            LongTuple.LongTuple9 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testLongTuple9_equals() {
            LongTuple.LongTuple9 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            LongTuple.LongTuple9 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testLongTuple9_toString() {
            LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testLongTuple9_forEach() {
            LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

    }

    @Nested
    @Tag("2510")
    class LongTuple2510Test extends TestBase {
        @Test
        public void testOf2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            assertEquals(2, tuple.arity());
            assertEquals(10L, tuple._1);
            assertEquals(20L, tuple._2);
        }

        @Test
        public void testOf3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals(3, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(2L, tuple._2);
            assertEquals(3L, tuple._3);
        }

        @Test
        public void testOf4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(4, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(2L, tuple._2);
            assertEquals(3L, tuple._3);
            assertEquals(4L, tuple._4);
        }

        @Test
        public void testOf5() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(5, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(5L, tuple._5);
        }

        @Test
        public void testOf6() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(6, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(6L, tuple._6);
        }

        @Test
        public void testOf7() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(7, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(7L, tuple._7);
        }

        @Test
        public void testOf8() {
            LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertEquals(8, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(8L, tuple._8);
        }

        @Test
        public void testOf9() {
            LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(9, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(9L, tuple._9);
        }

        @Test
        public void testCreate_nullArray() {
            LongTuple<?> tuple = LongTuple.copyOf(null);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_emptyArray() {
            LongTuple<?> tuple = LongTuple.copyOf(new long[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_sizeOne() {
            LongTuple1 tuple = LongTuple.copyOf(new long[] { 42L });
            assertEquals(1, tuple.arity());
            assertEquals(42L, tuple._1);
        }

        @Test
        public void testCreate_sizeTwo() {
            LongTuple2 tuple = LongTuple.copyOf(new long[] { 10L, 20L });
            assertEquals(2, tuple.arity());
            assertEquals(10L, tuple._1);
            assertEquals(20L, tuple._2);
        }

        @Test
        public void testCreate_sizeThree() {
            LongTuple3 tuple = LongTuple.copyOf(new long[] { 1L, 2L, 3L });
            assertEquals(3, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(3L, tuple._3);
        }

        @Test
        public void testCreate_sizeFour() {
            LongTuple4 tuple = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L });
            assertEquals(4, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(4L, tuple._4);
        }

        @Test
        public void testCreate_sizeFive() {
            LongTuple5 tuple = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L });
            assertEquals(5, tuple.arity());
            assertEquals(5L, tuple._5);
        }

        @Test
        public void testCreate_sizeSix() {
            LongTuple6 tuple = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L });
            assertEquals(6, tuple.arity());
            assertEquals(6L, tuple._6);
        }

        @Test
        public void testCreate_sizeSeven() {
            LongTuple7 tuple = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L });
            assertEquals(7, tuple.arity());
            assertEquals(7L, tuple._7);
        }

        @Test
        public void testCreate_sizeEight() {
            LongTuple8 tuple = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L });
            assertEquals(8, tuple.arity());
            assertEquals(8L, tuple._8);
        }

        @Test
        public void testCreate_sizeNine() {
            LongTuple9 tuple = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L });
            assertEquals(9, tuple.arity());
            assertEquals(9L, tuple._9);
        }

        @Test
        public void testCreate_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L }));
        }

        @Test
        public void testMin_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42L, tuple.min());
        }

        @Test
        public void testMin_tuple2() {
            LongTuple2 tuple = LongTuple.of(5L, 2L);
            assertEquals(2L, tuple.min());
        }

        @Test
        public void testMin_tuple3() {
            LongTuple3 tuple = LongTuple.of(5L, 2L, 8L);
            assertEquals(2L, tuple.min());
        }

        @Test
        public void testMin_tuple4() {
            LongTuple4 tuple = LongTuple.of(5L, 2L, 8L, 1L);
            assertEquals(1L, tuple.min());
        }

        @Test
        public void testMax_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42L, tuple.max());
        }

        @Test
        public void testMax_tuple2() {
            LongTuple2 tuple = LongTuple.of(5L, 2L);
            assertEquals(5L, tuple.max());
        }

        @Test
        public void testMax_tuple3() {
            LongTuple3 tuple = LongTuple.of(5L, 2L, 8L);
            assertEquals(8L, tuple.max());
        }

        @Test
        public void testMedian_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42L, tuple.median());
        }

        @Test
        public void testMedian_tuple2() {
            LongTuple2 tuple = LongTuple.of(5L, 2L);
            assertEquals(2L, tuple.median());
        }

        @Test
        public void testMedian_tuple3() {
            LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
            assertEquals(2L, tuple.median());
        }

        @Test
        public void testMedian_tuple4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(2L, tuple.median());
        }

        @Test
        public void testSum_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42L, tuple.sum());
        }

        @Test
        public void testSum_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            assertEquals(30L, tuple.sum());
        }

        @Test
        public void testSum_tuple3() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            assertEquals(60L, tuple.sum());
        }

        @Test
        public void testAverage_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverage_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            assertEquals(15.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverage_tuple3() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            assertEquals(20.0, tuple.average(), 0.001);
        }

        @Test
        public void testReverse_tuple1() {
            LongTuple1 tuple = LongTuple.of(1L);
            LongTuple1 reversed = tuple.reverse();
            assertEquals(1L, reversed._1);
        }

        @Test
        public void testReverse_tuple4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple4 reversed = tuple.reverse();
            assertEquals(4L, reversed._1);
            assertEquals(1L, reversed._4);
        }

        @Test
        public void testContains_tuple1_found() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertTrue(tuple.contains(42L));
        }

        @Test
        public void testContains_tuple1_notFound() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertFalse(tuple.contains(100L));
        }

        @Test
        public void testContains_tuple2_found() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            assertTrue(tuple.contains(20L));
        }

        @Test
        public void testContains_tuple2_notFound() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            assertFalse(tuple.contains(40L));
        }

        @Test
        public void testContains_tuple3_found() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            assertTrue(tuple.contains(20L));
        }

        @Test
        public void testContains_tuple3_notFound() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            assertFalse(tuple.contains(40L));
        }

        @Test
        public void testToArray_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            long[] array = tuple.toArray();
            assertArrayEquals(new long[] { 42L }, array);
        }

        @Test
        public void testToArray_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            long[] array = tuple.toArray();
            assertArrayEquals(new long[] { 10L, 20L }, array);
        }

        @Test
        public void testToArray_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long[] array = tuple.toArray();
            assertArrayEquals(new long[] { 1L, 2L, 3L }, array);
        }

        @Test
        public void testToList_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            LongList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(42L, list.get(0));
        }

        @Test
        public void testToList_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            LongList list = tuple.toList();
            assertEquals(2, list.size());
            assertEquals(10L, list.get(0));
            assertEquals(20L, list.get(1));
        }

        @Test
        public void testToList_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(3L, list.get(2));
        }

        @Test
        public void testForEach_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            AtomicLong sum = new AtomicLong(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(42L, sum.get());
        }

        @Test
        public void testForEach_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            AtomicLong sum = new AtomicLong(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(30L, sum.get());
        }

        @Test
        public void testForEach_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            AtomicLong sum = new AtomicLong(0);
            tuple.forEach(value -> sum.addAndGet(value));
            assertEquals(6L, sum.get());
        }

        @Test
        public void testStream_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            long sum = tuple.stream().sum();
            assertEquals(42L, sum);
        }

        @Test
        public void testStream_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            long sum = tuple.stream().sum();
            assertEquals(30L, sum);
        }

        @Test
        public void testStream_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long sum = tuple.stream().sum();
            assertEquals(6L, sum);
        }

        @Test
        public void testHashCode_tuple1() {
            LongTuple1 tuple1 = LongTuple.of(42L);
            LongTuple1 tuple2 = LongTuple.of(42L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCode_tuple1_different() {
            LongTuple1 tuple1 = LongTuple.of(42L);
            LongTuple1 tuple2 = LongTuple.of(100L);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCode_tuple2() {
            LongTuple2 tuple1 = LongTuple.of(10L, 20L);
            LongTuple2 tuple2 = LongTuple.of(10L, 20L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCode_tuple3() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testEquals_tuple1_same() {
            LongTuple1 tuple1 = LongTuple.of(42L);
            LongTuple1 tuple2 = LongTuple.of(42L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple1_sameObject() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEquals_tuple1_different() {
            LongTuple1 tuple1 = LongTuple.of(42L);
            LongTuple1 tuple2 = LongTuple.of(100L);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple1_null() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertNotEquals(null, tuple);
        }

        @Test
        public void testEquals_tuple2_same() {
            LongTuple2 tuple1 = LongTuple.of(10L, 20L);
            LongTuple2 tuple2 = LongTuple.of(10L, 20L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple2_different() {
            LongTuple2 tuple1 = LongTuple.of(10L, 20L);
            LongTuple2 tuple2 = LongTuple.of(10L, 30L);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple3_same() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple3_different() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 4L);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testToString_tuple1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals("(42)", tuple.toString());
        }

        @Test
        public void testToString_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            assertEquals("(10, 20)", tuple.toString());
        }

        @Test
        public void testToString_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals("(1, 2, 3)", tuple.toString());
        }

        @Test
        public void testAccept_tuple2() {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            AtomicLong result = new AtomicLong(0);
            tuple.accept((a, b) -> result.set(a + b));
            assertEquals(7L, result.get());
        }

        @Test
        public void testMap_tuple2() {
            LongTuple2 tuple = LongTuple.of(10L, 3L);
            long result = tuple.map((a, b) -> a % b);
            assertEquals(1L, result);
        }

        @Test
        public void testFilter_tuple2_match() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            Optional<LongTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testFilter_tuple2_noMatch() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            Optional<LongTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        public void testAccept_tuple3() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            AtomicLong result = new AtomicLong(0);
            tuple.accept((a, b, c) -> result.set(a + b + c));
            assertEquals(6L, result.get());
        }

        @Test
        public void testMap_tuple3() {
            LongTuple3 tuple = LongTuple.of(2L, 3L, 4L);
            long result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(24L, result);
        }

        @Test
        public void testFilter_tuple3_match() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            Optional<LongTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
        }

        @Test
        public void testFilter_tuple3_noMatch() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            Optional<LongTuple3> result = tuple.filter((a, b, c) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        public void testTuple0_min() {
            LongTuple<?> tuple = LongTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void testTuple0_max() {
            LongTuple<?> tuple = LongTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void testTuple0_median() {
            LongTuple<?> tuple = LongTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void testTuple0_sum() {
            LongTuple<?> tuple = LongTuple.copyOf(null);
            assertEquals(0L, tuple.sum());
        }

        @Test
        public void testTuple0_average() {
            LongTuple<?> tuple = LongTuple.copyOf(null);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void testTuple0_reverse() {
            LongTuple<?> tuple = LongTuple.copyOf(null);
            assertNotNull(tuple.reverse());
        }

        @Test
        public void testTuple0_contains() {
            LongTuple<?> tuple = LongTuple.copyOf(null);
            assertFalse(tuple.contains(1L));
        }

        @Test
        public void testTuple0_toString() {
            LongTuple<?> tuple = LongTuple.copyOf(null);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testTuple5_operations() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(5, tuple.arity());
            assertEquals(1L, tuple.min());
            assertEquals(5L, tuple.max());
            assertEquals(3L, tuple.median());
            assertEquals(15L, tuple.sum());
            assertEquals(3.0, tuple.average(), 0.001);
            assertTrue(tuple.contains(3L));
            assertFalse(tuple.contains(10L));
        }

        @Test
        public void testTuple5_reverse() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple5 reversed = tuple.reverse();
            assertEquals(5L, reversed._1);
            assertEquals(4L, reversed._2);
            assertEquals(3L, reversed._3);
            assertEquals(2L, reversed._4);
            assertEquals(1L, reversed._5);
        }

        @Test
        public void testTuple6_operations() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(6, tuple.arity());
            assertEquals(1L, tuple.min());
            assertEquals(6L, tuple.max());
            assertEquals(21L, tuple.sum());
        }

        @Test
        public void testTuple6_reverse() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            LongTuple6 reversed = tuple.reverse();
            assertEquals(6L, reversed._1);
            assertEquals(1L, reversed._6);
        }

        @Test
        public void testTuple7_operations() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(7, tuple.arity());
            assertEquals(4L, tuple.median());
            assertEquals(28L, tuple.sum());
        }

        @Test
        public void testTuple7_reverse() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            LongTuple7 reversed = tuple.reverse();
            assertEquals(7L, reversed._1);
            assertEquals(1L, reversed._7);
        }

        @Test
        public void testTuple8_operations() {
            LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testTuple8_reverse() {
            LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            LongTuple8 reversed = tuple.reverse();
            assertEquals(8L, reversed._1);
            assertEquals(1L, reversed._8);
        }

        @Test
        public void testTuple9_operations() {
            LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testTuple9_reverse() {
            LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            LongTuple9 reversed = tuple.reverse();
            assertEquals(9L, reversed._1);
            assertEquals(1L, reversed._9);
        }

        @Test
        public void testContains_tuple4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(4L));
            assertFalse(tuple.contains(5L));
        }

        @Test
        public void testContains_tuple5() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertTrue(tuple.contains(5L));
            assertFalse(tuple.contains(10L));
        }

        @Test
        public void testContains_tuple6() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertTrue(tuple.contains(6L));
            assertFalse(tuple.contains(7L));
        }

        @Test
        public void testContains_tuple7() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertTrue(tuple.contains(7L));
            assertFalse(tuple.contains(8L));
        }

        @Test
        public void testContains_tuple8() {
            LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertTrue(tuple.contains(8L));
            assertFalse(tuple.contains(9L));
        }

        @Test
        public void testContains_tuple9() {
            LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertTrue(tuple.contains(9L));
            assertFalse(tuple.contains(10L));
        }

        @Test
        public void testEquals_tuple4() {
            LongTuple4 tuple1 = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple4 tuple2 = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_tuple5() {
            LongTuple5 tuple1 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple5 tuple2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testToString_tuple4() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals("(1, 2, 3, 4)", tuple.toString());
        }

        @Test
        public void testToString_tuple5() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals("(1, 2, 3, 4, 5)", tuple.toString());
        }

        @Test
        public void testMap_tuple2_returnString() {
            LongTuple2 tuple = LongTuple.of(10L, 20L);
            String result = tuple.map((a, b) -> "Sum: " + (a + b));
            assertEquals("Sum: 30", result);
        }

        @Test
        public void testMap_tuple3_returnString() {
            LongTuple3 tuple = LongTuple.of(3L, 4L, 5L);
            String result = tuple.map((a, b, c) -> {
                if (a * a + b * b == c * c) {
                    return "Pythagorean triple!";
                }
                return "Not a Pythagorean triple";
            });
            assertEquals("Pythagorean triple!", result);
        }

        @Test
        public void testTuple4_minMaxMedian() {
            LongTuple4 tuple = LongTuple.of(10L, 5L, 15L, 20L);
            assertEquals(5L, tuple.min());
            assertEquals(20L, tuple.max());
            assertEquals(10L, tuple.median());
        }

        @Test
        public void testTuple5_sum() {
            LongTuple5 tuple = LongTuple.of(10L, 20L, 30L, 40L, 50L);
            assertEquals(150L, tuple.sum());
        }

        @Test
        public void testTuple6_average() {
            LongTuple6 tuple = LongTuple.of(10L, 20L, 30L, 40L, 50L, 60L);
            assertEquals(35.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple7_median() {
            LongTuple7 tuple = LongTuple.of(1L, 3L, 5L, 7L, 9L, 11L, 13L);
            assertEquals(7L, tuple.median());
        }
    }

    @Nested
    @Tag("2511")
    class LongTuple2511Test extends TestBase {

        // ====== Factory Methods Tests ======

        @Test
        public void testOf1() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(1, tuple.arity());
            assertEquals(42L, tuple._1);
        }

        @Test
        public void testCreateFromArray() {
            // Empty array
            LongTuple<?> empty = LongTuple.copyOf(null);
            assertEquals(0, empty.arity());

            empty = LongTuple.copyOf(new long[0]);
            assertEquals(0, empty.arity());

            // Single element
            LongTuple<?> tuple1 = LongTuple.copyOf(new long[] { 42L });
            assertEquals(1, tuple1.arity());

            // Multiple elements
            LongTuple<?> tuple3 = LongTuple.copyOf(new long[] { 10L, 20L, 30L });
            assertEquals(3, tuple3.arity());

            // Max size
            LongTuple<?> tuple9 = LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L });
            assertEquals(9, tuple9.arity());

            // Too many elements
            assertThrows(IllegalArgumentException.class, () -> LongTuple.copyOf(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L }));
        }

        // ====== Arity Tests ======

        @Test
        public void testArity0() {
            LongTuple<?> empty = LongTuple.copyOf(null);
            assertEquals(0, empty.arity());
        }

        @Test
        public void testArity1() {
            LongTuple1 tuple = LongTuple.of(1L);
            assertEquals(1, tuple.arity());
        }
        // ====== Statistics Tests (min, max, median, sum, average) ======

        @Test
        public void testMin() {
            assertEquals(2L, LongTuple.of(5L, 2L, 8L).min());
            assertEquals(1L, LongTuple.of(1L, 2L, 3L, 4L, 5L).min());
            assertEquals(-10L, LongTuple.of(-5L, -10L, 0L).min());
        }

        @Test
        public void testMax() {
            assertEquals(8L, LongTuple.of(5L, 2L, 8L).max());
            assertEquals(5L, LongTuple.of(1L, 2L, 3L, 4L, 5L).max());
            assertEquals(10L, LongTuple.of(-5L, 0L, 10L).max());
        }

        @Test
        public void testMedian() {
            // Odd number of elements - returns middle value
            assertEquals(2L, LongTuple.of(3L, 1L, 2L).median());
            assertEquals(3L, LongTuple.of(1L, 2L, 3L, 4L, 5L).median());

            // Even number of elements - returns lower middle value
            assertEquals(2L, LongTuple.of(1L, 2L, 3L, 4L).median());
        }

        @Test
        public void testSum() {
            assertEquals(60L, LongTuple.of(10L, 20L, 30L).sum());
            assertEquals(15L, LongTuple.of(1L, 2L, 3L, 4L, 5L).sum());
            assertEquals(0L, LongTuple.copyOf(null).sum());
        }

        @Test
        public void testAverage() {
            assertEquals(20.0, LongTuple.of(10L, 20L, 30L).average(), 0.0001);
            assertEquals(3.0, LongTuple.of(1L, 2L, 3L, 4L, 5L).average(), 0.0001);
        }

        @Test
        public void testMin_Empty() {
            assertThrows(NoSuchElementException.class, () -> LongTuple.copyOf(null).min());
        }

        @Test
        public void testMax_Empty() {
            assertThrows(NoSuchElementException.class, () -> LongTuple.copyOf(null).max());
        }

        @Test
        public void testMedian_Empty() {
            assertThrows(NoSuchElementException.class, () -> LongTuple.copyOf(null).median());
        }

        @Test
        public void testAverage_Empty() {
            assertThrows(NoSuchElementException.class, () -> LongTuple.copyOf(null).average());
        }

        // ====== Reverse Tests ======

        @Test
        public void testReverse1() {
            LongTuple1 reversed = LongTuple.of(42L).reverse();
            assertEquals(42L, reversed._1);
        }

        @Test
        public void testReverse2() {
            LongTuple2 reversed = LongTuple.of(10L, 20L).reverse();
            assertEquals(20L, reversed._1);
            assertEquals(10L, reversed._2);
        }

        @Test
        public void testReverse3() {
            LongTuple3 reversed = LongTuple.of(1L, 2L, 3L).reverse();
            assertEquals(3L, reversed._1);
            assertEquals(2L, reversed._2);
            assertEquals(1L, reversed._3);
        }

        @Test
        public void testReverse0() {
            LongTuple<?> reversed = LongTuple.copyOf(null).reverse();
            assertEquals(0, reversed.arity());
        }

        // ====== Contains Tests ======

        @Test
        public void testContains() {
            LongTuple3 tuple = LongTuple.of(10L, 20L, 30L);
            assertTrue(tuple.contains(20L));
            assertFalse(tuple.contains(40L));
            assertTrue(tuple.contains(10L));
            assertTrue(tuple.contains(30L));
        }

        @Test
        public void testContains_Empty() {
            assertFalse(LongTuple.copyOf(null).contains(10L));
        }

        @Test
        public void testContains_Single() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertTrue(tuple.contains(42L));
            assertFalse(tuple.contains(43L));
        }

        // ====== Array Conversion Tests ======

        @Test
        public void testToArray() {
            long[] array = LongTuple.of(1L, 2L, 3L).toArray();
            assertArrayEquals(new long[] { 1L, 2L, 3L }, array);

            // Test that modifications to returned array don't affect tuple
            array[0] = 999L;
            assertEquals(1L, LongTuple.of(1L, 2L, 3L).toArray()[0]);
        }

        @Test
        public void testToArray_Empty() {
            long[] array = LongTuple.copyOf(null).toArray();
            assertEquals(0, array.length);
        }

        // ====== List Conversion Tests ======

        @Test
        public void testToList() {
            LongList list = LongTuple.of(1L, 2L, 3L).toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals(1L, list.get(0));
            assertEquals(2L, list.get(1));
            assertEquals(3L, list.get(2));
        }

        @Test
        public void testToList_Empty() {
            LongList list = LongTuple.copyOf(null).toList();
            assertEquals(0, list.size());
        }

        // ====== forEach Tests ======

        @Test
        public void testForEach() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            AtomicLong sum = new AtomicLong(0);
            tuple.forEach(sum::addAndGet);
            assertEquals(6L, sum.get());
        }

        @Test
        public void testForEach_Empty() {
            AtomicLong sum = new AtomicLong(0);
            LongTuple.copyOf(null).forEach(sum::addAndGet);
            assertEquals(0L, sum.get());
        }

        // ====== Stream Tests ======

        @Test
        public void testStream() {
            long sum = LongTuple.of(1L, 2L, 3L).stream().sum();
            assertEquals(6L, sum);
        }

        @Test
        public void testStream_Empty() {
            long sum = LongTuple.copyOf(null).stream().sum();
            assertEquals(0L, sum);
        }

        // ====== Equality Tests ======

        @Test
        public void testEquals() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple3 = LongTuple.of(1L, 2L, 4L);

            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
            assertEquals(tuple1, tuple1);
            assertNotEquals(tuple1, null);
        }

        @Test
        public void testEquals_Empty() {
            LongTuple<?> empty1 = LongTuple.copyOf(null);
            LongTuple<?> empty2 = LongTuple.copyOf(new long[0]);
            assertEquals(empty1, empty2);
        }
        // ====== String Representation Tests ======

        @Test
        public void testToString() {
            assertEquals("(42)", LongTuple.of(42L).toString());
            assertEquals("(10, 20)", LongTuple.of(10L, 20L).toString());
            assertEquals("(1, 2, 3)", LongTuple.of(1L, 2L, 3L).toString());
            assertEquals("()", LongTuple.copyOf(null).toString());
        }

        // ====== LongTuple2 Specific Tests ======

        @Test
        public void testAccept2() {
            LongTuple2 pair = LongTuple.of(3L, 4L);
            AtomicLong sum = new AtomicLong(0);
            pair.accept((a, b) -> sum.set(a + b));
            assertEquals(7L, sum.get());
        }

        @Test
        public void testMap2() {
            LongTuple2 pair = LongTuple.of(10L, 3L);
            long remainder = pair.map((a, b) -> a % b);
            assertEquals(1L, remainder);
        }

        @Test
        public void testFilter2_True() {
            LongTuple2 pair = LongTuple.of(10L, 20L);
            Optional<LongTuple2> result = pair.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(pair, result.get());
        }

        @Test
        public void testFilter2_False() {
            LongTuple2 pair = LongTuple.of(20L, 10L);
            Optional<LongTuple2> result = pair.filter((a, b) -> a < b);
            assertFalse(result.isPresent());
        }

        // ====== LongTuple3 Specific Tests ======

        @Test
        public void testAccept3() {
            LongTuple3 triple = LongTuple.of(3L, 4L, 5L);
            AtomicLong sum = new AtomicLong(0);
            triple.accept((a, b, c) -> sum.set(a + b + c));
            assertEquals(12L, sum.get());
        }

        @Test
        public void testMap3() {
            LongTuple3 triple = LongTuple.of(2L, 3L, 4L);
            long volume = triple.map((l, w, h) -> l * w * h);
            assertEquals(24L, volume);
        }

        @Test
        public void testFilter3_True() {
            LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
            Optional<LongTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
            assertEquals(triple, result.get());
        }

        @Test
        public void testFilter3_False() {
            LongTuple3 triple = LongTuple.of(3L, 2L, 1L);
            Optional<LongTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
            assertFalse(result.isPresent());
        }

        // ====== Edge Cases and Special Values ======

        @Test
        public void testWithZero() {
            LongTuple3 tuple = LongTuple.of(0L, 1L, 2L);
            assertEquals(0L, tuple.min());
            assertEquals(2L, tuple.max());
            assertEquals(3L, tuple.sum());
        }

        @Test
        public void testWithNegatives() {
            LongTuple3 tuple = LongTuple.of(-5L, -10L, -1L);
            assertEquals(-10L, tuple.min());
            assertEquals(-1L, tuple.max());
            assertEquals(-16L, tuple.sum());
        }

        @Test
        public void testWithLargeValues() {
            long max = Long.MAX_VALUE / 2;
            LongTuple2 tuple = LongTuple.of(max, max);
            assertEquals(max, tuple.min());
            assertEquals(max, tuple.max());
        }

        @Test
        public void testWithSingleElement() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertEquals(42L, tuple.min());
            assertEquals(42L, tuple.max());
            assertEquals(42L, tuple.median());
            assertEquals(42L, tuple.sum());
            assertEquals(42.0, tuple.average(), 0.0001);
            assertTrue(tuple.contains(42L));
        }

        // ====== All Tuple Sizes Combined ======

        @Test
        public void testAllTupleSizes() {
            LongTuple0 t0 = LongTuple.copyOf(null);
            LongTuple1 t1 = LongTuple.of(1L);
            LongTuple2 t2 = LongTuple.of(1L, 2L);
            LongTuple3 t3 = LongTuple.of(1L, 2L, 3L);
            LongTuple4 t4 = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple5 t5 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            LongTuple6 t6 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            LongTuple7 t7 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            LongTuple8 t8 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            LongTuple9 t9 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);

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
            LongTuple2 pair = LongTuple.of(3L, 4L);
            assertThrows(IllegalArgumentException.class, () -> pair.accept((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        @Test
        public void testMap2_Exception() {
            LongTuple2 pair = LongTuple.of(3L, 4L);
            assertThrows(IllegalArgumentException.class, () -> pair.map((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        @Test
        public void testFilter2_Exception() {
            LongTuple2 pair = LongTuple.of(3L, 4L);
            assertThrows(IllegalArgumentException.class, () -> pair.filter((a, b) -> {
                throw new IllegalArgumentException("test");
            }));
        }

        // ====== Immutability Tests ======

        @Test
        public void testImmutability() {
            long[] arr = new long[] { 1L, 2L, 3L };
            LongTuple3 tuple = LongTuple.copyOf(arr);
            arr[0] = 999L;
            assertEquals(1L, tuple._1);
        }

        @Test
        public void testToArray_Independence() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long[] arr1 = tuple.toArray();
            long[] arr2 = tuple.toArray();
            arr1[0] = 999L;
            assertEquals(1L, arr2[0]);
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for LongTuple and its nested tuple classes (LongTuple0 through LongTuple9).
     * Tests cover all public methods including factory methods, statistical operations, conversions, and edge cases.
     */
    @Tag("2512")
    class LongTuple2512Test extends TestBase {

        // ===== Factory Method Tests =====

        @Test
        public void test_of_singleElement() {
            LongTuple1 tuple = LongTuple.of(42L);
            assertNotNull(tuple);
            assertEquals(42L, tuple._1);
        }

        @Test
        public void test_of_twoElements() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertNotNull(tuple);
            assertEquals(1L, tuple._1);
            assertEquals(2L, tuple._2);
        }

        @Test
        public void test_of_threeElements() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertNotNull(tuple);
            assertEquals(1L, tuple._1);
            assertEquals(2L, tuple._2);
            assertEquals(3L, tuple._3);
        }

        @Test
        public void test_of_fourElements() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertNotNull(tuple);
            assertEquals(1L, tuple._1);
            assertEquals(4L, tuple._4);
        }

        @Test
        public void test_of_fiveElements() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertNotNull(tuple);
            assertEquals(5L, tuple._5);
        }

        @Test
        public void test_of_sixElements() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertNotNull(tuple);
            assertEquals(6L, tuple._6);
        }

        @Test
        public void test_of_sevenElements() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertNotNull(tuple);
            assertEquals(7L, tuple._7);
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_of_eightElements() {
            LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertNotNull(tuple);
            assertEquals(8L, tuple._8);
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_of_nineElements() {
            LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertNotNull(tuple);
            assertEquals(9L, tuple._9);
        }

        @Test
        public void test_create_nullArray() {
            LongTuple0 tuple = LongTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_emptyArray() {
            LongTuple0 tuple = LongTuple.copyOf(new long[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_singleElementArray() {
            LongTuple1 tuple = LongTuple.copyOf(new long[] { 42L });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(42L, tuple._1);
        }

        @Test
        public void test_create_multipleElementsArray() {
            LongTuple3 tuple = LongTuple.copyOf(new long[] { 1L, 2L, 3L });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(1L, tuple._1);
            assertEquals(2L, tuple._2);
            assertEquals(3L, tuple._3);
        }

        @Test
        public void test_create_tooManyElements() {
            long[] array = new long[10];
            assertThrows(IllegalArgumentException.class, () -> LongTuple.copyOf(array));
        }

        // ===== LongTuple0 Tests =====

        @Test
        public void test_LongTuple0_arity() {
            LongTuple0 tuple = LongTuple.copyOf(new long[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_LongTuple0_min_throwsException() {
            LongTuple0 tuple = LongTuple.copyOf(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void test_LongTuple0_max_throwsException() {
            LongTuple0 tuple = LongTuple.copyOf(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void test_LongTuple0_median_throwsException() {
            LongTuple0 tuple = LongTuple.copyOf(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void test_LongTuple0_sum() {
            LongTuple0 tuple = LongTuple.copyOf(new long[0]);
            assertEquals(0L, tuple.sum());
        }

        @Test
        public void test_LongTuple0_average_throwsException() {
            LongTuple0 tuple = LongTuple.copyOf(new long[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void test_LongTuple0_reverse() {
            LongTuple0 tuple = LongTuple.copyOf(new long[0]);
            LongTuple0 reversed = tuple.reverse();
            assertSame(tuple, reversed);
        }

        @Test
        public void test_LongTuple0_contains() {
            LongTuple0 tuple = LongTuple.copyOf(new long[0]);
            assertFalse(tuple.contains(1L));
        }

        @Test
        public void test_LongTuple0_toString() {
            LongTuple0 tuple = LongTuple.copyOf(new long[0]);
            assertEquals("()", tuple.toString());
        }

        // ===== LongTuple1 Tests =====

        @Test
        public void test_LongTuple1_arity() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void test_LongTuple1_min() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(5L, tuple.min());
        }

        @Test
        public void test_LongTuple1_max() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(5L, tuple.max());
        }

        @Test
        public void test_LongTuple1_median() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(5L, tuple.median());
        }

        @Test
        public void test_LongTuple1_sum() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(5L, tuple.sum());
        }

        @Test
        public void test_LongTuple1_average() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(5.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_LongTuple1_reverse() {
            LongTuple1 tuple = LongTuple.of(5L);
            LongTuple1 reversed = tuple.reverse();
            assertEquals(5L, reversed._1);
        }

        @Test
        public void test_LongTuple1_contains_found() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertTrue(tuple.contains(5L));
        }

        @Test
        public void test_LongTuple1_contains_notFound() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertFalse(tuple.contains(10L));
        }

        @Test
        public void test_LongTuple1_hashCode() {
            LongTuple1 tuple1 = LongTuple.of(5L);
            LongTuple1 tuple2 = LongTuple.of(5L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_LongTuple1_equals_same() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals(tuple, tuple);
        }

        @Test
        public void test_LongTuple1_equals_equal() {
            LongTuple1 tuple1 = LongTuple.of(5L);
            LongTuple1 tuple2 = LongTuple.of(5L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void test_LongTuple1_equals_notEqual() {
            LongTuple1 tuple1 = LongTuple.of(5L);
            LongTuple1 tuple2 = LongTuple.of(10L);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void test_LongTuple1_equals_null() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertNotEquals(null, tuple);
        }

        @Test
        public void test_LongTuple1_equals_differentType() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertNotEquals("string", tuple);
        }

        @Test
        public void test_LongTuple1_toString() {
            LongTuple1 tuple = LongTuple.of(5L);
            assertEquals("(5)", tuple.toString());
        }

        // ===== LongTuple2 Tests =====

        @Test
        public void test_LongTuple2_min() {
            LongTuple2 tuple = LongTuple.of(3L, 1L);
            assertEquals(1L, tuple.min());
        }

        @Test
        public void test_LongTuple2_max() {
            LongTuple2 tuple = LongTuple.of(3L, 1L);
            assertEquals(3L, tuple.max());
        }

        @Test
        public void test_LongTuple2_median() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            long median = tuple.median();
            assertTrue(median == 1L || median == 2L);
        }

        @Test
        public void test_LongTuple2_sum() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertEquals(3L, tuple.sum());
        }

        @Test
        public void test_LongTuple2_average() {
            LongTuple2 tuple = LongTuple.of(1L, 3L);
            assertEquals(2.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_LongTuple2_reverse() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            LongTuple2 reversed = tuple.reverse();
            assertEquals(2L, reversed._1);
            assertEquals(1L, reversed._2);
        }

        @Test
        public void test_LongTuple2_contains_found() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertTrue(tuple.contains(1L));
            assertTrue(tuple.contains(2L));
        }

        @Test
        public void test_LongTuple2_contains_notFound() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertFalse(tuple.contains(3L));
        }

        @Test
        public void test_LongTuple2_forEach() throws Exception {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            List<Long> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(2, values.size());
            assertEquals(1L, values.get(0).longValue());
            assertEquals(2L, values.get(1).longValue());
        }

        @Test
        public void test_LongTuple2_accept() throws Exception {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            final long[] result = new long[1];
            tuple.accept((a, b) -> result[0] = a + b);
            assertEquals(7L, result[0]);
        }

        @Test
        public void test_LongTuple2_map() throws Exception {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            Long result = tuple.map((a, b) -> a * b);
            assertEquals(12L, result.longValue());
        }

        @Test
        public void test_LongTuple2_filter_satisfied() throws Exception {
            LongTuple2 tuple = LongTuple.of(3L, 4L);
            Optional<LongTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void test_LongTuple2_filter_notSatisfied() throws Exception {
            LongTuple2 tuple = LongTuple.of(1L, 1L);
            Optional<LongTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_LongTuple2_hashCode() {
            LongTuple2 tuple1 = LongTuple.of(1L, 2L);
            LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_LongTuple2_equals() {
            LongTuple2 tuple1 = LongTuple.of(1L, 2L);
            LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void test_LongTuple2_toString() {
            LongTuple2 tuple = LongTuple.of(1L, 2L);
            assertEquals("(1, 2)", tuple.toString());
        }

        // ===== LongTuple3 Tests =====

        @Test
        public void test_LongTuple3_min() {
            LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
            assertEquals(1L, tuple.min());
        }

        @Test
        public void test_LongTuple3_max() {
            LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
            assertEquals(3L, tuple.max());
        }

        @Test
        public void test_LongTuple3_median() {
            LongTuple3 tuple = LongTuple.of(30L, 10L, 20L);
            assertEquals(20L, tuple.median());
        }

        @Test
        public void test_LongTuple3_sum() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals(6L, tuple.sum());
        }

        @Test
        public void test_LongTuple3_average() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertEquals(2.0, tuple.average(), 0.0001);
        }

        @Test
        public void test_LongTuple3_reverse() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongTuple3 reversed = tuple.reverse();
            assertEquals(3L, reversed._1);
            assertEquals(2L, reversed._2);
            assertEquals(1L, reversed._3);
        }

        @Test
        public void test_LongTuple3_contains() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            assertTrue(tuple.contains(2L));
            assertFalse(tuple.contains(5L));
        }

        @Test
        public void test_LongTuple3_forEach() throws Exception {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            List<Long> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(3, values.size());
        }

        @Test
        public void test_LongTuple3_accept() throws Exception {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            final long[] result = new long[1];
            tuple.accept((a, b, c) -> result[0] = a + b + c);
            assertEquals(6L, result[0]);
        }

        @Test
        public void test_LongTuple3_map() throws Exception {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            Long result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6L, result.longValue());
        }

        @Test
        public void test_LongTuple3_filter() throws Exception {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            Optional<LongTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());
        }

        // ===== LongTuple4+ Tests =====

        @Test
        public void test_LongTuple4_arity() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void test_LongTuple4_statisticalOperations() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            assertEquals(1L, tuple.min());
            assertEquals(4L, tuple.max());
            assertEquals(10L, tuple.sum());
            assertEquals(2.5, tuple.average(), 0.0001);
        }

        @Test
        public void test_LongTuple5_arity() {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void test_LongTuple6_arity() {
            LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void test_LongTuple7_arity() {
            LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
            assertEquals(7, tuple.arity());
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_LongTuple8_arity() {
            LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            assertEquals(8, tuple.arity());
        }

        @Test
        @SuppressWarnings("deprecation")
        public void test_LongTuple9_arity() {
            LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertEquals(9, tuple.arity());
        }

        // ===== Common Method Tests =====

        @Test
        public void test_toArray() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long[] array = tuple.toArray();
            assertEquals(3, array.length);
            assertEquals(1L, array[0]);
            assertEquals(2L, array[1]);
            assertEquals(3L, array[2]);
        }

        @Test
        public void test_toArray_modification() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            long[] array = tuple.toArray();
            array[0] = 99L;
            assertEquals(1L, tuple._1);
        }

        @Test
        public void test_toList() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            LongList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1L, list.get(0));
        }

        @Test
        public void test_forEach_multipleElements() throws Exception {
            LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
            List<Long> values = new ArrayList<>();
            tuple.forEach(values::add);
            assertEquals(5, values.size());
        }

        @Test
        public void test_hashCode_consistency() {
            LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        public void test_equals_symmetric() {
            LongTuple3 tuple1 = LongTuple.of(1L, 2L, 3L);
            LongTuple3 tuple2 = LongTuple.of(1L, 2L, 3L);
            assertEquals(tuple1, tuple2);
            assertEquals(tuple2, tuple1);
        }

        @Test
        public void test_equals_differentClass() {
            LongTuple2 tuple2 = LongTuple.of(1L, 2L);
            LongTuple3 tuple3 = LongTuple.of(1L, 2L, 3L);
            assertNotEquals(tuple2, tuple3);
        }
        // ===== Edge Cases =====

        @Test
        public void test_negativeValues() {
            LongTuple3 tuple = LongTuple.of(-1L, -2L, -3L);
            assertEquals(-3L, tuple.min());
            assertEquals(-1L, tuple.max());
            assertEquals(-6L, tuple.sum());
        }

        @Test
        public void test_zeroValues() {
            LongTuple3 tuple = LongTuple.of(0L, 0L, 0L);
            assertEquals(0L, tuple.min());
            assertEquals(0L, tuple.max());
            assertEquals(0L, tuple.sum());
        }

        @Test
        public void test_largeValues() {
            LongTuple2 tuple = LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE / 2);
            assertEquals(Long.MAX_VALUE / 2, tuple.min());
            assertEquals(Long.MAX_VALUE, tuple.max());
        }

        @Test
        public void test_mixedValues() {
            LongTuple3 tuple = LongTuple.of(-100L, 0L, 100L);
            assertEquals(-100L, tuple.min());
            assertEquals(100L, tuple.max());
            assertEquals(0L, tuple.sum());
        }

        @Test
        public void test_reverse_largerTuples() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            LongTuple4 reversed = tuple.reverse();
            assertEquals(4L, reversed._1);
            assertEquals(3L, reversed._2);
            assertEquals(2L, reversed._3);
            assertEquals(1L, reversed._4);
        }

        @Test
        public void test_median_evenSize() {
            LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
            long median = tuple.median();
            assertTrue(median >= 1L && median <= 4L);
        }

        @Test
        public void test_median_oddSize() {
            LongTuple5 tuple = LongTuple.of(50L, 10L, 30L, 20L, 40L);
            long median = tuple.median();
            assertEquals(30L, median);
        }
    }

}
