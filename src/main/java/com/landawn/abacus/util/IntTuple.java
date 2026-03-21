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

import com.landawn.abacus.annotation.MayReturnNull;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.IntStream;

/**
 * Base class for immutable tuples of primitive {@code int} values.
 *
 * <p>The nested tuple types model fixed arities from 0 through 9. Factory methods such as
 * {@link #copyOf(int[])} and the {@code of(...)} overloads select the matching subtype, while the base
 * class supplies aggregate, reversal, containment, and functional helper operations.</p>
 *
 * @param <TP> the specific IntTuple subtype
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class IntTuple<TP extends IntTuple<TP>> extends PrimitiveTuple<TP> {

    /** Lazily initialized cached array view of all tuple elements. */
    protected volatile int[] elements;

    /**
     * Protected constructor for subclass instantiation.
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link #of(int)}, {@link #of(int, int)}, etc., to create tuple instances.
     */
    protected IntTuple() {
    }

    /**
     * Creates an IntTuple.IntTuple1 containing a single int value.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple1 single = IntTuple.of(42);
     * int value = single._1;  // 42
     * }</pre>
     *
     * @param _1 the int value to store in the tuple
     * @return a new IntTuple.IntTuple1 containing the specified value
     */
    public static IntTuple1 of(final int _1) {
        return new IntTuple1(_1);
    }

    /**
     * Creates an IntTuple.IntTuple2 containing two int values.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple2 pair = IntTuple.of(1, 2);
     * int sum = pair._1 + pair._2;  // 3
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @return a new IntTuple.IntTuple2 containing the specified values
     */
    public static IntTuple2 of(final int _1, final int _2) {
        return new IntTuple2(_1, _2);
    }

    /**
     * Creates an IntTuple.IntTuple3 containing three int values.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 triple = IntTuple.of(1, 2, 3);
     * double average = triple.average();   // 2.0
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @return a new IntTuple.IntTuple3 containing the specified values
     */
    public static IntTuple3 of(final int _1, final int _2, final int _3) {
        return new IntTuple3(_1, _2, _3);
    }

    /**
     * Creates an IntTuple.IntTuple4 containing four int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple4 quad = IntTuple.of(1, 2, 3, 4);
     * // quad._1 == 1, quad._2 == 2, quad._3 == 3, quad._4 == 4
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @return a new IntTuple.IntTuple4 containing the specified values
     */
    public static IntTuple4 of(final int _1, final int _2, final int _3, final int _4) {
        return new IntTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates an IntTuple.IntTuple5 containing five int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple5 quint = IntTuple.of(1, 2, 3, 4, 5);
     * // quint._5 == 5
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @param _5 the fifth int value
     * @return a new IntTuple.IntTuple5 containing the specified values
     */
    public static IntTuple5 of(final int _1, final int _2, final int _3, final int _4, final int _5) {
        return new IntTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates an IntTuple.IntTuple6 containing six int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple6 sext = IntTuple.of(1, 2, 3, 4, 5, 6);
     * // sext._6 == 6
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @param _5 the fifth int value
     * @param _6 the sixth int value
     * @return a new IntTuple.IntTuple6 containing the specified values
     */
    public static IntTuple6 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6) {
        return new IntTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates an IntTuple.IntTuple7 containing seven int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple7 sept = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
     * // sept._7 == 7
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @param _5 the fifth int value
     * @param _6 the sixth int value
     * @param _7 the seventh int value
     * @return a new IntTuple.IntTuple7 containing the specified values
     */
    public static IntTuple7 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7) {
        return new IntTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates an IntTuple.IntTuple8 containing eight int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple8 oct = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
     * // oct._8 == 8
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @param _5 the fifth int value
     * @param _6 the sixth int value
     * @param _7 the seventh int value
     * @param _8 the eighth int value
     * @return a new IntTuple.IntTuple8 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more int values
     */
    @Deprecated
    public static IntTuple8 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8) {
        return new IntTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates an IntTuple.IntTuple9 containing nine int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple9 non = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
     * // non._9 == 9
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @param _5 the fifth int value
     * @param _6 the sixth int value
     * @param _7 the seventh int value
     * @param _8 the eighth int value
     * @param _9 the ninth int value
     * @return a new IntTuple.IntTuple9 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more int values
     */
    @Deprecated
    public static IntTuple9 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8, final int _9) {
        return new IntTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates an IntTuple from an array of int values.
     * <p>
     * The size of the returned tuple depends on the length of the input array.
     * This factory method supports arrays with 0 to 9 elements. For empty or null
     * arrays, returns an empty {@code IntTuple<?>}. For arrays with 1-9 elements, returns
     * the corresponding IntTuple.IntTuple1-9 instance.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create from array
     * int[] values = {1, 2, 3};
     * IntTuple.IntTuple3 tuple = IntTuple.copyOf(values);
     *
     * // Empty array returns IntTuple<?>
     * IntTuple<?> empty = IntTuple.copyOf(new int[0]);
     *
     * // Single element
     * IntTuple.IntTuple1 single = IntTuple.copyOf(new int[]{42});
     * }</pre>
     *
     * <p><strong>Type note:</strong> the runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of int values (must have length 0-9), may be {@code null}
     * @return an IntTuple of appropriate size containing the array values, or an empty IntTuple if the array is null or empty
     * @throws IllegalArgumentException if the array has more than 9 elements
     */
    @SuppressWarnings("deprecation")
    public static <TP extends IntTuple<TP>> TP copyOf(final int[] values) {
        if (values == null || values.length == 0) {
            return (TP) IntTuple0.EMPTY;
        }

        switch (values.length) {
            case 1:
                return (TP) IntTuple.of(values[0]);

            case 2:
                return (TP) IntTuple.of(values[0], values[1]);

            case 3:
                return (TP) IntTuple.of(values[0], values[1], values[2]);

            case 4:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3]);

            case 5:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3], values[4]);

            case 6:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);

            case 7:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);

            case 8:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);

            case 9:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);

            default:
                throw new IllegalArgumentException("Too many elements (" + values.length + "). Maximum: 9");
        }
    }

    /**
     * Returns the minimum int value in this tuple.
     * <p>
     * Iterates through all elements in the tuple and returns the smallest value.
     * For single-element tuples, the element itself is returned.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
     * int min = tuple.min();   // 1
     *
     * IntTuple.IntTuple1 single = IntTuple.of(42);
     * int singleMin = single.min();   // 42
     * }</pre>
     *
     * @return the minimum int value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public int min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum int value in this tuple.
     * <p>
     * Iterates through all elements in the tuple and returns the largest value.
     * For single-element tuples, the element itself is returned.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
     * int max = tuple.max();   // 3
     *
     * IntTuple.IntTuple1 single = IntTuple.of(42);
     * int singleMax = single.max();   // 42
     * }</pre>
     *
     * @return the maximum int value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public int max() {
        return N.max(elements());
    }

    /**
     * Returns the median int value in this tuple.
     * <p>
     * The median is calculated by sorting the elements internally. For tuples with an odd
     * number of elements, returns the middle value when sorted. For tuples with an even
     * number of elements, returns the lower middle value when sorted.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 3, 2);
     * int median = tuple.median();   // 2 (middle value when sorted: 1, 2, 3)
     *
     * IntTuple.IntTuple4 evenTuple = IntTuple.of(1, 2, 3, 4);
     * int evenMedian = evenTuple.median();   // 2 (lower middle when sorted: 1, [2], 3, 4)
     * }</pre>
     *
     * @return the median int value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public int median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all int values in this tuple as an {@code int}.
     * <p>
     * Note: This method does not check for overflow. If the sum exceeds {@code Integer.MAX_VALUE},
     * the result will wrap around according to standard int arithmetic.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * int sum = tuple.sum();   // 6
     *
     * IntTuple.IntTuple2 pair = IntTuple.of(100, 200);
     * int total = pair.sum();  // 300
     * }</pre>
     *
     * @return the sum of all int values in this tuple as an {@code int}
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all int values in this tuple as a double.
     * <p>
     * Note: The result is returned as a double to preserve precision. The average is
     * calculated by converting int values to double during computation.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
     * double avg = tuple.average();   // 2.5
     * }</pre>
     *
     * @return the average of all int values in this tuple as a double
     * @throws NoSuchElementException if the tuple is empty
     */
    public double average() {
        return N.average(elements());
    }

    /**
     * Returns a new tuple with the elements in reverse order.
     * <p>
     * This method creates and returns a new tuple instance with all elements in reversed order.
     * The original tuple remains unchanged as tuples are immutable.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple2 pair = IntTuple.of(1, 2);
     * IntTuple.IntTuple2 reversedPair = pair.reverse();   // (2, 1)
     *
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * IntTuple.IntTuple3 reversed = tuple.reverse();   // (3, 2, 1)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified int value.
     * <p>
     * This method performs a linear search through all elements in the tuple to determine
     * if any element matches the specified value. Returns {@code true} if at least one
     * element equals the search value, {@code false} otherwise.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * boolean has2 = tuple.contains(2);   // true
     * boolean has5 = tuple.contains(5);   // false
     *
     * IntTuple.IntTuple2 pair = IntTuple.of(10, 10);
     * boolean hasTen = pair.contains(10);   // true
     * boolean hasOne = pair.contains(1);    // false
     * }</pre>
     *
     * @param valueToFind the int value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(int valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * <p>
     * Creates and returns a defensive copy of the internal element array. Modifications
     * to the returned array do not affect the tuple, maintaining immutability. The
     * returned array has the same length as the tuple's arity.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * int[] array = tuple.toArray();   // [1, 2, 3]
     * array[0] = 99;  // Does not modify the tuple
     *
     * IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
     * int[] pairArray = pair.toArray();   // [10, 20]
     * }</pre>
     *
     * @return a new int array containing all tuple elements
     */
    public int[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new IntList containing all elements of this tuple.
     * <p>
     * Converts this tuple to a mutable {@link IntList} containing all elements
     * in their original order. The returned list is a new instance and modifications
     * to it do not affect the original tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * IntList list = tuple.toList();
     * list.add(4);   // Adds to the list, tuple remains unchanged
     *
     * IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
     * IntList pairList = pair.toList();   // [10, 20]
     * }</pre>
     *
     * @return a new IntList containing all tuple elements
     */
    public IntList toList() {
        return IntList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     * <p>
     * Iterates through all elements in this tuple in order, executing the provided
     * consumer action for each element. This method is primarily used for side effects
     * such as logging, printing, or updating external state. Because tuples are immutable,
     * the iteration does not modify this tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * tuple.forEach(v -> System.out.println("Value: " + v));
     * // Output: Value: 1, Value: 2, Value: 3
     *
     * // Accumulate sum externally
     * java.util.concurrent.atomic.AtomicInteger total = new java.util.concurrent.atomic.AtomicInteger();
     * tuple.forEach(v -> total.addAndGet(v));
     * // total is now 6
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the consumer
     * @param consumer the action to be performed for each element, must not be {@code null}
     * @throws E if the consumer throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
        for (final int element : elements()) {
            consumer.accept(element);
        }
    }

    /**
     * Returns an IntStream of all elements in this tuple.
     * <p>
     * Converts this tuple to a sequential {@link IntStream} containing all elements
     * in their original order. This allows using standard stream operations like filter,
     * map, and reduce on the tuple elements.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * int sum = tuple.stream().sum();   // 6
     *
     * IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
     * int max = pair.stream().max().getAsInt();   // 20
     * }</pre>
     *
     * @return an IntStream containing all tuple elements
     */
    public IntStream stream() {
        return IntStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * <p>
     * The hash code is computed based on the contents of the tuple using a standard
     * algorithm that ensures equal tuples have equal hash codes. This implementation
     * is consistent with {@link #equals(Object)}.
     * </p>
     *
     * @return a hash code value for this tuple
     */
    @Override
    public int hashCode() {
        return N.hashCode(elements());
    }

    /**
     * Compares this tuple to the specified object for equality.
     *
     * <p>
     * Two tuples are considered equal if and only if:
     * </p>
     *
     * <ul>
     *   <li>They are the same object (reference equality), or</li>
     *   <li>They are instances of the exact same class, and</li>
     *   <li>They contain the same int values in the same order</li>
     * </ul>
     *
     * <p>This method is consistent with {@link #hashCode()}.</p>
     *
     * @param obj the object to be compared for equality with this tuple
     * @return {@code true} if the specified object is equal to this tuple, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        } else {
            return N.equals(elements(), ((IntTuple<TP>) obj).elements());
        }
    }

    /**
     * Returns a string representation of this tuple.
     * <p>
     * The string representation consists of the tuple elements enclosed in parentheses
     * and separated by commas and spaces, in the format {@code (element1, element2, ...)}.
     * This format is consistent across all tuple types and provides a readable representation
     * of the tuple's contents.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <ul>
     *   <li>{@code (1, 2, 3)} for an IntTuple.IntTuple3</li>
     *   <li>{@code (1, 2)} for an IntTuple.IntTuple2</li>
     *   <li>{@code (1)} for an IntTuple.IntTuple1</li>
     *   <li>{@code ()} for an empty {@code IntTuple<?>}</li>
     * </ul>
     *
     * @return a string representation of this tuple
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    /**
     * Returns the cached array view of the tuple contents.
     * <p>
     * Implementations lazily initialize this array on first access and then reuse it on subsequent
     * calls. The returned array is therefore a live internal cache, not a defensive copy.
     * </p>
     *
     * @return the array of int elements stored in this tuple
     */
    protected abstract int[] elements();

    /**
     * An empty tuple containing no elements.
     * This class is used to represent a tuple with zero elements
     * and is returned by {@link #copyOf(int[])} when passed a null or empty array.
     */
    static final class IntTuple0 extends IntTuple<IntTuple0> {

        private static final IntTuple0 EMPTY = new IntTuple0();

        IntTuple0() {
        }

        /**
         * Returns the number of elements in this tuple, which is always 0.
         *
         * @return 0
         */
        @Override
        public int arity() {
            return 0;
        }

        /**
         * Returns the minimum int value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public int min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the maximum int value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public int max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the median int value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public int median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the sum of all values in this tuple as an int.
         * For an empty tuple, the sum is 0.
         *
         * @return 0
         */
        @Override
        public int sum() {
            return 0;
        }

        /**
         * Returns the average of all int values in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public double average() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns a tuple with the elements in reverse order.
         * For an empty tuple, returns itself as there are no elements to reverse.
         *
         * @return this empty tuple
         */
        @Override
        public IntTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified value.
         * An empty tuple contains no values.
         *
         * @param valueToFind the int value to search for
         * @return {@code false} always, as the tuple is empty
         */
        @Override
        public boolean contains(final int valueToFind) {
            return false;
        }

        /**
         * Returns a string representation of this empty tuple.
         *
         * @return "()"
         */
        @Override
        public String toString() {
            return "()";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            return N.EMPTY_INT_ARRAY;
        }
    }

    /**
     * A tuple containing exactly one int value.
     * The value is accessible through the public final field {@code _1}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple1 single = IntTuple.of(42);
     * int value = single._1;  // 42
     * }</pre>
     */
    public static final class IntTuple1 extends IntTuple<IntTuple1> {

        /** The single int value stored in this tuple. */
        public final int _1;

        IntTuple1() {
            this(0);
        }

        IntTuple1(final int _1) {
            this._1 = _1;
        }

        /**
         * Returns the number of elements in this tuple, which is always 1.
         *
         * @return 1
         */
        @Override
        public int arity() {
            return 1;
        }

        /**
         * Returns the minimum value in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * @return the single element value
         */
        @Override
        public int min() {
            return _1;
        }

        /**
         * Returns the maximum value in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * @return the single element value
         */
        @Override
        public int max() {
            return _1;
        }

        /**
         * Returns the median value in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * @return the single element value
         */
        @Override
        public int median() {
            return _1;
        }

        /**
         * Returns the sum of all values in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * @return the single element value
         */
        @Override
        public int sum() {
            return _1;
        }

        /**
         * Returns the average of all values in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * @return the single element value as a double
         */
        @Override
        public double average() {
            return _1;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         * For a single-element tuple, returns a new tuple with the same value.
         *
         * @return a new IntTuple.IntTuple1 with the same value
         */
        @Override
        public IntTuple1 reverse() {
            return new IntTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if the value equals _1, {@code false} otherwise
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind;
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return the hash code of the single element
         */
        @Override
        public int hashCode() {
            return _1;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is an IntTuple.IntTuple1 with the same value
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final IntTuple1 other)) {
                return false;
            } else {
                return _1 == other._1;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return a string in the format "(value)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly two int values.
     * The values are accessible through the public final fields {@code _1} and {@code _2}.
     *
     * <p>This class provides additional functional methods for working with pairs:
     * <ul>
     *   <li>{@link #accept(Throwables.IntBiConsumer)} - consume both values</li>
     *   <li>{@link #map(Throwables.IntBiFunction)} - transform the pair to a single value</li>
     *   <li>{@link #filter(Throwables.IntBiPredicate)} - conditionally wrap in u.Optional</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple2 pair = IntTuple.of(3, 5);
     * int product = pair.map((a, b) -> a * b);   // 15
     * }</pre>
     */
    public static final class IntTuple2 extends IntTuple<IntTuple2> {

        /** The first int value in this tuple. */
        public final int _1;
        /** The second int value in this tuple. */
        public final int _2;

        IntTuple2() {
            this(0, 0);
        }

        IntTuple2(final int _1, final int _2) {
            this._1 = _1;
            this._2 = _2;
        }

        /**
         * Returns the number of elements in this tuple, which is always 2.
         *
         * @return 2
         */
        @Override
        public int arity() {
            return 2;
        }

        /**
         * Returns the minimum value among the two elements.
         *
         * @return the smaller of _1 and _2
         */
        @Override
        public int min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum value among the two elements.
         *
         * @return the larger of _1 and _2
         */
        @Override
        public int max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median int value in this tuple.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower) int value
         */
        @Override
        public int median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of the two elements as an int.
         *
         * @return _1 + _2 as an int
         */
        @Override
        public int sum() {
            return N.sum(_1, _2);
        }

        /**
         * Returns the average of the two elements.
         *
         * @return (_1 + _2) / 2.0 as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple.IntTuple2 with values (_2, _1)
         */
        @Override
        public IntTuple2 reverse() {
            return new IntTuple2(_2, _1);
        }

        /**
         * Checks if either element equals the specified value.
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals _1 or _2
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform on each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         * Performs the given bi-consumer action on the two elements of this tuple.
         * <p>
         * This is a convenience method that passes both elements (_1 and _2) to the
         * provided bi-consumer action. It's useful for operations that need to process
         * both values together.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple2 tuple = IntTuple.of(3, 4);
         * tuple.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         *
         * IntTuple.IntTuple2 coordinates = IntTuple.of(10, 20);
         * coordinates.accept((x, y) -> System.out.println("Point at (" + x + ", " + y + ")"));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the bi-consumer to perform on the two elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.IntBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given bi-function to the two elements and returns the result.
         * <p>
         * This method transforms the pair of int values into a single value of type U
         * using the provided mapper function. The mapper receives both _1 and _2 as arguments
         * and can return any type, including primitive wrapper types, objects, or null.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple2 tuple = IntTuple.of(3, 4);
         * int product = tuple.map((a, b) -> a * b);   // 12
         *
         * IntTuple.IntTuple2 dimensions = IntTuple.of(5, 10);
         * String desc = dimensions.map((w, h) -> w + "x" + h);  // "5x10"
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the bi-function to apply to the two elements
         * @return the result of applying the mapper function, may be {@code null}
         * @throws E if the mapper throws an exception
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.IntBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         * <p>
         * This method tests the two elements using the provided bi-predicate. If the predicate
         * returns {@code true}, an Optional containing this tuple is returned. Otherwise, an
         * empty Optional is returned. This is useful for conditional processing in functional chains.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple2 tuple = IntTuple.of(3, 4);
         * u.Optional<IntTuple.IntTuple2> result = tuple.filter((a, b) -> a + b > 5);   // Optional containing the pair
         *
         * IntTuple.IntTuple2 small = IntTuple.of(1, 2);
         * u.Optional<IntTuple.IntTuple2> empty = small.filter((a, b) -> a + b > 10);  // Empty Optional
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the bi-predicate to test the two elements
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<IntTuple2> filter(final Throwables.IntBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return 31 * _1 + _2
         */
        @Override
        public int hashCode() {
            return 31 * _1 + _2;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is an IntTuple.IntTuple2 with the same values
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final IntTuple2 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return a string in the format "(_1, _2)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly three int values.
     * The values are accessible through the public final fields {@code _1}, {@code _2}, and {@code _3}.
     *
     * <p>This class provides additional functional methods for working with triples:
     * <ul>
     *   <li>{@link #accept(Throwables.IntTriConsumer)} - consume all three values</li>
     *   <li>{@link #map(Throwables.IntTriFunction)} - transform the triple to a single value</li>
     *   <li>{@link #filter(Throwables.IntTriPredicate)} - conditionally wrap in u.Optional</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 triple = IntTuple.of(2, 3, 5);
     * int sum = triple.map((a, b, c) -> a + b + c);   // 10
     * }</pre>
     */
    public static final class IntTuple3 extends IntTuple<IntTuple3> {

        /** The first int value in this tuple. */
        public final int _1;
        /** The second int value in this tuple. */
        public final int _2;
        /** The third int value in this tuple. */
        public final int _3;

        IntTuple3() {
            this(0, 0, 0);
        }

        IntTuple3(final int _1, final int _2, final int _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        /**
         * Returns the number of elements in this tuple, which is always 3.
         *
         * @return 3
         */
        @Override
        public int arity() {
            return 3;
        }

        /**
         * Returns the minimum value among the three elements.
         *
         * @return the smallest of _1, _2, and _3
         */
        @Override
        public int min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum value among the three elements.
         *
         * @return the largest of _1, _2, and _3
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median value of the three elements.
         *
         * @return the middle int value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of all three elements as an int.
         *
         * @return _1 + _2 + _3 as an int
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3);
        }

        /**
         * Returns the average of all three elements.
         *
         * @return (_1 + _2 + _3) / 3.0 as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple.IntTuple3 with values (_3, _2, _1)
         */
        @Override
        public IntTuple3 reverse() {
            return new IntTuple3(_3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals _1, _2, or _3
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform on each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
        }

        /**
         * Performs the given tri-consumer action on the three elements of this tuple.
         * <p>
         * This is a convenience method that passes all three elements (_1, _2, and _3) to the
         * provided tri-consumer action. It's useful for operations that need to process
         * all three values together.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
         * tuple.accept((a, b, c) -> System.out.println("Sum: " + (a + b + c)));
         *
         * IntTuple.IntTuple3 rgb = IntTuple.of(255, 128, 64);
         * rgb.accept((r, g, b) -> System.out.println("RGB(" + r + ", " + g + ", " + b + ")"));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the tri-consumer to perform on the three elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.IntTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given tri-function to the three elements and returns the result.
         * <p>
         * This method transforms the three int values into a single value of type U
         * using the provided mapper function. The mapper receives all three elements
         * (_1, _2, and _3) as arguments and can return any type, including primitive
         * wrapper types, objects, or null.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
         * int product = tuple.map((a, b, c) -> a * b * c);   // 6
         *
         * IntTuple.IntTuple3 dimensions = IntTuple.of(10, 20, 30);
         * String formatted = dimensions.map((x, y, z) -> x + "x" + y + "x" + z);  // "10x20x30"
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the tri-function to apply to the three elements
         * @return the result of applying the mapper function, may be {@code null}
         * @throws E if the mapper throws an exception
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.IntTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         * <p>
         * This method tests the three elements using the provided tri-predicate. If the predicate
         * returns {@code true}, an Optional containing this tuple is returned. Otherwise, an
         * empty Optional is returned. This is useful for conditional processing in functional chains.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple3 triple = IntTuple.of(1, 2, 3);
         * u.Optional<IntTuple.IntTuple3> result = triple.filter((a, b, c) -> a < b && b < c);   // Optional containing the triple
         *
         * IntTuple.IntTuple3 descending = IntTuple.of(3, 2, 1);
         * u.Optional<IntTuple.IntTuple3> empty = descending.filter((a, b, c) -> a < b && b < c);  // Empty Optional
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the tri-predicate to test the three elements
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<IntTuple3> filter(final Throwables.IntTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return (31 * (31 * _1 + _2)) + _3
         */
        @Override
        public int hashCode() {
            return (31 * (31 * _1 + _2)) + _3;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is an IntTuple.IntTuple3 with the same values
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final IntTuple3 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return a string in the format "(_1, _2, _3)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly four int values.
     * The values are accessible through the public final fields {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple4 quad = IntTuple.of(1, 2, 3, 4);
     * double avg = quad.average();   // 2.5
     * }</pre>
     */
    public static final class IntTuple4 extends IntTuple<IntTuple4> {

        /** The first int value in this tuple. */
        public final int _1;
        /** The second int value in this tuple. */
        public final int _2;
        /** The third int value in this tuple. */
        public final int _3;
        /** The fourth int value in this tuple. */
        public final int _4;

        IntTuple4() {
            this(0, 0, 0, 0);
        }

        IntTuple4(final int _1, final int _2, final int _3, final int _4) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
        }

        /**
         * Returns the number of elements in this tuple, which is always 4.
         *
         * @return 4
         */
        @Override
        public int arity() {
            return 4;
        }

        /**
         * Returns the minimum value among the four elements.
         *
         * @return the smallest of _1, _2, _3, and _4
         */
        @Override
        public int min() {
            return N.min(_1, _2, _3, _4);
        }

        /**
         * Returns the maximum value among the four elements.
         *
         * @return the largest of _1, _2, _3, and _4
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the median value of the four elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) int value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of all four elements as an int.
         *
         * @return _1 + _2 + _3 + _4 as an int
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4);
        }

        /**
         * Returns the average of all four elements.
         *
         * @return (_1 + _2 + _3 + _4) / 4.0 as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple.IntTuple4 with values (_4, _3, _2, _1)
         */
        @Override
        public IntTuple4 reverse() {
            return new IntTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the four elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform on each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all four elements.
         *
         * @return a hash code based on all four elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * _1 + _2) + _3)) + _4;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple4 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple4 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final IntTuple4 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4;
            }
        }

        /**
         * Returns a string representation of this tuple.
         * The format is (_1, _2, _3, _4) where each element is displayed in order.
         *
         * @return a string representation in the format "(_1, _2, _3, _4)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly five int values.
     * The values are accessible through the public final fields {@code _1} through {@code _5}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
     * int median = tuple.median();   // 3
     * }</pre>
     */
    public static final class IntTuple5 extends IntTuple<IntTuple5> {

        /** The first int value in this tuple. */
        public final int _1;
        /** The second int value in this tuple. */
        public final int _2;
        /** The third int value in this tuple. */
        public final int _3;
        /** The fourth int value in this tuple. */
        public final int _4;
        /** The fifth int value in this tuple. */
        public final int _5;

        IntTuple5() {
            this(0, 0, 0, 0, 0);
        }

        IntTuple5(final int _1, final int _2, final int _3, final int _4, final int _5) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
        }

        /**
         * Returns the number of elements in this tuple, which is always 5.
         *
         * @return 5
         */
        @Override
        public int arity() {
            return 5;
        }

        /**
         * Returns the minimum value among the five elements.
         *
         * @return the smallest of _1, _2, _3, _4, and _5
         */
        @Override
        public int min() {
            return N.min(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the maximum value among the five elements.
         *
         * @return the largest of _1, _2, _3, _4, and _5
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the median value of the five elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median int value
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of all five elements as an int.
         *
         * @return _1 + _2 + _3 + _4 + _5 as an int
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the average of all five elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5) / 5.0 as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple.IntTuple5 with values (_5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple5 reverse() {
            return new IntTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the five elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform on each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all five elements.
         *
         * @return a hash code based on all five elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4)) + _5;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple5 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple5 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final IntTuple5 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4 && _5 == other._5;
            }
        }

        /**
         * Returns a string representation of this tuple.
         * The format is (_1, _2, _3, _4, _5) where each element is displayed in order.
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly six int values.
     * The values are accessible through the public final fields {@code _1} through {@code _6}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
     * int sum = tuple.sum();   // 21
     * }</pre>
     */
    public static final class IntTuple6 extends IntTuple<IntTuple6> {

        /** The first int value in this tuple. */
        public final int _1;
        /** The second int value in this tuple. */
        public final int _2;
        /** The third int value in this tuple. */
        public final int _3;
        /** The fourth int value in this tuple. */
        public final int _4;
        /** The fifth int value in this tuple. */
        public final int _5;
        /** The sixth int value in this tuple. */
        public final int _6;

        IntTuple6() {
            this(0, 0, 0, 0, 0, 0);
        }

        IntTuple6(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
        }

        /**
         * Returns the number of elements in this tuple, which is always 6.
         *
         * @return 6
         */
        @Override
        public int arity() {
            return 6;
        }

        /**
         * Returns the minimum value among the six elements.
         *
         * @return the smallest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public int min() {
            return N.min(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the maximum value among the six elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median value of the six elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) int value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of all six elements as an int.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 as an int
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the average of all six elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6) / 6.0 as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple.IntTuple6 with values (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple6 reverse() {
            return new IntTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the six elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform on each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all six elements.
         *
         * @return a hash code based on all six elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5)) + _6;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple6 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple6 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final IntTuple6 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4 && _5 == other._5 && _6 == other._6;
            }
        }

        /**
         * Returns a string representation of this tuple.
         * The format is (_1, _2, _3, _4, _5, _6) where each element is displayed in order.
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly seven int values.
     * The values are accessible through the public final fields {@code _1} through {@code _7}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
     * IntTuple.IntTuple7 reversed = tuple.reverse();   // (7, 6, 5, 4, 3, 2, 1)
     * }</pre>
     */
    public static final class IntTuple7 extends IntTuple<IntTuple7> {

        /** The first int value in this tuple. */
        public final int _1;
        /** The second int value in this tuple. */
        public final int _2;
        /** The third int value in this tuple. */
        public final int _3;
        /** The fourth int value in this tuple. */
        public final int _4;
        /** The fifth int value in this tuple. */
        public final int _5;
        /** The sixth int value in this tuple. */
        public final int _6;
        /** The seventh int value in this tuple. */
        public final int _7;

        IntTuple7() {
            this(0, 0, 0, 0, 0, 0, 0);
        }

        IntTuple7(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
        }

        /**
         * Returns the number of elements in this tuple, which is always 7.
         *
         * @return 7
         */
        @Override
        public int arity() {
            return 7;
        }

        /**
         * Returns the minimum value among the seven elements.
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public int min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the maximum value among the seven elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the median value of the seven elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median int value
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of all seven elements as an int.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 as an int
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the average of all seven elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7) / 7.0 as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple.IntTuple7 with values (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple7 reverse() {
            return new IntTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the seven elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform on each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
            consumer.accept(_7);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all seven elements.
         *
         * @return a hash code based on all seven elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6)) + _7;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple7 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple7 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final IntTuple7 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4 && _5 == other._5 && _6 == other._6 && _7 == other._7;
            }
        }

        /**
         * Returns a string representation of this tuple.
         * The format is (_1, _2, _3, _4, _5, _6, _7) where each element is displayed in order.
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6, _7)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly eight int values.
     * The values are accessible through the public final fields {@code _1} through {@code _8}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
     * boolean contains5 = tuple.contains(5);   // true
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more int values
     */
    @Deprecated
    public static final class IntTuple8 extends IntTuple<IntTuple8> {

        /** The first int value in this tuple. */
        public final int _1;
        /** The second int value in this tuple. */
        public final int _2;
        /** The third int value in this tuple. */
        public final int _3;
        /** The fourth int value in this tuple. */
        public final int _4;
        /** The fifth int value in this tuple. */
        public final int _5;
        /** The sixth int value in this tuple. */
        public final int _6;
        /** The seventh int value in this tuple. */
        public final int _7;
        /** The eighth int value in this tuple. */
        public final int _8;

        IntTuple8() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }

        IntTuple8(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
            this._8 = _8;
        }

        /**
         * Returns the number of elements in this tuple, which is always 8.
         *
         * @return 8
         */
        @Override
        public int arity() {
            return 8;
        }

        /**
         * Returns the minimum value among the eight elements.
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, _7, and _8
         */
        @Override
        public int min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the maximum value among the eight elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, and _8
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the median value of the eight elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) int value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of all eight elements as an int.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 as an int
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the average of all eight elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8) / 8.0 as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple.IntTuple8 with values (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple8 reverse() {
            return new IntTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the eight elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform on each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
            consumer.accept(_7);
            consumer.accept(_8);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all eight elements.
         *
         * @return a hash code based on all eight elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6) + _7)) + _8;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple8 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple8 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final IntTuple8 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4 && _5 == other._5 && _6 == other._6 && _7 == other._7
                        && _8 == other._8;
            }
        }

        /**
         * Returns a string representation of this tuple.
         * The format is (_1, _2, _3, _4, _5, _6, _7, _8) where each element is displayed in order.
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6, _7, _8)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly nine int values.
     * The values are accessible through the public final fields {@code _1} through {@code _9}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
     * double avg = tuple.average();   // 5.0
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more int values
     */
    @Deprecated
    public static final class IntTuple9 extends IntTuple<IntTuple9> {

        /** The first int value in this tuple. */
        public final int _1;
        /** The second int value in this tuple. */
        public final int _2;
        /** The third int value in this tuple. */
        public final int _3;
        /** The fourth int value in this tuple. */
        public final int _4;
        /** The fifth int value in this tuple. */
        public final int _5;
        /** The sixth int value in this tuple. */
        public final int _6;
        /** The seventh int value in this tuple. */
        public final int _7;
        /** The eighth int value in this tuple. */
        public final int _8;
        /** The ninth int value in this tuple. */
        public final int _9;

        IntTuple9() {
            this(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        IntTuple9(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8, final int _9) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
            this._8 = _8;
            this._9 = _9;
        }

        /**
         * Returns the number of elements in this tuple, which is always 9.
         *
         * @return 9
         */
        @Override
        public int arity() {
            return 9;
        }

        /**
         * Returns the minimum value among the nine elements.
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, _7, _8, and _9
         */
        @Override
        public int min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the maximum value among the nine elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, _8, and _9
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the median value of the nine elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median int value
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of all nine elements as an int.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9 as an int
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the average of all nine elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9) / 9.0 as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple.IntTuple9 with values (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple9 reverse() {
            return new IntTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the nine elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform on each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
            consumer.accept(_7);
            consumer.accept(_8);
            consumer.accept(_9);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all nine elements.
         *
         * @return a hash code based on all nine elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6) + _7) + _8)) + _9;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple9 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple9 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final IntTuple9 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4 && _5 == other._5 && _6 == other._6 && _7 == other._7
                        && _8 == other._8 && _9 == other._9;
            }
        }

        /**
         * Returns a string representation of this tuple.
         * The format is (_1, _2, _3, _4, _5, _6, _7, _8, _9) where each element is displayed in order.
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6, _7, _8, _9)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
