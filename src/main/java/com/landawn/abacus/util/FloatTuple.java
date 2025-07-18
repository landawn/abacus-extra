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
import com.landawn.abacus.util.stream.FloatStream;

/**
 * Abstract base class for immutable tuples containing float primitive values.
 * This class provides a type-safe way to work with fixed-size collections of float values.
 * 
 * <p>FloatTuple and its subclasses offer:</p>
 * <ul>
 *   <li>Type safety for float collections of known size</li>
 *   <li>Immutability for thread-safe operations</li>
 *   <li>Convenient factory methods and utilities</li>
 *   <li>Statistical operations (min, max, median, sum, average)</li>
 * </ul>
 * 
 * @param <TP> The specific FloatTuple subtype for fluent method chaining
 * @author HaiYang Li
 * @since 3.0
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class FloatTuple<TP extends FloatTuple<TP>> extends PrimitiveTuple<TP> {

    protected float[] elements;

    /**
     * Creates a FloatTuple1 containing a single float value.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple1 single = FloatTuple.of(3.14f);
     * float value = single._1; // 3.14f
     * }</pre>
     *
     * @param _1 the float value to wrap in a tuple
     * @return a new FloatTuple1 containing the provided value
     */
    public static FloatTuple1 of(final float _1) {
        return new FloatTuple1(_1);
    }

    /**
     * Creates a FloatTuple2 containing two float values.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
     * float sum = pair._1 + pair._2; // 4.0f
     * }</pre>
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @return a new FloatTuple2 containing the provided values
     */
    public static FloatTuple2 of(final float _1, final float _2) {
        return new FloatTuple2(_1, _2);
    }

    /**
     * Creates a FloatTuple3 containing three float values.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple3 triple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * double average = triple.average(); // 2.0
     * }</pre>
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @return a new FloatTuple3 containing the provided values
     */
    public static FloatTuple3 of(final float _1, final float _2, final float _3) {
        return new FloatTuple3(_1, _2, _3);
    }

    /**
     * Creates a FloatTuple4 containing four float values.
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @param _4 the fourth float value
     * @return a new FloatTuple4 containing the provided values
     */
    public static FloatTuple4 of(final float _1, final float _2, final float _3, final float _4) {
        return new FloatTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a FloatTuple5 containing five float values.
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @param _4 the fourth float value
     * @param _5 the fifth float value
     * @return a new FloatTuple5 containing the provided values
     */
    public static FloatTuple5 of(final float _1, final float _2, final float _3, final float _4, final float _5) {
        return new FloatTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a FloatTuple6 containing six float values.
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @param _4 the fourth float value
     * @param _5 the fifth float value
     * @param _6 the sixth float value
     * @return a new FloatTuple6 containing the provided values
     */
    public static FloatTuple6 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6) {
        return new FloatTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a FloatTuple7 containing seven float values.
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @param _4 the fourth float value
     * @param _5 the fifth float value
     * @param _6 the sixth float value
     * @param _7 the seventh float value
     * @return a new FloatTuple7 containing the provided values
     */
    public static FloatTuple7 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7) {
        return new FloatTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a FloatTuple8 containing eight float values.
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @param _4 the fourth float value
     * @param _5 the fifth float value
     * @param _6 the sixth float value
     * @param _7 the seventh float value
     * @param _8 the eighth float value
     * @return a new FloatTuple8 containing the provided values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static FloatTuple8 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7,
            final float _8) {
        return new FloatTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a FloatTuple9 containing nine float values.
     * 
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @param _4 the fourth float value
     * @param _5 the fifth float value
     * @param _6 the sixth float value
     * @param _7 the seventh float value
     * @param _8 the eighth float value
     * @param _9 the ninth float value
     * @return a new FloatTuple9 containing the provided values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static FloatTuple9 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7, final float _8,
            final float _9) {
        return new FloatTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a FloatTuple from an array of float values.
     * The size of the returned tuple depends on the array length (0-9).
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * float[] values = {1.0f, 2.0f, 3.0f};
     * FloatTuple3 tuple = FloatTuple.create(values);
     * }</pre>
     *
     * @param <TP> the specific FloatTuple type to return
     * @param a the array of float values (must have length 0-9)
     * @return a FloatTuple of appropriate size containing the array values
     * @throws IllegalArgumentException if the array has more than 9 elements
     */
    public static <TP extends FloatTuple<TP>> TP create(final float[] a) {
        if (a == null || a.length == 0) {
            return (TP) FloatTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) FloatTuple.of(a[0]);

            case 2:
                return (TP) FloatTuple.of(a[0], a[1]);

            case 3:
                return (TP) FloatTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     * Returns the minimum value among all elements in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
     * float min = tuple.min(); // 1.0f
     * }</pre>
     *
     * @return the minimum float value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public float min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum value among all elements in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
     * float max = tuple.max(); // 3.0f
     * }</pre>
     *
     * @return the maximum float value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public float max() {
        return N.max(elements());
    }

    /**
     * Returns the median value of all elements in this tuple.
     * For even-sized tuples, returns the average of the two middle values.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
     * float median = tuple.median(); // 2.5f
     * }</pre>
     *
     * @return the median float value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public float median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all elements in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * float sum = tuple.sum(); // 6.0f
     * }</pre>
     *
     * @return the sum of all float values in this tuple
     */
    public float sum() {
        return N.sum(elements());
    }

    /**
     * Returns the arithmetic mean of all elements in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * double avg = tuple.average(); // 2.0
     * }</pre>
     *
     * @return the average of all float values in this tuple as a double
     * @throws NoSuchElementException if the tuple is empty
     */
    public double average() {
        return N.average(elements());
    }

    /**
     * Returns a new tuple with elements in reversed order.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * FloatTuple3 reversed = tuple.reverse(); // (3.0f, 2.0f, 1.0f)
     * }</pre>
     *
     * @return a new tuple with elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks whether this tuple contains the specified float value.
     * Uses {@link N#equals(float, float)} for comparison to handle NaN and precision.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * boolean hasTwo = tuple.contains(2.0f); // true
     * }</pre>
     *
     * @param valueToFind the float value to search for
     * @return true if the value is found in this tuple, false otherwise
     */
    public abstract boolean contains(float valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * Modifications to the returned array do not affect the tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * float[] array = tuple.toArray(); // [1.0f, 2.0f, 3.0f]
     * }</pre>
     *
     * @return a new float array containing all tuple elements
     */
    public float[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new FloatList containing all elements of this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * FloatList list = tuple.toList();
     * }</pre>
     *
     * @return a new FloatList containing all tuple elements
     */
    public FloatList toList() {
        return FloatList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * tuple.forEach(System.out::println); // prints each value
     * }</pre>
     *
     * @param <E> the type of exception that the consumer may throw
     * @param consumer the action to perform for each element
     * @throws E if the consumer throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
        for (final float e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns a FloatStream of all elements in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * float sum = tuple.stream().sum(); // 6.0f
     * }</pre>
     *
     * @return a FloatStream containing all tuple elements
     */
    public FloatStream stream() {
        return FloatStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * The hash code is computed based on all elements using {@link N#hashCode(float[])}.
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
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        } else {
            return N.equals(elements(), ((FloatTuple<TP>) obj).elements());
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
     * Returns the internal array of elements.
     * Subclasses must implement this to provide access to their elements.
     *
     * @return the array of float elements
     */
    protected abstract float[] elements();

    /**
     * An empty FloatTuple containing no elements.
     * This class is used as a singleton for efficiency.
     */
    static final class FloatTuple0 extends FloatTuple<FloatTuple0> {

        private static final FloatTuple0 EMPTY = new FloatTuple0();

        FloatTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public float min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public float max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public float median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public float sum() {
            return 0;
        }

        @Override
        public double average() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public FloatTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final float valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected float[] elements() {
            return N.EMPTY_FLOAT_ARRAY;
        }
    }

    /**
     * A FloatTuple containing exactly one float value.
     * Provides direct access to the element via the public final field {@code _1}.
     */
    public static final class FloatTuple1 extends FloatTuple<FloatTuple1> {

        /** The single float value in this tuple. */
        public final float _1;

        FloatTuple1() {
            this(0);
        }

        FloatTuple1(final float _1) {
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
        public float min() {
            return _1;
        }

        /**
         * Returns the maximum value in this tuple, which is the single element.
         *
         * @return the value of _1
         */
        @Override
        public float max() {
            return _1;
        }

        /**
         * Returns the median value in this tuple, which is the single element.
         *
         * @return the value of _1
         */
        @Override
        public float median() {
            return _1;
        }

        /**
         * Returns the sum of elements in this tuple, which is the single element.
         *
         * @return the value of _1
         */
        @Override
        public float sum() {
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
         * Returns a new tuple with elements in reversed order.
         * For a single-element tuple, returns a copy of itself.
         *
         * @return a new FloatTuple1 with the same value
         */
        @Override
        public FloatTuple1 reverse() {
            return new FloatTuple1(_1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if _1 equals valueToFind, false otherwise
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind);
        }

        /**
         * Returns a hash code for this tuple based on its single element.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return (int) _1;
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return true if obj is a FloatTuple1 with equal value
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final FloatTuple1 other)) {
                return false;
            } else {
                return N.equals(_1, other._1);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly two float values.
     * Provides direct access to elements via public final fields {@code _1} and {@code _2}.
     */
    public static final class FloatTuple2 extends FloatTuple<FloatTuple2> {

        /** The first float value in this tuple. */
        public final float _1;
        /** The second float value in this tuple. */
        public final float _2;

        FloatTuple2() {
            this(0, 0);
        }

        FloatTuple2(final float _1, final float _2) {
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
        public float min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum value among the two elements.
         *
         * @return the larger of _1 and _2
         */
        @Override
        public float max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median value of the two elements.
         *
         * @return the average of _1 and _2
         */
        @Override
        public float median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of the two elements.
         *
         * @return _1 + _2
         */
        @Override
        public float sum() {
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
         * Returns a new tuple with elements in reversed order.
         *
         * @return a new FloatTuple2 with (_2, _1)
         */
        @Override
        public FloatTuple2 reverse() {
            return new FloatTuple2(_2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if either element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         * Performs the given binary action on the two elements.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
         * tuple.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the binary action to perform
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.FloatBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given binary function to the two elements and returns the result.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
         * float product = tuple.map((a, b) -> a * b); // 12.0f
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the binary function to apply
         * @return the result of applying the mapper to _1 and _2
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.FloatBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
         * Optional<FloatTuple2> result = tuple.filter((a, b) -> a + b > 5); // present
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the binary predicate to test
         * @return Optional containing this tuple if predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<FloatTuple2> filter(final Throwables.FloatBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on both elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return (int) (31 * _1 + _2);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return true if obj is a FloatTuple2 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final FloatTuple2 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly three float values.
     * Provides direct access to elements via public final fields {@code _1}, {@code _2}, and {@code _3}.
     */
    public static final class FloatTuple3 extends FloatTuple<FloatTuple3> {

        /** The first float value in this tuple. */
        public final float _1;
        /** The second float value in this tuple. */
        public final float _2;
        /** The third float value in this tuple. */
        public final float _3;

        FloatTuple3() {
            this(0, 0, 0);
        }

        FloatTuple3(final float _1, final float _2, final float _3) {
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
        public float min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum value among the three elements.
         *
         * @return the largest of _1, _2, and _3
         */
        @Override
        public float max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median value of the three elements.
         *
         * @return the middle value when sorted
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of the three elements.
         *
         * @return _1 + _2 + _3
         */
        @Override
        public float sum() {
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
         * Returns a new tuple with elements in reversed order.
         *
         * @return a new FloatTuple3 with (_3, _2, _1)
         */
        @Override
        public FloatTuple3 reverse() {
            return new FloatTuple3(_3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
        }

        /**
         * Performs the given ternary action on the three elements.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * tuple.accept((a, b, c) -> System.out.println("Sum: " + (a + b + c)));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the ternary action to perform
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.FloatTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given ternary function to the three elements and returns the result.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * float product = tuple.map((a, b, c) -> a * b * c); // 6.0f
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the ternary function to apply
         * @return the result of applying the mapper to _1, _2, and _3
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.FloatTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * Optional<FloatTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5); // present
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the ternary predicate to test
         * @return Optional containing this tuple if predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<FloatTuple3> filter(final Throwables.FloatTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on all three elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return (int) ((31 * (31 * _1 + _2)) + _3);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return true if obj is a FloatTuple3 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final FloatTuple3 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly four float values.
     * Provides direct access to elements via public final fields.
     */
    public static final class FloatTuple4 extends FloatTuple<FloatTuple4> {

        /** The first float value in this tuple. */
        public final float _1;
        /** The second float value in this tuple. */
        public final float _2;
        /** The third float value in this tuple. */
        public final float _3;
        /** The fourth float value in this tuple. */
        public final float _4;

        FloatTuple4() {
            this(0, 0, 0, 0);
        }

        FloatTuple4(final float _1, final float _2, final float _3, final float _4) {
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
         * Returns a new tuple with elements in reversed order.
         *
         * @return a new FloatTuple4 with (_4, _3, _2, _1)
         */
        @Override
        public FloatTuple4 reverse() {
            return new FloatTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly five float values.
     * Provides direct access to elements via public final fields.
     */
    public static final class FloatTuple5 extends FloatTuple<FloatTuple5> {

        /** The first float value in this tuple. */
        public final float _1;
        /** The second float value in this tuple. */
        public final float _2;
        /** The third float value in this tuple. */
        public final float _3;
        /** The fourth float value in this tuple. */
        public final float _4;
        /** The fifth float value in this tuple. */
        public final float _5;

        FloatTuple5() {
            this(0, 0, 0, 0, 0);
        }

        FloatTuple5(final float _1, final float _2, final float _3, final float _4, final float _5) {
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
         * Returns a new tuple with elements in reversed order.
         *
         * @return a new FloatTuple5 with (_5, _4, _3, _2, _1)
         */
        @Override
        public FloatTuple5 reverse() {
            return new FloatTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind)
                    || N.equals(_5, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly six float values.
     * Provides direct access to elements via public final fields.
     */
    public static final class FloatTuple6 extends FloatTuple<FloatTuple6> {

        /** The first float value in this tuple. */
        public final float _1;
        /** The second float value in this tuple. */
        public final float _2;
        /** The third float value in this tuple. */
        public final float _3;
        /** The fourth float value in this tuple. */
        public final float _4;
        /** The fifth float value in this tuple. */
        public final float _5;
        /** The sixth float value in this tuple. */
        public final float _6;

        FloatTuple6() {
            this(0, 0, 0, 0, 0, 0);
        }

        FloatTuple6(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6) {
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
         * Returns a new tuple with elements in reversed order.
         *
         * @return a new FloatTuple6 with (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public FloatTuple6 reverse() {
            return new FloatTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly seven float values.
     * Provides direct access to elements via public final fields.
     */
    public static final class FloatTuple7 extends FloatTuple<FloatTuple7> {

        /** The first float value in this tuple. */
        public final float _1;
        /** The second float value in this tuple. */
        public final float _2;
        /** The third float value in this tuple. */
        public final float _3;
        /** The fourth float value in this tuple. */
        public final float _4;
        /** The fifth float value in this tuple. */
        public final float _5;
        /** The sixth float value in this tuple. */
        public final float _6;
        /** The seventh float value in this tuple. */
        public final float _7;

        FloatTuple7() {
            this(0, 0, 0, 0, 0, 0, 0);
        }

        FloatTuple7(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7) {
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
         * Returns a new tuple with elements in reversed order.
         *
         * @return a new FloatTuple7 with (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public FloatTuple7 reverse() {
            return new FloatTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly eight float values.
     * Provides direct access to elements via public final fields.
     * 
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    public static final class FloatTuple8 extends FloatTuple<FloatTuple8> {

        /** The first float value in this tuple. */
        public final float _1;
        /** The second float value in this tuple. */
        public final float _2;
        /** The third float value in this tuple. */
        public final float _3;
        /** The fourth float value in this tuple. */
        public final float _4;
        /** The fifth float value in this tuple. */
        public final float _5;
        /** The sixth float value in this tuple. */
        public final float _6;
        /** The seventh float value in this tuple. */
        public final float _7;
        /** The eighth float value in this tuple. */
        public final float _8;

        FloatTuple8() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }

        FloatTuple8(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7, final float _8) {
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
         * Returns a new tuple with elements in reversed order.
         *
         * @return a new FloatTuple8 with elements in reverse order
         */
        @Override
        public FloatTuple8 reverse() {
            return new FloatTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly nine float values.
     * Provides direct access to elements via public final fields.
     * 
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    public static final class FloatTuple9 extends FloatTuple<FloatTuple9> {

        /** The first float value in this tuple. */
        public final float _1;
        /** The second float value in this tuple. */
        public final float _2;
        /** The third float value in this tuple. */
        public final float _3;
        /** The fourth float value in this tuple. */
        public final float _4;
        /** The fifth float value in this tuple. */
        public final float _5;
        /** The sixth float value in this tuple. */
        public final float _6;
        /** The seventh float value in this tuple. */
        public final float _7;
        /** The eighth float value in this tuple. */
        public final float _8;
        /** The ninth float value in this tuple. */
        public final float _9;

        FloatTuple9() {
            this(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        FloatTuple9(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7, final float _8,
                final float _9) {
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
         * Returns a new tuple with elements in reversed order.
         *
         * @return a new FloatTuple9 with elements in reverse order
         */
        @Override
        public FloatTuple9 reverse() {
            return new FloatTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return true if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind) || N.equals(_9, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}