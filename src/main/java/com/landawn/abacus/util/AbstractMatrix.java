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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.landawn.abacus.annotation.SuppressFBWarnings;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.Stream;

/**
 * Base class for the matrix types in this package.
 *
 * <p>This class stores a rectangular two-dimensional backing array and implements the operations shared by
 * primitive and object matrices, including shape validation, coordinate navigation, row and column access,
 * reshaping, and stream-oriented traversal.</p>
 *
 * <p>Some methods intentionally expose live storage for performance-sensitive code, notably
 * {@link #backingArray()}, {@link #rowView(int)}, and {@link #applyOnFlattened(Throwables.Consumer)}.
 * Prefer {@link #copy()}, {@link #toArray()}, or {@link #rowCopy(int)} when callers must not observe or
 * mutate the backing array.</p>
 *
 * @param <A> the array type used for internal row storage (for example {@code int[]}, {@code double[]}, or {@code Object[]})
 * @param <PL> the flattened-view list type
 * @param <ES> the element stream type
 * @param <RS> the row or column stream type
 * @param <X> the concrete matrix type used for fluent return values
 */
public abstract sealed class AbstractMatrix<A, PL, ES, RS, X extends AbstractMatrix<A, PL, ES, RS, X>>
        permits BooleanMatrix, CharMatrix, ByteMatrix, ShortMatrix, DoubleMatrix, FloatMatrix, IntMatrix, LongMatrix, Matrix {

    protected static final String ARRAY_PRINT_SEPARATOR = Arrays.ARRAY_PRINT_SEPARATOR;

    static final char CHAR_0 = (char) 0;

    static final byte BYTE_0 = (byte) 0;

    static final byte BYTE_1 = (byte) 1;

    static final short SHORT_0 = (short) 0;

    // ==================== Standardized Exception Message Constants ====================
    // These constants ensure consistent exception messages across all matrix classes.

    /** Exception message format for row index out of bounds. Arguments: rowIndex, rowCount */
    protected static final String MSG_ROW_INDEX_OUT_OF_BOUNDS = "Row index {} is out of bounds. Valid range is [0, {})";

    /** Exception message format for column index out of bounds. Arguments: columnIndex, columnCount */
    protected static final String MSG_COLUMN_INDEX_OUT_OF_BOUNDS = "Column index {} is out of bounds. Valid range is [0, {})";

    /** Exception message format for row length mismatch. Arguments: expected, actual */
    protected static final String MSG_ROW_LENGTH_MISMATCH = "Row length mismatch: expected {} columns but got {}";

    /** Exception message format for column length mismatch. Arguments: expected, actual */
    protected static final String MSG_COLUMN_LENGTH_MISMATCH = "Column length mismatch: expected {} rows but got {}";

    /** Exception message format for diagonal array length mismatch. Arguments: expected, actual */
    protected static final String MSG_DIAGONAL_LENGTH_MISMATCH = "Diagonal array length must equal matrix size: expected {} but got {}";

    /** Exception message format for non-square matrix error. Arguments: rowCount, columnCount */
    protected static final String MSG_MATRIX_NOT_SQUARE = "Matrix must be square: current dimensions are {} rows x {} columns";

    /** Exception message format for shape mismatch between two matrices. Arguments: rows1, cols1, rows2, cols2 */
    protected static final String MSG_SHAPE_MISMATCH = "Matrix shape mismatch: this matrix is {}x{} but other is {}x{}";

    /** Exception message format for non-rectangular matrix. Arguments: firstRowLength, currentRowIndex, currentRowLength */
    protected static final String MSG_NOT_RECTANGULAR = "Matrix must be rectangular: row 0 has {} columns, but row {} has {} columns";

    /** Exception message format for stackVertically column count mismatch. Arguments: thisColumnCount, otherColumnCount */
    protected static final String MSG_VSTACK_COLUMN_MISMATCH = "Column count mismatch for stackVertically: this matrix has {} columns but other has {}";

    /** Exception message format for stackHorizontally row count mismatch. Arguments: thisRowCount, otherRowCount */
    protected static final String MSG_HSTACK_ROW_MISMATCH = "Row count mismatch for stackHorizontally: this matrix has {} rows but other has {}";

    /** Exception message format for negative dimension. Arguments: paramName, value */
    protected static final String MSG_NEGATIVE_DIMENSION = "{} cannot be negative: {}";

    /** Exception message format for non-positive repeats. Arguments: rowRepeats, columnRepeats */
    protected static final String MSG_REPEATS_NOT_POSITIVE = "rowRepeats and columnRepeats must be positive: rowRepeats={}, columnRepeats={}";

    /**
     * Exception message format for matrix shapes that cannot be represented by this row-array-backed implementation.
     * Arguments: rowCount, columnCount.
     */
    protected static final String MSG_UNREPRESENTABLE_SHAPE = "Matrix shape {}x{} is not representable: zero rows require zero columns";

    // ==================== End Exception Message Constants ====================

    /**
     * The number of rows in this matrix.
     * This value is immutable after matrix creation.
     */
    final int rowCount;

    /**
     * The number of columns in this matrix.
     * This value is immutable after matrix creation.
     */
    final int columnCount;

    /**
     * The total number of elements in this matrix (rows × columns).
     * This value is cached for performance and is immutable after matrix creation.
     */
    final long elementCount;

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
        N.checkArgNotNull(a, "Matrix array cannot be null");

        this.a = a;
        rowCount = a.length;

        if (rowCount > 0) {
            N.checkArgument(a[0] != null, "Row 0 cannot be null");
        }

        columnCount = rowCount == 0 ? 0 : length(a[0]);

        if (a.length > 1) {
            for (int i = 1, len = a.length; i < len; i++) {
                N.checkArgument(a[i] != null, "Row {} cannot be null", i);
                if (length(a[i]) != columnCount) {
                    throw new IllegalArgumentException(formatMsg(MSG_NOT_RECTANGULAR, columnCount, i, length(a[i])));
                }
            }
        }

        elementCount = (long) columnCount * rowCount;
    }

    /**
     * Converts a non-negative element count to an {@code int} array length with overflow protection.
     * This utility method ensures that the element count fits within an {@code int} range before
     * converting it, guarding against overflow when materializing streams or arrays from large matrices.
     *
     * @param count the number of remaining elements (must be non-negative and at most {@code Integer.MAX_VALUE})
     * @return the count cast to an {@code int} array length
     * @throws IllegalStateException if count is negative or exceeds {@code Integer.MAX_VALUE}
     */
    protected static int toArrayLength(final long count) {
        if (count < 0 || count > Integer.MAX_VALUE) {
            throw new IllegalStateException("Matrix stream too large to convert to array: " + count + " elements");
        }

        return (int) count;
    }

    /**
     * Validates that the specified shape is representable by this matrix implementation.
     * Because dimensions are encoded by row arrays, a matrix with zero rows can only have zero columns.
     *
     * @param rowCount the row count
     * @param columnCount the column count
     * @throws IllegalArgumentException if the shape is not representable
     */
    protected static void checkRepresentableShape(final int rowCount, final int columnCount) {
        N.checkArgument(rowCount > 0 || columnCount == 0, MSG_UNREPRESENTABLE_SHAPE, rowCount, columnCount);
    }

    /**
     * Formats matrix error message templates that use "{}" placeholders.
     */
    protected static String formatMsg(final String template, final Object... args) {
        if (template == null || args == null || args.length == 0) {
            return template;
        }

        final StringBuilder sb = new StringBuilder(template.length() + args.length * 8);
        int fromIndex = 0;
        int argIndex = 0;
        int placeholderIndex = 0;

        while (argIndex < args.length && (placeholderIndex = template.indexOf("{}", fromIndex)) >= 0) {
            sb.append(template, fromIndex, placeholderIndex);
            sb.append(args[argIndex++]);
            fromIndex = placeholderIndex + 2;
        }

        sb.append(template, fromIndex, template.length());

        return sb.toString();
    }

    /**
     * Resolves the most specific assignable common type for the two input types.
     *
     * <p>This search considers both super classes and interfaces. If no better common type
     * can be identified, {@link Object} is returned.</p>
     */
    protected static Class<?> resolveCommonAssignableType(final Class<?> left, final Class<?> right) {
        if (left == null) {
            return right == null ? Object.class : right;
        }

        if (right == null) {
            return left;
        }

        if (left.isAssignableFrom(right)) {
            return left;
        }

        if (right.isAssignableFrom(left)) {
            return right;
        }

        final Map<Class<?>, Integer> leftDistances = collectTypeDistances(left);
        final Map<Class<?>, Integer> rightDistances = collectTypeDistances(right);
        Class<?> best = Object.class;
        int bestDistance = Integer.MAX_VALUE;
        int bestPenalty = Integer.MAX_VALUE;
        int bestMethodCount = Integer.MIN_VALUE;

        for (final Map.Entry<Class<?>, Integer> entry : leftDistances.entrySet()) {
            final Class<?> candidate = entry.getKey();
            final Integer rightDistance = rightDistances.get(candidate);

            if (rightDistance == null || !candidate.isAssignableFrom(left) || !candidate.isAssignableFrom(right)) {
                continue;
            }

            final int totalDistance = entry.getValue() + rightDistance;
            final int typePenalty = commonTypePenalty(candidate);
            final int methodCount = candidate.getMethods().length;

            if (totalDistance < bestDistance || (totalDistance == bestDistance && typePenalty < bestPenalty)
                    || (totalDistance == bestDistance && typePenalty == bestPenalty && methodCount > bestMethodCount)
                    || (totalDistance == bestDistance && typePenalty == bestPenalty && methodCount == bestMethodCount && best.isAssignableFrom(candidate))) {
                best = candidate;
                bestDistance = totalDistance;
                bestPenalty = typePenalty;
                bestMethodCount = methodCount;
            }
        }

        return best;
    }

    private static Map<Class<?>, Integer> collectTypeDistances(final Class<?> startType) {
        final Map<Class<?>, Integer> distances = new LinkedHashMap<>();
        final Deque<Class<?>> queue = new ArrayDeque<>();
        distances.put(startType, 0);
        queue.add(startType);

        while (!queue.isEmpty()) {
            final Class<?> current = queue.removeFirst();
            final int nextDistance = distances.get(current) + 1;

            final Class<?> superClass = current.getSuperclass();

            if (superClass != null && distances.putIfAbsent(superClass, nextDistance) == null) {
                queue.addLast(superClass);
            }

            for (final Class<?> intf : current.getInterfaces()) {
                if (distances.putIfAbsent(intf, nextDistance) == null) {
                    queue.addLast(intf);
                }
            }
        }

        return distances;
    }

    private static int commonTypePenalty(final Class<?> type) {
        if (type == Object.class) {
            return 3;
        }

        if (type.isInterface()) {
            return type.getMethods().length == 0 ? 2 : 1;
        }

        return 0;
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
     * Class<?> type = intMatrix.componentType();   // Returns int.class
     *
     * Matrix<String> stringMatrix = Matrix.of(new String[][] {{"a", "b"}, {"c", "d"}});
     * Class<?> strType = stringMatrix.componentType();   // Returns String.class
     * }</pre>
     *
     * @return the Class object representing the component type of matrix elements
     */
    public abstract Class<?> componentType();

    /**
     * Returns the underlying two-dimensional array of this matrix.
     * This method exposes the internal array representation for performance reasons and should be used with caution
     * as modifications to the returned array will directly affect the matrix.
     *
     * <p><strong>Unsafe API boundary:</strong> This method returns the actual internal array, not a copy.
     * Any changes made to the returned array will be reflected in the matrix.
     * If you need an independent copy, use {@link #copy()} instead.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * int[][] array = matrix.backingArray();
     * array[0][0] = 10;  // This WILL modify the matrix!
     * // matrix now contains {{10, 2}, {3, 4}}
     * }</pre>
     *
     * @return the underlying two-dimensional array (not a copy)
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public A[] backingArray() {
        return a;
    }

    /**
     * Returns the specified row as a direct view backed by internal storage.
     * Changes to the returned array will modify this matrix.
     *
     * <p><strong>Unsafe API boundary:</strong> the returned row is a mutable alias to internal storage. Prefer
     * {@link #rowCopy(int)} unless you intentionally need to mutate the matrix through the row view.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * int[] row0 = matrix.rowView(0);  // Returns [1, 2, 3] (direct reference)
     * row0[0] = 99;                // Also changes matrix element at (0, 0) to 99
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return the specified row array (direct reference to internal storage)
     * @throws IllegalArgumentException if rowIndex is out of bounds
     */
    public abstract A rowView(int rowIndex) throws IllegalArgumentException;

    /**
     * Returns a defensive copy of the specified row.
     * Changes to the returned array do not affect this matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * int[] rowCopy = matrix.rowCopy(0);  // Returns [1, 2, 3] (independent copy)
     * rowCopy[0] = 99;                    // Does NOT affect the original matrix
     * }</pre>
     *
     * @param rowIndex the index of the row to retrieve (0-based)
     * @return a new array containing the values from the specified row
     * @throws IllegalArgumentException if rowIndex is out of bounds
     */
    public abstract A rowCopy(int rowIndex) throws IllegalArgumentException;

    /**
     * Returns a defensive copy of the specified column.
     * Changes to the returned array do not affect this matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * int[] colCopy = matrix.columnCopy(1);  // Returns [2, 5] (independent copy)
     * colCopy[0] = 99;                       // Does NOT affect the original matrix
     * }</pre>
     *
     * @param columnIndex the index of the column to retrieve (0-based)
     * @return a new array containing the values from the specified column
     * @throws IllegalArgumentException if columnIndex is out of bounds
     */
    public abstract A columnCopy(int columnIndex) throws IllegalArgumentException;

    /**
     * Returns the number of rows in this matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * int rows = matrix.rowCount();   // Returns 2
     * }</pre>
     *
     * @return the number of rows
     */
    public int rowCount() {
        return rowCount;
    }

    /**
     * Returns the number of columns in this matrix.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * int cols = matrix.columnCount();   // Returns 3
     * }</pre>
     *
     * @return the number of columns
     */
    public int columnCount() {
        return columnCount;
    }

    /**
     * Returns the total number of elements in this matrix (rows x columns).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * long count = matrix.elementCount();   // Returns 6
     * }</pre>
     *
     * @return the total number of elements
     */
    public long elementCount() {
        return elementCount;
    }

    /**
     * Returns {@code true} if this matrix is empty (contains no elements).
     * A matrix is considered empty if either the number of rows or columns is zero,
     * resulting in a total count of zero elements.
     *
     * <p>Note: An empty matrix has zero rows (and therefore zero elements).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix empty = IntMatrix.of(new int[0][0]);
     * boolean result1 = empty.isEmpty();   // Returns true (0x0)
     *
     * IntMatrix notEmpty = IntMatrix.of(new int[][] {{1}});
     * boolean result2 = notEmpty.isEmpty();   // Returns false (1x1)
     * }</pre>
     *
     * @return {@code true} if the matrix has no elements (count == 0), {@code false} otherwise
     */
    public boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     * Prints this matrix to standard output in a formatted, human-readable manner and returns the output string.
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
     * String output = matrix.println();
     * // Prints to console:
     * // [1, 2]
     * // [3, 4]
     * // And returns the same string
     * }</pre>
     *
     * @return the formatted string representation of the matrix that was printed to standard output
     */
    public abstract String println();

    /**
     * Returns a copy of this matrix.
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
     * copy.set(0, 0, 10);   // Original matrix remains unchanged
     * // original: {{1, 2}, {3, 4}}
     * // copy:     {{10, 2}, {3, 4}}
     * }</pre>
     *
     * @return a new matrix that is a copy of this matrix with the same dimensions and values
     */
    public abstract X copy();

    /**
     * Returns a copy of a row range from this matrix.
     * The returned matrix contains only the specified rows (with all columns) and is completely
     * independent from the original matrix.
     *
     * <p>This is equivalent to calling {@code copy(fromRowIndex, toRowIndex, 0, columnCount)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * IntMatrix subMatrix = matrix.copy(0, 2);   // Contains rows 0 and 1
     * // subMatrix: {{1, 2}, {3, 4}}
     *
     * IntMatrix lastRow = matrix.copy(2, 3);   // Contains only row 2
     * // lastRow: {{5, 6}}
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a new matrix containing the specified rows with dimensions (toRowIndex - fromRowIndex) × columnCount
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    public abstract X copy(int fromRowIndex, int toRowIndex);

    /**
     * Returns a copy of a rectangular region from this matrix.
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
     * @throws IndexOutOfBoundsException if any index is out of bounds, fromRowIndex &gt; toRowIndex, or fromColumnIndex &gt; toColumnIndex
     */
    public abstract X copy(int fromRowIndex, int toRowIndex, int fromColumnIndex, int toColumnIndex);

    /**
     * Returns a new matrix that is this matrix rotated 90 degrees clockwise.
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the last row of the original matrix reading upward.
     * The original matrix is not modified.
     *
     * <p>Rotation formula: element at position (i, j) in the original matrix
     * moves to position (j, rowCount - 1 - i) in the rotated matrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 90 degrees clockwise:
     * // 1 2 3        7 4 1
     * // 4 5 6   =>   8 5 2
     * // 7 8 9        9 6 3
     *
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntMatrix rotated = original.rotate90();   // 3x3 remains 3x3
     * }</pre>
     *
     * @return a new matrix that is this matrix rotated 90 degrees clockwise, with dimensions columnCount x rowCount
     */
    public abstract X rotate90();

    /**
     * Returns a new matrix that is this matrix rotated 180 degrees clockwise.
     * This is equivalent to flipping both horizontally and vertically, reversing the
     * order of all elements. The resulting matrix has the same dimensions as the original.
     * The original matrix is not modified.
     *
     * <p>Rotation formula: element at position (i, j) in the original matrix
     * moves to position (rowCount - 1 - i, columnCount - 1 - j) in the rotated matrix.</p>
     *
     * <p>This operation is equivalent to calling {@code rotate90().rotate90()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 180 degrees:
     * // 1 2 3        9 8 7
     * // 4 5 6   =>   6 5 4
     * // 7 8 9        3 2 1
     *
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix rotated = original.rotate180();   // Dimensions remain 2x3
     * }</pre>
     *
     * @return a new matrix that is this matrix rotated 180 degrees clockwise, with the same dimensions (rowCount x columnCount)
     */
    public abstract X rotate180();

    /**
     * Returns a new matrix that is this matrix rotated 270 degrees clockwise (or equivalently, 90 degrees counter-clockwise).
     * The resulting matrix has dimensions swapped (rows become columns), with the first
     * column of the result being the first row of the original matrix reading downward.
     * The original matrix is not modified.
     *
     * <p>Rotation formula: element at position (i, j) in the original matrix
     * moves to position (columnCount - 1 - j, i) in the rotated matrix.</p>
     *
     * <p>This operation is equivalent to calling {@code rotate90().rotate90().rotate90()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    Rotated 270 degrees clockwise:
     * // 1 2 3        3 6 9
     * // 4 5 6   =>   2 5 8
     * // 7 8 9        1 4 7
     *
     * IntMatrix original = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntMatrix rotated = original.rotate270();   // 3x3 becomes 3x3
     * }</pre>
     *
     * @return a new matrix that is this matrix rotated 270 degrees clockwise, with dimensions columnCount x rowCount
     */
    public abstract X rotate270();

    /**
     * Returns a new matrix that is the transpose of this matrix.
     * The transpose operation swaps rows and columns, so element at position (i, j)
     * in the original matrix appears at position (j, i) in the transposed matrix. The resulting
     * matrix has dimensions swapped (rowCount x columnCount becomes columnCount x rowCount).
     * The original matrix is not modified.
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
     * IntMatrix transposed = original.transpose();   // 2x3 becomes 3x2
     * }</pre>
     *
     * @return a new matrix that is the transpose of this matrix, with dimensions columnCount x rowCount
     */
    public abstract X transpose();

    /**
     * Returns a new matrix with the elements of this matrix rearranged into the specified number of columns.
     * The number of rows is automatically calculated based on the total element count.
     * Elements are taken in row-major order from the original matrix and placed into the
     * new shape. If the total element count is not evenly divisible by the new column count,
     * the last row will be padded with default values (0 for numeric types, false for boolean, null for objects).
     * The original matrix is not modified.
     *
     * <p>The new row count is calculated as: {@code ceiling(elementCount / newColumnCount)}</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped = matrix.reshape(2);   // Becomes [[1, 2], [3, 4], [5, 6]]
     *
     * IntMatrix matrix2 = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped2 = matrix2.reshape(4);   // Becomes [[1, 2, 3, 4], [5, 6, 0, 0]]
     * }</pre>
     *
     * @param newColumnCount the number of columns in the reshaped matrix (must be positive)
     * @return a new matrix with the specified number of columns
     * @throws IllegalArgumentException if newColumnCount &lt;= 0
     */
    public X reshape(final int newColumnCount) {
        N.checkArgument(newColumnCount > 0, "newColumnCount must be positive, but got: {}", newColumnCount);

        final long newRowCount = elementCount % newColumnCount == 0 ? elementCount / newColumnCount : elementCount / newColumnCount + 1;

        N.checkArgument(newRowCount <= Integer.MAX_VALUE, "Reshaped row count overflow: ceil({} / {}) = {} exceeds Integer.MAX_VALUE", elementCount,
                newColumnCount, newRowCount);

        checkRepresentableShape((int) newRowCount, newColumnCount);

        return reshape((int) newRowCount, newColumnCount);
    }

    /**
     * Returns a new matrix with the elements of this matrix rearranged into the specified dimensions.
     * Elements are taken in row-major order from the original matrix and placed into the
     * new shape. The new shape must have at least as many total elements as the original
     * ({@code newRowCount * newColumnCount >= elementCount()}).
     * If the new shape has more elements, the extra positions are filled with
     * default values (0 for numeric types, false for boolean, null for objects).
     * The original matrix is not modified.
     *
     * <p>This is a fundamental operation for restructuring matrix data without changing
     * the underlying element values.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntMatrix reshaped = matrix.reshape(3, 2);    // Becomes [[1, 2], [3, 4], [5, 6]]
     * IntMatrix extended = matrix.reshape(2, 4);    // Becomes [[1, 2, 3, 4], [5, 6, 0, 0]]
     * }</pre>
     *
     * @param newRowCount the number of rows in the reshaped matrix; must be non-negative
     * @param newColumnCount the number of columns in the reshaped matrix; must be non-negative
     * @return a new matrix with the specified dimensions (newRowCount × newColumnCount)
     * @throws IllegalArgumentException if newRowCount &lt; 0 or newColumnCount &lt; 0, or if the new shape is too small to hold all elements
     */
    public abstract X reshape(int newRowCount, int newColumnCount);

    /**
     * Returns {@code true} if this matrix has the same shape (dimensions) as the specified matrix.
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
     * boolean sameShape = m1.isSameShape(m2);   // Returns true (both are 2×2)
     *
     * IntMatrix m3 = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * boolean sameShape2 = m1.isSameShape(m3);   // Returns false (2×2 vs 2×3)
     * }</pre>
     *
     * @param x the matrix to compare with
     * @return {@code true} if both matrices have the same dimensions, {@code false} otherwise
     * @throws IllegalArgumentException if {@code x} is {@code null}
     */
    public boolean isSameShape(final X x) {
        N.checkArgNotNull(x, "x");
        return rowCount == x.rowCount && columnCount == x.columnCount;
    }

    /**
     * Returns a new matrix with each element repeated the specified number of times in both dimensions.
     * Each element is expanded into a block of size rowRepeats x columnRepeats.
     * The resulting matrix has dimensions (rowCount x rowRepeats) x (columnCount x columnRepeats).
     * The original matrix is not modified.
     *
     * <p>This operation is similar to MATLAB's repeatElements function. Each element becomes a block,
     * effectively creating a "zoomed in" version of the matrix where each original element
     * occupies multiple positions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    repeatElements(2, 2):
     * // 1 2          1 1 2 2
     * // 3 4     =>   1 1 2 2
     * //              3 3 4 4
     * //              3 3 4 4
     *
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix repeated = matrix.repeatElements(2, 2);   // 2×2 becomes 4×4
     * }</pre>
     *
     * @param rowRepeats number of times to repeat each element in the row direction (must be &gt;= 1)
     * @param columnRepeats number of times to repeat each element in the column direction (must be &gt;= 1)
     * @return a new matrix with repeated elements, with dimensions (rowCount x rowRepeats) x (columnCount x columnRepeats)
     * @throws IllegalArgumentException if rowRepeats &lt; 1 or columnRepeats &lt; 1
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repeatElements.html">MATLAB repeatElements</a>
     */
    public abstract X repeatElements(int rowRepeats, int columnRepeats);

    /**
     * Returns a new matrix formed by tiling this matrix the specified number of times in both dimensions.
     * The matrix is tiled rowRepeats times vertically and columnRepeats times horizontally.
     * The resulting matrix has dimensions (rowCount x rowRepeats) x (columnCount x columnRepeats).
     * The original matrix is not modified.
     *
     * <p>This operation is similar to MATLAB's repeatMatrix function. The entire matrix pattern
     * is replicated, creating a tiled arrangement.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Original:    repeatMatrix(2, 2):
     * // 1 2          1 2 1 2
     * // 3 4     =>   3 4 3 4
     * //              1 2 1 2
     * //              3 4 3 4
     *
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix tiled = matrix.repeatMatrix(2, 2);   // 2×2 becomes 4×4
     * }</pre>
     *
     * @param rowRepeats number of times to repeat the matrix in the row direction (must be &gt;= 1)
     * @param columnRepeats number of times to repeat the matrix in the column direction (must be &gt;= 1)
     * @return a new matrix with this matrix tiled, with dimensions (rowCount x rowRepeats) x (columnCount x columnRepeats)
     * @throws IllegalArgumentException if rowRepeats &lt; 1 or columnRepeats &lt; 1
     * @see <a href="https://www.mathworks.com/help/matlab/ref/repeatMatrix.html">MATLAB repeatMatrix</a>
     */
    public abstract X repeatMatrix(int rowRepeats, int columnRepeats);

    /**
     * Flattens this matrix into a one-dimensional list.
     * Elements are taken in row-major order (row by row from left to right).
     * The returned list is a new instance; modifications to it do not affect this matrix.
     *
     * <p>The flattening operation converts a two-dimensional matrix structure into a one-dimensional sequence,
     * which is useful for operations that work on linear data structures.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntList flat = matrix.flatten();   // Returns [1, 2, 3, 4]
     *
     * IntMatrix matrix2 = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntList flat2 = matrix2.flatten();   // Returns [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @return a new list containing all elements in row-major order with size equal to {@code elementCount}
     */
    public abstract PL flatten();

    /**
     * Applies the specified operation to the flattened (row-major order) view of this matrix.
     * The operation receives a single one-dimensional array containing all elements in row-major order,
     * and any modifications to that array are reflected back in this matrix.
     *
     * <p>This is useful for operations that are easier to perform on a flat array representation,
     * such as sorting all elements, applying statistical transformations, or batch updates.</p>
     *
     * <p><strong>Unsafe API boundary:</strong> the supplied action receives a mutable flattened view of the matrix data.
     * Any mutation performed by the action is reflected back into this matrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{3, 1, 4}, {1, 5, 9}});
     * matrix.applyOnFlattened(a -> java.util.Arrays.sort(a));   // Sorts all elements
     * // Matrix becomes [[1, 1, 3], [4, 5, 9]] (elements sorted in row-major order)
     *
     * matrix.applyOnFlattened(a -> { for (int i = 0; i < a.length; i++) a[i] *= 2; });
     * // Doubles all elements
     * }</pre>
     *
     * @param <E> the type of exception that the operation might throw
     * @param action the operation to apply to the flattened array (receives array type A, not A[])
     * @throws E if the operation throws an exception
     */
    public abstract <E extends Exception> void applyOnFlattened(Throwables.Consumer<? super A, E> action) throws E;

    /**
     * Performs the specified action for each element position in the matrix.
     * The action receives the row and column indices for each element.
     * Elements are processed in row-major order (row by row from left to right).
     * For large matrices, the operation may be automatically parallelized for better performance.
     *
     * <p>This method is useful when you need to access matrix positions without caring about
     * the actual element values, or when the element access logic is handled inside the action.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     *
     * matrix.forEachIndices((i, j) -> {
     *     System.out.println("Position: (" + i + "," + j + ")");
     * });
     *
     * // Count elements on the main diagonal
     * AtomicInteger diagonalCount = new AtomicInteger(0);
     * matrix.forEachIndices((i, j) -> {
     *     if (i == j) diagonalCount.incrementAndGet();
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param action the action to perform for each position, receives (rowIndex, columnIndex)
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEachIndices(final Throwables.IntBiConsumer<E> action) throws E {
        N.checkArgNotNull(action, "action");

        if (Matrices.isParallelizable(this)) {
            //noinspection FunctionalExpressionCanBeFolded
            final Throwables.IntBiConsumer<E> elementAction = action::accept;
            Matrices.forEachIndices(rowCount, columnCount, elementAction, true);
        } else {
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    action.accept(i, j);
                }
            }
        }
    }

    /**
     * Performs the specified action for each element position in the specified rectangular region of the matrix.
     * The action receives the row and column indices for each element in the region.
     * Elements are processed in row-major order within the specified region.
     * For large regions, the operation may be automatically parallelized for better performance.
     *
     * <p>This allows selective processing of matrix subregions without creating a copy.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * // Process only a 2×2 subregion starting at (1,1)
     * matrix.forEachIndices(1, 3, 1, 3, (i, j) -> {
     *     System.out.println("Processing element at (" + i + "," + j + ")");
     * });
     *
     * // Process only the first column
     * matrix.forEachIndices(0, matrix.rowCount(), 0, 1, (i, j) -> {
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
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEachIndices(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntBiConsumer<E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);
        N.checkArgNotNull(action, "action");

        if (Matrices.isParallelizable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            //noinspection FunctionalExpressionCanBeFolded
            final Throwables.IntBiConsumer<E> elementAction = action::accept;
            Matrices.forEachIndices(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, elementAction, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(i, j);
                }
            }
        }
    }

    /**
     * Performs the specified action for each element position in the matrix, providing the matrix itself as a parameter.
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
     * matrix.forEachIndices((i, j, m) -> {
     *     int value = m.get(i, j);
     *     System.out.println("Value at (" + i + "," + j + ") is " + value);
     * });
     *
     * // Set each element to the sum of its indices
     * matrix.forEachIndices((i, j, m) -> m.set(i, j, i + j));
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param action the action to perform, receiving (rowIndex, columnIndex, matrix)
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEachIndices(final Throwables.BiIntObjConsumer<X, E> action) throws E {
        final X matrix = (X) this;
        N.checkArgNotNull(action, "action");

        if (Matrices.isParallelizable(this)) {
            final Throwables.IntBiConsumer<E> elementAction = (i, j) -> action.accept(i, j, matrix);
            Matrices.forEachIndices(rowCount, columnCount, elementAction, true);
        } else {
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    action.accept(i, j, matrix);
                }
            }
        }
    }

    /**
     * Performs the specified action for each element position in the specified rectangular region, providing the matrix itself.
     * The action receives the row index, column index, and the matrix instance for each position in the region.
     * Elements are processed in row-major order within the specified region.
     * For large regions, the operation may be automatically parallelized for better performance.
     *
     * <p>This combines region-based iteration with matrix access, allowing you to process
     * a subregion while having access to the entire matrix.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * matrix.forEachIndices(1, 3, 1, 3, (i, j, m) -> {
     *     // Process only the 2×2 subregion with access to matrix
     *     int value = m.get(i, j);
     *     System.out.println("Value at (" + i + "," + j + "): " + value);
     * });
     *
     * // Update subregion based on neighboring values
     * matrix.forEachIndices(1, matrix.rowCount() - 1, 1, matrix.columnCount() - 1, (i, j, m) -> {
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
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEachIndices(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.BiIntObjConsumer<X, E> action) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);
        N.checkArgNotNull(action, "action");

        final X matrix = (X) this;

        if (Matrices.isParallelizable(this, ((long) (toRowIndex - fromRowIndex)) * (toColumnIndex - fromColumnIndex))) {
            final Throwables.IntBiConsumer<E> elementAction = (i, j) -> action.accept(i, j, matrix);
            Matrices.forEachIndices(fromRowIndex, toRowIndex, fromColumnIndex, toColumnIndex, elementAction, true);
        } else {
            for (int i = fromRowIndex; i < toRowIndex; i++) {
                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    action.accept(i, j, matrix);
                }
            }
        }
    }

    /**
     * Returns a stream of points directly above, below, to the left of, and to the right of the
     * specified position (the four cardinal directions). Only includes points that are within
     * matrix bounds; positions at the edges will have fewer than 4 adjacent points.
     *
     * <p>This method is useful for grid traversal algorithms, pathfinding, and neighbor analysis
     * where only orthogonal (non-diagonal) adjacency is considered. Points are returned in the
     * order: up, right, down, left.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Stream<Point> adjacent = matrix.adjacent4Points(0, 0);
     * // Returns stream of Point(0, 1) and Point(1, 0) - only right and down exist
     *
     * // Center position has all 4 neighbors
     * IntMatrix larger = IntMatrix.of(new int[3][3]);
     * Stream<Point> centerAdj = larger.adjacent4Points(1, 1);
     * // Returns all 4 adjacent points: (0,1), (1,2), (2,1), (1,0)
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return a stream of adjacent points in the four cardinal directions (0 to 4 points depending on position)
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public Stream<Point> adjacent4Points(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        final List<Point> points = new ArrayList<>(4);

        if (rowIndex > 0) {
            points.add(Point.of(rowIndex - 1, columnIndex)); // up
        }
        if (columnIndex < columnCount - 1) {
            points.add(Point.of(rowIndex, columnIndex + 1)); // right
        }
        if (rowIndex < rowCount - 1) {
            points.add(Point.of(rowIndex + 1, columnIndex)); // down
        }
        if (columnIndex > 0) {
            points.add(Point.of(rowIndex, columnIndex - 1)); // left
        }

        return Stream.of(points);
    }

    /**
     * Returns a stream of all 8 points adjacent to the specified position, including the points
     * directly above, below, to the left of, to the right of, and diagonally adjacent to the
     * specified position. Only includes points that are within matrix bounds.
     *
     * <p>This method is useful for algorithms requiring full 8-way adjacency, such as
     * certain pathfinding algorithms, cellular automaton simulations (like Conway's Game of Life),
     * or flood fill operations. Points are returned clockwise starting from the top-left:
     * leftUp, up, rightUp, right, rightDown, down, leftDown, left.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] {{true, false, true}, {false, true, false}, {true, false, true}});
     * Stream<Point> adjacent = matrix.adjacent8Points(1, 1);
     * // Returns stream of all 8 surrounding points for the center position
     *
     * // Corner position has only 3 neighbors
     * Stream<Point> corner = matrix.adjacent8Points(0, 0);
     * // Returns 3 points: (0,1), (1,1), (1,0)
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @param columnIndex the column index (0-based)
     * @return a stream of adjacent points in all 8 directions (0 to 8 points depending on position)
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    public Stream<Point> adjacent8Points(final int rowIndex, final int columnIndex) {
        checkRowColumnIndex(rowIndex, columnIndex);

        final List<Point> points = new ArrayList<>(8);

        if (rowIndex > 0 && columnIndex > 0) {
            points.add(Point.of(rowIndex - 1, columnIndex - 1)); // leftUp
        }

        if (rowIndex > 0) {
            points.add(Point.of(rowIndex - 1, columnIndex)); // up
        }

        if (rowIndex > 0 && columnIndex < columnCount - 1) {
            points.add(Point.of(rowIndex - 1, columnIndex + 1)); // rightUp
        }

        if (columnIndex < columnCount - 1) {
            points.add(Point.of(rowIndex, columnIndex + 1)); // right
        }

        if (rowIndex < rowCount - 1 && columnIndex < columnCount - 1) {
            points.add(Point.of(rowIndex + 1, columnIndex + 1)); // rightDown
        }

        if (rowIndex < rowCount - 1) {
            points.add(Point.of(rowIndex + 1, columnIndex)); // down
        }

        if (rowIndex < rowCount - 1 && columnIndex > 0) {
            points.add(Point.of(rowIndex + 1, columnIndex - 1)); // leftDown
        }

        if (columnIndex > 0) {
            points.add(Point.of(rowIndex, columnIndex - 1)); // left
        }

        return Stream.of(points);
    }

    /**
     * Returns a stream of points along the main diagonal (left-up to right-down).
     * The main diagonal consists of elements where row index equals column index.
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>LU2RD = Left-Up to Right-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> diagonal = matrix.pointsMainDiagonal();   // Points: (0,0), (1,1), (2,2)
     * List<Point> points = diagonal.toList();          // [(0,0), (1,1), (2,2)]
     * }</pre>
     *
     * @return a stream of {@link Point} objects representing the main diagonal positions
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public Stream<Point> pointsMainDiagonal() {
        checkIfRowAndColumnSizeAreSame();

        //noinspection resource
        return IntStream.range(0, rowCount).mapToObj(i -> Point.of(i, i));
    }

    /**
     * Returns a stream of points along the anti-diagonal (right-up to left-down).
     * The anti-diagonal consists of elements where row index + column index equals (columnCount - 1).
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>RU2LD = Right-Up to Left-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * Stream<Point> antiDiagonal = matrix.pointsAntiDiagonal();   // Points: (0,2), (1,1), (2,0)
     * List<Point> points = antiDiagonal.toList();          // [(0,2), (1,1), (2,0)]
     * }</pre>
     *
     * @return a stream of {@link Point} objects representing the anti-diagonal positions
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public Stream<Point> pointsAntiDiagonal() {
        checkIfRowAndColumnSizeAreSame();

        //noinspection resource
        return IntStream.range(0, rowCount).mapToObj(i -> Point.of(i, columnCount - i - 1));
    }

    /**
     * Returns a stream of all points in the matrix in row-major order (horizontal traversal).
     * Points are generated row by row from left to right, top to bottom.
     *
     * <p>H = Horizontal. This is equivalent to calling {@code pointsHorizontal(0, rowCount)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Stream<Point> allPoints = matrix.pointsHorizontal();
     * // Points in order: (0,0), (0,1), (1,0), (1,1)
     * allPoints.forEach(p -> System.out.println("Point: " + p));
     * }</pre>
     *
     * @return a stream of all {@link Point} objects in row-major order
     */
    public Stream<Point> pointsHorizontal() {
        return pointsHorizontal(0, rowCount);
    }

    /**
     * Returns a stream of points for a specific row in horizontal order (left to right).
     *
     * <p>This is equivalent to calling {@code pointsHorizontal(rowIndex, rowIndex + 1)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * Stream<Point> row1Points = matrix.pointsHorizontal(1);   // All points in row 1
     * // For a 3-column matrix: (1,0), (1,1), (1,2)
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @return a stream of {@link Point} objects for all columns in the specified row
     * @throws IndexOutOfBoundsException if rowIndex &lt; 0 or rowIndex &gt;= rowCount
     */
    public Stream<Point> pointsHorizontal(final int rowIndex) {
        return pointsHorizontal(rowIndex, rowIndex + 1);
    }

    /**
     * Returns a stream of points for a range of rows in row-major order (horizontal traversal).
     * Points are generated row by row from left to right for the specified row range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * // Get points from rows 1 and 2 (indices 1 and 2, not including 3)
     * Stream<Point> points = matrix.pointsHorizontal(1, 3);
     * // For a 2-column matrix: (1,0), (1,1), (2,0), (2,1)
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of {@link Point} objects in the specified row range, in row-major order
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    @SuppressWarnings("resource")
    public Stream<Point> pointsHorizontal(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);

        return IntStream.range(fromRowIndex, toRowIndex)
                .flatMapToObj(rowIndex -> IntStream.range(0, columnCount).mapToObj(columnIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of all points in the matrix in column-major order (vertical traversal).
     * Points are generated column by column from top to bottom, left to right.
     *
     * <p>V = Vertical. This is equivalent to calling {@code pointsVertical(0, columnCount)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * Stream<Point> columnOrder = matrix.pointsVertical();
     * // Points in order: (0,0), (1,0), (0,1), (1,1)
     * // For a 2×3 matrix, order would be: (0,0), (1,0), (0,1), (1,1), (0,2), (1,2)
     * }</pre>
     *
     * @return a stream of all {@link Point} objects in column-major order
     */
    public Stream<Point> pointsVertical() {
        return pointsVertical(0, columnCount);
    }

    /**
     * Returns a stream of points for a specific column in vertical order (top to bottom).
     *
     * <p>This is equivalent to calling {@code pointsVertical(columnIndex, columnIndex + 1)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * Stream<Point> col2Points = matrix.pointsVertical(2);   // All points in column 2
     * // For a 3-row matrix: (0,2), (1,2), (2,2)
     * }</pre>
     *
     * @param columnIndex the column index (0-based)
     * @return a stream of {@link Point} objects for all rows in the specified column
     * @throws IndexOutOfBoundsException if columnIndex &lt; 0 or columnIndex &gt;= columnCount
     */
    public Stream<Point> pointsVertical(final int columnIndex) {
        return pointsVertical(columnIndex, columnIndex + 1);
    }

    /**
     * Returns a stream of points for a range of columns in column-major order (vertical traversal).
     * Points are generated column by column from top to bottom for the specified column range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * // Get points from columns 1 through 3 (indices 1, 2, 3, not including 4)
     * Stream<Point> points = matrix.pointsVertical(1, 4);
     * // For a 2-row matrix: (0,1), (1,1), (0,2), (1,2), (0,3), (1,3)
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of {@link Point} objects in the specified column range, in column-major order
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; columnCount, or fromColumnIndex &gt; toColumnIndex
     */
    @SuppressWarnings("resource")
    public Stream<Point> pointsVertical(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

        return IntStream.range(fromColumnIndex, toColumnIndex)
                .flatMapToObj(columnIndex -> IntStream.range(0, rowCount).mapToObj(rowIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of streams where each inner stream represents a row of points.
     * This allows for row-by-row processing of matrix positions.
     *
     * <p>R = Row. This is equivalent to calling {@code pointsRows(0, rowCount)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * matrix.pointsRows().forEach(rowStream -> {
     *     rowStream.forEach(point -> System.out.println("Point: " + point));
     * });
     *
     * // Collect each row's points separately
     * List<List<Point>> rowsOfPoints = matrix.pointsRows()
     *     .map(Stream::toList)
     *     .toList();
     * }</pre>
     *
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one row
     */
    public Stream<Stream<Point>> pointsRows() {
        return pointsRows(0, rowCount);
    }

    /**
     * Returns a stream of streams for a range of rows, where each inner stream represents a row of points.
     * This allows for selective row-by-row processing of matrix positions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * // Process rows 1 and 2 separately
     * matrix.pointsRows(1, 3).forEach(rowStream -> {
     *     List<Point> rowPoints = rowStream.toList();
     *     // Process each row's points
     * });
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one row
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    @SuppressWarnings("resource")
    public Stream<Stream<Point>> pointsRows(final int fromRowIndex, final int toRowIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, rowCount);

        return IntStream.range(fromRowIndex, toRowIndex)
                .mapToObj(rowIndex -> IntStream.range(0, columnCount).mapToObj(columnIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of streams where each inner stream represents a column of points.
     * This allows for column-by-column processing of matrix positions.
     *
     * <p>C = Column. This is equivalent to calling {@code pointsColumns(0, columnCount)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * matrix.pointsColumns().forEach(colStream -> {
     *     colStream.forEach(point -> System.out.println("Point: " + point));
     * });
     *
     * // Collect each column's points separately
     * List<List<Point>> columnsOfPoints = matrix.pointsColumns()
     *     .map(Stream::toList)
     *     .toList();
     * }</pre>
     *
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one column
     */
    public Stream<Stream<Point>> pointsColumns() {
        return pointsColumns(0, columnCount);
    }

    /**
     * Returns a stream of streams for a range of columns, where each inner stream represents a column of points.
     * This allows for selective column-by-column processing of matrix positions.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * // Process columns 2 through 4 separately
     * matrix.pointsColumns(2, 5).forEach(colStream -> {
     *     List<Point> colPoints = colStream.toList();
     *     // Process each column's points
     * });
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of streams, where each inner stream contains {@link Point} objects for one column
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; columnCount, or fromColumnIndex &gt; toColumnIndex
     */
    @SuppressWarnings("resource")
    public Stream<Stream<Point>> pointsColumns(final int fromColumnIndex, final int toColumnIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, columnCount);

        return IntStream.range(fromColumnIndex, toColumnIndex)
                .mapToObj(columnIndex -> IntStream.range(0, rowCount).mapToObj(rowIndex -> Point.of(rowIndex, columnIndex)));
    }

    /**
     * Returns a stream of elements along the main diagonal (left-up to right-down).
     * The main diagonal consists of elements where row index equals column index.
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>LU2RD = Left-Up to Right-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntStream diagonal = matrix.streamMainDiagonal();   // Stream of: 1, 5, 9
     * int sum = diagonal.sum();                    // 15
     * }</pre>
     *
     * @return a stream of diagonal elements
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public abstract ES streamMainDiagonal();

    /**
     * Returns a stream of elements along the anti-diagonal (right-up to left-down).
     * The anti-diagonal consists of elements where row index + column index equals (columnCount - 1).
     * The matrix must be square (rowCount == columnCount) for this operation.
     *
     * <p>RU2LD = Right-Up to Left-Down diagonal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
     * IntStream antiDiagonal = matrix.streamAntiDiagonal();   // Stream of: 3, 5, 7
     * int sum = antiDiagonal.sum();                    // 15
     * }</pre>
     *
     * @return a stream of anti-diagonal elements
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    public abstract ES streamAntiDiagonal();

    /**
     * Returns a stream of all elements in row-major order (horizontal traversal).
     * Elements are streamed row by row from left to right, top to bottom.
     *
     * <p>H = Horizontal.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntStream elements = matrix.streamHorizontal();   // Stream of: 1, 2, 3, 4
     * int sum = elements.sum();                // 10
     * }</pre>
     *
     * @return a stream of all elements in row-major order
     */
    public abstract ES streamHorizontal();

    /**
     * Returns a stream of elements from a specific row.
     * Elements are streamed from left to right within the row.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream row1 = matrix.streamHorizontal(1);   // Stream of: 4, 5, 6
     * int max = row1.max().orElse(0);       // 6
     * }</pre>
     *
     * @param rowIndex the row index (0-based)
     * @return a stream of elements in the specified row
     * @throws IndexOutOfBoundsException if rowIndex &lt; 0 or rowIndex &gt;= rowCount
     */
    public abstract ES streamHorizontal(final int rowIndex);

    /**
     * Returns a stream of elements from a range of rows in row-major order.
     * Elements are streamed row by row from left to right for the specified row range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}, {5, 6}});
     * IntStream rows1and2 = matrix.streamHorizontal(1, 3);   // Stream of: 3, 4, 5, 6
     * long count = rows1and2.count();               // 4
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of elements in the specified row range
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    public abstract ES streamHorizontal(final int fromRowIndex, final int toRowIndex);

    /**
     * Returns a stream of all elements in column-major order (vertical traversal).
     * Elements are streamed column by column from top to bottom, left to right.
     *
     * <p>V = Vertical.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntStream elements = matrix.streamVertical();   // Stream of: 1, 3, 2, 4
     * int sum = elements.sum();                // 10
     * }</pre>
     *
     * @return a stream of all elements in column-major order
     */
    public abstract ES streamVertical();

    /**
     * Returns a stream of elements from a specific column.
     * Elements are streamed from top to bottom within the column.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream col1 = matrix.streamVertical(1);   // Stream of: 2, 5
     * int min = col1.min().orElse(0);       // 2
     * }</pre>
     *
     * @param columnIndex the column index (0-based)
     * @return a stream of elements in the specified column
     * @throws IndexOutOfBoundsException if columnIndex &lt; 0 or columnIndex &gt;= columnCount
     */
    public abstract ES streamVertical(final int columnIndex);

    /**
     * Returns a stream of elements from a range of columns in column-major order.
     * Elements are streamed column by column from top to bottom for the specified column range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * IntStream cols1and2 = matrix.streamVertical(1, 3);   // Stream of: 2, 5, 3, 6
     * double avg = cols1and2.average().orElse(0);   // 4.0
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of elements in the specified column range
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; columnCount, or fromColumnIndex &gt; toColumnIndex
     */
    public abstract ES streamVertical(final int fromColumnIndex, final int toColumnIndex);

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
     * matrix.streamRows().forEach(rowStream -> {
     *     int sum = rowStream.sum();   // Sum each row
     *     System.out.println("Row sum: " + sum);
     * });
     * // Output: Row sum: 6
     * //         Row sum: 15
     * }</pre>
     *
     * @return a stream of row streams
     */
    public abstract RS streamRows();

    /**
     * Returns a stream of row streams for a range of rows.
     * Each element in the outer stream is a stream representing one row of the matrix
     * within the specified range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * // Process only rows 1 and 2
     * matrix.streamRows(1, 3).forEach(rowStream -> {
     *     int max = rowStream.max().orElse(0);
     *     System.out.println("Row max: " + max);
     * });
     * }</pre>
     *
     * @param fromRowIndex the starting row index (inclusive, 0-based)
     * @param toRowIndex the ending row index (exclusive)
     * @return a stream of row streams for the specified range
     * @throws IndexOutOfBoundsException if fromRowIndex &lt; 0, toRowIndex &gt; rowCount, or fromRowIndex &gt; toRowIndex
     */
    public abstract RS streamRows(final int fromRowIndex, final int toRowIndex);

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
     * matrix.streamColumns().forEach(colStream -> {
     *     double avg = colStream.average().orElse(0);
     *     System.out.println("Column average: " + avg);
     * });
     * // Output: Column average: 2.0
     * //         Column average: 3.0
     * }</pre>
     *
     * @return a stream of column streams
     */
    public abstract RS streamColumns();

    /**
     * Returns a stream of column streams for a range of columns.
     * Each element in the outer stream is a stream representing one column of the matrix
     * within the specified range.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * // Process columns 2 through 4
     * matrix.streamColumns(2, 5).forEach(colStream -> {
     *     int sum = colStream.sum();
     *     System.out.println("Column sum: " + sum);
     * });
     * }</pre>
     *
     * @param fromColumnIndex the starting column index (inclusive, 0-based)
     * @param toColumnIndex the ending column index (exclusive)
     * @return a stream of column streams for the specified range
     * @throws IndexOutOfBoundsException if fromColumnIndex &lt; 0, toColumnIndex &gt; columnCount, or fromColumnIndex &gt; toColumnIndex
     */
    public abstract RS streamColumns(final int fromColumnIndex, final int toColumnIndex);

    /**
     * Executes the specified action with this matrix as the parameter.
     * This method enables the functional programming pattern of passing the matrix to a consumer function
     * for side-effect operations such as logging, validation, or modification.
     *
     * <p>This method is useful for performing operations on the matrix without returning a value,
     * such as printing, logging, or passing to utility methods that expect a consumer.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}});
     *
     * // Log matrix details
     * matrix.accept(m -> {
     *     System.out.println("Matrix dimensions: " + m.rowCount() + "x" + m.columnCount());
     *     m.println();
     * });
     *
     * // Validate matrix before processing
     * matrix.accept(m -> {
     *     if (m.isEmpty()) {
     *         throw new IllegalStateException("Matrix cannot be empty");
     *     }
     * });
     *
     * // Modify matrix elements in place
     * matrix.accept(m -> {
     *     for (int i = 0; i < m.rowCount(); i++) {
     *         m.set(i, 0, 0);   // Set first column to 0
     *     }
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the action might throw
     * @param action the consumer action to perform on this matrix
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super X, E> action) throws E {
        N.checkArgNotNull(action, "action");
        action.accept((X) this);
    }

    /**
     * Applies the specified function to this matrix and returns the result.
     * This method enables fluent-style transformations where the matrix needs to be passed to a function.
     * It follows the functional programming pattern of applying a function and returning its result.
     *
     * <p>This method is useful for extracting values from the matrix or transforming it
     * into a different type while maintaining a fluent interface.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix matrix = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}});
     * long elementCount = matrix.apply(AbstractMatrix::elementCount);
     * String info = matrix.apply(m -> "Matrix " + m.rowCount() + "x" + m.columnCount());
     *
     * // Transform matrix into a different representation
     * IntList allValues = matrix.apply(IntMatrix::flatten);
     * }</pre>
     *
     * @param <R> the result type of the function
     * @param <E> the type of exception that the function might throw
     * @param action the function to apply to this matrix
     * @return the result of applying the function to this matrix
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the function throws an exception
     */
    public <R, E extends Exception> R apply(final Throwables.Function<? super X, R, E> action) throws E {
        N.checkArgNotNull(action, "action");
        return action.apply((X) this);
    }

    /**
     * Returns the length of the given row array.
     * This abstract method must be implemented by concrete subclasses to return the length
     * of their specific array type (e.g., {@code int[]}, {@code double[]}, or {@code Object[]}).
     *
     * @param a the row array whose length is to be determined
     * @return the length of the array
     */
    protected abstract int length(@SuppressWarnings("hiding") A a);

    /**
     * Validates that this matrix has the same shape (dimensions) as the specified matrix.
     * This is a helper method used internally to enforce shape compatibility before
     * operations that require matrices of the same dimensions (e.g., element-wise addition).
     *
     * @param x the matrix to compare shape with
     * @throws IllegalArgumentException if the matrices have different row counts or column counts
     */
    protected void checkSameShape(final X x) {
        N.checkArgument(this.isSameShape(x), MSG_SHAPE_MISMATCH, rowCount, columnCount, x.rowCount, x.columnCount);
    }

    /**
     * Validates that the specified row and column indices are within the bounds of this matrix.
     * This is a helper method used internally to enforce index validity before element access
     * or neighbor lookup operations.
     *
     * @param rowIndex the row index to validate (must be in range [0, rowCount))
     * @param columnIndex the column index to validate (must be in range [0, columnCount))
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex is out of bounds
     */
    protected void checkRowColumnIndex(final int rowIndex, final int columnIndex) {
        if (rowIndex < 0 || rowIndex >= rowCount) {
            throw new ArrayIndexOutOfBoundsException(formatMsg(MSG_ROW_INDEX_OUT_OF_BOUNDS, rowIndex, rowCount));
        }

        if (columnIndex < 0 || columnIndex >= columnCount) {
            throw new ArrayIndexOutOfBoundsException(formatMsg(MSG_COLUMN_INDEX_OUT_OF_BOUNDS, columnIndex, columnCount));
        }
    }

    /**
     * Validates that this matrix is square (rowCount == columnCount).
     * This is a helper method used internally to enforce the square matrix requirement
     * for diagonal operations such as {@link #streamMainDiagonal()}, {@link #streamAntiDiagonal()},
     * {@link #pointsMainDiagonal()}, and {@link #pointsAntiDiagonal()}.
     *
     * @throws IllegalStateException if the matrix is not square (rowCount != columnCount)
     */
    protected void checkIfRowAndColumnSizeAreSame() {
        N.checkState(rowCount == columnCount, MSG_MATRIX_NOT_SQUARE, rowCount, columnCount);
    }

}

