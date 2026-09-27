package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

@SuppressWarnings("deprecation")
class NumericTuplePerformanceTest extends TestBase {

    @Test
    void testDefensiveExportsDoNotInitializeCaches() {
        for (int arity = 1; arity <= 9; arity++) {
            final int[] ints = new int[arity];
            final long[] longs = new long[arity];
            final float[] floats = new float[arity];
            final double[] doubles = new double[arity];

            for (int i = 0; i < arity; i++) {
                ints[i] = i - 5;
                longs[i] = Long.MAX_VALUE - i;
                floats[i] = i - 0.5f;
                doubles[i] = i - 0.5d;
            }

            checkExports(ints);
            checkExports(longs);
            checkExports(floats);
            checkExports(doubles);
        }
    }

    private static void checkExports(final int[] values) {
        final IntTuple<?> tuple = IntTuple.from(values);
        final int[] exported = tuple.toArray();
        final IntList list = tuple.toList();

        assertArrayEquals(values, exported);
        assertArrayEquals(values, list.toArray());
        assertNotSame(exported, tuple.toArray());
        assertNotSame(list, tuple.toList());
        exported[0] = 42;
        list.set(0, 43);
        assertArrayEquals(values, tuple.toArray());
        assertArrayEquals(values, tuple.toList().toArray());
        assertNull(tuple.elements);

        final int[] cached = tuple.elements();
        final int[] warmExport = tuple.toArray();
        assertNotSame(cached, warmExport);
        warmExport[0] = 44;
        tuple.toList().set(0, 45);
        assertArrayEquals(values, cached);
        assertArrayEquals(values, tuple.toArray());
        assertSame(cached, tuple.elements);
    }

    private static void checkExports(final long[] values) {
        final LongTuple<?> tuple = LongTuple.from(values);
        final long[] exported = tuple.toArray();
        final LongList list = tuple.toList();

        assertArrayEquals(values, exported);
        assertArrayEquals(values, list.toArray());
        assertNotSame(exported, tuple.toArray());
        assertNotSame(list, tuple.toList());
        exported[0] = 42;
        list.set(0, 43);
        assertArrayEquals(values, tuple.toArray());
        assertArrayEquals(values, tuple.toList().toArray());
        assertNull(tuple.elements);

        final long[] cached = tuple.elements();
        final long[] warmExport = tuple.toArray();
        assertNotSame(cached, warmExport);
        warmExport[0] = 44;
        tuple.toList().set(0, 45);
        assertArrayEquals(values, cached);
        assertArrayEquals(values, tuple.toArray());
        assertSame(cached, tuple.elements);
    }

    private static void checkExports(final float[] values) {
        final FloatTuple<?> tuple = FloatTuple.from(values);
        final float[] exported = tuple.toArray();
        final FloatList list = tuple.toList();

        assertArrayEquals(values, exported);
        assertArrayEquals(values, list.toArray());
        assertNotSame(exported, tuple.toArray());
        assertNotSame(list, tuple.toList());
        exported[0] = 42;
        list.set(0, 43);
        assertArrayEquals(values, tuple.toArray());
        assertArrayEquals(values, tuple.toList().toArray());
        assertNull(tuple.elements);

        final float[] cached = tuple.elements();
        final float[] warmExport = tuple.toArray();
        assertNotSame(cached, warmExport);
        warmExport[0] = 44;
        tuple.toList().set(0, 45);
        assertArrayEquals(values, cached);
        assertArrayEquals(values, tuple.toArray());
        assertSame(cached, tuple.elements);
    }

    private static void checkExports(final double[] values) {
        final DoubleTuple<?> tuple = DoubleTuple.from(values);
        final double[] exported = tuple.toArray();
        final DoubleList list = tuple.toList();

        assertArrayEquals(values, exported);
        assertArrayEquals(values, list.toArray());
        assertNotSame(exported, tuple.toArray());
        assertNotSame(list, tuple.toList());
        exported[0] = 42;
        list.set(0, 43);
        assertArrayEquals(values, tuple.toArray());
        assertArrayEquals(values, tuple.toList().toArray());
        assertNull(tuple.elements);

        final double[] cached = tuple.elements();
        final double[] warmExport = tuple.toArray();
        assertNotSame(cached, warmExport);
        warmExport[0] = 44;
        tuple.toList().set(0, 45);
        assertArrayEquals(values, cached);
        assertArrayEquals(values, tuple.toArray());
        assertSame(cached, tuple.elements);
    }

    @Test
    void testSingletonCallbacksPreserveValidationAndCheckedExceptionsWithoutCaching() throws IOException {
        final IntTuple.IntTuple1 ints = IntTuple.of(-7);
        final LongTuple.LongTuple1 longs = LongTuple.of(Long.MIN_VALUE);
        final FloatTuple.FloatTuple1 floats = FloatTuple.of(-0f);
        final DoubleTuple.DoubleTuple1 doubles = DoubleTuple.of(-0d);
        final int[] calls = new int[4];

        ints.forEach(value -> {
            assertEquals(-7, value);
            calls[0]++;
        });
        longs.forEach(value -> {
            assertEquals(Long.MIN_VALUE, value);
            calls[1]++;
        });
        floats.forEach(value -> {
            assertEquals(Float.floatToRawIntBits(-0f), Float.floatToRawIntBits(value));
            calls[2]++;
        });
        doubles.forEach(value -> {
            assertEquals(Double.doubleToRawLongBits(-0d), Double.doubleToRawLongBits(value));
            calls[3]++;
        });

        assertArrayEquals(new int[] { 1, 1, 1, 1 }, calls);
        assertThrows(IllegalArgumentException.class, () -> ints.forEach(null));
        assertThrows(IllegalArgumentException.class, () -> longs.forEach(null));
        assertThrows(IllegalArgumentException.class, () -> floats.forEach(null));
        assertThrows(IllegalArgumentException.class, () -> doubles.forEach(null));

        final IOException failure = new IOException("callback");
        assertSame(failure, assertThrows(IOException.class, () -> ints.forEach(value -> {
            throw failure;
        })));
        assertSame(failure, assertThrows(IOException.class, () -> longs.forEach(value -> {
            throw failure;
        })));
        assertSame(failure, assertThrows(IOException.class, () -> floats.forEach(value -> {
            throw failure;
        })));
        assertSame(failure, assertThrows(IOException.class, () -> doubles.forEach(value -> {
            throw failure;
        })));
        assertNull(ints.elements);
        assertNull(longs.elements);
        assertNull(floats.elements);
        assertNull(doubles.elements);
    }

    @Test
    void testLowerMedianRandomizedOrderingAndCacheIndependence() {
        final Random random = new Random(0x5E1EC7L);

        for (int arity = 4; arity <= 9; arity++) {
            for (int iteration = 0; iteration < 200; iteration++) {
                final int[] ints = new int[arity];
                final long[] longs = new long[arity];
                final float[] floats = new float[arity];
                final double[] doubles = new double[arity];

                for (int i = 0; i < arity; i++) {
                    ints[i] = random.nextInt();
                    longs[i] = random.nextLong();
                    floats[i] = Float.intBitsToFloat(random.nextInt());
                    doubles[i] = Double.longBitsToDouble(random.nextLong());
                }

                checkLowerMedian(ints);
                checkLowerMedian(longs);
                checkLowerMedian(floats);
                checkLowerMedian(doubles);
            }
        }
    }

    @Test
    void testLowerMedianFloatingPointTotalOrder() {
        final float[] floatValues = { Float.NEGATIVE_INFINITY, -Float.MAX_VALUE, -Float.MIN_VALUE, -0f, 0f, Float.MIN_VALUE, Float.MAX_VALUE,
                Float.POSITIVE_INFINITY, Float.NaN, Float.intBitsToFloat(0x7FC00001) };
        final double[] doubleValues = { Double.NEGATIVE_INFINITY, -Double.MAX_VALUE, -Double.MIN_VALUE, -0d, 0d, Double.MIN_VALUE, Double.MAX_VALUE,
                Double.POSITIVE_INFINITY, Double.NaN, Double.longBitsToDouble(0x7FF8000000000001L) };

        for (int arity = 4; arity <= 9; arity++) {
            for (int shift = 0; shift < floatValues.length; shift++) {
                final float[] floats = new float[arity];
                final double[] doubles = new double[arity];

                for (int i = 0; i < arity; i++) {
                    floats[i] = floatValues[(shift + arity - i) % floatValues.length];
                    doubles[i] = doubleValues[(shift + arity - i) % doubleValues.length];
                }

                checkLowerMedian(floats);
                checkLowerMedian(doubles);
                Arrays.fill(floats, Float.NaN);
                Arrays.fill(doubles, Double.NaN);
                checkLowerMedian(floats);
                checkLowerMedian(doubles);
            }
        }

        assertEquals(Float.floatToRawIntBits(-0f), Float.floatToRawIntBits(FloatTuple.of(0f, -0f, 0f, -0f).lowerMedian()));
        assertEquals(Double.doubleToRawLongBits(-0d), Double.doubleToRawLongBits(DoubleTuple.of(0d, -0d, 0d, -0d).lowerMedian()));
        assertTrue(Float.isNaN(FloatTuple.of(1f, Float.NaN, Float.NaN, Float.NaN).lowerMedian()));
        assertTrue(Double.isNaN(DoubleTuple.of(1d, Double.NaN, Double.NaN, Double.NaN).lowerMedian()));
    }

    private static void checkLowerMedian(final int[] values) {
        final IntTuple<?> tuple = IntTuple.from(values);
        final int expected = N.lowerMedian(values);

        assertEquals(expected, tuple.lowerMedian());
        assertNull(tuple.elements);
        assertArrayEquals(values, tuple.toArray());
        final int[] cached = tuple.elements();
        assertEquals(expected, tuple.lowerMedian());
        assertSame(cached, tuple.elements);
        assertArrayEquals(values, cached);
    }

    private static void checkLowerMedian(final long[] values) {
        final LongTuple<?> tuple = LongTuple.from(values);
        final long expected = N.lowerMedian(values);

        assertEquals(expected, tuple.lowerMedian());
        assertNull(tuple.elements);
        assertArrayEquals(values, tuple.toArray());
        final long[] cached = tuple.elements();
        assertEquals(expected, tuple.lowerMedian());
        assertSame(cached, tuple.elements);
        assertArrayEquals(values, cached);
    }

    private static void checkLowerMedian(final float[] values) {
        final FloatTuple<?> tuple = FloatTuple.from(values);
        final int expectedBits = Float.floatToIntBits(N.lowerMedian(values));

        assertEquals(expectedBits, Float.floatToIntBits(tuple.lowerMedian()));
        assertNull(tuple.elements);
        assertArrayEquals(values, tuple.toArray());
        final float[] cached = tuple.elements();
        assertEquals(expectedBits, Float.floatToIntBits(tuple.lowerMedian()));
        assertSame(cached, tuple.elements);
        assertArrayEquals(values, cached);
    }

    private static void checkLowerMedian(final double[] values) {
        final DoubleTuple<?> tuple = DoubleTuple.from(values);
        final long expectedBits = Double.doubleToLongBits(N.lowerMedian(values));

        assertEquals(expectedBits, Double.doubleToLongBits(tuple.lowerMedian()));
        assertNull(tuple.elements);
        assertArrayEquals(values, tuple.toArray());
        final double[] cached = tuple.elements();
        assertEquals(expectedBits, Double.doubleToLongBits(tuple.lowerMedian()));
        assertSame(cached, tuple.elements);
        assertArrayEquals(values, cached);
    }

    @Test
    void testDoubleSumNonfiniteInputsAtEveryPosition() {
        final double[] special = { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY };

        for (int arity = 2; arity <= 9; arity++) {
            for (int position = 0; position < arity; position++) {
                for (final double value : special) {
                    final double[] values = new double[arity];

                    for (int i = 0; i < arity; i++) {
                        values[i] = (i + 1) / 10d;
                    }

                    values[position] = value;
                    assertEquals(Double.doubleToLongBits(N.sum(values)), Double.doubleToLongBits(DoubleTuple.from(values).sum()));
                }
            }

            final double[] oppositeInfinities = new double[arity];
            oppositeInfinities[0] = Double.POSITIVE_INFINITY;
            oppositeInfinities[arity - 1] = Double.NEGATIVE_INFINITY;
            assertTrue(Double.isNaN(DoubleTuple.from(oppositeInfinities).sum()));
        }
    }

    @Test
    void testDoubleSumRetainsExactFiniteFallbackAndOrdinaryCompensation() {
        final double[][] cases = { { -3.0E307, Double.MAX_VALUE }, { Double.MAX_VALUE, Double.MAX_VALUE },
                { Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE }, { Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE },
                { -Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE } };

        for (final double[] values : cases) {
            BigDecimal exact = BigDecimal.ZERO;

            for (final double value : values) {
                exact = exact.add(new BigDecimal(value));
            }

            assertEquals(Double.doubleToLongBits(exact.doubleValue()), Double.doubleToLongBits(DoubleTuple.from(values).sum()));
        }

        assertEquals(1.0E16 + 2d, DoubleTuple.of(1.0E16, 1d, 1d).sum());
        assertEquals(Double.doubleToRawLongBits(-0d), Double.doubleToRawLongBits(DoubleTuple.of(-0d).sum()));
        assertEquals(Double.doubleToRawLongBits(0d), Double.doubleToRawLongBits(DoubleTuple.of(-0d, -0d).sum()));
    }

    @Test
    void testLongPairMedianMatchesExactFullDomainMean() {
        final long[] boundaries = { Long.MIN_VALUE, Long.MIN_VALUE + 1, -(1L << 53) - 1, -(1L << 53), -3, -2, -1, 0, 1, 2, 3,
                (1L << 53) - 1, 1L << 53, (1L << 53) + 1, Long.MAX_VALUE - 1, Long.MAX_VALUE };

        for (final long first : boundaries) {
            for (final long second : boundaries) {
                checkLongPairMedian(first, second);
            }
        }

        final Random random = new Random(0x10A6BEEFL);

        for (int i = 0; i < 10_000; i++) {
            checkLongPairMedian(random.nextLong(), random.nextLong());
        }

        assertEquals(-0.5d, LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE).median());
    }

    private static void checkLongPairMedian(final long first, final long second) {
        final double expected = BigInteger.valueOf(first).add(BigInteger.valueOf(second)).doubleValue() / 2d;
        assertEquals(Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(LongTuple.of(first, second).median()));
    }
}
