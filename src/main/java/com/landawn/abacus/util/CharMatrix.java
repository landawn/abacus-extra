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
 * <p>The matrix is backed by a two-dimensional char array and provides methods for:
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
 * char value = matrix.get(0, 1);   // returns 'b'
 * matrix.transpose();   // returns new matrix with rows and columns swapped
 * }</pre>
 * 
 * @see AbstractMatrix
 * @see BooleanMatrix
 * @see ByteMatrix
 * @see ShortMatrix
 * @see IntMatrix
 * @see LongMatrix
 * @see FloatMatrix
 * @see DoubleMatrix
 * @see Matrix
 */
public final class CharMatrix extends AbstractMatrix<char[], CharList, CharStream, Stream<CharStream>, CharMatrix> {

    static final Random RAND = new SecureRandom();
    static final int BOUND = Character.MAX_VALUE + 1;
    static final CharMatrix EMPTY_CHAR_MATRIX = new CharMatrix(new char[0][0]);

    /**
     * Constructs a new CharMatrix with the specified two-dimensional char array.
     * If the input array is null, an empty matrix (0x0) is created.
     *
     * <p><b>Important:</b> The input array is used directly without defensive copying.
     * This means modifications to the input array after construction will affect the matrix,
     * and vice versa. For independent matrices, create a copy of the array before passing it.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] data = {{'a', 'b'}, {'c', 'd'}};
     * CharMatrix matrix = new CharMatrix(data);
     * data[0][0] = 'x';  // This also changes matrix.get(0,0) to 'x'
     *
     * CharMatrix empty = new CharMatrix(null);   // Creates 0x0 empty matrix
     * }</pre>
     *
     * @param a the two-dimensional char array to initialize the matrix with, or null for an empty matrix
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
     * // matrix.rowCount() returns 0
     * // matrix.columnCount() returns 0
     * }</pre>
     *
     * @return an empty char matrix
     */
    public static CharMatrix empty() {
        return EMPTY_CHAR_MATRIX;
    }

    /**
     * Creates a CharMatrix from a two-dimensional char array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * // matrix.get(1, 0) returns 'c'
     * }</pre>
     *
     * @param a the two-dimensional char array to create the matrix from, or null/empty for an empty matrix
     * @return a new CharMatrix containing the provided data, or an empty CharMatrix if input is null or empty
     */
    public static CharMatrix of(final char[]... a) {
        return N.isEmpty(a) ? EMPTY_CHAR_MATRIX : new CharMatrix(a);
    }

    /**
     * Creates a new 1xsize matrix filled with random char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.random(5);
     * // Result: a 1x5 matrix with random char values
     * }</pre>
     *
     * @param size the number of columns in the new matrix
     * @return a new CharMatrix of dimensions 1 x size filled with random values
     */
    public static CharMatrix random(final int size) {
        return random(1, size);
    }

    /**
     * Creates a new matrix of the specified dimensions filled with random char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.random(2, 3);
     * // Result: a 2x3 matrix with random characters
     * }</pre>
     *
     * @param rowCount the number of rows in the new matrix
     * @param columnCount the number of columns in the new matrix
     * @return a new CharMatrix of dimensions rowCount x columnCount filled with random values
     */
    public static CharMatrix random(final int rowCount, final int columnCount) {
        final char[][] a = new char[rowCount][columnCount];

        for (char[] ea : a) {
            for (int i = 0; i < columnCount; i++) {
                ea[i] = (char) RAND.nextInt(BOUND);
            }
        }

        return new CharMatrix(a);
    }

    /**
     * Creates a new matrix of the specified dimensions where every element is the provided {@code element}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.repeat(2, 3, 'a');
     * // Result: [['a', 'a', 'a'], ['a', 'a', 'a']]
     * }</pre>
     *
     * @param rowCount the number of rows in the new matrix
     * @param columnCount the number of columns in the new matrix
     * @param element the char value to fill the matrix with
     * @return a new CharMatrix of dimensions rowCount x columnCount filled with the specified element
     */
    public static CharMatrix repeat(final int rowCount, final int columnCount, final char element) {
        final char[][] a = new char[rowCount][columnCount];

        for (char[] ea : a) {
            N.fill(ea, element);
        }

        return new CharMatrix(a);
    }

    /**
     * Creates a single-row CharMatrix containing a range of char values.
     * The range is [startInclusive, endExclusive).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.range('a', 'e');   // Creates [['a', 'b', 'c', 'd']]
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
     * The range increments by the specified step size. Supports both ascending (positive step)
     * and descending (negative step) sequences.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.range('a', 'g', 2);   // Creates [['a', 'c', 'e']]
     * CharMatrix desc = CharMatrix.range('z', 'u', -2);    // Creates [['z', 'x', 'v']]
     * CharMatrix empty = CharMatrix.range('a', 'z', -1);   // Creates an empty matrix (step is wrong direction)
     * }</pre>
     *
     * @param startInclusive the starting char value (inclusive)
     * @param endExclusive the ending char value (exclusive)
     * @param by the step size (must not be zero; can be positive or negative)
     * @return a new 1×n CharMatrix with values incremented by the step size
     * @throws IllegalArgumentException if {@code by} is zero
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
     * CharMatrix matrix = CharMatrix.rangeClosed('a', 'd');   // Creates [['a', 'b', 'c', 'd']]
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
     * The range is [startInclusive, endInclusive]. The range increments by the specified step size.
     * Supports both ascending (positive step) and descending (negative step) sequences.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.rangeClosed('a', 'g', 2);    // Creates [['a', 'c', 'e', 'g']]
     * CharMatrix partial = CharMatrix.rangeClosed('a', 'i', 2);   // Creates [['a', 'c', 'e', 'g', 'i']]
     * CharMatrix desc = CharMatrix.rangeClosed('z', 'u', -2);     // Creates [['z', 'x', 'v']]
     * }</pre>
     *
     * @param startInclusive the starting char value (inclusive)
     * @param endInclusive the ending char value (inclusive, if reachable by stepping)
     * @param by the step size (must not be zero; can be positive or negative)
     * @return a new 1×n CharMatrix with values incremented by the step size
     * @throws IllegalArgumentException if {@code by} is zero
     */
    public static CharMatrix rangeClosed(final char startInclusive, final char endInclusive, final int by) {
        return new CharMatrix(new char[][] { Array.rangeClosed(startInclusive, endInclusive, by) });
    }

    /**
     * Creates a square matrix from the specified main diagonal elements (left-up to right-down).
     * All other elements (off-diagonal) are set to zero (the null character '\u0000'). The matrix size is n×n where n is the length
     * of the diagonal array. The main diagonal runs from top-left to bottom-right.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.diagonalLU2RD(new char[] {'a', 'b', 'c'});
     * // Creates 3x3 matrix with diagonal ['a', 'b', 'c'] and zeros elsewhere
     * // Resulting matrix:
     * //   {'a', '\u0000', '\u0000'},
     * //   {'\u0000', 'b', '\u0000'},
     * //   {'\u0000', '\u0000', 'c'}
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements
     * @return a square matrix with the specified main diagonal (n×n where n = diagonal length)
     */
    public static CharMatrix diagonalLU2RD(final char[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square matrix from the specified anti-diagonal elements (right-upper to left-down).
     * All other elements (off-diagonal) are set to zero (the null character '\u0000'). The matrix size is n×n where n is the length
     * of the diagonal array. The anti-diagonal runs from top-right to bottom-left.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.diagonalRU2LD(new char[] {'a', 'b', 'c'});
     * // Creates 3x3 matrix with anti-diagonal ['a', 'b', 'c'] and zeros elsewhere
     * // Resulting matrix:
     * //   {'\u0000', '\u0000', 'a'},
     * //   {'\u0000', 'b', '\u0000'},
     * //   {'c', '\u0000', '\u0000'}
     * }</pre>
     *
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements
     * @return a square matrix with the specified anti-diagonal (n×n where n = diagonal length)
     */
    public static CharMatrix diagonalRU2LD(final char[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a square matrix from the specified main diagonal and anti-diagonal elements.
     * All other elements are set to zero (the null character '\u0000'). If both arrays are provided, they must have the same length.
     * The resulting matrix has dimensions n×n where n is the length of the non-null/non-empty array
     * (or the maximum length if both are provided).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.diagonal(new char[] {'a', 'b', 'c'}, new char[] {'x', 'y', 'z'});
     * // Creates 3x3 matrix with both diagonals set
     * // Resulting matrix:
     * //   {'a', '\u0000', 'x'},
     * //   {'\u0000', 'b', '\u0000'},
     * //   {'z', '\u0000', 'c'}
     *
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements (can be null or empty)
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements (can be null or empty)
     * @return a square matrix with the specified diagonals, or an empty matrix if both inputs are null or empty
     * @throws IllegalArgumentException if both arrays are non-empty and have different lengths
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
     * Converts a boxed Character Matrix to a primitive CharMatrix.
     * Null values in the input matrix are converted to {@code '\u0000'} (the null character).
     *
     * <p>This method performs the opposite operation of {@link #boxed()}, converting
     * from object-based Character values to primitive char values. This conversion
     * improves memory efficiency and performance when working with large matrices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<Character> boxed = Matrix.of(new Character[][] {{'a', 'b'}, {null, 'c'}});
     * CharMatrix primitive = CharMatrix.unbox(boxed);
     * // null is converted to '\u0000': [['a', 'b'], ['\u0000', 'c']]
     * }</pre>
     *
     * @param x the boxed Character Matrix to convert; must not be null
     * @return a new CharMatrix with primitive char values
     * @see #boxed()
     */
    public static CharMatrix unbox(final Matrix<Character> x) {
        return CharMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of the matrix elements, which is always {@code char.class}.
     * This method is useful for reflection-based code that needs to determine the element type.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * Class componentType = matrix.componentType();
     * // componentType is char.class
     * assert componentType == char.class;
     * }</pre>
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
     * char value = matrix.get(0, 1);   // Returns 'b'
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position (rowIndex, columnIndex)
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public char get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Returns the element at the specified point.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * Sheet.Point point = Sheet.Point.of(0, 1);
     * char value = matrix.get(point);   // Returns 'b'
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @return the char element at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #get(int, int)
     */
    public char get(final Point point) {
        N.checkArgNotNull(point, "point");

        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.set(0, 1, 'x');   // Sets element at row 0, column 1 to 'x'
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public void set(final int i, final int j, final char val) {
        a[i][j] = val;
    }

    /**
     * Sets the element at the specified point to the given value.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * Sheet.Point point = Sheet.Point.of(0, 1);
     * matrix.set(point, 'x');
     * assert matrix.get(point) == 'x';
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @param val the new char value to set at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #set(int, int, char)
     */
    public void set(final Point point, final char val) {
        N.checkArgNotNull(point, "point");

        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element above the specified position, if it exists.
     * This method provides safe access to the element directly above the given position
     * without throwing an exception when at the top edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * OptionalChar value = matrix.upOf(1, 0);   // Returns OptionalChar.of('a')
     * OptionalChar empty = matrix.upOf(0, 0);   // Returns OptionalChar.empty() - no row above
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalChar containing the element at position (rowIndex - 1, columnIndex), or empty if rowIndex == 0
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalChar upOf(final int i, final int j) {
        checkRowColumnIndex(i, j);

        return i == 0 ? OptionalChar.empty() : OptionalChar.of(a[i - 1][j]);
    }

    /**
     * Returns the element below the specified position, if it exists.
     * This method provides safe access to the element directly below the given position
     * without throwing an exception when at the bottom edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * OptionalChar value = matrix.downOf(0, 0);   // Returns OptionalChar.of('c')
     * OptionalChar empty = matrix.downOf(1, 0);   // Returns OptionalChar.empty() - no row below
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalChar containing the element at position (rowIndex + 1, columnIndex), or empty if rowIndex == rowCount - 1
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalChar downOf(final int i, final int j) {
        checkRowColumnIndex(i, j);

        return i == rowCount - 1 ? OptionalChar.empty() : OptionalChar.of(a[i + 1][j]);
    }

    /**
     * Returns the element to the left of the specified position, if it exists.
     * This method provides safe access to the element directly to the left of the given position
     * without throwing an exception when at the leftmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * OptionalChar value = matrix.leftOf(0, 1);   // Returns OptionalChar.of('a')
     * OptionalChar empty = matrix.leftOf(0, 0);   // Returns OptionalChar.empty() - no column to the left
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalChar containing the element at position (rowIndex, columnIndex - 1), or empty if columnIndex == 0
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalChar leftOf(final int i, final int j) {
        checkRowColumnIndex(i, j);

        return j == 0 ? OptionalChar.empty() : OptionalChar.of(a[i][j - 1]);
    }

    /**
     * Returns the element to the right of the specified position, if it exists.
     * This method provides safe access to the element directly to the right of the given position
     * without throwing an exception when at the rightmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * OptionalChar value = matrix.rightOf(0, 0);   // Returns OptionalChar.of('b')
     * OptionalChar empty = matrix.rightOf(0, 1);   // Returns OptionalChar.empty() - no column to the right
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalChar containing the element at position (rowIndex, columnIndex + 1), or empty if columnIndex == columnCount - 1
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalChar rightOf(final int i, final int j) {
        checkRowColumnIndex(i, j);

        return j == columnCount - 1 ? OptionalChar.empty() : OptionalChar.of(a[i][j + 1]);
    }

    /**
     * Returns the specified row as a char array.
     *
     * <p><b>Note:</b> This method returns a reference to the internal array, not a copy.
     * Modifications to the returned array will affect the matrix. If you need an independent
     * copy, use {@code Arrays.copyOf(matrix.row(i), matrix.columnCount())}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * char[] firstRow = matrix.row(0);   // Returns ['a', 'b', 'c']
     *
     * // Direct modification affects the matrix
     * firstRow[0] = 'x';  // matrix now has 'x' at position (0,0)
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return the specified row array (direct reference to internal storage)
     * @throws IllegalArgumentException if rowIndex &lt; 0 or rowIndex &gt;= rows
     */
    public char[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rowCount, MSG_ROW_INDEX_OUT_OF_BOUNDS, rowIndex, rowCount);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as a new char array.
     *
     * <p>Unlike {@link #row(int)}, this method always returns a new array copy since
     * columns are not stored contiguously in memory. Modifications to the returned array
     * will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * char[] firstColumn = matrix.column(0);   // Returns ['a', 'd']
     *
     * // Modification does NOT affect the matrix (it's a copy)
     * firstColumn[0] = 'x';  // matrix still has 'a' at position (0,0)
     * }</pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing the values from the specified column
     * @throws IllegalArgumentException if columnIndex &lt; 0 or columnIndex &gt;= columnCount
     */
    public char[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < columnCount, MSG_COLUMN_INDEX_OUT_OF_BOUNDS, columnIndex, columnCount);

        final char[] c = new char[rowCount];

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
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * matrix.setRow(0, new char[] {'x', 'y', 'z'});   // First row is now ['x', 'y', 'z']
     * }</pre>
     *
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to copy into the row; must have length equal to the number of columns
     * @throws IllegalArgumentException if rowIndex is out of bounds or row length does not match column count
     */
    public void setRow(final int rowIndex, final char[] row) throws IllegalArgumentException {
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
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * matrix.setColumn(0, new char[] {'x', 'y'});   // First column is now ['x', 'y']
     * // Matrix is now: [['x', 'b', 'c'], ['y', 'e', 'f']]
     * }</pre>
     *
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to copy into the column; must have length equal to the number of rows
     * @throws IllegalArgumentException if columnIndex is out of bounds or column length does not match row count
     */
    public void setColumn(final int columnIndex, final char[] column) throws IllegalArgumentException {
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
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * matrix.updateRow(0, c -> Character.toUpperCase(c));   // Converts first row to uppercase
     * // matrix is now [['A', 'B', 'C'], ['d', 'e', 'f']]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param rowIndex the index of the row to update (0-based)
     * @param operator the operator to apply to each element in the row; receives the current
     *             element value and returns the new value
     * @throws ArrayIndexOutOfBoundsException if rowIndex is out of bounds
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.CharUnaryOperator<E> operator) throws E {
        for (int i = 0; i < columnCount; i++) {
            a[rowIndex][i] = operator.applyAsChar(a[rowIndex][i]);
        }
    }

    /**
     * Updates all elements in a column in-place by applying the specified operator.
     * This modifies the matrix directly.
     *
     * <p>The operator is applied to each element in the specified column sequentially
     * from top to bottom (row 0 to row rows-1).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'A', 'B'}, {'C', 'D'}});
     * matrix.updateColumn(1, c -> Character.toLowerCase(c));   // Converts second column to lowercase
     * // matrix is now [['A', 'b'], ['C', 'd']]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param columnIndex the index of the column to update (0-based)
     * @param operator the operator to apply to each element in the column; receives the current
     *             element value and returns the new value
     * @throws ArrayIndexOutOfBoundsException if columnIndex is out of bounds
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.CharUnaryOperator<E> operator) throws E {
        for (int i = 0; i < rowCount; i++) {
            a[i][columnIndex] = operator.applyAsChar(a[i][columnIndex]);
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
     * char[] diagonal = matrix.getLU2RD();   // Returns ['a', 'e', 'i']
     * }</pre>
     *
     * @return a new char array containing the main diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public char[] getLU2RD() throws IllegalStateException {
        checkIfRowAndColumnSizeAreSame();

        final char[] res = new char[rowCount];

        for (int i = 0; i < rowCount; i++) {
            res[i] = a[i][i]; // NOSONAR
        }

        return res;
    }

    /**
     * Sets the elements on the main diagonal from left-upper to right-down (main diagonal).
     * The matrix must be square (rows == columns), and the diagonal array must have
     * exactly as many elements as the matrix has rows.
     *
     * <p>This method sets the main diagonal elements at positions (0,0), (1,1), (2,2), etc.
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
     * @param diagonal the new values for the main diagonal; must have length equal to rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length does not equal to rows
     */
    public void setLU2RD(final char[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length == rowCount, MSG_DIAGONAL_LENGTH_MISMATCH, rowCount, diagonal.length);

        for (int i = 0; i < rowCount; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates the values on the main diagonal (left-up to right-down) by applying the specified operator.
     * The matrix must be square.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.updateLU2RD(c -> Character.toUpperCase(c));   // Converts diagonal to uppercase
     * // Diagonal is now ['A', 'D'], matrix: [['A', 'b'], ['c', 'D']]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param operator the operator to apply to each diagonal element
     * @throws IllegalStateException if the matrix is not square
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateLU2RD(final Throwables.CharUnaryOperator<E> operator) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rowCount; i++) {
            a[i][i] = operator.applyAsChar(a[i][i]);
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
     * char[] diagonal = matrix.getRU2LD();   // Returns ['c', 'e', 'g']
     * }</pre>
     *
     * @return a new char array containing the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public char[] getRU2LD() throws IllegalStateException {
        checkIfRowAndColumnSizeAreSame();

        final char[] res = new char[rowCount];

        for (int i = 0; i < rowCount; i++) {
            res[i] = a[i][columnCount - i - 1];
        }

        return res;
    }

    /**
     * Sets the elements on the anti-diagonal from right-upper to left-down (anti-diagonal).
     * The matrix must be square (rows == columns), and the diagonal array must have
     * a length equal to the number of rows in the matrix.
     *
     * <p>This method sets the anti-diagonal (secondary diagonal) elements from
     * top-right to bottom-left, at positions (0,n-1), (1,n-2), (2,n-3), etc.
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
     * @param diagonal the new values for the anti-diagonal; must have length equal to rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length != rows
     */
    public void setRU2LD(final char[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length == rowCount, MSG_DIAGONAL_LENGTH_MISMATCH, rowCount, diagonal.length);

        for (int i = 0; i < rowCount; i++) {
            a[i][columnCount - i - 1] = diagonal[i];
        }
    }

    /**
     * Updates the elements on the anti-diagonal (right-upper to left-down) using the specified operator.
     * The matrix must be square. Each anti-diagonal element is replaced with the result of applying
     * the operator to that element.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.updateRU2LD(c -> Character.toUpperCase(c));
     * // Anti-diagonal is now ['B', 'C'], matrix: [['a', 'B'], ['C', 'd']]
     * }</pre>
     *
     * @param <E> the exception type that the operator may throw
     * @param operator the operator to apply to each anti-diagonal element
     * @throws E if the operator throws an exception
     * @throws IllegalStateException if the matrix is not square (rows != columnCount)
     */
    public <E extends Exception> void updateRU2LD(final Throwables.CharUnaryOperator<E> operator) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rowCount; i++) {
            a[i][columnCount - i - 1] = operator.applyAsChar(a[i][columnCount - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix using the specified operator in-place.
     *
     * <p>Each element is replaced with the result of applying the operator to that element.
     * For large matrices, this operation may be performed in parallel to improve performance.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * matrix.updateAll(c -> Character.toUpperCase(c));
     * // Matrix is now [['A', 'B'], ['C', 'D']]
     * }</pre>
     *
     * @param <E> the exception type that the operator may throw
     * @param operator the operator to apply to each element
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.CharUnaryOperator<E> operator) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = operator.applyAsChar(a[i][j]);
        Matrixes.run(rowCount, columnCount, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Updates all elements in the matrix based on their position using a position-aware operator.
     *
     * <p>The operator receives the row and column indices and returns the new value for that position.
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
     * @param <E> the exception type that the operator may throw
     * @param operator the operator that takes (rowIndex, columnIndex) and returns the new char value
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Character, E> operator) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = operator.apply(i, j);
        Matrixes.run(rowCount, columnCount, cmd, Matrixes.isParallelable(this));
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
     * matrix.replaceIf(c -> c < 'd', 'x');   // Replace all chars less than 'd' with 'x'
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
        Matrixes.run(rowCount, columnCount, cmd, Matrixes.isParallelable(this));
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
     * matrix.replaceIf((i, j) -> i == j, 'X');   // Replace main diagonal elements with 'X'
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
        Matrixes.run(rowCount, columnCount, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new CharMatrix by applying a transformation function to each element.
     * The original matrix is not modified; a new matrix with transformed values is returned.
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.
     * This is the immutable counterpart to {@link #updateAll(Throwables.CharUnaryOperator)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix upper = matrix.map(c -> Character.toUpperCase(c));   // Creates new matrix with uppercase values
     * // upper is [['A', 'B'], ['C', 'D']], original matrix unchanged
     *
     * CharMatrix lower = matrix.map(c -> Character.toLowerCase(c));   // Convert all to lowercase
     * // lower is [['a', 'b'], ['c', 'd']]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param mapper the function to apply to each element; receives the current element value
     *             and returns the transformed value
     * @return a new CharMatrix with transformed values
     * @throws E if the function throws an exception
     * @see #updateAll(Throwables.CharUnaryOperator)
     */
    public <E extends Exception> CharMatrix map(final Throwables.CharUnaryOperator<E> mapper) throws E {
        final char[][] result = new char[rowCount][columnCount];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = mapper.applyAsChar(a[i][j]);

        Matrixes.run(rowCount, columnCount, cmd, Matrixes.isParallelable(this));

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
     * @param mapper the mapping function that converts each char to an object of type T
     * @param targetElementType the class object representing the target element type (required for array creation)
     * @return a new Matrix&lt;T&gt; with the mapped object values
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.CharFunction<? extends T, E> mapper, final Class<T> targetElementType) throws E {
        final T[][] result = Matrixes.newArray(rowCount, columnCount, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = mapper.apply(a[i][j]);

        Matrixes.run(rowCount, columnCount, cmd, Matrixes.isParallelable(this));

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
     * matrix.fill('x');   // All elements become 'x'
     * // Matrix is now [['x', 'x'], ['x', 'x']]
     * }</pre>
     *
     * @param val the value to fill the matrix with
     */
    public void fill(final char val) {
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
     * CharMatrix matrix = CharMatrix.of(new char[3][3]);
     * matrix.fill(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * // Top-left 2x2 region is filled: [['a', 'b', 0], ['c', 'd', 0], [0, 0, 0]]
     * }</pre>
     *
     * @param b the source array to copy values from (maybe smaller or larger than the matrix)
     */
    public void fill(final char[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from the specified two-dimensional array in-place, starting from a specified position.
     * Values are copied starting from the specified row and column indices. If the source array extends
     * beyond the matrix bounds from the starting position, only the portion that fits is copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[3][3]);
     * matrix.fill(1, 1, new char[][] {{'a', 'b'}, {'c', 'd'}});
     * // Result: [[0, 0, 0], [0, 'a', 'b'], [0, 'c', 'd']]
     * }</pre>
     *
     * @param fromRowIndex the starting row index in this matrix (0-based)
     * @param fromColumnIndex the starting column index in this matrix (0-based)
     * @param b the source array to copy values from
     * @throws IllegalArgumentException if the starting indices are negative or exceed matrix dimensions
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final char[][] b) throws IllegalArgumentException {
        N.checkArgNotNull(b, cs.b);
        N.checkArgument(fromRowIndex >= 0 && fromRowIndex <= rowCount, "fromRowIndex(%s) must be between 0 and rowCount(%s)", fromRowIndex, rowCount);
        N.checkArgument(fromColumnIndex >= 0 && fromColumnIndex <= columnCount, "fromColumnIndex(%s) must be between 0 and columnCount(%s)", fromColumnIndex,
                columnCount);

        for (int i = 0, minLen = N.min(rowCount - fromRowIndex, b.length); i < minLen; i++) {
            if (b[i] != null) {
                N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, columnCount - fromColumnIndex));
            }
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
        final char[][] c = new char[rowCount][];

        for (int i = 0; i < rowCount; i++) {
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
     * CharMatrix copy = matrix.copy(1, 3);   // Returns [['c', 'd'], ['e', 'f']]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new CharMatrix containing the specified rows
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows, or fromRowIndex &gt; toRowIndex
     */
    @Override
    public CharMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);

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
     * CharMatrix sub = matrix.copy(0, 2, 1, 3);   // Copy rows 0-1, columns 1-2
     * // Result: [['b', 'c'], ['e', 'f']]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new CharMatrix containing the specified region
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows, fromRowIndex &gt; toRowIndex,
     *         fromColumnIndex &lt; 0, toColumnIndex &gt; columnCount, or fromColumnIndex &gt; toColumnIndex
     */
    @Override
    public CharMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

        final char[][] c = new char[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new CharMatrix(c);
    }

    /**
     * Creates a new matrix by extending or truncating this matrix to the specified dimensions.
     * New cells are filled with {@code '\u0000'}.
     *
     * <p>If the new dimensions are smaller than the current dimensions, the matrix is truncated.
     * If larger, the existing content is preserved in the top-left corner and new cells are filled with '\u0000'.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix extended = matrix.extend(3, 3);
     * // Result: [['a', 'b', '\u0000'],
     * //          ['c', 'd', '\u0000'],
     * //          ['\u0000', '\u0000', '\u0000']]
     * }</pre>
     *
     * @param newRowCount the number of rows in the new matrix. It can be smaller than the row number of the current matrix but must be non-negative
     * @param newColumnCount the number of columns in the new matrix. It can be smaller than the column number of the current matrix but must be non-negative
     * @return a new CharMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRowCount} or {@code newColumnCount} is negative
     */
    public CharMatrix extend(final int newRowCount, final int newColumnCount) {
        return extend(newRowCount, newColumnCount, CHAR_0);
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
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix extended = matrix.extend(3, 4, 'x');   // Extend to 3x4, fill new cells with 'x'
     * // Result: [['a', 'b', 'x', 'x'],
     * //          ['c', 'd', 'x', 'x'],
     * //          ['x', 'x', 'x', 'x']]
     *
     * // Truncate to smaller size
     * CharMatrix truncated = matrix.extend(1, 1, '\u0000');   // Keep only top-left element
     * // Result: [['a']]
     * }</pre>
     *
     * @param newRowCount the number of rows in the new matrix. It can be smaller than the row number of the current matrix but must be non-negative
     * @param newColumnCount the number of columns in the new matrix. It can be smaller than the column number of the current matrix but must be non-negative
     * @param defaultValueForNewCell the char value to fill new cells with during extension
     * @return a new CharMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRowCount} or {@code newColumnCount} is negative,
     *         or if the resulting matrix would be too large (dimensions exceeding Integer.MAX_VALUE elements)
     */
    public CharMatrix extend(final int newRowCount, final int newColumnCount, final char defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRowCount >= 0, MSG_NEGATIVE_DIMENSION, "newRowCount", newRowCount);
        N.checkArgument(newColumnCount >= 0, MSG_NEGATIVE_DIMENSION, "newColumnCount", newColumnCount);

        // Check for overflow before allocation
        if ((long) newRowCount * newColumnCount > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Matrix dimensions overflow: " + newRowCount + " x " + newColumnCount + " exceeds Integer.MAX_VALUE");
        }

        if (newRowCount <= rowCount && newColumnCount <= columnCount) {
            return copy(0, newRowCount, 0, newColumnCount);
        } else {
            final boolean fillDefaultValue = defaultValueForNewCell != CHAR_0;
            final char[][] b = new char[newRowCount][];

            for (int i = 0; i < newRowCount; i++) {
                b[i] = i < rowCount ? N.copyOf(a[i], newColumnCount) : new char[newColumnCount];

                if (fillDefaultValue) {
                    if (i >= rowCount) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (columnCount < newColumnCount) {
                        N.fill(b[i], columnCount, newColumnCount, defaultValueForNewCell);
                    }
                }
            }

            return new CharMatrix(b);
        }
    }

    /**
     * Creates a new matrix by extending this matrix in all four directions.
     * New cells are filled with {@code '\u0000'}.
     *
     * <p>This method adds padding around the existing matrix, with the original content
     * positioned according to the specified padding amounts.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}});
     * CharMatrix extended = matrix.extend(1, 1, 1, 1);
     * // Result: [['\u0000', '\u0000', '\u0000', '\u0000'],
     * //          ['\u0000', 'a',      'b',      '\u0000'],
     * //          ['\u0000', '\u0000', '\u0000', '\u0000']]
     * }</pre>
     *
     * @param toUp number of rowCount to add above; must be non-negative
     * @param toDown number of rowCount to add below; must be non-negative
     * @param toLeft number of columnCount to add to the left; must be non-negative
     * @param toRight number of columnCount to add to the right; must be non-negative
     * @return a new extended CharMatrix with dimensions ((toUp+rowCount+toDown) x (toLeft+columnCount+toRight))
     * @throws IllegalArgumentException if any parameter is negative
     */
    public CharMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, CHAR_0);
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
     *   <li>Rows: {@code toUp + this.rowCount + toDown}</li>
     *   <li>Columns: {@code toLeft + this.columnCount + toRight}</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}});
     * CharMatrix padded = matrix.extend(1, 1, 2, 2, 'x');
     * // Result: [['x', 'x', 'x', 'x', 'x', 'x'],
     * //          ['x', 'x', 'a', 'b', 'x', 'x'],
     * //          ['x', 'x', 'x', 'x', 'x', 'x']]
     *
     * // Add border of '\u0000' values
     * CharMatrix bordered = matrix.extend(1, 1, 1, 1, '\u0000');
     * // Result: [['\u0000', '\u0000', '\u0000', '\u0000'],
     * //          ['\u0000', 'a',      'b',      '\u0000'],
     * //          ['\u0000', '\u0000', '\u0000', '\u0000']]
     * }</pre>
     *
     * @param toUp number of rowCount to add above; must be non-negative
     * @param toDown number of rowCount to add below; must be non-negative
     * @param toLeft number of columnCount to add to the left; must be non-negative
     * @param toRight number of columnCount to add to the right; must be non-negative
     * @param defaultValueForNewCell the char value to fill all new cells with
     * @return a new extended CharMatrix with dimensions ((toUp+rowCount+toDown) x (toLeft+columnCount+toRight))
     * @throws IllegalArgumentException if any padding parameter is negative,
     *         or if the resulting dimensions would exceed Integer.MAX_VALUE
     */
    public CharMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final char defaultValueForNewCell)
            throws IllegalArgumentException {
        N.checkArgument(toUp >= 0, MSG_NEGATIVE_DIMENSION, "toUp", toUp);
        N.checkArgument(toDown >= 0, MSG_NEGATIVE_DIMENSION, "toDown", toDown);
        N.checkArgument(toLeft >= 0, MSG_NEGATIVE_DIMENSION, "toLeft", toLeft);
        N.checkArgument(toRight >= 0, MSG_NEGATIVE_DIMENSION, "toRight", toRight);

        if (toUp == 0 && toDown == 0 && toLeft == 0 && toRight == 0) {
            return copy();
        } else {
            if ((long) toUp + rowCount + toDown > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Result rowCount overflow: " + toUp + " + " + rowCount + " + " + toDown + " exceeds Integer.MAX_VALUE");
            }

            if ((long) toLeft + columnCount + toRight > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(
                        "Result columnCount overflow: " + toLeft + " + " + columnCount + " + " + toRight + " exceeds Integer.MAX_VALUE");
            }

            final int newRowCount = toUp + rowCount + toDown;
            final int newColumnCount = toLeft + columnCount + toRight;
            final boolean fillDefaultValue = defaultValueForNewCell != CHAR_0;
            final char[][] b = new char[newRowCount][newColumnCount];

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
        for (int i = 0; i < rowCount; i++) {
            N.reverse(a[i]);
        }
    }

    /**
     * Reverses the order of rows in the matrix (vertical flip in-place).
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
        for (int j = 0; j < columnCount; j++) {
            char tmp = 0;
            for (int l = 0, h = rowCount - 1; l < h;) {
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
     * CharMatrix flipped = matrix.flipH();   // Returns [['c', 'b', 'a']]
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
     * CharMatrix flipped = matrix.flipV();   // Returns [['e', 'f'], ['c', 'd'], ['a', 'b']]
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
        final char[][] c = new char[columnCount][rowCount];

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
        final char[][] c = new char[rowCount][];

        for (int i = 0; i < rowCount; i++) {
            c[i] = a[rowCount - i - 1].clone();
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
        final char[][] c = new char[columnCount][rowCount];

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

        return new CharMatrix(c);
    }

    /**
     * Creates the transpose of this matrix by swapping rows and columns.
     * The transpose operation converts each row into a column, so element at position (i, j)
     * in the original matrix appears at position (j, i) in the transposed matrix. The resulting
     * matrix has dimensions swapped (rows × columnCount becomes columnCount × rows).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:  Transposed:
     * // a b c      a d
     * // d e f      b e
     * //            c f
     *
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * CharMatrix transposed = matrix.transpose();   // 2×3 becomes 3×2
     * }</pre>
     *
     * @return a new matrix that is the transpose of this matrix with dimensions columnCount × rows
     */
    @Override
    public CharMatrix transpose() {
        final char[][] c = new char[columnCount][rowCount];

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
     * @param newRowCount the number of rows in the reshaped matrix (must be &gt;= 0)
     * @param newColumnCount the number of columns in the reshaped matrix (must be &gt;= 0)
     * @return a new CharMatrix with the specified dimensions
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public CharMatrix reshape(final int newRowCount, final int newColumnCount) {
        final char[][] c = new char[newRowCount][newColumnCount];

        if (newRowCount == 0 || newColumnCount == 0 || N.isEmpty(a)) {
            return new CharMatrix(c);
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

        return new CharMatrix(c);
    }

    /**
     * Repeats each element in both row and column directions.
     * Each element is repeated rowRepeats times vertically and colRepeats times horizontally.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}});
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
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, MSG_REPEATS_NOT_POSITIVE, rowRepeats, colRepeats);

        // Check for overflow before allocation
        if ((long) rowCount * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result row count overflow: " + rowCount + " * " + rowRepeats + " exceeds Integer.MAX_VALUE");
        }
        if ((long) columnCount * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result column count overflow: " + columnCount + " * " + colRepeats + " exceeds Integer.MAX_VALUE");
        }

        final char[][] c = new char[rowCount * rowRepeats][columnCount * colRepeats];

        for (int i = 0; i < rowCount; i++) {
            final char[] aa = a[i];
            final char[] fr = c[i * rowRepeats];

            for (int j = 0; j < columnCount; j++) {
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
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
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
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, MSG_REPEATS_NOT_POSITIVE, rowRepeats, colRepeats);

        // Check for overflow before allocation
        if ((long) rowCount * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result row count overflow: " + rowCount + " * " + rowRepeats + " exceeds Integer.MAX_VALUE");
        }
        if ((long) columnCount * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result column count overflow: " + columnCount + " * " + colRepeats + " exceeds Integer.MAX_VALUE");
        }

        final char[][] c = new char[rowCount * rowRepeats][columnCount * colRepeats];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colRepeats; j++) {
                N.copy(a[i], 0, c[i], j * columnCount, columnCount);
            }
        }

        for (int i = 1; i < rowRepeats; i++) {
            for (int j = 0; j < rowCount; j++) {
                N.copy(c[j], 0, c[i * rowCount + j], 0, c[j].length);
            }
        }

        return new CharMatrix(c);
    }

    /**
     * Returns a CharList containing all matrix elements in row-major order.
     *
     * <p>This method converts the two-dimensional matrix into a one-dimensional list by reading elements
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
     * @throws IllegalStateException if the matrix is too large to flatten (rows * columnCount &gt; Integer.MAX_VALUE)
     */
    @Override
    public CharList flatten() {
        // Check for overflow before allocation
        if ((long) rowCount * columnCount > Integer.MAX_VALUE) {
            throw new IllegalStateException("Matrix too large to flatten: " + rowCount + " x " + columnCount);
        }

        final char[] c = new char[rowCount * columnCount];

        for (int i = 0; i < rowCount; i++) {
            N.copy(a[i], 0, c, i * columnCount, columnCount);
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
     * CharMatrix stacked = a.vstack(b);   // Result: [['a', 'b'], ['c', 'd']]
     * }</pre>
     *
     * @param other the matrix to stack below this matrix
     * @return a new CharMatrix with other appended below this matrix
     * @throws IllegalArgumentException if the matrices have different column counts
     * @see IntMatrix#vstack(IntMatrix)
     */
    public CharMatrix vstack(final CharMatrix other) throws IllegalArgumentException {
        N.checkArgument(columnCount == other.columnCount, MSG_VSTACK_COLUMN_MISMATCH, columnCount, other.columnCount);

        final char[][] c = new char[rowCount + other.rowCount][];
        int j = 0;

        for (int i = 0; i < rowCount; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < other.rowCount; i++) {
            c[j++] = other.a[i].clone();
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
     * CharMatrix stacked = a.hstack(b);   // Result: [['a', 'c'], ['b', 'd']]
     * }</pre>
     *
     * @param other the matrix to stack to the right of this matrix
     * @return a new CharMatrix with other appended to the right of this matrix
     * @throws IllegalArgumentException if the matrices have different row counts
     * @see IntMatrix#hstack(IntMatrix)
     */
    public CharMatrix hstack(final CharMatrix other) throws IllegalArgumentException {
        N.checkArgument(rowCount == other.rowCount, MSG_HSTACK_ROW_MISMATCH, rowCount, other.rowCount);

        final char[][] c = new char[rowCount][columnCount + other.columnCount];

        for (int i = 0; i < rowCount; i++) {
            N.copy(a[i], 0, c[i], 0, columnCount);
            N.copy(other.a[i], 0, c[i], columnCount, other.columnCount);
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
     * CharMatrix sum = a.add(b);   // Result: [['b', 'd']] (a+1, b+2)
     * }</pre>
     *
     * @param other the matrix to add to this matrix
     * @return a new CharMatrix containing the element-wise sum
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public CharMatrix add(final CharMatrix other) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, other), "Cannot add matrices with different shapes: this is %sx%s but other is %sx%s", rowCount, columnCount,
                other.rowCount, other.columnCount);

        final char[][] otherArray = other.a;
        final char[][] result = new char[rowCount][columnCount];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (char) (a[i][j] + otherArray[i][j]);

        Matrixes.run(rowCount, columnCount, cmd, Matrixes.isParallelable(this));

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
     * CharMatrix diff = a.subtract(b);   // Result: [['c', 'c']] (d-1, e-2)
     * }</pre>
     *
     * @param other the matrix to subtract from this matrix
     * @return a new CharMatrix containing the element-wise difference
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public CharMatrix subtract(final CharMatrix other) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, other), "Cannot subtract matrices with different shapes: this is %sx%s but other is %sx%s", rowCount,
                columnCount, other.rowCount, other.columnCount);

        final char[][] otherArray = other.a;
        final char[][] result = new char[rowCount][columnCount];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (char) (a[i][j] - otherArray[i][j]);

        Matrixes.run(rowCount, columnCount, cmd, Matrixes.isParallelable(this));

        return CharMatrix.of(result);
    }

    /**
     * Performs matrix multiplication with another matrix.
     * The number of columnCount in this matrix must equal the number of rowCount in the other matrix.
     * The resulting matrix will have dimensions [this.rowCount x b.columnCount].
     * 
     * <p>Note: Since char values are used, the multiplication may result in overflow
     * or unexpected character values. Consider using IntMatrix or DoubleMatrix for
     * numerical computations.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix a = CharMatrix.of(new char[][] {{2, 3}, {4, 5}});
     * CharMatrix b = CharMatrix.of(new char[][] {{1, 2}, {3, 4}});
     * CharMatrix product = a.multiply(b);   // Standard matrix multiplication
     * }</pre>
     *
     * @param other the matrix to multiply with this matrix
     * @return a new CharMatrix containing the matrix product
     * @throws IllegalArgumentException if this.columnCount != other.rowCount
     */
    public CharMatrix multiply(final CharMatrix other) throws IllegalArgumentException {
        N.checkArgument(columnCount == other.rowCount,
                "Matrix dimensions incompatible for multiplication: this is %sx%s, other is %sx%s (this.columnCount must equal other.rowCount)", rowCount,
                columnCount, other.rowCount, other.columnCount);

        final char[][] otherArray = other.a;
        final char[][] result = new char[rowCount][other.columnCount];
        final Throwables.IntTriConsumer<RuntimeException> cmd = (i, j, k) -> result[i][j] += (char) (a[i][k] * otherArray[k][j]);

        Matrixes.multiply(this, other, cmd);

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
        final Character[][] c = new Character[rowCount][columnCount];

        if (rowCount <= columnCount) {
            for (int i = 0; i < rowCount; i++) {
                final char[] aa = a[i];
                final Character[] cc = c[i];

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
     * Converts this CharMatrix to an IntMatrix.
     * Each char value is converted to its corresponding int value (Unicode code point).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][] {{'a', 'b'}});
     * IntMatrix intMatrix = charMatrix.toIntMatrix();   // Result: [[97, 98]]
     * }</pre>
     *
     * @return a new IntMatrix with the same dimensions containing the int values of the characters
     */
    public IntMatrix toIntMatrix() {
        return IntMatrix.from(a);
    }

    /**
     * Converts this CharMatrix to a LongMatrix.
     * Each char value is converted to its corresponding long value (Unicode code point).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][] {{'a', 'b'}});
     * LongMatrix longMatrix = charMatrix.toLongMatrix();   // Result: [[97L, 98L]]
     * }</pre>
     *
     * @return a new LongMatrix with the same dimensions containing the long values of the characters
     */
    public LongMatrix toLongMatrix() {
        final long[][] c = new long[rowCount][columnCount];

        if (rowCount <= columnCount) {
            for (int i = 0; i < rowCount; i++) {
                final char[] aa = a[i];
                final long[] cc = c[i];

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

        return new LongMatrix(c);
    }

    /**
     * Converts this CharMatrix to a FloatMatrix.
     * Each char value is converted to its corresponding float value (Unicode code point).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][] {{'a', 'b'}});
     * FloatMatrix floatMatrix = charMatrix.toFloatMatrix();   // Result: [[97.0f, 98.0f]]
     * }</pre>
     *
     * @return a new FloatMatrix with the same dimensions containing the float values of the characters
     */
    public FloatMatrix toFloatMatrix() {
        final float[][] c = new float[rowCount][columnCount];

        if (rowCount <= columnCount) {
            for (int i = 0; i < rowCount; i++) {
                final char[] aa = a[i];
                final float[] cc = c[i];

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

        return new FloatMatrix(c);
    }

    /**
     * Converts this CharMatrix to a DoubleMatrix.
     * Each char value is converted to its corresponding double value (Unicode code point).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix charMatrix = CharMatrix.of(new char[][] {{'a', 'b'}});
     * DoubleMatrix doubleMatrix = charMatrix.toDoubleMatrix();   // Result: [[97.0, 98.0]]
     * }</pre>
     *
     * @return a new DoubleMatrix with the same dimensions containing the double values of the characters
     */
    public DoubleMatrix toDoubleMatrix() {
        final double[][] c = new double[rowCount][columnCount];

        if (rowCount <= columnCount) {
            for (int i = 0; i < rowCount; i++) {
                final char[] aa = a[i];
                final double[] cc = c[i];

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
        N.checkArgument(isSameShape(matrixB), "Cannot zip matrices with different shapes: this is %sx%s but other is %sx%s", rowCount, columnCount,
                matrixB.rowCount, matrixB.columnCount);

        final char[][] b = matrixB.a;
        final char[][] result = new char[rowCount][columnCount];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsChar(a[i][j], b[i][j]);

        Matrixes.run(rowCount, columnCount, cmd, Matrixes.isParallelable(this));

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
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Cannot zip matrices with different shapes: all matrices must be %sx%s", rowCount,
                columnCount);

        final char[][] b = matrixB.a;
        final char[][] c = matrixC.a;
        final char[][] result = new char[rowCount][columnCount];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsChar(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rowCount, columnCount, cmd, Matrixes.isParallelable(this));

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
     * CharStream diagonal = matrix.streamLU2RD();   // Stream of: 'a', 'e', 'i'
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
            private final int toIndex = rowCount;
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
     * CharStream diagonal = matrix.streamRU2LD();   // Stream of: 'c', 'e', 'g'
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
            private final int toIndex = rowCount;
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

                final char result = a[cursor][columnCount - cursor - 1];
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
     * Returns a stream of all elements in the matrix, traversed horizontally (row by row).
     * Elements are returned in row-major order: all elements from the first row,
     * then all elements from the second row, and so on.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharStream stream = matrix.streamH();   // Stream of: 'a', 'b', 'c', 'd'
     * }</pre>
     *
     * @return a CharStream containing all matrix elements in row-major order
     */
    @Override
    public CharStream streamH() {
        return streamH(0, rowCount);
    }

    /**
     * Returns a stream of elements from a specific row.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharStream row = matrix.streamH(1);   // Stream of: 'c', 'd'
     * }</pre>
     *
     * @param rowIndex the index of the row to stream (0-based)
     * @return a CharStream containing all elements from the specified row
     * @throws IndexOutOfBoundsException if rowIndex is negative or &gt;= number of rows
     */
    @Override
    public CharStream streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * Returns a CharStream of elements from a range of rowCount, traversed horizontally.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
     * CharStream stream = matrix.streamH(1, 3);   // Elements from rows 1 and 2
     * // stream contains: 'c', 'd', 'e', 'f'
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a CharStream of elements from the specified rows
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    @Override
    public CharStream streamH(final int fromRowIndex, final int toRowIndex) {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);

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
            public char[] toArray() {
                final int len = (int) count();
                final char[] c = new char[len];

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
     * Returns a stream of all elements in the matrix, traversed vertically (column by column).
     * Elements are returned in column-major order: all elements from the first column,
     * then all elements from the second column, and so on.
     * 
     * <p>Note: This method is marked as @Beta and may be subject to change.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharStream stream = matrix.streamV();   // Stream of: 'a', 'c', 'b', 'd'
     * }</pre>
     *
     * @return a CharStream containing all matrix elements in column-major order
     */
    @Override
    @Beta
    public CharStream streamV() {
        return streamV(0, columnCount);
    }

    /**
     * Returns a stream of elements from a specific column.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharStream column = matrix.streamV(1);   // Stream of: 'b', 'd'
     * }</pre>
     *
     * @param columnIndex the index of the column to stream (0-based)
     * @return a CharStream containing all elements from the specified column
     * @throws IndexOutOfBoundsException if columnIndex is negative or &gt;= number of columns
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
     * CharStream stream = matrix.streamV(1, 3);   // Stream of: 'b', 'e', 'c', 'f'
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a CharStream containing elements from the specified column range
     * @throws IndexOutOfBoundsException if the indices are out of bounds or fromColumnIndex &gt; toColumnIndex
     */
    @Override
    @Beta
    public CharStream streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

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

                if (n >= (long) (toColumnIndex - j) * CharMatrix.this.rowCount - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    final long offset = n + i;
                    i = (int) (offset % CharMatrix.this.rowCount);
                    j += (int) (offset / CharMatrix.this.rowCount);
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rowCount - i; // NOSONAR
            }

            @Override
            public char[] toArray() {
                final int len = (int) count();
                final char[] c = new char[len];

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
        return streamR(0, rowCount);
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
     * @throws IndexOutOfBoundsException if the indices are out of bounds or fromRowIndex &gt; toRowIndex
     */
    @Override
    public Stream<CharStream> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);

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
        return streamC(0, columnCount);
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
     * @throws IndexOutOfBoundsException if the indices are out of bounds or fromColumnIndex &gt; toColumnIndex
     */
    @Override
    @Beta
    public Stream<CharStream> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public CharStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return CharStream.of(new CharIteratorEx() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rowCount;
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
     * matrix.forEach(ch -> System.out.print(ch + " "));   // Prints: a b c d
     *
     * List<Character> chars = new ArrayList<>();
     * matrix.forEach(ch -> chars.add(ch));   // Collects all characters
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the action to be performed on each element
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.CharConsumer<E> action) throws E {
        forEach(0, rowCount, 0, columnCount, action);
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
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

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
     * <p>Each row is printed on a separate line with elements separated by commas and enclosed
     * in square brackets, providing a clear visual representation of the matrix structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
     * matrix.println();
     * // Output:
     * // [a, b, c]
     * // [d, e, f]
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
     * Returns {@code true} if the given object is also a CharMatrix with the same dimensions
     * and all corresponding elements are equal.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharMatrix m1 = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * CharMatrix m2 = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
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

        if (obj instanceof final CharMatrix another) {
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
     * CharMatrix matrix = CharMatrix.of(new char[][] {{'a', 'b'}, {'c', 'd'}});
     * System.out.println(matrix.toString());   // [[a, b], [c, d]]
     * }</pre>
     *
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
