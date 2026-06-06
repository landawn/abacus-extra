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

import java.util.NoSuchElementException;

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.util.stream.IntStream;

/**
 * Immutable-style wrapper around an {@code int[]}.
 *
 * <p>{@link #copyOf(int[])} creates an isolated snapshot, while {@link #unsafeWrap(int[])} keeps the
 * supplied array as backing storage. The latter avoids copying but is only as immutable as the caller's
 * discipline, so prefer {@code copyOf} unless the backing array is exclusively owned for the lifetime of
 * the wrapper.</p>
 *
 * <p>The wrapper itself exposes no mutator methods, and accessors that return arrays
 * ({@link #copyOfRange(int, int)}) always return fresh copies. The {@link #stream()} method, however,
 * is constructed directly over the backing array; see its javadoc for the implications.</p>
 *
 * <p>This class is annotated with {@link Beta @Beta} and its API may evolve in future releases.</p>
 *
 * @see #copyOf(int[])
 * @see #unsafeWrap(int[])
 * @see Immutable
 */
@Beta
public final class ImmutableIntArray implements Immutable {
    /**
     * The number of elements in this array; equals {@code elements.length}.
     */
    private final int length;

    /**
     * Backing storage for this wrapper.
     */
    private final int[] elements;

    /**
     * Package-private constructor that retains the provided array as the backing storage
     * without copying. If {@code array} is {@code null}, {@link N#EMPTY_INT_ARRAY} is used instead.
     *
     * @param array the array to wrap, or {@code null} for an empty backing array
     */
    ImmutableIntArray(final int[] array) {
        elements = array == null ? N.EMPTY_INT_ARRAY : array;
        length = elements.length;
    }

    /**
     * Creates an ImmutableIntArray that uses the provided int array as backing storage without copying.
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
     * // Basic: wrap an existing array and read elements
     * int[] data = new int[] {10, 20, 30};
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);
     * array.get(0);   // returns 10
     * array.get(1);   // returns 20
     *
     * // Basic: wrap a single-element array
     * ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] {7});
     * single.length();   // returns 1
     *
     * // Edge: null input produces an empty wrapper (no exception)
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
     * empty.length();   // returns 0
     * empty.isEmpty();  // returns true
     *
     * // Edge: CAUTION - mutations to the source array are visible through the wrapper
     * data[1] = 99;
     * array.get(1);   // returns 99  (shared backing array - not a copy!)
     * }</pre>
     *
     * @param array the int array to wrap unsafely, or {@code null} to create an empty ImmutableIntArray
     * @return an ImmutableIntArray backed directly by the provided array, or an empty ImmutableIntArray if the input is {@code null}
     * @see #copyOf(int[])
     */
    public static ImmutableIntArray unsafeWrap(final int[] array) {
        return new ImmutableIntArray(array);
    }

    /**
     * Creates an ImmutableIntArray containing a defensive copy of the provided array.
     *
     * <p>This method creates a complete copy of the input array, ensuring that any subsequent
     * modifications to the original array will NOT affect the returned ImmutableIntArray.
     * This is the recommended factory method when the source array might be modified after
     * creating the ImmutableIntArray, or when you need guaranteed immutability.</p>
     *
     * <p>Unlike {@link #unsafeWrap(int[])}, which wraps the array directly, this method provides
     * true immutability at the cost of array copying overhead.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: copy an array and read its elements
     * int[] data = {10, 20, 30};
     * ImmutableIntArray array = ImmutableIntArray.copyOf(data);
     * array.get(0);   // returns 10
     * array.get(2);   // returns 30
     *
     * // Basic: copy a single-element array
     * ImmutableIntArray single = ImmutableIntArray.copyOf(new int[] {42});
     * single.get(0);   // returns 42
     *
     * // Edge: null input produces an empty wrapper (no exception)
     * ImmutableIntArray empty = ImmutableIntArray.copyOf(null);
     * empty.isEmpty();   // returns true
     *
     * // Edge: mutations to the original DO NOT affect the returned wrapper (defensive copy)
     * data[0] = 99;
     * array.get(0);   // still returns 10
     * }</pre>
     *
     * @param array the int array to copy, or {@code null} to create an empty ImmutableIntArray
     * @return a new ImmutableIntArray containing a defensive copy of the provided array,
     *         or an empty ImmutableIntArray if the input is {@code null}
     * @see #unsafeWrap(int[])
     */
    public static ImmutableIntArray copyOf(final int[] array) {
        return new ImmutableIntArray(array == null ? N.EMPTY_INT_ARRAY : array.clone());
    }

    /**
     * Returns {@code true} if this ImmutableIntArray contains no elements.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: non-empty array
     * ImmutableIntArray nonEmpty = ImmutableIntArray.unsafeWrap(new int[] {1, 2, 3});
     * nonEmpty.isEmpty();   // returns false
     *
     * // Basic: array with a single element is not empty
     * ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] {0});
     * single.isEmpty();   // returns false
     *
     * // Edge: explicit empty array
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
     * empty.isEmpty();   // returns true
     *
     * // Edge: null input is treated as empty
     * ImmutableIntArray fromNull = ImmutableIntArray.unsafeWrap(null);
     * fromNull.isEmpty();   // returns true
     * }</pre>
     *
     * @return {@code true} if {@code length() == 0}; {@code false} otherwise
     * @see #length()
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Returns the number of elements in this ImmutableIntArray.
     *
     * <p>The returned value is fixed at construction time and equals the length of the backing
     * {@code int[]}. It is always {@code >= 0}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: three-element array
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {1, 2, 3});
     * array.length();   // returns 3
     *
     * // Basic: single-element array
     * ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] {7});
     * single.length();   // returns 1
     *
     * // Edge: null input maps to length 0
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
     * empty.length();   // returns 0
     *
     * // Edge: explicit empty array
     * ImmutableIntArray explicit = ImmutableIntArray.unsafeWrap(new int[0]);
     * explicit.length();   // returns 0
     * }</pre>
     *
     * @return the number of elements in this array; {@code 0} if empty
     * @see #isEmpty()
     */
    public int length() {
        return length;
    }

    /**
     * Returns {@code true} if this ImmutableIntArray contains the specified value.
     *
     * <p>This method performs a linear search through the array, checking each element
     * for equality with the specified value. The time complexity is O(n) where n is the
     * length of the array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {10, 20, 30, 40, 50});
     *
     * // Basic: value present at interior position
     * array.contains(30);   // returns true
     *
     * // Basic: value present at first/last position
     * array.contains(10);   // returns true
     * array.contains(50);   // returns true
     *
     * // Edge: value absent
     * array.contains(99);   // returns false
     *
     * // Edge: empty array - always false
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
     * empty.contains(1);   // returns false
     *
     * // Edge: negative values are matched correctly
     * ImmutableIntArray neg = ImmutableIntArray.unsafeWrap(new int[] {-5, 0, 5});
     * neg.contains(-5);   // returns true
     * neg.contains(1);    // returns false
     * }</pre>
     *
     * @param value the value to search for
     * @return {@code true} if at least one element equals {@code value}; {@code false} otherwise
     *         (including when the array is empty)
     */
    public boolean contains(final int value) {
        for (int i = 0; i < length; i++) {
            if (elements[i] == value) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the minimum value among all elements in this array.
     *
     * <p>This method scans every element, so its time complexity is O(n) where n is the
     * length of the array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: minimum of a multi-element array
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {3, 1, 4, 1, 5});
     * array.min();   // returns 1
     *
     * // Basic: single-element array - the only element is the minimum
     * ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] {42});
     * single.min();   // returns 42
     *
     * // Edge: all-negative values
     * ImmutableIntArray neg = ImmutableIntArray.unsafeWrap(new int[] {-3, -1, -4});
     * neg.min();   // returns -4
     *
     * // Edge: empty array throws NoSuchElementException
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
     * empty.min();   // throws NoSuchElementException
     * }</pre>
     *
     * @return the minimum int value in this array
     * @throws NoSuchElementException if this array is empty
     * @see #max()
     * @see #sum()
     * @see #average()
     */
    public int min() {
        if (length == 0) {
            throw new NoSuchElementException("ImmutableIntArray is empty");
        }
        return N.min(elements);
    }

    /**
     * Returns the maximum value among all elements in this array.
     *
     * <p>This method scans every element, so its time complexity is O(n) where n is the
     * length of the array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: maximum of a multi-element array
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {3, 1, 4, 1, 5});
     * array.max();   // returns 5
     *
     * // Basic: single-element array - the only element is the maximum
     * ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] {-7});
     * single.max();   // returns -7
     *
     * // Edge: all-negative values
     * ImmutableIntArray neg = ImmutableIntArray.unsafeWrap(new int[] {-3, -1, -4});
     * neg.max();   // returns -1
     *
     * // Edge: empty array throws NoSuchElementException
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
     * empty.max();   // throws NoSuchElementException
     * }</pre>
     *
     * @return the maximum int value in this array
     * @throws NoSuchElementException if this array is empty
     * @see #min()
     * @see #sum()
     * @see #average()
     */
    public int max() {
        if (length == 0) {
            throw new NoSuchElementException("ImmutableIntArray is empty");
        }
        return N.max(elements);
    }

    /**
     * Returns the sum of all elements in this array as an {@code int}.
     *
     * <p>The summation is performed in {@code long} precision and narrowed to {@code int};
     * if the total does not fit in the {@code int} range, an {@link ArithmeticException} is thrown.
     * For an empty array this method returns {@code 0}.</p>
     *
     * <p>Unlike {@link #min()} and {@link #max()}, this method does not throw on an empty array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: sum of positive elements
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {1, 2, 3, 4});
     * array.sum();   // returns 10
     *
     * // Basic: single-element sum equals that element
     * ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] {100});
     * single.sum();   // returns 100
     *
     * // Edge: empty array returns 0 (no exception)
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
     * empty.sum();   // returns 0
     *
     * // Edge: negative elements are summed correctly
     * ImmutableIntArray neg = ImmutableIntArray.unsafeWrap(new int[] {-1, -2, -3});
     * neg.sum();   // returns -6
     * }</pre>
     *
     * @return the sum of all elements in this array as an {@code int}, or {@code 0} if empty
     * @throws ArithmeticException if the sum overflows the {@code int} range
     * @see #average()
     * @see #min()
     * @see #max()
     */
    public int sum() {
        return N.sum(elements);
    }

    /**
     * Returns the arithmetic mean of all elements in this array as a {@code double}.
     *
     * <p>The result is returned as a {@code double} to preserve fractional precision.
     * For an empty array this method returns {@code 0D} (it does not throw).</p>
     *
     * <p>Unlike {@link #min()} and {@link #max()}, this method does not throw on an empty array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: fractional average
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {1, 2, 3, 4});
     * array.average();   // returns 2.5
     *
     * // Basic: whole-number average
     * ImmutableIntArray even = ImmutableIntArray.unsafeWrap(new int[] {2, 4, 6});
     * even.average();   // returns 4.0
     *
     * // Edge: empty array returns 0.0 (no exception)
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
     * empty.average();   // returns 0.0
     *
     * // Edge: single-element average equals that element as double
     * ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] {10});
     * single.average();   // returns 10.0
     * }</pre>
     *
     * @return the average of all elements in this array as a {@code double}, or {@code 0D} if empty
     * @see #sum()
     * @see #min()
     * @see #max()
     */
    public double average() {
        return N.average(elements);
    }

    /**
     * Returns the int element at the specified index in this ImmutableIntArray.
     *
     * <p>This method provides constant-time O(1) access to elements by index.
     * The index is zero-based, meaning the first element is at index 0 and the
     * last element is at index {@code length() - 1}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {5, 10, 15, 20});
     *
     * // Basic: access by various valid indices
     * array.get(0);                    // returns 5
     * array.get(1);                    // returns 10
     * array.get(array.length() - 1);   // returns 20
     *
     * // Basic: access last element of single-element array
     * ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] {42});
     * single.get(0);   // returns 42
     *
     * // Edge: negative index throws ArrayIndexOutOfBoundsException
     * array.get(-1);   // throws ArrayIndexOutOfBoundsException
     *
     * // Edge: index >= length() throws ArrayIndexOutOfBoundsException
     * array.get(4);   // throws ArrayIndexOutOfBoundsException
     * }</pre>
     *
     * @param index the zero-based index of the element to retrieve; must be {@code >= 0} and {@code < length()}
     * @return the int element at the specified index
     * @throws ArrayIndexOutOfBoundsException if {@code index < 0} or {@code index >= length()}
     * @see #length()
     */
    public int get(final int index) {
        return elements[index];
    }

    /**
     * Performs the given action for each element in this ImmutableIntArray.
     *
     * <p>The action is executed sequentially for each element in array order, starting
     * from index 0 and proceeding to index {@code length() - 1}. The action receives
     * each element value as a primitive int.</p>
     *
     * <p>This method is useful for performing side effects on each element, such as
     * printing, accumulating values, or updating external state. For transformations
     * or filtering, consider using the {@link #stream()} method instead.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {1, 2, 3, 4, 5});
     *
     * // Basic: accumulate a sum across all elements
     * int[] sum = {0};
     * array.forEach(value -> sum[0] += value);
     * sum[0];   // returns 15
     *
     * // Basic: collect each value into a list
     * java.util.List<Integer> list = new java.util.ArrayList<>();
     * array.forEach(value -> list.add(value));   // adds each value to list
     * list.size();                               // returns 5
     * list.get(0);                               // returns 1
     *
     * // Edge: empty array - action is never invoked
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
     * int[] count = {0};
     * empty.forEach(v -> count[0]++);   // action not invoked (empty)
     * count[0];                         // returns 0
     *
     * // Edge: null action throws IllegalArgumentException
     * array.forEach(null);   // throws IllegalArgumentException
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the action to be performed for each element, must not be {@code null}
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     * @see #forEachIndexed(Throwables.IntIntConsumer)
     * @see #stream()
     */
    public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws IllegalArgumentException, E {
        N.checkArgNotNull(action, "action");

        for (int i = 0; i < length; i++) {
            action.accept(elements[i]);
        }
    }

    /**
     * Performs the given action for each element in this ImmutableIntArray, providing both the index and value.
     *
     * <p>The action is executed sequentially for each element in array order, starting from index 0
     * and proceeding to index {@code length() - 1}. For each element, the action receives two int parameters:
     * the element's index (first parameter) and the element's value (second parameter).</p>
     *
     * <p>This method is useful when you need to know the position of each element during iteration,
     * such as for creating index-based mappings or when the index is needed for computation.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {10, 20, 30});
     *
     * // Basic: capture (index, value) pairs into parallel arrays
     * int[] indices = new int[3];
     * int[] values = new int[3];
     * array.forEachIndexed((index, value) -> {
     *     indices[index] = index;
     *     values[index] = value;
     * });
     * indices[2];   // returns 2
     * values[2];    // returns 30
     *
     * // Basic: build a map from index to value
     * java.util.Map<Integer, Integer> map = new java.util.HashMap<>();
     * array.forEachIndexed((index, value) -> map.put(index, value));   // puts each index -> value into map
     * map.get(1);                                                      // returns 20
     *
     * // Edge: empty array - action is never invoked
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
     * int[] count = {0};
     * empty.forEachIndexed((i, v) -> count[0]++);   // action not invoked (empty)
     * count[0];                                     // returns 0
     *
     * // Edge: null action throws IllegalArgumentException
     * array.forEachIndexed(null);   // throws IllegalArgumentException
     * }</pre>
     *
     * @param <E> the type of exception that the action may throw
     * @param action the action to be performed for each element, receiving the index (first parameter)
     *               and value (second parameter) as primitive ints, must not be {@code null}
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     * @see #forEach(Throwables.IntConsumer)
     */
    public <E extends Exception> void forEachIndexed(final Throwables.IntIntConsumer<E> action) throws IllegalArgumentException, E {
        N.checkArgNotNull(action, "action");

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
     * {@code length() - 1}).</p>
     *
     * <p>This method is useful for applying functional transformations and operations
     * on the array elements without manually iterating through them.</p>
     *
     * <p><b>Note:</b> the returned stream is constructed directly over the backing array; it does not
     * make a defensive copy. When this wrapper was created via {@link #unsafeWrap(int[])}, the backing
     * array is the caller-supplied array, so any mutation of that array will be observable through the
     * returned stream. Use {@link #copyOf(int[])} to build the wrapper from a defensive copy if you require
     * full isolation from the original source array.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {1, 2, 3, 4, 5});
     *
     * // Basic: sum all elements via stream terminal operation
     * array.stream().sum();   // returns 15
     *
     * // Basic: map each element to its square and sum
     * array.stream().map(x -> x * x).sum();   // returns 55  (1+4+9+16+25)
     *
     * // Edge: filter to even elements and collect to array
     * array.stream().filter(x -> x % 2 == 0).toArray();   // returns {2, 4}
     *
     * // Edge: empty array produces an empty stream
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
     * empty.stream().sum();   // returns 0
     * }</pre>
     *
     * @return an IntStream containing all elements of this ImmutableIntArray in order
     */
    public IntStream stream() {
        return IntStream.of(elements);
    }

    /**
     * Returns a new int array containing a copy of the elements in the specified range.
     *
     * <p>The range follows the standard half-open interval convention: {@code [fromIndex, toIndex)}.
     * {@code fromIndex} is inclusive and {@code toIndex} is exclusive. The returned array length is
     * {@code toIndex - fromIndex}. If {@code fromIndex == toIndex}, an empty array is returned.</p>
     *
     * <p>The returned array is a fresh copy and is independent of this ImmutableIntArray.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {10, 20, 30, 40, 50});
     *
     * // Basic: interior slice
     * array.copyOfRange(1, 4);   // returns {20, 30, 40}
     *
     * // Basic: slice from the start
     * array.copyOfRange(0, 3);   // returns {10, 20, 30}
     *
     * // Edge: equal indices produce an empty array (no exception)
     * array.copyOfRange(2, 2);   // returns {} (length 0)
     *
     * // Edge: toIndex > length() throws IndexOutOfBoundsException
     * array.copyOfRange(0, 10);   // throws IndexOutOfBoundsException
     *
     * // Edge: fromIndex > toIndex throws IndexOutOfBoundsException
     * array.copyOfRange(3, 1);   // throws IndexOutOfBoundsException
     * }</pre>
     *
     * @param fromIndex the starting index (inclusive) of the range to copy (must be {@code >= 0})
     * @param toIndex the ending index (exclusive) of the range to copy (must be {@code <= length()})
     * @return a newly allocated int array containing the elements in {@code [fromIndex, toIndex)}
     * @throws IndexOutOfBoundsException if {@code fromIndex < 0}, {@code toIndex > length()},
     *                                   or {@code fromIndex > toIndex}
     * @see java.util.Arrays#copyOfRange(int[], int, int)
     */
    public int[] copyOfRange(final int fromIndex, final int toIndex) throws IndexOutOfBoundsException {
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: two wrappers with the same contents have the same hash code
     * ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] {1, 2, 3});
     * ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] {1, 2, 3});
     * array1.hashCode() == array2.hashCode();   // returns true
     *
     * // Basic: different order produces a different hash code
     * ImmutableIntArray reversed = ImmutableIntArray.unsafeWrap(new int[] {3, 2, 1});
     * array1.hashCode() == reversed.hashCode();   // returns false
     *
     * // Edge: two empty wrappers have the same hash code
     * ImmutableIntArray empty1 = ImmutableIntArray.unsafeWrap(new int[0]);
     * ImmutableIntArray empty2 = ImmutableIntArray.copyOf(null);
     * empty1.hashCode() == empty2.hashCode();   // returns true
     *
     * // Edge: distinct content typically (but not always) produces a distinct hash
     * ImmutableIntArray array3 = ImmutableIntArray.unsafeWrap(new int[] {1, 2, 4});
     * // array1.hashCode() == array3.hashCode() - typically false
     * }</pre>
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
     * <p>Two ImmutableIntArray instances are considered equal if:</p>
     * <ul>
     *   <li>They have the same length</li>
     *   <li>For every index {@code i}, {@code this.get(i) == other.get(i)}</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] {1, 2, 3});
     * ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] {1, 2, 3});
     * ImmutableIntArray array3 = ImmutableIntArray.unsafeWrap(new int[] {1, 2, 4});
     *
     * // Basic: same elements in same order - equal
     * array1.equals(array2);   // returns true
     *
     * // Basic: same length but different content - not equal
     * array1.equals(array3);   // returns false
     *
     * // Edge: a raw int[] is never equal to an ImmutableIntArray
     * array1.equals(new int[] {1, 2, 3});   // returns false
     *
     * // Edge: null is never equal
     * array1.equals(null);   // returns false
     *
     * // Edge: different lengths are not equal
     * ImmutableIntArray shorter = ImmutableIntArray.unsafeWrap(new int[] {1, 2});
     * array1.equals(shorter);   // returns false
     * }</pre>
     *
     * @param obj the object to compare with this ImmutableIntArray
     * @return {@code true} if the specified object is an ImmutableIntArray with the same elements
     *         in the same order; {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ImmutableIntArray other && N.equals(elements, other.elements);
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
     * // Basic: multi-element array
     * ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] {1, 2, 3});
     * array.toString();   // returns "[1, 2, 3]"
     *
     * // Basic: array with negative values
     * ImmutableIntArray neg = ImmutableIntArray.unsafeWrap(new int[] {-1, 0, 1});
     * neg.toString();   // returns "[-1, 0, 1]"
     *
     * // Edge: empty array
     * ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
     * empty.toString();   // returns "[]"
     *
     * // Edge: single-element array
     * ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] {42});
     * single.toString();   // returns "[42]"
     * }</pre>
     *
     * @return a string representation of this ImmutableIntArray in the format {@code "[element1, element2, ...]"}
     */
    @Override
    public String toString() {
        return N.toString(elements);
    }
}
