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
import com.landawn.abacus.util.stream.ShortStream;

/**
 * Abstract base class for immutable tuples containing primitive short values.
 * This class provides a type-safe way to work with fixed-size collections of short values
 * with compile-time size guarantees.
 * 
 * <p>ShortTuple and its subclasses offer:
 * <ul>
 *   <li>Immutable storage for 0-9 short values</li>
 *   <li>Type-safe access to elements via public final fields</li>
 *   <li>Statistical operations (min, max, median, sum, average)</li>
 *   <li>Functional operations (forEach, stream, map, filter)</li>
 *   <li>Utility methods (reverse, contains, toArray, toList)</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>{@code
 * ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
 * short min = tuple.min(); // 1
 * int sum = tuple.sum(); // 6
 * }</pre>
 * 
 * @param <TP> the specific ShortTuple subtype for method chaining
 * 
 * @see ShortTuple1
 * @see ShortTuple2
 * @see ShortTuple3
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class ShortTuple<TP extends ShortTuple<TP>> extends PrimitiveTuple<TP> {

    protected short[] elements;

    /**
     * Creates a ShortTuple1 containing a single short value.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple1 single = ShortTuple.of((short)42);
     * short value = single._1; // 42
     * }</pre>
     *
     * @param _1 the short value to store
     * @return a new ShortTuple1 containing the specified value
     */
    public static ShortTuple1 of(final short _1) {
        return new ShortTuple1(_1);
    }

    /**
     * Creates a ShortTuple2 containing two short values.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple2 pair = ShortTuple.of((short)10, (short)20);
     * short first = pair._1;  // 10
     * short second = pair._2; // 20
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @return a new ShortTuple2 containing the specified values
     */
    public static ShortTuple2 of(final short _1, final short _2) {
        return new ShortTuple2(_1, _2);
    }

    /**
     * Creates a ShortTuple3 containing three short values.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 triple = ShortTuple.of((short)1, (short)2, (short)3);
     * double avg = triple.average(); // 2.0
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @return a new ShortTuple3 containing the specified values
     */
    public static ShortTuple3 of(final short _1, final short _2, final short _3) {
        return new ShortTuple3(_1, _2, _3);
    }

    /**
     * Creates a ShortTuple4 containing four short values.
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @return a new ShortTuple4 containing the specified values
     */
    public static ShortTuple4 of(final short _1, final short _2, final short _3, final short _4) {
        return new ShortTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a ShortTuple5 containing five short values.
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @param _5 the fifth short value
     * @return a new ShortTuple5 containing the specified values
     */
    public static ShortTuple5 of(final short _1, final short _2, final short _3, final short _4, final short _5) {
        return new ShortTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a ShortTuple6 containing six short values.
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @param _5 the fifth short value
     * @param _6 the sixth short value
     * @return a new ShortTuple6 containing the specified values
     */
    public static ShortTuple6 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6) {
        return new ShortTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a ShortTuple7 containing seven short values.
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @param _5 the fifth short value
     * @param _6 the sixth short value
     * @param _7 the seventh short value
     * @return a new ShortTuple7 containing the specified values
     */
    public static ShortTuple7 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7) {
        return new ShortTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a ShortTuple8 containing eight short values.
     * 
     * <p>Note: For tuples with 8 or more elements, consider using a custom class
     * with meaningful property names for better code readability.
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @param _5 the fifth short value
     * @param _6 the sixth short value
     * @param _7 the seventh short value
     * @param _8 the eighth short value
     * @return a new ShortTuple8 containing the specified values
     * @deprecated you should consider using {@code class SomeClass { final T1 propName1, final T2 propName2...}}
     */
    @Deprecated
    public static ShortTuple8 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7,
            final short _8) {
        return new ShortTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a ShortTuple9 containing nine short values.
     * 
     * <p>Note: For tuples with 9 elements, consider using a custom class
     * with meaningful property names for better code readability and maintainability.
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @param _5 the fifth short value
     * @param _6 the sixth short value
     * @param _7 the seventh short value
     * @param _8 the eighth short value
     * @param _9 the ninth short value
     * @return a new ShortTuple9 containing the specified values
     * @deprecated you should consider using {@code class SomeClass { final T1 propName1, final T2 propName2...}}
     */
    @Deprecated
    public static ShortTuple9 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7, final short _8,
            final short _9) {
        return new ShortTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a ShortTuple from an array of short values.
     * The array length must be between 0 and 9 inclusive.
     * 
     * <p>Example:
     * <pre>{@code
     * short[] values = {1, 2, 3};
     * ShortTuple3 tuple = ShortTuple.create(values);
     * }</pre>
     *
     * @param <TP> the specific ShortTuple subtype to return
     * @param a the array of short values (length must be 0-9)
     * @return a ShortTuple of the appropriate size containing the array elements
     * @throws IllegalArgumentException if the array length is greater than 9
     */
    public static <TP extends ShortTuple<TP>> TP create(final short[] a) {
        if (a == null || a.length == 0) {
            return (TP) ShortTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) ShortTuple.of(a[0]);

            case 2:
                return (TP) ShortTuple.of(a[0], a[1]);

            case 3:
                return (TP) ShortTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     * Returns the minimum value among all elements in this tuple.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)5, (short)2, (short)8);
     * short min = tuple.min(); // 2
     * }</pre>
     *
     * @return the minimum short value in this tuple
     * @throws NoSuchElementException if the tuple is empty (ShortTuple0)
     */
    public short min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum value among all elements in this tuple.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)5, (short)2, (short)8);
     * short max = tuple.max(); // 8
     * }</pre>
     *
     * @return the maximum short value in this tuple
     * @throws NoSuchElementException if the tuple is empty (ShortTuple0)
     */
    public short max() {
        return N.max(elements());
    }

    /**
     * Returns the median value of all elements in this tuple.
     * For tuples with an even number of elements, returns the lower middle value.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)3, (short)2);
     * short median = tuple.median(); // 2
     * }</pre>
     *
     * @return the median short value in this tuple
     * @throws NoSuchElementException if the tuple is empty (ShortTuple0)
     */
    public short median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all elements in this tuple.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * int sum = tuple.sum(); // 6
     * }</pre>
     *
     * @return the sum of all short values in this tuple as an int
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average (arithmetic mean) of all elements in this tuple.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple4 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4);
     * double avg = tuple.average(); // 2.5
     * }</pre>
     *
     * @return the average of all short values in this tuple as a double
     * @throws NoSuchElementException if the tuple is empty (ShortTuple0)
     */
    public double average() {
        return N.average(elements());
    }

    /**
     * Returns a new tuple with elements in reverse order.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * ShortTuple3 reversed = tuple.reverse(); // (3, 2, 1)
     * }</pre>
     *
     * @return a new tuple of the same type with elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified value.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * boolean has2 = tuple.contains((short)2); // true
     * boolean has5 = tuple.contains((short)5); // false
     * }</pre>
     *
     * @param valueToFind the short value to search for
     * @return true if the value is found in this tuple, false otherwise
     */
    public abstract boolean contains(short valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * Modifications to the returned array do not affect the tuple.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * short[] array = tuple.toArray(); // [1, 2, 3]
     * }</pre>
     *
     * @return a new short array containing all tuple elements in order
     */
    public short[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a ShortList containing all elements of this tuple.
     * The returned list is a new instance and modifications do not affect the tuple.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * ShortList list = tuple.toList();
     * }</pre>
     *
     * @return a new ShortList containing all tuple elements in order
     */
    public ShortList toList() {
        return ShortList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * tuple.forEach(value -> System.out.println(value));
     * }</pre>
     *
     * @param <E> the type of exception that the consumer may throw
     * @param consumer the action to be performed for each element
     * @throws E if the consumer throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
        for (final short e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns a ShortStream of all elements in this tuple.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * int sum = tuple.stream().sum(); // 6
     * }</pre>
     *
     * @return a ShortStream containing all tuple elements in order
     */
    public ShortStream stream() {
        return ShortStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * The hash code is computed based on all elements in the tuple.
     *
     * @return a hash code value for this tuple
     */
    @Override
    public int hashCode() {
        return N.hashCode(elements());
    }

    /**
     * Compares this tuple to the specified object for equality.
     * Two tuples are equal if they have the same class and contain
     * the same elements in the same order.
     *
     * @param obj the object to compare with
     * @return true if the specified object is equal to this tuple
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        } else {
            return N.equals(elements(), ((ShortTuple<TP>) obj).elements());
        }
    }

    /**
     * Returns a string representation of this tuple.
     * The string representation consists of the tuple elements
     * enclosed in square brackets and separated by commas.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * String str = tuple.toString(); // "[1, 2, 3]"
     * }</pre>
     *
     * @return a string representation of this tuple
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    protected abstract short[] elements();

    /**
     * An empty tuple containing no elements.
     * This class is used to represent a tuple with zero elements
     * and is returned by {@link #create(short[])} when passed a null or empty array.
     */
    static final class ShortTuple0 extends ShortTuple<ShortTuple0> {

        private static final ShortTuple0 EMPTY = new ShortTuple0();

        ShortTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public short min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public short max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public short median() {
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
        public ShortTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final short valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected short[] elements() {
            return N.EMPTY_SHORT_ARRAY;
        }
    }

    /**
     * A tuple containing exactly one short value.
     * The value is accessible through the public final field {@code _1}.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple1 single = ShortTuple.of((short)42);
     * short value = single._1; // 42
     * }</pre>
     */
    public static final class ShortTuple1 extends ShortTuple<ShortTuple1> {

        /** The single short value stored in this tuple. */
        public final short _1;

        ShortTuple1() {
            this((short) 0);
        }

        ShortTuple1(final short _1) {
            this._1 = _1;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 1
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
        public short min() {
            return _1;
        }

        /**
         * Returns the maximum value in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * @return the single element value
         */
        @Override
        public short max() {
            return _1;
        }

        /**
         * Returns the median value in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * @return the single element value
         */
        @Override
        public short median() {
            return _1;
        }

        /**
         * Returns the sum of all values in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * @return the single element value as an int
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
         * Returns a new tuple with elements in reverse order.
         * For a single-element tuple, returns a new tuple with the same value.
         *
         * @return a new ShortTuple1 with the same value
         */
        @Override
        public ShortTuple1 reverse() {
            return new ShortTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if the value equals _1, false otherwise
         */
        @Override
        public boolean contains(final short valueToFind) {
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
         * @return true if the object is a ShortTuple1 with the same value
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ShortTuple1 other)) {
                return false;
            } else {
                return _1 == other._1;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return a string in the format "[value]"
         */
        @Override
        public String toString() {
            return "[" + _1 + "]";
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly two short values.
     * The values are accessible through the public final fields {@code _1} and {@code _2}.
     * 
     * <p>This class provides additional functional methods for working with pairs:
     * <ul>
     *   <li>{@link #accept(Throwables.ShortBiConsumer)} - consume both values</li>
     *   <li>{@link #map(Throwables.ShortBiFunction)} - transform the pair to a single value</li>
     *   <li>{@link #filter(Throwables.ShortBiPredicate)} - conditionally wrap in Optional</li>
     * </ul>
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple2 pair = ShortTuple.of((short)3, (short)5);
     * int product = pair.map((a, b) -> a * b); // 15
     * }</pre>
     */
    public static final class ShortTuple2 extends ShortTuple<ShortTuple2> {

        /** The first short value in this tuple. */
        public final short _1;
        /** The second short value in this tuple. */
        public final short _2;

        ShortTuple2() {
            this((short) 0, (short) 0);
        }

        ShortTuple2(final short _1, final short _2) {
            this._1 = _1;
            this._2 = _2;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 2
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
        public short min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum value among the two elements.
         *
         * @return the larger of _1 and _2
         */
        @Override
        public short max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median value of the two elements.
         * For two elements, returns the lower value.
         *
         * @return the median of _1 and _2
         */
        @Override
        public short median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of the two elements.
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
         * Returns a new tuple with elements in reverse order.
         *
         * @return a new ShortTuple2 with values (_2, _1)
         */
        @Override
        public ShortTuple2 reverse() {
            return new ShortTuple2(_2, _1);
        }

        /**
         * Checks if either element equals the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if valueToFind equals _1 or _2
         */
        @Override
        public boolean contains(final short valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         * Performs the given action with both values as arguments.
         * 
         * <p>Example:
         * <pre>{@code
         * ShortTuple2 pair = ShortTuple.of((short)3, (short)5);
         * pair.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the action to perform with both values
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.ShortBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given function to both values and returns the result.
         * 
         * <p>Example:
         * <pre>{@code
         * ShortTuple2 pair = ShortTuple.of((short)3, (short)5);
         * int product = pair.map((a, b) -> a * b); // 15
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the function to apply to both values
         * @return the result of applying the mapper function
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.ShortBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         * 
         * <p>Example:
         * <pre>{@code
         * ShortTuple2 pair = ShortTuple.of((short)3, (short)5);
         * Optional<ShortTuple2> result = pair.filter((a, b) -> a < b); // Optional containing the pair
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the predicate to test with both values
         * @return an Optional containing this tuple if the predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<ShortTuple2> filter(final Throwables.ShortBiPredicate<E> predicate) throws E {
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
         * @return true if the object is a ShortTuple2 with the same values
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ShortTuple2 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return a string in the format "[_1, _2]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + "]";
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly three short values.
     * The values are accessible through the public final fields {@code _1}, {@code _2}, and {@code _3}.
     * 
     * <p>This class provides additional functional methods for working with triples:
     * <ul>
     *   <li>{@link #accept(Throwables.ShortTriConsumer)} - consume all three values</li>
     *   <li>{@link #map(Throwables.ShortTriFunction)} - transform the triple to a single value</li>
     *   <li>{@link #filter(Throwables.ShortTriPredicate)} - conditionally wrap in Optional</li>
     * </ul>
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple3 triple = ShortTuple.of((short)2, (short)3, (short)5);
     * int sum = triple.map((a, b, c) -> a + b + c); // 10
     * }</pre>
     */
    public static final class ShortTuple3 extends ShortTuple<ShortTuple3> {

        /** The first short value in this tuple. */
        public final short _1;
        /** The second short value in this tuple. */
        public final short _2;
        /** The third short value in this tuple. */
        public final short _3;

        ShortTuple3() {
            this((short) 0, (short) 0, (short) 0);
        }

        ShortTuple3(final short _1, final short _2, final short _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 3
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
        public short min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum value among the three elements.
         *
         * @return the largest of _1, _2, and _3
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median value of the three elements.
         *
         * @return the middle value when _1, _2, and _3 are sorted
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of all three elements.
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
         * Returns a new tuple with elements in reverse order.
         *
         * @return a new ShortTuple3 with values (_3, _2, _1)
         */
        @Override
        public ShortTuple3 reverse() {
            return new ShortTuple3(_3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if valueToFind equals _1, _2, or _3
         */
        @Override
        public boolean contains(final short valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
        }

        /**
         * Performs the given action with all three values as arguments.
         * 
         * <p>Example:
         * <pre>{@code
         * ShortTuple3 triple = ShortTuple.of((short)2, (short)3, (short)5);
         * triple.accept((a, b, c) -> System.out.println(a + " + " + b + " + " + c + " = " + (a + b + c)));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the action to perform with all three values
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.ShortTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given function to all three values and returns the result.
         * 
         * <p>Example:
         * <pre>{@code
         * ShortTuple3 triple = ShortTuple.of((short)2, (short)3, (short)5);
         * int product = triple.map((a, b, c) -> a * b * c); // 30
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the function to apply to all three values
         * @return the result of applying the mapper function
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.ShortTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         * 
         * <p>Example:
         * <pre>{@code
         * ShortTuple3 triple = ShortTuple.of((short)2, (short)3, (short)5);
         * Optional<ShortTuple3> result = triple.filter((a, b, c) -> a < b && b < c); // Optional containing the triple
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the predicate to test with all three values
         * @return an Optional containing this tuple if the predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<ShortTuple3> filter(final Throwables.ShortTriPredicate<E> predicate) throws E {
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
         * @return true if the object is a ShortTuple3 with the same values
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ShortTuple3 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return a string in the format "[_1, _2, _3]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + "]";
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly four short values.
     * The values are accessible through the public final fields {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple4 quad = ShortTuple.of((short)1, (short)2, (short)3, (short)4);
     * double avg = quad.average(); // 2.5
     * }</pre>
     */
    public static final class ShortTuple4 extends ShortTuple<ShortTuple4> {

        /** The first short value in this tuple. */
        public final short _1;
        /** The second short value in this tuple. */
        public final short _2;
        /** The third short value in this tuple. */
        public final short _3;
        /** The fourth short value in this tuple. */
        public final short _4;

        ShortTuple4() {
            this((short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple4(final short _1, final short _2, final short _3, final short _4) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 4
         */
        @Override
        public int arity() {
            return 4;
        }

        /**
         * Returns a new tuple with elements in reverse order.
         *
         * @return a new ShortTuple4 with values (_4, _3, _2, _1)
         */
        @Override
        public ShortTuple4 reverse() {
            return new ShortTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if valueToFind equals any of the four elements
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly five short values.
     * The values are accessible through the public final fields {@code _1} through {@code _5}.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple5 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5);
     * short median = tuple.median(); // 3
     * }</pre>
     */
    public static final class ShortTuple5 extends ShortTuple<ShortTuple5> {

        /** The first short value in this tuple. */
        public final short _1;
        /** The second short value in this tuple. */
        public final short _2;
        /** The third short value in this tuple. */
        public final short _3;
        /** The fourth short value in this tuple. */
        public final short _4;
        /** The fifth short value in this tuple. */
        public final short _5;

        ShortTuple5() {
            this((short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple5(final short _1, final short _2, final short _3, final short _4, final short _5) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 5
         */
        @Override
        public int arity() {
            return 5;
        }

        /**
         * Returns a new tuple with elements in reverse order.
         *
         * @return a new ShortTuple5 with values (_5, _4, _3, _2, _1)
         */
        @Override
        public ShortTuple5 reverse() {
            return new ShortTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if valueToFind equals any of the five elements
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly six short values.
     * The values are accessible through the public final fields {@code _1} through {@code _6}.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple6 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6);
     * int sum = tuple.sum(); // 21
     * }</pre>
     */
    public static final class ShortTuple6 extends ShortTuple<ShortTuple6> {

        /** The first short value in this tuple. */
        public final short _1;
        /** The second short value in this tuple. */
        public final short _2;
        /** The third short value in this tuple. */
        public final short _3;
        /** The fourth short value in this tuple. */
        public final short _4;
        /** The fifth short value in this tuple. */
        public final short _5;
        /** The sixth short value in this tuple. */
        public final short _6;

        ShortTuple6() {
            this((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple6(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 6
         */
        @Override
        public int arity() {
            return 6;
        }

        /**
         * Returns a new tuple with elements in reverse order.
         *
         * @return a new ShortTuple6 with values (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public ShortTuple6 reverse() {
            return new ShortTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if valueToFind equals any of the six elements
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly seven short values.
     * The values are accessible through the public final fields {@code _1} through {@code _7}.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple7 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
     * ShortTuple7 reversed = tuple.reverse(); // (7, 6, 5, 4, 3, 2, 1)
     * }</pre>
     */
    public static final class ShortTuple7 extends ShortTuple<ShortTuple7> {

        /** The first short value in this tuple. */
        public final short _1;
        /** The second short value in this tuple. */
        public final short _2;
        /** The third short value in this tuple. */
        public final short _3;
        /** The fourth short value in this tuple. */
        public final short _4;
        /** The fifth short value in this tuple. */
        public final short _5;
        /** The sixth short value in this tuple. */
        public final short _6;
        /** The seventh short value in this tuple. */
        public final short _7;

        ShortTuple7() {
            this((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple7(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 7
         */
        @Override
        public int arity() {
            return 7;
        }

        /**
         * Returns a new tuple with elements in reverse order.
         *
         * @return a new ShortTuple7 with values (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public ShortTuple7 reverse() {
            return new ShortTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if valueToFind equals any of the seven elements
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly eight short values.
     * The values are accessible through the public final fields {@code _1} through {@code _8}.
     * 
     * <p>Note: For tuples with 8 or more elements, consider using a custom class
     * with meaningful property names for better code readability.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple8 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4, 
     *                                    (short)5, (short)6, (short)7, (short)8);
     * boolean contains5 = tuple.contains((short)5); // true
     * }</pre>
     * 
     * @deprecated you should consider using {@code class SomeClass { final T1 propName1, final T2 propName2...}}
     */
    public static final class ShortTuple8 extends ShortTuple<ShortTuple8> {

        /** The first short value in this tuple. */
        public final short _1;
        /** The second short value in this tuple. */
        public final short _2;
        /** The third short value in this tuple. */
        public final short _3;
        /** The fourth short value in this tuple. */
        public final short _4;
        /** The fifth short value in this tuple. */
        public final short _5;
        /** The sixth short value in this tuple. */
        public final short _6;
        /** The seventh short value in this tuple. */
        public final short _7;
        /** The eighth short value in this tuple. */
        public final short _8;

        ShortTuple8() {
            this((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple8(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7, final short _8) {
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
         * Returns the number of elements in this tuple.
         *
         * @return always returns 8
         */
        @Override
        public int arity() {
            return 8;
        }

        /**
         * Returns a new tuple with elements in reverse order.
         *
         * @return a new ShortTuple8 with values (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public ShortTuple8 reverse() {
            return new ShortTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if valueToFind equals any of the eight elements
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly nine short values.
     * The values are accessible through the public final fields {@code _1} through {@code _9}.
     * 
     * <p>Note: For tuples with 9 elements, consider using a custom class
     * with meaningful property names for better code readability and maintainability.
     * 
     * <p>Example:
     * <pre>{@code
     * ShortTuple9 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5,
     *                                    (short)6, (short)7, (short)8, (short)9);
     * double avg = tuple.average(); // 5.0
     * }</pre>
     * 
     * @deprecated you should consider using {@code class SomeClass { final T1 propName1, final T2 propName2...}}
     */
    public static final class ShortTuple9 extends ShortTuple<ShortTuple9> {

        /** The first short value in this tuple. */
        public final short _1;
        /** The second short value in this tuple. */
        public final short _2;
        /** The third short value in this tuple. */
        public final short _3;
        /** The fourth short value in this tuple. */
        public final short _4;
        /** The fifth short value in this tuple. */
        public final short _5;
        /** The sixth short value in this tuple. */
        public final short _6;
        /** The seventh short value in this tuple. */
        public final short _7;
        /** The eighth short value in this tuple. */
        public final short _8;
        /** The ninth short value in this tuple. */
        public final short _9;

        ShortTuple9() {
            this((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple9(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7, final short _8,
                final short _9) {
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
         * Returns the number of elements in this tuple.
         *
         * @return always returns 9
         */
        @Override
        public int arity() {
            return 9;
        }

        /**
         * Returns a new tuple with elements in reverse order.
         *
         * @return a new ShortTuple9 with values (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public ShortTuple9 reverse() {
            return new ShortTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if valueToFind equals any of the nine elements
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}