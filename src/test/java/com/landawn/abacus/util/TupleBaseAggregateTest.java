package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.util.u.OptionalDouble;

/**
 * Checks the aggregate and {@code forEach} bodies declared on the sealed tuple bases.
 *
 * <p>Every permitted arity overrides those operations and does not call {@code super}, so a virtual
 * call never enters the base body. These tests invoke that body directly and check the documented
 * results: empty-input behavior, numeric selection, overflow, and visitation order.</p>
 */
class TupleBaseAggregateTest {

    @Nested
    class BooleanForEachTests {

        private final Base forEach = Base.of(BooleanTuple.class, "forEach", void.class, Throwables.BooleanConsumer.class);

        @Test
        void forEachVisitsElementsInOrderAndSkipsAnEmptyTuple() {
            BooleanTuple.BooleanTuple3 values = BooleanTuple.of(true, false, true);
            List<Boolean> seen = new ArrayList<>();

            forEach.invoke(values, (Throwables.BooleanConsumer<RuntimeException>) seen::add);

            assertEquals(List.of(true, false, true), seen);
            assertEquals(List.of(values._1, values._2, values._3), seen);

            int[] calls = { 0 };
            forEach.invoke(BooleanTuple.from(new boolean[0]), (Throwables.BooleanConsumer<RuntimeException>) value -> calls[0]++);
            assertEquals(0, calls[0]);
        }

        @Test
        void forEachRejectsANullActionAndStopsWhenTheActionFails() {
            BooleanTuple<?> values = BooleanTuple.of(true, false);
            List<Boolean> seen = new ArrayList<>();

            assertThrows(IllegalArgumentException.class, () -> forEach.invoke(values, (Object) null));
            assertThrows(IllegalArgumentException.class, () -> forEach.invoke(BooleanTuple.from(new boolean[0]), (Object) null));

            IllegalStateException failure = assertThrows(IllegalStateException.class,
                    () -> forEach.invoke(values, (Throwables.BooleanConsumer<RuntimeException>) value -> {
                        seen.add(value);
                        if (!value) {
                            throw new IllegalStateException("stop");
                        }
                    }));

            assertEquals("stop", failure.getMessage());
            assertEquals(List.of(true, false), seen);
        }
    }

    @Nested
    class ByteAggregateTests {

        private final Base min = Base.of(ByteTuple.class, "min", byte.class);
        private final Base max = Base.of(ByteTuple.class, "max", byte.class);
        private final Base sum = Base.of(ByteTuple.class, "sum", int.class);
        private final Base average = Base.of(ByteTuple.class, "average", OptionalDouble.class);
        private final Base median = Base.of(ByteTuple.class, "median", double.class);
        private final Base lowerMedian = Base.of(ByteTuple.class, "lowerMedian", byte.class);
        private final Base forEach = Base.of(ByteTuple.class, "forEach", void.class, Throwables.ByteConsumer.class);

        @Test
        void emptyAggregatesReturnIdentityOrThrow() {
            ByteTuple<?> empty = ByteTuple.from(new byte[0]);

            assertThrows(NoSuchElementException.class, () -> min.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> max.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> median.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> lowerMedian.invoke(empty));
            assertEquals(0, (int) sum.invoke(empty));
            assertTrue(((OptionalDouble) average.invoke(empty)).isEmpty());
        }

        @Test
        void aggregatesUseSignedOrderAndWidenTheSum() {
            ByteTuple.ByteTuple3 values = ByteTuple.of((byte) 5, (byte) -1, (byte) 2);
            ByteTuple.ByteTuple2 pair = ByteTuple.of((byte) 10, (byte) 30);

            assertEquals((byte) -1, (byte) min.invoke(values));
            assertEquals((byte) 5, (byte) max.invoke(values));
            assertEquals(6, (int) sum.invoke(values));
            assertEquals(2.0, ((OptionalDouble) average.invoke(values)).getAsDouble());
            assertEquals(2.0, (double) median.invoke(values));
            assertEquals((byte) 2, (byte) lowerMedian.invoke(values));
            assertEquals(20.0, (double) median.invoke(pair));
            assertEquals((byte) 10, (byte) lowerMedian.invoke(pair));
            assertArrayUnchanged(values, new byte[] { 5, -1, 2 });
        }

        @Test
        void forEachVisitsElementsInOrder() {
            ByteTuple.ByteTuple3 values = ByteTuple.of((byte) 3, (byte) 1, (byte) 2);
            List<Byte> seen = new ArrayList<>();

            forEach.invoke(values, (Throwables.ByteConsumer<RuntimeException>) value -> seen.add(value));
            assertThrows(IllegalArgumentException.class, () -> forEach.invoke(values, (Object) null));

            assertEquals(List.of((byte) 3, (byte) 1, (byte) 2), seen);
        }
    }

    @Nested
    class CharAggregateTests {

        private final Base min = Base.of(CharTuple.class, "min", char.class);
        private final Base max = Base.of(CharTuple.class, "max", char.class);
        private final Base sum = Base.of(CharTuple.class, "sum", int.class);
        private final Base average = Base.of(CharTuple.class, "average", OptionalDouble.class);
        private final Base lowerMedian = Base.of(CharTuple.class, "lowerMedian", char.class);
        private final Base forEach = Base.of(CharTuple.class, "forEach", void.class, Throwables.CharConsumer.class);

        @Test
        void emptyAggregatesReturnIdentityOrThrow() {
            CharTuple<?> empty = CharTuple.from(new char[0]);

            assertThrows(NoSuchElementException.class, () -> min.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> max.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> lowerMedian.invoke(empty));
            assertEquals(0, (int) sum.invoke(empty));
            assertTrue(((OptionalDouble) average.invoke(empty)).isEmpty());
        }

        @Test
        void aggregatesOrderByUnsignedCodeUnit() {
            CharTuple.CharTuple3 values = CharTuple.of('b', 'A', 'c');
            CharTuple.CharTuple2 pair = CharTuple.of('d', 'b');

            assertEquals('A', (char) min.invoke(values));
            assertEquals('c', (char) max.invoke(values));
            assertEquals('A' + 'b' + 'c', (int) sum.invoke(values));
            assertEquals(('A' + 'b' + 'c') / 3d, ((OptionalDouble) average.invoke(values)).getAsDouble());
            assertEquals('b', (char) lowerMedian.invoke(values));
            assertEquals('b', (char) lowerMedian.invoke(pair));
            assertArrayUnchanged(values, new char[] { 'b', 'A', 'c' });
        }

        @Test
        void forEachVisitsCodeUnitsInOrder() {
            CharTuple.CharTuple3 values = CharTuple.of('C', 'A', 'B');
            List<Character> seen = new ArrayList<>();

            forEach.invoke(values, (Throwables.CharConsumer<RuntimeException>) value -> seen.add(value));
            assertThrows(IllegalArgumentException.class, () -> forEach.invoke(values, (Object) null));

            assertEquals(List.of('C', 'A', 'B'), seen);
        }
    }

    @Nested
    class ShortAggregateTests {

        private final Base min = Base.of(ShortTuple.class, "min", short.class);
        private final Base max = Base.of(ShortTuple.class, "max", short.class);
        private final Base sum = Base.of(ShortTuple.class, "sum", int.class);
        private final Base average = Base.of(ShortTuple.class, "average", OptionalDouble.class);
        private final Base median = Base.of(ShortTuple.class, "median", double.class);
        private final Base lowerMedian = Base.of(ShortTuple.class, "lowerMedian", short.class);
        private final Base forEach = Base.of(ShortTuple.class, "forEach", void.class, Throwables.ShortConsumer.class);

        @Test
        void emptyAggregatesReturnIdentityOrThrow() {
            ShortTuple<?> empty = ShortTuple.from(new short[0]);

            assertThrows(NoSuchElementException.class, () -> min.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> max.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> median.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> lowerMedian.invoke(empty));
            assertEquals(0, (int) sum.invoke(empty));
            assertTrue(((OptionalDouble) average.invoke(empty)).isEmpty());
        }

        @Test
        void aggregatesKeepValuesOutsideTheByteRange() {
            ShortTuple.ShortTuple3 values = ShortTuple.of((short) 30_000, (short) -30_000, (short) 1);
            ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 10, (short) 30);

            assertEquals((short) -30_000, (short) min.invoke(values));
            assertEquals((short) 30_000, (short) max.invoke(values));
            assertEquals(1, (int) sum.invoke(values));
            assertEquals(1.0 / 3.0, ((OptionalDouble) average.invoke(values)).getAsDouble());
            assertEquals(1.0, (double) median.invoke(values));
            assertEquals((short) 1, (short) lowerMedian.invoke(values));
            assertEquals(20.0, (double) median.invoke(pair));
            assertEquals((short) 10, (short) lowerMedian.invoke(pair));
            assertArrayUnchanged(values, new short[] { 30_000, -30_000, 1 });
        }

        @Test
        void forEachVisitsElementsInOrder() {
            ShortTuple.ShortTuple3 values = ShortTuple.of((short) 3, (short) 1, (short) 2);
            List<Short> seen = new ArrayList<>();

            forEach.invoke(values, (Throwables.ShortConsumer<RuntimeException>) value -> seen.add(value));
            assertThrows(IllegalArgumentException.class, () -> forEach.invoke(values, (Object) null));

            assertEquals(List.of((short) 3, (short) 1, (short) 2), seen);
        }
    }

    @Nested
    class IntAggregateTests {

        private final Base min = Base.of(IntTuple.class, "min", int.class);
        private final Base max = Base.of(IntTuple.class, "max", int.class);
        private final Base sum = Base.of(IntTuple.class, "sum", int.class);
        private final Base average = Base.of(IntTuple.class, "average", OptionalDouble.class);
        private final Base median = Base.of(IntTuple.class, "median", double.class);
        private final Base lowerMedian = Base.of(IntTuple.class, "lowerMedian", int.class);
        private final Base forEach = Base.of(IntTuple.class, "forEach", void.class, Throwables.IntConsumer.class);

        @Test
        void emptyAggregatesReturnIdentityOrThrow() {
            IntTuple<?> empty = IntTuple.from(new int[0]);

            assertThrows(NoSuchElementException.class, () -> min.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> max.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> median.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> lowerMedian.invoke(empty));
            assertEquals(0, (int) sum.invoke(empty));
            assertTrue(((OptionalDouble) average.invoke(empty)).isEmpty());
        }

        @Test
        void aggregatesSelectBySignedOrderWithoutChangingTheTuple() {
            IntTuple.IntTuple3 values = IntTuple.of(30, 10, 20);
            IntTuple.IntTuple4 even = IntTuple.of(8, 2, 6, 4);
            IntTuple.IntTuple2 pair = IntTuple.of(-5, -1);

            assertEquals(10, (int) min.invoke(values));
            assertEquals(30, (int) max.invoke(values));
            assertEquals(60, (int) sum.invoke(values));
            assertEquals(20.0, ((OptionalDouble) average.invoke(values)).getAsDouble());
            assertEquals(20.0, (double) median.invoke(values));
            assertEquals(20, (int) lowerMedian.invoke(values));
            assertEquals(5.0, (double) median.invoke(even));
            assertEquals(4, (int) lowerMedian.invoke(even));
            assertEquals(-3.0, (double) median.invoke(pair));
            assertEquals(-5, (int) lowerMedian.invoke(pair));
            assertArrayUnchanged(values, new int[] { 30, 10, 20 });
        }

        @Test
        void sumRejectsATotalThatDoesNotFitInAnInt() {
            assertThrows(ArithmeticException.class, () -> sum.invoke(IntTuple.of(Integer.MAX_VALUE, 1)));
            assertThrows(ArithmeticException.class, () -> sum.invoke(IntTuple.of(Integer.MIN_VALUE, -1)));
            assertEquals(Integer.MAX_VALUE, ((OptionalDouble) average.invoke(IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE))).getAsDouble());
        }

        @Test
        void forEachVisitsElementsInOrderAndStopsWhenTheActionFails() {
            IntTuple.IntTuple3 values = IntTuple.of(4, 5, 6);
            List<Integer> seen = new ArrayList<>();

            forEach.invoke(values, (Throwables.IntConsumer<RuntimeException>) value -> seen.add(value));
            assertEquals(List.of(4, 5, 6), seen);
            assertThrows(IllegalArgumentException.class, () -> forEach.invoke(values, (Object) null));
            assertThrows(IllegalArgumentException.class, () -> forEach.invoke(IntTuple.from(new int[0]), (Object) null));

            seen.clear();
            IllegalStateException failure = assertThrows(IllegalStateException.class,
                    () -> forEach.invoke(values, (Throwables.IntConsumer<RuntimeException>) value -> {
                        seen.add(value);
                        if (value == 5) {
                            throw new IllegalStateException("stop");
                        }
                    }));

            assertEquals("stop", failure.getMessage());
            assertEquals(List.of(4, 5), seen);
        }
    }

    @Nested
    class LongAggregateTests {

        private final Base min = Base.of(LongTuple.class, "min", long.class);
        private final Base max = Base.of(LongTuple.class, "max", long.class);
        private final Base sum = Base.of(LongTuple.class, "sum", long.class);
        private final Base average = Base.of(LongTuple.class, "average", OptionalDouble.class);
        private final Base median = Base.of(LongTuple.class, "median", double.class);
        private final Base lowerMedian = Base.of(LongTuple.class, "lowerMedian", long.class);
        private final Base forEach = Base.of(LongTuple.class, "forEach", void.class, Throwables.LongConsumer.class);

        @Test
        void emptyAggregatesReturnIdentityOrThrow() {
            LongTuple<?> empty = LongTuple.from(new long[0]);

            assertThrows(NoSuchElementException.class, () -> min.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> max.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> median.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> lowerMedian.invoke(empty));
            assertEquals(0L, (long) sum.invoke(empty));
            assertTrue(((OptionalDouble) average.invoke(empty)).isEmpty());
        }

        @Test
        void aggregatesSelectBySignedOrderAndWrapTheSum() {
            LongTuple.LongTuple3 values = LongTuple.of(30L, 10L, 20L);

            assertEquals(10L, (long) min.invoke(values));
            assertEquals(30L, (long) max.invoke(values));
            assertEquals(60L, (long) sum.invoke(values));
            assertEquals(20.0, ((OptionalDouble) average.invoke(values)).getAsDouble());
            assertEquals(20.0, (double) median.invoke(values));
            assertEquals(20L, (long) lowerMedian.invoke(values));
            assertEquals(25.0, (double) median.invoke(LongTuple.of(10L, 20L, 30L, 40L)));
            assertEquals(10L, (long) lowerMedian.invoke(LongTuple.of(10L, 40L)));
            assertEquals(Long.MIN_VALUE, (long) sum.invoke(LongTuple.of(Long.MAX_VALUE, 1L)));
            assertEquals(Long.MAX_VALUE, (long) sum.invoke(LongTuple.of(Long.MIN_VALUE, -1L)));
            assertArrayUnchanged(values, new long[] { 30L, 10L, 20L });
        }

        @Test
        void forEachVisitsElementsInOrder() {
            LongTuple.LongTuple3 values = LongTuple.of(3L, 1L, 2L);
            List<Long> seen = new ArrayList<>();

            forEach.invoke(values, (Throwables.LongConsumer<RuntimeException>) value -> seen.add(value));
            assertThrows(IllegalArgumentException.class, () -> forEach.invoke(values, (Object) null));

            assertEquals(List.of(3L, 1L, 2L), seen);
        }
    }

    @Nested
    class FloatAggregateTests {

        private final Base min = Base.of(FloatTuple.class, "min", float.class);
        private final Base max = Base.of(FloatTuple.class, "max", float.class);
        private final Base sum = Base.of(FloatTuple.class, "sum", float.class);
        private final Base average = Base.of(FloatTuple.class, "average", OptionalDouble.class);
        private final Base median = Base.of(FloatTuple.class, "median", double.class);
        private final Base lowerMedian = Base.of(FloatTuple.class, "lowerMedian", float.class);
        private final Base forEach = Base.of(FloatTuple.class, "forEach", void.class, Throwables.FloatConsumer.class);

        @Test
        void emptyAggregatesReturnIdentityOrThrow() {
            FloatTuple<?> empty = FloatTuple.from(new float[0]);

            assertThrows(NoSuchElementException.class, () -> min.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> max.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> median.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> lowerMedian.invoke(empty));
            assertEquals(0, Float.compare(0.0f, (float) sum.invoke(empty)));
            assertTrue(((OptionalDouble) average.invoke(empty)).isEmpty());
        }

        @Test
        void aggregatesPropagateNaNAndOrderSignedZeros() {
            FloatTuple.FloatTuple3 values = FloatTuple.of(3.0f, 1.0f, 2.0f);
            FloatTuple.FloatTuple2 zeros = FloatTuple.of(0.0f, -0.0f);
            FloatTuple.FloatTuple2 nan = FloatTuple.of(1.0f, Float.NaN);

            assertEquals(0, Float.compare(1.0f, (float) min.invoke(values)));
            assertEquals(0, Float.compare(3.0f, (float) max.invoke(values)));
            assertEquals(0, Float.compare(6.0f, (float) sum.invoke(values)));
            assertEquals(2.0, ((OptionalDouble) average.invoke(values)).getAsDouble());
            assertEquals(2.0, (double) median.invoke(values));
            assertEquals(0, Float.compare(2.0f, (float) lowerMedian.invoke(values)));
            assertEquals(2.5, (double) median.invoke(FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f)));
            assertEquals(0, Float.compare(1.0f, (float) lowerMedian.invoke(FloatTuple.of(1.0f, 4.0f))));
            assertEquals(0, Float.compare(-0.0f, (float) min.invoke(zeros)));
            assertEquals(0, Float.compare(0.0f, (float) max.invoke(zeros)));
            assertEquals(0, Float.compare(0.0f, (float) sum.invoke(FloatTuple.of(-0.0f, -0.0f))));
            assertTrue(Float.isNaN((float) min.invoke(nan)));
            assertTrue(Float.isNaN((float) max.invoke(nan)));
            assertTrue(Float.isNaN((float) sum.invoke(nan)));
            assertTrue(Double.isNaN((double) median.invoke(FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY))));
            assertEquals(0, Float.compare(Float.POSITIVE_INFINITY, (float) sum.invoke(FloatTuple.of(1.0f, Float.POSITIVE_INFINITY))));
            assertArrayUnchanged(values, new float[] { 3.0f, 1.0f, 2.0f });
        }

        @Test
        void singleElementMinDoesNotNeedASecondValue() {
            assertEquals(0, Float.compare(-4.5f, (float) min.invoke(FloatTuple.of(-4.5f))));
            assertEquals(0, Float.compare(-4.5f, (float) max.invoke(FloatTuple.of(-4.5f))));
        }

        @Test
        void forEachVisitsElementsInOrder() {
            FloatTuple.FloatTuple3 values = FloatTuple.of(3.0f, 1.0f, 2.0f);
            List<Float> seen = new ArrayList<>();

            forEach.invoke(values, (Throwables.FloatConsumer<RuntimeException>) value -> seen.add(value));
            assertThrows(IllegalArgumentException.class, () -> forEach.invoke(values, (Object) null));

            assertEquals(List.of(3.0f, 1.0f, 2.0f), seen);
        }
    }

    @Nested
    class DoubleAggregateTests {

        private final Base min = Base.of(DoubleTuple.class, "min", double.class);
        private final Base max = Base.of(DoubleTuple.class, "max", double.class);
        private final Base sum = Base.of(DoubleTuple.class, "sum", double.class);
        private final Base average = Base.of(DoubleTuple.class, "average", OptionalDouble.class);
        private final Base median = Base.of(DoubleTuple.class, "median", double.class);
        private final Base lowerMedian = Base.of(DoubleTuple.class, "lowerMedian", double.class);
        private final Base forEach = Base.of(DoubleTuple.class, "forEach", void.class, Throwables.DoubleConsumer.class);

        @Test
        void emptyAggregatesReturnIdentityOrThrow() {
            DoubleTuple<?> empty = DoubleTuple.from(new double[0]);

            assertThrows(NoSuchElementException.class, () -> min.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> max.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> median.invoke(empty));
            assertThrows(NoSuchElementException.class, () -> lowerMedian.invoke(empty));
            assertEquals(0, Double.compare(0.0, (double) sum.invoke(empty)));
            assertTrue(((OptionalDouble) average.invoke(empty)).isEmpty());
        }

        @Test
        void aggregatesPropagateNaNAndKeepASingleSignedInfinity() {
            DoubleTuple.DoubleTuple3 values = DoubleTuple.of(3.0, 1.0, 2.0);
            DoubleTuple.DoubleTuple2 zeros = DoubleTuple.of(0.0, -0.0);
            DoubleTuple.DoubleTuple2 nan = DoubleTuple.of(1.0, Double.NaN);
            DoubleTuple.DoubleTuple3 overflow = DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE, Double.NEGATIVE_INFINITY);

            assertEquals(0, Double.compare(1.0, (double) min.invoke(values)));
            assertEquals(0, Double.compare(3.0, (double) max.invoke(values)));
            assertEquals(0, Double.compare(6.0, (double) sum.invoke(values)));
            assertEquals(2.0, ((OptionalDouble) average.invoke(values)).getAsDouble());
            assertEquals(2.0, (double) median.invoke(values));
            assertEquals(0, Double.compare(2.0, (double) lowerMedian.invoke(values)));
            assertEquals(2.5, (double) median.invoke(DoubleTuple.of(1.0, 2.0, 3.0, 4.0)));
            assertEquals(0, Double.compare(1.0, (double) lowerMedian.invoke(DoubleTuple.of(1.0, 4.0))));
            assertEquals(0, Double.compare(-0.0, (double) min.invoke(zeros)));
            assertEquals(0, Double.compare(0.0, (double) max.invoke(zeros)));
            assertEquals(0, Double.compare(0.0, (double) sum.invoke(DoubleTuple.of(-0.0, -0.0))));
            assertTrue(Double.isNaN((double) min.invoke(nan)));
            assertTrue(Double.isNaN((double) max.invoke(nan)));
            assertTrue(Double.isNaN((double) sum.invoke(nan)));
            assertEquals(0, Double.compare(Double.NEGATIVE_INFINITY, (double) sum.invoke(overflow)));
            assertEquals(0, Double.compare(Double.NEGATIVE_INFINITY, ((OptionalDouble) average.invoke(overflow)).getAsDouble()));
            assertEquals(Double.MAX_VALUE, ((OptionalDouble) average.invoke(DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE))).getAsDouble());
            assertTrue(Double.isNaN((double) median.invoke(DoubleTuple.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY))));
            assertArrayUnchanged(values, new double[] { 3.0, 1.0, 2.0 });
        }

        @Test
        void singleElementMinDoesNotNeedASecondValue() {
            assertEquals(0, Double.compare(-4.5, (double) min.invoke(DoubleTuple.of(-4.5))));
            assertEquals(0, Double.compare(-4.5, (double) max.invoke(DoubleTuple.of(-4.5))));
        }

        @Test
        void forEachVisitsElementsInOrder() {
            DoubleTuple.DoubleTuple3 values = DoubleTuple.of(3.0, 1.0, 2.0);
            List<Double> seen = new ArrayList<>();

            forEach.invoke(values, (Throwables.DoubleConsumer<RuntimeException>) value -> seen.add(value));
            assertThrows(IllegalArgumentException.class, () -> forEach.invoke(values, (Object) null));

            assertEquals(List.of(3.0, 1.0, 2.0), seen);
        }
    }

    private static void assertArrayUnchanged(IntTuple.IntTuple3 tuple, int[] expected) {
        assertEquals(expected[0], tuple._1);
        assertEquals(expected[1], tuple._2);
        assertEquals(expected[2], tuple._3);
    }

    private static void assertArrayUnchanged(ByteTuple.ByteTuple3 tuple, byte[] expected) {
        assertEquals(expected[0], tuple._1);
        assertEquals(expected[1], tuple._2);
        assertEquals(expected[2], tuple._3);
    }

    private static void assertArrayUnchanged(CharTuple.CharTuple3 tuple, char[] expected) {
        assertEquals(expected[0], tuple._1);
        assertEquals(expected[1], tuple._2);
        assertEquals(expected[2], tuple._3);
    }

    private static void assertArrayUnchanged(ShortTuple.ShortTuple3 tuple, short[] expected) {
        assertEquals(expected[0], tuple._1);
        assertEquals(expected[1], tuple._2);
        assertEquals(expected[2], tuple._3);
    }

    private static void assertArrayUnchanged(LongTuple.LongTuple3 tuple, long[] expected) {
        assertEquals(expected[0], tuple._1);
        assertEquals(expected[1], tuple._2);
        assertEquals(expected[2], tuple._3);
    }

    private static void assertArrayUnchanged(FloatTuple.FloatTuple3 tuple, float[] expected) {
        assertEquals(0, Float.compare(expected[0], tuple._1));
        assertEquals(0, Float.compare(expected[1], tuple._2));
        assertEquals(0, Float.compare(expected[2], tuple._3));
    }

    private static void assertArrayUnchanged(DoubleTuple.DoubleTuple3 tuple, double[] expected) {
        assertEquals(0, Double.compare(expected[0], tuple._1));
        assertEquals(0, Double.compare(expected[1], tuple._2));
        assertEquals(0, Double.compare(expected[2], tuple._3));
    }

    /**
     * Non-virtual call to a method declared on a tuple base. Virtual dispatch would always select
     * the arity override and never execute this body.
     */
    private static final class Base {
        private final MethodHandle handle;

        private Base(MethodHandle handle) {
            this.handle = handle;
        }

        static Base of(Class<?> type, String name, Class<?> returnType, Class<?>... parameters) {
            try {
                MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(type, MethodHandles.lookup());
                MethodType methodType = MethodType.methodType(returnType, parameters);
                return new Base(lookup.findSpecial(type, name, methodType, type));
            } catch (IllegalAccessException | NoSuchMethodException e) {
                throw new IllegalStateException(type.getSimpleName() + "." + name, e);
            }
        }

        Object invoke(Object receiver, Object... arguments) {
            Object[] args = new Object[arguments.length + 1];
            args[0] = receiver;
            System.arraycopy(arguments, 0, args, 1, arguments.length);
            try {
                return handle.invokeWithArguments(args);
            } catch (RuntimeException | Error e) {
                throw e;
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
