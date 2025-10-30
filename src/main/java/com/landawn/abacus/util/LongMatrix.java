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
 * <p>The matrix is internally represented as a two-dimensional long array and supports various
 * matrix operations including arithmetic operations, transformations, and element-wise operations.</p>
 * 
 * <p><b>Usage Examples:</b></p>
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
     * Constructs a LongMatrix from a two-dimensional long array.
     * If the input array is null, an empty matrix (0x0) is created.
     *
     * <p><b>Important:</b> The array is used directly without copying. This means:
     * <ul>
     * <li>Modifications to the input array after construction will affect the matrix</li>
     * <li>Modifications to the matrix will affect the original array</li>
     * <li>This provides better performance but less encapsulation</li>
     * </ul>
     * For a safe copy, use {@link #of(long[][])} or {@link #copy()} after construction.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] data = {{1, 2}, {3, 4}};
     * LongMatrix matrix = new LongMatrix(data);
     * data[0][0] = 99; // This also changes matrix.get(0,0) to 99
     *
     * LongMatrix empty = new LongMatrix(null); // Creates 0x0 empty matrix
     * }</pre>
     *
     * @param a the two-dimensional long array to wrap as a matrix. Can be null.
     */
    public LongMatrix(final long[][] a) {
        super(a == null ? new long[0][0] : a);
    }

    /**
     * Creates an empty matrix with zero rows and zero columns.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.empty();
     * // matrix.rows returns 0
     * // matrix.cols returns 0
     * }</pre>
     *
     * @return an empty long matrix
     */
    public static LongMatrix empty() {
        return EMPTY_LONG_MATRIX;
    }

    /**
     * Creates a LongMatrix from a two-dimensional long array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * // matrix.get(1, 1) returns 4
     * }</pre>
     *
     * @param a the two-dimensional long array to create the matrix from, or null/empty for an empty matrix
     * @return a new LongMatrix containing the provided data, or an empty LongMatrix if input is null or empty
     */
    public static LongMatrix of(final long[]... a) {
        return N.isEmpty(a) ? EMPTY_LONG_MATRIX : new LongMatrix(a);
    }

    /**
     * Creates a LongMatrix from a two-dimensional int array by converting int values to long.
     * Each int value is widened to a long value without data loss, as int is a smaller primitive type than long.
     *
     * <p>All rows must have the same length as the first row (rectangular array required).
     * The method validates array structure and throws an exception if the array is jagged (rows of different lengths).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.create(new int[][] {{1, 2}, {3, 4}});
     * // Creates a matrix with values {{1L, 2L}, {3L, 4L}}
     * // matrix.get(0, 0) returns 1L
     *
     * // Empty or null input creates an empty matrix
     * LongMatrix empty = LongMatrix.create(null); // Returns empty matrix
     * }</pre>
     *
     * @param a the two-dimensional int array to convert to a long matrix, or null/empty for an empty matrix
     * @return a new LongMatrix with converted values, or an empty LongMatrix if input is null or empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths (non-rectangular array)
     */
    public static LongMatrix create(final int[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_LONG_MATRIX;
        }

        N.checkArgument(a[0] != null, "First row cannot be null");

        final int cols = a[0].length;

        // Validate all rows have the same length
        for (int i = 1; i < a.length; i++) {
            N.checkArgument(a[i] != null && a[i].length == cols, "All rows must have the same length. Row 0 has length %s but row %s has length %s", cols, i,
                    a[i] == null ? 0 : a[i].length);
        }

        final long[][] c = new long[a.length][cols];

        for (int i = 0, len = a.length; i < len; i++) {
            final int[] aa = a[i];
            final long[] cc = c[i];

            for (int j = 0; j < cols; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new LongMatrix(c);
    }

    /**
     * Creates a 1-row matrix filled with random long values.
     * Each element is a random long value generated using the default random number generator.
     * The values can range across the entire long value space (from {@code Long.MIN_VALUE} to {@code Long.MAX_VALUE}).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.random(5);
     * // Creates a 1x5 matrix with random long values
     * // Each value is in range [Long.MIN_VALUE, Long.MAX_VALUE]
     * }</pre>
     *
     * @param len the number of columns (must be non-negative)
     * @return a 1-row matrix filled with random long values
     * @throws IllegalArgumentException if len is negative
     */
    @SuppressWarnings("deprecation")
    public static LongMatrix random(final int len) {
        return new LongMatrix(new long[][] { LongList.random(len).array() });
    }

    /**
     * Creates a 1-row matrix with all elements set to the specified value.
     * This is useful for initializing matrices with a constant value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.repeat(42L, 5);
     * // Creates a 1x5 matrix: [[42, 42, 42, 42, 42]]
     *
     * LongMatrix zeros = LongMatrix.repeat(0L, 10);
     * // Creates a 1x10 matrix filled with zeros
     * }</pre>
     *
     * @param val the value to repeat
     * @param len the number of columns (must be non-negative)
     * @return a 1-row matrix with all elements set to val
     * @throws IllegalArgumentException if len is negative
     */
    public static LongMatrix repeat(final long val, final int len) {
        return new LongMatrix(new long[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a 1-row LongMatrix with values from startInclusive to endExclusive.
     * The values are generated with a step of 1. If {@code startInclusive >= endExclusive}, an empty matrix is returned.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.range(0L, 5L); // Creates [[0, 1, 2, 3, 4]]
     * LongMatrix empty = LongMatrix.range(5L, 0L);  // Creates an empty matrix
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @return a new 1×n LongMatrix where n = max(0, endExclusive - startInclusive)
     */
    public static LongMatrix range(final long startInclusive, final long endExclusive) {
        return new LongMatrix(new long[][] { Array.range(startInclusive, endExclusive) });
    }

    /**
     * Creates a 1-row LongMatrix with values from startInclusive to endExclusive with the specified step.
     * The step size can be positive (for ascending sequences) or negative (for descending sequences).
     * If the step would not reach endExclusive from startInclusive, an empty matrix is returned.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.range(0L, 10L, 2L);  // Creates [[0, 2, 4, 6, 8]]
     * LongMatrix desc = LongMatrix.range(10L, 0L, -2L);   // Creates [[10, 8, 6, 4, 2]]
     * LongMatrix empty = LongMatrix.range(0L, 10L, -1L);  // Creates an empty matrix (step is wrong direction)
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @param by the step size (must not be zero; can be positive or negative)
     * @return a new 1×n LongMatrix with values incremented by the step size
     * @throws IllegalArgumentException if {@code by} is zero
     */
    public static LongMatrix range(final long startInclusive, final long endExclusive, final long by) {
        return new LongMatrix(new long[][] { Array.range(startInclusive, endExclusive, by) });
    }

    /**
     * Creates a 1-row LongMatrix with values from startInclusive to endInclusive.
     * This method includes the end value, unlike {@link #range(long, long)}.
     * If {@code startInclusive > endInclusive}, an empty matrix is returned.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.rangeClosed(0L, 4L); // Creates [[0, 1, 2, 3, 4]]
     * LongMatrix single = LongMatrix.rangeClosed(5L, 5L); // Creates [[5]]
     * LongMatrix empty = LongMatrix.rangeClosed(5L, 0L);  // Creates an empty matrix
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive)
     * @return a new 1×n LongMatrix where n = max(0, endInclusive - startInclusive + 1)
     */
    public static LongMatrix rangeClosed(final long startInclusive, final long endInclusive) {
        return new LongMatrix(new long[][] { Array.rangeClosed(startInclusive, endInclusive) });
    }

    /**
     * Creates a 1-row LongMatrix with values from startInclusive to endInclusive with the specified step.
     * The step size can be positive (for ascending sequences) or negative (for descending sequences).
     * The end value is included only if it is reachable by stepping from start. If the step would not
     * reach endInclusive from startInclusive, an empty matrix is returned.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.rangeClosed(0L, 8L, 2L);  // Creates [[0, 2, 4, 6, 8]]
     * LongMatrix partial = LongMatrix.rangeClosed(0L, 9L, 2L); // Creates [[0, 2, 4, 6, 8]] (9 not reachable)
     * LongMatrix desc = LongMatrix.rangeClosed(10L, 0L, -2L);  // Creates [[10, 8, 6, 4, 2, 0]]
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive, if reachable by stepping)
     * @param by the step size (must not be zero; can be positive or negative)
     * @return a new 1×n LongMatrix with values incremented by the step size
     * @throws IllegalArgumentException if {@code by} is zero
     */
    public static LongMatrix rangeClosed(final long startInclusive, final long endInclusive, final long by) {
        return new LongMatrix(new long[][] { Array.rangeClosed(startInclusive, endInclusive, by) });
    }

    /**
     * Creates a square matrix from the specified main diagonal elements (left-upper to right-down).
     * All other elements (off-diagonal) are set to zero. The matrix size is n×n where n is the length
     * of the diagonal array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.diagonalLU2RD(new long[] {1, 2, 3});
     * // Creates a 3x3 matrix:
     * // [[1, 0, 0],
     * //  [0, 2, 0],
     * //  [0, 0, 3]]
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements (from top-left to bottom-right)
     * @return a square n×n matrix with the specified main diagonal, where n is the array length
     */
    public static LongMatrix diagonalLU2RD(final long[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square matrix from the specified anti-diagonal elements (right-upper to left-down).
     * All other elements (off-diagonal) are set to zero. The matrix size is n×n where n is the length
     * of the diagonal array. The anti-diagonal runs from top-right to bottom-left.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.diagonalRU2LD(new long[] {1, 2, 3});
     * // Creates a 3x3 matrix:
     * // [[0, 0, 1],
     * //  [0, 2, 0],
     * //  [3, 0, 0]]
     * }</pre>
     *
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements (from top-right to bottom-left)
     * @return a square n×n matrix with the specified anti-diagonal, where n is the array length
     */
    public static LongMatrix diagonalRU2LD(final long[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a square matrix from the specified main diagonal and anti-diagonal elements.
     * All other elements (off both diagonals) are set to zero. If both arrays are provided, they must have the same length.
     * Either array can be null/empty, in which case only the other diagonal is set.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.diagonal(new long[] { 1, 2, 3 }, new long[] { 4, 5, 6 });
     * // Creates 3x3 matrix with both diagonals set
     * // Resulting matrix: 
     * //   {1, 0, 4},
     * //   {0, 2, 0},
     * //   {6, 0, 3} 
     *
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements (top-left to bottom-right), or null
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements (top-right to bottom-left), or null
     * @return a square n×n matrix with the specified diagonals, where n is the length of the non-null array(s)
     * @throws IllegalArgumentException if both arrays are non-empty and have different lengths
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
     * Converts a boxed {@code Matrix<Long>} to a primitive {@code LongMatrix}.
     * This method unboxes all {@code Long} wrapper objects to primitive {@code long} values for more efficient
     * storage and operations. This is particularly beneficial when working with large matrices, as primitive
     * arrays have less memory overhead and better cache locality than arrays of wrapper objects.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<Long> boxedMatrix = Matrix.of(new Long[][] {{1L, 2L}, {3L, 4L}});
     * LongMatrix primitiveMatrix = LongMatrix.unbox(boxedMatrix);
     * // primitiveMatrix now uses primitive long[] arrays internally for better performance
     * }</pre>
     *
     * @param x the boxed Long matrix to convert
     * @return a new LongMatrix with unboxed primitive values
     */
    public static LongMatrix unbox(final Matrix<Long> x) {
        return LongMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of the matrix elements, which is always {@code long.class}.
     * 
     * @return {@code long.class}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return long.class;
    }

    /**
     * Returns the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * long value = matrix.get(0, 1); // Returns 2L
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position (i, j)
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public long get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Returns the element at the specified point.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * // Assuming you have a Point implementation
     * // long value = matrix.get(point); // Returns element at point
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
     * Sets the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * matrix.set(0, 1, 9L); // Sets element at row 0, column 1 to 9L
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public void set(final int i, final int j, final long val) {
        a[i][j] = val;
    }

    /**
     * Sets the element at the specified point.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * // Assuming you have a Point implementation
     * // matrix.set(point, 9L); // Sets element at point
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
     * Returns the element above the specified position, if it exists.
     * This method provides safe access to the element directly above the given position
     * without throwing an exception when at the top edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * OptionalLong value = matrix.upOf(1, 0); // Returns OptionalLong.of(1L)
     * OptionalLong empty = matrix.upOf(0, 0); // Returns OptionalLong.empty() - no row above
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalLong containing the element at position (i-1, j), or empty if i == 0
     * @throws ArrayIndexOutOfBoundsException if j is out of bounds
     */
    public OptionalLong upOf(final int i, final int j) {
        return i == 0 ? OptionalLong.empty() : OptionalLong.of(a[i - 1][j]);
    }

    /**
     * Returns the element below the specified position, if it exists.
     * This method provides safe access to the element directly below the given position
     * without throwing an exception when at the bottom edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * OptionalLong value = matrix.downOf(0, 0); // Returns OptionalLong.of(3L)
     * OptionalLong empty = matrix.downOf(1, 0); // Returns OptionalLong.empty() - no row below
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalLong containing the element at position (i+1, j), or empty if i == rows-1
     * @throws ArrayIndexOutOfBoundsException if j is out of bounds
     */
    public OptionalLong downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalLong.empty() : OptionalLong.of(a[i + 1][j]);
    }

    /**
     * Returns the element to the left of the specified position, if it exists.
     * This method provides safe access to the element directly to the left of the given position
     * without throwing an exception when at the leftmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * OptionalLong value = matrix.leftOf(0, 1); // Returns OptionalLong.of(1L)
     * OptionalLong empty = matrix.leftOf(0, 0); // Returns OptionalLong.empty() - no column to the left
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalLong containing the element at position (i, j-1), or empty if j == 0
     */
    public OptionalLong leftOf(final int i, final int j) {
        return j == 0 ? OptionalLong.empty() : OptionalLong.of(a[i][j - 1]);
    }

    /**
     * Returns the element to the right of the specified position, if it exists.
     * This method provides safe access to the element directly to the right of the given position
     * without throwing an exception when at the rightmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * OptionalLong value = matrix.rightOf(0, 0); // Returns OptionalLong.of(2L)
     * OptionalLong empty = matrix.rightOf(0, 1); // Returns OptionalLong.empty() - no column to the right
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalLong containing the element at position (i, j+1), or empty if j == cols-1
     */
    public OptionalLong rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalLong.empty() : OptionalLong.of(a[i][j + 1]);
    }

    /**
     * Returns the specified row as a long array.
     *
     * <p><b>Note:</b> This method returns a reference to the internal array, not a copy.
     * Modifications to the returned array will affect the matrix. If you need an independent
     * copy, use {@code Arrays.copyOf(matrix.row(i), matrix.cols)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L, 3L}, {4L, 5L, 6L}});
     * long[] firstRow = matrix.row(0); // Returns [1L, 2L, 3L]
     *
     * // Direct modification affects the matrix
     * firstRow[0] = 99L; // matrix now has 99L at position (0,0)
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return the specified row array (direct reference to internal storage)
     * @throws IllegalArgumentException if rowIndex &lt; 0 or rowIndex &gt;= rows
     */
    public long[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as a new long array.
     *
     * <p>Unlike {@link #row(int)}, this method always returns a new array copy since
     * columns are not stored contiguously in memory. Modifications to the returned array
     * will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L, 3L}, {4L, 5L, 6L}});
     * long[] firstColumn = matrix.column(0); // Returns [1L, 4L]
     *
     * // Modification does NOT affect the matrix (it's a copy)
     * firstColumn[0] = 99L; // matrix still has 1L at position (0,0)
     * }</pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing the values from the specified column
     * @throws IllegalArgumentException if columnIndex &lt; 0 or columnIndex &gt;= cols
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
     * Sets the values of the specified row.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L, 3L}, {4L, 5L, 6L}});
     * matrix.setRow(0, new long[] {7L, 8L, 9L}); // First row is now [7L, 8L, 9L]
     * }</pre>
     *
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to set; must have length equal to number of columns
     * @throws IllegalArgumentException if rowIndex is out of bounds or row length does not match column count
     */
    public void setRow(final int rowIndex, final long[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Sets the values of the specified column.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L, 3L}, {4L, 5L, 6L}});
     * matrix.setColumn(0, new long[] {7L, 8L}); // First column is now [7L, 8L]
     * }</pre>
     *
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to set; must have length equal to number of rows
     * @throws IllegalArgumentException if columnIndex is out of bounds or column length does not match row count
     */
    public void setColumn(final int columnIndex, final long[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all elements in the specified row by applying the given function.
     * Each element in the row is replaced with the result of applying the function to that element.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.updateRow(0, x -> x * 2); // Doubles all values in the first row
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param rowIndex the row index to update (0-based)
     * @param func the unary operator to apply to each element in the row
     * @throws IndexOutOfBoundsException if rowIndex is out of bounds [0, rows)
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.LongUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsLong(a[rowIndex][i]);
        }
    }

    /**
     * Updates all elements in the specified column by applying the given function.
     * Each element in the column is replaced with the result of applying the function to that element.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.updateColumn(0, x -> x + 10); // Adds 10 to all values in the first column
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param columnIndex the column index to update (0-based)
     * @param func the unary operator to apply to each element in the column
     * @throws IndexOutOfBoundsException if columnIndex is out of bounds [0, cols)
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.LongUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsLong(a[i][columnIndex]);
        }
    }

    /**
     * Returns the elements on the main diagonal from left-upper to right-down.
     * The matrix must be square (rows == columns) for this operation.
     *
     * <p>This method extracts the main diagonal elements at positions (0,0), (1,1), (2,2), etc.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L, 3L}, {4L, 5L, 6L}, {7L, 8L, 9L}});
     * long[] diagonal = matrix.getLU2RD(); // Returns [1L, 5L, 9L]
     * }</pre>
     *
     * @return a new long array containing the main diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public long[] getLU2RD() throws IllegalStateException {
        checkIfRowAndColumnSizeAreSame();

        final long[] res = new long[rows];

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
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * matrix.setLU2RD(new long[] {9L, 8L});
     * // Diagonal is now [9L, 8L]
     * }</pre>
     *
     * @param diagonal the new values for the main diagonal; must have length &gt;= rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length &lt; rows
     */
    public void setLU2RD(final long[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates the diagonal elements from left-upper to right-down (main diagonal) by applying the given function.
     * Each element on the main diagonal is replaced with the result of applying the function to that element.
     * The matrix must be square (rows == columns) for this operation.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.updateLU2RD(x -> x * x); // Squares all main diagonal elements
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each diagonal element
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateLU2RD(final Throwables.LongUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsLong(a[i][i]);
        }
    }

    /**
     * Returns the elements on the anti-diagonal from right-upper to left-down.
     * The matrix must be square (rows == columns) for this operation.
     *
     * <p>This method extracts the anti-diagonal (secondary diagonal) elements from
     * top-right to bottom-left, at positions (0,n-1), (1,n-2), (2,n-3), etc.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L, 3L}, {4L, 5L, 6L}, {7L, 8L, 9L}});
     * long[] diagonal = matrix.getRU2LD(); // Returns [3L, 5L, 7L]
     * }</pre>
     *
     * @return a new long array containing the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
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
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * matrix.setRU2LD(new long[] {9L, 8L});
     * // Anti-diagonal is now [9L, 8L]
     * }</pre>
     *
     * @param diagonal the new values for the anti-diagonal; must have length &gt;= rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length &lt; rows
     */
    public void setRU2LD(final long[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = diagonal[i];
        }
    }

    /**
     * Updates the diagonal elements from right-upper to left-down (anti-diagonal) by applying the given function.
     * Each element on the anti-diagonal is replaced with the result of applying the function to that element.
     * The matrix must be square (rows == columns) for this operation.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.updateRU2LD(x -> -x); // Negates all anti-diagonal elements
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each anti-diagonal element
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRU2LD(final Throwables.LongUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsLong(a[i][cols - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix by applying the given function.
     * Each element is replaced with the result of applying the function to that element.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
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
     * The function receives the row and column indices and returns the new value for that position.
     * Each element at position (i, j) is replaced with the result of {@code func.apply(i, j)}.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
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
     * For each element, if the predicate returns {@code true}, the element is replaced with {@code newValue};
     * otherwise, the element remains unchanged.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
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
     * The predicate receives row and column indices for each position.
     * For each element at position (i, j), if the predicate returns {@code true}, the element is replaced with {@code newValue};
     * otherwise, the element remains unchanged.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.replaceIf((i, j) -> i == j, 1L); // Sets diagonal elements to 1
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the predicate that takes row and column indices and returns true if the element should be replaced
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final long newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new matrix by applying the given function to each element.
     * The original matrix remains unchanged. Each element in the new matrix is the result of applying
     * the function to the corresponding element in this matrix.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
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
     * The original matrix remains unchanged. Each element in the new matrix is the result of applying
     * the function to the corresponding element in this matrix.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
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
     * The original matrix remains unchanged. Each element in the new matrix is the result of applying
     * the function to the corresponding element in this matrix.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
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
     * The original matrix remains unchanged. Each element in the new matrix is the result of applying
     * the function to the corresponding element in this matrix.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<String> stringMatrix = matrix.mapToObj(Long::toString, String.class);
     * }</pre>
     *
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the type of exception that the function may throw
     * @param func the function to convert long to the target type
     * @param targetElementType the class of the target element type (used for creating the result array)
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
     * All elements in the matrix are set to the given value.
     *
     * <p><b>Usage Examples:</b></p>
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
     * Fills the matrix with values from the given two-dimensional array starting at position (0, 0).
     * Copies as much data as will fit from the source array into this matrix.
     * If the source array is smaller than this matrix, only the overlapping portion is filled.
     * If the source array is larger, only the portion that fits is copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{0, 0, 0}, {0, 0, 0}});
     * long[][] data = {{1, 2}, {3, 4}};
     * matrix.fill(data);
     * // Matrix becomes: [[1, 2, 0], [3, 4, 0]]
     * }</pre>
     *
     * @param b the source array to copy values from
     */
    public void fill(final long[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from the given two-dimensional array starting at the specified position.
     * Copies as much data as will fit from the source array into this matrix beginning at the specified indices.
     * If the source array extends beyond the matrix boundaries, only the overlapping portion is copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] patch = {{1, 2}, {3, 4}};
     * matrix.fill(1, 1, patch); // Fills starting at row 1, column 1
     * }</pre>
     *
     * @param fromRowIndex the starting row index in this matrix (0-based)
     * @param fromColumnIndex the starting column index in this matrix (0-based)
     * @param b the source array to copy values from
     * @throws IllegalArgumentException if the starting indices are negative or exceed matrix dimensions [0, rows] and [0, cols]
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final long[][] b) throws IllegalArgumentException {
        N.checkArgument(fromRowIndex >= 0 && fromRowIndex <= rows, "fromRowIndex(%s) must be between 0 and rows(%s)", fromRowIndex, rows);
        N.checkArgument(fromColumnIndex >= 0 && fromColumnIndex <= cols, "fromColumnIndex(%s) must be between 0 and cols(%s)", fromColumnIndex, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
        }
    }

    /**
     * Returns a copy of this matrix.
     * All elements are copied to a new matrix, so modifications to the returned matrix
     * will not affect this matrix, and vice versa.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix original = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix copy = original.copy();
     * // copy is independent from original
     * copy.set(0, 0, 99L); // Does not affect original
     * }</pre>
     *
     * @return a new matrix that is a copy of this matrix
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
     * Creates a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows and is completely independent from the original matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix subMatrix = matrix.copy(1, 3); // Copies rows 1 and 2 (exclusive of 3)
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new LongMatrix containing the specified rows with all columns
     * @throws IndexOutOfBoundsException if indices are out of bounds [0, rows] or fromRowIndex &gt; toRowIndex
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
     * The returned matrix is independent of this matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix subMatrix = matrix.copy(1, 3, 0, 2); // Copies rows 1-2, columns 0-1
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new LongMatrix containing the specified submatrix
     * @throws IndexOutOfBoundsException if indices are out of bounds [0, rows] or [0, cols], or if fromRowIndex &gt; toRowIndex or fromColumnIndex &gt; toColumnIndex
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
     * Creates an extended or truncated matrix with the specified dimensions.
     * If the new dimensions are smaller than the current dimensions, the matrix is truncated.
     * If larger, new elements are filled with 0 (the default value for long primitives).
     * This is equivalent to calling {@link #extend(int, int, long)} with defaultValueForNewCell = 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix extended = matrix.extend(3, 4);
     * // Result: [[1, 2, 0, 0],
     * //          [3, 4, 0, 0],
     * //          [0, 0, 0, 0]]
     * }</pre>
     *
     * @param newRows the number of rows in the resulting matrix, must be non-negative
     * @param newCols the number of columns in the resulting matrix, must be non-negative
     * @return a new LongMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public LongMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, 0);
    }

    /**
     * Creates an extended or truncated matrix with the specified dimensions and default value for new cells.
     * If the new dimensions are smaller than the current dimensions, the matrix is truncated.
     * If larger, new elements are filled with the specified default value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix extended = matrix.extend(5, 5, -1L); // Extends to 5x5, filling new cells with -1
     * }</pre>
     *
     * @param newRows the number of rows in the resulting matrix, must be non-negative
     * @param newCols the number of columns in the resulting matrix, must be non-negative
     * @param defaultValueForNewCell the value to fill new cells with (only used when extending, not when truncating)
     * @return a new LongMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative, or if the total size would exceed Integer.MAX_VALUE
     */
    public LongMatrix extend(final int newRows, final int newCols, final long defaultValueForNewCell) throws IllegalArgumentException {
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
     * Creates an extended matrix by adding rows/columns in all four directions around this matrix.
     * New elements are filled with 0 (the default value for long primitives).
     * This method is useful for adding padding/borders around an existing matrix.
     * This is equivalent to calling {@link #extend(int, int, int, int, long)} with defaultValueForNewCell = 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix extended = matrix.extend(1, 1, 1, 1);
     * // Result: [[0, 0, 0, 0],
     * //          [0, 1, 2, 0],
     * //          [0, 3, 4, 0],
     * //          [0, 0, 0, 0]]
     * }</pre>
     *
     * @param toUp number of rows to add at the top, must be non-negative
     * @param toDown number of rows to add at the bottom, must be non-negative
     * @param toLeft number of columns to add on the left, must be non-negative
     * @param toRight number of columns to add on the right, must be non-negative
     * @return a new extended LongMatrix with dimensions (rows + toUp + toDown) × (cols + toLeft + toRight)
     * @throws IllegalArgumentException if any parameter is negative
     */
    public LongMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, 0);
    }

    /**
     * Creates an extended matrix by adding rows/columns in all four directions around this matrix with a specified default value.
     * This method is useful for adding padding/borders around an existing matrix with a custom fill value.
     * The original matrix content is preserved in the center, with new cells filled using the specified value.
     *
     * <p>The resulting matrix dimensions are: (rows + toUp + toDown) × (cols + toLeft + toRight).
     * The original matrix elements are positioned starting at offset (toUp, toLeft) in the result.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix extended = matrix.extend(1, 1, 1, 1, 99L);
     * // Result: [[99, 99, 99, 99],
     * //          [99,  1,  2, 99],
     * //          [99,  3,  4, 99],
     * //          [99, 99, 99, 99]]
     * }</pre>
     *
     * @param toUp number of rows to add at the top, must be non-negative
     * @param toDown number of rows to add at the bottom, must be non-negative
     * @param toLeft number of columns to add on the left, must be non-negative
     * @param toRight number of columns to add on the right, must be non-negative
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new extended LongMatrix with dimensions (rows + toUp + toDown) × (cols + toLeft + toRight)
     * @throws IllegalArgumentException if any parameter is negative or if the resulting dimensions would exceed Integer.MAX_VALUE
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
            if ((long) toUp + rows + toDown > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Result would have too many rows: " + toUp + " + " + rows + " + " + toDown);
            }

            if ((long) toLeft + cols + toRight > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Result would have too many columns: " + toLeft + " + " + cols + " + " + toRight);
            }

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
     * Reverses the order of elements in each row (horizontal flip in-place).
     * This operation modifies the matrix directly. For a non-destructive version, use {@link #flipH()}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.reverseH();
     * // Matrix is now: [[3, 2, 1], [6, 5, 4]]
     * }</pre>
     */
    public void reverseH() {
        for (int i = 0; i < rows; i++) {
            N.reverse(a[i]);
        }
    }

    /**
     * Reverses the order of elements in each column (vertical flip in-place).
     * This operation modifies the matrix directly. For a non-destructive version, use {@link #flipV()}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}, {5, 6}});
     * matrix.reverseV();
     * // Matrix is now: [[5, 6], [3, 4], [1, 2]]
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
     * <p><b>Usage Examples:</b></p>
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
     * <p><b>Usage Examples:</b></p>
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
     * Returns a new matrix rotated 90 degrees clockwise.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix rotated = matrix.rotate90();
     * // rotated is {{3, 1}, {4, 2}}
     * }</pre>
     *
     * @return a new matrix rotated 90 degrees clockwise
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
     * Returns a new matrix rotated 180 degrees.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix rotated = matrix.rotate180();
     * // rotated is {{4, 3}, {2, 1}}
     * }</pre>
     *
     * @return a new matrix rotated 180 degrees
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
     * Returns a new matrix rotated 270 degrees clockwise (or 90 degrees counter-clockwise).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix rotated = matrix.rotate270();
     * // rotated is {{2, 4}, {1, 3}}
     * }</pre>
     *
     * @return a new matrix rotated 270 degrees clockwise
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
     * Returns a new matrix that is the transpose of this matrix.
     * The transpose swaps rows and columns.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
     * LongMatrix transposed = matrix.transpose();
     * // transposed is {{1, 4}, {2, 5}, {3, 6}}
     * }</pre>
     *
     * @return a new matrix that is the transpose of this matrix
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
     * Reshapes the matrix to new dimensions while preserving element order.
     * Elements are read in row-major order from the original matrix and placed into the new shape.
     *
     * <p>If the new shape has fewer total elements than the original, excess elements are truncated.
     * If the new shape has more total elements, the additional positions are filled with zeros.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
     * LongMatrix reshaped = matrix.reshape(3, 2); // Becomes [[1, 2], [3, 4], [5, 6]]
     * LongMatrix extended = matrix.reshape(2, 4); // Becomes [[1, 2, 3, 4], [5, 6, 0, 0]]
     * }</pre>
     *
     * @param newRows the number of rows in the reshaped matrix
     * @param newCols the number of columns in the reshaped matrix
     * @return a new LongMatrix with the specified shape containing this matrix's elements
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
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

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many rows: " + rows + " * " + rowRepeats);
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many columns: " + cols + " * " + colRepeats);
        }

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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
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

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many rows: " + rows + " * " + rowRepeats);
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many columns: " + cols + " * " + colRepeats);
        }

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
     * Returns a list containing all matrix elements in row-major order (row by row, left to right).
     * The elements are flattened into a single-dimensional list.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * LongList list = matrix.flatten(); // Returns LongList of 1L, 2L, 3L, 4L
     * }</pre>
     *
     * @return a new LongList containing all elements in row-major order
     * @throws IllegalStateException if the matrix is too large to flatten (total size exceeds Integer.MAX_VALUE)
     */
    @Override
    public LongList flatten() {
        // Check for overflow before allocation
        if ((long) rows * cols > Integer.MAX_VALUE) {
            throw new IllegalStateException("Matrix too large to flatten: " + rows + " x " + cols);
        }

        final long[] c = new long[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return LongList.of(c);
    }

    /**
     * Flattens the internal two-dimensional array into a one-dimensional array, applies an operation to it, then reshapes it back to the original two-dimensional structure.
     * This is useful for operations that need to be applied to all elements as a contiguous array,
     * such as sorting all elements or applying array-wide transformations that work on one-dimensional arrays.
     * <p><b>Note:</b> The matrix structure (rows and columns) is preserved after the operation completes.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{3, 1, 4}, {1, 5, 9}});
     * matrix.flatOp(arr -> N.sort(arr)); // Sorts all elements in-place
     * // Matrix structure is maintained but elements are reordered: e.g., [[1, 1, 3], [4, 5, 9]]
     * }</pre>
     *
     * @param <E> the type of exception that the operation may throw
     * @param op the operation to apply to the flattened one-dimensional array
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][] {{1, 2, 3}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][] {{4, 5, 6}, {7, 8, 9}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][] {{5}, {6}});
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
     * <p><b>Note:</b> Long overflow may occur during addition.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][] {{5, 6}, {7, 8}});
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
     * <p><b>Note:</b> Long overflow may occur during subtraction.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][] {{5, 6}, {7, 8}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
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
     * This implements standard matrix multiplication where each element (i,j) of the result is the
     * dot product of row i from this matrix and column j from matrix b.
     * <p><b>Note:</b> Long overflow may occur during multiplication.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][] {{5, 6}, {7, 8}});
     * LongMatrix product = matrix1.multiply(matrix2);
     * // Result: [[19, 22],   // 1*5+2*7=19, 1*6+2*8=22
     * //          [43, 50]]   // 3*5+4*7=43, 3*6+4*8=50
     * }</pre>
     *
     * @param b the matrix to multiply with this matrix
     * @return a new matrix containing the matrix product
     * @throws IllegalArgumentException if the matrix dimensions are incompatible (this.cols != b.rows)
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
     * Each primitive long value is boxed into a {@code Long} wrapper object.
     * This is the inverse operation of {@link #unbox(Matrix)}.
     *
     * <p><b>Note:</b> Boxing creates wrapper objects which have additional memory overhead compared to primitives.
     * Use this method only when you need to work with generic Matrix API or when null values are required.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix primitive = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * Matrix<Long> boxed = primitive.boxed();
     * // Result: Matrix containing Long wrapper objects instead of primitives
     * // Can now be used with generic Matrix<T> operations
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
     * <p><b>Warning:</b> Precision loss may occur for large long values. The float type has only 24 bits
     * of precision in its mantissa, so long values with absolute values greater than 2^24 (16,777,216)
     * may not be represented exactly. For example, {@code 16777217L} becomes {@code 16777216.0f}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix longMatrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * FloatMatrix floatMatrix = longMatrix.toFloatMatrix();
     * // Result: [[1.0f, 2.0f],
     * //          [3.0f, 4.0f]]
     *
     * // Be aware of precision loss for large values
     * LongMatrix large = LongMatrix.of(new long[][] {{16777217L}});
     * FloatMatrix converted = large.toFloatMatrix(); // May lose precision
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
     * Each long value is promoted to a double value.
     * <p><b>Note:</b> Very large long values (with absolute value greater than 2^53)
     * may lose precision when converted to double, since double has only 53 bits of precision
     * in its mantissa. For example, {@code Long.MAX_VALUE} (9223372036854775807L) cannot be
     * exactly represented as a double.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix longMatrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
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
     * The two matrices must have the same dimensions (same number of rows and columns).
     * For each position (i, j), the result contains {@code zipFunction.applyAsLong(this.get(i,j), matrixB.get(i,j))}.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][] {{5, 6}, {7, 8}});
     * LongMatrix max = matrix1.zipWith(matrix2, Math::max);
     * // Result: [[5, 6],
     * //          [7, 8]]
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix to zip with this matrix; must have the same dimensions
     * @param zipFunction the binary operation to apply to corresponding elements from this and matrixB
     * @return a new LongMatrix with the results of the zip operation
     * @throws IllegalArgumentException if the matrices don't have the same shape (rows and columns)
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
     * All three matrices must have the same dimensions (same number of rows and columns).
     * The function receives corresponding elements from all three matrices at each position.
     * For each position (i, j), the result contains {@code zipFunction.applyAsLong(this.get(i,j), matrixB.get(i,j), matrixC.get(i,j))}.
     * The operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix1 = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
     * LongMatrix matrix2 = LongMatrix.of(new long[][] {{5, 6}, {7, 8}});
     * LongMatrix matrix3 = LongMatrix.of(new long[][] {{9, 10}, {11, 12}});
     * LongMatrix average = matrix1.zipWith(matrix2, matrix3,
     *     (a, b, c) -> (a + b + c) / 3);
     * // Result: [[5, 6],   // (1+5+9)/3=5, (2+6+10)/3=6
     * //          [7, 8]]   // (3+7+11)/3=7, (4+8+12)/3=8
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix to zip with; must have the same dimensions as this matrix
     * @param matrixC the third matrix to zip with; must have the same dimensions as this matrix
     * @param zipFunction the ternary operation to apply to corresponding elements from this, matrixB, and matrixC
     * @return a new LongMatrix with the results of the zip operation
     * @throws IllegalArgumentException if the matrices don't have the same shape (rows and columns)
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, 
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
     * Creates a stream of elements on the diagonal from right-upper to left-down.
     * The matrix must be square (same number of rows and columns).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, 
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
     * Creates a stream of all elements in the matrix in row-major order (horizontally).
     * Elements are streamed row by row from left to right.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}, {5, 6}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
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
            public void advance(final long n) {
                if (n <= 0) {
                    return;
                }

                if (n >= (long) (toColumnIndex - j) * LongMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    final int offset = (int) (n + i);
                    i = offset % LongMatrix.this.rows;
                    j += offset / LongMatrix.this.rows;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}, {5, 6}});
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
     * Creates a stream of column streams, where each element is a stream of a complete column.
     * Columns are streamed in order from left to right.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
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
                    public void advance(final long n) {
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
     * This is an internal helper method used by the abstract matrix framework to determine
     * the length of a row array. It is part of the template method pattern implementation
     * in the abstract base class.
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
     * Elements are processed in row-major order (row by row, left to right).
     * This is equivalent to calling {@link #forEach(int, int, int, int, Throwables.LongConsumer)}
     * with the full matrix bounds.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2}, {3, 4}});
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
     * The operation may be performed in parallel for large sub-regions to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.forEach(1, 3, 1, 3, value -> System.out.print(value + " "));
     * // Output: 5 6 8 9 (elements from rows 1-2, columns 1-2)
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to apply to each element in the specified region
     * @throws IndexOutOfBoundsException if the indices are out of bounds [0, rows] or [0, cols], or if fromRowIndex &gt; toRowIndex or fromColumnIndex &gt; toColumnIndex
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.println();
     * // Output:
     * // [1, 2, 3]
     * // [4, 5, 6]
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

                    final long[] row = a[i];
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
     * Returns {@code true} if the given object is also a LongMatrix with the same dimensions
     * and all corresponding elements are equal.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix m1 = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * LongMatrix m2 = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
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

        if (obj instanceof final LongMatrix another) {
            return cols == another.cols && rows == another.rows && N.deepEquals(a, another.a);
        }

        return false;
    }

    /**
     * Returns a string representation of this matrix.
     * The format consists of matrix elements in a two-dimensional array format with rows enclosed in brackets.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix matrix = LongMatrix.of(new long[][] {{1L, 2L}, {3L, 4L}});
     * System.out.println(matrix.toString()); // [[1, 2], [3, 4]]
     * }</pre>
     *
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
