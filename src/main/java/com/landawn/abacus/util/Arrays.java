/*
 * Copyright (C) 2017 HaiYang Li
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

import java.math.RoundingMode;

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.annotation.SuppressFBWarnings;

/**
 * A comprehensive utility class providing extensive methods for array manipulation and operations.
 * This class offers a wide range of functionality for working with both single and multi-dimensional
 * arrays of all primitive types and objects.
 * 
 * <p>The class contains various utility methods for array operations including:</p>
 * <ul>
 *   <li>Mathematical operations (add, subtract, multiply, divide) for element-wise calculations</li>
 *   <li>Array transformation (reshape, flatten) for changing array dimensions</li>
 *   <li>Type conversion between different array types (e.g., int[] to double[])</li>
 *   <li>Element-wise operations with customizable functions</li>
 *   <li>Array printing utilities for debugging and visualization</li>
 *   <li>Statistical operations (sum, average, min, max)</li>
 *   <li>Array manipulation (reverse, rotate, shuffle)</li>
 * </ul>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * // Element-wise addition
 * int[] a = {1, 2, 3};
 * int[] b = {4, 5, 6};
 * int[] sum = Arrays.add(a, b); // {5, 7, 9}
 * 
 * // Array reshaping
 * int[] flat = {1, 2, 3, 4, 5, 6};
 * int[][] matrix = Arrays.reshape(flat, 2, 3); // {{1, 2, 3}, {4, 5, 6}}
 * 
 * // Type conversion
 * int[] ints = {1, 2, 3};
 * double[] doubles = Arrays.toDoubleArray(ints); // {1.0, 2.0, 3.0}
 * }</pre>
 * 
 * @implSpec classes/interfaces implemented in abacus-extra are not for daily use case.
 * It's not going to handle {@code null} scenarios like what handled in abacus-common.
 * It's developer's responsibility to handle {@code null} scenarios.
 * 
 * @see Matrix
 * @see Matrixes
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
     * Prints a 1D object array to the console and returns the printed string.
     * Handles null arrays and empty arrays appropriately.
     *
     * <p>Example usage:
     * <pre>{@code
     * Object[] arr = {"Hello", "World", 123};
     * String output = println(arr);
     * // Prints and returns: [Hello, World, 123]
     * }</pre>
     *
     * @param a the object array to print
     * @return the string representation that was printed
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
     * Prints a 2D object array to the console with proper formatting and returns the printed string.
     * Uses the internal formatter for 2D arrays.
     *
     * @param a the 2D object array to print
     * @return the string representation that was printed
     */
    public static String println(final Object[][] a) {
        return ff.println(a);
    }

    /**
     * Prints a 3D object array to the console with proper formatting and returns the printed string.
     * Uses the internal formatter for 3D arrays.
     *
     * @param a the 3D object array to print
     * @return the string representation that was printed
     */
    public static String println(final Object[][][] a) {
        return fff.println(a);
    }

    /**
     * Maps each boolean element to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the boolean array to map
     * @param mapper the function that maps each boolean to an object
     * @param targetElementType the class of the target element type
     * @return an object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
     *
     * @example
     * <pre>
     * boolean[] flags = {true, false, true};
     * String[] strings = Arrays.mapToObj(flags, b -> b ? "YES" : "NO", String.class);
     * // Result: ["YES", "NO", "YES"]
     * </pre>
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
     * Maps each boolean element in a 2D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 2D boolean array to map
     * @param mapper the function that maps each boolean to an object
     * @param targetElementType the class of the target element type
     * @return a 2D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each boolean element in a 3D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 3D boolean array to map
     * @param mapper the function that maps each boolean to an object
     * @param targetElementType the class of the target element type
     * @return a 3D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each char element to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the char array to map
     * @param mapper the function that maps each char to an object
     * @param targetElementType the class of the target element type
     * @return an object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each char element in a 2D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 2D char array to map
     * @param mapper the function that maps each char to an object
     * @param targetElementType the class of the target element type
     * @return a 2D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each char element in a 3D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 3D char array to map
     * @param mapper the function that maps each char to an object
     * @param targetElementType the class of the target element type
     * @return a 3D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each byte element to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the byte array to map
     * @param mapper the function that maps each byte to an object
     * @param targetElementType the class of the target element type
     * @return an object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each byte element in a 2D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 2D byte array to map
     * @param mapper the function that maps each byte to an object
     * @param targetElementType the class of the target element type
     * @return a 2D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each byte element in a 3D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 3D byte array to map
     * @param mapper the function that maps each byte to an object
     * @param targetElementType the class of the target element type
     * @return a 3D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each short element to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the short array to map
     * @param mapper the function that maps each short to an object
     * @param targetElementType the class of the target element type
     * @return an object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each short element in a 2D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 2D short array to map
     * @param mapper the function that maps each short to an object
     * @param targetElementType the class of the target element type
     * @return a 2D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each short element in a 3D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 3D short array to map
     * @param mapper the function that maps each short to an object
     * @param targetElementType the class of the target element type
     * @return a 3D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each int element to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the int array to map
     * @param mapper the function that maps each int to an object
     * @param targetElementType the class of the target element type
     * @return an object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
     *
     * @example
     * <pre>
     * int[] numbers = {1, 2, 3};
     * String[] strings = Arrays.mapToObj(numbers, i -> "Number: " + i, String.class);
     * // Result: ["Number: 1", "Number: 2", "Number: 3"]
     * </pre>
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
     * Maps each int element in a 2D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 2D int array to map
     * @param mapper the function that maps each int to an object
     * @param targetElementType the class of the target element type
     * @return a 2D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each int element in a 3D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 3D int array to map
     * @param mapper the function that maps each int to an object
     * @param targetElementType the class of the target element type
     * @return a 3D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each long element to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the long array to map
     * @param mapper the function that maps each long to an object
     * @param targetElementType the class of the target element type
     * @return an object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each long element in a 2D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 2D long array to map
     * @param mapper the function that maps each long to an object
     * @param targetElementType the class of the target element type
     * @return a 2D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each long element in a 3D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 3D long array to map
     * @param mapper the function that maps each long to an object
     * @param targetElementType the class of the target element type
     * @return a 3D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each float element to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the float array to map
     * @param mapper the function that maps each float to an object
     * @param targetElementType the class of the target element type
     * @return an object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each float element in a 2D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 2D float array to map
     * @param mapper the function that maps each float to an object
     * @param targetElementType the class of the target element type
     * @return a 2D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps each float element in a 3D array to an object using the provided mapper function.
     *
     * @param <T> the type of elements in the result array
     * @param <E> the type of exception that the mapper may throw
     * @param a the 3D float array to map
     * @param mapper the function that maps each float to an object
     * @param targetElementType the class of the target element type
     * @return a 3D object array containing the mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a double array to an object array using the provided mapper function.
     * Each element in the input array is transformed using the mapper function.
     *
     * <p>Example usage:
     * <pre>{@code
     * double[] doubles = {1.5, 2.5, 3.5};
     * String[] strings = mapToObj(doubles, d -> String.format("%.1f", d), String.class);
     * // Result: {"1.5", "2.5", "3.5"}
     * }</pre>
     *
     * @param <T> the type of elements in the resulting array
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input double array
     * @param mapper the function to transform each double element
     * @param targetElementType the class of the target element type
     * @return an array of type T with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 2D double array to a 2D object array using the provided mapper function.
     * Each element in the input array is transformed using the mapper function.
     *
     * <p>Example usage:
     * <pre>{@code
     * double[][] doubles = {{1.5, 2.5}, {3.5, 4.5}};
     * String[][] strings = mapToObj(doubles, d -> String.format("%.1f", d), String.class);
     * // Result: {{"1.5", "2.5"}, {"3.5", "4.5"}}
     * }</pre>
     *
     * @param <T> the type of elements in the resulting array
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 2D double array
     * @param mapper the function to transform each double element
     * @param targetElementType the class of the target element type
     * @return a 2D array of type T with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 3D double array to a 3D object array using the provided mapper function.
     * Each element in the input array is transformed using the mapper function.
     *
     * <p>Example usage:
     * <pre>{@code
     * double[][][] doubles = {{{1.1, 2.2}}, {{3.3, 4.4}}};
     * Integer[][][] integers = mapToObj(doubles, d -> (int) d, Integer.class);
     * // Result: {{{1, 2}}, {{3, 4}}}
     * }</pre>
     *
     * @param <T> the type of elements in the resulting array
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 3D double array
     * @param mapper the function to transform each double element
     * @param targetElementType the class of the target element type
     * @return a 3D array of type T with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 1D int array to a 1D long array using the provided mapper function.
     * Each int element is transformed to a long value.
     *
     * <p>Example usage:
     * <pre>{@code
     * int[] ints = {1, 2, 3};
     * long[] longs = mapToLong(ints, i -> i * 1000000000L);
     * // Result: {1000000000L, 2000000000L, 3000000000L}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input int array
     * @param mapper the function to transform each int to long
     * @return a long array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 2D int array to a 2D long array using the provided mapper function.
     * Each int element is transformed to a long value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 2D int array
     * @param mapper the function to transform each int to long
     * @return a 2D long array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 3D int array to a 3D long array using the provided mapper function.
     * Each int element is transformed to a long value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 3D int array
     * @param mapper the function to transform each int to long
     * @return a 3D long array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 1D int array to a 1D double array using the provided mapper function.
     * Each int element is transformed to a double value.
     *
     * <p>Example usage:
     * <pre>{@code
     * int[] ints = {1, 2, 3};
     * double[] doubles = mapToDouble(ints, i -> i / 2.0);
     * // Result: {0.5, 1.0, 1.5}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input int array
     * @param mapper the function to transform each int to double
     * @return a double array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 2D int array to a 2D double array using the provided mapper function.
     * Each int element is transformed to a double value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 2D int array
     * @param mapper the function to transform each int to double
     * @return a 2D double array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 3D int array to a 3D double array using the provided mapper function.
     * Each int element is transformed to a double value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 3D int array
     * @param mapper the function to transform each int to double
     * @return a 3D double array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 1D long array to a 1D int array using the provided mapper function.
     * Each long element is transformed to an int value.
     *
     * <p>Example usage:
     * <pre>{@code
     * long[] longs = {1000L, 2000L, 3000L};
     * int[] ints = mapToInt(longs, l -> (int)(l / 1000));
     * // Result: {1, 2, 3}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input long array
     * @param mapper the function to transform each long to int
     * @return an int array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 2D long array to a 2D int array using the provided mapper function.
     * Each long element is transformed to an int value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 2D long array
     * @param mapper the function to transform each long to int
     * @return a 2D int array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 3D long array to a 3D int array using the provided mapper function.
     * Each long element is transformed to an int value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 3D long array
     * @param mapper the function to transform each long to int
     * @return a 3D int array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 1D long array to a 1D double array using the provided mapper function.
     * Each long element is transformed to a double value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input long array
     * @param mapper the function to transform each long to double
     * @return a double array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 2D long array to a 2D double array using the provided mapper function.
     * Each long element is transformed to a double value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 2D long array
     * @param mapper the function to transform each long to double
     * @return a 2D double array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 3D long array to a 3D double array using the provided mapper function.
     * Each long element is transformed to a double value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 3D long array
     * @param mapper the function to transform each long to double
     * @return a 3D double array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 1D double array to a 1D int array using the provided mapper function.
     * Each double element is transformed to an int value.
     *
     * <p>Example usage:
     * <pre>{@code
     * double[] doubles = {1.7, 2.3, 3.9};
     * int[] ints = mapToInt(doubles, d -> (int) Math.round(d));
     * // Result: {2, 2, 4}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input double array
     * @param mapper the function to transform each double to int
     * @return an int array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 2D double array to a 2D int array using the provided mapper function.
     * Each double element is transformed to an int value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 2D double array
     * @param mapper the function to transform each double to int
     * @return a 2D int array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 3D double array to a 3D int array using the provided mapper function.
     * Each double element is transformed to an int value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 3D double array
     * @param mapper the function to transform each double to int
     * @return a 3D int array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 1D double array to a 1D long array using the provided mapper function.
     * Each double element is transformed to a long value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input double array
     * @param mapper the function to transform each double to long
     * @return a long array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 2D double array to a 2D long array using the provided mapper function.
     * Each double element is transformed to a long value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 2D double array
     * @param mapper the function to transform each double to long
     * @return a 2D long array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Maps a 3D double array to a 3D long array using the provided mapper function.
     * Each double element is transformed to a long value.
     *
     * @param <E> the type of exception that may be thrown by the mapper
     * @param a the input 3D double array
     * @param mapper the function to transform each double to long
     * @return a 3D long array with mapped values, or null if input is null
     * @throws E if the mapper function throws an exception
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
     * Updates all elements in a boolean array using the provided unary operator.
     * Each element is replaced with the result of applying the operator.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[] arr = {true, false, true};
     * updateAll(arr, b -> !b);
     * // arr is now: {false, true, false}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operator
     * @param a the boolean array to update
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Updates all elements in a 2D boolean array using the provided unary operator.
     * Each element in every sub-array is replaced with the result of applying the operator.
     *
     * @param <E> the type of exception that may be thrown by the operator
     * @param a the 2D boolean array to update
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Updates all elements in a 3D boolean array using the provided unary operator.
     * Each element in every sub-array is replaced with the result of applying the operator.
     *
     * @param <E> the type of exception that may be thrown by the operator
     * @param a the 3D boolean array to update
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Replaces all elements in a boolean array that match the predicate with a new value.
     * Elements that don't match the predicate remain unchanged.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[] arr = {true, false, true, false};
     * replaceIf(arr, b -> b == true, false);
     * // arr is now: {false, false, false, false}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate
     * @param a the boolean array to modify
     * @param predicate the condition to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * Replaces all elements in a 2D boolean array that match the predicate with a new value.
     * Elements that don't match the predicate remain unchanged.
     *
     * @param <E> the type of exception that may be thrown by the predicate
     * @param a the 2D boolean array to modify
     * @param predicate the condition to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * Replaces all elements in a 3D boolean array that match the predicate with a new value.
     * Elements that don't match the predicate remain unchanged.
     *
     * @param <E> the type of exception that may be thrown by the predicate
     * @param a the 3D boolean array to modify
     * @param predicate the condition to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * The last row may have fewer elements if the total elements don't divide evenly.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[] arr = {true, false, true, false, true};
     * boolean[][] reshaped = reshape(arr, 2);
     * // Result: {{true, false}, {true, false}, {true}}
     * }</pre>
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
     * The array is divided into blocks of size rows × cols.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[] arr = {true, false, true, false, true, false};
     * boolean[][][] reshaped = reshape(arr, 2, 2);
     * // Result: {{{true, false}, {true, false}}, {{true, false}}}
     * }</pre>
     *
     * @param a the one-dimensional boolean array to reshape
     * @param rows the number of rows for the reshaped subarray
     * @param cols the number of columns for the reshaped subarray
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
     * Flattens a 2D boolean array into a 1D boolean array.
     * All elements from all sub-arrays are combined into a single array.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[][] arr = {{true, false}, {true}, {false, true}};
     * boolean[] flattened = flatten(arr);
     * // Result: {true, false, true, false, true}
     * }</pre>
     *
     * @param a the 2D boolean array to flatten
     * @return a 1D boolean array containing all elements, or empty array if input is null/empty
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
     * Flattens a 3D boolean array into a 1D boolean array.
     * All elements from all sub-arrays are combined into a single array.
     *
     * @param a the 3D boolean array to flatten
     * @return a 1D boolean array containing all elements, or empty array if input is null/empty
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
     * Flattens a 2D boolean array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[][] arr = {{true, false}, {false, true}};
     * flatOp(arr, t -> Arrays.sort(t)); // Sorts all elements
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operation
     * @param a the 2D boolean array to operate on
     * @param op the operation to apply to the flattened array
     * @throws E if the operation throws an exception
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
     * Flattens a 3D boolean array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure.
     *
     * @param <E> the type of exception that may be thrown by the operation
     * @param a the 3D boolean array to operate on
     * @param op the operation to apply to the flattened array
     * @throws E if the operation throws an exception
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
     * Combines elements from two boolean arrays using the provided zip function.
     * The operation stops when the shorter array is exhausted, resulting in an output
     * array with length equal to the minimum of the two input array lengths.
     * 
     * <p>This method is useful when you want to perform element-wise operations on two
     * arrays without worrying about length mismatches. Null arrays are treated as empty.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[] a = {true, false, true, false};
     * boolean[] b = {false, true, false};
     * boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);
     * // result: {false, false, false}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from both arrays
     * @return a new array containing the results of applying the zip function to corresponding elements
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[] zip(final boolean[] a, final boolean[] b, final Throwables.BooleanBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final boolean[] result = new boolean[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsBoolean(a[i], b[i]);
        }

        return result;
    }

    /**
     * Combines elements from two boolean arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths. The result array length equals
     * the maximum of the two input array lengths.
     * 
     * <p>This method is ideal when you need to process arrays of different lengths and want
     * to provide sensible defaults for missing values rather than truncating to the shorter length.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[] a = {true, false, true, false};
     * boolean[] b = {false, true};
     * boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);
     * // result: {true, true, true, false} (using true for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the longer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[] zip(final boolean[] a, final boolean[] b, final boolean valueForNoneA, final boolean valueForNoneB,
            final Throwables.BooleanBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final boolean[] result = new boolean[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsBoolean(a[i], b[i]);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zipFunction.applyAsBoolean(valueForNoneA, b[i]);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zipFunction.applyAsBoolean(a[i], valueForNoneB);
            }
        }

        return result;
    }

    /**
     * Combines elements from three boolean arrays using the provided zip function.
     * The operation stops when the shortest array is exhausted, making this suitable
     * for combining multiple arrays when you only care about positions where all arrays
     * have values.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[] a = {true, false, true, false};
     * boolean[] b = {false, true, false};
     * boolean[] c = {true, true};
     * boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);
     * // result: {true, true}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new array containing the results with length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[] zip(final boolean[] a, final boolean[] b, final boolean[] c,
            final Throwables.BooleanTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final boolean[] result = new boolean[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsBoolean(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     * Combines elements from three boolean arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths. This provides maximum flexibility
     * when working with three arrays of potentially different sizes.
     * 
     * <p>The result array has a length equal to the longest input array, with default values
     * substituted for any missing elements from shorter arrays.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[] a = {true, false, true, false};
     * boolean[] b = {false, true};
     * boolean[] c = {true, true, false};
     * boolean[] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x && y && z);
     * // result: {false, false, false, false} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param valueForNoneC the default value to use when array 'c' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the longest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[] zip(final boolean[] a, final boolean[] b, final boolean[] c, final boolean valueForNoneA,
            final boolean valueForNoneB, final boolean valueForNoneC, final Throwables.BooleanTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final boolean[] result = new boolean[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsBoolean(a[i], b[i], c[i]);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zipFunction.applyAsBoolean(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 2D boolean arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays (rows), stopping when the
     * shorter outer array is exhausted. Each pair of corresponding sub-arrays is zipped
     * using the standard 1D array zip logic.
     * 
     * <p>This method is useful for matrix-like operations where you want to combine
     * corresponding rows from two 2D arrays.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[][] a = {{true, false}, {false, true, false}};
     * boolean[][] b = {{false, true, false}, {true, false}};
     * boolean[][] result = Arrays.zip(a, b, (x, y) -> x ^ y);
     * // result: {{true, true}, {true, true}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 2D array containing the results of zipping corresponding sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[][] zip(final boolean[][] a, final boolean[][] b, final Throwables.BooleanBinaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final boolean[][] result = new boolean[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 2D boolean arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels. This provides complete control
     * over how mismatched array dimensions are handled.
     * 
     * <p>The outer array length of the result equals the maximum of the input outer array lengths.
     * For each position, if one array lacks a sub-array, a null is treated as an empty array and
     * default values are used for all positions.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[][] a = {{true, false}, {false, true, false}};
     * boolean[][] b = {{false, true, false}, {true, false}, {true}};
     * boolean[][] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);
     * // result: {{true, true, false}, {true, true, true}, {true}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the longer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[][] zip(final boolean[][] a, final boolean[][] b, final boolean valueForNoneA, final boolean valueForNoneB,
            final Throwables.BooleanBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final boolean[][] result = new boolean[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 2D boolean arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays (rows) from all three input arrays,
     * stopping when the shortest outer array is exhausted.
     * 
     * <p>This method extends the 2D zip concept to three arrays, useful for combining
     * data from three different sources row by row.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[][] a = {{true, false}, {false, true, false}};
     * boolean[][] b = {{false, true, false}, {true, false}};
     * boolean[][] c = {{true, true}, {false, false, true}};
     * boolean[][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);
     * // result: {{true, true}, {true, true}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 2D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[][] zip(final boolean[][] a, final boolean[][] b, final boolean[][] c,
            final Throwables.BooleanTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final boolean[][] result = new boolean[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 2D boolean arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels. This is the most flexible 2D zip
     * operation, handling any combination of array dimension mismatches.
     * 
     * <p>The result has an outer array length equal to the longest input outer array. Missing sub-arrays
     * are treated as null/empty, and the provided default values are used for any missing elements.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[][] a = {{true, false}, {false, true, false}};
     * boolean[][] b = {{false, true, false}, {true, false}, {true}};
     * boolean[][] c = {{true, true}};
     * boolean[][] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x && y && z);
     * // result: {{false, false, false}, {false, false, false}, {false}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the longest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[][] zip(final boolean[][] a, final boolean[][] b, final boolean[][] c, final boolean valueForNoneA,
            final boolean valueForNoneB, final boolean valueForNoneC, final Throwables.BooleanTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final boolean[][] result = new boolean[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 3D boolean arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, which in turn zip their
     * corresponding 1D sub-arrays. The operation stops when the shorter outer array is exhausted.
     * 
     * <p>This method is useful for combining 3D data structures like cubes or time-series of matrices,
     * where you want to perform element-wise operations on corresponding positions.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[][][] a = {{{true, false}, {false, true}}, {{true, true}, {false, false}}};
     * boolean[][][] b = {{{false, true}, {true, false}}, {{false, false}, {true, true}}};
     * boolean[][][] result = Arrays.zip(a, b, (x, y) -> x ^ y);
     * // result: {{{true, true}, {true, true}}, {{true, true}, {true, true}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 3D array containing the results of zipping corresponding 2D sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[][][] zip(final boolean[][][] a, final boolean[][][] b, final Throwables.BooleanBinaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final boolean[][][] result = new boolean[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 3D boolean arrays using the provided zip function, with default values
     * for missing elements at all array levels. This handles dimension mismatches at every level of
     * the 3D structure.
     * 
     * <p>The result has an outer array length equal to the longest input outer array. Missing 2D
     * sub-arrays are handled by treating them as null and using default values throughout.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[][][] a = {{{true, false}, {false, true}}, {{true, true}}};
     * boolean[][][] b = {{{false, true}, {true, false}}, {{false, false}, {true, true}}, {{false}}};
     * boolean[][][] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);
     * // result: {{{true, true}, {true, true}}, {{true, true}, {true, true}}, {{false}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the longer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[][][] zip(final boolean[][][] a, final boolean[][][] b, final boolean valueForNoneA,
            final boolean valueForNoneB, final Throwables.BooleanBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final boolean[][][] result = new boolean[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 3D boolean arrays using the provided zip function.
     * Applies the zip operation recursively through all three dimensions, combining corresponding
     * elements from all three arrays at the deepest level.
     * 
     * <p>This method is suitable for combining three 3D data structures where you need to perform
     * operations on corresponding elements across all three sources.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[][][] a = {{{true, false}}};
     * boolean[][][] b = {{{false, true}, {true, false}}};
     * boolean[][][] c = {{{true, true}}};
     * boolean[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);
     * // result: {{{true, true}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 3D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[][][] zip(final boolean[][][] a, final boolean[][][] b, final boolean[][][] c,
            final Throwables.BooleanTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final boolean[][][] result = new boolean[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 3D boolean arrays using the provided zip function, with default values
     * for missing elements at all array levels. This is the most comprehensive 3D zip operation,
     * handling any combination of dimension mismatches across all three arrays.
     * 
     * <p>The result has an outer array length equal to the longest input outer array. Default values
     * are used whenever any array lacks elements at any level of the structure.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[][][] a = {{{true, false}}};
     * boolean[][][] b = {{{false, true}, {true, false}}};
     * boolean[][][] c = {{{true, true}}};
     * boolean[][][] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x && y && z);
     * // result: {{{false, false}, {false, false}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the longest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> boolean[][][] zip(final boolean[][][] a, final boolean[][][] b, final boolean[][][] c, final boolean valueForNoneA,
            final boolean valueForNoneB, final boolean valueForNoneC, final Throwables.BooleanTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final boolean[][][] result = new boolean[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = minLen; i < maxLen; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    /**
     * Calculates the total count of elements in a 2D boolean array.
     * Counts all elements across all sub-arrays, handling null sub-arrays gracefully.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[][] arr = {{true, false}, {true}, {false, true, false}};
     * long count = totalCountOfElements(arr);
     * // Result: 6
     * }</pre>
     *
     * @param a the 2D boolean array
     * @return the total number of elements across all sub-arrays
     */
    public static long totalCountOfElements(final boolean[][] a) {
        long count = 0;

        for (final boolean[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total count of elements in a 3D boolean array.
     * Counts all elements across all sub-arrays at all levels, handling null arrays gracefully.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[][][] arr = {{{true, false}, {true}}, {{false}, {true, false}}};
     * long count = totalCountOfElements(arr);
     * // Result: 6
     * }</pre>
     *
     * @param a the 3D boolean array
     * @return the total number of elements across all sub-arrays
     */
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
     * Finds the minimum length among all sub-arrays in a 2D boolean array.
     * Returns 0 if the input array is null or empty.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[][] arr = {{true, false, true}, {true}, {false, true}};
     * int minLen = minSubArrayLen(arr);
     * // Result: 1
     * }</pre>
     *
     * @param a the 2D boolean array
     * @return the minimum length of sub-arrays, or 0 if array is empty
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
     * Finds the maximum length among all sub-arrays in a 2D boolean array.
     * Returns 0 if the input array is null or empty.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[][] arr = {{true, false, true}, {true}, {false, true}};
     * int maxLen = maxSubArrayLen(arr);
     * // Result: 3
     * }</pre>
     *
     * @param a the 2D boolean array
     * @return the maximum length of sub-arrays, or 0 if array is empty
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
     * Prints a 1D boolean array to the console and returns the printed string.
     * Handles null arrays and empty arrays appropriately.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[] arr = {true, false, true};
     * String output = println(arr);
     * // Prints and returns: [true, false, true]
     * }</pre>
     *
     * @param a the boolean array to print
     * @return the string representation that was printed
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
     * Prints a 2D boolean array to the console with proper formatting and returns the printed string.
     * Each sub-array is printed on a separate line with appropriate indentation.
     *
     * <p>Example usage:
     * <pre>{@code
     * boolean[][] arr = {{true, false}, {false, true}};
     * String output = println(arr);
     * // Prints: [[true, false],
     * //          [false, true]]
     * }</pre>
     *
     * @param a the 2D boolean array to print
     * @return the string representation that was printed
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
     * Prints a 3D boolean array to the console with proper formatting and returns the printed string.
     * The array is formatted with multiple levels of indentation for readability.
     *
     * @param a the 3D boolean array to print
     * @return the string representation that was printed
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

    /**
     * Updates every element in the specified character array by applying a given operator.
     * This method modifies the array in-place.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[] array = {'a', 'b', 'c'};
     * Arrays.updateAll(array, c -> (char) (c + 1));
     * // array becomes {'b', 'c', 'd'}
     * }</pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The character array to update. The update is performed in-place.
     * @param operator The unary operator to apply to each element.
     * @throws E if the operator throws an exception.
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
     * Recursively updates every element in the 2D character array by applying a given operator.
     * The method iterates through each sub-array and applies the operator to every character.
     * The operation is performed in-place.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][] array = {{'a', 'b'}, {'c'}};
     * Arrays.updateAll(array, c -> Character.toUpperCase(c));
     * // array becomes {{'A', 'B'}, {'C'}}
     * }</pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The 2D character array to update. The update is performed in-place.
     * @param operator The unary operator to apply to each element.
     * @throws E if the operator throws an exception.
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
     * Recursively updates every element in the 3D character array by applying a given operator.
     * It traverses the nested structure and applies the operator to each character.
     * The operation is performed in-place.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][][] array = {{{'a'}}, {{'b', 'c'}}};
     * Arrays.updateAll(array, c -> '*');
     * // array becomes {{{'*'}}, {{'*', '*'}}}
     * }</pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The 3D character array to update. The update is performed in-place.
     * @param operator The unary operator to apply to each element.
     * @throws E if the operator throws an exception.
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
     * Replaces each element of a character array with a new value if the element satisfies
     * the given predicate. The operation is performed in-place.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[] array = {'a', 'b', 'c', 'a'};
     * Arrays.replaceIf(array, c -> c == 'a', 'x');
     * // array becomes {'x', 'b', 'c', 'x'}
     * }</pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The character array to modify.
     * @param predicate The condition to test for each element.
     * @param newValue The value to be placed in the array if the predicate is true.
     * @throws E if the predicate throws an exception.
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
     * Recursively replaces each element of a 2D character array with a new value if the
     * element satisfies the given predicate. The operation is performed in-place.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][] array = {{'a', 'b'}, {'c', 'A'}};
     * Arrays.replaceIf(array, c -> Character.isUpperCase(c), 'U');
     * // array becomes {{'a', 'b'}, {'c', 'U'}}
     * }</pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The 2D character array to modify.
     * @param predicate The condition to test for each element.
     * @param newValue The value to be placed in the array if the predicate is true.
     * @throws E if the predicate throws an exception.
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
     * Recursively replaces each element of a 3D character array with a new value if the
     * element satisfies the given predicate. The operation is performed in-place.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][][] array = {{{'a'}}, {{'B', 'c'}}};
     * Arrays.replaceIf(array, c -> Character.isUpperCase(c), 'X');
     * // array becomes {{{'a'}}, {{'X', 'c'}}}
     * }</pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The 3D character array to modify.
     * @param predicate The condition to test for each element.
     * @param newValue The value to be placed in the array if the predicate is true.
     * @throws E if the predicate throws an exception.
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
     * Reshapes a 1D character array into a 2D character array with a specified number of columns.
     * If the length of the input array is not a multiple of {@code cols}, the last sub-array
     * will be shorter.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[] array = {'a', 'b', 'c', 'd', 'e'};
     * char[][] reshaped = Arrays.reshape(array, 2);
     * // reshaped is {{'a', 'b'}, {'c', 'd'}, {'e'}}
     * }</pre>
     *
     * @param a The 1D character array to reshape.
     * @param cols The number of columns in the new 2D array.
     * @return A new 2D character array.
     * @throws IllegalArgumentException if {@code cols} is not positive.
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
     * Reshapes a 1D character array into a 3D character array with specified dimensions.
     * If the input array's length is not a perfect multiple of {@code rows * cols},
     * the last sub-arrays may be shorter.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[] array = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
     * char[][][] reshaped = Arrays.reshape(array, 2, 2);
     * // reshaped is {{{'a', 'b'}, {'c', 'd'}}, {{'e', 'f'}, {'g'}}}
     * }</pre>
     *
     * @param a The 1D character array to reshape.
     * @param rows The number of rows in each 2D sub-array.
     * @param cols The number of columns in each 2D sub-array.
     * @return A new 3D character array.
     * @throws IllegalArgumentException if {@code rows} or {@code cols} are not positive.
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
     * Flattens a 2D character array into a new 1D character array by concatenating
     * all its sub-arrays. Null or empty sub-arrays are skipped.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][] array = {{'a', 'b'}, null, {'c'}};
     * char[] flattened = Arrays.flatten(array);
     * // flattened is {'a', 'b', 'c'}
     * }</pre>
     *
     * @param a The 2D character array to flatten.
     * @return A new 1D character array containing all elements from the input array.
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
     * Flattens a 3D character array into a new 1D character array by concatenating
     * all its innermost sub-arrays.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][][] array = {{{'a'}, {'b'}}, {{'c', 'd'}}};
     * char[] flattened = Arrays.flatten(array);
     * // flattened is {'a', 'b', 'c', 'd'}
     * }</pre>
     *
     * @param a The 3D character array to flatten.
     * @return A new 1D character array containing all elements from the input array.
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
     * Flattens a 2D array, performs a specified operation on the resulting 1D array,
     * and then copies the modified elements back into the original 2D array.
     * This allows for applying 1D array operations (like sorting) across a 2D structure.
     * The operation is performed in-place on the original 2D array.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][] array = {{'c', 'a'}, {'b'}};
     * Arrays.flatOp(array, t -> Arrays.sort(t));
     * // array becomes {{'a', 'b'}, {'c'}}
     * }</pre>
     *
     * @param <E> The type of exception that the operation may throw.
     * @param a The 2D character array to operate on.
     * @param op The consumer operation to apply to the flattened array.
     * @throws E if the operation throws an exception.
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
     * Flattens a 3D array, performs a specified operation on the resulting 1D array,
     * and then copies the modified elements back into the original 3D array.
     * This is useful for applying 1D array operations across a 3D structure.
     * The operation is performed in-place on the original 3D array.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][][] array = {{{'d', 'a'}}, {{'c'}, {'b'}}};
     * Arrays.flatOp(array, t -> Arrays.sort(t));
     * // array becomes {{{'a', 'b'}}, {{'c'}, {'d'}}}
     * }</pre>
     *
     * @param <E> The type of exception that the operation may throw.
     * @param a The 3D character array to operate on.
     * @param op The consumer operation to apply to the flattened array.
     * @throws E if the operation throws an exception.
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
     * Combines elements from two char arrays using the provided zip function.
     * The operation stops when the shorter array is exhausted.
     * 
     * <p>This method iterates through both arrays simultaneously and applies the zip function
     * to corresponding elements. The resulting array length equals the length of the shorter
     * input array. Null arrays are treated as empty arrays.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[] a = {'A', 'B', 'C', 'D'};
     * char[] b = {'X', 'Y', 'Z'};
     * char[] result = Arrays.zip(a, b, (x, y) -> (char)(x + y - 'A'));
     * // result: {'X', 'Z', '\'}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from both arrays
     * @return a new array containing the results of applying the zip function to corresponding elements
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[] zip(final char[] a, final char[] b, final Throwables.CharBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final char[] result = new char[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsChar(a[i], b[i]);
        }

        return result;
    }

    /**
     * Combines elements from two char arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>This method creates a result array with length equal to the longer input array.
     * When one array is shorter, the specified default value is used in place of missing elements.
     * This allows for complete processing of both arrays even when they have different lengths.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[] a = { 'A', 'B', 'C', 'D' };
     * char[] b = { 'X', 'Y' };
     * char[] result = Arrays.zip(a, b, '?', '!', (x, y) -> (char) (x + y - 'A'));
     * // result: {'X', 'Z', '#', '$'} (using '!' for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the longer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[] zip(final char[] a, final char[] b, final char valueForNoneA, final char valueForNoneB,
            final Throwables.CharBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final char[] result = new char[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsChar(a[i], b[i]);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zipFunction.applyAsChar(valueForNoneA, b[i]);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zipFunction.applyAsChar(a[i], valueForNoneB);
            }
        }

        return result;
    }

    /**
     * Combines elements from three char arrays using the provided zip function.
     * The operation stops when the shortest array is exhausted.
     * 
     * <p>This method processes three arrays simultaneously, applying the tri-function to
     * corresponding elements from all three arrays. The result array length is determined
     * by the shortest input array, ensuring all elements in the result have valid inputs
     * from all three source arrays.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[] a = {'A', 'B', 'C', 'D'};
     * char[] b = {'X', 'Y', 'Z'};
     * char[] c = {'1', '3'};
     * char[] result = Arrays.zip(a, b, c, (x, y, z) -> (char)(x + y + z - 'A' - '0'));
     * // result: {'Y', ']'}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new array containing the results with length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[] zip(final char[] a, final char[] b, final char[] c, final Throwables.CharTernaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final char[] result = new char[minLen];

        for (int i = 0; i < minLen; i++) {
            N.println((int) zipFunction.applyAsChar(a[i], b[i], c[i]));
            result[i] = zipFunction.applyAsChar(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     * Combines elements from three char arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>This method creates a result array with length equal to the longest input array.
     * When any array is shorter than the longest, the corresponding default value is used
     * for missing elements. This enables complete processing of all arrays regardless of
     * their individual lengths.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[] a = {'A', 'B', 'C', 'D'};
     * char[] b = {'X', 'Y'};
     * char[] c = {'1', '2', '3'};
     * char[] result = Arrays.zip(a, b, c, '?', '!', '0', (x, y, z) -> (char)(x + y + z - 'A' - '0'));
     * // result: {'Y', '\\', '&', '$'} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param valueForNoneC the default value to use when array 'c' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the longest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[] zip(final char[] a, final char[] b, final char[] c, final char valueForNoneA, final char valueForNoneB,
            final char valueForNoneC, final Throwables.CharTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final char[] result = new char[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsChar(a[i], b[i], c[i]);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zipFunction.applyAsChar(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 2D char arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>This method processes 2D arrays by applying the zip operation to each pair of
     * corresponding inner arrays. The outer array length of the result equals the length
     * of the shorter outer array. Each inner array is processed independently using the
     * single-array zip operation.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[][] a = {{'A', 'B'}, {'C', 'D', 'E'}};
     * char[][] b = {{'X', 'Y', 'Z'}, {'1', '2'}};
     * char[][] result = Arrays.zip(a, b, (x, y) -> (char)(x + y - 'A'));
     * // result: {{'X', 'Z'}, {'3', '5'}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 2D array containing the results of zipping corresponding sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[][] zip(final char[][] a, final char[][] b, final Throwables.CharBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final char[][] result = new char[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 2D char arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>This method handles 2D arrays of different sizes by using default values for missing
     * elements. The outer array length of the result equals the length of the longer outer array.
     * When processing inner arrays, default values are used for any missing elements within
     * those arrays as well.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[][] a = {{'A', 'B'}, {'C', 'D', 'E'}};
     * char[][] b = {{'X', 'Y', 'Z'}, {'1', '2'}, {'3'}};
     * char[][] result = Arrays.zip(a, b, '?', '!', (x, y) -> (char)(x + y - 'A'));
     * // result: {{'X', 'Z', 'X'}, {'3', '5', '%'}, {'1'}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the longer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[][] zip(final char[][] a, final char[][] b, final char valueForNoneA, final char valueForNoneB,
            final Throwables.CharBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final char[][] result = new char[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 2D char arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>This method processes three 2D arrays simultaneously by applying the tri-function
     * to corresponding elements within each triplet of inner arrays. The outer array length
     * of the result is determined by the shortest outer array among the three inputs.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[][] a = {{'A', 'B'}, {'C', 'D', 'E'}};
     * char[][] b = {{'X', 'Y', 'Z'}, {'1', '2'}};
     * char[][] c = {{'a', 'b'}, {'c', 'd', 'e'}};
     * char[][] result = Arrays.zip(a, b, c, (x, y, z) -> (char)((x + y + z) / 3));
     * // result: {{'S', 'T'}, {'G', 'H'}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 2D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[][] zip(final char[][] a, final char[][] b, final char[][] c, final Throwables.CharTernaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final char[][] result = new char[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 2D char arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>This method provides complete processing of three 2D arrays regardless of size differences.
     * The result has an outer array length equal to the longest outer array among the inputs.
     * Default values are used whenever an array or sub-array is shorter than the others,
     * ensuring all positions in the result array are filled.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[][] a = {{'A', 'B'}, {'C', 'D', 'E'}};
     * char[][] b = {{'X', 'Y', 'Z'}, {'1', '2'}, {'3'}};
     * char[][] c = {{'a', 'b'}};
     * char[][] result = Arrays.zip(a, b, c, '?', '!', '~', (x, y, z) -> (char)((x + y + z) / 3));
     * // result: {{'S', 'T', ']'}, {'P', 'Q', 'L'}, {'P'}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the longest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[][] zip(final char[][] a, final char[][] b, final char[][] c, final char valueForNoneA, final char valueForNoneB,
            final char valueForNoneC, final Throwables.CharTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final char[][] result = new char[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 3D char arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>This method processes 3D arrays by recursively applying the zip operation to each
     * level of the array hierarchy. The outermost array length of the result equals the
     * length of the shorter outermost array. Each 2D sub-array is processed using the 2D
     * array zip operation.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[][][] a = {{{'A', 'B'}, {'C', 'D'}}, {{'E', 'F'}, {'G', 'H'}}};
     * char[][][] b = {{{'1', '2'}, {'3', '4'}}, {{'5', '6'}, {'7', '8'}}};
     * char[][][] result = Arrays.zip(a, b, (x, y) -> (char)(x + y - 'A' + '0'));
     * // result: {{{'1', '3'}, {'5', '7'}}, {{'9', ';'}, {'=', '?'}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 3D array containing the results of zipping corresponding 2D sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[][][] zip(final char[][][] a, final char[][][] b, final Throwables.CharBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final char[][][] result = new char[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 3D char arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>This method handles 3D arrays of different sizes by using default values for missing
     * elements at any level of the array hierarchy. The outermost array length of the result
     * equals the length of the longer outermost array. Default values are propagated through
     * all levels of array processing.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[][][] a = {{{'A', 'B'}, {'C', 'D'}}, {{'E', 'F'}}};
     * char[][][] b = {{{'1', '2'}, {'3', '4'}}, {{'5', '6'}, {'7', '8'}}, {{'9'}}};
     * char[][][] result = Arrays.zip(a, b, 'A', '0', (x, y) -> (char) (x + y - 'A'));
     * // result: result: {{{'1', '3'}, {'5', '7'}}, {{'9', ';'}, {'7', '8'}}, {{'9'}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the longer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[][][] zip(final char[][][] a, final char[][][] b, final char valueForNoneA, final char valueForNoneB,
            final Throwables.CharBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final char[][][] result = new char[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 3D char arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>This method processes three 3D arrays simultaneously by applying the tri-function
     * through all levels of the array hierarchy. The outermost array length of the result
     * is determined by the shortest outermost array among the three inputs. Each triplet
     * of 2D sub-arrays is processed using the 2D array tri-zip operation.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[][][] a = {{{'A', 'B'}}};
     * char[][][] b = {{{'1', '2'}, {'3', '4'}}};
     * char[][][] c = {{{'a', 'b'}}};
     * char[][][] result = Arrays.zip(a, b, c, (x, y, z) -> (char)((x + y + z) / 3));
     * // result: {{{'F', 'G'}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 3D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[][][] zip(final char[][][] a, final char[][][] b, final char[][][] c,
            final Throwables.CharTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final char[][][] result = new char[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 3D char arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>This method provides the most comprehensive zip operation for 3D arrays, handling
     * size differences at every level of the array hierarchy. The result has an outermost
     * array length equal to the longest outermost array among the three inputs. Default
     * values are used whenever any array or sub-array is shorter than the others, ensuring
     * complete processing of all input data.</p>
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * char[][][] a = {{{'A', 'B'}}};
     * char[][][] b = {{{'1', '2'}, {'3', '4'}}};
     * char[][][] c = {{{'a', 'b'}}};
     * char[][][] result = Arrays.zip(a, b, c, '?', '!', '~', (x, y, z) -> (char)((x + y + z) / 3));
     * // result: {{{'F', 'G'}, {'P', 'P'}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the longest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> char[][][] zip(final char[][][] a, final char[][][] b, final char[][][] c, final char valueForNoneA,
            final char valueForNoneB, final char valueForNoneC, final Throwables.CharTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final char[][][] result = new char[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = minLen; i < maxLen; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    /**
     * Calculates the total number of characters in a 2D character array.
     * It safely handles null sub-arrays.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][] array = {{'a', 'b'}, {'c'}, null};
     * long count = Arrays.totalCountOfElements(array);
     * // count is 3
     * }</pre>
     *
     * @param a The 2D character array to count.
     * @return The total number of character elements in the array.
     */
    public static long totalCountOfElements(final char[][] a) {
        long count = 0;

        for (final char[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of characters in a 3D character array.
     * It safely handles null or empty sub-arrays at any level.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][][] array = {{{'a'}, {'b', 'c'}}, null, {{{'d'}}}};
     * long count = Arrays.totalCountOfElements(array);
     * // count is 4
     * }</pre>
     *
     * @param a The 3D character array to count.
     * @return The total number of character elements in the array.
     */
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
     * Finds the minimum length among all sub-arrays in a 2D character array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][] array = {{'a', 'b'}, {'c'}, null};
     * int minLen = Arrays.minSubArrayLen(array);
     * // minLen is 0
     * }</pre>
     *
     * @param a The 2D character array to inspect.
     * @return The minimum length of any sub-array. Returns 0 if the input array is empty or null.
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
     * Finds the maximum length among all sub-arrays in a 2D character array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][] array = {{'a'}, {'b', 'c', 'd'}, null};
     * int maxLen = Arrays.maxSubArrayLen(array);
     * // maxLen is 4
     * }</pre>
     *
     * @param a The 2D character array to inspect.
     * @return The maximum length of any sub-array. Returns 0 if the input array is empty or null.
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
     * Prints a 1D character array to the console and returns its string representation.
     * This method handles null and empty arrays gracefully.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[] arr = {'H', 'e', 'l', 'l', 'o'};
     * String output = Arrays.println(arr);
     * // Prints "[H, e, l, l, o]" to console and returns it.
     * }</pre>
     *
     * @param a The character array to print.
     * @return The string representation that was printed.
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
     * Prints a 2D character array to the console and returns its string representation.
     * Each sub-array is represented as a string, separated by commas.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][] arr = {{'a', 'b'}, {'c', 'd'}};
     * String output = Arrays.println(arr);
     * // Prints "[[a, b], [c, d]]" to console and returns it.
     * }</pre>
     *
     * @param a The 2D character array to print.
     * @return The string representation that was printed.
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
     * Prints a 3D character array to the console and returns its string representation.
     * The array is formatted with newlines between the major elements for readability.
     *
     * <p>Example usage:
     * <pre>{@code
     * char[][][] arr = {{{'a'}}, {{'b', 'c'}}};
     * String output = Arrays.println(arr);
     * // Prints a formatted string like "[[[a]], [[b, c]]]" and returns it.
     * }</pre>
     *
     * @param a The 3D character array to print.
     * @return The string representation that was printed.
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
     * Updates all elements in a byte array using the provided unary operator.
     * Each element is replaced with the result of applying the operator.
     *
     * <p>Example usage:
     * <pre>{@code
     * byte[] arr = {1, 2, 3};
     * updateAll(arr, b -> (byte)(b * b));
     * // arr is now: {1, 4, 9}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operator
     * @param a the byte array to update
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Updates all elements in a 2D byte array using the provided unary operator.
     * Each element in every sub-array is replaced with the result of applying the operator.
     *
     * @param <E> the type of exception that may be thrown by the operator
     * @param a the 2D byte array to update
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Updates all elements in a 3D byte array using the provided unary operator.
     * Each element at all levels is replaced with the result of applying the operator.
     *
     * @param <E> the type of exception that may be thrown by the operator
     * @param a the 3D byte array to update
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Replaces all elements in a byte array that match the predicate with a new value.
     * Elements that don't match the predicate remain unchanged.
     *
     * <p>Example usage:
     * <pre>{@code
     * byte[] arr = {1, 2, 3, 2, 4};
     * replaceIf(arr, b -> b == 2, (byte)10);
     * // arr is now: {1, 10, 3, 10, 4}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate
     * @param a the byte array to modify
     * @param predicate the condition to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * Replaces all elements in a 2D byte array that match the predicate with a new value.
     * Elements that don't match the predicate remain unchanged.
     *
     * @param <E> the type of exception that may be thrown by the predicate
     * @param a the 2D byte array to modify
     * @param predicate the condition to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * Replaces all elements in a 3D byte array that match the predicate with a new value.
     * Elements that don't match the predicate remain unchanged.
     *
     * @param <E> the type of exception that may be thrown by the predicate
     * @param a the 3D byte array to modify
     * @param predicate the condition to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * Flattens a 2D byte array into a 1D byte array.
     * All elements from all sub-arrays are combined into a single array.
     *
     * <p>Example usage:
     * <pre>{@code
     * byte[][] arr = {{1, 2}, {3}, {4, 5}};
     * byte[] flattened = flatten(arr);
     * // Result: {1, 2, 3, 4, 5}
     * }</pre>
     *
     * @param a the 2D byte array to flatten
     * @return a 1D byte array containing all elements, or empty array if input is null/empty
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
     * Flattens a 3D byte array into a 1D byte array.
     * All elements from all sub-arrays are combined into a single array.
     *
     * @param a the 3D byte array to flatten
     * @return a 1D byte array containing all elements, or empty array if input is null/empty
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
     * Flattens a 2D byte array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure.
     *
     * <p>Example usage:
     * <pre>{@code
     * byte[][] arr = {{3, 1}, {4, 2}};
     * flatOp(arr, t -> Arrays.sort(t)); // Sorts all elements
     * // arr becomes: {{1, 2}, {3, 4}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operation
     * @param a the 2D byte array to operate on
     * @param op the operation to apply to the flattened array
     * @throws E if the operation throws an exception
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
     * Flattens a 3D byte array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure.
     *
     * @param <E> the type of exception that may be thrown by the operation
     * @param a the 3D byte array to operate on
     * @param op the operation to apply to the flattened array
     * @throws E if the operation throws an exception
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
     * Reshapes a one-dimensional byte array into a two-dimensional byte array with the specified number of columns.
     * The last row may have fewer elements if the total elements don't divide evenly.
     *
     * <p>Example usage:
     * <pre>{@code
     * byte[] arr = {1, 2, 3, 4, 5};
     * byte[][] reshaped = reshape(arr, 2);
     * // Result: {{1, 2}, {3, 4}, {5}}
     * }</pre>
     *
     * @param a the one-dimensional byte array to reshape
     * @param cols the number of columns for the reshaped array
     * @return a two-dimensional byte array with the specified number of columns
     * @throws IllegalArgumentException if the number of columns is less than or equal to zero
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
     * Reshapes a one-dimensional byte array into a three-dimensional byte array with the specified number of rows and columns.
     * The array is divided into blocks of size rows × cols.
     *
     * <p>Example usage:
     * <pre>{@code
     * byte[] arr = {1, 2, 3, 4, 5, 6};
     * byte[][][] reshaped = reshape(arr, 2, 2);
     * // Result: {{{1, 2}, {3, 4}}, {{5, 6}}}
     * }</pre>
     *
     * @param a the one-dimensional byte array to reshape
     * @param rows the number of rows for the reshaped subarray
     * @param cols the number of columns for the reshaped subarray
     * @return a three-dimensional byte array with the specified number of rows and columns
     * @throws IllegalArgumentException if the number of rows or columns is less than or equal to zero
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
     * Combines elements from two byte arrays using the provided zip function.
     * The operation stops when the shorter array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[] a = {1, 2, 3, 4};
     * byte[] b = {5, 6, 7};
     * byte[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from both arrays
     * @return a new array containing the results of applying the zip function to corresponding elements
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[] zip(final byte[] a, final byte[] b, final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final byte[] result = new byte[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsByte(a[i], b[i]);
        }

        return result;
    }

    /**
     * Combines elements from two byte arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[] a = {1, 2, 3, 4};
     * byte[] b = {5, 6};
     * byte[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the byteer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[] zip(final byte[] a, final byte[] b, final byte valueForNoneA, final byte valueForNoneB,
            final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final byte[] result = new byte[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsByte(a[i], b[i]);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zipFunction.applyAsByte(valueForNoneA, b[i]);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zipFunction.applyAsByte(a[i], valueForNoneB);
            }
        }

        return result;
    }

    /**
     * Combines elements from three byte arrays using the provided zip function.
     * The operation stops when the shortest array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[] a = {1, 2, 3, 4};
     * byte[] b = {5, 6, 7};
     * byte[] c = {8, 9};
     * byte[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new array containing the results with length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[] zip(final byte[] a, final byte[] b, final byte[] c, final Throwables.ByteTernaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final byte[] result = new byte[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsByte(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     * Combines elements from three byte arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[] a = {1, 2, 3, 4};
     * byte[] b = {5, 6};
     * byte[] c = {8, 9, 10};
     * byte[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param valueForNoneC the default value to use when array 'c' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the byteest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[] zip(final byte[] a, final byte[] b, final byte[] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC, final Throwables.ByteTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final byte[] result = new byte[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsByte(a[i], b[i], c[i]);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zipFunction.applyAsByte(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 2D byte arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[][] a = {{1, 2}, {3, 4, 5}};
     * byte[][] b = {{5, 6, 7}, {8, 9}};
     * byte[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 2D array containing the results of zipping corresponding sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[][] zip(final byte[][] a, final byte[][] b, final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final byte[][] result = new byte[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 2D byte arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[][] a = {{1, 2}, {3, 4, 5}};
     * byte[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * byte[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the byteer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[][] zip(final byte[][] a, final byte[][] b, final byte valueForNoneA, final byte valueForNoneB,
            final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final byte[][] result = new byte[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 2D byte arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[][] a = {{1, 2}, {3, 4, 5}};
     * byte[][] b = {{5, 6, 7}, {8, 9}};
     * byte[][] c = {{10, 11}, {12, 13, 14}};
     * byte[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 2D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[][] zip(final byte[][] a, final byte[][] b, final byte[][] c, final Throwables.ByteTernaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final byte[][] result = new byte[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 2D byte arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[][] a = {{1, 2}, {3, 4, 5}};
     * byte[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * byte[][] c = {{10, 11}};
     * byte[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the byteest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[][] zip(final byte[][] a, final byte[][] b, final byte[][] c, final byte valueForNoneA, final byte valueForNoneB,
            final byte valueForNoneC, final Throwables.ByteTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final byte[][] result = new byte[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 3D byte arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * byte[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * byte[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 3D array containing the results of zipping corresponding 2D sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[][][] zip(final byte[][][] a, final byte[][][] b, final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final byte[][][] result = new byte[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 3D byte arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * byte[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * byte[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the byteer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[][][] zip(final byte[][][] a, final byte[][][] b, final byte valueForNoneA, final byte valueForNoneB,
            final Throwables.ByteBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final byte[][][] result = new byte[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 3D byte arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[][][] a = {{{1, 2}}};
     * byte[][][] b = {{{11, 12}, {13, 14}}};
     * byte[][][] c = {{{21, 22}}};
     * byte[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 3D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[][][] zip(final byte[][][] a, final byte[][][] b, final byte[][][] c,
            final Throwables.ByteTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final byte[][][] result = new byte[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 3D byte arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[][][] a = {{{1, 2}}};
     * byte[][][] b = {{{11, 12}, {13, 14}}};
     * byte[][][] c = {{{21, 22}}};
     * byte[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the byteest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> byte[][][] zip(final byte[][][] a, final byte[][][] b, final byte[][][] c, final byte valueForNoneA,
            final byte valueForNoneB, final byte valueForNoneC, final Throwables.ByteTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final byte[][][] result = new byte[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = minLen; i < maxLen; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    /**
     * Calculates the total count of elements in a two-dimensional byte array.
     * Null sub-arrays are treated as having zero elements.
     *
     * <pre>
     * byte[][] array = {{1, 2, 3}, null, {4, 5}};
     * long count = totalCountOfElements(array); // returns 5
     * </pre>
     *
     * @param a the two-dimensional byte array
     * @return the total count of all elements across all sub-arrays
     */
    public static long totalCountOfElements(final byte[][] a) {
        long count = 0;

        for (final byte[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total count of elements in a three-dimensional byte array.
     * Empty or null sub-arrays at any level are skipped.
     *
     * <pre>
     * byte[][][] array = {{{1, 2}, {3}}, {{4, 5, 6}}};
     * long count = totalCountOfElements(array); // returns 6
     * </pre>
     *
     * @param a the three-dimensional byte array
     * @return the total count of all elements across all sub-arrays
     */
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
     * Finds the minimum length among all sub-arrays in a two-dimensional byte array.
     * Null sub-arrays are treated as having length 0.
     * Returns 0 if the input array is null or empty.
     *
     * <pre>
     * byte[][] array = {{1, 2, 3}, {4, 5}, {6, 7, 8, 9}};
     * int minLen = minSubArrayLen(array); // returns 2
     * </pre>
     *
     * @param a the two-dimensional byte array to analyze
     * @return the minimum sub-array length, or 0 if the array is empty
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
     * Finds the maximum length among all sub-arrays in a two-dimensional byte array.
     * Null sub-arrays are treated as having length 0.
     * Returns 0 if the input array is null or empty.
     *
     * <pre>
     * byte[][] array = {{1, 2}, null, {3, 4, 5, 6}};
     * int maxLen = maxSubArrayLen(array); // returns 4
     * </pre>
     *
     * @param a the two-dimensional byte array to analyze
     * @return the maximum sub-array length, or 0 if the array is empty
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
     * Prints a one-dimensional byte array to the console and returns the printed string.
     * Handles null arrays and empty arrays appropriately.
     *
     * <pre>
     * byte[] array = {1, 2, 3};
     * String output = println(array); // prints and returns "[1, 2, 3]"
     * </pre>
     *
     * @param a the byte array to print
     * @return the string representation that was printed
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
     * Prints a two-dimensional byte array to the console with proper formatting.
     * Each sub-array is printed on the same line, separated by the ARRAY_PRINT_SEPARATOR.
     *
     * <pre>
     * byte[][] array = {{1, 2}, {3, 4, 5}};
     * String output = println(array); // prints formatted 2D array
     * </pre>
     *
     * @param a the two-dimensional byte array to print
     * @return the string representation that was printed
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
     * Prints a three-dimensional byte array to the console with multi-line formatting.
     * Sub-arrays at different levels are properly indented for readability.
     *
     * <pre>
     * byte[][][] array = {{{1, 2}, {3}}, {{4, 5}}};
     * String output = println(array); // prints formatted 3D array
     * </pre>
     *
     * @param a the three-dimensional byte array to print
     * @return the string representation that was printed
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
     * Updates all elements in a short array using the provided unary operator.
     * The operator is applied to each element and the result replaces the original value.
     *
     * <pre>
     * short[] array = {1, 2, 3};
     * updateAll(array, x -> (short)(x * x)); // array becomes {1, 4, 9}
     * </pre>
     *
     * @param <E> the type of exception the operator may throw
     * @param a the array to update
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Updates all elements in a two-dimensional short array using the provided unary operator.
     * The operator is applied to each element in all sub-arrays.
     *
     * <pre>
     * short[][] array = {{1, 2}, {3, 4}};
     * updateAll(array, x -> (short)(x + 10)); // array becomes {{11, 12}, {13, 14}}
     * </pre>
     *
     * @param <E> the type of exception the operator may throw
     * @param a the two-dimensional array to update
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Updates all elements in a three-dimensional short array using the provided unary operator.
     * The operator is applied recursively to all elements in all sub-arrays.
     *
     * <pre>
     * short[][][] array = {{{1, 2}}, {{3, 4}}};
     * updateAll(array, x -> (short)(x * 2)); // all elements doubled
     * </pre>
     *
     * @param <E> the type of exception the operator may throw
     * @param a the three-dimensional array to update
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Replaces elements in a short array that match the predicate with a new value.
     * Only elements for which the predicate returns true are replaced.
     *
     * <pre>
     * short[] array = {1, 2, 3, 4, 5};
     * replaceIf(array, x -> x > 3, (short)0); // array becomes {1, 2, 3, 0, 0}
     * </pre>
     *
     * @param <E> the type of exception the predicate may throw
     * @param a the array to modify
     * @param predicate the predicate to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * Replaces elements in a two-dimensional short array that match the predicate with a new value.
     * The predicate is applied to all elements in all sub-arrays.
     *
     * <pre>
     * short[][] array = {{1, 2}, {3, 4}};
     * replaceIf(array, x -> x % 2 == 0, (short)0); // array becomes {{1, 0}, {3, 0}}
     * </pre>
     *
     * @param <E> the type of exception the predicate may throw
     * @param a the two-dimensional array to modify
     * @param predicate the predicate to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * Replaces elements in a three-dimensional short array that match the predicate with a new value.
     * The predicate is applied recursively to all elements in all sub-arrays.
     *
     * <pre>
     * short[][][] array = {{{1, 2}}, {{3, 4}}};
     * replaceIf(array, x -> x < 3, (short)10); // replaces 1 and 2 with 10
     * </pre>
     *
     * @param <E> the type of exception the predicate may throw
     * @param a the three-dimensional array to modify
     * @param predicate the predicate to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * Reshapes a one-dimensional short array into a two-dimensional array with the specified number of columns.
     * The last row may have fewer elements if the array length is not evenly divisible by cols.
     *
     * <pre>
     * short[] array = {1, 2, 3, 4, 5};
     * short[][] reshaped = reshape(array, 2); // returns {{1, 2}, {3, 4}, {5}}
     * </pre>
     *
     * @param a the array to reshape
     * @param cols the number of columns in each row
     * @return a two-dimensional array with the specified column count
     * @throws IllegalArgumentException if cols is less than 1
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
     * Reshapes a one-dimensional short array into a three-dimensional array with the specified dimensions.
     * The array is divided into blocks of size rows×cols, with partial blocks allowed.
     *
     * <pre>
     * short[] array = {1, 2, 3, 4, 5, 6, 7, 8};
     * short[][][] reshaped = reshape(array, 2, 2); // returns {{{1,2},{3,4}}, {{5,6},{7,8}}}
     * </pre>
     *
     * @param a the array to reshape
     * @param rows the number of rows in each 2D block
     * @param cols the number of columns in each row
     * @return a three-dimensional array with the specified dimensions
     * @throws IllegalArgumentException if rows or cols is less than 1
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
     * Flattens a two-dimensional short array into a one-dimensional array.
     * All elements from all sub-arrays are combined into a single array in row-major order.
     * Null or empty sub-arrays are skipped.
     *
     * <pre>
     * short[][] array = {{1, 2}, {3, 4, 5}};
     * short[] flat = flatten(array); // returns {1, 2, 3, 4, 5}
     * </pre>
     *
     * @param a the two-dimensional array to flatten
     * @return a one-dimensional array containing all elements
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
     * Flattens a three-dimensional short array into a one-dimensional array.
     * All elements from all sub-arrays at all levels are combined into a single array.
     * Null or empty sub-arrays at any level are skipped.
     *
     * <pre>
     * short[][][] array = {{{1, 2}, {3}}, {{4, 5}}};
     * short[] flat = flatten(array); // returns {1, 2, 3, 4, 5}
     * </pre>
     *
     * @param a the three-dimensional array to flatten
     * @return a one-dimensional array containing all elements
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
     * Performs an operation on a flattened view of a two-dimensional array and writes the result back.
     * This method flattens the array, applies the operation, then copies the values back to their original positions.
     *
     * <pre>
     * short[][] array = {{3, 1}, {4, 2}};
     * flatOp(array, t -> Arrays.sort(t)); // sorts all elements across sub-arrays
     * </pre>
     *
     * @param <E> the type of exception the operation may throw
     * @param a the two-dimensional array to operate on
     * @param op the operation to perform on the flattened array
     * @throws E if the operation throws an exception
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
     * Performs an operation on a flattened view of a three-dimensional array and writes the result back.
     * This method flattens the array, applies the operation, then copies the values back to their original positions.
     *
     * <pre>
     * short[][][] array = {{{5, 2}}, {{3, 1}}};
     * flatOp(array, t -> Arrays.sort(t)); // sorts all elements across all sub-arrays
     * </pre>
     *
     * @param <E> the type of exception the operation may throw
     * @param a the three-dimensional array to operate on
     * @param op the operation to perform on the flattened array
     * @throws E if the operation throws an exception
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
     * Combines elements from two short arrays using the provided zip function.
     * The operation stops when the shorter array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[] a = {1, 2, 3, 4};
     * short[] b = {5, 6, 7};
     * short[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from both arrays
     * @return a new array containing the results of applying the zip function to corresponding elements
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[] zip(final short[] a, final short[] b, final Throwables.ShortBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final short[] result = new short[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsShort(a[i], b[i]);
        }

        return result;
    }

    /**
     * Combines elements from two short arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[] a = {1, 2, 3, 4};
     * short[] b = {5, 6};
     * short[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the shorter input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[] zip(final short[] a, final short[] b, final short valueForNoneA, final short valueForNoneB,
            final Throwables.ShortBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final short[] result = new short[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsShort(a[i], b[i]);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zipFunction.applyAsShort(valueForNoneA, b[i]);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zipFunction.applyAsShort(a[i], valueForNoneB);
            }
        }

        return result;
    }

    /**
     * Combines elements from three short arrays using the provided zip function.
     * The operation stops when the shortest array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[] a = {1, 2, 3, 4};
     * short[] b = {5, 6, 7};
     * short[] c = {8, 9};
     * short[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new array containing the results with length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[] zip(final short[] a, final short[] b, final short[] c, final Throwables.ShortTernaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final short[] result = new short[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsShort(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     * Combines elements from three short arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[] a = {1, 2, 3, 4};
     * short[] b = {5, 6};
     * short[] c = {8, 9, 10};
     * short[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param valueForNoneC the default value to use when array 'c' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[] zip(final short[] a, final short[] b, final short[] c, final short valueForNoneA, final short valueForNoneB,
            final short valueForNoneC, final Throwables.ShortTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final short[] result = new short[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsShort(a[i], b[i], c[i]);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zipFunction.applyAsShort(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 2D short arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[][] a = {{1, 2}, {3, 4, 5}};
     * short[][] b = {{5, 6, 7}, {8, 9}};
     * short[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 2D array containing the results of zipping corresponding sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[][] zip(final short[][] a, final short[][] b, final Throwables.ShortBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final short[][] result = new short[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 2D short arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[][] a = {{1, 2}, {3, 4, 5}};
     * short[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * short[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the shorter input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[][] zip(final short[][] a, final short[][] b, final short valueForNoneA, final short valueForNoneB,
            final Throwables.ShortBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final short[][] result = new short[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 2D short arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[][] a = {{1, 2}, {3, 4, 5}};
     * short[][] b = {{5, 6, 7}, {8, 9}};
     * short[][] c = {{10, 11}, {12, 13, 14}};
     * short[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 2D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[][] zip(final short[][] a, final short[][] b, final short[][] c,
            final Throwables.ShortTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final short[][] result = new short[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 2D short arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[][] a = {{1, 2}, {3, 4, 5}};
     * short[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * short[][] c = {{10, 11}};
     * short[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[][] zip(final short[][] a, final short[][] b, final short[][] c, final short valueForNoneA,
            final short valueForNoneB, final short valueForNoneC, final Throwables.ShortTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final short[][] result = new short[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 3D short arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * short[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * short[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 3D array containing the results of zipping corresponding 2D sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[][][] zip(final short[][][] a, final short[][][] b, final Throwables.ShortBinaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final short[][][] result = new short[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 3D short arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * short[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * short[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the shorter input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[][][] zip(final short[][][] a, final short[][][] b, final short valueForNoneA, final short valueForNoneB,
            final Throwables.ShortBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final short[][][] result = new short[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 3D short arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[][][] a = {{{1, 2}}};
     * short[][][] b = {{{11, 12}, {13, 14}}};
     * short[][][] c = {{{21, 22}}};
     * short[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 3D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[][][] zip(final short[][][] a, final short[][][] b, final short[][][] c,
            final Throwables.ShortTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final short[][][] result = new short[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 3D short arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * short[][][] a = {{{1, 2}}};
     * short[][][] b = {{{11, 12}, {13, 14}}};
     * short[][][] c = {{{21, 22}}};
     * short[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> short[][][] zip(final short[][][] a, final short[][][] b, final short[][][] c, final short valueForNoneA,
            final short valueForNoneB, final short valueForNoneC, final Throwables.ShortTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final short[][][] result = new short[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = minLen; i < maxLen; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    /**
     * Calculates the total number of elements in a 2D short array.
     * Handles null arrays and null sub-arrays gracefully.
     *
     * <p>Example usage:
     * <pre>
     * short[][] array = {{1, 2, 3}, {4, 5}, null, {6, 7, 8, 9}};
     * long count = totalCountOfElements(array);
     * // count will be 9 (3 + 2 + 0 + 4)
     * </pre></p>
     *
     * @param a the 2D array to count elements in
     * @return the total number of elements across all sub-arrays
     */
    public static long totalCountOfElements(final short[][] a) {
        long count = 0;

        for (final short[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of elements in a 3D short array.
     * Handles null arrays and null sub-arrays at all levels gracefully.
     *
     * @param a the 3D array to count elements in
     * @return the total number of elements across all sub-arrays
     */
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
     * Finds the minimum length among all sub-arrays in a 2D short array.
     * Returns 0 if the array is null or contains only null sub-arrays.
     *
     * <p>Example usage:
     * <pre>
     * short[][] array = {{1, 2, 3}, {4, 5}, {6, 7, 8, 9}};
     * int minLen = minSubArrayLen(array);
     * // minLen will be 2
     * </pre></p>
     *
     * @param a the 2D array to examine
     * @return the minimum length of any sub-array, or 0 if array is null
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
     * Finds the maximum length among all sub-arrays in a 2D short array.
     * Returns 0 if the array is null or empty.
     *
     * <p>Example usage:
     * <pre>
     * short[][] array = {{1, 2, 3}, {4, 5}, {6, 7, 8, 9}};
     * int maxLen = maxSubArrayLen(array);
     * // maxLen will be 4
     * </pre></p>
     *
     * @param a the 2D array to examine
     * @return the maximum length of any sub-array, or 0 if array is null or empty
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
     * Prints a 1D short array to the console in a formatted string representation.
     * Handles null arrays gracefully.
     *
     * <p>Example usage:
     * <pre>
     * short[] array = {1, 2, 3, 4, 5};
     * println(array);
     * // Prints: [1, 2, 3, 4, 5]
     * </pre></p>
     *
     * @param a the array to print
     * @return the string representation that was printed
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
     * Prints a 2D short array to the console in a formatted string representation.
     * Each sub-array is printed on a new line for clarity.
     *
     * <p>Example usage:
     * <pre>
     * short[][] array = {{1, 2}, {3, 4, 5}, {6}};
     * println(array);
     * // Prints formatted 2D array representation
     * </pre></p>
     *
     * @param a the 2D array to print
     * @return the string representation that was printed
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
     * Prints a 3D short array to the console in a formatted string representation.
     * Uses indentation and line breaks to show the array structure clearly.
     *
     * @param a the 3D array to print
     * @return the string representation that was printed
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
     * Updates all elements of the given array by applying the specified unary operator.
     *
     * <p>This method modifies the original array by applying the operator function to each element.
     * The operator can throw a checked exception of type E. If the array is null or empty,
     * the method returns without performing any operation.</p>
     *
     * <pre>
     * int[] arr = {1, 2, 3};
     * Arrays.updateAll(arr, x -> x * x);
     * // arr is now {1, 4, 9}
     * </pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param a the array to be modified
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Updates all elements of the given 2D array by applying the specified unary operator.
     *
     * <p>This method modifies the original 2D array by applying the operator function to each element
     * in all sub-arrays. The operator can throw a checked exception of type E.
     * If the array is null or empty, the method returns without performing any operation.</p>
     *
     * <pre>
     * int[][] arr = {{1, 2}, {3, 4}};
     * Arrays.updateAll(arr, x -> x + 10);
     * // arr is now {{11, 12}, {13, 14}}
     * </pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param a the 2D array to be modified
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Updates all elements of the given 3D array by applying the specified unary operator.
     *
     * <p>This method modifies the original 3D array by applying the operator function to each element
     * in all nested arrays. The operator can throw a checked exception of type E.
     * If the array is null or empty, the method returns without performing any operation.</p>
     *
     * <pre>
     * int[][][] arr = {{{1, 2}}, {{3, 4}}};
     * Arrays.updateAll(arr, x -> x * 2);
     * // arr is now {{{2, 4}}, {{6, 8}}}
     * </pre>
     *
     * @param <E> the type of exception that the operator may throw
     * @param a the 3D array to be modified
     * @param operator the unary operator to apply to each element
     * @throws E if the operator throws an exception
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
     * Replaces elements in the array that match the given predicate with the specified new value.
     *
     * <p>This method modifies the original array by testing each element with the predicate
     * and replacing matching elements with the new value. The predicate can throw a checked
     * exception of type E. If the array is null or empty, the method returns without performing any operation.</p>
     *
     * <pre>
     * int[] arr = {1, 2, 3, 4, 5};
     * Arrays.replaceIf(arr, x -> x % 2 == 0, 0);
     * // arr is now {1, 0, 3, 0, 5}
     * </pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param a the array to be modified
     * @param predicate the predicate to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * Replaces elements in the 2D array that match the given predicate with the specified new value.
     *
     * <p>This method modifies the original 2D array by testing each element in all sub-arrays
     * with the predicate and replacing matching elements with the new value.
     * If the array is null or empty, the method returns without performing any operation.</p>
     *
     * <pre>
     * int[][] arr = {{1, 2}, {3, 4}};
     * Arrays.replaceIf(arr, x -> x > 2, 10);
     * // arr is now {{1, 2}, {10, 10}}
     * </pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param a the 2D array to be modified
     * @param predicate the predicate to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * Replaces elements in the 3D array that match the given predicate with the specified new value.
     *
     * <p>This method modifies the original 3D array by testing each element in all nested arrays
     * with the predicate and replacing matching elements with the new value.
     * If the array is null or empty, the method returns without performing any operation.</p>
     *
     * <pre>
     * int[][][] arr = {{{1, 2}}, {{3, 4}}};
     * Arrays.replaceIf(arr, x -> x < 3, 0);
     * // arr is now {{{0, 0}}, {{3, 4}}}
     * </pre>
     *
     * @param <E> the type of exception that the predicate may throw
     * @param a the 3D array to be modified
     * @param predicate the predicate to test each element
     * @param newValue the value to replace matching elements with
     * @throws E if the predicate throws an exception
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
     * Reshapes a 1D array into a 2D array with the specified number of columns.
     *
     * <p>This method creates a new 2D array by dividing the input array into rows of the specified
     * column count. The last row may contain fewer elements if the array length is not evenly
     * divisible by the column count. If the input array is null or empty, returns an empty 2D array.</p>
     *
     * <pre>
     * int[] arr = {1, 2, 3, 4, 5, 6, 7};
     * int[][] result = Arrays.reshape(arr, 3);
     * // result is {{1, 2, 3}, {4, 5, 6}, {7}}
     * </pre>
     *
     * @param a the 1D array to reshape
     * @param cols the number of columns in each row
     * @return a new 2D array containing the reshaped data
     * @throws IllegalArgumentException if cols is less than 1
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
     * Reshapes a 1D array into a 3D array with the specified dimensions.
     *
     * <p>This method creates a new 3D array by dividing the input array first into matrices
     * of the specified row and column counts. Each matrix may be incomplete if the array length
     * is not evenly divisible by rows × cols. If the input array is null or empty, returns an empty 3D array.</p>
     *
     * <pre>
     * int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
     * int[][][] result = Arrays.reshape(arr, 2, 2);
     * // result is {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}}
     * </pre>
     *
     * @param a the 1D array to reshape
     * @param rows the number of rows in each 2D matrix
     * @param cols the number of columns in each row
     * @return a new 3D array containing the reshaped data
     * @throws IllegalArgumentException if rows or cols is less than 1
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
     * Flattens a 2D array into a 1D array.
     *
     * <p>This method creates a new 1D array containing all elements from the 2D array
     * in row-major order. Empty sub-arrays are skipped. If the input array is null or empty,
     * returns an empty array.</p>
     *
     * <pre>
     * int[][] arr = {{1, 2, 3}, {4, 5}, {6, 7, 8}};
     * int[] result = Arrays.flatten(arr);
     * // result is {1, 2, 3, 4, 5, 6, 7, 8}
     * </pre>
     *
     * @param a the 2D array to flatten
     * @return a new 1D array containing all elements from the input array
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
     * Flattens a 3D array into a 1D array.
     *
     * <p>This method creates a new 1D array containing all elements from the 3D array
     * in depth-first order. Empty sub-arrays at any level are skipped. If the input array
     * is null or empty, returns an empty array.</p>
     *
     * <pre>
     * int[][][] arr = {{{1, 2}, {3}}, {{4, 5, 6}}};
     * int[] result = Arrays.flatten(arr);
     * // result is {1, 2, 3, 4, 5, 6}
     * </pre>
     *
     * @param a the 3D array to flatten
     * @return a new 1D array containing all elements from the input array
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
     * Flattens a 2D array, applies the given operation, and updates the original array with the results.
     *
     * <p>This method provides a convenient way to apply operations that work on 1D arrays to 2D arrays.
     * The array is flattened, the operation is applied to the flattened array, and then the values
     * are copied back to the original 2D array structure.</p>
     *
     * <pre>
     * int[][] arr = {{3, 1, 4}, {1, 5, 9}};
     * Arrays.flatOp(arr, t -> Arrays.sort(t));
     * // arr is now {{1, 1, 3}, {4, 5, 9}}
     * </pre>
     *
     * @param <E> the type of exception that the operation may throw
     * @param a the 2D array to process
     * @param op the operation to apply to the flattened array
     * @throws E if the operation throws an exception
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
     * Flattens a 3D array, applies the given operation, and updates the original array with the results.
     *
     * <p>This method provides a convenient way to apply operations that work on 1D arrays to 3D arrays.
     * The array is flattened, the operation is applied to the flattened array, and then the values
     * are copied back to the original 3D array structure.</p>
     *
     * <pre>
     * int[][][] arr = {{{5, 2}}, {{8, 1}}};
     * Arrays.flatOp(arr, t -> Arrays.sort(t));
     * // arr is now {{{1, 2}}, {{5, 8}}}
     * </pre>
     *
     * @param <E> the type of exception that the operation may throw
     * @param a the 3D array to process
     * @param op the operation to apply to the flattened array
     * @throws E if the operation throws an exception
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
     * Combines elements from two int arrays using the provided zip function.
     * The operation stops when the shorter array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[] a = {1, 2, 3, 4};
     * int[] b = {5, 6, 7};
     * int[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from both arrays
     * @return a new array containing the results of applying the zip function to corresponding elements
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[] zip(final int[] a, final int[] b, final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final int[] result = new int[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsInt(a[i], b[i]);
        }

        return result;
    }

    /**
     * Combines elements from two int arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[] a = {1, 2, 3, 4};
     * int[] b = {5, 6};
     * int[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the inter input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[] zip(final int[] a, final int[] b, final int valueForNoneA, final int valueForNoneB,
            final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final int[] result = new int[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsInt(a[i], b[i]);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zipFunction.applyAsInt(valueForNoneA, b[i]);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zipFunction.applyAsInt(a[i], valueForNoneB);
            }
        }

        return result;
    }

    /**
     * Combines elements from three int arrays using the provided zip function.
     * The operation stops when the shortest array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[] a = {1, 2, 3, 4};
     * int[] b = {5, 6, 7};
     * int[] c = {8, 9};
     * int[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new array containing the results with length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[] zip(final int[] a, final int[] b, final int[] c, final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final int[] result = new int[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsInt(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     * Combines elements from three int arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[] a = {1, 2, 3, 4};
     * int[] b = {5, 6};
     * int[] c = {8, 9, 10};
     * int[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param valueForNoneC the default value to use when array 'c' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the intest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[] zip(final int[] a, final int[] b, final int[] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC, final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final int[] result = new int[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsInt(a[i], b[i], c[i]);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zipFunction.applyAsInt(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 2D int arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[][] a = {{1, 2}, {3, 4, 5}};
     * int[][] b = {{5, 6, 7}, {8, 9}};
     * int[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 2D array containing the results of zipping corresponding sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[][] zip(final int[][] a, final int[][] b, final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final int[][] result = new int[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 2D int arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[][] a = {{1, 2}, {3, 4, 5}};
     * int[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * int[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the inter input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[][] zip(final int[][] a, final int[][] b, final int valueForNoneA, final int valueForNoneB,
            final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final int[][] result = new int[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 2D int arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[][] a = {{1, 2}, {3, 4, 5}};
     * int[][] b = {{5, 6, 7}, {8, 9}};
     * int[][] c = {{10, 11}, {12, 13, 14}};
     * int[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 2D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[][] zip(final int[][] a, final int[][] b, final int[][] c, final Throwables.IntTernaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final int[][] result = new int[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 2D int arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[][] a = {{1, 2}, {3, 4, 5}};
     * int[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * int[][] c = {{10, 11}};
     * int[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the intest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[][] zip(final int[][] a, final int[][] b, final int[][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC, final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final int[][] result = new int[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 3D int arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * int[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * int[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 3D array containing the results of zipping corresponding 2D sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[][][] zip(final int[][][] a, final int[][][] b, final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final int[][][] result = new int[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 3D int arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * int[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * int[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the inter input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[][][] zip(final int[][][] a, final int[][][] b, final int valueForNoneA, final int valueForNoneB,
            final Throwables.IntBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final int[][][] result = new int[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 3D int arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[][][] a = {{{1, 2}}};
     * int[][][] b = {{{11, 12}, {13, 14}}};
     * int[][][] c = {{{21, 22}}};
     * int[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 3D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[][][] zip(final int[][][] a, final int[][][] b, final int[][][] c,
            final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final int[][][] result = new int[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 3D int arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * int[][][] a = {{{1, 2}}};
     * int[][][] b = {{{11, 12}, {13, 14}}};
     * int[][][] c = {{{21, 22}}};
     * int[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the intest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> int[][][] zip(final int[][][] a, final int[][][] b, final int[][][] c, final int valueForNoneA, final int valueForNoneB,
            final int valueForNoneC, final Throwables.IntTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final int[][][] result = new int[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = minLen; i < maxLen; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    /**
     * Calculates the total number of integer elements in a 2D array.
     * It handles null or empty sub-arrays gracefully.
     *
     * <p>Example usage:
     * <pre>
     * int[][] a = {{1, 2}, {3, 4, 5}, null, {}};
     * long count = totalCountOfElements(a);
     * // count will be 5
     * </pre>
     *
     * @param a The 2D integer array.
     * @return The total count of integer elements.
     */
    public static long totalCountOfElements(final int[][] a) {
        long count = 0;

        for (final int[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of integer elements in a 3D array.
     * It safely handles null or empty sub-arrays at any depth.
     *
     * <p>Example usage:
     * <pre>
     * int[][][] a = {{{1}, {2, 3}}, null, {{{4, 5, 6}}}};
     * long count = totalCountOfElements(a);
     * // count will be 6
     * </pre>
     *
     * @param a The 3D integer array.
     * @return The total count of integer elements.
     */
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
     * Finds the minimum length among all sub-arrays in a 2D integer array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p>Example usage:
     * <pre>
     * int[][] a = {{1, 2, 3}, {4, 5}, null, {6}};
     * int minLen = minSubArrayLen(a);
     * // minLen will be 0
     * </pre>
     *
     * @param a The 2D integer array.
     * @return The minimum length of a sub-array, or 0 if the input array is null or empty.
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
     * Finds the maximum length among all sub-arrays in a 2D integer array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p>Example usage:
     * <pre>
     * int[][] a = {{1}, {2, 3}, null, {4, 5, 6}};
     * int maxLen = maxSubArrayLen(a);
     * // maxLen will be 3
     * </pre>
     *
     * @param a The 2D integer array.
     * @return The maximum length of a sub-array, or 0 if the input array is null or empty.
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
     * Prints a string representation of an integer array to the console and returns the string.
     * Handles null and empty arrays.
     *
     * <p>Example usage:
     * <pre>
     * int[] a = {1, 2, 3};
     * String s = println(a);
     * // Prints "[1, 2, 3]" to console and s is "[1, 2, 3]"
     * </pre>
     *
     * @param a The integer array to print.
     * @return The string representation of the array.
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
     * Prints a string representation of a 2D integer array to the console and returns the string.
     * Handles null and empty arrays/sub-arrays.
     *
     * <p>Example usage:
     * <pre>
     * int[][] a = {{1, 2}, {3, 4}};
     * String s = println(a);
     * // Prints "[[1, 2], [3, 4]]" to console and s is "[[1, 2], [3, 4]]"
     * </pre>
     *
     * @param a The 2D integer array to print.
     * @return The string representation of the 2D array.
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
     * Prints a string representation of a 3D integer array to the console and returns the string.
     * Handles null and empty arrays/sub-arrays.
     *
     * <p>Example usage:
     * <pre>
     * int[][][] a = {{{1, 2}}, {{3, 4}}};
     * String s = println(a);
     * // Prints a formatted string representation to console and returns it.
     * </pre>
     *
     * @param a The 3D integer array to print.
     * @return The string representation of the 3D array.
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
     * Updates each element of the specified long array in-place by applying a unary operator.
     *
     * <p>Example usage:
     * <pre>
     * long[] array = {1L, 2L, 3L};
     * updateAll(array, x -> x * x);
     * // array is now {1L, 4L, 9L}
     * </pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The array to be modified. The modification happens in-place.
     * @param operator The unary operator to apply to each element.
     * @throws E If the operator throws an exception.
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
     * Updates each element of the specified 2D long array in-place by applying a unary operator.
     *
     * <p>Example usage:
     * <pre>
     * long[][] array = {{1L, 2L}, {3L, 4L}};
     * updateAll(array, x -> -x);
     * // array is now {{-1L, -2L}, {-3L, -4L}}
     * </pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The 2D array to be modified. The modification happens in-place.
     * @param operator The unary operator to apply to each element.
     * @throws E If the operator throws an exception.
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
     * Updates each element of the specified 3D long array in-place by applying a unary operator.
     *
     * <p>Example usage:
     * <pre>
     * long[][][] array = {{{1L}, {2L}}, {{3L}, {4L}}};
     * updateAll(array, x -> x + 1);
     * // array is now {{{2L}, {3L}}, {{4L}, {5L}}}
     * </pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The 3D array to be modified. The modification happens in-place.
     * @param operator The unary operator to apply to each element.
     * @throws E If the operator throws an exception.
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
     * Replaces each element of a long array with a new value if it satisfies a given predicate.
     *
     * <p>Example usage:
     * <pre>
     * long[] array = {1L, 2L, 3L, 4L, 5L};
     * replaceIf(array, x -> x % 2 == 0, 0L);
     * // array is now {1L, 0L, 3L, 0L, 5L}
     * </pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The array to be modified.
     * @param predicate The condition to test for each element.
     * @param newValue The value to replace with if the predicate is true.
     * @throws E If the predicate throws an exception.
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
     * Replaces each element of a 2D long array with a new value if it satisfies a given predicate.
     *
     * <p>Example usage:
     * <pre>
     * long[][] array = {{1L, -2L}, {3L, -4L}};
     * replaceIf(array, x -> x < 0, 0L);
     * // array is now {{1L, 0L}, {3L, 0L}}
     * </pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The 2D array to be modified.
     * @param predicate The condition to test for each element.
     * @param newValue The value to replace with if the predicate is true.
     * @throws E If the predicate throws an exception.
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
     * Replaces each element of a 3D long array with a new value if it satisfies a given predicate.
     *
     * <p>Example usage:
     * <pre>
     * long[][][] array = {{{1L, 2L}}, {{-3L, 4L}}};
     * replaceIf(array, x -> x < 0, 99L);
     * // array is now {{{1L, 2L}}, {{99L, 4L}}}
     * </pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The 3D array to be modified.
     * @param predicate The condition to test for each element.
     * @param newValue The value to replace with if the predicate is true.
     * @throws E If the predicate throws an exception.
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
     * Reshapes a 1D long array into a 2D long array with a specified number of columns.
     * The last row may be shorter if the total number of elements is not a multiple of {@code cols}.
     *
     * <p>Example usage:
     * <pre>
     * long[] array = {1, 2, 3, 4, 5, 6, 7};
     * long[][] reshaped = reshape(array, 3);
     * // reshaped is {{1, 2, 3}, {4, 5, 6}, {7}}
     * </pre>
     *
     * @param a The 1D array to reshape.
     * @param cols The number of columns in the resulting 2D array.
     * @return A new 2D long array.
     * @throws IllegalArgumentException if {@code cols} is not positive.
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
     * Reshapes a 1D long array into a 3D long array with a specified number of rows and columns.
     * The last sub-arrays may be shorter if the total element count is not perfectly divisible.
     *
     * <p>Example usage:
     * <pre>
     * long[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
     * long[][][] reshaped = reshape(array, 2, 2);
     * // reshaped is {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}, {{9}}}
     * </pre>
     *
     * @param a The 1D array to reshape.
     * @param rows The number of rows in each 2D sub-array.
     * @param cols The number of columns in each 2D sub-array.
     * @return A new 3D long array.
     * @throws IllegalArgumentException if {@code rows} or {@code cols} are not positive.
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
     * Flattens a 2D long array into a 1D long array by concatenating its rows.
     *
     * <p>Example usage:
     * <pre>
     * long[][] array = {{1L, 2L}, {3L, 4L, 5L}};
     * long[] flattened = flatten(array);
     * // flattened is {1L, 2L, 3L, 4L, 5L}
     * </pre>
     *
     * @param a The 2D array to flatten.
     * @return A new 1D long array containing all elements from the input.
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
     * Flattens a 3D long array into a 1D long array by concatenating its elements in order.
     *
     * <p>Example usage:
     * <pre>
     * long[][][] array = {{{1L, 2L}}, {{3L, 4L}, {5L}}};
     * long[] flattened = flatten(array);
     * // flattened is {1L, 2L, 3L, 4L, 5L}
     * </pre>
     *
     * @param a The 3D array to flatten.
     * @return A new 1D long array containing all elements from the input.
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
     * Flattens a 2D array, performs an in-place operation on the resulting 1D array, and then
     * copies the modified elements back into the original 2D array.
     *
     * <p>Example usage:
     * <pre>
     * long[][] array = {{3L, 1L}, {4L, 2L}};
     * flatOp(array, t -> Arrays.sort(t));
     * // array is now {{1L, 2L}, {3L, 4L}}
     * </pre>
     *
     * @param <E> The type of exception that the operation may throw.
     * @param a The 2D array to operate on.
     * @param op The consumer to accept the flattened 1D array for modification.
     * @throws E If the operation throws an exception.
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
     * Flattens a 3D array, performs an in-place operation on the resulting 1D array, and then
     * copies the modified elements back into the original 3D array.
     *
     * <p>Example usage:
     * <pre>
     * long[][][] array = {{{3L}, {1L}}, {{4L, 2L}}};
     * flatOp(array, t -> Arrays.sort(t));
     * // array is now {{{1L}, {2L}}, {{3L, 4L}}}
     * </pre>
     *
     * @param <E> The type of exception that the operation may throw.
     * @param a The 3D array to operate on.
     * @param op The consumer to accept the flattened 1D array for modification.
     * @throws E If the operation throws an exception.
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
     * Combines elements from two long arrays using the provided zip function.
     * The operation stops when the shorter array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[] a = {1, 2, 3, 4};
     * long[] b = {5, 6, 7};
     * long[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from both arrays
     * @return a new array containing the results of applying the zip function to corresponding elements
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[] zip(final long[] a, final long[] b, final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final long[] result = new long[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsLong(a[i], b[i]);
        }

        return result;
    }

    /**
     * Combines elements from two long arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[] a = {1, 2, 3, 4};
     * long[] b = {5, 6};
     * long[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the longer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[] zip(final long[] a, final long[] b, final long valueForNoneA, final long valueForNoneB,
            final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final long[] result = new long[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsLong(a[i], b[i]);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zipFunction.applyAsLong(valueForNoneA, b[i]);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zipFunction.applyAsLong(a[i], valueForNoneB);
            }
        }

        return result;
    }

    /**
     * Combines elements from three long arrays using the provided zip function.
     * The operation stops when the shortest array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[] a = {1, 2, 3, 4};
     * long[] b = {5, 6, 7};
     * long[] c = {8, 9};
     * long[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new array containing the results with length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[] zip(final long[] a, final long[] b, final long[] c, final Throwables.LongTernaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final long[] result = new long[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsLong(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     * Combines elements from three long arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[] a = {1, 2, 3, 4};
     * long[] b = {5, 6};
     * long[] c = {8, 9, 10};
     * long[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param valueForNoneC the default value to use when array 'c' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the longest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[] zip(final long[] a, final long[] b, final long[] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC, final Throwables.LongTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final long[] result = new long[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsLong(a[i], b[i], c[i]);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zipFunction.applyAsLong(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 2D long arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[][] a = {{1, 2}, {3, 4, 5}};
     * long[][] b = {{5, 6, 7}, {8, 9}};
     * long[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 2D array containing the results of zipping corresponding sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[][] zip(final long[][] a, final long[][] b, final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final long[][] result = new long[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 2D long arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[][] a = {{1, 2}, {3, 4, 5}};
     * long[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * long[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the longer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[][] zip(final long[][] a, final long[][] b, final long valueForNoneA, final long valueForNoneB,
            final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final long[][] result = new long[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 2D long arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[][] a = {{1, 2}, {3, 4, 5}};
     * long[][] b = {{5, 6, 7}, {8, 9}};
     * long[][] c = {{10, 11}, {12, 13, 14}};
     * long[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 2D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[][] zip(final long[][] a, final long[][] b, final long[][] c, final Throwables.LongTernaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final long[][] result = new long[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 2D long arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[][] a = {{1, 2}, {3, 4, 5}};
     * long[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * long[][] c = {{10, 11}};
     * long[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the longest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[][] zip(final long[][] a, final long[][] b, final long[][] c, final long valueForNoneA, final long valueForNoneB,
            final long valueForNoneC, final Throwables.LongTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final long[][] result = new long[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 3D long arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * long[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * long[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 3D array containing the results of zipping corresponding 2D sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[][][] zip(final long[][][] a, final long[][][] b, final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final long[][][] result = new long[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 3D long arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * long[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * long[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the longer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[][][] zip(final long[][][] a, final long[][][] b, final long valueForNoneA, final long valueForNoneB,
            final Throwables.LongBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final long[][][] result = new long[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 3D long arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[][][] a = {{{1, 2}}};
     * long[][][] b = {{{11, 12}, {13, 14}}};
     * long[][][] c = {{{21, 22}}};
     * long[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 3D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[][][] zip(final long[][][] a, final long[][][] b, final long[][][] c,
            final Throwables.LongTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final long[][][] result = new long[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 3D long arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * long[][][] a = {{{1, 2}}};
     * long[][][] b = {{{11, 12}, {13, 14}}};
     * long[][][] c = {{{21, 22}}};
     * long[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the longest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> long[][][] zip(final long[][][] a, final long[][][] b, final long[][][] c, final long valueForNoneA,
            final long valueForNoneB, final long valueForNoneC, final Throwables.LongTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final long[][][] result = new long[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = minLen; i < maxLen; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    /**
     * Calculates the total number of long elements in a 2D array, handling null sub-arrays.
     *
     * <p>Example usage:
     * <pre>
     * long[][] array = {{1, 2}, {3, 4, 5}, null};
     * long count = totalCountOfElements(array);
     * // count is 5
     * </pre>
     *
     * @param a The 2D array to inspect.
     * @return The total count of long elements.
     */
    public static long totalCountOfElements(final long[][] a) {
        long count = 0;

        for (final long[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of long elements in a 3D array, handling null sub-arrays.
     *
     * <p>Example usage:
     * <pre>
     * long[][][] array = {{{1}, {2, 3}}, null, {{{4, 5, 6}}}};
     * long count = totalCountOfElements(array);
     * // count is 6
     * </pre>
     *
     * @param a The 3D array to inspect.
     * @return The total count of long elements.
     */
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
     * Finds the minimum length among all sub-arrays in a 2D long array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p>Example usage:
     * <pre>
     * long[][] a = {{1L, 2L, 3L}, {4L, 5L}, null, {6L}};
     * int minLen = minSubArrayLen(a);
     * // minLen will be 0
     * </pre>
     *
     * @param a The 2D long array.
     * @return The minimum length of a sub-array, or 0 if the input array is null or empty.
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
     * Finds the maximum length among all sub-arrays in a 2D long array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p>Example usage:
     * <pre>
     * long[][] a = {{1L}, {2L, 3L}, null, {4L, 5L, 6L}};
     * int maxLen = maxSubArrayLen(a);
     * // maxLen will be 3
     * </pre>
     *
     * @param a The 2D long array.
     * @return The maximum length of a sub-array, or 0 if the input array is null or empty.
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
     * Prints a string representation of a long array to the console and returns the string.
     *
     * <p>Example usage:
     * <pre>
     * long[] a = {1L, 2L, 3L};
     * String s = println(a);
     * // Prints "[1, 2, 3]" to console and returns it.
     * </pre>
     *
     * @param a The long array to print.
     * @return The string representation of the array.
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
     * Prints a string representation of a 2D long array to the console and returns the string.
     *
     * <p>Example usage:
     * <pre>
     * long[][] a = {{1L, 2L}, {3L, 4L}};
     * String s = println(a);
     * // Prints "[[1, 2], [3, 4]]" to console and returns it.
     * </pre>
     *
     * @param a The 2D long array to print.
     * @return The string representation of the 2D array.
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
     * Prints a string representation of a 3D long array to the console and returns the string.
     *
     * <p>Example usage:
     * <pre>
     * long[][][] a = {{{1L, 2L}}, {{3L, 4L}}};
     * String s = println(a);
     * // Prints a formatted string to console and returns it.
     * </pre>
     *
     * @param a The 3D long array to print.
     * @return The string representation of the 3D array.
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
     * Updates each element of the specified float array in-place by applying a given unary operator.
     *
     * <pre><code>
     * float[] array = {1.0f, -2.0f, 3.0f};
     * Arrays.updateAll(array, x -> Math.abs(x)); // array becomes {1.0f, 2.0f, 3.0f}
     * </code></pre>
     *
     * @param <E> the type of exception that can be thrown by the operator.
     * @param a the array to be updated. If null or empty, the method returns immediately.
     * @param operator the unary operator to apply to each element.
     * @throws E if the operator throws an exception.
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
     * Updates each element of the specified 2D float array in-place by applying a given unary operator.
     *
     * <pre><code>
     * float[][] matrix = {{1.0f, -2.0f}, {-3.0f, 4.0f}};
     * Arrays.updateAll(matrix, x -> x * x); // matrix becomes {{1.0f, 4.0f}, {9.0f, 16.0f}}
     * </code></pre>
     *
     * @param <E> the type of exception that can be thrown by the operator.
     * @param a the 2D array to be updated. If null or empty, the method returns immediately.
     * @param operator the unary operator to apply to each element.
     * @throws E if the operator throws an exception.
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
     * Updates each element of the specified 3D float array in-place by applying a given unary operator.
     *
     * <pre><code>
     * float[][][] cube = {{{-1.0f}}, {{2.0f}}};
     * Arrays.updateAll(cube, x -> -x); // cube becomes {{{1.0f}}, {{-2.0f}}}
     * </code></pre>
     *
     * @param <E> the type of exception that can be thrown by the operator.
     * @param a the 3D array to be updated. If null or empty, the method returns immediately.
     * @param operator the unary operator to apply to each element.
     * @throws E if the operator throws an exception.
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
     * Replaces each element of a float array with the specified new value if it satisfies the given predicate.
     * The modification is done in-place.
     *
     * <pre><code>
     * float[] array = {1.0f, -2.0f, 3.0f, -4.0f};
     * Arrays.replaceIf(array, x -> x < 0, 0.0f); // array becomes {1.0f, 0.0f, 3.0f, 0.0f}
     * </code></pre>
     *
     * @param <E> the type of exception that can be thrown by the predicate.
     * @param a the array to be modified.
     * @param predicate the condition to test for each element.
     * @param newValue the value to be placed in the array if the predicate is true.
     * @throws E if the predicate throws an exception.
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
     * Replaces each element of a 2D float array with the specified new value if it satisfies the given predicate.
     * The modification is done in-place.
     *
     * <pre><code>
     * float[][] matrix = {{1.0f, -2.0f}, {0.0f, -4.0f}};
     * Arrays.replaceIf(matrix, x -> x <= 0, 99.0f); // matrix becomes {{1.0f, 99.0f}, {99.0f, 99.0f}}
     * </code></pre>
     *
     * @param <E> the type of exception that can be thrown by the predicate.
     * @param a the 2D array to be modified.
     * @param predicate the condition to test for each element.
     * @param newValue the value to be placed in the array if the predicate is true.
     * @throws E if the predicate throws an exception.
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
     * Replaces each element of a 3D float array with the specified new value if it satisfies the given predicate.
     * The modification is done in-place.
     *
     * <pre><code>
     * float[][][] cube = {{{1f, -2f}}, {{-3f, 4f}}};
     * Arrays.replaceIf(cube, x -> x > 0, 0.0f); // cube becomes {{{0.0f, -2.0f}}, {{-3.0f, 0.0f}}}
     * </code></pre>
     *
     * @param <E> the type of exception that can be thrown by the predicate.
     * @param a the 3D array to be modified.
     * @param predicate the condition to test for each element.
     * @param newValue the value to be placed in the array if the predicate is true.
     * @throws E if the predicate throws an exception.
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
     * Reshapes a 1D float array into a 2D float array with the specified number of columns.
     * The last row of the resulting 2D array may have fewer elements if the length of the
     * input array is not a multiple of {@code cols}.
     *
     * <pre><code>
     * float[] array = {1, 2, 3, 4, 5, 6, 7};
     * float[][] matrix = Arrays.reshape(array, 3); // returns {{1, 2, 3}, {4, 5, 6}, {7}}
     * </code></pre>
     *
     * @param a the 1D array to reshape.
     * @param cols the number of columns in the new 2D array.
     * @return a new 2D array containing the elements of the input array.
     * @throws IllegalArgumentException if {@code cols} is not positive.
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
     * Reshapes a 1D float array into a 3D float array with the specified number of rows and columns.
     * The last sub-array may be smaller if the total number of elements is not a multiple
     * of {@code rows * cols}.
     *
     * <pre><code>
     * float[] array = {1, 2, 3, 4, 5, 6, 7};
     * float[][][] cube = Arrays.reshape(array, 2, 2); // returns {{{1,2},{3,4}},{{5,6},{7}}}
     * </code></pre>
     *
     * @param a the 1D array to reshape.
     * @param rows the number of rows in each 2D sub-array.
     * @param cols the number of columns in each 2D sub-array.
     * @return a new 3D array containing the elements of the input array.
     * @throws IllegalArgumentException if {@code rows} or {@code cols} are not positive.
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
     * Flattens a 2D float array into a new 1D float array.
     * This method concatenates all sub-arrays into a single array.
     * Null or empty sub-arrays are skipped.
     *
     * <pre><code>
     * float[][] matrix = {{1.0f, 2.0f}, {3.0f, 4.0f}};
     * float[] array = Arrays.flatten(matrix); // returns {1.0f, 2.0f, 3.0f, 4.0f}
     * </code></pre>
     *
     * @param a the 2D array to flatten.
     * @return a new 1D array containing all elements from the 2D array.
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
     * Flattens a 3D float array into a new 1D float array.
     * This method concatenates all innermost sub-arrays into a single array.
     * Null or empty sub-arrays at any level are skipped.
     *
     * <pre><code>
     * float[][][] cube = {{{1.0f}, {2.0f}}, {{3.0f}, {4.0f}}};
     * float[] array = Arrays.flatten(cube); // returns {1.0f, 2.0f, 3.0f, 4.0f}
     * </code></pre>
     *
     * @param a the 3D array to flatten.
     * @return a new 1D array containing all elements from the 3D array.
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
     * Flattens a 2D array, performs a specified operation on the resulting 1D array,
     * and then copies the modified elements back into the original 2D array in-place.
     * This is useful for applying operations like sorting to the entire set of elements
     * in a multi-dimensional array.
     *
     * <pre><code>
     * float[][] matrix = {{4.0f, 1.0f}, {3.0f, 2.0f}};
     * Arrays.flatOp(matrix, N::sort); // matrix becomes {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * </code></pre>
     *
     * @param <E> the type of exception that can be thrown by the operation.
     * @param a the 2D array to operate on.
     * @param op the operation to perform on the flattened array.
     * @throws E if the operation throws an exception.
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
     * Flattens a 3D array, performs a specified operation on the resulting 1D array,
     * and then copies the modified elements back into the original 3D array in-place.
     * This is useful for applying operations like sorting to the entire set of elements
     * in a multi-dimensional array.
     *
     * <pre><code>
     * float[][][] cube = {{{4.0f, 1.0f}}, {{3.0f, 2.0f}}};
     * Arrays.flatOp(cube, N::sort); // cube becomes {{{1.0f, 2.0f}}, {{3.0f, 4.0f}}}
     * </code></pre>
     *
     * @param <E> the type of exception that can be thrown by the operation.
     * @param a the 3D array to operate on.
     * @param op the operation to perform on the flattened array.
     * @throws E if the operation throws an exception.
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
     * Combines elements from two float arrays using the provided zip function.
     * The operation stops when the shorter array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[] a = {1, 2, 3, 4};
     * float[] b = {5, 6, 7};
     * float[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from both arrays
     * @return a new array containing the results of applying the zip function to corresponding elements
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[] zip(final float[] a, final float[] b, final Throwables.FloatBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final float[] result = new float[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsFloat(a[i], b[i]);
        }

        return result;
    }

    /**
     * Combines elements from two float arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[] a = {1, 2, 3, 4};
     * float[] b = {5, 6};
     * float[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the floater input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[] zip(final float[] a, final float[] b, final float valueForNoneA, final float valueForNoneB,
            final Throwables.FloatBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final float[] result = new float[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsFloat(a[i], b[i]);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zipFunction.applyAsFloat(valueForNoneA, b[i]);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zipFunction.applyAsFloat(a[i], valueForNoneB);
            }
        }

        return result;
    }

    /**
     * Combines elements from three float arrays using the provided zip function.
     * The operation stops when the shortest array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[] a = {1, 2, 3, 4};
     * float[] b = {5, 6, 7};
     * float[] c = {8, 9};
     * float[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new array containing the results with length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[] zip(final float[] a, final float[] b, final float[] c, final Throwables.FloatTernaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final float[] result = new float[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsFloat(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     * Combines elements from three float arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[] a = {1, 2, 3, 4};
     * float[] b = {5, 6};
     * float[] c = {8, 9, 10};
     * float[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param valueForNoneC the default value to use when array 'c' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the floatest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[] zip(final float[] a, final float[] b, final float[] c, final float valueForNoneA, final float valueForNoneB,
            final float valueForNoneC, final Throwables.FloatTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final float[] result = new float[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsFloat(a[i], b[i], c[i]);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zipFunction.applyAsFloat(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 2D float arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[][] a = {{1, 2}, {3, 4, 5}};
     * float[][] b = {{5, 6, 7}, {8, 9}};
     * float[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 2D array containing the results of zipping corresponding sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[][] zip(final float[][] a, final float[][] b, final Throwables.FloatBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final float[][] result = new float[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 2D float arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[][] a = {{1, 2}, {3, 4, 5}};
     * float[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * float[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the floater input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[][] zip(final float[][] a, final float[][] b, final float valueForNoneA, final float valueForNoneB,
            final Throwables.FloatBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final float[][] result = new float[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 2D float arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[][] a = {{1, 2}, {3, 4, 5}};
     * float[][] b = {{5, 6, 7}, {8, 9}};
     * float[][] c = {{10, 11}, {12, 13, 14}};
     * float[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 2D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[][] zip(final float[][] a, final float[][] b, final float[][] c,
            final Throwables.FloatTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final float[][] result = new float[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 2D float arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[][] a = {{1, 2}, {3, 4, 5}};
     * float[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * float[][] c = {{10, 11}};
     * float[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the floatest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[][] zip(final float[][] a, final float[][] b, final float[][] c, final float valueForNoneA,
            final float valueForNoneB, final float valueForNoneC, final Throwables.FloatTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final float[][] result = new float[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 3D float arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * float[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * float[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 3D array containing the results of zipping corresponding 2D sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[][][] zip(final float[][][] a, final float[][][] b, final Throwables.FloatBinaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final float[][][] result = new float[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 3D float arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * float[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * float[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the floater input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[][][] zip(final float[][][] a, final float[][][] b, final float valueForNoneA, final float valueForNoneB,
            final Throwables.FloatBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final float[][][] result = new float[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 3D float arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[][][] a = {{{1, 2}}};
     * float[][][] b = {{{11, 12}, {13, 14}}};
     * float[][][] c = {{{21, 22}}};
     * float[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 3D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[][][] zip(final float[][][] a, final float[][][] b, final float[][][] c,
            final Throwables.FloatTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final float[][][] result = new float[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 3D float arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[][][] a = {{{1, 2}}};
     * float[][][] b = {{{11, 12}, {13, 14}}};
     * float[][][] c = {{{21, 22}}};
     * float[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the floatest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> float[][][] zip(final float[][][] a, final float[][][] b, final float[][][] c, final float valueForNoneA,
            final float valueForNoneB, final float valueForNoneC, final Throwables.FloatTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final float[][][] result = new float[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = minLen; i < maxLen; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    /**
     * Calculates the total number of elements in a 2D float array.
     * This method sums the lengths of all sub-arrays.
     *
     * <pre><code>
     * float[][] matrix = {{1.0f}, {2.0f, 3.0f}, null};
     * long count = Arrays.totalCountOfElements(matrix); // count is 3
     * </code></pre>
     *
     * @param a the 2D array to count elements in.
     * @return the total count of elements.
     */
    public static long totalCountOfElements(final float[][] a) {
        long count = 0;

        for (final float[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of elements in a 3D float array.
     * This method recursively sums the lengths of all innermost sub-arrays.
     *
     * <pre><code>
     * float[][][] cube = {{{1.0f}}, {{2.0f, 3.0f}, null}, null};
     * long count = Arrays.totalCountOfElements(cube); // count is 3
     * </code></pre>
     *
     * @param a the 3D array to count elements in.
     * @return the total count of elements.
     */
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
     * Finds the minimum length of any sub-array within a 2D float array.
     * Returns 0 for null or empty input array, or if a sub-array is null.
     *
     * <pre><code>
     * float[][] matrix = {{1.0f, 2.0f}, {3.0f}, null};
     * int minLen = Arrays.minSubArrayLen(matrix); // minLen is 0
     * </code></pre>
     *
     * @param a the 2D array to inspect.
     * @return the minimum sub-array length found.
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
     * Finds the maximum length of any sub-array within a 2D float array.
     * Returns 0 for null or empty input array. A null sub-array has a length of 0.
     *
     * <pre><code>
     * float[][] matrix = {{1.0f}, {2.0f, 3.0f, 4.0f}, null};
     * int maxLen = Arrays.maxSubArrayLen(matrix); // maxLen is 3
     * </code></pre>
     *
     * @param a the 2D array to inspect.
     * @return the maximum sub-array length found.
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
     * Converts a float array to a string, prints it to the console, and returns the string.
     * Handles null arrays gracefully.
     *
     * <pre><code>
     * float[] array = {1.1f, 2.2f, 3.3f};
     * String s = Arrays.println(array); // Prints "[1.1, 2.2, 3.3]" and returns it.
     * </code></pre>
     *
     * @param a the array to print.
     * @return the string representation of the array, followed by a newline.
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
     * Converts a 2D float array to a formatted string, prints it to the console, and returns the string.
     * Handles null arrays and null sub-arrays gracefully.
     *
     * <pre><code>
     * float[][] matrix = {{1.1f, 2.2f}, {3.3f}};
     * String s = Arrays.println(matrix); // Prints "[[1.1, 2.2],\n [3.3]]" (example) and returns it.
     * </code></pre>
     *
     * @param a the 2D array to print.
     * @return the formatted string representation of the array, followed by a newline.
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
     * Converts a 3D float array to a formatted string, prints it to the console, and returns the string.
     * Handles null arrays and null sub-arrays gracefully.
     *
     * <pre><code>
     * float[][][] cube = {{{1.1f}}, {{2.2f, 3.3f}}};
     * String s = Arrays.println(cube); // Prints a formatted 3D representation and returns it.
     * </code></pre>
     *
     * @param a the 3D array to print.
     * @return the formatted string representation of the array, followed by a newline.
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
     * Updates each element of the specified array in-place by applying a unary operator.
     *
     * <pre><code>
     * double[] array = {1.0, -2.0, 3.0};
     * Arrays.updateAll(array, x -> Math.abs(x));
     * // array is now {1.0, 2.0, 3.0}
     * </code></pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the array to be updated.
     * @param operator the unary operator to apply to each element.
     * @throws E if the operator throws an exception.
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
     * Updates each element of the specified 2D array in-place by applying a unary operator.
     *
     * <pre><code>
     * double[][] matrix = {{1.0, 4.0}, {9.0, 16.0}};
     * Arrays.updateAll(matrix, x -> Math.sqrt(x));
     * // matrix is now {{1.0, 2.0}, {3.0, 4.0}}
     * </code></pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the 2D array to be updated.
     * @param operator the unary operator to apply to each element.
     * @throws E if the operator throws an exception.
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
     * Updates each element of the specified 3D array in-place by applying a unary operator.
     *
     * <pre><code>
     * double[][][] cube = {{{1.1}, {2.8}}, {{3.3}, {4.6}}};
     * Arrays.updateAll(cube, x -> Math.round(x));
     * // cube is now {{{1.0}, {3.0}}, {{3.0}, {5.0}}}
     * </code></pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the 3D array to be updated.
     * @param operator the unary operator to apply to each element.
     * @throws E if the operator throws an exception.
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
     * Replaces each element of the array with the specified new value if it satisfies the given predicate.
     *
     * <pre><code>
     * double[] array = {1.0, -2.0, 3.0, -4.0};
     * Arrays.replaceIf(array, x -> x < 0, 0.0);
     * // array is now {1.0, 0.0, 3.0, 0.0}
     * </code></pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the array to be modified.
     * @param predicate a predicate to apply to each element to determine if it should be replaced.
     * @param newValue the value to be placed into the array.
     * @throws E if the predicate throws an exception.
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
     * Replaces each element of the 2D array with the specified new value if it satisfies the given predicate.
     *
     * <pre><code>
     * double[][] matrix = {{1.0, -2.0}, {Double.NaN, 4.0}};
     * Arrays.replaceIf(matrix, x -> Double.isNaN(x), 0.0);
     * // matrix is now {{1.0, -2.0}, {0.0, 4.0}}
     * </code></pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the 2D array to be modified.
     * @param predicate a predicate to apply to each element to determine if it should be replaced.
     * @param newValue the value to be placed into the array.
     * @throws E if the predicate throws an exception.
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
     * Replaces each element of the 3D array with the specified new value if it satisfies the given predicate.
     *
     * <pre><code>
     * double[][][] cube = {{{1.0}, {Double.POSITIVE_INFINITY}}, {{3.0}, {-4.0}}};
     * Arrays.replaceIf(cube, x -> !Double.isFinite(x), -1.0);
     * // cube is now {{{1.0}, {-1.0}}, {{3.0}, {-4.0}}}
     * </code></pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the 3D array to be modified.
     * @param predicate a predicate to apply to each element to determine if it should be replaced.
     * @param newValue the value to be placed into the array.
     * @throws E if the predicate throws an exception.
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
     * Reshapes a 1D array into a 2D array with the specified number of columns.
     * The number of rows is determined by dividing the total number of elements by the number of columns.
     * The last row may be shorter if the total number of elements is not a multiple of {@code cols}.
     *
     * <pre><code>
     * double[] array = {1, 2, 3, 4, 5, 6, 7};
     * double[][] matrix = Arrays.reshape(array, 3);
     * // matrix is now {{1, 2, 3}, {4, 5, 6}, {7}}
     * </code></pre>
     *
     * @param a the 1D array to reshape.
     * @param cols the number of columns in the new 2D array.
     * @return a new 2D array.
     * @throws IllegalArgumentException if {@code cols} is not positive.
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
     * Reshapes a 1D array into a 3D array with the specified number of rows and columns.
     * The dimensions of the resulting array are determined based on the total element count.
     * The last sub-arrays may be shorter if the total count is not a multiple of {@code rows * cols}.
     *
     * <pre><code>
     * double[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
     * double[][][] cube = Arrays.reshape(array, 2, 3);
     * // cube is now {{{1, 2, 3}, {4, 5, 6}}, {{7, 8, 9}, {10}}}
     * </code></pre>
     *
     * @param a the 1D array to reshape.
     * @param rows the number of rows in each 2D slice.
     * @param cols the number of columns in each 2D slice.
     * @return a new 3D array.
     * @throws IllegalArgumentException if {@code rows} or {@code cols} are not positive.
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
     * Flattens a 2D array into a 1D array by concatenating its rows.
     * Null or empty sub-arrays are skipped.
     *
     * <pre><code>
     * double[][] matrix = {{1.0, 2.0}, {3.0, 4.0}};
     * double[] array = Arrays.flatten(matrix);
     * // array is now {1.0, 2.0, 3.0, 4.0}
     * </code></pre>
     *
     * @param a the 2D array to flatten.
     * @return a new 1D array containing all elements from the input array.
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
     * Flattens a 3D array into a 1D array by concatenating its elements in order.
     * Null or empty sub-arrays are skipped.
     *
     * <pre><code>
     * double[][][] cube = {{{1.0}, {2.0, 3.0}}, {{4.0}}};
     * double[] array = Arrays.flatten(cube);
     * // array is now {1.0, 2.0, 3.0, 4.0}
     * </code></pre>
     *
     * @param a the 3D array to flatten.
     * @return a new 1D array containing all elements from the input array.
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
     * Flattens a 2D array, performs an operation on the resulting 1D array,
     * and then copies the modified elements back into the original 2D array.
     * This allows for operations like sorting across the entire 2D array.
     * The modification happens in-place on the original 2D array.
     *
     * <pre><code>
     * double[][] matrix = {{5.0, 2.0}, {8.0, 1.0}};
     * Arrays.flatOp(matrix, arr -> Arrays.sort(arr));
     * // matrix is now {{1.0, 2.0}, {5.0, 8.0}}
     * </code></pre>
     *
     * @param <E> the type of exception that may be thrown by the operation.
     * @param a the 2D array to operate on.
     * @param op the operation to perform on the flattened array.
     * @throws E if the operation throws an exception.
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
     * Flattens a 3D array, performs an operation on the resulting 1D array,
     * and then copies the modified elements back into the original 3D array.
     * This allows for operations like sorting across the entire 3D array.
     * The modification happens in-place on the original 3D array.
     *
     * <pre><code>
     * double[][][] cube = {{{9.0, 2.0}}, {{5.0}, {1.0}}};
     * Arrays.flatOp(cube, arr -> Arrays.sort(arr));
     * // cube is now {{{1.0, 2.0}}, {{5.0}, {9.0}}}
     * </code></pre>
     *
     * @param <E> the type of exception that may be thrown by the operation.
     * @param a the 3D array to operate on.
     * @param op the operation to perform on the flattened array.
     * @throws E if the operation throws an exception.
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
     * Combines elements from two double arrays using the provided zip function.
     * The operation stops when the shorter array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[] a = {1, 2, 3, 4};
     * double[] b = {5, 6, 7};
     * double[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from both arrays
     * @return a new array containing the results of applying the zip function to corresponding elements
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[] zip(final double[] a, final double[] b, final Throwables.DoubleBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final double[] result = new double[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsDouble(a[i], b[i]);
        }

        return result;
    }

    /**
     * Combines elements from two double arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[] a = {1, 2, 3, 4};
     * double[] b = {5, 6};
     * double[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the doubleer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[] zip(final double[] a, final double[] b, final double valueForNoneA, final double valueForNoneB,
            final Throwables.DoubleBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final double[] result = new double[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsDouble(a[i], b[i]);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zipFunction.applyAsDouble(valueForNoneA, b[i]);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zipFunction.applyAsDouble(a[i], valueForNoneB);
            }
        }

        return result;
    }

    /**
     * Combines elements from three double arrays using the provided zip function.
     * The operation stops when the shortest array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[] a = {1, 2, 3, 4};
     * double[] b = {5, 6, 7};
     * double[] c = {8, 9};
     * double[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new array containing the results with length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[] zip(final double[] a, final double[] b, final double[] c,
            final Throwables.DoubleTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final double[] result = new double[minLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsDouble(a[i], b[i], c[i]);
        }

        return result;
    }

    /**
     * Combines elements from three double arrays using the provided zip function, with default values
     * for missing elements when arrays have different lengths.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[] a = {1, 2, 3, 4};
     * double[] b = {5, 6};
     * double[] c = {8, 9, 10};
     * double[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first array (can be null, treated as empty)
     * @param b the second array (can be null, treated as empty)
     * @param c the third array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' is shorter
     * @param valueForNoneB the default value to use when array 'b' is shorter
     * @param valueForNoneC the default value to use when array 'c' is shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new array with length equal to the doubleest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[] zip(final double[] a, final double[] b, final double[] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC, final Throwables.DoubleTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final double[] result = new double[maxLen];

        for (int i = 0; i < minLen; i++) {
            result[i] = zipFunction.applyAsDouble(a[i], b[i], c[i]);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zipFunction.applyAsDouble(i < lenA ? a[i] : valueForNoneA, i < lenB ? b[i] : valueForNoneB, i < lenC ? c[i] : valueForNoneC);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 2D double arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[][] a = {{1, 2}, {3, 4, 5}};
     * double[][] b = {{5, 6, 7}, {8, 9}};
     * double[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 2D array containing the results of zipping corresponding sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[][] zip(final double[][] a, final double[][] b, final Throwables.DoubleBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final double[][] result = new double[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 2D double arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[][] a = {{1, 2}, {3, 4, 5}};
     * double[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * double[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the doubleer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[][] zip(final double[][] a, final double[][] b, final double valueForNoneA, final double valueForNoneB,
            final Throwables.DoubleBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final double[][] result = new double[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 2D double arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[][] a = {{1, 2}, {3, 4, 5}};
     * double[][] b = {{5, 6, 7}, {8, 9}};
     * double[][] c = {{10, 11}, {12, 13, 14}};
     * double[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 2D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[][] zip(final double[][] a, final double[][] b, final double[][] c,
            final Throwables.DoubleTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final double[][] result = new double[minLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 2D double arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[][] a = {{1, 2}, {3, 4, 5}};
     * double[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * double[][] c = {{10, 11}};
     * double[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 2D array (can be null, treated as empty)
     * @param b the second 2D array (can be null, treated as empty)
     * @param c the third 2D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 2D array with outer length equal to the doubleest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[][] zip(final double[][] a, final double[][] b, final double[][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC, final Throwables.DoubleTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final double[][] result = new double[maxLen][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        if (minLen < maxLen) {
            for (int i = minLen; i < maxLen; i++) {
                result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                        zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from two 3D double arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * double[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * double[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements in sub-arrays
     * @return a new 3D array containing the results of zipping corresponding 2D sub-arrays
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[][][] zip(final double[][][] a, final double[][][] b, final Throwables.DoubleBinaryOperator<E> zipFunction)
            throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);

        final double[][][] result = new double[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from two 3D double arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * double[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * double[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the doubleer input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[][][] zip(final double[][][] a, final double[][][] b, final double valueForNoneA, final double valueForNoneB,
            final Throwables.DoubleBinaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int minLen = N.min(lenA, lenB);
        final int maxLen = N.max(lenA, lenB);

        final double[][][] result = new double[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction);
        }

        if (lenA < maxLen) {
            for (int i = lenA; i < maxLen; i++) {
                result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction);
            }
        } else if (lenB < maxLen) {
            for (int i = lenB; i < maxLen; i++) {
                result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction);
            }
        }

        return result;
    }

    /**
     * Combines elements from three 3D double arrays using the provided zip function.
     * Applies the zip operation to corresponding 2D sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[][][] a = {{{1, 2}}};
     * double[][][] b = {{{11, 12}, {13, 14}}};
     * double[][][] c = {{{21, 22}}};
     * double[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param zipFunction the function to apply to corresponding elements from all three arrays
     * @return a new 3D array containing the results with outer length equal to the shortest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[][][] zip(final double[][][] a, final double[][][] b, final double[][][] c,
            final Throwables.DoubleTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);

        final double[][][] result = new double[minLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], zipFunction);
        }

        return result;
    }

    /**
     * Combines elements from three 3D double arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * double[][][] a = {{{1, 2}}};
     * double[][][] b = {{{11, 12}, {13, 14}}};
     * double[][][] c = {{{21, 22}}};
     * double[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw
     * @param a the first 3D array (can be null, treated as empty)
     * @param b the second 3D array (can be null, treated as empty)
     * @param c the third 3D array (can be null, treated as empty)
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter
     * @param zipFunction the function to apply to corresponding elements
     * @return a new 3D array with outer length equal to the doubleest input array
     * @throws E if the zip function throws an exception
     */
    public static <E extends Exception> double[][][] zip(final double[][][] a, final double[][][] b, final double[][][] c, final double valueForNoneA,
            final double valueForNoneB, final double valueForNoneC, final Throwables.DoubleTernaryOperator<E> zipFunction) throws E {
        final int lenA = N.len(a);
        final int lenB = N.len(b);
        final int lenC = N.len(c);
        final int minLen = N.min(lenA, lenB, lenC);
        final int maxLen = N.max(lenA, lenB, lenC);

        final double[][][] result = new double[maxLen][][];

        for (int i = 0; i < minLen; i++) {
            result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        for (int i = minLen; i < maxLen; i++) {
            result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction);
        }

        return result;
    }

    /**
     * Calculates the total number of double elements in a jagged 2D array.
     *
     * <pre><code>
     * double[][] matrix = {{1, 2}, {3, 4, 5}, null, {}};
     * long count = Arrays.totalCountOfElements(matrix);
     * // count is 5
     * </code></pre>
     *
     * @param a the 2D array.
     * @return the total count of elements.
     */
    public static long totalCountOfElements(final double[][] a) {
        long count = 0;

        for (final double[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of double elements in a jagged 3D array.
     *
     * <pre><code>
     * double[][][] cube = {{{1}, {2, 3}}, null, {{{4}}}};
     * long count = Arrays.totalCountOfElements(cube);
     * // count is 4
     * </code></pre>
     *
     * @param a the 3D array.
     * @return the total count of elements.
     */
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
     * Finds the minimum length of any sub-array in a 2D array.
     * A null sub-array is considered to have a length of 0.
     *
     * <pre><code>
     * double[][] matrix = {{1, 2, 3}, {4, 5}, null};
     * int minLen = Arrays.minSubArrayLen(matrix);
     * // minLen is 0
     * </code></pre>
     *
     * @param a the 2D array.
     * @return the minimum sub-array length, or 0 if the input array is null or empty.
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
     * Finds the maximum length of any sub-array in a 2D array.
     * A null sub-array is considered to have a length of 0.
     *
     * <pre><code>
     * double[][] matrix = {{1, 2, 3}, {4, 5}, null};
     * int maxLen = Arrays.maxSubArrayLen(matrix);
     * // maxLen is 3
     * </code></pre>
     *
     * @param a the 2D array.
     * @return the maximum sub-array length, or 0 if the input array is null or empty.
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
     * Prints the contents of a 1D double array to the console, followed by a new line.
     * Handles null and empty arrays gracefully.
     *
     * <pre><code>
     * double[] array = {1.1, 2.2, 3.3};
     * Arrays.println(array); // Prints "[1.1, 2.2, 3.3]"
     * </code></pre>
     *
     * @param a the array to print.
     * @return The string that was printed to the console.
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
     * Prints the contents of a 2D double array to the console, followed by a new line.
     * Provides a formatted string representation for readability.
     *
     * <pre><code>
     * double[][] matrix = {{1.0, 2.0}, {3.0, 4.0}};
     * Arrays.println(matrix); // Prints "[[1.0, 2.0],\n [3.0, 4.0]]" (format may vary)
     * </code></pre>
     *
     * @param a the 2D array to print.
     * @return The string that was printed to the console.
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
     * Prints the contents of a 3D double array to the console, followed by a new line.
     * Provides a formatted string representation for readability.
     *
     * <pre><code>
     * double[][][] cube = {{{1.0}, {2.0}}, {{3.0}, {4.0}}};
     * Arrays.println(cube); // Prints a formatted representation of the 3D array.
     * </code></pre>
     *
     * @param a the 3D array to print.
     * @return The string that was printed to the console.
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
     * Converts an array of bytes to an array of booleans. A byte value greater than 0
     * is converted to {@code true}, otherwise {@code false}.
     *
     * <pre><code>
     * byte[] bytes = {1, 0, -1, 5};
     * boolean[] bools = Arrays.toBoolean(bytes);
     * // bools is {true, false, false, true}
     * </code></pre>
     *
     * @param a the array of bytes to convert.
     * @return a new boolean array, or null if the input is null.
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
     * Converts a 2D array of bytes to a 2D array of booleans.
     *
     * @param a the 2D array of bytes to convert.
     * @return a new 2D boolean array, or null if the input is null.
     * @see #toBoolean(byte[])
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
     * Converts a 3D array of bytes to a 3D array of booleans.
     *
     * @param a the 3D array of bytes to convert.
     * @return a new 3D boolean array, or null if the input is null.
     * @see #toBoolean(byte[][])
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
     * Converts an array of integers to an array of booleans. An integer value greater than 0
     * is converted to {@code true}, otherwise {@code false}.
     *
     * <pre><code>
     * int[] ints = {1, 0, -1, 5};
     * boolean[] bools = Arrays.toBoolean(ints);
     * // bools is {true, false, false, true}
     * </code></pre>
     *
     * @param a the array of integers to convert.
     * @return a new boolean array, or null if the input is null.
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
     * Converts a 2D array of integers to a 2D array of booleans.
     *
     * @param a the 2D array of integers to convert.
     * @return a new 2D boolean array, or null if the input is null.
     * @see #toBoolean(int[])
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
     * Converts a 3D array of integers to a 3D array of booleans.
     *
     * @param a the 3D array of integers to convert.
     * @return a new 3D boolean array, or null if the input is null.
     * @see #toBoolean(int[][])
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
     * Converts an array of integers to an array of chars by casting.
     *
     * <pre><code>
     * int[] ints = {65, 66, 67};
     * char[] chars = Arrays.toChar(ints);
     * // chars is {'A', 'B', 'C'}
     * </code></pre>
     *
     * @param a the array of integers to convert.
     * @return a new char array, or null if the input is null.
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
     * Converts a 2D array of integers to a 2D array of chars.
     *
     * @param a the 2D array of integers to convert.
     * @return a new 2D char array, or null if the input is null.
     * @see #toChar(int[])
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
     * Converts a 3D array of integers to a 3D array of chars.
     *
     * @param a the 3D array of integers to convert.
     * @return a new 3D char array, or null if the input is null.
     * @see #toChar(int[][])
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
     * Converts an array of booleans to an array of bytes.
     * {@code true} becomes 1, and {@code false} becomes 0.
     *
     * <pre><code>
     * boolean[] bools = {true, false, true};
     * byte[] bytes = Arrays.toByte(bools);
     * // bytes is {1, 0, 1}
     * </code></pre>
     *
     * @param a the array of booleans to convert.
     * @return a new byte array, or null if the input is null.
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
     * Converts a 2D array of booleans to a 2D array of bytes.
     *
     * @param a the 2D array of booleans to convert.
     * @return a new 2D byte array, or null if the input is null.
     * @see #toByte(boolean[])
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
     * Converts a 3D array of booleans to a 3D array of bytes.
     *
     * @param a the 3D array of booleans to convert.
     * @return a new 3D byte array, or null if the input is null.
     * @see #toByte(boolean[][])
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
     * Converts an array of bytes to an array of shorts by casting.
     *
     * <pre><code>
     * byte[] bytes = {10, 20, 30};
     * short[] shorts = Arrays.toShort(bytes);
     * // shorts is {10, 20, 30}
     * </code></pre>
     *
     * @param a the array of bytes to convert.
     * @return a new short array, or null if the input is null.
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
     * Converts a 2D array of bytes to a 2D array of shorts.
     *
     * @param a the 2D array of bytes to convert.
     * @return a new 2D short array, or null if the input is null.
     * @see #toShort(byte[])
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
     * Converts a 3D array of bytes to a 3D array of shorts.
     *
     * @param a the 3D array of bytes to convert.
     * @return a new 3D short array, or null if the input is null.
     * @see #toShort(byte[][])
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
     * Converts an array of booleans to an array of integers.
     * {@code true} becomes 1, and {@code false} becomes 0.
     *
     * <pre><code>
     * boolean[] bools = {true, false, true};
     * int[] ints = Arrays.toInt(bools);
     * // ints is {1, 0, 1}
     * </code></pre>
     *
     * @param a the array of booleans to convert.
     * @return a new integer array, or null if the input is null.
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
     * Converts a 2D array of booleans to a 2D array of integers.
     *
     * @param a the 2D array of booleans to convert.
     * @return a new 2D integer array, or null if the input is null.
     * @see #toInt(boolean[])
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
     * Converts a 3D array of booleans to a 3D array of integers.
     *
     * @param a the 3D array of booleans to convert.
     * @return a new 3D integer array, or null if the input is null.
     * @see #toInt(boolean[][])
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
     * Converts an array of chars to an array of integers by casting.
     *
     * <pre><code>
     * char[] chars = {'A', 'B', 'C'};
     * int[] ints = Arrays.toInt(chars);
     * // ints is {65, 66, 67}
     * </code></pre>
     *
     * @param a the array of chars to convert.
     * @return a new integer array, or null if the input is null.
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
     * Converts a 2D array of chars to a 2D array of integers.
     *
     * @param a the 2D array of chars to convert.
     * @return a new 2D integer array, or null if the input is null.
     * @see #toInt(char[])
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
     * Converts a 3D array of chars to a 3D array of integers.
     *
     * @param a the 3D array of chars to convert.
     * @return a new 3D integer array, or null if the input is null.
     * @see #toInt(char[][])
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
     * Converts an array of bytes to an array of integers by casting.
     *
     * <pre><code>
     * byte[] bytes = {10, 20, 30};
     * int[] ints = Arrays.toInt(bytes);
     * // ints is {10, 20, 30}
     * </code></pre>
     *
     * @param a the array of bytes to convert.
     * @return a new integer array, or null if the input is null.
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
     * Converts a 2D array of bytes to a 2D array of integers.
     *
     * @param a the 2D array of bytes to convert.
     * @return a new 2D integer array, or null if the input is null.
     * @see #toInt(byte[])
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
     * Converts a 3D array of bytes to a 3D array of integers.
     *
     * @param a the 3D array of bytes to convert.
     * @return a new 3D integer array, or null if the input is null.
     * @see #toInt(byte[][])
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
     * Converts an array of shorts to an array of integers by casting.
     *
     * <pre><code>
     * short[] shorts = {100, 200, 300};
     * int[] ints = Arrays.toInt(shorts);
     * // ints is {100, 200, 300}
     * </code></pre>
     *
     * @param a the array of shorts to convert.
     * @return a new integer array, or null if the input is null.
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
     * Converts a 2D array of shorts to a 2D array of integers.
     *
     * @param a the 2D array of shorts to convert.
     * @return a new 2D integer array, or null if the input is null.
     * @see #toInt(short[])
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
     * Converts a 3D array of shorts to a 3D array of integers.
     *
     * @param a the 3D array of shorts to convert.
     * @return a new 3D integer array, or null if the input is null.
     * @see #toInt(short[][])
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
     * Converts an array of floats to an array of integers by casting (truncating).
     *
     * <pre><code>
     * float[] floats = {1.1f, 2.9f, -3.5f};
     * int[] ints = Arrays.toInt(floats);
     * // ints is {1, 2, -3}
     * </code></pre>
     *
     * @param a the array of floats to convert.
     * @return a new integer array, or null if the input is null.
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
     * Converts a 2D array of floats to a 2D array of integers.
     *
     * @param a the 2D array of floats to convert.
     * @return a new 2D integer array, or null if the input is null.
     * @see #toInt(float[])
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
     * Converts a 3D array of floats to a 3D array of integers.
     *
     * @param a the 3D array of floats to convert.
     * @return a new 3D integer array, or null if the input is null.
     * @see #toInt(float[][])
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
     * Converts an array of doubles to an array of integers by casting (truncating).
     *
     * <pre><code>
     * double[] doubles = {1.1, 2.9, -3.5};
     * int[] ints = Arrays.toInt(doubles);
     * // ints is {1, 2, -3}
     * </code></pre>
     *
     * @param a the array of doubles to convert.
     * @return a new integer array, or null if the input is null.
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
     * Converts a 2D array of doubles to a 2D array of integers.
     *
     * @param a the 2D array of doubles to convert.
     * @return a new 2D integer array, or null if the input is null.
     * @see #toInt(double[])
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
     * Converts a 3D array of doubles to a 3D array of integers.
     *
     * @param a the 3D array of doubles to convert.
     * @return a new 3D integer array, or null if the input is null.
     * @see #toInt(double[][])
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
     * Converts an array of bytes to an array of longs by casting.
     *
     * <pre><code>
     * byte[] bytes = {10, 20, 30};
     * long[] longs = Arrays.toLong(bytes);
     * // longs is {10L, 20L, 30L}
     * </code></pre>
     *
     * @param a the array of bytes to convert.
     * @return a new long array, or null if the input is null.
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
     * Converts a 2D array of bytes to a 2D array of longs.
     *
     * @param a the 2D array of bytes to convert.
     * @return a new 2D long array, or null if the input is null.
     * @see #toLong(byte[])
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
     * Converts a 3D array of bytes to a 3D array of longs.
     *
     * @param a the 3D array of bytes to convert.
     * @return a new 3D long array, or null if the input is null.
     * @see #toLong(byte[][])
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
     * Converts a one-dimensional {@code short} array to a one-dimensional {@code long} array.
     * Each {@code short} element is cast to a {@code long}.
     *
     * <pre><code>
     * short[] source = {1, 2, 3};
     * long[] result = Arrays.toLong(source);
     * // result is {1L, 2L, 3L}
     * </code></pre>
     *
     * @param a the one-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code short} array to a two-dimensional {@code long} array.
     * Each {@code short} element is cast to a {@code long}.
     *
     * <pre><code>
     * short[][] source = {{1, 2}, {3, 4}};
     * long[][] result = Arrays.toLong(source);
     * // result is {{1L, 2L}, {3L, 4L}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code short} array to a three-dimensional {@code long} array.
     * Each {@code short} element is cast to a {@code long}.
     *
     * <pre><code>
     * short[][][] source = {{{1, 2}}, {{3, 4}}};
     * long[][][] result = Arrays.toLong(source);
     * // result is {{{1L, 2L}}, {{3L, 4L}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code int} array to a one-dimensional {@code long} array.
     * Each {@code int} element is cast to a {@code long}.
     *
     * <pre><code>
     * int[] source = {1, 2, 3};
     * long[] result = Arrays.toLong(source);
     * // result is {1L, 2L, 3L}
     * </code></pre>
     *
     * @param a the one-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code int} array to a two-dimensional {@code long} array.
     * Each {@code int} element is cast to a {@code long}.
     *
     * <pre><code>
     * int[][] source = {{1, 2}, {3, 4}};
     * long[][] result = Arrays.toLong(source);
     * // result is {{1L, 2L}, {3L, 4L}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code int} array to a three-dimensional {@code long} array.
     * Each {@code int} element is cast to a {@code long}.
     *
     * <pre><code>
     * int[][][] source = {{{1, 2}}, {{3, 4}}};
     * long[][][] result = Arrays.toLong(source);
     * // result is {{{1L, 2L}}, {{3L, 4L}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code float} array to a one-dimensional {@code long} array.
     * Each {@code float} element is cast to a {@code long}, involving a narrowing primitive conversion.
     *
     * <pre><code>
     * float[] source = {1.1f, 2.9f, 3.5f};
     * long[] result = Arrays.toLong(source);
     * // result is {1L, 2L, 3L}
     * </code></pre>
     *
     * @param a the one-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code float} array to a two-dimensional {@code long} array.
     * Each {@code float} element is cast to a {@code long}.
     *
     * <pre><code>
     * float[][] source = {{1.1f, 2.9f}, {3.5f, 4.0f}};
     * long[][] result = Arrays.toLong(source);
     * // result is {{1L, 2L}, {3L, 4L}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code float} array to a three-dimensional {@code long} array.
     * Each {@code float} element is cast to a {@code long}.
     *
     * <pre><code>
     * float[][][] source = {{{1.1f, 2.9f}}, {{3.5f, 4.0f}}};
     * long[][][] result = Arrays.toLong(source);
     * // result is {{{1L, 2L}}, {{3L, 4L}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code double} array to a one-dimensional {@code long} array.
     * Each {@code double} element is cast to a {@code long}, involving a narrowing primitive conversion.
     *
     * <pre><code>
     * double[] source = {1.1, 2.9, 3.5};
     * long[] result = Arrays.toLong(source);
     * // result is {1L, 2L, 3L}
     * </code></pre>
     *
     * @param a the one-dimensional {@code double} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code double} array to a two-dimensional {@code long} array.
     * Each {@code double} element is cast to a {@code long}.
     *
     * <pre><code>
     * double[][] source = {{1.1, 2.9}, {3.5, 4.0}};
     * long[][] result = Arrays.toLong(source);
     * // result is {{1L, 2L}, {3L, 4L}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code double} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code double} array to a three-dimensional {@code long} array.
     * Each {@code double} element is cast to a {@code long}.
     *
     * <pre><code>
     * double[][][] source = {{{1.1, 2.9}}, {{3.5, 4.0}}};
     * long[][][] result = Arrays.toLong(source);
     * // result is {{{1L, 2L}}, {{3L, 4L}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code double} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code long} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code byte} array to a one-dimensional {@code float} array.
     * Each {@code byte} element is cast to a {@code float}.
     *
     * <pre><code>
     * byte[] source = {1, 2, 3};
     * float[] result = Arrays.toFloat(source);
     * // result is {1.0f, 2.0f, 3.0f}
     * </code></pre>
     *
     * @param a the one-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code byte} array to a two-dimensional {@code float} array.
     * Each {@code byte} element is cast to a {@code float}.
     *
     * <pre><code>
     * byte[][] source = {{1, 2}, {3, 4}};
     * float[][] result = Arrays.toFloat(source);
     * // result is {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code byte} array to a three-dimensional {@code float} array.
     * Each {@code byte} element is cast to a {@code float}.
     *
     * <pre><code>
     * byte[][][] source = {{{1, 2}}, {{3, 4}}};
     * float[][][] result = Arrays.toFloat(source);
     * // result is {{{1.0f, 2.0f}}, {{3.0f, 4.0f}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code short} array to a one-dimensional {@code float} array.
     * Each {@code short} element is cast to a {@code float}.
     *
     * <pre><code>
     * short[] source = {1, 2, 3};
     * float[] result = Arrays.toFloat(source);
     * // result is {1.0f, 2.0f, 3.0f}
     * </code></pre>
     *
     * @param a the one-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code short} array to a two-dimensional {@code float} array.
     * Each {@code short} element is cast to a {@code float}.
     *
     * <pre><code>
     * short[][] source = {{1, 2}, {3, 4}};
     * float[][] result = Arrays.toFloat(source);
     * // result is {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code short} array to a three-dimensional {@code float} array.
     * Each {@code short} element is cast to a {@code float}.
     *
     * <pre><code>
     * short[][][] source = {{{1, 2}}, {{3, 4}}};
     * float[][][] result = Arrays.toFloat(source);
     * // result is {{{1.0f, 2.0f}}, {{3.0f, 4.0f}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code int} array to a one-dimensional {@code float} array.
     * Each {@code int} element is cast to a {@code float}.
     *
     * <pre><code>
     * int[] source = {1, 2, 3};
     * float[] result = Arrays.toFloat(source);
     * // result is {1.0f, 2.0f, 3.0f}
     * </code></pre>
     *
     * @param a the one-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code int} array to a two-dimensional {@code float} array.
     * Each {@code int} element is cast to a {@code float}.
     *
     * <pre><code>
     * int[][] source = {{1, 2}, {3, 4}};
     * float[][] result = Arrays.toFloat(source);
     * // result is {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code int} array to a three-dimensional {@code float} array.
     * Each {@code int} element is cast to a {@code float}.
     *
     * <pre><code>
     * int[][][] source = {{{1, 2}}, {{3, 4}}};
     * float[][][] result = Arrays.toFloat(source);
     * // result is {{{1.0f, 2.0f}}, {{3.0f, 4.0f}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code long} array to a one-dimensional {@code float} array.
     * Each {@code long} element is cast to a {@code float}, which may result in a loss of precision.
     *
     * <pre><code>
     * long[] source = {1L, 2L, 9007199254740992L};
     * float[] result = Arrays.toFloat(source);
     * // result may be {1.0f, 2.0f, 9.0071992E15f}
     * </code></pre>
     *
     * @param a the one-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code long} array to a two-dimensional {@code float} array.
     * Each {@code long} element is cast to a {@code float}, which may result in a loss of precision.
     *
     * <pre><code>
     * long[][] source = {{1L, 2L}, {3L, 4L}};
     * float[][] result = Arrays.toFloat(source);
     * // result is {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code long} array to a three-dimensional {@code float} array.
     * Each {@code long} element is cast to a {@code float}, which may result in a loss of precision.
     *
     * <pre><code>
     * long[][][] source = {{{1L, 2L}}, {{3L, 4L}}};
     * float[][][] result = Arrays.toFloat(source);
     * // result is {{{1.0f, 2.0f}}, {{3.0f, 4.0f}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code float} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code byte} array to a one-dimensional {@code double} array.
     * Each {@code byte} element is cast to a {@code double}.
     *
     * <pre><code>
     * byte[] source = {1, 2, 3};
     * double[] result = Arrays.toDouble(source);
     * // result is {1.0, 2.0, 3.0}
     * </code></pre>
     *
     * @param a the one-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code byte} array to a two-dimensional {@code double} array.
     * Each {@code byte} element is cast to a {@code double}.
     *
     * <pre><code>
     * byte[][] source = {{1, 2}, {3, 4}};
     * double[][] result = Arrays.toDouble(source);
     * // result is {{1.0, 2.0}, {3.0, 4.0}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code byte} array to a three-dimensional {@code double} array.
     * Each {@code byte} element is cast to a {@code double}.
     *
     * <pre><code>
     * byte[][][] source = {{{1, 2}}, {{3, 4}}};
     * double[][][] result = Arrays.toDouble(source);
     * // result is {{{1.0, 2.0}}, {{3.0, 4.0}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code short} array to a one-dimensional {@code double} array.
     * Each {@code short} element is cast to a {@code double}.
     *
     * <pre><code>
     * short[] source = {1, 2, 3};
     * double[] result = Arrays.toDouble(source);
     * // result is {1.0, 2.0, 3.0}
     * </code></pre>
     *
     * @param a the one-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code short} array to a two-dimensional {@code double} array.
     * Each {@code short} element is cast to a {@code double}.
     *
     * <pre><code>
     * short[][] source = {{1, 2}, {3, 4}};
     * double[][] result = Arrays.toDouble(source);
     * // result is {{1.0, 2.0}, {3.0, 4.0}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code short} array to a three-dimensional {@code double} array.
     * Each {@code short} element is cast to a {@code double}.
     *
     * <pre><code>
     * short[][][] source = {{{1, 2}}, {{3, 4}}};
     * double[][][] result = Arrays.toDouble(source);
     * // result is {{{1.0, 2.0}}, {{3.0, 4.0}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code int} array to a one-dimensional {@code double} array.
     * Each {@code int} element is cast to a {@code double}.
     *
     * <pre><code>
     * int[] source = {1, 2, 3};
     * double[] result = Arrays.toDouble(source);
     * // result is {1.0, 2.0, 3.0}
     * </code></pre>
     *
     * @param a the one-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code int} array to a two-dimensional {@code double} array.
     * Each {@code int} element is cast to a {@code double}.
     *
     * <pre><code>
     * int[][] source = {{1, 2}, {3, 4}};
     * double[][] result = Arrays.toDouble(source);
     * // result is {{1.0, 2.0}, {3.0, 4.0}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code int} array to a three-dimensional {@code double} array.
     * Each {@code int} element is cast to a {@code double}.
     *
     * <pre><code>
     * int[][][] source = {{{1, 2}}, {{3, 4}}};
     * double[][][] result = Arrays.toDouble(source);
     * // result is {{{1.0, 2.0}}, {{3.0, 4.0}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code long} array to a one-dimensional {@code double} array.
     * Each {@code long} element is cast to a {@code double}, which may result in a loss of precision.
     *
     * <pre><code>
     * long[] source = {1L, 2L, 9007199254740992L};
     * double[] result = Arrays.toDouble(source);
     * // result is {1.0, 2.0, 9.007199254740992E15}
     * </code></pre>
     *
     * @param a the one-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code long} array to a two-dimensional {@code double} array.
     * Each {@code long} element is cast to a {@code double}, which may result in a loss of precision.
     *
     * <pre><code>
     * long[][] source = {{1L, 2L}, {3L, 4L}};
     * double[][] result = Arrays.toDouble(source);
     * // result is {{1.0, 2.0}, {3.0, 4.0}}
     * </code></pre>
     *
     * @param a the two-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code long} array to a three-dimensional {@code double} array.
     * Each {@code long} element is cast to a {@code double}, which may result in a loss of precision.
     *
     * <pre><code>
     * long[][][] source = {{{1L, 2L}}, {{3L, 4L}}};
     * double[][][] result = Arrays.toDouble(source);
     * // result is {{{1.0, 2.0}}, {{3.0, 4.0}}}
     * </code></pre>
     *
     * @param a the three-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a one-dimensional {@code float} array to a one-dimensional {@code double} array.
     * Each {@code float} element is cast to a {@code double}.
     *
     * <pre><code>
     * float[] source = {1.1f, 2.2f, 3.3f};
     * double[] result = Arrays.toDouble(source);
     * // result is approximately {1.100000023841858, 2.200000047683716, 3.299999952316284}
     * </code></pre>
     *
     * @param a the one-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a two-dimensional {@code float} array to a two-dimensional {@code double} array.
     * Each {@code float} element is cast to a {@code double}.
     *
     * <pre><code>
     * float[][] source = {{1.1f, 2.2f}, {3.3f, 4.4f}};
     * double[][] result = Arrays.toDouble(source);
     * // result is a 2D double array with converted values.
     * </code></pre>
     *
     * @param a the two-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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
     * Converts a three-dimensional {@code float} array to a three-dimensional {@code double} array.
     * Each {@code float} element is cast to a {@code double}.
     *
     * <pre><code>
     * float[][][] source = {{{1.1f, 2.2f}}, {{3.3f, 4.4f}}};
     * double[][][] result = Arrays.toDouble(source);
     * // result is a 3D double array with converted values.
     * </code></pre>
     *
     * @param a the three-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code double} array, or {@code null} if the input was {@code null}.
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

    /**
     * A placeholder utility class that extends {@code com.landawn.abacus.util.Arrays}.
     * This class is intended to be a part of a larger utility suite but does not define
     * any methods of its own. It is marked with {@code SuppressFBWarnings} for its naming convention.
     */
    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
    public static final class f extends com.landawn.abacus.util.Arrays { // NOSONAR
        private f() {
            // utility class
        }

        /**
         * Maps each element of the input array to a new type using the provided mapping function.
         * 
         * @param <T> the type of elements in the input array
         * @param <R> the type of elements in the result array
         * @param <E> the type of exception that the mapping function may throw
         * @param a the input array to map
         * @param func the mapping function to apply to each element
         * @param targetElementType the class of the target element type
         * @return a new array containing the mapped elements, or null if input is null
         * @throws E if the mapping function throws an exception
         * 
         * @example
         * <pre>
         * String[] strings = {"1", "2", "3"};
         * Integer[] ints = Arrays.map(strings, Integer::valueOf, Integer.class);
         * // Result: [1, 2, 3]
         * </pre>
         */
        public static <T, R, E extends Exception> R[] map(final T[] a, final Throwables.Function<? super T, ? extends R, E> func,
                final Class<R> targetElementType) throws E {
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
         * Maps each element of the input array to a boolean value using the provided function.
         *
         * @param <T> the type of elements in the input array
         * @param <E> the type of exception that the mapping function may throw
         * @param a the input array to map
         * @param func the function that maps each element to a boolean
         * @return a boolean array containing the mapped values, or null if input is null
         * @throws E if the mapping function throws an exception
         *
         * @example
         * <pre>
         * String[] words = {"hello", "hi", "world"};
         * boolean[] longWords = Arrays.mapToBoolean(words, s -> s.length() > 3);
         * // Result: [true, false, true]
         * </pre>
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
         * Maps each element of the input array to a char value using the provided function.
         *
         * @param <T> the type of elements in the input array
         * @param <E> the type of exception that the mapping function may throw
         * @param a the input array to map
         * @param func the function that maps each element to a char
         * @return a char array containing the mapped values, or null if input is null
         * @throws E if the mapping function throws an exception
         *
         * @example
         * <pre>
         * String[] words = {"apple", "banana", "cherry"};
         * char[] firstChars = Arrays.mapToChar(words, s -> s.charAt(0));
         * // Result: ['a', 'b', 'c']
         * </pre>
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
         * Maps each element of the input array to a byte value using the provided function.
         *
         * @param <T> the type of elements in the input array
         * @param <E> the type of exception that the mapping function may throw
         * @param a the input array to map
         * @param func the function that maps each element to a byte
         * @return a byte array containing the mapped values, or null if input is null
         * @throws E if the mapping function throws an exception
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
         * Maps each element of the input array to a short value using the provided function.
         *
         * @param <T> the type of elements in the input array
         * @param <E> the type of exception that the mapping function may throw
         * @param a the input array to map
         * @param func the function that maps each element to a short
         * @return a short array containing the mapped values, or null if input is null
         * @throws E if the mapping function throws an exception
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
         * Maps each element of the input array to an int value using the provided function.
         *
         * @param <T> the type of elements in the input array
         * @param <E> the type of exception that the mapping function may throw
         * @param a the input array to map
         * @param func the function that maps each element to an int
         * @return an int array containing the mapped values, or null if input is null
         * @throws E if the mapping function throws an exception
         *
         * @example
         * <pre>
         * String[] numbers = {"10", "20", "30"};
         * int[] ints = Arrays.mapToInt(numbers, Integer::parseInt);
         * // Result: [10, 20, 30]
         * </pre>
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
         * Maps each element of the input array to a long value using the provided function.
         *
         * @param <T> the type of elements in the input array
         * @param <E> the type of exception that the mapping function may throw
         * @param a the input array to map
         * @param func the function that maps each element to a long
         * @return a long array containing the mapped values, or null if input is null
         * @throws E if the mapping function throws an exception
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
         * Maps each element of the input array to a float value using the provided function.
         *
         * @param <T> the type of elements in the input array
         * @param <E> the type of exception that the mapping function may throw
         * @param a the input array to map
         * @param func the function that maps each element to a float
         * @return a float array containing the mapped values, or null if input is null
         * @throws E if the mapping function throws an exception
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
         * Maps each element of the input array to a double value using the provided function.
         *
         * @param <T> the type of elements in the input array
         * @param <E> the type of exception that the mapping function may throw
         * @param a the input array to map
         * @param func the function that maps each element to a double
         * @return a double array containing the mapped values, or null if input is null
         * @throws E if the mapping function throws an exception
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
    }

    /**
     * A utility class providing functional-style operations on two-dimensional arrays.
     * This class offers a comprehensive set of methods for manipulating 2D arrays including
     * updating, replacing, reshaping, flattening, mapping, and zipping operations.
     * 
     * <p>All methods in this class are static and the class cannot be instantiated.
     * The class name 'ff' is intentionally short for convenient static imports.</p>
     * 
     * <p>Key features include:</p>
     * <ul>
     *   <li>In-place update and replacement operations</li>
     *   <li>Array reshaping and flattening</li>
     *   <li>Type-safe mapping to primitive arrays</li>
     *   <li>Advanced zipping operations with multiple arrays</li>
     *   <li>Null-safe operations throughout</li>
     * </ul>
     * 
     * @author Haiyang Li
     * @since 1.0
     */
    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
    public static final class ff { // NOSONAR

        /**
         * Private constructor to prevent instantiation of this utility class.
         * 
         * @throws AssertionError if called
         */
        private ff() {
            // Singleton
        }

        /**
         * Updates all elements in a two-dimensional array by applying the specified operator to each element.
         * The operation is performed in-place, modifying the original array.
         * 
         * <p>This method iterates through each element of the nested arrays and replaces it with
         * the result of applying the operator. Null sub-arrays and empty sub-arrays are safely skipped.</p>
         * 
         * <pre><code>
         * String[][] array = {{"hello", "world"}, {"foo", "bar"}};
         * ff.updateAll(array, str -> str.toUpperCase());
         * // array is now {{"HELLO", "WORLD"}, {"FOO", "BAR"}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the array
         * @param <E> the type of exception that may be thrown by the operator
         * @param a the two-dimensional array to update. If null or empty, no operation is performed
         * @param operator the unary operator to apply to each element. Must not be null
         * @throws E if the operator throws an exception during processing
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
         * Replaces all elements in a two-dimensional array that match the specified predicate with a new value.
         * The replacement is performed in-place, modifying the original array.
         * 
         * <p>This method is useful for conditional replacement operations, such as replacing null values
         * or elements matching certain criteria.</p>
         * 
         * <pre><code>
         * Integer[][] array = {{1, null, 3}, {null, 5, 6}};
         * ff.replaceIf(array, val -> val == null, 0);
         * // array is now {{1, 0, 3}, {0, 5, 6}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the array
         * @param <E> the type of exception that may be thrown by the predicate
         * @param a the two-dimensional array to modify. If null or empty, no operation is performed
         * @param predicate the condition to test each element against. Must not be null
         * @param newValue the value to replace matching elements with
         * @throws E if the predicate throws an exception during testing
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
         * The resulting array will have rows of equal length (except possibly the last row).
         * 
         * <p>The number of rows is calculated automatically based on the array length and column count.
         * If the array length is not evenly divisible by the column count, the last row will contain
         * fewer elements than specified columns.</p>
         * 
         * <pre><code>
         * Integer[] array = {1, 2, 3, 4, 5, 6, 7};
         * Integer[][] reshaped = ff.reshape(array, 3);
         * // reshaped is {{1, 2, 3}, {4, 5, 6}, {7}}
         * </code></pre>
         * 
         * @param <T> the component type of the array
         * @param a the one-dimensional array to reshape. Must not be null
         * @param cols the number of columns in each row. Must be positive
         * @return a new two-dimensional array with the specified column structure
         * @throws IllegalArgumentException if cols is not positive (cols ≤ 0)
         */
        public static <T> T[][] reshape(final T[] a, final int cols) throws IllegalArgumentException {
            checkMForReshape(cols);

            //        if (N.isEmpty(a)) {
            //            return new T[0][0];
            //        }

            final int len = a.length;
            final int n = Numbers.divide(len, cols, RoundingMode.CEILING);
            final Class<T[]> arrayClass = (Class<T[]>) a.getClass();
            final T[][] c = N.newArray(arrayClass, n);

            for (int i = 0, from = 0; i < n; i++, from += cols) {
                c[i] = N.copyOfRange(a, from, from + N.min(len - from, cols));
            }

            return c;
        }

        /**
         * Flattens a two-dimensional array into a one-dimensional array by concatenating all sub-arrays.
         * This method handles jagged arrays (arrays with rows of different lengths) correctly.
         * 
         * <p>Elements are concatenated in row-major order. Null or empty sub-arrays are safely skipped
         * without adding any elements to the result.</p>
         * 
         * <pre><code>
         * Integer[][] array = {{1, 2}, {3, 4, 5}, {6}};
         * Integer[] flattened = ff.flatten(array);
         * // flattened is {1, 2, 3, 4, 5, 6}
         * </code></pre>
         * 
         * @param <T> the component type of the array elements
         * @param a the two-dimensional array to flatten. Must not be null
         * @return a new one-dimensional array containing all elements from the input array
         */
        public static <T> T[] flatten(final T[][] a) {
            final int count = Numbers.toIntExact(totalCountOfElements(a));

            final Class<T> componentType = (Class<T>) a.getClass().getComponentType().getComponentType();
            final T[] c = N.newArray(componentType, count);
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
         * Performs an operation on a flattened view of a two-dimensional array, then copies the
         * modified elements back to the original array structure.
         * 
         * <p>This method is particularly useful for operations that need to work on all elements
         * as a single sequence, such as sorting all elements across the entire 2D array.</p>
         * 
         * <pre><code>
         * Integer[][] array = {{3, 1, 4}, {1, 5, 9}};
         * ff.flatOp(array, arr -> java.util.Arrays.sort(arr));
         * // array is now {{1, 1, 3}, {4, 5, 9}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the array
         * @param <E> the type of exception that may be thrown by the operation
         * @param a the two-dimensional array to operate on. The operation modifies this array in-place
         * @param op the consumer that operates on the flattened array. Must not be null
         * @throws E if the operation throws an exception
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
         * Maps each element of a two-dimensional array to a new value using the provided function.
         * The mapping preserves the structure of the original array.
         * 
         * <p>This method creates a new array with the same dimensions as the input array,
         * where each element is the result of applying the function to the corresponding input element.</p>
         * 
         * <pre><code>
         * Integer[][] array = {{1, 2}, {3, 4}};
         * Integer[][] doubled = ff.map(array, x -> x * 2);
         * // doubled is {{2, 4}, {6, 8}}
         * </code></pre>
         * 
         * @param <T> the type of elements in both source and result arrays
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source two-dimensional array. If null, returns null
         * @param func the unary operator to apply to each element. Must not be null
         * @return a new two-dimensional array with mapped elements, or null if input is null
         * @throws E if the function throws an exception during mapping
         */
        public static <T, E extends Exception> T[][] map(final T[][] a, final Throwables.UnaryOperator<T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            return map(a, func, (Class<T>) a.getClass().getComponentType().getComponentType());
        }

        /**
         * Maps each element of a two-dimensional array to a new type using the provided function.
         * This method allows transformation between different element types.
         * 
         * <p>The resulting array has the same structure as the input array but with elements
         * of the target type.</p>
         * 
         * <pre><code>
         * String[][] array = {{"1", "2"}, {"3", "4"}};
         * Integer[][] numbers = ff.map(array, Integer::parseInt, Integer.class);
         * // numbers is {{1, 2}, {3, 4}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the source array
         * @param <R> the type of elements in the result array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source two-dimensional array. If null, returns null
         * @param func the function to transform each element. Must not be null
         * @param targetElementType the class of the target element type. Must not be null
         * @return a new two-dimensional array with transformed elements, or null if input is null
         * @throws E if the function throws an exception during mapping
         */
        public static <T, R, E extends Exception> R[][] map(final T[][] a, final Throwables.Function<? super T, ? extends R, E> func,
                final Class<R> targetElementType) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final R[][] c = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

            for (int i = 0; i < len; i++) {
                c[i] = com.landawn.abacus.util.Arrays.f.map(a[i], func, targetElementType);
            }

            return c;
        }

        /**
         * Maps each element of a two-dimensional array to a boolean value using the provided predicate.
         * This method is useful for creating boolean masks or condition arrays.
         * 
         * <pre><code>
         * Integer[][] array = {{1, 2, 3}, {4, 5, 6}};
         * boolean[][] evens = ff.mapToBoolean(array, x -> x % 2 == 0);
         * // evens is {{false, true, false}, {true, false, true}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source two-dimensional array. If null, returns null
         * @param func the predicate function to test each element. Must not be null
         * @return a new two-dimensional boolean array, or null if input is null
         * @throws E if the function throws an exception during mapping
         */
        public static <T, E extends Exception> boolean[][] mapToBoolean(final T[][] a, final Throwables.ToBooleanFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final boolean[][] c = new boolean[len][];

            for (int i = 0; i < len; i++) {
                c[i] = com.landawn.abacus.util.Arrays.f.mapToBoolean(a[i], func);
            }

            return c;
        }

        /**
         * Maps each element of a two-dimensional array to a char value using the provided function.
         * This method is useful for character-based transformations.
         * 
         * <pre><code>
         * String[][] array = {{"apple", "banana"}, {"cat", "dog"}};
         * char[][] firstChars = ff.mapToChar(array, s -> s.charAt(0));
         * // firstChars is {{'a', 'b'}, {'c', 'd'}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source two-dimensional array. If null, returns null
         * @param func the function to extract a char from each element. Must not be null
         * @return a new two-dimensional char array, or null if input is null
         * @throws E if the function throws an exception during mapping
         */
        public static <T, E extends Exception> char[][] mapToChar(final T[][] a, final Throwables.ToCharFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final char[][] c = new char[len][];

            for (int i = 0; i < len; i++) {
                c[i] = com.landawn.abacus.util.Arrays.f.mapToChar(a[i], func);
            }

            return c;
        }

        /**
         * Maps each element of a two-dimensional array to a byte value using the provided function.
         * This method is useful for byte-level transformations or data compression.
         * 
         * <pre><code>
         * Integer[][] array = {{10, 20}, {30, 40}};
         * byte[][] bytes = ff.mapToByte(array, Integer::byteValue);
         * // bytes is {{10, 20}, {30, 40}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source two-dimensional array. If null, returns null
         * @param func the function to convert each element to a byte. Must not be null
         * @return a new two-dimensional byte array, or null if input is null
         * @throws E if the function throws an exception during mapping
         */
        public static <T, E extends Exception> byte[][] mapToByte(final T[][] a, final Throwables.ToByteFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final byte[][] c = new byte[len][];

            for (int i = 0; i < len; i++) {
                c[i] = com.landawn.abacus.util.Arrays.f.mapToByte(a[i], func);
            }

            return c;
        }

        /**
         * Maps each element of a two-dimensional array to a short value using the provided function.
         * This method provides type-safe conversion to short primitive arrays.
         * 
         * <pre><code>
         * Integer[][] array = {{100, 200}, {300, 400}};
         * short[][] shorts = ff.mapToShort(array, Integer::shortValue);
         * // shorts is {{100, 200}, {300, 400}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source two-dimensional array. If null, returns null
         * @param func the function to convert each element to a short. Must not be null
         * @return a new two-dimensional short array, or null if input is null
         * @throws E if the function throws an exception during mapping
         */
        public static <T, E extends Exception> short[][] mapToShort(final T[][] a, final Throwables.ToShortFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final short[][] c = new short[len][];

            for (int i = 0; i < len; i++) {
                c[i] = com.landawn.abacus.util.Arrays.f.mapToShort(a[i], func);
            }

            return c;
        }

        /**
         * Maps each element of a two-dimensional array to an int value using the provided function.
         * This is one of the most commonly used primitive mapping operations.
         * 
         * <pre><code>
         * String[][] array = {{"10", "20"}, {"30", "40"}};
         * int[][] numbers = ff.mapToInt(array, Integer::parseInt);
         * // numbers is {{10, 20}, {30, 40}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source two-dimensional array. If null, returns null
         * @param func the function to convert each element to an int. Must not be null
         * @return a new two-dimensional int array, or null if input is null
         * @throws E if the function throws an exception during mapping
         */
        public static <T, E extends Exception> int[][] mapToInt(final T[][] a, final Throwables.ToIntFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final int[][] c = new int[len][];

            for (int i = 0; i < len; i++) {
                c[i] = com.landawn.abacus.util.Arrays.f.mapToInt(a[i], func);
            }

            return c;
        }

        /**
         * Maps each element of a two-dimensional array to a long value using the provided function.
         * This method is useful for converting to long primitive arrays for large numeric values.
         * 
         * <pre><code>
         * String[][] array = {{"1000000", "2000000"}, {"3000000", "4000000"}};
         * long[][] longs = ff.mapToLong(array, Long::parseLong);
         * // longs is {{1000000L, 2000000L}, {3000000L, 4000000L}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source two-dimensional array. If null, returns null
         * @param func the function to convert each element to a long. Must not be null
         * @return a new two-dimensional long array, or null if input is null
         * @throws E if the function throws an exception during mapping
         */
        public static <T, E extends Exception> long[][] mapToLong(final T[][] a, final Throwables.ToLongFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final long[][] c = new long[len][];

            for (int i = 0; i < len; i++) {
                c[i] = com.landawn.abacus.util.Arrays.f.mapToLong(a[i], func);
            }

            return c;
        }

        /**
         * Maps each element of a two-dimensional array to a float value using the provided function.
         * This method enables conversion to floating-point primitive arrays.
         * 
         * <pre><code>
         * String[][] array = {{"1.5", "2.5"}, {"3.5", "4.5"}};
         * float[][] floats = ff.mapToFloat(array, Float::parseFloat);
         * // floats is {{1.5f, 2.5f}, {3.5f, 4.5f}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source two-dimensional array. If null, returns null
         * @param func the function to convert each element to a float. Must not be null
         * @return a new two-dimensional float array, or null if input is null
         * @throws E if the function throws an exception during mapping
         */
        public static <T, E extends Exception> float[][] mapToFloat(final T[][] a, final Throwables.ToFloatFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final float[][] c = new float[len][];

            for (int i = 0; i < len; i++) {
                c[i] = com.landawn.abacus.util.Arrays.f.mapToFloat(a[i], func);
            }

            return c;
        }

        /**
         * Maps each element of a two-dimensional array to a double value using the provided function.
         * This method provides the highest precision for numeric conversions.
         * 
         * <pre><code>
         * String[][] array = {{"1.234", "2.345"}, {"3.456", "4.567"}};
         * double[][] doubles = ff.mapToDouble(array, Double::parseDouble);
         * // doubles is {{1.234, 2.345}, {3.456, 4.567}}
         * </code></pre>
         * 
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source two-dimensional array. If null, returns null
         * @param func the function to convert each element to a double. Must not be null
         * @return a new two-dimensional double array, or null if input is null
         * @throws E if the function throws an exception during mapping
         */
        public static <T, E extends Exception> double[][] mapToDouble(final T[][] a, final Throwables.ToDoubleFunction<? super T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            final int len = N.len(a);
            final double[][] c = new double[len][];

            for (int i = 0; i < len; i++) {
                c[i] = com.landawn.abacus.util.Arrays.f.mapToDouble(a[i], func);
            }

            return c;
        }

        /**
         * Combines corresponding elements from two 2D arrays using a binary function.
         * The result has dimensions equal to the minimum dimensions of the input arrays.
         * 
         * <p>This method pairs elements at the same positions and applies the zip function.
         * If arrays have different dimensions, extra elements are ignored.</p>
         * 
         * <pre><code>
         * Integer[][] a = {{1, 2}, {3, 4}};
         * Integer[][] b = {{10, 20}, {30, 40}};
         * Integer[][] sums = ff.zip(a, b, (x, y) -> x + y);
         * // sums is {{11, 22}, {33, 44}}
         * </code></pre>
         * 
         * @param <A> the element type of the first array and the result
         * @param <B> the element type of the second array
         * @param <E> the type of exception that may be thrown by the zip function
         * @param a the first 2D array. Must not be null
         * @param b the second 2D array. Must not be null
         * @param zipFunction the function to combine paired elements. Must not be null
         * @return a new 2D array containing the combined elements
         * @throws E if the zip function throws an exception
         */
        public static <A, B, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction)
                throws E {
            return zip(a, b, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
        }

        /**
         * Combines corresponding elements from two 2D arrays into a new array of a specified type.
         * This method allows the result type to differ from the input types.
         * 
         * <pre><code>
         * Integer[][] a = {{1, 2}, {3, 4}};
         * String[][] b = {{"a", "b"}, {"c", "d"}};
         * String[][] combined = ff.zip(a, b, (i, s) -> i + s, String.class);
         * // combined is {{"1a", "2b"}, {"3c", "4d"}}
         * </code></pre>
         * 
         * @param <A> the element type of the first array
         * @param <B> the element type of the second array
         * @param <R> the element type of the result array
         * @param <E> the type of exception that may be thrown by the zip function
         * @param a the first 2D array. Must not be null
         * @param b the second 2D array. Must not be null
         * @param zipFunction the function to combine paired elements. Must not be null
         * @param targetElementType the class of the result element type. Must not be null
         * @return a new 2D array of the specified type containing combined elements
         * @throws E if the zip function throws an exception
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
         * Combines elements from two 2D arrays with default values for missing elements.
         * The result has dimensions equal to the maximum dimensions of the input arrays.
         * 
         * <p>When one array is shorter than the other, the default values are used
         * for the missing elements during combination.</p>
         * 
         * <pre><code>
         * Integer[][] a = {{1, 2}, {3}};
         * Integer[][] b = {{10}, {30, 40}};
         * Integer[][] sums = ff.zip(a, b, 0, 0, (x, y) -> x + y);
         * // sums is {{11, 2}, {33, 40}}
         * </code></pre>
         * 
         * @param <A> the element type of the first array and the result
         * @param <B> the element type of the second array
         * @param <E> the type of exception that may be thrown by the zip function
         * @param a the first 2D array. Can be null
         * @param b the second 2D array. Can be null
         * @param valueForNoneA default value used when first array has no element at a position
         * @param valueForNoneB default value used when second array has no element at a position
         * @param zipFunction the function to combine elements. Must not be null
         * @return a new 2D array with combined elements using defaults where needed
         * @throws E if the zip function throws an exception
         */
        public static <A, B, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
            return zip(a, b, valueForNoneA, valueForNoneB, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
        }

        /**
         * Combines elements from two 2D arrays into a result array of a specified type,
         * using default values for missing elements.
         * 
         * <p>This is the most flexible zip operation, allowing different input and output types
         * while handling arrays of different sizes gracefully.</p>
         * 
         * <pre><code>
         * Integer[][] a = {{1}, {2, 3}};
         * String[][] b = {{"X", "Y"}, {"Z"}};
         * String[][] result = ff.zip(a, b, 0, "-", (i, s) -> i + s, String.class);
         * // result is {{"1X", "0Y"}, {"2Z", "3-"}}
         * </code></pre>
         * 
         * @param <A> the element type of the first array
         * @param <B> the element type of the second array
         * @param <R> the element type of the result array
         * @param <E> the type of exception that may be thrown by the zip function
         * @param a the first 2D array. Can be null
         * @param b the second 2D array. Can be null
         * @param valueForNoneA default value for the first array
         * @param valueForNoneB default value for the second array
         * @param zipFunction the function to combine elements. Must not be null
         * @param targetElementType the class of the result element type. Must not be null
         * @return a new 2D array of the specified type with combined elements
         * @throws E if the zip function throws an exception
         */
        public static <A, B, R, E extends Exception> R[][] zip(final A[][] a, final B[][] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, ? extends R, E> zipFunction, final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);
            final int len = N.max(lenA, lenB); // Use max to ensure we cover the longer array

            final R[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

            for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
                result[i] = zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
            }

            if (lenA < len) {
                for (int i = lenA; i < len; i++) {
                    result[i] = zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
                }
            } else if (lenB < len) {
                for (int i = lenB; i < len; i++) {
                    result[i] = zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction, targetElementType);
                }
            }

            return result;
        }

        /**
         * Combines corresponding elements from three 2D arrays using a tri-function.
         * The result has dimensions equal to the minimum dimensions of all input arrays.
         * 
         * <pre><code>
         * Integer[][] a = {{1, 2}, {3, 4}};
         * Integer[][] b = {{10, 20}, {30, 40}};
         * Integer[][] c = {{100, 200}, {300, 400}};
         * Integer[][] sums = ff.zip(a, b, c, (x, y, z) -> x + y + z);
         * // sums is {{111, 222}, {333, 444}}
         * </code></pre>
         * 
         * @param <A> the element type of all arrays and the result
         * @param <B> the element type of the second array
         * @param <C> the element type of the third array
         * @param <E> the type of exception that may be thrown
         * @param a the first 2D array. Must not be null
         * @param b the second 2D array. Must not be null
         * @param c the third 2D array. Must not be null
         * @param zipFunction the function to combine three elements. Must not be null
         * @return a new 2D array containing the combined elements
         * @throws E if the zip function throws an exception
         */
        public static <A, B, C, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final C[][] c,
                final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
            return zip(a, b, c, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
        }

        /**
         * Combines elements from three 2D arrays into a new array of a specified type.
         * This method allows flexible type transformation when combining three arrays.
         * 
         * <pre><code>
         * Integer[][] a = {{1}, {2}};
         * Double[][] b = {{1.5, 2.5}, {3.5}};
         * String[][] c = {{"A"}, {"B", "C"}};
         * String[][] result = ff.zip(a, b, c, (i, d, s) -> i + ":" + d + ":" + s, String.class);
         * // result is {{"1:1.5:A"}}
         * </code></pre>
         * 
         * @param <A> the element type of the first array
         * @param <B> the element type of the second array
         * @param <C> the element type of the third array
         * @param <R> the element type of the result array
         * @param <E> the type of exception that may be thrown
         * @param a the first 2D array. Must not be null
         * @param b the second 2D array. Must not be null
         * @param c the third 2D array. Must not be null
         * @param zipFunction the function to combine three elements. Must not be null
         * @param targetElementType the class of the result element type. Must not be null
         * @return a new 2D array of the specified type
         * @throws E if the zip function throws an exception
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
         * Combines elements from three 2D arrays with default values for missing elements.
         * The result has dimensions equal to the maximum dimensions of all input arrays.
         * 
         * <pre><code>
         * Integer[][] a = {{1}, {2, 3}};
         * Integer[][] b = {{10, 20}};
         * Integer[][] c = {{100}, {200, 300}};
         * Integer[][] sums = ff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
         * // sums is {{111, 20}, {202, 303}}
         * </code></pre>
         * 
         * @param <A> the element type of all arrays and the result
         * @param <B> the element type of the second array
         * @param <C> the element type of the third array
         * @param <E> the type of exception that may be thrown
         * @param a the first 2D array. Can be null
         * @param b the second 2D array. Can be null
         * @param c the third 2D array. Can be null
         * @param valueForNoneA default value for the first array
         * @param valueForNoneB default value for the second array
         * @param valueForNoneC default value for the third array
         * @param zipFunction the function to combine three elements. Must not be null
         * @return a new 2D array with combined elements using defaults where needed
         * @throws E if the zip function throws an exception
         */
        public static <A, B, C, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final C[][] c, final A valueForNoneA, final B valueForNoneB,
                final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
            return zip(a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
        }

        /**
         * Combines elements from three 2D arrays into a result array of a specified type,
         * using default values for missing elements. This is the most flexible three-array zip operation.
         * 
         * <pre><code>
         * Integer[][] a = {{1}};
         * String[][] b = {{"X", "Y"}};
         * Double[][] c = {{0.5}, {1.5, 2.5}};
         * String[][] result = ff.zip(a, b, c, 0, "-", 0.0, 
         *                           (i, s, d) -> i + s + d, String.class);
         * // result is {{"1X0.5", "0Y0.0"}, {"0-1.5", "0-2.5"}}
         * </code></pre>
         * 
         * @param <A> the element type of the first array
         * @param <B> the element type of the second array
         * @param <C> the element type of the third array
         * @param <R> the element type of the result array
         * @param <E> the type of exception that may be thrown
         * @param a the first 2D array. Can be null
         * @param b the second 2D array. Can be null
         * @param c the third 2D array. Can be null
         * @param valueForNoneA default value for the first array
         * @param valueForNoneB default value for the second array
         * @param valueForNoneC default value for the third array
         * @param zipFunction the function to combine three elements. Must not be null
         * @param targetElementType the class of the result element type. Must not be null
         * @return a new 2D array of the specified type with combined elements
         * @throws E if the zip function throws an exception
         */
        public static <A, B, C, R, E extends Exception> R[][] zip(final A[][] a, final B[][] b, final C[][] c, final A valueForNoneA, final B valueForNoneB,
                final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, ? extends R, E> zipFunction,
                final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);
            final int lenC = N.len(c);
            final int len = N.max(lenA, lenB, lenC); // Use max to ensure we cover the longest array

            final R[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

            for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
                result[i] = zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction, targetElementType);
            }

            if (N.min(lenA, lenB, lenC) < len) {
                for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                    result[i] = zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                            zipFunction, targetElementType);
                }
            }

            return result;
        }

        /**
         * A private helper method to zip elements of two 1D arrays into a result array.
         * @param a first array
         * @param b second array
         * @param valueForNoneA default value for first array
         * @param valueForNoneB default value for second array
         * @param zipFunction function to combine elements
         * @param targetElementType class of the result element type
         *
         * @param <A> type of first array element
         * @param <B> type of second array element
         * @param <R> type of result array element
         * @param <E> type of exception
         * @return a new array of zipped elements
         * @throws E if the zip function fails
         */
        private static <A, B, R, E extends Exception> R[] zip(final A[] a, final B[] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, ? extends R, E> zipFunction, final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);
            final int len = N.max(lenA, lenB); // Use max to ensure we cover the longer array

            final R[] result = N.newArray(targetElementType, len);

            for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
                result[i] = zipFunction.apply(a[i], b[i]);
            }

            if (lenA < len) {
                for (int i = lenA; i < len; i++) {
                    result[i] = zipFunction.apply(valueForNoneA, b[i]);
                }
            } else if (lenB < len) {
                for (int i = lenB; i < len; i++) {
                    result[i] = zipFunction.apply(a[i], valueForNoneB);
                }
            }

            return result;
        }

        /**
         * A private helper method to zip elements of three 1D arrays into a result array.
         *
         * @param <A> type of first array element
         * @param <B> type of second array element
         * @param <C> type of third array element
         * @param <R> type of result array element
         * @param <E> type of exception
         * @param a first array
         * @param b second array
         * @param c third array
         * @param valueForNoneA default value for first array
         * @param valueForNoneB default value for second array
         * @param valueForNoneC default value for third array
         * @param zipFunction function to combine elements
         * @param targetElementType class of the result element type
         * @return a new array of zipped elements
         * @throws E if the zip function fails
         */
        private static <A, B, C, R, E extends Exception> R[] zip(final A[] a, final B[] b, final C[] c, final A valueForNoneA, final B valueForNoneB,
                final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, ? extends R, E> zipFunction,
                final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);
            final int lenC = N.len(c);
            final int len = N.max(lenA, lenB, lenC); // Use max to ensure we cover the longest array

            final R[] result = N.newArray(targetElementType, len);

            for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
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
         * Calculates the total number of elements across all sub-arrays in a two-dimensional array.
         * This method correctly handles jagged arrays and counts null sub-arrays as having zero elements.
         * 
         * <pre><code>
         * Object[][] array = {{1, 2, 3}, {4, 5}, null, {6}};
         * long total = ff.totalCountOfElements(array);
         * // total is 6 (3 + 2 + 0 + 1)
         * </code></pre>
         * 
         * @param a the two-dimensional array to count elements in. Can be null
         * @return the total number of elements across all sub-arrays, or 0 if the array is null
         */
        public static long totalCountOfElements(final Object[][] a) {
            if (N.isEmpty(a)) {
                return 0;
            }
            long count = 0;

            for (final Object[] element : a) {
                count += (element == null ? 0 : element.length);
            }

            return count;
        }

        /**
         * Finds the minimum length among all sub-arrays in a two-dimensional array.
         * Null sub-arrays are treated as having length 0.
         * 
         * <pre><code>
         * Object[][] array = {{1, 2, 3}, {4, 5}, null, {6}};
         * int minLen = ff.minSubArrayLen(array);
         * // minLen is 0 (due to the null sub-array)
         * </code></pre>
         * 
         * @param a the two-dimensional array to examine. Can be null
         * @return the minimum sub-array length, or 0 if the array is null or empty
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
         * Finds the maximum length among all sub-arrays in a two-dimensional array.
         * Null sub-arrays are treated as having length 0.
         * 
         * <pre><code>
         * Object[][] array = {{1, 2, 3}, {4, 5}, null, {6}};
         * int maxLen = ff.maxSubArrayLen(array);
         * // maxLen is 3 (from the first sub-array)
         * </code></pre>
         * 
         * @param a the two-dimensional array to examine. Can be null
         * @return the maximum sub-array length, or 0 if the array is null or empty
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
         * Creates a string representation of a two-dimensional array and prints it to the console.
         * Each sub-array is formatted on a separate line for readability.
         * 
         * <pre><code>
         * String[][] array = {{"a", "b"}, {"c", "d", "e"}};
         * String result = ff.println(array);
         * // Prints:
         * // [[a, b],
         * //  [c, d, e]]
         * // Returns the same string
         * </code></pre>
         * 
         * @param a the two-dimensional array to print. Can be null
         * @return the string representation that was printed
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
                final String ARRAY_PRINT_SEPARATOR = System.lineSeparator();

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
     * A utility class providing functional-style operations on three-dimensional arrays.
     * This class offers a comprehensive set of methods for manipulating 3D arrays including
     * updating, replacing, reshaping, flattening, mapping, and zipping operations.
     * 
     * <p>The class follows a functional programming paradigm, allowing for declarative
     * transformations of three-dimensional array data structures. All methods are static
     * and the class cannot be instantiated.</p>
     * 
     * <p>Common use cases include:
     * <ul>
     *   <li>Bulk updates of array elements using functions</li>
     *   <li>Conditional replacement of values</li>
     *   <li>Reshaping between different dimensional representations</li>
     *   <li>Type conversions through mapping operations</li>
     *   <li>Combining multiple arrays through zip operations</li>
     * </ul>
     * </p>
     * 
     * @author Landawn Abacus
     * @since 1.0
     */
    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
    public static final class fff { // NOSONAR

        /**
         * Private constructor to prevent instantiation of this utility class.
         * This class is designed to be used statically only.
         * 
         * @throws AssertionError if instantiation is attempted
         */
        private fff() {
            // Singleton
        }

        /**
         * Updates all elements in a three-dimensional array by applying the specified unary operator
         * to each element. The operation modifies the array in-place, meaning the original array
         * is changed rather than creating a new array.
         * 
         * <p>The operator is applied to every non-null element in the array, traversing through
         * all three dimensions. Null elements at any level are skipped.</p>
         *
         * <pre><code>
         * String[][][] arr = {{{"hello", "world"}}, {{"foo", "bar"}}};
         * fff.updateAll(arr, str -> str.toUpperCase());
         * // arr is now {{{"HELLO", "WORLD"}}, {{"FOO", "BAR"}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the array
         * @param <E> the type of exception that may be thrown by the operator
         * @param a the three-dimensional array to update. Must not be null but can be empty
         * @param operator the unary operator to apply to each element. Must not be null
         * @throws E if the operator throws an exception during execution
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
         * Replaces all elements in a three-dimensional array that satisfy the given predicate
         * with the specified new value. The replacement is performed in-place, modifying the
         * original array rather than creating a new one.
         * 
         * <p>This method is useful for conditional bulk updates, such as replacing all null
         * values, replacing values that meet certain criteria, or normalizing data.</p>
         *
         * <pre><code>
         * Integer[][][] arr = {{{1, 2, null}}, {{3, null, 5}}};
         * fff.replaceIf(arr, val -> val == null, 0);
         * // arr is now {{{1, 2, 0}}, {{3, 0, 5}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the array
         * @param <E> the type of exception that may be thrown by the predicate
         * @param a the three-dimensional array to modify. Must not be null but can be empty
         * @param predicate the condition to test for each element. Elements that match will be replaced
         * @param newValue the value to replace matching elements with. Can be null
         * @throws E if the predicate throws an exception during evaluation
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
         * Reshapes a one-dimensional array into a three-dimensional array with the specified
         * dimensions. The method distributes elements from the input array into a 3D structure
         * where each 2D slice has the specified number of rows and columns.
         * 
         * <p>The reshaping process fills the 3D array in order, creating new 2D slices as needed.
         * If the total number of elements doesn't evenly divide into the specified dimensions,
         * the last slices may be partially filled.</p>
         *
         * <pre><code>
         * Integer[] flat = {1, 2, 3, 4, 5, 6, 7, 8};
         * Integer[][][] reshaped = fff.reshape(flat, 2, 2);
         * // reshaped is {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the array
         * @param a the one-dimensional array to reshape. Must not be null
         * @param rows the number of rows for each 2D slice. Must be positive
         * @param cols the number of columns for each 2D slice. Must be positive
         * @return a new three-dimensional array containing all elements from the input
         * @throws IllegalArgumentException if rows or cols are not positive numbers
         */
        public static <T> T[][][] reshape(final T[] a, final int rows, final int cols) throws IllegalArgumentException {
            N.checkArgument(rows > 0 && cols > 0, "'rows' and 'cols' must be positive numbers: rows = %s, cols = %s", rows, cols);

            //        if (N.isEmpty(a)) {
            //            return new T[0][0][0];
            //        }

            final Class<T[]> arrayClass = (Class<T[]>) a.getClass();
            final int len = a.length;
            final int n = Numbers.divide(len, rows * cols, RoundingMode.CEILING);

            final T[][][] c = N.newArray(N.newArray(arrayClass, 0).getClass(), n);

            for (int i = 0, from = 0; i < n; i++) {
                c[i] = N.newArray(arrayClass, N.min(rows, Numbers.divide(len - from, cols, RoundingMode.CEILING)));

                for (int j = 0, y = c[i].length; j < y; j++, from += cols) {
                    c[i][j] = N.copyOfRange(a, from, from + N.min(len - from, cols));
                }
            }

            return c;
        }

        /**
         * Flattens a three-dimensional array into a one-dimensional array by concatenating
         * all elements in their natural order. The method traverses the array depth-first,
         * preserving the order of elements as they appear in the original structure.
         * 
         * <p>This operation is the inverse of reshape, converting a multi-dimensional
         * structure back into a linear representation. Empty sub-arrays are skipped.</p>
         *
         * <pre><code>
         * Integer[][][] cube = {{{1, 2}}, {{3}}, {{4, 5, 6}}};
         * Integer[] flat = fff.flatten(cube);
         * // flat is {1, 2, 3, 4, 5, 6}
         * </code></pre>
         *
         * @param <T> the component type of the array elements
         * @param a the three-dimensional array to flatten. Must not be null
         * @return a new one-dimensional array containing all elements in order
         */
        public static <T> T[] flatten(final T[][][] a) {
            final int count = Numbers.toIntExact(totalCountOfElements(a));

            final Class<T> componentType = (Class<T>) a.getClass().getComponentType().getComponentType().getComponentType();
            final T[] c = N.newArray(componentType, count);
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
         * Flattens a three-dimensional array, applies an operation to the flattened result,
         * then copies the modified elements back into the original 3D structure. This method
         * is particularly useful for operations that are easier to perform on a linear array,
         * such as sorting all elements regardless of their position in the 3D structure.
         * 
         * <p>The operation preserves the original shape of the 3D array while allowing
         * transformations that work on the entire dataset as a single unit.</p>
         *
         * <pre><code>
         * Integer[][][] arr = {{{5, 2}}, {{9, 1}}, {{3, 7}}};
         * fff.flatOp(arr, flat -> java.util.Arrays.sort(flat));
         * // arr is now {{{1, 2}}, {{3, 5}}, {{7, 9}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the array
         * @param <E> the type of exception that may be thrown by the operation
         * @param a the three-dimensional array to operate on. Modified in-place
         * @param op the consumer operation to apply to the flattened array
         * @throws E if the operation throws an exception
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
         * Maps each element in a three-dimensional array to a new value using the provided
         * unary operator, creating a new array with the same structure but transformed elements.
         * The original array is not modified.
         * 
         * <p>This method preserves the exact structure of the input array, including any
         * null elements or empty sub-arrays. Only non-null elements are transformed.</p>
         *
         * <pre><code>
         * Integer[][][] numbers = {{{1, 2}}, {{3, 4}}};
         * Integer[][][] doubled = fff.map(numbers, n -> n * 2);
         * // doubled is {{{2, 4}}, {{6, 8}}}
         * </code></pre>
         *
         * @param <T> the type of elements in both source and result arrays
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source three-dimensional array. Can be null
         * @param func the unary operator to apply to each element
         * @return a new array with mapped elements, or null if input is null
         * @throws E if the function throws an exception
         */
        public static <T, E extends Exception> T[][][] map(final T[][][] a, final Throwables.UnaryOperator<T, E> func) throws E {
            if (a == null) {
                return null; // NOSONAR
            }

            return map(a, func, (Class<T>) a.getClass().getComponentType().getComponentType().getComponentType());
        }

        /**
         * Maps each element in a three-dimensional array of type T to a new three-dimensional
         * array of type R by applying a transformation function. This method allows for type
         * conversion while preserving the array structure.
         * 
         * <p>The resulting array has the same dimensions as the input array, with each element
         * transformed according to the provided function. This is useful for converting between
         * different data types or applying complex transformations.</p>
         *
         * <pre><code>
         * String[][][] strings = {{{"1", "2"}}, {{"3", "4"}}};
         * Integer[][][] numbers = fff.map(strings, Integer::parseInt, Integer.class);
         * // numbers is {{{1, 2}}, {{3, 4}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the source array
         * @param <R> the type of elements in the result array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source three-dimensional array. Can be null
         * @param func the function to transform each element
         * @param targetElementType the class of the result array's element type
         * @return a new array with transformed elements, or null if input is null
         * @throws E if the function throws an exception
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
         * Maps a three-dimensional array to a three-dimensional boolean array by applying
         * a predicate function to each element. This is useful for creating masks or
         * performing element-wise comparisons.
         * 
         * <p>The resulting array has the same structure as the input, with each element
         * replaced by the boolean result of the predicate evaluation.</p>
         *
         * <pre><code>
         * Integer[][][] numbers = {{{1, 2, 3}}, {{4, 5, 6}}};
         * boolean[][][] evenMask = fff.mapToBoolean(numbers, n -> n % 2 == 0);
         * // evenMask is {{{false, true, false}}, {{true, false, true}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source three-dimensional array. Can be null
         * @param func the predicate function to test each element
         * @return a new boolean array, or null if input is null
         * @throws E if the function throws an exception
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
         * Maps a three-dimensional array to a three-dimensional char array by applying
         * a function that extracts or computes a char value from each element.
         * 
         * <p>This method is particularly useful when working with string data where you
         * need to extract specific characters or perform character-based transformations.</p>
         *
         * <pre><code>
         * String[][][] words = {{{"apple", "banana"}}, {{"cat", "dog"}}};
         * char[][][] firstLetters = fff.mapToChar(words, s -> s.charAt(0));
         * // firstLetters is {{{'a', 'b'}}, {{'c', 'd'}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source three-dimensional array. Can be null
         * @param func the function to extract a char from each element
         * @return a new char array, or null if input is null
         * @throws E if the function throws an exception
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
         * Maps a three-dimensional array to a three-dimensional byte array by applying
         * a function that converts each element to a byte value. Useful for data compression
         * or when working with byte-oriented operations.
         * 
         * <p>Care should be taken to ensure that the conversion doesn't result in data loss
         * due to byte's limited range (-128 to 127).</p>
         *
         * <pre><code>
         * Integer[][][] numbers = {{{10, 20}}, {{30, 40}}};
         * byte[][][] bytes = fff.mapToByte(numbers, Integer::byteValue);
         * // bytes is {{{10, 20}}, {{30, 40}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source three-dimensional array. Can be null
         * @param func the function to convert each element to byte
         * @return a new byte array, or null if input is null
         * @throws E if the function throws an exception
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
         * Maps a three-dimensional array to a three-dimensional short array by applying
         * a function that converts each element to a short value. This is useful when
         * working with numeric data that fits within the short range.
         * 
         * <p>The short data type has a range of -32,768 to 32,767, making it suitable
         * for many numeric applications while using less memory than int.</p>
         *
         * <pre><code>
         * Integer[][][] numbers = {{{100, 200}}, {{300, 400}}};
         * short[][][] shorts = fff.mapToShort(numbers, Integer::shortValue);
         * // shorts is {{{100, 200}}, {{300, 400}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source three-dimensional array. Can be null
         * @param func the function to convert each element to short
         * @return a new short array, or null if input is null
         * @throws E if the function throws an exception
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
         * Maps a three-dimensional array to a three-dimensional int array by applying
         * a function that converts each element to an integer. This is one of the most
         * common mapping operations for numeric transformations.
         * 
         * <p>This method is particularly useful for parsing strings to integers or
         * extracting integer properties from complex objects.</p>
         *
         * <pre><code>
         * String[][][] stringNumbers = {{{"1", "2"}}, {{"3", "4"}}};
         * int[][][] integers = fff.mapToInt(stringNumbers, Integer::parseInt);
         * // integers is {{{1, 2}}, {{3, 4}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source three-dimensional array. Can be null
         * @param func the function to convert each element to int
         * @return a new int array, or null if input is null
         * @throws E if the function throws an exception
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
         * Maps a three-dimensional array to a three-dimensional long array by applying
         * a function that converts each element to a long value. This is useful for
         * working with large numeric values or timestamps.
         * 
         * <p>The long data type can represent values from -2^63 to 2^63-1, making it
         * suitable for applications requiring large integer values.</p>
         *
         * <pre><code>
         * String[][][] timestamps = {{{"1000000"}}, {{"2000000"}}};
         * long[][][] longs = fff.mapToLong(timestamps, Long::parseLong);
         * // longs is {{{1000000L}}, {{2000000L}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source three-dimensional array. Can be null
         * @param func the function to convert each element to long
         * @return a new long array, or null if input is null
         * @throws E if the function throws an exception
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
         * Maps a three-dimensional array to a three-dimensional float array by applying
         * a function that converts each element to a float value. This is useful for
         * applications requiring floating-point precision with moderate memory usage.
         * 
         * <p>Float provides approximately 7 decimal digits of precision and is suitable
         * for many scientific and graphics applications.</p>
         *
         * <pre><code>
         * String[][][] decimals = {{{"1.5", "2.7"}}, {{"3.14", "4.2"}}};
         * float[][][] floats = fff.mapToFloat(decimals, Float::parseFloat);
         * // floats is {{{1.5f, 2.7f}}, {{3.14f, 4.2f}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source three-dimensional array. Can be null
         * @param func the function to convert each element to float
         * @return a new float array, or null if input is null
         * @throws E if the function throws an exception
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
         * Maps a three-dimensional array to a three-dimensional double array by applying
         * a function that converts each element to a double value. This provides the
         * highest precision for floating-point operations.
         * 
         * <p>Double provides approximately 15-17 decimal digits of precision and is
         * the standard choice for scientific computing and high-precision calculations.</p>
         *
         * <pre><code>
         * String[][][] scientificData = {{{"1.23e10", "4.56e-5"}}, {{"7.89e15"}}};
         * double[][][] doubles = fff.mapToDouble(scientificData, Double::parseDouble);
         * // doubles is {{{1.23e10, 4.56e-5}}, {{7.89e15}}}
         * </code></pre>
         *
         * @param <T> the type of elements in the source array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the source three-dimensional array. Can be null
         * @param func the function to convert each element to double
         * @return a new double array, or null if input is null
         * @throws E if the function throws an exception
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
         * Combines two three-dimensional arrays element-wise using a binary function.
         * The resulting array has dimensions equal to the minimum dimensions of the input
         * arrays at each level, effectively truncating to the smaller array.
         * 
         * <p>This method is useful for element-wise operations like addition, multiplication,
         * or any custom binary operation between corresponding elements.</p>
         *
         * <pre><code>
         * Integer[][][] a = {{{1, 2}}, {{3, 4}}};
         * Integer[][][] b = {{{10, 20}}, {{30, 40}}};
         * Integer[][][] sum = fff.zip(a, b, (x, y) -> x + y);
         * // sum is {{{11, 22}}, {{33, 44}}}
         * </code></pre>
         *
         * @param <A> the element type of the first array and the result
         * @param <B> the element type of the second array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the first three-dimensional array
         * @param b the second three-dimensional array
         * @param zipFunction the binary function to combine corresponding elements
         * @return a new array with combined elements
         * @throws E if the zip function throws an exception
         */
        public static <A, B, E extends Exception> A[][][] zip(final A[][][] a, final B[][][] b,
                final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
            return zip(a, b, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType().getComponentType());
        }

        /**
         * Combines two three-dimensional arrays element-wise using a binary function,
         * producing a result array of a specified type. This allows for type transformation
         * during the zip operation.
         * 
         * <p>The dimensions of the result array are the minimum of the corresponding
         * dimensions of the input arrays. This method enables combining arrays of
         * different types into a new type.</p>
         *
         * <pre><code>
         * Integer[][][] numbers = {{{1, 2}}};
         * String[][][] strings = {{{"a", "b"}}};
         * String[][][] combined = fff.zip(numbers, strings, (n, s) -> n + s, String.class);
         * // combined is {{{"1a", "2b"}}}
         * </code></pre>
         *
         * @param <A> the element type of the first array
         * @param <B> the element type of the second array
         * @param <R> the element type of the result array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the first three-dimensional array
         * @param b the second three-dimensional array
         * @param zipFunction the function to combine corresponding elements
         * @param targetElementType the class of the result array's element type
         * @return a new array with combined elements of type R
         * @throws E if the zip function throws an exception
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
         * Combines two three-dimensional arrays element-wise with default values for
         * missing elements. When arrays have different dimensions, the default values
         * are used in place of missing elements, allowing the operation to continue.
         * 
         * <p>This method is particularly useful when you want to combine arrays of
         * different sizes without truncation, filling gaps with specified defaults.</p>
         *
         * <pre><code>
         * Integer[][][] a = {{{1, 2}}};
         * Integer[][][] b = {{{10}}, {{20, 30}}};
         * Integer[][][] result = fff.zip(a, b, 0, 0, (x, y) -> x + y);
         * // result is {{{11, 2}}, {{20, 30}}}
         * </code></pre>
         *
         * @param <A> the element type of the first array and the result
         * @param <B> the element type of the second array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the first three-dimensional array
         * @param b the second three-dimensional array
         * @param valueForNoneA default value when first array element is missing
         * @param valueForNoneB default value when second array element is missing
         * @param zipFunction the function to combine elements
         * @return a new array with combined elements
         * @throws E if the zip function throws an exception
         */
        public static <A, B, E extends Exception> A[][][] zip(final A[][][] a, final B[][][] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
            return zip(a, b, valueForNoneA, valueForNoneB, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType().getComponentType());
        }

        /**
         * Combines two three-dimensional arrays element-wise into a result array of a
         * specified type, using default values for missing elements. This provides maximum
         * flexibility for combining arrays of different sizes and types.
         * 
         * <p>The result array dimensions match the maximum dimensions of the input arrays,
         * with default values filling any gaps where one array is smaller than the other.</p>
         *
         * <pre><code>
         * Integer[][][] nums = {{{1}}};
         * String[][][] strs = {{{"a", "b"}}, {{"c"}}};
         * String[][][] result = fff.zip(nums, strs, 0, "x", 
         *                               (n, s) -> n + "-" + s, String.class);
         * // result is {{{"1-a", "0-b"}}, {{"0-c"}}}
         * </code></pre>
         *
         * @param <A> the element type of the first array
         * @param <B> the element type of the second array
         * @param <R> the element type of the result array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the first three-dimensional array
         * @param b the second three-dimensional array
         * @param valueForNoneA default value when first array element is missing
         * @param valueForNoneB default value when second array element is missing
         * @param zipFunction the function to combine elements
         * @param targetElementType the class of the result array's element type
         * @return a new array with combined elements of type R
         * @throws E if the zip function throws an exception
         */
        public static <A, B, R, E extends Exception> R[][][] zip(final A[][][] a, final B[][][] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, ? extends R, E> zipFunction, final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);
            final int len = N.max(lenA, lenB);

            final R[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

            for (int i = 0, min = N.min(lenA, lenB); i < min; i++) {
                result[i] = ff.zip(a[i], b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
            }

            if (lenA < len) {
                for (int i = lenA; i < len; i++) {
                    result[i] = ff.zip(null, b[i], valueForNoneA, valueForNoneB, zipFunction, targetElementType);
                }
            } else if (lenB < len) {
                for (int i = lenB; i < len; i++) {
                    result[i] = ff.zip(a[i], null, valueForNoneA, valueForNoneB, zipFunction, targetElementType);
                }
            }

            return result;
        }

        /**
         * Combines three three-dimensional arrays element-wise using a ternary function.
         * The resulting array dimensions are truncated to the minimum dimensions of all
         * three input arrays.
         * 
         * <p>This method enables complex three-way operations on corresponding elements
         * from three different arrays, useful for operations that require three inputs.</p>
         *
         * <pre><code>
         * Integer[][][] a = {{{1, 2}}};
         * Integer[][][] b = {{{10, 20}}};
         * Integer[][][] c = {{{100, 200}}};
         * Integer[][][] sum = fff.zip(a, b, c, (x, y, z) -> x + y + z);
         * // sum is {{{111, 222}}}
         * </code></pre>
         *
         * @param <A> the element type of the first array and the result
         * @param <B> the element type of the second array
         * @param <C> the element type of the third array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the first three-dimensional array
         * @param b the second three-dimensional array
         * @param c the third three-dimensional array
         * @param zipFunction the ternary function to combine corresponding elements
         * @return a new array with combined elements
         * @throws E if the zip function throws an exception
         */
        public static <A, B, C, E extends Exception> A[][][] zip(final A[][][] a, final B[][][] b, final C[][][] c,
                final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
            return zip(a, b, c, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType().getComponentType());
        }

        /**
         * Combines three three-dimensional arrays element-wise using a ternary function,
         * producing a result array of a specified type. This allows for type transformation
         * while combining three arrays.
         * 
         * <p>The dimensions of the result array are the minimum of the corresponding
         * dimensions of all three input arrays.</p>
         *
         * <pre><code>
         * Integer[][][] nums = {{{1}}};
         * String[][][] strs = {{{"a"}}};
         * Double[][][] dbls = {{{2.5}}};
         * String[][][] result = fff.zip(nums, strs, dbls, 
         *                               (n, s, d) -> n + s + d, String.class);
         * // result is {{{"1a2.5"}}}
         * </code></pre>
         *
         * @param <A> the element type of the first array
         * @param <B> the element type of the second array
         * @param <C> the element type of the third array
         * @param <R> the element type of the result array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the first three-dimensional array
         * @param b the second three-dimensional array
         * @param c the third three-dimensional array
         * @param zipFunction the function to combine corresponding elements
         * @param targetElementType the class of the result array's element type
         * @return a new array with combined elements of type R
         * @throws E if the zip function throws an exception
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
         * Combines three three-dimensional arrays element-wise with default values for
         * missing elements. This allows combining arrays of different sizes without
         * truncation, using specified defaults where needed.
         * 
         * <p>The result array dimensions match the maximum dimensions of the input arrays,
         * with default values used whenever an array lacks an element at a given position.</p>
         *
         * <pre><code>
         * Integer[][][] a = {{{1}}};
         * Integer[][][] b = {{{10, 20}}};
         * Integer[][][] c = {{{}, {{100}}};
         * Integer[][][] result = fff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
         * // result is {{{11, 20}, {100}}}
         * </code></pre>
         *
         * @param <A> the element type of the first array and the result
         * @param <B> the element type of the second array
         * @param <C> the element type of the third array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the first three-dimensional array
         * @param b the second three-dimensional array
         * @param c the third three-dimensional array
         * @param valueForNoneA default value when first array element is missing
         * @param valueForNoneB default value when second array element is missing
         * @param valueForNoneC default value when third array element is missing
         * @param zipFunction the function to combine elements
         * @return a new array with combined elements
         * @throws E if the zip function throws an exception
         */
        public static <A, B, C, E extends Exception> A[][][] zip(final A[][][] a, final B[][][] b, final C[][][] c, final A valueForNoneA,
                final B valueForNoneB, final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
            return zip(a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction,
                    (Class<A>) a.getClass().getComponentType().getComponentType().getComponentType());
        }

        /**
         * Combines three three-dimensional arrays element-wise into a result array of a
         * specified type, using default values for missing elements. This provides maximum
         * flexibility for combining three arrays of different sizes and types.
         * 
         * <p>The result array dimensions match the maximum dimensions of all input arrays,
         * with default values filling any gaps where arrays differ in size.</p>
         *
         * <pre><code>
         * Integer[][][] a = {{{1}}};
         * String[][][] b = {{{"a", "b"}}};
         * Double[][][] c = {{{1.1}}, {{2.2}}};
         * String[][][] result = fff.zip(a, b, c, 0, "x", 0.0,
         *                               (i, s, d) -> i + s + d, String.class);
         * // result is {{{"1a1.1", "0b0.0"}}, {{"0x2.2"}}}
         * </code></pre>
         *
         * @param <A> the element type of the first array
         * @param <B> the element type of the second array
         * @param <C> the element type of the third array
         * @param <R> the element type of the result array
         * @param <E> the type of exception that may be thrown by the function
         * @param a the first three-dimensional array
         * @param b the second three-dimensional array
         * @param c the third three-dimensional array
         * @param valueForNoneA default value when first array element is missing
         * @param valueForNoneB default value when second array element is missing
         * @param valueForNoneC default value when third array element is missing
         * @param zipFunction the function to combine elements
         * @param targetElementType the class of the result array's element type
         * @return a new array with combined elements of type R
         * @throws E if the zip function throws an exception
         */
        public static <A, B, C, R, E extends Exception> R[][][] zip(final A[][][] a, final B[][][] b, final C[][][] c, final A valueForNoneA,
                final B valueForNoneB, final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, ? extends R, E> zipFunction,
                final Class<R> targetElementType) throws E {
            final int lenA = N.len(a);
            final int lenB = N.len(b);
            final int lenC = N.len(c);
            final int len = N.max(lenA, lenB, lenC);

            final R[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

            for (int i = 0, min = N.min(lenA, lenB, lenC); i < min; i++) {
                result[i] = ff.zip(a[i], b[i], c[i], valueForNoneA, valueForNoneB, valueForNoneC, zipFunction, targetElementType);
            }

            if (N.min(lenA, lenB, lenC) < len) {
                for (int i = N.min(lenA, lenB, lenC); i < len; i++) {
                    result[i] = ff.zip(i < lenA ? a[i] : null, i < lenB ? b[i] : null, i < lenC ? c[i] : null, valueForNoneA, valueForNoneB, valueForNoneC,
                            zipFunction, targetElementType);
                }
            }

            return result;
        }

        /**
         * Calculates the total number of elements in a three-dimensional array.
         * This method correctly handles jagged arrays (arrays with varying dimensions)
         * and null sub-arrays at any level.
         * 
         * <p>The count includes only actual elements, not null references to sub-arrays.
         * Empty sub-arrays contribute zero to the count.</p>
         *
         * <pre><code>
         * Object[][][] array = {{{1, 2}, {3}}, null, {{4, 5, 6}}};
         * long count = fff.totalCountOfElements(array);
         * // count is 6 (elements: 1, 2, 3, 4, 5, 6)
         * </code></pre>
         *
         * @param a the three-dimensional array to count elements in
         * @return the total number of non-null elements across all dimensions
         */
        public static long totalCountOfElements(final Object[][][] a) {
            if (N.isEmpty(a)) {
                return 0;
            }
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

        /**
         * Generates a formatted string representation of a three-dimensional array and
         * prints it to the console. The output is structured with appropriate indentation
         * and line breaks to clearly show the three-dimensional structure.
         * 
         * <p>This method is primarily intended for debugging and development purposes
         * to visualize the contents and structure of 3D arrays. Each 2D slice is
         * separated by line breaks for clarity.</p>
         *
         * <pre><code>
         * String[][][] data = {{{"a", "b"}}, {{"c", "d"}, {"e"}}};
         * String output = fff.println(data);
         * // Prints to console and returns:
         * // [[["a", "b"]],
         * //  [["c", "d"],
         * //   ["e"]]]
         * </code></pre>
         *
         * @param a the three-dimensional array to print. Can be null
         * @return the string representation that was printed to console
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
                final String ARRAY_PRINT_SEPARATOR = System.lineSeparator();

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
