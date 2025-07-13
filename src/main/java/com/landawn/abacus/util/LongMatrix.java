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
import com.landawn.abacus.util.u.OptionalLong;
import com.landawn.abacus.util.stream.LongIteratorEx;
import com.landawn.abacus.util.stream.LongStream;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

/**
 * A matrix implementation for long primitive values, providing efficient storage and operations
 * for two-dimensional long arrays. This class extends AbstractMatrix and provides specialized
 * methods for long matrix manipulation including mathematical operations, transformations,
 * and element access.
 * 
 * <p>The matrix is internally represented as a 2D long array and supports various
 * matrix operations including arithmetic operations, transformations, and element-wise operations.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * // Create a 3x3 matrix
 * long[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
 * LongMatrix matrix = LongMatrix.of(data);
 * 
 * // Access elements
 * long value = matrix.get(1, 2); // Gets element at row 1, column 2
 * 
 * // Perform operations
 * LongMatrix transposed = matrix.transpose();
 * LongMatrix doubled = matrix.map(x -> x * 2);
 * }</pre>
 * 
 * @see IntMatrix
 * @see DoubleMatrix
 * @see Matrix
 */
public final class LongMatrix extends AbstractMatrix<long[], LongList, LongStream, Stream<LongStream>, LongMatrix> {

    static final LongMatrix EMPTY_LONG_MATRIX = new LongMatrix(new long[0][0]);

    /**
     * Constructs a LongMatrix with the specified 2D long array.
     * The array is used directly without copying, so modifications to the array
     * will be reflected in the matrix.
     * 
     * @param a the 2D long array representing the matrix data. If null, an empty matrix is created.
     */
    public LongMatrix(final long[][] a) {
        super(a == null ? new long[0][0] : a);
    }

    /**
     * Returns an empty LongMatrix with zero rows and columns.
     * This method returns a singleton instance for memory efficiency.
     * 
     * @return an empty LongMatrix instance
     */
    public static LongMatrix empty() {
        return EMPTY_LONG_MATRIX;
    }

    /**
     * Creates a LongMatrix from the specified 2D long array.
     * This is the preferred way to create a LongMatrix instance.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * }</pre>
     * 
     * @param a the 2D long array. Each inner array represents a row.
     * @return a new LongMatrix containing the specified data, or an empty matrix if the array is null or empty
     */
    public static LongMatrix of(final long[]... a) {
        return N.isEmpty(a) ? EMPTY_LONG_MATRIX : new LongMatrix(a);
    }

    /**
     * Creates a LongMatrix from a 2D int array by converting all int values to long.
     * This is useful when working with int data that needs to be promoted to long.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * int[][] intData = {{1, 2}, {3, 4}};
     * LongMatrix matrix = LongMatrix.create(intData);
     * }</pre>
     * 
     * @param a the 2D int array to convert
     * @return a new LongMatrix with the converted values, or an empty matrix if the input is null or empty
     */
    public static LongMatrix create(final int[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_LONG_MATRIX;
        }

        final long[][] c = new long[a.length][a[0].length];

        for (int i = 0, len = a.length; i < len; i++) {
            final int[] aa = a[i];
            final long[] cc = c[i];

            for (int j = 0, col = a[0].length; j < col; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new LongMatrix(c);
    }

    /**
     * Creates a 1-row LongMatrix with random long values.
     * The random values are generated using the default random number generator.
     * 
     * @param len the number of columns (length of the row)
     * @return a new 1×len LongMatrix with random values
     */
    @SuppressWarnings("deprecation")
    public static LongMatrix random(final int len) {
        return new LongMatrix(new long[][] { LongList.random(len).array() });
    }

    /**
     * Creates a 1-row LongMatrix with all elements set to the specified value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.repeat(5L, 10); // Creates a 1×10 matrix filled with 5
     * }</pre>
     * 
     * @param val the value to fill the matrix with
     * @param len the number of columns
     * @return a new 1×len LongMatrix with all elements set to val
     */
    public static LongMatrix repeat(final long val, final int len) {
        return new LongMatrix(new long[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a 1-row LongMatrix with values from startInclusive to endExclusive.
     * The values are generated with a step of 1.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.range(0L, 5L); // Creates [[0, 1, 2, 3, 4]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @return a new 1×n LongMatrix where n = endExclusive - startInclusive
     */
    public static LongMatrix range(final long startInclusive, final long endExclusive) {
        return new LongMatrix(new long[][] { Array.range(startInclusive, endExclusive) });
    }

    /**
     * Creates a 1-row LongMatrix with values from startInclusive to endExclusive with the specified step.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.range(0L, 10L, 2L); // Creates [[0, 2, 4, 6, 8]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @param by the step size
     * @return a new 1×n LongMatrix with values incremented by the step size
     */
    public static LongMatrix range(final long startInclusive, final long endExclusive, final long by) {
        return new LongMatrix(new long[][] { Array.range(startInclusive, endExclusive, by) });
    }

    /**
     * Creates a 1-row LongMatrix with values from startInclusive to endInclusive.
     * This method includes the end value, unlike {@link #range(long, long)}.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.rangeClosed(0L, 4L); // Creates [[0, 1, 2, 3, 4]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive)
     * @return a new 1×n LongMatrix where n = endInclusive - startInclusive + 1
     */
    public static LongMatrix rangeClosed(final long startInclusive, final long endInclusive) {
        return new LongMatrix(new long[][] { Array.rangeClosed(startInclusive, endInclusive) });
    }

    /**
     * Creates a 1-row LongMatrix with values from startInclusive to endInclusive with the specified step.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.rangeClosed(0L, 9L, 2L); // Creates [[0, 2, 4, 6, 8]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive)
     * @param by the step size
     * @return a new 1×n LongMatrix with values incremented by the step size
     */
    public static LongMatrix rangeClosed(final long startInclusive, final long endInclusive, final long by) {
        return new LongMatrix(new long[][] { Array.rangeClosed(startInclusive, endInclusive, by) });
    }

    /**
     * Creates a square diagonal matrix with the specified values on the main diagonal (left-up to right-down).
     * All other elements are set to 0.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.diagonalLU2RD(new long[]{1, 2, 3});
     * // Creates:
     * // [[1, 0, 0],
     * //  [0, 2, 0],
     * //  [0, 0, 3]]
     * }</pre>
     * 
     * @param leftUp2RightDownDiagonal the values for the main diagonal
     * @return a new n×n diagonal matrix where n is the length of the input array
     */
    public static LongMatrix diagonalLU2RD(final long[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square diagonal matrix with the specified values on the anti-diagonal (right-up to left-down).
     * All other elements are set to 0.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.diagonalRU2LD(new long[]{1, 2, 3});
     * // Creates:
     * // [[0, 0, 1],
     * //  [0, 2, 0],
     * //  [3, 0, 0]]
     * }</pre>
     * 
     * @param rightUp2LeftDownDiagonal the values for the anti-diagonal
     * @return a new n×n diagonal matrix where n is the length of the input array
     */
    public static LongMatrix diagonalRU2LD(final long[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a square matrix with specified values on both diagonals.
     * This method can create matrices with values on the main diagonal, anti-diagonal, or both.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.diagonal(new long[]{1, 2, 3}, new long[]{4, 5, 6});
     * // Creates:
     * // [[1, 0, 4],
     * //  [0, 2, 0],
     * //  [6, 0, 3]]
     * }</pre>
     * 
     * @param leftUp2RightDownDiagonal values for the main diagonal (can be null)
     * @param rightUp2LeftDownDiagonal values for the anti-diagonal (can be null)
     * @return a new n×n matrix with the specified diagonal values
     * @throws IllegalArgumentException if both arrays are non-null and have different lengths
     */
    public static LongMatrix diagonal(final long[] leftUp2RightDownDiagonal, final long[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        if (N.isEmpty(leftUp2RightDownDiagonal) && N.isEmpty(rightUp2LeftDownDiagonal)) {
            return EMPTY_LONG_MATRIX;
        }

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final long[][] c = new long[len][len];

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

        return new LongMatrix(c);
    }

    /**
     * Converts a boxed Long matrix to a primitive LongMatrix.
     * This method unboxes all Long values to primitive long values.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Long> boxedMatrix = Matrix.of(new Long[][]{{1L, 2L}, {3L, 4L}});
     * LongMatrix primitiveMatrix = LongMatrix.unbox(boxedMatrix);
     * }</pre>
     * 
     * @param x the boxed Long matrix to convert
     * @return a new LongMatrix with unboxed values
     */
    public static LongMatrix unbox(final Matrix<Long> x) {
        return LongMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of this matrix, which is long.class.
     * 
     * @return long.class
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return long.class;
    }

    /**
     * Gets the element at the specified row and column.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * long value = matrix.get(1, 0); // Returns 3
     * }</pre>
     * 
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position (i, j)
     * @throws ArrayIndexOutOfBoundsException if the indices are out of bounds
     */
    public long get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Gets the element at the specified point.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Point p = Point.of(1, 0);
     * long value = matrix.get(p); // Gets element at row 1, column 0
     * }</pre>
     * 
     * @param point the point containing row and column indices
     * @return the element at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public long get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.set(1, 0, 10L); // Sets element at row 1, column 0 to 10
     * }</pre>
     * 
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if the indices are out of bounds
     */
    public void set(final int i, final int j, final long val) {
        a[i][j] = val;
    }

    /**
     * Sets the element at the specified point.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Point p = Point.of(1, 0);
     * matrix.set(p, 10L); // Sets element at row 1, column 0 to 10
     * }</pre>
     * 
     * @param point the point containing row and column indices
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public void set(final Point point, final long val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Gets the element above the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalLong above = matrix.upOf(1, 0); // Gets element at (0, 0) if it exists
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalLong containing the element above, or empty if at the top row
     */
    public OptionalLong upOf(final int i, final int j) {
        return i == 0 ? OptionalLong.empty() : OptionalLong.of(a[i - 1][j]);
    }

    /**
     * Gets the element below the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalLong below = matrix.downOf(0, 0); // Gets element at (1, 0) if it exists
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalLong containing the element below, or empty if at the bottom row
     */
    public OptionalLong downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalLong.empty() : OptionalLong.of(a[i + 1][j]);
    }

    /**
     * Gets the element to the left of the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalLong left = matrix.leftOf(0, 1); // Gets element at (0, 0) if it exists
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalLong containing the element to the left, or empty if at the leftmost column
     */
    public OptionalLong leftOf(final int i, final int j) {
        return j == 0 ? OptionalLong.empty() : OptionalLong.of(a[i][j - 1]);
    }

    /**
     * Gets the element to the right of the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalLong right = matrix.rightOf(0, 0); // Gets element at (0, 1) if it exists
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalLong containing the element to the right, or empty if at the rightmost column
     */
    public OptionalLong rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalLong.empty() : OptionalLong.of(a[i][j + 1]);
    }

    /**
     * Returns a stream of the four adjacent points (up, right, down, left) of the specified position.
     * Points that would be outside the matrix bounds are returned as null.
     *
     * @param i the row index
     * @param j the column index
     * @return a stream of Points in order: up, right, down, left (null for non-existent positions)
     */
    public Stream<Point> adjacent4Points(final int i, final int j) {
        final Point up = i == 0 ? null : Point.of(i - 1, j);
        final Point right = j == cols - 1 ? null : Point.of(i, j + 1);
        final Point down = i == rows - 1 ? null : Point.of(i + 1, j);
        final Point left = j == 0 ? null : Point.of(i, j - 1);

        return Stream.of(up, right, down, left);
    }

    /**
     * Returns a stream of the eight adjacent points of the specified position.
     * Points are returned in order: left-up, up, right-up, right, right-down, down, left-down, left.
     * Points that would be outside the matrix bounds are returned as null.
     *
     * @param i the row index
     * @param j the column index
     * @return a stream of Points (null for non-existent positions)
     */
    public Stream<Point> adjacent8Points(final int i, final int j) {
        final Point up = i == 0 ? null : Point.of(i - 1, j);
        final Point right = j == cols - 1 ? null : Point.of(i, j + 1);
        final Point down = i == rows - 1 ? null : Point.of(i + 1, j);
        final Point left = j == 0 ? null : Point.of(i, j - 1);

        final Point leftUp = i > 0 && j > 0 ? Point.of(i - 1, j - 1) : null;
        final Point rightUp = i > 0 && j < cols - 1 ? Point.of(i - 1, j + 1) : null;
        final Point rightDown = i < rows - 1 && j < cols - 1 ? Point.of(j + 1, j + 1) : null;
        final Point leftDown = i < rows - 1 && j > 0 ? Point.of(i + 1, j - 1) : null;

        return Stream.of(leftUp, up, rightUp, right, rightDown, down, leftDown, left);
    }

    /**
     * Returns the specified row as an array.
     * The returned array is a direct reference to the internal data, so modifications will affect the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * long[] firstRow = matrix.row(0); // Gets the first row
     * }</pre>
     * 
     * @param rowIndex the row index (0-based)
     * @return the row as a long array
     * @throws IllegalArgumentException if rowIndex is out of bounds
     */
    public long[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns the specified column as a new array.
     * The returned array is a copy, so modifications won't affect the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * long[] firstColumn = matrix.column(0); // Gets the first column
     * }</pre>
     * 
     * @param columnIndex the column index (0-based)
     * @return the column as a new long array
     * @throws IllegalArgumentException if columnIndex is out of bounds
     */
    public long[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final long[] c = new long[rows];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i][columnIndex];
        }

        return c;
    }

    /**
     * Sets the specified row with the given array values.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.setRow(0, new long[]{10, 20, 30}); // Sets the first row
     * }</pre>
     * 
     * @param rowIndex the row index to set (0-based)
     * @param row the array of values to set
     * @throws IllegalArgumentException if rowIndex is out of bounds or row length doesn't match column count
     */
    public void setRow(final int rowIndex, final long[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Sets the specified column with the given array values.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.setColumn(0, new long[]{10, 20, 30}); // Sets the first column
     * }</pre>
     * 
     * @param columnIndex the column index to set (0-based)
     * @param column the array of values to set
     * @throws IllegalArgumentException if columnIndex is out of bounds or column length doesn't match row count
     */
    public void setColumn(final int columnIndex, final long[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all elements in the specified row by applying the given function.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.updateRow(0, x -> x * 2); // Doubles all values in the first row
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param rowIndex the row index to update (0-based)
     * @param func the unary operator to apply to each element
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.LongUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsLong(a[rowIndex][i]);
        }
    }

    /**
     * Updates all elements in the specified column by applying the given function.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.updateColumn(0, x -> x + 10); // Adds 10 to all values in the first column
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param columnIndex the column index to update (0-based)
     * @param func the unary operator to apply to each element
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.LongUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsLong(a[i][columnIndex]);
        }
    }

    /**
     * Gets the diagonal elements from left-up to right-down (main diagonal).
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * long[] diagonal = matrix.getLU2RD(); // Returns [1, 5, 9]
     * }</pre>
     * 
     * @return an array containing the main diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    public long[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final long[] res = new long[rows];

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][i]; // NOSONAR
        }

        return res;
    }

    /**
     * Sets the diagonal elements from left-up to right-down (main diagonal).
     * The matrix must be square.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.setLU2RD(new long[]{10, 20, 30}); // Sets main diagonal to [10, 20, 30]
     * }</pre>
     * 
     * @param diagonal the values to set on the main diagonal
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
     */
    public void setLU2RD(final long[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates the diagonal elements from left-up to right-down by applying the given function.
     * The matrix must be square.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.updateLU2RD(x -> x * x); // Squares all main diagonal elements
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateLU2RD(final Throwables.LongUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsLong(a[i][i]);
        }
    }

    /**
     * Gets the diagonal elements from right-up to left-down (anti-diagonal).
     * The matrix must be square.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * long[] diagonal = matrix.getRU2LD(); // Returns [3, 5, 7]
     * }</pre>
     * 
     * @return an array containing the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    public long[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final long[] res = new long[rows];

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][cols - i - 1];
        }

        return res;
    }

    /**
     * Sets the diagonal elements from right-up to left-down (anti-diagonal).
     * The matrix must be square.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.setRU2LD(new long[]{10, 20, 30}); // Sets anti-diagonal to [10, 20, 30]
     * }</pre>
     * 
     * @param diagonal the values to set on the anti-diagonal
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
     */
    public void setRU2LD(final long[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = diagonal[i];
        }
    }

    /**
     * Updates the diagonal elements from right-up to left-down by applying the given function.
     * The matrix must be square.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.updateRU2LD(x -> -x); // Negates all anti-diagonal elements
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each anti-diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateRU2LD(final Throwables.LongUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsLong(a[i][cols - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix by applying the given function.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.updateAll(x -> x * 2); // Doubles all values in the matrix
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each element
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.LongUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.applyAsLong(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Updates all elements in the matrix based on their position.
     * The function receives the row and column indices and returns the new value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.updateAll((i, j) -> (long)(i + j)); // Sets each element to sum of its indices
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function that takes row and column indices and returns the new value
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Long, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements that match the predicate with the specified new value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.replaceIf(x -> x < 0, 0L); // Replaces all negative values with 0
     * }</pre>
     * 
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the predicate to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.LongPredicate<E> predicate, final long newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces elements based on their position using a predicate.
     * The predicate receives row and column indices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.replaceIf((i, j) -> i == j, 1L); // Sets diagonal elements to 1
     * }</pre>
     * 
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the predicate that takes row and column indices
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final long newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new matrix by applying the given function to each element.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix squared = matrix.map(x -> x * x); // Squares all elements
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each element
     * @return a new LongMatrix with transformed values
     * @throws E if the function throws an exception
     */
    public <E extends Exception> LongMatrix map(final Throwables.LongUnaryOperator<E> func) throws E {
        final long[][] result = new long[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsLong(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * Creates a new IntMatrix by applying the given function to each element.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix intMatrix = matrix.mapToInt(x -> (int)(x % 100)); // Convert to int with modulo
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function to convert long to int
     * @return a new IntMatrix with converted values
     * @throws E if the function throws an exception
     */
    public <E extends Exception> IntMatrix mapToInt(final Throwables.LongToIntFunction<E> func) throws E {
        final int[][] result = new int[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsInt(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return IntMatrix.of(result);
    }

    /**
     * Creates a new DoubleMatrix by applying the given function to each element.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * DoubleMatrix doubleMatrix = matrix.mapToDouble(x -> Math.sqrt(x)); // Square root of each element
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function to convert long to double
     * @return a new DoubleMatrix with converted values
     * @throws E if the function throws an exception
     */
    public <E extends Exception> DoubleMatrix mapToDouble(final Throwables.LongToDoubleFunction<E> func) throws E {
        final double[][] result = new double[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsDouble(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * Creates a new object matrix by applying the given function to each element.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<String> stringMatrix = matrix.mapToObj(Long::toString, String.class);
     * }</pre>
     * 
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the type of exception that the function may throw
     * @param func the function to convert long to the target type
     * @param targetElementType the class of the target element type
     * @return a new Matrix with converted values
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.LongFunction<? extends T, E> func, final Class<T> targetElementType) throws E {
        final T[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Fills the entire matrix with the specified value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.fill(0L); // Sets all elements to 0
     * }</pre>
     * 
     * @param val the value to fill the matrix with
     */
    public void fill(final long val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills the matrix with values from the given 2D array starting at position (0, 0).
     * 
     * @param b the source array to copy values from
     */
    public void fill(final long[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from the given 2D array.
     * Copies as much data as will fit from the source array starting at the specified position.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * long[][] patch = {{1, 2}, {3, 4}};
     * matrix.fill(1, 1, patch); // Fills starting at row 1, column 1
     * }</pre>
     * 
     * @param fromRowIndex the starting row index in this matrix
     * @param fromColumnIndex the starting column index in this matrix
     * @param b the source array to copy values from
     * @throws IndexOutOfBoundsException if the starting indices are negative or out of bounds
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final long[][] b) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, rows, rows);
        N.checkFromToIndex(fromColumnIndex, cols, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
        }
    }

    /**
     * Creates a deep copy of this matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix copy = matrix.copy();
     * copy.set(0, 0, 100L); // Modifying copy doesn't affect original
     * }</pre>
     * 
     * @return a new LongMatrix with the same values
     */
    @Override
    public LongMatrix copy() {
        final long[][] c = new long[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new LongMatrix(c);
    }

    /**
     * Creates a copy of a range of rows from this matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix subMatrix = matrix.copy(1, 3); // Copies rows 1 and 2 (exclusive of 3)
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new LongMatrix containing the specified rows
     * @throws IndexOutOfBoundsException if indices are out of bounds or fromRowIndex > toRowIndex
     */
    @Override
    public LongMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final long[][] c = new long[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new LongMatrix(c);
    }

    /**
     * Creates a copy of a submatrix defined by row and column ranges.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix subMatrix = matrix.copy(1, 3, 0, 2); // Copies rows 1-2, columns 0-1
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new LongMatrix containing the specified submatrix
     * @throws IndexOutOfBoundsException if indices are out of bounds
     */
    @Override
    public LongMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final long[][] c = new long[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new LongMatrix(c);
    }

    /**
     * Creates an extended matrix with the specified dimensions.
     * If the new dimensions are smaller, the matrix is truncated.
     * If larger, new elements are filled with 0.
     * 
     * @param newRows the number of rows in the extended matrix
     * @param newCols the number of columns in the extended matrix
     * @return a new LongMatrix with the specified dimensions
     */
    public LongMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, 0);
    }

    /**
     * Creates an extended matrix with the specified dimensions and default value.
     * If the new dimensions are smaller, the matrix is truncated.
     * If larger, new elements are filled with the default value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix extended = matrix.extend(5, 5, -1L); // Extends to 5x5, filling new cells with -1
     * }</pre>
     * 
     * @param newRows the number of rows in the extended matrix
     * @param newCols the number of columns in the extended matrix
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new LongMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public LongMatrix extend(final int newRows, final int newCols, final long defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            final boolean fillDefaultValue = defaultValueForNewCell != 0;
            final long[][] b = new long[newRows][];

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : new long[newCols];

                if (fillDefaultValue) {
                    if (i >= rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, defaultValueForNewCell);
                    }
                }
            }

            return new LongMatrix(b);
        }
    }

    /**
     * Creates an extended matrix by adding rows/columns in all directions.
     * New elements are filled with 0.
     * 
     * @param toUp number of rows to add at the top
     * @param toDown number of rows to add at the bottom
     * @param toLeft number of columns to add on the left
     * @param toRight number of columns to add on the right
     * @return a new extended LongMatrix
     */
    public LongMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, 0);
    }

    /**
     * Creates an extended matrix by adding rows/columns in all directions with a default value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix extended = matrix.extend(1, 1, 1, 1, 99L); // Adds 1 row/column on each side, filled with 99
     * }</pre>
     * 
     * @param toUp number of rows to add at the top
     * @param toDown number of rows to add at the bottom
     * @param toLeft number of columns to add on the left
     * @param toRight number of columns to add on the right
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new extended LongMatrix
     * @throws IllegalArgumentException if any parameter is negative
     */
    public LongMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final long defaultValueForNewCell)
            throws IllegalArgumentException {
        N.checkArgument(toUp >= 0, "The 'toUp' can't be negative %s", toUp);
        N.checkArgument(toDown >= 0, "The 'toDown' can't be negative %s", toDown);
        N.checkArgument(toLeft >= 0, "The 'toLeft' can't be negative %s", toLeft);
        N.checkArgument(toRight >= 0, "The 'toRight' can't be negative %s", toRight);

        if (toUp == 0 && toDown == 0 && toLeft == 0 && toRight == 0) {
            return copy();
        } else {
            final int newRows = toUp + rows + toDown;
            final int newCols = toLeft + cols + toRight;
            final boolean fillDefaultValue = defaultValueForNewCell != 0;
            final long[][] b = new long[newRows][newCols];

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

            return new LongMatrix(b);
        }
    }

    /**
     * Reverses each row of the matrix horizontally (in-place).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.reverseH(); // [[1,2,3],[4,5,6]] becomes [[3,2,1],[6,5,4]]
     * }</pre>
     */
    public void reverseH() {
        for (int i = 0; i < rows; i++) {
            N.reverse(a[i]);
        }
    }

    /**
     * Reverses each column of the matrix vertically (in-place).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.reverseV(); // [[1,2],[3,4],[5,6]] becomes [[5,6],[3,4],[1,2]]
     * }</pre>
     */
    public void reverseV() {
        for (int j = 0; j < cols; j++) {
            long tmp = 0;
            for (int l = 0, h = rows - 1; l < h;) {
                tmp = a[l][j];
                a[l++][j] = a[h][j];
                a[h--][j] = tmp;
            }
        }
    }

    /**
     * Creates a new matrix that is horizontally flipped (each row reversed).
     * The original matrix is not modified.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix flipped = matrix.flipH(); // [[1,2,3],[4,5,6]] returns [[3,2,1],[6,5,4]]
     * }</pre>
     * 
     * @return a new horizontally flipped LongMatrix
     * @see #flipV()
     */
    public LongMatrix flipH() {
        final LongMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * Creates a new matrix that is vertically flipped (each column reversed).
     * The original matrix is not modified.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix flipped = matrix.flipV(); // [[1,2],[3,4],[5,6]] returns [[5,6],[3,4],[1,2]]
     * }</pre>
     * 
     * @return a new vertically flipped LongMatrix
     * @see #flipH()
     */
    public LongMatrix flipV() {
        final LongMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * Creates a new matrix rotated 90 degrees clockwise.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix rotated = matrix.rotate90(); // [[1,2],[3,4]] becomes [[3,1],[4,2]]
     * }</pre>
     * 
     * @return a new LongMatrix rotated 90 degrees clockwise
     */
    @Override
    public LongMatrix rotate90() {
        final long[][] c = new long[cols][rows];

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

        return new LongMatrix(c);
    }

    /**
     * Creates a new matrix rotated 180 degrees.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix rotated = matrix.rotate180(); // [[1,2],[3,4]] becomes [[4,3],[2,1]]
     * }</pre>
     * 
     * @return a new LongMatrix rotated 180 degrees
     */
    @Override
    public LongMatrix rotate180() {
        final long[][] c = new long[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new LongMatrix(c);
    }

    /**
     * Creates a new matrix rotated 270 degrees clockwise (90 degrees counter-clockwise).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix rotated = matrix.rotate270(); // [[1,2],[3,4]] becomes [[2,4],[1,3]]
     * }</pre>
     * 
     * @return a new LongMatrix rotated 270 degrees clockwise
     */
    @Override
    public LongMatrix rotate270() {
        final long[][] c = new long[cols][rows];

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

        return new LongMatrix(c);
    }

    /**
     * Creates the transpose of this matrix (rows become columns and vice versa).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix transposed = matrix.transpose(); // [[1,2,3],[4,5,6]] becomes [[1,4],[2,5],[3,6]]
     * }</pre>
     * 
     * @return a new transposed LongMatrix
     */
    @Override
    public LongMatrix transpose() {
        final long[][] c = new long[cols][rows];

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

        return new LongMatrix(c);
    }

    /**
     * Reshapes the matrix to the specified dimensions.
     * Elements are read in row-major order and placed into the new shape.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix reshaped = matrix.reshape(3, 2); // Reshapes a 2x3 matrix to 3x2
     * }</pre>
     * 
     * @param newRows the number of rows in the reshaped matrix
     * @param newCols the number of columns in the reshaped matrix
     * @return a new LongMatrix with the specified shape
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public LongMatrix reshape(final int newRows, final int newCols) {
        final long[][] c = new long[newRows][newCols];

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new LongMatrix(c);
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

        return new LongMatrix(c);
    }

    /**
     * Repeats elements in the matrix by the specified factors in both row and column directions.
     * Each element is repeated {@code rowRepeats} times in the row direction and {@code colRepeats} 
     * times in the column direction.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * LongMatrix repeated = matrix.repelem(2, 3);
     * // Result: [[1, 1, 1, 2, 2, 2],
     * //          [1, 1, 1, 2, 2, 2],
     * //          [3, 3, 3, 4, 4, 4],
     * //          [3, 3, 3, 4, 4, 4]]
     * }</pre>
     *
     * @param rowRepeats the number of times to repeat each element in the row direction
     * @param colRepeats the number of times to repeat each element in the column direction
     * @return a new matrix with repeated elements
     * @throws IllegalArgumentException if {@code rowRepeats} or {@code colRepeats} is less than or equal to 0
     * @see IntMatrix#repelem(int, int)
     */
    @Override
    public LongMatrix repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final long[][] c = new long[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            final long[] aa = a[i];
            final long[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                N.copy(Array.repeat(aa[j], colRepeats), 0, fr, j * colRepeats, colRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new LongMatrix(c);
    }

    /**
     * Repeats the entire matrix as a tile pattern by the specified factors in both row and column directions.
     * The whole matrix is repeated {@code rowRepeats} times in the row direction and {@code colRepeats} 
     * times in the column direction.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * LongMatrix tiled = matrix.repmat(2, 3);
     * // Result: [[1, 2, 1, 2, 1, 2],
     * //          [3, 4, 3, 4, 3, 4],
     * //          [1, 2, 1, 2, 1, 2],
     * //          [3, 4, 3, 4, 3, 4]]
     * }</pre>
     *
     * @param rowRepeats the number of times to repeat the matrix in the row direction
     * @param colRepeats the number of times to repeat the matrix in the column direction
     * @return a new matrix with the original matrix repeated as tiles
     * @throws IllegalArgumentException if {@code rowRepeats} or {@code colRepeats} is less than or equal to 0
     * @see IntMatrix#repmat(int, int)
     */
    @Override
    public LongMatrix repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final long[][] c = new long[rows * rowRepeats][cols * colRepeats];

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

        return new LongMatrix(c);
    }

    /**
     * Flattens the matrix into a single-dimensional list by concatenating all rows.
     * Elements are arranged in row-major order (row by row).
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}});
     * LongList flat = matrix.flatten();
     * // Result: [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @return a new {@code LongList} containing all elements of the matrix in row-major order
     */
    @Override
    public LongList flatten() {
        final long[] c = new long[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return LongList.of(c);
    }

    /**
     * Flattens the underlying 2D array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure. 
     *
     * @param <E> the type of exception that the operation may throw
     * @param op the operation to apply to the underlying 2D array
     * @throws E if the operation throws an exception
     * @see Arrays#flatOp(long[][], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super long[], E> op) throws E {
        Arrays.flatOp(a, op);
    }

    /**
     * Vertically stacks this matrix with another matrix.
     * The two matrices must have the same number of columns.
     * The result is a new matrix where the rows of the specified matrix are appended below the rows of this matrix.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][]{{1, 2, 3}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][]{{4, 5, 6}, {7, 8, 9}});
     * LongMatrix stacked = matrix1.vstack(matrix2);
     * // Result: [[1, 2, 3],
     * //          [4, 5, 6],
     * //          [7, 8, 9]]
     * }</pre>
     *
     * @param b the matrix to stack below this matrix
     * @return a new matrix with rows from both matrices stacked vertically
     * @throws IllegalArgumentException if the matrices don't have the same number of columns
     * @see IntMatrix#vstack(IntMatrix)
     */
    public LongMatrix vstack(final LongMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final long[][] c = new long[rows + b.rows][];
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
        }

        return LongMatrix.of(c);
    }

    /**
     * Horizontally stacks this matrix with another matrix.
     * The two matrices must have the same number of rows.
     * The result is a new matrix where the columns of the specified matrix are appended to the right of this matrix.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][]{{5}, {6}});
     * LongMatrix stacked = matrix1.hstack(matrix2);
     * // Result: [[1, 2, 5],
     * //          [3, 4, 6]]
     * }</pre>
     *
     * @param b the matrix to stack to the right of this matrix
     * @return a new matrix with columns from both matrices stacked horizontally
     * @throws IllegalArgumentException if the matrices don't have the same number of rows
     * @see IntMatrix#hstack(IntMatrix)
     */
    public LongMatrix hstack(final LongMatrix b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final long[][] c = new long[rows][cols + b.cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c[i], 0, cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
        }

        return LongMatrix.of(c);
    }

    /**
     * Performs element-wise addition of this matrix with another matrix.
     * The two matrices must have the same dimensions.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][]{{5, 6}, {7, 8}});
     * LongMatrix sum = matrix1.add(matrix2);
     * // Result: [[6, 8],
     * //          [10, 12]]
     * }</pre>
     *
     * @param b the matrix to add to this matrix
     * @return a new matrix containing the element-wise sum
     * @throws IllegalArgumentException if the matrices don't have the same shape
     */
    public LongMatrix add(final LongMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't add Matrixes with different shape");

        final long[][] ba = b.a;
        final long[][] result = new long[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (a[i][j] + ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * Performs element-wise subtraction of another matrix from this matrix.
     * The two matrices must have the same dimensions.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][]{{5, 6}, {7, 8}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * LongMatrix diff = matrix1.subtract(matrix2);
     * // Result: [[4, 4],
     * //          [4, 4]]
     * }</pre>
     *
     * @param b the matrix to subtract from this matrix
     * @return a new matrix containing the element-wise difference
     * @throws IllegalArgumentException if the matrices don't have the same shape
     */
    public LongMatrix subtract(final LongMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't subtract Matrixes with different shape");

        final long[][] ba = b.a;
        final long[][] result = new long[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (a[i][j] - ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * Performs matrix multiplication with another matrix.
     * The number of columns in this matrix must equal the number of rows in the specified matrix.
     * The result is a new matrix with dimensions (this.rows × b.cols).
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][]{{5, 6}, {7, 8}});
     * LongMatrix product = matrix1.multiply(matrix2);
     * // Result: [[19, 22],
     * //          [43, 50]]
     * }</pre>
     *
     * @param b the matrix to multiply with this matrix
     * @return a new matrix containing the matrix product
     * @throws IllegalArgumentException if the matrix dimensions are incompatible for multiplication
     */
    public LongMatrix multiply(final LongMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.rows, "Illegal matrix dimensions");

        final long[][] ba = b.a;
        final long[][] result = new long[rows][b.cols];
        final Throwables.IntTriConsumer<RuntimeException> cmd = (i, j, k) -> result[i][j] += a[i][k] * ba[k][j];

        Matrixes.multiply(this, b, cmd);

        return new LongMatrix(result);
    }

    /**
     * Converts this primitive long matrix to a boxed {@code Matrix<Long>}.
     * Each primitive long value is boxed into a {@code Long} object.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix primitive = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * Matrix<Long> boxed = primitive.boxed();
     * // Result: Matrix containing Long objects instead of primitives
     * }</pre>
     *
     * @return a new {@code Matrix<Long>} containing boxed values
     */
    public Matrix<Long> boxed() {
        final Long[][] c = new Long[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final long[] aa = a[i];
                final Long[] cc = c[i];

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
     * Converts this long matrix to a float matrix.
     * Each long value is cast to a float value.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix longMatrix = LongMatrix.of(new long[][]{{1L, 2L}, {3L, 4L}});
     * FloatMatrix floatMatrix = longMatrix.toFloatMatrix();
     * // Result: [[1.0f, 2.0f],
     * //          [3.0f, 4.0f]]
     * }</pre>
     *
     * @return a new {@code FloatMatrix} with values converted from long to float
     */
    public FloatMatrix toFloatMatrix() {
        final float[][] c = new float[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final long[] aa = a[i];
                final float[] cc = c[i];

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

        return new FloatMatrix(c);
    }

    /**
     * Converts this long matrix to a double matrix.
     * Each long value is cast to a double value without loss of precision.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix longMatrix = LongMatrix.of(new long[][]{{1L, 2L}, {3L, 4L}});
     * DoubleMatrix doubleMatrix = longMatrix.toDoubleMatrix();
     * // Result: [[1.0, 2.0],
     * //          [3.0, 4.0]]
     * }</pre>
     *
     * @return a new {@code DoubleMatrix} with values converted from long to double
     */
    public DoubleMatrix toDoubleMatrix() {
        return DoubleMatrix.create(a);
    }

    /**
     * Applies a binary operation element-wise to this matrix and another matrix.
     * The two matrices must have the same dimensions.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][]{{5, 6}, {7, 8}});
     * LongMatrix max = matrix1.zipWith(matrix2, Math::max);
     * // Result: [[5, 6],
     * //          [7, 8]]
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix to zip with this matrix
     * @param zipFunction the binary operation to apply to corresponding elements
     * @return a new matrix with the results of the zip operation
     * @throws IllegalArgumentException if the matrices don't have the same shape
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> LongMatrix zipWith(final LongMatrix matrixB, final Throwables.LongBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Can't zip two or more matrices which don't have same shape");

        final long[][] b = matrixB.a;
        final long[][] result = new long[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsLong(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * Applies a ternary operation element-wise to this matrix and two other matrices.
     * All three matrices must have the same dimensions.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][]{{5, 6}, {7, 8}});
     * LongMatrix matrix3 = LongMatrix.of(new long[][]{{9, 10}, {11, 12}});
     * LongMatrix median = matrix1.zipWith(matrix2, matrix3, 
     *     (a, b, c) -> (a + b + c) / 3);
     * // Result: [[5, 6],
     * //          [7, 8]]
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix to zip with
     * @param matrixC the third matrix to zip with
     * @param zipFunction the ternary operation to apply to corresponding elements
     * @return a new matrix with the results of the zip operation
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> LongMatrix zipWith(final LongMatrix matrixB, final LongMatrix matrixC, final Throwables.LongTernaryOperator<E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Can't zip two or more matrices which don't have same shape");

        final long[][] b = matrixB.a;
        final long[][] c = matrixC.a;
        final long[][] result = new long[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsLong(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * Creates a stream of elements on the diagonal from left-upper to right-down.
     * The matrix must be square (same number of rows and columns).
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, 
     *                                                 {4, 5, 6}, 
     *                                                 {7, 8, 9}});
     * LongStream diagonal = matrix.streamLU2RD();
     * // Stream contains: 1, 5, 9
     * }</pre>
     *
     * @return a stream of diagonal elements from left-upper to right-down
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public LongStream streamLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return LongStream.empty();
        }

        return LongStream.of(new LongIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public long nextLong() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return a[cursor][cursor++];
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                cursor = n < toIndex - cursor ? cursor + (int) n : toIndex;
            }

            @Override
            public long count() {
                return toIndex - cursor; // NOSONAR
            }
        });
    }

    /**
     * Creates a stream of elements on the diagonal from right-upper to left-down.
     * The matrix must be square (same number of rows and columns).
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, 
     *                                                 {4, 5, 6}, 
     *                                                 {7, 8, 9}});
     * LongStream diagonal = matrix.streamRU2LD();
     * // Stream contains: 3, 5, 7
     * }</pre>
     *
     * @return a stream of diagonal elements from right-upper to left-down
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public LongStream streamRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return LongStream.empty();
        }

        return LongStream.of(new LongIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public long nextLong() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return a[cursor][rows - ++cursor];
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                cursor = n < toIndex - cursor ? cursor + (int) n : toIndex;
            }

            @Override
            public long count() {
                return toIndex - cursor; // NOSONAR
            }
        });
    }

    /**
     * Creates a stream of all elements in the matrix in row-major order (horizontally).
     * Elements are streamed row by row from left to right.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}});
     * LongStream stream = matrix.streamH();
     * // Stream contains: 1, 2, 3, 4, 5, 6
     * }</pre>
     *
     * @return a stream of all matrix elements in row-major order
     */
    @Override
    public LongStream streamH() {
        return streamH(0, rows);
    }

    /**
     * Creates a stream of elements from a specific row.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}});
     * LongStream row = matrix.streamH(1);
     * // Stream contains: 4, 5, 6
     * }</pre>
     *
     * @param rowIndex the index of the row to stream
     * @return a stream of elements from the specified row
     * @throws IndexOutOfBoundsException if the row index is out of bounds
     */
    @Override
    public LongStream streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * Creates a stream of elements from a range of rows in row-major order.
     * Elements are streamed row by row from left to right.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2}, {3, 4}, {5, 6}});
     * LongStream stream = matrix.streamH(1, 3);
     * // Stream contains: 3, 4, 5, 6
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of elements from the specified row range
     * @throws IndexOutOfBoundsException if the row indices are out of bounds
     */
    @Override
    public LongStream streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return LongStream.empty();
        }

        return LongStream.of(new LongIteratorEx() {
            private int i = fromRowIndex;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i < toRowIndex;
            }

            @Override
            public long nextLong() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final long result = a[i][j++];

                if (j >= cols) {
                    i++;
                    j = 0;
                }

                return result;
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

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
            public long[] toArray() {
                final int len = (int) count();
                final long[] c = new long[len];

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
     * Creates a stream of all elements in the matrix in column-major order (vertically).
     * Elements are streamed column by column from top to bottom.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}});
     * LongStream stream = matrix.streamV();
     * // Stream contains: 1, 4, 2, 5, 3, 6
     * }</pre>
     *
     * @return a stream of all matrix elements in column-major order
     */
    @Override
    @Beta
    public LongStream streamV() {
        return streamV(0, cols);
    }

    /**
     * Creates a stream of elements from a specific column.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}});
     * LongStream column = matrix.streamV(1);
     * // Stream contains: 2, 5
     * }</pre>
     *
     * @param columnIndex the index of the column to stream
     * @return a stream of elements from the specified column
     * @throws IndexOutOfBoundsException if the column index is out of bounds
     */
    @Override
    public LongStream streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * Creates a stream of elements from a range of columns in column-major order.
     * Elements are streamed column by column from top to bottom.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}});
     * LongStream stream = matrix.streamV(1, 3);
     * // Stream contains: 2, 5, 3, 6
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of elements from the specified column range
     * @throws IndexOutOfBoundsException if the column indices are out of bounds
     */
    @Override
    @Beta
    public LongStream streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return LongStream.empty();
        }

        return LongStream.of(new LongIteratorEx() {
            private int i = 0;
            private int j = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return j < toColumnIndex;
            }

            @Override
            public long nextLong() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final long result = a[i++][j];

                if (i >= rows) {
                    i = 0;
                    j++;
                }

                return result;
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                if (n >= (long) (toColumnIndex - j) * LongMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    i += (int) ((n + i) % LongMatrix.this.rows);
                    j += (int) ((n + i) / LongMatrix.this.rows);
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public long[] toArray() {
                final int len = (int) count();
                final long[] c = new long[len];

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
     * Creates a stream of row streams, where each element is a stream of a complete row.
     * Rows are streamed in order from top to bottom.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<LongStream> rows = matrix.streamR();
     * // First stream contains: 1, 2, 3
     * // Second stream contains: 4, 5, 6
     * }</pre>
     *
     * @return a stream of row streams
     */
    @Override
    public Stream<LongStream> streamR() {
        return streamR(0, rows);
    }

    /**
     * Creates a stream of row streams from a range of rows.
     * Each element in the returned stream is a stream of a complete row.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2}, {3, 4}, {5, 6}});
     * Stream<LongStream> rows = matrix.streamR(1, 3);
     * // First stream contains: 3, 4
     * // Second stream contains: 5, 6
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of row streams from the specified range
     * @throws IndexOutOfBoundsException if the row indices are out of bounds
     */
    @Override
    public Stream<LongStream> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
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
            public LongStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return LongStream.of(a[cursor++]);
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                cursor = n < toIndex - cursor ? cursor + (int) n : toIndex;
            }

            @Override
            public long count() {
                return toIndex - cursor; // NOSONAR
            }
        });
    }

    /**
     * Creates a stream of column streams, where each element is a stream of a complete column.
     * Columns are streamed in order from left to right.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<LongStream> columns = matrix.streamC();
     * // First stream contains: 1, 4
     * // Second stream contains: 2, 5
     * // Third stream contains: 3, 6
     * }</pre>
     *
     * @return a stream of column streams
     */
    @Override
    @Beta
    public Stream<LongStream> streamC() {
        return streamC(0, cols);
    }

    /**
     * Creates a stream of column streams from a range of columns.
     * Each element in the returned stream is a stream of a complete column.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<LongStream> columns = matrix.streamC(1, 3);
     * // First stream contains: 2, 5
     * // Second stream contains: 3, 6
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of column streams from the specified range
     * @throws IndexOutOfBoundsException if the column indices are out of bounds
     */
    @Override
    @Beta
    public Stream<LongStream> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public LongStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return LongStream.of(new LongIteratorEx() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rows;
                    private int cursor2 = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor2 < toIndex2;
                    }

                    @Override
                    public long nextLong() {
                        if (cursor2 >= toIndex2) {
                            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                        }

                        return a[cursor2++][columnIndex];
                    }

                    @Override
                    public void advance(final long n) throws IllegalArgumentException {
                        N.checkArgNotNegative(n, "n");

                        cursor2 = n < toIndex2 - cursor2 ? cursor2 + (int) n : toIndex2;
                    }

                    @Override
                    public long count() {
                        return toIndex2 - cursor2; // NOSONAR
                    }
                });
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

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
     * This is an internal helper method.
     *
     * @param a the array to get the length of
     * @return the length of the array, or 0 if the array is null
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final long[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * Applies the specified action to each element in the matrix.
     * Elements are processed in row-major order.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * matrix.forEach(value -> System.out.print(value + " "));
     * // Output: 1 2 3 4
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the action to apply to each element
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.LongConsumer<E> action) throws E {
        forEach(0, rows, 0, cols, action);
    }

    /**
     * Applies the specified action to each element in a sub-region of the matrix.
     * Elements are processed in row-major order within the specified bounds.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.forEach(1, 3, 1, 3, value -> System.out.print(value + " "));
     * // Output: 5 6 8 9
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to apply to each element
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.LongConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final long[] aa = a[i];

                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(aa[j]);
                }
            }
        }
    }

    /**
     * Prints the matrix to the standard output.
     * The matrix is formatted with rows on separate lines and elements separated by spaces.
     *
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.println();
     * // Output:
     * // [1, 2, 3]
     * // [4, 5, 6]
     * }</pre>
     */
    @Override
    public void println() {
        Arrays.println(a);
    }

    /**
     * Returns the hash code value for this matrix.
     * The hash code is computed based on the matrix dimensions and element values.
     *
     * @return the hash code value for this matrix
     */
    @Override
    public int hashCode() {
        return N.deepHashCode(a);
    }

    /**
     * Compares this matrix with the specified object for equality.
     * Two matrices are equal if they have the same dimensions and all corresponding elements are equal.
     *
     * @param obj the object to compare with
     * @return {@code true} if the specified object is a LongMatrix with the same dimensions and elements
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof final LongMatrix another) {
            return cols == another.cols && rows == another.rows && N.deepEquals(a, another.a);
        }

        return false;
    }

    /**
     * Returns a string representation of this matrix.
     * The string representation consists of the matrix elements formatted as a 2D array.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][]{{1, 2}, {3, 4}});
     * System.out.println(matrix.toString()); // Prints: [[1, 2], [3, 4]]
     * }</pre>
     *
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
