package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

@SuppressWarnings("deprecation")
class SmallTupleFullReviewRegressionTest extends TestBase {

    @Test
    void byteAggregatesMatchAnIndependentOracleAtEveryArityWithoutCaching() {
        for (int arity = 1; arity <= 9; arity++) {
            for (final int[] input : aggregateInputs(arity, Byte.MIN_VALUE, Byte.MAX_VALUE)) {
                final byte[] values = new byte[arity];

                for (int i = 0; i < arity; i++) {
                    values[i] = (byte) input[i];
                }

                final ByteTuple<?> tuple = ByteTuple.from(values);
                final int[] sorted = input.clone();
                java.util.Arrays.sort(sorted);
                final int sum = java.util.Arrays.stream(input).sum();

                assertEquals(sorted[0], tuple.min());
                assertEquals(sorted[arity - 1], tuple.max());
                assertEquals(sum, tuple.sum());
                assertEquals((double) sum / arity, tuple.average().getAsDouble());
                assertEquals((sorted[(arity - 1) / 2] + sorted[arity / 2]) / 2d, tuple.median());
                assertEquals(sorted[(arity - 1) / 2], tuple.lowerMedian());
                assertNull(tuple.elements, "Scalar aggregates must not retain an array cache");
                assertArrayEquals(values, tuple.toArray());
                assertArrayEquals(values, tuple.stream().toArray());
            }
        }
    }

    @Test
    void shortAggregatesMatchAnIndependentOracleAtEveryArityWithoutCaching() {
        for (int arity = 1; arity <= 9; arity++) {
            for (final int[] input : aggregateInputs(arity, Short.MIN_VALUE, Short.MAX_VALUE)) {
                final short[] values = new short[arity];

                for (int i = 0; i < arity; i++) {
                    values[i] = (short) input[i];
                }

                final ShortTuple<?> tuple = ShortTuple.from(values);
                final int[] sorted = input.clone();
                java.util.Arrays.sort(sorted);
                final int sum = java.util.Arrays.stream(input).sum();

                assertEquals(sorted[0], tuple.min());
                assertEquals(sorted[arity - 1], tuple.max());
                assertEquals(sum, tuple.sum());
                assertEquals((double) sum / arity, tuple.average().getAsDouble());
                assertEquals((sorted[(arity - 1) / 2] + sorted[arity / 2]) / 2d, tuple.median());
                assertEquals(sorted[(arity - 1) / 2], tuple.lowerMedian());
                assertNull(tuple.elements, "Scalar aggregates must not retain an array cache");
                assertArrayEquals(values, tuple.toArray());
                assertArrayEquals(values, tuple.stream().toArray());
            }
        }
    }

    @Test
    void charAggregatesMatchAnIndependentUnsignedOracleAtEveryArityWithoutCaching() {
        for (int arity = 1; arity <= 9; arity++) {
            for (final int[] input : aggregateInputs(arity, Character.MIN_VALUE, Character.MAX_VALUE)) {
                final char[] values = new char[arity];

                for (int i = 0; i < arity; i++) {
                    values[i] = (char) input[i];
                }

                final CharTuple<?> tuple = CharTuple.from(values);
                final int[] sorted = input.clone();
                java.util.Arrays.sort(sorted);
                final int sum = java.util.Arrays.stream(input).sum();

                assertEquals(sorted[0], tuple.min());
                assertEquals(sorted[arity - 1], tuple.max());
                assertEquals(sum, tuple.sum());
                assertEquals((double) sum / arity, tuple.average().getAsDouble());
                assertEquals(sorted[(arity - 1) / 2], tuple.lowerMedian());
                assertNull(tuple.elements, "Scalar aggregates must not retain an array cache");
                assertArrayEquals(values, tuple.toArray());
                assertArrayEquals(values, tuple.stream().toArray());
            }
        }
    }

    @Test
    void medianLeavesPreviouslyCachedElementsInTheirOriginalOrder() {
        final byte[] byteValues = { Byte.MAX_VALUE, Byte.MIN_VALUE, 2, -2, 0, 3, -3, 1, -1 };
        final short[] shortValues = { Short.MAX_VALUE, Short.MIN_VALUE, 2000, -2000, 0, 3000, -3000, 1000, -1000 };

        for (int arity = 1; arity <= 9; arity++) {
            final byte[] expectedBytes = java.util.Arrays.copyOf(byteValues, arity);
            final short[] expectedShorts = java.util.Arrays.copyOf(shortValues, arity);
            final ByteTuple<?> byteTuple = ByteTuple.from(expectedBytes);
            final ShortTuple<?> shortTuple = ShortTuple.from(expectedShorts);
            final byte[] cachedBytes = byteTuple.elements();
            final short[] cachedShorts = shortTuple.elements();
            final byte[] sortedBytes = expectedBytes.clone();
            final short[] sortedShorts = expectedShorts.clone();
            java.util.Arrays.sort(sortedBytes);
            java.util.Arrays.sort(sortedShorts);

            assertEquals((sortedBytes[(arity - 1) / 2] + sortedBytes[arity / 2]) / 2d, byteTuple.median());
            assertEquals((sortedShorts[(arity - 1) / 2] + sortedShorts[arity / 2]) / 2d, shortTuple.median());
            assertSame(cachedBytes, byteTuple.elements());
            assertSame(cachedShorts, shortTuple.elements());
            assertArrayEquals(expectedBytes, cachedBytes);
            assertArrayEquals(expectedShorts, cachedShorts);
            assertArrayEquals(expectedBytes, byteTuple.stream().toArray());
            assertArrayEquals(expectedShorts, shortTuple.stream().toArray());
        }
    }

    @Test
    void everyBooleanPatternPreservesValueAndHashContractsAcrossAllArities() {
        for (int arity = 0; arity <= 9; arity++) {
            for (int pattern = 0; pattern < 1 << arity; pattern++) {
                final boolean[] values = new boolean[arity];
                final boolean[] reversed = new boolean[arity];
                int expectedHash = 0;

                for (int i = 0; i < arity; i++) {
                    values[i] = (pattern & 1 << i) != 0;
                    reversed[arity - i - 1] = values[i];
                    expectedHash = 31 * expectedHash + Boolean.hashCode(values[i]);
                }

                final BooleanTuple<?> tuple = BooleanTuple.from(values);
                final BooleanTuple<?> copy = BooleanTuple.from(values.clone());

                assertEquals(arity, tuple.arity());
                assertEquals(pattern != 0, tuple.contains(true));
                assertEquals(arity > 0 && pattern != (1 << arity) - 1, tuple.contains(false));
                assertEquals(tuple, copy);
                assertEquals(tuple.hashCode(), copy.hashCode());
                if (arity > 0) {
                    assertEquals(expectedHash, tuple.hashCode());
                }
                assertArrayEquals(values, tuple.toArray());
                assertArrayEquals(reversed, tuple.reversed().toArray());
                assertEquals(tuple, tuple.reversed().reversed());
            }
        }
    }

    private static List<int[]> aggregateInputs(final int arity, final int minValue, final int maxValue) {
        final List<int[]> inputs = new ArrayList<>();

        for (final int repeated : new int[] { minValue, maxValue, 0 }) {
            final int[] values = new int[arity];
            java.util.Arrays.fill(values, repeated);
            inputs.add(values);
        }

        final int[] alternating = new int[arity];
        for (int i = 0; i < arity; i++) {
            alternating[i] = i % 2 == 0 ? minValue : maxValue;
        }
        inputs.add(alternating);

        for (int position = 0; position < arity; position++) {
            final int[] minimumAtPosition = new int[arity];
            final int[] maximumAtPosition = new int[arity];
            java.util.Arrays.fill(minimumAtPosition, maxValue);
            java.util.Arrays.fill(maximumAtPosition, minValue);
            minimumAtPosition[position] = minValue;
            maximumAtPosition[position] = maxValue;
            inputs.add(minimumAtPosition);
            inputs.add(maximumAtPosition);
        }

        final Random random = new Random(0x5EEDL + arity + minValue);
        for (int sample = 0; sample < 32; sample++) {
            final int[] values = new int[arity];
            for (int i = 0; i < arity; i++) {
                values[i] = minValue + random.nextInt(maxValue - minValue + 1);
            }
            inputs.add(values);
        }

        return inputs;
    }
}
