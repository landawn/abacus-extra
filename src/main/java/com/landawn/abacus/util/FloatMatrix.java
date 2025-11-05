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
 * <p>The matrix is stored internally as a two-dimensional float array (float[][]) and provides
 * methods for element access, manipulation, and various matrix operations such as
 * transpose, rotation, multiplication, and more.</p>
 * 
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
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
     * Constructs a FloatMatrix from a two-dimensional float array.
     * If the input array is null, an empty matrix (0x0) is created.
     *
     * <p><b>Important:</b> The array is used directly without copying. Modifications to the input array
     * after construction will affect the matrix, and vice versa. If you need an independent copy,
     * use {@link #copy()} after construction.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] data = {{1.0f, 2.0f}, {3.0f, 4.0f}};
     * FloatMatrix matrix = new FloatMatrix(data);
     * // Modifying data[0][0] will also modify matrix.get(0, 0)
     * }</pre>
     *
     * @param a the two-dimensional float array to wrap as a matrix. Can be null, which creates an empty matrix.
     */
    public FloatMatrix(final float[][] a) {
        super(a == null ? new float[0][0] : a);
    }

    /**
     * Creates an empty matrix with zero rows and zero columns.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.empty();
     * // matrix.rows returns 0
     * // matrix.cols returns 0
     * }</pre>
     *
     * @return an empty float matrix
     */
    public static FloatMatrix empty() {
        return EMPTY_FLOAT_MATRIX;
    }

    /**
     * Creates a FloatMatrix from a two-dimensional float array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * // matrix.get(0, 1) returns 2.0f
     * }</pre>
     *
     * @param a the two-dimensional float array to create the matrix from, or null/empty for an empty matrix
     * @return a new FloatMatrix containing the provided data, or an empty FloatMatrix if input is null or empty
     */
    public static FloatMatrix of(final float[]... a) {
        return N.isEmpty(a) ? EMPTY_FLOAT_MATRIX : new FloatMatrix(a);
    }

    /**
     * Creates a FloatMatrix from a two-dimensional int array by converting int values to float.
     * Each int value is converted to its equivalent float representation.
     *
     * <p><b>Requirements:</b></p>
     * <ul>
     *   <li>All rows must have the same length as the first row (rectangular array required)</li>
     *   <li>The first row cannot be null if the array is non-empty</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.create(new int[][] {{1, 2}, {3, 4}});
     * // Creates a matrix with values {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * assert matrix.get(1, 0) == 3.0f;
     * assert matrix.rows == 2 && matrix.cols == 2;
     * }</pre>
     *
     * @param a the two-dimensional int array to convert to a float matrix, or null/empty for an empty matrix
     * @return a new FloatMatrix with converted values, or an empty FloatMatrix if input is null or empty
     * @throws IllegalArgumentException if the first row is null or if rows have different lengths (non-rectangular array)
     */
    public static FloatMatrix create(final int[]... a) {
        if (N.isEmpty(a)) {
            return EMPTY_FLOAT_MATRIX;
        }

        N.checkArgument(a[0] != null, "First row cannot be null");

        final int cols = a[0].length;

        // Validate all rows have the same length
        for (int i = 1; i < a.length; i++) {
            N.checkArgument(a[i] != null && a[i].length == cols, "All rows must have the same length. Row 0 has length %s but row %s has length %s", cols, i,
                    a[i] == null ? 0 : a[i].length);
        }

        final float[][] c = new float[a.length][cols];

        for (int i = 0, len = a.length; i < len; i++) {
            final int[] aa = a[i];
            final float[] cc = c[i];

            for (int j = 0; j < cols; j++) {
                cc[j] = aa[j]; // NOSONAR
            }
        }

        return new FloatMatrix(c);
    }

    /**
     * Creates a 1-row matrix filled with random float values.
     * Each element is a random float value generated using the default random number generator.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.random(5);
     * // Creates a 1x5 matrix with random float values
     * assert matrix.rows == 1;
     * assert matrix.cols == 5;
     * }</pre>
     *
     * @param len the number of columns in the resulting matrix (must be non-negative)
     * @return a 1-row matrix filled with random float values
     * @throws IllegalArgumentException if len is negative
     */
    @SuppressWarnings("deprecation")
    public static FloatMatrix random(final int len) {
        return new FloatMatrix(new float[][] { FloatList.random(len).array() });
    }

    /**
     * Creates a 1-row matrix with all elements set to the specified value.
     * This is useful for initializing matrices with constant values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.repeat(3.14f, 5);
     * // Creates a 1x5 matrix where all elements are 3.14f
     * assert matrix.rows == 1;
     * assert matrix.cols == 5;
     * assert matrix.get(0, 0) == 3.14f;
     * assert matrix.get(0, 4) == 3.14f;
     * }</pre>
     *
     * @param val the value to repeat for all elements
     * @param len the number of columns in the resulting matrix (must be non-negative)
     * @return a 1-row matrix with all elements set to val
     * @throws IllegalArgumentException if len is negative
     */
    public static FloatMatrix repeat(final float val, final int len) {
        return new FloatMatrix(new float[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a square matrix from the specified main diagonal elements (left-upper to right-down).
     * All other elements are set to zero. The resulting matrix has dimensions n×n where n is the length
     * of the diagonal array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.diagonalLU2RD(new float[] {1.0f, 2.0f, 3.0f});
     * // Creates 3x3 matrix:
     * // [[1.0, 0.0, 0.0],
     * //  [0.0, 2.0, 0.0],
     * //  [0.0, 0.0, 3.0]]
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements, or null/empty for an empty matrix
     * @return a square matrix with the specified main diagonal, or an empty matrix if input is null or empty
     */
    public static FloatMatrix diagonalLU2RD(final float[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square matrix from the specified anti-diagonal elements (right-upper to left-down).
     * All other elements are set to zero. The resulting matrix has dimensions n×n where n is the length
     * of the diagonal array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.diagonalRU2LD(new float[] {1.0f, 2.0f, 3.0f});
     * // Creates 3x3 matrix:
     * // [[0.0, 0.0, 1.0],
     * //  [0.0, 2.0, 0.0],
     * //  [3.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements, or null/empty for an empty matrix
     * @return a square matrix with the specified anti-diagonal, or an empty matrix if input is null or empty
     */
    public static FloatMatrix diagonalRU2LD(final float[] rightUp2LeftDownDiagonal) {
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
     * FloatMatrix matrix = FloatMatrix.diagonal(new float[] { 1.0, 2.0, 3.0 }, new float[] { 4.0, 5.0, 6.0 });
     * // Creates 3x3 matrix with both diagonals set
     * // Resulting matrix: 
     * //   {1.0, 0, 4.0},
     * //   {0, 2.0, 0},
     * //   {6.0, 0, 3.0}
     *
     * }</pre>
     *
     * @param leftUp2RightDownDiagonal the array of main diagonal elements (can be null or empty)
     * @param rightUp2LeftDownDiagonal the array of anti-diagonal elements (can be null or empty)
     * @return a square matrix with the specified diagonals, or an empty matrix if both inputs are null or empty
     * @throws IllegalArgumentException if both arrays are non-empty and have different lengths
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

        return new FloatMatrix(c);
    }

    /**
     * Converts a boxed Float Matrix to a primitive FloatMatrix.
     * Null values in the input matrix are converted to 0.0f.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<Float> boxed = Matrix.of(new Float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
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
     * Returns the component type of the matrix elements, which is always {@code float.class}.
     * 
     * @return {@code float.class}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return float.class;
    }

    /**
     * Returns the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * float value = matrix.get(0, 1); // Returns 2.0f
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position (i, j)
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public float get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Returns the element at the specified point.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * Point point = Point.of(0, 1);
     * float value = matrix.get(point); // Returns 2.0f
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @return the float element at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #get(int, int)
     */
    public float get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * matrix.set(0, 1, 9.0f); // Sets element at row 0, column 1 to 9.0f
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
     * Sets the element at the specified point to the given value.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * Point point = Point.of(0, 1);
     * matrix.set(point, 9.0f);
     * assert matrix.get(point) == 9.0f;
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @param val the new float value to set at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #set(int, int, float)
     */
    public void set(final Point point, final float val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element directly above the specified position, if it exists.
     * If the specified position is in the first row (i == 0), returns an empty Optional.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * OptionalFloat value = matrix.upOf(1, 0); // Returns OptionalFloat.of(1.0f)
     * OptionalFloat empty = matrix.upOf(0, 0); // Returns OptionalFloat.empty() (no row above)
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalFloat containing the element at position (i-1, j), or empty if i == 0
     * @throws ArrayIndexOutOfBoundsException if j is out of bounds
     */
    public OptionalFloat upOf(final int i, final int j) {
        return i == 0 ? OptionalFloat.empty() : OptionalFloat.of(a[i - 1][j]);
    }

    /**
     * Returns the element directly below the specified position, if it exists.
     * If the specified position is in the last row, returns an empty Optional.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * OptionalFloat value = matrix.downOf(0, 0); // Returns OptionalFloat.of(3.0f)
     * OptionalFloat empty = matrix.downOf(1, 0); // Returns OptionalFloat.empty() (no row below)
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalFloat containing the element at position (i+1, j), or empty if i == rows-1
     * @throws ArrayIndexOutOfBoundsException if j is out of bounds
     */
    public OptionalFloat downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalFloat.empty() : OptionalFloat.of(a[i + 1][j]);
    }

    /**
     * Returns the element directly to the left of the specified position, if it exists.
     * If the specified position is in the first column (j == 0), returns an empty Optional.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * OptionalFloat value = matrix.leftOf(0, 1); // Returns OptionalFloat.of(1.0f)
     * OptionalFloat empty = matrix.leftOf(0, 0); // Returns OptionalFloat.empty() (no column to left)
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalFloat containing the element at position (i, j-1), or empty if j == 0
     */
    public OptionalFloat leftOf(final int i, final int j) {
        return j == 0 ? OptionalFloat.empty() : OptionalFloat.of(a[i][j - 1]);
    }

    /**
     * Returns the element directly to the right of the specified position, if it exists.
     * If the specified position is in the last column, returns an empty Optional.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * OptionalFloat value = matrix.rightOf(0, 0); // Returns OptionalFloat.of(2.0f)
     * OptionalFloat empty = matrix.rightOf(0, 1); // Returns OptionalFloat.empty() (no column to right)
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return an OptionalFloat containing the element at position (i, j+1), or empty if j == cols-1
     */
    public OptionalFloat rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalFloat.empty() : OptionalFloat.of(a[i][j + 1]);
    }

    /**
     * Returns the specified row as an array.
     *
     * <p><b>Important:</b> This method returns a reference to the internal array, not a copy.
     * Modifications to the returned array will affect the matrix. If you need an independent
     * copy, use {@code row(rowIndex).clone()}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}});
     * float[] firstRow = matrix.row(0); // Returns [1.0f, 2.0f, 3.0f]
     * firstRow[0] = 99.0f; // This modifies the matrix
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return a reference to the internal row array (not a copy)
     * @throws IllegalArgumentException if rowIndex &lt; 0 or rowIndex &gt;= rows
     */
    public float[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as a new array.
     *
     * <p>Unlike {@link #row(int)}, this method always returns a new array copy since
     * columns are not stored contiguously in memory. Modifications to the returned array
     * will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}});
     * float[] firstColumn = matrix.column(0); // Returns [1.0f, 4.0f]
     * firstColumn[0] = 99.0f; // This does NOT modify the matrix
     * }</pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing a copy of the specified column
     * @throws IllegalArgumentException if columnIndex &lt; 0 or columnIndex &gt;= cols
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
     * Sets the values of the specified row by copying from the provided array.
     * All elements in the row are replaced with values from the provided array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}});
     * matrix.setRow(0, new float[] {7.0f, 8.0f, 9.0f}); // First row is now [7.0f, 8.0f, 9.0f]
     * assert matrix.get(0, 0) == 7.0f;
     * assert matrix.get(0, 2) == 9.0f;
     * }</pre>
     *
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to copy into the row; must have length equal to the number of columns
     * @throws IllegalArgumentException if row.length != cols
     * @throws ArrayIndexOutOfBoundsException if rowIndex is out of bounds or row is null
     */
    public void setRow(final int rowIndex, final float[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Sets the values of the specified column by copying from the provided array.
     * All elements in the column are replaced with values from the provided array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}});
     * matrix.setColumn(0, new float[] {7.0f, 8.0f}); // First column is now [7.0f, 8.0f]
     * assert matrix.get(0, 0) == 7.0f;
     * assert matrix.get(1, 0) == 8.0f;
     * }</pre>
     *
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to copy into the column; must have length equal to the number of rows
     * @throws IllegalArgumentException if column.length != rows
     * @throws ArrayIndexOutOfBoundsException if columnIndex is out of bounds or column is null
     */
    public void setColumn(final int columnIndex, final float[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all elements in a row by applying the specified function to each element.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.updateRow(0, x -> x * 2); // Doubles all values in the first row
     * matrix.updateRow(1, x -> x + 10); // Adds 10 to all values in the second row
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param rowIndex the index of the row to update (0-based)
     * @param func the function to apply to each element in the row
     * @throws ArrayIndexOutOfBoundsException if rowIndex is out of bounds
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.FloatUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsFloat(a[rowIndex][i]);
        }
    }

    /**
     * Updates all elements in a column by applying the specified function to each element.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.updateColumn(0, x -> x + 1); // Adds 1 to all values in the first column
     * matrix.updateColumn(2, x -> Math.abs(x)); // Takes absolute value of all values in third column
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param columnIndex the index of the column to update (0-based)
     * @param func the function to apply to each element in the column
     * @throws ArrayIndexOutOfBoundsException if columnIndex is out of bounds
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.FloatUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsFloat(a[i][columnIndex]);
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
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}, {7.0f, 8.0f, 9.0f}});
     * float[] diagonal = matrix.getLU2RD(); // Returns [1.0f, 5.0f, 9.0f]
     * }</pre>
     *
     * @return a new float array containing the main diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     */
    public float[] getLU2RD() throws IllegalStateException {
        checkIfRowAndColumnSizeAreSame();

        final float[] res = new float[rows];

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
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * matrix.setLU2RD(new float[] {9.0f, 8.0f});
     * // Diagonal is now [9.0f, 8.0f]
     * }</pre>
     *
     * @param diagonal the new values for the main diagonal; must have length &gt;= rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length does not equal to rows
     */
    public void setLU2RD(final float[] diagonal) throws IllegalStateException, IllegalArgumentException {
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
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateLU2RD(final Throwables.FloatUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsFloat(a[i][i]);
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
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}, {7.0f, 8.0f, 9.0f}});
     * float[] diagonal = matrix.getRU2LD(); // Returns [3.0f, 5.0f, 7.0f]
     * }</pre>
     *
     * @return a new float array containing the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != columns)
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
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * matrix.setRU2LD(new float[] {9.0f, 8.0f});
     * // Anti-diagonal is now [9.0f, 8.0f]
     * }</pre>
     *
     * @param diagonal the new values for the anti-diagonal; must have length &gt;= rows
     * @throws IllegalStateException if the matrix is not square (rows != columns)
     * @throws IllegalArgumentException if diagonal array length &lt; rows
     */
    public void setRU2LD(final float[] diagonal) throws IllegalStateException, IllegalArgumentException {
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
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
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
     * <p><b>Usage Examples:</b></p>
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
     * The function receives the row index (i) and column index (j) and returns the new value.
     * The operation may be performed in parallel for large matrices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.updateAll((i, j) -> i + j); // Sets element at [i][j] to i + j
     * matrix.updateAll((i, j) -> i == j ? 1.0f : 0.0f); // Creates identity matrix
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
     * <p><b>Usage Examples:</b></p>
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
     * <p><b>Usage Examples:</b></p>
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
     * <p><b>Usage Examples:</b></p>
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
     * The operation may be performed in parallel for large matrices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<String> stringMatrix = matrix.mapToObj(x -> String.format("%.2f", x), String.class);
     * Matrix<BigDecimal> decimalMatrix = matrix.mapToObj(BigDecimal::valueOf, BigDecimal.class);
     * }</pre>
     *
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the type of exception that the function may throw
     * @param func the function to convert float values to type T
     * @param targetElementType the Class object for type T (required for array creation)
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
     * <p><b>Usage Examples:</b></p>
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
     * Fills the matrix with values from another two-dimensional array, starting from the top-left corner.
     * If the source array is larger than the matrix, only the fitting portion is copied.
     * If the source array is smaller, only the available values are copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] data = {{1.0f, 2.0f}, {3.0f, 4.0f}};
     * matrix.fill(data); // Fills from position (0,0)
     * }</pre>
     *
     * @param b the two-dimensional array to copy values from
     */
    public void fill(final float[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from another two-dimensional array.
     * The filling starts at the specified position and copies as much as will fit.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] patch = {{1, 2}, {3, 4}};
     * matrix.fill(1, 1, patch); // Fills starting at row 1, column 1
     * }</pre>
     * 
     * @param fromRowIndex the starting row index for filling
     * @param fromColumnIndex the starting column index for filling
     * @param b the source array to copy values from
     * @throws IllegalArgumentException if the starting indices are negative or exceed matrix dimensions
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final float[][] b) throws IllegalArgumentException {
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
     * FloatMatrix original = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * FloatMatrix copy = original.copy();
     * // copy is independent from original
     * }</pre>
     *
     * @return a new matrix that is a copy of this matrix
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
     * Creates a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows and is completely independent from the original matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix subset = matrix.copy(1, 3); // Copies rows 1 and 2 (exclusive end)
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix submatrix = matrix.copy(0, 2, 1, 3); // Copies rows 0-1, columns 1-2
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
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
     * Creates a new matrix by extending or truncating this matrix to the specified dimensions.
     * New cells are filled with {@code 0.0f}.
     *
     * <p>If the new dimensions are smaller than the current dimensions, the matrix is truncated.
     * If larger, the existing content is preserved in the top-left corner and new cells are filled with 0.0f.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * FloatMatrix extended = matrix.extend(3, 3);
     * // Result: [[1.0, 2.0, 0.0],
     * //          [3.0, 4.0, 0.0],
     * //          [0.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param newRows the number of rows in the new matrix. It can smaller than the row number of current maxtrix but must be non-negative
     * @param newCols the number of columns in the new matrix. It can smaller than the column number of current maxtrix but must be non-negative
     * @return a new FloatMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRows} or {@code newCols} is negative
     */
    public FloatMatrix extend(final int newRows, final int newCols) {
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
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * FloatMatrix extended = matrix.extend(3, 4, 9.0f); // Extend to 3x4, fill new cells with 9.0f
     * // Result: [[1.0, 2.0, 9.0, 9.0],
     * //          [3.0, 4.0, 9.0, 9.0],
     * //          [9.0, 9.0, 9.0, 9.0]]
     *
     * // Truncate to smaller size
     * FloatMatrix truncated = matrix.extend(1, 1, 0.0f); // Keep only top-left element
     * // Result: [[1.0]]
     * }</pre>
     *
     * @param newRows the number of rows in the new matrix. It can smaller than the row number of current maxtrix but must be non-negative
     * @param newCols the number of columns in the new matrix. It can smaller than the column number of current maxtrix but must be non-negative
     * @param defaultValueForNewCell the float value to fill new cells with during extension
     * @return a new FloatMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRows} or {@code newCols} is negative,
     *         or if the resulting matrix would be too large (dimensions exceeding Integer.MAX_VALUE elements)
     */
    public FloatMatrix extend(final int newRows, final int newCols, final float defaultValueForNewCell) throws IllegalArgumentException {
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
     * Creates a new matrix by extending this matrix in all four directions.
     * New cells are filled with {@code 0.0f}.
     *
     * <p>This method adds padding around the existing matrix, with the original content
     * positioned according to the specified padding amounts.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}});
     * FloatMatrix extended = matrix.extend(1, 1, 1, 1);
     * // Result: [[0.0, 0.0, 0.0, 0.0],
     * //          [0.0, 1.0, 2.0, 0.0],
     * //          [0.0, 0.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param toUp number of rows to add above; must be non-negative
     * @param toDown number of rows to add below; must be non-negative
     * @param toLeft number of columns to add to the left; must be non-negative
     * @param toRight number of columns to add to the right; must be non-negative
     * @return a new extended FloatMatrix with dimensions ((toUp+rows+toDown) x (toLeft+cols+toRight))
     * @throws IllegalArgumentException if any parameter is negative
     */
    public FloatMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
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
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}});
     * FloatMatrix padded = matrix.extend(1, 1, 2, 2, 9.0f);
     * // Result: [[9.0, 9.0, 9.0, 9.0, 9.0, 9.0],
     * //          [9.0, 9.0, 1.0, 2.0, 9.0, 9.0],
     * //          [9.0, 9.0, 9.0, 9.0, 9.0, 9.0]]
     *
     * // Add border of 0.0f values
     * FloatMatrix bordered = matrix.extend(1, 1, 1, 1, 0.0f);
     * // Result: [[0.0, 0.0, 0.0, 0.0],
     * //          [0.0, 1.0, 2.0, 0.0],
     * //          [0.0, 0.0, 0.0, 0.0]]
     * }</pre>
     *
     * @param toUp number of rows to add above; must be non-negative
     * @param toDown number of rows to add below; must be non-negative
     * @param toLeft number of columns to add to the left; must be non-negative
     * @param toRight number of columns to add to the right; must be non-negative
     * @param defaultValueForNewCell the float value to fill all new cells with
     * @return a new extended FloatMatrix with dimensions ((toUp+rows+toDown) x (toLeft+cols+toRight))
     * @throws IllegalArgumentException if any padding parameter is negative,
     *         or if the resulting dimensions would exceed Integer.MAX_VALUE
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
            if ((long) toUp + rows + toDown > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Result would have too many rows: " + toUp + " + " + rows + " + " + toDown);
            }

            if ((long) toLeft + cols + toRight > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Result would have too many columns: " + toLeft + " + " + cols + " + " + toRight);
            }

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
     * This method modifies the matrix directly. For a non-destructive version, use {@link #flipH()}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.reverseH(); // [[1,2,3]] becomes [[3,2,1]] (modifies original)
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
     * This method modifies the matrix directly. For a non-destructive version, use {@link #flipV()}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.reverseV(); // [[1],[2],[3]] becomes [[3],[2],[1]] (modifies original)
     * }</pre>
     *
     * @see #flipV()
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
     * <p><b>Usage Examples:</b></p>
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
     * <p><b>Usage Examples:</b></p>
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
     * Returns a new matrix rotated 90 degrees clockwise.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * FloatMatrix rotated = matrix.rotate90();
     * // rotated is {{3.0, 1.0}, {4.0, 2.0}}
     * }</pre>
     *
     * @return a new matrix rotated 90 degrees clockwise
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
     * Returns a new matrix rotated 180 degrees.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * FloatMatrix rotated = matrix.rotate180();
     * // rotated is {{4.0, 3.0}, {2.0, 1.0}}
     * }</pre>
     *
     * @return a new matrix rotated 180 degrees
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
     * Returns a new matrix rotated 270 degrees clockwise (or 90 degrees counter-clockwise).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * FloatMatrix rotated = matrix.rotate270();
     * // rotated is {{2.0, 4.0}, {1.0, 3.0}}
     * }</pre>
     *
     * @return a new matrix rotated 270 degrees clockwise
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
     * Creates the transpose of this matrix by swapping rows and columns.
     * The transpose operation converts each row into a column, so element at position (i, j)
     * in the original matrix appears at position (j, i) in the transposed matrix. The resulting
     * matrix has dimensions swapped (rows × cols becomes cols × rows).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:      Transposed:
     * // 1.0f 2.0f 3.0f  1.0f 4.0f
     * // 4.0f 5.0f 6.0f  2.0f 5.0f
     * //                 3.0f 6.0f
     *
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}});
     * FloatMatrix transposed = matrix.transpose(); // 2×3 becomes 3×2
     * }</pre>
     *
     * @return a new matrix that is the transpose of this matrix with dimensions cols × rows
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
     * Reshapes the matrix to new dimensions while preserving element order.
     * Elements are read in row-major order from the original matrix and placed into the new shape.
     *
     * <p>If the new shape has fewer total elements than the original, excess elements are truncated.
     * If the new shape has more total elements, the additional positions are filled with zeros.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}});
     * FloatMatrix reshaped = matrix.reshape(3, 2); // Becomes [[1.0f, 2.0f], [3.0f, 4.0f], [5.0f, 6.0f]]
     * FloatMatrix extended = matrix.reshape(2, 4); // Becomes [[1.0f, 2.0f, 3.0f, 4.0f], [5.0f, 6.0f, 0.0f, 0.0f]]
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1,2}});
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

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many rows: " + rows + " * " + rowRepeats);
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many columns: " + cols + " * " + colRepeats);
        }

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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1,2},{3,4}});
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

        // Check for overflow before allocation
        if ((long) rows * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many rows: " + rows + " * " + rowRepeats);
        }
        if ((long) cols * colRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result would have too many columns: " + cols + " * " + colRepeats);
        }

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
     * Returns a list containing all matrix elements in row-major order.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * FloatList list = matrix.flatten(); // Returns FloatList of 1.0f, 2.0f, 3.0f, 4.0f
     * }</pre>
     *
     * @return a list of all elements in row-major order
     */
    @Override
    public FloatList flatten() {
        // Check for overflow before allocation
        if ((long) rows * cols > Integer.MAX_VALUE) {
            throw new IllegalStateException("Matrix too large to flatten: " + rows + " x " + cols);
        }

        final float[] c = new float[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return FloatList.of(c);
    }

    /**
     * Applies an operation to each row array of the matrix.
     * This is useful for operations that work on entire row arrays at once,
     * such as sorting or reversing rows.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.flatOp(row -> N.sort(row)); // Sorts each row independently
     * }</pre>
     *
     * @param <E> the type of exception that the operation may throw
     * @param op the operation to apply to each row array
     * @throws E if the operation throws an exception
     * @see Arrays#flatOp(float[][], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super float[], E> op) throws E {
        Arrays.flatOp(a, op);
    }

    /**
     * Stacks this matrix vertically with another matrix.
     * The matrices must have the same number of columns.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix a = FloatMatrix.of(new float[][] {{1,2},{3,4}});
     * FloatMatrix b = FloatMatrix.of(new float[][] {{5,6},{7,8}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix a = FloatMatrix.of(new float[][] {{1,2},{3,4}});
     * FloatMatrix b = FloatMatrix.of(new float[][] {{5,6},{7,8}});
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
     * Performs element-wise addition of this matrix with another matrix.
     * The matrices must have the same dimensions (same number of rows and columns).
     *
     * <p>For large matrices (8192+ elements), this operation may be parallelized automatically
     * for better performance.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix a = FloatMatrix.of(new float[][] {{1,2},{3,4}});
     * FloatMatrix b = FloatMatrix.of(new float[][] {{5,6},{7,8}});
     * FloatMatrix sum = a.add(b); // Result: [[6,8],[10,12]]
     * }</pre>
     *
     * @param b the matrix to add to this matrix
     * @return a new FloatMatrix containing the element-wise sum (same dimensions as inputs)
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
     * Performs element-wise subtraction of another matrix from this matrix.
     * The matrices must have the same dimensions (same number of rows and columns).
     *
     * <p>For large matrices (8192+ elements), this operation may be parallelized automatically
     * for better performance.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix a = FloatMatrix.of(new float[][] {{5,6},{7,8}});
     * FloatMatrix b = FloatMatrix.of(new float[][] {{1,2},{3,4}});
     * FloatMatrix diff = a.subtract(b); // Result: [[4,4],[4,4]]
     * }</pre>
     *
     * @param b the matrix to subtract from this matrix
     * @return a new FloatMatrix containing the element-wise difference (same dimensions as inputs)
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
     * Performs matrix multiplication of this matrix with another matrix.
     * The number of columns in this matrix must equal the number of rows in the other matrix.
     * The resulting matrix has dimensions (this.rows × b.cols).
     *
     * <p>This operation uses standard matrix multiplication where each element (i,j) in the result
     * is computed as the dot product of row i from this matrix and column j from the other matrix.</p>
     *
     * <p>For large matrices, this operation may be parallelized automatically for better performance.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix a = FloatMatrix.of(new float[][] {{1,2},{3,4}});
     * FloatMatrix b = FloatMatrix.of(new float[][] {{5,6},{7,8}});
     * FloatMatrix product = a.multiply(b); // Result: [[19,22],[43,50]]
     * }</pre>
     *
     * @param b the matrix to multiply with this matrix
     * @return a new FloatMatrix containing the matrix product with dimensions (this.rows × b.cols)
     * @throws IllegalArgumentException if the matrix dimensions are incompatible for multiplication
     *         (i.e., this.cols != b.rows)
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix primitive = FloatMatrix.of(new float[][] {{1.0f, 2.0f}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix floatMatrix = FloatMatrix.of(new float[][] {{1.5f, 2.5f}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix result = matrix1.zipWith(matrix2, (a, b) -> a * b); // Element-wise multiplication
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix result = matrix1.zipWith(matrix2, matrix3, (a, b, c) -> a + b + c);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix
     * @param matrixC the third matrix
     * @param zipFunction the ternary operator to apply element-wise
     * @return a new FloatMatrix with the results of the element-wise operation
     * @throws IllegalArgumentException if the matrices have different dimensions
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1,2,3},{4,5,6},{7,8,9}});
     * FloatStream diagonal = matrix.streamLU2RD(); // Stream of [1, 5, 9]
     * }</pre>
     * 
     * @return a FloatStream of diagonal elements
     * @throws IllegalStateException if the matrix is not square
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
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1,2,3},{4,5,6},{7,8,9}});
     * FloatStream antiDiagonal = matrix.streamRU2LD(); // Stream of [3, 5, 7]
     * }</pre>
     * 
     * @return a FloatStream of anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square
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
     * Elements are streamed row by row from left to right.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1,2},{3,4}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatStream rowStream = matrix.streamH(0); // Stream of first row
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @return a FloatStream of elements from the specified row
     */
    @Override
    public FloatStream streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * Returns a stream of elements from a range of rows in row-major order.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatStream stream = matrix.streamH(1, 3); // Stream rows 1 and 2
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1,2},{3,4}});
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatStream colStream = matrix.streamV(0); // Stream of first column
     * }</pre>
     *
     * @param columnIndex the column index (0-based)
     * @return a FloatStream of elements from the specified column
     */
    @Override
    public FloatStream streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * Returns a stream of elements from a range of columns in column-major order.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatStream stream = matrix.streamV(1, 3); // Stream columns 1 and 2
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
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
            public void advance(final long n) {
                if (n <= 0) {
                    return;
                }

                if (n >= (long) (toColumnIndex - j) * FloatMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    final int offset = (int) (n + i);
                    i = offset % FloatMatrix.this.rows;
                    j += offset / FloatMatrix.this.rows;
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
     * This allows processing the matrix row by row with stream operations.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Stream<FloatStream> rowStreams = matrix.streamR();
     * rowStreams.forEach(row -> System.out.println(row.sum())); // Print sum of each row
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Stream<FloatStream> rowStreams = matrix.streamR(1, 3); // Stream rows 1 and 2
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
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
     * Returns a stream where each element is a FloatStream representing a column.
     * This allows processing the matrix column by column with stream operations.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Stream<FloatStream> colStreams = matrix.streamC();
     * colStreams.forEach(col -> System.out.println(col.max())); // Print max of each column
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Stream<FloatStream> colStreams = matrix.streamC(1, 3); // Stream columns 1 and 2
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
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
     * Returns the length of the specified array, or 0 if null.
     * This is an internal helper method used by the AbstractMatrix base class for
     * determining array lengths safely without null pointer exceptions.
     *
     * @param a the float array to check (can be null)
     * @return the length of the array, or 0 if the array is null
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final float[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * Applies a consumer function to each element in the matrix.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p><b>Usage Examples:</b></p>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.forEach(0, 2, 0, 2, value -> System.out.println(value)); // Process 2x2 submatrix
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
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
     * and enclosed in square brackets.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}});
     * matrix.println();
     * // Output:
     * // [1.0, 2.0, 3.0]
     * // [4.0, 5.0, 6.0]
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

                    final float[] row = a[i];
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
     * Returns {@code true} if the given object is also a FloatMatrix with the same dimensions
     * and all corresponding elements are equal.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatMatrix m1 = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * FloatMatrix m2 = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
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

        if (obj instanceof final FloatMatrix another) {
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
     * FloatMatrix matrix = FloatMatrix.of(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}});
     * System.out.println(matrix.toString()); // [[1.0, 2.0], [3.0, 4.0]]
     * }</pre>
     *
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}