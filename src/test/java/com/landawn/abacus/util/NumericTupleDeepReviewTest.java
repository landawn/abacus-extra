package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

@SuppressWarnings("deprecation")
class NumericTupleDeepReviewTest extends TestBase {

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
}
