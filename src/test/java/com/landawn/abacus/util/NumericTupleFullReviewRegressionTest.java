package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

@SuppressWarnings("deprecation")
class NumericTupleFullReviewRegressionTest extends TestBase {

    @Test
    void intAggregatesPreserveExactTotalsAndOverflowAcrossEveryArity() {
        final Random random = new Random(0x1A663E6AL);

        for (int arity = 1; arity <= 9; arity++) {
            for (int iteration = 0; iteration < 100; iteration++) {
                final int[] values = new int[arity];

                for (int i = 0; i < arity; i++) {
                    values[i] = iteration == 0 ? Integer.MIN_VALUE : iteration == 1 ? Integer.MAX_VALUE : random.nextInt();
                }

                checkIntAggregates(values);
            }
        }

        checkIntAggregates(new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, -Integer.MAX_VALUE });
        checkIntAggregates(new int[] { Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE });
        checkIntAggregates(new int[] { Integer.MAX_VALUE, 1, -Integer.MAX_VALUE, -1 });
    }

    private static void checkIntAggregates(final int[] values) {
        final IntTuple<?> tuple = IntTuple.from(values);
        final long total = Arrays.stream(values).asLongStream().sum();

        assertEquals(Arrays.stream(values).min().orElseThrow(), tuple.min());
        assertEquals(Arrays.stream(values).max().orElseThrow(), tuple.max());
        assertEquals((double) total / values.length, tuple.average().getAsDouble());

        if (total < Integer.MIN_VALUE || total > Integer.MAX_VALUE) {
            assertThrows(ArithmeticException.class, tuple::sum);
        } else {
            assertEquals((int) total, tuple.sum());
        }

        assertNull(tuple.elements);
        assertArrayEquals(values, tuple.toArray());
    }

    @Test
    void longAggregatesPreserveExtremaAndModularOverflowAcrossEveryArity() {
        final Random random = new Random(0x10A66E6AL);

        for (int arity = 1; arity <= 9; arity++) {
            for (int iteration = 0; iteration < 100; iteration++) {
                final long[] values = new long[arity];
                BigInteger exact = BigInteger.ZERO;

                for (int i = 0; i < arity; i++) {
                    values[i] = iteration == 0 ? Long.MIN_VALUE : iteration == 1 ? Long.MAX_VALUE : random.nextLong();
                    exact = exact.add(BigInteger.valueOf(values[i]));
                }

                final LongTuple<?> tuple = LongTuple.from(values);
                assertEquals(Arrays.stream(values).min().orElseThrow(), tuple.min());
                assertEquals(Arrays.stream(values).max().orElseThrow(), tuple.max());
                assertEquals(exact.longValue(), tuple.sum());
                assertNull(tuple.elements);
                assertArrayEquals(values, tuple.toArray());
            }
        }
    }

    @Test
    void mediansPreserveOrderingWithoutRetainingOrMutatingElementArrays() {
        final Random random = new Random(0x4D3D1AAL);

        for (int arity = 1; arity <= 9; arity++) {
            for (int iteration = 0; iteration < 100; iteration++) {
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

                checkMedian(ints);
                checkMedian(longs);
                checkMedian(floats);
                checkMedian(doubles);
            }
        }
    }

    @Test
    void mediansPreserveBoundaryMidpointsAndFloatingPointTotalOrder() {
        final float[] floatBounds = { Float.NEGATIVE_INFINITY, -Float.MAX_VALUE, -Float.MIN_VALUE, -0f, 0f, Float.MIN_VALUE, Float.MAX_VALUE,
                Float.POSITIVE_INFINITY, Float.NaN };
        final double[] doubleBounds = { Double.NEGATIVE_INFINITY, -Double.MAX_VALUE, -Double.MIN_VALUE, -0d, 0d, Double.MIN_VALUE, Double.MAX_VALUE,
                Double.POSITIVE_INFINITY, Double.NaN };
        final long[] longBounds = { Long.MIN_VALUE, Long.MIN_VALUE + 1, -(1L << 53) - 1, -1, 0, 1, (1L << 53) + 1,
                Long.MAX_VALUE - 1, Long.MAX_VALUE };
        // One index selects the matching boundary of every type, so the three tables must stay the same size.
        assertEquals(doubleBounds.length, floatBounds.length);
        assertEquals(doubleBounds.length, longBounds.length);

        for (int arity = 2; arity <= 9; arity++) {
            for (int first = 0; first < doubleBounds.length; first++) {
                for (int second = 0; second < doubleBounds.length; second++) {
                    final float[] floats = new float[arity];
                    final double[] doubles = new double[arity];
                    final long[] longs = new long[arity];
                    Arrays.fill(floats, floatBounds[first]);
                    Arrays.fill(doubles, doubleBounds[first]);
                    Arrays.fill(longs, longBounds[first]);
                    floats[arity / 2] = floatBounds[second];
                    doubles[arity / 2] = doubleBounds[second];
                    longs[arity / 2] = longBounds[second];
                    checkMedian(floats);
                    checkMedian(doubles);
                    checkMedian(longs);
                }
            }
        }

        assertEquals(-0.5d, LongTuple.of(Long.MIN_VALUE, Long.MIN_VALUE, Long.MAX_VALUE, Long.MAX_VALUE).median());
        assertEquals(Double.MIN_VALUE, DoubleTuple.of(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE).median());
        assertEquals(Double.MAX_VALUE, DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE).median());
        assertEquals(Double.doubleToRawLongBits(-0d), Double.doubleToRawLongBits(DoubleTuple.of(-0d, -0d, -0d, -0d).median()));
        assertThrows(NoSuchElementException.class, () -> IntTuple.from(new int[0]).median());
        assertThrows(NoSuchElementException.class, () -> LongTuple.from(new long[0]).median());
        assertThrows(NoSuchElementException.class, () -> FloatTuple.from(new float[0]).median());
        assertThrows(NoSuchElementException.class, () -> DoubleTuple.from(new double[0]).median());
    }

    private static void checkMedian(final int[] values) {
        final IntTuple<?> tuple = IntTuple.from(values);
        final int[] sorted = values.clone();
        Arrays.sort(sorted);
        final int middle = sorted.length / 2;
        final double expected = sorted.length % 2 == 0 ? ((long) sorted[middle - 1] + sorted[middle]) / 2d : sorted[middle];
        assertEquals(expected, tuple.median());
        assertNull(tuple.elements);
        assertArrayEquals(values, tuple.toArray());
        final int[] cached = tuple.elements();
        assertEquals(expected, tuple.median());
        assertSame(cached, tuple.elements);
        assertArrayEquals(values, cached);
        assertArrayEquals(values, tuple.stream().toArray());
    }

    private static void checkMedian(final long[] values) {
        final LongTuple<?> tuple = LongTuple.from(values);
        final long[] sorted = values.clone();
        Arrays.sort(sorted);
        final int middle = sorted.length / 2;
        final double expected = sorted.length % 2 == 0
                ? BigInteger.valueOf(sorted[middle - 1]).add(BigInteger.valueOf(sorted[middle])).doubleValue() / 2d : sorted[middle];
        assertEquals(expected, tuple.median());
        assertNull(tuple.elements);
        assertArrayEquals(values, tuple.toArray());
        final long[] cached = tuple.elements();
        assertEquals(expected, tuple.median());
        assertSame(cached, tuple.elements);
        assertArrayEquals(values, cached);
        assertArrayEquals(values, tuple.stream().toArray());
    }

    private static void checkMedian(final float[] values) {
        final FloatTuple<?> tuple = FloatTuple.from(values);
        final long expectedBits = Double.doubleToLongBits(N.median(values));
        assertEquals(expectedBits, Double.doubleToLongBits(tuple.median()));
        assertNull(tuple.elements);
        assertArrayEquals(values, tuple.toArray());
        final float[] cached = tuple.elements();
        assertEquals(expectedBits, Double.doubleToLongBits(tuple.median()));
        assertSame(cached, tuple.elements);
        assertArrayEquals(values, cached);
        assertArrayEquals(values, tuple.stream().toArray());
    }

    private static void checkMedian(final double[] values) {
        final DoubleTuple<?> tuple = DoubleTuple.from(values);
        final long expectedBits = Double.doubleToLongBits(N.median(values));
        assertEquals(expectedBits, Double.doubleToLongBits(tuple.median()));
        assertNull(tuple.elements);
        assertArrayEquals(values, tuple.toArray());
        final double[] cached = tuple.elements();
        assertEquals(expectedBits, Double.doubleToLongBits(tuple.median()));
        assertSame(cached, tuple.elements);
        assertArrayEquals(values, cached);
        assertArrayEquals(values, tuple.stream().toArray());
    }

    @Test
    void doubleAggregationMatchesCompensationAndExactOverflowRecovery() {
        final double[] boundaries = { Double.NEGATIVE_INFINITY, -Double.MAX_VALUE, -3.0E307, -1.0E16, -1d, -Double.MIN_VALUE, -0d, 0d,
                Double.MIN_VALUE, 1d, 1.0E16, 3.0E307, Double.MAX_VALUE, Double.POSITIVE_INFINITY, Double.NaN };
        final Random random = new Random(0xC04E3A5A7EL);

        for (int arity = 2; arity <= 9; arity++) {
            for (final double boundary : boundaries) {
                for (int position = 0; position < arity; position++) {
                    final double[] values = new double[arity];
                    Arrays.fill(values, -0d);
                    values[position] = boundary;
                    checkDoubleAggregation(values);
                }
            }

            for (int iteration = 0; iteration < 200; iteration++) {
                final double[] values = new double[arity];

                for (int i = 0; i < arity; i++) {
                    values[i] = iteration % 2 == 0 ? boundaries[random.nextInt(boundaries.length)] : Double.longBitsToDouble(random.nextLong());
                }

                checkDoubleAggregation(values);
            }
        }

        checkDoubleAggregation(new double[] { Double.MAX_VALUE, 1d, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE });
        checkDoubleAggregation(new double[] { -3.0E307, Double.MAX_VALUE, -Double.MAX_VALUE, 3.0E307 });
        checkDoubleAggregation(new double[] { 1.0E16, 1d, 1d });
        checkDoubleAggregation(new double[] { 1.0E16, 1d, -1.0E16 });
        checkDoubleAggregation(new double[] { Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE,
                1d, 0x1.0p-53, Double.MIN_VALUE, 0d });
    }

    @Test
    void doubleOverflowRecoveryReturnsCorrectlyRoundedExactResults() {
        // Expected values are the exact rational sum and mean rounded once to double, computed independently of
        // the production recovery, so a wrong recovery policy fails here instead of only drifting from itself.
        assertDoubleAggregation(1.4976931348623158E308, 7.488465674311579E307, -3.0E307, Double.MAX_VALUE);
        assertDoubleAggregation(1d, 0.2d, Double.MAX_VALUE, 1d, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
        assertDoubleAggregation(0d, 0d, -3.0E307, Double.MAX_VALUE, -Double.MAX_VALUE, 3.0E307);
        assertDoubleAggregation(Double.POSITIVE_INFINITY, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        assertDoubleAggregation(Double.POSITIVE_INFINITY, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        assertDoubleAggregation(Double.NEGATIVE_INFINITY, -1.3482698511467367E308, -Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE, 1d);
        // 1 + 2^-53 is a tie that rounds down to 1; the trailing Double.MIN_VALUE breaks it upward only if every term is kept.
        assertDoubleAggregation(1.0000000000000002d, 0.12500000000000003d, Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE,
                -Double.MAX_VALUE, 1d, 0x1.0p-53, Double.MIN_VALUE, 0d);
    }

    private static void assertDoubleAggregation(final double expectedSum, final double expectedAverage, final double... values) {
        final DoubleTuple<?> tuple = DoubleTuple.from(values);
        assertEquals(expectedSum, tuple.sum(), Arrays.toString(values));
        assertEquals(expectedAverage, tuple.average().getAsDouble(), Arrays.toString(values));
    }

    // Differential check against the library's Kahan sum; the pinned test above checks the recovered values themselves.
    private static void checkDoubleAggregation(final double[] values) {
        final DoubleTuple<?> tuple = DoubleTuple.from(values);
        final double compensated = KahanSummation.of(values).sum();
        double expectedSum = compensated;
        double expectedAverage = compensated / values.length;

        if (!Double.isFinite(compensated) && Arrays.stream(values).allMatch(Double::isFinite)) {
            BigDecimal exact = BigDecimal.ZERO;

            for (final double value : values) {
                exact = exact.add(new BigDecimal(value));
            }

            expectedSum = exact.doubleValue();
            expectedAverage = exact.divide(BigDecimal.valueOf(values.length), new MathContext(1400, RoundingMode.HALF_EVEN)).doubleValue();
        }

        assertEquals(Double.doubleToLongBits(expectedSum), Double.doubleToLongBits(tuple.sum()), Arrays.toString(values));
        assertEquals(Double.doubleToLongBits(expectedAverage), Double.doubleToLongBits(tuple.average().getAsDouble()), Arrays.toString(values));
        assertNull(tuple.elements);
        assertArrayEquals(values, tuple.toArray());
    }
}
