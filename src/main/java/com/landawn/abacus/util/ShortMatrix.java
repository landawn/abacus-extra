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
import com.landawn.abacus.util.u.OptionalShort;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.ShortIteratorEx;
import com.landawn.abacus.util.stream.ShortStream;
import com.landawn.abacus.util.stream.Stream;

/**
 * A matrix implementation for short primitive values, providing efficient storage and operations
 * for two-dimensional short arrays. This class extends AbstractMatrix and provides specialized
 * methods for short matrix manipulation including mathematical operations, transformations,
 * and element access.
 * 
 * <p>The matrix is internally represented as a two-dimensional short array (short[][]) and supports various
 * operations including arithmetic operations, matrix transformations (transpose, rotate, flip),
 * and element-wise operations.</p>
 * 
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Create a 3x3 matrix
 * short[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
 * ShortMatrix matrix = ShortMatrix.of(data);
 * 
 * // Get element at position (1, 2)
 * short value = matrix.get(1, 2);   // returns 6
 * 
 * // Transpose the matrix
 * ShortMatrix transposed = matrix.transpose();
 * 
 * // Stream all elements
 * matrix.streamH().forEach(System.out::println);
 * }</pre>
 * 
 * @see AbstractMatrix
 * @see BooleanMatrix
 * @see ByteMatrix
 * @see CharMatrix
 * @see IntMatrix
 * @see LongMatrix
 * @see FloatMatrix
 * @see DoubleMatrix
 * @see Matrix
 */
public final class ShortMatrix extends AbstractMatrix<short[], ShortList, ShortStream, Stream<ShortStream>, ShortMatrix> {

    static final ShortMatrix EMPTY_SHORT_MATRIX = new ShortMatrix(new short[0][0]);

    /**
     * Constructs a ShortMatrix from a two-dimensional short array.
     * If the input array is null, an empty matrix (0x0) is created.
     *
     * <p><b>Important:</b> The array is used directly without copying. This means:
     * <ul>
     * <li>Modifications to the input array after construction will affect the matrix</li>
     * <li>Modifications to the matrix will affect the original array</li>
     * <li>This provides better performance but less encapsulation</li>
     * </ul>
     * For a safe copy, use {@link #of(short[][])} or {@link #copy()} after construction.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] data = {{1, 2}, {3, 4}};
     * ShortMatrix matrix = new ShortMatrix(data);
     * data[0][0] = 99;  // This also changes matrix.get(0,0) to 99
     *
     * ShortMatrix empty = new ShortMatrix(null);   // Creates 0x0 empty matrix
     * }</pre>
     *
     * @param a the two-dimensional short array to wrap as a matrix. Can be null.
     */
    public ShortMatrix(final short[][] a) {
        super(a == null ? new short[0][0] : a);
    }

    /**
     * Creates an empty matrix with zero rows and zero columns.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.empty();
     * // matrix.rows returns 0
     * // matrix.cols returns 0
     * }</pre>
     *
     * @return an empty short matrix
     */
    public static ShortMatrix empty() {
        return EMPTY_SHORT_MATRIX;
    }

    /**
     * Creates a ShortMatrix from a two-dimensional short array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * // matrix.get(0, 1) returns 2
     * }</pre>
     *
     * @param a the two-dimensional short array to create the matrix from, or null/empty for an empty matrix
     * @return a new ShortMatrix containing the provided data, or an empty ShortMatrix if input is null or empty
     */
    public static ShortMatrix of(final short[]... a) {
        return N.isEmpty(a) ? EMPTY_SHORT_MATRIX : new ShortMatrix(a);
    }

    /**
     * Creates a 1-row matrix filled with random short values.
     * Each element is a random short value generated using the default random number generator.
     * The values can range across the entire short value space (from {@code Short.MIN_VALUE} to {@code Short.MAX_VALUE}).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.random(5);
     * // Creates a 1x5 matrix with random short values
     * // Each value is in range [Short.MIN_VALUE, Short.MAX_VALUE]
     * }</pre>
     *
     * @param len the number of columns (must be non-negative)
     * @return a 1-row matrix filled with random short values
     * @throws IllegalArgumentException if len is negative
     */
    @SuppressWarnings("deprecation")
    public static ShortMatrix random(final int len) {
        return new ShortMatrix(new short[][] { ShortList.random(len).array() });
    }

    /**
     * Creates a 1-row matrix with all elements set to the specified value.
     * This is useful for initializing matrices with a constant value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.repeat((short) 42, 5);
     * // Creates a 1x5 matrix: [[42, 42, 42, 42, 42]]
     *
     * ShortMatrix zeros = ShortMatrix.repeat((short) 0, 10);
     * // Creates a 1x10 matrix filled with zeros
     * }</pre>
     *
     * @param val the value to repeat
     * @param len the number of columns (must be non-negative)
     * @return a 1-row matrix with all elements set to val
     * @throws IllegalArgumentException if len is negative
     */
    public static ShortMatrix repeat(final short val, final int len) {
        return new ShortMatrix(new short[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a 1-row ShortMatrix with values from startInclusive to endExclusive.
     * The values are generated with a step of 1. If {@code startInclusive >= endExclusive}, an empty matrix is returned.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.range((short) 0, (short) 5);   // Creates [[0, 1, 2, 3, 4]]
     * ShortMatrix empty = ShortMatrix.range((short) 5, (short) 0);    // Creates an empty matrix
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @return a new 1×n ShortMatrix where n = max(0, endExclusive - startInclusive)
     */
    public static ShortMatrix range(final short startInclusive, final short endExclusive) {
        return new ShortMatrix(new short[][] { Array.range(startInclusive, endExclusive) });
    }

    /**
     * Creates a 1-row ShortMatrix with values from startInclusive to endExclusive with the specified step.
     * The step size can be positive (for ascending sequences) or negative (for descending sequences).
     * If the step would not reach endExclusive from startInclusive, an empty matrix is returned.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.range((short) 0, (short) 10, (short) 2);   // Creates [[0, 2, 4, 6, 8]]
     * ShortMatrix desc = ShortMatrix.range((short) 10, (short) 0, (short) -2);    // Creates [[10, 8, 6, 4, 2]]
     * ShortMatrix empty = ShortMatrix.range((short) 0, (short) 10, (short) -1);   // Creates an empty matrix (step is wrong direction)
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @param by the step size (must not be zero; can be positive or negative)
     * @return a new 1×n ShortMatrix with values incremented by the step size
     * @throws IllegalArgumentException if {@code by} is zero
     */
    public static ShortMatrix range(final short startInclusive, final short endExclusive, final short by) {
        return new ShortMatrix(new short[][] { Array.range(startInclusive, endExclusive, by) });
    }

    /**
     * Creates a 1-row ShortMatrix with values from startInclusive to endInclusive.
     * This method includes the end value, unlike {@link #range(short, short)}.
     * If {@code startInclusive > endInclusive}, an empty matrix is returned.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.rangeClosed((short) 0, (short) 4);   // Creates [[0, 1, 2, 3, 4]]
     * ShortMatrix single = ShortMatrix.rangeClosed((short) 5, (short) 5);   // Creates [[5]]
     * ShortMatrix empty = ShortMatrix.rangeClosed((short) 5, (short) 0);    // Creates an empty matrix
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive)
     * @return a new 1×n ShortMatrix where n = max(0, endInclusive - startInclusive + 1)
     */
    public static ShortMatrix rangeClosed(final short startInclusive, final short endInclusive) {
        return new ShortMatrix(new short[][] { Array.rangeClosed(startInclusive, endInclusive) });
    }

    /**
     * Creates a 1-row ShortMatrix with values from startInclusive to endInclusive with the specified step.
     * The step size can be positive (for ascending sequences) or negative (for descending sequences).
     * The end value is included only if it is reachable by stepping from start. If the step would not
     * reach endInclusive from startInclusive, an empty matrix is returned.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.rangeClosed((short) 0, (short) 8, (short) 2);    // Creates [[0, 2, 4, 6, 8]]
     * ShortMatrix partial = ShortMatrix.rangeClosed((short) 0, (short) 9, (short) 2);   // Creates [[0, 2, 4, 6, 8]] (9 not reachable)
     * ShortMatrix desc = ShortMatrix.rangeClosed((short) 10, (short) 0, (short) -2);    // Creates [[10, 8, 6, 4, 2, 0]]
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive, if reachable by stepping)
     * @param by the step size (must not be zero; can be positive or negative)
     * @return a new 1×n ShortMatrix with values incremented by the step size
     * @throws IllegalArgumentException if {@code by} is zero
     */
    public static ShortMatrix rangeClosed(final short startInclusive, final short endInclusive, final short by) {
        return new ShortMatrix(new short[][] { Array.rangeClosed(startInclusive, endInclusive, by) });
    }

    /**
     * Creates a square matrix from the specified main diagonal elements (left-upper to right-down).
     * All other elements (off-diagonal) are set to zero. The matrix size is n×n where n is the length
     * of the diagonal array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.diagonalLU2RD(new short[] {1, 2, 3});
     * // Creates a 3x3 matrix:
     * // [[1, 0, 0],
     * //  [0, 2, 0],
     * //  [0, 0, 3]]
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements (from top-left to bottom-right)
     * @return a square n×n matrix with the specified main diagonal, where n is the array length
     */
    public static ShortMatrix diagonalLU2RD(final short[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square matrix from the specified anti-diagonal elements (right-upper to left-down).
     * All other elements (off-diagonal) are set to zero. The matrix size is n×n where n is the length
     * of the diagonal array. The anti-diagonal runs from top-right to bottom-left.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.diagonalRU2LD(new short[] {1, 2, 3});
     * // Creates a 3x3 matrix:
     * // [[0, 0, 1],
     * //  [0, 2, 0],
     * //  [3, 0, 0]]
     * }</pre>
     *
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements (from top-right to bottom-left)
     * @return a square n×n matrix with the specified anti-diagonal, where n is the array length
     */
    public static ShortMatrix diagonalRU2LD(final short[] rightUp2LeftDownDiagonal) {
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
     * ShortMatrix matrix = ShortMatrix.diagonal(new short[] { 1, 2, 3 }, new short[] { 4, 5, 6 });
     * // Creates 3x3 matrix with both diagonals set
     * // Resulting matrix:
     * //   {1, 0, 4},
     * //   {0, 2, 0},
     * //   {6, 0, 3}
     *
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements (can be null or empty)
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements (can be null or empty)
     * @return a square matrix with the specified diagonals, or an empty matrix if both inputs are null or empty
     * @throws IllegalArgumentException if both arrays are non-empty and have different lengths
     */
    public static ShortMatrix diagonal(final short[] leftUp2RightDownDiagonal, final short[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        if (N.isEmpty(leftUp2RightDownDiagonal) && N.isEmpty(rightUp2LeftDownDiagonal)) {
            return EMPTY_SHORT_MATRIX;
        }

        final int matrixSize = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final short[][] result = new short[matrixSize][matrixSize];

        if (N.notEmpty(rightUp2LeftDownDiagonal)) {
            for (int i = 0, j = matrixSize - 1; i < matrixSize; i++, j--) {
                result[i][j] = rightUp2LeftDownDiagonal[i];
            }
        }

        if (N.notEmpty(leftUp2RightDownDiagonal)) {
            for (int i = 0; i < matrixSize; i++) {
                result[i][i] = leftUp2RightDownDiagonal[i]; // NOSONAR
            }
        }

        return new ShortMatrix(result);
    }

    /**
     * Converts a boxed {@code Matrix<Short>} to a primitive {@code ShortMatrix}.
     * This method unboxes all {@code Short} wrapper objects to primitive {@code short} values for more efficient
     * storage and operations. This is particularly beneficial when working with large matrices, as primitive
     * arrays have less memory overhead and better cache locality than arrays of wrapper objects.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<Short> boxedMatrix = Matrix.of(new Short[][] {{1, 2}, {3, 4}});
     * ShortMatrix primitiveMatrix = ShortMatrix.unbox(boxedMatrix);
     * // primitiveMatrix now uses primitive short[] arrays internally for better performance
     * }</pre>
     *
     * @param matrix the boxed Short matrix to convert
     * @return a new ShortMatrix with unboxed primitive values
     */
    public static ShortMatrix unbox(final Matrix<Short> matrix) {
        return ShortMatrix.of(Array.unbox(matrix.a));
    }

    /**
     * Returns the component type of the matrix elements.
     *
     * <p>This method returns the Class object representing the component type of the internal array,
     * which is always {@code short.class} for ShortMatrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * Class<?> type = matrix.componentType();   // Returns short.class
     * }</pre>
     *
     * @return {@code short.class}, the component type of the matrix elements
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return short.class;
    }

    /**
     * Returns the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * short value = matrix.get(0, 1);   // Returns 2
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position (i, j)
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public short get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Returns the element at the specified point.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * Point point = Point.of(0, 1);
     * short value = matrix.get(point);   // Returns 2
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @return the short element at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #get(int, int)
     */
    public short get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * matrix.set(0, 1, (short) 9);   // Sets element at row 0, column 1 to 9
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public void set(final int i, final int j, final short val) {
        a[i][j] = val;
    }

    /**
     * Sets the element at the specified point to the given value.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * Point point = Point.of(0, 1);
     * matrix.set(point, (short) 9);
     * assert matrix.get(point) == 9;
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @param val the new short value to set at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #set(int, int, short)
     */
    public void set(final Point point, final short val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element above the specified position, if it exists.
     * This method provides safe access to the element directly above the given position
     * without throwing an exception when at the top edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * OptionalShort value = matrix.upOf(1, 0);   // Returns OptionalShort.of((short)1)
     * OptionalShort empty = matrix.upOf(0, 0);   // Returns OptionalShort.empty() - no row above
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalShort containing the element at position (i-1, j), or empty if i == 0
     * @throws ArrayIndexOutOfBoundsException if i or j is out of bounds
     */
    public OptionalShort upOf(final int i, final int j) {
        checkRowColumnIndex(i, j);

        return i == 0 ? OptionalShort.empty() : OptionalShort.of(a[i - 1][j]);
    }

    /**
     * Returns the element below the specified position, if it exists.
     * This method provides safe access to the element directly below the given position
     * without throwing an exception when at the bottom edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * OptionalShort value = matrix.downOf(0, 0);   // Returns OptionalShort.of((short)3)
     * OptionalShort empty = matrix.downOf(1, 0);   // Returns OptionalShort.empty() - no row below
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalShort containing the element at position (i+1, j), or empty if i == rows-1
     * @throws ArrayIndexOutOfBoundsException if i or j is out of bounds
     */
    public OptionalShort downOf(final int i, final int j) {
        checkRowColumnIndex(i, j);

        return i == rows - 1 ? OptionalShort.empty() : OptionalShort.of(a[i + 1][j]);
    }

    /**
     * Returns the element to the left of the specified position, if it exists.
     * This method provides safe access to the element directly to the left of the given position
     * without throwing an exception when at the leftmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * OptionalShort value = matrix.leftOf(0, 1);   // Returns OptionalShort.of((short)1)
     * OptionalShort empty = matrix.leftOf(0, 0);   // Returns OptionalShort.empty() - no column to the left
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalShort containing the element at position (i, j-1), or empty if j == 0
     * @throws ArrayIndexOutOfBoundsException if i or j is out of bounds
     */
    public OptionalShort leftOf(final int i, final int j) {
        checkRowColumnIndex(i, j);

        return j == 0 ? OptionalShort.empty() : OptionalShort.of(a[i][j - 1]);
    }

    /**
     * Returns the element to the right of the specified position, if it exists.
     * This method provides safe access to the element directly to the right of the given position
     * without throwing an exception when at the rightmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * OptionalShort value = matrix.rightOf(0, 0);   // Returns OptionalShort.of((short)2)
     * OptionalShort empty = matrix.rightOf(0, 1);   // Returns OptionalShort.empty() - no column to the right
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalShort containing the element at position (i, j+1), or empty if j == cols-1
     * @throws ArrayIndexOutOfBoundsException if i or j is out of bounds
     */
    public OptionalShort rightOf(final int i, final int j) {
        checkRowColumnIndex(i, j);

        return j == cols - 1 ? OptionalShort.empty() : OptionalShort.of(a[i][j + 1]);
    }

    /**
     * Returns the specified row as a short array.
     *
     * <p><b>Note:</b> This method returns a reference to the internal array, not a copy.
     * Modifications to the returned array will affect the matrix. If you need an independent
     * copy, use {@code Arrays.copyOf(matrix.row(i), matrix.cols)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * short[] firstRow = matrix.row(0);   // Returns [1, 2, 3]
     *
     * // Direct modification affects the matrix
     * firstRow[0] = 10;  // matrix now has 10 at position (0,0)
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return the specified row array (direct reference to internal storage)
     * @throws IllegalArgumentException if rowIndex &lt; 0 or rowIndex &gt;= rows
     */
    public short[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Row index out of bounds: %s. Valid range is [0, %s)", rowIndex, rows);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as a new short array.
     *
     * <p>Unlike {@link #row(int)}, this method always returns a new array copy since
     * columns are not stored contiguously in memory. Modifications to the returned array
     * will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * short[] firstColumn = matrix.column(0);   // Returns [1, 4]
     *
     * // Modification does NOT affect the matrix (it's a copy)
     * firstColumn[0] = 10;  // matrix still has 1 at position (0,0)
     * }</pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing the values from the specified column
     * @throws IllegalArgumentException if columnIndex &lt; 0 or columnIndex &gt;= cols
     */
    public short[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Column index out of bounds: %s. Valid range is [0, %s)", columnIndex, cols);

        final short[] result = new short[rows];

        for (int i = 0; i < rows; i++) {
            result[i] = a[i][columnIndex];
        }

        return result;
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
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.setRow(0, new short[] {7, 8, 9});   // First row is now [7, 8, 9]
     * }</pre>
     *
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to copy into the row; must have length equal to the number of columns
     * @throws IllegalArgumentException if rowIndex is out of bounds or row length does not match column count
     */
    public void setRow(final int rowIndex, final short[] row) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Row index out of bounds: %s. Valid range is [0, %s)", rowIndex, rows);
        N.checkArgument(row.length == cols, "Row length mismatch: expected %s columns but got %s", cols, row.length);

        N.copy(row, 0, a[rowIndex], 0, cols);
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
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.setColumn(0, new short[] {7, 8});   // First column is now [7, 8]
     * }</pre>
     *
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to copy into the column; must have length equal to the number of rows
     * @throws IllegalArgumentException if columnIndex is out of bounds or column length does not match row count
     */
    public void setColumn(final int columnIndex, final short[] column) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Column index out of bounds: %s. Valid range is [0, %s)", columnIndex, cols);
        N.checkArgument(column.length == rows, "Column length mismatch: expected %s rows but got %s", rows, column.length);

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all elements in the specified row by applying the given function to each element.
     * The matrix is modified in-place. Each element in the row is transformed by the function
     * and replaced with the result.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.updateRow(0, x -> (short)(x * 2));   // First row becomes [2, 4, 6]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param rowIndex the index of the row to update (0-based)
     * @param func the unary operator to apply to each element in the row, taking a short and returning a short
     * @throws E if the function throws an exception
     * @throws ArrayIndexOutOfBoundsException if rowIndex is out of bounds
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.ShortUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsShort(a[rowIndex][i]);
        }
    }

    /**
     * Updates all elements in the specified column by applying the given function to each element.
     * The matrix is modified in-place. Each element in the column is transformed by the function
     * and replaced with the result.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.updateColumn(1, x -> (short)(x + 10));   // Second column becomes [12, 15]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param columnIndex the index of the column to update (0-based)
     * @param func the unary operator to apply to each element in the column, taking a short and returning a short
     * @throws E if the function throws an exception
     * @throws ArrayIndexOutOfBoundsException if columnIndex is out of bounds
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.ShortUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsShort(a[i][columnIndex]);
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
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * short[] diagonal = matrix.getLU2RD();   // Returns [1, 5, 9]
     * }</pre>
     *
     * @return a new short array containing the main diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public short[] getLU2RD() throws IllegalStateException {
        checkIfRowAndColumnSizeAreSame();

        final short[] result = new short[rows];

        for (int i = 0; i < rows; i++) {
            result[i] = a[i][i]; // NOSONAR
        }

        return result;
    }

    /**
     * Sets the elements on the main diagonal from left-upper to right-down (main diagonal).
     * The matrix must be square (rows == columns), and the diagonal array must have
     * exactly as many elements as the matrix has rows.
     *
     * <p>This method sets the main diagonal elements at positions (0,0), (1,1), (2,2), etc.
     * The diagonal array length must exactly match the number of rows (and columns) in the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * matrix.setLU2RD(new short[] {9, 8});
     * // Diagonal is now [9, 8]
     * }</pre>
     *
     * @param diagonal the new values for the main diagonal; must have length == rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length does not equal to rows
     */
    public void setLU2RD(final short[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length == rows, "Diagonal array length must equal matrix size: expected %s but got %s", rows, diagonal.length);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates all elements on the main diagonal (left-up to right-down) by applying the given function.
     * The matrix must be square (same number of rows and columns).
     * The matrix is modified in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.updateLU2RD(x -> (short)(x * 2));   // Diagonal [1, 5, 9] becomes [2, 10, 18]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square (rows != cols)
     */
    public <E extends Exception> void updateLU2RD(final Throwables.ShortUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsShort(a[i][i]);
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
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * short[] diagonal = matrix.getRU2LD();   // Returns [3, 5, 7]
     * }</pre>
     *
     * @return a new short array containing the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public short[] getRU2LD() throws IllegalStateException {
        checkIfRowAndColumnSizeAreSame();

        final short[] result = new short[rows];

        for (int i = 0; i < rows; i++) {
            result[i] = a[i][cols - i - 1];
        }

        return result;
    }

    /**
     * Sets the elements on the anti-diagonal from right-upper to left-down (anti-diagonal).
     * The matrix must be square (rows == columns), and the diagonal array must have
     * exactly as many elements as the matrix has rows.
     *
     * <p>This method sets the anti-diagonal (secondary diagonal) elements from
     * top-right to bottom-left, at positions (0,n-1), (1,n-2), (2,n-3), etc.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * matrix.setRU2LD(new short[] {9, 8});
     * // Anti-diagonal is now [9, 8]
     * }</pre>
     *
     * @param diagonal the new values for the anti-diagonal; must have length equal to rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length != rows
     */
    public void setRU2LD(final short[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length == rows, "Diagonal array length must equal matrix size: expected %s but got %s", rows, diagonal.length);

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = diagonal[i];
        }
    }

    /**
     * Updates all elements on the anti-diagonal (right-up to left-down) by applying the given function.
     * The matrix must be square (same number of rows and columns).
     * The matrix is modified in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.updateRU2LD(x -> (short)(x + 1));   // Anti-diagonal [3, 5, 7] becomes [4, 6, 8]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each anti-diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square (rows != cols)
     */
    public <E extends Exception> void updateRU2LD(final Throwables.ShortUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsShort(a[i][cols - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix by applying the given function to each element.
     * The matrix is modified in-place. This operation may be performed in parallel for large matrices
     * to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * matrix.updateAll(x -> (short)(x * 2));   // All elements are doubled: [[2, 4], [6, 8]]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each element, taking a short and returning a short
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.ShortUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> operation = (i, j) -> a[i][j] = func.applyAsShort(a[i][j]);
        Matrixes.run(rows, cols, operation, Matrixes.isParallelable(this));
    }

    /**
     * Updates all elements in the matrix based on their position by applying the given function.
     * The function receives the row and column indices (0-based) and returns the new value for that position.
     * The matrix is modified in-place. This operation may be performed in parallel for large matrices
     * to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * matrix.updateAll((i, j) -> (short)(i + j));   // Element at (i,j) becomes i+j: [[0, 1], [1, 2]]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the bi-function that takes (rowIndex, columnIndex) and returns the new short value
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Short, E> func) throws E {
        final Throwables.IntBiConsumer<E> operation = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, operation, Matrixes.isParallelable(this));
    }

    /**
     * Conditionally replaces elements in the matrix based on a predicate.
     * Each element that satisfies the predicate is replaced with the specified new value.
     * The matrix is modified in-place. This operation may be performed in parallel for large matrices
     * to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.replaceIf(x -> x > 3, (short)0);   // Result: [[1, 2, 3], [0, 0, 0]]
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition to test each element; returns {@code true} if the element should be replaced
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.ShortPredicate<E> predicate, final short newValue) throws E {
        final Throwables.IntBiConsumer<E> operation = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, operation, Matrixes.isParallelable(this));
    }

    /**
     * Conditionally replaces elements in the matrix based on their position.
     * The predicate receives the row and column indices (0-based) and returns {@code true} if the element
     * at that position should be replaced with the new value. The matrix is modified in-place.
     * This operation may be performed in parallel for large matrices to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.replaceIf((i, j) -> i == j, (short)0);   // Replace diagonal: [[0, 2, 3], [4, 0, 6], [7, 8, 0]]
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the bi-predicate that takes (rowIndex, columnIndex) and returns {@code true} if element should be replaced
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final short newValue) throws E {
        final Throwables.IntBiConsumer<E> operation = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, operation, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new matrix by applying the given function to each element of this matrix.
     * The original matrix is not modified. This operation may be performed in parallel for large matrices
     * to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix squared = matrix.map(x -> (short)(x * x));   // Result: [[1, 4], [9, 16]]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each element, taking a short and returning a short
     * @return a new ShortMatrix with the transformed values; the original matrix is unchanged
     * @throws E if the function throws an exception
     */
    public <E extends Exception> ShortMatrix map(final Throwables.ShortUnaryOperator<E> func) throws E {
        final short[][] result = new short[rows][cols];
        final Throwables.IntBiConsumer<E> operation = (i, j) -> result[i][j] = func.applyAsShort(a[i][j]);

        Matrixes.run(rows, cols, operation, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Creates a new object matrix by applying the given function to each element of this matrix.
     * The function transforms each primitive short value to an object of the specified type.
     * The original matrix is not modified. This operation may be performed in parallel for large matrices
     * to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * Matrix<String> stringMatrix = matrix.mapToObj(x -> "Value: " + x, String.class);
     * // Result: [["Value: 1", "Value: 2"], ["Value: 3", "Value: 4"]]
     * }</pre>
     *
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the type of exception that the function may throw
     * @param func the function to transform each short to an object of type T
     * @param targetElementType the class of the target element type (used for array creation)
     * @return a new Matrix&lt;T&gt; with the transformed object values; the original matrix is unchanged
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.ShortFunction<? extends T, E> func, final Class<T> targetElementType) throws E {
        final T[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> operation = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, operation, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Fills all elements of the matrix with the specified value.
     * The matrix is modified in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * matrix.fill((short)5);   // Result: [[5, 5], [5, 5]]
     * }</pre>
     *
     * @param val the value to fill the matrix with
     */
    public void fill(final short val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills the matrix with values from another two-dimensional array, starting at position (0, 0).
     * The source array can be smaller than this matrix; only the overlapping region is copied.
     * If the source array is larger, only the portion that fits is copied. The matrix is modified in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{0, 0, 0}, {0, 0, 0}});
     * matrix.fill(new short[][] {{1, 2}, {3, 4}});
     * // Result: [[1, 2, 0], [3, 4, 0]]
     * }</pre>
     *
     * @param b the two-dimensional array to copy values from
     */
    public void fill(final short[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a region of the matrix with values from another two-dimensional array, starting at the specified position.
     * The source array can extend beyond this matrix's bounds; only the overlapping region is copied.
     * The matrix is modified in-place. Elements outside the matrix bounds are ignored.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
     * matrix.fill(1, 1, new short[][] {{1, 2}, {3, 4}});
     * // Result: [[0, 0, 0], [0, 1, 2], [0, 3, 4]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index in this matrix (0-based, must be 0 &lt;= fromRowIndex &lt;= rows)
     * @param fromColumnIndex the starting column index in this matrix (0-based, must be 0 &lt;= fromColumnIndex &lt;= cols)
     * @param b the source array to copy values from
     * @throws IllegalArgumentException if fromRowIndex &lt; 0 or &gt; rows, or if fromColumnIndex &lt; 0 or &gt; cols
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final short[][] b) throws IllegalArgumentException {
        N.checkArgNotNull(b, cs.b);
        N.checkArgument(fromRowIndex >= 0 && fromRowIndex <= rows, "fromRowIndex(%s) must be between 0 and rows(%s)", fromRowIndex, rows);
        N.checkArgument(fromColumnIndex >= 0 && fromColumnIndex <= cols, "fromColumnIndex(%s) must be between 0 and cols(%s)", fromColumnIndex, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            if (b[i] != null) {
                N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
            }
        }
    }

    /**
     * Creates a deep copy of this matrix.
     *
     * <p>All elements are copied into a new matrix, so modifications to the copy
     * will not affect the original matrix and vice versa. This method performs a deep copy,
     * meaning both the outer array and all inner row arrays are cloned.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix original = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix copy = original.copy();
     * copy.set(0, 0, (short)99);   // original is unchanged
     * }</pre>
     *
     * @return a new ShortMatrix that is an independent deep copy of this matrix
     */
    @Override
    public ShortMatrix copy() {
        final short[][] result = new short[rows][];

        for (int i = 0; i < rows; i++) {
            result[i] = a[i].clone();
        }

        return new ShortMatrix(result);
    }

    /**
     * Creates a copy of a subset of rows from this matrix.
     *
     * <p>The returned matrix contains only the specified rows and is completely independent from the original matrix.
     * All columns from the selected rows are included in the copy.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}, {5, 6}});
     * ShortMatrix subset = matrix.copy(1, 3);   // Returns [[3, 4], [5, 6]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new ShortMatrix containing an independent copy of the specified rows
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows, or fromRowIndex &gt; toRowIndex
     */
    @Override
    public ShortMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final short[][] result = new short[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            result[i - fromRowIndex] = a[i].clone();
        }

        return new ShortMatrix(result);
    }

    /**
     * Creates a copy of a rectangular sub-region from this matrix.
     *
     * <p>The specified row and column ranges define the sub-matrix to copy.
     * The returned matrix is completely independent from the original.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * ShortMatrix region = matrix.copy(0, 2, 1, 3);   // Returns [[2, 3], [5, 6]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new ShortMatrix containing an independent copy of the specified rectangular region
     * @throws IndexOutOfBoundsException if any index is out of bounds or fromIndex &gt; toIndex
     */
    @Override
    public ShortMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final short[][] result = new short[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            result[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new ShortMatrix(result);
    }

    /**
     * Creates a new matrix by extending or truncating this matrix to the specified dimensions.
     * New cells are filled with {@code 0}.
     *
     * <p>If the new dimensions are smaller than the current dimensions, the matrix is truncated.
     * If larger, the existing content is preserved in the top-left corner and new cells are filled with 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix extended = matrix.extend(3, 3);
     * // Result: [[1, 2, 0],
     * //          [3, 4, 0],
     * //          [0, 0, 0]]
     * }</pre>
     *
     * @param newRows the number of rows in the new matrix. It can be smaller than the row number of the current matrix but must be non-negative
     * @param newCols the number of columns in the new matrix. It can be smaller than the column number of the current matrix but must be non-negative
     * @return a new ShortMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRows} or {@code newCols} is negative
     */
    public ShortMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, SHORT_0);
    }

    /**
     * Creates a new matrix by extending or truncating this matrix to the specified dimensions.
     * New cells created during extension are filled with the specified default value.
     *
     * <p>If the new dimensions are smaller than the current dimensions, the matrix is truncated
     * from the top-left corner. If larger, the existing content is preserved in the top-left
     * corner and new cells are filled with the specified default value. This method provides
     * more control over the fill value compared to {@link #extend(int, int)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix extended = matrix.extend(3, 4, (short) 9);   // Extend to 3x4, fill new cells with 9
     * // Result: [[1, 2, 9, 9],
     * //          [3, 4, 9, 9],
     * //          [9, 9, 9, 9]]
     *
     * // Truncate to smaller size
     * ShortMatrix truncated = matrix.extend(1, 1, (short) 0);   // Keep only top-left element
     * // Result: [[1]]
     * }</pre>
     *
     * @param newRows the number of rows in the new matrix. It can be smaller than the row number of the current matrix but must be non-negative
     * @param newCols the number of columns in the new matrix. It can be smaller than the column number of the current matrix but must be non-negative
     * @param defaultValueForNewCell the short value to fill new cells with during extension
     * @return a new ShortMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRows} or {@code newCols} is negative,
     *         or if the resulting matrix would be too large (dimensions exceeding Integer.MAX_VALUE elements)
     */
    public ShortMatrix extend(final int newRows, final int newCols, final short defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "newRows cannot be negative: %s", newRows);
        N.checkArgument(newCols >= 0, "newCols cannot be negative: %s", newCols);

        // Check for overflow before allocation
        if ((long) newRows * newCols > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Matrix dimensions overflow: " + newRows + " x " + newCols + " exceeds Integer.MAX_VALUE");
        }

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            final boolean fillDefaultValue = defaultValueForNewCell != SHORT_0;
            final short[][] result = new short[newRows][];

            for (int i = 0; i < newRows; i++) {
                result[i] = i < rows ? N.copyOf(a[i], newCols) : new short[newCols];

                if (fillDefaultValue) {
                    if (i >= rows) {
                        N.fill(result[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        N.fill(result[i], cols, newCols, defaultValueForNewCell);
                    }
                }
            }

            return new ShortMatrix(result);
        }
    }

    /**
     * Creates a new matrix by extending this matrix in all four directions.
     * New cells are filled with {@code 0}.
     *
     * <p>This method adds padding around the existing matrix, with the original content
     * positioned according to the specified padding amounts.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}});
     * ShortMatrix extended = matrix.extend(1, 1, 1, 1);
     * // Result: [[0, 0, 0, 0],
     * //          [0, 1, 2, 0],
     * //          [0, 0, 0, 0]]
     * }</pre>
     *
     * @param toUp number of rows to add above; must be non-negative
     * @param toDown number of rows to add below; must be non-negative
     * @param toLeft number of columns to add to the left; must be non-negative
     * @param toRight number of columns to add to the right; must be non-negative
     * @return a new extended ShortMatrix with dimensions ((toUp+rows+toDown) x (toLeft+cols+toRight))
     * @throws IllegalArgumentException if any parameter is negative
     */
    public ShortMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, SHORT_0);
    }

    /**
     * Creates a new matrix by extending this matrix in all four directions with padding.
     * New cells created during extension are filled with the specified default value.
     *
     * <p>This method adds padding around the existing matrix in all four directions
     * (up, down, left, right). The original matrix content is positioned according to
     * the padding amounts specified. This is particularly useful for operations like
     * border padding in image processing or creating margins around data.
     *
     * <p>The resulting matrix has dimensions:
     * <ul>
     *   <li>Rows: {@code toUp + this.rows + toDown}</li>
     *   <li>Columns: {@code toLeft + this.cols + toRight}</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}});
     * ShortMatrix padded = matrix.extend(1, 1, 2, 2, (short) 9);
     * // Result: [[9, 9, 9, 9, 9, 9],
     * //          [9, 9, 1, 2, 9, 9],
     * //          [9, 9, 9, 9, 9, 9]]
     *
     * // Add border of 0 values
     * ShortMatrix bordered = matrix.extend(1, 1, 1, 1, (short) 0);
     * // Result: [[0, 0, 0, 0],
     * //          [0, 1, 2, 0],
     * //          [0, 0, 0, 0]]
     * }</pre>
     *
     * @param toUp number of rows to add above; must be non-negative
     * @param toDown number of rows to add below; must be non-negative
     * @param toLeft number of columns to add to the left; must be non-negative
     * @param toRight number of columns to add to the right; must be non-negative
     * @param defaultValueForNewCell the short value to fill all new cells with
     * @return a new extended ShortMatrix with dimensions ((toUp+rows+toDown) x (toLeft+cols+toRight))
     * @throws IllegalArgumentException if any padding parameter is negative,
     *         or if the resulting dimensions would exceed Integer.MAX_VALUE
     */
    public ShortMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final short defaultValueForNewCell)
            throws IllegalArgumentException {
        N.checkArgument(toUp >= 0, "toUp cannot be negative: %s", toUp);
        N.checkArgument(toDown >= 0, "toDown cannot be negative: %s", toDown);
        N.checkArgument(toLeft >= 0, "toLeft cannot be negative: %s", toLeft);
        N.checkArgument(toRight >= 0, "toRight cannot be negative: %s", toRight);

        if (toUp == 0 && toDown == 0 && toLeft == 0 && toRight == 0) {
            return copy();
        } else {
            if ((long) toUp + rows + toDown > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Result row count overflow: " + toUp + " + " + rows + " + " + toDown + " exceeds Integer.MAX_VALUE");
            }

            if ((long) toLeft + cols + toRight > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Result column count overflow: " + toLeft + " + " + cols + " + " + toRight + " exceeds Integer.MAX_VALUE");
            }

            final int newRows = toUp + rows + toDown;
            final int newCols = toLeft + cols + toRight;
            final boolean fillDefaultValue = defaultValueForNewCell != SHORT_0;
            final short[][] result = new short[newRows][newCols];

            for (int i = 0; i < newRows; i++) {
                if (i >= toUp && i < toUp + rows) {
                    N.copy(a[i - toUp], 0, result[i], toLeft, cols);
                }

                if (fillDefaultValue) {
                    if (i < toUp || i >= toUp + rows) {
                        N.fill(result[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        if (toLeft > 0) {
                            N.fill(result[i], 0, toLeft, defaultValueForNewCell);
                        }

                        if (toRight > 0) {
                            N.fill(result[i], cols + toLeft, newCols, defaultValueForNewCell);
                        }
                    }
                }
            }

            return new ShortMatrix(result);
        }
    }

    /**
     * Reverses the order of elements in each row (horizontal flip in-place).
     * This operation modifies the matrix directly.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.reverseH();
     * // matrix is now [[3, 2, 1], [6, 5, 4]]
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
     * Reverses the order of rows in the matrix (vertical flip in-place).
     * This operation modifies the matrix directly by reversing the row order.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.reverseV();
     * // matrix is now [[7, 8, 9], [4, 5, 6], [1, 2, 3]]
     * }</pre>
     *
     * @see #flipV()
     */
    public void reverseV() {
        for (int j = 0; j < cols; j++) {
            short tmp = 0;
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
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * ShortMatrix flipped = matrix.flipH();
     * // Result: [[3, 2, 1],
     * //          [6, 5, 4]]
     * }</pre>
     * 
     * @return a new matrix with each row reversed
     * @see #flipV()
     * @see IntMatrix#flipH()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1</a>
     */
    public ShortMatrix flipH() {
        final ShortMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * Creates a new matrix that is vertically flipped (each column reversed).
     * The original matrix is not modified.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * ShortMatrix flipped = matrix.flipV();
     * // Result: [[4, 5, 6],
     * //          [1, 2, 3]]
     * }</pre>
     * 
     * @return a new matrix with rows in reversed order
     * @see #flipH()
     * @see IntMatrix#flipV()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1</a>
     */
    public ShortMatrix flipV() {
        final ShortMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * Creates a new matrix rotated 90 degrees clockwise.
     *
     * <p>The resulting matrix has dimensions swapped (rows × cols becomes cols × rows).
     * The element at position (i, j) in the original matrix appears at position (j, rows-1-i)
     * in the rotated matrix. The original matrix is not modified.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix rotated = matrix.rotate90();
     * // Result: [[3, 1],
     * //          [4, 2]]
     * }</pre>
     *
     * @return a new ShortMatrix rotated 90 degrees clockwise with dimensions cols × rows
     */
    @Override
    public ShortMatrix rotate90() {
        final short[][] result = new short[cols][rows];

        if (rows <= cols) {
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    result[i][j] = a[rows - j - 1][i];
                }
            }
        } else {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    result[i][j] = a[rows - j - 1][i];
                }
            }
        }

        return new ShortMatrix(result);
    }

    /**
     * Creates a new matrix rotated 180 degrees.
     *
     * <p>The resulting matrix has the same dimensions as the original. The element at position (i, j)
     * in the original matrix appears at position (rows-1-i, cols-1-j) in the rotated matrix.
     * This is equivalent to reversing both row order and element order within each row.
     * The original matrix is not modified.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix rotated = matrix.rotate180();
     * // Result: [[4, 3],
     * //          [2, 1]]
     * }</pre>
     *
     * @return a new ShortMatrix rotated 180 degrees with the same dimensions
     */
    @Override
    public ShortMatrix rotate180() {
        final short[][] result = new short[rows][];

        for (int i = 0; i < rows; i++) {
            result[i] = a[rows - i - 1].clone();
            N.reverse(result[i]);
        }

        return new ShortMatrix(result);
    }

    /**
     * Creates a new matrix rotated 270 degrees clockwise (or 90 degrees counter-clockwise).
     *
     * <p>The resulting matrix has dimensions swapped (rows × cols becomes cols × rows).
     * The element at position (i, j) in the original matrix appears at position (cols-1-j, i)
     * in the rotated matrix. The original matrix is not modified.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix rotated = matrix.rotate270();
     * // Result: [[2, 4],
     * //          [1, 3]]
     * }</pre>
     *
     * @return a new ShortMatrix rotated 270 degrees clockwise with dimensions cols × rows
     */
    @Override
    public ShortMatrix rotate270() {
        final short[][] result = new short[cols][rows];

        if (rows <= cols) {
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    result[i][j] = a[j][cols - i - 1];
                }
            }
        } else {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    result[i][j] = a[j][cols - i - 1];
                }
            }
        }

        return new ShortMatrix(result);
    }

    /**
     * Creates the transpose of this matrix by swapping rows and columns.
     *
     * <p>The transpose operation converts each row into a column, so element at position (i, j)
     * in the original matrix appears at position (j, i) in the transposed matrix. The resulting
     * matrix has dimensions swapped (rows × cols becomes cols × rows).
     * The original matrix is not modified.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:  Transposed:
     * // 1 2 3      1 4
     * // 4 5 6      2 5
     * //            3 6
     *
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * ShortMatrix transposed = matrix.transpose();   // 2×3 becomes 3×2
     * }</pre>
     *
     * @return a new ShortMatrix that is the transpose with dimensions cols × rows
     */
    @Override
    public ShortMatrix transpose() {
        final short[][] result = new short[cols][rows];

        if (rows <= cols) {
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    result[i][j] = a[j][i];
                }
            }
        } else {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    result[i][j] = a[j][i];
                }
            }
        }

        return new ShortMatrix(result);
    }

    /**
     * Reshapes the matrix to new dimensions while preserving element order in row-major layout.
     *
     * <p>Elements are read in row-major order from the original matrix and placed into the new shape.
     * If the new shape has fewer total elements than the original, excess elements are truncated.
     * If the new shape has more total elements, the additional positions are filled with zeros (default value for short).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * ShortMatrix reshaped = matrix.reshape(3, 2);   // Becomes [[1, 2], [3, 4], [5, 6]]
     * ShortMatrix extended = matrix.reshape(2, 4);   // Becomes [[1, 2, 3, 4], [5, 6, 0, 0]]
     * }</pre>
     *
     * @param newRows the number of rows in the reshaped matrix (must be non-negative)
     * @param newCols the number of columns in the reshaped matrix (must be non-negative)
     * @return a new ShortMatrix with the specified shape containing this matrix's elements in row-major order
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public ShortMatrix reshape(final int newRows, final int newCols) {
        final short[][] result = new short[newRows][newCols];

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new ShortMatrix(result);
        }

        final int rowLen = (int) N.min(newRows, count % newCols == 0 ? count / newCols : count / newCols + 1);

        if (a.length == 1) {
            for (int i = 0; i < rowLen; i++) {
                N.copy(a[0], i * newCols, result[i], 0, (int) N.min(newCols, count - (long) i * newCols));
            }
        } else {
            long cnt = 0;

            for (int i = 0; i < rowLen; i++) {
                for (int j = 0, col = (int) N.min(newCols, count - (long) i * newCols); j < col; j++, cnt++) {
                    result[i][j] = a[(int) (cnt / cols)][(int) (cnt % cols)];
                }
            }
        }

        return new ShortMatrix(result);
    }

    /**
     * Repeats each element in the matrix by the specified factors.
     *
     * <p>Each element is repeated {@code rowRepeats} times in the row direction and {@code colRepeats}
     * times in the column direction. This creates a new matrix where each original element becomes
     * a block of size rowRepeats × colRepeats. The resulting matrix has dimensions
     * (rows * rowRepeats) × (cols * colRepeats). The original matrix is not modified.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix repeated = matrix.repelem(2, 3);
     * // Result: [[1, 1, 1, 2, 2, 2],
     * //          [1, 1, 1, 2, 2, 2],
     * //          [3, 3, 3, 4, 4, 4],
     * //          [3, 3, 3, 4, 4, 4]]
     * }</pre>
     *
     * @param rowRepeats the number of times to repeat each element in the row direction (must be positive)
     * @param colRepeats the number of times to repeat each element in the column direction (must be positive)
     * @return a new ShortMatrix with dimensions (rows * rowRepeats) × (cols * colRepeats)
     * @throws IllegalArgumentException if {@code rowRepeats} or {@code colRepeats} is less than or equal to 0
     * @see IntMatrix#repelem(int, int)
     */
    @Override
    public ShortMatrix repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats and colRepeats must be positive: rowRepeats=%s, colRepeats=%s", rowRepeats, colRepeats);

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result row count overflow: " + rows + " * " + rowRepeats + " exceeds Integer.MAX_VALUE");
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result column count overflow: " + cols + " * " + colRepeats + " exceeds Integer.MAX_VALUE");
        }

        final short[][] result = new short[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            final short[] sourceRow = a[i];
            final short[] firstRepeatedRow = result[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                N.copy(Array.repeat(sourceRow[j], colRepeats), 0, firstRepeatedRow, j * colRepeats, colRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(firstRepeatedRow, 0, result[i * rowRepeats + k], 0, firstRepeatedRow.length);
            }
        }

        return new ShortMatrix(result);
    }

    /**
     * Repeats the entire matrix as a tile pattern.
     *
     * <p>The whole matrix is repeated {@code rowRepeats} times in the row direction and {@code colRepeats}
     * times in the column direction, creating a tiled pattern. The resulting matrix has dimensions
     * (rows * rowRepeats) × (cols * colRepeats). This is different from {@link #repelem(int, int)} which
     * repeats individual elements. The original matrix is not modified.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix tiled = matrix.repmat(2, 3);
     * // Result: [[1, 2, 1, 2, 1, 2],
     * //          [3, 4, 3, 4, 3, 4],
     * //          [1, 2, 1, 2, 1, 2],
     * //          [3, 4, 3, 4, 3, 4]]
     * }</pre>
     *
     * @param rowRepeats the number of times to repeat the matrix in the row direction (must be positive)
     * @param colRepeats the number of times to repeat the matrix in the column direction (must be positive)
     * @return a new ShortMatrix with dimensions (rows * rowRepeats) × (cols * colRepeats) containing the tiled pattern
     * @throws IllegalArgumentException if {@code rowRepeats} or {@code colRepeats} is less than or equal to 0
     * @see IntMatrix#repmat(int, int)
     * @see #repelem(int, int)
     */
    @Override
    public ShortMatrix repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats and colRepeats must be positive: rowRepeats=%s, colRepeats=%s", rowRepeats, colRepeats);

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result row count overflow: " + rows + " * " + rowRepeats + " exceeds Integer.MAX_VALUE");
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result column count overflow: " + cols + " * " + colRepeats + " exceeds Integer.MAX_VALUE");
        }

        final short[][] result = new short[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colRepeats; j++) {
                N.copy(a[i], 0, result[i], j * cols, cols);
            }
        }

        for (int i = 1; i < rowRepeats; i++) {
            for (int j = 0; j < rows; j++) {
                N.copy(result[j], 0, result[i * rows + j], 0, result[j].length);
            }
        }

        return new ShortMatrix(result);
    }

    /**
     * Flattens the matrix into a one-dimensional list in row-major order.
     *
     * <p>Elements are read row by row from left to right, top to bottom, and collected into a single
     * ShortList. The original matrix is not modified. This operation is useful for converting the
     * two-dimensional structure into a linear sequence for processing or transmission.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortList list = matrix.flatten();   // Returns ShortList containing [1, 2, 3, 4]
     * }</pre>
     *
     * @return a new ShortList containing all elements in row-major order
     * @throws IllegalStateException if the matrix is too large to flatten (more than Integer.MAX_VALUE elements)
     */
    @Override
    public ShortList flatten() {
        // Check for overflow before allocation
        if ((long) rows * cols > Integer.MAX_VALUE) {
            throw new IllegalStateException("Matrix too large to flatten: " + rows + " x " + cols);
        }

        final short[] result = new short[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, result, i * cols, cols);
        }

        return ShortList.of(result);
    }

    /**
     * Applies an operation to each row array of the underlying two-dimensional array.
     *
     * <p>This method iterates through each row and passes the internal row array (not a copy) to the operation.
     * Any modifications made to the row arrays by the operation will directly affect the matrix.
     * This method is useful for performing in-place operations on rows, such as sorting.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{3, 1, 2}, {6, 4, 5}});
     * matrix.flatOp(row -> java.util.Arrays.sort(row));
     * // matrix is now [[1, 2, 3], [4, 5, 6]]
     * }</pre>
     *
     * @param <E> the type of exception that the operation may throw
     * @param op the operation to apply to each row array
     * @throws E if the operation throws an exception
     * @see Arrays#flatOp(short[][], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super short[], E> op) throws E {
        Arrays.flatOp(a, op);
    }

    /**
     * Vertically stacks this matrix with another matrix.
     * The two matrices must have the same number of columns.
     * The result is a new matrix where the rows of the specified matrix are appended below the rows of this matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][] {{1, 2, 3}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][] {{4, 5, 6}, {7, 8, 9}});
     * ShortMatrix stacked = matrix1.vstack(matrix2);
     * // Result: [[1, 2, 3],
     * //          [4, 5, 6],
     * //          [7, 8, 9]]
     * }</pre>
     *
     * @param other the matrix to stack below this matrix
     * @return a new matrix with rows from both matrices stacked vertically
     * @throws IllegalArgumentException if the matrices don't have the same number of columns
     * @see IntMatrix#vstack(IntMatrix)
     */
    public ShortMatrix vstack(final ShortMatrix other) throws IllegalArgumentException {
        N.checkArgument(cols == other.cols, "Column count mismatch for vstack: this matrix has %s columns but other has %s", cols, other.cols);

        final short[][] result = new short[rows + other.rows][];
        int resultRowIndex = 0;

        for (int i = 0; i < rows; i++) {
            result[resultRowIndex++] = a[i].clone();
        }

        for (int i = 0; i < other.rows; i++) {
            result[resultRowIndex++] = other.a[i].clone();
        }

        return ShortMatrix.of(result);
    }

    /**
     * Horizontally stacks this matrix with another matrix.
     * The two matrices must have the same number of rows.
     * The result is a new matrix where the columns of the specified matrix are appended to the right of this matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][] {{5}, {6}});
     * ShortMatrix stacked = matrix1.hstack(matrix2);
     * // Result: [[1, 2, 5],
     * //          [3, 4, 6]]
     * }</pre>
     *
     * @param other the matrix to stack to the right of this matrix
     * @return a new matrix with columns from both matrices stacked horizontally
     * @throws IllegalArgumentException if the matrices don't have the same number of rows
     * @see IntMatrix#hstack(IntMatrix)
     */
    public ShortMatrix hstack(final ShortMatrix other) throws IllegalArgumentException {
        N.checkArgument(rows == other.rows, "Row count mismatch for hstack: this matrix has %s rows but other has %s", rows, other.rows);

        final short[][] result = new short[rows][cols + other.cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, result[i], 0, cols);
            N.copy(other.a[i], 0, result[i], cols, other.cols);
        }

        return ShortMatrix.of(result);
    }

    /**
     * Performs element-wise addition of this matrix with another matrix.
     * The two matrices must have the same dimensions (same number of rows and columns).
     * The original matrices are not modified.
     * <p><b>Note:</b> Short overflow may occur during addition. Values exceeding Short.MAX_VALUE will wrap around.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][] {{5, 6}, {7, 8}});
     * ShortMatrix sum = matrix1.add(matrix2);
     * // Result: [[6, 8], [10, 12]]
     * }</pre>
     *
     * @param other the matrix to add to this matrix (must have same dimensions)
     * @return a new matrix containing the element-wise sum
     * @throws IllegalArgumentException if the matrices don't have the same shape (same rows and columns)
     */
    public ShortMatrix add(final ShortMatrix other) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, other), "Cannot add matrices with different shapes: this is %sx%s but other is %sx%s", rows, cols,
                other.rows, other.cols);

        final short[][] otherArray = other.a;
        final short[][] result = new short[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> operation = (i, j) -> result[i][j] = (short) (a[i][j] + otherArray[i][j]);

        Matrixes.run(rows, cols, operation, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Performs element-wise subtraction of another matrix from this matrix.
     * The two matrices must have the same dimensions (same number of rows and columns).
     * The original matrices are not modified.
     * <p><b>Note:</b> Short underflow may occur during subtraction. Values below Short.MIN_VALUE will wrap around.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][] {{5, 6}, {7, 8}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix diff = matrix1.subtract(matrix2);
     * // Result: [[4, 4], [4, 4]]
     * }</pre>
     *
     * @param other the matrix to subtract from this matrix (must have same dimensions)
     * @return a new matrix containing the element-wise difference (this - other)
     * @throws IllegalArgumentException if the matrices don't have the same shape (same rows and columns)
     */
    public ShortMatrix subtract(final ShortMatrix other) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, other), "Cannot subtract matrices with different shapes: this is %sx%s but other is %sx%s", rows, cols,
                other.rows, other.cols);

        final short[][] otherArray = other.a;
        final short[][] result = new short[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> operation = (i, j) -> result[i][j] = (short) (a[i][j] - otherArray[i][j]);

        Matrixes.run(rows, cols, operation, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Performs standard matrix multiplication with another matrix.
     * The number of columns in this matrix must equal the number of rows in the specified matrix.
     * The result is a new matrix with dimensions (this.rows × b.cols).
     * The original matrices are not modified.
     * <p><b>Note:</b> Short overflow may occur during multiplication. This performs standard matrix multiplication,
     * not element-wise multiplication.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][] {{5, 6}, {7, 8}});
     * ShortMatrix product = matrix1.multiply(matrix2);
     * // Result: [[19, 22], [43, 50]]
     * // where result[i][j] = sum of (matrix1[i][k] * matrix2[k][j]) for all k
     * }</pre>
     *
     * @param b the matrix to multiply with this matrix (this.cols must equal b.rows)
     * @return a new matrix of dimension (this.rows × b.cols) containing the matrix product
     * @throws IllegalArgumentException if this.cols != b.rows (incompatible dimensions for multiplication)
     */
    public ShortMatrix multiply(final ShortMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.rows, "Matrix dimensions incompatible for multiplication: this is %sx%s, other is %sx%s (this.cols must equal other.rows)",
                rows, cols, b.rows, b.cols);

        final short[][] ba = b.a;
        final short[][] result = new short[rows][b.cols];
        final Throwables.IntTriConsumer<RuntimeException> cmd = (i, j, k) -> result[i][j] += (short) (a[i][k] * ba[k][j]);

        Matrixes.multiply(this, b, cmd);

        return new ShortMatrix(result);
    }

    /**
     * Converts this primitive short matrix to a boxed {@code Matrix<Short>}.
     * Each primitive short value is boxed into a {@code Short} wrapper object.
     * This is the inverse operation of {@link #unbox(Matrix)}.
     *
     * <p><b>Note:</b> Boxing creates wrapper objects which have additional memory overhead compared to primitives.
     * Use this method only when you need to work with generic Matrix API or when null values are required.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix primitive = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * Matrix<Short> boxed = primitive.boxed();
     * // Result: Matrix containing Short wrapper objects instead of primitives
     * // Can now be used with generic Matrix<T> operations
     * }</pre>
     *
     * @return a new {@code Matrix<Short>} containing boxed values
     */
    public Matrix<Short> boxed() {
        final Short[][] c = new Short[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final short[] aa = a[i];
                final Short[] cc = c[i];

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
     * Converts this short matrix to an int matrix.
     * Each short value is promoted to an int value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix shortMatrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * IntMatrix intMatrix = shortMatrix.toIntMatrix();
     * // Result: [[1, 2],
     * //          [3, 4]] (as ints)
     * }</pre>
     *
     * @return a new {@code IntMatrix} with values converted from short to int
     */
    public IntMatrix toIntMatrix() {
        return IntMatrix.create(a);
    }

    /**
     * Converts this short matrix to a long matrix.
     * Each short value is promoted to a long value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix shortMatrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * LongMatrix longMatrix = shortMatrix.toLongMatrix();
     * // Result: [[1L, 2L],
     * //          [3L, 4L]]
     * }</pre>
     *
     * @return a new {@code LongMatrix} with values converted from short to long
     */
    public LongMatrix toLongMatrix() {
        final long[][] c = new long[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final short[] aa = a[i];
                final long[] cc = c[i];

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

        return new LongMatrix(c);
    }

    /**
     * Converts this short matrix to a float matrix.
     * Each short value is converted to a float value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix shortMatrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * FloatMatrix floatMatrix = shortMatrix.toFloatMatrix();
     * // Result: [[1.0f, 2.0f],
     * //          [3.0f, 4.0f]]
     * }</pre>
     *
     * @return a new {@code FloatMatrix} with values converted from short to float
     */
    public FloatMatrix toFloatMatrix() {
        final float[][] c = new float[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final short[] aa = a[i];
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
     * Converts this short matrix to a double matrix.
     * Each short value is converted to a double value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix shortMatrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * DoubleMatrix doubleMatrix = shortMatrix.toDoubleMatrix();
     * // Result: [[1.0, 2.0],
     * //          [3.0, 4.0]]
     * }</pre>
     *
     * @return a new {@code DoubleMatrix} with values converted from short to double
     */
    public DoubleMatrix toDoubleMatrix() {
        final double[][] c = new double[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final short[] aa = a[i];
                final double[] cc = c[i];

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

        return new DoubleMatrix(c);
    }

    /**
     * Applies a binary operation element-wise to this matrix and another matrix.
     * The two matrices must have the same dimensions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][] {{5, 6}, {7, 8}});
     * ShortMatrix max = matrix1.zipWith(matrix2, (a, b) -> (short)Math.max(a, b));
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
    public <E extends Exception> ShortMatrix zipWith(final ShortMatrix matrixB, final Throwables.ShortBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Cannot zip matrices with different shapes: this is %sx%s but other is %sx%s", rows, cols, matrixB.rows,
                matrixB.cols);

        final short[][] arrayB = matrixB.a;
        final short[][] result = new short[rows][cols];

        final Throwables.IntBiConsumer<E> operation = (i, j) -> result[i][j] = zipFunction.applyAsShort(a[i][j], arrayB[i][j]);

        Matrixes.run(rows, cols, operation, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Applies a ternary operation element-wise to this matrix and two other matrices.
     * All three matrices must have the same dimensions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][] {{5, 6}, {7, 8}});
     * ShortMatrix matrix3 = ShortMatrix.of(new short[][] {{9, 10}, {11, 12}});
     * ShortMatrix average = matrix1.zipWith(matrix2, matrix3,
     *     (a, b, c) -> (short)((a + b + c) / 3));
     * // Result: [[5, 6],
     * //          [7, 8]]
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix to zip with
     * @param matrixC the third matrix to zip with
     * @param zipFunction the ternary operation to apply to corresponding elements from all three matrices
     * @return a new matrix with the results of the zip operation
     * @throws IllegalArgumentException if the matrices don't have the same shape
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> ShortMatrix zipWith(final ShortMatrix matrixB, final ShortMatrix matrixC, final Throwables.ShortTernaryOperator<E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Cannot zip matrices with different shapes: all matrices must be %sx%s", rows, cols);

        final short[][] arrayB = matrixB.a;
        final short[][] arrayC = matrixC.a;
        final short[][] result = new short[rows][cols];

        final Throwables.IntBiConsumer<E> operation = (i, j) -> result[i][j] = zipFunction.applyAsShort(a[i][j], arrayB[i][j], arrayC[i][j]);

        Matrixes.run(rows, cols, operation, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Creates a stream of elements on the main diagonal from left-upper to right-down.
     *
     * <p>The matrix must be square (same number of rows and columns). The stream contains elements
     * at positions (0,0), (1,1), (2,2), ..., (n-1,n-1) where n is the matrix dimension.
     * This is the primary diagonal running from top-left to bottom-right.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3},
     *                                                   {4, 5, 6},
     *                                                   {7, 8, 9}});
     * ShortStream diagonal = matrix.streamLU2RD();
     * // Stream contains: 1, 5, 9
     * }</pre>
     *
     * @return a ShortStream of diagonal elements from left-upper to right-down
     * @throws IllegalStateException if the matrix is not square (rows != cols)
     */
    @Override
    public ShortStream streamLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return ShortStream.empty();
        }

        return ShortStream.of(new ShortIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public short nextShort() {
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
     * Creates a stream of elements on the anti-diagonal from right-upper to left-down.
     *
     * <p>The matrix must be square (same number of rows and columns). The stream contains elements
     * at positions (0,n-1), (1,n-2), (2,n-3), ..., (n-1,0) where n is the matrix dimension.
     * This is the secondary diagonal running from top-right to bottom-left.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3},
     *                                                   {4, 5, 6},
     *                                                   {7, 8, 9}});
     * ShortStream diagonal = matrix.streamRU2LD();
     * // Stream contains: 3, 5, 7
     * }</pre>
     *
     * @return a ShortStream of anti-diagonal elements from right-upper to left-down
     * @throws IllegalStateException if the matrix is not square (rows != cols)
     */
    @Override
    public ShortStream streamRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return ShortStream.empty();
        }

        return ShortStream.of(new ShortIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public short nextShort() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final short result = a[cursor][cols - cursor - 1];
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
     * Creates a stream of all elements in the matrix in row-major order (horizontally).
     *
     * <p>Elements are streamed row by row from left to right, top to bottom. This is the most common
     * streaming order for matrix traversal and corresponds to the natural iteration order of the
     * underlying two-dimensional array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * ShortStream stream = matrix.streamH();
     * // Stream contains: 1, 2, 3, 4, 5, 6
     * }</pre>
     *
     * @return a ShortStream of all matrix elements in row-major order
     */
    @Override
    public ShortStream streamH() {
        return streamH(0, rows);
    }

    /**
     * Creates a stream of elements from a specific row.
     *
     * <p>All elements in the specified row are streamed from left to right (column index 0 to cols-1).
     * This is equivalent to calling {@code streamH(rowIndex, rowIndex + 1)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * ShortStream row = matrix.streamH(1);
     * // Stream contains: 4, 5, 6
     * }</pre>
     *
     * @param rowIndex the index of the row to stream (0-based)
     * @return a ShortStream of elements from the specified row
     * @throws IndexOutOfBoundsException if the row index is out of bounds
     */
    @Override
    public ShortStream streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * Creates a stream of elements from a range of rows in row-major order.
     *
     * <p>Elements from the specified rows are streamed row by row from left to right, top to bottom.
     * Each complete row is streamed before moving to the next row within the specified range.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}, {5, 6}});
     * ShortStream stream = matrix.streamH(1, 3);
     * // Stream contains: 3, 4, 5, 6
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a ShortStream of elements from the specified row range in row-major order
     * @throws IndexOutOfBoundsException if the row indices are out of bounds or fromRowIndex &gt; toRowIndex
     */
    @Override
    public ShortStream streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return ShortStream.empty();
        }

        return ShortStream.of(new ShortIteratorEx() {
            private int i = fromRowIndex;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i < toRowIndex;
            }

            @Override
            public short nextShort() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final short result = a[i][j++];

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
                    j = (int) ((n + j) % cols);
                }
            }

            @Override
            public long count() {
                return (long) (toRowIndex - i) * cols - j;
            }

            @Override
            public short[] toArray() {
                final int arrayLength = (int) count();
                final short[] result = new short[arrayLength];

                for (int k = 0; k < arrayLength; k++) {
                    result[k] = a[i][j++];

                    if (j >= cols) {
                        i++;
                        j = 0;
                    }
                }

                return result;
            }
        });
    }

    /**
     * Creates a stream of all elements in the matrix in column-major order (vertically).
     *
     * <p>Elements are streamed column by column from top to bottom, left to right. This traversal
     * order processes all elements in the first column, then all elements in the second column, and so on.
     * This is the opposite of the more common row-major order used by {@link #streamH()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * ShortStream stream = matrix.streamV();
     * // Stream contains: 1, 4, 2, 5, 3, 6
     * }</pre>
     *
     * @return a ShortStream of all matrix elements in column-major order
     */
    @Override
    @Beta
    public ShortStream streamV() {
        return streamV(0, cols);
    }

    /**
     * Creates a stream of elements from a specific column.
     *
     * <p>All elements in the specified column are streamed from top to bottom (row index 0 to rows-1).
     * This is equivalent to calling {@code streamV(columnIndex, columnIndex + 1)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * ShortStream column = matrix.streamV(1);
     * // Stream contains: 2, 5
     * }</pre>
     *
     * @param columnIndex the index of the column to stream (0-based)
     * @return a ShortStream of elements from the specified column
     * @throws IndexOutOfBoundsException if the column index is out of bounds
     */
    @Override
    public ShortStream streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * Creates a stream of elements from a range of columns in column-major order.
     *
     * <p>Elements from the specified columns are streamed column by column from top to bottom, left to right.
     * Each complete column is streamed before moving to the next column within the specified range.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * ShortStream stream = matrix.streamV(1, 3);
     * // Stream contains: 2, 5, 3, 6
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a ShortStream of elements from the specified column range in column-major order
     * @throws IndexOutOfBoundsException if the column indices are out of bounds or fromColumnIndex &gt; toColumnIndex
     */
    @Override
    @Beta
    public ShortStream streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return ShortStream.empty();
        }

        return ShortStream.of(new ShortIteratorEx() {
            private int i = 0;
            private int j = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return j < toColumnIndex;
            }

            @Override
            public short nextShort() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final short result = a[i++][j];

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

                if (n >= (long) (toColumnIndex - j) * ShortMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    final long offset = n + i;
                    i = (int) (offset % ShortMatrix.this.rows);
                    j += (int) (offset / ShortMatrix.this.rows);
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public short[] toArray() {
                final int arrayLength = (int) count();
                final short[] result = new short[arrayLength];

                for (int k = 0; k < arrayLength; k++) {
                    result[k] = a[i++][j];

                    if (i >= rows) {
                        i = 0;
                        j++;
                    }
                }

                return result;
            }
        });
    }

    /**
     * Creates a stream of row streams, where each element is a stream representing a complete row.
     *
     * <p>Rows are streamed in order from top to bottom. This method is useful for processing the matrix
     * row-by-row where each row needs to be handled as a separate stream. Each row stream contains
     * all elements in that row from left to right.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * Stream<ShortStream> rows = matrix.streamR();
     * // First stream contains: 1, 2, 3
     * // Second stream contains: 4, 5, 6
     * }</pre>
     *
     * @return a Stream of ShortStream objects, one for each row
     */
    @Override
    public Stream<ShortStream> streamR() {
        return streamR(0, rows);
    }

    /**
     * Creates a stream of row streams from a range of rows.
     *
     * <p>Each element in the returned stream is a ShortStream representing a complete row within the
     * specified range. Rows are streamed in order within the range. Each row stream contains all
     * elements in that row from left to right.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}, {5, 6}});
     * Stream<ShortStream> rows = matrix.streamR(1, 3);
     * // First stream contains: 3, 4
     * // Second stream contains: 5, 6
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a Stream of ShortStream objects for rows in the specified range
     * @throws IndexOutOfBoundsException if the row indices are out of bounds or fromRowIndex &gt; toRowIndex
     */
    @Override
    public Stream<ShortStream> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
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
            public ShortStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return ShortStream.of(a[cursor++]);
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
     * Creates a stream of column streams, where each element is a stream representing a complete column.
     *
     * <p>Columns are streamed in order from left to right. This method is useful for processing the matrix
     * column-by-column where each column needs to be handled as a separate stream. Each column stream
     * contains all elements in that column from top to bottom.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * Stream<ShortStream> columns = matrix.streamC();
     * // First stream contains: 1, 4
     * // Second stream contains: 2, 5
     * // Third stream contains: 3, 6
     * }</pre>
     *
     * @return a Stream of ShortStream objects, one for each column
     */
    @Override
    @Beta
    public Stream<ShortStream> streamC() {
        return streamC(0, cols);
    }

    /**
     * Creates a stream of column streams from a range of columns.
     *
     * <p>Each element in the returned stream is a ShortStream representing a complete column within the
     * specified range. Columns are streamed in order within the range. Each column stream contains all
     * elements in that column from top to bottom.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
     * Stream<ShortStream> columns = matrix.streamC(1, 3);
     * // First stream contains: 2, 5
     * // Second stream contains: 3, 6
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a Stream of ShortStream objects for columns in the specified range
     * @throws IndexOutOfBoundsException if the column indices are out of bounds or fromColumnIndex &gt; toColumnIndex
     */
    @Override
    @Beta
    public Stream<ShortStream> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public ShortStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return ShortStream.of(new ShortIteratorEx() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rows;
                    private int cursor2 = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor2 < toIndex2;
                    }

                    @Override
                    public short nextShort() {
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
     * This is an internal helper method.
     *
     * @param a the array to get the length of
     * @return the length of the array, or 0 if the array is null
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final short[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * Applies the specified action to each element in the matrix.
     * Elements are processed in row-major order (left to right, top to bottom).
     * This operation may be performed in parallel for large matrices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * matrix.forEach(value -> System.out.print(value + " "));
     * // Output: 1 2 3 4
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the consumer to apply to each element
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> action) throws E {
        forEach(0, rows, 0, cols, action);
    }

    /**
     * Applies the specified action to each element in a rectangular sub-region of the matrix.
     * Elements are processed in row-major order (left to right, top to bottom) within the specified bounds.
     * This operation may be performed in parallel for large regions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.forEach(1, 3, 1, 3, value -> System.out.print(value + " "));
     * // Output: 5 6 8 9  (processes elements in rows 1-2, columns 1-2)
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the consumer to apply to each element in the region
     * @throws IndexOutOfBoundsException if any index is out of bounds or fromIndex &gt; toIndex
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.ShortConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> operation = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, operation, Matrixes.isParallelable(this));
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final short[] aa = a[i];

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
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2, 3}, {4, 5, 6}});
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

                    final short[] ai = a[i];
                    sb.append('[');

                    for (int j = 0, rowLen = ai.length; j < rowLen; j++) {
                        if (j > 0) {
                            sb.append(", ");
                        }

                        sb.append(ai[j]);
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
     * Returns {@code true} if the given object is also a ShortMatrix with the same dimensions
     * and all corresponding elements are equal.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortMatrix m1 = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * ShortMatrix m2 = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
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

        if (obj instanceof final ShortMatrix another) {
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
     * ShortMatrix matrix = ShortMatrix.of(new short[][] {{1, 2}, {3, 4}});
     * System.out.println(matrix.toString());   // [[1, 2], [3, 4]]
     * }</pre>
     *
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
