package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

@SuppressWarnings("deprecation")
class NumericTupleReviewRegressionTest extends TestBase {

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
            final double[] values = { sign * Double.MAX_VALUE, sign * Double.MAX_VALUE, -sign * Double.MAX_VALUE, -sign * Double.MAX_VALUE,
                    sign, sign * 0x1.0p-53, sign * Double.MIN_VALUE, 0d };

            assertEquals(sign * Math.nextUp(0.125d), DoubleTuple.from(values).average().getAsDouble());

            final double underflow = DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE, sign * Double.MIN_VALUE)
                    .average()
                    .getAsDouble();
            assertEquals(Double.doubleToRawLongBits(sign * 0d), Double.doubleToRawLongBits(underflow));
        }
    }

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
}
