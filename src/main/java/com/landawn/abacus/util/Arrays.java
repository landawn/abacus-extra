/*
 * Copyright (C) 2017 HaiYang Li
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

import java.math.RoundingMode;

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.annotation.SuppressFBWarnings;

/**
 * @implSpec classes/interfaces implemented in abacus-extra are not for daily use case. It's not going to handle {@code null} scenarios like what handled in abacus-common. It's developer's responsibility to handle {@code null} scenarios. // TODO
 *
 *
 */
@Beta
public sealed class Arrays permits Arrays.f {

    static final char CHAR_0 = (char) 0;
    static final byte BYTE_0 = (byte) 0;
    static final byte BYTE_1 = (byte) 1;
    static final short SHORT_0 = (short) 0;
    private static final String ARRAY_PRINT_SEPARATOR = IOUtil.LINE_SEPARATOR;

    private Arrays() {
        // utility class.
    }

    /**
     *
     *
     * @param <T>
     * @param <R>
     * @param <E>
     * @param a
     * @param func
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, R, E extends Exception> R[] map(final T[] a, final Throwables.Function<? super T, ? extends R, E> func, final Class<R> targetElementType)
            throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final R[] c = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            c[i] = func.apply(a[i]);
        }

        return c;
    }

    /**
     * Map to boolean.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param func
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> boolean[] mapToBoolean(final T[] a, final Throwables.ToBooleanFunction<? super T, E> func) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final boolean[] c = new boolean[len];

        for (int i = 0; i < len; i++) {
            c[i] = func.applyAsBoolean(a[i]);
        }

        return c;
    }

    /**
     * Map to char.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param func
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> char[] mapToChar(final T[] a, final Throwables.ToCharFunction<? super T, E> func) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final char[] c = new char[len];

        for (int i = 0; i < len; i++) {
            c[i] = func.applyAsChar(a[i]);
        }

        return c;
    }

    /**
     * Map to byte.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param func
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> byte[] mapToByte(final T[] a, final Throwables.ToByteFunction<? super T, E> func) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final byte[] c = new byte[len];

        for (int i = 0; i < len; i++) {
            c[i] = func.applyAsByte(a[i]);
        }

        return c;
    }

    /**
     * Map to short.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param func
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> short[] mapToShort(final T[] a, final Throwables.ToShortFunction<? super T, E> func) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final short[] c = new short[len];

        for (int i = 0; i < len; i++) {
            c[i] = func.applyAsShort(a[i]);
        }

        return c;
    }

    /**
     * Map to int.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param func
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> int[] mapToInt(final T[] a, final Throwables.ToIntFunction<? super T, E> func) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[] c = new int[len];

        for (int i = 0; i < len; i++) {
            c[i] = func.applyAsInt(a[i]);
        }

        return c;
    }

    /**
     * Map to long.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param func
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> long[] mapToLong(final T[] a, final Throwables.ToLongFunction<? super T, E> func) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[] c = new long[len];

        for (int i = 0; i < len; i++) {
            c[i] = func.applyAsLong(a[i]);
        }

        return c;
    }

    /**
     * Map to float.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param func
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> float[] mapToFloat(final T[] a, final Throwables.ToFloatFunction<? super T, E> func) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[] c = new float[len];

        for (int i = 0; i < len; i++) {
            c[i] = func.applyAsFloat(a[i]);
        }

        return c;
    }

    /**
     * Map to double.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param func
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> double[] mapToDouble(final T[] a, final Throwables.ToDoubleFunction<? super T, E> func) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[] c = new double[len];

        for (int i = 0; i < len; i++) {
            c[i] = func.applyAsDouble(a[i]);
        }

        return c;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[] mapToObj(final boolean[] a, final Throwables.BooleanFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][] mapToObj(final boolean[][] a, final Throwables.BooleanFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][][] mapToObj(final boolean[][][] a, final Throwables.BooleanFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[] mapToObj(final char[] a, final Throwables.CharFunction<? extends T, E> mapper, final Class<T> targetElementType)
            throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][] mapToObj(final char[][] a, final Throwables.CharFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][][] mapToObj(final char[][][] a, final Throwables.CharFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[] mapToObj(final byte[] a, final Throwables.ByteFunction<? extends T, E> mapper, final Class<T> targetElementType)
            throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][] mapToObj(final byte[][] a, final Throwables.ByteFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][][] mapToObj(final byte[][][] a, final Throwables.ByteFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[] mapToObj(final short[] a, final Throwables.ShortFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][] mapToObj(final short[][] a, final Throwables.ShortFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][][] mapToObj(final short[][][] a, final Throwables.ShortFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[] mapToObj(final int[] a, final Throwables.IntFunction<? extends T, E> mapper, final Class<T> targetElementType)
            throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][] mapToObj(final int[][] a, final Throwables.IntFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][][] mapToObj(final int[][][] a, final Throwables.IntFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[] mapToObj(final long[] a, final Throwables.LongFunction<? extends T, E> mapper, final Class<T> targetElementType)
            throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][] mapToObj(final long[][] a, final Throwables.LongFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][][] mapToObj(final long[][][] a, final Throwables.LongFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[] mapToObj(final float[] a, final Throwables.FloatFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][] mapToObj(final float[][] a, final Throwables.FloatFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][][] mapToObj(final float[][][] a, final Throwables.FloatFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[] mapToObj(final double[] a, final Throwables.DoubleFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][] mapToObj(final double[][] a, final Throwables.DoubleFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Map to obj.
     *
     * @param <T>
     * @param <E>
     * @param a
     * @param mapper
     * @param targetElementType
     * @return
     * @throws E the e
     */
    public static <T, E extends Exception> T[][][] mapToObj(final double[][][] a, final Throwables.DoubleFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final T[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[] mapToLong(final int[] a, final Throwables.IntToLongFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[] result = new long[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][] mapToLong(final int[][] a, final Throwables.IntToLongFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][] result = new long[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToLong(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][][] mapToLong(final int[][][] a, final Throwables.IntToLongFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][][] result = new long[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToLong(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[] mapToDouble(final int[] a, final Throwables.IntToDoubleFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[] result = new double[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsDouble(a[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][] mapToDouble(final int[][] a, final Throwables.IntToDoubleFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][] result = new double[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToDouble(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][][] mapToDouble(final int[][][] a, final Throwables.IntToDoubleFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][][] result = new double[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToDouble(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[] mapToInt(final long[] a, final Throwables.LongToIntFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][] mapToInt(final long[][] a, final Throwables.LongToIntFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToInt(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][][] mapToInt(final long[][][] a, final Throwables.LongToIntFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][][] result = new int[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToInt(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[] mapToDouble(final long[] a, final Throwables.LongToDoubleFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[] result = new double[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsDouble(a[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][] mapToDouble(final long[][] a, final Throwables.LongToDoubleFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][] result = new double[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToDouble(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][][] mapToDouble(final long[][][] a, final Throwables.LongToDoubleFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][][] result = new double[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToDouble(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[] mapToInt(final double[] a, final Throwables.DoubleToIntFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][] mapToInt(final double[][] a, final Throwables.DoubleToIntFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToInt(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][][] mapToInt(final double[][][] a, final Throwables.DoubleToIntFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][][] result = new int[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToInt(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[] mapToLong(final double[] a, final Throwables.DoubleToLongFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[] result = new long[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][] mapToLong(final double[][] a, final Throwables.DoubleToLongFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][] result = new long[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToLong(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param mapper
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][][] mapToLong(final double[][][] a, final Throwables.DoubleToLongFunction<E> mapper) throws E {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][][] result = new long[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToLong(a[i], mapper);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final Object[] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            return N.println(N.toString(a));
        }
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final Object[][] a) {
        return ff.println(a);
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final Object[][][] a) {
        return fff.println(a);
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final boolean[] a, final Throwables.BooleanUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] = operator.applyAsBoolean(a[i]);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final boolean[][] a, final Throwables.BooleanUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final boolean[] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final boolean[][][] a, final Throwables.BooleanUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final boolean[][] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final boolean[] a, final Throwables.BooleanPredicate<E> predicate, final boolean newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            if (predicate.test(a[i])) {
                a[i] = newValue;
            }
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final boolean[][] a, final Throwables.BooleanPredicate<E> predicate, final boolean newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final boolean[] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final boolean[][][] a, final Throwables.BooleanPredicate<E> predicate, final boolean newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final boolean[][] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     * Reshapes a one-dimensional boolean array into a two-dimensional boolean array with the specified number of columns.
     *
     * @param a the one-dimensional boolean array to reshape
     * @param cols the number of columns for the reshaped array
     * @return a two-dimensional boolean array with the specified number of columns
     * @throws IllegalArgumentException if the number of columns is less than or equal to zero
     */
    public static boolean[][] reshape(final boolean[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new boolean[0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, cols, RoundingMode.CEILING);
        final boolean[][] c = new boolean[n][];

        for (int i = 0, from = 0; i < n; i++, from += cols) {
            c[i] = N.copyOfRange(a, from, from + N.min(len - from, cols));
        }

        return c;
    }

    /**
     * Reshapes a one-dimensional boolean array into a three-dimensional boolean array with the specified number of rows and columns.
     *
     * @param a the one-dimensional boolean array to reshape
     * @param rows the number of rows for the reshaped sub-array
     * @param cols the number of columns for the reshaped sub-array
     * @return a three-dimensional boolean array with the specified number of rows and columns
     * @throws IllegalArgumentException if the number of rows or columns is less than or equal to zero
     */
    public static boolean[][][] reshape(final boolean[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new boolean[0][0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, rows * cols, RoundingMode.CEILING);
        final boolean[][][] c = new boolean[n][][];

        for (int i = 0, from = 0; i < n; i++) {
            c[i] = new boolean[N.min(rows, Numbers.divide(len - from, cols, RoundingMode.CEILING))][];

            for (int j = 0, y = c[i].length; j < y; j++, from += cols) {
                c[i][j] = N.copyOfRange(a, from, from + N.min(len - from, cols));
            }
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean[] flatten(final boolean[][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_BOOLEAN_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final boolean[] c = new boolean[count];
        int from = 0;

        for (final boolean[] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            N.copy(element, 0, c, from, element.length);

            from += element.length;
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean[] flatten(final boolean[][][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_BOOLEAN_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final boolean[] c = new boolean[count];
        int from = 0;

        for (final boolean[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final boolean[] booleans : element) {
                if (N.isEmpty(booleans)) {
                    continue;
                }

                N.copy(booleans, 0, c, from, booleans.length);

                from += booleans.length;
            }
        }

        return c;
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final boolean[][] a, final Throwables.Consumer<? super boolean[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final boolean[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final boolean[] e : a) {
            if (N.notEmpty(e)) {
                N.copy(tmp, idx, e, 0, e.length);
                idx += e.length;
            }
        }
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final boolean[][][] a, final Throwables.Consumer<? super boolean[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final boolean[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final boolean[][] e : a) {
            if (N.notEmpty(e)) {
                for (final boolean[] ee : e) {
                    if (N.notEmpty(ee)) {
                        N.copy(tmp, idx, ee, 0, ee.length);
                        idx += ee.length;
                    }
                }
            }
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[] zip(final boolean[] a, final boolean[] b, final Throwables.BooleanBiFunction<Boolean, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final boolean[] result = new boolean[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[] zip(final boolean[] a, final boolean[] b, final boolean valueForNoneA, final boolean valueForNoneB,
            final Throwables.BooleanBiFunction<Boolean, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return zip(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> boolean[] zip(final int len, final boolean[] a, final boolean[] b, final boolean valueForNoneA,
            final boolean valueForNoneB, final Throwables.BooleanBiFunction<Boolean, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final boolean[] result = new boolean[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zipFunction.apply(valueForNoneA, b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zipFunction.apply(a[i], valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zipFunction.apply(valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[] zip(final boolean[] a, final boolean[] b, final boolean[] c,
            final Throwables.BooleanTriFunction<Boolean, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final boolean[] result = new boolean[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[] zip(final boolean[] a, final boolean[] b, final boolean[] c, final boolean valueForNoneA,
            final boolean valueForNoneB, final boolean valueForNoneC, final Throwables.BooleanTriFunction<Boolean, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return zip(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> boolean[] zip(final int len, final boolean[] a, final boolean[] b, final boolean[] c, final boolean valueForNoneA,
            final boolean valueForNoneB, final boolean valueForNoneC, final Throwables.BooleanTriFunction<Boolean, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final boolean[] result = new boolean[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zipFunction.apply(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[][] zip(final boolean[][] a, final boolean[][] b, final Throwables.BooleanBiFunction<Boolean, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final boolean[][] result = new boolean[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[][] zip(final boolean[][] a, final boolean[][] b, final boolean valueForNoneA, final boolean valueForNoneB,
            final Throwables.BooleanBiFunction<Boolean, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> boolean[][] zip(final int len, final int rowLen, final boolean[][] a, final boolean[][] b, final boolean valueForNoneA,
            final boolean valueForNoneB, final Throwables.BooleanBiFunction<Boolean, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final boolean[][] result = new boolean[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zip(rowLen, null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zip(rowLen, a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zip(rowLen, null, null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[][] zip(final boolean[][] a, final boolean[][] b, final boolean[][] c,
            final Throwables.BooleanTriFunction<Boolean, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final boolean[][] result = new boolean[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[][] zip(final boolean[][] a, final boolean[][] b, final boolean[][] c, final boolean valueForNoneA,
            final boolean valueForNoneB, final boolean valueForNoneC, final Throwables.BooleanTriFunction<Boolean, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> boolean[][] zip(final int len, final int rowLen, final boolean[][] a, final boolean[][] b, final boolean[][] c,
            final boolean valueForNoneA, final boolean valueForNoneB, final boolean valueForNoneC, final Throwables.BooleanTriFunction<Boolean, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final boolean[][] result = new boolean[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zip(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[][][] zip(final boolean[][][] a, final boolean[][][] b,
            final Throwables.BooleanBiFunction<Boolean, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final boolean[][][] result = new boolean[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[][][] zip(final boolean[][][] a, final boolean[][][] b, final boolean valueForNoneA,
            final boolean valueForNoneB, final Throwables.BooleanBiFunction<Boolean, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final boolean[][][] result = new boolean[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[][][] zip(final boolean[][][] a, final boolean[][][] b, final boolean[][][] c,
            final Throwables.BooleanTriFunction<Boolean, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final boolean[][][] result = new boolean[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> boolean[][][] zip(final boolean[][][] a, final boolean[][][] b, final boolean[][][] c, final boolean valueForNoneA,
            final boolean valueForNoneB, final boolean valueForNoneC, final Throwables.BooleanTriFunction<Boolean, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final boolean[][][] result = new boolean[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    public static long totalCountOfElements(final boolean[][] a) {
        long count = 0;

        for (final boolean[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    public static long totalCountOfElements(final boolean[][][] a) {
        long count = 0;

        for (final boolean[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final boolean[] booleans : element) {
                if (N.isEmpty(booleans)) {
                    continue;
                }

                count += booleans.length;
            }
        }

        return count;
    }

    /**
     * Min sub array len.
     *
     * @param a
     * @return
     */
    public static int minSubArrayLen(final boolean[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;

        for (final boolean[] ae : a) {
            minLen = N.min(minLen, ae == null ? 0 : ae.length);
        }

        return minLen;
    }

    /**
     * Max sub array len.
     *
     * @param a
     * @return
     */
    public static int maxSubArrayLen(final boolean[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int maxLen = 0;

        for (final boolean[] ae : a) {
            maxLen = N.max(maxLen, ae == null ? 0 : ae.length);
        }

        return maxLen;
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final boolean[] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            return N.println(N.toString(a));
        }
    }

    /**
     *
     * @param a
     */
    public static String println(final boolean[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final boolean[] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(ai[j]);
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final boolean[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final boolean[][] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR).append("  ");
                            }

                            if (ai[j] == null) {
                                sb.append("null");
                            } else if (ai[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final boolean[] aij = ai[j];
                                sb.append('[');

                                for (int k = 0, aijLen = aij.length; k < aijLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(aij[k]);
                                }

                                sb.append(']');
                            }
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            N.println(str);

            return str;
        }
    }

    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[], Throwables.CharUnaryOperator)
    //     */
    //    public static void plus(final char[] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (int i = 0, n = a.length; i < n; i++) {
    //            a[i] += param;
    //        }
    //    }
    //
    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[][], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[][], Throwables.CharUnaryOperator)
    //     */
    //    public static void plus(final char[][] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (final char[] element : a) {
    //            plus(element, param);
    //        }
    //    }
    //
    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[][][], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[][][], Throwables.CharUnaryOperator)
    //     */
    //    public static void plus(final char[][][] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (final char[][] element : a) {
    //            plus(element, param);
    //        }
    //    }
    //
    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[], Throwables.CharUnaryOperator)
    //     */
    //    public static void minus(final char[] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (int i = 0, n = a.length; i < n; i++) {
    //            a[i] -= param;
    //        }
    //    }
    //
    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[][], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[][], Throwables.CharUnaryOperator)
    //     */
    //    public static void minus(final char[][] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (final char[] element : a) {
    //            minus(element, param);
    //        }
    //    }
    //
    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[][][], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[][][], Throwables.CharUnaryOperator)
    //     */
    //    public static void minus(final char[][][] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (final char[][] element : a) {
    //            minus(element, param);
    //        }
    //    }
    //
    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[], Throwables.CharUnaryOperator)
    //     */
    //    public static void multipliedBy(final char[] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (int i = 0, n = a.length; i < n; i++) {
    //            a[i] *= param;
    //        }
    //    }
    //
    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[][], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[][], Throwables.CharUnaryOperator)
    //     */
    //    public static void multipliedBy(final char[][] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (final char[] element : a) {
    //            multipliedBy(element, param);
    //        }
    //    }
    //
    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[][][], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[][][], Throwables.CharUnaryOperator)
    //     */
    //    public static void multipliedBy(final char[][][] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (final char[][] element : a) {
    //            multipliedBy(element, param);
    //        }
    //    }
    //
    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[], Throwables.CharUnaryOperator)
    //     */
    //    public static void dividedBy(final char[] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (int i = 0, n = a.length; i < n; i++) {
    //            a[i] /= param;
    //        }
    //    }
    //
    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[][], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[][], Throwables.CharUnaryOperator)
    //     */
    //    public static void dividedBy(final char[][] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (final char[] element : a) {
    //            dividedBy(element, param);
    //        }
    //    }
    //
    //    /**
    //     *
    //     * @param a
    //     * @param param
    //     * @deprecated replaced by {@link #replaceAll(char[][][], Throwables.CharUnaryOperator)}
    //     * @see #replaceAll(char[][][], Throwables.CharUnaryOperator)
    //     */
    //    public static void dividedBy(final char[][][] a, final int param) {
    //        if (N.isEmpty(a)) {
    //            return;
    //        }
    //
    //        for (final char[][] element : a) {
    //            dividedBy(element, param);
    //        }
    //    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final char[] a, final Throwables.CharUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] = operator.applyAsChar(a[i]);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final char[][] a, final Throwables.CharUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final char[] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final char[][][] a, final Throwables.CharUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final char[][] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final char[] a, final Throwables.CharPredicate<E> predicate, final char newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            if (predicate.test(a[i])) {
                a[i] = newValue;
            }
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final char[][] a, final Throwables.CharPredicate<E> predicate, final char newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final char[] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final char[][][] a, final Throwables.CharPredicate<E> predicate, final char newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final char[][] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     *
     * @param a
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static char[][] reshape(final char[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new char[0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, cols, RoundingMode.CEILING);
        final char[][] c = new char[n][];

        for (int i = 0, from = 0; i < n; i++, from += cols) {
            c[i] = N.copyOfRange(a, from, from + N.min(len - from, cols));
        }

        return c;
    }

    /**
     *
     *
     * @param a
     * @param rows
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static char[][][] reshape(final char[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new char[0][0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, rows * cols, RoundingMode.CEILING);
        final char[][][] c = new char[n][][];

        for (int i = 0, from = 0; i < n; i++) {
            c[i] = new char[N.min(rows, Numbers.divide(len - from, cols, RoundingMode.CEILING))][];

            for (int j = 0, y = c[i].length; j < y; j++, from += cols) {
                c[i][j] = N.copyOfRange(a, from, from + N.min(len - from, cols));
            }
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static char[] flatten(final char[][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_CHAR_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final char[] c = new char[count];
        int from = 0;

        for (final char[] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            N.copy(element, 0, c, from, element.length);

            from += element.length;
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static char[] flatten(final char[][][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_CHAR_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final char[] c = new char[count];
        int from = 0;

        for (final char[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final char[] chars : element) {
                if (N.isEmpty(chars)) {
                    continue;
                }

                N.copy(chars, 0, c, from, chars.length);

                from += chars.length;
            }
        }

        return c;
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final char[][] a, final Throwables.Consumer<? super char[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final char[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final char[] e : a) {
            if (N.notEmpty(e)) {
                N.copy(tmp, idx, e, 0, e.length);
                idx += e.length;
            }
        }
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final char[][][] a, final Throwables.Consumer<? super char[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final char[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final char[][] e : a) {
            if (N.notEmpty(e)) {
                for (final char[] ee : e) {
                    if (N.notEmpty(ee)) {
                        N.copy(tmp, idx, ee, 0, ee.length);
                        idx += ee.length;
                    }
                }
            }
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[] zip(final char[] a, final char[] b, final Throwables.CharBiFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final char[] result = new char[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[] zip(final char[] a, final char[] b, final char valueForNoneA, final char valueForNoneB,
            final Throwables.CharBiFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return zip(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> char[] zip(final int len, final char[] a, final char[] b, final char valueForNoneA, final char valueForNoneB,
            final Throwables.CharBiFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final char[] result = new char[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zipFunction.apply(valueForNoneA, b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zipFunction.apply(a[i], valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zipFunction.apply(valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[] zip(final char[] a, final char[] b, final char[] c, final Throwables.CharTriFunction<Character, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final char[] result = new char[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[] zip(final char[] a, final char[] b, final char[] c, final char valueForNoneA, final char valueForNoneB,
            final char valueForNoneC, final Throwables.CharTriFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return zip(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> char[] zip(final int len, final char[] a, final char[] b, final char[] c, final char valueForNoneA,
            final char valueForNoneB, final char valueForNoneC, final Throwables.CharTriFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final char[] result = new char[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zipFunction.apply(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[][] zip(final char[][] a, final char[][] b, final Throwables.CharBiFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final char[][] result = new char[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[][] zip(final char[][] a, final char[][] b, final char valueForNoneA, final char valueForNoneB,
            final Throwables.CharBiFunction<Character, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> char[][] zip(final int len, final int rowLen, final char[][] a, final char[][] b, final char valueForNoneA,
            final char valueForNoneB, final Throwables.CharBiFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final char[][] result = new char[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zip(rowLen, null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zip(rowLen, a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zip(rowLen, null, null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[][] zip(final char[][] a, final char[][] b, final char[][] c,
            final Throwables.CharTriFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final char[][] result = new char[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[][] zip(final char[][] a, final char[][] b, final char[][] c, final char valueForNoneA, final char valueForNoneB,
            final char valueForNoneC, final Throwables.CharTriFunction<Character, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> char[][] zip(final int len, final int rowLen, final char[][] a, final char[][] b, final char[][] c,
            final char valueForNoneA, final char valueForNoneB, final char valueForNoneC, final Throwables.CharTriFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final char[][] result = new char[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zip(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[][][] zip(final char[][][] a, final char[][][] b, final Throwables.CharBiFunction<Character, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final char[][][] result = new char[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[][][] zip(final char[][][] a, final char[][][] b, final char valueForNoneA, final char valueForNoneB,
            final Throwables.CharBiFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final char[][][] result = new char[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[][][] zip(final char[][][] a, final char[][][] b, final char[][][] c,
            final Throwables.CharTriFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final char[][][] result = new char[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> char[][][] zip(final char[][][] a, final char[][][] b, final char[][][] c, final char valueForNoneA,
            final char valueForNoneB, final char valueForNoneC, final Throwables.CharTriFunction<Character, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final char[][][] result = new char[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    public static long totalCountOfElements(final char[][] a) {
        long count = 0;

        for (final char[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    public static long totalCountOfElements(final char[][][] a) {
        long count = 0;

        for (final char[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final char[] chars : element) {
                if (N.isEmpty(chars)) {
                    continue;
                }

                count += chars.length;
            }
        }

        return count;
    }

    /**
     * Min sub array len.
     *
     * @param a
     * @return
     */
    public static int minSubArrayLen(final char[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;

        for (final char[] ae : a) {
            minLen = N.min(minLen, ae == null ? 0 : ae.length);
        }

        return minLen;
    }

    /**
     * Max sub array len.
     *
     * @param a
     * @return
     */
    public static int maxSubArrayLen(final char[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int maxLen = 0;

        for (final char[] ae : a) {
            maxLen = N.max(maxLen, ae == null ? 0 : ae.length);
        }

        return maxLen;
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final char[] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            return N.println(N.toString(a));
        }
    }

    /**
     *
     * @param a
     */
    public static String println(final char[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final char[] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(ai[j]);
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     */
    public static String println(final char[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final char[][] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR).append("  ");
                            }

                            if (ai[j] == null) {
                                sb.append("null");
                            } else if (ai[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final char[] aij = ai[j];
                                sb.append('[');

                                for (int k = 0, aijLen = aij.length; k < aijLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(aij[k]);
                                }

                                sb.append(']');
                            }
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final byte[] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] += param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final byte[][] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final byte[][][] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[][] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final byte[] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] -= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final byte[][] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final byte[][][] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[][] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final byte[] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] *= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final byte[][] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final byte[][][] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[][] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final byte[] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] /= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final byte[][] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final byte[][][] a, final byte param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[][] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final byte[] a, final Throwables.ByteUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] = operator.applyAsByte(a[i]);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final byte[][] a, final Throwables.ByteUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final byte[][][] a, final Throwables.ByteUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[][] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final byte[] a, final Throwables.BytePredicate<E> predicate, final byte newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            if (predicate.test(a[i])) {
                a[i] = newValue;
            }
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final byte[][] a, final Throwables.BytePredicate<E> predicate, final byte newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final byte[][][] a, final Throwables.BytePredicate<E> predicate, final byte newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final byte[][] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     * @param a
     * @return
     */
    public static byte[] flatten(final byte[][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_BYTE_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final byte[] c = new byte[count];
        int from = 0;

        for (final byte[] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            N.copy(element, 0, c, from, element.length);

            from += element.length;
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static byte[] flatten(final byte[][][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_BYTE_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final byte[] c = new byte[count];
        int from = 0;

        for (final byte[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final byte[] bytes : element) {
                if (N.isEmpty(bytes)) {
                    continue;
                }

                N.copy(bytes, 0, c, from, bytes.length);

                from += bytes.length;
            }
        }

        return c;
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final byte[][] a, final Throwables.Consumer<? super byte[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final byte[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final byte[] e : a) {
            if (N.notEmpty(e)) {
                N.copy(tmp, idx, e, 0, e.length);
                idx += e.length;
            }
        }
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final byte[][][] a, final Throwables.Consumer<? super byte[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final byte[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final byte[][] e : a) {
            if (N.notEmpty(e)) {
                for (final byte[] ee : e) {
                    if (N.notEmpty(ee)) {
                        N.copy(tmp, idx, ee, 0, ee.length);
                        idx += ee.length;
                    }
                }
            }
        }
    }

    /**
     *
     *
     * @param a
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static byte[][] reshape(final byte[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new byte[0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, cols, RoundingMode.CEILING);
        final byte[][] c = new byte[n][];

        for (int i = 0, from = 0; i < n; i++, from += cols) {
            c[i] = N.copyOfRange(a, from, from + N.min(len - from, cols));
        }

        return c;
    }

    /**
     *
     *
     * @param a
     * @param rows
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static byte[][][] reshape(final byte[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new byte[0][0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, rows * cols, RoundingMode.CEILING);
        final byte[][][] c = new byte[n][][];

        for (int i = 0, from = 0; i < n; i++) {
            c[i] = new byte[N.min(rows, Numbers.divide(len - from, cols, RoundingMode.CEILING))][];

            for (int j = 0, y = c[i].length; j < y; j++, from += cols) {
                c[i][j] = N.copyOfRange(a, from, from + N.min(len - from, cols));
            }
        }

        return c;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[] add(final byte[] a, final byte[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (byte) (a[i] + b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[] add(final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return add(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static byte[] add(final int len, final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = (byte) (a[i] + b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = (byte) (valueForNoneA + b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = (byte) (a[i] + valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = (byte) (valueForNoneA + valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[] add(final byte[] a, final byte[] b, final byte[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (byte) (a[i] + b[i] + c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[] add(final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB, final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return add(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static byte[] add(final int len, final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = (byte) (a[i] + b[i] + c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (byte) ((i < lenA ? a[i] : valueForNoneA) + (i < lenB ? b[i] : valueForNoneB) + (i < lenC ? c[i] : valueForNoneC));
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[][] add(final byte[][] a, final byte[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[][] add(final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB) {
        return add(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static byte[][] add(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = add(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = add(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = add(rowLen, (byte[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[][] add(final byte[][] a, final byte[][] b, final byte[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[][] add(final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        return add(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static byte[][] add(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA,
            final byte valueForNoneB, final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = add(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[][][] add(final byte[][][] a, final byte[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[][][] add(final byte[][][] a, final byte[][][] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = add(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = add(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = add(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[][][] add(final byte[][][] a, final byte[][][] b, final byte[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[][][] add(final byte[][][] a, final byte[][][] b, final byte[][][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = add(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = add(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[] subtract(final byte[] a, final byte[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (byte) (a[i] - b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[] subtract(final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return subtract(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static byte[] subtract(final int len, final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = (byte) (a[i] - b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = (byte) (valueForNoneA - b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = (byte) (a[i] - valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = (byte) (valueForNoneA - valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[] subtract(final byte[] a, final byte[] b, final byte[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (byte) (a[i] - b[i] - c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[] subtract(final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return subtract(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static byte[] subtract(final int len, final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = (byte) (a[i] - b[i] - c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (byte) ((i < lenA ? a[i] : valueForNoneA) - (i < lenB ? b[i] : valueForNoneB) - (i < lenC ? c[i] : valueForNoneC));
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[][] subtract(final byte[][] a, final byte[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[][] subtract(final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB) {
        return subtract(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static byte[][] subtract(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = subtract(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = subtract(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = subtract(rowLen, (byte[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[][] subtract(final byte[][] a, final byte[][] b, final byte[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[][] subtract(final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        return subtract(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static byte[][] subtract(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA,
            final byte valueForNoneB, final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = subtract(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[][][] subtract(final byte[][][] a, final byte[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[][][] subtract(final byte[][][] a, final byte[][][] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = subtract(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = subtract(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = subtract(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[][][] subtract(final byte[][][] a, final byte[][][] b, final byte[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[][][] subtract(final byte[][][] a, final byte[][][] b, final byte[][][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = subtract(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = subtract(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[] multipliedBy(final byte[] a, final byte[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (byte) (a[i] * b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[] multipliedBy(final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return multipliedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static byte[] multipliedBy(final int len, final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = (byte) (a[i] * b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = (byte) (valueForNoneA * b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = (byte) (a[i] * valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = (byte) (valueForNoneA * valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[] multipliedBy(final byte[] a, final byte[] b, final byte[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (byte) (a[i] * b[i] * c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[] multipliedBy(final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return multipliedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static byte[] multipliedBy(final int len, final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = (byte) (a[i] * b[i] * c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (byte) ((i < lenA ? a[i] : valueForNoneA) * (i < lenB ? b[i] : valueForNoneB) * (i < lenC ? c[i] : valueForNoneC));
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[][] multipliedBy(final byte[][] a, final byte[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[][] multipliedBy(final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB) {
        return multipliedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static byte[][] multipliedBy(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte valueForNoneA,
            final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = multipliedBy(rowLen, (byte[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[][] multipliedBy(final byte[][] a, final byte[][] b, final byte[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[][] multipliedBy(final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        return multipliedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static byte[][] multipliedBy(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA,
            final byte valueForNoneB, final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = multipliedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[][][] multipliedBy(final byte[][][] a, final byte[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[][][] multipliedBy(final byte[][][] a, final byte[][][] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = multipliedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = multipliedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[][][] multipliedBy(final byte[][][] a, final byte[][][] b, final byte[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[][][] multipliedBy(final byte[][][] a, final byte[][][] b, final byte[][][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = multipliedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[] dividedBy(final byte[] a, final byte[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (byte) (a[i] / b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[] dividedBy(final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static byte[] dividedBy(final int len, final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = (byte) (a[i] / b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = (byte) (valueForNoneA / b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = (byte) (a[i] / valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = (byte) (valueForNoneA / valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[] dividedBy(final byte[] a, final byte[] b, final byte[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (byte) (a[i] / b[i] / c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[] dividedBy(final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static byte[] dividedBy(final int len, final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = (byte) (a[i] / b[i] / c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (byte) ((i < lenA ? a[i] : valueForNoneA) / (i < lenB ? b[i] : valueForNoneB) / (i < lenC ? c[i] : valueForNoneC));
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[][] dividedBy(final byte[][] a, final byte[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[][] dividedBy(final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static byte[][] dividedBy(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, (byte[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[][] dividedBy(final byte[][] a, final byte[][] b, final byte[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[][] dividedBy(final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static byte[][] dividedBy(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA,
            final byte valueForNoneB, final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[][][] dividedBy(final byte[][][] a, final byte[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static byte[][][] dividedBy(final byte[][][] a, final byte[][][] b, final byte valueForNoneA, final byte valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static byte[][][] dividedBy(final byte[][][] a, final byte[][][] b, final byte[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static byte[][][] dividedBy(final byte[][][] a, final byte[][][] b, final byte[][][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static byte[] dividedBy(final byte[] a, final byte[] b, final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (byte) (a[i] / (b[i] == 0 ? defaultValueForZero : b[i]));
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static byte[] dividedBy(final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB, final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static byte[] dividedBy(final int len, final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB,
            final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = (byte) (a[i] / (b[i] == 0 ? defaultValueForZero : b[i]));
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = (byte) (valueForNoneA / (b[i] == 0 ? defaultValueForZero : b[i]));
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = (byte) (a[i] / valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = (byte) (valueForNoneA / valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static byte[] dividedBy(final byte[] a, final byte[] b, final byte[] c, final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (byte) (a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]));
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static byte[] dividedBy(final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB, final byte valueForNoneC,
            final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static byte[] dividedBy(final int len, final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC, final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = (byte) (a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]));
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (byte) ((i < lenA ? a[i] : valueForNoneA) / (i < lenB ? (b[i] == 0 ? defaultValueForZero : b[i]) : valueForNoneB)
                        / (i < lenC ? (c[i] == 0 ? defaultValueForZero : c[i]) : valueForNoneC));
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static byte[][] dividedBy(final byte[][] a, final byte[][] b, final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static byte[][] dividedBy(final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB, final byte defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static byte[][] dividedBy(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB,
            final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, (byte[]) null, null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static byte[][] dividedBy(final byte[][] a, final byte[][] b, final byte[][] c, final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static byte[][] dividedBy(final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC, final byte defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static byte[][] dividedBy(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA,
            final byte valueForNoneB, final byte valueForNoneC, final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static byte[][][] dividedBy(final byte[][][] a, final byte[][][] b, final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static byte[][][] dividedBy(final byte[][][] a, final byte[][][] b, final byte valueForNoneA, final byte valueForNoneB,
            final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static byte[][][] dividedBy(final byte[][][] a, final byte[][][] b, final byte[][][] c, final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static byte[][][] dividedBy(final byte[][][] a, final byte[][][] b, final byte[][][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC, final byte defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                    defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[] zip(final byte[] a, final byte[] b, final Throwables.ByteBiFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[] zip(final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB,
            final Throwables.ByteBiFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return zip(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> byte[] zip(final int len, final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB,
            final Throwables.ByteBiFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zipFunction.apply(valueForNoneA, b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zipFunction.apply(a[i], valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zipFunction.apply(valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[] zip(final byte[] a, final byte[] b, final byte[] c, final Throwables.ByteTriFunction<Byte, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[] zip(final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC, final Throwables.ByteTriFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return zip(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> byte[] zip(final int len, final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA,
            final byte valueForNoneB, final byte valueForNoneC, final Throwables.ByteTriFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[] result = new byte[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zipFunction.apply(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[][] zip(final byte[][] a, final byte[][] b, final Throwables.ByteBiFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[][] zip(final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB,
            final Throwables.ByteBiFunction<Byte, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> byte[][] zip(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte valueForNoneA,
            final byte valueForNoneB, final Throwables.ByteBiFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zip(rowLen, null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zip(rowLen, a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zip(rowLen, null, null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[][] zip(final byte[][] a, final byte[][] b, final byte[][] c,
            final Throwables.ByteTriFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[][] zip(final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC, final Throwables.ByteTriFunction<Byte, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> byte[][] zip(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte[][] c,
            final byte valueForNoneA, final byte valueForNoneB, final byte valueForNoneC, final Throwables.ByteTriFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][] result = new byte[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zip(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[][][] zip(final byte[][][] a, final byte[][][] b, final Throwables.ByteBiFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[][][] zip(final byte[][][] a, final byte[][][] b, final byte valueForNoneA, final byte valueForNoneB,
            final Throwables.ByteBiFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final byte[][][] result = new byte[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[][][] zip(final byte[][][] a, final byte[][][] b, final byte[][][] c,
            final Throwables.ByteTriFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> byte[][][] zip(final byte[][][] a, final byte[][][] b, final byte[][][] c, final byte valueForNoneA,
            final byte valueForNoneB, final byte valueForNoneC, final Throwables.ByteTriFunction<Byte, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final byte[][][] result = new byte[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[] zip(final byte[] a, final byte[] b, final Throwables.ByteBiFunction<? extends R, E> zipFunction,
    //            final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //
    //        final R[] result = Array.newInstance(targetElementType, N.min(lenA, lenB));
    //
    //        for (int i = 0, len = result.length; i < len; i++) {
    //            result[i] = zipFunction.apply(a[i], b[i]);
    //        }
    //
    //        return result;
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param valueForNoneA
    //     * @param valueForNoneB
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[] zip(final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB,
    //            final Throwables.ByteBiFunction<? extends R, E> zipFunction, final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //
    //        return zip(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, zipFunction, targetElementType);
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param len
    //     * @param a
    //     * @param b
    //     * @param valueForNoneA
    //     * @param valueForNoneB
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    private static <R, E extends Exception> R[] zip(final int len, final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB,
    //            final Throwables.ByteBiFunction<? extends R, E> zipFunction, final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //
    //        final R[] result = Array.newInstance(targetElementType, len);
    //
    //        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
    //            result[i] = zipFunction.apply(a[i], b[i]);
    //        }
    //
    //        if (lenA < lenB && lenA < len) {
    //            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
    //                result[i] = zipFunction.apply(valueForNoneA, b[i]);
    //            }
    //        } else if (lenB < lenA && lenB < len) {
    //            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
    //                result[i] = zipFunction.apply(a[i], valueForNoneB);
    //            }
    //        }
    //
    //        if (N.max(lenA, lenB) < len) {
    //            for (int i = N.max(lenA, lenB); i < len; i++) {
    //                result[i] = zipFunction.apply(valueForNoneA, valueForNoneB);
    //            }
    //        }
    //
    //        return result;
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param c
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[] zip(final byte[] a, final byte[] b, final byte[] c, final Throwables.ByteTriFunction<? extends R, E> zipFunction,
    //            final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //        final int lenC = N.len(c);
    //
    //        final R[] result = Array.newInstance(targetElementType, N.min(lenA, lenB, lenC));
    //
    //        for (int i = 0, len = result.length; i < len; i++) {
    //            result[i] = zipFunction.apply(a[i], b[i], c[i]);
    //        }
    //
    //        return result;
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param c
    //     * @param valueForNoneA
    //     * @param valueForNoneB
    //     * @param valueForNoneC
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[] zip(final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB,
    //            final byte valueForNoneC, final Throwables.ByteTriFunction<? extends R, E> zipFunction, final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //        final int lenC = N.len(c);
    //
    //        return zip(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction, targetElementType);
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param len
    //     * @param a
    //     * @param b
    //     * @param c
    //     * @param valueForNoneA
    //     * @param valueForNoneB
    //     * @param valueForNoneC
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    private static <R, E extends Exception> R[] zip(final int len, final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA,
    //            final byte valueForNoneB, final byte valueForNoneC, final Throwables.ByteTriFunction<? extends R, E> zipFunction,
    //            final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //        final int lenC = N.len(c);
    //
    //        final R[] result = Array.newInstance(targetElementType, len);
    //
    //        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
    //            result[i] = zipFunction.apply(a[i], b[i], c[i]);
    //        }
    //
    //        if (N.min(lenA, lenB, lenC) < len) {
    //            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
    //                result[i] = zipFunction.apply(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
    //            }
    //        }
    //
    //        return result;
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[][] zip(final byte[][] a, final byte[][] b, final Throwables.ByteBiFunction<? extends R, E> zipFunction,
    //            final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //
    //        final R[][] result = Array.newInstance(targetElementType, N.min(lenA, lenB), 0);
    //
    //        for (int i = 0, len = result.length; i < len; i++) {
    //            result[i] = zip(a[i], b[i], zipFunction, targetElementType);
    //        }
    //
    //        return result;
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param valueForNoneA
    //     * @param valueForNoneB
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[][] zip(final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB,
    //            final Throwables.ByteBiFunction<? extends R, E> zipFunction, final Class<? extends R> targetElementType) throws E {
    //        return zip(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, zipFunction, targetElementType);
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param len
    //     * @param rowLen
    //     * @param a
    //     * @param b
    //     * @param valueForNoneA
    //     * @param valueForNoneB
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    private static <R, E extends Exception> R[][] zip(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte valueForNoneA,
    //            final byte valueForNoneB, final Throwables.ByteBiFunction<? extends R, E> zipFunction, final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //
    //        final R[][] result = Array.newInstance(targetElementType, len, 0);
    //
    //        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
    //            result[i] = zip(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
    //        }
    //
    //        if (lenA < lenB && lenA < len) {
    //            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
    //                result[i] = zip(rowLen, null, b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
    //            }
    //        } else if (lenB < lenA && lenB < len) {
    //            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
    //                result[i] = zip(rowLen, a[i], null, valueForNoneA, valueForNoneB, zipFunction, targetElementType);
    //            }
    //        }
    //
    //        if (N.max(lenA, lenB) < len) {
    //            for (int i = N.max(lenA, lenB); i < len; i++) {
    //                result[i] = zip(rowLen, null, null, valueForNoneA, valueForNoneB, zipFunction, targetElementType);
    //            }
    //        }
    //
    //        return result;
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param c
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[][] zip(final byte[][] a, final byte[][] b, final byte[][] c,
    //            final Throwables.ByteTriFunction<? extends R, E> zipFunction, final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //        final int lenC = N.len(c);
    //
    //        final R[][] result = Array.newInstance(targetElementType, N.min(lenA, lenB, lenC), 0);
    //
    //        for (int i = 0, len = result.length; i < len; i++) {
    //            result[i] = zip(a[i], b[i], c[i], zipFunction, targetElementType);
    //        }
    //
    //        return result;
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param c
    //     * @param valueForNoneA
    //     * @param valueForNoneB
    //     * @param valueForNoneC
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[][] zip(final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA, final byte valueForNoneB,
    //            final byte valueForNoneC, final Throwables.ByteTriFunction<? extends R, E> zipFunction, final Class<? extends R> targetElementType) throws E {
    //        return zip(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
    //                valueForNoneC, zipFunction, targetElementType);
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param len
    //     * @param rowLen
    //     * @param a
    //     * @param b
    //     * @param c
    //     * @param valueForNoneA
    //     * @param valueForNoneB
    //     * @param valueForNoneC
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    private static <R, E extends Exception> R[][] zip(final int len, final int rowLen, final byte[][] a, final byte[][] b, final byte[][] c,
    //            final byte valueForNoneA, final byte valueForNoneB, final byte valueForNoneC, final Throwables.ByteTriFunction<? extends R, E> zipFunction,
    //            final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //        final int lenC = N.len(c);
    //
    //        final R[][] result = Array.newInstance(targetElementType, len, 0);
    //
    //        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
    //            result[i] = zip(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction, targetElementType);
    //        }
    //
    //        if (N.min(lenA, lenB, lenC) < len) {
    //            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
    //                result[i] = zip(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
    //                        zipFunction, targetElementType);
    //            }
    //        }
    //
    //        return result;
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[][][] zip(final byte[][][] a, final byte[][][] b, final Throwables.ByteBiFunction<? extends R, E> zipFunction,
    //            final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //
    //        final R[][][] result = Array.newInstance(targetElementType, N.min(lenA, lenB), 0, 0);
    //
    //        for (int i = 0, len = result.length; i < len; i++) {
    //            result[i] = zip(a[i], b[i], zipFunction, targetElementType);
    //        }
    //
    //        return result;
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param valueForNoneA
    //     * @param valueForNoneB
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[][][] zip(final byte[][][] a, final byte[][][] b, final byte valueForNoneA, final byte valueForNoneB,
    //            final Throwables.ByteBiFunction<? extends R, E> zipFunction, final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //
    //        final R[][][] result = Array.newInstance(targetElementType, N.max(lenA, lenB), 0, 0);
    //
    //        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
    //            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
    //        }
    //
    //        if (lenA < lenB) {
    //            for (int i = lenA; i < lenB; i++) {
    //                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
    //            }
    //        } else if (lenB < lenA) {
    //            for (int i = lenB; i < lenA; i++) {
    //                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction, targetElementType);
    //            }
    //        }
    //
    //        return result;
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param c
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[][][] zip(final byte[][][] a, final byte[][][] b, final byte[][][] c,
    //            final Throwables.ByteTriFunction<? extends R, E> zipFunction, final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //        final int lenC = N.len(c);
    //
    //        final R[][][] result = Array.newInstance(targetElementType, N.min(lenA, lenB, lenC), 0, 0);
    //
    //        for (int i = 0, len = result.length; i < len; i++) {
    //            result[i] = zip(a[i], b[i], c[i], zipFunction, targetElementType);
    //        }
    //
    //        return result;
    //    }
    //
    //    /**
    //     *
    //     * @param <R>
    //     * @param <E>
    //     * @param a
    //     * @param b
    //     * @param c
    //     * @param valueForNoneA
    //     * @param valueForNoneB
    //     * @param valueForNoneC
    //     * @param zipFunction
    //     * @param targetElementType
    //     * @return
    //     * @throws E the e
    //     */
    //    public static <R, E extends Exception> R[][][] zip(final byte[][][] a, final byte[][][] b, final byte[][][] c, final byte valueForNoneA,
    //            final byte valueForNoneB, final byte valueForNoneC, final Throwables.ByteTriFunction<? extends R, E> zipFunction,
    //            final Class<? extends R> targetElementType) throws E {
    //        final int lenA = N.len(a);
    //        final int lenB = N.len(b);
    //        final int lenC = N.len(c);
    //
    //        final R[][][] result = Array.newInstance(targetElementType, N.max(lenA, lenB, lenC), 0, 0);
    //
    //        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
    //            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction, targetElementType);
    //        }
    //
    //        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
    //            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction,
    //                    targetElementType);
    //        }
    //
    //        return result;
    //    }

    public static long totalCountOfElements(final byte[][] a) {
        long count = 0;

        for (final byte[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    public static long totalCountOfElements(final byte[][][] a) {
        long count = 0;

        for (final byte[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final byte[] bytes : element) {
                if (N.isEmpty(bytes)) {
                    continue;
                }

                count += bytes.length;
            }
        }

        return count;
    }

    /**
     * Min sub array len.
     *
     * @param a
     * @return
     */
    public static int minSubArrayLen(final byte[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;

        for (final byte[] ae : a) {
            minLen = N.min(minLen, ae == null ? 0 : ae.length);
        }

        return minLen;
    }

    /**
     * Max sub array len.
     *
     * @param a
     * @return
     */
    public static int maxSubArrayLen(final byte[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int maxLen = 0;

        for (final byte[] ae : a) {
            maxLen = N.max(maxLen, ae == null ? 0 : ae.length);
        }

        return maxLen;
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final byte[] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            return N.println(N.toString(a));
        }
    }

    /**
     *
     * @param a
     */
    public static String println(final byte[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final byte[] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(ai[j]);
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     */
    public static String println(final byte[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final byte[][] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR).append("  ");
                            }

                            if (ai[j] == null) {
                                sb.append("null");
                            } else if (ai[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final byte[] aij = ai[j];
                                sb.append('[');

                                for (int k = 0, aijLen = aij.length; k < aijLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(aij[k]);
                                }

                                sb.append(']');
                            }
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final short[] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] += param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final short[][] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final short[][][] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[][] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final short[] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] -= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final short[][] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final short[][][] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[][] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final short[] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] *= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final short[][] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final short[][][] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[][] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final short[] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] /= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final short[][] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final short[][][] a, final short param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[][] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final short[] a, final Throwables.ShortUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] = operator.applyAsShort(a[i]);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final short[][] a, final Throwables.ShortUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final short[][][] a, final Throwables.ShortUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[][] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final short[] a, final Throwables.ShortPredicate<E> predicate, final short newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            if (predicate.test(a[i])) {
                a[i] = newValue;
            }
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final short[][] a, final Throwables.ShortPredicate<E> predicate, final short newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final short[][][] a, final Throwables.ShortPredicate<E> predicate, final short newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final short[][] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     *
     * @param a
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static short[][] reshape(final short[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new short[0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, cols, RoundingMode.CEILING);
        final short[][] c = new short[n][];

        for (int i = 0, from = 0; i < n; i++, from += cols) {
            c[i] = N.copyOfRange(a, from, from + N.min(len - from, cols));
        }

        return c;
    }

    /**
     *
     *
     * @param a
     * @param rows
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static short[][][] reshape(final short[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new short[0][0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, rows * cols, RoundingMode.CEILING);
        final short[][][] c = new short[n][][];

        for (int i = 0, from = 0; i < n; i++) {
            c[i] = new short[N.min(rows, Numbers.divide(len - from, cols, RoundingMode.CEILING))][];

            for (int j = 0, y = c[i].length; j < y; j++, from += cols) {
                c[i][j] = N.copyOfRange(a, from, from + N.min(len - from, cols));
            }
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static short[] flatten(final short[][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_SHORT_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final short[] c = new short[count];
        int from = 0;

        for (final short[] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            N.copy(element, 0, c, from, element.length);

            from += element.length;
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static short[] flatten(final short[][][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_SHORT_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final short[] c = new short[count];
        int from = 0;

        for (final short[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final short[] shorts : element) {
                if (N.isEmpty(shorts)) {
                    continue;
                }

                N.copy(shorts, 0, c, from, shorts.length);

                from += shorts.length;
            }
        }

        return c;
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final short[][] a, final Throwables.Consumer<? super short[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final short[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final short[] e : a) {
            if (N.notEmpty(e)) {
                N.copy(tmp, idx, e, 0, e.length);
                idx += e.length;
            }
        }
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final short[][][] a, final Throwables.Consumer<? super short[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final short[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final short[][] e : a) {
            if (N.notEmpty(e)) {
                for (final short[] ee : e) {
                    if (N.notEmpty(ee)) {
                        N.copy(tmp, idx, ee, 0, ee.length);
                        idx += ee.length;
                    }
                }
            }
        }
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[] add(final short[] a, final short[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (short) (a[i] + b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[] add(final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return add(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static short[] add(final int len, final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = (short) (a[i] + b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = (short) (valueForNoneA + b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = (short) (a[i] + valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = (short) (valueForNoneA + valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[] add(final short[] a, final short[] b, final short[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (short) (a[i] + b[i] + c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[] add(final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return add(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static short[] add(final int len, final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = (short) (a[i] + b[i] + c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (short) ((i < lenA ? a[i] : valueForNoneA) + (i < lenB ? b[i] : valueForNoneB) + (i < lenC ? c[i] : valueForNoneC));
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[][] add(final short[][] a, final short[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[][] add(final short[][] a, final short[][] b, final short valueForNoneA, final short valueForNoneB) {
        return add(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static short[][] add(final int len, final int rowLen, final short[][] a, final short[][] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = add(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = add(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = add(rowLen, (short[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[][] add(final short[][] a, final short[][] b, final short[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[][] add(final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        return add(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static short[][] add(final int len, final int rowLen, final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA,
            final short valueForNoneB, final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = add(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[][][] add(final short[][][] a, final short[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[][][] add(final short[][][] a, final short[][][] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = add(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = add(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = add(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[][][] add(final short[][][] a, final short[][][] b, final short[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[][][] add(final short[][][] a, final short[][][] b, final short[][][] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = add(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = add(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[] subtract(final short[] a, final short[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (short) (a[i] - b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[] subtract(final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return subtract(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static short[] subtract(final int len, final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = (short) (a[i] - b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = (short) (valueForNoneA - b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = (short) (a[i] - valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = (short) (valueForNoneA - valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[] subtract(final short[] a, final short[] b, final short[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (short) (a[i] - b[i] - c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[] subtract(final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return subtract(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static short[] subtract(final int len, final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = (short) (a[i] - b[i] - c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (short) ((i < lenA ? a[i] : valueForNoneA) - (i < lenB ? b[i] : valueForNoneB) - (i < lenC ? c[i] : valueForNoneC));
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[][] subtract(final short[][] a, final short[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[][] subtract(final short[][] a, final short[][] b, final short valueForNoneA, final short valueForNoneB) {
        return subtract(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static short[][] subtract(final int len, final int rowLen, final short[][] a, final short[][] b, final short valueForNoneA,
            final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = subtract(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = subtract(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = subtract(rowLen, (short[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[][] subtract(final short[][] a, final short[][] b, final short[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[][] subtract(final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        return subtract(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static short[][] subtract(final int len, final int rowLen, final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA,
            final short valueForNoneB, final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = subtract(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[][][] subtract(final short[][][] a, final short[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[][][] subtract(final short[][][] a, final short[][][] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = subtract(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = subtract(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = subtract(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[][][] subtract(final short[][][] a, final short[][][] b, final short[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[][][] subtract(final short[][][] a, final short[][][] b, final short[][][] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = subtract(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = subtract(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[] multipliedBy(final short[] a, final short[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (short) (a[i] * b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[] multipliedBy(final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return multipliedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static short[] multipliedBy(final int len, final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = (short) (a[i] * b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = (short) (valueForNoneA * b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = (short) (a[i] * valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = (short) (valueForNoneA * valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[] multipliedBy(final short[] a, final short[] b, final short[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (short) (a[i] * b[i] * c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[] multipliedBy(final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return multipliedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static short[] multipliedBy(final int len, final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = (short) (a[i] * b[i] * c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (short) ((i < lenA ? a[i] : valueForNoneA) * (i < lenB ? b[i] : valueForNoneB) * (i < lenC ? c[i] : valueForNoneC));
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[][] multipliedBy(final short[][] a, final short[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[][] multipliedBy(final short[][] a, final short[][] b, final short valueForNoneA, final short valueForNoneB) {
        return multipliedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static short[][] multipliedBy(final int len, final int rowLen, final short[][] a, final short[][] b, final short valueForNoneA,
            final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = multipliedBy(rowLen, (short[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[][] multipliedBy(final short[][] a, final short[][] b, final short[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[][] multipliedBy(final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        return multipliedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static short[][] multipliedBy(final int len, final int rowLen, final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA,
            final short valueForNoneB, final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = multipliedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[][][] multipliedBy(final short[][][] a, final short[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[][][] multipliedBy(final short[][][] a, final short[][][] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = multipliedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = multipliedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[][][] multipliedBy(final short[][][] a, final short[][][] b, final short[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[][][] multipliedBy(final short[][][] a, final short[][][] b, final short[][][] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = multipliedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[] dividedBy(final short[] a, final short[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (short) (a[i] / b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[] dividedBy(final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static short[] dividedBy(final int len, final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = (short) (a[i] / b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = (short) (valueForNoneA / b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = (short) (a[i] / valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = (short) (valueForNoneA / valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[] dividedBy(final short[] a, final short[] b, final short[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (short) (a[i] / b[i] / c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[] dividedBy(final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static short[] dividedBy(final int len, final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = (short) (a[i] / b[i] / c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (short) ((i < lenA ? a[i] : valueForNoneA) / (i < lenB ? b[i] : valueForNoneB) / (i < lenC ? c[i] : valueForNoneC));
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[][] dividedBy(final short[][] a, final short[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[][] dividedBy(final short[][] a, final short[][] b, final short valueForNoneA, final short valueForNoneB) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static short[][] dividedBy(final int len, final int rowLen, final short[][] a, final short[][] b, final short valueForNoneA,
            final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, (short[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[][] dividedBy(final short[][] a, final short[][] b, final short[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[][] dividedBy(final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static short[][] dividedBy(final int len, final int rowLen, final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA,
            final short valueForNoneB, final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static short[][][] dividedBy(final short[][][] a, final short[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static short[][][] dividedBy(final short[][][] a, final short[][][] b, final short valueForNoneA, final short valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static short[][][] dividedBy(final short[][][] a, final short[][][] b, final short[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static short[][][] dividedBy(final short[][][] a, final short[][][] b, final short[][][] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static short[] dividedBy(final short[] a, final short[] b, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (short) (a[i] / (b[i] == 0 ? defaultValueForZero : b[i]));
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static short[] dividedBy(final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static short[] dividedBy(final int len, final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB,
            final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = (short) (a[i] / (b[i] == 0 ? defaultValueForZero : b[i]));
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = (short) (valueForNoneA / (b[i] == 0 ? defaultValueForZero : b[i]));
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = (short) (a[i] / valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = (short) (valueForNoneA / valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static short[] dividedBy(final short[] a, final short[] b, final short[] c, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = (short) (a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]));
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static short[] dividedBy(final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static short[] dividedBy(final int len, final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = (short) (a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]));
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (short) ((i < lenA ? a[i] : valueForNoneA) / (i < lenB ? (b[i] == 0 ? defaultValueForZero : b[i]) : valueForNoneB)
                        / (i < lenC ? (c[i] == 0 ? defaultValueForZero : c[i]) : valueForNoneC));
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static short[][] dividedBy(final short[][] a, final short[][] b, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static short[][] dividedBy(final short[][] a, final short[][] b, final short valueForNoneA, final short valueForNoneB,
            final short defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static short[][] dividedBy(final int len, final int rowLen, final short[][] a, final short[][] b, final short valueForNoneA,
            final short valueForNoneB, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, (short[]) null, null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static short[][] dividedBy(final short[][] a, final short[][] b, final short[][] c, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static short[][] dividedBy(final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC, final short defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static short[][] dividedBy(final int len, final int rowLen, final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA,
            final short valueForNoneB, final short valueForNoneC, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static short[][][] dividedBy(final short[][][] a, final short[][][] b, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static short[][][] dividedBy(final short[][][] a, final short[][][] b, final short valueForNoneA, final short valueForNoneB,
            final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static short[][][] dividedBy(final short[][][] a, final short[][][] b, final short[][][] c, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static short[][][] dividedBy(final short[][][] a, final short[][][] b, final short[][][] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC, final short defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                    defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[] zip(final short[] a, final short[] b, final Throwables.ShortBiFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[] zip(final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB,
            final Throwables.ShortBiFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return zip(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> short[] zip(final int len, final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB,
            final Throwables.ShortBiFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zipFunction.apply(valueForNoneA, b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zipFunction.apply(a[i], valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zipFunction.apply(valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[] zip(final short[] a, final short[] b, final short[] c, final Throwables.ShortTriFunction<Short, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[] zip(final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC, final Throwables.ShortTriFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return zip(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> short[] zip(final int len, final short[] a, final short[] b, final short[] c, final short valueForNoneA,
            final short valueForNoneB, final short valueForNoneC, final Throwables.ShortTriFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[] result = new short[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zipFunction.apply(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[][] zip(final short[][] a, final short[][] b, final Throwables.ShortBiFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[][] zip(final short[][] a, final short[][] b, final short valueForNoneA, final short valueForNoneB,
            final Throwables.ShortBiFunction<Short, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> short[][] zip(final int len, final int rowLen, final short[][] a, final short[][] b, final short valueForNoneA,
            final short valueForNoneB, final Throwables.ShortBiFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zip(rowLen, null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zip(rowLen, a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zip(rowLen, null, null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[][] zip(final short[][] a, final short[][] b, final short[][] c,
            final Throwables.ShortTriFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[][] zip(final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA,
            final short valueForNoneB, final short valueForNoneC, final Throwables.ShortTriFunction<Short, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> short[][] zip(final int len, final int rowLen, final short[][] a, final short[][] b, final short[][] c,
            final short valueForNoneA, final short valueForNoneB, final short valueForNoneC, final Throwables.ShortTriFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][] result = new short[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zip(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[][][] zip(final short[][][] a, final short[][][] b, final Throwables.ShortBiFunction<Short, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[][][] zip(final short[][][] a, final short[][][] b, final short valueForNoneA, final short valueForNoneB,
            final Throwables.ShortBiFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final short[][][] result = new short[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[][][] zip(final short[][][] a, final short[][][] b, final short[][][] c,
            final Throwables.ShortTriFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> short[][][] zip(final short[][][] a, final short[][][] b, final short[][][] c, final short valueForNoneA,
            final short valueForNoneB, final short valueForNoneC, final Throwables.ShortTriFunction<Short, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final short[][][] result = new short[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    public static long totalCountOfElements(final short[][] a) {
        long count = 0;

        for (final short[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    public static long totalCountOfElements(final short[][][] a) {
        long count = 0;

        for (final short[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final short[] shorts : element) {
                if (N.isEmpty(shorts)) {
                    continue;
                }

                count += shorts.length;
            }
        }

        return count;
    }

    /**
     * Min sub array len.
     *
     * @param a
     * @return
     */
    public static int minSubArrayLen(final short[][] a) {
        if (a == null) {
            return 0;
        }

        int minLen = 0;

        for (final short[] shorts : a) {
            minLen = N.min(minLen, shorts == null ? 0 : shorts.length);
        }

        return minLen;
    }

    /**
     * Max sub array len.
     *
     * @param a
     * @return
     */
    public static int maxSubArrayLen(final short[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int maxLen = 0;

        for (final short[] ae : a) {
            maxLen = N.max(maxLen, ae == null ? 0 : ae.length);
        }

        return maxLen;
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final short[] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            return N.println(N.toString(a));
        }
    }

    /**
     *
     * @param a
     */
    public static String println(final short[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final short[] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(ai[j]);
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final short[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final short[][] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR).append("  ");
                            }

                            if (ai[j] == null) {
                                sb.append("null");
                            } else if (ai[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final short[] aij = ai[j];
                                sb.append('[');

                                for (int k = 0, aijLen = aij.length; k < aijLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(aij[k]);
                                }

                                sb.append(']');
                            }
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final int[] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] += param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final int[][] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final int[][][] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[][] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final int[] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] -= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final int[][] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final int[][][] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[][] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final int[] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] *= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final int[][] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final int[][][] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[][] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final int[] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] /= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final int[][] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final int[][][] a, final int param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[][] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final int[] a, final Throwables.IntUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] = operator.applyAsInt(a[i]);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final int[][] a, final Throwables.IntUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final int[][][] a, final Throwables.IntUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[][] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final int[] a, final Throwables.IntPredicate<E> predicate, final int newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            if (predicate.test(a[i])) {
                a[i] = newValue;
            }
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final int[][] a, final Throwables.IntPredicate<E> predicate, final int newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final int[][][] a, final Throwables.IntPredicate<E> predicate, final int newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final int[][] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     *
     * @param a
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static int[][] reshape(final int[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new int[0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, cols, RoundingMode.CEILING);
        final int[][] c = new int[n][];

        for (int i = 0, from = 0; i < n; i++, from += cols) {
            c[i] = N.copyOfRange(a, from, from + N.min(len - from, cols));
        }

        return c;
    }

    /**
     *
     *
     * @param a
     * @param rows
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static int[][][] reshape(final int[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new int[0][0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, rows * cols, RoundingMode.CEILING);
        final int[][][] c = new int[n][][];

        for (int i = 0, from = 0; i < n; i++) {
            c[i] = new int[N.min(rows, Numbers.divide(len - from, cols, RoundingMode.CEILING))][];

            for (int j = 0, y = c[i].length; j < y; j++, from += cols) {
                c[i][j] = N.copyOfRange(a, from, from + N.min(len - from, cols));
            }
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[] flatten(final int[][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_INT_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final int[] c = new int[count];
        int from = 0;

        for (final int[] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            N.copy(element, 0, c, from, element.length);

            from += element.length;
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[] flatten(final int[][][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_INT_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final int[] c = new int[count];
        int from = 0;

        for (final int[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final int[] ints : element) {
                if (N.isEmpty(ints)) {
                    continue;
                }

                N.copy(ints, 0, c, from, ints.length);

                from += ints.length;
            }
        }

        return c;
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final int[][] a, final Throwables.Consumer<? super int[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final int[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final int[] e : a) {
            if (N.notEmpty(e)) {
                N.copy(tmp, idx, e, 0, e.length);
                idx += e.length;
            }
        }
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final int[][][] a, final Throwables.Consumer<? super int[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final int[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final int[][] e : a) {
            if (N.notEmpty(e)) {
                for (final int[] ee : e) {
                    if (N.notEmpty(ee)) {
                        N.copy(tmp, idx, ee, 0, ee.length);
                        idx += ee.length;
                    }
                }
            }
        }
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[] add(final int[] a, final int[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] + b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[] add(final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return add(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static int[] add(final int len, final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] + b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA + b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] + valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA + valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[] add(final int[] a, final int[] b, final int[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] + b[i] + c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[] add(final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB, final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return add(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static int[] add(final int len, final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] + b[i] + c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) + (i < lenB ? b[i] : valueForNoneB) + (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[][] add(final int[][] a, final int[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[][] add(final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB) {
        return add(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static int[][] add(final int len, final int rowLen, final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = add(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = add(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = add(rowLen, (int[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[][] add(final int[][] a, final int[][] b, final int[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[][] add(final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA, final int valueForNoneB, final int valueForNoneC) {
        return add(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static int[][] add(final int len, final int rowLen, final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA,
            final int valueForNoneB, final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = add(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[][][] add(final int[][][] a, final int[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[][][] add(final int[][][] a, final int[][][] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = add(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = add(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = add(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[][][] add(final int[][][] a, final int[][][] b, final int[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[][][] add(final int[][][] a, final int[][][] b, final int[][][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = add(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = add(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[] subtract(final int[] a, final int[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] - b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[] subtract(final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return subtract(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static int[] subtract(final int len, final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] - b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA - b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] - valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA - valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[] subtract(final int[] a, final int[] b, final int[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] - b[i] - c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[] subtract(final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB, final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return subtract(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static int[] subtract(final int len, final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] - b[i] - c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) - (i < lenB ? b[i] : valueForNoneB) - (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[][] subtract(final int[][] a, final int[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[][] subtract(final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB) {
        return subtract(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static int[][] subtract(final int len, final int rowLen, final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = subtract(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = subtract(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = subtract(rowLen, (int[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[][] subtract(final int[][] a, final int[][] b, final int[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[][] subtract(final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC) {
        return subtract(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static int[][] subtract(final int len, final int rowLen, final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA,
            final int valueForNoneB, final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = subtract(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[][][] subtract(final int[][][] a, final int[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[][][] subtract(final int[][][] a, final int[][][] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = subtract(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = subtract(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = subtract(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[][][] subtract(final int[][][] a, final int[][][] b, final int[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[][][] subtract(final int[][][] a, final int[][][] b, final int[][][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = subtract(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = subtract(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[] multipliedBy(final int[] a, final int[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] * b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[] multipliedBy(final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return multipliedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static int[] multipliedBy(final int len, final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] * b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA * b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] * valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA * valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[] multipliedBy(final int[] a, final int[] b, final int[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] * b[i] * c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[] multipliedBy(final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB, final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return multipliedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static int[] multipliedBy(final int len, final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] * b[i] * c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) * (i < lenB ? b[i] : valueForNoneB) * (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[][] multipliedBy(final int[][] a, final int[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[][] multipliedBy(final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB) {
        return multipliedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static int[][] multipliedBy(final int len, final int rowLen, final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = multipliedBy(rowLen, (int[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[][] multipliedBy(final int[][] a, final int[][] b, final int[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[][] multipliedBy(final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC) {
        return multipliedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static int[][] multipliedBy(final int len, final int rowLen, final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA,
            final int valueForNoneB, final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = multipliedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[][][] multipliedBy(final int[][][] a, final int[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[][][] multipliedBy(final int[][][] a, final int[][][] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = multipliedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = multipliedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[][][] multipliedBy(final int[][][] a, final int[][][] b, final int[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[][][] multipliedBy(final int[][][] a, final int[][][] b, final int[][][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = multipliedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[] dividedBy(final int[] a, final int[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[] dividedBy(final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static int[] dividedBy(final int len, final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] / b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA / b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] / valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA / valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[] dividedBy(final int[] a, final int[] b, final int[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / b[i] / c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[] dividedBy(final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB, final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static int[] dividedBy(final int len, final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] / b[i] / c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) / (i < lenB ? b[i] : valueForNoneB) / (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[][] dividedBy(final int[][] a, final int[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[][] dividedBy(final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static int[][] dividedBy(final int len, final int rowLen, final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, (int[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[][] dividedBy(final int[][] a, final int[][] b, final int[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[][] dividedBy(final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static int[][] dividedBy(final int len, final int rowLen, final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA,
            final int valueForNoneB, final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int[][][] dividedBy(final int[][][] a, final int[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static int[][][] dividedBy(final int[][][] a, final int[][][] b, final int valueForNoneA, final int valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[][][] dividedBy(final int[][][] a, final int[][][] b, final int[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static int[][][] dividedBy(final int[][][] a, final int[][][] b, final int[][][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static int[] dividedBy(final int[] a, final int[] b, final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static int[] dividedBy(final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB, final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static int[] dividedBy(final int len, final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB,
            final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA / (b[i] == 0 ? defaultValueForZero : b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] / valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA / valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static int[] dividedBy(final int[] a, final int[] b, final int[] c, final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static int[] dividedBy(final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB, final int valueForNoneC,
            final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static int[] dividedBy(final int len, final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC, final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) / (i < lenB ? (b[i] == 0 ? defaultValueForZero : b[i]) : valueForNoneB)
                        / (i < lenC ? (c[i] == 0 ? defaultValueForZero : c[i]) : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static int[][] dividedBy(final int[][] a, final int[][] b, final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static int[][] dividedBy(final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB, final int defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static int[][] dividedBy(final int len, final int rowLen, final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB,
            final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, (int[]) null, null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static int[][] dividedBy(final int[][] a, final int[][] b, final int[][] c, final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static int[][] dividedBy(final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC, final int defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static int[][] dividedBy(final int len, final int rowLen, final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA,
            final int valueForNoneB, final int valueForNoneC, final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static int[][][] dividedBy(final int[][][] a, final int[][][] b, final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static int[][][] dividedBy(final int[][][] a, final int[][][] b, final int valueForNoneA, final int valueForNoneB, final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static int[][][] dividedBy(final int[][][] a, final int[][][] b, final int[][][] c, final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static int[][][] dividedBy(final int[][][] a, final int[][][] b, final int[][][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC, final int defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                    defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[] zip(final int[] a, final int[] b, final Throwables.IntBiFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[] zip(final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB,
            final Throwables.IntBiFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return zip(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> int[] zip(final int len, final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB,
            final Throwables.IntBiFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zipFunction.apply(valueForNoneA, b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zipFunction.apply(a[i], valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zipFunction.apply(valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[] zip(final int[] a, final int[] b, final int[] c, final Throwables.IntTriFunction<Integer, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[] zip(final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC, final Throwables.IntTriFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return zip(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> int[] zip(final int len, final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC, final Throwables.IntTriFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[] result = new int[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zipFunction.apply(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][] zip(final int[][] a, final int[][] b, final Throwables.IntBiFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][] zip(final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB,
            final Throwables.IntBiFunction<Integer, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> int[][] zip(final int len, final int rowLen, final int[][] a, final int[][] b, final int valueForNoneA,
            final int valueForNoneB, final Throwables.IntBiFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zip(rowLen, null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zip(rowLen, a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zip(rowLen, null, null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][] zip(final int[][] a, final int[][] b, final int[][] c, final Throwables.IntTriFunction<Integer, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][] zip(final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC, final Throwables.IntTriFunction<Integer, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> int[][] zip(final int len, final int rowLen, final int[][] a, final int[][] b, final int[][] c,
            final int valueForNoneA, final int valueForNoneB, final int valueForNoneC, final Throwables.IntTriFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][] result = new int[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zip(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][][] zip(final int[][][] a, final int[][][] b, final Throwables.IntBiFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][][] zip(final int[][][] a, final int[][][] b, final int valueForNoneA, final int valueForNoneB,
            final Throwables.IntBiFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final int[][][] result = new int[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][][] zip(final int[][][] a, final int[][][] b, final int[][][] c,
            final Throwables.IntTriFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> int[][][] zip(final int[][][] a, final int[][][] b, final int[][][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC, final Throwables.IntTriFunction<Integer, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final int[][][] result = new int[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    public static long totalCountOfElements(final int[][] a) {
        long count = 0;

        for (final int[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    public static long totalCountOfElements(final int[][][] a) {
        long count = 0;

        for (final int[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final int[] ints : element) {
                if (N.isEmpty(ints)) {
                    continue;
                }

                count += ints.length;
            }
        }

        return count;
    }

    /**
     * Min sub array len.
     *
     * @param a
     * @return
     */
    public static int minSubArrayLen(final int[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;

        for (final int[] ae : a) {
            minLen = N.min(minLen, ae == null ? 0 : ae.length);
        }

        return minLen;
    }

    /**
     * Max sub array len.
     *
     * @param a
     * @return
     */
    public static int maxSubArrayLen(final int[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int maxLen = 0;

        for (final int[] ae : a) {
            maxLen = N.max(maxLen, ae == null ? 0 : ae.length);
        }

        return maxLen;
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final int[] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            return N.println(N.toString(a));
        }
    }

    /**
     *
     * @param a
     */
    public static String println(final int[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final int[] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(ai[j]);
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final int[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final int[][] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR).append("  ");
                            }

                            if (ai[j] == null) {
                                sb.append("null");
                            } else if (ai[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final int[] aij = ai[j];
                                sb.append('[');

                                for (int k = 0, aijLen = aij.length; k < aijLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(aij[k]);
                                }

                                sb.append(']');
                            }
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            N.println(str);

            return str;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final long[] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] += param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final long[][] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final long[][][] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[][] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final long[] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] -= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final long[][] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final long[][][] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[][] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final long[] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] *= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final long[][] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final long[][][] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[][] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final long[] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] /= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final long[][] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final long[][][] a, final long param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[][] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final long[] a, final Throwables.LongUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] = operator.applyAsLong(a[i]);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final long[][] a, final Throwables.LongUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final long[][][] a, final Throwables.LongUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[][] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final long[] a, final Throwables.LongPredicate<E> predicate, final long newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            if (predicate.test(a[i])) {
                a[i] = newValue;
            }
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final long[][] a, final Throwables.LongPredicate<E> predicate, final long newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final long[][][] a, final Throwables.LongPredicate<E> predicate, final long newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final long[][] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     *
     * @param a
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static long[][] reshape(final long[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new long[0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, cols, RoundingMode.CEILING);
        final long[][] c = new long[n][];

        for (int i = 0, from = 0; i < n; i++, from += cols) {
            c[i] = N.copyOfRange(a, from, from + N.min(len - from, cols));
        }

        return c;
    }

    /**
     *
     *
     * @param a
     * @param rows
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static long[][][] reshape(final long[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new long[0][0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, rows * cols, RoundingMode.CEILING);
        final long[][][] c = new long[n][][];

        for (int i = 0, from = 0; i < n; i++) {
            c[i] = new long[N.min(rows, Numbers.divide(len - from, cols, RoundingMode.CEILING))][];

            for (int j = 0, y = c[i].length; j < y; j++, from += cols) {
                c[i][j] = N.copyOfRange(a, from, from + N.min(len - from, cols));
            }
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[] flatten(final long[][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_LONG_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final long[] c = new long[count];
        int from = 0;

        for (final long[] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            N.copy(element, 0, c, from, element.length);

            from += element.length;
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[] flatten(final long[][][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_LONG_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final long[] c = new long[count];
        int from = 0;

        for (final long[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final long[] longs : element) {
                if (N.isEmpty(longs)) {
                    continue;
                }

                N.copy(longs, 0, c, from, longs.length);

                from += longs.length;
            }
        }

        return c;
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final long[][] a, final Throwables.Consumer<? super long[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final long[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final long[] e : a) {
            if (N.notEmpty(e)) {
                N.copy(tmp, idx, e, 0, e.length);
                idx += e.length;
            }
        }
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final long[][][] a, final Throwables.Consumer<? super long[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final long[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final long[][] e : a) {
            if (N.notEmpty(e)) {
                for (final long[] ee : e) {
                    if (N.notEmpty(ee)) {
                        N.copy(tmp, idx, ee, 0, ee.length);
                        idx += ee.length;
                    }
                }
            }
        }
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[] add(final long[] a, final long[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] + b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[] add(final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return add(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static long[] add(final int len, final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] + b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA + b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] + valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA + valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[] add(final long[] a, final long[] b, final long[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] + b[i] + c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[] add(final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB, final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return add(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static long[] add(final int len, final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] + b[i] + c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) + (i < lenB ? b[i] : valueForNoneB) + (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[][] add(final long[][] a, final long[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[][] add(final long[][] a, final long[][] b, final long valueForNoneA, final long valueForNoneB) {
        return add(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static long[][] add(final int len, final int rowLen, final long[][] a, final long[][] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = add(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = add(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = add(rowLen, (long[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[][] add(final long[][] a, final long[][] b, final long[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[][] add(final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        return add(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static long[][] add(final int len, final int rowLen, final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA,
            final long valueForNoneB, final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = add(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[][][] add(final long[][][] a, final long[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[][][] add(final long[][][] a, final long[][][] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = add(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = add(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = add(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[][][] add(final long[][][] a, final long[][][] b, final long[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[][][] add(final long[][][] a, final long[][][] b, final long[][][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = add(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = add(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[] subtract(final long[] a, final long[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] - b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[] subtract(final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return subtract(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static long[] subtract(final int len, final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] - b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA - b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] - valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA - valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[] subtract(final long[] a, final long[] b, final long[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] - b[i] - c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[] subtract(final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return subtract(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static long[] subtract(final int len, final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] - b[i] - c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) - (i < lenB ? b[i] : valueForNoneB) - (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[][] subtract(final long[][] a, final long[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[][] subtract(final long[][] a, final long[][] b, final long valueForNoneA, final long valueForNoneB) {
        return subtract(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static long[][] subtract(final int len, final int rowLen, final long[][] a, final long[][] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = subtract(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = subtract(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = subtract(rowLen, (long[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[][] subtract(final long[][] a, final long[][] b, final long[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[][] subtract(final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        return subtract(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static long[][] subtract(final int len, final int rowLen, final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA,
            final long valueForNoneB, final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = subtract(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[][][] subtract(final long[][][] a, final long[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[][][] subtract(final long[][][] a, final long[][][] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = subtract(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = subtract(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = subtract(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[][][] subtract(final long[][][] a, final long[][][] b, final long[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[][][] subtract(final long[][][] a, final long[][][] b, final long[][][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = subtract(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = subtract(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[] multipliedBy(final long[] a, final long[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] * b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[] multipliedBy(final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return multipliedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static long[] multipliedBy(final int len, final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] * b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA * b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] * valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA * valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[] multipliedBy(final long[] a, final long[] b, final long[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] * b[i] * c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[] multipliedBy(final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return multipliedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static long[] multipliedBy(final int len, final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] * b[i] * c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) * (i < lenB ? b[i] : valueForNoneB) * (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[][] multipliedBy(final long[][] a, final long[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[][] multipliedBy(final long[][] a, final long[][] b, final long valueForNoneA, final long valueForNoneB) {
        return multipliedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static long[][] multipliedBy(final int len, final int rowLen, final long[][] a, final long[][] b, final long valueForNoneA,
            final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = multipliedBy(rowLen, (long[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[][] multipliedBy(final long[][] a, final long[][] b, final long[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[][] multipliedBy(final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        return multipliedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static long[][] multipliedBy(final int len, final int rowLen, final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA,
            final long valueForNoneB, final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = multipliedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[][][] multipliedBy(final long[][][] a, final long[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[][][] multipliedBy(final long[][][] a, final long[][][] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = multipliedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = multipliedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[][][] multipliedBy(final long[][][] a, final long[][][] b, final long[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[][][] multipliedBy(final long[][][] a, final long[][][] b, final long[][][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = multipliedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[] dividedBy(final long[] a, final long[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[] dividedBy(final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static long[] dividedBy(final int len, final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] / b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA / b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] / valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA / valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[] dividedBy(final long[] a, final long[] b, final long[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / b[i] / c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[] dividedBy(final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static long[] dividedBy(final int len, final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] / b[i] / c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) / (i < lenB ? b[i] : valueForNoneB) / (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[][] dividedBy(final long[][] a, final long[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[][] dividedBy(final long[][] a, final long[][] b, final long valueForNoneA, final long valueForNoneB) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static long[][] dividedBy(final int len, final int rowLen, final long[][] a, final long[][] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, (long[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[][] dividedBy(final long[][] a, final long[][] b, final long[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[][] dividedBy(final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static long[][] dividedBy(final int len, final int rowLen, final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA,
            final long valueForNoneB, final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static long[][][] dividedBy(final long[][][] a, final long[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static long[][][] dividedBy(final long[][][] a, final long[][][] b, final long valueForNoneA, final long valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long[][][] dividedBy(final long[][][] a, final long[][][] b, final long[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static long[][][] dividedBy(final long[][][] a, final long[][][] b, final long[][][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static long[] dividedBy(final long[] a, final long[] b, final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static long[] dividedBy(final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB, final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static long[] dividedBy(final int len, final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB,
            final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA / (b[i] == 0 ? defaultValueForZero : b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] / valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA / valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static long[] dividedBy(final long[] a, final long[] b, final long[] c, final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static long[] dividedBy(final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB, final long valueForNoneC,
            final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static long[] dividedBy(final int len, final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC, final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) / (i < lenB ? (b[i] == 0 ? defaultValueForZero : b[i]) : valueForNoneB)
                        / (i < lenC ? (c[i] == 0 ? defaultValueForZero : c[i]) : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static long[][] dividedBy(final long[][] a, final long[][] b, final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static long[][] dividedBy(final long[][] a, final long[][] b, final long valueForNoneA, final long valueForNoneB, final long defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static long[][] dividedBy(final int len, final int rowLen, final long[][] a, final long[][] b, final long valueForNoneA, final long valueForNoneB,
            final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, (long[]) null, null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static long[][] dividedBy(final long[][] a, final long[][] b, final long[][] c, final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static long[][] dividedBy(final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC, final long defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static long[][] dividedBy(final int len, final int rowLen, final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA,
            final long valueForNoneB, final long valueForNoneC, final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static long[][][] dividedBy(final long[][][] a, final long[][][] b, final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static long[][][] dividedBy(final long[][][] a, final long[][][] b, final long valueForNoneA, final long valueForNoneB,
            final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static long[][][] dividedBy(final long[][][] a, final long[][][] b, final long[][][] c, final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static long[][][] dividedBy(final long[][][] a, final long[][][] b, final long[][][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC, final long defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                    defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[] zip(final long[] a, final long[] b, final Throwables.LongBiFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[] zip(final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB,
            final Throwables.LongBiFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return zip(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> long[] zip(final int len, final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB,
            final Throwables.LongBiFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zipFunction.apply(valueForNoneA, b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zipFunction.apply(a[i], valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zipFunction.apply(valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[] zip(final long[] a, final long[] b, final long[] c, final Throwables.LongTriFunction<Long, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[] zip(final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC, final Throwables.LongTriFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return zip(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> long[] zip(final int len, final long[] a, final long[] b, final long[] c, final long valueForNoneA,
            final long valueForNoneB, final long valueForNoneC, final Throwables.LongTriFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[] result = new long[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zipFunction.apply(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][] zip(final long[][] a, final long[][] b, final Throwables.LongBiFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][] zip(final long[][] a, final long[][] b, final long valueForNoneA, final long valueForNoneB,
            final Throwables.LongBiFunction<Long, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> long[][] zip(final int len, final int rowLen, final long[][] a, final long[][] b, final long valueForNoneA,
            final long valueForNoneB, final Throwables.LongBiFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zip(rowLen, null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zip(rowLen, a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zip(rowLen, null, null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][] zip(final long[][] a, final long[][] b, final long[][] c,
            final Throwables.LongTriFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][] zip(final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC, final Throwables.LongTriFunction<Long, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> long[][] zip(final int len, final int rowLen, final long[][] a, final long[][] b, final long[][] c,
            final long valueForNoneA, final long valueForNoneB, final long valueForNoneC, final Throwables.LongTriFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][] result = new long[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zip(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][][] zip(final long[][][] a, final long[][][] b, final Throwables.LongBiFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][][] zip(final long[][][] a, final long[][][] b, final long valueForNoneA, final long valueForNoneB,
            final Throwables.LongBiFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final long[][][] result = new long[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][][] zip(final long[][][] a, final long[][][] b, final long[][][] c,
            final Throwables.LongTriFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> long[][][] zip(final long[][][] a, final long[][][] b, final long[][][] c, final long valueForNoneA,
            final long valueForNoneB, final long valueForNoneC, final Throwables.LongTriFunction<Long, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final long[][][] result = new long[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    public static long totalCountOfElements(final long[][] a) {
        long count = 0;

        for (final long[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    public static long totalCountOfElements(final long[][][] a) {
        long count = 0;

        for (final long[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final long[] longs : element) {
                if (N.isEmpty(longs)) {
                    continue;
                }

                count += longs.length;
            }
        }

        return count;
    }

    /**
     * Min sub array len.
     *
     * @param a
     * @return
     */
    public static int minSubArrayLen(final long[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;

        for (final long[] ae : a) {
            minLen = N.min(minLen, ae == null ? 0 : ae.length);
        }

        return minLen;
    }

    /**
     * Max sub array len.
     *
     * @param a
     * @return
     */
    public static int maxSubArrayLen(final long[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int maxLen = 0;

        for (final long[] ae : a) {
            maxLen = N.max(maxLen, ae == null ? 0 : ae.length);
        }

        return maxLen;
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final long[] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            return N.println(N.toString(a));
        }
    }

    /**
     *
     * @param a
     */
    public static String println(final long[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final long[] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(ai[j]);
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     */
    public static String println(final long[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final long[][] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR).append("  ");
                            }

                            if (ai[j] == null) {
                                sb.append("null");
                            } else if (ai[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final long[] aij = ai[j];
                                sb.append('[');

                                for (int k = 0, aijLen = aij.length; k < aijLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(aij[k]);
                                }

                                sb.append(']');
                            }
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final float[] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] += param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final float[][] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final float[][][] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[][] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final float[] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] -= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final float[][] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final float[][][] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[][] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final float[] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] *= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final float[][] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final float[][][] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[][] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final float[] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] /= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final float[][] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final float[][][] a, final float param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[][] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final float[] a, final Throwables.FloatUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] = operator.applyAsFloat(a[i]);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final float[][] a, final Throwables.FloatUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final float[][][] a, final Throwables.FloatUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[][] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final float[] a, final Throwables.FloatPredicate<E> predicate, final float newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            if (predicate.test(a[i])) {
                a[i] = newValue;
            }
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final float[][] a, final Throwables.FloatPredicate<E> predicate, final float newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final float[][][] a, final Throwables.FloatPredicate<E> predicate, final float newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final float[][] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     *
     * @param a
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static float[][] reshape(final float[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new float[0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, cols, RoundingMode.CEILING);
        final float[][] c = new float[n][];

        for (int i = 0, from = 0; i < n; i++, from += cols) {
            c[i] = N.copyOfRange(a, from, from + N.min(len - from, cols));
        }

        return c;
    }

    /**
     *
     *
     * @param a
     * @param rows
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static float[][][] reshape(final float[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new float[0][0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, rows * cols, RoundingMode.CEILING);
        final float[][][] c = new float[n][][];

        for (int i = 0, from = 0; i < n; i++) {
            c[i] = new float[N.min(rows, Numbers.divide(len - from, cols, RoundingMode.CEILING))][];

            for (int j = 0, y = c[i].length; j < y; j++, from += cols) {
                c[i][j] = N.copyOfRange(a, from, from + N.min(len - from, cols));
            }
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[] flatten(final float[][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_FLOAT_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final float[] c = new float[count];
        int from = 0;

        for (final float[] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            N.copy(element, 0, c, from, element.length);

            from += element.length;
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[] flatten(final float[][][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_FLOAT_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final float[] c = new float[count];
        int from = 0;

        for (final float[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final float[] floats : element) {
                if (N.isEmpty(floats)) {
                    continue;
                }

                N.copy(floats, 0, c, from, floats.length);

                from += floats.length;
            }
        }

        return c;
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final float[][] a, final Throwables.Consumer<? super float[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final float[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final float[] e : a) {
            if (N.notEmpty(e)) {
                N.copy(tmp, idx, e, 0, e.length);
                idx += e.length;
            }
        }
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final float[][][] a, final Throwables.Consumer<? super float[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final float[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final float[][] e : a) {
            if (N.notEmpty(e)) {
                for (final float[] ee : e) {
                    if (N.notEmpty(ee)) {
                        N.copy(tmp, idx, ee, 0, ee.length);
                        idx += ee.length;
                    }
                }
            }
        }
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[] add(final float[] a, final float[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] + b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[] add(final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return add(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static float[] add(final int len, final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] + b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA + b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] + valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA + valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[] add(final float[] a, final float[] b, final float[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] + b[i] + c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[] add(final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return add(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static float[] add(final int len, final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] + b[i] + c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) + (i < lenB ? b[i] : valueForNoneB) + (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[][] add(final float[][] a, final float[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[][] add(final float[][] a, final float[][] b, final float valueForNoneA, final float valueForNoneB) {
        return add(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static float[][] add(final int len, final int rowLen, final float[][] a, final float[][] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = add(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = add(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = add(rowLen, (float[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[][] add(final float[][] a, final float[][] b, final float[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[][] add(final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        return add(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static float[][] add(final int len, final int rowLen, final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA,
            final float valueForNoneB, final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = add(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[][][] add(final float[][][] a, final float[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[][][] add(final float[][][] a, final float[][][] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = add(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = add(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = add(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[][][] add(final float[][][] a, final float[][][] b, final float[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[][][] add(final float[][][] a, final float[][][] b, final float[][][] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = add(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = add(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[] subtract(final float[] a, final float[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] - b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[] subtract(final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return subtract(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static float[] subtract(final int len, final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] - b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA - b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] - valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA - valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[] subtract(final float[] a, final float[] b, final float[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] - b[i] - c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[] subtract(final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return subtract(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static float[] subtract(final int len, final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] - b[i] - c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) - (i < lenB ? b[i] : valueForNoneB) - (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[][] subtract(final float[][] a, final float[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[][] subtract(final float[][] a, final float[][] b, final float valueForNoneA, final float valueForNoneB) {
        return subtract(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static float[][] subtract(final int len, final int rowLen, final float[][] a, final float[][] b, final float valueForNoneA,
            final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = subtract(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = subtract(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = subtract(rowLen, (float[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[][] subtract(final float[][] a, final float[][] b, final float[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[][] subtract(final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        return subtract(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static float[][] subtract(final int len, final int rowLen, final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA,
            final float valueForNoneB, final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = subtract(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[][][] subtract(final float[][][] a, final float[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[][][] subtract(final float[][][] a, final float[][][] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = subtract(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = subtract(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = subtract(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[][][] subtract(final float[][][] a, final float[][][] b, final float[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[][][] subtract(final float[][][] a, final float[][][] b, final float[][][] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = subtract(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = subtract(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[] multipliedBy(final float[] a, final float[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] * b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[] multipliedBy(final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return multipliedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static float[] multipliedBy(final int len, final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] * b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA * b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] * valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA * valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[] multipliedBy(final float[] a, final float[] b, final float[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] * b[i] * c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[] multipliedBy(final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return multipliedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static float[] multipliedBy(final int len, final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] * b[i] * c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) * (i < lenB ? b[i] : valueForNoneB) * (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[][] multipliedBy(final float[][] a, final float[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[][] multipliedBy(final float[][] a, final float[][] b, final float valueForNoneA, final float valueForNoneB) {
        return multipliedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static float[][] multipliedBy(final int len, final int rowLen, final float[][] a, final float[][] b, final float valueForNoneA,
            final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = multipliedBy(rowLen, (float[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[][] multipliedBy(final float[][] a, final float[][] b, final float[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[][] multipliedBy(final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        return multipliedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static float[][] multipliedBy(final int len, final int rowLen, final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA,
            final float valueForNoneB, final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = multipliedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[][][] multipliedBy(final float[][][] a, final float[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[][][] multipliedBy(final float[][][] a, final float[][][] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = multipliedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = multipliedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[][][] multipliedBy(final float[][][] a, final float[][][] b, final float[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[][][] multipliedBy(final float[][][] a, final float[][][] b, final float[][][] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = multipliedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[] dividedBy(final float[] a, final float[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[] dividedBy(final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static float[] dividedBy(final int len, final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] / b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA / b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] / valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA / valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[] dividedBy(final float[] a, final float[] b, final float[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / b[i] / c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[] dividedBy(final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static float[] dividedBy(final int len, final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] / b[i] / c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) / (i < lenB ? b[i] : valueForNoneB) / (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[][] dividedBy(final float[][] a, final float[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[][] dividedBy(final float[][] a, final float[][] b, final float valueForNoneA, final float valueForNoneB) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static float[][] dividedBy(final int len, final int rowLen, final float[][] a, final float[][] b, final float valueForNoneA,
            final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, (float[]) null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[][] dividedBy(final float[][] a, final float[][] b, final float[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[][] dividedBy(final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static float[][] dividedBy(final int len, final int rowLen, final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA,
            final float valueForNoneB, final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static float[][][] dividedBy(final float[][][] a, final float[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static float[][][] dividedBy(final float[][][] a, final float[][][] b, final float valueForNoneA, final float valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float[][][] dividedBy(final float[][][] a, final float[][][] b, final float[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static float[][][] dividedBy(final float[][][] a, final float[][][] b, final float[][][] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static float[] dividedBy(final float[] a, final float[] b, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static float[] dividedBy(final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static float[] dividedBy(final int len, final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB,
            final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA / (b[i] == 0 ? defaultValueForZero : b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] / valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA / valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static float[] dividedBy(final float[] a, final float[] b, final float[] c, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static float[] dividedBy(final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static float[] dividedBy(final int len, final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) / (i < lenB ? (b[i] == 0 ? defaultValueForZero : b[i]) : valueForNoneB)
                        / (i < lenC ? (c[i] == 0 ? defaultValueForZero : c[i]) : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static float[][] dividedBy(final float[][] a, final float[][] b, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static float[][] dividedBy(final float[][] a, final float[][] b, final float valueForNoneA, final float valueForNoneB,
            final float defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static float[][] dividedBy(final int len, final int rowLen, final float[][] a, final float[][] b, final float valueForNoneA,
            final float valueForNoneB, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, (float[]) null, null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static float[][] dividedBy(final float[][] a, final float[][] b, final float[][] c, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static float[][] dividedBy(final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC, final float defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static float[][] dividedBy(final int len, final int rowLen, final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA,
            final float valueForNoneB, final float valueForNoneC, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static float[][][] dividedBy(final float[][][] a, final float[][][] b, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static float[][][] dividedBy(final float[][][] a, final float[][][] b, final float valueForNoneA, final float valueForNoneB,
            final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static float[][][] dividedBy(final float[][][] a, final float[][][] b, final float[][][] c, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static float[][][] dividedBy(final float[][][] a, final float[][][] b, final float[][][] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC, final float defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                    defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[] zip(final float[] a, final float[] b, final Throwables.FloatBiFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[] zip(final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB,
            final Throwables.FloatBiFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return zip(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> float[] zip(final int len, final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB,
            final Throwables.FloatBiFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zipFunction.apply(valueForNoneA, b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zipFunction.apply(a[i], valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zipFunction.apply(valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[] zip(final float[] a, final float[] b, final float[] c, final Throwables.FloatTriFunction<Float, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[] zip(final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC, final Throwables.FloatTriFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return zip(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> float[] zip(final int len, final float[] a, final float[] b, final float[] c, final float valueForNoneA,
            final float valueForNoneB, final float valueForNoneC, final Throwables.FloatTriFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[] result = new float[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zipFunction.apply(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[][] zip(final float[][] a, final float[][] b, final Throwables.FloatBiFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[][] zip(final float[][] a, final float[][] b, final float valueForNoneA, final float valueForNoneB,
            final Throwables.FloatBiFunction<Float, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> float[][] zip(final int len, final int rowLen, final float[][] a, final float[][] b, final float valueForNoneA,
            final float valueForNoneB, final Throwables.FloatBiFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zip(rowLen, null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zip(rowLen, a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zip(rowLen, null, null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[][] zip(final float[][] a, final float[][] b, final float[][] c,
            final Throwables.FloatTriFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[][] zip(final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA,
            final float valueForNoneB, final float valueForNoneC, final Throwables.FloatTriFunction<Float, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> float[][] zip(final int len, final int rowLen, final float[][] a, final float[][] b, final float[][] c,
            final float valueForNoneA, final float valueForNoneB, final float valueForNoneC, final Throwables.FloatTriFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][] result = new float[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zip(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[][][] zip(final float[][][] a, final float[][][] b, final Throwables.FloatBiFunction<Float, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[][][] zip(final float[][][] a, final float[][][] b, final float valueForNoneA, final float valueForNoneB,
            final Throwables.FloatBiFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final float[][][] result = new float[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[][][] zip(final float[][][] a, final float[][][] b, final float[][][] c,
            final Throwables.FloatTriFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> float[][][] zip(final float[][][] a, final float[][][] b, final float[][][] c, final float valueForNoneA,
            final float valueForNoneB, final float valueForNoneC, final Throwables.FloatTriFunction<Float, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final float[][][] result = new float[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    public static long totalCountOfElements(final float[][] a) {
        long count = 0;

        for (final float[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    public static long totalCountOfElements(final float[][][] a) {
        long count = 0;

        for (final float[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final float[] floats : element) {
                if (N.isEmpty(floats)) {
                    continue;
                }

                count += floats.length;
            }
        }

        return count;
    }

    /**
     * Min sub array len.
     *
     * @param a
     * @return
     */
    public static int minSubArrayLen(final float[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;

        for (final float[] ae : a) {
            minLen = N.min(minLen, ae == null ? 0 : ae.length);
        }

        return minLen;
    }

    /**
     * Max sub array len.
     *
     * @param a
     * @return
     */
    public static int maxSubArrayLen(final float[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int maxLen = 0;

        for (final float[] ae : a) {
            maxLen = N.max(maxLen, ae == null ? 0 : ae.length);
        }

        return maxLen;
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final float[] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            return N.println(N.toString(a));
        }
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final float[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final float[] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(ai[j]);
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final float[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final float[][] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR).append("  ");
                            }

                            if (ai[j] == null) {
                                sb.append("null");
                            } else if (ai[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final float[] aij = ai[j];
                                sb.append('[');

                                for (int k = 0, aijLen = aij.length; k < aijLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(aij[k]);
                                }

                                sb.append(']');
                            }
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final double[] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] += param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final double[][] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void plus(final double[][][] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[][] element : a) {
            plus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final double[] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] -= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final double[][] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void minus(final double[][][] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[][] element : a) {
            minus(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final double[] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] *= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final double[][] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void multipliedBy(final double[][][] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[][] element : a) {
            multipliedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final double[] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] /= param;
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final double[][] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param a
     * @param param
     */
    public static void dividedBy(final double[][][] a, final double param) {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[][] element : a) {
            dividedBy(element, param);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final double[] a, final Throwables.DoubleUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            a[i] = operator.applyAsDouble(a[i]);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final double[][] a, final Throwables.DoubleUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param operator
     * @throws E the e
     */
    public static <E extends Exception> void updateAll(final double[][][] a, final Throwables.DoubleUnaryOperator<E> operator) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[][] element : a) {
            updateAll(element, operator);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final double[] a, final Throwables.DoublePredicate<E> predicate, final double newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (int i = 0, n = a.length; i < n; i++) {
            if (predicate.test(a[i])) {
                a[i] = newValue;
            }
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final double[][] a, final Throwables.DoublePredicate<E> predicate, final double newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     * @param <E>
     * @param a
     * @param predicate
     * @param newValue
     * @throws E the e
     */
    public static <E extends Exception> void replaceIf(final double[][][] a, final Throwables.DoublePredicate<E> predicate, final double newValue) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        for (final double[][] element : a) {
            replaceIf(element, predicate, newValue);
        }
    }

    /**
     *
     *
     * @param a
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static double[][] reshape(final double[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new double[0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, cols, RoundingMode.CEILING);
        final double[][] c = new double[n][];

        for (int i = 0, from = 0; i < n; i++, from += cols) {
            c[i] = N.copyOfRange(a, from, from + N.min(len - from, cols));
        }

        return c;
    }

    /**
     *
     *
     * @param a
     * @param rows
     * @param cols
     * @return
     * @throws IllegalArgumentException
     */
    public static double[][][] reshape(final double[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new double[0][0][0];
        }

        final int len = a.length;
        final int n = Numbers.divide(len, rows * cols, RoundingMode.CEILING);
        final double[][][] c = new double[n][][];

        for (int i = 0, from = 0; i < n; i++) {
            c[i] = new double[N.min(rows, Numbers.divide(len - from, cols, RoundingMode.CEILING))][];

            for (int j = 0, y = c[i].length; j < y; j++, from += cols) {
                c[i][j] = N.copyOfRange(a, from, from + N.min(len - from, cols));
            }
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[] flatten(final double[][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_DOUBLE_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final double[] c = new double[count];
        int from = 0;

        for (final double[] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            N.copy(element, 0, c, from, element.length);

            from += element.length;
        }

        return c;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[] flatten(final double[][][] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_DOUBLE_ARRAY;
        }

        final int count = Numbers.toIntExact(totalCountOfElements(a));

        final double[] c = new double[count];
        int from = 0;

        for (final double[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final double[] doubles : element) {
                if (N.isEmpty(doubles)) {
                    continue;
                }

                N.copy(doubles, 0, c, from, doubles.length);

                from += doubles.length;
            }
        }

        return c;
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final double[][] a, final Throwables.Consumer<? super double[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final double[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final double[] e : a) {
            if (N.notEmpty(e)) {
                N.copy(tmp, idx, e, 0, e.length);
                idx += e.length;
            }
        }
    }

    /**
     * flatten -> execute {@code op} -> set values back.
     * <pre>
     * <code>
     * f.flatOp(a, t -> N.sort(t));
     * </code>
     * </pre>
     *
     * @param <E>
     * @param a
     * @param op
     * @throws E the e
     */
    public static <E extends Exception> void flatOp(final double[][][] a, final Throwables.Consumer<? super double[], E> op) throws E {
        if (N.isEmpty(a)) {
            return;
        }

        final double[] tmp = flatten(a);

        op.accept(tmp);

        int idx = 0;

        for (final double[][] e : a) {
            if (N.notEmpty(e)) {
                for (final double[] ee : e) {
                    if (N.notEmpty(ee)) {
                        N.copy(tmp, idx, ee, 0, ee.length);
                        idx += ee.length;
                    }
                }
            }
        }
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[] add(final double[] a, final double[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] + b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[] add(final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return add(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static double[] add(final int len, final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] + b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA + b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] + valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA + valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[] add(final double[] a, final double[] b, final double[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] + b[i] + c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[] add(final double[] a, final double[] b, final double[] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return add(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static double[] add(final int len, final double[] a, final double[] b, final double[] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] + b[i] + c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) + (i < lenB ? b[i] : valueForNoneB) + (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[][] add(final double[][] a, final double[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[][] add(final double[][] a, final double[][] b, final double valueForNoneA, final double valueForNoneB) {
        return add(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static double[][] add(final int len, final int rowLen, final double[][] a, final double[][] b, final double valueForNoneA,
            final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = add(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = add(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = add(rowLen, null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[][] add(final double[][] a, final double[][] b, final double[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[][] add(final double[][] a, final double[][] b, final double[][] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC) {
        return add(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static double[][] add(final int len, final int rowLen, final double[][] a, final double[][] b, final double[][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = add(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = add(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[][][] add(final double[][][] a, final double[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[][][] add(final double[][][] a, final double[][][] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = add(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = add(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = add(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[][][] add(final double[][][] a, final double[][][] b, final double[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = add(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[][][] add(final double[][][] a, final double[][][] b, final double[][][] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = add(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = add(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[] subtract(final double[] a, final double[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] - b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[] subtract(final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return subtract(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static double[] subtract(final int len, final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] - b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA - b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] - valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA - valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[] subtract(final double[] a, final double[] b, final double[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] - b[i] - c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[] subtract(final double[] a, final double[] b, final double[] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return subtract(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static double[] subtract(final int len, final double[] a, final double[] b, final double[] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] - b[i] - c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) - (i < lenB ? b[i] : valueForNoneB) - (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[][] subtract(final double[][] a, final double[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[][] subtract(final double[][] a, final double[][] b, final double valueForNoneA, final double valueForNoneB) {
        return subtract(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static double[][] subtract(final int len, final int rowLen, final double[][] a, final double[][] b, final double valueForNoneA,
            final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = subtract(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = subtract(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = subtract(rowLen, null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[][] subtract(final double[][] a, final double[][] b, final double[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[][] subtract(final double[][] a, final double[][] b, final double[][] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC) {
        return subtract(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static double[][] subtract(final int len, final int rowLen, final double[][] a, final double[][] b, final double[][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = subtract(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = subtract(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[][][] subtract(final double[][][] a, final double[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[][][] subtract(final double[][][] a, final double[][][] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = subtract(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = subtract(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = subtract(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[][][] subtract(final double[][][] a, final double[][][] b, final double[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = subtract(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[][][] subtract(final double[][][] a, final double[][][] b, final double[][][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = subtract(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = subtract(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[] multipliedBy(final double[] a, final double[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] * b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[] multipliedBy(final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return multipliedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static double[] multipliedBy(final int len, final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] * b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA * b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] * valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA * valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[] multipliedBy(final double[] a, final double[] b, final double[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] * b[i] * c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[] multipliedBy(final double[] a, final double[] b, final double[] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return multipliedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static double[] multipliedBy(final int len, final double[] a, final double[] b, final double[] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] * b[i] * c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) * (i < lenB ? b[i] : valueForNoneB) * (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[][] multipliedBy(final double[][] a, final double[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[][] multipliedBy(final double[][] a, final double[][] b, final double valueForNoneA, final double valueForNoneB) {
        return multipliedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static double[][] multipliedBy(final int len, final int rowLen, final double[][] a, final double[][] b, final double valueForNoneA,
            final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = multipliedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = multipliedBy(rowLen, null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[][] multipliedBy(final double[][] a, final double[][] b, final double[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[][] multipliedBy(final double[][] a, final double[][] b, final double[][] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC) {
        return multipliedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static double[][] multipliedBy(final int len, final int rowLen, final double[][] a, final double[][] b, final double[][] c,
            final double valueForNoneA, final double valueForNoneB, final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = multipliedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = multipliedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[][][] multipliedBy(final double[][][] a, final double[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[][][] multipliedBy(final double[][][] a, final double[][][] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = multipliedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = multipliedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[][][] multipliedBy(final double[][][] a, final double[][][] b, final double[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[][][] multipliedBy(final double[][][] a, final double[][][] b, final double[][][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = multipliedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = multipliedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[] dividedBy(final double[] a, final double[] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / b[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[] dividedBy(final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static double[] dividedBy(final int len, final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] / b[i];
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA / b[i];
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] / valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA / valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[] dividedBy(final double[] a, final double[] b, final double[] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / b[i] / c[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[] dividedBy(final double[] a, final double[] b, final double[] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static double[] dividedBy(final int len, final double[] a, final double[] b, final double[] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] / b[i] / c[i];
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) / (i < lenB ? b[i] : valueForNoneB) / (i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[][] dividedBy(final double[][] a, final double[][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[][] dividedBy(final double[][] a, final double[][] b, final double valueForNoneA, final double valueForNoneB) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    private static double[][] dividedBy(final int len, final int rowLen, final double[][] a, final double[][] b, final double valueForNoneA,
            final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, null, null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[][] dividedBy(final double[][] a, final double[][] b, final double[][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[][] dividedBy(final double[][] a, final double[][] b, final double[][] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    private static double[][] dividedBy(final int len, final int rowLen, final double[][] a, final double[][] b, final double[][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[][][] dividedBy(final double[][][] a, final double[][][] b) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @return
     */
    public static double[][][] dividedBy(final double[][][] a, final double[][][] b, final double valueForNoneA, final double valueForNoneB) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[][][] dividedBy(final double[][][] a, final double[][][] b, final double[][][] c) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @return
     */
    public static double[][][] dividedBy(final double[][][] a, final double[][][] b, final double[][][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static double[] dividedBy(final double[] a, final double[] b, final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static double[] dividedBy(final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB,
            final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return dividedBy(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static double[] dividedBy(final int len, final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB,
            final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = valueForNoneA / (b[i] == 0 ? defaultValueForZero : b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = a[i] / valueForNoneB;
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = valueForNoneA / valueForNoneB;
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static double[] dividedBy(final double[] a, final double[] b, final double[] c, final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static double[] dividedBy(final double[] a, final double[] b, final double[] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC, final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return dividedBy(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static double[] dividedBy(final int len, final double[] a, final double[] b, final double[] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC, final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = a[i] / (b[i] == 0 ? defaultValueForZero : b[i]) / (c[i] == 0 ? defaultValueForZero : c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = (i < lenA ? a[i] : valueForNoneA) / (i < lenB ? (b[i] == 0 ? defaultValueForZero : b[i]) : valueForNoneB)
                        / (i < lenC ? (c[i] == 0 ? defaultValueForZero : c[i]) : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static double[][] dividedBy(final double[][] a, final double[][] b, final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static double[][] dividedBy(final double[][] a, final double[][] b, final double valueForNoneA, final double valueForNoneB,
            final double defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    private static double[][] dividedBy(final int len, final int rowLen, final double[][] a, final double[][] b, final double valueForNoneA,
            final double valueForNoneB, final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = dividedBy(rowLen, null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = dividedBy(rowLen, a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = dividedBy(rowLen, null, null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static double[][] dividedBy(final double[][] a, final double[][] b, final double[][] c, final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static double[][] dividedBy(final double[][] a, final double[][] b, final double[][] c, final double valueForNoneA, final double valueForNoneB,
            final double valueForNoneC, final double defaultValueForZero) {
        return dividedBy(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA,
                valueForNoneB, valueForNoneC, defaultValueForZero);
    }

    /**
     *
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    private static double[][] dividedBy(final int len, final int rowLen, final double[][] a, final double[][] b, final double[][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC, final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = dividedBy(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = dividedBy(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB,
                        valueForNoneC, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param defaultValueForZero
     * @return
     */
    public static double[][][] dividedBy(final double[][][] a, final double[][][] b, final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param defaultValueForZero
     * @return
     */
    public static double[][][] dividedBy(final double[][][] a, final double[][][] b, final double valueForNoneA, final double valueForNoneB,
            final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = dividedBy(null, b[i], valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = dividedBy(a[i], null, valueForNoneA, valueForNoneB, defaultValueForZero);
            }
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param defaultValueForZero
     * @return
     */
    public static double[][][] dividedBy(final double[][][] a, final double[][][] b, final double[][][] c, final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param defaultValueForZero
     * @return
     */
    public static double[][][] dividedBy(final double[][][] a, final double[][][] b, final double[][][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC, final double defaultValueForZero) {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = dividedBy(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, defaultValueForZero);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = dividedBy(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                    defaultValueForZero);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[] zip(final double[] a, final double[] b, final Throwables.DoubleBiFunction<Double, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[N.min(lenA, lenB)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[] zip(final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB,
            final Throwables.DoubleBiFunction<Double, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        return zip(N.max(lenA, lenB), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> double[] zip(final int len, final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB,
            final Throwables.DoubleBiFunction<Double, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i]);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zipFunction.apply(valueForNoneA, b[i]);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zipFunction.apply(a[i], valueForNoneB);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zipFunction.apply(valueForNoneA, valueForNoneB);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[] zip(final double[] a, final double[] b, final double[] c,
            final Throwables.DoubleTriFunction<Double, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[N.min(lenA, lenB, lenC)];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[] zip(final double[] a, final double[] b, final double[] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC, final Throwables.DoubleTriFunction<Double, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        return zip(N.max(lenA, lenB, lenC), a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> double[] zip(final int len, final double[] a, final double[] b, final double[] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC, final Throwables.DoubleTriFunction<Double, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[] result = new double[len];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zipFunction.apply(a[i], b[i], c[i]);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zipFunction.apply(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][] zip(final double[][] a, final double[][] b, final Throwables.DoubleBiFunction<Double, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[N.min(lenA, lenB)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][] zip(final double[][] a, final double[][] b, final double valueForNoneA, final double valueForNoneB,
            final Throwables.DoubleBiFunction<Double, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)), a, b, valueForNoneA, valueForNoneB, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> double[][] zip(final int len, final int rowLen, final double[][] a, final double[][] b, final double valueForNoneA,
            final double valueForNoneB, final Throwables.DoubleBiFunction<Double, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB && lenA < len) {
            for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                result[i] = zip(rowLen, null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA && lenB < len) {
            for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                result[i] = zip(rowLen, a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        if (N.max(lenA, lenB) < len) {
            for (int i = N.max(lenA, lenB); i < len; i++) {
                result[i] = zip(rowLen, null, null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][] zip(final double[][] a, final double[][] b, final double[][] c,
            final Throwables.DoubleTriFunction<Double, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[N.min(lenA, lenB, lenC)][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][] zip(final double[][] a, final double[][] b, final double[][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC, final Throwables.DoubleTriFunction<Double, E> zipFunction) throws E {
        return zip(N.max(N.len(a), N.len(b), N.len(c)), N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), a, b, c, valueForNoneA, valueForNoneB,
                valueForNoneC, zipFunction);
    }

    /**
     *
     * @param <E>
     * @param len
     * @param rowLen
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    private static <E extends Exception> double[][] zip(final int len, final int rowLen, final double[][] a, final double[][] b, final double[][] c,
            final double valueForNoneA, final double valueForNoneB, final double valueForNoneC, final Throwables.DoubleTriFunction<Double, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][] result = new double[len][];

        for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
            result[i] = zip(rowLen, a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (N.min(lenA, lenB, lenC) < len) {
            for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                result[i] = zip(rowLen, i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][][] zip(final double[][][] a, final double[][][] b, final Throwables.DoubleBiFunction<Double, E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.min(lenA, lenB)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param valueForNoneA
     * @param valueForNoneB
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][][] zip(final double[][][] a, final double[][][] b, final double valueForNoneA, final double valueForNoneB,
            final Throwables.DoubleBiFunction<Double, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);

        final double[][][] result = new double[N.max(lenA, lenB)][][];

        for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < lenB) {
            for (int i = lenA; i < lenB; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < lenA) {
            for (int i = lenB; i < lenA; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][][] zip(final double[][][] a, final double[][][] b, final double[][][] c,
            final Throwables.DoubleTriFunction<Double, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.min(lenA, lenB, lenC)][][];

        for (int i = 0, len = result.length; i < len; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     *
     * @param <E>
     * @param a
     * @param b
     * @param c
     * @param valueForNoneA
     * @param valueForNoneB
     * @param valueForNoneC
     * @param zipFunction
     * @return
     * @throws E the e
     */
    public static <E extends Exception> double[][][] zip(final double[][][] a, final double[][][] b, final double[][][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC, final Throwables.DoubleTriFunction<Double, E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);

        final double[][][] result = new double[N.max(lenA, lenB, lenC)][][];

        for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    public static long totalCountOfElements(final double[][] a) {
        long count = 0;

        for (final double[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    public static long totalCountOfElements(final double[][][] a) {
        long count = 0;

        for (final double[][] element : a) {
            if (N.isEmpty(element)) {
                continue;
            }

            for (final double[] doubles : element) {
                if (N.isEmpty(doubles)) {
                    continue;
                }

                count += doubles.length;
            }
        }

        return count;
    }

    /**
     * Min sub array len.
     *
     * @param a
     * @return
     */
    public static int minSubArrayLen(final double[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;

        for (final double[] ae : a) {
            minLen = N.min(minLen, ae == null ? 0 : ae.length);
        }

        return minLen;
    }

    /**
     * Max sub array len.
     *
     * @param a
     * @return
     */
    public static int maxSubArrayLen(final double[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int maxLen = 0;

        for (final double[] ae : a) {
            maxLen = N.max(maxLen, ae == null ? 0 : ae.length);
        }

        return maxLen;
    }

    /**
     *
     * @param a
     * @return
     */
    public static String println(final double[] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            return N.println(N.toString(a));
        }
    }

    /**
     *
     * @param a
     */
    public static String println(final double[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final double[] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(ai[j]);
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     */
    public static String println(final double[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final int len = a.length;
            final StringBuilder sb = Objectory.createStringBuilder();
            String str = null;

            try {
                sb.append('[');

                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                    }

                    if (a[i] == null) {
                        sb.append("null");
                    } else if (a[i].length == 0) {
                        sb.append("[]");
                    } else {
                        final double[][] ai = a[i];
                        sb.append('[');

                        for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR).append("  ");
                            }

                            if (ai[j] == null) {
                                sb.append("null");
                            } else if (ai[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final double[] aij = ai[j];
                                sb.append('[');

                                for (int k = 0, aijLen = aij.length; k < aijLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(aij[k]);
                                }

                                sb.append(']');
                            }
                        }

                        sb.append(']');
                    }
                }

                sb.append(']');
                str = sb.toString();
            } finally {
                Objectory.recycle(sb);
            }

            return N.println(str);
        }
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean[] toBoolean(final byte[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final boolean[] result = new boolean[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i] > 0;
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean[][] toBoolean(final byte[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final boolean[][] result = new boolean[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toBoolean(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean[][][] toBoolean(final byte[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final boolean[][][] result = new boolean[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toBoolean(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean[] toBoolean(final int[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final boolean[] result = new boolean[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i] > 0;
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean[][] toBoolean(final int[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final boolean[][] result = new boolean[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toBoolean(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean[][][] toBoolean(final int[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final boolean[][][] result = new boolean[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toBoolean(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static char[] toChar(final int[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final char[] result = new char[len];

        for (int i = 0; i < len; i++) {
            result[i] = (char) a[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static char[][] toChar(final int[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final char[][] result = new char[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toChar(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static char[][][] toChar(final int[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final char[][][] result = new char[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toChar(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static byte[] toByte(final boolean[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final byte[] result = new byte[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i] ? BYTE_1 : BYTE_0;
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static byte[][] toByte(final boolean[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final byte[][] result = new byte[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toByte(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static byte[][][] toByte(final boolean[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final byte[][][] result = new byte[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toByte(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static short[] toShort(final byte[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final short[] result = new short[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static short[][] toShort(final byte[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final short[][] result = new short[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toShort(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static short[][][] toShort(final byte[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final short[][][] result = new short[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toShort(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[] toInt(final boolean[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i] ? 1 : 0;
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][] toInt(final boolean[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][][] toInt(final boolean[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][][] result = new int[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[] toInt(final char[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][] toInt(final char[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][][] toInt(final char[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][][] result = new int[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[] toInt(final byte[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][] toInt(final byte[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][][] toInt(final byte[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][][] result = new int[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[] toInt(final short[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][] toInt(final short[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][][] toInt(final short[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][][] result = new int[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[] toInt(final float[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = (int) a[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][] toInt(final float[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][][] toInt(final float[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][][] result = new int[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[] toInt(final double[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = (int) a[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][] toInt(final double[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static int[][][] toInt(final double[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final int[][][] result = new int[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[] toLong(final byte[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[] result = new long[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[][] toLong(final byte[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][] result = new long[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[][][] toLong(final byte[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][][] result = new long[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[] toLong(final short[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[] result = new long[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[][] toLong(final short[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][] result = new long[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[][][] toLong(final short[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][][] result = new long[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[] toLong(final int[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[] result = new long[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[][] toLong(final int[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][] result = new long[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[][][] toLong(final int[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][][] result = new long[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[] toLong(final float[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[] result = new long[len];

        for (int i = 0; i < len; i++) {
            result[i] = (long) a[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[][] toLong(final float[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][] result = new long[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[][][] toLong(final float[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][][] result = new long[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[] toLong(final double[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[] result = new long[len];

        for (int i = 0; i < len; i++) {
            result[i] = (long) a[i];
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[][] toLong(final double[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][] result = new long[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static long[][][] toLong(final double[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final long[][][] result = new long[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toLong(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[] toFloat(final byte[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[] result = new float[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[][] toFloat(final byte[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[][] result = new float[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toFloat(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[][][] toFloat(final byte[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[][][] result = new float[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toFloat(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[] toFloat(final short[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[] result = new float[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[][] toFloat(final short[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[][] result = new float[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toFloat(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[][][] toFloat(final short[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[][][] result = new float[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toFloat(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[] toFloat(final int[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[] result = new float[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[][] toFloat(final int[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[][] result = new float[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toFloat(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[][][] toFloat(final int[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[][][] result = new float[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toFloat(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[] toFloat(final long[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[] result = new float[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[][] toFloat(final long[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[][] result = new float[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toFloat(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static float[][][] toFloat(final long[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final float[][][] result = new float[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toFloat(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[] toDouble(final byte[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[] result = new double[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[][] toDouble(final byte[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][] result = new double[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toDouble(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[][][] toDouble(final byte[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][][] result = new double[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toDouble(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[] toDouble(final short[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[] result = new double[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[][] toDouble(final short[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][] result = new double[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toDouble(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[][][] toDouble(final short[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][][] result = new double[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toDouble(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[] toDouble(final int[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[] result = new double[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[][] toDouble(final int[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][] result = new double[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toDouble(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[][][] toDouble(final int[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][][] result = new double[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toDouble(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[] toDouble(final long[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[] result = new double[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[][] toDouble(final long[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][] result = new double[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toDouble(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[][][] toDouble(final long[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][][] result = new double[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toDouble(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[] toDouble(final float[] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[] result = new double[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[][] toDouble(final float[][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][] result = new double[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toDouble(a[i]);
        }

        return result;
    }

    /**
     *
     * @param a
     * @return
     */
    public static double[][][] toDouble(final float[][][] a) {
        if (a == null) {
            return null; // NOSONAR
        }

        final int len = N.len(a);
        final double[][][] result = new double[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = toDouble(a[i]);
        }

        return result;
    }

    private static void checkMForReshape(final int m) { // NOSONAR
        N.checkArgument(m > 0, "'m' must be positive number: m = %s", m);
    }

    private static void checkMAndLForReshape(final int m, final int l) {
        N.checkArgument(m > 0 && l > 0, "'m'  and 'l' must be positive number: m = %s, l = %s", m, l);
    }

    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
    public static final class f extends Arrays { // NOSONAR
        private f() {
            // utility class
        }
    }

    /**
     * The Class ff.
     */
    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
    public static final class ff { // NOSONAR

        /**
         * Instantiates a new ff.
         */
        private ff() {
            // Singleton
        }

        /**
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param operator
         * @throws E the e
         */
        public static <T, E extends Exception> void updateAll(final T[][] a, final Throwables.UnaryOperator<T, E> operator) throws E {
            if (N.isEmpty(a)) {
                return;
            }

            for (final T[] element : a) {
                if (N.isEmpty(element)) {
                    continue;
                }

                for (int j = 0, m = element.length; j < m; j++) {
                    element[j] = operator.apply(element[j]);
                }
            }
        }

        /**
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param predicate
         * @param newValue
         * @throws E the e
         */
        public static <T, E extends Exception> void replaceIf(final T[][] a, final Throwables.Predicate<? super T, E> predicate, final T newValue) throws E {
            if (N.isEmpty(a)) {
                return;
            }

            for (final T[] element : a) {
                if (N.isEmpty(element)) {
                    continue;
                }

                for (int j = 0, m = element.length; j < m; j++) {
                    if (predicate.test(element[j])) {
                        element[j] = newValue;
                    }
                }
            }
        }

        /**
         * Reshapes a one-dimensional array into a two-dimensional array with the specified number of columns.
         *
         * @param <T> the type of the elements in the array
         * @param a the one-dimensional array to be reshaped
         * @param cols the number of columns for the resulting two-dimensional array
         * @return a two-dimensional array with the specified number of columns
         * @throws IllegalArgumentException if the number of columns is invalid
         */
        public static <T> T[][] reshape(final T[] a, final int cols) throws IllegalArgumentException {
            checkMForReshape(cols);

            //        if (N.isEmpty(a)) {
            //            return new T[0][0];
            //        }

            final int len = a.length;
            final int n = Numbers.divide(len, cols, RoundingMode.CEILING);
            final T[][] c = N.newArray(a.getClass(), n);

            for (int i = 0, from = 0; i < n; i++, from += cols) {
                c[i] = N.copyOfRange(a, from, from + N.min(len - from, cols));
            }

            return c;
        }

        /**
         * Flattens a two-dimensional array into a one-dimensional array.
         *
         * @param <T> the type of the elements in the array
         * @param a the two-dimensional array to be flattened
         * @return a one-dimensional array containing all the elements of the input array
         */
        public static <T> T[] flatten(final T[][] a) {
            final int count = Numbers.toIntExact(totalCountOfElements(a));

            final T[] c = N.newArray(a.getClass().getComponentType().getComponentType(), count);
            int from = 0;

            for (final T[] element : a) {
                if (N.isEmpty(element)) {
                    continue;
                }

                N.copy(element, 0, c, from, element.length);

                from += element.length;
            }

            return c;
        }

        /**
         * flatten -> execute {@code op} -> set values back.
         * <pre>
         * <code>
         * f.flattOp(a, t -> N.sort(t));
         * </code>
         * </pre>
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param op
         * @throws E the e
         */
        public static <T, E extends Exception> void flatOp(final T[][] a, final Throwables.Consumer<? super T[], E> op) throws E {
            if (N.isEmpty(a)) {
                return;
            }

            final T[] tmp = flatten(a);

            op.accept(tmp);

            int idx = 0;

            for (final T[] e : a) {
                if (N.notEmpty(e)) {
                    N.copy(tmp, idx, e, 0, e.length);
                    idx += e.length;
                }
            }
        }

        /**
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> T[][] map(final T[][] a, final Throwables.UnaryOperator<T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            return map(a, func, (Class<T>) a.getClass().getComponentType().getComponentType());
        }

        /**
         *
         *
         * @param <T>
         * @param <R>
         * @param <E>
         * @param a
         * @param func
         * @param targetElementType
         * @return
         * @throws E the e
         */
        public static <T, R, E extends Exception> R[][] map(final T[][] a, final Throwables.Function<? super T, ? extends R, E> func,
                final Class<R> targetElementType) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final R[][] c = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

            for (int i = 0; i < len; i++) {
                c[i] = Arrays.map(a[i], func, targetElementType);
            }

            return c;
        }

        /**
         * Map to boolean.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> boolean[][] mapToBoolean(final T[][] a, final Throwables.ToBooleanFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final boolean[][] c = new boolean[len][];

            for (int i = 0; i < len; i++) {
                c[i] = Arrays.mapToBoolean(a[i], func);
            }

            return c;
        }

        /**
         * Map to char.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> char[][] mapToChar(final T[][] a, final Throwables.ToCharFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final char[][] c = new char[len][];

            for (int i = 0; i < len; i++) {
                c[i] = Arrays.mapToChar(a[i], func);
            }

            return c;
        }

        /**
         * Map to byte.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> byte[][] mapToByte(final T[][] a, final Throwables.ToByteFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final byte[][] c = new byte[len][];

            for (int i = 0; i < len; i++) {
                c[i] = Arrays.mapToByte(a[i], func);
            }

            return c;
        }

        /**
         * Map to short.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> short[][] mapToShort(final T[][] a, final Throwables.ToShortFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final short[][] c = new short[len][];

            for (int i = 0; i < len; i++) {
                c[i] = Arrays.mapToShort(a[i], func);
            }

            return c;
        }

        /**
         * Map to int.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> int[][] mapToInt(final T[][] a, final Throwables.ToIntFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final int[][] c = new int[len][];

            for (int i = 0; i < len; i++) {
                c[i] = Arrays.mapToInt(a[i], func);
            }

            return c;
        }

        /**
         * Map to long.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> long[][] mapToLong(final T[][] a, final Throwables.ToLongFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final long[][] c = new long[len][];

            for (int i = 0; i < len; i++) {
                c[i] = Arrays.mapToLong(a[i], func);
            }

            return c;
        }

        /**
         * Map to float.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> float[][] mapToFloat(final T[][] a, final Throwables.ToFloatFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final float[][] c = new float[len][];

            for (int i = 0; i < len; i++) {
                c[i] = Arrays.mapToFloat(a[i], func);
            }

            return c;
        }

        /**
         * Map to double.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> double[][] mapToDouble(final T[][] a, final Throwables.ToDoubleFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final double[][] c = new double[len][];

            for (int i = 0; i < len; i++) {
                c[i] = Arrays.mapToDouble(a[i], func);
            }

            return c;
        }

        /**
         *
         * @param <A>
         * @param <B>
         * @param <R>
         * @param <E>
         * @param len
         * @param a
         * @param b
         * @param valueForNoneA
         * @param valueForNoneB
         * @param zipFunction
         * @param targetElementType
         * @return
         * @throws E the e
         */
        private static <A, B, R, E extends Exception> R[] zip(final int len, final A[] a, final B[] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, ? extends R, E> zipFunction, final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);

            final R[] result = N.newArray(targetElementType, len);

            for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
                result[i] = zipFunction.apply(a[i], b[i]);
            }

            if (lenA < lenB && lenA < len) {
                for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                    result[i] = zipFunction.apply(valueForNoneA, b[i]);
                }
            } else if (lenB < lenA && lenB < len) {
                for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                    result[i] = zipFunction.apply(a[i], valueForNoneB);
                }
            }

            if (N.max(lenA, lenB) < len) {
                for (int i = N.max(lenA, lenB); i < len; i++) {
                    result[i] = zipFunction.apply(valueForNoneA, valueForNoneB);
                }
            }

            return result;
        }

        /**
         *
         * @param a
         * @param b
         * @param c
         * @param valueForNoneA
         * @param valueForNoneB
         * @param valueForNoneC
         * @param zipFunction
         * @param len
         * @param targetElementType
         * @param <A>
         * @param <B>
         * @param <C>
         * @param <R>
         * @param <E>
         * @return
         * @throws E the e
         */
        private static <A, B, C, R, E extends Exception> R[] zip(final A[] a, final B[] b, final C[] c, final A valueForNoneA, final B valueForNoneB,
                final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, ? extends R, E> zipFunction, final int len,
                final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);
            final int lenC = N.len(c);

            final R[] result = N.newArray(targetElementType, len);

            for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
                result[i] = zipFunction.apply(a[i], b[i], c[i]);
            }

            if (N.min(lenA, lenB, lenC) < len) {
                for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                    result[i] = zipFunction.apply(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
                }
            }

            return result;
        }

        /**
         *
         * @param <A>
         * @param <B>
         * @param <E>
         * @param a
         * @param b
         * @param zipFunction
         * @return
         * @throws E the e
         */
        public static <A, B, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction)
                throws E {
            return zip(a, b, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
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
         * @throws E the e
         */
        public static <A, B, R, E extends Exception> R[][] zip(final A[][] a, final B[][] b,
                final Throwables.BiFunction<? super A, ? super B, ? extends R, E> zipFunction, final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);

            final R[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), N.min(lenA, lenB));

            for (int i = 0, len = result.length; i < len; i++) {
                final A[] aa = a[i];
                final B[] bb = b[i];
                final int minLen = N.min(N.len(aa), N.len(bb));
                final R[] ret = N.newArray(targetElementType, minLen);

                for (int j = 0; j < minLen; j++) {
                    ret[j] = zipFunction.apply(aa[j], bb[j]);
                }

                result[i] = ret;
            }

            return result;
        }

        /**
         *
         * @param <A>
         * @param <B>
         * @param <E>
         * @param a
         * @param b
         * @param valueForNoneA
         * @param valueForNoneB
         * @param zipFunction
         * @return
         * @throws E the e
         */
        public static <A, B, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
            return zip(a, b, valueForNoneA, valueForNoneB, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
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
         * @param valueForNoneA
         * @param valueForNoneB
         * @param zipFunction
         * @param targetElementType
         * @return
         * @throws E the e
         */
        public static <A, B, R, E extends Exception> R[][] zip(final A[][] a, final B[][] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, ? extends R, E> zipFunction, final Class<R> targetElementType) throws E {
            return zip(a, b, valueForNoneA, valueForNoneB, zipFunction, N.max(N.len(a), N.len(b)), N.max(maxSubArrayLen(a), maxSubArrayLen(b)),
                    targetElementType);
        }

        /**
         *
         * @param a
         * @param b
         * @param valueForNoneA
         * @param valueForNoneB
         * @param zipFunction
         * @param len
         * @param rowLen
         * @param targetElementType
         * @param <A>
         * @param <B>
         * @param <R>
         * @param <E>
         * @return
         * @throws E the e
         */
        private static <A, B, R, E extends Exception> R[][] zip(final A[][] a, final B[][] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, ? extends R, E> zipFunction, final int len, final int rowLen,
                final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);

            final R[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

            for (int i = 0, min = N.min(lenA, lenB, len); i < min; i++) {
                result[i] = zip(rowLen, a[i], b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
            }

            if (lenA < lenB && lenA < len) {
                for (int i = lenA, min = N.min(lenB, len); i < min; i++) {
                    result[i] = zip(rowLen, null, b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
                }
            } else if (lenB < lenA && lenB < len) {
                for (int i = lenB, min = N.min(lenA, len); i < min; i++) {
                    result[i] = zip(rowLen, a[i], null, valueForNoneA, valueForNoneB, zipFunction, targetElementType);
                }
            }

            if (N.max(lenA, lenB) < len) {
                for (int i = N.max(lenA, lenB); i < len; i++) {
                    result[i] = zip(rowLen, null, null, valueForNoneA, valueForNoneB, zipFunction, targetElementType);
                }
            }

            return result;
        }

        /**
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
         * @throws E the e
         */
        public static <A, B, C, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final C[][] c,
                final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
            return zip(a, b, c, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
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
         * @throws E the e
         */
        public static <A, B, C, R, E extends Exception> R[][] zip(final A[][] a, final B[][] b, final C[][] c,
                final Throwables.TriFunction<? super A, ? super B, ? super C, ? extends R, E> zipFunction, final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);
            final int lenC = N.len(c);

            final R[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), N.min(lenA, lenB, lenC));

            for (int i = 0, len = result.length; i < len; i++) {
                final A[] aa = a[i];
                final B[] bb = b[i];
                final C[] cc = c[i];
                final int minLen = N.min(N.len(aa), N.len(bb), N.len(cc));
                final R[] ret = N.newArray(targetElementType, minLen);

                for (int j = 0; j < minLen; j++) {
                    ret[j] = zipFunction.apply(aa[j], bb[j], cc[j]);
                }

                result[i] = ret;
            }

            return result;
        }

        /**
         *
         * @param <A>
         * @param <B>
         * @param <C>
         * @param <E>
         * @param a
         * @param b
         * @param c
         * @param valueForNoneA
         * @param valueForNoneB
         * @param valueForNoneC
         * @param zipFunction
         * @return
         * @throws E the e
         */
        public static <A, B, C, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final C[][] c, final A valueForNoneA, final B valueForNoneB,
                final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
            return zip(a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
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
         * @param valueForNoneA
         * @param valueForNoneB
         * @param valueForNoneC
         * @param zipFunction
         * @param targetElementType
         * @return
         * @throws E the e
         */
        public static <A, B, C, R, E extends Exception> R[][] zip(final A[][] a, final B[][] b, final C[][] c, final A valueForNoneA, final B valueForNoneB,
                final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, ? extends R, E> zipFunction,
                final Class<R> targetElementType) throws E {
            return zip(a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction, N.max(N.len(a), N.len(b), N.len(c)),
                    N.max(maxSubArrayLen(a), maxSubArrayLen(b), maxSubArrayLen(c)), targetElementType);
        }

        /**
         *
         * @param a
         * @param b
         * @param c
         * @param valueForNoneA
         * @param valueForNoneB
         * @param valueForNoneC
         * @param zipFunction
         * @param len
         * @param rowLen
         * @param targetElementType
         * @param <A>
         * @param <B>
         * @param <C>
         * @param <R>
         * @param <E>
         * @return
         * @throws E the e
         */
        private static <A, B, C, R, E extends Exception> R[][] zip(final A[][] a, final B[][] b, final C[][] c, final A valueForNoneA, final B valueForNoneB,
                final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, ? extends R, E> zipFunction, final int len,
                final int rowLen, final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);
            final int lenC = N.len(c);

            final R[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

            for (int i = 0, min = N.min(lenA, lenB, lenC, len); i < min; i++) {
                result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction, rowLen, targetElementType);
            }

            if (N.min(lenA, lenB, lenC) < len) {
                for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                    result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                            zipFunction, rowLen, targetElementType);
                }
            }

            return result;
        }

        /**
         * Calculates the total count of elements in a two-dimensional array.
         *
         * @param a the two-dimensional array whose elements are to be counted
         * @return the total count of elements in the array, or 0 if the array is null
         */
        public static long totalCountOfElements(final Object[][] a) {
            long count = 0;

            for (final Object[] element : a) {
                count += (element == null ? 0 : element.length);
            }

            return count;
        }

        /**
         * Finds the minimum length of sub-arrays in a two-dimensional array.
         *
         * @param a the two-dimensional array to be checked
         * @return the minimum length of sub-arrays, or 0 if the array is null
         */
        public static int minSubArrayLen(final Object[][] a) {
            if (N.isEmpty(a)) {
                return 0;
            }

            int minLen = Integer.MAX_VALUE;

            for (final Object[] ae : a) {
                minLen = N.min(minLen, ae == null ? 0 : ae.length);
            }

            return minLen;
        }

        /**
         * Finds the maximum length of sub-arrays in a two-dimensional array.
         *
         * @param a the two-dimensional array to be checked
         * @return the maximum length of sub-arrays, or 0 if the array is null
         */
        public static int maxSubArrayLen(final Object[][] a) {
            if (N.isEmpty(a)) {
                return 0;
            }

            int maxLen = 0;

            for (final Object[] ae : a) {
                maxLen = N.max(maxLen, ae == null ? 0 : ae.length);
            }

            return maxLen;
        }

        /**
         *
         * @param a
         */
        static String println(final Object[][] a) {
            if (a == null) {
                return N.println("null");
            } else if (a.length == 0) {
                return N.println("[]");
            } else {
                final int len = a.length;
                final StringBuilder sb = Objectory.createStringBuilder();
                String str = null;

                try {
                    sb.append('[');

                    for (int i = 0; i < len; i++) {
                        if (i > 0) {
                            sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                        }

                        if (a[i] == null) {
                            sb.append("null");
                        } else if (a[i].length == 0) {
                            sb.append("[]");
                        } else {
                            final Object[] ai = a[i];
                            sb.append('[');

                            for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                                if (j > 0) {
                                    sb.append(", ");
                                }

                                sb.append(ai[j]);
                            }

                            sb.append(']');
                        }
                    }

                    sb.append(']');
                    str = sb.toString();
                } finally {
                    Objectory.recycle(sb);
                }

                return N.println(str);
            }
        }
    }

    /**
     * The Class fff.
     */
    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
    public static final class fff { // NOSONAR

        /**
         * Instantiates a new fff.
         */
        private fff() {
            // Singleton
        }

        /**
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param operator
         * @throws E the e
         */
        public static <T, E extends Exception> void updateAll(final T[][][] a, final Throwables.UnaryOperator<T, E> operator) throws E {
            if (N.isEmpty(a)) {
                return;
            }

            for (final T[][] element : a) {
                ff.updateAll(element, operator);
            }
        }

        /**
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param predicate
         * @param newValue
         * @throws E the e
         */
        public static <T, E extends Exception> void replaceIf(final T[][][] a, final Throwables.Predicate<? super T, E> predicate, final T newValue) throws E {
            if (N.isEmpty(a)) {
                return;
            }

            for (final T[][] element : a) {
                ff.replaceIf(element, predicate, newValue);
            }
        }

        /**
         * Reshapes a one-dimensional array into a three-dimensional array with the specified number of rows and columns.
         *
         * @param <T> the type of the elements in the array
         * @param a the one-dimensional array to be reshaped
         * @param rows the number of rows for the resulting three-dimensional array
         * @param cols the number of columns for the resulting three-dimensional array
         * @return a three-dimensional array with the specified number of rows and columns
         * @throws IllegalArgumentException if the number of rows or columns is invalid
         */
        public static <T> T[][][] reshape(final T[] a, final int rows, final int cols) throws IllegalArgumentException {
            N.checkArgNotNull(a, "a");
            N.checkArgument(rows > 0 && cols > 0, "'m'  and 'l' must be positive number: m = %s, l = %s", rows, cols);

            //        if (N.isEmpty(a)) {
            //            return new T[0][0][0];
            //        }

            final int len = a.length;
            final int n = Numbers.divide(len, rows * cols, RoundingMode.CEILING);
            final T[][][] c = N.newArray(N.newArray(a.getClass(), 0).getClass(), n);

            for (int i = 0, from = 0; i < n; i++) {
                c[i] = N.newArray(a.getClass(), N.min(rows, Numbers.divide(len - from, cols, RoundingMode.CEILING)));

                for (int j = 0, y = c[i].length; j < y; j++, from += cols) {
                    c[i][j] = N.copyOfRange(a, from, from + N.min(len - from, cols));
                }
            }

            return c;
        }

        /**
         * Flattens a three-dimensional array into a one-dimensional array.
         *
         * @param <T> the type of the elements in the array
         * @param a the three-dimensional array to be flattened
         * @return a one-dimensional array containing all the elements of the input array
         */
        public static <T> T[] flatten(final T[][][] a) {
            final int count = Numbers.toIntExact(totalCountOfElements(a));

            final T[] c = N.newArray(a.getClass().getComponentType().getComponentType().getComponentType(), count);
            int from = 0;

            for (final T[][] element : a) {
                if (N.isEmpty(element)) {
                    continue;
                }

                for (final T[] ts : element) {
                    if (N.isEmpty(ts)) {
                        continue;
                    }

                    N.copy(ts, 0, c, from, ts.length);

                    from += ts.length;
                }
            }

            return c;
        }

        /**
         * flatten -> execute {@code op} -> set values back.
         * <pre>
         * <code>
         * f.flattOp(a, t -> N.sort(t));
         * </code>
         * </pre>
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param op
         * @throws E the e
         */
        public static <T, E extends Exception> void flatOp(final T[][][] a, final Throwables.Consumer<? super T[], E> op) throws E {
            if (N.isEmpty(a)) {
                return;
            }

            final T[] tmp = flatten(a);

            op.accept(tmp);

            int idx = 0;

            for (final T[][] e : a) {
                if (N.notEmpty(e)) {
                    for (final T[] ee : e) {
                        if (N.notEmpty(ee)) {
                            N.copy(tmp, idx, ee, 0, ee.length);
                            idx += ee.length;
                        }
                    }
                }
            }
        }

        /**
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> T[][][] map(final T[][][] a, final Throwables.UnaryOperator<T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            return map(a, func, (Class<T>) a.getClass().getComponentType().getComponentType().getComponentType());
        }

        /**
         *
         *
         * @param <T>
         * @param <R>
         * @param <E>
         * @param a
         * @param func
         * @param targetElementType
         * @return
         * @throws E the e
         */
        public static <T, R, E extends Exception> R[][][] map(final T[][][] a, final Throwables.Function<? super T, ? extends R, E> func,
                final Class<R> targetElementType) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final R[][][] c = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

            for (int i = 0; i < len; i++) {
                c[i] = ff.map(a[i], func, targetElementType);
            }

            return c;
        }

        /**
         * Map to boolean.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> boolean[][][] mapToBoolean(final T[][][] a, final Throwables.ToBooleanFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final boolean[][][] c = new boolean[len][][];

            for (int i = 0; i < len; i++) {
                c[i] = ff.mapToBoolean(a[i], func);
            }

            return c;
        }

        /**
         * Map to char.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> char[][][] mapToChar(final T[][][] a, final Throwables.ToCharFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final char[][][] c = new char[len][][];

            for (int i = 0; i < len; i++) {
                c[i] = ff.mapToChar(a[i], func);
            }

            return c;
        }

        /**
         * Map to byte.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> byte[][][] mapToByte(final T[][][] a, final Throwables.ToByteFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final byte[][][] c = new byte[len][][];

            for (int i = 0; i < len; i++) {
                c[i] = ff.mapToByte(a[i], func);
            }

            return c;
        }

        /**
         * Map to short.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> short[][][] mapToShort(final T[][][] a, final Throwables.ToShortFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final short[][][] c = new short[len][][];

            for (int i = 0; i < len; i++) {
                c[i] = ff.mapToShort(a[i], func);
            }

            return c;
        }

        /**
         * Map to int.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> int[][][] mapToInt(final T[][][] a, final Throwables.ToIntFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final int[][][] c = new int[len][][];

            for (int i = 0; i < len; i++) {
                c[i] = ff.mapToInt(a[i], func);
            }

            return c;
        }

        /**
         * Map to long.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> long[][][] mapToLong(final T[][][] a, final Throwables.ToLongFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final long[][][] c = new long[len][][];

            for (int i = 0; i < len; i++) {
                c[i] = ff.mapToLong(a[i], func);
            }

            return c;
        }

        /**
         * Map to float.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> float[][][] mapToFloat(final T[][][] a, final Throwables.ToFloatFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final float[][][] c = new float[len][][];

            for (int i = 0; i < len; i++) {
                c[i] = ff.mapToFloat(a[i], func);
            }

            return c;
        }

        /**
         * Map to double.
         *
         * @param <T>
         * @param <E>
         * @param a
         * @param func
         * @return
         * @throws E the e
         */
        public static <T, E extends Exception> double[][][] mapToDouble(final T[][][] a, final Throwables.ToDoubleFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final double[][][] c = new double[len][][];

            for (int i = 0; i < len; i++) {
                c[i] = ff.mapToDouble(a[i], func);
            }

            return c;
        }

        /**
         *
         * @param <A>
         * @param <B>
         * @param <E>
         * @param a
         * @param b
         * @param zipFunction
         * @return
         * @throws E the e
         */
        public static <A, B, E extends Exception> A[][][] zip(final A[][][] a, final B[][][] b,
                final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
            return zip(a, b, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType().getComponentType());
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
         * @throws E the e
         */
        public static <A, B, R, E extends Exception> R[][][] zip(final A[][][] a, final B[][][] b,
                final Throwables.BiFunction<? super A, ? super B, ? extends R, E> zipFunction, final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);

            final R[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), N.min(lenA, lenB));

            for (int i = 0, len = result.length; i < len; i++) {
                result[i] = ff.zip(a[i], b[i], zipFunction, targetElementType);
            }

            return result;
        }

        /**
         *
         * @param <A>
         * @param <B>
         * @param <E>
         * @param a
         * @param b
         * @param valueForNoneA
         * @param valueForNoneB
         * @param zipFunction
         * @return
         * @throws E the e
         */
        public static <A, B, E extends Exception> A[][][] zip(final A[][][] a, final B[][][] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
            return zip(a, b, valueForNoneA, valueForNoneB, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType().getComponentType());
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
         * @param valueForNoneA
         * @param valueForNoneB
         * @param zipFunction
         * @param targetElementType
         * @return
         * @throws E the e
         */
        public static <A, B, R, E extends Exception> R[][][] zip(final A[][][] a, final B[][][] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, ? extends R, E> zipFunction, final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);

            final R[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), N.max(lenA, lenB));

            for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
                result[i] = ff.zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
            }

            if (lenA < lenB) {
                for (int i = lenA; i < lenB; i++) {
                    result[i] = ff.zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
                }
            } else if (lenB < lenA) {
                for (int i = lenB; i < lenA; i++) {
                    result[i] = ff.zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction, targetElementType);
                }
            }

            return result;
        }

        /**
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
         * @throws E the e
         */
        public static <A, B, C, E extends Exception> A[][][] zip(final A[][][] a, final B[][][] b, final C[][][] c,
                final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
            return zip(a, b, c, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType().getComponentType());
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
         * @throws E the e
         */
        public static <A, B, C, R, E extends Exception> R[][][] zip(final A[][][] a, final B[][][] b, final C[][][] c,
                final Throwables.TriFunction<? super A, ? super B, ? super C, ? extends R, E> zipFunction, final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);
            final int lenC = N.len(c);

            final R[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), N.min(lenA, lenB, lenC));

            for (int i = 0, len = result.length; i < len; i++) {
                result[i] = ff.zip(a[i], b[i], c[i], zipFunction, targetElementType);
            }

            return result;
        }

        /**
         *
         * @param <A>
         * @param <B>
         * @param <C>
         * @param <E>
         * @param a
         * @param b
         * @param c
         * @param valueForNoneA
         * @param valueForNoneB
         * @param valueForNoneC
         * @param zipFunction
         * @return
         * @throws E the e
         */
        public static <A, B, C, E extends Exception> A[][][] zip(final A[][][] a, final B[][][] b, final C[][][] c, final A valueForNoneA,
                final B valueForNoneB, final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
            return zip(a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction,
                    (Class<A>) a.getClass().getComponentType().getComponentType().getComponentType());
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
         * @param valueForNoneA
         * @param valueForNoneB
         * @param valueForNoneC
         * @param zipFunction
         * @param targetElementType
         * @return
         * @throws E the e
         */
        public static <A, B, C, R, E extends Exception> R[][][] zip(final A[][][] a, final B[][][] b, final C[][][] c, final A valueForNoneA,
                final B valueForNoneB, final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, ? extends R, E> zipFunction,
                final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);
            final int lenC = N.len(c);

            final R[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), N.max(lenA, lenB, lenC));

            for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
                result[i] = ff.zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction, targetElementType);
            }

            for (int i = N.min(lenA, lenB, lenC), len = result.length; i < len; i++) {
                result[i] = ff.zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction, targetElementType);
            }

            return result;
        }

        /**
         * Calculates the total count of elements in a three-dimensional array.
         *
         * @param a the three-dimensional array whose elements are to be counted
         * @return the total count of elements in the array, or 0 if the array is null
         */
        public static long totalCountOfElements(final Object[][][] a) {
            long count = 0;

            for (final Object[][] element : a) {
                if (N.isEmpty(element)) {
                    continue;
                }

                for (final Object[] ts : element) {
                    if (N.isEmpty(ts)) {
                        continue;
                    }

                    count += ts.length;
                }
            }

            return count;
        }

        //    /**
        //     * Finds the minimum length of sub-sub-arrays in a three-dimensional array.
        //     *
        //     * @param a the three-dimensional array to be checked
        //     * @return the minimum length of sub-sub-arrays, or 0 if the array is null
        //     */
        //    public static int minSubArrayLen(final Object[][][] a) {
        //        if (N.isEmpty(a)) {
        //            return 0;
        //        }
        //
        //        int minLen = Integer.MAX_VALUE;
        //
        //        for (final Object[][] ae : a) {
        //            minLen = N.min(minLen, ff.minSubArrayLen(ae));
        //        }
        //
        //        return minLen;
        //    }
        //
        //    /**
        //     * Finds the maximum length of sub-sub-arrays in a three-dimensional array.
        //     *
        //     * @param a the three-dimensional array to be checked
        //     * @return the maximum length of sub-sub-arrays, or 0 if the array is null
        //     */
        //    public static int maxSubArrayLen(final Object[][][] a) {
        //        if (N.isEmpty(a)) {
        //            return 0;
        //        }
        //
        //        int maxLen = 0;
        //
        //        for (final Object[][] ae : a) {
        //            maxLen = N.max(maxLen, ff.maxSubArrayLen(ae));
        //        }
        //
        //        return maxLen;
        //    }

        /**
         *
         * @param a
         * @return
         */
        static String println(final Object[][][] a) {
            if (a == null) {
                return N.println("null");
            } else if (a.length == 0) {
                return N.println("[]");
            } else {
                final int len = a.length;
                final StringBuilder sb = Objectory.createStringBuilder();
                String str = null;

                try {
                    sb.append('[');

                    for (int i = 0; i < len; i++) {
                        if (i > 0) {
                            sb.append(',').append(ARRAY_PRINT_SEPARATOR).append(' ');
                        }

                        if (a[i] == null) {
                            sb.append("null");
                        } else if (a[i].length == 0) {
                            sb.append("[]");
                        } else {
                            final Object[][] ai = a[i];
                            sb.append('[');

                            for (int j = 0, aiLen = ai.length; j < aiLen; j++) {
                                if (j > 0) {
                                    sb.append(',').append(IOUtil.LINE_SEPARATOR).append("  ");
                                }

                                if (ai[j] == null) {
                                    sb.append("null");
                                } else if (ai[j].length == 0) {
                                    sb.append("[]");
                                } else {
                                    final Object[] aij = ai[j];
                                    sb.append('[');

                                    for (int k = 0, aijLen = aij.length; k < aijLen; k++) {
                                        if (k > 0) {
                                            sb.append(", ");
                                        }

                                        sb.append(aij[k]);
                                    }

                                    sb.append(']');
                                }
                            }

                            sb.append(']');
                        }
                    }

                    sb.append(']');
                    str = sb.toString();
                } finally {
                    Objectory.recycle(sb);
                }

                N.println(str);

                return str;
            }
        }
    }
}
