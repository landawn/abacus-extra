/*
 * Copyright (C) 2020 HaiYang Li
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
import java.util.Iterator;
import java.util.List;

import com.landawn.abacus.logging.Logger;
import com.landawn.abacus.logging.LoggerFactory;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.Stream;

/**
 * Utility class providing static methods for matrix operations, parallel processing control,
 * and matrix manipulation functions. This class serves as a central point for matrix-related
 * utilities including shape validation, element-wise operations, and matrix transformations.
 * 
 * <p>The class supports parallel processing for large matrices and provides various utility
 * methods for working with different matrix types (ByteMatrix, IntMatrix, LongMatrix, 
 * DoubleMatrix, and generic Matrix).</p>
 * 
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Check if two matrices have the same shape
 * boolean same = Matrixes.isSameShape(matrixA, matrixB);
 * 
 * // Enable parallel processing
 * Matrixes.setParallelEnabled(ParallelEnabled.YES);
 * 
 * // Zip two matrices with a custom function
 * IntMatrix result = Matrixes.zip(matrix1, matrix2, (a, b) -> a + b);
 * }</pre>
 * 
 * @see AbstractMatrix
 * @see Matrix
 * @see ParallelEnabled
 */
public final class Matrixes {

    static final Logger logger = LoggerFactory.getLogger(Matrixes.class);

    static final int MIN_COUNT_FOR_PARALLEL = 8192;

    static final boolean IS_PARALLEL_STREAM_SUPPORTED;
    static final ThreadLocal<ParallelEnabled> isParallelEnabled_TL = ThreadLocal.withInitial(() -> ParallelEnabled.DEFAULT);

    static {
        boolean tmp = false;

        try {
            if (ClassUtil.forClass("com.landawn.abacus.util.stream.ParallelArrayIntStream") != null
                    && ClassUtil.forClass("com.landawn.abacus.util.stream.ParallelIteratorIntStream") != null) {
                tmp = true;
            }
        } catch (final Exception e) {
            // ignore.
        }

        IS_PARALLEL_STREAM_SUPPORTED = tmp;
    }

    private Matrixes() {
        // singleton: utility class.
    }

    /**
     * Returns the current parallel processing setting for the current thread.
     *
     * <p>The parallel processing setting is thread-local, allowing different threads to have
     * independent parallelization behaviors. This enables fine-grained control over parallel
     * execution in multithreaded applications.</p>
     *
     * <p>The returned value indicates how matrix operations should decide whether to use
     * parallel processing:</p>
     * <ul>
     * <li>{@link ParallelEnabled#YES} - Forces parallel execution regardless of matrix size</li>
     * <li>{@link ParallelEnabled#NO} - Forces sequential execution regardless of matrix size</li>
     * <li>{@link ParallelEnabled#DEFAULT} - Automatically decides based on matrix size (threshold: 8192 elements)</li>
     * </ul>
     *
     * @return the current {@link ParallelEnabled} setting for this thread, never {@code null}
     * @see #setParallelEnabled(ParallelEnabled)
     * @see ParallelEnabled
     */
    public static ParallelEnabled getParallelEnabled() {
        return isParallelEnabled_TL.get();
    }

    /**
     * Sets the parallel processing behavior for matrix operations in the current thread.
     *
     * <p>This method configures a thread-local setting that controls how matrix operations
     * decide whether to use parallel processing. The setting only affects the current thread,
     * allowing different threads to have independent parallelization strategies.</p>
     *
     * <p>Available settings:</p>
     * <ul>
     * <li>{@link ParallelEnabled#YES} - Forces all matrix operations to use parallel processing,
     *     regardless of matrix size. Use this when you know operations will benefit from parallelization.</li>
     * <li>{@link ParallelEnabled#NO} - Forces all matrix operations to use sequential processing,
     *     regardless of matrix size. Use this to avoid parallelization overhead for small matrices.</li>
     * <li>{@link ParallelEnabled#DEFAULT} - Automatically decides based on matrix size. Operations
     *     on matrices with 8192 or more elements use parallel processing; smaller matrices use sequential processing.</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Force parallel execution for large matrix operations
     * Matrixes.setParallelEnabled(ParallelEnabled.YES);
     * try {
     *     // All matrix operations here will use parallel processing
     *     matrix1.multiply(matrix2);
     *     matrix3.add(matrix4);
     * } finally {
     *     // Always reset to default to avoid affecting other code
     *     Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
     * }
     * }</pre>
     *
     * @param flag the {@link ParallelEnabled} setting to apply to the current thread, must not be {@code null}
     * @throws IllegalArgumentException if {@code flag} is {@code null}
     * @see #getParallelEnabled()
     * @see ParallelEnabled
     */
    public static void setParallelEnabled(final ParallelEnabled flag) throws IllegalArgumentException {
        N.checkArgNotNull(flag);

        isParallelEnabled_TL.set(flag);
    }

    /**
     * Determines whether the given matrix should be processed using parallel execution.
     *
     * <p>This method evaluates whether parallel processing should be used for operations on the
     * specified matrix based on its total element count. The decision considers:</p>
     * <ul>
     * <li>The current thread's {@link ParallelEnabled} setting</li>
     * <li>Whether parallel stream support is available in the runtime environment</li>
     * <li>The total number of elements in the matrix (rows × columns)</li>
     * </ul>
     *
     * <p>This is a convenience method that delegates to {@link #isParallelable(AbstractMatrix, long)}
     * using the matrix's total element count.</p>
     *
     * @param x the matrix to evaluate for parallelization, must not be {@code null}
     * @return {@code true} if parallel processing should be used for this matrix; {@code false} for sequential processing
     * @see #isParallelable(AbstractMatrix, long)
     * @see #setParallelEnabled(ParallelEnabled)
     */
    public static boolean isParallelable(final AbstractMatrix<?, ?, ?, ?, ?> x) {
        return isParallelable(x, x.count);
    }

    /**
     * Determines whether a matrix operation should be processed using parallel execution
     * based on the element count and current parallel settings.
     *
     * <p>This method makes the parallelization decision using a multifactor evaluation:</p>
     * <ol>
     * <li><b>Runtime Support:</b> Parallel streams must be available in the runtime environment.
     *     If not supported, always returns {@code false}.</li>
     * <li><b>Thread Setting:</b> Checks the current thread's {@link ParallelEnabled} setting:
     *     <ul>
     *     <li>{@link ParallelEnabled#YES} - Always returns {@code true} (if runtime supports it)</li>
     *     <li>{@link ParallelEnabled#NO} - Always returns {@code false}</li>
     *     <li>{@link ParallelEnabled#DEFAULT} - Decides based on element count</li>
     *     </ul>
     * </li>
     * <li><b>Element Count:</b> When using {@code DEFAULT} setting, returns {@code true} only if
     *     {@code count >= 8192}. This threshold balances the overhead of parallel execution
     *     against the performance benefits for larger datasets.</li>
     * </ol>
     *
     * @param x the matrix being evaluated (reserved for future extensibility, currently not used in the decision logic)
     * @param count the number of elements to process; typically the total element count or a subset being operated on
     * @return {@code true} if parallel processing should be used; {@code false} for sequential processing
     * @see #setParallelEnabled(ParallelEnabled)
     * @see ParallelEnabled
     */
    public static boolean isParallelable(@SuppressWarnings("unused") final AbstractMatrix<?, ?, ?, ?, ?> x, final long count) { // NOSONAR
        return IS_PARALLEL_STREAM_SUPPORTED && (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.YES
                || (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.DEFAULT && count >= MIN_COUNT_FOR_PARALLEL));
    }

    /**
     * Checks if two matrices have the same shape (identical dimensions).
     *
     * <p>Two matrices are considered to have the same shape if and only if they have
     * the same number of rows AND the same number of columns. This is a fundamental
     * requirement for many matrix operations such as element-wise addition, subtraction,
     * and comparison.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});      // 2×2 matrix
     * IntMatrix m2 = IntMatrix.of(new int[][] {{5, 6}, {7, 8}});      // 2×2 matrix
     * IntMatrix m3 = IntMatrix.of(new int[][] {{1, 2, 3}, {4, 5, 6}}); // 2×3 matrix
     *
     * boolean same1 = Matrixes.isSameShape(m1, m2); // true
     * boolean same2 = Matrixes.isSameShape(m1, m3); // false
     * }</pre>
     *
     * @param <X> the type of matrix, must extend {@link AbstractMatrix}
     * @param a the first matrix to compare, must not be {@code null}
     * @param b the second matrix to compare, must not be {@code null}
     * @return {@code true} if both matrices have the same number of rows and columns; {@code false} otherwise
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> boolean isSameShape(final X a, final X b) {
        return a.rows == b.rows && a.cols == b.cols;
    }

    /**
     * Checks if three matrices have the same shape (identical dimensions).
     *
     * <p>Three matrices are considered to have the same shape if they all have the same
     * number of rows AND the same number of columns. This method is commonly used to
     * validate inputs for ternary matrix operations.</p>
     *
     * @param <X> the type of matrix, must extend {@link AbstractMatrix}
     * @param a the first matrix to compare, must not be {@code null}
     * @param b the second matrix to compare, must not be {@code null}
     * @param c the third matrix to compare, must not be {@code null}
     * @return {@code true} if all three matrices have the same number of rows and columns; {@code false} otherwise
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> boolean isSameShape(final X a, final X b, final X c) {
        return a.rows == b.rows && a.rows == c.rows && a.cols == b.cols && a.cols == c.cols;
    }

    /**
     * Checks if all matrices in a collection have the same shape (identical dimensions).
     *
     * <p>This method verifies that all matrices in the collection have the same number of
     * rows and columns. It is particularly useful for validating inputs before performing
     * operations that require multiple matrices of the same shape, such as element-wise
     * aggregations or zip operations.</p>
     *
     * <p>Special cases:</p>
     * <ul>
     * <li>Empty collection: Returns {@code true} (vacuous truth)</li>
     * <li>Single matrix: Returns {@code true} (trivially same shape)</li>
     * <li>Multiple matrices: Returns {@code true} only if all have identical dimensions</li>
     * </ul>
     *
     * @param <X> the type of matrix, must extend {@link AbstractMatrix}
     * @param xs the collection of matrices to check, may be {@code null} or empty
     * @return {@code true} if all matrices have the same number of rows and columns, or if the collection
     *         is {@code null}, empty, or contains only one matrix; {@code false} if any matrix has different dimensions
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> boolean isSameShape(final Collection<? extends X> xs) {
        if (N.isEmpty(xs) || xs.size() == 1) {
            return true;
        }

        final Iterator<? extends X> iterator = xs.iterator();
        final X first = iterator.next();
        final int rows = first.rows;
        final int cols = first.cols;
        X next = null;

        while (iterator.hasNext()) {
            next = iterator.next();

            if (next.rows != rows || next.cols != cols) {
                return false;
            }
        }

        return true;
    }

    /**
     * Creates a new two-dimensional array with the specified dimensions and element type.
     *
     * <p>This utility method constructs a properly typed 2D array at runtime, handling the
     * complexity of creating generic arrays in Java. The method automatically wraps primitive
     * types to their corresponding wrapper classes (e.g., {@code int} becomes {@code Integer}).</p>
     *
     * <p>The resulting array is fully initialized with all row arrays allocated. Each element
     * is initialized to {@code null} for reference types or the default value for primitive
     * wrapper types.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create a 3×4 array of Double objects
     * Double[][] doubles = Matrixes.newArray(3, 4, Double.class);
     *
     * // Create a 2×5 array of String objects
     * String[][] strings = Matrixes.newArray(2, 5, String.class);
     *
     * // Primitive types are automatically wrapped
     * Integer[][] ints = Matrixes.newArray(10, 20, int.class);
     * }</pre>
     *
     * @param <T> the element type of the array
     * @param rows the number of rows in the 2D array, must be non-negative
     * @param cols the number of columns in each row, must be non-negative
     * @param targetElementType the class of the element type; primitive types will be auto-wrapped
     * @return a new 2D array of type {@code T[][]} with the specified dimensions, never {@code null}
     */
    public static <T> T[][] newArray(final int rows, final int cols, final Class<T> targetElementType) {
        final Class<T> eleType = (Class<T>) ClassUtil.wrap(targetElementType);
        final Class<T[]> subArrayType = (Class<T[]>) N.newArray(eleType, 0).getClass();

        final T[][] result = N.newArray(subArrayType, rows);

        for (int i = 0; i < rows; i++) {
            result[i] = N.newArray(eleType, cols);
        }

        return result;
    }

    /**
     * Executes the specified command with a temporary parallel processing setting, then
     * restores the original setting.
     *
     * <p>This method provides a safe way to temporarily change the parallel processing behavior
     * for a specific operation without affecting the thread-local setting for subsequent operations.
     * The original {@link ParallelEnabled} setting is always restored, even if the command throws
     * an exception.</p>
     *
     * <p>This is particularly useful when you need to force parallel or sequential execution for
     * a specific block of code without manually managing the setting changes.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Force parallel execution for specific operations
     * Matrixes.run(() -> {
     *     // This operation will use parallel processing
     *     matrix1.multiply(matrix2);
     *     matrix3.add(matrix4);
     * }, ParallelEnabled.YES);
     *
     * // After execution, the original setting is restored
     *
     * // Force sequential execution for small operations
     * Matrixes.run(() -> {
     *     smallMatrix.transpose();
     * }, ParallelEnabled.NO);
     * }</pre>
     *
     * @param <E> the type of exception that the command might throw
     * @param cmd the command to execute, must not be {@code null}
     * @param parallelEnabled the temporary {@link ParallelEnabled} setting to use during command execution
     * @throws E if the command throws an exception during execution
     * @see #setParallelEnabled(ParallelEnabled)
     * @see #getParallelEnabled()
     */
    public static <E extends Exception> void run(final Throwables.Runnable<E> cmd, final ParallelEnabled parallelEnabled) throws E {
        final ParallelEnabled original = Matrixes.getParallelEnabled();
        Matrixes.setParallelEnabled(parallelEnabled);

        try {
            cmd.run();
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    /**
     * Executes a command for each position in a matrix grid defined by rows and columns.
     *
     * <p>This method iterates over all positions in a matrix of the specified dimensions,
     * executing the provided command with the row and column indices (i, j) for each position.
     * The iteration order is optimized based on the relative sizes of rows and columns to
     * improve cache locality.</p>
     *
     * <p>This is a convenience method that delegates to
     * {@link #run(int, int, int, int, Throwables.IntBiConsumer, boolean)} with the full
     * range of rows and columns (starting from 0).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Print all positions in a 3×4 matrix
     * Matrixes.run(3, 4, (i, j) ->
     *     System.out.println("(" + i + "," + j + ")"), false);
     *
     * // Initialize a result array in parallel
     * int[][] result = new int[100][100];
     * Matrixes.run(100, 100, (i, j) ->
     *     result[i][j] = i * j, true);
     * }</pre>
     *
     * @param <E> the type of exception that the command might throw
     * @param rows the number of rows to iterate over, must be non-negative
     * @param cols the number of columns to iterate over, must be non-negative
     * @param cmd the command to execute for each position (i, j), receives row index and column index
     * @param inParallel {@code true} to execute in parallel; {@code false} for sequential execution
     * @throws E if the command throws an exception during execution
     * @see #run(int, int, int, int, Throwables.IntBiConsumer, boolean)
     */
    public static <E extends Exception> void run(final int rows, final int cols, final Throwables.IntBiConsumer<E> cmd, final boolean inParallel) throws E {
        run(0, rows, 0, cols, cmd, inParallel);
    }

    /**
     * Executes a command for each position in a specified subregion of a matrix grid.
     *
     * <p>This method iterates over a rectangular region defined by the row and column index ranges,
     * executing the provided command with the (i, j) indices for each position in the region.
     * The iteration order is automatically optimized based on the relative sizes of the row and
     * column ranges to improve cache locality and performance.</p>
     *
     * <p>Iteration strategy:</p>
     * <ul>
     * <li>If there are fewer or equal rows than columns, iterates by rows first (row-major order)</li>
     * <li>If there are more rows than columns, iterates by columns first (column-major order)</li>
     * <li>When parallel execution is enabled, the outer loop is parallelized while the inner loop
     *     remains sequential</li>
     * </ul>
     *
     * @param <E> the type of exception that the command might throw
     * @param fromRowIndex the starting row index (inclusive), must be non-negative
     * @param toRowIndex the ending row index (exclusive), must be &gt;= fromRowIndex
     * @param fromColumnIndex the starting column index (inclusive), must be non-negative
     * @param toColumnIndex the ending column index (exclusive), must be &gt;= fromColumnIndex
     * @param cmd the command to execute for each position (i, j), receives row index and column index
     * @param inParallel {@code true} to execute in parallel; {@code false} for sequential execution
     * @throws IndexOutOfBoundsException if any index is negative or if toRowIndex &lt; fromRowIndex or toColumnIndex &lt; fromColumnIndex
     * @throws E if the command throws an exception during execution
     */
    public static <E extends Exception> void run(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntBiConsumer<E> cmd, final boolean inParallel) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, Integer.MAX_VALUE);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, Integer.MAX_VALUE);

        final int rows = toRowIndex - fromRowIndex;
        final int cols = toColumnIndex - fromColumnIndex;

        if (inParallel) {
            if (rows <= cols) {
                //noinspection resource
                IntStream.range(fromRowIndex, toRowIndex).parallel().forEach(i -> {
                    for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                        cmd.accept(i, j);
                    }
                });
            } else {
                //noinspection resource
                IntStream.range(fromColumnIndex, toColumnIndex).parallel().forEach(j -> {
                    for (int i = fromRowIndex; i < toRowIndex; i++) {
                        cmd.accept(i, j);
                    }
                });
            }
        } else {
            if (rows <= cols) {
                for (int i = fromRowIndex; i < toRowIndex; i++) {
                    for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                        cmd.accept(i, j);
                    }
                }
            } else {
                for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                    for (int i = fromRowIndex; i < toRowIndex; i++) {
                        cmd.accept(i, j);
                    }
                }
            }
        }
    }

    /**
     * Executes a function for each position in a matrix grid and returns the results as a stream.
     *
     * <p>This method applies the provided function to each position (i, j) in a matrix of the
     * specified dimensions and collects all results into a {@link Stream}. The iteration order
     * is optimized based on the relative sizes of rows and columns.</p>
     *
     * <p>This is a convenience method that delegates to
     * {@link #call(int, int, int, int, Throwables.IntBiFunction, boolean)} with the full
     * range of rows and columns (starting from 0).</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Generate coordinates as strings
     * Stream<String> coords = Matrixes.call(2, 3, (i, j) -> i + "," + j, false);
     * // Results: "0,0", "0,1", "0,2", "1,0", "1,1", "1,2"
     *
     * // Create Point objects for each position
     * Stream<Point> points = Matrixes.call(10, 10, (i, j) -> new Point(i, j), true);
     * }</pre>
     *
     * @param <T> the type of elements in the result stream
     * @param rows the number of rows to iterate over, must be non-negative
     * @param cols the number of columns to iterate over, must be non-negative
     * @param cmd the function to apply at each position (i, j), receives row index and column index
     * @param inParallel {@code true} to execute in parallel; {@code false} for sequential execution
     * @return a {@link Stream} of results from applying the function at each position, never {@code null}
     * @see #call(int, int, int, int, Throwables.IntBiFunction, boolean)
     */
    public static <T> Stream<T> call(final int rows, final int cols, final Throwables.IntBiFunction<? extends T, ? extends Exception> cmd,
            final boolean inParallel) {
        return call(0, rows, 0, cols, cmd, inParallel);
    }

    /**
     * Executes a function for each position in a specified subregion of a matrix grid and
     * returns the results as a stream.
     *
     * <p>This method applies the provided function to each position (i, j) in the rectangular
     * region defined by the row and column index ranges, collecting all results into a {@link Stream}.
     * The iteration order is automatically optimized based on the relative sizes of the row and
     * column ranges to improve performance.</p>
     *
     * <p>The order of elements in the stream depends on whether there are more rows or columns:</p>
     * <ul>
     * <li>If rows ≤ columns: Elements are ordered by rows first (row-major order)</li>
     * <li>If rows > columns: Elements are ordered by columns first (column-major order)</li>
     * </ul>
     *
     * @param <T> the type of elements in the result stream
     * @param fromRowIndex the starting row index (inclusive), must be non-negative
     * @param toRowIndex the ending row index (exclusive), must be &gt;= fromRowIndex
     * @param fromColumnIndex the starting column index (inclusive), must be non-negative
     * @param toColumnIndex the ending column index (exclusive), must be &gt;= fromColumnIndex
     * @param cmd the function to apply at each position (i, j), receives row index and column index
     * @param inParallel {@code true} to execute in parallel; {@code false} for sequential execution
     * @return a {@link Stream} of results from applying the function at each position, never {@code null}
     * @throws IndexOutOfBoundsException if any index is negative or if toRowIndex &lt; fromRowIndex or toColumnIndex &lt; fromColumnIndex
     */
    @SuppressWarnings("resource")
    public static <T> Stream<T> call(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntBiFunction<? extends T, ? extends Exception> cmd, final boolean inParallel) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, Integer.MAX_VALUE);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, Integer.MAX_VALUE);

        final int rows = toRowIndex - fromRowIndex;
        final int cols = toColumnIndex - fromColumnIndex;

        if (rows <= cols) {
            return IntStream.range(fromRowIndex, toRowIndex).transform(s -> inParallel ? s.parallel() : s).flatmapToObj(i -> {
                final List<T> ret = new ArrayList<>(cols);

                try {
                    for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                        ret.add(cmd.apply(i, j));
                    }
                } catch (final Exception e) {
                    throw ExceptionUtil.toRuntimeException(e, true);
                }

                return ret;
            });
        } else {
            return IntStream.range(fromColumnIndex, toColumnIndex).transform(s -> inParallel ? s.parallel() : s).flatmapToObj(j -> {
                final List<T> ret = new ArrayList<>(rows);

                try {
                    for (int i = fromRowIndex; i < toRowIndex; i++) {
                        ret.add(cmd.apply(i, j));
                    }
                } catch (final Exception e) {
                    throw ExceptionUtil.toRuntimeException(e, true);
                }

                return ret;
            });
        }
    }

    /**
     * Executes a function that returns {@code int} values for each position in a matrix grid
     * and returns the results as an {@link IntStream}.
     *
     * <p>This method applies the provided integer binary operator to each position (i, j) in a
     * matrix of the specified dimensions and collects all results into an {@link IntStream}.
     * This is optimized for primitive {@code int} operations, avoiding boxing overhead.</p>
     *
     * <p>This is a convenience method that delegates to
     * {@link #callToInt(int, int, int, int, Throwables.IntBinaryOperator, boolean)} with the
     * full range of rows and columns (starting from 0).</p>
     *
     * @param rows the number of rows to iterate over, must be non-negative
     * @param cols the number of columns to iterate over, must be non-negative
     * @param cmd the function to apply at each position (i, j), receives row index and column index
     * @param inParallel {@code true} to execute in parallel; {@code false} for sequential execution
     * @return an {@link IntStream} of results from applying the function at each position, never {@code null}
     * @see #callToInt(int, int, int, int, Throwables.IntBinaryOperator, boolean)
     */
    public static IntStream callToInt(final int rows, final int cols, final Throwables.IntBinaryOperator<? extends Exception> cmd, final boolean inParallel) {
        return callToInt(0, rows, 0, cols, cmd, inParallel);
    }

    /**
     * Executes a function that returns {@code int} values for each position in a specified
     * subregion of a matrix grid and returns the results as an {@link IntStream}.
     *
     * <p>This method applies the provided integer binary operator to each position (i, j) in the
     * rectangular region defined by the row and column index ranges, collecting all results into
     * an {@link IntStream}. This is optimized for primitive {@code int} operations, avoiding
     * boxing overhead associated with generic streams.</p>
     *
     * <p>The iteration order is automatically optimized based on the relative sizes of the row
     * and column ranges to improve cache locality and performance.</p>
     *
     * @param fromRowIndex the starting row index (inclusive), must be non-negative
     * @param toRowIndex the ending row index (exclusive), must be &gt;= fromRowIndex
     * @param fromColumnIndex the starting column index (inclusive), must be non-negative
     * @param toColumnIndex the ending column index (exclusive), must be &gt;= fromColumnIndex
     * @param cmd the function to apply at each position (i, j), receives row index and column index
     * @param inParallel {@code true} to execute in parallel; {@code false} for sequential execution
     * @return an {@link IntStream} of results from applying the function at each position, never {@code null}
     * @throws IndexOutOfBoundsException if any index is negative or if toRowIndex &lt; fromRowIndex or toColumnIndex &lt; fromColumnIndex
     */
    @SuppressWarnings("resource")
    public static IntStream callToInt(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntBinaryOperator<? extends Exception> cmd, final boolean inParallel) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromRowIndex, toRowIndex, Integer.MAX_VALUE);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, Integer.MAX_VALUE);

        final int rows = toRowIndex - fromRowIndex;
        final int cols = toColumnIndex - fromColumnIndex;

        if (rows <= cols) {
            return IntStream.range(fromRowIndex, toRowIndex).transform(s -> inParallel ? s.parallel() : s).flatmap(i -> {
                final int[] ret = new int[cols];

                try {
                    for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                        ret[j - fromColumnIndex] = cmd.applyAsInt(i, j);
                    }
                } catch (final Exception e) {
                    throw ExceptionUtil.toRuntimeException(e, true);
                }

                return ret;
            });
        } else {
            return IntStream.range(fromColumnIndex, toColumnIndex).transform(s -> inParallel ? s.parallel() : s).flatmap(j -> {
                final int[] ret = new int[rows];

                try {
                    for (int i = fromRowIndex; i < toRowIndex; i++) {
                        ret[i - fromRowIndex] = cmd.applyAsInt(i, j);
                    }
                } catch (final Exception e) {
                    throw ExceptionUtil.toRuntimeException(e, true);
                }

                return ret;
            });
        }
    }

    /**
     * Performs matrix multiplication iteration using a custom accumulator function.
     *
     * <p>This method iterates through all the positions required for matrix multiplication,
     * calling the provided command for each (i, j, k) triple. It does NOT perform the actual
     * multiplication arithmetic - that must be implemented in the command function. This provides
     * maximum flexibility for custom multiplication algorithms.</p>
     *
     * <p>For standard matrix multiplication C = A × B, the command would typically accumulate:
     * {@code C[i][j] += A[i][k] * B[k][j]}</p>
     *
     * <p>Index meanings:</p>
     * <ul>
     * <li>{@code i} - Row index in matrix A (and result matrix C)</li>
     * <li>{@code j} - Column index in matrix B (and result matrix C)</li>
     * <li>{@code k} - Common dimension (columns in A, rows in B)</li>
     * </ul>
     *
     * <p>The matrices must satisfy the multiplication constraint: {@code a.cols == b.rows}.
     * The resulting matrix would have dimensions {@code a.rows × b.cols}.</p>
     *
     * <p>Parallelization is automatically determined based on the matrix sizes and current
     * thread settings using {@link #isParallelable(AbstractMatrix, long)}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] result = new int[matrixA.rows()][matrixB.cols()];
     * Matrixes.multiply(matrixA, matrixB, (i, j, k) -> {
     *     result[i][j] += matrixA.get(i, k) * matrixB.get(k, j);
     * });
     * }</pre>
     *
     * @param <X> the type of matrix, must extend {@link AbstractMatrix}
     * @param a the first matrix (left operand), must not be {@code null}
     * @param b the second matrix (right operand), must not be {@code null}
     * @param cmd the accumulator function called for each (i, j, k) triple in the multiplication
     * @throws IllegalArgumentException if matrix dimensions are incompatible ({@code a.cols != b.rows})
     * @see #multiply(AbstractMatrix, AbstractMatrix, Throwables.IntTriConsumer, boolean)
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> void multiply(final X a, final X b, final Throwables.IntTriConsumer<RuntimeException> cmd)
            throws IllegalArgumentException {
        N.checkArgument(a.cols == b.rows, "Illegal matrix dimensions");

        multiply(a, b, cmd, Matrixes.isParallelable(a, a.count * b.cols));
    }

    /**
     * Performs matrix multiplication iteration using a custom accumulator function with explicit
     * control over parallel execution.
     *
     * <p>This method provides the same iteration functionality as
     * {@link #multiply(AbstractMatrix, AbstractMatrix, Throwables.IntTriConsumer)} but with
     * explicit control over whether to use parallel processing. The iteration strategy is
     * automatically optimized based on the matrix dimensions to minimize cache misses and
     * maximize performance.</p>
     *
     * <p>The iteration order is determined by which dimension is smallest among:
     * {@code a.rows}, {@code a.cols} (= {@code b.rows}), and {@code b.cols}. The smallest
     * dimension is used for the outermost loop to optimize parallelization.</p>
     *
     * <p>When parallel execution is enabled, the outermost loop is parallelized while inner
     * loops remain sequential for better performance.</p>
     *
     * @param <X> the type of matrix, must extend {@link AbstractMatrix}
     * @param a the first matrix (left operand), must not be {@code null}
     * @param b the second matrix (right operand), must not be {@code null}
     * @param cmd the accumulator function called for each (i, j, k) triple in the multiplication
     * @param inParallel {@code true} to force parallel execution; {@code false} for sequential execution
     * @throws IllegalArgumentException if matrix dimensions are incompatible ({@code a.cols != b.rows})
     * @see #multiply(AbstractMatrix, AbstractMatrix, Throwables.IntTriConsumer)
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> void multiply(final X a, final X b, final Throwables.IntTriConsumer<RuntimeException> cmd, // NOSONAR
            final boolean inParallel) throws IllegalArgumentException {
        N.checkArgument(a.cols == b.rows, "Illegal matrix dimensions");

        final int rowsA = a.rows;
        final int colsA = a.cols;
        final int colsB = b.cols;

        if (inParallel) {
            if (N.min(rowsA, colsA, colsB) == rowsA) {
                if (N.min(colsA, colsB) == colsA) {
                    //noinspection resource
                    IntStream.range(0, rowsA).parallel().forEach(i -> {
                        for (int k = 0; k < colsA; k++) {
                            for (int j = 0; j < colsB; j++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    });
                } else {
                    //noinspection resource
                    IntStream.range(0, rowsA).parallel().forEach(i -> {
                        for (int j = 0; j < colsB; j++) {
                            for (int k = 0; k < colsA; k++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    });
                }
            } else if (N.min(rowsA, colsA, colsB) == colsA) {
                if (N.min(rowsA, colsB) == rowsA) {
                    //noinspection resource
                    IntStream.range(0, colsA).parallel().forEach(k -> {
                        for (int i = 0; i < rowsA; i++) {
                            for (int j = 0; j < colsB; j++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    });
                } else {
                    //noinspection resource
                    IntStream.range(0, colsA).parallel().forEach(k -> {
                        for (int j = 0; j < colsB; j++) {
                            for (int i = 0; i < rowsA; i++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    });
                }
            } else {
                if (N.min(rowsA, colsA) == rowsA) {
                    //noinspection resource
                    IntStream.range(0, colsB).parallel().forEach(j -> {
                        for (int i = 0; i < rowsA; i++) {
                            for (int k = 0; k < colsA; k++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    });
                } else {
                    //noinspection resource
                    IntStream.range(0, colsB).parallel().forEach(j -> {
                        for (int k = 0; k < colsA; k++) {
                            for (int i = 0; i < rowsA; i++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    });
                }
            }
        } else {
            if (N.min(rowsA, colsA, colsB) == rowsA) {
                if (N.min(colsA, colsB) == colsA) {
                    for (int i = 0; i < rowsA; i++) {
                        for (int k = 0; k < colsA; k++) {
                            for (int j = 0; j < colsB; j++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < rowsA; i++) {
                        for (int j = 0; j < colsB; j++) {
                            for (int k = 0; k < colsA; k++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    }
                }
            } else if (N.min(rowsA, colsA, colsB) == colsA) {
                if (N.min(rowsA, colsB) == rowsA) {
                    for (int k = 0; k < colsA; k++) {
                        for (int i = 0; i < rowsA; i++) {
                            for (int j = 0; j < colsB; j++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    }
                } else {
                    for (int k = 0; k < colsA; k++) {
                        for (int j = 0; j < colsB; j++) {
                            for (int i = 0; i < rowsA; i++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    }
                }
            } else {
                if (N.min(rowsA, colsA) == rowsA) {
                    for (int j = 0; j < colsB; j++) {
                        for (int i = 0; i < rowsA; i++) {
                            for (int k = 0; k < colsA; k++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < colsB; j++) {
                        for (int k = 0; k < colsA; k++) {
                            for (int i = 0; i < rowsA; i++) {
                                cmd.accept(i, j, k);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Combines two {@link ByteMatrix} objects element-wise using a binary operator.
     *
     * <p>This method performs element-wise combination of two byte matrices using the provided
     * binary operator. For each position (i, j), the function is called with the corresponding
     * elements from both matrices: {@code zipFunction.apply(a[i][j], b[i][j])}.</p>
     *
     * <p>Both matrices must have identical dimensions (same number of rows and columns).
     * The operation delegates to the {@link ByteMatrix#zipWith} method.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteMatrix m1 = ByteMatrix.of(new byte[][] {{1, 2}, {3, 4}});
     * ByteMatrix m2 = ByteMatrix.of(new byte[][] {{5, 6}, {7, 8}});
     *
     * // Element-wise addition
     * ByteMatrix sum = Matrixes.zip(m1, m2, (a, b) -> (byte)(a + b));
     * // Result: [[6, 8], [10, 12]]
     *
     * // Element-wise maximum
     * ByteMatrix max = Matrixes.zip(m1, m2, (a, b) -> (byte)Math.max(a, b));
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the binary operator to combine corresponding elements from both matrices
     * @return a new {@link ByteMatrix} containing the results of applying the function to each pair of elements
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see ByteMatrix#zipWith(ByteMatrix, Throwables.ByteBinaryOperator)
     */
    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     * Combines three {@link ByteMatrix} objects element-wise using a ternary operator.
     *
     * <p>This method performs element-wise combination of three byte matrices using the provided
     * ternary operator. For each position (i, j), the function is called with the corresponding
     * elements from all three matrices: {@code zipFunction.apply(a[i][j], b[i][j], c[i][j])}.</p>
     *
     * <p>All three matrices must have identical dimensions (same number of rows and columns).
     * The operation delegates to the {@link ByteMatrix#zipWith} method.</p>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param c the third matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the ternary operator to combine corresponding elements from all three matrices
     * @return a new {@link ByteMatrix} containing the results of applying the function to each triple of elements
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see ByteMatrix#zipWith(ByteMatrix, ByteMatrix, Throwables.ByteTernaryOperator)
     */
    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final ByteMatrix c,
            final Throwables.ByteTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     * Combines multiple {@link ByteMatrix} objects element-wise using a binary operator applied sequentially.
     *
     * <p>This method combines an arbitrary number of byte matrices by applying the binary operator
     * sequentially across all matrices at each position. For a collection of matrices [m1, m2, m3, ...],
     * the result at position (i, j) is computed as:</p>
     * <pre>{@code
     * result[i][j] = zipFunction(zipFunction(m1[i][j], m2[i][j]), m3[i][j])...
     * }</pre>
     *
     * <p>All matrices in the collection must have identical dimensions. The operation is optimized
     * for single and two-element collections:</p>
     * <ul>
     * <li>One matrix: Returns a copy of that matrix</li>
     * <li>Two matrices: Directly applies the binary operator</li>
     * <li>Three or more: Applies the operator sequentially, accumulating results</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<ByteMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Element-wise maximum across all matrices
     * ByteMatrix max = Matrixes.zip(matrices, (a, b) -> (byte)Math.max(a, b));
     *
     * // Element-wise sum across all matrices
     * ByteMatrix sum = Matrixes.zip(matrices, (a, b) -> (byte)(a + b));
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the binary operator to combine elements sequentially
     * @return a new {@link ByteMatrix} containing the combined results
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> ByteMatrix zip(final Collection<ByteMatrix> c, final Throwables.ByteBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final ByteMatrix[] matrixes = c.toArray(new ByteMatrix[size]);

        if (c.size() == 1) {
            return matrixes[0].copy();
        } else if (c.size() == 2) {
            return matrixes[0].zipWith(matrixes[1], zipFunction);
        }

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final byte[][] result = new byte[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final byte[] ret = result[i];
            ret[j] = matrixes[0].a[i][j];

            for (int k = 1; k < size; k++) {
                ret[j] = zipFunction.applyAsByte(ret[j], matrixes[k].a[i][j]);
            }
        };

        run(rows, cols, cmd, Matrixes.isParallelable(matrixes[0]));

        return new ByteMatrix(result);
    }

    /**
     * Combines multiple {@link ByteMatrix} objects element-wise using a function that operates on byte arrays.
     *
     * <p>This method combines an arbitrary number of byte matrices by applying a function that takes
     * an array of bytes (one from each matrix at each position) and produces a result of any type.
     * At each position (i, j), an array containing [m1[i][j], m2[i][j], m3[i][j], ...] is passed
     * to the zip function.</p>
     *
     * <p>This is a convenience method that calls
     * {@link #zip(Collection, Throwables.ByteNFunction, boolean, Class)} with
     * {@code shareIntermediateArray = false}.</p>
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of bytes (one from each matrix) and returns a result of type R
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see #zip(Collection, Throwables.ByteNFunction, boolean, Class)
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<ByteMatrix> c, final Throwables.ByteNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     * Combines multiple {@link ByteMatrix} objects element-wise using a function that operates on byte arrays,
     * with control over intermediate array sharing.
     *
     * <p>This method combines byte matrices by applying a function that takes an array of bytes
     * (one from each matrix at each position). The {@code shareIntermediateArray} parameter controls
     * memory optimization:</p>
     * <ul>
     * <li>{@code true} and sequential execution: Reuses the same intermediate array for all positions,
     *     reducing memory allocations but requiring the zip function to not retain references to the array</li>
     * <li>{@code false} or parallel execution: Creates a new array for each position, safer but uses more memory</li>
     * </ul>
     *
     * <p><b>Warning:</b> When {@code shareIntermediateArray} is {@code true}, the zip function must NOT
     * store references to the array, as it will be mutated for subsequent positions. Only use this
     * optimization if the function immediately processes and discards the array.</p>
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of bytes (one from each matrix) and returns a result of type R
     * @param shareIntermediateArray {@code true} to reuse the intermediate array (sequential execution only);
     *                               {@code false} to create new arrays for each position
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<ByteMatrix> c, final Throwables.ByteNFunction<? extends R, E> zipFunction,
            final boolean shareIntermediateArray, final Class<R> targetElementType) throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final ByteMatrix[] matrixes = c.toArray(new ByteMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && !zipInParallel;
        final byte[] intermediateArray = new byte[size];
        final R[][] result = newArray(rows, cols, targetElementType);

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final byte[] tmp = shareArray ? intermediateArray : N.clone(intermediateArray);

            for (int k = 0; k < size; k++) {
                tmp[k] = matrixes[k].a[i][j];
            }

            result[i][j] = zipFunction.apply(tmp);
        };

        run(rows, cols, cmd, zipInParallel);

        return new Matrix<>(result);
    }

    /**
     * Combines two {@link ByteMatrix} objects element-wise using a function that returns {@code Integer} values,
     * producing an {@link IntMatrix}.
     *
     * <p>This method performs element-wise combination of two byte matrices using a function that
     * takes two {@code byte} values and returns an {@code Integer}. The result is collected into
     * an {@link IntMatrix}. This is useful for operations that widen from bytes to integers, such
     * as computing sums or differences that may exceed byte range.</p>
     *
     * <p>Both matrices must have identical dimensions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteMatrix m1 = ByteMatrix.of(new byte[][] {{100, 120}, {-50, 80}});
     * ByteMatrix m2 = ByteMatrix.of(new byte[][] {{60, 40}, {-30, 90}});
     *
     * // Compute sum as integers (to avoid byte overflow)
     * IntMatrix sum = Matrixes.zipToInt(m1, m2, (a, b) -> (int)a + (int)b);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements, takes two bytes and returns an Integer
     * @return a new {@link IntMatrix} with the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> IntMatrix zipToInt(final ByteMatrix a, final ByteMatrix b, final Throwables.ByteBiFunction<Integer, E> zipFunction)
            throws E {
        checkShapeForZip(a, b);

        final int rows = a.rows;
        final int cols = a.cols;
        final byte[][] aa = a.a;
        final byte[][] ba = b.a;
        final int[][] result = new int[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);

        run(rows, cols, cmd, Matrixes.isParallelable(a));

        return new IntMatrix(result);
    }

    /**
     * Combines three {@link ByteMatrix} objects element-wise using a function that returns {@code Integer} values,
     * producing an {@link IntMatrix}.
     *
     * <p>This method performs element-wise combination of three byte matrices using a function that
     * takes three {@code byte} values and returns an {@code Integer}. For each position (i, j), the
     * function is called with elements from all three matrices:
     * {@code zipFunction.apply(a[i][j], b[i][j], c[i][j])}. The result is collected into an
     * {@link IntMatrix}.</p>
     *
     * <p>This is useful for ternary operations that widen from bytes to integers, avoiding byte
     * overflow and providing greater precision.</p>
     *
     * <p>All three matrices must have identical dimensions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteMatrix m1 = ByteMatrix.of(new byte[][] {{10, 20}, {30, 40}});
     * ByteMatrix m2 = ByteMatrix.of(new byte[][] {{5, 10}, {15, 20}});
     * ByteMatrix m3 = ByteMatrix.of(new byte[][] {{2, 3}, {4, 5}});
     *
     * // Compute weighted sum: a*2 + b*3 + c
     * IntMatrix result = Matrixes.zipToInt(m1, m2, m3, (a, b, c) -> a*2 + b*3 + c);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param c the third matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements, takes three bytes and returns an Integer
     * @return a new {@link IntMatrix} with the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> IntMatrix zipToInt(final ByteMatrix a, final ByteMatrix b, final ByteMatrix c,
            final Throwables.ByteTriFunction<Integer, E> zipFunction) throws IllegalArgumentException, E {
        checkShapeForZip(a, b);
        checkShapeForZip(a, c);

        final int rows = a.rows;
        final int cols = a.cols;
        final byte[][] aa = a.a;
        final byte[][] ba = b.a;
        final byte[][] ca = c.a;
        final int[][] result = new int[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);

        run(rows, cols, cmd, Matrixes.isParallelable(a));

        return new IntMatrix(result);
    }

    /**
     * Combines multiple {@link ByteMatrix} objects element-wise using a function that returns {@code Integer} values,
     * producing an {@link IntMatrix}.
     *
     * <p>This method combines an arbitrary number of byte matrices by applying a function that takes
     * an array of bytes (one from each matrix at each position) and returns an {@code Integer}.
     * At each position (i, j), an array containing [m1[i][j], m2[i][j], m3[i][j], ...] is passed
     * to the zip function.</p>
     *
     * <p>This is a convenience method that calls
     * {@link #zipToInt(Collection, Throwables.ByteNFunction, boolean)} with
     * {@code shareIntermediateArray = false}.</p>
     *
     * <p>All matrices in the collection must have identical dimensions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<ByteMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Compute sum as integers (avoiding byte overflow)
     * IntMatrix sum = Matrixes.zipToInt(matrices, arr -> {
     *     int total = 0;
     *     for (byte b : arr) total += b;
     *     return total;
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of bytes and returns an Integer
     * @return a new {@link IntMatrix} with the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see #zipToInt(Collection, Throwables.ByteNFunction, boolean)
     */
    public static <E extends Exception> IntMatrix zipToInt(final Collection<ByteMatrix> c, final Throwables.ByteNFunction<Integer, E> zipFunction) throws E {
        return zipToInt(c, zipFunction, false);
    }

    /**
     * Combines multiple {@link ByteMatrix} objects element-wise using a function that returns {@code Integer} values,
     * with control over intermediate array sharing.
     *
     * <p>This method combines byte matrices by applying a function that takes an array of bytes
     * (one from each matrix at each position) and returns an {@code Integer}. The {@code shareIntermediateArray}
     * parameter controls memory optimization:</p>
     * <ul>
     * <li>{@code true} and sequential execution: Reuses the same intermediate array for all positions,
     *     reducing memory allocations but requiring the zip function to not retain references to the array</li>
     * <li>{@code false} or parallel execution: Creates a new array for each position, safer but uses more memory</li>
     * </ul>
     *
     * <p><b>Warning:</b> When {@code shareIntermediateArray} is {@code true}, the zip function must NOT
     * store references to the array, as it will be mutated for subsequent positions. Only use this
     * optimization if the function immediately processes and discards the array.</p>
     *
     * <p>All matrices in the collection must have identical dimensions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<ByteMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Compute average as integer (safe with shareIntermediateArray = true)
     * IntMatrix avg = Matrixes.zipToInt(matrices, arr -> {
     *     int sum = 0;
     *     for (byte b : arr) sum += b;
     *     return sum / arr.length;
     * }, true);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of bytes and returns an Integer
     * @param shareIntermediateArray {@code true} to reuse the intermediate array (sequential execution only);
     *                               {@code false} to create new arrays for each position
     * @return a new {@link IntMatrix} with the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> IntMatrix zipToInt(final Collection<ByteMatrix> c, final Throwables.ByteNFunction<Integer, E> zipFunction,
            final boolean shareIntermediateArray) throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final ByteMatrix[] matrixes = c.toArray(new ByteMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && !zipInParallel;
        final byte[] intermediateArray = new byte[size];
        final int[][] result = new int[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final byte[] tmp = shareArray ? intermediateArray : N.clone(intermediateArray);

            for (int k = 0; k < size; k++) {
                tmp[k] = matrixes[k].a[i][j];
            }

            result[i][j] = zipFunction.apply(tmp);
        };

        run(rows, cols, cmd, zipInParallel);

        return new IntMatrix(result);
    }

    /**
     * Combines two {@link IntMatrix} objects element-wise using a binary operator.
     *
     * <p>This method performs element-wise combination of two integer matrices using the provided
     * binary operator. For each position (i, j), the function is called with the corresponding
     * elements from both matrices: {@code zipFunction.apply(a[i][j], b[i][j])}.</p>
     *
     * <p>Both matrices must have identical dimensions (same number of rows and columns).
     * The operation delegates to the {@link IntMatrix#zipWith} method.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix m2 = IntMatrix.of(new int[][] {{5, 6}, {7, 8}});
     *
     * // Element-wise addition
     * IntMatrix sum = Matrixes.zip(m1, m2, (a, b) -> a + b);
     * // Result: [[6, 8], [10, 12]]
     *
     * // Element-wise maximum
     * IntMatrix max = Matrixes.zip(m1, m2, Integer::max);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the binary operator to combine corresponding elements from both matrices
     * @return a new {@link IntMatrix} containing the results of applying the function to each pair of elements
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see IntMatrix#zipWith(IntMatrix, Throwables.IntBinaryOperator)
     */
    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     * Combines three {@link IntMatrix} objects element-wise using a ternary operator.
     *
     * <p>This method performs element-wise combination of three integer matrices using the provided
     * ternary operator. For each position (i, j), the function is called with the corresponding
     * elements from all three matrices: {@code zipFunction.apply(a[i][j], b[i][j], c[i][j])}.</p>
     *
     * <p>All three matrices must have identical dimensions (same number of rows and columns).
     * The operation delegates to the {@link IntMatrix#zipWith} method.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][] {{1, 2}, {3, 4}});
     * IntMatrix m2 = IntMatrix.of(new int[][] {{5, 6}, {7, 8}});
     * IntMatrix m3 = IntMatrix.of(new int[][] {{10, 20}, {30, 40}});
     *
     * // Compute weighted sum: a*2 + b*3 + c
     * IntMatrix result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a*2 + b*3 + c);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param c the third matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the ternary operator to combine corresponding elements from all three matrices
     * @return a new {@link IntMatrix} containing the results of applying the function to each triple of elements
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see IntMatrix#zipWith(IntMatrix, IntMatrix, Throwables.IntTernaryOperator)
     */
    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     * Combines multiple {@link IntMatrix} objects element-wise using a binary operator applied sequentially.
     *
     * <p>This method combines an arbitrary number of integer matrices by applying the binary operator
     * sequentially across all matrices at each position. For a collection of matrices [m1, m2, m3, ...],
     * the result at position (i, j) is computed as:</p>
     * <pre>{@code
     * result[i][j] = zipFunction(zipFunction(m1[i][j], m2[i][j]), m3[i][j])...
     * }</pre>
     *
     * <p>All matrices in the collection must have identical dimensions. The operation is optimized
     * for single and two-element collections:</p>
     * <ul>
     * <li>One matrix: Returns a copy of that matrix</li>
     * <li>Two matrices: Directly applies the binary operator</li>
     * <li>Three or more: Applies the operator sequentially, accumulating results</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<IntMatrix> matrices = Arrays.asList(m1, m2, m3, m4);
     *
     * // Element-wise maximum across all matrices
     * IntMatrix max = Matrixes.zip(matrices, Integer::max);
     *
     * // Element-wise sum across all matrices
     * IntMatrix sum = Matrixes.zip(matrices, (a, b) -> a + b);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the binary operator to combine elements sequentially
     * @return a new {@link IntMatrix} containing the combined results
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> IntMatrix zip(final Collection<IntMatrix> c, final Throwables.IntBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final IntMatrix[] matrixes = c.toArray(new IntMatrix[size]);

        if (c.size() == 1) {
            return matrixes[0].copy();
        } else if (c.size() == 2) {
            return matrixes[0].zipWith(matrixes[1], zipFunction);
        }

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final int[][] result = new int[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final int[] ret = result[i];
            ret[j] = matrixes[0].a[i][j];

            for (int k = 1; k < size; k++) {
                ret[j] = zipFunction.applyAsInt(ret[j], matrixes[k].a[i][j]);
            }
        };

        run(rows, cols, cmd, Matrixes.isParallelable(matrixes[0]));

        return new IntMatrix(result);
    }

    /**
     * Combines multiple {@link IntMatrix} objects element-wise using a function that operates on integer arrays.
     *
     * <p>This method combines an arbitrary number of integer matrices by applying a function that takes
     * an array of integers (one from each matrix at each position) and produces a result of any type.
     * At each position (i, j), an array containing [m1[i][j], m2[i][j], m3[i][j], ...] is passed
     * to the zip function.</p>
     *
     * <p>This is a convenience method that calls
     * {@link #zip(Collection, Throwables.IntNFunction, boolean, Class)} with
     * {@code shareIntermediateArray = false}.</p>
     *
     * <p>All matrices in the collection must have identical dimensions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<IntMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Compute statistics at each position
     * Matrix<String> stats = Matrixes.zip(matrices,
     *     arr -> "avg=" + Arrays.stream(arr).average().orElse(0),
     *     String.class);
     * }</pre>
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of integers (one from each matrix) and returns a result of type R
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see #zip(Collection, Throwables.IntNFunction, boolean, Class)
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<IntMatrix> c, final Throwables.IntNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     * Combines multiple {@link IntMatrix} objects element-wise using a function that operates on integer arrays,
     * with control over intermediate array sharing.
     *
     * <p>This method combines integer matrices by applying a function that takes an array of integers
     * (one from each matrix at each position). The {@code shareIntermediateArray} parameter controls
     * memory optimization:</p>
     * <ul>
     * <li>{@code true} and sequential execution: Reuses the same intermediate array for all positions,
     *     reducing memory allocations but requiring the zip function to not retain references to the array</li>
     * <li>{@code false} or parallel execution: Creates a new array for each position, safer but uses more memory</li>
     * </ul>
     *
     * <p><b>Warning:</b> When {@code shareIntermediateArray} is {@code true}, the zip function must NOT
     * store references to the array, as it will be mutated for subsequent positions. Only use this
     * optimization if the function immediately processes and discards the array.</p>
     *
     * <p>All matrices in the collection must have identical dimensions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<IntMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Compute median at each position (safe with shareIntermediateArray = false)
     * Matrix<Double> median = Matrixes.zip(matrices, arr -> {
     *     int[] sorted = Arrays.copyOf(arr, arr.length);
     *     Arrays.sort(sorted);
     *     return (double) sorted[sorted.length / 2];
     * }, false, Double.class);
     * }</pre>
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of integers (one from each matrix) and returns a result of type R
     * @param shareIntermediateArray {@code true} to reuse the intermediate array (sequential execution only);
     *                               {@code false} to create new arrays for each position
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<IntMatrix> c, final Throwables.IntNFunction<? extends R, E> zipFunction,
            final boolean shareIntermediateArray, final Class<R> targetElementType) throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final IntMatrix[] matrixes = c.toArray(new IntMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && !zipInParallel;
        final int[] intermediateArray = new int[size];
        final R[][] result = newArray(rows, cols, targetElementType);

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final int[] tmp = shareArray ? intermediateArray : N.clone(intermediateArray);

            for (int k = 0; k < size; k++) {
                tmp[k] = matrixes[k].a[i][j];
            }

            result[i][j] = zipFunction.apply(tmp);
        };

        run(rows, cols, cmd, zipInParallel);

        return new Matrix<>(result);
    }

    /**
     * Combines two {@link IntMatrix} objects element-wise using a function that returns {@code Long} values,
     * producing a {@link LongMatrix}.
     *
     * <p>This method performs element-wise combination of two integer matrices using a function that
     * takes two {@code int} values and returns a {@code Long}. This is useful for operations that may
     * exceed integer range, such as computing products or sums of large integers.</p>
     *
     * <p>Both matrices must have identical dimensions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][] {{1000000, 2000000}, {3000000, 4000000}});
     * IntMatrix m2 = IntMatrix.of(new int[][] {{5000000, 6000000}, {7000000, 8000000}});
     *
     * // Compute product as longs (to avoid integer overflow)
     * LongMatrix product = Matrixes.zipToLong(m1, m2, (a, b) -> (long)a * (long)b);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements, takes two ints and returns a Long
     * @return a new {@link LongMatrix} with the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> LongMatrix zipToLong(final IntMatrix a, final IntMatrix b, final Throwables.IntBiFunction<Long, E> zipFunction)
            throws E {
        checkShapeForZip(a, b);

        final int rows = a.rows;
        final int cols = a.cols;
        final int[][] aa = a.a;
        final int[][] ba = b.a;
        final long[][] result = new long[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);

        run(rows, cols, cmd, Matrixes.isParallelable(a));

        return new LongMatrix(result);
    }

    /**
     * Combines three {@link IntMatrix} objects element-wise using a function that returns {@code Long} values,
     * producing a {@link LongMatrix}.
     *
     * <p>This method performs element-wise combination of three integer matrices using a function that
     * takes three {@code int} values and returns a {@code Long}. For each position (i, j), the function
     * is called with elements from all three matrices: {@code zipFunction.apply(a[i][j], b[i][j], c[i][j])}.
     * This is useful for ternary operations that may exceed integer range.</p>
     *
     * <p>All three matrices must have identical dimensions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][] {{100000, 200000}});
     * IntMatrix m2 = IntMatrix.of(new int[][] {{300000, 400000}});
     * IntMatrix m3 = IntMatrix.of(new int[][] {{500000, 600000}});
     *
     * // Compute a*b + c as long (to avoid overflow)
     * LongMatrix result = Matrixes.zipToLong(m1, m2, m3, (a, b, c) -> (long)a * b + c);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param c the third matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements, takes three ints and returns a Long
     * @return a new {@link LongMatrix} with the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> LongMatrix zipToLong(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTriFunction<Long, E> zipFunction) throws IllegalArgumentException, E {
        checkShapeForZip(a, b);
        checkShapeForZip(a, c);

        final int rows = a.rows;
        final int cols = a.cols;
        final int[][] aa = a.a;
        final int[][] ba = b.a;
        final int[][] ca = c.a;
        final long[][] result = new long[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);

        run(rows, cols, cmd, Matrixes.isParallelable(a));

        return new LongMatrix(result);
    }

    /**
     * Combines multiple {@link IntMatrix} objects element-wise using a function that returns {@code Long} values.
     *
     * <p>This method combines an arbitrary number of integer matrices by applying a function that takes
     * an array of integers (one from each matrix at each position) and returns a {@code Long}.
     * This is useful for aggregation operations that may exceed integer range.</p>
     *
     * <p>This is a convenience method that calls
     * {@link #zipToLong(Collection, Throwables.IntNFunction, boolean)} with
     * {@code shareIntermediateArray = false}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<IntMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Sum all values as long (avoiding overflow)
     * LongMatrix sum = Matrixes.zipToLong(matrices, arr -> {
     *     long total = 0;
     *     for (int i : arr) total += i;
     *     return total;
     * });
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of integers and returns a Long
     * @return a new {@link LongMatrix} with the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see #zipToLong(Collection, Throwables.IntNFunction, boolean)
     */
    public static <E extends Exception> LongMatrix zipToLong(final Collection<IntMatrix> c, final Throwables.IntNFunction<Long, E> zipFunction) throws E {
        return zipToLong(c, zipFunction, false);
    }

    /**
     * Combines multiple {@link IntMatrix} objects element-wise using a function that returns {@code Long} values,
     * with control over intermediate array sharing.
     *
     * <p>This method combines integer matrices by applying a function that takes an array of integers
     * (one from each matrix at each position) and returns a {@code Long}. The {@code shareIntermediateArray}
     * parameter controls memory optimization as described in other zip methods.</p>
     *
     * <p><b>Warning:</b> When {@code shareIntermediateArray} is {@code true}, the zip function must NOT
     * store references to the array, as it will be mutated for subsequent positions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<IntMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Compute product of all values as long
     * LongMatrix product = Matrixes.zipToLong(matrices, arr -> {
     *     long result = 1L;
     *     for (int i : arr) result *= i;
     *     return result;
     * }, true);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of integers and returns a Long
     * @param shareIntermediateArray {@code true} to reuse the intermediate array (sequential execution only);
     *                               {@code false} to create new arrays for each position
     * @return a new {@link LongMatrix} with the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> LongMatrix zipToLong(final Collection<IntMatrix> c, final Throwables.IntNFunction<Long, E> zipFunction,
            final boolean shareIntermediateArray) throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final IntMatrix[] matrixes = c.toArray(new IntMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && !zipInParallel;
        final int[] intermediateArray = new int[size];
        final long[][] result = new long[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final int[] tmp = shareArray ? intermediateArray : N.clone(intermediateArray);

            for (int k = 0; k < size; k++) {
                tmp[k] = matrixes[k].a[i][j];
            }

            result[i][j] = zipFunction.apply(tmp);
        };

        run(rows, cols, cmd, zipInParallel);

        return new LongMatrix(result);
    }

    /**
     * Combines two {@link IntMatrix} objects element-wise using a function that returns {@code Double} values,
     * producing a {@link DoubleMatrix}.
     *
     * <p>This method performs element-wise combination of two integer matrices using a function that
     * takes two {@code int} values and returns a {@code Double}. This is useful for operations requiring
     * floating-point precision, such as division or statistical calculations.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][] {{10, 20}, {30, 40}});
     * IntMatrix m2 = IntMatrix.of(new int[][] {{3, 4}, {5, 6}});
     *
     * // Compute division with double precision
     * DoubleMatrix ratio = Matrixes.zipToDouble(m1, m2, (a, b) -> (double)a / b);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements, takes two ints and returns a Double
     * @return a new {@link DoubleMatrix} with the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final IntMatrix a, final IntMatrix b, final Throwables.IntBiFunction<Double, E> zipFunction)
            throws E {
        checkShapeForZip(a, b);

        final int rows = a.rows;
        final int cols = a.cols;
        final int[][] aa = a.a;
        final int[][] ba = b.a;
        final double[][] result = new double[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);

        run(rows, cols, cmd, Matrixes.isParallelable(a));

        return new DoubleMatrix(result);
    }

    /**
     * Combines three {@link IntMatrix} objects element-wise using a function that returns {@code Double} values,
     * producing a {@link DoubleMatrix}.
     *
     * <p>This method performs element-wise combination of three integer matrices using a function that
     * takes three {@code int} values and returns a {@code Double}. For each position (i, j), the function
     * is called with elements from all three matrices.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][] {{10, 20}});
     * IntMatrix m2 = IntMatrix.of(new int[][] {{3, 4}});
     * IntMatrix m3 = IntMatrix.of(new int[][] {{2, 5}});
     *
     * // Compute (a + b) / c with double precision
     * DoubleMatrix result = Matrixes.zipToDouble(m1, m2, m3, (a, b, c) -> (double)(a + b) / c);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param c the third matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements, takes three ints and returns a Double
     * @return a new {@link DoubleMatrix} with the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTriFunction<Double, E> zipFunction) throws IllegalArgumentException, E {
        checkShapeForZip(a, b);
        checkShapeForZip(a, c);

        final int rows = a.rows;
        final int cols = a.cols;
        final int[][] aa = a.a;
        final int[][] ba = b.a;
        final int[][] ca = c.a;
        final double[][] result = new double[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);

        run(rows, cols, cmd, Matrixes.isParallelable(a));

        return new DoubleMatrix(result);
    }

    /**
     * Combines multiple {@link IntMatrix} objects element-wise using a function that returns {@code Double} values.
     *
     * <p>This method combines an arbitrary number of integer matrices by applying a function that takes
     * an array of integers (one from each matrix at each position) and returns a {@code Double}.
     * This is a convenience method that delegates with {@code shareIntermediateArray = false}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<IntMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Compute average as double
     * DoubleMatrix avg = Matrixes.zipToDouble(matrices,
     *     arr -> Arrays.stream(arr).average().orElse(0.0));
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of integers and returns a Double
     * @return a new {@link DoubleMatrix} with the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see #zipToDouble(Collection, Throwables.IntNFunction, boolean)
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<IntMatrix> c, final Throwables.IntNFunction<Double, E> zipFunction)
            throws IllegalArgumentException, E {
        return zipToDouble(c, zipFunction, false);
    }

    /**
     * Combines multiple {@link IntMatrix} objects element-wise using a function that returns {@code Double} values,
     * with control over intermediate array sharing.
     *
     * <p>This method combines integer matrices by applying a function that takes an array of integers
     * (one from each matrix at each position) and returns a {@code Double}. The {@code shareIntermediateArray}
     * parameter controls memory optimization as described in other zip methods.</p>
     *
     * <p><b>Warning:</b> When {@code shareIntermediateArray} is {@code true}, the zip function must NOT
     * store references to the array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<IntMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Compute standard deviation at each position
     * DoubleMatrix stdDev = Matrixes.zipToDouble(matrices, arr -> {
     *     double avg = Arrays.stream(arr).average().orElse(0);
     *     double variance = Arrays.stream(arr).mapToDouble(i -> Math.pow(i - avg, 2)).average().orElse(0);
     *     return Math.sqrt(variance);
     * }, true);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of integers and returns a Double
     * @param shareIntermediateArray {@code true} to reuse the intermediate array (sequential execution only);
     *                               {@code false} to create new arrays for each position
     * @return a new {@link DoubleMatrix} with the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<IntMatrix> c, final Throwables.IntNFunction<Double, E> zipFunction,
            final boolean shareIntermediateArray) throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final IntMatrix[] matrixes = c.toArray(new IntMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && !zipInParallel;
        final int[] intermediateArray = new int[size];
        final double[][] result = new double[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final int[] tmp = shareArray ? intermediateArray : N.clone(intermediateArray);

            for (int k = 0; k < size; k++) {
                tmp[k] = matrixes[k].a[i][j];
            }

            result[i][j] = zipFunction.apply(tmp);
        };

        run(rows, cols, cmd, zipInParallel);

        return new DoubleMatrix(result);
    }

    /**
     * Combines two {@link LongMatrix} objects element-wise using a binary operator.
     *
     * <p>This method performs element-wise combination of two long matrices using the provided
     * binary operator. For each position (i, j), the function is called with the corresponding
     * elements from both matrices. Both matrices must have identical dimensions.
     * The operation delegates to the {@link LongMatrix#zipWith} method.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix m1 = LongMatrix.of(new long[][] {{100L, 200L}, {300L, 400L}});
     * LongMatrix m2 = LongMatrix.of(new long[][] {{50L, 60L}, {70L, 80L}});
     *
     * // Element-wise addition
     * LongMatrix sum = Matrixes.zip(m1, m2, (a, b) -> a + b);
     *
     * // Element-wise maximum
     * LongMatrix max = Matrixes.zip(m1, m2, Long::max);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the binary operator to combine corresponding elements from both matrices
     * @return a new {@link LongMatrix} containing the results of applying the function to each pair of elements
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see LongMatrix#zipWith(LongMatrix, Throwables.LongBinaryOperator)
     */
    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     * Combines three {@link LongMatrix} objects element-wise using a ternary operator.
     *
     * <p>This method performs element-wise combination of three long matrices using the provided
     * ternary operator. For each position (i, j), the function is called with the corresponding
     * elements from all three matrices. All three matrices must have identical dimensions.
     * The operation delegates to the {@link LongMatrix#zipWith} method.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix m1 = LongMatrix.of(new long[][] {{1L, 2L}});
     * LongMatrix m2 = LongMatrix.of(new long[][] {{3L, 4L}});
     * LongMatrix m3 = LongMatrix.of(new long[][] {{5L, 6L}});
     *
     * // Compute a*b + c
     * LongMatrix result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a * b + c);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param c the third matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the ternary operator to combine corresponding elements from all three matrices
     * @return a new {@link LongMatrix} containing the results of applying the function to each triple of elements
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see LongMatrix#zipWith(LongMatrix, LongMatrix, Throwables.LongTernaryOperator)
     */
    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final LongMatrix c,
            final Throwables.LongTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     * Combines multiple {@link LongMatrix} objects element-wise using a binary operator applied sequentially.
     *
     * <p>This method combines an arbitrary number of long matrices by applying the binary operator
     * sequentially across all matrices at each position. The operation is optimized for single and
     * two-element collections. All matrices in the collection must have identical dimensions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<LongMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Element-wise sum
     * LongMatrix sum = Matrixes.zip(matrices, (a, b) -> a + b);
     *
     * // Element-wise minimum
     * LongMatrix min = Matrixes.zip(matrices, Long::min);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the binary operator to combine elements sequentially
     * @return a new {@link LongMatrix} containing the combined results
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> LongMatrix zip(final Collection<LongMatrix> c, final Throwables.LongBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final LongMatrix[] matrixes = c.toArray(new LongMatrix[size]);

        if (c.size() == 1) {
            return matrixes[0].copy();
        } else if (c.size() == 2) {
            return matrixes[0].zipWith(matrixes[1], zipFunction);
        }

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final long[][] result = new long[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final long[] ret = result[i];
            ret[j] = matrixes[0].a[i][j];

            for (int k = 1; k < size; k++) {
                ret[j] = zipFunction.applyAsLong(ret[j], matrixes[k].a[i][j]);
            }
        };

        run(rows, cols, cmd, Matrixes.isParallelable(matrixes[0]));

        return new LongMatrix(result);
    }

    /**
     * Combines multiple {@link LongMatrix} objects element-wise using a function that operates on long arrays.
     *
     * <p>This method combines an arbitrary number of long matrices by applying a function that takes
     * an array of longs (one from each matrix at each position) and produces a result of any type.
     * This is a convenience method that delegates with {@code shareIntermediateArray = false}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<LongMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Find the range (max - min) at each position
     * Matrix<Long> range = Matrixes.zip(matrices, arr -> {
     *     long max = Arrays.stream(arr).max().orElse(0L);
     *     long min = Arrays.stream(arr).min().orElse(0L);
     *     return max - min;
     * }, Long.class);
     * }</pre>
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of longs (one from each matrix) and returns a result of type R
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see #zip(Collection, Throwables.LongNFunction, boolean, Class)
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<LongMatrix> c, final Throwables.LongNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     * Combines multiple {@link LongMatrix} objects element-wise using a function that operates on long arrays,
     * with control over intermediate array sharing.
     *
     * <p>This method combines long matrices by applying a function that takes an array of longs
     * (one from each matrix at each position). The {@code shareIntermediateArray} parameter controls
     * memory optimization as described in other zip methods.</p>
     *
     * <p><b>Warning:</b> When {@code shareIntermediateArray} is {@code true}, the zip function must NOT
     * store references to the array, as it will be mutated for subsequent positions. Only use this
     * optimization if the function immediately processes and discards the array.</p>
     *
     * <p>All matrices in the collection must have identical dimensions.</p>
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of longs (one from each matrix) and returns a result of type R
     * @param shareIntermediateArray {@code true} to reuse the intermediate array (sequential execution only);
     *                               {@code false} to create new arrays for each position
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<LongMatrix> c, final Throwables.LongNFunction<? extends R, E> zipFunction,
            final boolean shareIntermediateArray, final Class<R> targetElementType) throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final LongMatrix[] matrixes = c.toArray(new LongMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && !zipInParallel;
        final long[] intermediateArray = new long[size];
        final R[][] result = newArray(rows, cols, targetElementType);

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final long[] tmp = shareArray ? intermediateArray : N.clone(intermediateArray);

            for (int k = 0; k < size; k++) {
                tmp[k] = matrixes[k].a[i][j];
            }

            result[i][j] = zipFunction.apply(tmp);
        };

        run(rows, cols, cmd, zipInParallel);

        return new Matrix<>(result);
    }

    /**
     * Combines two {@link LongMatrix} objects element-wise using a function that returns {@code Double} values,
     * producing a {@link DoubleMatrix}.
     *
     * <p>This method performs element-wise combination of two long matrices using a function that
     * takes two {@code long} values and returns a {@code Double}. This is useful for operations requiring
     * floating-point precision, such as division or statistical calculations on long values.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix m1 = LongMatrix.of(new long[][] {{100L, 200L}, {300L, 400L}});
     * LongMatrix m2 = LongMatrix.of(new long[][] {{3L, 4L}, {5L, 6L}});
     *
     * // Compute division with double precision
     * DoubleMatrix ratio = Matrixes.zipToDouble(m1, m2, (a, b) -> (double)a / b);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements, takes two longs and returns a Double
     * @return a new {@link DoubleMatrix} with the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final LongMatrix a, final LongMatrix b, final Throwables.LongBiFunction<Double, E> zipFunction)
            throws E {
        checkShapeForZip(a, b);

        final int rows = a.rows;
        final int cols = a.cols;
        final long[][] aa = a.a;
        final long[][] ba = b.a;
        final double[][] result = new double[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);

        run(rows, cols, cmd, Matrixes.isParallelable(a));

        return new DoubleMatrix(result);
    }

    /**
     * Combines three {@link LongMatrix} objects element-wise using a function that returns {@code Double} values,
     * producing a {@link DoubleMatrix}.
     *
     * <p>This method performs element-wise combination of three long matrices using a function that
     * takes three {@code long} values and returns a {@code Double}. For each position (i, j), the function
     * is called with elements from all three matrices.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongMatrix m1 = LongMatrix.of(new long[][] {{100L, 200L}});
     * LongMatrix m2 = LongMatrix.of(new long[][] {{10L, 20L}});
     * LongMatrix m3 = LongMatrix.of(new long[][] {{3L, 4L}});
     *
     * // Compute (a + b) / c with double precision
     * DoubleMatrix result = Matrixes.zipToDouble(m1, m2, m3, (a, b, c) -> (double)(a + b) / c);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param c the third matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements, takes three longs and returns a Double
     * @return a new {@link DoubleMatrix} with the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final LongMatrix a, final LongMatrix b, final LongMatrix c,
            final Throwables.LongTriFunction<Double, E> zipFunction) throws IllegalArgumentException, E {
        checkShapeForZip(a, b);
        checkShapeForZip(a, c);

        final int rows = a.rows;
        final int cols = a.cols;
        final long[][] aa = a.a;
        final long[][] ba = b.a;
        final long[][] ca = c.a;
        final double[][] result = new double[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);

        run(rows, cols, cmd, Matrixes.isParallelable(a));

        return new DoubleMatrix(result);
    }

    /**
     * Combines multiple {@link LongMatrix} objects element-wise using a function that returns {@code Double} values.
     *
     * <p>This method combines an arbitrary number of long matrices by applying a function that takes
     * an array of longs (one from each matrix at each position) and returns a {@code Double}.
     * This is a convenience method that delegates with {@code shareIntermediateArray = false}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<LongMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Compute average as double
     * DoubleMatrix avg = Matrixes.zipToDouble(matrices,
     *     arr -> Arrays.stream(arr).average().orElse(0.0));
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of longs and returns a Double
     * @return a new {@link DoubleMatrix} with the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see #zipToDouble(Collection, Throwables.LongNFunction, boolean)
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<LongMatrix> c, final Throwables.LongNFunction<Double, E> zipFunction)
            throws E {
        return zipToDouble(c, zipFunction, false);
    }

    /**
     * Combines multiple {@link LongMatrix} objects element-wise using a function that returns {@code Double} values,
     * with control over intermediate array sharing.
     *
     * <p>This method combines long matrices by applying a function that takes an array of longs
     * (one from each matrix at each position) and returns a {@code Double}. The {@code shareIntermediateArray}
     * parameter controls memory optimization as described in other zip methods.</p>
     *
     * <p><b>Warning:</b> When {@code shareIntermediateArray} is {@code true}, the zip function must NOT
     * store references to the array.</p>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of longs and returns a Double
     * @param shareIntermediateArray {@code true} to reuse the intermediate array (sequential execution only);
     *                               {@code false} to create new arrays for each position
     * @return a new {@link DoubleMatrix} with the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<LongMatrix> c, final Throwables.LongNFunction<Double, E> zipFunction,
            final boolean shareIntermediateArray) throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final LongMatrix[] matrixes = c.toArray(new LongMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && !zipInParallel;
        final long[] intermediateArray = new long[size];
        final double[][] result = new double[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final long[] tmp = shareArray ? intermediateArray : N.clone(intermediateArray);

            for (int k = 0; k < size; k++) {
                tmp[k] = matrixes[k].a[i][j];
            }

            result[i][j] = zipFunction.apply(tmp);
        };

        run(rows, cols, cmd, zipInParallel);

        return new DoubleMatrix(result);
    }

    /**
     * Combines two {@link DoubleMatrix} objects element-wise using a binary operator.
     *
     * <p>This method performs element-wise combination of two double matrices using the provided
     * binary operator. For each position (i, j), the function is called with the corresponding
     * elements from both matrices. Both matrices must have identical dimensions.
     * The operation delegates to the {@link DoubleMatrix#zipWith} method.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix m1 = DoubleMatrix.of(new double[][] {{1.5, 2.5}, {3.5, 4.5}});
     * DoubleMatrix m2 = DoubleMatrix.of(new double[][] {{0.5, 1.0}, {1.5, 2.0}});
     *
     * // Element-wise multiplication
     * DoubleMatrix product = Matrixes.zip(m1, m2, (a, b) -> a * b);
     *
     * // Element-wise power
     * DoubleMatrix power = Matrixes.zip(m1, m2, Math::pow);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the binary operator to combine corresponding elements from both matrices
     * @return a new {@link DoubleMatrix} containing the results of applying the function to each pair of elements
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see DoubleMatrix#zipWith(DoubleMatrix, Throwables.DoubleBinaryOperator)
     */
    public static <E extends Exception> DoubleMatrix zip(final DoubleMatrix a, final DoubleMatrix b, final Throwables.DoubleBinaryOperator<E> zipFunction)
            throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     * Combines three {@link DoubleMatrix} objects element-wise using a ternary operator.
     *
     * <p>This method performs element-wise combination of three double matrices using the provided
     * ternary operator. For each position (i, j), the function is called with the corresponding
     * elements from all three matrices. All three matrices must have identical dimensions.
     * The operation delegates to the {@link DoubleMatrix#zipWith} method.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleMatrix m1 = DoubleMatrix.of(new double[][] {{1.0, 2.0}});
     * DoubleMatrix m2 = DoubleMatrix.of(new double[][] {{3.0, 4.0}});
     * DoubleMatrix m3 = DoubleMatrix.of(new double[][] {{0.5, 0.25}});
     *
     * // Compute (a + b) * c
     * DoubleMatrix result = Matrixes.zip(m1, m2, m3, (a, b, c) -> (a + b) * c);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param c the third matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the ternary operator to combine corresponding elements from all three matrices
     * @return a new {@link DoubleMatrix} containing the results of applying the function to each triple of elements
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see DoubleMatrix#zipWith(DoubleMatrix, DoubleMatrix, Throwables.DoubleTernaryOperator)
     */
    public static <E extends Exception> DoubleMatrix zip(final DoubleMatrix a, final DoubleMatrix b, final DoubleMatrix c,
            final Throwables.DoubleTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     * Combines multiple {@link DoubleMatrix} objects element-wise using a binary operator applied sequentially.
     *
     * <p>This method combines an arbitrary number of double matrices by applying the binary operator
     * sequentially across all matrices at each position. The operation is optimized for single and
     * two-element collections. All matrices in the collection must have identical dimensions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<DoubleMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Element-wise sum
     * DoubleMatrix sum = Matrixes.zip(matrices, (a, b) -> a + b);
     *
     * // Element-wise weighted average
     * DoubleMatrix weightedAvg = Matrixes.zip(matrices, (a, b) -> (a + b) / 2.0);
     * }</pre>
     *
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the binary operator to combine elements sequentially
     * @return a new {@link DoubleMatrix} containing the combined results
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <E extends Exception> DoubleMatrix zip(final Collection<DoubleMatrix> c, final Throwables.DoubleBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final DoubleMatrix[] matrixes = c.toArray(new DoubleMatrix[size]);

        if (c.size() == 1) {
            return matrixes[0].copy();
        } else if (c.size() == 2) {
            return matrixes[0].zipWith(matrixes[1], zipFunction);
        }

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final double[][] result = new double[rows][cols];

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final double[] ret = result[i];
            ret[j] = matrixes[0].a[i][j];

            for (int k = 1; k < size; k++) {
                ret[j] = zipFunction.applyAsDouble(ret[j], matrixes[k].a[i][j]);
            }
        };

        run(rows, cols, cmd, Matrixes.isParallelable(matrixes[0]));

        return new DoubleMatrix(result);
    }

    /**
     * Combines multiple {@link DoubleMatrix} objects element-wise using a function that operates on double arrays.
     *
     * <p>This method combines an arbitrary number of double matrices by applying a function that takes
     * an array of doubles (one from each matrix at each position) and produces a result of any type.
     * This is a convenience method that delegates with {@code shareIntermediateArray = false}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<DoubleMatrix> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Find variance at each position
     * Matrix<Double> variance = Matrixes.zip(matrices, arr -> {
     *     double mean = Arrays.stream(arr).average().orElse(0);
     *     return Arrays.stream(arr).map(v -> Math.pow(v - mean, 2)).average().orElse(0);
     * }, Double.class);
     * }</pre>
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of doubles (one from each matrix) and returns a result of type R
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see #zip(Collection, Throwables.DoubleNFunction, boolean, Class)
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<DoubleMatrix> c, final Throwables.DoubleNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     * Combines multiple {@link DoubleMatrix} objects element-wise using a function that operates on double arrays,
     * with control over intermediate array sharing.
     *
     * <p>This method combines double matrices by applying a function that takes an array of doubles
     * (one from each matrix at each position). The {@code shareIntermediateArray} parameter controls
     * memory optimization as described in other zip methods.</p>
     *
     * <p><b>Warning:</b> When {@code shareIntermediateArray} is {@code true}, the zip function must NOT
     * store references to the array, as it will be mutated for subsequent positions. Only use this
     * optimization if the function immediately processes and discards the array.</p>
     *
     * <p>All matrices in the collection must have identical dimensions.</p>
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of doubles (one from each matrix) and returns a result of type R
     * @param shareIntermediateArray {@code true} to reuse the intermediate array (sequential execution only);
     *                               {@code false} to create new arrays for each position
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<DoubleMatrix> c, final Throwables.DoubleNFunction<? extends R, E> zipFunction,
            final boolean shareIntermediateArray, final Class<R> targetElementType) throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final DoubleMatrix[] matrixes = c.toArray(new DoubleMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && !zipInParallel;
        final double[] intermediateArray = new double[size];
        final R[][] result = newArray(rows, cols, targetElementType);

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final double[] tmp = shareArray ? intermediateArray : N.clone(intermediateArray);

            for (int k = 0; k < size; k++) {
                tmp[k] = matrixes[k].a[i][j];
            }

            result[i][j] = zipFunction.apply(tmp);
        };

        run(rows, cols, cmd, zipInParallel);

        return new Matrix<>(result);
    }

    /**
     * Combines two generic {@link Matrix} objects element-wise using a binary function.
     *
     * <p>This method performs element-wise combination of two matrices with potentially different
     * element types. For each position (i, j), the function is called with elements from both matrices:
     * {@code zipFunction.apply(a[i][j], b[i][j])}. The result matrix has the same element type as
     * the first matrix.</p>
     *
     * <p>Both matrices must have identical dimensions (same number of rows and columns).
     * This operation delegates to the {@link Matrix#zipWith} method.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<String> names = Matrix.of(new String[][] {{"Alice", "Bob"}, {"Carol", "Dave"}});
     * Matrix<Integer> ages = Matrix.of(new Integer[][] {{25, 30}, {35, 40}});
     *
     * // Combine names and ages into formatted strings
     * Matrix<String> result = Matrixes.zip(names, ages,
     *     (name, age) -> name + " (age " + age + ")");
     * // Result: [["Alice (age 25)", "Bob (age 30)"], ["Carol (age 35)", "Dave (age 40)"]]
     * }</pre>
     *
     * @param <A> the element type of the first matrix and the result matrix
     * @param <B> the element type of the second matrix
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements from both matrices
     * @return a new {@link Matrix} of type A containing the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see Matrix#zipWith(Matrix, Throwables.BiFunction)
     */
    public static <A, B, E extends Exception> Matrix<A> zip(final Matrix<A> a, final Matrix<B> b,
            final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     * Combines two generic {@link Matrix} objects element-wise using a binary function, producing
     * a result matrix with a potentially different element type.
     *
     * <p>This method performs element-wise combination of two matrices with potentially different
     * element types (A and B). For each position (i, j), the function is called with elements from both matrices:
     * {@code zipFunction.apply(a[i][j], b[i][j])}. The result matrix has element type R, which may differ
     * from both input types.</p>
     *
     * <p>Both matrices must have identical dimensions (same number of rows and columns).
     * This operation delegates to the {@link Matrix#zipWith} method.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<Integer> numbers = Matrix.of(new Integer[][] {{1, 2}, {3, 4}});
     * Matrix<String> labels = Matrix.of(new String[][] {{"A", "B"}, {"C", "D"}});
     *
     * // Combine numbers and labels into formatted strings
     * Matrix<String> result = Matrixes.zip(numbers, labels,
     *     (num, label) -> label + ":" + num,
     *     String.class);
     * // Result: [["A:1", "B:2"], ["C:3", "D:4"]]
     * }</pre>
     *
     * @param <A> the element type of the first matrix
     * @param <B> the element type of the second matrix
     * @param <R> the element type of the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements from both matrices
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see Matrix#zipWith(Matrix, Throwables.BiFunction, Class)
     */
    public static <A, B, R, E extends Exception> Matrix<R> zip(final Matrix<A> a, final Matrix<B> b,
            final Throwables.BiFunction<? super A, ? super B, R, E> zipFunction, final Class<R> targetElementType) throws E {
        return a.zipWith(b, zipFunction, targetElementType);
    }

    /**
     * Combines three generic {@link Matrix} objects element-wise using a ternary function.
     *
     * <p>This method performs element-wise combination of three matrices with potentially different
     * element types. For each position (i, j), the function is called with the corresponding
     * elements from all three matrices: {@code zipFunction.apply(a[i][j], b[i][j], c[i][j])}.
     * The result matrix has the same element type as the first matrix.</p>
     *
     * <p>All three matrices must have identical dimensions (same number of rows and columns).
     * This operation delegates to the {@link Matrix#zipWith} method.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<Integer> m1 = Matrix.of(new Integer[][] {{1, 2}});
     * Matrix<Integer> m2 = Matrix.of(new Integer[][] {{3, 4}});
     * Matrix<Integer> m3 = Matrix.of(new Integer[][] {{5, 6}});
     *
     * // Compute (a + b) * c
     * Matrix<Integer> result = Matrixes.zip(m1, m2, m3, (a, b, c) -> (a + b) * c);
     * }</pre>
     *
     * @param <A> the element type of the first matrix and the result matrix
     * @param <B> the element type of the second matrix
     * @param <C> the element type of the third matrix
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param c the third matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements from all three matrices
     * @return a new {@link Matrix} of type A containing the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see Matrix#zipWith(Matrix, Matrix, Throwables.TriFunction)
     */
    public static <A, B, C, E extends Exception> Matrix<A> zip(final Matrix<A> a, final Matrix<B> b, final Matrix<C> c,
            final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     * Combines three generic {@link Matrix} objects element-wise using a ternary function, producing
     * a result matrix with a potentially different element type.
     *
     * <p>This method performs element-wise combination of three matrices with potentially different
     * element types (A, B, and C). For each position (i, j), the function is called with elements
     * from all three matrices: {@code zipFunction.apply(a[i][j], b[i][j], c[i][j])}. The result
     * matrix has element type R, which may differ from all input types.</p>
     *
     * <p>All three matrices must have identical dimensions (same number of rows and columns).
     * This operation delegates to the {@link Matrix#zipWith} method.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Matrix<Integer> numbers = Matrix.of(new Integer[][] {{1, 2}});
     * Matrix<String> units = Matrix.of(new String[][] {{"kg", "m"}});
     * Matrix<Boolean> valid = Matrix.of(new Boolean[][] {{true, false}});
     *
     * // Combine all three into formatted strings
     * Matrix<String> result = Matrixes.zip(numbers, units, valid,
     *     (num, unit, isValid) -> (isValid ? num + unit : "N/A"),
     *     String.class);
     * }</pre>
     *
     * @param <A> the element type of the first matrix
     * @param <B> the element type of the second matrix
     * @param <C> the element type of the third matrix
     * @param <R> the element type of the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param a the first matrix, must not be {@code null}
     * @param b the second matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param c the third matrix, must not be {@code null} and must have the same shape as {@code a}
     * @param zipFunction the function to combine corresponding elements from all three matrices
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if the matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see Matrix#zipWith(Matrix, Matrix, Throwables.TriFunction, Class)
     */
    public static <A, B, C, R, E extends Exception> Matrix<R> zip(final Matrix<A> a, final Matrix<B> b, final Matrix<C> c,
            final Throwables.TriFunction<? super A, ? super B, ? super C, R, E> zipFunction, final Class<R> targetElementType) throws E {
        return a.zipWith(b, c, zipFunction, targetElementType);
    }

    /**
     * Combines multiple generic {@link Matrix} objects element-wise using a binary operator applied sequentially.
     *
     * <p>This method combines an arbitrary number of matrices by applying the binary operator
     * sequentially across all matrices at each position. For a collection of matrices [m1, m2, m3, ...],
     * the result at position (i, j) is computed as:</p>
     * <pre>{@code
     * result[i][j] = zipFunction(zipFunction(m1[i][j], m2[i][j]), m3[i][j])...
     * }</pre>
     *
     * <p>All matrices in the collection must have identical dimensions and element type. The operation
     * is optimized for single and two-element collections:</p>
     * <ul>
     * <li>One matrix: Returns a copy of that matrix</li>
     * <li>Two matrices: Directly applies the binary operator</li>
     * <li>Three or more: Applies the operator sequentially, accumulating results</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<Matrix<String>> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Concatenate strings at each position
     * Matrix<String> concatenated = Matrixes.zip(matrices, (a, b) -> a + "," + b);
     *
     * // Find first non-null value at each position
     * Matrix<Integer> firstNonNull = Matrixes.zip(matrices, (a, b) -> a != null ? a : b);
     * }</pre>
     *
     * @param <T> the element type of the matrices
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the binary operator to combine elements sequentially
     * @return a new {@link Matrix} of type T containing the combined results
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <T, E extends Exception> Matrix<T> zip(final Collection<Matrix<T>> c, final Throwables.BinaryOperator<T, E> zipFunction)
            throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final Matrix<T>[] matrixes = c.toArray(new Matrix[size]);

        if (c.size() == 1) {
            return matrixes[0].copy();
        } else if (c.size() == 2) {
            return matrixes[0].zipWith(matrixes[1], zipFunction);
        }

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final T[][] result = newArray(rows, cols, matrixes[0].elementType);

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final T[] ret = result[i];
            ret[j] = matrixes[0].a[i][j];

            for (int k = 1; k < size; k++) {
                ret[j] = zipFunction.apply(ret[j], matrixes[k].a[i][j]);
            }
        };

        run(rows, cols, cmd, Matrixes.isParallelable(matrixes[0]));

        return new Matrix<>(result);
    }

    /**
     * Combines multiple generic {@link Matrix} objects element-wise using a function that operates on arrays.
     *
     * <p>This method combines an arbitrary number of matrices by applying a function that takes
     * an array of values (one from each matrix at each position) and produces a result of any type.
     * At each position (i, j), an array containing [m1[i][j], m2[i][j], m3[i][j], ...] is passed
     * to the zip function.</p>
     *
     * <p>This is a convenience method that calls
     * {@link #zip(Collection, Throwables.Function, boolean, Class)} with
     * {@code shareIntermediateArray = false}.</p>
     *
     * <p>All matrices in the collection must have identical dimensions.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<Matrix<Integer>> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Find the most common value at each position
     * Matrix<Integer> mode = Matrixes.zip(matrices, arr -> {
     *     Map<Integer, Long> freq = Arrays.stream(arr)
     *         .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
     *     return freq.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
     * }, Integer.class);
     * }</pre>
     *
     * @param <T> the element type of the input matrices
     * @param <R> the element type of the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of values (one from each matrix) and returns a result of type R
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     * @see #zip(Collection, Throwables.Function, boolean, Class)
     */
    public static <T, R, E extends Exception> Matrix<R> zip(final Collection<Matrix<T>> c, final Throwables.Function<? super T[], R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     * Combines multiple generic {@link Matrix} objects element-wise using a function that operates on arrays,
     * with control over intermediate array sharing.
     *
     * <p>This method combines an arbitrary number of matrices by applying a function that takes
     * an array of values (one from each matrix at each position) and produces a result. At each
     * position (i, j), an array containing [m1[i][j], m2[i][j], m3[i][j], ...] is passed to the
     * zip function.</p>
     *
     * <p>The {@code shareIntermediateArray} parameter controls memory optimization:</p>
     * <ul>
     * <li>{@code true} and sequential execution: Reuses the same intermediate array for all positions,
     *     reducing memory allocations but requiring the zip function to not retain references to the array</li>
     * <li>{@code false} or parallel execution: Creates a new array for each position, safer but uses more memory</li>
     * </ul>
     *
     * <p><b>Warning:</b> When {@code shareIntermediateArray} is {@code true}, the zip function must NOT
     * store references to the array, as it will be mutated for subsequent positions. Only use this
     * optimization if the function immediately processes and discards the array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * List<Matrix<Integer>> matrices = Arrays.asList(m1, m2, m3);
     *
     * // Compute average across all matrices at each position
     * Matrix<Double> avg = Matrixes.zip(matrices,
     *     arr -> Arrays.stream(arr).mapToInt(i -> i).average().orElse(0.0),
     *     true, Double.class);
     *
     * // Find maximum value at each position
     * Matrix<Integer> max = Matrixes.zip(matrices,
     *     arr -> Arrays.stream(arr).max(Integer::compare).orElse(0),
     *     false, Integer.class);
     * }</pre>
     *
     * @param <T> the element type of the input matrices
     * @param <R> the element type of the result matrix
     * @param <E> the type of exception that the zip function might throw
     * @param c the collection of matrices to combine, must not be {@code null} or empty
     * @param zipFunction the function that takes an array of values (one from each matrix) and returns a result of type R
     * @param shareIntermediateArray {@code true} to reuse the intermediate array (sequential execution only);
     *                               {@code false} to create new arrays for each position
     * @param targetElementType the class of the result element type
     * @return a new {@link Matrix} of type R containing the combined values
     * @throws IllegalArgumentException if {@code c} is {@code null}, empty, or if matrices have different shapes
     * @throws E if the zip function throws an exception during execution
     */
    public static <T, R, E extends Exception> Matrix<R> zip(final Collection<Matrix<T>> c, final Throwables.Function<? super T[], R, E> zipFunction,
            final boolean shareIntermediateArray, final Class<R> targetElementType) throws IllegalArgumentException, E {
        checkShapeForZip(c);

        final int size = c.size();
        final Matrix<T>[] matrixes = c.toArray(new Matrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && !zipInParallel;
        final T[] intermediateArray = N.newArray(matrixes[0].elementType, size);
        final R[][] result = newArray(rows, cols, targetElementType);

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            final T[] tmp = shareArray ? intermediateArray : N.clone(intermediateArray);

            for (int k = 0; k < size; k++) {
                tmp[k] = matrixes[k].a[i][j];
            }

            result[i][j] = zipFunction.apply(tmp);
        };

        run(rows, cols, cmd, zipInParallel);

        return new Matrix<>(result);
    }

    private static void checkShapeForZip(final AbstractMatrix<?, ?, ?, ?, ?> a, final AbstractMatrix<?, ?, ?, ?, ?> b) {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");
    }

    private static void checkShapeForZip(final Collection<? extends AbstractMatrix<?, ?, ?, ?, ?>> c) {
        N.checkArgNotEmpty(c, "matrixes");

        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");
    }
}