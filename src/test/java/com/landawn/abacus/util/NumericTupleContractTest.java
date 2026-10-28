package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NumericTupleContractTest {

    @Nested
    class AggregateTests {

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
    }

    @Nested
    class SumTests {

        @Test
        void compensatedDoubleSumCanDependOnElementOrder() {
            assertEquals(0.0, DoubleTuple.of(1.0E16, 1.0, -1.0E16).sum());
            assertEquals(1.0, DoubleTuple.of(1.0E16, -1.0E16, 1.0).sum());
        }

        @Test
        void doubleAggregationMatchesCompensationAndExactOverflowRecovery() {
            final double[] boundaries = { Double.NEGATIVE_INFINITY, -Double.MAX_VALUE, -3.0E307, -1.0E16, -1d, -Double.MIN_VALUE, -0d, 0d, Double.MIN_VALUE, 1d,
                    1.0E16, 3.0E307, Double.MAX_VALUE, Double.POSITIVE_INFINITY, Double.NaN };
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
            checkDoubleAggregation(
                    new double[] { Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE, 1d, 0x1.0p-53, Double.MIN_VALUE, 0d });
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
            assertDoubleAggregation(1.0000000000000002d, 0.12500000000000003d, Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE, 1d,
                    0x1.0p-53, Double.MIN_VALUE, 0d);
        }

        @Test
        void doubleSumKeepsCompensatedAndNonfiniteInputSemantics() {
            assertEquals(0d, DoubleTuple.of(1.0E16, 1d, -1.0E16).sum());
            assertEquals(1d, DoubleTuple.of(1.0E16, -1.0E16, 1d).sum());
            assertEquals(1.0E16 + 2d, DoubleTuple.of(1.0E16, 1d, 1d).sum());
            assertEquals(Double.POSITIVE_INFINITY, DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE).sum());
            assertEquals(Double.NEGATIVE_INFINITY, DoubleTuple.of(-Double.MAX_VALUE, -Double.MAX_VALUE).sum());
            assertEquals(Double.POSITIVE_INFINITY, DoubleTuple.of(1d, Double.POSITIVE_INFINITY).sum());
            assertEquals(Double.NEGATIVE_INFINITY, DoubleTuple.of(1d, Double.NEGATIVE_INFINITY).sum());
            assertTrue(Double.isNaN(DoubleTuple.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).sum()));
            assertTrue(Double.isNaN(DoubleTuple.of(1d, Double.NaN).sum()));
            assertEquals(Double.doubleToRawLongBits(0d), Double.doubleToRawLongBits(DoubleTuple.of(-0d, -0d).sum()));
        }

        @Test
        void doubleSumNonfiniteInputsAtEveryPosition() {
            final double[] special = { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY };

            for (int arity = 2; arity <= 9; arity++) {
                for (int position = 0; position < arity; position++) {
                    for (final double value : special) {
                        final double[] values = new double[arity];

                        for (int i = 0; i < arity; i++) {
                            values[i] = (i + 1) / 10d;
                        }

                        values[position] = value;
                        // The lone non-finite element decides the total.
                        assertEquals(Double.doubleToLongBits(value), Double.doubleToLongBits(DoubleTuple.from(values).sum()), Arrays.toString(values));

                        // Finite elements overflowing toward the opposite sign must not turn an infinity into NaN.
                        // N.sum and DoubleStream.sum return NaN here when the overflow precedes the infinity.
                        Arrays.fill(values, value == Double.POSITIVE_INFINITY ? -Double.MAX_VALUE : Double.MAX_VALUE);
                        values[position] = value;
                        assertEquals(Double.doubleToLongBits(value), Double.doubleToLongBits(DoubleTuple.from(values).sum()), Arrays.toString(values));
                    }
                }

                final double[] oppositeInfinities = new double[arity];
                oppositeInfinities[0] = Double.POSITIVE_INFINITY;
                oppositeInfinities[arity - 1] = Double.NEGATIVE_INFINITY;
                assertTrue(Double.isNaN(DoubleTuple.from(oppositeInfinities).sum()));
            }
        }

        @Test
        void floatAndDoubleSumsKeepALoneInfinityAfterAnOpposingFiniteOverflow() {
            // Floats cannot overflow FloatTuple's double accumulator; DoubleTuple must give the same answer although its finite elements overflow.
            for (int arity = 3; arity <= 9; arity++) {
                for (int position = 0; position < arity; position++) {
                    for (final boolean positive : new boolean[] { true, false }) {
                        final float[] floats = new float[arity];
                        final double[] doubles = new double[arity];
                        Arrays.fill(floats, positive ? -Float.MAX_VALUE : Float.MAX_VALUE);
                        Arrays.fill(doubles, positive ? -Double.MAX_VALUE : Double.MAX_VALUE);
                        floats[position] = positive ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
                        doubles[position] = positive ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

                        assertEquals(floats[position], FloatTuple.from(floats).sum(), Arrays.toString(floats));
                        assertEquals(doubles[position], DoubleTuple.from(doubles).sum(), Arrays.toString(doubles));
                    }
                }
            }
        }

        @Test
        void doubleSumRecomputesTheExactTotalWhenOnlyTheCompensationOverflows() {
            for (int arity = 4; arity <= 9; arity++) {
                for (final double sign : new double[] { 1d, -1d }) {
                    final double[] values = new double[arity];
                    values[0] = -sign * 3.0E307;
                    values[1] = sign * Double.MAX_VALUE;
                    values[2] = -sign * Double.MAX_VALUE;
                    values[3] = sign * 3.0E307;

                    assertEquals(0d, DoubleTuple.from(values).sum(), "arity " + arity + ", sign " + sign);
                }
            }
        }

        @Test
        void doubleSumRetainsExactFiniteFallbackAndOrdinaryCompensation() {
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
        void doubleSumRetainsSmallTermsWhenAnOverflowedPrefixLaterCancels() {
            for (int arity = 5; arity <= 9; arity++) {
                for (final double sign : new double[] { 1d, -1d }) {
                    final double[] values = new double[arity];
                    values[0] = sign * Double.MAX_VALUE;
                    values[1] = sign;
                    values[2] = sign * Double.MAX_VALUE;
                    values[3] = -sign * Double.MAX_VALUE;
                    values[4] = -sign * Double.MAX_VALUE;
                    final DoubleTuple<?> tuple = DoubleTuple.from(values);

                    assertEquals(sign, tuple.sum(), "arity " + arity + ", sign " + sign);
                    assertEquals(sign / arity, tuple.average().getAsDouble());
                    assertArrayEquals(values, tuple.toArray());
                }
            }

            assertEquals(0d, DoubleTuple.of(Double.MAX_VALUE, 1d, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE, -1d).sum());
        }
    }

    @Nested
    class AverageTests {

        @Test
        void doubleAveragePreservesFiniteOverflowAndOrdinaryFloatingPointSemantics() {
            for (int arity = 2; arity <= 9; arity++) {
                final double[] values = new double[arity];

                for (final double sign : new double[] { 1d, -1d }) {
                    Arrays.fill(values, sign * Double.MAX_VALUE);
                    assertEquals(sign * Double.MAX_VALUE, DoubleTuple.from(values).average().getAsDouble());
                }

                Arrays.fill(values, -0d);
                assertEquals(Double.doubleToRawLongBits(0d), Double.doubleToRawLongBits(DoubleTuple.from(values).average().getAsDouble()));

                for (final double special : new double[] { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY }) {
                    Arrays.fill(values, 1d);
                    values[arity - 1] = special;
                    assertEquals(special, DoubleTuple.from(values).average().getAsDouble());
                }
            }

            assertEquals(0d, DoubleTuple.of(1.0E16, 1d, -1.0E16).average().getAsDouble());
            assertEquals(1d / 3d, DoubleTuple.of(1.0E16, -1.0E16, 1d).average().getAsDouble());
            assertEquals(Double.doubleToRawLongBits(-0d), Double.doubleToRawLongBits(DoubleTuple.of(-Double.MIN_VALUE, 0d).average().getAsDouble()));
            assertTrue(Double.isNaN(DoubleTuple.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).average().getAsDouble()));
        }

        @Test
        void doubleAverageRecoversCancellationAfterCompensationOverflows() {
            for (int arity = 3; arity <= 9; arity++) {
                for (final double sign : new double[] { 1d, -1d }) {
                    final double[] values = new double[arity];
                    values[0] = -sign * 3.0E307;
                    values[1] = sign * Double.MAX_VALUE;
                    values[2] = -sign * Double.MAX_VALUE;

                    if (arity >= 4) {
                        values[3] = sign * 3.0E307;
                    }

                    final DoubleTuple<?> tuple = DoubleTuple.from(values);
                    assertEquals(arity == 3 ? -sign * 3.0E307 / arity : 0d, tuple.average().getAsDouble(), "arity " + arity + ", sign " + sign);
                    assertArrayEquals(values, tuple.toArray());
                }
            }
        }

        @Test
        void doubleAverageRetainsSmallTermsAfterAnOverflowedPrefixCancels() {
            for (int arity = 7; arity <= 9; arity++) {
                for (final double sign : new double[] { 1d, -1d }) {
                    final double[] values = Arrays.copyOf(new double[] { sign * Double.MAX_VALUE, sign, -sign * 3.0E307, sign * Double.MAX_VALUE,
                            -sign * Double.MAX_VALUE, -sign * Double.MAX_VALUE, sign * 3.0E307 }, arity);

                    assertEquals(sign / arity, DoubleTuple.from(values).average().getAsDouble(), "arity " + arity + ", sign " + sign);
                }
            }
        }

        @Test
        void doubleAverageRetainsTheSideOfAMidpointAfterOverflow() {
            for (final double sign : new double[] { 1d, -1d }) {
                final double[] values = { sign * Double.MAX_VALUE, sign * Double.MAX_VALUE, -sign * Double.MAX_VALUE, -sign * Double.MAX_VALUE, sign,
                        sign * 0x1.0p-53, sign * Double.MIN_VALUE, 0d };

                assertEquals(sign * Math.nextUp(0.125d), DoubleTuple.from(values).average().getAsDouble());

                final double underflow = DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE, sign * Double.MIN_VALUE)
                        .average()
                        .getAsDouble();
                assertEquals(Double.doubleToRawLongBits(sign * 0d), Double.doubleToRawLongBits(underflow));
            }
        }

        @Test
        void floatAndDoubleAveragesKeepALoneInfinityAfterAnOpposingFiniteOverflow() {
            // average() has its own aggregation path, so it needs the same every-position check as sum().
            for (int arity = 3; arity <= 9; arity++) {
                for (int position = 0; position < arity; position++) {
                    for (final boolean positive : new boolean[] { true, false }) {
                        final float[] floats = new float[arity];
                        final double[] doubles = new double[arity];
                        Arrays.fill(floats, positive ? -Float.MAX_VALUE : Float.MAX_VALUE);
                        Arrays.fill(doubles, positive ? -Double.MAX_VALUE : Double.MAX_VALUE);
                        floats[position] = positive ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
                        doubles[position] = positive ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

                        assertEquals(doubles[position], FloatTuple.from(floats).average().getAsDouble(), Arrays.toString(floats));
                        assertEquals(doubles[position], DoubleTuple.from(doubles).average().getAsDouble(), Arrays.toString(doubles));
                    }
                }
            }
        }

        @Test
        void floatAveragePreservesTheBinaryFloatValueWhenWidening() {
            for (int arity = 1; arity <= 9; arity++) {
                final float[] values = new float[arity];
                java.util.Arrays.fill(values, 0.1f);
                final double average = FloatTuple.from(values).average().getAsDouble();

                assertEquals(0.10000000149011612, average, "arity " + arity);
                assertNotEquals(0.1d, average, "arity " + arity);
            }
        }

        @Test
        void intAverageSucceedsWhenTheSumDoesNotFitInAnInt() {
            for (int arity = 2; arity <= 9; arity++) {
                for (final int value : new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE }) {
                    final int[] values = new int[arity];
                    java.util.Arrays.fill(values, value);
                    final IntTuple<?> tuple = IntTuple.from(values);

                    assertThrows(ArithmeticException.class, tuple::sum);
                    assertEquals(value, tuple.average().getAsDouble(), "arity " + arity);
                }
            }
        }
    }

    @Nested
    class MedianTests {

        @Test
        void longPairMedianMatchesExactFullDomainMean() {
            final long[] boundaries = { Long.MIN_VALUE, Long.MIN_VALUE + 1, -(1L << 53) - 1, -(1L << 53), -3, -2, -1, 0, 1, 2, 3, (1L << 53) - 1, 1L << 53,
                    (1L << 53) + 1, Long.MAX_VALUE - 1, Long.MAX_VALUE };

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

        @Test
        void mediansPreserveBoundaryMidpointsAndFloatingPointTotalOrder() {
            final float[] floatBounds = { Float.NEGATIVE_INFINITY, -Float.MAX_VALUE, -Float.MIN_VALUE, -0f, 0f, Float.MIN_VALUE, Float.MAX_VALUE,
                    Float.POSITIVE_INFINITY, Float.NaN };
            final double[] doubleBounds = { Double.NEGATIVE_INFINITY, -Double.MAX_VALUE, -Double.MIN_VALUE, -0d, 0d, Double.MIN_VALUE, Double.MAX_VALUE,
                    Double.POSITIVE_INFINITY, Double.NaN };
            final long[] longBounds = { Long.MIN_VALUE, Long.MIN_VALUE + 1, -(1L << 53) - 1, -1, 0, 1, (1L << 53) + 1, Long.MAX_VALUE - 1, Long.MAX_VALUE };
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
    }

    @Nested
    class LowerMedianTests {

        @Test
        void lowerMedianFloatingPointTotalOrder() {
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

        @Test
        void lowerMedianRandomizedOrderingAndCacheIndependence() {
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
    }

    @Nested
    class ExportTests {

        @Test
        void defensiveExportsDoNotInitializeCaches() {
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
    }

    @Nested
    class ForEachTests {

        @Test
        void singletonCallbacksPreserveValidationAndCheckedExceptionsWithoutCaching() throws IOException {
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
    }

    @Nested
    class ValueContractTests {

        @Test
        void unequalPermutationsCanHaveTheSameHashCode() {
            final DoubleTuple.DoubleTuple3 first = DoubleTuple.of(0.0, -0.0, 1.0);
            final DoubleTuple.DoubleTuple3 second = DoubleTuple.of(-0.0, 0.0, 1.0);
            assertNotEquals(first, second);
            assertEquals(first.hashCode(), second.hashCode());

            final LongTuple.LongTuple3 longs = LongTuple.of(0L, -1L, 1L);
            final LongTuple.LongTuple3 permutedLongs = LongTuple.of(-1L, 0L, 1L);
            assertNotEquals(longs, permutedLongs);
            assertEquals(longs.hashCode(), permutedLongs.hashCode());
        }
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
        final double expected = sorted.length % 2 == 0 ? BigInteger.valueOf(sorted[middle - 1]).add(BigInteger.valueOf(sorted[middle])).doubleValue() / 2d
                : sorted[middle];
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

    private static void assertDoubleAggregation(final double expectedSum, final double expectedAverage, final double... values) {
        final DoubleTuple<?> tuple = DoubleTuple.from(values);
        assertEquals(expectedSum, tuple.sum(), Arrays.toString(values));
        assertEquals(expectedAverage, tuple.average().getAsDouble(), Arrays.toString(values));
    }

    private static void checkDoubleAggregation(final double[] values) {
        final DoubleTuple<?> tuple = DoubleTuple.from(values);
        final double compensated = KahanSummation.of(values).sum();
        double expectedSum = compensated;
        double expectedAverage = compensated / values.length;

        if (Arrays.stream(values).anyMatch(value -> !Double.isFinite(value))) {
            // Only the non-finite elements decide such a total; an overflowing finite prefix must not turn one infinity into NaN.
            final boolean hasNaN = Arrays.stream(values).anyMatch(Double::isNaN);
            final boolean hasPositiveInfinity = Arrays.stream(values).anyMatch(value -> value == Double.POSITIVE_INFINITY);
            final boolean hasNegativeInfinity = Arrays.stream(values).anyMatch(value -> value == Double.NEGATIVE_INFINITY);
            expectedSum = hasNaN || (hasPositiveInfinity && hasNegativeInfinity) ? Double.NaN
                    : hasPositiveInfinity ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
            expectedAverage = expectedSum / values.length;
        } else if (!Double.isFinite(compensated)) {
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

    private static void checkLongPairMedian(final long first, final long second) {
        final double expected = BigInteger.valueOf(first).add(BigInteger.valueOf(second)).doubleValue() / 2d;
        assertEquals(Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(LongTuple.of(first, second).median()));
    }
}
