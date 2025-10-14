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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.annotation.SuppressFBWarnings;
import com.landawn.abacus.util.Arrays.ff;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.Nullable;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

/**
 * A generic matrix implementation that stores elements in a two-dimensional array.
 * This class provides comprehensive matrix operations including element access,
 * transformations, and various utility methods for matrix manipulation.
 * 
 * <p>The matrix is immutable in terms of its dimensions once created, but individual
 * elements can be modified. Most transformation operations return new matrix instances.</p>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Generic type support for any object type</li>
 *   <li>Element-wise operations (map, update, replace)</li>
 *   <li>Matrix transformations (transpose, rotate, flip)</li>
 *   <li>Matrix combination operations (vstack, hstack, zip)</li>
 *   <li>Stream-based access patterns</li>
 *   <li>Conversion to primitive type matrices</li>
 * </ul>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * // Create a 3x3 matrix of integers
 * Integer[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
 * Matrix<Integer> matrix = Matrix.of(data);
 * 
 * // Access elements
 * Integer value = matrix.get(1, 2); // Gets 6
 * 
 * // Transform the matrix
 * Matrix<Double> doubled = matrix.map(x -> x * 2.0, Double.class);
 * 
 * // Combine matrices
 * Matrix<Integer> sum = matrix.zipWith(otherMatrix, (a, b) -> a + b);
 * }</pre>
 *
 * @param <T> the type of elements in this matrix
 * @see AbstractMatrix
 * @see IntMatrix
 * @see DoubleMatrix
 * @see LongMatrix
 * @see FloatMatrix
 * @see ByteMatrix
 * @see ShortMatrix
 * @see CharMatrix
 * @see BooleanMatrix
 */
public final class Matrix<T> extends AbstractMatrix<T[], List<T>, Stream<T>, Stream<Stream<T>>, Matrix<T>> {

    final Class<T[]> arrayType;

    final Class<T> elementType;

    /**
     * Constructs a Matrix from a two-dimensional array.
     *
     * <p><b>Important:</b> The matrix maintains a reference to the provided array,
     * not a copy. Modifications to the original array will affect the matrix,
     * and vice versa.</p>
     *
     * <p>The array must be rectangular (all rows must have the same length).
     * Empty arrays are allowed (e.g., {@code new String[0][0]} or {@code new String[5][0]}).</p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * String[][] data = {{"A", "B"}, {"C", "D"}};
     * Matrix<String> matrix = new Matrix<>(data);
     * data[0][0] = "X"; // This also changes the matrix
     * }</pre>
     *
     * @param a the two-dimensional array of elements (must not be null)
     * @throws IllegalArgumentException if the array is null or if rows have different lengths (not rectangular)
     */
    public Matrix(final T[][] a) {
        super(a);
        arrayType = (Class<T[]>) this.a.getClass().getComponentType();
        elementType = (Class<T>) arrayType.getComponentType();
    }

    /**
     * Creates a Matrix from a two-dimensional array.
     * This is a convenience factory method equivalent to calling the constructor.
     * The method uses varargs to allow for easy matrix creation.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * String[][] data = {{"a", "b"}, {"c", "d"}};
     * Matrix<String> matrix = Matrix.of(data);
     * 
     * // Using varargs
     * Matrix<Integer> numbers = Matrix.of(
     *     new Integer[]{1, 2, 3},
     *     new Integer[]{4, 5, 6},
     *     new Integer[]{7, 8, 9}
     * );
     * }</pre>
     *
     * @param <T> the type of elements in the matrix
     * @param a the two-dimensional array of elements (varargs of arrays)
     * @return a new Matrix containing the given elements
     * @throws IllegalArgumentException if the array is null, empty, or not rectangular
     */
    @SafeVarargs
    public static <T> Matrix<T> of(final T[]... a) {
        return new Matrix<>(a);
    }

    /**
     * Creates a matrix with a single row containing the specified element repeated.
     * This method requires the element to be non-null to infer the element type.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<String> matrix = Matrix.repeat("X", 5);
     * // Creates: [["X", "X", "X", "X", "X"]]
     * }</pre>
     *
     * @param <T> the type of elements
     * @param element the element to repeat (must not be null)
     * @param len the number of columns
     * @return a 1×len matrix filled with the element
     * @throws IllegalArgumentException if element is null
     * @see #repeat(Object, int, Class)
     * @see #repeatNonNull(Object, int)
     * @deprecated Use {@link #repeat(Object, int, Class)} for better type safety
     */
    @Deprecated
    public static <T> Matrix<T> repeat(final T element, final int len) throws IllegalArgumentException {
        N.checkArgNotNull(element, "element");

        return repeat(element, len, (Class<T>) element.getClass());
    }

    /**
     * Creates a matrix with a single row containing the specified element repeated.
     * This method requires explicit specification of the element class for proper array creation.
     * The element can be null when using this method.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Double> matrix = Matrix.repeat(0.0, 5, Double.class);
     * // Creates: [[0.0, 0.0, 0.0, 0.0, 0.0]]
     * 
     * Matrix<String> nullMatrix = Matrix.repeat(null, 3, String.class);
     * // Creates: [[null, null, null]]
     * }</pre>
     *
     * @param <T> the type of elements
     * @param element the element to repeat (can be null)
     * @param len the number of columns
     * @param elementClass the class of the element type
     * @return a 1×len matrix filled with the element
     * @throws IllegalArgumentException if elementClass is null or len is negative
     */
    public static <T> Matrix<T> repeat(final T element, final int len, final Class<T> elementClass) throws IllegalArgumentException {
        N.checkArgNotNull(elementClass, "elementClass");

        final T[][] c = N.newArray(N.newArray(elementClass, 0).getClass(), 1);
        c[0] = N.newArray(elementClass, len);
        N.fill(c[0], element);
        return new Matrix<>(c);
    }

    /**
     * Creates a matrix with a single row containing the specified non-null element repeated.
     * The element class is inferred from the element itself.
     * This method is a convenience wrapper that ensures type safety for non-null elements.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<String> matrix = Matrix.repeatNonNull("X", 3);
     * // Creates: [["X", "X", "X"]]
     * 
     * Matrix<Integer> numbers = Matrix.repeatNonNull(42, 4);
     * // Creates: [[42, 42, 42, 42]]
     * }</pre>
     *
     * @param <T> the type of elements
     * @param element the element to repeat (must not be null)
     * @param len the number of columns
     * @return a 1×len matrix filled with the element
     * @throws IllegalArgumentException if the specified element is null or len is negative
     */
    public static <T> Matrix<T> repeatNonNull(final T element, final int len) throws IllegalArgumentException {
        N.checkArgNotNull(element, "element");

        return repeat(element, len, (Class<T>) element.getClass());
    }

    /**
     * Creates a square diagonal matrix with the given values on the main diagonal (left-up to right-down).
     * All other elements are null. The matrix dimension is determined by the length of the diagonal array.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> diag = Matrix.diagonalLU2RD(new Integer[]{1, 2, 3});
     * // Creates: [[1, null, null],
     * //           [null, 2, null],
     * //           [null, null, 3]]
     * 
     * Matrix<String> strDiag = Matrix.diagonalLU2RD(new String[]{"A", "B"});
     * // Creates: [["A", null],
     * //           [null, "B"]]
     * }</pre>
     *
     * @param <T> the type of elements
     * @param leftUp2RightDownDiagonal the diagonal values
     * @return a square matrix with the given diagonal values
     * @throws IllegalArgumentException if the diagonal array is null
     */
    public static <T> Matrix<T> diagonalLU2RD(final T[] leftUp2RightDownDiagonal) {
        return diagonal(leftUp2RightDownDiagonal, null);
    }

    /**
     * Creates a square diagonal matrix with the given values on the anti-diagonal (right-up to left-down).
     * All other elements are null. The matrix dimension is determined by the length of the diagonal array.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> diag = Matrix.diagonalRU2LD(new Integer[]{1, 2, 3});
     * // Creates: [[null, null, 1],
     * //           [null, 2, null],
     * //           [3, null, null]]
     * 
     * Matrix<String> strDiag = Matrix.diagonalRU2LD(new String[]{"X", "Y"});
     * // Creates: [[null, "X"],
     * //           ["Y", null]]
     * }</pre>
     *
     * @param <T> the type of elements
     * @param rightUp2LeftDownDiagonal the anti-diagonal values
     * @return a square matrix with the given anti-diagonal values
     * @throws IllegalArgumentException if the diagonal array is null
     */
    public static <T> Matrix<T> diagonalRU2LD(final T[] rightUp2LeftDownDiagonal) {
        return diagonal(null, rightUp2LeftDownDiagonal);
    }

    /**
     * Creates a square matrix with values on both diagonals.
     * The main diagonal runs from left-up to right-down, and the anti-diagonal
     * runs from right-up to left-down. If diagonals intersect (odd dimension),
     * the main diagonal value takes precedence. At least one diagonal must be non-null.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<String> diag = Matrix.diagonal(
     *     new String[]{"A", "B", "C"},
     *     new String[]{"X", "Y", "Z"}
     * );
     * // Creates: [["A", null, "X"],
     * //           [null, "B", null],
     * //           ["Z", null, "C"]]
     * 
     * // With intersection (odd dimension)
     * Matrix<Integer> numbers = Matrix.diagonal(
     *     new Integer[]{1, 2, 3},
     *     new Integer[]{7, 8, 9}
     * );
     * // Creates: [[1, null, 7],
     * //           [null, 2, null],  // 2 takes precedence over 8
     * //           [9, null, 3]]
     * }</pre>
     *
     * @param <T> the type of elements
     * @param leftUp2RightDownDiagonal the main diagonal values.
     * @param rightUp2LeftDownDiagonal the anti-diagonal values.
     * @return a square matrix with the given diagonal values
     * @throws IllegalArgumentException if both arrays are null, or if both are non-null and have different lengths
     */
    @SuppressWarnings("null")
    public static <T> Matrix<T> diagonal(final T[] leftUp2RightDownDiagonal, final T[] rightUp2LeftDownDiagonal) throws IllegalArgumentException {
        N.checkArgument(leftUp2RightDownDiagonal != null || rightUp2LeftDownDiagonal != null,
                "Both 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' can't be null");

        N.checkArgument(
                N.isEmpty(leftUp2RightDownDiagonal) || N.isEmpty(rightUp2LeftDownDiagonal)
                        || leftUp2RightDownDiagonal.length == rightUp2LeftDownDiagonal.length,
                "The length of 'leftUp2RightDownDiagonal' and 'rightUp2LeftDownDiagonal' must be same");

        final int len = N.max(N.len(leftUp2RightDownDiagonal), N.len(rightUp2LeftDownDiagonal));
        final Class<?> arrayClass = leftUp2RightDownDiagonal != null ? leftUp2RightDownDiagonal.getClass() : rightUp2LeftDownDiagonal.getClass();
        final Class<?> componentClass = arrayClass.getComponentType();

        final T[][] c = N.newArray(arrayClass, len);

        for (int i = 0; i < len; i++) {
            c[i] = N.newArray(componentClass, len);
        }

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

        return new Matrix<>(c);
    }

    /**
     * Returns the component type of the elements in this matrix.
     * For example, for a {@code Matrix<Integer>}, this returns {@code Integer.class}.
     * This is useful for reflection-based operations or when creating new arrays.
     *
     * @return the Class object representing the element type
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class componentType() {
        return elementType;
    }

    /**
     * Gets the element at the specified row and column.
     * Row and column indices are 0-based.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<String> matrix = Matrix.of(new String[][]{{"A", "B"}, {"C", "D"}});
     * String value = matrix.get(1, 0); // Returns "C"
     * String corner = matrix.get(1, 1); // Returns "D"
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position (i, j)
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public T get(final int i, final int j) {
        return a[i][j];
    }

    /**
     * Gets the element at the specified point.
     * This is a convenience method that takes a Point object instead of separate indices.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Point p = Point.of(1, 2);
     * T value = matrix.get(p); // Same as matrix.get(1, 2)
     * }</pre>
     *
     * @param point the point containing row and column indices
     * @return the element at the specified point
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     * @throws IllegalArgumentException if point is null
     */
    public T get(final Point point) {
        return a[point.rowIndex()][point.columnIndex()];
    }

    /**
     * Sets the element at the specified row and column.
     * Row and column indices are 0-based.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.set(1, 2, "newValue");
     * // Element at row 1, column 2 is now "newValue"
     * }</pre>
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @param val the new value to set
     * @throws ArrayIndexOutOfBoundsException if indices are out of bounds
     */
    public void set(final int i, final int j, final T val) {
        a[i][j] = val;
    }

    /**
     * Sets the element at the specified point.
     * This is a convenience method that takes a Point object instead of separate indices.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Point p = Point.of(0, 1);
     * matrix.set(p, newValue); // Same as matrix.set(0, 1, newValue)
     * }</pre>
     *
     * @param point the point containing row and column indices
     * @param val the new value to set
     * @throws ArrayIndexOutOfBoundsException if the point is out of bounds
     * @throws IllegalArgumentException if point is null
     */
    public void set(final Point point, final T val) {
        a[point.rowIndex()][point.columnIndex()] = val;
    }

    /**
     * Gets the element above the specified position.
     * Returns empty if the position is in the first row.
     * This method is useful for algorithms that need to check adjacent elements.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Nullable<T> above = matrix.upOf(1, 2);
     * if (above.isPresent()) {
     *     T value = above.get(); // Element at (0, 2)
     * }
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return a Nullable containing the element above, or empty if none exists
     * @throws ArrayIndexOutOfBoundsException if the original position is out of bounds
     */
    public Nullable<T> upOf(final int i, final int j) {
        return i == 0 ? Nullable.empty() : Nullable.of(a[i - 1][j]);
    }

    /**
     * Gets the element below the specified position.
     * Returns empty if the position is in the last row.
     * This method is useful for algorithms that need to check adjacent elements.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Nullable<T> below = matrix.downOf(0, 1);
     * if (below.isPresent()) {
     *     T value = below.get(); // Element at (1, 1)
     * }
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return a Nullable containing the element below, or empty if none exists
     * @throws ArrayIndexOutOfBoundsException if the original position is out of bounds
     */
    public Nullable<T> downOf(final int i, final int j) {
        return i == rows - 1 ? Nullable.empty() : Nullable.of(a[i + 1][j]);
    }

    /**
     * Gets the element to the left of the specified position.
     * Returns empty if the position is in the first column.
     * This method is useful for algorithms that need to check adjacent elements.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Nullable<T> left = matrix.leftOf(1, 2);
     * if (left.isPresent()) {
     *     T value = left.get(); // Element at (1, 1)
     * }
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return a Nullable containing the element to the left, or empty if none exists
     * @throws ArrayIndexOutOfBoundsException if the original position is out of bounds
     */
    public Nullable<T> leftOf(final int i, final int j) {
        return j == 0 ? Nullable.empty() : Nullable.of(a[i][j - 1]);
    }

    /**
     * Gets the element to the right of the specified position.
     * Returns empty if the position is in the last column.
     * This method is useful for algorithms that need to check adjacent elements.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Nullable<T> right = matrix.rightOf(1, 0);
     * if (right.isPresent()) {
     *     T value = right.get(); // Element at (1, 1)
     * }
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return a Nullable containing the element to the right, or empty if none exists
     * @throws ArrayIndexOutOfBoundsException if the original position is out of bounds
     */
    public Nullable<T> rightOf(final int i, final int j) {
        return j == cols - 1 ? Nullable.empty() : Nullable.of(a[i][j + 1]);
    }

    /**
     * Returns the four adjacent points (up, right, down, left) of the specified position.
     * Points that would be outside the matrix bounds are returned as null.
     * The order is: up, right, down, left (clockwise starting from top).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Stream<Point> adjacent = matrix.adjacent4Points(1, 1);
     * adjacent.filter(Objects::nonNull).forEach(point -> {
     *     // Process each valid adjacent point
     * });
     * 
     * // For a position not on the edge, returns 4 points
     * // For a corner position, returns 2 non-null points
     * // For an edge position, returns 3 non-null points
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return a stream of Points representing the adjacencies, with null for non-existent positions
     * @throws ArrayIndexOutOfBoundsException if the original position is out of bounds
     */
    public Stream<Point> adjacent4Points(final int i, final int j) {
        final Point up = i == 0 ? null : Point.of(i - 1, j);
        final Point right = j == cols - 1 ? null : Point.of(i, j + 1);
        final Point down = i == rows - 1 ? null : Point.of(i + 1, j);
        final Point left = j == 0 ? null : Point.of(i, j - 1);

        return Stream.of(up, right, down, left);
    }

    /**
     * Returns the eight adjacent points of the specified position in clockwise order.
     * The order is: left-up, up, right-up, right, right-down, down, left-down, left.
     * Points that would be outside the matrix bounds are returned as null.
     * This includes both orthogonal and diagonal neighbors.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Stream<Point> adjacent = matrix.adjacent8Points(2, 2);
     * List<Point> validPoints = adjacent
     *     .filter(Objects::nonNull)
     *     .toList();
     * // For a central position, returns 8 points
     * // For a corner position, returns 3 non-null points
     * // For an edge position, returns 5 non-null points
     * }</pre>
     *
     * @param i the row index
     * @param j the column index
     * @return a stream of Points representing the adjacencies, with null for non-existent positions
     * @throws ArrayIndexOutOfBoundsException if the original position is out of bounds
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
     * Returns a reference to the internal array containing the specified row.
     *
     * <p><b>Warning:</b> This method returns a direct reference to the internal array, not a copy.
     * Any modifications to the returned array will directly affect the matrix.</p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<String> matrix = Matrix.of(new String[][]{{"A", "B"}, {"C", "D"}});
     * String[] rowData = matrix.row(0);
     * rowData[0] = "X"; // This modifies the matrix directly
     * // Matrix is now: [["X", "B"], ["C", "D"]]
     *
     * // Use clone() if you need an independent copy
     * String[] rowCopy = matrix.row(1).clone();
     * rowCopy[0] = "Y"; // Does not affect the matrix
     * }</pre>
     *
     * @param rowIndex the row index to retrieve (0-based)
     * @return the internal array containing the row elements (not a copy - modifications will affect the matrix)
     * @throws IllegalArgumentException if rowIndex is negative or greater than or equal to the number of rows
     */
    public T[] row(final int rowIndex) throws IllegalArgumentException {
        N.checkArgument(rowIndex >= 0 && rowIndex < rows, "Invalid row Index: %s", rowIndex);

        return a[rowIndex];
    }

    /**
     * Returns a copy of the specified column as an array.
     * Unlike {@link #row(int)}, this method returns a new array, not a reference to internal data.
     * Modifications to the returned array will not affect the matrix.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<String> matrix = Matrix.of(new String[][]{{"A", "B"}, {"C", "D"}});
     * String[] colData = matrix.column(1);
     * // colData contains: ["B", "D"]
     * colData[0] = "X"; // Does not affect the matrix
     * // Matrix remains: [["A", "B"], ["C", "D"]]
     * }</pre>
     *
     * @param columnIndex the column index to retrieve (0-based)
     * @return a new array containing a copy of the column elements
     * @throws IllegalArgumentException if columnIndex is negative or greater than or equal to the number of columns
     */
    public T[] column(final int columnIndex) throws IllegalArgumentException {
        N.checkArgument(columnIndex >= 0 && columnIndex < cols, "Invalid column Index: %s", columnIndex);

        final T[] c = N.newArray(elementType, rows);

        for (int i = 0; i < rows; i++) {
            c[i] = a[i][columnIndex];
        }

        return c;
    }

    /**
     * Replaces an entire row with values from the given array.
     * The array must have the same length as the number of columns in this matrix.
     * The values are copied from the provided array, so subsequent modifications to
     * the input array will not affect the matrix.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<String> matrix = Matrix.of(new String[][]{{"A", "B"}, {"C", "D"}});
     * String[] newRow = {"X", "Y"};
     * matrix.setRow(0, newRow); // Replace first row
     * // Matrix is now: [["X", "Y"], ["C", "D"]]
     * }</pre>
     *
     * @param rowIndex the row index to replace (0-based)
     * @param row the new row data (must have exactly {@code cols} elements)
     * @throws IllegalArgumentException if the row array length doesn't match the number of columns,
     *         or if rowIndex is negative or greater than or equal to the number of rows
     */
    public void setRow(final int rowIndex, final T[] row) throws IllegalArgumentException {
        N.checkArgument(row.length == cols, "The size of the specified row doesn't match the length of column");

        N.copy(row, 0, a[rowIndex], 0, cols);
    }

    /**
     * Replaces an entire column with values from the given array.
     * The array must have the same length as the number of rows in this matrix.
     * Each element is copied to the corresponding row in the specified column.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<String> matrix = Matrix.of(new String[][]{{"A", "B"}, {"C", "D"}});
     * String[] newColumn = {"X", "Y"};
     * matrix.setColumn(1, newColumn); // Replace second column
     * // Matrix is now: [["A", "X"], ["C", "Y"]]
     * }</pre>
     *
     * @param columnIndex the column index to replace (0-based)
     * @param column the new column data (must have exactly {@code rows} elements)
     * @throws IllegalArgumentException if the column array length doesn't match the number of rows,
     *         or if columnIndex is negative or greater than or equal to the number of columns
     */
    public void setColumn(final int columnIndex, final T[] column) throws IllegalArgumentException {
        N.checkArgument(column.length == rows, "The size of the specified column doesn't match the length of row");

        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = column[i];
        }
    }

    /**
     * Updates all elements in the specified row by applying the given function.
     * The function is applied to each element in the row, and the result
     * replaces the original value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Double all values in row 0
     * matrix.updateRow(0, x -> x * 2);
     * 
     * // Convert strings to uppercase in row 1
     * stringMatrix.updateRow(1, String::toUpperCase);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param rowIndex the row index to update
     * @param func the function to apply to each element
     * @throws E if the function throws an exception
     * @throws IllegalArgumentException if rowIndex is out of bounds
     */
    public <E extends Exception> void updateRow(final int rowIndex, final Throwables.UnaryOperator<T, E> func) throws E {
        for (int i = 0; i < cols; i++) {
            a[rowIndex][i] = func.apply(a[rowIndex][i]);
        }
    }

    /**
     * Updates all elements in the specified column by applying the given function.
     * The function is applied to each element in the column, and the result
     * replaces the original value.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Append suffix to all strings in column 1
     * matrix.updateColumn(1, s -> s + "_suffix");
     * 
     * // Square all numbers in column 0
     * numberMatrix.updateColumn(0, n -> n * n);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param columnIndex the column index to update
     * @param func the function to apply to each element
     * @throws E if the function throws an exception
     * @throws IllegalArgumentException if columnIndex is out of bounds
     */
    public <E extends Exception> void updateColumn(final int columnIndex, final Throwables.UnaryOperator<T, E> func) throws E {
        for (int i = 0; i < rows; i++) {
            a[i][columnIndex] = func.apply(a[i][columnIndex]);
        }
    }

    /**
     * Gets the main diagonal elements (left-up to right-down).
     * The matrix must be square. Returns a new array containing the diagonal values.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> m = Matrix.of(new Integer[][]{{1,2,3},{4,5,6},{7,8,9}});
     * Integer[] diag = m.getLU2RD(); // Returns [1, 5, 9]
     * }</pre>
     *
     * @return an array containing the diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    public T[] getLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        final T[] res = N.newArray(elementType, rows);

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][i]; // NOSONAR
        }

        return res;
    }

    /**
     * Sets the main diagonal elements (left-up to right-down).
     * The matrix must be square and the diagonal array must have at least as many
     * elements as the matrix dimension. Extra elements in the array are ignored.
     *
     * <p>Example:</p>
     * <pre>{@code
     * matrix.setLU2RD(new Integer[]{10, 20, 30});
     * // Diagonal is now [10, 20, 30]
     * }</pre>
     *
     * @param diagonal the new diagonal values
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
     */
    public void setLU2RD(final T[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][i] = diagonal[i]; // NOSONAR
        }
    }

    /**
     * Updates the main diagonal elements (left-up to right-down) by applying the given function.
     * The matrix must be square. Each diagonal element is replaced by the result of the function.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Double the diagonal values
     * matrix.updateLU2RD(x -> x * 2);
     * 
     * // Set diagonal to zeros
     * matrix.updateLU2RD(x -> 0);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function to apply to each diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateLU2RD(final Throwables.UnaryOperator<T, E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][i] = func.apply(a[i][i]);
        }
    }

    /**
     * Gets the anti-diagonal elements (right-up to left-down).
     * The matrix must be square. Returns a new array containing the anti-diagonal values.
     * The first element is from the top-right corner, the last from the bottom-left corner.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> m = Matrix.of(new Integer[][]{{1,2,3},{4,5,6},{7,8,9}});
     * Integer[] diag = m.getRU2LD(); // Returns [3, 5, 7]
     * }</pre>
     *
     * @return an array containing the anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    public T[] getRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        final T[] res = N.newArray(elementType, rows);

        for (int i = 0; i < rows; i++) {
            res[i] = a[i][cols - i - 1];
        }

        return res;
    }

    /**
     * Sets the anti-diagonal elements (right-up to left-down).
     * The matrix must be square and the diagonal array must have at least as many
     * elements as the matrix dimension. The first element goes to the top-right corner.
     *
     * <p>Example:</p>
     * <pre>{@code
     * matrix.setRU2LD(new Integer[]{10, 20, 30});
     * // Anti-diagonal is now [10, 20, 30] from top-right to bottom-left
     * }</pre>
     *
     * @param diagonal the new anti-diagonal values
     * @throws IllegalStateException if the matrix is not square
     * @throws IllegalArgumentException if diagonal array is too short
     */
    public void setRU2LD(final T[] diagonal) throws IllegalStateException, IllegalArgumentException {
        checkIfRowAndColumnSizeAreSame();
        N.checkArgument(diagonal.length >= rows, "The length of specified array is less than rows=%s", rows);

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = diagonal[i];
        }
    }

    /**
     * Updates the anti-diagonal elements (right-up to left-down) by applying the given function.
     * The matrix must be square. Each anti-diagonal element is replaced by the result of the function.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Negate the anti-diagonal values
     * matrix.updateRU2LD(x -> -x);
     * 
     * // Convert anti-diagonal strings to lowercase
     * stringMatrix.updateRU2LD(String::toLowerCase);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function to apply to each anti-diagonal element
     * @throws E if the function throws an exception
     * @throws IllegalStateException if the matrix is not square
     */
    public <E extends Exception> void updateRU2LD(final Throwables.UnaryOperator<T, E> func) throws E {
        checkIfRowAndColumnSizeAreSame();

        for (int i = 0; i < rows; i++) {
            a[i][cols - i - 1] = func.apply(a[i][cols - i - 1]);
        }
    }

    // TODO should the method name be "replaceAll"? If change the method name to replaceAll, what about updateLU2RD/updateRU2LD?

    /**
     * Updates all elements in the matrix by applying the given function.
     * The operation can be performed in parallel for large matrices.
     * Each element is replaced by the result of applying the function.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Convert all elements to uppercase
     * stringMatrix.updateAll(String::toUpperCase);
     * 
     * // Square all numbers
     * numberMatrix.updateAll(x -> x * x);
     * 
     * // Replace nulls with default value
     * matrix.updateAll(x -> x == null ? defaultValue : x);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function to apply to each element
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.UnaryOperator<T, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(a[i][j]);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Updates all elements in the matrix based on their position.
     * The function receives the row and column indices and returns the new value.
     * This is useful for position-dependent transformations.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Set each element to its row + column index
     * matrix.updateAll((i, j) -> i + j);
     * 
     * // Create a checkerboard pattern
     * matrix.updateAll((i, j) -> (i + j) % 2 == 0 ? "black" : "white");
     * 
     * // Set diagonal to special value
     * matrix.updateAll((i, j) -> i == j ? diagonalValue : otherValue);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function that takes row and column indices and returns the new value
     * @throws E if the function throws an exception
     */
    public <E extends Exception> void updateAll(final Throwables.IntBiFunction<? extends T, E> func) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = func.apply(i, j);
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces all elements that match the predicate with the new value.
     * The predicate is tested against each element's value, not its position.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Replace all null values with empty string
     * matrix.replaceIf(Objects::isNull, "");
     * 
     * // Replace negative numbers with zero
     * matrix.replaceIf(x -> x < 0, 0);
     * 
     * // Replace empty strings with placeholder
     * matrix.replaceIf(String::isEmpty, "N/A");
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param predicate the condition to test each element
     * @param newValue the value to use as replacement
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.Predicate<? super T, E> predicate, final T newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(a[i][j]) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Replaces elements based on their position using a predicate.
     * The predicate receives row and column indices, not the element value.
     * This is useful for position-based replacements.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Replace diagonal elements with zero
     * matrix.replaceIf((i, j) -> i == j, zero);
     * 
     * // Replace upper triangle with null
     * matrix.replaceIf((i, j) -> i < j, null);
     * 
     * // Replace border elements
     * matrix.replaceIf((i, j) -> i == 0 || i == rows-1 || j == 0 || j == cols-1, borderValue);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param predicate the condition based on position
     * @param newValue the value to use as replacement
     * @throws E if the predicate throws an exception
     */
    public <E extends Exception> void replaceIf(final Throwables.IntBiPredicate<E> predicate, final T newValue) throws E {
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> a[i][j] = predicate.test(i, j) ? newValue : a[i][j];
        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));
    }

    /**
     * Creates a new matrix by applying a transformation function to each element.
     * The result matrix has the same element type as the original.
     * This is a convenience method that uses the same element type for input and output.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<String> upper = stringMatrix.map(String::toUpperCase);
     * Matrix<Integer> doubled = intMatrix.map(x -> x * 2);
     * Matrix<String> trimmed = stringMatrix.map(String::trim);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the transformation function
     * @return a new matrix with transformed elements
     * @throws E if the function throws an exception
     */
    public <E extends Exception> Matrix<T> map(final Throwables.UnaryOperator<T, E> func) throws E {
        return map(func, elementType);
    }

    /**
     * Creates a new matrix by applying a transformation function to each element.
     * The result matrix can have a different element type than the original.
     * The target element type must be explicitly specified.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Convert Integer matrix to String matrix
     * Matrix<String> strings = intMatrix.map(Object::toString, String.class);
     * 
     * // Convert String matrix to Double matrix
     * Matrix<Double> doubles = stringMatrix.map(Double::parseDouble, Double.class);
     * 
     * // Complex transformation
     * Matrix<Boolean> booleans = matrix.map(x -> x != null && x > 0, Boolean.class);
     * }</pre>
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that might be thrown
     * @param func the transformation function
     * @param targetElementType the class of the result element type
     * @return a new matrix with transformed elements
     * @throws E if the function throws an exception
     */
    public <R, E extends Exception> Matrix<R> map(final Throwables.Function<? super T, R, E> func, final Class<R> targetElementType) throws E {
        final R[][] result = Matrixes.newArray(rows, cols, targetElementType);
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.apply(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Creates a boolean matrix by applying a boolean-valued function to each element.
     * This is useful for creating masks or performing element-wise comparisons.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Check for null values
     * BooleanMatrix nullMask = matrix.mapToBoolean(Objects::isNull);
     * 
     * // Check if numbers are positive
     * BooleanMatrix positive = numberMatrix.mapToBoolean(x -> x > 0);
     * 
     * // Check string length
     * BooleanMatrix longStrings = stringMatrix.mapToBoolean(s -> s.length() > 10);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function that returns a boolean for each element
     * @return a new BooleanMatrix
     * @throws E if the function throws an exception
     */
    public <E extends Exception> BooleanMatrix mapToBoolean(final Throwables.ToBooleanFunction<? super T, E> func) throws E {
        final boolean[][] result = new boolean[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsBoolean(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return BooleanMatrix.of(result);
    }

    /**
     * Creates a byte matrix by applying a byte-valued function to each element.
     * Values outside the byte range (-128 to 127) will be truncated.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Convert to byte values
     * ByteMatrix bytes = matrix.mapToByte(x -> x.byteValue());
     * 
     * // Extract first character as byte
     * ByteMatrix firstChars = stringMatrix.mapToByte(s -> (byte)s.charAt(0));
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function that returns a byte for each element
     * @return a new ByteMatrix
     * @throws E if the function throws an exception
     */
    public <E extends Exception> ByteMatrix mapToByte(final Throwables.ToByteFunction<? super T, E> func) throws E {
        final byte[][] result = new byte[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsByte(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ByteMatrix.of(result);
    }

    /**
     * Creates a char matrix by applying a char-valued function to each element.
     * This is useful for character-based transformations.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Get first character of each string
     * CharMatrix firstChars = stringMatrix.mapToChar(s -> s.charAt(0));
     * 
     * // Convert grade numbers to letters
     * CharMatrix grades = scoreMatrix.mapToChar(score -> 
     *     score >= 90 ? 'A' : score >= 80 ? 'B' : 'C'
     * );
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function that returns a char for each element
     * @return a new CharMatrix
     * @throws E if the function throws an exception
     */
    public <E extends Exception> CharMatrix mapToChar(final Throwables.ToCharFunction<? super T, E> func) throws E {
        final char[][] result = new char[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsChar(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return CharMatrix.of(result);
    }

    /**
     * Creates a short matrix by applying a short-valued function to each element.
     * Values outside the short range (-32,768 to 32,767) will be truncated.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Convert to short values
     * ShortMatrix shorts = matrix.mapToShort(x -> x.shortValue());
     * 
     * // Calculate hash codes as shorts
     * ShortMatrix hashes = matrix.mapToShort(x -> (short)x.hashCode());
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function that returns a short for each element
     * @return a new ShortMatrix
     * @throws E if the function throws an exception
     */
    public <E extends Exception> ShortMatrix mapToShort(final Throwables.ToShortFunction<? super T, E> func) throws E {
        final short[][] result = new short[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsShort(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return ShortMatrix.of(result);
    }

    /**
     * Creates an int matrix by applying an int-valued function to each element.
     * This is one of the most commonly used primitive type conversions.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Get string lengths
     * IntMatrix lengths = stringMatrix.mapToInt(String::length);
     * 
     * // Convert to int values
     * IntMatrix ints = matrix.mapToInt(x -> x.intValue());
     * 
     * // Calculate hash codes
     * IntMatrix hashes = matrix.mapToInt(Object::hashCode);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function that returns an int for each element
     * @return a new IntMatrix
     * @throws E if the function throws an exception
     */
    public <E extends Exception> IntMatrix mapToInt(final Throwables.ToIntFunction<? super T, E> func) throws E {
        final int[][] result = new int[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsInt(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return IntMatrix.of(result);
    }

    /**
     * Creates a long matrix by applying a long-valued function to each element.
     * Useful for operations that require 64-bit integer precision.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Convert to long values
     * LongMatrix longs = matrix.mapToLong(x -> x.longValue());
     * 
     * // Get timestamps
     * LongMatrix timestamps = dateMatrix.mapToLong(Date::getTime);
     * 
     * // Calculate large values
     * LongMatrix big = matrix.mapToLong(x -> x * 1_000_000_000L);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function that returns a long for each element
     * @return a new LongMatrix
     * @throws E if the function throws an exception
     */
    public <E extends Exception> LongMatrix mapToLong(final Throwables.ToLongFunction<? super T, E> func) throws E {
        final long[][] result = new long[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsLong(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return LongMatrix.of(result);
    }

    /**
     * Creates a float matrix by applying a float-valued function to each element.
     * Useful for single-precision floating-point operations.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Convert to float values
     * FloatMatrix floats = matrix.mapToFloat(x -> x.floatValue());
     * 
     * // Calculate percentages
     * FloatMatrix percents = matrix.mapToFloat(x -> x / 100.0f);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function that returns a float for each element
     * @return a new FloatMatrix
     * @throws E if the function throws an exception
     */
    public <E extends Exception> FloatMatrix mapToFloat(final Throwables.ToFloatFunction<? super T, E> func) throws E {
        final float[][] result = new float[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsFloat(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return FloatMatrix.of(result);
    }

    /**
     * Creates a double matrix by applying a double-valued function to each element.
     * Useful for double-precision floating-point operations.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Convert to double values
     * DoubleMatrix doubles = matrix.mapToDouble(x -> x.doubleValue());
     * 
     * // Parse strings to doubles
     * DoubleMatrix values = stringMatrix.mapToDouble(Double::parseDouble);
     * 
     * // Calculate complex values
     * DoubleMatrix results = matrix.mapToDouble(x -> Math.sqrt(x));
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param func the function that returns a double for each element
     * @return a new DoubleMatrix
     * @throws E if the function throws an exception
     */
    public <E extends Exception> DoubleMatrix mapToDouble(final Throwables.ToDoubleFunction<? super T, E> func) throws E {
        final double[][] result = new double[rows][cols];
        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = func.applyAsDouble(a[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return DoubleMatrix.of(result);
    }

    /**
     * Fills all elements in the matrix with the specified value.
     * This replaces every element with the same value.
     *
     * <p>Example:</p>
     * <pre>{@code
     * matrix.fill(null); // Clear all values
     * matrix.fill(defaultValue); // Reset to default
     * matrix.fill(""); // Fill with empty strings
     * }</pre>
     *
     * @param val the value to fill the matrix with
     */
    public void fill(final T val) {
        for (int i = 0; i < rows; i++) {
            N.fill(a[i], val);
        }
    }

    /**
     * Fills the matrix with values from another matrix.
     * Copies as much data as will fit, starting from the top-left corner.
     * If the source matrix is larger, extra data is ignored.
     * If the source matrix is smaller, the remaining cells are unchanged.
     *
     * <p>Example:</p>
     * <pre>{@code
     * T[][] data = {{"A", "B"}, {"C", "D"}};
     * matrix.fill(data); // Copy from top-left
     * }</pre>
     *
     * @param b the source matrix data
     */
    public void fill(final T[][] b) {
        fill(0, 0, b);
    }

    /**
     * Fills the matrix with values from another matrix starting at the specified position.
     * Copies as much data as will fit from the starting position.
     * If the source data extends beyond the matrix bounds, it is truncated.
     *
     * <p>Example:</p>
     * <pre>{@code
     * T[][] patch = {{"X", "Y"}, {"Z", "W"}};
     * matrix.fill(1, 2, patch); // Start filling at row 1, column 2
     * }</pre>
     *
     * @param fromRowIndex the starting row index
     * @param fromColumnIndex the starting column index
     * @param b the source array to copy values from
     * @throws IllegalArgumentException if the starting indices are negative or exceed matrix dimensions
     */
    public void fill(final int fromRowIndex, final int fromColumnIndex, final T[][] b) throws IllegalArgumentException {
        N.checkArgument(fromRowIndex >= 0 && fromRowIndex <= rows, "fromRowIndex(%s) must be between 0 and rows(%s)", fromRowIndex, rows);
        N.checkArgument(fromColumnIndex >= 0 && fromColumnIndex <= cols, "fromColumnIndex(%s) must be between 0 and cols(%s)", fromColumnIndex, cols);

        for (int i = 0, minLen = N.min(rows - fromRowIndex, b.length); i < minLen; i++) {
            N.copy(b[i], 0, a[i + fromRowIndex], fromColumnIndex, N.min(b[i].length, cols - fromColumnIndex));
        }
    }

    /**
     * Creates a copy of this matrix.
     * The returned matrix is independent of the original; modifications to one
     * do not affect the other. Each row array is cloned.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<T> copy = matrix.copy();
     * copy.set(0, 0, newValue); // Does not affect original matrix
     * }</pre>
     *
     * @return a new matrix with the same dimensions and copied elements
     */
    @Override
    public Matrix<T> copy() {
        final T[][] c = N.newArray(arrayType, rows);

        for (int i = 0; i < rows; i++) {
            c[i] = a[i].clone();
        }

        return new Matrix<>(c);
    }

    /**
     * Creates a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows.
     * Row indices are inclusive of fromRowIndex and exclusive of toRowIndex.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<T> topRows = matrix.copy(0, 3); // Copy first 3 rows
     * Matrix<T> middleRows = matrix.copy(2, 5); // Copy rows 2, 3, 4
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new matrix containing the specified rows
     * @throws IndexOutOfBoundsException if indices are out of bounds or fromRowIndex > toRowIndex
     */
    @Override
    public Matrix<T> copy(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        final T[][] c = N.newArray(arrayType, toRowIndex - fromRowIndex);

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = a[i].clone();
        }

        return new Matrix<>(c);
    }

    /**
     * Creates a copy of a rectangular sub-matrix defined by row and column ranges.
     * The returned matrix contains elements from the specified row and column ranges.
     * All indices are inclusive of the 'from' index and exclusive of the 'to' index.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Original 4x4 matrix
     * Matrix<T> subMatrix = matrix.copy(1, 3, 1, 3); // Copy 2x2 center sub-matrix
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new matrix containing the specified sub-matrix
     * @throws IndexOutOfBoundsException if any indices are out of bounds or if fromIndex > toIndex
     */
    @Override
    public Matrix<T> copy(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final T[][] c = N.newArray(arrayType, toRowIndex - fromRowIndex);

        for (int i = fromRowIndex; i < toRowIndex; i++) {
            c[i - fromRowIndex] = N.copyOfRange(a[i], fromColumnIndex, toColumnIndex);
        }

        return new Matrix<>(c);
    }

    /**
     * Creates a new matrix with the specified dimensions by extending or truncating this matrix.
     * If the new dimensions are larger, new cells are filled with null values.
     * If the new dimensions are smaller, the matrix is truncated.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> extended = matrix.extend(3, 3); // 3x3 matrix with nulls in new cells
     * }</pre>
     *
     * @param newRows the number of rows in the new matrix
     * @param newCols the number of columns in the new matrix
     * @return a new matrix with the specified dimensions
     */
    public Matrix<T> extend(final int newRows, final int newCols) {
        return extend(newRows, newCols, null);
    }

    /**
     * Creates a new matrix with the specified dimensions by extending or truncating this matrix.
     * If the new dimensions are larger, new cells are filled with the specified default value.
     * If the new dimensions are smaller, the matrix is truncated.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> extended = matrix.extend(3, 3, 0); // 3x3 matrix with 0s in new cells
     * }</pre>
     *
     * @param newRows the number of rows in the new matrix
     * @param newCols the number of columns in the new matrix
     * @param defaultValueForNewCell the value to fill new cells with (can be null)
     * @return a new matrix with the specified dimensions
     * @throws IllegalArgumentException if newRows or newCols is negative
     */
    public Matrix<T> extend(final int newRows, final int newCols, final T defaultValueForNewCell) throws IllegalArgumentException {
        N.checkArgument(newRows >= 0, "The 'newRows' can't be negative %s", newRows);
        N.checkArgument(newCols >= 0, "The 'newCols' can't be negative %s", newCols);

        if (newRows <= rows && newCols <= cols) {
            return copy(0, newRows, 0, newCols);
        } else {
            final boolean fillDefaultValue = defaultValueForNewCell != null;
            final T[][] b = N.newArray(arrayType, newRows);

            for (int i = 0; i < newRows; i++) {
                b[i] = i < rows ? N.copyOf(a[i], newCols) : (T[]) N.newArray(elementType, newCols);

                if (fillDefaultValue) {
                    if (i >= rows) {
                        N.fill(b[i], defaultValueForNewCell);
                    } else if (cols < newCols) {
                        N.fill(b[i], cols, newCols, defaultValueForNewCell);
                    }
                }
            }

            return new Matrix<>(b);
        }
    }

    /**
     * Extends the matrix by adding rows and columns in all directions.
     * New cells are filled with null values.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> extended = matrix.extend(1, 1, 1, 1); // Adds 1 row/column on each side
     * // Result: 4x4 matrix with original in center
     * }</pre>
     *
     * @param toUp number of rows to add at the top
     * @param toDown number of rows to add at the bottom
     * @param toLeft number of columns to add on the left
     * @param toRight number of columns to add on the right
     * @return a new extended matrix
     */
    public Matrix<T> extend(final int toUp, final int toDown, final int toLeft, final int toRight) {
        return extend(toUp, toDown, toLeft, toRight, null);
    }

    /**
     * Extends the matrix by adding rows and columns in all directions.
     * New cells are filled with the specified default value.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> extended = matrix.extend(1, 1, 1, 1, 0);
     * // Result: 4x4 matrix with original in center, surrounded by 0s
     * }</pre>
     *
     * @param toUp number of rows to add at the top
     * @param toDown number of rows to add at the bottom
     * @param toLeft number of columns to add on the left
     * @param toRight number of columns to add on the right
     * @param defaultValueForNewCell the value to fill new cells with (can be null)
     * @return a new extended matrix
     * @throws IllegalArgumentException if any extension parameter is negative
     */
    public Matrix<T> extend(final int toUp, final int toDown, final int toLeft, final int toRight, final T defaultValueForNewCell)
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
            final boolean fillDefaultValue = defaultValueForNewCell != null;
            final T[][] b = N.newArray(arrayType, newRows);

            for (int i = 0; i < newRows; i++) {
                b[i] = N.newArray(elementType, newCols);

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

            return new Matrix<>(b);
        }
    }

    /**
     * Reverses the order of elements in each row (horizontal flip in-place).
     * This modifies the matrix directly.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.reverseH();
     * // Matrix is now: [[3, 2, 1], [6, 5, 4]]
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
     * This modifies the matrix directly.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}, {5, 6}});
     * matrix.reverseV();
     * // Matrix is now: [[5, 6], [3, 4], [1, 2]]
     * }</pre>
     *
     * @see #flipV()
     */
    public void reverseV() {
        for (int j = 0; j < cols; j++) {
            T tmp = null;
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
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * Matrix<Integer> flipped = matrix.flipH();
     * // Result: {{3, 2, 1}, {6, 5, 4}}
     * }</pre>
     *
     * @return a new horizontally flipped matrix
     * @see #flipV()
     * @see IntMatrix#flipH()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1</a>
     */
    public Matrix<T> flipH() {
        final Matrix<T> res = this.copy();
        res.reverseH();
        return res;
    }

    /**
     * Creates a new matrix that is vertically flipped (each column reversed).
     * The original matrix is not modified.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}, {5, 6}});
     * Matrix<Integer> flipped = matrix.flipV();
     * // Result: {{5, 6}, {3, 4}, {1, 2}}
     * }</pre>
     *
     * @return a new vertically flipped matrix
     * @see #flipH()
     * @see IntMatrix#flipV()
     * @see <a href="https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1">https://www.mathworks.com/help/matlab/ref/flip.html#btz149s-1</a>
     */
    public Matrix<T> flipV() {
        final Matrix<T> res = this.copy();
        res.reverseV();
        return res;
    }

    /**
     * Creates a new matrix rotated 90 degrees clockwise.
     * The dimensions are swapped: an m×n matrix becomes n×m.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}, {5, 6}});
     * Matrix<Integer> rotated = matrix.rotate90();
     * // Result: {{5, 3, 1}, {6, 4, 2}}
     * }</pre>
     *
     * @return a new matrix rotated 90 degrees clockwise
     */
    @Override
    public Matrix<T> rotate90() {
        final T[][] c = N.newArray(arrayType, cols);

        for (int i = 0; i < cols; i++) {
            c[i] = N.newArray(elementType, rows);
        }

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

        return new Matrix<>(c);
    }

    /**
     * Creates a new matrix rotated 180 degrees.
     * Equivalent to flipping both horizontally and vertically.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * Matrix<Integer> rotated = matrix.rotate180();
     * // Result: {{6, 5, 4}, {3, 2, 1}}
     * }</pre>
     *
     * @return a new matrix rotated 180 degrees
     */
    @Override
    public Matrix<T> rotate180() {
        final T[][] c = N.newArray(arrayType, rows);

        for (int i = 0; i < rows; i++) {
            c[i] = a[rows - i - 1].clone();
            N.reverse(c[i]);
        }

        return new Matrix<>(c);
    }

    /**
     * Creates a new matrix rotated 270 degrees clockwise (90 degrees counter-clockwise).
     * The dimensions are swapped: an m×n matrix becomes n×m.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}, {5, 6}});
     * Matrix<Integer> rotated = matrix.rotate270();
     * // Result: {{2, 4, 6}, {1, 3, 5}}
     * }</pre>
     *
     * @return a new matrix rotated 270 degrees clockwise
     */
    @Override
    public Matrix<T> rotate270() {
        final T[][] c = N.newArray(arrayType, cols);

        for (int i = 0; i < cols; i++) {
            c[i] = N.newArray(elementType, rows);
        }

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

        return new Matrix<>(c);
    }

    /**
     * Creates a new matrix that is the transpose of this matrix.
     * Rows become columns and columns become rows.
     * The dimensions are swapped: an m×n matrix becomes n×m.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * Matrix<Integer> transposed = matrix.transpose();
     * // Result: {{1, 4}, {2, 5}, {3, 6}}
     * }</pre>
     *
     * @return a new transposed matrix
     */
    @Override
    public Matrix<T> transpose() {
        final T[][] c = N.newArray(arrayType, cols);

        for (int i = 0; i < cols; i++) {
            c[i] = N.newArray(elementType, rows);
        }

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
        return new Matrix<>(c);
    }

    /**
     * Reshapes the matrix to new dimensions while preserving element order.
     * Elements are read row-by-row from the original matrix and placed
     * row-by-row into the new shape. The total number of elements may change.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * Matrix<Integer> reshaped = matrix.reshape(3, 2);
     * // Result: {{1, 2}, {3, 4}, {5, 6}}
     * }</pre>
     *
     * @param newRows the number of rows in the reshaped matrix
     * @param newCols the number of columns in the reshaped matrix
     * @return a new matrix with the specified shape
     */
    @SuppressFBWarnings("ICAST_INTEGER_MULTIPLY_CAST_TO_LONG")
    @Override
    public Matrix<T> reshape(final int newRows, final int newCols) {
        final T[][] c = N.newArray(arrayType, newRows);

        for (int i = 0; i < newRows; i++) {
            c[i] = N.newArray(elementType, newCols);
        }

        if (newRows == 0 || newCols == 0 || N.isEmpty(a)) {
            return new Matrix<>(c);
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

        return new Matrix<>(c);
    }

    /**
     * Repeats each element in the matrix by the specified number of times in both directions.
     * Each element is expanded into a block of rowRepeats×colRepeats identical elements.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> repeated = matrix.repelem(2, 3);
     * // Result: {{1,1,1,2,2,2}, {1,1,1,2,2,2}, {3,3,3,4,4,4}, {3,3,3,4,4,4}}
     * }</pre>
     *
     * @param rowRepeats number of times to repeat each element vertically
     * @param colRepeats number of times to repeat each element horizontally
     * @return a new matrix with repeated elements
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see IntMatrix#repelem(int, int)
     */
    @Override
    public Matrix<T> repelem(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final T[][] c = N.newArray(arrayType, rows * rowRepeats);

        for (int i = 0, len = c.length; i < len; i++) {
            c[i] = N.newArray(elementType, cols * colRepeats);
        }

        for (int i = 0; i < rows; i++) {
            final T[] aa = a[i];
            final T[] fr = c[i * rowRepeats];

            for (int j = 0; j < cols; j++) {
                // N.copy(Array.repeat(a[i][j], colRepeats), 0, fr, j * colRepeats, colRepeats);
                N.fill(fr, j * colRepeats, j * colRepeats + colRepeats, aa[j]);
            }

            for (int k = 1; k < rowRepeats; k++) {
                N.copy(fr, 0, c[i * rowRepeats + k], 0, fr.length);
            }
        }

        return new Matrix<>(c);
    }

    /**
     * Repeats the entire matrix as a tile pattern by the specified number of times.
     * The matrix is repeated as a whole block rowRepeats times vertically and colRepeats times horizontally.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> tiled = matrix.repmat(2, 3);
     * // Result: {{1,2,1,2,1,2}, {3,4,3,4,3,4}, {1,2,1,2,1,2}, {3,4,3,4,3,4}}
     * }</pre>
     *
     * @param rowRepeats number of times to repeat the matrix vertically
     * @param colRepeats number of times to repeat the matrix horizontally
     * @return a new matrix with the tiled pattern
     * @throws IllegalArgumentException if rowRepeats or colRepeats is not positive
     * @see IntMatrix#repmat(int, int)
     */
    @Override
    public Matrix<T> repmat(final int rowRepeats, final int colRepeats) throws IllegalArgumentException {
        N.checkArgument(rowRepeats > 0 && colRepeats > 0, "rowRepeats=%s and colRepeats=%s must be bigger than 0", rowRepeats, colRepeats);

        final T[][] c = N.newArray(arrayType, rows * rowRepeats);

        for (int i = 0, len = c.length; i < len; i++) {
            c[i] = N.newArray(elementType, cols * colRepeats);
        }

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

        return new Matrix<>(c);
    }

    /**
     * Flattens the matrix into a one-dimensional list by reading elements row by row.
     * The list is a new collection independent of the matrix.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * List<Integer> flat = matrix.flatten();
     * // Result: [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @return a list containing all elements in row-major order
     */
    @Override
    public List<T> flatten() {
        final T[] c = N.newArray(elementType, rows * cols);

        for (int i = 0; i < rows; i++) {
            N.copy(a[i], 0, c, i * cols, cols);
        }

        return N.asList(c);
    }

    /**
     * Applies an operation to each row array of the matrix by passing the row array
     * directly to the operation. This provides direct access to the internal row arrays.
     *
     * <p><b>Warning:</b> The operation receives direct references to internal row arrays.
     * Modifications within the operation will affect the matrix.</p>
     *
     * <p>This is useful for bulk operations that need to work with entire rows,
     * such as sorting rows or applying array-level transformations.</p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{3, 1, 2}, {6, 4, 5}});
     * // Sort each row
     * matrix.flatOp(row -> java.util.Arrays.sort(row));
     * // Matrix is now: [[1, 2, 3], [4, 5, 6]]
     * }</pre>
     *
     * @param <E> the type of exception that the operation may throw
     * @param op the operation to apply to each internal row array
     * @throws E if the operation throws an exception
     * @see ff#flatOp(Object[][], Throwables.Consumer)
     */
    @Override
    public <E extends Exception> void flatOp(final Throwables.Consumer<? super T[], E> op) throws E {
        ff.flatOp(a, op);
    }

    /**
     * Vertically stacks this matrix with another matrix.
     * The matrices must have the same number of columns.
     * The result has rows from this matrix followed by rows from the other matrix.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> m1 = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> m2 = Matrix.of(new Integer[][]{{5, 6}, {7, 8}});
     * Matrix<Integer> stacked = m1.vstack(m2);
     * // Result: {{1, 2}, {3, 4}, {5, 6}, {7, 8}}
     * }</pre>
     *
     * @param b the matrix to stack below this matrix
     * @return a new vertically stacked matrix
     * @throws IllegalArgumentException if the matrices have different column counts
     * @see IntMatrix#vstack(IntMatrix)
     */
    public Matrix<T> vstack(final Matrix<? extends T> b) throws IllegalArgumentException {
        N.checkArgument(cols == b.cols, "The count of column in this matrix and the specified matrix are not equals");

        final T[][] c = N.newArray(arrayType, rows + b.rows);
        int j = 0;

        for (int i = 0; i < rows; i++) {
            c[j++] = a[i].clone();
        }

        for (int i = 0; i < b.rows; i++) {
            c[j++] = b.a[i].clone();
        }

        return Matrix.of(c);
    }

    /**
     * Horizontally stacks this matrix with another matrix.
     * The matrices must have the same number of rows.
     * The result has columns from this matrix followed by columns from the other matrix.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> m1 = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> m2 = Matrix.of(new Integer[][]{{5}, {6}});
     * Matrix<Integer> stacked = m1.hstack(m2);
     * // Result: {{1, 2, 5}, {3, 4, 6}}
     * }</pre>
     *
     * @param b the matrix to stack to the right of this matrix
     * @return a new horizontally stacked matrix
     * @throws IllegalArgumentException if the matrices have different row counts
     * @see IntMatrix#hstack(IntMatrix)
     */
    public Matrix<T> hstack(final Matrix<? extends T> b) throws IllegalArgumentException {
        N.checkArgument(rows == b.rows, "The count of row in this matrix and the specified matrix are not equals");

        final T[][] c = N.newArray(arrayType, rows);

        for (int i = 0; i < rows; i++) {
            c[i] = N.copyOf(a[i], cols + b.cols);
            N.copy(b.a[i], 0, c[i], cols, b.cols);
        }

        return Matrix.of(c);
    }

    /**
     * Combines this matrix with another matrix element-wise using the specified function.
     * The function is applied to corresponding elements at the same positions (i, j) in both matrices.
     * Both matrices must have the same dimensions. The result matrix has the same element type as this matrix.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> m1 = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> m2 = Matrix.of(new Integer[][]{{5, 6}, {7, 8}});
     * Matrix<Integer> sum = m1.zipWith(m2, (a, b) -> a + b);
     * // Result: {{6, 8}, {10, 12}}
     * }</pre>
     *
     * @param <B> the element type of the other matrix
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the other matrix to zip with (must have the same dimensions)
     * @param zipFunction the binary function to apply to corresponding elements
     * @return a new matrix with the results of the zip function
     * @throws IllegalArgumentException if the matrices don't have the same dimensions
     * @throws E if the zip function throws an exception
     */
    public <B, E extends Exception> Matrix<T> zipWith(final Matrix<B> matrixB, final Throwables.BiFunction<? super T, ? super B, T, E> zipFunction) throws E {
        return zipWith(matrixB, zipFunction, elementType);
    }

    /**
     * Combines this matrix with another matrix element-wise using the specified function.
     * The function can return elements of a different type than the input matrices.
     * The matrices must have the same dimensions.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> m1 = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Double> m2 = Matrix.of(new Double[][]{{0.5, 1.0}, {1.5, 2.0}});
     * Matrix<String> result = m1.zipWith(m2, (a, b) -> a + ":" + b, String.class);
     * // Result: {{"1:0.5", "2:1.0"}, {"3:1.5", "4:2.0"}}
     * }</pre>
     *
     * @param <B> the element type of the other matrix
     * @param <R> the element type of the result matrix
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the other matrix to zip with
     * @param zipFunction the function to apply to corresponding elements
     * @param targetElementType the class of the result element type
     * @return a new matrix with the results of the zip function
     * @throws IllegalArgumentException if the matrices don't have the same shape
     * @throws E if the zip function throws an exception
     */
    public <B, R, E extends Exception> Matrix<R> zipWith(final Matrix<B> matrixB, final Throwables.BiFunction<? super T, ? super B, R, E> zipFunction,
            final Class<R> targetElementType) throws IllegalArgumentException, E {
        N.checkArgument(Matrixes.isSameShape(this, matrixB), "Can't zip two or more matrices which don't have same shape");

        final B[][] b = matrixB.a;
        final R[][] result = Matrixes.newArray(rows, cols, targetElementType);

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(a[i][j], b[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Combines three matrices element-wise using the specified ternary function.
     * The function is applied to corresponding elements from all three matrices.
     * All matrices must have the same dimensions.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> m1 = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> m2 = Matrix.of(new Integer[][]{{5, 6}, {7, 8}});
     * Matrix<Integer> m3 = Matrix.of(new Integer[][]{{9, 10}, {11, 12}});
     * Matrix<Integer> result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
     * // Result: {{15, 18}, {21, 24}}
     * }</pre>
     *
     * @param <B> the element type of the second matrix
     * @param <C> the element type of the third matrix
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix to zip with
     * @param matrixC the third matrix to zip with
     * @param zipFunction the function to apply to corresponding elements
     * @return a new matrix with the results of the zip function
     * @throws E if the zip function throws an exception
     */
    public <B, C, E extends Exception> Matrix<T> zipWith(final Matrix<B> matrixB, final Matrix<C> matrixC,
            final Throwables.TriFunction<? super T, ? super B, ? super C, T, E> zipFunction) throws E {
        return zipWith(matrixB, matrixC, zipFunction, elementType);
    }

    /**
     * Combines three matrices element-wise using the specified ternary function.
     * The function can return elements of a different type than the input matrices.
     * All matrices must have the same dimensions.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> m1 = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<String> m2 = Matrix.of(new String[][]{{"a", "b"}, {"c", "d"}});
     * Matrix<Double> m3 = Matrix.of(new Double[][]{{0.1, 0.2}, {0.3, 0.4}});
     * Matrix<String> result = m1.zipWith(m2, m3, 
     *     (i, s, d) -> i + s + String.format("%.1f", d), String.class);
     * // Result: {{"1a0.1", "2b0.2"}, {"3c0.3", "4d0.4"}}
     * }</pre>
     *
     * @param <B> the element type of the second matrix
     * @param <C> the element type of the third matrix
     * @param <R> the element type of the result matrix
     * @param <E> the type of exception that the zip function may throw
     * @param matrixB the second matrix to zip with
     * @param matrixC the third matrix to zip with
     * @param zipFunction the function to apply to corresponding elements
     * @param targetElementType the class of the result element type
     * @return a new matrix with the results of the zip function
     * @throws IllegalArgumentException if the matrices don't have the same shape
     * @throws E if the zip function throws an exception
     */
    public <B, C, R, E extends Exception> Matrix<R> zipWith(final Matrix<B> matrixB, final Matrix<C> matrixC,
            final Throwables.TriFunction<? super T, ? super B, ? super C, R, E> zipFunction, final Class<R> targetElementType)
            throws IllegalArgumentException, E {
        N.checkArgument(Matrixes.isSameShape(this, matrixB, matrixC), "Can't zip two or more matrices which don't have same shape");

        final B[][] b = matrixB.a;
        final C[][] c = matrixC.a;
        final R[][] result = Matrixes.newArray(rows, cols, targetElementType);

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(a[i][j], b[i][j], c[i][j]);

        Matrixes.run(rows, cols, cmd, Matrixes.isParallelable(this));

        return Matrix.of(result);
    }

    /**
     * Creates a stream of elements on the diagonal from left-upper to right-down.
     * The matrix must be square (same number of rows and columns).
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Integer> diagonal = matrix.streamLU2RD();
     * // Stream contains: 1, 5, 9
     * }</pre>
     *
     * @return a stream of diagonal elements from top-left to bottom-right
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public Stream<T> streamLU2RD() {
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
            public T next() {
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
     * Creates a stream of elements on the diagonal from right-upper to left-down.
     * The matrix must be square (same number of rows and columns).
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Integer> diagonal = matrix.streamRU2LD();
     * // Stream contains: 3, 5, 7
     * }</pre>
     *
     * @return a stream of diagonal elements from top-right to bottom-left
     * @throws IllegalStateException if the matrix is not square
     */
    @Override
    public Stream<T> streamRU2LD() {
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
            public T next() {
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
     * Creates a stream of all elements in row-major order (left to right, top to bottom).
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Stream<Integer> stream = matrix.streamH();
     * // Stream contains: 1, 2, 3, 4
     * }</pre>
     *
     * @return a stream of all elements in row-major order
     */
    @Override
    public Stream<T> streamH() {
        return streamH(0, rows);
    }

    /**
     * Creates a stream of elements from a specific row.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Integer> row1 = matrix.streamH(1);
     * // Stream contains: 4, 5, 6
     * }</pre>
     *
     * @param rowIndex the index of the row to stream
     * @return a stream of elements from the specified row
     * @throws IndexOutOfBoundsException if rowIndex is out of bounds
     */
    @Override
    public Stream<T> streamH(final int rowIndex) {
        return streamH(rowIndex, rowIndex + 1);
    }

    /**
     * Creates a stream of elements from a range of rows in row-major order.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}, {5, 6}});
     * Stream<Integer> rows = matrix.streamH(1, 3);
     * // Stream contains: 3, 4, 5, 6
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of elements from the specified row range
     * @throws IndexOutOfBoundsException if indices are out of bounds
     */
    @Override
    public Stream<T> streamH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
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
            public T next() {
                if (i >= toRowIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final T result = a[i][j++];

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
            public <A> A[] toArray(A[] c) {
                final int len = (int) count();

                if (c.length < len) {
                    c = N.copyOf(c, len);
                }

                for (int k = 0; k < len; k++) {
                    c[k] = (A) a[i][j++];

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
     * Creates a stream of all elements in column-major order (top to bottom, left to right).
     * This method is marked as @Beta.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Stream<Integer> stream = matrix.streamV();
     * // Stream contains: 1, 3, 2, 4
     * }</pre>
     *
     * @return a stream of all elements in column-major order
     */
    @Override
    @Beta
    public Stream<T> streamV() {
        return streamV(0, cols);
    }

    /**
     * Creates a stream of elements from a specific column.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Integer> col1 = matrix.streamV(1);
     * // Stream contains: 2, 5, 8
     * }</pre>
     *
     * @param columnIndex the index of the column to stream
     * @return a stream of elements from the specified column
     * @throws IndexOutOfBoundsException if columnIndex is out of bounds
     */
    @Override
    public Stream<T> streamV(final int columnIndex) {
        return streamV(columnIndex, columnIndex + 1);
    }

    /**
     * Creates a stream of elements from a range of columns in column-major order.
     * This method is marked as @Beta.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<Integer> cols = matrix.streamV(1, 3);
     * // Stream contains: 2, 5, 3, 6
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of elements from the specified column range
     * @throws IndexOutOfBoundsException if indices are out of bounds
     */
    @Beta
    @Override
    public Stream<T> streamV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public T next() {
                if (j >= toColumnIndex) {
                    throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
                }

                final T result = a[i++][j];

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

                if (n >= (long) (toColumnIndex - j) * Matrix.this.rows - i) {
                    i = 0;
                    j = toColumnIndex;
                } else {
                    final int offset = (int) (n + i);
                    i = offset % Matrix.this.rows;
                    j += offset / Matrix.this.rows;
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
                    c[k] = (A) a[i++][j];

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
     * Creates a stream of streams, where each inner stream represents a row.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}, {5, 6}});
     * Stream<Stream<Integer>> rows = matrix.streamR();
     * // Outer stream contains 3 inner streams, each with row elements
     * }</pre>
     *
     * @return a stream of row streams
     */
    @Override
    public Stream<Stream<T>> streamR() {
        return streamR(0, rows);
    }

    /**
     * Creates a stream of streams for a range of rows.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}, {5, 6}});
     * Stream<Stream<Integer>> rows = matrix.streamR(1, 3);
     * // Outer stream contains 2 inner streams for rows 1 and 2
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of row streams for the specified range
     * @throws IndexOutOfBoundsException if indices are out of bounds
     */
    @Override
    public Stream<Stream<T>> streamR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
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
            public Stream<T> next() {
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
     * Creates a stream of streams, where each inner stream represents a column.
     * This method is marked as @Beta.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<Stream<Integer>> columns = matrix.streamC();
     * // Outer stream contains 3 inner streams, each with column elements
     * }</pre>
     *
     * @return a stream of column streams
     */
    @Override
    @Beta
    public Stream<Stream<T>> streamC() {
        return streamC(0, cols);
    }

    /**
     * Creates a stream of streams for a range of columns.
     * This method is marked as @Beta.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * Stream<Stream<Integer>> columns = matrix.streamC(1, 3);
     * // Outer stream contains 2 inner streams for columns 1 and 2
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of column streams for the specified range
     * @throws IndexOutOfBoundsException if indices are out of bounds
     */
    @Override
    @Beta
    public Stream<Stream<T>> streamC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
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
            public Stream<T> next() {
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
                    public T next() {
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
     * This is an internal helper method used by the abstract base class for iteration
     * and size calculations. It handles null arrays by returning 0.
     *
     * @param a the array to check
     * @return the length of the array, or 0 if the array is null
     */
    @Override
    protected int length(@SuppressWarnings("hiding") final T[] a) {
        return a == null ? 0 : a.length;
    }

    /**
     * Applies the given action to each element in the matrix.
     * The action is performed on all elements in row-major order.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * matrix.forEach(element -> System.out.print(element + " "));
     * // Prints: 1 2 3 4
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the action to perform on each element
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.Consumer<? super T, E> action) throws E {
        forEach(0, rows, 0, cols, action);
    }

    /**
     * Applies the given action to each element in a specified sub-matrix.
     * The action is performed on elements within the specified row and column ranges.
     * Processing may be parallelized for large matrices.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * matrix.forEach(1, 3, 1, 3, element -> System.out.print(element + " "));
     * // Prints: 5 6 8 9 (elements from rows 1-2, columns 1-2)
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
            final Throwables.Consumer<? super T, E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(a[i][j]);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                final T[] aa = a[i];

                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(aa[j]);
                }
            }
        }
    }

    /**
     * Converts this matrix to a Dataset with horizontally organized data.
     * Each row in the matrix becomes a record in the Dataset, and each column
     * is assigned the corresponding name from the provided collection.
     *
     * <p>The column names are used in the order they appear in the collection,
     * and must match the number of columns in the matrix exactly.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * List<String> columnNames = Arrays.asList("A", "B", "C");
     * 
     * Dataset dataset = matrix.toDatasetH(columnNames);
     * // Dataset with:
     * // A  B  C
     * // -------
     * // 1  2  3
     * // 4  5  6
     * 
     * // Access data by column name
     * List<Integer> columnA = dataset.getColumn("A"); // [1, 4]
     * }</pre>
     *
     * @param columnNames the names to assign to each column in the resulting Dataset
     * @return a Dataset containing the matrix data with the specified column names
     * @throws IllegalArgumentException if the size of columnNames doesn't match the column count
     * @see Dataset
     */
    @Beta
    public Dataset toDatasetH(final Collection<String> columnNames) throws IllegalArgumentException {
        N.checkArgument(columnNames.size() == cols, "The size({}) of specified columnNames and column count({}) of this Matrix are not equals",
                columnNames.size(), cols);

        final List<String> newColumnNameList = new ArrayList<>(columnNames);
        final List<List<Object>> newColumnList = new ArrayList<>(newColumnNameList.size());

        for (int j = 0; j < cols; j++) {
            final List<Object> column = new ArrayList<>(rows);

            for (int i = 0; i < rows; i++) {
                column.add(a[i][j]);
            }

            newColumnList.add(column);
        }

        return new RowDataset(newColumnNameList, newColumnList);
    }

    /**
     * Converts this matrix to a Dataset with vertically organized data.
     * Each column in the matrix becomes a record in the Dataset, and each row
     * is assigned the corresponding name from the provided collection.
     *
     * <p>The column names are used in the order they appear in the collection,
     * and must match the number of rows in the matrix exactly.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * List<String> columnNames = Arrays.asList("Row1", "Row2");
     *
     * Dataset dataset = matrix.toDatasetV(columnNames);
     * // Dataset with:
     * // Row1  Row2
     * // ----------
     * // 1     4
     * // 2     5
     * // 3     6
     * }</pre>
     *
     * @param columnNames the collection of column names to use for the Dataset
     * @return a Dataset containing the matrix data organized vertically
     * @throws IllegalArgumentException if the number of column names doesn't match the number of rows in the matrix
     * @see Dataset
     * @see RowDataset
     */
    @Beta
    public Dataset toDatasetV(final Collection<String> columnNames) throws IllegalArgumentException {
        N.checkArgument(columnNames.size() == rows, "The size({}) of specified columnNames and row count({}) of this Matrix are not equals", columnNames.size(),
                rows);

        final List<String> newColumnNameList = new ArrayList<>(columnNames);
        final List<List<Object>> newColumnList = new ArrayList<>(newColumnNameList.size());

        for (int i = 0; i < rows; i++) {
            newColumnList.add(new ArrayList<>(Array.asList(a[i])));
        }

        return new RowDataset(newColumnNameList, newColumnList);
    }

    /**
     * Prints this matrix to the standard output using the Arrays.println method.
     * This provides a convenient way to visualize the matrix content.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
     * matrix.println();
     * // Output:
     * // [[1, 2, 3], 
     * //  [4, 5, 6]]
     * }</pre>
     */
    @Override
    public void println() {
        Arrays.println(a);
    }

    /**
     * Returns a hash code value for this matrix.
     * The hash code is computed using a deep hash algorithm that considers
     * all elements in the matrix.
     *
     * @return a hash code value for this matrix
     */
    @Override
    public int hashCode() {
        return N.deepHashCode(a);
    }

    /**
     * Compares this matrix with the specified object for equality.
     * Two matrices are considered equal if they have the same dimensions and
     * contain the same elements in the same positions.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Matrix<Integer> matrix1 = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> matrix2 = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * Matrix<Integer> matrix3 = Matrix.of(new Integer[][]{{1, 3}, {2, 4}});
     *
     * boolean result1 = matrix1.equals(matrix2); // true
     * boolean result2 = matrix1.equals(matrix3); // false
     * }</pre>
     *
     * @param obj the object to compare with
     * @return {@code true} if this matrix is equal to the specified object,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Matrix) {
            final Matrix<T> another = (Matrix<T>) obj;

            return cols == another.cols && rows == another.rows && N.deepEquals(a, another.a);
        }

        return false;
    }

    /**
     * Returns a string representation of this matrix.
     * The string representation consists of a list of the matrix's elements,
     * enclosed in square brackets ("[]"). Adjacent elements are separated by
     * the characters ", " (comma and space).
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Matrix<Integer> matrix = Matrix.of(new Integer[][]{{1, 2}, {3, 4}});
     * String str = matrix.toString();
     * // Result: "[[1, 2], [3, 4]]"
     * }</pre>
     *
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        return N.deepToString(a);
    }
}
