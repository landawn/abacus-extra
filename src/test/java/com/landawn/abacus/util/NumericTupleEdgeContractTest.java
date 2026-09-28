package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

class NumericTupleEdgeContractTest extends TestBase {

    @Test
    void compensatedDoubleSumCanDependOnElementOrder() {
        assertEquals(0.0, DoubleTuple.of(1.0E16, 1.0, -1.0E16).sum());
        assertEquals(1.0, DoubleTuple.of(1.0E16, -1.0E16, 1.0).sum());
    }

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
                assertEquals((double) value, tuple.average().getAsDouble(), "arity " + arity);
            }
        }
    }
}
