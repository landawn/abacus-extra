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
import com.landawn.abacus.util.u.OptionalBoolean;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

/**
 * A matrix implementation for boolean primitive values, providing efficient storage and operations
 * for two-dimensional boolean arrays. This class extends AbstractMatrix and provides specialized
 * methods for boolean matrix manipulation including mathematical operations, transformations,
 * and element access.
 * 
 * <p>The matrix is internally represented as a 2D boolean array (boolean[][]) and supports
 * various operations such as:
 * <ul>
 *   <li>Element access and modification</li>
 *   <li>Matrix transformations (transpose, rotate, flip)</li>
 *   <li>Matrix reshaping and resizing</li>
 *   <li>Stream operations for rows, columns, and diagonals</li>
 *   <li>Functional operations (map, filter, forEach)</li>
 * </ul>
 * 
 * <p>Usage example:
 * <pre>
 * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
 * boolean value = matrix.get(0, 1); // false
 * matrix.set(1, 0, true); // Set element at row 1, column 0 to true
 * </pre>
 * 
 * @see AbstractMatrix
 * @see Matrix
 */
public final class BooleanMatrix extends AbstractMatrix<boolean[], BooleanList, Stream<Boolean>, Stream<Stream<Boolean>>, BooleanMatrix> {

    static final BooleanMatrix EMPTY_BOOLEAN_MATRIX = new BooleanMatrix(new boolean[0][0]);

    /**
     * Constructs a new BooleanMatrix with the specified 2D boolean array.
     * If the input array is null, an empty matrix (0x0) is created.
     *
     * @param a the 2D boolean array to initialize the matrix with, or null for an empty matrix
     */
    public BooleanMatrix(final boolean[][] a) {
        super(a == null ? new boolean[0][0] : a);
    }

    /**
     * Returns an empty BooleanMatrix instance (0x0 matrix).
     * This method returns a shared immutable empty matrix instance for memory efficiency.
     *
     * @return an empty BooleanMatrix with dimensions 0x0
     */
    public static BooleanMatrix empty() {
        return EMPTY_BOOLEAN_MATRIX;
    }

    /**
     * Creates a BooleanMatrix from the specified 2D boolean array.
     * This is a convenience factory method that handles null and empty inputs gracefully.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * </pre>
     *
     * @param a the 2D boolean array to create the matrix from
     * @return a new BooleanMatrix containing the provided data, or an empty matrix if input is null or empty
     */
    public static BooleanMatrix of(final boolean[]... a) {
        return N.isEmpty(a) ? EMPTY_BOOLEAN_MATRIX : new BooleanMatrix(a);
    }

    /**
     * Creates a 1-row BooleanMatrix with random boolean values.
     * The values are generated using BooleanList.random(len).
     *
     * @param len the number of columns (length) of the random matrix
     * @return a 1xlen BooleanMatrix with random boolean values
     */
    @SuppressWarnings("deprecation")
    public static BooleanMatrix random(final int len) {
        return new BooleanMatrix(new boolean[][] { BooleanList.random(len).array() });
    }

    /**
     * Creates a 1-row BooleanMatrix with all elements set to the specified value.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix matrix = BooleanMatrix.repeat(true, 5); // Creates [true, true, true, true, true]
     * </pre>
     *
     * @param val the boolean value to fill the matrix with
     * @param len the number of columns (length) of the matrix
     * @return a 1xlen BooleanMatrix with all elements set to val
     */
    public static BooleanMatrix repeat(final boolean val, final int len) {
        return new BooleanMatrix(new boolean[][] { Array.repeat(val, len) });
    }

    /**
     * Creates a diagonal matrix with the specified values on the diagonal from left-up to right-down.
     * All other elements are set to false.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix matrix = BooleanMatrix.diagonalLU2RD(new boolean[]{true, false, true});
     * // Creates: [[true, false, false],
     * //           [false, false, false],
     * //           [false, false, true]]
     * </pre>
     *
     * @param leftUp2RightDownDiagonal the values to place on the main diagonal
     * @return a square matrix with the specified diagonal values
     */
    public static BooleanMatrix diagonalLU2RD(final boolean[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a diagonal matrix with the specified values on the diagonal from right-up to left-down.
     * All other elements are set to false.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix matrix = BooleanMatrix.diagonalRU2LD(new boolean[]{true, false, true});
     * // Creates: [[false, false, true],
     * //           [false, false, false],
     * //           [true, false, false]]
     * </pre>
     *
     * @param rightUp2LeftDownDiagonal the values to place on the anti-diagonal
     * @return a square matrix with the specified anti-diagonal values
     */
    public static BooleanMatrix diagonalRU2LD(final boolean[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a diagonal matrix with values on both the main diagonal and anti-diagonal.
     * If both diagonal arrays are provided, they must have the same length.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix matrix = BooleanMatrix.diagonal(
     *     new boolean[]{true, true, true},    // main diagonal
     *     new boolean[]{false, true, false}   // anti-diagonal
     * // Creates:
     * // [[true, false, false],
     * //  [false, true, false],
     * //  [false, false, true]]
     * );
     * </pre>
     *
     * @param leftUp2RightDownDiagonal the values for the main diagonal (left-up to right-down), or null
     * @param rightUp2LeftDownDiagonal the values for the anti-diagonal (right-up to left-down), or null
     * @return a square matrix with the specified diagonal values
     * @throws IllegalArgumentException if both arrays are provided but have different lengths
     */
    public static BooleanMatrix diagonal(final boolean[] leftUp2RightDownDiagonal, final boolean[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        if (N.isEmpty(leftUp2RightDownDiagonal) && N.isEmpty(rightUp2LeftDownDiagonal)) {
            return EMPTY_BOOLEAN_MATRIX;
        }

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final boolean[][] c = new boolean[len][len];

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

        return new BooleanMatrix(c);
    }

    /**
     * Converts a boxed Boolean Matrix to a primitive BooleanMatrix.
     * Null values in the input matrix are converted to false.
     *
     * @param x the boxed Boolean Matrix to convert
     * @return a new BooleanMatrix with primitive boolean values
     */
    public static BooleanMatrix unbox(final Matrix<Boolean> x) {
        return BooleanMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of this matrix, which is boolean.class.
     *
     * @return boolean.class
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return boolean.class;
    }

    /**
     * Gets the boolean value at the specified row and column indices.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * boolean val = matrix.get(0, 1); // Returns false
     * </pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the boolean value at position (i, j)
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public boolean get(final int i, final int j) { // NOSONAR
        return a[i][j];
    }

    /**
     * Gets the boolean value at the specified point.
     *
     * @param point the Point object containing row and column indices
     * @return the boolean value at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public boolean get(final Point point) { // NOSONAR
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the boolean value at the specified row and column indices.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * matrix.set(0, 1, true); // Sets element at (0,1) to true
     * </pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the boolean value to set
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public void set(final int i, final int j, final boolean val) {
        a[i][j] = val;
    }

    /**
     * Sets the boolean value at the specified point.
     *
     * @param point the Point object containing row and column indices
     * @param val the boolean value to set
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     */
    public void set(final Point point, final boolean val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element above the specified position as an OptionalBoolean.
     * Returns empty if the position is in the first row.
     *
     * @param i the row index
     * @param j the column index
     * @return OptionalBoolean containing the element above, or empty if at top edge
     */
    public OptionalBoolean upOf(final int i, final int j) {
        return i == 0 ? OptionalBoolean.empty() : OptionalBoolean.of(a[i - 1][j]);
    }

    /**
     * Returns the element below the specified position as an OptionalBoolean.
     * Returns empty if the position is in the last row.
     *
     * @param i the row index
     * @param j the column index
     * @return OptionalBoolean containing the element below, or empty if at bottom edge
     */
    public OptionalBoolean downOf(final int i, final int j) {
        return i == rows - 1 ? OptionalBoolean.empty() : OptionalBoolean.of(a[i + 1][j]);
    }

    /**
     * Returns the element to the left of the specified position as an OptionalBoolean.
     * Returns empty if the position is in the first column.
     *
     * @param i the row index
     * @param j the column index
     * @return OptionalBoolean containing the element to the left, or empty if at left edge
     */
    public OptionalBoolean leftOf(final int i, final int j) {
        return j == 0 ? OptionalBoolean.empty() : OptionalBoolean.of(a[i][j - 1]);
    }

    /**
     * Returns the element to the right of the specified position as an OptionalBoolean.
     * Returns empty if the position is in the last column.
     *
     * @param i the row index
     * @param j the column index
     * @return OptionalBoolean containing the element to the right, or empty if at right edge
     */
    public OptionalBoolean rightOf(final int i, final int j) {
        return j == cols - 1 ? OptionalBoolean.empty() : OptionalBoolean.of(a[i][j + 1]);
    }

    /**
     * Returns a stream of the four adjacent points (up, right, down, left) of the specified position.
     * Points that would be outside the matrix bounds are returned as null.
     *
     * @param i the row index
     * @param j the column index
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
     * Returns a stream of the eight adjacent points of the specified position.
     * Points are returned in order: left-up, up, right-up, right, right-down, down, left-down, left.
     * Points that would be outside the matrix bounds are returned as null.
     *
     * @param i the row index
     * @param j the column index
     * @return a Stream of Points for all 8 adjacent positions (null for out-of-bounds positions)
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
     * Returns a copy of the specified row as a boolean array.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * boolean[] row = matrix.row(0); // Returns [true, false]
     * </pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return a boolean array containing the values of the specified row
     * @throws IllegalArgumentException if rowIndex is negative or >= number of rows
     */
    public boolean[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as a boolean array.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * boolean[] column = matrix.column(1); // Returns [false, true]
     * </pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a boolean array containing the values of the specified column
     * @throws IllegalArgumentException if columnIndex is negative or >= number of columns
     */
    public boolean[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final boolean[] c = new boolean[rows];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i][columnIndex];
        }

        return c;
    }

    /**
     * Sets the values of an entire row with the provided array.
     *
     * @param rowIndex the index of the row to set (0-based)
     * @param row the boolean array containing new values for the row
     * @throws IllegalArgumentException if the row array length doesn't match the matrix column count
     */
    public void setRow(final int rowIndex, final boolean[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Sets the values of an entire column with the provided array.
     *
     * @param columnIndex the index of the column to set (0-based)
     * @param column the boolean array containing new values for the column
     * @throws IllegalArgumentException if the column array length doesn't match the matrix row count
     */
    public void setColumn(final int columnIndex, final boolean[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all values in the specified row by applying the given function.
     * 
     * <p>Example:
     * <pre>
     * matrix.updateRow(0, val -> !val); // Inverts all values in row 0
     * </pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param rowIndex the index of the row to update
     * @param func the unary operator to apply to each element in the row
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.BooleanUnaryOperator<E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.applyAsBoolean(a[rowIndex][i]);
        }
    }

    /**
     * Updates all values in the specified column by applying the given function.
     * 
     * <p>Example:
     * <pre>
     * matrix.updateColumn(1, val -> !val); // Inverts all values in column 1
     * </pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param columnIndex the index of the column to update
     * @param func the unary operator to apply to each element in the column
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.BooleanUnaryOperator<E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.applyAsBoolean(a[i][columnIndex]);
        }
    }

    /**
     * Returns the diagonal elements from left-up to right-down (main diagonal).
     * The matrix must be square (rows == columns).
     *
     * @return a boolean array containing the main diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    public boolean[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final boolean[] res = new boolean[rows];

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][i]; // NOSONAR
        }

        return res;
    }

    /**
     * Sets the diagonal elements from left-up to right-down (main diagonal).
     * The matrix must be square (rows == columns).
     *
     * @param diagonal the new values for the main diagonal
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
     */
    public void setLU2RD(final boolean[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates the diagonal elements from left-up to right-down by applying the given function.
     * The matrix must be square (rows == columns).
     *
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateLU2RD(final Throwables.BooleanUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.applyAsBoolean(a[i][i]);
        }
    }

    /**
     * Returns the diagonal elements from right-up to left-down (anti-diagonal).
     * The matrix must be square (rows == columns).
     *
     * @return a boolean array containing the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    public boolean[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final boolean[] res = new boolean[rows];

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][cols - i - 1];
        }

        return res;
    }

    /**
     * Sets the diagonal elements from right-up to left-down (anti-diagonal).
     * The matrix must be square (rows == columns).
     *
     * @param diagonal the new values for the anti-diagonal
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
     */
    public void setRU2LD(final boolean[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = diagonal[i];
        }
    }

    /**
     * Updates the diagonal elements from right-up to left-down by applying the given function.
     * The matrix must be square (rows == columns).
     *
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each anti-diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateRU2LD(final Throwables.BooleanUnaryOperator<E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.applyAsBoolean(a[i][cols - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix by applying the given function.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:
     * <pre>
     * matrix.updateAll(val -> !val); // Inverts all values in the matrix
     * </pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each element
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.BooleanUnaryOperator<E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.applyAsBoolean(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Updates all elements in the matrix based on their position using the given function.
     * The function receives the row and column indices and returns the new boolean value.
     * 
     * <p>Example:
     * <pre>
     * matrix.updateAll((i, j) -> i == j); // Sets main diagonal to true, others to false
     * </pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the function that takes (rowIndex, columnIndex) and returns a Boolean value
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Boolean, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements that match the predicate with the specified new value.
     * 
     * <p>Example:
     * <pre>
     * matrix.replaceIf(val -> val == false, true); // Replace all false values with true
     * </pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the predicate to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.BooleanPredicate<E> predicate, final boolean newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces elements based on their position using the given predicate.
     * Elements at positions where the predicate returns true are replaced with the new value.
     * 
     * <p>Example:
     * <pre>
     * matrix.replaceIf((i, j) -> i == j, true); // Set main diagonal to true
     * </pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the predicate that takes (rowIndex, columnIndex) and returns true for positions to replace
     * @param newValue the value to replace at matching positions
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final boolean newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new BooleanMatrix by applying the given function to each element.
     * The operation may be performed in parallel for large matrices.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix inverted = matrix.map(val -> !val); // Creates new matrix with inverted values
     * </pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param func the unary operator to apply to each element
     * @return a new BooleanMatrix with the function applied to all elements
     * @throws E if the function throws an exception
     */
    public <E extends Exception> BooleanMatrix map(final Throwables.BooleanUnaryOperator<E> func) throws E {
        final boolean[][] result = new boolean[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsBoolean(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Creates a new object Matrix by applying the given function to each boolean element.
     * The resulting matrix contains objects of the specified target type.
     * 
     * <p>Example:
     * <pre>
     * Matrix<String> stringMatrix = matrix.mapToObj(val -> val ? "YES" : "NO", String.class);
     * </pre>
     *
     * @param <T> the target element type
     * @param <E> the type of exception that the function may throw
     * @param func the function to convert boolean values to the target type
     * @param targetElementType the Class object representing the target element type
     * @return a new Matrix containing the mapped values
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.BooleanFunction<? extends T, E> func, final Class<T> targetElementType) throws E {
        final T[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Fills all elements in the matrix with the specified value.
     * 
     * <p>Example:
     * <pre>
     * matrix.fill(true); // Sets all elements to true
     * </pre>
     *
     * @param val the boolean value to fill the matrix with
     */
    public void fill(final boolean val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills the matrix with values from the provided 2D array, starting from position (0, 0).
     *
     * @param b the 2D boolean array to copy values from
     */
    public void fill(final boolean[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from the provided 2D array.
     * Copying starts at the specified position and continues for the size of the input array
     * or until the matrix boundaries are reached.
     *
     * @param fromRowIndex the starting row index in this matrix
     * @param fromColumnIndex the starting column index in this matrix
     * @param b the 2D boolean array to copy values from
     * @throws IndexOutOfBoundsException if the starting indices are out of bounds
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final boolean[][] b) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, rows, rows);
        N.checkFromToIndex(fromColumnIndex, cols, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
        }
    }

    /**
     * Creates a copy of this matrix.
     *
     * @return a new BooleanMatrix with the same values as this matrix
     */
    @Override
    public BooleanMatrix copy() {
        final boolean[][] c = new boolean[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new BooleanMatrix(c);
    }

    /**
     * Creates a copy of a subset of rows from this matrix.
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new BooleanMatrix containing the specified rows
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @Override
    public BooleanMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final boolean[][] c = new boolean[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new BooleanMatrix(c);
    }

    /**
     * Creates a copy of a rectangular region from this matrix.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix subMatrix = matrix.copy(0, 2, 1, 3); // Copy rows 0-1, columns 1-2
     * </pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new BooleanMatrix containing the specified region
     * @throws IndexOutOfBoundsException if any index is out of bounds
     */
    @Override
    public BooleanMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex)
            throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final boolean[][] c = new boolean[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new BooleanMatrix(c);
    }

    /**
     * Creates a new matrix by extending or truncating this matrix to the specified dimensions.
     * New cells are filled with false.
     *
     * @param newRows the number of rows in the new matrix
     * @param newCols the number of columns in the new matrix
     * @return a new BooleanMatrix with the specified dimensions
     */
    public BooleanMatrix extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, false);
    }

    /**
     * Creates a new matrix by extending or truncating this matrix to the specified dimensions.
     * New cells are filled with the specified default value.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix extended = matrix.extend(5, 5, true); // Extend to 5x5, fill new cells with true
     * </pre>
     *
     * @param newRows the number of rows in the new matrix
     * @param newCols the number of columns in the new matrix
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new BooleanMatrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public BooleanMatrix extend(final int newRows, final int newCols, final boolean defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            // NOSONAR
            final boolean[][] b = new boolean[newRows][];

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : new boolean[newCols];

                if (defaultValueForNewCell) {
                    if (i >= rows) {
                        N.fill(b[i], true);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, true);
                    }
                }
            }

            return new BooleanMatrix(b);
        }
    }

    /**
     * Creates a new matrix by extending this matrix in all four directions.
     * New cells are filled with false.
     *
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @return a new extended BooleanMatrix
     */
    public BooleanMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, false);
    }

    /**
     * Creates a new matrix by extending this matrix in all four directions.
     * New cells are filled with the specified default value.
     * 
     * <p>Example:
     * <pre>
     * BooleanMatrix extended = matrix.extend(1, 1, 2, 2, true); // Add 1 row up/down, 2 cols left/right
     * </pre>
     *
     * @param toUp number of rows to add above
     * @param toDown number of rows to add below
     * @param toLeft number of columns to add to the left
     * @param toRight number of columns to add to the right
     * @param defaultValueForNewCell the value to fill new cells with
     * @return a new extended BooleanMatrix
     * @throws IllegalArgumentException if any extension parameter is negative
     */
    public BooleanMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final boolean defaultValueForNewCell)
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
            // NOSONAR
            final boolean[][] b = new boolean[newRows][newCols];

            for (int i = 0; i < newRows; i++) {
                if (i >= toUp && i < toUp + rows) {
                    N.copy(a[i - toUp], 0, b[i], toLeft, cols);
                }

                if (defaultValueForNewCell) {
                    if (i < toUp || i >= toUp + rows) {
                        N.fill(b[i], true);
                    } else if (cols < newCols) {
                        if (toLeft > 0) {
                            N.fill(b[i], 0, toLeft, true);
                        }

                        if (toRight > 0) {
                            N.fill(b[i], cols + toLeft, newCols, true);
                        }
                    }
                }
            }

            return new BooleanMatrix(b);
        }
    }

    /**
     * Reverses the order of elements in each row (horizontal flip in-place).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.reverseH(); // [[true,true,false]] becomes [[false,true,true]]
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
     * matrix.reverseV(); // [[true],[true],[false]] becomes [[false],[true],[true]]
     * }</pre>
     */
    public void reverseV() {
        for (int j = 0; j < cols; j++) {
            boolean tmp = false;
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
     * <pre>
     * // [[true, false], [false, true]] becomes [[false, true], [true, false]]
     * BooleanMatrix flipped = matrix.flipH();
     * </pre>
     *
     * @return a new BooleanMatrix with horizontally flipped content
     * @see #flipV()
     * @see IntMatrix#flipH()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1</a>
     */
    public BooleanMatrix flipH() {
        final BooleanMatrix res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * Creates a new matrix that is vertically flipped (each column reversed).
     * The original matrix is not modified.
     * 
     * <p>Example:
     * <pre>
     * // [[true, false], [false, true]] becomes [[false, true], [true, false]]
     * BooleanMatrix flipped = matrix.flipV();
     * </pre>
     *
     * @return a new BooleanMatrix with vertically flipped content
     * @see #flipH()
     * @see IntMatrix#flipV()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1</a>
     */
    public BooleanMatrix flipV() {
        final BooleanMatrix res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * Creates a new matrix by rotating this matrix 90 degrees clockwise.
     * The resulting matrix will have dimensions (cols x rows).
     *
     * @return a new BooleanMatrix rotated 90 degrees clockwise
     */
    @Override
    public BooleanMatrix rotate90() {
        final boolean[][] c = new boolean[cols][rows];

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

        return new BooleanMatrix(c);
    }

    /**
     * Creates a new matrix by rotating this matrix 180 degrees.
     * Equivalent to flipping both horizontally and vertically.
     *
     * @return a new BooleanMatrix rotated 180 degrees
     */
    @Override
    public BooleanMatrix rotate180() {
        final boolean[][] c = new boolean[rows][];

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new BooleanMatrix(c);
    }

    /**
     * Creates a new matrix by rotating this matrix 270 degrees clockwise (90 degrees counter-clockwise).
     * The resulting matrix will have dimensions (cols x rows).
     *
     * @return a new BooleanMatrix rotated 270 degrees clockwise
     */
    @Override
    public BooleanMatrix rotate270() {
        final boolean[][] c = new boolean[cols][rows];

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

        return new BooleanMatrix(c);
    }

    /**
     * Creates a new matrix that is the transpose of this matrix.
     * Rows become columns and columns become rows.
     * 
     * <p>Example:
     * <pre>
     * // [[true, false], [false, true]] becomes [[true, false], [false, true]]
     * BooleanMatrix transposed = matrix.transpose();
     * </pre>
     *
     * @return a new BooleanMatrix with dimensions (cols x rows)
     */
    @Override
    public BooleanMatrix transpose() {
        final boolean[][] c = new boolean[cols][rows];

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

        return new BooleanMatrix(c);
    }

    /**
     * Reshapes this matrix to the specified dimensions.
     * Elements are read in row-major order and placed into the new shape.
     * 
     * <p>Example:
     * <pre>
     * // Reshape a 2x3 matrix to 3x2
     * BooleanMatrix reshaped = matrix.reshape(3, 2);
     * </pre>
     *
     * @param newRows the number of rows in the reshaped matrix
     * @param newCols the number of columns in the reshaped matrix
     * @return a new BooleanMatrix with the specified shape
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public BooleanMatrix reshape(final int newRows, final int newCols) {
        final boolean[][] c = new boolean[newRows][newCols];

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new BooleanMatrix(c);
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

        return new BooleanMatrix(c);
    }

    /**
     * Repeats each element in the matrix the specified number of times in both dimensions.
     * Each element is expanded into a rowRepeats x colRepeats block.
     * 
     * <p>Example:
     * <pre>
     * // [[true, false]] with repelem(2, 3) becomes:
     * // [[true, true, true, false, false, false],
     * //  [true, true, true, false, false, false]]
     * BooleanMatrix repeated = matrix.repelem(2, 3);
     * </pre>
     *
     * @param rowRepeats number of times to repeat each element vertically
     * @param colRepeats number of times to repeat each element horizontally
     * @return a new BooleanMatrix with dimensions (rows*rowRepeats x cols*colRepeats)
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     */
    @Override
    public BooleanMatrix repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final boolean[][] c = new boolean[rows * rowRepeats][cols * colRepeats];

        for (int i = 0; i < rows; i++) {
            final boolean[] aa = a[i];
            final boolean[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                N.copy(Array.repeat(aa[j], colRepeats), 0, fr, j * colRepeats, colRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new BooleanMatrix(c);
    }

    /**
     * Repeats the entire matrix the specified number of times in both dimensions.
     * The matrix is tiled rowRepeats times vertically and colRepeats times horizontally.
     * 
     * <p>Example:
     * <pre>
     * // [[true, false]] with repmat(2, 3) becomes:
     * // [[true, false, true, false, true, false],
     * //  [true, false, true, false, true, false]]
     * BooleanMatrix tiled = matrix.repmat(2, 3);
     * </pre>
     *
     * @param rowRepeats number of times to repeat the matrix vertically
     * @param colRepeats number of times to repeat the matrix horizontally
     * @return a new BooleanMatrix with dimensions (rows*rowRepeats x cols*colRepeats)
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     */
    @Override
    public BooleanMatrix repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final boolean[][] c = new boolean[rows * rowRepeats][cols * colRepeats];

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

        return new BooleanMatrix(c);
    }

    /**
     * Flattens the matrix into a one-dimensional BooleanList.
     * Elements are collected in row-major order, meaning the matrix is traversed
     * row by row from left to right.
     * 
     * <p>This method is useful when you need to convert the 2D matrix structure
     * into a linear sequence while preserving the element order. The resulting
     * list can be used for operations that require a flat representation of the data.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * BooleanList flat = matrix.flatten(); // [true, false, false, true]
     * 
     * // Recreate matrix from flattened list
     * boolean[] array = flat.array();
     * BooleanMatrix restored = BooleanMatrix.of(array, matrix.rows(), matrix.cols());
     * }</pre>
     *
     * @return a BooleanList containing all elements of the matrix in row-major order
     */
    @Override
    public BooleanList flatten() {
        final boolean[] c = new boolean[rows * cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return BooleanList.of(c);
    }

    /**
     * Flattens the underlying 2D array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure.
     *
     * @param <E> the type of exception that the operation may throw
     * @param op the consumer operation to apply to the internal array
     * @throws E if the operation throws an exception
     * @see Arrays#flatOp(boolean[][], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super boolean[], E> op) throws E {
        Arrays.flatOp(a, op);
    }

    /**
     * Vertically stacks this matrix on top of the specified matrix.
     * Both matrices must have the same number of columns. The result is a new matrix
     * where this matrix forms the top rows and the specified matrix forms the bottom rows.
     * 
     * <p>This operation is useful for combining matrices that represent related data
     * or for building larger matrices from smaller components. The original matrices
     * are not modified.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix top = BooleanMatrix.of(new boolean[][]{{true, false}});
     * BooleanMatrix bottom = BooleanMatrix.of(new boolean[][]{{false, true}});
     * BooleanMatrix stacked = top.vstack(bottom);
     * // Result: [[true, false],
     * //          [false, true]]
     * 
     * // Stack multiple matrices
     * BooleanMatrix m1 = BooleanMatrix.of(new boolean[][]{{true, true}});
     * BooleanMatrix m2 = BooleanMatrix.of(new boolean[][]{{false, false}});
     * BooleanMatrix m3 = BooleanMatrix.of(new boolean[][]{{true, false}});
     * BooleanMatrix combined = m1.vstack(m2).vstack(m3); // 3x2 matrix
     * }</pre>
     *
     * @param b the matrix to stack below this matrix
     * @return a new BooleanMatrix with dimensions ((this.rows + b.rows) x cols)
     * @throws IllegalArgumentException if the matrices have different column counts
     */
    public BooleanMatrix vstack(final BooleanMatrix b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final boolean[][] c = new boolean[rows + b.rows][];
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
        }

        return BooleanMatrix.of(c);
    }

    /**
     * Horizontally stacks this matrix to the left of the specified matrix.
     * Both matrices must have the same number of rows. The result is a new matrix
     * where this matrix forms the left columns and the specified matrix forms the right columns.
     * 
     * <p>This operation is useful for combining matrices side by side, such as when
     * concatenating feature matrices or building wider data structures from narrower ones.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix left = BooleanMatrix.of(new boolean[][]{{true}, {false}});
     * BooleanMatrix right = BooleanMatrix.of(new boolean[][]{{false}, {true}});
     * BooleanMatrix stacked = left.hstack(right);
     * // Result: [[true, false],
     * //          [false, true]]
     * 
     * // Create a wider matrix by stacking multiple columns
     * BooleanMatrix col1 = BooleanMatrix.of(new boolean[][]{{true}, {true}, {false}});
     * BooleanMatrix col2 = BooleanMatrix.of(new boolean[][]{{false}, {true}, {true}});
     * BooleanMatrix wide = col1.hstack(col2); // 3x2 matrix
     * }</pre>
     *
     * @param b the matrix to stack to the right of this matrix
     * @return a new BooleanMatrix with dimensions (rows x (this.cols + b.cols))
     * @throws IllegalArgumentException if the matrices have different row counts
     */
    public BooleanMatrix hstack(final BooleanMatrix b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final boolean[][] c = new boolean[rows][cols + b.cols];

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c[i], 0, cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
        }

        return BooleanMatrix.of(c);
    }

    /**
     * Converts this primitive boolean matrix to a boxed Boolean Matrix.
     * Each boolean value is converted to its corresponding Boolean wrapper object.
     * 
     * <p>This conversion is useful when you need to work with APIs that require
     * object types rather than primitives, or when you need null values in the matrix.
     * Note that boxing incurs memory overhead and may impact performance.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix primitive = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * Matrix<Boolean> boxed = primitive.boxed();
     * 
     * // Now you can use methods that work with generic types
     * Stream<Boolean> stream = boxed.stream();
     * boxed.set(0, 0, null); // Can use null values
     * }</pre>
     *
     * @return a new Matrix<Boolean> with the same dimensions and values as this matrix
     */
    public Matrix<Boolean> boxed() {
        final Boolean[][] c = new Boolean[rows][cols];

        if (rows <= cols) {
            for (int i = 0; i < rows; i++) {
                final boolean[] aa = a[i];
                final Boolean[] cc = c[i];

                for (int j = 0; j < cols; j++) {
                    cc[j] = aa[j]; // NOSONAR
                }
            }
        } else {
            for (int j = 0; j < cols; j++) {
                for (int i = 0; i < rows; i++) {
                    c[i][j] = a[i][j]; // NOSONAR
                }
            }
        }

        return new Matrix<>(c);
    }

    /**
     * Performs element-wise combination of this matrix with another matrix using the provided binary function.
     * Both matrices must have exactly the same dimensions. The function is applied to each pair of
     * corresponding elements to produce the result matrix.
     * 
     * <p>This method is useful for implementing element-wise boolean operations such as AND, OR, XOR,
     * or any custom binary boolean logic. The operation may be parallelized for large matrices.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix a = BooleanMatrix.of(new boolean[][]{{true, false}, {true, true}});
     * BooleanMatrix b = BooleanMatrix.of(new boolean[][]{{true, true}, {false, true}});
     * 
     * // Element-wise AND
     * BooleanMatrix and = a.zipWith(b, (x, y) -> x && y);
     * // Result: [[true, false], [false, true]]
     * 
     * // Element-wise OR
     * BooleanMatrix or = a.zipWith(b, (x, y) -> x || y);
     * // Result: [[true, true], [true, true]]
     * 
     * // Element-wise XOR
     * BooleanMatrix xor = a.zipWith(b, (x, y) -> x ^ y);
     * // Result: [[false, true], [true, false]]
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param matrixB the second matrix to combine with this matrix
     * @param zipFunction the binary operator to apply to corresponding elements
     * @return a new BooleanMatrix containing the results of the element-wise operation
     * @throws IllegalArgumentException if the matrices have different dimensions
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> BooleanMatrix zipWith(final BooleanMatrix matrixB, final Throwables.BooleanBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Can't zip two or more matrices which don't have same shape");

        final boolean[][] b = matrixB.a;
        final boolean[][] result = new boolean[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsBoolean(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Performs element-wise combination of this matrix with two other matrices using the provided ternary function.
     * All three matrices must have exactly the same dimensions. The function is applied to each triple of
     * corresponding elements to produce the result matrix.
     * 
     * <p>This method enables complex three-way boolean operations that cannot be easily expressed
     * as a sequence of binary operations. The operation may be parallelized for large matrices.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix a = BooleanMatrix.of(new boolean[][]{{true, false}, {true, true}});
     * BooleanMatrix b = BooleanMatrix.of(new boolean[][]{{true, true}, {false, true}});
     * BooleanMatrix c = BooleanMatrix.of(new boolean[][]{{false, true}, {true, false}});
     * 
     * // Majority vote: true if at least 2 out of 3 are true
     * BooleanMatrix majority = a.zipWith(b, c, (x, y, z) -> 
     *     (x && y) || (x && z) || (y && z));
     * 
     * // Conditional operation: if a then b else c
     * BooleanMatrix conditional = a.zipWith(b, c, (x, y, z) -> x ? y : z);
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param matrixB the second matrix
     * @param matrixC the third matrix
     * @param zipFunction the ternary operator to apply to corresponding elements
     * @return a new BooleanMatrix containing the results of the element-wise operation
     * @throws IllegalArgumentException if the matrices have different dimensions
     * @throws E if the zip function throws an exception
     */
    public <E extends Exception> BooleanMatrix zipWith(final BooleanMatrix matrixB, final BooleanMatrix matrixC,
            final Throwables.BooleanTernaryOperator<E> zipFunction) throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Can't zip two or more matrices which don't have same shape");

        final boolean[][] b = matrixB.a;
        final boolean[][] c = matrixC.a;
        final boolean[][] result = new boolean[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.applyAsBoolean(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Returns a stream of Boolean values from the main diagonal (left-up to right-down).
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>This method streams the diagonal elements starting from position (0,0) and
     * proceeding to position (n-1,n-1) where n is the dimension of the square matrix.
     * This is useful for operations on diagonal matrices or extracting diagonal elements.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{
     *     {true, false, false},
     *     {false, true, false},
     *     {false, false, true}
     * });
     * List<Boolean> diagonal = matrix.streamLU2RD().toList(); // [true, true, true]
     * 
     * // Check if it's an identity-like matrix
     * boolean allTrue = matrix.streamLU2RD().allMatch(b -> b);
     * }</pre>
     *
     * @return a Stream<Boolean> containing the diagonal elements from top-left to bottom-right
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public Stream<Boolean> streamLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public Boolean next() {
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
     * Returns a stream of Boolean values from the anti-diagonal (right-up to left-down).
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>This method streams the anti-diagonal elements starting from position (0,n-1)
     * and proceeding to position (n-1,0) where n is the dimension of the square matrix.
     * This is useful for operations involving the secondary diagonal of a matrix.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{
     *     {true, false, true},
     *     {false, true, false},
     *     {true, false, false}
     * });
     * List<Boolean> antiDiagonal = matrix.streamRU2LD().toList(); // [true, true, true]
     * 
     * // Count true values on anti-diagonal
     * long trueCount = matrix.streamRU2LD().filter(b -> b).count();
     * }</pre>
     *
     * @return a Stream<Boolean> containing the anti-diagonal elements from top-right to bottom-left
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public Stream<Boolean> streamRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private final int toIndex = rows;
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < toIndex;
            }

            @Override
            public Boolean next() {
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
     * Elements are streamed row by row from left to right, starting from the
     * top-left corner and proceeding to the bottom-right corner.
     * 
     * <p>This method is useful for processing all matrix elements sequentially
     * without concern for their row/column positions. Unlike the primitive IntMatrix,
     * this returns a Stream<Boolean> with boxed values.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * Stream<Boolean> stream = matrix.streamH(); // Stream of [true, false, false, true]
     * 
     * // Count true values
     * long trueCount = matrix.streamH().filter(b -> b).count(); // Returns 2
     * 
     * // Convert to list
     * List<Boolean> list = matrix.streamH().toList(); // [true, false, false, true]
     * }</pre>
     * 
     * @return a Stream<Boolean> of all elements in row-major order, or an empty stream if the matrix is empty
     */
    @Override
    public Stream<Boolean> streamH() {
        return streamH(0, rows);
    }

    /**
     * Returns a stream of elements from a single row.
     * The elements are streamed from left to right within the specified row.
     * 
     * <p>This method is particularly useful when you need to process or analyze
     * a specific row of the matrix independently.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{
     *     {true, false, true},
     *     {false, true, false}
     * });
     * Stream<Boolean> firstRow = matrix.streamH(0); // Stream of [true, false, true]
     * 
     * // Check if any value in the second row is true
     * boolean hasTrue = matrix.streamH(1).anyMatch(b -> b); // Returns true
     * }</pre>
     * 
     * @param rowIndex the index of the row to stream (0-based)
     * @return a Stream<Boolean> of elements from the specified row
     * @throws IndexOutOfBoundsException if rowIndex < 0 or rowIndex >= rows
     */
    @Override
    public Stream<Boolean> streamH(final int rowIndex) {
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{
     *     {true, false},
     *     {false, true},
     *     {true, true}
     * });
     * Stream<Boolean> middleRows = matrix.streamH(1, 3); // Stream rows 1 and 2: [false, true, true, true]
     * 
     * // Process subset of rows
     * boolean[] subset = matrix.streamH(0, 2)
     *     .mapToInt(b -> b ? 1 : 0)
     *     .toArray(); // [1, 0, 0, 1]
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a Stream<Boolean> of elements from the specified row range, or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromRowIndex < 0, toRowIndex > rows, or fromRowIndex > toRowIndex
     */
    @Override
    public Stream<Boolean> streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private int i = fromRowIndex;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i < toRowIndex;
            }

            @Override
            public Boolean next() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final boolean result = a[i][j++];

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
            public <A> A[] toArray(A[] c) {
                final int len = (int) count();

                if (c.length < len) {
                    c = N.copyOf(c, len);
                }

                for (int k = 0; k < len; k++) {
                    c[k] = (A) (Boolean) a[i][j++];

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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * Stream<Boolean> stream = matrix.streamV(); // Stream of [true, false, false, true]
     * 
     * // Process in column order
     * List<Boolean> colMajor = matrix.streamV().toList(); // [true, false, false, true]
     * }</pre>
     * 
     * @return a Stream<Boolean> of all elements in column-major order, or an empty stream if the matrix is empty
     */
    @Override
    @Beta
    public Stream<Boolean> streamV() {
        return streamV(0, cols);
    }

    /**
     * Returns a stream of elements from a single column.
     * The elements are streamed from top to bottom within the specified column.
     * 
     * <p>This method is useful for column-wise operations such as checking
     * column properties or extracting column data.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{
     *     {true, false, true},
     *     {true, true, false}
     * });
     * Stream<Boolean> firstCol = matrix.streamV(0); // Stream of [true, true]
     * 
     * // Check if all values in a column are true
     * boolean allTrue = matrix.streamV(0).allMatch(b -> b); // Returns true
     * }</pre>
     * 
     * @param columnIndex the index of the column to stream (0-based)
     * @return a Stream<Boolean> of elements from the specified column
     * @throws IndexOutOfBoundsException if columnIndex < 0 or columnIndex >= cols
     */
    @Override
    public Stream<Boolean> streamV(final int columnIndex) {
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{
     *     {true, false, true},
     *     {false, true, false}
     * });
     * Stream<Boolean> lastTwoCols = matrix.streamV(1, 3); // Stream columns 1 and 2: [false, true, true, false]
     * 
     * // Count true values in column subset
     * long trueCount = matrix.streamV(0, 2).filter(b -> b).count();
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a Stream<Boolean> of elements from the specified column range in column-major order,
     *         or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromColumnIndex < 0, toColumnIndex > cols,
     *         or fromColumnIndex > toColumnIndex
     */
    @Override
    @Beta
    public Stream<Boolean> streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private int i = 0;
            private int j = fromColumnIndex;

            @Override
            public boolean hasNext() {
                return j < toColumnIndex;
            }

            @Override
            public Boolean next() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final boolean result = a[i++][j];

                if (i >= rows) {
                    i = 0;
                    j++;
                }

                return result;
            }

            @Override
            public void advance(final long n) throws IllegalArgumentException {
                N.checkArgNotNegative(n, "n");

                if (n >= (long) (toColumnIndex - j) * BooleanMatrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    i += (int) ((n + i) % BooleanMatrix.this.rows);
                    j += (int) ((n + i) / BooleanMatrix.this.rows);
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rows - i; // NOSONAR
            }

            @Override
            public <A> A[] toArray(A[] c) {
                final int len = (int) count();

                if (c.length < len) {
                    c = N.copyOf(c, len);
                }

                for (int k = 0; k < len; k++) {
                    c[k] = (A) (Boolean) a[i++][j];

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
     * Returns a stream of Stream<Boolean> objects, where each inner stream represents a complete row.
     * This creates a stream of streams, allowing for row-by-row processing of the matrix.
     * 
     * <p>This method is useful for operations that need to process entire rows as units,
     * such as row-wise transformations, filtering rows based on conditions, or mapping
     * rows to other values.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{
     *     {true, false, true},
     *     {false, false, false},
     *     {true, true, true}
     * });
     * 
     * // Count rows that contain at least one true value
     * long rowsWithTrue = matrix.streamR()
     *     .filter(row -> row.anyMatch(b -> b))
     *     .count(); // Returns 2
     * 
     * // Get row sums (count of true values per row)
     * int[] rowTrueCounts = matrix.streamR()
     *     .mapToInt(row -> (int) row.filter(b -> b).count())
     *     .toArray(); // [2, 0, 3]
     * }</pre>
     * 
     * @return a Stream of Stream<Boolean> objects, one for each row in the matrix,
     *         or an empty stream if the matrix is empty
     */
    @Override
    public Stream<Stream<Boolean>> streamR() {
        return streamR(0, rows);
    }

    /**
     * Returns a stream of Stream<Boolean> objects for a range of rows.
     * Each inner stream in the result represents a complete row within the specified range.
     * 
     * <p>This method allows for processing a subset of rows while maintaining the
     * ability to work with complete rows as individual streams.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{
     *     {true, true, false},
     *     {false, true, true},
     *     {true, false, true}
     * });
     * 
     * // Process middle rows only
     * List<Boolean> hasPattern = matrix.streamR(1, 3)
     *     .map(row -> {
     *         List<Boolean> list = row.toList();
     *         return list.get(0) != list.get(2); // Check if first != last
     *     })
     *     .toList(); // [true, false]
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a Stream of Stream<Boolean> objects for the specified row range,
     *         or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromRowIndex < 0, toRowIndex > rows,
     *         or fromRowIndex > toRowIndex
     */
    @Override
    public Stream<Stream<Boolean>> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
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
            public Stream<Boolean> next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return Stream.of(a[cursor++]);
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
     * Returns a stream of Stream<Boolean> objects, where each inner stream represents a complete column.
     * This creates a stream of streams, allowing for column-by-column processing of the matrix.
     * 
     * <p>This method is marked as @Beta and is useful for operations that need to process
     * entire columns as units, such as column-wise statistics, transformations, or filtering
     * columns based on conditions.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{
     *     {true, false, true},
     *     {true, true, false}
     * });
     * 
     * // Check which columns have all true values
     * List<Boolean> allTrueColumns = matrix.streamC()
     *     .map(col -> col.allMatch(b -> b))
     *     .toList(); // [true, false, false]
     * 
     * // Count true values per column
     * long[] colTrueCounts = matrix.streamC()
     *     .mapToLong(col -> col.filter(b -> b).count())
     *     .toArray(); // [2, 1, 1]
     * }</pre>
     * 
     * @return a Stream of Stream<Boolean> objects, one for each column in the matrix,
     *         or an empty stream if the matrix is empty
     */
    @Override
    @Beta
    public Stream<Stream<Boolean>> streamC() {
        return streamC(0, cols);
    }

    /**
     * Returns a stream of Stream<Boolean> objects for a range of columns.
     * Each inner stream in the result represents a complete column within the specified range.
     * 
     * <p>This method is marked as @Beta and allows for processing a subset of columns
     * while maintaining the ability to work with complete columns as individual streams.</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{
     *     {true, false, true, false},
     *     {false, true, false, true}
     * });
     * 
     * // Process last two columns
     * List<String> patterns = matrix.streamC(2, 4)
     *     .map(col -> col.map(b -> b ? "1" : "0")
     *                    .joining())
     *     .toList(); // ["10", "01"]
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a Stream of Stream<Boolean> objects for the specified column range,
     *         or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromColumnIndex < 0, toColumnIndex > cols,
     *         or fromColumnIndex > toColumnIndex
     */
    @Override
    @Beta
    public Stream<Stream<Boolean>> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public Stream<Boolean> next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return Stream.of(new ObjIteratorEx<>() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rows;
                    private int cursor2 = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor2 < toIndex2;
                    }

                    @Override
                    public Boolean next() {
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
     * Returns the length of the given array.
     * This is a utility method used internally by the abstract parent class.
     * 
     * @param a the array to measure, may be null
     * @return the length of the array, or 0 if the array is null
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final boolean[] a) {
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * 
     * // Count true values
     * int[] trueCount = {0};
     * matrix.forEach(value -> {
     *     if (value) trueCount[0]++;
     * });
     * // trueCount[0] is now 2
     * 
     * // Print all values
     * matrix.forEach(value -> System.out.print(value ? "T" : "F"));
     * // Prints: TFFT
     * }</pre>
     * 
     * @param <E> the type of exception that the action may throw
     * @param action the action to be performed for each element
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{
     *     {true, false, true},
     *     {false, true, false},
     *     {true, true, true}
     * });
     * 
     * // Process only the center 2x2 sub-matrix
     * List<Boolean> center = new ArrayList<>();
     * matrix.forEach(0, 2, 0, 2, value -> center.add(value));
     * // center contains [true, false, false, true]
     * 
     * // Count true values in bottom row
     * int[] bottomRowTrue = {0};
     * matrix.forEach(2, 3, 0, 3, value -> {
     *     if (value) bottomRowTrue[0]++;
     * });
     * // bottomRowTrue[0] is 3
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
            final Throwables.BooleanConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final boolean[] aa = a[i];

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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * matrix.println();
     * // Output:
     * // [true, false]
     * // [false, true]
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
     * <p>Two matrices with the same dimensions and identical boolean values in the same
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
     * Two BooleanMatrix objects are considered equal if they have the same dimensions
     * and contain the same boolean values in the same positions.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix m1 = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * BooleanMatrix m2 = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * BooleanMatrix m3 = BooleanMatrix.of(new boolean[][]{{true, false}, {true, false}});
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

        if (obj instanceof final BooleanMatrix another) {
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
     * suitable for debugging and logging purposes. Boolean values are shown as
     * "true" or "false".</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][]{{true, false}, {false, true}});
     * System.out.println(matrix.toString()); // Prints: [[true, false], [false, true]]
     * }</pre>
     * 
     * @return a string representation of the matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
