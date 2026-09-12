/*
 * Copyright (C) 2026 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

@Tag("2025")
class ArrayConversionsTest extends TestBase {

    @Test
    void nonZeroUsesNonZeroRatherThanPositiveSemantics() {
        final byte[] bytes = { Byte.MIN_VALUE, -1, 0, 1, Byte.MAX_VALUE };
        final int[] ints = { Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE };

        final boolean[] byteResult = ArrayConversions.nonZero(bytes);
        final boolean[] intResult = ArrayConversions.nonZero(ints);

        assertArrayEquals(new boolean[] { true, true, false, true, true }, byteResult);
        assertArrayEquals(new boolean[] { true, true, false, true, true }, intResult);
        assertNotSame(byteResult, ArrayConversions.nonZero(bytes));
        assertNotSame(intResult, ArrayConversions.nonZero(ints));
    }

    @Test
    void nonZeroMultidimensionalOverloadsPreserveNullTopology() {
        final byte[] aliasedByteRow = { -1, 0 };
        final byte[][] bytes2d = { aliasedByteRow, null, {}, aliasedByteRow };
        final byte[][][] bytes3d = { bytes2d, null, {} };
        final int[][] ints2d = { { -1, 0 }, null, {} };
        final int[][][] ints3d = { ints2d, null, ints2d };

        final boolean[][] byte2dResult = ArrayConversions.nonZero(bytes2d);
        final boolean[][][] byte3dResult = ArrayConversions.nonZero(bytes3d);
        final boolean[][] int2dResult = ArrayConversions.nonZero(ints2d);
        final boolean[][][] int3dResult = ArrayConversions.nonZero(ints3d);

        assertArrayEquals(new boolean[] { true, false }, byte2dResult[0]);
        assertNull(byte2dResult[1]);
        assertArrayEquals(new boolean[0], byte2dResult[2]);
        assertArrayEquals(byte2dResult[0], byte2dResult[3]);
        assertNotSame(byte2dResult[0], byte2dResult[3]);
        assertArrayEquals(new boolean[] { true, false }, byte3dResult[0][0]);
        assertNull(byte3dResult[0][1]);
        assertNull(byte3dResult[1]);

        assertArrayEquals(new boolean[] { true, false }, int2dResult[0]);
        assertNull(int2dResult[1]);
        assertArrayEquals(new boolean[0], int2dResult[2]);
        assertArrayEquals(new boolean[] { true, false }, int3dResult[0][0]);
        assertNull(int3dResult[0][1]);
        assertNull(int3dResult[1]);
        assertArrayEquals(int3dResult[0][0], int3dResult[2][0]);
        assertNotSame(int3dResult[0], int3dResult[2]);
        assertNotSame(int3dResult[0][0], int3dResult[2][0]);
    }

    @Test
    void toCharExactRejectsNarrowingWraparoundAndPreservesNullTopology() {
        assertArrayEquals(new char[] { Character.MIN_VALUE, 'A', '\uD800', '\uDC00', Character.MAX_VALUE },
                ArrayConversions.toCharExact(new int[] { Character.MIN_VALUE, 'A', 0xD800, 0xDC00, Character.MAX_VALUE }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toCharExact(new int[] { -1 }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toCharExact(new int[] { Character.MAX_VALUE + 1 }));

        final char[][] result2d = ArrayConversions.toCharExact(new int[][] { { 'A' }, null, {} });
        final char[][][] result3d = ArrayConversions.toCharExact(new int[][][] { { { 'B' }, null }, null });

        assertArrayEquals(new char[] { 'A' }, result2d[0]);
        assertNull(result2d[1]);
        assertArrayEquals(new char[0], result2d[2]);
        assertArrayEquals(new char[] { 'B' }, result3d[0][0]);
        assertNull(result3d[0][1]);
        assertNull(result3d[1]);
    }

    @Test
    void toIntExactAcceptsExactlyRepresentableValuesAndBothZeros() {
        assertArrayEquals(new int[] { Integer.MIN_VALUE, -1, 0, 0, 1, Integer.MAX_VALUE },
                ArrayConversions.toIntExact(new double[] { Integer.MIN_VALUE, -1, -0.0d, 0.0d, 1, Integer.MAX_VALUE }));
        assertArrayEquals(new int[] { Integer.MIN_VALUE, -1, 0, 0, 1, 1 << 24 },
                ArrayConversions.toIntExact(new float[] { Integer.MIN_VALUE, -1, -0.0f, 0.0f, 1, 1 << 24 }));
    }

    @Test
    void toIntExactRejectsNonFiniteFractionalAndOutOfRangeValues() {
        final double[] rejectedDoubles = { Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, -1.5d, 1.5d,
                Math.nextDown((double) Integer.MIN_VALUE), Math.nextUp((double) Integer.MAX_VALUE) };
        final float[] rejectedFloats = { Float.NaN, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, -1.5f, 1.5f,
                Math.nextDown((float) Integer.MIN_VALUE), (float) Integer.MAX_VALUE };

        for (final double value : rejectedDoubles) {
            assertThrows(ArithmeticException.class, () -> ArrayConversions.toIntExact(new double[] { value }), Double.toString(value));
        }

        for (final float value : rejectedFloats) {
            assertThrows(ArithmeticException.class, () -> ArrayConversions.toIntExact(new float[] { value }), Float.toString(value));
        }
    }

    @Test
    void toLongExactHandlesTheRoundedUpperBoundaryCorrectly() {
        final double greatestRepresentableLongBelowUpperBound = Math.nextDown(0x1p63);
        final float greatestRepresentableLongBelowUpperBoundAsFloat = Math.nextDown(0x1p63f);

        assertArrayEquals(new long[] { Long.MIN_VALUE, (long) greatestRepresentableLongBelowUpperBound },
                ArrayConversions.toLongExact(new double[] { -0x1p63, greatestRepresentableLongBelowUpperBound }));
        assertArrayEquals(new long[] { Long.MIN_VALUE, (long) greatestRepresentableLongBelowUpperBoundAsFloat },
                ArrayConversions.toLongExact(new float[] { -0x1p63f, greatestRepresentableLongBelowUpperBoundAsFloat }));
        assertArrayEquals(new long[] { 0, 0 }, ArrayConversions.toLongExact(new double[] { -0.0d, 0.0d }));
        assertArrayEquals(new long[] { 0, 0 }, ArrayConversions.toLongExact(new float[] { -0.0f, 0.0f }));

        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(new double[] { (double) Long.MAX_VALUE }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(new double[] { 0x1p63 }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(new double[] { Math.nextDown(-0x1p63) }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(new double[] { 0.5d }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(new double[] { Double.NaN }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(new double[] { Double.NEGATIVE_INFINITY }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(new double[] { Double.POSITIVE_INFINITY }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(new float[] { (float) Long.MAX_VALUE }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(new float[] { Float.NaN }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(new float[] { Float.NEGATIVE_INFINITY }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(new float[] { Float.POSITIVE_INFINITY }));
    }

    @Test
    void integralToFloatExactUsesBinaryRepresentabilityRatherThanRoundTripCasting() {
        assertArrayEquals(new float[] { Integer.MIN_VALUE, -(1 << 24), 0.0f, 1 << 24, (1 << 24) + 2 },
                ArrayConversions.toFloatExact(new int[] { Integer.MIN_VALUE, -(1 << 24), 0, 1 << 24, (1 << 24) + 2 }));
        assertArrayEquals(new float[] { Long.MIN_VALUE, -(1L << 40), 1L << 40, (1L << 40) + (1L << 17) },
                ArrayConversions.toFloatExact(new long[] { Long.MIN_VALUE, -(1L << 40), 1L << 40, (1L << 40) + (1L << 17) }));

        assertThrows(ArithmeticException.class, () -> ArrayConversions.toFloatExact(new int[] { (1 << 24) + 1 }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toFloatExact(new int[] { Integer.MAX_VALUE }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toFloatExact(new long[] { (1L << 40) + 1 }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toFloatExact(new long[] { Long.MAX_VALUE }));
    }

    @Test
    void longToDoubleExactUsesBinaryRepresentability() {
        assertArrayEquals(new double[] { Long.MIN_VALUE, -(1L << 53), 0, 1L << 53, (1L << 53) + 2 },
                ArrayConversions.toDoubleExact(new long[] { Long.MIN_VALUE, -(1L << 53), 0, 1L << 53, (1L << 53) + 2 }));

        assertThrows(ArithmeticException.class, () -> ArrayConversions.toDoubleExact(new long[] { (1L << 53) + 1 }));
        assertThrows(ArithmeticException.class, () -> ArrayConversions.toDoubleExact(new long[] { Long.MAX_VALUE }));
    }

    @Test
    void everyExactMultidimensionalOverloadPreservesNullInnerArrays() {
        final int[][] intFromFloat2d = ArrayConversions.toIntExact(new float[][] { { 1 }, null });
        final int[][][] intFromFloat3d = ArrayConversions.toIntExact(new float[][][] { { { 1 }, null }, null });
        final int[][] intFromDouble2d = ArrayConversions.toIntExact(new double[][] { { 1 }, null });
        final int[][][] intFromDouble3d = ArrayConversions.toIntExact(new double[][][] { { { 1 }, null }, null });
        final long[][] longFromFloat2d = ArrayConversions.toLongExact(new float[][] { { 1 }, null });
        final long[][][] longFromFloat3d = ArrayConversions.toLongExact(new float[][][] { { { 1 }, null }, null });
        final long[][] longFromDouble2d = ArrayConversions.toLongExact(new double[][] { { 1 }, null });
        final long[][][] longFromDouble3d = ArrayConversions.toLongExact(new double[][][] { { { 1 }, null }, null });
        final float[][] floatFromInt2d = ArrayConversions.toFloatExact(new int[][] { { 1 }, null });
        final float[][][] floatFromInt3d = ArrayConversions.toFloatExact(new int[][][] { { { 1 }, null }, null });
        final float[][] floatFromLong2d = ArrayConversions.toFloatExact(new long[][] { { 1 }, null });
        final float[][][] floatFromLong3d = ArrayConversions.toFloatExact(new long[][][] { { { 1 }, null }, null });
        final double[][] doubleFromLong2d = ArrayConversions.toDoubleExact(new long[][] { { 1 }, null });
        final double[][][] doubleFromLong3d = ArrayConversions.toDoubleExact(new long[][][] { { { 1 }, null }, null });

        assertNull(intFromFloat2d[1]);
        assertNull(intFromFloat3d[0][1]);
        assertNull(intFromFloat3d[1]);
        assertNull(intFromDouble2d[1]);
        assertNull(intFromDouble3d[0][1]);
        assertNull(intFromDouble3d[1]);
        assertNull(longFromFloat2d[1]);
        assertNull(longFromFloat3d[0][1]);
        assertNull(longFromFloat3d[1]);
        assertNull(longFromDouble2d[1]);
        assertNull(longFromDouble3d[0][1]);
        assertNull(longFromDouble3d[1]);
        assertNull(floatFromInt2d[1]);
        assertNull(floatFromInt3d[0][1]);
        assertNull(floatFromInt3d[1]);
        assertNull(floatFromLong2d[1]);
        assertNull(floatFromLong3d[0][1]);
        assertNull(floatFromLong3d[1]);
        assertNull(doubleFromLong2d[1]);
        assertNull(doubleFromLong3d[0][1]);
        assertNull(doubleFromLong3d[1]);

        assertEquals(1, intFromFloat2d[0][0]);
        assertEquals(1, intFromDouble2d[0][0]);
        assertEquals(1L, longFromFloat2d[0][0]);
        assertEquals(1L, longFromDouble2d[0][0]);
        assertEquals(1.0f, floatFromInt2d[0][0]);
        assertEquals(1.0f, floatFromLong2d[0][0]);
        assertEquals(1.0d, doubleFromLong2d[0][0]);
    }

    @Test
    void everyOverloadRejectsANullOuterArray() {
        assertThrows(NullPointerException.class, () -> ArrayConversions.nonZero((byte[]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.nonZero((byte[][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.nonZero((byte[][][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.nonZero((int[]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.nonZero((int[][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.nonZero((int[][][]) null));

        assertThrows(NullPointerException.class, () -> ArrayConversions.toCharExact((int[]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toCharExact((int[][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toCharExact((int[][][]) null));

        assertThrows(NullPointerException.class, () -> ArrayConversions.toIntExact((float[]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toIntExact((float[][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toIntExact((float[][][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toIntExact((double[]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toIntExact((double[][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toIntExact((double[][][]) null));

        assertThrows(NullPointerException.class, () -> ArrayConversions.toLongExact((float[]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toLongExact((float[][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toLongExact((float[][][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toLongExact((double[]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toLongExact((double[][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toLongExact((double[][][]) null));

        assertThrows(NullPointerException.class, () -> ArrayConversions.toFloatExact((int[]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toFloatExact((int[][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toFloatExact((int[][][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toFloatExact((long[]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toFloatExact((long[][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toFloatExact((long[][][]) null));

        assertThrows(NullPointerException.class, () -> ArrayConversions.toDoubleExact((long[]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toDoubleExact((long[][]) null));
        assertThrows(NullPointerException.class, () -> ArrayConversions.toDoubleExact((long[][][]) null));
    }

    @Test
    void emptyOneDimensionalResultsAreNewArrays() {
        assertNotSame(ArrayConversions.nonZero(new byte[0]), ArrayConversions.nonZero(new byte[0]));
        assertNotSame(ArrayConversions.nonZero(new int[0]), ArrayConversions.nonZero(new int[0]));
        assertNotSame(ArrayConversions.toCharExact(new int[0]), ArrayConversions.toCharExact(new int[0]));
        assertNotSame(ArrayConversions.toIntExact(new float[0]), ArrayConversions.toIntExact(new float[0]));
        assertNotSame(ArrayConversions.toIntExact(new double[0]), ArrayConversions.toIntExact(new double[0]));
        assertNotSame(ArrayConversions.toLongExact(new float[0]), ArrayConversions.toLongExact(new float[0]));
        assertNotSame(ArrayConversions.toLongExact(new double[0]), ArrayConversions.toLongExact(new double[0]));
        assertNotSame(ArrayConversions.toFloatExact(new int[0]), ArrayConversions.toFloatExact(new int[0]));
        assertNotSame(ArrayConversions.toFloatExact(new long[0]), ArrayConversions.toFloatExact(new long[0]));
        assertNotSame(ArrayConversions.toDoubleExact(new long[0]), ArrayConversions.toDoubleExact(new long[0]));
    }

    @Test
    void failureDoesNotModifyTheSource() {
        final double[][] source = { { 1, 2 }, { 3, Double.NaN }, null };
        final double[][] snapshot = { source[0].clone(), source[1].clone(), null };

        assertThrows(ArithmeticException.class, () -> ArrayConversions.toLongExact(source));
        assertArrayEquals(snapshot[0], source[0]);
        assertArrayEquals(snapshot[1], source[1]);
        assertNull(source[2]);
    }
}
