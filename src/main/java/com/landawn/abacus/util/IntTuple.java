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

import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.IntStream;

/**
 * Abstract base class for immutable tuples containing int primitive values.
 * This class provides a type-safe way to work with fixed-size collections of int values.
 *
 * <p>IntTuple and its subclasses offer:</p>
 * <ul>
 *   <li>Type safety for int collections of known size</li>
 *   <li>Immutability for thread-safe operations</li>
 *   <li>Convenient factory methods and utilities</li>
 *   <li>Statistical operations (min, max, median, sum, average)</li>
 * </ul>
 *
 * @param <TP> The specific IntTuple subtype for fluent method chaining
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class IntTuple<TP extends IntTuple<TP>> extends PrimitiveTuple<TP> {

    /**
     * Protected constructor for subclass instantiation.
     * <p>
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link IntTuple1#of(int)}, {@link IntTuple2#of(int, int)}, etc.,
     * to create tuple instances.
     */
    protected IntTuple() {
    }

    protected int[] elements;

    /**
     * Creates an IntTuple1 containing a single int value.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple1 single = IntTuple.of(42);
     * int value = single._1; // 42
     * }</pre>
     *
     * @param _1 the int value to wrap in a tuple
     * @return a new IntTuple1 containing the provided value
     */
    public static IntTuple1 of(final int _1) {
        return new IntTuple1(_1);
    }

    /**
     * Creates an IntTuple2 containing two int values.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple2 pair = IntTuple.of(1, 2);
     * int sum = pair._1 + pair._2; // 3
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @return a new IntTuple2 containing the provided values
     */
    public static IntTuple2 of(final int _1, final int _2) {
        return new IntTuple2(_1, _2);
    }

    /**
     * Creates an IntTuple3 containing three int values.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple3 triple = IntTuple.of(1, 2, 3);
     * double average = triple.average(); // 2.0
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @return a new IntTuple3 containing the provided values
     */
    public static IntTuple3 of(final int _1, final int _2, final int _3) {
        return new IntTuple3(_1, _2, _3);
    }

    /**
     * Creates an IntTuple4 containing four int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple4 quad = IntTuple.of(1, 2, 3, 4);
     * // quad._1 == 1, quad._2 == 2, quad._3 == 3, quad._4 == 4
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @return a new IntTuple4 containing the provided values
     */
    public static IntTuple4 of(final int _1, final int _2, final int _3, final int _4) {
        return new IntTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates an IntTuple5 containing five int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple5 quint = IntTuple.of(1, 2, 3, 4, 5);
     * // quint._5 == 5
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @param _5 the fifth int value
     * @return a new IntTuple5 containing the provided values
     */
    public static IntTuple5 of(final int _1, final int _2, final int _3, final int _4, final int _5) {
        return new IntTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates an IntTuple6 containing six int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple6 sext = IntTuple.of(1, 2, 3, 4, 5, 6);
     * // sext._6 == 6
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @param _5 the fifth int value
     * @param _6 the sixth int value
     * @return a new IntTuple6 containing the provided values
     */
    public static IntTuple6 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6) {
        return new IntTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates an IntTuple7 containing seven int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple7 sept = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
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
     * @return a new IntTuple7 containing the provided values
     */
    public static IntTuple7 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7) {
        return new IntTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates an IntTuple8 containing eight int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple8 oct = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
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
     * @return a new IntTuple8 containing the provided values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static IntTuple8 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8) {
        return new IntTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates an IntTuple9 containing nine int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple9 non = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
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
     * @return a new IntTuple9 containing the provided values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static IntTuple9 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8, final int _9) {
        return new IntTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates an IntTuple from an array of int values.
     * The size of the returned tuple depends on the array length (0-9).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] values = {1, 2, 3};
     * IntTuple3 tuple = IntTuple.create(values);
     * // tuple._1 == 1, tuple._2 == 2, tuple._3 == 3
     * }</pre>
     *
     * @param <TP> the specific IntTuple type to return
     * @param a the array of int values (must have length 0-9)
     * @return an IntTuple of appropriate size containing the array values
     * @throws IllegalArgumentException if the array has more than 9 elements
     */
    public static <TP extends IntTuple<TP>> TP create(final int[] a) {
        if (a == null || a.length == 0) {
            return (TP) IntTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) IntTuple.of(a[0]);

            case 2:
                return (TP) IntTuple.of(a[0], a[1]);

            case 3:
                return (TP) IntTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     * Returns the minimum int value in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple3 tuple = IntTuple.of(3, 1, 2);
     * int min = tuple.min(); // 1
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
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple3 tuple = IntTuple.of(3, 1, 2);
     * int max = tuple.max(); // 3
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
     * For tuples with an even number of elements, returns the lower middle value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple3 tuple = IntTuple.of(3, 1, 2);
     * int median = tuple.median(); // 2
     *
     * IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
     * int median2 = tuple.median(); // 2
     * }</pre>
     *
     * @return the median int value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public int median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all elements in this tuple.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * int sum = tuple.sum(); // 6
     * }</pre>
     *
     * @return the sum of all int values in this tuple
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all int values in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * double avg = tuple.average(); // 2.0
     * }</pre>
     *
     * @return the average of all int values in this tuple
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
     * IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * IntTuple3 reversed = tuple.reverse(); // (3, 2, 1)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified int value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * boolean hasTwo = tuple.contains(2); // true
     * boolean hasFive = tuple.contains(5); // false
     * }</pre>
     *
     * @param valueToFind the int value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(int valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * Modifications to the returned array do not affect the tuple.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * int[] array = tuple.toArray(); // [1, 2, 3]
     * }</pre>
     *
     * @return a new int array containing all tuple elements
     */
    public int[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new IntList containing all elements of this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * IntList list = tuple.toList();
     * }</pre>
     *
     * @return a new IntList containing all tuple elements
     */
    public IntList toList() {
        return IntList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * tuple.forEach(System.out::println); // prints each value
     * }</pre>
     *
     * @param <E> the type of exception that the consumer may throw
     * @param consumer the action to perform for each element
     * @throws E if the consumer throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
        for (final int e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns an IntStream of all elements in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * int sum = tuple.stream().sum(); // 6
     * }</pre>
     *
     * @return an IntStream containing all tuple elements
     */
    public IntStream stream() {
        return IntStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * The hash code is computed based on all elements using {@link N#hashCode(int[])}.
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
            return N.equals(elements(), ((IntTuple<TP>) obj).elements());
        }
    }

    /**
     * Returns a string representation of this tuple.
     * The format is [element1, element2, ...].
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
     * underlying array of int values. The returned array is lazily initialized
     * on first access and cached for subsequent calls.
     * </p>
     * <p>
     * Subclasses must implement this method to provide access to their elements.
     * Modifications to the returned array will affect the tuple's internal state.
     * </p>
     *
     * @return the array of int elements stored in this tuple
     */
    protected abstract int[] elements();

    /**
     * An empty IntTuple containing no elements.
     * This class is used as a singleton for efficiency.
     */
    static final class IntTuple0 extends IntTuple<IntTuple0> {

        private static final IntTuple0 EMPTY = new IntTuple0();

        IntTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public int min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public int max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public int median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public int sum() {
            return 0;
        }

        @Override
        public double average() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public IntTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final int valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected int[] elements() {
            return N.EMPTY_INT_ARRAY;
        }
    }

    /**
     * An IntTuple containing exactly one int value.
     * Provides direct access to the element via the public final field {@code _1}.
     */
    public static final class IntTuple1 extends IntTuple<IntTuple1> {

        /** The single int value in this tuple. */
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
         * Returns the minimum value in this tuple, which is the single element.
         *
         * @return the value of _1
         */
        @Override
        public int min() {
            return _1;
        }

        /**
         * Returns the maximum value in this tuple, which is the single element.
         *
         * @return the value of _1
         */
        @Override
        public int max() {
            return _1;
        }

        /**
         * Returns the median value in this tuple, which is the single element.
         *
         * @return the value of _1
         */
        @Override
        public int median() {
            return _1;
        }

        /**
         * Returns the sum of elements in this tuple, which is the single element.
         *
         * @return the value of _1
         */
        @Override
        public int sum() {
            return _1;
        }

        /**
         * Returns the average of elements in this tuple, which is the single element.
         *
         * @return the value of _1 as a double
         */
        @Override
        public double average() {
            return _1;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         * For a single-element tuple, returns a copy of itself.
         *
         * @return a new IntTuple1 with the same value
         */
        @Override
        public IntTuple1 reverse() {
            return new IntTuple1(_1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if _1 equals valueToFind, {@code false} otherwise
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind;
        }

        /**
         * Returns a hash code for this tuple based on its single element.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return _1;
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple1 with equal value
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
         * @return "[value]" where value is _1
         */
        @Override
        public String toString() {
            return "[" + _1 + "]";
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1 };
            }

            return elements;
        }
    }

    /**
     * An IntTuple containing exactly two int values.
     * Provides direct access to elements via public final fields {@code _1} and {@code _2}.
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
         * For a tuple of two elements, returns the lower value.
         *
         * @return the median (lower) int value
         */
        @Override
        public int median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of the two elements.
         *
         * @return _1 + _2
         */
        @Override
        public int sum() {
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
         * @return a new IntTuple2 with (_2, _1)
         */
        @Override
        public IntTuple2 reverse() {
            return new IntTuple2(_2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if either element equals valueToFind
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         * Performs the given bi-consumer on the two elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple2 tuple = IntTuple.of(3, 4);
         * tuple.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple2 tuple = IntTuple.of(3, 4);
         * int product = tuple.map((a, b) -> a * b); // 12
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the bi-function to apply to the two elements
         * @return the result of applying the mapper to _1 and _2
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.IntBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple2 tuple = IntTuple.of(3, 4);
         * Optional<IntTuple2> result = tuple.filter((a, b) -> a + b > 5); // present
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the bi-predicate to test the two elements
         * @return Optional containing this tuple if predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<IntTuple2> filter(final Throwables.IntBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on both elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return 31 * _1 + _2;
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple2 with equal elements
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
         * @return "[_1, _2]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + "]";
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * An IntTuple containing exactly three int values.
     * Provides direct access to elements via public final fields {@code _1}, {@code _2}, and {@code _3}.
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
         * @return the middle value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of the three elements.
         *
         * @return _1 + _2 + _3
         */
        @Override
        public int sum() {
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
         * @return a new IntTuple3 with (_3, _2, _1)
         */
        @Override
        public IntTuple3 reverse() {
            return new IntTuple3(_3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
        }

        /**
         * Performs the given tri-consumer on the three elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple3 tuple = IntTuple.of(1, 2, 3);
         * tuple.accept((a, b, c) -> System.out.println("Sum: " + (a + b + c)));
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple3 tuple = IntTuple.of(1, 2, 3);
         * int product = tuple.map((a, b, c) -> a * b * c); // 6
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the tri-function to apply to the three elements
         * @return the result of applying the mapper to _1, _2, and _3
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.IntTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple3 tuple = IntTuple.of(1, 2, 3);
         * Optional<IntTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5); // present
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the tri-predicate to test the three elements
         * @return Optional containing this tuple if predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<IntTuple3> filter(final Throwables.IntTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on all three elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return (31 * (31 * _1 + _2)) + _3;
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple3 with equal elements
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
         * @return "[_1, _2, _3]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + "]";
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * An IntTuple containing exactly four int values.
     * Provides direct access to elements via public final fields.
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
         * For a tuple of four elements, returns the lower middle value.
         *
         * @return the median (lower middle) int value
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of the four elements.
         *
         * @return _1 + _2 + _3 + _4
         */
        @Override
        public int sum() {
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
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple4 with (_4, _3, _2, _1)
         */
        @Override
        public IntTuple4 reverse() {
            return new IntTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform
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
         * Two tuples are equal if they are both IntTuple4 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple4 with equal elements, {@code false} otherwise
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
         * The format is [_1, _2, _3, _4] where each element is displayed in order.
         *
         * @return a string representation in the format "[_1, _2, _3, _4]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + "]";
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * An IntTuple containing exactly five int values.
     * Provides direct access to elements via public final fields.
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
         *
         * @return the middle value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of the five elements.
         *
         * @return _1 + _2 + _3 + _4 + _5
         */
        @Override
        public int sum() {
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
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple5 with (_5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple5 reverse() {
            return new IntTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform
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
         * Two tuples are equal if they are both IntTuple5 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple5 with equal elements, {@code false} otherwise
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
         * The format is [_1, _2, _3, _4, _5] where each element is displayed in order.
         *
         * @return a string representation in the format "[_1, _2, _3, _4, _5]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + "]";
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * An IntTuple containing exactly six int values.
     * Provides direct access to elements via public final fields.
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
         * For a tuple of six elements, returns the lower middle value.
         *
         * @return the median (lower middle) int value
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of the six elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6
         */
        @Override
        public int sum() {
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
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple6 with (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple6 reverse() {
            return new IntTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform
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
         * Two tuples are equal if they are both IntTuple6 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple6 with equal elements, {@code false} otherwise
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
         * The format is [_1, _2, _3, _4, _5, _6] where each element is displayed in order.
         *
         * @return a string representation in the format "[_1, _2, _3, _4, _5, _6]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + "]";
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * An IntTuple containing exactly seven int values.
     * Provides direct access to elements via public final fields.
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
         *
         * @return the middle value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of the seven elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7
         */
        @Override
        public int sum() {
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
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple7 with (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple7 reverse() {
            return new IntTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
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
         * @param consumer the action to perform
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
         * Two tuples are equal if they are both IntTuple7 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple7 with equal elements, {@code false} otherwise
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
         * The format is [_1, _2, _3, _4, _5, _6, _7] where each element is displayed in order.
         *
         * @return a string representation in the format "[_1, _2, _3, _4, _5, _6, _7]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + "]";
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * An IntTuple containing exactly eight int values.
     * Provides direct access to elements via public final fields.
     * 
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
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
         * @return the smallest of all eight elements
         */
        @Override
        public int min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the maximum value among the eight elements.
         *
         * @return the largest of all eight elements
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the median value of the eight elements.
         * For a tuple of eight elements, returns the lower middle value.
         *
         * @return the median (lower middle) int value
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of the eight elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the average of the eight elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8) / 8.0
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple8 with (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple8 reverse() {
            return new IntTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
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
         * @param consumer the action to perform
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
         * Two tuples are equal if they are both IntTuple8 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple8 with equal elements, {@code false} otherwise
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
         * The format is [_1, _2, _3, _4, _5, _6, _7, _8] where each element is displayed in order.
         *
         * @return a string representation in the format "[_1, _2, _3, _4, _5, _6, _7, _8]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + "]";
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * An IntTuple containing exactly nine int values.
     * Provides direct access to elements via public final fields.
     * 
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
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
         * @return the smallest of all nine elements
         */
        @Override
        public int min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the maximum value among the nine elements.
         *
         * @return the largest of all nine elements
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the median value of the nine elements.
         *
         * @return the middle value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of the nine elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the average of the nine elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9) / 9.0
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new IntTuple9 with (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple9 reverse() {
            return new IntTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
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
         * @param consumer the action to perform
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
         * Two tuples are equal if they are both IntTuple9 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple9 with equal elements, {@code false} otherwise
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
         * The format is [_1, _2, _3, _4, _5, _6, _7, _8, _9] where each element is displayed in order.
         *
         * @return a string representation in the format "[_1, _2, _3, _4, _5, _6, _7, _8, _9]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + "]";
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}