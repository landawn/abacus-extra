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
import com.landawn.abacus.util.stream.LongStream;

/**
 * Base class for immutable tuples of primitive {@code long} values.
 *
 * <p>The nested tuple types in this class provide fixed-arity carriers together with aggregate and
 * functional helper methods.</p>
 *
 * @param <TP> the specific LongTuple subtype
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class LongTuple<TP extends LongTuple<TP>> extends PrimitiveTuple<TP> {

    /**
     * Protected constructor for subclass instantiation.
     * <p>
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link #of(long)}, {@link #of(long, long)}, etc.,
     * to create tuple instances.
     */
    protected LongTuple() {
    }

    protected volatile long[] elements;

    /**
     * Creates a LongTuple.LongTuple1 containing a single long value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple1 single = LongTuple.of(42L);
     * long value = single._1;  // 42
     * }</pre>
     *
     * @param _1 the long value to store in the tuple
     * @return a new LongTuple.LongTuple1 containing the specified value
     */
    public static LongTuple1 of(final long _1) {
        return new LongTuple1(_1);
    }

    /**
     * Creates a LongTuple.LongTuple2 containing two long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple2 pair = LongTuple.of(1L, 2L);
     * long sum = pair._1 + pair._2;  // 3
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @return a new LongTuple.LongTuple2 containing the specified values
     */
    public static LongTuple2 of(final long _1, final long _2) {
        return new LongTuple2(_1, _2);
    }

    /**
     * Creates a LongTuple.LongTuple3 containing three long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
     * double average = triple.average();   // 2.0
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @return a new LongTuple.LongTuple3 containing the specified values
     */
    public static LongTuple3 of(final long _1, final long _2, final long _3) {
        return new LongTuple3(_1, _2, _3);
    }

    /**
     * Creates a LongTuple.LongTuple4 containing four long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
     * // quad._1 == 1, quad._2 == 2, quad._3 == 3, quad._4 == 4
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @return a new LongTuple.LongTuple4 containing the specified values
     */
    public static LongTuple4 of(final long _1, final long _2, final long _3, final long _4) {
        return new LongTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a LongTuple.LongTuple5 containing five long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple5 quint = LongTuple.of(1L, 2L, 3L, 4L, 5L);
     * // quint._5 == 5
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @return a new LongTuple.LongTuple5 containing the specified values
     */
    public static LongTuple5 of(final long _1, final long _2, final long _3, final long _4, final long _5) {
        return new LongTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a LongTuple.LongTuple6 containing six long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple6 sext = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
     * // sext._6 == 6
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @param _6 the sixth long value
     * @return a new LongTuple.LongTuple6 containing the specified values
     */
    public static LongTuple6 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6) {
        return new LongTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a LongTuple.LongTuple7 containing seven long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple7 sept = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
     * // sept._7 == 7
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @param _6 the sixth long value
     * @param _7 the seventh long value
     * @return a new LongTuple.LongTuple7 containing the specified values
     */
    public static LongTuple7 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7) {
        return new LongTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a LongTuple.LongTuple8 containing eight long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple8 oct = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
     * // oct._8 == 8
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @param _6 the sixth long value
     * @param _7 the seventh long value
     * @param _8 the eighth long value
     * @return a new LongTuple.LongTuple8 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more long values
     */
    @Deprecated
    public static LongTuple8 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8) {
        return new LongTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a LongTuple.LongTuple9 containing nine long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple9 non = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
     * // non._9 == 9
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @param _6 the sixth long value
     * @param _7 the seventh long value
     * @param _8 the eighth long value
     * @param _9 the ninth long value
     * @return a new LongTuple.LongTuple9 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more long values
     */
    @Deprecated
    public static LongTuple9 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8,
            final long _9) {
        return new LongTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a LongTuple from an array of long values.
     * <p>
     * The size of the returned tuple depends on the length of the input array.
     * This factory method supports arrays with 0 to 9 elements. For empty or null
     * arrays, returns an empty {@code LongTuple<?>}. For arrays with 1-9 elements, returns
     * the corresponding LongTuple.LongTuple1-9 instance.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create from array
     * long[] values = {1L, 2L, 3L};
     * LongTuple.LongTuple3 tuple = LongTuple.copyOf(values);
     *
     * // Empty array returns LongTuple<?>
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     *
     * // Single element
     * LongTuple.LongTuple1 single = LongTuple.copyOf(new long[]{42L});
     * }</pre>
     *
     * <p><strong>Type note:</strong> the runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of long values (must have length 0-9), may be {@code null}
     * @return a LongTuple of appropriate size containing the array values, or an empty LongTuple if the array is null or empty
     * @throws IllegalArgumentException if the array has more than 9 elements
     */
    @SuppressWarnings("deprecation")
    public static <TP extends LongTuple<TP>> TP copyOf(final long[] values) {
        if (values == null || values.length == 0) {
            return (TP) LongTuple0.EMPTY;
        }

        switch (values.length) {
            case 1:
                return (TP) LongTuple.of(values[0]);

            case 2:
                return (TP) LongTuple.of(values[0], values[1]);

            case 3:
                return (TP) LongTuple.of(values[0], values[1], values[2]);

            case 4:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3]);

            case 5:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3], values[4]);

            case 6:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);

            case 7:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);

            case 8:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);

            case 9:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);

            default:
                throw new IllegalArgumentException("Too many elements (" + values.length + "). Maximum: 9");
        }
    }

    /**
     * Returns the minimum long value in this tuple.
     * <p>
     * Iterates through all elements in the tuple and returns the smallest value.
     * For single-element tuples, the element itself is returned.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
     * long min = tuple.min();   // 1
     *
     * LongTuple.LongTuple1 single = LongTuple.of(42L);
     * long singleMin = single.min();   // 42
     * }</pre>
     *
     * @return the minimum long value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public long min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum long value in this tuple.
     * <p>
     * Iterates through all elements in the tuple and returns the largest value.
     * For single-element tuples, the element itself is returned.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
     * long max = tuple.max();   // 3
     *
     * LongTuple.LongTuple1 single = LongTuple.of(42L);
     * long singleMax = single.max();   // 42
     * }</pre>
     *
     * @return the maximum long value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public long max() {
        return N.max(elements());
    }

    /**
     * Returns the median long value in this tuple.
     * <p>
     * The median is calculated by sorting the elements internally. For tuples with an odd
     * number of elements, returns the middle value when sorted. For tuples with an even
     * number of elements, returns the lower middle value when sorted.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
     * long median = tuple.median();   // 2
     *
     * LongTuple.LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
     * long median2 = quad.median();   // 2 (lower middle value)
     * }</pre>
     *
     * @return the median long value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public long median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all elements in this tuple as a {@code long}.
     * <p>
     * Note: This method does not check for overflow. If the sum exceeds {@code Long.MAX_VALUE},
     * the result will wrap around according to standard long arithmetic.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * long sum = tuple.sum();   // 6
     *
     * LongTuple.LongTuple2 pair = LongTuple.of(100L, 200L);
     * long total = pair.sum();  // 300
     * }</pre>
     *
     * @return the sum of all long values in this tuple as a {@code long}
     */
    public long sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all long values in this tuple as a double.
     * <p>
     * Note: The result is returned as a double to preserve precision. The average is
     * calculated by converting long values to double during computation.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * double avg = tuple.average();   // 2.0
     * }</pre>
     *
     * @return the average of all long values in this tuple as a double
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
     * LongTuple.LongTuple2 pair = LongTuple.of(1L, 2L);
     * LongTuple.LongTuple2 reversedPair = pair.reverse();   // (2, 1)
     *
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * LongTuple.LongTuple3 reversed = tuple.reverse();   // (3, 2, 1)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified long value.
     * <p>
     * This method performs a linear search through all elements in the tuple to determine
     * if any element matches the specified value. Returns {@code true} if at least one
     * element equals the search value, {@code false} otherwise.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * boolean has2 = tuple.contains(2L);   // true
     * boolean has5 = tuple.contains(5L);   // false
     *
     * LongTuple.LongTuple2 pair = LongTuple.of(10L, 10L);
     * boolean hasTen = pair.contains(10L);   // true
     * boolean hasOne = pair.contains(1L);    // false
     * }</pre>
     *
     * @param valueToFind the long value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(long valueToFind);

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
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * long[] array = tuple.toArray();   // [1, 2, 3]
     * array[0] = 99L;  // Does not modify the tuple
     *
     * LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
     * long[] pairArray = pair.toArray();   // [10, 20]
     * }</pre>
     *
     * @return a new long array containing all tuple elements
     */
    public long[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new LongList containing all elements of this tuple.
     * <p>
     * Converts this tuple to a mutable {@link LongList} containing all elements
     * in their original order. The returned list is a new instance and modifications
     * to it do not affect the original tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * LongList list = tuple.toList();
     * list.add(4L);   // Adds to the list, tuple remains unchanged
     *
     * LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
     * LongList pairList = pair.toList();   // [10, 20]
     * }</pre>
     *
     * @return a new LongList containing all tuple elements
     */
    public LongList toList() {
        return LongList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     * <p>
     * Iterates through all elements in this tuple in order, executing the provided
     * consumer action for each element. This method is primarily used for side effects
     * such as logging, printing, or updating external state. The tuple itself is not
     * modified as it is immutable.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * tuple.forEach(v -> System.out.println("Value: " + v));
     * // Output: Value: 1, Value: 2, Value: 3
     *
     * // Accumulate sum externally
     * java.util.concurrent.atomic.AtomicLong total = new java.util.concurrent.atomic.AtomicLong();
     * tuple.forEach(v -> total.addAndGet(v));
     * // total is now 6
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the consumer
     * @param consumer the action to be performed for each element, must not be {@code null}
     * @throws E if the consumer throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.LongConsumer<E> consumer) throws E {
        for (final long element : elements()) {
            consumer.accept(element);
        }
    }

    /**
     * Returns a LongStream of all elements in this tuple.
     * <p>
     * Converts this tuple to a sequential {@link LongStream} containing all elements
     * in their original order. This allows using standard stream operations like filter,
     * map, and reduce on the tuple elements.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * long sum = tuple.stream().sum();   // 6
     *
     * LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
     * long max = pair.stream().max().getAsLong();   // 20
     * }</pre>
     *
     * @return a LongStream containing all tuple elements
     */
    public LongStream stream() {
        return LongStream.of(elements());
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
     *   <li>They contain the same long values in the same order</li>
     * </ul>
     * This method is consistent with {@link #hashCode()}.
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
            return N.equals(elements(), ((LongTuple<TP>) obj).elements());
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
     *   <li>{@code (1, 2, 3)} for a LongTuple.LongTuple3</li>
     *   <li>{@code (1, 2)} for a LongTuple.LongTuple2</li>
     *   <li>{@code (1)} for a LongTuple.LongTuple1</li>
     *   <li>{@code ()} for an empty {@code LongTuple<?>}</li>
     * </ul>
     *
     * @return a string representation of this tuple
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    /**
     * Returns the internal array of elements in this tuple.
     * <p>
     * This method is used internally by the tuple implementation to access the
     * underlying array of long values. The returned array is lazily initialized
     * on first access and cached for subsequent calls.
     * </p>
     * <p>
     * Subclasses must implement this method to provide access to their elements.
     * Modifications to the returned array will affect the tuple's internal state.
     * </p>
     *
     * @return the array of long elements stored in this tuple
     */
    protected abstract long[] elements();

    /**
     * An empty tuple containing no elements.
     * This class is used to represent a tuple with zero elements
     * and is returned by {@link #copyOf(long[])} when passed a null or empty array.
     */
    static final class LongTuple0 extends LongTuple<LongTuple0> {

        private static final LongTuple0 EMPTY = new LongTuple0();

        LongTuple0() {
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
         * Returns the minimum long value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public long min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the maximum long value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public long max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the median long value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public long median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the sum of all values in this tuple.
         * For an empty tuple, the sum is 0.
         *
         * @return 0
         */
        @Override
        public long sum() {
            return 0;
        }

        /**
         * Returns the average of all long values in this tuple.
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
        public LongTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified value.
         * An empty tuple contains no values.
         *
         * @param valueToFind the long value to search for
         * @return {@code false} always, as the tuple is empty
         */
        @Override
        public boolean contains(final long valueToFind) {
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

        @Override
        protected long[] elements() {
            return N.EMPTY_LONG_ARRAY;
        }
    }

    /**
     * A tuple containing exactly one long value.
     * The value is accessible through the public final field {@code _1}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple1 single = LongTuple.of(42L);
     * long value = single._1;  // 42
     * }</pre>
     */
    public static final class LongTuple1 extends LongTuple<LongTuple1> {

        /** The single long value stored in this tuple. */
        public final long _1;

        LongTuple1() {
            this(0);
        }

        LongTuple1(final long _1) {
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
        public long min() {
            return _1;
        }

        /**
         * Returns the maximum value in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * @return the single element value
         */
        @Override
        public long max() {
            return _1;
        }

        /**
         * Returns the median value in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * @return the single element value
         */
        @Override
        public long median() {
            return _1;
        }

        /**
         * Returns the sum of all values in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * @return the single element value
         */
        @Override
        public long sum() {
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
         * @return a new LongTuple.LongTuple1 with the same value
         */
        @Override
        public LongTuple1 reverse() {
            return new LongTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if the value equals _1, {@code false} otherwise
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind;
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return the hash code of the single element
         */
        @Override
        public int hashCode() {
            return Long.hashCode(_1);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is a LongTuple.LongTuple1 with the same value
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple1 other)) {
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

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly two long values.
     * The values are accessible through the public final fields {@code _1} and {@code _2}.
     *
     * <p>This class provides additional functional methods for working with pairs:
     * <ul>
     *   <li>{@link #accept(Throwables.LongBiConsumer)} - consume both values</li>
     *   <li>{@link #map(Throwables.LongBiFunction)} - transform the pair to a single value</li>
     *   <li>{@link #filter(Throwables.LongBiPredicate)} - conditionally wrap in u.Optional</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
     * pair.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
     * long product = pair.map((a, b) -> a * b);
     * }</pre>
     */
    public static final class LongTuple2 extends LongTuple<LongTuple2> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;

        LongTuple2() {
            this(0, 0);
        }

        LongTuple2(final long _1, final long _2) {
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
        public long min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum value among the two elements.
         *
         * @return the larger of _1 and _2
         */
        @Override
        public long max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median long value in this tuple.
         * For tuples with an even number of elements, returns the lower middle value.
         *
         * @return the median (lower middle) long value
         */
        @Override
        public long median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of the two elements.
         *
         * @return _1 + _2 as a long
         */
        @Override
        public long sum() {
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
         * @return a new LongTuple.LongTuple2 with values (_2, _1)
         */
        @Override
        public LongTuple2 reverse() {
            return new LongTuple2(_2, _1);
        }

        /**
         * Checks if either element equals the specified value.
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals _1 or _2
         */
        @Override
        public boolean contains(final long valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> consumer) throws E {
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
         * LongTuple.LongTuple2 pair = LongTuple.of(3L, 4L);
         * pair.accept((a, b) -> System.out.println("Distance: " + Math.sqrt(a*a + b*b)));
         *
         * LongTuple.LongTuple2 coordinates = LongTuple.of(10L, 20L);
         * coordinates.accept((x, y) -> System.out.println("Sheet.Point at (" + x + ", " + y + ")"));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the bi-consumer to perform on the two elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.LongBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given bi-function to the two elements and returns the result.
         * <p>
         * This method transforms the pair of long values into a single value of type U
         * using the provided mapper function. The mapper receives both _1 and _2 as arguments
         * and can return any type, including primitive wrapper types, objects, or null.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 pair = LongTuple.of(10L, 3L);
         * long remainder = pair.map((a, b) -> a % b);   // 1
         *
         * LongTuple.LongTuple2 dimensions = LongTuple.of(5L, 10L);
         * Long area = dimensions.map((width, height) -> width * height);  // 50
         *
         * LongTuple.LongTuple2 coords = LongTuple.of(3L, 4L);
         * String result = coords.map((x, y) -> "(" + x + ", " + y + ")");  // "(3, 4)"
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the bi-function to apply to the two elements
         * @return the result of applying the mapper function, may be {@code null}
         * @throws E if the mapper throws an exception
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.LongBiFunction<U, E> mapper) throws E {
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
         * LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
         * u.Optional<LongTuple.LongTuple2> result = pair.filter((a, b) -> a < b);   // Optional containing the tuple
         *
         * LongTuple.LongTuple2 values = LongTuple.of(5L, 3L);
         * u.Optional<LongTuple.LongTuple2> empty = values.filter((a, b) -> a < b);  // Empty Optional
         *
         * LongTuple.LongTuple2 coords = LongTuple.of(10L, 10L);
         * coords.filter((x, y) -> x == y)
         *       .ifPresent(t -> System.out.println("Equal values!"));
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the bi-predicate to test the two elements
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<LongTuple2> filter(final Throwables.LongBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return 31 * Long.hashCode(_1) + Long.hashCode(_2)
         */
        @Override
        public int hashCode() {
            return 31 * Long.hashCode(_1) + Long.hashCode(_2);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is a LongTuple.LongTuple2 with the same values
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple2 other)) {
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

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly three long values.
     * The values are accessible through the public final fields {@code _1}, {@code _2}, and {@code _3}.
     *
     * <p>This class provides additional functional methods for working with triples:
     * <ul>
     *   <li>{@link #accept(Throwables.LongTriConsumer)} - consume all three values</li>
     *   <li>{@link #map(Throwables.LongTriFunction)} - transform the triple to a single value</li>
     *   <li>{@link #filter(Throwables.LongTriPredicate)} - conditionally wrap in u.Optional</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
     * triple.accept((a, b, c) -> System.out.println("Sum: " + (a + b + c)));
     * long product = triple.map((a, b, c) -> a * b * c);
     * }</pre>
     */
    public static final class LongTuple3 extends LongTuple<LongTuple3> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;

        LongTuple3() {
            this(0, 0, 0);
        }

        LongTuple3(final long _1, final long _2, final long _3) {
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
        public long min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum value among the three elements.
         *
         * @return the largest of _1, _2, and _3
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median value of the three elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median long value
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of all three elements.
         *
         * @return _1 + _2 + _3 as a long
         */
        @Override
        public long sum() {
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
         * @return a new LongTuple.LongTuple3 with values (_3, _2, _1)
         */
        @Override
        public LongTuple3 reverse() {
            return new LongTuple3(_3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals _1, _2, or _3
         */
        @Override
        public boolean contains(final long valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> consumer) throws E {
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
         * LongTuple.LongTuple3 triple = LongTuple.of(3L, 4L, 5L);
         * triple.accept((a, b, c) -> {
         *     if (a*a + b*b == c*c) System.out.println("Pythagorean triple!");
         * });
         *
         * LongTuple.LongTuple3 rgb = LongTuple.of(255L, 128L, 64L);
         * rgb.accept((r, g, b) -> System.out.println("RGB(" + r + ", " + g + ", " + b + ")"));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the action to perform on the three elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.LongTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given tri-function to the three elements and returns the result.
         * <p>
         * This method transforms the three long values into a single value of type U
         * using the provided mapper function. The mapper receives all three elements
         * (_1, _2, and _3) as arguments and can return any type, including primitive
         * wrapper types, objects, or null.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 triple = LongTuple.of(2L, 3L, 4L);
         * long volume = triple.map((l, w, h) -> l * w * h);   // 24
         *
         * LongTuple.LongTuple3 dimensions = LongTuple.of(10L, 20L, 30L);
         * String formatted = dimensions.map((x, y, z) -> x + "x" + y + "x" + z);  // "10x20x30"
         *
         * LongTuple.LongTuple3 values = LongTuple.of(1L, 2L, 3L);
         * Double avg = values.map((a, b, c) -> (a + b + c) / 3.0);  // 2.0
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the tri-function to apply to the three elements
         * @return the result of applying the mapper function, may be {@code null}
         * @throws E if the mapper throws an exception
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.LongTriFunction<U, E> mapper) throws E {
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
         * LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
         * u.Optional<LongTuple.LongTuple3> result = triple.filter((a, b, c) -> a < b && b < c);   // Optional containing the tuple
         *
         * LongTuple.LongTuple3 pythagorean = LongTuple.of(3L, 4L, 5L);
         * pythagorean.filter((a, b, c) -> a*a + b*b == c*c)
         *            .ifPresent(t -> System.out.println("Pythagorean triple found!"));
         *
         * LongTuple.LongTuple3 descending = LongTuple.of(5L, 4L, 3L);
         * u.Optional<LongTuple.LongTuple3> empty = descending.filter((a, b, c) -> a < b && b < c);  // Empty Optional
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the tri-predicate to test the three elements
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<LongTuple3> filter(final Throwables.LongTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2))) + Long.hashCode(_3)
         */
        @Override
        public int hashCode() {
            return (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2))) + Long.hashCode(_3);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is a LongTuple.LongTuple3 with the same values
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple3 other)) {
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

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly four long values.
     * The values are accessible through the public final fields {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
     * long sum = quad.sum();                  // 10L
     * long min = quad.min();                  // 1L
     * LongTuple.LongTuple4 reversed = quad.reverse();   // (4, 3, 2, 1)
     * }</pre>
     */
    public static final class LongTuple4 extends LongTuple<LongTuple4> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;

        LongTuple4() {
            this(0, 0, 0, 0);
        }

        LongTuple4(final long _1, final long _2, final long _3, final long _4) {
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
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new LongTuple.LongTuple4 with values (_4, _3, _2, _1)
         */
        @Override
        public LongTuple4 reverse() {
            return new LongTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals any of the four elements
         */
        @Override
        public boolean contains(final long valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
        }

        /**
         * Returns the minimum value among the four elements.
         *
         * @return the smallest of _1, _2, _3, and _4
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4);
        }

        /**
         * Returns the maximum value among the four elements.
         *
         * @return the largest of _1, _2, _3, and _4
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the median value of the four elements.
         * For tuples with an even number of elements, returns the lower middle value.
         *
         * @return the median (lower middle) long value
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of all four elements.
         *
         * @return _1 + _2 + _3 + _4 as a long
         */
        @Override
        public long sum() {
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
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all four elements.
         *
         * @return a hash code based on all four elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3))) + Long.hashCode(_4);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple4 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple4 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple4 other)) {
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

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly five long values.
     * The values are accessible through the public final fields {@code _1} through {@code _5}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
     * double avg = tuple.average();            // 3.0
     * long median = tuple.median();            // 3
     * LongTuple.LongTuple5 reversed = tuple.reverse();   // (5, 4, 3, 2, 1)
     * }</pre>
     */
    public static final class LongTuple5 extends LongTuple<LongTuple5> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;
        /** The fifth long value in this tuple. */
        public final long _5;

        LongTuple5() {
            this(0, 0, 0, 0, 0);
        }

        LongTuple5(final long _1, final long _2, final long _3, final long _4, final long _5) {
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
        public long min() {
            return N.min(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the maximum value among the five elements.
         *
         * @return the largest of _1, _2, _3, _4, and _5
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the median value of the five elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median long value
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of all five elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 as a long
         */
        @Override
        public long sum() {
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
         * @return a new LongTuple.LongTuple5 with values (_5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple5 reverse() {
            return new LongTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals any of the five elements
         */
        @Override
        public boolean contains(final long valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> consumer) throws E {
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
            return (31 * (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3)) + Long.hashCode(_4))) + Long.hashCode(_5);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple5 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple5 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple5 other)) {
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

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly six long values.
     * The values are accessible through the public final fields {@code _1} through {@code _6}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
     * long sum = tuple.sum();         // 21
     * double avg = tuple.average();   // 3.5
     * }</pre>
     */
    public static final class LongTuple6 extends LongTuple<LongTuple6> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;
        /** The fifth long value in this tuple. */
        public final long _5;
        /** The sixth long value in this tuple. */
        public final long _6;

        LongTuple6() {
            this(0, 0, 0, 0, 0, 0);
        }

        LongTuple6(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6) {
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
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new LongTuple.LongTuple6 with values (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple6 reverse() {
            return new LongTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals any of the six elements
         */
        @Override
        public boolean contains(final long valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
        }

        /**
         * Returns the minimum value among the six elements.
         *
         * @return the smallest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the maximum value among the six elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median value of the six elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) long value when sorted
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of all six elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 as a long
         */
        @Override
        public long sum() {
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
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all six elements.
         *
         * @return a hash code based on all six elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3)) + Long.hashCode(_4)) + Long.hashCode(_5)))
                    + Long.hashCode(_6);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple6 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple6 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple6 other)) {
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

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly seven long values.
     * The values are accessible through the public final fields {@code _1} through {@code _7}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
     * long sum = tuple.sum();                  // 28
     * long median = tuple.median();            // 4
     * LongTuple.LongTuple7 reversed = tuple.reverse();   // (7, 6, 5, 4, 3, 2, 1)
     * }</pre>
     */
    public static final class LongTuple7 extends LongTuple<LongTuple7> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;
        /** The fifth long value in this tuple. */
        public final long _5;
        /** The sixth long value in this tuple. */
        public final long _6;
        /** The seventh long value in this tuple. */
        public final long _7;

        LongTuple7() {
            this(0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple7(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7) {
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
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new LongTuple.LongTuple7 with values (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple7 reverse() {
            return new LongTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals any of the seven elements
         */
        @Override
        public boolean contains(final long valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
            consumer.accept(_7);
        }

        /**
         * Returns the minimum value among the seven elements.
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the maximum value among the seven elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the median value of the seven elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median long value
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of all seven elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 as a long
         */
        @Override
        public long sum() {
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
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all seven elements.
         *
         * @return a hash code based on all seven elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3)) + Long.hashCode(_4)) + Long.hashCode(_5))
                    + Long.hashCode(_6))) + Long.hashCode(_7);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple7 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple7 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple7 other)) {
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

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly eight long values.
     * The values are accessible through the public final fields {@code _1} through {@code _8}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
     * long sum = tuple.sum();                   // 36
     * double avg = tuple.average();             // 4.5
     * boolean contains5 = tuple.contains(5L);   // true
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more long values
     */
    @Deprecated
    public static final class LongTuple8 extends LongTuple<LongTuple8> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;
        /** The fifth long value in this tuple. */
        public final long _5;
        /** The sixth long value in this tuple. */
        public final long _6;
        /** The seventh long value in this tuple. */
        public final long _7;
        /** The eighth long value in this tuple. */
        public final long _8;

        LongTuple8() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple8(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8) {
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
         * Returns the number of elements in this tuple (always 8).
         *
         * @return 8
         */
        @Override
        public int arity() {
            return 8;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new LongTuple.LongTuple8 with elements in order (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple8 reverse() {
            return new LongTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if any element equals the value, {@code false} otherwise
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to perform on each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> consumer) throws E {
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
         * Returns the minimum value among the eight elements.
         *
         * @return the minimum of all eight elements
         */
        @Override
        public long min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum value among the eight elements.
         *
         * @return the maximum of all eight elements
         */
        @Override
        public long max() {
            return N.max(elements());
        }

        /**
         * Returns the median value of the eight elements.
         * For tuples with an even number of elements, returns the lower middle value.
         *
         * @return the median (lower middle) long value
         */
        @Override
        public long median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of the eight elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8
         */
        @Override
        public long sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of the eight elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8) / 8.0
         */
        @Override
        public double average() {
            return N.average(elements());
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
            return (31
                    * (31 * (31 * (31 * (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3)) + Long.hashCode(_4)) + Long.hashCode(_5))
                            + Long.hashCode(_6)) + Long.hashCode(_7)))
                    + Long.hashCode(_8);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple8 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple8 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple8 other)) {
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

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly nine long values.
     * The values are accessible through the public final fields {@code _1} through {@code _9}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
     * long sum = tuple.sum();         // 45
     * long median = tuple.median();   // 5
     * double avg = tuple.average();   // 5.0
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more long values
     */
    @Deprecated
    public static final class LongTuple9 extends LongTuple<LongTuple9> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;
        /** The fifth long value in this tuple. */
        public final long _5;
        /** The sixth long value in this tuple. */
        public final long _6;
        /** The seventh long value in this tuple. */
        public final long _7;
        /** The eighth long value in this tuple. */
        public final long _8;
        /** The ninth long value in this tuple. */
        public final long _9;

        LongTuple9() {
            this(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple9(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8, final long _9) {
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
         * Returns the number of elements in this tuple (always 9).
         *
         * @return 9
         */
        @Override
        public int arity() {
            return 9;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new LongTuple.LongTuple9 with elements in order (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple9 reverse() {
            return new LongTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if any element equals the value, {@code false} otherwise
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to perform on each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> consumer) throws E {
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
         * Returns the minimum value among the nine elements.
         *
         * @return the minimum of all nine elements
         */
        @Override
        public long min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum value among the nine elements.
         *
         * @return the maximum of all nine elements
         */
        @Override
        public long max() {
            return N.max(elements());
        }

        /**
         * Returns the median value of the nine elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median long value
         */
        @Override
        public long median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of the nine elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9
         */
        @Override
        public long sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of the nine elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9) / 9.0
         */
        @Override
        public double average() {
            return N.average(elements());
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
            return (31 * (31
                    * (31 * (31 * (31 * (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3)) + Long.hashCode(_4)) + Long.hashCode(_5))
                            + Long.hashCode(_6)) + Long.hashCode(_7))
                    + Long.hashCode(_8))) + Long.hashCode(_9);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple9 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple9 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple9 other)) {
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

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}

