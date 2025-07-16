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
 * <p>Example usage:
 * <pre>{@code
 * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
 * char value = matrix.get(0, 1); // returns 'b'
 * matrix.transpose(); // returns new matrix with rows and columns swapped
 * }</pre>
 * 
 * @see AbstractMatrix
 * @see IntMatrix
 * @see DoubleMatrix
 * @author HaiYang Li
 */
public final class CharMatrix extends AbstractMatrix<char[], CharList, CharStream, Stream<CharStream>, CharMatrix> {

    static final CharMatrix EMPTY_CHAR_MATRIX = new CharMatrix(new char[0][0]);

    /**
     * Constructs a CharMatrix from a 2D char array.
     * If the input array is null, an empty matrix is created.
     *
     * @param a the 2D char array to wrap. The array is used directly without copying.
     */
    public CharMatrix(final char[][] a) {
        super(a == null ? new char[0][0] : a);
    }

    /**
     * Returns an empty CharMatrix with zero rows and columns.
     *
     * @return an empty CharMatrix instance
     */
    public static CharMatrix empty() {
        return EMPTY_CHAR_MATRIX;
    }

    /**
     * Creates a CharMatrix from the specified 2D char array.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
     * }</pre>
     *
     * @param a the 2D char array. If null or empty, returns an empty matrix.
     * @return a new CharMatrix wrapping the provided array
     */
    public static CharMatrix of(final char[]... a) {
        return N.isEmpty(a) ? EMPTY_CHAR_MATRIX : new CharMatrix(a);
    }

    /**
     * Creates a single-row CharMatrix with random char values.
     * The random values are generated using CharList.random().
     *
     * @param len the number of columns in the resulting matrix
     * @return a CharMatrix with one row and len columns containing random char values 
     */
    @SuppressWarnings("deprecation")
    public static CharMatrix random(final int len) {
        return new CharMatrix(new char[][] { CharList.random(len).array() });
    }

    /**
     * Creates a single-row CharMatrix where all elements have the specified value.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.repeat('x', 5); // Creates [[x, x, x, x, x]]
     * }</pre>
     *
     * @param val the char value to repeat
     * @param len the number of columns in the resulting matrix
     * @return a CharMatrix with one row and len columns, all containing val
     */
    public static CharMatrix repeat(final char val, final int len) {
        return new CharMatrix(new char[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a single-row CharMatrix containing a range of char values.
     * The range is [startInclusive, endExclusive).
     * 
     * <p>Example:
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
     * <p>Example:
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
     * <p>Example:
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
     * Creates a square diagonal matrix with the specified values on the main diagonal
     * (from left-upper to right-down).
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.diagonalLU2RD(new char[]{'a', 'b', 'c'});
     * // Creates:
     * // [['a', 0, 0],
     * //  [0, 'b', 0],
     * //  [0, 0, 'c']]
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the values for the main diagonal
     * @return a square CharMatrix with the specified diagonal values
     */
    public static CharMatrix diagonalLU2RD(final char[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square diagonal matrix with the specified values on the anti-diagonal
     * (from right-upper to left-down).
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.diagonalRU2LD(new char[]{'a', 'b', 'c'});
     * // Creates:
     * // [[0, 0, 'a'],
     * //  [0, 'b', 0],
     * //  ['c', 0, 0]]
     * }</pre>
     *
     * @param rightUp2LeftDownDiagonal the values for the anti-diagonal
     * @return a square CharMatrix with the specified anti-diagonal values
     */
    public static CharMatrix diagonalRU2LD(final char[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a square diagonal matrix with values on both the main diagonal and anti-diagonal.
     * The matrix size is determined by the length of the diagonal arrays.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.diagonal(new char[]{'a', 'b', 'c'}, new char[]{'x', 'y', 'z'});
     * // Creates:
     * // [['a', 0, 'x'],
     * //  [0, 'b', 0],
     * //  ['z', 0, 'c']]
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the values for the main diagonal (can be null)
     * @param rightUp2LeftDownDiagonal the values for the anti-diagonal (can be null)
     * @return a square CharMatrix with the specified diagonal values
     * @throws IllegalArgumentException if both arrays are non-null and have different lengths
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
     * Null values in the input matrix are converted to the default char value (0).
     *
     * @param x the boxed Character matrix to convert
     * @return a new CharMatrix with unboxed values
     */
    public static CharMatrix unbox(final Matrix<Character> x) {
        return CharMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of the matrix elements.
     *
     * @return char.class
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return char.class;
    }

    /**
     * Gets the char value at the specified position.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
     * char value = matrix.get(0, 1); // returns 'b'
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the char value at position [i][j]
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public char get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Gets the char value at the specified point.
     *
     * @param point the point containing row and column indices
     * @return the char value at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public char get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the char value at the specified position.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
     * matrix.set(0, 1, 'x'); // matrix[0][1] is now 'x'
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the char value to set
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public void set(final int i, final int j, final char val) {
        a[i][j] = val;
    }

    /**
     * Sets the char value at the specified point.
     *
     * @param point the point containing row and column indices
     * @param val the char value to set
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public void set(final Point point, final char val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element above the specified position as an OptionalChar.
     * Returns an empty OptionalChar if the position is in the first row.
     *
     * @param i the row index
     * @param j the column index
     * @return OptionalChar containing the element above, or empty if at top edge
     */
    public OptionalChar upOf(final int i, final int j) {
        return i == 0 ? OptionalChar.empty() : OptionalChar.of(a[i - 1][j]);
    }

    /**
     * Returns the element below the specified position as an OptionalChar.
     * Returns an empty OptionalChar if the position is in the last row.
     *
     * @param i the row index
     * @param j the column index
     * @return OptionalChar containing the element below, or empty if at bottom edge
     */
    public OptionalChar downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalChar.empty() : OptionalChar.of(a[i + 1][j]);
    }

    /**
     * Returns the element to the left of the specified position as an OptionalChar.
     * Returns an empty OptionalChar if the position is in the first column.
     *
     * @param i the row index
     * @param j the column index
     * @return OptionalChar containing the element to the left, or empty if at left edge
     */
    public OptionalChar leftOf(final int i, final int j) {
        return j == 0 ? OptionalChar.empty() : OptionalChar.of(a[i][j - 1]);
    }

    /**
     * Returns the element to the right of the specified position as an OptionalChar.
     * Returns an empty OptionalChar if the position is in the last column.
     *
     * @param i the row index
     * @param j the column index
     * @return OptionalChar containing the element to the right, or empty if at right edge
     */
    public OptionalChar rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalChar.empty() : OptionalChar.of(a[i][j + 1]);
    }

    /**
     * Returns a stream of the four adjacent points (up, right, down, left) of the specified position.
     * Points that would be outside the matrix bounds are returned as null.
     *
     * @param i the row index
     * @param j the column index
     * @return a Stream containing the four adjacent points in order: up, right, down, left
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
     * @return a Stream containing the eight adjacent points
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
     * Returns a copy of the specified row.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
     * char[] row = matrix.row(0); // returns ['a', 'b']
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @return a copy of the row at the specified index
     * @throws IllegalArgumentException if rowIndex is negative or >= rows
     */
    public char[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
     * char[] column = matrix.column(1); // returns ['b', 'd']
     * }</pre>
     *
     * @param columnIndex the column index (0-based)
     * @return a new array containing the values from the specified column
     * @throws IllegalArgumentException if columnIndex is negative or >= cols
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
     * Replaces the specified row with the given array.
     * The array must have exactly the same length as the number of columns.
     *
     * @param rowIndex the row index to replace (0-based)
     * @param row the new row values
     * @throws IllegalArgumentException if row length doesn't match column count or rowIndex is invalid
     */
    public void setRow(final int rowIndex, final char[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Replaces the specified column with the given array.
     * The array must have exactly the same length as the number of rows.
     *
     * @param columnIndex the column index to replace (0-based)
     * @param column the new column values
     * @throws IllegalArgumentException if column length doesn't match row count or columnIndex is invalid
     */
    public void setColumn(final int columnIndex, final char[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Applies the specified function to update all elements in the specified row.
     * 
     * <p>Example:
     * <pre>{@code
     * matrix.updateRow(0, c -> Character.toUpperCase(c));
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param rowIndex the row index to update (0-based)
     * @param func the function to apply to each element in the row
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.CharUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsChar(a[rowIndex][i]);
        }
    }

    /**
     * Applies the specified function to update all elements in the specified column.
     * 
     * <p>Example:
     * <pre>{@code
     * matrix.updateColumn(1, c -> Character.toLowerCase(c));
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param columnIndex the column index to update (0-based)
     * @param func the function to apply to each element in the column
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.CharUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsChar(a[i][columnIndex]);
        }
    }

    /**
     * Returns the elements on the main diagonal (left-upper to right-down).
     * The matrix must be square.
     *
     * @return an array containing the diagonal elements
     * @throws IllegalStateException if the matrix is not square
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
     * Sets the elements on the main diagonal (left-upper to right-down).
     * The matrix must be square and the diagonal array must have at least as many elements as rows.
     *
     * @param diagonal the new diagonal values
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
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
     * The matrix must be square.
     *
     * @param <E> the exception type that the function may throw
     * @param func the function to apply to each diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateLU2RD(final Throwables.CharUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsChar(a[i][i]);
        }
    }

    /**
     * Returns the elements on the anti-diagonal (right-upper to left-down).
     * The matrix must be square.
     *
     * @return an array containing the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square
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
     * Sets the elements on the anti-diagonal (right-upper to left-down).
     * The matrix must be square and the diagonal array must have at least as many elements as rows.
     *
     * @param diagonal the new anti-diagonal values
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
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
     * The matrix must be square.
     *
     * @param <E> the exception type that the function may throw
     * @param func the function to apply to each anti-diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateRU2LD(final Throwables.CharUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsChar(a[i][cols - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix using the specified function.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:
     * <pre>{@code
     * matrix.updateAll(c -> Character.toUpperCase(c));
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
     * Updates all elements in the matrix based on their position.
     * The function receives the row and column indices and returns the new value.
     * 
     * <p>Example:
     * <pre>{@code
     * matrix.updateAll((i, j) -> (char)('a' + i * cols + j));
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the function that takes row index, column index and returns new value
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Character, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements that match the predicate with the specified value.
     * 
     * <p>Example:
     * <pre>{@code
     * matrix.replaceIf(c -> c < 'd', 'x'); // Replace all chars less than 'd' with 'x'
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
     * Replaces all elements at positions that match the predicate with the specified value.
     * The predicate receives the row and column indices.
     * 
     * <p>Example:
     * <pre>{@code
     * matrix.replaceIf((i, j) -> i == j, 'X'); // Replace diagonal elements with 'X'
     * }</pre>
     *
     * @param <E> the exception type that the predicate may throw
     * @param predicate the predicate that takes row and column indices
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix upper = matrix.map(c -> Character.toUpperCase(c));
     * }</pre>
     *
     * @param <E> the exception type that the function may throw
     * @param func the mapping function to apply to each element
     * @return a new CharMatrix with the mapped values
     * @throws E if the function throws an exception
     */
    public <E extends Exception> CharMatrix map(final Throwables.CharUnaryOperator<E> func) throws E {
        final char[][] result = new char[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsChar(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return CharMatrix.of(result);
    }

    /**
     * Creates a new object matrix by applying the specified function to each element.
     * 
     * <p>Example:
     * <pre>{@code
     * Matrix<String> stringMatrix = matrix.mapToObj(c -> String.valueOf(c), String.class);
     * }</pre>
     *
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the exception type that the function may throw
     * @param func the mapping function to apply to each element
     * @param targetElementType the class of the target element type
     * @return a new Matrix with the mapped values
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
     * <p>Example:
     * <pre>{@code
     * matrix.fill('x'); // All elements become 'x'
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
     * @param fromRowIndex the starting row index in this matrix
     * @param fromColumnIndex the starting column index in this matrix
     * @param b the source array to copy values from
     * @throws IndexOutOfBoundsException if fromRowIndex or fromColumnIndex is negative or out of bounds
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final char[][] b) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, rows, rows);
        N.checkFromToIndex(fromColumnIndex, cols, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
        }
    }

    /**
     * Creates a copy of this matrix.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix copy = matrix.copy();
     * copy.set(0, 0, 'x'); // Original matrix is unchanged
     * }</pre>
     *
     * @return a new CharMatrix with the same values
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
     * Creates a copy of a portion of rows from this matrix.
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new CharMatrix containing the specified rows
     * @throws IndexOutOfBoundsException if the indices are out of bounds
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix sub = matrix.copy(0, 2, 1, 3); // Copy rows 0-1, columns 1-2
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new CharMatrix containing the specified region
     * @throws IndexOutOfBoundsException if any index is out of bounds
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
     * New cells are filled with the default char value (0).
     *
     * @param newRows the desired number of rows
     * @param newCols the desired number of columns
     * @return a new CharMatrix with the specified dimensions
     */
    public CharMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, CHAR_0);
    }

    /**
     * Creates a new matrix by extending or truncating rows and columns.
     * New cells are filled with the specified default value.
     * 
     * <p>Example:
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
     * New cells are filled with the default char value (0).
     *
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @return a new extended CharMatrix
     */
    public CharMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, CHAR_0);
    }

    /**
     * Creates a new matrix by extending the current matrix in all four directions.
     * New cells are filled with the specified default value.
     * 
     * <p>Example:
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
     * Reverses the order of elements in each row (horizontal flip in-place).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.reverseH(); // [['a','b','c']] becomes [['c','b','a']]
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
     * matrix.reverseV(); // [['a'],['b'],['c']] becomes [['c'],['b'],['a']]
     * }</pre>
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
     * The original matrix is not modified.
     *
     * @return a new CharMatrix with rows reversed
     * @see #reverseH()
     */
    public CharMatrix flipH() {
        final CharMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * Creates a new matrix that is vertically flipped (each column reversed).
     * The original matrix is not modified.
     *
     * @return a new CharMatrix with columns reversed
     * @see #reverseV()
     */
    public CharMatrix flipV() {
        final CharMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * Creates a new matrix rotated 90 degrees clockwise.
     * 
     * <p>Example:
     * <pre>{@code
     * // Before: [['a', 'b'], ['c', 'd']]
     * CharMatrix rotated = matrix.rotate90();
     * // Result: [['c', 'a'], ['d', 'b']]
     * }</pre>
     *
     * @return a new CharMatrix rotated 90 degrees clockwise
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
     * Creates a new matrix rotated 180 degrees.
     * 
     * <p>Example:
     * <pre>{@code
     * // Before: [['a', 'b'], ['c', 'd']]
     * CharMatrix rotated = matrix.rotate180();
     * // Result: [['d', 'c'], ['b', 'a']]
     * }</pre>
     *
     * @return a new CharMatrix rotated 180 degrees
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
     * Creates a new matrix rotated 270 degrees clockwise (90 degrees counter-clockwise).
     * 
     * <p>Example:
     * <pre>{@code
     * // Before: [['a', 'b'], ['c', 'd']]
     * CharMatrix rotated = matrix.rotate270();
     * // Result: [['b', 'd'], ['a', 'c']]
     * }</pre>
     *
     * @return a new CharMatrix rotated 270 degrees clockwise
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
     * Creates a new matrix that is the transpose of this matrix (rows become columns).
     * 
     * <p>Example:
     * <pre>{@code
     * // Before: [['a', 'b', 'c'], ['d', 'e', 'f']]
     * CharMatrix transposed = matrix.transpose();
     * // Result: [['a', 'd'], ['b', 'e'], ['c', 'f']]
     * }</pre>
     *
     * @return a new CharMatrix with rows and columns swapped
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
     * Elements are read in row-major order and written to the new shape in row-major order.
     * The total number of elements may change.
     * 
     * <p>Example:
     * <pre>{@code
     * // Before: [['a', 'b', 'c'], ['d', 'e', 'f']]
     * CharMatrix reshaped = matrix.reshape(3, 2);
     * // Result: [['a', 'b'], ['c', 'd'], ['e', 'f']]
     * }</pre>
     *
     * @param newRows the number of rows in the reshaped matrix
     * @param newCols the number of columns in the reshaped matrix
     * @return a new CharMatrix with the specified shape
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
     * <p>Example:
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
     * <p>Example:
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
     * Flattens the matrix into a single CharList in row-major order.
     * 
     * <p>Example:
     * <pre>{@code
     * // Matrix: [['a', 'b'], ['c', 'd']]
     * CharList flat = matrix.flatten(); // Returns ['a', 'b', 'c', 'd']
     * }</pre>
     *
     * @return a CharList containing all elements in row-major order
     */
    @Override
    public CharList flatten() {
        final char[] c = new char[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return CharList.of(c);
    }

    /**
     * Flattens the underlying 2D array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure.
     *
     * @param <E> the exception type that the operation may throw
     * @param op the operation to perform on the internal array
     * @throws E if the operation throws an exception
     * @see Arrays#flatOp(char[], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super char[], E> op) throws E {
        Arrays.flatOp(a, op);
    }

    /**
     * Vertically stacks this matrix on top of another matrix.
     * Both matrices must have the same number of columns.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][]{{'a', 'b'}});
     * CharMatrix b = CharMatrix.of(new char[][]{{'c', 'd'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][]{{'a'}, {'b'}});
     * CharMatrix b = CharMatrix.of(new char[][]{{'c'}, {'d'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][]{{'a', 'b'}});
     * CharMatrix b = CharMatrix.of(new char[][]{{1, 2}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][]{{'d', 'e'}});
     * CharMatrix b = CharMatrix.of(new char[][]{{1, 2}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][]{{2, 3}, {4, 5}});
     * CharMatrix b = CharMatrix.of(new char[][]{{1, 2}, {3, 4}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][]{{'a', 'b'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][]{{'a', 'b'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][]{{'a', 'b'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][]{{'a', 'b'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][]{{'a', 'b'}});
     * CharMatrix b = CharMatrix.of(new char[][]{{'A', 'B'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][]{{'a', 'b'}});
     * CharMatrix b = CharMatrix.of(new char[][]{{'c', 'd'}});
     * CharMatrix c = CharMatrix.of(new char[][]{{'e', 'f'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b', 'c'}, 
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
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b', 'c'}, 
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
     * Returns a stream of all elements in the matrix, traversed horizontally (row by row).
     * Elements are returned in row-major order: all elements from the first row,
     * then all elements from the second row, and so on.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b', 'c'}, {'d', 'e', 'f'}});
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
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                if (n >= (long) (toColumnIndex - j) * CharMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    i += (int) ((n + i) % CharMatrix.this.rows);
                    j += (int) ((n + i) / CharMatrix.this.rows);
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
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
     * Returns a stream of CharStreams, where each CharStream represents a column in the matrix.
     * This allows for column-wise operations on the matrix.
     * 
     * <p>Note: This method is marked as @Beta and may be subject to change.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
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
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b', 'c'}, {'d', 'e', 'f'}});
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
     * Returns the length of the specified char array.
     * This is an internal helper method used by the abstract parent class.
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
     * The action is performed on all elements in row-major order.
     * If the matrix is large enough, the operation may be parallelized.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
     * matrix.forEach(ch -> System.out.print(ch + " ")); // Prints: a b c d
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
     * The action is performed on elements within the specified row and column ranges
     * in row-major order. If the sub-region is large enough, the operation may be parallelized.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b', 'c'}, 
     *                                                 {'d', 'e', 'f'}, 
     *                                                 {'g', 'h', 'i'}});
     * matrix.forEach(1, 3, 1, 3, ch -> System.out.print(ch + " ")); // Prints: e f h i
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to be performed on each element
     * @throws IndexOutOfBoundsException if any index is out of bounds
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
     * Prints the matrix to standard output in a formatted manner.
     * Each row is printed on a separate line with elements separated by commas
     * and enclosed in square brackets. The entire matrix is also enclosed in brackets.
     *
     * <p>Example:</p>
     * <pre>{@code
     * CharMatrix matrix = Charatrix.of(new char[][]{{'a', 'a', 'c'}, {'d', 'e', 'f'}});
     * matrix.println();
     * // Output:
     * // ['a', 'b', 'c']
     * // ['d', 'e', 'f']
     * }</pre>
     */
    @Override
    public void println() {
        Arrays.println(a);
    }

    /**
     * Returns a hash code value for this matrix.
     * The hash code is computed based on the dimensions and all elements of the matrix.
     * Two matrices with the same dimensions and elements will have the same hash code.
     *
     * @return a hash code value for this matrix
     */
    @Override
    public int hashCode() {
        return N.deepHashCode(a);
    }

    /**
     * Compares this matrix to the specified object for equality.
     * Returns true if and only if the specified object is also a CharMatrix
     * with the same dimensions and all corresponding elements are equal.
     *
     * @param obj the object to compare with
     * @return true if the specified object is equal to this matrix, false otherwise
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
     * The string representation consists of the matrix elements arranged in rows,
     * with each row enclosed in brackets and rows separated by commas.
     * 
     * <p>Example:
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][]{{'a', 'b'}, {'c', 'd'}});
     * System.out.println(matrix.toString()); // Output: [[a, b], [c, d]]
     * }</pre>
     *
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
