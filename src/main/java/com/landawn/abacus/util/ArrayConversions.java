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

import java.util.Objects;

import com.landawn.abacus.annotation.Beta;

/**
 * Policy-explicit conversions for primitive arrays.
 *
 * <p>The similarly named conversion methods on {@link Arrays} follow Java primitive-cast rules and
 * often normalize {@code null} to an empty array. This class is deliberately strict:</p>
 * <ul>
 *   <li>the outer source array is required and a {@link NullPointerException} is thrown for {@code null};</li>
 *   <li>{@link #nonZero(byte[])} and {@link #nonZero(int[])} map every non-zero value, including a
 *       negative value, to {@code true};</li>
 *   <li>methods ending in {@code Exact} reject a value that is non-finite, fractional, out of range,
 *       or not exactly representable in the target type; floating-point positive and negative zero
 *       both convert to integral zero;</li>
 *   <li>multidimensional overloads preserve null inner arrays instead of replacing them with empty arrays.</li>
 * </ul>
 *
 * <p>All operations return newly allocated storage and never modify the source. Multidimensional
 * results preserve dimensions, ragged lengths, and null placement, but each non-null inner-array
 * occurrence is allocated independently; alias relationships between source inner arrays are not reproduced.</p>
 */
@Beta
public final class ArrayConversions {

    private static final int FLOAT_SIGNIFICAND_BITS = 24;

    private static final int DOUBLE_SIGNIFICAND_BITS = 53;

    private ArrayConversions() {
        // Utility class.
    }

    /**
     * Converts bytes to booleans using non-zero semantics.
     *
     * @param source the required source array
     * @return a new array in which an element is {@code true} exactly when the corresponding byte is not zero
     * @throws NullPointerException if {@code source} is {@code null}
     */
    public static boolean[] nonZero(final byte[] source) {
        Objects.requireNonNull(source, "source");

        final boolean[] result = new boolean[source.length];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] != 0;
        }

        return result;
    }

    /**
     * Two-dimensional counterpart of {@link #nonZero(byte[])}. Null rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     */
    public static boolean[][] nonZero(final byte[][] source) {
        Objects.requireNonNull(source, "source");

        final boolean[][] result = new boolean[source.length][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : nonZero(source[i]);
        }

        return result;
    }

    /**
     * Three-dimensional counterpart of {@link #nonZero(byte[])}. Null slices and rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     */
    public static boolean[][][] nonZero(final byte[][][] source) {
        Objects.requireNonNull(source, "source");

        final boolean[][][] result = new boolean[source.length][][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : nonZero(source[i]);
        }

        return result;
    }

    /**
     * Converts integers to booleans using non-zero semantics.
     *
     * @param source the required source array
     * @return a new array in which an element is {@code true} exactly when the corresponding integer is not zero
     * @throws NullPointerException if {@code source} is {@code null}
     */
    public static boolean[] nonZero(final int[] source) {
        Objects.requireNonNull(source, "source");

        final boolean[] result = new boolean[source.length];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] != 0;
        }

        return result;
    }

    /**
     * Two-dimensional counterpart of {@link #nonZero(int[])}. Null rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     */
    public static boolean[][] nonZero(final int[][] source) {
        Objects.requireNonNull(source, "source");

        final boolean[][] result = new boolean[source.length][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : nonZero(source[i]);
        }

        return result;
    }

    /**
     * Three-dimensional counterpart of {@link #nonZero(int[])}. Null slices and rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     */
    public static boolean[][][] nonZero(final int[][][] source) {
        Objects.requireNonNull(source, "source");

        final boolean[][][] result = new boolean[source.length][][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : nonZero(source[i]);
        }

        return result;
    }

    /**
     * Converts integers to UTF-16 code units ({@code char}) without the wraparound performed by a
     * Java narrowing cast. Every value in {@code 0..65535} is accepted, including isolated surrogate
     * code units; this method does not interpret or validate Unicode code points.
     *
     * @param source the required source array
     * @return a new array containing the exact UTF-16 code-unit values
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is outside the unsigned {@code char} range
     */
    public static char[] toCharExact(final int[] source) {
        Objects.requireNonNull(source, "source");

        final char[] result = new char[source.length];

        for (int i = 0; i < source.length; i++) {
            final int value = source[i];

            if (value < Character.MIN_VALUE || value > Character.MAX_VALUE) {
                throw inexact(value, "char");
            }

            result[i] = (char) value;
        }

        return result;
    }

    /**
     * Two-dimensional counterpart of {@link #toCharExact(int[])}; null rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is outside the unsigned {@code char} range
     */
    public static char[][] toCharExact(final int[][] source) {
        Objects.requireNonNull(source, "source");

        final char[][] result = new char[source.length][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toCharExact(source[i]);
        }

        return result;
    }

    /**
     * Three-dimensional counterpart of {@link #toCharExact(int[])}; null slices and rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is outside the unsigned {@code char} range
     */
    public static char[][][] toCharExact(final int[][][] source) {
        Objects.requireNonNull(source, "source");

        final char[][][] result = new char[source.length][][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toCharExact(source[i]);
        }

        return result;
    }

    /**
     * Converts finite, integral float values to {@code int} exactly.
     *
     * @param source the required source array
     * @return a new array containing the exact integer values
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code int} range
     */
    public static int[] toIntExact(final float[] source) {
        Objects.requireNonNull(source, "source");

        final int[] result = new int[source.length];

        for (int i = 0; i < source.length; i++) {
            result[i] = floatToIntExact(source[i]);
        }

        return result;
    }

    /**
     * Two-dimensional counterpart of {@link #toIntExact(float[])}; null rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code int} range
     */
    public static int[][] toIntExact(final float[][] source) {
        Objects.requireNonNull(source, "source");

        final int[][] result = new int[source.length][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toIntExact(source[i]);
        }

        return result;
    }

    /**
     * Three-dimensional counterpart of {@link #toIntExact(float[])}; null slices and rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code int} range
     */
    public static int[][][] toIntExact(final float[][][] source) {
        Objects.requireNonNull(source, "source");

        final int[][][] result = new int[source.length][][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toIntExact(source[i]);
        }

        return result;
    }

    /**
     * Converts finite, integral double values to {@code int} exactly.
     *
     * @param source the required source array
     * @return a new array containing the exact integer values
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code int} range
     */
    public static int[] toIntExact(final double[] source) {
        Objects.requireNonNull(source, "source");

        final int[] result = new int[source.length];

        for (int i = 0; i < source.length; i++) {
            result[i] = doubleToIntExact(source[i]);
        }

        return result;
    }

    /**
     * Two-dimensional counterpart of {@link #toIntExact(double[])}; null rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code int} range
     */
    public static int[][] toIntExact(final double[][] source) {
        Objects.requireNonNull(source, "source");

        final int[][] result = new int[source.length][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toIntExact(source[i]);
        }

        return result;
    }

    /**
     * Three-dimensional counterpart of {@link #toIntExact(double[])}; null slices and rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code int} range
     */
    public static int[][][] toIntExact(final double[][][] source) {
        Objects.requireNonNull(source, "source");

        final int[][][] result = new int[source.length][][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toIntExact(source[i]);
        }

        return result;
    }

    /**
     * Converts finite, integral float values to {@code long} exactly.
     *
     * @param source the required source array
     * @return a new array containing the exact long values
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code long} range
     */
    public static long[] toLongExact(final float[] source) {
        Objects.requireNonNull(source, "source");

        final long[] result = new long[source.length];

        for (int i = 0; i < source.length; i++) {
            result[i] = doubleToLongExact(source[i]);
        }

        return result;
    }

    /**
     * Two-dimensional counterpart of {@link #toLongExact(float[])}; null rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code long} range
     */
    public static long[][] toLongExact(final float[][] source) {
        Objects.requireNonNull(source, "source");

        final long[][] result = new long[source.length][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toLongExact(source[i]);
        }

        return result;
    }

    /**
     * Three-dimensional counterpart of {@link #toLongExact(float[])}; null slices and rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code long} range
     */
    public static long[][][] toLongExact(final float[][][] source) {
        Objects.requireNonNull(source, "source");

        final long[][][] result = new long[source.length][][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toLongExact(source[i]);
        }

        return result;
    }

    /**
     * Converts finite, integral double values to {@code long} exactly.
     *
     * @param source the required source array
     * @return a new array containing the exact long values
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code long} range
     */
    public static long[] toLongExact(final double[] source) {
        Objects.requireNonNull(source, "source");

        final long[] result = new long[source.length];

        for (int i = 0; i < source.length; i++) {
            result[i] = doubleToLongExact(source[i]);
        }

        return result;
    }

    /**
     * Two-dimensional counterpart of {@link #toLongExact(double[])}; null rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code long} range
     */
    public static long[][] toLongExact(final double[][] source) {
        Objects.requireNonNull(source, "source");

        final long[][] result = new long[source.length][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toLongExact(source[i]);
        }

        return result;
    }

    /**
     * Three-dimensional counterpart of {@link #toLongExact(double[])}; null slices and rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element is non-finite, fractional, or outside the {@code long} range
     */
    public static long[][][] toLongExact(final double[][][] source) {
        Objects.requireNonNull(source, "source");

        final long[][][] result = new long[source.length][][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toLongExact(source[i]);
        }

        return result;
    }

    /**
     * Converts integers to {@code float}, rejecting an element whose exact integer value cannot be represented.
     *
     * @param source the required source array
     * @return a new array containing exactly representable float values
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element cannot be represented exactly as {@code float}
     */
    public static float[] toFloatExact(final int[] source) {
        Objects.requireNonNull(source, "source");

        final float[] result = new float[source.length];

        for (int i = 0; i < source.length; i++) {
            result[i] = integralToFloatExact(source[i]);
        }

        return result;
    }

    /**
     * Two-dimensional counterpart of {@link #toFloatExact(int[])}; null rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element cannot be represented exactly as {@code float}
     */
    public static float[][] toFloatExact(final int[][] source) {
        Objects.requireNonNull(source, "source");

        final float[][] result = new float[source.length][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toFloatExact(source[i]);
        }

        return result;
    }

    /**
     * Three-dimensional counterpart of {@link #toFloatExact(int[])}; null slices and rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element cannot be represented exactly as {@code float}
     */
    public static float[][][] toFloatExact(final int[][][] source) {
        Objects.requireNonNull(source, "source");

        final float[][][] result = new float[source.length][][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toFloatExact(source[i]);
        }

        return result;
    }

    /**
     * Converts longs to {@code float}, rejecting an element whose exact integer value cannot be represented.
     *
     * @param source the required source array
     * @return a new array containing exactly representable float values
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element cannot be represented exactly as {@code float}
     */
    public static float[] toFloatExact(final long[] source) {
        Objects.requireNonNull(source, "source");

        final float[] result = new float[source.length];

        for (int i = 0; i < source.length; i++) {
            result[i] = integralToFloatExact(source[i]);
        }

        return result;
    }

    /**
     * Two-dimensional counterpart of {@link #toFloatExact(long[])}; null rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element cannot be represented exactly as {@code float}
     */
    public static float[][] toFloatExact(final long[][] source) {
        Objects.requireNonNull(source, "source");

        final float[][] result = new float[source.length][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toFloatExact(source[i]);
        }

        return result;
    }

    /**
     * Three-dimensional counterpart of {@link #toFloatExact(long[])}; null slices and rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element cannot be represented exactly as {@code float}
     */
    public static float[][][] toFloatExact(final long[][][] source) {
        Objects.requireNonNull(source, "source");

        final float[][][] result = new float[source.length][][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toFloatExact(source[i]);
        }

        return result;
    }

    /**
     * Converts longs to {@code double}, rejecting an element whose exact integer value cannot be represented.
     *
     * @param source the required source array
     * @return a new array containing exactly representable double values
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element cannot be represented exactly as {@code double}
     */
    public static double[] toDoubleExact(final long[] source) {
        Objects.requireNonNull(source, "source");

        final double[] result = new double[source.length];

        for (int i = 0; i < source.length; i++) {
            result[i] = integralToDoubleExact(source[i]);
        }

        return result;
    }

    /**
     * Two-dimensional counterpart of {@link #toDoubleExact(long[])}; null rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element cannot be represented exactly as {@code double}
     */
    public static double[][] toDoubleExact(final long[][] source) {
        Objects.requireNonNull(source, "source");

        final double[][] result = new double[source.length][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toDoubleExact(source[i]);
        }

        return result;
    }

    /**
     * Three-dimensional counterpart of {@link #toDoubleExact(long[])}; null slices and rows are preserved.
     *
     * @param source the required source array
     * @return a new array with the same dimensions, lengths, and null placements as {@code source}
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws ArithmeticException if an element cannot be represented exactly as {@code double}
     */
    public static double[][][] toDoubleExact(final long[][][] source) {
        Objects.requireNonNull(source, "source");

        final double[][][] result = new double[source.length][][];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] == null ? null : toDoubleExact(source[i]);
        }

        return result;
    }

    private static int floatToIntExact(final float value) {
        return doubleToIntExact(value);
    }

    private static int doubleToIntExact(final double value) {
        if (!Double.isFinite(value) || value != Math.rint(value) || value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw inexact(value, "int");
        }

        return (int) value;
    }

    private static long doubleToLongExact(final double value) {
        // Long.MAX_VALUE rounds to 2^63 as a double, so an ordinary comparison with Long.MAX_VALUE
        // would accept the first out-of-range value. The power-of-two boundary is exact.
        if (!Double.isFinite(value) || value != Math.rint(value) || value < -0x1p63 || value >= 0x1p63) {
            throw inexact(value, "long");
        }

        return (long) value;
    }

    private static float integralToFloatExact(final long value) {
        if (!isExactlyRepresentable(value, FLOAT_SIGNIFICAND_BITS)) {
            throw inexact(value, "float");
        }

        return value;
    }

    private static double integralToDoubleExact(final long value) {
        if (!isExactlyRepresentable(value, DOUBLE_SIGNIFICAND_BITS)) {
            throw inexact(value, "double");
        }

        return value;
    }

    private static boolean isExactlyRepresentable(final long value, final int significandBits) {
        if (value == 0) {
            return true;
        }

        // Binary floating-point represents an integer exactly when every bit discarded beyond the
        // significand is zero. Handle Long.MIN_VALUE separately because abs(MIN_VALUE) overflows.
        final int bitLength;
        final int trailingZeroBits;

        if (value == Long.MIN_VALUE) {
            bitLength = Long.SIZE;
            trailingZeroBits = Long.SIZE - 1;
        } else {
            final long magnitude = Math.abs(value);
            bitLength = Long.SIZE - Long.numberOfLeadingZeros(magnitude);
            trailingZeroBits = Long.numberOfTrailingZeros(magnitude);
        }

        return trailingZeroBits >= Math.max(0, bitLength - significandBits);
    }

    private static ArithmeticException inexact(final double value, final String targetType) {
        return new ArithmeticException("Cannot convert " + value + " exactly to " + targetType);
    }

    private static ArithmeticException inexact(final long value, final String targetType) {
        return new ArithmeticException("Cannot convert " + value + " exactly to " + targetType);
    }
}
