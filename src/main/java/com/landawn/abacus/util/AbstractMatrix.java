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
 * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
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

    protected static final String ARRAY_PRINT_SEPARATOR = Arrays.ARRAY_PRINT_SEPARATOR;

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
     * @throws IllegalArgumentException if the array is null or if rows have different lengths (not rectangular)
     */
    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    protected AbstractMatrix(final A[] a) {
        N.checkArgNotNull(a, "The array of arrays can't be null");

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
     * For primitive matrices, this returns the corresponding primitive class (e.g., {@code int.class} for {@link IntMatrix}).
     * For object matrices, this returns the element's class type.
     *
     * <p>This method is useful for reflection-based operations and type checking at runtime.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix intMatrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Class<?> type = intMatrix.componentType(); // Returns int.class
     *
     * Matrix<String> stringMatrix = Matrix.of(new String[][] {{"a", "b"}, {"c", "d"}});
     * Class<?> strType = stringMatrix.componentType(); // Returns String.class
     * }</pre>
     *
     * @return the Class object representing the component type of matrix elements
     */
    @SuppressWarnings("rawtypes")
    public abstract Class componentType();

    /**
     * Returns the underlying two-dimensional array of this matrix.
     * This method exposes the internal array representation for performance reasons and should be used with caution
     * as modifications to the returned array will directly affect the matrix.
     *
     * <p><strong>Warning:</strong> This method returns the actual internal array, not a copy.
     * Any changes made to the returned array will be reflected in the matrix.
     * If you need an independent copy, use {@link #copy()} instead.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * int[][] array = matrix.array();
     * array[0][0] = 10; // This WILL modify the matrix!
     * // matrix now contains {{10, 2}, {3, 4}}
     * }</pre>
     *
     * @return the underlying two-dimensional array (not a copy)
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public A[] array() {
        return a;
    }

    /**
     * Prints the matrix to standard output in a formatted, human-readable manner.
     * Each concrete implementation provides its own formatting based on the element type.
     * This method is primarily intended for debugging and logging purposes.
     *
     * <p>The exact output format depends on the matrix type:
     * <ul>
     *   <li>Numeric matrices typically display values aligned in rows and columns</li>
     *   <li>Object matrices display using the {@code toString()} method of elements</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * matrix.println();
     * // Output:
     * // [1, 2]
     * // [3, 4]
     * }</pre>
     *
     * @return the formatted string representation of the matrix
     */
    public abstract String println();

    /**
     * Checks if the matrix is empty (contains no elements).
     * A matrix is considered empty if either the number of rows or columns is zero,
     * resulting in a total count of zero elements.
     *
     * <p>Note: An empty matrix can have various shapes such as 0×0, 0×n, or m×0.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix empty1 = IntMatrix.of(new int[0][0]);
     * boolean isEmpty1 = empty1.isEmpty(); // Returns true (0×0)
     *
     * IntMatrix empty2 = IntMatrix.of(new int[0][5]);
     * boolean isEmpty2 = empty2.isEmpty(); // Returns true (0×5)
     *
     * IntMatrix notEmpty = IntMatrix.of(new int[][] {{1}});
     * boolean isEmpty3 = notEmpty.isEmpty(); // Returns false (1×1)
     * }</pre>
     *
     * @return {@code true} if the matrix has no elements (count == 0), {@code false} otherwise
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Creates a copy of this matrix.
     * The returned matrix is a completely independent copy with its own underlying array;
     * modifications to one matrix do not affect the other.
     *
     * <p>This method creates new array instances and copies all element values.
     * For large matrices, this operation can be memory and time intensive.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix copy = original.copy();
     * copy.set(0, 0, 10); // Original matrix remains unchanged
     * // original: {{1, 2}, {3, 4}}
     * // copy:     {{10, 2}, {3, 4}}
     * }</pre>
     *
     * @return a new matrix that is a copy of this matrix with the same dimensions and values
     */
    public abstract X copy();

    /**
     * Creates a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows (with all columns) and is completely
     * independent from the original matrix.
     *
     * <p>This is equivalent to calling {@code copy(fromRowIndex, toRowIndex, 0, cols)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * IntMatrix subMatrix = matrix.copy(0, 2); // Contains rows 0 and 1
     * // subMatrix: {{1, 2}, {3, 4}}
     *
     * IntMatrix lastRow = matrix.copy(2, 3); // Contains only row 2
     * // lastRow: {{5, 6}}
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new matrix containing the specified rows with dimensions (toRowIndex - fromRowIndex) × cols
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows, or fromRowIndex &gt; toRowIndex
     */
    public abstract X copy(int fromRowIndex, int toRowIndex);

    /**
     * Creates a copy of a rectangular region from this matrix.
     * The returned matrix contains only the specified rows and columns and is completely
     * independent from the original matrix.
     *
     * <p>This method allows you to extract any rectangular subregion of the matrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntMatrix subMatrix = matrix.copy(0, 2, 1, 3);
     * // subMatrix: {{2, 3}, {5, 6}} (rows 0-1, columns 1-2)
     *
     * IntMatrix centerElement = matrix.copy(1, 2, 1, 2);
     * // centerElement: {{5}} (just the center element)
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a new matrix containing the specified region with dimensions
     *         (toRowIndex - fromRowIndex) × (toColumnIndex - fromColumnIndex)
     * @throws IndexOutOfBoundsException if any index is out of bounds or if fromRowIndex &gt; toRowIndex
     *                                   or fromColumnIndex &gt; toColumnIndex
     */
    public abstract X copy(int fromRowIndex, int toRowIndex, int fromColumnIndex, int toColumnIndex);

    /**
     * Rotates this matrix 90 degrees clockwise.
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the last row of the original matrix reading upward.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>Rotation formula: element at position (i, j) in the original matrix
     * moves to position (j, rows - 1 - i) in the rotated matrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 90° clockwise:
     * // 1 2 3        7 4 1
     * // 4 5 6   =>   8 5 2
     * // 7 8 9        9 6 3
     *
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntMatrix rotated = original.rotate90(); // 3×2 becomes 2×3
     * }</pre>
     *
     * @return a new matrix rotated 90 degrees clockwise with dimensions cols × rows
     */
    public abstract X rotate90();

    /**
     * Rotates this matrix 180 degrees.
     * This is equivalent to flipping both horizontally and vertically, reversing the
     * order of all elements. The resulting matrix has the same dimensions as the original.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>Rotation formula: element at position (i, j) in the original matrix
     * moves to position (rows - 1 - i, cols - 1 - j) in the rotated matrix.</p>
     *
     * <p>This operation is equivalent to calling {@code rotate90().rotate90()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 180°:
     * // 1 2 3        9 8 7
     * // 4 5 6   =>   6 5 4
     * // 7 8 9        3 2 1
     *
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix rotated = original.rotate180(); // Dimensions remain 2×3
     * }</pre>
     *
     * @return a new matrix rotated 180 degrees with the same dimensions (rows × cols)
     */
    public abstract X rotate180();

    /**
     * Rotates this matrix 270 degrees clockwise (or 90 degrees counter-clockwise).
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the first row of the original matrix reading downward.
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>Rotation formula: element at position (i, j) in the original matrix
     * moves to position (cols - 1 - j, i) in the rotated matrix.</p>
     *
     * <p>This operation is equivalent to calling {@code rotate90().rotate90().rotate90()}
     * or {@code transpose().rotate180().transpose()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 270° clockwise:
     * // 1 2 3        3 6 9
     * // 4 5 6   =>   2 5 8
     * // 7 8 9        1 4 7
     *
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntMatrix rotated = original.rotate270(); // 3×3 becomes 3×3
     * }</pre>
     *
     * @return a new matrix rotated 270 degrees clockwise with dimensions cols × rows
     */
    public abstract X rotate270();

    /**
     * Creates the transpose of this matrix by swapping rows and columns.
     * The transpose operation converts each row into a column, so element at position (i, j)
     * in the original matrix appears at position (j, i) in the transposed matrix. The resulting
     * matrix has dimensions swapped (rows × cols becomes cols × rows).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>Transpose formula: element at position (i, j) in the original matrix
     * moves to position (j, i) in the transposed matrix.</p>
     *
     * <p>The transpose is a fundamental matrix operation used in linear algebra,
     * statistics, and many matrix algorithms.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Transposed:
     * // 1 2 3        1 4 7
     * // 4 5 6   =>   2 5 8
     * // 7 8 9        3 6 9
     *
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix transposed = original.transpose(); // 2×3 becomes 3×2
     * }</pre>
     *
     * @return a new matrix that is the transpose of this matrix with dimensions cols × rows
     */
    public abstract X transpose();

    /**
     * Reshapes this matrix to have the specified number of columns.
     * The number of rows is automatically calculated based on the total element count.
     * Elements are taken in row-major order from the original matrix and placed into the
     * new shape. If the total element count is not evenly divisible by the new column count,
     * the last row will be padded with default values (0 for numeric types, false for boolean, null for objects).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>The new row count is calculated as: {@code ceiling(count / newCols)}</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped = matrix.reshape(2); // Becomes [[1, 2], [3, 4], [5, 6]]
     *
     * IntMatrix matrix2 = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped2 = matrix2.reshape(4); // Becomes [[1, 2, 3, 4], [5, 6, 0, 0]]
     * }</pre>
     *
     * @param newCols the number of columns in the reshaped matrix (must be positive)
     * @return a new matrix with the specified number of columns
     * @throws IllegalArgumentException if newCols &lt;= 0
     */
    public X reshape(final int newCols) {
        return reshape((int) (count % newCols == 0 ? count / newCols : count / newCols + 1), newCols);
    }

    /**
     * Reshapes this matrix to have the specified dimensions.
     * Elements are taken in row-major order from the original matrix and placed into the
     * new shape. If the new shape has fewer elements than the original, excess elements are
     * discarded. If the new shape has more elements, the extra positions are filled with
     * default values (0 for numeric types, false for boolean, null for objects).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>This is a fundamental operation for restructuring matrix data without changing
     * the underlying element values (within the bounds of the new shape).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped = matrix.reshape(3, 2); // Becomes [[1, 2], [3, 4], [5, 6]]
     * IntMatrix extended = matrix.reshape(2, 4); // Becomes [[1, 2, 3, 4], [5, 6, 0, 0]]
     * IntMatrix truncated = matrix.reshape(1, 3); // Becomes [[1, 2, 3]] (remaining elements discarded)
     * }</pre>
     *
     * @param newRows the number of rows in the reshaped matrix (must be positive)
     * @param newCols the number of columns in the reshaped matrix (must be positive)
     * @return a new matrix with the specified dimensions (newRows × newCols)
     * @throws IllegalArgumentException if newRows &lt;= 0 or newCols &lt;= 0
     */
    public abstract X reshape(int newRows, int newCols);

    /**
     * Checks if this matrix has the same shape (dimensions) as another matrix.
     * Two matrices have the same shape if they have the same number of rows and columns.
     * The element values are not compared, only the dimensions.
     *
     * <p>This method is useful for validating matrix compatibility before operations
     * that require same-shaped matrices (e.g., element-wise addition or subtraction).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix m2 = IntMatrix.of(new int[][] {{5, 6}, {7, 8}});
     * boolean sameShape = m1.isSameShape(m2); // Returns true (both are 2×2)
     *
     * IntMatrix m3 = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * boolean sameShape2 = m1.isSameShape(m3); // Returns false (2×2 vs 2×3)
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
     * The resulting matrix has dimensions (rows × rowRepeats) × (cols × colRepeats).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>This operation is similar to MATLAB's repelem function. Each element becomes a block,
     * effectively creating a "zoomed in" version of the matrix where each original element
     * occupies multiple positions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    repelem(2, 2):
     * // 1 2          1 1 2 2
     * // 3 4     =>   1 1 2 2
     * //              3 3 4 4
     * //              3 3 4 4
     *
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix repeated = matrix.repelem(2, 2); // 2×2 becomes 4×4
     * }</pre>
     *
     * @param rowRepeats number of times to repeat each element in the row direction (must be &gt;= 1)
     * @param colRepeats number of times to repeat each element in the column direction (must be &gt;= 1)
     * @return a new matrix with repeated elements, dimensions (rows × rowRepeats) × (cols × colRepeats)
     * @throws IllegalArgumentException if rowRepeats &lt; 1 or colRepeats &lt; 1
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repelem.html">MATLAB repelem</a>
     */
    public abstract X repelem(int rowRepeats, int colRepeats);

    /**
     * Repeats the entire matrix the specified number of times in both dimensions.
     * The matrix is tiled rowRepeats times vertically and colRepeats times horizontally.
     * The resulting matrix has dimensions (rows × rowRepeats) × (cols × colRepeats).
     * Creates a new matrix; the original matrix is not modified.
     *
     * <p>This operation is similar to MATLAB's repmat function. The entire matrix pattern
     * is replicated, creating a tiled arrangement.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    repmat(2, 2):
     * // 1 2          1 2 1 2
     * // 3 4     =>   3 4 3 4
     * //              1 2 1 2
     * //              3 4 3 4
     *
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix tiled = matrix.repmat(2, 2); // 2×2 becomes 4×4
     * }</pre>
     *
     * @param rowRepeats number of times to repeat the matrix in the row direction (must be &gt;= 1)
     * @param colRepeats number of times to repeat the matrix in the column direction (must be &gt;= 1)
     * @return a new matrix with the original matrix repeated, dimensions (rows × rowRepeats) × (cols × colRepeats)
     * @throws IllegalArgumentException if rowRepeats &lt; 1 or colRepeats &lt; 1
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repmat.html">MATLAB repmat</a>
     */
    public abstract X repmat(int rowRepeats, int colRepeats);

    /**
     * Flattens the matrix into a one-dimensional list.
     * Elements are taken in row-major order (row by row from left to right).
     * The returned list is a new instance containing all matrix elements.
     *
     * <p>The flattening operation converts a 2D matrix structure into a 1D sequence,
     * which is useful for operations that work on linear data structures.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntList flat = matrix.flatten(); // Returns [1, 2, 3, 4]
     *
     * IntMatrix matrix2 = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntList flat2 = matrix2.flatten(); // Returns [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @return a new list containing all elements in row-major order with size equal to {@code count}
     */
    public abstract PL flatten();

    /**
     * Applies an operation to the flattened view of the matrix and updates the matrix with the result.
     * This method provides access to the underlying array representation in row-major order,
     * executes the operation on it, and the changes are reflected back in the matrix structure.
     *
     * <p>This is useful for operations that are easier to perform on a flat array representation,
     * such as sorting all elements, applying statistical transformations, or batch updates.</p>
     *
     * <p><strong>Note:</strong> The operation modifies the array in place, which affects the matrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{3, 1, 4}, {1, 5, 9}});
     * matrix.flatOp(a -> Arrays.sort(a)); // Sorts all elements
     * // Matrix becomes [[1, 1, 3], [4, 5, 9]] (elements sorted in row-major order)
     *
     * matrix.flatOp(a -> { for (int i = 0; i < a.length; i++) a[i] *= 2; });
     * // Doubles all elements
     * }</pre>
     *
     * @param <E> the type of exception that the operation might throw
     * @param op the operation to apply to the flattened array (receives array type A, not A[])
     * @throws E if the operation throws an exception
     */
    public abstract <E extends Exception> void flatOp(Throwables.Consumer<? super A, E> op) throws E;

    /**
     * Performs the given action for each position in the matrix.
     * The action receives the row and column indices for each element.
     * Elements are processed in row-major order (row by row from left to right).
     * For large matrices, the operation may be automatically parallelized for better performance.
     *
     * <p>This method is useful when you need to access matrix positions without caring about
     * the actual element values, or when the element access logic is handled inside the action.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.forEach((i, j) -> {
     *     System.out.println("Position: (" + i + "," + j + ")");
     * });
     *
     * // Count elements on the main diagonal
     * AtomicInteger diagonalCount = new AtomicInteger(0);
     * matrix.forEach((i, j) -> {
     *     if (i == j) diagonalCount.incrementAndGet();
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param action the action to perform for each position, receives (rowIndex, columnIndex)
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
     * Performs the given action for each position in the specified rectangular region of the matrix.
     * The action receives the row and column indices for each element in the region.
     * Elements are processed in row-major order within the specified region.
     * For large regions, the operation may be automatically parallelized for better performance.
     *
     * <p>This allows selective processing of matrix subregions without creating a copy.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Process only a 2×2 subregion starting at (1,1)
     * matrix.forEach(1, 3, 1, 3, (i, j) -> {
     *     System.out.println("Processing element at (" + i + "," + j + ")");
     * });
     *
     * // Process only the first column
     * matrix.forEach(0, matrix.rows, 0, 1, (i, j) -> {
     *     // Process each element in column 0
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to perform for each position, receives (rowIndex, columnIndex)
     * @throws IndexOutOfBoundsException if any index is out of bounds
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
     * The action receives the row index, column index, and the matrix instance.
     * Elements are processed in row-major order (row by row from left to right).
     * For large matrices, the operation may be automatically parallelized for better performance.
     *
     * <p>This variant is useful when the action needs access to matrix elements or methods,
     * allowing you to read/write values or use matrix operations within the action.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * matrix.forEach((i, j, m) -> {
     *     int value = m.get(i, j);
     *     System.out.println("Value at (" + i + "," + j + ") is " + value);
     * });
     *
     * // Set each element to the sum of its indices
     * matrix.forEach((i, j, m) -> m.set(i, j, i + j));
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param action the action to perform, receiving (rowIndex, columnIndex, matrix)
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
     * Performs the given action for each position in the specified rectangular region, providing the matrix itself.
     * The action receives the row index, column index, and the matrix instance for each position in the region.
     * Elements are processed in row-major order within the specified region.
     * For large regions, the operation may be automatically parallelized for better performance.
     *
     * <p>This combines region-based iteration with matrix access, allowing you to process
     * a subregion while having access to the entire matrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.forEach(1, 3, 1, 3, (i, j, m) -> {
     *     // Process only the 2×2 subregion with access to matrix
     *     int value = m.get(i, j);
     *     System.out.println("Value at (" + i + "," + j + "): " + value);
     * });
     *
     * // Update subregion based on neighboring values
     * matrix.forEach(1, matrix.rows - 1, 1, matrix.cols - 1, (i, j, m) -> {
     *     int avg = (m.get(i-1, j) + m.get(i+1, j) + m.get(i, j-1) + m.get(i, j+1)) / 4;
     *     m.set(i, j, avg);
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @param action the action to perform, receiving (rowIndex, columnIndex, matrix)
     * @throws IndexOutOfBoundsException if any index is out of bounds
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
     * The main diagonal consists of elements where row index equals column index.
     * The matrix must be square (rows == cols) for this operation.
     *
     * <p>LU2RD = Left-Up to Right-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> diagonal = matrix.pointsLU2RD(); // Points: (0,0), (1,1), (2,2)
     * List<Point> points = diagonal.toList(); // [(0,0), (1,1), (2,2)]
     * }</pre>
     *
     * @return a stream of {@link Point} objects representing the main diagonal positions
     * @throws IllegalStateException if the matrix is not square (rows != cols)
     */
    public Stream<Point> pointsLU2RD() {
        checkIfRowAndColumnSizeAreSame();

        //noinspection resource
        return IntStream.range(0, rows).mapToObj(i -> Point.of(i, i));
    }

    /**
     * Returns a stream of points along the anti-diagonal (right-up to left-down).
     * The anti-diagonal consists of elements where row index + column index equals (cols - 1).
     * The matrix must be square (rows == cols) for this operation.
     *
     * <p>RU2LD = Right-Up to Left-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> antiDiagonal = matrix.pointsRU2LD(); // Points: (0,2), (1,1), (2,0)
     * List<Point> points = antiDiagonal.toList(); // [(0,2), (1,1), (2,0)]
     * }</pre>
     *
     * @return a stream of {@link Point} objects representing the anti-diagonal positions
     * @throws IllegalStateException if the matrix is not square (rows != cols)
     */
    public Stream<Point> pointsRU2LD() {
        checkIfRowAndColumnSizeAreSame();

        //noinspection resource
        return IntStream.range(0, rows).mapToObj(i -> Point.of(i, cols - i - 1));
    }

    /**
     * Returns a stream of all points in the matrix in row-major order (horizontal traversal).
     * Points are generated row by row from left to right, top to bottom.
     *
     * <p>H = Horizontal. This is equivalent to calling {@code pointsH(0, rows)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Stream<Point> allPoints = matrix.pointsH();
     * // Points in order: (0,0), (0,1), (1,0), (1,1)
     * allPoints.forEach(p -> System.out.println("Point: " + p));
     * }</pre>
     *
     * @return a stream of all {@link Point} objects in row-major order
     */
    public Stream<Point> pointsH() {
        return pointsH(0, rows);
    }

    /**
     * Returns a stream of points for a specific row in horizontal order (left to right).
     *
     * <p>This is equivalent to calling {@code pointsH(rowIndex, rowIndex + 1)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Stream<Point> row1Points = matrix.pointsH(1); // All points in row 1
     * // For a 3-column matrix: (1,0), (1,1), (1,2)
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @return a stream of {@link Point} objects for all columns in the specified row
     * @throws IndexOutOfBoundsException if rowIndex &lt; 0 or rowIndex &gt;= rows
     */
    public Stream<Point> pointsH(final int rowIndex) {
        return pointsH(rowIndex, rowIndex + 1);
    }

    /**
     * Returns a stream of points for a range of rows in row-major order (horizontal traversal).
     * Points are generated row by row from left to right for the specified row range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Get points from rows 1 and 2 (indices 1 and 2, not including 3)
     * Stream<Point> points = matrix.pointsH(1, 3);
     * // For a 2-column matrix: (1,0), (1,1), (2,0), (2,1)
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of {@link Point} objects in the specified row range, in row-major order
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows, or fromRowIndex &gt; toRowIndex
     */
    @SuppressWarnings("resource")
    public Stream<Point> pointsH(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rows);

        return IntStream.range(fromRowIndex, toRowIndex)
                .flatMapToObj(rowIndex -> IntStream.range(0, cols).mapToObj(columnIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of all points in the matrix in column-major order (vertical traversal).
     * Points are generated column by column from top to bottom, left to right.
     *
     * <p>V = Vertical. This is equivalent to calling {@code pointsV(0, cols)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Stream<Point> columnOrder = matrix.pointsV();
     * // Points in order: (0,0), (1,0), (0,1), (1,1)
     * // For a 2×3 matrix, order would be: (0,0), (1,0), (0,1), (1,1), (0,2), (1,2)
     * }</pre>
     *
     * @return a stream of all {@link Point} objects in column-major order
     */
    public Stream<Point> pointsV() {
        return pointsV(0, cols);
    }

    /**
     * Returns a stream of points for a specific column in vertical order (top to bottom).
     *
     * <p>This is equivalent to calling {@code pointsV(columnIndex, columnIndex + 1)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Stream<Point> col2Points = matrix.pointsV(2); // All points in column 2
     * // For a 3-row matrix: (0,2), (1,2), (2,2)
     * }</pre>
     *
     * @param columnIndex the column index (0-based)
     * @return a stream of {@link Point} objects for all rows in the specified column
     * @throws IndexOutOfBoundsException if columnIndex &lt; 0 or columnIndex &gt;= cols
     */
    public Stream<Point> pointsV(final int columnIndex) {
        return pointsV(columnIndex, columnIndex + 1);
    }

    /**
     * Returns a stream of points for a range of columns in column-major order (vertical traversal).
     * Points are generated column by column from top to bottom for the specified column range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Get points from columns 1 through 3 (indices 1, 2, 3, not including 4)
     * Stream<Point> points = matrix.pointsV(1, 4);
     * // For a 2-row matrix: (0,1), (1,1), (0,2), (1,2), (0,3), (1,3)
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of {@link Point} objects in the specified column range, in column-major order
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; cols, or fromColumnIndex &gt; toColumnIndex
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
     * <p>R = Row. This is equivalent to calling {@code pointsR(0, rows)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.pointsR().forEach(rowStream -> {
     *     rowStream.forEach(point -> System.out.println("Point: " + point));
     * });
     *
     * // Collect each row's points separately
     * List<List<Point>> rowsOfPoints = matrix.pointsR()
     *     .map(Stream::toList)
     *     .toList();
     * }</pre>
     *
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one row
     */
    public Stream<Stream<Point>> pointsR() {
        return pointsR(0, rows);
    }

    /**
     * Returns a stream of streams for a range of rows, where each inner stream represents a row of points.
     * This allows for selective row-by-row processing of matrix positions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Process rows 1 and 2 separately
     * matrix.pointsR(1, 3).forEach(rowStream -> {
     *     List<Point> rowPoints = rowStream.toList();
     *     // Process each row's points
     * });
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one row
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows, or fromRowIndex &gt; toRowIndex
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
     * <p>C = Column. This is equivalent to calling {@code pointsC(0, cols)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.pointsC().forEach(colStream -> {
     *     colStream.forEach(point -> System.out.println("Point: " + point));
     * });
     *
     * // Collect each column's points separately
     * List<List<Point>> columnsOfPoints = matrix.pointsC()
     *     .map(Stream::toList)
     *     .toList();
     * }</pre>
     *
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one column
     */
    public Stream<Stream<Point>> pointsC() {
        return pointsC(0, cols);
    }

    /**
     * Returns a stream of streams for a range of columns, where each inner stream represents a column of points.
     * This allows for selective column-by-column processing of matrix positions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Process columns 2 through 4 separately
     * matrix.pointsC(2, 5).forEach(colStream -> {
     *     List<Point> colPoints = colStream.toList();
     *     // Process each column's points
     * });
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one column
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; cols, or fromColumnIndex &gt; toColumnIndex
     */
    @SuppressWarnings("resource")
    public Stream<Stream<Point>> pointsC(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, cols);

        return IntStream.range(fromColumnIndex, toColumnIndex)
                .mapToObj(columnIndex -> IntStream.range(0, rows).mapToObj(rowIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of elements along the main diagonal (left-up to right-down).
     * The main diagonal consists of elements where row index equals column index.
     * The matrix must be square (rows == cols) for this operation.
     *
     * <p>LU2RD = Left-Up to Right-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntStream diagonal = matrix.streamLU2RD(); // Stream of: 1, 5, 9
     * int sum = diagonal.sum(); // 15
     * }</pre>
     *
     * @return a stream of diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != cols)
     */
    public abstract ES streamLU2RD();

    /**
     * Returns a stream of elements along the anti-diagonal (right-up to left-down).
     * The anti-diagonal consists of elements where row index + column index equals (cols - 1).
     * The matrix must be square (rows == cols) for this operation.
     *
     * <p>RU2LD = Right-Up to Left-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntStream antiDiagonal = matrix.streamRU2LD(); // Stream of: 3, 5, 7
     * int sum = antiDiagonal.sum(); // 15
     * }</pre>
     *
     * @return a stream of anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rows != cols)
     */
    public abstract ES streamRU2LD();

    /**
     * Returns a stream of all elements in row-major order (horizontal traversal).
     * Elements are streamed row by row from left to right, top to bottom.
     *
     * <p>H = Horizontal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntStream elements = matrix.streamH(); // Stream of: 1, 2, 3, 4
     * int sum = elements.sum(); // 10
     * }</pre>
     *
     * @return a stream of all elements in row-major order
     */
    public abstract ES streamH();

    /**
     * Returns a stream of elements from a specific row.
     * Elements are streamed from left to right within the row.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream row1 = matrix.streamH(1); // Stream of: 4, 5, 6
     * int max = row1.max().orElse(0); // 6
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @return a stream of elements in the specified row
     * @throws IndexOutOfBoundsException if rowIndex &lt; 0 or rowIndex &gt;= rows
     */
    public abstract ES streamH(final int rowIndex);

    /**
     * Returns a stream of elements from a range of rows in row-major order.
     * Elements are streamed row by row from left to right for the specified row range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * IntStream rows1and2 = matrix.streamH(1, 3); // Stream of: 3, 4, 5, 6
     * long count = rows1and2.count(); // 4
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of elements in the specified row range
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows, or fromRowIndex &gt; toRowIndex
     */
    public abstract ES streamH(final int fromRowIndex, final int toRowIndex);

    /**
     * Returns a stream of all elements in column-major order (vertical traversal).
     * Elements are streamed column by column from top to bottom, left to right.
     *
     * <p>V = Vertical.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntStream elements = matrix.streamV(); // Stream of: 1, 3, 2, 4
     * int sum = elements.sum(); // 10
     * }</pre>
     *
     * @return a stream of all elements in column-major order
     */
    public abstract ES streamV();

    /**
     * Returns a stream of elements from a specific column.
     * Elements are streamed from top to bottom within the column.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream col1 = matrix.streamV(1); // Stream of: 2, 5
     * int min = col1.min().orElse(0); // 2
     * }</pre>
     *
     * @param columnIndex the column index (0-based)
     * @return a stream of elements in the specified column
     * @throws IndexOutOfBoundsException if columnIndex &lt; 0 or columnIndex &gt;= cols
     */
    public abstract ES streamV(final int columnIndex);

    /**
     * Returns a stream of elements from a range of columns in column-major order.
     * Elements are streamed column by column from top to bottom for the specified column range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream cols1and2 = matrix.streamV(1, 3); // Stream of: 2, 5, 3, 6
     * double avg = cols1and2.average().orElse(0); // 4.0
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of elements in the specified column range
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; cols, or fromColumnIndex &gt; toColumnIndex
     */
    public abstract ES streamV(final int fromColumnIndex, final int toColumnIndex);

    /**
     * Returns a stream of row streams.
     * Each element in the outer stream is a stream representing one row of the matrix.
     * This allows for per-row processing using stream operations.
     *
     * <p>R = Row.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * matrix.streamR().forEach(rowStream -> {
     *     int sum = rowStream.sum(); // Sum each row
     *     System.out.println("Row sum: " + sum);
     * });
     * // Output: Row sum: 6
     * //         Row sum: 15
     * }</pre>
     *
     * @return a stream of row streams
     */
    public abstract RS streamR();

    /**
     * Returns a stream of row streams for a range of rows.
     * Each element in the outer stream is a stream representing one row of the matrix
     * within the specified range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Process only rows 1 and 2
     * matrix.streamR(1, 3).forEach(rowStream -> {
     *     int max = rowStream.max().orElse(0);
     *     System.out.println("Row max: " + max);
     * });
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of row streams for the specified range
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rows, or fromRowIndex &gt; toRowIndex
     */
    public abstract RS streamR(final int fromRowIndex, final int toRowIndex);

    /**
     * Returns a stream of column streams.
     * Each element in the outer stream is a stream representing one column of the matrix.
     * This allows for per-column processing using stream operations.
     *
     * <p>C = Column.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
     * matrix.streamC().forEach(colStream -> {
     *     double avg = colStream.average().orElse(0);
     *     System.out.println("Column average: " + avg);
     * });
     * // Output: Column average: 2.0
     * //         Column average: 3.0
     * }</pre>
     *
     * @return a stream of column streams
     */
    public abstract RS streamC();

    /**
     * Returns a stream of column streams for a range of columns.
     * Each element in the outer stream is a stream representing one column of the matrix
     * within the specified range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Process columns 2 through 4
     * matrix.streamC(2, 5).forEach(colStream -> {
     *     int sum = colStream.sum();
     *     System.out.println("Column sum: " + sum);
     * });
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of column streams for the specified range
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; cols, or fromColumnIndex &gt; toColumnIndex
     */
    public abstract RS streamC(final int fromColumnIndex, final int toColumnIndex);

    /**
     * Executes the given action with this matrix as the parameter.
     * This method enables fluent-style operations where the matrix needs to be passed to a consumer.
     * It follows the functional programming pattern of accepting a consumer function.
     *
     * <p>This method is useful for performing operations on the matrix in a fluent chain
     * or for passing the matrix to utility methods that expect a consumer.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * matrix.accept(m -> {
     *     System.out.println("Matrix dimensions: " + m.rows + "x" + m.cols);
     *     m.println();
     * });
     *
     * // Use in a fluent chain
     * IntMatrix result = matrix.copy()
     *     .accept(m -> System.out.println("Processing..."))
     *     .transpose();
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param action the consumer action to perform on this matrix
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super X, E> action) throws E {
        action.accept((X) this);
    }

    /**
     * Applies the given function to this matrix and returns the result.
     * This method enables fluent-style transformations where the matrix needs to be passed to a function.
     * It follows the functional programming pattern of applying a function and returning its result.
     *
     * <p>This method is useful for extracting values from the matrix or transforming it
     * into a different type while maintaining a fluent interface.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double determinant = matrix.apply(MatrixUtils::calculateDeterminant);
     * String info = matrix.apply(m -> "Matrix " + m.rows + "x" + m.cols);
     *
     * // Transform matrix into a different representation
     * List<Integer> allValues = matrix.apply(m -> m.flatten().toList());
     * }</pre>
     *
     * @param <R> the result type of the function
     * @param <E> the type of exception that the function might throw
     * @param action the function to apply to this matrix
     * @return the result of applying the function to this matrix
     * @throws E if the function throws an exception
     */
    public <R, E extends Exception> R apply(final Throwables.Function<? super X, R, E> action) throws E {
        return action.apply((X) this);
    }

    /**
     * Returns the length of the given array.
     * This abstract method must be implemented by concrete subclasses to handle
     * their specific array types (primitive arrays like int[], double[], or object arrays).
     *
     * <p>Implementors should return the appropriate length for their array type:
     * <ul>
     *   <li>For primitive arrays: use the array's length field (e.g., {@code ((int[])a).length})</li>
     *   <li>For object arrays: use {@link java.lang.reflect.Array#getLength(Object)}</li>
     * </ul>
     * </p>
     *
     * @param a the array to measure
     * @return the length of the array
     */
    protected abstract int length(@SuppressWarnings("hiding") A a);

    /**
     * Validates that this matrix has the same shape (dimensions) as the given matrix.
     * This is a helper method used internally to enforce shape compatibility before
     * operations that require matrices of the same dimensions.
     *
     * @param x the matrix to compare with
     * @throws IllegalArgumentException if the matrices have different shapes (different rows or cols)
     */
    protected void checkSameShape(final X x) {
        N.checkArgument(this.isSameShape(x), "Must be same shape");
    }

    /**
     * Validates that this matrix is square (has the same number of rows and columns).
     * This is a helper method used internally to enforce the square matrix requirement
     * for operations like diagonal access.
     *
     * <p>Square matrices are required for operations such as:
     * <ul>
     *   <li>{@link #streamLU2RD()} - main diagonal stream</li>
     *   <li>{@link #streamRU2LD()} - anti-diagonal stream</li>
     *   <li>{@link #pointsLU2RD()} - main diagonal points</li>
     *   <li>{@link #pointsRU2LD()} - anti-diagonal points</li>
     * </ul>
     * </p>
     *
     * @throws IllegalStateException if the matrix is not square (rows != cols)
     */
    protected void checkIfRowAndColumnSizeAreSame() {
        N.checkState(rows == cols, "'rows' and 'cols' must be same to get diagonals: rows=%s, cols=%s", rows, cols);
    }

}