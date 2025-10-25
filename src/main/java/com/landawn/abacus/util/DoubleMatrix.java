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

import java.util.NoSuchElementException;

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.annotation.SuppressFBWarnings;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalDouble;
import com.landawn.abacus.util.stream.DoubleIteratorEx;
import com.landawn.abacus.util.stream.DoubleStream;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

/**
 * A matrix implementation for double primitive values, providing efficient storage and operations
 * for two-dimensional double arrays. This class extends AbstractMatrix and provides specialized
 * methods for double matrix manipulation including mathematical operations, transformations,
 * and element access.
 * 
 * <p>The matrix is backed by a 2D double array and provides methods for:
 * <ul>
 *   <li>Basic matrix operations (add, subtract, multiply)</li>
 *   <li>Transformations (transpose, rotate, flip)</li>
 *   <li>Element access and modification</li>
 *   <li>Streaming operations for rows, columns, and diagonals</li>
 *   <li>Reshaping and extending operations</li>
 *   <li>Type conversion to other numeric matrix types</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>{@code
 * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
 * double value = matrix.get(0, 1); // returns 2.0
 * matrix.transpose(); // returns new matrix with rows and columns swapped
 * }</pre>
 * 
 * @see AbstractMatrix
 * @see IntMatrix
 * @see LongMatrix
 * @see FloatMatrix
 */
public final class DoubleMatrix extends AbstractMatrix<double[], DoubleList, DoubleStream, Stream<DoubleStream>, DoubleMatrix> {

    static final DoubleMatrix EMPTY_DOUBLE_MATRIX = new DoubleMatrix(new double[0][0]);

    /**
     * Constructs a DoubleMatrix from a 2D double array.
     * If the input array is null, an empty matrix (0x0) is created.
     * <p><b>Note:</b> The array is used directly without copying. Modifications to the input array
     * after construction will affect the matrix, and vice versa.</p>
     *
     * @param a the 2D double array to wrap as a matrix. Can be null.
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
     * // matrix.rows() returns 0
     * // matrix.columns() returns 0
     * }</pre>
     *
     * @return an empty double matrix
     */
    public static DoubleMatrix empty() {
        return EMPTY_DOUBLE_MATRIX;
    }

    /**
     * Creates a DoubleMatrix from a 2D double array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * // matrix.get(1, 1) returns 4.0
     * }</pre>
     *
     * @param a the 2D double array to create the matrix from, or null/empty for an empty matrix
     * @return a new DoubleMatrix containing the provided data, or an empty DoubleMatrix if input is null or empty
     */
    public static DoubleMatrix of(final double[]... a) {
        return N.isEmpty(a) ? EMPTY_DOUBLE_MATRIX : new DoubleMatrix(a);
    }

    /**
     * Creates a DoubleMatrix from a 2D int array by converting int values to double.
     *
     * <p>All rows must have the same length as the first row (rectangular array required).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.create(new int[][] {{1, 2}, {3, 4}});
     * // Creates a matrix with values {{1.0, 2.0}, {3.0, 4.0}}
     * // matrix.get(0, 1) returns 2.0
     * }</pre>
     *
     * @param a the 2D int array to convert to a double matrix, or null/empty for an empty matrix
     * @return a new DoubleMatrix with converted values, or an empty DoubleMatrix if input is null or empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths (non-rectangular array)
     */
    public static DoubleMatrix create(final int[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_DOUBLE_MATRIX;
        }

        N.checkArgument(a[0] != null, "First row cannot be null");

        final int cols = a[0].length;

        // Validate all rows have the same length
        for (int i = 1; i < a.length; i++) {
            N.checkArgument(a[i] != null && a[i].length == cols, "All rows must have the same length. Row 0 has length %s but row %s has length %s", cols, i,
                    a[i] == null ? 0 : a[i].length);
        }

        final double[][] c = new double[a.length][cols];

        for (int i = 0, len = a.length; i < len; i++) {
            final int[] aa = a[i];
            final double[] cc = c[i];

            for (int j = 0; j < cols; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Creates a DoubleMatrix from a 2D long array by converting long values to double.
     *
     * <p>All rows must have the same length as the first row (rectangular array required).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.create(new long[][] {{1L, 2L}, {3L, 4L}});
     * // Creates a matrix with values {{1.0, 2.0}, {3.0, 4.0}}
     * // matrix.get(1, 0) returns 3.0
     * }</pre>
     *
     * @param a the 2D long array to convert to a double matrix, or null/empty for an empty matrix
     * @return a new DoubleMatrix with converted values, or an empty DoubleMatrix if input is null or empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths (non-rectangular array)
     */
    public static DoubleMatrix create(final long[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_DOUBLE_MATRIX;
        }

        N.checkArgument(a[0] != null, "First row cannot be null");

        final int cols = a[0].length;

        // Validate all rows have the same length
        for (int i = 1; i < a.length; i++) {
            N.checkArgument(a[i] != null && a[i].length == cols, "All rows must have the same length. Row 0 has length %s but row %s has length %s", cols, i,
                    a[i] == null ? 0 : a[i].length);
        }

        final double[][] c = new double[a.length][cols];

        for (int i = 0, len = a.length; i < len; i++) {
            final long[] aa = a[i];
            final double[] cc = c[i];

            for (int j = 0; j < cols; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Creates a DoubleMatrix from a 2D float array by converting float values to double.
     *
     * <p>All rows must have the same length as the first row (rectangular array required).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.create(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * // Creates a matrix with values {{1.0, 2.0}, {3.0, 4.0}}
     * // matrix.get(1, 1) returns 4.0
     * }</pre>
     *
     * @param a the 2D float array to convert to a double matrix, or null/empty for an empty matrix
     * @return a new DoubleMatrix with converted values, or an empty DoubleMatrix if input is null or empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths (non-rectangular array)
     */
    public static DoubleMatrix create(final float[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_DOUBLE_MATRIX;
        }

        N.checkArgument(a[0] != null, "First row cannot be null");

        final int cols = a[0].length;

        // Validate all rows have the same length
        for (int i = 1; i < a.length; i++) {
            N.checkArgument(a[i] != null && a[i].length == cols, "All rows must have the same length. Row 0 has length %s but row %s has length %s", cols, i,
                    a[i] == null ? 0 : a[i].length);
        }

        final double[][] c = new double[a.length][cols];

        for (int i = 0, len = a.length; i < len; i++) {
            final float[] aa = a[i];
            final double[] cc = c[i];

            for (int j = 0; j < cols; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Creates a 1-row matrix filled with random values between 0.0 (inclusive) and 1.0 (exclusive).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.random(5);
     * // Creates a 1x5 matrix with random double values in range [0.0, 1.0)
     * }</pre>
     *
     * @param len the number of columns (must be non-negative)
     * @return a 1-row matrix filled with random double values
     * @throws IllegalArgumentException if len is negative
     */
    @SuppressWarnings("deprecation")
    public static DoubleMatrix random(final int len) {
        return new DoubleMatrix(new double[][] { DoubleList.random(len).array() });
    }

    /**
     * Creates a 1-row matrix with all elements set to the specified value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.repeat(3.14, 5);
     * // Creates a 1x5 matrix where all elements are 3.14
     * }</pre>
     *
     * @param val the value to repeat
     * @param len the number of columns (must be non-negative)
     * @return a 1-row matrix with all elements set to val
     * @throws IllegalArgumentException if len is negative
     */
    public static DoubleMatrix repeat(final double val, final int len) {
        return new DoubleMatrix(new double[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a square matrix from the specified main diagonal elements (left-upper to right-down).
     * All other elements are set to zero. The resulting matrix has dimensions n×n where n is the length
     * of the diagonal array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.diagonalLU2RD(new double[] {1.0, 2.0, 3.0});
     * // Creates 3x3 matrix:
     * // [[1.0, 0.0, 0.0],
     * //  [0.0, 2.0, 0.0],
     * //  [0.0, 0.0, 3.0]]
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements, or null/empty for an empty matrix
     * @return a square matrix with the specified main diagonal, or an empty matrix if input is null or empty
     */
    public static DoubleMatrix diagonalLU2RD(final double[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square matrix from the specified anti-diagonal elements (right-upper to left-down).
     * All other elements are set to zero. The resulting matrix has dimensions n×n where n is the length
     * of the diagonal array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.diagonalRU2LD(new double[] {1.0, 2.0, 3.0});
     * // Creates 3x3 matrix:
     * // [[0.0, 0.0, 1.0],
     * //  [0.0, 2.0, 0.0],
     * //  [3.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements, or null/empty for an empty matrix
     * @return a square matrix with the specified anti-diagonal, or an empty matrix if input is null or empty
     */
    public static DoubleMatrix diagonalRU2LD(final double[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a square matrix from the specified main diagonal and anti-diagonal elements.
     * All other elements are set to zero. If both arrays are provided, they must have the same length.
     * The resulting matrix has dimensions n×n where n is the length of the non-null/non-empty array
     * (or the maximum length if both are provided).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.diagonal(new double[] {1.0, 4.0}, new double[] {2.0, 3.0});
     * // Creates 2x2 matrix:
     * // [[1.0, 2.0],
     * //  [3.0, 4.0]]
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements (can be null or empty)
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements (can be null or empty)
     * @return a square matrix with the specified diagonals, or an empty matrix if both inputs are null or empty
     * @throws IllegalArgumentException if both arrays are non-empty and have different lengths
     */
    public static DoubleMatrix diagonal(final double[] leftUp2RightDownDiagonal, final double[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        if (N.isEmpty(leftUp2RightDownDiagonal) && N.isEmpty(rightUp2LeftDownDiagonal)) {
            return EMPTY_DOUBLE_MATRIX;
        }

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final double[][] c = new double[len][len];

        if (N.notEmpty(rightUp2LeftDownDiagonal)) {
            for (int i = 0, j = len - 1; i < len; i++, j--) {
                c[i][j] = rightUp2LeftDownDiagonal[i];
            }
        }

        if (N.notEmpty(leftUp2RightDownDiagonal)) {
            for (int i = 0; i < len; i++) {
                c[i][i] = leftUp2RightDownDiagonal[i]; // NOSONAR
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
     */
    public static DoubleMatrix unbox(final Matrix<Double> x) {
        return DoubleMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of the matrix elements, which is always {@code double.class}.
     * 
     * @return {@code double.class}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return double.class;
    }

    /**
     * Returns the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double value = matrix.get(0, 1); // Returns 2.0
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position (i, j)
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public double get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Returns the element at the specified point.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * // Assuming you have a Point implementation
     * // double value = matrix.get(point); // Returns element at point
     * }</pre>
     *
     * @param point the point containing row and column indices
     * @return the element at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public double get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.set(0, 1, 9.0); // Sets element at row 0, column 1 to 9.0
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public void set(final int i, final int j, final double val) {
        a[i][j] = val;
    }

    /**
     * Sets the element at the specified point.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * // Assuming you have a Point implementation
     * // matrix.set(point, 9.0); // Sets element at point
     * }</pre>
     *
     * @param point the point containing row and column indices
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public void set(final Point point, final double val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element above the specified position, if it exists.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * OptionalDouble value = matrix.upOf(1, 0); // Returns OptionalDouble.of(1.0)
     * OptionalDouble empty = matrix.upOf(0, 0); // Returns OptionalDouble.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an Optional containing the element above, or empty if out of bounds
     */
    public OptionalDouble upOf(final int i, final int j) {
        return i == 0 ? OptionalDouble.empty() : OptionalDouble.of(a[i - 1][j]);
    }

    /**
     * Returns the element below the specified position, if it exists.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * OptionalDouble value = matrix.downOf(0, 0); // Returns OptionalDouble.of(3.0)
     * OptionalDouble empty = matrix.downOf(1, 0); // Returns OptionalDouble.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an Optional containing the element below, or empty if out of bounds
     */
    public OptionalDouble downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalDouble.empty() : OptionalDouble.of(a[i + 1][j]);
    }

    /**
     * Returns the element to the left of the specified position, if it exists.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * OptionalDouble value = matrix.leftOf(0, 1); // Returns OptionalDouble.of(1.0)
     * OptionalDouble empty = matrix.leftOf(0, 0); // Returns OptionalDouble.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an Optional containing the element to the left, or empty if out of bounds
     */
    public OptionalDouble leftOf(final int i, final int j) {
        return j == 0 ? OptionalDouble.empty() : OptionalDouble.of(a[i][j - 1]);
    }

    /**
     * Returns the element to the right of the specified position, if it exists.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * OptionalDouble value = matrix.rightOf(0, 0); // Returns OptionalDouble.of(2.0)
     * OptionalDouble empty = matrix.rightOf(0, 1); // Returns OptionalDouble.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an Optional containing the element to the right, or empty if out of bounds
     */
    public OptionalDouble rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalDouble.empty() : OptionalDouble.of(a[i][j + 1]);
    }

    /**
     * Returns a stream of points adjacent to the specified position in 4 directions (up, down, left, right).
     * Only includes points that are within matrix bounds. Null elements are filtered out from the stream.
     * The order of points is: up, right, down, left.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * Stream<Point> adjacent = matrix.adjacent4Points(0, 0);
     * // Returns stream containing Point.of(0, 1) and Point.of(1, 0) (right and down)
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return a stream of adjacent points (0 to 4 points depending on position)
     */
    public Stream<Point> adjacent4Points(final int i, final int j) {
        final Point up = i == 0 ? null : Point.of(i - 1, j);
        final Point right = j == cols - 1 ? null : Point.of(i, j + 1);
        final Point down = i == rows - 1 ? null : Point.of(i + 1, j);
        final Point left = j == 0 ? null : Point.of(i, j - 1);

        return Stream.of(up, right, down, left);
    }

    /**
     * Returns a stream of points adjacent to the specified position in 8 directions (including diagonals).
     * Only includes points that are within matrix bounds. Null elements are filtered out from the stream.
     * The order of points is: left-up, up, right-up, right, right-down, down, left-down, left.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
     * Stream<Point> adjacent = matrix.adjacent8Points(1, 1);
     * // Returns stream containing all 8 surrounding points
     * // For center position (1,1), all 8 neighbors are within bounds
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return a stream of adjacent points including diagonals (0 to 8 points depending on position)
     */
    public Stream<Point> adjacent8Points(final int i, final int j) {
        final Point up = i == 0 ? null : Point.of(i - 1, j);
        final Point right = j == cols - 1 ? null : Point.of(i, j + 1);
        final Point down = i == rows - 1 ? null : Point.of(i + 1, j);
        final Point left = j == 0 ? null : Point.of(i, j - 1);

        final Point leftUp = i > 0 && j > 0 ? Point.of(i - 1, j - 1) : null;
        final Point rightUp = i > 0 && j < cols - 1 ? Point.of(i - 1, j + 1) : null;
        final Point rightDown = i < rows - 1 && j < cols - 1 ? Point.of(i + 1, j + 1) : null;
        final Point leftDown = i < rows - 1 && j > 0 ? Point.of(i + 1, j - 1) : null;

        return Stream.of(leftUp, up, rightUp, right, rightDown, down, leftDown, left);
    }

    /**
     * Returns the specified row as an array.
     *
     * <p><b>Important:</b> This method returns a reference to the internal array, not a copy.
     * Modifications to the returned array will affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * double[] firstRow = matrix.row(0); // Returns [1.0, 2.0, 3.0]
     * firstRow[0] = 99.0; // This modifies the matrix
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return a reference to the internal row array (not a copy)
     * @throws IllegalArgumentException if rowIndex is out of bounds
     */
    public double[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a new array containing a copy of the specified column.
     * Unlike the {@link #row(int)} method, this returns a copy because columns are not
     * stored contiguously in the internal representation.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * double[] firstColumn = matrix.column(0); // Returns [1.0, 4.0]
     * firstColumn[0] = 99.0; // This does NOT modify the matrix
     * }</pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing a copy of the specified column
     * @throws IllegalArgumentException if columnIndex is out of bounds
     */
    public double[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final double[] c = new double[rows];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i][columnIndex];
        }

        return c;
    }

    /**
     * Sets the values of the specified row.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * matrix.setRow(0, new double[] {7.0, 8.0, 9.0}); // First row is now [7.0, 8.0, 9.0]
     * }</pre>
     *
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to set; must have length equal to number of columns
     * @throws IllegalArgumentException if rowIndex is out of bounds or row length does not match column count
     */
    public void setRow(final int rowIndex, final double[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Sets the values of the specified column.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * matrix.setColumn(0, new double[] {7.0, 8.0}); // First column is now [7.0, 8.0]
     * }</pre>
     *
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to set; must have length equal to number of rows
     * @throws IllegalArgumentException if columnIndex is out of bounds or column length does not match row count
     */
    public void setColumn(final int columnIndex, final double[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Applies the specified function to update all elements in the specified row in-place.
     * Each element in the row is transformed by the function, and the row is modified with the returned values.
     * This operation modifies the matrix directly.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.updateRow(0, d -> d * 2); // Row 0 becomes [2.0, 4.0]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param rowIndex the row index to update (0-based, must be within bounds)
     * @param func the function to apply to each element in the row; must not be null
     * @throws E if the function throws an exception
     * @throws IllegalArgumentException if rowIndex is out of bounds
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.DoubleUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsDouble(a[rowIndex][i]);
        }
    }

    /**
     * Applies the specified function to update all elements in the specified column in-place.
     * Each element in the column is transformed by the function, and the column is modified with the returned values.
     * This operation modifies the matrix directly.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 4.0}, {9.0, 16.0}});
     * matrix.updateColumn(1, d -> Math.sqrt(d)); // Column 1 becomes [2.0, 4.0]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param columnIndex the column index to update (0-based, must be within bounds)
     * @param func the function to apply to each element in the column; must not be null
     * @throws E if the function throws an exception
     * @throws IllegalArgumentException if columnIndex is out of bounds
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.DoubleUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsDouble(a[i][columnIndex]);
        }
    }

    /**
     * Returns a copy of the elements on the main diagonal from left-upper to right-down.
     * The matrix must be square (rows == columns) for this operation.
     *
     * <p>This method extracts the main diagonal elements at positions (0,0), (1,1), (2,2), etc.
     * The returned array is a copy; modifications to it will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
     * double[] diagonal = matrix.getLU2RD(); // Returns [1.0, 5.0, 9.0]
     * }</pre>
     *
     * @return a new double array containing a copy of the main diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public double[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final double[] res = new double[rows];

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][i]; // NOSONAR
        }

        return res;
    }

    /**
     * Sets the elements on the main diagonal from left-upper to right-down (main diagonal).
     * The matrix must be square (rows == columns), and the diagonal array must have
     * at least as many elements as the matrix has rows.
     *
     * <p>This method sets the main diagonal elements at positions (0,0), (1,1), (2,2), etc.
     * If the diagonal array is longer than needed, extra elements are ignored.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.setLU2RD(new double[] {9.0, 8.0});
     * // Diagonal is now [9.0, 8.0]
     * }</pre>
     *
     * @param diagonal the new values for the main diagonal; must have length &gt;= rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length &lt; rows
     */
    public void setLU2RD(final double[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates the elements on the main diagonal (left-upper to right-down) using the specified function.
     * The matrix must be square (rows == columns). Each diagonal element is transformed in-place
     * by applying the function to it. This operation modifies the matrix directly.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.updateLU2RD(d -> d * 10);
     * // matrix is now [[10.0, 2.0], [3.0, 40.0]]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the function to apply to each diagonal element; must not be null
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public <E extends Exception> void updateLU2RD(final Throwables.DoubleUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsDouble(a[i][i]);
        }
    }

    /**
     * Returns a copy of the elements on the anti-diagonal from right-upper to left-down.
     * The matrix must be square (rows == columns) for this operation.
     *
     * <p>This method extracts the anti-diagonal (secondary diagonal) elements from
     * top-right to bottom-left, at positions (0,n-1), (1,n-2), (2,n-3), etc.
     * The returned array is a copy; modifications to it will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
     * double[] diagonal = matrix.getRU2LD(); // Returns [3.0, 5.0, 7.0]
     * }</pre>
     *
     * @return a new double array containing a copy of the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public double[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final double[] res = new double[rows];

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][cols - i - 1];
        }

        return res;
    }

    /**
     * Sets the elements on the anti-diagonal from right-upper to left-down (anti-diagonal).
     * The matrix must be square (rows == columns), and the diagonal array must have
     * at least as many elements as the matrix has rows.
     *
     * <p>This method sets the anti-diagonal (secondary diagonal) elements from
     * top-right to bottom-left, at positions (0,n-1), (1,n-2), (2,n-3), etc.
     * If the diagonal array is longer than needed, extra elements are ignored.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.setRU2LD(new double[] {9.0, 8.0});
     * // Anti-diagonal is now [9.0, 8.0]
     * }</pre>
     *
     * @param diagonal the new values for the anti-diagonal; must have length &gt;= rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length &lt; rows
     */
    public void setRU2LD(final double[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = diagonal[i];
        }
    }

    /**
     * Updates the elements on the anti-diagonal (right-upper to left-down) using the specified function.
     * The matrix must be square (rows == columns). Each anti-diagonal element is transformed in-place
     * by applying the function to it. This operation modifies the matrix directly.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.updateRU2LD(d -> d * 10);
     * // matrix is now [[1.0, 20.0], [30.0, 4.0]]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the function to apply to each anti-diagonal element; must not be null
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public <E extends Exception> void updateRU2LD(final Throwables.DoubleUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsDouble(a[i][cols - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix in-place using the specified function.
     * Each element is transformed by the function, and the matrix is modified with the returned values.
     * This operation modifies the matrix directly and may be performed in parallel for large matrices
     * to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.updateAll(d -> d * 2); // Matrix becomes [[2.0, 4.0], [6.0, 8.0]]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the function to apply to each element; must not be null
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.DoubleUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.applyAsDouble(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Updates all elements in the matrix in-place based on their position.
     * The function receives the row and column indices for each element and returns the new value
     * for that position. This operation modifies the matrix directly and may be performed in parallel
     * for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
     * matrix.updateAll((i, j) -> i * 3 + j); // Sets each element to its linear index
     * // Matrix becomes [[0.0, 1.0, 2.0], [3.0, 4.0, 5.0]]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the function that takes row index and column index, then returns the new value for that position; must not be null
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Double, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements that match the predicate with the specified value in-place.
     * Each element is tested by the predicate, and if it matches, it is replaced with the new value.
     * This operation modifies the matrix directly and may be performed in parallel for large matrices
     * to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{-1.0, 2.0}, {-3.0, 4.0}});
     * matrix.replaceIf(d -> d < 0, 0.0); // Replace all negative values with 0
     * // Matrix becomes [[0.0, 2.0], [0.0, 4.0]]
     * }</pre>
     *
     * @param <E> the exception type that the predicate may throw
     * @param predicate the predicate to test each element; must not be null
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.DoublePredicate<E> predicate, final double newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements at positions that match the predicate with the specified value in-place.
     * The predicate receives the row and column indices for each position and determines whether
     * the element at that position should be replaced. This operation modifies the matrix directly
     * and may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.replaceIf((i, j) -> i == j, 0.0); // Set diagonal elements to 0.0
     * // Matrix becomes [[0.0, 2.0], [3.0, 0.0]]
     * }</pre>
     *
     * @param <E> the exception type that the predicate may throw
     * @param predicate the predicate that takes row and column indices and returns true if the element should be replaced; must not be null
     * @param newValue the value to replace at matching positions
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final double newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new DoubleMatrix by applying the specified function to each element of this matrix.
     * The original matrix is not modified. Each element is transformed independently by the function,
     * and the results are collected into a new matrix with the same dimensions. The operation may be
     * performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix squared = matrix.map(d -> d * d);
     * // squared is [[1.0, 4.0], [9.0, 16.0]], original matrix is unchanged
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the mapping function to apply to each element; must not be null
     * @return a new DoubleMatrix with the mapped values (same dimensions as the original)
     * @throws E if the function throws an exception
     */
    public <E extends Exception> DoubleMatrix map(final Throwables.DoubleUnaryOperator<E> func) throws E {
        final double[][] result = new double[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsDouble(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

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
     * @param func the mapping function that converts each double element to an int; must not be null
     * @return a new IntMatrix with the mapped values (same dimensions as the original)
     * @throws E if the function throws an exception
     */
    public <E extends Exception> IntMatrix mapToInt(final Throwables.DoubleToIntFunction<E> func) throws E {
        final int[][] result = new int[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsInt(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

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
     * @param func the mapping function that converts each double element to a long; must not be null
     * @return a new LongMatrix with the mapped values (same dimensions as the original)
     * @throws E if the function throws an exception
     */
    public <E extends Exception> LongMatrix mapToLong(final Throwables.DoubleToLongFunction<E> func) throws E {
        final long[][] result = new long[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsLong(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

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
     * @param func the mapping function that converts each double element to type T; must not be null
     * @param targetElementType the class object representing the target element type (used for array creation); must not be null
     * @return a new Matrix&lt;T&gt; with the mapped values (same dimensions as the original)
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.DoubleFunction<? extends T, E> func, final Class<T> targetElementType) throws E {
        final T[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Fills all elements in the matrix with the specified value in-place.
     * This method modifies the matrix directly.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.fill(0.0); // All elements become 0.0
     * // Matrix is now [[0.0, 0.0], [0.0, 0.0]]
     * }</pre>
     *
     * @param val the value to fill the matrix with
     */
    public void fill(final double val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills the matrix with values from the specified 2D array in-place, starting from position (0,0).
     * Values are copied up to the minimum of the matrix size and the source array size. If the source
     * array is smaller than the matrix, only the overlapping region is filled. If the source array is
     * larger, only the portion that fits is copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[3][3]);
     * matrix.fill(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * // Top-left 2x2 region is filled: [[1.0, 2.0, 0.0], [3.0, 4.0, 0.0], [0.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param b the source array to copy values from (maybe smaller or larger than the matrix)
     */
    public void fill(final double[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from the specified 2D array in-place, starting from a specified position.
     * Values are copied starting from the specified row and column indices. If the source array extends
     * beyond the matrix bounds from the starting position, only the portion that fits is copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[3][3]);
     * matrix.fill(1, 1, new double[][] {{9.0, 8.0}, {7.0, 6.0}});
     * // Result: [[0.0, 0.0, 0.0], [0.0, 9.0, 8.0], [0.0, 7.0, 6.0]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index in this matrix (0-based)
     * @param fromColumnIndex the starting column index in this matrix (0-based)
     * @param b the source array to copy values from
     * @throws IllegalArgumentException if the starting indices are negative or exceed matrix dimensions
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final double[][] b) throws IllegalArgumentException {
        N.checkArgument(fromRowIndex >= 0 && fromRowIndex <= rows, "fromRowIndex(%s) must be between 0 and rows(%s)", fromRowIndex, rows);
        N.checkArgument(fromColumnIndex >= 0 && fromColumnIndex <= cols, "fromColumnIndex(%s) must be between 0 and cols(%s)", fromColumnIndex, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
        }
    }

    /**
     * Returns a copy of this matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix original = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix copy = original.copy();
     * // copy is independent from original
     * }</pre>
     *
     * @return a new matrix that is a copy of this matrix
     */
    @Override
    public DoubleMatrix copy() {
        final double[][] c = new double[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new DoubleMatrix(c);
    }

    /**
     * Creates a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows and is completely independent from the original matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
     * DoubleMatrix partial = matrix.copy(1, 3); // Returns [[3.0, 4.0], [5.0, 6.0]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new DoubleMatrix containing a copy of the specified rows
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @Override
    public DoubleMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final double[][] c = new double[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new DoubleMatrix(c);
    }

    /**
     * Creates a copy of a rectangular region from this matrix.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix sub = matrix.copy(0, 2, 1, 3); // Copy rows 0-1, columns 1-2
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new DoubleMatrix containing the specified region
     * @throws IndexOutOfBoundsException if any index is out of bounds
     */
    @Override
    public DoubleMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex)
            throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final double[][] c = new double[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new DoubleMatrix(c);
    }

    /**
     * Creates a new matrix by extending or truncating rows and columns.
     * If the new dimensions are smaller, the matrix is truncated. If larger, new cells are filled with 0.0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix extended = matrix.extend(3, 3);
     * // Result: [[1.0, 2.0, 0.0], [3.0, 4.0, 0.0], [0.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param newRows the desired number of rows (must be non-negative)
     * @param newCols the desired number of columns (must be non-negative)
     * @return a new DoubleMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public DoubleMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, 0);
    }

    /**
     * Creates a new matrix by extending or truncating rows and columns.
     * New cells are filled with the specified default value.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix extended = matrix.extend(5, 5, -1.0); // Extend to 5x5, fill new cells with -1.0
     * }</pre>
     *
     * @param newRows the desired number of rows
     * @param newCols the desired number of columns
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new DoubleMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public DoubleMatrix extend(final int newRows, final int newCols, final double defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

        // Check for overflow before allocation
        if ((long) newRows * newCols > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Matrix dimensions too large: " + newRows + " x " + newCols);
        }

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            final boolean fillDefaultValue = defaultValueForNewCell != 0;
            final double[][] b = new double[newRows][];

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : new double[newCols];

                if (fillDefaultValue) {
                    if (i >= rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, defaultValueForNewCell);
                    }
                }
            }

            return new DoubleMatrix(b);
        }
    }

    /**
     * Creates a new matrix by extending the current matrix in all four directions.
     * New cells are filled with 0.0. The original matrix content is preserved in its position.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{5.0}});
     * DoubleMatrix extended = matrix.extend(1, 1, 1, 1);
     * // Result: [[0.0, 0.0, 0.0], [0.0, 5.0, 0.0], [0.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param toUp number of rows to add above (must be non-negative)
     * @param toDown number of rows to add below (must be non-negative)
     * @param toLeft number of columns to add to the left (must be non-negative)
     * @param toRight number of columns to add to the right (must be non-negative)
     * @return a new extended DoubleMatrix
     * @throws IllegalArgumentException if any extension parameter is negative
     */
    public DoubleMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, 0);
    }

    /**
     * Creates a new matrix by extending the current matrix in all four directions.
     * New cells are filled with the specified default value.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix extended = matrix.extend(1, 1, 1, 1, -999.0); // Add 1 row/col on each side with -999.0
     * }</pre>
     *
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new extended DoubleMatrix
     * @throws IllegalArgumentException if any extension parameter is negative
     */
    public DoubleMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final double defaultValueForNewCell)
            throws IllegalArgumentException {
        N.checkArgument(toUp >= 0, "The 'toUp' can't be negative %s", toUp);
        N.checkArgument(toDown >= 0, "The 'toDown' can't be negative %s", toDown);
        N.checkArgument(toLeft >= 0, "The 'toLeft' can't be negative %s", toLeft);
        N.checkArgument(toRight >= 0, "The 'toRight' can't be negative %s", toRight);

        if (toUp == 0 && toDown == 0 && toLeft == 0 && toRight == 0) {
            return copy();
        } else {
            if ((long) toUp + rows + toDown > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Result would have too many rows: " + toUp + " + " + rows + " + " + toDown);
            }

            if ((long) toLeft + cols + toRight > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Result would have too many columns: " + toLeft + " + " + cols + " + " + toRight);
            }

            final int newRows = toUp + rows + toDown;
            final int newCols = toLeft + cols + toRight;
            final boolean fillDefaultValue = defaultValueForNewCell != 0;
            final double[][] b = new double[newRows][newCols];

            for (int i = 0; i < newRows; i++) {
                if (i >= toUp && i < toUp + rows) {
                    N.copy(a[i - toUp], 0, b[i], toLeft, cols);
                }

                if (fillDefaultValue) {
                    if (i < toUp || i >= toUp + rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        if (toLeft > 0) {
                            N.fill(b[i], 0, toLeft, defaultValueForNewCell);
                        }

                        if (toRight > 0) {
                            N.fill(b[i], cols + toLeft, newCols, defaultValueForNewCell);
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
     * matrix.reverseH(); // matrix is now [[3.0, 2.0, 1.0]]
     * }</pre>
     *
     * @see #flipH()
     */
    public void reverseH() {
        for (int i = 0; i < rows; i++) {
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
     * matrix.reverseV(); // matrix is now [[3.0], [2.0], [1.0]]
     * }</pre>
     *
     * @see #flipV()
     */
    public void reverseV() {
        for (int j = 0; j < cols; j++) {
            double tmp = 0;
            for (int l = 0, h = rows - 1; l < h;) {
                tmp = a[l][j];
                a[l++][j] = a[h][j];
                a[h--][j] = tmp;
            }
        }
    }

    /**
     * Creates a new matrix that is horizontally flipped (each row reversed).
     * The original matrix is not modified. This is a non-mutating version of {@link #reverseH()}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}});
     * DoubleMatrix flipped = matrix.flipH(); // returns [[3.0, 2.0, 1.0]], original unchanged
     * }</pre>
     *
     * @return a new DoubleMatrix with each row reversed
     * @see #reverseH()
     */
    public DoubleMatrix flipH() {
        final DoubleMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * Creates a new matrix that is vertically flipped (rows reversed).
     * The original matrix is not modified. This is a non-mutating version of {@link #reverseV()}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0}, {2.0}, {3.0}});
     * DoubleMatrix flipped = matrix.flipV(); // returns [[3.0], [2.0], [1.0]], original unchanged
     * }</pre>
     *
     * @return a new DoubleMatrix with rows reversed
     * @see #reverseV()
     */
    public DoubleMatrix flipV() {
        final DoubleMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * Returns a new matrix rotated 90 degrees clockwise.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix rotated = matrix.rotate90();
     * // rotated is {{3.0, 1.0}, {4.0, 2.0}}
     * }</pre>
     *
     * @return a new matrix rotated 90 degrees clockwise
     */
    @Override
    public DoubleMatrix rotate90() {
        final double[][] c = new double[cols][rows];

        if (rows <= cols) {
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    c[i][j] = a[rows - j - 1][i];
                }
            }
        } else {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    c[i][j] = a[rows - j - 1][i];
                }
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Returns a new matrix rotated 180 degrees.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix rotated = matrix.rotate180();
     * // rotated is {{4.0, 3.0}, {2.0, 1.0}}
     * }</pre>
     *
     * @return a new matrix rotated 180 degrees
     */
    @Override
    public DoubleMatrix rotate180() {
        final double[][] c = new double[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new DoubleMatrix(c);
    }

    /**
     * Returns a new matrix rotated 270 degrees clockwise (or 90 degrees counter-clockwise).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix rotated = matrix.rotate270();
     * // rotated is {{2.0, 4.0}, {1.0, 3.0}}
     * }</pre>
     *
     * @return a new matrix rotated 270 degrees clockwise
     */
    @Override
    public DoubleMatrix rotate270() {
        final double[][] c = new double[cols][rows];

        if (rows <= cols) {
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    c[i][j] = a[j][cols - i - 1];
                }
            }
        } else {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    c[i][j] = a[j][cols - i - 1];
                }
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Returns a new matrix that is the transpose of this matrix.
     * The transpose swaps rows and columns.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * DoubleMatrix transposed = matrix.transpose();
     * // transposed is {{1.0, 4.0}, {2.0, 5.0}, {3.0, 6.0}}
     * }</pre>
     *
     * @return a new matrix that is the transpose of this matrix
     */
    @Override
    public DoubleMatrix transpose() {
        final double[][] c = new double[cols][rows];

        if (rows <= cols) {
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    c[i][j] = a[j][i];
                }
            }
        } else {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
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
     * <p>If the new shape has fewer total elements than the original, excess elements are truncated.
     * If the new shape has more total elements, the additional positions are filled with zeros.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * DoubleMatrix reshaped = matrix.reshape(3, 2); // Becomes [[1.0, 2.0], [3.0, 4.0], [5.0, 6.0]]
     * DoubleMatrix extended = matrix.reshape(2, 4); // Becomes [[1.0, 2.0, 3.0, 4.0], [5.0, 6.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param newRows the number of rows in the reshaped matrix
     * @param newCols the number of columns in the reshaped matrix
     * @return a new DoubleMatrix with the specified shape containing this matrix's elements
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public DoubleMatrix reshape(final int newRows, final int newCols) {
        final double[][] c = new double[newRows][newCols];

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new DoubleMatrix(c);
        }

        final int rowLen = (int) N.min(newRows, count % newCols == 0 ? count / newCols : count / newCols + 1);

        if (a.length == 1) {
            for (int i = 0; i < rowLen; i++) {
                N.copy(a[0], i * newCols, c[i], 0, (int) N.min(newCols, count - (long) i * newCols));
            }
        } else {
            long cnt = 0;

            for (int i = 0; i < rowLen; i++) {
                for (int j = 0, col = (int) N.min(newCols, count - (long) i * newCols); j < col; j++, cnt++) {
                    c[i][j] = a[(int) (cnt / cols)][(int) (cnt % cols)];
                }
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Repeats elements of the matrix in both row and column directions.
     * Each element is repeated {@code rowRepeats} times vertically and {@code colRepeats} times horizontally.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix repeated = matrix.repelem(2, 3);
     * // Result: [[1.0, 1.0, 1.0, 2.0, 2.0, 2.0],
     * //          [1.0, 1.0, 1.0, 2.0, 2.0, 2.0],
     * //          [3.0, 3.0, 3.0, 4.0, 4.0, 4.0],
     * //          [3.0, 3.0, 3.0, 4.0, 4.0, 4.0]]
     * }</pre>
     *
     * @param rowRepeats the number of times to repeat each element in the row direction
     * @param colRepeats the number of times to repeat each element in the column direction
     * @return a new matrix with repeated elements
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see IntMatrix#repelem(int, int)
     */
    @Override
    public DoubleMatrix repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many rows: " + rows + " * " + rowRepeats);
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many columns: " + cols + " * " + colRepeats);
        }

        final double[][] c = new double[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            final double[] aa = a[i];
            final double[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                N.copy(Array.repeat(aa[j], colRepeats), 0, fr, j * colRepeats, colRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new DoubleMatrix(c);
    }

    /**
     * Repeats the entire matrix in both row and column directions.
     * The matrix is tiled {@code rowRepeats} times vertically and {@code colRepeats} times horizontally.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix tiled = matrix.repmat(2, 3);
     * // Result: [[1.0, 2.0, 1.0, 2.0, 1.0, 2.0],
     * //          [3.0, 4.0, 3.0, 4.0, 3.0, 4.0],
     * //          [1.0, 2.0, 1.0, 2.0, 1.0, 2.0],
     * //          [3.0, 4.0, 3.0, 4.0, 3.0, 4.0]]
     * }</pre>
     *
     * @param rowRepeats the number of times to repeat the matrix in the row direction
     * @param colRepeats the number of times to repeat the matrix in the column direction
     * @return a new matrix with the tiled pattern
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see IntMatrix#repmat(int, int)
     */
    @Override
    public DoubleMatrix repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many rows: " + rows + " * " + rowRepeats);
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many columns: " + cols + " * " + colRepeats);
        }

        final double[][] c = new double[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colRepeats; j++) {
                N.copy(a[i], 0, c[i], j * cols, cols);
            }
        }

        for (int i = 1; i < rowRepeats; i++) {
            for (int j = 0; j < rows; j++) {
                N.copy(c[j], 0, c[i * rows + j], 0, c[j].length);
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
     * DoubleList list = matrix.flatten(); // Returns DoubleList containing [1.0, 2.0, 3.0, 4.0]
     * }</pre>
     *
     * @return a DoubleList containing all elements in row-major order
     * @throws IllegalStateException if the matrix is too large to flatten (more than Integer.MAX_VALUE elements)
     */
    @Override
    public DoubleList flatten() {
        // Check for overflow before allocation
        if ((long) rows * cols > Integer.MAX_VALUE) {
            throw new IllegalStateException("Matrix too large to flatten: " + rows + " x " + cols);
        }

        final double[] c = new double[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return DoubleList.of(c);
    }

    /**
     * Applies an operation to each row array of the underlying 2D array in-place.
     * The operation receives a reference to each row as a double array and can modify it directly.
     *
     * <p><b>Note:</b> This provides direct access to the internal row arrays, so modifications
     * will affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{3.0, 1.0, 2.0}, {6.0, 4.0, 5.0}});
     * matrix.flatOp(row -> java.util.Arrays.sort(row)); // Sort each row in-place
     * // Matrix becomes [[1.0, 2.0, 3.0], [4.0, 5.0, 6.0]]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown
     * @param op the operation to apply to each row array
     * @throws E if the operation throws an exception
     * @see Arrays#flatOp(double[][], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super double[], E> op) throws E {
        Arrays.flatOp(a, op);
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
     * DoubleMatrix stacked = a.vstack(b);
     * // Result: [[1.0, 2.0], [3.0, 4.0], [5.0, 6.0]]
     * }</pre>
     *
     * @param b the matrix to stack below this matrix
     * @return a new matrix with combined rows
     * @throws IllegalArgumentException if the matrices have different number of columns
     * @see IntMatrix#vstack(IntMatrix)
     */
    public DoubleMatrix vstack(final DoubleMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final double[][] c = new double[rows + b.rows][];
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
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
     * DoubleMatrix stacked = a.hstack(b);
     * // Result: [[1.0, 2.0, 5.0], [3.0, 4.0, 6.0]]
     * }</pre>
     *
     * @param b the matrix to stack to the right of this matrix
     * @return a new matrix with combined columns
     * @throws IllegalArgumentException if the matrices have different number of rows
     * @see IntMatrix#hstack(IntMatrix)
     */
    public DoubleMatrix hstack(final DoubleMatrix b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final double[][] c = new double[rows][cols + b.cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c[i], 0, cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
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
     * DoubleMatrix sum = a.add(b); // [[6.0, 8.0], [10.0, 12.0]]
     * }</pre>
     *
     * @param b the matrix to add to this matrix
     * @return a new matrix containing the element-wise sum
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public DoubleMatrix add(final DoubleMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't add Matrixes with different shape");

        final double[][] ba = b.a;
        final double[][] result = new double[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (a[i][j] + ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

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
     * DoubleMatrix diff = a.subtract(b); // [[4.0, 4.0], [4.0, 4.0]]
     * }</pre>
     *
     * @param b the matrix to subtract from this matrix
     * @return a new matrix containing the element-wise difference
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public DoubleMatrix subtract(final DoubleMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't subtract Matrixes with different shape");

        final double[][] ba = b.a;
        final double[][] result = new double[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (a[i][j] - ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * Performs matrix multiplication with another matrix.
     * The number of columns in this matrix must equal the number of rows in the other matrix.
     * Results in a matrix of dimensions (this.rows × b.cols).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix a = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * DoubleMatrix b = DoubleMatrix.of(new double[][] {{5.0, 6.0}, {7.0, 8.0}});
     * DoubleMatrix product = a.multiply(b); // [[19.0, 22.0], [43.0, 50.0]]
     * }</pre>
     *
     * @param b the matrix to multiply with this matrix
     * @return a new matrix containing the matrix product
     * @throws IllegalArgumentException if the matrix dimensions are incompatible for multiplication
     */
    public DoubleMatrix multiply(final DoubleMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.rows, "Illegal matrix dimensions");

        final double[][] ba = b.a;
        final double[][] result = new double[rows][b.cols];
        final Throwables.IntTriConsumer<RuntimeException> cmd = (i, j, k) -> result[i][j] += a[i][k] * ba[k][j];

        Matrixes.multiply(this, b, cmd);

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
     */
    public Matrix<Double> boxed() {
        final Double[][] c = new Double[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final double[] aa = a[i];
                final Double[] cc = c[i];

                for (int j = 0; j < cols; j++) {
                    cc[j] = aa[j]; // NOSONAR
                }
            }
        } else {
            for (int j = 0; j < cols; j++) {
                for (int i = 0; i < rows; i++) {
                    c[i][j] = a[i][j];
                }
            }
        }

        return new Matrix<>(c);
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
     * DoubleMatrix max = a.zipWith(b, Math::max); // [[5.0, 6.0], [7.0, 8.0]]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown
     * @param matrixB the matrix to combine with this matrix; must have the same dimensions and must not be null
     * @param zipFunction the binary operation to apply to corresponding elements; must not be null
     * @return a new matrix with the operation applied element-wise (same dimensions as the input matrices)
     * @throws IllegalArgumentException if the matrices have different dimensions
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> DoubleMatrix zipWith(final DoubleMatrix matrixB, final Throwables.DoubleBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Can't zip two or more matrices which don't have same shape");

        final double[][] b = matrixB.a;
        final double[][] result = new double[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsDouble(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

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
     * DoubleMatrix result = a.zipWith(b, c, (x, y, z) -> x + y * z); // [[16.0, 26.0]]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown
     * @param matrixB the second matrix to combine; must have the same dimensions and must not be null
     * @param matrixC the third matrix to combine; must have the same dimensions and must not be null
     * @param zipFunction the ternary operation to apply to corresponding elements; must not be null
     * @return a new matrix with the operation applied element-wise (same dimensions as the input matrices)
     * @throws IllegalArgumentException if the matrices have different dimensions
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> DoubleMatrix zipWith(final DoubleMatrix matrixB, final DoubleMatrix matrixC,
            final Throwables.DoubleTernaryOperator<E> zipFunction) throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Can't zip two or more matrices which don't have same shape");

        final double[][] b = matrixB.a;
        final double[][] c = matrixC.a;
        final double[][] result = new double[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsDouble(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * Creates a stream of all elements in the matrix in row-major (horizontal) order.
     * Elements are streamed from left to right within each row, then top to bottom across rows.
     * This is equivalent to calling {@code streamH(0, rows)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double sum = matrix.streamH().sum(); // Returns 10.0 (1.0 + 2.0 + 3.0 + 4.0)
     * }</pre>
     *
     * @return a DoubleStream of all matrix elements in row-major order
     */
    @Override
    public DoubleStream streamH() {
        return streamH(0, rows);
    }

    /**
     * Creates a stream of elements from the main diagonal (left-upper to right-down).
     * The matrix must be square (same number of rows and columns).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double[] diagonal = matrix.streamLU2RD().toArray(); // [1.0, 4.0]
     * }</pre>
     *
     * @return a DoubleStream of diagonal elements from top-left to bottom-right
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public DoubleStream streamLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return DoubleStream.empty();
        }

        return DoubleStream.of(new DoubleIteratorEx() {
            private final int toIndex = rows;
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
     * Creates a stream of elements from the anti-diagonal (right-upper to left-down).
     * The matrix must be square (same number of rows and columns).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double[] antiDiagonal = matrix.streamRU2LD().toArray(); // [2.0, 3.0]
     * }</pre>
     *
     * @return a DoubleStream of diagonal elements from top-right to bottom-left
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public DoubleStream streamRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return DoubleStream.empty();
        }

        return DoubleStream.of(new DoubleIteratorEx() {
            private final int toIndex = rows;
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

                return a[cursor][cols - ++cursor];
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
     * Creates a stream of elements from a single row in the matrix.
     * This is equivalent to calling {@code streamH(rowIndex, rowIndex + 1)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double[] row1 = matrix.streamH(1).toArray(); // Returns [3.0, 4.0]
     * }</pre>
     *
     * @param rowIndex the index of the row to stream (0-based)
     * @return a DoubleStream of elements in the specified row, from left to right
     * @throws IndexOutOfBoundsException if the row index is out of bounds
     */
    @Override
    public DoubleStream streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * Creates a stream of elements from a range of rows in row-major order.
     * Elements are streamed from left to right within each row, then top to bottom across rows.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
     * double[] elements = matrix.streamH(1, 3).toArray(); // [3.0, 4.0, 5.0, 6.0]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a DoubleStream of elements in the specified row range
     * @throws IndexOutOfBoundsException if the row indices are out of bounds
     */
    @Override
    public DoubleStream streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

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

                if (j >= cols) {
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

                if (n >= (long) (toRowIndex - i) * cols - j) {
                    i = toRowIndex;
                    j = 0;
                } else {
                    i += (int) ((n + j) / cols);
                    j += (int) ((n + j) % cols);
                }
            }

            @Override
            public long count() {
                return (long) (toRowIndex - i) * cols - j;
            }

            @Override
            public double[] toArray() {
                final int len = (int) count();
                final double[] c = new double[len];

                for (int k = 0; k < len; k++) {
                    c[k] = a[i][j++];

                    if (j >= cols) {
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
     * double[] columnMajor = matrix.streamV().toArray(); // [1.0, 3.0, 2.0, 4.0]
     * }</pre>
     *
     * @return a DoubleStream of all matrix elements in column-major order
     */
    @Override
    @Beta
    public DoubleStream streamV() {
        return streamV(0, cols);
    }

    /**
     * Creates a stream of elements from a single column in the matrix.
     * This is equivalent to calling {@code streamV(columnIndex, columnIndex + 1)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * double[] column1 = matrix.streamV(1).toArray(); // Returns [2.0, 4.0]
     * }</pre>
     *
     * @param columnIndex the index of the column to stream (0-based)
     * @return a DoubleStream of elements in the specified column, from top to bottom
     * @throws IndexOutOfBoundsException if the column index is out of bounds
     */
    @Override
    public DoubleStream streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * Creates a stream of elements from a range of columns in column-major order.
     * Elements are streamed from top to bottom within each column, then left to right across columns.
     * This method is marked as Beta and may change in future versions.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * double[] elements = matrix.streamV(1, 3).toArray(); // [2.0, 5.0, 3.0, 6.0]
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a DoubleStream of elements in the specified column range
     * @throws IndexOutOfBoundsException if the column indices are out of bounds
     */
    @Override
    @Beta
    public DoubleStream streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

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

                if (i >= rows) {
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

                if (n >= (long) (toColumnIndex - j) * DoubleMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    final int offset = (int) (n + i);
                    i = offset % DoubleMatrix.this.rows;
                    j += offset / DoubleMatrix.this.rows;
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public double[] toArray() {
                final int len = (int) count();
                final double[] c = new double[len];

                for (int k = 0; k < len; k++) {
                    c[k] = a[i++][j];

                    if (i >= rows) {
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
     * This is equivalent to calling {@code streamR(0, rows)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.streamR().forEach(row -> System.out.println(row.toList()));
     * // Prints: [1.0, 2.0]
     * //         [3.0, 4.0]
     * }</pre>
     *
     * @return a Stream of DoubleStreams, one for each row in the matrix
     */
    @Override
    public Stream<DoubleStream> streamR() {
        return streamR(0, rows);
    }

    /**
     * Creates a stream of streams for a range of rows.
     * Each inner stream represents a complete row of the matrix.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
     * List<double[]> rows = matrix.streamR(1, 3)
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
    public Stream<DoubleStream> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return Stream.empty();
        }

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
     * This is equivalent to calling {@code streamC(0, cols)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.streamC().forEach(col -> System.out.println(col.toList()));
     * // Prints: [1.0, 3.0]
     * //         [2.0, 4.0]
     * }</pre>
     *
     * @return a Stream of DoubleStreams, one for each column in the matrix
     */
    @Override
    @Beta
    public Stream<DoubleStream> streamC() {
        return streamC(0, cols);
    }

    /**
     * Creates a stream of streams for a range of columns.
     * Each inner stream represents a complete column of the matrix.
     * This method is marked as Beta and may change in future versions.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * List<double[]> columns = matrix.streamC(1, 3)
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
    public Stream<DoubleStream> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

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
                    private final int toIndex2 = rows;
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
     * Applies the specified action to each element of the matrix.
     * The action is performed on all elements in the matrix. For large matrices, the operation may be
     * parallelized to improve performance, so the order of execution is not guaranteed. This method
     * does not modify the matrix unless the action itself modifies external state.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.forEach(value -> System.out.print(value + " "));
     * // Prints all values (order may vary): 1.0 2.0 3.0 4.0
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown
     * @param action the action to perform on each element; must not be null
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> action) throws E {
        forEach(0, rows, 0, cols, action);
    }

    /**
     * Applies the specified action to each element in a sub-region of the matrix.
     * The action is performed on elements within the specified row and column ranges.
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
     * @param <E> the type of exception that may be thrown
     * @param fromRowIndex the starting row index (inclusive, 0-based, must be &gt;= 0 and &lt; rows)
     * @param toRowIndex the ending row index (exclusive, must be &gt; fromRowIndex and &lt;= rows)
     * @param fromColumnIndex the starting column index (inclusive, 0-based, must be &gt;= 0 and &lt; cols)
     * @param toColumnIndex the ending column index (exclusive, must be &gt; fromColumnIndex and &lt;= cols)
     * @param action the action to perform on each element in the sub-region; must not be null
     * @throws IndexOutOfBoundsException if the indices are out of bounds or invalid
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.DoubleConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final double[] aa = a[i];

                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(aa[j]);
                }
            }
        }
    }

    /**
     * Prints the matrix to standard output in a formatted manner.
     * Each row is printed on a separate line with elements separated by commas
     * and enclosed in square brackets.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
     * matrix.println();
     * // Output:
     * // [1.0, 2.0, 3.0]
     * // [4.0, 5.0, 6.0]
     * }</pre>
     * 
     * @return the formatted string representation of the matrix
     */
    @Override
    public String println() {
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
     * The hash code is computed based on the deep contents of the internal 2D array.
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
     * m1.equals(m2); // true
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
            return cols == another.cols && rows == another.rows && N.deepEquals(a, another.a);
        }

        return false;
    }

    /**
     * Returns a string representation of this matrix.
     * The format consists of matrix elements in a 2D array format with rows enclosed in brackets.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * System.out.println(matrix.toString()); // [[1.0, 2.0], [3.0, 4.0]]
     * }</pre>
     *
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
