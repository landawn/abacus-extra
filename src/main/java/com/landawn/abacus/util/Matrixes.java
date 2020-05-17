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

import com.landawn.abacus.logging.Logger;
import com.landawn.abacus.logging.LoggerFactory;

public final class Matrixes {

    /** The Constant logger. */
    static final Logger logger = LoggerFactory.getLogger(Matrixes.class);

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

    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> ByteMatrix zip(final ByteMatrix a, final ByteMatrix b, final ByteMatrix c,
            final Throwables.ByteTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> IntMatrix zip(final IntMatrix a, final IntMatrix b, final IntMatrix c,
            final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
    }

    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, zipFunction);
    }

    public static <E extends Exception> LongMatrix zip(final LongMatrix a, final LongMatrix b, final LongMatrix c,
            final Throwables.LongTernaryOperator<E> zipFunction) throws E {
        return a.zipWith(b, c, zipFunction);
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
