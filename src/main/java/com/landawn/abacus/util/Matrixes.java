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

import java.util.Collection;
import java.util.Iterator;

import com.landawn.abacus.logging.Logger;
import com.landawn.abacus.logging.LoggerFactory;
import com.landawn.abacus.util.stream.IntStream;

public final class Matrixes {

    /** The Constant logger. */
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

    public static void setParallelEnabled(final ParallelEnabled flag) {
        N.checkArgNotNull(flag);

        isParallelEnabled_TL.set(flag);
    }

    public static ParallelEnabled getParallelEnabled() {
        return isParallelEnabled_TL.get();
    }

    static boolean isParallelable(final AbstractMatrix<?, ?, ?, ?, ?> x) {
        return isParallelStreamSupported && (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.YES
                || (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.DEFAULT && x.count > MIN_COUNT_FOR_PARALLEL));
    }

    static boolean isParallelable(final AbstractMatrix<?, ?, ?, ?, ?> x, final int bm) {
        return isParallelStreamSupported && (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.YES
                || (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.DEFAULT && x.count * bm > MIN_COUNT_FOR_PARALLEL));
    }

    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> boolean isSameShape(final X a, final X b) {
        return a.rows == b.rows && a.cols == b.cols;
    }

    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> boolean isSameShape(final X a, final X b, final X c) {
        return a.rows == b.rows && a.rows == c.rows && a.cols == b.cols && a.cols == c.cols;
    }

    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> boolean isSameShape(final Collection<? extends X> xs) {
        if (N.isNullOrEmpty(xs) || xs.size() == 1) {
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

    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final ByteMatrix c,
            final Throwables.ByteTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <E extends Exception> IntMatrix zipToInt(final ByteMatrix a, final ByteMatrix b, final Throwables.ByteBiFunction<Integer, E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

        final int rows = a.rows;
        final int cols = b.cols;
        final byte[][] aa = a.a;
        final byte[][] ba = b.a;
        final int[][] result = new int[rows][cols];

        if (isParallelable(a)) {
            if (rows <= cols) {
                IntStream.range(0, rows).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int i) throws E {
                        for (int j = 0; j < cols; j++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                        }
                    }
                });
            } else {
                IntStream.range(0, cols).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int j) throws E {
                        for (int i = 0; i < rows; i++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                        }
                    }
                });
            }
        } else {
            if (rows <= cols) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                    }
                }
            } else {
                for (int j = 0; j < cols; j++) {
                    for (int i = 0; i < rows; i++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                    }
                }
            }
        }

        return new IntMatrix(result);
    }

    public static <E extends Exception> IntMatrix zipToInt(final ByteMatrix a, final ByteMatrix b, final ByteMatrix c,
            final Throwables.ByteTriFunction<Integer, E> zipFunction) throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

        final int rows = a.rows;
        final int cols = b.cols;
        final byte[][] aa = a.a;
        final byte[][] ba = b.a;
        final byte[][] ca = c.a;
        final int[][] result = new int[rows][cols];

        if (isParallelable(a)) {
            if (rows <= cols) {
                IntStream.range(0, rows).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int i) throws E {
                        for (int j = 0; j < cols; j++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                        }
                    }
                });
            } else {
                IntStream.range(0, cols).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int j) throws E {
                        for (int i = 0; i < rows; i++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                        }
                    }
                });
            }
        } else {
            if (rows <= cols) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                    }
                }
            } else {
                for (int j = 0; j < cols; j++) {
                    for (int i = 0; i < rows; i++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                    }
                }
            }
        }

        return new IntMatrix(result);
    }

    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <E extends Exception> LongMatrix zipToLong(final IntMatrix a, final IntMatrix b, final Throwables.IntBiFunction<Long, E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

        final int rows = a.rows;
        final int cols = b.cols;
        final int[][] aa = a.a;
        final int[][] ba = b.a;
        final long[][] result = new long[rows][cols];

        if (isParallelable(a)) {
            if (rows <= cols) {
                IntStream.range(0, rows).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int i) throws E {
                        for (int j = 0; j < cols; j++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                        }
                    }
                });
            } else {
                IntStream.range(0, cols).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int j) throws E {
                        for (int i = 0; i < rows; i++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                        }
                    }
                });
            }
        } else {
            if (rows <= cols) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                    }
                }
            } else {
                for (int j = 0; j < cols; j++) {
                    for (int i = 0; i < rows; i++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                    }
                }
            }
        }

        return new LongMatrix(result);
    }

    public static <E extends Exception> LongMatrix zipToLong(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTriFunction<Long, E> zipFunction) throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

        final int rows = a.rows;
        final int cols = b.cols;
        final int[][] aa = a.a;
        final int[][] ba = b.a;
        final int[][] ca = c.a;
        final long[][] result = new long[rows][cols];

        if (isParallelable(a)) {
            if (rows <= cols) {
                IntStream.range(0, rows).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int i) throws E {
                        for (int j = 0; j < cols; j++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                        }
                    }
                });
            } else {
                IntStream.range(0, cols).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int j) throws E {
                        for (int i = 0; i < rows; i++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                        }
                    }
                });
            }
        } else {
            if (rows <= cols) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                    }
                }
            } else {
                for (int j = 0; j < cols; j++) {
                    for (int i = 0; i < rows; i++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                    }
                }
            }
        }

        return new LongMatrix(result);
    }

    public static <E extends Exception> DoubleMatrix zipToDouble(final IntMatrix a, final IntMatrix b, final Throwables.IntBiFunction<Double, E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

        final int rows = a.rows;
        final int cols = b.cols;
        final int[][] aa = a.a;
        final int[][] ba = b.a;
        final double[][] result = new double[rows][cols];

        if (isParallelable(a)) {
            if (rows <= cols) {
                IntStream.range(0, rows).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int i) throws E {
                        for (int j = 0; j < cols; j++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                        }
                    }
                });
            } else {
                IntStream.range(0, cols).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int j) throws E {
                        for (int i = 0; i < rows; i++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                        }
                    }
                });
            }
        } else {
            if (rows <= cols) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                    }
                }
            } else {
                for (int j = 0; j < cols; j++) {
                    for (int i = 0; i < rows; i++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                    }
                }
            }
        }

        return new DoubleMatrix(result);
    }

    public static <E extends Exception> DoubleMatrix zipToDouble(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTriFunction<Double, E> zipFunction) throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

        final int rows = a.rows;
        final int cols = b.cols;
        final int[][] aa = a.a;
        final int[][] ba = b.a;
        final int[][] ca = c.a;
        final double[][] result = new double[rows][cols];

        if (isParallelable(a)) {
            if (rows <= cols) {
                IntStream.range(0, rows).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int i) throws E {
                        for (int j = 0; j < cols; j++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                        }
                    }
                });
            } else {
                IntStream.range(0, cols).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int j) throws E {
                        for (int i = 0; i < rows; i++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                        }
                    }
                });
            }
        } else {
            if (rows <= cols) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                    }
                }
            } else {
                for (int j = 0; j < cols; j++) {
                    for (int i = 0; i < rows; i++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                    }
                }
            }
        }

        return new DoubleMatrix(result);
    }

    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final LongMatrix c,
            final Throwables.LongTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <E extends Exception> DoubleMatrix zipToDouble(final LongMatrix a, final LongMatrix b, final Throwables.LongBiFunction<Double, E> zipFunction)
            throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

        final int rows = a.rows;
        final int cols = b.cols;
        final long[][] aa = a.a;
        final long[][] ba = b.a;
        final double[][] result = new double[rows][cols];

        if (isParallelable(a)) {
            if (rows <= cols) {
                IntStream.range(0, rows).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int i) throws E {
                        for (int j = 0; j < cols; j++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                        }
                    }
                });
            } else {
                IntStream.range(0, cols).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int j) throws E {
                        for (int i = 0; i < rows; i++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                        }
                    }
                });
            }
        } else {
            if (rows <= cols) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                    }
                }
            } else {
                for (int j = 0; j < cols; j++) {
                    for (int i = 0; i < rows; i++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j]);
                    }
                }
            }
        }

        return new DoubleMatrix(result);
    }

    public static <E extends Exception> DoubleMatrix zipToDouble(final LongMatrix a, final LongMatrix b, final LongMatrix c,
            final Throwables.LongTriFunction<Double, E> zipFunction) throws E {
        N.checkArgument(isSameShape(a, b), "Can't zip two or more matrices which don't have same shape.");

        final int rows = a.rows;
        final int cols = b.cols;
        final long[][] aa = a.a;
        final long[][] ba = b.a;
        final long[][] ca = c.a;
        final double[][] result = new double[rows][cols];

        if (isParallelable(a)) {
            if (rows <= cols) {
                IntStream.range(0, rows).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int i) throws E {
                        for (int j = 0; j < cols; j++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                        }
                    }
                });
            } else {
                IntStream.range(0, cols).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int j) throws E {
                        for (int i = 0; i < rows; i++) {
                            result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                        }
                    }
                });
            }
        } else {
            if (rows <= cols) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                    }
                }
            } else {
                for (int j = 0; j < cols; j++) {
                    for (int i = 0; i < rows; i++) {
                        result[i][j] = zipFunction.apply(aa[i][j], ba[i][j], ca[i][j]);
                    }
                }
            }
        }

        return new DoubleMatrix(result);
    }

    public static <E extends Exception> DoubleMatrix zip(final DoubleMatrix a, final DoubleMatrix b, final Throwables.DoubleBinaryOperator<E> zipFunction)
            throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> DoubleMatrix zip(final DoubleMatrix a, final DoubleMatrix b, final DoubleMatrix c,
            final Throwables.DoubleTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <A, B, E extends Exception> Matrix<A> zip(final Matrix<A> a, final Matrix<B> b,
            final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <A, B, R, E extends Exception> Matrix<R> zip(final Class<R> targetComponentType, final Matrix<A> a, final Matrix<B> b,
            final Throwables.BiFunction<? super A, ? super B, R, E> zipFunction) throws E {
        return a.zipWith(targetComponentType, b, zipFunction);
    }

    public static <A, B, C, E extends Exception> Matrix<A> zip(final Matrix<A> a, final Matrix<B> b, final Matrix<C> c,
            final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <A, B, C, R, E extends Exception> Matrix<R> zip(final Class<R> targetComponentType, final Matrix<A> a, final Matrix<B> b, final Matrix<C> c,
            final Throwables.TriFunction<? super A, ? super B, ? super C, R, E> zipFunction) throws E {
        return a.zipWith(targetComponentType, b, c, zipFunction);
    }

}
