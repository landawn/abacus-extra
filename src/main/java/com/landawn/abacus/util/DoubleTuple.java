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
import com.landawn.abacus.util.stream.DoubleStream;

/**
 * Abstract base class for immutable tuples containing double primitive values.
 * This class provides a type-safe way to work with fixed-size collections of double values.
 *
 * <p>DoubleTuple and its subclasses offer:</p>
 * <ul>
 *   <li>Type safety for double collections of known size</li>
 *   <li>Immutability for thread-safe operations</li>
 *   <li>Convenient factory methods and utilities</li>
 *   <li>Statistical operations (min, max, median, sum, average)</li>
 * </ul>
 *
 * @param <TP> The specific DoubleTuple subtype for fluent method chaining
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class DoubleTuple<TP extends DoubleTuple<TP>> extends PrimitiveTuple<TP> {

    /**
     * Protected constructor for subclass instantiation.
     * <p>
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link DoubleTuple1#of(double)}, {@link DoubleTuple2#of(double, double)}, etc.,
     * to create tuple instances.
     */
    protected DoubleTuple() {
    }

    protected double[] elements;

    /**
     * Creates a DoubleTuple1 containing a single double value.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple1 single = DoubleTuple.of(3.14);
     * double value = single._1; // 3.14
     * }</pre>
     *
     * @param _1 the double value to wrap in a tuple
     * @return a new DoubleTuple1 containing the provided value
     */
    public static DoubleTuple1 of(final double _1) {
        return new DoubleTuple1(_1);
    }

    /**
     * Creates a DoubleTuple2 containing two double values.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * double sum = pair._1 + pair._2; // 4.0
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @return a new DoubleTuple2 containing the provided values
     */
    public static DoubleTuple2 of(final double _1, final double _2) {
        return new DoubleTuple2(_1, _2);
    }

    /**
     * Creates a DoubleTuple3 containing three double values.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple3 triple = DoubleTuple.of(1.0, 2.0, 3.0);
     * double average = triple.average(); // 2.0
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @return a new DoubleTuple3 containing the provided values
     */
    public static DoubleTuple3 of(final double _1, final double _2, final double _3) {
        return new DoubleTuple3(_1, _2, _3);
    }

    /**
     * Creates a DoubleTuple4 containing four double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
     * double first = tuple._1;
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @return a new DoubleTuple4 containing the provided values
     */
    public static DoubleTuple4 of(final double _1, final double _2, final double _3, final double _4) {
        return new DoubleTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a DoubleTuple5 containing five double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
     * double first = tuple._1;
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @param _5 the fifth double value
     * @return a new DoubleTuple5 containing the provided values
     */
    public static DoubleTuple5 of(final double _1, final double _2, final double _3, final double _4, final double _5) {
        return new DoubleTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a DoubleTuple6 containing six double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
     * double first = tuple._1;
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @param _5 the fifth double value
     * @param _6 the sixth double value
     * @return a new DoubleTuple6 containing the provided values
     */
    public static DoubleTuple6 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6) {
        return new DoubleTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a DoubleTuple7 containing seven double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
     * double first = tuple._1;
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @param _5 the fifth double value
     * @param _6 the sixth double value
     * @param _7 the seventh double value
     * @return a new DoubleTuple7 containing the provided values
     */
    public static DoubleTuple7 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7) {
        return new DoubleTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a DoubleTuple8 containing eight double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
     * double first = tuple._1;
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @param _5 the fifth double value
     * @param _6 the sixth double value
     * @param _7 the seventh double value
     * @param _8 the eighth double value
     * @return a new DoubleTuple8 containing the provided values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static DoubleTuple8 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7,
            final double _8) {
        return new DoubleTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a DoubleTuple9 containing nine double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
     * double first = tuple._1;
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @param _5 the fifth double value
     * @param _6 the sixth double value
     * @param _7 the seventh double value
     * @param _8 the eighth double value
     * @param _9 the ninth double value
     * @return a new DoubleTuple9 containing the provided values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static DoubleTuple9 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7,
            final double _8, final double _9) {
        return new DoubleTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a DoubleTuple from an array of double values.
     * The size of the returned tuple depends on the array length (0-9).
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * double[] values = {1.0, 2.0, 3.0};
     * DoubleTuple3 tuple = DoubleTuple.create(values);
     * // tuple._1 == 1.0, tuple._2 == 2.0, tuple._3 == 3.0
     * }</pre>
     *
     * @param <TP> the specific DoubleTuple type to return
     * @param a the array of double values (must have length 0-9)
     * @return a DoubleTuple of appropriate size containing the array values
     * @throws IllegalArgumentException if the array has more than 9 elements
     */
    public static <TP extends DoubleTuple<TP>> TP create(final double[] a) {
        if (a == null || a.length == 0) {
            return (TP) DoubleTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) DoubleTuple.of(a[0]);

            case 2:
                return (TP) DoubleTuple.of(a[0], a[1]);

            case 3:
                return (TP) DoubleTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     * Returns the minimum double value in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
     * double min = tuple.min(); // 1.0
     * }</pre>
     *
     * @return the minimum double value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public double min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum double value in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
     * double max = tuple.max(); // 3.0
     * }</pre>
     *
     * @return the maximum double value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public double max() {
        return N.max(elements());
    }

    /**
     * Returns the median double value in this tuple.
     * For tuples with an even number of elements, returns the lower middle value.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple3 tuple = DoubleTuple.of(30.0, 10.0, 20.0);
     * double median = tuple.median(); // 20.0
     * 
     * DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
     * double median = tuple.median(); // 2.0
     * }</pre> 
     *
     * @return the median double value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public double median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all elements in this tuple.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * double sum = tuple.sum(); // 6.0
     * }</pre>
     *
     * @return the sum of all double values in this tuple
     */
    public double sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all double values in this tuple as a double.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * double avg = tuple.average(); // 2.0
     * }</pre>
     *
     * @return the average of all double values as a double
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
     * DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * DoubleTuple3 reversed = tuple.reverse(); // (3.0, 2.0, 1.0)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified double value.
     * Uses {@link N#equals(double, double)} for comparison to handle NaN and precision.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * boolean hasTwo = tuple.contains(2.0); // true
     * boolean hasFive = tuple.contains(5.0); // false
     * }</pre>
     *
     * @param valueToFind the double value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(double valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * Modifications to the returned array do not affect the tuple.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * double[] array = tuple.toArray(); // [1.0, 2.0, 3.0]
     * }</pre>
     *
     * @return a new double array containing all tuple elements
     */
    public double[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new DoubleList containing all elements of this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * DoubleList list = tuple.toList();
     * }</pre>
     *
     * @return a new DoubleList containing all tuple elements
     */
    public DoubleList toList() {
        return DoubleList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * tuple.forEach(System.out::println); // prints each value
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown
     * @param consumer the action to perform for each element
     * @throws E if the consumer throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> consumer) throws E {
        for (final double e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns a DoubleStream of all elements in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * double sum = tuple.stream().sum(); // 6.0
     * }</pre>
     *
     * @return a DoubleStream containing all tuple elements
     */
    public DoubleStream stream() {
        return DoubleStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * The hash code is computed based on all elements using {@link N#hashCode(double[])}.
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
            return N.equals(elements(), ((DoubleTuple<TP>) obj).elements());
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
     * @return the array of double elements
     */
    protected abstract double[] elements();

    /**
     * An empty DoubleTuple containing no elements.
     * This class is used as a singleton for efficiency.
     */
    static final class DoubleTuple0 extends DoubleTuple<DoubleTuple0> {

        private static final DoubleTuple0 EMPTY = new DoubleTuple0();

        DoubleTuple0() {
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
         * Returns the minimum value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, as there are no elements
         */
        @Override
        public double min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the maximum value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, as there are no elements
         */
        @Override
        public double max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the median value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, as there are no elements
         */
        @Override
        public double median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the sum of all elements in this tuple.
         * For an empty tuple, the sum is 0.
         *
         * @return 0
         */
        @Override
        public double sum() {
            return 0;
        }

        /**
         * Returns the average of all elements in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, as there are no elements
         */
        @Override
        public double average() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns a reversed version of this tuple.
         * For an empty tuple, returns the same instance.
         *
         * @return this instance
         */
        @Override
        public DoubleTuple0 reverse() {
            return this;
        }

        /**
         * Checks whether this tuple contains the specified value.
         * Since this tuple is empty, this method always returns false.
         *
         * @param valueToFind the value to search for
         * @return false always, as there are no elements
         */
        @Override
        public boolean contains(final double valueToFind) {
            return false;
        }

        /**
         * Returns a string representation of this empty tuple.
         *
         * @return "[]"
         */
        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected double[] elements() {
            return N.EMPTY_DOUBLE_ARRAY;
        }
    }

    /**
     * A DoubleTuple containing exactly one double value.
     * Provides direct access to the element via the public final field {@code _1}.
     */
    public static final class DoubleTuple1 extends DoubleTuple<DoubleTuple1> {

        /** The single double value in this tuple. */
        public final double _1;

        DoubleTuple1() {
            this(0);
        }

        DoubleTuple1(final double _1) {
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
        public double min() {
            return _1;
        }

        /**
         * Returns the maximum value in this tuple, which is the single element.
         *
         * @return the value of _1
         */
        @Override
        public double max() {
            return _1;
        }

        /**
         * Returns the median value in this tuple, which is the single element.
         *
         * @return the value of _1
         */
        @Override
        public double median() {
            return _1;
        }

        /**
         * Returns the sum of elements in this tuple, which is the single element.
         *
         * @return the value of _1
         */
        @Override
        public double sum() {
            return _1;
        }

        /**
         * Returns the average of elements in this tuple, which is the single element.
         *
         * @return the value of _1
         */
        @Override
        public double average() {
            return _1;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         * For a single-element tuple, returns a copy of itself.
         *
         * @return a new DoubleTuple1 with the same value
         */
        @Override
        public DoubleTuple1 reverse() {
            return new DoubleTuple1(_1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if _1 equals valueToFind, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind);
        }

        /**
         * Returns a hash code for this tuple based on its single element.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return Double.hashCode(_1);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple1 with equal value
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple1 other)) {
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
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly two double values.
     * Provides direct access to elements via public final fields {@code _1} and {@code _2}.
     */
    public static final class DoubleTuple2 extends DoubleTuple<DoubleTuple2> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;

        DoubleTuple2() {
            this(0, 0);
        }

        DoubleTuple2(final double _1, final double _2) {
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
        public double min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum value among the two elements.
         *
         * @return the larger of _1 and _2
         */
        @Override
        public double max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median double value in this tuple.
         * For a tuple of two elements, returns the lower value.
         *
         * @return the median (lower) double value
         */
        @Override
        public double median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of the two elements.
         *
         * @return _1 + _2
         */
        @Override
        public double sum() {
            return N.sum(_1, _2);
        }

        /**
         * Returns the average of the two elements.
         *
         * @return (_1 + _2) / 2
         */
        @Override
        public double average() {
            return N.average(_1, _2);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new DoubleTuple2 with (_2, _1)
         */
        @Override
        public DoubleTuple2 reverse() {
            return new DoubleTuple2(_2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if either element equals valueToFind
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         * Performs the given binary action on the two elements.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
         * tuple.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
         * @param action the binary action to perform
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.DoubleBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given binary function to the two elements and returns the result.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
         * double product = tuple.map((a, b) -> a * b); // 12.0
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown
         * @param mapper the binary function to apply
         * @return the result of applying the mapper to _1 and _2
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.DoubleBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
         * Optional<DoubleTuple2> result = tuple.filter((a, b) -> a + b > 5); // present
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
         * @param predicate the binary predicate to test
         * @return Optional containing this tuple if predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<DoubleTuple2> filter(final Throwables.DoubleBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on both elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            long bits1 = Double.doubleToLongBits(_1);
            long bits2 = Double.doubleToLongBits(_2);
            return (int) (31 * (bits1 ^ (bits1 >>> 32)) + (bits2 ^ (bits2 >>> 32)));
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple2 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple2 other)) {
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
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly three double values.
     * Provides direct access to elements via public final fields {@code _1}, {@code _2}, and {@code _3}.
     */
    public static final class DoubleTuple3 extends DoubleTuple<DoubleTuple3> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;

        DoubleTuple3() {
            this(0, 0, 0);
        }

        DoubleTuple3(final double _1, final double _2, final double _3) {
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
        public double min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum value among the three elements.
         *
         * @return the largest of _1, _2, and _3
         */
        @Override
        public double max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median value of the three elements.
         *
         * @return the middle value when sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of the three elements.
         *
         * @return _1 + _2 + _3
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3);
        }

        /**
         * Returns the average of the three elements.
         *
         * @return (_1 + _2 + _3) / 3
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new DoubleTuple3 with (_3, _2, _1)
         */
        @Override
        public DoubleTuple3 reverse() {
            return new DoubleTuple3(_3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
        }

        /**
         * Performs the given ternary action on the three elements.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
         * tuple.accept((a, b, c) -> System.out.println("Sum: " + (a + b + c)));
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
         * @param action the ternary action to perform
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.DoubleTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given ternary function to the three elements and returns the result.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
         * double product = tuple.map((a, b, c) -> a * b * c); // 6.0
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown
         * @param mapper the ternary function to apply
         * @return the result of applying the mapper to _1, _2, and _3
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.DoubleTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
         * Optional<DoubleTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5); // present
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
         * @param predicate the ternary predicate to test
         * @return Optional containing this tuple if predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<DoubleTuple3> filter(final Throwables.DoubleTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on all three elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            return 31 * result + Double.hashCode(_3);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple3 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple3 other)) {
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
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly four double values.
     * Provides direct access to elements via public final fields.
     */
    public static final class DoubleTuple4 extends DoubleTuple<DoubleTuple4> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;

        DoubleTuple4() {
            this(0, 0, 0, 0);
        }

        DoubleTuple4(final double _1, final double _2, final double _3, final double _4) {
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
        public double min() {
            return N.min(_1, _2, _3, _4);
        }

        /**
         * Returns the maximum value among the four elements.
         *
         * @return the largest of _1, _2, _3, and _4
         */
        @Override
        public double max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the median value of the four elements.
         * For a tuple with an even number of elements, returns the lower middle value.
         *
         * @return the median (lower middle) value when sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of the four elements.
         *
         * @return _1 + _2 + _3 + _4
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4);
        }

        /**
         * Returns the average of the four elements.
         *
         * @return (_1 + _2 + _3 + _4) / 4
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new DoubleTuple4 with (_4, _3, _2, _1)
         */
        @Override
        public DoubleTuple4 reverse() {
            return new DoubleTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
        }

        /**
         * Returns a hash code for this tuple based on all four elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            return 31 * result + Double.hashCode(_4);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple4 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple4 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4);
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return "[_1, _2, _3, _4]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + "]";
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly five double values.
     * Provides direct access to elements via public final fields.
     */
    public static final class DoubleTuple5 extends DoubleTuple<DoubleTuple5> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;
        /** The fifth double value in this tuple. */
        public final double _5;

        DoubleTuple5() {
            this(0, 0, 0, 0, 0);
        }

        DoubleTuple5(final double _1, final double _2, final double _3, final double _4, final double _5) {
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
        public double min() {
            return N.min(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the maximum value among the five elements.
         *
         * @return the largest of _1, _2, _3, _4, and _5
         */
        @Override
        public double max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the median value of the five elements.
         *
         * @return the middle value when sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of the five elements.
         *
         * @return _1 + _2 + _3 + _4 + _5
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the average of the five elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5) / 5
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new DoubleTuple5 with (_5, _4, _3, _2, _1)
         */
        @Override
        public DoubleTuple5 reverse() {
            return new DoubleTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind)
                    || N.equals(_5, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
        }

        /**
         * Returns a hash code for this tuple based on all five elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            result = 31 * result + Double.hashCode(_4);
            return 31 * result + Double.hashCode(_5);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple5 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple5 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4) && N.equals(_5, other._5);
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return "[_1, _2, _3, _4, _5]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + "]";
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly six double values.
     * Provides direct access to elements via public final fields.
     */
    public static final class DoubleTuple6 extends DoubleTuple<DoubleTuple6> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;
        /** The fifth double value in this tuple. */
        public final double _5;
        /** The sixth double value in this tuple. */
        public final double _6;

        DoubleTuple6() {
            this(0, 0, 0, 0, 0, 0);
        }

        DoubleTuple6(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6) {
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
        public double min() {
            return N.min(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the maximum value among the six elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public double max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median value of the six elements.
         * For a tuple with an even number of elements, returns the lower middle value.
         *
         * @return the median (lower middle) value when sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of the six elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the average of the six elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6) / 6
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new DoubleTuple6 with (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public DoubleTuple6 reverse() {
            return new DoubleTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
        }

        /**
         * Returns a hash code for this tuple based on all six elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            result = 31 * result + Double.hashCode(_4);
            result = 31 * result + Double.hashCode(_5);
            return 31 * result + Double.hashCode(_6);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple6 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple6 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4) && N.equals(_5, other._5)
                        && N.equals(_6, other._6);
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return "[_1, _2, _3, _4, _5, _6]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + "]";
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly seven double values.
     * Provides direct access to elements via public final fields.
     */
    public static final class DoubleTuple7 extends DoubleTuple<DoubleTuple7> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;
        /** The fifth double value in this tuple. */
        public final double _5;
        /** The sixth double value in this tuple. */
        public final double _6;
        /** The seventh double value in this tuple. */
        public final double _7;

        DoubleTuple7() {
            this(0, 0, 0, 0, 0, 0, 0);
        }

        DoubleTuple7(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7) {
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
        public double min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the maximum value among the seven elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public double max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the median value of the seven elements.
         *
         * @return the middle value when sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of the seven elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the average of the seven elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7) / 7
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new DoubleTuple7 with (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public DoubleTuple7 reverse() {
            return new DoubleTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
            consumer.accept(_7);
        }

        /**
         * Returns a hash code for this tuple based on all seven elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            result = 31 * result + Double.hashCode(_4);
            result = 31 * result + Double.hashCode(_5);
            result = 31 * result + Double.hashCode(_6);
            return 31 * result + Double.hashCode(_7);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple7 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple7 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4) && N.equals(_5, other._5)
                        && N.equals(_6, other._6) && N.equals(_7, other._7);
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return "[_1, _2, _3, _4, _5, _6, _7]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + "]";
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly eight double values.
     * Provides direct access to elements via public final fields.
     * 
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static final class DoubleTuple8 extends DoubleTuple<DoubleTuple8> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;
        /** The fifth double value in this tuple. */
        public final double _5;
        /** The sixth double value in this tuple. */
        public final double _6;
        /** The seventh double value in this tuple. */
        public final double _7;
        /** The eighth double value in this tuple. */
        public final double _8;

        DoubleTuple8() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }

        DoubleTuple8(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7, final double _8) {
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
        public double min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the maximum value among the eight elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, and _8
         */
        @Override
        public double max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the median value of the eight elements.
         * For a tuple with an even number of elements, returns the lower middle value.
         *
         * @return the median (lower middle) value when sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of the eight elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the average of the eight elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8) / 8
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new DoubleTuple8 with (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public DoubleTuple8 reverse() {
            return new DoubleTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> consumer) throws E {
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
         * Returns a hash code for this tuple based on all eight elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            result = 31 * result + Double.hashCode(_4);
            result = 31 * result + Double.hashCode(_5);
            result = 31 * result + Double.hashCode(_6);
            result = 31 * result + Double.hashCode(_7);
            return 31 * result + Double.hashCode(_8);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple8 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple8 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4) && N.equals(_5, other._5)
                        && N.equals(_6, other._6) && N.equals(_7, other._7) && N.equals(_8, other._8);
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return "[_1, _2, _3, _4, _5, _6, _7, _8]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + "]";
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly nine double values.
     * Provides direct access to elements via public final fields.
     * 
     * @deprecated Consider using a custom class with meaningful property names for better code clarity
     */
    @Deprecated
    public static final class DoubleTuple9 extends DoubleTuple<DoubleTuple9> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;
        /** The fifth double value in this tuple. */
        public final double _5;
        /** The sixth double value in this tuple. */
        public final double _6;
        /** The seventh double value in this tuple. */
        public final double _7;
        /** The eighth double value in this tuple. */
        public final double _8;
        /** The ninth double value in this tuple. */
        public final double _9;

        DoubleTuple9() {
            this(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        DoubleTuple9(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7, final double _8,
                final double _9) {
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
        public double min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the maximum value among the nine elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, _8, and _9
         */
        @Override
        public double max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the median value of the nine elements.
         *
         * @return the middle value when sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of the nine elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the average of the nine elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9) / 9
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new DoubleTuple9 with (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public DoubleTuple9 reverse() {
            return new DoubleTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind) || N.equals(_9, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to perform
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> consumer) throws E {
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
         * Returns a hash code for this tuple based on all nine elements.
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            result = 31 * result + Double.hashCode(_4);
            result = 31 * result + Double.hashCode(_5);
            result = 31 * result + Double.hashCode(_6);
            result = 31 * result + Double.hashCode(_7);
            result = 31 * result + Double.hashCode(_8);
            return 31 * result + Double.hashCode(_9);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple9 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple9 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4) && N.equals(_5, other._5)
                        && N.equals(_6, other._6) && N.equals(_7, other._7) && N.equals(_8, other._8) && N.equals(_9, other._9);
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return "[_1, _2, _3, _4, _5, _6, _7, _8, _9]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + "]";
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}