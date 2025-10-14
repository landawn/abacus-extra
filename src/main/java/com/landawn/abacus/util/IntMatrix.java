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
 * <p>The matrix is stored internally as a 2D int array (int[][]) and provides
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
 * <p>Example usage:</p>
 * <pre>{@code
 * // Create matrices
 * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
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
     * Constructs an IntMatrix from a 2D int array.
     * If the input array is null, an empty matrix (0x0) is created.
     * 
     * @param a the 2D int array to wrap in a matrix. Can be null.
     */
    public IntMatrix(final int[][] a) {
        super(a == null ? new int[0][0] : a);
    }

    /**
     * Returns an empty IntMatrix with dimensions 0x0.
     * This method returns a singleton empty matrix instance for memory efficiency.
     * 
     * @return an empty IntMatrix instance
     */
    public static IntMatrix empty() {
        return EMPTY_INT_MATRIX;
    }

    /**
     * Creates an IntMatrix from a 2D int array.
     * This is a factory method that provides a more readable way to create matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * }</pre>
     * 
     * @param a the 2D int array to create the matrix from
     * @return a new IntMatrix containing the provided data, or empty matrix if input is null/empty
     */
    public static IntMatrix of(final int[]... a) {
        return N.isEmpty(a) ? EMPTY_INT_MATRIX : new IntMatrix(a);
    }

    /**
     * Creates an IntMatrix from a 2D char array by converting char values to int.
     * All rows must have the same length as the first row.
     *
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.create(new char[][]{{'A', 'B'}, {'C', 'D'}});
     * // Creates a matrix with ASCII values {{65, 66}, {67, 68}}
     * }</pre>
     *
     * @param a the 2D char array to convert to an int matrix
     * @return a new IntMatrix with converted values, or empty matrix if input is null/empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths
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
     * Creates an IntMatrix from a 2D byte array by converting byte values to int.
     * All rows must have the same length as the first row.
     *
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.create(new byte[][]{{1, 2}, {3, 4}});
     * // Creates a matrix with values {{1, 2}, {3, 4}}
     * }</pre>
     *
     * @param a the 2D byte array to convert to an int matrix
     * @return a new IntMatrix with converted values, or empty matrix if input is null/empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths
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
     * Creates an IntMatrix from a 2D short array by converting short values to int.
     * All rows must have the same length as the first row.
     *
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.create(new short[][]{{1, 2}, {3, 4}});
     * // Creates a matrix with values {{1, 2}, {3, 4}}
     * }</pre>
     *
     * @param a the 2D short array to convert to an int matrix
     * @return a new IntMatrix with converted values, or empty matrix if input is null/empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths
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
     * Creates a 1xN IntMatrix with random int values.
     * The random values are generated within the full range of int values.
     *
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix randomRow = IntMatrix.random(5); // Creates 1x5 matrix with random values
     * }</pre>
     *
     * @param len the number of columns (length) of the resulting 1-row matrix
     * @return a new 1xN IntMatrix with random int values
     */
    @SuppressWarnings("deprecation")
    public static IntMatrix random(final int len) {
        return new IntMatrix(new int[][] { IntList.random(len).array() });
    }

    /**
     * Creates a 1xN IntMatrix where all elements have the same value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.repeat(42, 5); // Creates [[42, 42, 42, 42, 42]]
     * }</pre>
     * 
     * @param val the value to repeat
     * @param len the number of columns (length) of the resulting 1-row matrix
     * @return a new 1xN IntMatrix with all elements set to val
     */
    public static IntMatrix repeat(final int val, final int len) {
        return new IntMatrix(new int[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a 1xN IntMatrix with values from startInclusive to endExclusive.
     * 
     * <p>Example:</p>
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
     * <p>Example:</p>
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
     * <p>Example:</p>
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
     * <p>Example:</p>
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
     * Creates a diagonal matrix with the specified values on the main diagonal (left-up to right-down).
     * All other elements are set to 0.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix diagonal = IntMatrix.diagonalLU2RD(new int[]{1, 2, 3});
     * // Creates: [[1, 0, 0],
     * //           [0, 2, 0],
     * //           [0, 0, 3]]
     * }</pre>
     * 
     * @param leftUp2RightDownDiagonal the values for the main diagonal
     * @return a new square IntMatrix with the specified diagonal values
     */
    public static IntMatrix diagonalLU2RD(final int[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a diagonal matrix with the specified values on the anti-diagonal (right-up to left-down).
     * All other elements are set to 0.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix diagonal = IntMatrix.diagonalRU2LD(new int[]{1, 2, 3});
     * // Creates: [[0, 0, 1],
     * //           [0, 2, 0],
     * //           [3, 0, 0]]
     * }</pre>
     * 
     * @param rightUp2LeftDownDiagonal the values for the anti-diagonal
     * @return a new square IntMatrix with the specified anti-diagonal values
     */
    public static IntMatrix diagonalRU2LD(final int[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a diagonal matrix with values on both the main diagonal and anti-diagonal.
     * The matrix size is determined by the length of the diagonal arrays.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.diagonal(new int[]{1, 2, 3}, new int[]{4, 5, 6});
     * // Creates: [[1, 0, 4],
     * //           [0, 2, 0],
     * //           [6, 0, 3]]
     * }</pre>
     * 
     * @param leftUp2RightDownDiagonal the values for the main diagonal (can be null)
     * @param rightUp2LeftDownDiagonal the values for the anti-diagonal (can be null)
     * @return a new square IntMatrix with the specified diagonal values
     * @throws IllegalArgumentException if both arrays are non-null and have different lengths
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
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> boxed = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
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
     * Returns the component type of the matrix elements, which is int.class.
     * 
     * @return int.class
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return int.class;
    }

    /**
     * Gets the element at the specified row and column.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * int value = matrix.get(0, 1); // Returns 2
     * }</pre>
     * 
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position [i][j]
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public int get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Gets the element at the specified point.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Point p = Point.of(0, 1);
     * int value = matrix.get(p); // Gets element at row 0, column 1
     * }</pre>
     * 
     * @param point the point specifying row and column indices
     * @return the element at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public int get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.set(0, 1, 5); // Sets element at row 0, column 1 to 5
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
     * Sets the element at the specified point.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Point p = Point.of(0, 1);
     * matrix.set(p, 5); // Sets element at row 0, column 1 to 5
     * }</pre>
     * 
     * @param point the point specifying row and column indices
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public void set(final Point point, final int val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element above the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalInt above = matrix.upOf(1, 0); // Gets element at row 0, column 0
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalInt containing the element above, or empty if at top row
     */
    public OptionalInt upOf(final int i, final int j) {
        return i == 0 ? OptionalInt.empty() : OptionalInt.of(a[i - 1][j]);
    }

    /**
     * Returns the element below the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalInt below = matrix.downOf(0, 0); // Gets element at row 1, column 0
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalInt containing the element below, or empty if at bottom row
     */
    public OptionalInt downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalInt.empty() : OptionalInt.of(a[i + 1][j]);
    }

    /**
     * Returns the element to the left of the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalInt left = matrix.leftOf(0, 1); // Gets element at row 0, column 0
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalInt containing the element to the left, or empty if at leftmost column
     */
    public OptionalInt leftOf(final int i, final int j) {
        return j == 0 ? OptionalInt.empty() : OptionalInt.of(a[i][j - 1]);
    }

    /**
     * Returns the element to the right of the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalInt right = matrix.rightOf(0, 0); // Gets element at row 0, column 1
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalInt containing the element to the right, or empty if at rightmost column
     */
    public OptionalInt rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalInt.empty() : OptionalInt.of(a[i][j + 1]);
    }

    /**
     * Returns a stream of the four adjacent points (up, right, down, left) of the specified position.
     * Points that would be outside the matrix bounds are returned as null.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Stream<Point> adjacents = matrix.adjacent4Points(1, 1);
     * // Returns points for positions: up(0,1), right(1,2), down(2,1), left(1,0)
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return a stream of adjacent points in order: up, right, down, left (null for out-of-bounds)
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
     * <p>Example:</p>
     * <pre>{@code
     * Stream<Point> adjacents = matrix.adjacent8Points(1, 1);
     * // Returns all 8 surrounding points (diagonals included)
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return a stream of 8 adjacent points (null for out-of-bounds positions)
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
     * Returns the specified row as an int array.
     *
     * <p><b>Note:</b> The returned array is the actual internal array, not a copy.
     * Modifications to the returned array will affect the matrix. Use {@code row(rowIndex).clone()}
     * if you need an independent copy.</p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * int[] firstRow = matrix.row(0); // Gets the first row
     * int[] rowCopy = matrix.row(0).clone(); // Gets a copy of the first row
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return the internal array representing the specified row
     * @throws IllegalArgumentException if rowIndex is out of bounds
     */
    public int[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as an int array.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * int[] firstColumn = matrix.column(0); // Gets a copy of the first column
     * }</pre>
     * 
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing the values from the specified column
     * @throws IllegalArgumentException if columnIndex is out of bounds
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
     * Sets an entire row with the values from the provided array.
     * The array length must match the number of columns in the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.setRow(0, new int[]{1, 2, 3}); // Sets the first row
     * }</pre>
     * 
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to set in the row
     * @throws IllegalArgumentException if row length doesn't match matrix columns or index is out of bounds
     */
    public void setRow(final int rowIndex, final int[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Sets an entire column with the values from the provided array.
     * The array length must match the number of rows in the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.setColumn(0, new int[]{1, 2, 3}); // Sets the first column
     * }</pre>
     * 
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to set in the column
     * @throws IllegalArgumentException if column length doesn't match matrix rows or index is out of bounds
     */
    public void setColumn(final int columnIndex, final int[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all elements in a row by applying the specified function.
     *
     * <p>Example:</p>
     * <pre>{@code
     * matrix.updateRow(0, x -> x * 2); // Doubles all values in the first row
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param rowIndex the index of the row to update (0-based)
     * @param func the function to apply to each element in the row
     * @throws ArrayIndexOutOfBoundsException if rowIndex is out of bounds
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.IntUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsInt(a[rowIndex][i]);
        }
    }

    /**
     * Updates all elements in a column by applying the specified function.
     *
     * <p>Example:</p>
     * <pre>{@code
     * matrix.updateColumn(0, x -> x + 1); // Adds 1 to all values in the first column
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param columnIndex the index of the column to update (0-based)
     * @param func the function to apply to each element in the column
     * @throws ArrayIndexOutOfBoundsException if columnIndex is out of bounds
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.IntUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsInt(a[i][columnIndex]);
        }
    }

    /**
     * Gets the values on the main diagonal (left-up to right-down).
     * The matrix must be square.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * int[] diagonal = matrix.getLU2RD(); // Returns [1, 4]
     * }</pre>
     * 
     * @return an array containing the diagonal values
     * @throws IllegalStateException if the matrix is not square
     */
    public int[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final int[] res = new int[rows];

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][i]; // NOSONAR
        }

        return res;
    }

    /**
     * Sets the values on the main diagonal (left-up to right-down).
     * The matrix must be square and the diagonal array must have at least as many elements as the matrix dimension.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.setLU2RD(new int[]{1, 2, 3}); // Sets main diagonal values
     * }</pre>
     * 
     * @param diagonal the values to set on the main diagonal
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
     */
    public void setLU2RD(final int[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates the values on the main diagonal (left-up to right-down) by applying the specified function.
     * The matrix must be square.
     *
     * <p>Example:</p>
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
     * Gets the values on the anti-diagonal (right-up to left-down).
     * The matrix must be square.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * int[] antiDiagonal = matrix.getRU2LD(); // Returns [2, 3]
     * }</pre>
     * 
     * @return an array containing the anti-diagonal values
     * @throws IllegalStateException if the matrix is not square
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
     * Sets the values on the anti-diagonal (right-up to left-down).
     * The matrix must be square and the diagonal array must have at least as many elements as the matrix dimension.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.setRU2LD(new int[]{1, 2, 3}); // Sets anti-diagonal values
     * }</pre>
     * 
     * @param diagonal the values to set on the anti-diagonal
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
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
     * <p>Example:</p>
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
     * Updates all elements in the matrix by applying the specified function.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.updateAll(x -> x * 2); // Doubles all values in the matrix
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each element
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.applyAsInt(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Updates all elements in the matrix based on their position.
     * The function receives the row and column indices and returns the new value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.updateAll((i, j) -> i + j); // Sets each element to sum of its indices
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function that takes row and column indices and returns the new value
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Integer, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements that match the predicate with the specified new value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.replaceIf(x -> x < 0, 0); // Replaces all negative values with 0
     * }</pre>
     * 
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntPredicate<E> predicate, final int newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces elements based on their position using a predicate that tests row and column indices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.replaceIf((i, j) -> i == j, 1); // Sets diagonal elements to 1
     * }</pre>
     * 
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition that tests row and column indices
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final int newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new IntMatrix by applying a function to each element.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix squared = matrix.map(x -> x * x); // Creates new matrix with squared values
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each element
     * @return a new IntMatrix with transformed values
     * @throws E if the function throws an exception
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
     * <p>Example:</p>
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
     * <p>Example:</p>
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
     * <p>Example:</p>
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
     * Fills the entire matrix with the specified value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.fill(0); // Sets all elements to 0
     * }</pre>
     * 
     * @param val the value to fill the matrix with
     */
    public void fill(final int val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills the matrix with values from another 2D array, starting from the top-left corner.
     * If the source array is larger than the matrix, only the fitting portion is copied.
     * If the source array is smaller, only the available values are copied.
     *
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{0, 0, 0}, {0, 0, 0}});
     * int[][] patch = {{1, 2}, {3, 4}};
     * matrix.fill(patch); // Fills from (0,0): [[1, 2, 0], [3, 4, 0]]
     * }</pre>
     *
     * @param b the 2D array to copy values from
     */
    public void fill(final int[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from another 2D array.
     * The filling starts at the specified position and copies as much as will fit.
     * 
     * <p>Example:</p>
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
     * Creates a deep copy of the entire matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix copy = matrix.copy();
     * copy.set(0, 0, 99); // Original matrix is unchanged
     * }</pre>
     * 
     * @return a new IntMatrix that is a deep copy of this matrix
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
     * Creates a deep copy of a range of rows from the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix subset = matrix.copy(1, 3); // Copies rows 1 and 2 (exclusive end)
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new IntMatrix containing the specified rows
     * @throws IndexOutOfBoundsException if indices are out of bounds
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
     * Creates a deep copy of a submatrix defined by row and column ranges.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix submatrix = matrix.copy(0, 2, 1, 3); // Copies rows 0-1, columns 1-2
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new IntMatrix containing the specified submatrix
     * @throws IndexOutOfBoundsException if indices are out of bounds
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
     * Creates a matrix with extended dimensions, filling new cells with 0.
     * If the new dimensions are smaller than current, the matrix is truncated.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix extended = matrix.extend(5, 5); // Extends to 5x5, new cells are 0
     * }</pre>
     * 
     * @param newRows the desired number of rows
     * @param newCols the desired number of columns
     * @return a new IntMatrix with the specified dimensions
     */
    public IntMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, 0);
    }

    /**
     * Creates a matrix with extended dimensions, filling new cells with the specified value.
     * If the new dimensions are smaller than current, the matrix is truncated.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix extended = matrix.extend(5, 5, -1); // Extends to 5x5, new cells are -1
     * }</pre>
     * 
     * @param newRows the desired number of rows
     * @param newCols the desired number of columns
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new IntMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public IntMatrix extend(final int newRows, final int newCols, final int defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

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
     * Creates a matrix extended in all four directions by the specified amounts, filling new cells with 0.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix extended = matrix.extend(1, 1, 2, 2); // Adds 1 row up/down, 2 columns left/right
     * }</pre>
     * 
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @return a new IntMatrix with extended dimensions
     */
    public IntMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, 0);
    }

    /**
     * Creates a matrix extended in all four directions, filling new cells with the specified value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix extended = matrix.extend(1, 1, 2, 2, -1); // Extends with -1 in new cells
     * }</pre>
     * 
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new IntMatrix with extended dimensions
     * @throws IllegalArgumentException if any extension parameter is negative
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
            int tmp = 0;
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
     * IntMatrix flipped = matrix.flipH(); // [[1,2,3]] becomes [[3,2,1]]
     * }</pre>
     * 
     * @return a new IntMatrix with rows reversed
     * @see #flipV()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">MATLAB flip function</a>
     */
    public IntMatrix flipH() {
        final IntMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * Creates a new matrix that is vertically flipped (each column reversed).
     * The original matrix is not modified.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix flipped = matrix.flipV(); // [[1],[2],[3]] becomes [[3],[2],[1]]
     * }</pre>
     * 
     * @return a new IntMatrix with columns reversed
     * @see #flipH()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">MATLAB flip function</a>
     */
    public IntMatrix flipV() {
        final IntMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * Creates a new matrix rotated 90 degrees clockwise.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix rotated = matrix.rotate90();
     * // [[1,2],    becomes    [[3,1],
     * //  [3,4]]                [4,2]]
     * }</pre>
     * 
     * @return a new IntMatrix rotated 90 degrees clockwise
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
     * Creates a new matrix rotated 180 degrees.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix rotated = matrix.rotate180();
     * // [[1,2],    becomes    [[4,3],
     * //  [3,4]]                [2,1]]
     * }</pre>
     * 
     * @return a new IntMatrix rotated 180 degrees
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
     * Creates a new matrix rotated 270 degrees clockwise (90 degrees counter-clockwise).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix rotated = matrix.rotate270();
     * // [[1,2],    becomes    [[2,4],
     * //  [3,4]]                [1,3]]
     * }</pre>
     * 
     * @return a new IntMatrix rotated 270 degrees clockwise
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
     * Creates a new matrix that is the transpose of this matrix.
     * Rows become columns and columns become rows.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix transposed = matrix.transpose();
     * // [[1,2,3],    becomes    [[1,4],
     * //  [4,5,6]]                [2,5],
     * //                          [3,6]]
     * }</pre>
     * 
     * @return a new IntMatrix that is the transpose of this matrix
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
     * Reshapes the matrix to new dimensions.
     * Elements are read in row-major order from the original matrix and placed into the new shape.
     *
     * <p>If the new shape has fewer total elements than the original, elements are truncated.
     * If the new shape has more total elements, the excess positions are filled with zeros.</p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1,2,3},{4,5,6}});
     * IntMatrix reshaped = matrix.reshape(3, 2); // Becomes [[1,2],[3,4],[5,6]]
     * IntMatrix extended = matrix.reshape(2, 4); // Becomes [[1,2,3,4],[5,6,0,0]]
     * }</pre>
     *
     * @param newRows the number of rows in the reshaped matrix
     * @param newCols the number of columns in the reshaped matrix
     * @return a new IntMatrix with the specified shape containing this matrix's elements
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1,2}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1,2},{3,4}});
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
     * Flattens the matrix into a single IntList in row-major order.
     * Elements are read row by row from left to right.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1,2},{3,4}});
     * IntList flat = matrix.flatten(); // Returns [1, 2, 3, 4]
     * }</pre>
     * 
     * @return an IntList containing all elements in row-major order
     */
    @Override
    public IntList flatten() {
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{3, 1, 2}, {6, 4, 5}});
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
     * Stacks this matrix vertically with another matrix.
     * The matrices must have the same number of columns.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][]{{1,2,3},{4,5,6}});
     * IntMatrix b = IntMatrix.of(new int[][]{{7,8,9},{10,11,12}});
     * IntMatrix c = a.vstack(b);
     * // Result: [[1, 2, 3],
     * //          [4, 5, 6],
     * //          [7, 8, 9],
     * //          [10, 11, 12]]
     * }</pre>
     * 
     * @param b the matrix to stack below this matrix
     * @return a new IntMatrix with b stacked vertically below this matrix
     * @throws IllegalArgumentException if the matrices don't have the same number of columns
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
     * Stacks this matrix horizontally with another matrix.
     * The matrices must have the same number of rows.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][]{{1,2,3},{4,5,6}});
     * IntMatrix b = IntMatrix.of(new int[][]{{7,8,9},{10,11,12}});
     * IntMatrix c = a.hstack(b);
     * // Result: [[1, 2, 3, 7, 8, 9],
     * //          [4, 5, 6, 10, 11, 12]]
     * }</pre>
     * 
     * @param b the matrix to stack to the right of this matrix
     * @return a new IntMatrix with b stacked horizontally to the right
     * @throws IllegalArgumentException if the matrices don't have the same number of rows
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
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][]{{1,2},{3,4}});
     * IntMatrix b = IntMatrix.of(new int[][]{{5,6},{7,8}});
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
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][]{{5,6},{7,8}});
     * IntMatrix b = IntMatrix.of(new int[][]{{1,2},{3,4}});
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
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix a = IntMatrix.of(new int[][]{{1,2},{3,4}});
     * IntMatrix b = IntMatrix.of(new int[][]{{5,6},{7,8}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix primitive = IntMatrix.of(new int[][]{{1, 2}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix intMatrix = IntMatrix.of(new int[][]{{1, 2}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix intMatrix = IntMatrix.of(new int[][]{{1, 2}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix intMatrix = IntMatrix.of(new int[][]{{1, 2}});
     * DoubleMatrix doubleMatrix = intMatrix.toDoubleMatrix();
     * }</pre>
     * 
     * @return a new DoubleMatrix with converted values
     */
    public DoubleMatrix toDoubleMatrix() {
        return DoubleMatrix.create(a);
    }

    /**
     * Performs element-wise operation on two matrices using the provided binary operator.
     * The matrices must have the same dimensions.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix result = matrix1.zipWith(matrix2, (a, b) -> a * b); // Element-wise multiplication
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param matrixB the second matrix
     * @param zipFunction the binary operator to apply element-wise
     * @return a new IntMatrix with the results of the element-wise operation
     * @throws IllegalArgumentException if the matrices have different dimensions
     * @throws E if the zip function throws an exception
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
     * Performs element-wise operation on three matrices using the provided ternary operator.
     * All matrices must have the same dimensions.
     *
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix result = matrix1.zipWith(matrix2, matrix3, (a, b, c) -> a + b + c);
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param matrixB the second matrix
     * @param matrixC the third matrix
     * @param zipFunction the ternary operator to apply element-wise
     * @return a new IntMatrix with the results of the element-wise operation
     * @throws IllegalArgumentException if the matrices have different dimensions
     * @throws E if the zip function throws an exception
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1,2,3},{4,5,6},{7,8,9}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1,2,3},{4,5,6},{7,8,9}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}, {5, 6}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}, {5, 6}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}, {5, 6}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
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
     * Elements are processed in row-major order (row by row, left to right).
     * 
     * <p>The operation may be parallelized internally if the matrix is large enough
     * to benefit from parallel processing, based on internal heuristics.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * List<Integer> values = new ArrayList<>();
     * matrix.forEach(value -> values.add(value));
     * // values now contains [1, 2, 3, 4]
     * 
     * // Calculate sum using forEach
     * int[] sum = {0};
     * matrix.forEach(value -> sum[0] += value);
     * // sum[0] is now 10
     * }</pre>
     * 
     * @param <E> the type of exception that the action may throw
     * @param action the action to be performed for each element
     * @throws E if the action throws an exception
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
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
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
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
     * Returns a hash code value for the matrix.
     * The hash code is computed based on the deep contents of the internal 2D array,
     * taking into account all element values and the matrix structure.
     * 
     * <p>Two matrices with the same dimensions and identical elements in the same
     * positions will have the same hash code.</p>
     * 
     * @return a hash code value for this matrix
     */
    @Override
    public int hashCode() {
        return N.deepHashCode(a);
    }

    /**
     * Indicates whether some other object is "equal to" this matrix.
     * Two IntMatrix objects are considered equal if they have the same dimensions
     * and contain the same int values in the same positions.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * IntMatrix m2 = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * IntMatrix m3 = IntMatrix.of(new int[][]{{1, 2}, {4, 3}});
     * 
     * m1.equals(m2); // true - same dimensions and values
     * m1.equals(m3); // false - different values
     * }</pre>
     * 
     * @param obj the reference object with which to compare
     * @return {@code true} if this matrix is equal to the obj argument;
     *         {@code false} otherwise
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
     * Returns a string representation of the matrix.
     * The string representation consists of the matrix elements in a 2D array format,
     * with rows separated by commas and the entire structure enclosed in brackets.
     * 
     * <p>This method provides a human-readable representation of the matrix contents,
     * suitable for debugging and logging purposes.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * System.out.println(matrix.toString()); // Prints: [[1, 2], [3, 4]]
     * }</pre>
     * 
     * @return a string representation of the matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
