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
 * <p>Example usage:</p>
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
     * Gets the current parallel processing setting for the current thread.
     * The setting is thread-local, allowing different threads to have different
     * parallelization behaviors.
     *
     * @return the current {@link ParallelEnabled} setting for this thread
     * @see #setParallelEnabled(ParallelEnabled)
     */
    public static ParallelEnabled getParallelEnabled() {
        return isParallelEnabled_TL.get();
    }

    /**
     * Sets the parallel processing behavior for matrix operations in the current thread.
     * This setting affects how matrix operations decide whether to use parallel processing.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Force parallel execution for large matrix operations
     * Matrixes.setParallelEnabled(ParallelEnabled.YES);
     * try {
     *     // Perform matrix operations here
     * } finally {
     *     // Reset to default
     *     Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
     * }
     * }</pre>
     *
     * @param flag the {@link ParallelEnabled} setting to use
     * @throws IllegalArgumentException if flag is null
     * @see ParallelEnabled
     */
    public static void setParallelEnabled(final ParallelEnabled flag) throws IllegalArgumentException {
        N.checkArgNotNull(flag);

        isParallelEnabled_TL.set(flag);
    }

    /**
     * Determines whether the given matrix should be processed in parallel based on its size.
     * Uses the matrix's total element count to make the decision.
     *
     * @param x the matrix to check
     * @return {@code true} if parallel processing should be used, {@code false} otherwise
     * @see #isParallelable(AbstractMatrix, long)
     */
    public static boolean isParallelable(final AbstractMatrix<?, ?, ?, ?, ?> x) {
        return isParallelable(x, x.count);
    }

    /**
     * Determines whether a matrix operation should be processed in parallel based on
     * the element count and current parallel settings. The decision is made based on:
     * <ul>
     * <li>Whether parallel streams are supported in the runtime</li>
     * <li>The current thread's {@link ParallelEnabled} setting</li>
     * <li>The number of elements to process (default threshold is 8192)</li>
     * </ul>
     *
     * @param x the matrix (used for future extensibility, currently not used in decision)
     * @param count the number of elements to process
     * @return {@code true} if parallel processing should be used, {@code false} otherwise
     */
    public static boolean isParallelable(@SuppressWarnings("unused") final AbstractMatrix<?, ?, ?, ?, ?> x, final long count) { // NOSONAR
        return IS_PARALLEL_STREAM_SUPPORTED && (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.YES
                || (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.DEFAULT && count >= MIN_COUNT_FOR_PARALLEL));
    }

    /**
     * Checks if two matrices have the same shape (same number of rows and columns).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix m1 = IntMatrix.of(new int[][]{{1, 2}, {3, 4}});
     * IntMatrix m2 = IntMatrix.of(new int[][]{{5, 6}, {7, 8}});
     * boolean same = Matrixes.isSameShape(m1, m2); // true
     * }</pre>
     *
     * @param <X> the type of matrix
     * @param a the first matrix
     * @param b the second matrix
     * @return {@code true} if both matrices have the same number of rows and columns
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> boolean isSameShape(final X a, final X b) {
        return a.rows == b.rows && a.cols == b.cols;
    }

    /**
     * Checks if three matrices have the same shape (same number of rows and columns).
     *
     * @param <X> the type of matrix
     * @param a the first matrix
     * @param b the second matrix
     * @param c the third matrix
     * @return {@code true} if all three matrices have the same number of rows and columns
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> boolean isSameShape(final X a, final X b, final X c) {
        return a.rows == b.rows && a.rows == c.rows && a.cols == b.cols && a.cols == c.cols;
    }

    /**
     * Checks if all matrices in a collection have the same shape.
     * Returns true for empty collections or collections with a single matrix.
     *
     * @param <X> the type of matrix
     * @param xs the collection of matrices to check
     * @return {@code true} if all matrices have the same number of rows and columns, or if the collection is empty or has one element
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
     * This is a utility method for creating properly typed 2D arrays at runtime.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Double[][] array = Matrixes.newArray(3, 4, Double.class);
     * // Creates a 3x4 array of Double objects
     * }</pre>
     *
     * @param <T> the element type
     * @param rows the number of rows
     * @param cols the number of columns
     * @param targetElementType the class of the element type
     * @return a new 2D array with the specified dimensions
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
     * Executes the specified command with the given parallel setting and restores
     * the original setting after execution. This is useful for temporarily changing
     * the parallel execution behavior for a specific operation.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * Matrixes.run(() -> {
     *     // This operation will run with parallel enabled
     *     matrix.multiply(otherMatrix);
     * }, ParallelEnabled.YES);
     * }</pre>
     * 
     * @param <E> the type of exception that might be thrown
     * @param cmd the command to execute
     * @param parallelEnabled the parallel setting to use during execution
     * @throws E if the command throws an exception
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
     * Executes a command for each position in a matrix defined by rows and columns.
     * The command receives the row and column indices as parameters.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Print all positions in a 3x4 matrix
     * Matrixes.run(3, 4, (i, j) -> System.out.println("(" + i + "," + j + ")"), false);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param rows the number of rows
     * @param cols the number of columns
     * @param cmd the command to execute for each position
     * @param inParallel whether to execute in parallel
     * @throws E if the command throws an exception
     */
    public static <E extends Exception> void run(final int rows, final int cols, final Throwables.IntBiConsumer<E> cmd, final boolean inParallel) throws E {
        run(0, rows, 0, cols, cmd, inParallel);
    }

    /**
     * Executes a command for each position in a subregion of a matrix.
     * The command is called with row and column indices for each position
     * in the specified range. Execution order depends on which dimension
     * is larger and whether parallel execution is enabled.
     *
     * @param <E> the type of exception that might be thrown
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @param cmd the command to execute for each position
     * @param inParallel whether to execute in parallel
     * @throws IndexOutOfBoundsException if the indices are invalid
     * @throws E if the command throws an exception
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
     * Executes a function for each position in a matrix and returns the results as a stream.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Generate coordinates as strings
     * Stream<String> coords = Matrixes.call(2, 3, (i, j) -> i + "," + j, false);
     * // Results: "0,0", "0,1", "0,2", "1,0", "1,1", "1,2"
     * }</pre>
     *
     * @param <T> the type of elements in the result stream
     * @param rows the number of rows
     * @param cols the number of columns
     * @param cmd the function to apply at each position
     * @param inParallel whether to execute in parallel
     * @return a stream of results from applying the function at each position
     */
    public static <T> Stream<T> call(final int rows, final int cols, final Throwables.IntBiFunction<? extends T, ? extends Exception> cmd,
            final boolean inParallel) {
        return call(0, rows, 0, cols, cmd, inParallel);
    }

    /**
     * Executes a function for each position in a subregion of a matrix and returns the results as a stream.
     * The function is called with row and column indices and can return any type of result.
     * The order of elements in the stream depends on the relative sizes of rows and columns.
     *
     * @param <T> the type of elements in the result stream
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @param cmd the function to apply at each position
     * @param inParallel whether to execute in parallel
     * @return a stream of results from applying the function at each position
     * @throws IndexOutOfBoundsException if the indices are invalid
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
     * Executes a function that returns int values for each position in a matrix and returns the results as an IntStream.
     *
     * @param rows the number of rows
     * @param cols the number of columns
     * @param cmd the function to apply at each position
     * @param inParallel whether to execute in parallel
     * @return an IntStream of results from applying the function at each position
     */
    public static IntStream callToInt(final int rows, final int cols, final Throwables.IntBinaryOperator<? extends Exception> cmd, final boolean inParallel) {
        return callToInt(0, rows, 0, cols, cmd, inParallel);
    }

    /**
     * Executes a function that returns int values for each position in a subregion of a matrix 
     * and returns the results as an IntStream.
     *
     * @param fromRowIndex the starting row index (inclusive)
     * @param toRowIndex the ending row index (exclusive)
     * @param fromColumnIndex the starting column index (inclusive)
     * @param toColumnIndex the ending column index (exclusive)
     * @param cmd the function to apply at each position
     * @param inParallel whether to execute in parallel
     * @return an IntStream of results from applying the function at each position
     * @throws IndexOutOfBoundsException if the indices are invalid
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
     * Performs matrix multiplication using a custom accumulator function.
     * The function is called with indices (i, j, k) where i is the row index
     * in matrix a, j is the column index in matrix b, and k is the common dimension.
     * 
     * <p>The matrices must satisfy the multiplication constraint: a.cols == b.rows</p>
     * 
     * <p>Example:</p>
     * <pre>{@code
     * // Custom multiplication accumulator
     * Matrixes.multiply(matrixA, matrixB, (i, j, k) -> {
     *     result[i][j] += matrixA.get(i, k) * matrixB.get(k, j);
     * });
     * }</pre>
     *
     * @param <X> the type of matrix
     * @param a the first matrix
     * @param b the second matrix
     * @param cmd the accumulator function called for each multiplication step
     * @throws IllegalArgumentException if matrix dimensions are incompatible (a.cols != b.rows)
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> void multiply(final X a, final X b, final Throwables.IntTriConsumer<RuntimeException> cmd)
            throws IllegalArgumentException {
        N.checkArgument(a.cols == b.rows, "Illegal matrix dimensions");

        multiply(a, b, cmd, Matrixes.isParallelable(a, a.count * b.cols));
    }

    /**
     * Performs matrix multiplication using a custom accumulator function with explicit parallel control.
     * The function provides fine-grained control over the multiplication process by calling
     * the command for each (i, j, k) triple in the multiplication.
     *
     * @param <X> the type of matrix
     * @param a the first matrix
     * @param b the second matrix
     * @param cmd the accumulator function called for each multiplication step
     * @param inParallel whether to execute in parallel
     * @throws IllegalArgumentException if matrix dimensions are incompatible (a.cols != b.rows)
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
     * Combines two ByteMatrix objects element-wise using the provided binary operator.
     * Both matrices must have the same shape.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ByteMatrix sum = Matrixes.zip(matrix1, matrix2, (a, b) -> (byte)(a + b));
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param zipFunction the binary operator to combine corresponding elements
     * @return a new ByteMatrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     * Combines three ByteMatrix objects element-wise using the provided ternary operator.
     * All matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param c the third matrix
     * @param zipFunction the ternary operator to combine corresponding elements
     * @return a new ByteMatrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final ByteMatrix c,
            final Throwables.ByteTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     * Combines multiple ByteMatrix objects element-wise using the provided binary operator.
     * The operator is applied sequentially across all matrices at each position.
     * All matrices must have the same shape.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * List<ByteMatrix> matrices = Arrays.asList(m1, m2, m3);
     * ByteMatrix max = Matrixes.zip(matrices, (a, b) -> (byte)Math.max(a, b));
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the binary operator to combine elements
     * @return a new ByteMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape or collection is empty
     * @throws E if the zip function throws an exception
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
     * Combines multiple ByteMatrix objects element-wise using a function that takes an array of bytes.
     * At each position, the function receives an array containing the values from all matrices
     * at that position.
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of bytes and returns a result
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<ByteMatrix> c, final Throwables.ByteNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     * Combines multiple ByteMatrix objects element-wise using a function that takes an array of bytes.
     * Provides control over whether the intermediate array can be shared between calls.
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of bytes and returns a result
     * @param shareIntermediateArray if true and not parallel, reuses the same array for efficiency
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines two ByteMatrix objects element-wise using a function that returns Integer values.
     * Both matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param zipFunction the function to combine corresponding elements
     * @return a new IntMatrix with the combined values
     * @throws E if the zip function throws an exception
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
     * Combines three ByteMatrix objects element-wise using a function that returns Integer values.
     * All matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param c the third matrix
     * @param zipFunction the function to combine corresponding elements
     * @return a new IntMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines multiple ByteMatrix objects element-wise using a function that returns Integer values.
     * At each position, the function receives an array containing the byte values from all matrices.
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of bytes and returns an Integer
     * @return a new IntMatrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> IntMatrix zipToInt(final Collection<ByteMatrix> c, final Throwables.ByteNFunction<Integer, E> zipFunction) throws E {
        return zipToInt(c, zipFunction, false);
    }

    /**
     * Combines multiple ByteMatrix objects element-wise using a function that returns Integer values.
     * Provides control over whether the intermediate array can be shared between calls.
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of bytes and returns an Integer
     * @param shareIntermediateArray if true and not parallel, reuses the same array for efficiency
     * @return a new IntMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines two IntMatrix objects element-wise using the provided binary operator.
     * Both matrices must have the same shape.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * IntMatrix sum = Matrixes.zip(matrix1, matrix2, (a, b) -> a + b);
     * }</pre>
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param zipFunction the binary operator to combine corresponding elements
     * @return a new IntMatrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     * Combines three IntMatrix objects element-wise using the provided ternary operator.
     * All matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param c the third matrix
     * @param zipFunction the ternary operator to combine corresponding elements
     * @return a new IntMatrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     * Combines multiple IntMatrix objects element-wise using the provided binary operator.
     * The operator is applied sequentially across all matrices at each position.
     * All matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the binary operator to combine elements
     * @return a new IntMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape or collection is empty
     * @throws E if the zip function throws an exception
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
     * Combines multiple IntMatrix objects element-wise using a function that takes an array of integers.
     * At each position, the function receives an array containing the values from all matrices.
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of integers and returns a result
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<IntMatrix> c, final Throwables.IntNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     * Combines multiple IntMatrix objects element-wise using a function that takes an array of integers.
     * Provides control over whether the intermediate array can be shared between calls.
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of integers and returns a result
     * @param shareIntermediateArray if true and not parallel, reuses the same array for efficiency
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines two IntMatrix objects element-wise using a function that returns Long values.
     * Both matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param zipFunction the function to combine corresponding elements
     * @return a new LongMatrix with the combined values
     * @throws E if the zip function throws an exception
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
     * Combines three IntMatrix objects element-wise using a function that returns Long values.
     * All matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param c the third matrix
     * @param zipFunction the function to combine corresponding elements
     * @return a new LongMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines multiple IntMatrix objects element-wise using a function that returns Long values.
     * At each position, the function receives an array containing the integer values from all matrices.
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of integers and returns a Long
     * @return a new LongMatrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> LongMatrix zipToLong(final Collection<IntMatrix> c, final Throwables.IntNFunction<Long, E> zipFunction) throws E {
        return zipToLong(c, zipFunction, false);
    }

    /**
     * Combines multiple IntMatrix objects element-wise using a function that returns Long values.
     * Provides control over whether the intermediate array can be shared between calls.
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of integers and returns a Long
     * @param shareIntermediateArray if true and not parallel, reuses the same array for efficiency
     * @return a new LongMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines two IntMatrix objects element-wise using a function that returns Double values.
     * Both matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param zipFunction the function to combine corresponding elements
     * @return a new DoubleMatrix with the combined values
     * @throws E if the zip function throws an exception
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
     * Combines three IntMatrix objects element-wise using a function that returns Double values.
     * All matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param c the third matrix
     * @param zipFunction the function to combine corresponding elements
     * @return a new DoubleMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines multiple IntMatrix objects element-wise using a function that returns Double values.
     * At each position, the function receives an array containing the integer values from all matrices.
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of integers and returns a Double
     * @return a new DoubleMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<IntMatrix> c, final Throwables.IntNFunction<Double, E> zipFunction)
            throws IllegalArgumentException, E {
        return zipToDouble(c, zipFunction, false);
    }

    /**
     * Combines multiple IntMatrix objects element-wise using a function that returns Double values.
     * Provides control over whether the intermediate array can be shared between calls.
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of integers and returns a Double
     * @param shareIntermediateArray if true and not parallel, reuses the same array for efficiency
     * @return a new DoubleMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines two LongMatrix objects element-wise using the provided binary operator.
     * Both matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param zipFunction the binary operator to combine corresponding elements
     * @return a new LongMatrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     * Combines three LongMatrix objects element-wise using the provided ternary operator.
     * All matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param c the third matrix
     * @param zipFunction the ternary operator to combine corresponding elements
     * @return a new LongMatrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final LongMatrix c,
            final Throwables.LongTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     * Combines multiple LongMatrix objects element-wise using the provided binary operator.
     * The operator is applied sequentially across all matrices at each position.
     * All matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the binary operator to combine elements
     * @return a new LongMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape or collection is empty
     * @throws E if the zip function throws an exception
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
     * Combines multiple LongMatrix objects element-wise using a function that takes an array of longs.
     * At each position, the function receives an array containing the values from all matrices.
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of longs and returns a result
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<LongMatrix> c, final Throwables.LongNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     * Combines multiple LongMatrix objects element-wise using a function that takes an array of longs.
     * Provides control over whether the intermediate array can be shared between calls.
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of longs and returns a result
     * @param shareIntermediateArray if true and not parallel, reuses the same array for efficiency
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines two LongMatrix objects element-wise using a function that returns Double values.
     * Both matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param zipFunction the function to combine corresponding elements
     * @return a new DoubleMatrix with the combined values
     * @throws E if the zip function throws an exception
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
     * Combines three LongMatrix objects element-wise using a function that returns Double values.
     * All matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param c the third matrix
     * @param zipFunction the function to combine corresponding elements
     * @return a new DoubleMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines multiple LongMatrix objects element-wise using a function that returns Double values.
     * At each position, the function receives an array containing the long values from all matrices.
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of longs and returns a Double
     * @return a new DoubleMatrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<LongMatrix> c, final Throwables.LongNFunction<Double, E> zipFunction)
            throws E {
        return zipToDouble(c, zipFunction, false);
    }

    /**
     * Combines multiple LongMatrix objects element-wise using a function that returns Double values.
     * Provides control over whether the intermediate array can be shared between calls.
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of longs and returns a Double
     * @param shareIntermediateArray if true and not parallel, reuses the same array for efficiency
     * @return a new DoubleMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines two DoubleMatrix objects element-wise using the provided binary operator.
     * Both matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param zipFunction the binary operator to combine corresponding elements
     * @return a new DoubleMatrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> DoubleMatrix zip(final DoubleMatrix a, final DoubleMatrix b, final Throwables.DoubleBinaryOperator<E> zipFunction)
            throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     * Combines three DoubleMatrix objects element-wise using the provided ternary operator.
     * All matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param c the third matrix
     * @param zipFunction the ternary operator to combine corresponding elements
     * @return a new DoubleMatrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> DoubleMatrix zip(final DoubleMatrix a, final DoubleMatrix b, final DoubleMatrix c,
            final Throwables.DoubleTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     * Combines multiple DoubleMatrix objects element-wise using the provided binary operator.
     * The operator is applied sequentially across all matrices at each position.
     * All matrices must have the same shape.
     *
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the binary operator to combine elements
     * @return a new DoubleMatrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape or collection is empty
     * @throws E if the zip function throws an exception
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
     * Combines multiple DoubleMatrix objects element-wise using a function that takes an array of doubles.
     * At each position, the function receives an array containing the values from all matrices.
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of doubles and returns a result
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<DoubleMatrix> c, final Throwables.DoubleNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     * Combines multiple DoubleMatrix objects element-wise using a function that takes an array of doubles.
     * Provides control over whether the intermediate array can be shared between calls.
     *
     * @param <R> the type of elements in the result matrix
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of doubles and returns a result
     * @param shareIntermediateArray if true and not parallel, reuses the same array for efficiency
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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
     * Combines two generic Matrix objects element-wise using the provided binary function.
     * Both matrices must have the same shape. The result matrix has the same element type as the first matrix.
     *
     * @param <A> the element type of the first matrix
     * @param <B> the element type of the second matrix
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param zipFunction the function to combine corresponding elements
     * @return a new Matrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <A, B, E extends Exception> Matrix<A> zip(final Matrix<A> a, final Matrix<B> b,
            final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     * Combines two generic Matrix objects element-wise using the provided binary function.
     * Both matrices must have the same shape. The result matrix has the specified element type.
     *
     * @param <A> the element type of the first matrix
     * @param <B> the element type of the second matrix
     * @param <R> the element type of the result matrix
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param zipFunction the function to combine corresponding elements
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <A, B, R, E extends Exception> Matrix<R> zip(final Matrix<A> a, final Matrix<B> b,
            final Throwables.BiFunction<? super A, ? super B, R, E> zipFunction, final Class<R> targetElementType) throws E {
        return a.zipWith(b, zipFunction, targetElementType);
    }

    /**
     * Combines three generic Matrix objects element-wise using the provided ternary function.
     * All matrices must have the same shape. The result matrix has the same element type as the first matrix.
     *
     * @param <A> the element type of the first matrix
     * @param <B> the element type of the second matrix
     * @param <C> the element type of the third matrix
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param c the third matrix
     * @param zipFunction the function to combine corresponding elements
     * @return a new Matrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <A, B, C, E extends Exception> Matrix<A> zip(final Matrix<A> a, final Matrix<B> b, final Matrix<C> c,
            final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     * Combines three generic Matrix objects element-wise using the provided ternary function.
     * All matrices must have the same shape. The result matrix has the specified element type.
     *
     * @param <A> the element type of the first matrix
     * @param <B> the element type of the second matrix
     * @param <C> the element type of the third matrix
     * @param <R> the element type of the result matrix
     * @param <E> the type of exception that might be thrown
     * @param a the first matrix
     * @param b the second matrix
     * @param c the third matrix
     * @param zipFunction the function to combine corresponding elements
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <A, B, C, R, E extends Exception> Matrix<R> zip(final Matrix<A> a, final Matrix<B> b, final Matrix<C> c,
            final Throwables.TriFunction<? super A, ? super B, ? super C, R, E> zipFunction, final Class<R> targetElementType) throws E {
        return a.zipWith(b, c, zipFunction, targetElementType);
    }

    /**
     * Combines multiple generic Matrix objects element-wise using the provided binary operator.
     * The operator is applied sequentially across all matrices at each position.
     * All matrices must have the same shape and element type.
     *
     * @param <T> the element type of the matrices
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the binary operator to combine elements
     * @return a new Matrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape or collection is empty
     * @throws E if the zip function throws an exception
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
     * Combines multiple generic Matrix objects element-wise using a function that takes an array of values.
     * At each position, the function receives an array containing the values from all matrices.
     *
     * @param <T> the element type of the input matrices
     * @param <R> the element type of the result matrix
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of values and returns a result
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws E if the zip function throws an exception
     */
    public static <T, R, E extends Exception> Matrix<R> zip(final Collection<Matrix<T>> c, final Throwables.Function<? super T[], R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     * Combines multiple generic Matrix objects element-wise using a function that takes an array of values.
     * Provides control over whether the intermediate array can be shared between calls.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * List<Matrix<Integer>> matrices = Arrays.asList(m1, m2, m3);
     * Matrix<Double> avg = Matrixes.zip(matrices, 
     *     arr -> Arrays.stream(arr).mapToInt(i -> i).average().orElse(0.0),
     *     true, Double.class);
     * }</pre>
     *
     * @param <T> the element type of the input matrices
     * @param <R> the element type of the result matrix
     * @param <E> the type of exception that might be thrown
     * @param c the collection of matrices to combine
     * @param zipFunction the function that takes an array of values and returns a result
     * @param shareIntermediateArray if true and not parallel, reuses the same array for efficiency
     * @param targetElementType the class of the result element type
     * @return a new Matrix with the combined values
     * @throws IllegalArgumentException if matrices don't have the same shape
     * @throws E if the zip function throws an exception
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