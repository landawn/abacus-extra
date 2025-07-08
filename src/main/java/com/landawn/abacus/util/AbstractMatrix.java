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

import com.landawn.abacus.annotation.SuppressFBWarnings;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.Stream;

/**
 * Abstract base class for all matrix implementations in the Abacus library.
 * This class provides the common structure and operations for working with two-dimensional arrays
 * of various primitive types and objects.
 * 
 * <p>The class uses a sealed hierarchy to ensure type safety and provides common functionality such as:</p>
 * <ul>
 *   <li>Matrix dimensions (rows, columns, count)</li>
 *   <li>Stream-based access patterns (horizontal, vertical, diagonal)</li>
 *   <li>Matrix transformations (transpose, rotate, reshape)</li>
 *   <li>Element-wise operations</li>
 * </ul>
 * 
 * <p>Convention: {@code R} = Row, {@code C} = Column, {@code H} = Horizontal, {@code V} = Vertical.</p>
 * 
 * <p>Example usage through concrete implementations:</p>
 * <pre>{@code
 * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
 * IntMatrix transposed = matrix.transpose();
 * }</pre>
 *
 * @param <A> the array type (e.g., int[], double[], Object[])
 * @param <PL> the primitive list type for flattened operations
 * @param <ES> the element stream type
 * @param <RS> the row/column stream type
 * @param <X> the concrete matrix type (self-type)
 * @see BooleanMatrix
 * @see ByteMatrix
 * @see ShortMatrix
 * @see IntMatrix
 * @see LongMatrix
 * @see FloatMatrix
 * @see DoubleMatrix
 * @see CharMatrix
 * @see Matrix
 */
public abstract sealed class AbstractMatrix<A, PL, ES, RS, X extends AbstractMatrix<A, PL, ES, RS, X>>
        permits BooleanMatrix, CharMatrix, ByteMatrix, ShortMatrix, DoubleMatrix, FloatMatrix, IntMatrix, LongMatrix, Matrix {

    static final char CHAR_0 = (char) 0;

    static final byte BYTE_0 = (byte) 0;

    static final byte BYTE_1 = (byte) 1;

    static final short SHORT_0 = (short) 0;

    /**
     * The number of rows in this matrix.
     * This value is immutable after matrix creation.
     */
    public final int rows;

    /**
     * The number of columns in this matrix.
     * This value is immutable after matrix creation.
     */
    public final int cols;

    /**
     * The total number of elements in this matrix (rows × columns).
     * This value is cached for performance and is immutable after matrix creation.
     */
    public final long count;

    /**
     * The underlying two-dimensional array storing the matrix data.
     * Direct access to this array should be avoided; use the provided methods instead.
     */
    final A[] a;

    /**
     * Constructs a new AbstractMatrix with the specified two-dimensional array.
     * The constructor validates that all rows have the same length.
     * 
     * @param a the two-dimensional array containing matrix data
     * @throws IllegalArgumentException if the array is null or if rows have different lengths
     */
    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    protected AbstractMatrix(final A[] a) {
        this.a = a;
        rows = a.length;
        cols = a.length == 0 ? 0 : length(a[0]);

        if (a.length > 1) {
            for (int i = 1, len = a.length; i < len; i++) {
                if (length(a[i]) != cols) {
                    throw new IllegalArgumentException("The length of sub arrays must be same");
                }
            }
        }

        count = (long) cols * rows;
    }

    /**
     * Returns the component type of the elements in this matrix.
     * For primitive matrices, this returns the primitive class (e.g., int.class).
     * For object matrices, this returns the element class type.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix intMatrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * Class<?> type = intMatrix.componentType(); // Returns int.class
     * }</pre>
     * 
     * @return the Class object representing the component type
     */
    @SuppressWarnings("rawtypes")
    public abstract Class componentType();

    /**
     * Returns the underlying two-dimensional array of this matrix.
     * This method exposes the internal array representation and should be used with caution
     * as modifications to the returned array will affect the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * int[][] array = matrix.array();
     * // array[0][0] = 10; // This would modify the matrix!
     * }</pre>
     * 
     * @return the underlying two-dimensional array
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public A[] array() {
        return a;
    }

    /**
     * Prints the matrix to standard output in a formatted manner.
     * Each implementation provides its own formatting based on the element type.
     * This method is primarily intended for debugging purposes.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.println(); // Outputs the matrix in a readable format
     * }</pre>
     */
    public abstract void println();

    /**
     * Checks if the matrix is empty (has no elements).
     * A matrix is empty if either the number of rows or columns is zero.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix empty = IntMatrix.of(new int[0][0]);
     * boolean isEmpty = empty.isEmpty(); // Returns true
     * }</pre>
     * 
     * @return {@code true} if the matrix has no elements, {@code false} otherwise
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Creates a deep copy of this matrix.
     * The returned matrix is independent of the original; modifications to one
     * do not affect the other.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix original = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * IntMatrix copy = original.copy();
     * copy.set(0, 0, 10); // Original matrix remains unchanged
     * }</pre>
     * 
     * @return a new matrix with the same dimensions and copied elements
     */
    public abstract X copy();

    /**
     * Creates a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}, {5, 6}});
     * IntMatrix subMatrix = matrix.copy(0, 2); // Contains rows 0 and 1
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new matrix containing the specified rows
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    public abstract X copy(int fromRowIndex, int toRowIndex);

    /**
     * Creates a copy of a rectangular region from this matrix.
     * The returned matrix contains only the specified rows and columns.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntMatrix subMatrix = matrix.copy(0, 2, 1, 3); // Contains [[2, 3], [5, 6]]
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new matrix containing the specified region
     * @throws IndexOutOfBoundsException if any index is out of bounds
     */
    public abstract X copy(int fromRowIndex, int toRowIndex, int fromColumnIndex, int toColumnIndex);

    /**
     * Rotates this matrix 90 degrees clockwise.
     * The resulting matrix will have its rows and columns swapped,
     * with rows becoming columns in reverse order.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Original:    Rotated 90°:
     * // 1 2 3        7 4 1
     * // 4 5 6   =>   8 5 2
     * // 7 8 9        9 6 3
     * }</pre>
     * 
     * @return a new matrix rotated 90 degrees clockwise
     */
    public abstract X rotate90();

    /**
     * Rotates this matrix 180 degrees.
     * This is equivalent to flipping both horizontally and vertically.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Original:    Rotated 180°:
     * // 1 2 3        9 8 7
     * // 4 5 6   =>   6 5 4
     * // 7 8 9        3 2 1
     * }</pre>
     * 
     * @return a new matrix rotated 180 degrees
     */
    public abstract X rotate180();

    /**
     * Rotates this matrix 270 degrees clockwise (90 degrees counter-clockwise).
     * The resulting matrix will have its rows and columns swapped,
     * with columns becoming rows in reverse order.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Original:    Rotated 270°:
     * // 1 2 3        3 6 9
     * // 4 5 6   =>   2 5 8
     * // 7 8 9        1 4 7
     * }</pre>
     * 
     * @return a new matrix rotated 270 degrees clockwise
     */
    public abstract X rotate270();

    /**
     * Creates the transpose of this matrix.
     * The transpose operation swaps rows and columns, so element at position (i,j)
     * moves to position (j,i).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Original:    Transposed:
     * // 1 2 3        1 4 7
     * // 4 5 6   =>   2 5 8
     * // 7 8 9        3 6 9
     * }</pre>
     * 
     * @return a new matrix that is the transpose of this matrix
     */
    public abstract X transpose();

    /**
     * Reshapes this matrix to have the specified number of columns.
     * The number of rows is automatically calculated based on the total element count.
     * Elements are taken in row-major order from the original matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped = matrix.reshape(2); // Becomes [[1, 2], [3, 4], [5, 6]]
     * }</pre>
     * 
     * @param newCols the number of columns in the reshaped matrix
     * @return a new matrix with the specified number of columns
     */
    public X reshape(final int newCols) {
        return reshape((int) (count % newCols == 0 ? count / newCols : count / newCols + 1), newCols);
    }

    /**
     * Reshapes this matrix to have the specified dimensions.
     * Elements are taken in row-major order from the original matrix and
     * placed into the new shape. If the new shape has more elements than
     * the original, the extra positions are filled with default values.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped = matrix.reshape(3, 2); // Becomes [[1, 2], [3, 4], [5, 6]]
     * }</pre>
     * 
     * @param newRows the number of rows in the reshaped matrix
     * @param newCols the number of columns in the reshaped matrix
     * @return a new matrix with the specified dimensions
     */
    public abstract X reshape(int newRows, int newCols);

    /**
     * Checks if this matrix has the same shape (dimensions) as another matrix.
     * Two matrices have the same shape if they have the same number of rows and columns.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * IntMatrix m2 = IntMatrix.of(new int[][]{{5, 6}, {7, 8}});
     * boolean sameShape = m1.isSameShape(m2); // Returns true
     * }</pre>
     * 
     * @param x the matrix to compare with
     * @return {@code true} if both matrices have the same dimensions, {@code false} otherwise
     */
    public boolean isSameShape(final X x) {
        return rows == x.rows && cols == x.cols;
    }

    /**
     * Repeats each element in the matrix the specified number of times in both dimensions.
     * Each element is expanded into a block of size rowRepeats × colRepeats.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Original:    repelem(2, 2):
     * // 1 2          1 1 2 2
     * // 3 4     =>   1 1 2 2
     * //              3 3 4 4
     * //              3 3 4 4
     * }</pre>
     * 
     * @param rowRepeats number of times to repeat each element in the row direction
     * @param colRepeats number of times to repeat each element in the column direction
     * @return a new matrix with repeated elements
     * @throws IllegalArgumentException if rowRepeats or colRepeats is less than 1
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repelem.html">MATLAB repelem</a>
     */
    public abstract X repelem(int rowRepeats, int colRepeats);

    /**
     * Repeats the entire matrix the specified number of times in both dimensions.
     * The matrix is tiled rowRepeats times vertically and colRepeats times horizontally.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Original:    repmat(2, 2):
     * // 1 2          1 2 1 2
     * // 3 4     =>   3 4 3 4
     * //              1 2 1 2
     * //              3 4 3 4
     * }</pre>
     * 
     * @param rowRepeats number of times to repeat the matrix in the row direction
     * @param colRepeats number of times to repeat the matrix in the column direction
     * @return a new matrix with the original matrix repeated
     * @throws IllegalArgumentException if rowRepeats or colRepeats is less than 1
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repmat.html">MATLAB repmat</a>
     */
    public abstract X repmat(int rowRepeats, int colRepeats);

    /**
     * Flattens the matrix into a one-dimensional list.
     * Elements are taken in row-major order (row by row from left to right).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * IntList flat = matrix.flatten(); // Returns [1, 2, 3, 4]
     * }</pre>
     * 
     * @return a list containing all elements in row-major order
     */
    public abstract PL flatten();

    /**
     * Applies an operation to the flattened view of the matrix and updates the matrix with the result.
     * This method flattens the matrix, executes the operation on the flattened array,
     * then sets the values back into the matrix structure.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{3, 1, 4}, {1, 5, 9}});
     * matrix.flatOp(a -> Arrays.sort(a)); // Matrix becomes [[1, 1, 3], [4, 5, 9]]
     * }</pre>
     * 
     * @param <E> the type of exception that the operation might throw
     * @param op the operation to apply to the flattened array
     * @throws E if the operation throws an exception
     */
    public abstract <E extends Exception> void flatOp(Throwables.Consumer<? super A, E> op) throws E;

    /**
     * Performs the given action for each position in the matrix.
     * The action receives the row and column indices for each element.
     * For large matrices, the operation may be parallelized.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.forEach((i, j) -> {
     *     System.out.println("Element at (" + i + "," + j + ")");
     * });
     * }</pre>
     * 
     * @param <E> the type of exception that the action might throw
     * @param action the action to perform for each position
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.IntBiConsumer<E> action) throws E {
        if (Matrixes.isParallelable(this)) {
            //noinspection FunctionalExpressionCanBeFolded
            final Throwables.IntBiConsumer<E> cmd = action::accept;
            Matrixes.run(rows, cols, cmd, true);
        } else {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    action.accept(i, j);
                }
            }
        }
    }

    /**
     * Performs the given action for each position in the specified region of the matrix.
     * The action receives the row and column indices for each element in the region.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Process only a 2x2 subregion starting at (1,1)
     * matrix.forEach(1, 3, 1, 3, (i, j) -> {
     *     System.out.println("Processing element at (" + i + "," + j + ")");
     * });
     * }</pre>
     * 
     * @param <E> the type of exception that the action might throw
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to perform for each position
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntBiConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            //noinspection FunctionalExpressionCanBeFolded
            final Throwables.IntBiConsumer<E> cmd = action::accept;
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(i, j);
                }
            }
        }
    }

    /**
     * Performs the given action for each position in the matrix, providing the matrix itself as a parameter.
     * This is useful when the action needs access to matrix elements or methods.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.forEach((i, j, m) -> {
     *     System.out.println("Value at (" + i + "," + j + ") is " + m.get(i, j));
     * });
     * }</pre>
     * 
     * @param <E> the type of exception that the action might throw
     * @param action the action to perform, receiving row index, column index, and the matrix
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.BiIntObjConsumer<X, E> action) throws E {
        final X x = (X) this;
        if (Matrixes.isParallelable(this)) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(i, j, x);
            Matrixes.run(rows, cols, cmd, true);
        } else {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    action.accept(i, j, x);
                }
            }
        }
    }

    /**
     * Performs the given action for each position in the specified region, providing the matrix itself.
     * This combines region-based iteration with matrix access.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.forEach(1, 3, 1, 3, (i, j, m) -> {
     *     // Process only the 2x2 subregion with access to matrix
     *     System.out.println("Value: " + m.get(i, j));
     * });
     * }</pre>
     * 
     * @param <E> the type of exception that the action might throw
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to perform, receiving row index, column index, and the matrix
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.BiIntObjConsumer<X, E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        final X x = (X) this;

        if (Matrixes.isParallelable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> cmd = (i, j) -> action.accept(i, j, x);
            Matrixes.run(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, cmd, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(i, j, x);
                }
            }
        }
    }

    /**
     * Returns a stream of points along the main diagonal (left-up to right-down).
     * The matrix must be square for this operation.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> diagonal = matrix.pointsLU2RD(); // Points: (0,0), (1,1), (2,2)
     * }</pre>
     * 
     * @return a stream of points representing the main diagonal positions
     * @throws IllegalStateException if the matrix is not square
     */
    public Stream<Point> pointsLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        //noinspection resource
        return IntStream.range(0, rows).mapToObj(i -> Point.of(i, i));
    }

    /**
     * Returns a stream of points along the anti-diagonal (right-up to left-down).
     * The matrix must be square for this operation.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> antiDiagonal = matrix.pointsRU2LD(); // Points: (0,2), (1,1), (2,0)
     * }</pre>
     * 
     * @return a stream of points representing the anti-diagonal positions
     * @throws IllegalStateException if the matrix is not square
     */
    public Stream<Point> pointsRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        //noinspection resource
        return IntStream.range(0, rows).mapToObj(i -> Point.of(i, cols - i - 1));
    }

    /**
     * Returns a stream of all points in the matrix in row-major order.
     * Points are generated row by row from left to right.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Stream<Point> allPoints = matrix.pointsH();
     * allPoints.forEach(p -> System.out.println("Point: " + p));
     * }</pre>
     * 
     * @return a stream of all points in row-major order
     */
    public Stream<Point> pointsH() {
        return pointsH(0, rows);
    }

    /**
     * Returns a stream of points for a specific row.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Stream<Point> row1Points = matrix.pointsH(1); // All points in row 1
     * }</pre>
     * 
     * @param rowIndex the row index
     * @return a stream of points in the specified row
     */
    public Stream<Point> pointsH(final int rowIndex) {
        return pointsH(rowIndex, rowIndex + 1);
    }

    /**
     * Returns a stream of points for a range of rows in row-major order.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Get points from rows 1 and 2
     * Stream<Point> points = matrix.pointsH(1, 3);
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of points in the specified row range
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @SuppressWarnings("resource")
    public Stream<Point> pointsH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        return IntStream.range(fromRowIndex, toRowIndex)
                .flatMapToObj(rowIndex -> IntStream.range(0, cols).mapToObj(columnIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of all points in the matrix in column-major order.
     * Points are generated column by column from top to bottom.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Stream<Point> columnOrder = matrix.pointsV();
     * // For a 2x3 matrix, order would be: (0,0), (1,0), (0,1), (1,1), (0,2), (1,2)
     * }</pre>
     * 
     * @return a stream of all points in column-major order
     */
    public Stream<Point> pointsV() {
        return pointsV(0, cols);
    }

    /**
     * Returns a stream of points for a specific column.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Stream<Point> col2Points = matrix.pointsV(2); // All points in column 2
     * }</pre>
     * 
     * @param columnIndex the column index
     * @return a stream of points in the specified column
     */
    public Stream<Point> pointsV(final int columnIndex) {
        return pointsV(columnIndex, columnIndex + 1);
    }

    /**
     * Returns a stream of points for a range of columns in column-major order.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Get points from columns 1 through 3
     * Stream<Point> points = matrix.pointsV(1, 4);
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of points in the specified column range
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @SuppressWarnings("resource")
    public Stream<Point> pointsV(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        return IntStream.range(fromColumnIndex, toColumnIndex)
                .flatMapToObj(columnIndex -> IntStream.range(0, rows).mapToObj(rowIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of streams where each inner stream represents a row of points.
     * This allows for row-by-row processing of matrix positions.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.pointsR().forEach(rowStream -> {
     *     rowStream.forEach(point -> System.out.println("Point: " + point));
     * });
     * }</pre>
     * 
     * @return a stream of row streams of points
     */
    public Stream<Stream<Point>> pointsR() {
        return pointsR(0, rows);
    }

    /**
     * Returns a stream of streams for a range of rows, where each inner stream represents a row of points.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Process rows 1 and 2 separately
     * matrix.pointsR(1, 3).forEach(rowStream -> {
     *     List<Point> rowPoints = rowStream.toList();
     *     // Process each row's points
     * });
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of row streams of points
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @SuppressWarnings("resource")
    public Stream<Stream<Point>> pointsR(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        return IntStream.range(fromRowIndex, toRowIndex)
                .mapToObj(rowIndex -> IntStream.range(0, cols).mapToObj(columnIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of streams where each inner stream represents a column of points.
     * This allows for column-by-column processing of matrix positions.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.pointsC().forEach(colStream -> {
     *     colStream.forEach(point -> System.out.println("Point: " + point));
     * });
     * }</pre>
     * 
     * @return a stream of column streams of points
     */
    public Stream<Stream<Point>> pointsC() {
        return pointsR(0, cols);
    }

    /**
     * Returns a stream of streams for a range of columns, where each inner stream represents a column of points.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Process columns 2 through 4 separately
     * matrix.pointsC(2, 5).forEach(colStream -> {
     *     List<Point> colPoints = colStream.toList();
     *     // Process each column's points
     * });
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of column streams of points
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    @SuppressWarnings("resource")
    public Stream<Stream<Point>> pointsC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        return IntStream.range(fromColumnIndex, toColumnIndex)
                .mapToObj(columnIndex -> IntStream.range(0, rows).mapToObj(rowIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of elements along the main diagonal (left-up to right-down).
     * The matrix must be square for this operation.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntStream diagonal = matrix.streamLU2RD(); // Stream of: 1, 5, 9
     * }</pre>
     * 
     * @return a stream of diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    public abstract ES streamLU2RD();

    /**
     * Returns a stream of elements along the anti-diagonal (right-up to left-down).
     * The matrix must be square for this operation.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntStream antiDiagonal = matrix.streamRU2LD(); // Stream of: 3, 5, 7
     * }</pre>
     * 
     * @return a stream of anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square
     */
    public abstract ES streamRU2LD();

    /**
     * Returns a stream of all elements in row-major order.
     * Elements are streamed row by row from left to right.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * IntStream elements = matrix.streamH(); // Stream of: 1, 2, 3, 4
     * }</pre>
     * 
     * @return a stream of all elements in row-major order
     */
    public abstract ES streamH();

    /**
     * Returns a stream of elements from a specific row.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
     * IntStream row1 = matrix.streamH(1); // Stream of: 4, 5, 6
     * }</pre>
     * 
     * @param rowIndex the row index
     * @return a stream of elements in the specified row
     */
    public abstract ES streamH(final int rowIndex);

    /**
     * Returns a stream of elements from a range of rows in row-major order.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}, {5, 6}});
     * IntStream rows1and2 = matrix.streamH(1, 3); // Stream of: 3, 4, 5, 6
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of elements in the specified row range
     */
    public abstract ES streamH(final int fromRowIndex, final int toRowIndex);

    /**
     * Returns a stream of all elements in column-major order.
     * Elements are streamed column by column from top to bottom.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * IntStream elements = matrix.streamV(); // Stream of: 1, 3, 2, 4
     * }</pre>
     * 
     * @return a stream of all elements in column-major order
     */
    public abstract ES streamV();

    /**
     * Returns a stream of elements from a specific column.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
     * IntStream col1 = matrix.streamV(1); // Stream of: 2, 5
     * }</pre>
     * 
     * @param columnIndex the column index
     * @return a stream of elements in the specified column
     */
    public abstract ES streamV(final int columnIndex);

    /**
     * Returns a stream of elements from a range of columns in column-major order.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][]{{1, 2, 3}, {4, 5, 6}});
     * IntStream cols1and2 = matrix.streamV(1, 3); // Stream of: 2, 5, 3, 6
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of elements in the specified column range
     */
    public abstract ES streamV(final int fromColumnIndex, final int toColumnIndex);

    /**
     * Returns a stream of row streams.
     * Each element in the outer stream is a stream representing one row of the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.streamR().forEach(rowStream -> {
     *     int sum = rowStream.sum(); // Sum each row
     *     System.out.println("Row sum: " + sum);
     * });
     * }</pre>
     * 
     * @return a stream of row streams
     */
    public abstract RS streamR();

    /**
     * Returns a stream of row streams for a range of rows.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Process only rows 1 and 2
     * matrix.streamR(1, 3).forEach(rowStream -> {
     *     // Process each row
     * });
     * }</pre>
     * 
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of row streams for the specified range
     */
    public abstract RS streamR(final int fromRowIndex, final int toRowIndex);

    /**
     * Returns a stream of column streams.
     * Each element in the outer stream is a stream representing one column of the matrix.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.streamC().forEach(colStream -> {
     *     double avg = colStream.average().orElse(0); // Average each column
     *     System.out.println("Column average: " + avg);
     * });
     * }</pre>
     * 
     * @return a stream of column streams
     */
    public abstract RS streamC();

    /**
     * Returns a stream of column streams for a range of columns.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Process columns 2 through 4
     * matrix.streamC(2, 5).forEach(colStream -> {
     *     // Process each column
     * });
     * }</pre>
     * 
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of column streams for the specified range
     */
    public abstract RS streamC(final int fromColumnIndex, final int toColumnIndex);

    /**
     * Executes the given action with this matrix as the parameter.
     * This method enables fluent-style operations where the matrix needs to be passed to a consumer.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * matrix.accept(m -> {
     *     System.out.println("Matrix dimensions: " + m.rows + "x" + m.cols);
     *     m.println();
     * });
     * }</pre>
     * 
     * @param <E> the type of exception that the action might throw
     * @param action the action to perform on this matrix
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super X, E> action) throws E {
        action.accept((X) this);
    }

    /**
     * Applies the given function to this matrix and returns the result.
     * This method enables fluent-style transformations where the matrix needs to be passed to a function.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * double determinant = matrix.apply(MatrixUtils::calculateDeterminant);
     * String info = matrix.apply(m -> "Matrix " + m.rows + "x" + m.cols);
     * }</pre>
     * 
     * @param <R> the result type of the function
     * @param <E> the type of exception that the function might throw
     * @param action the function to apply to this matrix
     * @return the result of applying the function
     * @throws E if the function throws an exception
     */
    public <R, E extends Exception> R apply(final Throwables.Function<? super X, R, E> action) throws E {
        return action.apply((X) this);
    }

    /**
     * Returns the length of the given array.
     * This abstract method must be implemented by concrete subclasses to handle
     * their specific array types (primitive or object arrays).
     * 
     * @param a the array to measure
     * @return the length of the array
     */
    protected abstract int length(@SuppressWarnings("hiding") A a);

    /**
     * Checks that this matrix has the same shape as the given matrix.
     * Throws an exception if the shapes differ.
     * 
     * @param x the matrix to compare with
     * @throws IllegalArgumentException if the matrices have different shapes
     */
    protected void checkSameShape(final X x) {
        N.checkArgument(this.isSameShape(x), "Must be same shape");
    }

    /**
     * Checks that this matrix is square (same number of rows and columns).
     * Throws an exception if the matrix is not square.
     * 
     * @throws IllegalStateException if rows != cols
     */
    protected void checkIfRowAndColumnSizeAreSame() {
        N.checkState(rows == cols, "'rows' and 'cols' must be same to get diagonals: rows=%s, cols=%s", rows, cols);
    }

}