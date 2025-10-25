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

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.util.stream.IntStream;

/**
 * An immutable wrapper for primitive int arrays that provides a read-only view of the underlying array.
 * This class is useful when you need to expose an int array without allowing modifications,
 * ensuring thread-safety and preventing accidental mutations.
 * 
 * <p>This class provides two factory methods:</p>
 * <ul>
 *   <li>{@link #of(int[])} - Creates a wrapper around the provided array (no copy)</li>
 *   <li>{@link #copyOf(int[])} - Creates a wrapper around a defensive copy of the array</li>
 * </ul>
 * 
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * int[] data = {1, 2, 3, 4, 5};
 * ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);
 * int value = immutable.get(2); // returns 3
 * }</pre>
 * 
 */
@Beta
public final class ImmutableIntArray implements Immutable {
    /**
     * The length of the underlying array. This field is public and final for
     * efficient access without method invocation overhead.
     */
    public final int length;

    /**
     * The underlying int array storage. This field is private to ensure immutability.
     */
    private final int[] elements;

    /**
     * Package-private constructor that wraps the provided array.
     * If the array is null, an empty array is used instead.
     * 
     * @param a the array to wrap
     */
    ImmutableIntArray(final int[] a) {
        elements = a == null ? N.EMPTY_INT_ARRAY : a;
        length = elements.length;
    }

    /**
     * Creates an ImmutableIntArray that wraps the provided int array without copying.
     *
     * <p><strong>Important:</strong> This method does NOT create a defensive copy of the array.
     * The provided array is used directly as the underlying storage. For true immutability,
     * the caller must not modify the original array after passing it to this method.
     * If the source array might be modified externally, use {@link #copyOf(int[])} instead.</p>
     *
     * <p>This method is more efficient than {@link #copyOf(int[])} when you know the array
     * will not be modified, as it avoids the overhead of array copying.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] data = new int[] {10, 20, 30};
     * ImmutableIntArray array = ImmutableIntArray.of(data);
     * System.out.println(array.get(1)); // prints: 20
     *
     * // CAUTION: Modifying the original array affects the ImmutableIntArray
     * data[1] = 99;
     * System.out.println(array.get(1)); // prints: 99 (not recommended!)
     * }</pre>
     *
     * @param a the int array to wrap, or {@code null} to create an empty ImmutableIntArray
     * @return an ImmutableIntArray wrapping the provided array, or an empty ImmutableIntArray if the input is {@code null}
     * @see #copyOf(int[])
     */
    public static ImmutableIntArray of(final int[] a) {
        return new ImmutableIntArray(a);
    }

    /**
     * Creates an ImmutableIntArray containing a defensive copy of the provided array.
     *
     * <p>This method creates a complete copy of the input array, ensuring that any subsequent
     * modifications to the original array will NOT affect the returned ImmutableIntArray.
     * This is the recommended factory method when the source array might be modified after
     * creating the ImmutableIntArray, or when you need guaranteed immutability.</p>
     *
     * <p>Unlike {@link #of(int[])}, which wraps the array directly, this method provides
     * true immutability at the cost of array copying overhead.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] data = {10, 20, 30};
     * ImmutableIntArray array = ImmutableIntArray.copyOf(data);
     * System.out.println(array.get(0)); // prints: 10
     *
     * // Modifying the original array does NOT affect the ImmutableIntArray
     * data[0] = 99;
     * System.out.println(array.get(0)); // still prints: 10
     * }</pre>
     *
     * @param a the array to copy, or {@code null} to create an empty ImmutableIntArray
     * @return a new ImmutableIntArray containing a defensive copy of the provided array,
     *         or an empty ImmutableIntArray if the input is {@code null}
     * @see #of(int[])
     */
    public static ImmutableIntArray copyOf(final int[] a) {
        return new ImmutableIntArray(a == null ? N.EMPTY_INT_ARRAY : a.clone());
    }

    /**
     * Returns the int element at the specified index in this ImmutableIntArray.
     *
     * <p>This method provides constant-time O(1) access to elements by index.
     * The index is zero-based, meaning the first element is at index 0 and the
     * last element is at index {@code length - 1}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[] {5, 10, 15, 20});
     * int value = array.get(1); // returns 10
     * int first = array.get(0); // returns 5
     * int last = array.get(array.length - 1); // returns 20
     * }</pre>
     *
     * @param index the zero-based index of the element to retrieve (must be &gt;= 0 and &lt; length)
     * @return the int element at the specified index
     * @throws ArrayIndexOutOfBoundsException if the index is negative or greater than or equal to {@code length}
     */
    public int get(final int index) {
        return elements[index];
    }

    /**
     * Performs the given action for each element in this ImmutableIntArray.
     *
     * <p>The action is executed sequentially for each element in array order, starting
     * from index 0 and proceeding to index {@code length - 1}. The action receives
     * each element value as a primitive int.</p>
     *
     * <p>This method is useful for performing side effects on each element, such as
     * printing, accumulating values, or updating external state. For transformations
     * or filtering, consider using the {@link #stream()} method instead.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[] {1, 2, 3, 4, 5});
     *
     * // Print each element
     * array.forEach(value -> System.out.println(value));
     * // Output: 1, 2, 3, 4, 5 (each on a new line)
     *
     * // Accumulate sum
     * int[] sum = {0};
     * array.forEach(value -> sum[0] += value);
     * System.out.println(sum[0]); // prints: 15
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the action to be performed for each element (must not be {@code null})
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws IllegalArgumentException, E {
        N.checkArgNotNull(action);

        for (int i = 0; i < length; i++) {
            action.accept(elements[i]);
        }
    }

    /**
     * Performs the given action for each element in this ImmutableIntArray, providing both the index and value.
     *
     * <p>The action is executed sequentially for each element in array order, starting from index 0
     * and proceeding to index {@code length - 1}. For each element, the action receives two int parameters:
     * the element's index (first parameter) and the element's value (second parameter).</p>
     *
     * <p>This method is useful when you need to know the position of each element during iteration,
     * such as for creating index-based mappings or when the index is needed for computation.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[] {10, 20, 30});
     *
     * // Print each element with its index
     * array.forEachIndexed((index, value) ->
     *     System.out.println("Index " + index + ": " + value));
     * // Output:
     * // Index 0: 10
     * // Index 1: 20
     * // Index 2: 30
     *
     * // Create a map of index to value
     * Map<Integer, Integer> map = new HashMap<>();
     * array.forEachIndexed((index, value) -> map.put(index, value));
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the action to be performed for each element, receiving the index (first parameter)
     *               and value (second parameter) as primitive ints (must not be {@code null})
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     */
    public <E extends Exception> void forEachIndexed(final Throwables.IntIntConsumer<E> action) throws IllegalArgumentException, E {
        N.checkArgNotNull(action);

        for (int i = 0; i < length; i++) {
            action.accept(i, elements[i]);
        }
    }

    /**
     * Returns an IntStream containing all elements of this ImmutableIntArray.
     *
     * <p>The returned stream provides a functional programming interface for processing
     * the array elements, supporting operations such as filtering, mapping, reduction,
     * and collection. The stream processes elements in array order (from index 0 to
     * {@code length - 1}).</p>
     *
     * <p>This method is useful for applying functional transformations and operations
     * on the array elements without manually iterating through them.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[] {1, 2, 3, 4, 5});
     *
     * // Calculate sum
     * int sum = array.stream().sum(); // returns 15
     *
     * // Find maximum
     * int max = array.stream().max().orElse(0); // returns 5
     *
     * // Filter and collect
     * int[] evens = array.stream()
     *     .filter(x -> x % 2 == 0)
     *     .toArray(); // returns {2, 4}
     *
     * // Map and sum
     * int sumOfSquares = array.stream()
     *     .map(x -> x * x)
     *     .sum(); // returns 55 (1 + 4 + 9 + 16 + 25)
     * }</pre>
     *
     * @return an IntStream containing all elements of this ImmutableIntArray in order
     */
    public IntStream stream() {
        return IntStream.of(elements);
    }

    /**
     * Creates a new ImmutableIntArray containing a copy of the elements in the specified range.
     *
     * <p>The range follows the standard half-open interval convention: {@code [fromIndex, toIndex)},
     * meaning {@code fromIndex} is inclusive and {@code toIndex} is exclusive. The resulting
     * ImmutableIntArray will have a length of {@code toIndex - fromIndex}.</p>
     *
     * <p>This method creates a defensive copy of the specified range, so the returned
     * ImmutableIntArray is completely independent of the original array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[] {10, 20, 30, 40, 50});
     *
     * ImmutableIntArray subArray = array.copy(1, 4);
     * // subArray contains {20, 30, 40} with length 3
     *
     * ImmutableIntArray first3 = array.copy(0, 3);
     * // first3 contains {10, 20, 30}
     *
     * ImmutableIntArray empty = array.copy(2, 2);
     * // empty is an empty array with length 0
     * }</pre>
     *
     * @param fromIndex the starting index (inclusive) of the range to copy (must be &gt;= 0)
     * @param toIndex the ending index (exclusive) of the range to copy (must be &lt;= length)
     * @return a new ImmutableIntArray containing a copy of the elements in the specified range
     * @throws IndexOutOfBoundsException if {@code fromIndex < 0}, {@code toIndex > length},
     *                                   or {@code fromIndex > toIndex}
     * @see #copyToArray(int, int)
     */
    public ImmutableIntArray copy(final int fromIndex, final int toIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromIndex, toIndex, length);

        return ImmutableIntArray.of(N.copyOfRange(elements, fromIndex, toIndex));
    }

    /**
     * Copies the elements in the specified range to a new mutable primitive int array.
     *
     * <p>The range follows the standard half-open interval convention: {@code [fromIndex, toIndex)},
     * meaning {@code fromIndex} is inclusive and {@code toIndex} is exclusive. The resulting
     * array will have a length of {@code toIndex - fromIndex}.</p>
     *
     * <p>Unlike {@link #copy(int, int)}, which returns an ImmutableIntArray, this method returns
     * a standard mutable int array that can be modified. This is useful when you need to perform
     * mutations on the copied data or when interfacing with APIs that require primitive arrays.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[] {10, 20, 30, 40, 50});
     *
     * int[] mutableCopy = array.copyToArray(1, 4);
     * // mutableCopy is {20, 30, 40}
     *
     * // Can modify the returned array
     * mutableCopy[0] = 99;
     * System.out.println(Arrays.toString(mutableCopy)); // prints: [99, 30, 40]
     * System.out.println(array.get(1)); // still prints: 20 (original unchanged)
     *
     * // Get all elements as a mutable array
     * int[] allElements = array.copyToArray(0, array.length);
     * }</pre>
     *
     * @param fromIndex the starting index (inclusive) of the range to copy (must be &gt;= 0)
     * @param toIndex the ending index (exclusive) of the range to copy (must be &lt;= length)
     * @return a new mutable int array containing the specified range of elements
     * @throws IndexOutOfBoundsException if {@code fromIndex < 0}, {@code toIndex > length},
     *                                   or {@code fromIndex > toIndex}
     * @see #copy(int, int)
     */
    public int[] copyToArray(final int fromIndex, final int toIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromIndex, toIndex, length);

        return N.copyOfRange(elements, fromIndex, toIndex);
    }

    /**
     * Returns a hash code value for this ImmutableIntArray.
     *
     * <p>The hash code is computed based on the contents of the underlying array,
     * following the same contract as {@link java.util.Arrays#hashCode(int[])}.
     * Two ImmutableIntArray instances with the same elements in the same order
     * will produce the same hash code.</p>
     *
     * <p>This method satisfies the general contract of {@link Object#hashCode()}:
     * if two ImmutableIntArray instances are equal according to {@link #equals(Object)},
     * then calling this method on each will produce the same integer result.</p>
     *
     * @return a hash code value for this ImmutableIntArray based on its contents
     */
    @Override
    public int hashCode() {
        return N.hashCode(elements);
    }

    /**
     * Compares this ImmutableIntArray with the specified object for equality.
     *
     * <p>Returns {@code true} if and only if the specified object is also an ImmutableIntArray
     * and both arrays contain the same elements in the same order. This method follows the
     * same contract as {@link java.util.Arrays#equals(int[], int[])}.</p>
     *
     * <p>Two ImmutableIntArray instances are considered equal if:
     * <ul>
     *   <li>They have the same length</li>
     *   <li>For every index {@code i}, {@code this.get(i) == other.get(i)}</li>
     * </ul>
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array1 = ImmutableIntArray.of(new int[] {1, 2, 3});
     * ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] {1, 2, 3});
     * ImmutableIntArray array3 = ImmutableIntArray.of(new int[] {1, 2, 4});
     *
     * System.out.println(array1.equals(array2)); // prints: true
     * System.out.println(array1.equals(array3)); // prints: false
     * System.out.println(array1.equals(new int[] {1, 2, 3})); // prints: false (different type)
     * }</pre>
     *
     * @param obj the object to compare with this ImmutableIntArray
     * @return {@code true} if the specified object is an ImmutableIntArray with the same elements
     *         in the same order; {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ImmutableIntArray immutableintarray && N.equals(elements, immutableintarray.elements);
    }

    /**
     * Returns a string representation of this ImmutableIntArray.
     *
     * <p>The string representation consists of the array elements enclosed in square brackets
     * ({@code "[]"}) and separated by commas and spaces ({@code ", "}). This format is
     * consistent with {@link java.util.Arrays#toString(int[])} and provides a human-readable
     * representation of the array contents.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[] {1, 2, 3});
     * System.out.println(array.toString()); // prints: [1, 2, 3]
     * System.out.println(array); // prints: [1, 2, 3]
     *
     * ImmutableIntArray empty = ImmutableIntArray.of(null);
     * System.out.println(empty); // prints: []
     *
     * ImmutableIntArray single = ImmutableIntArray.of(new int[] {42});
     * System.out.println(single); // prints: [42]
     * }</pre>
     *
     * @return a string representation of this ImmutableIntArray in the format {@code "[element1, element2, ...]"}
     */
    @Override
    public String toString() {
        return N.toString(elements);
    }
}