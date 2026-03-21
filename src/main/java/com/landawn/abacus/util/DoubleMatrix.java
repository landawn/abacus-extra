/*
 * Copyright (C) 2016 HaiYang Li
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

import java.security.SecureRandom;
import java.util.NoSuchElementException;
import java.util.Random;

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.annotation.SuppressFBWarnings;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalDouble;
import com.landawn.abacus.util.stream.DoubleIteratorEx;
import com.landawn.abacus.util.stream.DoubleStream;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

/**
 * Matrix implementation backed by a {@code double[][]}.
 *
 * <p>It provides double-specific accessors and transformations while sharing the common matrix operations
 * defined by {@link AbstractMatrix}. Construction and factory methods typically wrap the supplied array
 * directly unless their documentation states otherwise.</p>
 */
public final class DoubleMatrix extends AbstractMatrix<double[], DoubleList, DoubleStream, Stream<DoubleStream>, DoubleMatrix> {

    static final Random RAND = new SecureRandom();
    static final DoubleMatrix EMPTY_DOUBLE_MATRIX = new DoubleMatrix(new double[0][0]);

    /**
     * Constructs a DoubleMatrix from a two-dimensional double array.
     *
     * <p>The provided array is used directly as the internal storage without copying.
     * If the input array is null, an empty matrix (0x0) is created instead.
     *
     * <p><b>Important:</b> Since the array is not copied, any external modifications
     * to the array will affect this matrix. To avoid this issue, use {@link #copy()} method
     * on the created matrix to obtain a copy, or manually copy the array before passing it.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
     * DoubleMatrix matrix = new DoubleMatrix(data);
     * // matrix.rowCount() returns 2, matrix.columnCount() returns 2
     * // Modifications to data will affect matrix
     *
     * // For a safe independent copy:
     * double[][] safeCopy = new double[data.length][];
     * for (int i = 0; i < data.length; i++) {
     *     safeCopy[i] = data[i].clone();
     * }
     * DoubleMatrix safeMat = new DoubleMatrix(safeCopy);
     *
     * DoubleMatrix empty = new DoubleMatrix(null);
     * // empty.rowCount() returns 0, empty.columnCount() returns 0
     * }</pre>
     *
     * @param a the two-dimensional double array to wrap, or null for an empty matrix
     */
    public DoubleMatrix(final double[][] a) {
        super(a == null ? new double[0][0] : a);
    }

    /**
     * Creates an empty matrix with zero rows and zero columns.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.empty();
     * // matrix.rowCount() returns 0
     * // matrix.columnCount() returns 0
     * }</pre>
     *
     * @return an empty double matrix
     */
    public static DoubleMatrix empty() {
        return EMPTY_DOUBLE_MATRIX;
    }

    /**
     * Creates a DoubleMatrix from a two-dimensional double array.
     *
     * <p><b>Important:</b> The provided array is used directly without defensive copying.
     * Changes to the input array are reflected in the returned matrix, and vice versa.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * // matrix.get(1, 1) returns 4.0
     * }</pre>
     *
     * @param a the two-dimensional double array to create the matrix from, or null/empty for an empty matrix
     * @return a new DoubleMatrix containing the provided data, or an empty DoubleMatrix if input is null or empty
     */
    public static DoubleMatrix of(final double[]... a) {
        return N.isEmpty(a) ? EMPTY_DOUBLE_MATRIX : new DoubleMatrix(a);
    }

    /**
     * Creates a DoubleMatrix from a two-dimensional int array by converting int values to double.
     *
     * <p>All rows must have the same length as the first row (rectangular array required).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.from(new int[][] {{1, 2}, {3, 4}});
     * // Creates a matrix with values {{1.0, 2.0}, {3.0, 4.0}}
     * // matrix.get(0, 1) returns 2.0
     * }</pre>
     *
     * @param a the two-dimensional int array to convert to a double matrix, or null/empty for an empty matrix
     * @return a new DoubleMatrix with converted values, or an empty DoubleMatrix if input is null or empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths (non-rectangular array)
     */
    public static DoubleMatrix from(final int[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_DOUBLE_MATRIX;
        }

        N.checkArgument(a[0] != null, "First row cannot be null");

        final int columnCount = a[0].length;

        // Validate all rows have the same length
        for (int i = 1; i < a.length; i++) {
            N.checkArgument(a[i] != null && a[i].length == columnCount, "All rows must have the same length. Row 0 has length {} but row {} has length {}",
                    columnCount, i, a[i] == null ? 0 : a[i].length);
        }

        final double[][] c = new double[a.length][columnCount];

        for (int i = 0, len = a.length; i < len; i++) {
            final int[] aa = a[i];
            final double[] cc = c[i];

            for (int j = 0; j < columnCount; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Creates a DoubleMatrix from a two-dimensional long array by converting long values to double.
     *
     * <p>All rows must have the same length as the first row (rectangular array required).</p>
     *
     * <p><b>Note:</b> Long values with more than 53 significant bits may lose precision when
     * converted to double, since double has a 52-bit mantissa. For example,
     * {@code Long.MAX_VALUE} (9223372036854775807) cannot be represented exactly as a double.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.from(new long[][] {{1L, 2L}, {3L, 4L}});
     * // Creates a matrix with values {{1.0, 2.0}, {3.0, 4.0}}
     * // matrix.get(1, 0) returns 3.0
     * }</pre>
     *
     * @param a the two-dimensional long array to convert to a double matrix, or null/empty for an empty matrix
     * @return a new DoubleMatrix with converted values, or an empty DoubleMatrix if input is null or empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths (non-rectangular array)
     */
    public static DoubleMatrix from(final long[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_DOUBLE_MATRIX;
        }

        N.checkArgument(a[0] != null, "First row cannot be null");

        final int columnCount = a[0].length;

        // Validate all rows have the same length
        for (int i = 1; i < a.length; i++) {
            N.checkArgument(a[i] != null && a[i].length == columnCount, "All rows must have the same length. Row 0 has length {} but row {} has length {}",
                    columnCount, i, a[i] == null ? 0 : a[i].length);
        }

        final double[][] c = new double[a.length][columnCount];

        for (int i = 0, len = a.length; i < len; i++) {
            final long[] aa = a[i];
            final double[] cc = c[i];

            for (int j = 0; j < columnCount; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Creates a DoubleMatrix from a two-dimensional float array by converting float values to double.
     *
     * <p>All rows must have the same length as the first row (rectangular array required).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.from(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * // Creates a matrix with values {{1.0, 2.0}, {3.0, 4.0}}
     * // matrix.get(1, 1) returns 4.0
     * }</pre>
     *
     * @param a the two-dimensional float array to convert to a double matrix, or null/empty for an empty matrix
     * @return a new DoubleMatrix with converted values, or an empty DoubleMatrix if input is null or empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths (non-rectangular array)
     */
    public static DoubleMatrix from(final float[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_DOUBLE_MATRIX;
        }

        N.checkArgument(a[0] != null, "First row cannot be null");

        final int columnCount = a[0].length;

        // Validate all rows have the same length
        for (int i = 1; i < a.length; i++) {
            N.checkArgument(a[i] != null && a[i].length == columnCount, "All rows must have the same length. Row 0 has length {} but row {} has length {}",
                    columnCount, i, a[i] == null ? 0 : a[i].length);
        }

        final double[][] c = new double[a.length][columnCount];

        for (int i = 0, len = a.length; i < len; i++) {
            final float[] aa = a[i];
            final double[] cc = c[i];

            for (int j = 0; j < columnCount; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Creates a new {@code 1 x size} matrix filled with random double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.random(5);
     * // Result: a 1x5 matrix with random double values
     * }</pre>
     *
     * @param size the number of columns in the new matrix
     * @return a new DoubleMatrix of dimensions 1 x size filled with random values
     * @throws IllegalArgumentException if {@code size} is negative
     */
    public static DoubleMatrix random(final int size) {
        return random(1, size);
    }

    /**
     * Creates a new matrix of the specified dimensions filled with random double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.random(2, 3);
     * // Result: a 2x3 matrix with random double values
     * }</pre>
     *
     * @param rowCount the number of rows in the new matrix
     * @param columnCount the number of columns in the new matrix
     * @return a new DoubleMatrix of dimensions rowCount x columnCount filled with random values
     * @throws IllegalArgumentException if {@code rowCount} or {@code columnCount} is negative
     */
    public static DoubleMatrix random(final int rowCount, final int columnCount) {
        N.checkArgument(rowCount >= 0, MSG_NEGATIVE_DIMENSION, "rowCount", rowCount);
        N.checkArgument(columnCount >= 0, MSG_NEGATIVE_DIMENSION, "columnCount", columnCount);
        checkRepresentableShape(rowCount, columnCount);

        final double[][] a = new double[rowCount][columnCount];

        for (double[] ea : a) {
            for (int i = 0; i < columnCount; i++) {
                ea[i] = RAND.nextDouble();
            }
        }

        return new DoubleMatrix(a);
    }

    /**
     * Creates a new matrix of the specified dimensions where every element is the provided {@code element}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.repeat(2, 3, 1.0);
     * // Result: [[1.0, 1.0, 1.0], [1.0, 1.0, 1.0]]
     * }</pre>
     *
     * @param rowCount the number of rows in the new matrix
     * @param columnCount the number of columns in the new matrix
     * @param element the double value to fill the matrix with
     * @return a new DoubleMatrix of dimensions rowCount x columnCount filled with the specified element
     * @throws IllegalArgumentException if {@code rowCount} or {@code columnCount} is negative
     */
    public static DoubleMatrix repeat(final int rowCount, final int columnCount, final double element) {
        N.checkArgument(rowCount >= 0, MSG_NEGATIVE_DIMENSION, "rowCount", rowCount);
        N.checkArgument(columnCount >= 0, MSG_NEGATIVE_DIMENSION, "columnCount", columnCount);
        checkRepresentableShape(rowCount, columnCount);

        final double[][] a = new double[rowCount][columnCount];

        for (double[] ea : a) {
            N.fill(ea, element);
        }

        return new DoubleMatrix(a);
    }

    /**
     * Creates a square matrix from the specified main diagonal elements (upper-left to lower-right).
     * All other elements are set to zero. The resulting matrix has dimensions n×n where n is the length
     * of the diagonal array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.mainDiagonal(new double[] {1.0, 2.0, 3.0});
     * // Creates 3x3 matrix:
     * // [[1.0, 0.0, 0.0],
     * //  [0.0, 2.0, 0.0],
     * //  [0.0, 0.0, 3.0]]
     * }</pre>
     *
     * @param mainDiagonal the array of main diagonal elements, or null/empty for an empty matrix
     * @return a square matrix with the specified main diagonal, or an empty matrix if input is null or empty
     */
    public static DoubleMatrix mainDiagonal(final double[] mainDiagonal) {
        return diagonals(mainDiagonal, null);
    }

    /**
     * Creates a square matrix from the specified anti-diagonal elements (upper-right to lower-left).
     * All other elements are set to zero. The resulting matrix has dimensions n×n where n is the length
     * of the diagonal array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.antiDiagonal(new double[] {1.0, 2.0, 3.0});
     * // Creates 3x3 matrix:
     * // [[0.0, 0.0, 1.0],
     * //  [0.0, 2.0, 0.0],
     * //  [3.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param antiDiagonal the array of anti-diagonal elements, or null/empty for an empty matrix
     * @return a square matrix with the specified anti-diagonal, or an empty matrix if input is null or empty
     */
    public static DoubleMatrix antiDiagonal(final double[] antiDiagonal) {
        return diagonals(null, antiDiagonal);
    }

    /**
     * Creates a square matrix from the specified main diagonal and anti-diagonal elements.
     * All other elements are set to zero. If both arrays are provided, they must have the same length.
     * The resulting matrix has dimensions n×n where n is the length of the non-empty diagonal array.
     *
     * <p><b>Note:</b> The anti-diagonal is written first, then the main diagonal. If both diagonals
     * share a position (which happens for odd-sized matrices at the center element), the main diagonal
     * value takes precedence.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.diagonals(new double[] { 1.0, 2.0, 3.0 }, new double[] { 4.0, 5.0, 6.0 });
     * // Creates 3x3 matrix with both diagonals set
     * // Resulting matrix:
     * //   {1.0, 0.0, 4.0},
     * //   {0.0, 2.0, 0.0},
     * //   {6.0, 0.0, 3.0}
     * }</pre>
     *
     * @param mainDiagonal the array of main diagonal elements (can be null or empty)
     * @param antiDiagonal the array of anti-diagonal elements (can be null or empty)
     * @return a square matrix with the specified diagonals, or an empty matrix if both inputs are null or empty
     * @throws IllegalArgumentException if both arrays are non-empty and have different lengths
     */
    public static DoubleMatrix diagonals(final double[] mainDiagonal, final double[] antiDiagonal) throws IllegalArgumentException {
        N.checkArgument(N.isEmpty(mainDiagonal) || N.isEmpty(antiDiagonal) || mainDiagonal.length == antiDiagonal.length,
                "The length of 'mainDiagonal' and 'antiDiagonal' must be same");

        if (N.isEmpty(mainDiagonal) && N.isEmpty(antiDiagonal)) {
            return EMPTY_DOUBLE_MATRIX;
        }

        final int len = N.max(N.len(mainDiagonal), N.len(antiDiagonal));
        final double[][] c = new double[len][len];

        if (N.notEmpty(antiDiagonal)) {
            for (int i = 0, j = len - 1; i < len; i++, j--) {
                c[i][j] = antiDiagonal[i];
            }
        }

        if (N.notEmpty(mainDiagonal)) {
            for (int i = 0; i < len; i++) {
                c[i][i] = mainDiagonal[i]; // NOSONAR
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Converts a boxed Double matrix to a primitive DoubleMatrix.
     * Null values in the input matrix are converted to 0.0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<Double> boxed = Matrix.of(new Double[][] {{1.0, 2.0}, {3.0, null}});
     * DoubleMatrix primitive = DoubleMatrix.unbox(boxed);
     * // primitive is [[1.0, 2.0], [3.0, 0.0]]
     * }</pre>
     *
     * @param x the boxed Double matrix to convert
     * @return a new DoubleMatrix with unboxed values (nulls become 0.0)
     * @see #boxed()
     */
    public static DoubleMatrix unbox(final Matrix<Double> x) {
        return DoubleMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of the matrix elements, which is always {@code double.class}.
     * This method is useful for reflection-based code that needs to determine the element type.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * Class<?> componentType = matrix.componentType();
     * // componentType is double.class
     * assert componentType == double.class;
     * }</pre>
     *
     * @return {@code double.class}
     */
    @Override
    public Class<?> componentType() {
        return double.class;
    }

    /**
     * Returns the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double value = matrix.get(0, 1);   // Returns 2.0
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return the element at position (rowIndex, columnIndex)
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public double get(final int rowIndex, final int columnIndex) {
        return a[rowIndex][columnIndex];
    }

    /**
     * Returns the element at the specified point.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * Sheet.Point point = Sheet.Point.of(0, 1);
     * double value = matrix.get(point);   // Returns 2.0
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @return the double element at the specified point
     * @throws IllegalArgumentException if {@code point} is {@code null}
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #get(int, int)
     */
    public double get(final Point point) {
        N.checkArgNotNull(point, "point");

        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.set(0, 1, 9.0);   // Sets element at row 0, column 1 to 9.0
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public void set(final int rowIndex, final int columnIndex, final double val) {
        a[rowIndex][columnIndex] = val;
    }

    /**
     * Sets the element at the specified point to the given value.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * Sheet.Point point = Sheet.Point.of(0, 1);
     * matrix.set(point, 9.0);
     * assert matrix.get(point) == 9.0;
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @param val the new double value to set at the specified point
     * @throws IllegalArgumentException if {@code point} is {@code null}
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #set(int, int, double)
     */
    public void set(final Point point, final double val) {
        N.checkArgNotNull(point, "point");

        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element directly above the specified position, if it exists.
     * This method provides safe access to the element directly above the given position
     * without throwing an exception when at the top edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * u.OptionalDouble value = matrix.above(1, 0);   // Returns u.OptionalDouble.of(1.0)
     * u.OptionalDouble empty = matrix.above(0, 0);   // Returns u.OptionalDouble.empty() - no row above
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return an u.OptionalDouble containing the element at position (rowIndex - 1, columnIndex), or empty if rowIndex == 0
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalDouble above(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        return rowIndex == 0 ? OptionalDouble.empty() : OptionalDouble.of(a[rowIndex - 1][columnIndex]);
    }

    /**
     * Returns the element directly below the specified position, if it exists.
     * This method provides safe access to the element directly below the given position
     * without throwing an exception when at the bottom edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * u.OptionalDouble value = matrix.below(0, 0);   // Returns u.OptionalDouble.of(3.0)
     * u.OptionalDouble empty = matrix.below(1, 0);   // Returns u.OptionalDouble.empty() - no row below
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return an u.OptionalDouble containing the element at position (rowIndex + 1, columnIndex), or empty if rowIndex == rowCount - 1
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalDouble below(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        return rowIndex == rowCount - 1 ? OptionalDouble.empty() : OptionalDouble.of(a[rowIndex + 1][columnIndex]);
    }

    /**
     * Returns the element directly to the left of the specified position, if it exists.
     * This method provides safe access to the element directly to the left of the given position
     * without throwing an exception when at the leftmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * u.OptionalDouble value = matrix.left(0, 1);   // Returns u.OptionalDouble.of(1.0)
     * u.OptionalDouble empty = matrix.left(0, 0);   // Returns u.OptionalDouble.empty() - no column to the left
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return an u.OptionalDouble containing the element at position (rowIndex, columnIndex - 1), or empty if columnIndex == 0
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalDouble left(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        return columnIndex == 0 ? OptionalDouble.empty() : OptionalDouble.of(a[rowIndex][columnIndex - 1]);
    }

    /**
     * Returns the element directly to the right of the specified position, if it exists.
     * This method provides safe access to the element directly to the right of the given position
     * without throwing an exception when at the rightmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * u.OptionalDouble value = matrix.right(0, 0);   // Returns u.OptionalDouble.of(2.0)
     * u.OptionalDouble empty = matrix.right(0, 1);   // Returns u.OptionalDouble.empty() - no column to the right
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return an u.OptionalDouble containing the element at position (rowIndex, columnIndex + 1), or empty if columnIndex == columnCount - 1
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalDouble right(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        return columnIndex == columnCount - 1 ? OptionalDouble.empty() : OptionalDouble.of(a[rowIndex][columnIndex + 1]);
    }

    /**
     * Returns the specified row as a double array.
     *
     * <p><b>Note:</b> This method returns a reference to the internal array, not a copy.
     * Modifications to the returned array will affect the matrix. If you need an independent
     * copy, use {@code Arrays.copyOf(matrix.rowView(i), matrix.columnCount())}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * double[] firstRow = matrix.rowView(0);   // Returns [1.0, 2.0, 3.0]
     *
     * // Direct modification affects the matrix
     * firstRow[0] = 99.0;  // matrix now has 99.0 at position (0,0)
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return the specified row array (direct reference to internal storage)
     * @throws IllegalArgumentException if rowIndex &lt; 0 or rowIndex &gt;= rowCount
     */
    @Override
    public double[] rowView(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rowCount, MSG_ROW_INDEX_OUT_OF_BOUNDS, rowIndex, rowCount);

        return a[rowIndex];
    }

    /**
     * Returns a defensive copy of the specified row.
     * Changes to the returned array do not affect this matrix.
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return a new double array containing the values from the specified row
     * @throws IllegalArgumentException if rowIndex &lt; 0 or rowIndex &gt;= rowCount
     */
    @Override
    public double[] rowCopy(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rowCount, MSG_ROW_INDEX_OUT_OF_BOUNDS, rowIndex, rowCount);

        return N.copyOf(a[rowIndex], columnCount);
    }

    /**
     * Returns a copy of the specified column as a new double array.
     *
     * <p>Unlike {@link #rowView(int)}, this method always returns a new array copy since
     * columns are not stored contiguously in memory. Modifications to the returned array
     * will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * double[] firstColumn = matrix.columnCopy(0);   // Returns [1.0, 4.0]
     *
     * // Modification does NOT affect the matrix (it's a copy)
     * firstColumn[0] = 99.0;  // matrix still has 1.0 at position (0,0)
     * }</pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing the values from the specified column
     * @throws IllegalArgumentException if columnIndex &lt; 0 or columnIndex &gt;= columnCount
     */
    @Override
    public double[] columnCopy(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < columnCount, MSG_COLUMN_INDEX_OUT_OF_BOUNDS, columnIndex, columnCount);

        final double[] c = new double[rowCount];

        for (int i = 0; i < rowCount; i++) {
            c[i] = a[i][columnIndex];
        }

        return c;
    }

    /**
     * Sets the values of the specified row by copying from the provided array.
     * All elements in the row are replaced with values from the provided array.
     *
     * <p>The values from the source array are copied into the matrix row.
     * The source array must have exactly the same length as the number of columns in the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * matrix.setRow(0, new double[] {7.0, 8.0, 9.0});   // First row is now [7.0, 8.0, 9.0]
     * }</pre>
     *
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to copy into the row; must have length equal to the number of columns
     * @throws NullPointerException if {@code row} is {@code null}
     * @throws IllegalArgumentException if rowIndex is out of bounds or row length does not match column count
     */
    public void setRow(final int rowIndex, final double[] row) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rowCount, MSG_ROW_INDEX_OUT_OF_BOUNDS, rowIndex, rowCount);
        N.checkArgument(row.length == columnCount, MSG_ROW_LENGTH_MISMATCH, columnCount, row.length);

        N.copy(row, 0, a[rowIndex], 0, columnCount);
    }

    /**
     * Sets the values of the specified column by copying from the provided array.
     * All elements in the column are replaced with values from the provided array.
     *
     * <p>The values from the source array are copied into the matrix column.
     * The source array must have exactly the same length as the number of rows in the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * matrix.setColumn(0, new double[] {7.0, 8.0});   // First column is now [7.0, 8.0]
     * }</pre>
     *
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to copy into the column; must have length equal to the number of rows
     * @throws NullPointerException if {@code column} is {@code null}
     * @throws IllegalArgumentException if columnIndex is out of bounds or column length does not match row count
     * @throws ArrayIndexOutOfBoundsException if the underlying wrapped array has been externally modified into a non-rectangular shape
     */
    public void setColumn(final int columnIndex, final double[] column) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < columnCount, MSG_COLUMN_INDEX_OUT_OF_BOUNDS, columnIndex, columnCount);
        N.checkArgument(column.length == rowCount, MSG_COLUMN_LENGTH_MISMATCH, rowCount, column.length);

        for (int i = 0; i < rowCount; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all elements in the specified row by applying the given operator to each element.
     * The matrix is modified in-place. Each element in the row is transformed by the operator
     * and replaced with the result.
     *
     * <p>The operator is applied to each element in the specified row sequentially
     * from left to right (column 0 to column columnCount-1).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * matrix.updateRow(0, x -> x * 2);   // Doubles all values in the first row
     * // matrix is now [[2.0, 4.0, 6.0], [4.0, 5.0, 6.0]]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param rowIndex the index of the row to update (0-based)
     * @param operator the operator to apply to each element in the row; receives the current
     *             element value and returns the new value
     * @throws ArrayIndexOutOfBoundsException if rowIndex is out of bounds
     * @throws IllegalArgumentException if operator is null
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.DoubleUnaryOperator<E> operator) throws E {
        if (rowIndex < 0 || rowIndex >= rowCount) {
            throw new ArrayIndexOutOfBoundsException(formatMsg(MSG_ROW_INDEX_OUT_OF_BOUNDS, rowIndex, rowCount));
        }

        N.checkArgNotNull(operator, "operator");

        for (int i = 0; i < columnCount; i++) {
            a[rowIndex][i] = operator.applyAsDouble(a[rowIndex][i]);
        }
    }

    /**
     * Updates all elements in the specified column in-place by applying the given operator to each element.
     * This modifies the matrix directly.
     *
     * <p>The operator is applied to each element in the specified column sequentially
     * from top to bottom (row 0 to row rowCount-1).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
     * matrix.updateColumn(0, x -> x + 10.0);   // Adds 10 to all values in the first column
     * // matrix is now [[11.0, 2.0], [13.0, 4.0], [15.0, 6.0]]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param columnIndex the index of the column to update (0-based)
     * @param operator the operator to apply to each element in the column; receives the current
     *             element value and returns the new value
     * @throws ArrayIndexOutOfBoundsException if columnIndex is out of bounds
     * @throws IllegalArgumentException if operator is null
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.DoubleUnaryOperator<E> operator) throws E {
        if (columnIndex < 0 || columnIndex >= columnCount) {
            throw new ArrayIndexOutOfBoundsException(formatMsg(MSG_COLUMN_INDEX_OUT_OF_BOUNDS, columnIndex, columnCount));
        }

        N.checkArgNotNull(operator, "operator");

        for (int i = 0; i < rowCount; i++) {
            a[i][columnIndex] = operator.applyAsDouble(a[i][columnIndex]);
        }
    }

    /**
     * Returns a copy of the main diagonal elements (upper-left to lower-right).
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>This method extracts the main diagonal elements at positions (0,0), (1,1), (2,2), etc.
     * The returned array is a copy; modifications to it will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
     * double[] diagonal = matrix.getMainDiagonal();   // Returns [1.0, 5.0, 9.0]
     * }</pre>
     *
     * @return a new double array containing a copy of the main diagonal elements
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public double[] getMainDiagonal() throws IllegalStateException {
        checkIfRowAndColumnSizeAreSame();

        final double[] res = new double[rowCount];

        for (int i = 0; i < rowCount; i++) {
            res[i] = a[i][i]; // NOSONAR
        }

        return res;
    }

    /**
     * Sets the elements on the main diagonal (upper-left to lower-right).
     * The matrix must be square (rowCount == columnCount), and the diagonal array must have
     * exactly as many elements as the matrix has rows.
     *
     * <p>This method sets the main diagonal elements at positions (0,0), (1,1), (2,2), etc.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.setMainDiagonal(new double[] {9.0, 8.0});
     * // Diagonal is now [9.0, 8.0]
     * }</pre>
     *
     * @param mainDiagonal the new values for the main diagonal; must have length equal to rowCount
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     * @throws IllegalArgumentException if mainDiagonal array length does not equal rowCount
     */
    public void setMainDiagonal(final double[] mainDiagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(N.len(mainDiagonal) == rowCount, MSG_DIAGONAL_LENGTH_MISMATCH, rowCount, N.len(mainDiagonal));

        for (int i = 0; i < rowCount; i++) {
            a[i][i] = mainDiagonal[i];
        }
    }

    /**
     * Updates the values on the main diagonal (upper-left to lower-right) by applying the specified operator.
     * The matrix must be square.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.updateMainDiagonal(x -> x * x);   // Squares all diagonal values
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param operator the operator to apply to each diagonal element; receives the current
     *             element value and returns the new value
     * @throws IllegalStateException if the matrix is not square
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateMainDiagonal(final Throwables.DoubleUnaryOperator<E> operator) throws IllegalStateException, E {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgNotNull(operator, "operator");

        for (int i = 0; i < rowCount; i++) {
            a[i][i] = operator.applyAsDouble(a[i][i]);
        }
    }

    /**
     * Returns a copy of the anti-diagonal elements (upper-right to lower-left).
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>This method extracts the anti-diagonal (secondary diagonal) elements from
     * upper-right to lower-left, at positions (0,n-1), (1,n-2), (2,n-3), etc.
     * The returned array is a copy; modifications to it will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
     * double[] diagonal = matrix.getAntiDiagonal();   // Returns [3.0, 5.0, 7.0]
     * }</pre>
     *
     * @return a new double array containing a copy of the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public double[] getAntiDiagonal() throws IllegalStateException {
        checkIfRowAndColumnSizeAreSame();

        final double[] res = new double[rowCount];

        for (int i = 0; i < rowCount; i++) {
            res[i] = a[i][columnCount - i - 1];
        }

        return res;
    }

    /**
     * Sets the elements on the anti-diagonal (upper-right to lower-left).
     * The matrix must be square (rowCount == columnCount), and the diagonal array must have
     * exactly as many elements as the matrix has rows.
     *
     * <p>This method sets the anti-diagonal (secondary diagonal) elements from
     * top-right to bottom-left, at positions (0,n-1), (1,n-2), (2,n-3), etc.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.setAntiDiagonal(new double[] {9.0, 8.0});
     * // Anti-diagonal is now [9.0, 8.0]
     * }</pre>
     *
     * @param antiDiagonal the new values for the anti-diagonal; must have length equal to rowCount
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     * @throws IllegalArgumentException if antiDiagonal array length does not equal rowCount
     */
    public void setAntiDiagonal(final double[] antiDiagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(N.len(antiDiagonal) == rowCount, MSG_DIAGONAL_LENGTH_MISMATCH, rowCount, N.len(antiDiagonal));

        for (int i = 0; i < rowCount; i++) {
            a[i][columnCount - i - 1] = antiDiagonal[i];
        }
    }

    /**
     * Updates the values on the anti-diagonal (upper-right to lower-left) by applying the specified operator.
     * The matrix must be square.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.updateAntiDiagonal(x -> -x);   // Negates all anti-diagonal values
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param operator the operator to apply to each anti-diagonal element; receives the current
     *             element value and returns the new value
     * @throws IllegalStateException if the matrix is not square
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateAntiDiagonal(final Throwables.DoubleUnaryOperator<E> operator) throws IllegalStateException, E {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgNotNull(operator, "operator");

        for (int i = 0; i < rowCount; i++) {
            a[i][columnCount - i - 1] = operator.applyAsDouble(a[i][columnCount - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix in-place by applying the specified operator.
     * This modifies the matrix directly.
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.
     * Elements are processed in row-major order when executed sequentially.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.updateAll(x -> x * 2);   // Doubles all values in the matrix
     * // matrix is now [[2.0, 4.0], [6.0, 8.0]]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param operator the operator to apply to each element; receives the current element value
     *             and returns the new value
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.DoubleUnaryOperator<E> operator) throws E {
        N.checkArgNotNull(operator, "operator");
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> a[i][j] = operator.applyAsDouble(a[i][j]);
        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));
    }

    /**
     * Updates all elements in the matrix in-place based on their position (row and column indices).
     * This modifies the matrix directly.
     *
     * <p>The operator receives the row and column indices for each element and returns the new value
     * for that position. This is useful for initializing matrices based on position patterns or
     * mathematical formulas. The operation may be performed in parallel for large matrices.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
     * matrix.updateAll((i, j) -> (double) (i + j));   // Sets each element to sum of its indices
     * // matrix is now [[0.0, 1.0, 2.0], [1.0, 2.0, 3.0]]
     *
     * matrix.updateAll((i, j) -> i * 10.0 + j);   // Position encoding
     * // matrix is now [[0.0, 1.0, 2.0], [10.0, 11.0, 12.0]]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param operator the operator that receives row index and column index (0-based) and returns
     *             the new value for that position
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Double, E> operator) throws E {
        N.checkArgNotNull(operator, "operator");
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> a[i][j] = operator.apply(i, j);
        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));
    }

    /**
     * Conditionally replaces elements in-place based on a predicate.
     * All elements that satisfy the predicate are replaced with the specified new value.
     * This modifies the matrix directly.
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{-1.0, 2.0, -3.0}, {4.0, -5.0, 6.0}});
     * matrix.replaceIf(x -> x < 0, 0.0);   // Replaces all negative values with 0
     * // matrix is now [[0.0, 2.0, 0.0], [4.0, 0.0, 6.0]]
     *
     * matrix.replaceIf(x -> x > 3.0, 99.0);   // Replaces all values greater than 3 with 99
     * // matrix is now [[0.0, 2.0, 0.0], [99.0, 0.0, 99.0]]
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition to test each element; elements for which this returns
     *                  {@code true} will be replaced
     * @param newValue the value to use for replacing matching elements
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.DoublePredicate<E> predicate, final double newValue) throws E {
        N.checkArgNotNull(predicate, "predicate");
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));
    }

    /**
     * Conditionally replaces elements in-place based on their position (row and column indices).
     * Elements at positions that satisfy the predicate are replaced with the specified new value.
     * This modifies the matrix directly.
     *
     * <p>This is useful for position-based replacements such as setting diagonals, borders,
     * or specific regions. The operation may be performed in parallel for large matrices.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
     * matrix.replaceIf((i, j) -> i == j, 0.0);   // Sets main diagonal elements to 0
     * // matrix is now [[0.0, 2.0, 3.0], [4.0, 0.0, 6.0], [7.0, 8.0, 0.0]]
     *
     * matrix.replaceIf((i, j) -> i == 0 || j == 0, -1.0);   // Sets first row and column to -1
     * // matrix is now [[-1.0, -1.0, -1.0], [-1.0, 0.0, 6.0], [-1.0, 8.0, 0.0]]
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition that tests row index and column index (0-based); elements
     *                  at positions for which this returns {@code true} will be replaced
     * @param newValue the value to use for replacing matching elements
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final double newValue) throws E {
        N.checkArgNotNull(predicate, "predicate");
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));
    }

    /**
     * Creates a new DoubleMatrix by applying a transformation function to each element.
     * The original matrix is not modified; a new matrix with transformed values is returned.
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.
     * This is the immutable counterpart to {@link #updateAll(Throwables.DoubleUnaryOperator)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix squared = matrix.map(x -> x * x);   // Creates new matrix with squared values
     * // squared is [[1.0, 4.0], [9.0, 16.0]], original matrix unchanged
     *
     * DoubleMatrix negated = matrix.map(x -> -x);   // Negate all values
     * // negated is [[-1.0, -2.0], [-3.0, -4.0]]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param mapper the function to apply to each element; receives the current element value
     *             and returns the transformed value
     * @return a new DoubleMatrix with transformed values
     * @throws E if the function throws an exception
     * @see #updateAll(Throwables.DoubleUnaryOperator)
     */
    public <E extends Exception> DoubleMatrix map(final Throwables.DoubleUnaryOperator<E> mapper) throws E {
        N.checkArgNotNull(mapper, "mapper");
        final double[][] result = new double[rowCount][columnCount];
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> result[i][j] = mapper.applyAsDouble(a[i][j]);

        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * Creates a new IntMatrix by applying the specified function to each element of this matrix.
     * The original matrix is not modified. Each double element is independently converted to an int
     * by the function, and the results are collected into a new IntMatrix with the same dimensions.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.6, 2.4}, {3.7, 4.2}});
     * IntMatrix rounded = matrix.mapToInt(d -> (int) Math.round(d));
     * // rounded is [[2, 2], [4, 4]]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param mapper the mapping function that converts each double element to an int; must not be null
     * @return a new IntMatrix with the mapped values (same dimensions as the original)
     * @throws E if the function throws an exception
     */
    public <E extends Exception> IntMatrix mapToInt(final Throwables.DoubleToIntFunction<E> mapper) throws E {
        N.checkArgNotNull(mapper, "mapper");
        final int[][] result = new int[rowCount][columnCount];
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> result[i][j] = mapper.applyAsInt(a[i][j]);

        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return IntMatrix.of(result);
    }

    /**
     * Creates a new LongMatrix by applying the specified function to each element of this matrix.
     * The original matrix is not modified. Each double element is independently converted to a long
     * by the function, and the results are collected into a new LongMatrix with the same dimensions.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.6, 2.4}, {3.7, 4.2}});
     * LongMatrix rounded = matrix.mapToLong(d -> Math.round(d));
     * // rounded is [[2L, 2L], [4L, 4L]]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param mapper the mapping function that converts each double element to a long; must not be null
     * @return a new LongMatrix with the mapped values (same dimensions as the original)
     * @throws E if the function throws an exception
     */
    public <E extends Exception> LongMatrix mapToLong(final Throwables.DoubleToLongFunction<E> mapper) throws E {
        N.checkArgNotNull(mapper, "mapper");
        final long[][] result = new long[rowCount][columnCount];
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> result[i][j] = mapper.applyAsLong(a[i][j]);

        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return LongMatrix.of(result);
    }

    /**
     * Creates a new object Matrix by applying the specified function to each element of this matrix.
     * The original matrix is not modified. Each double element is independently converted to an object
     * of type T by the function, and the results are collected into a new Matrix with the same dimensions.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.23, 4.56}, {7.89, 0.12}});
     * Matrix<String> stringMatrix = matrix.mapToObj(d -> String.format("%.2f", d), String.class);
     * // stringMatrix is [["1.23", "4.56"], ["7.89", "0.12"]]
     * }</pre>
     *
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the exception type that the function may throw
     * @param mapper the mapping function that converts each double element to type T; must not be null
     * @param targetElementType the class object representing the target element type (used for array creation); must not be null
     * @return a new Matrix&lt;T&gt; with the mapped values (same dimensions as the original)
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.DoubleFunction<? extends T, E> mapper, final Class<T> targetElementType) throws E {
        N.checkArgNotNull(mapper, "mapper");
        final T[][] result = Matrices.newMatrixArray(rowCount, columnCount, targetElementType);
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> result[i][j] = mapper.apply(a[i][j]);

        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return Matrix.of(result);
    }

    /**
     * Fills the entire matrix with the specified value in-place.
     * This method modifies the matrix directly, setting every element to the same value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.fill(0.0);
     * // Matrix is now [[0.0, 0.0], [0.0, 0.0]]
     *
     * DoubleMatrix identity = DoubleMatrix.of(new double[3][3]);
     * identity.fill(1.0);
     * // Creates a matrix filled with 1.0: [[1.0, 1.0, 1.0], [1.0, 1.0, 1.0], [1.0, 1.0, 1.0]]
     * }</pre>
     *
     * @param val the value to fill the matrix with
     */
    public void fill(final double val) {
        for (int i = 0; i < rowCount; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills the matrix with values from the specified two-dimensional array in-place, starting from position (0,0).
     * Values are copied up to the minimum of the matrix size and the source array size. If the source
     * array is smaller than the matrix, only the overlapping region is filled. If the source array is
     * larger, only the portion that fits is copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[3][3]);
     * matrix.copyFrom(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * // Top-left 2x2 region is filled: [[1.0, 2.0, 0.0], [3.0, 4.0, 0.0], [0.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param b the source array to copy values from (may be smaller or larger than the matrix)
     * @throws IllegalArgumentException if {@code b} is {@code null}
     */
    public void copyFrom(final double[][] b) {
        copyFrom(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from the specified two-dimensional array in-place, starting from a specified position.
     * Values are copied starting from the specified row and column indices. If the source array extends
     * beyond the matrix bounds from the starting position, only the portion that fits is copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[3][3]);
     * matrix.copyFrom(1, 1, new double[][] {{9.0, 8.0}, {7.0, 6.0}});
     * // Result: [[0.0, 0.0, 0.0], [0.0, 9.0, 8.0], [0.0, 7.0, 6.0]]
     * }</pre>
     *
     * @param destRowIndex the target row index in this matrix (0-based)
     * @param destColumnIndex the target column index in this matrix (0-based)
     * @param b the source array to copy values from
     * @throws IllegalArgumentException if the target indices are negative or exceed matrix dimensions
     */
    public void copyFrom(final int destRowIndex, final int destColumnIndex, final double[][] b) throws IllegalArgumentException {
        N.checkArgNotNull(b, "b");
        N.checkArgument(destRowIndex >= 0 && destRowIndex <= rowCount, "destRowIndex({}) must be between 0 and rowCount({})", destRowIndex, rowCount);
        N.checkArgument(destColumnIndex >= 0 && destColumnIndex <= columnCount, "destColumnIndex({}) must be between 0 and columnCount({})", destColumnIndex,
                columnCount);

        for (int i = 0, minLen = N.min(rowCount - destRowIndex, b.length); i < minLen; i++) {
            if (b[i] != null) {
                N.copy(b[i], 0, a[i + destRowIndex], destColumnIndex, N.min(b[i].length, columnCount - destColumnIndex));
            }
        }
    }

    /**
     * Returns a copy of this matrix.
     *
     * <p>The returned matrix is completely independent from the original. All elements
     * are copied into a new two-dimensional array, ensuring that modifications to either
     * the copy or the original will not affect the other.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix original = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix copy = original.copy();
     *
     * // Modifying the copy does NOT affect the original
     * copy.set(0, 0, 99.0);
     * assert original.get(0, 0)   == 1.0;  // Original unchanged
     * assert copy.get(0, 0)       == 99.0;  // Copy modified
     * }</pre>
     *
     * @return a new matrix that is a copy of this matrix with full independence guarantee
     */
    @Override
    public DoubleMatrix copy() {
        final double[][] c = new double[rowCount][];

        for (int i = 0; i < rowCount; i++) {
            c[i] = a[i].clone();
        }

        return new DoubleMatrix(c);
    }

    /**
     * Returns a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows and is completely independent from the original matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
     * DoubleMatrix partial = matrix.copy(1, 3);   // Returns [[3.0, 4.0], [5.0, 6.0]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new DoubleMatrix containing a copy of the specified rows
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @Override
    public DoubleMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        checkRepresentableShape(toRowIndex - fromRowIndex, columnCount);

        final double[][] c = new double[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new DoubleMatrix(c);
    }

    /**
     * Returns a copy of a rectangular region from this matrix.
     * The returned matrix contains only the specified rows and columns and is completely
     * independent from the original matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * DoubleMatrix sub = matrix.copy(0, 2, 1, 3);   // Copy rows 0-1, columns 1-2
     * // sub is [[2.0, 3.0], [5.0, 6.0]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new DoubleMatrix containing the specified region with dimensions
     *         (toRowIndex - fromRowIndex) × (toColumnIndex - fromColumnIndex)
     * @throws IndexOutOfBoundsException if any index is out of bounds, fromRowIndex &gt; toRowIndex, or fromColumnIndex &gt; toColumnIndex
     */
    @Override
    public DoubleMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex)
            throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);
        checkRepresentableShape(toRowIndex - fromRowIndex, toColumnIndex - fromColumnIndex);
        final double[][] c = new double[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new DoubleMatrix(c);
    }

    /**
     * Returns a new matrix whose dimensions are exactly {@code newRowCount × newColumnCount},
     * anchored at the top-left corner of this matrix. New cells are filled with {@code 0.0}.
     *
     * <ul>
     *   <li><b>If a dimension shrinks</b> — elements beyond the new boundary are discarded
     *       (excess rows removed from the bottom, excess columns removed from the right).</li>
     *   <li><b>If a dimension grows</b> — new cells are filled with {@code 0.0}.</li>
     *   <li><b>Mixed case</b> — each dimension is treated independently, so it is valid
     *       to grow rows while truncating columns, or vice versa.</li>
     * </ul>
     *
     * <p>The original matrix is never modified; a new matrix is always returned.</p>
     *
     * <p><b>Comparison with {@link #extend(int, int, int, int)}:</b>
     * {@code resize} takes <em>absolute</em> target dimensions and may truncate existing content.
     * {@code extend} takes <em>relative</em> padding amounts per edge and <em>never truncates</em>.
     * Use {@code extend} when the entire original content must be preserved.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
     *
     * // Grow: both dimensions larger — new cells filled with 0.0
     * DoubleMatrix grown = matrix.resize(4, 4);
     * // Result: [[1.0, 2.0, 3.0, 0.0],
     * //          [4.0, 5.0, 6.0, 0.0],
     * //          [7.0, 8.0, 9.0, 0.0],
     * //          [0.0, 0.0, 0.0, 0.0]]
     *
     * // Truncate: both dimensions smaller — bottom rows and right columns discarded
     * DoubleMatrix truncated = matrix.resize(2, 2);
     * // Result: [[1.0, 2.0],
     * //          [4.0, 5.0]]
     *
     * // Mixed: grow rows, truncate columns
     * DoubleMatrix mixed = matrix.resize(4, 2);
     * // Result: [[1.0, 2.0],
     * //          [4.0, 5.0],
     * //          [7.0, 8.0],
     * //          [0.0, 0.0]]
     * }</pre>
     *
     * @param newRowCount the row count of the returned matrix; must be {@code >= 0}
     * @param newColumnCount the column count of the returned matrix; must be {@code >= 0}
     * @return a new DoubleMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRowCount} or {@code newColumnCount} is negative
     * @see #resize(int, int, double)
     * @see #extend(int, int, int, int)
     */
    public DoubleMatrix resize(final int newRowCount, final int newColumnCount) {
        return resize(newRowCount, newColumnCount, 0);
    }

    /**
     * Returns a new matrix whose dimensions are exactly {@code newRowCount × newColumnCount},
     * anchored at the top-left corner of this matrix. New cells are filled with {@code defaultValueForNewCell}.
     *
     * <ul>
     *   <li><b>If a dimension shrinks</b> — elements beyond the new boundary are discarded
     *       (excess rows removed from the bottom, excess columns removed from the right).</li>
     *   <li><b>If a dimension grows</b> — new cells are filled with {@code defaultValueForNewCell}.</li>
     *   <li><b>Mixed case</b> — each dimension is treated independently, so it is valid
     *       to grow rows while truncating columns, or vice versa.</li>
     * </ul>
     *
     * <p>The original matrix is never modified; a new matrix is always returned.</p>
     *
     * <p><b>Comparison with {@link #extend(int, int, int, int, double)}:</b>
     * {@code resize} takes <em>absolute</em> target dimensions and may truncate existing content.
     * {@code extend} takes <em>relative</em> padding amounts per edge and <em>never truncates</em>.
     * Use {@code extend} when the entire original content must be preserved.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
     *
     * // Grow: both dimensions larger — new cells filled with 9.0
     * DoubleMatrix grown = matrix.resize(4, 4, 9.0);
     * // Result: [[1.0, 2.0, 3.0, 9.0],
     * //          [4.0, 5.0, 6.0, 9.0],
     * //          [7.0, 8.0, 9.0, 9.0],
     * //          [9.0, 9.0, 9.0, 9.0]]
     *
     * // Truncate: both dimensions smaller — bottom rows and right columns discarded
     * DoubleMatrix truncated = matrix.resize(2, 2, 9.0);
     * // Result: [[1.0, 2.0],
     * //          [4.0, 5.0]]
     *
     * // Mixed: grow rows, truncate columns
     * DoubleMatrix mixed = matrix.resize(4, 2, 9.0);
     * // Result: [[1.0, 2.0],
     * //          [4.0, 5.0],
     * //          [7.0, 8.0],
     * //          [9.0, 9.0]]
     * }</pre>
     *
     * @param newRowCount the row count of the returned matrix; must be {@code >= 0}
     * @param newColumnCount the column count of the returned matrix; must be {@code >= 0}
     * @param defaultValueForNewCell the double value used to fill any newly created cells
     * @return a new DoubleMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRowCount} or {@code newColumnCount} is negative
     * @see #resize(int, int)
     * @see #extend(int, int, int, int, double)
     */
    public DoubleMatrix resize(final int newRowCount, final int newColumnCount, final double defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRowCount >= 0, MSG_NEGATIVE_DIMENSION, "newRowCount", newRowCount);
        N.checkArgument(newColumnCount >= 0, MSG_NEGATIVE_DIMENSION, "newColumnCount", newColumnCount);
        checkRepresentableShape(newRowCount, newColumnCount);
        // Check for overflow before allocation
        if ((long) newRowCount * newColumnCount > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Matrix dimensions overflow: " + newRowCount + " x " + newColumnCount + " exceeds Integer.MAX_VALUE");
        }

        if (newRowCount <= rowCount && newColumnCount <= columnCount) {
            return copy(0, newRowCount, 0, newColumnCount);
        } else {
            final boolean fillDefaultValue = Double.doubleToRawLongBits(defaultValueForNewCell) != 0;
            final double[][] b = new double[newRowCount][];

            for (int i = 0; i < newRowCount; i++) {
                b[i] = i < rowCount ? N.copyOf(a[i], newColumnCount) : new double[newColumnCount];

                if (fillDefaultValue) {
                    if (i >= rowCount) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (columnCount < newColumnCount) {
                        N.fill(b[i], columnCount, newColumnCount, defaultValueForNewCell);
                    }
                }
            }

            return new DoubleMatrix(b);
        }
    }

    /**
     * Returns a new matrix formed by adding {@code 0.0}-filled padding around every edge of this matrix.
     * The original content is preserved in its entirety at the interior of the result.
     *
     * <p>The result dimensions are:
     * <ul>
     *   <li>Rows: {@code toUp + this.rowCount + toDown}</li>
     *   <li>Columns: {@code toLeft + this.columnCount + toRight}</li>
     * </ul>
     *
     * <p><b>Unlike {@link #resize(int, int)}, this method never truncates existing content.</b>
     * All elements of the original matrix appear unchanged in the result.</p>
     *
     * <p><b>Comparison with {@link #resize(int, int)}:</b>
     * {@code extend} takes <em>relative</em> padding amounts per edge and never truncates.
     * {@code resize} takes <em>absolute</em> target dimensions and may discard content.
     * Use {@code resize} when you need exact output dimensions regardless of the original size.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}});
     *
     * // Uniform border of 1 cell on every side
     * DoubleMatrix bordered = matrix.extend(1, 1, 1, 1);
     * // Result: [[0.0, 0.0, 0.0, 0.0],
     * //          [0.0, 1.0, 2.0, 0.0],
     * //          [0.0, 0.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param toUp number of rows to add above; must be {@code >= 0}
     * @param toDown number of rows to add below; must be {@code >= 0}
     * @param toLeft number of columns to add to the left; must be {@code >= 0}
     * @param toRight number of columns to add to the right; must be {@code >= 0}
     * @return a new DoubleMatrix with dimensions {@code (toUp+rowCount+toDown) × (toLeft+columnCount+toRight)}
     * @throws IllegalArgumentException if any padding parameter is negative
     * @see #extend(int, int, int, int, double)
     * @see #resize(int, int)
     */
    public DoubleMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, 0);
    }

    /**
     * Returns a new matrix formed by adding {@code defaultValueForNewCell}-filled padding around every edge
     * of this matrix. The original content is preserved in its entirety at the interior of the result.
     *
     * <p>The result dimensions are:
     * <ul>
     *   <li>Rows: {@code toUp + this.rowCount + toDown}</li>
     *   <li>Columns: {@code toLeft + this.columnCount + toRight}</li>
     * </ul>
     *
     * <p><b>Unlike {@link #resize(int, int, double)}, this method never truncates existing content.</b>
     * All elements of the original matrix appear unchanged in the result.</p>
     *
     * <p><b>Typical uses:</b> zero-padding before convolution, adding sentinel borders, or creating
     * asymmetric margins (e.g. more padding on one side than another).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}});
     *
     * // Asymmetric padding: 2 columns on the left, 1 on the right
     * DoubleMatrix padded = matrix.extend(1, 1, 2, 1, 9.0);
     * // Result: [[9.0, 9.0, 9.0, 9.0, 9.0],
     * //          [9.0, 9.0, 1.0, 2.0, 9.0],
     * //          [9.0, 9.0, 9.0, 9.0, 9.0]]
     *
     * // Uniform border of 1 cell on every side
     * DoubleMatrix bordered = matrix.extend(1, 1, 1, 1, 0.0);
     * // Result: [[0.0, 0.0, 0.0, 0.0],
     * //          [0.0, 1.0, 2.0, 0.0],
     * //          [0.0, 0.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param toUp number of rows to add above; must be {@code >= 0}
     * @param toDown number of rows to add below; must be {@code >= 0}
     * @param toLeft number of columns to add to the left; must be {@code >= 0}
     * @param toRight number of columns to add to the right; must be {@code >= 0}
     * @param defaultValueForNewCell the double value used to fill all newly added cells
     * @return a new DoubleMatrix with dimensions {@code (toUp+rowCount+toDown) × (toLeft+columnCount+toRight)}
     * @throws IllegalArgumentException if any padding parameter is negative,
     *         or if the resulting dimensions would overflow {@code Integer.MAX_VALUE}
     * @see #extend(int, int, int, int)
     * @see #resize(int, int, double)
     */
    public DoubleMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final double defaultValueForNewCell)
            throws IllegalArgumentException {
        N.checkArgument(toUp >= 0, MSG_NEGATIVE_DIMENSION, "toUp", toUp);
        N.checkArgument(toDown >= 0, MSG_NEGATIVE_DIMENSION, "toDown", toDown);
        N.checkArgument(toLeft >= 0, MSG_NEGATIVE_DIMENSION, "toLeft", toLeft);
        N.checkArgument(toRight >= 0, MSG_NEGATIVE_DIMENSION, "toRight", toRight);

        if (toUp == 0 && toDown == 0 && toLeft == 0 && toRight == 0) {
            return copy();
        } else {
            if ((long) toUp + rowCount + toDown > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Result row count overflow: " + toUp + " + " + rowCount + " + " + toDown + " exceeds Integer.MAX_VALUE");
            }

            if ((long) toLeft + columnCount + toRight > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(
                        "Result column count overflow: " + toLeft + " + " + columnCount + " + " + toRight + " exceeds Integer.MAX_VALUE");
            }

            final int newRowCount = toUp + rowCount + toDown;
            final int newColumnCount = toLeft + columnCount + toRight;
            checkRepresentableShape(newRowCount, newColumnCount);
            final boolean fillDefaultValue = Double.doubleToRawLongBits(defaultValueForNewCell) != 0;
            final double[][] b = new double[newRowCount][newColumnCount];

            for (int i = 0; i < newRowCount; i++) {
                if (i >= toUp && i < toUp + rowCount) {
                    N.copy(a[i - toUp], 0, b[i], toLeft, columnCount);
                }

                if (fillDefaultValue) {
                    if (i < toUp || i >= toUp + rowCount) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (columnCount < newColumnCount) {
                        if (toLeft > 0) {
                            N.fill(b[i], 0, toLeft, defaultValueForNewCell);
                        }

                        if (toRight > 0) {
                            N.fill(b[i], columnCount + toLeft, newColumnCount, defaultValueForNewCell);
                        }
                    }
                }
            }

            return new DoubleMatrix(b);
        }
    }

    /**
     * Reverses the order of elements in each row (horizontal flip in-place).
     * This method modifies the matrix in place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}});
     * matrix.flipInPlaceHorizontally();   // matrix is now [[3.0, 2.0, 1.0]]
     * }</pre>
     *
     * @see #flipHorizontally()
     */
    public void flipInPlaceHorizontally() {
        for (int i = 0; i < rowCount; i++) {
            N.reverse(a[i]);
        }
    }

    /**
     * Reverses the order of rows (vertical flip in-place).
     * This method modifies the matrix in place by reversing the order of rows.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0}, {2.0}, {3.0}});
     * matrix.flipInPlaceVertically();   // matrix is now [[3.0], [2.0], [1.0]]
     * }</pre>
     *
     * @see #flipVertically()
     */
    public void flipInPlaceVertically() {
        for (int j = 0; j < columnCount; j++) {
            double tmp = 0;
            for (int l = 0, h = rowCount - 1; l < h;) {
                tmp = a[l][j];
                a[l++][j] = a[h][j];
                a[h--][j] = tmp;
            }
        }
    }

    /**
     * Returns a new matrix that is a horizontal flip of this matrix (columns in reversed order within each row).
     * The original matrix is not modified. This is a non-mutating version of {@link #flipInPlaceHorizontally()}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}});
     * DoubleMatrix flipped = matrix.flipHorizontally();   // returns [[3.0, 2.0, 1.0]], original unchanged
     * }</pre>
     *
     * @return a new matrix that is a horizontal flip of this matrix (columns in reversed order within each row)
     * @see #flipInPlaceHorizontally()
     * @see #flipVertically()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">MATLAB flip function</a>
     */
    public DoubleMatrix flipHorizontally() {
        final DoubleMatrix res = this.copy();
        res.flipInPlaceHorizontally();
        return res;
    }

    /**
     * Returns a new matrix that is a vertical flip of this matrix (rows in reversed order).
     * The original matrix is not modified. This is a non-mutating version of {@link #flipInPlaceVertically()}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0}, {2.0}, {3.0}});
     * DoubleMatrix flipped = matrix.flipVertically();   // returns [[3.0], [2.0], [1.0]], original unchanged
     * }</pre>
     *
     * @return a new matrix that is a vertical flip of this matrix (rows in reversed order)
     * @see #flipInPlaceVertically()
     * @see #flipHorizontally()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">MATLAB flip function</a>
     */
    public DoubleMatrix flipVertically() {
        final DoubleMatrix res = this.copy();
        res.flipInPlaceVertically();
        return res;
    }

    /**
     * Returns a new matrix that is this matrix rotated 90 degrees clockwise.
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the last row of the original, reading upward.
     * The original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 90° clockwise:
     * // 1.0 2.0      3.0 1.0
     * // 3.0 4.0  =>  4.0 2.0
     * }</pre>
     *
     * @return a new matrix that is this matrix rotated 90 degrees clockwise
     */
    @Override
    public DoubleMatrix rotate90() {
        if (columnCount == 0) {
            return EMPTY_DOUBLE_MATRIX;
        }

        checkRepresentableShape(columnCount, rowCount);

        final double[][] c = new double[columnCount][rowCount];

        if (rowCount <= columnCount) {
            for (int j = 0; j < rowCount; j++) {
                for (int i = 0; i < columnCount; i++) {
                    c[i][j] = a[rowCount - j - 1][i];
                }
            }
        } else {
            for (int i = 0; i < columnCount; i++) {
                for (int j = 0; j < rowCount; j++) {
                    c[i][j] = a[rowCount - j - 1][i];
                }
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Returns a new matrix that is this matrix rotated 180 degrees clockwise.
     * This is equivalent to flipping both horizontally and vertically, reversing the
     * order of all elements. The resulting matrix has the same dimensions as the original.
     * The original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 180°:
     * // 1.0 2.0      4.0 3.0
     * // 3.0 4.0  =>  2.0 1.0
     * }</pre>
     *
     * @return a new matrix that is this matrix rotated 180 degrees clockwise
     */
    @Override
    public DoubleMatrix rotate180() {
        final double[][] c = new double[rowCount][];

        for (int i = 0; i < rowCount; i++) {
            c[i] = a[rowCount - i - 1].clone();
            N.reverse(c[i]);
        }

        return new DoubleMatrix(c);
    }

    /**
     * Returns a new matrix that is this matrix rotated 270 degrees clockwise.
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the first row of the original, reading downward.
     * The original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 270° clockwise:
     * // 1.0 2.0      2.0 4.0
     * // 3.0 4.0  =>  1.0 3.0
     * }</pre>
     *
     * @return a new matrix that is this matrix rotated 270 degrees clockwise
     */
    @Override
    public DoubleMatrix rotate270() {
        if (columnCount == 0) {
            return EMPTY_DOUBLE_MATRIX;
        }

        checkRepresentableShape(columnCount, rowCount);

        final double[][] c = new double[columnCount][rowCount];

        if (rowCount <= columnCount) {
            for (int j = 0; j < rowCount; j++) {
                for (int i = 0; i < columnCount; i++) {
                    c[i][j] = a[j][columnCount - i - 1];
                }
            }
        } else {
            for (int i = 0; i < columnCount; i++) {
                for (int j = 0; j < rowCount; j++) {
                    c[i][j] = a[j][columnCount - i - 1];
                }
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Creates the transpose of this matrix by swapping rows and columns.
     * The transpose operation converts each row into a column, so element at position (i, j)
     * in the original matrix appears at position (j, i) in the transposed matrix. The resulting
     * matrix has dimensions swapped (rowCount × columnCount becomes columnCount × rowCount).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Transposed:
     * // 1.0 2.0 3.0  1.0 4.0
     * // 4.0 5.0 6.0  2.0 5.0
     * //              3.0 6.0
     *
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * DoubleMatrix transposed = matrix.transpose();   // 2×3 becomes 3×2
     * }</pre>
     *
     * @return a new matrix that is the transpose of this matrix with dimensions columnCount × rowCount
     */
    @Override
    public DoubleMatrix transpose() {
        if (columnCount == 0) {
            return EMPTY_DOUBLE_MATRIX;
        }

        checkRepresentableShape(columnCount, rowCount);

        final double[][] c = new double[columnCount][rowCount];

        if (rowCount <= columnCount) {
            for (int j = 0; j < rowCount; j++) {
                for (int i = 0; i < columnCount; i++) {
                    c[i][j] = a[j][i];
                }
            }
        } else {
            for (int i = 0; i < columnCount; i++) {
                for (int j = 0; j < rowCount; j++) {
                    c[i][j] = a[j][i];
                }
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Reshapes the matrix to new dimensions while preserving element order.
     * Elements are read in row-major order from the original matrix and placed into the new shape.
     *
     * <p>The new shape must have at least as many total elements as the original
     * ({@code newRowCount * newColumnCount >= elementCount()}).
     * If the new shape has more total elements, the additional positions are filled with zeros.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * DoubleMatrix reshaped = matrix.reshape(3, 2);   // Becomes [[1.0, 2.0], [3.0, 4.0], [5.0, 6.0]]
     * DoubleMatrix extended = matrix.reshape(2, 4);   // Becomes [[1.0, 2.0, 3.0, 4.0], [5.0, 6.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param newRowCount the number of rows in the reshaped matrix
     * @param newColumnCount the number of columns in the reshaped matrix
     * @return a new DoubleMatrix with the specified shape containing this matrix's elements
     * @throws IllegalArgumentException if the new shape is too small to hold all elements
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public DoubleMatrix reshape(final int newRowCount, final int newColumnCount) {
        N.checkArgument(newRowCount >= 0, MSG_NEGATIVE_DIMENSION, "newRowCount", newRowCount);
        N.checkArgument(newColumnCount >= 0, MSG_NEGATIVE_DIMENSION, "newColumnCount", newColumnCount);
        checkRepresentableShape(newRowCount, newColumnCount);
        N.checkArgument((long) newRowCount * newColumnCount >= elementCount(), "New shape [{}x{}={}] is too small to hold all {} elements", newRowCount,
                newColumnCount, (long) newRowCount * newColumnCount, elementCount());

        final double[][] c = new double[newRowCount][newColumnCount];

        if (newRowCount == 0 || newColumnCount == 0 || N.isEmpty(a)) {
            return new DoubleMatrix(c);
        }

        final int rowLen = (int) N.min(newRowCount, elementCount % newColumnCount == 0 ? elementCount / newColumnCount : elementCount / newColumnCount + 1);

        if (a.length == 1) {
            for (int i = 0; i < rowLen; i++) {
                N.copy(a[0], i * newColumnCount, c[i], 0, (int) N.min(newColumnCount, elementCount - (long) i * newColumnCount));
            }
        } else {
            long cnt = 0;

            for (int i = 0; i < rowLen; i++) {
                for (int j = 0, col = (int) N.min(newColumnCount, elementCount - (long) i * newColumnCount); j < col; j++, cnt++) {
                    c[i][j] = a[(int) (cnt / columnCount)][(int) (cnt % columnCount)];
                }
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Repeats elements of the matrix in both row and column directions.
     * Each element is repeated {@code rowRepeats} times vertically and {@code columnRepeats} times horizontally.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix repeated = matrix.repeatElements(2, 3);
     * // Result: [[1.0, 1.0, 1.0, 2.0, 2.0, 2.0],
     * //          [1.0, 1.0, 1.0, 2.0, 2.0, 2.0],
     * //          [3.0, 3.0, 3.0, 4.0, 4.0, 4.0],
     * //          [3.0, 3.0, 3.0, 4.0, 4.0, 4.0]]
     * }</pre>
     *
     * @param rowRepeats the number of times to repeat each element in the row direction
     * @param columnRepeats the number of times to repeat each element in the column direction
     * @return a new matrix with repeated elements
     * @throws IllegalArgumentException if rowRepeats or columnRepeats is not positive
     * @see IntMatrix#repeatElements(int, int)
     */
    @Override
    public DoubleMatrix repeatElements(final int rowRepeats, final int columnRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && columnRepeats > 0, MSG_REPEATS_NOT_POSITIVE, rowRepeats, columnRepeats);

        // Check for overflow before allocation
        if ((long) rowCount * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result row count overflow: " + rowCount + " * " + rowRepeats + " exceeds Integer.MAX_VALUE");
        }
        if ((long) columnCount * columnRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result column count overflow: " + columnCount + " * " + columnRepeats + " exceeds Integer.MAX_VALUE");
        }

        final double[][] c = new double[rowCount * rowRepeats][columnCount * columnRepeats];

        for (int i = 0; i < rowCount; i++) {
            final double[] aa = a[i];
            final double[] fr = c[i * rowRepeats];

            for (int j = 0; j < columnCount; j++) {
                N.copy(Array.repeat(aa[j], columnRepeats), 0, fr, j * columnRepeats, columnRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Repeats the entire matrix in both row and column directions.
     * The matrix is tiled {@code rowRepeats} times vertically and {@code columnRepeats} times horizontally.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix tiled = matrix.repeatMatrix(2, 3);
     * // Result: [[1.0, 2.0, 1.0, 2.0, 1.0, 2.0],
     * //          [3.0, 4.0, 3.0, 4.0, 3.0, 4.0],
     * //          [1.0, 2.0, 1.0, 2.0, 1.0, 2.0],
     * //          [3.0, 4.0, 3.0, 4.0, 3.0, 4.0]]
     * }</pre>
     *
     * @param rowRepeats the number of times to repeat the matrix in the row direction
     * @param columnRepeats the number of times to repeat the matrix in the column direction
     * @return a new matrix with the tiled pattern
     * @throws IllegalArgumentException if rowRepeats or columnRepeats is not positive
     * @see IntMatrix#repeatMatrix(int, int)
     */
    @Override
    public DoubleMatrix repeatMatrix(final int rowRepeats, final int columnRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && columnRepeats > 0, MSG_REPEATS_NOT_POSITIVE, rowRepeats, columnRepeats);

        // Check for overflow before allocation
        if ((long) rowCount * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result row count overflow: " + rowCount + " * " + rowRepeats + " exceeds Integer.MAX_VALUE");
        }
        if ((long) columnCount * columnRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result column count overflow: " + columnCount + " * " + columnRepeats + " exceeds Integer.MAX_VALUE");
        }

        final double[][] c = new double[rowCount * rowRepeats][columnCount * columnRepeats];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnRepeats; j++) {
                N.copy(a[i], 0, c[i], j * columnCount, columnCount);
            }
        }

        for (int i = 1; i < rowRepeats; i++) {
            for (int j = 0; j < rowCount; j++) {
                N.copy(c[j], 0, c[i * rowCount + j], 0, c[j].length);
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Returns a DoubleList containing all matrix elements in row-major order (left-to-right, top-to-bottom).
     * The matrix is flattened into a single-dimensional list.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleList list = matrix.flatten();   // Returns DoubleList containing [1.0, 2.0, 3.0, 4.0]
     * }</pre>
     *
     * @return a DoubleList containing all elements in row-major order
     * @throws IllegalStateException if the matrix is too large to flatten (more than Integer.MAX_VALUE elements)
     */
    @Override
    public DoubleList flatten() {
        // Check for overflow before allocation
        if ((long) rowCount * columnCount > Integer.MAX_VALUE) {
            throw new IllegalStateException("Matrix too large to flatten: " + rowCount + " x " + columnCount);
        }

        final double[] c = new double[rowCount * columnCount];

        for (int i = 0; i < rowCount; i++) {
            N.copy(a[i], 0, c, i * columnCount, columnCount);
        }

        return DoubleList.of(c);
    }

    /**
     * Flattens all elements of this matrix into a single one-dimensional array, applies the given
     * operation to that flattened array, and then copies the modified elements back into the matrix.
     *
     * <p>This enables operations that need a global view of all matrix elements (e.g., sorting all
     * elements across the entire matrix). The operation receives a temporary flattened copy; after
     * the operation completes, the modified values are written back into the matrix row by row.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{5.0, 3.0}, {4.0, 1.0}});
     * matrix.applyOnFlattened(arr -> java.util.Arrays.sort(arr));
     * // matrix is now [[1.0, 3.0], [4.0, 5.0]] (all elements sorted globally, then placed back row by row)
     * }</pre>
     *
     * @param <E> the type of exception that the operation may throw
     * @param action the operation to apply to the flattened array
     * @throws E if the operation throws an exception
     * @see Arrays#applyOnFlattened(double[][], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void applyOnFlattened(final Throwables.Consumer<? super double[], E> action) throws E {
        Arrays.applyOnFlattened(a, action);
    }

    /**
     * Vertically stacks this matrix with another matrix.
     * The matrices must have the same number of columns.
     * The result is a new matrix with rows from this matrix followed by rows from the other matrix.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix a = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix b = DoubleMatrix.of(new double[][] {{5.0, 6.0}});
     * DoubleMatrix stacked = a.stackVertically(b);
     * // Result: [[1.0, 2.0], [3.0, 4.0], [5.0, 6.0]]
     * }</pre>
     *
     * @param other the matrix to stack below this matrix
     * @return a new matrix with combined rows
     * @throws IllegalArgumentException if the matrices have different number of columns
     * @see IntMatrix#stackVertically(IntMatrix)
     */
    public DoubleMatrix stackVertically(final DoubleMatrix other) throws IllegalArgumentException {
        N.checkArgNotNull(other, "other");
        N.checkArgument(columnCount == other.columnCount, MSG_VSTACK_COLUMN_MISMATCH, columnCount, other.columnCount);
        final long mergedRowCount = (long) rowCount + other.rowCount;
        N.checkArgument(mergedRowCount <= Integer.MAX_VALUE, "Merged row count overflow: {} + {} = {}", rowCount, other.rowCount, mergedRowCount);

        final double[][] c = new double[(int) mergedRowCount][];
        int j = 0;

        for (int i = 0; i < rowCount; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < other.rowCount; i++) {
            c[j++] = other.a[i].clone();
        }

        return DoubleMatrix.of(c);
    }

    /**
     * Horizontally stacks this matrix with another matrix.
     * The matrices must have the same number of rows.
     * The result is a new matrix with columns from this matrix followed by columns from the other matrix.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix a = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix b = DoubleMatrix.of(new double[][] {{5.0}, {6.0}});
     * DoubleMatrix stacked = a.stackHorizontally(b);
     * // Result: [[1.0, 2.0, 5.0], [3.0, 4.0, 6.0]]
     * }</pre>
     *
     * @param other the matrix to stack to the right of this matrix
     * @return a new matrix with combined columns
     * @throws IllegalArgumentException if the matrices have different number of rows
     * @see IntMatrix#stackHorizontally(IntMatrix)
     */
    public DoubleMatrix stackHorizontally(final DoubleMatrix other) throws IllegalArgumentException {
        N.checkArgNotNull(other, "other");
        N.checkArgument(rowCount == other.rowCount, MSG_HSTACK_ROW_MISMATCH, rowCount, other.rowCount);
        final long mergedColumnCount = (long) columnCount + other.columnCount;
        N.checkArgument(mergedColumnCount <= Integer.MAX_VALUE, "Merged column count overflow: {} + {} = {}", columnCount, other.columnCount,
                mergedColumnCount);

        final double[][] c = new double[rowCount][(int) mergedColumnCount];

        for (int i = 0; i < rowCount; i++) {
            N.copy(a[i], 0, c[i], 0, columnCount);
            N.copy(other.a[i], 0, c[i], columnCount, other.columnCount);
        }

        return DoubleMatrix.of(c);
    }

    /**
     * Performs element-wise addition of this matrix with another matrix.
     * The matrices must have the same dimensions.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix a = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix b = DoubleMatrix.of(new double[][] {{5.0, 6.0}, {7.0, 8.0}});
     * DoubleMatrix sum = a.add(b);   // [[6.0, 8.0], [10.0, 12.0]]
     * }</pre>
     *
     * @param other the matrix to add to this matrix
     * @return a new matrix containing the element-wise sum
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public DoubleMatrix add(final DoubleMatrix other) throws IllegalArgumentException {
        N.checkArgNotNull(other, "other");
        N.checkArgument(Matrices.isSameShape(this, other), "Cannot add matrices with different shapes: this is {}x{} but other is {}x{}", rowCount, columnCount,
                other.rowCount, other.columnCount);

        final double[][] otherData = other.a;
        final double[][] result = new double[rowCount][columnCount];
        final Throwables.IntBiConsumer<RuntimeException> elementAction = (i, j) -> result[i][j] = a[i][j] + otherData[i][j];

        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * Performs element-wise subtraction of another matrix from this matrix.
     * The matrices must have the same dimensions.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix a = DoubleMatrix.of(new double[][] {{5.0, 6.0}, {7.0, 8.0}});
     * DoubleMatrix b = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix diff = a.subtract(b);   // [[4.0, 4.0], [4.0, 4.0]]
     * }</pre>
     *
     * @param other the matrix to subtract from this matrix
     * @return a new matrix containing the element-wise difference
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public DoubleMatrix subtract(final DoubleMatrix other) throws IllegalArgumentException {
        N.checkArgNotNull(other, "other");
        N.checkArgument(Matrices.isSameShape(this, other), "Cannot subtract matrices with different shapes: this is {}x{} but other is {}x{}", rowCount,
                columnCount, other.rowCount, other.columnCount);

        final double[][] otherData = other.a;
        final double[][] result = new double[rowCount][columnCount];
        final Throwables.IntBiConsumer<RuntimeException> elementAction = (i, j) -> result[i][j] = a[i][j] - otherData[i][j];

        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * Performs matrix multiplication with another matrix.
     * The number of columns in this matrix must equal the number of rows in the other matrix.
     * Results in a matrix of dimensions (this.rowCount × other.columnCount).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix a = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix b = DoubleMatrix.of(new double[][] {{5.0, 6.0}, {7.0, 8.0}});
     * DoubleMatrix product = a.multiply(b);   // [[19.0, 22.0], [43.0, 50.0]]
     * }</pre>
     *
     * @param other the matrix to multiply with this matrix
     * @return a new matrix containing the matrix product
     * @throws IllegalArgumentException if the matrix dimensions are incompatible for multiplication
     */
    public DoubleMatrix multiply(final DoubleMatrix other) throws IllegalArgumentException {
        N.checkArgNotNull(other, "other");
        N.checkArgument(columnCount == other.rowCount,
                "Matrix dimensions incompatible for multiplication: this is {}x{}, other is {}x{} (this.columnCount must equal other.rowCount)", rowCount,
                columnCount, other.rowCount, other.columnCount);

        final double[][] otherData = other.a;
        final double[][] result = new double[rowCount][other.columnCount];
        final Throwables.IntTriConsumer<RuntimeException> multiplyAction = (i, j, k) -> result[i][j] += a[i][k] * otherData[k][j];

        Matrices.forEachCartesianIndices(this, other, multiplyAction);

        return new DoubleMatrix(result);
    }

    /**
     * Converts this primitive double matrix to a boxed Double matrix.
     * Each primitive double value is boxed into a Double object. The original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix primitive = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * Matrix<Double> boxed = primitive.boxed();
     * // boxed is a Matrix<Double> with the same values
     * }</pre>
     *
     * @return a new Matrix&lt;Double&gt; containing boxed Double values (same dimensions as the original)
     * @see #unbox(Matrix)
     */
    public Matrix<Double> boxed() {
        final Double[][] c = new Double[rowCount][columnCount];

        if (rowCount <= columnCount) {
            for (int i = 0; i < rowCount; i++) {
                final double[] aa = a[i];
                final Double[] cc = c[i];

                for (int j = 0; j < columnCount; j++) {
                    cc[j] = aa[j]; // NOSONAR
                }
            }
        } else {
            for (int j = 0; j < columnCount; j++) {
                for (int i = 0; i < rowCount; i++) {
                    c[i][j] = a[i][j];
                }
            }
        }

        return new Matrix<>(c);
    }

    /**
     * Converts this double matrix to an int matrix.
     * Each double value is narrowed to int by casting, which truncates toward zero.
     *
     * <p><b>Warning:</b> This is a narrowing conversion that may lose information.
     * The fractional part is discarded, and values outside the int range
     * ({@code Integer.MIN_VALUE} to {@code Integer.MAX_VALUE}) will overflow.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix doubleMatrix = DoubleMatrix.of(new double[][] {{1.9, 2.1}, {3.5, 4.0}});
     * IntMatrix intMatrix = doubleMatrix.toIntMatrix();
     * // Result: [[1, 2],
     * //          [3, 4]]
     * }</pre>
     *
     * @return a new {@code IntMatrix} with values converted from double to int
     */
    public IntMatrix toIntMatrix() {
        final int[][] c = new int[rowCount][columnCount];

        if (rowCount <= columnCount) {
            for (int i = 0; i < rowCount; i++) {
                final double[] aa = a[i];
                final int[] cc = c[i];

                for (int j = 0; j < columnCount; j++) {
                    cc[j] = (int) aa[j];
                }
            }
        } else {
            for (int j = 0; j < columnCount; j++) {
                for (int i = 0; i < rowCount; i++) {
                    c[i][j] = (int) a[i][j];
                }
            }
        }

        return new IntMatrix(c);
    }

    /**
     * Converts this double matrix to a long matrix.
     * Each double value is narrowed to long by casting, which truncates toward zero.
     *
     * <p><b>Warning:</b> This is a narrowing conversion that may lose information.
     * The fractional part is discarded, and values outside the long range
     * may overflow.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix doubleMatrix = DoubleMatrix.of(new double[][] {{1.9, 2.1}, {3.5, 4.0}});
     * LongMatrix longMatrix = doubleMatrix.toLongMatrix();
     * // Result: [[1, 2],
     * //          [3, 4]]
     * }</pre>
     *
     * @return a new {@code LongMatrix} with values converted from double to long
     */
    public LongMatrix toLongMatrix() {
        final long[][] c = new long[rowCount][columnCount];

        if (rowCount <= columnCount) {
            for (int i = 0; i < rowCount; i++) {
                final double[] aa = a[i];
                final long[] cc = c[i];

                for (int j = 0; j < columnCount; j++) {
                    cc[j] = (long) aa[j];
                }
            }
        } else {
            for (int j = 0; j < columnCount; j++) {
                for (int i = 0; i < rowCount; i++) {
                    c[i][j] = (long) a[i][j];
                }
            }
        }

        return new LongMatrix(c);
    }

    /**
     * Converts this double matrix to a float matrix.
     * Each double value is narrowed to float by casting.
     *
     * <p><b>Warning:</b> This is a narrowing conversion that may lose precision.
     * Double values that cannot be exactly represented as float will be rounded
     * to the nearest float value. Values outside the float range will result in
     * positive or negative infinity.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix doubleMatrix = DoubleMatrix.of(new double[][] {{1.5, 2.5}, {3.0, 4.0}});
     * FloatMatrix floatMatrix = doubleMatrix.toFloatMatrix();
     * // Result: [[1.5f, 2.5f],
     * //          [3.0f, 4.0f]]
     * }</pre>
     *
     * @return a new {@code FloatMatrix} with values converted from double to float
     */
    public FloatMatrix toFloatMatrix() {
        final float[][] c = new float[rowCount][columnCount];

        if (rowCount <= columnCount) {
            for (int i = 0; i < rowCount; i++) {
                final double[] aa = a[i];
                final float[] cc = c[i];

                for (int j = 0; j < columnCount; j++) {
                    cc[j] = (float) aa[j];
                }
            }
        } else {
            for (int j = 0; j < columnCount; j++) {
                for (int i = 0; i < rowCount; i++) {
                    c[i][j] = (float) a[i][j];
                }
            }
        }

        return new FloatMatrix(c);
    }

    /**
     * Applies a binary operation element-wise to this matrix and another matrix.
     * The matrices must have the same dimensions. The original matrices are not modified.
     * Each pair of corresponding elements from the two matrices is combined using the zip function,
     * and the results are collected into a new matrix. The operation may be performed in parallel
     * for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix a = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix b = DoubleMatrix.of(new double[][] {{5.0, 6.0}, {7.0, 8.0}});
     * DoubleMatrix max = a.zipWith(b, Math::max);   // [[5.0, 6.0], [7.0, 8.0]]
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the matrix to combine with this matrix; must have the same dimensions and must not be null
     * @param zipFunction the binary operation to apply to corresponding elements; must not be null
     * @return a new matrix with the operation applied element-wise (same dimensions as the input matrices)
     * @throws IllegalArgumentException if the matrices have different dimensions
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> DoubleMatrix zipWith(final DoubleMatrix matrixB, final Throwables.DoubleBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Cannot zip matrices with different shapes: this is {}x{} but other is {}x{}", rowCount, columnCount,
                matrixB.rowCount, matrixB.columnCount);
        N.checkArgNotNull(zipFunction, "zipFunction");

        final double[][] matrixBData = matrixB.a;
        final double[][] result = new double[rowCount][columnCount];

        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> result[i][j] = zipFunction.applyAsDouble(a[i][j], matrixBData[i][j]);

        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * Applies a ternary operation element-wise to this matrix and two other matrices.
     * All three matrices must have the same dimensions. The original matrices are not modified.
     * Each triplet of corresponding elements from the three matrices is combined using the zip function,
     * and the results are collected into a new matrix. The operation may be performed in parallel
     * for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix a = DoubleMatrix.of(new double[][] {{1.0, 2.0}});
     * DoubleMatrix b = DoubleMatrix.of(new double[][] {{3.0, 4.0}});
     * DoubleMatrix c = DoubleMatrix.of(new double[][] {{5.0, 6.0}});
     * DoubleMatrix result = a.zipWith(b, c, (x, y, z) -> x + y * z);   // [[16.0, 26.0]]
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix to combine; must have the same dimensions and must not be null
     * @param matrixC the third matrix to combine; must have the same dimensions and must not be null
     * @param zipFunction the ternary operation to apply to corresponding elements; must not be null
     * @return a new matrix with the operation applied element-wise (same dimensions as the input matrices)
     * @throws IllegalArgumentException if the matrices have different dimensions
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> DoubleMatrix zipWith(final DoubleMatrix matrixB, final DoubleMatrix matrixC,
            final Throwables.DoubleTernaryOperator<E> zipFunction) throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Cannot zip matrices with different shapes: all matrices must be {}x{}", rowCount,
                columnCount);
        N.checkArgNotNull(zipFunction, "zipFunction");

        final double[][] matrixBData = matrixB.a;
        final double[][] matrixCData = matrixC.a;
        final double[][] result = new double[rowCount][columnCount];

        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> result[i][j] = zipFunction.applyAsDouble(a[i][j], matrixBData[i][j], matrixCData[i][j]);

        Matrices.forEachIndex(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * Returns a stream of elements from the main diagonal (upper-left to lower-right).
     * The matrix must be square (same number of rows and columns).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double[] diagonal = matrix.streamMainDiagonal().toArray();   // [1.0, 4.0]
     * }</pre>
     *
     * @return a DoubleStream of diagonal elements from upper-left to lower-right
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public DoubleStream streamMainDiagonal() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return DoubleStream.empty();
        }

        return DoubleStream.of(new DoubleIteratorEx() {
            private final int toIndex = rowCount;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public double nextDouble() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return a[cursor][cursor++];
            }

            @Override
            public void advance(final long n) {
                if (n <= 0) {
                    return;
                }

                cursor = n < toIndex - cursor ? cursor + (int) n : toIndex;
            }

            @Override
            public long count() {
                return toIndex - cursor; // NOSONAR
            }
        });
    }

    /**
     * Returns a stream of elements from the anti-diagonal (upper-right to lower-left).
     * The matrix must be square (same number of rows and columns).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double[] antiDiagonal = matrix.streamAntiDiagonal().toArray();   // [2.0, 3.0]
     * }</pre>
     *
     * @return a DoubleStream of diagonal elements from upper-right to lower-left
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public DoubleStream streamAntiDiagonal() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return DoubleStream.empty();
        }

        return DoubleStream.of(new DoubleIteratorEx() {
            private final int toIndex = rowCount;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public double nextDouble() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final double result = a[cursor][columnCount - cursor - 1];
                cursor++;
                return result;
            }

            @Override
            public void advance(final long n) {
                if (n <= 0) {
                    return;
                }

                cursor = n < toIndex - cursor ? cursor + (int) n : toIndex;
            }

            @Override
            public long count() {
                return toIndex - cursor; // NOSONAR
            }
        });
    }

    /**
     * Returns a stream of all elements in this matrix, traversed horizontally (left to right, top to bottom).
     *
     * <p>This method is useful for processing all matrix elements sequentially. The returned
     * stream can be used with all standard DoubleStream operations including sum, average, filter, map, etc.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double sum = matrix.streamHorizontal().sum();           // Returns 10.0 (1.0 + 2.0 + 3.0 + 4.0)
     * double[] array = matrix.streamHorizontal().toArray();   // Returns [1.0, 2.0, 3.0, 4.0]
     * }</pre>
     *
     * @return a DoubleStream of all matrix elements in row-major order, or an empty stream if the matrix is empty
     */
    @Override
    public DoubleStream streamHorizontal() {
        return streamHorizontal(0, rowCount);
    }

    /**
     * Returns a stream of elements from a single row.
     * The elements are streamed from left to right within the specified row.
     *
     * <p>This method is particularly useful when you need to process or analyze
     * a specific row of the matrix independently. The returned stream can be
     * used with all standard DoubleStream operations.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double[] row1 = matrix.streamHorizontal(1).toArray();   // Returns [3.0, 4.0]
     * double rowSum = matrix.streamHorizontal(1).sum();       // Returns 7.0
     * }</pre>
     *
     * @param rowIndex the index of the row to stream (0-based)
     * @return a DoubleStream of elements in the specified row, from left to right
     * @throws IndexOutOfBoundsException if rowIndex &lt; 0 or rowIndex &gt;= rowCount
     */
    @Override
    public DoubleStream streamHorizontal(final int rowIndex) {
        return streamHorizontal(rowIndex, rowIndex + 1);
    }

    /**
     * Returns a stream of elements from a range of rows in row-major order.
     * Elements are streamed row by row from the starting row (inclusive) to
     * the ending row (exclusive), with each row streamed from left to right.
     *
     * <p>This method allows for efficient processing of a subset of matrix rows.
     * The stream maintains the row-major order, meaning all elements from one row
     * are streamed before moving to the next row.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
     * double[] elements = matrix.streamHorizontal(1, 3).toArray();   // [3.0, 4.0, 5.0, 6.0]
     * double[] subset = matrix.streamHorizontal(0, 2).toArray();     // Returns [1.0, 2.0, 3.0, 4.0]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a DoubleStream of elements in the specified row range, or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    @Override
    public DoubleStream streamHorizontal(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);

        if (isEmpty()) {
            return DoubleStream.empty();
        }

        return DoubleStream.of(new DoubleIteratorEx() {
            private int i = fromRowIndex;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i < toRowIndex;
            }

            @Override
            public double nextDouble() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final double result = a[i][j++];

                if (j >= columnCount) {
                    i++;
                    j = 0;
                }

                return result;
            }

            @Override
            public void advance(final long n) {
                if (n <= 0) {
                    return;
                }

                if (n >= (long) (toRowIndex - i) * columnCount - j) {
                    i = toRowIndex;
                    j = 0;
                } else {
                    i += (int) ((n + j) / columnCount);
                    j = (int) ((n + j) % columnCount);
                }
            }

            @Override
            public long count() {
                return (long) (toRowIndex - i) * columnCount - j;
            }

            @Override
            public double[] toArray() {
                final int len = toArrayLength(count());
                final double[] c = new double[len];

                for (int k = 0; k < len; k++) {
                    c[k] = a[i][j++];

                    if (j >= columnCount) {
                        i++;
                        j = 0;
                    }
                }

                return c;
            }
        });
    }

    /**
     * Creates a stream of all elements in the matrix in column-major order.
     * Elements are streamed from top to bottom, left to right.
     * This method is marked as Beta and may change in future versions.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double[] columnMajor = matrix.streamVertical().toArray();   // [1.0, 3.0, 2.0, 4.0]
     * }</pre>
     *
     * @return a DoubleStream of all matrix elements in column-major order
     */
    @Override
    @Beta
    public DoubleStream streamVertical() {
        return streamVertical(0, columnCount);
    }

    /**
     * Creates a stream of elements from a single column in the matrix.
     * This is equivalent to calling {@code streamVertical(columnIndex, columnIndex + 1)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double[] column1 = matrix.streamVertical(1).toArray();   // Returns [2.0, 4.0]
     * }</pre>
     *
     * @param columnIndex the index of the column to stream (0-based)
     * @return a DoubleStream of elements in the specified column, from top to bottom
     * @throws IndexOutOfBoundsException if the column index is out of bounds
     */
    @Override
    public DoubleStream streamVertical(final int columnIndex) {
        return streamVertical(columnIndex, columnIndex + 1);
    }

    /**
     * Creates a stream of elements from a range of columns in column-major order.
     * Elements are streamed from top to bottom within each column, then left to right across columns.
     * This method is marked as Beta and may change in future versions.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * double[] elements = matrix.streamVertical(1, 3).toArray();   // [2.0, 5.0, 3.0, 6.0]
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a DoubleStream of elements in the specified column range
     * @throws IndexOutOfBoundsException if the column indices are out of bounds
     */
    @Override
    @Beta
    public DoubleStream streamVertical(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

        if (isEmpty()) {
            return DoubleStream.empty();
        }

        return DoubleStream.of(new DoubleIteratorEx() {
            private int i = 0;
            private int j = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return j < toColumnIndex;
            }

            @Override
            public double nextDouble() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final double result = a[i++][j];

                if (i >= rowCount) {
                    i = 0;
                    j++;
                }

                return result;
            }

            @Override
            public void advance(final long n) {
                if (n <= 0) {
                    return;
                }

                if (n >= (long) (toColumnIndex - j) * DoubleMatrix.this.rowCount - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    final long offset = n + i;
                    i = (int) (offset % DoubleMatrix.this.rowCount);
                    j += (int) (offset / DoubleMatrix.this.rowCount);
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rowCount - i; // NOSONAR
            }

            @Override
            public double[] toArray() {
                final int len = toArrayLength(count());
                final double[] c = new double[len];

                for (int k = 0; k < len; k++) {
                    c[k] = a[i++][j];

                    if (i >= rowCount) {
                        i = 0;
                        j++;
                    }
                }

                return c;
            }
        });
    }

    /**
     * Creates a stream of streams, where each inner stream represents a complete row of the matrix.
     * This is equivalent to calling {@code streamRows(0, rowCount)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.streamRows().forEach(row -> System.out.println(row.toList()));
     * // Prints: [1.0, 2.0]
     * //         [3.0, 4.0]
     * }</pre>
     *
     * @return a Stream of DoubleStreams, one for each row in the matrix
     */
    @Override
    public Stream<DoubleStream> streamRows() {
        return streamRows(0, rowCount);
    }

    /**
     * Creates a stream of streams for a range of rows.
     * Each inner stream represents a complete row of the matrix.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
     * List<double[]> rows = matrix.streamRows(1, 3)
     *     .map(stream -> stream.toArray())
     *     .toList();
     * // rows contains: [[3.0, 4.0], [5.0, 6.0]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a Stream of DoubleStreams for the specified row range
     * @throws IndexOutOfBoundsException if the row indices are out of bounds
     */
    @Override
    public Stream<DoubleStream> streamRows(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);

        return Stream.of(new ObjIteratorEx<>() {
            private final int toIndex = toRowIndex;
            private int cursor = fromRowIndex;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public DoubleStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return DoubleStream.of(a[cursor++]);
            }

            @Override
            public void advance(final long n) {
                if (n <= 0) {
                    return;
                }

                cursor = n < toIndex - cursor ? cursor + (int) n : toIndex;
            }

            @Override
            public long count() {
                return toIndex - cursor; // NOSONAR
            }
        });
    }

    /**
     * Creates a stream of streams, where each inner stream represents a complete column of the matrix.
     * This method is marked as Beta and may change in future versions.
     * This is equivalent to calling {@code streamColumns(0, columnCount)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.streamColumns().forEach(col -> System.out.println(col.toList()));
     * // Prints: [1.0, 3.0]
     * //         [2.0, 4.0]
     * }</pre>
     *
     * @return a Stream of DoubleStreams, one for each column in the matrix
     */
    @Override
    @Beta
    public Stream<DoubleStream> streamColumns() {
        return streamColumns(0, columnCount);
    }

    /**
     * Creates a stream of streams for a range of columns.
     * Each inner stream represents a complete column of the matrix.
     * This method is marked as Beta and may change in future versions.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * List<double[]> columns = matrix.streamColumns(1, 3)
     *     .map(stream -> stream.toArray())
     *     .toList();
     * // columns contains: [[2.0, 5.0], [3.0, 6.0]]
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a Stream of DoubleStreams for the specified column range
     * @throws IndexOutOfBoundsException if the column indices are out of bounds
     */
    @Override
    @Beta
    public Stream<DoubleStream> streamColumns(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private final int toIndex = toColumnIndex;
            private int cursor = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public DoubleStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return DoubleStream.of(new DoubleIteratorEx() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rowCount;
                    private int cursor2 = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor2 < toIndex2;
                    }

                    @Override
                    public double nextDouble() {
                        if (cursor2 >= toIndex2) {
                            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                        }

                        return a[cursor2++][columnIndex];
                    }

                    @Override
                    public void advance(final long n) throws IllegalArgumentException {
                        if (n <= 0) {
                            return;
                        }

                        cursor2 = n < toIndex2 - cursor2 ? cursor2 + (int) n : toIndex2;
                    }

                    @Override
                    public long count() {
                        return toIndex2 - cursor2; // NOSONAR
                    }
                });
            }

            @Override
            public void advance(final long n) {
                if (n <= 0) {
                    return;
                }

                cursor = n < toIndex - cursor ? cursor + (int) n : toIndex;
            }

            @Override
            public long count() {
                return toIndex - cursor; // NOSONAR
            }
        });
    }

    /**
     * Returns the length of the specified array.
     * This is an internal helper method used by the matrix framework.
     *
     * @param a the array to measure
     * @return the length of the array, or 0 if the array is null
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final double[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * Performs the specified action for each element in this matrix.
     * For large matrices, the operation may be parallelized to improve performance,
     * so the order of execution is not guaranteed. This method does not modify the matrix
     * unless the action itself modifies external state.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.forEach(value -> System.out.print(value + " "));
     * // Prints all values (order may vary): 1.0 2.0 3.0 4.0
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the action to perform on each element; must not be null
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> action) throws E {
        forEach(0, rowCount, 0, columnCount, action);
    }

    /**
     * Performs the specified action for each element in the specified sub-region of this matrix.
     * For large sub-regions, the operation may be parallelized to improve performance, so the order
     * of execution is not guaranteed. This method does not modify the matrix unless the action itself
     * modifies external state.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * matrix.forEach(0, 2, 1, 3, value -> System.out.print(value + " "));
     * // Prints values in columns 1-2 (order may vary): 2.0 3.0 5.0 6.0
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param fromRowIndex the starting row index (inclusive, 0-based, must be &gt;= 0 and &lt; rowCount)
     * @param toRowIndex the ending row index (exclusive, must be &gt; fromRowIndex and &lt;= rowCount)
     * @param fromColumnIndex the starting column index (inclusive, 0-based, must be &gt;= 0 and &lt; columnCount)
     * @param toColumnIndex the ending column index (exclusive, must be &gt; fromColumnIndex and &lt;= columnCount)
     * @param action the action to perform on each element in the sub-region; must not be null
     * @throws IndexOutOfBoundsException if the indices are out of bounds or invalid
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.DoubleConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);
        N.checkArgNotNull(action, "action");

        if (Matrices.isParallelizable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> elementAction = (i, j) -> action.accept(a[i][j]);
            Matrices.forEachIndex(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, elementAction, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final double[] currentRow = a[i];

                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(currentRow[j]);
                }
            }
        }
    }

    /**
     * Prints this matrix to standard output and returns the formatted string.
     * Each row is printed on a separate line with elements separated by commas
     * and enclosed in square brackets.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * String output = matrix.printAndReturn();
     * // Prints to stdout and returns:
     * // [1.0, 2.0, 3.0]
     * // [4.0, 5.0, 6.0]
     * }</pre>
     *
     * @return the formatted string representation of the matrix
     */
    @Override
    public String printAndReturn() {
        if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
            String str = null;

            try {
                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(ARRAY_PRINT_SEPARATOR);
                    }

                    final double[] row = a[i];
                    sb.append('[');

                    for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                        if (j > 0) {
                            sb.append(", ");
                        }

                        sb.append(row[j]);
                    }

                    sb.append(']');
                }

                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     * Returns a hash code value for this matrix.
     * The hash code is computed based on the deep contents of the internal two-dimensional array.
     * Matrices with the same dimensions and element values will have equal hash codes,
     * consistent with the {@link #equals(Object)} method.
     *
     * @return a hash code value for this matrix
     */
    @Override
    public int hashCode() {
        return N.deepHashCode(a);
    }

    /**
     * Compares this matrix to the specified object for equality.
     * Returns {@code true} if the given object is also a DoubleMatrix with the same dimensions
     * and all corresponding elements are equal.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix m1 = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix m2 = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * m1.equals(m2);   // true
     * }</pre>
     *
     * @param obj the object to compare with
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof final DoubleMatrix another) {
            return columnCount == another.columnCount && rowCount == another.rowCount && N.deepEquals(a, another.a);
        }

        return false;
    }

    /**
     * Returns a string representation of this matrix.
     * The format consists of matrix elements in a two-dimensional array format with rows enclosed in brackets.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * System.out.println(matrix.toString());   // [[1.0, 2.0], [3.0, 4.0]]
     * }</pre>
     *
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
