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
 * A comprehensive, high-performance utility class providing extensive array manipulation and operation
 * capabilities for all primitive types and objects in single and multi-dimensional configurations.
 * This sealed class serves as the foundation for advanced array processing, mathematical computations,
 * and data transformation operations essential for scientific computing, data analysis, machine learning,
 * and enterprise application development requiring efficient array handling and numerical processing.
 *
 * <p>This class implements a sophisticated array processing framework that extends beyond Java's standard
 * {@link java.util.Arrays} utilities by providing comprehensive support for element-wise operations,
 * mathematical transformations, multi-dimensional array manipulation, and specialized functional programming
 * constructs. It addresses the limitations of Java's built-in array utilities by offering performance-optimized
 * algorithms, type-safe operations, and extensive coverage of array manipulation scenarios commonly required
 * in data-intensive applications and computational frameworks.</p>
 *
 * <p><b>Core Functionality Categories:</b>
 * <ul>
 *   <li><b>Mathematical Operations:</b> Element-wise arithmetic (add, subtract, multiply, divide)</li>
 *   <li><b>Array Transformation:</b> Reshape, flatten, transpose operations for dimension manipulation</li>
 *   <li><b>Type Conversion:</b> Safe conversion between primitive array types with precision handling</li>
 *   <li><b>Functional Operations:</b> Higher-order functions with custom operators and predicates</li>
 *   <li><b>Statistical Analysis:</b> Aggregation operations (sum, average, min, max, variance)</li>
 *   <li><b>Array Manipulation:</b> Sorting, searching, filtering, and transformation utilities</li>
 *   <li><b>Dimensional Operations:</b> Multi-dimensional array processing and navigation</li>
 *   <li><b>Utility Functions:</b> Printing, debugging, and visualization support</li>
 * </ul>
 *
 * <p><b>Supported Array Types and Dimensions:</b>
 * <table border="1" style="border-collapse: collapse;">
 *   <caption><b>Array Type Support Matrix</b></caption>
 *   <tr style="background-color: #f2f2f2;">
 *     <th>Type Category</th>
 *     <th>Primitive Types</th>
 *     <th>Dimensions Supported</th>
 *     <th>Operations Available</th>
 *   </tr>
 *   <tr>
 *     <td>Boolean</td>
 *     <td>boolean</td>
 *     <td>1D, 2D, 3D</td>
 *     <td>Logical operations, conversion</td>
 *   </tr>
 *   <tr>
 *     <td>Character</td>
 *     <td>char</td>
 *     <td>1D, 2D, 3D</td>
 *     <td>Character manipulation, conversion</td>
 *   </tr>
 *   <tr>
 *     <td>Integer Types</td>
 *     <td>byte, short, int, long</td>
 *     <td>1D, 2D, 3D</td>
 *     <td>Full mathematical operations</td>
 *   </tr>
 *   <tr>
 *     <td>Floating Point</td>
 *     <td>float, double</td>
 *     <td>1D, 2D, 3D</td>
 *     <td>Mathematical operations, precision control</td>
 *   </tr>
 *   <tr>
 *     <td>Object Types</td>
 *     <td>Generic objects</td>
 *     <td>1D, 2D, 3D</td>
 *     <td>Functional operations, transformations</td>
 *   </tr>
 * </table>
 *
 * <p><b>Key Design Principles:</b>
 * <ul>
 *   <li><b>Performance First:</b> Optimized algorithms with minimal memory allocation and efficient iteration</li>
 *   <li><b>Type Safety:</b> Comprehensive generic support with compile-time type checking</li>
 *   <li><b>Null Safety:</b> Robust null handling with clear behavior specifications</li>
 *   <li><b>Functional Style:</b> Higher-order functions with lambda expression support</li>
 *   <li><b>Immutability Preference:</b> Operations return new arrays rather than modifying inputs</li>
 *   <li><b>Exception Clarity:</b> Clear exception handling with descriptive error messages</li>
 * </ul>
 *
 * <p><b>Array Transformation Operations:</b>
 * <pre>{@code
 * // Flatten multi-dimensional arrays
 * int[][] matrix = {{1, 2, 3}, {4, 5, 6}};
 * int[] flattened = Arrays.flatten(matrix); // {1, 2, 3, 4, 5, 6}
 *
 * // Type conversion operations
 * int[] integers = {1, 2, 3, 4};
 * double[] doubles = Arrays.toDouble(integers); // {1.0, 2.0, 3.0, 4.0}
 * float[] floats = Arrays.toFloat(integers);    // {1.0f, 2.0f, 3.0f, 4.0f}
 * }</pre>
 *
 * <p><b>Array Reshaping Operations:</b>
 * <pre>{@code
 * // Reshape arrays to different dimensions
 * // reshape(array, cols) creates rows with specified number of columns
 * int[] linear = {1, 2, 3, 4, 5, 6};
 * int[][] reshaped2D = Arrays.reshape(linear, 2); // {{1, 2}, {3, 4}, {5, 6}} - 3 rows x 2 cols
 *
 * // reshape(array, rows, cols) creates blocks of rows x cols matrices
 * int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
 * int[][][] reshaped3D = Arrays.reshape(arr, 2, 2); // {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}}
 * }</pre>
 *
 * <p><b>Functional Programming Support:</b>
 * <pre>{@code
 * // Higher-order operations with custom functions
 * int[] numbers = {1, 2, 3, 4, 5};
 * 
 * // Update all elements with a function
 * Arrays.updateAll(numbers, x -> x * x); // {1, 4, 9, 16, 25}
 * 
 * // Conditional replacement
 * Arrays.replaceIf(numbers, x -> x > 10, 0); // Replace values > 10 with 0
 * 
 * // Zip operations with custom functions
 * int[] a = {1, 2, 3};
 * int[] b = {4, 5, 6};
 * int[] zipped = Arrays.zip(a, b, (x, y) -> x + y * 2); // {9, 12, 15}
 * 
 * // Multi-array operations
 * int[] c = {7, 8, 9};
 * int[] tripleZip = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
 * }</pre>
 *
 * <p><b>Advanced Multi-Dimensional Operations:</b>
 * <pre>{@code
 * // 3D array operations
 * int[][][] cube = new int[2][3][4]; // 2x3x4 cube
 * Arrays.updateAll(cube, x -> 42); // Update all elements
 *
 * // Flatten 3D to 1D
 * int[] flattened3D = Arrays.flatten(cube); // 24 elements
 *
 * // Calculate total elements across dimensions
 * long totalElements = Arrays.totalCountOfElements(cube); // 24
 *
 * // For 2D arrays - get min/max sub-array lengths
 * int[][] matrix2D = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
 * int minLength = Arrays.minSubArrayLen(matrix2D); // 4
 * int maxLength = Arrays.maxSubArrayLen(matrix2D); // 4
 * }</pre>
 *
 * <p><b>Performance Characteristics and Optimizations:</b>
 * <ul>
 *   <li><b>Memory Efficiency:</b> Minimal object allocation with optimized iteration patterns</li>
 *   <li><b>Cache Locality:</b> Sequential access patterns for optimal CPU cache utilization</li>
 *   <li><b>Vectorization Ready:</b> Algorithms designed for JVM auto-vectorization</li>
 *   <li><b>Parallel Processing:</b> Support for parallel operations on large datasets</li>
 *   <li><b>Bounds Checking:</b> Efficient bounds validation with clear error reporting</li>
 * </ul>
 *
 * <p><b>Error Handling and Validation:</b>
 * <ul>
 *   <li><b>Null Input Handling:</b> Graceful handling with appropriate null return values</li>
 *   <li><b>Dimension Validation:</b> Comprehensive checking for reshape and transformation operations</li>
 *   <li><b>Size Compatibility:</b> Validation of array size compatibility for mathematical operations</li>
 *   <li><b>Type Safety:</b> Compile-time and runtime type checking for conversion operations</li>
 *   <li><b>Index Bounds:</b> Proper bounds checking with informative exception messages</li>
 * </ul>
 *
 * <p><b>Debugging and Visualization Support:</b>
 * <pre>{@code
 * // Array printing utilities for debugging
 * int[] array1D = {1, 2, 3, 4, 5};
 * String output1D = Arrays.println(array1D);
 * 
 * int[][] array2D = {{1, 2, 3}, {4, 5, 6}};
 * String output2D = Arrays.println(array2D);
 * 
 * int[][][] array3D = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
 * String output3D = Arrays.println(array3D);
 * 
 * // Custom formatting for scientific notation
 * double[] scientific = {1.23e-4, 2.34e5, 3.45e-2};
 * String formatted = Arrays.println(scientific);
 * }</pre>
 *
 * <p><b>Thread Safety and Concurrency:</b>
 * <ul>
 *   <li><b>Stateless Design:</b> All static methods are thread-safe and stateless</li>
 *   <li><b>Immutable Operations:</b> Methods return new arrays without modifying inputs</li>
 *   <li><b>Concurrent Access:</b> Safe for concurrent access from multiple threads</li>
 *   <li><b>Parallel Ready:</b> Compatible with parallel stream operations</li>
 * </ul>
 *
 * <p><b>Integration with Java Collections Framework:</b>
 * <ul>
 *   <li><b>Stream Compatibility:</b> Works seamlessly with Java 8+ Stream API</li>
 *   <li><b>Collection Conversion:</b> Easy conversion between arrays and collections</li>
 *   <li><b>Functional Interface Support:</b> Extensive use of functional interfaces</li>
 *   <li><b>Optional Integration:</b> Support for Optional types in appropriate contexts</li>
 * </ul>
 *
 * <p><b>Best Practices and Usage Guidelines:</b>
 * <ul>
 *   <li>Use appropriate primitive types to avoid boxing overhead in mathematical operations</li>
 *   <li>Prefer immutable operations unless in-place modification is explicitly required</li>
 *   <li>Validate array dimensions before reshape operations to avoid runtime exceptions</li>
 *   <li>Use the printing utilities for debugging multi-dimensional array structures</li>
 *   <li>Consider memory implications when working with large multi-dimensional arrays</li>
 *   <li>Leverage functional operations for complex transformations and filtering</li>
 * </ul>
 *
 * <p><b>Related Utility Classes:</b>
 * <ul>
 *   <li><b>{@link Matrix}:</b> Specialized matrix operations and linear algebra</li>
 *   <li><b>{@link Matrixes}:</b> Advanced matrix manipulation utilities</li>
 *   <li><b>{@link java.util.Arrays}:</b> Standard Java array utilities</li>
 *   <li><b>{@link java.util.stream.Stream}:</b> Stream-based array processing</li>
 * </ul>
 *
 * <p><b>Common Anti-Patterns to Avoid:</b>
 * <ul>
 *   <li>Modifying arrays during iteration without proper synchronization</li>
 *   <li>Ignoring null checks when working with potentially null arrays</li>
 *   <li>Using inappropriate data types leading to precision loss or overflow</li>
 *   <li>Performing inefficient nested loops when utility methods are available</li>
 *   <li>Memory leaks from holding references to large temporary arrays</li>
 * </ul>
 *
 * @see Matrix
 * @see Matrixes
 * @see java.util.Arrays
 * @see java.util.stream.Stream
 * @see java.lang.reflect.Array
 */
@Beta
public sealed class Arrays permits Arrays.f {

    static final char CHAR_0 = (char) 0;
    static final byte BYTE_0 = (byte) 0;
    static final byte BYTE_1 = (byte) 1;
    static final short SHORT_0 = (short) 0;
    static final String ARRAY_PRINT_SEPARATOR = IOUtil.LINE_SEPARATOR_UNIX;

    private Arrays() {
        // utility class.
    }

    /**
     * Prints a string representation of a one-dimensional object array to the console and returns the string.
     *
     * <p>This method formats a one-dimensional object array as a readable string representation with standard
     * bracket notation. The method handles null arrays and empty arrays gracefully, providing
     * appropriate string representations for each case.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[element1, element2, element3]"</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Object[] array = {"Hello", "World", 123};
     * String result = Arrays.println(array);
     * // Prints to console and returns: "[Hello, World, 123]"
     *
     * // Null array
     * String nullResult = Arrays.println((Object[]) null);
     * // Prints "null"
     *
     * // Empty array
     * Object[] empty = new Object[0];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * <p><b>Performance:</b> This method delegates to {@code Arrays.println()} and {@code Arrays.toString()}
     * for the actual formatting and printing operations.</p>
     *
     * @param a the one-dimensional object array to print (can be {@code null}).
     * @return the string representation of the array that was printed to console.
     * @see #println(Object[][]) for two-dimensional array printing
     * @see #println(Object[][][]) for three-dimensional array printing
     * @see N#println(Object) for the underlying print implementation
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
     * Prints a string representation of a two-dimensional object array to the console and returns the string.
     *
     * <p>This method formats a two-dimensional object array as a readable string representation with proper
     * nested bracket notation. Each sub-array is displayed on its own line for better readability.
     * The method handles null arrays, empty arrays, null sub-arrays, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[element1, element2], [element3, element4]]" (with line separators)</li>
     *   <li>Null sub-array: "null" within the outer brackets</li>
     *   <li>Empty sub-array: "[]" within the outer brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Object[][] array = {{"Hello", "World"}, {123, 456}, null, {}, {"End"}};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[Hello, World],
     * //  [123, 456],
     * //  null,
     * //  [],
     * //  [End]]
     *
     * // Null array
     * String nullResult = Arrays.println((Object[][]) null);
     * // Prints "null"
     *
     * // Empty array
     * Object[][] empty = new Object[0][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the two-dimensional object array to print (can be {@code null}).
     * @return the string representation of the two-dimensional array that was printed to console.
     * @see #println(Object[]) for one-dimensional array printing
     * @see #println(Object[][][]) for three-dimensional array printing
     */
    public static String println(final Object[][] a) {
        return ff.println(a);
    }

    /**
     * Prints a string representation of a three-dimensional object array to the console and returns the string.
     *
     * <p>This method formats a three-dimensional object array as a readable string representation with proper
     * nested bracket notation. Each two-dimensional sub-array and its inner one-dimensional arrays are displayed with
     * appropriate line separators and indentation for better readability. The method handles
     * null arrays, empty arrays, null sub-arrays at any level, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[[element1, element2]], [[element3, element4]]]" (with line separators and indentation)</li>
     *   <li>Null two-dimensional sub-array: "null" within the outer brackets</li>
     *   <li>Empty two-dimensional sub-array: "[]" within the outer brackets</li>
     *   <li>Null one-dimensional sub-array: "null" within the two-dimensional sub-array brackets</li>
     *   <li>Empty one-dimensional sub-array: "[]" within the two-dimensional sub-array brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * Object[][][] array = {{{"A", "B"}, {"C"}}, {{"D"}, null, {}}, null};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[[A, B],
     * //    [C]],
     * //  [[D],
     * //   null,
     * //   []],
     * //  null]
     *
     * // Null array
     * String nullResult = Arrays.println((Object[][][]) null);
     * // Prints "null"
     *
     * // Empty array
     * Object[][][] empty = new Object[0][][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use. The formatting includes proper indentation
     * with two spaces for nested levels to enhance readability of complex three-dimensional structures.</p>
     *
     * @param a the three-dimensional object array to print (can be {@code null}).
     * @return the string representation of the three-dimensional array that was printed to console.
     * @see #println(Object[]) for one-dimensional array printing
     * @see #println(Object[][]) for two-dimensional array printing
     */
    public static String println(final Object[][][] a) {
        return fff.println(a);
    }

    /**
     * Maps each boolean element to an object using the provided mapper function.
     * This method transforms a boolean array into an object array by applying the mapper
     * function to each element.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] flags = {true, false, true};
     * String[] strings = Arrays.mapToObj(flags, b -> b ? "YES" : "NO", String.class);
     * // Result: ["YES", "NO", "YES"]
     * }</pre>
     *
     * <p><b>Type Conversion:</b> The target element type must be specified to ensure proper array creation
     * and type safety throughout the transformation process.</p>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the boolean array to map (can be {@code null}).
     * @param mapper the function that maps each boolean to an object.
     * @param targetElementType the class of the target element type.
     * @return an object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     * @see #mapToObj(boolean[][], Throwables.BooleanFunction, Class) for two-dimensional arrays
     * @see #mapToObj(boolean[][][], Throwables.BooleanFunction, Class) for three-dimensional arrays
     */
    public static <T, E extends Exception> T[] mapToObj(final boolean[] a, final Throwables.BooleanFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(targetElementType, 0);
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Maps each boolean element in a two-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every element across all sub-arrays, maintaining
     * the two-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] matrix = {{true, false}, {false, true}};
     * String[][] result = Arrays.mapToObj(matrix, b -> b ? "YES" : "NO", String.class);
     * // Result: {{"YES", "NO"}, {"NO", "YES"}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the two-dimensional boolean array to map (can be {@code null}).
     * @param mapper the function that maps each boolean to an object.
     * @param targetElementType the class of the target element type.
     * @return a two-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][] mapToObj(final boolean[][] a, final Throwables.BooleanFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(targetElementType, 0).getClass(), 0);
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Maps each boolean element in a three-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every element across all levels of nested sub-arrays,
     * preserving the three-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] cube = {{{true, false}, {true, true}}, {{false, false}, {true, false}}};
     * Integer[][][] result = Arrays.mapToObj(cube, b -> b ? 1 : 0, Integer.class);
     * // Result: {{{1, 0}, {1, 1}}, {{0, 0}, {1, 0}}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the three-dimensional boolean array to map (can be {@code null}).
     * @param mapper the function that maps each boolean to an object.
     * @param targetElementType the class of the target element type.
     * @return a three-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][][] mapToObj(final boolean[][][] a, final Throwables.BooleanFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), 0);
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
     * Each character in the array is transformed according to the provided mapping function.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] chars = {'a', 'b', 'c'};
     * String[] strings = Arrays.mapToObj(chars, c -> String.valueOf(c).toUpperCase(), String.class);
     * // Result: ["A", "B", "C"]
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the char array to map (can be {@code null}).
     * @param mapper the function that maps each char to an object.
     * @param targetElementType the class of the target element type.
     * @return an object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[] mapToObj(final char[] a, final Throwables.CharFunction<? extends T, E> mapper, final Class<T> targetElementType)
            throws E {
        if (N.isEmpty(a)) {
            return N.newArray(targetElementType, 0);
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Maps each char element in a two-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every character across all sub-arrays, maintaining
     * the two-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] matrix = {{'a', 'b'}, {'c', 'd'}};
     * String[][] result = Arrays.mapToObj(matrix, c -> String.valueOf(c).toUpperCase(), String.class);
     * // Result: {{"A", "B"}, {"C", "D"}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the two-dimensional char array to map (can be {@code null}).
     * @param mapper the function that maps each char to an object.
     * @param targetElementType the class of the target element type.
     * @return a two-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][] mapToObj(final char[][] a, final Throwables.CharFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(targetElementType, 0).getClass(), 0);
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Maps each char element in a three-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every character across all levels of nested sub-arrays,
     * preserving the three-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] cube = {{{'a', 'b'}, {'c', 'd'}}, {{'e', 'f'}, {'g', 'h'}}};
     * Integer[][][] result = Arrays.mapToObj(cube, c -> (int) c, Integer.class);
     * // Result: {{{97, 98}, {99, 100}}, {{101, 102}, {103, 104}}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the three-dimensional char array to map (can be {@code null}).
     * @param mapper the function that maps each char to an object.
     * @param targetElementType the class of the target element type.
     * @return a three-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][][] mapToObj(final char[][][] a, final Throwables.CharFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), 0);
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
     * Each byte value in the array is transformed according to the provided mapping function.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] bytes = {1, 2, 3, 4};
     * String[] hex = Arrays.mapToObj(bytes, b -> String.format("%02X", b), String.class);
     * // Result: ["01", "02", "03", "04"]
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the byte array to map (can be {@code null}).
     * @param mapper the function that maps each byte to an object.
     * @param targetElementType the class of the target element type.
     * @return an object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[] mapToObj(final byte[] a, final Throwables.ByteFunction<? extends T, E> mapper, final Class<T> targetElementType)
            throws E {
        if (N.isEmpty(a)) {
            return N.newArray(targetElementType, 0);
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Maps each byte element in a two-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every byte across all sub-arrays, maintaining
     * the two-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] matrix = {{1, 2}, {3, 4}};
     * String[][] result = Arrays.mapToObj(matrix, b -> "0x" + Integer.toHexString(b), String.class);
     * // Result: {{"0x1", "0x2"}, {"0x3", "0x4"}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the two-dimensional byte array to map (can be {@code null}).
     * @param mapper the function that maps each byte to an object.
     * @param targetElementType the class of the target element type.
     * @return a two-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][] mapToObj(final byte[][] a, final Throwables.ByteFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(targetElementType, 0).getClass(), 0);
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Maps each byte element in a three-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every byte across all levels of nested sub-arrays,
     * preserving the three-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] cube = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * Integer[][][] result = Arrays.mapToObj(cube, b -> (int) b * 10, Integer.class);
     * // Result: {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the three-dimensional byte array to map (can be {@code null}).
     * @param mapper the function that maps each byte to an object.
     * @param targetElementType the class of the target element type.
     * @return a three-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][][] mapToObj(final byte[][][] a, final Throwables.ByteFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), 0);
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
     * Each short value in the array is transformed according to the provided mapping function.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] values = {100, 200, 300};
     * String[] result = Arrays.mapToObj(values, s -> "Value: " + s, String.class);
     * // Result: ["Value: 100", "Value: 200", "Value: 300"]
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the short array to map (can be {@code null}).
     * @param mapper the function that maps each short to an object.
     * @param targetElementType the class of the target element type.
     * @return an object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[] mapToObj(final short[] a, final Throwables.ShortFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(targetElementType, 0);
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Maps each short element in a two-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every short value across all sub-arrays, maintaining
     * the two-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] matrix = {{10, 20}, {30, 40}};
     * Integer[][] result = Arrays.mapToObj(matrix, s -> (int) s * 2, Integer.class);
     * // Result: {{20, 40}, {60, 80}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the two-dimensional short array to map (can be {@code null}).
     * @param mapper the function that maps each short to an object.
     * @param targetElementType the class of the target element type.
     * @return a two-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][] mapToObj(final short[][] a, final Throwables.ShortFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(targetElementType, 0).getClass(), 0);
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Maps each short element in a three-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every short value across all levels of nested sub-arrays,
     * preserving the three-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] cube = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * String[][][] result = Arrays.mapToObj(cube, s -> "#" + s, String.class);
     * // Result: {{{"#1", "#2"}, {"#3", "#4"}}, {{"#5", "#6"}, {"#7", "#8"}}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the three-dimensional short array to map (can be {@code null}).
     * @param mapper the function that maps each short to an object.
     * @param targetElementType the class of the target element type.
     * @return a three-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][][] mapToObj(final short[][][] a, final Throwables.ShortFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), 0);
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
     * This method transforms an int array into an object array by applying the mapper
     * function to each element.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] numbers = {1, 2, 3};
     * String[] strings = Arrays.mapToObj(numbers, i -> "Number: " + i, String.class);
     * // Result: ["Number: 1", "Number: 2", "Number: 3"]
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the int array to map (can be {@code null}).
     * @param mapper the function that maps each int to an object.
     * @param targetElementType the class of the target element type.
     * @return an object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[] mapToObj(final int[] a, final Throwables.IntFunction<? extends T, E> mapper, final Class<T> targetElementType)
            throws E {
        if (N.isEmpty(a)) {
            return N.newArray(targetElementType, 0);
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Maps each int element in a two-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every integer across all sub-arrays, maintaining
     * the two-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] matrix = {{1, 2, 3}, {4, 5, 6}};
     * String[][] result = Arrays.mapToObj(matrix, i -> "Item " + i, String.class);
     * // Result: {{"Item 1", "Item 2", "Item 3"}, {"Item 4", "Item 5", "Item 6"}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the two-dimensional int array to map (can be {@code null}).
     * @param mapper the function that maps each int to an object.
     * @param targetElementType the class of the target element type.
     * @return a two-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][] mapToObj(final int[][] a, final Throwables.IntFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(targetElementType, 0).getClass(), 0);
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Maps each int element in a three-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every integer across all levels of nested sub-arrays,
     * preserving the three-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] cube = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * Double[][][] result = Arrays.mapToObj(cube, i -> i * 1.5, Double.class);
     * // Result: {{{1.5, 3.0}, {4.5, 6.0}}, {{7.5, 9.0}, {10.5, 12.0}}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the three-dimensional int array to map (can be {@code null}).
     * @param mapper the function that maps each int to an object.
     * @param targetElementType the class of the target element type.
     * @return a three-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][][] mapToObj(final int[][][] a, final Throwables.IntFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), 0);
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
     * Each long value in the array is transformed according to the provided mapping function.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] values = {1000000L, 2000000L, 3000000L};
     * String[] result = Arrays.mapToObj(values, l -> l / 1000000 + "M", String.class);
     * // Result: ["1M", "2M", "3M"]
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the long array to map (can be {@code null}).
     * @param mapper the function that maps each long to an object.
     * @param targetElementType the class of the target element type.
     * @return an object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[] mapToObj(final long[] a, final Throwables.LongFunction<? extends T, E> mapper, final Class<T> targetElementType)
            throws E {
        if (N.isEmpty(a)) {
            return N.newArray(targetElementType, 0);
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Maps each long element in a two-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every long value across all sub-arrays, maintaining
     * the two-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] matrix = {{100L, 200L}, {300L, 400L}};
     * String[][] result = Arrays.mapToObj(matrix, l -> "ID-" + l, String.class);
     * // Result: {{"ID-100", "ID-200"}, {"ID-300", "ID-400"}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the two-dimensional long array to map (can be {@code null}).
     * @param mapper the function that maps each long to an object.
     * @param targetElementType the class of the target element type.
     * @return a two-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][] mapToObj(final long[][] a, final Throwables.LongFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(targetElementType, 0).getClass(), 0);
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Maps each long element in a three-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every long value across all levels of nested sub-arrays,
     * preserving the three-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] cube = {{{1L, 2L}, {3L, 4L}}, {{5L, 6L}, {7L, 8L}}};
     * java.math.BigInteger[][][] result = Arrays.mapToObj(cube, l -> java.math.BigInteger.valueOf(l), java.math.BigInteger.class);
     * // Converts each long to BigInteger while maintaining three-dimensional structure
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the three-dimensional long array to map (can be {@code null}).
     * @param mapper the function that maps each long to an object.
     * @param targetElementType the class of the target element type.
     * @return a three-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][][] mapToObj(final long[][][] a, final Throwables.LongFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), 0);
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
     * Each float value in the array is transformed according to the provided mapping function.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] temperatures = {98.6f, 99.5f, 97.3f};
     * String[] result = Arrays.mapToObj(temperatures, t -> t + "°F", String.class);
     * // Result: ["98.6°F", "99.5°F", "97.3°F"]
     * }</pre>
     *
     * <p><b>Type Conversion:</b> The target element type must be specified to ensure proper array creation
     * and type safety throughout the transformation process.</p>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the float array to map (can be {@code null}).
     * @param mapper the function that maps each float to an object.
     * @param targetElementType the class of the target element type.
     * @return an object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     * @see #mapToObj(float[][], Throwables.FloatFunction, Class) for two-dimensional arrays
     * @see #mapToObj(float[][][], Throwables.FloatFunction, Class) for three-dimensional arrays
     */
    public static <T, E extends Exception> T[] mapToObj(final float[] a, final Throwables.FloatFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(targetElementType, 0);
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Maps each float element in a two-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every float value across all sub-arrays, maintaining
     * the two-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] matrix = {{1.1f, 2.2f}, {3.3f, 4.4f}};
     * String[][] result = Arrays.mapToObj(matrix, f -> String.format("%.1f", f), String.class);
     * // Result: {{"1.1", "2.2"}, {"3.3", "4.4"}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the two-dimensional float array to map (can be {@code null}).
     * @param mapper the function that maps each float to an object.
     * @param targetElementType the class of the target element type.
     * @return a two-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][] mapToObj(final float[][] a, final Throwables.FloatFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(targetElementType, 0).getClass(), 0);
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Maps each float element in a three-dimensional array to an object using the provided mapper function.
     * This method applies the mapper function to every float value across all levels of nested sub-arrays,
     * preserving the three-dimensional structure of the original array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] cube = {{{1.5f, 2.5f}, {3.5f, 4.5f}}, {{5.5f, 6.5f}, {7.5f, 8.5f}}};
     * Double[][][] result = Arrays.mapToObj(cube, f -> (double) f * 2, Double.class);
     * // Result: {{{3.0, 5.0}, {7.0, 9.0}}, {{11.0, 13.0}, {15.0, 17.0}}}
     * }</pre>
     *
     * @param <T> the type of elements in the result array.
     * @param <E> the type of exception that the mapper may throw.
     * @param a the three-dimensional float array to map (can be {@code null}).
     * @param mapper the function that maps each float to an object.
     * @param targetElementType the class of the target element type.
     * @return a three-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <T, E extends Exception> T[][][] mapToObj(final float[][][] a, final Throwables.FloatFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), 0);
        }

        final int len = N.len(a);
        final T[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Maps each element of a double array to an object using the provided mapper function.
     * Each element in the input array is transformed using the mapper function.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] doubles = {1.5, 2.5, 3.5};
     * String[] strings = Arrays.mapToObj(doubles, d -> String.format("%.1f", d), String.class);
     * // Result: ["1.5", "2.5", "3.5"]
     * }</pre>
     *
     * <p><b>Type Conversion:</b> The target element type must be specified to ensure proper array creation
     * and type safety throughout the transformation process.</p>
     *
     * @param <T> the type of elements in the resulting array.
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the double array to map (can be {@code null}).
     * @param mapper the function to transform each double element.
     * @param targetElementType the class of the target element type.
     * @return an object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     * @see #mapToObj(double[][], Throwables.DoubleFunction, Class) for two-dimensional arrays
     * @see #mapToObj(double[][][], Throwables.DoubleFunction, Class) for three-dimensional arrays
     */
    public static <T, E extends Exception> T[] mapToObj(final double[] a, final Throwables.DoubleFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(targetElementType, 0);
        }

        final int len = N.len(a);
        final T[] result = N.newArray(targetElementType, len);

        for (int i = 0; i < len; i++) {
            result[i] = mapper.apply(a[i]);
        }

        return result;
    }

    /**
     * Maps each element of a two-dimensional double array to an object using the provided mapper function.
     * Each element in the input array is transformed using the mapper function.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] doubles = {{1.5, 2.5}, {3.5, 4.5}};
     * String[][] strings = Arrays.mapToObj(doubles, d -> String.format("%.1f", d), String.class);
     * // Result: {{"1.5", "2.5"}, {"3.5", "4.5"}}
     * }</pre>
     *
     * @param <T> the type of elements in the resulting array.
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the two-dimensional double array to map (can be {@code null}).
     * @param mapper the function to transform each double element.
     * @param targetElementType the class of the target element type.
     * @return a two-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     * @see #mapToObj(double[], Throwables.DoubleFunction, Class) for one-dimensional arrays
     * @see #mapToObj(double[][][], Throwables.DoubleFunction, Class) for three-dimensional arrays
     */
    public static <T, E extends Exception> T[][] mapToObj(final double[][] a, final Throwables.DoubleFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(targetElementType, 0).getClass(), 0);
        }

        final int len = N.len(a);
        final T[][] result = N.newArray(N.newArray(targetElementType, 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Maps each element of a three-dimensional double array to an object using the provided mapper function.
     * Each element in the input array is transformed using the mapper function.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] doubles = {{{1.1, 2.2}}, {{3.3, 4.4}}};
     * Integer[][][] integers = Arrays.mapToObj(doubles, d -> (int) d, Integer.class);
     * // Result: {{{1, 2}}, {{3, 4}}}
     * }</pre>
     *
     * @param <T> the type of elements in the resulting array.
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the three-dimensional double array to map (can be {@code null}).
     * @param mapper the function to transform each double element.
     * @param targetElementType the class of the target element type.
     * @return a three-dimensional object array containing the mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     * @see #mapToObj(double[], Throwables.DoubleFunction, Class) for one-dimensional arrays
     * @see #mapToObj(double[][], Throwables.DoubleFunction, Class) for two-dimensional arrays
     */
    public static <T, E extends Exception> T[][][] mapToObj(final double[][][] a, final Throwables.DoubleFunction<? extends T, E> mapper,
            final Class<T> targetElementType) throws E {
        if (N.isEmpty(a)) {
            return N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), 0);
        }

        final int len = N.len(a);
        final T[][][] result = N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), len);

        for (int i = 0; i < len; i++) {
            result[i] = mapToObj(a[i], mapper, targetElementType);
        }

        return result;
    }

    /**
     * Maps a one-dimensional int array to a one-dimensional long array using the provided mapper function.
     * Each int element is transformed to a long value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] ints = {1, 2, 3};
     * long[] longs = Arrays.mapToLong(ints, i -> i * 1000000000L);
     * // Result: {1000000000L, 2000000000L, 3000000000L}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the int array to map (can be {@code null}).
     * @param mapper the function to transform each int to long.
     * @return a long array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     * @see #mapToLong(int[][], Throwables.IntToLongFunction) for two-dimensional arrays
     * @see #mapToLong(int[][][], Throwables.IntToLongFunction) for three-dimensional arrays
     */
    public static <E extends Exception> long[] mapToLong(final int[] a, final Throwables.IntToLongFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return N.EMPTY_LONG_ARRAY;
        }

        final int len = N.len(a);
        final long[] result = new long[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsLong(a[i]);
        }

        return result;
    }

    /**
     * Maps a two-dimensional int array to a two-dimensional long array using the provided mapper function.
     * Each int element is transformed to a long value, preserving the two-dimensional structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] matrix = {{1, 2}, {3, 4}};
     * long[][] result = Arrays.mapToLong(matrix, i -> i * 1000000000L);
     * // Result: {{1000000000L, 2000000000L}, {3000000000L, 4000000000L}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the two-dimensional int array to map (can be {@code null}).
     * @param mapper the function to transform each int to long.
     * @return a two-dimensional long array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     * @see #mapToLong(int[], Throwables.IntToLongFunction) for one-dimensional arrays
     * @see #mapToLong(int[][][], Throwables.IntToLongFunction) for three-dimensional arrays
     */
    public static <E extends Exception> long[][] mapToLong(final int[][] a, final Throwables.IntToLongFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new long[0][];
        }

        final int len = N.len(a);
        final long[][] result = new long[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToLong(a[i], mapper);
        }

        return result;
    }

    /**
     * Maps a three-dimensional int array to a three-dimensional long array using the provided mapper function.
     * Each int element is transformed to a long value, preserving the three-dimensional structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] cube = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * long[][][] result = Arrays.mapToLong(cube, i -> (long) i * i);
     * // Result: {{{1L, 4L}, {9L, 16L}}, {{25L, 36L}, {49L, 64L}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the three-dimensional int array to map (can be {@code null}).
     * @param mapper the function to transform each int to long.
     * @return a three-dimensional long array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     * @see #mapToLong(int[], Throwables.IntToLongFunction) for one-dimensional arrays
     * @see #mapToLong(int[][], Throwables.IntToLongFunction) for two-dimensional arrays
     */
    public static <E extends Exception> long[][][] mapToLong(final int[][][] a, final Throwables.IntToLongFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new long[0][][];
        }

        final int len = N.len(a);
        final long[][][] result = new long[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToLong(a[i], mapper);
        }

        return result;
    }

    /**
     * Maps a one-dimensional int array to a one-dimensional double array using the provided mapper function.
     * Each int element is transformed to a double value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] ints = {1, 2, 3};
     * double[] doubles = Arrays.mapToDouble(ints, i -> i / 2.0);
     * // Result: {0.5, 1.0, 1.5}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input int array (can be {@code null}).
     * @param mapper the function to transform each int to double.
     * @return a double array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> double[] mapToDouble(final int[] a, final Throwables.IntToDoubleFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return N.EMPTY_DOUBLE_ARRAY;
        }

        final int len = N.len(a);
        final double[] result = new double[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsDouble(a[i]);
        }

        return result;
    }

    /**
     * Maps a two-dimensional int array to a two-dimensional double array using the provided mapper function.
     * Each int element is transformed to a double value, preserving the two-dimensional structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] matrix = {{10, 20}, {30, 40}};
     * double[][] result = Arrays.mapToDouble(matrix, i -> i / 3.0);
     * // Result: {{3.33..., 6.66...}, {10.0, 13.33...}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input two-dimensional int array (can be {@code null}).
     * @param mapper the function to transform each int to double.
     * @return a two-dimensional double array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> double[][] mapToDouble(final int[][] a, final Throwables.IntToDoubleFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new double[0][];
        }

        final int len = N.len(a);
        final double[][] result = new double[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToDouble(a[i], mapper);
        }

        return result;
    }

    /**
     * Maps a three-dimensional int array to a three-dimensional double array using the provided mapper function.
     * Each int element is transformed to a double value, preserving the three-dimensional structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] cube = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * double[][][] result = Arrays.mapToDouble(cube, i -> Math.sqrt(i));
     * // Result: {{{1.0, 1.41...}, {1.73..., 2.0}}, {{2.23..., 2.44...}, {2.64..., 2.82...}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input three-dimensional int array (can be {@code null}).
     * @param mapper the function to transform each int to double.
     * @return a three-dimensional double array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> double[][][] mapToDouble(final int[][][] a, final Throwables.IntToDoubleFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new double[0][][];
        }

        final int len = N.len(a);
        final double[][][] result = new double[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToDouble(a[i], mapper);
        }

        return result;
    }

    /**
     * Maps a one-dimensional long array to a one-dimensional int array using the provided mapper function.
     * Each long element is transformed to an int value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] longs = {1000L, 2000L, 3000L};
     * int[] ints = Arrays.mapToInt(longs, l -> (int)(l / 1000));
     * // Result: {1, 2, 3}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input long array (can be {@code null}).
     * @param mapper the function to transform each long to int.
     * @return an int array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> int[] mapToInt(final long[] a, final Throwables.LongToIntFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return N.EMPTY_INT_ARRAY;
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsInt(a[i]);
        }

        return result;
    }

    /**
     * Maps a two-dimensional long array to a two-dimensional int array using the provided mapper function.
     * Each long element is transformed to an int value. This operation is useful for
     * downcasting or converting long values to integers with custom logic.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] matrix = {{100L, 200L}, {300L, 400L}};
     * int[][] result = Arrays.mapToInt(matrix, l -> (int) (l / 10));
     * // Result: {{10, 20}, {30, 40}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input two-dimensional long array (can be {@code null}).
     * @param mapper the function to transform each long to int.
     * @return a two-dimensional int array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> int[][] mapToInt(final long[][] a, final Throwables.LongToIntFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new int[0][];
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToInt(a[i], mapper);
        }

        return result;
    }

    /**
     * Maps a three-dimensional long array to a three-dimensional int array using the provided mapper function.
     * Each long element is transformed to an int value, preserving the three-dimensional structure.
     * This operation is useful for downcasting or converting long values to integers with custom logic.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] cube = {{{1000L, 2000L}, {3000L, 4000L}}, {{5000L, 6000L}, {7000L, 8000L}}};
     * int[][][] result = Arrays.mapToInt(cube, l -> (int) (l / 1000));
     * // Result: {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input three-dimensional long array (can be {@code null}).
     * @param mapper the function to transform each long to int.
     * @return a three-dimensional int array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> int[][][] mapToInt(final long[][][] a, final Throwables.LongToIntFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new int[0][][];
        }

        final int len = N.len(a);
        final int[][][] result = new int[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToInt(a[i], mapper);
        }

        return result;
    }

    /**
     * Maps a one-dimensional long array to a one-dimensional double array using the provided mapper function.
     * Each long element is transformed to a double value. This operation is useful for
     * converting long values to doubles with custom transformation logic.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] values = {100L, 200L, 300L};
     * double[] result = Arrays.mapToDouble(values, l -> l / 100.0);
     * // Result: {1.0, 2.0, 3.0}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input long array (can be {@code null}).
     * @param mapper the function to transform each long to double.
     * @return a double array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> double[] mapToDouble(final long[] a, final Throwables.LongToDoubleFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return N.EMPTY_DOUBLE_ARRAY;
        }

        final int len = N.len(a);
        final double[] result = new double[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsDouble(a[i]);
        }

        return result;
    }

    /**
     * Maps a two-dimensional long array to a two-dimensional double array using the provided mapper function.
     * Each long element is transformed to a double value. This operation preserves the two-dimensional structure
     * while converting long values to doubles with custom transformation logic.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] matrix = {{1000L, 2000L}, {3000L, 4000L}};
     * double[][] result = Arrays.mapToDouble(matrix, l -> l / 1000.0);
     * // Result: {{1.0, 2.0}, {3.0, 4.0}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input two-dimensional long array (can be {@code null}).
     * @param mapper the function to transform each long to double.
     * @return a two-dimensional double array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> double[][] mapToDouble(final long[][] a, final Throwables.LongToDoubleFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new double[0][];
        }

        final int len = N.len(a);
        final double[][] result = new double[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToDouble(a[i], mapper);
        }

        return result;
    }

    /**
     * Maps a three-dimensional long array to a three-dimensional double array using the provided mapper function.
     * Each long element is transformed to a double value, preserving the three-dimensional structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] cube = {{{100L, 200L}, {300L, 400L}}, {{500L, 600L}, {700L, 800L}}};
     * double[][][] result = Arrays.mapToDouble(cube, l -> l / 100.0);
     * // Result: {{{1.0, 2.0}, {3.0, 4.0}}, {{5.0, 6.0}, {7.0, 8.0}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input three-dimensional long array (can be {@code null}).
     * @param mapper the function to transform each long to double.
     * @return a three-dimensional double array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> double[][][] mapToDouble(final long[][][] a, final Throwables.LongToDoubleFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new double[0][][];
        }

        final int len = N.len(a);
        final double[][][] result = new double[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToDouble(a[i], mapper);
        }

        return result;
    }

    /**
     * Maps a one-dimensional double array to a one-dimensional int array using the provided mapper function.
     * Each double element is transformed to an int value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] doubles = {1.7, 2.3, 3.9};
     * int[] ints = Arrays.mapToInt(doubles, d -> (int) Math.round(d));
     * // Result: {2, 2, 4}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input double array (can be {@code null}).
     * @param mapper the function to transform each double to int.
     * @return an int array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> int[] mapToInt(final double[] a, final Throwables.DoubleToIntFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return N.EMPTY_INT_ARRAY;
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsInt(a[i]);
        }

        return result;
    }

    /**
     * Maps a two-dimensional double array to a two-dimensional int array using the provided mapper function.
     * Each double element is transformed to an int value. This operation preserves the two-dimensional structure
     * while converting double values to integers with custom transformation logic.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] matrix = {{1.5, 2.7}, {3.2, 4.9}};
     * int[][] result = Arrays.mapToInt(matrix, d -> (int) Math.round(d));
     * // Result: {{2, 3}, {3, 5}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input two-dimensional double array (can be {@code null}).
     * @param mapper the function to transform each double to int.
     * @return a two-dimensional int array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> int[][] mapToInt(final double[][] a, final Throwables.DoubleToIntFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new int[0][];
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToInt(a[i], mapper);
        }

        return result;
    }

    /**
     * Maps a three-dimensional double array to a three-dimensional int array using the provided mapper function.
     * Each double element is transformed to an int value, preserving the three-dimensional structure.
     * This operation is useful for converting double values to integers with custom rounding or scaling logic.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] cube = {{{1.5, 2.7}, {3.2, 4.9}}, {{5.1, 6.8}, {7.3, 8.6}}};
     * int[][][] result = Arrays.mapToInt(cube, d -> (int) Math.ceil(d));
     * // Result: {{{2, 3}, {4, 5}}, {{6, 7}, {8, 9}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input three-dimensional double array (can be {@code null}).
     * @param mapper the function to transform each double to int.
     * @return a three-dimensional int array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> int[][][] mapToInt(final double[][][] a, final Throwables.DoubleToIntFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new int[0][][];
        }

        final int len = N.len(a);
        final int[][][] result = new int[len][][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToInt(a[i], mapper);
        }

        return result;
    }

    /**
     * Maps a one-dimensional double array to a one-dimensional long array using the provided mapper function.
     * Each double element is transformed to a long value. This operation is useful for
     * converting double values to long integers with custom rounding or scaling logic.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] prices = {19.99, 29.99, 39.99};
     * long[] cents = Arrays.mapToLong(prices, d -> (long) (d * 100));
     * // Result: {1999, 2999, 3999}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input double array (can be {@code null}).
     * @param mapper the function to transform each double to long.
     * @return a long array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> long[] mapToLong(final double[] a, final Throwables.DoubleToLongFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return N.EMPTY_LONG_ARRAY;
        }

        final int len = N.len(a);
        final long[] result = new long[len];

        for (int i = 0; i < len; i++) {
            result[i] = mapper.applyAsLong(a[i]);
        }

        return result;
    }

    /**
     * Maps a two-dimensional double array to a two-dimensional long array using the provided mapper function.
     * Each double element is transformed to a long value. This operation preserves the two-dimensional structure
     * while converting double values to long integers with custom transformation logic.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] prices = {{19.99, 29.99}, {39.99, 49.99}};
     * long[][] cents = Arrays.mapToLong(prices, d -> (long) (d * 100));
     * // Result: {{1999, 2999}, {3999, 4999}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input two-dimensional double array (can be {@code null}).
     * @param mapper the function to transform each double to long.
     * @return a two-dimensional long array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> long[][] mapToLong(final double[][] a, final Throwables.DoubleToLongFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new long[0][];
        }

        final int len = N.len(a);
        final long[][] result = new long[len][];

        for (int i = 0; i < len; i++) {
            result[i] = mapToLong(a[i], mapper);
        }

        return result;
    }

    /**
     * Maps a three-dimensional double array to a three-dimensional long array using the provided mapper function.
     * Each double element is transformed to a long value, preserving the three-dimensional structure.
     * This operation is useful for converting double values to long integers with custom scaling or rounding logic.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] cube = {{{1.5, 2.7}, {3.2, 4.9}}, {{5.1, 6.8}, {7.3, 8.6}}};
     * long[][][] result = Arrays.mapToLong(cube, d -> (long) (d * 1000));
     * // Result: {{{1500, 2700}, {3200, 4900}}, {{5100, 6800}, {7300, 8600}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the mapper.
     * @param a the input three-dimensional double array (can be {@code null}).
     * @param mapper the function to transform each double to long.
     * @return a three-dimensional long array with mapped values, or an empty array if input is {@code null}.
     * @throws E if the {@code mapper} function throws an exception.
     */
    public static <E extends Exception> long[][][] mapToLong(final double[][][] a, final Throwables.DoubleToLongFunction<E> mapper) throws E {
        if (N.isEmpty(a)) {
            return new long[0][][];
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
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] arr = {true, false, true};
     * Arrays.updateAll(arr, b -> !b);
     * // arr is now: [false, true, false]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the boolean array to update (can be {@code null}).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
     * @see #updateAll(boolean[][], Throwables.BooleanUnaryOperator) for two-dimensional arrays
     * @see #updateAll(boolean[][][], Throwables.BooleanUnaryOperator) for three-dimensional arrays
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
     * Updates all elements in a two-dimensional boolean array using the provided unary operator.
     * Each element in every sub-array is replaced with the result of applying the operator.
     * This method modifies the array in-place, preserving the two-dimensional structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] matrix = {{true, false}, {false, true}};
     * Arrays.updateAll(matrix, b -> !b);
     * // matrix is now: {{false, true}, {true, false}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the two-dimensional boolean array to update (can be {@code null} or empty).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
     * @see #updateAll(boolean[], Throwables.BooleanUnaryOperator) for one-dimensional arrays
     * @see #updateAll(boolean[][][], Throwables.BooleanUnaryOperator) for three-dimensional arrays
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
     * Updates all elements in a three-dimensional boolean array using the provided unary operator.
     * Each element in every nested sub-array is replaced with the result of applying the operator.
     * This method modifies the array in-place, preserving the three-dimensional structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] cube = {{{true, false}, {false, true}}, {{true, true}, {false, false}}};
     * Arrays.updateAll(cube, b -> !b);
     * // cube is now: {{{false, true}, {true, false}}, {{false, false}, {true, true}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the three-dimensional boolean array to update (can be {@code null} or empty).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
     * @see #updateAll(boolean[], Throwables.BooleanUnaryOperator) for one-dimensional arrays
     * @see #updateAll(boolean[][], Throwables.BooleanUnaryOperator) for two-dimensional arrays
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
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] arr = {true, false, true, false};
     * Arrays.replaceIf(arr, b -> b, false);
     * // arr is now: [false, false, false, false]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the boolean array to modify (can be {@code null}).
     * @param predicate the condition to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
     * @see #replaceIf(boolean[][], Throwables.BooleanPredicate, boolean) for two-dimensional arrays
     * @see #replaceIf(boolean[][][], Throwables.BooleanPredicate, boolean) for three-dimensional arrays
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
     * Replaces all elements in a two-dimensional boolean array that match the predicate with a new value.
     * Elements that don't match the predicate remain unchanged. This method modifies the array
     * in-place, traversing all sub-arrays.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] matrix = {{true, false}, {true, false}};
     * Arrays.replaceIf(matrix, b -> b, false);
     * // matrix is now: {{false, false}, {false, false}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the two-dimensional boolean array to modify (can be {@code null} or empty).
     * @param predicate the condition to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
     * @see #replaceIf(boolean[], Throwables.BooleanPredicate, boolean) for one-dimensional arrays
     * @see #replaceIf(boolean[][][], Throwables.BooleanPredicate, boolean) for three-dimensional arrays
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
     * Replaces all elements in a three-dimensional boolean array that match the predicate with a new value.
     * Elements that don't match the predicate remain unchanged. This method modifies the array
     * in-place, traversing all nested sub-arrays.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] cube = {{{true, false}, {true, false}}, {{false, true}, {false, true}}};
     * Arrays.replaceIf(cube, b -> !b, true);
     * // cube is now: {{{true, true}, {true, true}}, {{true, true}, {true, true}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the three-dimensional boolean array to modify (can be {@code null} or empty).
     * @param predicate the condition to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
     * @see #replaceIf(boolean[], Throwables.BooleanPredicate, boolean) for one-dimensional arrays
     * @see #replaceIf(boolean[][], Throwables.BooleanPredicate, boolean) for two-dimensional arrays
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] arr = {true, false, true, false, true};
     * boolean[][] reshaped = Arrays.reshape(arr, 2);
     * // Result: [[true, false], [true, false], [true]]
     * }</pre>
     *
     * @param a the one-dimensional boolean array to reshape.
     * @param cols the number of columns for the reshaped array.
     * @return a two-dimensional boolean array with the specified number of columns.
     * @throws IllegalArgumentException if the number of columns is less than or equal to zero.
     */
    public static boolean[][] reshape(final boolean[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new boolean[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] arr = {true, false, true, false, true, false};
     * boolean[][][] reshaped = Arrays.reshape(arr, 2, 2);
     * // Result: {{{true, false}, {true, false}}, {{true, false}}}
     * }</pre>
     *
     * @param a the one-dimensional boolean array to reshape.
     * @param rows the number of rows for the reshaped subarray.
     * @param cols the number of columns for the reshaped subarray.
     * @return a three-dimensional boolean array with the specified number of rows and columns.
     * @throws IllegalArgumentException if the number of rows or columns is less than or equal to zero.
     */
    public static boolean[][][] reshape(final boolean[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new boolean[0][][];
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
     * Flattens a two-dimensional boolean array into a one-dimensional boolean array.
     * All elements from all sub-arrays are combined into a single array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] arr = {{true, false}, {true}, {false, true}};
     * boolean[] flattened = Arrays.flatten(arr);
     * // Result: {true, false, true, false, true}
     * }</pre>
     *
     * @param a the two-dimensional boolean array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
     * @see #flatten(boolean[][][]) for flattening three-dimensional arrays
     * @see #flatOp(boolean[][], Throwables.Consumer) for performing operations on flattened arrays
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
     * Flattens a three-dimensional boolean array into a one-dimensional boolean array.
     * All elements from all nested sub-arrays are combined into a single array in row-major order.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] cube = {{{true, false}, {true}}, {{false, true}}};
     * boolean[] flattened = Arrays.flatten(cube);
     * // Result: {true, false, true, false, true}
     * }</pre>
     *
     * @param a the three-dimensional boolean array to flatten (can be {@code null}).
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
     * @see #flatten(boolean[][]) for flattening two-dimensional arrays
     * @see #flatOp(boolean[][][], Throwables.Consumer) for performing operations on flattened three-dimensional arrays
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
     * Flattens a two-dimensional boolean array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] arr = {{true, false}, {false, true}};
     * flatOp(arr, t -> Arrays.sort(t)); // Sorts all elements
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operation.
     * @param a the two-dimensional boolean array to operate on.
     * @param op the operation to apply to the flattened array.
     * @throws E if the operation throws an exception.
     * @see #flatten(boolean[][]) for just flattening without applying operations
     * @see #flatOp(boolean[][][], Throwables.Consumer) for three-dimensional arrays
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
     * Flattens a three-dimensional boolean array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure,
     * such as sorting or bulk modifications. The original three-dimensional structure is preserved.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] cube = {{{false, true}, {true, false}}, {{true, false}, {false, true}}};
     * Arrays.flatOp(cube, arr -> java.util.Arrays.sort(arr)); // Sorts all elements
     * // cube's structure is preserved but elements are sorted
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operation.
     * @param a the three-dimensional boolean array to operate on (can be {@code null} or empty).
     * @param op the operation to apply to the flattened array.
     * @throws E if the operation throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] a = {true, false, true, false};
     * boolean[] b = {false, true, false};
     * boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);
     * // result: {false, false, false}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from both arrays.
     * @return a new array containing the results of applying the zip function to corresponding elements.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] a = {true, false, true, false};
     * boolean[] b = {false, true};
     * boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);
     * // result: {true, true, true, false} (using true for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longer input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] a = {true, false, true, false};
     * boolean[] b = {false, true, false};
     * boolean[] c = {true, true};
     * boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);
     * // result: {true, true}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new array containing the results with length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] a = {true, false, true, false};
     * boolean[] b = {false, true};
     * boolean[] c = {true, true, false};
     * boolean[] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x && y && z);
     * // result: {false, false, false, false} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param valueForNoneC the default value to use when array 'c' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional boolean arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays (rows), stopping when the
     * shorter outer array is exhausted. Each pair of corresponding sub-arrays is zipped
     * using the standard one-dimensional array zip logic.
     * 
     * <p>This method is useful for matrix-like operations where you want to combine
     * corresponding rows from two two-dimensional arrays.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] a = {{true, false}, {false, true, false}};
     * boolean[][] b = {{false, true, false}, {true, false}};
     * boolean[][] result = Arrays.zip(a, b, (x, y) -> x ^ y);
     * // result: {{true, true}, {true, true}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new two-dimensional array containing the results of zipping corresponding sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional boolean arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels. This provides complete control
     * over how mismatched array dimensions are handled.
     * 
     * <p>The outer array length of the result equals the maximum of the input outer array lengths.
     * For each position, if one array lacks a sub-array, a null is treated as an empty array and
     * default values are used for all positions.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] a = {{true, false}, {false, true, false}};
     * boolean[][] b = {{false, true, false}, {true, false}, {true}};
     * boolean[][] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);
     * // result: {{true, true, false}, {true, true, true}, {true}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the longer input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional boolean arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays (rows) from all three input arrays,
     * stopping when the shortest outer array is exhausted.
     * 
     * <p>This method extends the two-dimensional zip concept to three arrays, useful for combining
     * data from three different sources row by row.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] a = {{true, false}, {false, true, false}};
     * boolean[][] b = {{false, true, false}, {true, false}};
     * boolean[][] c = {{true, true}, {false, false, true}};
     * boolean[][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);
     * // result: {{true, true}, {true, true}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new two-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional boolean arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels. This is the most flexible two-dimensional zip
     * operation, handling any combination of array dimension mismatches.
     * 
     * <p>The result has an outer array length equal to the longest input outer array. Missing sub-arrays
     * are treated as null/empty, and the provided default values are used for any missing elements.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] a = {{true, false}, {false, true, false}};
     * boolean[][] b = {{false, true, false}, {true, false}, {true}};
     * boolean[][] c = {{true, true}};
     * boolean[][] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x && y && z);
     * // result: {{false, false, false}, {false, false, false}, {false}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional boolean arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, which in turn zip their
     * corresponding one-dimensional sub-arrays. The operation stops when the shorter outer array is exhausted.
     * 
     * <p>This method is useful for combining three-dimensional data structures like cubes or time-series of matrices,
     * where you want to perform element-wise operations on corresponding positions.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] a = {{{true, false}, {false, true}}, {{true, true}, {false, false}}};
     * boolean[][][] b = {{{false, true}, {true, false}}, {{false, false}, {true, true}}};
     * boolean[][][] result = Arrays.zip(a, b, (x, y) -> x ^ y);
     * // result: {{{true, true}, {true, true}}, {{true, true}, {true, true}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new three-dimensional array containing the results of zipping corresponding two-dimensional sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional boolean arrays using the provided zip function, with default values
     * for missing elements at all array levels. This handles dimension mismatches at every level of
     * the three-dimensional structure.
     * 
     * <p>The result has an outer array length equal to the longest input outer array. Missing 2D
     * sub-arrays are handled by treating them as null and using default values throughout.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] a = {{{true, false}, {false, true}}, {{true, true}}};
     * boolean[][][] b = {{{false, true}, {true, false}}, {{false, false}, {true, true}}, {{false}}};
     * boolean[][][] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);
     * // result: {{{true, true}, {true, true}}, {{true, true}, {true, true}}, {{false}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the longer input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional boolean arrays using the provided zip function.
     * Applies the zip operation recursively through all three dimensions, combining corresponding
     * elements from all three arrays at the deepest level.
     * 
     * <p>This method is suitable for combining three three-dimensional data structures where you need to perform
     * operations on corresponding elements across all three sources.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] a = {{{true, false}}};
     * boolean[][][] b = {{{false, true}, {true, false}}};
     * boolean[][][] c = {{{true, true}}};
     * boolean[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);
     * // result: {{{true, true}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new three-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional boolean arrays using the provided zip function, with default values
     * for missing elements at all array levels. This is the most comprehensive three-dimensional zip operation,
     * handling any combination of dimension mismatches across all three arrays.
     * 
     * <p>The result has an outer array length equal to the longest input outer array. Default values
     * are used whenever any array lacks elements at any level of the structure.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] a = {{{true, false}}};
     * boolean[][][] b = {{{false, true}, {true, false}}};
     * boolean[][][] c = {{{true, true}}};
     * boolean[][][] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x && y && z);
     * // result: {{{false, false}, {false, false}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Calculates the total count of elements in a two-dimensional boolean array.
     * Counts all elements across all sub-arrays, handling null sub-arrays gracefully.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] arr = {{true, false}, {true}, {false, true, false}};
     * long count = totalCountOfElements(arr);
     * // Result: 6
     * }</pre>
     *
     * @param a the two-dimensional boolean array.
     * @return the total number of elements across all sub-arrays.
     */
    public static long totalCountOfElements(final boolean[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        long count = 0;

        for (final boolean[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total count of elements in a three-dimensional boolean array.
     * Counts all elements across all sub-arrays at all levels, handling null arrays gracefully.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] arr = {{{true, false}, {true}}, {{false}, {true, false}}};
     * long count = totalCountOfElements(arr);
     * // Result: 6
     * }</pre>
     *
     * @param a the three-dimensional boolean array.
     * @return the total number of elements across all sub-arrays.
     */
    public static long totalCountOfElements(final boolean[][][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

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
     * Finds the minimum length among all sub-arrays in a two-dimensional boolean array.
     * Returns 0 if the input array is null or empty.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] arr = {{true, false, true}, {true}, {false, true}};
     * int minLen = minSubArrayLen(arr);
     * // Result: 1
     * }</pre>
     *
     * @param a the two-dimensional boolean array.
     * @return the minimum length of sub-arrays, or 0 if array is empty.
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
     * Finds the maximum length among all sub-arrays in a two-dimensional boolean array.
     * Returns 0 if the input array is null or empty.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] arr = {{true, false, true}, {true}, {false, true}};
     * int maxLen = maxSubArrayLen(arr);
     * // Result: 3
     * }</pre>
     *
     * @param a the two-dimensional boolean array.
     * @return the maximum length of sub-arrays, or 0 if array is empty.
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
     * Prints a string representation of a one-dimensional boolean array to the console and returns the string.
     *
     * <p>This method formats a one-dimensional boolean array as a readable string representation with standard
     * bracket notation. The method handles null arrays and empty arrays gracefully, providing
     * appropriate string representations for each case.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[element1, element2, element3]"</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] array = {true, false, true};
     * String result = Arrays.println(array);
     * // Prints to console and returns: "[true, false, true]"
     *
     * // Null array
     * String nullResult = Arrays.println((boolean[]) null);
     * // Prints "null"
     *
     * // Empty array
     * boolean[] empty = new boolean[0];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the one-dimensional boolean array to print (can be {@code null}).
     * @return the string representation of the array that was printed to console.
     * @see #println(boolean[][]) for two-dimensional array printing
     * @see #println(boolean[][][]) for three-dimensional array printing
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
     * Prints a string representation of a two-dimensional boolean array to the console and returns the string.
     *
     * <p>This method formats a two-dimensional boolean array as a readable string representation with proper
     * nested bracket notation. Each sub-array is displayed on its own line for better readability.
     * The method handles null arrays, empty arrays, null sub-arrays, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[element1, element2], [element3, element4]]" (with line separators)</li>
     *   <li>Null sub-array: "null" within the outer brackets</li>
     *   <li>Empty sub-array: "[]" within the outer brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] array = {{true, false, true}, {false, true}, null, {}, {true}};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[true, false, true],
     * //  [false, true],
     * //  null,
     * //  [],
     * //  [true]]
     *
     * // Null array
     * String nullResult = Arrays.println((boolean[][]) null);
     * // Prints "null"
     *
     * // Empty array
     * boolean[][] empty = new boolean[0][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the two-dimensional boolean array to print (can be {@code null}).
     * @return the string representation of the two-dimensional array that was printed to console.
     * @see #println(boolean[]) for one-dimensional array printing
     * @see #println(boolean[][][]) for three-dimensional array printing
     */
    public static String println(final boolean[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final boolean[] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(row[j]);
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
     * Prints a string representation of a three-dimensional boolean array to the console and returns the string.
     *
     * <p>This method formats a three-dimensional boolean array as a readable string representation with proper
     * nested bracket notation. Each two-dimensional sub-array and its inner one-dimensional arrays are displayed with
     * appropriate line separators and indentation for better readability. The method handles
     * null arrays, empty arrays, null sub-arrays at any level, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[[element1, element2]], [[element3, element4]]]" (with line separators and indentation)</li>
     *   <li>Null two-dimensional sub-array: "null" within the outer brackets</li>
     *   <li>Empty two-dimensional sub-array: "[]" within the outer brackets</li>
     *   <li>Null one-dimensional sub-array: "null" within the two-dimensional sub-array brackets</li>
     *   <li>Empty one-dimensional sub-array: "[]" within the two-dimensional sub-array brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] array = {{{true, false, true}, {false, true}}, {{true, false}, null, {}}, null};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[[true, false, true],
     * //    [false, true]],
     * //  [[true, false],
     * //   null,
     * //   []],
     * //  null]
     *
     * // Null array
     * String nullResult = Arrays.println((boolean[][][]) null);
     * // Prints "null"
     *
     * // Empty array
     * boolean[][][] empty = new boolean[0][][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use. The formatting includes proper indentation
     * with two spaces for nested levels to enhance readability of complex three-dimensional structures.</p>
     *
     * @param a the three-dimensional boolean array to print (can be {@code null}).
     * @return the string representation of the three-dimensional array that was printed to console.
     * @see #println(boolean[]) for one-dimensional array printing
     * @see #println(boolean[][]) for two-dimensional array printing
     */
    public static String println(final boolean[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final boolean[][] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR_UNIX).append("  ");
                            }

                            if (row[j] == null) {
                                sb.append("null");
                            } else if (row[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final boolean[] subRow = row[j];
                                sb.append('[');

                                for (int k = 0, subRowLen = subRow.length; k < subRowLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(subRow[k]);
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] array = {'a', 'b', 'c'};
     * Arrays.updateAll(array, c -> (char) (c + 1));
     * // array becomes {'b', 'c', 'd'}
     * }</pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The char array to update (can be {@code null}).
     * @param operator The unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
     * @see #updateAll(char[][], Throwables.CharUnaryOperator) for two-dimensional arrays
     * @see #updateAll(char[][][], Throwables.CharUnaryOperator) for three-dimensional arrays
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
     * Recursively updates every element in the two-dimensional character array by applying a given operator.
     * The method iterates through each sub-array and applies the operator to every character.
     * The operation is performed in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] array = {{'a', 'b'}, {'c'}};
     * Arrays.updateAll(array, c -> Character.toUpperCase(c));
     * // array becomes {{'A', 'B'}, {'C'}}
     * }</pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The two-dimensional char array to update (can be {@code null}).
     * @param operator The unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
     * @see #updateAll(char[], Throwables.CharUnaryOperator) for one-dimensional arrays
     * @see #updateAll(char[][][], Throwables.CharUnaryOperator) for three-dimensional arrays
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
     * Recursively updates every element in the three-dimensional character array by applying a given operator.
     * It traverses the nested structure and applies the operator to each character.
     * The operation is performed in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] array = {{{'a'}}, {{'b', 'c'}}};
     * Arrays.updateAll(array, c -> '*');
     * // array becomes {{{'*'}}, {{'*', '*'}}}
     * }</pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The three-dimensional char array to update (can be {@code null}).
     * @param operator The unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
     * @see #updateAll(char[], Throwables.CharUnaryOperator) for one-dimensional arrays
     * @see #updateAll(char[][], Throwables.CharUnaryOperator) for two-dimensional arrays
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
     * the given predicate. Elements that don't match the predicate remain unchanged.
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] array = {'a', 'b', 'c', 'a'};
     * Arrays.replaceIf(array, c -> c == 'a', 'x');
     * // array becomes {'x', 'b', 'c', 'x'}
     * }</pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The char array to modify (can be {@code null}).
     * @param predicate The condition to test for each element.
     * @param newValue The value to be placed in the array if the predicate is true.
     * @throws E if the {@code predicate} throws an exception.
     * @see #replaceIf(char[][], Throwables.CharPredicate, char) for two-dimensional arrays
     * @see #replaceIf(char[][][], Throwables.CharPredicate, char) for three-dimensional arrays
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
     * Recursively replaces each element of a two-dimensional character array with a new value if the
     * element satisfies the given predicate. The operation is performed in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] array = {{'a', 'b'}, {'c', 'A'}};
     * Arrays.replaceIf(array, c -> Character.isUpperCase(c), 'U');
     * // array becomes {{'a', 'b'}, {'c', 'U'}}
     * }</pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The two-dimensional char array to modify (can be {@code null}).
     * @param predicate The condition to test for each element.
     * @param newValue The value to be placed in the array if the predicate is true.
     * @throws E if the {@code predicate} throws an exception.
     * @see #replaceIf(char[], Throwables.CharPredicate, char) for one-dimensional arrays
     * @see #replaceIf(char[][][], Throwables.CharPredicate, char) for three-dimensional arrays
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
     * Recursively replaces each element of a three-dimensional character array with a new value if the
     * element satisfies the given predicate. The operation is performed in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] array = {{{'a'}}, {{'B', 'c'}}};
     * Arrays.replaceIf(array, c -> Character.isUpperCase(c), 'X');
     * // array becomes {{{'a'}}, {{'X', 'c'}}}
     * }</pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The three-dimensional char array to modify (can be {@code null}).
     * @param predicate The condition to test for each element.
     * @param newValue The value to be placed in the array if the predicate is true.
     * @throws E if the {@code predicate} throws an exception.
     * @see #replaceIf(char[], Throwables.CharPredicate, char) for one-dimensional arrays
     * @see #replaceIf(char[][], Throwables.CharPredicate, char) for two-dimensional arrays
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
     * Reshapes a one-dimensional character array into a two-dimensional character array with a specified number of columns.
     * If the length of the input array is not a multiple of {@code cols}, the last sub-array
     * will be shorter.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] array = {'a', 'b', 'c', 'd', 'e'};
     * char[][] reshaped = Arrays.reshape(array, 2);
     * // reshaped is {{'a', 'b'}, {'c', 'd'}, {'e'}}
     * }</pre>
     *
     * @param a The one-dimensional character array to reshape.
     * @param cols The number of columns in the new two-dimensional array.
     * @return A new two-dimensional character array.
     * @throws IllegalArgumentException if {@code cols} is not positive.
     */
    public static char[][] reshape(final char[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new char[0][];
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
     * Reshapes a one-dimensional character array into a three-dimensional character array with specified dimensions.
     * If the input array's length is not a perfect multiple of {@code rows * cols},
     * the last sub-arrays may be shorter.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] array = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
     * char[][][] reshaped = Arrays.reshape(array, 2, 2);
     * // reshaped is {{{'a', 'b'}, {'c', 'd'}}, {{'e', 'f'}, {'g'}}}
     * }</pre>
     *
     * @param a The one-dimensional character array to reshape.
     * @param rows The number of rows in each two-dimensional sub-array.
     * @param cols The number of columns in each two-dimensional sub-array.
     * @return A new three-dimensional character array.
     * @throws IllegalArgumentException if {@code rows} or {@code cols} are not positive.
     */
    public static char[][][] reshape(final char[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new char[0][][];
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
     * Flattens a two-dimensional character array into a new one-dimensional character array by concatenating
     * all its sub-arrays. Null or empty sub-arrays are skipped.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] array = {{'a', 'b'}, null, {'c'}};
     * char[] flattened = Arrays.flatten(array);
     * // flattened is {'a', 'b', 'c'}
     * }</pre>
     *
     * @param a The two-dimensional character array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a three-dimensional character array into a new one-dimensional character array by concatenating
     * all its innermost sub-arrays.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] array = {{{'a'}, {'b'}}, {{'c', 'd'}}};
     * char[] flattened = Arrays.flatten(array);
     * // flattened is {'a', 'b', 'c', 'd'}
     * }</pre>
     *
     * @param a The three-dimensional character array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a two-dimensional array, performs a specified operation on the resulting one-dimensional array,
     * and then copies the modified elements back into the original two-dimensional array.
     * This allows for applying one-dimensional array operations (like sorting) across a two-dimensional structure.
     * The operation is performed in-place on the original two-dimensional array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] array = {{'c', 'a'}, {'b'}};
     * Arrays.flatOp(array, t -> Arrays.sort(t));
     * // array becomes {{'a', 'b'}, {'c'}}
     * }</pre>
     *
     * @param <E> The type of exception that the operation may throw.
     * @param a The two-dimensional character array to operate on.
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
     * Flattens a three-dimensional array, performs a specified operation on the resulting one-dimensional array,
     * and then copies the modified elements back into the original three-dimensional array.
     * This is useful for applying one-dimensional array operations across a three-dimensional structure.
     * The operation is performed in-place on the original three-dimensional array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] array = {{{'d', 'a'}}, {{'c'}, {'b'}}};
     * Arrays.flatOp(array, t -> Arrays.sort(t));
     * // array becomes {{{'a', 'b'}}, {{'c'}, {'d'}}}
     * }</pre>
     *
     * @param <E> The type of exception that the operation may throw.
     * @param a The three-dimensional character array to operate on.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] a = {'A', 'B', 'C', 'D'};
     * char[] b = {'X', 'Y', 'Z'};
     * char[] result = Arrays.zip(a, b, (x, y) -> (char)(x + y - 'A'));
     * // result: {'X', 'Z', '\'}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from both arrays.
     * @return a new array containing the results of applying the zip function to corresponding elements.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] a = { 'A', 'B', 'C', 'D' };
     * char[] b = { 'X', 'Y' };
     * char[] result = Arrays.zip(a, b, '?', '!', (x, y) -> (char) (x + y - 'A'));
     * // result: {'X', 'Z', '#', '$'} (using '!' for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longer input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] a = {'A', 'B', 'C', 'D'};
     * char[] b = {'X', 'Y', 'Z'};
     * char[] c = {'1', '3'};
     * char[] result = Arrays.zip(a, b, c, (x, y, z) -> (char)(x + y + z - 'A' - '0'));
     * // result: {'Y', ']'}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new array containing the results with length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] a = {'A', 'B', 'C', 'D'};
     * char[] b = {'X', 'Y'};
     * char[] c = {'1', '2', '3'};
     * char[] result = Arrays.zip(a, b, c, '?', '!', '0', (x, y, z) -> (char)(x + y + z - 'A' - '0'));
     * // result: {'Y', '\\', '&', '$'} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param valueForNoneC the default value to use when array 'c' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional char arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>This method processes two-dimensional arrays by applying the zip operation to each pair of
     * corresponding inner arrays. The outer array length of the result equals the length
     * of the shorter outer array. Each inner array is processed independently using the
     * single-array zip operation.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] a = {{'A', 'B'}, {'C', 'D', 'E'}};
     * char[][] b = {{'X', 'Y', 'Z'}, {'1', '2'}};
     * char[][] result = Arrays.zip(a, b, (x, y) -> (char)(x + y - 'A'));
     * // result: {{'X', 'Z'}, {'3', '5'}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new two-dimensional array containing the results of zipping corresponding sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional char arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>This method handles two-dimensional arrays of different sizes by using default values for missing
     * elements. The outer array length of the result equals the length of the longer outer array.
     * When processing inner arrays, default values are used for any missing elements within
     * those arrays as well.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] a = {{'A', 'B'}, {'C', 'D', 'E'}};
     * char[][] b = {{'X', 'Y', 'Z'}, {'1', '2'}, {'3'}};
     * char[][] result = Arrays.zip(a, b, '?', '!', (x, y) -> (char)(x + y - 'A'));
     * // result: {{'X', 'Z', 'X'}, {'3', '5', '%'}, {'1'}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the longer input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional char arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>This method processes three two-dimensional arrays simultaneously by applying the tri-function
     * to corresponding elements within each triplet of inner arrays. The outer array length
     * of the result is determined by the shortest outer array among the three inputs.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] a = {{'A', 'B'}, {'C', 'D', 'E'}};
     * char[][] b = {{'X', 'Y', 'Z'}, {'1', '2'}};
     * char[][] c = {{'a', 'b'}, {'c', 'd', 'e'}};
     * char[][] result = Arrays.zip(a, b, c, (x, y, z) -> (char)((x + y + z) / 3));
     * // result: {{'S', 'T'}, {'G', 'H'}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new two-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional char arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p>This method provides complete processing of three two-dimensional arrays regardless of size differences.
     * The result has an outer array length equal to the longest outer array among the inputs.
     * Default values are used whenever an array or sub-array is shorter than the others,
     * ensuring all positions in the result array are filled.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] a = {{'A', 'B'}, {'C', 'D', 'E'}};
     * char[][] b = {{'X', 'Y', 'Z'}, {'1', '2'}, {'3'}};
     * char[][] c = {{'a', 'b'}};
     * char[][] result = Arrays.zip(a, b, c, '?', '!', '~', (x, y, z) -> (char)((x + y + z) / 3));
     * // result: {{'S', 'T', ']'}, {'P', 'Q', 'L'}, {'P'}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional char arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p>This method processes three-dimensional arrays by recursively applying the zip operation to each
     * level of the array hierarchy. The outermost array length of the result equals the
     * length of the shorter outermost array. Each two-dimensional sub-array is processed using the 2D
     * array zip operation.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] a = {{{'A', 'B'}, {'C', 'D'}}, {{'E', 'F'}, {'G', 'H'}}};
     * char[][][] b = {{{'1', '2'}, {'3', '4'}}, {{'5', '6'}, {'7', '8'}}};
     * char[][][] result = Arrays.zip(a, b, (x, y) -> (char)(x + y - 'A' + '0'));
     * // result: {{{'1', '3'}, {'5', '7'}}, {{'9', ';'}, {'=', '?'}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new three-dimensional array containing the results of zipping corresponding two-dimensional sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional char arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>This method handles three-dimensional arrays of different sizes by using default values for missing
     * elements at any level of the array hierarchy. The outermost array length of the result
     * equals the length of the longer outermost array. Default values are propagated through
     * all levels of array processing.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] a = {{{'A', 'B'}, {'C', 'D'}}, {{'E', 'F'}}};
     * char[][][] b = {{{'1', '2'}, {'3', '4'}}, {{'5', '6'}, {'7', '8'}}, {{'9'}}};
     * char[][][] result = Arrays.zip(a, b, 'A', '0', (x, y) -> (char) (x + y - 'A'));
     * // result: result: {{{'1', '3'}, {'5', '7'}}, {{'9', ';'}, {'7', '8'}}, {{'9'}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the longer input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional char arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p>This method processes three three-dimensional arrays simultaneously by applying the tri-function
     * through all levels of the array hierarchy. The outermost array length of the result
     * is determined by the shortest outermost array among the three inputs. Each triplet
     * of two-dimensional sub-arrays is processed using the two-dimensional array tri-zip operation.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] a = {{{'A', 'B'}}};
     * char[][][] b = {{{'1', '2'}, {'3', '4'}}};
     * char[][][] c = {{{'a', 'b'}}};
     * char[][][] result = Arrays.zip(a, b, c, (x, y, z) -> (char)((x + y + z) / 3));
     * // result: {{{'F', 'G'}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new three-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional char arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p>This method provides the most comprehensive zip operation for three-dimensional arrays, handling
     * size differences at every level of the array hierarchy. The result has an outermost
     * array length equal to the longest outermost array among the three inputs. Default
     * values are used whenever any array or sub-array is shorter than the others, ensuring
     * complete processing of all input data.</p>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] a = {{{'A', 'B'}}};
     * char[][][] b = {{{'1', '2'}, {'3', '4'}}};
     * char[][][] c = {{{'a', 'b'}}};
     * char[][][] result = Arrays.zip(a, b, c, '?', '!', '~', (x, y, z) -> (char)((x + y + z) / 3));
     * // result: {{{'F', 'G'}, {'P', 'P'}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Calculates the total number of characters in a two-dimensional character array.
     * It safely handles null sub-arrays.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] array = {{'a', 'b'}, {'c'}, null};
     * long count = Arrays.totalCountOfElements(array);
     * // count is 3
     * }</pre>
     *
     * @param a The two-dimensional character array to count.
     * @return The total number of character elements in the array.
     */
    public static long totalCountOfElements(final char[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        long count = 0;

        for (final char[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of characters in a three-dimensional character array.
     * It safely handles null or empty sub-arrays at any level.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] array = {{{'a'}, {'b', 'c'}}, null, {{{'d'}}}};
     * long count = Arrays.totalCountOfElements(array);
     * // count is 4
     * }</pre>
     *
     * @param a The three-dimensional character array to count.
     * @return The total number of character elements in the array.
     */
    public static long totalCountOfElements(final char[][][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

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
     * Finds the minimum length among all sub-arrays in a two-dimensional character array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] array = {{'a', 'b'}, {'c'}, null};
     * int minLen = Arrays.minSubArrayLen(array);
     * // minLen is 0
     * }</pre>
     *
     * @param a The two-dimensional character array to inspect.
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
     * Finds the maximum length among all sub-arrays in a two-dimensional character array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] array = {{'a'}, {'b', 'c', 'd'}, null};
     * int maxLen = Arrays.maxSubArrayLen(array);
     * // maxLen is 4
     * }</pre>
     *
     * @param a The two-dimensional character array to inspect.
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
     * Prints a string representation of a one-dimensional character array to the console and returns the string.
     *
     * <p>This method formats a one-dimensional character array as a readable string representation with standard
     * bracket notation. The method handles null arrays and empty arrays gracefully, providing
     * appropriate string representations for each case.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[element1, element2, element3]"</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] array = {'H', 'e', 'l', 'l', 'o'};
     * String result = Arrays.println(array);
     * // Prints to console and returns: "[H, e, l, l, o]"
     *
     * // Null array
     * String nullResult = Arrays.println((char[]) null);
     * // Prints "null"
     *
     * // Empty array
     * char[] empty = new char[0];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the one-dimensional character array to print (can be {@code null}).
     * @return the string representation of the array that was printed to console.
     * @see #println(char[][]) for two-dimensional array printing
     * @see #println(char[][][]) for three-dimensional array printing
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
     * Prints a string representation of a two-dimensional character array to the console and returns the string.
     *
     * <p>This method formats a two-dimensional character array as a readable string representation with proper
     * nested bracket notation. Each sub-array is displayed on its own line for better readability.
     * The method handles null arrays, empty arrays, null sub-arrays, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[element1, element2], [element3, element4]]" (with line separators)</li>
     *   <li>Null sub-array: "null" within the outer brackets</li>
     *   <li>Empty sub-array: "[]" within the outer brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] array = {{'a', 'b', 'c'}, {'d', 'e'}, null, {}, {'f'}};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[a, b, c],
     * //  [d, e],
     * //  null,
     * //  [],
     * //  [f]]
     *
     * // Null array
     * String nullResult = Arrays.println((char[][]) null);
     * // Prints "null"
     *
     * // Empty array
     * char[][] empty = new char[0][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the two-dimensional character array to print (can be {@code null}).
     * @return the string representation of the two-dimensional array that was printed to console.
     * @see #println(char[]) for one-dimensional array printing
     * @see #println(char[][][]) for three-dimensional array printing
     */
    public static String println(final char[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final char[] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(row[j]);
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
     * Prints a string representation of a three-dimensional character array to the console and returns the string.
     *
     * <p>This method formats a three-dimensional character array as a readable string representation with proper
     * nested bracket notation. Each two-dimensional sub-array and its inner one-dimensional arrays are displayed with
     * appropriate line separators and indentation for better readability. The method handles
     * null arrays, empty arrays, null sub-arrays at any level, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[[element1, element2]], [[element3, element4]]]" (with line separators and indentation)</li>
     *   <li>Null two-dimensional sub-array: "null" within the outer brackets</li>
     *   <li>Empty two-dimensional sub-array: "[]" within the outer brackets</li>
     *   <li>Null one-dimensional sub-array: "null" within the two-dimensional sub-array brackets</li>
     *   <li>Empty one-dimensional sub-array: "[]" within the two-dimensional sub-array brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] array = {{{'a', 'b', 'c'}, {'d', 'e'}}, {{'f', 'g'}, null, {}}, null};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[[a, b, c],
     * //    [d, e]],
     * //  [[f, g],
     * //   null,
     * //   []],
     * //  null]
     *
     * // Null array
     * String nullResult = Arrays.println((char[][][]) null);
     * // Prints "null"
     *
     * // Empty array
     * char[][][] empty = new char[0][][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use. The formatting includes proper indentation
     * with two spaces for nested levels to enhance readability of complex three-dimensional structures.</p>
     *
     * @param a the three-dimensional character array to print (can be {@code null}).
     * @return the string representation of the three-dimensional array that was printed to console.
     * @see #println(char[]) for one-dimensional array printing
     * @see #println(char[][]) for two-dimensional array printing
     */
    public static String println(final char[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final char[][] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR_UNIX).append("  ");
                            }

                            if (row[j] == null) {
                                sb.append("null");
                            } else if (row[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final char[] subRow = row[j];
                                sb.append('[');

                                for (int k = 0, subRowLen = subRow.length; k < subRowLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(subRow[k]);
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
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] arr = {1, 2, 3};
     * updateAll(arr, b -> (byte)(b * b));
     * // arr is now: [1, 4, 9]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the byte array to update (can be {@code null}).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
     * @see #updateAll(byte[][], Throwables.ByteUnaryOperator) for two-dimensional arrays
     * @see #updateAll(byte[][][], Throwables.ByteUnaryOperator) for three-dimensional arrays
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
     * Updates all elements in a two-dimensional byte array using the provided unary operator.
     * Each element in every sub-array is replaced with the result of applying the operator.
     * This method modifies the array in-place, preserving the two-dimensional structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] matrix = {{1, 2}, {3, 4}};
     * Arrays.updateAll(matrix, b -> (byte)(b * 2));
     * // matrix is now: {{2, 4}, {6, 8}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the two-dimensional byte array to update (can be {@code null} or empty).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
     * @see #updateAll(byte[], Throwables.ByteUnaryOperator) for one-dimensional arrays
     * @see #updateAll(byte[][][], Throwables.ByteUnaryOperator) for three-dimensional arrays
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
     * Updates all elements in a three-dimensional byte array using the provided unary operator.
     * Each element at all levels is replaced with the result of applying the operator.
     * This method modifies the array in-place, preserving the three-dimensional structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] cube = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * Arrays.updateAll(cube, b -> (byte)(b + 10));
     * // cube is now: {{{11, 12}, {13, 14}}, {{15, 16}, {17, 18}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the three-dimensional byte array to update (can be {@code null} or empty).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
     * @see #updateAll(byte[], Throwables.ByteUnaryOperator) for one-dimensional arrays
     * @see #updateAll(byte[][], Throwables.ByteUnaryOperator) for two-dimensional arrays
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
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] arr = {1, 2, 3, 2, 4};
     * replaceIf(arr, b -> b == 2, (byte)10);
     * // arr is now: [1, 10, 3, 10, 4]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the byte array to modify (can be {@code null}).
     * @param predicate the condition to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
     * @see #replaceIf(byte[][], Throwables.BytePredicate, byte) for two-dimensional arrays
     * @see #replaceIf(byte[][][], Throwables.BytePredicate, byte) for three-dimensional arrays
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
     * Replaces all elements in a two-dimensional byte array that match the predicate with a new value.
     * Elements that don't match the predicate remain unchanged. This method modifies the array
     * in-place, traversing all sub-arrays.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] matrix = {{1, 2}, {3, 2}};
     * Arrays.replaceIf(matrix, b -> b == 2, (byte)10);
     * // matrix is now: {{1, 10}, {3, 10}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the two-dimensional byte array to modify (can be {@code null} or empty).
     * @param predicate the condition to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
     * @see #replaceIf(byte[], Throwables.BytePredicate, byte) for one-dimensional arrays
     * @see #replaceIf(byte[][][], Throwables.BytePredicate, byte) for three-dimensional arrays
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
     * Replaces all elements in a three-dimensional byte array that match the predicate with a new value.
     * Elements that don't match the predicate remain unchanged. This method modifies the array
     * in-place, traversing all nested sub-arrays.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] cube = {{{1, 2}, {3, 2}}, {{5, 2}, {7, 8}}};
     * Arrays.replaceIf(cube, b -> b == 2, (byte)0);
     * // cube is now: {{{1, 0}, {3, 0}}, {{5, 0}, {7, 8}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the three-dimensional byte array to modify (can be {@code null} or empty).
     * @param predicate the condition to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
     * @see #replaceIf(byte[], Throwables.BytePredicate, byte) for one-dimensional arrays
     * @see #replaceIf(byte[][], Throwables.BytePredicate, byte) for two-dimensional arrays
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
     * Flattens a two-dimensional byte array into a one-dimensional byte array.
     * All elements from all sub-arrays are combined into a single array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] arr = {{1, 2}, {3}, {4, 5}};
     * byte[] flattened = Arrays.flatten(arr);
     * // Result: {1, 2, 3, 4, 5}
     * }</pre>
     *
     * @param a the two-dimensional byte array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a three-dimensional byte array into a one-dimensional byte array.
     * All elements from all nested sub-arrays are combined into a single array in row-major order.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] cube = {{{1, 2}, {3}}, {{4, 5}}};
     * byte[] flattened = Arrays.flatten(cube);
     * // Result: {1, 2, 3, 4, 5}
     * }</pre>
     *
     * @param a the three-dimensional byte array to flatten (can be {@code null}).
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a two-dimensional byte array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] arr = {{3, 1}, {4, 2}};
     * flatOp(arr, t -> Arrays.sort(t)); // Sorts all elements
     * // arr becomes: {{1, 2}, {3, 4}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operation.
     * @param a the two-dimensional byte array to operate on.
     * @param op the operation to apply to the flattened array.
     * @throws E if the operation throws an exception.
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
     * Flattens a three-dimensional byte array, applies an operation to the flattened array, then sets the values back.
     * This is useful for operations that need to be applied to all elements regardless of structure,
     * such as sorting or bulk modifications. The original three-dimensional structure is preserved.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] cube = {{{3, 1}, {4, 2}}, {{6, 5}, {8, 7}}};
     * Arrays.flatOp(cube, arr -> java.util.Arrays.sort(arr)); // Sorts all elements
     * // cube's structure is preserved but elements are sorted
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operation.
     * @param a the three-dimensional byte array to operate on (can be {@code null} or empty).
     * @param op the operation to apply to the flattened array.
     * @throws E if the operation throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] arr = {1, 2, 3, 4, 5};
     * byte[][] reshaped = Arrays.reshape(arr, 2);
     * // Result: {{1, 2}, {3, 4}, {5}}
     * }</pre>
     *
     * @param a the one-dimensional byte array to reshape.
     * @param cols the number of columns for the reshaped array.
     * @return a two-dimensional byte array with the specified number of columns.
     * @throws IllegalArgumentException if the number of columns is less than or equal to zero.
     */
    public static byte[][] reshape(final byte[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new byte[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] arr = {1, 2, 3, 4, 5, 6};
     * byte[][][] reshaped = Arrays.reshape(arr, 2, 2);
     * // Result: {{{1, 2}, {3, 4}}, {{5, 6}}}
     * }</pre>
     *
     * @param a the one-dimensional byte array to reshape.
     * @param rows the number of rows for the reshaped subarray.
     * @param cols the number of columns for the reshaped subarray.
     * @return a three-dimensional byte array with the specified number of rows and columns.
     * @throws IllegalArgumentException if the number of rows or columns is less than or equal to zero.
     */
    public static byte[][][] reshape(final byte[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new byte[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] a = {1, 2, 3, 4};
     * byte[] b = {5, 6, 7};
     * byte[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from both arrays.
     * @return a new array containing the results of applying the zip function to corresponding elements.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] a = {1, 2, 3, 4};
     * byte[] b = {5, 6};
     * byte[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the length of the longer input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] a = {1, 2, 3, 4};
     * byte[] b = {5, 6, 7};
     * byte[] c = {8, 9};
     * byte[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new array containing the results with length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] a = {1, 2, 3, 4};
     * byte[] b = {5, 6};
     * byte[] c = {8, 9, 10};
     * byte[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param valueForNoneC the default value to use when array 'c' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional byte arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] a = {{1, 2}, {3, 4, 5}};
     * byte[][] b = {{5, 6, 7}, {8, 9}};
     * byte[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new two-dimensional array containing the results of zipping corresponding sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional byte arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] a = {{1, 2}, {3, 4, 5}};
     * byte[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * byte[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the length of the longer input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional byte arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] a = {{1, 2}, {3, 4, 5}};
     * byte[][] b = {{5, 6, 7}, {8, 9}};
     * byte[][] c = {{10, 11}, {12, 13, 14}};
     * byte[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new two-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional byte arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] a = {{1, 2}, {3, 4, 5}};
     * byte[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * byte[][] c = {{10, 11}};
     * byte[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional byte arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * byte[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * byte[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new three-dimensional array containing the results of zipping corresponding two-dimensional sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional byte arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * byte[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * byte[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the length of the longer input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional byte arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] a = {{{1, 2}}};
     * byte[][][] b = {{{11, 12}, {13, 14}}};
     * byte[][][] c = {{{21, 22}}};
     * byte[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new three-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional byte arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] a = {{{1, 2}}};
     * byte[][][] b = {{{11, 12}, {13, 14}}};
     * byte[][][] c = {{{21, 22}}};
     * byte[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] array = {{1, 2, 3}, null, {4, 5}};
     * long count = totalCountOfElements(array); // returns 5
     * }</pre>
     *
     * @param a the two-dimensional byte array.
     * @return the total count of all elements across all sub-arrays.
     */
    public static long totalCountOfElements(final byte[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] array = {{{1, 2}, {3}}, {{4, 5, 6}}};
     * long count = totalCountOfElements(array); // returns 6
     * }</pre>
     *
     * @param a the three-dimensional byte array.
     * @return the total count of all elements across all sub-arrays.
     */
    public static long totalCountOfElements(final byte[][][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] array = {{1, 2, 3}, {4, 5}, {6, 7, 8, 9}};
     * int minLen = minSubArrayLen(array); // returns 2
     * }</pre>
     *
     * @param a the two-dimensional byte array to analyze.
     * @return the minimum sub-array length, or 0 if the array is empty.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] array = {{1, 2}, null, {3, 4, 5, 6}};
     * int maxLen = maxSubArrayLen(array); // returns 4
     * }</pre>
     *
     * @param a the two-dimensional byte array to analyze.
     * @return the maximum sub-array length, or 0 if the array is empty.
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
     * Prints a string representation of a one-dimensional byte array to the console and returns the string.
     *
     * <p>This method formats a one-dimensional byte array as a readable string representation with standard
     * bracket notation. The method handles null arrays and empty arrays gracefully, providing
     * appropriate string representations for each case.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[element1, element2, element3]"</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] array = {1, 2, 3, 4, 5};
     * String result = Arrays.println(array);
     * // Prints to console and returns: "[1, 2, 3, 4, 5]"
     *
     * // Null array
     * String nullResult = Arrays.println((byte[]) null);
     * // Prints "null"
     *
     * // Empty array
     * byte[] empty = new byte[0];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the one-dimensional byte array to print (can be {@code null}).
     * @return the string representation of the array that was printed to console.
     * @see #println(byte[][]) for two-dimensional array printing
     * @see #println(byte[][][]) for three-dimensional array printing
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
     * Prints a string representation of a two-dimensional byte array to the console and returns the string.
     *
     * <p>This method formats a two-dimensional byte array as a readable string representation with proper
     * nested bracket notation. Each sub-array is displayed on its own line for better readability.
     * The method handles null arrays, empty arrays, null sub-arrays, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[element1, element2], [element3, element4]]" (with line separators)</li>
     *   <li>Null sub-array: "null" within the outer brackets</li>
     *   <li>Empty sub-array: "[]" within the outer brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] array = {{1, 2, 3}, {4, 5}, null, {}, {6}};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[1, 2, 3],
     * //  [4, 5],
     * //  null,
     * //  [],
     * //  [6]]
     *
     * // Null array
     * String nullResult = Arrays.println((byte[][]) null);
     * // Prints "null"
     *
     * // Empty array
     * byte[][] empty = new byte[0][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the two-dimensional byte array to print (can be {@code null}).
     * @return the string representation of the two-dimensional array that was printed to console.
     * @see #println(byte[]) for one-dimensional array printing
     * @see #println(byte[][][]) for three-dimensional array printing
     */
    public static String println(final byte[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final byte[] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(row[j]);
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
     * Prints a string representation of a three-dimensional byte array to the console and returns the string.
     *
     * <p>This method formats a three-dimensional byte array as a readable string representation with proper
     * nested bracket notation. Each two-dimensional sub-array and its inner one-dimensional arrays are displayed with
     * appropriate line separators and indentation for better readability. The method handles
     * null arrays, empty arrays, null sub-arrays at any level, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[[element1, element2]], [[element3, element4]]]" (with line separators and indentation)</li>
     *   <li>Null two-dimensional sub-array: "null" within the outer brackets</li>
     *   <li>Empty two-dimensional sub-array: "[]" within the outer brackets</li>
     *   <li>Null one-dimensional sub-array: "null" within the two-dimensional sub-array brackets</li>
     *   <li>Empty one-dimensional sub-array: "[]" within the two-dimensional sub-array brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] array = {{{1, 2, 3}, {4, 5}}, {{6, 7}, null, {}}, null};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[[1, 2, 3],
     * //    [4, 5]],
     * //  [[6, 7],
     * //   null,
     * //   []],
     * //  null]
     *
     * // Null array
     * String nullResult = Arrays.println((byte[][][]) null);
     * // Prints "null"
     *
     * // Empty array
     * byte[][][] empty = new byte[0][][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use. The formatting includes proper indentation
     * with two spaces for nested levels to enhance readability of complex three-dimensional structures.</p>
     *
     * @param a the three-dimensional byte array to print (can be {@code null}).
     * @return the string representation of the three-dimensional array that was printed to console.
     * @see #println(byte[]) for one-dimensional array printing
     * @see #println(byte[][]) for two-dimensional array printing
     */
    public static String println(final byte[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final byte[][] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR_UNIX).append("  ");
                            }

                            if (row[j] == null) {
                                sb.append("null");
                            } else if (row[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final byte[] subRow = row[j];
                                sb.append('[');

                                for (int k = 0, subRowLen = subRow.length; k < subRowLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(subRow[k]);
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
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] array = {1, 2, 3};
     * updateAll(array, x -> (short)(x * x)); // array becomes [1, 4, 9]
     * }</pre>
     *
     * @param <E> the type of exception the operator may throw.
     * @param a the array to update (can be {@code null}).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] array = {{1, 2}, {3, 4}};
     * updateAll(array, x -> (short)(x + 10)); // array becomes {{11, 12}, {13, 14}}
     * }</pre>
     *
     * @param <E> the type of exception the operator may throw.
     * @param a the two-dimensional array to update (can be {@code null}).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] array = {{{1, 2}}, {{3, 4}}};
     * updateAll(array, x -> (short)(x * 2)); // all elements doubled
     * }</pre>
     *
     * @param <E> the type of exception the operator may throw.
     * @param a the three-dimensional array to update (can be {@code null}).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * Only elements for which the predicate returns {@code true} are replaced.
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] array = {1, 2, 3, 4, 5};
     * replaceIf(array, x -> x > 3, (short)0); // array becomes [1, 2, 3, 0, 0]
     * }</pre>
     *
     * @param <E> the type of exception the predicate may throw.
     * @param a the array to modify (can be {@code null}).
     * @param predicate the predicate to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] array = {{1, 2}, {3, 4}};
     * replaceIf(array, x -> x % 2 == 0, (short)0); // array becomes {{1, 0}, {3, 0}}
     * }</pre>
     *
     * @param <E> the type of exception the predicate may throw.
     * @param a the two-dimensional array to modify (can be {@code null}).
     * @param predicate the predicate to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] array = {{{1, 2}}, {{3, 4}}};
     * replaceIf(array, x -> x < 3, (short)10); // replaces 1 and 2 with 10
     * }</pre>
     *
     * @param <E> the type of exception the predicate may throw.
     * @param a the three-dimensional array to modify (can be {@code null}).
     * @param predicate the predicate to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] array = {1, 2, 3, 4, 5};
     * short[][] reshaped = Arrays.reshape(array, 2); // returns {{1, 2}, {3, 4}, {5}}
     * }</pre>
     *
     * @param a the array to reshape.
     * @param cols the number of columns in each row.
     * @return a two-dimensional array with the specified column count.
     * @throws IllegalArgumentException if cols is less than 1.
     */
    public static short[][] reshape(final short[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new short[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] array = {1, 2, 3, 4, 5, 6, 7, 8};
     * short[][][] reshaped = Arrays.reshape(array, 2, 2); // returns {{{1,2},{3,4}}, {{5,6},{7,8}}}
     * }</pre>
     *
     * @param a the array to reshape.
     * @param rows the number of rows in each two-dimensional block.
     * @param cols the number of columns in each row.
     * @return a three-dimensional array with the specified dimensions.
     * @throws IllegalArgumentException if rows or cols is less than 1.
     */
    public static short[][][] reshape(final short[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new short[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] array = {{1, 2}, {3, 4, 5}};
     * short[] flat = Arrays.flatten(array); // returns {1, 2, 3, 4, 5}
     * }</pre>
     *
     * @param a the two-dimensional array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] array = {{{1, 2}, {3}}, {{4, 5}}};
     * short[] flat = Arrays.flatten(array); // returns {1, 2, 3, 4, 5}
     * }</pre>
     *
     * @param a the three-dimensional array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] array = {{3, 1}, {4, 2}};
     * flatOp(array, t -> Arrays.sort(t)); // sorts all elements across sub-arrays
     * }</pre>
     *
     * @param <E> the type of exception the operation may throw.
     * @param a the two-dimensional array to operate on.
     * @param op the operation to perform on the flattened array.
     * @throws E if the operation throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] array = {{{5, 2}}, {{3, 1}}};
     * flatOp(array, t -> Arrays.sort(t)); // sorts all elements across all sub-arrays
     * }</pre>
     *
     * @param <E> the type of exception the operation may throw.
     * @param a the three-dimensional array to operate on.
     * @param op the operation to perform on the flattened array.
     * @throws E if the operation throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] a = {1, 2, 3, 4};
     * short[] b = {5, 6, 7};
     * short[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from both arrays.
     * @return a new array containing the results of applying the zip function to corresponding elements.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] a = {1, 2, 3, 4};
     * short[] b = {5, 6};
     * short[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longer input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] a = {1, 2, 3, 4};
     * short[] b = {5, 6, 7};
     * short[] c = {8, 9};
     * short[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new array containing the results with length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] a = {1, 2, 3, 4};
     * short[] b = {5, 6};
     * short[] c = {8, 9, 10};
     * short[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param valueForNoneC the default value to use when array 'c' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional short arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] a = {{1, 2}, {3, 4, 5}};
     * short[][] b = {{5, 6, 7}, {8, 9}};
     * short[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new two-dimensional array containing the results of zipping corresponding sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional short arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] a = {{1, 2}, {3, 4, 5}};
     * short[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * short[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the shorter input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional short arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] a = {{1, 2}, {3, 4, 5}};
     * short[][] b = {{5, 6, 7}, {8, 9}};
     * short[][] c = {{10, 11}, {12, 13, 14}};
     * short[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new two-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional short arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] a = {{1, 2}, {3, 4, 5}};
     * short[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * short[][] c = {{10, 11}};
     * short[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional short arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * short[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * short[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new three-dimensional array containing the results of zipping corresponding two-dimensional sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional short arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * short[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * short[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the shorter input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional short arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] a = {{{1, 2}}};
     * short[][][] b = {{{11, 12}, {13, 14}}};
     * short[][][] c = {{{21, 22}}};
     * short[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new three-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional short arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] a = {{{1, 2}}};
     * short[][][] b = {{{11, 12}, {13, 14}}};
     * short[][][] c = {{{21, 22}}};
     * short[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Calculates the total number of elements in a two-dimensional short array.
     * Handles null arrays and null sub-arrays gracefully.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] array = {{1, 2, 3}, {4, 5}, null, {6, 7, 8, 9}};
     * long count = totalCountOfElements(array);
     * // count will be 9 (3 + 2 + 0 + 4)
     * }</pre>
     *
     * @param a the two-dimensional array to count elements in.
     * @return the total number of elements across all sub-arrays.
     */
    public static long totalCountOfElements(final short[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        long count = 0;

        for (final short[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of elements in a three-dimensional short array.
     * Handles null arrays and null sub-arrays at all levels gracefully by skipping them.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] cube = {{{1, 2}, {3, 4, 5}}, null, {{6, 7}}};
     * long count = Arrays.totalCountOfElements(cube);
     * // count will be 7 (2 + 3 + 0 + 2)
     * }</pre>
     *
     * @param a the three-dimensional array to count elements in (can be {@code null}).
     * @return the total number of elements across all sub-arrays, or 0 if array is {@code null}/empty.
     */
    public static long totalCountOfElements(final short[][][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

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
     * Finds the minimum length among all sub-arrays in a two-dimensional short array.
     * Returns 0 if the array is null or contains only null sub-arrays.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] array = {{1, 2, 3}, {4, 5}, {6, 7, 8, 9}};
     * int minLen = minSubArrayLen(array);
     * // minLen will be 2
     * }</pre>
     *
     * @param a the two-dimensional array to examine.
     * @return the minimum length of any sub-array, or 0 if array is {@code null}.
     */
    public static int minSubArrayLen(final short[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        int minLen = 0;

        for (final short[] shorts : a) {
            minLen = N.min(minLen, shorts == null ? 0 : shorts.length);
        }

        return minLen;
    }

    /**
     * Finds the maximum length among all sub-arrays in a two-dimensional short array.
     * Returns 0 if the array is null or empty.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] array = {{1, 2, 3}, {4, 5}, {6, 7, 8, 9}};
     * int maxLen = maxSubArrayLen(array);
     * // maxLen will be 4
     * }</pre>
     *
     * @param a the two-dimensional array to examine.
     * @return the maximum length of any sub-array, or 0 if array is {@code null} or empty.
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
     * Prints a string representation of a one-dimensional short array to the console and returns the string.
     *
     * <p>This method formats a one-dimensional short array as a readable string representation with standard
     * bracket notation. The method handles null arrays and empty arrays gracefully, providing
     * appropriate string representations for each case.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[element1, element2, element3]"</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] array = {1, 2, 3, 4, 5};
     * String result = Arrays.println(array);
     * // Prints to console and returns: "[1, 2, 3, 4, 5]"
     *
     * // Null array
     * String nullResult = Arrays.println((short[]) null);
     * // Prints "null"
     *
     * // Empty array
     * short[] empty = new short[0];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the one-dimensional short array to print (can be {@code null}).
     * @return the string representation of the array that was printed to console.
     * @see #println(short[][]) for two-dimensional array printing
     * @see #println(short[][][]) for three-dimensional array printing
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
     * Prints a string representation of a two-dimensional short array to the console and returns the string.
     *
     * <p>This method formats a two-dimensional short array as a readable string representation with proper
     * nested bracket notation. Each sub-array is displayed on its own line for better readability.
     * The method handles null arrays, empty arrays, null sub-arrays, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[element1, element2], [element3, element4]]" (with line separators)</li>
     *   <li>Null sub-array: "null" within the outer brackets</li>
     *   <li>Empty sub-array: "[]" within the outer brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] array = {{1, 2, 3}, {4, 5}, null, {}, {6}};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[1, 2, 3],
     * //  [4, 5],
     * //  null,
     * //  [],
     * //  [6]]
     *
     * // Null array
     * String nullResult = Arrays.println((short[][]) null);
     * // Prints "null"
     *
     * // Empty array
     * short[][] empty = new short[0][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the two-dimensional short array to print (can be {@code null}).
     * @return the string representation of the two-dimensional array that was printed to console.
     * @see #println(short[]) for one-dimensional array printing
     * @see #println(short[][][]) for three-dimensional array printing
     */
    public static String println(final short[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final short[] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(row[j]);
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
     * Prints a string representation of a three-dimensional short array to the console and returns the string.
     *
     * <p>This method formats a three-dimensional short array as a readable string representation with proper
     * nested bracket notation. Each two-dimensional sub-array and its inner one-dimensional arrays are displayed with
     * appropriate line separators and indentation for better readability. The method handles
     * null arrays, empty arrays, null sub-arrays at any level, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[[element1, element2]], [[element3, element4]]]" (with line separators and indentation)</li>
     *   <li>Null two-dimensional sub-array: "null" within the outer brackets</li>
     *   <li>Empty two-dimensional sub-array: "[]" within the outer brackets</li>
     *   <li>Null one-dimensional sub-array: "null" within the two-dimensional sub-array brackets</li>
     *   <li>Empty one-dimensional sub-array: "[]" within the two-dimensional sub-array brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] array = {{{1, 2, 3}, {4, 5}}, {{6, 7}, null, {}}, null};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[[1, 2, 3],
     * //    [4, 5]],
     * //  [[6, 7],
     * //   null,
     * //   []],
     * //  null]
     *
     * // Null array
     * String nullResult = Arrays.println((short[][][]) null);
     * // Prints "null"
     *
     * // Empty array
     * short[][][] empty = new short[0][][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use. The formatting includes proper indentation
     * with two spaces for nested levels to enhance readability of complex three-dimensional structures.</p>
     *
     * @param a the three-dimensional short array to print (can be {@code null}).
     * @return the string representation of the three-dimensional array that was printed to console.
     * @see #println(short[]) for one-dimensional array printing
     * @see #println(short[][]) for two-dimensional array printing
     */
    public static String println(final short[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final short[][] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR_UNIX).append("  ");
                            }

                            if (row[j] == null) {
                                sb.append("null");
                            } else if (row[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final short[] subRow = row[j];
                                sb.append('[');

                                for (int k = 0, subRowLen = subRow.length; k < subRowLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(subRow[k]);
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
     * Each element is replaced with the result of applying the operator.
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] arr = {1, 2, 3};
     * Arrays.updateAll(arr, x -> x * x);
     * // arr is now [1, 4, 9]
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw.
     * @param a the array to be modified (can be {@code null}).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * Updates all elements of the given two-dimensional array by applying the specified unary operator.
     *
     * <p>This method modifies the original two-dimensional array by applying the operator function to each element
     * in all sub-arrays. The operator can throw a checked exception of type E.
     * If the array is null or empty, the method returns without performing any operation.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] arr = {{1, 2}, {3, 4}};
     * Arrays.updateAll(arr, x -> x + 10);
     * // arr is now {{11, 12}, {13, 14}}
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw.
     * @param a the two-dimensional array to be modified.
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * Updates all elements of the given three-dimensional array by applying the specified unary operator.
     *
     * <p>This method modifies the original three-dimensional array by applying the operator function to each element
     * in all nested arrays. The operator can throw a checked exception of type E.
     * If the array is null or empty, the method returns without performing any operation.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] arr = {{{1, 2}}, {{3, 4}}};
     * Arrays.updateAll(arr, x -> x * 2);
     * // arr is now {{{2, 4}}, {{6, 8}}}
     * }</pre>
     *
     * @param <E> the type of exception that the operator may throw.
     * @param a the three-dimensional array to be modified.
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * Elements that don't match the predicate remain unchanged.
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] arr = {1, 2, 3, 4, 5};
     * Arrays.replaceIf(arr, x -> x % 2 == 0, 0);
     * // arr is now [1, 0, 3, 0, 5]
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw.
     * @param a the array to be modified (can be {@code null}).
     * @param predicate the predicate to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
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
     * Replaces elements in the two-dimensional array that match the given predicate with the specified new value.
     *
     * <p>This method modifies the original two-dimensional array by testing each element in all sub-arrays
     * with the predicate and replacing matching elements with the new value.
     * If the array is null or empty, the method returns without performing any operation.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] arr = {{1, 2}, {3, 4}};
     * Arrays.replaceIf(arr, x -> x > 2, 10);
     * // arr is now {{1, 2}, {10, 10}}
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw.
     * @param a the two-dimensional array to be modified.
     * @param predicate the predicate to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
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
     * Replaces elements in the three-dimensional array that match the given predicate with the specified new value.
     *
     * <p>This method modifies the original three-dimensional array by testing each element in all nested arrays
     * with the predicate and replacing matching elements with the new value.
     * If the array is null or empty, the method returns without performing any operation.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] arr = {{{1, 2}}, {{3, 4}}};
     * Arrays.replaceIf(arr, x -> x < 3, 0);
     * // arr is now {{{0, 0}}, {{3, 4}}}
     * }</pre>
     *
     * @param <E> the type of exception that the predicate may throw.
     * @param a the three-dimensional array to be modified.
     * @param predicate the predicate to test each element.
     * @param newValue the value to replace matching elements with.
     * @throws E if the {@code predicate} throws an exception.
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
     * Reshapes a one-dimensional array into a two-dimensional array with the specified number of columns.
     *
     * <p>This method creates a new two-dimensional array by dividing the input array into rows of the specified
     * column count. The last row may contain fewer elements if the array length is not evenly
     * divisible by the column count. If the input array is null or empty, returns an empty two-dimensional array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] arr = {1, 2, 3, 4, 5, 6, 7};
     * int[][] result = Arrays.reshape(arr, 3);
     * // result is {{1, 2, 3}, {4, 5, 6}, {7}}
     * }</pre>
     *
     * @param a the one-dimensional array to reshape.
     * @param cols the number of columns in each row.
     * @return a new two-dimensional array containing the reshaped data.
     * @throws IllegalArgumentException if cols is less than 1.
     */
    public static int[][] reshape(final int[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new int[0][];
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
     * Reshapes a one-dimensional array into a three-dimensional array with the specified dimensions.
     *
     * <p>This method creates a new three-dimensional array by dividing the input array first into matrices
     * of the specified row and column counts. Each matrix may be incomplete if the array length
     * is not evenly divisible by rows × cols. If the input array is null or empty, returns an empty three-dimensional array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
     * int[][][] result = Arrays.reshape(arr, 2, 2);
     * // result is {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}}
     * }</pre>
     *
     * @param a the one-dimensional array to reshape.
     * @param rows the number of rows in each two-dimensional matrix.
     * @param cols the number of columns in each row.
     * @return a new three-dimensional array containing the reshaped data.
     * @throws IllegalArgumentException if rows or cols is less than 1.
     */
    public static int[][][] reshape(final int[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new int[0][][];
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
     * Flattens a two-dimensional array into a one-dimensional array.
     *
     * <p>This method creates a new one-dimensional array containing all elements from the two-dimensional array
     * in row-major order. Empty sub-arrays are skipped. If the input array is null or empty,
     * returns an empty array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] arr = {{1, 2, 3}, {4, 5}, {6, 7, 8}};
     * int[] result = Arrays.flatten(arr);
     * // result is {1, 2, 3, 4, 5, 6, 7, 8}
     * }</pre>
     *
     * @param a the two-dimensional array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a three-dimensional array into a one-dimensional array.
     *
     * <p>This method creates a new one-dimensional array containing all elements from the three-dimensional array
     * in depth-first order. Empty sub-arrays at any level are skipped. If the input array
     * is null or empty, returns an empty array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] arr = {{{1, 2}, {3}}, {{4, 5, 6}}};
     * int[] result = Arrays.flatten(arr);
     * // result is {1, 2, 3, 4, 5, 6}
     * }</pre>
     *
     * @param a the three-dimensional array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a two-dimensional array, applies the given operation, and updates the original array with the results.
     *
     * <p>This method provides a convenient way to apply operations that work on one-dimensional arrays to two-dimensional arrays.
     * The array is flattened, the operation is applied to the flattened array, and then the values
     * are copied back to the original two-dimensional array structure.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] arr = {{3, 1, 4}, {1, 5, 9}};
     * Arrays.flatOp(arr, t -> Arrays.sort(t));
     * // arr is now {{1, 1, 3}, {4, 5, 9}}
     * }</pre>
     *
     * @param <E> the type of exception that the operation may throw.
     * @param a the two-dimensional array to process.
     * @param op the operation to apply to the flattened array.
     * @throws E if the operation throws an exception.
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
     * Flattens a three-dimensional array, applies the given operation, and updates the original array with the results.
     *
     * <p>This method provides a convenient way to apply operations that work on one-dimensional arrays to three-dimensional arrays.
     * The array is flattened, the operation is applied to the flattened array, and then the values
     * are copied back to the original three-dimensional array structure.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] arr = {{{5, 2}}, {{8, 1}}};
     * Arrays.flatOp(arr, t -> Arrays.sort(t));
     * // arr is now {{{1, 2}}, {{5, 8}}}
     * }</pre>
     *
     * @param <E> the type of exception that the operation may throw.
     * @param a the three-dimensional array to process.
     * @param op the operation to apply to the flattened array.
     * @throws E if the operation throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] a = {1, 2, 3, 4};
     * int[] b = {5, 6, 7};
     * int[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from both arrays.
     * @return a new array containing the results of applying the zip function to corresponding elements.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] a = {1, 2, 3, 4};
     * int[] b = {5, 6};
     * int[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longer input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] a = {1, 2, 3, 4};
     * int[] b = {5, 6, 7};
     * int[] c = {8, 9};
     * int[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new array containing the results with length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] a = {1, 2, 3, 4};
     * int[] b = {5, 6};
     * int[] c = {8, 9, 10};
     * int[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param valueForNoneC the default value to use when array 'c' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional int arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] a = {{1, 2}, {3, 4, 5}};
     * int[][] b = {{5, 6, 7}, {8, 9}};
     * int[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new two-dimensional array containing the results of zipping corresponding sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional int arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] a = {{1, 2}, {3, 4, 5}};
     * int[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * int[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the inter input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional int arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] a = {{1, 2}, {3, 4, 5}};
     * int[][] b = {{5, 6, 7}, {8, 9}};
     * int[][] c = {{10, 11}, {12, 13, 14}};
     * int[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new two-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional int arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] a = {{1, 2}, {3, 4, 5}};
     * int[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * int[][] c = {{10, 11}};
     * int[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional int arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * int[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * int[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new three-dimensional array containing the results of zipping corresponding two-dimensional sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional int arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * int[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * int[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the inter input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional int arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] a = {{{1, 2}}};
     * int[][][] b = {{{11, 12}, {13, 14}}};
     * int[][][] c = {{{21, 22}}};
     * int[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new three-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional int arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] a = {{{1, 2}}};
     * int[][][] b = {{{11, 12}, {13, 14}}};
     * int[][][] c = {{{21, 22}}};
     * int[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Calculates the total number of integer elements in a two-dimensional array.
     * It handles null or empty sub-arrays gracefully.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] a = {{1, 2}, {3, 4, 5}, null, {}};
     * long count = totalCountOfElements(a);
     * // count will be 5
     * }</pre>
     *
     * @param a The two-dimensional integer array.
     * @return The total count of integer elements.
     */
    public static long totalCountOfElements(final int[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        long count = 0;

        for (final int[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of integer elements in a three-dimensional array.
     * It safely handles null or empty sub-arrays at any depth.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] a = {{{1}, {2, 3}}, null, {{{4, 5, 6}}}};
     * long count = totalCountOfElements(a);
     * // count will be 6
     * }</pre>
     *
     * @param a The three-dimensional integer array.
     * @return The total count of integer elements.
     */
    public static long totalCountOfElements(final int[][][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

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
     * Finds the minimum length among all sub-arrays in a two-dimensional integer array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] a = {{1, 2, 3}, {4, 5}, null, {6}};
     * int minLen = minSubArrayLen(a);
     * // minLen will be 0
     * }</pre>
     *
     * @param a The two-dimensional integer array.
     * @return The minimum length of a sub-array, or 0 if the input array is {@code null} or empty.
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
     * Finds the maximum length among all sub-arrays in a two-dimensional integer array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] a = {{1}, {2, 3}, null, {4, 5, 6}};
     * int maxLen = maxSubArrayLen(a);
     * // maxLen will be 3
     * }</pre>
     *
     * @param a The two-dimensional integer array.
     * @return The maximum length of a sub-array, or 0 if the input array is {@code null} or empty.
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
     * Prints a string representation of a one-dimensional integer array to the console and returns the string.
     *
     * <p>This method formats a one-dimensional integer array as a readable string representation with standard
     * bracket notation. The method handles null arrays and empty arrays gracefully, providing
     * appropriate string representations for each case.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[element1, element2, element3]"</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] array = {1, 2, 3, 4, 5};
     * String result = Arrays.println(array);
     * // Prints to console and returns: "[1, 2, 3, 4, 5]"
     *
     * // Null array
     * String nullResult = Arrays.println(null);
     * // Prints "null"
     *
     * // Empty array
     * int[] empty = new int[0];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the one-dimensional integer array to print (can be {@code null}).
     * @return the string representation of the array that was printed to console.
     * @see #println(int[][]) for two-dimensional array printing
     * @see #println(int[][][]) for three-dimensional array printing
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
     * Prints a string representation of a two-dimensional integer array to the console and returns the string.
     * 
     * <p>This method formats a two-dimensional integer array as a readable string representation with proper
     * nested bracket notation. Each sub-array is displayed on its own line for better readability.
     * The method handles null arrays, empty arrays, null sub-arrays, and empty sub-arrays gracefully.</p>
     * 
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[element1, element2], [element3, element4]]" (with line separators)</li>
     *   <li>Null sub-array: "null" within the outer brackets</li>
     *   <li>Empty sub-array: "[]" within the outer brackets</li>
     * </ul>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] array = {{1, 2, 3}, {4, 5}, null, {}, {6}};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[1, 2, 3],
     * //  [4, 5],
     * //  null,
     * //  [],
     * //  [6]]
     * 
     * // Null array
     * String nullResult = Arrays.println(null);
     * // Prints "null"
     * 
     * // Empty array
     * int[][] empty = new int[0][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     * 
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     * 
     * @param a the two-dimensional integer array to print (can be {@code null}).
     * @return the string representation of the two-dimensional array that was printed to console.
     * @see #println(int[]) for one-dimensional array printing
     * @see #println(int[][][]) for three-dimensional array printing
     */
    public static String println(final int[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final int[] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(row[j]);
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
     * Prints a string representation of a three-dimensional integer array to the console and returns the string.
     *
     * <p>This method formats a three-dimensional integer array as a readable string representation with proper
     * nested bracket notation. Each two-dimensional sub-array and its inner one-dimensional arrays are displayed with
     * appropriate line separators and indentation for better readability. The method handles
     * null arrays, empty arrays, null sub-arrays at any level, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[[element1, element2]], [[element3, element4]]]" (with line separators and indentation)</li>
     *   <li>Null two-dimensional sub-array: "null" within the outer brackets</li>
     *   <li>Empty two-dimensional sub-array: "[]" within the outer brackets</li>
     *   <li>Null one-dimensional sub-array: "null" within the two-dimensional sub-array brackets</li>
     *   <li>Empty one-dimensional sub-array: "[]" within the two-dimensional sub-array brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] array = {{{1, 2, 3}, {4, 5}}, {{6, 7}, null, {}}, null};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[[1, 2, 3],
     * //    [4, 5]],
     * //  [[6, 7],
     * //   null,
     * //   []],
     * //  null]
     *
     * // Null array
     * String nullResult = Arrays.println((int[][][]) null);
     * // Prints "null"
     *
     * // Empty array
     * int[][][] empty = new int[0][][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use. The formatting includes proper indentation
     * with two spaces for nested levels to enhance readability of complex three-dimensional structures.</p>
     *
     * @param a the three-dimensional integer array to print (can be {@code null}).
     * @return the string representation of the three-dimensional array that was printed to console.
     * @see #println(int[]) for one-dimensional array printing
     * @see #println(int[][]) for two-dimensional array printing
     */
    public static String println(final int[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final int[][] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR_UNIX).append("  ");
                            }

                            if (row[j] == null) {
                                sb.append("null");
                            } else if (row[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final int[] subRow = row[j];
                                sb.append('[');

                                for (int k = 0, subRowLen = subRow.length; k < subRowLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(subRow[k]);
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
     * Updates each element of the specified long array by applying a unary operator.
     * Each element is replaced with the result of applying the operator.
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] array = {1L, 2L, 3L};
     * updateAll(array, x -> x * x);
     * // array is now [1L, 4L, 9L]
     * }</pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The array to be modified (can be {@code null}).
     * @param operator The unary operator to apply to each element.
     * @throws E If the {@code operator} throws an exception.
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
     * Updates each element of the specified two-dimensional long array in-place by applying a unary operator.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] array = {{1L, 2L}, {3L, 4L}};
     * updateAll(array, x -> -x);
     * // array is now {{-1L, -2L}, {-3L, -4L}}
     * }</pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The two-dimensional array to be modified. The modification happens in-place.
     * @param operator The unary operator to apply to each element.
     * @throws E If the {@code operator} throws an exception.
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
     * Updates each element of the specified three-dimensional long array in-place by applying a unary operator.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] array = {{{1L}, {2L}}, {{3L}, {4L}}};
     * updateAll(array, x -> x + 1);
     * // array is now {{{2L}, {3L}}, {{4L}, {5L}}}
     * }</pre>
     *
     * @param <E> The type of exception that the operator may throw.
     * @param a The three-dimensional array to be modified. The modification happens in-place.
     * @param operator The unary operator to apply to each element.
     * @throws E If the {@code operator} throws an exception.
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
     * Elements that don't match the predicate remain unchanged.
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] array = {1L, 2L, 3L, 4L, 5L};
     * replaceIf(array, x -> x % 2 == 0, 0L);
     * // array is now [1L, 0L, 3L, 0L, 5L]
     * }</pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The array to be modified (can be {@code null}).
     * @param predicate The condition to test for each element.
     * @param newValue The value to replace with if the predicate is true.
     * @throws E If the {@code predicate} throws an exception.
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
     * Replaces each element of a two-dimensional long array with a new value if it satisfies a given predicate.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] array = {{1L, -2L}, {3L, -4L}};
     * replaceIf(array, x -> x < 0, 0L);
     * // array is now {{1L, 0L}, {3L, 0L}}
     * }</pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The two-dimensional array to be modified.
     * @param predicate The condition to test for each element.
     * @param newValue The value to replace with if the predicate is true.
     * @throws E If the {@code predicate} throws an exception.
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
     * Replaces each element of a three-dimensional long array with a new value if it satisfies a given predicate.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] array = {{{1L, 2L}}, {{-3L, 4L}}};
     * replaceIf(array, x -> x < 0, 99L);
     * // array is now {{{1L, 2L}}, {{99L, 4L}}}
     * }</pre>
     *
     * @param <E> The type of exception that the predicate may throw.
     * @param a The three-dimensional array to be modified.
     * @param predicate The condition to test for each element.
     * @param newValue The value to replace with if the predicate is true.
     * @throws E If the {@code predicate} throws an exception.
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
     * Reshapes a one-dimensional long array into a two-dimensional long array with a specified number of columns.
     * The last row may be shorter if the total number of elements is not a multiple of {@code cols}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] array = {1, 2, 3, 4, 5, 6, 7};
     * long[][] reshaped = Arrays.reshape(array, 3);
     * // reshaped is {{1, 2, 3}, {4, 5, 6}, {7}}
     * }</pre>
     *
     * @param a The one-dimensional array to reshape.
     * @param cols The number of columns in the resulting two-dimensional array.
     * @return A new two-dimensional long array.
     * @throws IllegalArgumentException if {@code cols} is not positive.
     */
    public static long[][] reshape(final long[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new long[0][];
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
     * Reshapes a one-dimensional long array into a three-dimensional long array with a specified number of rows and columns.
     * The last sub-arrays may be shorter if the total element count is not perfectly divisible.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
     * long[][][] reshaped = Arrays.reshape(array, 2, 2);
     * // reshaped is {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}, {{9}}}
     * }</pre>
     *
     * @param a The one-dimensional array to reshape.
     * @param rows The number of rows in each two-dimensional sub-array.
     * @param cols The number of columns in each two-dimensional sub-array.
     * @return A new three-dimensional long array.
     * @throws IllegalArgumentException if {@code rows} or {@code cols} are not positive.
     */
    public static long[][][] reshape(final long[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new long[0][][];
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
     * Flattens a two-dimensional long array into a one-dimensional long array by concatenating its rows.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] array = {{1L, 2L}, {3L, 4L, 5L}};
     * long[] flattened = Arrays.flatten(array);
     * // flattened is {1L, 2L, 3L, 4L, 5L}
     * }</pre>
     *
     * @param a The two-dimensional array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a three-dimensional long array into a one-dimensional long array by concatenating its elements in order.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] array = {{{1L, 2L}}, {{3L, 4L}, {5L}}};
     * long[] flattened = Arrays.flatten(array);
     * // flattened is {1L, 2L, 3L, 4L, 5L}
     * }</pre>
     *
     * @param a The three-dimensional array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a two-dimensional array, performs an in-place operation on the resulting one-dimensional array, and then
     * copies the modified elements back into the original two-dimensional array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] array = {{3L, 1L}, {4L, 2L}};
     * flatOp(array, t -> Arrays.sort(t));
     * // array is now {{1L, 2L}, {3L, 4L}}
     * }</pre>
     *
     * @param <E> The type of exception that the operation may throw.
     * @param a The two-dimensional array to operate on.
     * @param op The consumer to accept the flattened one-dimensional array for modification.
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
     * Flattens a three-dimensional array, performs an in-place operation on the resulting one-dimensional array, and then
     * copies the modified elements back into the original three-dimensional array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] array = {{{3L}, {1L}}, {{4L, 2L}}};
     * flatOp(array, t -> Arrays.sort(t));
     * // array is now {{{1L}, {2L}}, {{3L, 4L}}}
     * }</pre>
     *
     * @param <E> The type of exception that the operation may throw.
     * @param a The three-dimensional array to operate on.
     * @param op The consumer to accept the flattened one-dimensional array for modification.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] a = {1, 2, 3, 4};
     * long[] b = {5, 6, 7};
     * long[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from both arrays.
     * @return a new array containing the results of applying the zip function to corresponding elements.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] a = {1, 2, 3, 4};
     * long[] b = {5, 6};
     * long[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longer input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] a = {1, 2, 3, 4};
     * long[] b = {5, 6, 7};
     * long[] c = {8, 9};
     * long[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new array containing the results with length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] a = {1, 2, 3, 4};
     * long[] b = {5, 6};
     * long[] c = {8, 9, 10};
     * long[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param valueForNoneC the default value to use when array 'c' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional long arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] a = {{1, 2}, {3, 4, 5}};
     * long[][] b = {{5, 6, 7}, {8, 9}};
     * long[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new two-dimensional array containing the results of zipping corresponding sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional long arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] a = {{1, 2}, {3, 4, 5}};
     * long[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * long[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the longer input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional long arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] a = {{1, 2}, {3, 4, 5}};
     * long[][] b = {{5, 6, 7}, {8, 9}};
     * long[][] c = {{10, 11}, {12, 13, 14}};
     * long[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new two-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional long arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] a = {{1, 2}, {3, 4, 5}};
     * long[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * long[][] c = {{10, 11}};
     * long[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional long arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * long[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * long[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new three-dimensional array containing the results of zipping corresponding two-dimensional sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional long arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * long[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * long[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the longer input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional long arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] a = {{{1, 2}}};
     * long[][][] b = {{{11, 12}, {13, 14}}};
     * long[][][] c = {{{21, 22}}};
     * long[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new three-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional long arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] a = {{{1, 2}}};
     * long[][][] b = {{{11, 12}, {13, 14}}};
     * long[][][] c = {{{21, 22}}};
     * long[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Calculates the total number of long elements in a two-dimensional array, handling null sub-arrays.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] array = {{1, 2}, {3, 4, 5}, null};
     * long count = totalCountOfElements(array);
     * // count is 5
     * }</pre>
     *
     * @param a The two-dimensional array to inspect.
     * @return The total count of long elements.
     */
    public static long totalCountOfElements(final long[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        long count = 0;

        for (final long[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of long elements in a three-dimensional array, handling null sub-arrays.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] array = {{{1}, {2, 3}}, null, {{{4, 5, 6}}}};
     * long count = totalCountOfElements(array);
     * // count is 6
     * }</pre>
     *
     * @param a The three-dimensional array to inspect.
     * @return The total count of long elements.
     */
    public static long totalCountOfElements(final long[][][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

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
     * Finds the minimum length among all sub-arrays in a two-dimensional long array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] a = {{1L, 2L, 3L}, {4L, 5L}, null, {6L}};
     * int minLen = minSubArrayLen(a);
     * // minLen will be 0
     * }</pre>
     *
     * @param a The two-dimensional long array.
     * @return The minimum length of a sub-array, or 0 if the input array is {@code null} or empty.
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
     * Finds the maximum length among all sub-arrays in a two-dimensional long array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] a = {{1L}, {2L, 3L}, null, {4L, 5L, 6L}};
     * int maxLen = maxSubArrayLen(a);
     * // maxLen will be 3
     * }</pre>
     *
     * @param a The two-dimensional long array.
     * @return The maximum length of a sub-array, or 0 if the input array is {@code null} or empty.
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
     * Prints a string representation of a one-dimensional long array to the console and returns the string.
     *
     * <p>This method formats a one-dimensional long array as a readable string representation with standard
     * bracket notation. The method handles null arrays and empty arrays gracefully, providing
     * appropriate string representations for each case.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[element1, element2, element3]"</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] array = {1L, 2L, 3L, 4L, 5L};
     * String result = Arrays.println(array);
     * // Prints to console and returns: "[1, 2, 3, 4, 5]"
     *
     * // Null array
     * String nullResult = Arrays.println((long[]) null);
     * // Prints "null"
     *
     * // Empty array
     * long[] empty = new long[0];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the one-dimensional long array to print (can be {@code null}).
     * @return the string representation of the array that was printed to console.
     * @see #println(long[][]) for two-dimensional array printing
     * @see #println(long[][][]) for three-dimensional array printing
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
     * Prints a string representation of a two-dimensional long array to the console and returns the string.
     *
     * <p>This method formats a two-dimensional long array as a readable string representation with proper
     * nested bracket notation. Each sub-array is displayed on its own line for better readability.
     * The method handles null arrays, empty arrays, null sub-arrays, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[element1, element2], [element3, element4]]" (with line separators)</li>
     *   <li>Null sub-array: "null" within the outer brackets</li>
     *   <li>Empty sub-array: "[]" within the outer brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] array = {{1L, 2L, 3L}, {4L, 5L}, null, {}, {6L}};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[1, 2, 3],
     * //  [4, 5],
     * //  null,
     * //  [],
     * //  [6]]
     *
     * // Null array
     * String nullResult = Arrays.println((long[][]) null);
     * // Prints "null"
     *
     * // Empty array
     * long[][] empty = new long[0][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the two-dimensional long array to print (can be {@code null}).
     * @return the string representation of the two-dimensional array that was printed to console.
     * @see #println(long[]) for one-dimensional array printing
     * @see #println(long[][][]) for three-dimensional array printing
     */
    public static String println(final long[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final long[] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(row[j]);
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
     * Prints a string representation of a three-dimensional long array to the console and returns the string.
     *
     * <p>This method formats a three-dimensional long array as a readable string representation with proper
     * nested bracket notation. Each two-dimensional sub-array and its inner one-dimensional arrays are displayed with
     * appropriate line separators and indentation for better readability. The method handles
     * null arrays, empty arrays, null sub-arrays at any level, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[[element1, element2]], [[element3, element4]]]" (with line separators and indentation)</li>
     *   <li>Null two-dimensional sub-array: "null" within the outer brackets</li>
     *   <li>Empty two-dimensional sub-array: "[]" within the outer brackets</li>
     *   <li>Null one-dimensional sub-array: "null" within the two-dimensional sub-array brackets</li>
     *   <li>Empty one-dimensional sub-array: "[]" within the two-dimensional sub-array brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] array = {{{1L, 2L, 3L}, {4L, 5L}}, {{6L, 7L}, null, {}}, null};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[[1, 2, 3],
     * //    [4, 5]],
     * //  [[6, 7],
     * //   null,
     * //   []],
     * //  null]
     *
     * // Null array
     * String nullResult = Arrays.println((long[][][]) null);
     * // Prints "null"
     *
     * // Empty array
     * long[][][] empty = new long[0][][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use. The formatting includes proper indentation
     * with two spaces for nested levels to enhance readability of complex three-dimensional structures.</p>
     *
     * @param a the three-dimensional long array to print (can be {@code null}).
     * @return the string representation of the three-dimensional array that was printed to console.
     * @see #println(long[]) for one-dimensional array printing
     * @see #println(long[][]) for two-dimensional array printing
     */
    public static String println(final long[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final long[][] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR_UNIX).append("  ");
                            }

                            if (row[j] == null) {
                                sb.append("null");
                            } else if (row[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final long[] subRow = row[j];
                                sb.append('[');

                                for (int k = 0, subRowLen = subRow.length; k < subRowLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(subRow[k]);
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
     * Updates each element of the specified float array by applying a given unary operator.
     * Each element is replaced with the result of applying the operator.
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] array = {1.0f, -2.0f, 3.0f};
     * Arrays.updateAll(array, x -> Math.abs(x)); // array becomes [1.0f, 2.0f, 3.0f]
     * }</pre>
     *
     * @param <E> the type of exception that can be thrown by the operator.
     * @param a the array to be updated (can be {@code null}).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * Updates each element of the specified two-dimensional float array in-place by applying a given unary operator.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] matrix = {{1.0f, -2.0f}, {-3.0f, 4.0f}};
     * Arrays.updateAll(matrix, x -> x * x); // matrix becomes {{1.0f, 4.0f}, {9.0f, 16.0f}}
     * }</pre>
     *
     * @param <E> the type of exception that can be thrown by the operator.
     * @param a the two-dimensional array to be updated (can be {@code null}).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * Updates each element of the specified three-dimensional float array in-place by applying a given unary operator.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] cube = {{{-1.0f}}, {{2.0f}}};
     * Arrays.updateAll(cube, x -> -x); // cube becomes {{{1.0f}}, {{-2.0f}}}
     * }</pre>
     *
     * @param <E> the type of exception that can be thrown by the operator.
     * @param a the three-dimensional array to be updated (can be {@code null}).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * Elements that don't match the predicate remain unchanged.
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] array = {1.0f, -2.0f, 3.0f, -4.0f};
     * Arrays.replaceIf(array, x -> x < 0, 0.0f); // array becomes [1.0f, 0.0f, 3.0f, 0.0f]
     * }</pre>
     *
     * @param <E> the type of exception that can be thrown by the predicate.
     * @param a the array to be modified (can be {@code null}).
     * @param predicate the condition to test for each element.
     * @param newValue the value to be placed in the array if the predicate is true.
     * @throws E if the {@code predicate} throws an exception.
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
     * Replaces each element of a two-dimensional float array with the specified new value if it satisfies the given predicate.
     * The modification is done in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] matrix = {{1.0f, -2.0f}, {0.0f, -4.0f}};
     * Arrays.replaceIf(matrix, x -> x <= 0, 99.0f); // matrix becomes {{1.0f, 99.0f}, {99.0f, 99.0f}}
     * }</pre>
     *
     * @param <E> the type of exception that can be thrown by the predicate.
     * @param a the two-dimensional array to be modified.
     * @param predicate the condition to test for each element.
     * @param newValue the value to be placed in the array if the predicate is true.
     * @throws E if the {@code predicate} throws an exception.
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
     * Replaces each element of a three-dimensional float array with the specified new value if it satisfies the given predicate.
     * The modification is done in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] cube = {{{1f, -2f}}, {{-3f, 4f}}};
     * Arrays.replaceIf(cube, x -> x > 0, 0.0f); // cube becomes {{{0.0f, -2.0f}}, {{-3.0f, 0.0f}}}
     * }</pre>
     *
     * @param <E> the type of exception that can be thrown by the predicate.
     * @param a the three-dimensional array to be modified.
     * @param predicate the condition to test for each element.
     * @param newValue the value to be placed in the array if the predicate is true.
     * @throws E if the {@code predicate} throws an exception.
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
     * Reshapes a one-dimensional float array into a two-dimensional float array with the specified number of columns.
     * The last row of the resulting two-dimensional array may have fewer elements if the length of the
     * input array is not a multiple of {@code cols}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] array = {1, 2, 3, 4, 5, 6, 7};
     * float[][] matrix = Arrays.reshape(array, 3); // returns {{1, 2, 3}, {4, 5, 6}, {7}}
     * }</pre>
     *
     * @param a the one-dimensional array to reshape.
     * @param cols the number of columns in the new two-dimensional array.
     * @return a new two-dimensional array containing the elements of the input array.
     * @throws IllegalArgumentException if {@code cols} is not positive.
     */
    public static float[][] reshape(final float[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new float[0][];
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
     * Reshapes a one-dimensional float array into a three-dimensional float array with the specified number of rows and columns.
     * The last sub-array may be smaller if the total number of elements is not a multiple
     * of {@code rows * cols}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] array = {1, 2, 3, 4, 5, 6, 7};
     * float[][][] cube = Arrays.reshape(array, 2, 2); // returns {{{1,2},{3,4}},{{5,6},{7}}}
     * }</pre>
     *
     * @param a the one-dimensional array to reshape.
     * @param rows the number of rows in each two-dimensional sub-array.
     * @param cols the number of columns in each two-dimensional sub-array.
     * @return a new three-dimensional array containing the elements of the input array.
     * @throws IllegalArgumentException if {@code rows} or {@code cols} are not positive.
     */
    public static float[][][] reshape(final float[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new float[0][][];
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
     * Flattens a two-dimensional float array into a new one-dimensional float array.
     * This method concatenates all sub-arrays into a single array.
     * Null or empty sub-arrays are skipped.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] matrix = {{1.0f, 2.0f}, {3.0f, 4.0f}};
     * float[] array = Arrays.flatten(matrix); // returns {1.0f, 2.0f, 3.0f, 4.0f}
     * }</pre>
     *
     * @param a the two-dimensional array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a three-dimensional float array into a new one-dimensional float array.
     * This method concatenates all innermost sub-arrays into a single array.
     * Null or empty sub-arrays at any level are skipped.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] cube = {{{1.0f}, {2.0f}}, {{3.0f}, {4.0f}}};
     * float[] array = Arrays.flatten(cube); // returns {1.0f, 2.0f, 3.0f, 4.0f}
     * }</pre>
     *
     * @param a the three-dimensional array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a two-dimensional array, performs a specified operation on the resulting one-dimensional array,
     * and then copies the modified elements back into the original two-dimensional array in-place.
     * This is useful for applying operations like sorting to the entire set of elements
     * in a multi-dimensional array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] matrix = {{4.0f, 1.0f}, {3.0f, 2.0f}};
     * Arrays.flatOp(matrix, N::sort); // matrix becomes {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * }</pre>
     *
     * @param <E> the type of exception that can be thrown by the operation.
     * @param a the two-dimensional array to operate on.
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
     * Flattens a three-dimensional array, performs a specified operation on the resulting one-dimensional array,
     * and then copies the modified elements back into the original three-dimensional array in-place.
     * This is useful for applying operations like sorting to the entire set of elements
     * in a multi-dimensional array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] cube = {{{4.0f, 1.0f}}, {{3.0f, 2.0f}}};
     * Arrays.flatOp(cube, N::sort); // cube becomes {{{1.0f, 2.0f}}, {{3.0f, 4.0f}}}
     * }</pre>
     *
     * @param <E> the type of exception that can be thrown by the operation.
     * @param a the three-dimensional array to operate on.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] a = {1, 2, 3, 4};
     * float[] b = {5, 6, 7};
     * float[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from both arrays.
     * @return a new array containing the results of applying the zip function to corresponding elements.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] a = {1, 2, 3, 4};
     * float[] b = {5, 6};
     * float[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the floater input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] a = {1, 2, 3, 4};
     * float[] b = {5, 6, 7};
     * float[] c = {8, 9};
     * float[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new array containing the results with length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] a = {1, 2, 3, 4};
     * float[] b = {5, 6};
     * float[] c = {8, 9, 10};
     * float[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param valueForNoneC the default value to use when array 'c' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional float arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] a = {{1, 2}, {3, 4, 5}};
     * float[][] b = {{5, 6, 7}, {8, 9}};
     * float[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new two-dimensional array containing the results of zipping corresponding sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional float arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] a = {{1, 2}, {3, 4, 5}};
     * float[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * float[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the floater input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional float arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] a = {{1, 2}, {3, 4, 5}};
     * float[][] b = {{5, 6, 7}, {8, 9}};
     * float[][] c = {{10, 11}, {12, 13, 14}};
     * float[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new two-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional float arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] a = {{1, 2}, {3, 4, 5}};
     * float[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * float[][] c = {{10, 11}};
     * float[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional float arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * float[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * float[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new three-dimensional array containing the results of zipping corresponding two-dimensional sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional float arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * float[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * float[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the floater input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional float arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] a = {{{1, 2}}};
     * float[][][] b = {{{11, 12}, {13, 14}}};
     * float[][][] c = {{{21, 22}}};
     * float[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new three-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional float arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] a = {{{1, 2}}};
     * float[][][] b = {{{11, 12}, {13, 14}}};
     * float[][][] c = {{{21, 22}}};
     * float[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Calculates the total number of elements in a two-dimensional float array.
     * This method sums the lengths of all sub-arrays.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] matrix = {{1.0f}, {2.0f, 3.0f}, null};
     * long count = Arrays.totalCountOfElements(matrix); // count is 3
     * }</pre>
     *
     * @param a the two-dimensional array to count elements in.
     * @return the total count of elements.
     */
    public static long totalCountOfElements(final float[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        long count = 0;

        for (final float[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of elements in a three-dimensional float array.
     * This method recursively sums the lengths of all innermost sub-arrays.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] cube = {{{1.0f}}, {{2.0f, 3.0f}, null}, null};
     * long count = Arrays.totalCountOfElements(cube); // count is 3
     * }</pre>
     *
     * @param a the three-dimensional array to count elements in.
     * @return the total count of elements.
     */
    public static long totalCountOfElements(final float[][][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

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
     * Finds the minimum length of any sub-array within a two-dimensional float array.
     * Returns 0 for null or empty input array, or if a sub-array is null.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] matrix = {{1.0f, 2.0f}, {3.0f}, null};
     * int minLen = Arrays.minSubArrayLen(matrix); // minLen is 0
     * }</pre>
     *
     * @param a the two-dimensional array to inspect.
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
     * Finds the maximum length of any sub-array within a two-dimensional float array.
     * Returns 0 for null or empty input array. A null sub-array has a length of 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] matrix = {{1.0f}, {2.0f, 3.0f, 4.0f}, null};
     * int maxLen = Arrays.maxSubArrayLen(matrix); // maxLen is 3
     * }</pre>
     *
     * @param a the two-dimensional array to inspect.
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
     * Prints a string representation of a one-dimensional float array to the console and returns the string.
     *
     * <p>This method formats a one-dimensional float array as a readable string representation with standard
     * bracket notation. The method handles null arrays and empty arrays gracefully, providing
     * appropriate string representations for each case.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[element1, element2, element3]"</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] array = {1.1f, 2.2f, 3.3f, 4.4f, 5.5f};
     * String result = Arrays.println(array);
     * // Prints to console and returns: "[1.1, 2.2, 3.3, 4.4, 5.5]"
     *
     * // Null array
     * String nullResult = Arrays.println((float[]) null);
     * // Prints "null"
     *
     * // Empty array
     * float[] empty = new float[0];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the one-dimensional float array to print (can be {@code null}).
     * @return the string representation of the array that was printed to console.
     * @see #println(float[][]) for two-dimensional array printing
     * @see #println(float[][][]) for three-dimensional array printing
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
     * Prints a string representation of a two-dimensional float array to the console and returns the string.
     *
     * <p>This method formats a two-dimensional float array as a readable string representation with proper
     * nested bracket notation. Each sub-array is displayed on its own line for better readability.
     * The method handles null arrays, empty arrays, null sub-arrays, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[element1, element2], [element3, element4]]" (with line separators)</li>
     *   <li>Null sub-array: "null" within the outer brackets</li>
     *   <li>Empty sub-array: "[]" within the outer brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] array = {{1.1f, 2.2f, 3.3f}, {4.4f, 5.5f}, null, {}, {6.6f}};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[1.1, 2.2, 3.3],
     * //  [4.4, 5.5],
     * //  null,
     * //  [],
     * //  [6.6]]
     *
     * // Null array
     * String nullResult = Arrays.println((float[][]) null);
     * // Prints "null"
     *
     * // Empty array
     * float[][] empty = new float[0][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the two-dimensional float array to print (can be {@code null}).
     * @return the string representation of the two-dimensional array that was printed to console.
     * @see #println(float[]) for one-dimensional array printing
     * @see #println(float[][][]) for three-dimensional array printing
     */
    public static String println(final float[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final float[] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(row[j]);
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
     * Prints a string representation of a three-dimensional float array to the console and returns the string.
     *
     * <p>This method formats a three-dimensional float array as a readable string representation with proper
     * nested bracket notation. Each two-dimensional sub-array and its inner one-dimensional arrays are displayed with
     * appropriate line separators and indentation for better readability. The method handles
     * null arrays, empty arrays, null sub-arrays at any level, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[[element1, element2]], [[element3, element4]]]" (with line separators and indentation)</li>
     *   <li>Null two-dimensional sub-array: "null" within the outer brackets</li>
     *   <li>Empty two-dimensional sub-array: "[]" within the outer brackets</li>
     *   <li>Null one-dimensional sub-array: "null" within the two-dimensional sub-array brackets</li>
     *   <li>Empty one-dimensional sub-array: "[]" within the two-dimensional sub-array brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] array = {{{1.1f, 2.2f, 3.3f}, {4.4f, 5.5f}}, {{6.6f, 7.7f}, null, {}}, null};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[[1.1, 2.2, 3.3],
     * //    [4.4, 5.5]],
     * //  [[6.6, 7.7],
     * //   null,
     * //   []],
     * //  null]
     *
     * // Null array
     * String nullResult = Arrays.println((float[][][]) null);
     * // Prints "null"
     *
     * // Empty array
     * float[][][] empty = new float[0][][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use. The formatting includes proper indentation
     * with two spaces for nested levels to enhance readability of complex three-dimensional structures.</p>
     *
     * @param a the three-dimensional float array to print (can be {@code null}).
     * @return the string representation of the three-dimensional array that was printed to console.
     * @see #println(float[]) for one-dimensional array printing
     * @see #println(float[][]) for two-dimensional array printing
     */
    public static String println(final float[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final float[][] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR_UNIX).append("  ");
                            }

                            if (row[j] == null) {
                                sb.append("null");
                            } else if (row[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final float[] subRow = row[j];
                                sb.append('[');

                                for (int k = 0, subRowLen = subRow.length; k < subRowLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(subRow[k]);
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
     * Updates each element of the specified double array by applying a unary operator.
     * Each element is replaced with the result of applying the operator.
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] array = {1.0, -2.0, 3.0};
     * Arrays.updateAll(array, x -> Math.abs(x));
     * // array is now [1.0, 2.0, 3.0]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the array to be updated (can be {@code null}).
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * Updates each element of the specified two-dimensional array in-place by applying a unary operator.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] matrix = {{1.0, 4.0}, {9.0, 16.0}};
     * Arrays.updateAll(matrix, x -> Math.sqrt(x));
     * // matrix is now {{1.0, 2.0}, {3.0, 4.0}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the two-dimensional array to be updated.
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * Updates each element of the specified three-dimensional array in-place by applying a unary operator.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] cube = {{{1.1}, {2.8}}, {{3.3}, {4.6}}};
     * Arrays.updateAll(cube, x -> Math.round(x));
     * // cube is now {{{1.0}, {3.0}}, {{3.0}, {5.0}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operator.
     * @param a the three-dimensional array to be updated.
     * @param operator the unary operator to apply to each element.
     * @throws E if the {@code operator} throws an exception.
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
     * Elements that don't match the predicate remain unchanged.
     * This method modifies the array in-place.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] array = {1.0, -2.0, 3.0, -4.0};
     * Arrays.replaceIf(array, x -> x < 0, 0.0);
     * // array is now [1.0, 0.0, 3.0, 0.0]
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the array to be modified (can be {@code null}).
     * @param predicate a predicate to apply to each element to determine if it should be replaced.
     * @param newValue the value to be placed into the array.
     * @throws E if the {@code predicate} throws an exception.
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
     * Replaces each element of the two-dimensional array with the specified new value if it satisfies the given predicate.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] matrix = {{1.0, -2.0}, {Double.NaN, 4.0}};
     * Arrays.replaceIf(matrix, x -> Double.isNaN(x), 0.0);
     * // matrix is now {{1.0, -2.0}, {0.0, 4.0}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the two-dimensional array to be modified.
     * @param predicate a predicate to apply to each element to determine if it should be replaced.
     * @param newValue the value to be placed into the array.
     * @throws E if the {@code predicate} throws an exception.
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
     * Replaces each element of the three-dimensional array with the specified new value if it satisfies the given predicate.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] cube = {{{1.0}, {Double.POSITIVE_INFINITY}}, {{3.0}, {-4.0}}};
     * Arrays.replaceIf(cube, x -> !Double.isFinite(x), -1.0);
     * // cube is now {{{1.0}, {-1.0}}, {{3.0}, {-4.0}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the predicate.
     * @param a the three-dimensional array to be modified.
     * @param predicate a predicate to apply to each element to determine if it should be replaced.
     * @param newValue the value to be placed into the array.
     * @throws E if the {@code predicate} throws an exception.
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
     * Reshapes a one-dimensional array into a two-dimensional array with the specified number of columns.
     * The number of rows is determined by dividing the total number of elements by the number of columns.
     * The last row may be shorter if the total number of elements is not a multiple of {@code cols}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] array = {1, 2, 3, 4, 5, 6, 7};
     * double[][] matrix = Arrays.reshape(array, 3);
     * // matrix is now {{1, 2, 3}, {4, 5, 6}, {7}}
     * }</pre>
     *
     * @param a the one-dimensional array to reshape.
     * @param cols the number of columns in the new two-dimensional array.
     * @return a new two-dimensional array.
     * @throws IllegalArgumentException if {@code cols} is not positive.
     */
    public static double[][] reshape(final double[] a, final int cols) throws IllegalArgumentException {
        checkMForReshape(cols);

        if (N.isEmpty(a)) {
            return new double[0][];
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
     * Reshapes a one-dimensional array into a three-dimensional array with the specified number of rows and columns.
     * The dimensions of the resulting array are determined based on the total element count.
     * The last sub-arrays may be shorter if the total count is not a multiple of {@code rows * cols}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
     * double[][][] cube = Arrays.reshape(array, 2, 3);
     * // cube is now {{{1, 2, 3}, {4, 5, 6}}, {{7, 8, 9}, {10}}}
     * }</pre>
     *
     * @param a the one-dimensional array to reshape.
     * @param rows the number of rows in each two-dimensional slice.
     * @param cols the number of columns in each two-dimensional slice.
     * @return a new three-dimensional array.
     * @throws IllegalArgumentException if {@code rows} or {@code cols} are not positive.
     */
    public static double[][][] reshape(final double[] a, final int rows, final int cols) throws IllegalArgumentException {
        checkMAndLForReshape(rows, cols);

        if (N.isEmpty(a)) {
            return new double[0][][];
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
     * Flattens a two-dimensional array into a one-dimensional array by concatenating its rows.
     * Null or empty sub-arrays are skipped.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] matrix = {{1.0, 2.0}, {3.0, 4.0}};
     * double[] array = Arrays.flatten(matrix);
     * // array is now {1.0, 2.0, 3.0, 4.0}
     * }</pre>
     *
     * @param a the two-dimensional array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a three-dimensional array into a one-dimensional array by concatenating its elements in order.
     * Null or empty sub-arrays are skipped.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] cube = {{{1.0}, {2.0, 3.0}}, {{4.0}}};
     * double[] array = Arrays.flatten(cube);
     * // array is now {1.0, 2.0, 3.0, 4.0}
     * }</pre>
     *
     * @param a the three-dimensional array to flatten.
     * @return a new one-dimensional array containing all elements from the input array, or an empty array if input is null/empty.
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
     * Flattens a two-dimensional array, performs an operation on the resulting one-dimensional array,
     * and then copies the modified elements back into the original two-dimensional array.
     * This allows for operations like sorting across the entire two-dimensional array.
     * The modification happens in-place on the original two-dimensional array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] matrix = {{5.0, 2.0}, {8.0, 1.0}};
     * Arrays.flatOp(matrix, arr -> Arrays.sort(arr));
     * // matrix is now {{1.0, 2.0}, {5.0, 8.0}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operation.
     * @param a the two-dimensional array to operate on.
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
     * Flattens a three-dimensional array, performs an operation on the resulting one-dimensional array,
     * and then copies the modified elements back into the original three-dimensional array.
     * This allows for operations like sorting across the entire three-dimensional array.
     * The modification happens in-place on the original three-dimensional array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] cube = {{{9.0, 2.0}}, {{5.0}, {1.0}}};
     * Arrays.flatOp(cube, arr -> Arrays.sort(arr));
     * // cube is now {{{1.0, 2.0}}, {{5.0}, {9.0}}}
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the operation.
     * @param a the three-dimensional array to operate on.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] a = {1, 2, 3, 4};
     * double[] b = {5, 6, 7};
     * double[] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {6, 8, 10}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from both arrays.
     * @return a new array containing the results of applying the zip function to corresponding elements.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] a = {1, 2, 3, 4};
     * double[] b = {5, 6};
     * double[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {6, 8, 13, 14} (using 10 for missing b elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the length of the longer input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] a = {1, 2, 3, 4};
     * double[] b = {5, 6, 7};
     * double[] c = {8, 9};
     * double[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {14, 17}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new array containing the results with length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] a = {1, 2, 3, 4};
     * double[] b = {5, 6};
     * double[] c = {8, 9, 10};
     * double[] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {14, 17, 23, 34} (using defaults for missing elements)
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first array (can be {@code null}, treated as empty).
     * @param b the second array (can be {@code null}, treated as empty).
     * @param c the third array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' is shorter.
     * @param valueForNoneB the default value to use when array 'b' is shorter.
     * @param valueForNoneC the default value to use when array 'c' is shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new array with length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional double arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] a = {{1, 2}, {3, 4, 5}};
     * double[][] b = {{5, 6, 7}, {8, 9}};
     * double[][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{6, 8}, {11, 13}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new two-dimensional array containing the results of zipping corresponding sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two two-dimensional double arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] a = {{1, 2}, {3, 4, 5}};
     * double[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * double[][] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
     * // result: {{6, 8, 7}, {11, 13, 15}, {10}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the length of the longer input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional double arrays using the provided zip function.
     * Applies the zip operation to corresponding sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] a = {{1, 2}, {3, 4, 5}};
     * double[][] b = {{5, 6, 7}, {8, 9}};
     * double[][] c = {{10, 11}, {12, 13, 14}};
     * double[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{16, 19}, {23, 26}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new two-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three two-dimensional double arrays using the provided zip function, with default values
     * for missing elements at both the outer and inner array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] a = {{1, 2}, {3, 4, 5}};
     * double[][] b = {{5, 6, 7}, {8, 9}, {10}};
     * double[][] c = {{10, 11}};
     * double[][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{16, 19, 27}, {31, 33, 35}, {30}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first two-dimensional array (can be {@code null}, treated as empty).
     * @param b the second two-dimensional array (can be {@code null}, treated as empty).
     * @param c the third two-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new two-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional double arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shorter outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
     * double[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}};
     * double[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {77, 88}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements in sub-arrays.
     * @return a new three-dimensional array containing the results of zipping corresponding two-dimensional sub-arrays.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from two three-dimensional double arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}}};
     * double[][][] b = {{{10, 20}, {30, 40}}, {{50, 60}, {70, 80}}, {{90}}};
     * double[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
     * // result: {{{11, 22}, {33, 44}}, {{55, 66}, {70, 80}}, {{90}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the length of the longer input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional double arrays using the provided zip function.
     * Applies the zip operation to corresponding two-dimensional sub-arrays, stopping when the shortest outer array is exhausted.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] a = {{{1, 2}}};
     * double[][][] b = {{{11, 12}, {13, 14}}};
     * double[][][] c = {{{21, 22}}};
     * double[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param zipFunction the function to apply to corresponding elements from all three arrays.
     * @return a new three-dimensional array containing the results with outer length equal to the shortest input array.
     * @throws E if the zip function throws an exception.
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
     * Combines elements from three three-dimensional double arrays using the provided zip function, with default values
     * for missing elements at all array levels.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] a = {{{1, 2}}};
     * double[][][] b = {{{11, 12}, {13, 14}}};
     * double[][][] c = {{{21, 22}}};
     * double[][][] result = Arrays.zip(a, b, c, 0, 10, 20, (x, y, z) -> x + y + z);
     * // result: {{{33, 36}, {33, 34}}}}
     * }</pre>
     * 
     * @param <E> the type of exception that the zip function may throw.
     * @param a the first three-dimensional array (can be {@code null}, treated as empty).
     * @param b the second three-dimensional array (can be {@code null}, treated as empty).
     * @param c the third three-dimensional array (can be {@code null}, treated as empty).
     * @param valueForNoneA the default value to use when array 'a' or its sub-arrays are shorter.
     * @param valueForNoneB the default value to use when array 'b' or its sub-arrays are shorter.
     * @param valueForNoneC the default value to use when array 'c' or its sub-arrays are shorter.
     * @param zipFunction the function to apply to corresponding elements.
     * @return a new three-dimensional array with outer length equal to the longest input array.
     * @throws E if the zip function throws an exception.
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
     * Calculates the total number of double elements in a jagged two-dimensional array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] matrix = {{1, 2}, {3, 4, 5}, null, {}};
     * long count = Arrays.totalCountOfElements(matrix);
     * // count is 5
     * }</pre>
     *
     * @param a the two-dimensional array.
     * @return the total count of elements.
     */
    public static long totalCountOfElements(final double[][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

        long count = 0;

        for (final double[] element : a) {
            count += (element == null ? 0 : element.length);
        }

        return count;
    }

    /**
     * Calculates the total number of double elements in a jagged three-dimensional array.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] cube = {{{1}, {2, 3}}, null, {{{4}}}};
     * long count = Arrays.totalCountOfElements(cube);
     * // count is 4
     * }</pre>
     *
     * @param a the three-dimensional array.
     * @return the total count of elements.
     */
    public static long totalCountOfElements(final double[][][] a) {
        if (N.isEmpty(a)) {
            return 0;
        }

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
     * Finds the minimum length of any sub-array in a two-dimensional array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] matrix = {{1, 2, 3}, {4, 5}, null};
     * int minLen = Arrays.minSubArrayLen(matrix);
     * // minLen is 0
     * }</pre>
     *
     * @param a the two-dimensional array.
     * @return the minimum sub-array length, or 0 if the input array is {@code null} or empty.
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
     * Finds the maximum length of any sub-array in a two-dimensional array.
     * A null sub-array is considered to have a length of 0.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] matrix = {{1, 2, 3}, {4, 5}, null};
     * int maxLen = Arrays.maxSubArrayLen(matrix);
     * // maxLen is 3
     * }</pre>
     *
     * @param a the two-dimensional array.
     * @return the maximum sub-array length, or 0 if the input array is {@code null} or empty.
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
     * Prints a string representation of a one-dimensional double array to the console and returns the string.
     *
     * <p>This method formats a one-dimensional double array as a readable string representation with standard
     * bracket notation. The method handles null arrays and empty arrays gracefully, providing
     * appropriate string representations for each case.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[element1, element2, element3]"</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] array = {1.1, 2.2, 3.3, 4.4, 5.5};
     * String result = Arrays.println(array);
     * // Prints to console and returns: "[1.1, 2.2, 3.3, 4.4, 5.5]"
     *
     * // Null array
     * String nullResult = Arrays.println((double[]) null);
     * // Prints "null"
     *
     * // Empty array
     * double[] empty = new double[0];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the one-dimensional double array to print (can be {@code null}).
     * @return the string representation of the array that was printed to console.
     * @see #println(double[][]) for two-dimensional array printing
     * @see #println(double[][][]) for three-dimensional array printing
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
     * Prints a string representation of a two-dimensional double array to the console and returns the string.
     *
     * <p>This method formats a two-dimensional double array as a readable string representation with proper
     * nested bracket notation. Each sub-array is displayed on its own line for better readability.
     * The method handles null arrays, empty arrays, null sub-arrays, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[element1, element2], [element3, element4]]" (with line separators)</li>
     *   <li>Null sub-array: "null" within the outer brackets</li>
     *   <li>Empty sub-array: "[]" within the outer brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] array = {{1.1, 2.2, 3.3}, {4.4, 5.5}, null, {}, {6.6}};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[1.1, 2.2, 3.3],
     * //  [4.4, 5.5],
     * //  null,
     * //  [],
     * //  [6.6]]
     *
     * // Null array
     * String nullResult = Arrays.println((double[][]) null);
     * // Prints "null"
     *
     * // Empty array
     * double[][] empty = new double[0][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use.</p>
     *
     * @param a the two-dimensional double array to print (can be {@code null}).
     * @return the string representation of the two-dimensional array that was printed to console.
     * @see #println(double[]) for one-dimensional array printing
     * @see #println(double[][][]) for three-dimensional array printing
     */
    public static String println(final double[][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final double[] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }

                            sb.append(row[j]);
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
     * Prints a string representation of a three-dimensional double array to the console and returns the string.
     *
     * <p>This method formats a three-dimensional double array as a readable string representation with proper
     * nested bracket notation. Each two-dimensional sub-array and its inner one-dimensional arrays are displayed with
     * appropriate line separators and indentation for better readability. The method handles
     * null arrays, empty arrays, null sub-arrays at any level, and empty sub-arrays gracefully.</p>
     *
     * <p>The output format follows this pattern:</p>
     * <ul>
     *   <li>Null array: "null"</li>
     *   <li>Empty array: "[]"</li>
     *   <li>Non-empty array: "[[[element1, element2]], [[element3, element4]]]" (with line separators and indentation)</li>
     *   <li>Null two-dimensional sub-array: "null" within the outer brackets</li>
     *   <li>Empty two-dimensional sub-array: "[]" within the outer brackets</li>
     *   <li>Null one-dimensional sub-array: "null" within the two-dimensional sub-array brackets</li>
     *   <li>Empty one-dimensional sub-array: "[]" within the two-dimensional sub-array brackets</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] array = {{{1.1, 2.2, 3.3}, {4.4, 5.5}}, {{6.6, 7.7}, null, {}}, null};
     * String result = Arrays.println(array);
     * // Prints to console and returns:
     * // [[[1.1, 2.2, 3.3],
     * //    [4.4, 5.5]],
     * //  [[6.6, 7.7],
     * //   null,
     * //   []],
     * //  null]
     *
     * // Null array
     * String nullResult = Arrays.println((double[][][]) null);
     * // Prints "null"
     *
     * // Empty array
     * double[][][] empty = new double[0][][];
     * String emptyResult = Arrays.println(empty);
     * // Prints "[]"
     * }</pre>
     *
     * <p><b>Note:</b> This method both prints to the console via {@code Arrays.println()} and returns
     * the formatted string for potential further use. The formatting includes proper indentation
     * with two spaces for nested levels to enhance readability of complex three-dimensional structures.</p>
     *
     * @param a the three-dimensional double array to print (can be {@code null}).
     * @return the string representation of the three-dimensional array that was printed to console.
     * @see #println(double[]) for one-dimensional array printing
     * @see #println(double[][]) for two-dimensional array printing
     */
    public static String println(final double[][][] a) {
        if (a == null) {
            return N.println("null");
        } else if (a.length == 0) {
            return N.println("[]");
        } else {
            final StringBuilder sb = Objectory.createStringBuilder();
            final int len = a.length;
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
                        final double[][] row = a[i];
                        sb.append('[');

                        for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                            if (j > 0) {
                                sb.append(',').append(IOUtil.LINE_SEPARATOR_UNIX).append("  ");
                            }

                            if (row[j] == null) {
                                sb.append("null");
                            } else if (row[j].length == 0) {
                                sb.append("[]");
                            } else {
                                final double[] subRow = row[j];
                                sb.append('[');

                                for (int k = 0, subRowLen = subRow.length; k < subRowLen; k++) {
                                    if (k > 0) {
                                        sb.append(", ");
                                    }

                                    sb.append(subRow[k]);
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] bytes = {1, 0, -1, 5};
     * boolean[] bools = Arrays.toBoolean(bytes);
     * // bools is {true, false, false, true}
     * }</pre>
     *
     * @param a the array of bytes to convert.
     * @return a new boolean array, or an empty array if the input is {@code null}.
     */
    public static boolean[] toBoolean(final byte[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_BOOLEAN_ARRAY;
        }

        final int len = N.len(a);
        final boolean[] result = new boolean[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i] > 0;
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of bytes to a two-dimensional array of booleans.
     * Each byte value greater than 0 is converted to true, otherwise false.
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] bytes = {{1, 0}, {-1, 2}};
     * boolean[][] bools = Arrays.toBoolean(bytes);
     * // bools is {{true, false}, {false, true}}
     * }</pre>
     *
     * @param a the two-dimensional array of bytes to convert (can be {@code null}).
     * @return a new two-dimensional boolean array, or an empty array if the input is {@code null}.
     * @see #toBoolean(byte[])
     */
    public static boolean[][] toBoolean(final byte[][] a) {
        if (N.isEmpty(a)) {
            return new boolean[0][];
        }

        final int len = N.len(a);
        final boolean[][] result = new boolean[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toBoolean(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of bytes to a three-dimensional array of booleans.
     * Each byte value greater than 0 is converted to true, otherwise false.
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] bytes = {{{1, 0}, {-1}}, {{2, 0}}};
     * boolean[][][] bools = Arrays.toBoolean(bytes);
     * // bools is {{{true, false}, {false}}, {{true, false}}}
     * }</pre>
     *
     * @param a the three-dimensional array of bytes to convert (can be {@code null}).
     * @return a new three-dimensional boolean array, or an empty array if the input is {@code null}.
     * @see #toBoolean(byte[][])
     */
    public static boolean[][][] toBoolean(final byte[][][] a) {
        if (N.isEmpty(a)) {
            return new boolean[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] ints = {1, 0, -1, 5};
     * boolean[] bools = Arrays.toBoolean(ints);
     * // bools is {true, false, false, true}
     * }</pre>
     *
     * @param a the array of integers to convert.
     * @return a new boolean array, or an empty array if the input is {@code null}.
     */
    public static boolean[] toBoolean(final int[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_BOOLEAN_ARRAY;
        }

        final int len = N.len(a);
        final boolean[] result = new boolean[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i] > 0;
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of integers to a two-dimensional array of booleans.
     * Each integer value greater than 0 is converted to true, otherwise false.
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] ints = {{1, 0}, {-1, 5}};
     * boolean[][] bools = Arrays.toBoolean(ints);
     * // bools is {{true, false}, {false, true}}
     * }</pre>
     *
     * @param a the two-dimensional array of integers to convert (can be {@code null}).
     * @return a new two-dimensional boolean array, or an empty array if the input is {@code null}.
     * @see #toBoolean(int[])
     */
    public static boolean[][] toBoolean(final int[][] a) {
        if (N.isEmpty(a)) {
            return new boolean[0][];
        }

        final int len = N.len(a);
        final boolean[][] result = new boolean[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toBoolean(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of integers to a three-dimensional array of booleans.
     * Each integer value greater than 0 is converted to true, otherwise false.
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] ints = {{{1, 0}, {-1}}, {{5, 0}}};
     * boolean[][][] bools = Arrays.toBoolean(ints);
     * // bools is {{{true, false}, {false}}, {{true, false}}}
     * }</pre>
     *
     * @param a the three-dimensional array of integers to convert (can be {@code null}).
     * @return a new three-dimensional boolean array, or an empty array if the input is {@code null}.
     * @see #toBoolean(int[][])
     */
    public static boolean[][][] toBoolean(final int[][][] a) {
        if (N.isEmpty(a)) {
            return new boolean[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] ints = {65, 66, 67};
     * char[] chars = Arrays.toChar(ints);
     * // chars is {'A', 'B', 'C'}
     * }</pre>
     *
     * @param a the array of integers to convert.
     * @return a new char array, or an empty array if the input is {@code null}.
     */
    public static char[] toChar(final int[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_CHAR_ARRAY;
        }

        final int len = N.len(a);
        final char[] result = new char[len];

        for (int i = 0; i < len; i++) {
            result[i] = (char) a[i];
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of integers to a two-dimensional array of chars by casting.
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] ints = {{65, 66}, {67, 68}};
     * char[][] chars = Arrays.toChar(ints);
     * // chars is {{'A', 'B'}, {'C', 'D'}}
     * }</pre>
     *
     * @param a the two-dimensional array of integers to convert (can be {@code null}).
     * @return a new two-dimensional char array, or an empty array if the input is {@code null}.
     * @see #toChar(int[])
     */
    public static char[][] toChar(final int[][] a) {
        if (N.isEmpty(a)) {
            return new char[0][];
        }

        final int len = N.len(a);
        final char[][] result = new char[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toChar(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of integers to a three-dimensional array of chars by casting.
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] ints = {{{65, 66}, {67}}, {{68, 69}}};
     * char[][][] chars = Arrays.toChar(ints);
     * // chars is {{{'A', 'B'}, {'C'}}, {{'D', 'E'}}}
     * }</pre>
     *
     * @param a the three-dimensional array of integers to convert (can be {@code null}).
     * @return a new three-dimensional char array, or an empty array if the input is {@code null}.
     * @see #toChar(int[][])
     */
    public static char[][][] toChar(final int[][][] a) {
        if (N.isEmpty(a)) {
            return new char[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] bools = {true, false, true};
     * byte[] bytes = Arrays.toByte(bools);
     * // bytes is {1, 0, 1}
     * }</pre>
     *
     * @param a the array of booleans to convert.
     * @return a new byte array, or an empty array if the input is {@code null}.
     */
    public static byte[] toByte(final boolean[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_BYTE_ARRAY;
        }

        final int len = N.len(a);
        final byte[] result = new byte[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i] ? BYTE_1 : BYTE_0;
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of booleans to a two-dimensional array of bytes.
     * True values become 1, false values become 0.
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] bools = {{true, false}, {true, true}};
     * byte[][] bytes = Arrays.toByte(bools);
     * // bytes is {{1, 0}, {1, 1}}
     * }</pre>
     *
     * @param a the two-dimensional array of booleans to convert (can be {@code null}).
     * @return a new two-dimensional byte array, or an empty array if the input is {@code null}.
     * @see #toByte(boolean[])
     */
    public static byte[][] toByte(final boolean[][] a) {
        if (N.isEmpty(a)) {
            return new byte[0][];
        }

        final int len = N.len(a);
        final byte[][] result = new byte[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toByte(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of booleans to a three-dimensional array of bytes.
     * True values become 1, false values become 0.
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] bools = {{{true, false}, {true}}, {{false, false}}};
     * byte[][][] bytes = Arrays.toByte(bools);
     * // bytes is {{{1, 0}, {1}}, {{0, 0}}}
     * }</pre>
     *
     * @param a the three-dimensional array of booleans to convert (can be {@code null}).
     * @return a new three-dimensional byte array, or an empty array if the input is {@code null}.
     * @see #toByte(boolean[][])
     */
    public static byte[][][] toByte(final boolean[][][] a) {
        if (N.isEmpty(a)) {
            return new byte[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] bytes = {10, 20, 30};
     * short[] shorts = Arrays.toShort(bytes);
     * // shorts is {10, 20, 30}
     * }</pre>
     *
     * @param a the array of bytes to convert.
     * @return a new short array, or an empty array if the input is {@code null}.
     */
    public static short[] toShort(final byte[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_SHORT_ARRAY;
        }

        final int len = N.len(a);
        final short[] result = new short[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of bytes to a two-dimensional array of shorts by casting.
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] bytes = {{10, 20}, {30, 40}};
     * short[][] shorts = Arrays.toShort(bytes);
     * // shorts is {{10, 20}, {30, 40}}
     * }</pre>
     *
     * @param a the two-dimensional array of bytes to convert (can be {@code null}).
     * @return a new two-dimensional short array, or an empty array if the input is {@code null}.
     * @see #toShort(byte[])
     */
    public static short[][] toShort(final byte[][] a) {
        if (N.isEmpty(a)) {
            return new short[0][];
        }

        final int len = N.len(a);
        final short[][] result = new short[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toShort(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of bytes to a three-dimensional array of shorts by casting.
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] bytes = {{{10, 20}, {30}}, {{40, 50}}};
     * short[][][] shorts = Arrays.toShort(bytes);
     * // shorts is {{{10, 20}, {30}}, {{40, 50}}}
     * }</pre>
     *
     * @param a the three-dimensional array of bytes to convert (can be {@code null}).
     * @return a new three-dimensional short array, or an empty array if the input is {@code null}.
     * @see #toShort(byte[][])
     */
    public static short[][][] toShort(final byte[][][] a) {
        if (N.isEmpty(a)) {
            return new short[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[] bools = {true, false, true};
     * int[] ints = Arrays.toInt(bools);
     * // ints is {1, 0, 1}
     * }</pre>
     *
     * @param a the array of booleans to convert.
     * @return a new integer array, or an empty array if the input is {@code null}.
     */
    public static int[] toInt(final boolean[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_INT_ARRAY;
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i] ? 1 : 0;
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of booleans to a two-dimensional array of integers.
     * True values become 1, false values become 0.
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][] bools = {{true, false}, {true, true}};
     * int[][] ints = Arrays.toInt(bools);
     * // ints is {{1, 0}, {1, 1}}
     * }</pre>
     *
     * @param a the two-dimensional array of booleans to convert (can be {@code null}).
     * @return a new two-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(boolean[])
     */
    public static int[][] toInt(final boolean[][] a) {
        if (N.isEmpty(a)) {
            return new int[0][];
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of booleans to a three-dimensional array of integers.
     * True values become 1, false values become 0.
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * boolean[][][] bools = {{{true, false}, {true}}, {{false, false}}};
     * int[][][] ints = Arrays.toInt(bools);
     * // ints is {{{1, 0}, {1}}, {{0, 0}}}
     * }</pre>
     *
     * @param a the three-dimensional array of booleans to convert (can be {@code null}).
     * @return a new three-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(boolean[][])
     */
    public static int[][][] toInt(final boolean[][][] a) {
        if (N.isEmpty(a)) {
            return new int[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] chars = {'A', 'B', 'C'};
     * int[] ints = Arrays.toInt(chars);
     * // ints is {65, 66, 67}
     * }</pre>
     *
     * @param a the array of chars to convert.
     * @return a new integer array, or an empty array if the input is {@code null}.
     */
    public static int[] toInt(final char[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_INT_ARRAY;
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of chars to a two-dimensional array of integers by casting.
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][] chars = {{'A', 'B'}, {'C', 'D'}};
     * int[][] ints = Arrays.toInt(chars);
     * // ints is {{65, 66}, {67, 68}}
     * }</pre>
     *
     * @param a the two-dimensional array of chars to convert (can be {@code null}).
     * @return a new two-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(char[])
     */
    public static int[][] toInt(final char[][] a) {
        if (N.isEmpty(a)) {
            return new int[0][];
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of chars to a three-dimensional array of integers by casting.
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[][][] chars = {{{'A', 'B'}, {'C'}}, {{'D', 'E'}}};
     * int[][][] ints = Arrays.toInt(chars);
     * // ints is {{{65, 66}, {67}}, {{68, 69}}}
     * }</pre>
     *
     * @param a the three-dimensional array of chars to convert (can be {@code null}).
     * @return a new three-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(char[][])
     */
    public static int[][][] toInt(final char[][][] a) {
        if (N.isEmpty(a)) {
            return new int[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] bytes = {10, 20, 30};
     * int[] ints = Arrays.toInt(bytes);
     * // ints is {10, 20, 30}
     * }</pre>
     *
     * @param a the array of bytes to convert.
     * @return a new integer array, or an empty array if the input is {@code null}.
     */
    public static int[] toInt(final byte[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_INT_ARRAY;
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of bytes to a two-dimensional array of integers by casting.
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] bytes = {{10, 20}, {30, 40}};
     * int[][] ints = Arrays.toInt(bytes);
     * // ints is {{10, 20}, {30, 40}}
     * }</pre>
     *
     * @param a the two-dimensional array of bytes to convert (can be {@code null}).
     * @return a new two-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(byte[])
     */
    public static int[][] toInt(final byte[][] a) {
        if (N.isEmpty(a)) {
            return new int[0][];
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of bytes to a three-dimensional array of integers by casting.
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] bytes = {{{10, 20}, {30}}, {{40, 50}}};
     * int[][][] ints = Arrays.toInt(bytes);
     * // ints is {{{10, 20}, {30}}, {{40, 50}}}
     * }</pre>
     *
     * @param a the three-dimensional array of bytes to convert (can be {@code null}).
     * @return a new three-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(byte[][])
     */
    public static int[][][] toInt(final byte[][][] a) {
        if (N.isEmpty(a)) {
            return new int[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] shorts = {100, 200, 300};
     * int[] ints = Arrays.toInt(shorts);
     * // ints is {100, 200, 300}
     * }</pre>
     *
     * @param a the array of shorts to convert.
     * @return a new integer array, or an empty array if the input is {@code null}.
     */
    public static int[] toInt(final short[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_INT_ARRAY;
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of shorts to a two-dimensional array of integers by casting.
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] shorts = {{100, 200}, {300, 400}};
     * int[][] ints = Arrays.toInt(shorts);
     * // ints is {{100, 200}, {300, 400}}
     * }</pre>
     *
     * @param a the two-dimensional array of shorts to convert (can be {@code null}).
     * @return a new two-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(short[])
     */
    public static int[][] toInt(final short[][] a) {
        if (N.isEmpty(a)) {
            return new int[0][];
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of shorts to a three-dimensional array of integers by casting.
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] shorts = {{{100, 200}, {300}}, {{400, 500}}};
     * int[][][] ints = Arrays.toInt(shorts);
     * // ints is {{{100, 200}, {300}}, {{400, 500}}}
     * }</pre>
     *
     * @param a the three-dimensional array of shorts to convert (can be {@code null}).
     * @return a new three-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(short[][])
     */
    public static int[][][] toInt(final short[][][] a) {
        if (N.isEmpty(a)) {
            return new int[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] floats = {1.1f, 2.9f, -3.5f};
     * int[] ints = Arrays.toInt(floats);
     * // ints is {1, 2, -3}
     * }</pre>
     *
     * @param a the array of floats to convert.
     * @return a new integer array, or an empty array if the input is {@code null}.
     */
    public static int[] toInt(final float[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_INT_ARRAY;
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = (int) a[i];
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of floats to a two-dimensional array of integers by casting (truncating).
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] floats = {{1.1f, 2.9f}, {-3.5f, 4.2f}};
     * int[][] ints = Arrays.toInt(floats);
     * // ints is {{1, 2}, {-3, 4}}
     * }</pre>
     *
     * @param a the two-dimensional array of floats to convert (can be {@code null}).
     * @return a new two-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(float[])
     */
    public static int[][] toInt(final float[][] a) {
        if (N.isEmpty(a)) {
            return new int[0][];
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of floats to a three-dimensional array of integers by casting (truncating).
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] floats = {{{1.1f, 2.9f}, {3.5f}}, {{-4.2f, 5.8f}}};
     * int[][][] ints = Arrays.toInt(floats);
     * // ints is {{{1, 2}, {3}}, {{-4, 5}}}
     * }</pre>
     *
     * @param a the three-dimensional array of floats to convert (can be {@code null}).
     * @return a new three-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(float[][])
     */
    public static int[][][] toInt(final float[][][] a) {
        if (N.isEmpty(a)) {
            return new int[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] doubles = {1.1, 2.9, -3.5};
     * int[] ints = Arrays.toInt(doubles);
     * // ints is {1, 2, -3}
     * }</pre>
     *
     * @param a the array of doubles to convert.
     * @return a new integer array, or an empty array if the input is {@code null}.
     */
    public static int[] toInt(final double[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_INT_ARRAY;
        }

        final int len = N.len(a);
        final int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = (int) a[i];
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of doubles to a two-dimensional array of integers by casting (truncating).
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] doubles = {{1.1, 2.9}, {-3.5, 4.2}};
     * int[][] ints = Arrays.toInt(doubles);
     * // ints is {{1, 2}, {-3, 4}}
     * }</pre>
     *
     * @param a the two-dimensional array of doubles to convert (can be {@code null}).
     * @return a new two-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(double[])
     */
    public static int[][] toInt(final double[][] a) {
        if (N.isEmpty(a)) {
            return new int[0][];
        }

        final int len = N.len(a);
        final int[][] result = new int[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toInt(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of doubles to a three-dimensional array of integers by casting (truncating).
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] doubles = {{{1.1, 2.9}, {3.5}}, {{-4.2, 5.8}}};
     * int[][][] ints = Arrays.toInt(doubles);
     * // ints is {{{1, 2}, {3}}, {{-4, 5}}}
     * }</pre>
     *
     * @param a the three-dimensional array of doubles to convert (can be {@code null}).
     * @return a new three-dimensional integer array, or an empty array if the input is {@code null}.
     * @see #toInt(double[][])
     */
    public static int[][][] toInt(final double[][][] a) {
        if (N.isEmpty(a)) {
            return new int[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] bytes = {10, 20, 30};
     * long[] longs = Arrays.toLong(bytes);
     * // longs is {10L, 20L, 30L}
     * }</pre>
     *
     * @param a the array of bytes to convert.
     * @return a new long array, or an empty array if the input is {@code null}.
     */
    public static long[] toLong(final byte[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_LONG_ARRAY;
        }

        final int len = N.len(a);
        final long[] result = new long[len];

        for (int i = 0; i < len; i++) {
            result[i] = a[i]; // NOSONAR
        }

        return result;
    }

    /**
     * Converts a two-dimensional array of bytes to a two-dimensional array of longs by casting.
     * The two-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] bytes = {{10, 20}, {30, 40}};
     * long[][] longs = Arrays.toLong(bytes);
     * // longs is {{10L, 20L}, {30L, 40L}}
     * }</pre>
     *
     * @param a the two-dimensional array of bytes to convert (can be {@code null}).
     * @return a new two-dimensional long array, or an empty array if the input is {@code null}.
     * @see #toLong(byte[])
     */
    public static long[][] toLong(final byte[][] a) {
        if (N.isEmpty(a)) {
            return new long[0][];
        }

        final int len = N.len(a);
        final long[][] result = new long[len][];

        for (int i = 0; i < len; i++) {
            result[i] = toLong(a[i]);
        }

        return result;
    }

    /**
     * Converts a three-dimensional array of bytes to a three-dimensional array of longs by casting.
     * The three-dimensional structure is preserved in the conversion.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] bytes = {{{10, 20}, {30}}, {{40, 50}}};
     * long[][][] longs = Arrays.toLong(bytes);
     * // longs is {{{10L, 20L}, {30L}}, {{40L, 50L}}}
     * }</pre>
     *
     * @param a the three-dimensional array of bytes to convert (can be {@code null}).
     * @return a new three-dimensional long array, or an empty array if the input is {@code null}.
     * @see #toLong(byte[][])
     */
    public static long[][][] toLong(final byte[][][] a) {
        if (N.isEmpty(a)) {
            return new long[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] source = {1, 2, 3};
     * long[] result = Arrays.toLong(source);
     * // result is {1L, 2L, 3L}
     * }</pre>
     *
     * @param a the one-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[] toLong(final short[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_LONG_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] source = {{1, 2}, {3, 4}};
     * long[][] result = Arrays.toLong(source);
     * // result is {{1L, 2L}, {3L, 4L}}
     * }</pre>
     *
     * @param a the two-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[][] toLong(final short[][] a) {
        if (N.isEmpty(a)) {
            return new long[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] source = {{{1, 2}}, {{3, 4}}};
     * long[][][] result = Arrays.toLong(source);
     * // result is {{{1L, 2L}}, {{3L, 4L}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[][][] toLong(final short[][][] a) {
        if (N.isEmpty(a)) {
            return new long[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] source = {1, 2, 3};
     * long[] result = Arrays.toLong(source);
     * // result is {1L, 2L, 3L}
     * }</pre>
     *
     * @param a the one-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[] toLong(final int[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_LONG_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] source = {{1, 2}, {3, 4}};
     * long[][] result = Arrays.toLong(source);
     * // result is {{1L, 2L}, {3L, 4L}}
     * }</pre>
     *
     * @param a the two-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[][] toLong(final int[][] a) {
        if (N.isEmpty(a)) {
            return new long[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] source = {{{1, 2}}, {{3, 4}}};
     * long[][][] result = Arrays.toLong(source);
     * // result is {{{1L, 2L}}, {{3L, 4L}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[][][] toLong(final int[][][] a) {
        if (N.isEmpty(a)) {
            return new long[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] source = {1.1f, 2.9f, 3.5f};
     * long[] result = Arrays.toLong(source);
     * // result is {1L, 2L, 3L}
     * }</pre>
     *
     * @param a the one-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[] toLong(final float[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_LONG_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] source = {{1.1f, 2.9f}, {3.5f, 4.0f}};
     * long[][] result = Arrays.toLong(source);
     * // result is {{1L, 2L}, {3L, 4L}}
     * }</pre>
     *
     * @param a the two-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[][] toLong(final float[][] a) {
        if (N.isEmpty(a)) {
            return new long[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] source = {{{1.1f, 2.9f}}, {{3.5f, 4.0f}}};
     * long[][][] result = Arrays.toLong(source);
     * // result is {{{1L, 2L}}, {{3L, 4L}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[][][] toLong(final float[][][] a) {
        if (N.isEmpty(a)) {
            return new long[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] source = {1.1, 2.9, 3.5};
     * long[] result = Arrays.toLong(source);
     * // result is {1L, 2L, 3L}
     * }</pre>
     *
     * @param a the one-dimensional {@code double} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[] toLong(final double[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_LONG_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][] source = {{1.1, 2.9}, {3.5, 4.0}};
     * long[][] result = Arrays.toLong(source);
     * // result is {{1L, 2L}, {3L, 4L}}
     * }</pre>
     *
     * @param a the two-dimensional {@code double} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[][] toLong(final double[][] a) {
        if (N.isEmpty(a)) {
            return new long[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[][][] source = {{{1.1, 2.9}}, {{3.5, 4.0}}};
     * long[][][] result = Arrays.toLong(source);
     * // result is {{{1L, 2L}}, {{3L, 4L}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code double} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code long} array, or an empty array if the input was {@code null}.
     */
    public static long[][][] toLong(final double[][][] a) {
        if (N.isEmpty(a)) {
            return new long[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] source = {1, 2, 3};
     * float[] result = Arrays.toFloat(source);
     * // result is {1.0f, 2.0f, 3.0f}
     * }</pre>
     *
     * @param a the one-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[] toFloat(final byte[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_FLOAT_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] source = {{1, 2}, {3, 4}};
     * float[][] result = Arrays.toFloat(source);
     * // result is {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * }</pre>
     *
     * @param a the two-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[][] toFloat(final byte[][] a) {
        if (N.isEmpty(a)) {
            return new float[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] source = {{{1, 2}}, {{3, 4}}};
     * float[][][] result = Arrays.toFloat(source);
     * // result is {{{1.0f, 2.0f}}, {{3.0f, 4.0f}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[][][] toFloat(final byte[][][] a) {
        if (N.isEmpty(a)) {
            return new float[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] source = {1, 2, 3};
     * float[] result = Arrays.toFloat(source);
     * // result is {1.0f, 2.0f, 3.0f}
     * }</pre>
     *
     * @param a the one-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[] toFloat(final short[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_FLOAT_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] source = {{1, 2}, {3, 4}};
     * float[][] result = Arrays.toFloat(source);
     * // result is {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * }</pre>
     *
     * @param a the two-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[][] toFloat(final short[][] a) {
        if (N.isEmpty(a)) {
            return new float[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] source = {{{1, 2}}, {{3, 4}}};
     * float[][][] result = Arrays.toFloat(source);
     * // result is {{{1.0f, 2.0f}}, {{3.0f, 4.0f}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[][][] toFloat(final short[][][] a) {
        if (N.isEmpty(a)) {
            return new float[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] source = {1, 2, 3};
     * float[] result = Arrays.toFloat(source);
     * // result is {1.0f, 2.0f, 3.0f}
     * }</pre>
     *
     * @param a the one-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[] toFloat(final int[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_FLOAT_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] source = {{1, 2}, {3, 4}};
     * float[][] result = Arrays.toFloat(source);
     * // result is {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * }</pre>
     *
     * @param a the two-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[][] toFloat(final int[][] a) {
        if (N.isEmpty(a)) {
            return new float[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] source = {{{1, 2}}, {{3, 4}}};
     * float[][][] result = Arrays.toFloat(source);
     * // result is {{{1.0f, 2.0f}}, {{3.0f, 4.0f}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[][][] toFloat(final int[][][] a) {
        if (N.isEmpty(a)) {
            return new float[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] source = {1L, 2L, 9007199254740992L};
     * float[] result = Arrays.toFloat(source);
     * // result may be {1.0f, 2.0f, 9.0071992E15f}
     * }</pre>
     *
     * @param a the one-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[] toFloat(final long[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_FLOAT_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] source = {{1L, 2L}, {3L, 4L}};
     * float[][] result = Arrays.toFloat(source);
     * // result is {{1.0f, 2.0f}, {3.0f, 4.0f}}
     * }</pre>
     *
     * @param a the two-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[][] toFloat(final long[][] a) {
        if (N.isEmpty(a)) {
            return new float[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] source = {{{1L, 2L}}, {{3L, 4L}}};
     * float[][][] result = Arrays.toFloat(source);
     * // result is {{{1.0f, 2.0f}}, {{3.0f, 4.0f}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code float} array, or an empty array if the input was {@code null}.
     */
    public static float[][][] toFloat(final long[][][] a) {
        if (N.isEmpty(a)) {
            return new float[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[] source = {1, 2, 3};
     * double[] result = Arrays.toDouble(source);
     * // result is {1.0, 2.0, 3.0}
     * }</pre>
     *
     * @param a the one-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[] toDouble(final byte[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_DOUBLE_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][] source = {{1, 2}, {3, 4}};
     * double[][] result = Arrays.toDouble(source);
     * // result is {{1.0, 2.0}, {3.0, 4.0}}
     * }</pre>
     *
     * @param a the two-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[][] toDouble(final byte[][] a) {
        if (N.isEmpty(a)) {
            return new double[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * byte[][][] source = {{{1, 2}}, {{3, 4}}};
     * double[][][] result = Arrays.toDouble(source);
     * // result is {{{1.0, 2.0}}, {{3.0, 4.0}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code byte} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[][][] toDouble(final byte[][][] a) {
        if (N.isEmpty(a)) {
            return new double[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] source = {1, 2, 3};
     * double[] result = Arrays.toDouble(source);
     * // result is {1.0, 2.0, 3.0}
     * }</pre>
     *
     * @param a the one-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[] toDouble(final short[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_DOUBLE_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][] source = {{1, 2}, {3, 4}};
     * double[][] result = Arrays.toDouble(source);
     * // result is {{1.0, 2.0}, {3.0, 4.0}}
     * }</pre>
     *
     * @param a the two-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[][] toDouble(final short[][] a) {
        if (N.isEmpty(a)) {
            return new double[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[][][] source = {{{1, 2}}, {{3, 4}}};
     * double[][][] result = Arrays.toDouble(source);
     * // result is {{{1.0, 2.0}}, {{3.0, 4.0}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code short} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[][][] toDouble(final short[][][] a) {
        if (N.isEmpty(a)) {
            return new double[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] source = {1, 2, 3};
     * double[] result = Arrays.toDouble(source);
     * // result is {1.0, 2.0, 3.0}
     * }</pre>
     *
     * @param a the one-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[] toDouble(final int[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_DOUBLE_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][] source = {{1, 2}, {3, 4}};
     * double[][] result = Arrays.toDouble(source);
     * // result is {{1.0, 2.0}, {3.0, 4.0}}
     * }</pre>
     *
     * @param a the two-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[][] toDouble(final int[][] a) {
        if (N.isEmpty(a)) {
            return new double[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[][][] source = {{{1, 2}}, {{3, 4}}};
     * double[][][] result = Arrays.toDouble(source);
     * // result is {{{1.0, 2.0}}, {{3.0, 4.0}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code int} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[][][] toDouble(final int[][][] a) {
        if (N.isEmpty(a)) {
            return new double[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] source = {1L, 2L, 9007199254740992L};
     * double[] result = Arrays.toDouble(source);
     * // result is {1.0, 2.0, 9.007199254740992E15}
     * }</pre>
     *
     * @param a the one-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[] toDouble(final long[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_DOUBLE_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][] source = {{1L, 2L}, {3L, 4L}};
     * double[][] result = Arrays.toDouble(source);
     * // result is {{1.0, 2.0}, {3.0, 4.0}}
     * }</pre>
     *
     * @param a the two-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[][] toDouble(final long[][] a) {
        if (N.isEmpty(a)) {
            return new double[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[][][] source = {{{1L, 2L}}, {{3L, 4L}}};
     * double[][][] result = Arrays.toDouble(source);
     * // result is {{{1.0, 2.0}}, {{3.0, 4.0}}}
     * }</pre>
     *
     * @param a the three-dimensional {@code long} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[][][] toDouble(final long[][][] a) {
        if (N.isEmpty(a)) {
            return new double[0][][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] source = {1.1f, 2.2f, 3.3f};
     * double[] result = Arrays.toDouble(source);
     * // result is approximately {1.100000023841858, 2.200000047683716, 3.299999952316284}
     * }</pre>
     *
     * @param a the one-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new one-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[] toDouble(final float[] a) {
        if (N.isEmpty(a)) {
            return N.EMPTY_DOUBLE_ARRAY;
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][] source = {{1.1f, 2.2f}, {3.3f, 4.4f}};
     * double[][] result = Arrays.toDouble(source);
     * // result is a two-dimensional double array with converted values.
     * }</pre>
     *
     * @param a the two-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new two-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[][] toDouble(final float[][] a) {
        if (N.isEmpty(a)) {
            return new double[0][];
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[][][] source = {{{1.1f, 2.2f}}, {{3.3f, 4.4f}}};
     * double[][][] result = Arrays.toDouble(source);
     * // result is a three-dimensional double array with converted values.
     * }</pre>
     *
     * @param a the three-dimensional {@code float} array to convert. Can be {@code null}.
     * @return a new three-dimensional {@code double} array, or an empty array if the input was {@code null}.
     */
    public static double[][][] toDouble(final float[][][] a) {
        if (N.isEmpty(a)) {
            return new double[0][][];
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
     * A utility class that extends {@code com.landawn.abacus.util.Arrays} providing additional
     * mapping methods for object arrays. This class is marked with {@code SuppressFBWarnings} for its naming convention.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * String[] strings = {"1", "2", "3"};
     * Integer[] ints = Arrays.f.map(strings, Integer::valueOf, Integer.class);
     * // Result: [1, 2, 3]
     * }</pre>
     */
    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
    public static final class f extends com.landawn.abacus.util.Arrays { // NOSONAR
        private f() {
            // utility class
        }

        /**
         * Maps each element of the input array to a new type using the provided mapping function.
         * This is a convenience method for transforming object arrays.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[] strings = {"1", "2", "3"};
         * Integer[] ints = Arrays.f.map(strings, Integer::valueOf, Integer.class);
         * // Result: [1, 2, 3]
         * }</pre>
         *
         * @param <T> the type of elements in the input array.
         * @param <R> the type of elements in the result array.
         * @param <E> the type of exception that the mapping function may throw.
         * @param a the input array to map (can be {@code null}).
         * @param func the mapping function to apply to each element.
         * @param targetElementType the class of the target element type.
         * @return a new array containing the mapped elements, or an empty array if input is {@code null}.
         * @throws E if the mapping function throws an exception.
         */
        public static <T, R, E extends Exception> R[] map(final T[] a, final Throwables.Function<? super T, ? extends R, E> func,
                final Class<R> targetElementType) throws E {
            if (N.isEmpty(a)) {
                return N.newArray(targetElementType, 0);
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
         * This is a convenience method for transforming object arrays to boolean arrays.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[] words = {"hello", "hi", "world"};
         * boolean[] longWords = Arrays.f.mapToBoolean(words, s -> s.length() > 3);
         * // Result: [true, false, true]
         * }</pre>
         *
         * @param <T> the type of elements in the input array.
         * @param <E> the type of exception that the mapping function may throw.
         * @param a the input array to map (can be {@code null}).
         * @param func the function that maps each element to a boolean.
         * @return a boolean array containing the mapped values, or an empty array if input is {@code null}.
         * @throws E if the mapping function throws an exception.
         */
        public static <T, E extends Exception> boolean[] mapToBoolean(final T[] a, final Throwables.ToBooleanFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return N.EMPTY_BOOLEAN_ARRAY;
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
         * This is a convenience method for transforming object arrays to char arrays.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[] words = {"apple", "banana", "cherry"};
         * char[] firstChars = Arrays.f.mapToChar(words, s -> s.charAt(0));
         * // Result: ['a', 'b', 'c']
         * }</pre>
         *
         * @param <T> the type of elements in the input array.
         * @param <E> the type of exception that the mapping function may throw.
         * @param a the input array to map (can be {@code null}).
         * @param func the function that maps each element to a char.
         * @return a char array containing the mapped values, or an empty array if input is {@code null}.
         * @throws E if the mapping function throws an exception.
         */
        public static <T, E extends Exception> char[] mapToChar(final T[] a, final Throwables.ToCharFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return N.EMPTY_CHAR_ARRAY;
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
         * This is a convenience method for transforming object arrays to byte arrays.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[] numbers = {"10", "20", "30"};
         * byte[] bytes = Arrays.f.mapToByte(numbers, Byte::parseByte);
         * // Result: [10, 20, 30]
         * }</pre>
         *
         * @param <T> the type of elements in the input array.
         * @param <E> the type of exception that the mapping function may throw.
         * @param a the input array to map (can be {@code null}).
         * @param func the function that maps each element to a byte.
         * @return a byte array containing the mapped values, or an empty array if input is {@code null}.
         * @throws E if the mapping function throws an exception.
         */
        public static <T, E extends Exception> byte[] mapToByte(final T[] a, final Throwables.ToByteFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return N.EMPTY_BYTE_ARRAY;
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
         * This is a convenience method for transforming object arrays to short arrays.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[] numbers = {"100", "200", "300"};
         * short[] shorts = Arrays.f.mapToShort(numbers, Short::parseShort);
         * // Result: [100, 200, 300]
         * }</pre>
         *
         * @param <T> the type of elements in the input array.
         * @param <E> the type of exception that the mapping function may throw.
         * @param a the input array to map (can be {@code null}).
         * @param func the function that maps each element to a short.
         * @return a short array containing the mapped values, or an empty array if input is {@code null}.
         * @throws E if the mapping function throws an exception.
         */
        public static <T, E extends Exception> short[] mapToShort(final T[] a, final Throwables.ToShortFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return N.EMPTY_SHORT_ARRAY;
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
         * This method transforms an object array into a primitive int array by applying
         * the provided function to each element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[] numbers = {"10", "20", "30"};
         * int[] ints = Arrays.mapToInt(numbers, Integer::parseInt);
         * // Result: [10, 20, 30]
         * }</pre>
         *
         * @param <T> the type of elements in the input array.
         * @param <E> the type of exception that the mapping function may throw.
         * @param a the input array to map.
         * @param func the function that maps each element to an int.
         * @return an int array containing the mapped values, or an empty array if input is {@code null}.
         * @throws E if the mapping function throws an exception.
         */
        public static <T, E extends Exception> int[] mapToInt(final T[] a, final Throwables.ToIntFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return N.EMPTY_INT_ARRAY;
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
         * This method transforms an object array into a primitive long array by applying
         * the provided function to each element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[] numbers = {"1000", "2000", "3000"};
         * long[] longs = Arrays.mapToLong(numbers, Long::parseLong);
         * // Result: [1000L, 2000L, 3000L]
         * }</pre>
         *
         * @param <T> the type of elements in the input array.
         * @param <E> the type of exception that the mapping function may throw.
         * @param a the input array to map.
         * @param func the function that maps each element to a long.
         * @return a long array containing the mapped values, or an empty array if input is {@code null}.
         * @throws E if the mapping function throws an exception.
         */
        public static <T, E extends Exception> long[] mapToLong(final T[] a, final Throwables.ToLongFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return N.EMPTY_LONG_ARRAY;
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
         * This method transforms an object array into a primitive float array by applying
         * the provided function to each element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[] numbers = {"1.5", "2.5", "3.5"};
         * float[] floats = Arrays.mapToFloat(numbers, Float::parseFloat);
         * // Result: [1.5f, 2.5f, 3.5f]
         * }</pre>
         *
         * @param <T> the type of elements in the input array.
         * @param <E> the type of exception that the mapping function may throw.
         * @param a the input array to map.
         * @param func the function that maps each element to a float.
         * @return a float array containing the mapped values, or an empty array if input is {@code null}.
         * @throws E if the mapping function throws an exception.
         */
        public static <T, E extends Exception> float[] mapToFloat(final T[] a, final Throwables.ToFloatFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return N.EMPTY_FLOAT_ARRAY;
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
         * This method transforms an object array into a primitive double array by applying
         * the provided function to each element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[] numbers = {"1.5", "2.5", "3.5"};
         * double[] doubles = Arrays.mapToDouble(numbers, Double::parseDouble);
         * // Result: [1.5, 2.5, 3.5]
         * }</pre>
         *
         * @param <T> the type of elements in the input array.
         * @param <E> the type of exception that the mapping function may throw.
         * @param a the input array to map.
         * @param func the function that maps each element to a double.
         * @return a double array containing the mapped values, or an empty array if input is {@code null}.
         * @throws E if the mapping function throws an exception.
         */
        public static <T, E extends Exception> double[] mapToDouble(final T[] a, final Throwables.ToDoubleFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return N.EMPTY_DOUBLE_ARRAY;
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
     * This class offers a comprehensive set of methods for manipulating two-dimensional arrays including
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
     * @since 1.0
     */
    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
    public static final class ff { // NOSONAR

        /**
         * Private constructor to prevent instantiation of this utility class.
         * 
         * @throws AssertionError if called.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][] array = {{"hello", "world"}, {"foo", "bar"}};
         * ff.updateAll(array, str -> str.toUpperCase());
         * // array is now {{"HELLO", "WORLD"}, {"FOO", "BAR"}}
         * }</pre>
         *
         * @param <T> the type of elements in the array.
         * @param <E> the type of exception that may be thrown by the operator.
         * @param a the two-dimensional array to update (can be {@code null}).
         * @param operator the unary operator to apply to each element.
         * @throws E if the {@code operator} throws an exception during processing.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] array = {{1, null, 3}, {null, 5, 6}};
         * ff.replaceIf(array, val -> val == null, 0);
         * // array is now {{1, 0, 3}, {0, 5, 6}}
         * }</pre>
         *
         * @param <T> the type of elements in the array.
         * @param <E> the type of exception that may be thrown by the predicate.
         * @param a the two-dimensional array to modify (can be {@code null}).
         * @param predicate the condition to test each element against.
         * @param newValue the value to replace matching elements with (can be {@code null}).
         * @throws E if the {@code predicate} throws an exception during testing.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[] array = {1, 2, 3, 4, 5, 6, 7};
         * Integer[][] reshaped = ff.reshape(array, 3);
         * // reshaped is {{1, 2, 3}, {4, 5, 6}, {7}}
         * }</pre>
         *
         * @param <T> the component type of the array.
         * @param a the one-dimensional array to reshape.
         * @param cols the number of columns in each row.
         * @return a new two-dimensional array with the specified column structure.
         * @throws IllegalArgumentException if {@code cols} is not positive ({@code cols <= 0}).
         */
        public static <T> T[][] reshape(final T[] a, final int cols) throws IllegalArgumentException {
            checkMForReshape(cols);

            //        if (N.isEmpty(a)) {
            //            return new T[0][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] array = {{1, 2}, {3, 4, 5}, {6}};
         * Integer[] flattened = ff.flatten(array);
         * // flattened is {1, 2, 3, 4, 5, 6}
         * }</pre>
         *
         * @param <T> the component type of the array elements.
         * @param a the two-dimensional array to flatten.
         * @return a new one-dimensional array containing all elements from the input array.
         * @throws IllegalArgumentException if the input array is {@code null}.
         */
        public static <T> T[] flatten(final T[][] a) throws IllegalArgumentException {
            N.checkArgNotNull(a, "The input array must not be null");

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
         * as a single sequence, such as sorting all elements across the entire two-dimensional array.</p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] array = {{3, 1, 4}, {1, 5, 9}};
         * ff.flatOp(array, arr -> java.util.Arrays.sort(arr));
         * // array is now {{1, 1, 3}, {4, 5, 9}}
         * }</pre>
         *
         * @param <T> the type of elements in the array.
         * @param <E> the type of exception that may be thrown by the operation.
         * @param a the two-dimensional array to operate on (can be {@code null}). The operation modifies this array in-place.
         * @param op the consumer that operates on the flattened array.
         * @throws E if the operation throws an exception.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] array = {{1, 2}, {3, 4}};
         * Integer[][] doubled = ff.map(array, x -> x * 2);
         * // doubled is {{2, 4}, {6, 8}}
         * }</pre>
         *
         * @param <T> the type of elements in both source and result arrays.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source two-dimensional array (can be {@code null}).
         * @param func the unary operator to apply to each element.
         * @return a new two-dimensional array with mapped elements, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception during mapping.
         */
        public static <T, E extends Exception> T[][] map(final T[][] a, final Throwables.UnaryOperator<T, E> func) throws E {
            return map(a, func, (Class<T>) a.getClass().getComponentType().getComponentType());
        }

        /**
         * Maps each element of a two-dimensional array to a new type using the provided function.
         * This method allows transformation between different element types.
         * 
         * <p>The resulting array has the same structure as the input array but with elements
         * of the target type.</p>
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][] array = {{"1", "2"}, {"3", "4"}};
         * Integer[][] numbers = ff.map(array, Integer::parseInt, Integer.class);
         * // numbers is {{1, 2}, {3, 4}}
         * }</pre>
         * 
         * @param <T> the type of elements in the source array.
         * @param <R> the type of elements in the result array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source two-dimensional array (can be {@code null}).
         * @param func the function to transform each element.
         * @param targetElementType the class of the target element type.
         * @return a new two-dimensional array with transformed elements, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception during mapping.
         */
        public static <T, R, E extends Exception> R[][] map(final T[][] a, final Throwables.Function<? super T, ? extends R, E> func,
                final Class<R> targetElementType) throws E {
            if (N.isEmpty(a)) {
                return N.newArray(N.newArray(targetElementType, 0).getClass(), 0); // NOSONAR
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] array = {{1, 2, 3}, {4, 5, 6}};
         * boolean[][] evens = ff.mapToBoolean(array, x -> x % 2 == 0);
         * // evens is {{false, true, false}, {true, false, true}}
         * }</pre>
         * 
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source two-dimensional array (can be {@code null}).
         * @param func the predicate function to test each element.
         * @return a new two-dimensional boolean array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception during mapping.
         */
        public static <T, E extends Exception> boolean[][] mapToBoolean(final T[][] a, final Throwables.ToBooleanFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new boolean[0][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][] array = {{"apple", "banana"}, {"cat", "dog"}};
         * char[][] firstChars = ff.mapToChar(array, s -> s.charAt(0));
         * // firstChars is {{'a', 'b'}, {'c', 'd'}}
         * }</pre>
         * 
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source two-dimensional array (can be {@code null}).
         * @param func the function to extract a char from each element.
         * @return a new two-dimensional char array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception during mapping.
         */
        public static <T, E extends Exception> char[][] mapToChar(final T[][] a, final Throwables.ToCharFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new char[0][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] array = {{10, 20}, {30, 40}};
         * byte[][] bytes = ff.mapToByte(array, Integer::byteValue);
         * // bytes is {{10, 20}, {30, 40}}
         * }</pre>
         * 
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source two-dimensional array (can be {@code null}).
         * @param func the function to convert each element to a byte.
         * @return a new two-dimensional byte array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception during mapping.
         */
        public static <T, E extends Exception> byte[][] mapToByte(final T[][] a, final Throwables.ToByteFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new byte[0][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] array = {{100, 200}, {300, 400}};
         * short[][] shorts = ff.mapToShort(array, Integer::shortValue);
         * // shorts is {{100, 200}, {300, 400}}
         * }</pre>
         * 
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source two-dimensional array (can be {@code null}).
         * @param func the function to convert each element to a short.
         * @return a new two-dimensional short array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception during mapping.
         */
        public static <T, E extends Exception> short[][] mapToShort(final T[][] a, final Throwables.ToShortFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new short[0][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][] array = {{"10", "20"}, {"30", "40"}};
         * int[][] numbers = ff.mapToInt(array, Integer::parseInt);
         * // numbers is {{10, 20}, {30, 40}}
         * }</pre>
         * 
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source two-dimensional array (can be {@code null}).
         * @param func the function to convert each element to an int.
         * @return a new two-dimensional int array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception during mapping.
         */
        public static <T, E extends Exception> int[][] mapToInt(final T[][] a, final Throwables.ToIntFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new int[0][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][] array = {{"1000000", "2000000"}, {"3000000", "4000000"}};
         * long[][] longs = ff.mapToLong(array, Long::parseLong);
         * // longs is {{1000000L, 2000000L}, {3000000L, 4000000L}}
         * }</pre>
         * 
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source two-dimensional array (can be {@code null}).
         * @param func the function to convert each element to a long.
         * @return a new two-dimensional long array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception during mapping.
         */
        public static <T, E extends Exception> long[][] mapToLong(final T[][] a, final Throwables.ToLongFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new long[0][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][] array = {{"1.5", "2.5"}, {"3.5", "4.5"}};
         * float[][] floats = ff.mapToFloat(array, Float::parseFloat);
         * // floats is {{1.5f, 2.5f}, {3.5f, 4.5f}}
         * }</pre>
         * 
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source two-dimensional array (can be {@code null}).
         * @param func the function to convert each element to a float.
         * @return a new two-dimensional float array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception during mapping.
         */
        public static <T, E extends Exception> float[][] mapToFloat(final T[][] a, final Throwables.ToFloatFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new float[0][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][] array = {{"1.234", "2.345"}, {"3.456", "4.567"}};
         * double[][] doubles = ff.mapToDouble(array, Double::parseDouble);
         * // doubles is {{1.234, 2.345}, {3.456, 4.567}}
         * }</pre>
         * 
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source two-dimensional array (can be {@code null}).
         * @param func the function to convert each element to a double.
         * @return a new two-dimensional double array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception during mapping.
         */
        public static <T, E extends Exception> double[][] mapToDouble(final T[][] a, final Throwables.ToDoubleFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new double[0][];
            }

            final int len = N.len(a);
            final double[][] c = new double[len][];

            for (int i = 0; i < len; i++) {
                c[i] = com.landawn.abacus.util.Arrays.f.mapToDouble(a[i], func);
            }

            return c;
        }

        /**
         * Combines corresponding elements from two two-dimensional arrays using a binary function.
         * The result has dimensions equal to the minimum dimensions of the input arrays.
         * 
         * <p>This method pairs elements at the same positions and applies the zip function.
         * If arrays have different dimensions, extra elements are ignored.</p>
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] a = {{1, 2}, {3, 4}};
         * Integer[][] b = {{10, 20}, {30, 40}};
         * Integer[][] sums = ff.zip(a, b, (x, y) -> x + y);
         * // sums is {{11, 22}, {33, 44}}
         * }</pre>
         * 
         * @param <A> the element type of the first array and the result.
         * @param <B> the element type of the second array.
         * @param <E> the type of exception that may be thrown by the zip function.
         * @param a the first two-dimensional array.
         * @param b the second two-dimensional array.
         * @param zipFunction the function to combine paired elements.
         * @return a new two-dimensional array containing the combined elements.
         * @throws E if the zip function throws an exception.
         */
        public static <A, B, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction)
                throws E {
            return zip(a, b, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
        }

        /**
         * Combines corresponding elements from two two-dimensional arrays into a new array of a specified type.
         * This method allows the result type to differ from the input types.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] a = {{1, 2}, {3, 4}};
         * String[][] b = {{"a", "b"}, {"c", "d"}};
         * String[][] combined = ff.zip(a, b, (i, s) -> i + s, String.class);
         * // combined is {{"1a", "2b"}, {"3c", "4d"}}
         * }</pre>
         * 
         * @param <A> the element type of the first array.
         * @param <B> the element type of the second array.
         * @param <R> the element type of the result array.
         * @param <E> the type of exception that may be thrown by the zip function.
         * @param a the first two-dimensional array.
         * @param b the second two-dimensional array.
         * @param zipFunction the function to combine paired elements.
         * @param targetElementType the class of the result element type.
         * @return a new two-dimensional array of the specified type containing combined elements.
         * @throws E if the zip function throws an exception.
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
         * Combines elements from two two-dimensional arrays with default values for missing elements.
         * The result has dimensions equal to the maximum dimensions of the input arrays.
         * 
         * <p>When one array is shorter than the other, the default values are used
         * for the missing elements during combination.</p>
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] a = {{1, 2}, {3}};
         * Integer[][] b = {{10}, {30, 40}};
         * Integer[][] sums = ff.zip(a, b, 0, 0, (x, y) -> x + y);
         * // sums is {{11, 2}, {33, 40}}
         * }</pre>
         * 
         * @param <A> the element type of the first array and the result.
         * @param <B> the element type of the second array.
         * @param <E> the type of exception that may be thrown by the zip function.
         * @param a the first two-dimensional array. can be {@code null}.
         * @param b the second two-dimensional array. can be {@code null}.
         * @param valueForNoneA default value used when first array has no element at a position.
         * @param valueForNoneB default value used when second array has no element at a position.
         * @param zipFunction the function to combine elements.
         * @return a new two-dimensional array with combined elements using defaults where needed.
         * @throws E if the zip function throws an exception.
         */
        public static <A, B, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final A valueForNoneA, final B valueForNoneB,
                final Throwables.BiFunction<? super A, ? super B, A, E> zipFunction) throws E {
            return zip(a, b, valueForNoneA, valueForNoneB, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
        }

        /**
         * Combines elements from two two-dimensional arrays into a result array of a specified type,
         * using default values for missing elements.
         * 
         * <p>This is the most flexible zip operation, allowing different input and output types
         * while handling arrays of different sizes gracefully.</p>
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] a = {{1}, {2, 3}};
         * String[][] b = {{"X", "Y"}, {"Z"}};
         * String[][] result = ff.zip(a, b, 0, "-", (i, s) -> i + s, String.class);
         * // result is {{"1X", "0Y"}, {"2Z", "3-"}}
         * }</pre>
         * 
         * @param <A> the element type of the first array.
         * @param <B> the element type of the second array.
         * @param <R> the element type of the result array.
         * @param <E> the type of exception that may be thrown by the zip function.
         * @param a the first two-dimensional array. can be {@code null}.
         * @param b the second two-dimensional array. can be {@code null}.
         * @param valueForNoneA default value for the first array.
         * @param valueForNoneB default value for the second array.
         * @param zipFunction the function to combine elements.
         * @param targetElementType the class of the result element type.
         * @return a new two-dimensional array of the specified type with combined elements.
         * @throws E if the zip function throws an exception.
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
         * Combines corresponding elements from three two-dimensional arrays using a tri-function.
         * The result has dimensions equal to the minimum dimensions of all input arrays.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] a = {{1, 2}, {3, 4}};
         * Integer[][] b = {{10, 20}, {30, 40}};
         * Integer[][] c = {{100, 200}, {300, 400}};
         * Integer[][] sums = ff.zip(a, b, c, (x, y, z) -> x + y + z);
         * // sums is {{111, 222}, {333, 444}}
         * }</pre>
         * 
         * @param <A> the element type of all arrays and the result.
         * @param <B> the element type of the second array.
         * @param <C> the element type of the third array.
         * @param <E> the type of exception that may be thrown.
         * @param a the first two-dimensional array.
         * @param b the second two-dimensional array.
         * @param c the third two-dimensional array.
         * @param zipFunction the function to combine three elements.
         * @return a new two-dimensional array containing the combined elements.
         * @throws E if the zip function throws an exception.
         */
        public static <A, B, C, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final C[][] c,
                final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
            return zip(a, b, c, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
        }

        /**
         * Combines elements from three two-dimensional arrays into a new array of a specified type.
         * This method allows flexible type transformation when combining three arrays.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] a = {{1}, {2}};
         * Double[][] b = {{1.5, 2.5}, {3.5}};
         * String[][] c = {{"A"}, {"B", "C"}};
         * String[][] result = ff.zip(a, b, c, (i, d, s) -> i + ":" + d + ":" + s, String.class);
         * // result is {{"1:1.5:A"}}
         * }</pre>
         * 
         * @param <A> the element type of the first array.
         * @param <B> the element type of the second array.
         * @param <C> the element type of the third array.
         * @param <R> the element type of the result array.
         * @param <E> the type of exception that may be thrown.
         * @param a the first two-dimensional array.
         * @param b the second two-dimensional array.
         * @param c the third two-dimensional array.
         * @param zipFunction the function to combine three elements.
         * @param targetElementType the class of the result element type.
         * @return a new two-dimensional array of the specified type.
         * @throws E if the zip function throws an exception.
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
         * Combines elements from three two-dimensional arrays with default values for missing elements.
         * The result has dimensions equal to the maximum dimensions of all input arrays.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] a = {{1}, {2, 3}};
         * Integer[][] b = {{10, 20}};
         * Integer[][] c = {{100}, {200, 300}};
         * Integer[][] sums = ff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
         * // sums is {{111, 20}, {202, 303}}
         * }</pre>
         * 
         * @param <A> the element type of all arrays and the result.
         * @param <B> the element type of the second array.
         * @param <C> the element type of the third array.
         * @param <E> the type of exception that may be thrown.
         * @param a the first two-dimensional array. can be {@code null}.
         * @param b the second two-dimensional array. can be {@code null}.
         * @param c the third two-dimensional array. can be {@code null}.
         * @param valueForNoneA default value for the first array.
         * @param valueForNoneB default value for the second array.
         * @param valueForNoneC default value for the third array.
         * @param zipFunction the function to combine three elements.
         * @return a new two-dimensional array with combined elements using defaults where needed.
         * @throws E if the zip function throws an exception.
         */
        public static <A, B, C, E extends Exception> A[][] zip(final A[][] a, final B[][] b, final C[][] c, final A valueForNoneA, final B valueForNoneB,
                final C valueForNoneC, final Throwables.TriFunction<? super A, ? super B, ? super C, A, E> zipFunction) throws E {
            return zip(a, b, c, valueForNoneA, valueForNoneB, valueForNoneC, zipFunction, (Class<A>) a.getClass().getComponentType().getComponentType());
        }

        /**
         * Combines elements from three two-dimensional arrays into a result array of a specified type,
         * using default values for missing elements. This is the most flexible three-array zip operation.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][] a = {{1}};
         * String[][] b = {{"X", "Y"}};
         * Double[][] c = {{0.5}, {1.5, 2.5}};
         * String[][] result = ff.zip(a, b, c, 0, "-", 0.0, 
         *                           (i, s, d) -> i + s + d, String.class);
         * // result is {{"1X0.5", "0Y0.0"}, {"0-1.5", "0-2.5"}}
         * }</pre>
         * 
         * @param <A> the element type of the first array.
         * @param <B> the element type of the second array.
         * @param <C> the element type of the third array.
         * @param <R> the element type of the result array.
         * @param <E> the type of exception that may be thrown.
         * @param a the first two-dimensional array. can be {@code null}.
         * @param b the second two-dimensional array. can be {@code null}.
         * @param c the third two-dimensional array. can be {@code null}.
         * @param valueForNoneA default value for the first array.
         * @param valueForNoneB default value for the second array.
         * @param valueForNoneC default value for the third array.
         * @param zipFunction the function to combine three elements.
         * @param targetElementType the class of the result element type.
         * @return a new two-dimensional array of the specified type with combined elements.
         * @throws E if the zip function throws an exception.
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
         * A private helper method to zip elements of two one-dimensional arrays into a result array.
         * @param a first array.
         * @param b second array.
         * @param valueForNoneA default value for first array.
         * @param valueForNoneB default value for second array.
         * @param zipFunction function to combine elements.
         * @param targetElementType class of the result element type.
         *
         * @param <A> type of first array element.
         * @param <B> type of second array element.
         * @param <R> type of result array element.
         * @param <E> type of exception.
         * @return a new array of zipped elements.
         * @throws E if the zip function fails.
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
         * A private helper method to zip elements of three one-dimensional arrays into a result array.
         *
         * @param <A> type of first array element.
         * @param <B> type of second array element.
         * @param <C> type of third array element.
         * @param <R> type of result array element.
         * @param <E> type of exception.
         * @param a first array.
         * @param b second array.
         * @param c third array.
         * @param valueForNoneA default value for first array.
         * @param valueForNoneB default value for second array.
         * @param valueForNoneC default value for third array.
         * @param zipFunction function to combine elements.
         * @param targetElementType class of the result element type.
         * @return a new array of zipped elements.
         * @throws E if the zip function fails.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Object[][] array = {{1, 2, 3}, {4, 5}, null, {6}};
         * long total = ff.totalCountOfElements(array);
         * // total is 6 (3 + 2 + 0 + 1)
         * }</pre>
         * 
         * @param a the two-dimensional array to count elements in. can be {@code null}.
         * @return the total number of elements across all sub-arrays, or 0 if the array is {@code null}.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Object[][] array = {{1, 2, 3}, {4, 5}, null, {6}};
         * int minLen = ff.minSubArrayLen(array);
         * // minLen is 0 (due to the null sub-array)
         * }</pre>
         * 
         * @param a the two-dimensional array to examine. can be {@code null}.
         * @return the minimum sub-array length, or 0 if the array is {@code null} or empty.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Object[][] array = {{1, 2, 3}, {4, 5}, null, {6}};
         * int maxLen = ff.maxSubArrayLen(array);
         * // maxLen is 3 (from the first sub-array)
         * }</pre>
         * 
         * @param a the two-dimensional array to examine. can be {@code null}.
         * @return the maximum sub-array length, or 0 if the array is {@code null} or empty.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][] array = {{"a", "b"}, {"c", "d", "e"}};
         * String result = ff.println(array);
         * // Prints:
         * // [[a, b],
         * //  [c, d, e]]
         * // Returns the same string
         * }</pre>
         * 
         * @param a the two-dimensional array to print. can be {@code null}.
         * @return the string representation that was printed.
         */
        static String println(final Object[][] a) {
            if (a == null) {
                return N.println("null");
            } else if (a.length == 0) {
                return N.println("[]");
            } else {
                final StringBuilder sb = Objectory.createStringBuilder();
                final int len = a.length;
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
                            final Object[] row = a[i];
                            sb.append('[');

                            for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                                if (j > 0) {
                                    sb.append(", ");
                                }

                                sb.append(row[j]);
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
     * This class offers a comprehensive set of methods for manipulating three-dimensional arrays including
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
         * @throws AssertionError if instantiation is attempted.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][][] arr = {{{"hello", "world"}}, {{"foo", "bar"}}};
         * fff.updateAll(arr, str -> str.toUpperCase());
         * // arr is now {{{"HELLO", "WORLD"}}, {{"FOO", "BAR"}}}
         * }</pre>
         *
         * @param <T> the type of elements in the array.
         * @param <E> the type of exception that may be thrown by the operator.
         * @param a the three-dimensional array to update (can be {@code null}).
         * @param operator the unary operator to apply to each element.
         * @throws E if the {@code operator} throws an exception during execution.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] arr = {{{1, 2, null}}, {{3, null, 5}}};
         * fff.replaceIf(arr, val -> val == null, 0);
         * // arr is now {{{1, 2, 0}}, {{3, 0, 5}}}
         * }</pre>
         *
         * @param <T> the type of elements in the array.
         * @param <E> the type of exception that may be thrown by the predicate.
         * @param a the three-dimensional array to modify (can be {@code null}).
         * @param predicate the condition to test for each element. Elements that match will be replaced.
         * @param newValue the value to replace matching elements with (can be {@code null}).
         * @throws E if the {@code predicate} throws an exception during evaluation.
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
         * dimensions. The method distributes elements from the input array into a three-dimensional structure
         * where each two-dimensional slice has the specified number of rows and columns.
         * 
         * <p>The reshaping process fills the three-dimensional array in order, creating new two-dimensional slices as needed.
         * If the total number of elements doesn't evenly divide into the specified dimensions,
         * the last slices may be partially filled.</p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[] flat = {1, 2, 3, 4, 5, 6, 7, 8};
         * Integer[][][] reshaped = fff.reshape(flat, 2, 2);
         * // reshaped is {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}}
         * }</pre>
         *
         * @param <T> the type of elements in the array.
         * @param a the one-dimensional array to reshape.
         * @param rows the number of rows for each two-dimensional slice. Must be positive.
         * @param cols the number of columns for each two-dimensional slice. Must be positive.
         * @return a new three-dimensional array containing all elements from the input.
         * @throws IllegalArgumentException if rows or cols are not positive numbers.
         */
        public static <T> T[][][] reshape(final T[] a, final int rows, final int cols) throws IllegalArgumentException {
            N.checkArgument(rows > 0 && cols > 0, "'rows' and 'cols' must be positive numbers: rows = %s, cols = %s", rows, cols);

            //        if (N.isEmpty(a)) {
            //            return new T[0][][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] cube = {{{1, 2}}, {{3}}, {{4, 5, 6}}};
         * Integer[] flat = fff.flatten(cube);
         * // flat is {1, 2, 3, 4, 5, 6}
         * }</pre>
         *
         * @param <T> the component type of the array elements.
         * @param a the three-dimensional array to flatten.
         * @return a new one-dimensional array containing all elements in order.
         * @throws IllegalArgumentException if the input array is null.
         */
        public static <T> T[] flatten(final T[][][] a) throws IllegalArgumentException {
            N.checkArgNotNull(a, "The input array must not be null");

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
         * then copies the modified elements back into the original three-dimensional structure. This method
         * is particularly useful for operations that are easier to perform on a linear array,
         * such as sorting all elements regardless of their position in the three-dimensional structure.
         * 
         * <p>The operation preserves the original shape of the three-dimensional array while allowing
         * transformations that work on the entire dataset as a single unit.</p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] arr = {{{5, 2}}, {{9, 1}}, {{3, 7}}};
         * fff.flatOp(arr, flat -> java.util.Arrays.sort(flat));
         * // arr is now {{{1, 2}}, {{3, 5}}, {{7, 9}}}
         * }</pre>
         *
         * @param <T> the type of elements in the array.
         * @param <E> the type of exception that may be thrown by the operation.
         * @param a the three-dimensional array to operate on. Modified in-place.
         * @param op the consumer operation to apply to the flattened array.
         * @throws E if the operation throws an exception.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] numbers = {{{1, 2}}, {{3, 4}}};
         * Integer[][][] doubled = fff.map(numbers, n -> n * 2);
         * // doubled is {{{2, 4}}, {{6, 8}}}
         * }</pre>
         *
         * @param <T> the type of elements in both source and result arrays.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source three-dimensional array. can be {@code null}.
         * @param func the unary operator to apply to each element.
         * @return a new array with mapped elements, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception.
         */
        public static <T, E extends Exception> T[][][] map(final T[][][] a, final Throwables.UnaryOperator<T, E> func) throws E {
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][][] strings = {{{"1", "2"}}, {{"3", "4"}}};
         * Integer[][][] numbers = fff.map(strings, Integer::parseInt, Integer.class);
         * // numbers is {{{1, 2}}, {{3, 4}}}
         * }</pre>
         *
         * @param <T> the type of elements in the source array.
         * @param <R> the type of elements in the result array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source three-dimensional array. can be {@code null}.
         * @param func the function to transform each element.
         * @param targetElementType the class of the result array's element type.
         * @return a new array with transformed elements, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception.
         */
        public static <T, R, E extends Exception> R[][][] map(final T[][][] a, final Throwables.Function<? super T, ? extends R, E> func,
                final Class<R> targetElementType) throws E {
            if (N.isEmpty(a)) {
                return N.newArray(N.newArray(N.newArray(targetElementType, 0).getClass(), 0).getClass(), 0);
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] numbers = {{{1, 2, 3}}, {{4, 5, 6}}};
         * boolean[][][] evenMask = fff.mapToBoolean(numbers, n -> n % 2 == 0);
         * // evenMask is {{{false, true, false}}, {{true, false, true}}}
         * }</pre>
         *
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source three-dimensional array. can be {@code null}.
         * @param func the predicate function to test each element.
         * @return a new boolean array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception.
         */
        public static <T, E extends Exception> boolean[][][] mapToBoolean(final T[][][] a, final Throwables.ToBooleanFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new boolean[0][][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][][] words = {{{"apple", "banana"}}, {{"cat", "dog"}}};
         * char[][][] firstLetters = fff.mapToChar(words, s -> s.charAt(0));
         * // firstLetters is {{{'a', 'b'}}, {{'c', 'd'}}}
         * }</pre>
         *
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source three-dimensional array. can be {@code null}.
         * @param func the function to extract a char from each element.
         * @return a new char array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception.
         */
        public static <T, E extends Exception> char[][][] mapToChar(final T[][][] a, final Throwables.ToCharFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new char[0][][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] numbers = {{{10, 20}}, {{30, 40}}};
         * byte[][][] bytes = fff.mapToByte(numbers, Integer::byteValue);
         * // bytes is {{{10, 20}}, {{30, 40}}}
         * }</pre>
         *
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source three-dimensional array. can be {@code null}.
         * @param func the function to convert each element to byte.
         * @return a new byte array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception.
         */
        public static <T, E extends Exception> byte[][][] mapToByte(final T[][][] a, final Throwables.ToByteFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new byte[0][][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] numbers = {{{100, 200}}, {{300, 400}}};
         * short[][][] shorts = fff.mapToShort(numbers, Integer::shortValue);
         * // shorts is {{{100, 200}}, {{300, 400}}}
         * }</pre>
         *
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source three-dimensional array. can be {@code null}.
         * @param func the function to convert each element to short.
         * @return a new short array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception.
         */
        public static <T, E extends Exception> short[][][] mapToShort(final T[][][] a, final Throwables.ToShortFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new short[0][][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][][] stringNumbers = {{{"1", "2"}}, {{"3", "4"}}};
         * int[][][] integers = fff.mapToInt(stringNumbers, Integer::parseInt);
         * // integers is {{{1, 2}}, {{3, 4}}}
         * }</pre>
         *
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source three-dimensional array. can be {@code null}.
         * @param func the function to convert each element to int.
         * @return a new int array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception.
         */
        public static <T, E extends Exception> int[][][] mapToInt(final T[][][] a, final Throwables.ToIntFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new int[0][][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][][] timestamps = {{{"1000000"}}, {{"2000000"}}};
         * long[][][] longs = fff.mapToLong(timestamps, Long::parseLong);
         * // longs is {{{1000000L}}, {{2000000L}}}
         * }</pre>
         *
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source three-dimensional array. can be {@code null}.
         * @param func the function to convert each element to long.
         * @return a new long array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception.
         */
        public static <T, E extends Exception> long[][][] mapToLong(final T[][][] a, final Throwables.ToLongFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new long[0][][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][][] decimals = {{{"1.5", "2.7"}}, {{"3.14", "4.2"}}};
         * float[][][] floats = fff.mapToFloat(decimals, Float::parseFloat);
         * // floats is {{{1.5f, 2.7f}}, {{3.14f, 4.2f}}}
         * }</pre>
         *
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source three-dimensional array. can be {@code null}.
         * @param func the function to convert each element to float.
         * @return a new float array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception.
         */
        public static <T, E extends Exception> float[][][] mapToFloat(final T[][][] a, final Throwables.ToFloatFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new float[0][][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][][] scientificData = {{{"1.23e10", "4.56e-5"}}, {{"7.89e15"}}};
         * double[][][] doubles = fff.mapToDouble(scientificData, Double::parseDouble);
         * // doubles is {{{1.23e10, 4.56e-5}}, {{7.89e15}}}
         * }</pre>
         *
         * @param <T> the type of elements in the source array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the source three-dimensional array. can be {@code null}.
         * @param func the function to convert each element to double.
         * @return a new double array, or an empty array if input is {@code null}.
         * @throws E if the function throws an exception.
         */
        public static <T, E extends Exception> double[][][] mapToDouble(final T[][][] a, final Throwables.ToDoubleFunction<? super T, E> func) throws E {
            if (N.isEmpty(a)) {
                return new double[0][][];
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] a = {{{1, 2}}, {{3, 4}}};
         * Integer[][][] b = {{{10, 20}}, {{30, 40}}};
         * Integer[][][] sum = fff.zip(a, b, (x, y) -> x + y);
         * // sum is {{{11, 22}}, {{33, 44}}}
         * }</pre>
         *
         * @param <A> the element type of the first array and the result.
         * @param <B> the element type of the second array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the first three-dimensional array.
         * @param b the second three-dimensional array.
         * @param zipFunction the binary function to combine corresponding elements.
         * @return a new array with combined elements.
         * @throws E if the zip function throws an exception.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] numbers = {{{1, 2}}};
         * String[][][] strings = {{{"a", "b"}}};
         * String[][][] combined = fff.zip(numbers, strings, (n, s) -> n + s, String.class);
         * // combined is {{{"1a", "2b"}}}
         * }</pre>
         *
         * @param <A> the element type of the first array.
         * @param <B> the element type of the second array.
         * @param <R> the element type of the result array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the first three-dimensional array.
         * @param b the second three-dimensional array.
         * @param zipFunction the function to combine corresponding elements.
         * @param targetElementType the class of the result array's element type.
         * @return a new array with combined elements of type R.
         * @throws E if the zip function throws an exception.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] a = {{{1, 2}}};
         * Integer[][][] b = {{{10}}, {{20, 30}}};
         * Integer[][][] result = fff.zip(a, b, 0, 0, (x, y) -> x + y);
         * // result is {{{11, 2}}, {{20, 30}}}
         * }</pre>
         *
         * @param <A> the element type of the first array and the result.
         * @param <B> the element type of the second array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the first three-dimensional array.
         * @param b the second three-dimensional array.
         * @param valueForNoneA default value when first array element is missing.
         * @param valueForNoneB default value when second array element is missing.
         * @param zipFunction the function to combine elements.
         * @return a new array with combined elements.
         * @throws E if the zip function throws an exception.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] nums = {{{1}}};
         * String[][][] strs = {{{"a", "b"}}, {{"c"}}};
         * String[][][] result = fff.zip(nums, strs, 0, "x", 
         *                               (n, s) -> n + "-" + s, String.class);
         * // result is {{{"1-a", "0-b"}}, {{"0-c"}}}
         * }</pre>
         *
         * @param <A> the element type of the first array.
         * @param <B> the element type of the second array.
         * @param <R> the element type of the result array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the first three-dimensional array.
         * @param b the second three-dimensional array.
         * @param valueForNoneA default value when first array element is missing.
         * @param valueForNoneB default value when second array element is missing.
         * @param zipFunction the function to combine elements.
         * @param targetElementType the class of the result array's element type.
         * @return a new array with combined elements of type R.
         * @throws E if the zip function throws an exception.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] a = {{{1, 2}}};
         * Integer[][][] b = {{{10, 20}}};
         * Integer[][][] c = {{{100, 200}}};
         * Integer[][][] sum = fff.zip(a, b, c, (x, y, z) -> x + y + z);
         * // sum is {{{111, 222}}}
         * }</pre>
         *
         * @param <A> the element type of the first array and the result.
         * @param <B> the element type of the second array.
         * @param <C> the element type of the third array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the first three-dimensional array.
         * @param b the second three-dimensional array.
         * @param c the third three-dimensional array.
         * @param zipFunction the ternary function to combine corresponding elements.
         * @return a new array with combined elements.
         * @throws E if the zip function throws an exception.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] nums = {{{1}}};
         * String[][][] strs = {{{"a"}}};
         * Double[][][] dbls = {{{2.5}}};
         * String[][][] result = fff.zip(nums, strs, dbls, 
         *                               (n, s, d) -> n + s + d, String.class);
         * // result is {{{"1a2.5"}}}
         * }</pre>
         *
         * @param <A> the element type of the first array.
         * @param <B> the element type of the second array.
         * @param <C> the element type of the third array.
         * @param <R> the element type of the result array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the first three-dimensional array.
         * @param b the second three-dimensional array.
         * @param c the third three-dimensional array.
         * @param zipFunction the function to combine corresponding elements.
         * @param targetElementType the class of the result array's element type.
         * @return a new array with combined elements of type R.
         * @throws E if the zip function throws an exception.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] a = {{{1}}};
         * Integer[][][] b = {{{10, 20}}};
         * Integer[][][] c = {{{}}, {{100}}};
         * Integer[][][] result = fff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
         * // result is {{{11, 20}, {100}}}
         * }</pre>
         *
         * @param <A> the element type of the first array and the result.
         * @param <B> the element type of the second array.
         * @param <C> the element type of the third array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the first three-dimensional array.
         * @param b the second three-dimensional array.
         * @param c the third three-dimensional array.
         * @param valueForNoneA default value when first array element is missing.
         * @param valueForNoneB default value when second array element is missing.
         * @param valueForNoneC default value when third array element is missing.
         * @param zipFunction the function to combine elements.
         * @return a new array with combined elements.
         * @throws E if the zip function throws an exception.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Integer[][][] a = {{{1}}};
         * String[][][] b = {{{"a", "b"}}};
         * Double[][][] c = {{{1.1}}, {{2.2}}};
         * String[][][] result = fff.zip(a, b, c, 0, "x", 0.0,
         *                               (i, s, d) -> i + s + d, String.class);
         * // result is {{{"1a1.1", "0b0.0"}}, {{"0x2.2"}}}
         * }</pre>
         *
         * @param <A> the element type of the first array.
         * @param <B> the element type of the second array.
         * @param <C> the element type of the third array.
         * @param <R> the element type of the result array.
         * @param <E> the type of exception that may be thrown by the function.
         * @param a the first three-dimensional array.
         * @param b the second three-dimensional array.
         * @param c the third three-dimensional array.
         * @param valueForNoneA default value when first array element is missing.
         * @param valueForNoneB default value when second array element is missing.
         * @param valueForNoneC default value when third array element is missing.
         * @param zipFunction the function to combine elements.
         * @param targetElementType the class of the result array's element type.
         * @return a new array with combined elements of type R.
         * @throws E if the zip function throws an exception.
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * Object[][][] array = {{{1, 2}, {3}}, null, {{4, 5, 6}}};
         * long count = fff.totalCountOfElements(array);
         * // count is 6 (elements: 1, 2, 3, 4, 5, 6)
         * }</pre>
         *
         * @param a the three-dimensional array to count elements in.
         * @return the total number of non-null elements across all dimensions.
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
         * to visualize the contents and structure of three-dimensional arrays. Each two-dimensional slice is
         * separated by line breaks for clarity.</p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * String[][][] data = {{{"a", "b"}}, {{"c", "d"}, {"e"}}};
         * String output = fff.println(data);
         * // Prints to console and returns:
         * // [[["a", "b"]],
         * //  [["c", "d"],
         * //   ["e"]]]
         * }</pre>
         *
         * @param a the three-dimensional array to print. can be {@code null}.
         * @return the string representation that was printed to console.
         */
        static String println(final Object[][][] a) {
            if (a == null) {
                return N.println("null");
            } else if (a.length == 0) {
                return N.println("[]");
            } else {
                final StringBuilder sb = Objectory.createStringBuilder();
                final int len = a.length;
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
                            final Object[][] row = a[i];
                            sb.append('[');

                            for (int j = 0, rowLen = row.length; j < rowLen; j++) {
                                if (j > 0) {
                                    sb.append(',').append(IOUtil.LINE_SEPARATOR_UNIX).append("  ");
                                }

                                if (row[j] == null) {
                                    sb.append("null");
                                } else if (row[j].length == 0) {
                                    sb.append("[]");
                                } else {
                                    final Object[] subRow = row[j];
                                    sb.append('[');

                                    for (int k = 0, subRowLen = subRow.length; k < subRowLen; k++) {
                                        if (k > 0) {
                                            sb.append(", ");
                                        }

                                        sb.append(subRow[k]);
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
