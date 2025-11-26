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
import com.landawn.abacus.util.stream.ShortStream;

/**
 * Abstract base class for immutable tuples containing short primitive values.
 * This class provides a type-safe way to work with fixed-size collections of short values.
 *
 * <p>ShortTuple and its subclasses offer:</p>
 * <ul>
 *   <li>Type safety for short collections of known size</li>
 *   <li>Immutability for thread-safe operations</li>
 *   <li>Convenient factory methods and utilities</li>
 *   <li>Statistical operations (min, max, median, sum, average)</li>
 * </ul>
 *
 * @param <TP> The specific ShortTuple subtype for fluent method chaining
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class ShortTuple<TP extends ShortTuple<TP>> extends PrimitiveTuple<TP> {

    /**
     * Protected constructor for subclass instantiation.
     * <p>
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link ShortTuple1#of(short)}, {@link ShortTuple2#of(short, short)}, etc.,
     * to create tuple instances.
     */
    protected ShortTuple() {
    }

    protected short[] elements;

    /**
     * Creates a ShortTuple1 containing a single short value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple1 single = ShortTuple.of((short)42);
     * short value = single._1; // 42
     * }</pre>
     *
     * @param _1 the short value to store in the tuple
     * @return a new ShortTuple1 containing the provided value
     */
    public static ShortTuple1 of(final short _1) {
        return new ShortTuple1(_1);
    }

    /**
     * Creates a ShortTuple2 containing two short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple2 pair = ShortTuple.of((short)1, (short)2);
     * int sum = pair._1 + pair._2; // 3
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @return a new ShortTuple2 containing the provided values
     */
    public static ShortTuple2 of(final short _1, final short _2) {
        return new ShortTuple2(_1, _2);
    }

    /**
     * Creates a ShortTuple3 containing three short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple3 triple = ShortTuple.of((short)1, (short)2, (short)3);
     * double average = triple.average(); // 2.0
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @return a new ShortTuple3 containing the provided values
     */
    public static ShortTuple3 of(final short _1, final short _2, final short _3) {
        return new ShortTuple3(_1, _2, _3);
    }

    /**
     * Creates a ShortTuple4 containing four short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple4 quad = ShortTuple.of((short)1, (short)2, (short)3, (short)4);
     * // quad._1 == 1, quad._2 == 2, quad._3 == 3, quad._4 == 4
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @return a new ShortTuple4 containing the provided values
     */
    public static ShortTuple4 of(final short _1, final short _2, final short _3, final short _4) {
        return new ShortTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a ShortTuple5 containing five short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple5 quint = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5);
     * // quint._5 == 5
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @param _5 the fifth short value
     * @return a new ShortTuple5 containing the provided values
     */
    public static ShortTuple5 of(final short _1, final short _2, final short _3, final short _4, final short _5) {
        return new ShortTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a ShortTuple6 containing six short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple6 sext = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6);
     * // sext._6 == 6
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @param _5 the fifth short value
     * @param _6 the sixth short value
     * @return a new ShortTuple6 containing the provided values
     */
    public static ShortTuple6 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6) {
        return new ShortTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a ShortTuple7 containing seven short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple7 sept = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
     * // sept._7 == 7
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @param _5 the fifth short value
     * @param _6 the sixth short value
     * @param _7 the seventh short value
     * @return a new ShortTuple7 containing the provided values
     */
    public static ShortTuple7 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7) {
        return new ShortTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a ShortTuple8 containing eight short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple8 oct = ShortTuple.of((short)1, (short)2, (short)3, (short)4,
     *                                  (short)5, (short)6, (short)7, (short)8);
     * // oct._8 == 8
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @param _5 the fifth short value
     * @param _6 the sixth short value
     * @param _7 the seventh short value
     * @param _8 the eighth short value
     * @return a new ShortTuple8 containing the provided values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more short values
     */
    @Deprecated
    public static ShortTuple8 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7,
            final short _8) {
        return new ShortTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a ShortTuple9 containing nine short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple9 non = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5,
     *                                  (short)6, (short)7, (short)8, (short)9);
     * // non._9 == 9
     * }</pre>
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
     * @return a new ShortTuple9 containing the provided values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more short values
     */
    @Deprecated
    public static ShortTuple9 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7, final short _8,
            final short _9) {
        return new ShortTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a ShortTuple from an array of short values.
     * The size of the returned tuple depends on the array length (0-9).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * short[] values = {1, 2, 3};
     * ShortTuple3 tuple = ShortTuple.create(values);
     * // tuple._1 == 1, tuple._2 == 2, tuple._3 == 3
     * }</pre>
     *
     * @param <TP> the specific ShortTuple type to return
     * @param a the array of short values (must have length 0-9)
     * @return a ShortTuple of appropriate size containing the array values
     * @throws IllegalArgumentException if the array has more than 9 elements
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
     * Returns the minimum short value in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)3, (short)1, (short)2);
     * short min = tuple.min(); // 1
     * }</pre>
     *
     * @return the minimum short value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public short min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum short value in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)3, (short)1, (short)2);
     * short max = tuple.max(); // 3
     * }</pre>
     *
     * @return the maximum short value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public short max() {
        return N.max(elements());
    }

    /**
     * Returns the median short value in this tuple.
     * For tuples with an even number of elements, returns the lower middle element.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)3, (short)2);
     * short median = tuple.median(); // 2
     *
     * ShortTuple4 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4);
     * short median = tuple.median(); // 2
     * }</pre>
     *
     * @return the median short value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public short median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all elements in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * int sum = tuple.sum(); // 6
     * }</pre>
     *
     * @return the sum of all short values in this tuple
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all short values in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple4 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4);
     * double avg = tuple.average(); // 2.5
     * }</pre>
     *
     * @return the average of all short values in this tuple
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
     * ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * ShortTuple3 reversed = tuple.reverse(); // (3, 2, 1)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified short value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * boolean has2 = tuple.contains((short) 2); // true
     * boolean has5 = tuple.contains((short) 5); // false
     * }</pre>
     *
     * @param valueToFind the short value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(short valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * Modifications to the returned array do not affect the tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * short[] array = tuple.toArray(); // [1, 2, 3]
     * }</pre>
     *
     * @return a new short array containing all tuple elements
     */
    public short[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new ShortList containing all elements of this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * ShortList list = tuple.toList();
     * }</pre>
     *
     * @return a new ShortList containing all tuple elements
     */
    public ShortList toList() {
        return ShortList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * tuple.forEach(System.out::println); // prints each value
     * }</pre>
     *
     * @param <E> the type of exception that the consumer may throw
     * @param consumer the action to perform for each element
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * int sum = tuple.stream().sum(); // 6
     * }</pre>
     *
     * @return a ShortStream containing all tuple elements
     */
    public ShortStream stream() {
        return ShortStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * The hash code is computed based on all elements using {@link N#hashCode(short[])}.
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
            return N.equals(elements(), ((ShortTuple<TP>) obj).elements());
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
     * underlying array of short values. The returned array is lazily initialized
     * on first access and cached for subsequent calls.
     * </p>
     * <p>
     * Subclasses must implement this method to provide access to their elements.
     * Modifications to the returned array will affect the tuple's internal state.
     * </p>
     *
     * @return the array of short elements stored in this tuple
     */
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
         * Returns the minimum short value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public short min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the maximum short value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public short max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the median short value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public short median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the sum of all values in this tuple.
         * For an empty tuple, the sum is 0.
         *
         * @return 0
         */
        @Override
        public int sum() {
            return 0;
        }

        /**
         * Returns the average of all short values in this tuple.
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
        public ShortTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified value.
         * An empty tuple contains no values.
         *
         * @param valueToFind the value to search for
         * @return {@code false} always, as the tuple is empty
         */
        @Override
        public boolean contains(final short valueToFind) {
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
        protected short[] elements() {
            return N.EMPTY_SHORT_ARRAY;
        }
    }

    /**
     * A tuple containing exactly one short value.
     * The value is accessible through the public final field {@code _1}.
     * 
     * <p><b>Usage Examples:</b></p>
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
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if the value equals _1, {@code false} otherwise
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
         * @return {@code true} if the object is a ShortTuple1 with the same value
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
         * @return a string in the format "(value)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
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
     * <p><b>Usage Examples:</b></p>
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
         * Returns the median short value in this tuple.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower) short value
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
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if valueToFind equals _1 or _2
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
         * Performs the given bi-consumer on the two elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple2 pair = ShortTuple.of((short)3, (short)5);
         * pair.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the bi-consumer to perform on the two elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.ShortBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given bi-function to the two elements and returns the result.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple2 pair = ShortTuple.of((short)3, (short)5);
         * int product = pair.map((a, b) -> a * b); // 15
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the bi-function to apply to the two elements
         * @return the result of applying the mapper function
         * @throws E if the mapper throws an exception
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.ShortBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple2 pair = ShortTuple.of((short)3, (short)5);
         * Optional<ShortTuple2> result = pair.filter((a, b) -> a < b); // Optional containing the pair
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the bi-predicate to test the two elements
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
         * @return {@code true} if the object is a ShortTuple2 with the same values
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
         * @return a string in the format "(_1, _2)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
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
     * <p><b>Usage Examples:</b></p>
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
         * @return the middle short value when sorted
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
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if valueToFind equals _1, _2, or _3
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
         * Performs the given tri-consumer on the three elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple3 triple = ShortTuple.of((short)2, (short)3, (short)5);
         * triple.accept((a, b, c) -> System.out.println(a + " + " + b + " + " + c + " = " + (a + b + c)));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the tri-consumer to perform on the three elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.ShortTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given tri-function to the three elements and returns the result.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple3 triple = ShortTuple.of((short)2, (short)3, (short)5);
         * int product = triple.map((a, b, c) -> a * b * c); // 30
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the tri-function to apply to the three elements
         * @return the result of applying the mapper function
         * @throws E if the mapper throws an exception
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.ShortTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple3 triple = ShortTuple.of((short)2, (short)3, (short)5);
         * Optional<ShortTuple3> result = triple.filter((a, b, c) -> a < b && b < c); // Optional containing the triple
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the tri-predicate to test the three elements
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
         * @return {@code true} if the object is a ShortTuple3 with the same values
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
         * @return a string in the format "(_1, _2, _3)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
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
     * <p><b>Usage Examples:</b></p>
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
        public short min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum value among the four elements.
         *
         * @return the largest of _1, _2, _3, and _4
         */
        @Override
        public short max() {
            return N.max(elements());
        }

        /**
         * Returns the median value of the four elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) short value when sorted
         */
        @Override
        public short median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all four elements.
         *
         * @return _1 + _2 + _3 + _4 as an int
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all four elements.
         *
         * @return (_1 + _2 + _3 + _4) / 4.0 as a double
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if valueToFind equals any of the four elements
         */
        @Override
        public boolean contains(final short valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
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
         * Two tuples are equal if they are both ShortTuple4 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple4 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ShortTuple4 other)) {
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
     * <p><b>Usage Examples:</b></p>
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
        public short min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum value among the five elements.
         *
         * @return the largest of _1, _2, _3, _4, and _5
         */
        @Override
        public short max() {
            return N.max(elements());
        }

        /**
         * Returns the median value of the five elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median short value
         */
        @Override
        public short median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all five elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 as an int
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all five elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5) / 5.0 as a double
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if valueToFind equals any of the five elements
         */
        @Override
        public boolean contains(final short valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
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
         * Two tuples are equal if they are both ShortTuple5 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple5 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ShortTuple5 other)) {
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
     * <p><b>Usage Examples:</b></p>
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
        public short min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum value among the six elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public short max() {
            return N.max(elements());
        }

        /**
         * Returns the median value of the six elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) short value when sorted
         */
        @Override
        public short median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all six elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 as an int
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all six elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6) / 6.0 as a double
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if valueToFind equals any of the six elements
         */
        @Override
        public boolean contains(final short valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
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
         * Two tuples are equal if they are both ShortTuple6 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple6 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ShortTuple6 other)) {
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
     * <p><b>Usage Examples:</b></p>
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
        public short min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum value among the seven elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public short max() {
            return N.max(elements());
        }

        /**
         * Returns the median value of the seven elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median short value
         */
        @Override
        public short median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all seven elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 as an int
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all seven elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7) / 7.0 as a double
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if valueToFind equals any of the seven elements
         */
        @Override
        public boolean contains(final short valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
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
         * Two tuples are equal if they are both ShortTuple7 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple7 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ShortTuple7 other)) {
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple8 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4,
     *                                    (short)5, (short)6, (short)7, (short)8);
     * boolean contains5 = tuple.contains((short)5); // true
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more short values
     */
    @Deprecated
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
        public short min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum value among the eight elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, and _8
         */
        @Override
        public short max() {
            return N.max(elements());
        }

        /**
         * Returns the median value of the eight elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) short value when sorted
         */
        @Override
        public short median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all eight elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 as an int
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all eight elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8) / 8.0 as a double
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if valueToFind equals any of the eight elements
         */
        @Override
        public boolean contains(final short valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
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
         * Two tuples are equal if they are both ShortTuple8 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple8 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ShortTuple8 other)) {
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple9 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5,
     *                                    (short)6, (short)7, (short)8, (short)9);
     * double avg = tuple.average(); // 5.0
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more short values
     */
    @Deprecated
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
        public short min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum value among the nine elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, _8, and _9
         */
        @Override
        public short max() {
            return N.max(elements());
        }

        /**
         * Returns the median value of the nine elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median short value
         */
        @Override
        public short median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all nine elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9 as an int
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all nine elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9) / 9.0 as a double
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if valueToFind equals any of the nine elements
         */
        @Override
        public boolean contains(final short valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
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
         * Two tuples are equal if they are both ShortTuple9 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple9 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ShortTuple9 other)) {
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
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}