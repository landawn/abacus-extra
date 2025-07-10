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
 * <p>Example usage:</p>
 * <pre>{@code
 * int[] data = {1, 2, 3, 4, 5};
 * ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);
 * int value = immutable.get(2); // returns 3
 * }</pre>
 * 
 * @author HaiYang Li
 * @since 1.0
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
     * @param a the array to wrap (may be null)
     */
    ImmutableIntArray(final int[] a) {
        elements = a == null ? N.EMPTY_INT_ARRAY : a;
        length = elements.length;
    }

    /**
     * Creates an ImmutableIntArray that wraps the provided array directly without copying.
     * Changes to the original array will be reflected in the ImmutableIntArray.
     * Use this method when you are certain the array won't be modified after creation.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * int[] data = {10, 20, 30};
     * ImmutableIntArray array = ImmutableIntArray.of(data);
     * }</pre>
     * 
     * @param a the array to wrap (may be null, in which case an empty immutable array is created)
     * @return an ImmutableIntArray wrapping the provided array
     */
    public static ImmutableIntArray of(final int[] a) {
        return new ImmutableIntArray(a);
    }

    /**
     * Creates an ImmutableIntArray containing a defensive copy of the provided array.
     * Changes to the original array will NOT affect the ImmutableIntArray.
     * This is the preferred method when the source array might be modified.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * int[] data = {10, 20, 30};
     * ImmutableIntArray array = ImmutableIntArray.copyOf(data);
     * data[0] = 99; // Does not affect the immutable array
     * }</pre>
     * 
     * @param a the array to copy (may be null, in which case an empty immutable array is created)
     * @return an ImmutableIntArray containing a copy of the provided array
     */
    public static ImmutableIntArray copyOf(final int[] a) {
        return new ImmutableIntArray(a == null ? N.EMPTY_INT_ARRAY : a.clone());
    }

    /**
     * Returns the element at the specified index.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[]{5, 10, 15});
     * int value = array.get(1); // returns 10
     * }</pre>
     * 
     * @param index the index of the element to retrieve (0-based)
     * @return the element at the specified index
     * @throws ArrayIndexOutOfBoundsException if the index is negative or >= length
     */
    public int get(final int index) {
        return elements[index];
    }

    /**
     * Performs the given action for each element in the array.
     * The action is executed sequentially in array order (from index 0 to length-1).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});
     * array.forEach(value -> System.out.println(value));
     * // Prints: 1, 2, 3 (each on a new line)
     * }</pre>
     * 
     * @param <E> the type of exception that the action may throw
     * @param action the action to be performed for each element (must not be null)
     * @throws IllegalArgumentException if the action is null
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws IllegalArgumentException, E {
        N.checkArgNotNull(action);

        for (int i = 0; i < length; i++) {
            action.accept(elements[i]);
        }
    }

    /**
     * Performs the given action for each element in the array, providing both the index and value.
     * The action is executed sequentially in array order (from index 0 to length-1).
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});
     * array.forEachIndexed((index, value) -> 
     *     System.out.println("Index " + index + ": " + value));
     * // Prints: Index 0: 10, Index 1: 20, Index 2: 30
     * }</pre>
     * 
     * @param <E> the type of exception that the action may throw
     * @param action the action to be performed for each element, receiving the index and value (must not be null)
     * @throws IllegalArgumentException if the action is null
     * @throws E if the action throws an exception
     */
    public <E extends Exception> void forEachIndexed(final Throwables.IntIntConsumer<E> action) throws IllegalArgumentException, E {
        N.checkArgNotNull(action);

        for (int i = 0; i < length; i++) {
            action.accept(i, elements[i]);
        }
    }

    /**
     * Returns an IntStream of the elements in this array.
     * The stream provides a functional programming interface for processing the array elements.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5});
     * int sum = array.stream().sum(); // returns 15
     * int max = array.stream().max().orElse(0); // returns 5
     * }</pre>
     * 
     * @return an IntStream containing all elements of this array
     */
    public IntStream stream() {
        return IntStream.of(elements);
    }

    /**
     * Creates a new ImmutableIntArray containing a copy of the elements in the specified range.
     * The range is half-open: [fromIndex, toIndex), meaning fromIndex is inclusive and toIndex is exclusive.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30, 40, 50});
     * ImmutableIntArray subArray = array.copy(1, 4); // contains {20, 30, 40}
     * }</pre>
     * 
     * @param fromIndex the starting index (inclusive) of the range to copy
     * @param toIndex the ending index (exclusive) of the range to copy
     * @return a new ImmutableIntArray containing the specified range of elements
     * @throws IndexOutOfBoundsException if fromIndex < 0, toIndex > length, or fromIndex > toIndex
     */
    public ImmutableIntArray copy(final int fromIndex, final int toIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromIndex, toIndex, length);

        return ImmutableIntArray.of(N.copyOfRange(elements, fromIndex, toIndex));
    }

    /**
     * Copies the elements in the specified range to a new int array.
     * The range is half-open: [fromIndex, toIndex), meaning fromIndex is inclusive and toIndex is exclusive.
     * This method is useful when you need a mutable copy of a portion of the array.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30, 40, 50});
     * int[] mutableCopy = array.copyToArray(1, 4); // returns {20, 30, 40}
     * mutableCopy[0] = 99; // Can modify the returned array
     * }</pre>
     * 
     * @param fromIndex the starting index (inclusive) of the range to copy
     * @param toIndex the ending index (exclusive) of the range to copy
     * @return a new int array containing the specified range of elements
     * @throws IndexOutOfBoundsException if fromIndex < 0, toIndex > length, or fromIndex > toIndex
     */
    public int[] copyToArray(final int fromIndex, final int toIndex) throws IndexOutOfBoundsException {
        N.checkFromToIndex(fromIndex, toIndex, length);

        return N.copyOfRange(elements, fromIndex, toIndex);
    }

    /**
     * Returns a hash code value for this ImmutableIntArray.
     * The hash code is computed based on the contents of the underlying array,
     * following the same contract as {@link java.util.Arrays#hashCode(int[])}.
     * 
     * @return a hash code value for this array
     */
    @Override
    public int hashCode() {
        return N.hashCode(elements);
    }

    /**
     * Compares this ImmutableIntArray with the specified object for equality.
     * Two ImmutableIntArray instances are equal if they contain the same elements
     * in the same order. This method follows the same contract as 
     * {@link java.util.Arrays#equals(int[], int[])}.
     * 
     * @param obj the object to compare with
     * @return true if the specified object is an ImmutableIntArray with the same elements in the same order
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ImmutableIntArray immutableintarray && N.equals(elements, immutableintarray.elements);
    }

    /**
     * Returns a string representation of this ImmutableIntArray.
     * The string representation consists of the array elements enclosed in square brackets
     * and separated by commas, similar to {@link java.util.Arrays#toString(int[])}.
     * 
     * <p>Example:</p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});
     * System.out.println(array); // prints: [1, 2, 3]
     * }</pre>
     * 
     * @return a string representation of this array
     */
    @Override
    public String toString() {
        return N.toString(elements);
    }
}