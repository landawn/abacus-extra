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
import com.landawn.abacus.util.u.OptionalChar;
import com.landawn.abacus.util.stream.CharIteratorEx;
import com.landawn.abacus.util.stream.CharStream;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

/**
 * A matrix implementation for char primitive values, providing efficient storage and operations
 * for two-dimensional char arrays. This class extends AbstractMatrix and provides specialized
 * methods for char matrix manipulation including mathematical operations, transformations,
 * and element access.
 * 
 * <p>The matrix is backed by a 2D char array and provides methods for:
 * <ul>
 *   <li>Basic matrix operations (add, subtract, multiply)</li>
 *   <li>Transformations (transpose, rotate, flip)</li>
 *   <li>Element access and modification</li>
 *   <li>Streaming operations for rows, columns, and diagonals</li>
 *   <li>Reshaping and extending operations</li>
 * </ul>
 * 
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
 * char value = matrix.get(0, 1); // returns 'b'
 * matrix.transpose(); // returns new matrix with rows and columns swapped
 * }</pre>
 * 
 * @see AbstractMatrix
 * @see IntMatrix
 * @see DoubleMatrix
 */
public final class CharMatrix extends AbstractMatrix<char[], CharList, CharStream, Stream<CharStream>, CharMatrix> {

    static final CharMatrix EMPTY_CHAR_MATRIX = new CharMatrix(new char[0][0]);

    /**
     * Constructs a CharMatrix from a 2D char array.
     *
     * <p>The provided array is used directly as the internal storage without copying.
     * If the input array is null, an empty matrix (0x0) is created instead.
     *
     * <p><b>Important:</b> Since the array is not copied, any external modifications
     * to the array will affect this matrix. For a safe copy, use {@link #of(char[][])} instead.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] data = {{'a', 'b'}, {'c', 'd'}};
     * CharMatrix matrix = new CharMatrix(data);
     * // matrix.rows() returns 2, matrix.cols() returns 2
     *
     * CharMatrix empty = new CharMatrix(null);
     * // empty.rows() returns 0, empty.cols() returns 0
     * }</pre>
     *
     * @param a the 2D char array to wrap, or null for an empty matrix
     */
    public CharMatrix(final char[][] a) {
        super(a == null ? new char[0][0] : a);
    }

    /**
     * Creates an empty matrix with zero rows and zero columns.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.empty();
     * // matrix.rows() returns 0
     * // matrix.columns() returns 0
     * }</pre>
     *
     * @return an empty char matrix
     */
    public static CharMatrix empty() {
        return EMPTY_CHAR_MATRIX;
    }

    /**
     * Creates a CharMatrix from a 2D char array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * // matrix.get(1, 0) returns 'c'
     * }</pre>
     *
     * @param a the 2D char array to create the matrix from, or null/empty for an empty matrix
     * @return a new CharMatrix containing the provided data, or an empty CharMatrix if input is null or empty
     */
    public static CharMatrix of(final char[]... a) {
        return N.isEmpty(a) ? EMPTY_CHAR_MATRIX : new CharMatrix(a);
    }

    /**
     * Creates a 1-row matrix filled with random values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.random(5);
     * // Creates a 1x5 matrix with random char values
     * }</pre>
     *
     * @param len the number of columns
     * @return a 1-row matrix filled with random char values
     */
    @SuppressWarnings("deprecation")
    public static CharMatrix random(final int len) {
        return new CharMatrix(new char[][] { CharList.random(len).array() });
    }

    /**
     * Creates a 1-row matrix with all elements set to the specified value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.repeat('x', 5);
     * // Creates a 1x5 matrix where all elements are 'x'
     * }</pre>
     *
     * @param val the value to repeat
     * @param len the number of columns
     * @return a 1-row matrix with all elements set to val
     */
    public static CharMatrix repeat(final char val, final int len) {
        return new CharMatrix(new char[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a single-row CharMatrix containing a range of char values.
     * The range is [startInclusive, endExclusive).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.range('a', 'e'); // Creates [['a', 'b', 'c', 'd']]
     * }</pre>
     *
     * @param startInclusive the starting char value (inclusive)
     * @param endExclusive the ending char value (exclusive)
     * @return a CharMatrix with one row containing the range of values
     */
    public static CharMatrix range(final char startInclusive, final char endExclusive) {
        return new CharMatrix(new char[][] { Array.range(startInclusive, endExclusive) });
    }

    /**
     * Creates a single-row CharMatrix containing a range of char values with a step.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.range('a', 'g', 2); // Creates [['a', 'c', 'e']]
     * }</pre>
     *
     * @param startInclusive the starting char value (inclusive)
     * @param endExclusive the ending char value (exclusive)
     * @param by the step size between consecutive values
     * @return a CharMatrix with one row containing the range of values
     */
    public static CharMatrix range(final char startInclusive, final char endExclusive, final int by) {
        return new CharMatrix(new char[][] { Array.range(startInclusive, endExclusive, by) });
    }

    /**
     * Creates a single-row CharMatrix containing a closed range of char values.
     * The range is [startInclusive, endInclusive].
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.rangeClosed('a', 'd'); // Creates [['a', 'b', 'c', 'd']]
     * }</pre>
     *
     * @param startInclusive the starting char value (inclusive)
     * @param endInclusive the ending char value (inclusive)
     * @return a CharMatrix with one row containing the range of values
     */
    public static CharMatrix rangeClosed(final char startInclusive, final char endInclusive) {
        return new CharMatrix(new char[][] { Array.rangeClosed(startInclusive, endInclusive) });
    }

    /**
     * Creates a single-row CharMatrix containing a closed range of char values with a step.
     * The range is [startInclusive, endInclusive].
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.rangeClosed('a', 'g', 2); // Creates [['a', 'c', 'e', 'g']]
     * }</pre>
     *
     * @param startInclusive the starting char value (inclusive)
     * @param endInclusive the ending char value (inclusive)
     * @param by the step size between consecutive values
     * @return a CharMatrix with one row containing the range of values
     */
    public static CharMatrix rangeClosed(final char startInclusive, final char endInclusive, final int by) {
        return new CharMatrix(new char[][] { Array.rangeClosed(startInclusive, endInclusive, by) });
    }

    /**
     * Creates a square matrix from the specified main diagonal elements.
     * All other elements are set to zero.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.diagonalLU2RD(new char[] {'a', 'b', 'c'});
     * // Creates 3x3 matrix with diagonal ['a', 'b', 'c'] and zeros elsewhere
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of diagonal elements
     * @return a square matrix with the specified main diagonal
     */
    public static CharMatrix diagonalLU2RD(final char[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square matrix from the specified anti-diagonal elements.
     * All other elements are set to zero.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.diagonalRU2LD(new char[] {'a', 'b', 'c'});
     * // Creates 3x3 matrix with anti-diagonal ['a', 'b', 'c'] and zeros elsewhere
     * }</pre>
     *
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements
     * @return a square matrix with the specified anti-diagonal
     */
    public static CharMatrix diagonalRU2LD(final char[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a square matrix from the specified main diagonal and anti-diagonal elements.
     * All other elements are set to zero.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.diagonal(new char[] {'a', 'b'}, new char[] {'x', 'y'});
     * // Creates 2x2 matrix with both diagonals set
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements
     * @return a square matrix with the specified diagonals
     * @throws IllegalArgumentException if arrays have different lengths
     */
    public static CharMatrix diagonal(final char[] leftUp2RightDownDiagonal, final char[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        if (N.isEmpty(leftUp2RightDownDiagonal) && N.isEmpty(rightUp2LeftDownDiagonal)) {
            return EMPTY_CHAR_MATRIX;
        }

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final char[][] c = new char[len][len];

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

        return new CharMatrix(c);
    }

    /**
     * Converts a boxed Character matrix to a primitive CharMatrix.
     *
     * <p>This method unboxes each Character element to its primitive char value.
     * Null values in the input matrix are converted to the default char value ('\u0000' or 0).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<Character> boxed = Matrix.of(new Character[][] {{'a', 'b'}, {null, 'c'}});
     * CharMatrix unboxed = CharMatrix.unbox(boxed);
     * // unboxed contains [['a', 'b'], ['\u0000', 'c']]
     * }</pre>
     *
     * @param x the boxed Character matrix to convert
     * @return a new CharMatrix with unboxed primitive char values
     * @see #boxed()
     */
    public static CharMatrix unbox(final Matrix<Character> x) {
        return CharMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of the matrix elements, which is always {@code char.class}.
     * 
     * @return {@code char.class}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return char.class;
    }

    /**
     * Returns the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * char value = matrix.get(0, 1); // Returns 'b'
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position (i, j)
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public char get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Returns the element at the specified point.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * // Assuming you have a Point implementation
     * // char value = matrix.get(point); // Returns element at point
     * }</pre>
     *
     * @param point the point containing row and column indices
     * @return the element at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public char get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.set(0, 1, 'x'); // Sets element at row 0, column 1 to 'x'
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public void set(final int i, final int j, final char val) {
        a[i][j] = val;
    }

    /**
     * Sets the element at the specified point.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * // Assuming you have a Point implementation
     * // matrix.set(point, 'x'); // Sets element at point
     * }</pre>
     *
     * @param point the point containing row and column indices
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public void set(final Point point, final char val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element above the specified position, if it exists.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * OptionalChar value = matrix.upOf(1, 0); // Returns OptionalChar.of('a')
     * OptionalChar empty = matrix.upOf(0, 0); // Returns OptionalChar.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an Optional containing the element above, or empty if out of bounds
     */
    public OptionalChar upOf(final int i, final int j) {
        return i == 0 ? OptionalChar.empty() : OptionalChar.of(a[i - 1][j]);
    }

    /**
     * Returns the element below the specified position, if it exists.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * OptionalChar value = matrix.downOf(0, 0); // Returns OptionalChar.of('c')
     * OptionalChar empty = matrix.downOf(1, 0); // Returns OptionalChar.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an Optional containing the element below, or empty if out of bounds
     */
    public OptionalChar downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalChar.empty() : OptionalChar.of(a[i + 1][j]);
    }

    /**
     * Returns the element to the left of the specified position, if it exists.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * OptionalChar value = matrix.leftOf(0, 1); // Returns OptionalChar.of('a')
     * OptionalChar empty = matrix.leftOf(0, 0); // Returns OptionalChar.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an Optional containing the element to the left, or empty if out of bounds
     */
    public OptionalChar leftOf(final int i, final int j) {
        return j == 0 ? OptionalChar.empty() : OptionalChar.of(a[i][j - 1]);
    }

    /**
     * Returns the element to the right of the specified position, if it exists.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * OptionalChar value = matrix.rightOf(0, 0); // Returns OptionalChar.of('b')
     * OptionalChar empty = matrix.rightOf(0, 1); // Returns OptionalChar.empty()
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return an Optional containing the element to the right, or empty if out of bounds
     */
    public OptionalChar rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalChar.empty() : OptionalChar.of(a[i][j + 1]);
    }

    /**
     * Returns a stream of points adjacent to the specified position (up, down, left, right).
     * Only includes points within matrix bounds.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * Stream<Point> adjacent = matrix.adjacent4Points(0, 0);
     * // Returns stream of Point.of(0, 1) and Point.of(1, 0)
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return a stream of adjacent points (maximum 4)
     */
    public Stream<Point> adjacent4Points(final int i, final int j) {
        final Point up = i == 0 ? null : Point.of(i - 1, j);
        final Point right = j == cols - 1 ? null : Point.of(i, j + 1);
        final Point down = i == rows - 1 ? null : Point.of(i + 1, j);
        final Point left = j == 0 ? null : Point.of(i, j - 1);

        return Stream.of(up, right, down, left);
    }

    /**
     * Returns a stream of points adjacent to the specified position including diagonals.
     * Only includes points within matrix bounds.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}});
     * Stream<Point> adjacent = matrix.adjacent8Points(1, 1);
     * // Returns stream of all 8 surrounding points
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return a stream of adjacent points including diagonals (maximum 8)
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
     * <p><b>Important:</b> This method returns the internal array directly, not a copy.
     * Modifications to the returned array will affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * char[] firstRow = matrix.row(0); // Returns ['a', 'b', 'c']
     * firstRow[0] = 'x'; // This modifies the matrix!
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return the internal array of the specified row
     * @throws IllegalArgumentException if rowIndex is negative or &gt;= number of rows
     */
    public char[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as an array.
     *
     * <p>Unlike {@link #row(int)}, this method returns a new array containing
     * a copy of the column data. Modifications to the returned array will not
     * affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * char[] firstColumn = matrix.column(0); // Returns ['a', 'd']
     * firstColumn[0] = 'x'; // This does NOT modify the matrix
     * }</pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing a copy of the specified column
     * @throws IllegalArgumentException if columnIndex is negative or &gt;= number of columns
     */
    public char[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final char[] c = new char[rows];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i][columnIndex];
        }

        return c;
    }

    /**
     * Sets the values of the specified row by copying from the provided array.
     *
     * <p>The values from the source array are copied into the matrix row.
     * The source array must have exactly the same length as the number of columns in the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * matrix.setRow(0, new char[] {'x', 'y', 'z'}); // First row is now ['x', 'y', 'z']
     * }</pre>
     *
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to copy; must have length equal to number of columns
     * @throws IllegalArgumentException if row length does not match the number of columns
     * @throws ArrayIndexOutOfBoundsException if rowIndex is negative or &gt;= number of rows
     */
    public void setRow(final int rowIndex, final char[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Sets the values of the specified column by copying from the provided array.
     *
     * <p>The values from the source array are copied into the matrix column.
     * The source array must have exactly the same length as the number of rows in the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * matrix.setColumn(0, new char[] {'x', 'y'}); // First column is now ['x', 'y']
     * // Matrix is now: [['x', 'b', 'c'], ['y', 'e', 'f']]
     * }</pre>
     *
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to copy; must have length equal to number of rows
     * @throws IllegalArgumentException if column length does not match the number of rows
     * @throws ArrayIndexOutOfBoundsException if columnIndex is negative or &gt;= number of columns
     */
    public void setColumn(final int columnIndex, final char[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Applies the specified function to update all elements in the specified row in-place.
     *
     * <p>Each element in the row is replaced with the result of applying the function
     * to that element. The operation modifies the matrix directly.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.updateRow(0, c -> Character.toUpperCase(c));
     * // First row is now ['A', 'B']
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param rowIndex the row index to update (0-based)
     * @param func the function to apply to each element in the row
     * @throws E if the function throws an exception
     * @throws ArrayIndexOutOfBoundsException if rowIndex is negative or &gt;= number of rows
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.CharUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsChar(a[rowIndex][i]);
        }
    }

    /**
     * Applies the specified function to update all elements in the specified column in-place.
     *
     * <p>Each element in the column is replaced with the result of applying the function
     * to that element. The operation modifies the matrix directly.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'A', 'B'}, {'C', 'D'}});
     * matrix.updateColumn(1, c -> Character.toLowerCase(c));
     * // Second column is now ['b', 'd']
     * // Matrix is now: [['A', 'b'], ['C', 'd']]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param columnIndex the column index to update (0-based)
     * @param func the function to apply to each element in the column
     * @throws E if the function throws an exception
     * @throws ArrayIndexOutOfBoundsException if columnIndex is negative or &gt;= number of columns
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.CharUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsChar(a[i][columnIndex]);
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
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}});
     * char[] diagonal = matrix.getLU2RD(); // Returns ['a', 'e', 'i']
     * }</pre>
     *
     * @return a new char array containing the main diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public char[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final char[] res = new char[rows];

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
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'},
     *                                                 {'d', 'e', 'f'},
     *                                                 {'g', 'h', 'i'}});
     * matrix.setLU2RD(new char[] {'x', 'y', 'z'});
     * // Diagonal is now ['x', 'y', 'z']
     * }</pre>
     *
     * @param diagonal the new values for the main diagonal; must have length &gt;= rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length &lt; rows
     */
    public void setLU2RD(final char[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates the elements on the main diagonal (left-upper to right-down) using the specified function.
     * The matrix must be square. Each diagonal element is replaced with the result of applying
     * the function to that element.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.updateLU2RD(c -> Character.toUpperCase(c));
     * // Diagonal is now ['A', 'D'], matrix: [['A', 'b'], ['c', 'D']]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the function to apply to each diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square (rows != cols)
     */
    public <E extends Exception> void updateLU2RD(final Throwables.CharUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsChar(a[i][i]);
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
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'},
     *                                                 {'d', 'e', 'f'},
     *                                                 {'g', 'h', 'i'}});
     * char[] diagonal = matrix.getRU2LD(); // Returns ['c', 'e', 'g']
     * }</pre>
     *
     * @return a new char array containing the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public char[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final char[] res = new char[rows];

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
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'},
     *                                                 {'d', 'e', 'f'},
     *                                                 {'g', 'h', 'i'}});
     * matrix.setRU2LD(new char[] {'x', 'y', 'z'});
     * // Anti-diagonal is now ['x', 'y', 'z']
     * }</pre>
     *
     * @param diagonal the new values for the anti-diagonal; must have length &gt;= rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length &lt; rows
     */
    public void setRU2LD(final char[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = diagonal[i];
        }
    }

    /**
     * Updates the elements on the anti-diagonal (right-upper to left-down) using the specified function.
     * The matrix must be square. Each anti-diagonal element is replaced with the result of applying
     * the function to that element.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.updateRU2LD(c -> Character.toUpperCase(c));
     * // Anti-diagonal is now ['B', 'C'], matrix: [['a', 'B'], ['C', 'd']]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the function to apply to each anti-diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square (rows != cols)
     */
    public <E extends Exception> void updateRU2LD(final Throwables.CharUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsChar(a[i][cols - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix using the specified function in-place.
     *
     * <p>Each element is replaced with the result of applying the function to that element.
     * For large matrices, this operation may be performed in parallel to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.updateAll(c -> Character.toUpperCase(c));
     * // Matrix is now [['A', 'B'], ['C', 'D']]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the function to apply to each element
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.CharUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.applyAsChar(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Updates all elements in the matrix based on their position using a position-aware function.
     *
     * <p>The function receives the row and column indices and returns the new value for that position.
     * This is useful when the new value depends on the element's location in the matrix.
     * For large matrices, this operation may be performed in parallel.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[3][4]);
     * matrix.updateAll((i, j) -> (char)('a' + i * 4 + j));
     * // Creates a matrix filled with sequential characters based on position
     * // Result: [['a', 'b', 'c', 'd'],
     * //          ['e', 'f', 'g', 'h'],
     * //          ['i', 'j', 'k', 'l']]
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the function that takes (rowIndex, columnIndex) and returns the new char value
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Character, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements that match the predicate with the specified value.
     *
     * <p>This operation modifies the matrix in-place. Only elements for which the predicate
     * returns true are replaced; other elements remain unchanged.
     * For large matrices, this operation may be performed in parallel.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'e', 'c'}, {'b', 'f', 'd'}});
     * matrix.replaceIf(c -> c < 'd', 'x'); // Replace all chars less than 'd' with 'x'
     * // Matrix is now [['x', 'e', 'x'], ['x', 'f', 'd']]
     * }</pre>
     *
     * @param <E> the exception type that the predicate may throw
     * @param predicate the predicate to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.CharPredicate<E> predicate, final char newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements at positions that match the position-based predicate with the specified value.
     *
     * <p>The predicate receives the row and column indices for each position and determines
     * whether the element at that position should be replaced. This is useful for replacing
     * elements based on their location (e.g., diagonal elements, specific rows/columns).
     * For large matrices, this operation may be performed in parallel.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'},
     *                                                 {'d', 'e', 'f'},
     *                                                 {'g', 'h', 'i'}});
     * matrix.replaceIf((i, j) -> i == j, 'X'); // Replace main diagonal elements with 'X'
     * // Matrix is now [['X', 'b', 'c'], ['d', 'X', 'f'], ['g', 'h', 'X']]
     * }</pre>
     *
     * @param <E> the exception type that the predicate may throw
     * @param predicate the predicate that takes (rowIndex, columnIndex) and returns true for positions to replace
     * @param newValue the value to replace at matching positions
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final char newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new CharMatrix by applying the specified function to each element.
     *
     * <p>This is a non-mutating operation that returns a new matrix. The original matrix
     * is not modified. For large matrices, this operation may be performed in parallel.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix upper = matrix.map(c -> Character.toUpperCase(c));
     * // upper is [['A', 'B'], ['C', 'D']], original matrix is unchanged
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the mapping function to apply to each element
     * @return a new CharMatrix with the transformed values
     * @throws E if the function throws an exception
     */
    public <E extends Exception> CharMatrix map(final Throwables.CharUnaryOperator<E> func) throws E {
        final char[][] result = new char[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsChar(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return CharMatrix.of(result);
    }

    /**
     * Creates a new object Matrix by applying the specified function to each char element.
     *
     * <p>This method transforms the primitive CharMatrix into an object-based Matrix,
     * applying the mapping function to convert each char to an object of type T.
     * For large matrices, this operation may be performed in parallel.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * Matrix<String> stringMatrix = matrix.mapToObj(c -> String.valueOf(c), String.class);
     * // stringMatrix is [["a", "b"], ["c", "d"]]
     *
     * Matrix<Integer> codePoints = matrix.mapToObj(c -> (int) c, Integer.class);
     * // codePoints is [[97, 98], [99, 100]]
     * }</pre>
     *
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the exception type that the function may throw
     * @param func the mapping function that converts each char to an object of type T
     * @param targetElementType the class object representing the target element type (required for array creation)
     * @return a new Matrix&lt;T&gt; with the mapped object values
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.CharFunction<? extends T, E> func, final Class<T> targetElementType) throws E {
        final T[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Fills all elements in the matrix with the specified value.
     *
     * <p>This operation modifies the matrix in-place, setting every element
     * to the same value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.fill('x'); // All elements become 'x'
     * // Matrix is now [['x', 'x'], ['x', 'x']]
     * }</pre>
     *
     * @param val the value to fill the matrix with
     */
    public void fill(final char val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills the matrix with values from the specified 2D array, starting from position (0,0).
     * If the source array is larger than the matrix, only the fitting portion is copied.
     * If the source array is smaller, only the available values are copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{0, 0}, {0, 0}});
     * matrix.fill(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * // matrix is now [['a', 'b'], ['c', 'd']]
     * }</pre>
     *
     * @param b the source array to copy values from
     */
    public void fill(final char[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from the specified 2D array.
     * Values are copied starting from the specified position. If the source array
     * extends beyond the matrix bounds, only the fitting portion is copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
     * matrix.fill(1, 1, new char[][] {{'a', 'b'}, {'c', 'd'}});
     * // matrix is now [[0, 0, 0], [0, 'a', 'b'], [0, 'c', 'd']]
     * }</pre>
     *
     * @param fromRowIndex the starting row index in this matrix (must be &gt;= 0 and &lt;= rows)
     * @param fromColumnIndex the starting column index in this matrix (must be &gt;= 0 and &lt;= cols)
     * @param b the source array to copy values from
     * @throws IllegalArgumentException if the starting indices are negative or exceed matrix dimensions
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final char[][] b) throws IllegalArgumentException {
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
     * CharMatrix original = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix copy = original.copy();
     * // copy is independent from original
     * }</pre>
     *
     * @return a new matrix that is a copy of this matrix
     */
    @Override
    public CharMatrix copy() {
        final char[][] c = new char[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new CharMatrix(c);
    }

    /**
     * Creates a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows and is completely independent from the original matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
     * CharMatrix copy = matrix.copy(1, 3); // Returns [['c', 'd'], ['e', 'f']]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new CharMatrix containing the specified rows
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows, or fromRowIndex &gt; toRowIndex
     */
    @Override
    public CharMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final char[][] c = new char[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new CharMatrix(c);
    }

    /**
     * Creates a copy of a rectangular region from this matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}});
     * CharMatrix sub = matrix.copy(0, 2, 1, 3); // Copy rows 0-1, columns 1-2
     * // Result: [['b', 'c'], ['e', 'f']]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new CharMatrix containing the specified region
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows, fromRowIndex &gt; toRowIndex,
     *         fromColumnIndex &lt; 0, toColumnIndex &gt; cols, or fromColumnIndex &gt; toColumnIndex
     */
    @Override
    public CharMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final char[][] c = new char[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new CharMatrix(c);
    }

    /**
     * Creates a new matrix by extending or truncating rows and columns.
     * New cells are filled with the default char value (0/'\u0000').
     * If the new dimensions are smaller, the matrix is truncated.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix extended = matrix.extend(3, 3); // Extends to 3x3, new cells are '\u0000'
     * CharMatrix truncated = matrix.extend(1, 1); // Truncates to 1x1, result: [['a']]
     * }</pre>
     *
     * @param newRows the desired number of rows (must be >= 0)
     * @param newCols the desired number of columns (must be >= 0)
     * @return a new CharMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public CharMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, CHAR_0);
    }

    /**
     * Creates a new matrix by extending or truncating rows and columns.
     * New cells are filled with the specified default value.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix extended = matrix.extend(5, 5, 'x'); // Extend to 5x5, fill new cells with 'x'
     * }</pre>
     *
     * @param newRows the desired number of rows
     * @param newCols the desired number of columns
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new CharMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public CharMatrix extend(final int newRows, final int newCols, final char defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

        // Check for overflow before allocation
        if ((long) newRows * newCols > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Matrix dimensions too large: " + newRows + " x " + newCols);
        }

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            final boolean fillDefaultValue = defaultValueForNewCell != CHAR_0;
            final char[][] b = new char[newRows][];

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : new char[newCols];

                if (fillDefaultValue) {
                    if (i >= rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, defaultValueForNewCell);
                    }
                }
            }

            return new CharMatrix(b);
        }
    }

    /**
     * Creates a new matrix by extending the current matrix in all four directions.
     * New cells are filled with the default char value (0/'\u0000').
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix extended = matrix.extend(1, 1, 1, 1);
     * // Result is 4x4 with original matrix in center and '\u0000' padding
     * }</pre>
     *
     * @param toUp number of rows to add above (must be >= 0)
     * @param toDown number of rows to add below (must be >= 0)
     * @param toLeft number of columns to add to the left (must be >= 0)
     * @param toRight number of columns to add to the right (must be >= 0)
     * @return a new extended CharMatrix
     * @throws IllegalArgumentException if any parameter is negative
     */
    public CharMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, CHAR_0);
    }

    /**
     * Creates a new matrix by extending the current matrix in all four directions.
     * New cells are filled with the specified default value.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix extended = matrix.extend(1, 1, 1, 1, 'x'); // Add 1 row/col on each side with 'x'
     * }</pre>
     *
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new extended CharMatrix
     * @throws IllegalArgumentException if any extension parameter is negative
     */
    public CharMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final char defaultValueForNewCell)
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
            final boolean fillDefaultValue = defaultValueForNewCell != CHAR_0;
            final char[][] b = new char[newRows][newCols];

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

            return new CharMatrix(b);
        }
    }

    /**
     * Reverses the order of elements in each row horizontally (in-place).
     * This modifies the matrix directly. Each row is reversed independently.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * matrix.reverseH();
     * // matrix is now [['c', 'b', 'a'], ['f', 'e', 'd']]
     * }</pre>
     *
     * @see #flipH() for a non-mutating version
     */
    public void reverseH() {
        for (int i = 0; i < rows; i++) {
            N.reverse(a[i]);
        }
    }

    /**
     * Reverses the order of rows vertically (in-place).
     * This modifies the matrix directly. The first row becomes the last, second becomes second-to-last, etc.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
     * matrix.reverseV();
     * // matrix is now [['e', 'f'], ['c', 'd'], ['a', 'b']]
     * }</pre>
     *
     * @see #flipV() for a non-mutating version
     */
    public void reverseV() {
        for (int j = 0; j < cols; j++) {
            char tmp = 0;
            for (int l = 0, h = rows - 1; l < h;) {
                tmp = a[l][j];
                a[l++][j] = a[h][j];
                a[h--][j] = tmp;
            }
        }
    }

    /**
     * Creates a new matrix that is horizontally flipped (each row reversed).
     * The original matrix is not modified. This is equivalent to reversing each row.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}});
     * CharMatrix flipped = matrix.flipH(); // Returns [['c', 'b', 'a']]
     * // original matrix is unchanged
     * }</pre>
     *
     * @return a new CharMatrix with each row reversed
     * @see #reverseH() for an in-place version
     */
    public CharMatrix flipH() {
        final CharMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * Creates a new matrix that is vertically flipped (rows reversed).
     * The original matrix is not modified. The first row becomes the last row, etc.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
     * CharMatrix flipped = matrix.flipV(); // Returns [['e', 'f'], ['c', 'd'], ['a', 'b']]
     * // original matrix is unchanged
     * }</pre>
     *
     * @return a new CharMatrix with rows in reversed order
     * @see #reverseV() for an in-place version
     */
    public CharMatrix flipV() {
        final CharMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * Returns a new matrix rotated 90 degrees clockwise.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix rotated = matrix.rotate90();
     * // rotated is {{'c', 'a'}, {'d', 'b'}}
     * }</pre>
     *
     * @return a new matrix rotated 90 degrees clockwise
     */
    @Override
    public CharMatrix rotate90() {
        final char[][] c = new char[cols][rows];

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

        return new CharMatrix(c);
    }

    /**
     * Returns a new matrix rotated 180 degrees.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix rotated = matrix.rotate180();
     * // rotated is {{'d', 'c'}, {'b', 'a'}}
     * }</pre>
     *
     * @return a new matrix rotated 180 degrees
     */
    @Override
    public CharMatrix rotate180() {
        final char[][] c = new char[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new CharMatrix(c);
    }

    /**
     * Returns a new matrix rotated 270 degrees clockwise (or 90 degrees counter-clockwise).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix rotated = matrix.rotate270();
     * // rotated is {{'b', 'd'}, {'a', 'c'}}
     * }</pre>
     *
     * @return a new matrix rotated 270 degrees clockwise
     */
    @Override
    public CharMatrix rotate270() {
        final char[][] c = new char[cols][rows];

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

        return new CharMatrix(c);
    }

    /**
     * Returns a new matrix that is the transpose of this matrix.
     * The transpose swaps rows and columns.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * CharMatrix transposed = matrix.transpose();
     * // transposed is {{'a', 'd'}, {'b', 'e'}, {'c', 'f'}}
     * }</pre>
     *
     * @return a new matrix that is the transpose of this matrix
     */
    @Override
    public CharMatrix transpose() {
        final char[][] c = new char[cols][rows];

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

        return new CharMatrix(c);
    }

    /**
     * Reshapes the matrix to the specified dimensions.
     *
     * <p>Elements are read from the source matrix in row-major order (left to right, top to bottom)
     * and written to the new matrix in row-major order. If the new shape requires more elements
     * than available in the source matrix, the remaining positions are filled with default char
     * values ('\u0000'). If the new shape requires fewer elements, the excess elements are discarded.
     *
     * <p><b>Note:</b> Unlike some matrix libraries, this operation does not require the new
     * dimensions to match the total number of elements in the original matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * // Original is 2x3 (6 elements)
     *
     * CharMatrix reshaped1 = matrix.reshape(3, 2);
     * // Result: [['a', 'b'], ['c', 'd'], ['e', 'f']] - same 6 elements in 3x2
     *
     * CharMatrix reshaped2 = matrix.reshape(2, 4);
     * // Result: [['a', 'b', 'c', 'd'], ['e', 'f', '\u0000', '\u0000']] - 8 positions, last 2 filled with default
     *
     * CharMatrix reshaped3 = matrix.reshape(1, 4);
     * // Result: [['a', 'b', 'c', 'd']] - only first 4 elements used
     * }</pre>
     *
     * @param newRows the number of rows in the reshaped matrix (must be &gt;= 0)
     * @param newCols the number of columns in the reshaped matrix (must be &gt;= 0)
     * @return a new CharMatrix with the specified dimensions
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public CharMatrix reshape(final int newRows, final int newCols) {
        final char[][] c = new char[newRows][newCols];

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new CharMatrix(c);
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

        return new CharMatrix(c);
    }

    /**
     * Repeats each element in both row and column directions.
     * Each element is repeated rowRepeats times vertically and colRepeats times horizontally.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Before: [['a', 'b']]
     * CharMatrix repeated = matrix.repelem(2, 3);
     * // Result: [['a', 'a', 'a', 'b', 'b', 'b'],
     * //          ['a', 'a', 'a', 'b', 'b', 'b']]
     * }</pre>
     *
     * @param rowRepeats number of times to repeat each row
     * @param colRepeats number of times to repeat each column
     * @return a new CharMatrix with repeated elements
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see IntMatrix#repelem(int, int)
     */
    @Override
    public CharMatrix repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many rows: " + rows + " * " + rowRepeats);
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many columns: " + cols + " * " + colRepeats);
        }

        final char[][] c = new char[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            final char[] aa = a[i];
            final char[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                N.copy(Array.repeat(aa[j], colRepeats), 0, fr, j * colRepeats, colRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new CharMatrix(c);
    }

    /**
     * Repeats the entire matrix in both row and column directions.
     * The matrix is tiled rowRepeats times vertically and colRepeats times horizontally.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Before: [['a', 'b'], ['c', 'd']]
     * CharMatrix repeated = matrix.repmat(2, 3);
     * // Result: [['a', 'b', 'a', 'b', 'a', 'b'],
     * //          ['c', 'd', 'c', 'd', 'c', 'd'],
     * //          ['a', 'b', 'a', 'b', 'a', 'b'],
     * //          ['c', 'd', 'c', 'd', 'c', 'd']]
     * }</pre>
     *
     * @param rowRepeats number of times to repeat the matrix vertically
     * @param colRepeats number of times to repeat the matrix horizontally
     * @return a new CharMatrix with the repeated pattern
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see IntMatrix#repmat(int, int)
     */
    @Override
    public CharMatrix repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many rows: " + rows + " * " + rowRepeats);
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many columns: " + cols + " * " + colRepeats);
        }

        final char[][] c = new char[rows * rowRepeats][cols * colRepeats];

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

        return new CharMatrix(c);
    }

    /**
     * Returns a CharList containing all matrix elements in row-major order.
     *
     * <p>This method converts the 2D matrix into a 1D list by reading elements
     * row by row from left to right, top to bottom.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
     * CharList list = matrix.flatten();
     * // Returns CharList: ['a', 'b', 'c', 'd', 'e', 'f']
     * }</pre>
     *
     * @return a new CharList containing all elements in row-major order
     * @throws IllegalStateException if the matrix is too large to flatten (rows * cols &gt; Integer.MAX_VALUE)
     */
    @Override
    public CharList flatten() {
        // Check for overflow before allocation
        if ((long) rows * cols > Integer.MAX_VALUE) {
            throw new IllegalStateException("Matrix too large to flatten: " + rows + " x " + cols);
        }

        final char[] c = new char[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return CharList.of(c);
    }

    /**
     * Applies an operation to each row array of the matrix.
     * The operation receives the internal row arrays directly and can modify them.
     * This is useful for bulk operations that need to work with complete rows.
     *
     * <p><b>Note:</b> The operation receives references to internal arrays.
     * Any modifications will directly affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.flatOp(row -> java.util.Arrays.sort(row));
     * // Each row is now sorted
     * }</pre>
     *
     * @param <E> the exception type that the operation may throw
     * @param op the operation to perform on each row array
     * @throws E if the operation throws an exception
     * @see Arrays#flatOp(char[][], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super char[], E> op) throws E {
        Arrays.flatOp(a, op);
    }

    /**
     * Vertically stacks this matrix on top of another matrix.
     * Both matrices must have the same number of columns.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][] {{'a', 'b'}});
     * CharMatrix b = CharMatrix.of(new char[][] {{'c', 'd'}});
     * CharMatrix stacked = a.vstack(b); // Result: [['a', 'b'], ['c', 'd']]
     * }</pre>
     *
     * @param b the matrix to stack below this matrix
     * @return a new CharMatrix with b appended below this matrix
     * @throws IllegalArgumentException if the matrices have different column counts
     * @see IntMatrix#vstack(IntMatrix)
     */
    public CharMatrix vstack(final CharMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final char[][] c = new char[rows + b.rows][];
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
        }

        return CharMatrix.of(c);
    }

    /**
     * Horizontally stacks this matrix to the left of another matrix.
     * Both matrices must have the same number of rows.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][] {{'a'}, {'b'}});
     * CharMatrix b = CharMatrix.of(new char[][] {{'c'}, {'d'}});
     * CharMatrix stacked = a.hstack(b); // Result: [['a', 'c'], ['b', 'd']]
     * }</pre>
     *
     * @param b the matrix to stack to the right of this matrix
     * @return a new CharMatrix with b appended to the right of this matrix
     * @throws IllegalArgumentException if the matrices have different row counts
     * @see IntMatrix#hstack(IntMatrix)
     */
    public CharMatrix hstack(final CharMatrix b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final char[][] c = new char[rows][cols + b.cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c[i], 0, cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
        }

        return CharMatrix.of(c);
    }

    /**
     * Performs element-wise addition with another matrix.
     * Both matrices must have the same dimensions.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][] {{'a', 'b'}});
     * CharMatrix b = CharMatrix.of(new char[][] {{1, 2}});
     * CharMatrix sum = a.add(b); // Result: [['b', 'd']] (a+1, b+2)
     * }</pre>
     *
     * @param b the matrix to add to this matrix
     * @return a new CharMatrix containing the element-wise sum
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public CharMatrix add(final CharMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't add Matrixes with different shape");

        final char[][] ba = b.a;
        final char[][] result = new char[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (char) (a[i][j] + ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return CharMatrix.of(result);
    }

    /**
     * Performs element-wise subtraction of another matrix from this matrix.
     * Both matrices must have the same dimensions. The operation performs
     * this[i][j] - b[i][j] for each element.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][] {{'d', 'e'}});
     * CharMatrix b = CharMatrix.of(new char[][] {{1, 2}});
     * CharMatrix diff = a.subtract(b); // Result: [['c', 'c']] (d-1, e-2)
     * }</pre>
     *
     * @param b the matrix to subtract from this matrix
     * @return a new CharMatrix containing the element-wise difference
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public CharMatrix subtract(final CharMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't subtract Matrixes with different shape");

        final char[][] ba = b.a;
        final char[][] result = new char[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (char) (a[i][j] - ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return CharMatrix.of(result);
    }

    /**
     * Performs matrix multiplication with another matrix.
     * The number of columns in this matrix must equal the number of rows in the other matrix.
     * The resulting matrix will have dimensions [this.rows x b.cols].
     * 
     * <p>Note: Since char values are used, the multiplication may result in overflow
     * or unexpected character values. Consider using IntMatrix or DoubleMatrix for
     * numerical computations.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][] {{2, 3}, {4, 5}});
     * CharMatrix b = CharMatrix.of(new char[][] {{1, 2}, {3, 4}});
     * CharMatrix product = a.multiply(b); // Standard matrix multiplication
     * }</pre>
     *
     * @param b the matrix to multiply with this matrix
     * @return a new CharMatrix containing the matrix product
     * @throws IllegalArgumentException if this.cols != b.rows
     */
    public CharMatrix multiply(final CharMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.rows, "Illegal matrix dimensions");

        final char[][] ba = b.a;
        final char[][] result = new char[rows][b.cols];
        final Throwables.IntTriConsumer<RuntimeException> cmd = (i, j, k) -> result[i][j] += (char) (a[i][k] * ba[k][j]);

        Matrixes.multiply(this, b, cmd);

        return new CharMatrix(result);
    }

    /**
     * Converts this CharMatrix to a Matrix of Character objects.
     * Each primitive char value is boxed into a Character object.
     * This is useful when you need to work with object-based operations
     * or APIs that require Character objects instead of primitives.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * Matrix<Character> boxed = charMatrix.boxed();
     * }</pre>
     *
     * @return a new Matrix containing Character objects with the same values and dimensions
     */
    public Matrix<Character> boxed() {
        final Character[][] c = new Character[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final char[] aa = a[i];
                final Character[] cc = c[i];

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
     * Converts this CharMatrix to an IntMatrix.
     * Each char value is converted to its corresponding int value (Unicode code point).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][] {{'a', 'b'}});
     * IntMatrix intMatrix = charMatrix.toIntMatrix(); // Result: [[97, 98]]
     * }</pre>
     *
     * @return a new IntMatrix with the same dimensions containing the int values of the characters
     */
    public IntMatrix toIntMatrix() {
        return IntMatrix.create(a);
    }

    /**
     * Converts this CharMatrix to a LongMatrix.
     * Each char value is converted to its corresponding long value (Unicode code point).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][] {{'a', 'b'}});
     * LongMatrix longMatrix = charMatrix.toLongMatrix(); // Result: [[97L, 98L]]
     * }</pre>
     *
     * @return a new LongMatrix with the same dimensions containing the long values of the characters
     */
    public LongMatrix toLongMatrix() {
        final long[][] c = new long[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final char[] aa = a[i];
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
     * Converts this CharMatrix to a FloatMatrix.
     * Each char value is converted to its corresponding float value (Unicode code point).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][] {{'a', 'b'}});
     * FloatMatrix floatMatrix = charMatrix.toFloatMatrix(); // Result: [[97.0f, 98.0f]]
     * }</pre>
     *
     * @return a new FloatMatrix with the same dimensions containing the float values of the characters
     */
    public FloatMatrix toFloatMatrix() {
        final float[][] c = new float[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final char[] aa = a[i];
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
     * Converts this CharMatrix to a DoubleMatrix.
     * Each char value is converted to its corresponding double value (Unicode code point).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][] {{'a', 'b'}});
     * DoubleMatrix doubleMatrix = charMatrix.toDoubleMatrix(); // Result: [[97.0, 98.0]]
     * }</pre>
     *
     * @return a new DoubleMatrix with the same dimensions containing the double values of the characters
     */
    public DoubleMatrix toDoubleMatrix() {
        final double[][] c = new double[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final char[] aa = a[i];
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
     * Both matrices must have the same dimensions. The zip function is applied
     * to corresponding elements from both matrices.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][] {{'a', 'b'}});
     * CharMatrix b = CharMatrix.of(new char[][] {{'A', 'B'}});
     * CharMatrix result = a.zipWith(b, (x, y) -> (char) Math.max(x, y));
     * // Result: [['a', 'b']] (max of each pair)
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix to zip with this matrix
     * @param zipFunction the binary operation to apply to corresponding elements
     * @return a new CharMatrix containing the results of the zip operation
     * @throws IllegalArgumentException if the matrices have different dimensions
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> CharMatrix zipWith(final CharMatrix matrixB, final Throwables.CharBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Can't zip two or more matrices which don't have same shape");

        final char[][] b = matrixB.a;
        final char[][] result = new char[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsChar(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return CharMatrix.of(result);
    }

    /**
     * Applies a ternary operation element-wise to this matrix and two other matrices.
     * All three matrices must have the same dimensions. The zip function is applied
     * to corresponding elements from all three matrices.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][] {{'a', 'b'}});
     * CharMatrix b = CharMatrix.of(new char[][] {{'c', 'd'}});
     * CharMatrix c = CharMatrix.of(new char[][] {{'e', 'f'}});
     * CharMatrix result = a.zipWith(b, c, (x, y, z) -> (char) Math.max(Math.max(x, y), z));
     * // Result: [['e', 'f']] (max of each triple)
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix to zip with
     * @param matrixC the third matrix to zip with
     * @param zipFunction the ternary operation to apply to corresponding elements
     * @return a new CharMatrix containing the results of the zip operation
     * @throws IllegalArgumentException if any of the matrices have different dimensions
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> CharMatrix zipWith(final CharMatrix matrixB, final CharMatrix matrixC, final Throwables.CharTernaryOperator<E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Can't zip two or more matrices which don't have same shape");

        final char[][] b = matrixB.a;
        final char[][] c = matrixC.a;
        final char[][] result = new char[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsChar(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return CharMatrix.of(result);
    }

    /**
     * Returns a stream of elements on the diagonal from left-up to right-down.
     * The matrix must be square (same number of rows and columns).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, 
     *                                                 {'d', 'e', 'f'}, 
     *                                                 {'g', 'h', 'i'}});
     * CharStream diagonal = matrix.streamLU2RD(); // Stream of: 'a', 'e', 'i'
     * }</pre>
     *
     * @return a CharStream containing the diagonal elements from top-left to bottom-right
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public CharStream streamLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return CharStream.empty();
        }

        return CharStream.of(new CharIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public char nextChar() {
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
     * Returns a stream of elements on the diagonal from right-up to left-down.
     * The matrix must be square (same number of rows and columns).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, 
     *                                                 {'d', 'e', 'f'}, 
     *                                                 {'g', 'h', 'i'}});
     * CharStream diagonal = matrix.streamRU2LD(); // Stream of: 'c', 'e', 'g'
     * }</pre>
     *
     * @return a CharStream containing the diagonal elements from top-right to bottom-left
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public CharStream streamRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return CharStream.empty();
        }

        return CharStream.of(new CharIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public char nextChar() {
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
     * Returns a stream of all elements in the matrix, traversed horizontally (row by row).
     * Elements are returned in row-major order: all elements from the first row,
     * then all elements from the second row, and so on.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharStream stream = matrix.streamH(); // Stream of: 'a', 'b', 'c', 'd'
     * }</pre>
     *
     * @return a CharStream containing all matrix elements in row-major order
     */
    @Override
    public CharStream streamH() {
        return streamH(0, rows);
    }

    /**
     * Returns a stream of elements from a specific row.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharStream row = matrix.streamH(1); // Stream of: 'c', 'd'
     * }</pre>
     *
     * @param rowIndex the index of the row to stream (0-based)
     * @return a CharStream containing all elements from the specified row
     * @throws IndexOutOfBoundsException if rowIndex is negative or >= number of rows
     */
    @Override
    public CharStream streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * Returns a stream of elements from a range of rows, traversed horizontally.
     * Elements are returned in row-major order within the specified range.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
     * CharStream stream = matrix.streamH(1, 3); // Stream of: 'c', 'd', 'e', 'f'
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a CharStream containing elements from the specified row range
     * @throws IndexOutOfBoundsException if the indices are out of bounds or fromRowIndex > toRowIndex
     */
    @Override
    public CharStream streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return CharStream.empty();
        }

        return CharStream.of(new CharIteratorEx() {
            private int i = fromRowIndex;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i < toRowIndex;
            }

            @Override
            public char nextChar() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final char result = a[i][j++];

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
            public char[] toArray() {
                final int len = (int) count();
                final char[] c = new char[len];

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
     * Returns a stream of all elements in the matrix, traversed vertically (column by column).
     * Elements are returned in column-major order: all elements from the first column,
     * then all elements from the second column, and so on.
     * 
     * <p>Note: This method is marked as @Beta and may be subject to change.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharStream stream = matrix.streamV(); // Stream of: 'a', 'c', 'b', 'd'
     * }</pre>
     *
     * @return a CharStream containing all matrix elements in column-major order
     */
    @Override
    @Beta
    public CharStream streamV() {
        return streamV(0, cols);
    }

    /**
     * Returns a stream of elements from a specific column.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharStream column = matrix.streamV(1); // Stream of: 'b', 'd'
     * }</pre>
     *
     * @param columnIndex the index of the column to stream (0-based)
     * @return a CharStream containing all elements from the specified column
     * @throws IndexOutOfBoundsException if columnIndex is negative or >= number of columns
     */
    @Override
    public CharStream streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * Returns a stream of elements from a range of columns, traversed vertically.
     * Elements are returned in column-major order within the specified range.
     * 
     * <p>Note: This method is marked as @Beta and may be subject to change.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * CharStream stream = matrix.streamV(1, 3); // Stream of: 'b', 'e', 'c', 'f'
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a CharStream containing elements from the specified column range
     * @throws IndexOutOfBoundsException if the indices are out of bounds or fromColumnIndex > toColumnIndex
     */
    @Override
    @Beta
    public CharStream streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return CharStream.empty();
        }

        return CharStream.of(new CharIteratorEx() {
            private int i = 0;
            private int j = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return j < toColumnIndex;
            }

            @Override
            public char nextChar() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final char result = a[i++][j];

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

                if (n >= (long) (toColumnIndex - j) * CharMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    final int offset = (int) (n + i);
                    i = offset % CharMatrix.this.rows;
                    j += offset / CharMatrix.this.rows;
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public char[] toArray() {
                final int len = (int) count();
                final char[] c = new char[len];

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
     * Returns a stream of CharStreams, where each CharStream represents a row in the matrix.
     * This allows for row-wise operations on the matrix.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * Stream<CharStream> rows = matrix.streamR();
     * // First stream contains: 'a', 'b'
     * // Second stream contains: 'c', 'd'
     * }</pre>
     *
     * @return a Stream of CharStreams, one for each row in the matrix
     */
    @Override
    public Stream<CharStream> streamR() {
        return streamR(0, rows);
    }

    /**
     * Returns a stream of CharStreams for a range of rows.
     * Each CharStream in the result represents a complete row from the matrix.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
     * Stream<CharStream> rows = matrix.streamR(1, 3);
     * // First stream contains: 'c', 'd'
     * // Second stream contains: 'e', 'f'
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a Stream of CharStreams for the specified row range
     * @throws IndexOutOfBoundsException if the indices are out of bounds or fromRowIndex > toRowIndex
     */
    @Override
    public Stream<CharStream> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
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
            public CharStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return CharStream.of(a[cursor++]);
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
     * Returns a stream of CharStreams, where each CharStream represents a column in the matrix.
     * This allows for column-wise operations on the matrix.
     * 
     * <p>Note: This method is marked as @Beta and may be subject to change.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * Stream<CharStream> columns = matrix.streamC();
     * // First stream contains: 'a', 'c'
     * // Second stream contains: 'b', 'd'
     * }</pre>
     *
     * @return a Stream of CharStreams, one for each column in the matrix
     */
    @Override
    @Beta
    public Stream<CharStream> streamC() {
        return streamC(0, cols);
    }

    /**
     * Returns a stream of CharStreams for a range of columns.
     * Each CharStream in the result represents a complete column from the matrix.
     * 
     * <p>Note: This method is marked as @Beta and may be subject to change.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * Stream<CharStream> columns = matrix.streamC(1, 3);
     * // First stream contains: 'b', 'e'
     * // Second stream contains: 'c', 'f'
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a Stream of CharStreams for the specified column range
     * @throws IndexOutOfBoundsException if the indices are out of bounds or fromColumnIndex > toColumnIndex
     */
    @Override
    @Beta
    public Stream<CharStream> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public CharStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return CharStream.of(new CharIteratorEx() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rows;
                    private int cursor2 = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor2 < toIndex2;
                    }

                    @Override
                    public char nextChar() {
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
     * Returns the length of the specified char array.
     *
     * <p>This is an internal helper method used by the abstract parent class
     * for various matrix operations to determine row lengths.
     *
     * @param a the char array to measure
     * @return the length of the array, or 0 if the array is null
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final char[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * Applies the specified action to each element in the matrix.
     *
     * <p>The action is performed on all elements in row-major order (left to right, top to bottom).
     * For large matrices, the operation may be parallelized automatically to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.forEach(ch -> System.out.print(ch + " ")); // Prints: a b c d
     *
     * List<Character> chars = new ArrayList<>();
     * matrix.forEach(chars::add); // Collects all characters
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the action to be performed on each element
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.CharConsumer<E> action) throws E {
        forEach(0, rows, 0, cols, action);
    }

    /**
     * Applies the specified action to each element in a sub-region of the matrix.
     *
     * <p>The action is performed on elements within the specified row and column ranges
     * in row-major order. This allows you to operate on a rectangular portion of the matrix
     * without affecting other elements. For large sub-regions, the operation may be parallelized
     * automatically to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'},
     *                                                 {'d', 'e', 'f'},
     *                                                 {'g', 'h', 'i'}});
     *
     * // Process only the center 2x2 sub-region
     * matrix.forEach(1, 3, 1, 3, ch -> System.out.print(ch + " "));
     * // Prints: e f h i
     *
     * // Process first two rows, last two columns
     * matrix.forEach(0, 2, 1, 3, ch -> System.out.print(ch + " "));
     * // Prints: b c e f
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to be performed on each element in the sub-region
     * @throws IndexOutOfBoundsException if any index is out of bounds or fromIndex &gt; toIndex
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.CharConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final char[] aa = a[i];

                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(aa[j]);
                }
            }
        }
    }

    /**
     * Prints the matrix to standard output in a formatted, human-readable manner.
     *
     * <p>Each row is printed on a separate line with elements displayed as character literals
     * (enclosed in single quotes), separated by commas, and the row enclosed in square brackets.
     * This provides a clear visual representation of the matrix structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * matrix.println();
     * // Output:
     * // ['a', 'b', 'c']
     * // ['d', 'e', 'f']
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

                    final char[] row = a[i];
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
     * Returns {@code true} if the given object is also a CharMatrix with the same dimensions
     * and all corresponding elements are equal.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix m1 = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix m2 = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
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

        if (obj instanceof final CharMatrix another) {
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
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * System.out.println(matrix.toString()); // [[a, b], [c, d]]
     * }</pre>
     *
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
