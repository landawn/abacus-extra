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
import com.landawn.abacus.util.u.OptionalFloat;
import com.landawn.abacus.util.stream.FloatIteratorEx;
import com.landawn.abacus.util.stream.FloatStream;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

/**
 * A matrix implementation for float primitive values, providing efficient storage and operations
 * for two-dimensional float arrays. This class extends AbstractMatrix and provides specialized
 * methods for float matrix manipulation including mathematical operations, transformations,
 * and element access.
 * 
 * <p>The matrix is stored internally as a 2D float array (float[][]) and provides
 * methods for element access, manipulation, and various matrix operations such as
 * transpose, rotation, multiplication, and more.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1.0f, 2.0f}, {3.0f, 4.0f}});
 * FloatMatrix transposed = matrix.transpose();
 * float element = matrix.get(0, 1); // Returns 2.0f
 * }</pre>
 * 
 * @see IntMatrix
 * @see DoubleMatrix
 * @see Matrix
 */
public final class FloatMatrix extends AbstractMatrix<float[], FloatList, FloatStream, Stream<FloatStream>, FloatMatrix> {

    static final FloatMatrix EMPTY_FLOAT_MATRIX = new FloatMatrix(new float[0][0]);

    /**
     * Constructs a FloatMatrix from a 2D float array.
     * If the input array is null, an empty matrix (0x0) is created.
     * 
     * @param a the 2D float array to wrap in a matrix. Can be null.
     */
    public FloatMatrix(final float[][] a) {
        super(a == null ? new float[0][0] : a);
    }

    /**
     * Returns an empty FloatMatrix with dimensions 0x0.
     * This method returns a singleton empty matrix instance for memory efficiency.
     * 
     * @return an empty FloatMatrix instance
     */
    public static FloatMatrix empty() {
        return EMPTY_FLOAT_MATRIX;
    }

    /**
     * Creates a FloatMatrix from a 2D float array.
     * This is a factory method that provides a more readable way to create matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1.0f, 2.0f}, {3.0f, 4.0f}});
     * }</pre>
     * 
     * @param a the 2D float array to create the matrix from
     * @return a new FloatMatrix containing the provided data, or empty matrix if input is null/empty
     */
    public static FloatMatrix of(final float[]... a) {
        return N.isEmpty(a) ? EMPTY_FLOAT_MATRIX : new FloatMatrix(a);
    }

    /**
     * Creates a FloatMatrix from a 2D int array by converting int values to float.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.create(new int[][]{{1, 2}, {3, 4}});
     * // Creates a matrix with values {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * }</pre>
     * 
     * @param a the 2D int array to convert to a float matrix
     * @return a new FloatMatrix with converted values, or empty matrix if input is null/empty
     */
    public static FloatMatrix create(final int[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_FLOAT_MATRIX;
        }

        final float[][] c = new float[a.length][a[0].length];

        for (int i = 0, len = a.length; i < len; i++) {
            final int[] aa = a[i];
            final float[] cc = c[i];

            for (int j = 0, col = a[0].length; j < col; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new FloatMatrix(c);
    }

    /**
     * Creates a 1xN FloatMatrix with random float values between 0.0 and 1.0.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix randomRow = FloatMatrix.random(5); // Creates 1x5 matrix with random values
     * }</pre>
     * 
     * @param len the number of columns (length) of the resulting 1-row matrix
     * @return a new 1xN FloatMatrix with random values
     */
    @SuppressWarnings("deprecation")
    public static FloatMatrix random(final int len) {
        return new FloatMatrix(new float[][] { FloatList.random(len).array() });
    }

    /**
     * Creates a 1xN FloatMatrix where all elements have the same value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.repeat(3.14f, 5); // Creates [[3.14, 3.14, 3.14, 3.14, 3.14]]
     * }</pre>
     * 
     * @param val the value to repeat
     * @param len the number of columns (length) of the resulting 1-row matrix
     * @return a new 1xN FloatMatrix with all elements set to val
     */
    public static FloatMatrix repeat(final float val, final int len) {
        return new FloatMatrix(new float[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a diagonal matrix with the specified values on the main diagonal (left-up to right-down).
     * All other elements are set to 0.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix diagonal = FloatMatrix.diagonalLU2RD(new float[]{1.0f, 2.0f, 3.0f});
     * // Creates: [[1.0, 0.0, 0.0],
     * //           [0.0, 2.0, 0.0],
     * //           [0.0, 0.0, 3.0]]
     * }</pre>
     * 
     * @param leftUp2RightDownDiagonal the values for the main diagonal
     * @return a new square FloatMatrix with the specified diagonal values
     */
    public static FloatMatrix diagonalLU2RD(final float[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a diagonal matrix with the specified values on the anti-diagonal (right-up to left-down).
     * All other elements are set to 0.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix diagonal = FloatMatrix.diagonalRU2LD(new float[]{1.0f, 2.0f, 3.0f});
     * // Creates: [[0.0, 0.0, 1.0],
     * //           [0.0, 2.0, 0.0],
     * //           [3.0, 0.0, 0.0]]
     * }</pre>
     * 
     * @param rightUp2LeftDownDiagonal the values for the anti-diagonal
     * @return a new square FloatMatrix with the specified anti-diagonal values
     */
    public static FloatMatrix diagonalRU2LD(final float[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a diagonal matrix with values on both the main diagonal and anti-diagonal.
     * The matrix size is determined by the length of the diagonal arrays.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.diagonal(new float[]{1.0f, 2.0f, 3.0f}, 
     *                                          new float[]{4.0f, 5.0f, 6.0f});
     * // Creates: [[1.0, 0.0, 4.0],
     * //           [0.0, 2.0, 0.0],
     * //           [6.0, 0.0, 3.0]]
     * }</pre>
     * 
     * @param leftUp2RightDownDiagonal the values for the main diagonal (can be null)
     * @param rightUp2LeftDownDiagonal the values for the anti-diagonal (can be null)
     * @return a new square FloatMatrix with the specified diagonal values
     * @throws IllegalArgumentException if both arrays are non-null and have different lengths
     */
    public static FloatMatrix diagonal(final float[] leftUp2RightDownDiagonal, final float[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        if (N.isEmpty(leftUp2RightDownDiagonal) && N.isEmpty(rightUp2LeftDownDiagonal)) {
            return EMPTY_FLOAT_MATRIX;
        }

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final float[][] c = new float[len][len];

        if (N.isEmpty(leftUp2RightDownDiagonal)) {
            if (N.notEmpty(rightUp2LeftDownDiagonal)) {
                for (int i = 0, j = len - 1; i < len; i++, j--) {
                    c[i][j] = rightUp2LeftDownDiagonal[i];
                }
            }
        } else {
            for (int i = 0; i < len; i++) {
                c[i][i] = leftUp2RightDownDiagonal[i]; // NOSONAR
            }

            if (N.notEmpty(rightUp2LeftDownDiagonal)) {
                for (int i = 0, j = len - 1; i < len; i++, j--) {
                    c[i][j] = rightUp2LeftDownDiagonal[i];
                }
            }
        }

        return new FloatMatrix(c);
    }

    /**
     * Converts a boxed Float Matrix to a primitive FloatMatrix.
     * Null values in the input matrix are converted to 0.0f.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Float> boxed = Matrix.of(new Float[][]{{1.0f, 2.0f}, {3.0f, 4.0f}});
     * FloatMatrix primitive = FloatMatrix.unbox(boxed);
     * }</pre>
     * 
     * @param x the boxed Float matrix to convert
     * @return a new FloatMatrix with primitive float values
     */
    public static FloatMatrix unbox(final Matrix<Float> x) {
        return FloatMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of the matrix elements, which is float.class.
     * 
     * @return float.class
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return float.class;
    }

    /**
     * Gets the element at the specified row and column.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1.0f, 2.0f}, {3.0f, 4.0f}});
     * float value = matrix.get(0, 1); // Returns 2.0f
     * }</pre>
     * 
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position [i][j]
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public float get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Gets the element at the specified point.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Point p = Point.of(0, 1);
     * float value = matrix.get(p); // Gets element at row 0, column 1
     * }</pre>
     * 
     * @param point the point specifying row and column indices
     * @return the element at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public float get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.set(0, 1, 5.5f); // Sets element at row 0, column 1 to 5.5
     * }</pre>
     * 
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public void set(final int i, final int j, final float val) {
        a[i][j] = val;
    }

    /**
     * Sets the element at the specified point.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Point p = Point.of(0, 1);
     * matrix.set(p, 5.5f); // Sets element at row 0, column 1 to 5.5
     * }</pre>
     * 
     * @param point the point specifying row and column indices
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public void set(final Point point, final float val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element above the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalFloat above = matrix.upOf(1, 0); // Gets element at row 0, column 0
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalFloat containing the element above, or empty if at top row
     */
    public OptionalFloat upOf(final int i, final int j) {
        return i == 0 ? OptionalFloat.empty() : OptionalFloat.of(a[i - 1][j]);
    }

    /**
     * Returns the element below the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalFloat below = matrix.downOf(0, 0); // Gets element at row 1, column 0
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalFloat containing the element below, or empty if at bottom row
     */
    public OptionalFloat downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalFloat.empty() : OptionalFloat.of(a[i + 1][j]);
    }

    /**
     * Returns the element to the left of the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalFloat left = matrix.leftOf(0, 1); // Gets element at row 0, column 0
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalFloat containing the element to the left, or empty if at leftmost column
     */
    public OptionalFloat leftOf(final int i, final int j) {
        return j == 0 ? OptionalFloat.empty() : OptionalFloat.of(a[i][j - 1]);
    }

    /**
     * Returns the element to the right of the specified position, if it exists.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * OptionalFloat right = matrix.rightOf(0, 0); // Gets element at row 0, column 1
     * }</pre>
     * 
     * @param i the row index
     * @param j the column index
     * @return an OptionalFloat containing the element to the right, or empty if at rightmost column
     */
    public OptionalFloat rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalFloat.empty() : OptionalFloat.of(a[i][j + 1]);
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
        final Point rightDown = i < rows - 1 && j < cols - 1 ? Point.of(j + 1, j + 1) : null;
        final Point leftDown = i < rows - 1 && j > 0 ? Point.of(i + 1, j - 1) : null;

        return Stream.of(leftUp, up, rightUp, right, rightDown, down, leftDown, left);
    }

    /**
     * Returns a copy of the specified row as a float array.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * float[] firstRow = matrix.row(0); // Gets a copy of the first row
     * }</pre>
     * 
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return a copy of the specified row
     * @throws IllegalArgumentException if rowIndex is out of bounds
     */
    public float[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as a float array.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * float[] firstColumn = matrix.column(0); // Gets a copy of the first column
     * }</pre>
     * 
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing the values from the specified column
     * @throws IllegalArgumentException if columnIndex is out of bounds
     */
    public float[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final float[] c = new float[rows];

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
     * matrix.setRow(0, new float[]{1.0f, 2.0f, 3.0f}); // Sets the first row
     * }</pre>
     * 
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to set in the row
     * @throws IllegalArgumentException if row length doesn't match matrix columns or index is out of bounds
     */
    public void setRow(final int rowIndex, final float[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Sets an entire column with the values from the provided array.
     * The array length must match the number of rows in the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.setColumn(0, new float[]{1.0f, 2.0f, 3.0f}); // Sets the first column
     * }</pre>
     * 
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to set in the column
     * @throws IllegalArgumentException if column length doesn't match matrix rows or index is out of bounds
     */
    public void setColumn(final int columnIndex, final float[] column) throws IllegalArgumentException {
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
     * @param rowIndex the index of the row to update
     * @param func the function to apply to each element in the row
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.FloatUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsFloat(a[rowIndex][i]);
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
     * @param columnIndex the index of the column to update
     * @param func the function to apply to each element in the column
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.FloatUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsFloat(a[i][columnIndex]);
        }
    }

    /**
     * Gets the values on the main diagonal (left-up to right-down).
     * The matrix must be square.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1, 2}, {3, 4}});
     * float[] diagonal = matrix.getLU2RD(); // Returns [1, 4]
     * }</pre>
     * 
     * @return an array containing the diagonal values
     * @throws IllegalArgumentException if the matrix is not square
     */
    public float[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final float[] res = new float[rows];

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
     * matrix.setLU2RD(new float[]{1.0f, 2.0f, 3.0f}); // Sets main diagonal values
     * }</pre>
     * 
     * @param diagonal the values to set on the main diagonal
     * @throws IllegalArgumentException if the matrix is not square or diagonal array is too short
     */
    public void setLU2RD(final float[] diagonal) throws IllegalArgumentException {
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
     * @throws E if the function throws an exception
     * @throws IllegalArgumentException if the matrix is not square
     */
    public <E extends Exception> void updateLU2RD(final Throwables.FloatUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsFloat(a[i][i]);
        }
    }

    /**
     * Gets the values on the anti-diagonal (right-up to left-down).
     * The matrix must be square.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1, 2}, {3, 4}});
     * float[] antiDiagonal = matrix.getRU2LD(); // Returns [2, 3]
     * }</pre>
     * 
     * @return an array containing the anti-diagonal values
     * @throws IllegalArgumentException if the matrix is not square
     */
    public float[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final float[] res = new float[rows];

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
     * matrix.setRU2LD(new float[]{1.0f, 2.0f, 3.0f}); // Sets anti-diagonal values
     * }</pre>
     * 
     * @param diagonal the values to set on the anti-diagonal
     * @throws IllegalArgumentException if the matrix is not square or diagonal array is too short
     */
    public void setRU2LD(final float[] diagonal) throws IllegalArgumentException {
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
     * @throws E if the function throws an exception
     * @throws IllegalArgumentException if the matrix is not square
     */
    public <E extends Exception> void updateRU2LD(final Throwables.FloatUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsFloat(a[i][cols - i - 1]);
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
    public <E extends Exception> void updateAll(final Throwables.FloatUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.applyAsFloat(a[i][j]);
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
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Float, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements that match the predicate with the specified new value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.replaceIf(x -> x < 0, 0.0f); // Replaces all negative values with 0
     * }</pre>
     * 
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.FloatPredicate<E> predicate, final float newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces elements based on their position using a predicate that tests row and column indices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.replaceIf((i, j) -> i == j, 1.0f); // Sets diagonal elements to 1
     * }</pre>
     * 
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition that tests row and column indices
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final float newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new FloatMatrix by applying a function to each element.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix squared = matrix.map(x -> x * x); // Creates new matrix with squared values
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param func the function to apply to each element
     * @return a new FloatMatrix with transformed values
     * @throws E if the function throws an exception
     */
    public <E extends Exception> FloatMatrix map(final Throwables.FloatUnaryOperator<E> func) throws E {
        final float[][] result = new float[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsFloat(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return FloatMatrix.of(result);
    }

    /**
     * Creates a new Matrix by applying a function that converts float values to objects of type T.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<String> stringMatrix = matrix.mapToObj(x -> String.valueOf(x), String.class);
     * }</pre>
     * 
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the type of exception that the function may throw
     * @param func the function to convert float values to type T
     * @param targetElementType the Class object for type T
     * @return a new Matrix containing the converted values
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.FloatFunction<? extends T, E> func, final Class<T> targetElementType) throws E {
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
     * matrix.fill(0.0f); // Sets all elements to 0
     * }</pre>
     * 
     * @param val the value to fill the matrix with
     */
    public void fill(final float val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills the matrix with values from another 2D array, starting from the top-left corner.
     * If the source array is larger than the matrix, only the fitting portion is copied.
     * If the source array is smaller, only the available values are copied.
     * 
     * @param b the 2D array to copy values from
     */
    public void fill(final float[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from another 2D array.
     * The filling starts at the specified position and copies as much as will fit.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * float[][] patch = {{1, 2}, {3, 4}};
     * matrix.fill(1, 1, patch); // Fills starting at row 1, column 1
     * }</pre>
     * 
     * @param fromRowIndex the starting row index for filling
     * @param fromColumnIndex the starting column index for filling
     * @param b the 2D array to copy values from
     * @throws IndexOutOfBoundsException if the starting indices are out of bounds
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final float[][] b) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, rows, rows);
        N.checkFromToIndex(fromColumnIndex, cols, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
        }
    }

    /**
     * Creates a copy of the entire matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix copy = matrix.copy();
     * copy.set(0, 0, 99.0f); // Original matrix is unchanged
     * }</pre>
     * 
     * @return a new FloatMatrix that is a copy of this matrix
     */
    @Override
    public FloatMatrix copy() {
        final float[][] c = new float[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new FloatMatrix(c);
    }

    /**
     * Creates a copy of a range of rows from the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix subset = matrix.copy(1, 3); // Copies rows 1 and 2 (exclusive end)
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new FloatMatrix containing the specified rows
     * @throws IndexOutOfBoundsException if indices are out of bounds
     */
    @Override
    public FloatMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final float[][] c = new float[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new FloatMatrix(c);
    }

    /**
     * Creates a copy of a submatrix defined by row and column ranges.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix submatrix = matrix.copy(0, 2, 1, 3); // Copies rows 0-1, columns 1-2
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new FloatMatrix containing the specified submatrix
     * @throws IndexOutOfBoundsException if indices are out of bounds
     */
    @Override
    public FloatMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final float[][] c = new float[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new FloatMatrix(c);
    }

    /**
     * Creates a matrix with extended dimensions, filling new cells with 0.
     * If the new dimensions are smaller than current, the matrix is truncated.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix extended = matrix.extend(5, 5); // Extends to 5x5, new cells are 0
     * }</pre>
     * 
     * @param newRows the desired number of rows
     * @param newCols the desired number of columns
     * @return a new FloatMatrix with the specified dimensions
     */
    public FloatMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, 0);
    }

    /**
     * Creates a matrix with extended dimensions, filling new cells with the specified value.
     * If the new dimensions are smaller than current, the matrix is truncated.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix extended = matrix.extend(5, 5, -1.0f); // Extends to 5x5, new cells are -1
     * }</pre>
     * 
     * @param newRows the desired number of rows
     * @param newCols the desired number of columns
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new FloatMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public FloatMatrix extend(final int newRows, final int newCols, final float defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            final boolean fillDefaultValue = defaultValueForNewCell != 0;
            final float[][] b = new float[newRows][];

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : new float[newCols];

                if (fillDefaultValue) {
                    if (i >= rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, defaultValueForNewCell);
                    }
                }
            }

            return new FloatMatrix(b);
        }
    }

    /**
     * Creates a matrix extended in all four directions by the specified amounts, filling new cells with 0.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix extended = matrix.extend(1, 1, 2, 2); // Adds 1 row up/down, 2 columns left/right
     * }</pre>
     * 
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @return a new FloatMatrix with extended dimensions
     */
    public FloatMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, 0);
    }

    /**
     * Creates a matrix extended in all four directions, filling new cells with the specified value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix extended = matrix.extend(1, 1, 2, 2, -1.0f); // Extends with -1 in new cells
     * }</pre>
     * 
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new FloatMatrix with extended dimensions
     * @throws IllegalArgumentException if any extension parameter is negative
     */
    public FloatMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final float defaultValueForNewCell)
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
            final float[][] b = new float[newRows][newCols];

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

            return new FloatMatrix(b);
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
            float tmp = 0;
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
     * FloatMatrix flipped = matrix.flipH(); // [[1,2,3]] becomes [[3,2,1]]
     * }</pre>
     * 
     * @return a new FloatMatrix with rows reversed
     * @see #flipV()
     */
    public FloatMatrix flipH() {
        final FloatMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * Creates a new matrix that is vertically flipped (each column reversed).
     * The original matrix is not modified.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix flipped = matrix.flipV(); // [[1],[2],[3]] becomes [[3],[2],[1]]
     * }</pre>
     * 
     * @return a new FloatMatrix with columns reversed
     * @see #flipH()
     */
    public FloatMatrix flipV() {
        final FloatMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * Creates a new matrix rotated 90 degrees clockwise.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix rotated = matrix.rotate90();
     * // [[1,2],    becomes    [[3,1],
     * //  [3,4]]                [4,2]]
     * }</pre>
     * 
     * @return a new FloatMatrix rotated 90 degrees clockwise
     */
    @Override
    public FloatMatrix rotate90() {
        final float[][] c = new float[cols][rows];

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

        return new FloatMatrix(c);
    }

    /**
     * Creates a new matrix rotated 180 degrees.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix rotated = matrix.rotate180();
     * // [[1,2],    becomes    [[4,3],
     * //  [3,4]]                [2,1]]
     * }</pre>
     * 
     * @return a new FloatMatrix rotated 180 degrees
     */
    @Override
    public FloatMatrix rotate180() {
        final float[][] c = new float[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new FloatMatrix(c);
    }

    /**
     * Creates a new matrix rotated 270 degrees clockwise (90 degrees counter-clockwise).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix rotated = matrix.rotate270();
     * // [[1,2],    becomes    [[2,4],
     * //  [3,4]]                [1,3]]
     * }</pre>
     * 
     * @return a new FloatMatrix rotated 270 degrees clockwise
     */
    @Override
    public FloatMatrix rotate270() {
        final float[][] c = new float[cols][rows];

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

        return new FloatMatrix(c);
    }

    /**
     * Creates a new matrix that is the transpose of this matrix.
     * Rows become columns and columns become rows.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix transposed = matrix.transpose();
     * // [[1,2,3],    becomes    [[1,4],
     * //  [4,5,6]]                [2,5],
     * //                          [3,6]]
     * }</pre>
     * 
     * @return a new FloatMatrix that is the transpose of this matrix
     */
    @Override
    public FloatMatrix transpose() {
        final float[][] c = new float[cols][rows];

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

        return new FloatMatrix(c);
    }

    /**
     * Reshapes the matrix to new dimensions.
     * Elements are read in row-major order and placed into the new shape.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1,2,3},{4,5,6}});
     * FloatMatrix reshaped = matrix.reshape(3, 2); // Becomes [[1,2],[3,4],[5,6]]
     * }</pre>
     * 
     * @param newRows the number of rows in the reshaped matrix
     * @param newCols the number of columns in the reshaped matrix
     * @return a new FloatMatrix with the specified shape containing this matrix's elements
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public FloatMatrix reshape(final int newRows, final int newCols) {
        final float[][] c = new float[newRows][newCols];

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new FloatMatrix(c);
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

        return new FloatMatrix(c);
    }

    /**
     * Repeats elements in both row and column directions.
     * Each element is repeated to form a block of size rowRepeats x colRepeats.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1,2}});
     * FloatMatrix repeated = matrix.repelem(2, 3);
     * // Result: [[1,1,1,2,2,2],
     * //          [1,1,1,2,2,2]]
     * }</pre>
     * 
     * @param rowRepeats number of times to repeat each element in row direction
     * @param colRepeats number of times to repeat each element in column direction
     * @return a new FloatMatrix with repeated elements
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see IntMatrix#repelem(int, int)
     */
    @Override
    public FloatMatrix repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final float[][] c = new float[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            final float[] aa = a[i];
            final float[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                N.copy(Array.repeat(aa[j], colRepeats), 0, fr, j * colRepeats, colRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new FloatMatrix(c);
    }

    /**
     * Repeats the entire matrix in a tiled pattern.
     * The matrix is repeated as a whole rowRepeats times vertically and colRepeats times horizontally.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1,2},{3,4}});
     * FloatMatrix repeated = matrix.repmat(2, 3);
     * // Result: [[1,2,1,2,1,2],
     * //          [3,4,3,4,3,4],
     * //          [1,2,1,2,1,2],
     * //          [3,4,3,4,3,4]]
     * }</pre>
     * 
     * @param rowRepeats number of times to repeat the matrix vertically
     * @param colRepeats number of times to repeat the matrix horizontally
     * @return a new FloatMatrix with the tiled pattern
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see IntMatrix#repmat(int, int)
     */
    @Override
    public FloatMatrix repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final float[][] c = new float[rows * rowRepeats][cols * colRepeats];

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

        return new FloatMatrix(c);
    }

    /**
     * Flattens the matrix into a single FloatList in row-major order.
     * Elements are read row by row from left to right.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1,2},{3,4}});
     * FloatList flat = matrix.flatten(); // Returns [1, 2, 3, 4]
     * }</pre>
     * 
     * @return a FloatList containing all elements in row-major order
     */
    @Override
    public FloatList flatten() {
        final float[] c = new float[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return FloatList.of(c);
    }

    /**
     * Applies an operation to the underlying 2D array structure.
     * This method provides direct access to the internal array rows for efficient bulk operations.
     * 
     * @param <E> the type of exception that the operation may throw
     * @param op the operation to apply to each row array
     * @throws E if the operation throws an exception
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super float[], E> op) throws E {
        Arrays.flatOp(a, op);
    }

    /**
     * Stacks this matrix vertically with another matrix.
     * The matrices must have the same number of columns.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix a = FloatMatrix.of(new float[][]{{1,2},{3,4}});
     * FloatMatrix b = FloatMatrix.of(new float[][]{{5,6},{7,8}});
     * FloatMatrix stacked = a.vstack(b);
     * // Result: [[1,2],
     * //          [3,4],
     * //          [5,6],
     * //          [7,8]]
     * }</pre>
     * 
     * @param b the matrix to stack below this matrix
     * @return a new FloatMatrix with b stacked vertically below this matrix
     * @throws IllegalArgumentException if the matrices don't have the same number of columns
     * @see IntMatrix#vstack(IntMatrix)
     */
    public FloatMatrix vstack(final FloatMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final float[][] c = new float[rows + b.rows][];
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
        }

        return FloatMatrix.of(c);
    }

    /**
     * Stacks this matrix horizontally with another matrix.
     * The matrices must have the same number of rows.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix a = FloatMatrix.of(new float[][]{{1,2},{3,4}});
     * FloatMatrix b = FloatMatrix.of(new float[][]{{5,6},{7,8}});
     * FloatMatrix stacked = a.hstack(b);
     * // Result: [[1,2,5,6],
     * //          [3,4,7,8]]
     * }</pre>
     * 
     * @param b the matrix to stack to the right of this matrix
     * @return a new FloatMatrix with b stacked horizontally to the right
     * @throws IllegalArgumentException if the matrices don't have the same number of rows
     * @see IntMatrix#hstack(IntMatrix)
     */
    public FloatMatrix hstack(final FloatMatrix b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final float[][] c = new float[rows][cols + b.cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c[i], 0, cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
        }

        return FloatMatrix.of(c);
    }

    /**
     * Performs element-wise addition with another matrix.
     * The matrices must have the same dimensions.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix a = FloatMatrix.of(new float[][]{{1,2},{3,4}});
     * FloatMatrix b = FloatMatrix.of(new float[][]{{5,6},{7,8}});
     * FloatMatrix sum = a.add(b); // Result: [[6,8],[10,12]]
     * }</pre>
     * 
     * @param b the matrix to add to this matrix
     * @return a new FloatMatrix containing the element-wise sum
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public FloatMatrix add(final FloatMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't add Matrixes with different shape");

        final float[][] ba = b.a;
        final float[][] result = new float[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (a[i][j] + ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return FloatMatrix.of(result);
    }

    /**
     * Performs element-wise subtraction with another matrix.
     * The matrices must have the same dimensions.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix a = FloatMatrix.of(new float[][]{{5,6},{7,8}});
     * FloatMatrix b = FloatMatrix.of(new float[][]{{1,2},{3,4}});
     * FloatMatrix diff = a.subtract(b); // Result: [[4,4],[4,4]]
     * }</pre>
     * 
     * @param b the matrix to subtract from this matrix
     * @return a new FloatMatrix containing the element-wise difference
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public FloatMatrix subtract(final FloatMatrix b) throws IllegalArgumentException {
        N.checkArgument(Matrixes.isSameShape(this, b), "Can't subtract Matrixes with different shape");

        final float[][] ba = b.a;
        final float[][] result = new float[rows][cols];
        final Throwables.IntBiConsumer<RuntimeException> cmd = (i, j) -> result[i][j] = (a[i][j] - ba[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return FloatMatrix.of(result);
    }

    /**
     * Performs matrix multiplication with another matrix.
     * The number of columns in this matrix must equal the number of rows in the other matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix a = FloatMatrix.of(new float[][]{{1,2},{3,4}});
     * FloatMatrix b = FloatMatrix.of(new float[][]{{5,6},{7,8}});
     * FloatMatrix product = a.multiply(b); // Result: [[19,22],[43,50]]
     * }</pre>
     * 
     * @param b the matrix to multiply with
     * @return a new FloatMatrix containing the matrix product
     * @throws IllegalArgumentException if the matrix dimensions are incompatible for multiplication
     */
    public FloatMatrix multiply(final FloatMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.rows, "Illegal matrix dimensions");

        final float[][] ba = b.a;
        final float[][] result = new float[rows][b.cols];
        final Throwables.IntTriConsumer<RuntimeException> cmd = (i, j, k) -> result[i][j] += a[i][k] * ba[k][j];

        Matrixes.multiply(this, b, cmd);

        return new FloatMatrix(result);
    }

    /**
     * Converts this primitive float matrix to a boxed Float matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix primitive = FloatMatrix.of(new float[][]{{1.0f, 2.0f}});
     * Matrix<Float> boxed = primitive.boxed();
     * }</pre>
     * 
     * @return a new Matrix containing boxed Float values
     */
    public Matrix<Float> boxed() {
        final Float[][] c = new Float[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final float[] aa = a[i];
                final Float[] cc = c[i];

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
     * Converts this float matrix to a double matrix.
     * Each float value is converted to double precision.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix floatMatrix = FloatMatrix.of(new float[][]{{1.5f, 2.5f}});
     * DoubleMatrix doubleMatrix = floatMatrix.toDoubleMatrix();
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
     * FloatMatrix result = matrix1.zipWith(matrix2, (a, b) -> a * b); // Element-wise multiplication
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param matrixB the second matrix
     * @param zipFunction the binary operator to apply element-wise
     * @return a new FloatMatrix with the results of the element-wise operation
     * @throws IllegalArgumentException if the matrices have different dimensions
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> FloatMatrix zipWith(final FloatMatrix matrixB, final Throwables.FloatBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Can't zip two or more matrices which don't have same shape");

        final float[][] b = matrixB.a;
        final float[][] result = new float[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsFloat(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return FloatMatrix.of(result);
    }

    /**
     * Performs element-wise operation on three matrices using the provided ternary operator.
     * All matrices must have the same dimensions.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix result = matrix1.zipWith(matrix2, matrix3, (a, b, c) -> a + b + c);
     * }</pre>
     * 
     * @param <E> the type of exception that the function may throw
     * @param matrixB the second matrix
     * @param matrixC the third matrix
     * @param zipFunction the ternary operator to apply element-wise
     * @return a new FloatMatrix with the results of the element-wise operation
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> FloatMatrix zipWith(final FloatMatrix matrixB, final FloatMatrix matrixC, final Throwables.FloatTernaryOperator<E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Can't zip two or more matrices which don't have same shape");

        final float[][] b = matrixB.a;
        final float[][] c = matrixC.a;
        final float[][] result = new float[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsFloat(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return FloatMatrix.of(result);
    }

    /**
     * Returns a stream of elements on the main diagonal (left-up to right-down).
     * The matrix must be square.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1,2,3},{4,5,6},{7,8,9}});
     * FloatStream diagonal = matrix.streamLU2RD(); // Stream of [1, 5, 9]
     * }</pre>
     * 
     * @return a FloatStream of diagonal elements
     * @throws IllegalArgumentException if the matrix is not square
     */
    @Override
    public FloatStream streamLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return FloatStream.empty();
        }

        return FloatStream.of(new FloatIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public float nextFloat() {
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
     * Returns a stream of elements on the anti-diagonal (right-up to left-down).
     * The matrix must be square.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1,2,3},{4,5,6},{7,8,9}});
     * FloatStream antiDiagonal = matrix.streamRU2LD(); // Stream of [3, 5, 7]
     * }</pre>
     * 
     * @return a FloatStream of anti-diagonal elements
     * @throws IllegalArgumentException if the matrix is not square
     */
    @Override
    public FloatStream streamRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return FloatStream.empty();
        }

        return FloatStream.of(new FloatIteratorEx() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public float nextFloat() {
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
     * Returns a stream of all elements in row-major order (horizontal).
     * Elements are streamed row by row from left to right.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1,2},{3,4}});
     * FloatStream stream = matrix.streamH(); // Stream of [1, 2, 3, 4]
     * }</pre>
     * 
     * @return a FloatStream of all elements in row-major order
     */
    @Override
    public FloatStream streamH() {
        return streamH(0, rows);
    }

    /**
     * Returns a stream of elements from a single row.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatStream rowStream = matrix.streamH(0); // Stream of first row
     * }</pre>
     * 
     * @param rowIndex the index of the row to stream
     * @return a FloatStream of elements from the specified row
     */
    @Override
    public FloatStream streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * Returns a stream of elements from a range of rows in row-major order.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatStream stream = matrix.streamH(1, 3); // Stream rows 1 and 2
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a FloatStream of elements from the specified row range
     * @throws IndexOutOfBoundsException if indices are out of bounds
     */
    @Override
    public FloatStream streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return FloatStream.empty();
        }

        return FloatStream.of(new FloatIteratorEx() {
            private int i = fromRowIndex;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i < toRowIndex;
            }

            @Override
            public float nextFloat() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final float result = a[i][j++];

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
            public float[] toArray() {
                final int len = (int) count();
                final float[] c = new float[len];

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
     * Elements are streamed column by column from top to bottom.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1,2},{3,4}});
     * FloatStream stream = matrix.streamV(); // Stream of [1, 3, 2, 4]
     * }</pre>
     * 
     * @return a FloatStream of all elements in column-major order
     */
    @Override
    @Beta
    public FloatStream streamV() {
        return streamV(0, cols);
    }

    /**
     * Returns a stream of elements from a single column.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatStream colStream = matrix.streamV(0); // Stream of first column
     * }</pre>
     * 
     * @param columnIndex the index of the column to stream
     * @return a FloatStream of elements from the specified column
     */
    @Override
    public FloatStream streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * Returns a stream of elements from a range of columns in column-major order.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatStream stream = matrix.streamV(1, 3); // Stream columns 1 and 2
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a FloatStream of elements from the specified column range
     * @throws IndexOutOfBoundsException if indices are out of bounds
     */
    @Override
    @Beta
    public FloatStream streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return FloatStream.empty();
        }

        return FloatStream.of(new FloatIteratorEx() {
            private int i = 0;
            private int j = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return j < toColumnIndex;
            }

            @Override
            public float nextFloat() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final float result = a[i++][j];

                if (i >= rows) {
                    i = 0;
                    j++;
                }

                return result;
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                if (n >= (long) (toColumnIndex - j) * FloatMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    i += (int) ((n + i) % FloatMatrix.this.rows);
                    j += (int) ((n + i) / FloatMatrix.this.rows);
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public float[] toArray() {
                final int len = (int) count();
                final float[] c = new float[len];

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
     * Returns a stream where each element is a FloatStream representing a row.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Stream<FloatStream> rowStreams = matrix.streamR();
     * rowStreams.forEach(row -> row.forEach(System.out::println));
     * }</pre>
     * 
     * @return a Stream of FloatStream, one for each row
     */
    @Override
    public Stream<FloatStream> streamR() {
        return streamR(0, rows);
    }

    /**
     * Returns a stream of FloatStream for a range of rows.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Stream<FloatStream> rowStreams = matrix.streamR(1, 3); // Stream rows 1 and 2
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a Stream of FloatStream for the specified row range
     * @throws IndexOutOfBoundsException if indices are out of bounds
     */
    @Override
    public Stream<FloatStream> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
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
            public FloatStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return FloatStream.of(a[cursor++]);
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
     * Returns a stream where each element is a FloatStream representing a column.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Stream<FloatStream> colStreams = matrix.streamC();
     * colStreams.forEach(col -> col.forEach(System.out::println));
     * }</pre>
     * 
     * @return a Stream of FloatStream, one for each column
     */
    @Override
    @Beta
    public Stream<FloatStream> streamC() {
        return streamC(0, cols);
    }

    /**
     * Returns a stream of FloatStream for a range of columns.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Stream<FloatStream> colStreams = matrix.streamC(1, 3); // Stream columns 1 and 2
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a Stream of FloatStream for the specified column range
     * @throws IndexOutOfBoundsException if indices are out of bounds
     */
    @Override
    @Beta
    public Stream<FloatStream> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public FloatStream next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return FloatStream.of(new FloatIteratorEx() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rows;
                    private int cursor2 = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor2 < toIndex2;
                    }

                    @Override
                    public float nextFloat() {
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
     * Returns the length of the specified array, or 0 if null.
     * This is an internal helper method.
     * 
     * @param a the array to check
     * @return the length of the array, or 0 if null
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final float[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * Applies a consumer function to each element in the matrix.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.forEach(value -> System.out.println(value));
     * }</pre>
     * 
     * @param <E> the type of exception that the action may throw
     * @param action the action to perform on each element
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> action) throws E {
        forEach(0, rows, 0, cols, action);
    }

    /**
     * Applies a consumer function to elements in a specified submatrix region.
     * The operation may be performed in parallel for large regions.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.forEach(0, 2, 0, 2, value -> System.out.println(value)); // Process 2x2 submatrix
     * }</pre>
     * 
     * @param <E> the type of exception that the action may throw
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to perform on each element
     * @throws IndexOutOfBoundsException if indices are out of bounds
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.FloatConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final float[] aa = a[i];

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
     * FloatMatrix matrix = FloatMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
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
     * The hash code is computed based on the matrix elements.
     * 
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return N.deepHashCode(a);
    }

    /**
     * Compares this matrix with another object for equality.
     * Two matrices are equal if they have the same dimensions and equal elements at corresponding positions.
     * 
     * @param obj the object to compare with
     * @return true if the matrices are equal, false otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof final FloatMatrix another) {
            return cols == another.cols && rows == another.rows && N.deepEquals(a, another.a);
        }

        return false;
    }

    /**
     * Returns a string representation of the matrix.
     * The format shows the 2D array structure with nested brackets.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][]{{1, 2}, {3, 4}});
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