/*
 * Copyright (C) 2020 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import com.landawn.abacus.util.function.IntConsumer;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.Stream;

public final class Matrixes {

    static final Logger logger = LoggerFactory.getLogger(Matrixes.class);

    static final int MIN_COUNT_FOR_PARALLEL = 8192;

    static final boolean isParallelStreamSupported;
    static {
        boolean tmp = false;

        try {
            if (ClassUtil.forClass("com.landawn.abacus.util.stream.ParallelArrayIntStream") != null
                    && ClassUtil.forClass("com.landawn.abacus.util.stream.ParallelIteratorIntStream") != null) {
                tmp = true;
            }
        } catch (Exception e) {
            // ignore.
        }

        isParallelStreamSupported = tmp;
    }

    static final ThreadLocal<ParallelEnabled> isParallelEnabled_TL = ThreadLocal.withInitial(() -> ParallelEnabled.DEFAULT);

    private Matrixes() {
        // singleton: utility class.
    }

    /**
     *
     *
     * @param flag
     * @throws IllegalArgumentException
     */
    public static void setParallelEnabled(final ParallelEnabled flag) throws IllegalArgumentException {
        N.checkArgNotNull(flag);

        isParallelEnabled_TL.set(flag);
    }

    /**
     *
     *
     * @return
     */
    public static ParallelEnabled getParallelEnabled() {
        return isParallelEnabled_TL.get();
    }

    /**
     *
     *
     * @param x
     * @return
     */
    public static boolean isParallelable(final AbstractMatrix<?, ?, ?, ?, ?> x) {
        return isParallelStreamSupported && (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.YES
                || (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.DEFAULT && x.count > MIN_COUNT_FOR_PARALLEL));
    }

    /**
     *
     *
     * @param x
     * @param bm
     * @return
     */
    public static boolean isParallelable(final AbstractMatrix<?, ?, ?, ?, ?> x, final int bm) {
        return isParallelStreamSupported && (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.YES
                || (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.DEFAULT && x.count * bm > MIN_COUNT_FOR_PARALLEL));
    }

    /**
     *
     *
     * @param <X>
     * @param a
     * @param b
     * @return
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> boolean isSameShape(final X a, final X b) {
        return a.rows == b.rows && a.cols == b.cols;
    }

    /**
     *
     *
     * @param <X>
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> boolean isSameShape(final X a, final X b, final X c) {
        return a.rows == b.rows && a.rows == c.rows && a.cols == b.cols && a.cols == c.cols;
    }

    /**
     *
     *
     * @param <X>
     * @param xs
     * @return
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> boolean isSameShape(final Collection<? extends X> xs) {
        if (N.isEmpty(xs) || xs.size() == 1) {
            return true;
        }

        final Iterator<? extends X> iter = xs.iterator();
        final X first = iter.next();
        final int rows = first.rows;
        final int cols = first.cols;
        X next = null;

        while (iter.hasNext()) {
            next = iter.next();

            if (next.rows != rows || next.cols != cols) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     *
     * @param <T>
     * @param rows
     * @param cols
     * @param targetElementType
     * @return
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
     *
     * @param <E>
     * @param rows
     * @param cols
     * @param cmd
     * @param inParallel
     * @throws E
     */
    public static <E extends Exception> void run(final int rows, final int cols, final Throwables.IntBiConsumer<E> cmd, final boolean inParallel) throws E {
        run(0, rows, 0, cols, cmd, inParallel);
    }

    /**
     *
     *
     * @param <E>
     * @param fromRowIndex
     * @param toRowIndex
     * @param fromColumnIndex
     * @param toColumnIndex
     * @param cmd
     * @param inParallel
     * @throws IndexOutOfBoundsException
     * @throws E
     */
    public static <E extends Exception> void run(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntBiConsumer<E> cmd, final boolean inParallel) throws IndexOutOfBoundsException, E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, Integer.MAX_VALUE);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, Integer.MAX_VALUE);

        final int rows = toRowIndex - fromRowIndex;
        final int cols = toColumnIndex - fromColumnIndex;

        if (inParallel) {
            if (rows <= cols) {
                IntStream.range(fromRowIndex, toRowIndex).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int i) throws E {
                        for (int j = fromColumnIndex; j < toColumnIndex; j++) {
                            cmd.accept(i, j);
                        }
                    }
                });
            } else {
                IntStream.range(fromColumnIndex, toColumnIndex).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int j) throws E {
                        for (int i = fromRowIndex; i < toRowIndex; i++) {
                            cmd.accept(i, j);
                        }
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
     *
     *
     * @param <T>
     * @param <E>
     * @param rows
     * @param cols
     * @param cmd
     * @param inParallel
     * @return
     * @throws E
     */
    public static <T, E extends Exception> Stream<T> call(final int rows, final int cols, final Throwables.IntBiFunction<? extends T, E> cmd,
            final boolean inParallel) throws E {
        return call(0, rows, 0, cols, cmd, inParallel);
    }

    /**
     *
     *
     * @param <T>
     * @param <E>
     * @param fromRowIndex
     * @param toRowIndex
     * @param fromColumnIndex
     * @param toColumnIndex
     * @param cmd
     * @param inParallel
     * @return
     * @throws IndexOutOfBoundsException
     * @throws E
     */
    @SuppressWarnings("resource")
    public static <T, E extends Exception> Stream<T> call(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntBiFunction<? extends T, E> cmd, final boolean inParallel) throws IndexOutOfBoundsException, E {
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
                } catch (Exception e) {
                    N.toRuntimeException(e);
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
                } catch (Exception e) {
                    N.toRuntimeException(e);
                }

                return ret;
            });
        }
    }

    /**
     *
     *
     * @param <E>
     * @param rows
     * @param cols
     * @param cmd
     * @param inParallel
     * @return
     * @throws E
     */
    public static <E extends Exception> IntStream callToInt(final int rows, final int cols, final Throwables.IntBinaryOperator<E> cmd, final boolean inParallel)
            throws E {
        return callToInt(0, rows, 0, cols, cmd, inParallel);
    }

    /**
     *
     *
     * @param <E>
     * @param fromRowIndex
     * @param toRowIndex
     * @param fromColumnIndex
     * @param toColumnIndex
     * @param cmd
     * @param inParallel
     * @return
     * @throws IndexOutOfBoundsException
     * @throws E
     */
    @SuppressWarnings("resource")
    public static <E extends Exception> IntStream callToInt(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntBinaryOperator<E> cmd, final boolean inParallel) throws IndexOutOfBoundsException, E {
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
                } catch (Exception e) {
                    N.toRuntimeException(e);
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
                } catch (Exception e) {
                    N.toRuntimeException(e);
                }

                return ret;
            });
        }
    }

    /**
     *
     *
     * @param <X>
     * @param a
     * @param b
     * @param cmd
     * @throws IllegalArgumentException
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> void multiply(final X a, final X b, final Throwables.IntTriConsumer<RuntimeException> cmd)
            throws IllegalArgumentException {
        N.checkArgument(a.cols == b.rows, "Illegal matrix dimensions");

        multiply(a, b, cmd, Matrixes.isParallelable(a, b.cols));
    }

    /**
     *
     *
     * @param <X>
     * @param a
     * @param b
     * @param cmd
     * @param inParallel
     * @throws IllegalArgumentException
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> void multiply(final X a, final X b, final Throwables.IntTriConsumer<RuntimeException> cmd,
            final boolean inParallel) throws IllegalArgumentException {
        N.checkArgument(a.cols == b.rows, "Illegal matrix dimensions");

        final int rowsA = a.rows;
        final int colsA = a.cols;
        final int colsB = b.cols;

        if (inParallel) {
            if (N.min(rowsA, colsA, colsB) == rowsA) {
                if (N.min(colsA, colsB) == colsA) {
                    IntStream.range(0, rowsA).parallel().forEach(new IntConsumer() {
                        @Override
                        public void accept(final int i) {
                            for (int k = 0; k < colsA; k++) {
                                for (int j = 0; j < colsB; j++) {
                                    cmd.accept(i, j, k);
                                }
                            }
                        }
                    });
                } else {
                    IntStream.range(0, rowsA).parallel().forEach(new IntConsumer() {

                        @Override
                        public void accept(final int i) {
                            for (int j = 0; j < colsB; j++) {
                                for (int k = 0; k < colsA; k++) {
                                    cmd.accept(i, j, k);
                                }
                            }
                        }
                    });
                }
            } else if (N.min(rowsA, colsA, colsB) == colsA) {
                if (N.min(rowsA, colsB) == rowsA) {
                    IntStream.range(0, colsA).parallel().forEach(new IntConsumer() {
                        @Override
                        public void accept(final int k) {
                            for (int i = 0; i < rowsA; i++) {
                                for (int j = 0; j < colsB; j++) {
                                    cmd.accept(i, j, k);
                                }
                            }
                        }
                    });
                } else {
                    IntStream.range(0, colsA).parallel().forEach(new IntConsumer() {
                        @Override
                        public void accept(final int k) {
                            for (int j = 0; j < colsB; j++) {
                                for (int i = 0; i < rowsA; i++) {
                                    cmd.accept(i, j, k);
                                }
                            }
                        }
                    });
                }
            } else {
                if (N.min(rowsA, colsA) == rowsA) {
                    IntStream.range(0, colsB).parallel().forEach(new IntConsumer() {
                        @Override
                        public void accept(final int j) {
                            for (int i = 0; i < rowsA; i++) {
                                for (int k = 0; k < colsA; k++) {
                                    cmd.accept(i, j, k);
                                }
                            }
                        }
                    });
                } else {
                    IntStream.range(0, colsB).parallel().forEach(new IntConsumer() {
                        @Override
                        public void accept(final int j) {
                            for (int k = 0; k < colsA; k++) {
                                for (int i = 0; i < rowsA; i++) {
                                    cmd.accept(i, j, k);
                                }
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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final ByteMatrix c,
            final Throwables.ByteTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> ByteMatrix zip(final Collection<ByteMatrix> c, final Throwables.ByteBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <R>
     * @param <E>
     * @param c
     * @param zipFunction
     * @param targetElementType
     * @return
     * @throws E
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<ByteMatrix> c, final Throwables.ByteNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     *
     *
     * @param <R>
     * @param <E>
     * @param c
     * @param zipFunction
     * @param shareIntermediateArray
     * @param targetElementType
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<ByteMatrix> c, final Throwables.ByteNFunction<? extends R, E> zipFunction,
            final boolean shareIntermediateArray, final Class<R> targetElementType) throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> IntMatrix zipToInt(final ByteMatrix a, final ByteMatrix b, final Throwables.ByteBiFunction<Integer, E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> IntMatrix zipToInt(final ByteMatrix a, final ByteMatrix b, final ByteMatrix c,
            final Throwables.ByteTriFunction<Integer, E> zipFunction) throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

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
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> IntMatrix zipToInt(final Collection<ByteMatrix> c, final Throwables.ByteNFunction<Integer, E> zipFunction) throws E {
        return zipToInt(c, zipFunction, false);
    }

    /**
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @param shareIntermediateArray
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> IntMatrix zipToInt(final Collection<ByteMatrix> c, final Throwables.ByteNFunction<Integer, E> zipFunction,
            final boolean shareIntermediateArray) throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> IntMatrix zip(final Collection<IntMatrix> c, final Throwables.IntBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <R>
     * @param <E>
     * @param c
     * @param zipFunction
     * @param targetElementType
     * @return
     * @throws E
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<IntMatrix> c, final Throwables.IntNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     *
     *
     * @param <R>
     * @param <E>
     * @param c
     * @param zipFunction
     * @param shareIntermediateArray
     * @param targetElementType
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<IntMatrix> c, final Throwables.IntNFunction<? extends R, E> zipFunction,
            final boolean shareIntermediateArray, final Class<R> targetElementType) throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> LongMatrix zipToLong(final IntMatrix a, final IntMatrix b, final Throwables.IntBiFunction<Long, E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> LongMatrix zipToLong(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTriFunction<Long, E> zipFunction) throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

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
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> LongMatrix zipToLong(final Collection<IntMatrix> c, final Throwables.IntNFunction<Long, E> zipFunction) throws E {
        return zipToLong(c, zipFunction, false);
    }

    /**
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @param shareIntermediateArray
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> LongMatrix zipToLong(final Collection<IntMatrix> c, final Throwables.IntNFunction<Long, E> zipFunction,
            final boolean shareIntermediateArray) throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final IntMatrix a, final IntMatrix b, final Throwables.IntBiFunction<Double, E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTriFunction<Double, E> zipFunction) throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

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
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<IntMatrix> c, final Throwables.IntNFunction<Double, E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");

        return zipToDouble(c, zipFunction, false);
    }

    /**
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @param shareIntermediateArray
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<IntMatrix> c, final Throwables.IntNFunction<Double, E> zipFunction,
            final boolean shareIntermediateArray) throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final LongMatrix c,
            final Throwables.LongTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> LongMatrix zip(final Collection<LongMatrix> c, final Throwables.LongBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <R>
     * @param <E>
     * @param c
     * @param zipFunction
     * @param targetElementType
     * @return
     * @throws E
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<LongMatrix> c, final Throwables.LongNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     *
     *
     * @param <R>
     * @param <E>
     * @param c
     * @param zipFunction
     * @param shareIntermediateArray
     * @param targetElementType
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<LongMatrix> c, final Throwables.LongNFunction<? extends R, E> zipFunction,
            final boolean shareIntermediateArray, final Class<R> targetElementType) throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final LongMatrix a, final LongMatrix b, final Throwables.LongBiFunction<Double, E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final LongMatrix a, final LongMatrix b, final LongMatrix c,
            final Throwables.LongTriFunction<Double, E> zipFunction) throws IllegalArgumentException, E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

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
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<LongMatrix> c, final Throwables.LongNFunction<Double, E> zipFunction)
            throws E {
        N.checkArgNotEmpty(c, "matrixes");

        return zipToDouble(c, zipFunction, false);
    }

    /**
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @param shareLongermediateArray
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<LongMatrix> c, final Throwables.LongNFunction<Double, E> zipFunction,
            final boolean shareLongermediateArray) throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

        final int size = c.size();
        final LongMatrix[] matrixes = c.toArray(new LongMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareLongermediateArray && !zipInParallel;
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
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> DoubleMatrix zip(final DoubleMatrix a, final DoubleMatrix b, final Throwables.DoubleBinaryOperator<E> zipFunction)
            throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     *
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <E extends Exception> DoubleMatrix zip(final DoubleMatrix a, final DoubleMatrix b, final DoubleMatrix c,
            final Throwables.DoubleTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     *
     *
     * @param <E>
     * @param c
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <E extends Exception> DoubleMatrix zip(final Collection<DoubleMatrix> c, final Throwables.DoubleBinaryOperator<E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <R>
     * @param <E>
     * @param c
     * @param zipFunction
     * @param targetElementType
     * @return
     * @throws E
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<DoubleMatrix> c, final Throwables.DoubleNFunction<? extends R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     *
     *
     * @param <R>
     * @param <E>
     * @param c
     * @param zipFunction
     * @param shareIntermediateArray
     * @param targetElementType
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <R, E extends Exception> Matrix<R> zip(final Collection<DoubleMatrix> c, final Throwables.DoubleNFunction<? extends R, E> zipFunction,
            final boolean shareIntermediateArray, final Class<R> targetElementType) throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <A>
     * @param <B>
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <A, B, E extends Exception> Matrix<A> zip(final Matrix<A> a, final Matrix<B> b,
            final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    /**
     *
     *
     * @param <A>
     * @param <B>
     * @param <R>
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @param targetElementType
     * @return
     * @throws E
     */
    public static <A, B, R, E extends Exception> Matrix<R> zip(final Matrix<A> a, final Matrix<B> b,
            final Throwables.BiFunction<? super A, ? super B, R, E> zipFunction, final Class<R> targetElementType) throws E {
        return a.zipWith(b, zipFunction, targetElementType);
    }

    /**
     *
     *
     * @param <A>
     * @param <B>
     * @param <C>
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E
     */
    public static <A, B, C, E extends Exception> Matrix<A> zip(final Matrix<A> a, final Matrix<B> b, final Matrix<C> c,
            final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    /**
     *
     *
     * @param <A>
     * @param <B>
     * @param <C>
     * @param <R>
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @param targetElementType
     * @return
     * @throws E
     */
    public static <A, B, C, R, E extends Exception> Matrix<R> zip(final Matrix<A> a, final Matrix<B> b, final Matrix<C> c,
            final Throwables.TriFunction<? super A, ? super B, ? super C, R, E> zipFunction, final Class<R> targetElementType) throws E {
        return a.zipWith(b, c, zipFunction, targetElementType);
    }

    /**
     *
     *
     * @param <T>
     * @param <E>
     * @param c
     * @param zipFunction
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <T, E extends Exception> Matrix<T> zip(final Collection<Matrix<T>> c, final Throwables.BinaryOperator<T, E> zipFunction)
            throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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
     *
     *
     * @param <T>
     * @param <R>
     * @param <E>
     * @param c
     * @param zipFunction
     * @param targetElementType
     * @return
     * @throws E
     */
    public static <T, R, E extends Exception> Matrix<R> zip(final Collection<Matrix<T>> c, final Throwables.Function<? super T[], R, E> zipFunction,
            final Class<R> targetElementType) throws E {
        return zip(c, zipFunction, false, targetElementType);
    }

    /**
     *
     *
     * @param <T>
     * @param <R>
     * @param <E>
     * @param c
     * @param zipFunction
     * @param shareIntermediateArray
     * @param targetElementType
     * @return
     * @throws IllegalArgumentException
     * @throws E
     */
    public static <T, R, E extends Exception> Matrix<R> zip(final Collection<Matrix<T>> c, final Throwables.Function<? super T[], R, E> zipFunction,
            final boolean shareIntermediateArray, final Class<R> targetElementType) throws IllegalArgumentException, E {
        N.checkArgNotEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

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

}
