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
import com.landawn.abacus.util.u.OptionalBoolean;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

/**
 * Matrix implementation backed by a {@code boolean[][]}.
 *
 * <p>It provides boolean-specific accessors and transformations while sharing the common matrix operations
 * defined by {@link AbstractMatrix}. Construction and factory methods typically wrap the supplied array
 * directly unless their documentation states otherwise.</p>
 */
public final class BooleanMatrix extends AbstractMatrix<boolean[], BooleanList, Stream<Boolean>, Stream<Stream<Boolean>>, BooleanMatrix> {

    static final Random RAND = new SecureRandom();
    static final BooleanMatrix EMPTY_BOOLEAN_MATRIX = new BooleanMatrix(new boolean[0][0]);

    /**
     * Constructs a new BooleanMatrix with the specified two-dimensional boolean array.
     * If the input array is null, an empty matrix (0x0) is created.
     *
     * <p><b>Important:</b> The input array is used directly without defensive copying.
     * This means modifications to the input array after construction will affect the matrix,
     * and vice versa. For independent matrices, create a copy of the array before passing it.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] data = {{true, false, true}, {false, true, false}};
     * BooleanMatrix matrix = new BooleanMatrix(data);
     * data[0][0] = false;  // This will also modify the matrix
     * }</pre>
     *
     * @param a the two-dimensional boolean array to initialize the matrix with, or null for an empty matrix
     */
    public BooleanMatrix(final boolean[][] a) {
        super(a == null ? new boolean[0][0] : a);
    }

    /**
     * Creates an empty matrix with zero rows and zero columns.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.empty();
     * // matrix.rowCount() returns 0
     * // matrix.columnCount() returns 0
     * }</pre>
     *
     * @return an empty boolean matrix
     */
    public static BooleanMatrix empty() {
        return EMPTY_BOOLEAN_MATRIX;
    }

    /**
     * Creates a BooleanMatrix from a two-dimensional boolean array.
     *
     * <p><b>Important:</b> The provided array is used directly without defensive copying.
     * Changes to the input array are reflected in the returned matrix, and vice versa.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * // matrix.get(0, 1) returns false
     * }</pre>
     *
     * @param a the two-dimensional boolean array to create the matrix from, or null/empty for an empty matrix
     * @return a new BooleanMatrix containing the provided data, or an empty BooleanMatrix if input is null or empty
     */
    public static BooleanMatrix of(final boolean[]... a) {
        return N.isEmpty(a) ? EMPTY_BOOLEAN_MATRIX : new BooleanMatrix(a);
    }

    /**
     * Creates a new 1xsize matrix filled with random boolean values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.random(5);
     * // Result: a 1x5 matrix with random boolean values
     * }</pre>
     *
     * @param size the number of columns in the new matrix
     * @return a new BooleanMatrix of dimensions 1 x size filled with random values
     */
    public static BooleanMatrix random(final int size) {
        return random(1, size);
    }

    /**
     * Creates a new matrix of the specified dimensions filled with random boolean values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.random(2, 3);
     * // Result: a 2x3 matrix with random boolean values
     * }</pre>
     *
     * @param rowCount the number of rows in the new matrix
     * @param columnCount the number of columns in the new matrix
     * @return a new BooleanMatrix of dimensions rowCount x columnCount filled with random values
     */
    public static BooleanMatrix random(final int rowCount, final int columnCount) {
        N.checkArgument(rowCount >= 0, MSG_NEGATIVE_DIMENSION, "rowCount", rowCount);
        N.checkArgument(columnCount >= 0, MSG_NEGATIVE_DIMENSION, "columnCount", columnCount);
        checkRepresentableShape(rowCount, columnCount);

        final boolean[][] a = new boolean[rowCount][columnCount];

        for (boolean[] ea : a) {
            for (int i = 0; i < columnCount; i++) {
                ea[i] = RAND.nextInt() % 2 == 0;
            }
        }

        return new BooleanMatrix(a);
    }

    /**
     * Creates a new matrix of the specified dimensions where every element is the provided {@code element}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.repeat(2, 3, true);
     * // Result: [[true, true, true], [true, true, true]]
     * }</pre>
     *
     * @param rowCount the number of rows in the new matrix
     * @param columnCount the number of columns in the new matrix
     * @param element the boolean value to fill the matrix with
     * @return a new BooleanMatrix of dimensions rowCount x columnCount filled with the specified element
     */
    public static BooleanMatrix repeat(final int rowCount, final int columnCount, final boolean element) {
        N.checkArgument(rowCount >= 0, MSG_NEGATIVE_DIMENSION, "rowCount", rowCount);
        N.checkArgument(columnCount >= 0, MSG_NEGATIVE_DIMENSION, "columnCount", columnCount);
        checkRepresentableShape(rowCount, columnCount);

        final boolean[][] a = new boolean[rowCount][columnCount];

        for (boolean[] ea : a) {
            N.fill(ea, element);
        }

        return new BooleanMatrix(a);
    }

    /**
     * Creates a square matrix from the specified main diagonal elements (upper-left to lower-right).
     * All other elements are set to false.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.mainDiagonal(new boolean[] {true, false, true});
     * // Creates 3x3 matrix with diagonal [true, false, true] and false elsewhere
     * // Resulting matrix:
     * //   {true, false, false},
     * //   {false, false, false},
     * //   {false, false, true}
     * }</pre>
     *
     * @param mainDiagonal the array of main diagonal elements
     * @return a square matrix with the specified main diagonal (n×n where n = diagonal length)
     */
    public static BooleanMatrix mainDiagonal(final boolean[] mainDiagonal) {
        return diagonals(mainDiagonal, null);
    }

    /**
     * Creates a square matrix from the specified anti-diagonal elements.
     * All other elements are set to false.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.antiDiagonal(new boolean[] {true, false, true});
     * // Creates 3x3 matrix with anti-diagonal [true, false, true] and false elsewhere
     * // Resulting matrix:
     * //   {false, false, true},
     * //   {false, false, false},
     * //   {true, false, false}
     * }</pre>
     *
     * @param antiDiagonal the array of anti-diagonal elements
     * @return a square matrix with the specified anti-diagonal (n×n where n = diagonal length)
     */
    public static BooleanMatrix antiDiagonal(final boolean[] antiDiagonal) {
        return diagonals(null, antiDiagonal);
    }

    /**
     * Creates a square matrix from the specified main diagonal and anti-diagonal elements.
     * All other elements are set to false. If both arrays are provided, they must have the same length.
     * The resulting matrix has dimensions n×n where n is the length of the non-empty diagonal array.
     * When both diagonals are provided and they overlap (at the center element of odd-sized matrices),
     * the main diagonal value takes precedence.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.diagonals(new boolean[] {true, true, true}, new boolean[] {true, false, true});
     * // Creates 3x3 matrix with both diagonals set
     * // Resulting matrix (main diagonal takes precedence at center):
     * //   {true, false, true},
     * //   {false, true, false},
     * //   {true, false, true}
     *
     * }</pre>
     *
     * @param mainDiagonal the array of main diagonal elements (can be null or empty)
     * @param antiDiagonal the array of anti-diagonal elements (can be null or empty)
     * @return a square matrix with the specified diagonals, or an empty matrix if both inputs are null or empty
     * @throws IllegalArgumentException if both arrays are non-empty and have different lengths
     */
    public static BooleanMatrix diagonals(final boolean[] mainDiagonal, final boolean[] antiDiagonal) throws IllegalArgumentException {
        N.checkArgument(N.isEmpty(mainDiagonal) || N.isEmpty(antiDiagonal) || mainDiagonal.length == antiDiagonal.length,
                "The length of 'mainDiagonal' and 'antiDiagonal' must be same");

        if (N.isEmpty(mainDiagonal) && N.isEmpty(antiDiagonal)) {
            return EMPTY_BOOLEAN_MATRIX;
        }

        final int len = N.max(N.len(mainDiagonal), N.len(antiDiagonal));
        final boolean[][] c = new boolean[len][len];

        if (N.notEmpty(antiDiagonal)) {
            for (int i = 0, j = len - 1; i < len; i++, j--) {
                c[i][j] = antiDiagonal[i];
            }
        }

        if (N.notEmpty(mainDiagonal)) {
            for (int i = 0; i < len; i++) {
                c[i][i] = mainDiagonal[i]; // NOSONAR
            }
        }

        return new BooleanMatrix(c);
    }

    /**
     * Converts a boxed Boolean Matrix to a primitive BooleanMatrix.
     * Null values in the input matrix are converted to {@code false}.
     *
     * <p>This method performs the opposite operation of {@link #boxed()}, converting
     * from object-based Boolean values to primitive boolean values. This conversion
     * improves memory efficiency and performance when working with large matrices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<Boolean> boxed = Matrix.of(new Boolean[][] {{true, false}, {null, true}});
     * BooleanMatrix primitive = BooleanMatrix.unbox(boxed);
     * // null is converted to false: [[true, false], [false, true]]
     * }</pre>
     *
     * @param x the boxed Boolean Matrix to convert; must not be null
     * @return a new BooleanMatrix with primitive boolean values
     * @see #boxed()
     */
    public static BooleanMatrix unbox(final Matrix<Boolean> x) {
        return BooleanMatrix.of(Array.unbox(x.a));
    }

    /**
     * Returns the component type of the matrix elements, which is always {@code boolean.class}.
     * This method is useful for reflection-based code that needs to determine the element type.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * Class componentType = matrix.componentType();
     * // componentType is boolean.class
     * assert componentType == boolean.class;
     * }</pre>
     *
     * @return {@code boolean.class}
     */
    @Override
    public Class<?> componentType() {
        return boolean.class;
    }

    /**
     * Returns the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * boolean value = matrix.get(0, 1);   // Returns false
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return the element at position (rowIndex, columnIndex)
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public boolean get(final int rowIndex, final int columnIndex) { // NOSONAR
        return a[rowIndex][columnIndex];
    }

    /**
     * Returns the element at the specified point.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * Sheet.Point point = Sheet.Point.of(0, 1);
     * boolean value = matrix.get(point);   // Returns false
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @return the boolean element at the specified point
     * @throws IllegalArgumentException if {@code point} is {@code null}
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #get(int, int)
     */
    public boolean get(final Point point) { // NOSONAR
        N.checkArgNotNull(point, "point");

        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * matrix.set(0, 1, true);   // Sets element at row 0, column 1 to true
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @param val the value to set
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public void set(final int rowIndex, final int columnIndex, final boolean val) {
        a[rowIndex][columnIndex] = val;
    }

    /**
     * Sets the element at the specified point to the given value.
     * This is a convenience method that accepts a Point object instead of separate row and column indices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * Sheet.Point point = Sheet.Point.of(1, 0);
     * matrix.set(point, true);
     * assert matrix.get(point) == true;
     * }</pre>
     *
     * @param point the point containing row and column indices (must not be null)
     * @param val the new boolean value to set at the specified point
     * @throws IllegalArgumentException if {@code point} is {@code null}
     * @throws ArrayIndexOutOfBoundsException if the point coordinates are out of bounds
     * @see #set(int, int, boolean)
     */
    public void set(final Point point, final boolean val) {
        N.checkArgNotNull(point, "point");

        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Returns the element directly above the specified position, if it exists.
     * This method provides safe access to the element directly above the given position
     * without throwing an exception when at the top edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * OptionalBoolean value = matrix.above(1, 0);   // Returns OptionalBoolean.of(true)
     * OptionalBoolean empty = matrix.above(0, 0);   // Returns OptionalBoolean.empty() - no row above
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return an OptionalBoolean containing the element at position (rowIndex - 1, columnIndex), or empty if rowIndex == 0
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalBoolean above(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        return rowIndex == 0 ? OptionalBoolean.empty() : OptionalBoolean.of(a[rowIndex - 1][columnIndex]);
    }

    /**
     * Returns the element directly below the specified position, if it exists.
     * This method provides safe access to the element directly below the given position
     * without throwing an exception when at the bottom edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * OptionalBoolean value = matrix.below(0, 0);   // Returns OptionalBoolean.of(false)
     * OptionalBoolean empty = matrix.below(1, 0);   // Returns OptionalBoolean.empty() - no row below
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return an OptionalBoolean containing the element at position (rowIndex + 1, columnIndex), or empty if rowIndex == rowCount - 1
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalBoolean below(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        return rowIndex == rowCount - 1 ? OptionalBoolean.empty() : OptionalBoolean.of(a[rowIndex + 1][columnIndex]);
    }

    /**
     * Returns the element directly to the left of the specified position, if it exists.
     * This method provides safe access to the element directly to the left of the given position
     * without throwing an exception when at the leftmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * OptionalBoolean value = matrix.left(0, 1);   // Returns OptionalBoolean.of(true)
     * OptionalBoolean empty = matrix.left(0, 0);   // Returns OptionalBoolean.empty() - no column to the left
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return an OptionalBoolean containing the element at position (rowIndex, columnIndex - 1), or empty if columnIndex == 0
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalBoolean left(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        return columnIndex == 0 ? OptionalBoolean.empty() : OptionalBoolean.of(a[rowIndex][columnIndex - 1]);
    }

    /**
     * Returns the element directly to the right of the specified position, if it exists.
     * This method provides safe access to the element directly to the right of the given position
     * without throwing an exception when at the rightmost edge of the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * OptionalBoolean value = matrix.right(0, 0);   // Returns OptionalBoolean.of(false)
     * OptionalBoolean empty = matrix.right(0, 1);   // Returns OptionalBoolean.empty() - no column to the right
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return an OptionalBoolean containing the element at position (rowIndex, columnIndex + 1), or empty if columnIndex == columnCount - 1
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public OptionalBoolean right(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        return columnIndex == columnCount - 1 ? OptionalBoolean.empty() : OptionalBoolean.of(a[rowIndex][columnIndex + 1]);
    }

    /**
     * Returns the specified row as a boolean array.
     *
     * <p><b>Note:</b> This method returns a reference to the internal array, not a copy.
     * Modifications to the returned array will affect the matrix. If you need an independent
     * copy, use {@code Arrays.copyOf(matrix.rowView(i), matrix.columnCount())}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, false}, {false, true, false}});
     * boolean[] firstRow = matrix.rowView(0);   // Returns [true, false, false]
     *
     * // Direct modification affects the matrix
     * firstRow[0] = false;  // matrix now has false at position (0,0)
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return the specified row array (direct reference to internal storage)
     * @throws IllegalArgumentException if rowIndex &lt; 0 or rowIndex &gt;= rowCount
     */
    @Override
    public boolean[] rowView(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rowCount, MSG_ROW_INDEX_OUT_OF_BOUNDS, rowIndex, rowCount);

        return a[rowIndex];
    }

    /**
     * Returns a defensive copy of the specified row.
     * Changes to the returned array do not affect this matrix.
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return a new boolean array containing the values from the specified row
     * @throws IllegalArgumentException if rowIndex &lt; 0 or rowIndex &gt;= rowCount
     */
    @Override
    public boolean[] rowCopy(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rowCount, MSG_ROW_INDEX_OUT_OF_BOUNDS, rowIndex, rowCount);

        return N.copyOf(a[rowIndex], columnCount);
    }

    /**
     * Returns a copy of the specified column as a new boolean array.
     *
     * <p>Unlike {@link #rowView(int)}, this method always returns a new array copy since
     * columns are not stored contiguously in memory. Modifications to the returned array
     * will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, false}, {false, true, false}});
     * boolean[] firstColumn = matrix.columnCopy(0);   // Returns [true, false]
     *
     * // Modification does NOT affect the matrix (it's a copy)
     * firstColumn[0] = false;  // matrix still has true at position (0,0)
     * }</pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing the values from the specified column
     * @throws IllegalArgumentException if columnIndex &lt; 0 or columnIndex &gt;= columnCount
     */
    @Override
    public boolean[] columnCopy(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < columnCount, MSG_COLUMN_INDEX_OUT_OF_BOUNDS, columnIndex, columnCount);

        final boolean[] c = new boolean[rowCount];

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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, false}, {false, true, false}});
     * matrix.setRow(0, new boolean[] {false, false, false});   // First row is now [false, false, false]
     * }</pre>
     *
     * @param rowIndex the index of the row to set (0-based)
     * @param row the array of values to copy into the row; must have length equal to the number of columns
     * @throws NullPointerException if {@code row} is {@code null}
     * @throws IllegalArgumentException if rowIndex is out of bounds or row length does not match column count
     */
    public void setRow(final int rowIndex, final boolean[] row) throws IllegalArgumentException {
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, false}, {false, true, false}});
     * matrix.setColumn(0, new boolean[] {false, false});   // First column is now [false, false]
     * }</pre>
     *
     * @param columnIndex the index of the column to set (0-based)
     * @param column the array of values to copy into the column; must have length equal to the number of rows
     * @throws NullPointerException if {@code column} is {@code null}
     * @throws IllegalArgumentException if columnIndex is out of bounds or column length does not match row count
     */
    public void setColumn(final int columnIndex, final boolean[] column) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < columnCount, MSG_COLUMN_INDEX_OUT_OF_BOUNDS, columnIndex, columnCount);
        N.checkArgument(column.length == rowCount, MSG_COLUMN_LENGTH_MISMATCH, rowCount, column.length);

        for (int i = 0; i < rowCount; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all elements in a row in-place by applying the specified operator to each element.
     * This modifies the matrix directly.
     *
     * <p>The operator is applied to each element in the specified row sequentially
     * from left to right (column 0 to column columnCount-1).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, true}, {false, true, false}});
     * matrix.updateRow(0, val -> !val);   // Inverts all values in row 0
     * // Row 0 is now [false, true, false]
     *
     * // Set all to true
     * matrix.updateRow(1, val -> true);
     * // Row 1 is now [true, true, true]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param rowIndex the index of the row to update (0-based)
     * @param operator the operator to apply to each element in the row; receives the current
     *             element value and returns the new value
     * @throws ArrayIndexOutOfBoundsException if rowIndex is out of bounds
     * @throws IllegalArgumentException if operator is null
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.BooleanUnaryOperator<E> operator) throws E {
        if (rowIndex < 0 || rowIndex >= rowCount) {
            throw new ArrayIndexOutOfBoundsException(formatMsg(MSG_ROW_INDEX_OUT_OF_BOUNDS, rowIndex, rowCount));
        }

        N.checkArgNotNull(operator, "operator");

        for (int i = 0; i < columnCount; i++) {
            a[rowIndex][i] = operator.applyAsBoolean(a[rowIndex][i]);
        }
    }

    /**
     * Updates all elements in the specified column in-place by applying the specified operator to each element.
     * This modifies the matrix directly.
     *
     * <p>The operator is applied to each element in the specified column sequentially
     * from top to bottom (row 0 to row rowCount-1).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, true}, {false, true, false}});
     * matrix.updateColumn(1, val -> !val);   // Inverts all values in column 1
     * // Column 1 is now [true, false]
     *
     * // Set all to false
     * matrix.updateColumn(0, val -> false);
     * // Column 0 is now [false, false]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param columnIndex the index of the column to update (0-based)
     * @param operator the operator to apply to each element in the column; receives the current
     *             element value and returns the new value
     * @throws ArrayIndexOutOfBoundsException if columnIndex is out of bounds
     * @throws IllegalArgumentException if operator is null
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.BooleanUnaryOperator<E> operator) throws E {
        if (columnIndex < 0 || columnIndex >= columnCount) {
            throw new ArrayIndexOutOfBoundsException(formatMsg(MSG_COLUMN_INDEX_OUT_OF_BOUNDS, columnIndex, columnCount));
        }

        N.checkArgNotNull(operator, "operator");

        for (int i = 0; i < rowCount; i++) {
            a[i][columnIndex] = operator.applyAsBoolean(a[i][columnIndex]);
        }
    }

    /**
     * Returns a copy of the main diagonal elements (upper-left to lower-right).
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>This method extracts the main diagonal elements at positions (0,0), (1,1), (2,2), etc.
     * The returned array is a copy; modifications to it will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, false},
     *     {false, true, false},
     *     {false, false, true}
     * });
     * boolean[] diagonal = matrix.getMainDiagonal();   // Returns [true, true, true]
     * }</pre>
     *
     * @return a new boolean array containing a copy of the main diagonal elements
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public boolean[] getMainDiagonal() throws IllegalStateException {
        checkIfRowAndColumnSizeAreSame();

        final boolean[] res = new boolean[rowCount];

        for (int i = 0; i < rowCount; i++) {
            res[i] = a[i][i]; // NOSONAR
        }

        return res;
    }

    /**
     * Sets the elements on the main diagonal from upper-left to lower-right (main diagonal).
     * The matrix must be square (rowCount == columnCount), and the diagonal array must have
     * exactly as many elements as the matrix has rows.
     *
     * <p>This method sets the main diagonal elements at positions (0,0), (1,1), (2,2), etc.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, false},
     *     {false, true, false},
     *     {false, false, true}
     * });
     * matrix.setMainDiagonal(new boolean[] {false, false, false});
     * // Diagonal is now all false
     * }</pre>
     *
     * @param mainDiagonal the new values for the main diagonal; must have length equal to rowCount 
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     * @throws IllegalArgumentException if mainDiagonal array length does not equal rowCount
     */
    public void setMainDiagonal(final boolean[] mainDiagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(N.len(mainDiagonal) == rowCount, MSG_DIAGONAL_LENGTH_MISMATCH, rowCount, N.len(mainDiagonal));

        for (int i = 0; i < rowCount; i++) {
            a[i][i] = mainDiagonal[i];
        }
    }

    /**
     * Updates the values on the main diagonal (upper-left to lower-right) by applying the specified operator.
     * The matrix must be square.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, false},
     *     {false, true, false},
     *     {false, false, false}
     * });
     * matrix.updateMainDiagonal(val -> !val);   // Invert diagonal
     * // Diagonal is now [false, false, true]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param operator the operator to apply to each diagonal element; receives current element value and returns new value
     * @throws IllegalStateException if the matrix is not square
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateMainDiagonal(final Throwables.BooleanUnaryOperator<E> operator) throws IllegalStateException, E {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgNotNull(operator, "operator");

        for (int i = 0; i < rowCount; i++) {
            a[i][i] = operator.applyAsBoolean(a[i][i]);
        }
    }

    /**
     * Returns a copy of the elements on the anti-diagonal from upper-right to lower-left.
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>This method extracts the anti-diagonal (secondary diagonal) elements from
     * top-right to bottom-left, at positions (0,n-1), (1,n-2), (2,n-3), etc.
     * The returned array is a copy; modifications to it will not affect the matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, true},
     *     {false, true, false},
     *     {true, false, false}
     * });
     * boolean[] antiDiag = matrix.getAntiDiagonal();   // Returns [true, true, true]
     * }</pre>
     *
     * @return a new boolean array containing a copy of the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public boolean[] getAntiDiagonal() throws IllegalStateException {
        checkIfRowAndColumnSizeAreSame();

        final boolean[] res = new boolean[rowCount];

        for (int i = 0; i < rowCount; i++) {
            res[i] = a[i][columnCount - i - 1];
        }

        return res;
    }

    /**
     * Sets the elements on the anti-diagonal from upper-right to lower-left (anti-diagonal).
     * The matrix must be square (rowCount == columnCount), and the diagonal array must have
     * exactly as many elements as the matrix has rows.
     *
     * <p>This method sets the anti-diagonal (secondary diagonal) elements from
     * top-right to bottom-left, at positions (0,n-1), (1,n-2), (2,n-3), etc.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, false},
     *     {false, true, false},
     *     {false, false, true}
     * });
     * matrix.setAntiDiagonal(new boolean[] {true, true, true});
     * // Anti-diagonal is now all true
     * }</pre>
     *
     * @param antiDiagonal the new values for the anti-diagonal; must have length equal to rowCount 
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     * @throws IllegalArgumentException if antiDiagonal array length does not equal rowCount
     */
    public void setAntiDiagonal(final boolean[] antiDiagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(N.len(antiDiagonal) == rowCount, MSG_DIAGONAL_LENGTH_MISMATCH, rowCount, N.len(antiDiagonal));

        for (int i = 0; i < rowCount; i++) {
            a[i][columnCount - i - 1] = antiDiagonal[i];
        }
    }

    /**
     * Updates the values on the anti-diagonal (upper-right to lower-left) by applying the specified operator.
     * The matrix must be square.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, true},
     *     {false, true, false},
     *     {true, false, false}
     * });
     * matrix.updateAntiDiagonal(val -> !val);   // Invert anti-diagonal
     * // Anti-diagonal is now [false, false, false]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param operator the operator to apply to each anti-diagonal element; receives current element value and returns new value
     * @throws IllegalStateException if the matrix is not square
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateAntiDiagonal(final Throwables.BooleanUnaryOperator<E> operator) throws E {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgNotNull(operator, "operator");

        for (int i = 0; i < rowCount; i++) {
            a[i][columnCount - i - 1] = operator.applyAsBoolean(a[i][columnCount - i - 1]);
        }
    }

    /**
     * Updates all elements in the matrix in-place by applying the specified operator.
     * This modifies the matrix directly.
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.
     * Elements are processed in row-major order when executed sequentially.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * matrix.updateAll(val -> !val);   // Inverts all values in the matrix
     * // Matrix is now [[false, true], [true, false]]
     *
     * // Set all to true
     * matrix.updateAll(val -> true);
     * // Matrix is now [[true, true], [true, true]]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param operator the operator to apply to each element; receives the current element value
     *             and returns the new value
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.BooleanUnaryOperator<E> operator) throws E {
        N.checkArgNotNull(operator, "operator");
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> a[i][j] = operator.applyAsBoolean(a[i][j]);
        Matrices.forEachIndices(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));
    }

    /**
     * Updates all elements in the matrix in-place based on their position (row and column indices).
     * This modifies the matrix directly.
     *
     * <p>The operator receives the row and column indices for each element and returns the new value
     * for that position. This is useful for initializing matrices based on position patterns or
     * mathematical formulas. The operation may be performed in parallel for large matrices.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[3][3]);
     * matrix.updateAll((i, j) -> i == j);   // Sets main diagonal to true, others to false
     * // Matrix is now [[true, false, false], [false, true, false], [false, false, true]]
     *
     * // Create a checkerboard pattern
     * matrix.updateAll((i, j) -> (i + j) % 2 == 0);
     * // Matrix is now [[true, false, true], [false, true, false], [true, false, true]]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param operator the operator that receives row index and column index (0-based) and returns
     *             the new value for that position
     * @throws E if the operator throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<Boolean, E> operator) throws E {
        N.checkArgNotNull(operator, "operator");
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> a[i][j] = operator.apply(i, j);
        Matrices.forEachIndices(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * matrix.replaceIf(val -> val == false, true);   // Replace all false values with true
     * // Matrix is now [[true, true], [true, true]]
     *
     * // Replace all true values with false
     * BooleanMatrix matrix2 = BooleanMatrix.of(new boolean[][] {{true, false}, {true, false}});
     * matrix2.replaceIf(val -> val, false);
     * // Matrix2 is now [[false, false], [false, false]]
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition to test each element; elements for which this returns
     *                  {@code true} will be replaced
     * @param newValue the value to use for replacing matching elements
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.BooleanPredicate<E> predicate, final boolean newValue) throws E {
        N.checkArgNotNull(predicate, "predicate");
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrices.forEachIndices(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[3][3]);
     * matrix.replaceIf((i, j) -> i == j, true);   // Set main diagonal to true
     * // Matrix is now [[true, false, false], [false, true, false], [false, false, true]]
     *
     * // Set upper triangle to true
     * matrix.replaceIf((i, j) -> i < j, true);
     * // Matrix is now [[true, true, true], [false, true, true], [false, false, true]]
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param predicate the condition that tests row index and column index (0-based); elements
     *                  at positions for which this returns {@code true} will be replaced
     * @param newValue the value to use for replacing matching elements
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final boolean newValue) throws E {
        N.checkArgNotNull(predicate, "predicate");
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrices.forEachIndices(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));
    }

    /**
     * Creates a new BooleanMatrix by applying a transformation function to each element.
     * The original matrix is not modified; a new matrix with transformed values is returned.
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.
     * This is the immutable counterpart to {@link #updateAll(Throwables.BooleanUnaryOperator)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * BooleanMatrix inverted = matrix.map(val -> !val);   // Creates new matrix with inverted values
     * // inverted is [[false, true], [true, false]]
     * // original matrix remains [[true, false], [false, true]]
     *
     * // Set all to false in a new matrix
     * BooleanMatrix allFalse = matrix.map(val -> false);
     * }</pre>
     *
     * @param <E> the type of exception that the function may throw
     * @param mapper the function to apply to each element; receives the current element value
     *             and returns the transformed value
     * @return a new BooleanMatrix with transformed values
     * @throws E if the function throws an exception
     * @see #updateAll(Throwables.BooleanUnaryOperator)
     */
    public <E extends Exception> BooleanMatrix map(final Throwables.BooleanUnaryOperator<E> mapper) throws E {
        N.checkArgNotNull(mapper, "mapper");
        final boolean[][] result = new boolean[rowCount][columnCount];
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> result[i][j] = mapper.applyAsBoolean(a[i][j]);

        Matrices.forEachIndices(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Creates a new Matrix by applying a function that converts boolean values to objects of type T.
     * This operation may be executed in parallel for better performance on large matrices.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     *
     * // Convert to String matrix
     * Matrix<String> stringMatrix = matrix.mapToObj(val -> val ? "YES" : "NO", String.class);
     * // Result: [["YES", "NO"], ["NO", "YES"]]
     *
     * // Convert to Integer matrix (0/1)
     * Matrix<Integer> intMatrix = matrix.mapToObj(val -> val ? 1 : 0, Integer.class);
     * // Result: [[1, 0], [0, 1]]
     * }</pre>
     *
     * @param <T> the type of elements in the resulting matrix
     * @param <E> the type of exception that the function may throw
     * @param mapper the function to convert boolean values to type T
     * @param targetElementType the Class object for type T
     * @return a new Matrix containing the converted values
     * @throws E if the function throws an exception
     */
    public <T, E extends Exception> Matrix<T> mapToObj(final Throwables.BooleanFunction<? extends T, E> mapper, final Class<T> targetElementType) throws E {
        N.checkArgNotNull(mapper, "mapper");
        final T[][] result = Matrices.newMatrixArray(rowCount, columnCount, targetElementType);
        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> result[i][j] = mapper.apply(a[i][j]);

        Matrices.forEachIndices(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return Matrix.of(result);
    }

    /**
     * Fills all elements in the matrix with the specified value.
     * This method modifies the matrix in-place.
     *
     * <p>This is a fast operation that sets every element in the matrix to the same value,
     * effectively creating a uniform matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * matrix.fill(true);   // Sets all elements to true
     * // Matrix is now [[true, true], [true, true]]
     *
     * matrix.fill(false);   // Sets all elements to false
     * // Matrix is now [[false, false], [false, false]]
     * }</pre>
     *
     * @param val the boolean value to fill the matrix with
     */
    public void fill(final boolean val) {
        for (int i = 0; i < rowCount; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills the matrix with values from the provided two-dimensional array, starting from position (0, 0).
     * The copy continues for the size of the input array or until the matrix boundaries are reached.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[3][3]);   // 3x3 of false
     * matrix.copyFrom(new boolean[][] {{true, true}, {true, true}});
     * // Top-left 2x2 region is now true, rest remains false
     * }</pre>
     *
     * @param b the two-dimensional boolean array to copy values from; must not be null
     */
    public void copyFrom(final boolean[][] b) {
        copyFrom(0, 0, b);
    }

    /**
     * Fills a portion of the matrix with values from the provided two-dimensional array.
     * Copying starts at the specified position and continues for the size of the input array
     * or until the matrix boundaries are reached. If the input array extends beyond the matrix
     * boundaries, only the overlapping portion is copied.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[4][4]);   // 4x4 of false
     * matrix.copyFrom(1, 1, new boolean[][] {{true, true}, {true, true}});
     * // 2x2 region starting at (1,1) is now true, rest remains false
     * }</pre>
     *
     * @param targetStartRowIndex the target row index in this matrix (0-based)
     * @param targetStartColumnIndex the target column index in this matrix (0-based)
     * @param b the source array to copy values from
     * @throws IllegalArgumentException if the target indices are negative or exceed matrix dimensions
     */
    public void copyFrom(final int targetStartRowIndex, final int targetStartColumnIndex, final boolean[][] b) throws IllegalArgumentException {
        N.checkArgNotNull(b, "b");
        N.checkArgument(targetStartRowIndex >= 0 && targetStartRowIndex <= rowCount, "targetStartRowIndex({}) must be between 0 and rows({})",
                targetStartRowIndex, rowCount);
        N.checkArgument(targetStartColumnIndex >= 0 && targetStartColumnIndex <= columnCount,
                "targetStartColumnIndex({}) must be between 0 and columnCount({})", targetStartColumnIndex, columnCount);

        for (int i = 0, minLen = N.min(rowCount - targetStartRowIndex, b.length); i < minLen; i++) {
            if (b[i] != null) {
                N.copy(b[i], 0, a[i + targetStartRowIndex], targetStartColumnIndex, N.min(b[i].length, columnCount - targetStartColumnIndex));
            }
        }
    }

    /**
     * Returns a copy of this matrix.
     * The returned matrix is a completely independent copy; modifications to one
     * do not affect the other.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix original = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * BooleanMatrix copy = original.copy();
     *
     * // Modifying the copy does NOT affect the original
     * copy.set(0, 0, false);
     * assert original.get(0, 0) == true;   // Original unchanged
     * assert copy.get(0, 0)     == false;  // Copy modified
     * }</pre>
     *
     * @return a new matrix that is a copy of this matrix with full independence guarantee
     */
    @Override
    public BooleanMatrix copy() {
        final boolean[][] c = new boolean[rowCount][];

        for (int i = 0; i < rowCount; i++) {
            c[i] = a[i].clone();
        }

        return new BooleanMatrix(c);
    }

    /**
     * Creates a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows and is completely independent from the original matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false},
     *     {false, true},
     *     {true, true}
     * });
     * BooleanMatrix subset = matrix.copy(1, 3);   // Rows 1 and 2
     * // Result: [[false, true], [true, true]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new BooleanMatrix containing the specified rows
     * @throws IndexOutOfBoundsException if {@code fromRowIndex} &lt; 0, {@code toRowIndex} &gt; rowCount,
     *         or {@code fromRowIndex} &gt; {@code toRowIndex}
     */
    @Override
    public BooleanMatrix copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        checkRepresentableShape(toRowIndex - fromRowIndex, columnCount);

        final boolean[][] c = new boolean[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new BooleanMatrix(c);
    }

    /**
     * Creates a copy of a rectangular region from this matrix.
     * The returned matrix contains only the elements in the specified row and column range,
     * preserving their relative positions from the original matrix.
     *
     * <p>This method is useful for extracting sub-matrices or working with specific regions
     * of a larger matrix. The copy is independent of the original matrix - modifications to
     * either will not affect the other.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, true, false},
     *     {false, true, false, true},
     *     {true, true, false, false}
     * });
     * BooleanMatrix subMatrix = matrix.copy(0, 2, 1, 3);   // Copy rows 0-1, columns 1-2
     * // Result: [[false, true], [true, false]]
     *
     * // Extract a single column as a matrix
     * BooleanMatrix col = matrix.copy(0, 3, 2, 3);   // All rows, column 2 only
     * // Result: [[true], [false], [false]]
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new BooleanMatrix containing the specified rectangular region
     * @throws IndexOutOfBoundsException if {@code fromRowIndex} &lt; 0, {@code toRowIndex} &gt; rowCount,
     *         {@code fromColumnIndex} &lt; 0, {@code toColumnIndex} &gt; columnCount,
     *         {@code fromRowIndex} &gt; {@code toRowIndex}, or {@code fromColumnIndex} &gt; {@code toColumnIndex}
     */
    @Override
    public BooleanMatrix copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex)
            throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);
        checkRepresentableShape(toRowIndex - fromRowIndex, toColumnIndex - fromColumnIndex);
        final boolean[][] c = new boolean[toRowIndex - fromRowIndex][];

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new BooleanMatrix(c);
    }

    /**
     * Creates a new matrix by extending or truncating this matrix to the specified dimensions.
     * New cells are filled with {@code false}.
     *
     * <p>If the new dimensions are smaller than the current dimensions, the matrix is truncated.
     * If larger, the existing content is preserved in the top-left corner and new cells are filled with false.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * BooleanMatrix extended = matrix.resize(3, 3);
     * // Result: [[true, false, false],
     * //          [false, true, false],
     * //          [false, false, false]]
     * }</pre>
     *
     * @param newRowCount the number of rows in the new matrix. It can be smaller than the row number of current matrix but must be non-negative
     * @param newColumnCount the number of columns in the new matrix. It can be smaller than the column number of current matrix but must be non-negative
     * @return a new BooleanMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRowCount} or {@code newColumnCount} is negative
     */
    public BooleanMatrix resize(final int newRowCount, final int newColumnCount) {
        return resize(newRowCount, newColumnCount, false);
    }

    /**
     * Creates a new matrix by extending or truncating this matrix to the specified dimensions.
     * New cells created during extension are filled with the specified default value.
     *
     * <p>If the new dimensions are smaller than the current dimensions, the matrix is truncated
     * from the top-left corner. If larger, the existing content is preserved in the top-left
     * corner and new cells are filled with the specified default value. This method provides
     * more control over the fill value compared to {@link #resize(int, int)}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * BooleanMatrix extended = matrix.resize(3, 4, true);   // Extend to 3x4, fill new cells with true
     * // Result: [[true, false, true, true],
     * //          [false, true, true, true],
     * //          [true, true, true, true]]
     *
     * // Truncate to smaller size
     * BooleanMatrix truncated = matrix.resize(1, 1, false);   // Keep only top-left element
     * // Result: [[true]]
     * }</pre>
     *
     * @param newRowCount the number of rows in the new matrix. It can be smaller than the row number of current matrix but must be non-negative
     * @param newColumnCount the number of columns in the new matrix. It can be smaller than the column number of current matrix but must be non-negative
     * @param defaultValueForNewCell the boolean value to fill new cells with during extension
     * @return a new BooleanMatrix with the specified dimensions
     * @throws IllegalArgumentException if {@code newRowCount} or {@code newColumnCount} is negative,
     *         or if the resulting matrix would be too large (dimensions exceeding Integer.MAX_VALUE elements)
     */
    public BooleanMatrix resize(final int newRowCount, final int newColumnCount, final boolean defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRowCount >= 0, MSG_NEGATIVE_DIMENSION, "newRowCount", newRowCount);
        N.checkArgument(newColumnCount >= 0, MSG_NEGATIVE_DIMENSION, "newColumnCount", newColumnCount);
        checkRepresentableShape(newRowCount, newColumnCount);
        // Check for overflow before allocation
        if ((long) newRowCount * newColumnCount > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Matrix dimensions overflow: " + newRowCount + " x " + newColumnCount + " exceeds Integer.MAX_VALUE");
        }

        if (newRowCount <= rowCount && newColumnCount <= columnCount) {
            return copy(0, newRowCount, 0, newColumnCount);
        } else {
            // NOSONAR
            final boolean[][] b = new boolean[newRowCount][];

            for (int i = 0; i < newRowCount; i++) {
                b[i] = i < rowCount ? N.copyOf(a[i], newColumnCount) : new boolean[newColumnCount];

                if (defaultValueForNewCell) {
                    if (i >= rowCount) {
                        N.fill(b[i], true);
                    } else if (columnCount < newColumnCount) {
                        N.fill(b[i], columnCount, newColumnCount, true);
                    }
                }
            }

            return new BooleanMatrix(b);
        }
    }

    /**
     * Creates a new matrix by extending this matrix in all four directions.
     * New cells are filled with {@code false}.
     *
     * <p>This method adds padding around the existing matrix, with the original content
     * positioned according to the specified padding amounts.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, true}});
     * BooleanMatrix extended = matrix.extend(1, 1, 1, 1);
     * // Result: [[false, false, false, false],
     * //          [false, true,  true,  false],
     * //          [false, false, false, false]]
     * }</pre>
     *
     * @param toUp number of rows to add above; must be non-negative
     * @param toDown number of rows to add below; must be non-negative
     * @param toLeft number of columns to add to the left; must be non-negative
     * @param toRight number of columns to add to the right; must be non-negative
     * @return a new extended BooleanMatrix with dimensions ((toUp+rowCount+toDown) x (toLeft+columnCount+toRight))
     * @throws IllegalArgumentException if any parameter is negative
     */
    public BooleanMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, false);
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, true}});
     * BooleanMatrix padded = matrix.extend(1, 1, 2, 2, true);
     * // Result: [[true, true, true, true, true, true],
     * //          [true, true, true, true, true, true],
     * //          [true, true, true, true, true, true]]
     *
     * // Add border of false values
     * BooleanMatrix bordered = matrix.extend(1, 1, 1, 1, false);
     * // Result: [[false, false, false, false],
     * //          [false, true,  true,  false],
     * //          [false, false, false, false]]
     * }</pre>
     *
     * @param toUp number of rows to add above; must be non-negative
     * @param toDown number of rows to add below; must be non-negative
     * @param toLeft number of columns to add to the left; must be non-negative
     * @param toRight number of columns to add to the right; must be non-negative
     * @param defaultValueForNewCell the boolean value to fill all new cells with
     * @return a new extended BooleanMatrix with dimensions ((toUp+rowCount+toDown) x (toLeft+columnCount+toRight))
     * @throws IllegalArgumentException if any padding parameter is negative,
     *         or if the resulting dimensions would exceed Integer.MAX_VALUE
     */
    public BooleanMatrix extend(final int toUp, final int toDown, final int toLeft, final int toRight, final boolean defaultValueForNewCell)
            throws IllegalArgumentException {
        N.checkArgument(toUp >= 0, MSG_NEGATIVE_DIMENSION, "toUp", toUp);
        N.checkArgument(toDown >= 0, MSG_NEGATIVE_DIMENSION, "toDown", toDown);
        N.checkArgument(toLeft >= 0, MSG_NEGATIVE_DIMENSION, "toLeft", toLeft);
        N.checkArgument(toRight >= 0, MSG_NEGATIVE_DIMENSION, "toRight", toRight);

        if (toUp == 0 && toDown == 0 && toLeft == 0 && toRight == 0) {
            return copy();
        } else {
            if ((long) toUp + rowCount + toDown > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Result row count overflow: " + toUp + " + " + rowCount + " + " + toDown + " exceeds Integer.MAX_VALUE");
            }

            if ((long) toLeft + columnCount + toRight > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(
                        "Result column count overflow: " + toLeft + " + " + columnCount + " + " + toRight + " exceeds Integer.MAX_VALUE");
            }

            final int newRowCount = toUp + rowCount + toDown;
            final int newColumnCount = toLeft + columnCount + toRight;
            checkRepresentableShape(newRowCount, newColumnCount);
            // NOSONAR
            final boolean[][] b = new boolean[newRowCount][newColumnCount];

            for (int i = 0; i < newRowCount; i++) {
                if (i >= toUp && i < toUp + rowCount) {
                    N.copy(a[i - toUp], 0, b[i], toLeft, columnCount);
                }

                if (defaultValueForNewCell) {
                    if (i < toUp || i >= toUp + rowCount) {
                        N.fill(b[i], true);
                    } else if (columnCount < newColumnCount) {
                        if (toLeft > 0) {
                            N.fill(b[i], 0, toLeft, true);
                        }

                        if (toRight > 0) {
                            N.fill(b[i], columnCount + toLeft, newColumnCount, true);
                        }
                    }
                }
            }

            return new BooleanMatrix(b);
        }
    }

    /**
     * Reverses the order of elements in each row in-place (horizontal flip).
     * This modifies the current matrix; each row is reversed left-to-right.
     *
     * <p>This is an in-place operation that modifies the current matrix.
     * For a non-destructive version that returns a new matrix, use {@link #flipHorizontally()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, true, false}, {false, true, true}});
     * matrix.flipInPlaceHorizontally();
     * // matrix is now [[false, true, true], [true, true, false]]
     * }</pre>
     *
     * @see #flipHorizontally()
     * @see #flipInPlaceVertically()
     */
    public void flipInPlaceHorizontally() {
        for (int i = 0; i < rowCount; i++) {
            N.reverse(a[i]);
        }
    }

    /**
     * Reverses the order of rows in-place (vertical flip).
     * This modifies the current matrix; the order of rows is reversed top-to-bottom
     * while the order of elements within each row remains unchanged.
     *
     * <p>This is an in-place operation that modifies the current matrix.
     * For a non-destructive version that returns a new matrix, use {@link #flipVertically()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {true, true}, {false, true}});
     * matrix.flipInPlaceVertically();
     * // matrix is now [[false, true], [true, true], [true, false]]
     * }</pre>
     *
     * @see #flipVertically()
     * @see #flipInPlaceHorizontally()
     */
    public void flipInPlaceVertically() {
        for (int j = 0; j < columnCount; j++) {
            boolean tmp = false;
            for (int l = 0, h = rowCount - 1; l < h;) {
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, false}, {true, true, false}});
     * BooleanMatrix flipped = matrix.flipHorizontally();
     * // flipped is: {{false, false, true}, {false, true, true}}
     * }</pre>
     *
     * @return a new BooleanMatrix with each row reversed
     * @see #flipInPlaceHorizontally()
     * @see #flipVertically()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">MATLAB flip function</a>
     */
    public BooleanMatrix flipHorizontally() {
        final BooleanMatrix res = this.copy();
        res.flipInPlaceHorizontally();
        return res;
    }

    /**
     * Returns a new matrix that is a vertical flip of this matrix (rows in reversed order).
     * The rows are reversed top-to-bottom (the topmost row becomes bottommost).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, false}, {true, true, false}});
     * BooleanMatrix flipped = matrix.flipVertically();
     * // flipped is: {{true, true, false}, {true, false, false}}
     * }</pre>
     *
     * @return a new BooleanMatrix with rows reversed
     * @see #flipInPlaceVertically()
     * @see #flipHorizontally()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">MATLAB flip function</a>
     */
    public BooleanMatrix flipVertically() {
        final BooleanMatrix res = this.copy();
        res.flipInPlaceVertically();
        return res;
    }

    /**
     * Returns a new matrix that is this matrix rotated 90 degrees clockwise.
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the last row of the original, reading upward.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:             Rotated 90 clockwise:
     * // true  false false     true  true
     * // true  true  false     true  false
     * //                       false false
     * }</pre>
     *
     * @return a new matrix rotated 90 degrees clockwise
     */
    @Override
    public BooleanMatrix rotate90() {
        if (columnCount == 0) {
            return EMPTY_BOOLEAN_MATRIX;
        }

        checkRepresentableShape(columnCount, rowCount);

        final boolean[][] c = new boolean[columnCount][rowCount];

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

        return new BooleanMatrix(c);
    }

    /**
     * Returns a new matrix that is this matrix rotated 180 degrees clockwise.
     * This is equivalent to flipping both horizontally and vertically, reversing the
     * order of all elements. The resulting matrix has the same dimensions as the original.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:             Rotated 180:
     * // true  false false     false true  true
     * // true  true  false     false false true
     * }</pre>
     *
     * @return a new matrix rotated 180 degrees clockwise
     */
    @Override
    public BooleanMatrix rotate180() {
        final boolean[][] c = new boolean[rowCount][];

        for (int i = 0; i < rowCount; i++) {
            c[i] = a[rowCount - i - 1].clone();
            N.reverse(c[i]);
        }

        return new BooleanMatrix(c);
    }

    /**
     * Returns a new matrix that is this matrix rotated 270 degrees clockwise (or 90 degrees counter-clockwise).
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the first row of the original, reading downward.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:             Rotated 270 clockwise:
     * // true  false false     false false
     * // true  true  false     false true
     * //                       true  true
     * }</pre>
     *
     * @return a new matrix rotated 270 degrees clockwise
     */
    @Override
    public BooleanMatrix rotate270() {
        if (columnCount == 0) {
            return EMPTY_BOOLEAN_MATRIX;
        }

        checkRepresentableShape(columnCount, rowCount);

        final boolean[][] c = new boolean[columnCount][rowCount];

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

        return new BooleanMatrix(c);
    }

    /**
     * Creates the transpose of this matrix by swapping rows and columns.
     * The transpose operation converts each row into a column, so element at position (i, j)
     * in the original matrix appears at position (j, i) in the transposed matrix. The resulting
     * matrix has dimensions swapped (rowCount x columnCount becomes columnCount x rowCount).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:        Transposed:
     * // true false false true  false
     * // false true false false true
     * //                  false false
     *
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, false}, {false, true, false}});
     * BooleanMatrix transposed = matrix.transpose();   // 2×3 becomes 3×2
     * }</pre>
     *
     * @return a new matrix that is the transpose of this matrix with dimensions columnCount x rowCount
     */
    @Override
    public BooleanMatrix transpose() {
        if (columnCount == 0) {
            return EMPTY_BOOLEAN_MATRIX;
        }

        checkRepresentableShape(columnCount, rowCount);

        final boolean[][] c = new boolean[columnCount][rowCount];

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

        return new BooleanMatrix(c);
    }

    /**
     * Reshapes this matrix to the specified dimensions.
     * Elements are read from the original matrix in row-major order (row by row, left to right)
     * and placed into the new matrix shape in the same order. The new shape must have at least
     * as many total elements as the original ({@code newRowCount * newColumnCount >= elementCount()}).
     *
     * <p>If the new shape requires more elements than available, the excess positions
     * will be filled with {@code false}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, true, false}});
     * BooleanMatrix reshaped = matrix.reshape(2, 2);
     * // Result: [[true, false],
     * //          [true, false]]
     * }</pre>
     *
     * @param newRowCount the number of rows in the reshaped matrix; must be non-negative
     * @param newColumnCount the number of columns in the reshaped matrix; must be non-negative
     * @return a new BooleanMatrix with the specified shape
     * @throws IllegalArgumentException if the new shape is too small to hold all elements
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public BooleanMatrix reshape(final int newRowCount, final int newColumnCount) {
        N.checkArgument(newRowCount >= 0, MSG_NEGATIVE_DIMENSION, "newRowCount", newRowCount);
        N.checkArgument(newColumnCount >= 0, MSG_NEGATIVE_DIMENSION, "newColumnCount", newColumnCount);
        checkRepresentableShape(newRowCount, newColumnCount);
        N.checkArgument((long) newRowCount * newColumnCount >= elementCount(), "New shape [{}x{}={}] is too small to hold all {} elements", newRowCount,
                newColumnCount, (long) newRowCount * newColumnCount, elementCount());

        final boolean[][] c = new boolean[newRowCount][newColumnCount];

        if (newRowCount == 0 || newColumnCount == 0 || N.isEmpty(a)) {
            return new BooleanMatrix(c);
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

        return new BooleanMatrix(c);
    }

    /**
     * Repeats each element in the matrix the specified number of times in both dimensions.
     * Each element is expanded into a rowRepeats x columnRepeats block.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // [[true, false]] with repeatElements(2, 3) becomes:
     * // [[true, true, true, false, false, false],
     * //  [true, true, true, false, false, false]]
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}});
     * BooleanMatrix repeated = matrix.repeatElements(2, 3);
     * }</pre>
     *
     * @param rowRepeats number of times to repeat each element vertically
     * @param columnRepeats number of times to repeat each element horizontally
     * @return a new BooleanMatrix with dimensions (rowCount*rowRepeats x columnCount*columnRepeats)
     * @throws IllegalArgumentException if rowRepeats or columnRepeats is not positive
     */
    @Override
    public BooleanMatrix repeatElements(final int rowRepeats, final int columnRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && columnRepeats > 0, MSG_REPEATS_NOT_POSITIVE, rowRepeats, columnRepeats);

        // Check for overflow before allocation
        if ((long) rowCount * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result row count overflow: " + rowCount + " * " + rowRepeats + " exceeds Integer.MAX_VALUE");
        }
        if ((long) columnCount * columnRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result column count overflow: " + columnCount + " * " + columnRepeats + " exceeds Integer.MAX_VALUE");
        }

        final boolean[][] c = new boolean[rowCount * rowRepeats][columnCount * columnRepeats];

        for (int i = 0; i < rowCount; i++) {
            final boolean[] aa = a[i];
            final boolean[] fr = c[i * rowRepeats];

            for (int j = 0; j < columnCount; j++) {
                N.copy(Array.repeat(aa[j], columnRepeats), 0, fr, j * columnRepeats, columnRepeats);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new BooleanMatrix(c);
    }

    /**
     * Repeats the entire matrix the specified number of times in both dimensions.
     * The matrix is tiled rowRepeats times vertically and columnRepeats times horizontally.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // [[true, false]] with repeatMatrix(2, 3) becomes:
     * // [[true, false, true, false, true, false],
     * //  [true, false, true, false, true, false]]
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}});
     * BooleanMatrix tiled = matrix.repeatMatrix(2, 3);
     * }</pre>
     *
     * @param rowRepeats number of times to repeat the matrix vertically
     * @param columnRepeats number of times to repeat the matrix horizontally
     * @return a new BooleanMatrix with dimensions (rowCount*rowRepeats x columnCount*columnRepeats)
     * @throws IllegalArgumentException if rowRepeats or columnRepeats is not positive
     */
    @Override
    public BooleanMatrix repeatMatrix(final int rowRepeats, final int columnRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && columnRepeats > 0, MSG_REPEATS_NOT_POSITIVE, rowRepeats, columnRepeats);

        // Check for overflow before allocation
        if ((long) rowCount * rowRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result row count overflow: " + rowCount + " * " + rowRepeats + " exceeds Integer.MAX_VALUE");
        }
        if ((long) columnCount * columnRepeats > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Result column count overflow: " + columnCount + " * " + columnRepeats + " exceeds Integer.MAX_VALUE");
        }

        final boolean[][] c = new boolean[rowCount * rowRepeats][columnCount * columnRepeats];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnRepeats; j++) {
                N.copy(a[i], 0, c[i], j * columnCount, columnCount);
            }
        }

        for (int i = 1; i < rowRepeats; i++) {
            for (int j = 0; j < rowCount; j++) {
                N.copy(c[j], 0, c[i * rowCount + j], 0, c[j].length);
            }
        }

        return new BooleanMatrix(c);
    }

    /**
     * Returns a list containing all matrix elements in row-major order.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * BooleanList list = matrix.flatten();   // Returns BooleanList of true, false, false, true
     * }</pre>
     *
     * @return a list of all elements in row-major order
     * @throws IllegalStateException if the matrix is too large to flatten (rowCount * columnCount &gt; Integer.MAX_VALUE)
     */
    @Override
    public BooleanList flatten() {
        // Check for overflow before allocation
        if ((long) rowCount * columnCount > Integer.MAX_VALUE) {
            throw new IllegalStateException("Matrix too large to flatten: " + rowCount + " x " + columnCount);
        }

        final boolean[] c = new boolean[rowCount * columnCount];

        for (int i = 0; i < rowCount; i++) {
            N.copy(a[i], 0, c, i * columnCount, columnCount);
        }

        return BooleanList.of(c);
    }

    /**
     * Flattens all elements of this matrix into a single one-dimensional array, applies the given
     * operation to that flattened array, and then copies the modified elements back into the matrix.
     *
     * <p>This enables operations that need a global view of all matrix elements (e.g., sorting all
     * elements across the entire matrix). The operation receives a temporary flattened copy; after
     * the operation completes, the modified values are written back into the matrix row by row.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * matrix.applyOnFlattened(arr -> java.util.Arrays.fill(arr, true));
     * // matrix is now [[true, true], [true, true]] (all elements set globally, then placed back row by row)
     * }</pre>
     *
     * @param <E> the type of exception that the operation may throw
     * @param action the operation to apply to the flattened array
     * @throws E if the operation throws an exception
     * @see Arrays#applyOnFlattened(boolean[][], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void applyOnFlattened(final Throwables.Consumer<? super boolean[], E> action) throws E {
        Arrays.applyOnFlattened(a, action);
    }

    /**
     * Performs element-wise logical AND of this matrix with another matrix.
     * The matrices must have the same dimensions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix a = BooleanMatrix.of(new boolean[][] {{true, false}, {true, true}});
     * BooleanMatrix b = BooleanMatrix.of(new boolean[][] {{true, true}, {false, true}});
     * BooleanMatrix result = a.and(b);   // Result: [[true, false], [false, true]]
     * }</pre>
     *
     * @param other the matrix to AND with this matrix
     * @return a new BooleanMatrix containing the element-wise logical AND
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public BooleanMatrix and(final BooleanMatrix other) throws IllegalArgumentException {
        N.checkArgNotNull(other, "other");
        N.checkArgument(Matrices.isSameShape(this, other), "Cannot AND matrices with different shapes: this is {}x{} but other is {}x{}", rowCount, columnCount,
                other.rowCount, other.columnCount);

        final boolean[][] otherData = other.a;
        final boolean[][] result = new boolean[rowCount][columnCount];
        final Throwables.IntBiConsumer<RuntimeException> elementAction = (i, j) -> result[i][j] = a[i][j] && otherData[i][j];

        Matrices.forEachIndices(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Performs element-wise logical OR of this matrix with another matrix.
     * The matrices must have the same dimensions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix a = BooleanMatrix.of(new boolean[][] {{true, false}, {false, false}});
     * BooleanMatrix b = BooleanMatrix.of(new boolean[][] {{false, true}, {false, true}});
     * BooleanMatrix result = a.or(b);   // Result: [[true, true], [false, true]]
     * }</pre>
     *
     * @param other the matrix to OR with this matrix
     * @return a new BooleanMatrix containing the element-wise logical OR
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public BooleanMatrix or(final BooleanMatrix other) throws IllegalArgumentException {
        N.checkArgNotNull(other, "other");
        N.checkArgument(Matrices.isSameShape(this, other), "Cannot OR matrices with different shapes: this is {}x{} but other is {}x{}", rowCount, columnCount,
                other.rowCount, other.columnCount);

        final boolean[][] otherData = other.a;
        final boolean[][] result = new boolean[rowCount][columnCount];
        final Throwables.IntBiConsumer<RuntimeException> elementAction = (i, j) -> result[i][j] = a[i][j] || otherData[i][j];

        Matrices.forEachIndices(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Performs element-wise logical XOR of this matrix with another matrix.
     * The matrices must have the same dimensions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix a = BooleanMatrix.of(new boolean[][] {{true, false}, {true, true}});
     * BooleanMatrix b = BooleanMatrix.of(new boolean[][] {{true, true}, {false, true}});
     * BooleanMatrix result = a.xor(b);   // Result: [[false, true], [true, false]]
     * }</pre>
     *
     * @param other the matrix to XOR with this matrix
     * @return a new BooleanMatrix containing the element-wise logical XOR
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public BooleanMatrix xor(final BooleanMatrix other) throws IllegalArgumentException {
        N.checkArgNotNull(other, "other");
        N.checkArgument(Matrices.isSameShape(this, other), "Cannot XOR matrices with different shapes: this is {}x{} but other is {}x{}", rowCount, columnCount,
                other.rowCount, other.columnCount);

        final boolean[][] otherData = other.a;
        final boolean[][] result = new boolean[rowCount][columnCount];
        final Throwables.IntBiConsumer<RuntimeException> elementAction = (i, j) -> result[i][j] = a[i][j] ^ otherData[i][j];

        Matrices.forEachIndices(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Counts the number of {@code true} elements in this matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, true}, {false, true, false}});
     * int count = matrix.countTrue();   // Returns 3
     * }</pre>
     *
     * @return the number of {@code true} elements in this matrix
     */
    public int countTrue() {
        int count = 0;

        for (int i = 0; i < rowCount; i++) {
            final boolean[] row = a[i];

            for (int j = 0; j < columnCount; j++) {
                if (row[j]) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Returns {@code true} if all elements in this matrix are {@code true}.
     * Returns {@code true} for an empty matrix (vacuous truth).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix allTrue = BooleanMatrix.of(new boolean[][] {{true, true}, {true, true}});
     * allTrue.all();   // Returns true
     *
     * BooleanMatrix mixed = BooleanMatrix.of(new boolean[][] {{true, false}, {true, true}});
     * mixed.all();     // Returns false
     * }</pre>
     *
     * @return {@code true} if every element is {@code true}, or if the matrix is empty
     */
    public boolean all() {
        for (int i = 0; i < rowCount; i++) {
            final boolean[] row = a[i];

            for (int j = 0; j < columnCount; j++) {
                if (!row[j]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns {@code true} if any element in this matrix is {@code true}.
     * Returns {@code false} for an empty matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix allFalse = BooleanMatrix.of(new boolean[][] {{false, false}, {false, false}});
     * allFalse.any();   // Returns false
     *
     * BooleanMatrix mixed = BooleanMatrix.of(new boolean[][] {{false, true}, {false, false}});
     * mixed.any();      // Returns true
     * }</pre>
     *
     * @return {@code true} if at least one element is {@code true}
     */
    public boolean any() {
        for (int i = 0; i < rowCount; i++) {
            final boolean[] row = a[i];

            for (int j = 0; j < columnCount; j++) {
                if (row[j]) {
                    return true;
                }
            }
        }

        return false;
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
     * BooleanMatrix top = BooleanMatrix.of(new boolean[][] {{true, false}});
     * BooleanMatrix bottom = BooleanMatrix.of(new boolean[][] {{false, true}});
     * BooleanMatrix stacked = top.stackVertically(bottom);
     * // Result: [[true, false],
     * //          [false, true]]
     * 
     * // Stack multiple matrices
     * BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] {{true, true}});
     * BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] {{false, false}});
     * BooleanMatrix m3 = BooleanMatrix.of(new boolean[][] {{true, false}});
     * BooleanMatrix combined = m1.stackVertically(m2).stackVertically(m3);   // 3x2 matrix
     * }</pre>
     *
     * @param other the matrix to stack below this matrix (must have the same column count)
     * @return a new BooleanMatrix with dimensions (this.rowCount + other.rowCount) x this.columnCount
     * @throws IllegalArgumentException if {@code this.columnCount != other.columnCount}
     * @see #stackHorizontally(BooleanMatrix)
     */
    public BooleanMatrix stackVertically(final BooleanMatrix other) throws IllegalArgumentException {
        N.checkArgNotNull(other, "other");
        N.checkArgument(columnCount == other.columnCount, MSG_VSTACK_COLUMN_MISMATCH, columnCount, other.columnCount);
        final long mergedRowCount = (long) rowCount + other.rowCount;
        N.checkArgument(mergedRowCount <= Integer.MAX_VALUE, "Merged row count overflow: {} + {} = {}", rowCount, other.rowCount, mergedRowCount);

        final boolean[][] c = new boolean[(int) mergedRowCount][];
        int j = 0;

        for (int i = 0; i < rowCount; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < other.rowCount; i++) {
            c[j++] = other.a[i].clone();
        }

        return BooleanMatrix.of(c);
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
     * BooleanMatrix left = BooleanMatrix.of(new boolean[][] {{true}, {false}});
     * BooleanMatrix right = BooleanMatrix.of(new boolean[][] {{false}, {true}});
     * BooleanMatrix stacked = left.stackHorizontally(right);
     * // Result: [[true, false],
     * //          [false, true]]
     * 
     * // Create a wider matrix by stacking multiple columns
     * BooleanMatrix col1 = BooleanMatrix.of(new boolean[][] {{true}, {true}, {false}});
     * BooleanMatrix col2 = BooleanMatrix.of(new boolean[][] {{false}, {true}, {true}});
     * BooleanMatrix wide = col1.stackHorizontally(col2);   // 3x2 matrix
     * }</pre>
     *
     * @param other the matrix to stack to the right of this matrix (must have the same row count)
     * @return a new BooleanMatrix with dimensions this.rowCount x (this.columnCount + other.columnCount)
     * @throws IllegalArgumentException if {@code this.rowCount != other.rowCount}
     * @see #stackVertically(BooleanMatrix)
     */
    public BooleanMatrix stackHorizontally(final BooleanMatrix other) throws IllegalArgumentException {
        N.checkArgNotNull(other, "other");
        N.checkArgument(rowCount == other.rowCount, MSG_HSTACK_ROW_MISMATCH, rowCount, other.rowCount);
        final long mergedColumnCount = (long) columnCount + other.columnCount;
        N.checkArgument(mergedColumnCount <= Integer.MAX_VALUE, "Merged column count overflow: {} + {} = {}", columnCount, other.columnCount,
                mergedColumnCount);

        final boolean[][] c = new boolean[rowCount][(int) mergedColumnCount];

        for (int i = 0; i < rowCount; i++) {
            N.copy(a[i], 0, c[i], 0, columnCount);
            N.copy(other.a[i], 0, c[i], columnCount, other.columnCount);
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix primitive = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * Matrix<Boolean> boxed = primitive.boxed();
     *
     * // Now you can use methods that work with generic types
     * Stream<Boolean> stream = boxed.streamHorizontal();
     * boxed.set(0, 0, null);   // Can use null values
     * }</pre>
     *
     * @return a new Matrix&lt;Boolean&gt; with the same dimensions and values as this matrix
     */
    public Matrix<Boolean> boxed() {
        final Boolean[][] c = new Boolean[rowCount][columnCount];

        if (rowCount <= columnCount) {
            for (int i = 0; i < rowCount; i++) {
                final boolean[] aa = a[i];
                final Boolean[] cc = c[i];

                for (int j = 0; j < columnCount; j++) {
                    cc[j] = aa[j]; // NOSONAR
                }
            }
        } else {
            for (int j = 0; j < columnCount; j++) {
                for (int i = 0; i < rowCount; i++) {
                    c[i][j] = a[i][j]; // NOSONAR
                }
            }
        }

        return new Matrix<>(c);
    }

    /**
     * Performs element-wise operation on two matrices using a binary operator.
     * The matrices must have the same dimensions. Corresponding elements from both matrices
     * are combined using the provided function to produce the result matrix.
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.
     * Creates a new matrix; the original matrices are not modified.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix a = BooleanMatrix.of(new boolean[][] {{true, false}, {true, true}});
     * BooleanMatrix b = BooleanMatrix.of(new boolean[][] {{true, true}, {false, true}});
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
     * @param matrixB the second matrix (must have the same dimensions as this matrix)
     * @param zipFunction the binary operator to apply to corresponding elements; receives
     *                    element from this matrix as first argument and element from matrixB
     *                    as second argument
     * @return a new BooleanMatrix with the results of the element-wise operation
     * @throws IllegalArgumentException if the matrices have different dimensions (shape mismatch)
     * @throws E if the zip function throws an exception
     * @see #zipWith(BooleanMatrix, BooleanMatrix, Throwables.BooleanTernaryOperator)
     */
    public <E extends Exception> BooleanMatrix zipWith(final BooleanMatrix matrixB, final Throwables.BooleanBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB), "Cannot zip matrices with different shapes: this is {}x{} but other is {}x{}", rowCount, columnCount,
                matrixB.rowCount, matrixB.columnCount);
        N.checkArgNotNull(zipFunction, "zipFunction");

        final boolean[][] matrixBData = matrixB.a;
        final boolean[][] result = new boolean[rowCount][columnCount];

        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> result[i][j] = zipFunction.applyAsBoolean(a[i][j], matrixBData[i][j]);

        Matrices.forEachIndices(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Performs element-wise operation on three matrices using a ternary operator.
     * All matrices must have the same dimensions. Corresponding elements from all three matrices
     * are combined using the provided function to produce the result matrix.
     *
     * <p>This is useful for operations that combine three matrices, such as majority vote,
     * conditional selection, or mathematical formulas involving three variables.</p>
     *
     * <p>The operation may be performed in parallel for large matrices to improve performance.
     * Creates a new matrix; the original matrices are not modified.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix a = BooleanMatrix.of(new boolean[][] {{true, false}, {true, true}});
     * BooleanMatrix b = BooleanMatrix.of(new boolean[][] {{true, true}, {false, true}});
     * BooleanMatrix c = BooleanMatrix.of(new boolean[][] {{false, true}, {true, false}});
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
     * @param matrixB the second matrix (must have the same dimensions as this matrix)
     * @param matrixC the third matrix (must have the same dimensions as this matrix)
     * @param zipFunction the ternary operator to apply to corresponding elements; receives
     *                    element from this matrix as first argument, element from matrixB as
     *                    second argument, and element from matrixC as third argument
     * @return a new BooleanMatrix with the results of the element-wise operation
     * @throws IllegalArgumentException if any matrices have different dimensions (shape mismatch)
     * @throws E if the zip function throws an exception
     * @see #zipWith(BooleanMatrix, Throwables.BooleanBinaryOperator)
     */
    public <E extends Exception> BooleanMatrix zipWith(final BooleanMatrix matrixB, final BooleanMatrix matrixC,
            final Throwables.BooleanTernaryOperator<E> zipFunction) throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(matrixB) && isSameShape(matrixC), "Cannot zip matrices with different shapes: all matrices must be {}x{}", rowCount,
                columnCount);
        N.checkArgNotNull(zipFunction, "zipFunction");

        final boolean[][] matrixBData = matrixB.a;
        final boolean[][] matrixCData = matrixC.a;
        final boolean[][] result = new boolean[rowCount][columnCount];

        final Throwables.IntBiConsumer<E> elementAction = (i, j) -> result[i][j] = zipFunction.applyAsBoolean(a[i][j], matrixBData[i][j], matrixCData[i][j]);

        Matrices.forEachIndices(rowCount, columnCount, elementAction, Matrices.isParallelizable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Returns a stream of Boolean values from the main diagonal (upper-left to lower-right).
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>This method streams the diagonal elements starting from position (0,0) and
     * proceeding to position (n-1,n-1) where n is the dimension of the square matrix.
     * This is useful for operations on diagonal matrices or extracting diagonal elements.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, false},
     *     {false, true, false},
     *     {false, false, true}
     * });
     * List<Boolean> diagonal = matrix.streamMainDiagonal().toList();   // [true, true, true]
     * 
     * // Check if it's an identity-like matrix
     * boolean allTrue = matrix.streamMainDiagonal().allMatch(b -> b);
     * }</pre>
     *
     * @return a Stream&lt;Boolean&gt; containing the diagonal elements from top-left to bottom-right
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public Stream<Boolean> streamMainDiagonal() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private final int toIndex = rowCount;
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
     * Returns a stream of Boolean values from the anti-diagonal (upper-right to lower-left).
     * The matrix must be square (same number of rows and columns).
     * 
     * <p>This method streams the anti-diagonal elements starting from position (0,n-1)
     * and proceeding to position (n-1,0) where n is the dimension of the square matrix.
     * This is useful for operations involving the secondary diagonal of a matrix.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, true},
     *     {false, true, false},
     *     {true, false, false}
     * });
     * List<Boolean> antiDiagonal = matrix.streamAntiDiagonal().toList();   // [true, true, true]
     * 
     * // Count true values on anti-diagonal
     * long trueCount = matrix.streamAntiDiagonal().filter(b -> b).count();
     * }</pre>
     *
     * @return a Stream&lt;Boolean&gt; containing the anti-diagonal elements from top-right to bottom-left
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public Stream<Boolean> streamAntiDiagonal() {
        checkIfRowAndColumnSizeAreSame();

        if (isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new ObjIteratorEx<>() {
            private final int toIndex = rowCount;
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

                final Boolean result = a[cursor][columnCount - cursor - 1];
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
     * Returns a stream of all elements in this matrix, traversed horizontally (left to right, top to bottom).
     * Elements are streamed row by row from left to right, starting from the
     * top-left corner and proceeding to the bottom-right corner.
     *
     * <p>This method is useful for processing all matrix elements sequentially
     * without concern for their row/column positions. Unlike the primitive IntMatrix,
     * this returns a Stream&lt;Boolean&gt; with boxed values.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * Stream<Boolean> stream = matrix.streamHorizontal();   // Stream of [true, false, false, true]
     * 
     * // Count true values
     * long trueCount = matrix.streamHorizontal().filter(b -> b).count();   // Returns 2
     * 
     * // Convert to list
     * List<Boolean> list = matrix.streamHorizontal().toList();   // [true, false, false, true]
     * }</pre>
     * 
     * @return a Stream&lt;Boolean&gt; of all elements in row-major order, or an empty stream if the matrix is empty
     */
    @Override
    public Stream<Boolean> streamHorizontal() {
        return streamHorizontal(0, rowCount);
    }

    /**
     * Returns a stream of elements from a single row.
     * The elements are streamed from left to right within the specified row.
     * 
     * <p>This method is particularly useful when you need to process or analyze
     * a specific row of the matrix independently.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, true},
     *     {false, true, false}
     * });
     * Stream<Boolean> firstRow = matrix.streamHorizontal(0);   // Stream of [true, false, true]
     * 
     * // Check if any value in the second row is true
     * boolean hasTrue = matrix.streamHorizontal(1).anyMatch(b -> b);   // Returns true
     * }</pre>
     * 
     * @param rowIndex the index of the row to stream (0-based)
     * @return a Stream&lt;Boolean&gt; of elements from the specified row
     * @throws IndexOutOfBoundsException if rowIndex &lt; 0 or rowIndex &gt;= rowCount
     */
    @Override
    public Stream<Boolean> streamHorizontal(final int rowIndex) {
        return streamHorizontal(rowIndex, rowIndex + 1);
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false},
     *     {false, true},
     *     {true, true}
     * });
     * Stream<Boolean> middleRows = matrix.streamHorizontal(1, 3);   // Stream rows 1 and 2: [false, true, true, true]
     * 
     * // Process subset of rows
     * int[] subset = matrix.streamHorizontal(0, 2)
     *     .mapToInt(b -> b ? 1 : 0)
     *     .toArray();   // [1, 0, 0, 1]
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a Stream&lt;Boolean&gt; of elements from the specified row range, or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    @Override
    public Stream<Boolean> streamHorizontal(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);

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
            public <A> A[] toArray(A[] c) {
                final int len = toArrayLength(count());

                if (c.length < len) {
                    c = N.copyOf(c, len);
                }

                for (int k = 0; k < len; k++) {
                    c[k] = (A) (Boolean) a[i][j++];

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
     * Returns a stream of all elements in column-major order (vertical).
     * Elements are streamed column by column from top to bottom, starting from
     * the leftmost column and proceeding to the rightmost column.
     * 
     * <p>This method is marked as @Beta, indicating it may be subject to change
     * in future versions. It provides an alternative way to iterate through matrix
     * elements compared to the row-major order of streamHorizontal().</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * Stream<Boolean> stream = matrix.streamVertical();   // Stream of [true, false, false, true]
     * 
     * // Process in column order
     * List<Boolean> colMajor = matrix.streamVertical().toList();   // [true, false, false, true]
     * }</pre>
     * 
     * @return a Stream&lt;Boolean&gt; of all elements in column-major order, or an empty stream if the matrix is empty
     */
    @Override
    @Beta
    public Stream<Boolean> streamVertical() {
        return streamVertical(0, columnCount);
    }

    /**
     * Returns a stream of elements from a single column.
     * The elements are streamed from top to bottom within the specified column.
     * 
     * <p>This method is useful for column-wise operations such as checking
     * column properties or extracting column data.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, true},
     *     {true, true, false}
     * });
     * Stream<Boolean> firstCol = matrix.streamVertical(0);   // Stream of [true, true]
     * 
     * // Check if all values in a column are true
     * boolean allTrue = matrix.streamVertical(0).allMatch(b -> b);   // Returns true
     * }</pre>
     * 
     * @param columnIndex the index of the column to stream (0-based)
     * @return a Stream&lt;Boolean&gt; of elements from the specified column
     * @throws IndexOutOfBoundsException if columnIndex &lt; 0 or columnIndex &gt;= columnCount
     */
    @Override
    public Stream<Boolean> streamVertical(final int columnIndex) {
        return streamVertical(columnIndex, columnIndex + 1);
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, true},
     *     {false, true, false}
     * });
     * Stream<Boolean> lastTwoCols = matrix.streamVertical(1, 3);   // Stream columns 1 and 2: [false, true, true, false]
     * 
     * // Count true values in column subset
     * long trueCount = matrix.streamVertical(0, 2).filter(b -> b).count();
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a Stream&lt;Boolean&gt; of elements from the specified column range in column-major order,
     *         or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; columnCount,
     *         or fromColumnIndex &gt; toColumnIndex
     */
    @Override
    @Beta
    public Stream<Boolean> streamVertical(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

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

                if (n >= (long) (toColumnIndex - j) * BooleanMatrix.this.rowCount - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    final long offset = n + i;
                    i = (int) (offset % BooleanMatrix.this.rowCount);
                    j += (int) (offset / BooleanMatrix.this.rowCount);
                }
            }

            @Override
            public long count() {
                return (long) (toColumnIndex - j) * rowCount - i; // NOSONAR
            }

            @Override
            public <A> A[] toArray(A[] c) {
                final int len = toArrayLength(count());

                if (c.length < len) {
                    c = N.copyOf(c, len);
                }

                for (int k = 0; k < len; k++) {
                    c[k] = (A) (Boolean) a[i++][j];

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
     * Returns a stream of Stream&lt;Boolean&gt; objects, where each inner stream represents a complete row.
     * This creates a stream of streams, allowing for row-by-row processing of the matrix.
     * 
     * <p>This method is useful for operations that need to process entire rows as units,
     * such as row-wise transformations, filtering rows based on conditions, or mapping
     * rows to other values.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, true},
     *     {false, false, false},
     *     {true, true, true}
     * });
     * 
     * // Count rows that contain at least one true value
     * long rowsWithTrue = matrix.streamRows()
     *     .filter(row -> row.anyMatch(b -> b))
     *     .count();   // Returns 2
     * 
     * // Get row sums (count of true values per row)
     * int[] rowTrueCounts = matrix.streamRows()
     *     .mapToInt(row -> (int) row.filter(b -> b).count())
     *     .toArray();   // [2, 0, 3]
     * }</pre>
     * 
     * @return a Stream of Stream&lt;Boolean&gt; objects, one for each row in the matrix
     */
    @Override
    public Stream<Stream<Boolean>> streamRows() {
        return streamRows(0, rowCount);
    }

    /**
     * Returns a stream of Stream&lt;Boolean&gt; objects for a range of rows.
     * Each inner stream in the result represents a complete row within the specified range.
     * 
     * <p>This method allows for processing a subset of rows while maintaining the
     * ability to work with complete rows as individual streams.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, true, false},
     *     {false, true, true},
     *     {true, false, true}
     * });
     * 
     * // Process middle rows only
     * List<Boolean> hasPattern = matrix.streamRows(1, 3)
     *     .map(row -> {
     *         List<Boolean> list = row.toList();
     *         return list.get(0) != list.get(2);   // Check if first != last
     *     })
     *     .toList();   // [true, false]
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a Stream of Stream&lt;Boolean&gt; objects for the specified row range
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount,
     *         or fromRowIndex &gt; toRowIndex
     */
    @Override
    public Stream<Stream<Boolean>> streamRows(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);

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
     * Returns a stream of Stream&lt;Boolean&gt; objects, where each inner stream represents a complete column.
     * This creates a stream of streams, allowing for column-by-column processing of the matrix.
     * 
     * <p>This method is marked as @Beta and is useful for operations that need to process
     * entire columns as units, such as column-wise statistics, transformations, or filtering
     * columns based on conditions.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, true},
     *     {true, true, false}
     * });
     * 
     * // Check which columns have all true values
     * List<Boolean> allTrueColumns = matrix.streamColumns()
     *     .map(col -> col.allMatch(b -> b))
     *     .toList();   // [true, false, false]
     * 
     * // Count true values per column
     * long[] colTrueCounts = matrix.streamColumns()
     *     .mapToLong(col -> col.filter(b -> b).count())
     *     .toArray();   // [2, 1, 1]
     * }</pre>
     * 
     * @return a Stream of Stream&lt;Boolean&gt; objects, one for each column in the matrix,
     *         or an empty stream if the matrix is empty
     */
    @Override
    @Beta
    public Stream<Stream<Boolean>> streamColumns() {
        return streamColumns(0, columnCount);
    }

    /**
     * Returns a stream of Stream&lt;Boolean&gt; objects for a range of columns.
     * Each inner stream in the result represents a complete column within the specified range.
     * 
     * <p>This method is marked as @Beta and allows for processing a subset of columns
     * while maintaining the ability to work with complete columns as individual streams.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, true, false},
     *     {false, true, false, true}
     * });
     * 
     * // Process last two columns
     * List<String> patterns = matrix.streamColumns(2, 4)
     *     .map(col -> col.map(b -> b ? "1" : "0")
     *                    .collect(java.util.stream.Collectors.joining()))
     *     .toList();   // ["10", "01"]
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a Stream of Stream&lt;Boolean&gt; objects for the specified column range,
     *         or an empty stream if the matrix is empty
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; columnCount,
     *         or fromColumnIndex &gt; toColumnIndex
     */
    @Override
    @Beta
    public Stream<Stream<Boolean>> streamColumns(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public Stream<Boolean> next() {
                if (cursor >= toIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                return Stream.of(new ObjIteratorEx<>() {
                    private final int columnIndex = cursor++;
                    private final int toIndex2 = rowCount;
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
     * Returns the length of the given array.
     * This is a utility method used internally by the abstract parent class
     * to determine the column count of a row.
     *
     * @param a the array (row) to measure, may be null
     * @return the length of the array, or 0 if the array is null
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final boolean[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * Performs the specified action for each element in this matrix.
     * Elements are processed in row-major order (row by row, left to right) when executed sequentially.
     *
     * <p>The operation may be parallelized internally for large matrices to improve performance,
     * based on internal heuristics. If parallelized, the order of execution is not guaranteed,
     * but all elements will be processed exactly once.</p>
     *
     * <p><b>Note:</b> This method is for side-effect operations only (like printing, collecting,
     * or accumulating). For transformations that create new matrices, use {@link #map(Throwables.BooleanUnaryOperator)}
     * or {@link #updateAll(Throwables.BooleanUnaryOperator)}.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
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
     * @param action the action to be performed for each element; receives each element value
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception
     * @see #forEach(int, int, int, int, Throwables.BooleanConsumer)
     */
    public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
        forEach(0, rowCount, 0, columnCount, action);
    }

    /**
     * Performs the specified action for each element in the specified sub-matrix region.
     * Elements are processed in row-major order within the specified bounds.
     *
     * <p>This method allows for processing a rectangular subset of the matrix.
     * The operation may be parallelized internally if the sub-matrix is large enough
     * to benefit from parallel processing.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {
     *     {true, false, true},
     *     {false, true, false},
     *     {true, true, true}
     * });
     * 
     * // Process only the top-left 2x2 sub-matrix
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
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws IndexOutOfBoundsException if any index is out of bounds or fromIndex &gt; toIndex
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.BooleanConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);
        N.checkArgNotNull(action, "action");

        if (Matrices.isParallelizable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> elementAction = (i, j) -> action.accept(a[i][j]);
            Matrices.forEachIndices(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, elementAction, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final boolean[] currentRow = a[i];

                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(currentRow[j]);
                }
            }
        }
    }

    /**
     * Prints this matrix and returns the printed text.
     *
     * <p>Each row is formatted as {@code [e1, e2, ...]} and rows are separated by
     * {@link #ARRAY_PRINT_SEPARATOR}. If the matrix is empty, {@code []} is printed.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * matrix.println();
     * // Output:
     * // [true, false]
     * // [false, true]
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

                    final boolean[] row = a[i];
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
     * BooleanMatrix matrix1 = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * BooleanMatrix matrix2 = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
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
     * Returns {@code true} if the given object is also a BooleanMatrix with the same dimensions
     * and all corresponding elements are equal.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix m1 = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * BooleanMatrix m2 = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
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

        if (obj instanceof final BooleanMatrix another) {
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
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false}, {false, true}});
     * System.out.println(matrix.toString());   // [[true, false], [false, true]]
     * }</pre>
     *
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
