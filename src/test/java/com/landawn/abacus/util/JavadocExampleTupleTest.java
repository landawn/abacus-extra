package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.util.u.Optional;

/**
 * Tests verifying Javadoc examples across all primitive Tuple classes.
 */
@SuppressWarnings("deprecation")
@Tag("2512")
public class JavadocExampleTupleTest {

    // ===== PrimitiveTuple Javadoc examples =====

    @Test
    public void testPrimitiveTupleArity() {
        // IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
        // int size = tuple2.arity();   // returns 2
        IntTuple.IntTuple2 tuple2 = IntTuple.of(10, 20);
        assertEquals(2, tuple2.arity());

        // IntTuple.IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
        // int size3 = tuple3.arity();   // returns 3
        IntTuple.IntTuple3 tuple3 = IntTuple.of(1, 2, 3);
        assertEquals(3, tuple3.arity());
    }

    @Test
    public void testPrimitiveTupleMap() {
        // Calculate Euclidean distance from origin
        // IntTuple.IntTuple2 point = IntTuple.of(3, 4);
        // Double distance = point.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
        // distance = 5.0
        IntTuple.IntTuple2 point = IntTuple.of(3, 4);
        Double distance = point.map(t -> Math.sqrt(t._1 * t._1 + t._2 * t._2));
        assertEquals(5.0, distance, 0.001);

        // Convert tuple to a formatted string
        // IntTuple.IntTuple3 rgb = IntTuple.of(255, 128, 0);
        // String hexColor = rgb.map(t -> String.format("#%02X%02X%02X", t._1, t._2, t._3));
        // hexColor = "#FF8000"
        IntTuple.IntTuple3 rgb = IntTuple.of(255, 128, 0);
        String hexColor = rgb.map(t -> String.format("#%02X%02X%02X", t._1, t._2, t._3));
        assertEquals("#FF8000", hexColor);

        // Extract a single computed value
        // DoubleTuple.DoubleTuple2 dimensions = DoubleTuple.of(10.5, 20.3);
        // Double area = dimensions.map(t -> t._1 * t._2);
        // area = 213.15
        DoubleTuple.DoubleTuple2 dimensions = DoubleTuple.of(10.5, 20.3);
        Double area = dimensions.map(t -> t._1 * t._2);
        assertEquals(213.15, area, 0.001);
    }

    @Test
    public void testPrimitiveTupleFilter() {
        // Filter for positive values
        // IntTuple.IntTuple2 tuple = IntTuple.of(5, 10);
        // positive.isPresent() = true
        IntTuple.IntTuple2 tuple = IntTuple.of(5, 10);
        Optional<IntTuple.IntTuple2> positive = tuple.filter(t -> t._1 > 0 && t._2 > 0);
        assertTrue(positive.isPresent());

        // Filter for negative values
        // negative.isPresent() = false
        Optional<IntTuple.IntTuple2> negative = tuple.filter(t -> t._1 < 0 || t._2 < 0);
        assertFalse(negative.isPresent());

        // Chain with other operations
        // IntTuple.IntTuple3 values = IntTuple.of(10, 20, 30);
        // result = "Sum is: 60"
        IntTuple.IntTuple3 values = IntTuple.of(10, 20, 30);
        String result = values.filter(t -> t._1 + t._2 + t._3 > 50).map(t -> "Sum is: " + (t._1 + t._2 + t._3)).orElse("Sum too small");
        assertEquals("Sum is: 60", result);
    }

    @Test
    public void testPrimitiveTupleToOptional() {
        // IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        // optional.isPresent() = true
        IntTuple.IntTuple2 tuple = IntTuple.of(1, 2);
        Optional<IntTuple.IntTuple2> optional = tuple.toOptional();
        assertTrue(optional.isPresent());
    }

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

    // ===== DoubleTuple Javadoc examples =====

    @Test
    public void testDoubleTupleOf1() {
        // DoubleTuple.DoubleTuple1 single = DoubleTuple.of(3.14);
        // double value = single._1;  // 3.14
        DoubleTuple.DoubleTuple1 single = DoubleTuple.of(3.14);
        assertEquals(3.14, single._1, 0.001);
    }

    @Test
    public void testDoubleTupleOf2() {
        // DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
        // double first = pair._1;  // 1.5
        // double second = pair._2;  // 2.5
        DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
        assertEquals(1.5, pair._1, 0.001);
        assertEquals(2.5, pair._2, 0.001);
    }

    @Test
    public void testDoubleTupleOf3() {
        // DoubleTuple.DoubleTuple3 triple = DoubleTuple.of(1.0, 2.0, 3.0);
        // double third = triple._3;  // 3.0
        DoubleTuple.DoubleTuple3 triple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(3.0, triple._3, 0.001);
    }

    @Test
    public void testDoubleTupleOf4() {
        // DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        // double fourth = tuple._4;  // 4.0
        DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(4.0, tuple._4, 0.001);
    }

    @Test
    public void testDoubleTupleClassLevelExamples() {
        // double min = triple.min();   // 1.0
        // double max = triple.max();   // 3.0
        // double avg = triple.average();   // 2.0
        DoubleTuple.DoubleTuple3 triple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(1.0, triple.min(), 0.001);
        assertEquals(3.0, triple.max(), 0.001);
        assertEquals(2.0, triple.average(), 0.001);
    }

    @Test
    public void testDoubleTupleOf5Median() {
        // DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        // double median = tuple.median();   // 3.0
        DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertEquals(3.0, tuple.median(), 0.001);
    }

    @Test
    public void testDoubleTupleOf6Sum() {
        // DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        // double sum = tuple.sum();   // 21.0
        DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        assertEquals(21.0, tuple.sum(), 0.001);
    }

    @Test
    public void testDoubleTupleMin() {
        // DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        // double min = tuple.min();   // 1.0
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(1.0, tuple.min(), 0.001);

        // DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(2.5, 1.5);
        // double minPair = pair.min();   // 1.5
        DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(2.5, 1.5);
        assertEquals(1.5, pair.min(), 0.001);
    }

    @Test
    public void testDoubleTupleMap2() {
        // DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        // double product = tuple.map((a, b) -> a * b);   // 12.0
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        double product = tuple.map((a, b) -> a * b);
        assertEquals(12.0, product, 0.001);

        // DoubleTuple.DoubleTuple2 point = DoubleTuple.of(3.0, 4.0);
        // Double distance = point.map((x, y) -> Math.sqrt(x * x + y * y));   // 5.0
        DoubleTuple.DoubleTuple2 point = DoubleTuple.of(3.0, 4.0);
        Double distance = point.map((x, y) -> Math.sqrt(x * x + y * y));
        assertEquals(5.0, distance, 0.001);
    }

    @Test
    public void testDoubleTupleMap3() {
        // DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        // double product = tuple.map((a, b, c) -> a * b * c);   // 6.0
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double productVal = tuple.map((a, b, c) -> a * b * c);
        assertEquals(6.0, productVal, 0.001);

        // DoubleTuple.DoubleTuple3 point = DoubleTuple.of(1.0, 2.0, 2.0);
        // Double distance = point.map((x, y, z) -> Math.sqrt(x*x + y*y + z*z));   // 3.0
        DoubleTuple.DoubleTuple3 point = DoubleTuple.of(1.0, 2.0, 2.0);
        Double dist = point.map((x, y, z) -> Math.sqrt(x * x + y * y + z * z));
        assertEquals(3.0, dist, 0.001);
    }

    @Test
    public void testDoubleTupleFilter2() {
        // DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        // result = tuple.filter((a, b) -> a + b > 5);
        // Returns: Optional containing tuple (since 3.0 + 4.0 = 7.0 > 5)
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        Optional<DoubleTuple.DoubleTuple2> result = tuple.filter((a, b) -> a + b > 5);
        assertTrue(result.isPresent());

        // DoubleTuple.DoubleTuple2 small = DoubleTuple.of(1.0, 2.0);
        // empty = small.filter((a, b) -> a + b > 10);
        // Returns: Optional.empty()
        DoubleTuple.DoubleTuple2 small = DoubleTuple.of(1.0, 2.0);
        Optional<DoubleTuple.DoubleTuple2> empty = small.filter((a, b) -> a + b > 10);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDoubleTupleFilter3() {
        // DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        // result = tuple.filter((a, b, c) -> a + b + c > 5);
        // Returns: Optional containing tuple
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        Optional<DoubleTuple.DoubleTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
        assertTrue(result.isPresent());

        // DoubleTuple.DoubleTuple3 small = DoubleTuple.of(1.0, 1.0, 1.0);
        // empty = small.filter((a, b, c) -> a + b + c > 10);
        // Returns: Optional.empty()
        DoubleTuple.DoubleTuple3 small = DoubleTuple.of(1.0, 1.0, 1.0);
        Optional<DoubleTuple.DoubleTuple3> empty = small.filter((a, b, c) -> a + b + c > 10);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDoubleTupleReverse() {
        // DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
        // DoubleTuple.DoubleTuple2 reversed = tuple.reverse();   // (4.0, 3.0)
        DoubleTuple.DoubleTuple2 tuple2 = DoubleTuple.of(3.0, 4.0);
        DoubleTuple.DoubleTuple2 reversed2 = tuple2.reverse();
        assertEquals(4.0, reversed2._1, 0.001);
        assertEquals(3.0, reversed2._2, 0.001);

        // DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        // DoubleTuple.DoubleTuple3 reversed = tuple.reverse();   // (3.0, 2.0, 1.0)
        DoubleTuple.DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 reversed3 = tuple3.reverse();
        assertEquals(3.0, reversed3._1, 0.001);
        assertEquals(2.0, reversed3._2, 0.001);
        assertEquals(1.0, reversed3._3, 0.001);
    }

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

    // ===== BooleanTuple Javadoc examples =====

    @Test
    public void testBooleanTupleOf1() {
        // BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
        // boolean value = tuple._1;  // true
        BooleanTuple.BooleanTuple1 t = BooleanTuple.of(true);
        assertTrue(t._1);
    }

    @Test
    public void testBooleanTupleOf2() {
        // BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
        // boolean first = tuple._1;  // true
        // boolean second = tuple._2;  // false
        BooleanTuple.BooleanTuple2 t = BooleanTuple.of(true, false);
        assertTrue(t._1);
        assertFalse(t._2);
    }

    @Test
    public void testBooleanTupleOf3() {
        // BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
        // boolean third = tuple._3;  // true
        BooleanTuple.BooleanTuple3 t = BooleanTuple.of(true, false, true);
        assertTrue(t._3);
    }

    @Test
    public void testBooleanTupleOf4Reverse() {
        // BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
        // boolean first = tuple._1;  // true
        // boolean fourth = tuple._4;  // false
        // BooleanTuple.BooleanTuple4 reversed = tuple.reverse();   // (false, true, false, true)
        BooleanTuple.BooleanTuple4 t = BooleanTuple.of(true, false, true, false);
        assertTrue(t._1);
        assertFalse(t._4);
        BooleanTuple.BooleanTuple4 reversed = t.reverse();
        assertFalse(reversed._1);
        assertTrue(reversed._2);
        assertFalse(reversed._3);
        assertTrue(reversed._4);
    }

    @Test
    public void testBooleanTupleOf5Contains() {
        // BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
        // boolean first = tuple._1;  // true
        // boolean fifth = tuple._5;  // true
        // boolean contains = tuple.contains(false);   // true
        BooleanTuple.BooleanTuple5 t = BooleanTuple.of(true, false, true, false, true);
        assertTrue(t._1);
        assertTrue(t._5);
        assertTrue(t.contains(false));
    }

    @Test
    public void testBooleanTupleOf9Arity() {
        // BooleanTuple.BooleanTuple9 tuple = ...
        // boolean first = tuple._1;  // true
        // boolean ninth = tuple._9;  // true
        // int arity = tuple.arity();   // 9
        BooleanTuple.BooleanTuple9 t = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
        assertTrue(t._1);
        assertTrue(t._9);
        assertEquals(9, t.arity());
    }

    @Test
    public void testBooleanTupleClassLevelMap() {
        // boolean result = triple.map((a, b, c) -> a || b || c);
        BooleanTuple.BooleanTuple3 triple = BooleanTuple.of(true, false, true);
        boolean result = triple.map((a, b, c) -> a || b || c);
        assertTrue(result);
    }

    @Test
    public void testBooleanTupleClassLevelFilter() {
        // u.Optional<BooleanTuple.BooleanTuple2> filtered = pair.filter((a, b) -> a != b);
        BooleanTuple.BooleanTuple2 pair = BooleanTuple.of(true, false);
        Optional<BooleanTuple.BooleanTuple2> filtered = pair.filter((a, b) -> a != b);
        assertTrue(filtered.isPresent());
    }

    // ===== CharTuple Javadoc examples =====

    @Test
    public void testCharTupleOf1() {
        // CharTuple.CharTuple1 tuple = CharTuple.of('A');
        // char value = tuple._1;  // 'A'
        CharTuple.CharTuple1 t = CharTuple.of('A');
        assertEquals('A', t._1);
    }

    @Test
    public void testCharTupleOf2() {
        // CharTuple.CharTuple2 tuple = CharTuple.of('A', 'B');
        // char first = tuple._1;  // 'A'
        // char second = tuple._2;  // 'B'
        CharTuple.CharTuple2 t = CharTuple.of('A', 'B');
        assertEquals('A', t._1);
        assertEquals('B', t._2);
    }

    @Test
    public void testCharTupleOf3() {
        // CharTuple.CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
        // char third = tuple._3;  // 'C'
        CharTuple.CharTuple3 t = CharTuple.of('A', 'B', 'C');
        assertEquals('C', t._3);
    }

    @Test
    public void testCharTupleOf4() {
        // CharTuple.CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
        // char fourth = tuple._4;  // 'D'
        CharTuple.CharTuple4 t = CharTuple.of('A', 'B', 'C', 'D');
        assertEquals('D', t._4);
    }

    @Test
    public void testCharTupleClassLevelExamples() {
        // char min = triple.min();   // 'A'
        // char max = triple.max();   // 'C'
        // double avg = triple.average();   // 66.0 (average of ASCII values)
        CharTuple.CharTuple3 triple = CharTuple.of('A', 'B', 'C');
        assertEquals('A', triple.min());
        assertEquals('C', triple.max());
        assertEquals(66.0, triple.average(), 0.001);
    }

    @Test
    public void testCharTupleOf5Median() {
        // CharTuple.CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
        // char median = tuple.median();   // 'C'
        CharTuple.CharTuple5 t = CharTuple.of('A', 'B', 'C', 'D', 'E');
        assertEquals('C', t.median());
    }

    @Test
    public void testCharTupleOf8Contains() {
        // CharTuple.CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
        // boolean hasX = tuple.contains('X');   // false
        CharTuple.CharTuple8 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
        assertFalse(t.contains('X'));
    }

    @Test
    public void testCharTupleOf9ToArray() {
        // CharTuple.CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
        // char[] array = tuple.toArray();   // ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I']
        CharTuple.CharTuple9 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
        assertArrayEquals(new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' }, t.toArray());
    }

    @Test
    public void testCharTupleReverse() {
        // CharTuple.CharTuple7 tuple = CharTuple.of('M', 'O', 'N', 'D', 'A', 'Y', 'S');
        // CharTuple.CharTuple7 reversed = tuple.reverse();   // ('S', 'Y', 'A', 'D', 'N', 'O', 'M')
        CharTuple.CharTuple7 t = CharTuple.of('M', 'O', 'N', 'D', 'A', 'Y', 'S');
        CharTuple.CharTuple7 reversed = t.reverse();
        assertEquals('S', reversed._1);
        assertEquals('Y', reversed._2);
        assertEquals('A', reversed._3);
        assertEquals('D', reversed._4);
        assertEquals('N', reversed._5);
        assertEquals('O', reversed._6);
        assertEquals('M', reversed._7);
    }
}
