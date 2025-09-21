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
 * <p>The matrix is internally represented as a 2D short array (short[][]) and supports various
 * operations including arithmetic operations, matrix transformations (transpose, rotate, flip),
 * and element-wise operations.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * // Create a 3x3 matrix
 * short[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
 * ShortMatrix matrix = ShortMatrix.of(data);
 * 
 * // Get element at position (1, 2)
 * short value = matrix.get(1, 2); // returns 6
 * 
 * // Transpose the matrix
 * ShortMatrix transposed = matrix.transpose();
 * 
 * // Stream all elements
 * matrix.streamH().forEach(System.out::println);
 * }</pre>
 * 
 * @see AbstractMatrix
 * @see IntMatrix
 * @see LongMatrix
 * @see FloatMatrix
 * @see DoubleMatrix
 */
public final class ShortMatrix extends AbstractMatrix<short[], ShortList, ShortStream, Stream<ShortStream>, ShortMatrix> {

    static final ShortMatrix EMPTY_SHORT_MATRIX = new ShortMatrix(new short[0][0]);

    /**
     * Constructs a ShortMatrix from a 2D short array. If the input array is null,
     * an empty matrix (0x0) is created.
     * 
     * @param a the 2D short array to wrap in this matrix. The array is used directly
     *          without copying, so modifications to the array will affect the matrix.
     */
    public ShortMatrix(final short[][] a) {
        super(a == null ? new short[0][0] : a);
    }

    /**
     * Returns an empty ShortMatrix instance (0x0 matrix). This is a singleton instance
     * that is reused for all empty matrices.
     * 
     * @return an empty ShortMatrix with dimensions 0x0
     */
    public static ShortMatrix empty() {
        return EMPTY_SHORT_MATRIX;
    }

    /**
     * Creates a ShortMatrix from the specified 2D short array. This is a convenient
     * factory method that handles null and empty inputs gracefully.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * }</pre>
     * 
     * @param a the 2D short array to create the matrix from
     * @return a new ShortMatrix wrapping the provided array, or an empty matrix if the input is null or empty
     */
    public static ShortMatrix of(final short[]... a) {
        return N.isEmpty(a) ? EMPTY_SHORT_MATRIX : new ShortMatrix(a);
    }

    /**
     * Creates a 1xN matrix filled with random short values.
     * 
     * @param len the number of columns in the resulting 1-row matrix
     * @return a new ShortMatrix with one row and 'len' columns containing random short values
     */
    @SuppressWarnings("deprecation")
    public static ShortMatrix random(final int len) {
        return new ShortMatrix(new short[][] { ShortList.random(len).array() });
    }

    /**
     * Creates a 1xN matrix where all elements have the same specified value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.repeat((short)5, 4); // Creates [[5, 5, 5, 5]]
     * }</pre>
     * 
     * @param val the value to repeat
     * @param len the number of times to repeat the value (number of columns)
     * @return a new 1xN ShortMatrix where all elements equal 'val'
     */
    public static ShortMatrix repeat(final short val, final int len) {
        return new ShortMatrix(new short[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a 1xN matrix containing a sequence of short values from startInclusive
     * (inclusive) to endExclusive (exclusive) with a step of 1.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.range((short)1, (short)5); // Creates [[1, 2, 3, 4]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @return a new 1xN ShortMatrix containing the range of values
     */
    public static ShortMatrix range(final short startInclusive, final short endExclusive) {
        return new ShortMatrix(new short[][] { Array.range(startInclusive, endExclusive) });
    }

    /**
     * Creates a 1xN matrix containing a sequence of short values from startInclusive
     * (inclusive) to endExclusive (exclusive) with the specified step.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.range((short)0, (short)10, (short)2); // Creates [[0, 2, 4, 6, 8]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @param by the step size between consecutive values
     * @return a new 1xN ShortMatrix containing the range of values
     */
    public static ShortMatrix range(final short startInclusive, final short endExclusive, final short by) {
        return new ShortMatrix(new short[][] { Array.range(startInclusive, endExclusive, by) });
    }

    /**
     * Creates a 1xN matrix containing a sequence of short values from startInclusive
     * to endInclusive (both inclusive) with a step of 1.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.rangeClosed((short)1, (short)4); // Creates [[1, 2, 3, 4]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive)
     * @return a new 1xN ShortMatrix containing the range of values
     */
    public static ShortMatrix rangeClosed(final short startInclusive, final short endInclusive) {
        return new ShortMatrix(new short[][] { Array.rangeClosed(startInclusive, endInclusive) });
    }

    /**
     * Creates a 1xN matrix containing a sequence of short values from startInclusive
     * to endInclusive (both inclusive) with the specified step.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.rangeClosed((short)1, (short)9, (short)2); // Creates [[1, 3, 5, 7, 9]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive)
     * @param by the step size between consecutive values
     * @return a new 1xN ShortMatrix containing the range of values
     */
    public static ShortMatrix rangeClosed(final short startInclusive, final short endInclusive, final short by) {
        return new ShortMatrix(new short[][] { Array.rangeClosed(startInclusive, endInclusive, by) });
    }

    /**
     * Creates a square diagonal matrix with the specified values on the main diagonal
     * (from left-up to right-down). All other elements are zero.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.diagonalLU2RD(new short[]{1, 2, 3});
     * // Creates:
     * // [[1, 0, 0],
     * //  [0, 2, 0],
     * //  [0, 0, 3]]
     * }</pre>
     * 
     * @param leftUp2RightDownDiagonal the values to place on the main diagonal
     * @return a new square ShortMatrix with the specified diagonal values
     */
    public static ShortMatrix diagonalLU2RD(final short[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square diagonal matrix with the specified values on the anti-diagonal
     * (from right-up to left-down). All other elements are zero.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.diagonalRU2LD(new short[]{1, 2, 3});
     * // Creates:
     * // [[0, 0, 1],
     * //  [0, 2, 0],
     * //  [3, 0, 0]]
     * }</pre>
     * 
     * @param rightUp2LeftDownDiagonal the values to place on the anti-diagonal
     * @return a new square ShortMatrix with the specified anti-diagonal values
     */
    public static ShortMatrix diagonalRU2LD(final short[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a square diagonal matrix with values on both the main diagonal (left-up to right-down)
     * and the anti-diagonal (right-up to left-down). If only one diagonal is specified, the other
     * is filled with zeros. If both are specified, they must have the same length.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.diagonal(new short[]{1, 2, 3}, new short[]{4, 5, 6});
     * // Creates:
     * // [[1, 0, 4],
     * //  [0, 2, 0],
     * //  [6, 0, 3]]
     * }</pre>
     * 
     * @param leftUp2RightDownDiagonal the values for the main diagonal (can be null)
     * @param rightUp2LeftDownDiagonal the values for the anti-diagonal (can be null)
     * @return a new square ShortMatrix with the specified diagonal values
     * @throws IllegalArgumentException if both diagonals are non-null and have different lengths
     */
    public static ShortMatrix diagonal(final short[] leftUp2RightDownDiagonal, final short[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        if (N.isEmpty(leftUp2RightDownDiagonal) && N.isEmpty(rightUp2LeftDownDiagonal)) {
            return EMPTY_SHORT_MATRIX;
        }

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final short[][] c = new short[len][len];

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

        return new ShortMatrix(c);
    }

    /**
     * Converts a Matrix of Short objects to a ShortMatrix of primitive shorts.
     * This method performs unboxing of each Short element to its primitive short value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Short> boxedMatrix = Matrix.of(new Short[][]{{1, 2}, {3, 4}});
     * ShortMatrix primitiveMatrix = ShortMatrix.unbox(boxedMatrix);
     * }</pre>
     * 
     * @param x the Matrix of Short objects to convert
     * @return a new ShortMatrix containing the unboxed primitive values
     */
    public static ShortMatrix unbox(final Matrix<Short> x) {
        return ShortMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of the matrix elements, which is always short.class.
     * 
     * @return short.class
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return short.class;
    }

    /**
     * Gets the element at the specified row and column indices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * short value = matrix.get(1, 0); // returns 3
     * }</pre>
     * 
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the short value at position (i, j)
     * @throws ArrayIndexOutOfBoundsException if the indices are out of bounds
     */
    public short get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Gets the element at the specified point.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * Point p = Point.of(1, 0);
     * short value = matrix.get(p); // returns 3
     * }</pre>
     * 
     * @param point the point containing row and column indices
     * @return the short value at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     */
    public short get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column indices to the given value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * matrix.set(0, 1, (short)5); // matrix becomes [[1, 5], [3, 4]]
     * }</pre>
     * 
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the value to set at position (i, j)
     * @throws ArrayIndexOutOfBoundsException if the indices are out of bounds
     */
    public void set(final int i, final int j, final short val) {
        a[i][j] = val;
    }

    /**
     * Sets the element at the specified point to the given value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * Point p = Point.of(0, 1);
     * matrix.set(p, (short)5); // matrix becomes [[1, 5], [3, 4]]
     * }</pre>
     * 
     * @param point the point containing row and column indices
     * @param val the value to set at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     */
    public void set(final Point point, final short val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element directly above the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * OptionalShort above = matrix.upOf(1, 0); // returns Optional of 1
     * OptionalShort none = matrix.upOf(0, 0); // returns empty Optional
     * }</pre>
     * 
     * @param i the row index of the reference position
     * @param j the column index of the reference position
     * @return an OptionalShort containing the element above, or empty if at the top row
     */
    public OptionalShort upOf(final int i, final int j) {
        return i == 0 ? OptionalShort.empty() : OptionalShort.of(a[i - 1][j]);
    }

    /**
     * Returns the element directly below the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * OptionalShort below = matrix.downOf(0, 0); // returns Optional of 3
     * OptionalShort none = matrix.downOf(1, 0); // returns empty Optional
     * }</pre>
     * 
     * @param i the row index of the reference position
     * @param j the column index of the reference position
     * @return an OptionalShort containing the element below, or empty if at the bottom row
     */
    public OptionalShort downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalShort.empty() : OptionalShort.of(a[i + 1][j]);
    }

    /**
     * Returns the element directly to the left of the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * OptionalShort left = matrix.leftOf(0, 1); // returns Optional of 1
     * OptionalShort none = matrix.leftOf(0, 0); // returns empty Optional
     * }</pre>
     * 
     * @param i the row index of the reference position
     * @param j the column index of the reference position
     * @return an OptionalShort containing the element to the left, or empty if at the leftmost column
     */
    public OptionalShort leftOf(final int i, final int j) {
        return j == 0 ? OptionalShort.empty() : OptionalShort.of(a[i][j - 1]);
    }

    /**
     * Returns the element directly to the right of the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * OptionalShort right = matrix.rightOf(0, 0); // returns Optional of 2
     * OptionalShort none = matrix.rightOf(0, 1); // returns empty Optional
     * }</pre>
     * 
     * @param i the row index of the reference position
     * @param j the column index of the reference position
     * @return an OptionalShort containing the element to the right, or empty if at the rightmost column
     */
    public OptionalShort rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalShort.empty() : OptionalShort.of(a[i][j + 1]);
    }

    /**
     * Returns the four adjacent points (up, right, down, left) of the specified position.
     * Points that would be outside the matrix bounds are returned as null.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> adjacent = matrix.adjacent4Points(1, 1); 
     * // Returns stream of: Point(0,1), Point(1,2), Point(2,1), Point(1,0)
     * }</pre>
     * 
     * @param i the row index of the center position
     * @param j the column index of the center position
     * @return a Stream of Points in order: up, right, down, left (null for out-of-bounds positions)
     */
    public Stream<Point> adjacent4Points(final int i, final int j) {
        final Point up = i == 0 ? null : Point.of(i - 1, j);
        final Point right = j == cols - 1 ? null : Point.of(i, j + 1);
        final Point down = i == rows - 1 ? null : Point.of(i + 1, j);
        final Point left = j == 0 ? null : Point.of(i, j - 1);

        return Stream.of(up, right, down, left);
    }

    /**
     * Returns the eight adjacent points (including diagonals) of the specified position.
     * Points that would be outside the matrix bounds are returned as null.
     * The order is: left-up, up, right-up, right, right-down, down, left-down, left.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> adjacent = matrix.adjacent8Points(1, 1); 
     * // Returns stream of all 8 surrounding points of position (1,1)
     * }</pre>
     * 
     * @param i the row index of the center position
     * @param j the column index of the center position
     * @return a Stream of Points in clockwise order starting from left-up (null for out-of-bounds positions)
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
     * Returns a copy of the specified row as a short array.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * short[] row1 = matrix.row(1); // returns [4, 5, 6]
     * }</pre>
     * 
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return a short array containing the values of the specified row
     * @throws IllegalArgumentException if rowIndex is negative or >= number of rows
     */
    public short[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as a short array.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * short[] col1 = matrix.column(1); // returns [2, 5]
     * }</pre>
     * 
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a short array containing the values of the specified column
     * @throws IllegalArgumentException if columnIndex is negative or >= number of columns
     */
    public short[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final short[] c = new short[rows];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i][columnIndex];
        }

        return c;
    }

    /**
     * Sets the values of the specified row to the values in the provided array.
     * The length of the array must match the number of columns in the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.setRow(0, new short[]{7, 8, 9}); // matrix becomes [[7, 8, 9], [4, 5, 6]]
     * }</pre>
     * 
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to set in the row
     * @throws IllegalArgumentException if the array length doesn't match the number of columns
     */
    public void setRow(final int rowIndex, final short[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Sets the values of the specified column to the values in the provided array.
     * The length of the array must match the number of rows in the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.setColumn(1, new short[]{7, 8}); // matrix becomes [[1, 7, 3], [4, 8, 6]]
     * }</pre>
     * 
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to set in the column
     * @throws IllegalArgumentException if the array length doesn't match the number of rows
     */
    public void setColumn(final int columnIndex, final short[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all elements in the specified row by applying the given function to each element.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.updateRow(0, x -> (short)(x * 2)); // First row becomes [2, 4, 6]
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param rowIndex the index of the row to update (0-based)
     * @param func the function to apply to each element in the row
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.ShortUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsShort(a[rowIndex][i]);
        }
    }

    /**
     * Updates all elements in the specified column by applying the given function to each element.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.updateColumn(1, x -> (short)(x + 10)); // Second column becomes [12, 15]
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param columnIndex the index of the column to update (0-based)
     * @param func the function to apply to each element in the column
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.ShortUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsShort(a[i][columnIndex]);
        }
    }

    /**
     * Gets the values on the main diagonal (left-up to right-down) of the matrix.
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * short[] diagonal = matrix.getLU2RD(); // returns [1, 5, 9]
     * }</pre>
     * 
     * @return a short array containing the diagonal values
     * @throws IllegalStateException if the matrix is not square
     */
    public short[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final short[] res = new short[rows];

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][i]; // NOSONAR
        }

        return res;
    }

    /**
     * Sets the values on the main diagonal (left-up to right-down) of the matrix.
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.setLU2RD(new short[]{10, 11, 12}); // Diagonal becomes [10, 11, 12]
     * }</pre>
     * 
     * @param diagonal the array of values to set on the diagonal
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
     */
    public void setLU2RD(final short[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates all elements on the main diagonal (left-up to right-down) by applying the given function.
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.updateLU2RD(x -> (short)(x * 2)); // Diagonal [1, 5, 9] becomes [2, 10, 18]
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateLU2RD(final Throwables.ShortUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsShort(a[i][i]);
        }
    }

    /**
     * Gets the values on the anti-diagonal (right-up to left-down) of the matrix.
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * short[] antiDiagonal = matrix.getRU2LD(); // returns [3, 5, 7]
     * }</pre>
     * 
     * @return a short array containing the anti-diagonal values
     * @throws IllegalStateException if the matrix is not square
     */
    public short[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final short[] res = new short[rows];

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][cols - i - 1];
        }

        return res;
    }

    /**
     * Sets the values on the anti-diagonal (right-up to left-down) of the matrix.
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.setRU2LD(new short[]{10, 11, 12}); // Anti-diagonal becomes [10, 11, 12]
     * }</pre>
     * 
     * @param diagonal the array of values to set on the anti-diagonal
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
     */
    public void setRU2LD(final short[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = diagonal[i];
        }
    }

    /**
     * Updates all elements on the anti-diagonal (right-up to left-down) by applying the given function.
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.updateRU2LD(x -> (short)(x + 1)); // Anti-diagonal [3, 5, 7] becomes [4, 6, 8]
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each anti-diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateRU2LD(final Throwables.ShortUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsShort(a[i][cols - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix by applying the given function to each element.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * matrix.updateAll(x -> (short)(x * 2)); // All elements are doubled
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each element
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.ShortUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.applyAsShort(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Updates all elements in the matrix based on their position by applying the given function.
     * The function receives the row and column indices and returns the new value for that position.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * matrix.updateAll((i, j) -> (short)(i + j)); // Element at (i,j) becomes i+j
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function that takes row index, column index and returns the new value
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Short, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements that match the given predicate with the specified new value.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.replaceIf(x -> x > 3, (short)0); // All elements > 3 become 0
     * }</pre>
     * 
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.ShortPredicate<E> predicate, final short newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces elements based on their position by testing with the given predicate.
     * The predicate receives the row and column indices and returns {@code true} if the element
     * at that position should be replaced with the new value.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.replaceIf((i, j) -> i == j, (short)0); // Replace diagonal elements with 0
     * }</pre>
     * 
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition that takes row index, column index and returns {@code true} if element should be replaced
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final short newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new matrix by applying the given function to each element of this matrix.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix squared = matrix.map(x -> (short)(x * x)); // Elements become squared
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each element
     * @return a new ShortMatrix with the transformed values
     * @throws E if the function throws an exception
     */
    public <E extends Exception> ShortMatrix map(final Throwables.ShortUnaryOperator<E> func) throws E {
        final short[][] result = new short[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsShort(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Creates a new object matrix by applying the given function to each element of this matrix.
     * The function transforms each short value to an object of the specified type.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * Matrix<String> stringMatrix = matrix.mapToObj(x -> "Value: " + x, String.class);
     * }</pre>
     * 
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the type of exception that the function may throw
     * @param func the function to transform each short to type T
     * @param targetElementType the class of the target element type
     * @return a new Matrix<T> with the transformed values
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.ShortFunction<? extends T, E> func, final Class<T> targetElementType) throws E {
        final T[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Fills all elements of the matrix with the specified value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * matrix.fill((short)5); // All elements become 5
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
     * Fills the matrix with values from another 2D array, starting at position (0, 0).
     * The source array can be smaller than this matrix; only the overlapping region is copied.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{0, 0, 0}, {0, 0, 0}});
     * matrix.fill(new short[][]{{1, 2}, {3, 4}});
     * // matrix becomes [[1, 2, 0], [3, 4, 0]]
     * }</pre>
     * 
     * @param b the 2D array to copy values from
     */
    public void fill(final short[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a region of the matrix with values from another 2D array, starting at the specified position.
     * The source array can extend beyond this matrix's bounds; only the overlapping region is copied.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
     * matrix.fill(1, 1, new short[][]{{1, 2}, {3, 4}});
     * // matrix becomes [[0, 0, 0], [0, 1, 2], [0, 3, 4]]
     * }</pre>
     * 
     * @param fromRowIndex the starting row index in this matrix (0-based)
     * @param fromColumnIndex the starting column index in this matrix (0-based)
     * @param b the 2D array to copy values from
     * @throws IndexOutOfBoundsException if fromRowIndex or fromColumnIndex is negative or out of bounds
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final short[][] b) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, rows, rows);
        N.checkFromToIndex(fromColumnIndex, cols, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
        }
    }

    /**
     * Creates a deep copy of this matrix. The returned matrix has its own copy
     * of the underlying data array.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix copy = matrix.copy();
     * copy.set(0, 0, (short)9); // Original matrix is unchanged
     * }</pre>
     * 
     * @return a new ShortMatrix that is a deep copy of this matrix
     */
    @Override
    public ShortMatrix copy() {
        final short[][] c = new short[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new ShortMatrix(c);
    }

    /**
     * Creates a deep copy of a subset of rows from this matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}, {5, 6}});
     * ShortMatrix subset = matrix.copy(1, 3); // Returns matrix with rows [[3, 4], [5, 6]]
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new ShortMatrix containing the specified rows
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @Override
    public ShortMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final short[][] c = new short[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new ShortMatrix(c);
    }

    /**
     * Creates a deep copy of a rectangular region from this matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * ShortMatrix region = matrix.copy(0, 2, 1, 3); // Returns [[2, 3], [5, 6]]
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new ShortMatrix containing the specified region
     * @throws IndexOutOfBoundsException if any indices are out of bounds
     */
    @Override
    public ShortMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final short[][] c = new short[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new ShortMatrix(c);
    }

    /**
     * Creates a resized copy of this matrix with the specified dimensions.
     * If the new dimensions are smaller, the matrix is truncated. If larger,
     * the new cells are filled with zero (the default short value).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix extended = matrix.extend(3, 3); // Returns [[1, 2, 0], [3, 4, 0], [0, 0, 0]]
     * }</pre>
     * 
     * @param newRows the number of rows in the resulting matrix
     * @param newCols the number of columns in the resulting matrix
     * @return a new ShortMatrix with the specified dimensions
     */
    public ShortMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, SHORT_0);
    }

    /**
     * Creates a resized copy of this matrix with the specified dimensions.
     * If the new dimensions are smaller, the matrix is truncated. If larger,
     * the new cells are filled with the specified default value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix extended = matrix.extend(3, 3, (short)9); 
     * // Returns [[1, 2, 9], [3, 4, 9], [9, 9, 9]]
     * }</pre>
     * 
     * @param newRows the number of rows in the resulting matrix
     * @param newCols the number of columns in the resulting matrix
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new ShortMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public ShortMatrix extend(final int newRows, final int newCols, final short defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            final boolean fillDefaultValue = defaultValueForNewCell != SHORT_0;
            final short[][] b = new short[newRows][];

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : new short[newCols];

                if (fillDefaultValue) {
                    if (i >= rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, defaultValueForNewCell);
                    }
                }
            }

            return new ShortMatrix(b);
        }
    }

    /**
     * Creates an extended copy of this matrix by adding rows and columns in all directions.
     * New cells are filled with zero (the default short value).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix extended = matrix.extend(1, 1, 1, 1);
     * // Returns [[0, 0, 0, 0],
     * //          [0, 1, 2, 0],
     * //          [0, 3, 4, 0],
     * //          [0, 0, 0, 0]]
     * }</pre>
     * 
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @return a new ShortMatrix with the extended dimensions
     */
    public ShortMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, SHORT_0);
    }

    /**
     * Creates an extended copy of this matrix by adding rows and columns in all directions.
     * New cells are filled with the specified default value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix extended = matrix.extend(1, 1, 1, 1, (short)9);
     * // Returns [[9, 9, 9, 9],
     * //          [9, 1, 2, 9],
     * //          [9, 3, 4, 9],
     * //          [9, 9, 9, 9]]
     * }</pre>
     * 
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new ShortMatrix with the extended dimensions
     * @throws IllegalArgumentException if any extension parameter is negative
     */
    public ShortMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final short defaultValueForNewCell)
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
            final boolean fillDefaultValue = defaultValueForNewCell != SHORT_0;
            final short[][] b = new short[newRows][newCols];

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

            return new ShortMatrix(b);
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
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
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
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
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
     * Rotates the matrix 90 degrees clockwise.
     * The result is a new matrix where the first row of the original becomes the last column of the result.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * ShortMatrix rotated = matrix.rotate90();
     * // Result: [[4, 1],
     * //          [5, 2],
     * //          [6, 3]]
     * }</pre>
     * 
     * @return a new matrix rotated 90 degrees clockwise
     */
    @Override
    public ShortMatrix rotate90() {
        final short[][] c = new short[cols][rows];

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

        return new ShortMatrix(c);
    }

    /**
     * Rotates the matrix 180 degrees.
     * The result is a new matrix where both rows and columns are reversed.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * ShortMatrix rotated = matrix.rotate180();
     * // Result: [[6, 5, 4],
     * //          [3, 2, 1]]
     * }</pre>
     * 
     * @return a new matrix rotated 180 degrees
     */
    @Override
    public ShortMatrix rotate180() {
        final short[][] c = new short[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new ShortMatrix(c);
    }

    /**
     * Rotates the matrix 270 degrees clockwise (or 90 degrees counter-clockwise).
     * The result is a new matrix where the first row of the original becomes the first column of the result.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * ShortMatrix rotated = matrix.rotate270();
     * // Result: [[3, 6],
     * //          [2, 5],
     * //          [1, 4]]
     * }</pre>
     * 
     * @return a new matrix rotated 270 degrees clockwise
     */
    @Override
    public ShortMatrix rotate270() {
        final short[][] c = new short[cols][rows];

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

        return new ShortMatrix(c);
    }

    /**
     * Transposes the matrix by swapping rows and columns.
     * The result is a new matrix where element at position (i,j) becomes element at position (j,i).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * ShortMatrix transposed = matrix.transpose();
     * // Result: [[1, 4],
     * //          [2, 5],
     * //          [3, 6]]
     * }</pre>
     * 
     * @return a new transposed matrix
     */
    @Override
    public ShortMatrix transpose() {
        final short[][] c = new short[cols][rows];

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

        return new ShortMatrix(c);
    }

    /**
     * Reshapes the matrix to the specified dimensions.
     * Elements are read in row-major order from the original matrix and placed into the new shape.
     * The total number of elements must remain compatible with the new dimensions.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * ShortMatrix reshaped = matrix.reshape(3, 2);
     * // Result: [[1, 2],
     * //          [3, 4],
     * //          [5, 6]]
     * }</pre>
     * 
     * @param newRows the number of rows in the reshaped matrix
     * @param newCols the number of columns in the reshaped matrix
     * @return a new matrix with the specified dimensions
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public ShortMatrix reshape(final int newRows, final int newCols) {
        final short[][] c = new short[newRows][newCols];

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new ShortMatrix(c);
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

        return new ShortMatrix(c);
    }

    /**
     * Repeats elements in the matrix by the specified factors in both row and column directions.
     * Each element is repeated {@code rowRepeats} times in the row direction and {@code colRepeats} 
     * times in the column direction.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix repeated = matrix.repelem(2, 3);
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
    public ShortMatrix repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final short[][] c = new short[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            final short[] aa = a[i];
            final short[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                N.copy(Array.repeat(aa[j], colRepeats), 0, fr, j * colRepeats, colRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new ShortMatrix(c);
    }

    /**
     * Repeats the entire matrix as a tile pattern by the specified factors in both row and column directions.
     * The whole matrix is repeated {@code rowRepeats} times in the row direction and {@code colRepeats} 
     * times in the column direction.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix tiled = matrix.repmat(2, 3);
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
    public ShortMatrix repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final short[][] c = new short[rows * rowRepeats][cols * colRepeats];

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

        return new ShortMatrix(c);
    }

    /**
     * Flattens the matrix into a single-dimensional list by concatenating all rows.
     * Elements are arranged in row-major order (row by row).
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * ShortList flat = matrix.flatten();
     * // Result: [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @return a new {@code ShortList} containing all elements of the matrix in row-major order
     */
    @Override
    public ShortList flatten() {
        final short[] c = new short[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return ShortList.of(c);
    }

    /**
     * Flattens the underlying 2D array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure.
     *
     * @param <E> the type of exception that the operation may throw
     * @param op the operation to apply to the underlying 2D array
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
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][]{{1, 2, 3}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][]{{4, 5, 6}, {7, 8, 9}});
     * ShortMatrix stacked = matrix1.vstack(matrix2);
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
    public ShortMatrix vstack(final ShortMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final short[][] c = new short[rows + b.rows][];
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
        }

        return ShortMatrix.of(c);
    }

    /**
     * Horizontally stacks this matrix with another matrix.
     * The two matrices must have the same number of rows.
     * The result is a new matrix where the columns of the specified matrix are appended to the right of this matrix.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][]{{5}, {6}});
     * ShortMatrix stacked = matrix1.hstack(matrix2);
     * // Result: [[1, 2, 5],
     * //          [3, 4, 6]]
     * }</pre>
     *
     * @param b the matrix to stack to the right of this matrix
     * @return a new matrix with columns from both matrices stacked horizontally
     * @throws IllegalArgumentException if the matrices don't have the same number of rows
     * @see IntMatrix#hstack(IntMatrix)
     */
    public ShortMatrix hstack(final ShortMatrix b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final short[][] c = new short[rows][cols + b.cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c[i], 0, cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
        }

        return ShortMatrix.of(c);
    }

    /**
     * Performs element-wise addition of this matrix with another matrix.
     * The two matrices must have the same dimensions.
     * Note: Due to short arithmetic, overflow may occur without warning.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][]{{5, 6}, {7, 8}});
     * ShortMatrix sum = matrix1.add(matrix2);
     * // Result: [[6, 8],
     * //          [10, 12]]
     * }</pre>
     *
     * @param b the matrix to add to this matrix
     * @return a new matrix containing the element-wise sum
     * @throws IllegalArgumentException if the matrices don't have the same shape
     */
    public ShortMatrix add(final ShortMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't add Matrixes with different shape");

        final short[][] ba = b.a;
        final short[][] result = new short[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (short) (a[i][j] + ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Performs element-wise subtraction of another matrix from this matrix.
     * The two matrices must have the same dimensions.
     * Note: Due to short arithmetic, overflow may occur without warning.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][]{{5, 6}, {7, 8}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix diff = matrix1.subtract(matrix2);
     * // Result: [[4, 4],
     * //          [4, 4]]
     * }</pre>
     *
     * @param b the matrix to subtract from this matrix
     * @return a new matrix containing the element-wise difference
     * @throws IllegalArgumentException if the matrices don't have the same shape
     */
    public ShortMatrix subtract(final ShortMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't subtract Matrixes with different shape");

        final short[][] ba = b.a;
        final short[][] result = new short[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (short) (a[i][j] - ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Performs matrix multiplication with another matrix.
     * The number of columns in this matrix must equal the number of rows in the specified matrix.
     * The result is a new matrix with dimensions (this.rows × b.cols).
     * Note: Due to short arithmetic, overflow may occur without warning.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][]{{5, 6}, {7, 8}});
     * ShortMatrix product = matrix1.multiply(matrix2);
     * // Result: [[19, 22],
     * //          [43, 50]]
     * }</pre>
     *
     * @param b the matrix to multiply with this matrix
     * @return a new matrix containing the matrix product
     * @throws IllegalArgumentException if the matrix dimensions are incompatible for multiplication
     */
    public ShortMatrix multiply(final ShortMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.rows, "Illegal matrix dimensions");

        final short[][] ba = b.a;
        final short[][] result = new short[rows][b.cols];
        final Throwables.IntTriConsumer<RuntimeException> cmd = (i, j, k) -> result[i][j] += (short) (a[i][k] * ba[k][j]);

        Matrixes.multiply(this, b, cmd);

        return new ShortMatrix(result);
    }

    /**
     * Converts this primitive short matrix to a boxed {@code Matrix<Short>}.
     * Each primitive short value is boxed into a {@code Short} object.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix primitive = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * Matrix<Short> boxed = primitive.boxed();
     * // Result: Matrix containing Short objects instead of primitives
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
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix shortMatrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
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
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix shortMatrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
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
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix shortMatrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
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
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix shortMatrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
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
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][]{{5, 6}, {7, 8}});
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
        N.checkArgument(isSameShape(matrixB), "Can't zip two or more matrices which don't have same shape");

        final short[][] b = matrixB.a;
        final short[][] result = new short[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsShort(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Applies a ternary operation element-wise to this matrix and two other matrices.
     * All three matrices must have the same dimensions.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix1 = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * ShortMatrix matrix2 = ShortMatrix.of(new short[][]{{5, 6}, {7, 8}});
     * ShortMatrix matrix3 = ShortMatrix.of(new short[][]{{9, 10}, {11, 12}});
     * ShortMatrix median = matrix1.zipWith(matrix2, matrix3, 
     *     (a, b, c) -> (short)((a + b + c) / 3));
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
    public <E extends Exception> ShortMatrix zipWith(final ShortMatrix matrixB, final ShortMatrix matrixC, final Throwables.ShortTernaryOperator<E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Can't zip two or more matrices which don't have same shape");

        final short[][] b = matrixB.a;
        final short[][] c = matrixC.a;
        final short[][] result = new short[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsShort(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Creates a stream of elements on the diagonal from left-upper to right-down.
     * The matrix must be square (same number of rows and columns).
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, 
     *                                                   {4, 5, 6}, 
     *                                                   {7, 8, 9}});
     * ShortStream diagonal = matrix.streamLU2RD();
     * // Stream contains: 1, 5, 9
     * }</pre>
     *
     * @return a stream of diagonal elements from left-upper to right-down
     * @throws IllegalStateException if the matrix is not square
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
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, 
     *                                                   {4, 5, 6}, 
     *                                                   {7, 8, 9}});
     * ShortStream diagonal = matrix.streamRU2LD();
     * // Stream contains: 3, 5, 7
     * }</pre>
     *
     * @return a stream of diagonal elements from right-upper to left-down
     * @throws IllegalStateException if the matrix is not square
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
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * ShortStream stream = matrix.streamH();
     * // Stream contains: 1, 2, 3, 4, 5, 6
     * }</pre>
     *
     * @return a stream of all matrix elements in row-major order
     */
    @Override
    public ShortStream streamH() {
        return streamH(0, rows);
    }

    /**
     * Creates a stream of elements from a specific row.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * ShortStream row = matrix.streamH(1);
     * // Stream contains: 4, 5, 6
     * }</pre>
     *
     * @param rowIndex the index of the row to stream
     * @return a stream of elements from the specified row
     * @throws IndexOutOfBoundsException if the row index is out of bounds
     */
    @Override
    public ShortStream streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * Creates a stream of elements from a range of rows in row-major order.
     * Elements are streamed row by row from left to right.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}, {5, 6}});
     * ShortStream stream = matrix.streamH(1, 3);
     * // Stream contains: 3, 4, 5, 6
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of elements from the specified row range
     * @throws IndexOutOfBoundsException if the row indices are out of bounds
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
            public short[] toArray() {
                final int len = (int) count();
                final short[] c = new short[len];

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
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * ShortStream stream = matrix.streamV();
     * // Stream contains: 1, 4, 2, 5, 3, 6
     * }</pre>
     *
     * @return a stream of all matrix elements in column-major order
     */
    @Override
    @Beta
    public ShortStream streamV() {
        return streamV(0, cols);
    }

    /**
     * Creates a stream of elements from a specific column.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * ShortStream column = matrix.streamV(1);
     * // Stream contains: 2, 5
     * }</pre>
     *
     * @param columnIndex the index of the column to stream
     * @return a stream of elements from the specified column
     * @throws IndexOutOfBoundsException if the column index is out of bounds
     */
    @Override
    public ShortStream streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * Creates a stream of elements from a range of columns in column-major order.
     * Elements are streamed column by column from top to bottom.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * ShortStream stream = matrix.streamV(1, 3);
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
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                if (n >= (long) (toColumnIndex - j) * ShortMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    i += (int) ((n + i) % ShortMatrix.this.rows);
                    j += (int) ((n + i) / ShortMatrix.this.rows);
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public short[] toArray() {
                final int len = (int) count();
                final short[] c = new short[len];

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
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<ShortStream> rows = matrix.streamR();
     * // First stream contains: 1, 2, 3
     * // Second stream contains: 4, 5, 6
     * }</pre>
     *
     * @return a stream of row streams
     */
    @Override
    public Stream<ShortStream> streamR() {
        return streamR(0, rows);
    }

    /**
     * Creates a stream of row streams from a range of rows.
     * Each element in the returned stream is a stream of a complete row.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}, {5, 6}});
     * Stream<ShortStream> rows = matrix.streamR(1, 3);
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
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<ShortStream> columns = matrix.streamC();
     * // First stream contains: 1, 4
     * // Second stream contains: 2, 5
     * // Third stream contains: 3, 6
     * }</pre>
     *
     * @return a stream of column streams
     */
    @Override
    @Beta
    public Stream<ShortStream> streamC() {
        return streamC(0, cols);
    }

    /**
     * Creates a stream of column streams from a range of columns.
     * Each element in the returned stream is a stream of a complete column.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<ShortStream> columns = matrix.streamC(1, 3);
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
    protected int length(@SuppressWarnings("hiding") final short[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * Applies the specified action to each element in the matrix.
     * Elements are processed in row-major order.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
     * matrix.forEach(value -> System.out.print(value + " "));
     * // Output: 1 2 3 4
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the action to apply to each element
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> action) throws E {
        forEach(0, rows, 0, cols, action);
    }

    /**
     * Applies the specified action to each element in a sub-region of the matrix.
     * Elements are processed in row-major order within the specified bounds.
     *
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
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
            final Throwables.ShortConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, Matrixes.isParallelable(this));
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
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2, 3}, {4, 5, 6}});
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
     * @return {@code true} if the specified object is a ShortMatrix with the same dimensions and elements
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
     * The string representation consists of the matrix elements formatted as a 2D array.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ShortMatrix matrix = ShortMatrix.of(new short[][]{{1, 2}, {3, 4}});
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
