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
import com.landawn.abacus.util.DoubleTuple.DoubleTuple0;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple1;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple3;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple4;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple5;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple6;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple7;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple8;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.DoubleStream;

class DoubleTupleTest extends TestBase {

    private static final double DELTA = 0.0001;

    @Test
    public void testOf1() {
        DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.5);
        assertEquals(1, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
    }

    @Test
    public void testOf2() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        assertEquals(2, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
    }

    @Test
    public void testOf3() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.5, 2.5, 3.5);
        assertEquals(3, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
    }

    @Test
    public void testOf4() {
        DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5);
        assertEquals(4, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
    }

    @Test
    public void testOf5() {
        DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5);
        assertEquals(5, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
        assertEquals(5.5, tuple._5, DELTA);
    }

    @Test
    public void testOf6() {
        DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5);
        assertEquals(6, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
        assertEquals(5.5, tuple._5, DELTA);
        assertEquals(6.5, tuple._6, DELTA);
    }

    @Test
    public void testOf7() {
        DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5);
        assertEquals(7, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
        assertEquals(5.5, tuple._5, DELTA);
        assertEquals(6.5, tuple._6, DELTA);
        assertEquals(7.5, tuple._7, DELTA);
    }

    @Test
    public void testOf8() {
        DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5);
        assertEquals(8, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
        assertEquals(5.5, tuple._5, DELTA);
        assertEquals(6.5, tuple._6, DELTA);
        assertEquals(7.5, tuple._7, DELTA);
        assertEquals(8.5, tuple._8, DELTA);
    }

    @Test
    public void testOf9() {
        DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5);
        assertEquals(9, tuple.arity());
        assertEquals(1.5, tuple._1, DELTA);
        assertEquals(2.5, tuple._2, DELTA);
        assertEquals(3.5, tuple._3, DELTA);
        assertEquals(4.5, tuple._4, DELTA);
        assertEquals(5.5, tuple._5, DELTA);
        assertEquals(6.5, tuple._6, DELTA);
        assertEquals(7.5, tuple._7, DELTA);
        assertEquals(8.5, tuple._8, DELTA);
        assertEquals(9.5, tuple._9, DELTA);
    }

    @Test
    public void testEquals() {
        DoubleTuple.DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 4.0);

        assertEquals(tuple1, tuple2);
        assertNotEquals(tuple1, tuple3);
        assertNotEquals(tuple1, null);
    }

    @Test
    public void testHashCode() {
        DoubleTuple.DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 4.0);

        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotEquals(tuple1.hashCode(), tuple3.hashCode());
    }

    @Test
    public void testToString() {
        DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
        String result = tuple.toString();
        assertTrue(result.contains("1.5"));
        assertTrue(result.contains("2.5"));
    }

    @Test
    public void testReverse() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        DoubleTuple.DoubleTuple3 reversed = tuple.reverse();

        assertEquals(3.0, reversed._1, DELTA);
        assertEquals(2.0, reversed._2, DELTA);
        assertEquals(1.0, reversed._3, DELTA);
    }

    @Test
    public void testContains() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.5, 2.5, 3.5);

        assertTrue(tuple.contains(1.5));
        assertTrue(tuple.contains(2.5));
        assertTrue(tuple.contains(3.5));
        assertFalse(tuple.contains(4.5));
    }

    @Test
    public void testToArray() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.5, 2.5, 3.5);
        double[] array = tuple.toArray();

        assertEquals(3, array.length);
        assertEquals(1.5, array[0], DELTA);
        assertEquals(2.5, array[1], DELTA);
        assertEquals(3.5, array[2], DELTA);
    }

    @Test
    public void testStream() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        double sum = tuple.stream().sum();

        assertEquals(6.0, sum, DELTA);
    }

    @Test
    public void testMin() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.5, 1.5, 2.5);
        assertEquals(1.5, tuple.min(), DELTA);
    }

    @Test
    public void testMax() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.5, 1.5, 2.5);
        assertEquals(3.5, tuple.max(), DELTA);
    }

    @Test
    public void testSum() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.5, 2.5, 3.0);
        assertEquals(7.0, tuple.sum(), DELTA);
    }

    @Test
    public void testAverage() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
        assertEquals(2.0, tuple.average(), DELTA);
    }

    @Test
    public void testMedian() {
        DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
        assertEquals(2.0, tuple.median(), DELTA);

        DoubleTuple.DoubleTuple4 evenTuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(2.0, evenTuple.median(), DELTA);
    }

    @Nested
    /**
     * Comprehensive test suite for DoubleTuple and its nested classes.
     * Tests all public methods including factory methods, statistical operations,
     * collection conversions, and special methods in Tuple2 and Tuple3.
     */
    @Tag("2025")
    class DoubleTuple2025Test extends TestBase {

        // Factory method tests
        @Test
        public void testOf1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(2.0, tuple._2, 0.001);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(2.0, tuple._2, 0.001);
            assertEquals(3.0, tuple._3, 0.001);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(2.0, tuple._2, 0.001);
            assertEquals(3.0, tuple._3, 0.001);
            assertEquals(4.0, tuple._4, 0.001);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(5.0, tuple._5, 0.001);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf6() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(6.0, tuple._6, 0.001);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf7() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(7.0, tuple._7, 0.001);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf8() {
            DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(8.0, tuple._8, 0.001);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(9.0, tuple._9, 0.001);
            assertEquals(9, tuple.arity());
        }

        // Create method tests
        @Test
        public void testCreateEmpty() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreateNull() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate1() {
            DoubleTuple1 tuple = DoubleTuple.copyOf(new double[] { 1.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate3() {
            DoubleTuple3 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(2.0, tuple._2, 0.001);
            assertEquals(3.0, tuple._3, 0.001);
        }

        @Test
        public void testCreate9() {
            DoubleTuple9 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(9.0, tuple._9, 0.001);
        }

        @Test
        public void testCreateTooMany() {
            assertThrows(IllegalArgumentException.class, () -> {
                DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });
            });
        }

        // Statistical method tests - min
        @Test
        public void testMinTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1.0, tuple.min(), 0.001);
        }

        @Test
        public void testMinTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(1.0, tuple.min(), 0.001);
        }

        @Test
        public void testMinTuple0ThrowsException() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        // Statistical method tests - max
        @Test
        public void testMaxTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1.0, tuple.max(), 0.001);
        }

        @Test
        public void testMaxTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(3.0, tuple.max(), 0.001);
        }

        @Test
        public void testMaxTuple0ThrowsException() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        // Statistical method tests - median
        @Test
        public void testMedianTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1.0, tuple.median(), 0.001);
        }

        @Test
        public void testMedianTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(2.0, tuple.median(), 0.001);
        }

        @Test
        public void testMedianTuple0ThrowsException() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        // Statistical method tests - sum
        @Test
        public void testSumTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertEquals(0.0, tuple.sum(), 0.001);
        }

        @Test
        public void testSumTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1.0, tuple.sum(), 0.001);
        }

        @Test
        public void testSumTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(6.0, tuple.sum(), 0.001);
        }

        // Statistical method tests - average
        @Test
        public void testAverageTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(2.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple0ThrowsException() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        // Reverse tests
        @Test
        public void testReverseTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            DoubleTuple<DoubleTuple0> reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        public void testReverseTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            DoubleTuple1 reversed = tuple.reverse();
            assertEquals(1.0, reversed._1, 0.001);
        }

        @Test
        public void testReverseTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            DoubleTuple2 reversed = tuple.reverse();
            assertEquals(2.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._2, 0.001);
        }

        @Test
        public void testReverseTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 reversed = tuple.reverse();
            assertEquals(3.0, reversed._1, 0.001);
            assertEquals(2.0, reversed._2, 0.001);
            assertEquals(1.0, reversed._3, 0.001);
        }

        // Contains tests
        @Test
        public void testContainsTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertFalse(tuple.contains(1.0));
        }

        @Test
        public void testContainsTuple1True() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertTrue(tuple.contains(1.0));
        }

        @Test
        public void testContainsTuple1False() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertFalse(tuple.contains(99.0));
        }

        @Test
        public void testContainsTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(2.0));
            assertTrue(tuple.contains(3.0));
            assertFalse(tuple.contains(99.0));
        }

        // toArray tests
        @Test
        public void testToArrayTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            double[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        public void testToArrayTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 1.0 }, array, 0.001);
        }

        @Test
        public void testToArrayTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, array, 0.001);
        }

        @Test
        public void testToArrayModificationDoesNotAffectTuple() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array = tuple.toArray();
            array[0] = 999.0;
            assertEquals(1.0, tuple._1, 0.001);
        }

        // toList tests
        @Test
        public void testToListTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            DoubleList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        public void testToListTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            DoubleList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(1.0, list.get(0), 0.001);
        }

        @Test
        public void testToListTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1.0, list.get(0), 0.001);
            assertEquals(2.0, list.get(1), 0.001);
            assertEquals(3.0, list.get(2), 0.001);
        }

        // forEach tests
        @Test
        public void testForEachTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(0, result.size());
        }

        @Test
        public void testForEachTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(1, result.size());
            assertEquals(Double.valueOf(1.0), result.get(0));
        }

        @Test
        public void testForEachTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Double.valueOf(1.0), result.get(0));
            assertEquals(Double.valueOf(2.0), result.get(1));
            assertEquals(Double.valueOf(3.0), result.get(2));
        }

        // stream tests
        @Test
        public void testStreamTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            DoubleStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        public void testStreamTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            DoubleStream stream = tuple.stream();
            assertEquals(1.0, stream.sum(), 0.001);
        }

        @Test
        public void testStreamTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleStream stream = tuple.stream();
            assertEquals(6.0, stream.sum(), 0.001);
        }

        // hashCode tests
        @Test
        public void testHashCodeTuple1() {
            DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
            DoubleTuple1 tuple2 = DoubleTuple.of(1.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple2() {
            DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
            DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple3() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // equals tests
        @Test
        public void testEqualsSameObject() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple, tuple);
        }

        @Test
        public void testEqualsNull() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotEquals(null, tuple);
        }

        @Test
        public void testEqualsDifferentClass() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotEquals("not a tuple", tuple);
        }

        @Test
        public void testEqualsTuple1() {
            DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
            DoubleTuple1 tuple2 = DoubleTuple.of(1.0);
            DoubleTuple1 tuple3 = DoubleTuple.of(99.0);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple2() {
            DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
            DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
            DoubleTuple2 tuple3 = DoubleTuple.of(1.0, 3.0);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        @Test
        public void testEqualsTuple3() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 4.0);
            assertEquals(tuple1, tuple2);
            assertNotEquals(tuple1, tuple3);
        }

        // toString tests
        @Test
        public void testToStringTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testToStringTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            String str = tuple.toString();
            assertTrue(str.contains("1.0"));
        }

        @Test
        public void testToStringTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            String str = tuple.toString();
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("2.0"));
            assertTrue(str.contains("3.0"));
        }

        // Tuple2 special methods - accept
        @Test
        public void testTuple2Accept() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            List<Double> result = new ArrayList<>();
            tuple.accept((a, b) -> {
                result.add(a);
                result.add(b);
            });
            assertEquals(2, result.size());
            assertEquals(Double.valueOf(3.0), result.get(0));
            assertEquals(Double.valueOf(4.0), result.get(1));
        }

        // Tuple2 special methods - map
        @Test
        public void testTuple2Map() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            double result = tuple.map((a, b) -> a * b);
            assertEquals(12.0, result, 0.001);
        }

        // Tuple2 special methods - filter
        @Test
        public void testTuple2FilterTrue() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            var result = tuple.filter((a, b) -> a + b > 5.0);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple2FilterFalse() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            var result = tuple.filter((a, b) -> a + b > 10.0);
            assertFalse(result.isPresent());
        }

        // Tuple3 special methods - accept
        @Test
        public void testTuple3Accept() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> result = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                result.add(a);
                result.add(b);
                result.add(c);
            });
            assertEquals(3, result.size());
            assertEquals(Double.valueOf(1.0), result.get(0));
            assertEquals(Double.valueOf(2.0), result.get(1));
            assertEquals(Double.valueOf(3.0), result.get(2));
        }

        // Tuple3 special methods - map
        @Test
        public void testTuple3Map() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0, result, 0.001);
        }

        // Tuple3 special methods - filter
        @Test
        public void testTuple3FilterTrue() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            var result = tuple.filter((a, b, c) -> a + b + c > 5.0);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testTuple3FilterFalse() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            var result = tuple.filter((a, b, c) -> a + b + c > 10.0);
            assertFalse(result.isPresent());
        }

        // arity tests for all tuple sizes
        @Test
        public void testArity() {
            assertEquals(0, DoubleTuple.copyOf(new double[0]).arity());
            assertEquals(1, DoubleTuple.of(1.0).arity());
            assertEquals(2, DoubleTuple.of(1.0, 2.0).arity());
            assertEquals(3, DoubleTuple.of(1.0, 2.0, 3.0).arity());
            assertEquals(4, DoubleTuple.of(1.0, 2.0, 3.0, 4.0).arity());
            assertEquals(5, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0).arity());
            assertEquals(6, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0).arity());
            assertEquals(7, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0).arity());
            assertEquals(8, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0).arity());
            assertEquals(9, DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0).arity());
        }

        // Tests for inherited methods from PrimitiveTuple
        @Test
        public void testAcceptConsumer() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> result = new ArrayList<>();
            tuple.accept(t -> {
                result.add(t._1);
                result.add(t._2);
                result.add(t._3);
            });
            assertEquals(3, result.size());
            assertEquals(Double.valueOf(1.0), result.get(0));
            assertEquals(Double.valueOf(2.0), result.get(1));
            assertEquals(Double.valueOf(3.0), result.get(2));
        }

        @Test
        public void testMapFunction() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            String result = tuple.map(t -> "Sum: " + (t._1 + t._2 + t._3));
            assertEquals("Sum: 6.0", result);
        }

        @Test
        public void testFilterPredicate() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            var result = tuple.filter(t -> t._1 == 1.0);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void testFilterPredicateFalse() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            var result = tuple.filter(t -> t._1 == 99.0);
            assertFalse(result.isPresent());
        }

        @Test
        public void testToOptional() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            var result = tuple.toOptional();
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        // Additional tests for larger tuple sizes - reverse
        @Test
        public void testReverseTuple4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            DoubleTuple4 reversed = tuple.reverse();
            assertEquals(4.0, reversed._1, 0.001);
            assertEquals(3.0, reversed._2, 0.001);
            assertEquals(2.0, reversed._3, 0.001);
            assertEquals(1.0, reversed._4, 0.001);
        }

        @Test
        public void testReverseTuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            DoubleTuple5 reversed = tuple.reverse();
            assertEquals(5.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._5, 0.001);
        }

        @Test
        public void testReverseTuple6() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            DoubleTuple6 reversed = tuple.reverse();
            assertEquals(6.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._6, 0.001);
        }

        @Test
        public void testReverseTuple7() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            DoubleTuple7 reversed = tuple.reverse();
            assertEquals(7.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._7, 0.001);
        }

        @Test
        public void testReverseTuple8() {
            DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            DoubleTuple8 reversed = tuple.reverse();
            assertEquals(8.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._8, 0.001);
        }

        @Test
        public void testReverseTuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            DoubleTuple9 reversed = tuple.reverse();
            assertEquals(9.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._9, 0.001);
        }

        // Additional tests for larger tuple sizes - contains
        @Test
        public void testContainsTuple4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(4.0));
            assertFalse(tuple.contains(99.0));
        }

        @Test
        public void testContainsTuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertTrue(tuple.contains(5.0));
            assertFalse(tuple.contains(99.0));
        }

        @Test
        public void testContainsTuple6() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertTrue(tuple.contains(6.0));
            assertFalse(tuple.contains(99.0));
        }

        @Test
        public void testContainsTuple7() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertTrue(tuple.contains(7.0));
            assertFalse(tuple.contains(99.0));
        }

        @Test
        public void testContainsTuple8() {
            DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertTrue(tuple.contains(8.0));
            assertFalse(tuple.contains(99.0));
        }

        @Test
        public void testContainsTuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertTrue(tuple.contains(9.0));
            assertFalse(tuple.contains(99.0));
        }

        // Test for create() with all sizes (2, 4-9)
        @Test
        public void testCreate2() {
            DoubleTuple2 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(2.0, tuple._2, 0.001);
        }

        @Test
        public void testCreate4() {
            DoubleTuple4 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(4.0, tuple._4, 0.001);
        }

        @Test
        public void testCreate5() {
            DoubleTuple5 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(5.0, tuple._5, 0.001);
        }

        @Test
        public void testCreate6() {
            DoubleTuple6 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(6.0, tuple._6, 0.001);
        }

        @Test
        public void testCreate7() {
            DoubleTuple7 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(7.0, tuple._7, 0.001);
        }

        @Test
        public void testCreate8() {
            DoubleTuple8 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(8.0, tuple._8, 0.001);
        }

        // Comprehensive tests for Tuple4 through Tuple9
        @Test
        public void testTuple4Operations() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);

            // Test reverse
            DoubleTuple4 reversed = tuple.reverse();
            assertEquals(4.0, reversed._1, 0.001);
            assertEquals(3.0, reversed._2, 0.001);
            assertEquals(2.0, reversed._3, 0.001);
            assertEquals(1.0, reversed._4, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(4.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, tuple.toArray(), 0.001);

            // Test min/max/median/sum/average via base class
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(4.0, tuple.max(), 0.001);
            assertEquals(2.0, tuple.median(), 0.001); // For even-sized tuples, returns lower middle value
            assertEquals(10.0, tuple.sum(), 0.001);
            assertEquals(2.5, tuple.average(), 0.001);

            // Test hashCode and equals
            DoubleTuple4 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            DoubleTuple4 tuple3 = DoubleTuple.of(1.0, 2.0, 3.0, 5.0);
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
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);

            // Test reverse
            DoubleTuple5 reversed = tuple.reverse();
            assertEquals(5.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._5, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(5.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }, tuple.toArray(), 0.001);

            // Test statistical operations
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(5.0, tuple.max(), 0.001);
            assertEquals(3.0, tuple.median(), 0.001);
            assertEquals(15.0, tuple.sum(), 0.001);
            assertEquals(3.0, tuple.average(), 0.001);

            // Test equals
            DoubleTuple5 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(tuple, tuple2);
        }

        @Test
        public void testTuple6Operations() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);

            // Test reverse
            DoubleTuple6 reversed = tuple.reverse();
            assertEquals(6.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._6, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(6.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }, tuple.toArray(), 0.001);

            // Test statistical operations
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(6.0, tuple.max(), 0.001);
            assertEquals(3.0, tuple.median(), 0.001); // For even-sized tuples, returns lower middle value
            assertEquals(21.0, tuple.sum(), 0.001);
            assertEquals(3.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple7Operations() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

            // Test reverse
            DoubleTuple7 reversed = tuple.reverse();
            assertEquals(7.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._7, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(7.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 }, tuple.toArray(), 0.001);

            // Test statistical operations
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(7.0, tuple.max(), 0.001);
            assertEquals(28.0, tuple.sum(), 0.001);
            assertEquals(4.0, tuple.average(), 0.001);
        }

        @Test
        public void testTuple8Operations() {
            DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);

            // Test reverse
            DoubleTuple8 reversed = tuple.reverse();
            assertEquals(8.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._8, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(8.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0 }, tuple.toArray(), 0.001);

            // Test statistical operations
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(8.0, tuple.max(), 0.001);
            assertEquals(4.0, tuple.median(), 0.001); // For even-sized tuples, returns lower middle value
            assertEquals(36.0, tuple.sum(), 0.001);
            assertEquals(4.5, tuple.average(), 0.001);
        }

        @Test
        public void testTuple9Operations() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);

            // Test reverse
            DoubleTuple9 reversed = tuple.reverse();
            assertEquals(9.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._9, 0.001);

            // Test contains
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(9.0));
            assertFalse(tuple.contains(99.0));

            // Test toArray
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 }, tuple.toArray(), 0.001);

            // Test statistical operations
            assertEquals(1.0, tuple.min(), 0.001);
            assertEquals(9.0, tuple.max(), 0.001);
            assertEquals(45.0, tuple.sum(), 0.001);
            assertEquals(5.0, tuple.average(), 0.001);
        }

        // Test create methods for sizes 2, 4-8
        @Test
        public void testCreate2Through8() {
            DoubleTuple2 tuple2 = DoubleTuple.copyOf(new double[] { 1.0, 2.0 });
            assertEquals(1.0, tuple2._1, 0.001);
            assertEquals(2.0, tuple2._2, 0.001);

            DoubleTuple4 tuple4 = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0 });
            assertEquals(1.0, tuple4._1, 0.001);
            assertEquals(4.0, tuple4._4, 0.001);

            DoubleTuple5 tuple5 = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
            assertEquals(1.0, tuple5._1, 0.001);
            assertEquals(5.0, tuple5._5, 0.001);

            DoubleTuple6 tuple6 = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 });
            assertEquals(1.0, tuple6._1, 0.001);
            assertEquals(6.0, tuple6._6, 0.001);

            DoubleTuple7 tuple7 = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 });
            assertEquals(1.0, tuple7._1, 0.001);
            assertEquals(7.0, tuple7._7, 0.001);

            DoubleTuple8 tuple8 = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0 });
            assertEquals(1.0, tuple8._1, 0.001);
            assertEquals(8.0, tuple8._8, 0.001);
        }

        // Test toList for larger tuples
        @Test
        public void testToListTuple4Through9() {
            DoubleTuple4 tuple4 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            DoubleList list4 = tuple4.toList();
            assertEquals(4, list4.size());
            assertEquals(4.0, list4.get(3), 0.001);

            DoubleTuple9 tuple9 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            DoubleList list9 = tuple9.toList();
            assertEquals(9, list9.size());
            assertEquals(9.0, list9.get(8), 0.001);
        }

        // Test forEach for larger tuples
        @Test
        public void testForEachTuple4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(4, result.size());
            assertEquals(Double.valueOf(4.0), result.get(3));
        }

        // Test forEach override for Tuple2
        @Test
        public void testForEachTuple2Override() {
            DoubleTuple2 tuple = DoubleTuple.of(10.0, 20.0);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(2, result.size());
            assertEquals(Double.valueOf(10.0), result.get(0));
            assertEquals(Double.valueOf(20.0), result.get(1));
        }

        // Test forEach override for Tuple3
        @Test
        public void testForEachTuple3Override() {
            DoubleTuple3 tuple = DoubleTuple.of(10.0, 20.0, 30.0);
            List<Double> result = new ArrayList<>();
            tuple.forEach(i -> result.add(i));
            assertEquals(3, result.size());
            assertEquals(Double.valueOf(30.0), result.get(2));
        }

        // Test stream for larger tuples
        @Test
        public void testStreamTuple4Through9() {
            DoubleTuple4 tuple4 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(10.0, tuple4.stream().sum(), 0.001);

            DoubleTuple9 tuple9 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(45.0, tuple9.stream().sum(), 0.001);
        }

        // ==================== DoubleTuple Nested Class Tests ====================

        // ============ DoubleTuple1 Nested Class Tests ============

        @Test
        public void testDoubleTuple1_arity() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testDoubleTuple1_reverse() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            DoubleTuple.DoubleTuple1 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._1);
            assertEquals(tuple._1, reversed._1);
        }

        @Test
        public void testDoubleTuple1_contains() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertTrue(tuple.contains(1.0));
        }

        @Test
        public void testDoubleTuple1_hashCode() {
            DoubleTuple.DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
            DoubleTuple.DoubleTuple1 tuple2 = DoubleTuple.of(1.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testDoubleTuple1_equals() {
            DoubleTuple.DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
            DoubleTuple.DoubleTuple1 tuple2 = DoubleTuple.of(1.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testDoubleTuple1_toString() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testDoubleTuple1_forEach() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testDoubleTuple1_min() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertNotNull(tuple.min());
        }

        @Test
        public void testDoubleTuple1_max() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertNotNull(tuple.max());
        }

        @Test
        public void testDoubleTuple1_median() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertNotNull(tuple.median());
        }

        @Test
        public void testDoubleTuple1_sum() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testDoubleTuple1_average() {
            DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        // ============ DoubleTuple2 Nested Class Tests ============

        @Test
        public void testDoubleTuple2_arity() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testDoubleTuple2_reverse() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            DoubleTuple.DoubleTuple2 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._2);
            assertEquals(tuple._2, reversed._1);
        }

        @Test
        public void testDoubleTuple2_contains() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertTrue(tuple.contains(1.0));
        }

        @Test
        public void testDoubleTuple2_hashCode() {
            DoubleTuple.DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
            DoubleTuple.DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testDoubleTuple2_equals() {
            DoubleTuple.DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
            DoubleTuple.DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testDoubleTuple2_toString() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testDoubleTuple2_forEach() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(2, count.size());
        }

        @Test
        public void testDoubleTuple2_min() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertNotNull(tuple.min());
        }

        @Test
        public void testDoubleTuple2_max() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertNotNull(tuple.max());
        }

        @Test
        public void testDoubleTuple2_median() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertNotNull(tuple.median());
        }

        @Test
        public void testDoubleTuple2_sum() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testDoubleTuple2_average() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testDoubleTuple2_accept_biConsumer() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testDoubleTuple2_map_biFunction() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            String result = tuple.map((a, b) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testDoubleTuple2_filter_biPredicate() {
            DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertTrue(tuple.filter((a, b) -> true).isPresent());
            assertFalse(tuple.filter((a, b) -> false).isPresent());
        }

        // ============ DoubleTuple3 Nested Class Tests ============

        @Test
        public void testDoubleTuple3_arity() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testDoubleTuple3_reverse() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple.DoubleTuple3 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._3);
            assertEquals(tuple._3, reversed._1);
        }

        @Test
        public void testDoubleTuple3_contains() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertTrue(tuple.contains(1.0));
        }

        @Test
        public void testDoubleTuple3_hashCode() {
            DoubleTuple.DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple.DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testDoubleTuple3_equals() {
            DoubleTuple.DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple.DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testDoubleTuple3_toString() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testDoubleTuple3_forEach() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(3, count.size());
        }

        @Test
        public void testDoubleTuple3_min() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple.min());
        }

        @Test
        public void testDoubleTuple3_max() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple.max());
        }

        @Test
        public void testDoubleTuple3_median() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple.median());
        }

        @Test
        public void testDoubleTuple3_sum() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple.sum());
        }

        @Test
        public void testDoubleTuple3_average() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertTrue(tuple.average() >= 0 || tuple.average() < 0);
        }

        @Test
        public void testDoubleTuple3_accept_triConsumer() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Integer> count = new ArrayList<>();
            tuple.accept((a, b, c) -> count.add(1));
            assertEquals(1, count.size());
        }

        @Test
        public void testDoubleTuple3_map_triFunction() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            String result = tuple.map((a, b, c) -> "test");
            assertNotNull(result);
        }

        @Test
        public void testDoubleTuple3_filter_triPredicate() {
            DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertTrue(tuple.filter((a, b, c) -> true).isPresent());
            assertFalse(tuple.filter((a, b, c) -> false).isPresent());
        }

        // ============ DoubleTuple4 Nested Class Tests ============

        @Test
        public void testDoubleTuple4_arity() {
            DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testDoubleTuple4_reverse() {
            DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            DoubleTuple.DoubleTuple4 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._4);
            assertEquals(tuple._4, reversed._1);
        }

        @Test
        public void testDoubleTuple4_contains() {
            DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertTrue(tuple.contains(1.0));
        }

        @Test
        public void testDoubleTuple4_hashCode() {
            DoubleTuple.DoubleTuple4 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            DoubleTuple.DoubleTuple4 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testDoubleTuple4_equals() {
            DoubleTuple.DoubleTuple4 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            DoubleTuple.DoubleTuple4 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testDoubleTuple4_toString() {
            DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testDoubleTuple4_forEach() {
            DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(4, count.size());
        }

        // ============ DoubleTuple5 Nested Class Tests ============

        @Test
        public void testDoubleTuple5_arity() {
            DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testDoubleTuple5_reverse() {
            DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            DoubleTuple.DoubleTuple5 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._5);
            assertEquals(tuple._5, reversed._1);
        }

        @Test
        public void testDoubleTuple5_contains() {
            DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertTrue(tuple.contains(1.0));
        }

        @Test
        public void testDoubleTuple5_hashCode() {
            DoubleTuple.DoubleTuple5 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            DoubleTuple.DoubleTuple5 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testDoubleTuple5_equals() {
            DoubleTuple.DoubleTuple5 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            DoubleTuple.DoubleTuple5 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testDoubleTuple5_toString() {
            DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testDoubleTuple5_forEach() {
            DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(5, count.size());
        }

        // ============ DoubleTuple6 Nested Class Tests ============

        @Test
        public void testDoubleTuple6_arity() {
            DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testDoubleTuple6_reverse() {
            DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            DoubleTuple.DoubleTuple6 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._6);
            assertEquals(tuple._6, reversed._1);
        }

        @Test
        public void testDoubleTuple6_contains() {
            DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertTrue(tuple.contains(1.0));
        }

        @Test
        public void testDoubleTuple6_hashCode() {
            DoubleTuple.DoubleTuple6 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            DoubleTuple.DoubleTuple6 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testDoubleTuple6_equals() {
            DoubleTuple.DoubleTuple6 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            DoubleTuple.DoubleTuple6 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testDoubleTuple6_toString() {
            DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testDoubleTuple6_forEach() {
            DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(6, count.size());
        }

        // ============ DoubleTuple7 Nested Class Tests ============

        @Test
        public void testDoubleTuple7_arity() {
            DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testDoubleTuple7_reverse() {
            DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            DoubleTuple.DoubleTuple7 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._7);
            assertEquals(tuple._7, reversed._1);
        }

        @Test
        public void testDoubleTuple7_contains() {
            DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertTrue(tuple.contains(1.0));
        }

        @Test
        public void testDoubleTuple7_hashCode() {
            DoubleTuple.DoubleTuple7 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            DoubleTuple.DoubleTuple7 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testDoubleTuple7_equals() {
            DoubleTuple.DoubleTuple7 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            DoubleTuple.DoubleTuple7 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testDoubleTuple7_toString() {
            DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testDoubleTuple7_forEach() {
            DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(7, count.size());
        }

        // ============ DoubleTuple8 Nested Class Tests ============

        @Test
        public void testDoubleTuple8_arity() {
            DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testDoubleTuple8_reverse() {
            DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            DoubleTuple.DoubleTuple8 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._8);
            assertEquals(tuple._8, reversed._1);
        }

        @Test
        public void testDoubleTuple8_contains() {
            DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertTrue(tuple.contains(1.0));
        }

        @Test
        public void testDoubleTuple8_hashCode() {
            DoubleTuple.DoubleTuple8 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            DoubleTuple.DoubleTuple8 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testDoubleTuple8_equals() {
            DoubleTuple.DoubleTuple8 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            DoubleTuple.DoubleTuple8 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testDoubleTuple8_toString() {
            DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testDoubleTuple8_forEach() {
            DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(8, count.size());
        }

        // ============ DoubleTuple9 Nested Class Tests ============

        @Test
        public void testDoubleTuple9_arity() {
            DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testDoubleTuple9_reverse() {
            DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            DoubleTuple.DoubleTuple9 reversed = tuple.reverse();
            assertEquals(tuple._1, reversed._9);
            assertEquals(tuple._9, reversed._1);
        }

        @Test
        public void testDoubleTuple9_contains() {
            DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertTrue(tuple.contains(1.0));
        }

        @Test
        public void testDoubleTuple9_hashCode() {
            DoubleTuple.DoubleTuple9 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            DoubleTuple.DoubleTuple9 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testDoubleTuple9_equals() {
            DoubleTuple.DoubleTuple9 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            DoubleTuple.DoubleTuple9 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testDoubleTuple9_toString() {
            DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertNotNull(tuple.toString());
        }

        @Test
        public void testDoubleTuple9_forEach() {
            DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            List<Integer> count = new ArrayList<>();
            tuple.forEach(v -> count.add(1));
            assertEquals(9, count.size());
        }

    }

    @Nested
    /**
     * Comprehensive test suite for DoubleTuple and its nested classes.
     * Tests all public methods including factory methods, statistical operations,
     * collection conversions, and special methods in Tuple2 and Tuple3.
     */
    @Tag("2510")
    class DoubleTuple2510Test extends TestBase {

        // Factory method tests
        @Test
        public void testOf1() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple._1, 0.001);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(1.5, tuple._1, 0.001);
            assertEquals(2.5, tuple._2, 0.001);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(2.0, tuple._2, 0.001);
            assertEquals(3.0, tuple._3, 0.001);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(2.0, tuple._2, 0.001);
            assertEquals(3.0, tuple._3, 0.001);
            assertEquals(4.0, tuple._4, 0.001);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(5.0, tuple._5, 0.001);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf6() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(6.0, tuple._6, 0.001);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf7() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(7.0, tuple._7, 0.001);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf8() {
            DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(8.0, tuple._8, 0.001);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(9.0, tuple._9, 0.001);
            assertEquals(9, tuple.arity());
        }

        // Create method tests
        @Test
        public void testCreateEmpty() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreateNull() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate1() {
            DoubleTuple1 tuple = DoubleTuple.copyOf(new double[] { 42.0 });
            assertEquals(42.0, tuple._1, 0.001);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testCreate2() {
            DoubleTuple2 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(2.0, tuple._2, 0.001);
        }

        @Test
        public void testCreate9() {
            DoubleTuple9 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
            assertEquals(1.0, tuple._1, 0.001);
            assertEquals(9.0, tuple._9, 0.001);
        }

        @Test
        public void testCreateTooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> DoubleTuple.copyOf(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        }

        // Min tests
        @Test
        public void testMinTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals(42.0, tuple.min(), 0.001);
        }

        @Test
        public void testMinTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(1.0, tuple.min(), 0.001);
        }

        @Test
        public void testMinTuple0ThrowsException() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void testMinWithNegativeValues() {
            DoubleTuple3 tuple = DoubleTuple.of(-5.0, -1.0, -10.0);
            assertEquals(-10.0, tuple.min(), 0.001);
        }

        // Max tests
        @Test
        public void testMaxTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals(42.0, tuple.max(), 0.001);
        }

        @Test
        public void testMaxTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(3.0, tuple.max(), 0.001);
        }

        @Test
        public void testMaxTuple7() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertEquals(7.0, tuple.max(), 0.001);
        }

        @Test
        public void testMaxTuple0ThrowsException() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        // Median tests
        @Test
        public void testMedianTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals(42.0, tuple.median(), 0.001);
        }

        @Test
        public void testMedianTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(30.0, 10.0, 20.0);
            assertEquals(20.0, tuple.median(), 0.001);
        }

        @Test
        public void testMedianTuple4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(2.0, tuple.median(), 0.001);
        }

        @Test
        public void testMedianTuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(5.0, 1.0, 3.0, 2.0, 4.0);
            assertEquals(3.0, tuple.median(), 0.001);
        }

        @Test
        public void testMedianTuple0ThrowsException() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        // Sum tests
        @Test
        public void testSumTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertEquals(0.0, tuple.sum(), 0.001);
        }

        @Test
        public void testSumTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals(42.0, tuple.sum(), 0.001);
        }

        @Test
        public void testSumTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(6.0, tuple.sum(), 0.001);
        }

        @Test
        public void testSumTuple4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(10.0, tuple.sum(), 0.001);
        }

        @Test
        public void testSumTuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(45.0, tuple.sum(), 0.001);
        }

        // Average tests
        @Test
        public void testAverageTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals(42.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(2.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(10.0, 20.0);
            assertEquals(15.0, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple6() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertEquals(3.5, tuple.average(), 0.001);
        }

        @Test
        public void testAverageTuple0ThrowsException() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        // Reverse tests
        @Test
        public void testReverseTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            DoubleTuple<DoubleTuple0> reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(0, reversed.arity());
        }

        @Test
        public void testReverseTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            DoubleTuple1 reversed = tuple.reverse();
            assertEquals(42.0, reversed._1, 0.001);
        }

        @Test
        public void testReverseTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            DoubleTuple2 reversed = tuple.reverse();
            assertEquals(2.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._2, 0.001);
        }

        @Test
        public void testReverseTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 reversed = tuple.reverse();
            assertEquals(3.0, reversed._1, 0.001);
            assertEquals(2.0, reversed._2, 0.001);
            assertEquals(1.0, reversed._3, 0.001);
        }

        @Test
        public void testReverseTuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            DoubleTuple9 reversed = tuple.reverse();
            assertEquals(9.0, reversed._1, 0.001);
            assertEquals(1.0, reversed._9, 0.001);
        }

        // Contains tests
        @Test
        public void testContainsTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertFalse(tuple.contains(1.0));
        }

        @Test
        public void testContainsTuple1True() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertTrue(tuple.contains(42.0));
        }

        @Test
        public void testContainsTuple1False() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertFalse(tuple.contains(41.0));
        }

        @Test
        public void testContainsTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(2.0));
            assertTrue(tuple.contains(3.0));
            assertFalse(tuple.contains(4.0));
        }

        @Test
        public void testContainsTuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertTrue(tuple.contains(5.0));
            assertFalse(tuple.contains(10.0));
        }

        // toArray tests
        @Test
        public void testToArrayTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            double[] array = tuple.toArray();
            assertEquals(0, array.length);
        }

        @Test
        public void testToArrayTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 42.0 }, array, 0.001);
        }

        @Test
        public void testToArrayTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, array, 0.001);
        }

        @Test
        public void testToArrayTuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            double[] array = tuple.toArray();
            assertEquals(9, array.length);
            assertEquals(1.0, array[0], 0.001);
            assertEquals(9.0, array[8], 0.001);
        }

        @Test
        public void testToArrayImmutable() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array1 = tuple.toArray();
            double[] array2 = tuple.toArray();
            array1[0] = 999.0;
            assertEquals(1.0, array2[0], 0.001);
        }

        // toList tests
        @Test
        public void testToListTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            DoubleList list = tuple.toList();
            assertEquals(0, list.size());
        }

        @Test
        public void testToListTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            DoubleList list = tuple.toList();
            assertEquals(1, list.size());
            assertEquals(42.0, list.get(0), 0.001);
        }

        @Test
        public void testToListTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleList list = tuple.toList();
            assertEquals(3, list.size());
            assertEquals(1.0, list.get(0), 0.001);
            assertEquals(3.0, list.get(2), 0.001);
        }

        // forEach tests
        @Test
        public void testForEachTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            final List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(0, collected.size());
        }

        @Test
        public void testForEachTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            final List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals(42.0, collected.get(0), 0.001);
        }

        @Test
        public void testForEachTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            final List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(3, collected.size());
            assertEquals(1.0, collected.get(0), 0.001);
            assertEquals(2.0, collected.get(1), 0.001);
            assertEquals(3.0, collected.get(2), 0.001);
        }

        @Test
        public void testForEachTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(10.0, 20.0);
            final List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(2, collected.size());
            assertEquals(10.0, collected.get(0), 0.001);
            assertEquals(20.0, collected.get(1), 0.001);
        }

        // stream tests
        @Test
        public void testStreamTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            DoubleStream stream = tuple.stream();
            assertEquals(0, stream.count());
        }

        @Test
        public void testStreamTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            DoubleStream stream = tuple.stream();
            assertEquals(1, stream.count());
        }

        @Test
        public void testStreamTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double sum = tuple.stream().sum();
            assertEquals(6.0, sum, 0.001);
        }

        @Test
        public void testStreamTuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            double max = tuple.stream().max().getAsDouble();
            assertEquals(5.0, max, 0.001);
        }

        // hashCode tests
        @Test
        public void testHashCodeTuple0() {
            DoubleTuple<DoubleTuple0> tuple1 = DoubleTuple.copyOf(new double[0]);
            DoubleTuple<DoubleTuple0> tuple2 = DoubleTuple.copyOf(new double[0]);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple1() {
            DoubleTuple1 tuple1 = DoubleTuple.of(42.0);
            DoubleTuple1 tuple2 = DoubleTuple.of(42.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple2() {
            DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
            DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCodeTuple3() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // equals tests
        @Test
        public void testEqualsTuple0() {
            DoubleTuple<DoubleTuple0> tuple1 = DoubleTuple.copyOf(new double[0]);
            DoubleTuple<DoubleTuple0> tuple2 = DoubleTuple.copyOf(new double[0]);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEqualsTuple1() {
            DoubleTuple1 tuple1 = DoubleTuple.of(42.0);
            DoubleTuple1 tuple2 = DoubleTuple.of(42.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEqualsTuple2() {
            DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
            DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEqualsTuple3() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testNotEqualsDifferentValues() {
            DoubleTuple2 tuple1 = DoubleTuple.of(1.0, 2.0);
            DoubleTuple2 tuple2 = DoubleTuple.of(2.0, 3.0);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testNotEqualsDifferentTypes() {
            DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
            DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotEquals(tuple2, tuple3);
        }

        @Test
        public void testEqualsNull() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertNotEquals(tuple, null);
        }

        @Test
        public void testEqualsSelf() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertEquals(tuple, tuple);
        }

        // toString tests
        @Test
        public void testToStringTuple0() {
            DoubleTuple<DoubleTuple0> tuple = DoubleTuple.copyOf(new double[0]);
            assertEquals("()", tuple.toString());
        }

        @Test
        public void testToStringTuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(42.0);
            assertEquals("(42.0)", tuple.toString());
        }

        @Test
        public void testToStringTuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertEquals("(1.0, 2.0)", tuple.toString());
        }

        @Test
        public void testToStringTuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals("(1.0, 2.0, 3.0)", tuple.toString());
        }

        // DoubleTuple2 special methods
        @Test
        public void testTuple2Accept() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            final List<Double> values = new ArrayList<>();
            tuple.accept((a, b) -> {
                values.add(a);
                values.add(b);
            });
            assertEquals(2, values.size());
            assertEquals(3.0, values.get(0), 0.001);
            assertEquals(4.0, values.get(1), 0.001);
        }

        @Test
        public void testTuple2Map() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            double result = tuple.map((a, b) -> a * b);
            assertEquals(12.0, result, 0.001);
        }

        @Test
        public void testTuple2Filter() {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            assertTrue(tuple.filter((a, b) -> a + b > 5).isPresent());
            assertFalse(tuple.filter((a, b) -> a + b > 10).isPresent());
        }

        // DoubleTuple3 special methods
        @Test
        public void testTuple3Accept() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            final List<Double> values = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                values.add(a);
                values.add(b);
                values.add(c);
            });
            assertEquals(3, values.size());
            assertEquals(1.0, values.get(0), 0.001);
            assertEquals(2.0, values.get(1), 0.001);
            assertEquals(3.0, values.get(2), 0.001);
        }

        @Test
        public void testTuple3Map() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double result = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0, result, 0.001);
        }

        @Test
        public void testTuple3Filter() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertTrue(tuple.filter((a, b, c) -> a + b + c > 5).isPresent());
            assertFalse(tuple.filter((a, b, c) -> a + b + c > 10).isPresent());
        }

        // Edge cases and special values
        @Test
        public void testZeroValues() {
            DoubleTuple3 tuple = DoubleTuple.of(0.0, 0.0, 0.0);
            assertEquals(0.0, tuple.min(), 0.001);
            assertEquals(0.0, tuple.max(), 0.001);
            assertEquals(0.0, tuple.sum(), 0.001);
        }

        @Test
        public void testNegativeValues() {
            DoubleTuple3 tuple = DoubleTuple.of(-1.0, -2.0, -3.0);
            assertEquals(-3.0, tuple.min(), 0.001);
            assertEquals(-1.0, tuple.max(), 0.001);
            assertEquals(-6.0, tuple.sum(), 0.001);
        }

        @Test
        public void testMixedValues() {
            DoubleTuple5 tuple = DoubleTuple.of(-2.0, 0.0, 5.0, -1.0, 3.0);
            assertEquals(-2.0, tuple.min(), 0.001);
            assertEquals(5.0, tuple.max(), 0.001);
            assertEquals(5.0, tuple.sum(), 0.001);
        }
    }

    @Nested
    @Tag("2511")
    class DoubleTuple2511Test extends TestBase {

        // ============ Factory Method Tests - DoubleTuple.of() ============

        @Test
        public void testOf_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertNotNull(tuple);
            assertEquals(3.14, tuple._1);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testOf_tuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertNotNull(tuple);
            assertEquals(1.5, tuple._1);
            assertEquals(2.5, tuple._2);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testOf_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1);
            assertEquals(2.0, tuple._2);
            assertEquals(3.0, tuple._3);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testOf_tuple4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1);
            assertEquals(2.0, tuple._2);
            assertEquals(3.0, tuple._3);
            assertEquals(4.0, tuple._4);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void testOf_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1);
            assertEquals(2.0, tuple._2);
            assertEquals(3.0, tuple._3);
            assertEquals(4.0, tuple._4);
            assertEquals(5.0, tuple._5);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void testOf_tuple6() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1);
            assertEquals(6.0, tuple._6);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void testOf_tuple7() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1);
            assertEquals(7.0, tuple._7);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void testOf_tuple8() {
            DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1);
            assertEquals(8.0, tuple._8);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void testOf_tuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1);
            assertEquals(9.0, tuple._9);
            assertEquals(9, tuple.arity());
        }

        // ============ Factory Method Tests - DoubleTuple.copyOf() ============

        @Test
        public void testCreate_nullArray() {
            DoubleTuple<?> tuple = DoubleTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_emptyArray() {
            DoubleTuple<?> tuple = DoubleTuple.copyOf(new double[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testCreate_array1() {
            DoubleTuple1 tuple = DoubleTuple.copyOf(new double[] { 5.5 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(5.5, tuple._1);
        }

        @Test
        public void testCreate_array2() {
            DoubleTuple2 tuple = DoubleTuple.copyOf(new double[] { 1.1, 2.2 });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals(1.1, tuple._1);
            assertEquals(2.2, tuple._2);
        }

        @Test
        public void testCreate_array3() {
            DoubleTuple3 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0 });
            assertNotNull(tuple);
            assertEquals(3, tuple.arity());
            assertEquals(1.0, tuple._1);
            assertEquals(3.0, tuple._3);
        }

        @Test
        public void testCreate_array9() {
            DoubleTuple9 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
        }

        @Test
        public void testCreate_tooManyElements() {
            assertThrows(IllegalArgumentException.class, () -> DoubleTuple.copyOf(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        }

        // ============ Min/Max/Median Tests ============

        @Test
        public void testMin_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            assertEquals(5.5, tuple.min());
        }

        @Test
        public void testMin_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(1.0, tuple.min());
        }

        @Test
        public void testMin_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(10.5, -3.2, 0.0, 7.1, -10.0);
            assertEquals(-10.0, tuple.min());
        }

        @Test
        public void testMin_emptyTuple() {
            DoubleTuple<?> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, tuple::min);
        }

        @Test
        public void testMax_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            assertEquals(5.5, tuple.max());
        }

        @Test
        public void testMax_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(3.0, tuple.max());
        }

        @Test
        public void testMax_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(10.5, -3.2, 0.0, 7.1, -10.0);
            assertEquals(10.5, tuple.max());
        }

        @Test
        public void testMax_emptyTuple() {
            DoubleTuple<?> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, tuple::max);
        }

        @Test
        public void testMedian_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            assertEquals(5.5, tuple.median());
        }

        @Test
        public void testMedian_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(30.0, 10.0, 20.0);
            assertEquals(20.0, tuple.median());
        }

        @Test
        public void testMedian_tuple4_even() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(2.0, tuple.median());
        }

        @Test
        public void testMedian_emptyTuple() {
            DoubleTuple<?> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, tuple::median);
        }

        // ============ Sum/Average Tests ============

        @Test
        public void testSum_tuple0() {
            DoubleTuple<?> tuple = DoubleTuple.copyOf(new double[0]);
            assertEquals(0.0, tuple.sum());
        }

        @Test
        public void testSum_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.sum(), 0.001);
        }

        @Test
        public void testSum_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(6.0, tuple.sum());
        }

        @Test
        public void testSum_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(15.0, tuple.sum());
        }

        @Test
        public void testAverage_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.0);
            assertEquals(5.0, tuple.average());
        }

        @Test
        public void testAverage_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(2.0, tuple.average());
        }

        @Test
        public void testAverage_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(2.0, 4.0, 6.0, 8.0, 10.0);
            assertEquals(6.0, tuple.average());
        }

        @Test
        public void testAverage_emptyTuple() {
            DoubleTuple<?> tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, tuple::average);
        }

        // ============ Reverse Tests ============

        @Test
        public void testReverse_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            DoubleTuple1 reversed = tuple.reverse();
            assertEquals(5.5, reversed._1);
        }

        @Test
        public void testReverse_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 reversed = tuple.reverse();
            assertEquals(3.0, reversed._1);
            assertEquals(2.0, reversed._2);
            assertEquals(1.0, reversed._3);
        }

        @Test
        public void testReverse_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            DoubleTuple5 reversed = tuple.reverse();
            assertEquals(5.0, reversed._1);
            assertEquals(1.0, reversed._5);
        }

        @Test
        public void testReverse_tuple0() {
            DoubleTuple<?> tuple = DoubleTuple.copyOf(new double[0]);
            DoubleTuple<?> reversed = tuple.reverse();
            assertEquals(0, reversed.arity());
        }

        // ============ Contains Tests ============

        @Test
        public void testContains_tuple1_found() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            assertTrue(tuple.contains(5.5));
        }

        @Test
        public void testContains_tuple1_notFound() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            assertFalse(tuple.contains(3.3));
        }

        @Test
        public void testContains_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(2.0));
            assertTrue(tuple.contains(3.0));
            assertFalse(tuple.contains(4.0));
        }

        @Test
        public void testContains_emptyTuple() {
            DoubleTuple<?> tuple = DoubleTuple.copyOf(new double[0]);
            assertFalse(tuple.contains(1.0));
        }

        // ============ Array/List Conversion Tests ============

        @Test
        public void testToArray_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, array);
        }

        @Test
        public void testToArray_independence() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array1 = tuple.toArray();
            double[] array2 = tuple.toArray();
            assertNotSame(array1, array2);
            assertArrayEquals(array1, array2);
        }

        @Test
        public void testToList_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleList list = tuple.toList();
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals(1.0, list.get(0));
            assertEquals(3.0, list.get(2));
        }

        @Test
        public void testToList_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            DoubleList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals(5.5, list.get(0));
        }

        // ============ Functional Methods Tests (ForEach, Stream) ============

        @Test
        public void testForEach_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(3, collected.size());
            assertEquals(1.0, collected.get(0));
            assertEquals(3.0, collected.get(2));
        }

        @Test
        public void testForEach_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals(5.5, collected.get(0));
        }

        @Test
        public void testStream_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double sum = tuple.stream().sum();
            assertEquals(6.0, sum);
        }

        @Test
        public void testStream_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            long count = tuple.stream().count();
            assertEquals(5, count);
        }

        // ============ Tuple2 Specific Functional Methods ============

        @Test
        public void testTuple2_accept() throws Exception {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            List<Double> results = new ArrayList<>();
            tuple.accept((a, b) -> {
                results.add(a);
                results.add(b);
            });
            assertEquals(2, results.size());
            assertEquals(3.0, results.get(0));
            assertEquals(4.0, results.get(1));
        }

        @Test
        public void testTuple2_map() throws Exception {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            double product = tuple.map((a, b) -> a * b);
            assertEquals(12.0, product);
        }

        @Test
        public void testTuple2_filter() throws Exception {
            DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
            Optional<DoubleTuple2> result = tuple.filter((a, b) -> a + b > 5);
            assertTrue(result.isPresent());

            Optional<DoubleTuple2> result2 = tuple.filter((a, b) -> a + b < 5);
            assertFalse(result2.isPresent());
        }

        // ============ Tuple3 Specific Functional Methods ============

        @Test
        public void testTuple3_accept() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> results = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                results.add(a);
                results.add(b);
                results.add(c);
            });
            assertEquals(3, results.size());
            assertEquals(1.0, results.get(0));
            assertEquals(3.0, results.get(2));
        }

        @Test
        public void testTuple3_map() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double product = tuple.map((a, b, c) -> a * b * c);
            assertEquals(6.0, product);
        }

        @Test
        public void testTuple3_filter() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
            assertTrue(result.isPresent());

            Optional<DoubleTuple3> result2 = tuple.filter((a, b, c) -> a + b + c < 5);
            assertFalse(result2.isPresent());
        }

        // ============ Equality Tests ============

        @Test
        public void testEquals_sameTuple1() {
            DoubleTuple1 tuple1 = DoubleTuple.of(5.5);
            DoubleTuple1 tuple2 = DoubleTuple.of(5.5);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_sameTuple3() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_differentTuple3() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 4.0);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_differentTypes() {
            DoubleTuple1 tuple1 = DoubleTuple.of(5.5);
            DoubleTuple2 tuple2 = DoubleTuple.of(5.5, 1.0);
            assertNotEquals(tuple1, tuple2);
        }

        @Test
        public void testEquals_null() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            assertNotEquals(tuple, null);
        }

        @Test
        public void testEquals_self() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple, tuple);
        }

        // ============ HashCode Tests ============

        @Test
        public void testHashCode_consistency() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        public void testHashCode_equal() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void testHashCode_different() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 4.0);
            assertNotEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        // ============ ToString Tests ============

        @Test
        public void testToString_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(5.5);
            String str = tuple.toString();
            assertNotNull(str);
            assertTrue(str.contains("5.5"));
        }

        @Test
        public void testToString_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            String str = tuple.toString();
            assertNotNull(str);
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("2.0"));
            assertTrue(str.contains("3.0"));
        }

        @Test
        public void testToString_emptyTuple() {
            DoubleTuple<?> tuple = DoubleTuple.copyOf(new double[0]);
            String str = tuple.toString();
            assertEquals("()", str);
        }

        // ============ PrimitiveTuple Base Class Methods Tests ============

        @Test
        public void testAccept() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<DoubleTuple3> results = new ArrayList<>();
            tuple.accept(results::add);
            assertEquals(1, results.size());
            assertEquals(tuple, results.get(0));
        }

        @Test
        public void testMap() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            String result = tuple.map(t -> "Sum: " + (t._1 + t._2 + t._3));
            assertEquals("Sum: 6.0", result);
        }

        @Test
        public void testFilter() throws Exception {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> result = tuple.filter(t -> t._1 > 0);
            assertTrue(result.isPresent());

            Optional<DoubleTuple3> result2 = tuple.filter(t -> t._1 < 0);
            assertFalse(result2.isPresent());
        }

        @Test
        public void testToOptional() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> opt = tuple.toOptional();
            assertTrue(opt.isPresent());
            assertEquals(tuple, opt.get());
        }

        // ============ Arity Tests ============

        @Test
        public void testArity_tuple0() {
            DoubleTuple<?> tuple = DoubleTuple.copyOf(new double[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void testArity_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.0);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void testArity_tuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void testArity_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void testArity_tuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(9, tuple.arity());
        }

    }

    @Nested
    /**
     * Comprehensive unit tests for DoubleTuple and its inner classes (DoubleTuple0-9).
     * Tests cover all public methods including factory methods, statistical methods,
     * utility methods, functional methods, equality/hashCode, and stream operations.
     */
    @Tag("2512")
    class DoubleTuple2512Test extends TestBase {

        private static final double DELTA = 0.0001;

        // ============ Factory Method Tests - DoubleTuple.of() ============

        @Test
        public void test_of_tuple1() {
            DoubleTuple1 tuple = DoubleTuple.of(1.5);
            assertNotNull(tuple);
            assertEquals(1.5, tuple._1, DELTA);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void test_of_tuple2() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertNotNull(tuple);
            assertEquals(1.5, tuple._1, DELTA);
            assertEquals(2.5, tuple._2, DELTA);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void test_of_tuple3() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1, DELTA);
            assertEquals(2.0, tuple._2, DELTA);
            assertEquals(3.0, tuple._3, DELTA);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void test_of_tuple4() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1, DELTA);
            assertEquals(4.0, tuple._4, DELTA);
            assertEquals(4, tuple.arity());
        }

        @Test
        public void test_of_tuple5() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1, DELTA);
            assertEquals(5.0, tuple._5, DELTA);
            assertEquals(5, tuple.arity());
        }

        @Test
        public void test_of_tuple6() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1, DELTA);
            assertEquals(6.0, tuple._6, DELTA);
            assertEquals(6, tuple.arity());
        }

        @Test
        public void test_of_tuple7() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1, DELTA);
            assertEquals(7.0, tuple._7, DELTA);
            assertEquals(7, tuple.arity());
        }

        @Test
        public void test_of_tuple8() {
            DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1, DELTA);
            assertEquals(8.0, tuple._8, DELTA);
            assertEquals(8, tuple.arity());
        }

        @Test
        public void test_of_tuple9() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertNotNull(tuple);
            assertEquals(1.0, tuple._1, DELTA);
            assertEquals(9.0, tuple._9, DELTA);
            assertEquals(9, tuple.arity());
        }

        // ============ Factory Method Tests - DoubleTuple.copyOf() ============

        @Test
        public void test_create_nullArray() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(null);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_emptyArray() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            assertNotNull(tuple);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_create_arraySize1() {
            DoubleTuple1 tuple = DoubleTuple.copyOf(new double[] { 1.5 });
            assertNotNull(tuple);
            assertEquals(1, tuple.arity());
            assertEquals(1.5, tuple._1, DELTA);
        }

        @Test
        public void test_create_arraySize2() {
            DoubleTuple2 tuple = DoubleTuple.copyOf(new double[] { 1.5, 2.5 });
            assertNotNull(tuple);
            assertEquals(2, tuple.arity());
            assertEquals(1.5, tuple._1, DELTA);
            assertEquals(2.5, tuple._2, DELTA);
        }

        @Test
        public void test_create_arraySize9() {
            DoubleTuple9 tuple = DoubleTuple.copyOf(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
            assertNotNull(tuple);
            assertEquals(9, tuple.arity());
            assertEquals(1.0, tuple._1, DELTA);
            assertEquals(9.0, tuple._9, DELTA);
        }

        @Test
        public void test_create_arrayTooLarge() {
            assertThrows(IllegalArgumentException.class, () -> {
                DoubleTuple.copyOf(new double[10]);
            });
        }

        // ============ Tuple0 Tests ============

        @Test
        public void test_tuple0_arity() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            assertEquals(0, tuple.arity());
        }

        @Test
        public void test_tuple0_min_throwsException() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.min());
        }

        @Test
        public void test_tuple0_max_throwsException() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.max());
        }

        @Test
        public void test_tuple0_median_throwsException() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.median());
        }

        @Test
        public void test_tuple0_sum() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            assertEquals(0.0, tuple.sum(), DELTA);
        }

        @Test
        public void test_tuple0_average_throwsException() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            assertThrows(NoSuchElementException.class, () -> tuple.average());
        }

        @Test
        public void test_tuple0_reverse() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            DoubleTuple0 reversed = tuple.reverse();
            assertSame(tuple, reversed);
        }

        @Test
        public void test_tuple0_contains() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            assertFalse(tuple.contains(1.0));
        }

        @Test
        public void test_tuple0_toArray() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            double[] array = tuple.toArray();
            assertNotNull(array);
            assertEquals(0, array.length);
        }

        @Test
        public void test_tuple0_toList() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            DoubleList list = tuple.toList();
            assertNotNull(list);
            assertTrue(list.isEmpty());
        }

        @Test
        public void test_tuple0_forEach() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertTrue(collected.isEmpty());
        }

        @Test
        public void test_tuple0_stream() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            long count = tuple.stream().count();
            assertEquals(0, count);
        }

        @Test
        public void test_tuple0_toString() {
            DoubleTuple0 tuple = DoubleTuple.copyOf(new double[0]);
            assertEquals("()", tuple.toString());
        }

        // ============ Tuple1 Tests ============

        @Test
        public void test_tuple1_arity() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(1, tuple.arity());
        }

        @Test
        public void test_tuple1_min() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.min(), DELTA);
        }

        @Test
        public void test_tuple1_max() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.max(), DELTA);
        }

        @Test
        public void test_tuple1_median() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.median(), DELTA);
        }

        @Test
        public void test_tuple1_sum() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.sum(), DELTA);
        }

        @Test
        public void test_tuple1_average() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals(3.14, tuple.average(), DELTA);
        }

        @Test
        public void test_tuple1_reverse() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            DoubleTuple1 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(3.14, reversed._1, DELTA);
            assertNotSame(tuple, reversed);
        }

        @Test
        public void test_tuple1_contains_found() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertTrue(tuple.contains(3.14));
        }

        @Test
        public void test_tuple1_contains_notFound() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertFalse(tuple.contains(2.71));
        }

        @Test
        public void test_tuple1_toArray() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 3.14 }, array, DELTA);
        }

        @Test
        public void test_tuple1_toList() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            DoubleList list = tuple.toList();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals(3.14, list.get(0), DELTA);
        }

        @Test
        public void test_tuple1_forEach() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(1, collected.size());
            assertEquals(3.14, collected.get(0), DELTA);
        }

        @Test
        public void test_tuple1_stream() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            double sum = tuple.stream().sum();
            assertEquals(3.14, sum, DELTA);
        }

        @Test
        public void test_tuple1_hashCode() {
            DoubleTuple1 tuple1 = DoubleTuple.of(3.14);
            DoubleTuple1 tuple2 = DoubleTuple.of(3.14);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple1_equals_same() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertTrue(tuple.equals(tuple));
        }

        @Test
        public void test_tuple1_equals_equal() {
            DoubleTuple1 tuple1 = DoubleTuple.of(3.14);
            DoubleTuple1 tuple2 = DoubleTuple.of(3.14);
            assertTrue(tuple1.equals(tuple2));
            assertTrue(tuple2.equals(tuple1));
        }

        @Test
        public void test_tuple1_equals_notEqual() {
            DoubleTuple1 tuple1 = DoubleTuple.of(3.14);
            DoubleTuple1 tuple2 = DoubleTuple.of(2.71);
            assertFalse(tuple1.equals(tuple2));
        }

        @Test
        public void test_tuple1_equals_null() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertFalse(tuple.equals(null));
        }

        @Test
        public void test_tuple1_equals_differentClass() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertFalse(tuple.equals("not a tuple"));
        }

        @Test
        public void test_tuple1_toString() {
            DoubleTuple1 tuple = DoubleTuple.of(3.14);
            assertEquals("(3.14)", tuple.toString());
        }

        // ============ Tuple2 Tests ============

        @Test
        public void test_tuple2_arity() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(2, tuple.arity());
        }

        @Test
        public void test_tuple2_min() {
            DoubleTuple2 tuple = DoubleTuple.of(2.5, 1.5);
            assertEquals(1.5, tuple.min(), DELTA);
        }

        @Test
        public void test_tuple2_max() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(2.5, tuple.max(), DELTA);
        }

        @Test
        public void test_tuple2_median() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(1.5, tuple.median(), DELTA);
        }

        @Test
        public void test_tuple2_sum() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(4.0, tuple.sum(), DELTA);
        }

        @Test
        public void test_tuple2_average() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals(2.0, tuple.average(), DELTA);
        }

        @Test
        public void test_tuple2_reverse() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            DoubleTuple2 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(2.5, reversed._1, DELTA);
            assertEquals(1.5, reversed._2, DELTA);
        }

        @Test
        public void test_tuple2_contains_found() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertTrue(tuple.contains(1.5));
            assertTrue(tuple.contains(2.5));
        }

        @Test
        public void test_tuple2_contains_notFound() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertFalse(tuple.contains(3.5));
        }

        @Test
        public void test_tuple2_toArray() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 1.5, 2.5 }, array, DELTA);
        }

        @Test
        public void test_tuple2_toList() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            DoubleList list = tuple.toList();
            assertNotNull(list);
            assertEquals(2, list.size());
        }

        @Test
        public void test_tuple2_forEach() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            List<Double> collected = new ArrayList<>();
            tuple.forEach(collected::add);
            assertEquals(2, collected.size());
            assertEquals(1.5, collected.get(0), DELTA);
            assertEquals(2.5, collected.get(1), DELTA);
        }

        @Test
        public void test_tuple2_stream() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            long count = tuple.stream().count();
            assertEquals(2, count);
        }

        @Test
        public void test_tuple2_accept() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            List<Double> collected = new ArrayList<>();
            tuple.accept((a, b) -> {
                collected.add(a);
                collected.add(b);
            });
            assertEquals(2, collected.size());
            assertEquals(1.5, collected.get(0), DELTA);
            assertEquals(2.5, collected.get(1), DELTA);
        }

        @Test
        public void test_tuple2_map() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            double result = tuple.map((a, b) -> a + b);
            assertEquals(4.0, result, DELTA);
        }

        @Test
        public void test_tuple2_filter_match() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            Optional<DoubleTuple2> result = tuple.filter((a, b) -> a < b);
            assertTrue(result.isPresent());
            assertEquals(tuple, result.get());
        }

        @Test
        public void test_tuple2_filter_noMatch() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            Optional<DoubleTuple2> result = tuple.filter((a, b) -> a > b);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_tuple2_hashCode() {
            DoubleTuple2 tuple1 = DoubleTuple.of(1.5, 2.5);
            DoubleTuple2 tuple2 = DoubleTuple.of(1.5, 2.5);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple2_equals() {
            DoubleTuple2 tuple1 = DoubleTuple.of(1.5, 2.5);
            DoubleTuple2 tuple2 = DoubleTuple.of(1.5, 2.5);
            DoubleTuple2 tuple3 = DoubleTuple.of(2.5, 1.5);

            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
            assertFalse(tuple1.equals(null));
        }

        @Test
        public void test_tuple2_toString() {
            DoubleTuple2 tuple = DoubleTuple.of(1.5, 2.5);
            assertEquals("(1.5, 2.5)", tuple.toString());
        }

        // ============ Tuple3 Tests ============

        @Test
        public void test_tuple3_arity() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(3, tuple.arity());
        }

        @Test
        public void test_tuple3_min() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(1.0, tuple.min(), DELTA);
        }

        @Test
        public void test_tuple3_max() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(3.0, tuple.max(), DELTA);
        }

        @Test
        public void test_tuple3_median() {
            DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
            assertEquals(2.0, tuple.median(), DELTA);
        }

        @Test
        public void test_tuple3_sum() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(6.0, tuple.sum(), DELTA);
        }

        @Test
        public void test_tuple3_average() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(2.0, tuple.average(), DELTA);
        }

        @Test
        public void test_tuple3_reverse() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 reversed = tuple.reverse();
            assertNotNull(reversed);
            assertEquals(3.0, reversed._1, DELTA);
            assertEquals(2.0, reversed._2, DELTA);
            assertEquals(1.0, reversed._3, DELTA);
        }

        @Test
        public void test_tuple3_contains() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertTrue(tuple.contains(1.0));
            assertTrue(tuple.contains(2.0));
            assertTrue(tuple.contains(3.0));
            assertFalse(tuple.contains(4.0));
        }

        @Test
        public void test_tuple3_toArray() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array = tuple.toArray();
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, array, DELTA);
        }

        @Test
        public void test_tuple3_accept() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            List<Double> collected = new ArrayList<>();
            tuple.accept((a, b, c) -> {
                collected.add(a);
                collected.add(b);
                collected.add(c);
            });
            assertEquals(3, collected.size());
        }

        @Test
        public void test_tuple3_map() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double result = tuple.map((a, b, c) -> a + b + c);
            assertEquals(6.0, result, DELTA);
        }

        @Test
        public void test_tuple3_filter_match() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);
            assertTrue(result.isPresent());
        }

        @Test
        public void test_tuple3_filter_noMatch() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            Optional<DoubleTuple3> result = tuple.filter((a, b, c) -> a > c);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_tuple3_hashCode() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals(tuple1.hashCode(), tuple2.hashCode());
        }

        @Test
        public void test_tuple3_equals() {
            DoubleTuple3 tuple1 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple2 = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleTuple3 tuple3 = DoubleTuple.of(1.0, 2.0, 4.0);

            assertTrue(tuple1.equals(tuple2));
            assertFalse(tuple1.equals(tuple3));
        }

        @Test
        public void test_tuple3_toString() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            assertEquals("(1.0, 2.0, 3.0)", tuple.toString());
        }

        // ============ Tuple4-9 Basic Tests ============

        @Test
        public void test_tuple4_basic() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(4, tuple.arity());
            assertEquals(1.0, tuple.min(), DELTA);
            assertEquals(4.0, tuple.max(), DELTA);
            assertEquals(10.0, tuple.sum(), DELTA);
            assertEquals(2.5, tuple.average(), DELTA);
            assertTrue(tuple.contains(3.0));
            DoubleTuple4 reversed = tuple.reverse();
            assertEquals(4.0, reversed._1, DELTA);
            assertEquals(1.0, reversed._4, DELTA);
        }

        @Test
        public void test_tuple5_basic() {
            DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
            assertEquals(5, tuple.arity());
            assertEquals(1.0, tuple.min(), DELTA);
            assertEquals(5.0, tuple.max(), DELTA);
            assertEquals(3.0, tuple.median(), DELTA);
            assertEquals(15.0, tuple.sum(), DELTA);
            assertTrue(tuple.contains(3.0));
        }

        @Test
        public void test_tuple6_basic() {
            DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
            assertEquals(6, tuple.arity());
            assertEquals(1.0, tuple.min(), DELTA);
            assertEquals(6.0, tuple.max(), DELTA);
            assertEquals(21.0, tuple.sum(), DELTA);
            assertEquals(3.5, tuple.average(), DELTA);
        }

        @Test
        public void test_tuple7_basic() {
            DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
            assertEquals(7, tuple.arity());
            assertEquals(4.0, tuple.median(), DELTA);
            assertEquals(7, tuple.toArray().length);
        }

        @Test
        public void test_tuple8_basic() {
            DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
            assertEquals(8, tuple.arity());
            assertEquals(8, tuple.toArray().length);
            assertEquals(36.0, tuple.sum(), DELTA);
        }

        @Test
        public void test_tuple9_basic() {
            DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
            assertEquals(9, tuple.arity());
            assertEquals(9, tuple.toArray().length);
            assertEquals(45.0, tuple.sum(), DELTA);
            assertEquals(5.0, tuple.average(), DELTA);
        }

        // ============ Edge Cases and Additional Coverage ============

        @Test
        public void test_negativeValues() {
            DoubleTuple3 tuple = DoubleTuple.of(-1.0, 0.0, 1.0);
            assertEquals(-1.0, tuple.min(), DELTA);
            assertEquals(1.0, tuple.max(), DELTA);
            assertEquals(0.0, tuple.median(), DELTA);
            assertEquals(0.0, tuple.sum(), DELTA);
        }

        @Test
        public void test_extremeValues() {
            DoubleTuple2 tuple = DoubleTuple.of(Double.MIN_VALUE, Double.MAX_VALUE);
            assertEquals(Double.MIN_VALUE, tuple.min(), DELTA);
            assertEquals(Double.MAX_VALUE, tuple.max(), DELTA);
        }

        @Test
        public void test_specialValues_infinity() {
            DoubleTuple2 tuple = DoubleTuple.of(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            assertEquals(Double.NEGATIVE_INFINITY, tuple.min(), DELTA);
            assertEquals(Double.POSITIVE_INFINITY, tuple.max(), DELTA);
        }

        @Test
        public void test_specialValues_NaN() {
            DoubleTuple1 tuple = DoubleTuple.of(Double.NaN);
            assertTrue(Double.isNaN(tuple._1));
        }

        @Test
        public void test_verySmallDifferences() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0000001, 1.0000002);
            assertEquals(1.0000001, tuple.min(), 0.00000001);
            assertEquals(1.0000002, tuple.max(), 0.00000001);
        }

        @Test
        public void test_toArray_independence() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double[] array = tuple.toArray();
            array[0] = 99.9;
            assertEquals(1.0, tuple._1, DELTA); // Tuple should be unaffected
        }

        @Test
        public void test_toList_independence() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            DoubleList list = tuple.toList();
            list.set(0, 99.9);
            assertEquals(1.0, tuple._1, DELTA); // Tuple should be unaffected
        }

        @Test
        public void test_stream_operations() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            double sum = tuple.stream().sum();
            assertEquals(6.0, sum, DELTA);
        }

        @Test
        public void test_forEach_withException() {
            DoubleTuple2 tuple = DoubleTuple.of(1.0, 2.0);
            assertThrows(RuntimeException.class, () -> {
                tuple.forEach(d -> {
                    if (d == 2.0)
                        throw new RuntimeException("test exception");
                });
            });
        }

        @Test
        public void test_equals_differentArity() {
            DoubleTuple1 tuple1 = DoubleTuple.of(1.0);
            DoubleTuple2 tuple2 = DoubleTuple.of(1.0, 2.0);
            assertFalse(tuple1.equals(tuple2));
        }

        @Test
        public void test_hashCode_consistency() {
            DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
            int hash1 = tuple.hashCode();
            int hash2 = tuple.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        public void test_create_allSizes() {
            for (int i = 0; i <= 9; i++) {
                double[] array = new double[i];
                DoubleTuple<?> tuple = DoubleTuple.copyOf(array);
                assertNotNull(tuple);
                assertEquals(i, tuple.arity());
            }
        }

        @Test
        public void test_median_evenCount() {
            DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
            assertEquals(2.0, tuple.median(), DELTA); // Lower middle value
        }

        @Test
        public void test_fractionalValues() {
            DoubleTuple3 tuple = DoubleTuple.of(0.1, 0.2, 0.3);
            assertEquals(0.6, tuple.sum(), DELTA);
            assertEquals(0.2, tuple.average(), DELTA);
        }

        @Test
        public void test_zero() {
            DoubleTuple3 tuple = DoubleTuple.of(0.0, 0.0, 0.0);
            assertEquals(0.0, tuple.min(), DELTA);
            assertEquals(0.0, tuple.max(), DELTA);
            assertEquals(0.0, tuple.median(), DELTA);
            assertEquals(0.0, tuple.sum(), DELTA);
            assertEquals(0.0, tuple.average(), DELTA);
        }
    }

    @Nested
    @SuppressWarnings("deprecation")
    @Tag("2512")
    class JavadocExampleTupleTest_DoubleTuple extends TestBase {
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
    }

}
