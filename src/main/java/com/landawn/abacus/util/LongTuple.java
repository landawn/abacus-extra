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
 * Abstract base class for immutable tuples containing long primitive values.
 * This class provides a type-safe way to work with fixed-size collections of long values.
 *
 * <p>LongTuple and its subclasses offer:</p>
 * <ul>
 *   <li>Type safety for long collections of known size</li>
 *   <li>Immutability for thread-safe operations</li>
 *   <li>Convenient factory methods and utilities</li>
 *   <li>Statistical operations (min, max, median, sum, average)</li>
 * </ul>
 *
 * @param <TP> The specific LongTuple subtype for fluent method chaining
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class LongTuple<TP extends LongTuple<TP>> extends PrimitiveTuple<TP> {

    /**
     * Protected constructor for subclass instantiation.
     * <p>
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link LongTuple1#of(long)}, {@link LongTuple2#of(long, long)}, etc.,
     * to create tuple instances.
     */
    protected LongTuple() {
    }

    protected long[] elements;

    /**
     * Creates a LongTuple1 containing a single long value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple1 single = LongTuple.of(42L);
     * long value = single._1; // 42
     * }</pre>
     *
     * @param _1 the long value to wrap in a tuple
     * @return a new LongTuple1 containing the provided value
     */
    public static LongTuple1 of(final long _1) {
        return new LongTuple1(_1);
    }

    /**
     * Creates a LongTuple2 containing two long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple2 pair = LongTuple.of(1L, 2L);
     * long sum = pair._1 + pair._2; // 3
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @return a new LongTuple2 containing the provided values
     */
    public static LongTuple2 of(final long _1, final long _2) {
        return new LongTuple2(_1, _2);
    }

    /**
     * Creates a LongTuple3 containing three long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
     * double average = triple.average(); // 2.0
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @return a new LongTuple3 containing the provided values
     */
    public static LongTuple3 of(final long _1, final long _2, final long _3) {
        return new LongTuple3(_1, _2, _3);
    }

    /**
     * Creates a LongTuple4 containing four long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
     * // quad._1 == 1, quad._2 == 2, quad._3 == 3, quad._4 == 4
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @return a new LongTuple4 containing the provided values
     */
    public static LongTuple4 of(final long _1, final long _2, final long _3, final long _4) {
        return new LongTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a LongTuple5 containing five long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple5 quint = LongTuple.of(1L, 2L, 3L, 4L, 5L);
     * // quint._5 == 5
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @return a new LongTuple5 containing the provided values
     */
    public static LongTuple5 of(final long _1, final long _2, final long _3, final long _4, final long _5) {
        return new LongTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a LongTuple6 containing six long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple6 sext = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
     * // sext._6 == 6
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @param _6 the sixth long value
     * @return a new LongTuple6 containing the provided values
     */
    public static LongTuple6 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6) {
        return new LongTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a LongTuple7 containing seven long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple7 sept = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
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
     * @return a new LongTuple7 containing the provided values
     */
    public static LongTuple7 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7) {
        return new LongTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a LongTuple8 containing eight long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple8 oct = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
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
     * @return a new LongTuple8 containing the provided values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static LongTuple8 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8) {
        return new LongTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a LongTuple9 containing nine long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple9 non = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
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
     * @return a new LongTuple9 containing the provided values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static LongTuple9 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8,
            final long _9) {
        return new LongTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a LongTuple from an array of long values.
     * The size of the returned tuple depends on the array length (0-9).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * long[] values = {1L, 2L, 3L};
     * LongTuple3 tuple = LongTuple.create(values);
     * // tuple._1 == 1, tuple._2 == 2, tuple._3 == 3
     * }</pre>
     *
     * @param <TP> the specific LongTuple type to return
     * @param a the array of long values (must have length 0-9)
     * @return a LongTuple of appropriate size containing the array values
     * @throws IllegalArgumentException if the array has more than 9 elements
     */
    public static <TP extends LongTuple<TP>> TP create(final long[] a) {
        if (a == null || a.length == 0) {
            return (TP) LongTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) LongTuple.of(a[0]);

            case 2:
                return (TP) LongTuple.of(a[0], a[1]);

            case 3:
                return (TP) LongTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     * Returns the minimum long value in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
     * long min = tuple.min(); // 1
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
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
     * long max = tuple.max(); // 3
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
     * For tuples with an even number of elements, returns the lower middle value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
     * long median = tuple.median(); // 2
     *
     * LongTuple4 tuple = LongTuple.of(1L, 2L, 3L, 4L);
     * long median = tuple.median(); // 2
     * }</pre>
     *
     * @return the median long value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public long median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all elements in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * long sum = tuple.sum(); // 6
     * }</pre>
     *
     * @return the sum of all long values in this tuple
     */
    public long sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all long values in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * double avg = tuple.average(); // 2.0
     * }</pre>
     *
     * @return the average of all long values in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public double average() {
        return N.average(elements());
    }

    /**
     * Returns a new tuple with the elements in reverse order.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * LongTuple3 reversed = tuple.reverse(); // (3, 2, 1)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified long value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * boolean hasTwo = tuple.contains(2L); // true
     * boolean hasFive = tuple.contains(5L); // false
     * }</pre>
     *
     * @param valueToFind the long value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(long valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * Modifications to the returned array do not affect the tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * long[] array = tuple.toArray(); // [1, 2, 3]
     * }</pre>
     *
     * @return a new long array containing all tuple elements
     */
    public long[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new LongList containing all elements of this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * LongList list = tuple.toList();
     * }</pre>
     *
     * @return a new LongList containing all tuple elements
     */
    public LongList toList() {
        return LongList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * tuple.forEach(System.out::println); // prints each value
     * }</pre>
     *
     * @param <E> the type of exception that the consumer may throw
     * @param consumer the action to perform for each element
     * @throws E if the consumer throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.LongConsumer<E> consumer) throws E {
        for (final long e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns a LongStream of all elements in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * long sum = tuple.stream().sum(); // 6
     * }</pre>
     *
     * @return a LongStream containing all tuple elements
     */
    public LongStream stream() {
        return LongStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * The hash code is computed based on all elements using {@link N#hashCode(long[])}.
     *
     * @return a hash code value for this tuple
     */
    @Override
    public int hashCode() {
        return N.hashCode(elements());
    }

    /**
     * Compares this tuple to another object for equality.
     * Two tuples are equal if they are of the same class and contain equal elements
     * in the same order.
     *
     * @param obj the object to compare with
     * @return {@code true} if the objects are equal, {@code false} otherwise
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
     * The format is (element1, element2, ...).
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
     * An empty LongTuple with no elements.
     * <p>
     * This class represents an empty tuple and provides appropriate behavior for operations
     * on empty collections (throwing exceptions for operations requiring elements).
     * </p>
     */
    static final class LongTuple0 extends LongTuple<LongTuple0> {

        private static final LongTuple0 EMPTY = new LongTuple0();

        LongTuple0() {
        }

        /**
         * Returns the number of elements in this tuple (always 0).
         *
         * @return 0
         */
        @Override
        public int arity() {
            return 0;
        }

        /**
         * Returns the minimum value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return nothing (always throws exception)
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public long min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the maximum value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return nothing (always throws exception)
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public long max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the median value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return nothing (always throws exception)
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public long median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the sum of all elements in this tuple.
         * Since this tuple is empty, the sum is 0.
         *
         * @return 0
         */
        @Override
        public long sum() {
            return 0;
        }

        /**
         * Returns the average of all elements in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return nothing (always throws exception)
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public double average() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         * Since this tuple is empty, returns the same empty tuple.
         *
         * @return this empty tuple instance
         */
        @Override
        public LongTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified value.
         * Since this tuple is empty, always returns {@code false}.
         *
         * @param valueToFind the value to search for
         * @return {@code false} always
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
     * A LongTuple containing exactly one element.
     * <p>
     * This class provides optimized implementations for single-element operations.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple1 single = LongTuple.of(42L);
     * long value = single._1;  // 42
     * }</pre>
     */
    public static final class LongTuple1 extends LongTuple<LongTuple1> {

        /** The single element of this tuple. */
        public final long _1;

        LongTuple1() {
            this(0);
        }

        LongTuple1(final long _1) {
            this._1 = _1;
        }

        /**
         * Returns the number of elements in this tuple (always 1).
         *
         * @return 1
         */
        @Override
        public int arity() {
            return 1;
        }

        /**
         * Returns the minimum value (which is the only element).
         *
         * @return the single element value
         */
        @Override
        public long min() {
            return _1;
        }

        /**
         * Returns the maximum value (which is the only element).
         *
         * @return the single element value
         */
        @Override
        public long max() {
            return _1;
        }

        /**
         * Returns the median value (which is the only element).
         *
         * @return the single element value
         */
        @Override
        public long median() {
            return _1;
        }

        /**
         * Returns the sum (which is the only element).
         *
         * @return the single element value
         */
        @Override
        public long sum() {
            return _1;
        }

        /**
         * Returns the average (which is the only element).
         *
         * @return the single element value as a double
         */
        @Override
        public double average() {
            return _1;
        }

        /**
         * Returns a new tuple with the elements in reverse order (which is the same tuple for single element).
         *
         * @return a new LongTuple1 with the same element
         */
        @Override
        public LongTuple1 reverse() {
            return new LongTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if the value equals the single element, {@code false} otherwise
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
            return (int) _1;
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is a LongTuple1 with the same element value
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
         * @return a string representation in the format "(element)"
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
     * A LongTuple containing exactly two elements.
     * <p>
     * This class provides specialized methods for two-element operations, including
     * bi-consumer and bi-function support.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple2 pair = LongTuple.of(10L, 20L);
     * pair.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
     * long product = pair.map((a, b) -> a * b);
     * }</pre>
     */
    public static final class LongTuple2 extends LongTuple<LongTuple2> {

        /** The first element of this tuple. */
        public final long _1;
        /** The second element of this tuple. */
        public final long _2;

        LongTuple2() {
            this(0, 0);
        }

        LongTuple2(final long _1, final long _2) {
            this._1 = _1;
            this._2 = _2;
        }

        /**
         * Returns the number of elements in this tuple (always 2).
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
         * @return the minimum of _1 and _2
         */
        @Override
        public long min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum value among the two elements.
         *
         * @return the maximum of _1 and _2
         */
        @Override
        public long max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median long value in this tuple.
         * For a tuple of two elements, returns the lower value.
         *
         * @return the median (lower) long value
         */
        @Override
        public long median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of the two elements.
         *
         * @return _1 + _2
         */
        @Override
        public long sum() {
            return N.sum(_1, _2);
        }

        /**
         * Returns the average of the two elements.
         *
         * @return (_1 + _2) / 2.0
         */
        @Override
        public double average() {
            return N.average(_1, _2);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new LongTuple2 with (_2, _1)
         */
        @Override
        public LongTuple2 reverse() {
            return new LongTuple2(_2, _1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if either element equals the value, {@code false} otherwise
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
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
        }

        /**
         * Performs the given bi-consumer on the two elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple2 pair = LongTuple.of(3L, 4L);
         * pair.accept((a, b) -> System.out.println("Distance: " + Math.sqrt(a*a + b*b)));
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple2 pair = LongTuple.of(10L, 3L);
         * long remainder = pair.map((a, b) -> a % b);  // 1
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the bi-function to apply to the two elements
         * @return the result of applying the mapper to _1 and _2
         * @throws E if the mapper throws an exception
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.LongBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple2 pair = LongTuple.of(10L, 20L);
         * Optional<LongTuple2> result = pair.filter((a, b) -> a < b);  // Optional containing the tuple
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the bi-predicate to test the two elements
         * @return an Optional containing this tuple if the predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<LongTuple2> filter(final Throwables.LongBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return a hash code based on both elements
         */
        @Override
        public int hashCode() {
            return (int) (31 * _1 + _2);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is a LongTuple2 with the same element values
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
         * @return a string representation in the format "(_1, _2)"
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
     * A LongTuple containing exactly three elements.
     * <p>
     * This class provides specialized methods for three-element operations, including
     * tri-consumer and tri-function support.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
     * triple.accept((a, b, c) -> System.out.println("Sum: " + (a + b + c)));
     * long product = triple.map((a, b, c) -> a * b * c);
     * }</pre>
     */
    public static final class LongTuple3 extends LongTuple<LongTuple3> {

        /** The first element of this tuple. */
        public final long _1;
        /** The second element of this tuple. */
        public final long _2;
        /** The third element of this tuple. */
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
         * Returns the number of elements in this tuple (always 3).
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
         * @return the minimum of _1, _2, and _3
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum value among the three elements.
         *
         * @return the maximum of _1, _2, and _3
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median value of the three elements.
         *
         * @return the median value
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of the three elements.
         *
         * @return _1 + _2 + _3
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3);
        }

        /**
         * Returns the average of the three elements.
         *
         * @return (_1 + _2 + _3) / 3.0
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new LongTuple3 with (_3, _2, _1)
         */
        @Override
        public LongTuple3 reverse() {
            return new LongTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals the value, {@code false} otherwise
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
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
        }

        /**
         * Performs the given tri-consumer action on the three elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple3 triple = LongTuple.of(3L, 4L, 5L);
         * triple.accept((a, b, c) -> {
         *     if (a*a + b*b == c*c) System.out.println("Pythagorean triple!");
         * });
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple3 triple = LongTuple.of(2L, 3L, 4L);
         * long volume = triple.map((l, w, h) -> l * w * h);  // 24
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the tri-function to apply to the three elements
         * @return the result of applying the mapper to _1, _2, and _3
         * @throws E if the mapper throws an exception
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.LongTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
         * Optional<LongTuple3> result = triple.filter((a, b, c) -> a < b && b < c);  // Optional containing the tuple
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the tri-predicate to test the three elements
         * @return an Optional containing this tuple if the predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<LongTuple3> filter(final Throwables.LongTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return a hash code based on all three elements
         */
        @Override
        public int hashCode() {
            return (int) ((31 * (31 * _1 + _2)) + _3);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is a LongTuple3 with the same element values
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
         * @return a string representation in the format "(_1, _2, _3)"
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
     * A LongTuple containing exactly four elements.
     * <p>
     * This class provides storage for four long values with optimized implementations
     * for common operations like min, max, sum, and average.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
     * long sum = quad.sum();  // 10L
     * long min = quad.min();  // 1L
     * LongTuple4 reversed = quad.reverse();  // (4, 3, 2, 1)
     * }</pre>
     */
    public static final class LongTuple4 extends LongTuple<LongTuple4> {

        /** The first element of this tuple. */
        public final long _1;
        /** The second element of this tuple. */
        public final long _2;
        /** The third element of this tuple. */
        public final long _3;
        /** The fourth element of this tuple. */
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
         * Returns the number of elements in this tuple (always 4).
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
         * @return a new LongTuple4 with elements in order (_4, _3, _2, _1)
         */
        @Override
        public LongTuple4 reverse() {
            return new LongTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals the value, {@code false} otherwise
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
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
        }

        /**
         * Returns the minimum value among the four elements.
         *
         * @return the minimum of _1, _2, _3, and _4
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4);
        }

        /**
         * Returns the maximum value among the four elements.
         *
         * @return the maximum of _1, _2, _3, and _4
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
         * Returns the sum of the four elements.
         *
         * @return _1 + _2 + _3 + _4
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3, _4);
        }

        /**
         * Returns the average of the four elements.
         *
         * @return (_1 + _2 + _3 + _4) / 4.0
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
            return (int) ((31 * (31 * (31 * _1 + _2) + _3)) + _4);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple4 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple4 with equal elements, {@code false} otherwise
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
     * A LongTuple containing exactly five elements.
     * <p>
     * This class provides storage for five long values with optimized implementations
     * for common operations like min, max, sum, average, and median.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple5 quintuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
     * double avg = quintuple.average();  // 3.0
     * long median = quintuple.median();  // 3
     * LongTuple5 reversed = quintuple.reverse();  // (5, 4, 3, 2, 1)
     * }</pre>
     */
    public static final class LongTuple5 extends LongTuple<LongTuple5> {

        /** The first element of this tuple. */
        public final long _1;
        /** The second element of this tuple. */
        public final long _2;
        /** The third element of this tuple. */
        public final long _3;
        /** The fourth element of this tuple. */
        public final long _4;
        /** The fifth element of this tuple. */
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
         * Returns the number of elements in this tuple (always 5).
         *
         * @return 5
         */
        @Override
        public int arity() {
            return 5;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new LongTuple5 with elements in order (_5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple5 reverse() {
            return new LongTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals the value, {@code false} otherwise
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
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
        }

        /**
         * Returns the minimum value among the five elements.
         *
         * @return the minimum of _1, _2, _3, _4, and _5
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the maximum value among the five elements.
         *
         * @return the maximum of _1, _2, _3, _4, and _5
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the median value of the five elements.
         *
         * @return the middle value when sorted
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of the five elements.
         *
         * @return _1 + _2 + _3 + _4 + _5
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the average of the five elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5) / 5.0
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5);
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
            return (int) ((31 * (31 * (31 * (31 * _1 + _2) + _3) + _4)) + _5);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple5 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple5 with equal elements, {@code false} otherwise
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
     * A LongTuple containing exactly six elements.
     * <p>
     * This class provides storage for six long values with optimized implementations
     * for common operations like min, max, sum, average, and median.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple6 sextuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
     * long sum = sextuple.sum();  // 21
     * double avg = sextuple.average();  // 3.5
     * }</pre>
     */
    public static final class LongTuple6 extends LongTuple<LongTuple6> {

        /** The first element of this tuple. */
        public final long _1;
        /** The second element of this tuple. */
        public final long _2;
        /** The third element of this tuple. */
        public final long _3;
        /** The fourth element of this tuple. */
        public final long _4;
        /** The fifth element of this tuple. */
        public final long _5;
        /** The sixth element of this tuple. */
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
         * Returns the number of elements in this tuple (always 6).
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
         * @return a new LongTuple6 with elements in order (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple6 reverse() {
            return new LongTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals the value, {@code false} otherwise
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
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
        }

        /**
         * Returns the minimum value among the six elements.
         *
         * @return the minimum of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the maximum value among the six elements.
         *
         * @return the maximum of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median value of the six elements.
         * For tuples with an even number of elements, returns the lower middle value.
         *
         * @return the median (lower middle) long value
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of the six elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the average of the six elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6) / 6.0
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
            return (int) ((31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5)) + _6);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple6 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple6 with equal elements, {@code false} otherwise
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
     * A LongTuple containing exactly seven elements.
     * <p>
     * This class provides storage for seven long values with optimized implementations
     * for common operations like min, max, sum, average, and median.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple7 septuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
     * long sum = septuple.sum();  // 28
     * long median = septuple.median();  // 4
     * }</pre>
     */
    public static final class LongTuple7 extends LongTuple<LongTuple7> {

        /** The first element of this tuple. */
        public final long _1;
        /** The second element of this tuple. */
        public final long _2;
        /** The third element of this tuple. */
        public final long _3;
        /** The fourth element of this tuple. */
        public final long _4;
        /** The fifth element of this tuple. */
        public final long _5;
        /** The sixth element of this tuple. */
        public final long _6;
        /** The seventh element of this tuple. */
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
         * Returns the number of elements in this tuple (always 7).
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
         * @return a new LongTuple7 with elements in order (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple7 reverse() {
            return new LongTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals the value, {@code false} otherwise
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
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
        }

        /**
         * Returns the minimum value among the seven elements.
         *
         * @return the minimum of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the maximum value among the seven elements.
         *
         * @return the maximum of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the median value of the seven elements.
         *
         * @return the middle value when sorted
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of the seven elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the average of the seven elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7) / 7.0
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
            return (int) ((31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6)) + _7);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple7 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple7 with equal elements, {@code false} otherwise
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
     * A LongTuple containing exactly eight elements.
     * <p>
     * This class provides storage for eight long values with optimized implementations
     * for common operations like min, max, sum, average, and median.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple8 octuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
     * long sum = octuple.sum();  // 36
     * double avg = octuple.average();  // 4.5
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static final class LongTuple8 extends LongTuple<LongTuple8> {

        /** The first element of this tuple. */
        public final long _1;
        /** The second element of this tuple. */
        public final long _2;
        /** The third element of this tuple. */
        public final long _3;
        /** The fourth element of this tuple. */
        public final long _4;
        /** The fifth element of this tuple. */
        public final long _5;
        /** The sixth element of this tuple. */
        public final long _6;
        /** The seventh element of this tuple. */
        public final long _7;
        /** The eighth element of this tuple. */
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
         * @return a new LongTuple8 with elements in order (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple8 reverse() {
            return new LongTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
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
            return (int) ((31 * (31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6) + _7)) + _8);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple8 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple8 with equal elements, {@code false} otherwise
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
     * A LongTuple containing exactly nine elements.
     * <p>
     * This class provides storage for nine long values with optimized implementations
     * for common operations like min, max, sum, average, and median.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple9 nonuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
     * long sum = nonuple.sum();  // 45
     * long median = nonuple.median();  // 5
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static final class LongTuple9 extends LongTuple<LongTuple9> {

        /** The first element of this tuple. */
        public final long _1;
        /** The second element of this tuple. */
        public final long _2;
        /** The third element of this tuple. */
        public final long _3;
        /** The fourth element of this tuple. */
        public final long _4;
        /** The fifth element of this tuple. */
        public final long _5;
        /** The sixth element of this tuple. */
        public final long _6;
        /** The seventh element of this tuple. */
        public final long _7;
        /** The eighth element of this tuple. */
        public final long _8;
        /** The ninth element of this tuple. */
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
         * @return a new LongTuple9 with elements in order (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple9 reverse() {
            return new LongTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
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
         *
         * @return the middle value when sorted
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
            return (int) ((31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6) + _7) + _8)) + _9);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple9 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple9 with equal elements, {@code false} otherwise
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