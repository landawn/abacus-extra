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
import com.landawn.abacus.util.function.IntConsumer;
import com.landawn.abacus.util.stream.IntStream;

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

    public static void setParallelEnabled(final ParallelEnabled flag) {
        N.checkArgNotNull(flag);

        isParallelEnabled_TL.set(flag);
    }

    public static ParallelEnabled getParallelEnabled() {
        return isParallelEnabled_TL.get();
    }

    public static boolean isParallelable(final AbstractMatrix<?, ?, ?, ?, ?> x) {
        return isParallelStreamSupported && (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.YES
                || (Matrixes.isParallelEnabled_TL.get() == ParallelEnabled.DEFAULT && x.count > MIN_COUNT_FOR_PARALLEL));
    }

    public static boolean isParallelable(final AbstractMatrix<?, ?, ?, ?, ?> x, final int bm) {
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

    public static <T> T[][] newArray(final Class<T> targetElementType, final int rows, final int cols) {
        final Class<T> eleType = (Class<T>) N.wrap(targetElementType);
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
     * @param <E>
     * @param fromRowIndex
     * @param toRowIndex
     * @param fromColumnIndex
     * @param toColumnIndex
     * @param cmd
     * @param inParallel
     * @throws E
     */
    public static <E extends Exception> void run(final int fromRowIndex, final int toRowIndex, final int fromColumnIndex, final int toColumnIndex,
            final Throwables.IntBiConsumer<E> cmd, final boolean inParallel) throws E {
        N.checkFromToIndex(fromRowIndex, toRowIndex, Integer.MAX_VALUE);
        N.checkFromToIndex(fromColumnIndex, toColumnIndex, Integer.MAX_VALUE);

        final int rows = toRowIndex - fromRowIndex;
        final int cols = toColumnIndex - fromColumnIndex;

        if (inParallel) {
            if (rows <= cols) {
                IntStream.range(fromRowIndex, toRowIndex).parallel().forEach(new Throwables.IntConsumer<E>() {
                    @Override
                    public void accept(final int i) throws E {
                        for (int j = fromColumnIndex; toColumnIndex < cols; j++) {
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
     * @param <X>
     * @param a
     * @param b
     * @param cmd
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> void multiply(final X a, final X b, final Throwables.IntTriConsumer<RuntimeException> cmd) {
        N.checkArgument(a.cols == b.rows, "Illegal matrix dimensions");

        multiply(a, b, cmd, Matrixes.isParallelable(a, b.cols));
    }

    /**
     * 
     * @param <X>
     * @param a
     * @param b
     * @param cmd
     * @param inParallel
     */
    public static <X extends AbstractMatrix<?, ?, ?, ?, ?>> void multiply(final X a, final X b, final Throwables.IntTriConsumer<RuntimeException> cmd,
            final boolean inParallel) {
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

    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final ByteMatrix c,
            final Throwables.ByteTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <E extends Exception> ByteMatrix zip(final Collection<ByteMatrix> c, final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
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
            result[i][j] = matrixes[0].a[i][j];

            for (int k = 1; k < size; k++) {
                result[i][j] = zipFunction.applyAsByte(result[i][j], matrixes[k].a[i][j]);
            }
        };

        run(rows, cols, cmd, Matrixes.isParallelable(matrixes[0]));

        return new ByteMatrix(result);
    }

    public static <R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Collection<ByteMatrix> c,
            final ByteNFunction<R, E> zipFunction) throws E {
        return zip(targetElementType, c, zipFunction, false);
    }

    public static <R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Collection<ByteMatrix> c,
            final ByteNFunction<R, E> zipFunction, final boolean shareIntermediateArray) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

        final int size = c.size();
        final ByteMatrix[] matrixes = c.toArray(new ByteMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && zipInParallel == false;
        final byte[] intermediateArray = new byte[size];
        final R[][] result = newArray(targetElementType, rows, cols);

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

    public static <E extends Exception> IntMatrix zipToInt(final ByteMatrix a, final ByteMatrix b, final ByteMatrix c,
            final Throwables.ByteTriFunction<Integer, E> zipFunction) throws E {
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

    public static <E extends Exception> IntMatrix zipToInt(final Collection<ByteMatrix> c, final ByteNFunction<Integer, E> zipFunction) throws E {
        return zipToInt(c, zipFunction, false);
    }

    public static <E extends Exception> IntMatrix zipToInt(final Collection<ByteMatrix> c, final ByteNFunction<Integer, E> zipFunction,
            final boolean shareIntermediateArray) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

        final int size = c.size();
        final ByteMatrix[] matrixes = c.toArray(new ByteMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && zipInParallel == false;
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

    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <E extends Exception> IntMatrix zip(final Collection<IntMatrix> c, final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
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
            result[i][j] = matrixes[0].a[i][j];

            for (int k = 1; k < size; k++) {
                result[i][j] = zipFunction.applyAsInt(result[i][j], matrixes[k].a[i][j]);
            }
        };

        run(rows, cols, cmd, Matrixes.isParallelable(matrixes[0]));

        return new IntMatrix(result);
    }

    public static <R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Collection<IntMatrix> c, final IntNFunction<R, E> zipFunction)
            throws E {
        return zip(targetElementType, c, zipFunction, false);
    }

    public static <R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Collection<IntMatrix> c, final IntNFunction<R, E> zipFunction,
            final boolean shareIntermediateArray) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

        final int size = c.size();
        final IntMatrix[] matrixes = c.toArray(new IntMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && zipInParallel == false;
        final int[] intermediateArray = new int[size];
        final R[][] result = newArray(targetElementType, rows, cols);

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

    public static <E extends Exception> LongMatrix zipToLong(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTriFunction<Long, E> zipFunction) throws E {
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

    public static <E extends Exception> LongMatrix zipToLong(final Collection<IntMatrix> c, final IntNFunction<Long, E> zipFunction) throws E {
        return zipToLong(c, zipFunction, false);
    }

    public static <E extends Exception> LongMatrix zipToLong(final Collection<IntMatrix> c, final IntNFunction<Long, E> zipFunction,
            final boolean shareIntermediateArray) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

        final int size = c.size();
        final IntMatrix[] matrixes = c.toArray(new IntMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && zipInParallel == false;
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

    public static <E extends Exception> DoubleMatrix zipToDouble(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTriFunction<Double, E> zipFunction) throws E {
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

    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<IntMatrix> c, final IntNFunction<Double, E> zipFunction) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");

        return zipToDouble(c, zipFunction, false);
    }

    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<IntMatrix> c, final IntNFunction<Double, E> zipFunction,
            final boolean shareIntermediateArray) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

        final int size = c.size();
        final IntMatrix[] matrixes = c.toArray(new IntMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && zipInParallel == false;
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

    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final LongMatrix c,
            final Throwables.LongTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <E extends Exception> LongMatrix zip(final Collection<LongMatrix> c, final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
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
            result[i][j] = matrixes[0].a[i][j];

            for (int k = 1; k < size; k++) {
                result[i][j] = zipFunction.applyAsLong(result[i][j], matrixes[k].a[i][j]);
            }
        };

        run(rows, cols, cmd, Matrixes.isParallelable(matrixes[0]));

        return new LongMatrix(result);
    }

    public static <R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Collection<LongMatrix> c,
            final LongNFunction<R, E> zipFunction) throws E {
        return zip(targetElementType, c, zipFunction, false);
    }

    public static <R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Collection<LongMatrix> c,
            final LongNFunction<R, E> zipFunction, final boolean shareIntermediateArray) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

        final int size = c.size();
        final LongMatrix[] matrixes = c.toArray(new LongMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && zipInParallel == false;
        final long[] intermediateArray = new long[size];
        final R[][] result = newArray(targetElementType, rows, cols);

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

    public static <E extends Exception> DoubleMatrix zipToDouble(final LongMatrix a, final LongMatrix b, final LongMatrix c,
            final Throwables.LongTriFunction<Double, E> zipFunction) throws E {
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

    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<LongMatrix> c, final LongNFunction<Double, E> zipFunction) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");

        return zipToDouble(c, zipFunction, false);
    }

    public static <E extends Exception> DoubleMatrix zipToDouble(final Collection<LongMatrix> c, final LongNFunction<Double, E> zipFunction,
            final boolean shareLongermediateArray) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

        final int size = c.size();
        final LongMatrix[] matrixes = c.toArray(new LongMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareLongermediateArray && zipInParallel == false;
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

    public static <E extends Exception> DoubleMatrix zip(final DoubleMatrix a, final DoubleMatrix b, final Throwables.DoubleBinaryOperator<E> zipFunction)
            throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> DoubleMatrix zip(final DoubleMatrix a, final DoubleMatrix b, final DoubleMatrix c,
            final Throwables.DoubleTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <E extends Exception> DoubleMatrix zip(final Collection<DoubleMatrix> c, final Throwables.DoubleBinaryOperator<E> zipFunction) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
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
            result[i][j] = matrixes[0].a[i][j];

            for (int k = 1; k < size; k++) {
                result[i][j] = zipFunction.applyAsDouble(result[i][j], matrixes[k].a[i][j]);
            }
        };

        run(rows, cols, cmd, Matrixes.isParallelable(matrixes[0]));

        return new DoubleMatrix(result);
    }

    public static <R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Collection<DoubleMatrix> c,
            final DoubleNFunction<R, E> zipFunction) throws E {
        return zip(targetElementType, c, zipFunction, false);
    }

    public static <R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Collection<DoubleMatrix> c,
            final DoubleNFunction<R, E> zipFunction, final boolean shareIntermediateArray) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

        final int size = c.size();
        final DoubleMatrix[] matrixes = c.toArray(new DoubleMatrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && zipInParallel == false;
        final double[] intermediateArray = new double[size];
        final R[][] result = newArray(targetElementType, rows, cols);

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

    public static <A, B, E extends Exception> Matrix<A> zip(final Matrix<A> a, final Matrix<B> b,
            final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <A, B, R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Matrix<A> a, final Matrix<B> b,
            final Throwables.BiFunction<? super A, ? super B, R, E> zipFunction) throws E {
        return a.zipWith(targetElementType, b, zipFunction);
    }

    public static <A, B, C, E extends Exception> Matrix<A> zip(final Matrix<A> a, final Matrix<B> b, final Matrix<C> c,
            final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <A, B, C, R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Matrix<A> a, final Matrix<B> b, final Matrix<C> c,
            final Throwables.TriFunction<? super A, ? super B, ? super C, R, E> zipFunction) throws E {
        return a.zipWith(targetElementType, b, c, zipFunction);
    }

    public static <T, E extends Exception> Matrix<T> zip(final Collection<Matrix<T>> c, final Throwables.BinaryOperator<T, E> zipFunction) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
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
        final T[][] result = newArray(matrixes[0].elementType, rows, cols);

        final Throwables.IntBiConsumer<E> cmd = (i, j) -> {
            result[i][j] = matrixes[0].a[i][j];

            for (int k = 1; k < size; k++) {
                result[i][j] = zipFunction.apply(result[i][j], matrixes[k].a[i][j]);
            }
        };

        run(rows, cols, cmd, Matrixes.isParallelable(matrixes[0]));

        return new Matrix<>(result);
    }

    public static <T, R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Collection<Matrix<T>> c,
            final Throwables.Function<? super T[], R, E> zipFunction) throws E {
        return zip(targetElementType, c, zipFunction, false);
    }

    public static <T, R, E extends Exception> Matrix<R> zip(final Class<R> targetElementType, final Collection<Matrix<T>> c,
            final Throwables.Function<? super T[], R, E> zipFunction, final boolean shareIntermediateArray) throws E {
        N.checkArgNotNullOrEmpty(c, "matrixes");
        N.checkArgument(isSameShape(c), "Can't zip two or more matrices which don't have same shape");

        final int size = c.size();
        final Matrix<T>[] matrixes = c.toArray(new Matrix[size]);

        final int rows = matrixes[0].rows;
        final int cols = matrixes[0].cols;
        final boolean zipInParallel = Matrixes.isParallelable(matrixes[0]);
        final boolean shareArray = shareIntermediateArray && zipInParallel == false;
        final T[] intermediateArray = N.newArray(matrixes[0].elementType, size);
        final R[][] result = newArray(targetElementType, rows, cols);

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

    public static interface ByteNFunction<R, E extends Throwable> {

        R apply(byte... args) throws E;

        default <V> ByteNFunction<V, E> andThen(java.util.function.Function<? super R, ? extends V> after) {
            N.checkArgNotNull(after);

            return args -> after.apply(apply(args));
        }
    }

    public static interface IntNFunction<R, E extends Throwable> {

        R apply(int... args) throws E;

        default <V> IntNFunction<V, E> andThen(java.util.function.Function<? super R, ? extends V> after) {
            N.checkArgNotNull(after);

            return args -> after.apply(apply(args));
        }
    }

    public static interface LongNFunction<R, E extends Throwable> {

        R apply(long... args) throws E;

        default <V> LongNFunction<V, E> andThen(java.util.function.Function<? super R, ? extends V> after) {
            N.checkArgNotNull(after);

            return args -> after.apply(apply(args));
        }
    }

    public static interface DoubleNFunction<R, E extends Throwable> {

        R apply(double... args) throws E;

        default <V> DoubleNFunction<V, E> andThen(java.util.function.Function<? super R, ? extends V> after) {
            N.checkArgNotNull(after);

            return args -> after.apply(apply(args));
        }
    }

}
