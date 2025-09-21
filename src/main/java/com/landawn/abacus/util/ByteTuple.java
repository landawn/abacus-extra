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
import com.landawn.abacus.util.stream.ByteStream;

/**
 * Abstract base class for immutable tuple implementations that hold primitive byte values.
 * This class provides common functionality for byte-based tuples of various sizes (0 to 9 elements).
 * 
 * <p>ByteTuple is designed to be a lightweight, type-safe container for multiple byte values
 * that can be used as a composite key, return multiple values from a method, or group related
 * byte values together.</p>
 * 
 * <p>All tuple implementations are immutable and thread-safe.</p>
 * 
 * @param <TP> the specific ByteTuple subtype
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class ByteTuple<TP extends ByteTuple<TP>> extends PrimitiveTuple<TP> {

    protected byte[] elements;

    /**
     * Creates a ByteTuple1 containing a single byte value.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple1 tuple = ByteTuple.of((byte) 10);
     * byte value = tuple._1; // 10
     * }</pre>
     *
     * @param _1 the byte value to store in the tuple
     * @return a new ByteTuple1 containing the specified value
     */
    public static ByteTuple1 of(final byte _1) {
        return new ByteTuple1(_1);
    }

    /**
     * Creates a ByteTuple2 containing two byte values.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
     * byte first = tuple._1;  // 10
     * byte second = tuple._2; // 20
     * }</pre>
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @return a new ByteTuple2 containing the specified values
     */
    public static ByteTuple2 of(final byte _1, final byte _2) {
        return new ByteTuple2(_1, _2);
    }

    /**
     * Creates a ByteTuple3 containing three byte values.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * byte third = tuple._3; // 30
     * }</pre>
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @return a new ByteTuple3 containing the specified values
     */
    public static ByteTuple3 of(final byte _1, final byte _2, final byte _3) {
        return new ByteTuple3(_1, _2, _3);
    }

    /**
     * Creates a ByteTuple4 containing four byte values.
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @param _4 the fourth byte value
     * @return a new ByteTuple4 containing the specified values
     */
    public static ByteTuple4 of(final byte _1, final byte _2, final byte _3, final byte _4) {
        return new ByteTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a ByteTuple5 containing five byte values.
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @param _4 the fourth byte value
     * @param _5 the fifth byte value
     * @return a new ByteTuple5 containing the specified values
     */
    public static ByteTuple5 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5) {
        return new ByteTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a ByteTuple6 containing six byte values.
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @param _4 the fourth byte value
     * @param _5 the fifth byte value
     * @param _6 the sixth byte value
     * @return a new ByteTuple6 containing the specified values
     */
    public static ByteTuple6 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6) {
        return new ByteTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a ByteTuple7 containing seven byte values.
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @param _4 the fourth byte value
     * @param _5 the fifth byte value
     * @param _6 the sixth byte value
     * @param _7 the seventh byte value
     * @return a new ByteTuple7 containing the specified values
     */
    public static ByteTuple7 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7) {
        return new ByteTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a ByteTuple8 containing eight byte values.
     * 
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @param _4 the fourth byte value
     * @param _5 the fifth byte value
     * @param _6 the sixth byte value
     * @param _7 the seventh byte value
     * @param _8 the eighth byte value
     * @return a new ByteTuple8 containing the specified values
     * @deprecated you should consider using {@code class SomeClass { final T1 propName1, final T2 propName2...}}
     *             for better readability and maintainability when dealing with many values
     */
    @Deprecated
    public static ByteTuple8 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7, final byte _8) {
        return new ByteTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a ByteTuple9 containing nine byte values.
     * 
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @param _4 the fourth byte value
     * @param _5 the fifth byte value
     * @param _6 the sixth byte value
     * @param _7 the seventh byte value
     * @param _8 the eighth byte value
     * @param _9 the ninth byte value
     * @return a new ByteTuple9 containing the specified values
     * @deprecated you should consider using {@code class SomeClass { final T1 propName1, final T2 propName2...}}
     *             for better readability and maintainability when dealing with many values
     */
    @Deprecated
    public static ByteTuple9 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7, final byte _8,
            final byte _9) {
        return new ByteTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a ByteTuple from an array of byte values.
     * The size of the returned tuple depends on the length of the input array (0-9 elements).
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * byte[] values = {10, 20, 30};
     * ByteTuple3 tuple = ByteTuple.create(values);
     * }</pre>
     *
     * @param <TP> the specific ByteTuple subtype to return
     * @param a the array of byte values (must contain 0-9 elements)
     * @return a ByteTuple of appropriate size containing the array elements
     * @throws IllegalArgumentException if the array contains more than 9 elements
     */
    public static <TP extends ByteTuple<TP>> TP create(final byte[] a) {
        if (a == null || a.length == 0) {
            return (TP) ByteTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) ByteTuple.of(a[0]);

            case 2:
                return (TP) ByteTuple.of(a[0], a[1]);

            case 3:
                return (TP) ByteTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     * Returns the minimum byte value in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
     * byte min = tuple.min(); // 10
     * }</pre>
     *
     * @return the minimum byte value
     * @throws NoSuchElementException if the tuple is empty
     */
    public byte min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum byte value in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
     * byte max = tuple.max(); // 30
     * }</pre>
     *
     * @return the maximum byte value
     * @throws NoSuchElementException if the tuple is empty
     */
    public byte max() {
        return N.max(elements());
    }

    /**
     * Returns the median byte value in this tuple.
     * For tuples with an even number of elements, returns the lower middle value.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
     * byte median = tuple.median(); // 20
     * }</pre>
     *
     * @return the median byte value
     * @throws NoSuchElementException if the tuple is empty
     */
    public byte median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all byte values in this tuple as an integer.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * int sum = tuple.sum(); // 60
     * }</pre>
     *
     * @return the sum of all byte values as an integer
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all byte values in this tuple as a double.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * double avg = tuple.average(); // 20.0
     * }</pre>
     *
     * @return the average of all byte values
     * @throws NoSuchElementException if the tuple is empty
     */
    public double average() {
        return N.average(elements());
    }

    /**
     * Creates a new tuple with the elements in reversed order.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * ByteTuple3 reversed = tuple.reverse(); // contains 30, 20, 10
     * }</pre>
     *
     * @return a new tuple with elements in reversed order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified byte value.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * boolean has20 = tuple.contains((byte) 20); // true
     * boolean has40 = tuple.contains((byte) 40); // false
     * }</pre>
     *
     * @param valueToFind the byte value to search for
     * @return {@code true} if the value is found, {@code false} otherwise
     */
    public abstract boolean contains(byte valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * byte[] array = tuple.toArray(); // [10, 20, 30]
     * }</pre>
     *
     * @return a new byte array containing all tuple elements
     */
    public byte[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new ByteList containing all elements of this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * ByteList list = tuple.toList();
     * }</pre>
     *
     * @return a new ByteList containing all tuple elements
     */
    public ByteList toList() {
        return ByteList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * tuple.forEach(b -> System.out.println("Value: " + b));
     * }</pre>
     *
     * @param <E> the type of exception that the consumer may throw
     * @param consumer the action to be performed for each element
     * @throws E if the consumer throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
        for (final byte e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns a ByteStream of the elements in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * long count = tuple.stream().filter(b -> b > 15).count(); // 2
     * }</pre>
     *
     * @return a ByteStream containing all tuple elements
     */
    public ByteStream stream() {
        return ByteStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * The hash code is computed based on the contents of the tuple.
     *
     * @return a hash code value for this tuple
     */
    @Override
    public int hashCode() {
        return N.hashCode(elements());
    }

    /**
     * Compares this tuple to the specified object for equality.
     * Two tuples are equal if they are of the same class and contain the same elements in the same order.
     *
     * @param obj the object to be compared for equality with this tuple
     * @return {@code true} if the specified object is equal to this tuple
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        } else {
            return N.equals(elements(), ((ByteTuple<TP>) obj).elements());
        }
    }

    /**
     * Returns a string representation of this tuple.
     * The string representation consists of the tuple elements enclosed in square brackets
     * and separated by commas and spaces.
     * 
     * <p>Example: {@code [10, 20, 30]}</p>
     *
     * @return a string representation of this tuple
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    protected abstract byte[] elements();

    /**
     * An empty ByteTuple containing no elements.
     * This class represents a tuple with arity 0.
     */
    static final class ByteTuple0 extends ByteTuple<ByteTuple0> {

        private static final ByteTuple0 EMPTY = new ByteTuple0();

        ByteTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public byte min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public byte max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public byte median() {
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
        public ByteTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final byte valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected byte[] elements() {
            return N.EMPTY_BYTE_ARRAY;
        }
    }

    /**
     * A ByteTuple containing exactly one byte element.
     * Provides direct access to the element through the public final field {@code _1}.
     */
    public static final class ByteTuple1 extends ByteTuple<ByteTuple1> {

        /** The single byte value stored in this tuple. */
        public final byte _1;

        ByteTuple1() {
            this((byte) 0);
        }

        ByteTuple1(final byte _1) {
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
         * Returns the minimum byte value in this tuple.
         * Since this tuple contains only one element, it returns that element.
         *
         * @return the single byte value in this tuple
         */
        @Override
        public byte min() {
            return _1;
        }

        /**
         * Returns the maximum byte value in this tuple.
         * Since this tuple contains only one element, it returns that element.
         *
         * @return the single byte value in this tuple
         */
        @Override
        public byte max() {
            return _1;
        }

        /**
         * Returns the median byte value in this tuple.
         * Since this tuple contains only one element, it returns that element.
         *
         * @return the single byte value in this tuple
         */
        @Override
        public byte median() {
            return _1;
        }

        /**
         * Returns the sum of all byte values in this tuple.
         * Since this tuple contains only one element, it returns that element as an integer.
         *
         * @return the single byte value as an integer
         */
        @Override
        public int sum() {
            return _1;
        }

        /**
         * Returns the average of all byte values in this tuple.
         * Since this tuple contains only one element, it returns that element as a double.
         *
         * @return the single byte value as a double
         */
        @Override
        public double average() {
            return _1;
        }

        /**
         * Creates a new ByteTuple1 with the same element.
         * Since this tuple has only one element, reversing has no effect.
         *
         * @return a new ByteTuple1 with the same element
         */
        @Override
        public ByteTuple1 reverse() {
            return new ByteTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if the tuple's element equals valueToFind, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind;
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return the byte value as the hash code
         */
        @Override
        public int hashCode() {
            return _1;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple1 with the same element
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple1 other)) {
                return false;
            } else {
                return _1 == other._1;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return a string representation in the format "[element]"
         */
        @Override
        public String toString() {
            return "[" + _1 + "]";
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A ByteTuple containing exactly two byte elements.
     * Provides direct access to elements through public final fields {@code _1} and {@code _2}.
     */
    public static final class ByteTuple2 extends ByteTuple<ByteTuple2> {

        /** The first byte value stored in this tuple. */
        public final byte _1;
        /** The second byte value stored in this tuple. */
        public final byte _2;

        ByteTuple2() {
            this((byte) 0, (byte) 0);
        }

        ByteTuple2(final byte _1, final byte _2) {
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
         * Returns the minimum byte value in this tuple.
         *
         * @return the smaller of the two byte values
         */
        @Override
        public byte min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum byte value in this tuple.
         *
         * @return the larger of the two byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median byte value in this tuple.
         * For a tuple of two elements, returns the lower value.
         *
         * @return the median (lower) byte value
         */
        @Override
        public byte median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         *
         * @return the sum of both byte values as an integer
         */
        @Override
        public int sum() {
            return N.sum(_1, _2);
        }

        /**
         * Returns the average of all byte values in this tuple.
         *
         * @return the average of both byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2);
        }

        /**
         * Creates a new ByteTuple2 with the elements in reversed order.
         *
         * @return a new ByteTuple2 with elements swapped
         */
        @Override
        public ByteTuple2 reverse() {
            return new ByteTuple2(_2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if either element equals valueToFind, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         * Applies the given action to both elements of this tuple.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
         * tuple.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * }</pre>
         * 
         * @param <E> the type of exception that the action may throw
         * @param action the action to be performed on both elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.ByteBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given function to both elements of this tuple and returns the result.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
         * int sum = tuple.map((a, b) -> a + b); // 30
         * }</pre>
         * 
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the mapping function to apply to both elements
         * @return the result of applying the mapping function
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.ByteBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if it matches the given predicate,
         * otherwise returns an empty Optional.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
         * Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b); // Optional containing the tuple
         * }</pre>
         * 
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the predicate to test both elements
         * @return an Optional containing this tuple if the predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<ByteTuple2> filter(final Throwables.ByteBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return a hash code value calculated from both elements
         */
        @Override
        public int hashCode() {
            return 31 * _1 + _2;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple2 with the same elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple2 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return a string representation in the format "[element1, element2]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + "]";
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A ByteTuple containing exactly three byte elements.
     * Provides direct access to elements through public final fields {@code _1}, {@code _2}, and {@code _3}.
     */
    public static final class ByteTuple3 extends ByteTuple<ByteTuple3> {

        /** The first byte value stored in this tuple. */
        public final byte _1;
        /** The second byte value stored in this tuple. */
        public final byte _2;
        /** The third byte value stored in this tuple. */
        public final byte _3;

        ByteTuple3() {
            this((byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple3(final byte _1, final byte _2, final byte _3) {
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
         * Returns the minimum byte value in this tuple.
         *
         * @return the smallest of the three byte values
         */
        @Override
        public byte min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum byte value in this tuple.
         *
         * @return the largest of the three byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median byte value in this tuple.
         *
         * @return the middle byte value when sorted
         */
        @Override
        public byte median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         *
         * @return the sum of all three byte values as an integer
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3);
        }

        /**
         * Returns the average of all byte values in this tuple.
         *
         * @return the average of all three byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3);
        }

        /**
         * Creates a new ByteTuple3 with the elements in reversed order.
         *
         * @return a new ByteTuple3 with elements in reversed order
         */
        @Override
        public ByteTuple3 reverse() {
            return new ByteTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if any element equals valueToFind, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
        }

        /**
         * Applies the given action to all three elements of this tuple.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
         * tuple.accept((a, b, c) -> System.out.println(a + ", " + b + ", " + c));
         * }</pre>
         * 
         * @param <E> the type of exception that the action may throw
         * @param action the action to be performed on all three elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.ByteTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given function to all three elements of this tuple and returns the result.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
         * int sum = tuple.map((a, b, c) -> a + b + c); // 60
         * }</pre>
         * 
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the mapping function to apply to all three elements
         * @return the result of applying the mapping function
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.ByteTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if it matches the given predicate,
         * otherwise returns an empty Optional.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
         * Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c); // Optional containing the tuple
         * }</pre>
         * 
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the predicate to test all three elements
         * @return an Optional containing this tuple if the predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<ByteTuple3> filter(final Throwables.ByteTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return a hash code value calculated from all three elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * _1 + _2)) + _3;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple3 with the same elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple3 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * @return a string representation in the format "[element1, element2, element3]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + "]";
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A ByteTuple containing exactly four byte elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class ByteTuple4 extends ByteTuple<ByteTuple4> {

        /** The first byte value stored in this tuple. */
        public final byte _1;
        /** The second byte value stored in this tuple. */
        public final byte _2;
        /** The third byte value stored in this tuple. */
        public final byte _3;
        /** The fourth byte value stored in this tuple. */
        public final byte _4;

        ByteTuple4() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple4(final byte _1, final byte _2, final byte _3, final byte _4) {
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
         * Creates a new ByteTuple4 with the elements in reversed order.
         *
         * @return a new ByteTuple4 with elements in reversed order
         */
        @Override
        public ByteTuple4 reverse() {
            return new ByteTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if any element equals valueToFind, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A ByteTuple containing exactly five byte elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class ByteTuple5 extends ByteTuple<ByteTuple5> {

        /** The first byte value stored in this tuple. */
        public final byte _1;
        /** The second byte value stored in this tuple. */
        public final byte _2;
        /** The third byte value stored in this tuple. */
        public final byte _3;
        /** The fourth byte value stored in this tuple. */
        public final byte _4;
        /** The fifth byte value stored in this tuple. */
        public final byte _5;

        ByteTuple5() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple5(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5) {
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
         * Creates a new ByteTuple5 with the elements in reversed order.
         *
         * @return a new ByteTuple5 with elements in reversed order
         */
        @Override
        public ByteTuple5 reverse() {
            return new ByteTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if any element equals valueToFind, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A ByteTuple containing exactly six byte elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class ByteTuple6 extends ByteTuple<ByteTuple6> {

        /** The first byte value stored in this tuple. */
        public final byte _1;
        /** The second byte value stored in this tuple. */
        public final byte _2;
        /** The third byte value stored in this tuple. */
        public final byte _3;
        /** The fourth byte value stored in this tuple. */
        public final byte _4;
        /** The fifth byte value stored in this tuple. */
        public final byte _5;
        /** The sixth byte value stored in this tuple. */
        public final byte _6;

        ByteTuple6() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple6(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6) {
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
         * Creates a new ByteTuple6 with the elements in reversed order.
         *
         * @return a new ByteTuple6 with elements in reversed order
         */
        @Override
        public ByteTuple6 reverse() {
            return new ByteTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if any element equals valueToFind, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A ByteTuple containing exactly seven byte elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class ByteTuple7 extends ByteTuple<ByteTuple7> {

        /** The first byte value stored in this tuple. */
        public final byte _1;
        /** The second byte value stored in this tuple. */
        public final byte _2;
        /** The third byte value stored in this tuple. */
        public final byte _3;
        /** The fourth byte value stored in this tuple. */
        public final byte _4;
        /** The fifth byte value stored in this tuple. */
        public final byte _5;
        /** The sixth byte value stored in this tuple. */
        public final byte _6;
        /** The seventh byte value stored in this tuple. */
        public final byte _7;

        ByteTuple7() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple7(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7) {
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
         * Creates a new ByteTuple7 with the elements in reversed order.
         *
         * @return a new ByteTuple7 with elements in reversed order
         */
        @Override
        public ByteTuple7 reverse() {
            return new ByteTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if any element equals valueToFind, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A ByteTuple containing exactly eight byte elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class ByteTuple8 extends ByteTuple<ByteTuple8> {

        /** The first byte value stored in this tuple. */
        public final byte _1;
        /** The second byte value stored in this tuple. */
        public final byte _2;
        /** The third byte value stored in this tuple. */
        public final byte _3;
        /** The fourth byte value stored in this tuple. */
        public final byte _4;
        /** The fifth byte value stored in this tuple. */
        public final byte _5;
        /** The sixth byte value stored in this tuple. */
        public final byte _6;
        /** The seventh byte value stored in this tuple. */
        public final byte _7;
        /** The eighth byte value stored in this tuple. */
        public final byte _8;

        ByteTuple8() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple8(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7, final byte _8) {
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
         * Creates a new ByteTuple8 with the elements in reversed order.
         *
         * @return a new ByteTuple8 with elements in reversed order
         */
        @Override
        public ByteTuple8 reverse() {
            return new ByteTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if any element equals valueToFind, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A ByteTuple containing exactly nine byte elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class ByteTuple9 extends ByteTuple<ByteTuple9> {

        /** The first byte value stored in this tuple. */
        public final byte _1;
        /** The second byte value stored in this tuple. */
        public final byte _2;
        /** The third byte value stored in this tuple. */
        public final byte _3;
        /** The fourth byte value stored in this tuple. */
        public final byte _4;
        /** The fifth byte value stored in this tuple. */
        public final byte _5;
        /** The sixth byte value stored in this tuple. */
        public final byte _6;
        /** The seventh byte value stored in this tuple. */
        public final byte _7;
        /** The eighth byte value stored in this tuple. */
        public final byte _8;
        /** The ninth byte value stored in this tuple. */
        public final byte _9;

        ByteTuple9() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple9(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7, final byte _8, final byte _9) {
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
         * Creates a new ByteTuple9 with the elements in reversed order.
         *
         * @return a new ByteTuple9 with elements in reversed order
         */
        @Override
        public ByteTuple9 reverse() {
            return new ByteTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if any element equals valueToFind, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}