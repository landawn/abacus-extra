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
import com.landawn.abacus.util.u.OptionalByte;
import com.landawn.abacus.util.stream.ByteIteratorEx;
import com.landawn.abacus.util.stream.ByteStream;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

/**
 * A matrix implementation for byte primitive values, providing efficient storage and operations
 * for two-dimensional byte arrays. This class extends AbstractMatrix and provides specialized
 * methods for byte matrix manipulation including mathematical operations, transformations,
 * and element access.
 * 
 * <p>The matrix is stored as a 2D byte array and supports various operations such as:
 * <ul>
 * <li>Element access and modification</li>
 * <li>Matrix arithmetic (add, subtract, multiply)</li>
 * <li>Transformations (transpose, rotate, flip)</li>
 * <li>Reshaping and extending</li>
 * <li>Stream-based iteration</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>{@code
 * // Create a 3x3 matrix
 * byte[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
 * ByteMatrix matrix = ByteMatrix.of(data);
 * 
 * // Access elements
 * byte value = matrix.get(1, 2); // Gets value at row 1, column 2
 * 
 * // Perform operations
 * ByteMatrix transposed = matrix.transpose();
 * ByteMatrix rotated = matrix.rotate90();
 * }</pre>
 * 
 * @see AbstractMatrix
 * @see IntMatrix
 * @see DoubleMatrix
 */
public final class ByteMatrix extends AbstractMatrix<byte[], ByteList, ByteStream, Stream<ByteStream>, ByteMatrix> {

    static final ByteMatrix EMPTY_BYTE_MATRIX = new ByteMatrix(new byte[0][0]);

    /**
     * Constructs a ByteMatrix from a 2D byte array. If the input array is null,
     * an empty matrix is created. The array is used directly without copying,
     * so modifications to the array will affect the matrix.
     *
     * @param a the 2D byte array to wrap as a matrix
     */
    public ByteMatrix(final byte[][] a) {
        super(a == null ? new byte[0][0] : a);
    }

    /**
     * Returns an empty ByteMatrix with zero rows and columns.
     * This method returns a singleton instance for efficiency.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix empty = ByteMatrix.empty();
     * System.out.println(empty.rows()); // 0
     * System.out.println(empty.cols()); // 0
     * }</pre>
     *
     * @return an empty ByteMatrix instance
     */
    public static ByteMatrix empty() {
        return EMPTY_BYTE_MATRIX;
    }

    /**
     * Creates a ByteMatrix from one or more byte arrays. Each array becomes a row in the matrix.
     * If no arrays are provided or the input is null, returns an empty matrix.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(
     *     new byte[]{1, 2, 3},
     *     new byte[]{4, 5, 6}
     * );
     * // Creates a 2x3 matrix
     * }</pre>
     *
     * @param a the byte arrays to use as rows of the matrix
     * @return a new ByteMatrix containing the provided arrays as rows
     */
    public static ByteMatrix of(final byte[]... a) {
        return N.isEmpty(a) ? EMPTY_BYTE_MATRIX : new ByteMatrix(a);
    }

    /**
     * Creates a 1-row ByteMatrix with random byte values of the specified length.
     * The random values are generated using ByteList's random method.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix random = ByteMatrix.random(5);
     * // Creates a 1x5 matrix with random byte values
     * }</pre>
     *
     * @param len the number of columns for the matrix
     * @return a new 1xlen ByteMatrix with random byte values 
     */
    @SuppressWarnings("deprecation")
    public static ByteMatrix random(final int len) {
        return new ByteMatrix(new byte[][] { ByteList.random(len).array() });
    }

    /**
     * Creates a 1-row ByteMatrix filled with the specified value repeated for the given length.
     * @return a new 1xlen ByteMatrix filled with the specified value
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.repeat((byte)7, 4);
     * // Creates matrix: [[7, 7, 7, 7]]
     * }</pre>
     *
     * @param val the byte value to repeat
     * @param len the number of times to repeat the value
     */
    public static ByteMatrix repeat(final byte val, final int len) {
        return new ByteMatrix(new byte[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a 1-row ByteMatrix containing a range of byte values from startInclusive to endExclusive.
     * The range increments by 1 for each element.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix range = ByteMatrix.range((byte)1, (byte)5);
     * // Creates matrix: [[1, 2, 3, 4]]
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @return a new ByteMatrix containing the range of values
     */
    public static ByteMatrix range(final byte startInclusive, final byte endExclusive) {
        return new ByteMatrix(new byte[][] { Array.range(startInclusive, endExclusive) });
    }

    /**
     * Creates a 1-row ByteMatrix containing a range of byte values with a specified step.
     * The range starts at startInclusive, increments by the step value, and stops before endExclusive.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix range = ByteMatrix.range((byte)0, (byte)10, (byte)2);
     * // Creates matrix: [[0, 2, 4, 6, 8]]
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @param by the step size for incrementing values
     * @return a new ByteMatrix containing the range of values
     */
    public static ByteMatrix range(final byte startInclusive, final byte endExclusive, final byte by) {
        return new ByteMatrix(new byte[][] { Array.range(startInclusive, endExclusive, by) });
    }

    /**
     * Creates a 1-row ByteMatrix containing a closed range of byte values from startInclusive to endInclusive.
     * The range increments by 1 for each element and includes the end value.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix range = ByteMatrix.rangeClosed((byte)1, (byte)4);
     * // Creates matrix: [[1, 2, 3, 4]]
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive)
     * @return a new ByteMatrix containing the range of values
     */
    public static ByteMatrix rangeClosed(final byte startInclusive, final byte endInclusive) {
        return new ByteMatrix(new byte[][] { Array.rangeClosed(startInclusive, endInclusive) });
    }

    /**
     * Creates a 1-row ByteMatrix containing a closed range of byte values with a specified step.
     * The range includes both start and end values.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix range = ByteMatrix.rangeClosed((byte)0, (byte)9, (byte)3);
     * // Creates matrix: [[0, 3, 6, 9]]
     * }</pre>
     *
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive)
     * @param by the step size for incrementing values
     * @return a new ByteMatrix containing the range of values
     */
    public static ByteMatrix rangeClosed(final byte startInclusive, final byte endInclusive, final byte by) {
        return new ByteMatrix(new byte[][] { Array.rangeClosed(startInclusive, endInclusive, by) });
    }

    /**
     * Creates a square diagonal matrix with the specified values on the diagonal from left-up to right-down.
     * All other elements are set to 0.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix diagonal = ByteMatrix.diagonalLU2RD(new byte[]{1, 2, 3});
     * // Creates matrix:
     * // [[1, 0, 0],
     * //  [0, 2, 0],
     * //  [0, 0, 3]]
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the values for the main diagonal
     * @return a new square ByteMatrix with the specified diagonal values
     */
    public static ByteMatrix diagonalLU2RD(final byte[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square diagonal matrix with the specified values on the diagonal from right-up to left-down.
     * All other elements are set to 0.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix diagonal = ByteMatrix.diagonalRU2LD(new byte[]{1, 2, 3});
     * // Creates matrix:
     * // [[0, 0, 1],
     * //  [0, 2, 0],
     * //  [3, 0, 0]]
     * }</pre>
     *
     * @param rightUp2LeftDownDiagonal the values for the anti-diagonal
     * @return a new square ByteMatrix with the specified diagonal values
     */
    public static ByteMatrix diagonalRU2LD(final byte[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a square diagonal matrix with specified values on both diagonals.
     * The main diagonal runs from left-up to right-down, and the anti-diagonal runs from right-up to left-down.
     * If both diagonals are specified, they must have the same length.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix diagonal = ByteMatrix.diagonal(
     *     new byte[]{1, 2, 3},
     *     new byte[]{4, 5, 6}
     * );
     * // Creates matrix:
     * // [[1, 0, 4],
     * //  [0, 2, 0],
     * //  [6, 0, 3]]
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal values for the main diagonal (null if not needed)
     * @param rightUp2LeftDownDiagonal values for the anti-diagonal (null if not needed)
     * @return a new square ByteMatrix with the specified diagonal values
     * @throws IllegalArgumentException if both diagonals are specified but have different lengths
     */
    public static ByteMatrix diagonal(final byte[] leftUp2RightDownDiagonal, final byte[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        if (N.isEmpty(leftUp2RightDownDiagonal) && N.isEmpty(rightUp2LeftDownDiagonal)) {
            return EMPTY_BYTE_MATRIX;
        }

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final byte[][] c = new byte[len][len];

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

        return new ByteMatrix(c);
    }

    /**
     * Converts a Matrix of Byte objects to a ByteMatrix by unboxing all values.
     * Null values in the input matrix will be converted to 0.
     * 
     * <p>Example:
     * <pre>{@code
     * Matrix<Byte> boxed = Matrix.of(new Byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix unboxed = ByteMatrix.unbox(boxed);
     * }</pre>
     *
     * @param x the Matrix of Byte objects to convert
     * @return a new ByteMatrix containing the unboxed values
     */
    public static ByteMatrix unbox(final Matrix<Byte> x) {
        return ByteMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of the matrix elements, which is byte.class for ByteMatrix.
     *
     * @return byte.class
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return byte.class;
    }

    /**
     * Gets the byte value at the specified row and column indices.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * byte value = matrix.get(0, 1); // Returns 2
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the byte value at position [i,j]
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public byte get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Gets the byte value at the specified point in the matrix.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * byte value = matrix.get(Point.of(1, 0)); // Returns 3
     * }</pre>
     *
     * @param point the point containing row and column indices
     * @return the byte value at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public byte get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the byte value at the specified row and column indices.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * matrix.set(0, 1, (byte)5);
     * // Matrix is now: [[1, 5], [3, 4]]
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the byte value to set
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public void set(final int i, final int j, final byte val) {
        a[i][j] = val;
    }

    /**
     * Sets the byte value at the specified point in the matrix.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * matrix.set(Point.of(1, 1), (byte)5);
     * // Matrix is now: [[1, 2], [3, 5]]
     * }</pre>
     *
     * @param point the point containing row and column indices
     * @param val the byte value to set
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public void set(final Point point, final byte val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the value immediately above the specified position, if it exists.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * OptionalByte above = matrix.upOf(1, 0); // Returns OptionalByte.of(1)
     * OptionalByte none = matrix.upOf(0, 0);  // Returns OptionalByte.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an OptionalByte containing the value above [i,j], or empty if at the top row
     */
    public OptionalByte upOf(final int i, final int j) {
        return i == 0 ? OptionalByte.empty() : OptionalByte.of(a[i - 1][j]);
    }

    /**
     * Returns the value immediately below the specified position, if it exists.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * OptionalByte below = matrix.downOf(0, 0); // Returns OptionalByte.of(3)
     * OptionalByte none = matrix.downOf(1, 0);  // Returns OptionalByte.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an OptionalByte containing the value below [i,j], or empty if at the bottom row
     */
    public OptionalByte downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalByte.empty() : OptionalByte.of(a[i + 1][j]);
    }

    /**
     * Returns the value immediately to the left of the specified position, if it exists.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * OptionalByte left = matrix.leftOf(0, 1); // Returns OptionalByte.of(1)
     * OptionalByte none = matrix.leftOf(0, 0); // Returns OptionalByte.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an OptionalByte containing the value left of [i,j], or empty if at the leftmost column
     */
    public OptionalByte leftOf(final int i, final int j) {
        return j == 0 ? OptionalByte.empty() : OptionalByte.of(a[i][j - 1]);
    }

    /**
     * Returns the value immediately to the right of the specified position, if it exists.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * OptionalByte right = matrix.rightOf(0, 0); // Returns OptionalByte.of(2)
     * OptionalByte none = matrix.rightOf(0, 1);  // Returns OptionalByte.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an OptionalByte containing the value right of [i,j], or empty if at the rightmost column
     */
    public OptionalByte rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalByte.empty() : OptionalByte.of(a[i][j + 1]);
    }

    /**
     * Returns a stream of the four adjacent points (up, right, down, left) to the specified position.
     * Points that would be outside the matrix bounds are returned as null.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> adjacent = matrix.adjacent4Points(1, 1);
     * // Returns points for positions: [0,1] (up), [1,2] (right), [2,1] (down), [1,0] (left)
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return a Stream of Points representing the four adjacent positions, with null for out-of-bounds positions
     */
    public Stream<Point> adjacent4Points(final int i, final int j) {
        final Point up = i == 0 ? null : Point.of(i - 1, j);
        final Point right = j == cols - 1 ? null : Point.of(i, j + 1);
        final Point down = i == rows - 1 ? null : Point.of(i + 1, j);
        final Point left = j == 0 ? null : Point.of(i, j - 1);

        return Stream.of(up, right, down, left);
    }

    /**
     * Returns a stream of the eight adjacent points (including diagonals) to the specified position.
     * Points are returned in order: left-up, up, right-up, right, right-down, down, left-down, left.
     * Points that would be outside the matrix bounds are returned as null.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> adjacent = matrix.adjacent8Points(1, 1);
     * // Returns points for all 8 surrounding positions of [1,1]
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return a Stream of Points representing the eight adjacent positions, with null for out-of-bounds positions
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
     * Returns a copy of the specified row as a byte array.
     * The returned array is a direct reference to the internal array, so modifications will affect the matrix.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * byte[] row = matrix.row(0); // Returns [1, 2, 3]
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return the byte array representing the specified row
     * @throws IllegalArgumentException if rowIndex is out of bounds
     */
    public byte[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as a byte array.
     * The returned array is a new array containing copies of the column values.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * byte[] col = matrix.column(1); // Returns [2, 5]
     * }</pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new byte array containing the values from the specified column
     * @throws IllegalArgumentException if columnIndex is out of bounds
     */
    public byte[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final byte[] c = new byte[rows];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i][columnIndex];
        }

        return c;
    }

    /**
     * Replaces the specified row with the provided byte array.
     * The array must have the same length as the number of columns in the matrix.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.setRow(1, new byte[]{7, 8, 9});
     * // Matrix is now: [[1, 2, 3], [7, 8, 9]]
     * }</pre>
     *
     * @param rowIndex the index of the row to replace (0-based)
     * @param row the new values for the row
     * @throws IllegalArgumentException if rowIndex is out of bounds or row length doesn't match column count
     */
    public void setRow(final int rowIndex, final byte[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Replaces the specified column with the provided byte array.
     * The array must have the same length as the number of rows in the matrix.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.setColumn(1, new byte[]{7, 8});
     * // Matrix is now: [[1, 7, 3], [4, 8, 6]]
     * }</pre>
     *
     * @param columnIndex the index of the column to replace (0-based)
     * @param column the new values for the column
     * @throws IllegalArgumentException if columnIndex is out of bounds or column length doesn't match row count
     */
    public void setColumn(final int columnIndex, final byte[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all values in the specified row by applying the given function to each element.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.updateRow(0, b -> (byte)(b * 2));
     * // First row is now: [2, 4, 6]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the function
     * @param rowIndex the index of the row to update (0-based)
     * @param func the function to apply to each element in the row
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.ByteUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsByte(a[rowIndex][i]);
        }
    }

    /**
     * Updates all values in the specified column by applying the given function to each element.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.updateColumn(1, b -> (byte)(b + 10));
     * // Second column is now: [12, 15]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the function
     * @param columnIndex the index of the column to update (0-based)
     * @param func the function to apply to each element in the column
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.ByteUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsByte(a[i][columnIndex]);
        }
    }

    /**
     * Returns an array containing the values on the main diagonal (left-up to right-down).
     * The matrix must be square for this operation.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * byte[] diagonal = matrix.getLU2RD(); // Returns [1, 5, 9]
     * }</pre>
     *
     * @return a byte array containing the diagonal values
     * @throws IllegalStateException if the matrix is not square
     */
    public byte[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final byte[] res = new byte[rows];

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][i]; // NOSONAR
        }

        return res;
    }

    /**
     * Sets the values on the main diagonal (left-up to right-down) from the provided array.
     * The matrix must be square and the array length must be at least as long as the matrix dimension.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.setLU2RD(new byte[]{10, 11, 12});
     * // Matrix is now: [[10, 2, 3], [4, 11, 6], [7, 8, 12]]
     * }</pre>
     *
     * @param diagonal the new values for the main diagonal
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
     */
    public void setLU2RD(final byte[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates all values on the main diagonal (left-up to right-down) by applying the given function.
     * The matrix must be square for this operation.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.updateLU2RD(b -> (byte)(b * 2));
     * // Diagonal is now: [2, 10, 18]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the function
     * @param func the function to apply to each diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateLU2RD(final Throwables.ByteUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsByte(a[i][i]);
        }
    }

    /**
     * Returns an array containing the values on the anti-diagonal (right-up to left-down).
     * The matrix must be square for this operation.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * byte[] diagonal = matrix.getRU2LD(); // Returns [3, 5, 7]
     * }</pre>
     *
     * @return a byte array containing the anti-diagonal values
     * @throws IllegalStateException if the matrix is not square
     */
    public byte[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final byte[] res = new byte[rows];

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][cols - i - 1];
        }

        return res;
    }

    /**
     * Sets the values on the anti-diagonal (right-up to left-down) from the provided array.
     * The matrix must be square and the array length must be at least as long as the matrix dimension.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.setRU2LD(new byte[]{10, 11, 12});
     * // Matrix is now: [[1, 2, 10], [4, 11, 6], [12, 8, 9]]
     * }</pre>
     *
     * @param diagonal the new values for the anti-diagonal
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
     */
    public void setRU2LD(final byte[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = diagonal[i];
        }
    }

    /**
     * Updates all values on the anti-diagonal (right-up to left-down) by applying the given function.
     * The matrix must be square for this operation.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.updateRU2LD(b -> (byte)(b + 1));
     * // Anti-diagonal is now: [4, 6, 8]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the function
     * @param func the function to apply to each anti-diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateRU2LD(final Throwables.ByteUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsByte(a[i][cols - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix by applying the given function to each value.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * matrix.updateAll(b -> (byte)(b * 2));
     * // Matrix is now: [[2, 4], [6, 8]]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the function
     * @param func the function to apply to each element
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.ByteUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.applyAsByte(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Updates all elements in the matrix based on their position by applying the given function.
     * The function receives the row and column indices and returns the new value for that position.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{0, 0}, {0, 0}});
     * matrix.updateAll((i, j) -> (byte)(i + j));
     * // Matrix is now: [[0, 1], [1, 2]]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the function
     * @param func the function that takes row and column indices and returns a new byte value
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Byte, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements that match the given predicate with the specified new value.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.replaceIf(b -> b % 2 == 0, (byte)0);
     * // Matrix is now: [[1, 0, 3], [0, 5, 0]]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate
     * @param predicate the condition to test each element
     * @param newValue the value to use as replacement
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.BytePredicate<E> predicate, final byte newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces elements based on their position using the given predicate.
     * Elements at positions where the predicate returns true are replaced with the new value.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * matrix.replaceIf((i, j) -> i == j, (byte)0);
     * // Matrix is now: [[0, 2], [3, 0]] (diagonal replaced with 0)
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate
     * @param predicate the condition that takes row and column indices and returns true for positions to replace
     * @param newValue the value to use as replacement
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final byte newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new ByteMatrix by applying the given function to each element of this matrix.
     * The resulting matrix has the same dimensions as the original.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix doubled = matrix.map(b -> (byte)(b * 2));
     * // doubled is: [[2, 4], [6, 8]]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the function
     * @param func the function to apply to each element
     * @return a new ByteMatrix with transformed values
     * @throws E if the function throws an exception
     */
    public <E extends Exception> ByteMatrix map(final Throwables.ByteUnaryOperator<E> func) throws E {
        final byte[][] result = new byte[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsByte(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ByteMatrix.of(result);
    }

    /**
     * Creates a new Matrix by applying the given function to map byte values to objects of type T.
     * The resulting matrix has the same dimensions but contains objects instead of primitives.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix byteMatrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * Matrix<String> stringMatrix = byteMatrix.mapToObj(
     *     b -> "Value: " + b,
     *     String.class
     * );
     * // stringMatrix contains: [["Value: 1", "Value: 2"], ["Value: 3", "Value: 4"]]
     * }</pre>
     *
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the type of exception that may be thrown by the function
     * @param func the function to convert byte values to type T
     * @param targetElementType the class of the target element type
     * @return a new Matrix containing the mapped values
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.ByteFunction<? extends T, E> func, final Class<T> targetElementType) throws E {
        final T[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Fills all elements in the matrix with the specified value.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * matrix.fill((byte)5);
     * // Matrix is now: [[5, 5], [5, 5]]
     * }</pre>
     *
     * @param val the value to fill the matrix with
     */
    public void fill(final byte val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills this matrix with values from another 2D byte array, starting from position [0,0].
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{0, 0, 0}, {0, 0, 0}});
     * matrix.fill(new byte[][]{{1, 2}, {3, 4}});
     * // Matrix is now: [[1, 2, 0], [3, 4, 0]]
     * }</pre>
     *
     * @param b the source array to copy values from
     */
    public void fill(final byte[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a portion of this matrix with values from another 2D byte array.
     * The filling starts at the specified position and copies as many values as possible
     * without exceeding the bounds of either array.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
     * matrix.fill(1, 1, new byte[][]{{1, 2}, {3, 4}});
     * // Matrix is now: [[0, 0, 0], [0, 1, 2], [0, 3, 4]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index in this matrix
     * @param fromColumnIndex the starting column index in this matrix
     * @param b the source array to copy values from
     * @throws IndexOutOfBoundsException if the starting indices are out of bounds
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final byte[][] b) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, rows, rows);
        N.checkFromToIndex(fromColumnIndex, cols, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
        }
    }

    /**
     * Creates a copy of this matrix with all values duplicated.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix copy = matrix.copy();
     * copy.set(0, 0, (byte)9);
     * // matrix is still: [[1, 2], [3, 4]]
     * // copy is: [[9, 2], [3, 4]]
     * }</pre>
     *
     * @return a new ByteMatrix that is an exact copy of this matrix
     */
    @Override
    public ByteMatrix copy() {
        final byte[][] c = new byte[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new ByteMatrix(c);
    }

    /**
     * Creates a copy of a range of rows from this matrix.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}, {5, 6}});
     * ByteMatrix subset = matrix.copy(0, 2);
     * // subset is: [[1, 2], [3, 4]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new ByteMatrix containing the specified rows
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @Override
    public ByteMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final byte[][] c = new byte[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new ByteMatrix(c);
    }

    /**
     * Creates a copy of a rectangular region from this matrix.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * ByteMatrix subset = matrix.copy(0, 2, 1, 3);
     * // subset is: [[2, 3], [5, 6]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new ByteMatrix containing the specified region
     * @throws IndexOutOfBoundsException if any indices are out of bounds
     */
    @Override
    public ByteMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final byte[][] c = new byte[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new ByteMatrix(c);
    }

    /**
     * Creates a new matrix by extending or truncating this matrix to the specified dimensions.
     * New cells (if any) are filled with 0.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix extended = matrix.extend(3, 3);
     * // extended is: [[1, 2, 0], [3, 4, 0], [0, 0, 0]]
     * }</pre>
     *
     * @param newRows the number of rows in the new matrix
     * @param newCols the number of columns in the new matrix
     * @return a new ByteMatrix with the specified dimensions
     */
    public ByteMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, BYTE_0);
    }

    /**
     * Creates a new matrix by extending or truncating this matrix to the specified dimensions.
     * New cells (if any) are filled with the specified default value.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix extended = matrix.extend(3, 3, (byte)9);
     * // extended is: [[1, 2, 9], [3, 4, 9], [9, 9, 9]]
     * }</pre>
     *
     * @param newRows the number of rows in the new matrix
     * @param newCols the number of columns in the new matrix
     * @param defaultValueForNewCell the value to use for any new cells
     * @return a new ByteMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public ByteMatrix extend(final int newRows, final int newCols, final byte defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            final boolean fillDefaultValue = defaultValueForNewCell != BYTE_0;
            final byte[][] b = new byte[newRows][];

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : new byte[newCols];

                if (fillDefaultValue) {
                    if (i >= rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, defaultValueForNewCell);
                    }
                }
            }

            return new ByteMatrix(b);
        }
    }

    /**
     * Creates a new matrix by extending this matrix in all four directions.
     * New cells are filled with 0.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix extended = matrix.extend(1, 1, 1, 1);
     * // extended is: [[0, 0, 0, 0],
     * //               [0, 1, 2, 0],
     * //               [0, 3, 4, 0],
     * //               [0, 0, 0, 0]]
     * }</pre>
     *
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @return a new extended ByteMatrix
     */
    public ByteMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, BYTE_0);
    }

    /**
     * Creates a new matrix by extending this matrix in all four directions.
     * New cells are filled with the specified default value.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix extended = matrix.extend(1, 1, 1, 1, (byte)9);
     * // extended is: [[9, 9, 9, 9],
     * //               [9, 1, 2, 9],
     * //               [9, 3, 4, 9],
     * //               [9, 9, 9, 9]]
     * }</pre>
     *
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @param defaultValueForNewCell the value to use for new cells
     * @return a new extended ByteMatrix
     * @throws IllegalArgumentException if any extension parameter is negative
     */
    public ByteMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final byte defaultValueForNewCell)
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
            final boolean fillDefaultValue = defaultValueForNewCell != BYTE_0;
            final byte[][] b = new byte[newRows][newCols];

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

            return new ByteMatrix(b);
        }
    }

    /**
     * Reverses the order of elements in each row (horizontal flip in-place).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.reverseH(); // [[1,2,3]] becomes [[3,2,1]]
     * }</pre>
     */
    public void reverseH() {
        for (int i = 0; i < rows; i++) {
            N.reverse(a[i]);
        }
    }

    /**
     * Reverses the order of elements in each column (vertical flip in-place).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.reverseV(); // [[1],[2],[3]] becomes [[3],[2],[1]]
     * }</pre>
     */
    public void reverseV() {
        for (int j = 0; j < cols; j++) {
            byte tmp = 0;
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
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteMatrix flipped = matrix.flipH();
     * // flipped is: [[3, 2, 1], [6, 5, 4]]
     * // original matrix is unchanged
     * }</pre>
     *
     * @return a new ByteMatrix with rows reversed
     * @see #flipV()
     * @see IntMatrix#flipH()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1</a>
     */
    public ByteMatrix flipH() {
        final ByteMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * Creates a new matrix that is vertically flipped (each column reversed).
     * The original matrix is not modified.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteMatrix flipped = matrix.flipV();
     * // flipped is: [[4, 5, 6], [1, 2, 3]]
     * // original matrix is unchanged
     * }</pre>
     *
     * @return a new ByteMatrix with columns reversed
     * @see #flipH()
     * @see IntMatrix#flipV()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1</a>
     */
    public ByteMatrix flipV() {
        final ByteMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * Creates a new matrix rotated 90 degrees clockwise.
     * The dimensions are swapped: an m×n matrix becomes n×m.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteMatrix rotated = matrix.rotate90();
     * // rotated is: [[4, 1],
     * //              [5, 2],
     * //              [6, 3]]
     * }</pre>
     *
     * @return a new ByteMatrix rotated 90 degrees clockwise
     */
    @Override
    public ByteMatrix rotate90() {
        final byte[][] c = new byte[cols][rows];

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

        return new ByteMatrix(c);
    }

    /**
     * Creates a new matrix rotated 180 degrees.
     * This is equivalent to flipping both horizontally and vertically.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteMatrix rotated = matrix.rotate180();
     * // rotated is: [[6, 5, 4],
     * //              [3, 2, 1]]
     * }</pre>
     *
     * @return a new ByteMatrix rotated 180 degrees
     */
    @Override
    public ByteMatrix rotate180() {
        final byte[][] c = new byte[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new ByteMatrix(c);
    }

    /**
     * Creates a new matrix rotated 270 degrees clockwise (90 degrees counter-clockwise).
     * The dimensions are swapped: an m×n matrix becomes n×m.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteMatrix rotated = matrix.rotate270();
     * // rotated is: [[3, 6],
     * //              [2, 5],
     * //              [1, 4]]
     * }</pre>
     *
     * @return a new ByteMatrix rotated 270 degrees clockwise
     */
    @Override
    public ByteMatrix rotate270() {
        final byte[][] c = new byte[cols][rows];

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

        return new ByteMatrix(c);
    }

    /**
     * Creates a new matrix that is the transpose of this matrix.
     * Rows become columns and columns become rows.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteMatrix transposed = matrix.transpose();
     * // transposed is: [[1, 4],
     * //                 [2, 5],
     * //                 [3, 6]]
     * }</pre>
     *
     * @return a new ByteMatrix that is the transpose of this matrix
     */
    @Override
    public ByteMatrix transpose() {
        final byte[][] c = new byte[cols][rows];

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

        return new ByteMatrix(c);
    }

    /**
     * Reshapes the matrix to new dimensions while preserving element order.
     * Elements are read in row-major order and placed into the new shape.
     * The total number of elements may change if the new dimensions differ.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteMatrix reshaped = matrix.reshape(3, 2);
     * // reshaped is: [[1, 2],
     * //               [3, 4],
     * //               [5, 6]]
     * }</pre>
     *
     * @param newRows the number of rows in the reshaped matrix
     * @param newCols the number of columns in the reshaped matrix
     * @return a new ByteMatrix with the specified shape
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public ByteMatrix reshape(final int newRows, final int newCols) {
        final byte[][] c = new byte[newRows][newCols];

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new ByteMatrix(c);
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

        return new ByteMatrix(c);
    }

    /**
     * Creates a new matrix by repeating each element multiple times.
     * Each element is repeated rowRepeats times vertically and colRepeats times horizontally.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix repeated = matrix.repelem(2, 3);
     * // repeated is: [[1, 1, 1, 2, 2, 2],
     * //               [1, 1, 1, 2, 2, 2],
     * //               [3, 3, 3, 4, 4, 4],
     * //               [3, 3, 3, 4, 4, 4]]
     * }</pre>
     *
     * @param rowRepeats number of times to repeat each element vertically
     * @param colRepeats number of times to repeat each element horizontally
     * @return a new matrix with repeated elements
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see IntMatrix#repelem(int, int)
     */
    @Override
    public ByteMatrix repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final byte[][] c = new byte[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            final byte[] aa = a[i];
            final byte[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                N.copy(Array.repeat(aa[j], colRepeats), 0, fr, j * colRepeats, colRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new ByteMatrix(c);
    }

    /**
     * Creates a new matrix by repeating the entire matrix multiple times.
     * The matrix is tiled rowRepeats times vertically and colRepeats times horizontally.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix repeated = matrix.repmat(2, 3);
     * // repeated is: [[1, 2, 1, 2, 1, 2],
     * //               [3, 4, 3, 4, 3, 4],
     * //               [1, 2, 1, 2, 1, 2],
     * //               [3, 4, 3, 4, 3, 4]]
     * }</pre>
     *
     * @param rowRepeats number of times to repeat the matrix vertically
     * @param colRepeats number of times to repeat the matrix horizontally
     * @return a new matrix with the original matrix repeated
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see IntMatrix#repmat(int, int)
     */
    @Override
    public ByteMatrix repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final byte[][] c = new byte[rows * rowRepeats][cols * colRepeats];

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

        return new ByteMatrix(c);
    }

    /**
     * Flattens the matrix into a single ByteList in row-major order.
     * Elements are read row by row from left to right.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteList flattened = matrix.flatten();
     * // flattened is: [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @return a ByteList containing all elements of the matrix
     */
    @Override
    public ByteList flatten() {
        final byte[] c = new byte[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return ByteList.of(c);
    }

    /**
     * Flattens the underlying 2D array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure.
     *
     * @param <E> the type of exception that may be thrown
     * @param op the operation to apply to the 2D array
     * @throws E if the operation throws an exception
     * @see Arrays#flatOp(byte[][], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super byte[], E> op) throws E {
        Arrays.flatOp(a, op);
    }

    /**
     * Stacks this matrix vertically with another matrix (row-wise concatenation).
     * The matrices must have the same number of columns.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix1 = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix matrix2 = ByteMatrix.of(new byte[][]{{5, 6}, {7, 8}});
     * ByteMatrix stacked = matrix1.vstack(matrix2);
     * // stacked is: [[1, 2],
     * //              [3, 4],
     * //              [5, 6],
     * //              [7, 8]]
     * }</pre>
     *
     * @param b the matrix to stack below this matrix
     * @return a new ByteMatrix with b appended below this matrix
     * @throws IllegalArgumentException if the matrices have different column counts
     * @see IntMatrix#vstack(IntMatrix)
     */
    public ByteMatrix vstack(final ByteMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final byte[][] c = new byte[rows + b.rows][];
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
        }

        return ByteMatrix.of(c);
    }

    /**
     * Stacks this matrix horizontally with another matrix (column-wise concatenation).
     * The matrices must have the same number of rows.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix1 = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix matrix2 = ByteMatrix.of(new byte[][]{{5, 6}, {7, 8}});
     * ByteMatrix stacked = matrix1.hstack(matrix2);
     * // stacked is: [[1, 2, 5, 6],
     * //              [3, 4, 7, 8]]
     * }</pre>
     *
     * @param b the matrix to concatenate to the right of this matrix
     * @return a new ByteMatrix with b appended to the right of this matrix
     * @throws IllegalArgumentException if the matrices have different row counts
     * @see IntMatrix#hstack(IntMatrix)
     */
    public ByteMatrix hstack(final ByteMatrix b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final byte[][] c = new byte[rows][cols + b.cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c[i], 0, cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
        }

        return ByteMatrix.of(c);
    }

    /**
     * Performs element-wise addition with another matrix of the same dimensions.
     * Note that byte overflow may occur during addition.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix1 = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix matrix2 = ByteMatrix.of(new byte[][]{{5, 6}, {7, 8}});
     * ByteMatrix sum = matrix1.add(matrix2);
     * // sum is: [[6, 8], [10, 12]]
     * }</pre>
     *
     * @param b the matrix to add to this matrix
     * @return a new ByteMatrix containing the element-wise sum
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public ByteMatrix add(final ByteMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't add Matrixes with different shape");

        final byte[][] ba = b.a;
        final byte[][] result = new byte[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (byte) (a[i][j] + ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ByteMatrix.of(result);
    }

    /**
     * Performs element-wise subtraction with another matrix of the same dimensions.
     * Note that byte overflow may occur during subtraction.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix1 = ByteMatrix.of(new byte[][]{{5, 6}, {7, 8}});
     * ByteMatrix matrix2 = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix diff = matrix1.subtract(matrix2);
     * // diff is: [[4, 4], [4, 4]]
     * }</pre>
     *
     * @param b the matrix to subtract from this matrix
     * @return a new ByteMatrix containing the element-wise difference
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public ByteMatrix subtract(final ByteMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't subtract Matrixes with different shape");

        final byte[][] ba = b.a;
        final byte[][] result = new byte[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (byte) (a[i][j] - ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ByteMatrix.of(result);
    }

    /**
     * Performs matrix multiplication with another matrix.
     * The number of columns in this matrix must equal the number of rows in the other matrix.
     * Note that byte overflow may occur during multiplication.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix1 = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix matrix2 = ByteMatrix.of(new byte[][]{{5, 6}, {7, 8}});
     * ByteMatrix product = matrix1.multiply(matrix2);
     * // product is: [[19, 22], [43, 50]]
     * }</pre>
     *
     * @param b the matrix to multiply with
     * @return a new ByteMatrix containing the matrix product
     * @throws IllegalArgumentException if the matrices have incompatible dimensions for multiplication
     */
    public ByteMatrix multiply(final ByteMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.rows, "Illegal matrix dimensions");

        final byte[][] ba = b.a;
        final byte[][] result = new byte[rows][b.cols];
        final Throwables.IntTriConsumer<RuntimeException> cmd = (i, j, k) -> result[i][j] += (byte) (a[i][k] * ba[k][j]);

        Matrixes.multiply(this, b, cmd);

        return new ByteMatrix(result);
    }

    /**
     * Converts this ByteMatrix to a Matrix of Byte objects (boxed values).
     * Each primitive byte value is boxed into a Byte object.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix byteMatrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * Matrix<Byte> boxed = byteMatrix.boxed();
     * // boxed contains Byte objects instead of primitive bytes
     * }</pre>
     *
     * @return a new Matrix containing Byte objects
     */
    public Matrix<Byte> boxed() {
        final Byte[][] c = new Byte[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final byte[] aa = a[i];
                final Byte[] cc = c[i];

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
     * Converts this ByteMatrix to an IntMatrix by widening each byte value to int.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix byteMatrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * IntMatrix intMatrix = byteMatrix.toIntMatrix();
     * // intMatrix contains the same values as 32-bit integers
     * }</pre>
     *
     * @return a new IntMatrix with the same values as ints
     */
    public IntMatrix toIntMatrix() {
        return IntMatrix.create(a);
    }

    /**
     * Converts this ByteMatrix to a LongMatrix by widening each byte value to long.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix byteMatrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * LongMatrix longMatrix = byteMatrix.toLongMatrix();
     * // longMatrix contains the same values as 64-bit longs
     * }</pre>
     *
     * @return a new LongMatrix with the same values as longs
     */
    public LongMatrix toLongMatrix() {
        final long[][] c = new long[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final byte[] aa = a[i];
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
     * Converts this ByteMatrix to a FloatMatrix by converting each byte value to float
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix byteMatrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * FloatMatrix floatMatrix = byteMatrix.toFloatMatrix();
     * // floatMatrix contains the same values as floating-point numbers
     * }</pre>
     *
     * @return a new FloatMatrix with the same values as floats
     */
    public FloatMatrix toFloatMatrix() {
        final float[][] c = new float[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final byte[] aa = a[i];
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
     * Converts this ByteMatrix to a DoubleMatrix by converting each byte value to double.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix byteMatrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * DoubleMatrix doubleMatrix = byteMatrix.toDoubleMatrix();
     * // doubleMatrix contains the same values as double-precision floating-point numbers
     * }</pre>
     *
     * @return a new DoubleMatrix with the same values as doubles
     */
    public DoubleMatrix toDoubleMatrix() {
        final double[][] c = new double[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final byte[] aa = a[i];
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
     * Applies a binary operation element-wise to this matrix and another matrix of the same shape.
     * The operation is applied to corresponding elements from both matrices.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix1 = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix matrix2 = ByteMatrix.of(new byte[][]{{5, 6}, {7, 8}});
     * ByteMatrix result = matrix1.zipWith(matrix2, (a, b) -> (byte)(a * b));
     * // result is: [[5, 12], [21, 32]]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operation
     * @param matrixB the second matrix
     * @param zipFunction the binary operation to apply to corresponding elements
     * @return a new ByteMatrix containing the results
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> ByteMatrix zipWith(final ByteMatrix matrixB, final Throwables.ByteBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Can't zip two or more matrices which don't have same shape");

        final byte[][] b = matrixB.a;
        final byte[][] result = new byte[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsByte(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ByteMatrix.of(result);
    }

    /**
     * Applies a ternary operation element-wise to this matrix and two other matrices of the same shape.
     * The operation is applied to corresponding elements from all three matrices.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix1 = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * ByteMatrix matrix2 = ByteMatrix.of(new byte[][]{{5, 6}, {7, 8}});
     * ByteMatrix matrix3 = ByteMatrix.of(new byte[][]{{9, 10}, {11, 12}});
     * ByteMatrix result = matrix1.zipWith(matrix2, matrix3, 
     *     (a, b, c) -> (byte)(a + b + c));
     * // result is: [[15, 18], [21, 24]]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operation
     * @param matrixB the second matrix
     * @param matrixC the third matrix
     * @param zipFunction the ternary operation to apply to corresponding elements
     * @return a new ByteMatrix containing the results
     * @throws E if the zip function throws an exception
     * @throws IllegalArgumentException if the matrices have different shapes
     */
    public <E extends Exception> ByteMatrix zipWith(final ByteMatrix matrixB, final ByteMatrix matrixC, final Throwables.ByteTernaryOperator<E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Can't zip two or more matrices which don't have same shape");

        final byte[][] b = matrixB.a;
        final byte[][] c = matrixC.a;
        final byte[][] result = new byte[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsByte(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ByteMatrix.of(result);
    }

    /**
     * Returns a stream of elements on the diagonal from left-up to right-down.
     * The matrix must be square for this operation.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * ByteStream diagonal = matrix.streamLU2RD();
     * // diagonal contains: [1, 5, 9]
     * }</pre>
     *
     * @return a ByteStream of diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public ByteStream streamLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return ByteStream.empty();
        }

        return ByteStream.of(new ByteIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public byte nextByte() {
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
     * Returns a stream of elements on the diagonal from right-up to left-down.
     * The matrix must be square for this operation.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * ByteStream diagonal = matrix.streamRU2LD();
     * // diagonal contains: [3, 5, 7]
     * }</pre>
     *
     * @return a ByteStream of anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public ByteStream streamRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return ByteStream.empty();
        }

        return ByteStream.of(new ByteIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public byte nextByte() {
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
     * Returns a stream of all elements in row-major order (horizontally).
     * Elements are streamed row by row from left to right.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteStream stream = matrix.streamH();
     * // stream contains: [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @return a ByteStream of all matrix elements in row-major order
     */
    @Override
    public ByteStream streamH() {
        return streamH(0, rows);
    }

    /**
     * Returns a stream of elements from a specific row.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteStream row = matrix.streamH(1);
     * // row contains: [4, 5, 6]
     * }</pre>
     *
     * @param rowIndex the index of the row to stream
     * @return a ByteStream of elements from the specified row
     */
    @Override
    public ByteStream streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * Returns a stream of elements from a range of rows in row-major order.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}, {5, 6}});
     * ByteStream stream = matrix.streamH(0, 2);
     * // stream contains: [1, 2, 3, 4]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a ByteStream of elements from the specified rows
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @Override
    public ByteStream streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return ByteStream.empty();
        }

        return ByteStream.of(new ByteIteratorEx() {
            private int i = fromRowIndex;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i < toRowIndex;
            }

            @Override
            public byte nextByte() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final byte result = a[i][j++];

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
            public byte[] toArray() {
                final int len = (int) count();
                final byte[] c = new byte[len];

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
     * Returns a stream of all elements in column-major order (vertically).
     * Elements are streamed column by column from top to bottom.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteStream stream = matrix.streamV();
     * // stream contains: [1, 4, 2, 5, 3, 6]
     * }</pre>
     *
     * @return a ByteStream of all matrix elements in column-major order
     */
    @Override
    @Beta
    public ByteStream streamV() {
        return streamV(0, cols);
    }

    /**
     * Returns a stream of elements from a specific column.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteStream column = matrix.streamV(1);
     * // column contains: [2, 5]
     * }</pre>
     *
     * @param columnIndex the index of the column to stream
     * @return a ByteStream of elements from the specified column
     */
    @Override
    public ByteStream streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * Returns a stream of elements from a range of columns in column-major order.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * ByteStream stream = matrix.streamV(1, 3);
     * // stream contains: [2, 5, 3, 6]
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a ByteStream of elements from the specified columns
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @Override
    @Beta
    public ByteStream streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return ByteStream.empty();
        }

        return ByteStream.of(new ByteIteratorEx() {
            private int i = 0;
            private int j = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return j < toColumnIndex;
            }

            @Override
            public byte nextByte() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final byte result = a[i++][j];

                if (i >= rows) {
                    i = 0;
                    j++;
                }
                return result;
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                if (n >= (long) (toColumnIndex - j) * ByteMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    i += (int) ((n + i) % ByteMatrix.this.rows);
                    j += (int) ((n + i) / ByteMatrix.this.rows);
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public byte[] toArray() {
                final int len = (int) count();
                final byte[] c = new byte[len];

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
     * Returns a stream where each element is a ByteStream representing a row of the matrix.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<ByteStream> rows = matrix.streamR();
     * // First element: ByteStream of [1, 2, 3]
     * // Second element: ByteStream of [4, 5, 6]
     * }</pre>
     *
     * @return a Stream of ByteStream, one for each row
     */
    @Override
    public Stream<ByteStream> streamR() {
        return streamR(0, rows);
    }

    /**
     * Returns a stream where each element is a ByteStream representing a row from the specified range.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}, {5, 6}});
     * Stream<ByteStream> rows = matrix.streamR(1, 3);
     * // First element: ByteStream of [3, 4]
     * // Second element: ByteStream of [5, 6]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a Stream of ByteStream, one for each row in the range
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @Override
    public Stream<ByteStream> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
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
            public ByteStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return ByteStream.of(a[cursor++]);
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
     * Returns a stream where each element is a ByteStream representing a column of the matrix.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<ByteStream> columns = matrix.streamC();
     * // First element: ByteStream of [1, 4]
     * // Second element: ByteStream of [2, 5]
     * // Third element: ByteStream of [3, 6]
     * }</pre>
     *
     * @return a Stream of ByteStream, one for each column
     */
    @Override
    @Beta
    public Stream<ByteStream> streamC() {
        return streamC(0, cols);
    }

    /**
     * Returns a stream where each element is a ByteStream representing a column from the specified range.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<ByteStream> columns = matrix.streamC(1, 3);
     * // First element: ByteStream of [2, 5]
     * // Second element: ByteStream of [3, 6]
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a Stream of ByteStream, one for each column in the range
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @Override
    @Beta
    public Stream<ByteStream> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public ByteStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return ByteStream.of(new ByteIteratorEx() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rows;
                    private int cursor2 = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor2 < toIndex2;
                    }

                    @Override
                    public byte nextByte() {
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
     * Returns the length of the specified array, or 0 if the array is null.
     * This is an internal helper method.
     *
     * @param a the array to measure
     * @return the length of the array, or 0 if null
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final byte[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * Applies the given action to each element in the matrix.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
     * matrix.forEach(value -> System.out.print(value + " "));
     * // Prints: 1 2 3 4
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the action to perform on each element
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> action) throws E {
        forEach(0, rows, 0, cols, action);
    }

    /**
     * Applies the given action to each element in the specified rectangular region of the matrix.
     * The operation may be performed in parallel for large regions.
     * 
     * <p>Example:
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.forEach(0, 2, 1, 3, value -> System.out.print(value + " "));
     * // Prints: 2 3 5 6 (the 2x2 submatrix from [0,1] to [2,3])
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to perform on each element
     * @throws IndexOutOfBoundsException if any indices are out of bounds
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.ByteConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final byte[] aa = a[i];

                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(aa[j]);
                }
            }
        }
    }

    /**
     * Prints the matrix to standard output in a formatted manner.
     * Each row is printed on a separate line with elements separated by commas
     * and enclosed in square brackets. The entire matrix is also enclosed in brackets.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
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
     * Returns a hash code value for the matrix based on its contents.
     *
     * @return a hash code value for this matrix
     */
    @Override
    public int hashCode() {
        return N.deepHashCode(a);
    }

    /**
     * Compares this matrix to another object for equality.
     * Two ByteMatrix objects are equal if they have the same dimensions and all corresponding elements are equal.
     *
     * @param obj the object to compare with
     * @return {@code true} if the specified object is a ByteMatrix with the same dimensions and values
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof final ByteMatrix another) {
            return cols == another.cols && rows == another.rows && N.deepEquals(a, another.a);
        }

        return false;
    }

    /**
     * Returns a string representation of the matrix.
     * The format is a 2D array representation with rows and columns clearly displayed.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ByteMatrix matrix = ByteMatrix.of(new byte[][]{{1, 2}, {3, 4}});
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