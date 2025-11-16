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
import com.landawn.abacus.util.u.OptionalInt;
import com.landawn.abacus.util.stream.IntIteratorEx;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

/**
 * A matrix implementation for int primitive values, providing efficient storage and operations
 * for two-dimensional int arrays. This class extends AbstractMatrix and provides specialized
 * methods for int matrix manipulation including mathematical operations, transformations,
 * element access, and streaming capabilities.
 *
 * <p>The matrix is stored internally as a two-dimensional int array (int[][]) and provides
 * methods for element access, manipulation, and various matrix operations such as
 * transpose, rotation, multiplication, diagonal operations, and more.</p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Factory methods for creating matrices from various sources (arrays, ranges, random values)</li>
 *   <li>Matrix transformations (transpose, rotate, flip, reshape, extend)</li>
 *   <li>Mathematical operations (add, subtract, multiply, element-wise operations)</li>
 *   <li>Streaming support for rows, columns, and diagonals</li>
 *   <li>Diagonal operations (get, set, update main and anti-diagonals)</li>
 *   <li>Conversion methods to other matrix types (Long, Float, Double, boxed Integer)</li>
 * </ul>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Create matrices
 * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
 * IntMatrix range = IntMatrix.range(0, 10);
 *
 * // Transformations
 * IntMatrix transposed = matrix.transpose();
 * IntMatrix rotated = matrix.rotate90();
 *
 * // Operations
 * IntMatrix doubled = matrix.map(x -> x * 2);
 * int sum = matrix.streamH().sum();
 * }</pre>
 *
 * @see FloatMatrix
 * @see DoubleMatrix
 * @see LongMatrix
 * @see Matrix
 */
public final class IntMatrix extends AbstractMatrix<int[], IntList, IntStream, Stream<IntStream>, IntMatrix> {

    static final IntMatrix EMPTY_INT_MATRIX = new IntMatrix(new int[0][0]);

    /**
     * Constructs a IntMatrix from a two-dimensional int array.
     * If the input array is null, an empty matrix (0x0) is created.
     *
     * <p><b>Important:</b> The input array is used directly without defensive copying.
     * This means modifications to the input array after construction will affect the matrix,
     * and vice versa. For independent matrices, create a copy of the array before passing it.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] data = {{1, 2, 3}, {4, 5, 6}};
     * IntMatrix matrix = new IntMatrix(data);
     * data[0][0] = 99;  // This will also modify the matrix
     * }</pre>
     *
     * @param a the two-dimensional int array to wrap as a matrix. If null, an empty matrix is created.
     */
    public IntMatrix(final int[][] a) {
        super(a == null ? new int[0][0] : a);
    }

    /**
     * Creates an empty matrix with zero rows and zero columns.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.empty();
     * // matrix.rows returns 0
     * // matrix.cols returns 0
     * }</pre>
     *
     * @return an empty int matrix
     */
    public static IntMatrix empty() {
        return EMPTY_INT_MATRIX;
    }

    /**
     * Creates an IntMatrix from a two-dimensional int array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * // matrix.get(0, 1) returns 2
     * }</pre>
     *
     * @param a the two-dimensional int array to create the matrix from, or null/empty for an empty matrix
     * @return a new IntMatrix containing the provided data, or an empty IntMatrix if input is null or empty
     */
    public static IntMatrix of(final int[]... a) {
        return N.isEmpty(a) ? EMPTY_INT_MATRIX : new IntMatrix(a);
    }

    /**
     * Creates an IntMatrix from a two-dimensional char array by converting char values to int (using their ASCII/Unicode values).
     *
     * <p>All rows must have the same length as the first row (rectangular array required).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.create(new char[][] {{'A', 'B'}, {'C', 'D'}});
     * // Creates a matrix with ASCII values {{65, 66}, {67, 68}}
     * // matrix.get(0, 0) returns 65
     * }</pre>
     *
     * @param a the two-dimensional char array to convert to an int matrix, or null/empty for an empty matrix
     * @return a new IntMatrix with converted values, or an empty IntMatrix if input is null or empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths (non-rectangular array)
     */
    public static IntMatrix create(final char[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_INT_MATRIX;
        }

        N.checkArgument(a[0] != null, "First row cannot be null");

        final int cols = a[0].length;

        // Validate all rows have the same length
        for (int i = 1; i < a.length; i++) {
            N.checkArgument(a[i] != null && a[i].length == cols, "All rows must have the same length. Row 0 has length %s but row %s has length %s", cols, i,
                    a[i] == null ? 0 : a[i].length);
        }

        final int[][] c = new int[a.length][cols];

        for (int i = 0, len = a.length; i < len; i++) {
            final char[] aa = a[i];
            final int[] cc = c[i];

            for (int j = 0; j < cols; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new IntMatrix(c);
    }

    /**
     * Creates an IntMatrix from a two-dimensional byte array by converting byte values to int.
     *
     * <p>All rows must have the same length as the first row (rectangular array required).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.create(new byte[][] {{1, 2}, {3, 4}});
     * // Creates a matrix with values {{1, 2}, {3, 4}}
     * // matrix.get(1, 0) returns 3
     * }</pre>
     *
     * @param a the two-dimensional byte array to convert to an int matrix, or null/empty for an empty matrix
     * @return a new IntMatrix with converted values, or an empty IntMatrix if input is null or empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths (non-rectangular array)
     */
    public static IntMatrix create(final byte[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_INT_MATRIX;
        }

        N.checkArgument(a[0] != null, "First row cannot be null");

        final int cols = a[0].length;

        // Validate all rows have the same length
        for (int i = 1; i < a.length; i++) {
            N.checkArgument(a[i] != null && a[i].length == cols, "All rows must have the same length. Row 0 has length %s but row %s has length %s", cols, i,
                    a[i] == null ? 0 : a[i].length);
        }

        final int[][] c = new int[a.length][cols];

        for (int i = 0, len = a.length; i < len; i++) {
            final byte[] aa = a[i];
            final int[] cc = c[i];

            for (int j = 0; j < cols; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new IntMatrix(c);
    }

    /**
     * Creates an IntMatrix from a two-dimensional short array by converting short values to int.
     *
     * <p>All rows must have the same length as the first row (rectangular array required).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.create(new short[][] {{1, 2}, {3, 4}});
     * // Creates a matrix with values {{1, 2}, {3, 4}}
     * // matrix.get(1, 1) returns 4
     * }</pre>
     *
     * @param a the two-dimensional short array to convert to an int matrix, or null/empty for an empty matrix
     * @return a new IntMatrix with converted values, or an empty IntMatrix if input is null or empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths (non-rectangular array)
     */
    public static IntMatrix create(final short[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_INT_MATRIX;
        }

        N.checkArgument(a[0] != null, "First row cannot be null");

        final int cols = a[0].length;

        // Validate all rows have the same length
        for (int i = 1; i < a.length; i++) {
            N.checkArgument(a[i] != null && a[i].length == cols, "All rows must have the same length. Row 0 has length %s but row %s has length %s", cols, i,
                    a[i] == null ? 0 : a[i].length);
        }

        final int[][] c = new int[a.length][cols];

        for (int i = 0, len = a.length; i < len; i++) {
            final short[] aa = a[i];
            final int[] cc = c[i];

            for (int j = 0; j < cols; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new IntMatrix(c);
    }

    /**
     * Creates a 1-row matrix filled with random int values.
     *
     * <p>The random values are generated using the default random number generator
     * and will span the entire range of possible int values (Integer.MIN_VALUE to Integer.MAX_VALUE).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.random(5);
     * // Creates a 1x5 matrix like [[453, -8291, 9384, 1243, -4432]]
     * }</pre>
     *
     * @param len the number of columns (must be non-negative)
     * @return a new 1xlen IntMatrix filled with random int values, or an empty matrix if len is 0
     */
    @SuppressWarnings("deprecation")
    public static IntMatrix random(final int len) {
        return new IntMatrix(new int[][] { IntList.random(len).array() });
    }

    /**
     * Creates a 1-row matrix with all elements set to the specified value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.repeat(42, 5);
     * // Creates a 1x5 matrix [[42, 42, 42, 42, 42]]
     * }</pre>
     *
     * @param val the value to repeat in all positions
     * @param len the number of columns (must be non-negative)
     * @return a new 1xlen IntMatrix with all elements set to val, or an empty matrix if len is 0
     */
    public static IntMatrix repeat(final int val, final int len) {
        return new IntMatrix(new int[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a 1xN IntMatrix with values from startInclusive to endExclusive.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix range = IntMatrix.range(0, 5); // Creates [[0, 1, 2, 3, 4]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @return a new 1xN IntMatrix with sequential values
     */
    public static IntMatrix range(final int startInclusive, final int endExclusive) {
        return new IntMatrix(new int[][] { Array.range(startInclusive, endExclusive) });
    }

    /**
     * Creates a 1xN IntMatrix with values from startInclusive to endExclusive with specified step.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix range = IntMatrix.range(0, 10, 2); // Creates [[0, 2, 4, 6, 8]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endExclusive the ending value (exclusive)
     * @param by the step size
     * @return a new 1xN IntMatrix with sequential values
     */
    public static IntMatrix range(final int startInclusive, final int endExclusive, final int by) {
        return new IntMatrix(new int[][] { Array.range(startInclusive, endExclusive, by) });
    }

    /**
     * Creates a 1xN IntMatrix with values from startInclusive to endInclusive.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix range = IntMatrix.rangeClosed(0, 4); // Creates [[0, 1, 2, 3, 4]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive)
     * @return a new 1xN IntMatrix with sequential values
     */
    public static IntMatrix rangeClosed(final int startInclusive, final int endInclusive) {
        return new IntMatrix(new int[][] { Array.rangeClosed(startInclusive, endInclusive) });
    }

    /**
     * Creates a 1xN IntMatrix with values from startInclusive to endInclusive with specified step.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix range = IntMatrix.rangeClosed(0, 10, 2); // Creates [[0, 2, 4, 6, 8, 10]]
     * }</pre>
     * 
     * @param startInclusive the starting value (inclusive)
     * @param endInclusive the ending value (inclusive)
     * @param by the step size
     * @return a new 1xN IntMatrix with sequential values
     */
    public static IntMatrix rangeClosed(final int startInclusive, final int endInclusive, final int by) {
        return new IntMatrix(new int[][] { Array.rangeClosed(startInclusive, endInclusive, by) });
    }

    /**
     * Creates a square matrix from the specified main diagonal elements.
     * All other elements are set to zero.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.diagonalLU2RD(new int[] {1, 2, 3});
     * // Creates 3x3 matrix with diagonal [1, 2, 3] and zeros elsewhere
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of diagonal elements
     * @return a square matrix with the specified main diagonal
     */
    public static IntMatrix diagonalLU2RD(final int[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square matrix from the specified anti-diagonal elements.
     * All other elements are set to zero.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.diagonalRU2LD(new int[] {1, 2, 3});
     * // Creates 3x3 matrix with anti-diagonal [1, 2, 3] and zeros elsewhere
     * }</pre>
     *
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements
     * @return a square matrix with the specified anti-diagonal
     */
    public static IntMatrix diagonalRU2LD(final int[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a square matrix from the specified main diagonal and anti-diagonal elements.
     * All other elements are set to zero.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.diagonal(new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 });
     * // Creates 3x3 matrix with both diagonals set
     * // Resulting matrix: 
     * //   {1, 0, 4},
     * //   {0, 2, 0},
     * //   {6, 0, 3}
     *
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements
     * @return a square matrix with the specified diagonals
     * @throws IllegalArgumentException if arrays have different lengths
     */
    public static IntMatrix diagonal(final int[] leftUp2RightDownDiagonal, final int[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        if (N.isEmpty(leftUp2RightDownDiagonal) && N.isEmpty(rightUp2LeftDownDiagonal)) {
            return EMPTY_INT_MATRIX;
        }

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final int[][] c = new int[len][len];

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

        return new IntMatrix(c);
    }

    /**
     * Converts a boxed Integer Matrix to a primitive IntMatrix.
     * Null values in the input matrix are converted to 0.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<Integer> boxed = Matrix.of(new Integer[][] {{1, 2}, {3, 4}});
     * IntMatrix primitive = IntMatrix.unbox(boxed);
     * }</pre>
     * 
     * @param x the boxed Integer matrix to convert
     * @return a new IntMatrix with primitive int values
     */
    public static IntMatrix unbox(final Matrix<Integer> x) {
        return IntMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of the matrix elements, which is always {@code int.class}.
     * This method is useful for reflection-based code that needs to determine the element type.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Class componentType = matrix.componentType();
     * // componentType is int.class
     * assert componentType == int.class;
     * }</pre>
     *
     * @return {@code int.class}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return int.class;
    }

    /**
     * Returns the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * int value = matrix.get(0, 1); // Returns 2
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position (i, j)
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public int get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Returns the element at the specified point.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Point point = Point.of(0, 1);
     * int value = matrix.get(point); // Returns 2
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @return the int element at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #get(int, int)
     */
    public int get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * matrix.set(0, 1, 9); // Sets element at row 0, column 1 to 9
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public void set(final int i, final int j, final int val) {
        a[i][j] = val;
    }

    /**
     * Sets the element at the specified point to the given value.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Point point = Point.of(0, 1);
     * matrix.set(point, 9);
     * assert matrix.get(point) == 9;
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @param val the new int value to set at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #set(int, int, int)
     */
    public void set(final Point point, final int val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element above the specified position, if it exists.
     * This method provides safe access to the element directly above the given position
     * without throwing an exception when at the top edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * OptionalInt value = matrix.upOf(1, 0); // Returns OptionalInt.of(1)
     * OptionalInt empty = matrix.upOf(0, 0); // Returns OptionalInt.empty() - no row above
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalInt containing the element at position (i-1, j), or empty if i == 0
     * @throws ArrayIndexOutOfBoundsException if j is out of bounds
     */
    public OptionalInt upOf(final int i, final int j) {
        return i == 0 ? OptionalInt.empty() : OptionalInt.of(a[i - 1][j]);
    }

    /**
     * Returns the element below the specified position, if it exists.
     * This method provides safe access to the element directly below the given position
     * without throwing an exception when at the bottom edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * OptionalInt value = matrix.downOf(0, 0); // Returns OptionalInt.of(3)
     * OptionalInt empty = matrix.downOf(1, 0); // Returns OptionalInt.empty() - no row below
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalInt containing the element at position (i+1, j), or empty if i == rows-1
     * @throws ArrayIndexOutOfBoundsException if j is out of bounds
     */
    public OptionalInt downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalInt.empty() : OptionalInt.of(a[i + 1][j]);
    }

    /**
     * Returns the element to the left of the specified position, if it exists.
     * This method provides safe access to the element directly to the left of the given position
     * without throwing an exception when at the leftmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * OptionalInt value = matrix.leftOf(0, 1); // Returns OptionalInt.of(1)
     * OptionalInt empty = matrix.leftOf(0, 0); // Returns OptionalInt.empty() - no column to the left
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalInt containing the element at position (i, j-1), or empty if j == 0
     */
    public OptionalInt leftOf(final int i, final int j) {
        return j == 0 ? OptionalInt.empty() : OptionalInt.of(a[i][j - 1]);
    }

    /**
     * Returns the element to the right of the specified position, if it exists.
     * This method provides safe access to the element directly to the right of the given position
     * without throwing an exception when at the rightmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * OptionalInt value = matrix.rightOf(0, 0); // Returns OptionalInt.of(2)
     * OptionalInt empty = matrix.rightOf(0, 1); // Returns OptionalInt.empty() - no column to the right
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalInt containing the element at position (i, j+1), or empty if j == cols-1
     */
    public OptionalInt rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalInt.empty() : OptionalInt.of(a[i][j + 1]);
    }

    /**
     * Returns the specified row as an int array.
     *
     * <p><b>Note:</b> This method returns a reference to the internal array, not a copy.
     * Modifications to the returned array will affect the matrix. If you need an independent
     * copy, use {@code Arrays.copyOf(matrix.row(i), matrix.cols)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * int[] firstRow = matrix.row(0); // Returns [1, 2, 3]
     *
     * // Direct modification affects the matrix
     * firstRow[0] = 10; // matrix now has 10 at position (0,0)
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return the specified row array (direct reference to internal storage)
     * @throws IllegalArgumentException if rowIndex &lt; 0 or rowIndex &gt;= rows
     */
    public int[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as a new int array.
     *
     * <p>Unlike {@link #row(int)}, this method always returns a new array copy since
     * columns are not stored contiguously in memory. Modifications to the returned array
     * will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * int[] firstColumn = matrix.column(0); // Returns [1, 4]
     *
     * // Modification does NOT affect the matrix (it's a copy)
     * firstColumn[0] = 10; // matrix still has 1 at position (0,0)
     * }</pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing the values from the specified column
     * @throws IllegalArgumentException if columnIndex &lt; 0 or columnIndex &gt;= cols
     */
    public int[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final int[] c = new int[rows];

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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.setRow(0, new int[] {7, 8, 9}); // First row is now [7, 8, 9]
     * }</pre>
     *
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to set; must have length equal to number of columns
     * @throws IllegalArgumentException if row.length != cols
     * @throws ArrayIndexOutOfBoundsException if rowIndex is out of bounds or row is null
     */
    public void setRow(final int rowIndex, final int[] row) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Sets the values of the specified column.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.setColumn(0, new int[] {7, 8}); // First column is now [7, 8]
     * }</pre>
     *
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to set; must have length equal to number of rows
     * @throws IllegalArgumentException if column.length != rows
     * @throws ArrayIndexOutOfBoundsException if columnIndex is out of bounds or column is null
     */
    public void setColumn(final int columnIndex, final int[] column) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all elements in a row in-place by applying the specified function.
     * This modifies the matrix directly.
     *
     * <p>The function is applied to each element in the specified row sequentially
     * from left to right (column 0 to column cols-1).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.updateRow(0, x -> x * 2); // Doubles all values in the first row
     * // matrix is now [[2, 4, 6], [4, 5, 6]]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param rowIndex the index of the row to update (0-based)
     * @param func the function to apply to each element in the row; receives the current
     *             element value and returns the new value
     * @throws ArrayIndexOutOfBoundsException if rowIndex is out of bounds
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.IntUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsInt(a[rowIndex][i]);
        }
    }

    /**
     * Updates all elements in a column in-place by applying the specified function.
     * This modifies the matrix directly.
     *
     * <p>The function is applied to each element in the specified column sequentially
     * from top to bottom (row 0 to row rows-1).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * matrix.updateColumn(0, x -> x + 10); // Adds 10 to all values in the first column
     * // matrix is now [[11, 2], [13, 4], [15, 6]]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param columnIndex the index of the column to update (0-based)
     * @param func the function to apply to each element in the column; receives the current
     *             element value and returns the new value
     * @throws ArrayIndexOutOfBoundsException if columnIndex is out of bounds
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.IntUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsInt(a[i][columnIndex]);
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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * int[] diagonal = matrix.getLU2RD(); // Returns [1, 5, 9]
     * }</pre>
     *
     * @return a new int array containing a copy of the main diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public int[] getLU2RD() throws IllegalStateException {
        checkIfRowAndColumnSizeAreSame();

        final int[] res = new int[rows];

        for (int i = 0; i < rows; i++) {
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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * matrix.setLU2RD(new int[] {9, 8});
     * // Diagonal is now [9, 8]
     * }</pre>
     *
     * @param diagonal the new values for the main diagonal; must have length equal to rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length does not equal to rows
     */
    public void setLU2RD(final int[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length == rows, "The length of specified array does not equal to rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates the values on the main diagonal (left-up to right-down) by applying the specified function.
     * The matrix must be square.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.updateLU2RD(x -> x * x); // Squares all diagonal values
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each diagonal element
     * @throws IllegalStateException if the matrix is not square
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateLU2RD(final Throwables.IntUnaryOperator<E> func) throws IllegalStateException, E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsInt(a[i][i]);
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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * int[] diagonal = matrix.getRU2LD(); // Returns [3, 5, 7]
     * }</pre>
     *
     * @return a new int array containing a copy of the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public int[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final int[] res = new int[rows];

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
     * If the diagonal array is longer than needed, extra elements are ignored.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * matrix.setRU2LD(new int[] {9, 8});
     * // Anti-diagonal is now [9, 8]
     * }</pre>
     *
     * @param diagonal the new values for the anti-diagonal; must have length &gt;= rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length &lt; rows
     */
    public void setRU2LD(final int[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = diagonal[i];
        }
    }

    /**
     * Updates the values on the anti-diagonal (right-up to left-down) by applying the specified function.
     * The matrix must be square.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.updateRU2LD(x -> -x); // Negates all anti-diagonal values
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each anti-diagonal element
     * @throws IllegalStateException if the matrix is not square
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRU2LD(final Throwables.IntUnaryOperator<E> func) throws IllegalStateException, E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsInt(a[i][cols - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix in-place by applying the specified function.
     * This modifies the matrix directly.
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.
     * Elements are processed in row-major order when executed sequentially.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * matrix.updateAll(x -> x * 2); // Doubles all values in the matrix
     * // matrix is now [[2, 4], [6, 8]]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each element; receives the current element value
     *             and returns the new value
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.applyAsInt(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Updates all elements in the matrix in-place based on their position (row and column indices).
     * This modifies the matrix directly.
     *
     * <p>The function receives the row and column indices for each element and returns the new value
     * for that position. This is useful for initializing matrices based on position patterns or
     * mathematical formulas. The operation may be performed in parallel for large matrices.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{0, 0, 0}, {0, 0, 0}});
     * matrix.updateAll((i, j) -> i + j); // Sets each element to sum of its indices
     * // matrix is now [[0, 1, 2], [1, 2, 3]]
     *
     * matrix.updateAll((i, j) -> i * 10 + j); // Position encoding
     * // matrix is now [[0, 1, 2], [10, 11, 12]]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the function that receives row index and column index (0-based) and returns
     *             the new value for that position
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Integer, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{-1, 2, -3}, {4, -5, 6}});
     * matrix.replaceIf(x -> x < 0, 0); // Replaces all negative values with 0
     * // matrix is now [[0, 2, 0], [4, 0, 6]]
     *
     * matrix.replaceIf(x -> x % 2 == 0, 1); // Replaces all even values with 1
     * // matrix is now [[0, 1, 0], [1, 0, 1]]
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition to test each element; elements for which this returns
     *                  {@code true} will be replaced
     * @param newValue the value to use for replacing matching elements
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntPredicate<E> predicate, final int newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.replaceIf((i, j) -> i == j, 0); // Sets main diagonal elements to 0
     * // matrix is now [[0, 2, 3], [4, 0, 6], [7, 8, 0]]
     *
     * matrix.replaceIf((i, j) -> i == 0 || j == 0, -1); // Sets first row and column to -1
     * // matrix is now [[-1, -1, -1], [-1, 0, 6], [-1, 8, 0]]
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition that tests row index and column index (0-based); elements
     *                  at positions for which this returns {@code true} will be replaced
     * @param newValue the value to use for replacing matching elements
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final int newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new IntMatrix by applying a transformation function to each element.
     * The original matrix is not modified; a new matrix with transformed values is returned.
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.
     * This is the immutable counterpart to {@link #updateAll(Throwables.IntUnaryOperator)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix squared = matrix.map(x -> x * x); // Creates new matrix with squared values
     * // squared is [[1, 4], [9, 16]], original matrix unchanged
     *
     * IntMatrix negated = matrix.map(x -> -x); // Negate all values
     * // negated is [[-1, -2], [-3, -4]]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each element; receives the current element value
     *             and returns the transformed value
     * @return a new IntMatrix with transformed values
     * @throws E if the function throws an exception
     * @see #updateAll(Throwables.IntUnaryOperator)
     */
    public <E extends Exception> IntMatrix map(final Throwables.IntUnaryOperator<E> func) throws E {
        final int[][] result = new int[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsInt(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return IntMatrix.of(result);
    }

    /**
     * Creates a new LongMatrix by applying a function that converts int values to long.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix longMatrix = matrix.mapToLong(x -> (long) x * 1000000);
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function to convert int values to long
     * @return a new LongMatrix with converted values
     * @throws E if the function throws an exception
     */
    public <E extends Exception> LongMatrix mapToLong(final Throwables.IntToLongFunction<E> func) throws E {
        final long[][] result = new long[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsLong(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * Creates a new DoubleMatrix by applying a function that converts int values to double.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix doubleMatrix = matrix.mapToDouble(x -> x * 0.1);
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function to convert int values to double
     * @return a new DoubleMatrix with converted values
     * @throws E if the function throws an exception
     */
    public <E extends Exception> DoubleMatrix mapToDouble(final Throwables.IntToDoubleFunction<E> func) throws E {
        final double[][] result = new double[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsDouble(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * Creates a new Matrix by applying a function that converts int values to objects of type T.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<String> stringMatrix = matrix.mapToObj(x -> String.valueOf(x), String.class);
     * }</pre>
     * 
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the type of exception that the function may throw
     * @param func the function to convert int values to type T
     * @param targetElementType the Class object for type T
     * @return a new Matrix containing the converted values
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.IntFunction<? extends T, E> func, final Class<T> targetElementType) throws E {
        final T[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Fills the entire matrix with the specified value in-place.
     * This modifies the matrix directly, setting every element to the given value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.fill(0); // Sets all elements to 0
     * // matrix is now [[0, 0, 0], [0, 0, 0]]
     *
     * matrix.fill(42); // Sets all elements to 42
     * // matrix is now [[42, 42, 42], [42, 42, 42]]
     * }</pre>
     *
     * @param val the value to fill all matrix elements with
     */
    public void fill(final int val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills the matrix with values from another two-dimensional array, starting from the top-left corner.
     * If the source array is larger than the matrix, only the fitting portion is copied.
     * If the source array is smaller, only the available values are copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{0, 0, 0}, {0, 0, 0}});
     * int[][] patch = {{1, 2}, {3, 4}};
     * matrix.fill(patch); // Fills from (0,0): [[1, 2, 0], [3, 4, 0]]
     * }</pre>
     *
     * @param b the two-dimensional array to copy values from
     */
    public void fill(final int[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from another two-dimensional array.
     * The filling starts at the specified position and copies as much as will fit.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] patch = {{1, 2}, {3, 4}};
     * matrix.fill(1, 1, patch); // Fills starting at row 1, column 1
     * }</pre>
     * 
     * @param fromRowIndex the starting row index for filling
     * @param fromColumnIndex the starting column index for filling
     * @param b the source array to copy values from
     * @throws IllegalArgumentException if the starting indices are negative or exceed matrix dimensions
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final int[][] b) throws IllegalArgumentException {
        N.checkArgument(fromRowIndex >= 0 && fromRowIndex <= rows, "fromRowIndex(%s) must be between 0 and rows(%s)", fromRowIndex, rows);
        N.checkArgument(fromColumnIndex >= 0 && fromColumnIndex <= cols, "fromColumnIndex(%s) must be between 0 and cols(%s)", fromColumnIndex, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
        }
    }

    /**
     * Creates a copy of this matrix.
     * The returned matrix is a completely independent copy; modifications to one
     * do not affect the other.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix copy = original.copy();
     * copy.set(0, 0, 10); // Original matrix remains unchanged
     * }</pre>
     *
     * @return a new matrix that is a copy of this matrix
     */
    @Override
    public IntMatrix copy() {
        final int[][] c = new int[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new IntMatrix(c);
    }

    /**
     * Creates a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows and is completely independent from the original matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * IntMatrix subMatrix = matrix.copy(0, 2); // Contains rows 0 and 1
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new IntMatrix containing the specified rows
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @Override
    public IntMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final int[][] c = new int[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new IntMatrix(c);
    }

    /**
     * Creates a copy of a rectangular region from this matrix.
     * The returned matrix contains only the specified rows and columns and is completely
     * independent from the original matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntMatrix subMatrix = matrix.copy(0, 2, 1, 3); // Contains [[2, 3], [5, 6]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new IntMatrix containing the specified region
     * @throws IndexOutOfBoundsException if any index is out of bounds
     */
    @Override
    public IntMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final int[][] c = new int[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new IntMatrix(c);
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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix extended = matrix.extend(3, 3);
     * // Result: [[1, 2, 0],
     * //          [3, 4, 0],
     * //          [0, 0, 0]]
     * }</pre>
     *
     * @param newRows the number of rows in the new matrix. It can be smaller than the row number of the current matrix but must be non-negative
     * @param newCols the number of columns in the new matrix. It can be smaller than the column number of the current matrix but must be non-negative
     * @return a new IntMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRows} or {@code newCols} is negative
     */
    public IntMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, 0);
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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix extended = matrix.extend(3, 4, 9); // Extend to 3x4, fill new cells with 9
     * // Result: [[1, 2, 9, 9],
     * //          [3, 4, 9, 9],
     * //          [9, 9, 9, 9]]
     *
     * // Truncate to smaller size
     * IntMatrix truncated = matrix.extend(1, 1, 0); // Keep only top-left element
     * // Result: [[1]]
     * }</pre>
     *
     * @param newRows the number of rows in the new matrix. It can be smaller than the row number of the current matrix but must be non-negative
     * @param newCols the number of columns in the new matrix. It can be smaller than the column number of the current matrix but must be non-negative
     * @param defaultValueForNewCell the int value to fill new cells with during extension
     * @return a new IntMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRows} or {@code newCols} is negative,
     *         or if the resulting matrix would be too large (dimensions exceeding Integer.MAX_VALUE elements)
     */
    public IntMatrix extend(final int newRows, final int newCols, final int defaultValueForNewCell) throws IllegalArgumentException {
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
            final int[][] b = new int[newRows][];

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : new int[newCols];

                if (fillDefaultValue) {
                    if (i >= rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, defaultValueForNewCell);
                    }
                }
            }

            return new IntMatrix(b);
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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}});
     * IntMatrix extended = matrix.extend(1, 1, 1, 1);
     * // Result: [[0, 0, 0, 0],
     * //          [0, 1, 2, 0],
     * //          [0, 0, 0, 0]]
     * }</pre>
     *
     * @param toUp number of rows to add above; must be non-negative
     * @param toDown number of rows to add below; must be non-negative
     * @param toLeft number of columns to add to the left; must be non-negative
     * @param toRight number of columns to add to the right; must be non-negative
     * @return a new extended IntMatrix with dimensions ((toUp+rows+toDown) x (toLeft+cols+toRight))
     * @throws IllegalArgumentException if any parameter is negative
     */
    public IntMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, 0);
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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}});
     * IntMatrix padded = matrix.extend(1, 1, 2, 2, 9);
     * // Result: [[9, 9, 9, 9, 9, 9],
     * //          [9, 9, 1, 2, 9, 9],
     * //          [9, 9, 9, 9, 9, 9]]
     *
     * // Add border of 0 values
     * IntMatrix bordered = matrix.extend(1, 1, 1, 1, 0);
     * // Result: [[0, 0, 0, 0],
     * //          [0, 1, 2, 0],
     * //          [0, 0, 0, 0]]
     * }</pre>
     *
     * @param toUp number of rows to add above; must be non-negative
     * @param toDown number of rows to add below; must be non-negative
     * @param toLeft number of columns to add to the left; must be non-negative
     * @param toRight number of columns to add to the right; must be non-negative
     * @param defaultValueForNewCell the int value to fill all new cells with
     * @return a new extended IntMatrix with dimensions ((toUp+rows+toDown) x (toLeft+cols+toRight))
     * @throws IllegalArgumentException if any padding parameter is negative,
     *         or if the resulting dimensions would exceed Integer.MAX_VALUE
     */
    public IntMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final int defaultValueForNewCell)
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
            final int[][] b = new int[newRows][newCols];

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

            return new IntMatrix(b);
        }
    }

    /**
     * Reverses the order of elements in each row in-place (horizontal flip).
     * This modifies the current matrix; each row is reversed left-to-right.
     *
     * <p>This is an in-place operation that modifies the current matrix.
     * For a non-destructive version that returns a new matrix, use {@link #flipH()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.reverseH();
     * // matrix is now [[3, 2, 1], [6, 5, 4]]
     * }</pre>
     *
     * @see #flipH()
     * @see #reverseV()
     */
    public void reverseH() {
        for (int i = 0; i < rows; i++) {
            N.reverse(a[i]);
        }
    }

    /**
     * Reverses the order of rows in-place (vertical flip).
     * This modifies the current matrix; the order of rows is reversed top-to-bottom
     * while the order of elements within each row remains unchanged.
     *
     * <p>This is an in-place operation that modifies the current matrix.
     * For a non-destructive version that returns a new matrix, use {@link #flipV()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * matrix.reverseV();
     * // matrix is now [[5, 6], [3, 4], [1, 2]]
     * }</pre>
     *
     * @see #flipV()
     * @see #reverseH()
     */
    public void reverseV() {
        for (int j = 0; j < cols; j++) {
            int tmp = 0;
            for (int l = 0, h = rows - 1; l < h;) {
                tmp = a[l][j];
                a[l++][j] = a[h][j];
                a[h--][j] = tmp;
            }
        }
    }

    /**
     * Creates a horizontally flipped copy of this matrix.
     * Each row is reversed left-to-right (the leftmost element becomes rightmost).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix flipped = matrix.flipH();
     * // flipped is: [[3, 2, 1], [6, 5, 4]]
     * }</pre>
     *
     * @return a new IntMatrix with each row reversed
     * @see #flipV()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">MATLAB flip function</a>
     */
    public IntMatrix flipH() {
        final IntMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * Creates a vertically flipped copy of this matrix.
     * The rows are reversed top-to-bottom (the topmost row becomes bottommost).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix flipped = matrix.flipV();
     * // flipped is: [[4, 5, 6], [1, 2, 3]]
     * }</pre>
     *
     * @return a new IntMatrix with rows reversed
     * @see #flipH()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">MATLAB flip function</a>
     */
    public IntMatrix flipV() {
        final IntMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * Rotates this matrix 90 degrees clockwise.
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the last row of the original, reading upward.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 90° clockwise:
     * // 1 2 3        7 4 1
     * // 4 5 6   =>   8 5 2
     * // 7 8 9        9 6 3
     * }</pre>
     *
     * @return a new matrix rotated 90 degrees clockwise
     */
    @Override
    public IntMatrix rotate90() {
        final int[][] c = new int[cols][rows];

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

        return new IntMatrix(c);
    }

    /**
     * Rotates this matrix 180 degrees.
     * This is equivalent to flipping both horizontally and vertically, reversing the
     * order of all elements. The resulting matrix has the same dimensions as the original.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 180°:
     * // 1 2 3        9 8 7
     * // 4 5 6   =>   6 5 4
     * // 7 8 9        3 2 1
     * }</pre>
     *
     * @return a new matrix rotated 180 degrees
     */
    @Override
    public IntMatrix rotate180() {
        final int[][] c = new int[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new IntMatrix(c);
    }

    /**
     * Rotates this matrix 270 degrees clockwise (or 90 degrees counter-clockwise).
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the first row of the original, reading downward.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 270° clockwise:
     * // 1 2 3        3 6 9
     * // 4 5 6   =>   2 5 8
     * // 7 8 9        1 4 7
     * }</pre>
     *
     * @return a new matrix rotated 270 degrees clockwise
     */
    @Override
    public IntMatrix rotate270() {
        final int[][] c = new int[cols][rows];

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

        return new IntMatrix(c);
    }

    /**
     * Creates the transpose of this matrix by swapping rows and columns.
     * The transpose operation converts each row into a column, so element at position (i, j)
     * in the original matrix appears at position (j, i) in the transposed matrix. The resulting
     * matrix has dimensions swapped (rows × cols becomes cols × rows).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:  Transposed:
     * // 1 2 3      1 4 7
     * // 4 5 6      2 5 8
     * // 7 8 9      3 6 9
     *
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix transposed = matrix.transpose(); // 2×3 becomes 3×2
     * }</pre>
     *
     * @return a new matrix that is the transpose of this matrix with dimensions cols × rows
     */
    @Override
    public IntMatrix transpose() {
        final int[][] c = new int[cols][rows];

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

        return new IntMatrix(c);
    }

    /**
     * Reshapes this matrix to have the specified dimensions.
     * Elements are taken in row-major order from the original matrix and placed into the
     * new shape. If the new shape has fewer elements than the original, excess elements are
     * discarded. If the new shape has more elements, the extra positions are filled with
     * default values (0 for numeric types, false for boolean, null for objects).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped = matrix.reshape(3, 2); // Becomes [[1, 2], [3, 4], [5, 6]]
     * IntMatrix extended = matrix.reshape(2, 4); // Becomes [[1, 2, 3, 4], [5, 6, 0, 0]]
     * }</pre>
     *
     * @param newRows the number of rows in the reshaped matrix (must be non-negative)
     * @param newCols the number of columns in the reshaped matrix (must be non-negative)
     * @return a new IntMatrix with the specified dimensions
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public IntMatrix reshape(final int newRows, final int newCols) {
        final int[][] c = new int[newRows][newCols];

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new IntMatrix(c);
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

        return new IntMatrix(c);
    }

    /**
     * Repeats elements in both row and column directions.
     * Each element is repeated to form a block of size rowRepeats x colRepeats.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1,2}});
     * IntMatrix repeated = matrix.repelem(2, 3);
     * // Result: [[1,1,1,2,2,2],
     * //          [1,1,1,2,2,2]]
     * }</pre>
     * 
     * @param rowRepeats number of times to repeat each element in row direction
     * @param colRepeats number of times to repeat each element in column direction
     * @return a new IntMatrix with repeated elements
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repelem.html">MATLAB repelem function</a>
     */
    @Override
    public IntMatrix repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many rows: " + rows + " * " + rowRepeats);
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many columns: " + cols + " * " + colRepeats);
        }

        final int[][] c = new int[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            final int[] aa = a[i];
            final int[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                N.copy(Array.repeat(aa[j], colRepeats), 0, fr, j * colRepeats, colRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new IntMatrix(c);
    }

    /**
     * Repeats the entire matrix in a tiled pattern.
     * The matrix is repeated as a whole rowRepeats times vertically and colRepeats times horizontally.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1,2},{3,4}});
     * IntMatrix repeated = matrix.repmat(2, 3);
     * // Result: [[1,2,1,2,1,2],
     * //          [3,4,3,4,3,4],
     * //          [1,2,1,2,1,2],
     * //          [3,4,3,4,3,4]]
     * }</pre>
     * 
     * @param rowRepeats number of times to repeat the matrix vertically
     * @param colRepeats number of times to repeat the matrix horizontally
     * @return a new IntMatrix with the tiled pattern
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repmat.html">MATLAB repmat function</a>
     */
    @Override
    public IntMatrix repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many rows: " + rows + " * " + rowRepeats);
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many columns: " + cols + " * " + colRepeats);
        }

        final int[][] c = new int[rows * rowRepeats][cols * colRepeats];

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

        return new IntMatrix(c);
    }

    /**
     * Returns a list containing all matrix elements in row-major order.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntList list = matrix.flatten(); // Returns IntList of 1, 2, 3, 4
     * }</pre>
     *
     * @return a list of all elements in row-major order
     */
    @Override
    public IntList flatten() {
        // Check for overflow before allocation
        if ((long) rows * cols > Integer.MAX_VALUE) {
            throw new IllegalStateException("Matrix too large to flatten: " + rows + " x " + cols);
        }

        final int[] c = new int[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return IntList.of(c);
    }

    /**
     * Applies an operation to each row array of the matrix.
     * This method provides direct access to the internal row arrays for batch operations.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{3, 1, 2}, {6, 4, 5}});
     * matrix.flatOp(row -> java.util.Arrays.sort(row));
     * // Matrix becomes: [[1, 2, 3], [4, 5, 6]]
     * }</pre>
     *
     * @param <E> the type of exception that the operation may throw
     * @param op the operation to apply to each row array
     * @throws E if the operation throws an exception
     * @see Arrays#flatOp(int[][], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super int[], E> op) throws E {
        Arrays.flatOp(a, op);
    }

    /**
     * Stacks this matrix vertically with another matrix (vertical concatenation).
     * The matrices must have the same number of columns. The result has rows from this matrix
     * on top and rows from the other matrix below.
     *
     * <p>This operation is also known as vertical concatenation or rbind (bind by rows).
     * Creates a new matrix; the original matrices are not modified.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});     // 2x3
     * IntMatrix b = IntMatrix.of(new int[][] {{7, 8, 9}, {10, 11, 12}});  // 2x3
     * IntMatrix c = a.vstack(b);                                         // 4x3
     * // Result: [[1, 2, 3],
     * //          [4, 5, 6],
     * //          [7, 8, 9],
     * //          [10, 11, 12]]
     * }</pre>
     *
     * @param b the matrix to stack below this matrix (must have the same column count)
     * @return a new IntMatrix with dimensions (this.rows + b.rows) x this.cols
     * @throws IllegalArgumentException if {@code this.cols != b.cols}
     * @see #hstack(IntMatrix)
     */
    public IntMatrix vstack(final IntMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final int[][] c = new int[rows + b.rows][];
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
        }

        return IntMatrix.of(c);
    }

    /**
     * Stacks this matrix horizontally with another matrix (horizontal concatenation).
     * The matrices must have the same number of rows. The result has columns from this matrix
     * on the left and columns from the other matrix on the right.
     *
     * <p>This operation is also known as horizontal concatenation or cbind (bind by columns).
     * Creates a new matrix; the original matrices are not modified.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});     // 2x3
     * IntMatrix b = IntMatrix.of(new int[][] {{7, 8, 9}, {10, 11, 12}});  // 2x3
     * IntMatrix c = a.hstack(b);                                         // 2x6
     * // Result: [[1, 2, 3, 7, 8, 9],
     * //          [4, 5, 6, 10, 11, 12]]
     * }</pre>
     *
     * @param b the matrix to stack to the right of this matrix (must have the same row count)
     * @return a new IntMatrix with dimensions this.rows x (this.cols + b.cols)
     * @throws IllegalArgumentException if {@code this.rows != b.rows}
     * @see #vstack(IntMatrix)
     */
    public IntMatrix hstack(final IntMatrix b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final int[][] c = new int[rows][cols + b.cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c[i], 0, cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
        }

        return IntMatrix.of(c);
    }

    /**
     * Performs element-wise addition with another matrix.
     * The matrices must have the same dimensions.
     * <p><b>Note:</b> Integer overflow may occur during addition.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][] {{1,2},{3,4}});
     * IntMatrix b = IntMatrix.of(new int[][] {{5,6},{7,8}});
     * IntMatrix sum = a.add(b); // Result: [[6,8],[10,12]]
     * }</pre>
     *
     * @param b the matrix to add to this matrix
     * @return a new IntMatrix containing the element-wise sum
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public IntMatrix add(final IntMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't add Matrixes with different shape");

        final int[][] ba = b.a;
        final int[][] result = new int[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (a[i][j] + ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return IntMatrix.of(result);
    }

    /**
     * Performs element-wise subtraction with another matrix.
     * The matrices must have the same dimensions.
     * <p><b>Note:</b> Integer overflow may occur during subtraction.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][] {{5,6},{7,8}});
     * IntMatrix b = IntMatrix.of(new int[][] {{1,2},{3,4}});
     * IntMatrix diff = a.subtract(b); // Result: [[4,4],[4,4]]
     * }</pre>
     * 
     * @param b the matrix to subtract from this matrix
     * @return a new IntMatrix containing the element-wise difference
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public IntMatrix subtract(final IntMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't subtract Matrixes with different shape");

        final int[][] ba = b.a;
        final int[][] result = new int[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (a[i][j] - ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return IntMatrix.of(result);
    }

    /**
     * Performs matrix multiplication with another matrix.
     * The number of columns in this matrix must equal the number of rows in the other matrix.
     * <p><b>Note:</b> Integer overflow may occur during multiplication.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][] {{1,2},{3,4}});
     * IntMatrix b = IntMatrix.of(new int[][] {{5,6},{7,8}});
     * IntMatrix product = a.multiply(b); // Result: [[19,22],[43,50]]
     * }</pre>
     *
     * @param b the matrix to multiply with
     * @return a new IntMatrix containing the matrix product
     * @throws IllegalArgumentException if the matrix dimensions are incompatible for multiplication
     */
    public IntMatrix multiply(final IntMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.rows, "Illegal matrix dimensions");

        final int[][] ba = b.a;
        final int[][] result = new int[rows][b.cols];
        final Throwables.IntTriConsumer<RuntimeException> cmd = (i, j, k) -> result[i][j] += a[i][k] * ba[k][j];

        Matrixes.multiply(this, b, cmd);

        return new IntMatrix(result);
    }

    /**
     * Converts this primitive int matrix to a boxed Integer matrix.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix primitive = IntMatrix.of(new int[][] {{1, 2}});
     * Matrix<Integer> boxed = primitive.boxed();
     * }</pre>
     * 
     * @return a new Matrix containing boxed Integer values
     */
    public Matrix<Integer> boxed() {
        final Integer[][] c = new Integer[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final int[] aa = a[i];
                final Integer[] cc = c[i];

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
     * Converts this int matrix to a long matrix.
     * Each int value is converted to long.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix intMatrix = IntMatrix.of(new int[][] {{1, 2}});
     * LongMatrix longMatrix = intMatrix.toLongMatrix();
     * }</pre>
     * 
     * @return a new LongMatrix with converted values
     */
    public LongMatrix toLongMatrix() {
        return LongMatrix.create(a);
    }

    /**
     * Converts this int matrix to a float matrix.
     * Each int value is converted to float.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix intMatrix = IntMatrix.of(new int[][] {{1, 2}});
     * FloatMatrix floatMatrix = intMatrix.toFloatMatrix();
     * }</pre>
     * 
     * @return a new FloatMatrix with converted values
     */
    public FloatMatrix toFloatMatrix() {
        return FloatMatrix.create(a);
    }

    /**
     * Converts this int matrix to a double matrix.
     * Each int value is converted to double.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix intMatrix = IntMatrix.of(new int[][] {{1, 2}});
     * DoubleMatrix doubleMatrix = intMatrix.toDoubleMatrix();
     * }</pre>
     * 
     * @return a new DoubleMatrix with converted values
     */
    public DoubleMatrix toDoubleMatrix() {
        return DoubleMatrix.create(a);
    }

    /**
     * Performs element-wise operation on two matrices using a binary operator.
     * The matrices must have the same dimensions. Corresponding elements from both matrices
     * are combined using the provided function to produce the result matrix.
     *
     * <p>This is a generalized element-wise operation. For specific operations like addition,
     * subtraction, or multiplication, consider using the dedicated methods {@link #add(IntMatrix)},
     * {@link #subtract(IntMatrix)}, or {@link #multiply(IntMatrix)}.</p>
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.
     * Creates a new matrix; the original matrices are not modified.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix b = IntMatrix.of(new int[][] {{5, 6}, {7, 8}});
     *
     * IntMatrix product = a.zipWith(b, (x, y) -> x * y); // Element-wise multiplication
     * // product is [[5, 12], [21, 32]]
     *
     * IntMatrix max = a.zipWith(b, Math::max); // Element-wise maximum
     * // max is [[5, 6], [7, 8]]
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix (must have the same dimensions as this matrix)
     * @param zipFunction the binary operator to apply to corresponding elements; receives
     *                    element from this matrix as first argument and element from matrixB
     *                    as second argument
     * @return a new IntMatrix with the results of the element-wise operation
     * @throws IllegalArgumentException if the matrices have different dimensions (shape mismatch)
     * @throws E if the zip function throws an exception
     * @see #zipWith(IntMatrix, IntMatrix, Throwables.IntTernaryOperator)
     */
    public <E extends Exception> IntMatrix zipWith(final IntMatrix matrixB, final Throwables.IntBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Can't zip two or more matrices which don't have same shape");

        final int[][] b = matrixB.a;
        final int[][] result = new int[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsInt(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return IntMatrix.of(result);
    }

    /**
     * Performs element-wise operation on three matrices using a ternary operator.
     * All matrices must have the same dimensions. Corresponding elements from all three matrices
     * are combined using the provided function to produce the result matrix.
     *
     * <p>This is useful for operations that combine three matrices, such as weighted averages,
     * conditional selection, or mathematical formulas involving three variables.</p>
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.
     * Creates a new matrix; the original matrices are not modified.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix b = IntMatrix.of(new int[][] {{5, 6}, {7, 8}});
     * IntMatrix c = IntMatrix.of(new int[][] {{9, 10}, {11, 12}});
     *
     * IntMatrix sum = a.zipWith(b, c, (x, y, z) -> x + y + z); // Sum three matrices
     * // sum is [[15, 18], [21, 24]]
     *
     * IntMatrix weighted = a.zipWith(b, c, (x, y, z) -> x * 2 + y * 3 + z * 5);
     * // weighted is [[62, 74], [86, 98]]
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix (must have the same dimensions as this matrix)
     * @param matrixC the third matrix (must have the same dimensions as this matrix)
     * @param zipFunction the ternary operator to apply to corresponding elements; receives
     *                    element from this matrix as first argument, element from matrixB as
     *                    second argument, and element from matrixC as third argument
     * @return a new IntMatrix with the results of the element-wise operation
     * @throws IllegalArgumentException if any matrices have different dimensions (shape mismatch)
     * @throws E if the zip function throws an exception
     * @see #zipWith(IntMatrix, Throwables.IntBinaryOperator)
     */
    public <E extends Exception> IntMatrix zipWith(final IntMatrix matrixB, final IntMatrix matrixC, final Throwables.IntTernaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Can't zip two or more matrices which don't have same shape");

        final int[][] b = matrixB.a;
        final int[][] c = matrixC.a;
        final int[][] result = new int[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsInt(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return IntMatrix.of(result);
    }

    /**
     * Returns a stream of elements on the main diagonal (left-up to right-down).
     * The matrix must be square.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1,2,3},{4,5,6},{7,8,9}});
     * IntStream diagonal = matrix.streamLU2RD(); // Stream of [1, 5, 9]
     * }</pre>
     * 
     * @return an IntStream of diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public IntStream streamLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return IntStream.empty();
        }

        return IntStream.of(new IntIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public int nextInt() {
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
     * Returns a stream of elements on the anti-diagonal (right-up to left-down).
     * The matrix must be square.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1,2,3},{4,5,6},{7,8,9}});
     * IntStream antiDiagonal = matrix.streamRU2LD(); // Stream of [3, 5, 7]
     * }</pre>
     * 
     * @return an IntStream of anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public IntStream streamRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return IntStream.empty();
        }

        return IntStream.of(new IntIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public int nextInt() {
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
     * Returns a stream of all elements in row-major order (horizontal).
     * Elements are streamed row by row from left to right, starting from the
     * top-left corner and proceeding to the bottom-right corner.
     * 
     * <p>This method is useful for processing all matrix elements sequentially
     * without concern for their row/column positions. The stream supports all
     * standard IntStream operations including sum, average, filter, map, etc.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntStream stream = matrix.streamH(); // Stream of [1, 2, 3, 4]
     * int sum = matrix.streamH().sum(); // Returns 10
     * int[] array = matrix.streamH().toArray(); // Returns [1, 2, 3, 4]
     * }</pre>
     * 
     * @return an IntStream of all elements in row-major order, or an empty stream if the matrix is empty
     */
    @Override
    public IntStream streamH() {
        return streamH(0, rows);
    }

    /**
     * Returns a stream of elements from a single row.
     * The elements are streamed from left to right within the specified row.
     * 
     * <p>This method is particularly useful when you need to process or analyze
     * a specific row of the matrix independently. The returned stream can be
     * used with all standard IntStream operations.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream rowStream = matrix.streamH(0); // Stream of [1, 2, 3]
     * int rowSum = matrix.streamH(1).sum(); // Returns 15 (sum of second row)
     * int[] firstRow = matrix.streamH(0).toArray(); // Returns [1, 2, 3]
     * }</pre>
     * 
     * @param rowIndex the index of the row to stream (0-based)
     * @return an IntStream of elements from the specified row
     * @throws IndexOutOfBoundsException if rowIndex &lt; 0 or rowIndex &gt;= rows
     */
    @Override
    public IntStream streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * IntStream stream = matrix.streamH(1, 3); // Stream rows 1 and 2: [3, 4, 5, 6]
     * int[] subset = matrix.streamH(0, 2).toArray(); // Returns [1, 2, 3, 4]
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return an IntStream of elements from the specified row range, or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows, or fromRowIndex &gt; toRowIndex
     */
    @Override
    public IntStream streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return IntStream.empty();
        }

        return IntStream.of(new IntIteratorEx() {
            private int i = fromRowIndex;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i < toRowIndex;
            }

            @Override
            public int nextInt() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final int result = a[i][j++];

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
            public int[] toArray() {
                final int len = (int) count();
                final int[] c = new int[len];

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
     * Returns a stream of all elements in column-major order (vertical).
     * Elements are streamed column by column from top to bottom, starting from
     * the leftmost column and proceeding to the rightmost column.
     * 
     * <p>This method is marked as @Beta, indicating it may be subject to change
     * in future versions. It provides an alternative way to iterate through matrix
     * elements compared to the row-major order of streamH().</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntStream stream = matrix.streamV(); // Stream of [1, 3, 2, 4]
     * int[] colMajor = matrix.streamV().toArray(); // Returns [1, 3, 2, 4]
     * }</pre>
     * 
     * @return an IntStream of all elements in column-major order, or an empty stream if the matrix is empty
     */
    @Override
    @Beta
    public IntStream streamV() {
        return streamV(0, cols);
    }

    /**
     * Returns a stream of elements from a single column.
     * The elements are streamed from top to bottom within the specified column.
     * 
     * <p>This method is useful for column-wise operations such as calculating
     * column sums, finding column maximums, or filtering column values.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream colStream = matrix.streamV(1); // Stream of [2, 5]
     * int colSum = matrix.streamV(0).sum(); // Returns 5 (sum of first column)
     * int[] secondCol = matrix.streamV(1).toArray(); // Returns [2, 5]
     * }</pre>
     * 
     * @param columnIndex the index of the column to stream (0-based)
     * @return an IntStream of elements from the specified column
     * @throws IndexOutOfBoundsException if columnIndex &lt; 0 or columnIndex &gt;= cols
     */
    @Override
    public IntStream streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * Returns a stream of elements from a range of columns in column-major order.
     * Elements are streamed column by column from the starting column (inclusive)
     * to the ending column (exclusive), with each column streamed from top to bottom.
     * 
     * <p>This method is marked as @Beta and allows for efficient processing of a
     * subset of matrix columns in column-major order.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream stream = matrix.streamV(1, 3); // Stream columns 1 and 2: [2, 5, 3, 6]
     * int[] subset = matrix.streamV(0, 2).toArray(); // Returns [1, 4, 2, 5]
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return an IntStream of elements from the specified column range in column-major order,
     *         or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; cols,
     *         or fromColumnIndex &gt; toColumnIndex
     */
    @Override
    @Beta
    public IntStream streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return IntStream.empty();
        }

        return IntStream.of(new IntIteratorEx() {
            private int i = 0;
            private int j = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return j < toColumnIndex;
            }

            @Override
            public int nextInt() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final int result = a[i++][j];

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

                if (n >= (long) (toColumnIndex - j) * IntMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    final int offset = (int) (n + i);
                    i = offset % IntMatrix.this.rows;
                    j += offset / IntMatrix.this.rows;
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public int[] toArray() {
                final int len = (int) count();
                final int[] c = new int[len];

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
     * Returns a stream of IntStream objects, where each IntStream represents a complete row.
     * This creates a stream of streams, allowing for row-by-row processing of the matrix.
     * 
     * <p>This method is useful for operations that need to process entire rows as units,
     * such as row-wise transformations, filtering rows based on conditions, or mapping
     * rows to other values.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * Stream<IntStream> rows = matrix.streamR();
     * int[] rowSums = matrix.streamR()
     *     .mapToInt(row -> row.sum())
     *     .toArray(); // Returns [3, 7, 11]
     * }</pre>
     * 
     * @return a Stream of IntStream objects, one for each row in the matrix,
     *         or an empty stream if the matrix is empty
     */
    @Override
    public Stream<IntStream> streamR() {
        return streamR(0, rows);
    }

    /**
     * Returns a stream of IntStream objects for a range of rows.
     * Each IntStream in the result represents a complete row within the specified range.
     * 
     * <p>This method allows for processing a subset of rows while maintaining the
     * ability to work with complete rows as individual streams.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * Stream<IntStream> middleRows = matrix.streamR(1, 3); // Rows 1 and 2
     * List<Integer> maxValues = matrix.streamR(0, 2)
     *     .map(row -> row.max().orElse(0))
     *     .collect(Collectors.toList()); // [2, 4]
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a Stream of IntStream objects for the specified row range,
     *         or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows,
     *         or fromRowIndex &gt; toRowIndex
     */
    @Override
    public Stream<IntStream> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
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
            public IntStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return IntStream.of(a[cursor++]);
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
     * Returns a stream of IntStream objects, where each IntStream represents a complete column.
     * This creates a stream of streams, allowing for column-by-column processing of the matrix.
     * 
     * <p>This method is marked as @Beta and is useful for operations that need to process
     * entire columns as units, such as column-wise statistics, transformations, or filtering
     * columns based on conditions.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * Stream<IntStream> columns = matrix.streamC();
     * int[] colSums = matrix.streamC()
     *     .mapToInt(col -> col.sum())
     *     .toArray(); // Returns [5, 7, 9]
     * }</pre>
     * 
     * @return a Stream of IntStream objects, one for each column in the matrix,
     *         or an empty stream if the matrix is empty
     */
    @Override
    @Beta
    public Stream<IntStream> streamC() {
        return streamC(0, cols);
    }

    /**
     * Returns a stream of IntStream objects for a range of columns.
     * Each IntStream in the result represents a complete column within the specified range.
     * 
     * <p>This method is marked as @Beta and allows for processing a subset of columns
     * while maintaining the ability to work with complete columns as individual streams.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * Stream<IntStream> lastTwoCols = matrix.streamC(1, 3); // Columns 1 and 2
     * List<Double> avgValues = matrix.streamC(0, 2)
     *     .map(col -> col.average().orElse(0.0))
     *     .collect(Collectors.toList()); // [2.5, 3.5]
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a Stream of IntStream objects for the specified column range,
     *         or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; cols,
     *         or fromColumnIndex &gt; toColumnIndex
     */
    @Override
    @Beta
    public Stream<IntStream> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public IntStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return IntStream.of(new IntIteratorEx() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rows;
                    private int cursor2 = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor2 < toIndex2;
                    }

                    @Override
                    public int nextInt() {
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
     * Returns the length of the given array.
     * This is a utility method used internally by the abstract parent class
     * to determine the column count of a row.
     *
     * @param a the array (row) to measure, may be null
     * @return the length of the array, or 0 if the array is null
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final int[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * Applies the given action to each element in the matrix.
     * Elements are processed in row-major order (row by row, left to right) when executed sequentially.
     *
     * <p>The operation may be parallelized internally for large matrices to improve performance,
     * based on internal heuristics. If parallelized, the order of execution is not guaranteed,
     * but all elements will be processed exactly once.</p>
     *
     * <p><b>Note:</b> This method is for side-effect operations only (like printing, collecting,
     * or accumulating). For transformations that create new matrices, use {@link #map(Throwables.IntUnaryOperator)}
     * or {@link #updateAll(Throwables.IntUnaryOperator)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     *
     * // Collect all values
     * List<Integer> values = new ArrayList<>();
     * matrix.forEach(value -> values.add(value));
     * // values now contains [1, 2, 3, 4]
     *
     * // Calculate sum using forEach (though streamH().sum() is preferable)
     * int[] sum = {0};
     * matrix.forEach(value -> sum[0] += value);
     * // sum[0] is now 10
     *
     * // Print all positive values
     * matrix.forEach(value -> {
     *     if (value > 0) System.out.println(value);
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the action to be performed for each element; receives each element value
     * @throws E if the action throws an exception
     * @see #forEach(int, int, int, int, Throwables.IntConsumer)
     */
    public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws E {
        forEach(0, rows, 0, cols, action);
    }

    /**
     * Applies the given action to each element in the specified sub-matrix region.
     * Elements are processed in row-major order within the specified bounds.
     * 
     * <p>This method allows for processing a rectangular subset of the matrix.
     * The operation may be parallelized internally if the sub-matrix is large enough
     * to benefit from parallel processing.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * 
     * // Process only the center element
     * matrix.forEach(1, 2, 1, 2, value -> System.out.println(value)); // Prints: 5
     * 
     * // Process a 2x2 sub-matrix
     * List<Integer> subMatrix = new ArrayList<>();
     * matrix.forEach(0, 2, 1, 3, value -> subMatrix.add(value));
     * // subMatrix contains [2, 3, 5, 6]
     * }</pre>
     * 
     * @param <E> the type of exception that the action may throw
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to be performed for each element in the sub-matrix
     * @throws IndexOutOfBoundsException if any index is out of bounds
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final int[] aa = a[i];

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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
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

                    final int[] row = a[i];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix1 = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix matrix2 = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * // Same content yields same hash code
     * int hash1 = matrix1.hashCode();
     * int hash2 = matrix2.hashCode();
     * assert hash1 == hash2;
     * }</pre>
     *
     * @return a hash code value for this matrix
     */
    @Override
    public int hashCode() {
        return N.deepHashCode(a);
    }

    /**
     * Compares this matrix to the specified object for equality.
     * Returns {@code true} if the given object is also an IntMatrix with the same dimensions
     * and all corresponding elements are equal.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix m2 = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
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

        if (obj instanceof final IntMatrix another) {
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
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
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
